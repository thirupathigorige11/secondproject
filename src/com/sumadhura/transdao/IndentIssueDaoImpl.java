package com.sumadhura.transdao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import javax.naming.NamingException;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.IndentIssueBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentIssueDto;
import com.sumadhura.dto.IndentIssueRequesterDto;
import com.sumadhura.service.IndentCreationService;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Repository
public class IndentIssueDaoImpl extends UIProperties  implements IndentIssueDao {

	static Logger log = Logger.getLogger(IndentIssueDaoImpl.class);

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private IndentCreationDao icd;
	
	@Autowired
	private IndentReceiveDao ird;
	
	@Autowired
	@Qualifier("posClass")
	IndentCreationService ics;
	
	public Map<String, String> loadProds(String siteId) {
		String marketing_Dept_Id=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String product_Dept="";
		Map<String, String> products = null;
		List<Map<String, Object>> dbProductsList = null;
		
		if(siteId.equals(marketing_Dept_Id)){product_Dept="MARKETING";}
		else{product_Dept="STORE";}
		
		products = new HashMap<String, String>();

		String prodsQry = "SELECT PRODUCT_ID, NAME FROM PRODUCT WHERE STATUS = 'A' AND PRODUCT_DEPT in ('ALL','"+product_Dept+"')";
		log.debug("Query to fetch product = "+prodsQry);

		dbProductsList = jdbcTemplate.queryForList(prodsQry, new Object[]{});

		for(Map<String, Object> prods : dbProductsList) {
			products.put(String.valueOf(prods.get("PRODUCT_ID")), String.valueOf(prods.get("NAME")));
		}

		return printMap(products);
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

	public String loadIndentIssueMeasurements(String childProdId,String siteId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbMeasurementsList = null;		

		log.debug("Child Product Id = "+childProdId);

		sb = new StringBuffer();
		String measurementId="";
		String groupId="";
		//String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		String measurementsQry = "SELECT M.MEASUREMENT_ID,M.NAME,C.MATERIAL_GROUP_ID FROM MEASUREMENT M,CHILD_PRODUCT C WHERE M.CHILD_PRODUCT_ID = ? and M.STATUS = ? AND C.CHILD_PRODUCT_ID=M.CHILD_PRODUCT_ID";
		log.debug("Query to fetch measurement(s) = "+measurementsQry);

		dbMeasurementsList = jdbcTemplate.queryForList(measurementsQry, new Object[]{childProdId,"A"});

		for(Map<String, Object> measurements : dbMeasurementsList) {
			measurementId=String.valueOf(measurements.get("MEASUREMENT_ID"));
			groupId=String.valueOf(measurements.get("MATERIAL_GROUP_ID")== null ? "0" : measurements.get("MATERIAL_GROUP_ID"));
			
			if(groupId.equalsIgnoreCase("NA")){
				groupId="0";
			}
			
			sb = sb.append(measurementId+"_"+String.valueOf(measurements.get("NAME"))+"_"+groupId+"|");
		}
		
		
		return sb.toString();
	}

	public int insertRequesterData(int indentEntryId, String userId, String siteId, IndentIssueRequesterDto iiReqDto) {

		int result = 0;

		Calendar cal1 = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
		
		
		String requesterQry = "INSERT INTO INDENT_ENTRY (INDENT_ENTRY_ID, USER_ID, SITE_ID, REQ_ID, RECEIVED_OR_ISSUED_DATE, REQUESTER_NAME, REQUESTER_ID, NOTE, INDENT_TYPE, SLIP_NUMBER,CONTRACTOR_ID,ENTRY_DATE,WORK_ORDER_NUMBER,VENDOR_ID,VEHICLENO,ISSUE_TYPE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,sysdate,?,?,?,?)";
		log.debug("Query for requester = "+requesterQry);

		result = jdbcTemplate.update(requesterQry, new Object[] {
				indentEntryId, 
				userId,siteId,	iiReqDto.getReqId(), 
				iiReqDto.getReqDate()+" "+sdf.format(cal1.getTime()), 
				iiReqDto.getRequesterName(), 
				iiReqDto.getRequesterId(), 

				iiReqDto.getPurpose(), 
				//if indent type is null it means the request came from new issue
				iiReqDto.getIndentType()==null?"OUT":iiReqDto.getIndentType(),
				iiReqDto.getSlipNumber(),
				iiReqDto.getContractorId(),
				iiReqDto.getWorkOrderNo(),
				iiReqDto.getVendorId(),
				iiReqDto.getVehicleNo(),
				iiReqDto.getIssueType()
				}
		
		);
		log.debug("Result = "+result);			
		logger.info("IndentIssueDao --> insertRequestorData() --> Result = "+result);

		return result;
	}

	public int insertIndentIssueData(int indentEntrySeqNum, IndentIssueDto issueDto, String basicAmt, String quantity, String priceId, String userId, String siteId,String expiryDate) {

		int result = 0;	
		AuditLogDetailsBean auditBean = null;
		Double totAmt = Double.parseDouble(basicAmt.replace(",", "").trim());
		Double qty = Double.parseDouble(quantity);
		Double perPiceAmt = totAmt*qty;
		String totalAmt = new DecimalFormat().format(perPiceAmt);
		//String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, ISSUED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, HSN_CODE, UORF, REMARKS, BLOCK_ID, FLOOR_ID, FLAT_ID, ENTRY_DATE,PRICE,TOTAL_AMOUNT, PRICE_ID) VALUES (INDENT_ENTRY_DETAILS_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate,?,?,?)";
		String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID, CHILD_PRODUCT_NAME, ISSUED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, HSN_CODE, UORF, REMARKS, BLOCK_ID, FLOOR_ID, FLAT_ID, ENTRY_DATE, PRICE_ID,IS_RECOVERABLE,EXPIRY_DATE,MATERIAL_GROUP_ID,WO_WORK_DESC_ID) VALUES (INDENT_ENTRY_DETAILS_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate,?,?,?,?,?)";
		log.debug("Query for indent issue = "+indentIssueQry);

		result = jdbcTemplate.update(indentIssueQry, new Object[]{
				indentEntrySeqNum, 
				issueDto.getProdId(), 
				issueDto.getProdName(), 
				issueDto.getSubProdId(), 
				issueDto.getSubProdName(), 
				issueDto.getChildProdId(), 
				issueDto.getChildProdName(), 
				issueDto.getQuantity(), 
				issueDto.getMeasurementId(), 
				issueDto.getMeasurementName(), 
				issueDto.getHsnCd(),
				issueDto.getuOrF(),
				issueDto.getRemarks(), 
				issueDto.getBlockId(), 
				issueDto.getFloorId(), 
				issueDto.getFlatId(),
				//basicAmt,
				//totalAmt,
				priceId,
				issueDto.getIsRecoverable(),expiryDate,issueDto.getGroupId(),issueDto.getWd()
		}
		);

		auditBean = new AuditLogDetailsBean();
		auditBean.setEntryDetailsId(String.valueOf(indentEntrySeqNum));
		auditBean.setLoginId(userId);
		auditBean.setOperationName("New Issue");
		auditBean.setStatus("Info");
		auditBean.setSiteId(siteId);
		/*new SaveAuditLogDetails().saveAuditLogDetails(auditBean);
		log.debug("Result = "+result);
		logger.info("IndentIssueDao --> insertIndentIssueData() --> Result = "+result);*/

		return result;
	}

