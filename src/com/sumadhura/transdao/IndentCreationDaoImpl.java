package com.sumadhura.transdao;

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
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import com.sumadhura.bean.AuditLogDetailsBean;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentCreationDetailsDto;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.IndentReceiveDto;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.NumberToWord;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;


@Repository
public class IndentCreationDaoImpl extends UIProperties  implements IndentCreationDao {

	static Logger log = Logger.getLogger(IndentCreationDaoImpl.class);

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	@Override
	public int insertIndentCreation(int indentCreationSeqNum, IndentCreationDto indentCreationDto) {

	
		
		// checking is site wise indent no exist or not . If exist getting max number +1 and saving.
		//Getting site wise indent number is asynchronous...if multiple persons clicking same time getting same number in indent creation page
		String sql="select count(1) from SUMADHURA_INDENT_CREATION where SITE_ID = ? and SITEWISE_INDENT_NO = ?";
		int count = jdbcTemplate.queryForInt(sql, new Object[] {indentCreationDto.getSiteId(),indentCreationDto.getSiteWiseIndentNo()});
		int siteWiseIndentNo =  indentCreationDto.getSiteWiseIndentNo();
	
		if(count > 0){
			sql="select max(SITEWISE_INDENT_NO) from SUMADHURA_INDENT_CREATION where SITE_ID ='"+indentCreationDto.getSiteId()+"'";
			siteWiseIndentNo = jdbcTemplate.queryForInt(sql, new Object[] {});
			siteWiseIndentNo=siteWiseIndentNo+1;
		}

		String query = "INSERT INTO SUMADHURA_INDENT_CREATION(INDENT_CREATION_ID, create_date, site_id ,INDENT_CREATE_EMP_ID ,schedule_date ,Pending_emp_id ,pendind_dept_id ,status,REQUIRED_DATE,TEMPPASS,SITEWISE_INDENT_NO,VERSION_NO,REFERENCE_NO,ISSUE_DATE,INDENT_NAME) "+
		"VALUES(?, sysdate ,? ,? ,? ,? ,? ,? ,? ,?, ?, ?, ?, ?,?)";
		int result = jdbcTemplate.update(query, new Object[] {
				indentCreationSeqNum, 	indentCreationDto.getSiteId(),
				indentCreationDto.getUserId(), 	
				indentCreationDto.getScheduleDate(),	indentCreationDto.getPendingEmpId(),
				indentCreationDto.getPendingDeptId(),	"A", indentCreationDto.getRequiredDate(),
				indentCreationDto.getTempPass(), siteWiseIndentNo,
				indentCreationDto.getVersionNo(),indentCreationDto.getReference_No(),indentCreationDto.getIssue_date(),indentCreationDto.getIndentName()
		});
		return result;
	}

	@Override
	public int insertIndentCreationDetails(int indentCreationDetailsSeqNum, int indentCreationSeqNum,
			IndentCreationDetailsDto indentCreationDetailsDto) {
		String query = "INSERT INTO SUMADHURA_INDENT_CREATION_DTLS(INDENT_CREATION_DETAILS_ID,INDENT_CREATION_ID"+
		",PRODUCT_ID  , SUB_PRODUCT_ID ,CHILD_PRODUCT_ID ,MEASUREMENT_ID ,REQ_QUANTITY , remarks"+
		",priority,RECEIVE_QUANTITY,AVAIL_QUANTITY_AT_CREATION,CREATED_BY) "+
		"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? , ?)";
		int result = jdbcTemplate.update(query, new Object[] {
				indentCreationDetailsSeqNum, indentCreationSeqNum,
				indentCreationDetailsDto.getProdId(),indentCreationDetailsDto.getSubProdId(),
				indentCreationDetailsDto.getChildProdId(),indentCreationDetailsDto.getMeasurementId(),
				indentCreationDetailsDto.getRequiredQuantity(),indentCreationDetailsDto.getRemarks(),
				null,"0",indentCreationDetailsDto.getAvailableQuantity(),indentCreationDetailsDto.getCreatedBy()
		});
		return result;
	}

	@Override
	public int insertIndentCreationApproval(int indentCreationApprovalSeqNum, int indentNumber,
			IndentCreationDto indentCreationDto) {
		String query = "INSERT INTO SUM_INT_CREATION_APPROVAL_DTLS(INT_CREATION_APPROVAL_DTLS_ID ,INDENT_CREATION_ID ,"
			+ "INDENT_TYPE ,creation_date ,SITE_ID ,INDENT_CREATE_APPROVE_EMP_ID, PURPOSE  ) "+
			"VALUES(?, ?, ?, sysdate, ?, ?, ?)";
		int result = jdbcTemplate.update(query, new Object[] {
				indentCreationApprovalSeqNum, 	indentNumber,
				"C", indentCreationDto.getSiteId(),indentCreationDto.getUserId(),indentCreationDto.getPurpose()
		});
		return result;
	}





	@Override
	public int getIndentCreationDetailsSequenceNumber() {
		int indentCreationDetailsSeqNum = jdbcTemplate.queryForInt("SELECT INDENT_CREATION_DTLS_SEQ.NEXTVAL FROM DUAL");
		return indentCreationDetailsSeqNum;
	}



	@Override
	public int getIndentChangedDetailsSequenceNumber() {
		int indentChangedDetailsSeqNum = jdbcTemplate.queryForInt("SELECT INDENT_CHANGED_DTLS_SEQ.NEXTVAL FROM DUAL");
		return indentChangedDetailsSeqNum;
	}

	@Override
	public int getCentralIndentProcessSequenceNumber() {
		int centralIndentProcessSeqNum = jdbcTemplate.queryForInt("SELECT CENTRAL_INDENT_PROCESS_SEQ.NEXTVAL FROM DUAL");
		return centralIndentProcessSeqNum;
	}


