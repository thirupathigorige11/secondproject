package com.sumadhura.transdao;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.BOQBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.MaterialDetails;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.bean.WDRateAnalysis;
import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.dto.BOQAreaMappingDto;
import com.sumadhura.dto.BOQDetailsDto;
import com.sumadhura.dto.BOQProductDetailsDto;
import com.sumadhura.dto.MultiObject;
import com.sumadhura.dto.ReviseBOQQtyChangedDtlsDto;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.UIProperties;

@Repository
public class BOQDaoImpl extends UIProperties implements BOQDao{
	
	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	@Override
	public String getMajorHeadId(String majorHeadDesc,String typeOfWork) {
		String query = "select WO_MAJORHEAD_ID from QS_WO_MAJORHEAD where lower(WO_MAJORHEAD_DESC) = '"+majorHeadDesc.toLowerCase()+"' and TYPE_OF_WORK = '"+typeOfWork+"' and STATUS='A'";
		String majorHeadId = null;
		try{
			majorHeadId = jdbcTemplate.queryForObject(query,String.class);
		}catch(EmptyResultDataAccessException e){}
		
		return majorHeadId;
		
	}

	@Override
	public String getMajorHeadTblSeqId() {
		String query = "select 'WOMAJ'||QS_WO_MAJORHEAD_SEQ.nextval from dual";
		String seqId = jdbcTemplate.queryForObject(query,String.class);
		return seqId;
	}

	@Override
	public int insertMajorHead(String majorHeadId, String majorHeadDesc, String user_id, String typeOfWork) {
		String query = " insert into QS_WO_MAJORHEAD (WO_MAJORHEAD_ID,WO_MAJORHEAD_DESC,CREATED_BY,CREATED_DATE,STATUS,TYPE_OF_WORK) "
					 + " values(?,?,?,sysdate,'A',?)";
		int count = jdbcTemplate.update(query,new Object[]{majorHeadId,majorHeadDesc,user_id,typeOfWork});
		return count;
	}

	@Override
	public String getMinorHeadId(String minorHeadDesc, String majorHeadId, String typeOfWork) {
		String query = "select WO_MINORHEAD_ID from QS_WO_MINORHEAD where lower(WO_MINORHEAD_DESC) = '"+minorHeadDesc.toLowerCase()+"' and WO_MAJORHEAD_ID = '"+majorHeadId+"' and TYPE_OF_WORK = '"+typeOfWork+"' and STATUS='A'";
		String minorHeadId = null;
		try{
			minorHeadId = jdbcTemplate.queryForObject(query,String.class);
		}catch(EmptyResultDataAccessException e){}
		
		return minorHeadId;
	}

	@Override
	public String getMinorHeadTblSeqId() {
		String query = "select 'WOMIN'||QS_WO_MINORHEAD_SEQ.nextval from dual";
		String seqId = jdbcTemplate.queryForObject(query,String.class);
		return seqId;
	}

	@Override
	public int insertMinorHead(String minorHeadId, String minorHeadDesc, String user_id, String majorHeadId,String typeOfWork) {
		String query = " insert into QS_WO_MINORHEAD (WO_MINORHEAD_ID,WO_MINORHEAD_DESC,WO_MAJORHEAD_ID,CREATED_BY,CREATED_DATE,STATUS,TYPE_OF_WORK) "
					 + " values (?,?,?,?,sysdate,'A',?)";
		int count = jdbcTemplate.update(query,new Object[]{minorHeadId,minorHeadDesc,majorHeadId,user_id,typeOfWork});
		return count;
	}

	@Override
	public String getWorkDescriptionId(String workDescription, String minorHeadId, String typeOfWork) {
		String query = "select WO_WORK_DESC_ID from QS_WO_WORKDESC where lower(WO_WORK_DESCRIPTION) = '"+workDescription.toLowerCase()+"' and WO_MINORHEAD_ID = '"+minorHeadId+"' and TYPE_OF_WORK = '"+typeOfWork+"' and STATUS='A'";
		String workDescriptionId = null;
		try{
			workDescriptionId = jdbcTemplate.queryForObject(query,String.class);
		}catch(EmptyResultDataAccessException e){}
		
		return workDescriptionId;
	}

	@Override
	public String getWorkDescriptionIdIndependentOnMinorHeadId(String workDescription, String minorHeadId, String typeOfWork) {
		String query = "select min(WO_WORK_DESC_ID) from QS_WO_WORKDESC where lower(WO_WORK_DESCRIPTION) = '"+workDescription.toLowerCase()+"' and TYPE_OF_WORK = '"+typeOfWork+"' and STATUS='A' ";
		
		String workDescriptionId = null;
		try{
			workDescriptionId = jdbcTemplate.queryForObject(query,String.class);
		}catch(EmptyResultDataAccessException e){}
		
		return workDescriptionId;
	}
	
	@Override
	public String getWorkDescriptionTblSeqId() {
		String query = "select 'WOWDESC'||QS_WO_WORKDESC_SEQ.nextval from dual";
		String seqId = jdbcTemplate.queryForObject(query,String.class);
		return seqId;
	}

	@Override
	public int insertWorkDescription(String workDescription, String workDescriptionId, String minorHeadId,String user_id,String levelOfWork,String typeOfWork) {
		String query = " insert into QS_WO_WORKDESC (WO_WORK_DESCRIPTION,WO_WORK_DESC_ID,WO_MINORHEAD_ID,CREATED_BY,CREATED_DATE,"
					 + " STATUS,LEVEL_OF_WORK,TYPE_OF_WORK) values (?,?,?,?,sysdate,'A',?,?)";
		int count = jdbcTemplate.update(query,new Object[]{workDescription,workDescriptionId,minorHeadId,user_id,levelOfWork,typeOfWork});
		return count;
	}

	@Override
	public String getmeasurementId(String measurementName, String workDescriptionId,String typeOfWork) {
		String query = "select WO_MEASURMENT_ID from QS_WO_MEASURMENT where lower(WO_MEASURMEN_NAME) = '"+measurementName.toLowerCase()+"' and WO_WORK_DESC_ID = '"+workDescriptionId+"' and TYPE_OF_WORK = '"+typeOfWork+"' and STATUS='A'";
		//System.out.println("WO_MEASURMEN_NAME = '"+measurementName.toLowerCase()+"'");
		String measurementId = null;
		try{
			measurementId = jdbcTemplate.queryForObject(query,String.class);
		}catch(EmptyResultDataAccessException e){}
		
		return measurementId;
	}

	@Override
	public String getMeasurementTblSeqId() {
		String query = "select 'WOUOM'||QS_WO_MEASURMENT_SEQ.nextval from dual";
		String seqId = jdbcTemplate.queryForObject(query,String.class);
		return seqId;
	}

	@Override
	public int insertMeasurement(String measurementId, String measurementName, String workDescriptionId,String user_id,String typeOfWork) {
		String query = " insert into QS_WO_MEASURMENT (WO_MEASURMENT_ID,WO_MEASURMEN_NAME,WO_WORK_DESC_ID,CREATED_BY,CREATED_DATE,STATUS,TYPE_OF_WORK) "
					 + " values (?,?,?,?,sysdate,'A',?)";
		int count = jdbcTemplate.update(query,new Object[]{measurementId,measurementName,workDescriptionId,user_id,typeOfWork});
		return count;
	}

	@Override
	public int getTempBOQNo() {
		String query = "select QS_TEMP_BOQ_SEQ.nextval  from dual";
		int seqId = jdbcTemplate.queryForInt(query);
		return seqId;
	}

	@Override
	public int insertQsTempBOQ(int tempBOQNo, String user_id, String site_id,String pendingEmpId,String typeOfWork,double nmrGrandTotal,String boqType) {
		String query = " insert into QS_TEMP_BOQ (STATUS,SITE_ID,CREATED_DATE,CREATED_BY,TEMP_BOQ_NO,PENDING_EMP_ID,TYPE_OF_WORK,BOQ_TOTAL_AMOUNT,BOQ_TYPE) "
					 + " values ('A',?,sysdate,?,?,?,?,?,?)";
		int count = jdbcTemplate.update(query,new Object[]{site_id,user_id,tempBOQNo,pendingEmpId,typeOfWork,nmrGrandTotal,boqType});
		return count;
	}

	@Override
	public int getTempBOQDetailsId() {
		String query = "select QS_TEMP_BOQ_DETAILS_SEQ.nextval  from dual";
		int seqId = jdbcTemplate.queryForInt(query);
		return seqId;
	}

	@Override
	public int insertQsTempBOQDetails(int tempBOQDetailsId, String workDescriptionId, String measurementId,double totalAreaOfAllBlocks, double totalAmountForAllBlocks, int tempBOQNo,String scopeOfWork,String minorHeadId,String Action,String recordType) {
		String query = " insert into QS_TEMP_BOQ_DETAILS (QS_TEMP_BOQ_DETAILS_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,WO_WORK_AREA,STATUS,TEMP_BOQ_NO,WO_WORK_AREA_AMOUNT,SCOPE_OF_WORK,WO_MINORHEAD_ID,ACTION,RECORD_TYPE) "
					 + " values(?,?,?,?,'A',?,?,?,?,?,?)";
		int count = jdbcTemplate.update(query,new Object[]{tempBOQDetailsId,workDescriptionId,measurementId,new DecimalFormat("##.##").format(totalAreaOfAllBlocks),tempBOQNo,new DecimalFormat("##.##").format(totalAmountForAllBlocks),scopeOfWork,minorHeadId,Action,recordType});
		return count;
	}
	@Override
	public int insertQsTempBOQDetails(BOQDetailsDto objBOQDetailsDto) {
		String query = " insert into QS_TEMP_BOQ_DETAILS (QS_TEMP_BOQ_DETAILS_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,WO_WORK_AREA,STATUS,TEMP_BOQ_NO,WO_WORK_AREA_AMOUNT,SCOPE_OF_WORK,WO_MINORHEAD_ID,ACTION,RECORD_TYPE) "
					 + " values(?,?,?,?,'A',?,?,?,?,?,?)";
		int count = jdbcTemplate.update(query,new Object[]{objBOQDetailsDto.getTempBOQDetailsId(),objBOQDetailsDto.getWorkDescriptionId(),objBOQDetailsDto.getMeasurementId(),objBOQDetailsDto.getTotalAreaOfAllBlocks(),objBOQDetailsDto.getTempBOQNo(),objBOQDetailsDto.getTotalAmountForAllBlocks(),objBOQDetailsDto.getScopeOfWork(),objBOQDetailsDto.getMinorHeadId(),objBOQDetailsDto.getAction(),objBOQDetailsDto.getRecordType()});
		return count;
	}
	

	@Override
	public String getBlockId(String blockName, String site_id) {
		String query = "select BLOCK_ID from BLOCK where lower(NAME) = '"+blockName.toLowerCase()+"' and SITE_ID = '"+site_id+"'";
		//System.out.println("Block Name = '"+blockName.toLowerCase()+"' and SITE_ID = '"+site_id+"'");
		String blockId = jdbcTemplate.queryForObject(query,String.class);
		return blockId;
	}

	@Override
	public String getWorkAreaSeqId() {
		String query = "select 'WOAREA'||QS_TEMP_BOQ_AREA_MAPPING_SEQ.nextval from dual";
		String seqId = jdbcTemplate.queryForObject(query,String.class);
		return seqId;
	}
	
	@Override
	public String getWorkAreaGroupSeqId() {
		String query = "select 'WWAG'||WO_WORK_AREA_GROUP_SEQ.nextval from dual";
		String seqId = jdbcTemplate.queryForObject(query,String.class);
		return seqId;
	}

	@Override
	public int insertQsTempBOQAreaMapping(String workAreaId, String workDescriptionId, String measurementId,String blockId, double blockArea, int tempBOQDetailsId, double blockAreaAmount, String floorId,double laborRatePerUnit,int tempBOQNo,String Action,String recordType,String workAreaGroupId) {
		String query = " insert into QS_TEMP_BOQ_AREA_MAPPING (WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,"
					 + " BLOCK_ID,WO_WORK_AREA,STATUS,QS_TEMP_BOQ_DETAILS_ID,FLOOR_ID,QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,TEMP_BOQ_NO,ACTION,RECORD_TYPE,WO_WORK_AREA_GROUP_ID) "
					 + " values (?,?,?,?,?,'A',?,?,?,?,?,?,?,?)";
		System.out.println("blockArea:"+blockArea+",blockAreaAmount:"+blockAreaAmount);
		int count = jdbcTemplate.update(query,new Object[]{ 
						workAreaId,workDescriptionId,measurementId,blockId,new DecimalFormat("##.##").format(blockArea),tempBOQDetailsId,floorId,new DecimalFormat("##.##").format(laborRatePerUnit),new DecimalFormat("##.##").format(blockAreaAmount),tempBOQNo,Action,recordType,workAreaGroupId});
		return count;
	}
	@Override
	public int insertQsTempBOQAreaMapping(BOQAreaMappingDto objBOQAreaMappingDto) {
		String query = " insert into QS_TEMP_BOQ_AREA_MAPPING (WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,"
					 + " BLOCK_ID,WO_WORK_AREA,STATUS,QS_TEMP_BOQ_DETAILS_ID,FLOOR_ID,QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,TEMP_BOQ_NO,ACTION,RECORD_TYPE,WO_WORK_AREA_GROUP_ID) "
					 + " values (?,?,?,?,?,'A',?,?,?,?,?,?,?,?)";
		System.out.println("blockArea:"+objBOQAreaMappingDto.getBlockArea()+",blockAreaAmount:"+objBOQAreaMappingDto.getBlockAreaAmount());
		int count = jdbcTemplate.update(query,new Object[]{ 
				objBOQAreaMappingDto.getWorkAreaId(),objBOQAreaMappingDto.getWorkDescriptionId(),objBOQAreaMappingDto.getMeasurementId(),objBOQAreaMappingDto.getBlockId(),objBOQAreaMappingDto.getBlockArea(),objBOQAreaMappingDto.getTempBOQDetailsId(),objBOQAreaMappingDto.getFloorId(),objBOQAreaMappingDto.getRatePerUnit(),objBOQAreaMappingDto.getBlockAreaAmount(),objBOQAreaMappingDto.getTempBOQNo(),objBOQAreaMappingDto.getAction(),objBOQAreaMappingDto.getRecordType(),objBOQAreaMappingDto.getWorkAreaGroupId()});
		return count;
	}
	@Override
	public int checkIsThisWorkIsAlreadyInPresentBoq(String workDescriptionId, String measurementId,String blockId, String floorId,String boqSiteId,int BOQSeqNo, String recordType) {
		String sql5 = "select count(*) from QS_BOQ_AREA_MAPPING "
				+ "where WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID =? and SITE_ID = ? and BOQ_SEQ_NO = ? "
				+ "and BLOCK_ID = ? and RECORD_TYPE = ? ";
		if(StringUtils.isBlank(floorId)){
			sql5 = sql5 + " and FLOOR_ID is null ";
		}else{
			sql5 = sql5 + " and FLOOR_ID = '"+floorId+"' ";
		}
		int count = jdbcTemplate.queryForInt(sql5,new Object[]{ 
						workDescriptionId,measurementId,boqSiteId,BOQSeqNo,blockId,recordType});
		return count;
	}

	@Override
	public String getFloorId(String floorName, String blockId) {
		String query = "select FLOOR_ID from FLOOR where lower(NAME) = '"+floorName.toLowerCase()+"' and BLOCK_ID = '"+blockId+"'";
		System.out.println("FLOOR_NAME = '"+floorName.toLowerCase()+"' and BLOCK_ID = '"+blockId+"'");
		String floorId = jdbcTemplate.queryForObject(query,String.class);
		return floorId;
	}

	@Override
	public List<BOQBean> getPendingForApprovalBOQList( String user_id,String siteId, boolean isViewTempBoq){
		
			List<BOQBean> list = new ArrayList<BOQBean>();
			List<Map<String, Object>> dbPendingBOQDtls = null;
			BOQBean objBOQBean =  null;
			String query ="";
			if(!isViewTempBoq){
			query = " select QTB.TEMP_BOQ_NO,QTB.BOQ_TYPE,S.SITE_NAME,QTB.SITE_ID,QTB.CREATED_DATE,SED.EMP_NAME,QTB.TYPE_OF_WORK from  QS_TEMP_BOQ QTB, SITE s,SUMADHURA_EMPLOYEE_DETAILS SED "+
                            " where QTB.SITE_ID = s.SITE_ID and SED.EMP_ID = QTB.CREATED_BY "+
                            " and QTB.PENDING_EMP_ID = ? AND QTB.STATUS='A'";
			dbPendingBOQDtls = jdbcTemplate.queryForList(query, new Object[] {user_id});
			}
			if(isViewTempBoq){
				query = " select QTB.TEMP_BOQ_NO,QTB.BOQ_TYPE,S.SITE_NAME,QTB.SITE_ID,QTB.CREATED_DATE,SED.EMP_NAME,QTB.TYPE_OF_WORK from  QS_TEMP_BOQ QTB, SITE s,SUMADHURA_EMPLOYEE_DETAILS SED "+
						" where QTB.SITE_ID = s.SITE_ID and SED.EMP_ID = QTB.CREATED_BY "+
						" AND QTB.STATUS='A' and QTB.SITE_ID =? ";
				dbPendingBOQDtls = jdbcTemplate.queryForList(query, new Object[] {siteId});
			}
			int sno = 0;
			for(Map<String, Object> dbPendingBOQDtlsList : dbPendingBOQDtls) {
				 objBOQBean = new BOQBean();
				 objBOQBean.setStrSiteId(dbPendingBOQDtlsList.get("SITE_ID")==null ? "0" :   dbPendingBOQDtlsList.get("SITE_ID").toString());
				 objBOQBean.setIntTempBOQNo(dbPendingBOQDtlsList.get("TEMP_BOQ_NO")==null ? 0 :   Integer.valueOf(dbPendingBOQDtlsList.get("TEMP_BOQ_NO").toString()));
				 objBOQBean.setStrSiteName(dbPendingBOQDtlsList.get("SITE_NAME")==null ? "" :   dbPendingBOQDtlsList.get("SITE_NAME").toString());
				 objBOQBean.setStrTemBOQCreatedDate(dbPendingBOQDtlsList.get("CREATED_DATE")==null ? "0" :   dbPendingBOQDtlsList.get("CREATED_DATE").toString());
				 objBOQBean.setStrTemBOQCreatedEmployeeName(dbPendingBOQDtlsList.get("EMP_NAME")==null ? "0" :   dbPendingBOQDtlsList.get("EMP_NAME").toString());
				 objBOQBean.setTypeOfWork(dbPendingBOQDtlsList.get("TYPE_OF_WORK")==null ? "" :   dbPendingBOQDtlsList.get("TYPE_OF_WORK").toString());
				 objBOQBean.setBoqType(dbPendingBOQDtlsList.get("BOQ_TYPE")==null ? "" :   dbPendingBOQDtlsList.get("BOQ_TYPE").toString());
				 objBOQBean.setStrSerialNumber(String.valueOf(++sno));
				 list.add(objBOQBean);
			}
			return list;
	}
	@Override
	public List<BOQBean> getSiteWisePendingForApprovalBOQList( String user_id,String siteId,boolean isViewTempBoq ){
		
			List<BOQBean> list = new ArrayList<BOQBean>();
			List<Map<String, Object>> dbPendingBOQDtls = null;
			BOQBean objBOQBean =  null;
			String query = "";
			if(!isViewTempBoq){
			query = " select QTB.TEMP_BOQ_NO,QTB.BOQ_TYPE,S.SITE_NAME,QTB.SITE_ID,QTB.CREATED_DATE,SED.EMP_NAME,QTB.TYPE_OF_WORK from  QS_TEMP_BOQ QTB, SITE s,SUMADHURA_EMPLOYEE_DETAILS SED "+
                            " where QTB.SITE_ID = s.SITE_ID and SED.EMP_ID = QTB.CREATED_BY "+
                            " and QTB.PENDING_EMP_ID = ? AND QTB.STATUS='A' and QTB.SITE_ID = ? ";
			
			dbPendingBOQDtls = jdbcTemplate.queryForList(query, new Object[] {user_id,siteId});
			}
			if(isViewTempBoq){
				query = " select QTB.TEMP_BOQ_NO,QTB.BOQ_TYPE,S.SITE_NAME,QTB.SITE_ID,QTB.CREATED_DATE,SED.EMP_NAME,QTB.TYPE_OF_WORK from  QS_TEMP_BOQ QTB, SITE s,SUMADHURA_EMPLOYEE_DETAILS SED "+
	                            " where QTB.SITE_ID = s.SITE_ID and SED.EMP_ID = QTB.CREATED_BY "+
	                            " AND QTB.STATUS='A' and QTB.SITE_ID = ? ";
				
				dbPendingBOQDtls = jdbcTemplate.queryForList(query, new Object[] {siteId});
			}
			int sno = 0;
			for(Map<String, Object> dbPendingBOQDtlsList : dbPendingBOQDtls) {
				 objBOQBean = new BOQBean();
				 objBOQBean.setStrSiteId(dbPendingBOQDtlsList.get("SITE_ID")==null ? "0" :   dbPendingBOQDtlsList.get("SITE_ID").toString());
				 objBOQBean.setIntTempBOQNo(dbPendingBOQDtlsList.get("TEMP_BOQ_NO")==null ? 0 :   Integer.valueOf(dbPendingBOQDtlsList.get("TEMP_BOQ_NO").toString()));
				 objBOQBean.setStrSiteName(dbPendingBOQDtlsList.get("SITE_NAME")==null ? "" :   dbPendingBOQDtlsList.get("SITE_NAME").toString());
				 objBOQBean.setStrTemBOQCreatedDate(dbPendingBOQDtlsList.get("CREATED_DATE")==null ? "0" :   dbPendingBOQDtlsList.get("CREATED_DATE").toString());
				 objBOQBean.setStrTemBOQCreatedEmployeeName(dbPendingBOQDtlsList.get("EMP_NAME")==null ? "0" :   dbPendingBOQDtlsList.get("EMP_NAME").toString());
				 objBOQBean.setTypeOfWork(dbPendingBOQDtlsList.get("TYPE_OF_WORK")==null ? "" :   dbPendingBOQDtlsList.get("TYPE_OF_WORK").toString());
				 objBOQBean.setBoqType(dbPendingBOQDtlsList.get("BOQ_TYPE")==null ? "" :   dbPendingBOQDtlsList.get("BOQ_TYPE").toString());
				 objBOQBean.setStrSerialNumber(String.valueOf(++sno));
				 list.add(objBOQBean);
			}
			return list;
	}
	
