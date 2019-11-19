package com.sumadhura.transdao;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;

import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.IssueToOtherSiteInwardBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IssueToOtherSiteDto;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Repository
public class IssueToOtherSiteDaoImpl extends UIProperties  implements IssueToOtherSiteDao {

	static Logger log = Logger.getLogger(IssueToOtherSiteDaoImpl.class);

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private IndentReceiveDao ird;

	@Override
	public Map<String, String> issueToOtherSiteProducts(String siteId) {
		String marketing_Dept_Id=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String product_Dept="";
		Map<String, String> products = null;
		List<Map<String, Object>> dbProductsList = null;

		products = new HashMap<String, String>();
		
		if(siteId.equals(marketing_Dept_Id)){product_Dept="MARKETING";}
		else{product_Dept="STORE";}

		String prodsQry = "SELECT PRODUCT_ID, NAME FROM PRODUCT WHERE STATUS = 'A' AND PRODUCT_DEPT in ('ALL','"+product_Dept+"') order by NAME ASC";
		log.debug("Query to fetch product = "+prodsQry);

		dbProductsList = jdbcTemplate.queryForList(prodsQry, new Object[]{});

		for(Map<String, Object> prods : dbProductsList) {
			products.put(String.valueOf(prods.get("PRODUCT_ID")), String.valueOf(prods.get("NAME")));
		}
		return products;
	}	
	
	@Override
	public Map<String, String> loadSitesInformationForIssueToOtherSite() {

		Map<String, String> sites = null;
		List<Map<String, Object>> dbSitesList = null;

		sites = new HashMap<String, String>();

		String sitesQry = "SELECT SITE_ID, SITE_NAME FROM SITE where STATUS in ('ACTIVE','IDEAL')";
		log.debug("Query to fetch site info = "+sitesQry);

		dbSitesList = jdbcTemplate.queryForList(sitesQry, new Object[]{});

		for(Map<String, Object> site : dbSitesList) {
			sites.put(String.valueOf(site.get("SITE_ID")), String.valueOf(site.get("SITE_NAME")));
		}
		return sites;
	}

	@Override
	public String issueToOtherSiteSubProducts(String prodId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbSubProductsList = null;		

		log.debug("Product Id = "+prodId);

		sb = new StringBuffer();

		String subProdsQry = "SELECT SUB_PRODUCT_ID, NAME FROM SUB_PRODUCT WHERE PRODUCT_ID = ? AND STATUS = 'A'";
		log.debug("Query to fetch subproduct = "+subProdsQry);

		dbSubProductsList = jdbcTemplate.queryForList(subProdsQry, new Object[]{prodId});

		for(Map<String, Object> subProds : dbSubProductsList) {
			sb = sb.append(String.valueOf(subProds.get("SUB_PRODUCT_ID"))+"_"+String.valueOf(subProds.get("NAME"))+"|");
		}

		return sb.toString();
	}

	@Override
	public String issueToOtherSiteChildProducts(String subProductId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbChildProductsList = null;		

		log.debug("Sub Product Id = "+subProductId);

		sb = new StringBuffer();

		String subProdsQry = "SELECT CHILD_PRODUCT_ID, NAME FROM CHILD_PRODUCT WHERE SUB_PRODUCT_ID = ? AND STATUS = 'A'";
		log.debug("Query to fetch child product = "+subProdsQry);

		dbChildProductsList = jdbcTemplate.queryForList(subProdsQry, new Object[]{subProductId});

		for(Map<String, Object> childProds : dbChildProductsList) {
			sb = sb.append(String.valueOf(childProds.get("CHILD_PRODUCT_ID"))+"_"+String.valueOf(childProds.get("NAME"))+"|");
		}		
		return sb.toString();
	}

	@Override
	public String issueToOtherSiteMeasurements(String childProdId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbMeasurementsList = null;		

		log.debug("Child Product Id = "+childProdId);

		sb = new StringBuffer();

		String measurementsQry = "SELECT MEASUREMENT_ID, NAME FROM MEASUREMENT WHERE CHILD_PRODUCT_ID = ?";
		log.debug("Query to fetch measurement(s) = "+measurementsQry);

		dbMeasurementsList = jdbcTemplate.queryForList(measurementsQry, new Object[]{childProdId});

		for(Map<String, Object> measurements : dbMeasurementsList) {
			sb = sb.append(String.valueOf(measurements.get("MEASUREMENT_ID"))+"_"+String.valueOf(measurements.get("NAME"))+"|");
		}
		return sb.toString();
	}

	@Override
	public int insertRequesterData(int indentEntryId, String userId, String siteId, IssueToOtherSiteDto iiReqDto,String strFromoSite) {


		int result = 0;

		Calendar cal1 = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");

		String requesterQry = "INSERT INTO INDENT_ENTRY (INDENT_ENTRY_ID, USER_ID, SITE_ID, REQ_ID,RECEIVED_OR_ISSUED_DATE,REQUESTER_NAME, REQUESTER_ID, NOTE,ISSUE_TYPE, INDENT_TYPE,VENDOR_ID,VEHICLENO,INDENT_CREATION_ID,ENTRY_DATE)"
			+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,sysdate)";
		log.debug("Query for requester = "+requesterQry);
		result = jdbcTemplate.update(requesterQry, new Object[] {
				indentEntryId, 
				userId, 
				siteId, 
				iiReqDto.getReqId(), 
				iiReqDto.getReqDate()+" "+sdf.format(cal1.getTime()), 
				iiReqDto.getRequesterName(), 
				iiReqDto.getRequesterId(),
				iiReqDto.getPurpose(),
				iiReqDto.getIssueType(),
				"OUTO",strFromoSite,iiReqDto.getVehicleNo(),iiReqDto.getIndentNumber()
		}
		);
		log.debug("Result = "+result);			
		logger.info("IssueToOtherSiteDaoImpl --> insertRequesterData() --> Result = "+result);