	@Override
	public String getPendingEmployeeId(String user_id,int reqSiteId) {

		String strApproverEmpId = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT APPROVER_EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? AND MODULE_TYPE='INDENT' AND SITE_ID=?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id,reqSiteId});  



		for(Map<String, Object> prods : dbIndentDts) {
			strApproverEmpId = prods.get("APPROVER_EMP_ID")==null ? "" :   prods.get("APPROVER_EMP_ID").toString();
			//icb.setFromEmpName(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());		
		}

		return strApproverEmpId;
	}
	@Override
	public String getPendingDeptId(String user_id,int siteId) {



		String strApproverDepId = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT APPROVER_DEPT_ID FROM SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? AND MODULE_TYPE='INDENT' AND SITE_ID=?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id,siteId});  



		for(Map<String, Object> prods : dbIndentDts) {
			strApproverDepId = prods.get("APPROVER_DEPT_ID")==null ? "" :   prods.get("APPROVER_DEPT_ID").toString();
			//icb.setFromEmpName(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());		
		}

		return strApproverDepId;


		/*String query = "SELECT APPROVER_DEPT_ID FROM SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = "+user_id;
		String result = jdbcTemplate.queryForObject(query,String.class);  
		return result;*/
	}





	@Override
	public int rejectIndentCreation(int indentCreationSeqNum, IndentCreationDto indentCreationDto) {
		int result =0;
		String query = "UPDATE SUMADHURA_INDENT_CREATION set STATUS = 'I', MODIFYDATE= sysdate, TEMPPASS = ?  "+
		"WHERE INDENT_CREATION_ID = ?";
		 result = jdbcTemplate.update(query, new Object[] {
				indentCreationDto.getTempPass(), indentCreationSeqNum
		});
		if(result>0){
			String sql = "INSERT INTO SUM_INT_CREATION_APPROVAL_DTLS(INT_CREATION_APPROVAL_DTLS_ID ,INDENT_CREATION_ID ,"
				+ "INDENT_TYPE ,creation_date ,SITE_ID ,INDENT_CREATE_APPROVE_EMP_ID, PURPOSE  ) "+
				"VALUES(?, ?, ?, sysdate, ?, ?, ?)";
			 result = jdbcTemplate.update(sql, new Object[] {
					 indentCreationDto.getSequenccce_Number(),indentCreationSeqNum,
					"R", indentCreationDto.getSiteId(),indentCreationDto.getUserId(),indentCreationDto.getPurpose()
			});
			
		}
		return result;
	}

	@Override
	public String getIndentFrom(int indentNumber) {
		String indentFrom = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT  SED.EMP_ID,SED.EMP_NAME FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED where SICAD.INDENT_CREATE_APPROVE_EMP_ID = SED.EMP_ID  AND SICAD.INT_CREATION_APPROVAL_DTLS_ID in"
			+ "(SELECT max(SICAD.INT_CREATION_APPROVAL_DTLS_ID) FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD where SICAD.INDENT_CREATION_ID = ? GROUP BY SICAD.INDENT_CREATION_ID)";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {
			indentFrom = prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString();		
		}
		return indentFrom;
	}
	@Override
	public String getIndentTo(String user_id) {
		String indentTo = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT  SED.EMP_ID,SED.EMP_NAME FROM SUMADHURA_APPROVER_MAPPING_DTL SAMD,SUMADHURA_EMPLOYEE_DETAILS SED where SAMD.EMP_ID = ? AND SAMD.APPROVER_EMP_ID = SED.EMP_ID AND MODULE_TYPE='INDENT'";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id});
		for(Map<String, Object> prods : dbIndentDts) {
			indentTo = prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString();		
		}
		return indentTo;
	}
	@Override
	public void getIndentFromDetails(int indentNumber,IndentCreationBean icb) {
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT  SED.EMP_ID,SED.EMP_NAME FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED where SICAD.INDENT_CREATE_APPROVE_EMP_ID = SED.EMP_ID  AND SICAD.INT_CREATION_APPROVAL_DTLS_ID in"
			+ "(SELECT max(SICAD.INT_CREATION_APPROVAL_DTLS_ID) FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD where SICAD.INDENT_CREATION_ID = ? GROUP BY SICAD.INDENT_CREATION_ID)";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {
			icb.setFromEmpId(prods.get("EMP_ID")==null ? "" :   prods.get("EMP_ID").toString());
			icb.setFromEmpName(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());		
		}
	}

	@Override
	public void getIndentToDetails(String strSite_id,String user_id,IndentCreationBean icb) {
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT  SED.EMP_ID,SED.EMP_NAME FROM SUMADHURA_APPROVER_MAPPING_DTL SAMD,SUMADHURA_EMPLOYEE_DETAILS SED where SAMD.EMP_ID = ? AND SAMD.APPROVER_EMP_ID = SED.EMP_ID AND MODULE_TYPE='INDENT' and SITE_ID = ?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id,strSite_id});
		for(Map<String, Object> prods : dbIndentDts) {
			icb.setToEmpId(prods.get("EMP_ID")==null ? "" :   prods.get("EMP_ID").toString());
			icb.setToEmpName(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());		
		}
	}
	@Override
	public void getIndentToDetails2(String strSite_id,String user_id,IndentCreationBean icb) {
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT  SDD.DEPT_ID,SDD.DEPT_NAME FROM SUMADHURA_APPROVER_MAPPING_DTL SAMD,SUMADHURA_DEPARTMENT_DETAILS SDD where SAMD.EMP_ID = ? AND SDD.DEPT_ID = SAMD.APPROVER_DEPT_ID AND MODULE_TYPE='INDENT' and SITE_ID = ?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id,strSite_id});
		for(Map<String, Object> prods : dbIndentDts) {
			icb.setToEmpId(prods.get("DEPT_ID")==null ? "" :   prods.get("DEPT_ID").toString());
			icb.setToEmpName(prods.get("DEPT_NAME")==null ? "" :   prods.get("DEPT_NAME").toString());		
		}
	}
	@Override
	public int insertIndentChangedDetails(IndentCreationBean actualIndentDetails, IndentCreationBean changedIndentDetails,int indentnumber,int indentChangedDetailsSeqNum,int IndentCreationDetailsId,String indentChangeAction,String user_id) {
		String query = "INSERT INTO SUMADHURA_INDENT_CHANGED_DTLS(INDENT_CHANGE_DETAILS_ID,INDENT_CREATION_DETAILS_ID,ACTUAL_PRODCT_ID,CHANGED_PRODCT_ID"+
		",ACTUAL_SUB_PRODCT_ID,CHANGED_SUB_PRODCT_ID,ACTUAL_CHILD_PRODCT_ID,CHANGED_CHILD_PRODCT_ID,ACTUAL_MEASURMENT_ID"+
		",CHANGED_MEASURMENT_ID,ACTUAL_QUANTITY,CHANGED_QUANTITY,INDENT_CREATION_ID,INDENT_CHANGE_ACTION,CREATION_DATE,EMP_ID,REMARKS) "+
		"VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,sysdate,?,?)";
		int result = jdbcTemplate.update(query, new Object[] {
				indentChangedDetailsSeqNum,	IndentCreationDetailsId,
				actualIndentDetails.getProductId1(),changedIndentDetails.getProductId1(),
				actualIndentDetails.getSubProductId1(),changedIndentDetails.getSubProductId1(),
				actualIndentDetails.getChildProductId1(),changedIndentDetails.getChildProductId1(),
				actualIndentDetails.getUnitsOfMeasurementId1(),changedIndentDetails.getUnitsOfMeasurementId1(),
				actualIndentDetails.getRequiredQuantity1(),changedIndentDetails.getRequiredQuantity1(),
				indentnumber,indentChangeAction,user_id,changedIndentDetails.getRemarks1()
		});
		return result;
	}
	@Override
	public int updateProductsComments(int intIndentCreationDetailsId,String strRemarks) {


		String query = "update SUMADHURA_INDENT_CREATION_DTLS set REMARKS = ? where INDENT_CREATION_DETAILS_ID = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				strRemarks,intIndentCreationDetailsId
		});
		return result;
	}




	@Override
	public int insertCentralIndentProcess(int indentProcessId,IndentCreationBean changedIndentDetails,int IndentCreationDetailsId,int site_id)
	{
		String query = "INSERT INTO SUMADHURA_CNTL_INDENT_PROCESS(INDENT_PROCESS_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,CENTRAL_REQ_QUANTITY,ALLOCATED_QUANTITY,PENDING_QUANTIY,INTIATED_QUANTITY,STATUS"+
		",INDENT_REQ_SITE_ID,INDENT_CREATION_DETAILS_ID,CREATION_DATE,MEASUREMENT_ID,INDENT_REQ_QUANTITY) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,sysdate,?,?)";
		int result = jdbcTemplate.update(query, new Object[] {
				indentProcessId,
				changedIndentDetails.getProductId1(),
				changedIndentDetails.getSubProductId1(),
				changedIndentDetails.getChildProductId1(),
				changedIndentDetails.getRequiredQuantity1(),"0",
				changedIndentDetails.getRequiredQuantity1(),"0",
				"A",site_id,IndentCreationDetailsId,
				changedIndentDetails.getUnitsOfMeasurementId1(),
				changedIndentDetails.getRequiredQuantity1()
		});
		return result;
	}





	@Override
	public int updateIndentCreationDetails(int IndentCreationDetailsId,IndentCreationBean changedIndentDetails) {
		String query = "UPDATE SUMADHURA_INDENT_CREATION_DTLS set PRODUCT_ID = ?, SUB_PRODUCT_ID = ?, CHILD_PRODUCT_ID = ?, "+
		"MEASUREMENT_ID = ?, REQ_QUANTITY = ? "+
		"WHERE INDENT_CREATION_DETAILS_ID = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				changedIndentDetails.getProductId1(),
				changedIndentDetails.getSubProductId1(),
				changedIndentDetails.getChildProductId1(),
				changedIndentDetails.getUnitsOfMeasurementId1(),
				changedIndentDetails.getRequiredQuantity1(),
				IndentCreationDetailsId
		});
		return result;

	}
	@Override
	public int deleteRowInIndentCreationDetails(int IndentCreationDetailsId) {
		String query = "DELETE FROM SUMADHURA_INDENT_CREATION_DTLS WHERE INDENT_CREATION_DETAILS_ID = ? ";

		int result = jdbcTemplate.update(query, new Object[] {
				IndentCreationDetailsId
		});
		return result;

	}





	@Override
	public List<ProductDetails> getAllSubProducts(String prodId) {
		List<Map<String,Object>> AllProductList = null;
		ProductDetails objProductDetails = null;
		List<ProductDetails> listAllProductList =  new ArrayList<ProductDetails>();
		try {

			String sql = "SELECT distinct(SP.SUB_PRODUCT_ID),SP.NAME FROM SUB_PRODUCT SP, SUMADHURA_INDENT_CREATION_DTLS SICD, SUMADHURA_INDENT_CREATION SIC WHERE "+
			"SIC.INDENT_CREATION_ID = SICD.INDENT_CREATION_ID AND SIC.PENDIND_DEPT_ID = '998' AND SIC.STATUS = 'A' "+
			"AND SICD.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID AND SICD.PRODUCT_ID = ? ";

			//	System.out.println(sql);

			AllProductList = jdbcTemplate.queryForList(sql,new Object[]{prodId});
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

	@Override
	public List<ProductDetails> getAllChildProducts(String strSubProdId) {
		List<Map<String,Object>> AllProductList = null;
		ProductDetails objProductDetails = null;
		List<ProductDetails> listAllProductList =  new ArrayList<ProductDetails>();
		try {
			String sql = "SELECT distinct(CP.CHILD_PRODUCT_ID),CP.NAME FROM CHILD_PRODUCT CP, SUMADHURA_INDENT_CREATION_DTLS SICD, SUMADHURA_INDENT_CREATION SIC WHERE "+
			"SIC.INDENT_CREATION_ID = SICD.INDENT_CREATION_ID AND SIC.PENDIND_DEPT_ID = '998' AND SIC.STATUS = 'A' "+
			"AND SICD.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID AND SICD.SUB_PRODUCT_ID = ? ";

			//System.out.println(sql);

			AllProductList = jdbcTemplate.queryForList(sql,new Object[]{strSubProdId});

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



	/*@Override
	public int updatePurchaseDepartmentIndentProcess(ProductDetails productDetails,String strSattus)
	{
		String query = "update SUM_PURCHASE_DEPT_INDENT_PROSS set PENDING_QUANTIY = PENDING_QUANTIY-"+productDetails.getQuantity()+" ,"
		+ "PO_INTIATED_QUANTITY  = PO_INTIATED_QUANTITY +"+productDetails.getQuantity()+",STATUS = ?"
		+ " where PURCHASE_DEPT_INDENT_PROSS_SEQ = ?";

		int result = jdbcTemplate.update(query, new Object[] {strSattus,
				productDetails.getPurchaseDeptIndentProcessSeqId()

		});
		return result;
	}*/

	@Override
	public int updateIndentCreationDetails(ProductDetails productDetails)
	{
		String query = "update SUMADHURA_INDENT_CREATION_DTLS set RECEIVE_QUANTITY = RECEIVE_QUANTITY+? where  INDENT_CREATION_ID = ? and CHILD_PRODUCT_ID = ?";

		int result = jdbcTemplate.update(query, new Object[] {
				productDetails.getQuantity(),productDetails.getStrIndentId(),productDetails.getChild_ProductId()



		});
		return result;
	}

	@Override
	public void inactiveIndent(ProductDetails productDetails)
	{


		String strStatus = "";
		int intRequestedQuantity = 0;
		int intReceiveQuantity  = 0;

		List<Map<String, Object>> productList = null;


		String query = "select REQ_QUANTITY,RECEIVE_QUANTITY from SUMADHURA_INDENT_CREATION_DTLS where INDENT_CREATION_ID = ? ";

		productList = jdbcTemplate.queryForList(query, new Object[] { productDetails.getStrIndentId(),				
		});

		for (Map ProductDetails : productList) {


			intRequestedQuantity = Integer.parseInt(ProductDetails.get("REQ_QUANTITY") == null ? "0" : ProductDetails.get("REQ_QUANTITY").toString());
			intReceiveQuantity = Integer.parseInt(ProductDetails.get("RECEIVE_QUANTITY") == null ? "0" : ProductDetails.get("RECEIVE_QUANTITY").toString());


			if(intReceiveQuantity >= intRequestedQuantity){
				strStatus = "I";
			}

		}

		if(strStatus.equals("I")){
			query = "update SUMADHURA_INDENT_CREATION set STATUS = ?, MODIFYDATE= sysdate where INDENT_CREATION_ID = ?";

			int result = jdbcTemplate.update(query, new Object[] {
					"I",productDetails.getStrIndentId()
			});
		}

		//return result;
	}


	@Override
	public List<IndentCreationBean> getViewMyRequestIndents(String centralDeptId, String siteId, String siteWiseIndentNo) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;


		//String query = "select distinct(INDENT_NO) AS INDENT_NO ,REQ_SITE,CREATION_DATE from SUMADHURA_CNTL_INDENT_REQ_DTLS where  STATUS = 'A' and SENDER_SITE = ? ";

		StringBuffer query =  new StringBuffer("select min(SIC.SITEWISE_INDENT_NO) as SITEWISE_INDENT_NO,SCIRD.INDENT_NO ,min(SCIRD.REQ_SITE) as REQ_SITE,min(S.SITE_NAME) as SITE_NAME,min(SCIRD.CREATION_DATE) as CREATION_DATE from SUMADHURA_CNTL_INDENT_REQ_DTLS SCIRD, "
			+ "SITE S,SUMADHURA_INDENT_CREATION SIC "
			+ "where SCIRD.REQ_SITE = S.SITE_ID and  "+
			"SCIRD.SENDER_PENDING > 0  "+
			"and  "+
			"SCIRD.STATUS = 'A' and SCIRD.SENDER_SITE = ? and SIC.INDENT_CREATION_ID=SCIRD.INDENT_NO ");
		if(siteId.length()!=0){
			query.append(" and  SCIRD.REQ_SITE='"+siteId+"' ");	
		}
		if(siteWiseIndentNo.length()!=0){
			query.append(" and  SIC.SITEWISE_INDENT_NO='"+siteWiseIndentNo+"' ");
		}
		
		
		query.append(" GROUP BY SCIRD.INDENT_NO ");
 
		dbIndentDts = jdbcTemplate.queryForList(query.toString(), new Object[] {centralDeptId});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setIndentNumber(Integer.parseInt(prods.get("INDENT_NO")==null ? "0" : prods.get("INDENT_NO").toString()));
			String strCreateDate = prods.get("CREATION_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("CREATION_DATE").toString();
			//	String strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			Date createDate = null;
			//	Date requiredDate = null;
			try {
				createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strCreateDate);
				//	requiredDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strRequiredDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			strCreateDate = new SimpleDateFormat("dd-MM-yyyy").format(createDate);
			//	strRequiredDate = new SimpleDateFormat("dd-MM-yyyy").format(requiredDate);
			indentCreationBean.setStrCreateDate(strCreateDate);
			//	indentCreationBean.setStrRequiredDate(strRequiredDate);
			//	indentCreationBean.setIndentFrom(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());
			indentCreationBean.setSiteId(Integer.parseInt(prods.get("REQ_SITE")==null ? "" :   prods.get("REQ_SITE").toString()));
			indentCreationBean.setSiteName(prods.get("SITE_NAME")==null ? "" :   prods.get("SITE_NAME").toString());
			indentCreationBean.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" :   prods.get("SITEWISE_INDENT_NO").toString()));
			//System.out.println("%@"+indentCreationBean.getSiteWiseIndentNo());
			list.add(indentCreationBean);
		}
		return list;
	}
	@Override
	public List<IndentCreationBean> getViewAllMyRequestIndents(String centralDeptId,int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		//String query = "select distinct(INDENT_NO) AS INDENT_NO ,REQ_SITE,CREATION_DATE from SUMADHURA_CNTL_INDENT_REQ_DTLS where  STATUS = 'A' and SENDER_SITE = ? ";

		String query = "select SIC.SITEWISE_INDENT_NO,SCIRD.INDENT_NO ,SCIRD.REQ_SITE,S.SITE_NAME,SCIRD.CREATION_DATE from SUMADHURA_CNTL_INDENT_REQ_DTLS SCIRD, "
			+ "SITE S,SUMADHURA_INDENT_CREATION SIC where "
			+ "SCIRD.REQ_SITE = S.SITE_ID and  "+
			"SCIRD.SENDER_PENDING > 0  "+
			"and  "+
			"SCIRD.STATUS = 'A' and SCIRD.SENDER_SITE = ? and SCIRD.INDENT_NO = ? and SIC.INDENT_CREATION_ID = SCIRD.INDENT_NO ";

		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {centralDeptId,indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setIndentNumber(Integer.parseInt(prods.get("INDENT_NO")==null ? "0" : prods.get("INDENT_NO").toString()));
			String strCreateDate = prods.get("CREATION_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("CREATION_DATE").toString();
			//	String strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			Date createDate = null;
			//	Date requiredDate = null;
			try {
				createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strCreateDate);
				//	requiredDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strRequiredDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			strCreateDate = new SimpleDateFormat("dd-MM-yyyy").format(createDate);
			//	strRequiredDate = new SimpleDateFormat("dd-MM-yyyy").format(requiredDate);
			indentCreationBean.setStrCreateDate(strCreateDate);
			//	indentCreationBean.setStrRequiredDate(strRequiredDate);
			//	indentCreationBean.setIndentFrom(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());
			indentCreationBean.setSiteId(Integer.parseInt(prods.get("REQ_SITE")==null ? "" :   prods.get("REQ_SITE").toString()));
			indentCreationBean.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "" :   prods.get("SITEWISE_INDENT_NO").toString()));
			indentCreationBean.setSiteName(prods.get("SITE_NAME")==null ? "" :   prods.get("SITE_NAME").toString());

			list.add(indentCreationBean);
			break;
		}
		return list;
	}
	// view my request details
	@Override
	public List<IndentCreationBean> getViewissuedIndentDetailsLists(int indentNumber,String strSiteId) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		//	 HttpServletRequest request=null;
		//	HttpSession session = request.getSession(true);
		//	String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

		int strSerialNumber = 0;
		String query = "SELECT P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,"+
		"SCIRD.PRODUCT_ID,SCIRD.SUB_PRODUCT_ID,SCIRD.CHILD_PRODUCT_ID,SCIRD.MEASUREMENT_ID,IA.PRODUT_QTY,"+
		"SCIRD.REQ_QUANTITY,SCIRD.SENDER_ISSUED_QUANTITY,SCIRD.SENDER_PENDING,SCIRD.INDENT_PROCESS_ID,  SCIRD.CNTL_INDENT_REQ_DTLS_SEQ,SCIP.INDENT_CREATION_DETAILS_ID  FROM SUMADHURA_CNTL_INDENT_REQ_DTLS SCIRD, PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST,INDENT_AVAILABILITY IA "+ 
		" ,SUMADHURA_CNTL_INDENT_PROCESS SCIP WHERE SCIRD.INDENT_PROCESS_ID = SCIP.INDENT_PROCESS_ID and SCIRD.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID AND SCIRD.PRODUCT_ID=P.PRODUCT_ID AND SCIRD.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SCIRD.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID AND SCIRD.MEASUREMENT_ID=MST.MEASUREMENT_ID AND SCIRD.INDENT_NO = ?" +
		"   and SCIRD.SENDER_SITE = ? and IA.CHILD_PRODUCT_ID = SCIRD.CHILD_PRODUCT_ID and IA.SITE_ID = ?";


		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber,strSiteId,strSiteId});
		for(Map<String, Object> prods : dbIndentDts) {

			IndentCreationBean indentCreationBean = new IndentCreationBean();

			indentCreationBean.setProductId1(prods.get("PRODUCT_ID")==null ? "" :   prods.get("PRODUCT_ID").toString());
			indentCreationBean.setSubProductId1(prods.get("SUB_PRODUCT_ID")==null ? "" :   prods.get("SUB_PRODUCT_ID").toString());
			String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
			indentCreationBean.setChildProductId1(childProductId);
			indentCreationBean.setUnitsOfMeasurementId1(prods.get("MEASUREMENT_ID")==null ? "" :   prods.get("MEASUREMENT_ID").toString());
			indentCreationBean.setProduct1(prods.get("PRODUCT_NAME")==null ? "" :   prods.get("PRODUCT_NAME").toString());
			indentCreationBean.setSubProduct1(prods.get("SUB_PRODUCT_NAME")==null ? "" :   prods.get("SUB_PRODUCT_NAME").toString());
			indentCreationBean.setChildProduct1(prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString());
			indentCreationBean.setUnitsOfMeasurement1(prods.get("MEASUREMENT_NAME")==null ? "" :   prods.get("MEASUREMENT_NAME").toString());
			indentCreationBean.setRequiredQuantity1(prods.get("REQ_QUANTITY")==null ? "" :   prods.get("REQ_QUANTITY").toString());
			indentCreationBean.setIssuedquantity(prods.get("SENDER_ISSUED_QUANTITY")==null ? "" :   prods.get("SENDER_ISSUED_QUANTITY").toString());
			indentCreationBean.setPendingQuantity(prods.get("SENDER_PENDING")==null ? "" :   prods.get("SENDER_PENDING").toString());
			indentCreationBean.setCentralIndentReqDtlsId(Integer.parseInt(prods.get("CNTL_INDENT_REQ_DTLS_SEQ")==null ? "0" :   prods.get("CNTL_INDENT_REQ_DTLS_SEQ").toString()));
			indentCreationBean.setIndentCreationDetailsId(Integer.parseInt(prods.get("INDENT_CREATION_DETAILS_ID")==null ? "0" :   prods.get("INDENT_CREATION_DETAILS_ID").toString()));


			indentCreationBean.setIndentProcessId(Integer.parseInt(prods.get("INDENT_PROCESS_ID")==null ? "" :   prods.get("INDENT_PROCESS_ID").toString()));
			indentCreationBean.setIndentAvailability(prods.get("PRODUT_QTY")==null ? "" :   prods.get("PRODUT_QTY").toString());
			strSerialNumber++;
			indentCreationBean.setStrSerialNumber(String.valueOf(strSerialNumber));
			list.add(indentCreationBean);
		}	
		return list;
	}

	@Override
	public int updateCentralProcessReqDtlsTable( int site_id, String user_id,IndentCreationBean indentCreationBean,int indentNumber){
		//System.out.println(indentNumber);
		//int num=Integer.parseInt((request.getAttribute("nums")).toString());
		//int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
		//int issuedQuantity=Integer.parseInt(request.getParameter("issuedquantity"+num));
		//	int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));

		String issuedQuantity = indentCreationBean.getAllocatedQuantity();
		int indentprocessId = indentCreationBean.getIndentProcessId();
		int centralIndentReqDtlsId = indentCreationBean.getCentralIndentReqDtlsId();

		//String strStatus = indentCreationBean.getStatus();
		String query = "update SUMADHURA_CNTL_INDENT_REQ_DTLS set SENDER_INTIATED_TRANS_MODE = SENDER_INTIATED_TRANS_MODE + " +issuedQuantity+
		",SENDER_PENDING = SENDER_PENDING - "+issuedQuantity+",RECEIVER_PENDING = RECEIVER_PENDING + "+issuedQuantity+" "
		+ " where INDENT_NO = ? and CNTL_INDENT_REQ_DTLS_SEQ = ? and SENDER_SITE = ?";

		int result = jdbcTemplate.update(query, new Object[]  {indentNumber,centralIndentReqDtlsId,site_id});
		return result;
	}

	@Override
	public List<Map<String, Object>> getIndentCreatedEmpName(String reqSiteId, int indentNumber) {
		String query="SELECT SED.EMP_EMAIL,SED.DEPT_ID,SED.MOBILE_NUMBER,SED.USER_PROFILE,SED.EMP_NAME,SICAD.PURPOSE from  SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED "
				+ " where SICAD.INDENT_CREATION_ID = ? AND SED.EMP_ID=SICAD.INDENT_CREATE_APPROVE_EMP_ID";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query,indentNumber);
		
		return list;
	}
	
	public int getRejectedDetails(Model model, HttpServletRequest request, int site_id, String user_id){
		//System.out.println(indentNumber);

		int num=Integer.parseInt((request.getAttribute("nums")).toString());
		int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
		int issuedQuantity=Integer.parseInt(request.getParameter("issuedquantity"+num));
		int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));


		String query = "update SUMADHURA_CNTL_INDENT_REQ_DTLS set SENDER_INTIATED_TRANS_MODE = SENDER_INTIATED_TRANS_MODE - " +issuedQuantity+
		",SENDER_PENDING = SENDER_PENDING + "+issuedQuantity+",RECEIVER_PENDING = RECEIVER_PENDING - "+issuedQuantity+" where INDENT_NO = ? and INDENT_PROCESS_ID = ? and SENDER_SITE = ? ";


		int result = jdbcTemplate.update(query, new Object[]  {indentNumber,indentprocessId,site_id});



		String query1="update SUMADHURA_CNTL_INDENT_REQ_DTLS set STATUS='I' where STATUS='A' and INDENT_PROCESS_ID= ? ";
		jdbcTemplate.update(query1, new Object[]  {indentprocessId});



		//System.out.println("query exceuted");
		return result;
	}

	@Override
	public List<IndentCreationBean> getViewReceiveIndentDetails(String siteId) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		//	String query = "select distinct(INDENT_PROCESS_ID) AS INDENT_PROCESS_ID ,PROCESS_INTIATED_SITE,CREATION_DATE from SUMADHURA_CNTL_INDENT_REQ_DTLS where  STATUS = 'A'";

		String query = "select Distinct(INDENT_NO) AS INDENT_NO ,SENDER_SITE,   to_char(CREATION_DATE,'dd-MM-yy') from SUMADHURA_CNTL_INDENT_REQ_DTLS where "+
		" RECEIVER_PENDING > 0 " +
		" and " +
		"STATUS = 'A' and SENDER_SITE = ?" ;

		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {siteId});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setIndentNumber(Integer.parseInt(prods.get("INDENT_NO")==null ? "0" : prods.get("INDENT_NO").toString()));
			String strCreateDate = prods.get("CREATION_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("CREATION_DATE").toString();
			//	String strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			Date createDate = null;
			//	Date requiredDate = null;
			try {
				createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strCreateDate);
				//	requiredDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strRequiredDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			strCreateDate = new SimpleDateFormat("dd-MM-yyyy").format(createDate);
			//	strRequiredDate = new SimpleDateFormat("dd-MM-yyyy").format(requiredDate);
			indentCreationBean.setStrCreateDate(strCreateDate);
			//	indentCreationBean.setStrRequiredDate(strRequiredDate);
			//	indentCreationBean.setIndentFrom(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());
			indentCreationBean.setSiteName(prods.get("SENDER_SITE")==null ? "" :   prods.get("SENDER_SITE").toString());
			indentCreationBean.setPurpose("PO");
			list.add(indentCreationBean);
		}
		return list;
	}

	@Override
	public int updateIndentCreationDetials(double issuedQuantity,int indentCreationDetailId){


		String query = "update SUMADHURA_INDENT_CREATION_DTLS set RECEIVE_QUANTITY = RECEIVE_QUANTITY + "+issuedQuantity+" where INDENT_CREATION_DETAILS_ID = ?";

		int result = jdbcTemplate.update(query, new Object[]  {indentCreationDetailId});




		return result;
	}

	@Override
	public List<IndentCreationBean> getAndUpdateCentralIndentRequestDetails(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;

		int strSerialNumber = 0;
		double intRequestQuantity  = 0;
		double intIssuesQuantity  = 0;
		double centralIndentSeqId  = 0;


		String query = "SELECT  SCIRD.PRODUCT_ID,SCIRD.SUB_PRODUCT_ID,SCIRD.CHILD_PRODUCT_ID,SCIRD.MEASUREMENT_ID, "+
		" SCIRD.REQ_QUANTITY,SCIRD.SENDER_ISSUED_QUANTITY,SCIRD.RECEIVER_PENDING,SCIRD.INDENT_PROCESS_ID,SCIRD.CNTL_INDENT_REQ_DTLS_SEQ "+
		" FROM SUMADHURA_CNTL_INDENT_REQ_DTLS SCIRD where "+
		" SCIRD.INDENT_NO = ? ";


		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {

			IndentCreationBean indentCreationBean = new IndentCreationBean();

			intRequestQuantity =Double.parseDouble(prods.get("REQ_QUANTITY")==null ? "0" :   prods.get("REQ_QUANTITY").toString());
			intIssuesQuantity = Double.parseDouble(prods.get("SENDER_ISSUED_QUANTITY")==null ? "0" :   prods.get("SENDER_ISSUED_QUANTITY").toString());
			centralIndentSeqId = Double.parseDouble(prods.get("CNTL_INDENT_REQ_DTLS_SEQ")==null ? "0" :   prods.get("CNTL_INDENT_REQ_DTLS_SEQ").toString());

			if(intIssuesQuantity >= intRequestQuantity){
				query = "update SUMADHURA_CNTL_INDENT_REQ_DTLS set STATUS = 'I' where CNTL_INDENT_REQ_DTLS_SEQ = ? ";
				jdbcTemplate.update(query, new Object[]  {centralIndentSeqId});

			}



			//	list.add(indentCreationBean);
		}	
		return list;
	}


	@Override
	public List<IndentCreationBean> getAndUpdateCentralIndentProcess(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;


		int intPendingQuantity  = 0;
		int intIndentProcessId  = 0;



		String query = " select SCIP.INDENT_PROCESS_ID ,SCIP.PENDING_QUANTIY from SUMADHURA_CNTL_INDENT_PROCESS SCIP, SUMADHURA_CNTL_INDENT_REQ_DTLS SCIRD "+
		"  where  SCIRD.INDENT_PROCESS_ID = SCIP.INDENT_PROCESS_ID and SCIRD.INDENT_NO = ?";


		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {

			IndentCreationBean indentCreationBean = new IndentCreationBean();

			//intRequestQuantity = Integer.parseInt(prods.get("REQ_QUANTITY")==null ? "0" :   prods.get("REQ_QUANTITY").toString());
			intPendingQuantity = Integer.parseInt(prods.get("SCIP.PENDING_QUANTIY")==null ? "0" :   prods.get("SCIP.PENDING_QUANTIY").toString());
			intIndentProcessId = Integer.parseInt(prods.get("INDENT_PROCESS_ID")==null ? "0" :   prods.get("INDENT_PROCESS_ID").toString());

			if(intPendingQuantity == 0){
				query = "update SUMADHURA_CNTL_INDENT_PROCESS set STATUS = 'I' where INDENT_PROCESS_ID = ?";
				jdbcTemplate.update(query, new Object[]  {intIndentProcessId});

			}



			//	list.add(indentCreationBean);
		}	
		return list;
	}


	@Override
	public List<IndentCreationBean> getAndUpdateIndentNo(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;

		int strSerialNumber = 0;
		double intRequestQuantity  = 0;
		double intIssuesQuantity  = 0;
		int centralIndentSeqId  = 0;
		String strStatus = "I";

		String query = "   select REQ_QUANTITY,RECEIVE_QUANTITY from SUMADHURA_INDENT_CREATION_DTLS SICD,SUMADHURA_INDENT_CREATION SIC "+
		"  where SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID and SIC.INDENT_CREATION_ID = ?";


		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {

			IndentCreationBean indentCreationBean = new IndentCreationBean();

			intRequestQuantity =Double.parseDouble(prods.get("REQ_QUANTITY")==null ? "0" :   prods.get("REQ_QUANTITY").toString());
			intIssuesQuantity =Double.parseDouble(prods.get("RECEIVE_QUANTITY")==null ? "0" :   prods.get("RECEIVE_QUANTITY").toString());

			if(intIssuesQuantity < intRequestQuantity){
				strStatus = "A";
				break;
			}



			//	list.add(indentCreationBean);
		}	


		if(strStatus.equals("I")){

			query = "	 update SUMADHURA_INDENT_CREATION set STATUS = 'I', MODIFYDATE= sysdate where INDENT_CREATION_ID = ?";
			jdbcTemplate.update(query, new Object[]  {indentNumber});


		}

		return list;
	}

	@Override
	public int getReceivedDetails(IndentCreationBean indentCreationBean, int site_id, String user_id){
		//System.out.println(indentNumber);
		//int result1=0;
		//int num=Integer.parseInt((request.getAttribute("nums")).toString());
		//	int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
		//int issuedQuantity=Integer.parseInt(request.getParameter("issuedquantity"+num));
		//int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));

		double issuedQuantity =  Double.valueOf(indentCreationBean.getIssuedquantity() == null ? "0" : indentCreationBean.getIssuedquantity().toString());


		int indentprocessId = indentCreationBean.getIndentProcessId();

		String query = "update SUMADHURA_CNTL_INDENT_PROCESS set ALLOCATED_QUANTITY = ALLOCATED_QUANTITY + " +issuedQuantity+
		",PENDING_QUANTIY = PENDING_QUANTIY - "+issuedQuantity+",INTIATED_QUANTITY = INTIATED_QUANTITY - "+issuedQuantity+" where INDENT_PROCESS_ID = ? ";


		int result = jdbcTemplate.update(query, new Object[]  {indentprocessId});


		//System.out.println("query exceuted");
		return result;
	}

	public int updateReceivedDetails(double issuedQuantity,int intCentralIndentReqDtlsId){

		/*	String query = "update SUMADHURA_CNTL_INDENT_REQ_DTLS set SENDER_ISSUED_QUANTITY = SENDER_ISSUED_QUANTITY + " +issuedQuantity+
		",RECEIVER_PENDING = RECEIVER_PENDING - "+issuedQuantity+ ",SENDER_INTIATED_TRANS_MODE = SENDER_INTIATED_TRANS_MODE - "+issuedQuantity+
		",SENDER_PENDING = SENDER_PENDING - "+issuedQuantity+"  where  CNTL_INDENT_REQ_DTLS_SEQ = ?";
		 */

		String query = "update SUMADHURA_CNTL_INDENT_REQ_DTLS set SENDER_ISSUED_QUANTITY = SENDER_ISSUED_QUANTITY + " +issuedQuantity+
		",RECEIVER_PENDING = RECEIVER_PENDING - "+issuedQuantity+ ",SENDER_INTIATED_TRANS_MODE = SENDER_INTIATED_TRANS_MODE - "+issuedQuantity+
		"  where  CNTL_INDENT_REQ_DTLS_SEQ = ?";

		int result = jdbcTemplate.update(query, new Object[]  {intCentralIndentReqDtlsId});




		return result;
	}



	@Override
	public List<IndentCreationBean> getViewReceiveDetails(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		//String query = "select distinct(INDENT_PROCESS_ID) AS INDENT_PROCESS_ID ,PROCESS_INTIATED_SITE,CREATION_DATE from SUMADHURA_CNTL_INDENT_REQ_DTLS where  STATUS = 'A' and INDENT_PROCESS_ID= ? ";

		String query = "select distinct(INDENT_NO) AS INDENT_NO ,REQ_SITE,to_char(CREATION_DATE,'dd-MM-yy') "+
		" from SUMADHURA_CNTL_INDENT_REQ_DTLS where  "+
		" STATUS = 'A' and INDENT_NO= ? ";

		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setIndentNumber(Integer.parseInt(prods.get("INDENT_NO")==null ? "0" : prods.get("INDENT_NO").toString()));
			String strCreateDate = prods.get("CREATION_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("CREATION_DATE").toString();
			//	String strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			Date createDate = null;
			//	Date requiredDate = null;
			try {
				createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strCreateDate);
				//	requiredDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strRequiredDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			strCreateDate = new SimpleDateFormat("dd-MM-yyyy").format(createDate);
			//	strRequiredDate = new SimpleDateFormat("dd-MM-yyyy").format(requiredDate);
			indentCreationBean.setStrCreateDate(strCreateDate);
			//	indentCreationBean.setStrRequiredDate(strRequiredDate);
			//	indentCreationBean.setIndentFrom(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());
			indentCreationBean.setSiteName(prods.get("REQ_SITE")==null ? "" :   prods.get("REQ_SITE").toString());
			indentCreationBean.setPurpose("PO");
			list.add(indentCreationBean);
		}
		return list;
	}

	public int getRejectedQuantityDetails(Model model, HttpServletRequest request, int site_id, String user_id){
		//System.out.println(indentNumber);

		//	int num=Integer.parseInt((request.getAttribute("nums")).toString());
		int indentNumber= Integer.parseInt(request.getParameter("indentNumber"));
		//	int issuedQuantity=Integer.parseInt(request.getParameter("issuedquantity"+num));
		//	int indentprocessId=Integer.parseInt(request.getParameter("indentCreationDetailsId"+num));

		String query="update SUMADHURA_CNTL_INDENT_PROCESS 	set  PENDING_QUANTIY='0' where INDENT_PROCESS_ID= ? ";
		int result = jdbcTemplate.update(query, new Object[]  {indentNumber});

		if(result==1)
		{
			String query1 = "update SUMADHURA_CNTL_INDENT_REQ_DTLS set STATUS='I' where STATUS='A' and INDENT_PROCESS_ID= ? ";

			jdbcTemplate.update(query1, new Object[]  {indentNumber});
		}


		//System.out.println("query exceuted");
		return result;
	}






	@Override
	public List<IndentCreationBean> getViewReceivedIndentDetailsLists(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;

		int strSerialNumber = 0;
		String query = "SELECT P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,"+
		"SCIRD.PRODUCT_ID,SCIRD.SUB_PRODUCT_ID,SCIRD.CHILD_PRODUCT_ID,SCIRD.MEASUREMENT_ID,"+
		"SCIRD.REQ_QUANTITY,SCIRD.SENDER_ISSUED_QUANTITY,SCIRD.RECEIVER_PENDING,SCIRD.INDENT_PROCESS_ID,SCIRD.CNTL_INDENT_REQ_DTLS_SEQ,INDENT_CREATION_DETAILS_ID FROM SUMADHURA_CNTL_INDENT_REQ_DTLS SCIRD, PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST "+ 
		",SUMADHURA_CNTL_INDENT_PROCESS SCIP WHERE SCIP.INDENT_PROCESS_ID = SCIRD.INDENT_PROCESS_ID and SCIRD.PRODUCT_ID=P.PRODUCT_ID AND SCIRD.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SCIRD.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID AND SCIRD.MEASUREMENT_ID=MST.MEASUREMENT_ID AND SCIRD.INDENT_NO = ? ";


		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {

			IndentCreationBean indentCreationBean = new IndentCreationBean();

			indentCreationBean.setProductId1(prods.get("PRODUCT_ID")==null ? "" :   prods.get("PRODUCT_ID").toString());
			indentCreationBean.setSubProductId1(prods.get("SUB_PRODUCT_ID")==null ? "" :   prods.get("SUB_PRODUCT_ID").toString());
			String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
			indentCreationBean.setChildProductId1(childProductId);
			indentCreationBean.setUnitsOfMeasurementId1(prods.get("MEASUREMENT_ID")==null ? "" :   prods.get("MEASUREMENT_ID").toString());
			indentCreationBean.setProduct1(prods.get("PRODUCT_NAME")==null ? "" :   prods.get("PRODUCT_NAME").toString());
			indentCreationBean.setSubProduct1(prods.get("SUB_PRODUCT_NAME")==null ? "" :   prods.get("SUB_PRODUCT_NAME").toString());
			indentCreationBean.setChildProduct1(prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString());
			indentCreationBean.setUnitsOfMeasurement1(prods.get("MEASUREMENT_NAME")==null ? "" :   prods.get("MEASUREMENT_NAME").toString());
			indentCreationBean.setStrRequestQuantity(prods.get("REQ_QUANTITY")==null ? "" :   prods.get("REQ_QUANTITY").toString());
			indentCreationBean.setIssuedquantity(prods.get("SENDER_ISSUED_QUANTITY")==null ? "" :   prods.get("SENDER_ISSUED_QUANTITY").toString());
			indentCreationBean.setPendingQuantity(prods.get("RECEIVER_PENDING")==null ? "" :   prods.get("RECEIVER_PENDING").toString());
			indentCreationBean.setCentralIndentReqDtlsId(Integer.parseInt(prods.get("CNTL_INDENT_REQ_DTLS_SEQ")==null ? "0" :   prods.get("CNTL_INDENT_REQ_DTLS_SEQ").toString()));
			indentCreationBean.setIndentProcessId(Integer.parseInt(prods.get("INDENT_PROCESS_ID")==null ? "0" :   prods.get("INDENT_PROCESS_ID").toString()));



			indentCreationBean.setIndentCreationDetailsId(Integer.parseInt(prods.get("INDENT_CREATION_DETAILS_ID")==null ? "" :   prods.get("INDENT_CREATION_DETAILS_ID").toString()));
			strSerialNumber++;
			indentCreationBean.setStrSerialNumber(String.valueOf(strSerialNumber));
			list.add(indentCreationBean);
		}	
		return list;
	}





	@Override
	public 	List<Map<String, Object>> getCentralIndentProcessDetails( String strChildProductId ,String strMesurmentId,String StrIndentNo) {



		List<Map<String, Object>> dbIndentProcessDts = null;


		String query = " select INDENT_CREATION_DETAILS_ID,CNTL_INDENT_REQ_DTLS_SEQ ,INDENT_PROCESS_ID "+
		"from SUMADHURA_CNTL_INDENT_REQ_DTLS SCIRD, SUMADHURA_INDENT_CREATION_DTLS SICD,SUMADHURA_INDENT_CREATION SIC "+
		"where SCIRD.CHILD_PRODUCT_ID =  ? and SCIRD.MEASUREMENT_ID = ? " +
		"and SCIRD.INDENT_NO = ? and SCIRD.CHILD_PRODUCT_ID=SICD.CHILD_PRODUCT_ID AND SCIRD.MEASUREMENT_ID =SICD.MEASUREMENT_ID AND " +
		" SIC.INDENT_CREATION_ID = SCIRD.INDENT_NO and SIC.INDENT_CREATION_ID = SICD.INDENT_CREATION_ID";


		dbIndentProcessDts = jdbcTemplate.queryForList(query, new Object[] {strChildProductId,strMesurmentId,StrIndentNo});

		return dbIndentProcessDts;

	}

	@Override
	public 	List<Map<String, Object>> getIndentCreationDetails( int intIndentNumber,String aprroverTo) {



		List<Map<String, Object>> dbIndentProcessDts = null;
		String query = "";
		if(aprroverTo.equals("approvalToEmployee")){


			query = " select SITE_NAME,CREATE_DATE,EMP_EMAIL as Email_id,SCHEDULE_DATE as scheduleDate from  SITE S, SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED where "+
			"SIC.SITE_ID = S.SITE_ID and SIC.PENDING_EMP_ID = SED.EMP_ID and SIC.INDENT_CREATION_ID = ?";

		}else{
			query = " select SITE_NAME,CREATE_DATE,DEPT_EMAIL as Email_id,SCHEDULE_DATE as scheduleDate from  SITE S, SUMADHURA_INDENT_CREATION SIC,SUMADHURA_DEPARTMENT_DETAILS SDD where "+
			" SIC.SITE_ID = S.SITE_ID and SIC.PENDIND_DEPT_ID = SDD.DEPT_ID and SIC.INDENT_CREATION_ID = ? ";

		}

		dbIndentProcessDts = jdbcTemplate.queryForList(query, new Object[] {intIndentNumber});

		return dbIndentProcessDts;

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


				if(!strEmailId.equals("")){
					if(strEmailId.contains(",")){
						for(String strEmailId1 : strEmailId.split(","))
						{
							objList.add(strEmailId1);
						}
					}
					else{
						objList.add(strEmailId);
					}
				}

			}	
		}

		return objList;
	}
	@Override
	public int getIndentCreationApprovalSequenceNumber() {
		int indentCreationDetailsSeqNum = jdbcTemplate.queryForInt("SELECT INDENT_CREATION_APPROVAL_SEQ.NEXTVAL FROM DUAL");
		return indentCreationDetailsSeqNum;
	}

	@Override
	public int insertIndentCreationApprovalAsApprove(int indentCreationApprovalSeqNum, int indentNumber,
			IndentCreationDto indentCreationDto) {
		String query = "INSERT INTO SUM_INT_CREATION_APPROVAL_DTLS(INT_CREATION_APPROVAL_DTLS_ID ,INDENT_CREATION_ID ,"
			+ "INDENT_TYPE ,creation_date ,SITE_ID ,INDENT_CREATE_APPROVE_EMP_ID, PURPOSE  ) "+
			"VALUES(?, ?, ?, sysdate, ?, ?, ?)";
		int result = jdbcTemplate.update(query, new Object[] {
				indentCreationApprovalSeqNum, 	indentNumber,
				"A", indentCreationDto.getSiteId(),indentCreationDto.getUserId(),indentCreationDto.getPurpose()
		});
		return result;
	}

	@Override
	public int updateIndentCreation(int indentCreationSeqNum, String pendingEmpId, String pendingDeptId,IndentCreationDto indentCreationDto,String strFinalChangedComments) {

		int result = 0;
		if(strFinalChangedComments.equals("approving from Mail")){
			String query = "UPDATE SUMADHURA_INDENT_CREATION set PENDING_EMP_ID = ?, MODIFYDATE= sysdate , PENDIND_DEPT_ID = ? ,TEMPPASS = ?   "+
			"WHERE INDENT_CREATION_ID = ?";
			result = jdbcTemplate.update(query, new Object[] {
					pendingEmpId, pendingDeptId, indentCreationDto.getTempPass(), indentCreationSeqNum
			});
		}else{
			String query = "UPDATE SUMADHURA_INDENT_CREATION set PENDING_EMP_ID = ?, MODIFYDATE= sysdate , PENDIND_DEPT_ID = ? ,TEMPPASS = ? , METERIAL_EDIT_COMMENT = ? "+
			"WHERE INDENT_CREATION_ID = ?";
			result = jdbcTemplate.update(query, new Object[] {
					pendingEmpId, pendingDeptId, indentCreationDto.getTempPass(),strFinalChangedComments,indentCreationSeqNum
			});
		}






		return result;
	}
	@Override
	public int updateIndentCreation(int indentCreationSeqNum, String pendingEmpId, String pendingDeptId) {
		String query = "UPDATE SUMADHURA_INDENT_CREATION set PENDING_EMP_ID = ? , MODIFYDATE= sysdate , PENDIND_DEPT_ID = ? "+
		"WHERE INDENT_CREATION_ID = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				pendingEmpId, pendingDeptId, indentCreationSeqNum
		});
		return result;
	}

	@Override
	public IndentCreationBean getIndentFromAndToDetails(String user_id, IndentCreationBean icb,int SiteId) {
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select ((SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID = ?)) as emp_name, "
			+"SED.EMP_NAME as approver_name,SED.EMP_ID as approver_id FROM SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_APPROVER_MAPPING_DTL  SAMD WHERE SED.EMP_ID = SAMD.APPROVER_EMP_ID and "
			+"SAMD.EMP_ID = ? AND SAMD.MODULE_TYPE='INDENT' AND SAMD.SITE_ID=?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id,user_id,SiteId});

		for(Map<String, Object> prods : dbIndentDts) {
			icb.setApproverEmpId(prods.get("APPROVER_ID")==null ? "" :   prods.get("APPROVER_ID").toString());
			icb.setIndentFrom(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());		
			icb.setIndentTo(prods.get("APPROVER_NAME")==null ? "" :   prods.get("APPROVER_NAME").toString());			}

		return icb;
	}
	@Override
	public int getIndentCreationSequenceNumber() {
		int indentCreationSeqNum = jdbcTemplate.queryForInt("SELECT INDENT_CREATION_SEQ.NEXTVAL FROM DUAL");
		return indentCreationSeqNum;
	}
	@Override
	public List<IndentCreationBean> getIndentFromAndToDetails(String centralDeptId) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT SIC.SITEWISE_INDENT_NO,SIC.INDENT_CREATION_ID ,SIC.CREATE_DATE, SED.EMP_NAME ,S.SITE_NAME,S.SITE_ID , REQUIRED_DATE ,SIC.INDENT_NAME "+
		"FROM SUMADHURA_INDENT_CREATION SIC, SUMADHURA_EMPLOYEE_DETAILS SED, SITE S "+
		"WHERE  SIC.PENDING_EMP_ID = '-' and SIC.PENDIND_DEPT_ID = ? and SIC.STATUS = 'A' AND "+
		"SIC.INDENT_CREATE_EMP_ID = SED.EMP_ID AND SIC.SITE_ID = S.SITE_ID  and SIC.SITE_ID != '999' "; //and SIC.SITE_ID != '999' because central site create the indent. It should not come to the cemtral llop directly forward to Purchase dept. In Central site  pool it will not visible
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {centralDeptId});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setIndentNumber(Integer.parseInt(prods.get("INDENT_CREATION_ID")==null ? "0" : prods.get("INDENT_CREATION_ID").toString()));
			String strCreateDate = prods.get("CREATE_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("CREATE_DATE").toString();
			String strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			Date createDate = null;
			Date requiredDate = null;
			try {
				createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strCreateDate);
				requiredDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strRequiredDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			strCreateDate = new SimpleDateFormat("dd-MM-yyyy").format(createDate);
			strRequiredDate = new SimpleDateFormat("dd-MM-yyyy").format(requiredDate);
			indentCreationBean.setStrCreateDate(strCreateDate);
			indentCreationBean.setStrRequiredDate(strRequiredDate);
			indentCreationBean.setIndentFrom(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());
			indentCreationBean.setIndentName(prods.get("INDENT_NAME")==null ? "-" :   prods.get("INDENT_NAME").toString());
			indentCreationBean.setSiteName(prods.get("SITE_NAME")==null ? "" :   prods.get("SITE_NAME").toString());
			indentCreationBean.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "" :   prods.get("SITE_ID").toString()));
			indentCreationBean.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" :   prods.get("SITEWISE_INDENT_NO").toString()));
			indentCreationBean.setPurpose("PO");
			list.add(indentCreationBean);
		}
		return list;
	}
	@Override
	public List<IndentCreationBean> getIndentCreationDetailsLists(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		int strSerialNumber = 0;
		String query = "SELECT P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,CP.MATERIAL_GROUP_ID,MST.NAME as MEASUREMENT_NAME,"+
		"SICD.PRODUCT_ID,SICD.SUB_PRODUCT_ID,SICD.CHILD_PRODUCT_ID,SICD.MEASUREMENT_ID,"+
		"SICD.REQ_QUANTITY,SICD.REMARKS,SICD.INDENT_CREATION_DETAILS_ID FROM SUMADHURA_INDENT_CREATION_DTLS SICD, PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST "+
		"WHERE SICD.PRODUCT_ID=P.PRODUCT_ID AND SICD.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SICD.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID "+
		"AND SICD.MEASUREMENT_ID=MST.MEASUREMENT_ID AND SICD.INDENT_CREATION_ID= ? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();

			indentCreationBean.setProductId1(prods.get("PRODUCT_ID")==null ? "" :   prods.get("PRODUCT_ID").toString());
			indentCreationBean.setSubProductId1(prods.get("SUB_PRODUCT_ID")==null ? "" :   prods.get("SUB_PRODUCT_ID").toString());
			indentCreationBean.setChildProductId1(prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString());
			indentCreationBean.setUnitsOfMeasurementId1(prods.get("MEASUREMENT_ID")==null ? "" :   prods.get("MEASUREMENT_ID").toString());
			indentCreationBean.setProduct1(prods.get("PRODUCT_NAME")==null ? "" :   prods.get("PRODUCT_NAME").toString());
			indentCreationBean.setSubProduct1(prods.get("SUB_PRODUCT_NAME")==null ? "" :   prods.get("SUB_PRODUCT_NAME").toString());
			indentCreationBean.setChildProduct1(prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString());
			indentCreationBean.setUnitsOfMeasurement1(prods.get("MEASUREMENT_NAME")==null ? "" :   prods.get("MEASUREMENT_NAME").toString());
			indentCreationBean.setRequiredQuantity1(prods.get("REQ_QUANTITY")==null ? "" :   prods.get("REQ_QUANTITY").toString());
			indentCreationBean.setGroupId1(prods.get("MATERIAL_GROUP_ID")==null ? "" :   prods.get("MATERIAL_GROUP_ID").toString());
			indentCreationBean.setRemarks1(prods.get("REMARKS")==null ? "" :   prods.get("REMARKS").toString());
			indentCreationBean.setIndentCreationDetailsId(Integer.parseInt(prods.get("INDENT_CREATION_DETAILS_ID")==null ? "" :   prods.get("INDENT_CREATION_DETAILS_ID").toString()));
			strSerialNumber++;
			indentCreationBean.setStrSerialNumber(String.valueOf(strSerialNumber));
			list.add(indentCreationBean);
		}	
		return list;
	}
	@Override
	public boolean checkIndentNumberIsValidForEmployee(int indentNumber, String user_id) {
		int count = jdbcTemplate.queryForInt("SELECT count(1) FROM SUMADHURA_INDENT_CREATION WHERE INDENT_CREATION_ID = '"+indentNumber+"' AND PENDING_EMP_ID = '"+user_id+"' AND STATUS='A'");
		if(count>0){return true;}
		else{return false;}
	}
	@Override
	public List<IndentCreationBean> getIndentCreationLists(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		String strPendingEmployeeName = "";
		String strPendingDeptName = "";

		String query = "SELECT SIC.VERSION_NO,SIC.REFERENCE_NO,SIC.ISSUE_DATE, SIC.SITEWISE_INDENT_NO,SIC.SCHEDULE_DATE ,SIC.REQUIRED_DATE, SIC.INDENT_CREATE_EMP_ID ,SIC.PENDING_EMP_ID, S.SITE_NAME,S.SITE_ID,SIC.INDENT_NAME, "+
		"( SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=SIC.INDENT_CREATE_EMP_ID and INDENT_CREATION_ID = ?) as INDENT_CREATE_EMP_NAME, "+
		"( SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=SIC.PENDING_EMP_ID and INDENT_CREATION_ID = ?) as PENDING_EMP_NAME,METERIAL_EDIT_COMMENT , "+
		"( SELECT SDD.DEPT_NAME FROM SUMADHURA_DEPARTMENT_DETAILS SDD WHERE SDD.DEPT_ID=SIC.PENDIND_DEPT_ID and INDENT_CREATION_ID = ?) as PENDING_DEPT_NAME "+
		" FROM SUMADHURA_INDENT_CREATION SIC, SITE S WHERE SIC.INDENT_CREATION_ID = ? AND S.SITE_ID = SIC.SITE_ID";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {
				indentNumber,indentNumber,indentNumber,indentNumber
		});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setIndentNumber(indentNumber);

			indentCreationBean.setVersionNo(prods.get("VERSION_NO")==null ? "" :   prods.get("VERSION_NO").toString());
			indentCreationBean.setReference_No(prods.get("REFERENCE_NO")==null ? "" :   prods.get("REFERENCE_NO").toString());
			indentCreationBean.setIssue_date(prods.get("ISSUE_DATE")==null ? "" :   prods.get("ISSUE_DATE").toString());

			String strScheduleDate = prods.get("SCHEDULE_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("SCHEDULE_DATE").toString();
			String strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			Date scheduleDate = null;
			Date requiredDate = null;
			try {
				scheduleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strScheduleDate);
				requiredDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strRequiredDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			strScheduleDate = new SimpleDateFormat("dd-MMM-yy").format(scheduleDate);
			strRequiredDate = new SimpleDateFormat("dd-MMM-yy").format(requiredDate);
			indentCreationBean.setStrScheduleDate(strScheduleDate);
			indentCreationBean.setStrRequiredDate(strRequiredDate);
			indentCreationBean.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" :   prods.get("SITEWISE_INDENT_NO").toString()));
			indentCreationBean.setIndentFrom(prods.get("INDENT_CREATE_EMP_NAME")==null ? "" :   prods.get("INDENT_CREATE_EMP_NAME").toString());

			indentCreationBean.setSiteName(prods.get("SITE_NAME")==null ? "" :   prods.get("SITE_NAME").toString());
			indentCreationBean.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "" :   prods.get("SITE_ID").toString()));
			indentCreationBean.setIndentNumber(indentNumber);
			indentCreationBean.setMaterialEditComment(prods.get("METERIAL_EDIT_COMMENT")==null ? "" :   prods.get("METERIAL_EDIT_COMMENT").toString());
			indentCreationBean.setIndentName(prods.get("INDENT_NAME")==null ? "-" :   prods.get("INDENT_NAME").toString());//added by ravi to know the which indent
			
			strPendingEmployeeName = prods.get("PENDING_EMP_NAME")==null ? "" :   prods.get("PENDING_EMP_NAME").toString();

			strPendingDeptName = prods.get("PENDING_DEPT_NAME")==null ? "" :   prods.get("PENDING_DEPT_NAME").toString();


			if(strPendingEmployeeName.equals("")){
				indentCreationBean.setIndentTo(strPendingDeptName);
			}else{
				indentCreationBean.setIndentTo(strPendingEmployeeName);
			}






			//indentCreationBean.setPurpose("PO");
			list.add(indentCreationBean);
		}
		List<Map<String, Object>> dbIndentDts2 = null;
		String query2 = "SELECT SED.EMP_NAME,SICAD.PURPOSE from  SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED "
			+ " where SICAD.INDENT_CREATION_ID = ? AND SED.EMP_ID=SICAD.INDENT_CREATE_APPROVE_EMP_ID";
		dbIndentDts2 = jdbcTemplate.queryForList(query2, new Object[] {
				indentNumber
		});
		String purposeView="";
		for(Map<String, Object> prods2 : dbIndentDts2) {
			String empName = prods2.get("EMP_NAME")==null ? "" : prods2.get("EMP_NAME").toString();
			String purpose = prods2.get("PURPOSE")==null ? "" : prods2.get("PURPOSE").toString();
			purposeView+=empName+" - "+purpose+"<br>";
		}
		list.get(0).setPurpose(purposeView);
		return list;
	}

	@Override
	public String getTempPasswordOfIndent(int indentNumber) {
		String query = "SELECT TEMPPASS FROM SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = "+indentNumber;
		String result = jdbcTemplate.queryForObject(query,String.class);  
		return result;

	}

	@Override
	public List<ViewIndentIssueDetailsBean> getRaisedIndentDetails(String fromDate, String toDate, String siteId,String indentNumber) {

		String query = "";
		String strDCFormQuery = "";
		String strDCNumber = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<ViewIndentIssueDetailsBean> list = new ArrayList<ViewIndentIssueDetailsBean>();
		ViewIndentIssueDetailsBean indentObj = null; 
		String strPendingEmployeeName="";
		String strPendingDeptName="";
		String strPendingdeptIdName="";

		try {
			//if part is for view indent receive details,else part is for view indent issue details
			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = "SELECT DISTINCT SIC.SITEWISE_INDENT_NO,SIC.INDENT_CREATION_ID,CREATE_DATE,SIC.SCHEDULE_DATE,SIC.STATUS,SIC.SITE_ID,SIC.INDENT_NAME, "
					+ "(SELECT SDD.DEPT_NAME FROM SUMADHURA_DEPARTMENT_DETAILS SDD WHERE SDD.DEPT_ID=SIC.PENDIND_DEPT_ID ) as PENDING_DEPT_NAME,"
					+"(SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=SIC.PENDING_EMP_ID) as PENDING_EMP_NAME,SIC.PENDIND_DEPT_ID as PENDING_DEPT_IDNAME  FROM SUMADHURA_INDENT_CREATION SIC,SUMADHURA_INDENT_CREATION_DTLS SICD,VENDOR_DETAILS VD WHERE  SIC.STATUS in('A','SITELEVEL') and SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID AND SIC.SITE_ID='"+siteId+"' AND TRUNC(SIC.CREATE_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') ";
				//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			} else if (StringUtils.isNotBlank(fromDate)) {
				query = "SELECT DISTINCT SIC.SITEWISE_INDENT_NO,SIC.INDENT_CREATION_ID,CREATE_DATE,SIC.SCHEDULE_DATE,SIC.STATUS,SIC.SITE_ID,SIC.INDENT_NAME, "
					+" (SELECT SDD.DEPT_NAME FROM SUMADHURA_DEPARTMENT_DETAILS SDD WHERE SDD.DEPT_ID=SIC.PENDIND_DEPT_ID ) as PENDING_DEPT_NAME,"
					+" (SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=SIC.PENDING_EMP_ID) as PENDING_EMP_NAME,SIC.PENDIND_DEPT_ID as PENDING_DEPT_IDNAME"
					+ " FROM SUMADHURA_INDENT_CREATION SIC,SUMADHURA_INDENT_CREATION_DTLS SICD,VENDOR_DETAILS VD WHERE SIC.STATUS in('A','SITELEVEL') and SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID  AND SIC.SITE_ID='"+siteId+"' AND TRUNC(SIC.CREATE_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				;
			} else if(StringUtils.isNotBlank(toDate)) {
				query = "SELECT DISTINCT SIC.SITEWISE_INDENT_NO,SIC.INDENT_CREATION_ID,CREATE_DATE,SIC.SCHEDULE_DATE,SIC.STATUS,SIC.SITE_ID,SIC.INDENT_NAME, "
					+" (SELECT SDD.DEPT_NAME FROM SUMADHURA_DEPARTMENT_DETAILS SDD WHERE SDD.DEPT_ID=SIC.PENDIND_DEPT_ID ) as PENDING_DEPT_NAME,"
					+" (SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=SIC.PENDING_EMP_ID) as PENDING_EMP_NAME,SIC.PENDIND_DEPT_ID as PENDING_DEPT_IDNAME"
					+ " FROM SUMADHURA_INDENT_CREATION SIC,SUMADHURA_INDENT_CREATION_DTLS SICD,VENDOR_DETAILS VD WHERE SIC.STATUS in('A','SITELEVEL') and SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID AND SIC.SITE_ID='"+siteId+"' AND TRUNC(SIC.CREATE_DATE) =TO_DATE('"+toDate+"', 'dd-MM-yy')";
			}else if(StringUtils.isNotBlank(indentNumber)) {
				query = "SELECT DISTINCT SIC.SITEWISE_INDENT_NO,SIC.INDENT_CREATION_ID,CREATE_DATE,SIC.SCHEDULE_DATE,SIC.STATUS,SIC.SITE_ID,SIC.INDENT_NAME, "
					+" (SELECT SDD.DEPT_NAME FROM SUMADHURA_DEPARTMENT_DETAILS SDD WHERE SDD.DEPT_ID=SIC.PENDIND_DEPT_ID ) as PENDING_DEPT_NAME,"
					+" (SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=SIC.PENDING_EMP_ID) as PENDING_EMP_NAME,SIC.PENDIND_DEPT_ID as PENDING_DEPT_IDNAME"
					+ " FROM SUMADHURA_INDENT_CREATION SIC,SUMADHURA_INDENT_CREATION_DTLS SICD,VENDOR_DETAILS VD WHERE SIC.STATUS in('A','SITELEVEL') and SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID AND SIC.SITE_ID='"+siteId+"' and SIC.SITEWISE_INDENT_NO='"+indentNumber+"'";
			}






			dbIndentDts = template.queryForList(query, new Object[]{});

			for(Map<String, Object> prods : dbIndentDts) {
				indentObj = new ViewIndentIssueDetailsBean();

				indentObj.setSiteWiseIndentNo(prods.get("SITEWISE_INDENT_NO")==null ? "" : prods.get("SITEWISE_INDENT_NO").toString());
				indentObj.setRequesterId(prods.get("INDENT_CREATION_ID")==null ? "" : prods.get("INDENT_CREATION_ID").toString());
				indentObj.setStrInvoiceDate(prods.get("SCHEDULE_DATE")==null ? "" : prods.get("SCHEDULE_DATE").toString());
				indentObj.setStatus(prods.get("STATUS")==null ? "" : prods.get("STATUS").toString());
				indentObj.setSiteId(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
				indentObj.setIndentName(prods.get("INDENT_NAME")==null ? "-" : prods.get("INDENT_NAME").toString());
				
				String date=prods.get("SCHEDULE_DATE")==null ? "" : prods.get("SCHEDULE_DATE").toString();

				strPendingEmployeeName = prods.get("PENDING_EMP_NAME")==null ? "" :   prods.get("PENDING_EMP_NAME").toString();

				strPendingDeptName = prods.get("PENDING_DEPT_NAME")==null ? "" :   prods.get("PENDING_DEPT_NAME").toString();

				strPendingdeptIdName=prods.get("PENDING_DEPT_IDNAME")==null ? "" :   prods.get("PENDING_DEPT_IDNAME").toString();
				
				if(strPendingEmployeeName.equals("") && strPendingdeptIdName.equalsIgnoreCase("VND")){
					
					indentObj.setIndentTo(strPendingdeptIdName);
				}

				else if(strPendingEmployeeName.equals("") && ((strPendingdeptIdName.equalsIgnoreCase("998") || strPendingdeptIdName.equalsIgnoreCase("999")|| strPendingdeptIdName.equalsIgnoreCase("998_PDM")))){
					indentObj.setIndentTo(strPendingDeptName);
				}else{
					indentObj.setIndentTo(strPendingEmployeeName);
				}
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setReceivedDate(date);

				list.add(indentObj);
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

	public String getVendorDetails(String poNumber, String siteId,HttpServletRequest request,String siteName,String vedorId) throws ParseException {
		//	List<Map<String, Object>> productList = null;
		//	JdbcTemplate template;
		List<Map<String, Object>> dbIndentDts = null;
		//IndentCreationBean indentCreationBean=null;
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
		String revaddress="";
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
		String receiverContactPersonTwo="";
		String receiverMobileNumberTwo="";
		String contactPersonTwo="";
		String mobileNumberTwo="";

		if (StringUtils.isNotBlank(poNumber) ) {
			String query = "SELECT DISTINCT (STPE.INDENT_NO),STPE.PO_ENTRY_ID,STPE.SUBJECT,STPE.BILLING_ADDRESS,STPE.PO_DATE,STPE.VENDOR_ID,VD.VENDOR_CON_PER_NAME,VD.VENDOR_NAME,VD.ADDRESS, VD.MOBILE_NUMBER,VD.LANDLINE_NO,VD.GSIN_NUMBER,VD.STATE,VD.EMP_EMAIL,STPE.PO_NUMBER,(select STATE from VENDOR_DETAILS where  VENDOR_ID = ?) as ReceiverState,STPE.VERSION_NUMBER,STPE.PO_ISSUE_DATE,STPE.REFFERENCE_NO,STPE.DELIVERY_DATE,VD.VENDOR_CON_PER_NAME_TWO,VD.MOBILE_NUMBER_TWO FROM VENDOR_DETAILS VD,SUMADHURA_PO_ENTRY STPE WHERE STPE.VENDOR_ID =VD.VENDOR_ID  AND  STPE.PO_NUMBER=?";


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
				contactPerson=IndentGetBean.get("VENDOR_CON_PER_NAME") == null ? "" : IndentGetBean.get("VENDOR_CON_PER_NAME").toString();
				billingAddress=IndentGetBean.get("BILLING_ADDRESS") == null ? "-" : IndentGetBean.get("BILLING_ADDRESS").toString();
				ponumber=IndentGetBean.get("PO_NUMBER") == null ? "" : IndentGetBean.get("PO_NUMBER").toString();
				strMobileNo=IndentGetBean.get("MOBILE_NUMBER") == null ? "" : IndentGetBean.get("MOBILE_NUMBER").toString();
				strLandLine=IndentGetBean.get("LANDLINE_NO") == null ? "" : IndentGetBean.get("LANDLINE_NO").toString();
				strEmailId=IndentGetBean.get("EMP_EMAIL") == null ? "-" : IndentGetBean.get("EMP_EMAIL").toString();
				ReceiverState=IndentGetBean.get("ReceiverState") == null ? " " : IndentGetBean.get("ReceiverState").toString();
				
				version_No=IndentGetBean.get("VERSION_NUMBER") == null ? "-" : IndentGetBean.get("VERSION_NUMBER").toString();
				refference_No=IndentGetBean.get("REFFERENCE_NO") == null ? "-" : IndentGetBean.get("REFFERENCE_NO").toString();
				poRefference_Date=IndentGetBean.get("PO_ISSUE_DATE") == null ? "-" : IndentGetBean.get("PO_ISSUE_DATE").toString();
				strDeliveryDate=IndentGetBean.get("DELIVERY_DATE") == null ? "" : IndentGetBean.get("DELIVERY_DATE").toString();

				contactPersonTwo=IndentGetBean.get("VENDOR_CON_PER_NAME_TWO") == null ? "" : IndentGetBean.get("VENDOR_CON_PER_NAME_TWO").toString();
				mobileNumberTwo=IndentGetBean.get("MOBILE_NUMBER_TWO") == null ? "" : IndentGetBean.get("MOBILE_NUMBER_TWO").toString();
				
				if(!poRefference_Date.equals("-")){
					DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
					DateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
					Date poReffDate = inputFormat.parse(poRefference_Date);
					poRefference_Date=outputFormat.format(poReffDate);
					if(StringUtils.isNotBlank(strDeliveryDate)){
						Date deliveryDate = inputFormat.parse(strDeliveryDate);
						strDeliveryDate=outputFormat.format(deliveryDate);
					}
				}
				  //  System.out.println(outputFormat.format(date));
				request.setAttribute("versionNo", version_No);
				request.setAttribute("refferenceNo", refference_No);
				request.setAttribute("strPoPrintRefdate", poRefference_Date);
				

				if(ReceiverState.equalsIgnoreCase("Telangana")){
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
				if(!contactPersonTwo.equals("") && !contactPerson.equals("") && !contactPersonTwo.equals("-") && !contactPerson.equals("-")){
					contactPerson = contactPerson+","+contactPersonTwo;
				}
				if(!mobilenumber.equals("")){
					contactPerson = contactPerson+" ( "+mobilenumber;
				}if(!mobileNumberTwo.equals("")){
					contactPerson = contactPerson+","+mobileNumberTwo;
				}

				contactPerson = " "+contactPerson +" )";
				strVendorEmail = strEmailId;
				//state=state+". Email Id : "+emailId;

			//	contactPerson = " Mr/Mrs "+contactPerson +" ( "+strMobileNo+" "+strLandLine+" )";
				//contactPerson =  contactPerson +" ( "+strMobileNo+","+strLandLine+" )";
				state = state;//+". Email Id : "+strEmailId;
				

				//	terms=IndentGetBean.get("TERMS_CONDITION_DESC") == null ? "-" : IndentGetBean.get("TERMS_CONDITION_DESC").toString();
				//	request.setAttribute("terms",terms);
				request.setAttribute("poentryId",poentryId);

				getPOApproveCreateEmp(poentryId,request);
				Map<String, String> verifyNames = (Map<String, String>)request.getAttribute("listOfVerifiedName");
				Map<String, String> approverNames = (Map<String, String>)request.getAttribute("listOfApproverName");
				Map<String, String> preparedNames = (Map<String, String>)request.getAttribute("listOfPreparedName");

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
					String query = "SELECT VENDOR_NAME,ADDRESS,MOBILE_NUMBER,GSIN_NUMBER,STATE,VENDOR_CON_PER_NAME,VENDOR_CON_PER_NAME_TWO,MOBILE_NUMBER_TWO,LANDLINE_NO"+
					" FROM VENDOR_DETAILS VD,SUMADHURA_PO_ENTRY STPE WHERE to_char(STPE.SITE_ID) =VD.VENDOR_ID  AND  STPE.PO_NUMBER=?";

					dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{poNumber});


					if (null != dbIndentDts && dbIndentDts.size() > 0) {
						for (Map<?, ?> siteVendoraddress : dbIndentDts) {

							//	indentnumber = siteVendoraddress.get("INDENT_NO") == null ? "" : siteVendoraddress.get("INDENT_NO").toString();
							//	ponumber = siteVendoraddress.get("PO_ENTRY_ID") == null ? "" : siteVendoraddress.get("PO_ENTRY_ID").toString();
							receivername = siteVendoraddress.get("VENDOR_NAME") == null ? "" : siteVendoraddress.get("VENDOR_NAME").toString();
							revaddress = siteVendoraddress.get("ADDRESS") == null ? "" : siteVendoraddress.get("ADDRESS").toString();
							recemobilenumber = siteVendoraddress.get("MOBILE_NUMBER") == null ? "" : siteVendoraddress.get("MOBILE_NUMBER").toString();
							recegstinnumber = siteVendoraddress.get("GSIN_NUMBER") == null ? "" : siteVendoraddress.get("GSIN_NUMBER").toString();
							receiverState=siteVendoraddress.get("STATE") == null ? "" : siteVendoraddress.get("STATE").toString();
							strReceiverContactPersonName = siteVendoraddress.get("VENDOR_CON_PER_NAME") == null ? " " : siteVendoraddress.get("VENDOR_CON_PER_NAME").toString();
							strReceiverContactNo=siteVendoraddress.get("LANDLINE_NO") == null ? " " : siteVendoraddress.get("LANDLINE_NO").toString();
							receiverContactPersonTwo=siteVendoraddress.get("VENDOR_CON_PER_NAME_TWO")==null ? "" :   siteVendoraddress.get("VENDOR_CON_PER_NAME_TWO").toString();
							receiverMobileNumberTwo=siteVendoraddress.get("MOBILE_NUMBER_TWO")==null ? "" :   siteVendoraddress.get("MOBILE_NUMBER_TWO").toString();




							request.setAttribute("receiverState",receiverState);

						}
						if(!receiverContactPersonTwo.equals("")){
							strReceiverContactPersonName=strReceiverContactPersonName+","+receiverContactPersonTwo;
						}

					}

				}


				if (StringUtils.isNotBlank(indentnumber) ) {
					String query = "SELECT CREATE_DATE FROM SUMADHURA_INDENT_CREATION WHERE INDENT_CREATION_ID=?";

					dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{indentnumber});




					if (null != dbIndentDts && dbIndentDts.size() > 0) {
						for (Map<?, ?> siteVendoraddress : dbIndentDts) {

							//	indentnumber = siteVendoraddress.get("INDENT_NO") == null ? "" : siteVendoraddress.get("INDENT_NO").toString();
							//	ponumber = siteVendoraddress.get("PO_ENTRY_ID") == null ? "" : siteVendoraddress.get("PO_ENTRY_ID").toString();
							createdDate = siteVendoraddress.get("CREATE_DATE") == null ? "" : siteVendoraddress.get("CREATE_DATE").toString();



							SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
							Date date = dt.parse(createdDate); 


							SimpleDateFormat dt1 = new SimpleDateFormat("dd-MM-yyyy");
							createdDate = dt1.format(date);
						}

					}

				}


				String siteWiseIndentNo = String.valueOf(getSiteWiseIndentNo(Integer.parseInt(indentnumber)));
				tblOneData+= ponumber+"@@"+podate+"@@"+indentnumber+"@@"+vendorname+"@@"+address+"@@"+state+"@@"+gstinnumber+
				"@@"+receivername+"@@"+revaddress+"@@"+recemobilenumber+"@@"+recegstinnumber+"@@"+""+"@@"+subject+
				"@@"+contactPerson+"@@"+""+"@@"+billingAddress+"@@"+approverName+"@@"+approveDate+"@@"+preparedName+"@@"+preparedDate+"@@"+verifyName+"@@"+verifyDate+"@@"+createdDate+"@@"+siteWiseIndentNo+
				"@@"+strBillingAddressGSTIN+"@@"+strReceiverContactPersonName+"@@"+receiverMobileNumberTwo+"@@"+strVendorEmail+"@@"+strBillingCompanyName+"@@"+siteId+"@@"+siteName+"@@"+strDeliveryDate+"@@"+vendorid;

				break;

			}

			request.setAttribute("gstinumber",gstinnumber);

		}

		return tblOneData;

	}

	@Override
	public List<IndentCreationBean> getPoDetails(String fromDate, String toDate, String siteId,String poNumber,String sessionId,boolean allPoOrNot,String siteIds,String vendorName) {
		
		//String query = "";
		//String strDCFormQuery = "";
		//String strDCNumber = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		IndentCreationBean indentObj = null; 
		String type_Of_Purchase="";
		String old_Po_Number="";
		String status="";
		String operationType="";
		String deptName="";
		
		String purchaseDeptId=validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
		String marketingDeptId=validateParams.getProperty("MARKETING_DEPT_ID") == null ? "" : validateParams.getProperty("MARKETING_DEPT_ID").toString();	

		try {
			//if part is for view indent receive details,else part is for view indent issue details
			template = new JdbcTemplate(DBConnection.getDbConnection());
			if(allPoOrNot){
				StringBuilder query = new StringBuilder("SELECT DISTINCT SPE.PO_NUMBER,SPE.PO_DATE,SPE.PO_STATUS,SPE.PO_ENTRY_ID,SPE.SITE_ID,S.SITE_NAME,VD.VENDOR_NAME,")
												.append(" VD.VENDOR_ID,SPE.SUBJECT,SPE.OLD_PO_NUMBER,SPE.PREPARED_BY,SPE.OPERATION_TYPE FROM SUMADHURA_PO_ENTRY SPE, ")
												.append(" SUMADHURA_PO_ENTRY_DETAILS SPED,SITE S,VENDOR_DETAILS VD  WHERE SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID  AND ")
												.append(" SPE.PO_STATUS in('A','I','CNL') and  S.SITE_ID = SPE.SITE_ID and SPE.VENDOR_ID = VD.VENDOR_ID ");
				
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") ;
					} else if (StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')") ;
					} else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')") ;
					}
					else if(StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(toDate) && StringUtils.isBlank(fromDate) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND SPE.PO_NUMBER='"+poNumber+"'") ;
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')")
							.append(" AND SPE.SITE_ID in ('"+siteIds+"') AND SPE.PO_NUMBER='"+poNumber+"' and SPE.VENDOR_ID='"+vendorName+"'") ;
						
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND SPE.SITE_ID in ('"+siteIds+"')") ;
						
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND SPE.PO_NUMBER='"+poNumber+"'");
						
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') and SPE.VENDOR_ID='"+vendorName+"'");
						
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SPE.PO_NUMBER='"+poNumber+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND SPE.PO_NUMBER='"+poNumber+"' and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND SPE.PO_NUMBER='"+poNumber+"' AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND SPE.PO_NUMBER='"+poNumber+"' AND SPE.SITE_ID in ('"+siteIds+"') and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND SPE.SITE_ID in ('"+siteIds+"') and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SPE.PO_NUMBER='"+poNumber+"'") 
							.append(" and SPE.VENDOR_ID='"+vendorName+"' AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
							.append(" and SPE.VENDOR_ID='"+vendorName+"' AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" and SPE.VENDOR_ID='"+vendorName+"' AND SPE.PO_NUMBER='"+poNumber+"'");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" and SPE.VENDOR_ID='"+vendorName+"' ");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" AND SPE.PO_NUMBER='"+poNumber+"' ");
					}
				dbIndentDts = jdbcTemplate.queryForList(query.toString());	
				 
				
			}else{
			if(StringUtils.isNotBlank(sessionId) && StringUtils.isBlank(siteId)){
				if(sessionId.equals(purchaseDeptId)){
					deptName="PURCHASE_DEPT";
				}else if(sessionId.equals(marketingDeptId)){
						deptName="MARKETING_DEPT";
					}
				StringBuilder query = new StringBuilder("SELECT DISTINCT SPE.PO_NUMBER,SPE.PO_DATE,SPE.PO_STATUS,SPE.PO_ENTRY_ID,SPE.SITE_ID,S.SITE_NAME,VD.VENDOR_NAME,")
												.append(" VD.VENDOR_ID,SPE.SUBJECT,SPE.OLD_PO_NUMBER,SPE.PREPARED_BY,SPE.OPERATION_TYPE FROM SUMADHURA_PO_ENTRY SPE, ")
												.append(" SUMADHURA_PO_ENTRY_DETAILS SPED,SITE S,VENDOR_DETAILS VD  WHERE SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID  AND ")
												.append(" SPE.PO_STATUS in('A','I','CNL') and  S.SITE_ID = SPE.SITE_ID and SPE.VENDOR_ID = VD.VENDOR_ID AND PREPARED_BY='"+deptName+"' ");	
					
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") ;
					} else if (StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')") ;
					} else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')") ;
					}
					else if(StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(toDate) && StringUtils.isBlank(fromDate) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND SPE.PO_NUMBER='"+poNumber+"'") ;
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')")
							.append(" AND SPE.SITE_ID in ('"+siteIds+"') AND SPE.PO_NUMBER='"+poNumber+"' and SPE.VENDOR_ID='"+vendorName+"'") ;
						
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND SPE.SITE_ID in ('"+siteIds+"')") ;
						
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND SPE.PO_NUMBER='"+poNumber+"'");
						
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') and SPE.VENDOR_ID='"+vendorName+"'");
						
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SPE.PO_NUMBER='"+poNumber+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND SPE.PO_NUMBER='"+poNumber+"' and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND SPE.PO_NUMBER='"+poNumber+"' AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND SPE.PO_NUMBER='"+poNumber+"' AND SPE.SITE_ID in ('"+siteIds+"') and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND SPE.SITE_ID in ('"+siteIds+"') and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SPE.PO_NUMBER='"+poNumber+"'") 
							.append(" and SPE.VENDOR_ID='"+vendorName+"' AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" and SPE.VENDOR_ID='"+vendorName+"' AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isNotBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" AND SPE.SITE_ID in ('"+siteIds+"')");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" and SPE.VENDOR_ID='"+vendorName+"' AND SPE.PO_NUMBER='"+poNumber+"'");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" and SPE.VENDOR_ID='"+vendorName+"' ");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(siteIds) && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" AND SPE.PO_NUMBER='"+poNumber+"' ");
					}
				
				dbIndentDts = jdbcTemplate.queryForList(query.toString());	
				}
				
			else if(siteId!=null&&!siteId.equals("")){//Fetching active and inactive records A- Active,I-Inactive records. As per client requirment showing inactive records
					StringBuilder query = new StringBuilder("SELECT DISTINCT SPE.PO_NUMBER,SPE.PO_DATE,SPE.PO_STATUS,SPE.PO_ENTRY_ID,SPE.SITE_ID,S.SITE_NAME,VD.VENDOR_NAME,")
													.append(" VD.VENDOR_ID,SPE.SUBJECT,SPE.OLD_PO_NUMBER,SPE.PREPARED_BY,SPE.OPERATION_TYPE FROM SUMADHURA_PO_ENTRY SPE, ")
													.append(" SUMADHURA_PO_ENTRY_DETAILS SPED,SITE S,VENDOR_DETAILS VD  WHERE SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID  AND ")
													.append(" SPE.PO_STATUS in('A','I','CNL') and  S.SITE_ID = SPE.SITE_ID and SPE.VENDOR_ID = VD.VENDOR_ID and s.site_id='"+siteId+"' ");	

					if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber) && StringUtils.isBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") ;
					} else if (StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber)  && StringUtils.isBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')") ;
					} else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber)  && StringUtils.isBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')") ;
					}
					else if(StringUtils.isNotBlank(poNumber) && StringUtils.isBlank(toDate) && StringUtils.isBlank(fromDate)  && StringUtils.isBlank(vendorName)) {
					query.append(" AND SPE.PO_NUMBER='"+poNumber+"'") ;
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber)  && StringUtils.isNotBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')")
					.append(" AND SPE.PO_NUMBER='"+poNumber+"' and SPE.VENDOR_ID='"+vendorName+"'") ;
					
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isNotBlank(poNumber)  && StringUtils.isBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND SPE.PO_NUMBER='"+poNumber+"'");
					
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber)  && StringUtils.isNotBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') and SPE.VENDOR_ID='"+vendorName+"'");
					
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber)  && StringUtils.isBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SPE.PO_NUMBER='"+poNumber+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber)  && StringUtils.isNotBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isNotBlank(poNumber)  && StringUtils.isNotBlank(vendorName)) {
					query.append(" AND SPE.PO_NUMBER='"+poNumber+"' and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isBlank(toDate) && StringUtils.isBlank(poNumber)  && StringUtils.isNotBlank(vendorName)) {
					query.append(" and SPE.VENDOR_ID='"+vendorName+"'");
					}else if(StringUtils.isBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber)  && StringUtils.isNotBlank(vendorName)) {
					query.append(" AND TRUNC(SPE.PO_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy') AND SPE.PO_NUMBER='"+poNumber+"'") 
					.append(" and SPE.VENDOR_ID='"+vendorName+"' ");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isBlank(poNumber)  && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" and SPE.VENDOR_ID='"+vendorName+"' ");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber)  && StringUtils.isNotBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" and SPE.VENDOR_ID='"+vendorName+"' AND SPE.PO_NUMBER='"+poNumber+"'");
					}else if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(poNumber)  && StringUtils.isBlank(vendorName)) {
						query.append(" AND TRUNC(SPE.PO_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')") 
						.append(" AND SPE.PO_NUMBER='"+poNumber+"'");
					}
				
				dbIndentDts = jdbcTemplate.queryForList(query.toString());
				//System.out.println("Site id is not null in dao "+siteId);
			}
			}
			
			for(Map<String, Object> prods : dbIndentDts) {
				indentObj = new IndentCreationBean();

				indentObj.setPonumber(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());
				//	indentObj.setStrInvoiceDate(prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString());
				status=prods.get("PO_STATUS")==null ? "" : prods.get("PO_STATUS").toString();
				indentObj.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString()));
				indentObj.setPoentryId(prods.get("PO_ENTRY_ID")==null ? "" : prods.get("PO_ENTRY_ID").toString());
				indentObj.setSiteName(prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString());
				indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
				indentObj.setVendorId(prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
				indentObj.setSubject(prods.get("SUBJECT")==null ? "-" : prods.get("SUBJECT").toString());
				type_Of_Purchase=(prods.get("PREPARED_BY")==null ? "" : prods.get("PREPARED_BY").toString());
				old_Po_Number=(prods.get("OLD_PO_NUMBER")==null ? "" : prods.get("OLD_PO_NUMBER").toString());
				operationType=(prods.get("OPERATION_TYPE")==null ? "" : prods.get("OPERATION_TYPE").toString());
				//indentObj.setType_Of_Purchase
				if(type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT") && !operationType.equalsIgnoreCase("REVISED")){
					indentObj.setType_Of_Purchase("MARKETING PO");
				}else if(type_Of_Purchase.equalsIgnoreCase("MARKETING_DEPT") && operationType.equalsIgnoreCase("REVISED")){
					indentObj.setType_Of_Purchase("REVISED PO");
				}
				
				else{
				if((type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("REVISED"))){

					indentObj.setType_Of_Purchase("REVISED PO");
				}else if(type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && operationType.equalsIgnoreCase("UPDATE")){
					indentObj.setType_Of_Purchase("UPDATE PO");
				}
				else if(type_Of_Purchase.equals("") || (type_Of_Purchase.equalsIgnoreCase("PURCHASE_DEPT") && old_Po_Number.equals("-") || old_Po_Number.equals(""))){

					indentObj.setType_Of_Purchase("PURCHASE DEPT PO");
				} else {

					indentObj.setType_Of_Purchase("SITE LEVEL PO");
				}
				}
				if(status.equalsIgnoreCase("CNL")){
					indentObj.setType_Of_Purchase("CANCEL PO");
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





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//.info("Exception Occured Inside getViewGrnDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			//query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;
	}


	public String getProductDetails(String poNumber, String siteId,HttpServletRequest request,String receiverState) {
		List<Map<String, Object>> GetproductDetailsList = null;

		//List<GetInvoiceDetailsBean> GetInvoiceDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		IndentCreationBean objGetInvoiceDetailsInward=null;

		JdbcTemplate template = null;
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
		String pd_Product_Desc="";
		String product_name="";
		String productId="";
		String sub_productId="";
		String child_productId="";
		String measurementId="";
		String indentCreationDetailsId="";
		String strCGSTAMT = "0";
		String strSGSTAMT = "0";
		String strIGSTAMT = "0";
		try{
			if (StringUtils.isNotBlank(poNumber) ) {

				sql+="select (P.NAME)AS PRODUCT_NAME,(SP.NAME) AS SUB_PRODUCT,(CP.NAME) AS CHILD_PRODUCT,M.NAME, SPED.PO_QTY,SPED.TAX,SPED.TAX_AMOUNT,SPED.AMOUNT_AFTER_TAX, "
					+" SPED.PRODUCT_ID,SPED.SUB_PRODUCT_ID,SPED.CHILD_PRODUCT_ID,SPED.MEASUR_MNT_ID, "
					+" SPED.BASIC_AMOUNT,SPED.PRICE, SPED.HSN_CODE,SPED.TOTAL_AMOUNT,SPED.DISCOUNT,SPED.AMOUNT_AFTER_DISCOUNT,SPED.VENDOR_PRODUCT_DESC, "
					+" IGST.TAX_PERCENTAGE,SPED.PD_PRODUCT_DESC,SPED.INDENT_CREATION_DTLS_ID from SUMADHURA_PO_ENTRY SPE,SUMADHURA_PO_ENTRY_DETAILS SPED, " 
					+" SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT M,INDENT_GST IGST,PRODUCT P  where IGST.TAX_ID = SPED.TAX and SPE.PO_ENTRY_ID = SPED.PO_ENTRY_ID " 
					+" AND P.PRODUCT_ID=SPED.PRODUCT_ID and  SPED.SUB_PRODUCT_ID = SP.SUB_PRODUCT_ID and SPED.MEASUR_MNT_ID= M.MEASUREMENT_ID and SPED.CHILD_PRODUCT_ID = CP.CHILD_PRODUCT_ID "
					+" and SPE.PO_ENTRY_ID =? AND SPE.SITE_ID=? order by SPED.INDENT_CREATION_DTLS_ID "; 

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
					indentCreationDetailsId=GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID") == null ? "": GetDetailsInwardBean.get("INDENT_CREATION_DTLS_ID").toString();

					if(pd_Product_Desc!=null && !pd_Product_Desc.equals("") ){

						childproduct=pd_Product_Desc;
					}

					gstinumber=request.getAttribute("gstinumber").toString();
					char firstLetterChar = gstinumber.charAt(0);
					char secondLetterChar=gstinumber.charAt(1);

					if(tax.contains("%")){
						String data[] = tax.split("%");
						tax=data[0];

					}


					if(receiverState.equalsIgnoreCase("Telangana")){

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
								CGSTAMT =Double.parseDouble(new DecimalFormat("##.##").format(amt));
								SGSTAMT =Double.parseDouble(new DecimalFormat("##.##").format(amt));
								CGST = String.valueOf(percent);
								SGST = String.valueOf(percent);
								 strCGSTAMT =String.format("%.2f",CGSTAMT);
								 strSGSTAMT =String.format("%.2f",SGSTAMT);
							}
						} else {

							request.setAttribute("isCGSTSGST","false");
							percent = Double.parseDouble(tax);
							amt = Double.parseDouble(taxamount);
							IGST = String.valueOf(percent);
							IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
							strIGSTAMT=String.format("%.2f",IGSTAMT);
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
						} else {

							request.setAttribute("isCGSTSGST","false");
							percent = Double.parseDouble(tax);
							amt = Double.parseDouble(taxamount);
							IGST = String.valueOf(percent);
							IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
							strIGSTAMT=String.format("%.2f",IGSTAMT);
						}

					}
					double totalvalue=Double.valueOf(totalamount);
					totalAmt=totalAmt+totalvalue;
					totalAmt =Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
					int val = (int) Math.ceil(totalAmt);
					double roundoff=Math.ceil(totalAmt)-totalAmt;
					double grandtotal=(Math.ceil(totalAmt));

					//String s = String.format("%f", d);
					String strGroundVal=String.format("%.2f",grandtotal);
					String totalVal=String.format("%.2f",totalAmt);
					//long strGroundVal=(grandtotal).longValue();


					NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
					String strtotal = numberFormat.format(totalAmt);
					quantity=String.format("%.2f",Double.valueOf(quantity));
					price=String.format("%.2f",Double.valueOf(price));
					discountaftertax=String.format("%.2f",Double.valueOf(discountaftertax));
					amountaftertax=String.format("%.2f",Double.valueOf(amountaftertax));
					basicamount=String.format("%.2f",Double.valueOf(basicamount));
					
					
					//String strtotal=String.valueOf(totalAmt);
					String strroundoff=String.format("%.2f",roundoff);
					String strgrandtotal=numberFormat.format(grandtotal);
					double tempTaxAmt=Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(taxamount)));
					double tempBasicAmount=Double.parseDouble(new DecimalFormat("##.##").format(Double.parseDouble(basicamount)));
					//totalBasicAmount=totalBasicAmount+tempBasicAmount;
					//totalTaxAmount=totalTaxAmount+tempTaxAmt;

					//	productDetails.setPurchaseDeptIndentProcessSeqId(purchaseDepartmentIndentProcessSeqId);


					if(receiverState.equalsIgnoreCase("Telangana")){

						if (firstLetterChar=='3' && secondLetterChar=='6') {

							tblTwoData += sno+"@@"+subproduct+"@@"+childproduct+"@@"+hsncode+"@@"+measurement+"@@"+
							quantity+"@@"+price+"@@"+basicamount+"@@"+discount+"@@"+discountaftertax+"@@"+CGST+"%"+"@@"+strCGSTAMT+"@@"+SGST+"%"+"@@"+strSGSTAMT+"@@"+""+"@@"+""+"@@"+amountaftertax+"@@"+amountaftertax+"@@"+strroundoff+"@@"+strGroundVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+totalVal+"@@"+vendorProductDesc+"@@"
							+product_name+"@@"+productId+"@@"+sub_productId+"@@"+child_productId+"@@"+measurementId+"@@"+indentCreationDetailsId+"&&";
						}else
						{

							tblTwoData += sno+"@@"+subproduct+"@@"+childproduct+"@@"+hsncode+"@@"+measurement+"@@"+
							quantity+"@@"+price+"@@"+basicamount+"@@"+discount+"@@"+discountaftertax+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"%"+"@@"+strIGSTAMT+"@@"+amountaftertax+"@@"+amountaftertax+"@@"+strroundoff+"@@"+strGroundVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+totalVal+"@@"+vendorProductDesc+"@@"
							+product_name+"@@"+productId+"@@"+sub_productId+"@@"+child_productId+"@@"+measurementId+"@@"+indentCreationDetailsId+"&&";

						}

					}else{


						if (firstLetterChar=='2' && secondLetterChar=='9') {

							tblTwoData += sno+"@@"+subproduct+"@@"+childproduct+"@@"+hsncode+"@@"+measurement+"@@"+
							quantity+"@@"+price+"@@"+basicamount+"@@"+discount+"@@"+discountaftertax+"@@"+CGST+"%"+"@@"+strCGSTAMT+"@@"+SGST+"%"+"@@"+strSGSTAMT+"@@"+""+"@@"+""+"@@"+amountaftertax+"@@"+amountaftertax+"@@"+strroundoff+"@@"+strGroundVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+totalVal+"@@"+vendorProductDesc+"@@"
							+product_name+"@@"+productId+"@@"+sub_productId+"@@"+child_productId+"@@"+measurementId+"@@"+indentCreationDetailsId+"&&";
						}else
						{

							tblTwoData += sno+"@@"+subproduct+"@@"+childproduct+"@@"+hsncode+"@@"+measurement+"@@"+
							quantity+"@@"+price+"@@"+basicamount+"@@"+discount+"@@"+discountaftertax+"@@"+""+"@@"+""+"@@"+""+"@@"+""+"@@"+IGST+"%"+"@@"+strIGSTAMT+"@@"+amountaftertax+"@@"+amountaftertax+"@@"+strroundoff+"@@"+strGroundVal+"@@"+new NumberToWord().convertNumberToWords(val)+" Rupees Only."+"@@"+totalVal+"@@"+vendorProductDesc+"@@"
							+product_name+"@@"+productId+"@@"+sub_productId+"@@"+child_productId+"@@"+measurementId+"@@"+indentCreationDetailsId+"&&";

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
	public String getTransportChargesListForGRN(String poEntryId,String strSiteId,String gstinumber,HttpServletRequest request,String poNumber,String receiverState) {
		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> termList = null;
		List<GetInvoiceDetailsBean> GetTransportChargesListDetails = new ArrayList<GetInvoiceDetailsBean>();
		GetInvoiceDetailsBean objGetTransportChargesDetails=null;
		List<String> listOfTermsAndConditions = new ArrayList<String>();
		JdbcTemplate template = null;
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
						AmountAfterTax = GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "0" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString();
						ConveyanceName = GetTransportChargesDetails.get("CHARGE_NAME") == null ? "" : GetTransportChargesDetails.get("CHARGE_NAME").toString();



						char firstLetterChar = gstinumber.charAt(0);
						char secondLetterChar=gstinumber.charAt(1);

						if(GSTTax.contains("%")){
							String data[] = GSTTax.split("%");
							GSTTax=data[0];

						}

						if(receiverState.equalsIgnoreCase("Telangana")){


							if (firstLetterChar=='3' && secondLetterChar=='6') {

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
							} else {
								percent = Double.parseDouble(GSTTax);
								amt = Double.parseDouble(GSTAmount);
								IGST = String.valueOf(percent);
								IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
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
							} else {
								percent = Double.parseDouble(GSTTax);
								amt = Double.parseDouble(GSTAmount);
								IGST = String.valueOf(percent);
								IGSTAMT = Double.parseDouble(new DecimalFormat("##.##").format(amt));
							}

						}
						//ConveyanceAmount=String.format(".2f%",());
						ConveyanceAmount=String.format("%.2f",Double.valueOf(ConveyanceAmount));
						GSTAmount=String.format("%.2f",Double.valueOf(GSTAmount));
						AmountAfterTax=String.format("%.2f",Double.valueOf(AmountAfterTax));


						if(receiverState.equalsIgnoreCase("Telangana")){

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
						}
						//	strTableThreeData += ConveyanceName+"@@"+ConveyanceAmount+"@@"+GSTTax+"@@"+GSTAmount+"@@"+AmountAfterTax+"@@"+AmountAfterTax+"&&";


					}
				}

				if(response=="success"){
					//	int i=1;

					String sql1="SELECT TERMS_CONDITION_DESC FROM  SUMADHURA_PD_TERMS_CONDITIONS WHERE PO_ENTRY_ID=?";
					termList = jdbcTemplate.queryForList(sql1, new Object[] {poEntryId});
					//System.out.println("the list size is "+termList.size());
					if (null != termList && termList.size() > 0) {
						for (Map<?, ?> GetTransportChargesDetails : termList) {

							termscondition = GetTransportChargesDetails.get("TERMS_CONDITION_DESC") == null ? "" : GetTransportChargesDetails.get("TERMS_CONDITION_DESC").toString();

							//	String [] third = termscondition.split(",");
							//	for(int j=0; j <= third.length-1; j++) {
							//	String data = third[j];
							//	String splData[] = data.split("@@");

							listOfTermsAndConditions.add(String.valueOf(termscondition));

						}
					}
				}
				request.setAttribute("listOfTermsAndConditions", listOfTermsAndConditions);
				//		}
				//		}

				//	} 

			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		return strTableThreeData;
	}	

	public List<IndentCreationBean> getClosedIndents(String fromDate, String toDate, String pdmngId,String indentNumber,String siteId,String sites) {

		//String query = "";
		//String query1 = "";
		//String strDCFormQuery = "";
		//String strDCNumber = "";
		JdbcTemplate template = null;
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbIndentClosedDts = null;
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		IndentCreationBean indentObj = null; 
		String purchaseDeptId=validateParams.getProperty("PURCHASE_DEPT_ID") == null ? "" : validateParams.getProperty("PURCHASE_DEPT_ID").toString();
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			
				StringBuilder query = new StringBuilder(" select distinct(SIC.INDENT_CREATION_ID),SIC.SITEWISE_INDENT_NO,SIC.MODIFYDATE, S.SITE_NAME,SIC.INDENT_NAME from SUMADHURA_INDENT_CREATION SIC ")
												.append("join SUM_INT_CREATION_APPROVAL_DTLS SICAD  on SIC.INDENT_CREATION_ID = SICAD.INDENT_CREATION_ID join SITE S on S.SITE_ID = SIC.SITE_ID ")
												.append(" where SICAD.INDENT_TYPE = 'S' ");
				
				if(purchaseDeptId.equals(siteId) || siteId.equals("119")){
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(sites)) {
					query.append(" and TRUNC(SICAD.CREATION_DATE)  between TO_DATE('"+fromDate+"', 'dd-MM-yy') and  TO_DATE('"+toDate+"', 'dd-MM-yy') ")
						.append("or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and TRUNC(SIC.MODIFYDATE)  between TO_DATE('"+fromDate+"', 'dd-MM-yy') and  TO_DATE('"+toDate+"', 'dd-MM-yy') AND  SIC.SITE_ID='"+sites+"' ");
				}else if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(sites)){
					query.append("  and TRUNC(SICAD.CREATION_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')")
						.append("or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and TRUNC(SIC.MODIFYDATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') AND  SIC.SITE_ID='"+sites+"' ");
				}else if(StringUtils.isNotBlank(toDate) && StringUtils.isNotBlank(sites)) {
					query.append(" and TRUNC(SICAD.CREATION_DATE) =TO_DATE('"+toDate+"', 'dd-MM-yy')")
						.append("or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and TRUNC(SIC.MODIFYDATE) =TO_DATE('"+toDate+"', 'dd-MM-yy') AND  SIC.SITE_ID='"+sites+"' ");
				}else if(StringUtils.isNotBlank(indentNumber) && StringUtils.isNotBlank(sites)) {
					query.append(" and SIC.INDENT_CREATION_ID='"+indentNumber+"'")
						.append("or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and SIC.INDENT_CREATION_ID='"+indentNumber+"' AND  SIC.SITE_ID='"+sites+"' ");
				}
				
			}else{
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query.append("and TRUNC(SICAD.CREATION_DATE)  between TO_DATE('"+fromDate+"', 'dd-MM-yy') and  TO_DATE('"+toDate+"', 'dd-MM-yy') ")
						.append("or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and TRUNC(SIC.MODIFYDATE)  between TO_DATE('"+fromDate+"', 'dd-MM-yy') and  TO_DATE('"+toDate+"', 'dd-MM-yy') and SIC.SITE_ID='"+siteId+"'");
				}else if (StringUtils.isNotBlank(fromDate)){
					query.append("and TRUNC(SICAD.CREATION_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')")
						.append("or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and TRUNC(SIC.MODIFYDATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') and SIC.SITE_ID='"+siteId+"'"); 
				}else if(StringUtils.isNotBlank(toDate)) {
					query.append("and TRUNC(SICAD.CREATION_DATE) =TO_DATE('"+toDate+"', 'dd-MM-yy')")
						.append("or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and TRUNC(SIC.MODIFYDATE) =TO_DATE('"+toDate+"', 'dd-MM-yy') and SIC.SITE_ID='"+siteId+"'");
				}else if(StringUtils.isNotBlank(indentNumber)) {
					query.append("and SIC.INDENT_CREATION_ID='"+indentNumber+"'")
						.append("or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and SIC.INDENT_CREATION_ID='"+indentNumber+"' and SIC.SITE_ID='"+siteId+"'");
				}
			}
			/*if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = "select distinct(SIC.INDENT_CREATION_ID),SIC.SITEWISE_INDENT_NO,SIC.MODIFYDATE, S.SITE_NAME,SIC.INDENT_NAME from SUMADHURA_INDENT_CREATION SIC "
					+" join SUM_INT_CREATION_APPROVAL_DTLS SICAD  on SIC.INDENT_CREATION_ID = SICAD.INDENT_CREATION_ID join SITE S on S.SITE_ID = SIC.SITE_ID "  
					+" where SICAD.INDENT_TYPE = 'S' and TRUNC(SICAD.CREATION_DATE)  between TO_DATE('"+fromDate+"', 'dd-MM-yy') and  TO_DATE('"+toDate+"', 'dd-MM-yy') "
					+" or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and TRUNC(SIC.MODIFYDATE)  between TO_DATE('"+fromDate+"', 'dd-MM-yy') and  TO_DATE('"+toDate+"', 'dd-MM-yy') ";
				query1 = "SELECT distinct(SIC.INDENT_CREATION_ID),SIC.SITEWISE_INDENT_NO,SICAD.CREATION_DATE,S.SITE_NAME FROM SITE S , SUMADHURA_INDENT_CREATION SIC,SUM_INT_CREATION_APPROVAL_DTLS SICAD  WHERE  SIC.SITE_ID=S.SITE_ID and SIC.STATUS='I'" 
						 +" AND SICAD.INDENT_TYPE='S' AND SIC.INDENT_CREATION_ID=SICAD.INDENT_CREATION_ID AND TRUNC(SICAD.CREATION_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
				 
			} else if (StringUtils.isNotBlank(fromDate)) {
				query = "select distinct(SIC.INDENT_CREATION_ID),SIC.SITEWISE_INDENT_NO,SIC.MODIFYDATE, S.SITE_NAME,SIC.INDENT_NAME from SUMADHURA_INDENT_CREATION SIC "
					+" join SUM_INT_CREATION_APPROVAL_DTLS SICAD  on SIC.INDENT_CREATION_ID = SICAD.INDENT_CREATION_ID "
					+" join SITE S on S.SITE_ID = SIC.SITE_ID where SICAD.INDENT_TYPE = 'S' and TRUNC(SICAD.CREATION_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') "
					+" or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and TRUNC(SIC.MODIFYDATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy') ";

				query1 = "SELECT distinct(SIC.INDENT_CREATION_ID),SIC.SITEWISE_INDENT_NO,SICAD.CREATION_DATE,S.SITE_NAME FROM SITE S , SUMADHURA_INDENT_CREATION SIC,SUM_INT_CREATION_APPROVAL_DTLS SICAD  WHERE  SIC.SITE_ID=S.SITE_ID and SIC.STATUS='I'" 
						 +" AND SICAD.INDENT_TYPE='S' AND SIC.INDENT_CREATION_ID=SICAD.INDENT_CREATION_ID AND TRUNC(SICAD.CREATION_DATE) >=TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				 
			} else if(StringUtils.isNotBlank(toDate)) {
				query = "select distinct(SIC.INDENT_CREATION_ID),SIC.SITEWISE_INDENT_NO,SIC.MODIFYDATE, S.SITE_NAME,SIC.INDENT_NAME from SUMADHURA_INDENT_CREATION SIC "
					+" join SUM_INT_CREATION_APPROVAL_DTLS SICAD  on SIC.INDENT_CREATION_ID = SICAD.INDENT_CREATION_ID "
					+" join SITE S on S.SITE_ID = SIC.SITE_ID where SICAD.INDENT_TYPE = 'S' and TRUNC(SICAD.CREATION_DATE) =TO_DATE('"+toDate+"', 'dd-MM-yy') "
					+" or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and TRUNC(SIC.MODIFYDATE) =TO_DATE('"+toDate+"', 'dd-MM-yy') ";

				query1 = "SELECT distinct(SIC.INDENT_CREATION_ID),SIC.SITEWISE_INDENT_NO,SICAD.CREATION_DATE,S.SITE_NAME FROM SITE S , SUMADHURA_INDENT_CREATION SIC,SUM_INT_CREATION_APPROVAL_DTLS SICAD  WHERE  SIC.SITE_ID=S.SITE_ID and SIC.STATUS='I'" 
						 +" AND SICAD.INDENT_TYPE='S' AND SIC.INDENT_CREATION_ID=SICAD.INDENT_CREATION_ID AND TRUNC(SICAD.CREATION_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
				 

			}else if(StringUtils.isNotBlank(indentNumber)) {
				query = "select distinct(SIC.INDENT_CREATION_ID),SIC.SITEWISE_INDENT_NO,SIC.MODIFYDATE, S.SITE_NAME,SIC.INDENT_NAME from SUMADHURA_INDENT_CREATION SIC "
					+" join SUM_INT_CREATION_APPROVAL_DTLS SICAD  on SIC.INDENT_CREATION_ID = SICAD.INDENT_CREATION_ID "
					+" join SITE S on S.SITE_ID = SIC.SITE_ID where SICAD.INDENT_TYPE = 'S' and SIC.INDENT_CREATION_ID='"+indentNumber+"' " 
					+" or  SIC.PENDIND_DEPT_ID in ('998_PDM','VND') and SIC.INDENT_CREATION_ID='"+indentNumber+"' ";

				query1 = "SELECT distinct(SIC.INDENT_CREATION_ID),SIC.SITEWISE_INDENT_NO,SICAD.CREATION_DATE,S.SITE_NAME FROM SITE S , SUMADHURA_INDENT_CREATION SIC,SUM_INT_CREATION_APPROVAL_DTLS SICAD WHERE  SICAD.SITE_ID=S.SITE_ID and SIC.STATUS='I' AND SICAD.INDENT_TYPE='S' AND SIC.INDENT_CREATION_ID=SICAD.INDENT_CREATION_ID AND SIC.INDENT_CREATION_ID='"+indentNumber+"'";

			}*/


			dbIndentDts = jdbcTemplate.queryForList(query.toString());
			//	dbIndentClosedDts = jdbcTemplate.queryForList(query1, new Object[]{});

			for(Map<String, Object> prods : dbIndentDts) {
				indentObj = new IndentCreationBean();

				indentObj.setPonumber(prods.get("INDENT_CREATION_ID")==null ? "" : prods.get("INDENT_CREATION_ID").toString());
				indentObj.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" : prods.get("SITEWISE_INDENT_NO").toString()));
				//	indentObj.setStatus(prods.get("PO_STATUS")==null ? "" : prods.get("PO_STATUS").toString());
				indentObj.setSiteName(prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString());
				indentObj.setIndentName(prods.get("INDENT_NAME")==null ? "-" : prods.get("INDENT_NAME").toString());
				//	indentObj.setPoentryId(prods.get("PO_ENTRY_ID")==null ? "" : prods.get("PO_ENTRY_ID").toString());
				String date=prods.get("MODIFYDATE")==null ? "" : prods.get("MODIFYDATE").toString();
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setStrScheduleDate(date);

				list.add(indentObj);
			}


			/*for(Map<String, Object> prods : dbIndentClosedDts) {
				indentObj = new IndentCreationBean();

				indentObj.setPonumber(prods.get("INDENT_CREATION_ID")==null ? "" : prods.get("INDENT_CREATION_ID").toString());
				indentObj.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" : prods.get("SITEWISE_INDENT_NO").toString()));

				indentObj.setSiteName(prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString());

				String date=prods.get("CREATION_DATE")==null ? "" : prods.get("CREATION_DATE").toString();
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				indentObj.setStrScheduleDate(date);

				list.add(indentObj);
			}

			 */



		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//.info("Exception Occured Inside getViewGrnDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			//query = "";
			indentObj = null; 
			template = null;
			dbIndentDts = null;
		}
		return list;
	}





	@Override
	public String  getIndentLevelComments(int indentNo) {


		List<Map<String, Object>> getcommentDtls = null;
		String strEmployeName = "";
		String strComments = "";
		String StrIndentLevelComments = "";

		String query = "select PURPOSE,SED.EMP_NAME from SUM_INT_CREATION_APPROVAL_DTLS SICAD , SUMADHURA_EMPLOYEE_DETAILS SED where "+
		" SED.EMP_ID = SICAD.INDENT_CREATE_APPROVE_EMP_ID "+
		" and INDENT_CREATION_ID = ? ";
		getcommentDtls = jdbcTemplate.queryForList(query, new Object[] {indentNo});

		if(getcommentDtls != null && getcommentDtls.size() > 0){
			for(Map<String, Object> prods : getcommentDtls) {

				strEmployeName = prods.get("EMP_NAME")==null ? "" : prods.get("EMP_NAME").toString();
				strComments = prods.get("PURPOSE")==null ? "" : prods.get("PURPOSE").toString();

				if((strEmployeName!=null && strComments!=null) && (!strEmployeName.equals("") && !strComments.equals(""))){

					StrIndentLevelComments +=  strEmployeName + " :  "+strComments +"   ,";
				}

			}
			if(StrIndentLevelComments!=null && !StrIndentLevelComments.equals("")){
				StrIndentLevelComments =  StrIndentLevelComments.substring(0,StrIndentLevelComments.length()-1);
			}

		}


		return StrIndentLevelComments;

	}



	@Override
	public List<IndentCreationBean>  getndentChangedDetails(int indentNo) {


		List<Map<String, Object>> getcommentDtls = null;
		String strEmployeName = "";
		String strComments = "";
		String METERIAL_EDIT_COMMENT = "";
		List<IndentCreationBean> editList = new  ArrayList<IndentCreationBean>();
		try{

			String query = "  select METERIAL_EDIT_COMMENT from SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = ? ";
			getcommentDtls = jdbcTemplate.queryForList(query, new Object[] {indentNo});

			if(getcommentDtls != null && getcommentDtls.size() > 0) {
				for(Map<String, Object> prods : getcommentDtls) {

					strEmployeName = prods.get("METERIAL_EDIT_COMMENT")==null ? "  " : prods.get("METERIAL_EDIT_COMMENT").toString();


				}
				//METERIAL_EDIT_COMMENT =  strEmployeName.substring(0,strEmployeName.length()-1);
			}




			if(strEmployeName.contains("@@@")){
				String strEditCommentsArr [] = strEmployeName.split("@@@");
				for(int j = 0; j< strEditCommentsArr.length;j++){
					IndentCreationBean objCommentIndentCreationBean  = new IndentCreationBean();
					objCommentIndentCreationBean.setMaterialEditComment(strEditCommentsArr[j]);
					editList.add(objCommentIndentCreationBean);
				}


			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return editList;

	}

	public void getApproveCreateEmp(String indentnumber,HttpServletRequest request,String siteId)
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


		String sql= "SELECT SED.EMP_NAME,SICAD.CREATION_DATE FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED"+
		" where SICAD.INDENT_TYPE = 'A' and  SICAD.INDENT_CREATE_APPROVE_EMP_ID=SED.EMP_ID and SICAD.SITE_ID= ? and INDENT_CREATION_ID = ?  order by SICAD.CREATION_DATE  ASC"; 

		verifyBy = jdbcTemplate.queryForList(sql, new Object[] {siteId,indentnumber});





		String query = "SELECT SED.EMP_NAME,SICAD.CREATION_DATE FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED"+
		" where SICAD.INDENT_TYPE = 'A' and  SICAD.INDENT_CREATE_APPROVE_EMP_ID=SED.EMP_ID and SICAD.SITE_ID= ? and INDENT_CREATION_ID = ?  order by SICAD.CREATION_DATE  DESC"; 

		approverBy = jdbcTemplate.queryForList(query, new Object[] {siteId,indentnumber});

		String query1 = "SELECT SED.EMP_NAME,SICAD.CREATION_DATE FROM SUMADHURA_EMPLOYEE_DETAILS SED,SUM_INT_CREATION_APPROVAL_DTLS  SICAD"+
		" where INDENT_TYPE = 'C' and  SICAD.INDENT_CREATE_APPROVE_EMP_ID=SED.EMP_ID and INDENT_CREATION_ID = ?";

		createdBy = jdbcTemplate.queryForList(query1, new Object[] {indentnumber});

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

				}	} 

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


	public void getPOApproveCreateEmp(String strPONumber,HttpServletRequest request)
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

		/*	if(verifyBy != null && verifyBy.size() == 0 && approverBy != null && approverBy.size() == 0){

			verifyBy = createdBy;
			approverBy = createdBy;
		}*/


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

				}	} 

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



	public static void main(String [] atrgs){

		String strEmployeName = "ravi :  Sub Product Asset name changed to Batching Plant , Child Product Coffee Machine  changed to Batching Plant CP-18  , Mesurment Nos changed to Nos @@@";


		//System.out.println(strEmployeName);


		strEmployeName = strEmployeName.replace("ravi :", "");


		//System.out.println(strEmployeName);


		if(strEmployeName.contains("@@@")){
			String strEditCommentsArr [] = strEmployeName.split("@@@");
			for(int j = 0; j< strEditCommentsArr.length;j++){
				IndentCreationBean objCommentIndentCreationBean  = new IndentCreationBean();
				objCommentIndentCreationBean.setMaterialEditComment(strEditCommentsArr[j]);
				//editList.add(objCommentIndentCreationBean);
			}


		}


	}

	@Override
	public int getMaxOfSiteWiseIndentNumber(int siteId) {
		String query = "select max(SITEWISE_INDENT_NO) from SUMADHURA_INDENT_CREATION where SITE_ID = ? ";
		int result = jdbcTemplate.queryForInt(query, new Object[] {siteId});
		result = result+1;
		System.out.println("...>"+result+"<...");
		return result;
	}

	@Override
	public int getIndentNumber(int siteWiseIndentNumber, int site_Id) {
		List<Map<String, Object>> dbIndentDts = null;
		int result=0;
		String query = "select INDENT_CREATION_ID from SUMADHURA_INDENT_CREATION where SITE_ID = ? AND SITEWISE_INDENT_NO = ? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {site_Id,siteWiseIndentNumber});
		if(dbIndentDts!=null && dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				result=Integer.parseInt(prods.get("INDENT_CREATION_ID")==null ? "0" : prods.get("INDENT_CREATION_ID").toString());
			}
			//result=1;
		}
		/*for(Map<String, Object> prods : dbIndentDts) {
			indentObj = new IndentCreationBean();

			indentObj.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "" : prods.get("SITEWISE_INDENT_NO").toString()));*/
		//int result = jdbcTemplate.queryForInt(query, new Object[] {site_Id,siteWiseIndentNumber});
		return result;
	}

	@Override
	public int getSiteWiseIndentNo(int indentNumber) {
		String query = "select SITEWISE_INDENT_NO from SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = ? ";
		int result = jdbcTemplate.queryForInt(query, new Object[] {indentNumber});
		return result;
	}


	public List<IndentCreationBean> viewIndentProductDetails(String fromDate,String toDate,String siteId) {


		List<Map<String,Object>> AllProductList = null;
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		IndentCreationBean indentObj = null; 
		double request_Quantity=0.0;
		double received_Quantity=0.0;
		double doubleRecQuantity=0.0;
		double po_init_quan=0.0;
		String po_Initiated_Quantity="";
		String allocatedQuan="";
		String req_Quantity="";
		String receved_Quantity="";
		String pending_Dept_Id="";
		String remarks="";

		String query="";
		String siteName="";
		String RejectedEmpId="";
		String indentNumber="";
		String status="";
		String poNumber="";
		//	jt = new JdbcTemplate(DBConnection.getDbConnection());
		try{
			if(StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){



				query="select SIC.SITEWISE_INDENT_NO,SIC.PENDIND_DEPT_ID,(P.NAME) AS PRODUCT_NAME,(S.NAME) AS SUB_PRODUCT_NAME,(CP.NAME) AS CHILD_PRODUCT_NAME,"
						//+" (M.NAME) AS MEASUREMENT_NAME,SICD.REQ_QUANTITY,SPDIP.ALLOCATED_QUANTITY,SPDIP.PO_INTIATED_QUANTITY,SICD.RECEIVE_QUANTITY,"
					+" (M.NAME) AS MEASUREMENT_NAME,SICD.REQ_QUANTITY,SPDIP.ALLOCATED_QUANTITY,SPED.PO_QTY,SPED.RECEIVED_QUANTITY,SPE.PO_ENTRY_ID,VD.VENDOR_NAME,"	
					+" SPDIP.TYPE_OF_PURCHASE,SPE.PO_NUMBER,SPE.VENDOR_ID,SPE.SITE_ID,S.SITE_NAME,SICAD.PURPOSE,SICAD.INDENT_CREATE_APPROVE_EMP_ID,SIC.INDENT_CREATION_ID,SIC.INDENT_NAME,SPE.PO_STATUS"
						+" from SUMADHURA_INDENT_CREATION SIC,PRODUCT P,SUB_PRODUCT S,CHILD_PRODUCT CP,MEASUREMENT M,SITE S,SUMADHURA_INDENT_CREATION_DTLS SICD"
						+" LEFT OUTER JOIN SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP ON SPDIP.INDENT_CREATION_DETAILS_ID=SICD.INDENT_CREATION_DETAILS_ID"
						+" LEFT OUTER JOIN SUMADHURA_PO_ENTRY_DETAILS SPED ON SPED.INDENT_CREATION_DTLS_ID=SICD.INDENT_CREATION_DETAILS_ID "
						+" LEFT OUTER JOIN SUMADHURA_PO_ENTRY SPE ON SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID "
						+" LEFT OUTER JOIN VENDOR_DETAILS VD ON VD.VENDOR_ID=SPE.VENDOR_ID "
						+" lEFT OUTER join (Select * from SUM_INT_CREATION_APPROVAL_DTLS where INDENT_TYPE in('R','M')) SICAD ON SICD.INDENT_CREATION_ID=SICAD.INDENT_CREATION_ID"
						+" WHERE SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID AND SICD.PRODUCT_ID=P.PRODUCT_ID AND SICD.SUB_PRODUCT_ID=S.SUB_PRODUCT_ID" 
						+" AND SICD.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID AND TRUNC(SIC.CREATE_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') and"
						+" SICD.MEASUREMENT_ID=M.MEASUREMENT_ID  AND SIC.SITE_ID='"+siteId+"' AND S.SITE_ID=SIC.SITE_ID  and (SPE.PO_STATUS  != 'REVISED' or SPE.PO_STATUS  is null) order by CHILD_PRODUCT_NAME ";
			}else if(StringUtils.isNotBlank(fromDate) && !StringUtils.isNotBlank(toDate)){

				query="select SIC.SITEWISE_INDENT_NO,SIC.PENDIND_DEPT_ID,(P.NAME) AS PRODUCT_NAME,(S.NAME) AS SUB_PRODUCT_NAME,(CP.NAME) AS CHILD_PRODUCT_NAME,"
					//+" (M.NAME) AS MEASUREMENT_NAME,SICD.REQ_QUANTITY,SPDIP.ALLOCATED_QUANTITY,SPDIP.PO_INTIATED_QUANTITY,SICD.RECEIVE_QUANTITY,"
					+" (M.NAME) AS MEASUREMENT_NAME,SICD.REQ_QUANTITY,SPDIP.ALLOCATED_QUANTITY,SPED.PO_QTY,SPED.RECEIVED_QUANTITY,SPE.PO_ENTRY_ID,VD.VENDOR_NAME,"	
					+" SPDIP.TYPE_OF_PURCHASE,SPE.PO_NUMBER,SPE.VENDOR_ID,SPE.SITE_ID,S.SITE_NAME,SICAD.PURPOSE,SICAD.INDENT_CREATE_APPROVE_EMP_ID,SIC.INDENT_CREATION_ID,SIC.INDENT_NAME,SPE.PO_STATUS"
					+" from SUMADHURA_INDENT_CREATION SIC,PRODUCT P,SUB_PRODUCT S,CHILD_PRODUCT CP,MEASUREMENT M,SITE S,SUMADHURA_INDENT_CREATION_DTLS SICD"
					+" LEFT OUTER JOIN SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP ON SPDIP.INDENT_CREATION_DETAILS_ID=SICD.INDENT_CREATION_DETAILS_ID"
					+" LEFT OUTER JOIN SUMADHURA_PO_ENTRY_DETAILS SPED ON SPED.INDENT_CREATION_DTLS_ID=SICD.INDENT_CREATION_DETAILS_ID "
					+" LEFT OUTER JOIN SUMADHURA_PO_ENTRY SPE ON SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID "
					+" LEFT OUTER JOIN VENDOR_DETAILS VD ON VD.VENDOR_ID=SPE.VENDOR_ID "
					+" lEFT OUTER join (Select * from SUM_INT_CREATION_APPROVAL_DTLS where INDENT_TYPE in('R','M') ) SICAD ON SICD.INDENT_CREATION_ID=SICAD.INDENT_CREATION_ID"
					+" WHERE SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID AND SICD.PRODUCT_ID=P.PRODUCT_ID AND SICD.SUB_PRODUCT_ID=S.SUB_PRODUCT_ID"
					+" AND SICD.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID AND TRUNC(SIC.CREATE_DATE)>= TO_DATE('"+fromDate+"', 'dd-MM-yy') and"
					+" SICD.MEASUREMENT_ID=M.MEASUREMENT_ID  AND SIC.SITE_ID='"+siteId+"' AND S.SITE_ID=SIC.SITE_ID  and (SPE.PO_STATUS  != 'REVISED'  or SPE.PO_STATUS  is null)  order by CHILD_PRODUCT_NAME";



			}else if(!StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)){

				query="select SIC.SITEWISE_INDENT_NO,SIC.PENDIND_DEPT_ID,(P.NAME) AS PRODUCT_NAME,(S.NAME) AS SUB_PRODUCT_NAME,(CP.NAME) AS CHILD_PRODUCT_NAME,"
					//+" (M.NAME) AS MEASUREMENT_NAME,SICD.REQ_QUANTITY,SPDIP.ALLOCATED_QUANTITY,SPDIP.PO_INTIATED_QUANTITY,SICD.RECEIVE_QUANTITY,"
					+" (M.NAME) AS MEASUREMENT_NAME,SICD.REQ_QUANTITY,SPDIP.ALLOCATED_QUANTITY,SPED.PO_QTY,SPED.RECEIVED_QUANTITY,SPE.PO_ENTRY_ID,VD.VENDOR_NAME,"	
						+" SPDIP.TYPE_OF_PURCHASE,SPE.PO_NUMBER,SPE.VENDOR_ID,SPE.SITE_ID,S.SITE_NAME,SICAD.PURPOSE,SICAD.INDENT_CREATE_APPROVE_EMP_ID,SIC.INDENT_CREATION_ID,SIC.INDENT_NAME,SPE.PO_STATUS"
						+" from SUMADHURA_INDENT_CREATION SIC,PRODUCT P,SUB_PRODUCT S,CHILD_PRODUCT CP,MEASUREMENT M,SITE S,SUMADHURA_INDENT_CREATION_DTLS SICD"
						+" LEFT OUTER JOIN SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP ON SPDIP.INDENT_CREATION_DETAILS_ID=SICD.INDENT_CREATION_DETAILS_ID"
						+" LEFT OUTER JOIN SUMADHURA_PO_ENTRY_DETAILS SPED ON SPED.INDENT_CREATION_DTLS_ID=SICD.INDENT_CREATION_DETAILS_ID "
						+" LEFT OUTER JOIN SUMADHURA_PO_ENTRY SPE ON SPE.PO_ENTRY_ID=SPED.PO_ENTRY_ID "
						+" LEFT OUTER JOIN VENDOR_DETAILS VD ON VD.VENDOR_ID=SPE.VENDOR_ID "
						+" lEFT OUTER join (Select * from SUM_INT_CREATION_APPROVAL_DTLS where INDENT_TYPE in('R','M')) SICAD ON SICD.INDENT_CREATION_ID=SICAD.INDENT_CREATION_ID"
						+" WHERE SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID AND SICD.PRODUCT_ID=P.PRODUCT_ID AND SICD.SUB_PRODUCT_ID=S.SUB_PRODUCT_ID" 
						+" AND SICD.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID AND TRUNC(SIC.CREATE_DATE)<= TO_DATE('"+toDate+"', 'dd-MM-yy') and"
						+" SICD.MEASUREMENT_ID=M.MEASUREMENT_ID  AND SIC.SITE_ID='"+siteId+"' AND S.SITE_ID=SIC.SITE_ID  and (SPE.PO_STATUS  != 'REVISED'  or SPE.PO_STATUS  is null)  order by CHILD_PRODUCT_NAME";

			}

			logger.info(query);

			AllProductList=jdbcTemplate.queryForList(query, new Object[] {});

			for(Map<String, Object> prods : AllProductList) {
				indentObj = new IndentCreationBean();

				indentObj.setProduct1(prods.get("PRODUCT_NAME")==null ? "" : prods.get("PRODUCT_NAME").toString());
				indentObj.setSubProduct1(prods.get("SUB_PRODUCT_NAME")==null ? "" : prods.get("SUB_PRODUCT_NAME").toString());
				indentObj.setChildProduct1(prods.get("CHILD_PRODUCT_NAME")==null ? "" : prods.get("CHILD_PRODUCT_NAME").toString());
				indentObj.setUnitsOfMeasurement1(prods.get("MEASUREMENT_NAME")==null ? "" : prods.get("MEASUREMENT_NAME").toString());
				indentObj.setPonumber(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());
				indentObj.setVendorId(prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
				indentObj.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "0" : prods.get("SITE_ID").toString()));
				indentObj.setIndentName(prods.get("INDENT_NAME")==null ? "-" : prods.get("INDENT_NAME").toString());
				indentObj.setVendorName(prods.get("VENDOR_NAME")==null ? "-" : prods.get("VENDOR_NAME").toString());
				
				RejectedEmpId=prods.get("INDENT_CREATE_APPROVE_EMP_ID")==null ? "-" : prods.get("INDENT_CREATE_APPROVE_EMP_ID").toString();
				siteName=prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString();
				indentNumber=prods.get("INDENT_CREATION_ID")==null ? "-" : prods.get("INDENT_CREATION_ID").toString();
				indentObj.setSiteName(siteName);
				indentObj.setApproverEmpId(RejectedEmpId);
				req_Quantity=(prods.get("REQ_QUANTITY")==null ? "0" : prods.get("REQ_QUANTITY").toString());
				allocatedQuan=(prods.get("ALLOCATED_QUANTITY")==null ? "0" : prods.get("ALLOCATED_QUANTITY").toString());
				receved_Quantity=(prods.get("RECEIVED_QUANTITY")==null ? "0" : prods.get("RECEIVED_QUANTITY").toString());
				indentObj.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" : prods.get("SITEWISE_INDENT_NO").toString()));
				pending_Dept_Id=(prods.get("PENDIND_DEPT_ID")==null ? "-" : prods.get("PENDIND_DEPT_ID").toString());
				remarks=(prods.get("TYPE_OF_PURCHASE")==null ? "-" : prods.get("TYPE_OF_PURCHASE").toString());
				status=prods.get("PO_STATUS")==null ? "-" : prods.get("PO_STATUS").toString();
				indentObj.setPoentryId(prods.get("PO_ENTRY_ID")==null ? "" : prods.get("PO_ENTRY_ID").toString());
				if(remarks.equals("-")){
					remarks=(prods.get("PURPOSE")==null ? "-" : prods.get("PURPOSE").toString());
					if(!remarks.contains("-")){
						String EmpName=getIndentRejectedEmpName(RejectedEmpId,indentNumber);
						remarks="<Strong>REJECTED By "+EmpName+"</Strong>: "+remarks;}
				}
				po_Initiated_Quantity=(prods.get("PO_QTY")==null ? "0" : prods.get("PO_QTY").toString());



				//po_init_quan=Double.parseDouble(po_Initiated_Quantity)+Double.parseDouble(allocatedQuan);
				
				po_init_quan=Double.parseDouble(po_Initiated_Quantity);

				request_Quantity=Double.parseDouble(req_Quantity);
				received_Quantity=Double.parseDouble(receved_Quantity);
				//	doubleRecQuantity=request_Quantity-received_Quantity;



				/*------	the below conditions for pending department --------------*/ 	

				if((request_Quantity<=po_init_quan) && (po_init_quan>received_Quantity)){

					pending_Dept_Id="VENDOR";

				}

				else if(pending_Dept_Id.equalsIgnoreCase("999") || remarks.contains("SETTLED IN CENTRAL")){

					pending_Dept_Id="CENTRAL";

				}else if(pending_Dept_Id.equalsIgnoreCase("998") || pending_Dept_Id.equalsIgnoreCase("998_PDM")){

					if((request_Quantity <= received_Quantity) || (po_init_quan!=0 && po_init_quan==received_Quantity)){

						pending_Dept_Id="RECEIVED"; 
					}else if(po_init_quan > 0){
						
						pending_Dept_Id="VENDOR";
					}
                    else if(received_Quantity >= po_init_quan && po_init_quan != 0){ //if it zero its mean  po is not created
						
						pending_Dept_Id="RECEIVED";
					}
					
					
					else{

						pending_Dept_Id="PURCHASE DEPARTMENT";
					}

				}  else if((pending_Dept_Id.equalsIgnoreCase("VND") && request_Quantity <= received_Quantity) || pending_Dept_Id.equalsIgnoreCase("REC")){

					pending_Dept_Id="RECEIVED";

				}
				else if(pending_Dept_Id.equalsIgnoreCase("VND") && request_Quantity == received_Quantity ){

					pending_Dept_Id="RECEIVED";

				} 
				else if(pending_Dept_Id.equalsIgnoreCase("VND") && po_init_quan < received_Quantity ){

					pending_Dept_Id="VENDOR";

				} else if(pending_Dept_Id.equalsIgnoreCase("VND") && po_init_quan==received_Quantity ){

					pending_Dept_Id="PURCHASE DEPARTMENT";

				} 
				
				else if(pending_Dept_Id.equals("-")){
					
					pending_Dept_Id=siteName;
				}
				if(status.equalsIgnoreCase("CNL")){
					pending_Dept_Id="CANCEL PO";
				}

				indentObj.setStrRequestQuantity(req_Quantity);
				//	indentObj.setAllocatedQuantity();
				indentObj.setReceived_Quantity(receved_Quantity);
				indentObj.setPending_Dept_Id(pending_Dept_Id);
				indentObj.setType_Of_Purchase(remarks);
				indentObj.setPoIntiatedQuantity(String.valueOf(po_init_quan));

				list.add(indentObj);

			} 

		}catch(Exception e){
			e.printStackTrace();
		}

		return list;

	}

