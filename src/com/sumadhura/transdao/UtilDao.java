package com.sumadhura.transdao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.sumadhura.bean.ContractorBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.bean.userDetails;
import com.sumadhura.service.WriteTrHistory;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.UIProperties;
import com.sumadhura.bean.TransportorBean;

@Repository
public class UtilDao extends UIProperties {
	@Autowired
	PlatformTransactionManager transactionManager;
	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	public List<Map<String, Object>> getAllSites() {
		JdbcTemplate template = null;
		List<Map<String, Object>> totalProductList = null;
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select SITE_ID, SITE_NAME  from  SITE where STATUS = 'ACTIVE'";
			System.out.println(sql);

			totalProductList = template.queryForList(sql, new Object[] {});

			/*
			 * for(Map productList : TotalProductList){ objProductDetails = new
			 * ProductDetails();
			 * 
			 * objProductDetails.setProduct_Name((productList.get("name") ==
			 * null ? "" : productList.get("name").toString()));
			 * listProductList.add(objProductDetails); }
			 */

		} catch (Exception e) {
			e.printStackTrace();

		}

		return totalProductList;
	}

	public List<Map<String, Object>> getAllSitesBasedOnCreationDate(String strDate) {
		JdbcTemplate template = null;
		List<Map<String, Object>> totalProductList = null;
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select SITE_ID, SITE_NAME  from  SITE where TRUNC( CREATED_DATE)  <= TO_DATE(?,'dd-MM-yy') and STATUS = 'ACTIVE' order by  SITE_NAME asc ";
			System.out.println(sql);

			totalProductList = template.queryForList(sql, new Object[] {strDate});

			/*
			 * for(Map productList : TotalProductList){ objProductDetails = new
			 * ProductDetails();
			 * 
			 * objProductDetails.setProduct_Name((productList.get("name") ==
			 * null ? "" : productList.get("name").toString()));
			 * listProductList.add(objProductDetails); }
			 */

		} catch (Exception e) {
			e.printStackTrace();

		}

		return totalProductList;
	}

	//******************************************

	public List<Map<String,Object>> getTotalProducts(String siteId) {
		JdbcTemplate template=null;
		List<Map<String,Object>> totalProductList = null;
		String sql ="";
		String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			if(siteId.equals(marketing_dept_id)){
				sql = "SELECT PRODUCT_ID, NAME FROM PRODUCT WHERE STATUS = 'A' AND PRODUCT_DEPT in ('ALL','MARKETING')";
			}else{
				sql = "SELECT PRODUCT_ID, NAME FROM PRODUCT WHERE STATUS = 'A' AND PRODUCT_DEPT in ('ALL','STORE')";
			}

			 

			System.out.println(sql);

			totalProductList = template.queryForList(sql,new Object[]{});


		} catch (Exception e) {
			e.printStackTrace();

		}

		return totalProductList;
	}


	public List<ProductDetails> getAllSubProducts(String strProductId) {
		JdbcTemplate template=null;
		List<Map<String,Object>> AllProductList = null;
		ProductDetails objProductDetails = null;
		List<ProductDetails> listAllProductList =  new ArrayList<ProductDetails>();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select SUB_PRODUCT_ID,NAME from SUB_PRODUCT where STATUS = ? and PRODUCT_ID = ?";

			System.out.println(sql);

			AllProductList = template.queryForList(sql,new Object[]{"A",strProductId});
			objProductDetails = new ProductDetails();
			objProductDetails.setSub_ProductName("");
			objProductDetails.setSub_ProductId("");
			listAllProductList.add(objProductDetails);
			for(Map productList :AllProductList){
				objProductDetails = new ProductDetails();
				objProductDetails.setSub_ProductName((productList.get("NAME") == null ? "" : productList.get("NAME").toString()));
				objProductDetails.setSub_ProductId((productList.get("SUB_PRODUCT_ID") == null ? "" : productList.get("SUB_PRODUCT_ID").toString()));
				listAllProductList.add(objProductDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return listAllProductList;
	}

	public List<ProductDetails> getAllChildProducts(String strSubProductId) {
		JdbcTemplate template=null;
		List<Map<String,Object>> AllProductList = null;
		ProductDetails objProductDetails = null;
		List<ProductDetails> listAllProductList =  new ArrayList<ProductDetails>();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select CHILD_PRODUCT_ID,NAME from CHILD_PRODUCT where SUB_PRODUCT_ID = ? and STATUS = ?";

			System.out.println(sql);

			AllProductList = template.queryForList(sql,new Object[]{strSubProductId,"A"});

			objProductDetails = new ProductDetails();
			objProductDetails.setChild_ProductName("");
			objProductDetails.setChild_ProductId("");
			listAllProductList.add(objProductDetails);
			for(Map productList :AllProductList){
				objProductDetails = new ProductDetails();
				objProductDetails.setChild_ProductName((productList.get("NAME") == null ? "" : productList.get("NAME").toString()));
				objProductDetails.setChild_ProductId((productList.get("CHILD_PRODUCT_ID") == null ? "" : productList.get("CHILD_PRODUCT_ID").toString()));
				listAllProductList.add(objProductDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return listAllProductList;
	}
	
	public List<ProductDetails> getAllChildProductGroups() {
		JdbcTemplate template=null;
		List<Map<String,Object>> AllProductList = null;
		ProductDetails objProductDetails = null;
		List<ProductDetails> listAllProductList =  new ArrayList<ProductDetails>();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select MATERIAL_GROUP_ID,MATERIAL_GROUP_NAME,MATERIAL_GROUP_MEASUREMENT_ID,MATERIAL_GROUP_MST_NAME from PRODUCT_GROUP_MASTER where STATUS = ?";

			System.out.println(sql);

			AllProductList = template.queryForList(sql,new Object[]{"A"});

			objProductDetails = new ProductDetails();
			objProductDetails.setChild_ProductName("");
			objProductDetails.setChild_ProductId("");
			objProductDetails.setMeasurementName("");
			objProductDetails.setMeasurementId("");
			listAllProductList.add(objProductDetails);
			objProductDetails = new ProductDetails();
			objProductDetails.setChild_ProductName("NA");
			objProductDetails.setChild_ProductId("NA");
			objProductDetails.setMeasurementName("NA");
			objProductDetails.setMeasurementId("NA");
			listAllProductList.add(objProductDetails);
			for(Map productList :AllProductList){
				objProductDetails = new ProductDetails();
				objProductDetails.setChild_ProductName((productList.get("MATERIAL_GROUP_NAME") == null ? "" : productList.get("MATERIAL_GROUP_NAME").toString()));
				objProductDetails.setChild_ProductId((productList.get("MATERIAL_GROUP_ID") == null ? "" : productList.get("MATERIAL_GROUP_ID").toString()));
				objProductDetails.setMeasurementName((productList.get("MATERIAL_GROUP_MST_NAME") == null ? "" : productList.get("MATERIAL_GROUP_MST_NAME").toString()));
				objProductDetails.setMeasurementId((productList.get("MATERIAL_GROUP_MEASUREMENT_ID") == null ? "" : productList.get("MATERIAL_GROUP_MEASUREMENT_ID").toString()));
				listAllProductList.add(objProductDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return listAllProductList;
	}




	public List<String> getProductsBySearch(String product,String siteId,String AllProducts) {
		JdbcTemplate template=null;
		List<Map<String,Object>> TotalProductList = null;
		//ProductDetails objProductDetails = null;
		String strName = "";
		String sql ="";
		String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		List<String> listProductList =  new ArrayList<String>();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			if(siteId.equals(marketing_dept_id)){
				sql = "select name from product where upper(NAME) like upper(?) and STATUS = 'A'";	
			}
			
			else if(siteId.equals(marketing_dept_id)){
				sql = "select name from product where upper(NAME) like upper(?) and STATUS = 'A' and PRODUCT_DEPT='MARKETING'";	
			}else{

			 sql = "select name from product where upper(NAME) like upper(?) and STATUS = 'A' and PRODUCT_DEPT='STORE'";
			}
			System.out.println(sql);

			TotalProductList = template.queryForList(sql,new Object[]{product+"%"});

			System.out.println("UtilsDao class TotalProductList  "+TotalProductList);
			for(Map productList : TotalProductList){
				//	objProductDetails = new ProductDetails();

				strName = (productList.get("name") == null ? "" : productList.get("name").toString());
				listProductList.add(strName);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return listProductList;
	}

	public List<String> getSubProductsBySearch(String subproduct) {
		JdbcTemplate template=null;
		List<Map<String,Object>> TotalSubProductList = null;
		//ProductDetails objProductDetails = null;
		String strName = "";
		List<String> listSubProductList =  new ArrayList<String>();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select name from sub_product where upper(NAME) like upper(?) and STATUS = 'A'";

			System.out.println(sql);

			TotalSubProductList = template.queryForList(sql,new Object[]{subproduct+"%"});

			for(Map productList : TotalSubProductList){
				//	objProductDetails = new ProductDetails();
				strName = (productList.get("name") == null ? "" : productList.get("name").toString());
				//objProductDetails.setProduct_Name((productList.get("product_name") == null ? "" : productList.get("product_name").toString()));
				listSubProductList.add(strName);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return listSubProductList;
	}
	public List<String> getchildProductsBySearch(String strSubProductId,String strSearchingChildProdName) {
		JdbcTemplate template=null;
		List<Map<String,Object>> AllProductList = null;
		//	ProductDetails objProductDetails = null;
		String strName = "";
		List<String> listAllProductList =  new ArrayList<String>();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select NAME from CHILD_PRODUCT where upper(NAME) like upper(?) and STATUS = 'A' and SUB_PRODUCT_ID = ?";

			System.out.println(sql);

			AllProductList = template.queryForList(sql,new Object[]{strSearchingChildProdName+"%",strSubProductId});

			for(Map productList :AllProductList){
				//	objProductDetails = new ProductDetails();
				strName = (productList.get("NAME") == null ? "" : productList.get("NAME").toString());
				listAllProductList.add(strName);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listAllProductList;
	}

	public List<String> getVendorsBySearch(String vendor) {
		JdbcTemplate template=null;
		List<Map<String,Object>> AllVendorsList = null;
		//VendorDetails objVendorDetails = null;
		String strName = "";
		List<String> listAllVendorsList =  new ArrayList<String>();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select VENDOR_NAME from vendor_details where upper(VENDOR_NAME) like upper(?) and STATUS = 'A'";

			System.out.println(sql);

			AllVendorsList = template.queryForList(sql,new Object[]{vendor+"%"});

			for(Map vendorList :AllVendorsList){
				//objVendorDetails = new VendorDetails();
				strName = (vendorList.get("vendor_name") == null ? "" : vendorList.get("vendor_name").toString());

				listAllVendorsList.add(strName);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return listAllVendorsList;
	}


	public userDetails getUserDetails(String user_id) {
		JdbcTemplate template=null;
		List<Map<String,Object>> userDetailsList = null;
		//VendorDetails objVendorDetails = null;
		String strName = "";
		userDetails objuserDetails = null;
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select EMP_NAME,MOBILE_NUMBER,EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = ?";

			System.out.println(sql);

			userDetailsList = template.queryForList(sql,new Object[]{user_id});

			for(Map mapuserDetailsList :userDetailsList){
				objuserDetails = new userDetails();
				objuserDetails.setUserName(mapuserDetailsList.get("EMP_NAME") == null ? "" : mapuserDetailsList.get("EMP_NAME").toString());
				objuserDetails.setMobileNo(mapuserDetailsList.get("MOBILE_NUMBER") == null ? "" : mapuserDetailsList.get("MOBILE_NUMBER").toString());
				objuserDetails.setEmailId(mapuserDetailsList.get("EMP_EMAIL") == null ? "" : mapuserDetailsList.get("EMP_EMAIL").toString());


			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return objuserDetails;
	}


	public String []  getEmployeesEmailId(String siteId) {
		JdbcTemplate template=null;
		List<Map<String,Object>> userDetailsList = null;
		//VendorDetails objVendorDetails = null;
		String strName = "";
		String[] emailArr = null;
		String emailId = "";
		ArrayList<String> objArrayList = new ArrayList<String>();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select EMP_EMAIL from VENDOR_DETAILS WHERE VENDOR_ID=?";

			System.out.println(sql);

			userDetailsList = template.queryForList(sql,new Object[]{siteId});

			for(Map mapuserDetailsList :userDetailsList){

				emailId = mapuserDetailsList.get("EMP_EMAIL") == null ? "-" : mapuserDetailsList.get("EMP_EMAIL").toString();

				if(!emailId.equals("")){

					objArrayList.add(emailId);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		if(objArrayList.size() > 0){
			String[] arr=new String[objArrayList.size()];
			emailArr =  objArrayList.toArray(arr);
		}else{
			emailArr = null;
		}

		return emailArr;
	}

	public String doContractorRegistration(ContractorBean bean, String siteId, String user_id,
			HttpServletRequest request, HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in contractor registration, ");
		String response = "";
		try {
			//db record changed make the list empty to load again data 
			list = new ArrayList<ContractorBean>();
			int contratorSeqNum = jdbcTemplate.queryForInt("select SUMADHURA_CONTRACTOR_SEQ.NEXTVAL from dual");

			String query = "insert into SUMADHURA_CONTRACTOR(CONTRACTOR_ID,FIRST_NAME,LAST_NAME,ADDRESS,PAN_NUMBER,MOBILE_NUMBER,ALTERNATE_MOBILE_NUMBER,BANK_ACC_NUMBER,BANK_NAME,IFSC_CODE,STATUS,GSTIN,EMAIL,LANDLINE_NUMBER,ACCOUNT_TYPE,BRANCH_NAME,STATE_NAME,CITY_NAME)"
					+ "  values(?,?,?,?,?,?,?,?,?,?,'A',?,?,?,?,?,?,?)";
			String str = "SIPL/CON/" + contratorSeqNum;
			System.out.println("Contractor Id "+str);
			bean.setContractor_id(str);
			Object[] queryParams = { str,bean.getFirst_name(),bean.getLast_name(), 
					bean.getAddress(), bean.getPan_number(),
					bean.getMobile_number(),bean.getAlternate_mobile_number(), 
					bean.getBank_acc_number(), bean.getBank_name(), 
					bean.getIfsc_code(),bean.getGSTIN(),
					bean.getEmail(),bean.getLandline_number(),bean.getAccount_type(),bean.getBranch_name(),bean.getState_name(),bean.getCity_name() };
			int result = jdbcTemplate.update(query, queryParams);

			if (result != 0) {
				response = "Registration done Successfully.";
			} else {
				response = "Registration Failed.";
			}
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");

		} catch (Exception e) {
			transactionManager.rollback(status);
			// System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			response = "Registration Failed.";
			//String s=null;
			//s.trim();
			e.printStackTrace();
				
		}

		return response;
	}

	public boolean validatePanNumber(String str, String condition, String contractorId) {

		int count =0;
		if(condition.equals("GSTIN")){
			//checking GSTIN number is already exists or not if exists return true
			
			if(contractorId.length()==0){
				//request from  Contractor registration page
				count= jdbcTemplate.queryForInt("select count(*) from SUMADHURA_CONTRACTOR where GSTIN=? and STATUS!='I'",	str);
			}else{
				//request from Contractor update page checking the GSTIN numnber is already exists or not
				count= jdbcTemplate.queryForInt("select count(*) from SUMADHURA_CONTRACTOR where GSTIN=? and CONTRACTOR_ID!=?  and STATUS!='I'",	str,contractorId);
			}
		}else if(condition.equals("PAN")){
			//checking pan number is already exists or not if exists return true
			//
			if(contractorId.length()==0){
				//request from Contractor registration page
			count= jdbcTemplate.queryForInt("select count(*) from SUMADHURA_CONTRACTOR where PAN_NUMBER=?  and STATUS!='I'",	str);
			}else{
				//request from Contractor update page checking the PAN numnber is already exists or not
				count= jdbcTemplate.queryForInt("select count(*) from SUMADHURA_CONTRACTOR where PAN_NUMBER=? and CONTRACTOR_ID!=?  and STATUS!='I'",	str,contractorId);
			}
		}
		System.out.println(count);
		if (count > 0) {
			return false;
		} else {
			return true;
		}

	}
//load the data only one time if any modification happens load the data again from DB
	List<ContractorBean> list = new ArrayList<ContractorBean>();
//this class is for extractig the data
	class ExtractContractorData implements ResultSetExtractor<List<ContractorBean>>{
		
		@Override
		public List<ContractorBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
			List<ContractorBean> list = new ArrayList<ContractorBean>();
			while (rs.next()) {
				ContractorBean bean = new ContractorBean();
				bean.setContractor_id(rs.getString("CONTRACTOR_ID") == null ? "" : rs.getString("CONTRACTOR_ID"));
				bean.setContractor_name(rs.getString("CONTRACTOR_NAME") == null ? "" : rs.getString("CONTRACTOR_NAME"));
				bean.setFirst_name(rs.getString("FIRST_NAME")==null?"":rs.getString("FIRST_NAME"));
				bean.setLast_name(rs.getString("LAST_NAME")==null?"":rs.getString("LAST_NAME"));
				bean.setAddress(rs.getString("ADDRESS") == null ? "" : rs.getString("ADDRESS"));
				bean.setPan_number(rs.getString("PAN_NUMBER") == null ? "" : rs.getString("PAN_NUMBER"));
				bean.setGSTIN(rs.getString("GSTIN")==null?"":rs.getString("GSTIN"));
				bean.setEmail(rs.getString("EMAIL")==null?"":rs.getString("EMAIL"));
				bean.setMobile_number(rs.getString("MOBILE_NUMBER") == null ? "" : rs.getString("MOBILE_NUMBER"));
				bean.setAlternate_mobile_number(rs.getString("ALTERNATE_MOBILE_NUMBER")==null?"":rs.getString("ALTERNATE_MOBILE_NUMBER"));
				bean.setBank_acc_number(rs.getString("BANK_ACC_NUMBER") == null ? "" : rs.getString("BANK_ACC_NUMBER"));
				bean.setBank_name(rs.getString("BANK_NAME") == null ? "" : rs.getString("BANK_NAME"));
				bean.setIfsc_code(rs.getString("IFSC_CODE") == null ? "" : rs.getString("IFSC_CODE"));
	
				bean.setLandline_number(rs.getString("LANDLINE_NUMBER") == null ? "" : rs.getString("LANDLINE_NUMBER"));
				bean.setAccount_type(rs.getString("ACCOUNT_TYPE") == null ? "" : rs.getString("ACCOUNT_TYPE"));
				bean.setBranch_name(rs.getString("BRANCH_NAME") == null ? "" : rs.getString("BRANCH_NAME"));
				bean.setState_name(rs.getString("STATE_NAME") == null ? "" : rs.getString("STATE_NAME"));
				bean.setCity_name(rs.getString("CITY_NAME") == null ? "" : rs.getString("CITY_NAME"));
				list.add(bean);
			}
			return list;
		}
	}
	
	
	public List<ContractorBean> getAllContratorDetails(String contractorId) throws NamingException {
		String query = "";

		if (contractorId.equals("")) {
			//select all contractor
			query = "select CONTRACTOR_ID,FIRST_NAME,LAST_NAME,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,ADDRESS,PAN_NUMBER,MOBILE_NUMBER,ALTERNATE_MOBILE_NUMBER,BANK_ACC_NUMBER,BANK_NAME,IFSC_CODE,GSTIN,EMAIL,LANDLINE_NUMBER,ACCOUNT_TYPE,BRANCH_NAME,STATE_NAME,CITY_NAME from SUMADHURA_CONTRACTOR where status='A'";
		} else {
			// select perticular contractor
			query = "select CONTRACTOR_ID,FIRST_NAME,LAST_NAME,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,ADDRESS,PAN_NUMBER,MOBILE_NUMBER,ALTERNATE_MOBILE_NUMBER,BANK_ACC_NUMBER,BANK_NAME,IFSC_CODE,GSTIN,EMAIL,LANDLINE_NUMBER,ACCOUNT_TYPE,BRANCH_NAME,STATE_NAME,CITY_NAME from SUMADHURA_CONTRACTOR where CONTRACTOR_ID=? and status='A'";
			Object[] obj={contractorId};
			List<ContractorBean> listById = jdbcTemplate.query(query, obj,new ExtractContractorData());
			return listById;
		}

		if (list != null)
			synchronized (this) {
				if (list.size() > 0) {
					System.out.println("Contractor List is not empty "+list.size());
					 return list;
				}	
			}
			
		
		list = jdbcTemplate.query(query, new ExtractContractorData());
		return list;
	}

	public String updateContractorDetail(ContractorBean bean, String siteId, String user_id, HttpServletRequest request,
			HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in contractor registration, ");
		String response = "";
		try { 
			if(list.size()>0){
				System.out.println("contractor list Object  reset");
				//db record changed make the list empty to load again data 
				list = new ArrayList<ContractorBean>();
			}
			String queryForUpdate="UPDATE  SUMADHURA_CONTRACTOR SET FIRST_NAME=?,LAST_NAME=?,ADDRESS=?,PAN_NUMBER=?,"
					+ "MOBILE_NUMBER=?,ALTERNATE_MOBILE_NUMBER=?,BANK_ACC_NUMBER=?,"
					+ "BANK_NAME=?,IFSC_CODE=?,GSTIN=?,EMAIL=?,LANDLINE_NUMBER=?,ACCOUNT_TYPE=?,BRANCH_NAME=?,STATE_NAME=?,CITY_NAME=? WHERE CONTRACTOR_ID=?";
			Object[] queryParams = {bean.getFirst_name().trim(),bean.getLast_name().trim(), bean.getAddress(), bean.getPan_number(),
					bean.getMobile_number(),bean.getAlternate_mobile_number(), bean.getBank_acc_number(),
					bean.getBank_name(), bean.getIfsc_code(),bean.getGSTIN(),bean.getEmail(),bean.getLandline_number(),bean.getAccount_type(),bean.getBranch_name(),bean.getState_name(),bean.getCity_name(),bean.getContractor_id()};
			
			int result=jdbcTemplate.update(queryForUpdate,queryParams);
			
			if (result != 0) {
				response = "Contractor Detail Updated Successfully.";
			} else {
				response = "Contractor Update Failed.";
			}
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
		} catch (Exception e) {
		transactionManager.rollback(status);
		// System.out.println("Indent creation Failed");
		WriteTrHistory.write("Tr_Completed");
		response = "Contractor Update Failed.";
		e.printStackTrace();
	}
		return response;
	}

	public String deleteContractorDeteail(ContractorBean bean, String siteId, String user_id,
			HttpServletRequest request, HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in contractor registration, ");
		String response = "";
		try { 
			//db record changed make the list empty to load again data 
			list=new ArrayList<ContractorBean>();
			String query="update SUMADHURA_CONTRACTOR set status='I' where CONTRACTOR_ID=?";
			int result=jdbcTemplate.update(query,bean.getContractor_id());
			if(result!=0){
				response="Contractor deleted successfully.";
			}else{
				response="Failed to delete contractor.";
			}
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
		} catch (Exception e) {
			transactionManager.rollback(status);
			// System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			response="Failed to delete contractor.";
			e.printStackTrace();
		}
		return response;
	}

	public boolean validateContractorName(String first_name, String last_name, String condition, String contractor_id) {
		int count=0;
			//checking CONTRACTOR_NAME is already exists or not if exists return true
		if(condition.equals("1")){
			//if condition is 1 means reauest from contractor registration page 
			count= jdbcTemplate.queryForInt("select count(*) from SUMADHURA_CONTRACTOR where upper(FIRST_NAME)=upper(?) and upper(LAST_NAME)=upper(?) and STATUS!='I' ",first_name,last_name);	
		}	else{
			//if condition is 2 means reauest from contractor update page
			count= jdbcTemplate.queryForInt("select count(*) from SUMADHURA_CONTRACTOR where upper(FIRST_NAME)=upper(?) and upper(LAST_NAME)=upper(?) and STATUS!='I' and CONTRACTOR_ID!=?  ",first_name,last_name,contractor_id);
		}
		System.out.println(count);
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}
	
	/******************************************************this is used in the time of all products **********************************************/
	public List<Map<String,Object>> getTotalProductsList(String siteId) {
		JdbcTemplate template=null;
		List<Map<String,Object>> totalProductList = null;
		String sql ="";

		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			sql = "SELECT PRODUCT_ID, NAME,PRODUCT_DEPT FROM PRODUCT WHERE STATUS = 'A'";
			System.out.println(sql);
			totalProductList = template.queryForList(sql,new Object[]{});


		} catch (Exception e) {
			e.printStackTrace();

		}

		return totalProductList;
	}
	/********************************************************select the differen product so written this one*************************************/
	public List<Map<String,Object>> getSiteProducts(String productType) {
		JdbcTemplate template=null;
		List<Map<String,Object>> totalProductList = null;
		String sql ="";
		//String marketing_dept_id = validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			if(productType.equalsIgnoreCase("ALL")){
				sql = "SELECT PRODUCT_ID, NAME FROM PRODUCT WHERE STATUS = 'A' AND PRODUCT_DEPT='ALL'";
			}
			else if(productType.equals("MARKETING")){
				sql = "SELECT PRODUCT_ID, NAME FROM PRODUCT WHERE STATUS = 'A' AND PRODUCT_DEPT='MARKETING'";
			}else{
				sql = "SELECT PRODUCT_ID, NAME FROM PRODUCT WHERE STATUS = 'A' AND PRODUCT_DEPT='STORE'";
			}
			System.out.println(sql);
			totalProductList = template.queryForList(sql,new Object[]{});
			} catch (Exception e) {
			e.printStackTrace();

		}

		return totalProductList;
	}
	/******************************************** TRANSPORTOR DETAILS DELETED START*******************************************************/
	public String deleteTransportorDeteails(TransportorBean bean, String siteId, String user_id,
			HttpServletRequest request, HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in transportor registration, ");
		String response = "";
		try { 
			//db record changed make the list empty to load again data 
			listData=new ArrayList<TransportorBean>();
			String query="update TRANSPORTOR_DETAILS set status='I' where TRANSPORTOR_ID=?";
			int result=jdbcTemplate.update(query,bean.getContractor_id());
			if(result!=0){
				response="Transporoter deleted successfully.";
			}else{
				response="Failed to delete Transporoter.";
			}
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
		} catch (Exception e) {
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			response="Failed to delete Transporoter.";
			e.printStackTrace();
		}
		return response;
	}
	
	//load the data only one time if any modification happens load the data again from DB
	List<TransportorBean> listData = new ArrayList<TransportorBean>();
	//this class is for extractig the data
	class ExtractTransporotorData implements ResultSetExtractor<List<TransportorBean>>{
		
		@Override
		public List<TransportorBean> extractData(ResultSet rs) throws SQLException, DataAccessException {
			List<TransportorBean> listData = new ArrayList<TransportorBean>();
			while (rs.next()) {
				TransportorBean bean = new TransportorBean();
				bean.setContractor_id(rs.getString("TRANSPORTOR_ID") == null ? "" : rs.getString("TRANSPORTOR_ID"));
				bean.setContractor_name(rs.getString("CONTRACTOR_NAME") == null ? "" : rs.getString("CONTRACTOR_NAME"));
				bean.setFirst_name(rs.getString("FIRST_NAME")==null?"":rs.getString("FIRST_NAME"));
				bean.setLast_name(rs.getString("LAST_NAME")==null?"":rs.getString("LAST_NAME"));
				bean.setAddress(rs.getString("ADDRESS") == null ? "" : rs.getString("ADDRESS"));
				bean.setPan_number(rs.getString("PAN_NUMBER") == null ? "" : rs.getString("PAN_NUMBER"));
				bean.setGSTIN(rs.getString("GSTIN")==null?"":rs.getString("GSTIN"));
				bean.setEmail(rs.getString("EMAIL")==null?"":rs.getString("EMAIL"));
				bean.setMobile_number(rs.getString("MOBILE_NUMBER") == null ? "" : rs.getString("MOBILE_NUMBER"));
				bean.setAlternate_mobile_number(rs.getString("ALTERNATE_MOBILE_NUMBER")==null?"":rs.getString("ALTERNATE_MOBILE_NUMBER"));
				bean.setBank_acc_number(rs.getString("BANK_ACC_NUMBER") == null ? "" : rs.getString("BANK_ACC_NUMBER"));
				bean.setBank_name(rs.getString("BANK_NAME") == null ? "" : rs.getString("BANK_NAME"));
				bean.setIfsc_code(rs.getString("IFSC_CODE") == null ? "" : rs.getString("IFSC_CODE"));
	
				bean.setLandline_number(rs.getString("LANDLINE_NUMBER") == null ? "" : rs.getString("LANDLINE_NUMBER"));
				bean.setAccount_type(rs.getString("ACCOUNT_TYPE") == null ? "" : rs.getString("ACCOUNT_TYPE"));
				bean.setBranch_name(rs.getString("BRANCH_NAME") == null ? "" : rs.getString("BRANCH_NAME"));
				bean.setState_name(rs.getString("STATE_NAME") == null ? "" : rs.getString("STATE_NAME"));
				bean.setCity_name(rs.getString("CITY_NAME") == null ? "" : rs.getString("CITY_NAME"));
				listData.add(bean);
			}
			return listData;
		}
	}
	
	// getting the data and set to the list for transportors start ************************************
	public List<TransportorBean> getAllTransportorDetails(String trasportorId) throws NamingException {
		String query = "";

		if (trasportorId.equals("")) {
			//select all transporotors
			query = "select TRANSPORTOR_ID,FIRST_NAME,LAST_NAME,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,ADDRESS,PAN_NUMBER,MOBILE_NUMBER,ALTERNATE_MOBILE_NUMBER,BANK_ACC_NUMBER,BANK_NAME,IFSC_CODE,GSTIN,EMAIL,LANDLINE_NUMBER,ACCOUNT_TYPE,BRANCH_NAME,STATE_NAME,CITY_NAME from TRANSPORTOR_DETAILS where status='A'";
		} else {
			// select perticular transporotor
			query = "select TRANSPORTOR_ID,FIRST_NAME,LAST_NAME,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,ADDRESS,PAN_NUMBER,MOBILE_NUMBER,ALTERNATE_MOBILE_NUMBER,BANK_ACC_NUMBER,BANK_NAME,IFSC_CODE,GSTIN,EMAIL,LANDLINE_NUMBER,ACCOUNT_TYPE,BRANCH_NAME,STATE_NAME,CITY_NAME from TRANSPORTOR_DETAILS where TRANSPORTOR_ID=? and status='A'";
			Object[] obj={trasportorId};
			List<TransportorBean> listById = jdbcTemplate.query(query, obj,new ExtractTransporotorData());
			logger.debug("***********************Query result for perticular Data***************"+listById);
			return listById;
		}

		if (listData != null)
			synchronized (this) {
				if (listData.size() > 0) {
					System.out.println("Transporter List is not empty "+list.size());
					 return listData;
				}	
			}
		
		listData = jdbcTemplate.query(query, new ExtractTransporotorData());
		logger.debug("***********************Query result for Entire Data***************"+listData);	
		return listData;
	}
	
	/***************************************** update the transportor details  start *****************************************************/
	public String updateTransportorDetails(TransportorBean bean, String siteId, String user_id, HttpServletRequest request,
			HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in transportor update, ");
		String response = "";
		try { 
			if(listData.size()>0){
				logger.info("***********************transportor list Object  reset***************");
				//System.out.println("contractor list Object  reset");
				//db record changed make the list empty to load again data 
				listData = new ArrayList<TransportorBean>();
			}
			String queryForUpdate="UPDATE  TRANSPORTOR_DETAILS SET FIRST_NAME=?,LAST_NAME=?,ADDRESS=?,PAN_NUMBER=?,"
					+ "MOBILE_NUMBER=?,ALTERNATE_MOBILE_NUMBER=?,BANK_ACC_NUMBER=?,"
					+ "BANK_NAME=?,IFSC_CODE=?,GSTIN=?,EMAIL=?,LANDLINE_NUMBER=?,ACCOUNT_TYPE=?,BRANCH_NAME=?,STATE_NAME=?,CITY_NAME=? WHERE TRANSPORTOR_ID=?";
			Object[] queryParams = {bean.getFirst_name().trim(),bean.getLast_name().trim(), bean.getAddress(), bean.getPan_number(),
					bean.getMobile_number(),bean.getAlternate_mobile_number(), bean.getBank_acc_number(),
					bean.getBank_name(), bean.getIfsc_code(),bean.getGSTIN(),bean.getEmail(),bean.getLandline_number(),bean.getAccount_type(),bean.getBranch_name(),bean.getState_name(),bean.getCity_name(),bean.getContractor_id()};
			
			int result=jdbcTemplate.update(queryForUpdate,queryParams);
			
			if (result != 0) {
				response = "Transporter Details Updated Successfully.";
			} else {
				response = "Transporter Update Failed.";
			}
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");
		} catch (Exception e) {
		transactionManager.rollback(status);
		// System.out.println("Indent creation Failed");
		WriteTrHistory.write("Tr_Completed");
		response = "Transporter Update Failed.";
		e.printStackTrace();
	}
		return response;
	}
	/****************************************** TRANSPORTOR UPDATION COMPLETED *************************************************** */
	/***********************************************TRANSPORTOR REGISTRATION START*******************************************/
	public String doTransportorRegistration(TransportorBean bean, String siteId, String user_id,
			HttpServletRequest request, HttpSession session) {
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in Transportor insertion, ");
		String response = "";
		try {
			//db record changed make the list empty to load again data 
			list = new ArrayList<ContractorBean>();
			int transportorSeqNum = jdbcTemplate.queryForInt("select TRANSPORTOR_SEQ.NEXTVAL from dual");

			String query = "insert into TRANSPORTOR_DETAILS(TRANSPORTOR_ID,FIRST_NAME,LAST_NAME,ADDRESS,PAN_NUMBER,MOBILE_NUMBER,ALTERNATE_MOBILE_NUMBER,BANK_ACC_NUMBER,BANK_NAME,IFSC_CODE,STATUS,GSTIN,EMAIL,LANDLINE_NUMBER,ACCOUNT_TYPE,BRANCH_NAME,STATE_NAME,CITY_NAME)"
					+ "  values(?,?,?,?,?,?,?,?,?,?,'A',?,?,?,?,?,?,?)";
			String str = "TRANS" + transportorSeqNum;
			logger.info("***************TRANSPORTOR SEQUENCE NUMBER ***************************"+str);
			//System.out.println("Contractor Id "+str);
			bean.setContractor_id(str);
			Object[] queryParams = { str,bean.getFirst_name(),bean.getLast_name(), 
					bean.getAddress(), bean.getPan_number(),
					bean.getMobile_number(),bean.getAlternate_mobile_number(), 
					bean.getBank_acc_number(), bean.getBank_name(), 
					bean.getIfsc_code(),bean.getGSTIN(),
					bean.getEmail(),bean.getLandline_number(),bean.getAccount_type(),bean.getBranch_name(),bean.getState_name(),bean.getCity_name() };
			int result = jdbcTemplate.update(query, queryParams);
			logger.info("*************** QUERY RESULT OF INSERTION FOR TRANSPOROTOR DETAILS***************************"+result);

			if (result != 0) {
				response = "Registration done Successfully.";
			} else {
				response = "Registration Failed.";
			}
			transactionManager.commit(status);
			// System.out.println("is status completed:"+status.isCompleted());
			WriteTrHistory.write("Tr_Completed");

		} catch (Exception e) {
			transactionManager.rollback(status);
			// System.out.println("Indent creation Failed");
			WriteTrHistory.write("Tr_Completed");
			response = "Registration Failed.";
			//String s=null;
			//s.trim();
			e.printStackTrace();
				
		}

		return response;
	}
	/********************************** COMPLETE THE INSERT OPERATION END************************************************************/
	/****************************************VALIDATE THE tRANSPORTOR NAME START**********************************************/
	public boolean validatTransportorname(String first_name, String last_name, String condition, String contractor_id) {
		int count=0;
			//checking CONTRACTOR_NAME is already exists or not if exists return true
		if(condition.equals("1")){
			//if condition is 1 means reauest from contractor registration page 
			count= jdbcTemplate.queryForInt("select count(*) from TRANSPORTOR_DETAILS where upper(FIRST_NAME)=upper(?) and upper(LAST_NAME)=upper(?) and STATUS!='I' ",first_name,last_name);	
		}	else{
			//if condition is 2 means reauest from contractor update page
			count= jdbcTemplate.queryForInt("select count(*) from TRANSPORTOR_DETAILS where upper(FIRST_NAME)=upper(?) and upper(LAST_NAME)=upper(?) and STATUS!='I' and CONTRACTOR_ID!=?  ",first_name,last_name,contractor_id);
		}
		System.out.println(count);
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}
	/************************************************VALIDATE THE PAN NUMBER START******************************************************/
	public boolean validateTransportorPanNumber(String str, String condition, String contractorId) {

		int count =0;
		if(condition.equals("GSTIN")){
			//checking GSTIN number is already exists or not if exists return true
			
			if(contractorId.length()==0){
				//request from  Contractor registration page
				count= jdbcTemplate.queryForInt("select count(*) from TRANSPORTOR_DETAILS where GSTIN=? and STATUS!='I'",	str);
			}else{
				//request from Contractor update page checking the GSTIN numnber is already exists or not
				count= jdbcTemplate.queryForInt("select count(*) from TRANSPORTOR_DETAILS where GSTIN=? and TRANSPORTOR_ID!=?  and STATUS!='I'",	str,contractorId);
			}
		}else if(condition.equals("PAN")){
			//checking pan number is already exists or not if exists return true
			//
			if(contractorId.length()==0){
				//request from Contractor registration page
			count= jdbcTemplate.queryForInt("select count(*) from TRANSPORTOR_DETAILS where PAN_NUMBER=?  and STATUS!='I'",	str);
			}else{
				//request from Contractor update page checking the PAN numnber is already exists or not
				count= jdbcTemplate.queryForInt("select count(*) from TRANSPORTOR_DETAILS where PAN_NUMBER=? and TRANSPORTOR_ID!=?  and STATUS!='I'",	str,contractorId);
			}
		}
		System.out.println(count);
		if (count > 0) {
			return false;
		} else {
			return true;
		}

	}
}
