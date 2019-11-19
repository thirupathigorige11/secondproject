package com.sumadhura.transdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.AdminTempChildProductStatusDto;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.UIProperties;
@Repository
public class ProductDao extends UIProperties  {

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	//JdbcTemplate template=null;
	//SELECT * FROM CHILD_PRODUCT WHERE UPPER(NAME) = UPPER('Ms Elbow ©CLASS 65NB - FF72') AND SUB_PRODUCT_ID='SP1020' AND STATUS='A'
	public int checkProductNameAvailableOrNot(String productName,String site_id,String productType) {

		int res = 0;
		String sql = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> TotalProductList = null;
		String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		try {
			logger.info("Inside checkProductAvailableOrNot() Product Name --->  " + productName);
			template = new JdbcTemplate(DBConnection.getDbConnection());
			/*if(!productType.equals("")){
				sql = "SELECT * FROM PRODUCT WHERE UPPER(NAME) =UPPER('"+productName+"') AND STATUS='A' AND PRODUCT_DEPT='"+productType+"'";
			}
			else if(site_id.equals(marketing_dept_id)){
				sql = "SELECT * FROM PRODUCT WHERE UPPER(NAME) =UPPER('"+productName+"') AND STATUS='A' AND PRODUCT_DEPT='MARKETING'";
			}else{*/
				sql = "SELECT * FROM PRODUCT WHERE UPPER(NAME) =UPPER('"+productName+"') AND STATUS='A'";
			//}
			
			TotalProductList = template.queryForList(sql, new Object[] {});
			if (null != TotalProductList && TotalProductList.size() > 0) {
				res = 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			template = null;
		}
		return res;
	}
	
	
	
	public int checkSubProductNameAvailableOrNot(String productId, String subProductName) {

		int res = 0;
		String sql = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> TotalProductList = null;
		try {

			logger.info("Inside checkSubProductNameAvailableOrNot() method in ProductDao class--->  Product Id -->"+productId+" Subproduct Name is "+subProductName);
			template = new JdbcTemplate(DBConnection.getDbConnection());
			sql = "SELECT * FROM SUB_PRODUCT WHERE UPPER(NAME) = UPPER('"+subProductName+"') AND PRODUCT_ID='"+productId+"' AND STATUS='A'";
			logger.info("query in Inside checkSubProductNameAvailableOrNot() method "+ sql);
			TotalProductList = template.queryForList(sql, new Object[] {});
			
			if (null != TotalProductList && TotalProductList.size() > 0) {
				res = 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			template = null;
		}
		return res;
	}
	
	
	
	public int checkChildProductNameAvailableOrNot(String subProductId, String childProductName) {

		int res = 0;
		String sql = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> TotalProductList = null;
		try {

			logger.info("Inside checkChildProductNameAvailableOrNot() method in ProductDao class---> subProductId -->"+subProductId+" Subproduct Name is "+childProductName);
			template = new JdbcTemplate(DBConnection.getDbConnection());
			sql = "SELECT * FROM CHILD_PRODUCT WHERE UPPER(NAME) = UPPER('"+childProductName+"') AND SUB_PRODUCT_ID='"+subProductId+"' AND STATUS='A'";
			logger.info("query in Inside checkChildProductNameAvailableOrNot() method "+ sql);
			TotalProductList = template.queryForList(sql, new Object[] {});
			
			if (null != TotalProductList && TotalProductList.size() > 0) {
				res = 1;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			template = null;
		}
		return res;
	}
	
	public int saveproduct(String strProductName,String strProductId,String productType,String siteId) throws Exception{

		int res = 0;
		String sql="";
		JdbcTemplate template=null;
		String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		try{

			logger.info("save db1 "+strProductName);
			template = new JdbcTemplate(DBConnection.getDbConnection());
			logger.info("db connection");
			
			if(!productType.equals("")){
				 sql="insert into product(name,PRODUCT_ID,STATUS,created_date,PRODUCT_DEPT) values(?,?,?,sysdate,?)";  
				res  = template.update(sql,new Object[]{strProductName,strProductId,"A",productType});
			}else if(siteId.equals(marketing_dept_id)){
				 sql="insert into product(name,PRODUCT_ID,STATUS,created_date,PRODUCT_DEPT) values(?,?,?,sysdate,?)";  
					res  = template.update(sql,new Object[]{strProductName,strProductId,"A","MARKETING"});
			}else{
				
				sql="insert into product(name,PRODUCT_ID,STATUS,created_date,PRODUCT_DEPT) values(?,?,?,sysdate,?)";  
				res  = template.update(sql,new Object[]{strProductName,strProductId,"A","STORE"});
			}
			

		}
		catch(Exception e){
			e.printStackTrace();
		}

		return res;

	}
	public List<ProductDetails> getTotalProducts(String sessionSite_id,String allProducts) {
		JdbcTemplate template=null;
		List<Map<String,Object>> TotalProductList = null;
		ProductDetails objProductDetails = null;
		List<ProductDetails> listProductList =  new ArrayList<ProductDetails>();
		String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String sql ="";
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			if(allProducts.equalsIgnoreCase("true")){
				sql = "select name,PRODUCT_DEPT from product where STATUS='A' ";
			}
			else if(sessionSite_id.equals(marketing_dept_id)){
				sql = "select name,PRODUCT_DEPT from product where STATUS='A' AND PRODUCT_DEPT in ('ALL','MARKETING')";
			}else{
				sql = "select name,PRODUCT_DEPT from product where STATUS='A' AND PRODUCT_DEPT in ('ALL','STORE')";
			}
			

			logger.info(sql);

			TotalProductList = template.queryForList(sql,new Object[]{});
			int i=1;
			for(Map productList : TotalProductList){
				objProductDetails = new ProductDetails();

				objProductDetails.setProductName((productList.get("name") == null ? "" : productList.get("name").toString()));
				objProductDetails.setProductType((productList.get("PRODUCT_DEPT") == null ? "" : productList.get("PRODUCT_DEPT").toString()));
				objProductDetails.setSerialno(i);
				listProductList.add(objProductDetails);
				i++;
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return listProductList;
	}

	
	public int deleteproduct(String strProductId,String siteId,String productType){
		//int id = 0;
		JdbcTemplate template=null;
		int count = 0;
		List<Map<String,Object>> TotalSubProductList = null;
		String sql ="";
		//String query="";
		String subProductId="";
		String childProductId="";
		//String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		try{
			template = new JdbcTemplate(DBConnection.getDbConnection());

			int result=checkDeletedProductReceive(strProductId);
			if(result==0){
				sql = "select SUB_PRODUCT_ID,NAME from SUB_PRODUCT WHERE PRODUCT_ID =?";
				TotalSubProductList = template.queryForList(sql,new Object[]{strProductId});
				if(TotalSubProductList.size()>0){
					for(Map productList : TotalSubProductList){
						subProductId=productList.get("SUB_PRODUCT_ID") == null ? "" : productList.get("SUB_PRODUCT_ID").toString();
						if(!subProductId.equals("")){
							childProductId="UPDATE CHILD_PRODUCT SET STATUS = 'I' WHERE SUB_PRODUCT_ID =?";
							count = template.update(childProductId,new Object[]{subProductId});

							sql = "UPDATE SUB_PRODUCT SET STATUS = 'I' WHERE PRODUCT_ID =?";
							count = template.update(sql,new Object[]{strProductId});
						}
					}
				}

				sql = "UPDATE product SET STATUS = 'I' WHERE PRODUCT_ID = ?";
				count = template.update(sql,new Object[]{strProductId});
			}


		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}


	public int savesubproduct(String strProductId,String strSubProductId,String strSubProductName) throws Exception{

		int res = 0;
		JdbcTemplate template=null;
		
		try{

			logger.info("save subdb "+strSubProductId);

			template = new JdbcTemplate(DBConnection.getDbConnection());
			logger.info("db connection");
			String sql="insert into sub_product(SUB_PRODUCT_ID,PRODUCT_ID,NAME,STATUS) values(?,?,?,?)";  
			res  = template.update(sql,new Object[]{strSubProductId,strProductId,strSubProductName,"A"});

		}
		catch(Exception e){
			e.printStackTrace();
		}

		return res;

	}
	public List<ProductDetails> getTotalSubProducts(String Site_id,String AllProducts) {
		JdbcTemplate template=null;
		List<Map<String,Object>> TotalSubProductList = null;
		ProductDetails objProductDetails = null;
		List<ProductDetails> listSubProductList =  new ArrayList<ProductDetails>();
		String sql ="";
		String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			if(AllProducts.equalsIgnoreCase("true")){
				sql = "select S.NAME as Sub_ProductName ,P.NAME as ProductName,P.PRODUCT_DEPT from  SUB_PRODUCT S,PRODUCT P WHERE S.PRODUCT_ID = P.PRODUCT_ID  and S.STATUS = 'A'";
			}
			else if(Site_id.equals(marketing_dept_id)){
				sql = "select S.NAME as Sub_ProductName ,P.NAME as ProductName,P.PRODUCT_DEPT from  SUB_PRODUCT S,PRODUCT P WHERE S.PRODUCT_ID = P.PRODUCT_ID  and S.STATUS = 'A' AND P.PRODUCT_DEPT in ('ALL','MARKETING')";
			}else{
				sql = "select S.NAME as Sub_ProductName ,P.NAME as ProductName,P.PRODUCT_DEPT from  SUB_PRODUCT S,PRODUCT P WHERE S.PRODUCT_ID = P.PRODUCT_ID  and S.STATUS = 'A' AND P.PRODUCT_DEPT in('ALL','STORE')";
			}
			 

			logger.info(sql);

			TotalSubProductList = template.queryForList(sql,new Object[]{});

			for(Map productList : TotalSubProductList){
				objProductDetails = new ProductDetails();
				objProductDetails.setSub_ProductName((productList.get("Sub_ProductName") == null ? "" : productList.get("Sub_ProductName").toString()));
				objProductDetails.setProductName((productList.get("ProductName") == null ? "" : productList.get("ProductName").toString()));
				objProductDetails.setProductType((productList.get("PRODUCT_DEPT") == null ? "" : productList.get("PRODUCT_DEPT").toString()));
				listSubProductList.add(objProductDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();
			
		}

		return listSubProductList;
	}
	
	public int deletesubproduct(String strProductId,String strSubProductId){
		//int id = 0;
		JdbcTemplate template=null;
		int count = 0;
		List<Map<String,Object>> TotalChildProductList = null;
		String childProductId="";
		try{
			template = new JdbcTemplate(DBConnection.getDbConnection());
			int result=checkDeletedSubProductReceive(strSubProductId);
			if(result==0){
			childProductId="UPDATE CHILD_PRODUCT SET STATUS = 'I' WHERE SUB_PRODUCT_ID =?";
			count = template.update(childProductId,new Object[]{strSubProductId});

			String sql = "UPDATE sub_product SET STATUS = ? WHERE  PRODUCT_ID = ? and SUB_PRODUCT_ID = ?";
			count = template.update(sql,new Object[]{"I",strProductId,strSubProductId});
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}

	//not using this method because of approvals....
	//oct 20 - using this method again. because saving on first level at time of creation
	public int saveChildProduct(String strChildProductName,String strChildProductId,String strSubProductId,String strMeasurementName,String strMeasurementId, String materialGroupId) throws Exception{
		 
		int res = 0;
		JdbcTemplate template=null;
		String sql= "";
		try{
			template = new JdbcTemplate(DBConnection.getDbConnection());
			
			
			logger.info("db connection for child product");
			sql="insert into child_product(CHILD_PRODUCT_ID,SUB_PRODUCT_ID,name,STATUS, MATERIAL_GROUP_ID) values(?,?,?,?,?)";  
			res  = template.update(sql,new Object[]{strChildProductId,strSubProductId,strChildProductName,"A",materialGroupId});

			logger.info("db connection for measurement");
			 sql="insert into measurement(MEASUREMENT_ID,NAME,CREATED_DATE,CHILD_PRODUCT_ID, STATUS) values(?,?,sysdate,?,?)";  
			res  = template.update(sql,new Object[]{strMeasurementId,strMeasurementName,strChildProductId,"A"});

		}
		catch(Exception e){
			e.printStackTrace();
		}

		return res;

	}
	public int saveTempChildProduct(AdminTempChildProductStatusDto tempChildProductDto) throws Exception{
		 
		int res = 0;
		String sql= "";
		try{
			
			logger.info("db connection for child product");
			sql="insert into ADMIN_TEMP_CHILDPRODUCT_STATUS(TEMP_CHILDPRODUCT_ID,PRODUCT_ID,PRODUCT_NAME,SUB_PRODUCT_ID,SUB_PRODUCT_NAME,CHILD_PRODUCT_ID,CHILD_PRODUCT_NAME,MEASUREMENT_ID,MEASUREMENT_NAME,PENDING_EMPLOYEE_ID,CHILD_PRODUCT_STATUS,MATERIAL_GROUP_ID,CREATION_DATE) "
					+ "values(?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";  
			logger.info(sql);
			res  = jdbcTemplate.update(sql,new Object[]{
			tempChildProductDto.getTempChildProductId(),
			tempChildProductDto.getProductId(),
			tempChildProductDto.getProductName(),
			tempChildProductDto.getSubProductId(),
			tempChildProductDto.getSubProductName(),
			tempChildProductDto.getChildProductId(),
			tempChildProductDto.getChildProductName(),
			tempChildProductDto.getMeasurementId(),
			tempChildProductDto.getMeasurementName(),
			tempChildProductDto.getPendingEmployeeId(),
			tempChildProductDto.getChildProductStatus(),
			tempChildProductDto.getMaterialGroupId()
			});

			
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return res;

	}
	public int geTempChildproductSeq(){
		return jdbcTemplate.queryForInt("select ADM_TEMP_CHILDPROD_STATUS_SEQ.nextval from dual");
	}
	public void saveTempChildProdAprRejDtls(String tempChildProductId, String user_id, String action, String remarks) {
		String sql = "insert into ADMIN_PRODUCT_APP_REJ_DETAILS(ADM_PROD_APP_REJ_DTLS_ID,TEMP_CHILDPRODUCT_ID,ACTION,PRODUCT_APPR_EMP_ID,APPROVING_DATE,REMARKS) "
				+ " values(ADM_PROD_APP_REJ_DTLS_SEQ.nextval,?,?,?,sysdate,?)";
		logger.info(sql);
		jdbcTemplate.update(sql,new Object[]{tempChildProductId,action,user_id,remarks});
	}

	public List<AdminTempChildProductStatusDto> getPendingChildProductsForApproval(String user_id) {
		String sql = "select ATCPS.CREATION_DATE,ATCPS.TEMP_CHILDPRODUCT_ID,ATCPS.PRODUCT_NAME,ATCPS.SUB_PRODUCT_NAME,"
					+ " ATCPS.CHILD_PRODUCT_NAME,ATCPS.MEASUREMENT_NAME,ATCPS.SUB_PRODUCT_ID,"
					+ " PGM.MATERIAL_GROUP_NAME "
					
					+ ",(SELECT LISTAGG(CONCAT(CONCAT(SED1.EMP_NAME,'  :  '),APARD.REMARKS), '@@@') "
					+ " WITHIN GROUP (ORDER BY APARD.ADM_PROD_APP_REJ_DTLS_ID) "
					+ " from ADMIN_PRODUCT_APP_REJ_DETAILS APARD,SUMADHURA_EMPLOYEE_DETAILS SED1 "
					+ " where SED1.EMP_ID = APARD.PRODUCT_APPR_EMP_ID and APARD.TEMP_CHILDPRODUCT_ID = ATCPS.TEMP_CHILDPRODUCT_ID and regexp_like(APARD.REMARKS, '[a-zA-Z0-9]')) as REMARKS " 
							
					+ " from ADMIN_TEMP_CHILDPRODUCT_STATUS ATCPS left outer join PRODUCT_GROUP_MASTER PGM on ATCPS.MATERIAL_GROUP_ID = PGM.MATERIAL_GROUP_ID "
					+ " where ATCPS.PENDING_EMPLOYEE_ID = ? and ATCPS.CHILD_PRODUCT_STATUS='A' ";
		List<Map<String,Object>> list = null;
		try {
			logger.info(sql);
			list = jdbcTemplate.queryForList(sql,new Object[]{user_id});
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		List<AdminTempChildProductStatusDto> beanlist = new ArrayList<AdminTempChildProductStatusDto>();
		int intSerialNo = 0;
		String tempChildProductId = "";
		String productName = "";
		String subProductName = "";
		String childProductName = "";
		String measurementName = "";
		String materialGroupName = "";
		String creationDate = "";
		String subProductId = "";
		String remarks = "";
		
		for(Map<String,Object> map : list){
		tempChildProductId = map.get("TEMP_CHILDPRODUCT_ID")==null ? "" : map.get("TEMP_CHILDPRODUCT_ID").toString();
		productName = map.get("PRODUCT_NAME")==null ? "" : map.get("PRODUCT_NAME").toString();
		subProductName = map.get("SUB_PRODUCT_NAME")==null ? "" : map.get("SUB_PRODUCT_NAME").toString();
		childProductName = map.get("CHILD_PRODUCT_NAME")==null ? "" : map.get("CHILD_PRODUCT_NAME").toString();
		measurementName = map.get("MEASUREMENT_NAME")==null ? "" : map.get("MEASUREMENT_NAME").toString();
		materialGroupName = map.get("MATERIAL_GROUP_NAME")==null ? "" : map.get("MATERIAL_GROUP_NAME").toString();
		creationDate = map.get("CREATION_DATE")==null ? "" : map.get("CREATION_DATE").toString();
		subProductId = map.get("SUB_PRODUCT_ID")==null ? "" : map.get("SUB_PRODUCT_ID").toString();
		remarks = map.get("REMARKS")==null ? "" : map.get("REMARKS").toString();
		
		AdminTempChildProductStatusDto bean = new AdminTempChildProductStatusDto();
		bean.setIntSerialNo(++intSerialNo);
		bean.setTempChildProductId(tempChildProductId);
		bean.setProductName(productName);
		bean.setSubProductName(subProductName);
		bean.setChildProductName(childProductName);
		bean.setMeasurementName(measurementName);
		bean.setMaterialGroupName(materialGroupName);
		bean.setCreationDate(DateUtil.convertDBDateInAnotherFormat(creationDate,"dd-MMM-yy"));
		bean.setSubProductId(subProductId);
		bean.setStrRemarks(remarks);
		bean.setStrRemarksForView(StringUtils.isBlank(remarks) ? "-" : remarks.replace("@@@", ", ").substring(0, 5).concat("....."));
		bean.setStrRemarksForTitle(StringUtils.isBlank(remarks) ? "-" : remarks.replace("@@@", "&#013;"));
		beanlist.add(bean);
		}
		return beanlist;
	}
	public List<AdminTempChildProductStatusDto> getTempChildProductRequestStatus() {
		String sql = "select ATCPS.CREATION_DATE,ATCPS.TEMP_CHILDPRODUCT_ID,ATCPS.PRODUCT_NAME,ATCPS.SUB_PRODUCT_NAME,"
					+ " ATCPS.CHILD_PRODUCT_NAME,ATCPS.MEASUREMENT_NAME,ATCPS.SUB_PRODUCT_ID,"
					+ " PGM.MATERIAL_GROUP_NAME , SED.EMP_NAME, ATCPS.CHILD_PRODUCT_STATUS "
					
					+ ",(SELECT LISTAGG(CONCAT(CONCAT(SED1.EMP_NAME,'  :  '),APARD.REMARKS), '@@@') "
					+ " WITHIN GROUP (ORDER BY APARD.ADM_PROD_APP_REJ_DTLS_ID) "
					+ " from ADMIN_PRODUCT_APP_REJ_DETAILS APARD,SUMADHURA_EMPLOYEE_DETAILS SED1 "
					+ " where SED1.EMP_ID = APARD.PRODUCT_APPR_EMP_ID and APARD.TEMP_CHILDPRODUCT_ID = ATCPS.TEMP_CHILDPRODUCT_ID and regexp_like(APARD.REMARKS, '[a-zA-Z0-9]')) as REMARKS " 
							
					+ " from ADMIN_TEMP_CHILDPRODUCT_STATUS ATCPS left outer join PRODUCT_GROUP_MASTER PGM on ATCPS.MATERIAL_GROUP_ID = PGM.MATERIAL_GROUP_ID , SUMADHURA_EMPLOYEE_DETAILS SED "
					+ " where ATCPS.CHILD_PRODUCT_STATUS!='I' "
					+ " and SED.EMP_ID=ATCPS.PENDING_EMPLOYEE_ID ";
		List<Map<String,Object>> list = null;
		try {
			logger.info(sql);
			list = jdbcTemplate.queryForList(sql);
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		List<AdminTempChildProductStatusDto> beanlist = new ArrayList<AdminTempChildProductStatusDto>();
		int intSerialNo = 0;
		String tempChildProductId = "";
		String productName = "";
		String subProductName = "";
		String childProductName = "";
		String measurementName = "";
		String materialGroupName = "";
		String creationDate = "";
		String subProductId = "";
		String childProductStatus = "";
		String pendingEmployeeId = "";
		String remarks = "";
		
		for(Map<String,Object> map : list){
		tempChildProductId = map.get("TEMP_CHILDPRODUCT_ID")==null ? "" : map.get("TEMP_CHILDPRODUCT_ID").toString();
		productName = map.get("PRODUCT_NAME")==null ? "" : map.get("PRODUCT_NAME").toString();
		subProductName = map.get("SUB_PRODUCT_NAME")==null ? "" : map.get("SUB_PRODUCT_NAME").toString();
		childProductName = map.get("CHILD_PRODUCT_NAME")==null ? "" : map.get("CHILD_PRODUCT_NAME").toString();
		measurementName = map.get("MEASUREMENT_NAME")==null ? "" : map.get("MEASUREMENT_NAME").toString();
		materialGroupName = map.get("MATERIAL_GROUP_NAME")==null ? "" : map.get("MATERIAL_GROUP_NAME").toString();
		creationDate = map.get("CREATION_DATE")==null ? "" : map.get("CREATION_DATE").toString();
		subProductId = map.get("SUB_PRODUCT_ID")==null ? "" : map.get("SUB_PRODUCT_ID").toString();
		pendingEmployeeId = map.get("EMP_NAME")==null ? "" : map.get("EMP_NAME").toString();
		childProductStatus = map.get("CHILD_PRODUCT_STATUS")==null ? "" : map.get("CHILD_PRODUCT_STATUS").toString();
		remarks = map.get("REMARKS")==null ? "" : map.get("REMARKS").toString();
		AdminTempChildProductStatusDto bean = new AdminTempChildProductStatusDto();
		bean.setIntSerialNo(++intSerialNo);
		bean.setTempChildProductId(tempChildProductId);
		bean.setProductName(productName);
		bean.setSubProductName(subProductName);
		bean.setChildProductName(childProductName);
		bean.setMeasurementName(measurementName);
		bean.setMaterialGroupName(materialGroupName);
		bean.setCreationDate(DateUtil.convertDBDateInAnotherFormat(creationDate,"dd-MMM-yy"));
		bean.setSubProductId(subProductId);
		bean.setPendingEmployeeId(pendingEmployeeId);
		bean.setChildProductStatus(childProductStatus.equals("A")?"Active" : (childProductStatus.equals("R")?"Rejected":(childProductStatus.equals("I")?"Completed":"")));
		bean.setStrRemarks(remarks);
		bean.setStrRemarksForView(StringUtils.isBlank(remarks) ? "-" : remarks.replace("@@@", ", ").substring(0, 5).concat("....."));
		bean.setStrRemarksForTitle(StringUtils.isBlank(remarks) ? "-" : remarks.replace("@@@", "&#013;"));
		beanlist.add(bean);
		}
		return beanlist;
	}
	public AdminTempChildProductStatusDto getTempChildProductDetails(String user_id,String tempChildProductId) {
		String sql = " select TEMP_CHILDPRODUCT_ID,PRODUCT_ID,PRODUCT_NAME,SUB_PRODUCT_ID,SUB_PRODUCT_NAME,CHILD_PRODUCT_ID,CHILD_PRODUCT_NAME,MEASUREMENT_ID,MEASUREMENT_NAME,PENDING_EMPLOYEE_ID,CHILD_PRODUCT_STATUS,MATERIAL_GROUP_ID,CREATION_DATE "
					+ " from ADMIN_TEMP_CHILDPRODUCT_STATUS where PENDING_EMPLOYEE_ID = ? and TEMP_CHILDPRODUCT_ID = ? ";
		Map<String,Object> map = null;
		try {
			logger.info(sql);
			map = jdbcTemplate.queryForMap(sql,new Object[]{user_id, tempChildProductId});
		} catch (DataAccessException e) {
			map=null;
			e.printStackTrace();
		}
		if(map==null){
			return null;
		}
					
		String productId = "";
		String productName = "";
		String subProductId = "";
		String subProductName = "";
		String childProductId = "";
		String childProductName = "";
		String measurementId = "";
		String measurementName = "";
		String pendingEmployeeId = "";
		String childProductStatus = "";
		String materialGroupId = "";
		String creationDate = "";
		AdminTempChildProductStatusDto bean = new AdminTempChildProductStatusDto();
		tempChildProductId = map.get("TEMP_CHILDPRODUCT_ID")==null ? "" : map.get("TEMP_CHILDPRODUCT_ID").toString();
		productId = map.get("PRODUCT_ID")==null ? "" : map.get("PRODUCT_ID").toString();
		productName = map.get("PRODUCT_NAME")==null ? "" : map.get("PRODUCT_NAME").toString();
		subProductId = map.get("SUB_PRODUCT_ID")==null ? "" : map.get("SUB_PRODUCT_ID").toString();
		subProductName = map.get("SUB_PRODUCT_NAME")==null ? "" : map.get("SUB_PRODUCT_NAME").toString();
		childProductId = map.get("CHILD_PRODUCT_ID")==null ? "" : map.get("CHILD_PRODUCT_ID").toString();
		childProductName = map.get("CHILD_PRODUCT_NAME")==null ? "" : map.get("CHILD_PRODUCT_NAME").toString();
		measurementId = map.get("MEASUREMENT_ID")==null ? "" : map.get("MEASUREMENT_ID").toString();
		measurementName = map.get("MEASUREMENT_NAME")==null ? "" : map.get("MEASUREMENT_NAME").toString();
		pendingEmployeeId = map.get("PENDING_EMPLOYEE_ID")==null ? "" : map.get("PENDING_EMPLOYEE_ID").toString();
		childProductStatus = map.get("CHILD_PRODUCT_STATUS")==null ? "" : map.get("CHILD_PRODUCT_STATUS").toString();
		materialGroupId = map.get("MATERIAL_GROUP_ID")==null ? "" : map.get("MATERIAL_GROUP_ID").toString();
		creationDate = map.get("CREATION_DATE")==null ? "" : map.get("CREATION_DATE").toString();
		
		bean.setTempChildProductId(tempChildProductId);
		bean.setProductId(productId);
		bean.setProductName(productName);
		bean.setSubProductId(subProductId);
		bean.setSubProductName(subProductName);
		bean.setChildProductId(childProductId);
		bean.setChildProductName(childProductName);
		bean.setMeasurementId(measurementId);
		bean.setMeasurementName(measurementName);
		bean.setPendingEmployeeId(pendingEmployeeId);
		bean.setChildProductStatus(childProductStatus);
		bean.setMaterialGroupId(materialGroupId);
		bean.setCreationDate(creationDate);
		return bean;
	}
	public String getPendingEmpIdForCP_APPR(String user_id,String sessionSite_id){
		
		String sql = "select APPROVER_EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? and STATUS='A' and MODULE_TYPE = 'CP_APPR' and SITE_ID = ? ";
		logger.info(sql);
		String pendingEmpId =  jdbcTemplate.queryForObject(sql, new Object[]{user_id,sessionSite_id}, String.class);
		return pendingEmpId;
	}
	public void approveTempChildProduct(String tempChildProductId, String childProductName,String user_id,String sessionSite_id, List<String> errMsg, List<String> successMsg, String remarks) {
		
		String pendingEmpId = getPendingEmpIdForCP_APPR(user_id, sessionSite_id);
		//=======
		if(pendingEmpId.equals("END")){
			String sqlrcount = "select count(*) from  ADMIN_TEMP_CHILDPRODUCT_STATUS where TEMP_CHILDPRODUCT_ID = ? and CHILD_PRODUCT_STATUS='A' "; 
			logger.info(sqlrcount);
			int rcount = jdbcTemplate.queryForInt(sqlrcount,new Object[]{tempChildProductId});
			if(rcount!=1){errMsg.add("Approve for Child Product: "+childProductName+" with Id: "+tempChildProductId+" Failed.");return;}
			String sql2 = " insert into CHILD_PRODUCT(CHILD_PRODUCT_ID,SUB_PRODUCT_ID,NAME,STATUS,MATERIAL_GROUP_ID) "
					+ " select CHILD_PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_NAME,?,MATERIAL_GROUP_ID from ADMIN_TEMP_CHILDPRODUCT_STATUS where TEMP_CHILDPRODUCT_ID = ? and CHILD_PRODUCT_STATUS='A' ";
			int count = 0;
			try {
				logger.info(sql2);
				count = jdbcTemplate.update(sql2,new Object[]{"A",tempChildProductId});
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
			
			String sql4 = " insert into MEASUREMENT(MEASUREMENT_ID,NAME,CREATED_DATE,CHILD_PRODUCT_ID, STATUS) "
					+ " select MEASUREMENT_ID,MEASUREMENT_NAME,sysdate,CHILD_PRODUCT_ID,? from ADMIN_TEMP_CHILDPRODUCT_STATUS where TEMP_CHILDPRODUCT_ID = ? and CHILD_PRODUCT_STATUS='A' ";
			int count4 = 0;
			try {
				logger.info(sql4);
				count4 = jdbcTemplate.update(sql4,new Object[]{"A",tempChildProductId});
			} catch (DataAccessException e) {
				e.printStackTrace();
			}
			if(count==0||count4==0){errMsg.add("Approve for Child Product: "+childProductName+" with Id: "+tempChildProductId+" Failed.");return;}
			else if(count>1||count4>1){errMsg.add("Duplicate records found for Child Product: "+childProductName+" with Id: "+tempChildProductId+".");return;}
			
		}
		
		//==
		int count1 = 0;
		if(pendingEmpId.equals("END")){
			String sql3 = "update ADMIN_TEMP_CHILDPRODUCT_STATUS set PENDING_EMPLOYEE_ID = ?,CHILD_PRODUCT_STATUS='I' where TEMP_CHILDPRODUCT_ID = ? ";
			logger.info(sql3);
			count1 = jdbcTemplate.update(sql3,new Object[]{pendingEmpId,tempChildProductId});
		}else{
			String sql3 = "update ADMIN_TEMP_CHILDPRODUCT_STATUS set PENDING_EMPLOYEE_ID = ? where TEMP_CHILDPRODUCT_ID = ? ";
			logger.info(sql3);
			count1 = jdbcTemplate.update(sql3,new Object[]{pendingEmpId,tempChildProductId});
		}
		if(count1==1){
			saveTempChildProdAprRejDtls(String.valueOf(tempChildProductId),user_id,"A",remarks);
			if(pendingEmpId.equals("END")){
				successMsg.add("Child Product Name '"+childProductName+"' has been Added successfully.");
			}
			else{
				successMsg.add("Approved Child Product: "+childProductName+" with Id: "+tempChildProductId+".");
			}
		}
		else if(count1>1){errMsg.add("Duplicate records found for Child Product: "+childProductName+" with Id: "+tempChildProductId+".");}
		else{errMsg.add("Approve for Child Product: "+childProductName+" with Id: "+tempChildProductId+" Failed.");}
		return;
	}
	public void rejectTempChildProduct(String tempChildProductId,String childProductName,String user_id,String sessionSite_id, List<String> errMsg, List<String> successMsg, String remarks) {
		String sql3 = "update ADMIN_TEMP_CHILDPRODUCT_STATUS set CHILD_PRODUCT_STATUS = 'R' where TEMP_CHILDPRODUCT_ID = ? and CHILD_PRODUCT_STATUS='A' ";
		logger.info(sql3);
		int count = jdbcTemplate.update(sql3,new Object[]{tempChildProductId});
		if(count==0){errMsg.add("Reject for Child Product: "+childProductName+" with Id: "+tempChildProductId+" Failed.");}
		else if(count==1){
			saveTempChildProdAprRejDtls(String.valueOf(tempChildProductId),user_id,"R",remarks);
			successMsg.add("Rejected Child Product: "+childProductName+" with Id: "+tempChildProductId+".");
		}
		else{errMsg.add("More than one record found for Child Product: "+childProductName+" with Id: "+tempChildProductId+".");}
	
		return;
	}
	public List<ProductDetails> getAllChildProducts(String siteId,String AllProducts) {
		JdbcTemplate template=null;
		List<Map<String,Object>> AllProductList = null;
		ProductDetails objProductDetails = null;
		List<ProductDetails> listAllProductList =  new ArrayList<ProductDetails>();
		String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String sql ="";
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			if(AllProducts.equalsIgnoreCase("true")){
				sql = "select C.NAME as CHILD_PRODUCT_NAME,S.NAME as SUB_PRODUCTNAME ,P.NAME as PRODUCT_NAME,P.PRODUCT_DEPT,M.NAME AS MEASUREMENT from CHILD_PRODUCT c,SUB_PRODUCT S,PRODUCT P,MEASUREMENT M where C.SUB_PRODUCT_ID = S.SUB_PRODUCT_ID and S.PRODUCT_ID = P.PRODUCT_ID  and C.STATUS = 'A' AND M.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID";
			}
			else if(siteId.equals(marketing_dept_id)){
				sql = "select C.NAME as CHILD_PRODUCT_NAME,S.NAME as SUB_PRODUCTNAME ,P.NAME as PRODUCT_NAME,P.PRODUCT_DEPT,M.NAME AS MEASUREMENT from CHILD_PRODUCT c,SUB_PRODUCT S,PRODUCT P,MEASUREMENT M where C.SUB_PRODUCT_ID = S.SUB_PRODUCT_ID and S.PRODUCT_ID = P.PRODUCT_ID  and C.STATUS = 'A' AND P.PRODUCT_DEPT in('ALL','MARKETING') AND M.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID";
			}else{
				sql = "select C.NAME as CHILD_PRODUCT_NAME,S.NAME as SUB_PRODUCTNAME ,P.NAME as PRODUCT_NAME,P.PRODUCT_DEPT,M.NAME AS MEASUREMENT from CHILD_PRODUCT c,SUB_PRODUCT S,PRODUCT P,MEASUREMENT M where C.SUB_PRODUCT_ID = S.SUB_PRODUCT_ID and S.PRODUCT_ID = P.PRODUCT_ID  and C.STATUS = 'A' AND P.PRODUCT_DEPT in('ALL','STORE') AND M.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID";
			}
			 

			logger.info(sql);

			AllProductList = template.queryForList(sql,new Object[]{});

			for(Map productList :AllProductList){
				objProductDetails = new ProductDetails();
				objProductDetails.setProductName((productList.get("PRODUCT_NAME") == null ? "" : productList.get("PRODUCT_NAME").toString()));
				objProductDetails.setSub_ProductName((productList.get("SUB_PRODUCTNAME") == null ? "" : productList.get("SUB_PRODUCTNAME").toString()));
				objProductDetails.setChild_ProductName((productList.get("CHILD_PRODUCT_NAME") == null ? "" : productList.get("CHILD_PRODUCT_NAME").toString()));
				objProductDetails.setProductType((productList.get("PRODUCT_DEPT") == null ? "" : productList.get("PRODUCT_DEPT").toString()));
				objProductDetails.setMeasurementName(productList.get("MEASUREMENT") == null ? "" : productList.get("MEASUREMENT").toString());
				listAllProductList.add(objProductDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();
			//throw new MyException("");
		}

		return listAllProductList;
	}

	
	public int deletechildproduct(String strSubProdId,String strChildProdId){
		//	int id = 0;
		JdbcTemplate template=null;
		int count = 0;
		try{
			template = new JdbcTemplate(DBConnection.getDbConnection());
			int result=checkDeletedChildProductReceive(strChildProdId);
			if(result==0){
			String sql = "UPDATE child_product SET STATUS = 'I' WHERE CHILD_PRODUCT_ID = ? and SUB_PRODUCT_ID = ?";
			count = template.update(sql,new Object[]{strChildProdId,strSubProdId});
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	

	public int getSeqNo(String strProductType){
		//	int id = 0;
		JdbcTemplate template=null;
		int count = 0;
		String sql = "";
		try{
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if(strProductType.equals("combobox_Product")){
				sql = "select PRODUCT_ID_SEQ.nextval from dual";
			}
			else if(strProductType.equals("delete_SubProd")){
				sql = "select SUBPRODUCT_ID_SEQ.nextval from dual";
			}
			else if(strProductType.equals("childproduct")){
				sql = "select CHILD_PRODUCT_ID_SEQ.nextval from dual";
			}
			else if(strProductType.equals("measurement")){
				sql = "select MEASUREMENT_ID_SEQ.nextval from dual";
			}

			count = template.queryForInt(sql);

		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}

	/*************************************************Check the product already receive or not******************************************************/
	public int checkDeletedProductReceive(String strProductId){
		int count=1;
		JdbcTemplate template;
		String quantity="";
		List<Map<String,Object>> AllProductList = null;
		List<Map<String,Object>> List = null;
		
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			String sql = "select sum(PRODUT_QTY) as QUANTITY from INDENT_AVAILABILITY where PRODUCT_ID=?";
			AllProductList = template.queryForList(sql,new Object[]{strProductId});
			
			for(Map productList :AllProductList){
				quantity=productList.get("QUANTITY") == null ? "" : productList.get("QUANTITY").toString();
				if(quantity==null || quantity.equals("")){

					String query="select sum(PO_QTY) as QUANTITY from SUMADHURA_PO_ENTRY_DETAILS  WHERE PRODUCT_ID=?";
					List = template.queryForList(query,new Object[]{strProductId});
					for(Map list :List){
						quantity=list.get("QUANTITY") == null ? "" : list.get("QUANTITY").toString();
					}
					if(quantity==null || quantity.equals("")){
						String query1="select sum(REQ_QUANTITY) as QUANTITY from SUMADHURA_INDENT_CREATION_DTLS  WHERE PRODUCT_ID=?";
						List = template.queryForList(query1,new Object[]{strProductId});
						for(Map list :List){
							quantity=list.get("QUANTITY") == null ? "" : list.get("QUANTITY").toString();
						}
						if(quantity==null || quantity.equals("")){
						count=0;}
					}

				}
			}
		} catch (NamingException e) {
			e.printStackTrace();
		}

		return count;
	}
	/********************************************************check the previous sub product is receive or not check here**************************/
	public int checkDeletedSubProductReceive(String strSubProductId){
		int count=1;
		JdbcTemplate template;
		String quantity="";
		List<Map<String,Object>> AllProductList = null;
		List<Map<String,Object>> List = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			String sql = "select sum(PRODUT_QTY) as QUANTITY from INDENT_AVAILABILITY where SUB_PRODUCT_ID=? ";
			AllProductList = template.queryForList(sql,new Object[]{strSubProductId});
			
			//quantity = template.queryForObject(sql,String.class);
			for(Map productList :AllProductList){
				quantity=productList.get("QUANTITY") == null ? "" : productList.get("QUANTITY").toString();
				if(quantity==null || quantity.equals("")){

					String query="select sum(PO_QTY) as QUANTITY from SUMADHURA_PO_ENTRY_DETAILS  WHERE SUB_PRODUCT_ID=?";
					List = template.queryForList(query,new Object[]{strSubProductId});
					for(Map list :List){
						quantity=list.get("QUANTITY") == null ? "" : list.get("QUANTITY").toString();
					}
					if(quantity==null || quantity.equals("")){
						String query1="select sum(REQ_QUANTITY) as QUANTITY from SUMADHURA_INDENT_CREATION_DTLS  WHERE SUB_PRODUCT_ID=?";
						List = template.queryForList(query1,new Object[]{strSubProductId});
						for(Map list :List){
							quantity=list.get("QUANTITY") == null ? "" : list.get("QUANTITY").toString();
						}
						
					}

				}
			}
			
			if(quantity==null || quantity.equals("")){
				count=0;

			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return count;
	}
	
	/**************************************************check the previous child product receivced or not start**********************************/
	public int checkDeletedChildProductReceive(String strChildProductId){
		int count=1;
		JdbcTemplate template;
		String quantity="";
		List<Map<String,Object>> AllProductList = null;
		List<Map<String,Object>> List = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			String sql = "select sum(PRODUT_QTY) as QUANTITY from INDENT_AVAILABILITY where CHILD_PRODUCT_ID=?";
			AllProductList = template.queryForList(sql,new Object[]{strChildProductId});
			
			//quantity = template.queryForObject(sql,String.class);
			
			for(Map productList :AllProductList){
				quantity=productList.get("QUANTITY") == null ? "" : productList.get("QUANTITY").toString();
				if(quantity==null || quantity.equals("")){

					String query="select sum(PO_QTY) as QUANTITY from SUMADHURA_PO_ENTRY_DETAILS  WHERE CHILD_PRODUCT_ID=?";
					List = template.queryForList(query,new Object[]{strChildProductId});
					for(Map list :List){
						quantity=list.get("QUANTITY") == null ? "" : list.get("QUANTITY").toString();
					}
					if(quantity==null || quantity.equals("")){
						String query1="select sum(REQ_QUANTITY) as QUANTITY from SUMADHURA_INDENT_CREATION_DTLS  WHERE CHILD_PRODUCT_ID=?";
						List = template.queryForList(query1,new Object[]{strChildProductId});
						for(Map list :List){
							quantity=list.get("QUANTITY") == null ? "" : list.get("QUANTITY").toString();
						}
						
					}

				}
			}
			if(quantity==null || quantity.equals("")){
				count=0;

			}

		} catch (NamingException e) {
			e.printStackTrace();
		}

		return count;
	}
	/****************************************************get the child product data for auto complete start**************************************/
	
	public String ViewChildProductForAutoComplete(String childProduct) {
		JdbcTemplate template =null; 
		//	System.out.println("in dao:"+employeeName);
		StringBuffer sb = null;
		List<Map<String, Object>> dbSiteList = null;		
		//siteName = siteName.replace("$$", "&");
		//siteName = childProduct.toUpperCase();
		sb = new StringBuffer();
		try{
		template=new JdbcTemplate(DBConnection.getDbConnection());
		String contractorInfoQry = "select C.NAME as CHILD_PRODUCT_NAME from CHILD_PRODUCT C where lower(C.NAME) like '%"+childProduct+"%' and rownum <100 ORDER BY C.NAME ASC";
		dbSiteList = template.queryForList(contractorInfoQry, new Object[]{});

		for(Map<String, Object> siteInfo : dbSiteList) {
			sb = sb.append(String.valueOf(siteInfo.get("CHILD_PRODUCT_NAME")));
			sb = sb.append("@@");
		}	
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 2);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Hai "+sb.toString());

		return sb.toString();
	}
	/****************************************************to show the selected chjild product details start********************************************/
	public List<ProductDetails> ViewandGetChildProduct(String childProduct) {
		JdbcTemplate template=null;
		List<Map<String,Object>> AllProductList = null;
		ProductDetails objProductDetails = null;
		List<ProductDetails> listAllProductList =  new ArrayList<ProductDetails>();
		//String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		String sql ="";
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
		//	if(AllProducts.equalsIgnoreCase("true")){
				sql = "select C.NAME as CHILD_PRODUCT_NAME,S.NAME as SUB_PRODUCTNAME ,P.NAME as PRODUCT_NAME,P.PRODUCT_DEPT,M.NAME AS MEASUREMENT from CHILD_PRODUCT c,SUB_PRODUCT S,PRODUCT P,MEASUREMENT M where C.SUB_PRODUCT_ID = S.SUB_PRODUCT_ID and S.PRODUCT_ID = P.PRODUCT_ID  and C.STATUS = 'A' AND M.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID and lower(C.NAME) like lower('%"+childProduct+"%')";
			
			 

			logger.info(sql);

			AllProductList = template.queryForList(sql,new Object[]{});

			for(Map productList :AllProductList){
				objProductDetails = new ProductDetails();
				objProductDetails.setProductName((productList.get("PRODUCT_NAME") == null ? "" : productList.get("PRODUCT_NAME").toString()));
				objProductDetails.setSub_ProductName((productList.get("SUB_PRODUCTNAME") == null ? "" : productList.get("SUB_PRODUCTNAME").toString()));
				objProductDetails.setChild_ProductName((productList.get("CHILD_PRODUCT_NAME") == null ? "" : productList.get("CHILD_PRODUCT_NAME").toString()));
				objProductDetails.setProductType((productList.get("PRODUCT_DEPT") == null ? "" : productList.get("PRODUCT_DEPT").toString()));
				objProductDetails.setMeasurementName(productList.get("MEASUREMENT") == null ? "" : productList.get("MEASUREMENT").toString());
				listAllProductList.add(objProductDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();
			//throw new MyException("");
		}

		return listAllProductList;
	}

	/***************************************************this is only for product ******************************************************************/
	public String ViewProductForAutoComplete(String Product,String product_type) {
		JdbcTemplate template =null; 
		//	System.out.println("in dao:"+employeeName);
		StringBuffer sb = null;
		List<Map<String, Object>> dbSiteList = null;		
		//siteName = siteName.replace("$$", "&");
		//siteName = childProduct.toUpperCase();
		sb = new StringBuffer();
		String contractorInfoQry ="";
		try{
		template=new JdbcTemplate(DBConnection.getDbConnection());
		if(product_type.equalsIgnoreCase("STORE")){
			 contractorInfoQry = "select C.NAME as PRODUCT_NAME from PRODUCT C where PRODUCT_DEPT IN('STORE','ALL') AND lower(C.NAME) like lower('%"+Product+"%')  ORDER BY C.NAME ASC";
		}else if(product_type.equalsIgnoreCase("MARKETING")){
			contractorInfoQry = "select C.NAME as PRODUCT_NAME from PRODUCT C where PRODUCT_DEPT IN('MARKETING','ALL') AND lower(C.NAME) like lower('%"+Product+"%')  ORDER BY C.NAME ASC";
		}else{
			contractorInfoQry = "select C.NAME as PRODUCT_NAME from PRODUCT C where  lower(C.NAME) like lower('%"+Product+"%')  ORDER BY C.NAME ASC";
		}
		
		dbSiteList = template.queryForList(contractorInfoQry, new Object[]{});

		for(Map<String, Object> siteInfo : dbSiteList) {
			sb = sb.append(String.valueOf(siteInfo.get("PRODUCT_NAME")));
			sb = sb.append("@@");
		}	
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 2);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Hai "+sb.toString());

		return sb.toString();
	}
	/************************************************auto complete start**************************************************************/
	public String getSubProductsforAutoComplete(String product,String subProduct) {
		JdbcTemplate template =null; 
		//	System.out.println("in dao:"+employeeName);
		StringBuffer sb = null;
		List<Map<String, Object>> dbSiteList = null;		
		//siteName = siteName.replace("$$", "&");
		//siteName = childProduct.toUpperCase();
		sb = new StringBuffer();
		try{
		template=new JdbcTemplate(DBConnection.getDbConnection());
		String contractorInfoQry = "select S.NAME as SUB_PRODUCT_NAME from SUB_PRODUCT S where lower(S.NAME) like lower('%"+subProduct+"%') and S.PRODUCT_ID='"+product+"' and rownum <8 ORDER BY S.NAME ASC";
		dbSiteList = template.queryForList(contractorInfoQry, new Object[]{});

		for(Map<String, Object> siteInfo : dbSiteList) {
			sb = sb.append(String.valueOf(siteInfo.get("SUB_PRODUCT_NAME")));
			sb = sb.append("@@");
		}	
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 2);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Hai "+sb.toString());

		return sb.toString();
	}
	/****************************************for measurement auto completetion start*******************************************************/
	public String getmeasurementforAutoComplete(String childProduct,String measurement) {
		JdbcTemplate template =null; 
		//	System.out.println("in dao:"+employeeName);
		StringBuffer sb = null;
		List<Map<String, Object>> dbSiteList = null;
		List<Map<String, Object>> dbchildList = null;
		//siteName = siteName.replace("$$", "&");
		//siteName = childProduct.toUpperCase();
		sb = new StringBuffer();
		String chilProductId="";
		try{
		template=new JdbcTemplate(DBConnection.getDbConnection());
		
		/*String childData = "select CHILD_PRODUCT_ID from CHILD_PRODUCT where  UPPER(NAME) =UPPER('"+childProduct+"') ORDER BY NAME ASC";
		dbchildList = template.queryForList(childData, new Object[]{});
		for(Map<String, Object> childInfo : dbchildList) {
			chilProductId=((childInfo.get("CHILD_PRODUCT_ID") == null ? "" : childInfo.get("CHILD_PRODUCT_ID").toString()));
		}*/
		
		String contractorInfoQry = "select M.NAME as MEASUREMENT_NAME from MEASUREMENT M where lower(M.NAME) like lower('%"+measurement+"%')  and rownum <2 ORDER BY M.NAME ASC";
		dbSiteList = template.queryForList(contractorInfoQry, new Object[]{});

		for(Map<String, Object> siteInfo : dbSiteList) {
			sb = sb.append(String.valueOf(siteInfo.get("MEASUREMENT_NAME")));
			sb = sb.append("@@");
		}	
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 2);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Hai "+sb.toString());

		return sb.toString();
	}
/*	***************************************************only for single product it will written it***************************************************/
	/****************************************for measurement auto completetion start*******************************************************/
	public String getProductforAutoComplete(String Product,String siteId) {
		JdbcTemplate template =null; 
		//	System.out.println("in dao:"+employeeName);
		StringBuffer sb = null;
		List<Map<String, Object>> dbSiteList = null;
		
		sb = new StringBuffer();
		String contractorInfoQry = "";
		
		String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		try{
		template=new JdbcTemplate(DBConnection.getDbConnection());
		if(siteId.equals(marketing_dept_id)){
			 contractorInfoQry = "select P.NAME as PRODUCT_NAME from PRODUCT P where lower(P.NAME) like lower('%"+Product+"%') and PRODUCT_DEPT in('MARKETING','ALL') and rownum <5 ORDER BY P.NAME ASC";
		}else{
			 contractorInfoQry = "select P.NAME as PRODUCT_NAME from PRODUCT P where lower(P.NAME) like lower('%"+Product+"%') and PRODUCT_DEPT in('STORE','ALL') and rownum <5 ORDER BY P.NAME ASC";
		}
		
		dbSiteList = template.queryForList(contractorInfoQry, new Object[]{});

		for(Map<String, Object> siteInfo : dbSiteList) {
			sb = sb.append(String.valueOf(siteInfo.get("PRODUCT_NAME")));
			sb = sb.append("@@");
		}	
		if (sb.length() > 0) {
			sb.setLength(sb.length() - 2);
		}
		}catch(Exception e){
			e.printStackTrace();
		}
		//System.out.println("Hai "+sb.toString());

		return sb.toString();
	}
	
}
