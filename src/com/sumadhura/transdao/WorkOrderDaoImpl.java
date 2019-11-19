package com.sumadhura.transdao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.ContractorQSBillBean;
import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.service.WorkOrderService;

@Repository("workControllerDao")
public class WorkOrderDaoImpl implements WorkOrderDao {
	
	static Logger log = Logger.getLogger(WorkOrderDaoImpl.class);
	
	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;
	
	@Autowired(required = true)
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	@Qualifier("workControllerService")
	WorkOrderService workControllerService;
		
	/**
	 * @author Aniket Chavan
	 * @description this method is used to select the major head for work order and NMR work order
	 * @param siteId used for selecting the BOQ data by site wise
	 * @param typeOFWork used to select the data based on on type it may be PIECE Work or NMR work 
	 * @return qs_wo_majorheadList is the type of the map implement class
	 */
	@Override
	public Map<String, String> loadQSProducts(String siteId, String typeOfWork) {
		log.info("WorkOrderDaoImpl.loadQSProducts() "+siteId+" \t"+typeOfWork);
		Map<String, String> qs_wo_majorheadList = null;
		String WorkDescContainsRecordType="";
		qs_wo_majorheadList = new HashMap<String, String>();
		if(typeOfWork.equals("PIECEWORK")){
			typeOfWork="PIECEWORK";
		}
		StringBuffer query=new StringBuffer().append("SELECT DISTINCT(QWMJ.WO_MAJORHEAD_ID),WO_MAJORHEAD_DESC ")
				 .append(",(SELECT LISTAGG(RECORD_TYPE, '@@')  WITHIN GROUP (ORDER BY WO_WORK_DESC_ID)  FROM QS_BOQ_AREA_MAPPING QBAM1 WHERE QBAM1.SITE_ID= QBAM.SITE_ID AND QBAM1.WO_WORK_DESC_ID=QBAM.WO_WORK_DESC_ID   AND QBAM1.WO_WORK_AREA_GROUP_ID=QBAM.WO_WORK_AREA_GROUP_ID) AS WO_CONTAINS")
				 .append(" FROM QS_BOQ_AREA_MAPPING QBAM,QS_BOQ QB, QS_WO_WORKDESC QWD ,QS_WO_MINORHEAD QWM , QS_WO_MAJORHEAD QWMJ ")
				 .append(" WHERE QBAM.SITE_ID = ? AND  QWD.TYPE_OF_WORK='PIECEWORK' AND QWD.WO_WORK_DESC_ID = QBAM.WO_WORK_DESC_ID AND ")
				 .append(" QWD.WO_MINORHEAD_ID = QWM.WO_MINORHEAD_ID AND  	QB.BOQ_SEQ_NO=QBAM.BOQ_SEQ_NO AND  QBAM.STATUS='A' AND QB.STATUS='A' AND ")
				 .append(" QWM.WO_MAJORHEAD_ID = QWMJ.WO_MAJORHEAD_ID");
		if(typeOfWork.equals("MATERIAL")){//Checking type of work if it is material, then load WD related to Material else LABOR
			query.append(" AND QBAM.RECORD_TYPE='MATERIAL'");	
		}else{
			query.append(" AND QBAM.RECORD_TYPE='LABOR'");
		}
		query.append(" ORDER BY WO_MAJORHEAD_ID");
		
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query.toString(),siteId);
		//converting the list type object into map for better iteration
		for (Map<String, Object> prods : list) {
			WorkDescContainsRecordType=prods.get("WO_CONTAINS")==null?"":prods.get("WO_CONTAINS").toString();
			if(typeOfWork.equals("MATERIAL")){//Checking type of work if it is material don't load WD of LABOR+MATERIAL
				if(WorkDescContainsRecordType.equals("MATERIAL"))
					qs_wo_majorheadList.put(String.valueOf(prods.get("WO_MAJORHEAD_ID")),String.valueOf(prods.get("WO_MAJORHEAD_DESC")));
			}else{
					qs_wo_majorheadList.put(String.valueOf(prods.get("WO_MAJORHEAD_ID")),String.valueOf(prods.get("WO_MAJORHEAD_DESC")));
			}
		}
		return qs_wo_majorheadList;
	}

	/**
	 * @author Aniket Chavan
	 * @description this method is used to select the minor head for work order and NMR work order
	 * @param majorHeadId is holding the major head id so we can select the data minor head data based on the majorHeadId
	 * @param siteId used for selecting the BOQ data by site wise
	 * @param typeOfWork  used to select the data based on on type it may be PIECE Work or NMR work 
	 * @return sb is holding the returning object
	 */
	@Override
	public String loadWOSubProds(String majorHeadId, String siteId, String typeOfWork) {
	log.info("WorkOrderDaoImpl.loadWOSubProds()" +siteId+" \t"+typeOfWork);
		StringBuffer sb = null;
		String WorkDescContainsRecordType="";
		List<Map<String, Object>> dbSubProductsList = null;
		sb = new StringBuffer();
		
		StringBuffer subProdsQry = new StringBuffer();
		
		//here selecting all the minor head data based on major head id, but checking in QS_BOQ_DETAILS that this major head related minor head is exists or not in QS_BOQ_DETAILS if there select all the minor head names 
		//selecting if only status is active
		 if(typeOfWork.equals("NMR")){
			subProdsQry.append("select distinct QWM.WO_MINORHEAD_ID, QWM.WO_MINORHEAD_DESC  from QS_BOQ_DETAILS QBAM,QS_BOQ QB, QS_WO_WORKDESC QWD ,QS_WO_MINORHEAD QWM ")
					.append(" where QB.SITE_ID =  '"+siteId+"'  and  QWD.TYPE_OF_WORK='NMR' and QWD.WO_WORK_DESC_ID = QBAM.WO_WORK_DESC_ID and QBAM.STATUS='A' AND QB.STATUS='A' and ")
					.append(" qb.BOQ_SEQ_NO=QBAM.BOQ_SEQ_NO  and QWD.WO_MINORHEAD_ID = QBAM.WO_MINORHEAD_ID  AND	QWD.WO_MINORHEAD_ID = QWM.WO_MINORHEAD_ID  and QWM.WO_MAJORHEAD_ID=? order by QWM.WO_MAJORHEAD_ID");
		}else{
			 //here selecting all the minor head data based on major head id, but checking in QS_BOQ_AREA_MAPPING
			//selecting if only status is active
				subProdsQry.append("SELECT DISTINCT QWM.WO_MINORHEAD_ID, QWM.WO_MINORHEAD_DESC")
				.append(",(select LISTAGG(RECORD_TYPE, '@@')  WITHIN GROUP (ORDER BY WO_WORK_DESC_ID)  from QS_BOQ_AREA_MAPPING QBAM1 where QBAM1.SITE_ID= QBAM.SITE_ID AND QBAM1.WO_WORK_DESC_ID=QBAM.WO_WORK_DESC_ID   AND QBAM1.WO_WORK_AREA_GROUP_ID=QBAM.WO_WORK_AREA_GROUP_ID) as WO_CONTAINS")
				.append("  FROM QS_BOQ_AREA_MAPPING QBAM,QS_BOQ QB, QS_WO_WORKDESC QWD ,QS_WO_MINORHEAD QWM ")
				.append(" WHERE QBAM.SITE_ID = '"+siteId+"' AND  QWD.TYPE_OF_WORK='PIECEWORK' AND QWD.WO_WORK_DESC_ID = QBAM.WO_WORK_DESC_ID AND QBAM.STATUS='A' AND QB.STATUS='A' AND ")
				.append(" QB.BOQ_SEQ_NO=QBAM.BOQ_SEQ_NO AND	QWD.WO_MINORHEAD_ID = QWM.WO_MINORHEAD_ID  AND QWM.WO_MAJORHEAD_ID=? ");
				if(typeOfWork.equals("MATERIAL")){//Checking type of work if it is material, then load WD related to Material else LABOR
					subProdsQry.append(" AND QBAM.RECORD_TYPE='MATERIAL'");	
				}else{
					subProdsQry.append(" AND QBAM.RECORD_TYPE='LABOR'");
				}
					subProdsQry.append(" ORDER BY QWM.WO_MAJORHEAD_ID");
			}
		dbSubProductsList = jdbcTemplate.queryForList(subProdsQry.toString(), new Object[] { majorHeadId });
		for (Map<String, Object> subProds : dbSubProductsList) {
			WorkDescContainsRecordType=subProds.get("WO_CONTAINS")==null?"":subProds.get("WO_CONTAINS").toString();
			if(typeOfWork.equals("MATERIAL")){//Checking type of work if it is material don't load WD of LABOR+MATERIAL
				if(WorkDescContainsRecordType.equals("MATERIAL")){
					sb.append(String.valueOf(subProds.get("WO_MINORHEAD_ID")) + "_"+ String.valueOf(subProds.get("WO_MINORHEAD_DESC")) + "|");
				}
			}else{
					if(!sb.toString().contains(String.valueOf(subProds.get("WO_MINORHEAD_DESC")))){
						sb.append(String.valueOf(subProds.get("WO_MINORHEAD_ID")) + "_"+ String.valueOf(subProds.get("WO_MINORHEAD_DESC")) + "|");
					}
					
			}
		}

		return sb.toString();
	}

	/**
	 * @author Aniket Chavan
	 * @description this method is used to select the minor head for work order and NMR work order
	 * @param minorheadId is holding the minor head id, so we can select the work desc data based on the minorHeadId
	 * @param siteId used for selecting the BOQ data by site wise
	 * @param typeOfWork  used to select the data based on on type it may be PIECE Work or NMR work 
	 * @return sb 
	 */
	@Override
	public String loadWOChildProds(String minorheadId, String siteId, String typeOfWork) {
		log.info("WorkOrderDaoImpl.loadWOChildProds() "+siteId+" \t"+typeOfWork);
		StringBuffer sb = null;
		String WorkDescContainsRecordType="";
		List<Map<String, Object>> dbChildProductsList = null;
		sb = new StringBuffer();
		StringBuffer subProdsQry=new StringBuffer();
		if(typeOfWork.equals("NMR")){
			subProdsQry.append("SELECT DISTINCT(QWD.WO_WORK_DESC_ID),QWD.WO_WORK_DESCRIPTION FROM QS_BOQ QB,QS_BOQ_DETAILS QBAM, QS_WO_WORKDESC QWD ")
					   .append(" WHERE QB.BOQ_SEQ_NO=QBAM.BOQ_SEQ_NO AND QB.SITE_ID = ?  AND QWD.WO_MINORHEAD_ID =? AND QWD.WO_WORK_DESC_ID = QBAM.WO_WORK_DESC_ID  AND QWD.STATUS = 'A' AND QB.STATUS = 'A'");
		}else{
			subProdsQry.append(" SELECT DISTINCT(QWD.WO_WORK_DESC_ID),QWD.WO_WORK_DESCRIPTION ")
			.append(",(select LISTAGG(RECORD_TYPE, '@@')  WITHIN GROUP (ORDER BY WO_WORK_DESC_ID)  from QS_BOQ_AREA_MAPPING QBAM1 where QBAM1.SITE_ID= QBAM.SITE_ID AND QBAM1.WO_WORK_DESC_ID=QBAM.WO_WORK_DESC_ID   AND QBAM1.WO_WORK_AREA_GROUP_ID=QBAM.WO_WORK_AREA_GROUP_ID) as WO_CONTAINS")
			.append( " FROM QS_BOQ QS,QS_BOQ_AREA_MAPPING QBAM, QS_WO_WORKDESC QWD ")
			.append(" WHERE QBAM.SITE_ID = ? AND QWD.WO_MINORHEAD_ID =? AND QWD.WO_WORK_DESC_ID = QBAM.WO_WORK_DESC_ID AND QS.BOQ_SEQ_NO=QBAM.BOQ_SEQ_NO ")
			.append(" AND QWD.STATUS = 'A' AND QS.STATUS='A'");
			if(typeOfWork.equals("MATERIAL")){//Checking type of work if it is material, then load WD related to Material else LABOR
				subProdsQry.append(" AND QBAM.RECORD_TYPE='MATERIAL'");	
			}else{
				subProdsQry.append(" AND QBAM.RECORD_TYPE='LABOR'");
			}
		}
		dbChildProductsList = jdbcTemplate.queryForList(subProdsQry.toString(), new Object[] {siteId,minorheadId });
		
		for (Map<String, Object> childProds : dbChildProductsList) {
			WorkDescContainsRecordType=childProds.get("WO_CONTAINS")==null?"":childProds.get("WO_CONTAINS").toString();
			if(typeOfWork.equals("MATERIAL")){//Checking type of work if it is material don't load WD of LABOR+MATERIAL
				if(WorkDescContainsRecordType.equals("MATERIAL"))
					sb.append(String.valueOf(childProds.get("WO_WORK_DESC_ID")) + "_"+ String.valueOf(childProds.get("WO_WORK_DESCRIPTION")) + "|");
			}else{
				if(!sb.toString().contains(String.valueOf(childProds.get("WO_WORK_DESC_ID"))))
					sb.append(String.valueOf(childProds.get("WO_WORK_DESC_ID")) + "_"+ String.valueOf(childProds.get("WO_WORK_DESCRIPTION")) + "|");
			}
		}
		return sb.toString();
	}


	/**
	 * @author Aniket Chavan
	 * @description this method is used to select the measurement for work order and NMR work order
	 * @param workDescId is holding the wokr desc  id, so we can select the work desc data based on the minorHeadId
	 * @return sb 
	 */
	@Override
	public String loadWorkOrderMeasurements(String workDescId, String siteId, String typeOfWork) {
		log.info("WorkOrderDaoImpl.loadWorkOrderMeasurements() "+siteId+" \t"+typeOfWork );
		StringBuffer sb = null;
		List<Map<String, Object>> dbMeasurementsList = null;
		sb = new StringBuffer();
		StringBuffer query=new StringBuffer("");
		Object[] obj={siteId,workDescId};
		if(typeOfWork.equals("NMR")){
		    query.append(" SELECT DISTINCT(QBAM.WO_MEASURMENT_ID),QWD.WO_MEASURMEN_NAME FROM QS_BOQ QB,QS_BOQ_DETAILS QBAM, QS_WO_MEASURMENT QWD ")
		     	 .append(" WHERE QB.BOQ_SEQ_NO=QBAM.BOQ_SEQ_NO AND QB.SITE_ID = ? AND QBAM.WO_WORK_DESC_ID = ?  AND QBAM.WO_MEASURMENT_ID=QWD.WO_MEASURMENT_ID AND QWD.WO_WORK_DESC_ID = QBAM.WO_WORK_DESC_ID  AND QWD.STATUS = 'A' AND QB.STATUS = 'A' ");
		}else{
			query.append(" SELECT DISTINCT(QBAM.WO_MEASURMENT_ID),QWD.WO_MEASURMEN_NAME FROM QS_BOQ QS,QS_BOQ_AREA_MAPPING QBAM,  QS_WO_MEASURMENT QWD ")
				 .append(" WHERE QBAM.SITE_ID =? AND QWD.WO_WORK_DESC_ID = QBAM.WO_WORK_DESC_ID AND QS.BOQ_SEQ_NO=QBAM.BOQ_SEQ_NO ")
				 .append(" AND QBAM.WO_WORK_DESC_ID = ? AND QBAM.WO_MEASURMENT_ID=QWD.WO_MEASURMENT_ID AND QWD.STATUS = 'A' AND QS.STATUS='A' ");
			if(typeOfWork.equals("MATERIAL")){
				query.append(" AND QBAM.RECORD_TYPE='MATERIAL'");	
			}else{
				query.append(" AND QBAM.RECORD_TYPE='LABOR'");
			}
		} 
 		dbMeasurementsList = jdbcTemplate.queryForList(query.toString(), obj);

		for (Map<String, Object> measurements : dbMeasurementsList) {
			sb = sb.append(String.valueOf(measurements.get("WO_MEASURMENT_ID"))+"_"+ String.valueOf(measurements.get("WO_MEASURMEN_NAME")) + "|");
		}
		return sb.toString();
	}

	/**
	 * @author Aniket Chavan
	 * @description this method is used to select the measurement for work order and NMR work order
	 * @param bean is holding all data in object
	 * @return ScopeOfWork is holding the scope the work and the BOQ amount
	 */
	
	@Override
	public String loadScopeOfWork(WorkOrderBean bean) {
		String childProductId=bean.getWO_Desc1();
		String minorHeadId=bean.getWO_MinorHead1();
		String siteId=bean.getSiteId();
		//String mesurmentId=bean.getUnitsOfMeasurement1();
		String typeOfWork=	bean.getTypeOfWork();
		String ScopeOfWork="";
		//List<Map<String, Object>> list=null;
		StringBuffer scopeOfWorkQuery =null;
		BigDecimal bigDecimal3=new BigDecimal(0);
		
		if(typeOfWork.equals("NMR")){
			 scopeOfWorkQuery = new StringBuffer("SELECT  CONCAT(BOQ_NO,'@@'||WO_WORK_AREA_AMOUNT||'@@'||WO_WORK_AREA||'@@'||QB.BOQ_TOTAL_AMOUNT) FROM QS_BOQ_DETAILS QBD,QS_BOQ QB WHERE QB.BOQ_SEQ_NO=QBD.BOQ_SEQ_NO AND WO_WORK_DESC_ID=? AND WO_MINORHEAD_ID='"+minorHeadId+"' AND QB.SITE_ID=? AND QB.STATUS='A' and rownum=1");
		}else{
			 scopeOfWorkQuery = new StringBuffer("SELECT  CONCAT(SCOPE_OF_WORK,'@@'||WO_WORK_AREA_AMOUNT||'@@'||WO_WORK_AREA||'@@'||QB.BOQ_TOTAL_AMOUNT) FROM QS_BOQ_DETAILS QBD,QS_BOQ QB WHERE QB.BOQ_SEQ_NO=QBD.BOQ_SEQ_NO AND WO_WORK_DESC_ID=? AND QB.SITE_ID=? AND QB.STATUS='A'");
			 if(typeOfWork.equals("MATERIAL")){
				 scopeOfWorkQuery.append(" AND RECORD_TYPE='MATERIAL'");
			 }else{
				 scopeOfWorkQuery.append(" AND RECORD_TYPE='LABOR'");
			 }
		}
		
		try {
			 ScopeOfWork= jdbcTemplate.queryForObject(scopeOfWorkQuery.toString(), new Object[] { childProductId,siteId},String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(typeOfWork.equals("NMR")){
			String 	pertotalAmountOfBOQ=jdbcTemplate.queryForObject("SELECT  NVL(SUM(TOTAL_WO_AMOUNT),0)  FROM QS_WORKORDER_ISSUE WHERE TYPE_OF_WORK=? AND STATUS='A' AND SITE_ID=? ", new Object[] { typeOfWork,siteId}, String.class);
			String	temptotalAmountOfBOQ=jdbcTemplate.queryForObject("SELECT  NVL(SUM(TOTAL_WO_AMOUNT),0)  FROM QS_WORKORDER_TEMP_ISSUE WHERE TYPE_OF_WORK=? AND STATUS IN ('A','DF','M') AND SITE_ID=? ", new Object[] { typeOfWork,siteId}, String.class);
			 
			 BigDecimal bigDecimal2=new BigDecimal(temptotalAmountOfBOQ).setScale(2,RoundingMode.CEILING);
			 bigDecimal3=new BigDecimal(pertotalAmountOfBOQ).add(bigDecimal2).setScale(2,RoundingMode.CEILING);
		 
		}
		return ScopeOfWork+"@@"+bigDecimal3;
	}
	
	/**
	 * @author Aniket Chavan
	 * @description this method is used to select the measurement for work order and NMR work order
	 * @param bean is holding all data in object
	 * @return ScopeOfWork is holding the scope the work and the BOQ amount
	 */
	@Override
	public String loadScopeOfWorkAndBOQAmount(WorkOrderBean bean) {
		String childProductId=bean.getWO_Desc1();
		String minorHeadId=bean.getWO_MinorHead1();
		String siteId=bean.getSiteId();
		String mesurmentId=bean.getUnitsOfMeasurement1();
		String typeOfWork=	bean.getTypeOfWork();
		String boqNo=bean.getBoqNo();
		String ScopeOfWork="";
		List<Map<String, Object>> list=null;
		StringBuffer scopeOfWorkQuery =null;
		BigDecimal bigDecimal3=new BigDecimal(0);
		try {
			if(typeOfWork.equals("NMR")){
				 scopeOfWorkQuery = new StringBuffer("SELECT CONCAT(BOQ_NO,'@@'||WO_WORK_AREA_AMOUNT||'@@'||WO_WORK_AREA||'@@'||QB.BOQ_TOTAL_AMOUNT) FROM QS_BOQ_DETAILS QBD,QS_BOQ QB WHERE QB.BOQ_SEQ_NO=QBD.BOQ_SEQ_NO AND WO_WORK_DESC_ID=? AND QB.SITE_ID=? AND WO_MINORHEAD_ID=? AND QB.BOQ_NO=? and rownum=1");
				 ScopeOfWork= jdbcTemplate.queryForObject(scopeOfWorkQuery.toString(), new Object[] { childProductId,siteId,minorHeadId,boqNo},String.class);
			}else{
				 scopeOfWorkQuery = new StringBuffer("SELECT CONCAT(SCOPE_OF_WORK,'@@'||WO_WORK_AREA_AMOUNT||'@@'||WO_WORK_AREA||'@@'||QB.BOQ_TOTAL_AMOUNT) FROM QS_BOQ_DETAILS QBD,QS_BOQ QB WHERE QB.BOQ_SEQ_NO=QBD.BOQ_SEQ_NO AND WO_WORK_DESC_ID=? AND QB.SITE_ID=? AND QB.STATUS='A' ");
				 if(typeOfWork.equals("MATERIAL")){
					 scopeOfWorkQuery.append(" AND RECORD_TYPE='MATERIAL'");
				 }else{
					 scopeOfWorkQuery.append(" AND RECORD_TYPE='LABOR'");
				 }
				 ScopeOfWork= jdbcTemplate.queryForObject(scopeOfWorkQuery.toString(), new Object[] { childProductId,siteId},String.class);
			}
	 	} catch (Exception e) {
			e.printStackTrace();
		}
		if(typeOfWork.equals("NMR")){
			String 	pertotalAmountOfBOQ=jdbcTemplate.queryForObject("SELECT  NVL(SUM(TOTAL_WO_AMOUNT),0)  FROM QS_WORKORDER_ISSUE WHERE TYPE_OF_WORK=? AND STATUS='A' AND SITE_ID=? ", new Object[] { typeOfWork,siteId}, String.class);
			String	temptotalAmountOfBOQ=jdbcTemplate.queryForObject("SELECT  NVL(SUM(TOTAL_WO_AMOUNT),0)  FROM QS_WORKORDER_TEMP_ISSUE WHERE TYPE_OF_WORK=? AND STATUS  IN ('A','DF','M') AND SITE_ID=? ", new Object[] { typeOfWork,siteId}, String.class);
			BigDecimal bigDecimal2=new BigDecimal(temptotalAmountOfBOQ).setScale(2,RoundingMode.CEILING);
			bigDecimal3=new BigDecimal(pertotalAmountOfBOQ).add(bigDecimal2).setScale(2,RoundingMode.CEILING);
		}
		return ScopeOfWork+"@@"+bigDecimal3;
	}
	
	/**
	 * @author Aniket Chavan
	 * @description this method is checking the work order ni is exist or not if not it will return false
	 * @param workOrderNo is holding the work order number for checking 
	 * @param siteId is supporting to query while checking work order number
	 * @return String true or false
	 */
	
	@Override
	public String checkWorkOrderNoExistsOrNot(String workOrderNo, String siteId) {
		String query="SELECT COUNT(*) FROM QS_WORKORDER_ISSUE WHERE STATUS IN ('I','A') AND WORK_ORDER_NUMBER=? AND SITE_ID=? ";
		//checking work order number in permanent table if the result of the query is more than one means work order is already exists in DB
		int count=jdbcTemplate.queryForObject(query,new Object[]{workOrderNo,siteId}, Integer.class);
		if(count==0){
			query="SELECT COUNT(*) FROM QS_WORKORDER_TEMP_ISSUE WHERE STATUS IN ('A','DF') AND PER_WORK_ORDER_NUMBER=? AND SITE_ID=? ";
			//checking work order number in temporary table 
			count=jdbcTemplate.queryForObject(query,new Object[]{workOrderNo,siteId}, Integer.class);
			if(count!=0){
				return "true";
			}
			return "false";
		}else{
			return "true";
		}
	}
	
	@Override
	public String checkIsDraftWorkOrderExistsOrNot(String workOrderNo, String contractorId,String typeOfWork, String siteId) {
		String tempWorkOrderNumber="";
		String 	query="SELECT TEMP_WORK_ORDER_NUMBER FROM QS_WORKORDER_TEMP_ISSUE WHERE STATUS IN ('DF') AND CONTRACTOR_ID=? AND SITE_ID=? AND TYPE_OF_WORK=? AND rownum=1";
		try {
			tempWorkOrderNumber=jdbcTemplate.queryForObject(query, new Object[]{contractorId,siteId,typeOfWork}, String.class);
			return tempWorkOrderNumber;
		} catch (Exception e) {
			return"";	
		}
	}
	
	/**
	 * @description this method used for validating work order material BOQ
	 */
	@Override
	public Map<String, String> validateWOMaterialBOQDetails(List<WorkOrderBean> list, String siteId, String contractorId, String workOrderNumber,String operationType, String condition) throws Exception {
		//used tempIssueAreaDetailsId,workAreaId,contractorId,workOrderNumber,siteId
		Map<String, String> errorMessage=new HashMap<String, String>();
		List<Double> issuedMaterialForThisWorkAreaList=new ArrayList<Double>();
		String blockId="",floorId="",flatId="";
		String materialGroupId="",workDescId="";
		String workDescName="";
		String tempIssueAreaDetailsId="",workAreaGroupId="";
		String workAreaId="",actualArea="",inputBoxWorkAreaGroupId="";
		double selectedArea=0.0;
		Double bufferQuantity=0.0;
		//loading the updated material BOQ data
		StringBuffer queryForLatestMaterialBOQDetails=new StringBuffer("SELECT  MATERIAL_GROUP_NAME,QBPD.WO_WORK_AREA ,QBPD.MATERIAL_GROUP_ID ,QBPD.MATERIAL_GROUP_MEASUREMENT_ID , QBPD.PER_UNIT_QUANTITY , QBPD.PER_UNIT_AMOUNT ")
				.append(" from QS_BOQ_PRODUCT_DTLS QBPD,PRODUCT_GROUP_MASTER PGM ")
				.append(" where  PGM.MATERIAL_GROUP_ID= QBPD.MATERIAL_GROUP_ID ")
				.append(" AND PGM.MATERIAL_GROUP_MEASUREMENT_ID= QBPD.MATERIAL_GROUP_MEASUREMENT_ID  ")
				.append(" AND QBPD.WO_WORK_AREA_ID=? AND QBPD.SITE_ID=? order by QBPD.MATERIAL_GROUP_ID,QBPD.MATERIAL_GROUP_MEASUREMENT_ID");

		//loading the old material BOQ data which was inserted in QS_WORKORDER_PRODUCT_DTLS at the time of work order creation
		StringBuffer queryForOldMaterialBOQDetails=new StringBuffer("SELECT QWPD.QS_WORKORDER_PRODUCT_DTLS_ID ,QWPD.WO_WORK_ISSUE_AREA_DTLS_ID ,")
				.append(" QWPD.MATERIAL_GROUP_ID ,QWPD.UOM ,QWPD.WORK_AREA ,QWPD.PER_UNIT_QUANTITY , ")
				.append(" QWPD.TOTAL_QUANTITY ,QWIAD.WO_WORK_AREA_ID ,QWIAD.AREA_ALOCATED_QTY ,QWIAD.PRICE , ")
				.append(" QWIAD.RECORD_TYPE ,QBAM.BLOCK_ID,QBAM.FLOOR_ID,QBAM.FLAT_ID,QBAM.WO_WORK_DESC_ID,QWW.WO_WORK_DESCRIPTION ")
				.append(" FROM QS_WORKORDER_PRODUCT_DTLS QWPD,QS_WORKORDER_ISSUE_AREA_DETAILS QWIAD,QS_BOQ_AREA_MAPPING QBAM,QS_WO_WORKDESC QWW ")
				.append(" WHERE QWW.WO_WORK_DESC_ID=QBAM.WO_WORK_DESC_ID AND QBAM.WO_WORK_AREA_ID=QWIAD.WO_WORK_AREA_ID AND QWPD.WO_WORK_ISSUE_AREA_DTLS_ID=QWIAD.WO_WORK_ISSUE_AREA_DTLS_ID AND QWPD.WO_WORK_ISSUE_AREA_DTLS_ID=? ORDER BY QWPD.MATERIAL_GROUP_ID,QWPD.UOM");

 		
		for (WorkOrderBean bean : list) {
			 tempIssueAreaDetailsId=bean.getTempIssueAreaDetailsId();
			 workAreaId=bean.getWorkAreaId();
	 		 selectedArea=Double.valueOf(bean.getSelectedArea());
			 workAreaGroupId=bean.getWorkAreaGroupId();
			 inputBoxWorkAreaGroupId=bean.getInputBoxWorkAreaGroupId();
			 
			 Object[]  queryParams={workAreaId,siteId};
			 //loading BOQ updated details
			 List<Map<String, Object>> latestMatestMaterialBOQ=jdbcTemplate.queryForList(queryForLatestMaterialBOQDetails.toString(),queryParams);
			
			 
			 
			 //making list as empty for storing next iteration values 
			 issuedMaterialForThisWorkAreaList=new ArrayList<Double>();
			 double materialIssuedCumulativeQuantity=0.0;
			 Double materialIssuedQuantity=0.0;
			 double issuedMaterialForThisWorkArea=0.0;
			 List<Map<String, Object>> oldWOMaterialBOQDetails=jdbcTemplate.queryForList(queryForOldMaterialBOQDetails.toString(), new Object[]{tempIssueAreaDetailsId});
			 
			 for (int index = 0; index < oldWOMaterialBOQDetails.size(); index++) {
				 Map<String, Object> oldMaterialData=oldWOMaterialBOQDetails.get(index);
				 //Map<String, Object> newMaterialData=latestMatestMaterialBOQ.get(index);
				
				 for (Map<String, Object> newMaterialData : latestMatestMaterialBOQ) {
						
				
				 
				 double oldPerUnitQuantity=Double.valueOf(oldMaterialData.get("PER_UNIT_QUANTITY").toString());
				 double newPerUnitQuantity=Double.valueOf(newMaterialData.get("PER_UNIT_QUANTITY").toString());
				 
				 double oldTotalQuantity=Double.valueOf(oldMaterialData.get("TOTAL_QUANTITY").toString());
				 double newTotalQuantity=selectedArea*newPerUnitQuantity;
				 
				 blockId=oldMaterialData.get("BLOCK_ID")==null?"":oldMaterialData.get("BLOCK_ID").toString();
				 floorId=oldMaterialData.get("FLOOR_ID")==null?"":oldMaterialData.get("FLOOR_ID").toString();
				 flatId=oldMaterialData.get("FLAT_ID")==null?"":oldMaterialData.get("FLAT_ID").toString();
				 materialGroupId=oldMaterialData.get("MATERIAL_GROUP_ID")==null?"":oldMaterialData.get("MATERIAL_GROUP_ID").toString();
				 workDescId=oldMaterialData.get("WO_WORK_DESC_ID")==null?"":oldMaterialData.get("WO_WORK_DESC_ID").toString();
				 workDescName=oldMaterialData.get("WO_WORK_DESCRIPTION")==null?"":oldMaterialData.get("WO_WORK_DESCRIPTION").toString();
				 workAreaId= oldMaterialData.get("WO_WORK_AREA_ID")==null?"":oldMaterialData.get("WO_WORK_AREA_ID").toString();
				 String workArea=oldMaterialData.get("WORK_AREA")==null?"":oldMaterialData.get("WORK_AREA").toString();
				 if(materialGroupId.equals(newMaterialData.get("MATERIAL_GROUP_ID").toString())){
					 log.info("same material Group Id "+newMaterialData.get("MATERIAL_GROUP_ID").toString()+" "+materialGroupId);
					 //continue;
				
					 try {
						 //here loading buffer quantity
						 StringBuffer getWOBufferQuantity=new StringBuffer("select sum(QBS.QUANTITY) from QS_BUFFER_STOCK  QBS where QBS.SITE_ID=? AND  QBS.MATERIAL_GROUP_ID=? AND QBS.WO_WORK_AREA_ID=? ");
						  bufferQuantity=jdbcTemplate.queryForObject(getWOBufferQuantity.toString(), new Object[]{siteId,materialGroupId,workAreaId},Double.class);if(bufferQuantity==null){bufferQuantity=0.0;}
					} catch (Exception e) {
						log.info("Got Exception in buffer quntity no buffer quantity found :"+e.getMessage());
					}
				 
				 StringBuffer queryForMaterialIssuedQty=new StringBuffer("select  nvl(sum(IED.ISSUED_QTY),0)as ISSUED_QTY ")
						.append(" from INDENT_ENTRY_DETAILS IED ,QS_WO_WORKDESC QWW , INDENT_ENTRY IE  ,CHILD_PRODUCT CP,MEASUREMENT UOM  ,PRODUCT_GROUP_MASTER PGM ")
						.append(" where  UOM.MEASUREMENT_ID=IED.MEASUR_MNT_ID  AND CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID ")
						.append(" AND PGM.MATERIAL_GROUP_ID=IED.MATERIAL_GROUP_ID AND IED.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID ")
						.append(" AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID ")
						.append(" AND IE.WORK_ORDER_NUMBER =? and  IE.SITE_ID=?  AND IED.WO_WORK_DESC_ID is not null ");

				 if(blockId.length()!=0&&!blockId.equals("0")){queryForMaterialIssuedQty.append("AND IED.BLOCK_ID='"+blockId+"' ");}
				 if(floorId.length()!=0&&!floorId.equals("0")){queryForMaterialIssuedQty.append("AND IED.FLOOR_ID='"+floorId+"' ");}
				 if(flatId.length()!=0&&!flatId.equals("0")) {queryForMaterialIssuedQty.append("AND IED.FLAT_ID='"+flatId+"' ");}
				 if(materialGroupId.length()!=0){queryForMaterialIssuedQty.append("AND IED.MATERIAL_GROUP_ID='"+materialGroupId+"' ");}
				 if(workDescId.length()!=0){queryForMaterialIssuedQty.append("AND IED.WO_WORK_DESC_ID='"+workDescId+"' ");}
					
				 materialIssuedQuantity=jdbcTemplate.queryForObject(queryForMaterialIssuedQty.toString(), new Object[]{workOrderNumber,siteId},Double.class);
				 if(materialIssuedQuantity!=null&&materialIssuedQuantity!=0.0){
					 System.out.println(oldMaterialData.get("WORK_AREA").toString());
					 issuedMaterialForThisWorkArea=materialIssuedQuantity/(oldTotalQuantity+bufferQuantity)*(Double.valueOf(oldMaterialData.get("WORK_AREA").toString()));
				 }
				 issuedMaterialForThisWorkArea =Double.parseDouble(new DecimalFormat("##.###").format(issuedMaterialForThisWorkArea));
				//formula = issued quantity/total quantity*work area
				 issuedMaterialForThisWorkAreaList.add(issuedMaterialForThisWorkArea);
				//issuedMaterialForThisWorkArea
				 if(issuedMaterialForThisWorkArea>0&&operationType.length()==0){
					 double issuedMaterialForWorkArea=Collections.max(issuedMaterialForThisWorkAreaList);
					 errorMessage.put("MaterialIssued","can't remove row.@@"+workDescId+"##"+inputBoxWorkAreaGroupId+"##"+issuedMaterialForWorkArea+"##"+workDescName);
					 if(condition.equals("removeRow")){
						 return errorMessage;
					 }
				 }
				 
				
			 	
			/*	if material issued quantity is not zero
				then we are checking is material per unit quantity is changed or not 
				if latest BOQ per unit quantity is less than old BOQ per unit quantity,then system will not allow to revise the work order
				because material issued already for some area
				(if material is issued for work the system will not allow to revise the work order)
*/				if(materialIssuedQuantity!=null&&materialIssuedQuantity>0){
					/*if(selectedArea<issuedMaterialForThisWorkArea){
						errorMessage.put("IssuedSomeQuanity", "Some Material Issued for work area.@@"+workDescId+"##"+materialGroupId);	
					}*/
					if(newPerUnitQuantity<oldPerUnitQuantity){
						errorMessage.put("NewPerUnitLessThanOldPerUnit@@"+inputBoxWorkAreaGroupId,"can't revise work order.@@"+workDescId+"##"+inputBoxWorkAreaGroupId+"##"+issuedMaterialForThisWorkArea+"##"+workDescName+"##"+newPerUnitQuantity+"##"+oldPerUnitQuantity);
						return errorMessage;	
					}
				}
				
				log.info(" Iteration : "+index+" materialIssuedQuantity: "+materialIssuedQuantity+" oldTotalQuantity : "+oldTotalQuantity+" materialGroupId : "+materialGroupId+" issuedMaterial : "+issuedMaterialForThisWorkArea);
				
			  }//if condition
				//break;
		    }//inner for loop		
				 if( oldWOMaterialBOQDetails.size()==(index+1)){
					double issuedMaterialForWorkArea=Collections.max(issuedMaterialForThisWorkAreaList);
					if(selectedArea<issuedMaterialForWorkArea){
						errorMessage.put("IssuedMaterialQuanity@@"+inputBoxWorkAreaGroupId, "Some Material Issued for work area.@@"+workDescId+"##"+inputBoxWorkAreaGroupId+"##"+issuedMaterialForWorkArea+"##"+workDescName);
						log.info("Got error in  Material Issue for work area. "+workDescId+" material Issued  for "+issuedMaterialForWorkArea+ " Quantity and Selected Quantity is "+selectedArea+" Work Desc Name "+workDescName);
					}
				}
		}	
	}
		
 		return errorMessage;
	}
	
	@Override
	public List<Map<String, Object>> getMaterialBOQProductDetails(WorkOrderBean bean) {
		String finalWOnumber=bean.getWorkOrderNo().trim();
		String workOrderIssueId=bean.getQS_Temp_Issue_Id();
		String contractorId=bean.getContractorId().split(",")[0];
		String siteId=bean.getSiteId();
		String blockId="",floorId="",flatId="";
		String materialGroupId="",workDescId="",workAreadId="";
		String indentEntryId="";
		Double bufferQuantity=0.0;
		log.info(finalWOnumber+"-"+workOrderIssueId);
		
		StringBuffer queryForMaterialBOQ=new StringBuffer("")
				.append("SELECT SUM(QWTPD.TOTAL_QUANTITY) AS TOTAL_QUANTITY,PGM.MATERIAL_GROUP_ID,PGM.MATERIAL_GROUP_NAME,QWTID.WO_WORK_DESC_ID,QWTID.WO_MINORHEAD_ID,QWTID.WO_MAJORHEAD_ID,") 
				.append("QWTIAD.WO_WORK_AREA_ID ")
				.append(",B.NAME as BLOCK_NAME,F.NAME as FLOOR_NAME,FLAT.NAME as FLAT_NAME")
				.append(", PGM.MATERIAL_GROUP_MST_NAME,QWW.WO_WORK_DESCRIPTION,(case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID") 
				.append(" FROM")
				.append(" QS_WORKORDER_ISSUE  QWTI,")
				.append(" QS_WORKORDER_ISSUE_DETAILS QWTID,")
				.append(" QS_WORKORDER_ISSUE_AREA_DETAILS QWTIAD,")
				.append(" QS_WORKORDER_PRODUCT_DTLS QWTPD") 
				.append(" ,QS_WO_WORKDESC QWW ,PRODUCT_GROUP_MASTER PGM,")
				.append(" QS_BOQ_AREA_MAPPING QBAM left outer join  BLOCK B on B.BLOCK_ID = QBAM.BLOCK_ID")  
			    .append(" LEFT OUTER JOIN FLOOR F ON F.FLOOR_ID = QBAM.FLOOR_ID") 
				.append(" LEFT OUTER JOIN FLAT FLAT ON FLAT.FLAT_ID = QBAM.FLAT_ID") 
				.append(" WHERE QWTI.QS_WORKORDER_ISSUE_ID=QWTID.QS_WORKORDER_ISSUE_ID AND QWTID.WO_WORK_ISSUE_DTLS_ID=QWTIAD.WO_WORK_ISSUE_DTLS_ID")
				.append(" AND QWTPD.WO_WORK_ISSUE_AREA_DTLS_ID=QWTIAD.WO_WORK_ISSUE_AREA_DTLS_ID") 
				.append(" AND QWW.WO_WORK_DESC_ID=QWTID.WO_WORK_DESC_ID")  
				.append(" AND PGM.MATERIAL_GROUP_ID=QWTPD.MATERIAL_GROUP_ID") 
				.append(" AND QBAM.WO_WORK_AREA_ID=QWTIAD.WO_WORK_AREA_ID")
				.append(" AND QWTI.WORK_ORDER_NUMBER=? AND QWTI.SITE_ID=? AND QWTI.STATUS IN ('I','DF','A') GROUP BY QWTID.WO_WORK_DESC_ID, QWTID.WO_MINORHEAD_ID, QWTID.WO_MAJORHEAD_ID, QWTIAD.WO_WORK_AREA_ID ,") 
				.append(" B.NAME, F.NAME, FLAT.NAME,PGM.MATERIAL_GROUP_ID,PGM.MATERIAL_GROUP_NAME, PGM.MATERIAL_GROUP_MST_NAME,") 
				.append(" QWW.WO_WORK_DESCRIPTION ,")
				.append(" (CASE WHEN B.BLOCK_ID  IS NULL THEN '0'  ELSE B.BLOCK_ID END),(CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END),")
				.append(" (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END)")
			    .append(" ORDER BY (CASE WHEN B.BLOCK_ID  IS NULL THEN '0'  ELSE B.BLOCK_ID END),")
			    .append(" (CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END),")
			    .append(" (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END)");
		
		// jdbcTemplate.queryForList(queryForMaterialBOQ.toString(),finalWOnumber,siteId);
		
/*		StringBuffer query=new StringBuffer().append("select  sum(IED.ISSUED_QTY)as ISSUED_QTY  ,IED.INDENT_ENTRY_ID,B.NAME as BLOCK_NAME,F.NAME as FLOOR_NAME,FLAT.NAME as FLAT_NAME,PGM.MATERIAL_GROUP_NAME, PGM.MATERIAL_GROUP_MST_NAME,QWW.WO_WORK_DESCRIPTION,IED.MATERIAL_GROUP_ID,IED.WO_WORK_DESC_ID  ")
				.append(" ,(case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID ")
				.append(" from INDENT_ENTRY_DETAILS IED left outer join  BLOCK B on B.BLOCK_ID = IED.BLOCK_ID ")
				.append(" left outer join FLOOR F on F.FLOOR_ID = IED.FLOOR_ID ")
				.append(" left outer join FLAT FLAT on FLAT.FLAT_ID = IED.FLAT_ID ,QS_WO_WORKDESC QWW , SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY IE ,CHILD_PRODUCT CP,MEASUREMENT UOM  ,PRODUCT_GROUP_MASTER PGM ")
				.append(" where  UOM.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT  AND CP.CHILD_PRODUCT_ID=SPL.CHILD_PRODUCT_ID    ")
				.append(" AND PGM.MATERIAL_GROUP_ID=IED.MATERIAL_GROUP_ID AND IED.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID")
				.append(" AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID  AND SPL.PRICE_ID=IED.PRICE_ID AND IED.IS_RECOVERABLE='Yes' ")
				.append(" AND IE.CONTRACTOR_ID=? AND IE.WORK_ORDER_NUMBER = ? and  IE.SITE_ID=?  AND IED.WO_WORK_DESC_ID is not null")
				.append(" group by IED.INDENT_ENTRY_ID,B.NAME  ,F.NAME ,FLAT.NAME  ,(case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),(case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),(case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end) ,PGM.MATERIAL_GROUP_NAME, PGM.MATERIAL_GROUP_MST_NAME,QWW.WO_WORK_DESCRIPTION,IED.MATERIAL_GROUP_ID,IED.WO_WORK_DESC_ID ")
				.append(" order by (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),(case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),(case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");
*/		
		//List<Map<String, Object>> list=jdbcTemplate.queryForList(query.toString(),contractorId,finalWOnumber,siteId);
		List<Map<String, Object>> list=jdbcTemplate.queryForList(queryForMaterialBOQ.toString(),finalWOnumber,siteId);
		int count=0;
		for (Map<String, Object> map : list) {
			 blockId=map.get("BLOCK_ID")==null?"":map.get("BLOCK_ID").toString();
			 floorId=map.get("FLOOR_ID")==null?"":map.get("FLOOR_ID").toString();
			 flatId=map.get("FLAT_ID")==null?"":map.get("FLAT_ID").toString();
			 materialGroupId=map.get("MATERIAL_GROUP_ID")==null?"":map.get("MATERIAL_GROUP_ID").toString();
			 workDescId=map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString();
			 workAreadId=map.get("WO_WORK_AREA_ID")==null?"":map.get("WO_WORK_AREA_ID").toString();
			 //indentEntryId=map.get("INDENT_ENTRY_ID")==null?"":map.get("INDENT_ENTRY_ID").toString();
			 try {
				 StringBuffer getWOBufferQuantity=new StringBuffer("SELECT SUM(QBS.QUANTITY) FROM QS_BUFFER_STOCK  QBS WHERE QBS.SITE_ID=? AND  QBS.MATERIAL_GROUP_ID=? AND QBS.WO_WORK_AREA_ID=? ");
				  bufferQuantity=jdbcTemplate.queryForObject(getWOBufferQuantity.toString(), new Object[]{siteId,materialGroupId,workAreadId},Double.class);
				  if(bufferQuantity==null){bufferQuantity=0.0;}
			} catch (Exception e) {
				log.info("Got Exception in buffer quntity no buffer quantity found :"+e.getMessage());
			}
			StringBuffer  queryForQtyAndProdName=new StringBuffer("").append("SELECT  SUM(TO_NUMBER(IED.ISSUED_QTY)) AS ISSUED_QTY,CP.NAME ")
					.append(" FROM INDENT_ENTRY_DETAILS IED  ,CHILD_PRODUCT CP,INDENT_ENTRY IE")
					.append(" WHERE CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID ")
					.append(" AND IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID ")
					.append(" AND  IED.WO_WORK_DESC_ID IS NOT NULL  AND IED.WO_WORK_DESC_ID=?  ").append(" AND  IE.WORK_ORDER_NUMBER=?").append(" AND  IE.SITE_ID=?");
			if(blockId.length()!=0&&!blockId.equals("0")){queryForQtyAndProdName.append("AND IED.BLOCK_ID='"+blockId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.BLOCK_ID is null ");}
			if(floorId.length()!=0&&!floorId.equals("0")){queryForQtyAndProdName.append("AND IED.FLOOR_ID='"+floorId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.FLOOR_ID is null ");}
			if(flatId.length()!=0&&!flatId.equals("0")) {queryForQtyAndProdName.append("AND IED.FLAT_ID='"+flatId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.FLAT_ID is null ");}
			queryForQtyAndProdName.append("AND IED.MATERIAL_GROUP_ID='"+materialGroupId+"' GROUP BY CP.NAME ");
			
			List<Map<String, Object>> qtyAndProdNameList=jdbcTemplate.queryForList(queryForQtyAndProdName.toString(),workDescId,finalWOnumber,siteId);
			map.put("QtyAndProdName", qtyAndProdNameList);//Product Name and Quantity material group wise data
			
			StringBuffer queryForTotalQuantity=new StringBuffer("SELECT SUM(IED.ISSUED_QTY)AS ISSUED_QTY,IED.MATERIAL_GROUP_ID,IED.WO_WORK_DESC_ID ")
					.append(" FROM INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE")
					.append(" WHERE IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID ")
					.append(" AND IED.WO_WORK_DESC_ID=? AND IED.MATERIAL_GROUP_ID=? ")
					.append(" AND  IE.WORK_ORDER_NUMBER=? AND IE.SITE_ID=? ");
			if(blockId.length()!=0&&!blockId.equals("0")){queryForTotalQuantity.append("AND IED.BLOCK_ID='"+blockId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.BLOCK_ID is null ");}
			if(floorId.length()!=0&&!floorId.equals("0")){queryForTotalQuantity.append("AND IED.FLOOR_ID='"+floorId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.FLOOR_ID is null ");}
			if(flatId.length()!=0&&!flatId.equals("0")) {queryForTotalQuantity.append("AND IED.FLAT_ID='"+flatId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.FLAT_ID is null ");}
			queryForTotalQuantity.append(" group by IED.MATERIAL_GROUP_ID,IED.WO_WORK_DESC_ID ");
			
			List<Map<String, Object>> totalQuantityList=jdbcTemplate.queryForList(queryForTotalQuantity.toString(),workDescId,materialGroupId,finalWOnumber,siteId);
			
			for (Map<String, Object> map2 : totalQuantityList) {
				String totalQuantity=map2.get("ISSUED_QTY")==null?"":map2.get("ISSUED_QTY").toString();
				Map<String, Object> tempMap =list.get(count);
				count++;
				tempMap.put("ISSUED_QTY", totalQuantity);
				tempMap.put("Buffer_QTY", bufferQuantity);
			}
			if(totalQuantityList.size()==0){
				Map<String, Object> tempMap =list.get(count);
				tempMap.put("ISSUED_QTY", 0.0);
				tempMap.put("Buffer_QTY", bufferQuantity);
				count++;
			}
		
		}
		
		return list;
	}
	
/* 	@Override
	public List<Map<String, Object>> getMaterialBOQProductDetails(WorkOrderBean bean) {
		
		String finalWOnumber=bean.getWorkOrderNo().trim();
		String workOrderIssueId=bean.getQS_Temp_Issue_Id();
		String contractorId=bean.getContractorId().split(",")[0];
		String siteId=bean.getSiteId();
		String blockId="",floorId="",flatId="";
		String materialGroupId="",workDescId="";
		String indentEntryId="";
		log.info(finalWOnumber+"-"+workOrderIssueId);
		
		StringBuffer query=new StringBuffer().append("select  sum(IED.ISSUED_QTY)as ISSUED_QTY  ,IED.INDENT_ENTRY_ID,B.NAME as BLOCK_NAME,F.NAME as FLOOR_NAME,FLAT.NAME as FLAT_NAME,PGM.MATERIAL_GROUP_NAME, PGM.MATERIAL_GROUP_MST_NAME,QWW.WO_WORK_DESCRIPTION,IED.MATERIAL_GROUP_ID,IED.WO_WORK_DESC_ID  ")
				.append(" ,(case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID ")
				.append(" from INDENT_ENTRY_DETAILS IED left outer join  BLOCK B on B.BLOCK_ID = IED.BLOCK_ID ")
				.append(" left outer join FLOOR F on F.FLOOR_ID = IED.FLOOR_ID ")
				.append(" left outer join FLAT FLAT on FLAT.FLAT_ID = IED.FLAT_ID ,QS_WO_WORKDESC QWW , SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY IE ,CHILD_PRODUCT CP,MEASUREMENT UOM  ,PRODUCT_GROUP_MASTER PGM ")
				.append(" where  UOM.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT  AND CP.CHILD_PRODUCT_ID=SPL.CHILD_PRODUCT_ID    ")
				.append(" AND PGM.MATERIAL_GROUP_ID=IED.MATERIAL_GROUP_ID AND IED.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID")
				.append(" AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID  AND SPL.PRICE_ID=IED.PRICE_ID AND IED.IS_RECOVERABLE='Yes' ")
				.append(" AND IE.CONTRACTOR_ID='"+contractorId+"' AND IE.WORK_ORDER_NUMBER = '"+finalWOnumber+"' and  IE.SITE_ID='"+siteId+"'  AND IED.WO_WORK_DESC_ID is not null")
				.append(" group by IED.INDENT_ENTRY_ID,B.NAME  ,F.NAME ,FLAT.NAME  ,(case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),(case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),(case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end) ,PGM.MATERIAL_GROUP_NAME, PGM.MATERIAL_GROUP_MST_NAME,QWW.WO_WORK_DESCRIPTION,IED.MATERIAL_GROUP_ID,IED.WO_WORK_DESC_ID ")
				.append(" order by (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),(case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),(case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");
		
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query.toString());
		int count=0;
		for (Map<String, Object> map : list) {
			 blockId=map.get("BLOCK_ID")==null?"":map.get("BLOCK_ID").toString();
			 floorId=map.get("FLOOR_ID")==null?"":map.get("FLOOR_ID").toString();
			 flatId=map.get("FLAT_ID")==null?"":map.get("FLAT_ID").toString();
			 materialGroupId=map.get("MATERIAL_GROUP_ID")==null?"":map.get("MATERIAL_GROUP_ID").toString();
			 workDescId=map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString();
			 indentEntryId=map.get("INDENT_ENTRY_ID")==null?"":map.get("INDENT_ENTRY_ID").toString();
			
			StringBuffer  queryForQtyAndProdName=new StringBuffer("").append("select  IED.ISSUED_QTY ,CP.NAME ")
					.append(" from INDENT_ENTRY_DETAILS IED  ,CHILD_PRODUCT CP")
					.append(" where CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID ")
					.append(" AND  IED.IS_RECOVERABLE='Yes' AND IED.INDENT_ENTRY_ID='"+indentEntryId+"' ")
					.append(" AND  IED.WO_WORK_DESC_ID is not null ");
			if(blockId.length()!=0&&!blockId.equals("0")){queryForQtyAndProdName.append("AND IED.BLOCK_ID='"+blockId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.BLOCK_ID is null ");}
			if(floorId.length()!=0&&!floorId.equals("0")){queryForQtyAndProdName.append("AND IED.FLOOR_ID='"+floorId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.FLOOR_ID is null ");}
			if(flatId.length()!=0&&!flatId.equals("0")) {queryForQtyAndProdName.append("AND IED.FLAT_ID='"+flatId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.FLAT_ID is null ");}
			queryForQtyAndProdName.append("AND IED.MATERIAL_GROUP_ID='"+materialGroupId+"'");
			
			List<Map<String, Object>> qtyAndProdNameList=jdbcTemplate.queryForList(queryForQtyAndProdName.toString());
			map.put("QtyAndProdName", qtyAndProdNameList);
			
			StringBuffer queryForTotalQuantity=new StringBuffer("select sum(QWPD.TOTAL_QUANTITY) as TOTAL_QUANTITY,QWPD.MATERIAL_GROUP_ID from QS_WORKORDER_ISSUE_DETAILS QWID,QS_WORKORDER_ISSUE_AREA_DETAILS QWIAD, ")
					.append(" QS_WORKORDER_PRODUCT_DTLS QWPD,QS_BOQ_AREA_MAPPING QBAM ")
					.append(" where QWID.WO_WORK_ISSUE_DTLS_ID=QWIAD.WO_WORK_ISSUE_DTLS_ID ")
					.append(" AND QWIAD.WO_WORK_ISSUE_AREA_DTLS_ID=QWPD.WO_WORK_ISSUE_AREA_DTLS_ID ")
					.append(" AND QBAM.WO_WORK_AREA_ID=QWIAD.WO_WORK_AREA_ID ")
					.append(" AND QS_WORKORDER_ISSUE_ID='"+workOrderIssueId+"' ")
					.append(" AND QWID.WO_WORK_DESC_ID='"+workDescId+"' ");
			if(blockId.length()!=0&&!blockId.equals("0")){queryForTotalQuantity.append("AND QBAM.BLOCK_ID='"+blockId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.BLOCK_ID is null ");}
			if(floorId.length()!=0&&!floorId.equals("0")){queryForTotalQuantity.append("AND QBAM.FLOOR_ID='"+floorId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.FLOOR_ID is null ");}
			if(flatId.length()!=0&&!flatId.equals("0")) {queryForTotalQuantity.append("AND QBAM.FLAT_ID='"+flatId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.FLAT_ID is null ");}
			queryForTotalQuantity.append("AND QWPD.MATERIAL_GROUP_ID='"+materialGroupId+"' group by QWPD.MATERIAL_GROUP_ID ");
			
			List<Map<String, Object>> totalQuantityList=jdbcTemplate.queryForList(queryForTotalQuantity.toString());
			
			for (Map<String, Object> map2 : totalQuantityList) {
				String totalQuantity=map2.get("TOTAL_QUANTITY")==null?"":map2.get("TOTAL_QUANTITY").toString();
				Map<String, Object> tempMap =list.get(count);count++;
				tempMap.put("TOTAL_QUANTITY", totalQuantity);
			}
			if(totalQuantityList.size()==0){
				count++;
			}
		
		}
		
		return list;
	}*/
	
	/**
	 * @description this method is for loading work desc wise Group wise total material quantity
	 */
	@Override
	public List<Map<String, Object>> getTempWorkDescMaterialBOQProductDetails(WorkOrderBean bean) {
		String workDescId="";
	 	String siteId=bean.getSiteId();
		workDescId= bean.getWO_Desc1();
 		log.info("inside WorkOrderDaoImpl.getTempMaterialBOQProductDetails() "+siteId+" "+workDescId );
		StringBuffer query=new StringBuffer("select sum(QBPD.TOTAL_QUANTITY) as TOTAL_QUANTITY ,PGM.MATERIAL_GROUP_ID,PGM.MATERIAL_GROUP_NAME,PGM.MATERIAL_GROUP_MST_NAME,QWW.WO_WORK_DESC_ID,QWW.WO_WORK_DESCRIPTION")
		.append(" from QS_WO_WORKDESC QWW ,PRODUCT_GROUP_MASTER PGM ,QS_BOQ_PRODUCT_DTLS QBPD,QS_BOQ QB,QS_BOQ_DETAILS QBD,QS_BOQ_AREA_MAPPING QBAM")
		.append(" where PGM.MATERIAL_GROUP_ID=QBPD.MATERIAL_GROUP_ID AND PGM.MATERIAL_GROUP_MEASUREMENT_ID=QBPD.MATERIAL_GROUP_MEASUREMENT_ID")
		.append(" AND QBAM.WO_WORK_AREA_ID=QBPD.WO_WORK_AREA_ID")
		.append(" AND QB.BOQ_SEQ_NO=QBD.BOQ_SEQ_NO and QBD.BOQ_SEQ_NO=QBAM.BOQ_SEQ_NO")
		.append(" AND QWW.WO_WORK_DESC_ID=QBAM.WO_WORK_DESC_ID")
		.append(" AND QB.STATUS='A'")
		.append(" AND QBAM.SITE_ID=? ANd QBAM.WO_WORK_DESC_ID=?") 
		.append(" group by PGM.MATERIAL_GROUP_ID,PGM.MATERIAL_GROUP_NAME,PGM.MATERIAL_GROUP_ID, PGM.MATERIAL_GROUP_NAME, PGM.MATERIAL_GROUP_MST_NAME,QWW.WO_WORK_DESC_ID,QWW.WO_WORK_DESCRIPTION");
		
 		List<Map<String, Object>> list=jdbcTemplate.queryForList(query.toString(),siteId,workDescId);

		return list;
	}
	
	/**
	 * @description 
	 * this code is used for group wise area material details 
	 */
	@Override
	public List<Map<String, Object>> getTempWorkAreaWiseMaterialDetails(WorkOrderBean bean) {
	 	String materialGroupId="",workDescId="",workAreadId="";
	 	String siteId=bean.getSiteId();
		workDescId= bean.getWO_Desc1();
		materialGroupId=bean.getMaterialGroupId();
 		log.info("inside WorkOrderDaoImpl.getTempMaterialBOQProductDetails() : "+siteId+" : "+workDescId+" : "+materialGroupId);
		
		StringBuffer query=new StringBuffer("");
		query.append(" select sum(QBPD.TOTAL_QUANTITY) as TOTAL_QUANTITY ,PGM.MATERIAL_GROUP_ID,PGM.MATERIAL_GROUP_NAME ,B.NAME as BLOCK_NAME,F.NAME as FLOOR_NAME,FLAT.NAME as FLAT_NAME, PGM.MATERIAL_GROUP_MST_NAME,QWW.WO_WORK_DESCRIPTION,")
			.append(" (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID") 
			.append(" from QS_WO_WORKDESC QWW ,PRODUCT_GROUP_MASTER PGM ,QS_BOQ_PRODUCT_DTLS QBPD,QS_BOQ QB,QS_BOQ_DETAILS QBD, QS_BOQ_AREA_MAPPING QBAM")
			.append(" left outer join  BLOCK B on B.BLOCK_ID = QBAM.BLOCK_ID ")
			.append(" left outer join FLOOR F on F.FLOOR_ID = QBAM.FLOOR_ID ")
			.append(" left outer join FLAT FLAT on FLAT.FLAT_ID = QBAM.FLAT_ID") 
			.append(" where PGM.MATERIAL_GROUP_ID=QBPD.MATERIAL_GROUP_ID AND PGM.MATERIAL_GROUP_MEASUREMENT_ID=QBPD.MATERIAL_GROUP_MEASUREMENT_ID")
			.append(" AND QBAM.WO_WORK_AREA_ID=QBPD.WO_WORK_AREA_ID")
			.append(" AND QB.BOQ_SEQ_NO=QBD.BOQ_SEQ_NO and QBD.BOQ_SEQ_NO=QBAM.BOQ_SEQ_NO")
			.append(" AND QWW.WO_WORK_DESC_ID=QBAM.WO_WORK_DESC_ID")
			.append(" AND QB.STATUS='A'")
			.append(" AND QBAM.SITE_ID=? ANd QBAM.WO_WORK_DESC_ID=? AND PGM.MATERIAL_GROUP_ID=? ")
			.append(" group by PGM.MATERIAL_GROUP_ID,PGM.MATERIAL_GROUP_NAME,B.NAME, F.NAME, FLAT.NAME,PGM.MATERIAL_GROUP_ID, PGM.MATERIAL_GROUP_NAME, PGM.MATERIAL_GROUP_MST_NAME, QWW.WO_WORK_DESCRIPTION ,") 
			.append(" (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),")
			.append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),") 
			.append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)")
			.append(" order by (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),")
			.append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),")
			.append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");
			
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query.toString(),siteId,workDescId,materialGroupId);
		
		return list;
	}
	
	
	/**
	 * @description this method is for returning temporary material BOQ product Details
	 * if this is revised work order we are loading previous issued quantity
	 */
	@Override
	public List<Map<String, Object>> getTempMaterialBOQProductDetails(WorkOrderBean bean) {
		String finalWOnumber=bean.getWorkOrderNo().trim();
		String siteId=bean.getSiteId();
		String blockId="",floorId="",flatId="";
		String materialGroupId="",workDescId="",workAreadId="";
		Double bufferQuantity=0.0;
		String revisionOfWorkOrder=bean.getRevision()==null?"":bean.getRevision().trim();
		log.info("inside WorkOrderDaoImpl.getTempMaterialBOQProductDetails()");
		
		StringBuffer query=new StringBuffer("SELECT SUM(QWTPD.TOTAL_QUANTITY) AS TOTAL_QUANTITY,PGM.MATERIAL_GROUP_ID,PGM.MATERIAL_GROUP_NAME,QWTID.WO_WORK_DESC_ID,QWTID.WO_MINORHEAD_ID,QWTID.WO_MAJORHEAD_ID,")
		.append("QWTIAD.WO_WORK_AREA_ID ")
		.append(",B.NAME AS BLOCK_NAME,F.NAME AS FLOOR_NAME,FLAT.NAME AS FLAT_NAME")
		.append(", PGM.MATERIAL_GROUP_MST_NAME,QWW.WO_WORK_DESCRIPTION,(CASE WHEN B.BLOCK_ID  IS NULL THEN '0'  ELSE B.BLOCK_ID END)AS BLOCK_ID,   (CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END)AS FLOOR_ID,    (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END)AS  FLAT_ID")
		.append(" FROM  QS_WORKORDER_TEMP_ISSUE  QWTI,QS_WORKORDER_TEMP_ISSUE_DTLS QWTID,")
		.append(" QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QWTIAD,QS_WORKORDER_TEMP_PRODUCT_DTLS QWTPD")
		.append(" ,QS_WO_WORKDESC QWW ,PRODUCT_GROUP_MASTER PGM,")
		.append(" QS_BOQ_AREA_MAPPING QBAM LEFT OUTER JOIN  BLOCK B ON B.BLOCK_ID = QBAM.BLOCK_ID")
		.append(" LEFT OUTER JOIN FLOOR F ON F.FLOOR_ID = QBAM.FLOOR_ID")
		.append(" LEFT OUTER JOIN FLAT FLAT ON FLAT.FLAT_ID = QBAM.FLAT_ID")
		.append(" WHERE QWTI.QS_WO_TEMP_ISSUE_ID=QWTID.QS_WO_TEMP_ISSUE_ID AND QWTID.WO_WORK_TEMP_ISSUE_DTLS_ID=QWTIAD.WO_WORK_TEMP_ISSUE_DTLS_ID")
		.append(" AND QWTPD.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID=QWTIAD.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID") 
		.append(" AND QWW.WO_WORK_DESC_ID=QWTID.WO_WORK_DESC_ID")
		.append(" AND PGM.MATERIAL_GROUP_ID=QWTPD.MATERIAL_GROUP_ID")
		.append(" AND QBAM.WO_WORK_AREA_ID=QWTIAD.WO_WORK_AREA_ID")
		.append(" AND QWTI.PER_WORK_ORDER_NUMBER=? AND QWTI.SITE_ID=? AND QWTI.STATUS IN ('I','DF','A','R') GROUP BY QWTID.WO_WORK_DESC_ID, QWTID.WO_MINORHEAD_ID, QWTID.WO_MAJORHEAD_ID, QWTIAD.WO_WORK_AREA_ID ,") 
		.append(" B.NAME, F.NAME, FLAT.NAME,PGM.MATERIAL_GROUP_ID, PGM.MATERIAL_GROUP_NAME, PGM.MATERIAL_GROUP_MST_NAME,") 
		.append(" QWW.WO_WORK_DESCRIPTION ,")
		.append(" (CASE WHEN B.BLOCK_ID  IS NULL THEN '0'  ELSE B.BLOCK_ID END),(CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END),")
		.append(" (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END)")
	    .append(" ORDER BY (CASE WHEN B.BLOCK_ID  IS NULL THEN '0'  ELSE B.BLOCK_ID END),")
	    .append(" (CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END),")
	    .append(" (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END)");
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query.toString(),finalWOnumber,siteId);
		if(!finalWOnumber.contains("/R")){
			return list;
		}
		int count=0;
		log.info("finalWOnumber : "+finalWOnumber+" siteId : "+siteId+" revisionOfWorkOrder : "+revisionOfWorkOrder);
		finalWOnumber=jdbcTemplate.queryForObject("Select OLD_WORK_ORDER_NUMBER from QS_WORKORDER_TEMP_ISSUE where STATUS in ('A','I','DF') AND PER_WORK_ORDER_NUMBER=? AND SITE_ID=? ", new Object[]{finalWOnumber,siteId}, String.class);
		
		for (Map<String, Object> map : list) {
			 blockId=map.get("BLOCK_ID")==null?"":map.get("BLOCK_ID").toString();
			 floorId=map.get("FLOOR_ID")==null?"":map.get("FLOOR_ID").toString();
			 flatId=map.get("FLAT_ID")==null?"":map.get("FLAT_ID").toString();
			 materialGroupId=map.get("MATERIAL_GROUP_ID")==null?"":map.get("MATERIAL_GROUP_ID").toString();
			 workDescId=map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString();
			 workAreadId=map.get("WO_WORK_AREA_ID")==null?"":map.get("WO_WORK_AREA_ID").toString();
			 try {
				 StringBuffer getWOBufferQuantity=new StringBuffer("SELECT SUM(QBS.QUANTITY) FROM QS_BUFFER_STOCK  QBS WHERE QBS.SITE_ID=? AND  QBS.MATERIAL_GROUP_ID=? AND QBS.WO_WORK_AREA_ID=? ");
				  bufferQuantity=jdbcTemplate.queryForObject(getWOBufferQuantity.toString(), new Object[]{siteId,materialGroupId,workAreadId},Double.class);
				  if(bufferQuantity==null){bufferQuantity=0.0;}
				  map.put("Buffer_QTY", bufferQuantity);
			} catch (Exception e) {
				log.info("Got Exception in buffer quntity no buffer quantity found :"+e.getMessage());
			}
			 
			 StringBuffer  queryForQtyAndProdName=new StringBuffer("").append("select  sum(to_number(IED.ISSUED_QTY)) as ISSUED_QTY,CP.NAME ")
						.append(" from INDENT_ENTRY_DETAILS IED  ,CHILD_PRODUCT CP,INDENT_ENTRY IE")
						.append(" where CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID ")
						.append(" AND IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID ")
						.append(" AND  IED.WO_WORK_DESC_ID is not null AND IED.WO_WORK_DESC_ID=? AND IED.MATERIAL_GROUP_ID=?")
						.append(" AND  IE.WORK_ORDER_NUMBER=?").append(" AND  IE.SITE_ID=?");
				if(blockId.length()!=0&&!blockId.equals("0")){queryForQtyAndProdName.append("AND IED.BLOCK_ID='"+blockId+"' ");}
				//else{queryForTotalQuantity.append("AND QBAM.BLOCK_ID is null ");}
				if(floorId.length()!=0&&!floorId.equals("0")){queryForQtyAndProdName.append("AND IED.FLOOR_ID='"+floorId+"' ");}
				//else{queryForTotalQuantity.append("AND QBAM.FLOOR_ID is null ");}
				if(flatId.length()!=0&&!flatId.equals("0")) {queryForQtyAndProdName.append("AND IED.FLAT_ID='"+flatId+"' ");}
				//else{queryForTotalQuantity.append("AND QBAM.FLAT_ID is null ");}
				queryForQtyAndProdName.append(" group by CP.NAME  ");
				
				List<Map<String, Object>> qtyAndProdNameList=jdbcTemplate.queryForList(queryForQtyAndProdName.toString(),workDescId,materialGroupId,finalWOnumber,siteId);
				map.put("QtyAndProdName", qtyAndProdNameList);
			
			StringBuffer queryForTotalQuantity=new StringBuffer("SELECT SUM(IED.ISSUED_QTY)AS ISSUED_QTY,IED.MATERIAL_GROUP_ID,IED.WO_WORK_DESC_ID ")
					.append(" FROM INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE")
					.append(" WHERE IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID ")
					.append(" AND IED.WO_WORK_DESC_ID=? AND IED.MATERIAL_GROUP_ID=? ")
					.append(" AND  IE.WORK_ORDER_NUMBER=? AND IE.SITE_ID=? ");
			if(blockId.length()!=0&&!blockId.equals("0")){queryForTotalQuantity.append("AND IED.BLOCK_ID='"+blockId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.BLOCK_ID is null ");}
			if(floorId.length()!=0&&!floorId.equals("0")){queryForTotalQuantity.append("AND IED.FLOOR_ID='"+floorId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.FLOOR_ID is null ");}
			if(flatId.length()!=0&&!flatId.equals("0")) {queryForTotalQuantity.append("AND IED.FLAT_ID='"+flatId+"' ");}
			//else{queryForTotalQuantity.append("AND QBAM.FLAT_ID is null ");}
			queryForTotalQuantity.append(" group by IED.MATERIAL_GROUP_ID,IED.WO_WORK_DESC_ID ");
			
			List<Map<String, Object>> totalQuantityList=jdbcTemplate.queryForList(queryForTotalQuantity.toString(),workDescId,materialGroupId,finalWOnumber,siteId);
			
			for (Map<String, Object> map2 : totalQuantityList) {
				String totalQuantity=map2.get("ISSUED_QTY")==null?"":map2.get("ISSUED_QTY").toString();
				Map<String, Object> tempMap =list.get(count);
				count++;
				tempMap.put("ISSUED_QTY", totalQuantity);
			}
			if(totalQuantityList.size()==0){
				Map<String, Object> tempMap =list.get(count);
				tempMap.put("ISSUED_QTY", 0.0);
				count++;
			}
		}
		
		return list;

	}
	
	/**
	 * @author Aniket Chavan
	 * @description this method is used to delete the inserted data 
	 * @param workOrderNo is holding the work order number for checking 
	 * @param siteId is supporting to query while checking work order number
	 * @return String true or false
	 */
	
	@Override
	public int deleteWOCommitedData(String workOrderTMPIssurPK,String siteId) {
		String query="delete  FROM QS_WORKORDER_TEMP_ISSUE WHERE QS_WO_TEMP_ISSUE_ID=? and SITE_ID=?";
		int count=jdbcTemplate.update(query,workOrderTMPIssurPK,siteId);
		return count;
	}
	
	
	/**
	 * @author Aniket Chavan
	 * @description this method is used to select the contractor details
	 * @param contractor_name is used for loading the data based on name if name exists 
	 * @param loadContractorData is the boolean value to load the data or nor
	 * @param event is the variable which is telling for what data we should load  
	 * @return list implement class
	 */
	
	@Override
	public List<String> getContractorInfo( final String contractor_name, boolean loadContractorData, String event) {
		String contractorInfoQry = "";
		if(event.equals("autoCompleteFunction")){
			//request from indent return page
			 contractorInfoQry = "SELECT CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME FROM SUMADHURA_CONTRACTOR WHERE upper(CONCAT(FIRST_NAME,' '||LAST_NAME)) like upper('%"+contractor_name+"%') and rownum<20";
			return jdbcTemplate.query(contractorInfoQry, new ResultSetExtractor<List<String>>() {

				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<String> list = new ArrayList<String>();
					while (rs.next()) {
						list.add(rs.getString(1));
					}
					return list;
				}
			});
		}else	if(event.equals("getContractorId")){
			String loadContractorInfoQry = "SELECT CONTRACTOR_ID ,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME,ADDRESS,TDS_PERCENTAGE,GSTIN,PAN_NUMBER,MOBILE_NUMBER,BANK_ACC_NUMBER,BANK_NAME,IFSC_CODE,GSTIN FROM SUMADHURA_CONTRACTOR "
					  + "WHERE upper(CONCAT(FIRST_NAME,' '||LAST_NAME))=upper('"+contractor_name+"') and rownum<20" ;
		return  jdbcTemplate.query(loadContractorInfoQry, new ResultSetExtractor<List<String>>() {

				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<String> vendorList = new ArrayList<String>();

					while (rs.next()) {
						String contractor_id = rs.getString("CONTRACTOR_ID");
						String contractor_address = rs.getString("ADDRESS") == null ? "" : rs.getString("ADDRESS");
						String mobileNo = rs.getString("MOBILE_NUMBER") == null ? " " : rs.getString("MOBILE_NUMBER");
						String panNo = rs.getString("PAN_NUMBER") == null ? " " : rs.getString("PAN_NUMBER");
						String acc_no=rs.getString("BANK_ACC_NUMBER")==null?"-":rs.getString("BANK_ACC_NUMBER");
						String ifsc_code=rs.getString("IFSC_CODE")==null?"-":rs.getString("IFSC_CODE");
						String GSTIN=rs.getString("GSTIN")==null?"-":rs.getString("GSTIN");
						StringBuffer vendorData = new StringBuffer().append(contractor_id + "@@" + contractor_name + "@@"
						+ contractor_address +"@@"+ mobileNo + "@@" + panNo+ "@@" +acc_no+ "@@" +ifsc_code+ "@@" +GSTIN);
						
					
						vendorList.add(vendorData.toString());
					}
					return vendorList;
				}
			});
		}
		
		 contractorInfoQry = "SELECT CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME FROM SUMADHURA_CONTRACTOR WHERE upper(CONCAT(FIRST_NAME,' '||LAST_NAME)) like upper('%"+contractor_name+"%') and STATUS='A' and rownum<20";

		List<String> dbContractorList = new ArrayList<String>();
		// if load contractor data is false then select the data for auto complete text box
		if (!loadContractorData) {

			dbContractorList = jdbcTemplate.query(contractorInfoQry, new ResultSetExtractor<List<String>>() {

				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<String> list = new ArrayList<String>();
					while (rs.next()) {
						list.add(rs.getString(1));
					}
					return list;
				}
			});
		} else {
			
			StringBuffer loadContractorInfoQry = new StringBuffer().append("SELECT TDS_PERCENTAGE,CONTRACTOR_ID ,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME,ADDRESS,TDS_PERCENTAGE,GSTIN,PAN_NUMBER,MOBILE_NUMBER,BANK_ACC_NUMBER,BANK_NAME,IFSC_CODE FROM SUMADHURA_CONTRACTOR ")
			     .append("WHERE upper(CONCAT(FIRST_NAME,' '||LAST_NAME))=upper(?) and rownum<20 and STATUS='A'" );
				dbContractorList = jdbcTemplate.query(loadContractorInfoQry.toString(), new Object[]{contractor_name}, new ResultSetExtractor<List<String>>() {

				@Override
				public List<String> extractData(ResultSet rs) throws SQLException, DataAccessException {
					List<String> vendorList = new ArrayList<String>();

					while (rs.next()) {
						String contractor_id = rs.getString("CONTRACTOR_ID");
						String contractor_address = rs.getString("ADDRESS") == null ? "-" : rs.getString("ADDRESS");
						String mobileNo = rs.getString("MOBILE_NUMBER") == null ? "-" : rs.getString("MOBILE_NUMBER");
						String panNo = rs.getString("PAN_NUMBER") == null ? " " : rs.getString("PAN_NUMBER");
						String acc_no=rs.getString("BANK_ACC_NUMBER")==null?"-":rs.getString("BANK_ACC_NUMBER");
						String ifsc_code=rs.getString("IFSC_CODE")==null?"-":rs.getString("IFSC_CODE");
						String GSTIN=rs.getString("GSTIN")==null?"-":rs.getString("GSTIN");
						String Bank_Name=rs.getString("BANK_NAME")==null?"-":rs.getString("BANK_NAME");
						String tds_percentage=rs.getString("TDS_PERCENTAGE")==null?"-":rs.getString("TDS_PERCENTAGE");
						String vendorData = contractor_id + "@@" + contractor_name + "@@" + contractor_address + 
								"@@"+ mobileNo + "@@" + panNo+ "@@" +acc_no+ "@@" +ifsc_code+ "@@" +GSTIN+"@@"+Bank_Name+"@@"+tds_percentage;
						
					
						vendorList.add(vendorData);
					}
					return vendorList;
				}
			});
		}

		return dbContractorList;
	}

	/**
	 * @author Aniket Chavan
	 * @description this method is used for loading the work area of related to work description
	 * @param workDescId is holding the work description  id, so we can select the work description area
	 * @param unitsOfMeasurementId is the selecting which type of measurement we have to select with the combination of work description
	 * @param siteId selecting the work area using site id
	 * @return list implemented class
	 */
	@Override
	public List<Map<String, Object>> loadWOAreaMapping(String siteId, String workDeskId, String unitsOfMeasurementId, String typeOfWork) {
		StringBuffer loadWoAreaMapping =new StringBuffer().append("select (QWAM.WO_WORK_AREA_ID),QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,QWAM.QS_BOQ_DETAILS_ID,QWAM.QS_AREA_PRICE_PER_UNIT,QWAM.QS_AREA_AMOUNT,QB.BOQ_NO,QWAM.WO_WORK_INITIATE_AREA,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME ")
				.append(" ,B.NAME as BLOCK_NAME,F.NAME AS FLOOR_NAME,FLAT.name as FLATNAME, " )
				.append(" (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,")
				.append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,")
				.append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID,")
				.append(" SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL,")
				.append(" (select LISTAGG(RECORD_TYPE, '@@')  WITHIN GROUP (ORDER BY WO_WORK_DESC_ID)  from QS_BOQ_AREA_MAPPING QBAM1 where QBAM1.SITE_ID= QWAM.SITE_ID AND QBAM1.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID   AND QBAM1.WO_WORK_AREA_GROUP_ID=QWAM.WO_WORK_AREA_GROUP_ID) as WO_CONTAINS")
				.append(" from QS_BOQ_AREA_MAPPING QWAM left outer join BLOCK B on B.BLOCK_ID = QWAM.BLOCK_ID")
				.append(" left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID ")
				.append(" left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID ,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,SITE SI,QS_BOQ QB ")
				.append(" where QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID  AND SI.SITE_ID=QWAM.SITE_ID AND QB.BOQ_SEQ_NO=QWAM.BOQ_SEQ_NO ");
				if(typeOfWork.equals("MATERIAL")){
					loadWoAreaMapping.append(" AND QWAM.RECORD_TYPE='MATERIAL'");
				}
				loadWoAreaMapping.append(" AND QWAM.site_id=? and QWAM.status='A' and QB.status='A' and  QWW.WO_WORK_DESC_ID=? ")// and QWAM.WO_MEASURMENT_ID='"+unitsOfMeasurementId+"' 
				.append(" order by (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end), ")
				.append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),")
			    .append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end), QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE");
		
		List<Map<String, Object>> areaMappingList = jdbcTemplate.queryForList(loadWoAreaMapping.toString(),siteId,workDeskId);
		return areaMappingList;
	}
	
	/**
	 * @author Aniket Chavan
	 * @description this method is used for loading the work area of related to work description while revise work order
	 * @param workOrderBean object holding the data which are going to use for operation
	 * @return list implemented class
	 */
	@Override
	public List<Map<String, Object>> loadWOAreaMappingForRevise(WorkOrderBean bean) {
		String typeOfWork=bean.getTypeOfWork();
		StringBuffer loadWoAreaMapping =new StringBuffer().append("select (QWAM.WO_WORK_AREA_ID),QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,QWAM.QS_BOQ_DETAILS_ID,QWAM.QS_AREA_PRICE_PER_UNIT,QWAM.QS_AREA_AMOUNT,QB.BOQ_NO,QWAM.WO_WORK_INITIATE_AREA,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME ")
				.append(" ,B.NAME as BLOCK_NAME,F.NAME AS FLOOR_NAME,FLAT.name as FLATNAME, ")
				.append(" (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,")
				.append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,")
				.append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID")
				.append(" ,SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL")
				.append(" ,(select LISTAGG(RECORD_TYPE, '@@')  WITHIN GROUP (ORDER BY WO_WORK_DESC_ID)  from QS_BOQ_AREA_MAPPING QBAM1 where QBAM1.SITE_ID= QWAM.SITE_ID AND QBAM1.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID   AND QBAM1.WO_WORK_AREA_GROUP_ID=QWAM.WO_WORK_AREA_GROUP_ID) as WO_CONTAINS")
				.append(" from QS_BOQ_AREA_MAPPING QWAM left outer join BLOCK B on B.BLOCK_ID = QWAM.BLOCK_ID")
				.append(" left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID ")
				.append(" left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID ,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,SITE SI,QS_BOQ QB ")
				.append(" where QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID  AND SI.SITE_ID=QWAM.SITE_ID AND QB.BOQ_SEQ_NO=QWAM.BOQ_SEQ_NO ")
				.append(" AND QWAM.site_id=? and QB.BOQ_NO=? AND  QWW.WO_WORK_DESC_ID=? ");//and QWAM.WO_MEASURMENT_ID=?
				if(typeOfWork.equals("MATERIAL")){
					loadWoAreaMapping.append(" AND QWAM.RECORD_TYPE='"+typeOfWork+"'");
				}
				loadWoAreaMapping.append(" order by  QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,(case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end), ")
				.append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),")
				.append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end) "); /*QB.status='A'  and QWAM.status='A' */
		
		Object[] params={ bean.getSiteId(),bean.getBoqNo(),bean.getWO_Desc1() };
		List<Map<String, Object>> areaMappingList = jdbcTemplate.queryForList(loadWoAreaMapping.toString(),params);
		return areaMappingList;
	}

	/**
	 * @author Aniket Chavan
	 * @description this method is used for loading the work order issue id
	 * @return int type holding the sequence number
	 */
	
	@Override
	public int getQS_WO_Temp_Issue_Dtls() {
		int workorderDetailsSeqNum = jdbcTemplate.queryForInt("SELECT QS_WO_TEMP_ISSUE_SEQ.NEXTVAL FROM DUAL");
		return workorderDetailsSeqNum;
	}

	/**
	 * @author Aniket Chavan
	 * @description this method is used for loading changed details sequence number
	 * @return int type holding the sequence number
	 */
	@Override
	public int getChnagedWorkOrderSeqNumber() {
		String seq = "select QS_WORK_ORDER_CHANGED_DTLS_SEQ.NEXTVAL from daul";
		int chnagedWorkOrderSeqNumber = jdbcTemplate.queryForInt(seq);
		return chnagedWorkOrderSeqNumber;
	}

	/**
	 * @author Aniket Chavan
	 * @description this method is used for loading next approval of work order nor NMR work order
	 * @param user_id used to select next level approval employee id
	 * @param workOrderBean object holding the data which are going to use for operation
	 * @return WorkOrderBean holding the next level approval name and employee id
	 */
	@Override
	public WorkOrderBean getWorkOrderFromAndToDetails(String user_id, WorkOrderBean workOrderBean) {
 		List<Map<String, Object>> dbIndentDts = null;
		String moduleType="";
		if(workOrderBean.getTypeOfWork()!=null){
			if(workOrderBean.getTypeOfWork().equals("MATERIAL")){
				moduleType="WOM";
			}else if(workOrderBean.getTypeOfWork().equals("CONSULTANT")){
				moduleType="CONSULTANT";
			}else{
				moduleType="WOC";
			}
		}else{
			moduleType="WOC";
		}
	//	String query = "SELECT SAMD.APPROVER_EMP_ID, SAMD.SITE_ID FROM  SUMADHURA_APPROVER_MAPPING_DTL  SAMD WHERE SAMD.EMP_ID = ? AND  SAMD.SITE_ID=? AND SAMD.MODULE_TYPE=? AND SAMD.STATUS='A' ";
		StringBuffer query = new StringBuffer(" SELECT ((SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID = :user_id)) AS EMP_NAME")
				.append(",SED.EMP_NAME AS APPROVER_NAME,SED.EMP_EMAIL,SED.EMP_ID AS APPROVER_ID, SAMD.SITE_ID ")
				.append(" FROM SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_APPROVER_MAPPING_DTL  SAMD")
				.append(" WHERE SED.EMP_ID = SAMD.APPROVER_EMP_ID AND SAMD.EMP_ID = :user_id1 AND  SAMD.SITE_ID = :site_id AND SAMD.MODULE_TYPE = :module_type");
		

		Map<String, String> parameters = new HashMap<String, String>();
		parameters.put("user_id", user_id);
		parameters.put("user_id1", user_id);
		parameters.put("site_id",workOrderBean.getSiteId() );
		parameters.put("module_type",moduleType);
		
		//Object[] obj={ user_id, user_id,workOrderBean.getSiteId() ,moduleType};
		dbIndentDts = namedParameterJdbcTemplate.queryForList(query.toString(),parameters);
		for (Map<String, Object> prods : dbIndentDts) {
			log.info("Mapping Details in Work Order :: "+prods);
			System.out.println("Mapping Details :: "+prods);
			workOrderBean.setApproverEmpId(prods.get("APPROVER_ID") == null ? "" : prods.get("APPROVER_ID").toString());
			workOrderBean.setWorkorderFrom(prods.get("EMP_NAME") == null ? "" : prods.get("EMP_NAME").toString());
			workOrderBean.setWorkorderTo(prods.get("APPROVER_NAME") == null ? "" : prods.get("APPROVER_NAME").toString());
			workOrderBean.setApproverEmpMail(prods.get("EMP_EMAIL") == null ? "" : prods.get("EMP_EMAIL").toString());
		}
		return workOrderBean;
	}
	//loading type of work order can create user
	@Override
	public List<Map<String, Object>> userAbleToCreateWOTypes(String user_id, WorkOrderBean workOrderBean) {
		List<Map<String, Object>> dbWOTypes = null;
		String query = "SELECT SAMD.MODULE_TYPE FROM  SUMADHURA_APPROVER_MAPPING_DTL SAMD where SAMD.SITE_ID=? AND SAMD.EMP_ID = ? and  SAMD.MODULE_TYPE in ('WOC','WOM','CONSULTANT') ";
		Object[] obj={ workOrderBean.getSiteId(),user_id};
		dbWOTypes = jdbcTemplate.queryForList(query,obj);
		return dbWOTypes;
	}
	
	/**
	 * @author Aniket Chavan
	 * @description this method is used for revise work order time to generate next work order number
	 * @param workOrderBean object holding the data which are going to use for operation
	 * @return listOfRevisedWorkOrder is holding the work order number
	 */
	
	@Override
	public List<Map<String, Object>> getNewRevisedWorkOrderName(String user_id, WorkOrderBean bean) {
		String workOrderDaNo=bean.getWorkOrderNo();
		String query="SELECT WORK_ORDER_NUMBER FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER = ? AND SITE_ID=?  AND STATUS NOT IN ('R','I') ";// 
		List<Map<String, Object>> listOfRevisedWorkOrder=jdbcTemplate.queryForList(query,workOrderDaNo,bean.getSiteId());
		return listOfRevisedWorkOrder;
	}
	
	@Override
	public boolean checkIsAnyBillisInActiveMode(WorkOrderBean bean) {
		String query="SELECT COUNT(*) FROM QS_CONTRACTOR_BILL_TEMP WHERE STATUS='A' AND  QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER = '"+bean.getWorkOrderNo()+"'  AND SITE_ID='"+bean.getSiteId()+"') ";
		int count=jdbcTemplate.queryForObject(query, Integer.class);//PAYMENT_TYPE='"+billBean.getPaymentType()+"' and 
		if(count!=0){
			return true;
		}
		return false;
	}
	
	/**
	 * @author Aniket Chavan
	 * @description this method is for generating temp work order number by site wise
	 * @param siteId used for query operation to load temp work order number site wise
	 * @return listOfRevisedWorkOrder is holding the work order number
	 */
	
	@Override
	public int getMaxQSWorkOrderNo(String siteId) {
		String query = "SELECT MAX(TEMP_WORK_ORDER_NUMBER)+1 AS MAX FROM QS_WORKORDER_TEMP_ISSUE WHERE SITE_ID = ?";
		Integer result = jdbcTemplate.queryForObject(query, Integer.class, new Object[] { siteId });
		if (result == null) {
			result = 1;
		}
		return result;
	}
	
	
	/**
	 * @author Aniket Chavan
	 * @description this method is for generating permanent work order number by site wise
	 * @param siteId used for query operation to load  work order number site wise
	 * @param siteName used for concatenating siteName with the work order number
	 * @return finalWOnumber is holding the permanent work order number
	 */
	
	@Override
	public String getWorkOrderNoSiteWise(String siteId, String siteName, String woTypeOfWork) {
		StringBuffer query=null;
		String woNumberFromPermanentWOtable;
		String finalWOnumber=null;
		
		//here checking is any work order number is exist in RESERVED_WO_NUMBER work order number table or not 
		//if exits then select that work order number no need to generate permanent work order manually. 
		query= new StringBuffer("SELECT RESERVED_WO_NUMBER FROM QS_RESERVED_WO_NUMBER WHERE STATUS='I' AND SITE_ID=? AND ROWNUM=1");
		if(woTypeOfWork.equals("MATERIAL")){
			query.append(" AND TYPE_OF_WORK='MATERIAL'");
		}else{
			query.append(" AND TYPE_OF_WORK!='MATERIAL'");
		}
		try {
			String reservedWOnumber=jdbcTemplate.queryForObject(query.toString(),new Object[]{siteId},String.class);
			if(reservedWOnumber!=null&&!reservedWOnumber.contains("/R")){
				finalWOnumber=reservedWOnumber;
			}
		} catch (Exception e) {
			//e.g. WO/SIPL/DEV/12 so have add 1 then next number will generate WO/SIPL/DEV/13
			//in query we are splitting the work order number and selecting max work order till date generated
			query= new StringBuffer("");
			if(woTypeOfWork.equals("MATERIAL")){
				query.append("SELECT  MAX(NVL(to_number(REGEXP_SUBSTR (PER_WORK_ORDER_NUMBER, '[^/]+', 1, 5)),0))+1   AS wokrOrderNo FROM    QS_WORKORDER_TEMP_ISSUE  where SITE_ID =? AND TYPE_OF_WORK='MATERIAL' ");
			}else{
				query.append("SELECT  MAX(NVL(to_number(REGEXP_SUBSTR (PER_WORK_ORDER_NUMBER, '[^/]+', 1, 4)),0))+1   AS wokrOrderNo FROM    QS_WORKORDER_TEMP_ISSUE  where SITE_ID =? AND TYPE_OF_WORK!='MATERIAL' ");
			}
			query.append(" ORDER BY WOKRORDERNO");
			finalWOnumber=jdbcTemplate.queryForObject(query.toString(),new Object[]{siteId},String.class);
			//loading the max work order number from work order permanent table
			query= new StringBuffer("");
			if(woTypeOfWork.equals("MATERIAL")){
				query.append("SELECT  MAX(NVL(to_number(REGEXP_SUBSTR (WORK_ORDER_NUMBER, '[^/]+', 1, 5)),0))+1   AS wokrOrderNo FROM    QS_WORKORDER_ISSUE  where SITE_ID =? AND TYPE_OF_WORK='MATERIAL' ");
			}else{
				query.append("SELECT  MAX(NVL(to_number(REGEXP_SUBSTR (WORK_ORDER_NUMBER, '[^/]+', 1, 4)),0))+1   AS wokrOrderNo FROM    QS_WORKORDER_ISSUE  where SITE_ID =? AND TYPE_OF_WORK!='MATERIAL' ");
			}
			query.append(" order by wokrOrderNo");
			woNumberFromPermanentWOtable=jdbcTemplate.queryForObject(query.toString(),new Object[]{siteId},String.class);
			if(finalWOnumber!=null&&woNumberFromPermanentWOtable!=null){
				if(Integer.valueOf(finalWOnumber)>Integer.valueOf(woNumberFromPermanentWOtable)){
					//finalWOnumber=finalWOnumber;
				}else{
					finalWOnumber=woNumberFromPermanentWOtable;
				}
			}
			if(finalWOnumber==null&&woNumberFromPermanentWOtable==null){
				finalWOnumber="1";
			}else if(finalWOnumber==null&&woNumberFromPermanentWOtable!=null){
				finalWOnumber=woNumberFromPermanentWOtable;
			}
			if(woTypeOfWork.equals("MATERIAL")){
				finalWOnumber = "WO/SIPL/"+siteName+"/MAT/"+finalWOnumber;
			}else{
				finalWOnumber = "WO/SIPL/"+siteName+"/"+finalWOnumber;
			}
		}
		return finalWOnumber;
	}
	
	/**
	 * @author Aniket Chavan
	 * @description this method is for generating permanent work order number by site wise
	 * @param user_id used for selecting the next approver employee id
	 * @return strApproverEmpId is holding the next approver employee id
	 */
/*	@Override
	public String getWorkOrderPendingEmployeeId(String user_id) {
		String strApproverEmpId = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT APPROVER_EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? AND MODULE_TYPE='WOP'";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] { user_id });

		for (Map<String, Object> prods : dbIndentDts) {
			strApproverEmpId = prods.get("APPROVER_EMP_ID") == null ? "" : prods.get("APPROVER_EMP_ID").toString();
		}
		return strApproverEmpId;
	}
*/
	/**
	 * @author Aniket Chavan
	 * @description this method is for inserting the revise work order detail
	 * @param workOrderBean object is holding all the data which are going to use in while inserting data
	 * @param workOrderCreateEmpId holding the value of the user_id who is creating this work order number.  
	 * @return list implemented class which is holding the sequence number of generated work order number
	 */
	@Override
	public List<Number> insertReviseWorkOrderDetail(WorkOrderBean workOrderBean, String workOrderCreateEmpId) {
		List<Number> responseList = new ArrayList<Number>();
		//KeyHolder holder = new GeneratedKeyHolder();
		//String tempWoNum= workOrderBean.getSiteWiseWONO();
		String oldWorkOrderWhileRevise="";
		String oldWorkOrderIssueId="";
		String workOrderStatus="";String saveOrUpdateOperation="";
		int result=0;
		int tempWorkOrderNo=getMaxQSWorkOrderNo(workOrderBean.getSiteId());
		workOrderBean.setTypeOfWork(workOrderBean.getTypeOfWork().split("@@")[0]);
		//workOrderBean.setWorkOrderNo(workOrderBean.getRevisedWorkOrderNo());
		if(workOrderBean.getIsSaveOrUpdateOperation().equals("Draft Work Order")){
			workOrderStatus="DF";
			saveOrUpdateOperation="Draft Work Order";
		}else{
			workOrderStatus="A";
		}
		//if this is Modify Work Order Operation then we are taking data approve work order page we have used approve work order page for Modify Work Order
		if(workOrderBean.getWorkOrderStatus()!=null&&workOrderBean.getWorkOrderStatus().equals("ModifyWorkOrder")){
			workOrderBean.setSiteWiseWONO(workOrderBean.getOldWorkOrderNo());	
		}else{
			workOrderBean.setOldWorkOrderNo(workOrderBean.getSiteWiseWONO());
		}
		if(workOrderBean.getTypeOfWork()!=null){
			if(workOrderBean.getTypeOfWork().equals("NMR")){
				workOrderBean.setLaborWoAmount(workOrderBean.getTotalAmount1());
			}else if(workOrderBean.getTypeOfWork().equals("MATERIAL")){
				workOrderBean.setTotalAmount1(workOrderBean.getMaterialWoAmount());
				workOrderBean.setTotalWoAmount(workOrderBean.getMaterialWoAmount());
			}
		}

		//taking next version no
		String queryForNextVersionNO="select nvl(VERSION_NUMBER,0)+1 from QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+workOrderBean.getSiteWiseWONO()+"' And SITE_ID='"+workOrderBean.getSiteId()+"'";
		Integer qsWOTempIssueSeq=jdbcTemplate.queryForObject("select QS_WO_TEMP_ISSUE_SEQ.NEXTVAL from dual", Integer.class);
		double oldVersionNo=jdbcTemplate.queryForObject(queryForNextVersionNO,Double.class);
		double newVersionNo=oldVersionNo;
		workOrderBean.setVersionNumber(String.valueOf(newVersionNo));
		//workOrderBean.getSiteWiseWONO();
		String[] strWONoPart=workOrderBean.getWorkOrderNo().split("/");
		String revision=strWONoPart[strWONoPart.length-1].split("R")[1];
		workOrderBean.setRevision(revision);	
	
	// Deactivating old Work Order No
		String doInactivePrevWO="UPDATE QS_WORKORDER_ISSUE set STATUS='I' where WORK_ORDER_NUMBER='"+workOrderBean.getSiteWiseWONO()+"' And SITE_ID='"+workOrderBean.getSiteId()+"'";
		result =jdbcTemplate.update(doInactivePrevWO);
	
		final StringBuffer query =new StringBuffer("insert into QS_WORKORDER_TEMP_ISSUE(PER_WORK_ORDER_NUMBER,WORK_ORDER_NAME,TOTAL_WO_AMOUNT,STATUS,QS_WO_TEMP_ISSUE_ID,CONTRACTOR_ID,CREATED_BY,CREATED_DATE,TEMP_WORK_ORDER_NUMBER,PENDING_EMP_ID,SITE_ID,WORK_ORDER_DATE,BOQ_NO,TYPE_OF_WORK,VERSION_NUMBER ,REVISION,OLD_WORK_ORDER_NUMBER,WO_RECORD_CONTAINS,TOTAL_LABOUR_AMOUNT,TOTAL_MATERIAL_AMOUNT)")//
					.append(" values(?,?,?,?,?,?, ?,sysdate,?,?,?,?,?,?,?,?,?,?,?,?)");
		
		Object queryParams[]={workOrderBean.getWorkOrderNo(),workOrderBean.getWorkOrderName(),workOrderBean.getTotalAmount1() ,workOrderStatus ,qsWOTempIssueSeq, workOrderBean.getContractorId() ,
				workOrderCreateEmpId , tempWorkOrderNo,
				(workOrderBean.getIsSaveOrUpdateOperation().equals("Draft Work Order")?workOrderCreateEmpId:workOrderBean.getApproverEmpId()) ,
				workOrderBean.getSiteId() ,
				workOrderBean.getWorkOrderDate() ,workOrderBean.getBoqNo(),workOrderBean.getTypeOfWork(),workOrderBean.getVersionNumber() ,workOrderBean.getRevision(),workOrderBean.getOldWorkOrderNo(),workOrderBean.getBoqRecordType(),workOrderBean.getLaborWoAmount(),workOrderBean.getMaterialWoAmount()};
		log.info(query);
	    result=jdbcTemplate.update("update QS_RESERVED_WO_NUMBER set status='A' where RESERVED_WO_NUMBER='"+workOrderBean.getWorkOrderNo()+"'");
	    result = jdbcTemplate.update("UPDATE QS_WORKORDER_TEMP_ISSUE SET STATUS='MI' WHERE PER_WORK_ORDER_NUMBER=? AND SITE_ID=? AND  STATUS='M' ",workOrderBean.getWorkOrderNo(),workOrderBean.getSiteId());
	    result = jdbcTemplate.update(query.toString(),queryParams);

		responseList.add(result);
		responseList.add(qsWOTempIssueSeq);
 
		workOrderBean.setSiteWiseWONO(String.valueOf(tempWorkOrderNo));
		return responseList;
	}//insertReviseWorkOrderDetail() method completed
	
	/**
	 * @author Aniket Chavan
	 * @description this method is for inserting the new work order detail it may NMR or PIECE WORK
	 * @param bean object is holding all the data which are going to use in while inserting data
	 * @param workOrderCreateEmpId holding the value of the user_id who is creating this work order number.  
	 * @return list implemented class which is holding the sequence number of generated work order number
	 */
	@Override
	public List<Number> insertWorkOrderDetail(WorkOrderBean bean, String workOrderCreateEmpId) {
		List<Number> responseList = new ArrayList<Number>();
		int result=0;
		Integer qsWOTempIssueSeq=null;
		String WorkOrderNumber="";String workOrderStatus="";String saveOrUpdateOperation="";
		int tempWorkOrderNo=getMaxQSWorkOrderNo(bean.getSiteId());
		String isWorkOrderNumberExists="";
		
/*		bean.getVersionNumber();
		bean.getRevision();	*/
		
		if(bean.getIsSaveOrUpdateOperation().equals("Draft Work Order")){
			workOrderStatus="DF";
			saveOrUpdateOperation="Draft Work Order";
		}else{
			workOrderStatus="A";
		}
		if(bean.getTypeOfWork()!=null){
			if(bean.getTypeOfWork().equals("NMR")){
				bean.setLaborWoAmount(bean.getTotalAmount1());
			}else if(bean.getTypeOfWork().equals("MATERIAL")){
				bean.setTotalAmount1(bean.getMaterialWoAmount());
			}
		}
		
		if(!bean.getTypeOfWork().contains("@@")){
			if(bean.getActualWorkOrderNo().equals(bean.getWorkOrderNo())){
				//checking here is work order number exists or not if exists then generate new work order number
					isWorkOrderNumberExists=checkWorkOrderNoExistsOrNot(bean.getWorkOrderNo(),bean.getSiteId());
				 	if(isWorkOrderNumberExists.equals("true"))
				 		WorkOrderNumber=workControllerService.getWorkOrderNoSiteWise(bean.getSiteId(),bean.getSiteName(),bean.getTypeOfWork());//bean.getTypeOfWork()
				 	
					bean.setWorkOrderNo(WorkOrderNumber.length()==0?bean.getWorkOrderNo():WorkOrderNumber);
					bean.setSiteWiseWONO(String.valueOf(tempWorkOrderNo));
			}else{
				//this else condition is for Old work order number, if user enter's old work order number if the old work order number is exists in record,
				//then we have to throw the message to user that work order number already exists
				isWorkOrderNumberExists=checkWorkOrderNoExistsOrNot(bean.getWorkOrderNo(),bean.getSiteId());
				if(isWorkOrderNumberExists.equals("true")){
					throw new RuntimeException("Work Order Number already exists");
				}
			}
			//giving default version number bcoz creating first time work order
			bean.setVersionNumber("1.0");
			bean.setRevision("0");	
		}else if(bean.getTypeOfWork().contains("@@")){}
	
		 qsWOTempIssueSeq=jdbcTemplate.queryForObject("SELECT QS_WO_TEMP_ISSUE_SEQ.NEXTVAL FROM DUAL", Integer.class);
		 StringBuffer query =new StringBuffer("INSERT INTO QS_WORKORDER_TEMP_ISSUE(PER_WORK_ORDER_NUMBER,WORK_ORDER_NAME,TOTAL_WO_AMOUNT,STATUS,QS_WO_TEMP_ISSUE_ID,CONTRACTOR_ID,CREATED_BY,CREATED_DATE,TEMP_WORK_ORDER_NUMBER,PENDING_EMP_ID,SITE_ID,WORK_ORDER_DATE,BOQ_NO,TYPE_OF_WORK,VERSION_NUMBER ,REVISION,WO_RECORD_CONTAINS,TOTAL_LABOUR_AMOUNT,TOTAL_MATERIAL_AMOUNT,OLD_WORK_ORDER_NUMBER)")//,TOTAL_LABOUR_AMOUNT,TOTAL_MATERIAL_AMOUNT
				 		.append(" VALUES(?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?,?,?,?,?,?,?)");
	
		 Object queryParams[]={bean.getWorkOrderNo(),bean.getWorkOrderName(),bean.getTotalAmount1(),workOrderStatus  ,qsWOTempIssueSeq, bean.getContractorId() ,
				workOrderCreateEmpId , tempWorkOrderNo
				,(saveOrUpdateOperation.equals("Draft Work Order")?workOrderCreateEmpId:bean.getApproverEmpId())
				,bean.getSiteId(),
				bean.getWorkOrderDate() ,bean.getBoqNo(),bean.getTypeOfWork(),bean.getVersionNumber() ,bean.getRevision(),bean.getBoqRecordType(),bean.getLaborWoAmount(),bean.getMaterialWoAmount(),bean.getSiteWiseWONO()};

		result = jdbcTemplate.update("UPDATE QS_RESERVED_WO_NUMBER SET STATUS='A' WHERE RESERVED_WO_NUMBER=? ",bean.getWorkOrderNo());
		//MI= Modified inactive, making old temp work order to inactive if exists
		result = jdbcTemplate.update("UPDATE QS_WORKORDER_TEMP_ISSUE SET STATUS='MI' WHERE PER_WORK_ORDER_NUMBER=? AND SITE_ID=? AND  STATUS='M' ",bean.getWorkOrderNo(),bean.getSiteId());
	    
		result = jdbcTemplate.update(query.toString(),queryParams);
	    
		responseList.add(result);
		responseList.add(qsWOTempIssueSeq);
		bean.setSiteWiseWONO(String.valueOf(tempWorkOrderNo));
	return responseList;
}
	
	/**
	 * @author Aniket Chavan
	 * @description this method is for inserting approval details of the work order like who is approved who created and who rejectd
	 * @param workOrderBean object is holding all the data which are going to use in while inserting data
	 * @param operType holding the value of the operation type means it is create(C) or approve(A) or reject(R) the are denoting by the (C,A,R)  
	 * @return int will hold the result of method
	 */
	@Override
	public int inserWorkOrderCreationDetail(WorkOrderBean workOrderBean,String operType) {
		  String query = " INSERT INTO QS_WORKORDER_CRT_APPRL_DTLS(TEMP_WORK_ORDER_NUMBER,QS_WO_CRT_APPRL_DTLS_ID,CREATION_DATE,SITE_ID,WO_CREATE_APPROVE_EMP_ID,PURPOSE,OPERATION_TYPE) "
				     + " VALUES(?,QS_WO_CRT_APPRL_DTLS_SEQ.NEXTVAL,SYSDATE,?,?,?,?) ";
		  Object[] params={ workOrderBean.getSiteWiseWONO(),workOrderBean.getSiteId(), workOrderBean.getUserId().trim(), workOrderBean.getPurpose(), operType };
		  int result = jdbcTemplate.update(query, params);
	  return result;
	}
	
	@Override
	public int changeWorkOrderApprRejDetail(WorkOrderBean workOrderBean, String actionType) {
		 
		return 0;
	}
	
	/**
	 * @description this method will give the work order to first level employee or any one who will modify the work order
	 * after modification of the work order it will go to next level employee for approval
	 */
	@Override
	public int sendToModifyWorkOrder(WorkOrderBean bean) {
		StringBuffer query=new  StringBuffer("UPDATE QS_WORKORDER_TEMP_ISSUE set STATUS = 'M',")
				.append(" PENDING_EMP_ID=(SELECT WO_CREATE_APPROVE_EMP_ID FROM QS_WORKORDER_CRT_APPRL_DTLS WHERE TEMP_WORK_ORDER_NUMBER=? AND SITE_ID=? AND OPERATION_TYPE='C')")
				.append(" WHERE TEMP_WORK_ORDER_NUMBER = ? AND  SITE_ID=? ");
		Object[] params={bean.getSiteWiseWONO(),bean.getSiteId(),bean.getSiteWiseWONO(),bean.getSiteId()};
		int result = jdbcTemplate.update(query.toString(),params);	
		return result;
	}

	/**
	 *  @author Aniket Chavan
	 *  @description this method is for inserting data in QS_WORKORDER_TEMP_ISSUE_DTLS which is temporary details
	 *  @param workOrderBean object is holding all the data which are going to use in while inserting data
	 *  @param operType holding the value of the operation type means it is create(C) or approve(A) or reject(R) the are denoting by the (C,A,R)  
	 *  @return List will hold the result of method and sequence number of 
	 */
	@Override
	public List<Integer> insertWorkOrderTempIssueDetails(WorkOrderBean bean, String workOrderGenratedKey) {
		List<Integer> list = new ArrayList<Integer>();
		//loading the sequence number of QS_WORKORDER_TEMP_ISSUE_DTLS primary key
		String queryForSeq = "SELECT QS_WO_TEMP_ISSUE_DTLS_SEQ.NEXTVAL FROM DUAL";
		int seqNum = jdbcTemplate.queryForObject(queryForSeq, Integer.class);
		StringBuffer query = new StringBuffer("INSERT INTO QS_WORKORDER_TEMP_ISSUE_DTLS(WO_WORK_TEMP_ISSUE_DTLS_ID,WO_WORK_DESC_ID,TOTAL_AMOUNT,CREATED_DATE ,")
				.append(" STATUS,QS_WO_TEMP_ISSUE_ID,WO_COMMENT,WO_MANUAL_DESC,UOM,QUANTITY,WO_MINORHEAD_ID,WO_MAJORHEAD_ID,WO_ROW_CODE,RECORD_TYPE)")
				.append(" VALUES(?,?,?,SYSDATE,'A',?,?,?,?,?,?,?,?,?)");
		Object[] obj = { seqNum, bean.getWO_Desc1(),  bean.getTotalAmount1(),workOrderGenratedKey, bean.getComments1(), bean.getwOManualDescription(),
				bean.getUnitsOfMeasurement1(),bean.getQuantity1(),bean.getWO_MinorHead1(),bean.getWO_MajorHead1(),bean.getWoRowCode(),bean.getWoRecordContains()};
		int result = jdbcTemplate.update(query.toString(), obj);
		list.add(seqNum);
		list.add(result);
		return list;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method is for inserting data in QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS table which is temporary details
	 *  @param bean object is holding all the data which are going to use in while inserting data
	 *  @param tempIssueDetailsIdPK is the primary key of the   QS_WORKORDER_TEMP_ISSUE_DTLS table
	 *  @return List will hold the result of method and sequence number of 
	 */
	@Override
	public List<Integer> WorkOrderTempIssueAreaDetails(WorkOrderBean bean, String tempIssueDetailsIdPK) {
		List<Integer> list = new ArrayList<Integer>();
		String queryForSeq = "select QS_WO_TEMP_ISSUE_AREA_DTLS_SEQ.NEXTVAL from dual";
		int SeqNum = jdbcTemplate.queryForObject(queryForSeq, Integer.class);
		StringBuffer query=new StringBuffer("insert into QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS(WO_WORK_TEMP_ISSUE_AREA_DTLS_ID,WO_WORK_AREA_ID,")
				.append(" AREA_ALOCATED_QTY,PRICE,BASIC_AMOUNT,TOTAL_AMOUNT,CREATED_DATE,STATUS,WO_WORK_TEMP_ISSUE_DTLS_ID,OLD_WO_WORK_ISSUE_AREA_DTLS_ID,BOQ_RATE,RECORD_TYPE)")
				.append(" values(?,?,?,?,?,?,sysdate,'A',?,?,?,?)");
		double amount=Double.valueOf(bean.getSelectedArea())*Double.valueOf(bean.getAcceptedRate1());
		Object[] obj={SeqNum,bean.getWorkAreaId(),bean.getSelectedArea(),bean.getAcceptedRate1(),amount
				,amount,tempIssueDetailsIdPK,bean.getOldQSWorkOrderAreaDTLSId(),bean.getBoqRate(),bean.getBoqRecordType()};
		int result = jdbcTemplate.update(query.toString(), obj);
		list.add(SeqNum);
		list.add(result);
		return list;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method is for inserting data in QS_WORKORDER_ISSUE_AREA_DETAILS table which is temporary details
	 *  @param bean object is holding all the data which are going to use in while inserting data
	 *  @param tempIssueDetailsIdPK is the primary key of the   QS_WORKORDER_ISSUE_DTLS table
	 *  @return List will hold the result of method and sequence number of 
	 */

	@Override
	public List<Integer> insertPermanentQSTEMPDetails(WorkOrderBean bean, String tempIssueDetailsIdPK) {
		List<Integer> list = new ArrayList<Integer>();
		String queryForSeq = "select QS_WO_ISSUE_AREA_DTLS_SEQ.NEXTVAL from dual";
		int seqNum = jdbcTemplate.queryForObject(queryForSeq, Integer.class);
/*		double basicAmt = Double.valueOf(bean.getTotalAmount1());
		double price = Double.valueOf(bean.getAcceptedRate1());*/
		StringBuffer query=new StringBuffer("insert into QS_WORKORDER_ISSUE_AREA_DETAILS(WO_WORK_ISSUE_AREA_DTLS_ID,WO_WORK_AREA_ID,")
				.append(" AREA_ALOCATED_QTY,PRICE,BASIC_AMOUNT,TOTAL_AMOUNT,CREATED_DATE,STATUS,WO_WORK_ISSUE_DTLS_ID)")
				.append(" values(?,?,?,?,?,?,sysdate,'A',?)");
	
		Object[] obj={seqNum,bean.getWorkAreaId(),bean.getSelectedArea(),bean.getAcceptedRate1(),bean.getTotalAmount1()
				,bean.getTotalAmount1(),tempIssueDetailsIdPK};
		int result = jdbcTemplate.update(query.toString(), obj);
		list.add(seqNum);
		list.add(result);
	return list;
	}

	/**
	 *  @author Aniket Chavan
	 *  @description this method is for inserting data in QS_WORKORDER_ISSUE_AREA_DETAILS table which is temporary details
	 *  @param bean object is holding all the data which are going to use in while inserting data
	 *  @param tempIssueDetailsIdPK is the primary key of the   QS_WORKORDER_ISSUE_DTLS table
	 *  @return List will hold the result of method and sequence number of 
	 */

	@Override
	public void updateWorkAreaMapping(WorkOrderBean bean, String nextApprovelEmpId123, String opeType) {
		String query = "";//"update QS_BOQ_AREA_MAPPING set WO_PERCENTAGE_AVAIL=WO_PERCENTAGE_AVAIL-?, WO_WORK_AVAILABE_AREA=WO_WORK_AVAILABE_AREA-?, WO_WORK_INITIATE_AREA=? where WO_WORK_AREA_ID=?";
		int result=0;
		//if operation type is empty means decrease the work area
		if( opeType.equals("")){
			//updating new work area
			query="UPDATE QS_BOQ_AREA_MAPPING SET WO_PERCENTAGE_AVAIL=(WO_WORK_INITIATE_AREA/WO_WORK_AREA)*100, WO_WORK_AVAILABE_AREA=WO_WORK_AVAILABE_AREA-?,WO_WORK_INITIATE_AREA=WO_WORK_INITIATE_AREA+? WHERE WO_WORK_AREA_ID=?";
			Object[] params = { bean.getSelectedArea(), bean.getSelectedArea(),bean.getWorkAreaId() };
			result = jdbcTemplate.update(query, params);
		}else if ( opeType.equals("deleted")){
			//reallocating the work area and updating WO_WORK_INITIATE_AREA and WO_WORK_AVAILABE_AREA
			query="UPDATE QS_BOQ_AREA_MAPPING SET WO_PERCENTAGE_AVAIL=(WO_WORK_INITIATE_AREA/WO_WORK_AREA)*100, WO_WORK_AVAILABE_AREA=WO_WORK_AVAILABE_AREA+?,WO_WORK_INITIATE_AREA=WO_WORK_INITIATE_AREA-? WHERE WO_WORK_AREA_ID=?";
			Object[] params = { bean.getSelectedArea(), bean.getSelectedArea(),bean.getWorkAreaId() };
			 result = jdbcTemplate.update(query, params);
		} else if (opeType.equals("modify")) {
			//checking is area increased(1) or decreased(2) 
		//	if(bean.getCondition()!=null){
				if(bean.getCondition().equals("1")){
					//here area is work work is increasing
					query = "UPDATE QS_BOQ_AREA_MAPPING SET WO_PERCENTAGE_AVAIL=(WO_WORK_INITIATE_AREA/WO_WORK_AREA)*100,  WO_WORK_AVAILABE_AREA=WO_WORK_AVAILABE_AREA-?,WO_WORK_INITIATE_AREA=WO_WORK_INITIATE_AREA+? WHERE WO_WORK_AREA_ID=?";
				}else if(bean.getCondition().equals("2")){
					query = "UPDATE QS_BOQ_AREA_MAPPING SET WO_PERCENTAGE_AVAIL=(WO_WORK_INITIATE_AREA/WO_WORK_AREA)*100,WO_WORK_AVAILABE_AREA=WO_WORK_AVAILABE_AREA+?,WO_WORK_INITIATE_AREA=WO_WORK_INITIATE_AREA-? WHERE WO_WORK_AREA_ID=?";	
				}
				Object[] params = {  bean.getSelectedArea(), bean.getSelectedArea(),bean.getWorkAreaId() };
				result = jdbcTemplate.update(query, params);
		//	}
		}
		
/*		try {
			query="Update QS_BOQ_AREA_MAPPING set WO_PERCENTAGE_AVAIL=(WO_WORK_INITIATE_AREA/WO_WORK_AREA)*100 where WO_WORK_AREA_ID=?";
			Object[] params = {bean.getWorkAreaId() };
			result = jdbcTemplate.update(query, params);
		} catch (Exception e) {
			log.info("Error occurred while updating the percentage "+e.getMessage());
		}
*/		
	}
	@Override
	public int batchUpdateWorkAreaMapping(final List<WorkOrderBean> updateBOQWorkAreaDetails, String taskName) {
		// TODO Auto-generated method stub
		String query="UPDATE QS_BOQ_AREA_MAPPING SET WO_PERCENTAGE_AVAIL=(WO_WORK_INITIATE_AREA/WO_WORK_AREA)*100, WO_WORK_AVAILABE_AREA=WO_WORK_AVAILABE_AREA+?,WO_WORK_INITIATE_AREA=WO_WORK_INITIATE_AREA-? WHERE WO_WORK_AREA_ID=?";
		final int len=updateBOQWorkAreaDetails.size();
		int result[] = jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {
			
			@Override
			public int getBatchSize() {
				return len;
			} 
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				WorkOrderBean bean=updateBOQWorkAreaDetails.get(i);
				ps.setString(1, bean.getSelectedArea());
				ps.setString(2, bean.getSelectedArea());
				ps.setString(3, bean.getWorkAreaId());
 			}
		});

		log.info(result.length);
		return result.length;
	}
	
	/**
	 * @author admin
	 * @description this method will return the BOQ material details
	 */
	@Override
	public List<Map<String, Object>> loadMaterialBoqDetailsForWO(WorkOrderBean bean) {
		StringBuffer query=new StringBuffer("select  MATERIAL_GROUP_NAME,QBPD.WO_WORK_AREA ,QBPD.MATERIAL_GROUP_ID ,QBPD.MATERIAL_GROUP_MEASUREMENT_ID , QBPD.PER_UNIT_QUANTITY , QBPD.PER_UNIT_AMOUNT ")
				.append(" from QS_BOQ_PRODUCT_DTLS QBPD,PRODUCT_GROUP_MASTER PGM ")
				.append(" where  PGM.MATERIAL_GROUP_ID= QBPD.MATERIAL_GROUP_ID ")
				.append(" AND PGM.MATERIAL_GROUP_MEASUREMENT_ID= QBPD.MATERIAL_GROUP_MEASUREMENT_ID  ")
				.append(" AND QBPD.WO_WORK_AREA_ID=? AND QBPD.SITE_ID=? order by QBPD.MATERIAL_GROUP_ID,QBPD.MATERIAL_GROUP_MEASUREMENT_ID");
		
		Object[]  queryParams={	bean.getWorkAreaId(),bean.getSiteId()};
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query.toString(),queryParams);
		return  list;
	}
	
	@Override
	public List<Map<String, Object>> loadWorkOrderMaterialBOQ(WorkOrderBean bean) {
		StringBuffer query=new StringBuffer("select PGM.MATERIAL_GROUP_NAME,QWPD.WO_WORK_ISSUE_AREA_DTLS_ID ,QWPD.MATERIAL_GROUP_ID , ")
				.append(" QWPD.UOM ,QWPD.PER_UNIT_QUANTITY ,QWPD.PER_UNIT_AMOUNT,QWPD.TOTAL_QUANTITY,QWPD.TOTAL_AMOUNT ")
				.append(" from QS_WORKORDER_PRODUCT_DTLS QWPD ,PRODUCT_GROUP_MASTER PGM ")
				.append(" where   PGM.MATERIAL_GROUP_ID= QWPD.MATERIAL_GROUP_ID   AND PGM.MATERIAL_GROUP_MEASUREMENT_ID= QWPD.UOM")
				.append(" AND  WO_WORK_ISSUE_AREA_DTLS_ID=? ")
				.append(" order by QWPD.MATERIAL_GROUP_ID,QWPD.UOM");
		Object[]  queryParams={	bean.getTempIssueAreaDetailsId()};
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query.toString(),queryParams);
		return list;
	}
	
	/**
	 * #description this method is used to insert the work order material details
	 */
	@Override
	public int insertDataIntoWorkOrderTempProductDtls(final List<WorkOrderBean> listOfMaterialBoq,String operationType) {
		StringBuffer query=new StringBuffer();
		int result=0;
		if(operationType.equals("Del_insert")){
			WorkOrderBean bean= listOfMaterialBoq.get(0);
			result=jdbcTemplate.update("DELETE FROM QS_WORKORDER_TEMP_PRODUCT_DTLS WHERE WO_WORK_TEMP_ISSUE_AREA_DTLS_ID=?",bean.getTempIssueAreaDetailsId());
		}
		
		query.append(" INSERT  INTO QS_WORKORDER_TEMP_PRODUCT_DTLS (QS_WORKORDER_TEMP_PRODUCT_DTLS_ID,WO_WORK_TEMP_ISSUE_AREA_DTLS_ID,")
			 .append(" MATERIAL_GROUP_ID, UOM,WORK_AREA,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS) ")
			 .append(" VALUES(QS_WORKORDER_TEMP_PRODUCT_DTLS_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,'A',?)");
		
		int result1[] = jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {
			
			@Override
			public int getBatchSize() {
				return listOfMaterialBoq.size();
			}
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				WorkOrderBean bean= listOfMaterialBoq.get(i);
				ps.setString(1,bean.getTempIssueAreaDetailsId());
				ps.setString(2, bean.getMaterialGroupId());
				ps.setString(3, bean.getUnitsOfMeasurement1());
				ps.setString(4, bean.getSelectedArea());
				ps.setString(5, bean.getPerUnitQuantity());
				ps.setString(6, bean.getPerUnitAmount());
				ps.setString(7, bean.getTotalQuantity());
				ps.setString(8, bean.getTotalAmount());
				ps.setString(9,bean.getRemarks());
			}
		});		
		result=result1.length;
		return result;
	}
	
	@Override
	public int deleteWorkOrderTempProductDtls(String string, String string2) {
		
		return 0;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method is for reallocating all the work area while revise work order
	 *  @param oldWorkOrderIssueId  is holding the work order issue id for selecting the work area data which  are going to reallocating the given area to contractor while creating the work order
	 *  @return List will hold the result of method and sequence number of 
	 */
	
@Override
public int revertAlltheAllocatedQuantity(String oldWorkOrderIssueId) {
	StringBuffer query =new StringBuffer("UPDATE QS_BOQ_AREA_MAPPING QBAM SET") 
			.append(" WO_WORK_AVAILABE_AREA=WO_WORK_AVAILABE_AREA+(select AREA_ALOCATED_QTY from  QS_WORKORDER_ISSUE_AREA_DETAILS QWIA ")
			.append(" WHERE WO_WORK_ISSUE_DTLS_ID=? AND QBAM.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID),")
			.append(" WO_WORK_INITIATE_AREA=WO_WORK_INITIATE_AREA-(select AREA_ALOCATED_QTY from  QS_WORKORDER_ISSUE_AREA_DETAILS QWIA ")
			.append(" WHERE WO_WORK_ISSUE_DTLS_ID=?  AND QBAM.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID)")
			.append(" where WO_WORK_AREA_ID IN (select WO_WORK_AREA_ID from  QS_WORKORDER_ISSUE_AREA_DETAILS QWIA")
			.append(" WHERE WO_WORK_ISSUE_DTLS_ID=?)");
	Object[] obj={oldWorkOrderIssueId,oldWorkOrderIssueId,oldWorkOrderIssueId};
	int result=jdbcTemplate.update(query.toString(),obj);
	return result;
}

/**
 *  @author Aniket Chavan
 *  @description this method is for selecting the pending work order/NMR  
 *  @param pendingEmpId is holding the current user id by using this used id we are loading the work order details.
 *  @return List<WorkOrderBean> will hold the result of method 
 */
	@Override
	public List<WorkOrderBean> getPendingWorkOrder(String pendingEmpId, String siteId,String statusType) {
		final List<WorkOrderBean> list = new ArrayList<WorkOrderBean>();
		StringBuffer query = new StringBuffer("");
		Object[] queryParams = { statusType };
		if(statusType.equals("DF")){
			query = new StringBuffer("SELECT QW.TYPE_OF_WORK,QW.PER_WORK_ORDER_NUMBER,CONCAT(FIRST_NAME,' '||LAST_NAME)  AS CONTRACTOR_NAME ,QW.TYPE_OF_WORK,QW.WORK_ORDER_NAME,QW.QS_WO_TEMP_ISSUE_ID,TO_CHAR(QW.CREATED_DATE,'DD-MM-YYYY HH24:MI:SS') AS CREATED_DATE,QW.PENDING_EMP_ID,QW.CONTRACTOR_ID,QW.TEMP_WORK_ORDER_NUMBER,SI.SITE_ID,SI.SITE_NAME,QW.WORK_ORDER_DATE FROM QS_WORKORDER_TEMP_ISSUE QW  LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QW.CONTRACTOR_ID  ,SITE SI ")
					.append("WHERE SI.SITE_ID=QW.SITE_ID  AND QW.STATUS=? ");
			if(siteId!=null&&!siteId.equals("ALL")){
				query.append(" AND QW.SITE_ID= '"+siteId+"' ");
			}
		}else{
			query = new StringBuffer("SELECT QW.TYPE_OF_WORK,QW.PER_WORK_ORDER_NUMBER,CONCAT(FIRST_NAME,' '||LAST_NAME)  AS CONTRACTOR_NAME ,QW.TYPE_OF_WORK,QW.WORK_ORDER_NAME,QW.QS_WO_TEMP_ISSUE_ID,TO_CHAR(QW.CREATED_DATE,'DD-MM-YYYY HH24:MI:SS') AS CREATED_DATE,QW.PENDING_EMP_ID,QW.CONTRACTOR_ID,QW.TEMP_WORK_ORDER_NUMBER,SI.SITE_ID,SI.SITE_NAME,QW.WORK_ORDER_DATE FROM QS_WORKORDER_TEMP_ISSUE QW LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QW.CONTRACTOR_ID ,SITE SI")
					.append(" WHERE  SI.SITE_ID=QW.SITE_ID")
					.append(" AND  QW.PENDING_EMP_ID='")
					.append(pendingEmpId)
					.append("' AND QW.STATUS=?");
			if(!(siteId!=null&&siteId.equals("ALL"))){
				query.append(" AND QW.SITE_ID= '"+siteId+"' ");
			}
		}
		jdbcTemplate.query(query.toString(), queryParams, new ResultSetExtractor<List<WorkOrderBean>>() {
			@Override
			public List<WorkOrderBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					WorkOrderBean bean = new WorkOrderBean();
					String typeOfWork=rs.getString("TYPE_OF_WORK") == null ? "-" : rs.getString("TYPE_OF_WORK");
					if(typeOfWork.equals("PIECEWORK")){
						typeOfWork="PIECE WORK";
					}
					bean.setTypeOfWork(typeOfWork);
					bean.setWorkOrderNo(rs.getString("PER_WORK_ORDER_NUMBER") == null ? "0" : rs.getString("PER_WORK_ORDER_NUMBER"));
					bean.setSiteName(rs.getString("site_name") == null ? "0" : rs.getString("site_name"));
					bean.setQS_Temp_Issue_Id(rs.getString("QS_WO_TEMP_ISSUE_ID") == null ? "0" : rs.getString("QS_WO_TEMP_ISSUE_ID"));
					bean.setWorkOrderCreadeDate(rs.getString("CREATED_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("CREATED_DATE"));
					bean.setPendingEmpId(rs.getString("PENDING_EMP_ID") == null ? "0" : rs.getString("PENDING_EMP_ID"));
					bean.setContractorId(rs.getString("CONTRACTOR_ID") == null ? "0" : rs.getString("CONTRACTOR_ID"));
					String work_order_number = rs.getString("TEMP_WORK_ORDER_NUMBER") == null ? "0": rs.getString("TEMP_WORK_ORDER_NUMBER");
					bean.setSiteWiseWONO(work_order_number);
					bean.setContractorName(rs.getString("CONTRACTOR_NAME") == null ? "0" : rs.getString("CONTRACTOR_NAME"));
					bean.setWorkOrderName(rs.getString("WORK_ORDER_NAME") == null ? "" : rs.getString("WORK_ORDER_NAME"));
				
					bean.setSiteId(rs.getString("SITE_ID") == null ? "" : rs.getString("SITE_ID"));
					bean.setWorkOrderDate(rs.getString("WORK_ORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("WORK_ORDER_DATE").toUpperCase());
					list.add(bean);
				}
				return null;
			}
		});
	
		return list;
	}

	/**
	 *  @author Aniket Chavan
	 *  @description this method used for checking is this processing work order number is valid for the user or not
	 *  @param workOrderTempIssueId is holding the current workOrderTempIssueId
	 *  @return boolean value valid or not
	 */
	@Override
	public boolean checkWorkOrderNumberIsValidForEmployee(String workOrderTempIssueId, String user_id, boolean status, String statusType) {
		int count = 0;
		//if status is true the the query for my work order status
		if (status) {
			count = jdbcTemplate.queryForInt("SELECT count(1) FROM QS_WORKORDER_TEMP_ISSUE WHERE QS_WO_TEMP_ISSUE_ID =? ",workOrderTempIssueId);
		} else {
			if(statusType.equals("DF")){
				count = jdbcTemplate.queryForInt("SELECT count(1) FROM QS_WORKORDER_TEMP_ISSUE WHERE QS_WO_TEMP_ISSUE_ID =?  AND STATUS=?",workOrderTempIssueId,statusType);
			}else{
				count = jdbcTemplate.queryForInt("SELECT count(1) FROM QS_WORKORDER_TEMP_ISSUE WHERE QS_WO_TEMP_ISSUE_ID =? AND PENDING_EMP_ID =? AND STATUS=?",workOrderTempIssueId,user_id,statusType);
			}
		}

		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 *  @author Aniket Chavan
	 *  @description this method used for checking is this processing work order number is valid for the user or not
	 *  @param workOrderTempIssueId is holding the current workOrderTempIssueId
	 *  @return boolean value valid or not
	 */
	@Override
	public String getWorkOrderNumber(String siteWiseWorkOrderNo, int site_Id, String user_id) {
		int result = 0;
		String query = "select QS_WO_TEMP_ISSUE_ID from QS_WORKORDER_TEMP_ISSUE where SITE_ID = ? AND TEMP_WORK_ORDER_NUMBER = ?";
		result = jdbcTemplate.queryForInt(query, new Object[] {site_Id, siteWiseWorkOrderNo });
		return String.valueOf(result);
	}

	/**
	 *  @author Aniket Chavan
	 *  @description this method used for checking is this processing work order number is valid for the user or not
	 *  @param workOrderTempIssueId is holding the current workOrderTempIssueId
	 *  @return boolean value valid or not
	 */
	@Override
	public WorkOrderBean getWorkOrderDetailsByWorkOrderId(String workOrderTempIssueNo) {
		StringBuffer query = new StringBuffer("SELECT  QWTS.WO_RECORD_CONTAINS,QWTS.TOTAL_LABOUR_AMOUNT,QWTS.TOTAL_MATERIAL_AMOUNT,QWTS.STATUS,QWTS.BOQ_NO,QWTS.VERSION_NUMBER ,QWTS.REVISION,QWTS.OLD_WORK_ORDER_NUMBER,QWTS.TYPE_OF_WORK,QWTS.PER_WORK_ORDER_NUMBER,QWTS.WORK_ORDER_NAME,QWTS.QS_WO_TEMP_ISSUE_ID,QWTS.TEMP_WORK_ORDER_NUMBER,to_char(to_date(QWTS.WORK_ORDER_DATE,'dd-MM-yyyy'),'dd-MM-yyyy') as WORK_ORDER_DATE, QWTS.CREATED_BY ,QWTS.PENDING_EMP_ID, S.SITE_NAME,S.SITE_ID,QWTS.CREATED_DATE,QWTS.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME,SC.ADDRESS,SC.GSTIN,SC.PAN_NUMBER,SC.MOBILE_NUMBER,")
				.append(" (SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTS.CREATED_BY and QS_WO_TEMP_ISSUE_ID = ?) as WORK_ORDER_EMP_NAME,")
				.append(" (SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTS.PENDING_EMP_ID and QS_WO_TEMP_ISSUE_ID = ?) as PENDING_EMP_NAME ")
				.append(" FROM QS_WORKORDER_TEMP_ISSUE QWTS LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QWTS.CONTRACTOR_ID, SITE S WHERE QWTS.QS_WO_TEMP_ISSUE_ID = ? AND S.SITE_ID = QWTS.SITE_ID");
		
		Object[] obj = { workOrderTempIssueNo,workOrderTempIssueNo, workOrderTempIssueNo };
		final StringBuffer queryForLoadingPW_Amount=new StringBuffer("SELECT SUM(QCBT.PAYBLE_AMOUNT) AS PAYBLE_AMOUNT ")
				.append(" ,SUM((SELECT SUM(DEDUCTION_AMOUNT) FROM QS_BILL_DEDUCTION_DTLS  WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID AND TYPE_OF_DEDUCTION!='ADV'  AND  QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID)) AS TOTAL_DEDUCTION_AMOUNT ")
				.append(" FROM QS_CONTRACTOR_BILL QCBT LEFT JOIN QS_BILL_DEDUCTION_DTLS QSDD ON QSDD.CONTRACTOR_BILL_ID=QCBT.BILL_ID  AND QSDD.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID AND QSDD.TYPE_OF_DEDUCTION='ADV' ")
				.append(" WHERE CONTRACTOR_ID=?  AND PAYMENT_TYPE IN('ADV','RA') AND QCBT.QS_WORKORDER_ISSUE_ID = ?  ORDER BY  QCBT.TEMP_BILL_ID");

		WorkOrderBean workOrderBean = jdbcTemplate.query(query.toString(), obj, new ResultSetExtractor<WorkOrderBean>() {

			@Override
			public WorkOrderBean extractData(ResultSet rs) throws SQLException, DataAccessException {
				String workOrderStatus="";
				WorkOrderBean bean = new WorkOrderBean();
				while (rs.next()) {
					//String temp_issue_dtls_id = rs.getString("WO_WORK_ISSUE_DTLS_ID");
					bean.setQS_Temp_Issue_Id(rs.getString("QS_WO_TEMP_ISSUE_ID") == null ? "0" : rs.getString("QS_WO_TEMP_ISSUE_ID"));
					String work_order_number = rs.getString("TEMP_WORK_ORDER_NUMBER") == null ? "0": rs.getString("TEMP_WORK_ORDER_NUMBER");
					bean.setSiteWiseWONO(work_order_number);
					bean.setWorkOrderNo(rs.getString("PER_WORK_ORDER_NUMBER") == null ? "0": rs.getString("PER_WORK_ORDER_NUMBER"));
					workOrderStatus=rs.getString("STATUS") == null ? "-": rs.getString("STATUS");
					if(workOrderStatus.equals("DF")){workOrderStatus="DraftModify";}
					if(workOrderStatus.equals("M")){workOrderStatus="ModifyWorkOrder";}
					bean.setWorkOrderStatus(workOrderStatus);
					bean.setTypeOfWork(rs.getString("TYPE_OF_WORK") == null ? "0": rs.getString("TYPE_OF_WORK"));
					bean.setWorkOrderDate(rs.getString("WORK_ORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("WORK_ORDER_DATE"));
					bean.setWorkOrderName(rs.getString("WORK_ORDER_NAME") == null ? "": rs.getString("WORK_ORDER_NAME"));
					bean.setWoRecordContains(rs.getString("WO_RECORD_CONTAINS") == null ? "": rs.getString("WO_RECORD_CONTAINS"));
					bean.setWorkorderFrom(rs.getString("PENDING_EMP_NAME") == null ? "" : rs.getString("PENDING_EMP_NAME"));
					bean.setPendingEmpId(rs.getString("PENDING_EMP_ID") == null ? "0" : rs.getString("PENDING_EMP_ID"));
					
					bean.setContractorId(rs.getString("CONTRACTOR_ID") == null ? "0" : rs.getString("CONTRACTOR_ID"));
					bean.setWorkOrderCreadeDate(rs.getString("CREATED_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("CREATED_DATE"));
					bean.setSiteName(rs.getString("SITE_NAME") == null ? "" : rs.getString("SITE_NAME"));
					bean.setSiteId(rs.getString("SITE_ID") == null ? "" : rs.getString("SITE_ID"));
					
					//bean.setFromEmpName(rs.getString("WORK_ORDER_EMP_NAME") == null ? ""	: rs.getString("WORK_ORDER_EMP_NAME"));
					//bean.setToEmpName(rs.getString("PENDING_EMP_NAME") == null ? "" : rs.getString("PENDING_EMP_NAME"));
					
					bean.setContractorName(rs.getString("CONTRACTOR_NAME"));
					bean.setGSTIN(rs.getString("GSTIN")==null?"-":rs.getString("GSTIN"));
					bean.setContractorAddress(rs.getString("ADDRESS") == null ? "-" : rs.getString("ADDRESS"));
					bean.setContractorPhoneNo(rs.getString("MOBILE_NUMBER") == null ? "-" : rs.getString("MOBILE_NUMBER"));
					bean.setContractorPanNo(rs.getString("PAN_NUMBER") == null ? "-" : rs.getString("PAN_NUMBER"));
					
					bean.setBoqNo(rs.getString("BOQ_NO") == null ? "-" : rs.getString("BOQ_NO"));
					bean.setRevision(rs.getString("REVISION") == null ? "" : rs.getString("REVISION"));
					bean.setVersionNumber(rs.getString("VERSION_NUMBER") == null ? "" : rs.getString("VERSION_NUMBER"));
					bean.setOldWorkOrderNo(rs.getString("OLD_WORK_ORDER_NUMBER") == null ? "" : rs.getString("OLD_WORK_ORDER_NUMBER"));
					bean.setMaterialWoAmount(rs.getString("TOTAL_MATERIAL_AMOUNT") == null ? "0" : rs.getString("TOTAL_MATERIAL_AMOUNT"));
					bean.setLaborWoAmount(rs.getString("TOTAL_LABOUR_AMOUNT") == null ? "0" : rs.getString("TOTAL_LABOUR_AMOUNT"));
			
					try {
						String issueId=jdbcTemplate.queryForObject("SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID=?", new Object[]{bean.getOldWorkOrderNo(),bean.getSiteId()}, String.class);

						List<Map<String, Object>> billDetails=jdbcTemplate.queryForList(queryForLoadingPW_Amount.toString(),bean.getContractorId(),issueId);
								for (Map<String, Object> billData : billDetails) {
									double woBillPaidAmount=billData.get("PAYBLE_AMOUNT")==null?0.0:Double.valueOf(billData.get("PAYBLE_AMOUNT").toString());
									double deductionAmount=billData.get("TOTAL_DEDUCTION_AMOUNT")==null?0.0:Double.valueOf(billData.get("TOTAL_DEDUCTION_AMOUNT").toString());
									double woBillBilledAmount=woBillPaidAmount+deductionAmount;
									bean.setWoBillBilledAmount(woBillBilledAmount);
									bean.setWoBillPaidAmount(woBillPaidAmount);
						}
					} catch (Exception e) {
						System.out.println("Got issue while loading work order billed amount ::::::::::::::::  \n"+e.getMessage());
					}
				
				}
				
				return bean;
			}	
		});
	
		String siteId=workOrderBean.getSiteId();
		String query2 = "SELECT SED.EMP_NAME,QWCAT.PURPOSE from  QS_WORKORDER_CRT_APPRL_DTLS QWCAT,SUMADHURA_EMPLOYEE_DETAILS SED,QS_WORKORDER_TEMP_ISSUE QWTS where  SED.EMP_ID=QWCAT.WO_CREATE_APPROVE_EMP_ID and QWCAT.TEMP_WORK_ORDER_NUMBER=QWTS.TEMP_WORK_ORDER_NUMBER and QS_WO_TEMP_ISSUE_ID=? and QWCAT.SITE_ID=? order by QS_WO_CRT_APPRL_DTLS_ID";
		List<Map<String, Object>> purposeList = jdbcTemplate.queryForList(query2, new Object[] { workOrderTempIssueNo,siteId });
		
		StringBuffer purposeView = new StringBuffer();
		for (Map<String, Object> prods2 : purposeList) {
			String empName = prods2.get("EMP_NAME") == null ? "" : prods2.get("EMP_NAME").toString();
			String purpose = prods2.get("PURPOSE") == null ? "" : prods2.get("PURPOSE").toString();
			purpose=purpose.trim();
			if(purpose.length()!=0)
				purposeView.append(empName+" - "+ purpose+"\n");
		
		}
		
		workOrderBean.setPurpose(purposeView.toString());
		return workOrderBean;
	}
//==========================================Selecting all the rows of work order============================================
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used for Selecting all the rows of work order details
	 *  @param workOrderBean object is holding the data which are going to use for selection of data
	 *  @return List<WorkOrderBean>
	 */
	@Override
	public List<WorkOrderBean> getCreatedWorkOrderDetails(WorkOrderBean workOrderBean) {
		StringBuffer queryForWODetail =new StringBuffer("select QWTS.WO_WORK_TEMP_ISSUE_DTLS_ID,QWTS.TOTAL_AMOUNT ,QWTS.CREATED_DATE ,QWTS.STATUS ,QWTS.WO_COMMENT ,QWTS.QS_WO_TEMP_ISSUE_ID ,QWTS.WO_MANUAL_DESC ,QWTS.QUANTITY,QWM.WO_MAJORHEAD_ID ,QWM.WO_MAJORHEAD_DESC, QWMM.WO_MINORHEAD_ID ,QWMM.WO_MINORHEAD_DESC ,QWW.WO_WORK_DESC_ID ,QWW.WO_WORK_DESCRIPTION ,QWQM.WO_MEASURMEN_NAME,QWQM.WO_MEASURMENT_ID  ")
				.append(" from  QS_WORKORDER_TEMP_ISSUE_DTLS QWTS ,QS_WO_MAJORHEAD QWM,QS_WO_MINORHEAD QWMM,QS_WO_WORKDESC QWW,QS_WO_MEASURMENT QWQM ")
				.append(" where QWTS.QS_WO_TEMP_ISSUE_ID=? and QWTS.STATUS='A' and QWTS.WO_MINORHEAD_ID=QWMM.WO_MINORHEAD_ID   and QWTS.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID AND QWW.WO_MINORHEAD_ID=QWMM.WO_MINORHEAD_ID AND QWMM.WO_MAJORHEAD_ID=QWM.WO_MAJORHEAD_ID AND QWQM.WO_MEASURMENT_ID=QWTS.UOM AND QWQM.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID  order by QWM.WO_MAJORHEAD_DESC,QWMM.WO_MINORHEAD_DESC,QWW.WO_WORK_DESCRIPTION "); 
	//if this is the  revise NMR  work order then we have to select the payment initiated area
		if(workOrderBean.getWorkOrderNo().contains("/R")){
			queryForWODetail=new StringBuffer("select (select RTRIM(XMLAGG(XMLELEMENT(E,concat(QIAWP.ALLOCATED_QTY,'##'||QIAWP.NO_OF_HOURS||'##'||QIAWP.RATE),'@@').EXTRACT('//text()') ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID).GetClobVal(),'@@')  ")
					.append(" from QS_INV_AGN_WORK_PMT_DTL_HR QIAWP,QS_CONTRACTOR_BILL QCB where QIAWP.ALLOCATED_QTY!=0 AND QCB.BILL_ID=QIAWP.BILL_ID  ")
					.append(" AND QCB.QS_WORKORDER_ISSUE_ID=QIAWP.QS_WORKORDER_ISSUE_ID AND QIAWP.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+workOrderBean.getOldWorkOrderNo()+"' AND SITE_ID='"+workOrderBean.getSiteId()+"' ) AND QIAWP.WO_WORK_DESC_ID=QWTS.WO_WORK_DESC_ID AND QIAWP.WO_MINORHEAD_ID=QWTS.WO_MINORHEAD_ID    AND QIAWP.WO_ROW_CODE=QWTS.WO_ROW_CODE ) as PAYMENTDONE  ")
					.append(" ,QWTS.WO_WORK_TEMP_ISSUE_DTLS_ID,QWTS.TOTAL_AMOUNT ,QWTS.CREATED_DATE ,QWTS.STATUS ,QWTS.WO_COMMENT ,QWTS.QS_WO_TEMP_ISSUE_ID ,QWTS.WO_MANUAL_DESC ,QWTS.QUANTITY,QWM.WO_MAJORHEAD_ID ,QWM.WO_MAJORHEAD_DESC, QWMM.WO_MINORHEAD_ID ,QWMM.WO_MINORHEAD_DESC ,QWW.WO_WORK_DESC_ID ,QWW.WO_WORK_DESCRIPTION ,QWQM.WO_MEASURMEN_NAME,QWQM.WO_MEASURMENT_ID ")
					.append(" from  QS_WORKORDER_TEMP_ISSUE_DTLS QWTS ,QS_WO_MAJORHEAD QWM,QS_WO_MINORHEAD QWMM,QS_WO_WORKDESC QWW,QS_WO_MEASURMENT QWQM  ")
					.append(" where QWTS.QS_WO_TEMP_ISSUE_ID=? and QWTS.STATUS='A' and QWTS.WO_MINORHEAD_ID=QWMM.WO_MINORHEAD_ID   and QWTS.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID AND QWW.WO_MINORHEAD_ID=QWMM.WO_MINORHEAD_ID AND QWMM.WO_MAJORHEAD_ID=QWM.WO_MAJORHEAD_ID AND QWQM.WO_MEASURMENT_ID=QWTS.UOM AND QWQM.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID  order by QWM.WO_MAJORHEAD_DESC,QWMM.WO_MINORHEAD_DESC,QWTS.WO_ROW_CODE,QWW.WO_WORK_DESCRIPTION ");
		}
		
		Object[] obj = {workOrderBean.getQS_Temp_Issue_Id()};
	
		List<WorkOrderBean> workOrderList = jdbcTemplate.query(queryForWODetail.toString(), obj,
				new ResultSetExtractor<List<WorkOrderBean>>() {
				//changed code if list is giving problem change to list
					@Override
					public List<WorkOrderBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
						List<WorkOrderBean> workOrderList = new ArrayList<WorkOrderBean>();
					//	int row = 1;
						while (rs.next()) {
							WorkOrderBean bean = new WorkOrderBean();
							// for temporary issue details
							try {
								String paymentInitiated=rs.getString("PAYMENTDONE");	
								bean.setNmrPaymentDetails(paymentInitiated);
							} catch (Exception e) {
								log.info(" getALLCompltedWorkOrderS "+e.getMessage());
							}
							String temp_issue_dtls_id = rs.getString("WO_WORK_TEMP_ISSUE_DTLS_ID");
							bean.setQS_Temp_Issue_Dtls_Id(temp_issue_dtls_id);
							bean.setQS_Temp_Issue_Id(rs.getString("QS_WO_TEMP_ISSUE_ID") == null ? "0.0": rs.getString("QS_WO_TEMP_ISSUE_ID"));
							bean.setWorkOrderCreadeDate(rs.getString("CREATED_DATE") == null ? "": rs.getString("CREATED_DATE"));
							bean.setQuantity(rs.getString("QUANTITY") == null ? "": rs.getString("QUANTITY"));
							String manualDesc = rs.getString("WO_MANUAL_DESC") == null ? "": rs.getString("WO_MANUAL_DESC");
							bean.setwOManualDescription(manualDesc);
							bean.setTotalAmount1(rs.getString("TOTAL_AMOUNT") == null ? "0.0" : rs.getString("TOTAL_AMOUNT"));
							bean.setComments1(rs.getString("WO_COMMENT") == null ? "" : rs.getString("WO_COMMENT").replace("@@", " "));

							bean.setWoMajorHead(rs.getString("WO_MAJORHEAD_ID") == null ? "" : rs.getString("WO_MAJORHEAD_ID"));
							bean.setWoMinorHeads(rs.getString("WO_MINORHEAD_ID") == null ? "" : rs.getString("WO_MINORHEAD_ID"));
							bean.setwODescription(rs.getString("WO_WORK_DESC_ID") == null ? "" : rs.getString("WO_WORK_DESC_ID"));
							bean.setUnitsOfMeasurement(	rs.getString("WO_MEASURMENT_ID") == null ? "" : rs.getString("WO_MEASURMENT_ID"));

							bean.setWO_MajorHead1(rs.getString("WO_MAJORHEAD_DESC") == null ? "" : rs.getString("WO_MAJORHEAD_DESC"));
							bean.setWO_MinorHead1(rs.getString("WO_MINORHEAD_DESC") == null ? "" : rs.getString("WO_MINORHEAD_DESC"));
							bean.setWO_Desc1(rs.getString("WO_WORK_DESCRIPTION") == null ? "": rs.getString("WO_WORK_DESCRIPTION"));
							bean.setUnitsOfMeasurement1(rs.getString("WO_MEASURMEN_NAME") == null ? "": rs.getString("WO_MEASURMEN_NAME").trim());
							workOrderList.add(bean);
						}
						return workOrderList;
					}
				});

		//Collections.sort(workOrderList);
		return workOrderList;
	}// method getCreatedWorkOrderDetails() completed
	
//====================This method is used for revise work order and selecting the payment initiated area==============================	

	/**
	 *  @author Aniket Chavan
	 *  @description this method used for Selecting all the rows of work order details
	 *  @param workOrderBean object is holding the data which are going to use for selection of data
	 *  @return List<WorkOrderBean>
	 */
@Override
public List<Map<String, Object>> loadWOAreaMappingForReviseByWorkOrderNo(WorkOrderBean bean){
	String workOrderNo=bean.getWorkOrderNo();
	String siteId=bean.getSiteId();
	String workOrderIssueDtlsNO=bean.getQS_Temp_Issue_Dtls_Id();
	String workDescId= bean.getWO_Desc1();
	String unitsOfMeasurementId=bean.getUnitsOfMeasurement1();
	String boqNo=bean.getBoqNo();String typeOfWork=bean.getTypeOfWork();
	StringBuffer loadWoAreaMapping = new StringBuffer();
	 List<Map<String, Object>> areaMappingList = new ArrayList<Map<String,Object>>();
	List<String> areaId = new ArrayList<String>();
	StringBuffer query = new StringBuffer("Select distinct(QSTA.WO_WORK_ISSUE_AREA_DTLS_ID),QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,(QWAM.WO_WORK_AREA_ID),")
		//this sub query is for selecting previous allocated quantity of piece work
		.append(" (select  LISTAGG(concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT ")
		.append(" WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID  AND   QCBT.BILL_ID=QSINA.BILL_ID ")//and  QCBT.PAYMENT_TYPE='RA'  
		.append(" AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER =? AND SITE_ID=? ) and QSINA.WO_WORK_AREA_ID=QSTA.WO_WORK_AREA_ID   AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QSTA.WO_WORK_ISSUE_AREA_DTLS_ID  group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID) as paymentDone,")
		//this sub query is end's here
		.append(" QWAM.QS_AREA_PRICE_PER_UNIT,QWAM.QS_AREA_AMOUNT,QB.BOQ_NO,qsta.WO_WORK_ISSUE_AREA_DTLS_ID,QSTA.AREA_ALOCATED_QTY ,QSTA.PRICE,TD.TOTAL_AMOUNT ,TD.CREATED_DATE ,TD.STATUS ,TD.QS_WORKORDER_ISSUE_ID ,TD.WO_COMMENT,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME")
		.append(" ,F.NAME AS FLOOR_NAME,FLAT.name as FLATNAME,block.NAME as BLOCK_NAME,     (case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end)as BLOCK_ID,  (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,  (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID,SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL")
		.append(" ,(select LISTAGG(RECORD_TYPE, '@@')  WITHIN GROUP (ORDER BY WO_WORK_DESC_ID)  from QS_BOQ_AREA_MAPPING QBAM1 where QBAM1.SITE_ID= QWAM.SITE_ID AND QBAM1.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID   AND QBAM1.WO_WORK_AREA_GROUP_ID=QWAM.WO_WORK_AREA_GROUP_ID) as WO_CONTAINS")
		.append(" from QS_BOQ QB, QS_WORKORDER_ISSUE_DETAILS TD,QS_WORKORDER_ISSUE_AREA_DETAILS QSTA,QS_WORKORDER_ISSUE QWI, ")
		.append(" QS_BOQ_AREA_MAPPING QWAM  left outer join  BLOCK block on block.BLOCK_ID = QWAM.BLOCK_ID")
		.append(" left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID ")
		.append(" left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,")
		.append(" QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,SITE SI")
		.append(" where QWI.QS_WORKORDER_ISSUE_ID=TD.QS_WORKORDER_ISSUE_ID and  TD.WO_WORK_ISSUE_DTLS_ID=QSTA.WO_WORK_ISSUE_DTLS_ID and QSTA.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID 	 AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID and  QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID AND SI.SITE_ID=QWAM.SITE_ID AND QB.BOQ_SEQ_NO=QWAM.BOQ_SEQ_NO ")
		.append(" AND TD.STATUS='A' and QB.STATUS='A' and qsta.status='A' and TD.WO_WORK_ISSUE_DTLS_ID=? and  QWW.WO_WORK_DESC_ID=? ");//  and QWAM.WO_MEASURMENT_ID='"+unitsOfMeasurementId+"'
		if(typeOfWork.equals("MATERIAL")){
			query.append(" AND QWAM.RECORD_TYPE='MATERIAL'");
		}
		query.append(" order by  QSTA.WO_WORK_ISSUE_AREA_DTLS_ID,QWAM.RECORD_TYPE,(case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end),(case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),(case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");

		areaMappingList = jdbcTemplate.queryForList(query.toString(),workOrderNo,siteId,workOrderIssueDtlsNO,workDescId);

		//using this loop we are retrieving all the work area id and, in next query we are not selecting this work area id data
		for (Map<String, Object> map : areaMappingList) {
			String temp = (String) map.get("WO_WORK_AREA_ID");
			areaId.add("'" + temp + "'");
		}
		String temp = areaId.toString().replace("[", "").replace("]", "");
	
		//this code is for unselected work area
		if (temp.equals("")) {
		} else {
			//Working For Order
			loadWoAreaMapping.append("select  (QWAM.WO_WORK_AREA_ID),QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,QWAM.QS_BOQ_DETAILS_ID,QWAM.QS_AREA_PRICE_PER_UNIT,QWAM.QS_AREA_AMOUNT,QB.BOQ_NO,QWAM.WO_WORK_INITIATE_AREA,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME ")
			.append(" ,block.NAME as BLOCK_NAME ,F.NAME AS FLOOR_NAME,FLAT.name as FLATNAME,")
			.append(" (case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end)as BLOCK_ID,")
			.append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,")
			.append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID, ")
			.append(" SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL ")
			.append(" ,(select LISTAGG(RECORD_TYPE, '@@')  WITHIN GROUP (ORDER BY WO_WORK_DESC_ID)  from QS_BOQ_AREA_MAPPING QBAM1 where QBAM1.SITE_ID= QWAM.SITE_ID AND QBAM1.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID   AND QBAM1.WO_WORK_AREA_GROUP_ID=QWAM.WO_WORK_AREA_GROUP_ID) as WO_CONTAINS")
			.append(" from QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK block on block.BLOCK_ID = QWAM.BLOCK_ID ")
			.append(" left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID ")
			.append(" left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,SITE SI ,QS_BOQ QB ")
			.append(" where QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID AND SI.SITE_ID=QWAM.SITE_ID ")
			.append(" AND QWAM.site_id=?  and QB.site_id=? and QWAM.status='A' and QB.status='A' AND QB.BOQ_SEQ_NO=QWAM.BOQ_SEQ_NO and QWAM.WO_WORK_AREA_ID not in("+temp+") and QWW.WO_WORK_DESC_ID=? ");//and QWAM.WO_MEASURMENT_ID=?
			if(typeOfWork.equals("MATERIAL")){
				loadWoAreaMapping.append(" AND QWAM.RECORD_TYPE='MATERIAL'");
			}
			loadWoAreaMapping.append(" order by  QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,(case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end),")
			.append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),")
			.append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");
			//List<Map<String, Object>> unSelectedWorkAreaMappingList = jdbcTemplate.queryForList(loadWoAreaMapping.toString(), siteId,siteId,workDescId);
			areaMappingList.addAll(jdbcTemplate.queryForList(loadWoAreaMapping.toString(), siteId,siteId,workDescId));	
			}
	return areaMappingList;
}//method loadWOAreaMappingForReviseByWorkOrderNo() completed

//====================================this method is used for selecting selected work area details for specific work order======================================

	/**
	 *  @author Aniket Chavan
	 *  @description this method used for Selecting the details of work order area which has given to contractor to work 
	 *   @param workOrderNo is used to get tempWorkOrderIssueId for the query operation
	 *   @param siteId is used to select the data by site wise
	 *   @param workOrderIssueId
	 *   @param operType used for selecting data by the request wise it's status or permanent data
	 *   @param workDeskId 
	 *   @param unitsOfMeasurementId
	 *  @return List<Map<String, Object>>
	 */
	@Override
	public List<Map<String, Object>> loadWOAreaMappingByWorkOrderNo(String workOrderNo, String siteId,
			String workOrderIssueId, String operType, String workDeskId, String unitsOfMeasurementId, String typeOfWork) {
		StringBuffer query = new StringBuffer("");
		StringBuffer loadUnSelectedWOAreaMapping = new StringBuffer("");
		//operation type if one(1) then select the work order detail from permanent table for permanent work order
		if (operType != null)
			if (operType.equals("1")) {
			 query.append("Select (QWAM.WO_WORK_AREA_ID),QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,QWAM.QS_AREA_PRICE_PER_UNIT,QWAM.QS_AREA_AMOUNT,QB.BOQ_NO,qsta.WO_WORK_ISSUE_AREA_DTLS_ID,QSTA.AREA_ALOCATED_QTY ,QSTA.PRICE,TD.TOTAL_AMOUNT ,TD.CREATED_DATE ,TD.STATUS ,TD.QS_WORKORDER_ISSUE_ID ,TD.WO_COMMENT,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME")
				  .append(" ,F.NAME AS FLOOR_NAME,FLAT.name as FLATNAME,block.NAME as BLOCK_NAME,     (case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end)as BLOCK_ID,  (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,  (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID,SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL")
				  .append(" from QS_BOQ QB, QS_WORKORDER_ISSUE_DETAILS TD,QS_WORKORDER_ISSUE_AREA_DETAILS QSTA,QS_WORKORDER_ISSUE QWI, QS_BOQ_AREA_MAPPING QWAM  left outer join  BLOCK block on block.BLOCK_ID = QWAM.BLOCK_ID left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID  left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,SITE SI")
				  .append(" where QWI.QS_WORKORDER_ISSUE_ID=TD.QS_WORKORDER_ISSUE_ID and  TD.WO_WORK_ISSUE_DTLS_ID=QSTA.WO_WORK_ISSUE_DTLS_ID and QSTA.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID 	 AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID and  QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID AND SI.SITE_ID=QWAM.SITE_ID AND QB.BOQ_SEQ_NO=QWAM.BOQ_SEQ_NO ")
				  .append(" AND TD.STATUS='A' and qsta.status='A' and TD.WO_WORK_ISSUE_DTLS_ID=? and  QWW.WO_WORK_DESC_ID=? ")// and QWAM.WO_MEASURMENT_ID=?)
				  .append(" order by QSTA.WO_WORK_ISSUE_AREA_DTLS_ID,(case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end),(case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),(case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");
				List<Map<String, Object>> selectedWorkAreaDetailsList = jdbcTemplate.queryForList(query.toString(), workOrderIssueId,workDeskId);
				return selectedWorkAreaDetailsList;
			}
		//Selecting selected workArea
		//if the work order no contains ("/R") means this is the revise work order so we have to selected payment initiated area or quantity from  bills table
		if (workOrderNo.contains("/R")) {
			//get the old work order number  
			//this temporary work order is holding the old one Work Order No
			String oldWorkOrderNoWhileRevise=jdbcTemplate.queryForObject("SELECT OLD_WORK_ORDER_NUMBER FROM QS_WORKORDER_TEMP_ISSUE WHERE PER_WORK_ORDER_NUMBER='"+workOrderNo+"'  and site_id='"+siteId+"' AND STATUS IN ('A','DF','M')", String.class);
			//String oldWorkOrderIssueId=jdbcTemplate.queryForObject("select QS_WORKORDER_ISSUE_ID from QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+oldWorkOrderWhileRevise+"' and SITE_ID='"+siteId+"'", String.class);

			query .append("SELECT  (QWAM.WO_WORK_AREA_ID),QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,(SELECT  LISTAGG(CONCAT(NVL(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')  WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) FROM QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT ")
					.append(" WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID  AND   QCBT.BILL_ID=QSINA.BILL_ID ")
					.append(" AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = '"+oldWorkOrderNoWhileRevise+"' AND SITE_ID='"+siteId+"' ) and QSINA.WO_WORK_AREA_ID=QSTA.WO_WORK_AREA_ID AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QSTA.OLD_WO_WORK_ISSUE_AREA_DTLS_ID  group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID) as paymentDone")
					.append(" ,QWAM.QS_BOQ_DETAILS_ID,QWAM.QS_AREA_PRICE_PER_UNIT,QWAM.QS_AREA_AMOUNT,QB.BOQ_NO,QSTA.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID,QSTA.OLD_WO_WORK_ISSUE_AREA_DTLS_ID,QSTA.AREA_ALOCATED_QTY ,QSTA.PRICE,TD.TOTAL_AMOUNT  ,TD.STATUS ,TD.WO_WORK_TEMP_ISSUE_DTLS_ID ,TD.QS_WO_TEMP_ISSUE_ID ,TD.WO_COMMENT,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,F.NAME AS FLOOR_NAME,FLAT.NAME AS FLATNAME,BLOCK.NAME AS BLOCK_NAME,   (CASE WHEN BLOCK.BLOCK_ID  IS NULL THEN '0'  ELSE BLOCK.BLOCK_ID END)AS BLOCK_ID,  (CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END)AS FLOOR_ID, (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END)AS  FLAT_ID,SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL ")
					.append(" ,(SELECT LISTAGG(RECORD_TYPE, '@@')  WITHIN GROUP (ORDER BY WO_WORK_DESC_ID)  FROM QS_BOQ_AREA_MAPPING QBAM1 WHERE QBAM1.SITE_ID= QWAM.SITE_ID AND QBAM1.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID   AND QBAM1.WO_WORK_AREA_GROUP_ID=QWAM.WO_WORK_AREA_GROUP_ID) AS WO_CONTAINS")
					.append(" FROM QS_BOQ_AREA_MAPPING QWAM LEFT OUTER JOIN  BLOCK BLOCK ON BLOCK.BLOCK_ID = QWAM.BLOCK_ID LEFT OUTER JOIN FLOOR F ON F.FLOOR_ID = QWAM.FLOOR_ID  LEFT OUTER JOIN FLAT FLAT ON FLAT.FLAT_ID = QWAM.FLAT_ID,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,SITE SI ,QS_BOQ QB  , 	QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QSTA,QS_WORKORDER_TEMP_ISSUE_DTLS TD")
					.append(" WHERE TD.WO_WORK_TEMP_ISSUE_DTLS_ID=QSTA.WO_WORK_TEMP_ISSUE_DTLS_ID AND QSTA.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID AND  QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID  AND SI.SITE_ID=QWAM.SITE_ID")
					.append(" AND QWAM.STATUS='A' AND QSTA.STATUS='A'   AND TD.WO_WORK_TEMP_ISSUE_DTLS_ID=?  AND QB.STATUS='A' AND QB.BOQ_SEQ_NO=QWAM.BOQ_SEQ_NO")
					.append(" AND TD.STATUS='A' and  QWW.WO_WORK_DESC_ID=? ")
					.append(" ORDER BY   QSTA.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID,(CASE WHEN BLOCK.BLOCK_ID  IS NULL THEN '0'  ELSE BLOCK.BLOCK_ID END),")
					.append(" (CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END),")
					.append(" (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END),QSTA.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID");
		}else{
			//this is query is for fresh work order,  this query is selecting work area details which are selected in while work order creation time 
			query.append("SELECT  (QWAM.WO_WORK_AREA_ID),QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,QWAM.QS_BOQ_DETAILS_ID,QWAM.QS_AREA_PRICE_PER_UNIT,QWAM.QS_AREA_AMOUNT,QB.BOQ_NO,QSTA.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID,QSTA.AREA_ALOCATED_QTY ,QSTA.PRICE,TD.TOTAL_AMOUNT  ,TD.STATUS ,TD.WO_WORK_TEMP_ISSUE_DTLS_ID ,TD.QS_WO_TEMP_ISSUE_ID ,TD.WO_COMMENT,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,F.NAME AS FLOOR_NAME,FLAT.NAME AS FLATNAME,BLOCK.NAME AS BLOCK_NAME,   (CASE WHEN BLOCK.BLOCK_ID  IS NULL THEN '0'  ELSE BLOCK.BLOCK_ID END)AS BLOCK_ID,  (CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END)AS FLOOR_ID, (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END)AS  FLAT_ID,SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL ")
					.append(" ,(SELECT LISTAGG(RECORD_TYPE, '@@')  WITHIN GROUP (ORDER BY WO_WORK_DESC_ID)  FROM QS_BOQ_AREA_MAPPING QBAM1 WHERE QBAM1.SITE_ID= QWAM.SITE_ID AND QBAM1.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID   AND QBAM1.WO_WORK_AREA_GROUP_ID=QWAM.WO_WORK_AREA_GROUP_ID) AS WO_CONTAINS")
					.append(" FROM QS_BOQ_AREA_MAPPING QWAM LEFT OUTER JOIN  BLOCK BLOCK ON BLOCK.BLOCK_ID = QWAM.BLOCK_ID LEFT OUTER JOIN FLOOR F ON F.FLOOR_ID = QWAM.FLOOR_ID  LEFT OUTER JOIN FLAT FLAT ON FLAT.FLAT_ID = QWAM.FLAT_ID,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,SITE SI ,QS_BOQ QB ,QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QSTA,QS_WORKORDER_TEMP_ISSUE_DTLS TD")
					.append(" WHERE TD.WO_WORK_TEMP_ISSUE_DTLS_ID=QSTA.WO_WORK_TEMP_ISSUE_DTLS_ID AND QSTA.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID AND  QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID  AND SI.SITE_ID=QWAM.SITE_ID")
					.append(" AND QWAM.STATUS='A' AND QSTA.STATUS='A'   AND TD.WO_WORK_TEMP_ISSUE_DTLS_ID=?  AND QB.STATUS='A' AND QB.BOQ_SEQ_NO=QWAM.BOQ_SEQ_NO")
					.append(" AND TD.STATUS='A' AND  QWW.WO_WORK_DESC_ID=? ")// and QWAM.WO_MEASURMENT_ID=?
					.append(" ORDER BY (CASE WHEN BLOCK.BLOCK_ID  IS NULL THEN '0'  ELSE BLOCK.BLOCK_ID END),")
					.append(" (CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END),")
					.append(" (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END),QSTA.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID");
		}
		
		List<Map<String, Object>> selectedWorkAreaDetailsList = jdbcTemplate.queryForList(query.toString(), workOrderIssueId,workDeskId);
		//if this request came from status page of work order then return the gain result back from here only
		if (operType != null)
			if (operType.equals("statusPage")) {
				return selectedWorkAreaDetailsList;
//====================================Return the response from here if request from status page=============================================					
		}
			
		List<String> areaId = new ArrayList<String>();
		//this loop is used to collecting all the WO_WORK_AREA_ID which are selected while creation of work order
		for (Map<String, Object> map : selectedWorkAreaDetailsList) {
			String temp = (String) map.get("WO_WORK_AREA_ID");
			areaId.add("'" + temp + "'");
		}
		//replacing the brackets of list type collection to use for ( IN ) operator in SQL
		String selectedWorkAreaId = areaId.toString().replace("[", "").replace("]", "");
		if (!selectedWorkAreaId.equals("")) {
			//this query is used to select all the non selected work area details for work order 
			loadUnSelectedWOAreaMapping.append("select  (QWAM.WO_WORK_AREA_ID),QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,QWAM.QS_BOQ_DETAILS_ID,QWAM.QS_AREA_PRICE_PER_UNIT,QWAM.QS_AREA_AMOUNT,QB.BOQ_NO,QWAM.WO_WORK_INITIATE_AREA,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME ")
					.append(" ,block.NAME as BLOCK_NAME ,F.NAME AS FLOOR_NAME,FLAT.name as FLATNAME,")
					
					.append(" (case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end)as BLOCK_ID,")
					.append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,")
					.append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID, ")
					
					.append(" SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL ")
					.append(" ,(select LISTAGG(RECORD_TYPE, '@@')  WITHIN GROUP (ORDER BY WO_WORK_DESC_ID)  from QS_BOQ_AREA_MAPPING QBAM1 where QBAM1.SITE_ID= QWAM.SITE_ID AND QBAM1.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID   AND QBAM1.WO_WORK_AREA_GROUP_ID=QWAM.WO_WORK_AREA_GROUP_ID) as WO_CONTAINS")
					.append(" from QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK block on block.BLOCK_ID = QWAM.BLOCK_ID ")
					.append(" left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID ")
					.append(" left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,SITE SI ,QS_BOQ QB ")
					.append(" where QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID AND SI.SITE_ID=QWAM.SITE_ID ");
					if(typeOfWork.equals("MATERIAL")){
						loadUnSelectedWOAreaMapping.append(" AND QWAM.RECORD_TYPE='"+typeOfWork+"'");
					}
					loadUnSelectedWOAreaMapping.append(" AND QWAM.site_id=?  and QB.site_id=? and QWAM.status='A' and QB.status='A' AND QB.BOQ_SEQ_NO=QWAM.BOQ_SEQ_NO ")
					.append(" and QWAM.WO_WORK_AREA_ID not in("+selectedWorkAreaId+") and QWW.WO_WORK_DESC_ID=? ")// and QWAM.WO_MEASURMENT_ID=?
				
					.append(" order by QWAM.WO_WORK_AREA_GROUP_ID,QWAM.RECORD_TYPE,(case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end),")
					.append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),")
					.append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");
			
			List<Map<String, Object>> nonSelectedWorkArea = jdbcTemplate.queryForList(loadUnSelectedWOAreaMapping.toString(), siteId,siteId,workDeskId);
		
			selectedWorkAreaDetailsList.addAll(nonSelectedWorkArea);	
		}
		return selectedWorkAreaDetailsList;
	}

	/**
	 * @description updateWorkOrderDetail method is used for mail approval 
	 * it will just update pending employee id and status
	 */
	@Override
	public int updateWorkOrderDetail(WorkOrderBean workOrderBean) {
		boolean isValid = checkWorkOrderNumberIsValidForEmployee(workOrderBean.getQS_Temp_Issue_Id(),
				workOrderBean.getUserId(), false,"A");
		if(!isValid){
			throw new RuntimeException("Work Order Number is Not Valid");
		}

		String updateWorkOrderApprovel = "update QS_WORKORDER_TEMP_ISSUE set PENDING_EMP_ID=?,STATUS=? where TEMP_WORK_ORDER_NUMBER=? and SITE_ID=?";
		Object queryForWOTempIssue[] = { workOrderBean.getApproverEmpId(),"A" , workOrderBean.getSiteWiseWONO() ,workOrderBean.getSiteId()};
		int result = jdbcTemplate.update(updateWorkOrderApprovel, queryForWOTempIssue);
		if (workOrderBean.getApproverEmpId().equals("END")) {
			//inserting temporary data into permanent table
			result=insertWorkOrderTemporaryDataToPermanentTable(workOrderBean,workOrderBean.getSiteWiseWONO(),workOrderBean.getQS_Temp_Issue_Id());
		}
		return result;
	}
	
	/**
	 * @description this method will execute if called from work order email controller
	 * and it will take work order area initiated quantity back 
	 */
	@Override
	public int rejectWorkOrderFromMail(WorkOrderBean workOrderBean) {
		//taking work order, work area allocated quantity back
		int result = takeTempWorkOrderQuantityBack(workOrderBean, workOrderBean.getQS_Temp_Issue_Id());
		String updateWorkOrderApprovel = "UPDATE QS_WORKORDER_TEMP_ISSUE SET STATUS=? WHERE TEMP_WORK_ORDER_NUMBER=? AND SITE_ID=?";
		Object queryForWOTempIssue[] = { "R", workOrderBean.getSiteWiseWONO(), workOrderBean.getSiteId() };
		result = jdbcTemplate.update(updateWorkOrderApprovel, queryForWOTempIssue);
		if (workOrderBean.getRevision().equals("0")) {//revision zero means this work order is not revised.
			result = insertIntoWOReservedNum(workOrderBean);
		}
		return result;
	}
	
	
	public int insertWorkOrderTemporaryDataToPermanentTable(WorkOrderBean workOrderBean,String workOrderNoSiteWise,String tempIssueId){
		String revisionNumber = workOrderBean.getRevision();
		int woIssueIDSequence = 0;
		String oldWorkOrderNoWhileRevise = "0";
		String oldWorkOrderIssueId = "";
		String finalWOnumber = "";
		String oldWorkOrderLaborAmount = "0";
		finalWOnumber = workOrderBean.getWorkOrderNo().trim();
		workOrderBean.setOldWorkOrderLaborAmount(oldWorkOrderLaborAmount);
	
		
		woIssueIDSequence = jdbcTemplate.queryForInt("select QS_WO_ISSUE_SEQ.NEXTVAL from dual");
		if (finalWOnumber.contains("/R") && !revisionNumber.equals("0")) {
			oldWorkOrderNoWhileRevise = jdbcTemplate.queryForObject( "SELECT OLD_WORK_ORDER_NUMBER FROM QS_WORKORDER_TEMP_ISSUE WHERE QS_WO_TEMP_ISSUE_ID=? ", new Object[] { tempIssueId }, String.class);
			oldWorkOrderIssueId = jdbcTemplate.queryForObject( "SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER=? and SITE_ID=? ", new Object[] { oldWorkOrderNoWhileRevise, workOrderBean.getSiteId() }, String.class);
			oldWorkOrderLaborAmount = jdbcTemplate.queryForObject( "SELECT TOTAL_LABOUR_AMOUNT FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER=? and SITE_ID=? ", new Object[] { oldWorkOrderNoWhileRevise, workOrderBean.getSiteId() }, String.class);
			workOrderBean.setOldWorkOrderLaborAmount(oldWorkOrderLaborAmount);
		}
		
		//query for insert data into Permanent table, get data from QS_WORKORDER_TEMP_ISSUE
		StringBuffer finalWorrkOrderIssue = new StringBuffer("INSERT INTO QS_WORKORDER_ISSUE ")
				.append(" (TOTAL_LABOUR_AMOUNT,TOTAL_MATERIAL_AMOUNT,WO_RECORD_CONTAINS,VERSION_NUMBER,REVISION,OLD_WORK_ORDER_NUMBER,WORK_ORDER_NAME,TOTAL_WO_AMOUNT,CONTRACTOR_ID,SITE_ID,CREATED_BY,QS_WORKORDER_DATE,CREATED_DATE,WORK_ORDER_NUMBER,STATUS,QS_WORKORDER_ISSUE_ID,QS_WORKORDER_TEMP_ISSUE_ID,BOQ_NO,TYPE_OF_WORK) ")
				.append(" SELECT  TOTAL_LABOUR_AMOUNT,TOTAL_MATERIAL_AMOUNT,WO_RECORD_CONTAINS,VERSION_NUMBER,REVISION,OLD_WORK_ORDER_NUMBER,WORK_ORDER_NAME,TOTAL_WO_AMOUNT,CONTRACTOR_ID,SITE_ID,CREATED_BY,WORK_ORDER_DATE,SYSDATE,?,'A',?,QS_WO_TEMP_ISSUE_ID,BOQ_NO,TYPE_OF_WORK FROM QS_WORKORDER_TEMP_ISSUE WHERE QS_WO_TEMP_ISSUE_ID=? AND STATUS='A'");
		int result = jdbcTemplate.update(finalWorrkOrderIssue.toString(), finalWOnumber, woIssueIDSequence,tempIssueId);
		//workOrderBean.setSiteWiseWONO(finalWOnumber);
		workOrderBean.setWorkOrderNo(finalWOnumber);
		
		List<Map<String, Object>> listOfTempIssueDetails = jdbcTemplate.queryForList("SELECT WO_WORK_TEMP_ISSUE_DTLS_ID FROM QS_WORKORDER_TEMP_ISSUE_DTLS WHERE QS_WO_TEMP_ISSUE_ID=? AND STATUS='A'",tempIssueId);
		
		for (Map<String, Object> tempIssueDetailsMap : listOfTempIssueDetails) {
			String tempIssueDeatailsId = tempIssueDetailsMap.get("WO_WORK_TEMP_ISSUE_DTLS_ID").toString();
			
		//query for insert data into Permanent table QS_WORKORDER_ISSUE_DETAILS from QS_WORKORDER_TEMP_ISSUE_DTLS
		//for (String tempIssueDeatailsId : qS_Temp_Issue_Dtls_Id) {
			int issueDetailSequence = jdbcTemplate.queryForInt("select QS_WO_ISSUE_DTLS_SEQ.NEXTVAL from dual");
			StringBuffer queryForQS_IssueDetials =new StringBuffer( "insert into QS_WORKORDER_ISSUE_DETAILS ")
					.append("(WO_WORK_ISSUE_DTLS_ID,WO_ROW_CODE,WO_WORK_DESC_ID,TOTAL_AMOUNT,CREATED_DATE,STATUS,WO_COMMENT ")
					.append(",WO_MANUAL_DESC,UOM,QUANTITY,QS_WORKORDER_ISSUE_ID,WO_MINORHEAD_ID,WO_MAJORHEAD_ID,RECORD_TYPE) ")
					.append(" select ?,WO_ROW_CODE,WO_WORK_DESC_ID,TOTAL_AMOUNT,sysdate,STATUS,WO_COMMENT,WO_MANUAL_DESC,UOM,QUANTITY,?,WO_MINORHEAD_ID,WO_MAJORHEAD_ID,RECORD_TYPE from QS_WORKORDER_TEMP_ISSUE_DTLS where WO_WORK_TEMP_ISSUE_DTLS_ID=? and STATUS='A'");
		
			result = jdbcTemplate.update(queryForQS_IssueDetials.toString(),issueDetailSequence,woIssueIDSequence,tempIssueDeatailsId);
		 
			if (result != 0) {
				List<Map<String, Object>> listOfTempAreaDetails=jdbcTemplate.queryForList("select OLD_WO_WORK_ISSUE_AREA_DTLS_ID,WO_WORK_TEMP_ISSUE_AREA_DTLS_ID from  QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS WHERE  WO_WORK_TEMP_ISSUE_DTLS_ID=?  and STATUS='A'",tempIssueDeatailsId);
				
				for (Map<String, Object> map : listOfTempAreaDetails) {
					String oldAreaDtlsID=map.get("OLD_WO_WORK_ISSUE_AREA_DTLS_ID")==null?"":map.get("OLD_WO_WORK_ISSUE_AREA_DTLS_ID").toString();
					String tempAreaDetlsId=map.get("WO_WORK_TEMP_ISSUE_AREA_DTLS_ID")==null?"":map.get("WO_WORK_TEMP_ISSUE_AREA_DTLS_ID").toString();
					String areaDetlsIdPK=jdbcTemplate.queryForObject("select QS_WO_ISSUE_AREA_DTLS_SEQ.NEXTVAL from dual", String.class);
					
					StringBuffer queryForWO_AreaDetails=new StringBuffer("insert into QS_WORKORDER_ISSUE_AREA_DETAILS(WO_WORK_ISSUE_AREA_DTLS_ID,BOQ_RATE,WO_WORK_AREA_ID, ")
							.append(" AREA_ALOCATED_QTY,PRICE,BASIC_AMOUNT,TOTAL_AMOUNT, CREATED_DATE,STATUS,WO_WORK_ISSUE_DTLS_ID,RECORD_TYPE) ")
							.append(" select ?,BOQ_RATE,WO_WORK_AREA_ID,AREA_ALOCATED_QTY,PRICE, ")
							.append(" BASIC_AMOUNT,TOTAL_AMOUNT,CREATED_DATE,STATUS,?,RECORD_TYPE from QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS where WO_WORK_TEMP_ISSUE_AREA_DTLS_ID=? and STATUS='A'");
					Object[] obj={areaDetlsIdPK,issueDetailSequence,tempAreaDetlsId};
					//woIssueIDSequence
					if(finalWOnumber.contains("/R")){
						result = jdbcTemplate.update("update QS_INV_AGN_AREA_PMT_DTL set WO_WORK_ISSUE_AREA_DTLS_ID=? where WO_WORK_ISSUE_AREA_DTLS_ID=? AND QS_WORKORDER_ISSUE_ID=? ",areaDetlsIdPK,oldAreaDtlsID,oldWorkOrderIssueId);
					}
					result = jdbcTemplate.update(queryForWO_AreaDetails.toString(),obj); 
					
					//inserting material BOQ details table data temporary to permanent 
					StringBuffer materialBOQProductDtls=new StringBuffer("insert into QS_WORKORDER_PRODUCT_DTLS(QS_WORKORDER_PRODUCT_DTLS_ID,")
							.append(" WO_WORK_ISSUE_AREA_DTLS_ID,MATERIAL_GROUP_ID,UOM,WORK_AREA,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,")
							.append(" TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS)")
							.append(" select QS_WORKORDER_PRODUCT_DTLS_SEQ.NEXTVAL,")
							.append(" ?,MATERIAL_GROUP_ID,UOM,WORK_AREA,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT, ")
							.append(" TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS ")
							.append(" from QS_WORKORDER_TEMP_PRODUCT_DTLS where WO_WORK_TEMP_ISSUE_AREA_DTLS_ID=? ");
					result = jdbcTemplate.update(materialBOQProductDtls.toString(),areaDetlsIdPK,tempAreaDetlsId); 
				}			
			}//result1 condition
		}
	
		//Inactive temporary Work Order
		result=jdbcTemplate.update("UPDATE QS_WORKORDER_TEMP_ISSUE SET  PENDING_EMP_ID='END',STATUS='I' WHERE QS_WO_TEMP_ISSUE_ID=?",tempIssueId);
		result=jdbcTemplate.update("UPDATE QS_WORKORDER_CRT_APPRL_DTLS SET WO_WORK_ISSUE_ID=? WHERE TEMP_WORK_ORDER_NUMBER=? AND SITE_ID=?",woIssueIDSequence,workOrderNoSiteWise,workOrderBean.getSiteId());
		
		//insert terms and condition from temporary table to permanent table
		String queryForTerms="INSERT INTO QS_WORKORDER_TERMS_COND(TERMS_CONDITION_ID,CONTRACTOR_ID,QS_WORKORDER_ISSUE_ID,TERMS_CONDITION_DESC,ENTRY_DATE) "
				+ " SELECT QS_WO_TERMS_CONDITIONS_SEQ.NEXTVAL,CONTRACTOR_ID,?,TERMS_CONDITION_DESC,ENTRY_DATE FROM QS_WORKORDER_TEMP_TERMS_COND WHERE QS_WO_TEMP_ISSUE_ID=?";
		result=jdbcTemplate.update(queryForTerms,woIssueIDSequence,tempIssueId);
	
		//Query for Taking Backup of Payment records while revise work order
		if(finalWOnumber.contains("/R")&&!revisionNumber.equals("0")){
			//get the old work order number  
			//this temporary work order is holding the old one Work Order No
			
			//In Active Bill's
			//result=jdbcTemplate.update("update QS_CONTRACTOR_BILL set STATUS='I' where QS_WORKORDER_ISSUE_ID="+oldWorkOrderIssueId+"");
			result=jdbcTemplate.update("update QS_CONTRACTOR_BILL_TEMP set STATUS='R' where QS_WORKORDER_ISSUE_ID=? and STATUS='T'",oldWorkOrderIssueId);
			
			//oldWorkOrderIssueId is the variable which is used to take back up bcoz this is stored in Payment table
			//taking backup of all the payments records while revise work order
			StringBuffer queryForBackupPaymentRecords = new StringBuffer("insert into ACC_CNT_ADVANCE_HISTORY_BACK_UP (CNT_ADV_HISTORY_BACK_UP_ID,CNT_ADVANCE_HISTORY_ID,WO_NUMBER,AMOUNT,ENTRY_DATE,ADVANCE_BILL_NO,RA_BILL_NO,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID)")
		 		.append(" select  CNT_ADV_HISTORY_BACK_UP_SEQ.nextval,CNT_ADVANCE_HISTORY_ID,WO_NUMBER,AMOUNT,ENTRY_DATE,ADVANCE_BILL_NO,RA_BILL_NO,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID ")
		 		.append(" from ACC_CNT_ADVANCE_HISTORY where  QS_WORKORDER_ISSUE_ID=?");

			result=jdbcTemplate.update(queryForBackupPaymentRecords.toString(),oldWorkOrderIssueId);
			
			queryForBackupPaymentRecords = new StringBuffer("insert into ACC_CNT_INVOICE_HISTORY_BACK_UP (CNT_INV_HISTORY_BACK_UP_ID,CNT_INVOICE_HISTORY_ID,WO_NUMBER,BILL_NO,BILL_AMOUNT,PAID_AMOUNT,ENTRY_DATE,PAYMENT_MODE,REF_NO,PAYMENT_TRANSACTIONS_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE)")
		 		.append(" select  CNT_INV_HISTORY_BACK_UP_SEQ.nextval,CNT_INVOICE_HISTORY_ID,WO_NUMBER,BILL_NO,BILL_AMOUNT,PAID_AMOUNT,ENTRY_DATE,PAYMENT_MODE,REF_NO,PAYMENT_TRANSACTIONS_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE")
		 		.append(" from ACC_CNT_INVOICE_HISTORY  where QS_WORKORDER_ISSUE_ID=?");
			result=jdbcTemplate.update(queryForBackupPaymentRecords.toString(),oldWorkOrderIssueId);
			
			queryForBackupPaymentRecords = new StringBuffer("insert into ACC_CNT_SEC_DEPOSIT_HISTORY_BACK_UP(CNT_SD_HISTORY_BACK_UP_ID,CNT_SEC_DEPOSIT_HISTORY_ID,SD_AMOUNT,CONTRACTOR_ID,RA_BILL_NO,ADV_PMT_BILL_NO,STATUS,WORK_ORDER_NO,SITE_ID,CREATION_DATE,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID)")
		 		.append(" select CNT_SD_HISTORY_BACK_UP_SEQ.nextval,CNT_SEC_DEPOSIT_HISTORY_ID,SD_AMOUNT,CONTRACTOR_ID,RA_BILL_NO,ADV_PMT_BILL_NO,STATUS,WORK_ORDER_NO,SITE_ID,CREATION_DATE,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID")
		 		.append(" from ACC_CNT_SEC_DEPOSIT_HISTORY where QS_WORKORDER_ISSUE_ID=?");

			result=jdbcTemplate.update(queryForBackupPaymentRecords.toString(),oldWorkOrderIssueId);
			
			queryForBackupPaymentRecords  = new StringBuffer("insert  into ACC_CNT_PAYMENT_BACK_UP(CREATED_DATE,CNT_PAYMENT_ID,PAYMENT_REQ_AMOUNT,WO_AMOUNT,WO_DATE,WO_NUMBER,REMARKS,SITE_ID,STATUS,CONTRACTOR_ID,BILL_ID,PAYMENT_TYPE,REQUEST_PENDING_DEPT_ID,REQUEST_PENDING_EMP_ID,SITEWISE_PAYMENT_NO,PARTICULAR,PAYMENT_REQ_DATE,QS_WORKORDER_ISSUE_ID,BILL_DATE,BILL_NUMBER,BILL_AMOUNT )")
					.append(" select CREATED_DATE,CNT_PAYMENT_ID,PAYMENT_REQ_AMOUNT,WO_AMOUNT,WO_DATE,WO_NUMBER,REMARKS,SITE_ID,STATUS,CONTRACTOR_ID,BILL_ID,PAYMENT_TYPE,REQUEST_PENDING_DEPT_ID,REQUEST_PENDING_EMP_ID,SITEWISE_PAYMENT_NO,PARTICULAR,PAYMENT_REQ_DATE,QS_WORKORDER_ISSUE_ID,BILL_DATE,BILL_NUMBER,BILL_AMOUNT")
					.append(" from ACC_CNT_PAYMENT where QS_WORKORDER_ISSUE_ID=?"); 
	 
			result=jdbcTemplate.update(queryForBackupPaymentRecords.toString(),oldWorkOrderIssueId);
			
			/*String queryForCheckingIsThisDuplicateRecordInBills="Select CONT_SEQ_BILL_NO from  QS_CONTRACTOR_BILL_BACK_UP where QS_WORKORDER_ISSUE_ID="+oldWorkOrderIssueId+"";
			List<Map<String, Object>> listOfBackRecords=jdbcTemplate.queryForList(queryForCheckingIsThisDuplicateRecordInBills);
			for (Map<String, Object> map : listOfBackRecords) {
			
			}*/
			//QS-CONTRACOTR_BILL BACKUP TABLE While revise work order
			queryForBackupPaymentRecords = new StringBuffer("insert into QS_CONTRACTOR_BILL_BACK_UP (BILL_ID,CONTRACTOR_ID,SITE_ID,PAYBLE_AMOUNT,STATUS,PENDING_EMP_ID,PENDING_DEPT_ID,PAYMENT_REQ_DATE, ")
					.append(" ENTRY_DATE,PAYMENT_TYPE,CERTIFIED_AMOUNT,DEDUCTION_AMOUNT,TEMP_BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_STATUS,CREATED_BY,DEDUCTION_REF_ID, ")
					.append(" REMARKS,BILL_NO,CONT_SEQ_BILL_NO,PAYMENT_TYPE_OF_WORK) ")
					.append(" select BILL_ID,CONTRACTOR_ID,SITE_ID,PAYBLE_AMOUNT,STATUS,PENDING_EMP_ID,PENDING_DEPT_ID,PAYMENT_REQ_DATE, ")
					.append(" ENTRY_DATE,PAYMENT_TYPE,CERTIFIED_AMOUNT,DEDUCTION_AMOUNT,TEMP_BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_STATUS,CREATED_BY,DEDUCTION_REF_ID, ")
					.append(" REMARKS,BILL_NO,CONT_SEQ_BILL_NO,PAYMENT_TYPE_OF_WORK from QS_CONTRACTOR_BILL where QS_WORKORDER_ISSUE_ID=?"); 
			
			result=jdbcTemplate.update(queryForBackupPaymentRecords.toString(),oldWorkOrderIssueId);
			
			
			//Updating all Payment table records with Revised Work Order Number and also Updating with Work Order Issue ID
			String queryForUpdatePaymentRecords ="UPDATE ACC_CNT_ADVANCE_HISTORY set  WO_NUMBER=?,  QS_WORKORDER_ISSUE_ID=? where   QS_WORKORDER_ISSUE_ID=? ";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,finalWOnumber,woIssueIDSequence,oldWorkOrderIssueId);
			
			queryForUpdatePaymentRecords ="UPDATE ACC_CNT_INVOICE_HISTORY set  WO_NUMBER=?,  QS_WORKORDER_ISSUE_ID=?  where   QS_WORKORDER_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,finalWOnumber,woIssueIDSequence,oldWorkOrderIssueId);
			queryForUpdatePaymentRecords ="UPDATE ACC_CNT_SEC_DEPOSIT_HISTORY set  WORK_ORDER_NO=?, QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,finalWOnumber,woIssueIDSequence,oldWorkOrderIssueId);
			//indent entry
			queryForUpdatePaymentRecords ="UPDATE INDENT_ENTRY set  WORK_ORDER_NUMBER=? where WORK_ORDER_NUMBER=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,finalWOnumber,oldWorkOrderNoWhileRevise);
			
		
				
			queryForUpdatePaymentRecords ="UPDATE ACC_CNT_PAYMENT set   WO_AMOUNT=(select TOTAL_WO_AMOUNT from QS_WORKORDER_ISSUE where QS_WORKORDER_ISSUE_ID=?) ,WO_NUMBER=?, QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,finalWOnumber,woIssueIDSequence,oldWorkOrderIssueId);
			
			queryForUpdatePaymentRecords ="UPDATE ACC_CNT_ADV_DETAILS set   WO_AMOUNT=(select TOTAL_WO_AMOUNT from QS_WORKORDER_ISSUE where QS_WORKORDER_ISSUE_ID=?) ,WO_NUMBER=?, QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,finalWOnumber,woIssueIDSequence,oldWorkOrderIssueId);
			
			
			//Updating QS_WORKORDER_ISSUE_ID with new QS_WORKORDER_ISSUE_ID while Revise Work Order
			queryForUpdatePaymentRecords ="UPDATE QS_CONTRACTOR_BILL set  QS_WORKORDER_ISSUE_ID=?  where  QS_WORKORDER_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
			
			queryForUpdatePaymentRecords ="UPDATE QS_INV_AGN_AREA_PMT_DTL set  QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
			
			queryForUpdatePaymentRecords ="UPDATE QS_BILL_DEDUCTION_DTLS set  QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
			
			queryForUpdatePaymentRecords ="UPDATE QS_BILL_RECOVERY set  QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
			
			queryForUpdatePaymentRecords ="UPDATE QS_BILL_RECOVERY_HISTORY set  QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
			
			queryForUpdatePaymentRecords ="UPDATE QS_INV_AGN_WORK_PMT_DTL_HR set  QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);

	
	}
		return result;
}
	
	
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used to update the next employee and also copy data from temporary table's to permanent table's
	 *  @param workOrderBean object is holding the data which are going to process from temporary to permanent table
	 *  @param tempIssueId this number unique so we are using this number to select data or copy the data from one table to another table
	 *  @param workOrderNoSiteWise this is temporary work order number which is stored in approve and reject details table
	 *  @param nextApprovelEmpId this variable is holding the next level approver employee id if this variable contains "END" means there is not next level approval id, so insert temporary table data in permanent table
	 *  @param insertType is the variable which is telling the record should copy from temporary table to permanent  with combination of nextApprovelEmpId
	 *  @param qS_Temp_Issue_Dtls_Id this array holding all the QS_WORKORDER_TEMP_ISSUE_DTLS table row wise unique id's
	 * 	@return	 result of the insert or update operation
	 */
	@Override
	public int updatePendingWorkOrder(WorkOrderBean workOrderBean, String tempIssueId, String workOrderNoSiteWise,String nextApprovelEmpId, String insertType
				) {
		// if the next approval is end then store directly in Permanent Table
		//String oldWorkOrderWhileRevise="";
		//String oldWorkOrderIssueId="0";
		String workOrderStatus="";
		String updateWorkOrderApprovel = "update QS_WORKORDER_TEMP_ISSUE set PENDING_EMP_ID=?,TOTAL_WO_AMOUNT=?,WORK_ORDER_NAME=?,TOTAL_LABOUR_AMOUNT=?,TOTAL_MATERIAL_AMOUNT=?,STATUS=? where TEMP_WORK_ORDER_NUMBER=? and SITE_ID=?";
		String revisionNumber=workOrderBean.getRevision();
		if (nextApprovelEmpId.equals("END") && insertType.equals("insert")) {
			boolean isValid = checkWorkOrderNumberIsValidForEmployee(workOrderBean.getQS_Temp_Issue_Id(),
					workOrderBean.getUserId(), false, "A");
			if(!isValid){
				throw new RuntimeException("Work Order Number is Not Valid");
			}
			//int woIssueIDSequence=jdbcTemplate.queryForInt("select QS_WO_ISSUE_SEQ.NEXTVAL from dual");
			//String finalWOnumber=workOrderBean.getWorkOrderNo().trim();
			/*if(finalWOnumber.split("/")[finalWOnumber.split("/").length-1].contains("R")){
				 oldWorkOrderWhileRevise=jdbcTemplate.queryForObject("select OLD_WORK_ORDER_NUMBER from QS_WORKORDER_TEMP_ISSUE where QS_WO_TEMP_ISSUE_ID="+tempIssueId+"", String.class);
				 oldWorkOrderIssueId=jdbcTemplate.queryForObject("select QS_WORKORDER_ISSUE_ID from QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+oldWorkOrderWhileRevise+"' and SITE_ID='"+workOrderBean.getSiteId()+"'", String.class);
			}*/
			//updating the temporary work order data 
			Object queryForWOTempIssue[] = { nextApprovelEmpId,workOrderBean.getTotalWoAmount(),workOrderBean.getWorkOrderName(),workOrderBean.getLaborWoAmount(),workOrderBean.getMaterialWoAmount(),"A" , workOrderNoSiteWise ,workOrderBean.getSiteId()};
			int result = jdbcTemplate.update(updateWorkOrderApprovel, queryForWOTempIssue);
			
			//inserting temporary table data to permanent table
			result=insertWorkOrderTemporaryDataToPermanentTable(workOrderBean,workOrderNoSiteWise,tempIssueId);
			
/*			//query for insert data into Permanent table, get data from QS_WORKORDER_TEMP_ISSUE
			StringBuffer finalWorrkOrderIssue = new StringBuffer("insert into QS_WORKORDER_ISSUE ")
					.append(" (TOTAL_LABOUR_AMOUNT,TOTAL_MATERIAL_AMOUNT,WO_RECORD_CONTAINS,VERSION_NUMBER,REVISION,OLD_WORK_ORDER_NUMBER,WORK_ORDER_NAME,TOTAL_WO_AMOUNT,CONTRACTOR_ID,SITE_ID,CREATED_BY,QS_WORKORDER_DATE,CREATED_DATE,WORK_ORDER_NUMBER,STATUS,QS_WORKORDER_ISSUE_ID,QS_WORKORDER_TEMP_ISSUE_ID,BOQ_NO,TYPE_OF_WORK) ")
					.append(" select  TOTAL_LABOUR_AMOUNT,TOTAL_MATERIAL_AMOUNT,WO_RECORD_CONTAINS,VERSION_NUMBER,REVISION,OLD_WORK_ORDER_NUMBER,WORK_ORDER_NAME,?,CONTRACTOR_ID,SITE_ID,CREATED_BY,WORK_ORDER_DATE,sysdate,?,'A',?,QS_WO_TEMP_ISSUE_ID,BOQ_NO,TYPE_OF_WORK from QS_WORKORDER_TEMP_ISSUE where QS_WO_TEMP_ISSUE_ID=? and STATUS='A'");
			 result = jdbcTemplate.update(finalWorrkOrderIssue.toString(),workOrderBean.getTotalWoAmount(),finalWOnumber,woIssueIDSequence, tempIssueId);
			workOrderBean.setSiteWiseWONO(finalWOnumber);
			workOrderBean.setWorkOrderNo(finalWOnumber);
			
			List<Map<String, Object>> listOfTempIssueDetails=jdbcTemplate.queryForList("select WO_WORK_TEMP_ISSUE_DTLS_ID from QS_WORKORDER_TEMP_ISSUE_DTLS where QS_WO_TEMP_ISSUE_ID=? AND STATUS='A'",tempIssueId);
			
			for ( Map<String, Object> tempIssueDetailsMap: listOfTempIssueDetails) {
				String tempIssueDeatailsId=tempIssueDetailsMap.get("WO_WORK_TEMP_ISSUE_DTLS_ID").toString();
				
			//query for insert data into Permanent table QS_WORKORDER_ISSUE_DETAILS from QS_WORKORDER_TEMP_ISSUE_DTLS
			//for (String tempIssueDeatailsId : qS_Temp_Issue_Dtls_Id) {
				int issueDetailSequence=jdbcTemplate.queryForInt("select QS_WO_ISSUE_DTLS_SEQ.NEXTVAL from dual");
				StringBuffer queryForQS_IssueDetials =new StringBuffer( "insert into QS_WORKORDER_ISSUE_DETAILS ")
						.append("(WO_WORK_ISSUE_DTLS_ID,WO_ROW_CODE,WO_WORK_DESC_ID,TOTAL_AMOUNT,CREATED_DATE,STATUS,WO_COMMENT ")
						.append(",WO_MANUAL_DESC,UOM,QUANTITY,QS_WORKORDER_ISSUE_ID,WO_MINORHEAD_ID,WO_MAJORHEAD_ID,RECORD_TYPE) ")
						.append(" select ?,WO_ROW_CODE,WO_WORK_DESC_ID,TOTAL_AMOUNT,sysdate,STATUS,WO_COMMENT,WO_MANUAL_DESC,UOM,QUANTITY,?,WO_MINORHEAD_ID,WO_MAJORHEAD_ID,RECORD_TYPE from QS_WORKORDER_TEMP_ISSUE_DTLS where WO_WORK_TEMP_ISSUE_DTLS_ID=? and STATUS='A'");
			
				result = jdbcTemplate.update(queryForQS_IssueDetials.toString(),issueDetailSequence,woIssueIDSequence,tempIssueDeatailsId);
			 
				if(result!=0){
					List<Map<String, Object>> listOfTempAreaDetails=jdbcTemplate.queryForList("select OLD_WO_WORK_ISSUE_AREA_DTLS_ID,WO_WORK_TEMP_ISSUE_AREA_DTLS_ID from  QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS WHERE  WO_WORK_TEMP_ISSUE_DTLS_ID=?  and STATUS='A'",tempIssueDeatailsId);
					
					for (Map<String, Object> map : listOfTempAreaDetails) {
						String oldAreaDtlsID=map.get("OLD_WO_WORK_ISSUE_AREA_DTLS_ID")==null?"":map.get("OLD_WO_WORK_ISSUE_AREA_DTLS_ID").toString();
						String tempAreaDetlsId=map.get("WO_WORK_TEMP_ISSUE_AREA_DTLS_ID")==null?"":map.get("WO_WORK_TEMP_ISSUE_AREA_DTLS_ID").toString();
						String areaDetlsIdPK=jdbcTemplate.queryForObject("select QS_WO_ISSUE_AREA_DTLS_SEQ.NEXTVAL from dual", String.class);
						
						StringBuffer queryForWO_AreaDetails=new StringBuffer("insert into QS_WORKORDER_ISSUE_AREA_DETAILS(WO_WORK_ISSUE_AREA_DTLS_ID,BOQ_RATE,WO_WORK_AREA_ID, ")
								.append(" AREA_ALOCATED_QTY,PRICE,BASIC_AMOUNT,TOTAL_AMOUNT, CREATED_DATE,STATUS,WO_WORK_ISSUE_DTLS_ID,RECORD_TYPE) ")
								.append(" select ?,BOQ_RATE,WO_WORK_AREA_ID,AREA_ALOCATED_QTY,PRICE, ")
								.append(" BASIC_AMOUNT,TOTAL_AMOUNT,CREATED_DATE,STATUS,?,RECORD_TYPE from QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS where WO_WORK_TEMP_ISSUE_AREA_DTLS_ID=? and STATUS='A'");
						Object[] obj={areaDetlsIdPK,issueDetailSequence,tempAreaDetlsId};
						//woIssueIDSequence
						if(finalWOnumber.split("/")[finalWOnumber.split("/").length-1].contains("R")){
							result = jdbcTemplate.update("update QS_INV_AGN_AREA_PMT_DTL set WO_WORK_ISSUE_AREA_DTLS_ID=? where WO_WORK_ISSUE_AREA_DTLS_ID=? AND QS_WORKORDER_ISSUE_ID=? ",areaDetlsIdPK,oldAreaDtlsID,oldWorkOrderIssueId);
						}
						result = jdbcTemplate.update(queryForWO_AreaDetails.toString(),obj); 
						
						//inserting material BOQ details table data temporary to permanent 
						StringBuffer materialBOQProductDtls=new StringBuffer("insert into QS_WORKORDER_PRODUCT_DTLS(QS_WORKORDER_PRODUCT_DTLS_ID,")
								.append(" WO_WORK_ISSUE_AREA_DTLS_ID,MATERIAL_GROUP_ID,UOM,WORK_AREA,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,")
								.append(" TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS)")
								.append(" select QS_WORKORDER_PRODUCT_DTLS_SEQ.NEXTVAL,")
								.append(" ?,MATERIAL_GROUP_ID,UOM,WORK_AREA,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT, ")
								.append(" TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS ")
								.append(" from QS_WORKORDER_TEMP_PRODUCT_DTLS where WO_WORK_TEMP_ISSUE_AREA_DTLS_ID=? ");
						result = jdbcTemplate.update(materialBOQProductDtls.toString(),areaDetlsIdPK,tempAreaDetlsId); 
					}			
				}//result1 condition
			}
		
			//Inactive temporary Work Order
			result=jdbcTemplate.update("update QS_WORKORDER_TEMP_ISSUE set STATUS='I' where QS_WO_TEMP_ISSUE_ID=?",tempIssueId);
			result=jdbcTemplate.update("update QS_WORKORDER_CRT_APPRL_DTLS set WO_WORK_ISSUE_ID=? where TEMP_WORK_ORDER_NUMBER=? and SITE_ID=?",woIssueIDSequence,workOrderNoSiteWise,workOrderBean.getSiteId());
			
			//insert terms and condition from temporary table to permanent table
			String queryForTerms="insert into QS_WORKORDER_TERMS_COND(TERMS_CONDITION_ID,CONTRACTOR_ID,QS_WORKORDER_ISSUE_ID,TERMS_CONDITION_DESC,ENTRY_DATE) "
					+ " select QS_WO_TERMS_CONDITIONS_SEQ.NEXTVAL,CONTRACTOR_ID,?,TERMS_CONDITION_DESC,ENTRY_DATE FROM QS_WORKORDER_TEMP_TERMS_COND where QS_WO_TEMP_ISSUE_ID=?";
			result=jdbcTemplate.update(queryForTerms,woIssueIDSequence,tempIssueId);
		
			//Query for Taking Backup of Payment records while revise work order
			if(finalWOnumber.split("/")[finalWOnumber.split("/").length-1].contains("R")&&!revisionNumber.equals("0")){
				//get the old work order number  
				//this temporary work order is holding the old one Work Order No
				
				//In Active Bill's
				//result=jdbcTemplate.update("update QS_CONTRACTOR_BILL set STATUS='I' where QS_WORKORDER_ISSUE_ID="+oldWorkOrderIssueId+"");
				result=jdbcTemplate.update("update QS_CONTRACTOR_BILL_TEMP set STATUS='R' where QS_WORKORDER_ISSUE_ID=? and STATUS='T'",oldWorkOrderIssueId);
				
				//oldWorkOrderIssueId is the variable which is used to take back up bcoz this is stored in Payment table
				//taking backup of all the payments records while revise work order
				StringBuffer queryForBackupPaymentRecords = new StringBuffer("insert into ACC_CNT_ADVANCE_HISTORY_BACK_UP (CNT_ADV_HISTORY_BACK_UP_ID,CNT_ADVANCE_HISTORY_ID,WO_NUMBER,AMOUNT,ENTRY_DATE,ADVANCE_BILL_NO,RA_BILL_NO,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID)")
			 		.append(" select  CNT_ADV_HISTORY_BACK_UP_SEQ.nextval,CNT_ADVANCE_HISTORY_ID,WO_NUMBER,AMOUNT,ENTRY_DATE,ADVANCE_BILL_NO,RA_BILL_NO,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID ")
			 		.append(" from ACC_CNT_ADVANCE_HISTORY where  QS_WORKORDER_ISSUE_ID=?");
	
				result=jdbcTemplate.update(queryForBackupPaymentRecords.toString(),oldWorkOrderIssueId);
				
				queryForBackupPaymentRecords = new StringBuffer("insert into ACC_CNT_INVOICE_HISTORY_BACK_UP (CNT_INV_HISTORY_BACK_UP_ID,CNT_INVOICE_HISTORY_ID,WO_NUMBER,BILL_NO,BILL_AMOUNT,PAID_AMOUNT,ENTRY_DATE,PAYMENT_MODE,REF_NO,PAYMENT_TRANSACTIONS_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE)")
			 		.append(" select  CNT_INV_HISTORY_BACK_UP_SEQ.nextval,CNT_INVOICE_HISTORY_ID,WO_NUMBER,BILL_NO,BILL_AMOUNT,PAID_AMOUNT,ENTRY_DATE,PAYMENT_MODE,REF_NO,PAYMENT_TRANSACTIONS_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE")
			 		.append(" from ACC_CNT_INVOICE_HISTORY  where QS_WORKORDER_ISSUE_ID=?");
				result=jdbcTemplate.update(queryForBackupPaymentRecords.toString(),oldWorkOrderIssueId);
				
				queryForBackupPaymentRecords = new StringBuffer("insert into ACC_CNT_SEC_DEPOSIT_HISTORY_BACK_UP(CNT_SD_HISTORY_BACK_UP_ID,CNT_SEC_DEPOSIT_HISTORY_ID,SD_AMOUNT,CONTRACTOR_ID,RA_BILL_NO,ADV_PMT_BILL_NO,STATUS,WORK_ORDER_NO,SITE_ID,CREATION_DATE,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID)")
			 		.append(" select CNT_SD_HISTORY_BACK_UP_SEQ.nextval,CNT_SEC_DEPOSIT_HISTORY_ID,SD_AMOUNT,CONTRACTOR_ID,RA_BILL_NO,ADV_PMT_BILL_NO,STATUS,WORK_ORDER_NO,SITE_ID,CREATION_DATE,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID")
			 		.append(" from ACC_CNT_SEC_DEPOSIT_HISTORY where QS_WORKORDER_ISSUE_ID=?");
	
				result=jdbcTemplate.update(queryForBackupPaymentRecords.toString(),oldWorkOrderIssueId);
				
				queryForBackupPaymentRecords  = new StringBuffer("insert  into ACC_CNT_PAYMENT_BACK_UP(CREATED_DATE,CNT_PAYMENT_ID,PAYMENT_REQ_AMOUNT,WO_AMOUNT,WO_DATE,WO_NUMBER,REMARKS,SITE_ID,STATUS,CONTRACTOR_ID,BILL_ID,PAYMENT_TYPE,REQUEST_PENDING_DEPT_ID,REQUEST_PENDING_EMP_ID,SITEWISE_PAYMENT_NO,PARTICULAR,PAYMENT_REQ_DATE,QS_WORKORDER_ISSUE_ID,BILL_DATE,BILL_NUMBER,BILL_AMOUNT )")
						.append(" select CREATED_DATE,CNT_PAYMENT_ID,PAYMENT_REQ_AMOUNT,WO_AMOUNT,WO_DATE,WO_NUMBER,REMARKS,SITE_ID,STATUS,CONTRACTOR_ID,BILL_ID,PAYMENT_TYPE,REQUEST_PENDING_DEPT_ID,REQUEST_PENDING_EMP_ID,SITEWISE_PAYMENT_NO,PARTICULAR,PAYMENT_REQ_DATE,QS_WORKORDER_ISSUE_ID,BILL_DATE,BILL_NUMBER,BILL_AMOUNT")
						.append(" from ACC_CNT_PAYMENT where QS_WORKORDER_ISSUE_ID=?"); 
		 
				result=jdbcTemplate.update(queryForBackupPaymentRecords.toString(),oldWorkOrderIssueId);
				
				String queryForCheckingIsThisDuplicateRecordInBills="Select CONT_SEQ_BILL_NO from  QS_CONTRACTOR_BILL_BACK_UP where QS_WORKORDER_ISSUE_ID="+oldWorkOrderIssueId+"";
				List<Map<String, Object>> listOfBackRecords=jdbcTemplate.queryForList(queryForCheckingIsThisDuplicateRecordInBills);
				for (Map<String, Object> map : listOfBackRecords) {
				
				}
				//QS-CONTRACOTR_BILL BACKUP TABLE While revise work order
				queryForBackupPaymentRecords = new StringBuffer("insert into QS_CONTRACTOR_BILL_BACK_UP (BILL_ID,CONTRACTOR_ID,SITE_ID,PAYBLE_AMOUNT,STATUS,PENDING_EMP_ID,PENDING_DEPT_ID,PAYMENT_REQ_DATE, ")
						.append(" ENTRY_DATE,PAYMENT_TYPE,CERTIFIED_AMOUNT,DEDUCTION_AMOUNT,TEMP_BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_STATUS,CREATED_BY,DEDUCTION_REF_ID, ")
						.append(" REMARKS,BILL_NO,CONT_SEQ_BILL_NO,PAYMENT_TYPE_OF_WORK) ")
						.append(" select BILL_ID,CONTRACTOR_ID,SITE_ID,PAYBLE_AMOUNT,STATUS,PENDING_EMP_ID,PENDING_DEPT_ID,PAYMENT_REQ_DATE, ")
						.append(" ENTRY_DATE,PAYMENT_TYPE,CERTIFIED_AMOUNT,DEDUCTION_AMOUNT,TEMP_BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_STATUS,CREATED_BY,DEDUCTION_REF_ID, ")
						.append(" REMARKS,BILL_NO,CONT_SEQ_BILL_NO,PAYMENT_TYPE_OF_WORK from QS_CONTRACTOR_BILL where QS_WORKORDER_ISSUE_ID=?"); 
				
				result=jdbcTemplate.update(queryForBackupPaymentRecords.toString(),oldWorkOrderIssueId);
				
				
				//Updating all Payment table records with Revised Work Order Number and also Updating with Work Order Issue ID
				String queryForUpdatePaymentRecords ="UPDATE ACC_CNT_ADVANCE_HISTORY set  WO_NUMBER=?,  QS_WORKORDER_ISSUE_ID=? where   QS_WORKORDER_ISSUE_ID=? ";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,finalWOnumber,woIssueIDSequence,oldWorkOrderIssueId);
				
				queryForUpdatePaymentRecords ="UPDATE ACC_CNT_INVOICE_HISTORY set  WO_NUMBER=?,  QS_WORKORDER_ISSUE_ID=?  where   QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,finalWOnumber,woIssueIDSequence,oldWorkOrderIssueId);
				queryForUpdatePaymentRecords ="UPDATE ACC_CNT_SEC_DEPOSIT_HISTORY set  WORK_ORDER_NO=?, QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,finalWOnumber,woIssueIDSequence,oldWorkOrderIssueId);
				//indent entry
				queryForUpdatePaymentRecords ="UPDATE INDENT_ENTRY set  WORK_ORDER_NUMBER=? where WORK_ORDER_NUMBER=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,finalWOnumber,oldWorkOrderWhileRevise);
				
			
					
				queryForUpdatePaymentRecords ="UPDATE ACC_CNT_PAYMENT set   WO_AMOUNT=(select TOTAL_WO_AMOUNT from QS_WORKORDER_ISSUE where QS_WORKORDER_ISSUE_ID=?) ,WO_NUMBER=?, QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,finalWOnumber,woIssueIDSequence,oldWorkOrderIssueId);
				
				queryForUpdatePaymentRecords ="UPDATE ACC_CNT_ADV_DETAILS set   WO_AMOUNT=(select TOTAL_WO_AMOUNT from QS_WORKORDER_ISSUE where QS_WORKORDER_ISSUE_ID=?) ,WO_NUMBER=?, QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,finalWOnumber,woIssueIDSequence,oldWorkOrderIssueId);
				
				
				//Updating QS_WORKORDER_ISSUE_ID with new QS_WORKORDER_ISSUE_ID while Revise Work Order
				queryForUpdatePaymentRecords ="UPDATE QS_CONTRACTOR_BILL set  QS_WORKORDER_ISSUE_ID=?  where  QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
				
				queryForUpdatePaymentRecords ="UPDATE QS_INV_AGN_AREA_PMT_DTL set  QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
				
				queryForUpdatePaymentRecords ="UPDATE QS_BILL_DEDUCTION_DTLS set  QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
				
				queryForUpdatePaymentRecords ="UPDATE QS_BILL_RECOVERY set  QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
				
				queryForUpdatePaymentRecords ="UPDATE QS_BILL_RECOVERY_HISTORY set  QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
				
				queryForUpdatePaymentRecords ="UPDATE QS_INV_AGN_WORK_PMT_DTL_HR set  QS_WORKORDER_ISSUE_ID=? where  QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.update(queryForUpdatePaymentRecords,woIssueIDSequence,oldWorkOrderIssueId);
				
			
			//	String s=null;s.trim();
			}
*/		return result;
		} else {
			if(workOrderBean.getIsSaveOrUpdateOperation().equals("Draft Work Order")){
				nextApprovelEmpId=workOrderBean.getUserId();
				workOrderStatus="DF";
			}else if(workOrderBean.getIsSaveOrUpdateOperation().equals("Submit")){
				workOrderStatus="A";
			}else{
				workOrderStatus="A";
			}
			//if nextApprovelEmpId not contains "END" String
			Object queryForWOTempIssue[] = { nextApprovelEmpId,workOrderBean.getTotalWoAmount(),workOrderBean.getWorkOrderName(),workOrderBean.getLaborWoAmount(),workOrderBean.getMaterialWoAmount() ,workOrderStatus, workOrderNoSiteWise ,workOrderBean.getSiteId()};
			int result = jdbcTemplate.update(updateWorkOrderApprovel, queryForWOTempIssue);
			return result;
		}
	}
	/**
	 *  @author Aniket Chavan
	 *  @description this method used to update the work order amount
	 * 	@param workOrderBean object holding all the data
	 * 	@return	 result of the insert or update operation
	 */
	@Override
	public int updatePermanentWorkOrder(WorkOrderBean workOrderBean, String tempIssueId, String workOrderNoSiteWise,
			String nextApprovelEmpId, String string) {
		String updateWorkOrderApprovel = "update QS_WORKORDER_ISSUE set TOTAL_WO_AMOUNT=? where WORK_ORDER_NUMBER=?";
		Object obj[] = { workOrderBean.getTotalWoAmount(),workOrderNoSiteWise};
		int result = jdbcTemplate.update(updateWorkOrderApprovel, obj);
		return result;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used to update comments of work order rows
	 * 	@param tempIssueId is holding the unique value using this we can update the comments
	 * 	@param actualComments is holding the value of current comment and previous comment and giving here to update
	 * 	@return	 result of the insert or update operation
	 */
	@Override
	public int updateWorkOrderComments(String tempIssueId, String actualComments, String changedAcceptedRate) {
		String query = "update QS_WORKORDER_TEMP_ISSUE_DTLS set WO_COMMENT = ? where WO_WORK_TEMP_ISSUE_DTLS_ID = ? ";
		int result = jdbcTemplate.update(query, new Object[] { actualComments, tempIssueId });
		return result;
	}
	
	/**
	 * @description this method is for updating scope of the work
	 */
	@Override
	public int updateWorkOrderRowScope(String tempIssueId, String woManualDesc) {
		String query = "update QS_WORKORDER_TEMP_ISSUE_DTLS set WO_MANUAL_DESC = ? where WO_WORK_TEMP_ISSUE_DTLS_ID = ? ";
		int result = jdbcTemplate.update(query, new Object[] { woManualDesc, tempIssueId });
		return result;
	}

	/**
	 *  @author Aniket Chavan
	 *  @description this method used to update comments of work order rows
	 * 	@param qsIssueId is holding the unique value using this we can update the comments
	 * 	@param actualComments is holding the value of current comment and previous comment and giving here to update
	 * 	@return	 result of the insert or update operation
	 */
	@Override
	public int updatePermanentWorkOrderComments(String qsIssueId, String actualComments,
			String changedAcceptedRate) {
		String query = "update QS_WORKORDER_ISSUE_DETAILS set WO_COMMENT = ? where WO_WORK_ISSUE_DTLS_ID = ? ";
		int result = jdbcTemplate.update(query, new Object[] { actualComments, qsIssueId });
		return result;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used to update comments of work order rows
	 *  @param changedWorkOderDetail holding the current data of work area details after changing the work order work area details
	 *  @param actualWorkOderDetail holding the actual data of work area details before changing the details
	 *  @return	 result of the insert or update operation
	 */
	@Override
	public int updateWorkOrderAreaDetails(WorkOrderBean changedWorkOderDetail, WorkOrderBean actualWorkOderDetail) {
		String query = "UPDATE QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS set PRICE=?,BASIC_AMOUNT=?,TOTAL_AMOUNT=?, AREA_ALOCATED_QTY = ?,WO_WORK_AREA_ID=? WHERE WO_WORK_TEMP_ISSUE_AREA_DTLS_ID = ?";
		double amount=Double.valueOf(changedWorkOderDetail.getSelectedArea())*Double.valueOf(changedWorkOderDetail.getAcceptedRate1());
		Object[] params={ changedWorkOderDetail.getAcceptedRate1(),amount,amount,
				changedWorkOderDetail.getSelectedArea(), changedWorkOderDetail.getWorkAreaId(),
				changedWorkOderDetail.getQS_Temp_Issue_Dtls_Id()};
		return jdbcTemplate.update(query,params);
	}

	/**
	 *  @author Aniket Chavan
	 *  @description this method used to the permanent work order area detail's 
	 *  @param changedWorkOderDetail holding the current data of work area details after changing the work order work area details
	 *  @param actualWorkOderDetail holding the actual data of work area details before changing the details
	 *  @return	 result of the insert or update operation
	 */
	@Override
	public int updatePermanentWorkOrderCreationDetails(WorkOrderBean changedWorkOderDetail,
			WorkOrderBean actualWorkOderDetail) {
		String query = "UPDATE QS_WORKORDER_ISSUE_AREA_DETAILS set PRICE=?,BASIC_AMOUNT=?, AREA_ALOCATED_QTY = ?,WO_WORK_AREA_ID=? WHERE WO_WORK_ISSUE_AREA_DTLS_ID = ?";
		Object[] params={ changedWorkOderDetail.getAcceptedRate1(),changedWorkOderDetail.getTotalAmount1(),
				changedWorkOderDetail.getSelectedArea(), changedWorkOderDetail.getWorkAreaId(),
				changedWorkOderDetail.getQS_Temp_Issue_Dtls_Id()};
		return jdbcTemplate.update(query,params);
		
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used to the permanent work order area detail's 
	 *  @param tmpIssueAreaDtlsId this is unique of WO_WORK_TEMP_ISSUE_AREA_DTLS_ID table updating the details of work area
	 *  @param changedAcceptedRate is holding the current accepted rate for QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS table this rate is updating by WO_WORK_TEMP_ISSUE_AREA_DTLS_ID
	 *  @param tempIssueId this is unique value of QS_WORKORDER_TEMP_ISSUE_DTLS table updating the details of the amount and quantity
	 *  @param actualTotalAmount this variable is holding the actual amount work order row before doing any changes
	 *  @param changedTotalAmount this variable is holding the current value of the work order and this is going to update by unique id's
	 *  @param changedQuantity this variable is holding the quantity of the work order row
	 *	@return	 result of the insert or update operation 
	 */
	@Override
	public int updateWorkOrderPriceByTmpIssuId(String tmpIssueAreaDtlsId, String changedAcceptedRate, String tempIssueId, String actualTotalAmount,String changedTotalAmount,String changedQuantity) {
		int result = 0;
		String query = "";
		//if tempIssueId is empty then update the QS_WORKORDER_TEMP_ISSUE_DTLS 
		if(!tempIssueId.equals("")){
			query = "UPDATE QS_WORKORDER_TEMP_ISSUE_DTLS set QUANTITY=?,TOTAL_AMOUNT=?  WHERE WO_WORK_TEMP_ISSUE_DTLS_ID = ?  ";
			result = jdbcTemplate.update(query,changedQuantity,changedTotalAmount, tempIssueId);
		}else{
			query="UPDATE QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS set PRICE=?,BASIC_AMOUNT=?, TOTAL_AMOUNT=? WHERE WO_WORK_TEMP_ISSUE_AREA_DTLS_ID = ?";
			result = jdbcTemplate.update(query,changedAcceptedRate, changedTotalAmount,changedTotalAmount,tmpIssueAreaDtlsId);
		}
		return result;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used to the permanent work order area detail's 
	 *  @param changedWorkOderDetail holding the current data of work area details after changing the work order work area details
	 *  @param actualWorkOderDetail holding the actual data of work area details before changing the details
	 *  @return	 result of the insert or update operation
	 */
	@Override
	public int updatePermanentWorkOrderPriceByTmpIssuId(String tmpIssueDtlsId, String changedAcceptedRate, String tempIssueId,String actualTotalAmount, String changedTotalAmount, String changedQuantity) {
		int result = 0;
		String query = "";
		if(!tempIssueId.equals("")){
			query = "UPDATE QS_WORKORDER_ISSUE_DETAILS set QUANTITY=?, TOTAL_AMOUNT=?  WHERE WO_WORK_ISSUE_DTLS_ID = ? ";
			result = jdbcTemplate.update(query,changedQuantity,changedTotalAmount, tempIssueId);
		}else{
			query="UPDATE QS_WORKORDER_ISSUE_AREA_DETAILS set PRICE=?,BASIC_AMOUNT=?, TOTAL_AMOUNT=? WHERE WO_WORK_ISSUE_AREA_DTLS_ID = ?";
			result = jdbcTemplate.update(query,changedAcceptedRate, changedTotalAmount,changedTotalAmount,tmpIssueDtlsId);
		}
		return result;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used to insert the changed details of the work order
	 *  @param changedWorkOderDetail holding the current data of work area details after changing the work order work area details
	 *  @param actualWorkOderDetail holding the actual data of work area details before changing the details
	 *  @param tempIssueId is the unique value of QS_WORKORDER_ISSUE_DETAILS table which is string in QS_WORKORDER_CHANGED_DTLS table
	 *  @param opeType is holding the operation type either it's a modify or delete
	 *  @param user_id stroing used_id QS_WORKORDER_CHANGED_DTLS table so that we can come to know who did the modification in work order
	 *  @param strFinalChangedComments is holding the string data what data is changed 
	 *  @return	 result of the insert or update operation
	 */	
	@Override
	public int insertWorkOrderChangedDetails(WorkOrderBean actualWorkOderDetail, WorkOrderBean changedWorkOderDetail,
			String tempIssueId, String nextApprovelEmpId, String opeType, String user_id,String strFinalChangedComments) {
		int  changedDtlsPK=jdbcTemplate.queryForObject("select QS_WORK_ORDER_CHANGED_DTLS_SEQ.NEXTVAL from dual", Integer.class);
		
		StringBuffer strChangedDetils = new StringBuffer("insert into QS_WORKORDER_CHANGED_DTLS (QS_WO_CHANGED_DTLS_ID,")
				.append(" ACTUAL_WORK_AREA_ID,CHANGED_WORK_AREA_ID ,ACTUAL_AREA_ALOCATED,CHANGED_AREA_ALOCATED,")
				.append(" ACTUAL_PRICE,CHANGED_PRICE,")
				.append(" CREATION_DATE,WO_CHANGE_ACTION,EMP_ID,REMARKS,QS_WORKORDER_TEMP_ISSUE_AREA_DTLS_ID,WO_WORK_TEMP_ISSUE_DTLS_ID,TYPE_OF_WORK,RECORD_TYPE)")
				.append(" values(?,?,?,?,?,?,?,sysdate,?,?,?,?,?,?,?)");
		Object[] data = { changedDtlsPK,actualWorkOderDetail.getWorkAreaId(), changedWorkOderDetail.getWorkAreaId(),
				actualWorkOderDetail.getSelectedArea(), changedWorkOderDetail.getSelectedArea(),
				actualWorkOderDetail.getAcceptedRate1(), changedWorkOderDetail.getAcceptedRate1(), opeType, user_id,
				strFinalChangedComments, actualWorkOderDetail.getQS_Temp_Issue_Dtls_Id(),changedWorkOderDetail.getQS_Temp_Issue_Id() ,changedWorkOderDetail.getTypeOfWork(),changedWorkOderDetail.getBoqRecordType()};
		int result = jdbcTemplate.update(strChangedDetils.toString(), data);
		return changedDtlsPK;
	}
	
	//inserting work order material changed details
	@Override
	public int insertWOmaterialChangedDetails(WorkOrderBean actualWorkOderDetail, WorkOrderBean changedWorkOderDetail, String materialModified, int changedDtlsPK, String actionType) {
		StringBuffer query=new StringBuffer("INSERT INTO QS_WORKORDER_QTY_CHG_DTLS(QS_WORKORDER_QTY_CHG_DTLS_ID,QS_WO_CHANGED_DTLS_ID,MATERIAL_GROUP_ID,UOM, ")
				.append(" OLD_PER_UNIT_QUANTITY,NEW_PER_UNIT_QUANTITY,OLD_PER_UNIT_AMOUNT,NEW_PER_UNIT_AMOUNT, ")
				.append(" OLD_TOTAL_QUANTITY,NEW_TOTAL_QUANTITY,OLD_TOTAL_AMOUNT,NEW_TOTAL_AMOUNT,REMARKS,ACTION) ")
				.append(" VALUES(QS_WORKORDER_QTY_CHG_DTLS_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?)");
		Object[] data = {changedDtlsPK,actualWorkOderDetail.getMaterialGroupId(),actualWorkOderDetail.getUnitsOfMeasurement1(),
				actualWorkOderDetail.getPerUnitQuantity(),changedWorkOderDetail.getPerUnitQuantity(),actualWorkOderDetail.getPerUnitAmount(),changedWorkOderDetail.getPerUnitAmount(),
				actualWorkOderDetail.getTotalQuantity(),changedWorkOderDetail.getTotalQuantity(),actualWorkOderDetail.getTotalAmount(),changedWorkOderDetail.getTotalAmount(),
				changedWorkOderDetail.getRemarks(),actionType};

		int	result = jdbcTemplate.update(query.toString(), data);
		return result;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used to deactivating the 
	 *  @param workAreaId is used for update the status of QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS if row removed
	 *  @param tempIssueDtlsId  is used for update the status of QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS if row removed
	 *  @return	 result of the insert or update operation QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS table row if removed from the work order
	 */	
	@Override
	public int doInActiveWorkOder(String workAreaId, String tempIssueId, String tempIssueDtlsId) throws Exception {
		String query = "UPDATE QS_WORKORDER_TEMP_ISSUE_DTLS set STATUS = 'I' WHERE QS_WO_TEMP_ISSUE_DTLS_ID = ? and WO_WORK_AREA_ID=?";
		query ="UPDATE QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS set STATUS = 'I' WHERE WO_WORK_TEMP_ISSUE_AREA_DTLS_ID = ? and WO_WORK_AREA_ID=?";
		int result = jdbcTemplate.update(query, tempIssueDtlsId, workAreaId);
		return result;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used to deactivating the 
	 *  @param workAreaId is used for update the status of QS_WORKORDER_ISSUE_AREA_DETAILS if row removed
	 *  @param tempIssueDtlsId  is used for update the status of QS_WORKORDER_ISSUE_AREA_DETAILS if row removed
	 *  @return	 result of the insert or update operation QS_WORKORDER_ISSUE_AREA_DETAILS table row if removed from the work order
	 */	
	@Override
		public int doInActivePermanentCurrentWorkOder(String workAreaId, String tempIssueId, String tempIssueDtlsId) {
		String	query ="UPDATE QS_WORKORDER_ISSUE_AREA_DETAILS set STATUS = 'I' WHERE WO_WORK_ISSUE_AREA_DTLS_ID = ?";
		int result = jdbcTemplate.update(query, tempIssueDtlsId);	
		return result;
		}	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used for rejecting the created work order in approval level's
	 *  @param bean object holding all the data which are going to use for rejecting the temporary work order
	 *  @return	 result of the insert or update operation QS_WORKORDER_ISSUE_AREA_DETAILS table row if removed from the work order
	 */	
	@Override
	public int rejectWorkOrderCreationCreation(WorkOrderBean bean) {
		// if this is revise work order
		String tempQSIssueId = bean.getQS_Temp_Issue_Id();
		String isSaveModifyOrUpdateOperation = bean.getIsSaveOrUpdateOperation();
		String query = "", actionType = "";

		// taking temporary quantity initiated back
		int result = takeTempWorkOrderQuantityBack(bean, tempQSIssueId);

		// here updating the status of QS_WORKORDER_TEMP_ISSUE table
		if (isSaveModifyOrUpdateOperation != null && isSaveModifyOrUpdateOperation.equals("Discard")) {
			actionType = "RDF";// rejected - draft work order
		} else if (isSaveModifyOrUpdateOperation != null && isSaveModifyOrUpdateOperation.equals("Modify WorkOrder")) {
			actionType = "RMW";// rejected - Modify work order
		} else {
			actionType = "R";
		}
		query = "UPDATE QS_WORKORDER_TEMP_ISSUE SET STATUS = ? WHERE TEMP_WORK_ORDER_NUMBER = ? AND SITE_ID=?";
		result = jdbcTemplate.update(query, actionType, bean.getSiteWiseWONO(), bean.getSiteId());
		return result;
	}
	

	public int takeTempWorkOrderQuantityBack(WorkOrderBean bean, String tempIssueId) {
		//if this is revise work order
		String tempQSIssueId=bean.getQS_Temp_Issue_Id();
		String revision=bean.getRevision(); 
		if(revision==null){
			throw new RuntimeException("revision number not found.");
		}
		String query = "";
		//if the work order contains "/R" means this is revise work order
        if(bean.getWorkOrderNo().contains("/R")&&!revision.equals("0")){
			//loading old work order no bcoz this is the revise work order 
			String oldWorkOrderWhileRevise=jdbcTemplate.queryForObject("SELECT OLD_WORK_ORDER_NUMBER FROM QS_WORKORDER_TEMP_ISSUE WHERE QS_WO_TEMP_ISSUE_ID=? ", new Object[]{tempQSIssueId}, String.class);
			//loading old QS_WORKORDER_ISSUE_ID for activating and deactivating old work order
			String oldWorkOrderIssueId=jdbcTemplate.queryForObject("SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER=? AND SITE_ID=? ", new Object[]{oldWorkOrderWhileRevise,bean.getSiteId()}, String.class);
		
			//if revise work order i rejected then active Previous Work Order Again
			String doActivePrevWO="UPDATE QS_WORKORDER_ISSUE set STATUS='A' where WORK_ORDER_NUMBER=? And SITE_ID=?";
			int count=jdbcTemplate.update(doActivePrevWO,oldWorkOrderWhileRevise,bean.getSiteId());

			//Adding Remarks for Running Bills which are going to activate again while revise work order rejection operation
/*			List<Map<String, Object>> listOfRejectedBills=jdbcTemplate.queryForList("SELECT TEMP_BILL_ID FROM QS_CONTRACTOR_BILL_TEMP WHERE QS_WORKORDER_ISSUE_ID="+oldWorkOrderIssueId+" and STATUS='T' ");
			for (Map<String, Object> map : listOfRejectedBills) {
				String tempBillId = map.get("TEMP_BILL_ID") == null ? "" : map.get("TEMP_BILL_ID").toString();
				int QS_BILL_APPR_REJ_DTLS_SEQ = jdbcTemplate .queryForObject("SELECT QS_BILL_APPR_REJ_DTLS_SEQ.NEXTVAL FROM DUAL", Integer.class);
				String queryForRemarks = "INSERT INTO  QS_CONT_TMP_BILL_APPR_REJ_DTLS (QS_BILL_APPR_REJ_DTLS,TEMP_BILL_ID,EMP_ID,STATUS,REMARKS,SITE_ID,CREATED_DATE) VALUES(?,?,?,?,?,?,sysdate)";
				Object[] params = { QS_BILL_APPR_REJ_DTLS_SEQ, tempBillId, bean.getUserId(), "A", " Activated bill while revising work order.", bean.getSiteId() };
				count = jdbcTemplate.update(queryForRemarks, params);
			}	*/
			//if revise work order i rejected then active Previous Work Order Again
			//T means Temporary Inactive Bill while revise work order operation
			//count=jdbcTemplate.update("UPDATE QS_CONTRACTOR_BILL_TEMP SET STATUS='A' WHERE QS_WORKORDER_ISSUE_ID="+oldWorkOrderIssueId+" and  STATUS='T' ");			
			
			//here reassigning the work area quantity to revise work order
			query = "SELECT WO_WORK_ISSUE_AREA_DTLS_ID,WO_WORK_AREA_ID,AREA_ALOCATED_QTY FROM  QS_WORKORDER_ISSUE_AREA_DETAILS QWIA,QS_WORKORDER_ISSUE_DETAILS DTLS  WHERE  DTLS.WO_WORK_ISSUE_DTLS_ID=QWIA.WO_WORK_ISSUE_DTLS_ID  AND  DTLS.QS_WORKORDER_ISSUE_ID=? AND  DTLS.STATUS='A'  AND QWIA.STATUS='A' ";
			Object[] obj={oldWorkOrderIssueId};
			List<Map<String, Object>> result = jdbcTemplate.queryForList(query,obj);
			for (Map<String, Object> map : result) {
				String workAreaId = map.get("WO_WORK_AREA_ID") == null ? "" : map.get("WO_WORK_AREA_ID").toString();
				String allocatedArea = map.get("AREA_ALOCATED_QTY") == null ? "" : map.get("AREA_ALOCATED_QTY").toString();
				String tempIssueId1 = map.get("WO_WORK_ISSUE_AREA_DTLS_ID") == null ? "" : map.get("WO_WORK_ISSUE_AREA_DTLS_ID").toString();
				WorkOrderBean actualWorkOderDetail = new WorkOrderBean();
				actualWorkOderDetail.setSelectedArea(allocatedArea);
				actualWorkOderDetail.setWorkAreaId(workAreaId);
				actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueId1);
				updateWorkAreaMapping(actualWorkOderDetail, "", "");
			}
		}
	    //here reverting the work area quantity
		query = "SELECT WO_WORK_TEMP_ISSUE_AREA_DTLS_ID,WO_WORK_AREA_ID,AREA_ALOCATED_QTY FROM  QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QWIA,QS_WORKORDER_TEMP_ISSUE_DTLS DTLS   WHERE  DTLS.WO_WORK_TEMP_ISSUE_DTLS_ID=QWIA.WO_WORK_TEMP_ISSUE_DTLS_ID  AND  QS_WO_TEMP_ISSUE_ID=?   AND QWIA.STATUS='A' AND DTLS.STATUS='A' ";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query,tempIssueId);
		for (Map<String, Object> map : result) {
			String workAreaId = map.get("WO_WORK_AREA_ID") == null ? "" : map.get("WO_WORK_AREA_ID").toString();
			String allocatedArea = map.get("AREA_ALOCATED_QTY") == null ? "" : map.get("AREA_ALOCATED_QTY").toString();
			String tempIssueId1 = map.get("WO_WORK_TEMP_ISSUE_AREA_DTLS_ID") == null ? "" : map.get("WO_WORK_TEMP_ISSUE_AREA_DTLS_ID").toString();
			WorkOrderBean actualWorkOderDetail = new WorkOrderBean();
			actualWorkOderDetail.setSelectedArea(allocatedArea);
			actualWorkOderDetail.setWorkAreaId(workAreaId);
			actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueId1);
			updateWorkAreaMapping(actualWorkOderDetail, "", "deleted");
		}
		return result.size();
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used for rejecting the permanent work order
	 *  @param bean object holding all the data which are going to use for rejecting the temporary work order
	 *  @return	 result of the insert or update operation QS_WORKORDER_ISSUE_AREA_DETAILS table row if removed from the work order
	 */	
	@Override
	public int rejectPermanentWorkOrderCreationCreation(WorkOrderBean bean) {
		//here reallocating the work area
		String query = "SELECT WO_WORK_ISSUE_AREA_DTLS_ID,WO_WORK_AREA_ID,AREA_ALOCATED_QTY FROM  QS_WORKORDER_ISSUE_AREA_DETAILS QWIA,QS_WORKORDER_ISSUE_DETAILS DTLS  WHERE  DTLS.WO_WORK_ISSUE_DTLS_ID=QWIA.WO_WORK_ISSUE_DTLS_ID  AND  DTLS.QS_WORKORDER_ISSUE_ID=? AND  DTLS.STATUS='A'  AND QWIA.STATUS='A' ";
		Object[] obj={0};//1881//1374
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query,obj);
		for (Map<String, Object> map : result) {
			System.out.println(map);
			String workAreaId=map.get("WO_WORK_AREA_ID")==null?"":map.get("WO_WORK_AREA_ID").toString();
			String allocatedArea=map.get("AREA_ALOCATED_QTY")==null?"":map.get("AREA_ALOCATED_QTY").toString();
			String tempIssueId1=map.get("WO_WORK_ISSUE_AREA_DTLS_ID")==null?"":map.get("WO_WORK_ISSUE_AREA_DTLS_ID").toString();
			WorkOrderBean actualWorkOderDetail=new WorkOrderBean();
			actualWorkOderDetail.setSelectedArea(allocatedArea);
			actualWorkOderDetail.setWorkAreaId(workAreaId);
			actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueId1);
			updateWorkAreaMapping(actualWorkOderDetail,"","deleted");
		}
		return result.size();
	}
	
	
	
	public int deletePermanentWorkOrder123 (WorkOrderBean bean) {
		//here reallocating the work area
		String query = "SELECT WO_WORK_ISSUE_AREA_DTLS_ID,WO_WORK_AREA_ID,AREA_ALOCATED_QTY FROM  QS_WORKORDER_ISSUE_AREA_DETAILS QWIA,QS_WORKORDER_ISSUE_DETAILS DTLS  WHERE  DTLS.WO_WORK_ISSUE_DTLS_ID=QWIA.WO_WORK_ISSUE_DTLS_ID  AND  DTLS.QS_WORKORDER_ISSUE_ID=? AND  DTLS.STATUS='A'  AND QWIA.STATUS='A' ";
		Object[] obj={0};//1881//1374
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query,obj);
		log.info(result.size());
		int count=0;
		for (Map<String, Object> map : result) {
			log.info(map);
			String workAreaId=map.get("WO_WORK_AREA_ID")==null?"":map.get("WO_WORK_AREA_ID").toString();
			String allocatedArea=map.get("AREA_ALOCATED_QTY")==null?"":map.get("AREA_ALOCATED_QTY").toString();
			String tempIssueId1=map.get("WO_WORK_ISSUE_AREA_DTLS_ID")==null?"":map.get("WO_WORK_ISSUE_AREA_DTLS_ID").toString();
			WorkOrderBean actualWorkOderDetail=new WorkOrderBean();
			actualWorkOderDetail.setSelectedArea(allocatedArea);
			actualWorkOderDetail.setWorkAreaId(workAreaId);
			actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueId1);
			updateWorkAreaMapping(actualWorkOderDetail,"","deleted");
		}
		if(result.size()>0){
			return result.size();
		}
		//
		
		StringBuffer bufferBillRecoveryDetails=new StringBuffer("delete from QS_BILL_RECOVERY QTB where QS_WORKORDER_ISSUE_ID in (Select QWI.QS_WORKORDER_ISSUE_ID from QS_BILL_RECOVERY QTB, QS_CONTRACTOR_BILL QC , QS_WORKORDER_ISSUE QWI")
				.append(" WHERE QTB.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID") 
				.append(" AND QC.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID ")
				.append(" AND QC.QS_WORKORDER_ISSUE_ID=QTB.QS_WORKORDER_ISSUE_ID ")
				.append(" AND QWI.SITE_ID=?")
				.append(" AND QWI.TYPE_OF_WORK=? ")
				.append(" AND QWI.QS_WORKORDER_ISSUE_ID=?)") 
				.append(" AND QS_WORKORDER_ISSUE_ID=?");
		
		//count=	jdbcTemplate.update(bufferBillRecoveryDetails.toString(),bean.getSiteId(),bean.getTypeOfWork(),bean.getQS_Temp_Issue_Id(),bean.getQS_Temp_Issue_Id());
		
		
		StringBuffer bufferBillRecoveryHistoryDetails=new StringBuffer("delete from QS_BILL_RECOVERY_HISTORY where QS_WORKORDER_ISSUE_ID in(Select QWI.QS_WORKORDER_ISSUE_ID from QS_BILL_RECOVERY_HISTORY QTBH , QS_CONTRACTOR_BILL QC , QS_WORKORDER_ISSUE QWI") 
				.append(" where QTBH.BILL_ID=QC.BILL_ID")
				.append(" AND QC.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID") 
				.append(" AND QC.QS_WORKORDER_ISSUE_ID=QTBH.QS_WORKORDER_ISSUE_ID ")
				.append(" AND QWI.SITE_ID=? ")
				.append(" AND QWI.TYPE_OF_WORK in (?)") 
				.append(" AND QWI.QS_WORKORDER_ISSUE_ID=?)")
				.append(" QS_WORKORDER_ISSUE_ID=?");
				
		//count=	jdbcTemplate.update(bufferBillRecoveryHistoryDetails.toString(),bean.getSiteId(),bean.getTypeOfWork(),bean.getQS_Temp_Issue_Id(),bean.getQS_Temp_Issue_Id());	
		
		
		StringBuffer bufferNmrPaymentAreaDetails=new StringBuffer("delete from QS_INV_AGN_WORK_PMT_DTL_HR where QS_WORKORDER_ISSUE_ID in(select QC.BILL_ID from QS_INV_AGN_WORK_PMT_DTL_HR QTIA, QS_CONTRACTOR_BILL QC , QS_WORKORDER_ISSUE QWI")
				.append(" where QTIA.BILL_ID=QC.BILL_ID ")
				.append(" AND QC.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID ")
				.append(" AND QTIA.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID ")
				.append(" AND QWI.SITE_ID=? ")
				.append(" AND QWI.TYPE_OF_WORK=? ")
				.append(" AND QWI.QS_WORKORDER_ISSUE_ID=?)")
				.append(" AND QS_WORKORDER_ISSUE_ID=? ")
				.append(" AND CONTRACTOR_BILL_ID=?");
		
		//count=	jdbcTemplate.update(bufferNmrPaymentAreaDetails.toString(),bean.getSiteId(),bean.getTypeOfWork(),bean.getQS_Temp_Issue_Id(),bean.getQS_Temp_Issue_Id());
		
		
		StringBuffer bufferPWPaymentAreaDetails=new StringBuffer("delete from QS_INV_AGN_AREA_PMT_DTL where QS_WORKORDER_ISSUE_ID in (select QC.QS_WORKORDER_ISSUE_ID from QS_INV_AGN_AREA_PMT_DTL QTIA, QS_CONTRACTOR_BILL QC , QS_WORKORDER_ISSUE QWI")
				.append("where QTIA.BILL_ID=QC.BILL_ID") 
				.append("AND QC.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID") 
				.append("AND QTIA.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID") 
				.append("AND QWI.SITE_ID=?")
				.append("AND QWI.TYPE_OF_WORK=?")
				.append("AND QWI.QS_WORKORDER_ISSUE_ID=?)")
				.append("AND BILL_ID=?") 
				.append("AND QS_WORKORDER_ISSUE_ID=?");
		
		
		
		StringBuffer bufferDeductDetails=new StringBuffer("delete from QS_BILL_DEDUCTION_DTLS where QS_WORKORDER_ISSUE_ID in (select QWI.QS_WORKORDER_ISSUE_ID from QS_BILL_DEDUCTION_DTLS QTIA, QS_CONTRACTOR_BILL QC , QS_WORKORDER_ISSUE QWI")
				.append(" where QTIA.CONTRACTOR_BILL_ID=QC.BILL_ID")
				.append(" AND QC.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID ")
				.append(" AND QTIA.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID ")
				.append(" AND QWI.SITE_ID=? ")
				.append(" AND QWI.TYPE_OF_WORK=?")
				.append(" AND QWI.QS_WORKORDER_ISSUE_ID=?)")
				.append(" AND CONTRACTOR_BILL_ID in (?)")
				.append(" AND QS_WORKORDER_ISSUE_ID=?");
		
		StringBuffer bufferBillDetails=new StringBuffer("delete from QS_CONTRACTOR_BILL where QS_WORKORDER_ISSUE_ID in (select QWI.QS_WORKORDER_ISSUE_ID from QS_CONTRACTOR_BILL QSBT , QS_WORKORDER_ISSUE QWI") 
				.append(" where QSBT.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID ")
				.append(" AND QWI.SITE_ID=?")
				.append(" AND QWI.TYPE_OF_WORK in (?)")
				.append(" AND QWI.QS_WORKORDER_ISSUE_ID=?)")
				.append(" AND BILL_ID in (?)")
				.append(" AND QS_WORKORDER_ISSUE_ID=?");
		
		
		
		
		//work order tables
		StringBuffer bufferAreaDetails=new StringBuffer("delete from QS_WORKORDER_ISSUE_AREA_DETAILS where WO_WORK_ISSUE_DTLS_ID in (select QWID.WO_WORK_ISSUE_DTLS_ID from QS_WORKORDER_ISSUE_AREA_DETAILS QWIAD,QS_WORKORDER_ISSUE_DETAILS QWID,QS_WORKORDER_ISSUE QWI ") 
				.append(" WHERE QWI.QS_WORKORDER_ISSUE_ID=QWID.QS_WORKORDER_ISSUE_ID") 
				.append(" AND QWID.WO_WORK_ISSUE_DTLS_ID=QWIAD.WO_WORK_ISSUE_DTLS_ID")
				.append(" AND QWI.SITE_ID=? AND QWI.TYPE_OF_WORK IN (?) AND QWI.QS_WORKORDER_ISSUE_ID=?)");
		
		//count=	jdbcTemplate.update(bufferAreaDetails.toString(),bean.getSiteId(),bean.getTypeOfWork(),bean.getQS_Temp_Issue_Id());
		
		StringBuffer bufferIssueDetails=new StringBuffer("delete from QS_WORKORDER_ISSUE_DETAILS where QS_WORKORDER_ISSUE_ID in (select QWID.QS_WORKORDER_ISSUE_ID from QS_WORKORDER_ISSUE_DETAILS QWID,QS_WORKORDER_ISSUE QWI") 
				.append(" where QWI.QS_WORKORDER_ISSUE_ID=QWID.QS_WORKORDER_ISSUE_ID") 
				.append(" AND QWI.SITE_ID=? AND TYPE_OF_WORK in (?)  AND QWI.QS_WORKORDER_ISSUE_ID=?)");
		
		//count=	jdbcTemplate.update(bufferIssueDetails.toString(),bean.getSiteId(),bean.getTypeOfWork(),bean.getQS_Temp_Issue_Id());
		
		StringBuffer bufferApprRejDetails=new StringBuffer("delete from QS_WORKORDER_CRT_APPRL_DTLS where WO_WORK_ISSUE_ID in (select QS_WORKORDER_ISSUE_ID from QS_WORKORDER_ISSUE QWI") 
				.append(" where QWI.SITE_ID=? AND TYPE_OF_WORK in (?) AND QWI.QS_WORKORDER_ISSUE_ID=?)");
		//count=jdbcTemplate.update(bufferApprRejDetails.toString(),bean.getSiteId(),bean.getTypeOfWork(),bean.getQS_Temp_Issue_Id());
		
		
		StringBuffer bufferWoIssueDetails=new StringBuffer("delete from QS_WORKORDER_ISSUE QWI") 
		.append("where QWI.SITE_ID=? AND QWI.TYPE_OF_WORK in (?)  AND QWI.QS_WORKORDER_ISSUE_ID=?");
		
		//count=	jdbcTemplate.update(bufferWoIssueDetails.toString(),bean.getSiteId(),bean.getTypeOfWork(),bean.getQS_Temp_Issue_Id());
		
		return 0;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used selecting the temporary work order details means where the work order is pending
	 *  @param fromDate is holding date from where the data you want
	 *  @param toDate is holding date that you can select the data till date 
	 *  @param site_Id used for selecting work order by site wise
	 *  @param tempWorkOrderNumber is the unique value by site wise 
	 *  @return result of the select query
	 */	

	@Override
	public List<WorkOrderBean> getMyWorkOrderStatus(String fromDate, String toDate, String site_Id,String tempWorkOrderNumber) {
		StringBuffer query = null;

		if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
			query =new StringBuffer(" SELECT S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,NVL(QWTI.WORK_ORDER_NAME,'-') AS WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,TO_CHAR(QWTI.CREATED_DATE,'DD-MON-YY HH24:MI:SS') AS CREATED_DATE,(SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) AS PENDING_EMP_NAME, ")
					.append(" SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,QWTI.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" from QS_WORKORDER_TEMP_ISSUE QWTI  LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON  SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S where QWTI.status in ('A','R')  and   S.SITE_ID=QWTI.SITE_ID  and  QWTI.site_id='"+ site_Id + "' AND TRUNC(QWTI.CREATED_DATE)  BETWEEN TO_DATE('" + fromDate + "','dd-MM-yy') AND TO_DATE('"+ toDate + "','dd-MM-yy')");
		} else if (StringUtils.isNotBlank(fromDate)) {
			query =new StringBuffer("SELECT  S.SITE_NAME, QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,NVL(QWTI.WORK_ORDER_NAME,'-') AS WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,TO_CHAR(QWTI.CREATED_DATE,'DD-MON-YY HH:MM:SS') AS CREATED_DATE,(SELECT  INITCAP(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) AS PENDING_EMP_NAME, ")
					.append(" QWTI.CONTRACTOR_ID,QWTI.TEMP_WORK_ORDER_NUMBER,QWTI.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS , SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  AS CONTRACTOR_NAME ")
					.append(" FROM QS_WORKORDER_TEMP_ISSUE QWTI  LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON  SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S  WHERE  QWTI.SITE_ID='"+site_Id + "' and   S.SITE_ID=QWTI.SITE_ID  AND QWTI.status in ('A','R','DF','M') and  TRUNC(QWTI.CREATED_DATE) >=TO_DATE('" + fromDate + "', 'dd-MM-yy')");
		} else if (StringUtils.isNotBlank(toDate)) {
			query =new StringBuffer("select   S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,nvl(QWTI.WORK_ORDER_NAME,'-') as WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,to_char(QWTI.CREATED_DATE,'dd-MON-yy hh:MM:ss') as CREATED_DATE,(SELECT  initCap(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) as PENDING_EMP_NAME, ")
					.append(" QWTI.CONTRACTOR_ID,SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  AS CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,S.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" FROM QS_WORKORDER_TEMP_ISSUE  QWTI LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON  SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S WHERE  QWTI.SITE_ID='"+ site_Id + "' and   S.SITE_ID=QWTI.SITE_ID  AND QWTI.status in ('A','R','DF','M') and  TRUNC(QWTI.CREATED_DATE) <=TO_DATE('" + toDate + "', 'dd-MM-yy')");
		} else if (StringUtils.isNotBlank(tempWorkOrderNumber)) {
			query =new StringBuffer("SELECT   S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,NVL(QWTI.WORK_ORDER_NAME,'-') AS WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,TO_CHAR(QWTI.CREATED_DATE,'DD-MON-YY HH:MM:SS') AS CREATED_DATE,(SELECT  INITCAP(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) AS PENDING_EMP_NAME, ")
					.append(" QWTI.CONTRACTOR_ID,SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  AS CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,S.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" FROM QS_WORKORDER_TEMP_ISSUE QWTI  LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON  SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S WHERE  QWTI.SITE_ID='"+ site_Id + "'  and   S.SITE_ID=QWTI.SITE_ID     and QWTI.status in ('A','R','DF','M') and  TEMP_WORK_ORDER_NUMBER='" + tempWorkOrderNumber + "'");
		}else{
			query =new StringBuffer("SELECT   S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,NVL(QWTI.WORK_ORDER_NAME,'-') AS WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,TO_CHAR(QWTI.CREATED_DATE,'DD-MM-YYYY HH24:MI:SS') AS CREATED_DATE,(SELECT  INITCAP(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) AS PENDING_EMP_NAME, ")
					.append(" QWTI.CONTRACTOR_ID,SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  AS CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,S.SITE_ID,TO_CHAR(TO_DATE(QWTI.WORK_ORDER_DATE ,'DD-MM-YY'),'DD-MM-YYYY') AS WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" FROM QS_WORKORDER_TEMP_ISSUE QWTI LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON  SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S  WHERE  QWTI.SITE_ID='"+ site_Id + "'  and   S.SITE_ID=QWTI.SITE_ID  and QWTI.status in ('A','R','DF','M') ");
		}
		List<WorkOrderBean> list = new ArrayList<WorkOrderBean>();
		list=jdbcTemplate.query(query.toString(), new ExtractWorkOrderData());
		return list;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used selecting the temporary work order details means where the work order is pending and this is used for site wise work order status
	 *  @param fromDate is holding date from where the data you want
	 *  @param toDate is holding date that you can select the data till date 
	 *  @param site_Id used for selecting work order by site wise
	 *  @param tempWorkOrderNumber is the unique value by site wise 
	 *  @return	 result of the select query
	 */	
	@Override
	public List<WorkOrderBean> getSitewiseWorkOrderStatus(String fromDate, String toDate, String site_Id,
			String tempWorkOrderNumber) {
		StringBuffer query = null;

		if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
			query =new StringBuffer(" SELECT S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,NVL(QWTI.WORK_ORDER_NAME,'-') AS WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,TO_CHAR(QWTI.CREATED_DATE,'DD-MON-YY HH:MM:SS') AS CREATED_DATE,(SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) AS PENDING_EMP_NAME, ")
					.append(" SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,QWTI.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" FROM QS_WORKORDER_TEMP_ISSUE QWTI LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S  WHERE QWTI.STATUS IN ('A','R','DF','M') AND   S.SITE_ID=QWTI.SITE_ID  AND TRUNC(QWTI.CREATED_DATE)  BETWEEN TO_DATE('" + fromDate + "','dd-MM-yy') AND TO_DATE('"+ toDate + "','dd-MM-yy')");
		} else if (StringUtils.isNotBlank(fromDate)) {
			query =new StringBuffer("select   S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,nvl(QWTI.WORK_ORDER_NAME,'-') as WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,to_char(QWTI.CREATED_DATE,'dd-MON-yy hh:MM:ss') as CREATED_DATE,(SELECT  initCap(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) as PENDING_EMP_NAME,")
					.append(" QWTI.CONTRACTOR_ID,QWTI.TEMP_WORK_ORDER_NUMBER,QWTI.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS , SC.CONTRACTOR_ID,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  AS CONTRACTOR_NAME")
					.append(" FROM QS_WORKORDER_TEMP_ISSUE QWTI  LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  ON SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S  WHERE S.SITE_ID=QWTI.SITE_ID AND QWTI.STATUS IN ('A','R','DF','M') AND  TRUNC(QWTI.CREATED_DATE) >=TO_DATE('" + fromDate + "', 'dd-MM-yy')");
		} else if (StringUtils.isNotBlank(toDate)) {
			query =new StringBuffer("select   S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,nvl(QWTI.WORK_ORDER_NAME,'-') as WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,to_char(QWTI.CREATED_DATE,'dd-MON-yy hh:MM:ss') as CREATED_DATE,(SELECT  initCap(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) as PENDING_EMP_NAME, ")
					.append(" QWTI.CONTRACTOR_ID,SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  AS CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,QWTI.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" FROM QS_WORKORDER_TEMP_ISSUE  QWTI  LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  ON SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S WHERE  S.SITE_ID=QWTI.SITE_ID    AND QWTI.STATUS IN ('A','R','DF','M') AND  TRUNC(QWTI.CREATED_DATE) <=TO_DATE('" + toDate + "', 'dd-MM-yy')");
		} else if (StringUtils.isNotBlank(tempWorkOrderNumber)) {
			query =new StringBuffer("SELECT   S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,NVL(QWTI.WORK_ORDER_NAME,'-') AS WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,TO_CHAR(QWTI.CREATED_DATE,'DD-MON-YY HH:MM:SS') AS CREATED_DATE,(SELECT  INITCAP(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) AS PENDING_EMP_NAME, ")
					.append(" QWTI.CONTRACTOR_ID,SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  AS CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,QWTI.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" FROM QS_WORKORDER_TEMP_ISSUE QWTI LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  ON SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S   WHERE  S.SITE_ID=QWTI.SITE_ID    AND QWTI.STATUS IN ('A','R','DF','M') AND  TEMP_WORK_ORDER_NUMBER='" + tempWorkOrderNumber + "'");
		}else{
			query =new StringBuffer("select   S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,nvl(QWTI.WORK_ORDER_NAME,'-') as WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,to_char(QWTI.CREATED_DATE,'dd-MM-yyyy hh24:mi:ss') as CREATED_DATE,(SELECT  initCap(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) as PENDING_EMP_NAME, ")
					.append(" QWTI.CONTRACTOR_ID,SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  AS CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,S.SITE_ID,TO_CHAR(TO_DATE(QWTI.WORK_ORDER_DATE ,'DD-MM-YY'),'DD-MM-YYYY') AS  WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" FROM QS_WORKORDER_TEMP_ISSUE QWTI  LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  ON SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S   WHERE  QWTI.SITE_ID='"+ site_Id + "'  and   S.SITE_ID=QWTI.SITE_ID    and QWTI.status in ('A','R','DF','M') ");
		}

		List<WorkOrderBean> list = new ArrayList<WorkOrderBean>();

		list=jdbcTemplate.query(query.toString(), new ExtractWorkOrderData());
		return list;
	}
	//this class is for extracting data
	class ExtractWorkOrderData implements ResultSetExtractor<List<WorkOrderBean>>{
		@Override
		public List<WorkOrderBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
			List<WorkOrderBean> list = new ArrayList<WorkOrderBean>();
			while (rs.next()) {
				WorkOrderBean bean = new WorkOrderBean();
				String typeOfWork=rs.getString("TYPE_OF_WORK") == null ? "-" : rs.getString("TYPE_OF_WORK");
				if(typeOfWork.equals("PIECEWORK")){
					typeOfWork="PIECE WORK";
				}
				bean.setTypeOfWork(typeOfWork);
				bean.setWorkOrderNo(rs.getString("PER_WORK_ORDER_NUMBER") == null ? "0" : rs.getString("PER_WORK_ORDER_NUMBER"));
				bean.setQS_Temp_Issue_Id(rs.getString("QS_WO_TEMP_ISSUE_ID") == null ? "0" : rs.getString("QS_WO_TEMP_ISSUE_ID"));
				bean.setWorkOrderCreadeDate(rs.getString("CREATED_DATE") == null ? "0000-00-00 00:00:00.000"		: rs.getString("CREATED_DATE"));
				String pendingEmpName = rs.getString("PENDING_EMP_NAME") == null ? "": rs.getString("PENDING_EMP_NAME");
				bean.setContractorId(rs.getString("CONTRACTOR_ID") == null ? "0" : rs.getString("CONTRACTOR_ID"));
				bean.setContractorName(rs.getString("CONTRACTOR_NAME") == null ? ""		: rs.getString("CONTRACTOR_NAME"));
				bean.setWorkOrderName(rs.getString("WORK_ORDER_NAME") == null ? ""		: rs.getString("WORK_ORDER_NAME"));
				String work_order_number = rs.getString("TEMP_WORK_ORDER_NUMBER") == null ? "0": rs.getString("TEMP_WORK_ORDER_NUMBER");
				bean.setSiteWiseWONO(work_order_number);
				String status=rs.getString("STATUS") == null ? "-" : rs.getString("STATUS");
				bean.setPendingEmpId(pendingEmpName);
			
				if(status.equals("A")){
					status="Active";
				}else if(status.equals("I")){
					bean.setPendingEmpId(" Work Order Competed ");
					status="Inactive";
				}else if(status.equals("R")){
					bean.setPendingEmpId(status.equals("R")?pendingEmpName=pendingEmpName+" Rejected Work Order ":pendingEmpName);
					status="Rejected";
				}else if(status.equals("DF")){
					//bean.setPendingEmpId(status.equals("R")?pendingEmpName=pendingEmpName+" Rejected Work Order ":pendingEmpName);
					status="Active";
				}else if(status.equals("M")){
					//bean.setPendingEmpId(status.equals("R")?pendingEmpName=pendingEmpName+" Rejected Work Order ":pendingEmpName);
					status="Active";
				}
				
				bean.setWorkOrderStatus(status);
				bean.setSiteId(rs.getString("SITE_ID") == null ? "" : rs.getString("SITE_ID"));
				bean.setSiteName(rs.getString("SITE_NAME") == null ? "" : rs.getString("SITE_NAME"));
				bean.setWorkOrderDate(rs.getString("WORK_ORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("WORK_ORDER_DATE"));
				list.add(bean);
			}
			return list;
		}
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used selecting the temporary work order details means where the work order is pending or for update temp work order purpse we used this method
	 *  @param fromDate is holding date from where the data you want
	 *  @param toDate is holding date that you can select the data till date 
	 *  @param site_Id used for selecting work order by site wise
	 *  @param tempWorkOrderNumber is the unique value by site wise 
	 *  @return	 result of the select query
	 */	
	@Override
	public List<WorkOrderBean> getMyTempWOForUpdateDetail(String fromDate, String toDate, String site_Id,
			String tempWorkOrderNumber, String user_id) {
		StringBuffer query = null;

		if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
			query =new StringBuffer(" select    S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,nvl(QWTI.WORK_ORDER_NAME,'-') as WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,to_char(QWTI.CREATED_DATE,'dd-MON-yy hh:MM:ss') as CREATED_DATE,(SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) as PENDING_EMP_NAME, ")
					.append("  SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,QWTI.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" from QS_WORKORDER_TEMP_ISSUE QWTI  LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S where QWTI.site_id='"+ site_Id + "'  and   S.SITE_ID=QWTI.SITE_ID  and QWTI.PENDING_EMP_ID!='END' and QWTI.CREATED_BY='"+user_id+"' AND TRUNC(QWTI.CREATED_DATE)  BETWEEN TO_DATE('" + fromDate + "','dd-MM-yy') AND TO_DATE('"+ toDate + "','dd-MM-yy')");
		} else if (StringUtils.isNotBlank(fromDate)) {
			query =new StringBuffer("select S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,nvl(QWTI.WORK_ORDER_NAME,'-') as WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,to_char(QWTI.CREATED_DATE,'dd-MON-yy hh:MM:ss') as CREATED_DATE,(SELECT  initCap(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) as PENDING_EMP_NAME,")
					.append(" QWTI.CONTRACTOR_ID,QWTI.TEMP_WORK_ORDER_NUMBER,QWTI.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS , SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ")
					.append(" from QS_WORKORDER_TEMP_ISSUE QWTI  LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S  where  s.site_id='"+ site_Id + "'  and   S.SITE_ID=QWTI.SITE_ID and QWTI.CREATED_BY='"+user_id+"' and QWTI.PENDING_EMP_ID!='END'   AND  TRUNC(QWTI.CREATED_DATE) >=TO_DATE('" + fromDate + "', 'dd-MM-yy')");
		} else if (StringUtils.isNotBlank(toDate)) {
			query =new StringBuffer("select  S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,nvl(QWTI.WORK_ORDER_NAME,'-') as WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,to_char(QWTI.CREATED_DATE,'dd-MON-yy hh:MM:ss') as CREATED_DATE,(SELECT  initCap(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) as PENDING_EMP_NAME, ")
					.append("QWTI.CONTRACTOR_ID,SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,s.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" from QS_WORKORDER_TEMP_ISSUE QWTI LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S where  s.site_id='"	+ site_Id + "'  and   S.SITE_ID=QWTI.SITE_ID and QWTI.CREATED_BY='"+user_id+"' and QWTI.PENDING_EMP_ID!='END'  AND  TRUNC(QWTI.CREATED_DATE) <=TO_DATE('" + toDate + "', 'dd-MM-yy')");
		} else if (StringUtils.isNotBlank(tempWorkOrderNumber)) {
			query =new StringBuffer("select    S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,nvl(QWTI.WORK_ORDER_NAME,'-') as WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,to_char(QWTI.CREATED_DATE,'dd-MON-yy hh:MM:ss') as CREATED_DATE,(SELECT  initCap(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) as PENDING_EMP_NAME, ")
					.append("QWTI.CONTRACTOR_ID,SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,s.SITE_ID,QWTI.WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" from QS_WORKORDER_TEMP_ISSUE QWTI LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S where  s.site_id='"+ site_Id + "'  and   S.SITE_ID=QWTI.SITE_ID and QWTI.CREATED_BY='"+user_id+"'  and QWTI.PENDING_EMP_ID!='END'  and TEMP_WORK_ORDER_NUMBER='" + tempWorkOrderNumber + "'");
		}else{
			query =new StringBuffer("select   S.SITE_NAME,QWTI.PER_WORK_ORDER_NUMBER,QWTI.TYPE_OF_WORK,nvl(QWTI.WORK_ORDER_NAME,'-') as WORK_ORDER_NAME,QWTI.QS_WO_TEMP_ISSUE_ID,QWTI.CREATED_BY,to_char(QWTI.CREATED_DATE,'dd-MM-yyyy hh24:mi:ss') as CREATED_DATE,(SELECT  initCap(SED.EMP_NAME) FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTI.PENDING_EMP_ID) as PENDING_EMP_NAME,")
					.append("QWTI.CONTRACTOR_ID,SC.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME,QWTI.TEMP_WORK_ORDER_NUMBER,s.SITE_ID,to_char(to_date(QWTI.WORK_ORDER_DATE ,'dd-MM-yy'),'dd-MM-yyyy') as  WORK_ORDER_DATE,QWTI.STATUS ")
					.append(" from QS_WORKORDER_TEMP_ISSUE QWTI LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QWTI.CONTRACTOR_ID,SITE S where  QWTI.site_id='"+ site_Id + "'  and   S.SITE_ID=QWTI.SITE_ID and QWTI.status in ('A','R','M') ");
		}
		
		List<WorkOrderBean> list = new ArrayList<WorkOrderBean>();
		list=jdbcTemplate.query(query.toString(), new ExtractWorkOrderData());
		return list;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used for selecting the temporary work order changed details
	 *  @return	 result of the insert or update operation 
	 */	
	@Override
	public List<WorkOrderBean> getDeletedProductDetailsLists(String workOrderNo, String user_id, int site_Id, String TypeOfWork) {
	
		String queryForWODetail ="select QWCD.REMARKS  from QS_WORKORDER_CHANGED_DTLS QWCD,QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QSTA,QS_WORKORDER_TEMP_ISSUE_DTLS DTLS,QS_WORKORDER_TEMP_ISSUE QWTI where QWCD.QS_WORKORDER_TEMP_ISSUE_AREA_DTLS_ID=QSTA.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID	and QWTI.QS_WO_TEMP_ISSUE_ID=DTLS.QS_WO_TEMP_ISSUE_ID and  DTLS.WO_WORK_TEMP_ISSUE_DTLS_ID=QSTA.WO_WORK_TEMP_ISSUE_DTLS_ID and QWTI.QS_WO_TEMP_ISSUE_ID=? order by QS_WO_CHANGED_DTLS_ID";
	
		if(TypeOfWork.equals("NMR")){
			queryForWODetail="select QWCD.REMARKS  from QS_WORKORDER_CHANGED_DTLS QWCD,QS_WORKORDER_TEMP_ISSUE_DTLS DTLS	where  QWCD.WO_WORK_TEMP_ISSUE_DTLS_ID=DTLS.WO_WORK_TEMP_ISSUE_DTLS_ID   and DTLS.QS_WO_TEMP_ISSUE_ID=?  order by QS_WO_CHANGED_DTLS_ID";
		}
		Object[] obj = {workOrderNo};

		List<WorkOrderBean> workData = jdbcTemplate.query(queryForWODetail, obj,
				new ResultSetExtractor<List<WorkOrderBean>>() {
				@Override
					public List<WorkOrderBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
						// For avoiding duplicate object use Set
						List<WorkOrderBean> workOrderList = new ArrayList<WorkOrderBean>();
						while (rs.next()) {
							WorkOrderBean bean = new WorkOrderBean();
							bean.setRemarks(rs.getString("REMARKS") == null ? "" : rs.getString("REMARKS").replace("@@@", ""));
							workOrderList.add(bean);
						}
						return workOrderList;
					}
				});
/*		StringBuffer queryForWOMaterialChangedDetail1=new  StringBuffer(" SELECT  QWQCD.REMARKS  FROM QS_WORKORDER_CHANGED_DTLS QWCD LEFT OUTER JOIN QS_WORKORDER_QTY_CHG_DTLS QWQCD ON QWQCD.QS_WO_CHANGED_DTLS_ID=QWCD.QS_WO_CHANGED_DTLS_ID,QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QSTA,QS_WORKORDER_TEMP_ISSUE_DTLS DTLS,QS_WORKORDER_TEMP_ISSUE QWTI") 
				.append(" WHERE QWCD.QS_WORKORDER_TEMP_ISSUE_AREA_DTLS_ID=QSTA.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID") 
				.append(" AND QWTI.QS_WO_TEMP_ISSUE_ID=DTLS.QS_WO_TEMP_ISSUE_ID ")
				.append(" AND  DTLS.WO_WORK_TEMP_ISSUE_DTLS_ID=QSTA.WO_WORK_TEMP_ISSUE_DTLS_ID ") 
				.append(" AND QWTI.QS_WO_TEMP_ISSUE_ID=? AND QWCD.RECORD_TYPE='MATERIAL' AND QWQCD.REMARKS IS NOT NULL  ORDER BY QWCD.QS_WO_CHANGED_DTLS_ID");
		IMP code
		List<Map<String, Object>> materialLogs= jdbcTemplate.queryForList(queryForWOMaterialChangedDetail1.toString(),workOrderNo);
		for (Map<String, Object> map : materialLogs) {
			String remarks=map.get("REMARKS")==null?"":map.get("REMARKS").toString();
			WorkOrderBean bean = new WorkOrderBean();
			bean.setRemarks(remarks);
			workData.add(bean);
		}*/
//		List<WorkOrderBean> workOrderList = new ArrayList<WorkOrderBean>(workData);
		return workData;
	}
	/**
	 *  @author Aniket Chavan
	 *  @description this method used for selecting the permanent work order's
	 *  @return	 result of the work order list
	 */	
	@Override
	public List<WorkOrderBean> getMyWOCompltedDetail(String fromDate, String toDate, String site_Id,
			String workOrderNumber, String typeOfWork, String contractorId) {
	
		final List<WorkOrderBean> list = new ArrayList<WorkOrderBean>();
		StringBuffer	query = new StringBuffer("SELECT  QWI.TOTAL_LABOUR_AMOUNT,QWI.TYPE_OF_WORK,QWI.WORK_ORDER_NAME,QWI.QS_WORKORDER_ISSUE_ID,QWI.CREATED_BY,to_char(QWI.CREATED_DATE,'dd-MM-yyyy hh24:mi:ss') as CREATED_DATE,SC.CONTRACTOR_ID,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME,WORK_ORDER_NUMBER,QWI.SITE_ID,QWI.STATUS,to_char(to_date(QWI.QS_WORKORDER_DATE ,'dd-MM-yy'),'dd-MM-yyyy') as QS_WORKORDER_DATE ")
				.append(" FROM QS_WORKORDER_ISSUE QWI LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC  ON   SC.CONTRACTOR_ID=QWI.CONTRACTOR_ID ,SITE S  WHERE QWI.STATUS IN ('A')  AND  QWI.SITE_ID=?  AND   S.SITE_ID=QWI.SITE_ID ");
		
		if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
			    query.append(" AND TRUNC(QWI.CREATED_DATE)  BETWEEN TO_DATE('" + fromDate + "','dd-MM-yy') AND TO_DATE('"+ toDate + "','dd-MM-yy')");
		} else if (StringUtils.isNotBlank(fromDate)) {
				query.append(" AND  TRUNC(QWI.CREATED_DATE) =TO_DATE('" + fromDate + "', 'dd-MM-yy')");
		} else if (StringUtils.isNotBlank(toDate)) {
			    query.append("AND  TRUNC(QWI.CREATED_DATE) <=TO_DATE('" + toDate + "', 'dd-MM-yy')");
		} else if (StringUtils.isNotBlank(workOrderNumber)) {
			    query.append(" AND  WORK_ORDER_NUMBER='" + workOrderNumber + "'");
		}else if (StringUtils.isNotBlank(contractorId)) {
			    query.append(" AND  QWI.CONTRACTOR_ID='" + contractorId + "'");
		}
		   		query.append(" Order by QWI.CREATED_DATE");
		jdbcTemplate.queryForList(query.toString(), new Object[]{site_Id});
		jdbcTemplate.query(query.toString(),new Object[]{site_Id}, new ResultSetExtractor<List<WorkOrderBean>>() {
			@Override
			public List<WorkOrderBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					WorkOrderBean bean = new WorkOrderBean();
					String typeOfWork=rs.getString("TYPE_OF_WORK") == null ? "-" : rs.getString("TYPE_OF_WORK");
					if(typeOfWork.equals("PIECEWORK")){
						typeOfWork="PIECE WORK";
					}
					bean.setTypeOfWork(typeOfWork);
					bean.setContractorName(rs.getString("CONTRACTOR_NAME") == null ? "-": rs.getString("CONTRACTOR_NAME"));
					bean.setWorkOrderName(rs.getString("WORK_ORDER_NAME") == null ? "-": rs.getString("WORK_ORDER_NAME"));
					bean.setQS_Temp_Issue_Id(rs.getString("QS_WORKORDER_ISSUE_ID") == null ? "0": rs.getString("QS_WORKORDER_ISSUE_ID"));
					bean.setWorkOrderDate(rs.getString("QS_WORKORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("QS_WORKORDER_DATE"));
					bean.setWorkOrderCreadeDate(rs.getString("QS_WORKORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("QS_WORKORDER_DATE"));
					bean.setContractorId(rs.getString("CONTRACTOR_ID") == null ? "0" : rs.getString("CONTRACTOR_ID"));
					String work_order_number = rs.getString("WORK_ORDER_NUMBER") == null ? "0": rs.getString("WORK_ORDER_NUMBER");
					bean.setWorkOrderNo(work_order_number);
					bean.setWorkOrderStatus(rs.getString("STATUS") == null ? "-" : rs.getString("STATUS"));
					bean.setSiteId(rs.getString("SITE_ID") == null ? "" : rs.getString("SITE_ID"));
					bean.setWorkOrderDate(rs.getString("CREATED_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("CREATED_DATE"));
					bean.setTotalWoAmount(rs.getString("TOTAL_LABOUR_AMOUNT")==null?"0":rs.getString("TOTAL_LABOUR_AMOUNT"));
					// sum(QCBT.CERTIFIED_AMOUNT) as CERTIFIED_AMOUNT, sum(QSDD.DEDUCTION_AMOUNT) as ADV_DEDUCTION, 
					if(typeOfWork.equals("PIECE WORK")){
						String subQuery="SELECT sum(QCBT.PAYBLE_AMOUNT) as PAYBLE_AMOUNT "
								+ " ,sum((SELECT sum(DEDUCTION_AMOUNT) FROM QS_BILL_DEDUCTION_DTLS  WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID and TYPE_OF_DEDUCTION!='ADV'  and  QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID)) as TOTAL_DEDUCTION_AMOUNT "
								+ " FROM QS_CONTRACTOR_BILL QCBT left join QS_BILL_DEDUCTION_DTLS QSDD on QSDD.CONTRACTOR_BILL_ID=QCBT.BILL_ID  and QSDD.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and QSDD.TYPE_OF_DEDUCTION='ADV' "
								+ " WHERE CONTRACTOR_ID='"+bean.getContractorId()+"'  AND PAYMENT_TYPE in('ADV','RA') AND QCBT.QS_WORKORDER_ISSUE_ID = '"+bean.getQS_Temp_Issue_Id()+"'  order by  QCBT.TEMP_BILL_ID";
						
						List<Map<String, Object>> billDetails=jdbcTemplate.queryForList(subQuery);
						for (Map<String, Object> billData : billDetails) {
							double woBillPaidAmount=billData.get("PAYBLE_AMOUNT")==null?0.0:Double.valueOf(billData.get("PAYBLE_AMOUNT").toString());
							double deductionAmount=billData.get("TOTAL_DEDUCTION_AMOUNT")==null?0.0:Double.valueOf(billData.get("TOTAL_DEDUCTION_AMOUNT").toString());
							double woBillBilledAmount=woBillPaidAmount+deductionAmount;
							bean.setWoBillBilledAmount(woBillBilledAmount);
							bean.setWoBillPaidAmount(woBillPaidAmount);
						}
					}else if(typeOfWork.equals("NMR")){
						String nmrSubQuery="SELECT sum(QCBT.CERTIFIED_AMOUNT) as CERTIFIED_AMOUNT,sum(QCBT.PAYBLE_AMOUNT) as PAYBLE_AMOUNT, sum(QCBT.DEDUCTION_AMOUNT) as TOTAL_DEDUCTION_AMOUNT "
								+ "FROM QS_CONTRACTOR_BILL QCBT "
								+ "WHERE CONTRACTOR_ID='"+bean.getContractorId()+"' AND PAYMENT_TYPE in('NMR') AND QCBT.QS_WORKORDER_ISSUE_ID = '"+bean.getQS_Temp_Issue_Id()+"'    order by  QCBT.TEMP_BILL_ID"; 
				
						List<Map<String, Object>> billDetails=jdbcTemplate.queryForList(nmrSubQuery);
						for (Map<String, Object> billData : billDetails) {
							double woBillPaidAmount=billData.get("PAYBLE_AMOUNT")==null?0.0:Double.valueOf(billData.get("PAYBLE_AMOUNT").toString());
							double deductionAmount=billData.get("TOTAL_DEDUCTION_AMOUNT")==null?0.0:Double.valueOf(billData.get("TOTAL_DEDUCTION_AMOUNT").toString());
							double woBillBilledAmount=woBillPaidAmount+deductionAmount;
							bean.setWoBillBilledAmount(woBillBilledAmount);
							bean.setWoBillPaidAmount(woBillPaidAmount);
						}
					}
			
					
					list.add(bean);
				}
				return null;
			}
		});
	
		return list;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used for selecting the permanent work order's for revise work order
	 *  @param site_Id used for selecting data site wise work order for revise work order 
	 *  @return	 result of the work order list
	 */	
	@Override
	public List<WorkOrderBean> getCompltedWorkOrderDetailForRevise(String fromDate, String toDate, String site_Id,
			String workOrderNumber, String typeOfWork, String contractorId) {
	
			final List<WorkOrderBean> list = new ArrayList<WorkOrderBean>();
			StringBuffer query = new StringBuffer("select  QWI.TOTAL_LABOUR_AMOUNT,QWI.TYPE_OF_WORK,QWI.WORK_ORDER_NAME,QWI.QS_WORKORDER_ISSUE_ID,QWI.CREATED_BY,to_char(QWI.CREATED_DATE,'dd-MM-yyyy') as CREATED_DATE,SC.CONTRACTOR_ID,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME,WORK_ORDER_NUMBER,QWI.SITE_ID,QWI.STATUS,to_char(to_date(QWI.QS_WORKORDER_DATE,'dd-MM-yy'),'dd-MM-yyyy') as QS_WORKORDER_DATE ")
					.append(" from QS_WORKORDER_ISSUE QWI LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QWI.CONTRACTOR_ID ")
					.append(" where QWI.site_id='"+site_Id+"'  and QWI.STATUS='A' ");
		
			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query.append(" AND TRUNC(QWI.CREATED_DATE)  BETWEEN TO_DATE('" + fromDate + "','dd-MM-yy') AND TO_DATE('"+ toDate + "','dd-MM-yy') ");
			} else if (StringUtils.isNotBlank(fromDate)) {
				query.append(" AND  TRUNC(QWI.CREATED_DATE) =TO_DATE('" + fromDate + "', 'dd-MM-yy')");
			} else if (StringUtils.isNotBlank(toDate)) {
				query.append(" AND  TRUNC(QWI.CREATED_DATE) <=TO_DATE('" + toDate + "', 'dd-MM-yy') ");
			} else if (StringUtils.isNotBlank(workOrderNumber)) {
				query.append(" AND  WORK_ORDER_NUMBER='" + workOrderNumber + "'");
			}else if (StringUtils.isNotBlank(contractorId)) {
				query.append(" AND  QWI.CONTRACTOR_ID='" + contractorId + "'");
			} 
				query.append(" AND QWI.TYPE_OF_WORK in ("+typeOfWork+") ");
	 
			final StringBuffer queryForLoadingPW_Billed_Paid_Amount=new StringBuffer("SELECT sum(QCBT.PAYBLE_AMOUNT) as PAYBLE_AMOUNT ")
					.append(" ,sum((SELECT sum(DEDUCTION_AMOUNT) FROM QS_BILL_DEDUCTION_DTLS  WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID and TYPE_OF_DEDUCTION!='ADV'  and  QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID)) as TOTAL_DEDUCTION_AMOUNT ")
					.append(" FROM QS_CONTRACTOR_BILL QCBT left join QS_BILL_DEDUCTION_DTLS QSDD on QSDD.CONTRACTOR_BILL_ID=QCBT.BILL_ID  and QSDD.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and QSDD.TYPE_OF_DEDUCTION='ADV' ")
					.append(" WHERE CONTRACTOR_ID=?  AND PAYMENT_TYPE in('ADV','RA') AND QCBT.QS_WORKORDER_ISSUE_ID = ?  order by  QCBT.TEMP_BILL_ID");

			final StringBuffer queryForLoadingNMR_Billed_Paid_Amount=new StringBuffer("SELECT sum(QCBT.CERTIFIED_AMOUNT) as CERTIFIED_AMOUNT,sum(QCBT.PAYBLE_AMOUNT) as PAYBLE_AMOUNT, sum(QCBT.DEDUCTION_AMOUNT) as TOTAL_DEDUCTION_AMOUNT ")
					.append(" FROM QS_CONTRACTOR_BILL QCBT ")
					.append(" WHERE CONTRACTOR_ID=? AND PAYMENT_TYPE in('NMR') AND QCBT.QS_WORKORDER_ISSUE_ID =?   order by  QCBT.TEMP_BILL_ID"); 
	
	
		jdbcTemplate.query(query.toString(), new ResultSetExtractor<List<WorkOrderBean>>() {
			@Override
			public List<WorkOrderBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					WorkOrderBean bean = new WorkOrderBean();
					//CONTRACTOR_NAME
					String typeOfWork=rs.getString("TYPE_OF_WORK") == null ? "-" : rs.getString("TYPE_OF_WORK");
					if(typeOfWork.equals("PIECEWORK")){
						typeOfWork="PIECE WORK";
					}
					bean.setTypeOfWork(typeOfWork);
					bean.setContractorName(rs.getString("CONTRACTOR_NAME") == null ? "": rs.getString("CONTRACTOR_NAME"));
					bean.setWorkOrderName(rs.getString("WORK_ORDER_NAME") == null ? "-": rs.getString("WORK_ORDER_NAME"));
					bean.setQS_Temp_Issue_Id(rs.getString("QS_WORKORDER_ISSUE_ID") == null ? "0": rs.getString("QS_WORKORDER_ISSUE_ID"));
					bean.setWorkOrderDate(rs.getString("QS_WORKORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("QS_WORKORDER_DATE"));
					bean.setWorkOrderCreadeDate(rs.getString("QS_WORKORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("QS_WORKORDER_DATE"));
					bean.setContractorId(rs.getString("CONTRACTOR_ID") == null ? "0" : rs.getString("CONTRACTOR_ID"));
					String work_order_number = rs.getString("WORK_ORDER_NUMBER") == null ? "0": rs.getString("WORK_ORDER_NUMBER");
					bean.setWorkOrderNo(work_order_number);
					bean.setWorkOrderStatus(rs.getString("STATUS") == null ? "-" : rs.getString("STATUS"));
					bean.setSiteId(rs.getString("SITE_ID") == null ? "" : rs.getString("SITE_ID"));
					bean.setWorkOrderDate(rs.getString("CREATED_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("CREATED_DATE"));
					bean.setTotalWoAmount(rs.getString("TOTAL_LABOUR_AMOUNT")==null?"0":rs.getString("TOTAL_LABOUR_AMOUNT"));
					 if(typeOfWork.equals("NMR")){
				
						List<Map<String, Object>> billDetails=jdbcTemplate.queryForList(queryForLoadingNMR_Billed_Paid_Amount.toString(),bean.getContractorId(),bean.getQS_Temp_Issue_Id());
						for (Map<String, Object> billData : billDetails) {
							double woBillPaidAmount=billData.get("PAYBLE_AMOUNT")==null?0.0:Double.valueOf(billData.get("PAYBLE_AMOUNT").toString());
							double deductionAmount=billData.get("TOTAL_DEDUCTION_AMOUNT")==null?0.0:Double.valueOf(billData.get("TOTAL_DEDUCTION_AMOUNT").toString());
							double woBillBilledAmount=woBillPaidAmount+deductionAmount;
							bean.setWoBillBilledAmount(woBillBilledAmount);
							bean.setWoBillPaidAmount(woBillPaidAmount);
						}
					}else{
						List<Map<String, Object>> billDetails=jdbcTemplate.queryForList(queryForLoadingPW_Billed_Paid_Amount.toString(),bean.getContractorId(),bean.getQS_Temp_Issue_Id());
						for (Map<String, Object> billData : billDetails) {
							double woBillPaidAmount=billData.get("PAYBLE_AMOUNT")==null?0.0:Double.valueOf(billData.get("PAYBLE_AMOUNT").toString());
							double deductionAmount=billData.get("TOTAL_DEDUCTION_AMOUNT")==null?0.0:Double.valueOf(billData.get("TOTAL_DEDUCTION_AMOUNT").toString());
							double woBillBilledAmount=woBillPaidAmount+deductionAmount;
							bean.setWoBillBilledAmount(woBillBilledAmount);
							bean.setWoBillPaidAmount(woBillPaidAmount);
						}
					}
					
					list.add(bean);
				}

				return null;
			}
		});
	
		return list;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used for selecting the permanent work order's by site wise
	 *  @param site_Id used for selecting site wise work order for revise work order 
	 *  @return	 result of the work order list
	 */	
	@Override
	public List<WorkOrderBean> getSitewiseWOCompltedDetail(String fromDate, String toDate, String site_Id,String workOrderNumber, String user_id, String contractorId) {
		final List<WorkOrderBean> list = new ArrayList<WorkOrderBean>();
		StringBuffer query = new StringBuffer("SELECT  QWI.TOTAL_LABOUR_AMOUNT,S.SITE_NAME,QWI.TYPE_OF_WORK,QWI.WORK_ORDER_NAME,QWI.QS_WORKORDER_ISSUE_ID,QWI.CREATED_BY,TO_CHAR(QWI.CREATED_DATE,'DD-MON-YY') AS CREATED_DATE,SC.CONTRACTOR_ID,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  AS CONTRACTOR_NAME,WORK_ORDER_NUMBER,QWI.SITE_ID,QWI.STATUS,QWI.QS_WORKORDER_DATE ")
				.append(" FROM QS_WORKORDER_ISSUE  QWI,SUMADHURA_CONTRACTOR SC,SITE S ")
				.append(" WHERE SC.CONTRACTOR_ID=QWI.CONTRACTOR_ID  AND QWI.STATUS='A' AND S.SITE_ID=QWI.SITE_ID"); 
			if(!(site_Id!=null&&site_Id.equals("ALL"))){
				query.append(" AND  QWI.SITE_ID='"+site_Id+"'");
			}
			
			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
			    query.append(" AND TRUNC(QWI.CREATED_DATE)  BETWEEN TO_DATE('" + fromDate + "','dd-MM-yy') AND TO_DATE('"+ toDate + "','dd-MM-yy')");
			} else if (StringUtils.isNotBlank(fromDate)) {
					query.append(" AND  TRUNC(QWI.CREATED_DATE) =TO_DATE('" + fromDate + "', 'dd-MM-yy')");
			} else if (StringUtils.isNotBlank(toDate)) {
				    query.append(" AND  TRUNC(QWI.CREATED_DATE) <=TO_DATE('" + toDate + "', 'dd-MM-yy')");
			} else if (StringUtils.isNotBlank(workOrderNumber)) {
				    query.append(" AND  WORK_ORDER_NUMBER='" + workOrderNumber + "'");
			}else if (StringUtils.isNotBlank(contractorId)) {
				    query.append(" AND  QWI.CONTRACTOR_ID='" + contractorId + "'");
			}
			
			final StringBuffer queryForLoadingPW_Billed_Paid_Amount=new StringBuffer("SELECT sum(QCBT.PAYBLE_AMOUNT) as PAYBLE_AMOUNT ")
					.append(" ,SUM((SELECT SUM(DEDUCTION_AMOUNT) FROM QS_BILL_DEDUCTION_DTLS  WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID AND TYPE_OF_DEDUCTION!='ADV'  AND  QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID)) AS TOTAL_DEDUCTION_AMOUNT ")
					.append(" FROM QS_CONTRACTOR_BILL QCBT LEFT JOIN QS_BILL_DEDUCTION_DTLS QSDD ON QSDD.CONTRACTOR_BILL_ID=QCBT.BILL_ID  AND QSDD.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID AND QSDD.TYPE_OF_DEDUCTION='ADV' ")
					.append(" WHERE CONTRACTOR_ID=?  AND PAYMENT_TYPE IN('ADV','RA') AND QCBT.QS_WORKORDER_ISSUE_ID = ?  ORDER BY  QCBT.TEMP_BILL_ID");
	
			final StringBuffer queryForLoadingNMR_Billed_Paid_Amount=new StringBuffer("SELECT sum(QCBT.CERTIFIED_AMOUNT) as CERTIFIED_AMOUNT,sum(QCBT.PAYBLE_AMOUNT) as PAYBLE_AMOUNT, sum(QCBT.DEDUCTION_AMOUNT) as TOTAL_DEDUCTION_AMOUNT ")
					.append(" FROM QS_CONTRACTOR_BILL QCBT ")
					.append(" WHERE CONTRACTOR_ID=? AND PAYMENT_TYPE IN('NMR') AND QCBT.QS_WORKORDER_ISSUE_ID =?   ORDER BY  QCBT.TEMP_BILL_ID"); 


		jdbcTemplate.query(query.toString(), new ResultSetExtractor<List<WorkOrderBean>>() {
			@Override
			public List<WorkOrderBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					WorkOrderBean bean = new WorkOrderBean();
					//CONTRACTOR_NAME
					String typeOfWork=rs.getString("TYPE_OF_WORK") == null ? "-" : rs.getString("TYPE_OF_WORK");
					if(typeOfWork.equals("PIECEWORK")){
						typeOfWork="PIECE WORK";
					}
					bean.setTypeOfWork(typeOfWork);
					bean.setContractorName(rs.getString("CONTRACTOR_NAME") == null ? "": rs.getString("CONTRACTOR_NAME"));
					bean.setWorkOrderName(rs.getString("WORK_ORDER_NAME") == null ? "": rs.getString("WORK_ORDER_NAME"));
					bean.setQS_Temp_Issue_Id(rs.getString("QS_WORKORDER_ISSUE_ID") == null ? "0": rs.getString("QS_WORKORDER_ISSUE_ID"));
					bean.setWorkOrderDate(rs.getString("QS_WORKORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("QS_WORKORDER_DATE"));
					bean.setWorkOrderCreadeDate(rs.getString("QS_WORKORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("QS_WORKORDER_DATE"));
					bean.setContractorId(rs.getString("CONTRACTOR_ID") == null ? "0" : rs.getString("CONTRACTOR_ID"));
					String work_order_number = rs.getString("WORK_ORDER_NUMBER") == null ? "0": rs.getString("WORK_ORDER_NUMBER");
					bean.setWorkOrderNo(work_order_number);
					bean.setWorkOrderStatus(rs.getString("STATUS") == null ? "-" : rs.getString("STATUS"));
					bean.setSiteId(rs.getString("SITE_ID") == null ? "" : rs.getString("SITE_ID"));
					bean.setSiteName(rs.getString("SITE_NAME") == null ? "" : rs.getString("SITE_NAME"));
					bean.setWorkOrderDate(rs.getString("CREATED_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("CREATED_DATE"));
					bean.setTotalWoAmount(rs.getString("TOTAL_LABOUR_AMOUNT")==null?"0":rs.getString("TOTAL_LABOUR_AMOUNT"));
					if(typeOfWork.equals("NMR")){//loading billed amount and paid amount of NMR work order
						List<Map<String, Object>> billDetails=jdbcTemplate.queryForList(queryForLoadingNMR_Billed_Paid_Amount.toString(),bean.getContractorId(),bean.getQS_Temp_Issue_Id());
						for (Map<String, Object> billData : billDetails) {
							double woBillPaidAmount=billData.get("PAYBLE_AMOUNT")==null?0.0:Double.valueOf(billData.get("PAYBLE_AMOUNT").toString());
							double deductionAmount=billData.get("TOTAL_DEDUCTION_AMOUNT")==null?0.0:Double.valueOf(billData.get("TOTAL_DEDUCTION_AMOUNT").toString());
							double woBillBilledAmount=woBillPaidAmount+deductionAmount;
							bean.setWoBillBilledAmount(woBillBilledAmount);
							bean.setWoBillPaidAmount(woBillPaidAmount);
						}
					}else{//loading billed amount and paid amount of piece work work order
						List<Map<String, Object>> billDetails=jdbcTemplate.queryForList(queryForLoadingPW_Billed_Paid_Amount.toString(),bean.getContractorId(),bean.getQS_Temp_Issue_Id());
						for (Map<String, Object> billData : billDetails) {
							double woBillPaidAmount=billData.get("PAYBLE_AMOUNT")==null?0.0:Double.valueOf(billData.get("PAYBLE_AMOUNT").toString());
							double deductionAmount=billData.get("TOTAL_DEDUCTION_AMOUNT")==null?0.0:Double.valueOf(billData.get("TOTAL_DEDUCTION_AMOUNT").toString());
							double woBillBilledAmount=woBillPaidAmount+deductionAmount;
							bean.setWoBillBilledAmount(woBillBilledAmount);
							bean.setWoBillPaidAmount(woBillPaidAmount);
						}
					} 
			 			list.add(bean);
				}
				return null;
			}
		});
		return list;
	}
	/**
	 *  @author Aniket Chavan
	 *  @description this method used for checking is the work order number is valid 
	 *  @param site_Id used for selecting site wise work order for revise work order 
	 *  @return	 result of the work order list
	 */	
	@Override
	public boolean checkCompletedWorkOrderNumberIsValidForEmployee(String siteWiseWorkOrderNo, String user_id,
			boolean status, String site_Id, String operType) {
		int count = 0;
		if (operType != null) {
			if (operType.equals("1")){
				count = jdbcTemplate.queryForInt("SELECT count(1) FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER =? and site_id=? ",siteWiseWorkOrderNo,site_Id);
			}else if(operType.equals("requestFromMail")){
				count = jdbcTemplate.queryForInt("SELECT count(1) FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER =? and site_id=? AND STATUS='A' ",siteWiseWorkOrderNo,site_Id);
			}else if(operType.equals("reviseWorkOrder")){
				count = jdbcTemplate.queryForInt("SELECT count(1) FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER =? and site_id=? AND STATUS='A' ",siteWiseWorkOrderNo,site_Id);
			}else if(operType.equals("modifyReviseWorkOrder")){
				count = jdbcTemplate.queryForInt("SELECT count(1) FROM QS_WORKORDER_TEMP_ISSUE WHERE TEMP_WORK_ORDER_NUMBER =? and site_id=? AND STATUS='M' ",siteWiseWorkOrderNo,site_Id);
			}else{
				count = jdbcTemplate.queryForInt("SELECT count(1) FROM QS_WORKORDER_ISSUE  WHERE WORK_ORDER_NUMBER =? and site_id=? and CREATED_BY=? ",siteWiseWorkOrderNo,site_Id,user_id);
			}
		} else{
		 	    count = jdbcTemplate.queryForInt("SELECT count(1) FROM QS_WORKORDER_ISSUE  WHERE WORK_ORDER_NUMBER =? and site_id=? and CREATED_BY=? ",siteWiseWorkOrderNo,site_Id,user_id);
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Map<String, Object> loadActiveWorkOrderDetails(WorkOrderBean bean) {	
		Map<String, Object> latestWorkOrderData=new HashMap<String, Object>();
		String[] str=bean.getWorkOrderNo().split("/");
		String 	workOrderNo=""+str[0]+"/"+str[1]+"/"+str[2]+"/"+str[3]+"";
		String queryForLoadingActiveWO="SELECT WORK_ORDER_NUMBER as WORK_ORDER_NUMBER,to_char(CREATED_DATE,'dd-MM-yyyy') as WORK_ORDER_CREATED_DATE,WORK_ORDER_NAME,TOTAL_LABOUR_AMOUNT,QS_WORKORDER_DATE as WORK_ORDER_DATE FROM QS_WORKORDER_ISSUE WHERE CONTRACTOR_ID=? AND SITE_ID=? AND WORK_ORDER_NUMBER LIKE concat('%', concat(?, '%')) AND STATUS='A' and ROWNUM=1";
		List<Map<String, Object>> list= jdbcTemplate.queryForList(queryForLoadingActiveWO,bean.getContractorId(),bean.getSiteId(),workOrderNo);
		if(list.size()==0){
			 queryForLoadingActiveWO="SELECT PER_WORK_ORDER_NUMBER as WORK_ORDER_NUMBER,to_char(CREATED_DATE,'dd-MM-yyyy') as WORK_ORDER_CREATED_DATE,WORK_ORDER_NAME,TOTAL_LABOUR_AMOUNT,WORK_ORDER_DATE as WORK_ORDER_DATE FROM QS_WORKORDER_TEMP_ISSUE WHERE CONTRACTOR_ID=? AND SITE_ID=? AND PER_WORK_ORDER_NUMBER LIKE  concat('%', concat(?, '%')) AND STATUS='A' and ROWNUM=1";
			 list= jdbcTemplate.queryForList(queryForLoadingActiveWO,bean.getContractorId(),bean.getSiteId(),workOrderNo);
		}
		for (Map<String, Object> map : list) {
			latestWorkOrderData=map;
		}
		return latestWorkOrderData;
		
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used for loading all the rows which are created while work order creation time
	 *  @param  workOrderIssueNo which is the holding the unique value used to select all the created rows of work order
	 *  @param user_id not in use
	 *  @param status not in use
	 *  @param operType not in use
	 *  @return	 result of the work order list
	 */	
	@Override
	public List<WorkOrderBean> getALLCompltedWorkOrderS(String workOrderIssueNo, String user_id, String operType,boolean status) {
		StringBuffer query =new StringBuffer("select QWTS.WO_ROW_CODE,QWTS.WO_WORK_ISSUE_DTLS_ID,QWTS.TOTAL_AMOUNT ,QWTS.CREATED_DATE ,QWTS.STATUS ,QWTS.WO_COMMENT ,QWTS.QS_WORKORDER_ISSUE_ID ,QWTS.WO_MANUAL_DESC ,QWTS.QUANTITY,QWM.WO_MAJORHEAD_ID ,QWM.WO_MAJORHEAD_DESC, QWMM.WO_MINORHEAD_ID ,QWMM.WO_MINORHEAD_DESC ,QWW.WO_WORK_DESC_ID ,QWW.WO_WORK_DESCRIPTION ,QWQM.WO_MEASURMEN_NAME,QWQM.WO_MEASURMENT_ID ")
				.append(" from  QS_WORKORDER_ISSUE_DETAILS QWTS ,QS_WO_MAJORHEAD QWM,QS_WO_MINORHEAD QWMM,QS_WO_WORKDESC QWW,QS_WO_MEASURMENT QWQM ")
				.append(" where QWTS.QS_WORKORDER_ISSUE_ID=?  and QWQM.WO_MEASURMENT_ID=QWTS.UOM and QWTS.WO_MINORHEAD_ID=QWMM.WO_MINORHEAD_ID    and QWTS.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID AND QWW.WO_MINORHEAD_ID=QWMM.WO_MINORHEAD_ID AND QWMM.WO_MAJORHEAD_ID=QWM.WO_MAJORHEAD_ID AND QWQM.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID  order by QWM.WO_MAJORHEAD_DESC,QWMM.WO_MINORHEAD_DESC,QWW.WO_WORK_DESCRIPTION  "); //order by QWM.WO_MAJORHEAD_DESC
			String[] params = {workOrderIssueNo};
		
			if(operType.equals("reviseWorkOrder")){
				query =new StringBuffer(" select  QWTS.WO_ROW_CODE,(select  RTRIM(XMLAGG(XMLELEMENT(E,concat(QIAWP.ALLOCATED_QTY,'##'||QIAWP.NO_OF_HOURS||'##'||QIAWP.RATE),'@@').EXTRACT('//text()') ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID).GetClobVal(),'@@')  from QS_INV_AGN_WORK_PMT_DTL_HR QIAWP,QS_CONTRACTOR_BILL QCB where QIAWP.ALLOCATED_QTY!=0 and  QCB.BILL_ID=QIAWP.BILL_ID ")
					.append(" AND QCB.QS_WORKORDER_ISSUE_ID=QWTS.QS_WORKORDER_ISSUE_ID AND QIAWP.QS_WORKORDER_ISSUE_ID=QWTS.QS_WORKORDER_ISSUE_ID AND QIAWP.WO_WORK_DESC_ID=QWTS.WO_WORK_DESC_ID    AND QIAWP.WO_MINORHEAD_ID=QWTS.WO_MINORHEAD_ID AND QIAWP.WO_ROW_CODE=QWTS.WO_ROW_CODE) as PAYMENTDONE ")
					.append(" ,QWTS.WO_WORK_ISSUE_DTLS_ID,QWTS.TOTAL_AMOUNT ,QWTS.CREATED_DATE ,QWTS.STATUS ,QWTS.WO_COMMENT ,QWTS.QS_WORKORDER_ISSUE_ID ,QWTS.WO_MANUAL_DESC ,QWTS.QUANTITY,QWM.WO_MAJORHEAD_ID ,QWM.WO_MAJORHEAD_DESC, QWMM.WO_MINORHEAD_ID ,QWMM.WO_MINORHEAD_DESC ,QWW.WO_WORK_DESC_ID ,QWW.WO_WORK_DESCRIPTION ,QWQM.WO_MEASURMEN_NAME,QWQM.WO_MEASURMENT_ID ")
					.append(" from  QS_WORKORDER_ISSUE_DETAILS QWTS ,QS_WO_MAJORHEAD QWM,QS_WO_MINORHEAD QWMM,QS_WO_WORKDESC QWW,QS_WO_MEASURMENT QWQM  ")
					.append(" where QWTS.QS_WORKORDER_ISSUE_ID=?  and QWQM.WO_MEASURMENT_ID=QWTS.UOM and QWTS.WO_MINORHEAD_ID=QWMM.WO_MINORHEAD_ID and QWTS.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID AND QWW.WO_MINORHEAD_ID=QWMM.WO_MINORHEAD_ID AND QWMM.WO_MAJORHEAD_ID=QWM.WO_MAJORHEAD_ID AND QWQM.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID  order by QWM.WO_MAJORHEAD_DESC,QWMM.WO_MINORHEAD_DESC,QWTS.WO_ROW_CODE,QWW.WO_WORK_DESCRIPTION");
			}
			
			List<WorkOrderBean> workData = jdbcTemplate.query(query.toString(), params, new ResultSetExtractor<List<WorkOrderBean>>() {

			@Override
			public List<WorkOrderBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<WorkOrderBean> workOrderList = new ArrayList<WorkOrderBean>();
				while (rs.next()) {
					WorkOrderBean bean = new WorkOrderBean();
					String temp_issue_dtls_id = rs.getString("WO_WORK_ISSUE_DTLS_ID");
					try {
						String paymentInitiated=rs.getString("PAYMENTDONE");	
						bean.setNmrPaymentDetails(paymentInitiated);
					} catch (Exception e) {
						log.info(" getALLCompltedWorkOrderS "+e.getMessage());
					}
					bean.setQS_Temp_Issue_Dtls_Id(temp_issue_dtls_id);
					bean.setWoRowCode(rs.getString("WO_ROW_CODE") == null ? "0.0": rs.getString("WO_ROW_CODE"));
					bean.setQS_Temp_Issue_Id(rs.getString("QS_WORKORDER_ISSUE_ID") == null ? "0.0": rs.getString("QS_WORKORDER_ISSUE_ID"));
					bean.setQuantity(rs.getString("QUANTITY") == null ? "": rs.getString("QUANTITY"));
					bean.setTotalAmount1(rs.getString("TOTAL_AMOUNT") == null ? "0.0" : rs.getString("TOTAL_AMOUNT"));
					bean.setComments1(rs.getString("WO_COMMENT") == null ? "" : rs.getString("WO_COMMENT") .replace("@@", " "));
					String manualDesc = rs.getString("WO_MANUAL_DESC") == null ? "" : rs.getString("WO_MANUAL_DESC");
					bean.setwOManualDescription(manualDesc);
					bean.setWoMajorHead(rs.getString("WO_MAJORHEAD_ID") == null ? "" : rs.getString("WO_MAJORHEAD_ID"));
					bean.setWoMinorHeads(rs.getString("WO_MINORHEAD_ID") == null ? "" : rs.getString("WO_MINORHEAD_ID"));
					bean.setwODescription(rs.getString("WO_WORK_DESC_ID") == null ? "" : rs.getString("WO_WORK_DESC_ID"));
					bean.setUnitsOfMeasurement(rs.getString("WO_MEASURMENT_ID") == null ? "" : rs.getString("WO_MEASURMENT_ID"));
					bean.setWO_MajorHead1(	rs.getString("WO_MAJORHEAD_DESC") == null ? "" : rs.getString("WO_MAJORHEAD_DESC"));
					bean.setWO_MinorHead1(	rs.getString("WO_MINORHEAD_DESC") == null ? "" : rs.getString("WO_MINORHEAD_DESC"));
					bean.setWO_Desc1(rs.getString("WO_WORK_DESCRIPTION") == null ? "" : rs.getString("WO_WORK_DESCRIPTION"));
					bean.setUnitsOfMeasurement1(rs.getString("WO_MEASURMEN_NAME") == null ? "" : rs.getString("WO_MEASURMEN_NAME").trim());
					workOrderList.add(bean);
				}
				return workOrderList;
			}
		});
		return workData;
	}

	/**
	 *  @author Aniket Chavan
	 *  @description this method used for loading all the rows which are created while work order creation time
	 *  @param  workOrderIssueNo which is the holding the unique value used to select all the created rows of work order
	 *  @param workOrderNo not in use
	 *  @return	 result of the work order list
	 */	
	@Override
	public WorkOrderBean getCompletedWorkOrderDetailsByWorkOrderNumber(String workOrderNo, String workOrderIssueNo) {
		StringBuffer queryForCompletedWorkOrder = new StringBuffer("SELECT QWTS.WO_RECORD_CONTAINS,QWTS.TOTAL_LABOUR_AMOUNT,QWTS.TOTAL_MATERIAL_AMOUNT,QWTS.TOTAL_WO_AMOUNT,QWTS.VERSION_NUMBER ,QWTS.REVISION,QWTS.OLD_WORK_ORDER_NUMBER,QWTS.BOQ_NO,QWTS.TYPE_OF_WORK,QWTS.WORK_ORDER_NAME,QWTS.QS_WORKORDER_ISSUE_ID,QWTS.WORK_ORDER_NUMBER,QWTS.CREATED_BY, S.SITE_NAME,S.SITE_ID,to_char(QWTS.CREATED_DATE,'dd-MM-yyyy') as WORK_ORDER_CREATED_DATE,to_char(to_date(QWTS.QS_WORKORDER_DATE,'dd-MM-yyyy'),'dd-MM-yyyy') as QS_WORKORDER_DATE,QWTS.CONTRACTOR_ID,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME,SC.ADDRESS,SC.GSTIN,SC.PAN_NUMBER,SC.MOBILE_NUMBER,SC.BANK_ACC_NUMBER,SC.BANK_NAME,SC.IFSC_CODE, ")
				.append(" ( SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QWTS.CREATED_BY and  QWTS.WORK_ORDER_NUMBER = '"+ workOrderNo+ "') as WORK_ORDER_CREATE_EMP_NAME  ")
				.append(" FROM QS_WORKORDER_ISSUE QWTS LEFT OUTER JOIN SUMADHURA_CONTRACTOR SC ON SC.CONTRACTOR_ID=QWTS.CONTRACTOR_ID, SITE S  ")
				.append(" WHERE QWTS.QS_WORKORDER_ISSUE_ID = ? AND S.SITE_ID = QWTS.SITE_ID  ");
		Object queryParams[]={workOrderIssueNo};
		final StringBuffer queryForLoadingWO_Billed_Paid_Amount=new StringBuffer("SELECT SUM(QCBT.CERTIFIED_AMOUNT) as CERTIFIED_AMOUNT,SUM(QCBT.PAYBLE_AMOUNT) as PAYBLE_AMOUNT ")
				.append(" ,SUM((SELECT SUM(DEDUCTION_AMOUNT) FROM QS_BILL_DEDUCTION_DTLS  WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID AND TYPE_OF_DEDUCTION!='ADV'  AND  QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID)) AS TOTAL_DEDUCTION_AMOUNT ")
				.append(" FROM QS_CONTRACTOR_BILL QCBT LEFT JOIN QS_BILL_DEDUCTION_DTLS QSDD ON QSDD.CONTRACTOR_BILL_ID=QCBT.BILL_ID  AND QSDD.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID AND QSDD.TYPE_OF_DEDUCTION='ADV' ")
				.append(" WHERE CONTRACTOR_ID=?  AND PAYMENT_TYPE IN('ADV','RA') AND QCBT.QS_WORKORDER_ISSUE_ID = ?  ORDER BY  QCBT.TEMP_BILL_ID");

		WorkOrderBean workOrderBean = jdbcTemplate.query(queryForCompletedWorkOrder.toString(),queryParams,new ResultSetExtractor<WorkOrderBean>() {
				@Override
				public WorkOrderBean extractData(ResultSet rs) throws SQLException, DataAccessException {
					WorkOrderBean bean = new WorkOrderBean();
					while (rs.next()) {
						bean.setBoqNo(rs.getString("BOQ_NO") == null ? "0": rs.getString("BOQ_NO"));
						bean.setTotalWoAmount(rs.getString("TOTAL_WO_AMOUNT") == null ? "0": rs.getString("TOTAL_WO_AMOUNT"));
						bean.setQS_Temp_Issue_Id(rs.getString("QS_WORKORDER_ISSUE_ID") == null ? "0": rs.getString("QS_WORKORDER_ISSUE_ID"));
						String work_order_number = rs.getString("WORK_ORDER_NUMBER") == null ? "0": rs.getString("WORK_ORDER_NUMBER");
						bean.setWoRecordContains(rs.getString("WO_RECORD_CONTAINS") == null ? "0": rs.getString("WO_RECORD_CONTAINS"));
						bean.setWorkOrderNo(work_order_number);
						bean.setSiteWiseWONO(work_order_number);
						bean.setWorkOrderName(rs.getString("WORK_ORDER_NAME") == null ? "": rs.getString("WORK_ORDER_NAME"));
						bean.setTypeOfWork(rs.getString("TYPE_OF_WORK") == null ? "0": rs.getString("TYPE_OF_WORK"));
						bean.setWorkorderFrom(rs.getString("CREATED_BY") == null ? "" : rs.getString("CREATED_BY"));
						bean.setSiteName(rs.getString("SITE_NAME") == null ? "" : rs.getString("SITE_NAME"));
						bean.setSiteId(rs.getString("SITE_ID") == null ? "" : rs.getString("SITE_ID"));
						bean.setWorkOrderDate(rs.getString("QS_WORKORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("QS_WORKORDER_DATE"));
						bean.setContractorId(rs.getString("CONTRACTOR_ID") == null ? "0" : rs.getString("CONTRACTOR_ID"));
						bean.setContractorName(rs.getString("CONTRACTOR_NAME"));
						bean.setContractorAddress(rs.getString("ADDRESS") == null ? "-" : rs.getString("ADDRESS"));
						bean.setContractorPanNo(rs.getString("PAN_NUMBER") == null ? "-" : rs.getString("PAN_NUMBER"));
						bean.setContractorPhoneNo(rs.getString("MOBILE_NUMBER") == null ? "-" : rs.getString("MOBILE_NUMBER"));
						bean.setGSTIN(rs.getString("GSTIN") == null ? "-" : rs.getString("GSTIN"));
						bean.setContractorBankName(rs.getString("BANK_NAME") == null ? "-" : rs.getString("BANK_NAME").toString());
						bean.setContractorBankAccNumber(rs.getString("BANK_ACC_NUMBER") == null ? "-" : rs.getString("BANK_ACC_NUMBER").toString());
						bean.setContractorIFSCCode(rs.getString("IFSC_CODE") == null ? "-" : rs.getString("IFSC_CODE").toString());
						bean.setRevision(rs.getString("REVISION") == null ? "" : rs.getString("REVISION"));
						bean.setVersionNumber(rs.getString("VERSION_NUMBER") == null ? "" : rs.getString("VERSION_NUMBER"));
						bean.setOldWorkOrderNo(rs.getString("OLD_WORK_ORDER_NUMBER") == null ? "" : rs.getString("OLD_WORK_ORDER_NUMBER"));
						bean.setMaterialWoAmount(rs.getString("TOTAL_MATERIAL_AMOUNT") == null ? "0" : rs.getString("TOTAL_MATERIAL_AMOUNT"));
						bean.setLaborWoAmount(rs.getString("TOTAL_LABOUR_AMOUNT") == null ? "0" : rs.getString("TOTAL_LABOUR_AMOUNT"));
						bean.setWorkOrderCreadeDate(rs.getString("WORK_ORDER_CREATED_DATE") == null ? "0000-00-00 00:00:00.000" : rs.getString("WORK_ORDER_CREATED_DATE"));
						List<Map<String, Object>> billDetails=jdbcTemplate.queryForList(queryForLoadingWO_Billed_Paid_Amount.toString(),bean.getContractorId(),bean.getQS_Temp_Issue_Id());
						for (Map<String, Object> billData : billDetails) {
								double woBillPaidAmount=billData.get("PAYBLE_AMOUNT")==null?0.0:Double.valueOf(billData.get("PAYBLE_AMOUNT").toString());
								double deductionAmount=billData.get("TOTAL_DEDUCTION_AMOUNT")==null?0.0:Double.valueOf(billData.get("TOTAL_DEDUCTION_AMOUNT").toString());
								double woBillBilledAmount=woBillPaidAmount+deductionAmount;
								bean.setWoBillBilledAmount(woBillBilledAmount);
								bean.setWoBillPaidAmount(woBillPaidAmount);
						}
					}
						return bean;
					}
				});
		
		String siteId=workOrderBean.getSiteId();
		String query2 = "SELECT SED.EMP_NAME,QWCAT.PURPOSE,TEMP_WORK_ORDER_NUMBER from  QS_WORKORDER_CRT_APPRL_DTLS QWCAT,SUMADHURA_EMPLOYEE_DETAILS SED,QS_WORKORDER_ISSUE QWTS where  SED.EMP_ID=QWCAT.WO_CREATE_APPROVE_EMP_ID and QWCAT.WO_WORK_ISSUE_ID=QWTS.QS_WORKORDER_ISSUE_ID and WO_WORK_ISSUE_ID=? and QWCAT.SITE_ID=? ORDER BY QS_WO_CRT_APPRL_DTLS_ID";
		List<Map<String, Object>> dbIndentDts2 = jdbcTemplate.queryForList(query2, workOrderIssueNo ,siteId);
		StringBuffer purposeView = new StringBuffer();
		for (Map<String, Object> prods2 : dbIndentDts2) {
			String empName = prods2.get("EMP_NAME") == null ? "" : prods2.get("EMP_NAME").toString();
			String purpose = prods2.get("PURPOSE") == null ? "" : prods2.get("PURPOSE").toString();
			purpose=purpose.trim();
			if(purpose.length()!=0)
				purposeView.append(empName+" - "+ purpose+"\n");
		}
		workOrderBean.setPurpose(purposeView.toString());
		return workOrderBean;
	}
	
	/**
	 *  @author Aniket Chavan
	 *  @description this method used for loading permanent work order issue id
	 *  @param site_Id used for selecting the work order issue id by site wise
	 *  @param workOrderNo used for selecting QS_WORKORDER_ISSUE_ID from QS_WORKORDER_ISSUE table
	 *  @param user_id not in use
	 *  @return	 work order issue id
	 */	
	@Override
	public String getPermanentWorkOrderIssueIdNumber(String workOrderNo, String site_Id, String user_id) {
		String query = "select QS_WORKORDER_ISSUE_ID from QS_WORKORDER_ISSUE where SITE_ID = ? AND WORK_ORDER_NUMBER = ?";
		String[] params = { site_Id, workOrderNo };
		String issueId = jdbcTemplate.queryForObject(query, params, String.class);
		return issueId;
	}

	/**
	 *  @author Aniket Chavan
	 *  @description this method used for inserting all the terms and condition's of the work order
	 * @param workOrderBean object is holding all the data that is going to use for insert operation
	 * @param workOrderTMPIssurPK this is the unique value of QS_WORKORDER_TEMP_ISSUE_DETAILS
	 * @param termsAndConditions is the array where all the terms and condition's are exist
	 *  @return	insert operation result
	 */	
	
	@Override
	public int insertTermsAndConditions(WorkOrderBean workOrderBean, final String workOrderTMPIssurPK, final String[] termsAndConditions) {
		final String contractorId=workOrderBean.getContractorId();
		
		String sql = "INSERT INTO QS_WORKORDER_TEMP_TERMS_COND (TERMS_CONDITION_ID, CONTRACTOR_ID,QS_WO_TEMP_ISSUE_ID,TERMS_CONDITION_DESC,ENTRY_DATE) VALUES (?,?,?,?,SYSDATE)";
		int result[]=jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
				
			@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					final int termsSequenceNumber=jdbcTemplate.queryForInt("SELECT QS_WO_TEMP_TERMS_CONDITIONS_SEQ.NEXTVAL FROM DUAL");					
					ps.setInt(1, termsSequenceNumber);
					ps.setString(2, contractorId);
					ps.setString(3, workOrderTMPIssurPK);
					ps.setString(4, termsAndConditions[i]);
				}			
				@Override
				public int getBatchSize() {
					return termsAndConditions.length;
				}
			});
		
		if(result.length!=0){
			return result.length;
		}
		return result.length;
	}
	
	/**
	 * @author Aniket Chavan
	 * @description this method used for inserting all the terms and conditions in permanent table
	 * @param workOrderBean object is holding all the data that is going to use for insert operation
	 * @param workOrderTMPIssurPK this is the unique value of QS_WORKORDER_TEMP_ISSUE_DETAILS
	 * @param termsAndConditions is the array where all the terms and condition's are exist
	 * @return	insert operation result
	 */	
     @Override
    public int insertPermanentTermsAndConditions(WorkOrderBean workOrderBean, final String workOrderTMPIssurPK, final String[] termsAndConditions) {
    	 final String contractorId=workOrderBean.getContractorId().trim();
    	 int deleteStatus=jdbcTemplate.update("delete from QS_WORKORDER_TERMS_COND where QS_WORKORDER_ISSUE_ID=?",workOrderTMPIssurPK);

 		String sql = "INSERT INTO QS_WORKORDER_TERMS_COND (TERMS_CONDITION_ID,CONTRACTOR_ID,QS_WORKORDER_ISSUE_ID,TERMS_CONDITION_DESC,ENTRY_DATE) VALUES (?, ?, ?,?,sysdate)";
 		int result[]=jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
 				
 			@Override
 				public void setValues(PreparedStatement ps, int i) throws SQLException {
 					final int termsSequenceNumber=jdbcTemplate.queryForInt("select QS_WO_TERMS_CONDITIONS_SEQ.NEXTVAL FROM DUAL");					
 					ps.setInt(1, termsSequenceNumber);
 					ps.setString(2, contractorId);
 					ps.setString(3, workOrderTMPIssurPK);
 					ps.setString(4, termsAndConditions[i]);
 				}			
 				@Override
 				public int getBatchSize() {
 					return termsAndConditions.length;
 				}
 			});
 		
 		if(result.length!=0){
 			return result.length;
 		}
 	 	return result.length;
    }
	
     /**
 	 *  @author Aniket Chavan
 	 *  @description loading all the created work order's terms and condition
 	 *  @param tempIssueId is the unique value of QS_WORKORDER_TEMP_ISSUE_DETAILS which is used to select te terms and condition's
 	 */	
	@Override
	public List<Map<String, Object>> loadTermsAndConditions(String tempWorkOrderIssueId,String operType) {
	// if operation type is permanent then select the data form permanent table 
		if(operType.equals("permanent")){
		 	String query="select TERMS_CONDITION_ID ,CONTRACTOR_ID ,QS_WORKORDER_ISSUE_ID ,TERMS_CONDITION_DESC ,ENTRY_DATE from QS_WORKORDER_TERMS_COND WTC where WTC.QS_WORKORDER_ISSUE_ID=?  and TERMS_CONDITION_DESC  is not null";
			List<Map<String, Object>> termsList=jdbcTemplate.queryForList(query,tempWorkOrderIssueId);	
			return termsList;
		}else{
			String query="select TERMS_CONDITION_ID ,CONTRACTOR_ID ,QS_WO_TEMP_ISSUE_ID ,TERMS_CONDITION_DESC ,ENTRY_DATE from QS_WORKORDER_TEMP_TERMS_COND WTC where WTC.QS_WO_TEMP_ISSUE_ID=? and TERMS_CONDITION_DESC  is not null";
			List<Map<String, Object>> termsList=jdbcTemplate.queryForList(query,tempWorkOrderIssueId);	
		return termsList;
		}
	}
	
	@Override
	public List<Map<String, Object>> getWorkOrderVerifiedEmpNames(WorkOrderBean bean) {
		String siteId=bean.getSiteId();
		String tempWONumber=bean.getSiteWiseWONO();
		if(bean.getApproverEmpId()!=null&&bean.getApproverEmpId().equals("END")&&tempWONumber.contains("/R")){
			tempWONumber=jdbcTemplate.queryForObject("SELECT TEMP_WORK_ORDER_NUMBER from QS_WORKORDER_TEMP_ISSUE where PER_WORK_ORDER_NUMBER=?", new Object[]{tempWONumber}, String.class);
		}
		Object[] obj={tempWONumber,siteId};
		StringBuffer queryForLoadingWOEmpNames=new StringBuffer("SELECT upper(SED.EMP_NAME) as EMP_NAME,SED.EMP_EMAIL,SED.EMP_ID,upper(SED.EMP_DESIGNATION) as EMP_DESIGNATION,QWCAD.OPERATION_TYPE,to_char(QWCAD.CREATION_DATE,'dd-MM-yyyy hh24:mi:ss') as CREATION_DATE ")
				.append(" FROM SUMADHURA_EMPLOYEE_DETAILS SED,QS_WORKORDER_CRT_APPRL_DTLS  QWCAD ")
				.append(" where  QWCAD.WO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and QWCAD.TEMP_WORK_ORDER_NUMBER = ? and QWCAD.SITE_ID=? and QWCAD.OPERATION_TYPE in ('C','A','R')")
				.append(" order by QS_WO_CRT_APPRL_DTLS_ID");
		log.info(" tempWONumber : "+tempWONumber+" siteId : "+siteId);
		List<Map<String, Object>> list=jdbcTemplate.queryForList(queryForLoadingWOEmpNames.toString(),obj);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getPermanentWorkOrderVerifiedEmpNames(WorkOrderBean bean) {
		String siteId=bean.getSiteId();
		String tempWONumber=bean.getQS_Temp_Issue_Id();
		Object[] obj={tempWONumber,siteId};
		StringBuffer query=new StringBuffer("SELECT upper(SED.EMP_NAME) as EMP_NAME,upper(SED.EMP_DESIGNATION) as EMP_DESIGNATION,QWCAD.OPERATION_TYPE,to_char(QWCAD.CREATION_DATE,'dd-MM-yyyy hh24:mi:ss') as CREATION_DATE ")
				.append(" FROM SUMADHURA_EMPLOYEE_DETAILS SED,QS_WORKORDER_CRT_APPRL_DTLS  QWCAD ")
				.append(" where  QWCAD.WO_CREATE_APPROVE_EMP_ID=SED.EMP_ID and QWCAD.WO_WORK_ISSUE_ID = ? and QWCAD.SITE_ID=?   and QWCAD.OPERATION_TYPE in ('C','A','R')")
				.append(" order by QS_WO_CRT_APPRL_DTLS_ID");
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query.toString(),obj);
		return list;
	}
	
	@Override
	public List<Map<String, Object>> getModificationDetailsList(String workOrderIssueNo, String typeOfWork) {
		StringBuffer queryForWODetail=null;
		if(typeOfWork.equals("NMR")){
			queryForWODetail=new StringBuffer("select QWCD.REMARKS  from QS_WORKORDER_CHANGED_DTLS QWCD,QS_WORKORDER_TEMP_ISSUE_DTLS DTLS	where  QWCD.WO_WORK_TEMP_ISSUE_DTLS_ID=DTLS.WO_WORK_TEMP_ISSUE_DTLS_ID   and DTLS.QS_WO_TEMP_ISSUE_ID=(select QS_WORKORDER_TEMP_ISSUE_ID from QS_WORKORDER_ISSUE where QS_WORKORDER_ISSUE_ID=?)");
		}else{
			queryForWODetail=new StringBuffer("select QWCD.REMARKS  from QS_WORKORDER_CHANGED_DTLS QWCD,QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QSTA,QS_WORKORDER_TEMP_ISSUE_DTLS DTLS,QS_WORKORDER_TEMP_ISSUE QWTI ")
				.append(" where QWCD.QS_WORKORDER_TEMP_ISSUE_AREA_DTLS_ID=QSTA.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID ")
				.append(" and QWTI.QS_WO_TEMP_ISSUE_ID=DTLS.QS_WO_TEMP_ISSUE_ID ")
				.append(" and  DTLS.WO_WORK_TEMP_ISSUE_DTLS_ID=QSTA.WO_WORK_TEMP_ISSUE_DTLS_ID ")
				.append(" and QWTI.QS_WO_TEMP_ISSUE_ID=(select QS_WORKORDER_TEMP_ISSUE_ID from QS_WORKORDER_ISSUE where QS_WORKORDER_ISSUE_ID=?) order by QS_WO_CHANGED_DTLS_ID");
		}
 		List<Map<String, Object>> list=jdbcTemplate.queryForList(queryForWODetail.toString(),workOrderIssueNo);
		
		queryForWODetail=new  StringBuffer(" select  QWQCD.REMARKS  from QS_WORKORDER_CHANGED_DTLS QWCD left outer join QS_WORKORDER_QTY_CHG_DTLS QWQCD on QWQCD.QS_WO_CHANGED_DTLS_ID=QWCD.QS_WO_CHANGED_DTLS_ID,QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QSTA,QS_WORKORDER_TEMP_ISSUE_DTLS DTLS,QS_WORKORDER_TEMP_ISSUE QWTI") 
						.append(" where QWCD.QS_WORKORDER_TEMP_ISSUE_AREA_DTLS_ID=QSTA.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID") 
						.append(" and QWTI.QS_WO_TEMP_ISSUE_ID=DTLS.QS_WO_TEMP_ISSUE_ID ")
						.append(" and  DTLS.WO_WORK_TEMP_ISSUE_DTLS_ID=QSTA.WO_WORK_TEMP_ISSUE_DTLS_ID ") 
						.append(" and QWTI.QS_WO_TEMP_ISSUE_ID=(select QS_WORKORDER_TEMP_ISSUE_ID from QS_WORKORDER_ISSUE where QS_WORKORDER_ISSUE_ID=?)  AND QWQCD.REMARKS is not null  order by QWCD.QS_WO_CHANGED_DTLS_ID");
		list.addAll(jdbcTemplate.queryForList(queryForWODetail.toString(),workOrderIssueNo));
		
		return list;
	}
	
	/**
	 * @description this method will return all the related work order version of current work order 
	 */
		@Override
		public List<WorkOrderBean> getAllRevisedWorkOrderList(WorkOrderBean workOrderBean, String typeOfWork) {
			final List<WorkOrderBean> list=new ArrayList<WorkOrderBean>();
			String[] str=workOrderBean.getWorkOrderNo().split("/");
			String workOrderNo="'%"+str[0]+"/"+str[1]+"/"+str[2]+"/"+str[3]+"%'";
			
			StringBuffer query=new StringBuffer("select QWI.TYPE_OF_WORK,QWI.WORK_ORDER_NAME,QWI.CONTRACTOR_ID,QWI.TOTAL_WO_AMOUNT,QWI.QS_WORKORDER_ISSUE_ID,QWI.CREATED_BY, ")
					.append(" to_char(QWI.CREATED_DATE,'dd-MM-yyyy hh:MM:ss AM') as CREATED_DATE, ")
					.append(" WORK_ORDER_NUMBER,QWI.SITE_ID,QWI.STATUS,QWI.QS_WORKORDER_DATE ")
					.append(" from QS_WORKORDER_ISSUE  QWI ")
					.append(" where QWI.SITE_ID=? AND QWI.CONTRACTOR_ID=? AND WORK_ORDER_NUMBER like "+workOrderNo+"  order by  QWI.QS_WORKORDER_ISSUE_ID ");
			Object[] params={workOrderBean.getSiteId(),workOrderBean.getContractorId().split(",")[0]};
			jdbcTemplate.query(query.toString(),params, new ResultSetExtractor<List<WorkOrderBean>>() {
				@Override
				public List<WorkOrderBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
					while (rs.next()) {
						WorkOrderBean bean = new WorkOrderBean();
						String typeOfWork=rs.getString("TYPE_OF_WORK") == null ? "-" : rs.getString("TYPE_OF_WORK");
						if(typeOfWork.equals("PIECEWORK")){
							typeOfWork="PIECE WORK";
						}
						bean.setTypeOfWork(typeOfWork);
						//bean.setContractorName(rs.getString("CONTRACTOR_NAME") == null ? "": rs.getString("CONTRACTOR_NAME"));
						bean.setWorkOrderName(rs.getString("WORK_ORDER_NAME") == null ? "-": rs.getString("WORK_ORDER_NAME"));
						bean.setQS_Temp_Issue_Id(rs.getString("QS_WORKORDER_ISSUE_ID") == null ? "0": rs.getString("QS_WORKORDER_ISSUE_ID"));
						bean.setWorkOrderDate(rs.getString("QS_WORKORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("QS_WORKORDER_DATE"));
						bean.setWorkOrderCreadeDate(rs.getString("QS_WORKORDER_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("QS_WORKORDER_DATE"));
						bean.setContractorId(rs.getString("CONTRACTOR_ID") == null ? "0" : rs.getString("CONTRACTOR_ID"));
						String work_order_number = rs.getString("WORK_ORDER_NUMBER") == null ? "0": rs.getString("WORK_ORDER_NUMBER");
						bean.setWorkOrderNo(work_order_number);
						bean.setWorkOrderStatus(rs.getString("STATUS") == null ? "-" : rs.getString("STATUS"));
						bean.setSiteId(rs.getString("SITE_ID") == null ? "" : rs.getString("SITE_ID"));
						bean.setWorkOrderDate(rs.getString("CREATED_DATE") == null ? "0000-00-00 00:00:00.000": rs.getString("CREATED_DATE"));
						bean.setTotalAmount(rs.getString("TOTAL_WO_AMOUNT") == null ? "0.00": rs.getString("TOTAL_WO_AMOUNT"));
						
						list.add(bean);
					}
					return null;
				}
			});
			return list;
		}

   /**
	* @author Aniket Chavan
	* @description updating the terms and condition's
	* @return result of delete operation
   */	
	@Override
	public int updateTheTermsAndCondition(String tempIssueId) {
		String query="delete from QS_WORKORDER_TEMP_TERMS_COND where QS_WO_TEMP_ISSUE_ID='"+tempIssueId+"'";
		int deleteStatus=jdbcTemplate.update(query);
		return deleteStatus;
	}

	 /**
 	 * @author Aniket Chavan
 	 * @description updating the purpose in QS_WORKORDER_CRT_APPRL_DTLS table
 	 * @param workOrderNoSiteWise used to update the temporary work order purpose
 	 * @return result of update operation
 	 */	
	@Override
	public int updatePurpose(String workOrderNoSiteWise, String purpose) {
	String query="UPDATE  QS_WORKORDER_CRT_APPRL_DTLS SET  PURPOSE=? where TEMP_WORK_ORDER_NUMBER=?";
	Object[] params={purpose,workOrderNoSiteWise};
	int result=jdbcTemplate.update(query,params);
		return result;
	}
	
	 /**
 	 * @author Aniket Chavan
 	 * @description updating the purpose in QS_WORKORDER_CRT_APPRL_DTLS table
 	 * @param workOrderNoSiteWise used to update the temporary work order purpose
 	 * @return result of update operation
 	 */	
	@Override
	public int updatePermanentWOPurpose(String tempIssueId, String purpose) {
		String query="UPDATE  QS_WORKORDER_CRT_APPRL_DTLS SET  PURPOSE=? where WO_WORK_ISSUE_ID=?";
		Object[] params={purpose,tempIssueId};
		int result=jdbcTemplate.update(query,params);
		return result;
	}
	
	/**
 	 *  @author Aniket Chavan
 	 *	@description here loading the work description id and name using the work order number and name
 	 *  @return result of select query 
 	 */	
	public Map<String,String> loadWorkDsc(ContractorQSBillBean billBean){
		List<Map<String, Object>> workDescList = null;
		Map<String,String> map = 	 new HashMap<String, String>();
		try{
			StringBuffer sql =new StringBuffer( "select distinct(QWW.WO_MAJORHEAD_ID),QWW.WO_MAJORHEAD_DESC from QS_WO_MAJORHEAD QWW,QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_DETAILS QWID ")
					.append(" WHERE QWI.WORK_ORDER_NUMBER=? AND QWI.QS_WORKORDER_ISSUE_ID=QWID.QS_WORKORDER_ISSUE_ID ")
					.append(" AND QWID.WO_MAJORHEAD_ID=QWW.WO_MAJORHEAD_ID AND SITE_ID=?");
			Object[] obj={billBean.getWorkOrderNo(),billBean.getSiteId()};
			workDescList =  jdbcTemplate.queryForList(sql.toString(),obj);
			if (workDescList.size() > 0) {
				for (Map workDtlsList : workDescList) {
					map.put(String.valueOf(workDtlsList.get("WO_MAJORHEAD_ID")), String.valueOf(workDtlsList.get("WO_MAJORHEAD_DESC")));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return map;
	}
	
	/**
 	 *  @author Aniket Chavan
 	 *	@description here loading the work order no for bill's
 	 *  @param strControctorId used to select all the work order
 	 *  @param strTypeOfWork is used to select the work order by type e.g NMR or PIECE WORK
 	 *  @return result of select query 
 	 */	
	@Override
	public String getWorkOrderDetails(String steWorkDescId,String strControctorId, int SiteId, String strTypeOfWork){
		StringBuffer sb = null;
		String totalLaborAmount="";
		String workOrderRecordContains="";
		List<Map<String, Object>> dbSubProductsList = null;		
		sb = new StringBuffer();
		StringBuffer subProdsQry = new StringBuffer("");
		boolean isTypeOfWorkContainsChar=strTypeOfWork.contains("@@");
		//if the strTypeOfWork contains the ("@@") symbol then select the work order for bill's using the contractor id
		//else select the work order number for indent issue
		if(isTypeOfWorkContainsChar){
			subProdsQry.append(" SELECT DISTINCT(WORK_ORDER_NUMBER),TOTAL_LABOUR_AMOUNT,TYPE_OF_WORK,WO_RECORD_CONTAINS FROM QS_WORKORDER_ISSUE WI ")
					   .append(" WHERE WI.CONTRACTOR_ID =?  AND WI.SITE_ID = ? AND WI.STATUS='A'");	
		}else{
			subProdsQry.append(" SELECT DISTINCT(WORK_ORDER_NUMBER),TOTAL_LABOUR_AMOUNT,TYPE_OF_WORK,WO_RECORD_CONTAINS FROM QS_WORKORDER_ISSUE WI  ")
					   .append(" WHERE   WI.CONTRACTOR_ID =?  AND WI.SITE_ID = ? AND WI.STATUS='A' AND WI.TYPE_OF_WORK IN ("+strTypeOfWork+")");	
		}
		dbSubProductsList = jdbcTemplate.queryForList(subProdsQry.toString(), new Object[]{strControctorId,SiteId});
		if(isTypeOfWorkContainsChar){
			for(Map<String, Object> subProds : dbSubProductsList) {
				totalLaborAmount=subProds.get("TOTAL_LABOUR_AMOUNT")==null?"":subProds.get("TOTAL_LABOUR_AMOUNT").toString();
				workOrderRecordContains=subProds.get("WO_RECORD_CONTAINS")==null?"":subProds.get("WO_RECORD_CONTAINS").toString()+"|";
				sb.append(subProds.get("WORK_ORDER_NUMBER").toString()+"_");
				sb.append(subProds.get("WORK_ORDER_NUMBER").toString()+"_");
				sb.append(totalLaborAmount+"_");
				sb.append(subProds.get("TYPE_OF_WORK").toString()+"_");
				sb.append(workOrderRecordContains);
			}
		}else{
			for(Map<String, Object> subProds : dbSubProductsList) {
				totalLaborAmount=subProds.get("TOTAL_LABOUR_AMOUNT")==null?"":subProds.get("TOTAL_LABOUR_AMOUNT").toString();
				sb.append(subProds.get("WORK_ORDER_NUMBER").toString()+"_"+subProds.get("WORK_ORDER_NUMBER").toString()+"_"+totalLaborAmount+"|");
			}
		}
		return sb.toString();
	}
	
	/**
	 * @description this method is for auto complete work order, it will give work order names to popup operation
	 * @param siteId
	 * @param strTypeOfWork
	 * @return
	 */
	@Override
	public List<Map<String, Object>> autoCompleteWorkOrderNo(int siteId, String strTypeOfWork, String workOrderNo) {
		log.info("WorkOrderDaoImpl.autoCompleteWorkOrderNo() SiteId : "+siteId+" Type Of Work : "+strTypeOfWork+" workOrderNo : "+workOrderNo);
		List<Map<String, Object>> dbSubProductsList = null;		
		StringBuffer subProdsQry = new StringBuffer("SELECT DISTINCT(WORK_ORDER_NUMBER) as WORK_ORDER_NUMBER,QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WI ")
		   .append(" WHERE WI.SITE_ID = ? AND TYPE_OF_WORK=? AND WI.STATUS='A' AND WORK_ORDER_NUMBER like upper(?) ");	
		dbSubProductsList = jdbcTemplate.queryForList(subProdsQry.toString(), new Object[]{siteId,strTypeOfWork,"%" + workOrderNo + "%"});
		return dbSubProductsList;
	}
	/*public static <K, V> Map<K, V> printMap(Map<K, V> map) {
		Map<K, V> mapObje = new TreeMap<K, V>();
		for (Map.Entry<K, V> entry : map.entrySet()) {
			mapObje.put( entry.getKey(), entry.getValue());

		}
		return mapObje;
	}*/

	/**
 	 *  @author Aniket Chavan
 	 *	@description deactivating the work order row which has been created while create work order so in next the row is removed from approval employee
 	 * 	@param tempIssueId used to update the record
 	 *  @return result of update query 
 	 */	
	@Override
	public int doInActiveWorkOder(String tempIssueId, String actualTotalAmt) {
		String query = "UPDATE QS_WORKORDER_TEMP_ISSUE_DTLS set STATUS = 'I' WHERE   WO_WORK_TEMP_ISSUE_DTLS_ID=?";
		int result = jdbcTemplate.update(query, tempIssueId);
		return result;
	}
	
	/**
 	 *  @author Aniket Chavan
 	 *	@description deactivating the work order row which has been created while create work order so in next the row is removed from approval employee
 	 * 	@param workOrderIssueId used to update the record
 	 *  @return result of update query 
 	 */	
	@Override
	public int doInActivePermanentWorkOder(String workOrderIssueId, String actualTotalAmt) {
		String query = "UPDATE QS_WORKORDER_ISSUE_DETAILS set STATUS = 'I' WHERE TOTAL_AMOUNT = ? and WO_WORK_ISSUE_DTLS_ID=?";
		int result = jdbcTemplate.update(query, actualTotalAmt, workOrderIssueId);
		return result;
	}

	/**
 	 *  @author Aniket Chavan
 	 *	@description this method is used for reallocating all the quantity of BOQ which has given to contractor
 	 * 	@param tempIssueId used to update the record
 	 *  @param isDelete variable holding the value that is saying this is reallocating operation or only select operation of the work area details
 	 *  @return result of update query 
 	 */	
	@Override
	public List<Map<String, Object>> getTempIssDTLSId(String tempIssueId, String actualTotalAmount, boolean isDelete) {
	
		StringBuffer query =new StringBuffer("select QWIA.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID,QWIA.WO_WORK_AREA_ID,QWIA.AREA_ALOCATED_QTY from ")
				.append(" QS_WORKORDER_TEMP_ISSUE_DTLS DTLS,QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QWIA ")
				.append(" WHERE DTLS.WO_WORK_TEMP_ISSUE_DTLS_ID=QWIA.WO_WORK_TEMP_ISSUE_DTLS_ID and ")
				.append("  DTLS.WO_WORK_TEMP_ISSUE_DTLS_ID=?");
		
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query.toString(), tempIssueId);
		for (Map<String, Object> map : result) {
			String workAreaId=map.get("WO_WORK_AREA_ID")==null?"":map.get("WO_WORK_AREA_ID").toString();
			String allocatedArea=map.get("AREA_ALOCATED_QTY")==null?"":map.get("AREA_ALOCATED_QTY").toString();
			String tempIssueId1=map.get("WO_WORK_TEMP_ISSUE_AREA_DTLS_ID")==null?"":map.get("WO_WORK_TEMP_ISSUE_AREA_DTLS_ID").toString();
			WorkOrderBean actualWorkOderDetail=new WorkOrderBean();
			actualWorkOderDetail.setSelectedArea(allocatedArea);
			actualWorkOderDetail.setWorkAreaId(workAreaId);
			actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueId1);
			if(isDelete){
				//reallocating BOQ work area quantity
				updateWorkAreaMapping(actualWorkOderDetail,"","deleted");
			}
		}
		return result;
	}
	
	/**
	 * this method is used to retrieve all the work area of particular work order and also
	 *  we can reallocate the quantity is row is deleted while approve work order
	 */
	@Override
	public List<Map<String, Object>> getPermanentTempIssDTLSId(String tempIssueId, String actualTotalAmount,boolean isDelete) {
		String query ="select WO_WORK_ISSUE_AREA_DTLS_ID,WO_WORK_AREA_ID,AREA_ALOCATED_QTY from  QS_WORKORDER_ISSUE_AREA_DETAILS  WHERE WO_WORK_ISSUE_DTLS_ID=?";
		List<Map<String, Object>> result = jdbcTemplate.queryForList(query, tempIssueId);
		for (Map<String, Object> map : result) {
			String workAreaId=map.get("WO_WORK_AREA_ID")==null?"":map.get("WO_WORK_AREA_ID").toString();
			String allocatedArea=map.get("AREA_ALOCATED_QTY")==null?"":map.get("AREA_ALOCATED_QTY").toString();
			String tempIssueId1=map.get("WO_WORK_ISSUE_AREA_DTLS_ID")==null?"":map.get("WO_WORK_ISSUE_AREA_DTLS_ID").toString();
			WorkOrderBean actualWorkOderDetail=new WorkOrderBean();
			actualWorkOderDetail.setSelectedArea(allocatedArea);
			actualWorkOderDetail.setWorkAreaId(workAreaId);
			actualWorkOderDetail.setQS_Temp_Issue_Dtls_Id(tempIssueId1);
			if(isDelete){
				//reallocating BOQ work area quantity
				updateWorkAreaMapping(actualWorkOderDetail,"","deleted");
			}
		}
		return result;
	}
	/**
 	 *  @author Aniket Chavan
 	 *  @description this method is used if any work order has been rejected so this work order number is going in reserved work order table
 	 *  so we can use that work order again because the work number is the permanent work order number
 	 *  @param bean object is having all the details to insert the data
 	 */	
	@Override
	public int insertIntoWOReservedNum(WorkOrderBean bean) {
		int result = 0;
		int count = 0;
		String query = "SELECT RESERVED_WO_NUMBER FROM QS_RESERVED_WO_NUMBER WHERE  SITE_ID=? and RESERVED_WO_NUMBER=? ";
		count = jdbcTemplate.update(query,bean.getSiteId(),bean.getWorkOrderNo());
		//here checking is this work order number already exists or not it exits then don't
		//insert the work order number again just update the status of the work order number
		if (count == 0) {
			query = "insert into QS_RESERVED_WO_NUMBER (QS_RESERVED_ID,RESERVED_WO_NUMBER,SITE_ID,TYPE_OF_WORK,STATUS) values(QS_RESERVED_WO_NUMBER_SEQ.NEXTVAL,?,?,?,'I')";
			Object[] params = { bean.getWorkOrderNo(),bean.getSiteId(),bean.getTypeOfWork()};
			result = jdbcTemplate.update(query, params);
		} else {
			//update work order status in QS_RESERVED_WO_NUMBER table if exits
			result = jdbcTemplate.update("UPDATE QS_RESERVED_WO_NUMBER SET STATUS='I' WHERE RESERVED_WO_NUMBER=? AND  SITE_ID=?", bean.getWorkOrderNo(),bean.getSiteId());
		}
		return result;
	}
	
	/**
 	 *  @author Aniket Chavan
 	 *  @description this method is used to load the NMR major head data by site wise
 	 *  @param data array is having all the details that is going to used for selecting NMR details
 	 */	
	@Override
	public Map<String, String> loadNMRProducts(String[] data) {
		String typeOfWork=data[0];
		String siteId=data[1];
		Map<String, String> nmr_majorheadList = new HashMap<String, String>();
 		StringBuffer queryForLoadingNMRProduct=new StringBuffer("select distinct(QWMJ.WO_MAJORHEAD_ID),WO_MAJORHEAD_DESC from QS_BOQ QB,QS_BOQ_DETAILS QBAM, QS_WO_WORKDESC QWD ,QS_WO_MINORHEAD QWM , QS_WO_MAJORHEAD QWMJ ")
			.append(" where QB.SITE_ID = ?  and QWD.WO_WORK_DESC_ID = QBAM.WO_WORK_DESC_ID and QB.BOQ_SEQ_NO=QBAM.BOQ_SEQ_NO AND QB.TYPE_OF_WORK=? AND ")
			.append(" QWD.WO_MINORHEAD_ID = QWM.WO_MINORHEAD_ID  and  QB.STATUS='A' and ")
			.append(" QWM.WO_MAJORHEAD_ID = QWMJ.WO_MAJORHEAD_ID order by WO_MAJORHEAD_ID");
		List<Map<String, Object>> list=	jdbcTemplate.queryForList(queryForLoadingNMRProduct.toString(),siteId,typeOfWork);
		for (Map<String, Object> prods : list) {
			nmr_majorheadList.put(String.valueOf(prods.get("WO_MAJORHEAD_ID")),
					String.valueOf(prods.get("WO_MAJORHEAD_DESC")));
		}
		return nmr_majorheadList;
	}

	@Override
	public int insertCompletedWorkOrder(WorkOrderBean workOrderBean, String workOrderTMPIssurPK, String siteWiseWONO,
			String nextApprovelEmpId) {
		// TODO Auto-generated method stub
		return 0;
	}
}
