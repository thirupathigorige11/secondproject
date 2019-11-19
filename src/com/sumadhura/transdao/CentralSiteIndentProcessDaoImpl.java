package com.sumadhura.transdao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.IndentCreationDto;

@Repository
public class CentralSiteIndentProcessDaoImpl implements CentralSiteIndentProcessDao{


	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;


	@Override
	public int getSiteIdByName(String reqSiteName) {
		String query = "SELECT SITE_ID FROM SITE where SITE_NAME = '"+reqSiteName+"'";
		int result = jdbcTemplate.queryForInt(query);  
		return result;
	}






	@Override
	public int getPurchaseIndentProcessSequenceNumber() {
		int centralIndentProcessSeqNum = jdbcTemplate.queryForInt("SELECT PURCHASE_DEPT_INDENT_PROSS_SEQ.NEXTVAL FROM DUAL");
		return centralIndentProcessSeqNum;
	}
	@Override
	public int getCentralIndentRequestDetailsSequenceNumber() {
		int centralIndentProcessSeqNum = jdbcTemplate.queryForInt("SELECT CNTL_INDENT_REQ_DTLS_SEQ.NEXTVAL FROM DUAL");
		return centralIndentProcessSeqNum;
	}

/*@description this method is validatig the quantity of settement time*/
	@Override
	public boolean validateSiteQuantity(IndentCreationBean indentCreationBean, int indentProcessId, int senderSite) {
		
		String query = "SELECT  IA.PRODUT_QTY FROM  INDENT_AVAILABILITY IA  WHERE IA.CHILD_PRODUCT_ID = ?  and IA.MESURMENT_ID=?  and IA.SITE_ID=? ";
		//this is available quantity
		Double	sumOfIndentAvailability = jdbcTemplate.queryForObject(query, new Object[] {indentCreationBean.getChildProductId1(),indentCreationBean.getUnitsOfMeasurementId1(),senderSite},Double.class);
		//this is the requested quantity for the sender site
		double requestedQtyFromStore=Double.valueOf(indentCreationBean.getRequiredQuantity1());
		//if requested quantity is more than available quantity in site, then we have to return the error 
		//message to front end, that you can not request quantity than available quantity in specific site
		if(requestedQtyFromStore>sumOfIndentAvailability){
			return true;
		}
		
		return false;
	}

	@Override
	public List<Map<String, Object>> getIndentCreatedEmpName(int reqSiteId, int indentNumber) {
		String query="SELECT SED.EMP_EMAIL,SED.DEPT_ID,SED.MOBILE_NUMBER,SED.USER_PROFILE,SED.EMP_NAME,SICAD.PURPOSE from  SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED "
				+ " where SICAD.INDENT_CREATION_ID = ? AND SED.EMP_ID=SICAD.INDENT_CREATE_APPROVE_EMP_ID";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query,indentNumber);
		