/**************************************************************this is for central indent purpose**************************************************/
	@Override
	public int updateIndentCreationForCentral(String pendingEmpId,int siteId,int indentNumber) {
		int result=0;
		String query = "UPDATE SUMADHURA_INDENT_CREATION set PENDING_EMP_ID = ?, MODIFYDATE= sysdate , PENDIND_DEPT_ID = ? WHERE INDENT_CREATION_ID = ?";
		result = jdbcTemplate.update(query, new Object[] {
				pendingEmpId, siteId,indentNumber
		});
		return result;
		
	}
/****************************************************************Rejected Indent start***************************************************************/	
	public List<IndentCreationBean> getRejectedIndentsList(String userId) {

		List<IndentCreationBean> IndentList = new ArrayList<IndentCreationBean>();
		IndentCreationBean indentObj=null;
		List<Map<String, Object>> dbIndentDts = null;
		String type_Of_Purchase="";
		String old_Po_Number="";
		String sql="select SIC.SITEWISE_INDENT_NO,SIC.SITE_ID,SIC.INDENT_CREATION_ID,SIC.CREATE_DATE,SIC.INDENT_NAME"
					+" from SUMADHURA_INDENT_CREATION SIC,SUM_INT_CREATION_APPROVAL_DTLS SICAD"
					+" WHERE SIC.INDENT_CREATE_EMP_ID=? AND SICAD.INDENT_TYPE='R' AND SIC.INDENT_CREATION_ID=SICAD.INDENT_CREATION_ID";
		dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {userId});
		for(Map<String, Object> prods : dbIndentDts) {
			indentObj = new IndentCreationBean();

			indentObj.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "" : prods.get("SITEWISE_INDENT_NO").toString()));
			indentObj.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString()));
			indentObj.setStrCreateDate(prods.get("CREATE_DATE")==null ? "" : prods.get("CREATE_DATE").toString());
			indentObj.setIndentNumber(Integer.parseInt(prods.get("INDENT_CREATION_ID")==null ? "" : prods.get("INDENT_CREATION_ID").toString()));
			indentObj.setIndentName(prods.get("INDENT_NAME")==null ? "-" : prods.get("INDENT_NAME").toString());
			IndentList.add(indentObj);

		}

		return IndentList;

	}
	
	
	