	public int updateIndentAvalibility(IndentIssueDto indentIssuDto, String siteId) {
		List<Map<String, Object>> dbProductDetailsList = null;

		int result = 0;

		String availability_Id="";
		double issue_Form_Quantity=0.0;
		double quantity=0.0;
		String query="SELECT PRODUT_QTY,INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND MESURMENT_ID= ? AND SITE_ID=?  ";
		
		dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] {

				indentIssuDto.getProdId(), 
				indentIssuDto.getSubProdId(), 
				indentIssuDto.getChildProdId(), 
				indentIssuDto.getMeasurementId(), 
				siteId
		});
		if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
			for (Map<String, Object> prods : dbProductDetailsList) {
				quantity = Double.parseDouble(prods.get("PRODUT_QTY") == null ? "" : prods.get("PRODUT_QTY").toString());
				availability_Id = (prods.get("INDENT_AVAILABILITY_ID") == null ? "" : prods.get("INDENT_AVAILABILITY_ID").toString());
			}
		}
		
		
		issue_Form_Quantity=Double.parseDouble(indentIssuDto.getQuantity());
		quantity=quantity-issue_Form_Quantity;
		
		String updateIndentAvalibilityQry ="UPDATE INDENT_AVAILABILITY SET PRODUT_QTY ='"+quantity+"' WHERE INDENT_AVAILABILITY_ID ='"+availability_Id+"'";
		log.debug("Query for update indent avalibility = "+updateIndentAvalibilityQry);

		result = jdbcTemplate.update(updateIndentAvalibilityQry, new Object[] {});
		log.debug("Result = "+result);
		logger.info("IndentIssueDao --> updateIndentAvalibility() --> Result = "+result);

		return result;
	}

	public void updateIndentAvalibilityWithNewIndent(IndentIssueDto indentIssuDto, String siteId) {

		int result = 0;

		String requesterQry = "INSERT INTO INDENT_AVAILABILITY (INDENT_AVAILABILITY_ID, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, PRODUT_QTY, MESURMENT_ID, SITE_ID) VALUES (INDENT_AVAILABILITY_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";
		log.debug("Query for new indent entry in indent availability = "+requesterQry);

		result = jdbcTemplate.update(requesterQry, new Object[] {
				indentIssuDto.getProdId(), 
				indentIssuDto.getSubProdId(), 
				indentIssuDto.getChildProdId(), 
				indentIssuDto.getQuantity(), 
				indentIssuDto.getMeasurementId(),
				siteId
		}
		);
		log.debug("Result = "+result);			
		logger.info("IndentIssueDao --> updateIndentAvalibilityWithNewIndent() --> Result = "+result);
	}

	public int getIndentEntrySequenceNumber() {
		int indentEntrySeqNum = 0;
		indentEntrySeqNum = jdbcTemplate.queryForInt("SELECT INDENT_ENTRY_SEQ.NEXTVAL FROM DUAL");
		return indentEntrySeqNum;
	}
	
	@Override
	public String isWorkOrderExistsInSite(IndentIssueBean iib) {
		String siteId=iib.getSiteId();
		StringBuffer result=new StringBuffer("");
		String query="SELECT DISTINCT(WO_RECORD_CONTAINS) AS TYPE FROM QS_WORKORDER_TEMP_ISSUE WHERE SITE_ID=? AND WO_RECORD_CONTAINS IN('LABOR','MATERIAL','LABOR@@MATERIAL')";
		List<Map<String, Object>> dbData=jdbcTemplate.queryForList(query,new Object[] {siteId});
		for (Map<String, Object> map : dbData) {
			result.append(map.get("TYPE").toString()+",");
		}
		return result.toString();
	}
	
	@Override
	public String getProductAvailability(String prodId, String subProductId, String childProdId, String measurementId,String requesteddate,String siteId,String groupId,String isReceive) {

		List<Map<String, Object>> dbProductAvailabilityList = null;	

		//start------------ user341 ---------
		
		StringBuffer totalData = new StringBuffer();
		
		String doConvert="no";
		String qty = "0";
		
		String resultData="0";
		
		String strConversionMeasurmentId  =  validateParams.getProperty(childProdId+"ID") == null ? "" : validateParams.getProperty(childProdId+"ID").toString();
		//System.out.println("strConversionMeasurmentId(in properties file): "+strConversionMeasurmentId);
	//	System.out.println("measurementId(at the time of user input): "+measurementId );
		if(!strConversionMeasurmentId.equals(measurementId)&&!strConversionMeasurmentId.equals(""))
		{

			measurementId=strConversionMeasurmentId;
			doConvert="yes";
		}

		//end-----------------------------------


		log.debug("Product Id = "+prodId+", Sub Product Id = "+subProductId+", Child Product Id = "+childProdId+", Measurement Id = "+measurementId+" and Site Id = "+siteId);
		//System.out.println("requesteddate"+requesteddate);
		if(requesteddate!=null && !requesteddate.equals("")){
		String indentAvaQry = "SELECT  SUM(AVAILABLE_QUANTITY) FROM SUMADHURA_PRICE_LIST  WHERE PRODUCT_ID = ? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID = ? AND UNITS_OF_MEASUREMENT = ? AND SITE_ID = ? AND CREATED_DATE<= TO_DATE(?,'dd-MM-yy') and STATUS = 'A'";
		//log.debug("Query to fetch product availability = "+indentAvaQry);

		dbProductAvailabilityList = jdbcTemplate.queryForList(indentAvaQry, new Object[] {
				prodId,
				subProductId,
				childProdId,
				measurementId,siteId,requesteddate

		}
		);
		//System.out.println("dblist"+dbProductAvailabilityList);

		
		for(Map<String, Object> availableQuantity : dbProductAvailabilityList) {
			//String measurementName = getMeasurementName(measurementId);
			//qty = String.valueOf(availableQuantity.get("PRODUT_QTY")+" "+measurementName);
			qty = String.valueOf(availableQuantity.get("SUM(AVAILABLE_QUANTITY)")==null ? "0" : availableQuantity.get("SUM(AVAILABLE_QUANTITY)").toString());
			
			//qty=String.format( "%.2f",Double.valueOf(qty));
			
			/*double i =Double.parseDouble(qty);
			 i =Double.parseDouble(new DecimalFormat("##.##").format(qty));
			qty=String.valueOf(i);*/
			//System.out.println("value"+qty);
			break;
		}
		}
		//start--------- user341 ----------
		if(doConvert.equals("yes"))
		{
		
			String childCheck=validateParams.getProperty(childProdId+"AVAILQTYCALCULATION")== null ? "" : validateParams.getProperty(childProdId+"AVAILQTYCALCULATION").toString();
			/*if(qty!=null && !qty.equals("")){
				double i=Double.parseDouble(qty);
			}*/
			//double i=Double.parseDouble(qty);
			double i =Double.parseDouble(new DecimalFormat("##.##").format(Double.valueOf(qty)));
			String stractqty=validateParams.getProperty(childProdId+"ActualQuantity");
			Double actqty=Double.parseDouble(stractqty);
			if(childCheck.equalsIgnoreCase("Multiplication")){
				
			i=i*actqty;
			
			}else{
				
				i=i/actqty;
				
			}

			
			qty=String.format( "%.2f", i );
		}
		//end--------------------------
		if(groupId!=null && !groupId.equals("0")){
			if(isReceive!=null && isReceive.equalsIgnoreCase("true")){
				resultData=ird.gettingReqBoqQuantityAjax(groupId,siteId);
			}else{
			 resultData=ics.gettingBoqQuantityAjax(groupId,siteId);}
			// resultData=ics.gettingBoqQuantityAjax(groupId,siteId);
			/*childSortMap=icd.getChildProductsWithGroupId(groupId);
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
			 indentPendingQuantity=(icd.getIndentPendingQuantity(childProductList,siteId,measurementList));
			 totalBOQQuantity=(icd.getBOQQuantity(groupId,siteId));
			 resultQuantity=totalBOQQuantity-(receivedQuantity-issuedQuantity+indentPendingQuantity);
			 if(resultQuantity<0){ resultQuantity=0;}*/
			 totalData=totalData.append(qty+"_"+resultData);
		}else{
			 totalData=totalData.append(qty);
		}
		
		//totalData=totalData.toString();
		return totalData.toString();
	}



	@Override
	public Map<String, String> loadBlockDetails(String strSiteId) {

		Map<String, String> blocks = null;
		List<Map<String, Object>> dbBlocksList = null;

		blocks = new HashMap<String, String>();

		String blocksQry = "SELECT BLOCK_ID, NAME FROM BLOCK where SITE_ID = ?";
		log.debug("Query to fetch blocks = "+blocksQry);

		dbBlocksList = jdbcTemplate.queryForList(blocksQry, new Object[]{strSiteId});

		for(Map<String, Object> block : dbBlocksList) {
			blocks.put(String.valueOf(block.get("BLOCK_ID")), String.valueOf(block.get("NAME")));
		}
		return blocks;
	}



	@Override
	public String getProjectName(HttpSession session) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbSitesList = null;		

		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

		log.debug("Site Id = "+siteId);
		logger.info(siteId);

		sb = new StringBuffer();

		String siteNameQry = "SELECT SITE_NAME FROM SITE WHERE SITE_ID = ?";
		log.debug("Query to fetch site name = "+siteNameQry);

		dbSitesList = jdbcTemplate.queryForList(siteNameQry, new Object[]{siteId});

		for(Map<String, Object> siteName : dbSitesList) {
			sb = sb.append(String.valueOf(siteName.get("SITE_NAME")));
			break;
		}
		logger.info(sb.toString());
		return sb.toString();	
	}

	@Override
	public String getFloorDetails(String blockId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbFloorList = null;

		log.debug("Block Id = "+blockId);

		sb = new StringBuffer();

		String floorsQry = "SELECT FLOOR_ID, NAME FROM FLOOR WHERE BLOCK_ID = ?";
		log.debug("Query to fetch floors = "+floorsQry);

		dbFloorList = jdbcTemplate.queryForList(floorsQry, new Object[]{blockId});

		for(Map<String, Object> floors : dbFloorList) {
			sb = sb.append(String.valueOf(floors.get("FLOOR_ID"))+"@@"+String.valueOf(floors.get("NAME"))+"|");
		}
		return sb.toString();
	}

	@Override
	public String getFlatDetails(String floorId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbFlatsList = null;

		log.debug("Floor Id = "+floorId);

		sb = new StringBuffer();

		String flatsQry = "SELECT FLAT_ID, NAME FROM FLAT WHERE FLOOR_ID = ?";
		log.debug("Query to fetch flats = "+flatsQry);

		dbFlatsList = jdbcTemplate.queryForList(flatsQry, new Object[]{floorId});

		for(Map<String, Object> flats : dbFlatsList) {
			sb = sb.append(String.valueOf(flats.get("FLAT_ID"))+"@@"+String.valueOf(flats.get("NAME"))+"|");
		}
		return sb.toString();
	}
	
	/*============================================ getting the workorder desvcription using work order number start ==================================*/
	@Override
	public String getWdDetails(String workOrderId, String siteId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbWdList = null;

		log.debug("Floor Id = "+workOrderId);

		sb = new StringBuffer();

		 StringBuffer queryForWD=new StringBuffer("SELECT QWID.WO_WORK_DESC_ID,QWW.WO_WORK_DESCRIPTION,QWID.QS_WORKORDER_ISSUE_ID  FROM QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_DETAILS QWID,QS_WO_WORKDESC QWW")
					.append(" WHERE QWI.QS_WORKORDER_ISSUE_ID=QWID.QS_WORKORDER_ISSUE_ID AND  QWID.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID") 
					.append(" AND QWI.STATUS='A' AND QWI.WORK_ORDER_NUMBER=? AND QWI.SITE_ID=?");
			
			log.debug("Query to fetch flats = "+queryForWD);

		dbWdList = jdbcTemplate.queryForList(queryForWD.toString(), new Object[]{workOrderId,siteId});

		for(Map<String, Object> wd : dbWdList) {
			sb = sb.append(String.valueOf(wd.get("WO_WORK_DESC_ID"))+"@@"+String.valueOf(wd.get("WO_WORK_DESCRIPTION"))+"|");
		}
		return sb.toString();
	}
	/*=========================================== getting the block id and name  start================================================*/
	@Override
	public String getWDblockDetails(String wdId, String materialGroupId, String workOrderNo, String siteId) {
		String blockId="",floorId="",flatId="";
		String blockName="",floorName="",flatName="";
		String workAreadId="";
		double totalMaterialQuantity=0.0;
		 Double bufferQuantity=0.0;
//		Map<String, Map<String, String>> blockData=new HashMap<String, Map<String, String>>();
//		Map<String, Map<String, String>> floorData=new HashMap<String, Map<String, String>>();
//		Map<String, Map<String, String>> flatData=new HashMap<String, Map<String, String>>();
		//holding xml data in this fields and and last converting into json object and passing to front ent in string type
		StringBuffer floorXML=new StringBuffer("");
		StringBuffer flatXML=new StringBuffer("");
		StringBuffer blockXML=new StringBuffer("");
		//for checking data is block floor or flat wise
		boolean isMaterialDataBlockWise=false;
		boolean isMaterialDataFloorWise=false;
		boolean isMaterialDataFlatWise=false;
		String holdPreviousBlockName="";
		String holdPreviousFloorName="";
	 	StringBuffer sb = null;
		List<Map<String, Object>> dbBlockList = null;
	

		log.debug("Floor Id = "+wdId);

		
	//	blockXML.append("<blockNames>");	
		
		sb = new StringBuffer();

		String flatsQry = "SELECT DISTINCT(QBAM.BLOCK_ID),B.NAME FROM QS_BOQ_AREA_MAPPING QBAM,BLOCK B  WHERE WO_WORK_DESC_ID=? AND QBAM.BLOCK_ID=B.BLOCK_ID AND QBAM.RECORD_TYPE='MATERIAL' ";
		
		StringBuffer queryForMaterialData = new StringBuffer("SELECT  QWIAD.WO_WORK_AREA_ID,QWID.WO_WORK_DESC_ID,QWID.UOM,QWPD.WORK_AREA,QWPD.TOTAL_QUANTITY,")
				.append(" (CASE WHEN B.BLOCK_ID  IS NULL THEN '0'  ELSE B.BLOCK_ID END)AS BLOCK_ID,")
				.append(" (CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END)AS FLOOR_ID,")
				.append(" (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END)AS  FLAT_ID,")
				.append(" QWPD.MATERIAL_GROUP_ID,QWPD.UOM AS MATERIAL_UOM,B.NAME AS BLOCK_NAME,F.NAME AS FLOOR_NAME,FLAT.NAME AS FLAT_NAME")
				.append(" FROM QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_DETAILS QWID,QS_WORKORDER_ISSUE_AREA_DETAILS QWIAD,")
				.append(" QS_WORKORDER_PRODUCT_DTLS QWPD,QS_BOQ_AREA_MAPPING QBAM LEFT OUTER JOIN  BLOCK B ON B.BLOCK_ID = QBAM.BLOCK_ID")
				.append(" LEFT OUTER JOIN FLOOR F ON F.FLOOR_ID = QBAM.FLOOR_ID ")
				.append(" LEFT OUTER JOIN FLAT FLAT ON FLAT.FLAT_ID = QBAM.FLAT_ID")
				.append(" WHERE QWI.QS_WORKORDER_ISSUE_ID=QWID.QS_WORKORDER_ISSUE_ID AND")
				.append(" QWID.WO_WORK_ISSUE_DTLS_ID=QWIAD.WO_WORK_ISSUE_DTLS_ID AND")
				.append(" QWIAD.WO_WORK_ISSUE_AREA_DTLS_ID=QWPD.WO_WORK_ISSUE_AREA_DTLS_ID AND")
				.append(" QBAM.WO_WORK_AREA_ID=QWIAD.WO_WORK_AREA_ID AND")
				.append(" QWID.WO_WORK_DESC_ID=? AND")
				.append(" QWPD.MATERIAL_GROUP_ID=? AND")
				.append(" QWI.WORK_ORDER_NUMBER=? AND QWI.SITE_ID=?")
				.append(" ORDER BY (CASE WHEN B.BLOCK_ID  IS NULL THEN '0'  ELSE B.BLOCK_ID END),")
				.append(" (CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END),")
				.append(" (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END)");
		Object[] queryParams={wdId,materialGroupId,workOrderNo,siteId};
		log.debug("Query to fetch flats = "+flatsQry);
		int count=0;
		List<Map<String,Object>> list= jdbcTemplate.queryForList(queryForMaterialData.toString(),queryParams);
		for (Map<String, Object> map : list) {
			 blockId=map.get("BLOCK_ID")==null?"":map.get("BLOCK_ID").toString();
			 floorId=map.get("FLOOR_ID")==null?"":map.get("FLOOR_ID").toString();
			 flatId=map.get("FLAT_ID")==null?"":map.get("FLAT_ID").toString();

			 blockName=map.get("BLOCK_NAME")==null?"":map.get("BLOCK_NAME").toString();
			 floorName=map.get("FLOOR_NAME")==null?"":map.get("FLOOR_NAME").toString();
			 flatName=map.get("FLAT_NAME")==null?"":map.get("FLAT_NAME").toString();
			
			 totalMaterialQuantity=Double.valueOf(map.get("TOTAL_QUANTITY")==null?"0":map.get("TOTAL_QUANTITY").toString());
			 
			 
			 //materialGroupId=map.get("MATERIAL_GROUP_ID")==null?"":map.get("MATERIAL_GROUP_ID").toString();
			// workDescId=map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString();
			 workAreadId=map.get("WO_WORK_AREA_ID")==null?"":map.get("WO_WORK_AREA_ID").toString();
			 try {
				 //here loading buffer quantity
				 StringBuffer getWOBufferQuantity=new StringBuffer("select sum(QBS.QUANTITY) from QS_BUFFER_STOCK  QBS where QBS.SITE_ID=? AND  QBS.MATERIAL_GROUP_ID=? AND QBS.WO_WORK_AREA_ID=? ");
				  bufferQuantity=jdbcTemplate.queryForObject(getWOBufferQuantity.toString(), new Object[]{siteId,materialGroupId,workAreadId},Double.class);if(bufferQuantity==null){bufferQuantity=0.0;}
			} catch (Exception e) {
				log.info("Got Exception in buffer quntity no buffer quantity found :"+e.getMessage());
			}
			  
			 //here loading previous issued quantity for WD and Group Id 
			StringBuffer queryForTotalQuantity=new StringBuffer("select sum(IED.ISSUED_QTY) as ISSUED_QTY,IED.MATERIAL_GROUP_ID,IED.WO_WORK_DESC_ID ")
					.append(" from INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE")
					.append(" where IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID")
					.append(" AND IED.WO_WORK_DESC_ID=? AND IE.WORK_ORDER_NUMBER=? AND IE.SITE_ID=?");
					//if blockid is not empty add condition in query
					if(blockId.length()!=0&&!blockId.equals("0")){
						queryForTotalQuantity.append(" AND IED.BLOCK_ID='"+blockId+"' ");isMaterialDataBlockWise=true;
						
					}					
					//if floorid is not empty add condition in query
					if(floorId.length()!=0&&!floorId.equals("0")){
						queryForTotalQuantity.append(" AND IED.FLOOR_ID='"+floorId+"' ");isMaterialDataFloorWise=true;
						
					}					
					//if flat is not empty add condition in query
					if(flatId.length()!=0&&!flatId.equals("0")) {
						queryForTotalQuantity.append(" AND IED.FLAT_ID='"+flatId+"' ");isMaterialDataFlatWise=true;
						
					}
					
				 queryForTotalQuantity.append(" AND IED.MATERIAL_GROUP_ID='"+materialGroupId+"' group by IED.MATERIAL_GROUP_ID,IED.WO_WORK_DESC_ID ");
			
			List<Map<String, Object>> totalQuantityList=jdbcTemplate.queryForList(queryForTotalQuantity.toString(),wdId,workOrderNo,siteId);
			if(totalQuantityList.size()>1){
				throw new RuntimeException("Got two results, expected one.");
			}
			for (Map<String, Object> map2 : totalQuantityList) {
				double totalQuantity=Double.valueOf(map2.get("ISSUED_QTY")==null?"0":map2.get("ISSUED_QTY").toString());
				Map<String, Object> tempMap =list.get(count);
				count++;
				tempMap.put("ISSUED_QTY", totalQuantity);
				BigDecimal bigDecimal2=new BigDecimal((totalMaterialQuantity+bufferQuantity)-totalQuantity).setScale(2,RoundingMode.CEILING);
				tempMap.put("AVAILABLE_QTY", (bigDecimal2.doubleValue()));
			}
			//if we did not get the previous value then we we need to point our cursor (count) to next list object for adding issued quantity
			if(totalQuantityList.size()==0){
				Map<String, Object> tempMap =list.get(count);
				tempMap.put("ISSUED_QTY", "0");
				tempMap.put("AVAILABLE_QTY", (totalMaterialQuantity+bufferQuantity));
				count++;
			}
 
			floorXML=new StringBuffer();
			flatXML=new StringBuffer();
			
			Map<String, Object> tempMap =list.get(count-1);
			double totalIssuedQuantity=Double.valueOf(tempMap.get("ISSUED_QTY")==null?"0":tempMap.get("ISSUED_QTY").toString());
			double AVAILABLE_QTY= ((totalMaterialQuantity+bufferQuantity)-totalIssuedQuantity);
			//BigDecimal bigDecimal2=new BigDecimal(totalMaterialQuantity-totalQuantity).setScale(2,RoundingMode.CEILING);
			//double AVAILABLE_QTY=bigDecimal2.doubleValue();
			//generating XML object and converting into json and sending to front end 
			if(isMaterialDataFlatWise){
				 if(!holdPreviousBlockName.equals(blockName)){
					 //starting tag of XML object with block name if condition is satisfied
					 blockXML.append("<blockName>"+blockName);	
				}
				
				if(!holdPreviousFloorName.equals(floorName)){
					//if this is second iteration and holdPreviousFloorName variable is not empty and not equal to floor name 
					//close the tag's flats, floor
					if(holdPreviousFloorName.length()!=0){
						//adding empty so we will get the value in object array so we can iterate json easily
						//========================================================						
						/*	flatXML.append("<flat>");	
							flatXML.append("<id><![CDATA["+flatId+"]]></id>");
							flatXML.append("<blockId><![CDATA["+blockId+"]]></blockId>");
							flatXML.append("<floorId><![CDATA["+floorId+"]]></floorId>");
							flatXML.append("<name><![CDATA["+flatName+"]]></name>");
							flatXML.append("<AvlQty><![CDATA["+AVAILABLE_QTY+"]]></AvlQty>");
							flatXML.append("</flat>");*/
						//========================================================
						
						flatXML.append("<flat>");	
						flatXML.append("</flat>");
		 				
		 				//appending flat data to floor 
						floorXML.append(flatXML);
						//closing the tag if condition is satisfied
						//if current iteration floor name and previous holed floor name is not same that time closing tag
						floorXML.append("</flats>");
						floorXML.append("</floor>");
					}
					//if holdPreviousFloorName and floorName is not equal start new tag with floor name
					floorXML.append("<floor>"+floorName);	 	
					floorXML.append("<flats>");
				}
				//adding flat data and quantity  
				flatXML.append("<flat>");	
				flatXML.append("<id><![CDATA["+flatId+"]]></id>");
				flatXML.append("<blockId><![CDATA["+blockId+"]]></blockId>");
				flatXML.append("<floorId><![CDATA["+floorId+"]]></floorId>");
				flatXML.append("<name><![CDATA["+flatName+"]]></name>");
				flatXML.append("<AvlQty><![CDATA["+AVAILABLE_QTY+"]]></AvlQty>");
				flatXML.append("<totalQty><![CDATA["+totalMaterialQuantity+"]]></totalQty>");
				flatXML.append("<totalIssuedQty><![CDATA["+totalIssuedQuantity+"]]></totalIssuedQty>");
				flatXML.append("</flat>");
 				
 				//appending flat data to floor 
				floorXML.append(flatXML);
				if((!holdPreviousFloorName.equals(floorName))){
				
				}
				 //appending floor data to block
				blockXML.append(floorXML);
				//if this is the last iteration we have to close block name tag's block name
				if((!holdPreviousBlockName.equals(blockName)&&count!=1)||list.size()==(count)){
					floorXML=new StringBuffer();
			    	floorXML.append("</flats>");
					floorXML.append("</floor>");
					blockXML.append(floorXML);
					
					//adding empty object
					/*floorXML=new StringBuffer();
					floorXML.append("<floor>");	 	
					floorXML.append("<flats>");
					floorXML.append("</flats>");
					floorXML.append("</floor>");
					blockXML.append(floorXML);*/
					//closing block name tag if this is last iteration or block name is changed
				   	blockXML.append("</blockName>");
			    }
				holdPreviousBlockName=blockName;
				holdPreviousFloorName=floorName;
				
				
			}else if(isMaterialDataFloorWise){
				//blockXML.append("<blocks>");
				//if data is coming in floor wise  
				floorXML=new StringBuffer();
				//only first adding the node in string buffer
				//next time adding it's sub nodes
				if(!holdPreviousBlockName.equals(blockName)){
					if(holdPreviousBlockName.length()!=0){
						floorXML=new StringBuffer();
						floorXML.append("<floor>");
						floorXML.append("</floor>");
					    //adding empty floor object for json array object, if only one object exists it's giving problem in iteration in front end 
						blockXML.append(floorXML);
						blockXML.append("</blockName>");	
					}
					blockXML.append("<blockName>"+blockName);	
				}
				//adding floor name and floor avail quantity
				floorXML.append("<floor>");
				floorXML.append("<id><![CDATA["+floorId+"]]></id>");
				floorXML.append("<blockId><![CDATA["+blockId+"]]></blockId>");
				floorXML.append("<name><![CDATA["+floorName+"]]></name>");
				floorXML.append("<AvlQty><![CDATA["+AVAILABLE_QTY+"]]></AvlQty>");
				
				floorXML.append("<totalQty><![CDATA["+totalMaterialQuantity+"]]></totalQty>");
				floorXML.append("<totalIssuedQty><![CDATA["+totalIssuedQuantity+"]]></totalIssuedQty>");
				
				floorXML.append("</floor>");
			    blockXML.append(floorXML);
			    //if this is the last iteration of list then closing main node of xml
			    if(list.size()==(count)){
			    	floorXML=new StringBuffer();
					floorXML.append("<floor>");
					floorXML.append("</floor>");
				    //adding empty floor object for json array object, if only one object exists it's giving problem in iteration in front end 
					blockXML.append(floorXML);
			    	//closing mail tag
			    	blockXML.append("</blockName>");
			    }
				holdPreviousBlockName=blockName;
			}else if(isMaterialDataBlockWise){
				blockXML.append("<blockName>");
				blockXML.append("<id><![CDATA["+blockId+"]]></id>");
				blockXML.append("<name><![CDATA["+blockName+"]]></name>");
				blockXML.append("<AvlQty><![CDATA["+AVAILABLE_QTY+"]]></AvlQty>");

				blockXML.append("<totalQty><![CDATA["+totalMaterialQuantity+"]]></totalQty>");
				blockXML.append("<totalIssuedQty><![CDATA["+totalIssuedQuantity+"]]></totalIssuedQty>");
				blockXML.append("</blockName>");
			}
		}
		//blockXML.append("</blockNames>");	
		//adding dummy object for getting length more than one
		floorXML=new StringBuffer();
		blockXML.append("<blockName>");
	 	blockXML.append("</blockName>");
	
	 	
		/*blockXML=new  StringBuffer();
	 	
		blockXML.append("<blockName>");
		blockXML.append("<name>Block A</name>");
		blockXML.append("<AvlQty>1</AvlQty>");
		blockXML.append("</blockName>");
		blockXML.append("<blockName>");
		blockXML.append("<name>Block B</name>");
		blockXML.append("<AvlQty>2</AvlQty>");
		blockXML.append("</blockName>");*/
		
		int PRETTY_PRINT_INDENT_FACTOR = 4;
		JSONObject xmlJSONObj;
		try {
			xmlJSONObj = XML.toJSONObject(blockXML.toString());
			blockXML=new StringBuffer(xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR));
			System.out.println(blockXML);
		} catch (JSONException e) {
		 	e.printStackTrace();
		}
			
	  
		return blockXML.toString();
	}
	/*=========================================== getting the block id and name  end================================================*/
	
	/*=========================================== getting the floor id and name  start================================================*/
	@Override
	public String getFloorDataDetails(String wdId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbBlockList = null;

		log.debug("Floor Id = "+wdId);

		sb = new StringBuffer();

		String flatsQry = "SELECT DISTINCT(QBAM.FLOOR_ID),F.NAME FROM QS_BOQ_AREA_MAPPING QBAM,FLOOR F WHERE WO_WORK_DESC_ID=? AND QBAM.FLOOR_ID=F.FLOOR_ID";
		log.debug("Query to fetch flats = "+flatsQry);

		dbBlockList = jdbcTemplate.queryForList(flatsQry, new Object[]{wdId});

		for(Map<String, Object> wd : dbBlockList) {
			sb = sb.append(String.valueOf(wd.get("FLOOR_ID"))+"@@"+String.valueOf(wd.get("NAME"))+"|");
		}
		return sb.toString();
	}
	/*=========================================== getting the floor id and name  end================================================*/
	
	/*=========================================== getting the floor id and name  start================================================*/
	@Override
	public String getFlatDataDetails(String wdId) {

		StringBuffer sb = null;
		List<Map<String, Object>> dbBlockList = null;

		log.debug("Floor Id = "+wdId);

		sb = new StringBuffer();

		String flatsQry = "SELECT DISTINCT(QBAM.FLAT_ID),F.NAME FROM QS_BOQ_AREA_MAPPING QBAM,FLAT F WHERE WO_WORK_DESC_ID=? AND QBAM.FLAT_ID=F.FLAT_ID";
		log.debug("Query to fetch flats = "+flatsQry);

		dbBlockList = jdbcTemplate.queryForList(flatsQry, new Object[]{wdId});

		for(Map<String, Object> wd : dbBlockList) {
			sb = sb.append(String.valueOf(wd.get("FLAT_ID"))+"@@"+String.valueOf(wd.get("NAME"))+"|");
		}
		return sb.toString();
	}
	/*=========================================== getting the floor id and name  end================================================*/
	
	/*============================================== getting the workorder description using work order number end ======================================*/
	public List<ViewIndentIssueDetailsBean> getViewIndentIssueDetails(String fromDate, String toDate, String siteId, String val, String[] data) {
		String blockId="",floorId="",flatId="",issueType="",contractorId="",indentEntryId="";
		if(data!=null){
			 blockId=data[0];floorId=data[1];
			 flatId=data[2];issueType=data[3];
			 contractorId=data[4];indentEntryId=data[5];
		}
		
		String query = "";
		String strDCFormQuery = "";
		String strDCNumber = "";
		String strInvoiceNo = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<ViewIndentIssueDetailsBean> list = new ArrayList<ViewIndentIssueDetailsBean>();
		ViewIndentIssueDetailsBean indentObj = null; 
		double amtafterTaxPerUnit=0.0;
		double total=0.0;
		int sNo=0;
		String workOrderNumber="";
		com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		try {
			//if part is for view indent receive details,else part is for view indent issue details
			template = new JdbcTemplate(DBConnection.getDbConnection());
			if (StringUtils.isNotBlank(val)&&!val.equals("GetIssueDetails")) {
				query = "SELECT IE.INDENT_TYPE,IE.PO_ID,IE.SITE_ID,IE.INDENT_ENTRY_ID,VD.VENDOR_ID,VD.VENDOR_NAME,IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME,IED.RECEVED_QTY, IE.INVOICE_ID,IED.MEASUR_MNT_NAME,IE.RECEIVED_OR_ISSUED_DATE,SED.EMP_NAME,IE.INVOICE_DATE,IE.TOTAL_AMOUNT,SPL.BASIC_AMOUNT,SPL.TAX_AMOUNT,SPL.OTHER_CHARGES_AFTER_TAX,(SPL.TOTAL_AMOUNT) as AFTER_TAX_TOTAL,NVL(IED.EXPIRY_DATE, '-') as EXPIRY_DATE FROM INDENT_ENTRY IE,INDENT_ENTRY_DETAILS IED, VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PRICE_LIST SPL WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE IN ('IN','IND','INO','INU') AND IE.SITE_ID='"+siteId+"' AND IE.VENDOR_ID=VD.VENDOR_ID  and IE.USER_ID = SED.EMP_ID and SPL.INDENT_ENTRY_DETAILS_ID = IED.INDENT_ENTRY_DETAILS_ID ";


				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = query+ " AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = query+ " AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE) >=TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = query+ " AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
				}


				// extra condition
				if(StringUtils.isNotBlank(query)){
					query = query + " and IE.INDENT_ENTRY_ID not  in (select DCE.INDENT_ENTRY_ID from DC_ENTRY DCE where DCE.INDENT_ENTRY_ID is not null)";
				}
				// above extra condition is added newly for not getting products from invoice which is converted from DC

				strDCFormQuery = "SELECT IE.PO_ID,IE.SITE_ID,IE.DC_ENTRY_ID,IE.INDENT_ENTRY_ID,IE.DC_DATE,VD.VENDOR_ID,VD.VENDOR_NAME,SED.EMP_NAME, DF.PRODUCT_NAME, DF.SUB_PRODUCT_NAME, DF.CHILD_PRODUCT_NAME, DF.RECEVED_QTY,IE.INVOICE_ID, DF.MEASUR_MNT_NAME,IE.RECEIVED_DATE as RECEIVED_OR_ISSUED_DATE,IE.DC_NUMBER,IE.INVOICE_DATE,IE.TOTAL_AMOUNT,SPL.BASIC_AMOUNT,SPL.TAX_AMOUNT,SPL.OTHER_CHARGES_AFTER_TAX,(SPL.TOTAL_AMOUNT) as AFTER_TAX_TOTAL,NVL(DF.EXPIRY_DATE, '-') as EXPIRY_DATE FROM DC_ENTRY IE,DC_FORM DF, VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PRICE_LIST SPL WHERE IE.DC_ENTRY_ID = DF.DC_ENTRY_ID  AND IE.SITE_ID='"+siteId+"'  AND IE.VENDOR_ID=VD.VENDOR_ID and IE.USER_ID = SED.EMP_ID and SPL.DC_FORM_ENTRY_ID = DF.DC_FORM_ID ";


				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					strDCFormQuery = strDCFormQuery + " AND TRUNC(IE.RECEIVED_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') ";

				} else if (StringUtils.isNotBlank(fromDate)) {
					strDCFormQuery = strDCFormQuery + " AND TRUNC(IE.RECEIVED_DATE)  >= TO_DATE('"+fromDate+"','dd-MM-yy') ";
				} else if(StringUtils.isNotBlank(toDate)) {
					strDCFormQuery = strDCFormQuery + " AND TRUNC(IE.RECEIVED_DATE)  <=  TO_DATE('"+toDate+"','dd-MM-yy') ";
				}

				// " and DF.STATUS='A' " is removed in above 3 queries to get products from all DC's (means DC's which are converted into invoice & DC's which are not converted into invoice)

			} else {
				 if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID,IE.ENTRY_DATE, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY,B.BLOCK_ID FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD,BLOCK B WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND TRUNC(IE.ENTRY_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
					//query = "SELECT LD.USERNAME,IE.CONTRACTOR_NAME,IE.REQUESTER_NAME, IE.REQUESTER_ID,IE.ENTRY_DATE, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY,B.NAME,IE.SLIP_NUMBER FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD,BLOCK B WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND  IED.BLOCK_ID = B.BLOCK_ID and IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND TRUNC(IE.ENTRY_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
					query="SELECT nvl(VD.ADDRESS,SC.ADDRESS) as ADDRESS,IE.VEHICLENO,IE.ISSUE_TYPE,IE.INDENT_ENTRY_ID,VD.VENDOR_NAME,IE.INDENT_TYPE,IE.RECEIVED_OR_ISSUED_DATE,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,IE.REQUESTER_NAME, IE.REQUESTER_ID ,IE.ENTRY_DATE , IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY,IE.SLIP_NUMBER,IE.TOTAL_AMOUNT,IED.MEASUR_MNT_NAME,IED.REMARKS,SED.EMP_NAME,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TAX,B.NAME,(F.NAME)AS FLAT_NAME,(FR.NAME)AS FLOOR_NAME,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,NVL(IED.EXPIRY_DATE, '-') as EXPIRY_DATE "
						+ ",IE.WORK_ORDER_NUMBER,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where   WORK_ORDER_NUMBER =IE.WORK_ORDER_NUMBER and SITE_ID='"+siteId+"') as QS_WORKORDER_ISSUE_ID,IE.SITE_ID"	
						+ " FROM INDENT_ENTRY IE LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  on SC.CONTRACTOR_ID=IE.CONTRACTOR_ID  LEFT  JOIN VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID  ,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED "
							+ " LEFT OUTER JOIN BLOCK B on B.BLOCK_ID=IED.BLOCK_ID "
							+ " LEFT OUTER JOIN FLAT F on F.FLAT_ID=IED.FLAT_ID "
							+ " LEFT OUTER JOIN FLOOR FR on FR.FLOOR_ID=IED.FLOOR_ID "
							+ " WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID and IE.USER_ID=SED.EMP_ID AND SPL.PRICE_ID=IED.PRICE_ID and IE.INDENT_TYPE IN ('OUT','OUTO','OUTT','OUTR','OUTS') AND IE.SITE_ID='"+siteId+"' AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') ";
				} else if (StringUtils.isNotBlank(fromDate)) {
					//query = "SELECT LD.USERNAME, IE.CONTRACTOR_NAME,IE.REQUESTER_NAME, IE.REQUESTER_ID ,IE.ENTRY_DATE , IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY,B.NAME,IE.SLIP_NUMBER FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD,BLOCK B WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND   IED.BLOCK_ID = B.BLOCK_ID and IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND  TRUNC(IE.ENTRY_DATE) = TO_DATE('"+fromDate+"', 'dd-MM-yy')";
					query = "SELECT  nvl(VD.ADDRESS,SC.ADDRESS) as ADDRESS,IE.VEHICLENO,IE.ISSUE_TYPE,IE.INDENT_ENTRY_ID,VD.VENDOR_NAME,IE.INDENT_TYPE,IE.RECEIVED_OR_ISSUED_DATE,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,IE.REQUESTER_NAME, IE.REQUESTER_ID ,IE.ENTRY_DATE ,IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY,IE.SLIP_NUMBER,IE.TOTAL_AMOUNT,IED.MEASUR_MNT_NAME,IED.REMARKS,SED.EMP_NAME,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TAX,B.NAME,(F.NAME)AS FLAT_NAME,(FR.NAME)AS FLOOR_NAME,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,NVL(IED.EXPIRY_DATE, '-') as EXPIRY_DATE  "
						+ ",IE.WORK_ORDER_NUMBER,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where   WORK_ORDER_NUMBER =IE.WORK_ORDER_NUMBER and SITE_ID='"+siteId+"') as QS_WORKORDER_ISSUE_ID,IE.SITE_ID"		
						+ " FROM INDENT_ENTRY IE 	LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  on SC.CONTRACTOR_ID=IE.CONTRACTOR_ID  LEFT  JOIN VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID ,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED "
							+ " LEFT OUTER JOIN BLOCK B on B.BLOCK_ID=IED.BLOCK_ID"
							+ " LEFT OUTER JOIN FLAT F on F.FLAT_ID=IED.FLAT_ID "
							+ " LEFT OUTER JOIN FLOOR FR on FR.FLOOR_ID=IED.FLOOR_ID  "
							+ " WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID and IE.USER_ID=SED.EMP_ID AND SPL.PRICE_ID=IED.PRICE_ID and IE.INDENT_TYPE IN ('OUT','OUTO','OUTT','OUTR','OUTS') AND IE.SITE_ID='"+siteId+"'  AND  TRUNC(IE.RECEIVED_OR_ISSUED_DATE) = TO_DATE('"+fromDate+"', 'dd-MM-yy') ";

				} else if(StringUtils.isNotBlank(toDate)) {
					//query = "SELECT LD.USERNAME, IE.CONTRACTOR_NAME,IE.REQUESTER_NAME, IE.REQUESTER_ID ,IE.ENTRY_DATE  , IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY,B.NAME,IE.SLIP_NUMBER FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD,BLOCK B WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IED.BLOCK_ID = B.BLOCK_ID and IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND TRUNC(IE.ENTRY_DATE) = TO_DATE('"+toDate+"', 'dd-MM-yy')";
					query = "SELECT  nvl(VD.ADDRESS,SC.ADDRESS) as ADDRESS,IE.VEHICLENO,IE.ISSUE_TYPE,IE.INDENT_ENTRY_ID,VD.VENDOR_NAME,IE.INDENT_TYPE,IE.RECEIVED_OR_ISSUED_DATE,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,IE.REQUESTER_NAME, IE.REQUESTER_ID ,IE.ENTRY_DATE , IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY,IE.SLIP_NUMBER,IE.TOTAL_AMOUNT,IED.MEASUR_MNT_NAME,IED.REMARKS,SED.EMP_NAME,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TAX,B.NAME,(F.NAME)AS FLAT_NAME,(FR.NAME)AS FLOOR_NAME,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,NVL(IED.EXPIRY_DATE, '-') as EXPIRY_DATE  "
						+ ",IE.WORK_ORDER_NUMBER,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where   WORK_ORDER_NUMBER =IE.WORK_ORDER_NUMBER and SITE_ID='"+siteId+"') as QS_WORKORDER_ISSUE_ID,IE.SITE_ID"		
						+ "	FROM INDENT_ENTRY IE 	LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  on SC.CONTRACTOR_ID=IE.CONTRACTOR_ID  LEFT  JOIN VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID ,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED "
							+ " LEFT OUTER JOIN BLOCK B on B.BLOCK_ID=IED.BLOCK_ID"
							+ " LEFT OUTER JOIN FLAT F on F.FLAT_ID=IED.FLAT_ID "
							+ " LEFT OUTER JOIN FLOOR FR on FR.FLOOR_ID=IED.FLOOR_ID  "
							+ " WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID and IE.USER_ID=SED.EMP_ID AND SPL.PRICE_ID=IED.PRICE_ID and IE.INDENT_TYPE IN ('OUT','OUTO','OUTT','OUTR','OUTS') AND IE.SITE_ID='"+siteId+"'  AND  TRUNC(IE.RECEIVED_OR_ISSUED_DATE) <= TO_DATE('"+toDate+"', 'dd-MM-yy') ";
				}
				
				 //ACP for get issue details
				 if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
						if (fromDate.equals("N/A") && toDate.equals("N/A")) {
							query = "SELECT  distinct(IE.INDENT_ENTRY_ID),nvl(VD.ADDRESS,SC.ADDRESS) as ADDRESS,IE.VEHICLENO,IE.ISSUE_TYPE,VD.VENDOR_NAME,IE.INDENT_TYPE,IE.RECEIVED_OR_ISSUED_DATE,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,IE.REQUESTER_NAME, IE.REQUESTER_ID ,IE.ENTRY_DATE , IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY,IE.SLIP_NUMBER,IE.TOTAL_AMOUNT,IED.MEASUR_MNT_NAME,IED.REMARKS,SED.EMP_NAME,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TAX,B.NAME,(F.NAME)AS FLAT_NAME,(FR.NAME)AS FLOOR_NAME,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,NVL(IED.EXPIRY_DATE, '-') as EXPIRY_DATE  "
									+ ",IE.WORK_ORDER_NUMBER,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where   WORK_ORDER_NUMBER =IE.WORK_ORDER_NUMBER and SITE_ID='"+siteId+"') as QS_WORKORDER_ISSUE_ID,IE.SITE_ID"		
									+ "	FROM INDENT_ENTRY IE 	LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  on SC.CONTRACTOR_ID=IE.CONTRACTOR_ID  LEFT  JOIN VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID ,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED "
										+ " LEFT OUTER JOIN BLOCK B on B.BLOCK_ID=IED.BLOCK_ID"
										+ " LEFT OUTER JOIN FLAT F on F.FLAT_ID=IED.FLAT_ID "
										+ " LEFT OUTER JOIN FLOOR FR on FR.FLOOR_ID=IED.FLOOR_ID  "
										+ " WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID and IE.USER_ID=SED.EMP_ID AND SPL.PRICE_ID=IED.PRICE_ID and IE.INDENT_TYPE IN ('OUT','OUTO','OUTT','OUTR','OUTS') AND IE.SITE_ID='"+siteId+"' AND IE.INDENT_ENTRY_ID='"+indentEntryId+"' ";

						}
					}
				 
				 if(val.equals("GetIssueDetails")){
					 
					 if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
						 query = "  SELECT  distinct(IE.INDENT_ENTRY_ID),IE.VEHICLENO,IE.ISSUE_TYPE,VD.VENDOR_NAME,IE.INDENT_TYPE,IE.RECEIVED_OR_ISSUED_DATE,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,IE.REQUESTER_NAME, IE.REQUESTER_ID ,IE.ENTRY_DATE ,IE.SLIP_NUMBER,IE.TOTAL_AMOUNT,SED.EMP_NAME "
							 	   + " ,IE.WORK_ORDER_NUMBER,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where   WORK_ORDER_NUMBER =IE.WORK_ORDER_NUMBER and SITE_ID='+siteId+') as QS_WORKORDER_ISSUE_ID,IE.SITE_ID "
							 	   + "	FROM INDENT_ENTRY IE 	LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  on SC.CONTRACTOR_ID=IE.CONTRACTOR_ID "
							 	   + "  LEFT  JOIN VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID ,SUMADHURA_EMPLOYEE_DETAILS SED "
							 	   + "	WHERE IE.USER_ID=SED.EMP_ID   and IE.INDENT_TYPE IN ('OUT','OUTO','OUTT','OUTR','OUTS') AND IE.SITE_ID='"+siteId+"'  AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') ";
						 
					 } else if (StringUtils.isNotBlank(fromDate)) {
						 query = "  SELECT  distinct(IE.INDENT_ENTRY_ID),IE.VEHICLENO,IE.ISSUE_TYPE,VD.VENDOR_NAME,IE.INDENT_TYPE,IE.RECEIVED_OR_ISSUED_DATE,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,IE.REQUESTER_NAME, IE.REQUESTER_ID ,IE.ENTRY_DATE ,IE.SLIP_NUMBER,IE.TOTAL_AMOUNT,SED.EMP_NAME "
							 	   + " ,IE.WORK_ORDER_NUMBER,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where   WORK_ORDER_NUMBER =IE.WORK_ORDER_NUMBER and SITE_ID='+siteId+') as QS_WORKORDER_ISSUE_ID,IE.SITE_ID "
							 	   + "	FROM INDENT_ENTRY IE 	LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  on SC.CONTRACTOR_ID=IE.CONTRACTOR_ID "
							 	   + "  LEFT  JOIN VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID ,SUMADHURA_EMPLOYEE_DETAILS SED "
							 	   + "	WHERE IE.USER_ID=SED.EMP_ID   and IE.INDENT_TYPE IN ('OUT','OUTO','OUTT','OUTR','OUTS') AND IE.SITE_ID='"+siteId+"'  AND  TRUNC(IE.RECEIVED_OR_ISSUED_DATE) = TO_DATE('"+fromDate+"', 'dd-MM-yy') ";

					 } else if(StringUtils.isNotBlank(toDate)) {
						 query = "  SELECT  distinct(IE.INDENT_ENTRY_ID),IE.VEHICLENO,IE.ISSUE_TYPE,VD.VENDOR_NAME,IE.INDENT_TYPE,IE.RECEIVED_OR_ISSUED_DATE,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,IE.REQUESTER_NAME, IE.REQUESTER_ID ,IE.ENTRY_DATE ,IE.SLIP_NUMBER,IE.TOTAL_AMOUNT,SED.EMP_NAME "
							 	   + " ,IE.WORK_ORDER_NUMBER,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where   WORK_ORDER_NUMBER =IE.WORK_ORDER_NUMBER and SITE_ID='+siteId+') as QS_WORKORDER_ISSUE_ID,IE.SITE_ID "
							 	   + "	FROM INDENT_ENTRY IE 	LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  on SC.CONTRACTOR_ID=IE.CONTRACTOR_ID "
							 	   + "  LEFT  JOIN VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID ,SUMADHURA_EMPLOYEE_DETAILS SED "
							 	   + "	WHERE IE.USER_ID=SED.EMP_ID   and IE.INDENT_TYPE IN ('OUT','OUTO','OUTT','OUTR','OUTS') AND IE.SITE_ID='"+siteId+"'  AND  TRUNC(IE.RECEIVED_OR_ISSUED_DATE) <= TO_DATE('"+toDate+"', 'dd-MM-yy')  ";

					 }
					}
				
				if(StringUtils.isNotBlank(blockId)){
					query= query+" AND IED.BLOCK_ID='"+blockId+"'";
				}
				
				if(StringUtils.isNotBlank(floorId)){
					query=query+" AND IED.FLOOR_ID='"+floorId+"'";
				}
				
				if(StringUtils.isNotBlank(flatId)){
					query=query+" AND IED.FLAT_ID='"+flatId+"'";
				}
			
				if(StringUtils.isNotBlank(issueType)){
					query=query+" AND IE.ISSUE_TYPE='"+issueType+"'";
				}
				if(StringUtils.isNotBlank(contractorId)){
					query=query+" AND IE.CONTRACTOR_ID='"+contractorId+"'";
				}
				query=query+" order by IE.RECEIVED_OR_ISSUED_DATE";
			}


			dbIndentDts = template.queryForList(query, new Object[]{});
			if (StringUtils.isNotBlank(val)&&!val.equals("GetIssueDetails")) {
				for(Map<String, Object> prods : dbIndentDts) {
					indentObj = new ViewIndentIssueDetailsBean();
					indentObj.setIndentEntryId(prods.get("INDENT_ENTRY_ID")==null ? "" : prods.get("INDENT_ENTRY_ID").toString());
					indentObj.setSiteId(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
					indentObj.setPoNo(prods.get("PO_ID")==null ? "" : prods.get("PO_ID").toString());
					
					indentObj.setIndentType(prods.get("INDENT_TYPE")==null ? "" : prods.get("INDENT_TYPE").toString());
					indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
					indentObj.setVendorId(prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
					indentObj.setUserId(prods.get("EMP_NAME")==null ? "" : prods.get("EMP_NAME").toString());
					indentObj.setRequesterName(prods.get("MEASUR_MNT_NAME")==null ? "" : prods.get("MEASUR_MNT_NAME").toString());
					strInvoiceNo = prods.get("INVOICE_ID")==null ? "" : prods.get("INVOICE_ID").toString();
					indentObj.setInvoiceNumber(strInvoiceNo);
					indentObj.setInvNoForHyperLink(strInvoiceNo.replace('&','@'));
					indentObj.setStrInvoiceDate(prods.get("INVOICE_DATE")==null ? "" : DateUtil.dateConversion(prods.get("INVOICE_DATE").toString()));
					indentObj.setProductName(prods.get("PRODUCT_NAME")==null ? "" : prods.get("PRODUCT_NAME").toString());
					indentObj.setSubProdName(prods.get("SUB_PRODUCT_NAME")==null ? "" : prods.get("SUB_PRODUCT_NAME").toString());
					indentObj.setChildProdName(prods.get("CHILD_PRODUCT_NAME")==null ? "" : prods.get("CHILD_PRODUCT_NAME").toString());
					double issueQTY = Double.valueOf(prods.get("RECEVED_QTY")==null ? "" : prods.get("RECEVED_QTY").toString());
					indentObj.setStrSlipNumber(prods.get("SLIP_NUMBER")==null ? "" : prods.get("SLIP_NUMBER").toString());
					
					
					double basicAmount = Double.valueOf(prods.get("BASIC_AMOUNT")==null ? "" : prods.get("BASIC_AMOUNT").toString());
					double taxAmount = Double.valueOf(prods.get("TAX_AMOUNT")==null ? "0" : prods.get("TAX_AMOUNT").toString());
					double otherCharges = Double.valueOf(prods.get("OTHER_CHARGES_AFTER_TAX")==null ? "0" : prods.get("OTHER_CHARGES_AFTER_TAX").toString());
					
					
					indentObj.setIssueQTY(format.format(issueQTY));
					indentObj.setBasicAmount(format.format(basicAmount));
					indentObj.setTaxAmount(format.format(taxAmount));
					indentObj.setOtherCharges(format.format(otherCharges));
					
					
					total=Double.valueOf(prods.get("AFTER_TAX_TOTAL")==null ? "0" : prods.get("AFTER_TAX_TOTAL").toString());
					indentObj.setTotalAmt(format.format(total));
					
					double doubleTotalAmount = Double.valueOf(prods.get("TOTAL_AMOUNT")==null ? "" : prods.get("TOTAL_AMOUNT").toString());
					String doubleTotalAmnt=format.format(doubleTotalAmount);
					indentObj.setStrTotalAmount(doubleTotalAmnt);


					String date=prods.get("RECEIVED_OR_ISSUED_DATE")==null ? "" : prods.get("RECEIVED_OR_ISSUED_DATE").toString();
					String expiryDate = prods.get("EXPIRY_DATE")==null ? "-" : prods.get("EXPIRY_DATE").toString();
					
					
					DateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					String convertreceive_time = "";
					
					
					try{

						Date receive_date = dt.parse(date);
					//	Date invoice_date = dt.parse(invoicedate);
						SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yy");
						SimpleDateFormat time1 = new SimpleDateFormat("HH:mm:ss");
						date = dt1.format(receive_date);
						if(expiryDate!=null && !expiryDate.equalsIgnoreCase("N/A") && !expiryDate.equalsIgnoreCase("NA") && !expiryDate.equals("-")){
							Date expiry_Date = DateUtil.convertToJavaDateFormat(expiryDate);
							expiryDate = dt1.format(expiry_Date);
						}
						//expiryDate = dt1.format(expiry_Date);
						
						convertreceive_time = time1.format(receive_date);
					//	invoicedate = dt1.format(invoice_date);
					
					}
					catch(Exception e){
						e.printStackTrace();
					}
					indentObj.setTime(convertreceive_time);
					
				/*	if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}*/
					indentObj.setReceivedDate(date);
					indentObj.setExpiryDate(expiryDate);
					list.add(indentObj);
				}
				dbIndentDts = template.queryForList(strDCFormQuery, new Object[]{});
				for(Map<String, Object> prods : dbIndentDts) {
					indentObj = new ViewIndentIssueDetailsBean();
					indentObj.setIndentEntryId(prods.get("INDENT_ENTRY_ID")==null ? "" : prods.get("INDENT_ENTRY_ID").toString());
					indentObj.setDcEntryId(prods.get("DC_ENTRY_ID")==null ? "" : prods.get("DC_ENTRY_ID").toString());
					indentObj.setSiteId(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
					indentObj.setPoNo(prods.get("PO_ID")==null ? "" : prods.get("PO_ID").toString());
					
					indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
					indentObj.setVendorId(prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
					indentObj.setUserId(prods.get("EMP_NAME")==null ? "" : prods.get("EMP_NAME").toString());
					indentObj.setRequesterName(prods.get("MEASUR_MNT_NAME")==null ? "" : prods.get("MEASUR_MNT_NAME").toString());
					strInvoiceNo = prods.get("INVOICE_ID")==null ? "" : prods.get("INVOICE_ID").toString();
					strDCNumber = prods.get("DC_NUMBER")==null ? "" : prods.get("DC_NUMBER").toString();
					
					indentObj.setInvoiceNumber(strInvoiceNo);
					indentObj.setDcNumber("DCNO_"+strDCNumber);
					indentObj.setInvNoForHyperLink(strInvoiceNo.replace('&','@'));
					indentObj.setDcNoForHyperLink(strDCNumber.replace('&','@'));
					
					
					indentObj.setStrInvoiceDate(prods.get("INVOICE_DATE")==null ? "" : DateUtil.dateConversion(prods.get("INVOICE_DATE").toString()));
					indentObj.setStrDcDate(prods.get("DC_DATE")==null ? "" : DateUtil.dateConversion(prods.get("DC_DATE").toString()));
					
					indentObj.setProductName(prods.get("PRODUCT_NAME")==null ? "" : prods.get("PRODUCT_NAME").toString());
					indentObj.setSubProdName(prods.get("SUB_PRODUCT_NAME")==null ? "" : prods.get("SUB_PRODUCT_NAME").toString());
					indentObj.setChildProdName(prods.get("CHILD_PRODUCT_NAME")==null ? "" : prods.get("CHILD_PRODUCT_NAME").toString());
					double issueQTY = Double.valueOf(prods.get("RECEVED_QTY")==null ? "" : prods.get("RECEVED_QTY").toString());
					indentObj.setStrSlipNumber(prods.get("SLIP_NUMBER")==null ? "" : prods.get("SLIP_NUMBER").toString());
					
					double basicAmount = Double.valueOf(prods.get("BASIC_AMOUNT")==null ? "" : prods.get("BASIC_AMOUNT").toString());
					double taxAmount = Double.valueOf(prods.get("TAX_AMOUNT")==null ? "0" : prods.get("TAX_AMOUNT").toString());
					double otherCharges = Double.valueOf(prods.get("OTHER_CHARGES_AFTER_TAX")==null ? "0" : prods.get("OTHER_CHARGES_AFTER_TAX").toString());
					
					indentObj.setIssueQTY(format.format(issueQTY));
					indentObj.setBasicAmount(format.format(basicAmount));
					indentObj.setTaxAmount(format.format(taxAmount));
					indentObj.setOtherCharges(format.format(otherCharges));
					
					total=Double.valueOf(prods.get("AFTER_TAX_TOTAL")==null ? "" : prods.get("AFTER_TAX_TOTAL").toString());
					indentObj.setTotalAmt(format.format(total));
					
					double doubleTotalAmount = Double.valueOf(prods.get("TOTAL_AMOUNT")==null ? "" : prods.get("TOTAL_AMOUNT").toString());
					String doubleTotalAmnt=format.format(doubleTotalAmount);
					indentObj.setStrTotalAmount(doubleTotalAmnt);
					
					String date=prods.get("RECEIVED_OR_ISSUED_DATE")==null ? "" : prods.get("RECEIVED_OR_ISSUED_DATE").toString();
					String expiryDate = prods.get("EXPIRY_DATE")==null ? "-" : prods.get("EXPIRY_DATE").toString();
					Date expiry_Date = DateUtil.convertToJavaDateFormat(expiryDate);
					
					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					String convertreceive_time = "";
					
					try{

						Date receive_date = dt.parse(date);
					//	Date invoice_date = dt.parse(invoicedate);
						SimpleDateFormat dt1 = new SimpleDateFormat("dd-MMM-yy");
						SimpleDateFormat time1 = new SimpleDateFormat("HH:mm:ss");
						date = dt1.format(receive_date);
						expiryDate = dt1.format(expiry_Date);
						convertreceive_time = time1.format(receive_date);
					//	invoicedate = dt1.format(invoice_date);
					
					}
					catch(Exception e){
						e.printStackTrace();
					}
					indentObj.setTime(convertreceive_time);

					
				/*	if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}*/
					indentObj.setReceivedDate(date);
					indentObj.setExpiryDate(expiryDate);
					list.add(indentObj);
				}


			} else {
				for(Map<String, Object> prods : dbIndentDts) {
					indentObj = new ViewIndentIssueDetailsBean();
					sNo++;
					indentObj.setSerialNo(sNo);
					indentObj.setUserId(prods.get("USERNAME")==null ? "" : prods.get("USERNAME").toString());
					indentObj.setRequesterName(prods.get("REQUESTER_NAME")==null ? "-" : prods.get("REQUESTER_NAME").toString());
					indentObj.setRequesterId(prods.get("REQUESTER_ID")==null ? "-" : prods.get("REQUESTER_ID").toString());
					indentObj.setProductName(prods.get("PRODUCT_NAME")==null ? "-" : prods.get("PRODUCT_NAME").toString());
					indentObj.setSubProdName(prods.get("SUB_PRODUCT_NAME")==null ? "-" : prods.get("SUB_PRODUCT_NAME").toString());
					indentObj.setChildProdName(prods.get("CHILD_PRODUCT_NAME")==null ? "-" : prods.get("CHILD_PRODUCT_NAME").toString());
					indentObj.setBlock_Name(prods.get("NAME")==null ? "-" : prods.get("NAME").toString());
					indentObj.setFloorId(prods.get("FLOOR_ID")==null ? "-" : prods.get("FLOOR_ID").toString());
					double issueQuantity=Double.parseDouble(prods.get("ISSUED_QTY")==null ? "0" : prods.get("ISSUED_QTY").toString());
				//	indentObj.setIssueQTY(prods.get("ISSUED_QTY")==null ? "" : prods.get("ISSUED_QTY").toString());
					indentObj.setStrSlipNumber(prods.get("SLIP_NUMBER")==null ? "-" : prods.get("SLIP_NUMBER").toString());
					//String contractor=prods.get("CONTRACTOR_NAME")==null ? "-" : prods.get("CONTRACTOR_NAME").toString();
					String vendorName=prods.get("VENDOR_NAME")==null ? "-" : prods.get("VENDOR_NAME").toString();
					indentObj.setVendorName(vendorName);
					String contractor=prods.get("CONTRACTOR_NAME")==null ?(vendorName): prods.get("CONTRACTOR_NAME").toString().trim();
					
					indentObj.setStrTotalAmount(prods.get("TOTAL_AMOUNT")==null ? "0" : prods.get("TOTAL_AMOUNT").toString());
					String date=prods.get("RECEIVED_OR_ISSUED_DATE")==null ? "" : prods.get("RECEIVED_OR_ISSUED_DATE").toString();
					indentObj.setMeasurementName(prods.get("MEASUR_MNT_NAME")==null ? "-" : prods.get("MEASUR_MNT_NAME").toString());
					indentObj.setRemarks(prods.get("REMARKS")==null ? "-" : prods.get("REMARKS").toString());
					indentObj.setIssuerName(prods.get("EMP_NAME")==null ? "-" : prods.get("EMP_NAME").toString());
				//	indentObj.setBlock_Number(prods.get("BLOCK_ID")==null ? "-" : prods.get("BLOCK_ID").toString());
					indentObj.setFlat_Name(prods.get("FLAT_NAME")==null ? "-" : prods.get("FLAT_NAME").toString());
					indentObj.setFloor_Name(prods.get("FLOOR_NAME")==null ? "-" : prods.get("FLOOR_NAME").toString());
					indentObj.setExpiryDate(prods.get("EXPIRY_DATE")==null ? "" : prods.get("EXPIRY_DATE").toString());
					indentObj.setSiteId(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());

					//ACP
					indentObj.setVehileNo(prods.get("VEHICLENO")==null ? "-" : prods.get("VEHICLENO").toString());
					indentObj.setIssueType(prods.get("ISSUE_TYPE")==null ? "-" : prods.get("ISSUE_TYPE").toString());
					indentObj.setIndentEntryId(prods.get("INDENT_ENTRY_ID")==null ? "-" : prods.get("INDENT_ENTRY_ID").toString());
					indentObj.setIndentType(prods.get("INDENT_TYPE")==null ? "-" : prods.get("INDENT_TYPE").toString());
					indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "-" : prods.get("VENDOR_NAME").toString());
					indentObj.setContractorAddress(prods.get("ADDRESS")==null ? "" : prods.get("ADDRESS").toString());
					
					workOrderNumber=(prods.get("WORK_ORDER_NUMBER")==null ? "" : prods.get("WORK_ORDER_NUMBER").toString());
					indentObj.setWorkOrderIssueId(prods.get("QS_WORKORDER_ISSUE_ID")==null ? "-" : prods.get("QS_WORKORDER_ISSUE_ID").toString());
					
					amtafterTaxPerUnit=Double.parseDouble((prods.get("AMOUNT_PER_UNIT_AFTER_TAXES")==null ? "0" : prods.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString()));
					//indentObj.setAmountAfterTaxPerUnit
					double price=Double.parseDouble(prods.get("AMOUNT_PER_UNIT_BEFORE_TAXES")==null ? "0" : prods.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
					double tax=Double.parseDouble(prods.get("TAX")==null ? "0" : prods.get("TAX").toString());
					double basicAmt=(price)*(issueQuantity);
					String strBasicAmount = String.format ("%.2f", basicAmt);
					double basicAmount=Double.parseDouble(strBasicAmount);
					double taxAmt=basicAmount*(tax/100);
					//double basicAmtWithTax=
					double basicAmtAfterTax=basicAmount+taxAmt;
					double strTotalAfterTax=(issueQuantity)*(amtafterTaxPerUnit);
					String strBasicAmtAfterTax=String.format ("%.2f", basicAmtAfterTax);
					String strFinalAmt=String.format ("%.2f", strTotalAfterTax);
					String strIssueQuantity=String.format("%.2f",issueQuantity);
					indentObj.setIssueQTY(format.format(Double.valueOf(strIssueQuantity)).replaceAll("Rs",""));
					indentObj.setBasicAmount(format.format(Double.valueOf(strBasicAmount)).replaceAll("Rs",""));
					indentObj.setBasicAmtAfterTax(format.format(Double.valueOf(strBasicAmtAfterTax)).replaceAll("Rs",""));
					//indentObj.setTotalAmt(strFinalAmt);
					indentObj.setTotalAmt(format.format(Double.valueOf(strFinalAmt)).replaceAll("Rs",""));
					
					//String doubleTotalAmount = String.format("%,d", numberAsString);
					if(workOrderNumber.equals("0")){
						workOrderNumber="";
					}
					if(contractor.length()==0){
						contractor="-";
					}
					indentObj.setWorkOrderNumber(workOrderNumber);
					indentObj.setContractorName(contractor);
					
					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					
					String convertreceive_time = "";
					
					try{

						Date receive_date = dt.parse(date);
					//	Date invoice_date = dt.parse(invoicedate);
						SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
						SimpleDateFormat time1 = new SimpleDateFormat("HH:mm:ss");
						date = dt1.format(receive_date);
						convertreceive_time = time1.format(receive_date);
					//	invoicedate = dt1.format(invoice_date);
					
					}
					catch(Exception e){
						e.printStackTrace();
					}
					indentObj.setTime(convertreceive_time);

					
					
					
					/*if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}*/
					indentObj.setEntryDate(date);
					list.add(indentObj);
				}
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
	

	public Object getGetIssuedDetailsLists(String indentEntryId, String siteId, String indentType, String string) {
		JdbcTemplate template;
		//ACP
	
		
		List<ViewIndentIssueDetailsBean> list = new ArrayList<ViewIndentIssueDetailsBean>();
		
		com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
	
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
	
		
		String query = "SELECT  IED.IS_RECOVERABLE,IED.UORF,IE.INDENT_ENTRY_ID,IE.VEHICLENO,IE.ISSUE_TYPE,VD.VENDOR_NAME,IE.INDENT_TYPE,IE.RECEIVED_OR_ISSUED_DATE,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,IE.REQUESTER_NAME, IE.REQUESTER_ID ,IE.ENTRY_DATE , IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY,IE.SLIP_NUMBER,IE.TOTAL_AMOUNT,IED.MEASUR_MNT_NAME,IED.REMARKS,SED.EMP_NAME,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TAX,B.NAME,(F.NAME)AS FLAT_NAME,(FR.NAME)AS FLOOR_NAME,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,NVL(IED.EXPIRY_DATE, '-') as EXPIRY_DATE  "
				+ ",IE.WORK_ORDER_NUMBER,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where   WORK_ORDER_NUMBER =IE.WORK_ORDER_NUMBER and SITE_ID='"+siteId+"') as QS_WORKORDER_ISSUE_ID,IE.SITE_ID"		
				+ "	FROM INDENT_ENTRY IE 	LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  on SC.CONTRACTOR_ID=IE.CONTRACTOR_ID  LEFT  JOIN VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID ,SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED "
					+ " LEFT OUTER JOIN BLOCK B on B.BLOCK_ID=IED.BLOCK_ID"
					+ " LEFT OUTER JOIN FLAT F on F.FLAT_ID=IED.FLAT_ID "
					+ " LEFT OUTER JOIN FLOOR FR on FR.FLOOR_ID=IED.FLOOR_ID  "
					+ " WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID and IE.USER_ID=SED.EMP_ID AND SPL.PRICE_ID=IED.PRICE_ID and IE.INDENT_TYPE IN ('OUT','OUTO','OUTT','OUTR','OUTS') AND IE.SITE_ID='"+siteId+"' AND IE.INDENT_ENTRY_ID='"+indentEntryId+"' ";

		query=query+" order by IE.RECEIVED_OR_ISSUED_DATE,IED.PRODUCT_NAME";
		
		List<Map<String, Object>> 		dbIndentDts = template.queryForList(query, new Object[]{});
		int sNo=0;
		for(Map<String, Object> prods : dbIndentDts) {
			ViewIndentIssueDetailsBean indentObj = new ViewIndentIssueDetailsBean();
			sNo++;
			indentObj.setSerialNo(sNo);
			indentObj.setUserId(prods.get("USERNAME")==null ? "" : prods.get("USERNAME").toString());
			indentObj.setRequesterName(prods.get("REQUESTER_NAME")==null ? "-" : prods.get("REQUESTER_NAME").toString());
			indentObj.setRequesterId(prods.get("REQUESTER_ID")==null ? "-" : prods.get("REQUESTER_ID").toString());
			indentObj.setProductName(prods.get("PRODUCT_NAME")==null ? "-" : prods.get("PRODUCT_NAME").toString());
			indentObj.setSubProdName(prods.get("SUB_PRODUCT_NAME")==null ? "-" : prods.get("SUB_PRODUCT_NAME").toString());
			indentObj.setChildProdName(prods.get("CHILD_PRODUCT_NAME")==null ? "-" : prods.get("CHILD_PRODUCT_NAME").toString());
			indentObj.setBlock_Name(prods.get("NAME")==null ? "-" : prods.get("NAME").toString());
			indentObj.setFloorId(prods.get("FLOOR_ID")==null ? "-" : prods.get("FLOOR_ID").toString());
			double issueQuantity=Double.parseDouble(prods.get("ISSUED_QTY")==null ? "0" : prods.get("ISSUED_QTY").toString());
		//	indentObj.setIssueQTY(prods.get("ISSUED_QTY")==null ? "" : prods.get("ISSUED_QTY").toString());
			indentObj.setStrSlipNumber(prods.get("SLIP_NUMBER")==null ? "-" : prods.get("SLIP_NUMBER").toString());
			String contractor=prods.get("CONTRACTOR_NAME")==null ? "-" : prods.get("CONTRACTOR_NAME").toString();
			
			String strIssueQuantity=String.format("%.2f",issueQuantity);
			indentObj.setIssueQTY(format.format(Double.valueOf(strIssueQuantity)).replaceAll("Rs",""));
			
			indentObj.setContractorName(contractor);
			indentObj.setStrTotalAmount(prods.get("TOTAL_AMOUNT")==null ? "0" : prods.get("TOTAL_AMOUNT").toString());
			String date=prods.get("RECEIVED_OR_ISSUED_DATE")==null ? "" : prods.get("RECEIVED_OR_ISSUED_DATE").toString();
			indentObj.setMeasurementName(prods.get("MEASUR_MNT_NAME")==null ? "-" : prods.get("MEASUR_MNT_NAME").toString());
			indentObj.setRemarks(prods.get("REMARKS")==null ? "-" : prods.get("REMARKS").toString());
			indentObj.setIssuerName(prods.get("EMP_NAME")==null ? "-" : prods.get("EMP_NAME").toString());
		//	indentObj.setBlock_Number(prods.get("BLOCK_ID")==null ? "-" : prods.get("BLOCK_ID").toString());
			indentObj.setFlat_Name(prods.get("FLAT_NAME")==null ? "-" : prods.get("FLAT_NAME").toString());
			indentObj.setFloor_Name(prods.get("FLOOR_NAME")==null ? "-" : prods.get("FLOOR_NAME").toString());
			indentObj.setExpiryDate(prods.get("EXPIRY_DATE")==null ? "" : prods.get("EXPIRY_DATE").toString());
			indentObj.setSiteId(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());

			//ACP
			indentObj.setVehileNo(prods.get("VEHICLENO")==null ? "-" : prods.get("VEHICLENO").toString());
			indentObj.setIssueType(prods.get("ISSUE_TYPE")==null ? "-" : prods.get("ISSUE_TYPE").toString());
			indentObj.setIndentEntryId(prods.get("INDENT_ENTRY_ID")==null ? "-" : prods.get("INDENT_ENTRY_ID").toString());
			indentObj.setIndentType(prods.get("INDENT_TYPE")==null ? "-" : prods.get("INDENT_TYPE").toString());
			indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "-" : prods.get("VENDOR_NAME").toString());
		
	//		workOrderNumber=(prods.get("WORK_ORDER_NUMBER")==null ? "0" : prods.get("WORK_ORDER_NUMBER").toString());
			indentObj.setWorkOrderIssueId(prods.get("QS_WORKORDER_ISSUE_ID")==null ? "-" : prods.get("QS_WORKORDER_ISSUE_ID").toString());
			
			indentObj.setIsRecoverable(prods.get("IS_RECOVERABLE")==null ? "-" : prods.get("IS_RECOVERABLE").toString());
			indentObj.setUORF(prods.get("UORF")==null ? "-" : prods.get("UORF").toString());
			
			indentObj.setEntryDate(date);
			list.add(indentObj);
		}
	
		} catch (NamingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return list;
	}
	

	//29-07-17 written by Madhu start
	@Override
	public List<IndentIssueDto>  getPriceListDetails(IndentIssueDto issueDto, String siteId) {

		String sql = "";
		List<Map<String, Object>> dbProductsList = null;
		List<IndentIssueDto> allDetails = null;
		IndentIssueDto issueDetails = null;
		//added unit of mesurment not there in uat but ther in my code
		sql = "select * from( SELECT SPL.PRICE_ID,SPL.PRODUCT_ID,SPL.SUB_PRODUCT_ID,SPL.CHILD_PRODUCT_ID,SPL.UNITS_OF_MEASUREMENT,SPL.AVAILABLE_QUANTITY,SPL.AMOUNT_PER_UNIT_AFTER_TAXES, "
			  +" IED.EXPIRY_DATE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TAX,SPL.HSN_CODE,SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG, SPL.RECEVED_QTY,SPL.OTHER_CHARGES_AFTER_TAX FROM SUMADHURA_PRICE_LIST SPL "
			  +" LEFT OUTER JOIN INDENT_ENTRY_DETAILS IED ON IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID "
			  +" WHERE SPL.PRODUCT_ID=? AND SPL.SUB_PRODUCT_ID=? AND SPL.CHILD_PRODUCT_ID=? AND SPL.UNITS_OF_MEASUREMENT=? AND SPL.SITE_ID=? AND SPL.STATUS='A' "
			  +" AND TRUNC(SPL.CREATED_DATE) < to_date('"+issueDto.getDate()+"', 'dd-MM-yy')  ORDER BY SPL.CREATED_DATE ASC ) where  ROWNUM <=1";
		logger.info("Issue "+sql);
		dbProductsList = jdbcTemplate.queryForList(sql, new Object[] {issueDto.getProdId(),issueDto.getSubProdId(), issueDto.getChildProdId(), issueDto.getMeasurementId(), siteId});
		allDetails = new ArrayList<IndentIssueDto>();
		if (null == dbProductsList || dbProductsList.size() == 0){
			sql = "select * from( SELECT SPL.PRICE_ID,SPL.PRODUCT_ID,SPL.SUB_PRODUCT_ID,SPL.CHILD_PRODUCT_ID,SPL.UNITS_OF_MEASUREMENT,SPL.AVAILABLE_QUANTITY,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,IED.EXPIRY_DATE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TAX,SPL.HSN_CODE,SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG, SPL.RECEVED_QTY,SPL.OTHER_CHARGES_AFTER_TAX  "
				+" FROM SUMADHURA_PRICE_LIST SPL "
				+" LEFT OUTER JOIN INDENT_ENTRY_DETAILS IED ON IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID "
				+" WHERE SPL.PRODUCT_ID=? AND SPL.SUB_PRODUCT_ID=? AND SPL.CHILD_PRODUCT_ID=? AND SPL.UNITS_OF_MEASUREMENT=? AND SPL.SITE_ID=? AND SPL.STATUS='A' "
				+" AND TRUNC(SPL.CREATED_DATE) <= to_date('"+issueDto.getDate()+"', 'dd-MM-yy') ORDER BY SPL.CREATED_DATE ASC ) where  ROWNUM <=1 ";

			logger.info("Issue "+sql);//not there in uat issueDto.getMeasurementId() but exists in my code
			dbProductsList = jdbcTemplate.queryForList(sql, new Object[] {issueDto.getProdId(),issueDto.getSubProdId(), issueDto.getChildProdId(), issueDto.getMeasurementId(), siteId});
		}
		for(Map<String, Object> prods : dbProductsList) {

			issueDetails = new IndentIssueDto();
			issueDetails.setPriceId(prods.get("PRICE_ID")==null ? "" : prods.get("PRICE_ID").toString());
			issueDetails.setProdId(prods.get("PRODUCT_ID")==null ? "" : prods.get("PRODUCT_ID").toString());
			issueDetails.setSubProdId(prods.get("SUB_PRODUCT_ID")==null ? "" : prods.get("SUB_PRODUCT_ID").toString());
			issueDetails.setChildProdId(prods.get("CHILD_PRODUCT_ID")==null ? "" : prods.get("CHILD_PRODUCT_ID").toString());
			issueDetails.setMeasurementId(prods.get("UNITS_OF_MEASUREMENT")==null ? "" : prods.get("UNITS_OF_MEASUREMENT").toString());
			issueDetails.setQuantity(prods.get("AVAILABLE_QUANTITY")==null ? "" : prods.get("AVAILABLE_QUANTITY").toString());
			issueDetails.setAmount(prods.get("AMOUNT_PER_UNIT_AFTER_TAXES")==null ? "" : prods.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString());
			issueDetails.setExpiryDate(prods.get("EXPIRY_DATE")==null ? "N/A" : prods.get("EXPIRY_DATE").toString());
			issueDetails.setHsnCd(prods.get("HSN_CODE")==null ? "N/A" : prods.get("HSN_CODE").toString());
			
			//ACP
			issueDetails.setAmountPerUnitBeforeTax(prods.get("AMOUNT_PER_UNIT_BEFORE_TAXES")==null ? "1" : prods.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
			issueDetails.setTax(prods.get("TAX")==null ? "0" : prods.get("TAX").toString());
			issueDetails.setOtherOrTransportCharges(prods.get("OTHER_CHARGES")==null ? "" : prods.get("OTHER_CHARGES").toString());
			issueDetails.setTaxotherOrTransportCharges(prods.get("TAX_ON_OTHER_TRANSPORT_CHG")==null ? "" : prods.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
			issueDetails.setReceivedQuantity(prods.get("RECEVED_QTY")==null ? "0" : prods.get("RECEVED_QTY").toString());
			issueDetails.setAmountAfterTaxotherOrTransportCharges(prods.get("OTHER_CHARGES_AFTER_TAX")==null ? "0" : prods.get("OTHER_CHARGES_AFTER_TAX").toString());
			
			allDetails.add(issueDetails);
		}


		return allDetails;
	}

	@Override
	public List<IndentIssueDto>  getPriceListDetails(IndentIssueDto issueDto, String siteId, String All) {

		String sql = "";
		List<Map<String, Object>> dbProductsList = null;
		List<IndentIssueDto> allDetails = null;
		IndentIssueDto issueDetails = null;
		try {
			sql = "SELECT SPL.PRICE_ID,SPL.PRODUCT_ID,SPL.SUB_PRODUCT_ID,SPL.CHILD_PRODUCT_ID,SPL.UNITS_OF_MEASUREMENT,SPL.AVAILABLE_QUANTITY,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,IED.EXPIRY_DATE,SPL.HSN_CODE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TAX,SPL.OTHER_CHARGES,SPL.TAX_ON_OTHER_TRANSPORT_CHG, SPL.RECEVED_QTY,SPL.OTHER_CHARGES_AFTER_TAX  "
				+" FROM SUMADHURA_PRICE_LIST SPL "
				+" LEFT OUTER JOIN INDENT_ENTRY_DETAILS IED ON IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID "
				+" WHERE SPL.PRODUCT_ID=? AND SPL.SUB_PRODUCT_ID=? AND SPL.CHILD_PRODUCT_ID=? AND SPL.UNITS_OF_MEASUREMENT=? AND SPL.SITE_ID=? "
				+" AND SPL.STATUS='A' ORDER BY SPL.CREATED_DATE,PRICE_ID ASC";


			dbProductsList = jdbcTemplate.queryForList(sql, new Object[] {issueDto.getProdId(),issueDto.getSubProdId(), issueDto.getChildProdId(), issueDto.getMeasurementId(), siteId});
			allDetails = new ArrayList<IndentIssueDto>();
			for(Map<String, Object> prods : dbProductsList) {
				issueDetails = new IndentIssueDto();
				issueDetails.setPriceId(prods.get("PRICE_ID")==null ? "" : prods.get("PRICE_ID").toString());
				issueDetails.setProdId(prods.get("PRODUCT_ID")==null ? "" : prods.get("PRODUCT_ID").toString());
				issueDetails.setSubProdId(prods.get("SUB_PRODUCT_ID")==null ? "" : prods.get("SUB_PRODUCT_ID").toString());
				issueDetails.setChildProdId(prods.get("CHILD_PRODUCT_ID")==null ? "" : prods.get("CHILD_PRODUCT_ID").toString());
				issueDetails.setMeasurementId(prods.get("UNITS_OF_MEASUREMENT")==null ? "" : prods.get("UNITS_OF_MEASUREMENT").toString());
				issueDetails.setQuantity(prods.get("AVAILABLE_QUANTITY")==null ? "" : prods.get("AVAILABLE_QUANTITY").toString());
				issueDetails.setAmount(prods.get("AMOUNT_PER_UNIT_AFTER_TAXES")==null ? "" : prods.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString());
				issueDetails.setExpiryDate(prods.get("EXPIRY_DATE")==null ? "N/A" : prods.get("EXPIRY_DATE").toString());
				
				//ACP
				issueDetails.setHsnCd(prods.get("HSN_CODE")==null ? "N/A" : prods.get("HSN_CODE").toString());
				issueDetails.setAmountPerUnitBeforeTax(prods.get("AMOUNT_PER_UNIT_BEFORE_TAXES")==null ? "1" : prods.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
				issueDetails.setTax(prods.get("TAX")==null ? "0" : prods.get("TAX").toString());
				issueDetails.setOtherOrTransportCharges(prods.get("OTHER_CHARGES")==null ? "" : prods.get("OTHER_CHARGES").toString());
				issueDetails.setTaxotherOrTransportCharges(prods.get("TAX_ON_OTHER_TRANSPORT_CHG")==null ? "" : prods.get("TAX_ON_OTHER_TRANSPORT_CHG").toString());
				issueDetails.setReceivedQuantity(prods.get("RECEVED_QTY")==null ? "0" : prods.get("RECEVED_QTY").toString());
				issueDetails.setAmountAfterTaxotherOrTransportCharges(prods.get("OTHER_CHARGES_AFTER_TAX")==null ? "0" : prods.get("OTHER_CHARGES_AFTER_TAX").toString());				
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
	public void updatePriceListDetails(String quantity, String status, String priceId) {
		String query = "UPDATE SUMADHURA_PRICE_LIST SET AVAILABLE_QUANTITY=?, STATUS=?,UPDATED_DATE=SYSDATE  WHERE PRICE_ID=? ";
		double quantity1=Double.parseDouble(new DecimalFormat("##.##").format(Double.valueOf(quantity)));
		log.info("Qunatity :"+quantity1);
		
		jdbcTemplate.update(query, new Object[] { quantity1, status, priceId });

	}

	/*@Override
	public void updateSumadhuraPriceListIndentEntryDetailsId(String priceId, String indentEntryDetailsId) {

		String query = "UPDATE SUMADHURA_PRICE_LIST SET INDENT_ENTRY_DETAILS_ID=? WHERE PRICE_ID=? ";
		jdbcTemplate.update(query, new Object[] {indentEntryDetailsId, priceId });

	}*/
	public static <K, V> Map<K, V> printMap(Map<K, V> map) {
		Map<K, V> mapObje = new TreeMap<K, V>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			mapObje.put( entry.getKey(), entry.getValue());

		}

		return mapObje;
	}
	//29-07-17 written by Madhu end

	@Override
	public void updateIssueDetailsIntoSumadhuraCloseBalance(IndentIssueDto issueDto, String strSiteId, String priceId, Double IssueQty) {

		String query = "";
		Double quantity = 0.0;
		Double totalAmt = 0.0;
		Double totalAmtForOneQty = 0.0;

		List<Map<String, Object>> dbClosingBalancesList = null;

		query = "SELECT CLOSING_BALANCE_ID,QUANTITY, TOTAL_AMOUNT FROM SUMADHURA_CLOSING_BALANCE WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) = TO_DATE('"+ issueDto.getDate() + "', 'dd-MM-yy') AND ROWNUM <= 1 ORDER BY DATE_AND_TIME  DESC";
		dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] { issueDto.getProdId(), issueDto.getSubProdId(), issueDto.getChildProdId(), strSiteId, issueDto.getMeasurementId() });

		if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {

			logger.info("Indent Issue Close balance query " + query);
			logger.info("Indent Issue Close balance dbClosingBalancesList " + dbClosingBalancesList);
			logger.info("totalAmtForOneQty  " + totalAmtForOneQty);
			quantity = Double.valueOf(quantity - IssueQty);
			totalAmtForOneQty = (IssueQty * Double.valueOf(priceId));

			query = "UPDATE SUMADHURA_CLOSING_BALANCE SET QUANTITY=QUANTITY-'" + IssueQty + "', TOTAL_AMOUNT= TOTAL_AMOUNT-'" + totalAmtForOneQty + "' WHERE DATE_AND_TIME >= '" + issueDto.getDate() + "' AND PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE=? AND MEASUREMENT_ID = ?";
			jdbcTemplate.update(query, new Object[] { issueDto.getProdId(), issueDto.getSubProdId(), issueDto.getChildProdId(), strSiteId, issueDto.getMeasurementId() });

		} else {
			query = "SELECT CLOSING_BALANCE_ID,QUANTITY, TOTAL_AMOUNT FROM SUMADHURA_CLOSING_BALANCE WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE=? AND MEASUREMENT_ID = ? AND TRUNC(DATE_AND_TIME) <= TO_DATE('" + issueDto.getDate() + "', 'dd-MM-yy') AND ROWNUM <= 1 ORDER BY DATE_AND_TIME  DESC";
			dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] { issueDto.getProdId(), issueDto.getSubProdId(), issueDto.getChildProdId(), strSiteId, issueDto.getMeasurementId() });
			if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {
				for (Map<String, Object> prods : dbClosingBalancesList) {

					quantity = Double.parseDouble(prods.get("QUANTITY") == null ? "" : prods.get("QUANTITY").toString());
					totalAmt = Double.parseDouble(prods.get("TOTAL_AMOUNT") == null ? "" : prods.get("TOTAL_AMOUNT").toString());
				}
				logger.info("Indent Issue Close balance query " + query);
				logger.info("Indent Issue Close balance dbClosingBalancesList " + dbClosingBalancesList);
				logger.info("totalAmtForOneQty  " + totalAmtForOneQty);
				quantity = Double.valueOf(quantity - IssueQty);
				totalAmtForOneQty = totalAmt - (IssueQty * Double.valueOf(priceId));

				query = "INSERT INTO SUMADHURA_CLOSING_BALANCE (CLOSING_BALANCE_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MEASUREMENT_ID,QUANTITY,SITE,TOTAL_AMOUNT,DATE_AND_TIME) VALUES (SUMADHURA_CLOSING_BALANCE_SEQ.NEXTVAL,?, ?, ?, ?, ?,?,?,?)";
				jdbcTemplate.update(query, new Object[] { issueDto.getProdId(), issueDto.getSubProdId(), issueDto.getChildProdId(), issueDto.getMeasurementId(), quantity, strSiteId, totalAmtForOneQty, issueDto.getDate() });

				query = "UPDATE SUMADHURA_CLOSING_BALANCE SET QUANTITY=QUANTITY-'" + IssueQty + "', TOTAL_AMOUNT= TOTAL_AMOUNT-'" + totalAmtForOneQty + "' WHERE DATE_AND_TIME > '" + issueDto.getDate() + "' AND PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE=? AND MEASUREMENT_ID = ?";
				jdbcTemplate.update(query, new Object[] { issueDto.getProdId(), issueDto.getSubProdId(), issueDto.getChildProdId(), strSiteId, issueDto.getMeasurementId() });

			}
		}
	}

	@Override
	public void updateIssueDetailsSumadhuClosingBalByProduct(IndentIssueDto issueDto, String strSiteId, String priceId, Double IssueQty) {

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
			dbClosingBalancesList = jdbcTemplate.queryForList(query, new Object[] { issueDto.getProdId(), issueDto.getSubProdId(), issueDto.getChildProdId(), strSiteId, issueDto.getMeasurementId() });

			if (null != dbClosingBalancesList && dbClosingBalancesList.size() > 0) {


				quantity = Double.valueOf(quantity - IssueQty);
				totalAmtForOneQty = (IssueQty * Double.valueOf(priceId));

				query = "UPDATE SUMADHU_CLOSING_BAL_BY_PRODUCT SET QUANTITY=QUANTITY-'" + IssueQty + "', TOTAL_AMOUNT= TOTAL_AMOUNT-'" + totalAmtForOneQty + "' WHERE DATE_AND_TIME >= '" + issueDto.getDate() + "' AND PRODUCT_ID=? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND MEASUREMENT_ID = ?";
				jdbcTemplate.update(query, new Object[] { issueDto.getProdId(), issueDto.getSubProdId(), issueDto.getChildProdId(), strSiteId, issueDto.getMeasurementId() });
			} 
		} 
	}
	public int getIssuesCount(String strSlipNo,String strSiteId,String strReqMonthStart,String strReqDate) {

		int intIssueCount = 0;

		String strSql = "select count(1) from  INDENT_ENTRY where SLIP_NUMBER = ? and SITE_ID = ? and ENTRY_DATE between ? and ?";

		intIssueCount = jdbcTemplate.queryForInt(strSql,new Object []{strSlipNo,strSiteId,strReqMonthStart,strReqDate});

		return intIssueCount;
	}

	public int getIssuesCount1(String strSlipNumber, String strSiteId, String strReqDate) {
		int indent_entry_id = 0;

		try{
			String strSql = "select INDENT_ENTRY_ID from  INDENT_ENTRY where SLIP_NUMBER = ? and SITE_ID = ?   and TRUNC(ENTRY_DATE) = to_date(?,'dd-MM-yy')";

			indent_entry_id = jdbcTemplate.queryForInt(strSql,new Object []{strSlipNumber,strSiteId,strReqDate});



		}catch(Exception e){
			indent_entry_id = 0;
			//e.printStackTrace();
		}
		return indent_entry_id;
	}


	@Override
	public String getContractorInfo(String contractorName) {
		//System.out.println("hai");
		StringBuffer sb = null;
		List<Map<String, Object>> dbContractorList = null;		


		contractorName = contractorName.replace("$$", "&");
		contractorName = contractorName.toUpperCase();

		log.debug("Contractor Name = "+contractorName);

		sb = new StringBuffer();

		String contractorInfoQry = "SELECT CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME FROM SUMADHURA_CONTRACTOR WHERE upper(CONCAT(FIRST_NAME,' '||LAST_NAME)) like upper('%"+contractorName+"%') and STATUS='A'  and rownum<50";
	//	log.debug("Query to fetch contractor info = "+contractorInfoQry);

		dbContractorList = jdbcTemplate.queryForList(contractorInfoQry, new Object[]{});

		for(Map<String, Object> contractorInfo : dbContractorList) {
			sb = sb.append(String.valueOf(contractorInfo.get("CONTRACTOR_NAME")));
			sb = sb.append("@@");
		}	
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 2);
		}
		//System.out.println("Hai "+sb.toString());

		return sb.toString();
	}

	@Override
	public String getEmployerInfo(String employeeName) {
	//	System.out.println("in dao:"+employeeName);
		StringBuffer sb = null;
		List<Map<String, Object>> dbEmployeeList = null;		


		employeeName = employeeName.replace("$$", "&");
		employeeName = employeeName.toUpperCase();

		log.debug("Vendor Name = "+employeeName);

		sb = new StringBuffer();

		String contractorInfoQry = "SELECT EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS WHERE upper(EMP_NAME) like '%"+employeeName+"%' ";
		log.debug("Query to fetch employee info = "+contractorInfoQry);

		dbEmployeeList = jdbcTemplate.queryForList(contractorInfoQry, new Object[]{});

		for(Map<String, Object> employeeInfo : dbEmployeeList) {
			sb = sb.append(String.valueOf(employeeInfo.get("EMP_NAME")));
			sb = sb.append("@@");
		}	
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 2);
		}
		//System.out.println("Hai "+sb.toString());
		
		return sb.toString();
	}
		
	public String getEmployerid(String employeeName) {
		
		//System.out.println("in dao:"+employeeName);
		StringBuffer sb = null;
		List<Map<String, Object>> dbEmployeeList = null;		

		
		employeeName = employeeName.replace("$$", "&");
		
		//log.debug("Vendor Name = "+employeeName);

		sb = new StringBuffer();

		String contractorInfoQry = "SELECT EMP_ID FROM SUMADHURA_EMPLOYEE_DETAILS WHERE EMP_NAME =? ";
		//log.debug("Query to fetch employee info = "+contractorInfoQry);

		dbEmployeeList = jdbcTemplate.queryForList(contractorInfoQry, new Object[]{employeeName});
	//	System.out.println("in dao resulted data"+dbEmployeeList);
		for(Map<String, Object> employeeInfo : dbEmployeeList) {
			sb = sb.append(String.valueOf(employeeInfo.get("EMP_ID")));
			sb = sb.append("@@");
		}	

		
		if (sb.length() > 0) {
			   sb.setLength(sb.length() - 2);
		 }
		
		return sb.toString();
	}
	