		return result;
	}

	@Override
	public int insertIndentIssueData(int indentEntrySeqNum, IssueToOtherSiteDto issueDto) {

		int result = 0;			

		String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID,"
			+ " PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, ISSUED_QTY,"
			+ " MEASUR_MNT_ID, MEASUR_MNT_NAME, UORF, REMARKS, ENTRY_DATE)"
			+ " VALUES (INDENT_ENTRY_DETAILS_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate)";
		log.debug("Query for indent issue = "+indentIssueQry);

		result = jdbcTemplate.update(indentIssueQry, new Object[]{
				indentEntrySeqNum, 
				issueDto.getProdId1(), 
				issueDto.getProdName1(), 
				issueDto.getSubProdId1(), 
				issueDto.getSubProdName1(), 
				issueDto.getChildProdId1(), 
				issueDto.getChildProdName1(), 
				issueDto.getQuantity1(), 
				issueDto.getMeasurementId1(), 
				issueDto.getMeasurementName1(), 
				issueDto.getUOrF1(),
				issueDto.getRemarks1(),
		}
		);
		log.debug("Result = "+result);
		logger.info("IssueToOtherSiteDaoImpl --> insertIndentIssueData() --> Result = "+result);

		return result;
	}

	@Override
	public int updateIndentAvalibility(IssueToOtherSiteDto toOtherSiteDto, String siteId) {
		List<Map<String, Object>> dbProductDetailsList = null;

		int result = 0;
		String availability_Id="";
		double issue_Form_Quantity=0.0;
		double quantity=0.0;

		String query="SELECT PRODUT_QTY,INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND MESURMENT_ID= ? AND SITE_ID=?  ";
		
		dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] {
				toOtherSiteDto.getProdId1(), 
				toOtherSiteDto.getSubProdId1(), 
				toOtherSiteDto.getChildProdId1(), 
				toOtherSiteDto.getMeasurementId1(), 
				siteId
		});
	
		if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
			for (Map<String, Object> prods : dbProductDetailsList) {
				quantity = Double.parseDouble(prods.get("PRODUT_QTY") == null ? "" : prods.get("PRODUT_QTY").toString());
				availability_Id = (prods.get("INDENT_AVAILABILITY_ID") == null ? "" : prods.get("INDENT_AVAILABILITY_ID").toString());
			}
		}
			issue_Form_Quantity=toOtherSiteDto.getQuantity1();
			quantity=quantity-issue_Form_Quantity;

		String updateIndentAvalibilityQry = "UPDATE INDENT_AVAILABILITY SET PRODUT_QTY ='"+quantity+"' WHERE INDENT_AVAILABILITY_ID ='"+availability_Id+"'";
		log.debug("Query for update indent avalibility = "+updateIndentAvalibilityQry);

		result = jdbcTemplate.update(updateIndentAvalibilityQry, new Object[] {});
		log.debug("Result = "+result);
		logger.info("IssueToOtherSiteDaoImpl --> updateIndentAvalibility() --> Result = "+result);

		return result;
	}

	@Override
	public void updateIndentAvalibilityWithNewIndent(IssueToOtherSiteDto toOtherSiteDto, String siteId) {

		int result = 0;

		String requesterQry = "INSERT INTO INDENT_AVAILABILITY (INDENT_AVAILABILITY_ID, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, PRODUT_QTY, MESURMENT_ID, SITE_ID) VALUES (INDENT_AVAILABILITY_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";
		log.debug("Query for new indent entry in indent availability = "+requesterQry);

		result = jdbcTemplate.update(requesterQry, new Object[] {
				toOtherSiteDto.getProdId1(), 
				toOtherSiteDto.getSubProdId1(), 
				toOtherSiteDto.getChildProdId1(), 
				toOtherSiteDto.getQuantity1(), 
				toOtherSiteDto.getMeasurementId1(),
				siteId
		}
		);
		log.debug("Result = "+result);			
		logger.info("IssueToOtherSiteDaoImpl --> updateIndentAvalibilityWithNewIndent() --> Result = "+result);
	}

	@Override
	public int getIndentEntrySequenceNumber() {

		int indentEntrySeqNum = 0;

		indentEntrySeqNum = jdbcTemplate.queryForInt("SELECT INDENT_ENTRY_SEQ.NEXTVAL FROM DUAL");

		return indentEntrySeqNum;
	}

	@Override
	public String getIssueToOtherSiteProductAvailability(String prodId, String subProductId, String childProdId, String measurementId, String siteId) {

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
			qty = String.valueOf(availableQuantity.get("PRODUT_QTY"));
			break;
		}
		return qty;
	}


	public List<ViewIndentIssueDetailsBean> getViewIndentIssueDetails(String fromDate, String toDate, String siteId, String val) {

		String query = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<ViewIndentIssueDetailsBean> list = new ArrayList<ViewIndentIssueDetailsBean>();
		ViewIndentIssueDetailsBean indentObj = null; 

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			if (StringUtils.isNotBlank(val)) {
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = "SELECT LD.USERNAME, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.RECEVED_QTY, IE.INVOICE_ID, IED.MEASUR_MNT_NAME FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = "SELECT LD.USERNAME, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.RECEVED_QTY, IE.INVOICE_ID, IED.MEASUR_MNT_NAME FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE='"+fromDate+"'";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = "SELECT LD.USERNAME, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.RECEVED_QTY, IE.INVOICE_ID, IED.MEASUR_MNT_NAME FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE='"+toDate+"'";
				}
			} else {
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE='"+fromDate+"'";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE='"+toDate+"'";
				}
			}


			dbIndentDts = template.queryForList(query, new Object[]{});
			if (StringUtils.isNotBlank(val)) {
				for(Map<String, Object> prods : dbIndentDts) {
					indentObj = new ViewIndentIssueDetailsBean();
					indentObj.setUserId(prods.get("USERNAME")==null ? "" : prods.get("USERNAME").toString());
					indentObj.setRequesterName(prods.get("MEASUR_MNT_NAME")==null ? "" : prods.get("MEASUR_MNT_NAME").toString());
					indentObj.setRequesterId(prods.get("INVOICE_ID")==null ? "" : prods.get("INVOICE_ID").toString());
					indentObj.setProductName(prods.get("PRODUCT_NAME")==null ? "" : prods.get("PRODUCT_NAME").toString());
					indentObj.setSubProdName(prods.get("SUB_PRODUCT_NAME")==null ? "" : prods.get("SUB_PRODUCT_NAME").toString());
					indentObj.setChildProdName(prods.get("CHILD_PRODUCT_NAME")==null ? "" : prods.get("CHILD_PRODUCT_NAME").toString());
					indentObj.setIssueQTY(prods.get("RECEVED_QTY")==null ? "" : prods.get("RECEVED_QTY").toString());
					list.add(indentObj);
				}
			} else {
				for(Map<String, Object> prods : dbIndentDts) {
					indentObj = new ViewIndentIssueDetailsBean();
					indentObj.setUserId(prods.get("USERNAME")==null ? "" : prods.get("USERNAME").toString());
					indentObj.setRequesterName(prods.get("REQUESTER_NAME")==null ? "" : prods.get("REQUESTER_NAME").toString());
					indentObj.setRequesterId(prods.get("REQUESTER_ID")==null ? "" : prods.get("REQUESTER_ID").toString());
					indentObj.setProductName(prods.get("PRODUCT_NAME")==null ? "" : prods.get("PRODUCT_NAME").toString());
					indentObj.setSubProdName(prods.get("SUB_PRODUCT_NAME")==null ? "" : prods.get("SUB_PRODUCT_NAME").toString());
					indentObj.setChildProdName(prods.get("CHILD_PRODUCT_NAME")==null ? "" : prods.get("CHILD_PRODUCT_NAME").toString());
					indentObj.setIssueQTY(prods.get("ISSUED_QTY")==null ? "" : prods.get("ISSUED_QTY").toString());
					list.add(indentObj);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			log.debug("Exception = "+ex.getMessage());
			logger.info("Exception Occured Inside getViewIndentIssueDetails() in IssueToOtherSiteDaoImpl class --"+ex.getMessage());
		} finally {
			query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;
	}




	@Override
	public String doIndentIssueToOtherSite(Model model, HttpServletRequest request, HttpSession session) {

		return null;
	}

	@Override
	public Map<String, String> loadSitesInformation() {

		Map<String, String> sites = null;
		List<Map<String, Object>> dbSitesList = null;

		sites = new HashMap<String, String>();

		String sitesQry = "SELECT SITE_ID, SITE_NAME FROM SITE where STATUS = 'ACTIVE'";
		log.debug("Query to fetch site info = "+sitesQry);

		dbSitesList = jdbcTemplate.queryForList(sitesQry, new Object[]{});

		for(Map<String, Object> site : dbSitesList) {
			sites.put(String.valueOf(site.get("SITE_ID")), String.valueOf(site.get("SITE_NAME")));
		}
		return sites;
	}

	@Override
	public List<IssueToOtherSiteDto> getPriceListDetails(IssueToOtherSiteDto issueDto, String strSiteId) {

		String sql = "";
		List<Map<String, Object>> dbProductsList = null;
		List<IssueToOtherSiteDto> allDetails = null;
		IssueToOtherSiteDto issueDetails = null;
		try {
			//sql = "SELECT PRICE_ID, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, AVAILABLE_QUANTITY, AMOUNT_PER_UNIT_AFTER_TAXES,HSN_CODE,AMOUNT_PER_UNIT_BEFORE_TAXES,TAX,OTHER_CHARGES,TAX_ON_OTHER_TRANSPORT_CHG FROM SUMADHURA_PRICE_LIST WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND TRUNC(CREATED_DATE) <= to_date('"+issueDto.getDate()+"', 'dd-MM-yy') AND STATUS='A' ORDER BY CREATED_DATE,PRICE_ID ASC";

			sql = "SELECT SPL.PRICE_ID, SPL.PRODUCT_ID, SPL.SUB_PRODUCT_ID, SPL.CHILD_PRODUCT_ID, SPL.UNITS_OF_MEASUREMENT, SPL.AVAILABLE_QUANTITY, "
				+" SPL.AMOUNT_PER_UNIT_AFTER_TAXES, "
				+" SPL.HSN_CODE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TAX,SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG ,"
				+" SPL.RECEVED_QTY,"	/*+" IED.RECEVED_QTY,"*/
				+ "SPL.OTHER_CHARGES_AFTER_TAX,IED.EXPIRY_DATE FROM SUMADHURA_PRICE_LIST SPL "
							/*+" ,INDENT_ENTRY_DETAILS IED "*/
				+ " LEFT OUTER JOIN INDENT_ENTRY_DETAILS IED  ON IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID AND IED.PRICE_ID=SPL.PRICE_ID "
				+" WHERE SPL.PRODUCT_ID=? AND SPL.SUB_PRODUCT_ID=? AND SPL.CHILD_PRODUCT_ID=?  AND SPL.UNITS_OF_MEASUREMENT=? AND SPL.SITE_ID=? AND "
				+" TRUNC(SPL.CREATED_DATE) <= to_date('"+issueDto.getDate()+"', 'dd-MM-yy') AND SPL.STATUS='A' "
							/*+" and  SPL.INDENT_ENTRY_DETAILS_ID  = IED.INDENT_ENTRY_DETAILS_ID "*/
				+" ORDER BY SPL.CREATED_DATE,SPL.PRICE_ID ASC";



			dbProductsList = jdbcTemplate.queryForList(sql, new Object[] {issueDto.getProdId1(),issueDto.getSubProdId1(), issueDto.getChildProdId1(), issueDto.getMeasurementId1(), strSiteId});
			allDetails = new ArrayList<IssueToOtherSiteDto>();
			for(Map<String, Object> prods : dbProductsList) {
				issueDetails = new IssueToOtherSiteDto();
				issueDetails.setStrPriceId(prods.get("PRICE_ID")==null ? "" : prods.get("PRICE_ID").toString());
				issueDetails.setProdId1(prods.get("PRODUCT_ID")==null ? "" : prods.get("PRODUCT_ID").toString());
				issueDetails.setSubProdId1(prods.get("SUB_PRODUCT_ID")==null ? "" : prods.get("SUB_PRODUCT_ID").toString());
				issueDetails.setChildProdId1(prods.get("CHILD_PRODUCT_ID")==null ? "" : prods.get("CHILD_PRODUCT_ID").toString());
				issueDetails.setMeasurementId1(prods.get("UNITS_OF_MEASUREMENT")==null ? "" : prods.get("UNITS_OF_MEASUREMENT").toString());
				issueDetails.setQuantity1(Double.valueOf(prods.get("AVAILABLE_QUANTITY")==null ? "0" : prods.get("AVAILABLE_QUANTITY").toString()));
				issueDetails.setPrice1(prods.get("AMOUNT_PER_UNIT_AFTER_TAXES")==null ? "0" : prods.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString());
				issueDetails.setStrHSNCode1(prods.get("HSN_CODE")==null ? "" : prods.get("HSN_CODE").toString());
				issueDetails.setAmountPerUnitBeforeTax(prods.get("AMOUNT_PER_UNIT_BEFORE_TAXES")==null ? "1" : prods.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
				issueDetails.setTax(prods.get("TAX")==null ? "0" : prods.get("TAX").toString());
				issueDetails.setOtherOrTransportCharges(prods.get("OTHER_CHARGES")==null ? "" : prods.get("OTHER_CHARGES").toString());
				issueDetails.setTaxotherOrTransportCharges(prods.get("TAX_ON_OTHER_TRANSPORT_CHG")==null ? "" : prods.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
				issueDetails.setReceivedQuantity(prods.get("RECEVED_QTY")==null ? "0" : prods.get("RECEVED_QTY").toString());
				issueDetails.setAmountAfterTaxotherOrTransportCharges(prods.get("OTHER_CHARGES_AFTER_TAX")==null ? "0" : prods.get("OTHER_CHARGES_AFTER_TAX").toString());
				issueDetails.setExpiryDate(prods.get("EXPIRY_DATE")==null ? "N/A" : prods.get("EXPIRY_DATE").toString());
				
				allDetails.add(issueDetails);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return allDetails;
	}

	@Override
	public void updateIndentEntryAmountColumn(String basicAmt, String quantity, String indentId) {
		String totalAmt = new DecimalFormat().format(Double.valueOf(basicAmt));
		String query = "UPDATE INDENT_ENTRY SET TOTAL_AMOUNT=? WHERE INDENT_ENTRY_ID=? ";
		jdbcTemplate.update(query, new Object[] {totalAmt, indentId});


	}

	@Override
	public int insertIndentIssueData(int indentEntrySeqNum, IssueToOtherSiteDto issueDto, String basicAmt, String quantity, String priceId, String userId, String siteId,String expiryDate)  {
		int result = 0;	
		int intSeqNo = 0;
		AuditLogDetailsBean auditBean = null;
		Double totAmt = Double.parseDouble(basicAmt.replace(",", "").trim());
		Double qty = Double.parseDouble(quantity);
		Double perPiceAmt = totAmt*qty;
		String totalAmt = new DecimalFormat().format(perPiceAmt);


		String getSeqNo = "select   INDENT_ENTRY_DETAILS_SEQ.NEXTVAL from dual";


		intSeqNo = jdbcTemplate.queryForInt(getSeqNo, new Object[]{}); 



		//String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, ISSUED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, HSN_CODE, UORF, REMARKS, ENTRY_DATE,PRICE,TOTAL_AMOUNT, PRICE_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate,?,?,?)";

		String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, ISSUED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, HSN_CODE, UORF, REMARKS, ENTRY_DATE, PRICE_ID,EXPIRY_DATE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate,?,?)";

		log.debug("Query for indent issue = "+indentIssueQry);

		result = jdbcTemplate.update(indentIssueQry, new Object[]{intSeqNo,
				indentEntrySeqNum, 
				issueDto.getProdId1(), 
				issueDto.getProdName1(), 
				issueDto.getSubProdId1(), 
				issueDto.getSubProdName1(), 
				issueDto.getChildProdId1(), 
				issueDto.getChildProdName1(), 
				issueDto.getQuantity1(), 
				issueDto.getMeasurementId1(), 
				issueDto.getMeasurementName1(), 
				issueDto.getStrHSNCode1(),
				issueDto.getUOrF1(),
				issueDto.getRemarks1(), 
				//basicAmt,totalAmt,
				priceId,expiryDate
		}
		);

		auditBean = new AuditLogDetailsBean();
		auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
		auditBean.setLoginId(userId);
		auditBean.setOperationName("New Issue");
		auditBean.setStatus("Info");
		auditBean.setSiteId(siteId);
		new SaveAuditLogDetails().saveAuditLogDetails(auditBean);
		log.debug("Result = "+result);
		logger.info("IndentIssueDao --> insertIndentIssueData() --> Result = "+result);


		if(result > 0){
			result = intSeqNo;
		}

		return result;
	}

	@Override
	public void updatePriceListDetails(String quantity, String status, String priceId) {
		String query = "UPDATE SUMADHURA_PRICE_LIST SET AVAILABLE_QUANTITY=?, STATUS=?,UPDATED_DATE=SYSDATE WHERE PRICE_ID=? ";
		jdbcTemplate.update(query, new Object[] { quantity, status, priceId });

	}

	@Override
	public void updateIssueDetailsSumadhuClosingBalByProduct(IssueToOtherSiteDto issueDto, String strSiteId,
			String amount, double issuQuantiy) {
		String query = "";
		Double quantity = 0.0;
		Double totalAmt = 0.0;
		Double totalAmtForOneQty = 0.0;

		List<Map<String, Object>> dbClosingBalancesList = null;

		Date today = new Date();                   
		@SuppressWarnings("deprecation")
		Date myDate = new Date(issueDto.getDate()+" 23:59:59");


		if (today.compareTo(myDate)>0) {

			query = "SELECT CLOSING_BAL_BY_PRODUCT_ID, QUANTITY, TOTAL_AMOUNT FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) = TO_DATE('"+ issueDto.getDate() + "', 'dd-MM-yy') AND ROWNUM <= 1 ORDER BY DATE_AND_TIME  DESC";
			dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] { issueDto.getProdId1(), issueDto.getSubProdId1(), issueDto.getChildProdId1(), strSiteId, issueDto.getMeasurementId1() });

			if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {


				quantity = Double.valueOf(quantity - issuQuantiy);
				totalAmtForOneQty = (issuQuantiy * Double.valueOf(amount));

				query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT SET QUANTITY=QUANTITY-'" + issuQuantiy + "', TOTAL_AMOUNT= TOTAL_AMOUNT-'" + totalAmtForOneQty + "' WHERE DATE_AND_TIME >= '" + issueDto.getDate() + "' AND PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ?";
				jdbcTemplate.update(query, new Object[] { issueDto.getProdId1(), issueDto.getSubProdId1(), issueDto.getChildProdId1(), strSiteId, issueDto.getMeasurementId1() });
			} 
		} 

	}

	@Override
	public List<IssueToOtherSiteDto> getPriceListDetails(IssueToOtherSiteDto issueDto, String strSiteId,
			String string) {
		String sql = "";
		List<Map<String, Object>> dbProductsList = null;
		List<IssueToOtherSiteDto> allDetails = null;
		IssueToOtherSiteDto issueDetails = null;
	
			//sql = "select * from( SELECT PRICE_ID, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, AVAILABLE_QUANTITY, AMOUNT_PER_UNIT_AFTER_TAXES FROM SUMADHURA_PRICE_LIST WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND STATUS='A' AND TRUNC(CREATED_DATE) < to_date('"+issueDto.getDate()+"', 'dd-MM-yy')  ORDER BY CREATED_DATE ASC ) where ROWNUM <=1";


			sql = "select * from( SELECT SPL.PRICE_ID, SPL.PRODUCT_ID, SPL.SUB_PRODUCT_ID, SPL.CHILD_PRODUCT_ID, SPL.UNITS_OF_MEASUREMENT, SPL.AVAILABLE_QUANTITY, "+
			" SPL.AMOUNT_PER_UNIT_AFTER_TAXES, "+
			" SPL.HSN_CODE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TAX,SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG , "+
			/*IED.RECEVED_QTY,*/" SPL.OTHER_CHARGES_AFTER_TAX,IED.EXPIRY_DATE FROM SUMADHURA_PRICE_LIST SPL "+/*,INDENT_ENTRY_DETAILS IED*/
			" LEFT OUTER JOIN INDENT_ENTRY_DETAILS IED  ON IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID AND IED.PRICE_ID=SPL.PRICE_ID "+
			" WHERE SPL.PRODUCT_ID=? AND SPL.SUB_PRODUCT_ID=? AND SPL.CHILD_PRODUCT_ID=? AND SPL.UNITS_OF_MEASUREMENT=? AND SPL.SITE_ID=? AND SPL.STATUS='A' "+
			" AND TRUNC(SPL.CREATED_DATE) < to_date('"+issueDto.getDate()+"', 'dd-MM-yy')  "+
			/*" and SPL.INDENT_ENTRY_DETAILS_ID  = IED.INDENT_ENTRY_DETAILS_ID "+*/
			" ORDER BY SPL.CREATED_DATE ASC ) where ROWNUM <=1";


			logger.info("Issue "+sql);
			dbProductsList = jdbcTemplate.queryForList(sql, new Object[] {issueDto.getProdId1(),issueDto.getSubProdId1(), issueDto.getChildProdId1(), issueDto.getMeasurementId1(), strSiteId});
			allDetails = new ArrayList<IssueToOtherSiteDto>();
			if (null == dbProductsList || dbProductsList.size() == 0){
				sql = "select * from( SELECT SPL.PRICE_ID, SPL.PRODUCT_ID, SPL.SUB_PRODUCT_ID, SPL.CHILD_PRODUCT_ID, SPL.UNITS_OF_MEASUREMENT, " +
				"SPL.AVAILABLE_QUANTITY,SPL.AMOUNT_PER_UNIT_AFTER_TAXES, SPL.HSN_CODE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES," +/*IED.RECEVED_QTY,*/
				" SPL.TAX,SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG,SPL.OTHER_CHARGES_AFTER_TAX,IED.EXPIRY_DATE FROM SUMADHURA_PRICE_LIST SPL " +
				" LEFT OUTER JOIN INDENT_ENTRY_DETAILS IED  ON IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID AND IED.PRICE_ID=SPL.PRICE_ID "+
				/*,INDENT_ENTRY_DETAILS IED*/" WHERE SPL.PRODUCT_ID=? AND SPL.SUB_PRODUCT_ID=? AND " +
				"SPL.CHILD_PRODUCT_ID=? AND SPL.UNITS_OF_MEASUREMENT=? AND SPL.SITE_ID=? AND SPL.STATUS='A' AND" +
				" TRUNC(SPL.CREATED_DATE) <= to_date('"+issueDto.getDate()+"', 'dd-MM-yy')  " +
				/*and SPL.INDENT_ENTRY_DETAILS_ID  = IED.INDENT_ENTRY_DETAILS_ID*/" ORDER BY SPL.CREATED_DATE ASC ) where" +
				" ROWNUM <=1";
				logger.info("Issue "+sql);
				dbProductsList = jdbcTemplate.queryForList(sql, new Object[] {issueDto.getProdId1(),issueDto.getSubProdId1(), issueDto.getChildProdId1(), issueDto.getMeasurementId1(), strSiteId});
			}
			for(Map<String, Object> prods : dbProductsList) {

				issueDetails = new IssueToOtherSiteDto();
				issueDetails.setPrice1(prods.get("AMOUNT_PER_UNIT_AFTER_TAXES")==null ? "" : prods.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString());
				issueDetails.setProdId1(prods.get("PRODUCT_ID")==null ? "" : prods.get("PRODUCT_ID").toString());
				issueDetails.setSubProdId1(prods.get("SUB_PRODUCT_ID")==null ? "" : prods.get("SUB_PRODUCT_ID").toString());
				issueDetails.setChildProdId1(prods.get("CHILD_PRODUCT_ID")==null ? "" : prods.get("CHILD_PRODUCT_ID").toString());
				issueDetails.setMeasurementId1(prods.get("UNITS_OF_MEASUREMENT")==null ? "" : prods.get("UNITS_OF_MEASUREMENT").toString());
				issueDetails.setQuantity1(Double.valueOf(prods.get("AVAILABLE_QUANTITY")==null ? "" : prods.get("AVAILABLE_QUANTITY").toString()));
				//issueDetails.setTotalAmount1(prods.get("AMOUNT")==null ? "" : prods.get("AMOUNT").toString());
				issueDetails.setStrPriceId(prods.get("PRICE_ID")==null ? "" : prods.get("PRICE_ID").toString());
				issueDetails.setStrHSNCode1(prods.get("HSN_CODE")==null ? "" : prods.get("HSN_CODE").toString());
				issueDetails.setAmountPerUnitBeforeTax(prods.get("AMOUNT_PER_UNIT_BEFORE_TAXES")==null ? "" : prods.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
				issueDetails.setTax(prods.get("TAX")==null ? "0" : prods.get("TAX").toString());
				issueDetails.setOtherOrTransportCharges(prods.get("OTHER_CHARGES")==null ? "0" : prods.get("OTHER_CHARGES").toString());
				issueDetails.setTaxotherOrTransportCharges(prods.get("TAX_ON_OTHER_TRANSPORT_CHG")==null ? "0" : prods.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
				issueDetails.setReceivedQuantity(prods.get("RECEVED_QTY")==null ? "" : prods.get("RECEVED_QTY").toString());
				issueDetails.setAmountAfterTaxotherOrTransportCharges(prods.get("OTHER_CHARGES_AFTER_TAX")==null ? "0" : prods.get("OTHER_CHARGES_AFTER_TAX").toString());
				issueDetails.setExpiryDate(prods.get("EXPIRY_DATE")==null ? "N/A" : prods.get("EXPIRY_DATE").toString());
				
				allDetails.add(issueDetails);
			}

		

		return allDetails;
	}


	public int insertIntremidiatoryTabbleData(int indentEntryDetailsSeqNum, IssueToOtherSiteDto issueDto, String basicAmt, String quantity, String priceId,int indentEntrySeqNum)  {
		int result = 0;	
		AuditLogDetailsBean auditBean = null;
		Double totAmt = Double.parseDouble(basicAmt.replace(",", "").trim());
		Double qty = Double.parseDouble(quantity);
		Double perPiceAmt = totAmt*qty;
		String indentIssueQry = "insert into SUMADHURA_INTERMIDIATARY(INTERMIDIATATY_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MEASUREMENT_ID,QUANTITY,PRICE_LIST_ID,INDENT_ENTRY_DETAILS_ID,FROM_SITE,TO_SITE,CREATED_DATE,INDENT_ENTRY_ID,STATUS) "
			+ "values(SUMADHURA_INTERMIDIATARY_SEQ.nextval,?,?,?,?,?,?,?,?,?,sysdate,?,?)";
		log.debug("Query for indent issue = "+indentIssueQry);

		result = jdbcTemplate.update(indentIssueQry, new Object[]{
				//    issueDto.getIntrmidiatendentEntryId(),
				issueDto.getProdId1(), 
				issueDto.getSubProdId1(),  
				issueDto.getChildProdId1(), 
				issueDto.getMeasurementId1(), 
				issueDto.getQuantity1(), 
				priceId,
				indentEntryDetailsSeqNum ,
				issueDto.getFromSite(),
				issueDto.getToSite(),
				indentEntrySeqNum,"A"

		}
		);

		auditBean = new AuditLogDetailsBean();
		auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
		auditBean.setLoginId(issueDto.getUserId());
		auditBean.setOperationName("Issue to other Site");
		auditBean.setStatus("Info");
		auditBean.setSiteId(issueDto.getFromSite());
		new SaveAuditLogDetails().saveAuditLogDetails(auditBean);
		log.debug("Result = "+result);
		logger.info("IndentIssueDao --> insertIndentIssueData() --> Result = "+result);

		return result;
	}

	//pavan changed method
	public int insertInvoiceData(int indentEntrySeqNum, IssueToOtherSiteDto objIssueToOtherSiteDto ) {

		//IndentReceiveDto objIndentReceiveDto = null;

		int result = 0;
		Calendar cal1 = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");

		String invoiceInsertionQry = "INSERT INTO INDENT_ENTRY (INDENT_ENTRY_ID, USER_ID, SITE_ID, INVOICE_ID,"
			+ " ENTRY_DATE, "
			+ "VENDOR_ID, TOTAL_AMOUNT, INDENT_TYPE, NOTE, RECEIVED_OR_ISSUED_DATE, INVOICE_DATE," +
			" TRANSPORTERNAME,VEHICLENO, STATE)" +
			" VALUES (?, ?, ?,?, sysdate, ?, ?, ?, ?,?,?,?,?,?)";


		log.debug("Query for invoice insertion = "+invoiceInsertionQry);

		result = jdbcTemplate.update(invoiceInsertionQry, new Object[] {indentEntrySeqNum,
				objIssueToOtherSiteDto.getUserId(), objIssueToOtherSiteDto.getFromSite(), 
				objIssueToOtherSiteDto.getInvoiceNo(),
				objIssueToOtherSiteDto.getStrVendorId(), objIssueToOtherSiteDto.getTotalAmount1(), "INO", 
				objIssueToOtherSiteDto.getRemarks1(),
				objIssueToOtherSiteDto.getReceviedDate()+" "+sdf.format(cal1.getTime()), 
				objIssueToOtherSiteDto.getInvoiceDate()+" "+sdf.format(cal1.getTime()),
				objIssueToOtherSiteDto.getTransportInvoice1(),
				objIssueToOtherSiteDto.getVehicleNo(),
				objIssueToOtherSiteDto.getState(),
		});
		log.debug("Result = "+result);			
		logger.info("IndentReceiveDao --> insertInvoiceData() --> Result = "+result);

		return result;
	}


	public int insertIndentReceiveData(int intIndentEntryDetailsSeqNum,int indentEntrySeqNum, IssueToOtherSiteDto irdto, String userId, String siteId,int intSumadhuraPriceList ) {

		int result = 0;	

		//double doubleQuantity = 0.0;
		//double doublePrice = 0.0;
		double doubleBasicAmount = irdto.getQuantity1() * Double.valueOf(irdto.getPrice1()) ;


		AuditLogDetailsBean auditBean = null;
		String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID,"
			+ " PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, RECEVED_QTY,"
			+ " MEASUR_MNT_ID, MEASUR_MNT_NAME, "
			+ "EXPIRY_DATE,ENTRY_DATE,PRICE_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,sysdate,?)";
		log.debug("Query for indent issue = "+indentIssueQry);

		result = jdbcTemplate.update(indentIssueQry, new Object[] { 
				intIndentEntryDetailsSeqNum,
				indentEntrySeqNum, 
				irdto.getProdId1(), 
				irdto.getProdName1(), 
				irdto.getSubProdId1(), 
				irdto.getSubProdName1(), 
				irdto.getChildProdId1(), 
				irdto.getChildProdName1(), 
				irdto.getQuantity1(), 
				irdto.getMeasurementId1(), 
				irdto.getMeasurementName1(), 
				irdto.getExpiryDate1(),
				intSumadhuraPriceList
		}
		);

		auditBean = new AuditLogDetailsBean();
		auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
		auditBean.setLoginId(userId);
		auditBean.setOperationName("New Recive from othe site");
		auditBean.setStatus("Info");
		auditBean.setSiteId(siteId);
		new SaveAuditLogDetails().saveAuditLogDetails(auditBean);
		log.debug("Result = "+result);
		logger.info("IndentIssueDao --> insertIndentReceiveData() --> Result = "+result);

		return result;
	}


	public int updateIndentAvalibilityInwards(IssueToOtherSiteDto irdto, String siteId) {
		List<Map<String, Object>> dbProductDetailsList = null;
		int result=0;
		String availability_Id="";
		double issue_Form_Quantity=0.0;
		double quantity=0.0;

		String query="SELECT PRODUT_QTY,INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND MESURMENT_ID= ? AND SITE_ID=?  ";
		
		dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] {
				irdto.getProdId1(), 
				irdto.getSubProdId1(), 
				irdto.getChildProdId1(), 
				irdto.getMeasurementId1(), 
				siteId
				
		});
	
		if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
			for (Map<String, Object> prods : dbProductDetailsList) {
				quantity = Double.parseDouble(prods.get("PRODUT_QTY") == null ? "" : prods.get("PRODUT_QTY").toString());
				availability_Id = (prods.get("INDENT_AVAILABILITY_ID") == null ? "" : prods.get("INDENT_AVAILABILITY_ID").toString());
			}
		}
			issue_Form_Quantity=irdto.getQuantity1();
			quantity=quantity+issue_Form_Quantity;


		String updateIndentAvalibilityQry = "UPDATE INDENT_AVAILABILITY SET PRODUT_QTY ='"+quantity+"' WHERE INDENT_AVAILABILITY_ID ='"+availability_Id+"'";
		log.debug("Query for update indent avalibility = "+updateIndentAvalibilityQry);

		result = jdbcTemplate.update(updateIndentAvalibilityQry, new Object[] {});
		log.debug("Result = "+result);
		logger.info("IndentReceiveDao --> updateIndentAvalibility() --> Result = "+result);
		return result;
	}



	public String getIndentAvailableId(IssueToOtherSiteDto irdto, String site_id) {

		String query = "SELECT INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID = ? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID = ? AND MESURMENT_ID = ? AND SITE_ID = ? ";
		return String.valueOf(jdbcTemplate.queryForInt(query, new Object[] {
				irdto.getProdId1(), 
				irdto.getSubProdId1(), 
				irdto.getChildProdId1(), 
				irdto.getMeasurementId1(),  
				site_id}));

	}


	@Override
	public String getProductAvailabilitySequenceNumber() {

		String query = "SELECT INDENT_AVAILABILITY_SEQ.NEXTVAL FROM DUAL";
		return String.valueOf(jdbcTemplate.queryForInt(query));
	}



	public void saveReciveDetailsIntoSumduraPriceList(int entryDetailssequenceId,IssueToOtherSiteDto irdto, String invoiceNumber, String siteId, String id,int intSumadhuraPriceListSeqNo) {
		String query = "";
		Double perPiceAmt = 0.0;
		Double totalAmt = 0.0;
		Double quantity = 0.0;
		//String entryDetailssequenceId = getEntryDetailsSequenceNumber();

		double doubleBasicAmount = irdto.getQuantity1() * Double.valueOf(irdto.getPrice1()) ;
		totalAmt = Double.valueOf(irdto.getTotalAmount1());
		quantity = Double.valueOf(irdto.getQuantity1());
		//perPiceAmt = Double.valueOf(totalAmt/quantity);

		if(quantity > 0){
			perPiceAmt = Double.valueOf(totalAmt/quantity);
		}else{
			perPiceAmt = 0.0;
		}

		query = "INSERT INTO SUMADHURA_PRICE_LIST(PRICE_ID, DC_NUMBER, INVOICE_NUMBER, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, INDENT_AVAILABILITY_ID, STATUS, AVAILABLE_QUANTITY, AMOUNT_PER_UNIT_AFTER_TAXES, SITE_ID, UNITS_OF_MEASUREMENT,INDENT_ENTRY_DETAILS_ID,CREATED_DATE,"
			+ " AMOUNT_PER_UNIT_BEFORE_TAXES, BASIC_AMOUNT,TOTAL_AMOUNT,OTHER_CHARGES,TAX_ON_OTHER_TRANSPORT_CHG,OTHER_CHARGES_AFTER_TAX,HSN_CODE,RECEVED_QTY,TYPE_OF_PURCHASE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?, ?, ?,?,?,?)";
		log.debug("Query for save recive details into sumadhura price list table " + query);
		//logger.info("Query for save recive details into sumadhura price list table " + query);
		jdbcTemplate.update(query, new Object[] {intSumadhuraPriceListSeqNo,irdto.getDcNumber(),  invoiceNumber, irdto.getProdId1(), irdto.getSubProdId1(), irdto.getChildProdId1(),id , "A", irdto.getQuantity1(), perPiceAmt, siteId, irdto.getMeasurementId1(),entryDetailssequenceId, irdto.getDate(),
				irdto.getPrice1(), doubleBasicAmount, irdto.getTotalAmount1(), irdto.getOtherOrTransportCharges(),
				irdto.getTaxotherOrTransportCharges(), irdto.getAmountAfterTaxotherOrTransportCharges(),irdto.getStrHSNCode1(),irdto.getQuantity1(),irdto.getTypeOfPurchase()});


	}




	public int getEntryDetailsSequenceNumber() {

		//String query = "SELECT MAX(INDENT_ENTRY_DETAILS_ID) FROM INDENT_ENTRY_DETAILS";
		String query = "SELECT INDENT_ENTRY_DETAILS_SEQ.nextval FROM DUAL";


		return jdbcTemplate.queryForInt(query);
	}



	@Override
	public void saveReceivedDataIntoSumadhuClosingBalByProduct(IssueToOtherSiteDto irdto, String siteId) {

		String query = "";
		Double quantity = 0.0;
		Double totalAmt = 0.0;
		List<Map<String, Object>> dbClosingBalancesList = null;


		Date today = new Date();                   
		@SuppressWarnings("deprecation")
		Date myDate = new Date(irdto.getDate()+" 23:59:59");


		if (today.compareTo(myDate)>0) {

			query = "SELECT CLOSING_BAL_BY_PRODUCT_ID,QUANTITY, TOTAL_AMOUNT FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) = TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
			dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {irdto.getProdId1(), irdto.getSubProdId1(), irdto.getChildProdId1(), siteId,  irdto.getMeasurementId1()});

			if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
				query = "";
				query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT SET QUANTITY= QUANTITY + '"+irdto.getQuantity1()+"', TOTAL_AMOUNT=TOTAL_AMOUNT+'"+irdto.getTotalAmount1().trim()+"'WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) >= TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
				jdbcTemplate.update(query, new Object[] {irdto.getProdId1(), irdto.getSubProdId1(), irdto.getChildProdId1(), siteId,  irdto.getMeasurementId1()});

			} else {
				query = "";
				String dateTime = "";
				query = "SELECT DATE_AND_TIME, CLOSING_BAL_BY_PRODUCT_ID,QUANTITY, TOTAL_AMOUNT FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) <= TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')  AND ROWNUM <= 1 ORDER BY DATE_AND_TIME  DESC";
				dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {irdto.getProdId1(), irdto.getSubProdId1(), irdto.getChildProdId1(), siteId,  irdto.getMeasurementId1()});
				if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
					for (Map<String, Object> prods : dbClosingBalancesList) {
						//dateTime = prods.get("DATE_AND_TIME") == null ? "" : prods.get("DATE_AND_TIME").toString();
						quantity = Double.parseDouble(prods.get("QUANTITY") == null ? "" : prods.get("QUANTITY").toString());
						totalAmt = Double.parseDouble(prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString());
					}
				}

				query = "";
				query = "INSERT INTO SUMADHU_CLOSING_BAL_BY_PRODUCT(CLOSING_BAL_BY_PRODUCT_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,QUANTITY,SITE_ID,TOTAL_AMOUNT,DATE_AND_TIME,MEASUREMENT_ID)VALUES(SUMA_CLOSING_BAL_BY_PRODUCT.NEXTVAL,?,?,?,?,?,?,?,?)";
				jdbcTemplate.update(query,new Object[] {irdto.getProdId1(),irdto.getSubProdId1(), irdto.getChildProdId1(),quantity + irdto.getQuantity1(),siteId,Double.parseDouble(irdto.getTotalAmount1())+totalAmt,irdto.getDate(), irdto.getMeasurementId1()});
				query = "";

				query = "SELECT TO_CHAR(TRUNC(DATE_AND_TIME)) AS DATE_AND_TIME FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE TRUNC(DATE_AND_TIME) > TO_DATE('"+irdto.getDate()+"','dd-MM-yy') AND PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND ROWNUM <= 1";
				dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {irdto.getProdId1(), irdto.getSubProdId1(), irdto.getChildProdId1(), siteId,  irdto.getMeasurementId1()});

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
							jdbcTemplate.update(query,new Object[] {irdto.getProdId1(),irdto.getSubProdId1(), irdto.getChildProdId1(),quantity, siteId,totalAmt,yourDate1, irdto.getMeasurementId1()});

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
						jdbcTemplate.update(query,new Object[] {irdto.getProdId1(),irdto.getSubProdId1(), irdto.getChildProdId1(),quantity ,siteId,totalAmt,yourDate1, irdto.getMeasurementId1()});
					}
				}
				query="";
				query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT SET QUANTITY= QUANTITY + '"+irdto.getQuantity1()+"', TOTAL_AMOUNT=TOTAL_AMOUNT+'"+irdto.getTotalAmount1().trim()+"' WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) > TO_DATE('"+irdto.getDate()+"', 'dd-MM-yy')";
				jdbcTemplate.update(query, new Object[] {irdto.getProdId1(), irdto.getSubProdId1(), irdto.getChildProdId1(), siteId,  irdto.getMeasurementId1()});
			}
		}



	}



	public int updateIntermidiatoryTabledata(String strIntermidiatoryIndentEntryId) {


		//IndentReceiveDto objIndentReceiveDto = null;

		int result = 0;


		String invoiceInsertionQry = "update SUMADHURA_INTERMIDIATARY set STATUS = 'I' where INDENT_ENTRY_ID = ?";
		log.debug("Query for invoice insertion = "+invoiceInsertionQry);

		result = jdbcTemplate.update(invoiceInsertionQry, new Object[] {strIntermidiatoryIndentEntryId });





		log.debug("Result = "+result);			
		logger.info("IndentReceiveDao --> insertInvoiceData() --> Result = "+result);

		return result;

	}



	@Override
	public void updateInvoiceOtherCharges(IssueToOtherSiteDto objIssueToOtherSiteDto, String strSiteId,
			int strindentEntryId) {
		String query = "insert into SUMADHURA_TRNS_OTHR_CHRGS_DTLS(ID,TRANSPORT_ID,TRANSPORT_AMOUNT,TRANSPORT_GST_PERCENTAGE," +
		"TOTAL_AMOUNT_AFTER_GST_TAX,TRANSPORT_INVOICE_ID,INDENT_ENTRY_INVOICE_ID," +
		"DATE_AND_TIME,SITE_ID,INDENT_ENTRY_ID) " +
		" values(TRANS_SEQ_ID.nextval,?,?,?,?,?,?,sysdate,?,?)";
		jdbcTemplate.update(query, new Object[] { objIssueToOtherSiteDto.getConveyance1(),
				objIssueToOtherSiteDto.getConveyanceAmount1(),objIssueToOtherSiteDto.getGSTTax1(),objIssueToOtherSiteDto.getGSTAmount1(),
				objIssueToOtherSiteDto.getAmountAfterTax1(),objIssueToOtherSiteDto.getTransportInvoice1(),strSiteId,strindentEntryId });


	}
	public List<IssueToOtherSiteInwardBean> getGetProductDetailsLists(HttpSession session,String requestId, String siteId,String strIssueType) {
		List<Map<String, Object>> productList = null;
		List<IssueToOtherSiteInwardBean> IssueToOtherSiteInward  = new ArrayList<IssueToOtherSiteInwardBean>();
		IssueToOtherSiteInwardBean objIssueToOtherSiteInwardBean= null;
		JdbcTemplate template = null;
		StringBuilder sql = new StringBuilder();
		double doubleFinalAmount = 0.0;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(requestId) ) {

				/*	sql.append("select IED.PRODUCT_NAME,IED.PRODUCT_ID,IED.SUB_PRODUCT_ID,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_ID,IED.CHILD_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.MEASUR_MNT_ID,SP.AMOUNT,IED.BASIC_AMOUNT, ");
			sql.append("	IED.TAX,IED.HSN_CODE,IED.TAX_AMOUNT,IED.AMOUNT_AFTER_TAX,IED.OTHER_CHARGES,IED.TAX_ON_OTHER_TRANSPORT_CHG,IED.TOTAL_AMOUNT,");
			sql.append("IED.EXPIRY_DATE,IE.ISSUE_TYPE,SI.QUANTITY , SI.INDENT_ENTRY_ID FROM  INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE,SUMADHURA_PRICE_LIST SP,SUMADHURA_INTERMIDIATARY SI ");
			sql.append("WHERE IED.INDENT_ENTRY_DETAILS_ID in ");
			sql.append("(SELECT SP1.INDENT_ENTRY_DETAILS_ID FROM SUMADHURA_INTERMIDIATARY SI1,SUMADHURA_PRICE_LIST SP1 ");
			sql.append("WHERE SI1.PRICE_LIST_ID=SP1.PRICE_ID AND SI1.INDENT_ENTRY_ID=? ");
			sql.append("and SI1.TO_SITE = ? ) ");
			sql.append("AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID ");
			sql.append("and IED.INDENT_ENTRY_DETAILS_ID = SP.INDENT_ENTRY_DETAILS_ID ");
			sql.append("and SP.PRICE_ID = SI.PRICE_LIST_ID and  SI.INDENT_ENTRY_ID=? and SI.status='A'");
				 */


				/*	sql.append("select distinct(IED.INDENT_ENTRY_DETAILS_ID),IED.PRODUCT_NAME,IED.PRODUCT_ID,IED.SUB_PRODUCT_ID,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_ID,IED.CHILD_PRODUCT_NAME,");
			sql.append("IED.MEASUR_MNT_NAME,IED.MEASUR_MNT_ID,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,SPL.BASIC_AMOUNT, 	SPL.TAX,SPL.HSN_CODE,SPL.TAX_AMOUNT,SPL.AMOUNT_AFTER_TAX,");
			sql.append(" SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG,SPL.TOTAL_AMOUNT,IED.EXPIRY_DATE,IE.ISSUE_TYPE,SI.QUANTITY , ");
			sql.append(" SI.INDENT_ENTRY_ID,SPL.HSN_CODE ,SPL.DC_NUMBER FROM  INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE,SUMADHURA_PRICE_LIST SPL,SUMADHURA_INTERMIDIATARY SI ");
			sql.append(" WHERE  IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID and IED.PRICE_ID = SPL.PRICE_ID and ");
			sql.append("   IE.INDENT_ENTRY_ID = SI.INDENT_ENTRY_ID  and  IE.INDENT_ENTRY_ID=?  and SI.TO_SITE = ?  and SI.status= ?");
				 */	


				sql.append("select IED.PRODUCT_NAME,IED.PRODUCT_ID,IED.SUB_PRODUCT_ID,IED.SUB_PRODUCT_NAME,CP.CHILD_PRODUCT_ID,(CP.NAME) AS CHILD_PRODUCT_NAME,CP.MATERIAL_GROUP_ID,");
				sql.append("IED.MEASUR_MNT_NAME,IED.MEASUR_MNT_ID,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,SPL.BASIC_AMOUNT,SPL.TAX,SPL.HSN_CODE,SPL.TAX_AMOUNT,SPL.AMOUNT_AFTER_TAX,");
				sql.append(" SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TOTAL_AMOUNT,IED.EXPIRY_DATE,IE.ISSUE_TYPE,SI.QUANTITY , ");
				sql.append(" SI.INDENT_ENTRY_ID,SPL.HSN_CODE ,SPL.DC_NUMBER,to_char(SI.CREATED_DATE,'hh24:mi:ss') as ISSUED_TIME  FROM  INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE,SUMADHURA_PRICE_LIST SPL,");
				sql.append("SUMADHURA_INTERMIDIATARY SI,CHILD_PRODUCT CP  WHERE  IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID ");
				sql.append("and  IED.INDENT_ENTRY_ID=SI.INDENT_ENTRY_ID and	IED.PRICE_ID = SPL.PRICE_ID	and	SI.PRICE_LIST_ID = SPL.PRICE_ID ");
				sql.append("and  IE.INDENT_ENTRY_ID = SI.INDENT_ENTRY_ID  and SI.INDENT_ENTRY_ID= ? and SI.TO_SITE = ?	and SI.status= ? ");
				productList = template.queryForList(sql.toString(), new Object[] {requestId, siteId,"A"});

			} 
			if (null != productList && productList.size() > 0) {

				int i=1;
				double doubleTotalAmount = 0.0;
				double doubleQuantity = 0.0;
				double doublePrice = 0.0 ;
				String groupId="";
				String availablequantity="";
				String boqQuantity="";
				
				for (Map<?, ?> IssueToOtherSiteInwardBean : productList) {
					if(strIssueType.equals("Returnable")){
						objIssueToOtherSiteInwardBean = new IssueToOtherSiteInwardBean();
						doubleQuantity = Double.valueOf(IssueToOtherSiteInwardBean.get("QUANTITY") == null ? "" : IssueToOtherSiteInwardBean.get("QUANTITY").toString());
						doublePrice = Double.valueOf(IssueToOtherSiteInwardBean.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "" : IssueToOtherSiteInwardBean.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString());
						doubleTotalAmount = doubleQuantity*doublePrice;
						doubleFinalAmount = doubleFinalAmount+doubleTotalAmount;

						objIssueToOtherSiteInwardBean.setDcNumber(IssueToOtherSiteInwardBean.get("DC_NUMBER") == null ? "" : IssueToOtherSiteInwardBean.get("DC_NUMBER").toString());
						objIssueToOtherSiteInwardBean.setProductId(IssueToOtherSiteInwardBean.get("PRODUCT_ID") == null ? "" : IssueToOtherSiteInwardBean.get("PRODUCT_ID").toString());
						objIssueToOtherSiteInwardBean.setSubProductId(IssueToOtherSiteInwardBean.get("SUB_PRODUCT_ID") == null ? "" : IssueToOtherSiteInwardBean.get("SUB_PRODUCT_ID").toString());
						objIssueToOtherSiteInwardBean.setChildProductId(IssueToOtherSiteInwardBean.get("CHILD_PRODUCT_ID") == null ? "" : IssueToOtherSiteInwardBean.get("CHILD_PRODUCT_ID").toString());
						objIssueToOtherSiteInwardBean.setMeasurmentId(IssueToOtherSiteInwardBean.get("MEASUR_MNT_ID") == null ? "" : IssueToOtherSiteInwardBean.get("MEASUR_MNT_ID").toString());


						objIssueToOtherSiteInwardBean.setIndentEntryDetailsId(IssueToOtherSiteInwardBean.get("INDENT_ENTRY_DETAILS_ID") == null ? "" : IssueToOtherSiteInwardBean.get("INDENT_ENTRY_DETAILS_ID").toString());
						objIssueToOtherSiteInwardBean.setProduct(IssueToOtherSiteInwardBean.get("PRODUCT_NAME") == null ? "" : IssueToOtherSiteInwardBean.get("PRODUCT_NAME").toString());
						objIssueToOtherSiteInwardBean.setSubProduct(IssueToOtherSiteInwardBean.get("SUB_PRODUCT_NAME") == null ? "": IssueToOtherSiteInwardBean.get("SUB_PRODUCT_NAME").toString());
						objIssueToOtherSiteInwardBean.setChildProduct(IssueToOtherSiteInwardBean.get("CHILD_PRODUCT_NAME") == null ? "" : IssueToOtherSiteInwardBean.get("CHILD_PRODUCT_NAME").toString());
						objIssueToOtherSiteInwardBean.setUnitsOfMeasurement(IssueToOtherSiteInwardBean.get("MEASUR_MNT_NAME") == null ? "" : IssueToOtherSiteInwardBean.get("MEASUR_MNT_NAME").toString());
						objIssueToOtherSiteInwardBean.setQuantity(IssueToOtherSiteInwardBean.get("QUANTITY") == null ? "" : IssueToOtherSiteInwardBean.get("QUANTITY").toString());
						objIssueToOtherSiteInwardBean.setHsnCode(IssueToOtherSiteInwardBean.get("HSN_CODE") == null ? "" : IssueToOtherSiteInwardBean.get("HSN_CODE").toString());
						objIssueToOtherSiteInwardBean.setPrice(IssueToOtherSiteInwardBean.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "" : IssueToOtherSiteInwardBean.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString());
						//objIssueToOtherSiteInwardBean.setTotalAmount(IssueToOtherSiteInwardBean.get("ISSUE_TYPE") == null ? "" : IssueToOtherSiteInwardBean.get("ISSUE_TYPE").toString());
						objIssueToOtherSiteInwardBean.setTotalAmount(String.valueOf(doubleTotalAmount));
						objIssueToOtherSiteInwardBean.setBasicAmount(IssueToOtherSiteInwardBean.get("BASIC_AMOUNT") == null ? "" : IssueToOtherSiteInwardBean.get("BASIC_AMOUNT").toString());
						objIssueToOtherSiteInwardBean.setTax(IssueToOtherSiteInwardBean.get("TAX") == null ? "" : IssueToOtherSiteInwardBean.get("TAX").toString());
						objIssueToOtherSiteInwardBean.setTaxAmount(IssueToOtherSiteInwardBean.get("TAX_AMOUNT") == null ? "" : IssueToOtherSiteInwardBean.get("TAX_AMOUNT").toString());
						objIssueToOtherSiteInwardBean.setAmountAfterTax(IssueToOtherSiteInwardBean.get("AMOUNT_AFTER_TAX") == null ? "" : IssueToOtherSiteInwardBean.get("AMOUNT_AFTER_TAX").toString());
						objIssueToOtherSiteInwardBean.setOtherOrTransportCharges(IssueToOtherSiteInwardBean.get("OTHER_CHARGES") == null ? "" : IssueToOtherSiteInwardBean.get("OTHER_CHARGES").toString());
						objIssueToOtherSiteInwardBean.setTaxOnOtherOrTransportCharges(IssueToOtherSiteInwardBean.get("TAX_ON_OTHER_TRANSPORT_CHG") == null ? "" : IssueToOtherSiteInwardBean.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
						objIssueToOtherSiteInwardBean.setOtherOrTransportChargesAfterTax(IssueToOtherSiteInwardBean.get("OTHER_CHARGES_AFTER_TAX") == null ? "" : IssueToOtherSiteInwardBean.get("OTHER_CHARGES_AFTER_TAX").toString());
						objIssueToOtherSiteInwardBean.setExpireDate(IssueToOtherSiteInwardBean.get("EXPIRY_DATE") == null ? "" : IssueToOtherSiteInwardBean.get("EXPIRY_DATE").toString());
						objIssueToOtherSiteInwardBean.setIntrmidiatendentEntryId(IssueToOtherSiteInwardBean.get("INDENT_ENTRY_ID") == null ? "" : IssueToOtherSiteInwardBean.get("INDENT_ENTRY_ID").toString());
						objIssueToOtherSiteInwardBean.setIssuedTime(IssueToOtherSiteInwardBean.get("ISSUED_TIME") == null ? "-" : IssueToOtherSiteInwardBean.get("ISSUED_TIME").toString());
		groupId=IssueToOtherSiteInwardBean.get("MATERIAL_GROUP_ID") == null ? "0" : IssueToOtherSiteInwardBean.get("MATERIAL_GROUP_ID").toString();
						if(groupId!=null && !groupId.equals("0") && !groupId.equals("")){
							String data=ird.gettingReqBoqQuantityAjax(groupId,siteId);
							availablequantity=data.split("_")[0];
							boqQuantity=data.split("_")[1];
						}
						objIssueToOtherSiteInwardBean.setAvailbleQuantity(availablequantity);
						objIssueToOtherSiteInwardBean.setBoqQuantity(boqQuantity);
						
						objIssueToOtherSiteInwardBean.setGroupId1(groupId);

						objIssueToOtherSiteInwardBean.setStrSerialNo(i);


						IssueToOtherSiteInward.add(objIssueToOtherSiteInwardBean);

						i++;
					}
					else{
						objIssueToOtherSiteInwardBean = new IssueToOtherSiteInwardBean();
						doubleQuantity = Double.valueOf(IssueToOtherSiteInwardBean.get("QUANTITY") == null ? "" : IssueToOtherSiteInwardBean.get("QUANTITY").toString());
						doublePrice = Double.valueOf(IssueToOtherSiteInwardBean.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "" : IssueToOtherSiteInwardBean.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString());
						doubleTotalAmount = doubleQuantity*doublePrice;
						doubleFinalAmount = doubleFinalAmount+doubleTotalAmount;

						objIssueToOtherSiteInwardBean.setDcNumber(IssueToOtherSiteInwardBean.get("DC_NUMBER") == null ? "" : IssueToOtherSiteInwardBean.get("DC_NUMBER").toString());
						objIssueToOtherSiteInwardBean.setProductId(IssueToOtherSiteInwardBean.get("PRODUCT_ID") == null ? "" : IssueToOtherSiteInwardBean.get("PRODUCT_ID").toString());
						objIssueToOtherSiteInwardBean.setSubProductId(IssueToOtherSiteInwardBean.get("SUB_PRODUCT_ID") == null ? "" : IssueToOtherSiteInwardBean.get("SUB_PRODUCT_ID").toString());
						objIssueToOtherSiteInwardBean.setChildProductId(IssueToOtherSiteInwardBean.get("CHILD_PRODUCT_ID") == null ? "" : IssueToOtherSiteInwardBean.get("CHILD_PRODUCT_ID").toString());
						objIssueToOtherSiteInwardBean.setMeasurmentId(IssueToOtherSiteInwardBean.get("MEASUR_MNT_ID") == null ? "" : IssueToOtherSiteInwardBean.get("MEASUR_MNT_ID").toString());
						objIssueToOtherSiteInwardBean.setIndentEntryDetailsId(IssueToOtherSiteInwardBean.get("INDENT_ENTRY_DETAILS_ID") == null ? "" : IssueToOtherSiteInwardBean.get("INDENT_ENTRY_DETAILS_ID").toString());
						objIssueToOtherSiteInwardBean.setProduct(IssueToOtherSiteInwardBean.get("PRODUCT_NAME") == null ? "" : IssueToOtherSiteInwardBean.get("PRODUCT_NAME").toString());
						objIssueToOtherSiteInwardBean.setSubProduct(IssueToOtherSiteInwardBean.get("SUB_PRODUCT_NAME") == null ? "": IssueToOtherSiteInwardBean.get("SUB_PRODUCT_NAME").toString());
						objIssueToOtherSiteInwardBean.setChildProduct(IssueToOtherSiteInwardBean.get("CHILD_PRODUCT_NAME") == null ? "" : IssueToOtherSiteInwardBean.get("CHILD_PRODUCT_NAME").toString());
						objIssueToOtherSiteInwardBean.setUnitsOfMeasurement(IssueToOtherSiteInwardBean.get("MEASUR_MNT_NAME") == null ? "" : IssueToOtherSiteInwardBean.get("MEASUR_MNT_NAME").toString());

						double Quantity = Double.valueOf(IssueToOtherSiteInwardBean.get("QUANTITY") == null ? "" : IssueToOtherSiteInwardBean.get("QUANTITY").toString() );
						double price = Double.valueOf(IssueToOtherSiteInwardBean.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "" : IssueToOtherSiteInwardBean.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString() );
						double basicAmount = Double.valueOf(price*Quantity);
						objIssueToOtherSiteInwardBean.setQuantity(String.valueOf(Quantity));
						objIssueToOtherSiteInwardBean.setHsnCode(IssueToOtherSiteInwardBean.get("HSN_CODE") == null ? "" : IssueToOtherSiteInwardBean.get("HSN_CODE").toString());
						objIssueToOtherSiteInwardBean.setPrice(String.valueOf(price));
						objIssueToOtherSiteInwardBean.setTotalAmount(String.valueOf(doubleTotalAmount));

						objIssueToOtherSiteInwardBean.setBasicAmount(String.valueOf(basicAmount));
						//objIssueToOtherSiteInwardBean.setBasicAmount(IssueToOtherSiteInwardBean.get("BASIC_AMOUNT") == null ? "" : IssueToOtherSiteInwardBean.get("BASIC_AMOUNT").toString());
						objIssueToOtherSiteInwardBean.setOtherOrTransportChargesAfterTax(IssueToOtherSiteInwardBean.get("OTHER_CHARGES_AFTER_TAX") == null ? "" : IssueToOtherSiteInwardBean.get("OTHER_CHARGES_AFTER_TAX").toString());
						objIssueToOtherSiteInwardBean.setExpireDate(IssueToOtherSiteInwardBean.get("EXPIRY_DATE") == null ? "" : IssueToOtherSiteInwardBean.get("EXPIRY_DATE").toString());
						objIssueToOtherSiteInwardBean.setIntrmidiatendentEntryId(IssueToOtherSiteInwardBean.get("INDENT_ENTRY_ID") == null ? "" : IssueToOtherSiteInwardBean.get("INDENT_ENTRY_ID").toString());
						objIssueToOtherSiteInwardBean.setOtherOrTransportCharges("0");
						objIssueToOtherSiteInwardBean.setTaxOnOtherOrTransportCharges("0");
						objIssueToOtherSiteInwardBean.setOtherOrTransportChargesAfterTax("0");
							objIssueToOtherSiteInwardBean.setIssuedTime(IssueToOtherSiteInwardBean.get("ISSUED_TIME") == null ? "-" : IssueToOtherSiteInwardBean.get("ISSUED_TIME").toString());

groupId=IssueToOtherSiteInwardBean.get("MATERIAL_GROUP_ID") == null ? "0" : IssueToOtherSiteInwardBean.get("MATERIAL_GROUP_ID").toString();
						if(groupId!=null && !groupId.equals("0") && !groupId.equals("")){
							String data=ird.gettingReqBoqQuantityAjax(groupId,siteId);
							availablequantity=data.split("_")[0];
							boqQuantity=data.split("_")[1];
						}
						objIssueToOtherSiteInwardBean.setAvailbleQuantity(availablequantity);
						objIssueToOtherSiteInwardBean.setBoqQuantity(boqQuantity);
						objIssueToOtherSiteInwardBean.setGroupId1(groupId);

						objIssueToOtherSiteInwardBean.setStrSerialNo(i);
						IssueToOtherSiteInward.add(objIssueToOtherSiteInwardBean);

						i++;
					}
				}
			}

			session.setAttribute("doubleFinalAmount", doubleFinalAmount);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return IssueToOtherSiteInward;
	}

	@Override
	public List<IssueToOtherSiteInwardBean> getTransportChargesList(String requestId, String siteId){
		List<Map<String, Object>> transportList = null;

		List<IssueToOtherSiteInwardBean> GetTransportChargesListDetails = new ArrayList<IssueToOtherSiteInwardBean>();
		IssueToOtherSiteInwardBean objGetTransportChargesDetails=null;
		JdbcTemplate template = null;
		String sql = "";

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(requestId) ) {
				sql = "SELECT * FROM SUMADHURA_TRNS_OTHR_CHRGS_DTLS WHERE SITE_ID=? AND INDENT_ENTRY_INVOICE_ID=?";
				transportList = template.queryForList(sql, new Object[] {requestId, siteId});
			} 
			if (null != transportList && transportList.size() > 0) {
				for (Map<?, ?> GetTransportChargesDetails : transportList) {

					objGetTransportChargesDetails = new IssueToOtherSiteInwardBean();

					objGetTransportChargesDetails.setTransportId(GetTransportChargesDetails.get("TRANSPORT_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_ID").toString());
					objGetTransportChargesDetails.setConveyance1(GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE").toString());
					objGetTransportChargesDetails.setConveyanceAmount1(GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString());
					objGetTransportChargesDetails.setGSTTax1(GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString());
					objGetTransportChargesDetails.setGSTAmount1(GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID").toString());
					objGetTransportChargesDetails.setAmountAfterTax1(GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString());


					GetTransportChargesListDetails.add(objGetTransportChargesDetails);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return GetTransportChargesListDetails;


	}

	@Override
	public List<IssueToOtherSiteInwardBean> getIssueLists(HttpSession session,String invoiceNumber, String siteId, String strIssueType1) {

		List<Map<String, Object>> productList = null;

		List<IssueToOtherSiteInwardBean> listGetInvoiceDetailsInward = new ArrayList<IssueToOtherSiteInwardBean>();
		IssueToOtherSiteInwardBean objGetInvoiceDetailsInward=null;
		JdbcTemplate template = null;
		StringBuilder sql = new StringBuilder();

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(invoiceNumber) ) {
				sql.append("select distinct(IE.INDENT_ENTRY_ID),IE.ISSUE_TYPE, IE.ENTRY_DATE,IE.STATE, IE.INVOICE_DATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,SI.INDENT_ENTRY_ID,IE.VENDOR_ID as VENDOR_ID,IE.INDENT_CREATION_ID as Indent_Number,SIC.SITEWISE_INDENT_NO,SI.FROM_SITE,S.SITE_NAME ");
				sql.append("FROM  INDENT_ENTRY IE ");
				sql.append("left outer join SUMADHURA_INDENT_CREATION SIC on SIC.INDENT_CREATION_ID=IE.INDENT_CREATION_ID ");
				sql.append(",SUMADHURA_INTERMIDIATARY SI left join SITE s on s.SITE_ID=SI.FROM_SITE,");
				sql.append("VENDOR_DETAILS VD  ");
				sql.append("WHERE SI.INDENT_ENTRY_ID = IE.INDENT_ENTRY_ID ");
				sql.append("and VD.VENDOR_ID = IE.VENDOR_ID ");
				sql.append("and ");
				
				sql.append("SI.INDENT_ENTRY_ID=? and SI.status=? and SI.TO_SITE = ?");

				productList = template.queryForList(sql.toString(), new Object[] {invoiceNumber,"A", siteId});
			}else if(strIssueType1.equals("loadAllData")){
				sql.append("select distinct(IE.INDENT_ENTRY_ID),IE.ISSUE_TYPE, IE.ENTRY_DATE,IE.STATE, IE.INVOICE_DATE,VD.VENDOR_NAME,VD.ADDRESS,VD.GSIN_NUMBER,SI.INDENT_ENTRY_ID,IE.VENDOR_ID as VENDOR_ID,IE.INDENT_CREATION_ID as Indent_Number,SIC.SITEWISE_INDENT_NO,SI.FROM_SITE,S.SITE_NAME, to_char(SI.CREATED_DATE,'dd-mm-yyyy') as ISSUED_TIME  ");
				sql.append("FROM  INDENT_ENTRY IE ");
				sql.append("left outer join SUMADHURA_INDENT_CREATION SIC on SIC.INDENT_CREATION_ID=IE.INDENT_CREATION_ID ");
				sql.append(",SUMADHURA_INTERMIDIATARY SI left join SITE s on s.SITE_ID=SI.FROM_SITE,");
				sql.append("VENDOR_DETAILS VD  ");
				sql.append("WHERE SI.INDENT_ENTRY_ID = IE.INDENT_ENTRY_ID ");
				sql.append("and VD.VENDOR_ID = IE.VENDOR_ID ");
				sql.append("and ");
				
				sql.append("  SI.status=? and SI.TO_SITE = ?");

				productList = template.queryForList(sql.toString(), new Object[] {"A", siteId});
			}
			
			
			if (null != productList && productList.size() > 0) {

				String strIssueType = "";
				String strInvoiceDate = "";
				for (Map<?, ?> IssueToOtherSiteInwardBean : productList) {

					strIssueType = IssueToOtherSiteInwardBean.get("ISSUE_TYPE") == null ? "": IssueToOtherSiteInwardBean.get("ISSUE_TYPE").toString();

					objGetInvoiceDetailsInward = new IssueToOtherSiteInwardBean();
					objGetInvoiceDetailsInward.setState(IssueToOtherSiteInwardBean.get("STATE") == null ? "" : IssueToOtherSiteInwardBean.get("STATE").toString());
					objGetInvoiceDetailsInward.setInvoiceNumber(invoiceNumber);

					strInvoiceDate = DateUtil.dateConversionForIssueToOtherSite(IssueToOtherSiteInwardBean.get("ENTRY_DATE") == null ? "": IssueToOtherSiteInwardBean.get("ENTRY_DATE").toString());
					objGetInvoiceDetailsInward.setInvoiceDate(strInvoiceDate);
					/*objGetInvoiceDetailsInward.setSiteName("");
					objGetInvoiceDetailsInward.setSiteName("");*/
					objGetInvoiceDetailsInward.setFromSiteId(IssueToOtherSiteInwardBean.get("FROM_SITE") == null ? "" : IssueToOtherSiteInwardBean.get("FROM_SITE").toString());
					objGetInvoiceDetailsInward.setFromSite(IssueToOtherSiteInwardBean.get("SITE_NAME") == null ? "" : IssueToOtherSiteInwardBean.get("SITE_NAME").toString());
					
					//ISSUED_TIME
					objGetInvoiceDetailsInward.setIssuedTime(IssueToOtherSiteInwardBean.get("ISSUED_TIME") == null ? "" : IssueToOtherSiteInwardBean.get("ISSUED_TIME").toString());
					//objGetInvoiceDetailsInward.setFromSite(IssueToOtherSiteInwardBean.get("FROM_SITE") == null ? "" : IssueToOtherSiteInwardBean.get("FROM_SITE").toString());
					
					objGetInvoiceDetailsInward.setVendorName(IssueToOtherSiteInwardBean.get("VENDOR_NAME") == null ? "" : IssueToOtherSiteInwardBean.get("VENDOR_NAME").toString());
					objGetInvoiceDetailsInward.setGsinNo(IssueToOtherSiteInwardBean.get("GSIN_NUMBER") == null ? "" : IssueToOtherSiteInwardBean.get("GSIN_NUMBER").toString());
					objGetInvoiceDetailsInward.setVendorAdress(IssueToOtherSiteInwardBean.get("ADDRESS") == null ? "" : IssueToOtherSiteInwardBean.get("ADDRESS").toString());
					objGetInvoiceDetailsInward.setReceivedDate(IssueToOtherSiteInwardBean.get("RECEVED_DATE") == null ? "" : IssueToOtherSiteInwardBean.get("RECEVED_DATE").toString());
					objGetInvoiceDetailsInward.setIntrmidiatetEntryId(IssueToOtherSiteInwardBean.get("INDENT_ENTRY_ID") == null ? "" : IssueToOtherSiteInwardBean.get("INDENT_ENTRY_ID").toString());
					objGetInvoiceDetailsInward.setVendorId(IssueToOtherSiteInwardBean.get("VENDOR_ID") == null ? "" : IssueToOtherSiteInwardBean.get("VENDOR_ID").toString());
					objGetInvoiceDetailsInward.setIndentNumber(Integer.parseInt(IssueToOtherSiteInwardBean.get("Indent_Number") == null ? "0" : IssueToOtherSiteInwardBean.get("Indent_Number").toString()));
					objGetInvoiceDetailsInward.setSiteWiseIndentNo(Integer.parseInt(IssueToOtherSiteInwardBean.get("SITEWISE_INDENT_NO") == null ? "0" : IssueToOtherSiteInwardBean.get("SITEWISE_INDENT_NO").toString()));




					listGetInvoiceDetailsInward.add(objGetInvoiceDetailsInward);
					//i++;
				}




				session.setAttribute("IssueType", strIssueType);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return listGetInvoiceDetailsInward;
	}



	public void updateInvoiceOtherCharges(IssueToOtherSiteDto objIssueToOtherSiteDto, String strSiteId, String strindentEntryId) {
		String query = "insert into SUMADHURA_TRNS_OTHR_CHRGS_DTLS(ID,TRANSPORT_ID,TRANSPORT_AMOUNT,TRANSPORT_GST_PERCENTAGE," +
		"TOTAL_AMOUNT_AFTER_GST_TAX,TRANSPORT_INVOICE_ID,INDENT_ENTRY_INVOICE_ID," +
		"DATE_AND_TIME,SITE_ID,INDENT_ENTRY_ID) " +
		" values(TRANS_SEQ_ID.nextval,?,?,?,?,?,?,sysdate,?,?)";
		jdbcTemplate.update(query, new Object[] { objIssueToOtherSiteDto.getConveyance1(),
				objIssueToOtherSiteDto.getConveyanceAmount1(),objIssueToOtherSiteDto.getGSTTax1(),objIssueToOtherSiteDto.getGSTAmount1(),
				objIssueToOtherSiteDto.getAmountAfterTax1(),objIssueToOtherSiteDto.getTransportInvoice1(),strSiteId,strindentEntryId });


	}

	public List<Map<String, Object>> getSiteDetails(String siteId){
		List<Map<String, Object>>  dbClosingBalancesList = null;
		String query = "select VENDOR_NAME,ADDRESS,GSIN_NUMBER,STATE from VENDOR_DETAILS where VENDOR_ID = ?";
		dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] {siteId});


		return dbClosingBalancesList;
	}
	@SuppressWarnings("unchecked")
	public static void main(String [] args) throws ParseException{


		String strSiteOneState = "";
		List<Map<String, Object>> getStateDetails = new ArrayList<Map<String, Object>>();
		Map map1 = new HashMap();
		map1.put("State","karnataka");
		Map map2 = new HashMap();
		map2.put("State","karnataka");
		getStateDetails.add(map1);
		getStateDetails.add(map2);

		strSiteOneState = getStateDetails.get(0).get("State").toString();

		//	logger.info(date);
	}
	
	
	@Override
	public String getVendorOrContractorState(String fromSite, String contractorId, String moduleName, String vendorId, String indentType){
		List<Map<String, Object>> getStateDetails = null;
		String strState = " ";
		String strSiteTwoState ="";
		String strSiteOneState = "";
		String query = "select  STATE from VENDOR_DETAILS where VENDOR_ID =? and rownum=1";
		try{
			//we need to load the state of site then we need to compare the state
			strSiteOneState = jdbcTemplate.queryForObject(query,new Object[]{fromSite}, String.class);
		}catch(Exception e){
			log.info("Got Exception while checking indent issueing Local or Non Local "+e.getMessage());
		}
		 query="select STATE_NAME  from SUMADHURA_CONTRACTOR where CONTRACTOR_ID=?";
		try{
			strSiteTwoState = jdbcTemplate.queryForObject(query,new Object[]{contractorId}, String.class);
		}catch(Exception e){
			log.info("Got Exception while checking indent issueing Local or Non Local "+e.getMessage());			
		}
		
		//comparing the state if both state are equal it means this is the local transaction
		//so based on the local or non-local we need to calculate IGST or CGST
		//if indent type is OUTR then we need to compare contractor state and site state
		//if not then we need to compare vendor state and site state for getting local or non-local
		if(indentType.equals("OUTR")){
			if(strSiteOneState.equals(strSiteTwoState) ){
				strState = "Local";
			}else{
				strState = "NonLocal";
			}
		}else if(indentType.equals("OUTS")||indentType.equals("OUTT")){
			//if indent type is Scrap or loss-theft then we need to load the state of the vendor
			query = "select STATE from VENDOR_DETAILS where VENDOR_ID =? and rownum=1";
			try{
				strSiteTwoState = jdbcTemplate.queryForObject(query,new Object[]{vendorId}, String.class);
			}catch(Exception e){
				log.info("Got Exception while checking indent issueing Local or Non Local "+e.getMessage());
			}
			if(strSiteOneState.equals(strSiteTwoState) ){
				strState = "Local";
			}else{
				strState = "NonLocal";
			}
		}
		
		return strState;
	}
	@Override
	public List<Map<String, Object>> getIndentCreatedEmpName(String string, String indentNumber) {
		String query="SELECT SED.EMP_EMAIL,SED.DEPT_ID,SED.MOBILE_NUMBER,SED.USER_PROFILE,SED.EMP_NAME,SICAD.PURPOSE from  SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED "
				+ " where SICAD.INDENT_CREATION_ID = ? AND SED.EMP_ID=SICAD.INDENT_CREATE_APPROVE_EMP_ID";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query,indentNumber);
		
		return list;
	}
	@Override
	public String loadIndentRequestedQuantity(IssueToOtherSiteDto objIssueToOtherSiteDto, String invoiceNumber) {
	String qty="";
	String query="Select  SI.INDENT_ENTRY_ID ,CP.NAME,SCIRD.CHILD_PRODUCT_ID,SCIRD.REQ_QUANTITY,SCIRD.INDENT_NO,SCIP.CENTRAL_REQ_QUANTITY,SCIRD.PRODUCT_ID,SCIRD.SUB_PRODUCT_ID from  INDENT_ENTRY IE "
		+ "left outer join SUMADHURA_INDENT_CREATION SIC on SIC.INDENT_CREATION_ID=IE.INDENT_CREATION_ID "
		+ "left outer join  SUMADHURA_CNTL_INDENT_REQ_DTLS SCIRD on  SIC.INDENT_CREATION_ID=SCIRD.INDENT_NO "
		+ "left outer join SUMADHURA_CNTL_INDENT_PROCESS SCIP on SCIRD.INDENT_PROCESS_ID = SCIP.INDENT_PROCESS_ID "
		+ ",SUMADHURA_INTERMIDIATARY SI,CHILD_PRODUCT CP where "
		+ "CP.CHILD_PRODUCT_ID=SCIRD.CHILD_PRODUCT_ID and "
		+ "SI.INDENT_ENTRY_ID = IE.INDENT_ENTRY_ID  and SCIRD.PRODUCT_ID='"+objIssueToOtherSiteDto.getProdId1()+"' and SCIRD.SUB_PRODUCT_ID='"+objIssueToOtherSiteDto.getSubProdId1()+"' and SCIRD.CHILD_PRODUCT_ID='"+objIssueToOtherSiteDto.getChildProdId1()+"'  and SI.INDENT_ENTRY_ID='"+invoiceNumber+"' ";
		List<Map<String, Object>> requestedQty=null;
		requestedQty=jdbcTemplate.queryForList(query);
				for (Map<String, Object> map : requestedQty) {
				qty=	map.get("REQ_QUANTITY")==null?"":map.get("REQ_QUANTITY").toString();
					if(qty.length()!=0){
						break;
					}
				}

				return qty;
	}
	@Override
	public String getVendorState(String fromSite, String toSite) {
		String strSiteOneState = "";
		String strSiteTwoState = "";
		String strState = " ";
		List<Map<String, Object>> getStateDetails = null;

			String query = "select STATE from VENDOR_DETAILS where VENDOR_ID in (?,?)";
			getStateDetails = jdbcTemplate.queryForList(query, new Object[]{fromSite,toSite});

			if(getStateDetails != null && getStateDetails.size() ==2){

				strSiteOneState = getStateDetails.get(0).get("STATE").toString();
				strSiteTwoState = getStateDetails.get(1).get("STATE").toString();

				if(strSiteOneState.equals(strSiteTwoState) ){
					strState = "Local";
				}else{
					strState = "NonLocal";
				}
			}
		return strState;
	}

}
