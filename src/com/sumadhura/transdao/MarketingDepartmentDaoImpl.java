package com.sumadhura.transdao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
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
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.itextpdf.text.pdf.hyphenation.TernaryTree.Iterator;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentCreationDetailsDto;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.TransportChargesDto;

import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.UIProperties;




@Repository("MarketingDepartmentDao")
public class MarketingDepartmentDaoImpl extends UIProperties implements
MarketingDepartmentDao {

	static Logger log = Logger.getLogger(DCFormDaoImpl.class);
	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	@Autowired
	CentralSiteIndentProcessDao cntlIndentrocss;

	@Autowired
	private IndentCreationDao icd;

	@Autowired
	@Qualifier("purchaseDeptIndentrocessDao")
	PurchaseDepartmentIndentProcessDao objPurchaseDepartmentIndentProcessDao;
	
	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public List<Map<String, Object>> getVendorOrSiteAddress(String siteId) {

		List<Map<String, Object>> dbIndentDts = null;
		String query = "select VENDOR_NAME,STATE,ADDRESS,MOBILE_NUMBER,GSIN_NUMBER,VENDOR_CON_PER_NAME,LANDLINE_NO,EMP_EMAIL from VENDOR_DETAILS where VENDOR_ID = ?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] { siteId });
		return dbIndentDts;
	}

	public static <K, V> Map<K, V> printMap(Map<K, V> map) {
		Map<K, V> mapObje = new TreeMap<K, V>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			mapObje.put( entry.getKey(), entry.getValue());

		}

		return mapObje;
	}
	/*=========================================================get Marketing Product Details start===============================================*/
	public Map<String, String> loadProds() {

		Map<String, String> products = null;
		List<Map<String, Object>> dbProductsList = null;

		products = new HashMap<String, String>();

		String prodsQry = "SELECT PRODUCT_ID, NAME FROM PRODUCT WHERE STATUS = 'A' AND PRODUCT_DEPT='MARKETING'";
		log.debug("Query to fetch product = "+prodsQry);

		dbProductsList = jdbcTemplate.queryForList(prodsQry, new Object[]{});

		for(Map<String, Object> prods : dbProductsList) {
			products.put(String.valueOf(prods.get("PRODUCT_ID")), String.valueOf(prods.get("NAME")));
		}
		return  printMap(products);
	}

	/*=========================================================get Marketing Product Details end===============================================*/


	@Override
	public int insertPOEntry(MarketingDeptBean productDetails) {
		String query = "INSERT INTO SUMADHURA_PO_ENTRY(PO_ENTRY_ID,PO_NUMBER,PO_DATE,VENDOR_ID,PO_STATUS,"
			+ "PO_ENTRY_USER_ID, SITE_ID,TERMS_CONDITIONS_ID,SUBJECT,BILLING_ADDRESS,VERSION_NUMBER,PO_ISSUE_DATE,REFFERENCE_NO,REVISION,OLD_PO_NUMBER,PREPARED_BY,DELIVERY_DATE,PAYMENT_REQ_DAYS,OPERATION_TYPE) values(? , ?, sysdate, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int result = jdbcTemplate.update(query,new Object[] {
				productDetails.getPoEntryId(),productDetails.getPoNumber(),
				productDetails.getVendorId(),"A",
				productDetails.getUserId(),productDetails.getSiteId(),
				"0",productDetails.getSubject(),productDetails.getBillingAddress(),
				productDetails.getVersionNo(),DateUtil.convertToJavaDateFormat(productDetails.getStrPoPrintRefdate()),
				productDetails.getRefferenceNo(),productDetails.getRevision_Number(),
				productDetails.getEdit_Po_Number(),productDetails.getPreparedBy(),
				StringUtils.isBlank(productDetails.getStrDeliveryDate()) ? null: DateUtil.convertToJavaDateFormat(productDetails.getStrDeliveryDate()),productDetails.getPayment_Req_days(),productDetails.getOperation_Type() });
		return result;
	}

	@Override
	public int insertTempPOEntry(MarketingDeptBean productDetails,String tempuserId, String ccEmailId, String subject,String selectedSite,String siteNumberOrNot)

	{
		String query = "INSERT INTO SUMADHURA_TEMP_PO_ENTRY(PO_ENTRY_ID,PO_NUMBER,PO_DATE,VENDOR_ID,PO_STATUS,"
			+ "PO_ENTRY_USER_ID, SITE_ID,TEMP_PO_PENDING_EMP_ID,CC_EMAIL_ID,SUBJECT,BILLING_ADDRESS,VERSION_NUMBER,PO_ISSUE_DATE,REFFERENCE_NO,REVISION,OLD_PO_NUMBER,PREPARED_BY,PASSWORD_MAIL,DELIVERY_DATE,TYPE_OF_PO,TYPE_OF_PO_DETAILS,PAYMENT_REQ_DAYS,OPERATION_TYPE) values(? , ?, sysdate, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int result = jdbcTemplate.update(query,new Object[] {productDetails.getPoNumber(),
				productDetails.getPoNumber(),productDetails.getVendorId(),"A",
				productDetails.getUserId(),productDetails.getSiteId(),
				tempuserId,ccEmailId,productDetails.getSubject(),productDetails.getBillingAddress(),
				productDetails.getVersionNo(),DateUtil.convertToJavaDateFormat(productDetails.getStrPoPrintRefdate()),
				productDetails.getRefferenceNo(),productDetails.getRevision_Number(),
				productDetails.getEdit_Po_Number(),productDetails.getPreparedBy(),
				productDetails.getPasswdForMail(),
				StringUtils.isBlank(productDetails.getStrDeliveryDate()) ? null: DateUtil.convertToJavaDateFormat(productDetails.getStrDeliveryDate()),
						selectedSite,siteNumberOrNot,productDetails.getPayment_Req_days(),productDetails.getOperation_Type()});

		return result;
	}

	/******************************************************** to get billing address *****************************************************************/
	public List<String> getBillingAddGstin(String receiverState) {
		List<String> billingAddressDetails = new ArrayList<String>();
		String billingAddress = "";
		String strBillingAddressGSTIN = "";
		String strBillingCompanyName = "";
		if (receiverState.equalsIgnoreCase("Telangana")) {

			billingAddress = validateParams.getProperty("BILLING_ADDRESSS_HYDERABAD");
			strBillingAddressGSTIN = validateParams.getProperty("GSTIN_HYDERABAD");
			strBillingCompanyName = validateParams.getProperty("BILLING_NAME");
		} else {

			billingAddress = validateParams.getProperty("BILLING_ADDRESSS_BENGALORE");
			strBillingAddressGSTIN = validateParams.getProperty("GSTIN_BENGALORE");
			strBillingCompanyName = validateParams.getProperty("BILLING_NAME");

		}
		billingAddressDetails.add(billingAddress);
		billingAddressDetails.add(strBillingAddressGSTIN);
		billingAddressDetails.add(strBillingCompanyName);

		return billingAddressDetails;

	}

	@Override
	public String getTemproryuser(String strUserId)

	{
		int result = 0;
		String tempUserId = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = " select SPAMD.APPROVER_EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL SPAMD  WHERE  SPAMD.EMP_ID= ? and SPAMD.STATUS = 'A' AND SPAMD.MODULE_TYPE='MARKETING_PO'";
		dbIndentDts = jdbcTemplate.queryForList(query,new Object[] { strUserId });
		if (dbIndentDts != null) {
			for (Map<String, Object> prods : dbIndentDts) {
				tempUserId = prods.get("APPROVER_EMP_ID") == null ? "" : prods.get("APPROVER_EMP_ID").toString();

			}
		}
		return tempUserId;

	}

	/*
	 *  ****************************************to get perminent po
	 * number********
	 * *****************************************************************
	 */
	public String getPermenentPoNumber(String poState, String type,String siteId, String financialyear) {
		String strPONumber = "";
		String dbState = "";
		String strState = "";// this is for po number purpose to take which

		if (poState.equalsIgnoreCase("Telangana")) {
			dbState = "MKRT_PO_HYD";
			strState = "HYD";
		} else {
			dbState = "MKRT_PO_BNG";
			strState = "BNG";
		}
		List<String> yearWiseAndInfinityNumber = getPoEnterSeqNoOrMaxId(dbState); // PO_NUMBER

		int intHOWiseYearwiseNumber = Integer.valueOf(yearWiseAndInfinityNumber.get(0));
		int intHOWiseInfinityNumber = Integer.valueOf(yearWiseAndInfinityNumber.get(1));
		strPONumber = "PO/SIPL/" + strState + "/"+ String.valueOf(intHOWiseInfinityNumber) + "/" + financialyear+ "/" + intHOWiseYearwiseNumber;
		getUpdatePoNumberGeneratorHeadOfficeWise(intHOWiseInfinityNumber + 1,intHOWiseYearwiseNumber + 1, dbState); // to increase head office wise number

		return strPONumber;

	}

	public List<String> getPoEnterSeqNoOrMaxId(String poState) {
		List<Map<String, Object>> dbIndentDts = null;
		List<String> poList = new ArrayList<String>();
		String yearWiseNumber = "";
		String infinityNumber = "";
		String query = "select YEARWISE_NUMBER,INFINITY_NUMBER from SUMADHURA_HO_WISE_PO_NUMBER where SERVICE_NAME=? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] { poState });
		for (Map<String, Object> mails : dbIndentDts) {
			yearWiseNumber = mails.get("YEARWISE_NUMBER") == null ? "" : mails.get("YEARWISE_NUMBER").toString();
			infinityNumber = mails.get("INFINITY_NUMBER") == null ? "" : mails.get("INFINITY_NUMBER").toString();
		}
		poList.add(yearWiseNumber);
		poList.add(infinityNumber);

		return poList;
	}
	public int getUpdatePoNumberGeneratorHeadOfficeWise(int infinityNumber,int yearWiseNumber, String serviceName) {

		String query = "update SUMADHURA_HO_WISE_PO_NUMBER set INFINITY_NUMBER = ?,YEARWISE_NUMBER=? where SERVICE_NAME = ?";
		int result = jdbcTemplate.update(query, new Object[] { infinityNumber,yearWiseNumber, serviceName });
		return result;
	}

	/*===================================================site wise getState=====================================================*/
	public Set<String> getLocationDetails() {

		String siteNames = "";
		String sql = "";
		Set<String> hash_Set = new HashSet<String>(); 
		List<Map<String, Object>> productList = null;
		final StringBuffer xmlData = new StringBuffer();
		StringBuffer bufferMainProd = new StringBuffer();
		//Map<String, String> map = null;
		JdbcTemplate template = null;

		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			sql = "SELECT DISTINCT(ADDRESS) FROM SITE WHERE STATUS='ACTIVE'";
			productList = template.queryForList(sql, new Object[] {});
			if (null != productList && productList.size() > 0) {
				for (Map siteId : productList) {
					siteNames = siteId.get("ADDRESS") == null ? "" : siteId.get("ADDRESS").toString();
					if(siteNames!=null && !siteNames.equals("")){
						hash_Set.add(siteNames);	
					}

				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return hash_Set;
	}

	@Override
	public String loadAndSetSiteInfo(String siteName) {
		//	System.out.println("in dao:"+employeeName);
		StringBuffer sb = null;
		List<Map<String, Object>> dbSiteList = null;		
		siteName = siteName.replace("$$", "&");
		siteName = siteName.toUpperCase();

		log.debug("Vendor Name = "+siteName);

		sb = new StringBuffer();

		String contractorInfoQry = "SELECT SITE_NAME FROM SITE WHERE upper(SITE_NAME) like '%"+siteName+"%' ";
		log.debug("Query to fetch employee info = "+contractorInfoQry);

		dbSiteList = jdbcTemplate.queryForList(contractorInfoQry, new Object[]{});

		for(Map<String, Object> siteInfo : dbSiteList) {
			sb = sb.append(String.valueOf(siteInfo.get("SITE_NAME")));
			sb = sb.append("@@");
		}	
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 2);
		}
		//System.out.println("Hai "+sb.toString());

		return sb.toString();
	}
	// transportaion details perminent save 
	@Override
	public int insertPOTransportDetails(int poTransChrgsSeqNo, MarketingDeptBean productDetails, TransportChargesDto transportChargesDto) {
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
				productDetails.getSiteId(),
				productDetails.getPoEntryId(),
				productDetails.getIndentNo()
		});
		return result;

	}
	// save transporatation details in temporary
	@Override
	public int insertPOTempTransportDetails(int poTransChrgsSeqNo, MarketingDeptBean productDetails, TransportChargesDto transportChargesDto) {
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
				productDetails.getSiteId(),
				productDetails.getPoNumber(),
				productDetails.getIndentNo()
		});
		return result;

	}

	// calculate gst amount
	public List<String> calculateGstAmount(HttpServletRequest request,String tax,String strVendorGSTIN,String receiverState,String taxAmount){
		List<String> listOfDetails = new ArrayList<String>();
		double CGSTAMT = 0.0;
		double SGSTAMT = 0.0;
		double IGSTAMT = 0.0;
		String strCGSTAMT="0";
		String strSGSTAMT="0";
		String strIGSTAMT="0";
		String CGST = "";
		String SGST = "";
		String IGST = "";
		double percent=0.0;
		double amt=0.0;
		if (receiverState.equalsIgnoreCase("Telangana")) {

			if (strVendorGSTIN.startsWith("36")) {
				request.setAttribute("isCGSTSGST", "true");// this is

				if (tax.equals("0")) {
					SGST = CGST = "0";
					//SGST = "0";
				} else {
					percent = Double.parseDouble(tax) / 2;
					amt = Double.parseDouble(taxAmount) / 2;
					CGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
					SGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
					CGST = String.valueOf(percent);
					SGST = String.valueOf(percent);
					strCGSTAMT = String.format("%.2f", CGSTAMT);
					strSGSTAMT = String.format("%.2f", SGSTAMT);
				}


			} else {

				request.setAttribute("isCGSTSGST", "false");// this is
				percent = Double.parseDouble(tax);
				amt = Double.parseDouble(taxAmount);
				IGST = String.valueOf(percent);
				IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
				strIGSTAMT = String.format("%.2f", IGSTAMT);

			}
		} else {

			if (strVendorGSTIN.startsWith("29")) {
				request.setAttribute("isCGSTSGST", "true");// this is
				if (tax.equals("0")) {
					CGST = "0";
					SGST = "0";
				} else {
					percent = Double.parseDouble(tax) / 2;
					amt = Double.parseDouble(taxAmount) / 2;
					CGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
					SGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
					CGST = String.valueOf(percent);
					SGST = String.valueOf(percent);
					strCGSTAMT = String.format("%.2f", CGSTAMT);
					strSGSTAMT = String.format("%.2f", SGSTAMT);
				}


			} else {

				request.setAttribute("isCGSTSGST", "false");// this is
				percent = Double.parseDouble(tax);
				amt = Double.parseDouble(taxAmount);
				IGST = String.valueOf(percent);
				IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
				strIGSTAMT = String.format("%.2f", IGSTAMT);


			}
		}
		listOfDetails.add(CGST);listOfDetails.add(strCGSTAMT);listOfDetails.add(SGST);listOfDetails.add(strSGSTAMT);
		listOfDetails.add(IGST);listOfDetails.add(strIGSTAMT);
		return listOfDetails;
	}

	// save product details
	@Override
	public int insertPOEntryDetails(MarketingDeptBean productDetails,int poEntrySeqNo)
	{
		List<Integer> resuIntegers=new ArrayList<Integer>();
		int po_EntrydetailsId=	jdbcTemplate.queryForObject("select SUMADHURA_PO_ENTRY_DTLS_SEQ.nextval from dual", Integer.class);

		String query = "INSERT INTO SUMADHURA_PO_ENTRY_DETAILS(PO_ENTRY_DETAILS_ID,PO_ENTRY_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,"+
		"MEASUR_MNT_ID, PO_QTY,ENTRY_DATE,PRICE,BASIC_AMOUNT,TAX,TAX_AMOUNT,AMOUNT_AFTER_TAX,OTHER_CHARGES,OTHER_CHARGES_AFTER_TAX,TOTAL_AMOUNT,HSN_CODE,TAX_ON_OTHER_TRANSPORT_CHG,DISCOUNT,AMOUNT_AFTER_DISCOUNT,VENDOR_PRODUCT_DESC,PD_PRODUCT_DESC"
		+ ") values(?, ?, ?, ?, ?, ?, ?,sysdate, ?, ?, ?, ?,?, ?, ?, ?, ?, ?, ?,?,?,?)";

		int result = jdbcTemplate.update(query, new Object[] {po_EntrydetailsId,
				poEntrySeqNo,
				productDetails.getProductId(),productDetails.getSub_ProductId(),
				productDetails.getChild_ProductId(),productDetails.getMeasurementId(),
				productDetails.getQuantity(),productDetails.getPricePerUnit(),productDetails.getBasicAmt(),
				productDetails.getTax(),
				productDetails.getTaxAmount(),productDetails.getAmountAfterTax(),
				productDetails.getOtherOrTransportCharges1(),productDetails.getOtherOrTransportChargesAfterTax1(),
				productDetails.getTotalAmount(),productDetails.getHsnCode(),productDetails.getTaxOnOtherOrTransportCharges1(),
				productDetails.getStrDiscount(),productDetails.getStrAmtAfterDiscount(),
				productDetails.getChildProductCustDisc(),productDetails.getChild_ProductName()

		});
		return po_EntrydetailsId;
	}
	// temp data save 
	@Override
	public int insertTempPOEntryDetails(MarketingDeptBean productDetails,String poEntrySeqNo)
	{
		List<Integer> resuIntegers=new ArrayList<Integer>();
		int po_EntrydetailsId=	jdbcTemplate.queryForObject("select SUMADHURA_T_PO_ENTRY_DTLS_SEQ.nextval from dual", Integer.class);

		String query = "INSERT INTO SUMADHURA_TEMP_PO_ENTRY_DTLS(PO_ENTRY_DETAILS_ID,PO_ENTRY_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,"+
		"MEASUR_MNT_ID, PO_QTY,ENTRY_DATE,PRICE,BASIC_AMOUNT,TAX,TAX_AMOUNT,AMOUNT_AFTER_TAX,OTHER_CHARGES,OTHER_CHARGES_AFTER_TAX,TOTAL_AMOUNT,HSN_CODE,TAX_ON_OTHER_TRANSPORT_CHG,DISCOUNT,AMOUNT_AFTER_DISCOUNT,VENDOR_PRODUCT_DESC,PD_PRODUCT_DESC"
		+ ") values(?, ?, ?, ?, ?, ?, ?,sysdate, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

		int result = jdbcTemplate.update(query, new Object[] {po_EntrydetailsId,
				poEntrySeqNo,
				productDetails.getProductId(),productDetails.getSub_ProductId(),
				productDetails.getChild_ProductId(),productDetails.getMeasurementId(),
				productDetails.getQuantity(),productDetails.getPricePerUnit(),productDetails.getBasicAmt(),
				productDetails.getTax(),
				productDetails.getTaxAmount(),productDetails.getAmountAfterTax(),"0","0",
				productDetails.getTotalAmount(),productDetails.getHsnCode(),"0",
				productDetails.getStrDiscount(),productDetails.getStrAmtAfterDiscount(),
				productDetails.getChildProductCustDisc(),productDetails.getChild_ProductName()

		});
		return po_EntrydetailsId;
	}
	@Override
	public int insertPoAreaWiseData(int poEntryId ,String type_Po,String location_Wise)
	{
		String query = "INSERT INTO MRKT_PO_AREA_WISE_DTLS(PO_ENTRY_ID,TYPE_OF_PO,TYPE_OF_PO_DETAILS) values(?,?,?)";

		int result = jdbcTemplate.update(query, new Object[] {poEntryId,type_Po,location_Wise});
		return result;
	}

	// save data in PERMINENT location table
	@Override
	public int saveLocationdetailsData(MarketingDeptBean productDetails,String po_entryDetailsId,String poNumber)
	{
		String query = "INSERT INTO MRKT_PO_PROD_LOC_DTLS(CHILD_PRODUCT_ID,LOCATION_ID,HOARDING_DATE,TIME,"+
		"QUANTITY,CREATION_DATE,PO_ENTRY_DETAILS_ID,PO_NUMBER,PO_PROD_LOC_DTLS_ID,SITE_ID,AMOUNT_PER_UNIT_AFTER_TAX,TOTAL_AMOUNT,PRODUCT_SERIAL_NO) values(?, ?, ?,?, ?,sysdate,?,?,MRKT_PO_PROD_LOC_DTL_SEQ.nextval,?,?,?,?)";

		int result = jdbcTemplate.update(query, new Object[] {
				productDetails.getChild_ProductId(),
				productDetails.getLocation(),
				StringUtils.isBlank(productDetails.getFromDate()) ? null : DateUtil.convertToJavaDateFormat(productDetails.getFromDate()),

						productDetails.getTime(),
						productDetails.getQuantity(),po_entryDetailsId,poNumber,Integer.parseInt(productDetails.getSiteId()),
						productDetails.getPricePerUnit(),
						productDetails.getTotalAmount(),productDetails.getIntSerialNo()

		});
		return result;
	}

	// SAVE DATA IN TEMP TABLE LOCATION TABLE
	public int saveTempLocationdetailsData(MarketingDeptBean productDetails,String po_entryDetailsId,String poNumber)
	{ 
		String query = "INSERT INTO MRKT_TEMP_PO_PROD_LOC_DTLS(CHILD_PRODUCT_ID,LOCATION_ID,HOARDING_DATE,TIME,"+
		"QUANTITY,CREATION_DATE,PO_ENTRY_DETAILS_ID,PO_NUMBER,TEMP_PO_PROD_LOC_DTLS_ID,SITE_ID,AMOUNT_PER_UNIT_AFTER_TAX,TOTAL_AMOUNT,PRODUCT_SERIAL_NO) values(?, ?, ?, ?, ?,sysdate,?,?,MRKT_TEMP_PO_PROD_LOC_DTL_SEQ.nextval,?,?,?,?)";

		int result = jdbcTemplate.update(query, new Object[] {
				productDetails.getChild_ProductId(),
				productDetails.getLocation(),
				StringUtils.isBlank(productDetails.getFromDate()) ? null : DateUtil.convertToJavaDateFormat(productDetails.getFromDate()),
						productDetails.getTime(),
						productDetails.getQuantity(),po_entryDetailsId,poNumber,Integer.parseInt(productDetails.getSiteId()),
						productDetails.getPricePerUnit(),
						productDetails.getTotalAmount(),productDetails.getIntSerialNo()


		});
		return result;
	}

	// get billing address purpose we can write
	public String getDeptId(String userId){
		String state ="";
		String deptId = "";
		String sql="select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID='"+userId+"'";
		deptId = jdbcTemplate.queryForObject(sql, String.class);
		if(deptId!=null && !deptId.equals("")){
			String query="select DEPT_NAME from SUMADHURA_DEPARTMENT_DETAILS where DEPT_ID='"+deptId+"'";
			state = jdbcTemplate.queryForObject(query, String.class);	
		}
		return state;
	}

	/*	=====================================================================set location area data===================================================*/
	@Override
	public Map<String, String> loadAndSetLocationData(String childProductId) {
		//	System.out.println("in dao:"+employeeName);
		StringBuffer sb = null;
		List<Map<String, Object>> dbAreaList = null;		
		childProductId = childProductId.replace("$$", "&");
		childProductId = childProductId.toUpperCase();
		String hoardingId="";
		String hoardingName="";
		Map<String, String> Area = new TreeMap<String, String>();

		log.debug("Vendor Name = "+childProductId);
		sb = new StringBuffer();

		String locationAreaInfoQry = "SELECT HOARDING_ID,HOARDING_AREA FROM MRKT_HOARDING_DTLS WHERE CHILD_PRODUCT_ID=? ";
		log.debug("Query to fetch employee info = "+locationAreaInfoQry);

		dbAreaList = jdbcTemplate.queryForList(locationAreaInfoQry, new Object[]{childProductId});
		for(Map<String, Object> prods : dbAreaList) {
			hoardingId=prods.get("HOARDING_ID")==null ? "" : prods.get("HOARDING_ID").toString();
			hoardingName=prods.get("HOARDING_AREA")==null ? "" : prods.get("HOARDING_AREA").toString();

			Area.put(hoardingId,hoardingName);
		}
		return Area;
	}

	/*================================================================Temp Po Page Approve =========================================================*/

	@Override
	public List<IndentCreationBean> ViewPoPendingforApproval(String fromDate, String toDate, String strUserId,String tempPoNumber) {
		String query = "";
		//String strDCFormQuery = "";
		//String strDCNumber = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		IndentCreationBean indentObj = null; 
		String old_Po_Number="";
		//String type_Of_Purchase="";
		try {
			//if part is for view indent receive details,else part is for view indent issue details
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID FROM SUMADHURA_TEMP_PO_ENTRY STPE, SITE S WHERE STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
				//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			} else if (StringUtils.isNotBlank(fromDate)) {
				query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID FROM SUMADHURA_TEMP_PO_ENTRY STPE, SITE S WHERE STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE) >=TO_DATE('"+fromDate+"', 'dd-MM-yy')";


			} else if(StringUtils.isNotBlank(toDate)) {
				query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID FROM SUMADHURA_TEMP_PO_ENTRY STPE, SITE S WHERE STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and TRUNC(STPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
			}else if(StringUtils.isNotBlank(tempPoNumber)) {
				query = "SELECT DISTINCT (STPE.PO_NUMBER),STPE.PO_DATE,S.SITE_NAME,STPE.PREPARED_BY,STPE.OLD_PO_NUMBER,STPE.SITE_ID FROM SUMADHURA_TEMP_PO_ENTRY STPE, SITE S WHERE STPE.TEMP_PO_PENDING_EMP_ID='"+strUserId+"' and STPE.PO_STATUS='A' and  S.SITE_ID =  STPE.SITE_ID and NVL(STPE.VIEWORCANCEL, ' ') != 'CANCEL' and  STPE.PO_NUMBER='"+tempPoNumber+"'";

			}


			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});

			for(Map<String, Object> prods : dbIndentDts) {
				indentObj = new IndentCreationBean();

				indentObj.setPonumber(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());
				//	indentObj.setStrInvoiceDate(prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString());
				indentObj.setStatus(prods.get("PO_STATUS")==null ? "" : prods.get("PO_STATUS").toString());
				indentObj.setSiteName(prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString());
				indentObj.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "0" : prods.get("SITE_ID").toString()));
				indentObj.setType_Of_Purchase(prods.get("PREPARED_BY")==null ? "" : prods.get("PREPARED_BY").toString());
				old_Po_Number=(prods.get("OLD_PO_NUMBER")==null ? "" : prods.get("OLD_PO_NUMBER").toString());
				//indentObj.setType_Of_Purchase
				/*if(type_Of_Purchase.equals("") && !old_Po_Number.equals("") || (type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && !old_Po_Number.equals(""))){

					indentObj.setType_Of_Purchase("REVISED_PO");
				}
				else if(type_Of_Purchase.equals("") || (type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && old_Po_Number.equals(""))){

					indentObj.setType_Of_Purchase("PURCHASE_DEPT_PO");
				} else{

					indentObj.setType_Of_Purchase("MARKETING_DEPT");
				}*/

				String date=prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString();
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

		} finally {
			query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;
	}
	/*===========================================================show temp po details start=====================================================================*/
	// to get product  details for temp po show 
	public String getPendingVendorDetails(String poNumber, String siteId,HttpServletRequest request,String siteName) {
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> deleiverAddress=null;
		Map<String, String> Names = null;
		String tblOneData="";
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

		String deliverySiteName="";
		String deliverySiteAddress="";
		String deliverySiteMobileNo="";
		String deliverySiteGSTIN="";
		String deliverySiteState="";
		String landLineNumber="";
		String emailId="";
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
		String type_Of_Po="";
		String type_Of_Po_Details="";
		String userId="";
		String old_PO_EntryId="";
		String old_PO_Date="";
		

		if (StringUtils.isNotBlank(poNumber) ) {
			String query = "SELECT STPE.PO_ENTRY_ID,STPE.BILLING_ADDRESS,STPE.TYPE_OF_PO,STPE.TYPE_OF_PO_DETAILS,STPE.SUBJECT,STPE.PO_DATE,STPE.VENDOR_ID,VD.VENDOR_CON_PER_NAME,VD.VENDOR_NAME,VD.ADDRESS, VD.MOBILE_NUMBER,VD.GSIN_NUMBER,VD.STATE,STPE.CC_EMAIL_ID,STPE.SITE_ID,VD.LANDLINE_NO,VD.EMP_EMAIL,STPE.VERSION_NUMBER,STPE.PO_ISSUE_DATE,STPE.REFFERENCE_NO,STPE.REVISION,STPE.OLD_PO_NUMBER,STPE.PREPARED_BY,STPE.DELIVERY_DATE,STPE.PO_ENTRY_USER_ID FROM VENDOR_DETAILS VD,SUMADHURA_TEMP_PO_ENTRY STPE WHERE STPE.VENDOR_ID =VD.VENDOR_ID  AND  STPE.PO_ENTRY_ID=?";
			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{poNumber});

		}
		if (null != dbIndentDts && dbIndentDts.size() > 0) {
			try{
				for (Map<?, ?> IndentGetBean : dbIndentDts) {

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
					userId=IndentGetBean.get("PO_ENTRY_USER_ID") == null ? "-" : IndentGetBean.get("PO_ENTRY_USER_ID").toString(); // 
					
					 type_Of_Po=IndentGetBean.get("TYPE_OF_PO") == null ? "-" : IndentGetBean.get("TYPE_OF_PO").toString();
					 type_Of_Po_Details=IndentGetBean.get("TYPE_OF_PO_DETAILS") == null ? "-" : IndentGetBean.get("TYPE_OF_PO_DETAILS").toString();

					

					version_Number=IndentGetBean.get("VERSION_NUMBER") == null ? "-" : IndentGetBean.get("VERSION_NUMBER").toString();
					poRefferenceDate=IndentGetBean.get("PO_ISSUE_DATE") == null ? "-" : IndentGetBean.get("PO_ISSUE_DATE").toString();
					refference_No=IndentGetBean.get("REFFERENCE_NO") == null ? "-" : IndentGetBean.get("REFFERENCE_NO").toString();
					// below data is used at the time of revised po
					revision_No=IndentGetBean.get("REVISION") == null ? " " : IndentGetBean.get("REVISION").toString();
					old_Po_Number=IndentGetBean.get("OLD_PO_NUMBER") == null ? "-" : IndentGetBean.get("OLD_PO_NUMBER").toString();
					preparedBy=IndentGetBean.get("PREPARED_BY") == null ? "-" : IndentGetBean.get("PREPARED_BY").toString();
					strDeliveryDate=IndentGetBean.get("DELIVERY_DATE") == null ? "-" : IndentGetBean.get("DELIVERY_DATE").toString();

					if(!poRefferenceDate.equals("-")){
						DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
						DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
						Date poReffDate = inputFormat.parse(poRefferenceDate);
						poRefferenceDate=outputFormat.format(poReffDate);
						if(StringUtils.isNotBlank(strDeliveryDate) && !strDeliveryDate.equals("-")){
							Date deliveryDate = inputFormat.parse(strDeliveryDate);
							strDeliveryDate=outputFormat.format(deliveryDate);
						}
					}
					request.setAttribute("versionNo", version_Number);
					request.setAttribute("refferenceNo", refference_No);
					request.setAttribute("strPoPrintRefdate",poRefferenceDate);

					billingAddressCompanyName=validateParams.getProperty("BILLING_NAME");
					String strReceiveState=getDeptId(userId);
					request.setAttribute("billingState",strReceiveState);
					request.setAttribute("strReceiverBillingState",strReceiveState);
					if(billingAddress.contains("Sumadhura Infracon pvt Ltd")){
						billingAddress=billingAddress.replace("Sumadhura Infracon pvt Ltd,","");
					}
					if(!mobilenumber.equals("")){
						contactPersonName = contactPersonName+" ( "+mobilenumber;
					}if(!landLineNumber.equals("")){
						contactPersonName = contactPersonName+","+landLineNumber;
					}

					if(type_Of_Po.equalsIgnoreCase("SiteWise") && !type_Of_Po_Details.equals("-")){
						strSiteId=type_Of_Po_Details;
					}
					contactPersonName = " "+contactPersonName +" )";
					state=state+". Email Id : "+emailId;


					int tempiPOnumber = Integer.parseInt(ponumber);
					Names=getPOApproveCreateEmp(tempiPOnumber,request); // get signatures of create or approval time 

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
					
					/******************************************update po time need po date along with poentry id so written this one**********************/
					if(old_Po_Number!=null && !old_Po_Number.equals("") && !old_Po_Number.equals("-") && old_Po_Number.startsWith("PO/SIPL")){
					//Date date = null;
					
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
						deleiverAddress=getVendorOrSiteAddress(strSiteId);
					}
					if (null != dbIndentDts && dbIndentDts.size() > 0) {
						for (Map<?, ?> deleiverAddressMap : deleiverAddress) {
							deliverySiteName = deleiverAddressMap.get("VENDOR_NAME") == null ? "" : deleiverAddressMap.get("VENDOR_NAME").toString();
							deliverySiteAddress = deleiverAddressMap.get("ADDRESS") == null ? "" : deleiverAddressMap.get("ADDRESS").toString();
							deliverySiteState=	 deleiverAddressMap.get("STATE") == null ? "" : deleiverAddressMap.get("STATE").toString();

							request.setAttribute("deliverySiteState",deliverySiteState);
						}

					}
					if(strReceiveState.equalsIgnoreCase("Telangana")){
						strBillingAddressGSTIN=validateParams.getProperty("GSTIN_HYDERABAD");

					}else{
						strBillingAddressGSTIN=validateParams.getProperty("GSTIN_BENGALORE");
					}
					if(!type_Of_Po.equalsIgnoreCase("SiteWise") && !type_Of_Po_Details.equals("-")){
						List<String> projectData=getProjectAddGstin(strReceiveState); // to get receiver address
						deliverySiteName=projectData.get(1);
						deliverySiteAddress=projectData.get(0);
						//deliverySiteState=projectData.get(2);
						
						request.setAttribute("deliverySiteState",strReceiveState);
						
					}
						
					tblOneData+= ponumber+"@@"+podate+"@@"+""+"@@"+vendorname+"@@"+address+"@@"+state+"@@"+gstinnumber+
					"@@"+deliverySiteName+"@@"+deliverySiteAddress+"@@"+""+"@@"+""+"@@"+""+"@@"+subject+"@@"+contactPersonName+
					"@@"+ccEmailId+"@@"+billingAddress+"@@"+preparedName+"@@"+preparedDate+"@@"+""+"@@"+siteId+"@@"+verifyName+"@@"+verifyDate+"@@"+""+"@@"+""+"@@"+billingAddressCompanyName+
					"@@"+strDeliveryAddressContactPerson+"@@"+deliveryAdddressLandLineNumber+"@@"+strBillingAddressGSTIN+"@@"+siteId+"@@"+siteName+"@@"+vendorid+"@@"+revision_No+"@@"+old_Po_Number+"@@"+strDeliveryDate+"@@"+preparedBy;

				}
				request.setAttribute("gstinumber",gstinnumber);
				request.setAttribute("old_PO_EntryId",old_PO_EntryId);
				request.setAttribute("old_PO_Date",old_PO_Date);
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return tblOneData;

	}

	// to get product  details for temp po show 

	public String getPendingProductDetails(String poNumber, String siteId,HttpServletRequest request,String deliverySiteState) {
		List<Map<String, Object>> GetproductDetailsList = null;

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

		try{
			if (StringUtils.isNotBlank(poNumber) ) {

				sql+="select (P.NAME)AS PRODUCT_NAME,SP.NAME AS SUB_PRODUCT,CP.NAME AS CHILD_PRODUCT,M.NAME,SPED.PRODUCT_ID,SPED.SUB_PRODUCT_ID,SPED.CHILD_PRODUCT_ID,SPED.MEASUR_MNT_ID, "
					+" SPED.PO_QTY,SPED.TAX,SPED.TAX_AMOUNT,SPED.AMOUNT_AFTER_TAX,SPED.BASIC_AMOUNT,SPED.PRICE, SPED.HSN_CODE,SPED.TOTAL_AMOUNT,SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,SPED.VENDOR_PRODUCT_DESC ,IGST.TAX_PERCENTAGE , "
					+" SPED.PD_PRODUCT_DESC,SPE.TEMP_PO_PENDING_EMP_ID  from SUMADHURA_TEMP_PO_ENTRY SPE,SUMADHURA_TEMP_PO_ENTRY_DTLS SPED, SUB_PRODUCT SP,"
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
					//request.setAttribute("strReceiverBillingState",strReceiveState);
					String gstinState=request.getAttribute("strReceiverBillingState").toString();
					List<String> gstDetails=calculateGstAmount(request,tax,gstinumber,gstinState,taxamount);

					tblTwoData += sno+"@@"+subproduct+"@@"+childproduct+"@@"+hsncode+"@@"+measurement+"@@"+
					quantity+"@@"+price+"@@"+basicamount+"@@"+discount+"@@"+discountaftertax+"@@"+gstDetails.get(0)+"%"+"@@"+gstDetails.get(1)+"@@"
					+gstDetails.get(2)+"%"+"@@"+gstDetails.get(3)+"@@"+gstDetails.get(4) + "%" +"@@"+gstDetails.get(5)+"@@"+amountaftertax+"@@"+amountaftertax+"@@"
					+strroundoff+"@@"+strGrandVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+strTotalVal+"@@"+vendorProductDesc+"@@"
					+strIndentCreationDetailsId+"@@"+product_name+"@@"+productId+"@@"+sub_productId+"@@"+child_productId+"@@"+measurementId+"@@"+indentCreationDetailsId+"@@"+userId+"&&";


				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tblTwoData;

	}


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
		String strTableThreeData="";
		String response="success";
		String termscondition="";

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

						//char firstLetterChar = gstinumber.charAt(0);
						//char secondLetterChar=gstinumber.charAt(1);
						if(GSTTax.contains("%")){
							String data[] = GSTTax.split("%");
							GSTTax=data[0];

						}
						String gstinState=request.getAttribute("strReceiverBillingState").toString();
						List<String> gstDetails=calculateGstAmount(request,GSTTax,gstinumber,gstinState,GSTAmount);
						strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+gstDetails.get(0)+"%"+"@@"+gstDetails.get(1)+"@@"+gstDetails.get(2)+"%"+"@@"+gstDetails.get(3)+"@@"+gstDetails.get(4)+"%"+"@@"+gstDetails.get(5)+"&&";				
					}
				}

				if(response=="success"){
					String sql1="SELECT TERMS_CONDITION_DESC FROM  SUMADHURA_TEMP_PO_TERMS_COND WHERE PO_NUMBER=?";
					termList = jdbcTemplate.queryForList(sql1, new Object[] {poNumber});
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
	/*=======================================================show temp po details end====================================================================	*/
	/*====================================================show temp po location details start========================================================*/
	public String getPendingTempLocationList(String poNumber) {
		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> termList = null;
		List<String> listOfTermsAndConditions = new ArrayList<String>();

		String sql="";
		String childProductName="";
		String Location="";
		String fromDate="";
		String toDate="";
		String time="";
		String locationData="";
		String response="success";
		String quantity="";
		String siteName="";
		String amountPerUnit="";
		String totalAmount="";

		try {

			if (StringUtils.isNotBlank(poNumber) ) {
				sql += "SELECT (C.NAME)AS CHILD_PROD_NAME,min(MPDLD.HOARDING_DATE) as fromDate,"
					+" max(MPDLD.HOARDING_DATE) as todate,MPDLD.TIME,MPDLD.QUANTITY,S.SITE_NAME,MPDLD.AMOUNT_PER_UNIT_AFTER_TAX, MPDLD.PRODUCT_SERIAL_NO,sum(MPDLD.TOTAL_AMOUNT) AS TOTAL,MHD.HOARDING_AREA "
					+" from CHILD_PRODUCT C,SITE S,MRKT_TEMP_PO_PROD_LOC_DTLS MPDLD"
					+" LEFT OUTER JOIN MRKT_HOARDING_DTLS MHD ON MHD.HOARDING_ID=MPDLD.LOCATION_ID "
					+" where MPDLD.PO_NUMBER=? AND MPDLD.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID " 
					+" AND S.SITE_ID=MPDLD.SITE_ID   group by C.NAME,MHD.HOARDING_AREA,MPDLD.TIME,"
					+" MPDLD.QUANTITY,S.SITE_NAME,MPDLD.AMOUNT_PER_UNIT_AFTER_TAX,MPDLD.TOTAL_AMOUNT,MPDLD.PRODUCT_SERIAL_NO";
          
				productList = jdbcTemplate.queryForList(sql, new Object[] {poNumber});

				if (null != productList && productList.size() > 0) {
					for (Map<?, ?> GetLocationDetails : productList) {
						childProductName = GetLocationDetails.get("CHILD_PROD_NAME") == null ? "-" : GetLocationDetails.get("CHILD_PROD_NAME").toString();	
						Location = GetLocationDetails.get("HOARDING_AREA") == null ? "-" : GetLocationDetails.get("HOARDING_AREA").toString();	
						fromDate = GetLocationDetails.get("fromDate") == null ? "-" : GetLocationDetails.get("fromDate").toString();
						toDate = GetLocationDetails.get("todate") == null ? "-" : GetLocationDetails.get("todate").toString();
						time = GetLocationDetails.get("TIME") == null ? "-" : GetLocationDetails.get("TIME").toString();
						quantity=GetLocationDetails.get("QUANTITY") == null ? "-" : GetLocationDetails.get("QUANTITY").toString();
						siteName=GetLocationDetails.get("SITE_NAME") == null ? "-" : GetLocationDetails.get("SITE_NAME").toString();
						//amountPerUnit=GetLocationDetails.get("AMOUNT_PER_UNIT_AFTER_TAX") == null ? "0" : GetLocationDetails.get("AMOUNT_PER_UNIT_AFTER_TAX").toString();
						//totalAmount=GetLocationDetails.get("TOTAL") == null ? "0" : GetLocationDetails.get("TOTAL").toString();

						SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
						Date from_Date = input.parse(fromDate); 
						Date to_Date = input.parse(toDate);
						SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
						fromDate=output.format(from_Date);
						toDate=output.format(to_Date);

						locationData +=childProductName+"@@"+Location+"@@"+fromDate+"@@"+toDate+"@@"+time+"@@"+quantity+"@@"+siteName+"&&";


					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return locationData;
	}	



	/*===========================================================get creted emp names start=====================================================================
	 */
	public Map<String, String> getPOApproveCreateEmp(int tempPONumber,HttpServletRequest request)
	{
		List<Map<String, Object>> productList = null;

		Map<String, String> gst = null;
		gst = new TreeMap<String, String>();
		String creationDate="";

		String query = "SELECT SED.EMP_NAME,SPCAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD "+
		"where SPCAD.OPERATION_TYPE = 'C' and  SPCAD.PO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and TEMP_PO_NUMBER = ?";

		productList = jdbcTemplate.queryForList(query, new Object[] {tempPONumber});
		try{

			if(productList!= null){
				for(Map<String, Object> gstSlabs : productList) {


					creationDate=gstSlabs.get("CREATION_DATE").toString();
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

	/*=========================================================get verified names start========================================================================	*/
	public void getPOVerifiedEmpNames(int intTempPONumber,HttpServletRequest request)
	{

		Map<String, String> names = null;
		names = new TreeMap<String, String>();
		Map<String, String> names1=new TreeMap<String, String>();
		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> productList1 = null;
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
					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(creationDate); 

					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					creationDate=dt1.format(date);

					names.put(String.valueOf(gstSlabs.get("EMP_NAME")),creationDate);
					break;

				}	} 
			request.setAttribute("listOfVerifiedNames",names);


			if(productList1!= null){
				for(Map<String, Object> gstSlabs : productList1) {
					//SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					creationDate=String.valueOf(gstSlabs.get("CREATION_DATE"));

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

	/*=========================================================get verified and authorized names end================================================*/
	/*====================================================get vendor data for perminent==========================================================================*/
	public String getMarketingVendorDetails(String poNumber, String siteId,HttpServletRequest request,String siteName,String vedorId) {
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbDts = null;
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
		String terms="";
		String subject="";
		int intPOYear=0;
		int intPOMonth=0;
		int intPOYearYY = 0;
		String contactPerson="";
		String billingAddress="";
		Map<String, String> Names = null;
		String approverName="";
		String approveDate="";
		String preparedName="";
		String preparedDate="";
		String receivername="";
		String receiverAddress="";
		String recemobilenumber="";
		String recegstinnumber="";
		String poentryId="";
		String verifyName="";
		String verifyDate="";
		String createdDate="";
		String receiverState="";
		String strMobileNo = "";
		String strLandLine = "";
		String strEmailId = "";
		String strReceiverContactPersonName = "";
		String strReceiverContactNo = "";
		String ReceiverState = "";
		String strBillingAddressGSTIN = "";
		String strVendorEmail = "";
		String strBillingCompanyName="";

		String version_No="";
		String refference_No="";
		String strDeliveryDate="";
		String poRefference_Date="";
		String strSiteId="";
		String type_Of_Po="";
		String type_Of_Po_Details="";
		String userId="";
		String strReceivedState="";
		String strBillingState="";

		if (StringUtils.isNotBlank(poNumber) ) {
			String query = "SELECT DISTINCT (STPE.INDENT_NO),STPE.PO_ENTRY_ID,STPE.SITE_ID,STPE.SUBJECT,STPE.BILLING_ADDRESS,STPE.PO_DATE,STPE.VENDOR_ID,VD.VENDOR_CON_PER_NAME,VD.VENDOR_NAME,VD.ADDRESS, VD.MOBILE_NUMBER,VD.LANDLINE_NO,VD.GSIN_NUMBER,VD.STATE,VD.EMP_EMAIL,STPE.PO_NUMBER,(select STATE from VENDOR_DETAILS where  VENDOR_ID = ?) as ReceiverState,STPE.VERSION_NUMBER,STPE.PO_ISSUE_DATE,STPE.REFFERENCE_NO,STPE.DELIVERY_DATE,STPE.PO_ENTRY_USER_ID FROM VENDOR_DETAILS VD,SUMADHURA_PO_ENTRY STPE WHERE STPE.VENDOR_ID =VD.VENDOR_ID  AND  STPE.PO_NUMBER=?";


			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{siteId,poNumber});

		}
		if (null != dbIndentDts && dbIndentDts.size() > 0) {
			for (Map<?, ?> IndentGetBean : dbIndentDts) {

				indentnumber=IndentGetBean.get("INDENT_NO") == null ? "" : IndentGetBean.get("INDENT_NO").toString();
				poentryId=IndentGetBean.get("PO_ENTRY_ID") == null ? "" : IndentGetBean.get("PO_ENTRY_ID").toString();
				vendorid=IndentGetBean.get("VENDOR_ID") == null ? "" : IndentGetBean.get("VENDOR_ID").toString();
				vendorname=IndentGetBean.get("VENDOR_NAME") == null ? "" : IndentGetBean.get("VENDOR_NAME").toString();
				address=IndentGetBean.get("ADDRESS") == null ? "" : IndentGetBean.get("ADDRESS").toString();
				mobilenumber=IndentGetBean.get("MOBILE_NUMBER") == null ? "" : IndentGetBean.get("MOBILE_NUMBER").toString();
				gstinnumber=IndentGetBean.get("GSIN_NUMBER") == null ? "" : IndentGetBean.get("GSIN_NUMBER").toString();
				state=IndentGetBean.get("STATE") == null ? "-" : IndentGetBean.get("STATE").toString();
				podate=IndentGetBean.get("PO_DATE") == null ? "-" : IndentGetBean.get("PO_DATE").toString();
				subject=IndentGetBean.get("SUBJECT") == null ? "-" : IndentGetBean.get("SUBJECT").toString();
				contactPerson=IndentGetBean.get("VENDOR_CON_PER_NAME") == null ? "-" : IndentGetBean.get("VENDOR_CON_PER_NAME").toString();
				billingAddress=IndentGetBean.get("BILLING_ADDRESS") == null ? "-" : IndentGetBean.get("BILLING_ADDRESS").toString();
				ponumber=IndentGetBean.get("PO_NUMBER") == null ? "" : IndentGetBean.get("PO_NUMBER").toString();
				strMobileNo=IndentGetBean.get("MOBILE_NUMBER") == null ? "" : IndentGetBean.get("MOBILE_NUMBER").toString();
				strLandLine=IndentGetBean.get("LANDLINE_NO") == null ? "" : IndentGetBean.get("LANDLINE_NO").toString();
				strEmailId=IndentGetBean.get("EMP_EMAIL") == null ? "-" : IndentGetBean.get("EMP_EMAIL").toString();
				ReceiverState=IndentGetBean.get("ReceiverState") == null ? " " : IndentGetBean.get("ReceiverState").toString();
				strSiteId=IndentGetBean.get("SITE_ID") == null ? " " : IndentGetBean.get("SITE_ID").toString();
				userId=IndentGetBean.get("PO_ENTRY_USER_ID") == null ? " " : IndentGetBean.get("PO_ENTRY_USER_ID").toString();
				
				// here we need to take below values for revised po purpose 
				version_No=IndentGetBean.get("VERSION_NUMBER") == null ? "-" : IndentGetBean.get("VERSION_NUMBER").toString();
				refference_No=IndentGetBean.get("REFFERENCE_NO") == null ? "-" : IndentGetBean.get("REFFERENCE_NO").toString();
				poRefference_Date=IndentGetBean.get("PO_ISSUE_DATE") == null ? "-" : IndentGetBean.get("PO_ISSUE_DATE").toString();
				strDeliveryDate=IndentGetBean.get("DELIVERY_DATE") == null ? "" : IndentGetBean.get("DELIVERY_DATE").toString();


				if(!poRefference_Date.equals("-")){
					DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
					DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
					Date poReffDate;
					try {
						poReffDate = inputFormat.parse(poRefference_Date);
						poRefference_Date=outputFormat.format(poReffDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					/*poRefference_Date=outputFormat.format(poReffDate);
					if(StringUtils.isNotBlank(strDeliveryDate)){
						Date deliveryDate = inputFormat.parse(strDeliveryDate);
						strDeliveryDate=outputFormat.format(deliveryDate);
					}*/
				}
				String strReceiverState=getDeptId(userId); // this is for delivery address purpose
				//  System.out.println(outputFormat.format(date));
				request.setAttribute("versionNo", version_No);
				request.setAttribute("refferenceNo", refference_No);
				request.setAttribute("strPoPrintRefdate", poRefference_Date);
				request.setAttribute("strReceiverState",strReceiverState);

				if(strReceiverState.equalsIgnoreCase("Telangana")){
					strBillingAddressGSTIN = validateParams.getProperty("GSTIN_HYDERABAD");
					strBillingCompanyName=validateParams.getProperty("BILLING_NAME");

					if(billingAddress.contains("Sumadhura Infracon pvt Ltd")){

						billingAddress=billingAddress.replace("Sumadhura Infracon pvt Ltd,","");
					}

				}else{
					strBillingAddressGSTIN = validateParams.getProperty("GSTIN_BENGALORE");
					strBillingCompanyName=validateParams.getProperty("BILLING_NAME");

					if(billingAddress.contains("Sumadhura Infracon pvt Ltd")){

						billingAddress=billingAddress.replace("Sumadhura Infracon pvt Ltd,","");

					}

				}
				if(!poentryId.equals("")){
				 strBillingState=gettingBillingState(poentryId);
				}
				request.setAttribute("billingState",strBillingState);
				//.contains("")
				//	contactPerson = " Mr/Mrs "+contactPerson +" ( "+strMobileNo+" "+strLandLine+" )";
				contactPerson =  contactPerson +" ( "+strMobileNo+","+strLandLine+" )";
				state = state;//+". Email Id : "+strEmailId;
				strVendorEmail = strEmailId;

				//	terms=IndentGetBean.get("TERMS_CONDITION_DESC") == null ? "-" : IndentGetBean.get("TERMS_CONDITION_DESC").toString();
				//	request.setAttribute("terms",terms);
				request.setAttribute("poentryId",poentryId); // here this is using in controller for getting the po level comments

				getPOPermenentApproveCreateEmp(poentryId,request); // get the signature of success page i.e prepared,verified,authorized
				Map<String, String> verifyNames = (Map<String, String>)request.getAttribute("listOfVerifiedName");
				Map<String, String> approverNames = (Map<String, String>)request.getAttribute("listOfApproverName");
				Map<String, String> preparedNames = (Map<String, String>)request.getAttribute("listOfPreparedName");

				// below we need to get and  set the prepared,verified,authorized
				for(Map.Entry<String, String> approver : approverNames.entrySet()) {
					approverName=approver.getKey().toUpperCase();
					approveDate=approver.getValue(); 
				}

				for(Map.Entry<String, String> prepared : preparedNames.entrySet()) {
					preparedName=prepared.getKey().toUpperCase();
					preparedDate=prepared.getValue(); 
				}
				for(Map.Entry<String, String> verify : verifyNames.entrySet()) {
					verifyName=verify.getKey().toUpperCase();
					verifyDate=verify.getValue(); 
				}
				try{

					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(podate); 


					SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
					podate=dt1.format(date);


					dt1.applyPattern("yyyy");
					intPOYear =Integer.parseInt((dt1.format(date)));

					dt1.applyPattern("MM");
					intPOMonth = Integer.parseInt((dt1.format(date)));
					String strFinacialYear = "";
					dt1.applyPattern("YY");
					intPOYearYY = Integer.parseInt((dt1.format(date)));

					if(intPOMonth <=3){
						strFinacialYear = (intPOYear-1)+"-"+intPOYearYY;
					}else{
						strFinacialYear = intPOYear+"-"+(intPOYearYY+1);
					}

					request.setAttribute("strFinacialYear",strFinacialYear);

				}


				catch(Exception e){
					e.printStackTrace();
				}
				
				
				if (StringUtils.isNotBlank(poNumber) ) {
					
					String sql="select TYPE_OF_PO,TYPE_OF_PO_DETAILS from MRKT_PO_AREA_WISE_DTLS where PO_ENTRY_ID=?";
					dbDts = jdbcTemplate.queryForList(sql, new Object[]{poentryId});
					if (null != dbDts && dbDts.size() > 0) {
						for (Map<?, ?> poType : dbDts) {
							 type_Of_Po=poType.get("TYPE_OF_PO") == null ? "" : poType.get("TYPE_OF_PO").toString();
							 type_Of_Po_Details=poType.get("TYPE_OF_PO_DETAILS") == null ? "-" : poType.get("TYPE_OF_PO_DETAILS").toString();
						}
						
					}
					if(type_Of_Po.equalsIgnoreCase("SiteWise") && !type_Of_Po_Details.equals("-")){strSiteId=type_Of_Po_Details;
					
					}
					dbIndentDts=getVendorOrSiteAddress(strSiteId);
					if (null != dbIndentDts && dbIndentDts.size() > 0) {
						for (Map<?, ?> siteVendoraddress : dbIndentDts) {
							receivername = siteVendoraddress.get("VENDOR_NAME") == null ? "" : siteVendoraddress.get("VENDOR_NAME").toString();
							receiverState=siteVendoraddress.get("STATE") == null ? "" : siteVendoraddress.get("STATE").toString();
							receiverAddress=siteVendoraddress.get("ADDRESS") == null ? "" : siteVendoraddress.get("ADDRESS").toString();
							request.setAttribute("receiverState",receiverState);

						}
					}
					if(!type_Of_Po.equalsIgnoreCase("SiteWise") && !type_Of_Po_Details.equals("-")){
					List<String> projectData=getProjectAddGstin(strReceiverState); // to get receiver address
					receivername=projectData.get(1);
					receiverAddress=projectData.get(0);
					request.setAttribute("receiverState",strReceiverState);
				}
					
				}

				tblOneData+= ponumber+"@@"+podate+"@@"+vendorname+"@@"+address+"@@"+state+"@@"+gstinnumber+
				"@@"+receivername+"@@"+receiverAddress+"@@"+recemobilenumber+"@@"+recegstinnumber+"@@"+""+"@@"+subject+
				"@@"+contactPerson+"@@"+""+"@@"+billingAddress+"@@"+approverName+"@@"+approveDate+"@@"+preparedName+"@@"+preparedDate+"@@"+verifyName+"@@"+verifyDate+"@@"+createdDate+"@@"+strBillingAddressGSTIN+
				"@@"+""+"@@"+strReceiverContactPersonName+"@@"+strVendorEmail+"@@"+strBillingCompanyName+","+"@@"+siteId+"@@"+siteName+"@@"+strDeliveryDate+"@@"+vendorid+"@@"+"Ref No";

				break;
			}
			request.setAttribute("gstinumber",gstinnumber);
		}

		return tblOneData;

	}
	/*=======================================================show perminent product details start======================================================*/
	public String getMarketingProductDetails(String poNumber, String siteId,HttpServletRequest request,String receiverState) {
		List<Map<String, Object>> GetproductDetailsList = null;
		IndentCreationBean objGetInvoiceDetailsInward=null;

		//JdbcTemplate template = null;
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
		int sno=0;
		String discount="";
		String discountaftertax="";
		double totalAmt=0.0;
		String vendorProductDesc="";
		String pd_Product_Desc="";
		String product_name="";
		String productId="";
		String sub_productId="";
		String child_productId="";
		String measurementId="";
		String indentCreationDetailsId="";

		try{
			if (StringUtils.isNotBlank(poNumber) ) {

				sql+="select (P.NAME)AS PRODUCT_NAME,(SP.NAME) AS SUB_PRODUCT,(CP.NAME) AS CHILD_PRODUCT,M.NAME, SPED.PO_QTY,SPED.TAX,SPED.TAX_AMOUNT,SPED.AMOUNT_AFTER_TAX, "
					+" SPED.PRODUCT_ID,SPED.SUB_PRODUCT_ID,SPED.CHILD_PRODUCT_ID,SPED.MEASUR_MNT_ID, "
					+" SPED.BASIC_AMOUNT,SPED.PRICE, SPED.HSN_CODE,SPED.TOTAL_AMOUNT,SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,SPED.VENDOR_PRODUCT_DESC, "
					+" IGST.TAX_PERCENTAGE,SPED.PD_PRODUCT_DESC from SUMADHURA_PO_ENTRY SPE,SUMADHURA_PO_ENTRY_DETAILS SPED, " 
					+" SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT M,INDENT_GST IGST,PRODUCT P  where IGST.TAX_ID = SPED.TAX and SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID " 
					+" AND P.PRODUCT_ID=SPED.PRODUCT_ID and  SPED.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID and SPED.MEASUR_MNT_ID= M.MEASUREMENT_ID and SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID "
					+" and SPE.PO_ENTRY_ID =? AND SPE.SITE_ID=? "; 

				GetproductDetailsList = jdbcTemplate.queryForList(sql, new Object[] {poNumber, siteId});
				//System.out.println("second product data in service"+GetproductDetailsList);
			} 
			if (null != GetproductDetailsList && GetproductDetailsList.size() > 0) {
				for (Map<?, ?> GetDetailsInwardBean : GetproductDetailsList) {
					objGetInvoiceDetailsInward = new IndentCreationBean();
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
					gstinumber=GetDetailsInwardBean.get("GSIN_NUMBER") == null ? "": GetDetailsInwardBean.get("GSIN_NUMBER").toString();
					discount=GetDetailsInwardBean.get("DISCOUNT") == null ? "": GetDetailsInwardBean.get("DISCOUNT").toString();
					discountaftertax=GetDetailsInwardBean.get("AMOUNT_AFTER_DISCOUNT") == null ? "": GetDetailsInwardBean.get("AMOUNT_AFTER_DISCOUNT").toString();
					vendorProductDesc=GetDetailsInwardBean.get("VENDOR_PRODUCT_DESC") == null ? "-": GetDetailsInwardBean.get("VENDOR_PRODUCT_DESC").toString();
					pd_Product_Desc=GetDetailsInwardBean.get("PD_PRODUCT_DESC") == null ? "": GetDetailsInwardBean.get("PD_PRODUCT_DESC").toString();

					product_name=GetDetailsInwardBean.get("PRODUCT_NAME") == null ? "": GetDetailsInwardBean.get("PRODUCT_NAME").toString();
					productId=GetDetailsInwardBean.get("PRODUCT_ID") == null ? "": GetDetailsInwardBean.get("PRODUCT_ID").toString();
					sub_productId=GetDetailsInwardBean.get("SUB_PRODUCT_ID") == null ? "": GetDetailsInwardBean.get("SUB_PRODUCT_ID").toString();
					child_productId=GetDetailsInwardBean.get("CHILD_PRODUCT_ID") == null ? "": GetDetailsInwardBean.get("CHILD_PRODUCT_ID").toString();
					measurementId=GetDetailsInwardBean.get("MEASUR_MNT_ID") == null ? "": GetDetailsInwardBean.get("MEASUR_MNT_ID").toString();
					//indentCreationDetailsId=GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID") == null ? "": GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID").toString();

					if(pd_Product_Desc!=null && !pd_Product_Desc.equals("") ){

						childproduct=pd_Product_Desc;
					}

					gstinumber=request.getAttribute("gstinumber").toString();
					//char firstLetterChar = gstinumber.charAt(0);
					//char secondLetterChar=gstinumber.charAt(1);

					if(tax.contains("%")){
						String data[] = tax.split("%");
						tax=data[0];

					}
					// here calculate cgst,sgst,igst then get back to take tax values
					String gstinState=request.getAttribute("strReceiverState").toString();
					List<String> gstDetails=calculateGstAmount(request,tax,gstinumber,gstinState,taxamount);


					double totalvalue=Double.valueOf(totalamount);
					totalAmt=totalAmt+totalvalue;
					totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
					int val = (int) Math.ceil(totalAmt);
					double roundoff=Math.ceil(totalAmt)-totalAmt;
					double grandtotal=(Math.ceil(totalAmt));

					String strGroundVal=String.format("%.2f",grandtotal);
					String totalVal=String.format("%.2f",totalAmt);

					NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
					String strtotal = numberFormat.format(totalAmt);

					String strroundoff=String.format("%.2f",roundoff);
					String strgrandtotal=numberFormat.format(grandtotal);

					tblTwoData += sno+"@@"+subproduct+"@@"+childproduct+"@@"+hsncode+"@@"+measurement+"@@"+
					quantity+"@@"+price+"@@"+basicamount+"@@"+discount+"@@"+discountaftertax+"@@"+gstDetails.get(0)+"%"+"@@"+gstDetails.get(1)+"@@"
					+gstDetails.get(2)+"%"+"@@"+gstDetails.get(3)+"@@"+gstDetails.get(4) + "%" +"@@"+gstDetails.get(5)+"@@"+amountaftertax+"@@"+amountaftertax+"@@"
					+strroundoff+"@@"+strgrandtotal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+strtotal+"@@"+vendorProductDesc+"@@"
					+""+"@@"+product_name+"@@"+productId+"@@"+sub_productId+"@@"+child_productId+"@@"+measurementId+"@@"+indentCreationDetailsId+"@@"+""+"&&";

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return tblTwoData;

	}
	/*================================================show transport perminent details for show start=========================================================*/
	@Override
	public String getMarketingTransportChargesListForGRN(String poEntryId,String strSiteId,String gstinumber,HttpServletRequest request,String poNumber,String receiverState) {
		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> termList = null;

		List<String> listOfTermsAndConditions = new ArrayList<String>();
		JdbcTemplate template = null;
		String sql="";
		String ConveyanceName="";
		String ConveyanceAmount="";
		String GSTTax="";
		String GSTAmount="";
		String AmountAfterTax="";
		String strTableThreeData="";
		String response="success";
		String termscondition="";

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(poNumber) ) {
				sql += "SELECT STOCM.CHARGE_NAME,STOCD.TRANSPORT_AMOUNT,STOCD.TRANSPORT_GST_PERCENTAGE,STOCD.TRANSPORT_GST_AMOUNT,STOCD.TOTAL_AMOUNT_AFTER_GST_TAX,IGST.TAX_PERCENTAGE FROM SUMADHURA_PO_TRNS_O_CHRGS_DTLS STOCD,SUMADHURA_TRNS_OTHR_CHRGS_MST STOCM,INDENT_GST IGST "
					+ " WHERE IGST.TAX_ID=STOCD.TRANSPORT_GST_PERCENTAGE AND STOCD.TRANSPORT_ID=STOCM.CHARGE_ID and STOCD.PO_NUMBER= ?";	

				productList = jdbcTemplate.queryForList(sql, new Object[] {poEntryId});

				if (null != productList && productList.size() > 0) {
					for (Map<?, ?> GetTransportChargesDetails : productList) {
						ConveyanceAmount = GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString();	
						GSTTax = GetTransportChargesDetails.get("TAX_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TAX_PERCENTAGE").toString();	
						GSTAmount = GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString();
						AmountAfterTax = GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString();
						ConveyanceName = GetTransportChargesDetails.get("CHARGE_NAME") == null ? "" : GetTransportChargesDetails.get("CHARGE_NAME").toString();

						if(GSTTax.contains("%")){
							String data[] = GSTTax.split("%");
							GSTTax=data[0];

						}
						// calculate cgst,sgst,igst then take the tax values
						String gstinState=request.getAttribute("strReceiverState").toString();
						List<String> gstDetails=calculateGstAmount(request,GSTTax,gstinumber,gstinState,GSTAmount);
						strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"@@"+gstDetails.get(0)+"%"+"@@"+gstDetails.get(1)+"@@"+gstDetails.get(2)+"%"+"@@"+gstDetails.get(3)+"@@"+gstDetails.get(4)+"%"+"@@"+gstDetails.get(5)+"&&";				

					}
				}

				if(response=="success"){

					String sql1="SELECT TERMS_CONDITION_DESC FROM  SUMADHURA_PD_TERMS_CONDITIONS WHERE PO_ENTRY_ID=?";
					termList = jdbcTemplate.queryForList(sql1, new Object[] {poEntryId});
					if (null != termList && termList.size() > 0) {
						for (Map<?, ?> GetTransportChargesDetails : termList) {
							termscondition = GetTransportChargesDetails.get("TERMS_CONDITION_DESC") == null ? "" : GetTransportChargesDetails.get("TERMS_CONDITION_DESC").toString();
							listOfTermsAndConditions.add(String.valueOf(termscondition));

						}
					}
				}
				request.setAttribute("listOfTermsAndConditions", listOfTermsAndConditions);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return strTableThreeData;
	}	
	// get perminent location details show
	public String getPendingperminentLocationList(String poNumber) {
		List<Map<String, Object>> productList = null;
		//List<Map<String, Object>> termList = null;
		//List<String> listOfTermsAndConditions = new ArrayList<String>();

		String sql="";
		String childProductName="";
		String Location="";
		String fromDate="";
		String toDate="";
		String time="";
		String locationData="";
		String quantity="";
		String siteName="";
		try {

			if (StringUtils.isNotBlank(poNumber) ) {
				sql += "SELECT (C.NAME)AS CHILD_PROD_NAME,min(MPDLD.HOARDING_DATE) as fromDate,"
						+" max(MPDLD.HOARDING_DATE) as todate,MPDLD.TIME,MPDLD.PRODUCT_SERIAL_NO,"
						+" MPDLD.QUANTITY,S.SITE_NAME,MPDLD.AMOUNT_PER_UNIT_AFTER_TAX,sum(MPDLD.TOTAL_AMOUNT) AS TOTAL,MHD.HOARDING_AREA" 
						+" from CHILD_PRODUCT C,SITE S,MRKT_PO_PROD_LOC_DTLS MPDLD"
						+" LEFT OUTER JOIN MRKT_HOARDING_DTLS MHD ON MHD.HOARDING_ID=MPDLD.LOCATION_ID"
						+" where MPDLD.PO_NUMBER=? AND MPDLD.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID" 
						+" AND S.SITE_ID=MPDLD.SITE_ID   group by C.NAME,MHD.HOARDING_AREA,MPDLD.TIME,"
						+" MPDLD.QUANTITY,S.SITE_NAME,MPDLD.AMOUNT_PER_UNIT_AFTER_TAX,MPDLD.TOTAL_AMOUNT,MPDLD.PRODUCT_SERIAL_NO";
    
				productList = jdbcTemplate.queryForList(sql, new Object[] {poNumber});

				if (null != productList && productList.size() > 0) {
					for (Map<?, ?> GetLocationDetails : productList) {
						childProductName = GetLocationDetails.get("CHILD_PROD_NAME") == null ? "0" : GetLocationDetails.get("CHILD_PROD_NAME").toString();	
						Location = GetLocationDetails.get("HOARDING_AREA") == null ? "-" : GetLocationDetails.get("HOARDING_AREA").toString();	
						fromDate = GetLocationDetails.get("fromDate") == null ? "0" : GetLocationDetails.get("fromDate").toString();
						toDate = GetLocationDetails.get("todate") == null ? "0" : GetLocationDetails.get("todate").toString();
						time = GetLocationDetails.get("TIME") == null ? "-" : GetLocationDetails.get("TIME").toString();
						quantity=GetLocationDetails.get("QUANTITY") == null ? "0" : GetLocationDetails.get("QUANTITY").toString();
						siteName=GetLocationDetails.get("SITE_NAME") == null ? "-" : GetLocationDetails.get("SITE_NAME").toString();

						SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
						Date from_Date = input.parse(fromDate); 
						Date to_Date = input.parse(toDate);
						SimpleDateFormat output = new SimpleDateFormat("dd-MM-yyyy");
						fromDate=output.format(from_Date);
						toDate=output.format(to_Date);

						locationData +=childProductName+"@@"+Location+"@@"+fromDate+"@@"+toDate+"@@"+time+"@@"+quantity+"@@"+siteName+"&&";


					}
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return locationData;
	}
	// get verified and prepared by get names for perminent po
	public void getPOPermenentApproveCreateEmp(String strPONumber,HttpServletRequest request)
	{
		List<Map<String, Object>> createdBy = null;
		List<Map<String, Object>> approverBy = null;
		List<Map<String, Object>> verifyBy = null;
		Map<String, String> created =new TreeMap<String, String>();
		Map<String, String> approver =new TreeMap<String, String>();
		Map<String, String> verify =new TreeMap<String, String>();
		String empName="";
		String creationDate="";
		//	DateFormat df2 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"); 

		String sql= "SELECT SED.EMP_NAME,SPCAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD "+
		"where SPCAD.OPERATION_TYPE = 'A' and  SPCAD.PO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and TEMP_PO_NUMBER = (select TEMP_PO_NUMBER from SUMADHURA_PO_CRT_APPRL_DTLS where PO_ENTRY_ID = ? )   order by SPCAD.CREATION_DATE  ASC"; 

		verifyBy = jdbcTemplate.queryForList(sql, new Object[] {strPONumber});

		String query =  "SELECT SED.EMP_NAME,SPCAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD "+
		"where SPCAD.OPERATION_TYPE = 'A' and  SPCAD.PO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and TEMP_PO_NUMBER = (select TEMP_PO_NUMBER from SUMADHURA_PO_CRT_APPRL_DTLS where PO_ENTRY_ID = ? )   order by SPCAD.CREATION_DATE  DESC"; 

		approverBy = jdbcTemplate.queryForList(query, new Object[] {strPONumber});

		/*String query1 = "SELECT SED.EMP_NAME,SPCAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD "+
                        "where SPCAD.OPERATION_TYPE = 'C' and  SPCAD.PO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and TEMP_PO_NUMBER = (select TEMP_PO_NUMBER from SUMADHURA_PO_CRT_APPRL_DTLS where PO_NUMBER = ? )"; 
		 */
		String query1 = "SELECT SED.EMP_NAME,SPCAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD "+
		"where SPCAD.OPERATION_TYPE = 'C' and  SPCAD.PO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and PO_ENTRY_ID =  ? "; 


		createdBy = jdbcTemplate.queryForList(query1, new Object[] {strPONumber});

		if(createdBy != null && createdBy.size() ==0){
			query1 = "SELECT SED.EMP_NAME,SPCAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD "+
			"where SPCAD.OPERATION_TYPE = 'C' and  SPCAD.PO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and TEMP_PO_NUMBER = (select TEMP_PO_NUMBER from SUMADHURA_PO_CRT_APPRL_DTLS where PO_ENTRY_ID = ? )"; 
			createdBy = jdbcTemplate.queryForList(query1, new Object[] {strPONumber});
		}
		try{
			if(verifyBy!= null){
				for(Map<String, Object> gstSlabs : verifyBy) {
					creationDate=String.valueOf(gstSlabs.get("CREATION_DATE"));
					//	 Date d1 = df2.parse(creationDate);
					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(creationDate); 
					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					creationDate=dt1.format(date);
					verify.put(String.valueOf(gstSlabs.get("EMP_NAME")), creationDate);
					break;

				}	
			} 

			request.setAttribute("listOfVerifiedName",verify);

			if(approverBy!= null){
				for(Map<String, Object> gstSlabs : approverBy) {
					creationDate=String.valueOf(gstSlabs.get("CREATION_DATE"));
					//	 Date d1 = df2.parse(creationDate);
					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(creationDate); 
					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					creationDate=dt1.format(date);
					approver.put(String.valueOf(gstSlabs.get("EMP_NAME")), creationDate);

					break;

				}	} 

			request.setAttribute("listOfApproverName",approver);

			if(createdBy!= null){
				for(Map<String, Object> gstSlabs : createdBy) {
					creationDate=String.valueOf(gstSlabs.get("CREATION_DATE"));
					//	 Date d1 = df2.parse(creationDate);

					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
					Date date = dt.parse(creationDate); 
					SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					creationDate=dt1.format(date);
					created.put(String.valueOf(gstSlabs.get("EMP_NAME")),creationDate);

				}	} 

			request.setAttribute("listOfPreparedName",created);

		}catch(Exception e){
			e.printStackTrace();
		}


	}
	// this is for update temp po table emp name
	@Override
	public int updateTempMarketingPoEntry(String approvalEmpId,String poNumber,String ccmailId,String passwdForMail,String deliveryDate){
		int val=0;
		String response="";
		String query = "update SUMADHURA_TEMP_PO_ENTRY set TEMP_PO_PENDING_EMP_ID=?, CC_EMAIL_ID=?,PASSWORD_MAIL=?,DELIVERY_DATE=? where PO_NUMBER=?";
		val = jdbcTemplate.update(query, new Object[] {approvalEmpId,ccmailId,passwdForMail,StringUtils.isBlank(deliveryDate)?null:DateUtil.convertToJavaDateFormat(deliveryDate),poNumber
		});


		return val;
	}

	/*============================================================get and save temp Marketing po details start=================================================*/
	public String getAndMarketingsaveVendorDetails(String tempPONumber, String siteId,String user_id,HttpServletRequest request,String revision_No,String oldPoNumber,String siteName,String deliveryDate) {

		List<Map<String, Object>> dbIndentDts = null;

		//List<String> listRevisedDetails = new ArrayList<String>();
		//String indentnumber="";
		String ponumber ="";
		String vendorid="";
		String response="";
		int result=0;
		String subject="";
		String billingAddress="";
		String State="";
		//String poState="";

		String strIndentFromSiteId = "";
		String version_Number="";
		String porefferenceDate="";
		String refference_No="";
		int temprevision_no=0;
		//int revision_no=0;

		String preparedBy="";
		String strPOdate="";
		String strFinacialYear="";
		String type_Of_Po="";
		String type_Of_Po_Details="";
		String operation_Type="";
		String payment_Req_Days="";
		String poEntryId="";
		//String oldPOEntryId="";//this is for revised po purpose to get received quantity receive from inwards from po
		//String strRevisedTypePurchase="";//this is for revised po purpose to get received quantity receive from inwards from po
		if (StringUtils.isNotBlank(tempPONumber) ) {
			String query = "SELECT STPE.PO_ENTRY_ID,STPE.VENDOR_ID,STPE.SUBJECT,STPE.TYPE_OF_PO,STPE.TYPE_OF_PO_DETAILS,STPE.BILLING_ADDRESS,VD.STATE,STPE.SITE_ID,STPE.VERSION_NUMBER,STPE.PO_ISSUE_DATE,STPE.REFFERENCE_NO,STPE.PAYMENT_REQ_DAYS,STPE.OPERATION_TYPE,STPE.PO_ENTRY_USER_ID FROM VENDOR_DETAILS VD,SUMADHURA_TEMP_PO_ENTRY STPE WHERE STPE.VENDOR_ID =VD.VENDOR_ID  AND  STPE.PO_ENTRY_ID=?";


			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{tempPONumber});

		}

		String query = "select STATE from VENDOR_DETAILS where VENDOR_ID='"+siteId+"'";
		State = jdbcTemplate.queryForObject(query, String.class);
		int currentYear = Calendar.getInstance().get(Calendar.YEAR);
		int currentMonth = Calendar.getInstance().get(Calendar.MONTH)+1;
		Calendar cal = Calendar.getInstance();
		String currentYearYY = new SimpleDateFormat("YY").format(cal.getTime());
		

		request.setAttribute("state",State);

		if(currentMonth <=3){

			strFinacialYear = (currentYear-1)+"-"+Integer.parseInt(currentYearYY);
		}else{
			strFinacialYear = currentYear+"-"+(Integer.parseInt(currentYearYY)+1);
		}
		preparedBy="MARKETING_DEPT";
		/*List<Map<String, Object>> listReceiverDtls = getVendorOrSiteAddress(strSiteId);// strToSite
			for (Map<String, Object> prods : listReceiverDtls) {
				receiverAddress = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
				receiverState = prods.get("STATE") == null ? "" : prods.get("STATE").toString();
			}*/
		if(oldPoNumber!=null && !oldPoNumber.equals("-") && !oldPoNumber.equals("") && oldPoNumber.startsWith("PO/SIPL")){
			ponumber=getPermenentRevisedPoNumber(oldPoNumber);
				/*listRevisedDetails=getRevisionNumber(oldPoNumber);
				revision_no=Integer.parseInt(listRevisedDetails.get(0));
				oldPOEntryId=listRevisedDetails.get(1);
				strRevisedTypePurchase=listRevisedDetails.get(2);

				strPOdate=inactiveOldPo(oldPoNumber,"false");
				request.setAttribute("RevisedOrNot","true");
				request.setAttribute("strPOdate",strPOdate);
				String tempPoNumber=oldPoNumber;
				if(tempPoNumber.contains("R")){ 
					temprevision_no=revision_no+1;
					ponumber=tempPoNumber.replace("R"+revision_no, "R"+temprevision_no);


				}else{
					temprevision_no=revision_no+1;
					ponumber=tempPoNumber+"/R"+temprevision_no;

				}*/

			}
		else {
			//dbIndentDts.get(0).get("PO_ENTRY_USER_ID").toString();
		String strState=getDeptId(dbIndentDts.get(0).get("PO_ENTRY_USER_ID").toString());//getDeptIdInApprovalTime(tempPONumber); // this is for po number purpose written state wise based on user
		ponumber=getPermenentPoNumber(strState, siteName,siteId,strFinacialYear);
		}
		int poSeqNo  = objPurchaseDepartmentIndentProcessDao.getPoEnterSeqNo();
		String temppoOrNot="Ref No";

		//request.setAttribute("oldPOEntryId",oldPOEntryId);
		//	ponumber = getPoEnterSeqNoOrMaxId();
		//request.setAttribute("strRevisedTypePurchase",strRevisedTypePurchase);
		//int poSeqNo = getPoEnterSeqNo();

		if (null != dbIndentDts && dbIndentDts.size() > 0) {
			for (Map<?, ?> IndentGetBean : dbIndentDts) {

				vendorid=IndentGetBean.get("VENDOR_ID") == null ? "" : IndentGetBean.get("VENDOR_ID").toString();
				subject=IndentGetBean.get("SUBJECT") == null ? "" : IndentGetBean.get("SUBJECT").toString();
				billingAddress=IndentGetBean.get("BILLING_ADDRESS") == null ? "" : IndentGetBean.get("BILLING_ADDRESS").toString();
				strIndentFromSiteId=IndentGetBean.get("SITE_ID") == null ? "" : IndentGetBean.get("SITE_ID").toString();

				version_Number=IndentGetBean.get("VERSION_NUMBER") == null ? "" : IndentGetBean.get("VERSION_NUMBER").toString();
				porefferenceDate=IndentGetBean.get("PO_ISSUE_DATE") == null ? "" : IndentGetBean.get("PO_ISSUE_DATE").toString();
				refference_No=IndentGetBean.get("REFFERENCE_NO") == null ? "" : IndentGetBean.get("REFFERENCE_NO").toString();
				poEntryId=IndentGetBean.get("PO_ENTRY_ID") == null ? "0" : IndentGetBean.get("PO_ENTRY_ID").toString();
				
				 type_Of_Po=IndentGetBean.get("TYPE_OF_PO") == null ? "" : IndentGetBean.get("TYPE_OF_PO").toString();;
				 type_Of_Po_Details=IndentGetBean.get("TYPE_OF_PO_DETAILS") == null ? "-" : IndentGetBean.get("TYPE_OF_PO_DETAILS").toString();
				 operation_Type=IndentGetBean.get("OPERATION_TYPE") == null ? "-" : IndentGetBean.get("OPERATION_TYPE").toString();
				 payment_Req_Days=IndentGetBean.get("PAYMENT_REQ_DAYS") == null ? "" : IndentGetBean.get("PAYMENT_REQ_DAYS").toString();
				 
				 insertPoAreaWiseData(poSeqNo,type_Of_Po,type_Of_Po_Details);

				String query1 = "INSERT INTO SUMADHURA_PO_ENTRY(PO_ENTRY_ID,PO_NUMBER,PO_DATE,VENDOR_ID,PO_STATUS,"+
				"PO_ENTRY_USER_ID,SITE_ID,TERMS_CONDITIONS_ID,SUBJECT,BILLING_ADDRESS,VERSION_NUMBER,PO_ISSUE_DATE,REFFERENCE_NO,REVISION,OLD_PO_NUMBER,PREPARED_BY,DELIVERY_DATE,OPERATION_TYPE,PAYMENT_REQ_DAYS,TEMP_PO_ENTRY_ID) values(? , ?, sysdate, ?, ?, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?)";
				result = jdbcTemplate.update(query1, new Object[] {
						poSeqNo,ponumber,vendorid,"A",user_id,
						siteId,"0" ,subject,billingAddress,version_Number,DateUtil.convertToJavaDateFormat(porefferenceDate),refference_No,temprevision_no,oldPoNumber,preparedBy,StringUtils.isBlank(deliveryDate)?null:DateUtil.convertToJavaDateFormat(deliveryDate),operation_Type,payment_Req_Days,poEntryId});

			}
			request.setAttribute("tempPoNumber",tempPONumber);
			request.setAttribute("poEntrySeqID",poSeqNo);
			request.setAttribute("permentPoNumber",ponumber);
			request.setAttribute("strPOdate",strPOdate);
			request.setAttribute("State",State);

			response="success";
		}
		return "response";

	}

	/*=====================================================get and save marketing po details start======================================================*/
	public Map<String,String> getAndsaveMarketingPoProductDetails(String tempPONumber, String siteId,HttpServletRequest request,String premPONumber,int intPOEntrySeqId,String old_Po_Number) {
		List<Map<String, Object>> getTemPoProductDetailsList = null;
		Map<String, String> productData = new HashMap<String, String>();
		Map<String, String> MarketingData = new HashMap<String, String>();

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

		String vendorProductDesc="";
		String pd_Product_Desc="";
		String totalAmt="";
		double finalAmount=0.0; // to update po_entry table total amount

		try{
			if (StringUtils.isNotBlank(tempPONumber) ) {

				sql+="select DISTINCT SP.SUB_PRODUCT_ID,SPE.TOTAL_AMOUNT,SPED.PO_ENTRY_DETAILS_ID,CP.CHILD_PRODUCT_ID,M.MEASUREMENT_ID,SPED.PO_QTY,SPED.TAX,SPED.TAX_AMOUNT,SPED.AMOUNT_AFTER_TAX,SPED.BASIC_AMOUNT,SPED.PRICE,SPED.HSN_CODE,SPED.TOTAL_AMOUNT,SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,P.PRODUCT_ID,SPED.VENDOR_PRODUCT_DESC,SPED.PD_PRODUCT_DESC from SUMADHURA_TEMP_PO_ENTRY SPE,SUMADHURA_TEMP_PO_ENTRY_DTLS SPED,VENDOR_DETAILS VD,PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT M "  
					+"where P.PRODUCT_ID=SPED.PRODUCT_ID AND SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID and SPE.VENDOR_ID = VD.VENDOR_ID and SPED.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID and SPED.MEASUR_MNT_ID= M.MEASUREMENT_ID and SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID and SPE.PO_ENTRY_ID =? AND SPE.SITE_ID=? "; 

				getTemPoProductDetailsList = jdbcTemplate.queryForList(sql, new Object[] {tempPONumber, siteId});

			} 

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
					//indentCreationDetailsId=GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID") == null ? "": GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID").toString();
					vendorProductDesc=GetDetailsInwardBean.get("VENDOR_PRODUCT_DESC") == null ? "": GetDetailsInwardBean.get("VENDOR_PRODUCT_DESC").toString();
					pd_Product_Desc=GetDetailsInwardBean.get("PD_PRODUCT_DESC") == null ? "-": GetDetailsInwardBean.get("PD_PRODUCT_DESC").toString();
					totalAmt=GetDetailsInwardBean.get("TOTAL_AMOUNT") == null ? "-": GetDetailsInwardBean.get("TOTAL_AMOUNT").toString();
					finalAmount=finalAmount+Double.valueOf(totalAmt);

					int po_EntrydetailsId=	jdbcTemplate.queryForObject("select SUMADHURA_PO_ENTRY_DTLS_SEQ.nextval from dual", Integer.class);
					String query1 = "INSERT INTO SUMADHURA_PO_ENTRY_DETAILS(PO_ENTRY_DETAILS_ID,PO_ENTRY_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,"+
					"MEASUR_MNT_ID, PO_QTY,ENTRY_DATE,PRICE,BASIC_AMOUNT,TAX,TAX_AMOUNT,AMOUNT_AFTER_TAX,OTHER_CHARGES,OTHER_CHARGES_AFTER_TAX,TOTAL_AMOUNT,HSN_CODE,TAX_ON_OTHER_TRANSPORT_CHG,DISCOUNT,AMOUNT_AFTER_DISCOUNT,VENDOR_PRODUCT_DESC,PD_PRODUCT_DESC,RECEIVED_QUANTITY"
					+ ") values(?, ?, ?, ?, ?, ?, ?,sysdate, ?, ?, ?, ?,?, ?, ?,?, ?, ?, ?,?,?,?,?)";

					int result = jdbcTemplate.update(query1, new Object[] {po_EntrydetailsId,
							intPOEntrySeqId,product,subproduct,childproduct,measurement,
							quantity,price,basicamount,tax,taxamount,amountaftertax,
							"0","0",totalamount,hsncode,"0",discount,discountaftertax,vendorProductDesc,pd_Product_Desc,receiveQuanPO

					});
					MarketingData.put(childproduct,String.valueOf(po_EntrydetailsId));
					response="success";

				}
			}
			request.setAttribute("totalAmt",finalAmount);
		}catch(Exception e){
			e.printStackTrace();
		}
		return MarketingData;

	}


	/*==========================================================get save transportation details start==================================*/
	public String getAndsaveMarketingTransportChargesList(String tempPONumber,String strSiteId,HttpServletRequest request,int poEntryId) {
		List<Map<String, Object>> productList = null;

		String sql="";
		String ConveyanceName="";
		String ConveyanceAmount="";
		String GSTTax="";
		String GSTAmount="";
		String AmountAfterTax="";
		//String indentnumber="";
		String id="";
		String transportId="";
		String reponse="";

		try {

			if (StringUtils.isNotBlank(tempPONumber) ) {
				sql += "SELECT STOCD.ID,STOCD.TRANSPORT_ID,STOCM.CHARGE_NAME,STOCD.TRANSPORT_AMOUNT,STOCD.TRANSPORT_GST_PERCENTAGE,STOCD.TRANSPORT_GST_AMOUNT,STOCD.TOTAL_AMOUNT_AFTER_GST_TAX FROM SUMADHURA_TEMP_PO_TRNS_O_CHRGS STOCD,SUMADHURA_TRNS_OTHR_CHRGS_MST STOCM " 
					+" WHERE  STOCD.TRANSPORT_ID=STOCM.CHARGE_ID and STOCD.PO_NUMBER=?";	

				productList = jdbcTemplate.queryForList(sql, new Object[] {tempPONumber});

				if (null != productList && productList.size() > 0) {
					for (Map<?, ?> GetTransportChargesDetails : productList) {
						ConveyanceAmount = GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString();	
						GSTTax = GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE").toString();	
						GSTAmount = GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString();
						AmountAfterTax = GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString();
						ConveyanceName = GetTransportChargesDetails.get("CHARGE_NAME") == null ? "" : GetTransportChargesDetails.get("CHARGE_NAME").toString();
						//indentnumber = GetTransportChargesDetails.get("INDENT_NUMBER") == null ? "" : GetTransportChargesDetails.get("INDENT_NUMBER").toString();
						id = GetTransportChargesDetails.get("ID") == null ? "" : GetTransportChargesDetails.get("ID").toString();
						transportId = GetTransportChargesDetails.get("TRANSPORT_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_ID").toString();
						request.setAttribute("indentnumber","indentnumber");

						String query1 = "INSERT INTO SUMADHURA_PO_TRNS_O_CHRGS_DTLS(ID,TRANSPORT_ID,TRANSPORT_GST_PERCENTAGE,TRANSPORT_GST_AMOUNT,"
							+ "TOTAL_AMOUNT_AFTER_GST_TAX,DATE_AND_TIME,TRANSPORT_AMOUNT,SITE_ID,PO_NUMBER) "
							+ "values( ?, ?, ?, ?, ?, sysdate, ?, ?, ?)";
						int result = jdbcTemplate.update(query1, new Object[] {id,
								transportId,GSTTax,GSTAmount,AmountAfterTax,ConveyanceAmount,
								strSiteId,poEntryId});

						reponse="success";
					}
				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return "reponse";
	}	

	/*=====================================================get temp po save perminent details start==================================================*/
	/*==========================================================get save transportation details start==================================*/
	public String getAndsaveMarketingLocationDetailsList(String tempPONumber,Map<String, String> LocationDetails,String permPoNumber) {
		List<Map<String, Object>> productList = null;

		String sql="";
		String childprodId="";
		String locationId="";
		String hoardingDate="";
		String toDate="";
		String time="";

		String id="";
		String quantity="";
		String reponse="";
		String po_entryDetailsId="";
		String siteId="";
		String amountPerUnit="";
		String totalAmount="";
		int serialNo=0;

		try {

			if (StringUtils.isNotBlank(tempPONumber) ) {
				sql += "SELECT CHILD_PRODUCT_ID,LOCATION_ID,HOARDING_DATE,TIME,QUANTITY,SITE_ID,AMOUNT_PER_UNIT_AFTER_TAX,TOTAL_AMOUNT,PRODUCT_SERIAL_NO  FROM MRKT_TEMP_PO_PROD_LOC_DTLS WHERE PO_NUMBER=?";	

				productList = jdbcTemplate.queryForList(sql, new Object[] {tempPONumber});

				if (null != productList && productList.size() > 0) {
					for (Map<?, ?> GetLocationDetails : productList) {
						childprodId = GetLocationDetails.get("CHILD_PRODUCT_ID") == null ? "" : GetLocationDetails.get("CHILD_PRODUCT_ID").toString();	
						locationId = GetLocationDetails.get("LOCATION_ID") == null ? "" : GetLocationDetails.get("LOCATION_ID").toString();	
						hoardingDate = GetLocationDetails.get("HOARDING_DATE") == null ? "" : GetLocationDetails.get("HOARDING_DATE").toString();
						//toDate = GetLocationDetails.get("TO_DATE") == null ? "" : GetLocationDetails.get("TO_DATE").toString();
						time = GetLocationDetails.get("TIME") == null ? "" : GetLocationDetails.get("TIME").toString();
						quantity = GetLocationDetails.get("QUANTITY") == null ? "" : GetLocationDetails.get("QUANTITY").toString();

						siteId=GetLocationDetails.get("SITE_ID") == null ? "0" : GetLocationDetails.get("SITE_ID").toString();
						amountPerUnit=GetLocationDetails.get("AMOUNT_PER_UNIT_AFTER_TAX") == null ? "0" : GetLocationDetails.get("AMOUNT_PER_UNIT_AFTER_TAX").toString();
						totalAmount=GetLocationDetails.get("TOTAL_AMOUNT") == null ? "0" : GetLocationDetails.get("TOTAL_AMOUNT").toString();
						serialNo=Integer.parseInt(GetLocationDetails.get("PRODUCT_SERIAL_NO") == null ? "0" : GetLocationDetails.get("PRODUCT_SERIAL_NO").toString());

						if(LocationDetails.containsKey(childprodId)){
							po_entryDetailsId=LocationDetails.get(childprodId).toString();
						}
						String query1 = "INSERT INTO MRKT_PO_PROD_LOC_DTLS(CHILD_PRODUCT_ID,LOCATION_ID,HOARDING_DATE,TIME,"
							+" QUANTITY,CREATION_DATE,PO_ENTRY_DETAILS_ID,PO_NUMBER,PO_PROD_LOC_DTLS_ID,SITE_ID,AMOUNT_PER_UNIT_AFTER_TAX,TOTAL_AMOUNT,PRODUCT_SERIAL_NO) values(?, ?, ?, ?, ?,sysdate,?,?,MRKT_PO_PROD_LOC_DTL_SEQ.nextval,?,?,?,?)";
						int result = jdbcTemplate.update(query1, new Object[] {childprodId,
								locationId,DateUtil.convertToJavaDateFormat(hoardingDate),time,quantity,
								po_entryDetailsId,permPoNumber,siteId,amountPerUnit,totalAmount,serialNo});

						reponse="success";
					}
				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return "reponse";
	}	
	/*================================================after create marketing po then get emails start======================================================*/

	public List<String>  getAllEmployeeEmailsMarketingDepartment(String deptId) {

		List<Map<String, Object>> dbIndentDts = null;
		String strEmailId = "";
		List<String> objList = new ArrayList<String>();

		String query = "select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS SED where DEPT_ID LIKE '%"+deptId+"%' ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {});

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

	/*================================================after create marketing po then get emails END======================================================*/
	// inactive temp po
	public int updateMarketingpoEntrydetails(String poNumber,String passwdForMail,String deliveryDate) {
		int result=0;

		String query1 = "UPDATE SUMADHURA_TEMP_PO_ENTRY set PO_STATUS = 'I',PASSWORD_MAIL=?,DELIVERY_DATE=? WHERE PO_NUMBER = ?";
		result = jdbcTemplate.update(query1, new Object[] {passwdForMail,StringUtils.isBlank(deliveryDate)?null:DateUtil.convertToJavaDateFormat(deliveryDate),poNumber });

		return result;

	}
	public List<Map<String,Object>> getAllViewExpenditures(String invoiceId) {
		logger.info("***** The control is inside the getAllViewExpenditures in MarketingDepartmentDaoImpl ******");


		String query = new StringBuilder("SELECT")
		//.append(" WHERE MRKT_EXP.INVOICE_ID ="+invoiceId)

		.append("  CHD_PRD.NAME AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID, ")
		.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID, ")
		.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE, ")
		.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ")
		.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT ")
		.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
		.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
		.append(" WHERE  MRKT_EXP.STATUS = 'A'  ")
		.append(" AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID ")
		.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID").toString();

		logger.info("**** The getAllViewExpenditures query is *****"+query);
		List<Map<String,Object>> expendituresList = jdbcTemplate.queryForList(query);
		logger.info("**** The expendituresList object is ****"+expendituresList);
		return expendituresList;

	}

	public List<Map<String,Object>> getAllViewExpenditures(String invoiceFromDate,String invoiceToDate){
		logger.info("***** The control is inside the getAllViewExpenditures in   invoiceFromDate&invoiceToDate MarketingDepartmentDaoImpl ******");
		logger.info("***** The input invoiceFromDate & invoiceToDate values is ******"+invoiceFromDate+"-------->"+invoiceToDate);
		String query ="";
		if(StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)){
			 query = new StringBuilder("SELECT")
			.append(" MRKT_EXPD.INVOICE_ID  AS INVOICE_ID,")
			.append(" MRKT_EXPD.INVOICE_AMOUNT AS INVOICE_AMOUNT,")
			.append(" MRKT_EXPD.INVOICE_DATE AS INVOICE_DATE")
			.append(" FROM MRKT_EXPENDATURE  MRKT_EXPD")
			.append(" WHERE CAST(MRKT_EXPD.INVOICE_DATE AS DATE) BETWEEN")
			.append(" TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceFromDate)+"','DD-MM-YY')")
			.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceToDate)+"','DD-MM-YY')")
			.append(" AND MRKT_EXPD.STATUS = 'A' ")
			.toString();
			}
		if(StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isBlank(invoiceToDate)){
			query = new StringBuilder("SELECT")
			.append(" MRKT_EXPD.INVOICE_ID  AS INVOICE_ID,")
			.append(" MRKT_EXPD.INVOICE_AMOUNT AS INVOICE_AMOUNT,")
			.append(" MRKT_EXPD.INVOICE_DATE AS INVOICE_DATE")
			.append(" FROM MRKT_EXPENDATURE  MRKT_EXPD")
			.append(" WHERE CAST(MRKT_EXPD.INVOICE_DATE AS DATE) >= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceFromDate)+"','DD-MM-YY')")
			.append(" AND MRKT_EXPD.STATUS = 'A' ")
			.toString();
		}
		if(StringUtils.isBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)){
			query = new StringBuilder("SELECT")
			.append(" MRKT_EXPD.INVOICE_ID  AS INVOICE_ID,")
			.append(" MRKT_EXPD.INVOICE_AMOUNT AS INVOICE_AMOUNT,")
			.append(" MRKT_EXPD.INVOICE_DATE AS INVOICE_DATE")
			.append(" FROM MRKT_EXPENDATURE  MRKT_EXPD")
			.append(" WHERE CAST(MRKT_EXPD.INVOICE_DATE AS DATE) <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceToDate)+"','DD-MM-YY')")
			.append(" AND MRKT_EXPD.STATUS = 'A' ")
			.toString();
		}
		logger.info("**** The getAllViewExpenditures  invoiceFromDate&invoiceToDate query is *****"+query);			
		List<Map<String,Object>> expendituresList = jdbcTemplate.queryForList(query);
		logger.info("**** The expendituresList object is ****"+expendituresList);
		return expendituresList;
	}

	public List<Map<String,Object>> getViewMyHoardingDetails(String fromDate,String toDate,String Site) {
		logger.info("***** The control is inside the getViewMyHoardingDetails in MarketingDepartmentDaoImpl ******");
		logger.info("**** The input values for the fromDate & toDate & Site ****"+fromDate+"----"+"--------"+toDate+"-------"+Site);

		StringBuilder query = new StringBuilder("SELECT ")
		.append(" CP.NAME AS CHILD_PRODUCT_NAME,")
		.append(" MHD.HOARDING_AREA AS HOARDING_AREA,")
		.append(" MPL.HOARDING_DATE AS HOARDING_DATE,")
		.append(" MPL.QUANTITY AS QUANTITY,")
		.append(" MPL.TOTAL_AMOUNT AS AMOUNT,")
		.append(" MPL.AMOUNT_PER_UNIT_AFTER_TAX AS RATE,")
		.append(" SITE.SITE_NAME AS SITE_NAME")
		.append(" FROM")
		.append(" MRKT_PO_PROD_LOC_DTLS MPL,")
		.append(" CHILD_PRODUCT CP,")
		.append(" MRKT_HOARDING_DTLS MHD,")
		.append(" SITE SITE")
		.append(" WHERE");
		if (fromDate != "" && toDate != "" && !(Site.equalsIgnoreCase("-1"))) {
			query.append(" CAST(MPL.HOARDING_DATE AS DATE) BETWEEN ").append(" TO_DATE('" + fromDate + "','DD-MM-YY')")
			.append(" AND TO_DATE('" + toDate + "','DD-MM-YY')").append(" AND SITE.SITE_ID = " + Site);
		} else if (fromDate != "" && toDate != "") {
			query.append("  to_date(to_char(MPL.HOARDING_DATE ,'dd-MM-yy'),'dd-MM-yy') BETWEEN") 
			.append(" TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')");
		} else if (fromDate != "" && !(Site.equalsIgnoreCase("-1"))) {
			query.append(" to_char(CAST(MPL.HOARDING_DATE AS DATE)) = ").append(" TO_DATE('" + fromDate + "','DD-MM-YY')")
			.append(" AND SITE.SITE_ID = " + Site);
		} else if (toDate != "" && !(Site.equalsIgnoreCase("-1"))) {
			query.append(" CAST(MPL.HOARDING_DATE AS DATE) <= ").append(" TO_DATE('" + toDate + "','DD-MM-YY')")
			.append(" AND SITE.SITE_ID = " + Site);
		} else if (fromDate != "") {
			query.append(" to_char(CAST(MPL.HOARDING_DATE AS DATE)) = ").append(" TO_DATE('" + fromDate + "','DD-MM-YY')");
		} else if (toDate != "") {
			query.append(" CAST(MPL.HOARDING_DATE AS DATE) <= ").append(" TO_DATE('" + toDate + "','DD-MM-YY')");
		}else if(!(Site.equalsIgnoreCase("-1"))) {
			query.append("  SITE.SITE_ID = " + Site);	
		}

		query.append(" AND MPL.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID")
		.append(" AND MPL.LOCATION_ID = MHD.HOARDING_ID")
		.append("  AND MPL.SITE_ID = SITE.SITE_ID");	

		logger.info("**** The getViewMyHoardingDetails  query is *****"+query);			
		List<Map<String,Object>> HoardingDetailsList = jdbcTemplate.queryForList(query.toString());
		logger.info("**** The HoardingDetailsList object is ****"+HoardingDetailsList);
		return HoardingDetailsList;


	}
	public void updateExpenditure() {
		logger.info("***** The control is inside the updateExpenditure service  in MarketingDepartmentServiceImpl ******");


	}
	// here the month came like only ex: 02-2019 so it will retrive the data
	public List<MarketingDeptBean> getAvailableAreaForSaleOnMonthWise(String strMonth,String siteIds){
		List<MarketingDeptBean> marketingBeanList = new ArrayList<MarketingDeptBean>();
		try{
			int intSerialNo = 1;
			List<Map<String, Object>> dbIndentDts = null;
			String query="";
			//String query = "select distinct(INDENT_PROCESS_ID) AS INDENT_PROCESS_ID ,PROCESS_INTIATED_SITE,CREATION_DATE from SUMADHURA_CNTL_INDENT_REQ_DTLS where  STATUS = 'A' and INDENT_PROCESS_ID= ? ";
			if(siteIds==null || siteIds.equals("")){
				query = "select MMAA.SITE_ID,MMAA.UOM,MMAA.TOTAL_AREA,MMAA.AVAILABLE_AREA,S.SITE_NAME,MMAA.STATUS    "+
				" from  MRKT_MONTHLY_AVAIL_AREA MMAA, SITE S "+
				" where MMAA.SITE_ID = S.SITE_ID  and MONTH_YEAR ='"+strMonth+"' and MMAA.STATUS = 'A' ";
			}else{
				query = "select MMAA.SITE_ID,MMAA.UOM,MMAA.TOTAL_AREA,MMAA.AVAILABLE_AREA,S.SITE_NAME,MMAA.STATUS    "+
				" from  MRKT_MONTHLY_AVAIL_AREA MMAA, SITE S "+
				" where MMAA.SITE_ID = S.SITE_ID  and MONTH_YEAR ='"+strMonth+"' and MMAA.STATUS = 'A' AND MMAA.SITE_ID in('"+siteIds+"')";
			}
			dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {});

			for(Map<String, Object> prods : dbIndentDts) {
				MarketingDeptBean objMarketingBean = new MarketingDeptBean();

				objMarketingBean.setIntSerialNo(intSerialNo);
				objMarketingBean.setSiteId(prods.get("SITE_ID")==null ? "0" : prods.get("SITE_ID").toString());
				objMarketingBean.setSiteName(prods.get("SITE_NAME")==null ? "0" : prods.get("SITE_NAME").toString());
				objMarketingBean.setMeasurementName(prods.get("UOM")==null ? "Sqft" : prods.get("UOM").toString());
				objMarketingBean.setDoubleAVailableArea(Double.valueOf(prods.get("AVAILABLE_AREA")==null ? "0" : prods.get("AVAILABLE_AREA").toString()));
				objMarketingBean.setDoubleTotalArea(Double.valueOf(prods.get("TOTAL_AREA")==null ? "0" : prods.get("TOTAL_AREA").toString()));
				objMarketingBean.setStrStatus(prods.get("STATUS")==null ? "" : prods.get("STATUS").toString());
				marketingBeanList.add(objMarketingBean);
				intSerialNo++;
			}

		}
		catch(Exception e){
			e.printStackTrace();
		}

		return marketingBeanList;
	}

	// this method called at the time of calculate expenditure time user select the site as location details or branding or  multiple site
	public   String getAvailableAreaForSale(  String strLocation,String month_year){

		String strResponse = "";
		try{
			List<Map<String, Object>> dbIndentDts = null;
			String strSiteName = "";
			String strUOM = "";
			String strTotalArea = "";
			String strAvailArea = "";
			String strSiteId = "";
			String query = "";

			//strLocation = "All" means available area selecting branding wise
			if(!strLocation.equals("All")){ // here except branding wise remain selected  getting the data

				query = "select S.SITE_ID,SITE_NAME,UOM, TOTAL_AREA, AVAILABLE_AREA , " +
				" (select sum(IMAA.AVAILABLE_AREA) from  MRKT_MONTHLY_AVAIL_AREA IMAA ,SITE ISITE  " +
				" where IMAA.STATUS = 'A' and  IMAA.MONTH_YEAR = ? and ISITE.ADDRESS = ?  and IMAA.SITE_ID = ISITE.SITE_ID  ) as Total_Aval_Area from MRKT_MONTHLY_AVAIL_AREA MMAA,SITE S where MMAA.SITE_ID = S.SITE_ID "+
				" and MMAA.STATUS = 'A' and  "+
				" MMAA.MONTH_YEAR = ? and S.ADDRESS = ? ";
				dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {month_year,strLocation,month_year,strLocation});
			}else if(strLocation.equals("All")){// fetching the data without address

				query = "select S.SITE_ID,SITE_NAME,UOM, TOTAL_AREA, AVAILABLE_AREA , " +
				" (select sum(AVAILABLE_AREA) from MRKT_MONTHLY_AVAIL_AREA  "+
				" where STATUS = 'A' and  MONTH_YEAR = ?) as Total_Aval_Area from MRKT_MONTHLY_AVAIL_AREA MMAA,SITE S where MMAA.SITE_ID = S.SITE_ID "+
				" and MMAA.STATUS = 'A' and  "+
				" MMAA.MONTH_YEAR = ?  ";
				dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {month_year,month_year});
			}




			if(dbIndentDts.size() > 0){
				strResponse += "<xml>";


				for(Map<String, Object> prods : dbIndentDts) {


					strSiteId =  prods.get("SITE_ID")==null ? "0" : prods.get("SITE_ID").toString();
					strSiteName =  prods.get("SITE_NAME")==null ? "0" : prods.get("SITE_NAME").toString();
					strUOM =  prods.get("UOM")==null ? "0" : prods.get("UOM").toString();
					strTotalArea =  prods.get("Total_Aval_Area")==null ? "0" : prods.get("Total_Aval_Area").toString();
					strAvailArea =  prods.get("AVAILABLE_AREA")==null ? "0" : prods.get("AVAILABLE_AREA").toString();


					strResponse += "<site> <siteId>"+strSiteId+"</siteId> <siteName>"+strSiteName+"</siteName> <availableArea>"+strAvailArea+"</availableArea>"+
					" <totalArea>"+strTotalArea+"</totalArea> </site> ";
				}

				strResponse += "</xml>";
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return strResponse;
	}

	// store newly available area with measurement like 'sqft' which is default added  
	public int insertNewAvailableArea(String strCreatedBy,String strSiteId,String strUOM,double doubleTotalArea,double doubleAvailArea,String strMonth_Year,String status){

		int intSiteId = Integer.parseInt(strSiteId);
		String strAvailArea = String.valueOf(doubleAvailArea);
		String strTotalArea = String.valueOf(doubleTotalArea);

		String query = "insert into MRKT_MONTHLY_AVAIL_AREA(MONTHLY_AVAIL_AREA_ID,SITE_ID,UOM,TOTAL_AREA,AVAILABLE_AREA,STATUS, MONTH_YEAR,CREATED_BY,CREATED_DATE)" +
		" values(MRKT_MONTHLY_AVAIL_AREA_SEQ.nextval,?,?,?,?,?,?,?,sysdate)";

		int intsavedCount  = jdbcTemplate.update(query, new Object[] {intSiteId,strUOM,strTotalArea,strAvailArea,status,strMonth_Year,strCreatedBy});

		return intsavedCount;
	}

	public int updateAvailableArea(String strUpdatedBy,String strSiteId,String strUOM,double doubleTotalArea,double doubleAvailArea,String strMonth_Year,String strStatus){


		String query = "update MRKT_MONTHLY_AVAIL_AREA set TOTAL_AREA = ? ,AVAILABLE_AREA = ?,UOM = ?,STATUS = ?,UPDATED_BY = ? "+
		",UPDATED_DATE = sysdate where SITE_ID =  ? and MONTH_YEAR = ? AND STATUS='A'";

		int intUpdateCount  = jdbcTemplate.update(query, new Object[] {doubleTotalArea,doubleAvailArea,strUOM,strStatus,strUpdatedBy,strSiteId,strMonth_Year});

		return intUpdateCount;
	}

	public boolean inactiveAvailableArea(String strUpdatedBy,String strSiteId,String strMonth_Year,String strStatus,List<String> hydList,List<String> bngList,HttpServletRequest request){
		//String bngState=validateParams.getProperty("STATE_BNG") == null ? "" : validateParams.getProperty("STATE_BNG").toString();
		List<Map<String, Object>> dbHydDts = null;
		List<Map<String, Object>> dbBngDts = null;
		
		List<Map<String, Object>> dbDtsHyd = null;
		List<Map<String, Object>> dbDtsBng = null;
		List<Map<String, Object>> dbDtsBoth = null;
		boolean status=false;
		//boolean check=false;
		//String state=validateParams.getProperty("STATE_HYD") == null ? "" : validateParams.getProperty("STATE_HYD").toString();
		String locationSites=validateParams.getProperty("HYD_SITES_LOCATION") == null ? "" : validateParams.getProperty("HYD_SITES_LOCATION").toString();
		locationSites=locationSites.replace(",","','");
		String hydSiteNames="";
		String bngSitenames="";
		String bothsites="";
		Set<String> set=new HashSet<String>(hydList);
		set.addAll(bngList);
		String modifiedMonth=strMonth_Year.replaceFirst("0","");
		//strMonth_Year=
		modifiedMonth=modifiedMonth.trim();
				//"siteIds=siteIds.replace(",","','");
		//List<String,String> hydData=
			
		String sql="SELECT SITE_ID FROM MRKT_MONTHLY_AVAIL_AREA WHERE STATUS='A' AND MONTH_YEAR='"+strMonth_Year+"' AND SITE_ID IN ('"+locationSites+"') ";	
		dbHydDts = jdbcTemplate.queryForList(sql, new Object[] {});
		
		String sql1="SELECT SITE_ID FROM MRKT_MONTHLY_AVAIL_AREA WHERE STATUS='A' AND MONTH_YEAR='"+strMonth_Year+"' AND SITE_ID NOT IN ('"+locationSites+"') ";	
		dbBngDts = jdbcTemplate.queryForList(sql1, new Object[] {});
		int length=dbHydDts.size()+dbBngDts.size();
		if(dbHydDts.size()==hydList.size()){
			for(String list:hydList){
				hydSiteNames+=list+",";
			}
			if(hydSiteNames!=null && !hydSiteNames.equals("")){
				hydSiteNames=hydSiteNames.substring(0, hydSiteNames.length()-1);
				hydSiteNames=hydSiteNames.replace(",","','");
			}
			String location="select DISTINCT(ME.EXPENDATURE_ID),ME.INVOICE_ID,ME.INVOICE_AMOUNT from MRKT_EXPENDATURE ME,MRKT_EXPENDATURE_DTLS MED "
						+" where  ME.EXPENDATURE_MONTH='"+modifiedMonth+"' and ME.EXPENDATURE_TYPE ='LocationWise' "
						+" AND ME.EXPENDATURE_ID=MED.EXPENDATURE_ID AND MED.SITE_ID IN ('"+hydSiteNames+"') AND MED.STATUS='A'";	
			dbDtsHyd = jdbcTemplate.queryForList(location, new Object[] {});
			if(dbDtsHyd.size()>0){
				status=true;
				return status;
			}
			
		}if(dbBngDts.size()==bngList.size()){
			for(String list:bngList){
				bngSitenames+=list+",";
			}
			if(bngSitenames!=null && !bngSitenames.equals("")){
				bngSitenames=bngSitenames.substring(0, bngSitenames.length()-1);
				bngSitenames=bngSitenames.replace(",","','");
			}
			
			String location="select DISTINCT(ME.EXPENDATURE_ID),ME.INVOICE_ID,ME.INVOICE_AMOUNT from MRKT_EXPENDATURE ME,MRKT_EXPENDATURE_DTLS MED "
				+" where  ME.EXPENDATURE_MONTH='"+modifiedMonth+"' and ME.EXPENDATURE_TYPE ='LocationWise' "
				+" AND ME.EXPENDATURE_ID=MED.EXPENDATURE_ID AND MED.SITE_ID IN ('"+bngSitenames+"') AND MED.STATUS='A'";	
			dbDtsBng = jdbcTemplate.queryForList(location, new Object[] {});
			if(dbDtsBng.size()>0){
				status=true;
				return status;
			}
			
		}
		if(set.size()==length){
			for(String s:set){
				bothsites+=s+",";
			}
			if(bothsites!=null && !bothsites.equals("")){
				bothsites=bothsites.substring(0, bothsites.length()-1);
				bothsites=bothsites.replace(",","','");
			}
			String brandWise="select DISTINCT(ME.EXPENDATURE_ID),ME.INVOICE_ID,ME.INVOICE_AMOUNT from MRKT_EXPENDATURE ME,MRKT_EXPENDATURE_DTLS MED "
				+" where  ME.EXPENDATURE_MONTH='"+modifiedMonth+"' and ME.EXPENDATURE_TYPE ='BrandingWise' "
				+" AND ME.EXPENDATURE_ID=MED.EXPENDATURE_ID AND MED.SITE_ID IN ('"+bothsites+"') AND MED.STATUS='A'";	
			dbDtsBoth = jdbcTemplate.queryForList(brandWise, new Object[] {});
		
		if(dbDtsBoth.size()>0){
			status=true;
			return status;
		}
		
		
		}
		
		for(String s : set){
			String query = "update MRKT_MONTHLY_AVAIL_AREA set  STATUS = ? ,UPDATED_BY = ? ,UPDATED_DATE = sysdate where SITE_ID = ? and MONTH_YEAR = ? ";

			int intUpdateCount  = jdbcTemplate.update(query, new Object[] {strStatus,strUpdatedBy,s,strMonth_Year});
			}
		
		/*String query = "update MRKT_MONTHLY_AVAIL_AREA set  STATUS = ? ,UPDATED_BY = ? ,UPDATED_DATE = sysdate where SITE_ID = ? and MONTH_YEAR = ? ";

		int intUpdateCount  = jdbcTemplate.update(query, new Object[] {strStatus,strUpdatedBy,strSiteId,strMonth_Year});*/
		
		return status;
	}


	public List<Map<String, Object>> getMonthlyWiseExpendatureInvoices(String  strExpendatureType,String strMonth_Year){
		List<Map<String, Object>> dbIndentDts = null;
		try{
			
			//int intSerialNo = 1;

			//String strExpendatureTypeArray [] =  {"a","b"};
			strMonth_Year=strMonth_Year.replaceFirst("0","");
			strMonth_Year=strMonth_Year.trim();
			//String query = "select distinct(INDENT_PROCESS_ID) AS INDENT_PROCESS_ID ,PROCESS_INTIATED_SITE,CREATION_DATE from SUMADHURA_CNTL_INDENT_REQ_DTLS where  STATUS = 'A' and INDENT_PROCESS_ID= ? ";
			if(strExpendatureType.equalsIgnoreCase("Locationwise")){
				String query = "select INVOICE_ID,EXPENDATURE_ID,INVOICE_AMOUNT from MRKT_EXPENDATURE where  EXPENDATURE_MONTH=? and EXPENDATURE_TYPE =  ? ";
				dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {strMonth_Year,"LocationWise"});	
			}else{
			String query = "select INVOICE_ID,EXPENDATURE_ID,INVOICE_AMOUNT from MRKT_EXPENDATURE where  EXPENDATURE_TYPE in('LocationWise','BrandingWise') and EXPENDATURE_MONTH=?";
			
			dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {strMonth_Year});
		}
		}catch(Exception e){
			e.printStackTrace();
		}

		return dbIndentDts;
	}


	public List<Map<String, Object>> getExpendatureDetilsInvoiceProductWise(int strExpendatureId,List<String> removeList){
		List<Map<String, Object>> dbIndentDts = null;
	
		try{
			String sites="";
			/********************************* this is used to inactive all the sites *****************************************/
			if(removeList!=null && removeList.size()>0){
				for(int i=0;i<removeList.size();i++){
					sites+=removeList.get(i)+",";
				}
				sites =  sites.substring(0,sites.length()-1);
				sites=sites.replace(",","','");
				
				String sql = "UPDATE MRKT_EXPENDATURE_DTLS SET STATUS='I' WHERE SITE_ID IN ('"+sites+"') AND EXPENDATURE_ID=?";

				int intCount = jdbcTemplate.update(sql, new Object[] {strExpendatureId});
			
			}
			
			//String query = "select distinct(INDENT_PROCESS_ID) AS INDENT_PROCESS_ID ,PROCESS_INTIATED_SITE,CREATION_DATE from SUMADHURA_CNTL_INDENT_REQ_DTLS where  STATUS = 'A' and INDENT_PROCESS_ID= ? ";

			String query = "select CHILD_PRODUCT_ID,sum(AMOUNT) as TOTAL_AMOUNT,QTY,ME.EXPENDATURE_TYPE from MRKT_EXPENDATURE_DTLS MED,MRKT_EXPENDATURE ME where MED.EXPENDATURE_ID = ME.EXPENDATURE_ID and ME.EXPENDATURE_ID = ? and MED.STATUS='A' group by CHILD_PRODUCT_ID ,QTY,ME.EXPENDATURE_TYPE ";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {strExpendatureId});
		}catch(Exception e){
			e.printStackTrace();
		}

		return dbIndentDts;
	}
	// getting total available area in that respective month
	public double getTotalAvailableAreaInMonth(String strMonth,String siteIds){
		List<Map<String, Object>> dbIndentDts = null;
		double doubleTotalAvailArea = 0.0;
		try{


			String query = "select sum(AVAILABLE_AREA) as TOTAL_AVAL_AREA from MRKT_MONTHLY_AVAIL_AREA where MONTH_YEAR = ? and STATUS = 'A' and SITE_ID in('"+siteIds+"')";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {strMonth});

			for(Map<String, Object> prods : dbIndentDts) {


				doubleTotalAvailArea =  Double.valueOf(prods.get("TOTAL_AVAL_AREA")==null ? "0" : prods.get("TOTAL_AVAL_AREA").toString());

			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return doubleTotalAvailArea;
	}


	public int insertOrUpdateExpendaturedtlsTable( int intExpendatureId,String strChildProductId,double doubleQty,double doubleRate,double doubleAmount,String strSiteId,String user_id){

		int intCount = 0 ;
		String oldAmount="";
		String strTotalModified="";
		try{

			String query = "select count(1) from MRKT_EXPENDATURE_DTLS where EXPENDATURE_ID = ? and SITE_ID = ? and CHILD_PRODUCT_ID=? AND STATUS='A' ";

			intCount = jdbcTemplate.queryForInt(query, new Object[] {intExpendatureId,strSiteId,strChildProductId});


			if(intCount > 0){
				String sql1="SELECT AMOUNT FROM MRKT_EXPENDATURE_DTLS WHERE EXPENDATURE_ID ='"+intExpendatureId+"' and SITE_ID ='"+strSiteId+"' and CHILD_PRODUCT_ID='"+strChildProductId+"' and STATUS='A'";
				oldAmount = jdbcTemplate.queryForObject(sql1, String.class);
				strTotalModified="user:"+user_id+",old:"+oldAmount+",new:"+String.valueOf(doubleAmount)+"@@";
				
				query = "update  MRKT_EXPENDATURE_DTLS set AMOUNT = ? ,RATE  = ?,MODIFIED_DETAILS=? where   EXPENDATURE_ID = ? and SITE_ID = ? and CHILD_PRODUCT_ID=? and STATUS='A'";

				intCount = jdbcTemplate.update(query, new Object[] {doubleAmount,doubleRate,strTotalModified,intExpendatureId,strSiteId,strChildProductId});

			}else{

				query = "insert into  MRKT_EXPENDATURE_DTLS(EXPENDATURE_ID,EXPENDATURE_DTLS_ID,CHILD_PRODUCT_ID,QTY,RATE,AMOUNT,SITE_ID,STATUS)" +
				" values(?,MRKT_EXPENDATURE_DTLS_SEQ.nextval,?,?,?,?,?,'A')";

				intCount = jdbcTemplate.update(query, new Object[] {intExpendatureId,strChildProductId,doubleQty,doubleRate,doubleAmount,strSiteId});


			}


		}catch(Exception e){
			e.printStackTrace();
		}

		return intCount;
	}

	/*@Override
	public List<String> getAllEmployeeEmailsMarketingDepartment(String deptId) {
		// TODO Auto-generated method stub
		return null;
	}*/
	/*	=====================================================================set location area for create Po Time===================================================*/
	@Override
	public Map<String, String> getLocationData(String siteName) {
		//	System.out.println("in dao:"+employeeName);
		StringBuffer sb = null;
		List<Map<String, Object>> dbAreaList = null;		

		String siteId="";
		String strSiteName="";
		Map<String, String> Area = new TreeMap<String, String>();

		log.debug("Vendor Name = "+siteName);
		sb = new StringBuffer();

		String locationAreaInfoQry = "SELECT SITE_ID,SITE_NAME FROM SITE WHERE ADDRESS=? and STATUS='ACTIVE' ";
		log.debug("Query to fetch employee info = "+locationAreaInfoQry);

		dbAreaList = jdbcTemplate.queryForList(locationAreaInfoQry, new Object[]{siteName});
		for(Map<String, Object> prods : dbAreaList) {
			siteId=prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString();
			strSiteName=prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString();

			Area.put(siteId,strSiteName);
		}
		return Area;
	}

	public int getExpenditureId() {
		String query = "select MRKT_EXPENDATURE_SEQ.nextval  from dual";
		int seqId = jdbcTemplate.queryForInt(query);
		return seqId;
	}


	public int getExpenditureDetailsId() {
		String query = "select MRKT_EXPENDATURE_DTLS_SEQ.nextval  from dual";
		int seqId = jdbcTemplate.queryForInt(query);
		return seqId;
	}
	public int insertAndUpdatePrevMarketExpenditure(MarketingDeptBean objMarketingExpenditure){

		logger.info("***** The control is inside the insertMarketExpenditure in MarketingDepartmentServiceImpl *****");
		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MMM-yyyy");
		StringBuilder query = null;

		try {

			query = new StringBuilder("insert into MRKT_EXPENDATURE ")
			.append("(EXPENDATURE_ID,INVOICE_ID,INVOICE_AMOUNT,ENTRY_DATE,CREATED_BY,EXPENDATURE_MONTH,EXPENDATURE_TYPE,STATUS,INVOICE_DATE )")
			.append(" values (")
			.append( objMarketingExpenditure.getExpenditureId()+",'")
			.append( objMarketingExpenditure.getInvoiceNumber()+"','")
			.append( objMarketingExpenditure.getInvoiceAmount()+"',")
			.append( " SYSTIMESTAMP  ,'")
			.append( objMarketingExpenditure.getCreatedBy()+"','")
			.append( objMarketingExpenditure.getExpenditureMonth()+"','")
			.append( objMarketingExpenditure.getExpenditureType()+"','")
			.append( objMarketingExpenditure.getDeliveryStatus()+"','")
			.append( sdf1.format(sdf.parse(objMarketingExpenditure.getInvoiceDate()))+"')");
		}catch(Exception ex) {
			ex.printStackTrace();
		}

		logger.info("****** The query of insertMarketExpenditure is **********"+query );


		result = jdbcTemplate.update(query.toString());

		jdbcTemplate.update(" update MRKT_EXPENDATURE set STATUS = 'M' where EXPENDATURE_ID = "+objMarketingExpenditure.getPreviousexpendatureId());

		logger.info("****** The value of the result object in insertMarketExpenditure is **********"+result);
		return result;

	}

	public int insertMarketExpenditureDtls(MarketingDeptBean objMarketingExpenditureDtls) {
		String query = "insert into MRKT_EXPENDATURE_DTLS (EXPENDATURE_ID,EXPENDATURE_DTLS_ID,"
			+ "CHILD_PRODUCT_ID,QTY,RATE,AMOUNT,HOARDING_ID,FROM_DATE,TO_DATE,TIME,SITE_ID) "
			+ "  values(?,?,?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(query, new Object[] {

				objMarketingExpenditureDtls.getExpenditureId(),
				objMarketingExpenditureDtls.getExpenditureDetailsId(),
				objMarketingExpenditureDtls.getChild_ProductId(),
				objMarketingExpenditureDtls.getQuantity(),
				objMarketingExpenditureDtls.getPrice(),
				objMarketingExpenditureDtls.getAmount(),
				objMarketingExpenditureDtls.getHoardingId(),
				objMarketingExpenditureDtls.getFromDate(),
				objMarketingExpenditureDtls.getToDate(),
				objMarketingExpenditureDtls.getTime(),
				objMarketingExpenditureDtls.getSiteId()
		});

	}	

	@Override
	public int[] insertMarketExpenditureDtlsDuringUpdateExpendatureTime( final List<MarketingDeptBean> objMarketingExpenditureDtlsList) {

		String query = "insert into MRKT_EXPENDATURE_DTLS (EXPENDATURE_ID,EXPENDATURE_DTLS_ID,"
			+ "CHILD_PRODUCT_ID,QTY,RATE,AMOUNT,HOARDING_ID,FROM_DATE,TO_DATE,TIME,SITE_ID) "
			+ "  values(?,?,?,?,?,?,?,?,?,?,?)";

		int[] updateCounts = jdbcTemplate.batchUpdate(
				query,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setLong(1,objMarketingExpenditureDtlsList.get(i).getExpenditureId());
						ps.setLong(2,objMarketingExpenditureDtlsList.get(i).getExpenditureDetailsId());
						ps.setString(3, objMarketingExpenditureDtlsList.get(i).getChild_ProductId());
						ps.setString(4, objMarketingExpenditureDtlsList.get(i).getQuantity());
						ps.setString(5, objMarketingExpenditureDtlsList.get(i).getPrice());
						ps.setString(6, objMarketingExpenditureDtlsList.get(i).getAmount());
						ps.setString(7, objMarketingExpenditureDtlsList.get(i).getHoardingId());
						ps.setString(8, objMarketingExpenditureDtlsList.get(i).getFromDate());
						ps.setString(9, objMarketingExpenditureDtlsList.get(i).getToDate());
						ps.setString(10, objMarketingExpenditureDtlsList.get(i).getToDate());
						ps.setString(11, objMarketingExpenditureDtlsList.get(i).getSiteId());

					}

					public int getBatchSize() {
						return objMarketingExpenditureDtlsList.size();
					}
				} );
		return updateCounts;
	}

	public int[] MarketExpenditureDtlsbatchUpdate( final List<MarketingDeptBean> objMarketingExpenditureList) {

		String query="insert into MRKT_EXPENDATURE (EXPENDATURE_ID,INVOICE_ID,INVOICE_AMOUNT,"
			+ "ENTRY_DATE,CREATED_BY,EXPENDATURE_MONTH,EXPENDATURE_TYPE)  "
			+ "  values(?,?,?,sysdate,?,?,?)";
		int[] updateCounts = jdbcTemplate.batchUpdate(
				query,
				new BatchPreparedStatementSetter() {
					public void setValues(PreparedStatement ps, int i) throws SQLException {
						ps.setInt(1,objMarketingExpenditureList.get(i).getExpenditureId());
						ps.setString(2,objMarketingExpenditureList.get(i).getInvoiceNumber());
						ps.setString(3,objMarketingExpenditureList.get(i).getInvoiceAmount());
						ps.setTimestamp(4, new java.sql.Timestamp(new java.util.Date().getTime()));
						//   ps.setTimestamp(4,new Timestamp(System.currentTimeMillis()));
						ps.setString(5,objMarketingExpenditureList.get(i).getCreatedBy());
						ps.setString(6,objMarketingExpenditureList.get(i).getExpenditureMonth());
						ps.setString(7,objMarketingExpenditureList.get(i).getExpenditureType());
					}

					public int getBatchSize() {
						return objMarketingExpenditureList.size();
					}
				} );
		return updateCounts;
	}


	public  List<Map<String,Object>> expenditureDetails(Integer expendatureId) {
		logger.info("***** The control is inside the expenditureDetails in MarketingDepartmentDaoImpl ******");
		logger.info("**** The input values for the expendatureId *****"+expendatureId);

		String query = new StringBuilder("SELECT ")
		.append(" MKT_EXP.INVOICE_ID  AS INVOICE_ID,")
		.append(" MKT_EXP.INVOICE_AMOUNT AS INVOICE_AMOUNT,")
		.append(" MKT_EXP.EXPENDATURE_MONTH AS EXPENDATURE_MONTH,")
		.append(" MKT_EXP.INVOICE_DATE AS INVOICE_DATE")
		.append(" FROM MRKT_EXPENDATURE MKT_EXP WHERE MKT_EXP.EXPENDATURE_ID = '"+expendatureId+"' AND STATUS='A'")
		.toString();

		List<Map<String,Object>> expenditureDetailsList = jdbcTemplate.queryForList(query);
		logger.info("**** The expenditureDetailsList object values is *****"+expenditureDetailsList);
		return expenditureDetailsList;


	}
	/*	=====================================================================set location area for create Po Time end===================================================*/

	@Override
	public LinkedList getAllViewExpenditures(String invoiceId,String invoiceToDate,String invoiceFromDate ) {
		logger.info("****** The conroller is inside the getAllViewExpenditures service in MarketingController *****");


		LinkedList expListObj = new LinkedList();

		logger.info("***** The input invoiceid is---->"+invoiceId);
		logger.info("***** The input invoiceFromDate is---->"+invoiceFromDate);
		logger.info("***** The input invoiceToDate is---->"+invoiceToDate);

		

	
		if(invoiceId != "") {

			String	sql = "SELECT EXPENDATURE_ID AS EXPENDATURE_ID FROM MRKT_EXPENDATURE WHERE INVOICE_ID = '"+invoiceId+"' and STATUS = 'A'  ";
			logger.info("******* The ExpendatureId based on the invoiceId *******"+sql);
			List<Map<String,Object>> expList = jdbcTemplate.queryForList(sql);
			logger.info("******* The ExpendatureId based on the invoiceId *******"+expList);


			for(Map<String,Object>map : expList ) {

				logger.info("*** expendatuireId in the map ***"+map);
				expListObj.add(map.get("EXPENDATURE_ID").toString());

			}
			logger.info("**** The expListObj is *****"+expListObj);



		}

		return expListObj;
	}
	@Override
	public int insertMarketExpenditure(MarketingDeptBean objMarketingExpenditure) {
		String query="insert into MRKT_EXPENDATURE (EXPENDATURE_ID,INVOICE_ID,INVOICE_AMOUNT,"
				+ "ENTRY_DATE,CREATED_BY,EXPENDATURE_MONTH,EXPENDATURE_TYPE,INVOICE_DATE,STATUS,INDENT_ENTRY_ID)  "
				+ "  values(?,?,?,sysdate,?,?,?,?,?,?)";
		return jdbcTemplate.update(query, new Object[] {
				
				objMarketingExpenditure.getExpenditureId(),
				objMarketingExpenditure.getInvoiceNumber(),
				objMarketingExpenditure.getInvoiceAmount(),
				objMarketingExpenditure.getCreatedBy(),
				objMarketingExpenditure.getExpenditureMonth(),
				objMarketingExpenditure.getExpenditureType(),
				DateUtil.convertToJavaDateFormat(objMarketingExpenditure.getInvoiceDate()),
				"A",objMarketingExpenditure.getIndentEntryId()
				
		});
	}

	@Override
	public int[] insertMarketExpenditureDtls(final List<MarketingDeptBean> objMarketingExpenditureDtlsList) {
		/*String query = "insert into MRKT_EXPENDATURE_DTLS (EXPENDATURE_ID,EXPENDATURE_DTLS_ID,"
				+ "CHILD_PRODUCT_ID,QTY,RATE,AMOUNT,HOARDING_ID,FROM_DATE,TO_DATE,TIME,SITE_ID) "
				+ "  values(?,?,?,?,?,?,?,?,?,?,?)";
		return jdbcTemplate.update(query, new Object[] {
				
				objMarketingExpenditureDtls.getExpenditureId(),
				objMarketingExpenditureDtls.getExpenditureDetailsId(),
				objMarketingExpenditureDtls.getChild_ProductId(),
				objMarketingExpenditureDtls.getQuantity(),
				objMarketingExpenditureDtls.getPrice(),
				objMarketingExpenditureDtls.getAmount(),
				objMarketingExpenditureDtls.getHoardingId(),
				StringUtils.isBlank(objMarketingExpenditureDtls.getFromDate())?null:DateUtil.convertToJavaDateFormat(objMarketingExpenditureDtls.getFromDate()),
				StringUtils.isBlank(objMarketingExpenditureDtls.getToDate())?null:DateUtil.convertToJavaDateFormat(objMarketingExpenditureDtls.getToDate()),
				objMarketingExpenditureDtls.getTime(),
				objMarketingExpenditureDtls.getSiteId()
		});*/
		/*****************************************************/
		// here using the batch update multiple records inserted into table with single time
		String query= "insert into MRKT_EXPENDATURE_DTLS (EXPENDATURE_ID,EXPENDATURE_DTLS_ID,"
				+ "CHILD_PRODUCT_ID,QTY,RATE,AMOUNT,HOARDING_ID,FROM_DATE,TO_DATE,SITE_ID,TIME,STATUS) "
				+ "  values(?,?,?,?,?,?,?,?,?,?,TO_DSINTERVAL('0 23:59:59'),'A')";
	        int[] updateCounts = jdbcTemplate.batchUpdate(
	                query,
	                new BatchPreparedStatementSetter() {
	                    public void setValues(PreparedStatement ps, int i) throws SQLException {
	                        ps.setInt(1,objMarketingExpenditureDtlsList.get(i).getExpenditureId());
	                        ps.setInt(2,objMarketingExpenditureDtlsList.get(i).getExpenditureDetailsId());
	                        ps.setString(3,objMarketingExpenditureDtlsList.get(i).getChild_ProductId());
	                        ps.setString(4,objMarketingExpenditureDtlsList.get(i).getQuantity());
	                        ps.setString(5,objMarketingExpenditureDtlsList.get(i).getPrice());
	                        ps.setString(6,objMarketingExpenditureDtlsList.get(i).getAmount());
	                        ps.setString(7,objMarketingExpenditureDtlsList.get(i).getHoardingId());
	                        ps.setDate(8, (java.sql.Date) (StringUtils.isBlank(objMarketingExpenditureDtlsList.get(i).getFromDate())?null:DateUtil.convertToSqlDateFormat(objMarketingExpenditureDtlsList.get(i).getFromDate())));
	                        ps.setDate(9, (java.sql.Date) (StringUtils.isBlank(objMarketingExpenditureDtlsList.get(i).getToDate())?null:DateUtil.convertToSqlDateFormat(objMarketingExpenditureDtlsList.get(i).getToDate())));
	                        ps.setInt(10,Integer.parseInt(objMarketingExpenditureDtlsList.get(i).getSiteId()));
	                        //ps.setString(11, objMarketingExpenditureDtlsList.get(i).getTime());
	                    }

	                    public int getBatchSize() {
	                        return objMarketingExpenditureDtlsList.size();
	                    }
	                } );
	        return updateCounts;
	}	
	/*===============================================view expenditure for invoice number and vendor data start=======================================*/
	// if the user enter the invoice id and vendorID and invoicedate then it will call this method only invoice id given then else condition executed
	public List<Map<String,Object>> getAllViewExpendituresWithVendorData(String invoiceId,String vendorId,String invoiceDate) {
		logger.info("***** The control is inside the getAllViewExpendituresWithData in MarketingDepartmentDaoImpl ******");
		String query = "";
		if(invoiceId!="" && vendorId!="" && invoiceDate!="" ){
	
		query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
		.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,IED.PRODUCT_NAME,") 
		.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
		.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
		.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID,IED.SUB_PRODUCT_NAME,VD.VENDOR_NAME,IED.MEASUR_MNT_NAME ")
		.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
		.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
		.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
		.append(" WHERE  MRKT_EXP.STATUS = 'A' ")
		.append(" AND MRKT_EXP.INVOICE_ID ='"+invoiceId+"' AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
		.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID AND IE.VENDOR_ID=VD.VENDOR_ID")
		.append(" AND IE.VENDOR_ID='"+vendorId+"' and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID AND IE.SITE_ID='996'").toString();
		}
		
		else if(invoiceId!="" && vendorId=="" && invoiceDate==""){
			 query = new StringBuilder("SELECT")
			.append(" DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID, ")
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,IED.PRODUCT_NAME, ")
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE, ")
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ")
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IED.SUB_PRODUCT_NAME,VD.VENDOR_NAME,IED.MEASUR_MNT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A'  ")
			.append(" AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID ")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID")
			.append(" AND IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID= MRKT_EXP.INVOICE_ID AND IE.SITE_ID='996'").toString();

		}
		logger.info("**** The getAllViewExpenditures query is *****"+query);
		List<Map<String,Object>> expendituresList = jdbcTemplate.queryForList(query);
		logger.info("**** The expendituresList object is ****"+expendituresList);
		return expendituresList;

	}
	
	/*===============================================view expenditure for invoice number and vendor data end=======================================*/

/*=====================================================marketing po Reject  start================================================================*/
	/**********************************************************for Temp Mail***************************************************************************/
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
		String query = "select VD.VENDOR_NAME,SPE.PO_DATE,SED.EMP_NAME,SED.EMP_EMAIL,SPE.TOTAL_AMOUNT,S.SITE_NAME,SPE.SITE_ID "
					+" FROM SUMADHURA_TEMP_PO_ENTRY SPE,VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SITE S "
					+" WHERE VD.VENDOR_ID=SPE.VENDOR_ID  AND SPE.PO_NUMBER=? AND EMP_ID=? and SPE.SITE_ID=S.SITE_ID";

		dbEmpDts = jdbcTemplate.queryForList(query, new Object[] { tempPoNumber, userId });
		for (Map<String, Object> prods : dbEmpDts) {
			empName = prods.get("EMP_NAME") == null ? "" : prods.get("EMP_NAME").toString();
			email = prods.get("EMP_EMAIL") == null ? "" : prods.get("EMP_EMAIL").toString();
			vendorName = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
			//siteWiseInhdentNo = prods.get("SITEWISE_INDENT_NO") == null ? "": prods.get("SITEWISE_INDENT_NO").toString();
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
			listOfDetails.add("-");
			listOfDetails.add(convertedPoDate);
			listOfDetails.add(total);
			listOfDetails.add(siteName);listOfDetails.add(siteId);
		}

		return listOfDetails;
	}
	/*==================================================rejected po purpose to for mail end===============================================*/
	public int updateTablesOnTempPORejection(String ponumber,String status) {

		String query1 = "update SUMADHURA_TEMP_PO_ENTRY set PO_STATUS =?,PASSWORD_MAIL=? where PO_NUMBER=?";
		int result1 = jdbcTemplate.update(query1, new Object[] { status,"0",ponumber });
		
		return result1;

	}
	
	// view expenditure data
	
	public List<Map<String,Object>> getAllViewExpendituresDates(String invoiceFromDate,String invoiceToDate,String siteIds,String invoicDate,String vendorId){
		logger.info("***** The control is inside the getAllViewExpenditures in   invoiceFromDate&invoiceToDate MarketingDepartmentDaoImpl ******");
		logger.info("***** The input invoiceFromDate & invoiceToDate values is ******"+invoiceFromDate+"-------->"+invoiceToDate);
		List<Map<String,Object>> expendituresList =null;
		// based on the user data it will check condition then it will executed with dates
		if(StringUtils.isBlank(siteIds) && invoicDate=="" && vendorId==""){
		StringBuilder query = new StringBuilder("SELECT")
			.append(" DISTINCT(MRKT_EXPD.INVOICE_ID)  AS INVOICE_ID,")
			.append(" MRKT_EXPD.INVOICE_AMOUNT AS INVOICE_AMOUNT,")
			.append(" MRKT_EXPD.INVOICE_DATE AS INVOICE_DATE,S.SITE_NAME AS SITENAME,VD.VENDOR_NAME")
			.append(" FROM MRKT_EXPENDATURE  MRKT_EXPD,MRKT_EXPENDATURE_DTLS MED,SITE S,INDENT_ENTRY IE,VENDOR_DETAILS VD")
			.append(" WHERE IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID AND MRKT_EXPD.EXPENDATURE_ID=MED.EXPENDATURE_ID AND IE.INDENT_ENTRY_ID=MRKT_EXPD.INDENT_ENTRY_ID AND ")
			.append(" TO_CHAR(IE.INVOICE_DATE,'DD-MM-YY')=TO_CHAR(MRKT_EXPD.INVOICE_DATE,'DD-MM-YY') and CAST(MRKT_EXPD.INVOICE_DATE AS DATE)");
			
		if(StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate) && invoicDate=="" && vendorId==""){
			query.append("BETWEEN  TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceFromDate)+"','DD-MM-YY')")
			.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceToDate)+"','DD-MM-YY')")
			.append(" AND MRKT_EXPD.STATUS = 'A'  AND IE.SITE_ID=S.SITE_ID AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID and IE.SITE_ID='996'");
		}
		else if(StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isBlank(invoiceToDate)){
			query.append(" = TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceFromDate)+"','DD-MM-YY')")
			.append(" AND MRKT_EXPD.STATUS = 'A' AND IE.SITE_ID=S.SITE_ID AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID and IE.SITE_ID='996'");

		}
		else if(StringUtils.isBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)){

			query.append(" <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceToDate)+"','DD-MM-YY')")
			.append(" AND MRKT_EXPD.STATUS = 'A' AND IE.SITE_ID=S.SITE_ID AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID and IE.SITE_ID='996'");

		}
			logger.info("**** The getAllViewExpenditures  invoiceFromDate&invoiceToDate query is *****"+query);
			expendituresList = jdbcTemplate.queryForList(query.toString());
		}	// user selected siteIds only then it will executed this condition
		else if(StringUtils.isNotBlank(siteIds) && invoicDate=="" && vendorId==""){
			
			StringBuilder sql = new StringBuilder("SELECT")
			.append(" DISTINCT(IE.INVOICE_ID)  AS INVOICE_ID,")
			.append(" MED.AMOUNT AS INVOICE_AMOUNT, ")
			.append(" MRKT_EXPD.INVOICE_DATE AS INVOICE_DATE,S.SITE_NAME AS SITENAME,VD.VENDOR_NAME")
			.append(" FROM MRKT_EXPENDATURE  MRKT_EXPD,MRKT_EXPENDATURE_DTLS MED,SITE S,VENDOR_DETAILS VD,INDENT_ENTRY IE ")
			.append(" WHERE IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID AND MRKT_EXPD.EXPENDATURE_ID=MED.EXPENDATURE_ID AND IE.INDENT_ENTRY_ID=MRKT_EXPD.INDENT_ENTRY_ID AND   ")
			.append(" TO_CHAR(IE.INVOICE_DATE,'DD-MM-YY')=TO_CHAR(MRKT_EXPD.INVOICE_DATE,'DD-MM-YY') and CAST(MRKT_EXPD.INVOICE_DATE AS DATE)");
			
			if(StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)){
				sql.append("BETWEEN  TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceFromDate)+"','DD-MM-YY')")
				.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceToDate)+"','DD-MM-YY')")
				.append(" AND MRKT_EXPD.STATUS = 'A' AND MED.SITE_ID in('"+siteIds+"')  AND MED.SITE_ID=S.SITE_ID");
			}
			else if(StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isBlank(invoiceToDate)){
				sql.append(" = TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceFromDate)+"','DD-MM-YY')")
				.append(" AND MRKT_EXPD.STATUS = 'A' AND MED.SITE_ID in('"+siteIds+"')  AND MED.SITE_ID=S.SITE_ID");
			}
			else if(StringUtils.isBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)){
				sql.append(" <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceToDate)+"','DD-MM-YY')")
				.append(" AND MRKT_EXPD.STATUS = 'A' AND MED.SITE_ID in('"+siteIds+"')  AND MED.SITE_ID=S.SITE_ID");
			}

			expendituresList = jdbcTemplate.queryForList(sql.toString());
			}
		//only vendorId given then it will check the condition and executed   
		else if(siteIds=="" && invoicDate=="" && vendorId!=""){
				
				StringBuilder query = new StringBuilder("SELECT")
				.append(" DISTINCT(MRKT_EXPD.INVOICE_ID)  AS INVOICE_ID,")
				.append(" MRKT_EXPD.INVOICE_AMOUNT AS INVOICE_AMOUNT,")
				.append(" MRKT_EXPD.INVOICE_DATE AS INVOICE_DATE,S.SITE_NAME AS SITENAME,VD.VENDOR_NAME")
				.append(" FROM MRKT_EXPENDATURE  MRKT_EXPD,MRKT_EXPENDATURE_DTLS MED,INDENT_ENTRY IE,SITE S,VENDOR_DETAILS VD")
				.append(" WHERE IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID AND MRKT_EXPD.EXPENDATURE_ID=MED.EXPENDATURE_ID AND IE.INDENT_ENTRY_ID=MRKT_EXPD.INDENT_ENTRY_ID AND ")
				.append(" TO_CHAR(IE.INVOICE_DATE,'DD-MM-YY')=TO_CHAR(MRKT_EXPD.INVOICE_DATE,'DD-MM-YY') and CAST(MRKT_EXPD.INVOICE_DATE AS DATE)");
				
			if(StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)){
				query.append("BETWEEN  TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceFromDate)+"','DD-MM-YY')")
				.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceToDate)+"','DD-MM-YY')")
				.append(" AND MRKT_EXPD.STATUS = 'A' AND IE.VENDOR_ID='"+vendorId+"' AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID AND IE.SITE_ID=S.SITE_ID");
			}
			else if(StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isBlank(invoiceToDate)){
				query.append(" = TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceFromDate)+"','DD-MM-YY')")
				.append(" AND MRKT_EXPD.STATUS = 'A' AND IE.VENDOR_ID='"+vendorId+"' AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID AND IE.SITE_ID=S.SITE_ID");

			}
			else if(StringUtils.isBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)){

				query.append(" <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceToDate)+"','DD-MM-YY')")
				.append(" AND MRKT_EXPD.STATUS = 'A' AND IE.VENDOR_ID='"+vendorId+"' AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID AND IE.SITE_ID=S.SITE_ID");

			}
				logger.info("**** The getAllViewExpenditures  invoiceFromDate&invoiceToDate query is *****"+query);
				expendituresList = jdbcTemplate.queryForList(query.toString());	
				
			}
		// only siteIds and vendorIds given then it will check and executed 
		else if(siteIds!="" && invoicDate=="" && vendorId!=""){
				StringBuilder sql = new StringBuilder("SELECT")
				.append(" DISTINCT(MRKT_EXPD.INVOICE_ID)  AS INVOICE_ID,")
				.append(" MED.AMOUNT AS INVOICE_AMOUNT,MED.RATE,")
				.append(" MRKT_EXPD.INVOICE_DATE AS INVOICE_DATE,S.SITE_NAME AS SITENAME,VD.VENDOR_NAME")
				.append(" FROM MRKT_EXPENDATURE  MRKT_EXPD,MRKT_EXPENDATURE_DTLS MED,INDENT_ENTRY IE,SITE S,VENDOR_DETAILS VD  ")
				.append(" WHERE IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID AND IE.INDENT_ENTRY_ID=MRKT_EXPD.INDENT_ENTRY_ID AND   ")
				.append(" TO_CHAR(IE.INVOICE_DATE,'DD-MM-YY')=TO_CHAR(MRKT_EXPD.INVOICE_DATE,'DD-MM-YY') and CAST(MRKT_EXPD.INVOICE_DATE AS DATE)");
				
				
				if(StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)){
					sql.append("BETWEEN  TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceFromDate)+"','DD-MM-YY')")
					.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceToDate)+"','DD-MM-YY')")
					.append(" AND MRKT_EXPD.STATUS = 'A' AND MED.SITE_ID in('"+siteIds+"') AND MRKT_EXPD.EXPENDATURE_ID=MED.EXPENDATURE_ID AND MED.SITE_ID=S.SITE_ID")
					.append(" AND IE.VENDOR_ID='"+vendorId+"' AND MRKT_EXPD.INVOICE_ID=IE.INVOICE_ID ");
				}
				else if(StringUtils.isNotBlank(invoiceFromDate) && StringUtils.isBlank(invoiceToDate)){
					sql.append(" = TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceFromDate)+"','DD-MM-YY')")
					.append(" AND MRKT_EXPD.STATUS = 'A' AND MED.SITE_ID in('"+siteIds+"') AND MRKT_EXPD.EXPENDATURE_ID=MED.EXPENDATURE_ID AND MED.SITE_ID=S.SITE_ID")
					.append(" AND IE.VENDOR_ID='"+vendorId+"' AND MRKT_EXPD.INVOICE_ID=IE.INVOICE_ID ");
				}
				else if(StringUtils.isBlank(invoiceFromDate) && StringUtils.isNotBlank(invoiceToDate)){
					sql.append(" <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(invoiceToDate)+"','DD-MM-YY')")
					.append(" AND MRKT_EXPD.STATUS = 'A' AND MED.SITE_ID in('"+siteIds+"') AND MRKT_EXPD.EXPENDATURE_ID=MED.EXPENDATURE_ID AND MED.SITE_ID=S.SITE_ID")
					.append(" AND IE.VENDOR_ID='"+vendorId+"' AND MRKT_EXPD.INVOICE_ID=IE.INVOICE_ID ");
				}

				expendituresList = jdbcTemplate.queryForList(sql.toString());
			}
					
		
		logger.info("**** The expendituresList object is ****"+expendituresList);
		return expendituresList;
	}

	// this is for invoice id forn view expenditure
	/*===============================================view expenditure for invoice number and vendor data start=======================================*/
	public List<Map<String,Object>> getAllViewExpendituresWithInvoiceData(String invoiceId,String vendorId,String invoiceDate,String siteIds,
			String productId,String subProductId,String childProductId,String fromDate,String toDate) {
		logger.info("***** The control is inside the getAllViewExpendituresWithData in MarketingDepartmentDaoImpl ******");
		//String query = "";
		List<Map<String,Object>> expendituresList =null;
		// based on the user given data it will executed 
		// invoice Id and vendorId and invoiceDate not empty then below condition executed with site Id or without siteId
		
		if(invoiceId!=null && vendorId!=null && invoiceDate!=null && invoiceId!="" && vendorId!="" && invoiceDate!="" && StringUtils.isBlank(productId)&& StringUtils.isBlank(subProductId) && StringUtils.isBlank(childProductId)){
		
		StringBuilder query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
		.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,") 
		.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
		.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
		.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID ")
		.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
		.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
		.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
		.append(" WHERE  MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A' ")
		.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID ");
		if(siteIds==null || siteIds==""){
			query.append(" AND MRKT_EXP.INVOICE_ID ='"+invoiceId+"' AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID AND IE.SITE_ID='996' ") 
			.append(" AND IE.VENDOR_ID='"+vendorId+"' and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ");
		}else{
			query.append(" AND MRKT_EXP.INVOICE_ID ='"+invoiceId+"' AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
			.append(" AND IE.VENDOR_ID='"+vendorId+"' and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ")
			.append(" AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
		}
		expendituresList = jdbcTemplate.queryForList(query.toString());
		
		}
		// invoice id not empty or not null remain null or empty then below condition executed
		else if((invoiceId!=null && vendorId==null && invoiceDate==null) || (invoiceId!="" && vendorId=="" && invoiceDate=="") && StringUtils.isBlank(productId)&& StringUtils.isBlank(subProductId) && StringUtils.isBlank(childProductId)){
			StringBuilder sql = new StringBuilder("SELECT")
			.append(" DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID, ")
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID, ")
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE, ")
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ")
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IED.SUB_PRODUCT_NAME,VD.VENDOR_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A'  AND IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID= MRKT_EXP.INVOICE_ID AND MRKT_EXP_DTLS.STATUS='A'")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID");
			if(siteIds==null || siteIds==""){
			
				sql.append(" AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID AND IE.SITE_ID='996' ");
			}else{
				sql.append(" AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID  AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			}
			
			expendituresList = jdbcTemplate.queryForList(sql.toString());

		}
		// if user select siteId and invoiceid not null or not empty then below condition executed
		else if((siteIds!=null && invoiceId==null && invoiceDate==null && vendorId==null) || (siteIds!="" && invoiceId=="" && invoiceDate=="" && vendorId=="") && StringUtils.isBlank(productId)&& StringUtils.isBlank(subProductId) && StringUtils.isBlank(childProductId)){
			StringBuilder sql = new StringBuilder("SELECT")
			.append(" DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID, ")
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID, ")
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE, ")
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ")
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IED.SUB_PRODUCT_NAME,VD.VENDOR_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A'  AND IE.VENDOR_ID=VD.VENDOR_ID AND IE.INVOICE_ID= MRKT_EXP.INVOICE_ID AND MRKT_EXP_DTLS.STATUS='A' ")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID")
			.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID  AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			
			expendituresList = jdbcTemplate.queryForList(sql.toString());

		}
		// only invoice id not null or not empty then below condition executed
		else if((invoiceDate!=null && invoiceId==null && vendorId==null) || (invoiceDate!="" && invoiceId=="" && vendorId=="") && StringUtils.isBlank(productId)&& StringUtils.isBlank(subProductId) && StringUtils.isBlank(childProductId)){
			
			StringBuilder query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,") 
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID,IED.SUB_PRODUCT_NAME,VD.VENDOR_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A' ")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID ");
			if(siteIds==null || siteIds==""){
				query.append("  AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append("  and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID AND IE.SITE_ID='996'");
			}else{
				query.append("  AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append("  and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ")
				.append(" AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			}
			expendituresList = jdbcTemplate.queryForList(query.toString());
			
		}
		//vendot id not null remain empty then below condion excuted
		else if((vendorId!=null && invoiceId==null && invoiceDate==null) ||(vendorId!="" && invoiceId=="" && invoiceDate=="") && StringUtils.isBlank(productId)&& StringUtils.isBlank(subProductId) && StringUtils.isBlank(childProductId)){
			StringBuilder query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,") 
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID,VD.VENDOR_NAME,IED.SUB_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A' ")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID ");
			if(siteIds==null || siteIds==""){
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND IE.VENDOR_ID='"+vendorId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID AND IE.SITE_ID='996'");
			}else{
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND IE.VENDOR_ID='"+vendorId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ")
				.append(" AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			}
			expendituresList = jdbcTemplate.queryForList(query.toString());
		}
		//vendorId and invoiceId not empty then below condition executed 
		else if((vendorId!=null && invoiceId!=null && invoiceDate==null) || (vendorId!="" && invoiceId!="" && invoiceDate=="") && StringUtils.isBlank(productId)&& StringUtils.isBlank(subProductId) && StringUtils.isBlank(childProductId)){
			StringBuilder query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,") 
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID,VD.VENDOR_NAME,IED.SUB_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A'")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID ");
			if(siteIds==null || siteIds==""){
				query.append(" AND MRKT_EXP.INVOICE_ID ='"+invoiceId+"' AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND IE.VENDOR_ID='"+vendorId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID AND IE.SITE_ID='996'");
			}else{
				query.append(" AND MRKT_EXP.INVOICE_ID ='"+invoiceId+"' AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND IE.VENDOR_ID='"+vendorId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ")
				.append(" AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			}
			expendituresList = jdbcTemplate.queryForList(query.toString());
		}
		// invoice id and invoice date tehn remain empty below condition executed
		else if((vendorId==null && invoiceId!=null && invoiceDate!=null) || (vendorId=="" && invoiceId!="" && invoiceDate!="") && StringUtils.isBlank(productId)&& StringUtils.isBlank(subProductId) && StringUtils.isBlank(childProductId)){
			
			StringBuilder query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,") 
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID,VD.VENDOR_NAME,IED.SUB_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A'")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID ");
			if(siteIds==null || siteIds==""){
				query.append(" AND MRKT_EXP.INVOICE_ID ='"+invoiceId+"' AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID AND IE.SITE_ID='996'");
			}else{
				query.append(" AND MRKT_EXP.INVOICE_ID ='"+invoiceId+"' AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ")
				.append(" AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			}
			expendituresList = jdbcTemplate.queryForList(query.toString());
			
		}
		// vendorID and invoice date given then below condition executed
		else if((vendorId!=null && invoiceId==null && invoiceDate!=null) || (vendorId!="" && invoiceId=="" && invoiceDate!="") && StringUtils.isBlank(productId)&& StringUtils.isBlank(subProductId) && StringUtils.isBlank(childProductId)){
			
			StringBuilder query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,") 
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID,VD.VENDOR_NAME,IED.SUB_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A' ")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID ");
			if(siteIds==null || siteIds==""){
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND IE.VENDOR_ID='"+vendorId+"' and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ");
			}else{
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND IE.VENDOR_ID='"+vendorId+"' and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ")
				.append(" AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			}
			expendituresList = jdbcTemplate.queryForList(query.toString());
			
		}
		else if((StringUtils.isNotBlank(productId)&& StringUtils.isNotBlank(subProductId) && StringUtils.isNotBlank(childProductId))){
			StringBuilder query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,") 
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID,VD.VENDOR_NAME,IED.SUB_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A'")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID AND IE.INDENT_ENTRY_ID=MRKT_EXP.INDENT_ENTRY_ID");
			if(siteIds==null || siteIds==""){
				// if the user enter from or to date then it will execute without site and with site
				 if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
						query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) BETWEEN  TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')")
						.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
					}
					else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate)){
						query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) = TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')");
					}
					else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)){

						query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
					}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
						query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
					}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
						query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"'");
					}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
						query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"')");
					}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
						query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND IE.VENDOR_ID='"+vendorId+"'");
					}else if(StringUtils.isBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
						query.append("AND MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
					}
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID AND IE.SITE_ID='996'") 
				.append(" AND IED.PRODUCT_ID='"+productId+"' AND IED.SUB_PRODUCT_ID='"+subProductId+"' AND IED.CHILD_PRODUCT_ID='"+childProductId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ");
			}else{
				if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) BETWEEN  TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')")
					.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
				}
				else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate)){
					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) = TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')");
				}
				else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)){

					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"'");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"')");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND IE.VENDOR_ID='"+vendorId+"'");
				}else if(StringUtils.isBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
				}
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND IED.PRODUCT_ID='"+productId+"' AND IED.SUB_PRODUCT_ID='"+subProductId+"' AND IED.CHILD_PRODUCT_ID='"+childProductId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ")
				.append(" AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			}
			expendituresList = jdbcTemplate.queryForList(query.toString());
			
		}else if((StringUtils.isNotBlank(productId)&& StringUtils.isNotBlank(subProductId) && StringUtils.isBlank(childProductId))){
			StringBuilder query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,") 
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID,VD.VENDOR_NAME,IED.SUB_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A' ")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID AND IE.INDENT_ENTRY_ID=MRKT_EXP.INDENT_ENTRY_ID ");
			if(siteIds==null || siteIds==""){
				if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) BETWEEN  TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')")
					.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
				}
				else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate)){
					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) = TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')");
				}
				else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)){

					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"'");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"')");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND IE.VENDOR_ID='"+vendorId+"'");
				}else if(StringUtils.isBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
				}
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID AND IE.SITE_ID='996' ") 
				.append(" AND IED.PRODUCT_ID='"+productId+"' AND IED.SUB_PRODUCT_ID='"+subProductId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ");
			}else{
				if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) BETWEEN  TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')")
					.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
				}
				else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate)){
					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) = TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')");
				}
				else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)){

					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"'");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"')");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND IE.VENDOR_ID='"+vendorId+"'");
				}else if(StringUtils.isBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
				}
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND IED.PRODUCT_ID='"+productId+"' AND IED.SUB_PRODUCT_ID='"+subProductId+"'  AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ")
				.append(" AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			}
			expendituresList = jdbcTemplate.queryForList(query.toString());
			
		}else if((StringUtils.isNotBlank(productId)&& StringUtils.isBlank(subProductId) && StringUtils.isBlank(childProductId))){
			StringBuilder query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,") 
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID,VD.VENDOR_NAME,IED.SUB_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A' ")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID AND IE.INDENT_ENTRY_ID=MRKT_EXP.INDENT_ENTRY_ID ");
			if(siteIds==null || siteIds==""){
				if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) BETWEEN  TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')")
					.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
				}
				else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate)){
					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) = TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')");
				}
				else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)){

					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
				}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)){
					
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"'");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"')");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND IE.VENDOR_ID='"+vendorId+"'");
				}else if(StringUtils.isBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
				}
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND IED.PRODUCT_ID='"+productId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID AND IE.SITE_ID='996' ");
			}else{
				if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) BETWEEN  TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')")
					.append(" AND TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
				}
				else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate)){
					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) = TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(fromDate)+"','DD-MM-YY')");
				}
				else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)){

					query.append("AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) <= TO_DATE('"+DateUtil.DD_MMM_YYToDD_MM_YY(toDate)+"','DD-MM-YY')");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') and  MRKT_EXP.INVOICE_DATE='"+invoiceDate+"'");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"')");
				}else if(StringUtils.isNotBlank(invoiceId)&& StringUtils.isBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND IE.VENDOR_ID='"+vendorId+"'");
				}else if(StringUtils.isBlank(invoiceId)&& StringUtils.isNotBlank(invoiceDate) && StringUtils.isNotBlank(vendorId)){
					query.append("AND MRKT_EXP.INVOICE_DATE='"+invoiceDate+"' AND IE.VENDOR_ID='"+vendorId+"'");
				}
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND IED.PRODUCT_ID='"+productId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ")
				.append(" AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			}
			expendituresList = jdbcTemplate.queryForList(query.toString());
			
		}
		/*else if((StringUtils.isNotBlank(productId)&& StringUtils.isNotBlank(subProductId) && StringUtils.isNotBlank(childProductId)) && StringUtils.isBlank(productId)&& StringUtils.isNotBlank(subProductId) && StringUtils.isNotBlank(childProductId)){
			StringBuilder query = new StringBuilder("SELECT DISTINCT(CHD_PRD.NAME) AS CHILD_PRODUCT_NAME, SITE.SITE_NAME AS SITE_NAME, MRKT_EXP_DTLS.SITE_ID AS SITE_ID,") 
			.append(" MHD.HOARDING_AREA  AS LOCATION, MHD.HOARDING_ID  AS LOCATION_ID,") 
			.append(" MRKT_EXP_DTLS.CHILD_PRODUCT_ID  AS CHILD_PRODUCT_ID, MRKT_EXP_DTLS.QTY AS QUANTITY, MRKT_EXP.INVOICE_DATE AS INVOICE_DATE," )
			.append(" MRKT_EXP_DTLS.FROM_DATE AS FROM_DATE, MRKT_EXP_DTLS.TO_DATE AS TO_DATE, MRKT_EXP_DTLS.TIME AS TIME, ") 
			.append(" MRKT_EXP_DTLS.AMOUNT AS AMOUNT,IE.VENDOR_ID,VD.VENDOR_NAME,IED.SUB_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.PRODUCT_NAME ")
			.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
			.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
			.append(" left outer join INDENT_ENTRY_DETAILS IED ON IED.CHILD_PRODUCT_ID=MRKT_EXP_DTLS.CHILD_PRODUCT_ID")
			.append(" WHERE  MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID")
			.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND  MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID ");
			if(siteIds==null || siteIds==""){
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND IED.PRODUCT_ID='"+productId+"' AND IED.SUB_PRODUCT_ID='"+subProductId+"' AND IED.CHILD_PRODUCT_ID='"+childProductId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ");
			}else{
				query.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID") 
				.append(" AND MRKT_EXP.INVOICE_ID = ('"+invoiceId+"') AND IED.PRODUCT_ID='"+productId+"' AND IED.SUB_PRODUCT_ID='"+subProductId+"' AND IED.CHILD_PRODUCT_ID='"+childProductId+"' AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID ")
				.append(" AND MRKT_EXP_DTLS.SITE_ID IN ('"+siteIds+"')");
			}
			expendituresList = jdbcTemplate.queryForList(query.toString());
		}*/
			
		logger.info("**** The expendituresList object is ****"+expendituresList);
		return expendituresList;

	}
	/*==============================================update expenditure for show data start=====================================================*/