public String getEmployerName(String employeeid) {
		
		//System.out.println("in dao:"+employeeid);
		StringBuffer sb = null;
		List<Map<String, Object>> dbEmployeeList = null;		

		
		employeeid = employeeid.replace("$$", "&");
		
		//log.debug("Vendor Name = "+employeeName);

		sb = new StringBuffer();

		String contractorInfoQry = "SELECT EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS WHERE EMP_ID =? ";
		log.debug("Query to fetch employee info = "+contractorInfoQry);

		dbEmployeeList = jdbcTemplate.queryForList(contractorInfoQry, new Object[]{employeeid});
		//System.out.println("in dao resulted id data"+dbEmployeeList);
		for(Map<String, Object> employeeInfo : dbEmployeeList) {
			sb = sb.append(String.valueOf(employeeInfo.get("EMP_NAME")));
			sb = sb.append("@@");
		}	

		
		if (sb.length() > 0) {
			   sb.setLength(sb.length() - 2);
			}
		//System.out.println("Hai "+sb.toString());
		
		return sb.toString();
	}
	

	
	@Override
	public List<ViewIndentIssueDetailsBean> getViewInvoiceIssueDetails(String fromDate, String toDate, String siteId, String val) {

		String query = "";
		String strDCFormQuery = "";
		String strDCNumber = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<ViewIndentIssueDetailsBean> list = new ArrayList<ViewIndentIssueDetailsBean>();
		ViewIndentIssueDetailsBean indentObj = null; 

		try {
			//if part is for view indent receive details,else part is for view indent issue details
			template = new JdbcTemplate(DBConnection.getDbConnection());
			/*
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = "SELECT VD.VENDOR_NAME, LD.USERNAME, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.RECEVED_QTY, IE.INVOICE_ID, IED.MEASUR_MNT_NAME,IE.RECEVED_DATE,IE.INVOICE_DATE,IE.TOTAL_AMOUNT FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.VENDOR_ID=VD.VENDOR_ID AND TRUNC(IE.RECEVED_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = "SELECT VD.VENDOR_NAME, LD.USERNAME, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.RECEVED_QTY, IE.INVOICE_ID, IED.MEASUR_MNT_NAME,IE.RECEVED_DATE,IE.INVOICE_DATE,IE.TOTAL_AMOUNT FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"' AND IE.VENDOR_ID=VD.VENDOR_ID AND LD.UNAME=IE.USER_ID AND TRUNC(IE.RECEVED_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = "SELECT VD.VENDOR_NAME, LD.USERNAME, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.RECEVED_QTY, IE.INVOICE_ID, IED.MEASUR_MNT_NAME,IE.RECEVED_DATE,IE.INVOICE_DATE,IE.TOTAL_AMOUNT FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"' AND IE.VENDOR_ID=VD.VENDOR_ID AND LD.UNAME=IE.USER_ID AND TRUNC(IE.RECEVED_DATE) =TO_DATE('"+toDate+"', 'dd-MM-yy')";
				}*/
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = "SELECT DISTINCT IE.INVOICE_ID, IE.RECEIVED_OR_ISSUED_DATE,IE.INVOICE_DATE, VD.VENDOR_NAME FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"'  AND IE.VENDOR_ID=VD.VENDOR_ID AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = "SELECT DISTINCT IE.INVOICE_ID,IE.RECEIVED_OR_ISSUED_DATE, IE.INVOICE_DATE, VD.VENDOR_NAME FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"' AND IE.VENDOR_ID=VD.VENDOR_ID AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = "SELECT DISTINCT IE.INVOICE_ID,IE.RECEIVED_OR_ISSUED_DATE, IE.INVOICE_DATE, VD.VENDOR_NAME FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD, VENDOR_DETAILS VD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  AND IE.INDENT_TYPE='IN' AND IE.SITE_ID='"+siteId+"' AND IE.VENDOR_ID=VD.VENDOR_ID AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE) =TO_DATE('"+toDate+"', 'dd-MM-yy')";
				}






			dbIndentDts = template.queryForList(query, new Object[]{});
			if (StringUtils.isNotBlank(val)) {
				for(Map<String, Object> prods : dbIndentDts) {
					indentObj = new ViewIndentIssueDetailsBean();

					indentObj.setRequesterId(prods.get("INVOICE_ID")==null ? "" : prods.get("INVOICE_ID").toString());
					String invoicedate=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
					indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
					String receviedDate=prods.get("RECEIVED_OR_ISSUED_DATE")==null ? "" : prods.get("RECEIVED_OR_ISSUED_DATE").toString();
					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date receive_date = dt.parse(receviedDate);
					Date invoice_date = dt.parse(invoicedate);
					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat time1 = new SimpleDateFormat("hh:mm:ss");
					// System.out.println("Time: " + time1.format(receviedDate));
					String convertreceive_date=dt1.format(receive_date);
					String convertreceive_time=time1.format(receive_date);
					String convertinvoice_date=dt1.format(invoice_date);
					//System.out.println("the date is "+date2);
					
					indentObj.setStrInvoiceDate(convertinvoice_date);
					indentObj.setDate(convertreceive_date);
					indentObj.setTime(convertreceive_time);
					
				String date=prods.get("RECEIVED_OR_ISSUED_DATE")==null ? "" : prods.get("RECEIVED_OR_ISSUED_DATE").toString();
					if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}
					indentObj.setReceivedDate(date);
					
					String query1 = "select sum(TOTAL_AMOUNT) as SUM_TOTAL_AMOUNT from INDENT_ENTRY where INVOICE_ID = ?";
					List<Map<String, Object>> dbIndentDts1 = null;
					dbIndentDts1 = template.queryForList(query1, new Object[]{prods.get("INVOICE_ID").toString()});
					for(Map<String, Object> prods1 : dbIndentDts1) {
					indentObj.setStrTotalAmount(prods1.get("SUM_TOTAL_AMOUNT")==null ? "" : prods1.get("SUM_TOTAL_AMOUNT").toString());
					}
					list.add(indentObj);
				}



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
	// ravi written below code
	/*============================================ getting the work order quantity start=====================================================*/
	public double gettingWorkorderQuantity(IndentIssueDto issueDto) {
		List<Map<String, Object>> dbIndentDts = null;
		//List<Map<String, Object>> dbDts = null;
		List<Map<String, Object>> dbQuantityDts = null;
		JdbcTemplate template = null;
		double workOrderQuantity=0.0;
		double bufferQuantity=0.0;
		double totalQuantity=0.0;
		double issuedQuantity=0.0;
		try{
		template = new JdbcTemplate(DBConnection.getDbConnection());
	 
		StringBuffer queryForTotalQuantity=new StringBuffer("SELECT SUM(QWPD.TOTAL_QUANTITY)as QUANTITY,sum(QBS.QUANTITY) as BUFFER_QUANTITY,QWID.WO_WORK_DESC_ID,QWID.WO_WORK_ISSUE_DTLS_ID,QWPD.WO_WORK_ISSUE_AREA_DTLS_ID ")
				.append(" FROM QS_WORKORDER_PRODUCT_DTLS QWPD,QS_WORKORDER_ISSUE_AREA_DETAILS QWIAD,QS_WORKORDER_ISSUE_DETAILS QWID,QS_BOQ_AREA_MAPPING QBAM ")
				.append(" LEFT OUTER JOIN QS_BUFFER_STOCK QBS ON QBS.WO_WORK_AREA_ID=QBAM.WO_WORK_AREA_ID AND QBS.SITE_ID=? AND QBS.INVENTORY_TYPE='OUT'");
		queryForTotalQuantity.append(" WHERE  QWPD.WO_WORK_ISSUE_AREA_DTLS_ID=QWIAD.WO_WORK_ISSUE_AREA_DTLS_ID AND QWIAD.WO_WORK_ISSUE_DTLS_ID=QWID.WO_WORK_ISSUE_DTLS_ID ");
				if(issueDto.getBlockId()!=null&&!issueDto.getBlockId().equals("0")){queryForTotalQuantity.append(" AND QBAM.BLOCK_ID='"+issueDto.getBlockId()+"' ");}					
				//if floorid is not empty add condition in query
				if(issueDto.getFloorId()!=null&&!issueDto.getFloorId().equals("0")){ queryForTotalQuantity.append(" AND QBAM.FLOOR_ID='"+issueDto.getFloorId()+"' ");  }					
				//if flat is not empty add condition in query
				if(issueDto.getFlatId()!=null&&!issueDto.getFlatId().equals("0")) { queryForTotalQuantity.append(" AND QBAM.FLAT_ID='"+issueDto.getFlatId()+"' ");  }
 
				queryForTotalQuantity.append(" AND QBAM.WO_WORK_AREA_ID=QWIAD.WO_WORK_AREA_ID AND QBAM.WO_WORK_DESC_ID=?   AND QWPD.MATERIAL_GROUP_ID=?   And QWID.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID ") 
				.append(" FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = ? and SITE_ID=?)  group by QWID.WO_WORK_DESC_ID, QWID.WO_WORK_ISSUE_DTLS_ID, QWPD.WO_WORK_ISSUE_AREA_DTLS_ID ");
 		
		dbIndentDts = template.queryForList(queryForTotalQuantity.toString(), new Object[]{issueDto.getSiteId(),
				issueDto.getWd(),issueDto.getGroupId(),/*issueDto.getMeasurementId(),*/issueDto.getWorkorderNumber(),issueDto.getSiteId()
		});
		if (dbIndentDts!=null && dbIndentDts.size()>0) {
			for(Map<String, Object> prods : dbIndentDts) {
				workOrderQuantity+=Double.valueOf(prods.get("QUANTITY")==null ? "0" : prods.get("QUANTITY").toString());
				bufferQuantity=Double.valueOf(prods.get("BUFFER_QUANTITY")==null ? "0" : prods.get("BUFFER_QUANTITY").toString());
			}
		}
		/*String sql="SELECT sum(QBS.QUANTITY) as BUFFER_QUANTITY FROM QS_BUFFER_STOCK QBS,QS_BOQ_AREA_MAPPING QBAM WHERE QBS.WO_WORK_AREA_ID=QBAM.WO_WORK_AREA_ID " +
					"AND QBAM.WO_WORK_DESC_ID=? AND QBS.SITE_ID=? AND QBS.INVENTORY_TYPE='OUT'";
		dbDts = template.queryForList(sql, new Object[]{
				issueDto.getWd(),issueDto.getSiteId()
		});
		if(dbDts!=null && dbDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				bufferQuantity=Double.valueOf(prods.get("BUFFER_QUANTITY")==null ? "0" : prods.get("BUFFER_QUANTITY").toString());
			}
		}*/
		StringBuffer queryForIssuedQty=new StringBuffer("SELECT SUM(IED.ISSUED_QTY) as ISSUED_QUANTITY FROM INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE WHERE IE.INDENT_TYPE IN('OUTO') ")
					.append(" AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND IE.SITE_ID=? AND IED.WO_WORK_DESC_ID=? AND IED.MATERIAL_GROUP_ID=?");
			if(issueDto.getBlockId()!=null&&!issueDto.getBlockId().equals("0")){queryForIssuedQty.append(" AND IED.BLOCK_ID='"+issueDto.getBlockId()+"' ");}					
			//if floorid is not empty add condition in query
			if(issueDto.getFloorId()!=null&&!issueDto.getFloorId().equals("0")){ queryForIssuedQty.append(" AND IED.FLOOR_ID='"+issueDto.getFloorId()+"' ");  }					
			//if flat is not empty add condition in query
			if(issueDto.getFlatId()!=null&&!issueDto.getFlatId().equals("0")) { queryForIssuedQty.append(" AND IED.FLAT_ID='"+issueDto.getFlatId()+"' ");  }

		
		dbQuantityDts=template.queryForList(queryForIssuedQty.toString(), new Object[]{issueDto.getSiteId(),issueDto.getWd(),issueDto.getGroupId()});
		if(dbQuantityDts!=null && dbQuantityDts.size()>0){
			for(Map<String, Object> prods : dbQuantityDts) {
				issuedQuantity=Double.valueOf(prods.get("ISSUED_QUANTITY")==null ? "0" : prods.get("ISSUED_QUANTITY").toString());
			}
		}
		//System.err.println(String.format("%.2f", a));
		workOrderQuantity= Math.round(workOrderQuantity * 100.0) / 100.0;
		totalQuantity=(workOrderQuantity+bufferQuantity)-issuedQuantity;
		}catch(Exception e){
			e.printStackTrace();
		}
		return totalQuantity;
	}
	
	/*============================================ getting the work order quantity end=====================================================*/
	
	public static void main(String [] args){

		double doubleTotalAmount = 1.254;
		int intTotalAmount  = (int)((doubleTotalAmount*100));
		System.out.println("intTotalAmount "+intTotalAmount);
		
		System.out.println("(double)intTotalAmount) "+ (double)intTotalAmount);
		
		doubleTotalAmount = ((double)intTotalAmount)/100;
		
		System.out.println(doubleTotalAmount);
	}


}