		return list;
	}


	@Override
	public IndentCreationBean getIndentFromAndToDetails(String user_id, IndentCreationBean icb) {
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select ((SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID = ?)) as emp_name, "
			+"SED.EMP_NAME as approver_name,SED.EMP_ID as approver_id FROM SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_APPROVER_MAPPING_DTL  SAMD WHERE SED.EMP_ID = SAMD.APPROVER_EMP_ID and "
			+"SAMD.EMP_ID = ? AND SAMD.MODULE_TYPE='INDENT'";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id,user_id});

		for(Map<String, Object> prods : dbIndentDts) {
			icb.setApproverEmpId(prods.get("APPROVER_ID")==null ? "" :   prods.get("APPROVER_ID").toString());
			icb.setIndentFrom(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());		
			icb.setIndentTo(prods.get("APPROVER_NAME")==null ? "" :   prods.get("APPROVER_NAME").toString());			}

		return icb;
	}


	@Override
	public List<IndentCreationBean> getIndentFromAndToDetails(String centralDeptId) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;

		/*String query = "SELECT SIC.INDENT_CREATION_ID ,SIC.CREATE_DATE, SED.EMP_NAME ,S.SITE_NAME,S.SITE_ID , REQUIRED_DATE "+
				"FROM SUMADHURA_INDENT_CREATION SIC, SUMADHURA_EMPLOYEE_DETAILS SED, SITE S "+
				"WHERE  SIC.PENDING_EMP_ID = '-' and SIC.PENDIND_DEPT_ID = ? and SIC.STATUS = 'A' AND "+
				"SIC.INDENT_CREATE_EMP_ID = SED.EMP_ID AND SIC.SITE_ID = S.SITE_ID ";*/


		/*String query = "SELECT distinct(SIC.INDENT_CREATION_ID) ,SIC.CREATE_DATE, SED.EMP_NAME ,S.SITE_NAME,S.SITE_ID , REQUIRED_DATE ," +
		"SPDIP.CREATION_DATE  as PURCHASE_DEPT_RECEIVEDATE "+
		"FROM SUMADHURA_INDENT_CREATION SIC, SUMADHURA_EMPLOYEE_DETAILS SED, SITE S , "+
		"SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP,  "+
		"SUMADHURA_INDENT_CREATION_DTLS SICD "+
		" WHERE  SIC.PENDING_EMP_ID = '-' and SIC.PENDIND_DEPT_ID = ? and SIC.STATUS = 'A' AND  "+
		"SIC.INDENT_CREATE_EMP_ID = SED.EMP_ID AND SIC.SITE_ID = S.SITE_ID and "+
		" SIC.INDENT_CREATION_ID = SICD.INDENT_CREATION_ID and "+
		" SPDIP.INDENT_CREATION_DETAILS_ID = SICD.INDENT_CREATION_DETAILS_ID ";*/
		String query = "SELECT max(SIC.SITEWISE_INDENT_NO) as SITEWISE_INDENT_NO,max(SIC.INDENT_CREATION_ID) as INDENT_CREATION_ID ,max(SIC.CREATE_DATE) as CREATE_DATE, "
				+ " max(SED.EMP_NAME) as EMP_NAME,max(S.SITE_NAME) as SITE_NAME,max(S.SITE_ID) as SITE_ID ,max(SIC.INDENT_NAME) as INDENT_NAME, "
				+ " max(SIC.REQUIRED_DATE) as REQUIRED_DATE ,	max(SPDIP.CREATION_DATE)  as PURCHASE_DEPT_RECEIVEDATE 	FROM SUMADHURA_INDENT_CREATION SIC,"
				+ " SUMADHURA_EMPLOYEE_DETAILS SED, SITE S , 	SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP,  	SUMADHURA_INDENT_CREATION_DTLS SICD "
				+ " WHERE  SIC.PENDING_EMP_ID = '-' and SIC.PENDIND_DEPT_ID = ? and SIC.STATUS = 'A' AND  	"
				+ "	SIC.INDENT_CREATE_EMP_ID = SED.EMP_ID AND SIC.SITE_ID = S.SITE_ID and  SIC.INDENT_CREATION_ID = SICD.INDENT_CREATION_ID and 	"
				+ " SPDIP.INDENT_CREATION_DETAILS_ID = SICD.INDENT_CREATION_DETAILS_ID group by SIC.INDENT_CREATION_ID ";
		

		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {centralDeptId});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setIndentNumber(Integer.parseInt(prods.get("INDENT_CREATION_ID")==null ? "0" : prods.get("INDENT_CREATION_ID").toString()));
			indentCreationBean.setIndentName((prods.get("INDENT_NAME")==null ? "-" : prods.get("INDENT_NAME").toString()));


			String strCreateDate = prods.get("CREATE_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("CREATE_DATE").toString();
			String strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			String strurchase = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			String purchaseDeptDate=prods.get("PURCHASE_DEPT_RECEIVEDATE")==null ? "0000-00-00 00:00:00.000" : prods.get("PURCHASE_DEPT_RECEIVEDATE").toString();

			Date createDate = null;
			Date requiredDate = null;
			Date strPurchaseDeptDate=null;
			try {
				createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strCreateDate);
				requiredDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strRequiredDate);
				strPurchaseDeptDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(purchaseDeptDate);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			strCreateDate = new SimpleDateFormat("dd-MM-yyyy").format(createDate);
			strRequiredDate = new SimpleDateFormat("dd-MM-yyyy").format(requiredDate);
			purchaseDeptDate=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(strPurchaseDeptDate);




			indentCreationBean.setStrCreateDate(strCreateDate);
			indentCreationBean.setStrRequiredDate(strRequiredDate);
			indentCreationBean.setIndentFrom(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());
			indentCreationBean.setSiteName(prods.get("SITE_NAME")==null ? "" :   prods.get("SITE_NAME").toString());
			indentCreationBean.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "" :   prods.get("SITE_ID").toString()));
			indentCreationBean.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" :   prods.get("SITEWISE_INDENT_NO").toString()));
			indentCreationBean.setPurchaseDeptReceivedDate(purchaseDeptDate);
			indentCreationBean.setPurpose("PO");
			list.add(indentCreationBean);
		}
		return list;
	}



	@Override
	public List<IndentCreationBean> getSpecificSiteIndentFromAndToDetails(String centralDeptId,String strSiteId) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		int i=1;
		String query =new StringBuilder("SELECT") 
				.append(" max(SIC.SITEWISE_INDENT_NO) as SITEWISE_INDENT_NO,max(SIC.INDENT_CREATION_ID) as INDENT_CREATION_ID ,")
				.append(" max(SIC.CREATE_DATE) as CREATE_DATE,max(SED.EMP_NAME) as EMP_NAME,max(S.SITE_NAME) as SITE_NAME,max(S.SITE_ID) as SITE_ID ,max(SIC.INDENT_NAME) as INDENT_NAME,") 
				.append(" max(SIC.REQUIRED_DATE) as REQUIRED_DATE ,	max(SPDIP.CREATION_DATE)  as PURCHASE_DEPT_RECEIVEDATE 	FROM SUMADHURA_INDENT_CREATION SIC,")
				.append(" SUMADHURA_EMPLOYEE_DETAILS SED, SITE S , 	SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP,  	SUMADHURA_INDENT_CREATION_DTLS SICD ")
				.append(" WHERE  SIC.PENDIND_DEPT_ID ='"+centralDeptId+"' and SIC.STATUS = 'A' AND  	SIC.SITE_ID ='"+strSiteId+"' and ")
				.append(" SIC.INDENT_CREATE_EMP_ID = SED.EMP_ID AND SIC.SITE_ID = S.SITE_ID and  SIC.INDENT_CREATION_ID = SICD.INDENT_CREATION_ID and ") 	
				.append(" SPDIP.INDENT_CREATION_DETAILS_ID = SICD.INDENT_CREATION_DETAILS_ID group by SIC.INDENT_CREATION_ID ").toString();
		dbIndentDts = jdbcTemplate.queryForList(query);
		//List<Map<String,Object>> expendituresList = jdbcTemplate.queryForList(query);
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setIndentNumber(Integer.parseInt(prods.get("INDENT_CREATION_ID")==null ? "0" : prods.get("INDENT_CREATION_ID").toString()));
			String strCreateDate = prods.get("CREATE_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("CREATE_DATE").toString();
			String strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			String purchaseDeptDate=prods.get("PURCHASE_DEPT_RECEIVEDATE")==null ? "0000-00-00 00:00:00.000" : prods.get("PURCHASE_DEPT_RECEIVEDATE").toString();
			Date createDate = null;
			Date requiredDate = null;
			Date strpurchaseDeptDate=null;
			try {
				createDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strCreateDate);
				requiredDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strRequiredDate);
				strpurchaseDeptDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(purchaseDeptDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			strCreateDate = new SimpleDateFormat("dd-MM-yyyy").format(createDate);
			strRequiredDate = new SimpleDateFormat("dd-MM-yyyy").format(requiredDate);
			purchaseDeptDate=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(strpurchaseDeptDate);
			indentCreationBean.setStrCreateDate(strCreateDate);
			indentCreationBean.setStrRequiredDate(strRequiredDate);
			indentCreationBean.setIndentFrom(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());
			//indentCreationBean.setSiteName(prods.get("SITE_NAME")==null ? "" :   prods.get("SITE_NAME").toString());
			indentCreationBean.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "" :   prods.get("SITE_ID").toString()));
			indentCreationBean.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" :   prods.get("SITEWISE_INDENT_NO").toString()));
			indentCreationBean.setIndentName(prods.get("INDENT_NAME")==null ? "-" : prods.get("INDENT_NAME").toString());
			indentCreationBean.setPurchaseDeptReceivedDate(purchaseDeptDate);
			indentCreationBean.setPurpose("PO");
			indentCreationBean.setStrSerialNumber(String.valueOf(i));
			list.add(indentCreationBean);
			i++;
		}
		return list;
	}


	@Override
	public List<IndentCreationBean> getCentralIndentDetailsLists(int indentNumber,String strSiteId) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		int strSerialNumber = 0;
		String strRemarks="";
		//String strdetailedRemarks="";
		String query = "SELECT P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,"+
		"SCIP.PRODUCT_ID,SCIP.SUB_PRODUCT_ID,SCIP.CHILD_PRODUCT_ID,SCIP.MEASUREMENT_ID,"+
		"SCIP.CENTRAL_REQ_QUANTITY,SCIP.ALLOCATED_QUANTITY,SCIP.PENDING_QUANTIY,SCIP.INTIATED_QUANTITY,"
		+ "SICD.REMARKS,SCIP.INDENT_CREATION_DETAILS_ID, SCIP.INDENT_PROCESS_ID,SCIP.INDENT_REQ_QUANTITY FROM SUMADHURA_INDENT_CREATION_DTLS SICD, SUMADHURA_CNTL_INDENT_PROCESS SCIP, PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST "+
		"WHERE SCIP.PRODUCT_ID=P.PRODUCT_ID AND SCIP.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SCIP.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID "+
		"AND SCIP.MEASUREMENT_ID=MST.MEASUREMENT_ID  AND SICD.INDENT_CREATION_DETAILS_ID = SCIP.INDENT_CREATION_DETAILS_ID AND SICD.INDENT_CREATION_ID= ? order by SICD.INDENT_CREATION_DETAILS_ID";
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
			indentCreationBean.setRequiredQuantity1(prods.get("CENTRAL_REQ_QUANTITY")==null ? "" :   prods.get("CENTRAL_REQ_QUANTITY").toString());
			indentCreationBean.setAllocatedQuantity(prods.get("ALLOCATED_QUANTITY")==null ? "" :   prods.get("ALLOCATED_QUANTITY").toString());
			indentCreationBean.setPendingQuantity(prods.get("PENDING_QUANTIY")==null ? "" :   prods.get("PENDING_QUANTIY").toString());
			indentCreationBean.setIntiatedQuantity(prods.get("INTIATED_QUANTITY")==null ? "" :   prods.get("INTIATED_QUANTITY").toString());
			
			indentCreationBean.setIndentCreationDetailsId(Integer.parseInt(prods.get("INDENT_CREATION_DETAILS_ID")==null ? "" :   prods.get("INDENT_CREATION_DETAILS_ID").toString()));
			indentCreationBean.setIndentProcessId(Integer.parseInt(prods.get("INDENT_PROCESS_ID")==null ? "" :   prods.get("INDENT_PROCESS_ID").toString()));
			indentCreationBean.setIndentRequestQuantity(prods.get("INDENT_REQ_QUANTITY")==null ? "" :   prods.get("INDENT_REQ_QUANTITY").toString());
			strRemarks=(prods.get("REMARKS")==null ? "" :   prods.get("REMARKS").toString());
			indentCreationBean.setRemarks1(strRemarks);
			if(strRemarks.contains("@@@")){
				String strdetailedRemarks="";
				String strRemarksArr[] = strRemarks.split("@@@");
				int j=0;
				for(int i =0 ; i< strRemarksArr.length;i++){
					if(!strRemarksArr [i].equals("") && !strRemarksArr [i].equals(" ")){
					strdetailedRemarks += " "+(j+1)+") "+strRemarksArr [i];
					j++;
					}
				}
				strRemarks = strdetailedRemarks;
			}
			
			
			indentCreationBean.setRemarks(strRemarks);// this is for show the coment
			//System.out.println(">>"+indentCreationBean.getRequiredQuantity1()+"<< coming");
			strSerialNumber++;
			indentCreationBean.setStrSerialNumber(String.valueOf(strSerialNumber));
			List<Map<String, Object>> listOfIndentAvailability = null;
			String subquery = "SELECT S.SITE_NAME, IA.PRODUT_QTY FROM  INDENT_AVAILABILITY IA,SITE S WHERE IA.CHILD_PRODUCT_ID = ? AND IA.SITE_ID=S.SITE_ID";
			listOfIndentAvailability = jdbcTemplate.queryForList(subquery, new Object[] {childProductId});
			String strMouseOverData ="";
			for(Map<String, Object> prods1 : listOfIndentAvailability) {
				String siteName = prods1.get("SITE_NAME")==null ? "" :   prods1.get("SITE_NAME").toString();
				String quantity = prods1.get("PRODUT_QTY")==null ? "" :   prods1.get("PRODUT_QTY").toString();
				strMouseOverData = strMouseOverData+siteName+"="+quantity+"\n";
			}
			//System.out.println("strMouseOverData: >>>"+strMouseOverData+"<<<");
			indentCreationBean.setStrMouseOverData(strMouseOverData);
			list.add(indentCreationBean);
		}	
		return list;
	}
	@Override //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
	public List<IndentCreationBean> getPurchaseIndentDetailsLists(int indentNumber,String strSiteId) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		int strSerialNumber = 0;
		String strRemarks="";
		//String strdetailedRemarks="";
		double po_quantity=0.0;
		//Date myDate = new Date();
		//String requesteddate = new SimpleDateFormat("dd-MMM-YY").format(myDate);
		//String siteIdQuery = "SELECT SITE_ID FROM SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = "+indentNumber; 
		//String siteId = jdbcTemplate.queryForObject(siteIdQuery, String.class);
		String query = "SELECT  SIC.VERSION_NO,SIC.REFERENCE_NO,SIC.ISSUE_DATE, P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,CP.MATERIAL_GROUP_ID,MST.NAME as MEASUREMENT_NAME,"+
		" SPDIP.PRODUCT_ID,SPDIP.SUB_PRODUCT_ID,SPDIP.CHILD_PRODUCT_ID,SPDIP.MEASUREMENT_ID,SPDIP.PURCHASE_DEPT_REQ_QUANTITY,SICD.REMARKS,SICD.AVAIL_QUANTITY_AT_CREATION,SICD.REQ_QUANTITY as QUANTITY,SPDIP.CLOSED_INDENT_QUANT, "+
		"(SPDIP.PENDING_QUANTIY-SPDIP.PO_INTIATED_QUANTITY-CLOSED_INDENT_QUANT) as PO_QUANTITY,SPDIP.PENDING_QUANTIY,SPDIP.INDENT_CREATION_DETAILS_ID,SPDIP.PO_INTIATED_QUANTITY ," +
		"SPDIP.PURCHASE_DEPT_INDENT_PROSS_SEQ FROM SUMADHURA_INDENT_CREATION SIC, SUMADHURA_INDENT_CREATION_DTLS SICD, SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP, PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST "+
		"WHERE SPDIP.PRODUCT_ID=P.PRODUCT_ID AND SPDIP.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SPDIP.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID "+
		"AND SPDIP.MEASUREMENT_ID=MST.MEASUREMENT_ID AND SPDIP.INDENT_CREATION_DETAILS_ID= SICD.INDENT_CREATION_DETAILS_ID  and SPDIP.STATUS='A' "+
		"AND SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID AND SIC.INDENT_CREATION_ID = ?  and SIC. SITE_ID = ? order by SICD.INDENT_CREATION_DETAILS_ID";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber,strSiteId});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			//ACP
			indentCreationBean.setVersionNo(prods.get("VERSION_NO")==null ? "" :   prods.get("VERSION_NO").toString());
			indentCreationBean.setReference_No(prods.get("REFERENCE_NO")==null ? "" :   prods.get("REFERENCE_NO").toString());
			indentCreationBean.setIssue_date(prods.get("ISSUE_DATE")==null ? "" :   prods.get("ISSUE_DATE").toString());
		
			System.out.println("central site indent \t"+indentCreationBean.getVersionNo());
	
			po_quantity=Double.parseDouble(prods.get("PO_QUANTITY")==null ? "" :   prods.get("PO_QUANTITY").toString());
			if(po_quantity >0){
			indentCreationBean.setRequiredQuantity1(String.valueOf(po_quantity));
			String prodId = prods.get("PRODUCT_ID")==null ? "" :   prods.get("PRODUCT_ID").toString();
			String subProductId = prods.get("SUB_PRODUCT_ID")==null ? "" :   prods.get("SUB_PRODUCT_ID").toString();
			String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
			String measurementId = prods.get("MEASUREMENT_ID")==null ? "" :   prods.get("MEASUREMENT_ID").toString();

			indentCreationBean.setProductId1(prodId);
			indentCreationBean.setSubProductId1(subProductId);
			indentCreationBean.setChildProductId1(childProductId);
			indentCreationBean.setUnitsOfMeasurementId1(measurementId);
			indentCreationBean.setProduct1(prods.get("PRODUCT_NAME")==null ? "" :   prods.get("PRODUCT_NAME").toString());
			indentCreationBean.setSubProduct1(prods.get("SUB_PRODUCT_NAME")==null ? "" :   prods.get("SUB_PRODUCT_NAME").toString());
			indentCreationBean.setChildProduct1(prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString());
			indentCreationBean.setUnitsOfMeasurement1(prods.get("MEASUREMENT_NAME")==null ? "" :   prods.get("MEASUREMENT_NAME").toString());
			indentCreationBean.setStrRequestQuantity(prods.get("PURCHASE_DEPT_REQ_QUANTITY")==null ? "" :   prods.get("PURCHASE_DEPT_REQ_QUANTITY").toString());
			indentCreationBean.setGroupId1(prods.get("MATERIAL_GROUP_ID")==null ? "0" :   prods.get("MATERIAL_GROUP_ID").toString());
			indentCreationBean.setIndentCreationDetailsId(Integer.parseInt(prods.get("INDENT_CREATION_DETAILS_ID")==null ? "" :   prods.get("INDENT_CREATION_DETAILS_ID").toString()));
			indentCreationBean.setPendingQuantity(prods.get("PENDING_QUANTIY")==null ? "" :   prods.get("PENDING_QUANTIY").toString());
			indentCreationBean.setPurchaseDepartmentIndentProcessSeqId(Integer.parseInt(prods.get("PURCHASE_DEPT_INDENT_PROSS_SEQ")==null ? "" :   prods.get("PURCHASE_DEPT_INDENT_PROSS_SEQ").toString()));
			indentCreationBean.setPoIntiatedQuantity(prods.get("PO_INTIATED_QUANTITY")==null ? "" :   prods.get("PO_INTIATED_QUANTITY").toString());
			indentCreationBean.setIndentRequestQuantity(prods.get("INDENT_REQ_QUANTITY")==null ? "" :   prods.get("INDENT_REQ_QUANTITY").toString());
			strRemarks=(prods.get("REMARKS")==null ? "" :   prods.get("REMARKS").toString());
			indentCreationBean.setProductAvailability1(prods.get("AVAIL_QUANTITY_AT_CREATION")==null ? "0" :   prods.get("AVAIL_QUANTITY_AT_CREATION").toString());
			indentCreationBean.setPrintQuantity(prods.get("QUANTITY")==null ? "0" :   prods.get("QUANTITY").toString());
			indentCreationBean.setClosed_Indent_Quan(prods.get("CLOSED_INDENT_QUANT")==null ? "0" :   prods.get("CLOSED_INDENT_QUANT").toString());
			
			/**/
			/*String dbProductAvailability = "";	
			String indentAvaQry = "SELECT  SUM(AVAILABLE_QUANTITY) FROM SUMADHURA_PRICE_LIST  WHERE PRODUCT_ID = ? AND SUB_PRODUCT_ID = ? AND CHILD_PRODUCT_ID = ? AND UNITS_OF_MEASUREMENT = ? AND SITE_ID = ? AND CREATED_DATE<= TO_DATE(?,'dd-MM-yy')";
			dbProductAvailability = jdbcTemplate.queryForObject(indentAvaQry, new Object[] {
					prodId,
					subProductId,
					childProductId,
					measurementId,siteId,requesteddate

			},String.class

			);

			if(dbProductAvailability == null || dbProductAvailability.equals("")){

				dbProductAvailability = "0";
			}


			indentCreationBean.setProductAvailability1(dbProductAvailability);*/

			/**/

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
			
			
			strSerialNumber++;
			indentCreationBean.setStrSerialNumber(String.valueOf(strSerialNumber));
			list.add(indentCreationBean);
		}
		}
		return list;
	}


	@Override
	public int insertPurchaseIndentProcess(int purchaseIndentProcessId,IndentCreationBean purchaseIndentDetails,int IndentCreationDetailsId,int indentReqSiteId,String reqReceiveFrom,IndentCreationDto centralTableData)
	{
		
		double dept_Req_Quantity=Double.valueOf(centralTableData.getReqQuantity())-Double.valueOf(centralTableData.getIntiatedQuantity());
		double pending_Quantity=Double.valueOf(centralTableData.getPendingQuantity())-Double.valueOf(centralTableData.getIntiatedQuantity());
		//double double_Pending_Quan=
		
		String query = "INSERT INTO SUM_PURCHASE_DEPT_INDENT_PROSS(PURCHASE_DEPT_INDENT_PROSS_SEQ,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MEASUREMENT_ID,"+
		"PURCHASE_DEPT_REQ_QUANTITY, ALLOCATED_QUANTITY, PENDING_QUANTIY, PO_INTIATED_QUANTITY,"+
		"STATUS,INDENT_REQ_SITE_ID,REQ_RECEIVE_FROM,CREATION_DATE,INDENT_CREATION_DETAILS_ID,INDENT_REQ_QUANTITY,CLOSED_INDENT_QUANT) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,sysdate,?,?,?)";
		int result = jdbcTemplate.update(query, new Object[] {
				purchaseIndentProcessId,
				purchaseIndentDetails.getProductId1(),
				purchaseIndentDetails.getSubProductId1(),
				purchaseIndentDetails.getChildProductId1(),
				purchaseIndentDetails.getUnitsOfMeasurementId1(),
				dept_Req_Quantity,
				centralTableData.getAllocatedQuantity(),
				pending_Quantity,
				"0",
				"A",indentReqSiteId,reqReceiveFrom,IndentCreationDetailsId,
				centralTableData.getReqQuantity(),"0"
		});
		
		if(pending_Quantity==0 || dept_Req_Quantity==0){
			String sql="UPDATE SUM_PURCHASE_DEPT_INDENT_PROSS  set STATUS = 'I',TYPE_OF_PURCHASE='SETTLED IN CENTRAL STORE' "
			+ " where INDENT_CREATION_DETAILS_ID= ? ";
		
			int value = jdbcTemplate.update(sql, new Object[] {IndentCreationDetailsId});
			
		}
		
		
		return result;
	}


	@Override
	public IndentCreationDto getCentralIndentProcessData(int indentCreationDetailsId) {
		IndentCreationDto icd = null;;
		List<Map<String, Object>> dbIndentDts = null;
		int strSerialNumber = 0;
		String query = "SELECT CENTRAL_REQ_QUANTITY, ALLOCATED_QUANTITY, PENDING_QUANTIY,INTIATED_QUANTITY FROM SUMADHURA_CNTL_INDENT_PROCESS "+
		"WHERE INDENT_CREATION_DETAILS_ID = ? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentCreationDetailsId});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationDto indentCreationDto = new IndentCreationDto();

			indentCreationDto.setReqQuantity(prods.get("CENTRAL_REQ_QUANTITY")==null ? "" :   prods.get("CENTRAL_REQ_QUANTITY").toString());
			indentCreationDto.setAllocatedQuantity(prods.get("ALLOCATED_QUANTITY")==null ? "" :   prods.get("ALLOCATED_QUANTITY").toString());
			indentCreationDto.setPendingQuantity(prods.get("PENDING_QUANTIY")==null ? "" :   prods.get("PENDING_QUANTIY").toString());
			indentCreationDto.setIntiatedQuantity(prods.get("INTIATED_QUANTITY")==null ? "" :   prods.get("INTIATED_QUANTITY").toString());
			return indentCreationDto;
		}
		return icd;
	}

	@Override
	public int requestToOtherSite(IndentCreationBean indentCreationBean, int centralIndentReqDetailsSeqNum, int senderSite) {
		String query = "INSERT INTO SUMADHURA_CNTL_INDENT_REQ_DTLS(CNTL_INDENT_REQ_DTLS_SEQ,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MEASUREMENT_ID,"
			+ "REQ_QUANTITY,SENDER_ISSUED_QUANTITY,REQ_SITE,SENDER_SITE,PROCESS_INTIATED_SITE,"
			+ "STATUS,SENDER_INTIATED_TRANS_MODE,SENDER_PENDING,RECEIVER_PENDING,CREATION_DATE,"
			+ "INDENT_PROCESS_ID,INDENT_NO) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, sysdate, ?, ? )";
		/*	int result = jdbcTemplate.update(query, new Object[] {
				centralIndentReqDetailsSeqNum,
				indentCreationBean.getProductId1(),
				indentCreationBean.getSubProductId1(),
				indentCreationBean.getChildProductId1(),
				indentCreationBean.getUnitsOfMeasurementId1(),
				indentCreationBean.getRequiredQuantity1(),
				"0",indentCreationBean.getSiteId(),senderSite,"999",
				"A",
				senderSite==999 ? indentCreationBean.getRequiredQuantity1() : "0" ,
						senderSite==999 ? "0" : indentCreationBean.getRequiredQuantity1(),
								senderSite==999 ? indentCreationBean.getRequiredQuantity1() : "0" ,
										indentCreationBean.getIndentProcessId(),indentCreationBean.getIndentNumber()
		});*/
		int result = jdbcTemplate.update(query, new Object[] {
				centralIndentReqDetailsSeqNum,
				indentCreationBean.getProductId1(),
				indentCreationBean.getSubProductId1(),
				indentCreationBean.getChildProductId1(),
				indentCreationBean.getUnitsOfMeasurementId1(),
				indentCreationBean.getRequiredQuantity1(),
				"0",indentCreationBean.getSiteId(),senderSite,"999",
				"A",
				"0" ,
				indentCreationBean.getRequiredQuantity1(),
				"0" ,
				indentCreationBean.getIndentProcessId(),indentCreationBean.getIndentNumber()
		});
		return result;
	}
	@Override
	public int updateRequestToOtherSite(IndentCreationBean indentCreationBean, int centralIndentReqDetailsSeqNum,int senderSite) {
		String query = "UPDATE SUMADHURA_CNTL_INDENT_REQ_DTLS set "
			+ "REQ_QUANTITY = REQ_QUANTITY + ? ,SENDER_INTIATED_TRANS_MODE = SENDER_INTIATED_TRANS_MODE + ? ,"
			+ "SENDER_PENDING = SENDER_PENDING + ? ,RECEIVER_PENDING = RECEIVER_PENDING + ? ,STATUS='A' "
			+ "where SENDER_SITE = ? AND INDENT_PROCESS_ID = ?";
		/*	int result = jdbcTemplate.update(query, new Object[] {
				indentCreationBean.getRequiredQuantity1(),
				senderSite==999 ? indentCreationBean.getRequiredQuantity1() : "0" ,
						senderSite==999 ? "0" : indentCreationBean.getRequiredQuantity1(),
								senderSite==999 ? indentCreationBean.getRequiredQuantity1() : "0" ,
										String.valueOf(senderSite),indentCreationBean.getIndentProcessId()
		});
		 */
		int result = jdbcTemplate.update(query, new Object[] {
				indentCreationBean.getRequiredQuantity1(),
				"0" ,
				indentCreationBean.getRequiredQuantity1(),
				"0" ,
				String.valueOf(senderSite),indentCreationBean.getIndentProcessId()
		});
		return result;
	}


	@Override
	public int updateInitiatedQuantityInCentralTable(String aQuantity,int indentProcessId) {
		String query = "UPDATE SUMADHURA_CNTL_INDENT_PROCESS set INTIATED_QUANTITY = INTIATED_QUANTITY+"+aQuantity+" where "
		+ "INDENT_PROCESS_ID = ? ";
		int result = jdbcTemplate.update(query, new Object[] {
				indentProcessId
		});
		return result;
	}

	@Override
	public IndentCreationBean getCreateAndRequiredDates(int indentNumber) {
		List<Map<String, Object>> dbIndentDts = null;
		IndentCreationBean indentCreationBean = new IndentCreationBean();
		String strCreateDate = null;
		String strRequiredDate = null;
		String query = "SELECT CREATE_DATE,REQUIRED_DATE FROM SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = ?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {
			strCreateDate = prods.get("CREATE_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("CREATE_DATE").toString();
			strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
		}
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
		return indentCreationBean;

	}


	@Override
	public int getIndentProcessIdCount(int indentProcessId,int senderSite) {
		int count = jdbcTemplate.queryForInt("SELECT COUNT(1) FROM SUMADHURA_CNTL_INDENT_REQ_DTLS where INDENT_PROCESS_ID = "+indentProcessId+" AND SENDER_SITE = "+senderSite);
		return count;
	}






	@Override
	public List<ProductDetails> getPurchaseIndentDtlsLists(String indentCreationDetailsIdForenquiry,String strVendorId) {
		List<ProductDetails> list = new ArrayList<ProductDetails>();
		List<Map<String, Object>> dbIndentDts = null;
		int strSerialNumber = 0;
		/*String query = "SELECT P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,"+
				"SPDIP.PRODUCT_ID,SPDIP.SUB_PRODUCT_ID,SPDIP.CHILD_PRODUCT_ID,SPDIP.MEASUREMENT_ID,SPDIP.PURCHASE_DEPT_REQ_QUANTITY,"+
				"(SPDIP.PENDING_QUANTIY-SPDIP.PO_INTIATED_QUANTITY) as PO_QUANTITY,SPDIP.PENDING_QUANTIY,SPDIP.INDENT_CREATION_DETAILS_ID,SPDIP.PO_INTIATED_QUANTITY ," +
				"SPDIP.PURCHASE_DEPT_INDENT_PROSS_SEQ FROM SUMADHURA_INDENT_CREATION SIC, SUMADHURA_INDENT_CREATION_DTLS SICD, SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP, PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST "+
				"WHERE SPDIP.PRODUCT_ID=P.PRODUCT_ID AND SPDIP.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SPDIP.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID "+
				"AND SPDIP.MEASUREMENT_ID=MST.MEASUREMENT_ID AND SPDIP.INDENT_CREATION_DETAILS_ID= SICD.INDENT_CREATION_DETAILS_ID  "+
				"AND SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID AND SIC.INDENT_CREATION_ID = ? ";
		 */	


		String query = "SELECT P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,SEFD.PURCHASE_DEPT_CHILD_PROD_DISC as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,"+
		"SPDIP.PRODUCT_ID,SPDIP.SUB_PRODUCT_ID,SPDIP.CHILD_PRODUCT_ID,SPDIP.MEASUREMENT_ID,SPDIP.PURCHASE_DEPT_REQ_QUANTITY,"+
		"(SEFD.INDENT_QTY) as PO_QUANTITY,SPDIP.PENDING_QUANTIY,SPDIP.INDENT_CREATION_DETAILS_ID,SPDIP.PO_INTIATED_QUANTITY ," +
		"SPDIP.PURCHASE_DEPT_INDENT_PROSS_SEQ,SICD.INDENT_CREATION_DETAILS_ID FROM SUMADHURA_INDENT_CREATION SIC, SUMADHURA_INDENT_CREATION_DTLS SICD, SUM_PURCHASE_DEPT_INDENT_PROSS SPDIP, PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST "
		+ ",SUMADHURA_ENQUIRY_FORM_DETAILS SEFD "+
		"WHERE SEFD.INDENT_CREATION_DETAILS_ID = SICD.INDENT_CREATION_DETAILS_ID AND SPDIP.PRODUCT_ID=P.PRODUCT_ID AND SPDIP.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SPDIP.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID "+
		"AND SPDIP.MEASUREMENT_ID=MST.MEASUREMENT_ID AND SPDIP.INDENT_CREATION_DETAILS_ID= SICD.INDENT_CREATION_DETAILS_ID  "+
		"AND SICD.INDENT_CREATION_ID = SIC.INDENT_CREATION_ID AND SEFD.VENDOR_ID = '"+strVendorId+"' AND SICD.INDENT_CREATION_DETAILS_ID in ("+indentCreationDetailsIdForenquiry+") order by SICD.INDENT_CREATION_DETAILS_ID";


		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {});
		for(Map<String, Object> prods : dbIndentDts) {
			ProductDetails productDetails = new ProductDetails();

			productDetails.setProductId(prods.get("PRODUCT_ID")==null ? "" :   prods.get("PRODUCT_ID").toString());
			productDetails.setSub_ProductId(prods.get("SUB_PRODUCT_ID")==null ? "" :   prods.get("SUB_PRODUCT_ID").toString());
			String childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
			productDetails.setChild_ProductId(childProductId);
			productDetails.setMeasurementId(prods.get("MEASUREMENT_ID")==null ? "" :   prods.get("MEASUREMENT_ID").toString());
			productDetails.setProductName(prods.get("PRODUCT_NAME")==null ? "" :   prods.get("PRODUCT_NAME").toString());
			productDetails.setSub_ProductName(prods.get("SUB_PRODUCT_NAME")==null ? "" :   prods.get("SUB_PRODUCT_NAME").toString());
			productDetails.setChild_ProductName(prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString());
			productDetails.setMeasurementName(prods.get("MEASUREMENT_NAME")==null ? "" :   prods.get("MEASUREMENT_NAME").toString());
			productDetails.setRequestQantity(prods.get("PURCHASE_DEPT_REQ_QUANTITY")==null ? "" :   prods.get("PURCHASE_DEPT_REQ_QUANTITY").toString());
			//productDetails.setRequiredQuantity1(prods.get("PO_QUANTITY")==null ? "" :   prods.get("PO_QUANTITY").toString());
			//productDetails.setIndentCreationDetailsId(Integer.parseInt(prods.get("INDENT_CREATION_DETAILS_ID")==null ? "" :   prods.get("INDENT_CREATION_DETAILS_ID").toString()));
			productDetails.setPendingQuantity(prods.get("PO_QUANTITY")==null ? "" :   prods.get("PO_QUANTITY").toString());
			productDetails.setIndentCreationDetailsId(prods.get("INDENT_CREATION_DETAILS_ID")==null ? "" :   prods.get("INDENT_CREATION_DETAILS_ID").toString());
			//productDetails.setPurchaseDepartmentIndentProcessSeqId(Integer.parseInt(prods.get("PURCHASE_DEPT_INDENT_PROSS_SEQ")==null ? "" :   prods.get("PURCHASE_DEPT_INDENT_PROSS_SEQ").toString()));
			//productDetails.setPoIntiatedQuantity(prods.get("PO_INTIATED_QUANTITY")==null ? "" :   prods.get("PO_INTIATED_QUANTITY").toString());
			//productDetails.setIndentRequestQuantity(prods.get("INDENT_REQ_QUANTITY")==null ? "" :   prods.get("INDENT_REQ_QUANTITY").toString());


			strSerialNumber++;
			productDetails.setStrSerialNumber(String.valueOf(strSerialNumber));
			list.add(productDetails);
		}	
		return list;
	}






	@Override
	public String getAddressOfSite(int site_id) {
		String result="";
		String query = "SELECT ADDRESS FROM VENDOR_DETAILS where VENDOR_ID = '"+site_id+"'";



		List<String> result1 = jdbcTemplate.queryForList(query,String.class);
		result=result1.get(0);

		return result;
	}






	@Override
	public String getSiteNameWhereIndentCreated(int indentNumber) {
		String result="";
		String query = "SELECT S.SITE_NAME FROM SUMADHURA_INDENT_CREATION SID,SITE S where SID.INDENT_CREATION_ID = '"+indentNumber+"' and SID.SITE_ID = S.SITE_ID ";



		List<String> result1 = jdbcTemplate.queryForList(query,String.class);
		result=result1.get(0);

		return result;
	}







}