// view update Expenditure data
	
	public List<Map<String,Object>> getAllViewExpendituresForVendorName(String invoiceId,String vendorId,String invoiceDate){
		logger.info("***** The control is inside the getAllViewExpenditures in   invoiceFromDate&invoiceToDate MarketingDepartmentDaoImpl ******");
		//logger.info("***** The input invoiceFromDate & invoiceToDate values is ******"+invoiceFromDate+"-------->"+invoiceToDate);
		List<Map<String,Object>> expendituresList =null;
		String query = "";
		if(StringUtils.isBlank(vendorId) && invoiceDate!=""){
		 query = new StringBuilder("SELECT")
			.append(" MRKT_EXPD.INVOICE_ID  AS INVOICE_ID,")
			.append(" MRKT_EXPD.INVOICE_AMOUNT AS INVOICE_AMOUNT,")
			.append(" MRKT_EXPD.INVOICE_DATE AS INVOICE_DATE,S.SITE_NAME AS SITENAME")
			.append(" FROM MRKT_EXPENDATURE  MRKT_EXPD,SITE S,INDENT_ENTRY IE")
			.append(" WHERE MRKT_EXPD.INVOICE_DATE  = TO_DATE('"+invoiceDate+"','DD-MM-YY') ")
			.append("  AND MRKT_EXPD.STATUS = 'A'  AND IE.SITE_ID=S.SITE_ID AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID").toString();
		}
		
		//List<Map<String,Object>> expendituresList = jdbcTemplate.queryForList(query);
		
		if(StringUtils.isNotBlank(vendorId) && invoiceDate==""){
			
			 query = new StringBuilder("SELECT")
				.append(" MRKT_EXPD.INVOICE_ID  AS INVOICE_ID,")
				.append(" MRKT_EXPD.INVOICE_AMOUNT AS INVOICE_AMOUNT,")
				.append(" MRKT_EXPD.INVOICE_DATE AS INVOICE_DATE,S.SITE_NAME AS SITENAME")
				.append(" FROM MRKT_EXPENDATURE  MRKT_EXPD,SITE S,INDENT_ENTRY IE")
				.append(" WHERE IE.VENDOR_ID='"+vendorId+"' ")
				.append("  AND MRKT_EXPD.STATUS = 'A'  AND IE.SITE_ID=S.SITE_ID AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID").toString();
			}

			expendituresList = jdbcTemplate.queryForList(query);
			
			if(StringUtils.isNotBlank(vendorId) && StringUtils.isNotBlank(invoiceDate)){
				
				query = new StringBuilder("SELECT")
				.append(" MRKT_EXPD.INVOICE_ID  AS INVOICE_ID,")
				.append(" MRKT_EXPD.INVOICE_AMOUNT AS INVOICE_AMOUNT,")
				.append(" MRKT_EXPD.INVOICE_DATE AS INVOICE_DATE,S.SITE_NAME AS SITENAME")
				.append(" FROM MRKT_EXPENDATURE  MRKT_EXPD,SITE S,INDENT_ENTRY IE")
				.append(" WHERE IE.VENDOR_ID='"+vendorId+"' MRKT_EXPD.INVOICE_DATE  = TO_DATE('"+invoiceDate+"','DD-MM-YY') ")
				.append("  AND MRKT_EXPD.STATUS = 'A'  AND IE.SITE_ID=S.SITE_ID AND IE.INVOICE_ID=MRKT_EXPD.INVOICE_ID").toString();
			}

				expendituresList = jdbcTemplate.queryForList(query);
			
					
		
		//logger.info("**** The expendituresList object is ****"+expendituresList);
		return expendituresList;
	}
	/********************************************************Marketing po details for emails getting from temp tables start****************************/
	
	public List<String> getEmployeesEmailsForMD(String tempPONumber) {
		
		
		List<String> emailList = new ArrayList<String>();
		
		List<Map<String, Object>> dbIndentDts = null;
		
			
			String query = "select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in ( "
						+" select distinct(PO_CREATE_APPROVE_EMP_ID) from SUMADHURA_PO_CRT_APPRL_DTLS where "
						+" TEMP_PO_NUMBER =?)";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{tempPONumber});
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
			
		
		return emailList;
	}
	
	// this is for receiving address for as per client requirement
	
	public List<String> getProjectAddGstin(String receiverState) {
		List<String> projectAddressDetails = new ArrayList<String>();
		String projectAddress = "";
		//String strBillingAddressGSTIN = "";
		String strProjectCompanyName = "";
		if (receiverState.equalsIgnoreCase("Telangana")) {

			projectAddress = validateParams.getProperty("PROJECT_ADDRESSS_HYDERABAD");
			//strBillingAddressGSTIN = validateParams.getProperty("GSTIN_HYDERABAD");
			strProjectCompanyName = validateParams.getProperty("PROJECT_NAME_HYD");
		} else {

			projectAddress = validateParams.getProperty("PROJECT_ADDRESSS_BENGALORE");
			//strBillingAddressGSTIN = validateParams.getProperty("GSTIN_BENGALORE");
			strProjectCompanyName = validateParams.getProperty("PROJECT_NAME_BNG");

		}
		projectAddressDetails.add(projectAddress);
		//billingAddressDetails.add(strBillingAddressGSTIN);
		projectAddressDetails.add(strProjectCompanyName);

		return projectAddressDetails;

	}
	
	
	
	/***************************************************************
	 * mailFor tempPo start
	 *************************************************************************/

	/*public int getDataForMailForMarketingTempPo(String poNumber, String siteId) {

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
				
			}
			result = updateTablesOnTempPORejection(indentNumber, poNumber, "");
		}

		return result;
	}*/
	// here the month came like only ex: 02-2019 so it will retrive the data
	public Map<String,String> getLocationBrandingData(List<String> list){
		//List<MarketingDeptBean> marketingBeanList = new ArrayList<MarketingDeptBean>();
		Map<String,String> addr=new HashMap<String,String>();
		try{
			String siteIds="";
			String address="";
			String siteId="";
			//String state=validateParams.getProperty("STATE_HYD") == null ? "" : validateParams.getProperty("STATE_HYD").toString(); 
			
			for(int i=0;i<list.size();i++){
				siteIds+=list.get(i)+",";
			}
			siteIds=siteIds.substring(0,siteIds.length()-1);
			siteIds=siteIds.replace(",","','");
			List<Map<String, Object>> dbIndentDts = null;
			//String query = "select distinct(INDENT_PROCESS_ID) AS INDENT_PROCESS_ID ,PROCESS_INTIATED_SITE,CREATION_DATE from SUMADHURA_CNTL_INDENT_REQ_DTLS where  STATUS = 'A' and INDENT_PROCESS_ID= ? ";

			String query = "SELECT SITE_ID,ADDRESS FROM SITE WHERE SITE_ID in('"+siteIds+"')";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {});

			for(Map<String, Object> prods : dbIndentDts) {
				//MarketingDeptBean objMarketingBean = new MarketingDeptBean();
				address=prods.get("ADDRESS")==null ? "0" : prods.get("ADDRESS").toString();
				siteId=prods.get("SITE_ID")==null ? "0" : prods.get("SITE_ID").toString();
				addr.put(siteId,address);
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return addr;
	}
	
	// getting total available area in that respective month
	public double getTotalAvailableAreaInMonthForHyd(String strMonth,boolean isHyd){
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbDts = null;
		String state=validateParams.getProperty("STATE_HYD") == null ? "" : validateParams.getProperty("STATE_HYD").toString();
		double doubleTotalAvailArea = 0.0;
		String site_Ids="";
		String siteIds="";
		try{
			if(isHyd){
			String sql="select SITE_ID from SITE where STATUS='ACTIVE' AND ADDRESS=?";
			dbDts=jdbcTemplate.queryForList(sql, new Object[] {state});
			
			for(Map<String, Object> prod : dbDts) {

				site_Ids =(prod.get("SITE_ID")==null ? "0" : prod.get("SITE_ID").toString());
				siteIds+=site_Ids+",";
			}
			siteIds=siteIds.substring(0,siteIds.length()-1);
			siteIds=siteIds.replace(",","','");
			}else{
				String sql="select SITE_ID from SITE where STATUS='ACTIVE' AND ADDRESS!='"+state+"'";
				dbDts=jdbcTemplate.queryForList(sql, new Object[] {});
				
				for(Map<String, Object> prod : dbDts) {

					site_Ids =(prod.get("SITE_ID")==null ? "0" : prod.get("SITE_ID").toString());
					siteIds+=site_Ids+",";
				}
				siteIds=siteIds.substring(0,siteIds.length()-1);
				siteIds=siteIds.replace(",","','");
			}
			String query = "select sum(AVAILABLE_AREA) as TOTAL_AVAL_AREA from MRKT_MONTHLY_AVAIL_AREA where MONTH_YEAR = ? and STATUS = 'A' and SITE_ID in('"+siteIds+"')";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {strMonth});

			for(Map<String, Object> prods : dbIndentDts) {

				doubleTotalAvailArea =  Double.valueOf(prods.get("TOTAL_AVAL_AREA")==null ? "0" : prods.get("TOTAL_AVAL_AREA").toString());

			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return doubleTotalAvailArea;
	}
	
	// location 
	public int insertOrUpdateExpendaturedtlsTableLocation( int intExpendatureId,String strChildProductId,double doubleQty,double doubleRate,double doubleAmount,String strSiteId,String user_id){
		String locationSites=validateParams.getProperty("HYD_SITES_LOCATION") == null ? "" : validateParams.getProperty("HYD_SITES_LOCATION").toString();
		int intCount = 0 ;
		List<Map<String, Object>> dbIndentDts = null;
		List<String> list=new ArrayList<String>();
		List<String> modifiedDetails=new ArrayList<String>();
		String siteIds="";
		String site_Ids="";
		boolean hydType=false;
		boolean bngType=false;
		String oldAmount="";
		String strTotalModified="";
		try{
			
			
			String sql="select SITE_ID from MRKT_EXPENDATURE_DTLS where EXPENDATURE_ID = ? AND STATUS='A'";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {intExpendatureId});
			for(Map<String, Object> prods : dbIndentDts) {

				siteIds=(prods.get("SITE_ID")==null ? "0" : prods.get("SITE_ID").toString());
				if(locationSites.contains(siteIds)){
					hydType=true;
					break;
				}else{
					bngType=true;
					break;
				}
				//site_Ids+=siteIds+",";

			}
			
			String query = "select count(1) from MRKT_EXPENDATURE_DTLS where EXPENDATURE_ID = ? and SITE_ID = ? and CHILD_PRODUCT_ID=? AND STATUS='A'";

			intCount = jdbcTemplate.queryForInt(query, new Object[] {intExpendatureId,strSiteId,strChildProductId});
			
			if(intCount > 0){
			
			//site_Ids=site_Ids.substring(0,site_Ids.length()-1);
			//site_Ids=site_Ids.replace(",","','");
			}
			
		///	if(locationSites.contains(strSiteId) && hydType){
			if(intCount > 0){
				
				String sql1="SELECT AMOUNT FROM MRKT_EXPENDATURE_DTLS WHERE EXPENDATURE_ID ='"+intExpendatureId+"' and SITE_ID ='"+strSiteId+"' and CHILD_PRODUCT_ID='"+strChildProductId+"' and STATUS='A'";
				oldAmount = jdbcTemplate.queryForObject(sql1, String.class);
				strTotalModified="user:"+user_id+",old:"+oldAmount+",new:"+String.valueOf(doubleAmount)+"@@";
				//oldAmount+=","+user_id+","+doubleAmount+"@@";
				
				query = "update  MRKT_EXPENDATURE_DTLS set AMOUNT = ? ,RATE  = ?,MODIFIED_DETAILS=? where   EXPENDATURE_ID = ? and SITE_ID = ? and CHILD_PRODUCT_ID=? and STATUS='A'";

				intCount = jdbcTemplate.update(query, new Object[] {doubleAmount,doubleRate,strTotalModified,intExpendatureId,strSiteId,strChildProductId});

			}else{
				if(locationSites.contains(strSiteId) && hydType){
				query = "insert into  MRKT_EXPENDATURE_DTLS(EXPENDATURE_ID,EXPENDATURE_DTLS_ID,CHILD_PRODUCT_ID,QTY,RATE,AMOUNT,SITE_ID,STATUS)" +
				" values(?,MRKT_EXPENDATURE_DTLS_SEQ.nextval,?,?,?,?,?,'A')";
				
				intCount = jdbcTemplate.update(query, new Object[] {intExpendatureId,strChildProductId,doubleQty,doubleRate,doubleAmount,strSiteId});
			
				}else if(bngType && !locationSites.contains(strSiteId)){
					query = "insert into  MRKT_EXPENDATURE_DTLS(EXPENDATURE_ID,EXPENDATURE_DTLS_ID,CHILD_PRODUCT_ID,QTY,RATE,AMOUNT,SITE_ID,STATUS)" +
					" values(?,MRKT_EXPENDATURE_DTLS_SEQ.nextval,?,?,?,?,?,'A')";
					
				intCount = jdbcTemplate.update(query, new Object[] {intExpendatureId,strChildProductId,doubleQty,doubleRate,doubleAmount,strSiteId});
				}
				
				

			}
			/*else{
				if(intCount > 0){
					query = "update  MRKT_EXPENDATURE_DTLS set AMOUNT = ? ,RATE  = ? where   EXPENDATURE_ID = ? and SITE_ID = ?";

					intCount = jdbcTemplate.update(query, new Object[] {doubleAmount,doubleRate,intExpendatureId,strSiteId});

				}
			}*/

		}catch(Exception e){
			e.printStackTrace();
		}

		return intCount;
	}
	/****************************** getting the locatin and field details start
	 * @throws ParseException *****************************/
	@Override
	public List<ProductDetails> getLocationFieldData(String poNumber,boolean isModify) throws ParseException {
		JdbcTemplate template = null;
		
		
		List<ProductDetails> list = new ArrayList<ProductDetails>(); 
		List<Map<String, Object>> dbTransDts = null;
		String query ="";
		try {
		template = new JdbcTemplate(DBConnection.getDbConnection());
		if(isModify){
			 query = "SELECT (C.NAME)AS CHILD_PROD_NAME,min(MPDLD.HOARDING_DATE) as fromDate,"
					+" max(MPDLD.HOARDING_DATE) as todate,MPDLD.TIME,MPDLD.QUANTITY,S.SITE_NAME," 
					+" MPDLD.AMOUNT_PER_UNIT_AFTER_TAX, MPDLD.PRODUCT_SERIAL_NO,sum(MPDLD.TOTAL_AMOUNT) AS TOTAL,MHD.HOARDING_AREA,S.SITE_ID,C.CHILD_PRODUCT_ID,MHD.HOARDING_ID "
					+" from CHILD_PRODUCT C,SITE S,MRKT_TEMP_PO_PROD_LOC_DTLS MPDLD"
					+" LEFT OUTER JOIN MRKT_HOARDING_DTLS MHD ON MHD.HOARDING_ID=MPDLD.LOCATION_ID "
					+" where MPDLD.PO_NUMBER=? AND MPDLD.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID " 
					+" AND S.SITE_ID=MPDLD.SITE_ID   group by C.NAME,MHD.HOARDING_AREA,MPDLD.TIME,"
					+" MPDLD.QUANTITY,S.SITE_NAME,MPDLD.AMOUNT_PER_UNIT_AFTER_TAX,MPDLD.TOTAL_AMOUNT,MPDLD.PRODUCT_SERIAL_NO,S.SITE_ID,C.CHILD_PRODUCT_ID,MHD.HOARDING_ID";
		
		}else{
		 query = "SELECT (C.NAME)AS CHILD_PROD_NAME,min(MPDLD.HOARDING_DATE) as fromDate,"
					+" max(MPDLD.HOARDING_DATE) as todate,MPDLD.TIME,MPDLD.QUANTITY,S.SITE_NAME," 
					+" MPDLD.AMOUNT_PER_UNIT_AFTER_TAX, MPDLD.PRODUCT_SERIAL_NO,sum(MPDLD.TOTAL_AMOUNT) AS TOTAL,MHD.HOARDING_AREA,S.SITE_ID,C.CHILD_PRODUCT_ID,MHD.HOARDING_ID "
					+" from CHILD_PRODUCT C,SITE S,MRKT_PO_PROD_LOC_DTLS MPDLD"
					+" LEFT OUTER JOIN MRKT_HOARDING_DTLS MHD ON MHD.HOARDING_ID=MPDLD.LOCATION_ID "
					+" where MPDLD.PO_NUMBER=? AND MPDLD.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID " 
					+" AND S.SITE_ID=MPDLD.SITE_ID   group by C.NAME,MHD.HOARDING_AREA,MPDLD.TIME,"
					+" MPDLD.QUANTITY,S.SITE_NAME,MPDLD.AMOUNT_PER_UNIT_AFTER_TAX,MPDLD.TOTAL_AMOUNT,MPDLD.PRODUCT_SERIAL_NO,S.SITE_ID,C.CHILD_PRODUCT_ID,MHD.HOARDING_ID";
		}
		dbTransDts = template.queryForList(query, new Object[]{poNumber});
		int sno = 0;
		if (null != dbTransDts && dbTransDts.size() > 0) {
			for (Map<?, ?> GetLocationDetails : dbTransDts) {
				ProductDetails productdetails=new ProductDetails();
				sno++;
				productdetails.setStrChildProdname(GetLocationDetails.get("CHILD_PROD_NAME") == null ? "-" : GetLocationDetails.get("CHILD_PROD_NAME").toString());	
				productdetails.setLocationArea(GetLocationDetails.get("HOARDING_AREA") == null ? "-" : GetLocationDetails.get("HOARDING_AREA").toString());	
				String fromDate = GetLocationDetails.get("fromDate") == null ? "-" : GetLocationDetails.get("fromDate").toString();
				String toDate = GetLocationDetails.get("todate") == null ? "-" : GetLocationDetails.get("todate").toString();
				productdetails.setLocationTime(GetLocationDetails.get("TIME") == null ? "-" : GetLocationDetails.get("TIME").toString());
				productdetails.setLocationQuantity(GetLocationDetails.get("QUANTITY") == null ? "-" : GetLocationDetails.get("QUANTITY").toString());
				productdetails.setLocationSiteName(GetLocationDetails.get("SITE_NAME") == null ? "-" : GetLocationDetails.get("SITE_NAME").toString());
				productdetails.setAmountPerUnit(GetLocationDetails.get("AMOUNT_PER_UNIT_AFTER_TAX") == null ? "-" : GetLocationDetails.get("AMOUNT_PER_UNIT_AFTER_TAX").toString());
				productdetails.setTotalAmount(GetLocationDetails.get("TOTAL") == null ? "-" : GetLocationDetails.get("TOTAL").toString());
				productdetails.setSite_Id(GetLocationDetails.get("SITE_ID") == null ? "-" : GetLocationDetails.get("SITE_ID").toString());
				productdetails.setChild_ProductId(GetLocationDetails.get("CHILD_PRODUCT_ID") == null ? "-" : GetLocationDetails.get("CHILD_PRODUCT_ID").toString());
				productdetails.setLocationAreaId(GetLocationDetails.get("HOARDING_ID") == null ? "" : GetLocationDetails.get("HOARDING_ID").toString());
				//amountPerUnit=GetLocationDetails.get("AMOUNT_PER_UNIT_AFTER_TAX") == null ? "0" : GetLocationDetails.get("AMOUNT_PER_UNIT_AFTER_TAX").toString();
				//totalAmount=GetLocationDetails.get("TOTAL") == null ? "0" : GetLocationDetails.get("TOTAL").toString();

				SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
				Date from_Date = input.parse(fromDate); 
				Date to_Date = input.parse(toDate);
				SimpleDateFormat output = new SimpleDateFormat("dd-MMM-yy");
				//SimpleDateFormat output1 = new SimpleDateFormat("dd-MMM-yyyy");
				fromDate=output.format(from_Date);
				toDate=output.format(to_Date);
				//String one=output1.format(from_Date);
				productdetails.setLocationFromdate(fromDate);
				productdetails.setLocationToDate(toDate);
				productdetails.setStrSerialNumber(String.valueOf(sno));
				//locationData +=childProductName+"@@"+Location+"@@"+fromDate+"@@"+toDate+"@@"+time+"@@"+quantity+"@@"+siteName+"&&";
				list.add(productdetails);

			}
		}
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return list;

	}
	// getting the revised po number start 
	public String getPermenentRevisedPoNumber(String editPonumber) {
		String tempPoNumber=editPonumber;
		int temprevision_no=0;
		String strPONumber="";
		if(tempPoNumber.contains("/R")){
			String data=tempPoNumber.split("/R")[1];
			if(data.contains("/U")){
				String data1=data.split("/U")[0];
				temprevision_no=Integer.valueOf(data1)+1;
				strPONumber=tempPoNumber.replace("R"+Integer.valueOf(data1), "R"+temprevision_no);
				//System.out.print("the split data1  "+data1);
			}else{
				temprevision_no=Integer.valueOf(data)+1;
				strPONumber=tempPoNumber.replace("R"+Integer.valueOf(data), "R"+temprevision_no);
				
			}
			}else{
				strPONumber=tempPoNumber+"/R"+"1";
				System.out.print("else the split data ");
			}
		String strPODate=objPurchaseDepartmentIndentProcessDao.inactiveOldPo(editPonumber,"false");
		return strPONumber;
	}
	public boolean checkAvailableAreaCreatedOrnot(String month) {
		boolean status=false;
		JdbcTemplate template = null;
		String strMonth="";
		List<Map<String, Object>> dbTransDts = null;
		try{
		template = new JdbcTemplate(DBConnection.getDbConnection());
		if(month!=null && !month.equals("")){
		String query="SELECT DISTINCT(MONTH_YEAR) AS MONTH FROM MRKT_MONTHLY_AVAIL_AREA WHERE MONTH_YEAR=?";
		dbTransDts = template.queryForList(query, new Object[]{month});
		if (null != dbTransDts && dbTransDts.size() > 0) {
			for (Map<?, ?> GetLocationDetails : dbTransDts) {
				strMonth=GetLocationDetails.get("MONTH")==null  ? "" : GetLocationDetails.get("MONTH").toString();
				if(!strMonth.equals("")){
					status=true;
					break;
				}
			}
			}
		}
		
		
		}catch(Exception e){
			e.getMessage();
		}
		return status;
	}
	// getting the siteName for siteWise selection strat
	public String gettingSiteNameForSiteWise(List<ProductDetails> poDetails){
		String siteWisenumber="";
		String siteName=poDetails.get(0).getType_Of_Po();
		if(siteName.equalsIgnoreCase("SiteWise")){
			siteWisenumber=poDetails.get(0).getType_Of_Po_Details();
		}
		return siteWisenumber;
	}
	
	public Map<String,Double> getRemoveAreaProductWise(int strExpendatureId,List<String> removeList){
		String sites="";
		double total=0.0;
		String childProdId="";
		List<Map<String, Object>> dbIndentDts = null;
		Map<String,Double> map =new HashMap<String,Double>();
		if(removeList!=null && removeList.size()>0){
			for(int i=0;i<removeList.size();i++){
				sites+=removeList.get(i)+",";
			}
			sites =  sites.substring(0,sites.length()-1);
			sites=sites.replace(",","','");

			String sql = "SELECT CHILD_PRODUCT_ID,AMOUNT FROM MRKT_EXPENDATURE_DTLS WHERE SITE_ID IN ('"+sites+"') AND EXPENDATURE_ID=?";

			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {strExpendatureId});

		}
		for(Map<String, Object> prods : dbIndentDts) {
			total=Double.valueOf(prods.get("AMOUNT")==null ? "0" : prods.get("AMOUNT").toString());
			childProdId=prods.get("CHILD_PRODUCT_ID")==null ? "0" : prods.get("CHILD_PRODUCT_ID").toString();
			if(map.containsKey(childProdId)){
				map.put(childProdId,map.get(childProdId)+total);
			}else{
				map.put(childProdId,total);
			}
		}
		
		return map;
		
	}
	/******************************************* getting the Approve Emp Id*************************************************************/
	//get previous employee id
	public String  getpendingEmpId(String poNumber,String  user_Id)
	{
		List<Map<String, Object>> empList = null;
		String empId="";
		String query="select PO_CREATE_APPROVE_EMP_ID FROM SUMADHURA_PO_CRT_APPRL_DTLS  SPCAD where TEMP_PO_NUMBER='"+poNumber+"' and OPERATION_TYPE='C'";
		
		empList = jdbcTemplate.queryForList(query, new Object[] {});
		if(empList!= null){
			
			String sql="UPDATE SUMADHURA_PO_CRT_APPRL_DTLS SET OPERATION_TYPE='I' WHERE TEMP_PO_NUMBER='"+poNumber+"' AND OPERATION_TYPE='A'";
			int result = jdbcTemplate.update(sql, new Object[] {});
			
			for(Map<String, Object> emp : empList) {

				empId=emp.get("PO_CREATE_APPROVE_EMP_ID").toString();
				
			}
		}
	return empId;
	
	}
	// getting trhe approval email start
	public List<String> getApprovalEmpMails(String tempPoNumber){
		List<Map<String, Object>> dbEmpDts = null;
		List<Map<String, Object>> emailDts = null;
		List<String> mailsList = new ArrayList<String>();
		String empId="";
		String strEmpId="";
		String mailId="";
		String strMailId="";

		String query = "select PO_CREATE_APPROVE_EMP_ID FROM SUMADHURA_PO_CRT_APPRL_DTLS where OPERATION_TYPE='A' AND TEMP_PO_NUMBER=?";

		dbEmpDts = jdbcTemplate.queryForList(query, new Object[] { tempPoNumber});
		if(dbEmpDts!=null && dbEmpDts.size()>0){
			for(Map<String, Object> emp : dbEmpDts) {

				empId=emp.get("PO_CREATE_APPROVE_EMP_ID")==null ? "" :emp.get("PO_CREATE_APPROVE_EMP_ID").toString();
				strEmpId+=empId+",";

			}
		}
		if(strEmpId!=null && !strEmpId.equals("")){
			strEmpId=strEmpId.replace(",","','");
			String sql="SELECT EMP_EMAIL FROM SUMADHURA_EMPLOYEE_DETAILS WHERE EMP_ID IN ('"+strEmpId+"')";
			emailDts=jdbcTemplate.queryForList(sql, new Object[] {});
			if(emailDts!=null && emailDts.size()>0){
				for(Map<String, Object> emp : emailDts) {

					mailId=emp.get("EMP_EMAIL")==null ? "" :emp.get("EMP_EMAIL").toString();
					mailsList.add(mailId);
					//strMailId+=mailId+",";

				}
			}
			/*if(strMailId!=null && !strMailId.equals("")){
				strMailId=strMailId.substring(0, strMailId.length()-1);
			}*/
		}
		
		return mailsList;
	}
	// get billing address purpose we can write
	public String updateReceivedQuantity(Map<String,String> locationDetails,String oldPoEntryId){
		String state ="";
		
		List<Map<String, Object>> dbEmpDts = null;
		String receivedQuantity="";
		String childProdId="";
		int result =0;
		String PoQuantity="";
		
		
		String sql="select RECEIVED_QUANTITY,CHILD_PRODUCT_ID,PO_QTY from SUMADHURA_PO_ENTRY_DETAILS where PO_ENTRY_ID=? ";
		dbEmpDts = jdbcTemplate.queryForList(sql, new Object[] { oldPoEntryId});
		
		if(dbEmpDts!=null && dbEmpDts.size()>0){
			for(Map<String, Object> emp : dbEmpDts) {
				receivedQuantity=emp.get("RECEIVED_QUANTITY")==null ? "0" :emp.get("RECEIVED_QUANTITY").toString();
				childProdId=emp.get("CHILD_PRODUCT_ID")==null ? "" :emp.get("CHILD_PRODUCT_ID").toString();
				PoQuantity=emp.get("PO_QTY")==null ? "" :emp.get("PO_QTY").toString();
				if(locationDetails.containsKey(childProdId)){
					String query = "update SUMADHURA_PO_ENTRY_DETAILS set RECEIVED_QUANTITY = ? where PO_ENTRY_DETAILS_ID = ?";
					result = jdbcTemplate.update(query, new Object[] { receivedQuantity,locationDetails.get(childProdId) });
				}

			}
		}
		
		return state;
	}
	// for getting the po details modify temp po purpose start
	
	public List<ProductDetails> getModifyMarketingTempPODetails(String poNumber, String reqSiteId) {
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
		//if(marketingDept.equals(reqSiteId)){
			//isMarketing=true;
			query = "select VD.VENDOR_ID,VD.VENDOR_NAME,VD.GSIN_NUMBER,VD.ADDRESS,SPE.INDENT_NO,VD.STATE,SPE.SUBJECT,SPE.PO_DATE,S.SITE_ID,S.SITE_NAME,SPE.OLD_PO_NUMBER," 
				+ " SPE.DELIVERY_DATE,SPE.PAYMENT_REQ_DAYS,SPE.VERSION_NUMBER,SPE.PO_ISSUE_DATE,SPE.REFFERENCE_NO,SPE.PREPARED_BY,SPE.PO_ENTRY_ID,SPE.TOTAL_AMOUNT,SPE.TYPE_OF_PO,SPE.TYPE_OF_PO_DETAILS "
				+ " from VENDOR_DETAILS VD,SITE S,SUMADHURA_TEMP_PO_ENTRY SPE "
				+ " where SPE.VENDOR_ID = VD.VENDOR_ID AND SPE.SITE_ID = S.SITE_ID "
				+ " AND SPE.PO_NUMBER = '"+poNumber+"' AND SPE.SITE_ID = '"+reqSiteId+"' and SPE.PO_STATUS='A'";
			
		//}
		dbPODts = template.queryForList(query, new Object[]{});

		for(Map<String, Object> prods : dbPODts) {
			ProductDetails pd = new ProductDetails();
			pd.setVendorId(prods.get("VENDOR_ID") == null ? "" : prods.get("VENDOR_ID").toString());
			pd.setVendorName(prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString());
			pd.setStrGSTINNumber(prods.get("GSIN_NUMBER") == null ? "" : prods.get("GSIN_NUMBER").toString());
			pd.setVendorAddress(prods.get("ADDRESS") == null ? "" : prods.get("ADDRESS").toString());
			pd.setIndentNo(prods.get("INDENT_NO") == null ? "" : prods.get("INDENT_NO").toString());
			
			pd.setSite_Id(prods.get("SITE_ID") == null ? "" : prods.get("SITE_ID").toString());
			pd.setSiteName(prods.get("SITE_NAME") == null ? "" : prods.get("SITE_NAME").toString());
			pd.setPayment_Req_days(prods.get("PAYMENT_REQ_DAYS") == null ? "" : prods.get("PAYMENT_REQ_DAYS").toString());
			pd.setState(prods.get("STATE") == null ? "" : prods.get("STATE").toString());
			pd.setSubject(prods.get("SUBJECT") == null ? "" : prods.get("SUBJECT").toString());
			pd.setPoEntryId(Integer.valueOf(prods.get("PO_ENTRY_ID") == null ? "0" : prods.get("PO_ENTRY_ID").toString()));
			pd.setFinalamtdiv(prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString());
			pd.setEdit_Po_Number(prods.get("OLD_PO_NUMBER") == null ? "" : prods.get("OLD_PO_NUMBER").toString());
			
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
			if(typePo.equalsIgnoreCase("SiteWise")){
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
	// update the temp po table in marketing po start
	public int updateMarketingTempPo(String temp_Po_Number,String siteId)
	{

		String query =  "update SUMADHURA_TEMP_PO_ENTRY set VIEWORCANCEL ='MODIFIED',PO_STATUS='MODIFIED' WHERE VIEWORCANCEL='CANCEL' AND PO_NUMBER='"+temp_Po_Number+"' AND SITE_ID= '"+siteId+"'";
		int result = jdbcTemplate.update(query, new Object[] {});
	
	return result;
	
	}
	// getting the result of the old PO Entry iD and Po date Start
	public List<String> gettingPOEntryId(String oldPONumber){
		List<Map<String, Object>> dbPODts = null;
		JdbcTemplate template = null;
		List<String> list=new ArrayList<String>();
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		String query = "SELECT PO_ENTRY_ID,TO_CHAR(PO_DATE,'dd-MON-yy') AS PO_DATE FROM SUMADHURA_PO_ENTRY WHERE PO_NUMBER=? ";
		dbPODts = template.queryForList(query, new Object[]{oldPONumber});

	for(Map<String, Object> prods : dbPODts) {
		list.add(prods.get("PO_ENTRY_ID") == null ? "" : prods.get("PO_ENTRY_ID").toString());
		list.add(prods.get("PO_DATE") == null ? "" : prods.get("PO_DATE").toString());
	}
	return list;
	}
	
	@Override
	public List<ProductDetails> gettingOldPoReceivedQuantity(String poNumber,String reqSiteId,String oldPoEntryId) {
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
		List<Map<String, Object>> dbRevisedPODts = null;

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
			pd.setRequiredQuantity(prods.get("PO_QTY") == null ? "0" : prods.get("PO_QTY").toString());
			pd.setPrice(prods.get("PRICE") == null ? " " : prods.get("PRICE").toString());
			pd.setBasicAmt(prods.get("BASIC_AMOUNT") == null ? "0" : prods.get("BASIC_AMOUNT").toString());
			pd.setStrDiscount(prods.get("DISCOUNT") == null ? "" : prods.get("DISCOUNT").toString());
			pd.setStrAmtAfterDiscount(prods.get("AMOUNT_AFTER_DISCOUNT") == null ? "" : prods.get("AMOUNT_AFTER_DISCOUNT").toString());
			pd.setHsnCode(prods.get("HSN_CODE") == null ? " " : prods.get("HSN_CODE").toString());
			pd.setChildProductCustDisc(prods.get("VENDOR_PRODUCT_DESC") == null ? "-" : prods.get("VENDOR_PRODUCT_DESC").toString());
			String taxId = prods.get("TAX") == null ? " " : prods.get("TAX").toString();
			//String query1 = "select TAX_PERCENTAGE from INDENT_GST where TAX_ID = "+taxId+ " ";
		//	String taxValue = template.queryForObject(query1,String.class);
			String taxValue = prods.get("TAX_PERCENTAGE") == null ? " " : prods.get("TAX_PERCENTAGE").toString();
			
			
			pd.setTaxId(taxId);
			pd.setTax(taxValue);
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
			
			if(oldPoEntryId!=null && !oldPoEntryId.equals("")){
				String sql = "SELECT RECEIVED_QUANTITY FROM SUMADHURA_PO_ENTRY_DETAILS WHERE PO_ENTRY_ID=? and CHILD_PRODUCT_ID=?";
				dbRevisedPODts = template.queryForList(sql, new Object[]{oldPoEntryId,pd.getChild_ProductId()});
				for(Map<String, Object> prods1 : dbRevisedPODts) {
					pd.setRecivedQty(prods1.get("RECEIVED_QUANTITY") == null ? "0" : prods1.get("RECEIVED_QUANTITY").toString());
					//listOfGetProductDetails.add(pd);
				}
			}
			
			list.add(pd);
		}
		
		
	
		return list;
	}

	
	// getting the received quantity for revised po start
	/*public List<ProductDetails> gettingOldPoReceivedQuantity(String oldPOEntryId,List<ProductDetails> listOfGetProductDetails){
		List<Map<String, Object>> dbPODts = null;
		JdbcTemplate template = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			e.printStackTrace();
		}
		for(int i=0;i<listOfGetProductDetails.size();i++){
			ProductDetails pd = new ProductDetails();
			String query = "SELECT RECEIVED_QUANTITY FROM SUMADHURA_PO_ENTRY_DETAILS WHERE PO_ENTRY_ID=? and CHILD_PRODUCT_ID=?";
			dbPODts = template.queryForList(query, new Object[]{oldPOEntryId,listOfGetProductDetails.get(i).getChild_ProductId()});
			for(Map<String, Object> prods : dbPODts) {
				pd.setRecivedQty(prods.get("RECEIVED_QUANTITY") == null ? "0" : prods.get("RECEIVED_QUANTITY").toString());
				listOfGetProductDetails.add(pd);
			}
			
		}
		return listOfGetProductDetails;
	}*/
	// getting the data from when the user click on the side module
	public String gettingProductData(String fromDate,String toDate) {
		String productName = "";
		String amount = "";
		String productId="";
		List<Map<String, Object>> productList = null;
		final StringBuffer xmlData = new StringBuffer();
		String productdata="";
		JdbcTemplate template = null;
		String siteName="";
		Set<String> set=new HashSet<String>();
		List<String> list=new ArrayList<String>();
		String siteNames="";
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "": validateParams.getProperty("MARKETING_DEPT_ID").toString();

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			String sql =new StringBuilder("SELECT") 
							.append(" sum(MRKT_EXP_DTLS.AMOUNT) AS AMOUNT")
							.append(" ,P1.NAME PRODUCT_NAME,P1.PRODUCT_ID")
							.append(" ,listagg(SITE.SITE_NAME,',') WITHIN GROUP (ORDER BY SITE.SITE_NAME) AS SITE_NAME ")
							.append(" ,listagg(to_char(MRKT_EXP.INVOICE_DATE,'dd-mm-yyyy'),',') WITHIN GROUP (ORDER BY MRKT_EXP.INVOICE_DATE) AS INVOICE_DATE ")
							.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
							.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
							.append(" left outer join CHILD_PRODUCT CP1 JOIN SUB_PRODUCT SP1 JOIN PRODUCT P1 ON P1.PRODUCT_ID=SP1.PRODUCT_ID ")
							.append(" ON CP1.SUB_PRODUCT_ID=SP1.SUB_PRODUCT_ID ")
							.append(" ON MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CP1.CHILD_PRODUCT_ID ")
							.append(" WHERE MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A' ")
							.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID")
							.append(" AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) BETWEEN TO_DATE('"+fromDate+"','DD-MM-YY')")
							.append(" AND TO_DATE('"+toDate+"','DD-MM-YY')")
							.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID")
							.append(" AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID AND IE.SITE_ID='"+marketingDeptId+"' ")
							.append(" AND IE.INDENT_ENTRY_ID=MRKT_EXP.INDENT_ENTRY_ID")
							.append(" group by P1.NAME,P1.PRODUCT_ID").toString();
			
			productList = template.queryForList(sql);
			if (null != productList && productList.size() > 0) {
				xmlData.append("<xml>");
				
				for (Map siteDets : productList) {
					xmlData.append("<data>");
					siteName=siteDets.get("SITE_NAME") == null ? "" : siteDets.get("SITE_NAME").toString().trim();
					productId=siteDets.get("PRODUCT_ID") == null ? "" : siteDets.get("PRODUCT_ID").toString();
					productName = siteDets.get("PRODUCT_NAME") == null ? "" : siteDets.get("PRODUCT_NAME").toString();
					amount = siteDets.get("AMOUNT") == null ? "" : siteDets.get("AMOUNT").toString();
					list.add(siteName);
					xmlData.append("<productId>" +productId+"</productId>");
					xmlData.append("<label>" +productName+"</label>");
					xmlData.append("<value><![CDATA["+amount+"]]></value>");
					xmlData.append("</data>");
					
				}
				String numOfRec[] = null;
				for(int i=0;i<list.size();i++){
					siteNames+=list.get(i)+",";
				}
				if(siteNames!=null && !siteNames.equals("")){
					siteNames=siteNames.substring(0, siteNames.length()-1);
				}
				if((siteNames!= null) && (!siteNames.equals(""))) {
					numOfRec = siteNames.split(",");
				}
				for(int i=0;i<numOfRec.length;i++){
					set.add(numOfRec[i]);
				}
				xmlData.append("<notedata>");
				xmlData.append("<sitenames>" +set+"</sitenames>");
				xmlData.append("<fromdate>" +fromDate+"</fromdate>");
				xmlData.append("<todate><![CDATA["+toDate+"]]></todate>");
				xmlData.append("</notedata>");
				//xmlData.append(bufferMainProd);
			xmlData.append("</xml>");
			
			int PRETTY_PRINT_INDENT_FACTOR = 4;
			JSONObject xmlJSONObj;
			xmlJSONObj = XML.toJSONObject(xmlData.toString());
			productdata = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
			System.out.println(productdata);
		}	
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return productdata;
	}
	// getting the data user entered data then it will call start
	public String selectedProductDetailsForGraph(String fromDate,String toDate,String SiteData) {
		
		String productName = "";
		String amount = "";
		String productId="";
		List<Map<String, Object>> productList = null;
		final StringBuffer xmlData = new StringBuffer();
		List<Map<String, Object>> productListDates = null;
		Set<String> set=new HashSet<String>();
		String productdata="";
		JdbcTemplate template = null;
		//SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		SiteData=SiteData.replace(",","','");
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "": validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String startDate=validateParams.getProperty("MARKETING_DEPT_ID_DATE") == null ? "": validateParams.getProperty("MARKETING_DEPT_ID_DATE").toString();// THIS IS FOR NOTE PURPOSE WRITTEN THIS ONE 
		
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			StringBuilder sql = new StringBuilder("SELECT ")
					.append(" sum(MRKT_EXP_DTLS.AMOUNT) AS AMOUNT,P1.NAME PRODUCT_NAME,P1.PRODUCT_ID")
					//",listagg(SITE.SITE_NAME,',') WITHIN GROUP (ORDER BY SITE.SITE_NAME)") 
					//.append(" AS SITE_NAME ,listagg(to_char(MRKT_EXP.INVOICE_DATE,'dd-mm-yyyy'),',') WITHIN GROUP (ORDER BY MRKT_EXP.INVOICE_DATE) AS INVOICE_DATE ")
					.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
					.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
					.append(" left outer join CHILD_PRODUCT CP1 JOIN SUB_PRODUCT SP1 JOIN PRODUCT P1 ON P1.PRODUCT_ID=SP1.PRODUCT_ID ")
					.append(" ON CP1.SUB_PRODUCT_ID=SP1.SUB_PRODUCT_ID ON MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CP1.CHILD_PRODUCT_ID ")
					.append(" WHERE MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A' ")
					.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID ")
					.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID AND IE.SITE_ID='"+marketingDeptId+"'")
					.append(" AND IE.INDENT_ENTRY_ID=MRKT_EXP.INDENT_ENTRY_ID");
				
			if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
				sql.append(" AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) BETWEEN TO_DATE('"+fromDate+"','DD-MM-YY')AND TO_DATE('"+toDate+"','DD-MM-YY')");
				//.append(" AND TO_DATE('"+toDate+"','DD-MM-YY') ");
				
				if(SiteData!=null && !SiteData.equalsIgnoreCase("null") && !SiteData.equals("") ){
					sql.append(" AND MRKT_EXP_DTLS.SITE_ID in('"+SiteData+"')");
				}
				sql.append(" group by P1.NAME,P1.PRODUCT_ID");
			}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate)){
				sql.append(" AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) = TO_DATE('"+fromDate+"','DD-MM-YY')");
				if(SiteData!=null && !SiteData.equalsIgnoreCase("null") && !SiteData.equals("")){
					sql.append(" AND MRKT_EXP_DTLS.SITE_ID in('"+SiteData+"')");
				}
				sql.append(" group by P1.NAME,P1.PRODUCT_ID");
			}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)){
				sql.append(" AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) <= TO_DATE('"+toDate+"','DD-MM-YY')");
				if(SiteData!=null && !SiteData.equalsIgnoreCase("null") && !SiteData.equals("")){
					sql.append(" AND MRKT_EXP_DTLS.SITE_ID in('"+SiteData+"')");
				}
				sql.append(" group by P1.NAME,P1.PRODUCT_ID");
			}
			// dates and site purpose written this one 
			StringBuilder query = new StringBuilder("SELECT ")
								.append(" distinct(SITE.SITE_NAME)")
								//.append(" sum(MRKT_EXP_DTLS.AMOUNT) AS AMOUNT,P1.NAME PRODUCT_NAME,P1.PRODUCT_ID,listagg(SITE.SITE_NAME,',') WITHIN GROUP (ORDER BY SITE.SITE_NAME)") 
								//.append(" AS SITE_NAME ,listagg(to_char(MRKT_EXP.INVOICE_DATE,'dd-mm-yyyy'),',') WITHIN GROUP (ORDER BY MRKT_EXP.INVOICE_DATE) AS INVOICE_DATE ")
								.append(" FROM MRKT_EXPENDATURE MRKT_EXP,SITE SITE,CHILD_PRODUCT CHD_PRD,VENDOR_DETAILS VD,INDENT_ENTRY IE,MRKT_EXPENDATURE_DTLS MRKT_EXP_DTLS ")
								.append(" left outer join MRKT_HOARDING_DTLS MHD on MRKT_EXP_DTLS.HOARDING_ID = MHD.HOARDING_ID ")
								.append(" left outer join CHILD_PRODUCT CP1 JOIN SUB_PRODUCT SP1 JOIN PRODUCT P1 ON P1.PRODUCT_ID=SP1.PRODUCT_ID ")
								.append(" ON CP1.SUB_PRODUCT_ID=SP1.SUB_PRODUCT_ID ON MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CP1.CHILD_PRODUCT_ID ")
								.append(" WHERE MRKT_EXP.STATUS = 'A' AND IE.VENDOR_ID=VD.VENDOR_ID AND MRKT_EXP_DTLS.STATUS='A' ")
								.append(" AND MRKT_EXP_DTLS.CHILD_PRODUCT_ID = CHD_PRD.CHILD_PRODUCT_ID AND MRKT_EXP_DTLS.SITE_ID = SITE.SITE_ID ")
								.append(" AND MRKT_EXP.EXPENDATURE_ID = MRKT_EXP_DTLS.EXPENDATURE_ID AND IE.INVOICE_ID=MRKT_EXP.INVOICE_ID AND IE.SITE_ID='"+marketingDeptId+"'")
								.append(" AND IE.INDENT_ENTRY_ID=MRKT_EXP.INDENT_ENTRY_ID");
		
			if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){
				query.append(" AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) BETWEEN TO_DATE('"+fromDate+"','DD-MM-YY')AND TO_DATE('"+toDate+"','DD-MM-YY')");
				
				
				if(SiteData!=null && !SiteData.equalsIgnoreCase("null") && !SiteData.equals("")){
					query.append(" AND MRKT_EXP_DTLS.SITE_ID in('"+SiteData+"')");
				}
				
			}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate)){
				query.append(" AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) = TO_DATE('"+fromDate+"','DD-MM-YY')");
				if(SiteData!=null && !SiteData.equalsIgnoreCase("null") && !SiteData.equals("")){
					query.append(" AND MRKT_EXP_DTLS.SITE_ID in('"+SiteData+"')");
				}
				
			}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate)){
				query.append(" AND CAST(MRKT_EXP.INVOICE_DATE AS DATE) <= TO_DATE('"+toDate+"','DD-MM-YY')");
				if(SiteData!=null && !SiteData.equalsIgnoreCase("null") && !SiteData.equals("")){
					query.append(" AND MRKT_EXP_DTLS.SITE_ID in('"+SiteData+"')");
				}
				
			}
			productListDates = template.queryForList(query.toString());
			
			productList = template.queryForList(sql.toString());
			
			if (null != productList && productList.size() > 0) {
				xmlData.append("<xml>");

				for (Map siteDets : productList) {
					xmlData.append("<data>");
					productId=siteDets.get("PRODUCT_ID") == null ? "" : siteDets.get("PRODUCT_ID").toString();
					productName = siteDets.get("PRODUCT_NAME") == null ? "" : siteDets.get("PRODUCT_NAME").toString();
					amount = siteDets.get("AMOUNT") == null ? "" : siteDets.get("AMOUNT").toString();
					if(productName.contains("&")){
						productName=productName.replace("&", "and");
					}
					xmlData.append("<productId>" +productId+"</productId>");
					xmlData.append("<label>" +productName+"</label>");
					xmlData.append("<value><![CDATA["+amount+"]]></value>");
					xmlData.append("</data>");
				}

				for (Map site : productListDates) {
					set.add(site.get("SITE_NAME") == null ? "" : site.get("SITE_NAME").toString());
				}
				if(fromDate==null || fromDate.equals("")){
					fromDate=startDate;//"03-12-18";
				}else if(toDate==null || toDate.equals("")){
					toDate=fromDate;
				}
				xmlData.append("<notedata>");
				xmlData.append("<sitenames>" +set+"</sitenames>");
				xmlData.append("<fromdate>" +fromDate+"</fromdate>");
				xmlData.append("<todate><![CDATA["+toDate+"]]></todate>");
				xmlData.append("</notedata>");
				xmlData.append("</xml>");

				int PRETTY_PRINT_INDENT_FACTOR = 4;
				JSONObject xmlJSONObj;
				xmlJSONObj = XML.toJSONObject(xmlData.toString());
				productdata = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
				System.out.println(productdata);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return productdata;
	}
	// this is for when the user has to create by state taken 
	// get billing address purpose we can write
	public String gettingBillingState(String poEntryId){
		String state ="";
		//String deptId = "";
		String sql="SELECT DEPT_NAME FROM SUMADHURA_DEPARTMENT_DETAILS WHERE DEPT_ID in "
				+" (SELECT DEPT_ID FROM SUMADHURA_EMPLOYEE_DETAILS WHERE EMP_ID in "
				+" (SELECT PO_CREATE_APPROVE_EMP_ID FROM SUMADHURA_PO_CRT_APPRL_DTLS WHERE OPERATION_TYPE='C' AND TEMP_PO_NUMBER in "
				+" (SELECT TEMP_PO_NUMBER FROM SUMADHURA_PO_CRT_APPRL_DTLS WHERE PO_ENTRY_ID='"+poEntryId+"')))";
		state = jdbcTemplate.queryForObject(sql, String.class);
		
		return state;
	}
}