/*	********************************************************reject purpose****************************************************************************/
	@Override
	public List<String> getPendingEmployeeIdForIndent(String user_id,int reqSiteId) {

		String strApproverEmpId = "";
		List<Map<String, Object>> dbIndentDts = null;
		List<String> List = new ArrayList<String> ();
		String query = "SELECT EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL where  MODULE_TYPE='INDENT' AND SITE_ID=?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id,reqSiteId});  

		for(Map<String, Object> prods : dbIndentDts) {
			strApproverEmpId = prods.get("EMP_ID")==null ? "" :   prods.get("EMP_ID").toString();
			List.add(strApproverEmpId);
			//icb.setFromEmpName(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());		
		}

		return List;
	}
	
	/******************************************************************for mail purpose***********************************************************/
	@Override
public String[] getEmailsOfEmployeesInAllLowerDeptOfThisEmployee(String indentNumber) {
		
		
		List<String> emailList = new ArrayList<String>();
		
		List<Map<String, Object>> dbIndentDts = null;
		
			
			String query = "select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in ( "
						+" select distinct(INDENT_CREATE_APPROVE_EMP_ID) from SUM_INT_CREATION_APPROVAL_DTLS where "
						+" INDENT_CREATION_ID =?)";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{indentNumber});
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
	@Override
	public String getEmpdesignation(String user_Id) {
		String query = "SELECT EMP_DESIGNATION FROM SUMADHURA_EMPLOYEE_DETAILS where EMP_ID ='"+user_Id+"'";
		String designation = jdbcTemplate.queryForObject(query,String.class);  
		return designation;

	}
	@Override
	public String getIndentRejectedComments(int oldIndentNumber) {
		String query = "SELECT PURPOSE FROM SUM_INT_CREATION_APPROVAL_DTLS where INDENT_CREATION_ID ='"+oldIndentNumber+"' and INDENT_TYPE='R'";
		String purpose = jdbcTemplate.queryForObject(query,String.class);  
		return purpose;

	}
	
	public int updateIndentCreationApprovetbl(String oldIndentNumber) {
		int result=0;
		String query = "UPDATE SUM_INT_CREATION_APPROVAL_DTLS set INDENT_TYPE ='M' WHERE INDENT_CREATION_ID = ? AND INDENT_TYPE='R'";
		result = jdbcTemplate.update(query, new Object[] {oldIndentNumber });
		return result;
		
	}
