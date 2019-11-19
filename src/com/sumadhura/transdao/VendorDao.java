package com.sumadhura.transdao;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.VendorDetails;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.UIProperties;

@Repository
public class VendorDao extends UIProperties  {
	
	public int checkVendorAvailableOrNot(String vendorName) {
		int res = 0;
		String sql = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> TotalProductList = null;
		try {
			logger.info("Inside checkProductAvailableOrNot() Product Name --->  " + vendorName);
			template = new JdbcTemplate(DBConnection.getDbConnection());
			sql = "SELECT * FROM VENDOR_DETAILS WHERE UPPER(VENDOR_NAME) =UPPER('"+vendorName.trim()+"') AND STATUS='A'";
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
	
	public int save(String strVendor,VendorDetails vd) throws Exception {

		int id = 0;
		String sql = "";
		JdbcTemplate template = null;
		String strVendorId=vd.getVendor_Id();
		try {

			logger.info("save db " + strVendor);
			logger.info("saveId db " + strVendorId);

			template = new JdbcTemplate(DBConnection.getDbConnection());
			logger.info("db connection");
			//convertToJavaDateFormat(vd.getDate());
			
			
			
			 sql = "insert into vendor_details(vendor_name,address,mobile_number,gsin_number,vendor_id,Status,STATE,EMP_EMAIL,LANDLINE_NO,STATE_CODE,VENDOR_CON_PER_NAME,VENDOR_CON_PER_NAME_TWO,MOBILE_NUMBER_TWO,TYPE_OF_BUSINESS,NATURE_OF_BUSINESS,PAN_NUMBER,BANK_ACC_NUMBER,ACC_HOLDER_NAME,ACC_TYPE,BANK_NAME," 
			 		+" IFSC_CODE,PARENT_COMPANY,OVERSEASES_REPRESENTATIVE,ESTABLISHED_YEAR,NO_OF_EMPLOYEES,PF_NO,ESI_NO,TOTALSALES_LAST_THREE_YEARS,BRANCH_NAME,BRANCH_CITY,BRANCH_STATE,LIST_WORK_COMPLETED,MAJOR_CUSTOMER,WHETHER_ISO,OTHER_INFO,DATE_OF_INCLUSION)"
			 		+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			id = template.update(sql, new Object[] { vd.getVendor_name(),vd.getAddress(),vd.getMobile_number(),vd.getGsin_number(),
													vd.getVendor_Id(),"A",vd.getVendor_state(),vd.getVendor_email(),vd.getLandline(),
													vd.getState_code(),vd.getVendorcontact_person(),vd.getVendor_Contact_Person_2(),
													vd.getMobile_Number_2(),vd.getType_Of_Business(),vd.getNature_Of_Business(),
													vd.getPan_Number(),vd.getAcc_Number(),vd.getAcc_Holder_Name(),vd.getAcc_Type(),vd.getBank_name(),vd.getIfsc_Code(),
													 vd.getParentCompany(),vd.getOverseasesRepresentative(),vd.getEstablishedYear(),vd.getNoOfEmployees(), vd.getPFRegNo(),
													 vd.getEsiNo(),vd.getTotalSalesForLastThreeYears(),vd.getBranchName(),vd.getBankcity(),vd.getBankState(),
													 vd.getListOfWorkCompleted(),vd.getMajorCustomer(),vd.getWhetherISO(),vd.getOtherInformation(),vd.getDate()
			 
				
			});

		} catch (Exception e) {
			e.printStackTrace();
			id = 9;
		}
		return id;
	}
	
	
	public List<VendorDetails> getAllVendors() {
		JdbcTemplate template=null;
		List<Map<String,Object>> AllVendorsList = null;
		VendorDetails objVendorDetails = null;
		List<VendorDetails> listAllVendorsList =  new ArrayList<VendorDetails>();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select VENDOR_ID,vendor_name,address,mobile_number,gsin_number,STATE,EMP_EMAIL,LANDLINE_NO,STATE_CODE,VENDOR_CON_PER_NAME,DATE_OF_INCLUSION from vendor_details where STATUS = 'A'";

			logger.info(sql);

			AllVendorsList = template.queryForList(sql,new Object[]{});

			for(Map vendorList :AllVendorsList){
			
				objVendorDetails = new VendorDetails();
				objVendorDetails.setVendor_Id(vendorList.get("VENDOR_ID") == null ? "" : vendorList.get("VENDOR_ID").toString());
				objVendorDetails.setVendor_name(vendorList.get("VENDOR_NAME") == null ? "" : vendorList.get("VENDOR_NAME").toString());
				objVendorDetails.setAddress(vendorList.get("ADDRESS") == null ? "" : vendorList.get("ADDRESS").toString());
				objVendorDetails.setMobile_number(vendorList.get("MOBILE_NUMBER") == null ? "" : vendorList.get("MOBILE_NUMBER").toString());
				objVendorDetails.setGsin_number(vendorList.get("GSIN_NUMBER") == null ? "" : vendorList.get("GSIN_NUMBER").toString());
				objVendorDetails.setVendor_state(vendorList.get("STATE") == null ? "-" : vendorList.get("STATE").toString());
				objVendorDetails.setLandline(vendorList.get("LANDLINE_NO") == null ? "-" : vendorList.get("LANDLINE_NO").toString());
				objVendorDetails.setState_code(vendorList.get("STATE_CODE") == null ? "-" : vendorList.get("STATE_CODE").toString());
				objVendorDetails.setVendorcontact_person(vendorList.get("VENDOR_CON_PER_NAME") == null ? "-" : vendorList.get("VENDOR_CON_PER_NAME").toString());
				objVendorDetails.setVendor_email(vendorList.get("EMP_EMAIL")==null?"-":vendorList.get("EMP_EMAIL").toString());
				objVendorDetails.setDate(vendorList.get("DATE_OF_INCLUSION")==null?"-":vendorList.get("DATE_OF_INCLUSION").toString());
				listAllVendorsList.add(objVendorDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();
			//throw new MyException("");
		}

		return listAllVendorsList;
	}
	public int deletevendor(String strvendorName){
		int id = 0;
		JdbcTemplate template=null;
		int count = 0;
		try{
			template = new JdbcTemplate(DBConnection.getDbConnection());
			String sql = "UPDATE vendor_details SET STATUS = 'I' WHERE VENDOR_NAME = ?";
		//	UPDATE sub_product SET STATUS = 'I' WHERE SUB_PRODUCT_ID = ?
			count = template.update(sql,new Object[]{strvendorName});
		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	public int getVendorSeqNo(String strVendorType){
		//	int id = 0;
		JdbcTemplate template=null;
		int count = 0;
		String sql = "";
		try{
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if(strVendorType.equals("vendor")){
				 sql = "select VENDORID_SEQ.nextval from dual";
			}
						
			count = template.queryForInt(sql);

		}catch(Exception e){
			e.printStackTrace();
		}
		return count;
	}
	
	public List<VendorDetails> getAllVendorsList(String vendorId) {
		JdbcTemplate template=null;
		List<Map<String,Object>> AllVendorsList = null;
		VendorDetails objVendorDetails = null;
		List<VendorDetails> listAllVendorsList =  new ArrayList<VendorDetails>();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select VENDOR_ID,vendor_name,address,mobile_number,gsin_number,STATE,EMP_EMAIL,LANDLINE_NO,STATE_CODE,VENDOR_CON_PER_NAME,TYPE_OF_BUSINESS,NATURE_OF_BUSINESS,PAN_NUMBER,BANK_ACC_NUMBER,ACC_HOLDER_NAME,ACC_TYPE,BANK_NAME,IFSC_CODE,VENDOR_CON_PER_NAME_TWO,MOBILE_NUMBER_TWO, "
					+"PARENT_COMPANY,OVERSEASES_REPRESENTATIVE,ESTABLISHED_YEAR,NO_OF_EMPLOYEES,PF_NO,ESI_NO,TOTALSALES_LAST_THREE_YEARS,BRANCH_NAME,BRANCH_CITY,BRANCH_STATE,LIST_WORK_COMPLETED,MAJOR_CUSTOMER,WHETHER_ISO,OTHER_INFO,DATE_OF_INCLUSION"
					+ " from vendor_details where STATUS = 'A' and VENDOR_ID=?";

			logger.info(sql);

			AllVendorsList = template.queryForList(sql,new Object[]{vendorId});

			for(Map vendorList :AllVendorsList){
				objVendorDetails = new VendorDetails();
				objVendorDetails.setVendor_Id((vendorList.get("vendor_Id") == null ? "" : vendorList.get("vendor_Id").toString()));
				objVendorDetails.setVendor_name((vendorList.get("vendor_name") == null ? "" : vendorList.get("vendor_name").toString()));
				objVendorDetails.setAddress((vendorList.get("address") == null ? "" : vendorList.get("address").toString()));
				objVendorDetails.setMobile_number((vendorList.get("mobile_number") == null ? "" : vendorList.get("mobile_number").toString()));
				objVendorDetails.setGsin_number((vendorList.get("gsin_number") == null ? "" : vendorList.get("gsin_number").toString()));
				objVendorDetails.setVendor_state((vendorList.get("STATE") == null ? "-" : vendorList.get("STATE").toString()));
				objVendorDetails.setVendor_email((vendorList.get("EMP_EMAIL") == null ? "-" : vendorList.get("EMP_EMAIL").toString()));
				objVendorDetails.setLandline((vendorList.get("LANDLINE_NO") == null ? "0" : vendorList.get("LANDLINE_NO").toString()));
				objVendorDetails.setState_code((vendorList.get("STATE_CODE") == null ? "-" : vendorList.get("STATE_CODE").toString()));
				objVendorDetails.setVendorcontact_person((vendorList.get("VENDOR_CON_PER_NAME") == null ? "-" : vendorList.get("VENDOR_CON_PER_NAME").toString()));
				objVendorDetails.setVendor_Contact_Person_2((vendorList.get("VENDOR_CON_PER_NAME_TWO") == null ? "-" : vendorList.get("VENDOR_CON_PER_NAME_TWO").toString()));
				objVendorDetails.setMobile_Number_2((vendorList.get("MOBILE_NUMBER_TWO") == null ? "0" : vendorList.get("MOBILE_NUMBER_TWO").toString()));
				objVendorDetails.setType_Of_Business((vendorList.get("TYPE_OF_BUSINESS") == null ? "-" : vendorList.get("TYPE_OF_BUSINESS").toString()));
				objVendorDetails.setNature_Of_Business((vendorList.get("NATURE_OF_BUSINESS") == null ? "-" : vendorList.get("NATURE_OF_BUSINESS").toString()));
				objVendorDetails.setPan_Number((vendorList.get("PAN_NUMBER") == null ? "-" : vendorList.get("PAN_NUMBER").toString()));
				objVendorDetails.setAcc_Number(Long.parseLong((vendorList.get("BANK_ACC_NUMBER") == null ? "0" : vendorList.get("BANK_ACC_NUMBER").toString())));
				objVendorDetails.setAcc_Holder_Name((vendorList.get("ACC_HOLDER_NAME") == null ? "-" : vendorList.get("ACC_HOLDER_NAME").toString()));
				objVendorDetails.setAcc_Type((vendorList.get("ACC_TYPE") == null ? "-" : vendorList.get("ACC_TYPE").toString()));
				objVendorDetails.setBank_name((vendorList.get("BANK_NAME") == null ? "-" : vendorList.get("BANK_NAME").toString()));
				objVendorDetails.setIfsc_Code((vendorList.get("IFSC_CODE") == null ? "-" : vendorList.get("IFSC_CODE").toString()));
				
				objVendorDetails.setParentCompany((vendorList.get("PARENT_COMPANY") == null ? "-" : vendorList.get("PARENT_COMPANY").toString()));
				objVendorDetails.setOverseasesRepresentative((vendorList.get("OVERSEASES_REPRESENTATIVE") == null ? "-" : vendorList.get("OVERSEASES_REPRESENTATIVE").toString()));
				objVendorDetails.setEstablishedYear((vendorList.get("ESTABLISHED_YEAR") == null ? "-" : vendorList.get("ESTABLISHED_YEAR").toString()));
				objVendorDetails.setNoOfEmployees((vendorList.get("NO_OF_EMPLOYEES") == null ? "-" : vendorList.get("NO_OF_EMPLOYEES").toString()));
				objVendorDetails.setPFRegNo((vendorList.get("PF_NO") == null ? "-" : vendorList.get("PF_NO").toString()));
				objVendorDetails.setEsiNo((vendorList.get("ESI_NO") == null ? "-" : vendorList.get("ESI_NO").toString()));
				objVendorDetails.setTotalSalesForLastThreeYears((vendorList.get("TOTALSALES_LAST_THREE_YEARS") == null ? "-" : vendorList.get("TOTALSALES_LAST_THREE_YEARS").toString()));
				objVendorDetails.setBranchName((vendorList.get("BRANCH_NAME") == null ? "-" : vendorList.get("BRANCH_NAME").toString()));
				objVendorDetails.setBankcity((vendorList.get("BRANCH_CITY") == null ? "-" : vendorList.get("BRANCH_CITY").toString()));
				objVendorDetails.setBankState((vendorList.get("BRANCH_STATE") == null ? "-" : vendorList.get("BRANCH_STATE").toString()));
				objVendorDetails.setListOfWorkCompleted((vendorList.get("LIST_WORK_COMPLETED") == null ? "-" : vendorList.get("LIST_WORK_COMPLETED").toString()));
				objVendorDetails.setMajorCustomer((vendorList.get("MAJOR_CUSTOMER") == null ? "-" : vendorList.get("MAJOR_CUSTOMER").toString()));
				objVendorDetails.setWhetherISO((vendorList.get("WHETHER_ISO") == null ? "-" : vendorList.get("WHETHER_ISO").toString()));
				objVendorDetails.setOtherInformation((vendorList.get("OTHER_INFO") == null ? "-" : vendorList.get("OTHER_INFO").toString()));
				objVendorDetails.setDate((vendorList.get("DATE_OF_INCLUSION") == null ? "-" : vendorList.get("DATE_OF_INCLUSION").toString()));
				
				listAllVendorsList.add(objVendorDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();
			//throw new MyException("");
		}

		return listAllVendorsList;
	}

	public int editAndSaveVendor(VendorDetails vd) {
		int result = 0;
		JdbcTemplate template=null;
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "update VENDOR_DETAILS set VENDOR_NAME = ?, GSIN_NUMBER = ?, STATE = ?, EMP_EMAIL = ?, "
					+ " LANDLINE_NO = ?, STATE_CODE = ?, VENDOR_CON_PER_NAME = ? ,MOBILE_NUMBER = ? , ADDRESS = ?, "
					+ " VENDOR_CON_PER_NAME_TWO = ?, MOBILE_NUMBER_TWO = ?, TYPE_OF_BUSINESS = ?, NATURE_OF_BUSINESS = ?, "
					+ " PAN_NUMBER = ?, BANK_ACC_NUMBER = ? , ACC_HOLDER_NAME = ?, ACC_TYPE = ?, BANK_NAME = ?, IFSC_CODE = ?, "
					+ " PARENT_COMPANY=?, OVERSEASES_REPRESENTATIVE=?, ESTABLISHED_YEAR=?, NO_OF_EMPLOYEES=?, PF_NO=?, ESI_NO=?, TOTALSALES_LAST_THREE_YEARS=?," 
					+ " BRANCH_NAME=?, BRANCH_CITY=?, BRANCH_STATE=?, LIST_WORK_COMPLETED=?, MAJOR_CUSTOMER=?, WHETHER_ISO=?,OTHER_INFO=?,DATE_OF_INCLUSION=?"
					+ " where VENDOR_ID = ? ";
			result = template.update(sql,new Object[]{
					vd.getVendor_name(),
					vd.getGsin_number(),
					vd.getVendor_state(),
					vd.getVendor_email(),
					vd.getLandline(),
					vd.getState_code(),
					vd.getVendorcontact_person(),
					vd.getMobile_number(),
					vd.getAddress(),
					vd.getVendor_Contact_Person_2(),
					vd.getMobile_Number_2(),vd.getType_Of_Business(),vd.getNature_Of_Business(),
					vd.getPan_Number(),vd.getAcc_Number(),vd.getAcc_Holder_Name(),vd.getAcc_Type(),vd.getBank_name(),vd.getIfsc_Code(),
					vd.getParentCompany(),vd.getOverseasesRepresentative(),vd.getEstablishedYear(),vd.getNoOfEmployees(), vd.getPFRegNo(),
					 vd.getEsiNo(),vd.getTotalSalesForLastThreeYears(),vd.getBranchName(),vd.getBankcity(),vd.getBankState(),
					 vd.getListOfWorkCompleted(),vd.getMajorCustomer(),vd.getWhetherISO(),vd.getOtherInformation(),vd.getDate(),vd.getVendor_Id()
					
					});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	
	public List<Map<String,Object>> getAllProductDetails(String siteId,String AllOrNot) {
		JdbcTemplate template=null;
		List<Map<String,Object>> AllProductList = null;
		VendorDetails objProductDetails = null;
		List<VendorDetails> listAllProductList =  new ArrayList<VendorDetails>();
		String sql ="";
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			if(AllOrNot.equalsIgnoreCase("ALL")){
				 sql = "select P.PRODUCT_ID as Product_Id,P.NAME as Product_Name ,S.SUB_PRODUCT_ID as Sub_Product_Id ,S.NAME as Sub_Product_Name ,"
						+" C.CHILD_PRODUCT_ID as Child_product_id,C.Name as Child_Product_Name,M.MEASUREMENT_ID as MeasurmentId,M.NAME as Measurment_Name,P.PRODUCT_DEPT"
						+" from PRODUCT P ,SUB_PRODUCT S,CHILD_PRODUCT C,MEASUREMENT M"
						+" where P.PRODUCT_ID = S.PRODUCT_ID and S.SUB_PRODUCT_ID = C.SUB_PRODUCT_ID and M.CHILD_PRODUCT_ID = C.CHILD_PRODUCT_ID"
						+" and P.STATUS = 'A' and S.STATUS = 'A' and  C.Status = 'A'  and M.STATUS = 'A'";

			}
			
			else if(siteId.equals("996")){
				
				 sql = "select P.PRODUCT_ID as Product_Id,P.NAME as Product_Name ,S.SUB_PRODUCT_ID as Sub_Product_Id ,S.NAME as Sub_Product_Name ,"
					+" C.CHILD_PRODUCT_ID as Child_product_id,C.Name as Child_Product_Name,M.MEASUREMENT_ID as MeasurmentId,M.NAME as Measurment_Name"
					+" from PRODUCT P ,SUB_PRODUCT S,CHILD_PRODUCT C,MEASUREMENT M"
					+" where P.PRODUCT_ID = S.PRODUCT_ID and S.SUB_PRODUCT_ID = C.SUB_PRODUCT_ID and M.CHILD_PRODUCT_ID = C.CHILD_PRODUCT_ID"
					+" and P.STATUS = 'A' and S.STATUS = 'A' and  C.Status = 'A'  and M.STATUS = 'A'  AND PRODUCT_DEPT in('ALL','MARKETING')";

				
			}else{
				
				 sql = "select P.PRODUCT_ID as Product_Id,P.NAME as Product_Name ,S.SUB_PRODUCT_ID as Sub_Product_Id ,S.NAME as Sub_Product_Name ,"
						+" C.CHILD_PRODUCT_ID as Child_product_id,C.Name as Child_Product_Name,M.MEASUREMENT_ID as MeasurmentId,M.NAME as Measurment_Name"
						+" from PRODUCT P ,SUB_PRODUCT S,CHILD_PRODUCT C,MEASUREMENT M"
						+" where P.PRODUCT_ID = S.PRODUCT_ID and S.SUB_PRODUCT_ID = C.SUB_PRODUCT_ID and M.CHILD_PRODUCT_ID = C.CHILD_PRODUCT_ID"
						+" and P.STATUS = 'A' and S.STATUS = 'A' and  C.Status = 'A'  and M.STATUS = 'A'  AND PRODUCT_DEPT in('ALL','STORE')";
			}
			logger.info(sql);

			AllProductList = template.queryForList(sql,new Object[]{});
			/*for(Map productList :AllProductList){
				objProductDetails = new VendorDetails();
				
				objProductDetails.setProduct_Id((productList.get("Product_Id") == null ? "" : productList.get("Product_Id").toString()));
				objProductDetails.setProduct_name((productList.get("Product_Name") == null ? "" : productList.get("Product_Name").toString()));
				objProductDetails.setSubProduct_Id((productList.get("Sub_Product_Id") == null ? "" : productList.get("Sub_Product_Id").toString()));
				objProductDetails.setSubProduct_Name((productList.get("Sub_Product_Name") == null ? "" : productList.get("Sub_Product_Name").toString()));
				objProductDetails.setChildProduct_Id((productList.get("Child_product_id") == null ? "" : productList.get("Child_product_id").toString()));
				objProductDetails.setChildProduct_Name((productList.get("Child_Product_Name") == null ? "-" : productList.get("Child_Product_Name").toString()));
				objProductDetails.setMeasurement_Id((productList.get("MeasurmentId") == null ? "-" : productList.get("MeasurmentId").toString()));
				objProductDetails.setMeasurement_Name((productList.get("Measurment_Name") == null ? "-" : productList.get("Measurment_Name").toString()));
				
				listAllProductList.add(objProductDetails);
			}*/

			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return AllProductList;
		
	}
	
	public String checkMandatoryData(VendorDetails vd) {
		
		String response="";
		if(StringUtils.isNotBlank(vd.getVendor_name()) && StringUtils.isNotBlank(vd.getAddress()) && StringUtils.isNotBlank(vd.getMobile_number()) && StringUtils.isNotBlank(vd.getGsin_number())/* StringUtils.isNotBlank(vd.getVendor_state())*/&& StringUtils.isNotBlank(vd.getVendor_email())&& StringUtils.isNotBlank(vd.getLandline())&& /*StringUtils.isNotBlank(vd.getState_code())&&*/ StringUtils.isNotBlank(vd.getVendorcontact_person()))
		{
			if(StringUtils.isNotBlank(vd.getVendor_name())/* && StringUtils.isNotBlank(vd.getVendor_Contact_Person_2()) && StringUtils.isNotBlank(vd.getMobile_Number_2())*/ && StringUtils.isNotBlank(vd.getAcc_Holder_Name()) && (vd.getAcc_Number()!=0) && StringUtils.isNotBlank(vd.getAcc_Type()) && StringUtils.isNotBlank(vd.getBank_name()) && StringUtils.isNotBlank(vd.getType_Of_Business()) && StringUtils.isNotBlank(vd.getNature_Of_Business()) && StringUtils.isNotBlank(vd.getVendor_name())){
			response="true";
		}
		}else{
			response="false";
		}
		
		
		return response;
	}
	
	public String checkGstinNumber(String gstiNumber) {
		List<Map<String, Object>> AllVendorsList = null;
		JdbcTemplate template = null;

		String response = "true";
		String strGstNumber = "";

		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			//String query = "select GSIN_NUMBER from VENDOR_DETAILS where STATUS='A'";
			int count = template.queryForInt("select count(*) from VENDOR_DETAILS where GSIN_NUMBER=? and STATUS='A'",
					gstiNumber);
			System.out.println("VENDOR_DETAILS "+count);
			if (count > 0) {
				return "true";
			} else {
				return "false";
			}
			
			
		
		}catch(Exception e){
			e.printStackTrace();
		}

		return response;
	}
	/**********************************************************check company name***********************************************************/
	public String checkVendorName(String vendor) {
		List<Map<String, Object>> AllVendorsList = null;
		JdbcTemplate template = null;

		String response = "true";
		String strGstNumber = "";

		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			//String query = "select GSIN_NUMBER from VENDOR_DETAILS where STATUS='A'";
			int count = template.queryForInt("select count(*) from VENDOR_DETAILS where VENDOR_NAME=? and STATUS='A'",
					vendor);
			System.out.println("VENDOR_NAME "+count);
			if (count > 0) {
				return "true";
			} else {
				return "false";
			}
			
			
		
		}catch(Exception e){
			e.printStackTrace();
		}

		return response;
	}
	
}