	@Override
	public BOQBean getBOQFromAndToDetails( BOQBean bean, String userId) {
		List<Map<String, Object>> dbIndentDts = null;
	//	String query = " select ((SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID =?)) as emp_name, SED.EMP_NAME as approver_name,SED.EMP_ID as approver_id FROM SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_APPROVER_MAPPING_DTL  SAMD WHERE SED.EMP_ID = SAMD.APPROVER_EMP_ID and SAMD.EMP_ID = ? AND SAMD.MODULE_TYPE='WOP'";
	//	Object[] obj = { userId, userId };
	String query = " select ((SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID =?)) as emp_name, SED.EMP_NAME as approver_name,SED.EMP_ID as approver_id FROM SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_APPROVER_MAPPING_DTL  SAMD WHERE SED.EMP_ID = SAMD.APPROVER_EMP_ID and SAMD.EMP_ID = ?  and  SAMD.SITE_ID=?   AND SAMD.MODULE_TYPE='BOQ'";
		Object[] obj = { userId, userId ,bean.getStrSiteId().trim()};
		dbIndentDts = jdbcTemplate.queryForList(query, obj);
		for (Map<String, Object> prods : dbIndentDts) {
			System.out.println("Mapping Details :: " + prods);
			bean.setStrApproverEmpId(prods.get("APPROVER_ID") == null ? "" : prods.get("APPROVER_ID").toString());
			bean.setStrBoqFrom(prods.get("EMP_NAME") == null ? "" : prods.get("EMP_NAME").toString());
			bean.setStrBoqTo(prods.get("APPROVER_NAME") == null ? "" : prods.get("APPROVER_NAME").toString());
		}
		String query1 = " select SED.EMP_ID,SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_APPROVER_MAPPING_DTL  SAMD WHERE SED.EMP_ID = SAMD.EMP_ID and SAMD.APPROVER_EMP_ID = ?  and  SAMD.SITE_ID=?   AND SAMD.MODULE_TYPE='BOQ'";
		Object[] obj1 = { userId ,bean.getStrSiteId().trim()};
		dbIndentDts = jdbcTemplate.queryForList(query1, obj1);
		for (Map<String, Object> prods : dbIndentDts) {
			System.out.println("Mapping Details :: " + prods);
			bean.setStrLowerEmpId(prods.get("EMP_ID") == null ? "" : prods.get("EMP_ID").toString());
			bean.setStrBoqFrom(prods.get("EMP_NAME") == null ? "" : prods.get("EMP_NAME").toString());
		}
		
		System.out.println(bean.getStrBoqFrom()+""+bean.getStrBoqTo());
		return bean;
	}
	@Override
	public String getBOQLevelComments(BOQBean bean) {
		String query="select PURPOSE from QS_BOQ_CRT_APPRL_DTLS where TEMP_BOQ_NO='"+bean.getIntTempBOQNo()+"'";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query);
		String str="";
		for (Map<String, Object> map : list) {
			String purpose=map.get("PURPOSE")==null?"":map.get("PURPOSE").toString();
			str+=purpose+"\n";
		}
		return str;
	}
	
	@Override
	public List<BOQBean> getBOQWorkDetails(String siteId,String BOQNumber, String typeOfWork){


	List<BOQBean> list = new ArrayList<BOQBean>();
	List<Map<String, Object>> dbBOQDtls = null;

	String query = "";
	if(typeOfWork.equals("PIECEWORK")){
	query = "select QBD.SCOPE_OF_WORK, QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME, "
			+ " QBAM.WO_WORK_AREA, B.NAME as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME,QBAM.QS_AREA_PRICE_PER_UNIT "
			+ " from QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD QSMINOR,  "
			+ "QS_TEMP_BOQ_AREA_MAPPING QBAM left outer join BLOCK B on B.BLOCK_ID = QBAM.BLOCK_ID "
			+ "left outer join FLOOR F on F.FLOOR_ID = QBAM.FLOOR_ID "
			+ "left outer join FLAT FLAT on FLAT.FLAT_ID = QBAM.FLAT_ID "
			 + "where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID "
			 + " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
			 + " QBAM.QS_TEMP_BOQ_DETAILS_ID = QBD.QS_TEMP_BOQ_DETAILS_ID and QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "
			 + "QB.TEMP_BOQ_NO = ? and QB.SITE_ID = ? and QBD.status='A' "
			 + " order by to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', ''))"; 
	}
	if(typeOfWork.equals("NMR")){
	query = "select QBD.SCOPE_OF_WORK, QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME"   
			+"  from QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD QSMINOR"  
			+"  where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID "
			+"  and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and" 
			+"  QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "
			+"  QB.TEMP_BOQ_NO = ? and QB.SITE_ID = ? and QBD.status='A'" 
			+"  order by to_number(QBD.QS_TEMP_BOQ_DETAILS_ID)";
	}
	
	dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {BOQNumber,siteId});
	int serialNumber=1;
	for(Map<String, Object> mapBOQDtls : dbBOQDtls) {
		BOQBean objBOQBean = new BOQBean();
		objBOQBean.setStrSerialNumber(String.valueOf(serialNumber));
		serialNumber++;
		objBOQBean.setStrMajorHeadDesc(mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" : mapBOQDtls.get("WO_MAJORHEAD_DESC").toString());
		objBOQBean.setStrMinorHeadDesc(mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" : mapBOQDtls.get("WO_MINORHEAD_DESC").toString());
		objBOQBean.setStrWorkDescription(mapBOQDtls.get("WO_WORK_DESCRIPTION")==null ? "" : mapBOQDtls.get("WO_WORK_DESCRIPTION").toString());
		objBOQBean.setStrMeasurementName(mapBOQDtls.get("WO_MEASURMEN_NAME")==null ? "" : mapBOQDtls.get("WO_MEASURMEN_NAME").toString());
		objBOQBean.setStrScopeOfWork(mapBOQDtls.get("SCOPE_OF_WORK")==null ? "" : mapBOQDtls.get("SCOPE_OF_WORK").toString());
		objBOQBean.setStrArea(mapBOQDtls.get("WO_WORK_AREA")==null ? "" : mapBOQDtls.get("WO_WORK_AREA").toString());
		objBOQBean.setStrBlock(mapBOQDtls.get("BLOCK_NAME")==null ? "" : mapBOQDtls.get("BLOCK_NAME").toString());
		objBOQBean.setStrFloor(mapBOQDtls.get("FLOOR_NAME")==null ? "" : mapBOQDtls.get("FLOOR_NAME").toString());
		objBOQBean.setStrFlat(mapBOQDtls.get("FLAT_NAME")==null ? "" : mapBOQDtls.get("FLAT_NAME").toString());
		objBOQBean.setDoubleLaborRatePerUnit(Double.valueOf(mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT")==null ? "0" : mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT").toString()));
		list.add(objBOQBean);
	}
	return list;
	}
	
	@Override
	public List<BOQBean> getReviseBOQWorkDetails(String siteId,String tempBOQNumber, String typeOfWork, String recordType, String majorHeadId){


	List<BOQBean> list = new ArrayList<BOQBean>();
	List<Map<String, Object>> dbBOQDtls = null;

	String query = "";
	if(typeOfWork.equals("PIECEWORK")){
		if(recordType.equals("LABOR")){
			query = "select QBAM.ACTION,QBD.SCOPE_OF_WORK, QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME, "
					+ " QBAM.WO_WORK_AREA, B.NAME as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME,QBAM.QS_AREA_PRICE_PER_UNIT "
					+ " from QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD QSMINOR,  "
					+ "QS_TEMP_BOQ_AREA_MAPPING QBAM left outer join BLOCK B on B.BLOCK_ID = QBAM.BLOCK_ID "
					+ "left outer join FLOOR F on F.FLOOR_ID = QBAM.FLOOR_ID "
					+ "left outer join FLAT FLAT on FLAT.FLAT_ID = QBAM.FLAT_ID "
					+ "where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID "
					+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
					+ " QBAM.QS_TEMP_BOQ_DETAILS_ID = QBD.QS_TEMP_BOQ_DETAILS_ID and QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "
					+ "QB.TEMP_BOQ_NO = ? and QB.SITE_ID = ? and QBD.status='A' "
					+ " REPLACE_WORD_FOR_RECORD_TYPE_CONDITION "
					+ " REPLACE_WORD_FOR_MAJORHEAD_ID_CONDITION "
					+ " order by to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', ''))"; 
		}
		if(recordType.equals("MATERIAL")){
			query = "select QBAM.ACTION,QBD.SCOPE_OF_WORK, QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME, "
					+ " QBAM.WO_WORK_AREA, B.NAME as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME,QBAM.QS_AREA_PRICE_PER_UNIT "
					+ ",PGM.MATERIAL_GROUP_NAME,PGM.MATERIAL_GROUP_MST_NAME,QBPD.PER_UNIT_QUANTITY,QBPD.PER_UNIT_AMOUNT,QBPD.ACTION as M_ACTION "

					+ " from QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD QSMINOR,  "
					+ " QS_TEMP_BOQ_PRODUCT_DTLS QBPD,PRODUCT_GROUP_MASTER PGM,QS_TEMP_BOQ_AREA_MAPPING QBAM left outer join BLOCK B on B.BLOCK_ID = QBAM.BLOCK_ID "
					+ "left outer join FLOOR F on F.FLOOR_ID = QBAM.FLOOR_ID "
					+ "left outer join FLAT FLAT on FLAT.FLAT_ID = QBAM.FLAT_ID "
					+ "where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID "
					+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
					+ " QBAM.QS_TEMP_BOQ_DETAILS_ID = QBD.QS_TEMP_BOQ_DETAILS_ID and QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "
					+ " QBAM.TEMP_BOQ_NO=QBPD.TEMP_BOQ_NO and QBAM.WO_WORK_AREA_ID=QBPD.WO_WORK_AREA_ID and "
					+ " QBPD.MATERIAL_GROUP_ID=PGM.MATERIAL_GROUP_ID and QBPD.MATERIAL_GROUP_MEASUREMENT_ID = PGM.MATERIAL_GROUP_MEASUREMENT_ID and "
					
					+ "QB.TEMP_BOQ_NO = ? and QB.SITE_ID = ? and QBD.status='A' "
					+ " REPLACE_WORD_FOR_RECORD_TYPE_CONDITION "
					+ " REPLACE_WORD_FOR_MAJORHEAD_ID_CONDITION "
					+ " order by to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', ''))"; 
		}
	}
	if(typeOfWork.equals("NMR")){
	query = "select QBD.SCOPE_OF_WORK, QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME"   
			+"  from QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD QSMINOR"  
			+"  where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID "
			+"  and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and" 
			+"  QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "
			+"  QB.TEMP_BOQ_NO = ? and QB.SITE_ID = ? and QBD.status='A'" 
			+"  order by to_number(QBD.QS_TEMP_BOQ_DETAILS_ID)";
	}
	
	
	if(StringUtils.isNotBlank(recordType)){
		query = query.replace("REPLACE_WORD_FOR_RECORD_TYPE_CONDITION", " and QBAM.RECORD_TYPE='"+recordType+"' ");
	}else { 
		query = query.replace("REPLACE_WORD_FOR_RECORD_TYPE_CONDITION", "");
	} 
	if(StringUtils.isNotBlank(majorHeadId)){
		query = query.replace("REPLACE_WORD_FOR_MAJORHEAD_ID_CONDITION", " and QWM.WO_MAJORHEAD_ID='"+majorHeadId+"' ");
	}else{
		query = query.replace("REPLACE_WORD_FOR_MAJORHEAD_ID_CONDITION", "");
	}
	
	dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {tempBOQNumber,siteId});
	int serialNumber=1;
	for(Map<String, Object> mapBOQDtls : dbBOQDtls) {
		BOQBean objBOQBean = new BOQBean();
		objBOQBean.setStrSerialNumber(String.valueOf(serialNumber));
		serialNumber++;
		objBOQBean.setStrMajorHeadDesc(mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" : mapBOQDtls.get("WO_MAJORHEAD_DESC").toString());
		objBOQBean.setStrMinorHeadDesc(mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" : mapBOQDtls.get("WO_MINORHEAD_DESC").toString());
		objBOQBean.setStrWorkDescription(mapBOQDtls.get("WO_WORK_DESCRIPTION")==null ? "" : mapBOQDtls.get("WO_WORK_DESCRIPTION").toString());
		objBOQBean.setStrMeasurementName(mapBOQDtls.get("WO_MEASURMEN_NAME")==null ? "" : mapBOQDtls.get("WO_MEASURMEN_NAME").toString());
		objBOQBean.setStrScopeOfWork(mapBOQDtls.get("SCOPE_OF_WORK")==null ? "" : mapBOQDtls.get("SCOPE_OF_WORK").toString());
		objBOQBean.setStrArea(mapBOQDtls.get("WO_WORK_AREA")==null ? "" : mapBOQDtls.get("WO_WORK_AREA").toString());
		objBOQBean.setStrBlock(mapBOQDtls.get("BLOCK_NAME")==null ? "" : mapBOQDtls.get("BLOCK_NAME").toString());
		objBOQBean.setStrFloor(mapBOQDtls.get("FLOOR_NAME")==null ? "" : mapBOQDtls.get("FLOOR_NAME").toString());
		objBOQBean.setStrFlat(mapBOQDtls.get("FLAT_NAME")==null ? "" : mapBOQDtls.get("FLAT_NAME").toString());
		objBOQBean.setDoubleLaborRatePerUnit(Double.valueOf(mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT")==null ? "0" : mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT").toString()));
		objBOQBean.setAction(mapBOQDtls.get("ACTION")==null ? "" : mapBOQDtls.get("ACTION").toString());
		
		if(recordType.equals("MATERIAL")){
		objBOQBean.setMaterialGroupName(mapBOQDtls.get("MATERIAL_GROUP_NAME")==null ? "-" :   mapBOQDtls.get("MATERIAL_GROUP_NAME").toString());
		objBOQBean.setMaterialGroupUOM(mapBOQDtls.get("MATERIAL_GROUP_MST_NAME")==null ? "-" :   mapBOQDtls.get("MATERIAL_GROUP_MST_NAME").toString());
		objBOQBean.setMaterialPerUnitQuantity(mapBOQDtls.get("PER_UNIT_QUANTITY")==null ? "-" :   mapBOQDtls.get("PER_UNIT_QUANTITY").toString());
		objBOQBean.setMaterialPerUnitAmount(mapBOQDtls.get("PER_UNIT_AMOUNT")==null ? "-" :   mapBOQDtls.get("PER_UNIT_AMOUNT").toString());
		objBOQBean.setMaterialAction(mapBOQDtls.get("M_ACTION")==null ? "" : mapBOQDtls.get("M_ACTION").toString());
		}
		
		list.add(objBOQBean);
	}
	return list;
	}
	
	@Override
	public List<BOQBean> getReviseBOQChangedWorkDetails(String siteId,String BOQNumber, String typeOfWork, String recordType,String majorHeadId){


	List<BOQBean> list = new ArrayList<BOQBean>();
	List<Map<String, Object>> dbBOQDtls = null;

	String query = "";
	if(typeOfWork.equals("PIECEWORK")){
		if(recordType.equals("LABOR")){
			query = " select  QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME," 
					+"  B.NAME as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME,"
					+"  QRBCD.OLD_WO_WORK_AREA,QRBCD.NEW_WO_WORK_AREA,QRBCD.OLD_QS_AREA_PRICE,QRBCD.NEW_QS_AREA_PRICE,QRBCD.ACTION "  
					+"  from QS_WO_MAJORHEAD QWM,QS_WO_MINORHEAD QSMINOR, QS_WO_WORKDESC QWD,QS_WO_MEASURMENT QUOM, "
					+"  QS_REVISE_BOQ_CHANGED_DTLS QRBCD left outer join BLOCK B on B.BLOCK_ID = QRBCD.BLOCK_ID "
					+"  left outer join FLOOR F on F.FLOOR_ID = QRBCD.FLOOR_ID "
					+"  left outer join FLAT FLAT on FLAT.FLAT_ID = QRBCD.FLAT_ID " 
					+"  where QRBCD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID " 
					+"  and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and QRBCD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and " 

			+"  QRBCD.TEMP_BOQ_NO = ? and QRBCD.SITE_ID = ?  and "
			+ " (QRBCD.ACTION = 'EDIT' or QRBCD.ACTION = 'DEL' or QRBCD.ACTION = 'NEW')"
			+ " REPLACE_WORD_FOR_RECORD_TYPE_CONDITION "
			+ " REPLACE_WORD_FOR_MAJORHEAD_ID_CONDITION "
			+"  order by to_number(regexp_replace(QRBCD.WO_WORK_AREA_ID, '[^0-9]', ''))"; 
		}
		if(recordType.equals("MATERIAL")){
			query = " select  QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME," 
					+"  B.NAME as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME,"
					+"  QRBCD.OLD_WO_WORK_AREA,QRBCD.NEW_WO_WORK_AREA,QRBCD.OLD_QS_AREA_PRICE,QRBCD.NEW_QS_AREA_PRICE,QRBCD.ACTION "  
					+ ",PGM.MATERIAL_GROUP_NAME,PGM.MATERIAL_GROUP_MST_NAME,QRBQCD.OLD_PER_UNIT_QUANTITY,QRBQCD.NEW_PER_UNIT_QUANTITY,QRBQCD.OLD_PER_UNIT_AMOUNT,QRBQCD.NEW_PER_UNIT_AMOUNT,QRBQCD.ACTION as M_ACTION "

					
					+"  from QS_WO_MAJORHEAD QWM,QS_WO_MINORHEAD QSMINOR, QS_WO_WORKDESC QWD,QS_WO_MEASURMENT QUOM, "
					+"  QS_REVISE_BOQ_CHANGED_DTLS QRBCD left outer join BLOCK B on B.BLOCK_ID = QRBCD.BLOCK_ID "
					+"  left outer join "
					+ " QS_REV_BOQ_QTY_CHG_DTLS QRBQCD "+" left outer join PRODUCT_GROUP_MASTER PGM on QRBQCD.MATERIAL_GROUP_ID=PGM.MATERIAL_GROUP_ID and QRBQCD.MATERIAL_GROUP_MEASUREMENT_ID = PGM.MATERIAL_GROUP_MEASUREMENT_ID "
					+"  on QRBQCD.REV_BOQ_CHANGED_DTLS_ID=QRBCD.REV_BOQ_CHANGED_DTLS_ID "
					+"  left outer join FLOOR F on F.FLOOR_ID = QRBCD.FLOOR_ID "
					+"  left outer join FLAT FLAT on FLAT.FLAT_ID = QRBCD.FLAT_ID " 
					+"  where QRBCD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID " 
					+"  and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and QRBCD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and " 
					
					
					
			+"  QRBCD.TEMP_BOQ_NO = ? and QRBCD.SITE_ID = ?  and "
			+ " (QRBCD.ACTION = 'EDIT' or QRBCD.ACTION = 'DEL' or QRBCD.ACTION = 'NEW')"
			+ " REPLACE_WORD_FOR_RECORD_TYPE_CONDITION "
			+ " REPLACE_WORD_FOR_MAJORHEAD_ID_CONDITION "
			+"  order by to_number(regexp_replace(QRBCD.WO_WORK_AREA_ID, '[^0-9]', ''))"; 
		}
	}
	/*if(typeOfWork.equals("NMR")){
	query = "select QBD.SCOPE_OF_WORK, QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME"   
			+"  from QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD QSMINOR"  
			+"  where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID "
			+"  and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and" 
			+"  QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "
			+"  QB.TEMP_BOQ_NO = ? and QB.SITE_ID = ? and QBD.status='A'" 
			+"  order by to_number(QBD.QS_TEMP_BOQ_DETAILS_ID)";
	}*/
	
	if(StringUtils.isNotBlank(recordType)){
		query = query.replace("REPLACE_WORD_FOR_RECORD_TYPE_CONDITION", " and QRBCD.RECORD_TYPE='"+recordType+"' ");
	}else { 
		query = query.replace("REPLACE_WORD_FOR_RECORD_TYPE_CONDITION", "");
	} 
	if(StringUtils.isNotBlank(majorHeadId)){
		query = query.replace("REPLACE_WORD_FOR_MAJORHEAD_ID_CONDITION", " and QWM.WO_MAJORHEAD_ID='"+majorHeadId+"' ");
	}else{
		query = query.replace("REPLACE_WORD_FOR_MAJORHEAD_ID_CONDITION", "");
	}
	
	dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {BOQNumber,siteId});
	int serialNumber=1;
	HashSet<String> DuplicateCheck = new HashSet<String>();
	for(Map<String, Object> mapBOQDtls : dbBOQDtls) {
		BOQBean objBOQBean = new BOQBean();
		objBOQBean.setStrSerialNumber(String.valueOf(serialNumber));
		serialNumber++;
		String majorHead = mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" : mapBOQDtls.get("WO_MAJORHEAD_DESC").toString();
		String minorHead = mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" : mapBOQDtls.get("WO_MINORHEAD_DESC").toString();
		String workDescription = mapBOQDtls.get("WO_WORK_DESCRIPTION")==null ? "" : mapBOQDtls.get("WO_WORK_DESCRIPTION").toString();
		String measurement = mapBOQDtls.get("WO_MEASURMEN_NAME")==null ? "" : mapBOQDtls.get("WO_MEASURMEN_NAME").toString();
		String blockName = mapBOQDtls.get("BLOCK_NAME")==null ? "" : mapBOQDtls.get("BLOCK_NAME").toString();
		String floorName = mapBOQDtls.get("FLOOR_NAME")==null ? "" : mapBOQDtls.get("FLOOR_NAME").toString();
		String flatName = mapBOQDtls.get("FLAT_NAME")==null ? "" : mapBOQDtls.get("FLAT_NAME").toString();
		String action = mapBOQDtls.get("ACTION")==null ? "" : mapBOQDtls.get("ACTION").toString();
		String oldWorkArea = mapBOQDtls.get("OLD_WO_WORK_AREA")==null ? "0" : mapBOQDtls.get("OLD_WO_WORK_AREA").toString();
		String newWorkArea = mapBOQDtls.get("NEW_WO_WORK_AREA")==null ? "0" : mapBOQDtls.get("NEW_WO_WORK_AREA").toString();
		String oldPrice = mapBOQDtls.get("OLD_QS_AREA_PRICE")==null ? "0" : mapBOQDtls.get("OLD_QS_AREA_PRICE").toString();
		String newPrice = mapBOQDtls.get("NEW_QS_AREA_PRICE")==null ? "0" : mapBOQDtls.get("NEW_QS_AREA_PRICE").toString();
		
		String materialAction = mapBOQDtls.get("M_ACTION")==null ? "" : mapBOQDtls.get("M_ACTION").toString();
		String materialGroupName = mapBOQDtls.get("MATERIAL_GROUP_NAME")==null ? "" : mapBOQDtls.get("MATERIAL_GROUP_NAME").toString();
		String oldPerUnitQuantity = mapBOQDtls.get("OLD_PER_UNIT_QUANTITY")==null ? "0" : mapBOQDtls.get("OLD_PER_UNIT_QUANTITY").toString();
		String newPerUnitQuantity = mapBOQDtls.get("NEW_PER_UNIT_QUANTITY")==null ? "0" : mapBOQDtls.get("NEW_PER_UNIT_QUANTITY").toString();
		String oldPerUnitAmount = mapBOQDtls.get("OLD_PER_UNIT_AMOUNT")==null ? "0" : mapBOQDtls.get("OLD_PER_UNIT_AMOUNT").toString();
		String newPerUnitAmount = mapBOQDtls.get("NEW_PER_UNIT_AMOUNT")==null ? "0" : mapBOQDtls.get("NEW_PER_UNIT_AMOUNT").toString();
		
		String modificationDetails = "In "+majorHead+"(MAJOR),"+minorHead+"(MINOR),"+workDescription+"(WD),";
		modificationDetails += " in "+blockName+",";
		if(StringUtils.isNotBlank(floorName)){
			modificationDetails += " in "+floorName+",";
		}
		if(Double.valueOf(newWorkArea)==0){
			modificationDetails += " Area Deleted";
			if(recordType.equals("MATERIAL")){
				modificationDetails += " and Material '"+materialGroupName+"' is also Deleted";
			}
		}
		else if(Double.valueOf(oldWorkArea)==0){
			modificationDetails += " Area newly added is "+newWorkArea;
			//if(recordType.equals("LABOR")){
			modificationDetails += " and its Price is "+newPrice;
			//}
			if(recordType.equals("MATERIAL")){
				modificationDetails += " and Material '"+materialGroupName+"' PerUnitQuantity is "+newPerUnitQuantity+" & PerUnitAmount is "+newPerUnitAmount;
			}
		}
		else{
			if(!oldWorkArea.equals(newWorkArea)){
				modificationDetails += " Area changed "+oldWorkArea+" to "+newWorkArea;
			}
			
			//labor
			//if(recordType.equals("LABOR")){
			if(!oldWorkArea.equals(newWorkArea)&&!oldPrice.equals(newPrice)){
				modificationDetails += " and";
			}
			if(!oldPrice.equals(newPrice)){
				modificationDetails += " Price changed "+oldPrice+" to "+newPrice;
			}
			//}
			//material
			if(recordType.equals("MATERIAL")){
				String TextForDuplicateCheck = modificationDetails;
				if(StringUtils.isNotBlank(materialAction)){
					if(materialAction.equals("EDIT")){
						if(oldPerUnitQuantity.equals(newPerUnitQuantity)&&oldPerUnitAmount.equals(newPerUnitAmount)){
							if(DuplicateCheck.contains(TextForDuplicateCheck)){
								continue;
							}
						}
					}
					if(!oldPerUnitQuantity.equals(newPerUnitQuantity)||!oldPerUnitAmount.equals(newPerUnitAmount)){
						modificationDetails += " and";
					}
				}
				if(materialAction.equals("DEL")){
					modificationDetails += " material '"+materialGroupName+"' Deleted";
				}
				if(materialAction.equals("NEW")){
					modificationDetails += " material newly added is '"+materialGroupName+"'"
							+ " PerUnitQuantity "+newPerUnitQuantity
							+ " ,PerUnitAmount "+newPerUnitAmount;
							
				}
				if(materialAction.equals("EDIT")){
					
					if(!oldPerUnitQuantity.equals(newPerUnitQuantity)||!oldPerUnitAmount.equals(newPerUnitAmount)){
						modificationDetails += " material '"+materialGroupName+"'";
					}
					if(!oldPerUnitQuantity.equals(newPerUnitQuantity)){
						modificationDetails += " PerUnitQuantity changed "+oldPerUnitQuantity+" to "+newPerUnitQuantity;
					}
					if(!oldPerUnitQuantity.equals(newPerUnitQuantity)&&!oldPerUnitAmount.equals(newPerUnitAmount)){
						modificationDetails += " and";
					}
					if(!oldPerUnitAmount.equals(newPerUnitAmount)){
						modificationDetails += " PerUnitAmount changed "+oldPerUnitAmount+" to "+newPerUnitAmount;
					}
				}
				DuplicateCheck.add(TextForDuplicateCheck);
			}
			
		}
		modificationDetails+=".";
		
		objBOQBean.setStrMajorHeadDesc(majorHead);
		objBOQBean.setStrMinorHeadDesc(minorHead);
		objBOQBean.setStrWorkDescription(workDescription);
		objBOQBean.setStrMeasurementName(measurement);
		objBOQBean.setStrBlock(blockName);
		objBOQBean.setStrFloor(floorName);
		objBOQBean.setStrFlat(flatName);
		objBOQBean.setAction(action);
		objBOQBean.setOldStrArea(oldWorkArea);
		objBOQBean.setStrArea(newWorkArea);
		objBOQBean.setOldDoubleLaborRatePerUnit(Double.valueOf(oldPrice));
		objBOQBean.setDoubleLaborRatePerUnit(Double.valueOf(Double.valueOf(newWorkArea)==0?"0":newPrice));
		objBOQBean.setModificationDetails(modificationDetails);
		
		list.add(objBOQBean);
		
		
	}
	return list;
	}
	
	@Override
	public List<BOQBean> getReviseBOQChangedWorkDetailsBasedOnVerNo(String siteId,int BOQSeqNo,String versionNo,String typeOfWork){


	List<BOQBean> list = new ArrayList<BOQBean>();
	List<Map<String, Object>> dbBOQDtls = null;

	String query = "";
	if(typeOfWork.equals("PIECEWORK")){
	query = " select  QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME," 
			+"  B.NAME as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME,"
			+"  QRBCD.OLD_WO_WORK_AREA,QRBCD.NEW_WO_WORK_AREA,QRBCD.OLD_QS_AREA_PRICE,QRBCD.NEW_QS_AREA_PRICE,QRBCD.ACTION "  
			+"  from QS_WO_MAJORHEAD QWM,QS_WO_MINORHEAD QSMINOR, QS_WO_WORKDESC QWD,QS_WO_MEASURMENT QUOM, "
			+"  QS_REVISE_BOQ_CHANGED_DTLS QRBCD left outer join BLOCK B on B.BLOCK_ID = QRBCD.BLOCK_ID "
			+"  left outer join FLOOR F on F.FLOOR_ID = QRBCD.FLOOR_ID "
			+"  left outer join FLAT FLAT on FLAT.FLAT_ID = QRBCD.FLAT_ID " 
			+"  where QRBCD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID " 
			+"  and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and QRBCD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and " 
			 
			+"  QRBCD.BOQ_SEQ_NO = ? and QRBCD.NEW_VERSION_NO = ? and QRBCD.SITE_ID = ? and "
			+ " (QRBCD.ACTION = 'EDIT' or QRBCD.ACTION = 'DEL' or QRBCD.ACTION = 'NEW')"
			+"  order by to_number(regexp_replace(QRBCD.WO_WORK_AREA_ID, '[^0-9]', ''))"; 
	}
	/*if(typeOfWork.equals("NMR")){
	query = "select QBD.SCOPE_OF_WORK, QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME"   
			+"  from QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD QSMINOR"  
			+"  where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID "
			+"  and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and" 
			+"  QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "
			+"  QB.TEMP_BOQ_NO = ? and QB.SITE_ID = ? and QBD.status='A'" 
			+"  order by to_number(QBD.QS_TEMP_BOQ_DETAILS_ID)";
	}*/
	
	dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {BOQSeqNo,versionNo,siteId});
	int serialNumber=1;
	for(Map<String, Object> mapBOQDtls : dbBOQDtls) {
		BOQBean objBOQBean = new BOQBean();
		objBOQBean.setStrSerialNumber(String.valueOf(serialNumber));
		serialNumber++;
		String majorHead = mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" : mapBOQDtls.get("WO_MAJORHEAD_DESC").toString();
		String minorHead = mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" : mapBOQDtls.get("WO_MINORHEAD_DESC").toString();
		String workDescription = mapBOQDtls.get("WO_WORK_DESCRIPTION")==null ? "" : mapBOQDtls.get("WO_WORK_DESCRIPTION").toString();
		String measurement = mapBOQDtls.get("WO_MEASURMEN_NAME")==null ? "" : mapBOQDtls.get("WO_MEASURMEN_NAME").toString();
		String blockName = mapBOQDtls.get("BLOCK_NAME")==null ? "" : mapBOQDtls.get("BLOCK_NAME").toString();
		String floorName = mapBOQDtls.get("FLOOR_NAME")==null ? "" : mapBOQDtls.get("FLOOR_NAME").toString();
		String flatName = mapBOQDtls.get("FLAT_NAME")==null ? "" : mapBOQDtls.get("FLAT_NAME").toString();
		String action = mapBOQDtls.get("ACTION")==null ? "" : mapBOQDtls.get("ACTION").toString();
		String oldWorkArea = mapBOQDtls.get("OLD_WO_WORK_AREA")==null ? "0" : mapBOQDtls.get("OLD_WO_WORK_AREA").toString();
		String newWorkArea = mapBOQDtls.get("NEW_WO_WORK_AREA")==null ? "0" : mapBOQDtls.get("NEW_WO_WORK_AREA").toString();
		String oldPrice = mapBOQDtls.get("OLD_QS_AREA_PRICE")==null ? "0" : mapBOQDtls.get("OLD_QS_AREA_PRICE").toString();
		String newPrice = mapBOQDtls.get("NEW_QS_AREA_PRICE")==null ? "0" : mapBOQDtls.get("NEW_QS_AREA_PRICE").toString();
		
		String modificationDetails = "In "+majorHead+"(MAJOR),"+minorHead+"(MINOR),"+workDescription+"(WD),";
		modificationDetails += " in "+blockName+",";
		if(StringUtils.isNotBlank(floorName)){
			modificationDetails += " in "+floorName+",";
		}
		if(Double.valueOf(newWorkArea)==0){
			modificationDetails += " Area Deleted";
		}
		else if(Double.valueOf(oldWorkArea)==0){
			modificationDetails += " Area newly added is "+newWorkArea+" and its Price is "+newPrice;
		}
		else{
			//
			if(!oldWorkArea.equals(newWorkArea)){
				modificationDetails += " Area changed "+oldWorkArea+" to "+newWorkArea;
			}
			if(!oldWorkArea.equals(newWorkArea)&&!oldPrice.equals(newPrice)){
				modificationDetails += " and";
			}
			if(!oldPrice.equals(newPrice)){
				modificationDetails += " Price changed "+oldPrice+" to "+newPrice;
			}
			//
		}
		modificationDetails+=".";
		
		objBOQBean.setStrMajorHeadDesc(majorHead);
		objBOQBean.setStrMinorHeadDesc(minorHead);
		objBOQBean.setStrWorkDescription(workDescription);
		objBOQBean.setStrMeasurementName(measurement);
		objBOQBean.setStrBlock(blockName);
		objBOQBean.setStrFloor(floorName);
		objBOQBean.setStrFlat(flatName);
		objBOQBean.setAction(action);
		objBOQBean.setOldStrArea(oldWorkArea);
		objBOQBean.setStrArea(newWorkArea);
		objBOQBean.setOldDoubleLaborRatePerUnit(Double.valueOf(oldPrice));
		objBOQBean.setDoubleLaborRatePerUnit(Double.valueOf(Double.valueOf(newWorkArea)==0?"0":newPrice));
		objBOQBean.setModificationDetails(modificationDetails);
		
		list.add(objBOQBean);
		
		
	}
	return list;
	}
	
	@Override
	public List<BOQBean> getSOWChangedWorkDetails(String siteId,String tempBOQNumber, String typeOfWork){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>> dbBOQDtls = null;

		String query = "";
		if(typeOfWork.equals("PIECEWORK")){
			query = " select QTBD.WO_WORK_DESC_ID,QTBD.SCOPE_OF_WORK,QWM.WO_MINORHEAD_ID,QWMAJ.WO_MAJORHEAD_ID,"
					+ " QWW.WO_WORK_DESCRIPTION,QWM.WO_MINORHEAD_DESC,QWMAJ.WO_MAJORHEAD_DESC "
					+ " from QS_TEMP_BOQ_DETAILS QTBD,QS_WO_WORKDESC QWW,QS_WO_MINORHEAD QWM,QS_WO_MAJORHEAD QWMAJ"  
					+ " where QTBD.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID and QTBD.WO_MINORHEAD_ID=QWM.WO_MINORHEAD_ID and QWMAJ.WO_MAJORHEAD_ID=QWM.WO_MAJORHEAD_ID" 
					+ " and QTBD.TEMP_BOQ_NO = ? and QTBD.ACTION like '%SOW%' ";
		}
		
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {tempBOQNumber});
		int serialNumber=1;
		for(Map<String, Object> mapBOQDtls : dbBOQDtls) {
			BOQBean objBOQBean = new BOQBean();
			objBOQBean.setStrSerialNumber(String.valueOf(serialNumber));
			serialNumber++;
			String majorHead = mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" : mapBOQDtls.get("WO_MAJORHEAD_DESC").toString();
			String minorHead = mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" : mapBOQDtls.get("WO_MINORHEAD_DESC").toString();
			String workDescription = mapBOQDtls.get("WO_WORK_DESCRIPTION")==null ? "" : mapBOQDtls.get("WO_WORK_DESCRIPTION").toString();
			String sow = mapBOQDtls.get("SCOPE_OF_WORK")==null ? "" : mapBOQDtls.get("SCOPE_OF_WORK").toString();
			
			String modificationDetails = "In "+majorHead+"(MAJOR),"+minorHead+"(MINOR),"+workDescription+"(WD),";
			
			modificationDetails = modificationDetails+" Scope Of Work Renamed as '"+sow+"'";
			
			modificationDetails+=".";

			objBOQBean.setModificationDetails(modificationDetails);

			list.add(objBOQBean);


		}
		return list;
	}
	
	@Override
	public List<BOQBean> getBOQForApprovalByID(BOQBean boqBean) {
		return null;
		
		
		/*
		 int serialNumber=1;
		List<BOQBean> listOfBoq=new ArrayList<BOQBean>();
		String query = "select   	QTBT.SCOPE_OF_WORK,	QTBT.REMARKS, ((SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID =QTB.PENDING_EMP_ID )) as APPROVER_EMP_NAME , QTBT.QS_TEMP_BOQ_DETAILS_ID,QTBT.WO_WORK_AREA,QTBT.STATUS,QTBT.WO_WORK_AREA_AMOUNT ,"
				+ "QTB.TEMP_BOQ_NO,S.SITE_NAME,QTB.SITE_ID,QTB.CREATED_DATE,SED.EMP_NAME ,"
				+ "minor.WO_MINORHEAD_ID,minor.WO_MINORHEAD_DESC,major.WO_MAJORHEAD_ID,major.WO_MAJORHEAD_DESC"
				+ ",workdesc.WO_WORK_DESCRIPTION,workdesc.WO_WORK_DESC_ID,mesur.WO_MEASURMENT_ID,mesur.WO_MEASURMEN_NAME  "
				+ "from  QS_TEMP_BOQ QTB,QS_TEMP_BOQ_DETAILS QTBT, SITE s,SUMADHURA_EMPLOYEE_DETAILS SED ,"
				+ "QS_WO_MAJORHEAD major,QS_WO_MINORHEAD minor,QS_WO_WORKDESC workdesc,QS_WO_MEASURMENT mesur"
				+ " where QTB.SITE_ID = s.SITE_ID and SED.EMP_ID = QTB.CREATED_BY and  QTBT.TEMP_BOQ_NO=QTB.TEMP_BOQ_NO "
				+ "and QTBT.WO_WORK_DESC_ID=workdesc.WO_WORK_DESC_ID and workdesc.WO_MINORHEAD_ID=minor.WO_MINORHEAD_ID "
				+ "and minor.WO_MAJORHEAD_ID=major.WO_MAJORHEAD_ID and QTBT.WO_MEASURMENT_ID=mesur.WO_MEASURMENT_ID "
				+ "and QTB.PENDING_EMP_ID =? and QTB.TEMP_BOQ_NO=? and QTB.SITE_ID=?";
		
		Object[] params={boqBean.getUserId(),boqBean.getTempBOQNo(),boqBean.getSiteId() };
		
		List<Map<String, Object>> dbBOQListForApprovel = jdbcTemplate.queryForList(query,params);
		for (Map<String, Object> map : dbBOQListForApprovel) {
			BOQBean	bean=new BOQBean();
			bean.setSerialNumber(String.valueOf(serialNumber));
			serialNumber++;
			bean.setMajorHeadDesc(map.get("WO_MAJORHEAD_DESC")==null?"":map.get("WO_MAJORHEAD_DESC").toString());
			bean.setMajorHeadId(map.get("WO_MAJORHEAD_ID")==null?"":map.get("WO_MAJORHEAD_ID").toString());
			bean.setMinorHeadDesc(map.get("WO_MINORHEAD_DESC")==null?"":map.get("WO_MINORHEAD_DESC").toString());
			bean.setMinorHeadId(map.get("WO_MINORHEAD_ID")==null?"":map.get("WO_MINORHEAD_ID").toString());
			bean.setWorkDescription(map.get("WO_WORK_DESCRIPTION")==null?"":map.get("WO_WORK_DESCRIPTION").toString());
			bean.setWorkDescriptionId(map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString());
			bean.setUnitOfMeasurementName(map.get("WO_MEASURMEN_NAME")==null?"":map.get("WO_MEASURMEN_NAME").toString());
			bean.setUnitOfMeasurementId(map.get("WO_MEASURMENT_ID")==null?"":map.get("WO_MEASURMENT_ID").toString());
			
			bean.setScopeOfWork(map.get("SCOPE_OF_WORK")==null?"":map.get("SCOPE_OF_WORK").toString());
			bean.setRemarks(map.get("REMARKS")==null?"":map.get("REMARKS").toString());
			bean.setApproverEmpId(map.get("APPROVER_EMP_NAME")==null?"":map.get("APPROVER_EMP_NAME").toString());
			bean.setTempBOQNo(map.get("TEMP_BOQ_NO")==null?"":map.get("TEMP_BOQ_NO").toString());
			bean.setTempBOQDetailsId(map.get("QS_TEMP_BOQ_DETAILS_ID")==null?"":map.get("QS_TEMP_BOQ_DETAILS_ID").toString());
			bean.setWorkArea(map.get("WO_WORK_AREA")==null?"":map.get("WO_WORK_AREA").toString());
			bean.setStatus(map.get("STATUS")==null?"":map.get("STATUS").toString());
			bean.setWorkAreaAmount(map.get("WO_WORK_AREA_AMOUNT")==null?"":map.get("WO_WORK_AREA_AMOUNT").toString());
			bean.setSiteName(map.get("SITE_NAME")==null?"":map.get("SITE_NAME").toString());
			bean.setSiteId(map.get("SITE_ID")==null?"":map.get("SITE_ID").toString());
			bean.setTemBOQCreatedDate(map.get("CREATED_DATE")==null?"":map.get("CREATED_DATE").toString());
			listOfBoq.add(bean);
		}
		return listOfBoq;
	*/}

	@Override
	public List<BOQBean> getBOQData(BOQBean boqBean) {
		List<BOQBean> listOfBoq = new ArrayList<BOQBean>();
		String query = "";
		List<Map<String, Object>> dbBOQListForApprovel = null;
		if(!boqBean.getViewTempBoq().equals("true")){
			query = "select ((SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID =QTB.PENDING_EMP_ID )) as APPROVER_EMP_NAME ,"
					+ " QTB.BOQ_TOTAL_AMOUNT,QTB.BOQ_MATERIAL_AMOUNT,QTB.BOQ_LABOR_AMOUNT,QTB.TEMP_BOQ_NO,S.SITE_NAME,QTB.SITE_ID,QTB.CREATED_DATE,QTB.TYPE_OF_WORK,QTB.BOQ_TYPE,SED.EMP_NAME,QTB.CREATED_BY,PENDING_EMP_ID "
					+ "from  QS_TEMP_BOQ QTB, SITE s,SUMADHURA_EMPLOYEE_DETAILS SED "
					+ "where QTB.SITE_ID = s.SITE_ID and SED.EMP_ID = QTB.CREATED_BY and QTB.status='A' "
					+ "and QTB.PENDING_EMP_ID =? and QTB.TEMP_BOQ_NO=? and QTB.SITE_ID=?";
			Object[] params = { boqBean.getStrUserId(), boqBean.getIntTempBOQNo(), boqBean.getStrSiteId() };
			dbBOQListForApprovel = jdbcTemplate.queryForList(query, params);
		}
		if(boqBean.getViewTempBoq().equals("true")){
			query = "select ((SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID =QTB.PENDING_EMP_ID )) as APPROVER_EMP_NAME ,"
					+ " QTB.BOQ_TOTAL_AMOUNT,QTB.BOQ_MATERIAL_AMOUNT,QTB.BOQ_LABOR_AMOUNT,QTB.TEMP_BOQ_NO,S.SITE_NAME,QTB.SITE_ID,QTB.CREATED_DATE,QTB.TYPE_OF_WORK,QTB.BOQ_TYPE,SED.EMP_NAME,QTB.CREATED_BY,PENDING_EMP_ID "
					+ "from  QS_TEMP_BOQ QTB, SITE s,SUMADHURA_EMPLOYEE_DETAILS SED "
					+ "where QTB.SITE_ID = s.SITE_ID and SED.EMP_ID = QTB.CREATED_BY and QTB.status='A' "
					+ "and QTB.TEMP_BOQ_NO=? and QTB.SITE_ID=?";
			Object[] params = { boqBean.getIntTempBOQNo(), boqBean.getStrSiteId() };
			 dbBOQListForApprovel = jdbcTemplate.queryForList(query, params);
		}
		
		for (Map<String, Object> map : dbBOQListForApprovel) {
			BOQBean bean = new BOQBean();
			bean.setStrApproverEmpId(map.get("APPROVER_EMP_NAME") == null ? "" : map.get("APPROVER_EMP_NAME").toString());
			bean.setIntTempBOQNo(map.get("TEMP_BOQ_NO") == null ? 0 :Integer.valueOf( map.get("TEMP_BOQ_NO").toString()));
			bean.setStrStatus(map.get("STATUS") == null ? "" : map.get("STATUS").toString());
			bean.setStrSiteName(map.get("SITE_NAME") == null ? "" : map.get("SITE_NAME").toString());
			String boqSiteId = map.get("SITE_ID") == null ? "" : map.get("SITE_ID").toString();
			bean.setStrSiteId(boqSiteId);
			bean.setStrTemBOQCreatedDate(map.get("CREATED_DATE") == null ? "" : map.get("CREATED_DATE").toString());
			bean.setStrBoqFrom(map.get("EMP_NAME") == null ? "" : map.get("EMP_NAME").toString());
			bean.setStrTepBOQCreatedEmployeId(map.get("CREATED_BY") == null ? "" : map.get("CREATED_BY").toString());
			bean.setStrTempBOQApproveEmployeeId(map.get("PENDING_EMP_ID") == null ? "" : map.get("PENDING_EMP_ID").toString());
			String typeOfWork = map.get("TYPE_OF_WORK")==null ? "" :   map.get("TYPE_OF_WORK").toString();
			bean.setTypeOfWork(typeOfWork);
			boqBean.setTypeOfWork(typeOfWork);
			String boqType = map.get("BOQ_TYPE")==null ? "" :   map.get("BOQ_TYPE").toString();
			bean.setBoqType(boqType);
			if(boqType.equals("REVISED")){
				String strBOQSeqNo = String.valueOf(getBOQSeqNoOfCurrentActivePermanentBOQ(boqSiteId, typeOfWork));
				String strBOQNo=getBOQNoOfCurrentActivePermanentBOQ(boqSiteId, typeOfWork);
				String strVersionNo=getVersionNoOfCurrentActivePermanentBOQ(boqSiteId, typeOfWork);
				bean.setStrBOQSeqNo(strBOQSeqNo);
				bean.setStrBOQNo(strBOQNo);
				bean.setStrVersionNo(strVersionNo);
			}
			String amount = map.get("BOQ_TOTAL_AMOUNT")==null ? "0" :   map.get("BOQ_TOTAL_AMOUNT").toString();
			String materialamount = map.get("BOQ_MATERIAL_AMOUNT")==null ? "0" :   map.get("BOQ_MATERIAL_AMOUNT").toString();
			String laboramount = map.get("BOQ_LABOR_AMOUNT")==null ? "0" :   map.get("BOQ_LABOR_AMOUNT").toString();

			Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
			bean.setBoqTotalAmount(format.format(new BigDecimal(amount)));
			bean.setBoqMaterialAmount(format.format(new BigDecimal(materialamount)));
			bean.setBoqLaborAmount(format.format(new BigDecimal(laboramount)));
			listOfBoq.add(bean);
		}
		return listOfBoq;
	}
	
	@Override
	public int approveTempBOQ(BOQBean bean,BOQBean objForOnlyPermanentBoqNumber) {
	int count=0;

		if (bean.getStrApproverEmpId().equals("END")) {
			int result=0;
			
			//inactive product dtls of inactive BOQ on upload (only for TESTING not necessary for live)
			jdbcTemplate.update("update QS_BOQ_PRODUCT_DTLS set STATUS='I' where site_id = '"+bean.getStrSiteId()+"'");
			
			
			//Inactive Old BOQ
			jdbcTemplate.update("update QS_BOQ set STATUS='I' WHERE SITE_ID='"+bean.getStrSiteId()+"' and  TYPE_OF_WORK='"+bean.getTypeOfWork()+"'");
			
			int version_No = 1+jdbcTemplate.queryForInt("SELECT MAX(TO_NUMBER(VERSION_NO)) FROM QS_BOQ WHERE SITE_ID='"+bean.getStrSiteId()+"' and TYPE_OF_WORK='"+bean.getTypeOfWork()+"'");
			
			String permanantBOQSeqNumPK=jdbcTemplate.queryForObject("select QS_BOQ_SEQ.NEXTVAL FROM DUAL", String.class);
			String BOQNumber=jdbcTemplate.queryForObject("select CONCAT('BO/SIPL/"+bean.getStrSiteId()+"/',BOQ_NUMBER_SEQ.NEXTVAL) FROM DUAL", String.class);
			System.out.println("bean.getIntTempBOQNo() "+bean.getIntTempBOQNo()+" "+bean.getStrSiteId());
			String queryForTempBOQtoPermanant="INSERT INTO QS_BOQ(BOQ_SEQ_NO,VERSION_NO,STATUS,SITE_ID,CREATED_DATE,APPROVED_BY,BOQ_NO,TEMP_BOQ_NO,TYPE_OF_WORK,BOQ_TOTAL_AMOUNT,BOQ_LABOR_AMOUNT,BOQ_MATERIAL_AMOUNT) "
					+ "  select ?,? "
					+ " ,STATUS,SITE_ID,sysdate,PENDING_EMP_ID,'"+BOQNumber+"',TEMP_BOQ_NO,TYPE_OF_WORK,BOQ_TOTAL_AMOUNT,BOQ_LABOR_AMOUNT,BOQ_MATERIAL_AMOUNT FROM QS_TEMP_BOQ WHERE TEMP_BOQ_NO=? AND STATUS='A'";
			Object[] params={permanantBOQSeqNumPK,version_No,bean.getIntTempBOQNo()};
			result=jdbcTemplate.update(queryForTempBOQtoPermanant,params);
			//for qs boq back up table
			String queryForQsBoqBackup="INSERT INTO QS_BOQ_BACK_UP(BOQ_SEQ_NO,VERSION_NO,STATUS,SITE_ID,CREATED_DATE,APPROVED_BY,BOQ_NO,TEMP_BOQ_NO,TYPE_OF_WORK,BOQ_TOTAL_AMOUNT,BOQ_LABOR_AMOUNT,BOQ_MATERIAL_AMOUNT) "
					+ "  select ?,? "
					+ " ,STATUS,SITE_ID,sysdate,PENDING_EMP_ID,'"+BOQNumber+"',TEMP_BOQ_NO,TYPE_OF_WORK,BOQ_TOTAL_AMOUNT,BOQ_LABOR_AMOUNT,BOQ_MATERIAL_AMOUNT FROM QS_TEMP_BOQ WHERE TEMP_BOQ_NO=? AND STATUS='A'";
			Object[] params10={permanantBOQSeqNumPK,version_No,bean.getIntTempBOQNo()};
			result=jdbcTemplate.update(queryForQsBoqBackup,params10);
			//
			String queryForAllTempBOQDetailsId="select QS_TEMP_BOQ_DETAILS_ID from QS_TEMP_BOQ_DETAILS where TEMP_BOQ_NO=?";
			List<Map<String, Object>> listOfTempBOQDetailsId=jdbcTemplate.queryForList(queryForAllTempBOQDetailsId,bean.getIntTempBOQNo());
			for (Map<String, Object> map : listOfTempBOQDetailsId) {
				String QS_TEMP_BOQ_DETAILS_ID= map.get("QS_TEMP_BOQ_DETAILS_ID")==null?"":map.get("QS_TEMP_BOQ_DETAILS_ID").toString();
				
				String permanantBOQDeatilsSeq=jdbcTemplate.queryForObject("select QS_BOQ_DETAILS_SEQ.NEXTVAL FROM DUAL", String.class);
				String queryForTempBOQDeatilsToPermanant="INSERT INTO QS_BOQ_DETAILS"
						+ "(QS_BOQ_DETAILS_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,WO_WORK_AREA,STATUS,TEMP_BOQ_NO,WO_WORK_AREA_AMOUNT,BOQ_SEQ_NO,REMARKS,SCOPE_OF_WORK,WO_MINORHEAD_ID,RECORD_TYPE) "
						+ "SELECT ?,WO_WORK_DESC_ID,WO_MEASURMENT_ID,WO_WORK_AREA,STATUS,TEMP_BOQ_NO,WO_WORK_AREA_AMOUNT,?,REMARKS ,SCOPE_OF_WORK,WO_MINORHEAD_ID,RECORD_TYPE "
						+ "FROM QS_TEMP_BOQ_DETAILS where QS_TEMP_BOQ_DETAILS_ID=?  AND STATUS='A'";
				Object[] params1={permanantBOQDeatilsSeq,permanantBOQSeqNumPK,QS_TEMP_BOQ_DETAILS_ID};
				result=jdbcTemplate.update(queryForTempBOQDeatilsToPermanant,params1);
			if(result!=0){
				String queryForTempBOQAreaMapping="INSERT INTO QS_BOQ_AREA_MAPPING(WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,FLAT_ID,BLOCK_ID,SITE_ID,"
						+ "WO_WORK_AREA,WO_WORK_AVAILABE_AREA,WO_PERCENTAGE_AVAIL,STATUS,WO_WORK_INITIATE_AREA,BOQ_SEQ_NO,QS_BOQ_DETAILS_ID, "
						+ "QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,RECORD_TYPE,WO_WORK_AREA_GROUP_ID) "
						+ "SELECT        			WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,FLAT_ID,BLOCK_ID,"+bean.getStrSiteId()+", "
						+ " WO_WORK_AREA,WO_WORK_AREA,100,STATUS,0,?,?, "
						+ "QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,RECORD_TYPE,WO_WORK_AREA_GROUP_ID FROM QS_TEMP_BOQ_AREA_MAPPING WHERE QS_TEMP_BOQ_DETAILS_ID=? AND STATUS='A'";
				Object[] params2={permanantBOQSeqNumPK,permanantBOQDeatilsSeq,QS_TEMP_BOQ_DETAILS_ID};
				result=jdbcTemplate.update(queryForTempBOQAreaMapping,params2);
			}
			
			
				String query = "UPDATE QS_TEMP_BOQ SET PENDING_EMP_ID=?,STATUS='I' WHERE TEMP_BOQ_NO=?";
				Object[] params3 = { bean.getStrApproverEmpId(), bean.getIntTempBOQNo() };
				count = jdbcTemplate.update(query, params3);
			}
			
			String queryForPermanentBOQProductDetails="insert into QS_BOQ_PRODUCT_DTLS (QS_BOQ_PRODUCT_DTLS_ID,WO_WORK_AREA_ID,WO_WORK_AREA,MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS,SITE_ID,BOQ_SEQ_NO) select QS_BOQ_PRODUCT_DTLS_SEQ.nextval,WO_WORK_AREA_ID,WO_WORK_AREA,MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS,SITE_ID,? from QS_TEMP_BOQ_PRODUCT_DTLS where TEMP_BOQ_NO = ? ";
			result=jdbcTemplate.update(queryForPermanentBOQProductDetails,new Object[]{permanantBOQSeqNumPK,bean.getIntTempBOQNo()});
			
			/*MultiObject multiObj = getTotalAmountOfCurrentTempBOQ(bean.getIntTempBOQNo());
			String BOQTotalAmount = multiObj.getString1();
			String BOQMaterialTotalAmount = multiObj.getString2();
			String BOQLaborTotalAmount = multiObj.getString3();
			getBOQTotalinBOQAreaMapping(Integer.parseInt(permanantBOQSeqNumPK));
			getBOQMaterialTotalinBOQProductDetails(Integer.parseInt(permanantBOQSeqNumPK));*/
			objForOnlyPermanentBoqNumber.setStrBOQNo(BOQNumber);
			copyTempExcelFileIntoThePermanentFolder(bean, BOQNumber,String.valueOf(version_No));
		} else {
			String query = "UPDATE QS_TEMP_BOQ SET PENDING_EMP_ID=? WHERE TEMP_BOQ_NO=?";
			Object[] params = { bean.getStrApproverEmpId(), bean.getIntTempBOQNo() };
			count = jdbcTemplate.update(query, params);
		}
		return count;
	}
	private void copyTempExcelFileIntoThePermanentFolder(BOQBean bean,String BOQNumber,String newVersionNo) {
		String siteName = bean.getStrSiteName();
		int tempBOQNo = bean.getIntTempBOQNo();
		int portNumber = bean.getPortNumber();
        String rootFilePath = null;
        if(portNumber==80){
        	rootFilePath = validateParams.getProperty("BOQ_EXCEL_PATH") == null ? "" : validateParams.getProperty("BOQ_EXCEL_PATH").toString();
        }
        else{
        	rootFilePath = validateParams.getProperty("BOQ_EXCEL_PATH_DEV") == null ? "" : validateParams.getProperty("BOQ_EXCEL_PATH_DEV").toString();
        }
        File dir1 = new File(rootFilePath+bean.getStrSiteId()+"//temp");
		String inputFilePath = dir1.getAbsolutePath() + File.separator +"BOQ_"+ siteName+"_"+tempBOQNo+".xlsx"; 
		File dir2 = new File(rootFilePath+bean.getStrSiteId());
		BOQNumber = BOQNumber.replace("/","$$");
		String outputFilePath = dir2.getAbsolutePath()+ File.separator +"BOQ_"+ siteName+"_"+BOQNumber+"_"+newVersionNo+".xlsx"; 
		Path src = Paths.get(inputFilePath);
		Path dst = Paths.get(outputFilePath);
		
		try {
			Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public int approveRejectDeatilsForBOQ(BOQBean bean) {
		int count=0;
			String query="INSERT INTO QS_BOQ_CRT_APPRL_DTLS "
					+ " (TEMP_BOQ_NO,QS_BOQ_CRT_APPRL_DTLS_ID,CREATION_DATE,SITE_ID,WO_CREATE_APPROVE_EMP_ID,PURPOSE,OPERATION_TYPE) "
					+ "   VALUES(?,QS_BOQ_CRT_APPRL_DTLS_SEQ.NEXTVAL,SYSDATE,?,?,?,?)";
			Object[] params={bean.getIntTempBOQNo(),bean.getStrSiteId(),bean.getStrUserId(),bean.getStrRemarks(),bean.getStrOperationType()};
			count=jdbcTemplate.update(query,params);
		return count;
	}
	
	@Override
	public int rejectTempBOQ(BOQBean bean) {
		int count=0;
		String query="UPDATE QS_TEMP_BOQ SET PENDING_EMP_ID=? WHERE TEMP_BOQ_NO=?";
		 query="UPDATE QS_TEMP_BOQ SET PENDING_EMP_ID=CREATED_BY ,STATUS='R' WHERE TEMP_BOQ_NO=?";
		Object[] params={bean.getIntTempBOQNo()};
		count=jdbcTemplate.update(query,params);
		return count;
	}
	
		@Override
	public List<BOQBean> getMyBOQList( String siteId){

		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>> listOfBOQ = null;
		BOQBean objBOQBean =  null;
		
		String query = " select BOQ.BOQ_SEQ_NO,BOQ.VERSION_NO,BOQ.CREATED_DATE,BOQ.BOQ_NO,BOQ.TYPE_OF_WORK,S.SITE_NAME,BOQ.SITE_ID "+
                       " from QS_BOQ  BOQ ,SITE S where BOQ.SITE_ID = S.SITE_ID and BOQ.SITE_ID = ? and BOQ.STATUS = 'A'";
		listOfBOQ = jdbcTemplate.queryForList(query, new Object[] {siteId});
		for(Map<String, Object> dbBOQList : listOfBOQ) {

			objBOQBean = new BOQBean();


			objBOQBean.setStrBOQSeqNo(dbBOQList.get("BOQ_SEQ_NO")==null ? "0" :   dbBOQList.get("BOQ_SEQ_NO").toString());
			objBOQBean.setStrVersionNo(dbBOQList.get("VERSION_NO")==null ? "0" :   dbBOQList.get("VERSION_NO").toString());
			objBOQBean.setStrSiteName(dbBOQList.get("SITE_NAME")==null ? "0" :   dbBOQList.get("SITE_NAME").toString());
			objBOQBean.setStrSiteId(dbBOQList.get("SITE_ID")==null ? "0" :   dbBOQList.get("SITE_ID").toString());
			objBOQBean.setStrBOQCreationDate(dbBOQList.get("CREATED_DATE")==null ? "0" :   dbBOQList.get("CREATED_DATE").toString());
			objBOQBean.setStrBOQNo(dbBOQList.get("BOQ_NO")==null ? "0" :   dbBOQList.get("BOQ_NO").toString());
			objBOQBean.setTypeOfWork(dbBOQList.get("TYPE_OF_WORK")==null ? "" :   dbBOQList.get("TYPE_OF_WORK").toString());

			list.add(objBOQBean);
		}
		return list;
	}

	@Override
	public BOQBean getBOQDetails( int intBOQSeqNo, String siteId){

		
		List<Map<String, Object>> listOfBOQ = null;
		BOQBean objBOQBean =  null;
		
		String query = " select BOQ.BOQ_SEQ_NO,BOQ.SITE_ID,BOQ.BOQ_TOTAL_AMOUNT,BOQ.BOQ_LABOR_AMOUNT,BOQ.BOQ_MATERIAL_AMOUNT,BOQ.VERSION_NO,BOQ.CREATED_DATE,BOQ.BOQ_NO,BOQ.TYPE_OF_WORK,S.SITE_NAME "+
                       " from QS_BOQ  BOQ ,SITE S where BOQ.SITE_ID = S.SITE_ID and BOQ.BOQ_SEQ_NO = ? and BOQ.STATUS = 'A'";
		listOfBOQ = jdbcTemplate.queryForList(query, new Object[] {intBOQSeqNo});
		for(Map<String, Object> dbBOQList : listOfBOQ) {

			objBOQBean = new BOQBean();


			objBOQBean.setStrBOQSeqNo(dbBOQList.get("BOQ_SEQ_NO")==null ? "0" :   dbBOQList.get("BOQ_SEQ_NO").toString());
			objBOQBean.setStrVersionNo(dbBOQList.get("VERSION_NO")==null ? "0" :   dbBOQList.get("VERSION_NO").toString());
			objBOQBean.setStrSiteName(dbBOQList.get("SITE_NAME")==null ? "0" :   dbBOQList.get("SITE_NAME").toString());
			objBOQBean.setStrSiteId(dbBOQList.get("SITE_ID")==null ? "0" :   dbBOQList.get("SITE_ID").toString());
			objBOQBean.setStrBOQCreationDate(dbBOQList.get("UPDATED_DATE")==null ? "" : new SimpleDateFormat("dd-MMM-yy").format((Date)dbBOQList.get("UPDATED_DATE")));
			if(dbBOQList.get("UPDATED_DATE")==null){
				objBOQBean.setStrBOQCreationDate(dbBOQList.get("CREATED_DATE")==null? "" : new SimpleDateFormat("dd-MMM-yy").format((Date)dbBOQList.get("CREATED_DATE")));
			}objBOQBean.setStrBOQNo(dbBOQList.get("BOQ_NO")==null ? "0" :   dbBOQList.get("BOQ_NO").toString());
			objBOQBean.setTypeOfWork(dbBOQList.get("TYPE_OF_WORK")==null ? "" :   dbBOQList.get("TYPE_OF_WORK").toString());
			String amount = dbBOQList.get("BOQ_TOTAL_AMOUNT")==null ? "0" :   dbBOQList.get("BOQ_TOTAL_AMOUNT").toString();
			String materialamount = dbBOQList.get("BOQ_MATERIAL_AMOUNT")==null ? "0" :   dbBOQList.get("BOQ_MATERIAL_AMOUNT").toString();
			String laboramount = dbBOQList.get("BOQ_LABOR_AMOUNT")==null ? "0" :   dbBOQList.get("BOQ_LABOR_AMOUNT").toString();

			Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
			//amount=format.format(new BigDecimal(amount));
			//amount = amount.replaceAll("Rs", "");
			//amount = amount.trim();
			//objBOQBean.setBoqTotalAmount(amount);
			//objBOQBean.setBoqMaterialAmount(format.format(new BigDecimal(materialamount)));
			//objBOQBean.setBoqLaborAmount(format.format(new BigDecimal(laboramount)));
			
			objBOQBean.setBoqTotalAmount(format.format(new BigDecimal(amount).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
			objBOQBean.setBoqMaterialAmount(format.format(new BigDecimal(materialamount).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
			objBOQBean.setBoqLaborAmount(format.format(new BigDecimal(laboramount).setScale(2, BigDecimal.ROUND_HALF_EVEN)));
			
			
		}
		return objBOQBean;
	}

	@Override
	public List<BOQBean> getBOQWorkDetails(int intBOQSeqNumber,String strSiteId,BOQBean objBOQDetails, String majorHead, String minorHead, String workDescription){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>>  dbBOQDtls = null;
		int intSerailNo = 1;

		String query = "";
		if(objBOQDetails.getTypeOfWork().equals("PIECEWORK")){
		query = " select QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME, "

			+ " QBAM.WO_WORK_AREA, B.NAME  as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME,QBAM.QS_AREA_PRICE_PER_UNIT,QBAM.QS_AREA_AMOUNT,QBD.SCOPE_OF_WORK "
			+ ",QBAM.RECORD_TYPE,QBAM.WO_WORK_AREA_ID "

			+ " from   QS_BOQ QB,QS_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR, "

			+ " QS_BOQ_AREA_MAPPING  QBAM left outer join  BLOCK B on B.BLOCK_ID = QBAM.BLOCK_ID  "

			+ " left outer join  FLOOR F on F.FLOOR_ID = QBAM.FLOOR_ID "

			+ " left outer join  FLAT FLAT on FLAT.FLAT_ID = QBAM.FLAT_ID "

			+ " where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "

			+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "

			+ " QBAM.QS_BOQ_DETAILS_ID =  QBD.QS_BOQ_DETAILS_ID  and    QB.BOQ_SEQ_NO = QBD.BOQ_SEQ_NO and "

			+ " QB.BOQ_SEQ_NO = ?  " 
			
			+ "OPTIONS"
			
			+ " order by to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', '')) ";
		}
		if(objBOQDetails.getTypeOfWork().equals("NMR")){
		query = " select QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME " 
			 +" from   QS_BOQ QB,QS_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR "  
			 +" where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID  and QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID  and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "
			 +" and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
			 +" QB.BOQ_SEQ_NO = QBD.BOQ_SEQ_NO and  QB.BOQ_SEQ_NO = ?   "
			 + "OPTIONS"
			 +" order by to_number(QBD.QS_BOQ_DETAILS_ID)";
		}
		//BOQNumber = "BO/SIPL/112/8";
		//siteId = 112;
		if(StringUtils.isNotBlank(majorHead)&&StringUtils.isBlank(minorHead)&&StringUtils.isBlank(workDescription)){
			query = query.replace("OPTIONS", " and QWM.WO_MAJORHEAD_ID = '"+majorHead.split("@@")[0]+"' ");
		}
		if(StringUtils.isNotBlank(majorHead)&&StringUtils.isNotBlank(minorHead)&&StringUtils.isBlank(workDescription)){
			query = query.replace("OPTIONS", " and QWM.WO_MAJORHEAD_ID = '"+majorHead.split("@@")[0]+"'  and QSMINOR.WO_MINORHEAD_ID = '"+minorHead.split("@@")[0]+"' ");
		}
		if(StringUtils.isNotBlank(majorHead)&&StringUtils.isNotBlank(minorHead)&&StringUtils.isNotBlank(workDescription)){
			query = query.replace("OPTIONS", " and QWM.WO_MAJORHEAD_ID = '"+majorHead.split("@@")[0]+"'  and QSMINOR.WO_MINORHEAD_ID = '"+minorHead.split("@@")[0]+"'  and QWD.WO_WORK_DESC_ID = '"+workDescription.split("@@")[0]+"' ");
		}
		query = query.replace("OPTIONS", "");
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {
				intBOQSeqNumber
		});

		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		double selectedWorkTotal = 0;
		for(Map<String, Object> mapBOQDtls : dbBOQDtls) {


			BOQBean objBOQBean = new BOQBean();

			objBOQBean.setIntSerialNumber(intSerailNo);
			objBOQBean.setStrMajorHeadDesc(mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_DESC").toString());

			objBOQBean.setStrMinorHeadDesc(mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MINORHEAD_DESC").toString());

			objBOQBean.setStrWorkDescription(mapBOQDtls.get("WO_WORK_DESCRIPTION")==null ? "" :   mapBOQDtls.get("WO_WORK_DESCRIPTION").toString());

			objBOQBean.setStrMeasurementName(mapBOQDtls.get("WO_MEASURMEN_NAME")==null ? "" :   mapBOQDtls.get("WO_MEASURMEN_NAME").toString());

			objBOQBean.setStrArea(mapBOQDtls.get("WO_WORK_AREA")==null ? "0" :   mapBOQDtls.get("WO_WORK_AREA").toString());

			objBOQBean.setStrBlock(mapBOQDtls.get("BLOCK_NAME")==null ? "-" :   mapBOQDtls.get("BLOCK_NAME").toString());

			objBOQBean.setStrFloor(mapBOQDtls.get("FLOOR_NAME")==null ? "-" :   mapBOQDtls.get("FLOOR_NAME").toString());

			objBOQBean.setStrFlat(mapBOQDtls.get("FLAT_NAME")==null ? "-" :   mapBOQDtls.get("FLAT_NAME").toString());

			objBOQBean.setDoubleLaborRatePerUnit(Double.valueOf(mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT")==null ? "0" :   mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT").toString()));

			objBOQBean.setStrScopeOfWork(mapBOQDtls.get("SCOPE_OF_WORK")==null ? "-" :   mapBOQDtls.get("SCOPE_OF_WORK").toString());
			
			objBOQBean.setRecordType(mapBOQDtls.get("RECORD_TYPE")==null ? "-" :   mapBOQDtls.get("RECORD_TYPE").toString());
			objBOQBean.setWorkAreaId(mapBOQDtls.get("WO_WORK_AREA_ID")==null ? "-" :   mapBOQDtls.get("WO_WORK_AREA_ID").toString());

			/*double area = Double.valueOf(mapBOQDtls.get("WO_WORK_AREA")==null ? "0" :   mapBOQDtls.get("WO_WORK_AREA").toString());
			double price = Double.valueOf(mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT")==null ? "0" :   mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT").toString());
			*/
			double singleWorkCost = Double.valueOf(mapBOQDtls.get("QS_AREA_AMOUNT")==null ? "0" :   mapBOQDtls.get("QS_AREA_AMOUNT").toString());
			String strSingleWorkCost  = format.format(new BigDecimal(String.valueOf(singleWorkCost)));
			//strSingleWorkCost = strSingleWorkCost.replaceAll("Rs", "");
			strSingleWorkCost = strSingleWorkCost.trim();
			objBOQBean.setSingleWorkCost(strSingleWorkCost);
			selectedWorkTotal+=(singleWorkCost);
			intSerailNo ++;
			list.add(objBOQBean);
			
		}
		String amount=format.format(new BigDecimal(String.valueOf(selectedWorkTotal)));
		amount = amount.replaceAll("Rs", "");
		amount = amount.trim();
		objBOQDetails.setSelectedWorkTotal(amount);
		return list;
	}
	@Override
	public List<BOQBean> getBOQMaterialDetails(int intBOQSeqNumber,String workAreaId,BOQBean objBOQDetails){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>>  dbBOQDtls = null;
		int intSerailNo = 1;

		String query = "";
		if(objBOQDetails.getTypeOfWork().equals("PIECEWORK")){
		query = " select QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME, "

			+ " QBAM.WO_WORK_AREA, B.NAME  as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME,QBAM.QS_AREA_PRICE_PER_UNIT,QBAM.QS_AREA_AMOUNT,QBD.SCOPE_OF_WORK "
			+ ",QBAM.RECORD_TYPE,QBAM.WO_WORK_AREA_ID "
			+ ",PGM.MATERIAL_GROUP_NAME,PGM.MATERIAL_GROUP_MST_NAME,QBPD.TOTAL_QUANTITY,QBPD.TOTAL_AMOUNT "

			+ " from   QS_BOQ QB,QS_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR, "
			+ " QS_BOQ_PRODUCT_DTLS QBPD,PRODUCT_GROUP_MASTER PGM,"

			+ " QS_BOQ_AREA_MAPPING  QBAM left outer join  BLOCK B on B.BLOCK_ID = QBAM.BLOCK_ID  "

			+ " left outer join  FLOOR F on F.FLOOR_ID = QBAM.FLOOR_ID "

			+ " left outer join  FLAT FLAT on FLAT.FLAT_ID = QBAM.FLAT_ID "

			+ " where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "

			+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "

			+ " QBAM.QS_BOQ_DETAILS_ID =  QBD.QS_BOQ_DETAILS_ID  and    QB.BOQ_SEQ_NO = QBD.BOQ_SEQ_NO and "

			+ " QB.BOQ_SEQ_NO = ?  "
			+ " and QBAM.WO_WORK_AREA_ID=QBPD.WO_WORK_AREA_ID and QBAM.BOQ_SEQ_NO=QBPD.BOQ_SEQ_NO "
			+ " and QBPD.MATERIAL_GROUP_ID=PGM.MATERIAL_GROUP_ID and QBPD.MATERIAL_GROUP_MEASUREMENT_ID = PGM.MATERIAL_GROUP_MEASUREMENT_ID "
			+ " and QBPD.WO_WORK_AREA_ID = ? " 
			
			+ " order by to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', '')) ";
		}
		
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {
				intBOQSeqNumber, workAreaId
		});

		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		double selectedWorkTotal = 0;
		for(Map<String, Object> mapBOQDtls : dbBOQDtls) {


			BOQBean objBOQBean = new BOQBean();

			objBOQBean.setIntSerialNumber(intSerailNo);
			objBOQBean.setStrMajorHeadDesc(mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_DESC").toString());

			objBOQBean.setStrMinorHeadDesc(mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MINORHEAD_DESC").toString());

			objBOQBean.setStrWorkDescription(mapBOQDtls.get("WO_WORK_DESCRIPTION")==null ? "" :   mapBOQDtls.get("WO_WORK_DESCRIPTION").toString());

			objBOQBean.setStrMeasurementName(mapBOQDtls.get("WO_MEASURMEN_NAME")==null ? "" :   mapBOQDtls.get("WO_MEASURMEN_NAME").toString());

			objBOQBean.setStrArea(mapBOQDtls.get("WO_WORK_AREA")==null ? "0" :   mapBOQDtls.get("WO_WORK_AREA").toString());

			objBOQBean.setStrBlock(mapBOQDtls.get("BLOCK_NAME")==null ? "-" :   mapBOQDtls.get("BLOCK_NAME").toString());

			objBOQBean.setStrFloor(mapBOQDtls.get("FLOOR_NAME")==null ? "-" :   mapBOQDtls.get("FLOOR_NAME").toString());

			objBOQBean.setStrFlat(mapBOQDtls.get("FLAT_NAME")==null ? "-" :   mapBOQDtls.get("FLAT_NAME").toString());

			objBOQBean.setDoubleLaborRatePerUnit(Double.valueOf(mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT")==null ? "0" :   mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT").toString()));

			objBOQBean.setStrScopeOfWork(mapBOQDtls.get("SCOPE_OF_WORK")==null ? "-" :   mapBOQDtls.get("SCOPE_OF_WORK").toString());
			
			objBOQBean.setRecordType(mapBOQDtls.get("RECORD_TYPE")==null ? "-" :   mapBOQDtls.get("RECORD_TYPE").toString());
			objBOQBean.setWorkAreaId(mapBOQDtls.get("WO_WORK_AREA_ID")==null ? "-" :   mapBOQDtls.get("WO_WORK_AREA_ID").toString());
			
objBOQBean.setMaterialGroupName(mapBOQDtls.get("MATERIAL_GROUP_NAME")==null ? "-" :   mapBOQDtls.get("MATERIAL_GROUP_NAME").toString());
objBOQBean.setMaterialGroupUOM(mapBOQDtls.get("MATERIAL_GROUP_MST_NAME")==null ? "-" :   mapBOQDtls.get("MATERIAL_GROUP_MST_NAME").toString());
objBOQBean.setMaterialTotalQuantity(mapBOQDtls.get("TOTAL_QUANTITY")==null ? "-" :   mapBOQDtls.get("TOTAL_QUANTITY").toString());
objBOQBean.setMaterialTotalAmount(mapBOQDtls.get("TOTAL_AMOUNT")==null ? "-" :   mapBOQDtls.get("TOTAL_AMOUNT").toString());
			/*double area = Double.valueOf(mapBOQDtls.get("WO_WORK_AREA")==null ? "0" :   mapBOQDtls.get("WO_WORK_AREA").toString());
			double price = Double.valueOf(mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT")==null ? "0" :   mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT").toString());
			*/
			double singleWorkCost = Double.valueOf(mapBOQDtls.get("TOTAL_AMOUNT")==null ? "0" :   mapBOQDtls.get("TOTAL_AMOUNT").toString());
			String strSingleWorkCost  = format.format(new BigDecimal(String.valueOf(singleWorkCost)));
			//strSingleWorkCost = strSingleWorkCost.replaceAll("Rs", "");
			strSingleWorkCost = strSingleWorkCost.trim();
			objBOQBean.setSingleWorkCost(strSingleWorkCost);
			selectedWorkTotal+=(singleWorkCost);
			intSerailNo ++;
			list.add(objBOQBean);
			
		}
		String amount=format.format(new BigDecimal(String.valueOf(selectedWorkTotal)));
		amount = amount.replaceAll("Rs", "");
		amount = amount.trim();
		objBOQDetails.setSelectedWorkTotal(amount);
		return list;
	}
	@Override
	public List<BOQBean> getTempBOQMaterialDetails(int tempBOQNo,String workAreaId,BOQBean objBOQDetails){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>>  dbBOQDtls = null;
		int intSerailNo = 1;

		String query = "";
		if(objBOQDetails.getTypeOfWork().equals("PIECEWORK")){
		query = " select QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME, "

			+ " QBAM.WO_WORK_AREA, B.NAME  as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME,QBAM.QS_AREA_PRICE_PER_UNIT,QBAM.QS_AREA_AMOUNT,QBD.SCOPE_OF_WORK "
			+ ",QBAM.RECORD_TYPE,QBAM.WO_WORK_AREA_ID "
			+ ",PGM.MATERIAL_GROUP_NAME,PGM.MATERIAL_GROUP_MST_NAME,QBPD.TOTAL_QUANTITY,QBPD.TOTAL_AMOUNT "

			+ " from   QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR, "
			+ " QS_TEMP_BOQ_PRODUCT_DTLS QBPD,PRODUCT_GROUP_MASTER PGM,"

			+ " QS_TEMP_BOQ_AREA_MAPPING  QBAM left outer join  BLOCK B on B.BLOCK_ID = QBAM.BLOCK_ID  "

			+ " left outer join  FLOOR F on F.FLOOR_ID = QBAM.FLOOR_ID "

			+ " left outer join  FLAT FLAT on FLAT.FLAT_ID = QBAM.FLAT_ID "

			+ " where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "

			+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "

			+ " QBAM.QS_TEMP_BOQ_DETAILS_ID =  QBD.QS_TEMP_BOQ_DETAILS_ID  and    QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "

			+ " QB.TEMP_BOQ_NO = ?  "
			+ " and QBAM.WO_WORK_AREA_ID=QBPD.WO_WORK_AREA_ID and QBAM.TEMP_BOQ_NO=QBPD.TEMP_BOQ_NO "
			+ " and QBPD.MATERIAL_GROUP_ID=PGM.MATERIAL_GROUP_ID and QBPD.MATERIAL_GROUP_MEASUREMENT_ID = PGM.MATERIAL_GROUP_MEASUREMENT_ID "
			+ " and QBPD.WO_WORK_AREA_ID = ? " 
			
			+ " order by to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', '')) ";
		}
		
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {
				tempBOQNo, workAreaId
		});

		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		double selectedWorkTotal = 0;
		for(Map<String, Object> mapBOQDtls : dbBOQDtls) {


			BOQBean objBOQBean = new BOQBean();

			objBOQBean.setIntSerialNumber(intSerailNo);
			objBOQBean.setStrMajorHeadDesc(mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_DESC").toString());

			objBOQBean.setStrMinorHeadDesc(mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MINORHEAD_DESC").toString());

			objBOQBean.setStrWorkDescription(mapBOQDtls.get("WO_WORK_DESCRIPTION")==null ? "" :   mapBOQDtls.get("WO_WORK_DESCRIPTION").toString());

			objBOQBean.setStrMeasurementName(mapBOQDtls.get("WO_MEASURMEN_NAME")==null ? "" :   mapBOQDtls.get("WO_MEASURMEN_NAME").toString());

			objBOQBean.setStrArea(mapBOQDtls.get("WO_WORK_AREA")==null ? "0" :   mapBOQDtls.get("WO_WORK_AREA").toString());

			objBOQBean.setStrBlock(mapBOQDtls.get("BLOCK_NAME")==null ? "-" :   mapBOQDtls.get("BLOCK_NAME").toString());

			objBOQBean.setStrFloor(mapBOQDtls.get("FLOOR_NAME")==null ? "-" :   mapBOQDtls.get("FLOOR_NAME").toString());

			objBOQBean.setStrFlat(mapBOQDtls.get("FLAT_NAME")==null ? "-" :   mapBOQDtls.get("FLAT_NAME").toString());

			objBOQBean.setDoubleLaborRatePerUnit(Double.valueOf(mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT")==null ? "0" :   mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT").toString()));

			objBOQBean.setStrScopeOfWork(mapBOQDtls.get("SCOPE_OF_WORK")==null ? "-" :   mapBOQDtls.get("SCOPE_OF_WORK").toString());
			
			objBOQBean.setRecordType(mapBOQDtls.get("RECORD_TYPE")==null ? "-" :   mapBOQDtls.get("RECORD_TYPE").toString());
			objBOQBean.setWorkAreaId(mapBOQDtls.get("WO_WORK_AREA_ID")==null ? "-" :   mapBOQDtls.get("WO_WORK_AREA_ID").toString());
			
objBOQBean.setMaterialGroupName(mapBOQDtls.get("MATERIAL_GROUP_NAME")==null ? "-" :   mapBOQDtls.get("MATERIAL_GROUP_NAME").toString());
objBOQBean.setMaterialGroupUOM(mapBOQDtls.get("MATERIAL_GROUP_MST_NAME")==null ? "-" :   mapBOQDtls.get("MATERIAL_GROUP_MST_NAME").toString());
objBOQBean.setMaterialTotalQuantity(mapBOQDtls.get("TOTAL_QUANTITY")==null ? "-" :   mapBOQDtls.get("TOTAL_QUANTITY").toString());
objBOQBean.setMaterialTotalAmount(mapBOQDtls.get("TOTAL_AMOUNT")==null ? "-" :   mapBOQDtls.get("TOTAL_AMOUNT").toString());
			/*double area = Double.valueOf(mapBOQDtls.get("WO_WORK_AREA")==null ? "0" :   mapBOQDtls.get("WO_WORK_AREA").toString());
			double price = Double.valueOf(mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT")==null ? "0" :   mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT").toString());
			*/
			double singleWorkCost = Double.valueOf(mapBOQDtls.get("TOTAL_AMOUNT")==null ? "0" :   mapBOQDtls.get("TOTAL_AMOUNT").toString());
			String strSingleWorkCost  = format.format(new BigDecimal(String.valueOf(singleWorkCost)));
			//strSingleWorkCost = strSingleWorkCost.replaceAll("Rs", "");
			strSingleWorkCost = strSingleWorkCost.trim();
			objBOQBean.setSingleWorkCost(strSingleWorkCost);
			selectedWorkTotal+=(singleWorkCost);
			intSerailNo ++;
			list.add(objBOQBean);
			
		}
		String amount=format.format(new BigDecimal(String.valueOf(selectedWorkTotal)));
		//amount = amount.replaceAll("Rs", "");
		//amount = amount.trim();
		objBOQDetails.setSelectedWorkTotal(amount);
		return list;
	}
	@Override
	public List<BOQBean> getTempBOQWorkDetails(int tempBOQNo,String strSiteId,BOQBean objBOQDetails, String majorHead, String minorHead, String workDescription){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>>  dbBOQDtls = null;
		int intSerailNo = 1;

		String query = "";
		if(objBOQDetails.getTypeOfWork().equals("PIECEWORK")){
		query = " select QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME, "

			+ " QBAM.WO_WORK_AREA, B.NAME  as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME,QBAM.QS_AREA_PRICE_PER_UNIT,QBAM.QS_AREA_AMOUNT,QBD.SCOPE_OF_WORK "
			+ ",QBAM.RECORD_TYPE,QBAM.WO_WORK_AREA_ID "

			+ " from   QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR, "

			+ " QS_TEMP_BOQ_AREA_MAPPING  QBAM left outer join  BLOCK B on B.BLOCK_ID = QBAM.BLOCK_ID  "

			+ " left outer join  FLOOR F on F.FLOOR_ID = QBAM.FLOOR_ID "

			+ " left outer join  FLAT FLAT on FLAT.FLAT_ID = QBAM.FLAT_ID "

			+ " where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "

			+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "

			+ " QBAM.QS_TEMP_BOQ_DETAILS_ID =  QBD.QS_TEMP_BOQ_DETAILS_ID  and    QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "

			+ " QB.TEMP_BOQ_NO = ?  " 
			
			+ "OPTIONS"
			
			+ " order by to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', '')) ";
		}
		if(objBOQDetails.getTypeOfWork().equals("NMR")){
		query = " select QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,QUOM.WO_MEASURMEN_NAME " 
			 +" from   QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR "  
			 +" where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID  and QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID  and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "
			 +" and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
			 +" QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and  QB.TEMP_BOQ_NO = ?   "
			 + "OPTIONS"
			 +" order by to_number(QBD.QS_TEMP_BOQ_DETAILS_ID)";
		}
		//BOQNumber = "BO/SIPL/112/8";
		//siteId = 112;
		if(StringUtils.isNotBlank(majorHead)&&StringUtils.isBlank(minorHead)&&StringUtils.isBlank(workDescription)){
			query = query.replace("OPTIONS", " and QWM.WO_MAJORHEAD_ID = '"+majorHead.split("@@")[0]+"' ");
		}
		if(StringUtils.isNotBlank(majorHead)&&StringUtils.isNotBlank(minorHead)&&StringUtils.isBlank(workDescription)){
			query = query.replace("OPTIONS", " and QWM.WO_MAJORHEAD_ID = '"+majorHead.split("@@")[0]+"'  and QSMINOR.WO_MINORHEAD_ID = '"+minorHead.split("@@")[0]+"' ");
		}
		if(StringUtils.isNotBlank(majorHead)&&StringUtils.isNotBlank(minorHead)&&StringUtils.isNotBlank(workDescription)){
			query = query.replace("OPTIONS", " and QWM.WO_MAJORHEAD_ID = '"+majorHead.split("@@")[0]+"'  and QSMINOR.WO_MINORHEAD_ID = '"+minorHead.split("@@")[0]+"'  and QWD.WO_WORK_DESC_ID = '"+workDescription.split("@@")[0]+"' ");
		}
		query = query.replace("OPTIONS", "");
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {
				tempBOQNo
		});

		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		double selectedWorkTotal = 0;
		for(Map<String, Object> mapBOQDtls : dbBOQDtls) {


			BOQBean objBOQBean = new BOQBean();

			objBOQBean.setIntSerialNumber(intSerailNo);
			objBOQBean.setStrMajorHeadDesc(mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_DESC").toString());

			objBOQBean.setStrMinorHeadDesc(mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MINORHEAD_DESC").toString());

			objBOQBean.setStrWorkDescription(mapBOQDtls.get("WO_WORK_DESCRIPTION")==null ? "" :   mapBOQDtls.get("WO_WORK_DESCRIPTION").toString());

			objBOQBean.setStrMeasurementName(mapBOQDtls.get("WO_MEASURMEN_NAME")==null ? "" :   mapBOQDtls.get("WO_MEASURMEN_NAME").toString());

			objBOQBean.setStrArea(mapBOQDtls.get("WO_WORK_AREA")==null ? "0" :   mapBOQDtls.get("WO_WORK_AREA").toString());

			objBOQBean.setStrBlock(mapBOQDtls.get("BLOCK_NAME")==null ? "-" :   mapBOQDtls.get("BLOCK_NAME").toString());

			objBOQBean.setStrFloor(mapBOQDtls.get("FLOOR_NAME")==null ? "-" :   mapBOQDtls.get("FLOOR_NAME").toString());

			objBOQBean.setStrFlat(mapBOQDtls.get("FLAT_NAME")==null ? "-" :   mapBOQDtls.get("FLAT_NAME").toString());

			objBOQBean.setDoubleLaborRatePerUnit(Double.valueOf(mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT")==null ? "0" :   mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT").toString()));

			objBOQBean.setStrScopeOfWork(mapBOQDtls.get("SCOPE_OF_WORK")==null ? "-" :   mapBOQDtls.get("SCOPE_OF_WORK").toString());
		
			objBOQBean.setRecordType(mapBOQDtls.get("RECORD_TYPE")==null ? "-" :   mapBOQDtls.get("RECORD_TYPE").toString());
			objBOQBean.setWorkAreaId(mapBOQDtls.get("WO_WORK_AREA_ID")==null ? "-" :   mapBOQDtls.get("WO_WORK_AREA_ID").toString());

			/*double area = Double.valueOf(mapBOQDtls.get("WO_WORK_AREA")==null ? "0" :   mapBOQDtls.get("WO_WORK_AREA").toString());
			double price = Double.valueOf(mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT")==null ? "0" :   mapBOQDtls.get("QS_AREA_PRICE_PER_UNIT").toString());
			*/
			double singleWorkCost = Double.valueOf(mapBOQDtls.get("QS_AREA_AMOUNT")==null ? "0" :   mapBOQDtls.get("QS_AREA_AMOUNT").toString());
			String strSingleWorkCost  = format.format(new BigDecimal(String.valueOf(singleWorkCost)));
			//strSingleWorkCost = strSingleWorkCost.replaceAll("Rs", "");
			//strSingleWorkCost = strSingleWorkCost.trim();
			objBOQBean.setSingleWorkCost(strSingleWorkCost);
			selectedWorkTotal+=(singleWorkCost);
			intSerailNo ++;
			list.add(objBOQBean);
			
		}
		String amount=format.format(new BigDecimal(String.valueOf(selectedWorkTotal)));
		//amount = amount.replaceAll("Rs", "");
		//amount = amount.trim();
		objBOQDetails.setSelectedWorkTotal(amount);
		return list;
	}
	@Override
	public List<Map<String, Object>> getBOQMajorHeads(int intBOQSeqNumber,String strSiteId,String typeOfWork){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>>  dbBOQDtls = null;
		int intSerailNo = 1;

		String query = "";
		if(typeOfWork.equals("PIECEWORK")){
		/*query = " select QWM.WO_MAJORHEAD_DESC ,min(QWM.WO_MAJORHEAD_ID) as WO_MAJORHEAD_ID "
				+ " from   QS_WO_MAJORHEAD QWM group by QWM.WO_MAJORHEAD_DESC";*/
		
		query = " select QWM.WO_MAJORHEAD_ID,min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC "

			+ " from   QS_BOQ QB,QS_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR, "

			+ " QS_BOQ_AREA_MAPPING  QBAM  "

			

			+ " where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "

			+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "

			+ " QBAM.QS_BOQ_DETAILS_ID =  QBD.QS_BOQ_DETAILS_ID  and    QB.BOQ_SEQ_NO = QBD.BOQ_SEQ_NO and "

			+ " QB.BOQ_SEQ_NO = ?  " 
			
			+ " group by QWM.WO_MAJORHEAD_ID "
			
			+ " order by min(to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', '')))";
		}
		if(typeOfWork.equals("NMR")){
		query = " select QWM.WO_MAJORHEAD_ID,min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC " 
			 +" from   QS_BOQ QB,QS_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR "  
			 +" where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "
			 +" and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
			 +" QB.BOQ_SEQ_NO = QBD.BOQ_SEQ_NO and  QB.BOQ_SEQ_NO = ?   "
			 //+" order by to_number(QBD.QS_BOQ_DETAILS_ID)";
			 + " group by QWM.WO_MAJORHEAD_ID "
			 +" order by min(to_number(QBD.QS_BOQ_DETAILS_ID))";
		}
		//BOQNumber = "BO/SIPL/112/8";
		//siteId = 112;
		
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {
				intBOQSeqNumber
		});

		return dbBOQDtls;
	}
	@Override
	public List<Map<String, Object>> getTempBOQMajorHeads(int tempBOQNumber,String strSiteId,String typeOfWork){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>>  dbBOQDtls = null;
		int intSerailNo = 1;

		String query = "";
		if(typeOfWork.equals("PIECEWORK")){
		/*query = " select QWM.WO_MAJORHEAD_DESC ,min(QWM.WO_MAJORHEAD_ID) as WO_MAJORHEAD_ID "
				+ " from   QS_WO_MAJORHEAD QWM group by QWM.WO_MAJORHEAD_DESC";*/
		
		query = " select QWM.WO_MAJORHEAD_ID,min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC "

			+ " from   QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR, "

			+ " QS_TEMP_BOQ_AREA_MAPPING  QBAM  "

			

			+ " where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "

			+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "

			+ " QBAM.QS_TEMP_BOQ_DETAILS_ID =  QBD.QS_TEMP_BOQ_DETAILS_ID  and    QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "

			+ " QB.TEMP_BOQ_NO = ?  " 
			
			+ " group by QWM.WO_MAJORHEAD_ID "
			
			+ " order by min(to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', '')))";
		}
		if(typeOfWork.equals("NMR")){
		query = " select QWM.WO_MAJORHEAD_ID,min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC " 
			 +" from   QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR "  
			 +" where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "
			 +" and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
			 +" QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and  QB.TEMP_BOQ_NO = ?   "
			 //+" order by to_number(QBD.QS_BOQ_DETAILS_ID)";
			 + " group by QWM.WO_MAJORHEAD_ID "
			 +" order by min(to_number(QBD.QS_TEMP_BOQ_DETAILS_ID))";
		}
		//BOQNumber = "BO/SIPL/112/8";
		//siteId = 112;
		
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {
				tempBOQNumber
		});

		return dbBOQDtls;
	}

	
	@Override
	public List<BOQBean> getBOQMajorHeadsDetails(int intBOQSeqNumber,String strSiteId,String typeOfWork){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>>  dbBOQDtls = null;
		int intSerailNo = 1;

		String query = "";
		if(typeOfWork.equals("PIECEWORK")){
		/*query = " select QWM.WO_MAJORHEAD_DESC ,min(QWM.WO_MAJORHEAD_ID) as WO_MAJORHEAD_ID "
				+ " from   QS_WO_MAJORHEAD QWM group by QWM.WO_MAJORHEAD_DESC";*/
		
		query = " select QWM.WO_MAJORHEAD_ID,min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC "
				+ ",sum(QBAM.QS_AREA_AMOUNT) as MAJORHEADTOTALAMOUNT "
				+ ",SUM(case when QBAM.RECORD_TYPE = 'MATERIAL' then QBAM.QS_AREA_AMOUNT else 0 end) as  MAJORHEADMATERIALTOTALAMOUNT " 
		        + ",SUM(case when QBAM.RECORD_TYPE = 'LABOR' then QBAM.QS_AREA_AMOUNT else 0 end) as  MAJORHEADLABORTOTALAMOUNT " 

			+ " from   QS_BOQ QB,QS_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR, "

			+ " QS_BOQ_AREA_MAPPING  QBAM  "

			

			+ " where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "

			+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "

			+ " QBAM.QS_BOQ_DETAILS_ID =  QBD.QS_BOQ_DETAILS_ID  and    QB.BOQ_SEQ_NO = QBD.BOQ_SEQ_NO and "

			+ " QB.BOQ_SEQ_NO = ?  " 
			
			+ " group by QWM.WO_MAJORHEAD_ID "
			
			+ " order by min(to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', '')))";
		}
		if(typeOfWork.equals("NMR")){
		query = " select QWM.WO_MAJORHEAD_ID,min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC "
				+ ",'0' as MAJORHEADTOTALAMOUNT "
				+ ",'0' as MAJORHEADMATERIALTOTALAMOUNT "
				+ ",'0' as MAJORHEADLABORTOTALAMOUNT "
			 +" from   QS_BOQ QB,QS_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR "  
			 +" where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID  and QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID  and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "
			 +" and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
			 +" QB.BOQ_SEQ_NO = QBD.BOQ_SEQ_NO and  QB.BOQ_SEQ_NO = ?   "
			 //+" order by to_number(QBD.QS_BOQ_DETAILS_ID)";
			 + " group by QWM.WO_MAJORHEAD_ID "
			 +" order by min(to_number(QBD.QS_BOQ_DETAILS_ID))";
		}
		//BOQNumber = "BO/SIPL/112/8";
		//siteId = 112;
		
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {
				intBOQSeqNumber
		});
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
		for(Map<String, Object> mapBOQDtls : dbBOQDtls) {


			BOQBean objBOQBean = new BOQBean();

			objBOQBean.setIntSerialNumber(intSerailNo);
			objBOQBean.setStrMajorHeadDesc(mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_DESC").toString());
			objBOQBean.setStrMajorHeadId(mapBOQDtls.get("WO_MAJORHEAD_ID")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_ID").toString());
			String majorHeadTotalAmount = mapBOQDtls.get("MAJORHEADTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MAJORHEADTOTALAMOUNT").toString();
			String majorHeadMaterialTotalAmount = mapBOQDtls.get("MAJORHEADMATERIALTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MAJORHEADMATERIALTOTALAMOUNT").toString();
			String majorHeadLaborTotalAmount = mapBOQDtls.get("MAJORHEADLABORTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MAJORHEADLABORTOTALAMOUNT").toString();
			
			majorHeadTotalAmount = format.format(new BigDecimal(String.valueOf(majorHeadTotalAmount)));
			//majorHeadTotalAmount = majorHeadTotalAmount.replaceAll("Rs", "");
			//majorHeadTotalAmount = majorHeadTotalAmount.trim();
			objBOQBean.setStrMajorHeadTotalAmount(majorHeadTotalAmount);
			objBOQBean.setStrMajorHeadMaterialTotalAmount(format.format(new BigDecimal(majorHeadMaterialTotalAmount)));
			objBOQBean.setStrMajorHeadLaborTotalAmount(format.format(new BigDecimal(majorHeadLaborTotalAmount)));
			intSerailNo ++;
			list.add(objBOQBean);
			
		}
		return list;
	}
	@Override
	public List<BOQBean> getTempBOQMajorHeadsDetails(int tempBOQNo,String strSiteId,String typeOfWork){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>>  dbBOQDtls = null;
		int intSerailNo = 1;

		String query = "";
		if(typeOfWork.equals("PIECEWORK")){
		/*query = " select QWM.WO_MAJORHEAD_DESC ,min(QWM.WO_MAJORHEAD_ID) as WO_MAJORHEAD_ID "
				+ " from   QS_WO_MAJORHEAD QWM group by QWM.WO_MAJORHEAD_DESC";*/
		
		query = " select QWM.WO_MAJORHEAD_ID,min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC "
				+ ",sum(QBAM.QS_AREA_AMOUNT) as MAJORHEADTOTALAMOUNT "
				+ ",SUM(case when QBAM.RECORD_TYPE = 'MATERIAL' then QBAM.QS_AREA_AMOUNT else 0 end) as  MAJORHEADMATERIALTOTALAMOUNT " 
		        + ",SUM(case when QBAM.RECORD_TYPE = 'LABOR' then QBAM.QS_AREA_AMOUNT else 0 end) as  MAJORHEADLABORTOTALAMOUNT " 

			+ " from   QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR, "

			+ " QS_TEMP_BOQ_AREA_MAPPING  QBAM  "

			

			+ " where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "

			+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "

			+ " QBAM.QS_TEMP_BOQ_DETAILS_ID =  QBD.QS_TEMP_BOQ_DETAILS_ID  and    QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "

			+ " QB.TEMP_BOQ_NO = ?  " 
			
			+ " group by QWM.WO_MAJORHEAD_ID "
			
			+ " order by min(to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', '')))";
		}
		if(typeOfWork.equals("NMR")){
		query = " select QWM.WO_MAJORHEAD_ID,min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC "
				+ ",'0' as MAJORHEADTOTALAMOUNT "
				
			 +" from   QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR "  
			 +" where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID  and QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID  and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "
			 +" and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
			 +" QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and  QB.TEMP_BOQ_NO = ?   "
			 //+" order by to_number(QBD.QS_BOQ_DETAILS_ID)";
			 + " group by QWM.WO_MAJORHEAD_ID "
			 +" order by min(to_number(QBD.QS_TEMP_BOQ_DETAILS_ID))";
		}
		//BOQNumber = "BO/SIPL/112/8";
		//siteId = 112;
		
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {
				tempBOQNo
		});
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
		for(Map<String, Object> mapBOQDtls : dbBOQDtls) {


			BOQBean objBOQBean = new BOQBean();

			objBOQBean.setIntSerialNumber(intSerailNo);
			objBOQBean.setStrMajorHeadDesc(mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_DESC").toString());
			objBOQBean.setStrMajorHeadId(mapBOQDtls.get("WO_MAJORHEAD_ID")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_ID").toString());
			String majorHeadTotalAmount = mapBOQDtls.get("MAJORHEADTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MAJORHEADTOTALAMOUNT").toString();
			String majorHeadMaterialTotalAmount = mapBOQDtls.get("MAJORHEADMATERIALTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MAJORHEADMATERIALTOTALAMOUNT").toString();
			String majorHeadLaborTotalAmount = mapBOQDtls.get("MAJORHEADLABORTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MAJORHEADLABORTOTALAMOUNT").toString();
			
			majorHeadTotalAmount = format.format(new BigDecimal(String.valueOf(majorHeadTotalAmount)));
			//majorHeadTotalAmount = majorHeadTotalAmount.replaceAll("Rs", "");
			//majorHeadTotalAmount = majorHeadTotalAmount.trim();
			objBOQBean.setStrMajorHeadTotalAmount(majorHeadTotalAmount);
			objBOQBean.setStrMajorHeadMaterialTotalAmount(format.format(new BigDecimal(majorHeadMaterialTotalAmount)));
			objBOQBean.setStrMajorHeadLaborTotalAmount(format.format(new BigDecimal(majorHeadLaborTotalAmount)));
			objBOQBean.setStrSiteId(strSiteId);
			objBOQBean.setIntTempBOQNo(tempBOQNo);
			
			intSerailNo ++;
			list.add(objBOQBean);
			
		}
		return list;
	}
	@Override
	public List<BOQBean> getBOQMinorHeadsDetails(int intBOQSeqNumber,String strSiteId,String typeOfWork,String majorHeadId){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>>  dbBOQDtls = null;
		int intSerailNo = 1;

		String query = "";
		if(typeOfWork.equals("PIECEWORK")){
		
		query = " select QSMINOR.WO_MINORHEAD_ID,min(QSMINOR.WO_MINORHEAD_DESC) as WO_MINORHEAD_DESC "
				+ ",sum(QBAM.QS_AREA_AMOUNT) as MINORHEADTOTALAMOUNT "
				+ ",SUM(case when QBAM.RECORD_TYPE = 'MATERIAL' then QBAM.QS_AREA_AMOUNT else 0 end) as  MINORHEADMATERIALTOTALAMOUNT " 
		        + ",SUM(case when QBAM.RECORD_TYPE = 'LABOR' then QBAM.QS_AREA_AMOUNT else 0 end) as  MINORHEADLABORTOTALAMOUNT " 

				+ ",min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC ,min(QWM.WO_MAJORHEAD_ID) as WO_MAJORHEAD_ID "

			+ " from   QS_BOQ QB,QS_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR, "

			+ " QS_BOQ_AREA_MAPPING  QBAM  "

			

			+ " where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "

			+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "

			+ " QBAM.QS_BOQ_DETAILS_ID =  QBD.QS_BOQ_DETAILS_ID  and    QB.BOQ_SEQ_NO = QBD.BOQ_SEQ_NO and "

			+ " QB.BOQ_SEQ_NO = ?  " 
			+ " and QWM.WO_MAJORHEAD_ID = ? "
			+ " group by QSMINOR.WO_MINORHEAD_ID "
			
			+ " order by min(to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', '')))";
		}
		if(typeOfWork.equals("NMR")){
		query = " select QSMINOR.WO_MINORHEAD_ID,min(QSMINOR.WO_MINORHEAD_DESC) as WO_MINORHEAD_DESC "
				+ ",'0' as MINORHEADTOTALAMOUNT "
				+ ",'0' as MINORHEADMATERIALTOTALAMOUNT "
				+ ",'0' as MINORHEADLABORTOTALAMOUNT "
			+ ",min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC ,min(QWM.WO_MAJORHEAD_ID) as WO_MAJORHEAD_ID "
				
			 +" from   QS_BOQ QB,QS_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR "  
			 +" where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID  and QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "
			 +" and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
			 +" QB.BOQ_SEQ_NO = QBD.BOQ_SEQ_NO and  QB.BOQ_SEQ_NO = ?   "
			 + " and QWM.WO_MAJORHEAD_ID = ? "
			 //+" order b-y to_number(QBD.QS_BOQ_DETAILS_ID)";
			 + " group by QSMINOR.WO_MINORHEAD_ID "
			 +" order by min(to_number(QBD.QS_BOQ_DETAILS_ID))";
		}
		//BOQNumber = "BO/SIPL/112/8";
		//siteId = 112;
		
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {
				intBOQSeqNumber,majorHeadId
		});
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
		for(Map<String, Object> mapBOQDtls : dbBOQDtls) {


			BOQBean objBOQBean = new BOQBean();

			objBOQBean.setIntSerialNumber(intSerailNo);
			objBOQBean.setStrMajorHeadDesc(mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_DESC").toString());
			objBOQBean.setStrMajorHeadId(mapBOQDtls.get("WO_MAJORHEAD_ID")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_ID").toString());
			objBOQBean.setStrMinorHeadDesc(mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MINORHEAD_DESC").toString());
			objBOQBean.setStrMinorHeadId(mapBOQDtls.get("WO_MINORHEAD_ID")==null ? "" :   mapBOQDtls.get("WO_MINORHEAD_ID").toString());
			String minorHeadTotalAmount = mapBOQDtls.get("MINORHEADTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MINORHEADTOTALAMOUNT").toString();
			String minorHeadMaterialTotalAmount = mapBOQDtls.get("MINORHEADMATERIALTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MINORHEADMATERIALTOTALAMOUNT").toString();
			String minorHeadLaborTotalAmount = mapBOQDtls.get("MINORHEADLABORTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MINORHEADLABORTOTALAMOUNT").toString();
			
			minorHeadTotalAmount = format.format(new BigDecimal(String.valueOf(minorHeadTotalAmount)));
			//minorHeadTotalAmount = minorHeadTotalAmount.replaceAll("Rs", "");
			//minorHeadTotalAmount = minorHeadTotalAmount.trim();
			objBOQBean.setStrMinorHeadTotalAmount(minorHeadTotalAmount);
			objBOQBean.setStrMinorHeadMaterialTotalAmount(format.format(new BigDecimal(minorHeadMaterialTotalAmount)));
			objBOQBean.setStrMinorHeadLaborTotalAmount(format.format(new BigDecimal(minorHeadLaborTotalAmount)));
			
			intSerailNo ++;
			list.add(objBOQBean);
			
		}
		return list;
	}
	@Override
	public List<BOQBean> getTempBOQMinorHeadsDetails(int tempBOQNo,String strSiteId,String typeOfWork,String majorHeadId){


		List<BOQBean> list = new ArrayList<BOQBean>();
		List<Map<String, Object>>  dbBOQDtls = null;
		int intSerailNo = 1;

		String query = "";
		if(typeOfWork.equals("PIECEWORK")){
		
		query = " select QSMINOR.WO_MINORHEAD_ID,min(QSMINOR.WO_MINORHEAD_DESC) as WO_MINORHEAD_DESC "
				+ ",sum(QBAM.QS_AREA_AMOUNT) as MAJORHEADTOTALAMOUNT "
				+ ",SUM(case when QBAM.RECORD_TYPE = 'MATERIAL' then QBAM.QS_AREA_AMOUNT else 0 end) as  MINORHEADMATERIALTOTALAMOUNT " 
		        + ",SUM(case when QBAM.RECORD_TYPE = 'LABOR' then QBAM.QS_AREA_AMOUNT else 0 end) as  MINORHEADLABORTOTALAMOUNT " 
		        + ",min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC ,min(QWM.WO_MAJORHEAD_ID) as WO_MAJORHEAD_ID "

			+ " from   QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR, "

			+ " QS_TEMP_BOQ_AREA_MAPPING  QBAM  "

			

			+ " where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "

			+ " and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "

			+ " QBAM.QS_TEMP_BOQ_DETAILS_ID =  QBD.QS_TEMP_BOQ_DETAILS_ID  and    QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and "

			+ " QB.TEMP_BOQ_NO = ?  " 
			+ " and QWM.WO_MAJORHEAD_ID = ? "
			+ " group by QSMINOR.WO_MINORHEAD_ID "
			
			+ " order by min(to_number(regexp_replace(QBAM.WO_WORK_AREA_ID, '[^0-9]', '')))";
		}
		if(typeOfWork.equals("NMR")){
		query = " select QSMINOR.WO_MINORHEAD_ID,min(QSMINOR.WO_MINORHEAD_DESC) as WO_MINORHEAD_DESC "
				+ ",'0' as MAJORHEADTOTALAMOUNT "
				+ ",min(QWM.WO_MAJORHEAD_DESC) as WO_MAJORHEAD_DESC ,min(QWM.WO_MAJORHEAD_ID) as WO_MAJORHEAD_ID "
				
			 +" from   QS_TEMP_BOQ QB,QS_TEMP_BOQ_DETAILS QBD,QS_WO_MAJORHEAD QWM,QS_WO_MEASURMENT QUOM,QS_WO_WORKDESC QWD,QS_WO_MINORHEAD  QSMINOR "  
			 +" where QBD.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID  and QBD.WO_MINORHEAD_ID = QWD.WO_MINORHEAD_ID and  QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID  "
			 +" and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID and  QBD.WO_MEASURMENT_ID = QUOM.WO_MEASURMENT_ID and "
			 +" QB.TEMP_BOQ_NO = QBD.TEMP_BOQ_NO and  QB.TEMP_BOQ_NO = ?   "
			 + " and QWM.WO_MAJORHEAD_ID = ? "
			 //+" order b-y to_number(QBD.QS_BOQ_DETAILS_ID)";
			 + " group by QSMINOR.WO_MINORHEAD_ID "
			 +" order by min(to_number(QBD.QS_TEMP_BOQ_DETAILS_ID))";
		}
		//BOQNumber = "BO/SIPL/112/8";
		//siteId = 112;
		
		dbBOQDtls = jdbcTemplate.queryForList(query, new Object[] {
				tempBOQNo,majorHeadId
		});
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		
		for(Map<String, Object> mapBOQDtls : dbBOQDtls) {


			BOQBean objBOQBean = new BOQBean();

			objBOQBean.setIntSerialNumber(intSerailNo);
			objBOQBean.setStrMajorHeadDesc(mapBOQDtls.get("WO_MAJORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_DESC").toString());
			objBOQBean.setStrMajorHeadId(mapBOQDtls.get("WO_MAJORHEAD_ID")==null ? "" :   mapBOQDtls.get("WO_MAJORHEAD_ID").toString());
			objBOQBean.setStrMinorHeadDesc(mapBOQDtls.get("WO_MINORHEAD_DESC")==null ? "" :   mapBOQDtls.get("WO_MINORHEAD_DESC").toString());
			objBOQBean.setStrMinorHeadId(mapBOQDtls.get("WO_MINORHEAD_ID")==null ? "" :   mapBOQDtls.get("WO_MINORHEAD_ID").toString());
			String majorHeadTotalAmount = mapBOQDtls.get("MAJORHEADTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MAJORHEADTOTALAMOUNT").toString();
			String minorHeadMaterialTotalAmount = mapBOQDtls.get("MINORHEADMATERIALTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MINORHEADMATERIALTOTALAMOUNT").toString();
			String minorHeadLaborTotalAmount = mapBOQDtls.get("MINORHEADLABORTOTALAMOUNT")==null ? "0" :   mapBOQDtls.get("MINORHEADLABORTOTALAMOUNT").toString();
			
			majorHeadTotalAmount = format.format(new BigDecimal(String.valueOf(majorHeadTotalAmount)));
			//majorHeadTotalAmount = majorHeadTotalAmount.replaceAll("Rs", "");
			//majorHeadTotalAmount = majorHeadTotalAmount.trim();
			objBOQBean.setStrMinorHeadTotalAmount(majorHeadTotalAmount);
			objBOQBean.setStrMinorHeadMaterialTotalAmount(format.format(new BigDecimal(minorHeadMaterialTotalAmount)));
			objBOQBean.setStrMinorHeadLaborTotalAmount(format.format(new BigDecimal(minorHeadLaborTotalAmount)));
			
			intSerailNo ++;
			list.add(objBOQBean);
			
		}
		return list;
	}
	@Override
	public int getTempBOQDetailsId(String workDescriptionId, int tempBOQNo, String measurementId, String recordType) {
		String query = "select QS_TEMP_BOQ_DETAILS_ID from QS_TEMP_BOQ_DETAILS where WO_WORK_DESC_ID = '"+workDescriptionId+"' and WO_MEASURMENT_ID = '"+measurementId+"' and TEMP_BOQ_NO = '"+tempBOQNo+"' and RECORD_TYPE = '"+recordType+"'";
		//System.out.println("WO_WORK_DESC_ID = '"+workDescriptionId+"'");
		int tempBOQDetailsId = 0;
		try{
			tempBOQDetailsId = jdbcTemplate.queryForInt(query);
		}
		catch(EmptyResultDataAccessException e){}
		return tempBOQDetailsId;
	}

	@Override
	public int updateQsTempBOQDetails(int tempBOQDetailsId, double totalAreaOfAllBlocks,double totalAmountForAllBlocks) {
		String query = " update QS_TEMP_BOQ_DETAILS set WO_WORK_AREA = WO_WORK_AREA+?,  WO_WORK_AREA_AMOUNT = WO_WORK_AREA_AMOUNT+? "
					 + " where QS_TEMP_BOQ_DETAILS_ID = ? ";
		int count = jdbcTemplate.update(query,new Object[]{totalAreaOfAllBlocks, totalAmountForAllBlocks,tempBOQDetailsId});
		return count;
	}
	@Override
	public int updateQsTempBOQDetails(BOQDetailsDto objBOQDetailsDto) {
		String query = " update QS_TEMP_BOQ_DETAILS set WO_WORK_AREA = WO_WORK_AREA+?,  WO_WORK_AREA_AMOUNT = WO_WORK_AREA_AMOUNT+? "
					 + " where QS_TEMP_BOQ_DETAILS_ID = ? ";
		int count = jdbcTemplate.update(query,new Object[]{objBOQDetailsDto.getDoubleTotalAreaOfAllBlocks(), objBOQDetailsDto.getDoubleTotalAmountForAllBlocks(),objBOQDetailsDto.getTempBOQDetailsId()});
		return count;
	}

	@Override
	public int updateQsTempBOQDetailsWithAction(int tempBOQDetailsId, double totalAreaOfAllBlocks,double totalAmountForAllBlocks, String action) {
		String query = " update QS_TEMP_BOQ_DETAILS set WO_WORK_AREA = WO_WORK_AREA+?,  WO_WORK_AREA_AMOUNT = WO_WORK_AREA_AMOUNT+?, ACTION = ? "
					 + " where QS_TEMP_BOQ_DETAILS_ID = ? ";
		int count = jdbcTemplate.update(query,new Object[]{totalAreaOfAllBlocks, totalAmountForAllBlocks,action,tempBOQDetailsId});
		return count;
	}
	
	

	@Override
	public String getPendingEmpId(String user_id, String site_id) {
		String query = "select APPROVER_EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? and SITE_ID = ? and MODULE_TYPE = 'BOQ'  ";
		String pendingEmpId = jdbcTemplate.queryForObject(query, new Object[]{user_id, site_id},String.class);
		return pendingEmpId;
	}

	@Override
	public int getBlockIdIfItIsThere(String blockName, String site_id) {
		String query = "select BLOCK_ID from BLOCK where lower(NAME) = '"+blockName.toLowerCase()+"' and SITE_ID = '"+site_id+"'";
		//System.out.println("Block Name = '"+blockName.toLowerCase()+"' and SITE_ID = '"+site_id+"'");
		int blockId = 0;
		try{
			blockId = jdbcTemplate.queryForInt(query);
		}catch(EmptyResultDataAccessException e){}
		return blockId;
	}

	@Override
	public int getMaxBlockIdInBlockTable() {
		String query = "select max(to_number(BLOCK_ID)) from BLOCK";
		
		return jdbcTemplate.queryForInt(query);
	}
	@Override
	public int getMaxFloorIdInFloorTable() {
		String query = "select max(to_number(FLOOR_ID)) from FLOOR";
		
		return jdbcTemplate.queryForInt(query);
	}
	
	@Override
	public int createBlockId(int blockId, String blockName, String site_id) {
		String query = "insert into BLOCK(BLOCK_ID,NAME,SITE_ID) values(?,?,?)";
		int count = jdbcTemplate.update(query,new Object[]{blockId, blockName, site_id});
		return count;
	}
	
	@Override
	public int getFloorIdIfItIsThere(String floorName, int blockId) {
		String query = "select FLOOR_ID from FLOOR where lower(NAME) = '"+floorName.toLowerCase()+"' and BLOCK_ID = '"+blockId+"'";
		//System.out.println("FLOOR_NAME = '"+floorName.toLowerCase()+"' and BLOCK_ID = '"+blockId+"'");
		int floorId = 0;
		try{ 
			floorId = jdbcTemplate.queryForInt(query);
		}catch(EmptyResultDataAccessException e){}
		return floorId;
	}

	@Override
	public int createFloorId(int floorId, String floorName, int blockId) {
		String query = "insert into FLOOR(FLOOR_ID,NAME,BLOCK_ID) values(?,?,?)";
		int count = jdbcTemplate.update(query,new Object[]{floorId, floorName, blockId});
		return count;
	}
	@Override
	public String getSiteNameBySiteId(String siteId) {
		String query = "select SITE_NAME  from SITE where SITE_ID = ? ";
		return jdbcTemplate.queryForObject(query, new Object[]{siteId},String.class);
	}

	@Override
	public int updateGrandTotalInQsTempBOQ(int tempBOQNo, double givenGrandTotalForVerification, double givenLaborTotalForVerification, double givenMaterialTotalForVerification) {
		String query = "update QS_TEMP_BOQ set BOQ_TOTAL_AMOUNT=?, BOQ_LABOR_AMOUNT=?, BOQ_MATERIAL_AMOUNT=? where TEMP_BOQ_NO=?";
		return jdbcTemplate.update(query,new Object[]{givenGrandTotalForVerification,givenLaborTotalForVerification,givenMaterialTotalForVerification,tempBOQNo});
	}

	@Override
	public int getTempBOQNoOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork) {
		String query = "select TEMP_BOQ_NO  from QS_BOQ where SITE_ID = ? and TYPE_OF_WORK = ? and STATUS = 'A'";
		return Integer.parseInt(jdbcTemplate.queryForObject(query, new Object[]{boqSiteId,typeOfWork},String.class));
	}
	@Override
	public String getVersionNoOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork) {
		String query = "select VERSION_NO  from QS_BOQ where SITE_ID = ? and TYPE_OF_WORK = ? and STATUS = 'A'";
		return jdbcTemplate.queryForObject(query, new Object[]{boqSiteId,typeOfWork},String.class);
	}
	@Override
	public int getBOQSeqNoOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork) {
		String query = "select BOQ_SEQ_NO  from QS_BOQ where SITE_ID = ? and TYPE_OF_WORK = ? and STATUS = 'A'";
		return Integer.parseInt(jdbcTemplate.queryForObject(query, new Object[]{boqSiteId,typeOfWork},String.class));
	}
	@Override
	public String getBOQNoOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork) {
		String query = "select BOQ_NO  from QS_BOQ where SITE_ID = ? and TYPE_OF_WORK = ? and STATUS = 'A'";
		return jdbcTemplate.queryForObject(query, new Object[]{boqSiteId,typeOfWork},String.class);
	}
	@Override
	public String getBOQTotalOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork) {
		String query = "select BOQ_TOTAL_AMOUNT  from QS_BOQ where SITE_ID = ? and TYPE_OF_WORK = ? and STATUS = 'A'";
		return jdbcTemplate.queryForObject(query, new Object[]{boqSiteId,typeOfWork},String.class);
	}
	@Override
	public String getBOQLaborTotalOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork) {
		String query = "select BOQ_LABOR_AMOUNT  from QS_BOQ where SITE_ID = ? and TYPE_OF_WORK = ? and STATUS = 'A'";
		return jdbcTemplate.queryForObject(query, new Object[]{boqSiteId,typeOfWork},String.class);
	}
	@Override
	public String getBOQMaterialTotalOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork) {
		String query = "select BOQ_MATERIAL_AMOUNT  from QS_BOQ where SITE_ID = ? and TYPE_OF_WORK = ? and STATUS = 'A'";
		return jdbcTemplate.queryForObject(query, new Object[]{boqSiteId,typeOfWork},String.class);
	}
	@Override
	public MultiObject getTotalAmountOfCurrentTempBOQ(int tempBOQNo) {
		String query = "select BOQ_TOTAL_AMOUNT,BOQ_MATERIAL_AMOUNT,BOQ_LABOR_AMOUNT  from QS_TEMP_BOQ where TEMP_BOQ_NO = ? ";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(query, new Object[]{tempBOQNo});
		if(list.size()!=1){throw new RuntimeException("Expected:1 Result:"+list.size());}
		MultiObject multiObj = new MultiObject();
		multiObj.setString1(list.get(0).get("BOQ_TOTAL_AMOUNT")==null?"0":list.get(0).get("BOQ_TOTAL_AMOUNT").toString());
		multiObj.setString2(list.get(0).get("BOQ_MATERIAL_AMOUNT")==null?"0":list.get(0).get("BOQ_MATERIAL_AMOUNT").toString());
		multiObj.setString3(list.get(0).get("BOQ_LABOR_AMOUNT")==null?"0":list.get(0).get("BOQ_LABOR_AMOUNT").toString());
		
		return multiObj;
		
	}

	@Override
	public BOQBean getBOQAreaMappingOfParticularBlock(int BOQSeqNo, String workDescriptionId, String measurementId,String blockId, String floorId, String recordType) {
		String query = "select QS_AREA_PRICE_PER_UNIT,WO_WORK_AREA,WO_WORK_INITIATE_AREA,WO_WORK_AREA_ID  from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = ? and WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID = ?  and BLOCK_ID = ? and RECORD_TYPE = ? ";
		if(StringUtils.isBlank(floorId)){
			query = query + " and FLOOR_ID is null ";
		}else{
		query = query + " and FLOOR_ID = '"+floorId+"' ";
		}
		System.out.println(BOQSeqNo+","+ workDescriptionId+","+ blockId+","+ floorId+","+ recordType);
		BOQBean bean= new BOQBean();
		try{
			Map<String,Object> map = jdbcTemplate.queryForMap(query, new Object[]{BOQSeqNo, workDescriptionId,measurementId, blockId, recordType});
			bean.setQsAreaPricePerUnit(map.get("QS_AREA_PRICE_PER_UNIT").toString());
			bean.setWoWorkArea(map.get("WO_WORK_AREA").toString());
			bean.setWoWorkInitiateArea(map.get("WO_WORK_INITIATE_AREA").toString());
			String WO_WORK_AREA_ID = map.get("WO_WORK_AREA_ID").toString();
			bean.setWorkAreaId(WO_WORK_AREA_ID);
			/*String sql = "select max(price) from ("
					+"select max(PRICE) as price from QS_WORKORDER_ISSUE_AREA_DETAILS where WO_WORK_AREA_ID = '"+WO_WORK_AREA_ID+"'"
					 +"union "
					+"select max(PRICE) as price from QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS where WO_WORK_AREA_ID = '"+WO_WORK_AREA_ID+"'"
					+")";*/
			String sql = "select max(price) from ("
					+" select max(PRICE) as price from QS_WORKORDER_ISSUE_AREA_DETAILS tab3,QS_WORKORDER_ISSUE_DETAILS tab2,QS_WORKORDER_ISSUE tab1 " 
					+" where tab3.WO_WORK_AREA_ID = '"+WO_WORK_AREA_ID+"' and tab3.WO_WORK_ISSUE_DTLS_ID = tab2.WO_WORK_ISSUE_DTLS_ID " 
					+" and tab2.QS_WORKORDER_ISSUE_ID=tab1.QS_WORKORDER_ISSUE_ID and tab1.STATUS='A' "
					  +" union "
					+" select  max(QWTIAD.PRICE) as price from QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QWTIAD,QS_WORKORDER_TEMP_ISSUE_DTLS QWTID,QS_WORKORDER_TEMP_ISSUE QWTI where QWTIAD.WO_WORK_AREA_ID = '"+WO_WORK_AREA_ID+"'"
					+" and QWTIAD.WO_WORK_TEMP_ISSUE_DTLS_ID = QWTID.WO_WORK_TEMP_ISSUE_DTLS_ID and QWTID.QS_WO_TEMP_ISSUE_ID = QWTI.QS_WO_TEMP_ISSUE_ID and (QWTI.STATUS = 'A' or QWTI.STATUS = 'DF'))";
			
			String price = jdbcTemplate.queryForObject(sql,new Object[]{},String.class);
			bean.setWoIssueAreaPrice(price==null?"0":price);
		}
		catch(EmptyResultDataAccessException e){
			String query2 = "select QS_AREA_PRICE_PER_UNIT,WO_WORK_AREA,WO_WORK_INITIATE_AREA  from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = ? and WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID = ?   ";
			/*if(StringUtils.isBlank(floorId)){
				query2 = query2 + " and FLOOR_ID is null ";
			}else{
			query2 = query2 + " and FLOOR_ID = '"+floorId+"' ";
			}*/
			List<Map<String,Object>> list = jdbcTemplate.queryForList(query2, new Object[]{BOQSeqNo, workDescriptionId,measurementId});
			if(list!=null&&list.size()>0){
				bean.setQsAreaPricePerUnit("0");
				bean.setWoWorkArea("0");
				bean.setWoWorkInitiateArea("0");
				bean.setWoIssueAreaPrice("0");
				bean.setWorkAreaId("");
			}
			else{
				throw e;
			}
		}
		return bean;
	}
	
	@Override
	public String getParticularBlockPrice(int BOQSeqNo, String workDescriptionId, String measurementId,String blockId, String floorId, String recordType) {
		String query = "select QS_AREA_PRICE_PER_UNIT  from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = ? and WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID = ?  and BLOCK_ID = ? and RECORD_TYPE = ? ";
		if(StringUtils.isBlank(floorId)){
			query = query + " and FLOOR_ID is null ";
		}else{
		query = query + " and FLOOR_ID = '"+floorId+"' ";
		}
		System.out.println(BOQSeqNo+","+ workDescriptionId+","+ blockId+","+ floorId);
		return jdbcTemplate.queryForObject(query, new Object[]{BOQSeqNo, workDescriptionId,measurementId, blockId, recordType},String.class);
	}
	@Override
	public String getParticularBlockArea(int BOQSeqNo, String workDescriptionId,String measurementId, String blockId, String floorId, String recordType) {
		String query = "select WO_WORK_AREA  from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = ? and WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID = ?  and BLOCK_ID = ? and RECORD_TYPE = ? ";
		if(StringUtils.isBlank(floorId)){
			query = query + " and FLOOR_ID is null ";
		}else{
		query = query + " and FLOOR_ID = '"+floorId+"' ";
		}
		return jdbcTemplate.queryForObject(query, new Object[]{BOQSeqNo, workDescriptionId,measurementId, blockId, recordType},String.class);
	}

	@Override
	public String getWorkOrderIntiateArea(int BOQSeqNo, String workDescriptionId,String measurementId, String blockId, String floorId, String recordType) {
		String query = "select WO_WORK_INITIATE_AREA  from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = ? and WO_WORK_DESC_ID = ?  and WO_MEASURMENT_ID = ? and BLOCK_ID = ? and RECORD_TYPE = ? ";
		if(StringUtils.isBlank(floorId)){
			query = query + " and FLOOR_ID is null ";
		}else{
		query = query + " and FLOOR_ID = '"+floorId+"' ";
		}
		return jdbcTemplate.queryForObject(query, new Object[]{BOQSeqNo, workDescriptionId,measurementId, blockId, recordType},String.class);
	}
	@Override
	public String getPresentWorkAreaId(int BOQSeqNo, String workDescriptionId,String measurementId, String blockId, String floorId, String recordType) {
		String query = "select WO_WORK_AREA_ID from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = ? and WO_WORK_DESC_ID = ?  and WO_MEASURMENT_ID = ? and BLOCK_ID = ? and RECORD_TYPE = ? ";
		if(StringUtils.isBlank(floorId)){
			query = query + " and FLOOR_ID is null ";
		}else{
		query = query + " and FLOOR_ID = '"+floorId+"' ";
		}
		return jdbcTemplate.queryForObject(query, new Object[]{BOQSeqNo, workDescriptionId,measurementId, blockId, recordType},String.class);
	}

	@Override
	public int insertReviseBOQChangedDetails(int reviseBOQChangedDetailsId, int tempBOQNo, int BOQSeqNo, String workAreaId,String workDescriptionId,String measurementId, String floorId, String blockId, String boqSiteId,
			double presentArea,	double blockArea, double presentPrice, double laborRatePerUnit, int presentVersionNo, int newVersionNo, String Action, String recordType) {
		String sql = "insert into QS_REVISE_BOQ_CHANGED_DTLS (REV_BOQ_CHANGED_DTLS_ID,TEMP_BOQ_NO,BOQ_SEQ_NO,"
				+ "WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,BLOCK_ID,SITE_ID,OLD_WO_WORK_AREA,"
				+ "NEW_WO_WORK_AREA,OLD_QS_AREA_PRICE,NEW_QS_AREA_PRICE,OLD_VERSION_NO,NEW_VERSION_NO,"
				+ "STATUS,ACTION,RECORD_TYPE)  values (?  ,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'A',?, ?)";
		return jdbcTemplate.update(sql, new Object[]{reviseBOQChangedDetailsId,tempBOQNo, BOQSeqNo,workAreaId, workDescriptionId,
				measurementId, 	floorId, blockId, boqSiteId, 
				new DecimalFormat("##.##").format(presentArea),new DecimalFormat("##.##").format(blockArea),  
				new DecimalFormat("##.##").format(presentPrice), new DecimalFormat("##.##").format(laborRatePerUnit),
				presentVersionNo, newVersionNo, Action, recordType});
	}

	@Override
	public MultiObject getPreviousBOQTotal(String tempBOQNumber) {
		String sql = "select QB.BOQ_TOTAL_AMOUNT,QB.BOQ_MATERIAL_AMOUNT,QB.BOQ_LABOR_AMOUNT from QS_BOQ QB,QS_TEMP_BOQ QTB "
				+ "where QB.SITE_ID=QTB.SITE_ID and QB.TYPE_OF_WORK = QTB.TYPE_OF_WORK "
				+ "and QTB.STATUS='A' and QB.STATUS='A' and QTB.TEMP_BOQ_NO= ? ";
		
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new Object[]{tempBOQNumber});
		Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
		if(list.size()!=1){throw new RuntimeException("Expected:1 Result:"+list.size());}
		String amount = list.get(0).get("BOQ_TOTAL_AMOUNT")==null?"0":list.get(0).get("BOQ_TOTAL_AMOUNT").toString();
		String materialamount = list.get(0).get("BOQ_MATERIAL_AMOUNT")==null?"0":list.get(0).get("BOQ_MATERIAL_AMOUNT").toString();
		String laboramount = list.get(0).get("BOQ_LABOR_AMOUNT")==null?"0":list.get(0).get("BOQ_LABOR_AMOUNT").toString();
		
		MultiObject multiObj = new MultiObject();
		multiObj.setString1(format.format(new BigDecimal(amount)));
		multiObj.setString2(format.format(new BigDecimal(materialamount)));
		multiObj.setString3(format.format(new BigDecimal(laboramount)));
		return multiObj;
	}
	
	@Override
	public int approveReviseTempBOQ(BOQBean bean,BOQBean objForOnlyPermanentBoqNumber) {
		int count=0;

		if (bean.getStrApproverEmpId().equals("END")) {
			int result=0;

			int tempBOQNo = bean.getIntTempBOQNo();
			String boqSiteId = bean.getStrSiteId();
			String typeOfWork = bean.getTypeOfWork();
			int BOQSeqNo = getBOQSeqNoOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork);
			String BOQNumber = getBOQNoOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork);
			MultiObject multiObj = getTotalAmountOfCurrentTempBOQ(tempBOQNo);
			String BOQTotalAmount = multiObj.getString1();
			String BOQMaterialTotalAmount = multiObj.getString2();
			String BOQLaborTotalAmount = multiObj.getString3();
			int presentVersionNo = Integer.parseInt(getVersionNoOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork));
			int newVersionNo=presentVersionNo+1;

			
			//======================================
			Map<String,List<BOQProductDetailsDto>> WorkAreaWiseProdDtls = getQsTempProductDetails(boqSiteId, tempBOQNo);
			List<BOQProductDetailsDto> QsBoqProdDetails_update = new ArrayList<BOQProductDetailsDto>();
			List<BOQProductDetailsDto> QsBoqProdDetails_delete = new ArrayList<BOQProductDetailsDto>();
			List<BOQProductDetailsDto> QsBoqProdDetails_insert = new ArrayList<BOQProductDetailsDto>();
			
			
			//================================================== for EDIT , NEW(only WorkArea is new but WD is existed) , DEL rows=================================================
			Set<String> setOfDeleted_QS_BOQ_DETAILS_ID = new HashSet<String>();
			Set<String> setOfChanged_QS_BOQ_DETAILS_ID = new HashSet<String>();
			List<Map<String, Object>> list =new ArrayList<Map<String, Object>>();
			String sql = "select WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,FLAT_ID,BLOCK_ID,WO_WORK_AREA,STATUS,"
					+ "QS_TEMP_BOQ_DETAILS_ID,QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,ACTION,RECORD_TYPE "
					+ "from QS_TEMP_BOQ_AREA_MAPPING where TEMP_BOQ_NO = ? ";

			list = jdbcTemplate.queryForList(sql,new Object[]{tempBOQNo});
			for(Map<String, Object> map : list){
				String tempWorkAreaId = map.get("WO_WORK_AREA_ID")==null?"":map.get("WO_WORK_AREA_ID").toString();
				String QS_TEMP_BOQ_DETAILS_ID = map.get("QS_TEMP_BOQ_DETAILS_ID")==null?"":map.get("QS_TEMP_BOQ_DETAILS_ID").toString();
				String workDescriptionId = map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString();
				String measurementId = map.get("WO_MEASURMENT_ID")==null?"":map.get("WO_MEASURMENT_ID").toString();
				String floorId = map.get("FLOOR_ID")==null?null:map.get("FLOOR_ID").toString();
				String blockId = map.get("BLOCK_ID")==null?"":map.get("BLOCK_ID").toString();
				String area = map.get("WO_WORK_AREA")==null?"":map.get("WO_WORK_AREA").toString();
				String price = map.get("QS_AREA_PRICE_PER_UNIT")==null?"":map.get("QS_AREA_PRICE_PER_UNIT").toString();
				String areaAmount = map.get("QS_AREA_AMOUNT")==null?"":map.get("QS_AREA_AMOUNT").toString();
				String action = map.get("ACTION")==null?"":map.get("ACTION").toString();
				String recordType = map.get("RECORD_TYPE")==null?"":map.get("RECORD_TYPE").toString();
				if(action.equals("EDIT")){
					boolean wd_EDIT_butArea_NEW = false;
					String PermanentWorkAreaId = null;
					try{
						String queryToGetQsBoqDtlsId = "select QS_BOQ_DETAILS_ID,WO_WORK_AREA_ID from QS_BOQ_AREA_MAPPING "
								+ "where WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID =? and SITE_ID = ? and BOQ_SEQ_NO = ? and RECORD_TYPE = ? "
								+ "and BLOCK_ID = ? ";
						if(StringUtils.isBlank(floorId)){
							queryToGetQsBoqDtlsId = queryToGetQsBoqDtlsId + " and FLOOR_ID is null ";
						}else{
							queryToGetQsBoqDtlsId = queryToGetQsBoqDtlsId + " and FLOOR_ID = '"+floorId+"' ";
						}
						List<Map<String,Object>> WorkArea_list = jdbcTemplate.queryForList(queryToGetQsBoqDtlsId, new Object[]{
								workDescriptionId,measurementId,boqSiteId,BOQSeqNo,recordType,blockId
						});
						
						if(WorkArea_list.size()==1){
							//throw new RuntimeException("Rows updated. Expected:1 Result:"+WorkArea_list.size()); //error
							String QS_BOQ_DETAILS_ID = WorkArea_list.get(0).get("QS_BOQ_DETAILS_ID").toString();
							PermanentWorkAreaId = WorkArea_list.get(0).get("WO_WORK_AREA_ID").toString();
							setOfChanged_QS_BOQ_DETAILS_ID.add(QS_BOQ_DETAILS_ID);
						}else{
							wd_EDIT_butArea_NEW = true;
						}
					}catch(EmptyResultDataAccessException e){
						e.printStackTrace(); //no NEW in EDIT
					}
					//=======
					if(!wd_EDIT_butArea_NEW){
						String sql2 = "update QS_BOQ_AREA_MAPPING set WO_WORK_AREA = ?, QS_AREA_PRICE_PER_UNIT = ?,QS_AREA_AMOUNT = ?,WO_WORK_AVAILABE_AREA=?-WO_WORK_INITIATE_AREA "
								+"where WO_WORK_AREA_ID = ?";
						/*+ "where WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID =? and SITE_ID = ? and BOQ_SEQ_NO = ? "
							+ "and BLOCK_ID = ? ";
					if(StringUtils.isBlank(floorId)){
						sql2 = sql2 + " and FLOOR_ID is null ";
					}else{
						sql2 = sql2 + " and FLOOR_ID = '"+floorId+"' ";
					}*/
						count = jdbcTemplate.update(sql2,new Object[]{
								area,
								price,
								areaAmount,
								area,
								PermanentWorkAreaId/*workDescriptionId,measurementId,boqSiteId,BOQSeqNo,blockId*/
						});
					}
					//wd_EDIT_butArea_NEW = false;
					if(wd_EDIT_butArea_NEW){
						//throw new RuntimeException("No Rows updated.");
						String queryForpermanantBOQDeatilsId = "select QS_BOQ_DETAILS_ID from QS_BOQ_DETAILS where BOQ_SEQ_NO = ? and WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID =? ";
						String permanantBOQDeatilsId =  jdbcTemplate.queryForObject(queryForpermanantBOQDeatilsId, new Object[]{BOQSeqNo,workDescriptionId,measurementId},String.class);
						String queryForTempBOQAreaMapping="INSERT INTO QS_BOQ_AREA_MAPPING(WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,FLAT_ID,BLOCK_ID,SITE_ID,"
								+ "WO_WORK_AREA,WO_WORK_AVAILABE_AREA,WO_PERCENTAGE_AVAIL,STATUS,WO_WORK_INITIATE_AREA,BOQ_SEQ_NO,QS_BOQ_DETAILS_ID, "
								+ "QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,RECORD_TYPE,WO_WORK_AREA_GROUP_ID) "
								+ "SELECT        			WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,FLAT_ID,BLOCK_ID,"+bean.getStrSiteId()+", "
								+ " WO_WORK_AREA,WO_WORK_AREA,100,STATUS,0,?,?, "
								+ "QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,RECORD_TYPE,WO_WORK_AREA_GROUP_ID FROM QS_TEMP_BOQ_AREA_MAPPING WHERE QS_TEMP_BOQ_DETAILS_ID=? and WO_WORK_AREA_ID = ? AND STATUS='A'";
						Object[] params2={BOQSeqNo,permanantBOQDeatilsId,QS_TEMP_BOQ_DETAILS_ID,tempWorkAreaId};
						count=jdbcTemplate.update(queryForTempBOQAreaMapping,params2);
						if(count!=1){ throw new RuntimeException("Rows updated. Expected:1 Result:"+count);}
						setOfChanged_QS_BOQ_DETAILS_ID.add(permanantBOQDeatilsId);
						wd_EDIT_butArea_NEW = true;
					}
					else if(count!=1){ throw new RuntimeException("Rows updated. Expected:1 Result:"+count);}
					System.out.println("tempWorkAreaId: "+tempWorkAreaId);
					if(wd_EDIT_butArea_NEW){
						if(recordType.equals("MATERIAL")){
							updateQsBoqProductDetails(QsBoqProdDetails_update,QsBoqProdDetails_delete,QsBoqProdDetails_insert,WorkAreaWiseProdDtls.get(tempWorkAreaId),tempWorkAreaId);
						}
					}
					else{
						if(recordType.equals("MATERIAL")){
							updateQsBoqProductDetails(QsBoqProdDetails_update,QsBoqProdDetails_delete,QsBoqProdDetails_insert,WorkAreaWiseProdDtls.get(tempWorkAreaId),PermanentWorkAreaId);
						}
					}
				}
				/*if(action.equals("NEW")){
					//no NEW in EDIT - the above commented code copied
					String queryForpermanantBOQDeatilsId = "select QS_BOQ_DETAILS_ID from QS_BOQ_DETAILS where BOQ_SEQ_NO = ? and WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID =? ";
					String permanantBOQDeatilsId = null;
					try {
						permanantBOQDeatilsId = jdbcTemplate.queryForObject(queryForpermanantBOQDeatilsId, new Object[]{BOQSeqNo,workDescriptionId,measurementId},String.class);
					} catch (DataAccessException e) {
						continue;
					}
					String queryForTempBOQAreaMapping="INSERT INTO QS_BOQ_AREA_MAPPING(WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,FLAT_ID,BLOCK_ID,SITE_ID,"
							+ "WO_WORK_AREA,WO_WORK_AVAILABE_AREA,WO_PERCENTAGE_AVAIL,STATUS,WO_WORK_INITIATE_AREA,BOQ_SEQ_NO,QS_BOQ_DETAILS_ID, "
							+ "QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,RECORD_TYPE,WO_WORK_AREA_GROUP_ID) "
							+ "SELECT        			WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,FLAT_ID,BLOCK_ID,"+bean.getStrSiteId()+", "
							+ " WO_WORK_AREA,WO_WORK_AREA,100,STATUS,0,?,?, "
							+ "QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,RECORD_TYPE,WO_WORK_AREA_GROUP_ID FROM QS_TEMP_BOQ_AREA_MAPPING WHERE QS_TEMP_BOQ_DETAILS_ID=? and WO_WORK_AREA_ID = ? AND STATUS='A'";
					Object[] params2={BOQSeqNo,permanantBOQDeatilsId,QS_TEMP_BOQ_DETAILS_ID,tempWorkAreaId};
					count=jdbcTemplate.update(queryForTempBOQAreaMapping,params2);
					if(count!=1){ throw new RuntimeException("Rows updated. Expected:1 Result:"+count);}
					setOfChanged_QS_BOQ_DETAILS_ID.add(permanantBOQDeatilsId);
					System.out.println("tempWorkAreaId: "+tempWorkAreaId);
					if(recordType.equals("MATERIAL")){
						updateQsBoqProductDetails(QsBoqProdDetails_update,QsBoqProdDetails_delete,QsBoqProdDetails_insert,WorkAreaWiseProdDtls.get(tempWorkAreaId),tempWorkAreaId);
					}
				}*/
				if(action.equals("DEL")){
					String PermanentWorkAreaId = null;
					String queryToGetQsBoqDtlsId = "select QS_BOQ_DETAILS_ID,WO_WORK_AREA_ID from QS_BOQ_AREA_MAPPING "
							+ "where WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID =? and SITE_ID = ? and BOQ_SEQ_NO = ? and RECORD_TYPE = ? "
							+ "and BLOCK_ID = ? ";
					if(StringUtils.isBlank(floorId)){
						queryToGetQsBoqDtlsId = queryToGetQsBoqDtlsId + " and FLOOR_ID is null ";
					}else{
						queryToGetQsBoqDtlsId = queryToGetQsBoqDtlsId + " and FLOOR_ID = '"+floorId+"' ";
					}
					/*String QS_BOQ_DETAILS_ID = jdbcTemplate.queryForObject(queryToGetQsBoqDtlsId, new Object[]{
							workDescriptionId,measurementId,boqSiteId,BOQSeqNo,blockId
					},String.class);*/
					List<Map<String,Object>> WorkArea_list = jdbcTemplate.queryForList(queryToGetQsBoqDtlsId, new Object[]{
							workDescriptionId,measurementId,boqSiteId,BOQSeqNo,recordType,blockId
					});
					if(WorkArea_list.size()!=1){throw new RuntimeException("Rows updated. Expected:1 Result:"+WorkArea_list.size());}
					String QS_BOQ_DETAILS_ID = WorkArea_list.get(0).get("QS_BOQ_DETAILS_ID").toString();
					PermanentWorkAreaId = WorkArea_list.get(0).get("WO_WORK_AREA_ID").toString();
					//=======
					String sql3 = "delete from QS_BOQ_AREA_MAPPING "
							+"where WO_WORK_AREA_ID = ?";
							/*+ "where WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID =? and SITE_ID = ? and BOQ_SEQ_NO = ? "
							+ "and BLOCK_ID = ? ";
					if(StringUtils.isBlank(floorId)){
						sql3 = sql3 + " and FLOOR_ID is null ";
					}else{
						sql3 = sql3 + " and FLOOR_ID = '"+floorId+"' ";
					}*/
					count = jdbcTemplate.update(sql3,new Object[]{
							PermanentWorkAreaId //workDescriptionId,measurementId,boqSiteId,BOQSeqNo,blockId
					});
					if(count!=1){ throw new RuntimeException("Rows updated. Expected:1 Result:"+count);}
					setOfDeleted_QS_BOQ_DETAILS_ID.add(QS_BOQ_DETAILS_ID);
					setOfChanged_QS_BOQ_DETAILS_ID.add(QS_BOQ_DETAILS_ID);
					System.out.println("tempWorkAreaId: "+tempWorkAreaId);
					if(recordType.equals("MATERIAL")){
						updateQsBoqProductDetails(QsBoqProdDetails_update,QsBoqProdDetails_delete,QsBoqProdDetails_insert,WorkAreaWiseProdDtls.get(tempWorkAreaId),PermanentWorkAreaId);
					}
				}
			}
			//============================== for NEW rows===========================
			String queryForAllTempBOQDetailsId="select QS_TEMP_BOQ_DETAILS_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,WO_MINORHEAD_ID from QS_TEMP_BOQ_DETAILS where TEMP_BOQ_NO=? and ACTION like '%NEW%' ";
			List<Map<String, Object>> listOfTempBOQDetails=jdbcTemplate.queryForList(queryForAllTempBOQDetailsId,bean.getIntTempBOQNo());
			List<String> listOfTempBOQDetailsId = new ArrayList<String>(); 
			for (Map<String, Object> map : listOfTempBOQDetails) {
				String QS_TEMP_BOQ_DETAILS_ID= map.get("QS_TEMP_BOQ_DETAILS_ID")==null?"":map.get("QS_TEMP_BOQ_DETAILS_ID").toString();
				String workDescriptionId= map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString();
				String measurementId= map.get("WO_MEASURMENT_ID")==null?"":map.get("WO_MEASURMENT_ID").toString();
				String minorHeadId= map.get("WO_MINORHEAD_ID")==null?"":map.get("WO_MINORHEAD_ID").toString();
				String queryToCheckThisWDInPermanentBoqDetails = "select count(*) from QS_BOQ_DETAILS where WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID = ? and WO_MINORHEAD_ID = ? and BOQ_SEQ_NO = ? and status = 'A' ";
				int count4 = jdbcTemplate.queryForInt(queryToCheckThisWDInPermanentBoqDetails,new Object[]{workDescriptionId,measurementId,minorHeadId,BOQSeqNo});
				if(count4==0){listOfTempBOQDetailsId.add(QS_TEMP_BOQ_DETAILS_ID);}
			}
			List<String> listoftempWorkAreaId_WDAlsoNew = new ArrayList<String>();
			for (Map<String, Object> map  : listOfTempBOQDetails) {
				String QS_TEMP_BOQ_DETAILS_ID= map.get("QS_TEMP_BOQ_DETAILS_ID")==null?"":map.get("QS_TEMP_BOQ_DETAILS_ID").toString();
				String workDescriptionId= map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString();
				String measurementId= map.get("WO_MEASURMENT_ID")==null?"":map.get("WO_MEASURMENT_ID").toString();
				String minorHeadId= map.get("WO_MINORHEAD_ID")==null?"":map.get("WO_MINORHEAD_ID").toString();

				String permanantBOQDeatilsSeq=null;
				if(listOfTempBOQDetailsId.contains(QS_TEMP_BOQ_DETAILS_ID)){
					permanantBOQDeatilsSeq=jdbcTemplate.queryForObject("select QS_BOQ_DETAILS_SEQ.NEXTVAL FROM DUAL", String.class);
					String queryForTempBOQDeatilsToPermanant="INSERT INTO QS_BOQ_DETAILS"
							+ "(QS_BOQ_DETAILS_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,WO_WORK_AREA,STATUS,TEMP_BOQ_NO,WO_WORK_AREA_AMOUNT,BOQ_SEQ_NO,REMARKS,SCOPE_OF_WORK,WO_MINORHEAD_ID,RECORD_TYPE) "
							+ "SELECT ?,WO_WORK_DESC_ID,WO_MEASURMENT_ID,WO_WORK_AREA,STATUS,TEMP_BOQ_NO,WO_WORK_AREA_AMOUNT,?,REMARKS ,SCOPE_OF_WORK,WO_MINORHEAD_ID,RECORD_TYPE "
							+ "FROM QS_TEMP_BOQ_DETAILS where QS_TEMP_BOQ_DETAILS_ID=?  AND STATUS='A'";
					Object[] params1={permanantBOQDeatilsSeq,BOQSeqNo,QS_TEMP_BOQ_DETAILS_ID};
					result=jdbcTemplate.update(queryForTempBOQDeatilsToPermanant,params1);
				}
				else{
					String queryToCheckThisWDInPermanentBoqDetails = "select QS_BOQ_DETAILS_ID from QS_BOQ_DETAILS where WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID = ? and WO_MINORHEAD_ID = ? and BOQ_SEQ_NO = ? and status = 'A' ";
					permanantBOQDeatilsSeq=jdbcTemplate.queryForObject(queryToCheckThisWDInPermanentBoqDetails,new Object[]{workDescriptionId,measurementId,minorHeadId,BOQSeqNo},String.class);

				}
				setOfChanged_QS_BOQ_DETAILS_ID.add(permanantBOQDeatilsSeq);
				if(StringUtils.isBlank(permanantBOQDeatilsSeq)){throw new RuntimeException("qsboqdetailsId is blank.");}
				if(permanantBOQDeatilsSeq!=null){
					String queryForTempBOQAreaMapping="INSERT INTO QS_BOQ_AREA_MAPPING(WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,FLAT_ID,BLOCK_ID,SITE_ID,"
							+ "WO_WORK_AREA,WO_WORK_AVAILABE_AREA,WO_PERCENTAGE_AVAIL,STATUS,WO_WORK_INITIATE_AREA,BOQ_SEQ_NO,QS_BOQ_DETAILS_ID, "
							+ "QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,RECORD_TYPE,WO_WORK_AREA_GROUP_ID) "
							+ "SELECT        			WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,FLAT_ID,BLOCK_ID,"+bean.getStrSiteId()+", "
							+ " WO_WORK_AREA,WO_WORK_AREA,100,STATUS,0,?,?, "
							+ "QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,RECORD_TYPE,WO_WORK_AREA_GROUP_ID FROM QS_TEMP_BOQ_AREA_MAPPING WHERE QS_TEMP_BOQ_DETAILS_ID=? AND STATUS='A' and ACTION = 'NEW' ";
					Object[] params2={BOQSeqNo,permanantBOQDeatilsSeq,QS_TEMP_BOQ_DETAILS_ID};
					result=jdbcTemplate.update(queryForTempBOQAreaMapping,params2);
					
					String querytogetcurrentlyInserted = "SELECT WO_WORK_AREA_ID FROM QS_TEMP_BOQ_AREA_MAPPING WHERE QS_TEMP_BOQ_DETAILS_ID=? AND STATUS='A' and ACTION = 'NEW' ";
					List<String> currentlyInserted = jdbcTemplate.queryForList(querytogetcurrentlyInserted,new Object[]{QS_TEMP_BOQ_DETAILS_ID},String.class);
					listoftempWorkAreaId_WDAlsoNew.addAll(currentlyInserted);
				}

			}
			insertQsBoqProductDetails_WDAlsoNew(QsBoqProdDetails_update,QsBoqProdDetails_delete,QsBoqProdDetails_insert,WorkAreaWiseProdDtls,listoftempWorkAreaId_WDAlsoNew);
			
			//==================================== insert , update , delete QsBoqProdDtls =============
			update_QsBoqProdDtls(QsBoqProdDetails_update,boqSiteId,BOQSeqNo);
			delete_QsBoqProdDtls(QsBoqProdDetails_delete,boqSiteId,BOQSeqNo);
			insert_QsBoqProdDtls(QsBoqProdDetails_insert,boqSiteId,BOQSeqNo);
			
			//====================================
			String queryToGetListofWDsUpdatedSOW = "select WO_WORK_DESC_ID,SCOPE_OF_WORK from QS_TEMP_BOQ_DETAILS  "
					+ " where  ACTION like '%SOW%' and TEMP_BOQ_NO = ?";
			List<Map<String,Object>> EditedSOW = jdbcTemplate.queryForList(queryToGetListofWDsUpdatedSOW,new Object[]{tempBOQNo});
			for(Map<String,Object> map : EditedSOW){
				String workDescriptionId= map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString();
				String sow= map.get("SCOPE_OF_WORK")==null?"":map.get("SCOPE_OF_WORK").toString();

				String queryToUpdateSOW = "update QS_BOQ_DETAILS QBD set QBD.SCOPE_OF_WORK = ? "
						+ " where QBD.WO_WORK_DESC_ID = ? and QBD.BOQ_SEQ_NO = ? ";
				int count5 = jdbcTemplate.update(queryToUpdateSOW,new Object[]{sow,workDescriptionId,BOQSeqNo});
			}//====================================
			for(String QS_BOQ_DETAILS_ID : setOfDeleted_QS_BOQ_DETAILS_ID){
				String sql4 = "select count(*) from QS_BOQ_AREA_MAPPING where QS_BOQ_DETAILS_ID = ? and BOQ_SEQ_NO = ? ";
				int count1 = jdbcTemplate.queryForInt(sql4,new Object[]{QS_BOQ_DETAILS_ID,BOQSeqNo});
				if(count1==0){
					String queryToDelQsBoqDtls = "delete from QS_BOQ_DETAILS where QS_BOQ_DETAILS_ID = ? and BOQ_SEQ_NO = ? ";
					int count2 = jdbcTemplate.update(queryToDelQsBoqDtls,new Object[]{QS_BOQ_DETAILS_ID,BOQSeqNo});
					if(count2!=1){
						System.out.println("rows deleted for "+QS_BOQ_DETAILS_ID +" : "+count2);
					}
				}

			}
			//====================================
			for(String QS_BOQ_DETAILS_ID : setOfChanged_QS_BOQ_DETAILS_ID){
				String queryToEditQsBoqDtls = "update QS_BOQ_DETAILS set WO_WORK_AREA = (select sum(WO_WORK_AREA) from QS_BOQ_AREA_MAPPING where QS_BOQ_DETAILS_ID = ? ) "
						+", WO_WORK_AREA_AMOUNT = (select sum(QS_AREA_AMOUNT) from QS_BOQ_AREA_MAPPING where QS_BOQ_DETAILS_ID = ? ) where QS_BOQ_DETAILS_ID = ?  ";
				int count3 = jdbcTemplate.update(queryToEditQsBoqDtls,new Object[]{QS_BOQ_DETAILS_ID,QS_BOQ_DETAILS_ID,QS_BOQ_DETAILS_ID});
				int edited = count3;

			}
			//====================================
			String queryToInactiveReviseBoqChangedDtls = "UPDATE QS_REVISE_BOQ_CHANGED_DTLS SET STATUS='I' WHERE TEMP_BOQ_NO=?";
			int count6 = jdbcTemplate.update(queryToInactiveReviseBoqChangedDtls, new Object[]{bean.getIntTempBOQNo()});
			
			String queryToInactiveReviseBoqQtyChangedDtls = "UPDATE QS_REV_BOQ_QTY_CHG_DTLS SET STATUS='I' WHERE TEMP_BOQ_NO=?";
			int count7 = jdbcTemplate.update(queryToInactiveReviseBoqQtyChangedDtls, new Object[]{bean.getIntTempBOQNo()});
			
			
			//====================================
			String query = "UPDATE QS_TEMP_BOQ SET PENDING_EMP_ID=?,STATUS='I' WHERE TEMP_BOQ_NO=?";
			Object[] params3 = { bean.getStrApproverEmpId(), bean.getIntTempBOQNo() };
			count = jdbcTemplate.update(query, params3);

			String queryToUpdateVerNo="update QS_BOQ  set VERSION_NO = ?,BOQ_TOTAL_AMOUNT = ?,BOQ_LABOR_AMOUNT=?,BOQ_MATERIAL_AMOUNT=?, UPDATED_DATE = sysdate,TEMP_BOQ_NO=?  where SITE_ID = ? and TYPE_OF_WORK = ?  and BOQ_NO = ? and STATUS = 'A'";
			count = jdbcTemplate.update(queryToUpdateVerNo, new Object[]{
					newVersionNo,BOQTotalAmount,BOQLaborTotalAmount,BOQMaterialTotalAmount,bean.getIntTempBOQNo(),boqSiteId,typeOfWork,BOQNumber
			});
			String queryForQsBoqBackup="INSERT INTO QS_BOQ_BACK_UP(BOQ_SEQ_NO,VERSION_NO,STATUS,SITE_ID,CREATED_DATE,APPROVED_BY,BOQ_NO,TEMP_BOQ_NO,TYPE_OF_WORK,BOQ_TOTAL_AMOUNT,UPDATED_DATE,BOQ_LABOR_AMOUNT,BOQ_MATERIAL_AMOUNT) "
					+ "  select BOQ_SEQ_NO,VERSION_NO,STATUS,SITE_ID,CREATED_DATE,APPROVED_BY,BOQ_NO,TEMP_BOQ_NO,TYPE_OF_WORK,BOQ_TOTAL_AMOUNT,UPDATED_DATE,BOQ_LABOR_AMOUNT,BOQ_MATERIAL_AMOUNT"
					+ "  FROM QS_BOQ where SITE_ID = ? and TYPE_OF_WORK = ?  and BOQ_NO = ? and STATUS = 'A'";
			result=jdbcTemplate.update(queryForQsBoqBackup,new Object[]{boqSiteId,typeOfWork,BOQNumber});

			objForOnlyPermanentBoqNumber.setStrBOQNo(BOQNumber);
			copyTempExcelFileIntoThePermanentFolder(bean, BOQNumber,String.valueOf(newVersionNo));

			//====================================




		} else {
			String query = "UPDATE QS_TEMP_BOQ SET PENDING_EMP_ID=? WHERE TEMP_BOQ_NO=?";
			Object[] params = { bean.getStrApproverEmpId(), bean.getIntTempBOQNo() };
			count = jdbcTemplate.update(query, params);
		}
		return count;
	}

	
	@Override
	public boolean isAnyWorkOrderCreatingOrRevisingOnThisBoqNo(String BOQNumber) {
		String sql = "select * from QS_WORKORDER_TEMP_ISSUE where BOQ_NO = ? and STATUS = 'A' " ;
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql, new Object[]{BOQNumber});
		if(list.size()>0){
			return true;
		}
		return false;
	}

	@Override
	public boolean checkIsThisWorkIsDeletingNow(String workDescriptionId, String measurementId, String blockId,
			String floorId, String boqSiteId, int tempBOQNo, String recordType) {
		String sql5 = "select count(*) from QS_TEMP_BOQ_AREA_MAPPING "
				+ "where WO_WORK_DESC_ID = ? and WO_MEASURMENT_ID =?  and TEMP_BOQ_NO = ? and ACTION = 'DEL'  "
				+ "and BLOCK_ID = ? and RECORD_TYPE = ? ";
		if(StringUtils.isBlank(floorId)){
			sql5 = sql5 + " and FLOOR_ID is null ";
		}else{
			sql5 = sql5 + " and FLOOR_ID = '"+floorId+"' ";
		}
		int count = jdbcTemplate.queryForInt(sql5,new Object[]{ 
						workDescriptionId,measurementId,tempBOQNo,blockId,recordType});
		if(count>0){
			return true;
		}
		return false;
	}
	@Override
	public int insertQsTempBOQDetailsForSOW(int tempBOQDetailsId, String workDescriptionId, String measurementId,double totalAreaOfAllBlocks, double totalAmountForAllBlocks, int tempBOQNo,String scopeOfWork,String minorHeadId,String Action) {
		String query = " insert into QS_TEMP_BOQ_DETAILS (QS_TEMP_BOQ_DETAILS_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,WO_WORK_AREA,STATUS,TEMP_BOQ_NO,WO_WORK_AREA_AMOUNT,SCOPE_OF_WORK,WO_MINORHEAD_ID,ACTION) "
					 + " values(?,?,?,?,'S',?,?,?,?,?)";
		int count = jdbcTemplate.update(query,new Object[]{tempBOQDetailsId,workDescriptionId,measurementId,new DecimalFormat("##.##").format(totalAreaOfAllBlocks),tempBOQNo,new DecimalFormat("##.##").format(totalAmountForAllBlocks),scopeOfWork,minorHeadId,Action});
		return count;
	}

	@Override
	public BOQBean getBOQDetailsFromBackup( int intBOQSeqNo, String siteId, String versionNo){

		
		List<Map<String, Object>> listOfBOQ = null;
		BOQBean objBOQBean =  null;
		
		String query = " select BOQ.BOQ_SEQ_NO,BOQ.SITE_ID, S.SITE_NAME ,BOQ.BOQ_TOTAL_AMOUNT,BOQ.VERSION_NO,BOQ.CREATED_DATE,BOQ.UPDATED_DATE,BOQ.BOQ_NO,BOQ.TEMP_BOQ_NO,BOQ.TYPE_OF_WORK "+
                       " from QS_BOQ_BACK_UP  BOQ ,SITE S where BOQ.SITE_ID = S.SITE_ID and BOQ.BOQ_SEQ_NO = ? and BOQ.STATUS = 'A' and VERSION_NO = ? ";
		listOfBOQ = jdbcTemplate.queryForList(query, new Object[] {intBOQSeqNo,versionNo});
		for(Map<String, Object> dbBOQList : listOfBOQ) {

			objBOQBean = new BOQBean();


			objBOQBean.setStrBOQSeqNo(dbBOQList.get("BOQ_SEQ_NO")==null ? "0" :   dbBOQList.get("BOQ_SEQ_NO").toString());
			objBOQBean.setStrVersionNo(dbBOQList.get("VERSION_NO")==null ? "0" :   dbBOQList.get("VERSION_NO").toString());
			objBOQBean.setStrSiteName(dbBOQList.get("SITE_NAME")==null ? "0" :   dbBOQList.get("SITE_NAME").toString());
			objBOQBean.setStrSiteId(dbBOQList.get("SITE_ID")==null ? "0" :   dbBOQList.get("SITE_ID").toString());
			objBOQBean.setStrBOQCreationDate(dbBOQList.get("UPDATED_DATE")==null ? "" : new SimpleDateFormat("dd-MMM-yy").format((Date)dbBOQList.get("UPDATED_DATE")));
			if(dbBOQList.get("UPDATED_DATE")==null){
				objBOQBean.setStrBOQCreationDate(dbBOQList.get("CREATED_DATE")==null? "" : new SimpleDateFormat("dd-MMM-yy").format((Date)dbBOQList.get("CREATED_DATE")));
			}
			objBOQBean.setStrBOQNo(dbBOQList.get("BOQ_NO")==null ? "0" :   dbBOQList.get("BOQ_NO").toString());
			objBOQBean.setIntTempBOQNo(Integer.parseInt(dbBOQList.get("TEMP_BOQ_NO")==null ? "0" :   dbBOQList.get("TEMP_BOQ_NO").toString()));
			objBOQBean.setTypeOfWork(dbBOQList.get("TYPE_OF_WORK")==null ? "" :   dbBOQList.get("TYPE_OF_WORK").toString());
			String amount = dbBOQList.get("BOQ_TOTAL_AMOUNT")==null ? "0" :   dbBOQList.get("BOQ_TOTAL_AMOUNT").toString();

			Format format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
			amount=format.format(new BigDecimal(amount));
			amount = amount.replaceAll("Rs", "");
			amount = amount.trim();
			objBOQBean.setBoqTotalAmount(amount);
			
			
		}
		return objBOQBean;
	}

	@Override
	public List<BOQBean> getBOQAllVersions(String siteId, String BOQSeqNo) {

		
		List<Map<String, Object>> listOfBOQ = null;
		List<BOQBean> list =  new ArrayList<BOQBean>();
		
		String query = "select BOQ.BOQ_SEQ_NO,BOQ.VERSION_NO,BOQ.SITE_ID,S.SITE_NAME,BOQ.CREATED_DATE,BOQ.BOQ_NO,BOQ.TEMP_BOQ_NO,BOQ.TYPE_OF_WORK,BOQ.BOQ_TOTAL_AMOUNT,BOQ.UPDATED_DATE "
				+ " from QS_BOQ_BACK_UP BOQ, SITE S where BOQ.SITE_ID = S.SITE_ID and  BOQ.BOQ_SEQ_NO = ? and BOQ.SITE_ID = ? ";
		listOfBOQ = jdbcTemplate.queryForList(query, new Object[] {BOQSeqNo,siteId});
		for(Map<String, Object> dbBOQList : listOfBOQ) {

			BOQBean objBOQBean = new BOQBean();


			objBOQBean.setStrBOQSeqNo(dbBOQList.get("BOQ_SEQ_NO")==null ? "0" :   dbBOQList.get("BOQ_SEQ_NO").toString());
			objBOQBean.setStrVersionNo(dbBOQList.get("VERSION_NO")==null ? "0" :   dbBOQList.get("VERSION_NO").toString());
			objBOQBean.setStrSiteName(dbBOQList.get("SITE_NAME")==null ? "0" :   dbBOQList.get("SITE_NAME").toString());
			objBOQBean.setStrSiteId(dbBOQList.get("SITE_ID")==null ? "0" :   dbBOQList.get("SITE_ID").toString());
			objBOQBean.setStrBOQCreationDate(dbBOQList.get("UPDATED_DATE")==null ? "" : new SimpleDateFormat("dd-MMM-yy").format((Date)dbBOQList.get("UPDATED_DATE")));
			if(dbBOQList.get("UPDATED_DATE")==null){
				objBOQBean.setStrBOQCreationDate(dbBOQList.get("CREATED_DATE")==null? "" : new SimpleDateFormat("dd-MMM-yy").format((Date)dbBOQList.get("CREATED_DATE")));
			}
			objBOQBean.setStrBOQNo(dbBOQList.get("BOQ_NO")==null ? "0" :   dbBOQList.get("BOQ_NO").toString());
			objBOQBean.setIntTempBOQNo(Integer.parseInt(dbBOQList.get("TEMP_BOQ_NO")==null ? "0" :   dbBOQList.get("TEMP_BOQ_NO").toString()));
			objBOQBean.setTypeOfWork(dbBOQList.get("TYPE_OF_WORK")==null ? "" :   dbBOQList.get("TYPE_OF_WORK").toString());
			String amount = dbBOQList.get("BOQ_TOTAL_AMOUNT")==null ? "0" :   dbBOQList.get("BOQ_TOTAL_AMOUNT").toString();

			Format format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
			amount=format.format(new BigDecimal(amount));
			amount = amount.replaceAll("Rs", "");
			amount = amount.trim();
			objBOQBean.setBoqTotalAmount(amount);
			
			list.add(objBOQBean);
			
		}
		return list;
	}

	@Override
	public String deleteAllTempBoqRecordsInDBForThisSite(String boqSiteId, String typeOfWork) {
		String sql = "select TEMP_BOQ_NO  from QS_TEMP_BOQ where SITE_ID = ? and TYPE_OF_WORK = ? and STATUS='A' ";
		List<String> activeTempBOQNoList = jdbcTemplate.queryForList(sql, new Object[]{boqSiteId,typeOfWork},String.class);
		for(String tempBOQNo : activeTempBOQNoList){
			String sql3 = "delete from QS_REV_BOQ_QTY_CHG_DTLS where TEMP_BOQ_NO = ? and STATUS='A' ";
			jdbcTemplate.update(sql3, new Object[]{tempBOQNo});
			
			String sql1 = "delete from QS_REVISE_BOQ_CHANGED_DTLS where TEMP_BOQ_NO = ? and STATUS='A' ";
			jdbcTemplate.update(sql1, new Object[]{tempBOQNo});
			
			String sql2 = "update QS_TEMP_BOQ set STATUS='D' where TEMP_BOQ_NO = ? and STATUS='A'  ";
			jdbcTemplate.update(sql2, new Object[]{tempBOQNo});
		}
		
		String query = "select TEMP_BOQ_NO  from QS_TEMP_BOQ where SITE_ID = ? and TYPE_OF_WORK = ? ";
		List<String> tempBOQNoList = jdbcTemplate.queryForList(query, new Object[]{boqSiteId,typeOfWork},String.class);
		for(String tempBOQNo : tempBOQNoList){
			String query3 = "delete from QS_TEMP_BOQ_PRODUCT_DTLS where TEMP_BOQ_NO = ? ";
			jdbcTemplate.update(query3, new Object[]{tempBOQNo});
			
			String query1 = "delete from QS_TEMP_BOQ_AREA_MAPPING where TEMP_BOQ_NO = ? ";
			jdbcTemplate.update(query1, new Object[]{tempBOQNo});
			
			String query2 = "delete from QS_TEMP_BOQ_DETAILS where TEMP_BOQ_NO = ? ";
			jdbcTemplate.update(query2, new Object[]{tempBOQNo});
			
		}
		
		return null;
	}
	@Override
	public String deleteAllRejectedTempBoqRecordsInDB(int tempBOQNo) {

		String sql3 = "delete from QS_REV_BOQ_QTY_CHG_DTLS where TEMP_BOQ_NO = ? and STATUS='A' ";
		jdbcTemplate.update(sql3, new Object[]{tempBOQNo});

		String sql1 = "delete from QS_REVISE_BOQ_CHANGED_DTLS where TEMP_BOQ_NO = ? and STATUS='A' ";
		jdbcTemplate.update(sql1, new Object[]{tempBOQNo});

		

		String query3 = "delete from QS_TEMP_BOQ_PRODUCT_DTLS where TEMP_BOQ_NO = ? ";
		jdbcTemplate.update(query3, new Object[]{tempBOQNo});

		String query1 = "delete from QS_TEMP_BOQ_AREA_MAPPING where TEMP_BOQ_NO = ? ";
		jdbcTemplate.update(query1, new Object[]{tempBOQNo});

		String query2 = "delete from QS_TEMP_BOQ_DETAILS where TEMP_BOQ_NO = ? ";
		jdbcTemplate.update(query2, new Object[]{tempBOQNo});
		
		


		return null;
	}

	@Override
	public String getDBActionOfTempBoqDetailsId(int tempBOQDetailsId) {
		String query = "select ACTION from QS_TEMP_BOQ_DETAILS where QS_TEMP_BOQ_DETAILS_ID = ? ";
		return jdbcTemplate.queryForObject(query, new Object[]{tempBOQDetailsId},String.class);
	}
	
	@Override
	public int updateQsTempBOQDetailsWithSOWandAction(int tempBOQDetailsId, String scopeOfWork, String action1) {
		String query = " update QS_TEMP_BOQ_DETAILS set SCOPE_OF_WORK = ?, ACTION = ? "
					 + " where QS_TEMP_BOQ_DETAILS_ID = ? ";
		int count = jdbcTemplate.update(query,new Object[]{scopeOfWork,action1,tempBOQDetailsId});
		return count;
	}

	@Override
	public List<BOQBean> viewAllBlocksFloorsFlats(String siteId) {
		List<Map<String, Object>> listOfBOQ = null;
		List<BOQBean> list =  new ArrayList<BOQBean>();
		
		String query = "select B.BLOCK_ID,B.NAME as BLOCK_NAME,B.SITE_ID,F.FLOOR_ID,F.NAME as FLOOR_NAME,FT.FLAT_ID,FT.NAME as FLAT_NAME "
				+ " from BLOCK B "
				+ " LEFT OUTER JOIN FLOOR F LEFT OUTER JOIN FLAT FT on F.FLOOR_ID=FT.FLOOR_ID on B.BLOCK_ID=F.BLOCK_ID " 
				+ " where B.SITE_ID = ? ";
		listOfBOQ = jdbcTemplate.queryForList(query, new Object[] {siteId});
		for(Map<String, Object> dbBOQList : listOfBOQ) {

			BOQBean objBOQBean = new BOQBean();


			objBOQBean.setStrBlock(dbBOQList.get("BLOCK_NAME")==null ? "-" :   dbBOQList.get("BLOCK_NAME").toString());
			objBOQBean.setStrFloor(dbBOQList.get("FLOOR_NAME")==null ? "-" :   dbBOQList.get("FLOOR_NAME").toString());
			objBOQBean.setStrFlat(dbBOQList.get("FLAT_NAME")==null ? "-" :   dbBOQList.get("FLAT_NAME").toString());
			list.add(objBOQBean);
		}
		return list;
	}

	@Override
	public boolean isBoqAlreadyPresent(String boqSiteId, String typeOfWork) {
		List<Map<String, Object>> listOfBOQ = null;
		
		String query = " select * from QS_BOQ where SITE_ID = ? and TYPE_OF_WORK = ? and STATUS = 'A'";
		listOfBOQ = jdbcTemplate.queryForList(query, new Object[] {boqSiteId, typeOfWork});
		if(listOfBOQ.isEmpty()){
			return false;
		}
		else{
			return true;
		}
	}

	@Override
	public HashMap<String,String> getMajorHeadIdsMap(String typeOfWork) {
		String query = "select WO_MAJORHEAD_ID,WO_MAJORHEAD_DESC from QS_WO_MAJORHEAD where TYPE_OF_WORK = '"+typeOfWork+"' and STATUS='A'";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(query, new Object[] {});
		HashMap<String,String> dataMap = new HashMap<String,String>();
		HashSet<String> duplicateCheck = new HashSet<String>();
		for(Map<String, Object> map : list) {
			String majDesc = map.get("WO_MAJORHEAD_DESC").toString().trim().toLowerCase();
			String majId = map.get("WO_MAJORHEAD_ID").toString();
			dataMap.put(majDesc,majId);
			if(duplicateCheck.add(majDesc)==false){
				throw new RuntimeException(majDesc+" duplicate");
			}
			
		}
		return dataMap;
	}
	@Override
	public HashMap<String,HashMap<String,String>> getMinorHeadIdsMap(String typeOfWork) {

		String query = "select WO_MINORHEAD_ID,WO_MINORHEAD_DESC,WO_MAJORHEAD_ID from QS_WO_MINORHEAD where TYPE_OF_WORK = '"+typeOfWork+"' and STATUS='A'";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(query, new Object[] {});
		HashMap<String,HashMap<String,String>> dataMap = new HashMap<String,HashMap<String,String>>();
		HashMap<String,HashSet<String>> duplicateCheck = new HashMap<String,HashSet<String>>();
		for(Map<String, Object> map : list) {
			String majId = map.get("WO_MAJORHEAD_ID").toString();
			if(!dataMap.containsKey(majId)){
				dataMap.put(majId, new HashMap<String,String>());
			}
			String minDesc = map.get("WO_MINORHEAD_DESC").toString().trim().toLowerCase();
			String minId = map.get("WO_MINORHEAD_ID").toString();
			dataMap.get(majId ).put( minDesc,minId);
			//--
			if(!duplicateCheck.containsKey(majId)){
				duplicateCheck.put(majId, new HashSet<String>());
			}
			if(duplicateCheck.get(majId ).add( minDesc)==false){
				throw new RuntimeException(minDesc+" duplicate");
			}

		}
		return dataMap;
	}
	@Override
	public HashMap<String,HashMap<String,String>> getWorkDescriptionIdsMap(String typeOfWork) {
		String query = "select WO_WORK_DESCRIPTION,WO_WORK_DESC_ID,WO_MINORHEAD_ID from QS_WO_WORKDESC where TYPE_OF_WORK = '"+typeOfWork+"' and STATUS='A'";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(query, new Object[] {});
		long startTime1 = System.currentTimeMillis();
		HashMap<String,HashMap<String,String>> dataMap = new HashMap<String,HashMap<String,String>>();
		HashMap<String,HashSet<String>> duplicateCheck = new HashMap<String,HashSet<String>>();
		for(Map<String, Object> map : list) {
			String minId = map.get("WO_MINORHEAD_ID").toString();
			if(!dataMap.containsKey(minId)){
				dataMap.put(minId, new HashMap<String,String>());
			}
			String wdDesc = map.get("WO_WORK_DESCRIPTION").toString().trim().toLowerCase();
			String wdId = map.get("WO_WORK_DESC_ID").toString();
			dataMap.get(minId ).put( wdDesc,wdId);
			//--checking is there duplicates already in database 
			if(!duplicateCheck.containsKey(minId)){
				duplicateCheck.put(minId, new HashSet<String>());
			}
			if(duplicateCheck.get(minId ).add( wdDesc)==false){
				throw new RuntimeException(wdDesc+" duplicate");
			}
		}
		long stopTime1 = System.currentTimeMillis();
	    long elapsedTime1 = stopTime1 - startTime1;
	    System.out.println("WD map elapsed time: "+elapsedTime1);
	    
		return dataMap;
	}
	@Override
	public HashMap<String,HashMap<String,String>> getMeasurementIdsMap(String typeOfWork) {
		String query = "select WO_MEASURMENT_ID,WO_MEASURMEN_NAME,WO_WORK_DESC_ID from QS_WO_MEASURMENT where TYPE_OF_WORK = '"+typeOfWork+"' and STATUS='A'";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(query, new Object[] {});
		HashMap<String,HashMap<String,String>> dataMap = new HashMap<String,HashMap<String,String>>();
		HashMap<String,HashSet<String>> duplicateCheck = new HashMap<String,HashSet<String>>();
		for(Map<String, Object> map : list) {
			String wdId = map.get("WO_WORK_DESC_ID").toString();
			if(!dataMap.containsKey(wdId)){
				dataMap.put(wdId, new HashMap<String,String>());
			}
			String uomDesc = map.get("WO_MEASURMEN_NAME").toString().trim().toLowerCase();
			String uomId = map.get("WO_MEASURMENT_ID").toString();
			dataMap.get(wdId ).put( uomDesc,uomId);
			//--checking is there duplicates already in database 
			if(!duplicateCheck.containsKey(wdId )){
				duplicateCheck.put(wdId , new HashSet<String>());
			}
			if(duplicateCheck.get(wdId  ).add( uomDesc)==false){
				throw new RuntimeException(uomDesc+" duplicate");
			}
		}
		return dataMap;
	}
	

	@Override
	public HashMap<String,String> getBlockIdsMap(String boqSiteId) {	    
		String query = "select BLOCK_ID,NAME from BLOCK where SITE_ID = '"+boqSiteId+"'";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(query, new Object[] {});
		HashMap<String,String> dataMap = new HashMap<String,String>();
		HashSet<String> duplicateCheck = new HashSet<String>();
		for(Map<String, Object> map : list) {
			String blockName = map.get("NAME").toString().trim().toLowerCase();
			String blockId = map.get("BLOCK_ID").toString();
			dataMap.put(blockName,blockId);
			if(duplicateCheck.add(blockName)==false){
				throw new RuntimeException(blockName+" duplicate");
			}
		}
		return dataMap;
	}
	@Override
	public HashMap<String,HashMap<String,String>> getFloorIdsMap(String boqSiteId) {
		String query = "select NAME,BLOCK_ID,FLOOR_ID from FLOOR where BLOCK_ID in ("
		+"select BLOCK_ID from BLOCK where SITE_ID = '"+boqSiteId+"'"
		+")";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(query, new Object[] {});
		HashMap<String,HashMap<String,String>> dataMap = new HashMap<String,HashMap<String,String>>();
		HashMap<String,HashSet<String>> duplicateCheck = new HashMap<String,HashSet<String>>();
		for(Map<String, Object> map : list) {
			String floorName = map.get("NAME").toString().trim().toLowerCase();
			if(!dataMap.containsKey(floorName)){
				dataMap.put(floorName, new HashMap<String,String>());
			}
			String blockId = map.get("BLOCK_ID").toString();
			String floorId = map.get("FLOOR_ID").toString();
			dataMap.get(floorName ).put( blockId,floorId);
			//--checking is there duplicates already in database 
			if(!duplicateCheck.containsKey(floorName )){
				duplicateCheck.put(floorName , new HashSet<String>());
			}
			if(duplicateCheck.get(floorName  ).add( blockId)==false){
				throw new RuntimeException(floorName+" duplicate");
			}
		}
		return dataMap;
	}
	@Override
	public HashMap<String,String> getMaterialGroupIdsMap() {
		String query = "select MATERIAL_GROUP_ID,MATERIAL_GROUP_NAME from PRODUCT_GROUP_MASTER";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(query, new Object[] {});
		HashMap<String,String> dataMap = new HashMap<String,String>();
		for(Map<String, Object> map : list) {
			String matGroupName = map.get("MATERIAL_GROUP_NAME").toString().trim().toLowerCase();
			String matGroupId = map.get("MATERIAL_GROUP_ID").toString();
			if(!dataMap.containsKey(matGroupName)){
				dataMap.put(matGroupName, matGroupId);
			}
			
		}
		return dataMap;
	}
	@Override
	public HashMap<String,HashMap<String,String>> getMaterialGroupMeasurementIdsMap() {
		String query = "select MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,MATERIAL_GROUP_MST_NAME from PRODUCT_GROUP_MASTER";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(query, new Object[] {});
		HashMap<String,HashMap<String,String>> dataMap = new HashMap<String,HashMap<String,String>>();
		for(Map<String, Object> map : list) {
			String matGroupId = map.get("MATERIAL_GROUP_ID").toString();
			if(!dataMap.containsKey(matGroupId)){
				dataMap.put(matGroupId, new HashMap<String,String>());
			}
			
			String matGroupMstName = map.get("MATERIAL_GROUP_MST_NAME").toString().trim().toLowerCase();
			String matGroupMstId = map.get("MATERIAL_GROUP_MEASUREMENT_ID").toString();
			if(!dataMap.get(matGroupId).containsKey(matGroupMstName)){
				dataMap.get(matGroupId).put(matGroupMstName, matGroupMstId);
			}



		}
		return dataMap;
	}

	@Override
	public int[] insertQsTempBOQProductDtls(final String workAreaId, final double blockArea, WDRateAnalysis objWDRA, final String boqSiteId, final int tempBOQNo, final String action) {
		final List<MaterialDetails> materialsList= objWDRA.getMaterialsList();
		String sql = "INSERT INTO QS_TEMP_BOQ_PRODUCT_DTLS (QS_TEMP_BOQ_PRODUCT_DTLS_ID,WO_WORK_AREA_ID,WO_WORK_AREA,MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS,SITE_ID,TEMP_BOQ_NO,ACTION) " +
				" VALUES (QS_TEMP_BOQ_PRODUCT_DTLS_SEQ.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
						
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					MaterialDetails objMD = materialsList.get(i);
					ps.setString(1, workAreaId);
					ps.setDouble(2, blockArea);
					ps.setString(3, objMD.getMaterialGroupId() );
					ps.setString(4, objMD.getMaterialGroupMeasurementId() );
					ps.setDouble(5, objMD.getPerUnitQuantity() );
					ps.setDouble(6, objMD.getPerUnitAmount());
					ps.setDouble(7, blockArea*objMD.getPerUnitQuantity());
					ps.setDouble(8, blockArea*objMD.getPerUnitAmount());
					ps.setString(9, "A");
					ps.setString(10, objMD.getRemarks());
					ps.setString(11, boqSiteId);
					ps.setInt(12, tempBOQNo);
					ps.setString(13, action);
					
				}
						
				@Override
				public int getBatchSize() {
					return materialsList.size();
				}
			  });
	}

	@Override
	public int[] insertQsTempBOQProductDetails(final List<BOQProductDetailsDto> QsTempBOQProductDtls_Insert) {
		//final List<MaterialDetails> materialsList= objWDRA.getMaterialsList();
		String sql = "INSERT INTO QS_TEMP_BOQ_PRODUCT_DTLS (QS_TEMP_BOQ_PRODUCT_DTLS_ID,WO_WORK_AREA_ID,WO_WORK_AREA,MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS,SITE_ID,TEMP_BOQ_NO,ACTION) " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
						
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					BOQProductDetailsDto objQsTempBOQProductDtlsDto = QsTempBOQProductDtls_Insert.get(i);
					ps.setInt(1, objQsTempBOQProductDtlsDto.getQsTempBOQProductDetailsId());
					ps.setString(2, objQsTempBOQProductDtlsDto.getWorkAreaId());
					ps.setDouble(3, objQsTempBOQProductDtlsDto.getWorkArea());
					ps.setString(4, objQsTempBOQProductDtlsDto.getMaterialGroupId());
					ps.setString(5, objQsTempBOQProductDtlsDto.getMaterialGroupMeasurementId());
					ps.setDouble(6, objQsTempBOQProductDtlsDto.getPerUnitQuantity());
					ps.setDouble(7, objQsTempBOQProductDtlsDto.getPerUnitAmount());
					ps.setDouble(8, objQsTempBOQProductDtlsDto.getTotalQuantity());
					ps.setDouble(9, objQsTempBOQProductDtlsDto.getTotalAmount());
					ps.setString(10, objQsTempBOQProductDtlsDto.getStatus());
					ps.setString(11, objQsTempBOQProductDtlsDto.getRemarks());
					ps.setString(12, objQsTempBOQProductDtlsDto.getSiteId());
					ps.setInt(13, objQsTempBOQProductDtlsDto.getTempBOQNo());
					ps.setString(14, objQsTempBOQProductDtlsDto.getAction());
					
					
				}
						
				@Override
				public int getBatchSize() {
					return QsTempBOQProductDtls_Insert.size();
				}
			  });
	}
	@Override
	public void dummy(int QS_TEMP_BOQ_PRODUCT_DTLS_ID, String WO_WORK_AREA_ID, String WO_WORK_AREA,
			String MATERIAL_GROUP_ID, String MATERIAL_GROUP_MEASUREMENT_ID, double PER_UNIT_QUANTITY,
			double PER_UNIT_AMOUNT, double TOTAL_QUANTITY, double TOTAL_AMOUNT, String STATUS, String REMARKS,
			String SITE_ID) {
		
		String sql = "INSERT INTO QS_TEMP_BOQ_PRODUCT_DTLS (QS_TEMP_BOQ_PRODUCT_DTLS_ID,WO_WORK_AREA_ID,WO_WORK_AREA,MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS,SITE_ID) " +
				" VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(sql,new Object[]{QS_TEMP_BOQ_PRODUCT_DTLS_ID,WO_WORK_AREA_ID,WO_WORK_AREA,MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS,SITE_ID});
		
		
	}
	@Override
	public int getReviseBOQChangedDetailsId() {
		String query = "select REV_BOQ_CHANGED_DTLS_SEQ.nextval from dual";
		int seqId = jdbcTemplate.queryForInt(query);
		return seqId;
	}
	@Override
	public int getReviseBOQQuantityChangedDetailsId() {
		String query = "select QS_REV_BOQ_QTY_CHG_DTLS_SEQ.nextval from dual";
		int seqId = jdbcTemplate.queryForInt(query);
		return seqId;
	}
	@Override
	public int getQsTempBOQProductDetailsId() {
		String query = "select QS_TEMP_BOQ_PRODUCT_DTLS_SEQ.nextval from dual";
		int seqId = jdbcTemplate.queryForInt(query);
		return seqId;
	}

	@Override
	public WDRateAnalysis getPresentMaterialDetails(String presentWorkAreaId) {
		WDRateAnalysis objWDRA = new WDRateAnalysis();
		objWDRA.setMaterialsList(new ArrayList<MaterialDetails>());
		String sql = "select MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,TOTAL_QUANTITY,TOTAL_AMOUNT,REMARKS from QS_BOQ_PRODUCT_DTLS where WO_WORK_AREA_ID = ? ";
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(sql, new Object[] {presentWorkAreaId});
		
		
		for(Map<String, Object> map : list) {
			MaterialDetails objMD = new MaterialDetails();
			objMD.setMaterialGroupId(map.get("MATERIAL_GROUP_ID").toString() );
			objMD.setMaterialGroupMeasurementId(map.get("MATERIAL_GROUP_MEASUREMENT_ID").toString());
			objMD.setPerUnitQuantity(Double.valueOf(map.get("PER_UNIT_QUANTITY").toString() ));
			objMD.setPerUnitAmount(Double.valueOf(map.get("PER_UNIT_AMOUNT").toString()));
			objMD.setTotalQuantity(Double.valueOf(map.get("TOTAL_QUANTITY").toString()));
			objMD.setTotalAmount(Double.valueOf(map.get("TOTAL_AMOUNT").toString()));
			objMD.setRemarks(map.get("REMARKS")==null?"":map.get("REMARKS").toString());
			
			objWDRA.getMaterialsList().add(objMD);
		}
		return objWDRA;
		
	}
	
	@Override
	public WDRateAnalysis getWOMaterialDetails(String presentWorkAreaId) {
		WDRateAnalysis objWDRA = new WDRateAnalysis();
		objWDRA.setMaterialsList(new ArrayList<MaterialDetails>());
		String sql = "select QWPD.MATERIAL_GROUP_ID,QWPD.UOM,QWPD.PER_UNIT_QUANTITY from QS_WORKORDER_PRODUCT_DTLS QWPD,QS_WORKORDER_ISSUE_AREA_DETAILS QWIAD "
				+ "where QWIAD.WO_WORK_AREA_ID = ? and QWIAD.WO_WORK_ISSUE_AREA_DTLS_ID = QWPD.WO_WORK_ISSUE_AREA_DTLS_ID "
		
			+" union "
			+" select QWTPD.MATERIAL_GROUP_ID,QWTPD.UOM,QWTPD.PER_UNIT_QUANTITY from QS_WORKORDER_TEMP_PRODUCT_DTLS QWTPD,QS_WORKORDER_TEMP_ISSUE_AREA_DETAILS QWTIAD,QS_WORKORDER_TEMP_ISSUE_DTLS QWTID,QS_WORKORDER_TEMP_ISSUE QWTI "
			+" where QWTIAD.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID = QWTPD.WO_WORK_TEMP_ISSUE_AREA_DTLS_ID and QWTIAD.WO_WORK_AREA_ID = ? "
			+" and QWTIAD.WO_WORK_TEMP_ISSUE_DTLS_ID = QWTID.WO_WORK_TEMP_ISSUE_DTLS_ID and QWTID.QS_WO_TEMP_ISSUE_ID = QWTI.QS_WO_TEMP_ISSUE_ID and (QWTI.STATUS = 'A' or QWTI.STATUS = 'DF')";
	
		
		List<Map<String, Object>> list = null;
		list = jdbcTemplate.queryForList(sql, new Object[] {presentWorkAreaId,presentWorkAreaId});
		
		
		for(Map<String, Object> map : list) {
			MaterialDetails objMD = new MaterialDetails();
			objMD.setMaterialGroupId(map.get("MATERIAL_GROUP_ID").toString() );
			objMD.setMaterialGroupMeasurementId(map.get("UOM").toString());
			objMD.setPerUnitQuantity(Double.valueOf(map.get("PER_UNIT_QUANTITY").toString() ));
			
			objWDRA.getMaterialsList().add(objMD);
		}
		return objWDRA;
		
	}

	@Override
	public int[] insertReviseBOQQuantityChangedDetails(final List<ReviseBOQQtyChangedDtlsDto> ReviseBOQQtyChangedDtls_Insert) {
		String sql = "INSERT INTO QS_REV_BOQ_QTY_CHG_DTLS (QS_REV_BOQ_QTY_CHG_DTLS_ID,REV_BOQ_CHANGED_DTLS_ID,MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,OLD_PER_UNIT_QUANTITY,NEW_PER_UNIT_QUANTITY,OLD_PER_UNIT_AMOUNT,NEW_PER_UNIT_AMOUNT,OLD_TOTAL_QUANTITY,NEW_TOTAL_QUANTITY,OLD_TOTAL_AMOUNT,NEW_TOTAL_AMOUNT,REMARKS,STATUS,ACTION,TEMP_BOQ_NO) " +
				" VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
						
		return jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
						
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					ReviseBOQQtyChangedDtlsDto objReviseBOQQtyChangedDtlsDto = ReviseBOQQtyChangedDtls_Insert.get(i);
					ps.setInt(1, objReviseBOQQtyChangedDtlsDto.getReviseBOQQuantityChangedDetailsId());
					ps.setInt(2, objReviseBOQQtyChangedDtlsDto.getReviseBOQChangedDetailsId());
					ps.setString(3, objReviseBOQQtyChangedDtlsDto.getMaterialGroupId());
					ps.setString(4, objReviseBOQQtyChangedDtlsDto.getMaterialGroupMeasurementId());
					ps.setDouble(5, objReviseBOQQtyChangedDtlsDto.getOldPerUnitQuantity());
					ps.setDouble(6, objReviseBOQQtyChangedDtlsDto.getNewPerUnitQuantity());
					ps.setDouble(7, objReviseBOQQtyChangedDtlsDto.getOldPerUnitAmount());
					ps.setDouble(8, objReviseBOQQtyChangedDtlsDto.getNewPerUnitAmount());
					ps.setDouble(9, objReviseBOQQtyChangedDtlsDto.getOldTotalQuantity());
					ps.setDouble(10, objReviseBOQQtyChangedDtlsDto.getNewTotalQuantity());
					ps.setDouble(11, objReviseBOQQtyChangedDtlsDto.getOldTotalAmount());
					ps.setDouble(12, objReviseBOQQtyChangedDtlsDto.getNewTotalAmount());
					ps.setString(13, objReviseBOQQtyChangedDtlsDto.getRemarks());
					ps.setString(14, objReviseBOQQtyChangedDtlsDto.getStatus());
					ps.setString(15, objReviseBOQQtyChangedDtlsDto.getAction());
					ps.setInt(16, objReviseBOQQtyChangedDtlsDto.getTempBOQNo());
					
				}
						
				@Override
				public int getBatchSize() {
					return ReviseBOQQtyChangedDtls_Insert.size();
				}
			  });
	}

	@Override
	public Map<String,Double> getWDtotals_inDB(int BOQSeqNo) {
		Map<String,Double> WDtotals = new HashMap<String,Double>();
		String sql = "select WO_WORK_DESC_ID,sum(QS_AREA_AMOUNT) as WD_TOTAL from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = ? group by WO_WORK_DESC_ID";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new Object[]{BOQSeqNo});
		for(Map<String,Object> map : list){
			WDtotals.put(map.get("WO_WORK_DESC_ID").toString(), Double.valueOf(map.get("WD_TOTAL").toString()));
		}
		return WDtotals;
	}

	@Override
	public Map<String, Double> getMAJtotals_inDB(int BOQSeqNo) {
		Map<String,Double> MAJtotals = new HashMap<String,Double>();
		String sql = "select QWMIN.WO_MAJORHEAD_ID,sum(QBAM.QS_AREA_AMOUNT) as MAJ_TOTAL "
				+ "from QS_BOQ_AREA_MAPPING QBAM,QS_WO_WORKDESC QWWD,QS_WO_MINORHEAD QWMIN "
				+ "where QBAM.BOQ_SEQ_NO = ? and QBAM.WO_WORK_DESC_ID = QWWD.WO_WORK_DESC_ID and QWWD.WO_MINORHEAD_ID = QWMIN.WO_MINORHEAD_ID "
				+ "and QWWD.TYPE_OF_WORK = 'PIECEWORK' and QWMIN.TYPE_OF_WORK = 'PIECEWORK' "
				+ "group by QWMIN.WO_MAJORHEAD_ID";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new Object[]{BOQSeqNo});
		for(Map<String,Object> map : list){
			MAJtotals.put(map.get("WO_MAJORHEAD_ID").toString(), Double.valueOf(map.get("MAJ_TOTAL").toString()));
		}
		return MAJtotals;
	}
	
	public void updateQsBoqProductDetails(List<BOQProductDetailsDto> QsBoqProdDetails_update, List<BOQProductDetailsDto> QsBoqProdDetails_delete, List<BOQProductDetailsDto> QsBoqProdDetails_insert, List<BOQProductDetailsDto> listOfProductDtls, String permanentWorkAreaId){
		
		for(BOQProductDetailsDto objPD : listOfProductDtls){
			String action = objPD.getAction();
			if(StringUtils.isBlank(action)){throw new RuntimeException("Action is blank while getting records from qs_temp_prod_dtls");}
			if(action.equals("EDIT")){
				objPD.setWorkAreaId(permanentWorkAreaId);
				QsBoqProdDetails_update.add(objPD);
			}
			if(action.equals("DEL")){
				objPD.setWorkAreaId(permanentWorkAreaId);
				QsBoqProdDetails_delete.add(objPD);
			}
			if(action.equals("NEW")){
				objPD.setWorkAreaId(permanentWorkAreaId);
				QsBoqProdDetails_insert.add(objPD);
			}
		}
		
	}
	public void insertQsBoqProductDetails_WDAlsoNew(List<BOQProductDetailsDto> QsBoqProdDetails_update, List<BOQProductDetailsDto> QsBoqProdDetails_delete, List<BOQProductDetailsDto> QsBoqProdDetails_insert, Map<String, List<BOQProductDetailsDto>> workAreaWiseProdDtls, List<String> listoftempWorkAreaId_WDAlsoNew){
		for(String tempWorkAreaId : listoftempWorkAreaId_WDAlsoNew){
			List<BOQProductDetailsDto> listOfProductDtls = workAreaWiseProdDtls.get(tempWorkAreaId);
			if(listOfProductDtls!=null){
			QsBoqProdDetails_insert.addAll(listOfProductDtls);
			}
		}
	}
	public int[] update_QsBoqProdDtls(final List<BOQProductDetailsDto> QsBoqProdDetails_update, final String boqSiteId, final int BOQSeqNo) {
		String updateQuery = "update QS_BOQ_PRODUCT_DTLS set WO_WORK_AREA=?, PER_UNIT_QUANTITY=?, PER_UNIT_AMOUNT=?, TOTAL_QUANTITY=?, TOTAL_AMOUNT=?, REMARKS=?      "
				+ "where WO_WORK_AREA_ID = ? and MATERIAL_GROUP_ID = ? and MATERIAL_GROUP_MEASUREMENT_ID = ? and SITE_ID = ? and BOQ_SEQ_NO = ? ";
						
		return jdbcTemplate.batchUpdate(updateQuery, new BatchPreparedStatementSetter() {
						
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					BOQProductDetailsDto objPD = QsBoqProdDetails_update.get(i);
					ps.setDouble(1, objPD.getWorkArea());
					ps.setDouble(2, objPD.getPerUnitQuantity() );
					ps.setDouble(3, objPD.getPerUnitAmount());
					ps.setDouble(4, objPD.getTotalQuantity());
					ps.setDouble(5, objPD.getTotalAmount());
					ps.setString(6, objPD.getRemarks());
					
					ps.setString(7, objPD.getWorkAreaId());
					ps.setString(8, objPD.getMaterialGroupId() );
					ps.setString(9, objPD.getMaterialGroupMeasurementId() );
					ps.setString(10, boqSiteId);
					ps.setInt(11, BOQSeqNo);
					
				}
						
				@Override
				public int getBatchSize() {
					return QsBoqProdDetails_update.size();
				}
			  });
		
	}
	public int[] delete_QsBoqProdDtls(final List<BOQProductDetailsDto> QsBoqProdDetails_delete, final String boqSiteId, final int BOQSeqNo) {
		String deleteQuery = "delete from QS_BOQ_PRODUCT_DTLS "
				+ "where WO_WORK_AREA_ID = ? and MATERIAL_GROUP_ID = ? and MATERIAL_GROUP_MEASUREMENT_ID = ? and SITE_ID = ? and BOQ_SEQ_NO = ? ";
					
		return jdbcTemplate.batchUpdate(deleteQuery, new BatchPreparedStatementSetter() {
						
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					BOQProductDetailsDto objPD = QsBoqProdDetails_delete.get(i);
					ps.setString(1, objPD.getWorkAreaId());
					ps.setString(2, objPD.getMaterialGroupId() );
					ps.setString(3, objPD.getMaterialGroupMeasurementId() );
					ps.setString(4, boqSiteId);
					ps.setInt(5, BOQSeqNo);
					
				}
						
				@Override
				public int getBatchSize() {
					return QsBoqProdDetails_delete.size();
				}
			  });
		
	}
	public int[] insert_QsBoqProdDtls(final List<BOQProductDetailsDto> QsBoqProdDetails_insert, final String boqSiteId, final int BOQSeqNo) {
		String insertQuery = "INSERT INTO QS_BOQ_PRODUCT_DTLS (QS_BOQ_PRODUCT_DTLS_ID,WO_WORK_AREA_ID,WO_WORK_AREA,MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS,SITE_ID,BOQ_SEQ_NO) " +
				" VALUES (QS_BOQ_PRODUCT_DTLS_SEQ.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
						
		return jdbcTemplate.batchUpdate(insertQuery, new BatchPreparedStatementSetter() {
						
				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					BOQProductDetailsDto objPD = QsBoqProdDetails_insert.get(i);
					ps.setString(1, objPD.getWorkAreaId());
					ps.setDouble(2, objPD.getWorkArea());
					ps.setString(3, objPD.getMaterialGroupId() );
					ps.setString(4, objPD.getMaterialGroupMeasurementId() );
					ps.setDouble(5, objPD.getPerUnitQuantity() );
					ps.setDouble(6, objPD.getPerUnitAmount());
					ps.setDouble(7, objPD.getTotalQuantity());
					ps.setDouble(8, objPD.getTotalAmount());
					ps.setString(9, "A");
					ps.setString(10, objPD.getRemarks());
					ps.setString(11, boqSiteId);
					ps.setInt(12, BOQSeqNo);
					
				}
						
				@Override
				public int getBatchSize() {
					return QsBoqProdDetails_insert.size();
				}
			  });
		
	}

	
	public Map<String,List<BOQProductDetailsDto>> getQsTempProductDetails(String boqSiteId, int tempBOQNo){
		Map<String,List<BOQProductDetailsDto>> WorkAreaWiseProdDtls = new HashMap<String,List<BOQProductDetailsDto>>();
		String query = "select WO_WORK_AREA_ID,WO_WORK_AREA,MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID, "
				+ "PER_UNIT_QUANTITY, PER_UNIT_AMOUNT, TOTAL_QUANTITY, TOTAL_AMOUNT, REMARKS, ACTION "
				+ "from QS_TEMP_BOQ_PRODUCT_DTLS where SITE_ID = ? and TEMP_BOQ_NO = ? ";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(query, new Object[]{boqSiteId, tempBOQNo});
		for(Map<String,Object> map : list){
			String tempWorkAreaId = map.get("WO_WORK_AREA_ID").toString();
			BOQProductDetailsDto objPD = new BOQProductDetailsDto();
			objPD.setWorkAreaId(tempWorkAreaId);
			objPD.setWorkArea(Double.valueOf(map.get("WO_WORK_AREA").toString()));
			objPD.setMaterialGroupId(map.get("MATERIAL_GROUP_ID").toString());
			objPD.setMaterialGroupMeasurementId(map.get("MATERIAL_GROUP_MEASUREMENT_ID").toString());
			objPD.setPerUnitQuantity(Double.valueOf(map.get("PER_UNIT_QUANTITY").toString()));
			objPD.setPerUnitAmount(Double.valueOf(map.get("PER_UNIT_AMOUNT").toString()));
			objPD.setTotalQuantity(Double.valueOf(map.get("TOTAL_QUANTITY").toString()));
			objPD.setTotalAmount(Double.valueOf(map.get("TOTAL_AMOUNT").toString()));
			objPD.setRemarks(map.get("REMARKS")==null?"":map.get("REMARKS").toString());
			objPD.setAction(map.get("ACTION")==null?"":map.get("ACTION").toString());
			
			if(WorkAreaWiseProdDtls.containsKey(tempWorkAreaId)){
				WorkAreaWiseProdDtls.get(tempWorkAreaId).add(objPD);
			}
			else{
				List<BOQProductDetailsDto> objPD_list= new ArrayList<BOQProductDetailsDto>();
				objPD_list.add(objPD);
				WorkAreaWiseProdDtls.put(tempWorkAreaId, objPD_list);
			}
		}
		return WorkAreaWiseProdDtls;
		
	}

	@Override
	public Map<String, String> getWorkAreaGroupIdsMap(int BOQSeqNo) {
		Map<String,String> WorkAreaGroupIdsMap = new HashMap<String,String>();
		String sql = "select WO_WORK_DESC_ID,BLOCK_ID,FLOOR_ID,WO_WORK_AREA_GROUP_ID from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = ? group by WO_WORK_DESC_ID,BLOCK_ID,FLOOR_ID,WO_WORK_AREA_GROUP_ID";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new Object[]{BOQSeqNo});
		for(Map<String,Object> map : list){
			String workDescriptionId = map.get("WO_WORK_DESC_ID").toString();
			String blockId = map.get("BLOCK_ID").toString();
			String floorId = map.get("FLOOR_ID")==null?null:map.get("FLOOR_ID").toString();
			String workAreaGroupId = map.get("WO_WORK_AREA_GROUP_ID").toString();
			if(WorkAreaGroupIdsMap.containsKey(workDescriptionId + "@@" + blockId + "@@" + floorId)){
				throw new RuntimeException("Multiple WorkAreaGroupIds for same work area - "+workDescriptionId + "@@" + blockId + "@@" + floorId);
			}
			WorkAreaGroupIdsMap.put(workDescriptionId + "@@" + blockId + "@@" + floorId,workAreaGroupId);
		}
		return WorkAreaGroupIdsMap;
	}

	@Override
	public MultiObject getMajorHeadWiseWDs(int BOQSeqNo) {
		Map<String, List<String>> mapOfStringAndList1 = new HashMap<String, List<String>>();
		Map<String, String> mapOfStrings1 = new HashMap<String, String>();
		String sql = "select QWMAJ.WO_MAJORHEAD_DESC,QWMAJ.WO_MAJORHEAD_ID,QBAM.WO_WORK_DESC_ID,QWWD.WO_WORK_DESCRIPTION "
				+"from QS_BOQ_AREA_MAPPING QBAM,QS_WO_WORKDESC QWWD,QS_WO_MINORHEAD QWMIN,QS_WO_MAJORHEAD QWMAJ " 
				+"where QBAM.BOQ_SEQ_NO = ? and QBAM.WO_WORK_DESC_ID=QWWD.WO_WORK_DESC_ID " 
				+"and QWWD.WO_MINORHEAD_ID=QWMIN.WO_MINORHEAD_ID and QWMIN.WO_MAJORHEAD_ID=QWMAJ.WO_MAJORHEAD_ID " 
				+"group by  QWMAJ.WO_MAJORHEAD_DESC,QWMAJ.WO_MAJORHEAD_ID,QBAM.WO_WORK_DESC_ID,QWWD.WO_WORK_DESCRIPTION  ";
		List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, new Object[]{BOQSeqNo});
		for(Map<String,Object> map : list){
			String majorHeadId = map.get("WO_MAJORHEAD_ID").toString();
			String workDescriptionId = map.get("WO_WORK_DESC_ID").toString();
			String workDescription = map.get("WO_WORK_DESCRIPTION").toString();
			if(mapOfStringAndList1.containsKey(majorHeadId)){
				mapOfStringAndList1.get(majorHeadId).add(workDescriptionId);
			}
			else{
				List<String> listOfWDid = new ArrayList<String>();
				listOfWDid.add(workDescriptionId);
				mapOfStringAndList1.put(majorHeadId, listOfWDid);
			}
			mapOfStrings1.put(workDescriptionId, workDescription);
		}
		MultiObject multiObj = new MultiObject();
		multiObj.setMapOfStringAndList1(mapOfStringAndList1);
		multiObj.setMapOfStrings1(mapOfStrings1);
		return multiObj;
	}
	@Override
	public double getBOQTotalinBOQAreaMapping(int BOQSeqNo){
		String sql = "select sum(WO_WORK_AREA*QS_AREA_PRICE_PER_UNIT) from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = ? ";
		return jdbcTemplate.queryForObject(sql, Double.class);
	}
	@Override
	public double getBOQMaterialTotalinBOQProductDetails(int BOQSeqNo){
		String sql = "select sum(WO_WORK_AREA*PER_UNIT_AMOUNT) from QS_BOQ_PRODUCT_DTLS where BOQ_SEQ_NO = ? ";
		return jdbcTemplate.queryForObject(sql, Double.class);
	}
	@Override
	public void getWDdataInDB(String wd,int BOQSeqNo, HashMap<String, BOQBean> workAreaNoChangesInPrevWD, List<String> workAreaGroupNoChangesInPrevWD) {
		String sql = "select RECORD_TYPE,WO_WORK_DESC_ID,BLOCK_ID,FLOOR_ID,WO_WORK_AREA,WO_WORK_INITIATE_AREA from QS_BOQ_AREA_MAPPING where WO_WORK_DESC_ID = ? and BOQ_SEQ_NO = ? ";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql, new Object[] {wd, BOQSeqNo});
		for(Map<String,Object> map : list){
			String recordType = map.get("RECORD_TYPE").toString();
			String workDescriptionId = map.get("WO_WORK_DESC_ID").toString();
			String blockId = map.get("BLOCK_ID").toString();
			String floorId = map.get("FLOOR_ID")==null?null:map.get("FLOOR_ID").toString();
			String presentArea = map.get("WO_WORK_AREA").toString();
			String wointiateArea = map.get("WO_WORK_INITIATE_AREA").toString();
			
			BOQBean bean = new BOQBean();
			bean.setWoWorkInitiateArea(wointiateArea);
			bean.setWoWorkArea(presentArea);
			bean.setStrArea(presentArea);
			
			workAreaNoChangesInPrevWD.put(recordType+"@@"+workDescriptionId + "@@" + blockId + "@@" + floorId, bean);
			workAreaGroupNoChangesInPrevWD.add(workDescriptionId+"@@"+blockId+"@@"+floorId);
		
		}
		
	}

	@Override
	public boolean checkingIsItReallyPendindOnCurrentUser(String userId, String tempBOQNumber) {
		String sql = "select TEMP_BOQ_NO from QS_TEMP_BOQ where TEMP_BOQ_NO = ? AND PENDING_EMP_ID = ? AND STATUS = 'A' ";
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql,new Object[]{tempBOQNumber,userId});
		if(list==null) { return false; }
		else if(list.size()==0) { return false;}
		else { return true; }
	}

	@Override
	public void finalValidationOfGrandTotalInEveryTable(String BOQNumber) {
		String boqSeqNo = jdbcTemplate.queryForObject("select BOQ_SEQ_NO from QS_BOQ where STATUS='A' and BOQ_NO = '"+BOQNumber+"'",String.class);
		double val1 = jdbcTemplate.queryForObject("select sum(BOQ_TOTAL_AMOUNT) from QS_BOQ where BOQ_SEQ_NO = '"+boqSeqNo+"'",Double.class);
		double val2 = jdbcTemplate.queryForObject("select sum(WO_WORK_AREA_AMOUNT) from QS_BOQ_DETAILS where BOQ_SEQ_NO = '"+boqSeqNo+"'",Double.class);
		double val3 = jdbcTemplate.queryForObject("select sum(QS_AREA_AMOUNT) from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = '"+boqSeqNo+"'",Double.class);
		double val4 = 0;
		try{val4 = jdbcTemplate.queryForObject("select sum(TOTAL_AMOUNT) from QS_BOQ_PRODUCT_DTLS where BOQ_SEQ_NO = '"+boqSeqNo+"'",Double.class);}
		catch(Exception e){val4=0;}
		double val5 = 0;
		try{val5 = jdbcTemplate.queryForObject("select sum(QS_AREA_AMOUNT) from QS_BOQ_AREA_MAPPING where BOQ_SEQ_NO = '"+boqSeqNo+"' and RECORD_TYPE = 'MATERIAL'",Double.class);}
		catch(Exception e){val5=0;}
		System.out.println("QS_BOQ total                : "+val1);
		System.out.println("QS_BOQ_DETAILS total        : "+val2);
		System.out.println("QS_BOQ_AREA_MAPPING total   : "+val3);
		System.out.println("QS_BOQ_PRODUCT_DTLS total   : "+val4);
		System.out.println("QS_BOQ_AREA_MAPPING m_total : "+val5);
		if(Math.abs(val1-val2)>1){ throw new RuntimeException("Final Grand Total Verification Failed. QS_BOQ total: "+val1+"QS_BOQ_DETAILS total: "+val2+"QS_BOQ_AREA_MAPPING total: "+val3+"QS_BOQ_PRODUCT_DTLS total: "+val4+"QS_BOQ_AREA_MAPPING m_total : "+val5); }
		if(Math.abs(val1-val3)>1){ throw new RuntimeException("Final Grand Total Verification Failed. QS_BOQ total: "+val1+"QS_BOQ_DETAILS total: "+val2+"QS_BOQ_AREA_MAPPING total: "+val3+"QS_BOQ_PRODUCT_DTLS total: "+val4+"QS_BOQ_AREA_MAPPING m_total : "+val5); }
		if(Math.abs(val4-val5)>1){ throw new RuntimeException("Final Grand Total Verification Failed. QS_BOQ total: "+val1+"QS_BOQ_DETAILS total: "+val2+"QS_BOQ_AREA_MAPPING total: "+val3+"QS_BOQ_PRODUCT_DTLS total: "+val4+"QS_BOQ_AREA_MAPPING m_total : "+val5); }
	}
	
	
	
	


	@Override
	public List<Map<String, Object>> getTempBOQAreaMappingDataInOrderOfWD(int tempBOQNo) {
		String sql = "select WO_WORK_AREA_ID,WO_WORK_DESC_ID,WO_MEASURMENT_ID,FLOOR_ID,FLAT_ID,BLOCK_ID,WO_WORK_AREA,STATUS,"
				+ "QS_TEMP_BOQ_DETAILS_ID,QS_AREA_PRICE_PER_UNIT,QS_AREA_AMOUNT,ACTION,RECORD_TYPE "
				+ "from QS_TEMP_BOQ_AREA_MAPPING where TEMP_BOQ_NO = ? order by WO_WORK_DESC_ID";

		return jdbcTemplate.queryForList(sql,new Object[]{tempBOQNo});
		
		
	}

	@Override
	public List<Map<String, Object>> getWorkLocationName(String recordType, String workDescriptionId, String blockId, String floorId) {
		String sql = "select QWM.WO_MAJORHEAD_DESC,QSMINOR.WO_MINORHEAD_DESC,QWD.WO_WORK_DESCRIPTION,B.NAME as BLOCK_NAME, F.NAME as FLOOR_NAME, FLAT.NAME as FLAT_NAME "
				+ "from QS_WO_MAJORHEAD QWM,QS_WO_MINORHEAD QSMINOR,QS_WO_WORKDESC QWD,"
				+ "QS_TEMP_BOQ_AREA_MAPPING QBAM left outer join BLOCK B on B.BLOCK_ID = QBAM.BLOCK_ID "
				+ "left outer join FLOOR F on F.FLOOR_ID = QBAM.FLOOR_ID "
				+ "left outer join FLAT FLAT on FLAT.FLAT_ID = QBAM.FLAT_ID "
				+ "where QBAM.WO_WORK_DESC_ID = QWD.WO_WORK_DESC_ID and QWD.WO_MINORHEAD_ID = QSMINOR.WO_MINORHEAD_ID "
				+ "and QSMINOR.WO_MAJORHEAD_ID = QWM.WO_MAJORHEAD_ID "
				+ "and QBAM.RECORD_TYPE = ? and QBAM.WO_WORK_DESC_ID = ? and QBAM.BLOCK_ID = ? ";
		if(StringUtils.isBlank(floorId)){
			sql = sql + " and QBAM.FLOOR_ID is null ";
		}else{
			sql = sql + " and QBAM.FLOOR_ID = '"+floorId+"' ";
		}
		return jdbcTemplate.queryForList(sql,new Object[]{recordType, workDescriptionId, blockId});
	}
	
}