/*	*******************************************this is for get emp name***********************************************************************/
	public String getIndentRejectedEmpName(String  rejectedEmpId,String indentNumber) {
		String query = "SELECT SED.EMP_NAME FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED"
					+" where SICAD.INDENT_CREATE_APPROVE_EMP_ID='"+rejectedEmpId+"' AND SICAD.INDENT_CREATION_ID='"+indentNumber+"' AND SICAD.INDENT_CREATE_APPROVE_EMP_ID=SED.EMP_ID";
		String empName = jdbcTemplate.queryForObject(query,String.class);  
		return empName;

	}
	
	/*=========================================Material BOQ started ============================================================================*/
	// this is for getting the child produts for based in the group id
	public Map<String,String> getChildProductsWithGroupId(String groupId) {

		Map<String,String> childIds =new HashMap<String,String>();
		List<Map<String, Object>> dbIndentDts = null;
		String childProdId="";
		//String childProductList="";
		String measumentId="";
		if(!groupId.equals("") && groupId!=null){
			String sql="select C.CHILD_PRODUCT_ID,M.MEASUREMENT_ID from CHILD_PRODUCT C,MEASUREMENT M WHERE C.MATERIAL_GROUP_ID=? AND M.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {groupId});
			for(Map<String, Object> prods : dbIndentDts) {
				childProdId=(prods.get("CHILD_PRODUCT_ID")==null ? "" : prods.get("CHILD_PRODUCT_ID").toString());
				measumentId=(prods.get("MEASUREMENT_ID")==null ? "" : prods.get("MEASUREMENT_ID").toString());
				childIds.put(childProdId,measumentId);
			}
		}
		
		return childIds;

	}
	// getting the received quantity for receive and dc form based on the child product ids
	public double getindentAndDcReceivedQuantity(String childProductList,String siteId,String measurementList) {

		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbDcDts = null;
		double indent_Quantity=0.0;
		double dc_Quantity=0.0;
		double totalQuantity=0.0;
		//String childProductList="";
		if(!childProductList.equals("") && childProductList!=null){
			childProductList=childProductList.replace(",","','");
			measurementList=measurementList.replace(",","','");
			String sql="SELECT SUM(IED.RECEVED_QTY) as RECEIVED_QUANTITY FROM INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE WHERE IED.CHILD_PRODUCT_ID in ('"+childProductList+"') AND IED.MEASUR_MNT_ID in('"+measurementList+"') and IE.SITE_ID=? AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {siteId});

			String query="SELECT SUM(DF.RECEVED_QTY) as DC_QUANTITY FROM DC_FORM DF,DC_ENTRY DE WHERE DF.CHILD_PRODUCT_ID in ('"+childProductList+"') AND DF.MEASUR_MNT_ID in('"+measurementList+"') and DE.SITE_ID=? AND DF.STATUS='A' AND DF.DC_ENTRY_ID=DE.DC_ENTRY_ID";
			dbDcDts = jdbcTemplate.queryForList(query, new Object[] {siteId});

			for(Map<String, Object> prods : dbIndentDts) {
				indent_Quantity=Double.valueOf(prods.get("RECEIVED_QUANTITY")==null ? "0" : prods.get("RECEIVED_QUANTITY").toString());
			}
			for(Map<String, Object> prod : dbDcDts) {
				dc_Quantity=Double.valueOf(prod.get("DC_QUANTITY")==null ? "0" : prod.get("DC_QUANTITY").toString());
			}
			totalQuantity=indent_Quantity+dc_Quantity;
		}

		return totalQuantity;

	}
	// this is used to getting the issued quantity i.e issue to other site,issue to scrap,issue to theft
	public double getIssuedQuantity(String childProductList,String siteId,String measurementList) {
		List<Map<String, Object>> dbIndentDts = null;
		
		double issued_Quantity=0.0;
		if(!childProductList.equals("") && childProductList!=null){
			childProductList=childProductList.replace(",","','");
			measurementList=measurementList.replace(",","','");
			String sql="SELECT SUM(IED.ISSUED_QTY) as ISSUED_QUANTITY FROM INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE  WHERE IED.CHILD_PRODUCT_ID in ('"+childProductList+"') AND IED.MEASUR_MNT_ID in('"+measurementList+"') AND IE.INDENT_TYPE IN('OUTO','OUTS','OUTT') AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND IE.SITE_ID=?";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {siteId});
			for(Map<String, Object> prods : dbIndentDts) {
				issued_Quantity=Double.valueOf(prods.get("ISSUED_QUANTITY")==null ? "0" : prods.get("ISSUED_QUANTITY").toString());
			}
		}

		return issued_Quantity;

	}
	// this is used to getting the indent pending quantity i.e reqQuantity and receiveQuantity 
	public double getIndentPendingQuantity(String childProductList,String siteId,String measurementList) {
		List<Map<String, Object>> dbIndentDts = null;
		
		double indent_available_Quantity=0.0;
		if(!childProductList.equals("") && childProductList!=null){
			childProductList=childProductList.replace(",","','");
			measurementList=measurementList.replace(",","','");
			String sql="SELECT SUM(SICD.REQ_QUANTITY)-SUM(SICD.RECEIVE_QUANTITY) as AVAILABLE_QUANTITY FROM SUMADHURA_INDENT_CREATION SIC,SUMADHURA_INDENT_CREATION_DTLS SICD WHERE SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID AND SICD.CHILD_PRODUCT_ID in ('"+childProductList+"') AND SICD.MEASUREMENT_ID in('"+measurementList+"') AND SIC.SITE_ID=? AND SIC.STATUS='A'";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {siteId});
			for(Map<String, Object> prods : dbIndentDts) {
				indent_available_Quantity=Double.valueOf(prods.get("AVAILABLE_QUANTITY")==null ? "0" : prods.get("AVAILABLE_QUANTITY").toString());
			}
		}

		return indent_available_Quantity;

	}
	// getting the TOTAL boq Quantity from qs_product_dtls
	public double getBOQQuantity(String groupId,String siteId) {
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbDts = null;
		
		double total_BOQ_Quantity=0.0;
		double buffer_Quantity=0.0;
		double totalQuantity=0.0;
		if(!groupId.equals("") && groupId!=null){
			String sql="SELECT SUM(TOTAL_QUANTITY) AS TOTAL FROM QS_BOQ_PRODUCT_DTLS  WHERE MATERIAL_GROUP_ID=? AND  SITE_ID=? AND STATUS='A'";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {groupId,siteId});

			String query="SELECT SUM(QUANTITY) AS BUFFERQUAN FROM QS_BUFFER_STOCK  WHERE MATERIAL_GROUP_ID=? AND  SITE_ID=?";
			dbDts = jdbcTemplate.queryForList(query, new Object[] {groupId,siteId});
			if(dbIndentDts!=null && dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				total_BOQ_Quantity=Double.valueOf(prods.get("TOTAL")==null ? "0" : prods.get("TOTAL").toString());
			}
			}
			if(dbDts!=null && dbDts.size()>0){
			for(Map<String, Object> prods : dbDts) {
				buffer_Quantity=Double.valueOf(prods.get("BUFFERQUAN")==null ? "0" : prods.get("BUFFERQUAN").toString());
			}
			}
			totalQuantity=total_BOQ_Quantity+buffer_Quantity;
		}

		return totalQuantity;

	}
	// getting prevoius quantity for ajax call
	public double getIndentPreviousQuantity(String childProductList,String indentNumber,String siteId) {
		List<Map<String, Object>> dbIndentDts = null;
		
		double indent_Quantity=0.0;
		if(!indentNumber.equals("") && indentNumber!=null){
			childProductList=childProductList.replace(",","','");
			String sql="SELECT SUM(SICD.REQ_QUANTITY) AS QUANTITY FROM SUMADHURA_INDENT_CREATION SIC,SUMADHURA_INDENT_CREATION_DTLS SICD " 
					+" WHERE SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID AND SICD.CHILD_PRODUCT_ID in ('"+childProductList+"') " 
					+" AND SIC.SITE_ID=? AND SIC.STATUS='A' AND SICD.INDENT_CREATION_ID=?";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {siteId,indentNumber});
			for(Map<String, Object> prods : dbIndentDts) {
				indent_Quantity=Double.valueOf(prods.get("QUANTITY")==null ? "0" : prods.get("QUANTITY").toString());
			}
		}

		return indent_Quantity;

	}
	
	
}

