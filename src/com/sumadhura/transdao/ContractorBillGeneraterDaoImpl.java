package com.sumadhura.transdao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.ContractorQSBillBean;
import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.util.UIProperties;



@Repository("wo_bill_generator_repo")
public class ContractorBillGeneraterDaoImpl implements ContrctorBillGeneraterDao {

	static Logger log = Logger.getLogger(ContractorBillGeneraterDaoImpl.class);
	
	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	/**
	 * updating the bill remarks
	 */
	@Override
	public int updateRemarks(String tempBillNo, String billRemarks) {
		String query = "update QS_CONT_TMP_BILL_APPR_REJ_DTLS set REMARKS = ?   where TEMP_BILL_ID = '" + tempBillNo + "'";
		int result = jdbcTemplate.update(query, billRemarks);
		return result;
	}

	/**
	 * @throws Exception 
	 * @description this method is inserting bill details like work order number,amount,total deduciton amount
	 */
	@Override
	public List<Object> generateAdvanceRAPaymentBill(ContractorQSBillBean bean) throws Exception {
		String contractorId = bean.getContractorId();
		List<Object> response = new ArrayList<Object>();
		StringBuffer query = new StringBuffer("");
		String workOrderIssueId="";
		Integer billTempSeqNum = 0;
		Integer advancePaymentNo = 0;
		int result = 0;
			billTempSeqNum = jdbcTemplate.queryForObject("SELECT QS_BILL_TEMP_SEQ.NEXTVAL FROM DUAL", Integer.class);
		try {
			 workOrderIssueId=jdbcTemplate.queryForObject("SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER=? AND SITE_ID=?", new Object[]{ bean.getWorkOrderNo().split("\\$")[0],bean.getSiteId()}, String.class);
			 //bean.setWorkOrderIssueId(workOrderIssueId);
			 if(workOrderIssueId==null){
				 throw new Exception("missing work order issue id");
			 }
			 if(bean.getOldBillNo()==null||bean.getOldBillNo().length()==0){
				 throw new Exception("missing bill number");
			 }
		} catch (Exception e) {
			throw new Exception("missing work order issue id");
		}
		
	//	String deductionRefNo = "DedRef.NO/" + billTempSeqNum;
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		Date date=null;
		try {
			String[] str=timeStamp.split(" ");
			String	raBillDate=bean.getBillDate();
			raBillDate=raBillDate+" "+str[1].trim();
			bean.setBillDate(raBillDate);
			date=dateFormat.parse(raBillDate);
		} catch (Exception e) {
			log.info("Error Msg "+e.getMessage());
		}
	
		// for advance payment
		if (bean.getPaymentType().equals("ADV")) {
			 query = new StringBuffer("INSERT INTO QS_CONTRACTOR_BILL_TEMP(PAYMENT_TYPE_OF_WORK,TEMP_BILL_ID,PERMANANT_BILL_NUMBER,CONTRACTOR_ID,SITE_ID,")
					.append(" PAYBLE_AMOUNT, STATUS,CREATED_BY,PENDING_EMP_ID,")
					.append(" PENDING_DEPT_ID,PAYMENT_REQ_DATE,ENTRY_DATE,PAYMENT_TYPE,CERTIFIED_AMOUNT,DEDUCTION_AMOUNT,")
					.append(" QS_WORKORDER_ISSUE_ID,BILL_INVOICE_NUMBER,TYPE_OF_WORK) ")
					.append(" values(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?)");

			Object[] queryParams = {bean.getPaymentTYpeOfWork1(), billTempSeqNum,bean.getOldBillNo(), contractorId, bean.getSiteId(),
					bean.getPaybleAmt(), "A", bean.getFromEmpId(), bean.getApproverEmpId(), "0",
					new java.sql.Date(date.getTime()), bean.getPaymentType(), bean.getPaybleAmt(), "0",
					workOrderIssueId,bean.getBillInvoiceNumber(),bean.getTypeOfWork()};
		 
			result = jdbcTemplate.update(query.toString(), queryParams);
			log.info("Query Parameters : "+Arrays.toString(queryParams));
			response.add(billTempSeqNum);

		} else if (bean.getPaymentType().equals("RA")) {
			// for Regular Bill
			 query =new StringBuffer("INSERT INTO QS_CONTRACTOR_BILL_TEMP(PAYMENT_TYPE_OF_WORK,TEMP_BILL_ID,PERMANANT_BILL_NUMBER,CONTRACTOR_ID,SITE_ID,")
					.append(" PAYBLE_AMOUNT, STATUS,CREATED_BY,PENDING_EMP_ID,")
					.append(" PENDING_DEPT_ID,PAYMENT_REQ_DATE,ENTRY_DATE,PAYMENT_TYPE,CERTIFIED_AMOUNT,DEDUCTION_AMOUNT,")
					.append(" QS_WORKORDER_ISSUE_ID,BILL_INVOICE_NUMBER,TYPE_OF_WORK)")
					.append(" values(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?)");

			 
			Object[] queryParams = {bean.getPaymentTYpeOfWork1(), billTempSeqNum,bean.getOldBillNo(),contractorId, bean.getSiteId(), 
					bean.getPaybleAmt(), "A", bean.getFromEmpId(), bean.getApproverEmpId(), "0",
					new java.sql.Date(date.getTime()), bean.getPaymentType(), bean.getTotalCurrentCerti(),
					bean.getTotalCurrntDeducAmt(), workOrderIssueId,bean.getBillInvoiceNumber(),bean.getTypeOfWork()};
			log.info("Query Parameters : "+Arrays.toString(queryParams));
			result = jdbcTemplate.update(query.toString(), queryParams);
			response.add(billTempSeqNum);

			// QS_TEMP_BILL_DEDUCTION_DTLS_SEQ query for advance deduction
			// insertRaBillDeductionDetails
			 result=0;
			if (bean.getRaDeductionAmt().length() != 0 ) {
				try {
					String advDeductionIdSeqNum = jdbcTemplate.queryForObject("SELECT QS_TEMP_BILL_DEDUCTION_DTLS_SEQ.NEXTVAL FROM DUAL", String.class);
					query =new StringBuffer( "INSERT INTO QS_TEMP_BILL_DEDUCTION_DTLS (DEDUCTION_ID,TEMP_BILL_ID,DEDUCTION_DESC,DEDUCTION_AMOUNT,TYPE_OF_DEDUCTION) ")
							.append(" VALUES(?,?,?,?,?)");
					Object[] params = { advDeductionIdSeqNum,billTempSeqNum, "deduction desc", bean.getRaDeductionAmt(), "ADV" };
					result = jdbcTemplate.update(query.toString(), params);
					bean.setAdvanceDeductionId(advDeductionIdSeqNum);
				} catch (Exception e) {
					log.info(e.getMessage());
				}
			}
			query = new StringBuffer("INSERT INTO QS_TEMP_BILL_DEDUCTION_DTLS (DEDUCTION_ID,TEMP_BILL_ID,DEDUCTION_DESC,DEDUCTION_AMOUNT,TYPE_OF_DEDUCTION) ")
					.append(" VALUES(QS_TEMP_BILL_DEDUCTION_DTLS_SEQ.NEXTVAL,?,?,?,?)");
			
			// Query for security Deposit
			if (bean.getSecDepositCurrentCerti().length() != 0) {
				Object[] params1 = { billTempSeqNum, "deduction desc", bean.getSecDepositCurrentCerti(), "SEC" };
				result= jdbcTemplate.update(query.toString(), params1);
			}
			if (bean.getPettyExpensesCurrentCerti().length() != 0) {// &&!bean.getPettyExpensesCurrentCerti().equals("0")
				Object[] params1 = { billTempSeqNum, "Petty deduction desc", bean.getPettyExpensesCurrentCerti(), "PETTY" };
				result = jdbcTemplate.update(query.toString(), params1);
			}
			if (bean.getOtherAmout().length() != 0) {// &&!bean.getOtherAmout().equals("0")
				Object[] params1 = { billTempSeqNum, "Other Amount deduction desc", bean.getOtherAmout(), "OTHER" };
				result = jdbcTemplate.update(query.toString(), params1);
			}
			if(bean.getRecovery_amount().length()!=0){
				Object[] params1 = { billTempSeqNum, "Recovery Deduction Amount", bean.getRecovery_amount(), "RECOVERY" };
				result = jdbcTemplate.update(query.toString(), params1);
			}
		}
		return response;
	}
	/**
	 * checking is any bill is pending for the approval or not if ending return true
	 * so then we can't initiate another bill
	 */
	@Override
	public boolean isAnyRaAndAdvBillPending(ContractorQSBillBean billBean) {
		String workOrderNo = billBean.getWorkOrderNo().split("\\$")[0].trim();
		
		/*String[] strWONoPart=workOrderNo.split("/");
		String strWorkOrderNumber=	strWONoPart[0]+"/"+strWONoPart[1]+"/"+strWONoPart[2]+"/"+strWONoPart[3]+"%";
	*/
		String query="select count(*) from QS_CONTRACTOR_BILL_TEMP where STATUS='A' and  QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where   WORK_ORDER_NUMBER = '"+workOrderNo+"'  and SITE_ID='"+billBean.getSiteId()+"') ";
		int count=jdbcTemplate.queryForObject(query, Integer.class);//PAYMENT_TYPE='"+billBean.getPaymentType()+"' and 
		if(count!=0){
			return true;
		}
		
		/* query="select count(*) from QS_CONTRACTOR_BILL where PAYMENT_STATUS='PENDING' and  QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where   WORK_ORDER_NUMBER = '"+workOrderNo+"'  and SITE_ID='"+billBean.getSiteId()+"') ";
		 count=jdbcTemplate.queryForObject(query, Integer.class);//PAYMENT_TYPE='"+billBean.getPaymentType()+"' and 
		if(count!=0){return true;}*/
		return false;
	}
	
	/**
	 * this method will return all the created previous bill's and current bill's details by using work order number 
	 */
	@Override
	public List<Map<String, Object>> getAdvPaymentCertificateDetail(ContractorQSBillBean bean) {
		String workOrderNo = bean.getWorkOrderNo().split("\\$")[0].trim();
		Object[] queryParams = { bean.getContractorId(), workOrderNo };
		String query = "";
		if(bean.getIsApprovePage().equals("false")){
			query="SELECT QCBT.BILL_ID ,QCBT.CONTRACTOR_ID ,QCBT.SITE_ID ,QCBT.PAYBLE_AMOUNT ,	QCBT.STATUS ,QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID ,to_char(QCBT.PAYMENT_REQ_DATE,'dd-MM-yyyy') as PAYMENT_REQ_DATE,	to_char(ENTRY_DATE,'dd-MM-yy hh:MM:ss') as ENTRY_DATE  ,QCBT.PAYMENT_TYPE ,QCBT.CERTIFIED_AMOUNT ,QCBT.DEDUCTION_AMOUNT DA ,QCBT.TEMP_BILL_ID ,QCBT.QS_WORKORDER_ISSUE_ID ,QCBT.PAYMENT_STATUS , QCBT.CREATED_BY ,QCBT.DEDUCTION_REF_ID ,QCBT.REMARKS ,QSDD.DEDUCTION_ID ,QSDD.CONTRACTOR_BILL_ID ,	QSDD.DEDUCTION_DESC, "
				+ " (SELECT LISTAGG(TYPE_OF_DEDUCTION, '@@')   WITHIN GROUP (ORDER BY TYPE_OF_DEDUCTION) FROM QS_BILL_DEDUCTION_DTLS  WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID  and QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID ) as  TYPE_OF_DEDUCTION,"
				+ " (SELECT LISTAGG(nvl(DEDUCTION_AMOUNT,0), '@@')   WITHIN GROUP (ORDER BY TYPE_OF_DEDUCTION) FROM QS_BILL_DEDUCTION_DTLS  WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID  and  QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID) as DEDUCTION_AMOUNT "
				+ "FROM QS_CONTRACTOR_BILL QCBT left join QS_BILL_DEDUCTION_DTLS QSDD on QSDD.CONTRACTOR_BILL_ID=QCBT.BILL_ID  and QSDD.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and QSDD.TYPE_OF_DEDUCTION='ADV' "
				+ "	WHERE CONTRACTOR_ID=?  AND PAYMENT_TYPE in('ADV','RA') AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = ? and SITE_ID='"+bean.getSiteId()+"')   order by  QCBT.TEMP_BILL_ID"; 
		}else{
			query="SELECT QCBT.BILL_ID ,QCBT.CONTRACTOR_ID ,QCBT.SITE_ID ,QCBT.PAYBLE_AMOUNT ,	QCBT.STATUS ,QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID ,to_char(QCBT.PAYMENT_REQ_DATE,'dd-MM-yyyy') as PAYMENT_REQ_DATE,	to_char(ENTRY_DATE,'dd-MM-yy hh:MM:ss') as ENTRY_DATE  ,QCBT.PAYMENT_TYPE ,QCBT.CERTIFIED_AMOUNT ,QCBT.DEDUCTION_AMOUNT DA ,QCBT.TEMP_BILL_ID ,QCBT.QS_WORKORDER_ISSUE_ID ,QCBT.PAYMENT_STATUS , "
				+ " QCBT.CREATED_BY ,QCBT.DEDUCTION_REF_ID ,QCBT.REMARKS ,QSDD.DEDUCTION_ID ,QSDD.CONTRACTOR_BILL_ID ,	QSDD.DEDUCTION_DESC,(SELECT LISTAGG(TYPE_OF_DEDUCTION, '@@')   WITHIN GROUP (ORDER BY TYPE_OF_DEDUCTION) FROM QS_BILL_DEDUCTION_DTLS  WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID   and QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID) as  TYPE_OF_DEDUCTION, "
				+ " (SELECT LISTAGG(nvl(DEDUCTION_AMOUNT,0), '@@')   WITHIN GROUP (ORDER BY TYPE_OF_DEDUCTION) FROM QS_BILL_DEDUCTION_DTLS  WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID  and QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID) as DEDUCTION_AMOUNT "
				+ "FROM QS_CONTRACTOR_BILL QCBT left join QS_BILL_DEDUCTION_DTLS QSDD on QSDD.CONTRACTOR_BILL_ID=QCBT.BILL_ID  and QSDD.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID  and QSDD.TYPE_OF_DEDUCTION='ADV' "
				+ "WHERE CONTRACTOR_ID=? and QCBT.TEMP_BILL_ID<='"+bean.getTempBillNo().trim()+"'  AND PAYMENT_TYPE in('ADV','RA') AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = ? and SITE_ID='"+bean.getSiteId()+"')   order by  QCBT.TEMP_BILL_ID";
		}
		List<Map<String, Object>> advPaymentDetails = jdbcTemplate.queryForList(query, queryParams);
		return advPaymentDetails;
	}

	/**
	 * @description this method used for inserting RA/ADV abstract payment area details
	 */
	@Override
	public int insertPaymentAreaDetails(final String[] workAreaId, final String[] qty, final int tempBllNo,
			final String paybleAmt, String operType, final String[] actualQty, final String[] initiatedArea, final String[] wOQuantityRateArray, final String[] wo_work_issue_area_dtls_id) {
		// QS_TEMP_INV_AGN_AREA_PMT_DTL_SEQ Sequence name
		if (operType.equals("approve")) {
			String query = "delete from QS_TEMP_INV_AGN_AREA_PMT_DTL where TEMP_BILL_ID=?";
			int i = jdbcTemplate.update(query, tempBllNo);
			log.info(i);
		}

		final int len = workAreaId.length;
		StringBuffer query =new StringBuffer( "INSERT INTO QS_TEMP_INV_AGN_AREA_PMT_DTL")
				.append("(INV_AGAINST_PMT_DTLS_ID,WO_WORK_AREA_ID,RATE,AMOUNT,TEMP_BILL_ID,ALLOCATED_QTY,ACTUAL_AREA,AVAILABLE_AREA,INITIATED_AREA,WO_WORK_ISSUE_AREA_DTLS_ID)")
				.append("VALUES(QS_TEMP_INV_AGN_AREA_PMT_DTL_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?)");

		int result[] = jdbcTemplate.batchUpdate(query.toString(), new BatchPreparedStatementSetter() {
			
			@Override
			public int getBatchSize() {
				return len;
			}
			
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				double availbleQty = Double.valueOf(actualQty[i]) - Double.valueOf(qty[i]);
				double amount=Double.valueOf(qty[i])*Double.valueOf(wOQuantityRateArray[i]);
				ps.setString(1, workAreaId[i]);
				ps.setString(2, wOQuantityRateArray[i]); // rate
				ps.setDouble(3,amount); //total Amount
				ps.setInt(4, tempBllNo);
				ps.setString(5, qty[i]);
				ps.setString(6, actualQty[i]);
				ps.setString(7, String.valueOf(availbleQty));
				ps.setString(8, initiatedArea[i]);
				ps.setString(9, wo_work_issue_area_dtls_id[i]);
			}
		});

		log.info(result.length);
		return result.length;
	}

	/**
	 * @description this method is loading all the completed bill's using site_id
	 */
	@Override
	public List<ContractorQSBillBean> getCompletdWorkOrderBills( String siteId, ContractorQSBillBean billBean) {
		List<ContractorQSBillBean> billPaymentlist = new ArrayList<ContractorQSBillBean>();
		List<String> queryParams=new ArrayList<String>();
		log.info(billBean.getFromDate()+" \t"+billBean.getToDate()+" \t"+billBean.getWorkOrderNo()+" \t"+billBean.getContractorId()+" \t");
		List<Map<String, Object>> list = null;
		StringBuffer query = new StringBuffer("select QCBT.QS_WORKORDER_ISSUE_ID,(select concat(WORK_ORDER_NUMBER,'&&'||nvl(WORK_ORDER_NAME,'-'))  from QS_WORKORDER_ISSUE  where QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID) AS WORK_ORDER_NUMBER,QCBT.BILL_ID,QCBT.CONTRACTOR_ID,CREATED_BY, TEMP_BILL_ID ,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME,(SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QCBT.PENDING_EMP_ID) as PENDING_EMP_NAME ")
				.append(",QCBT.SITE_ID ,PAYBLE_AMOUNT ,QCBT.STATUS ,PENDING_EMP_ID ,PENDING_DEPT_ID ,to_char(PAYMENT_REQ_DATE,'dd-MM-yyyy') as PAYMENT_REQ_DATE ,to_char(ENTRY_DATE,'dd-MM-yyyy hh24:mi:ss') as ENTRY_DATE  ,PAYMENT_TYPE ,QS_WORKORDER_ISSUE_ID ")
				.append(" FROM  QS_CONTRACTOR_BILL QCBT,SUMADHURA_CONTRACTOR SC  WHERE SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID AND SITE_ID=? ");
		  queryParams.add(billBean.getSiteId());
		if (StringUtils.isNotBlank(billBean.getFromDate()) && StringUtils.isNotBlank(billBean.getToDate())) {
		    query.append(" AND TRUNC(QCBT.ENTRY_DATE)  BETWEEN TO_DATE(?,'dd-MM-yy') AND TO_DATE(?,'dd-MM-yy')");
		    queryParams.add(billBean.getFromDate());
		    queryParams.add(billBean.getToDate());
		} else if (StringUtils.isNotBlank(billBean.getFromDate())) {
			queryParams.add(billBean.getFromDate());
			query.append(" AND  TRUNC(QCBT.ENTRY_DATE) =TO_DATE(?, 'dd-MM-yy')");
		} else if (StringUtils.isNotBlank(billBean.getToDate())) {
			queryParams.add(billBean.getToDate());
			query.append("AND  TRUNC(QCBT.ENTRY_DATE) <=TO_DATE(?, 'dd-MM-yy')");
		} else if (StringUtils.isNotBlank(billBean.getWorkOrderNo())) {
			queryParams.add(billBean.getWorkOrderNo());
			queryParams.add(billBean.getSiteId());
			query.append(" AND QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE  WORK_ORDER_NUMBER =? AND SITE_ID=?)");
		}else if (StringUtils.isNotBlank(billBean.getContractorId())) {
			queryParams.add(billBean.getContractorId());
			query.append(" AND  QCBT.CONTRACTOR_ID=? ");
		}
		
		Object[] obj=queryParams.toArray();
		list = jdbcTemplate.queryForList(query.toString(), obj);
		for (Map<String, Object> map : list) {
			ContractorQSBillBean bean = new ContractorQSBillBean();
			String workOrderNumerAndNAme = map.get("WORK_ORDER_NUMBER") == null ? "": map.get("WORK_ORDER_NUMBER").toString();
			if (workOrderNumerAndNAme.contains("&&")) {
				String[] str = workOrderNumerAndNAme.split("&&");
				bean.setWorkOrderNo(str[0]);
				bean.setWorkOrderName(str[1]);
			}
			
			bean.setWorkOrderIssueId(map.get("QS_WORKORDER_ISSUE_ID") == null ? "" : map.get("QS_WORKORDER_ISSUE_ID").toString());
			bean.setSiteId(map.get("SITE_ID") == null ? "" : map.get("SITE_ID").toString());
			bean.setSiteName(map.get("SITE_NAME") == null ? "" : map.get("SITE_NAME").toString());
			bean.setFromEmpId(map.get("CREATED_BY") == null ? "" : map.get("CREATED_BY").toString());
			bean.setContractorName(map.get("CONTRACTOR_NAME") == null ? "" : map.get("CONTRACTOR_NAME").toString());
			bean.setPaybleAmt(map.get("PAYBLE_AMOUNT") == null ? "" : map.get("PAYBLE_AMOUNT").toString());
			bean.setEntryDate(map.get("ENTRY_DATE") == null ? "" : map.get("ENTRY_DATE").toString());
			bean.setPaymentReqDate(map.get("PAYMENT_REQ_DATE") == null ? "" : map.get("PAYMENT_REQ_DATE").toString());
			bean.setBillNo(map.get("BILL_ID") == null ? "" : map.get("BILL_ID").toString());
			bean.setContractorId(map.get("CONTRACTOR_ID") == null ? "" : map.get("CONTRACTOR_ID").toString());
			bean.setPaymentType(map.get("PAYMENT_TYPE") == null ? "" : map.get("PAYMENT_TYPE").toString());
			String empName = map.get("PENDING_EMP_NAME") == null ? bean.getPaymentType() + " Completed": map.get("PENDING_EMP_NAME").toString();
	
			bean.setPendingEmpId(empName);
			log.info(bean.getPendingEmpId());
			billPaymentlist.add(bean);
		}

		log.info(billPaymentlist);
		return billPaymentlist;
	}
	/**
	 * this method is used to load all the bill's using site_id
	 */
	@Override
	public List<ContractorQSBillBean> getSitewiseCompletdWorkOrderBills(String string, String siteId, String billType) {
		List<ContractorQSBillBean> billPaymentlist = new ArrayList<ContractorQSBillBean>();

		List<Map<String, Object>> list = null;
		StringBuffer query =new StringBuffer("select S.SITE_NAME,QCBT.QS_WORKORDER_ISSUE_ID,(select concat(WORK_ORDER_NUMBER,'&&'||nvl(WORK_ORDER_NAME,'-'))  from QS_WORKORDER_ISSUE  where QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID) AS WORK_ORDER_NUMBER,QCBT.BILL_ID,QCBT.CONTRACTOR_ID,QCBT.CREATED_BY, TEMP_BILL_ID ,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME,(SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QCBT.PENDING_EMP_ID) as PENDING_EMP_NAME,QCBT.SITE_ID ,PAYBLE_AMOUNT ,QCBT.STATUS ,PENDING_EMP_ID ,PENDING_DEPT_ID ,")
				.append(" to_char(PAYMENT_REQ_DATE,'dd-MON-yy') as PAYMENT_REQ_DATE ,to_char(ENTRY_DATE,'dd-MM-yyyy hh24:mi:ss') as ENTRY_DATE  ,PAYMENT_TYPE ,QS_WORKORDER_ISSUE_ID  from  QS_CONTRACTOR_BILL QCBT,SUMADHURA_CONTRACTOR SC,SITE S ")
				.append(" where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID and S.SITE_ID=QCBT.SITE_ID");
		if(!(siteId!=null&&siteId.equals("ALL"))){
			query.append(" AND  QCBT.SITE_ID='"+siteId+"'");
		}
		list = jdbcTemplate.queryForList(query.toString());
		for (Map<String, Object> map : list) {
			ContractorQSBillBean bean = new ContractorQSBillBean();
			String workOrderNumerAndNAme = map.get("WORK_ORDER_NUMBER") == null ? "": map.get("WORK_ORDER_NUMBER").toString();
			if (workOrderNumerAndNAme.contains("&&")) {
				String[] str = workOrderNumerAndNAme.split("&&");
				bean.setWorkOrderNo(str[0]);
				bean.setWorkOrderName(str[1]);
			}
			
			bean.setWorkOrderIssueId(map.get("QS_WORKORDER_ISSUE_ID") == null ? "" : map.get("QS_WORKORDER_ISSUE_ID").toString());
			bean.setSiteId(map.get("SITE_ID") == null ? "" : map.get("SITE_ID").toString());
			bean.setSiteName(map.get("SITE_NAME") == null ? "" : map.get("SITE_NAME").toString());
			bean.setFromEmpId(map.get("CREATED_BY") == null ? "" : map.get("CREATED_BY").toString());
			bean.setContractorName(map.get("CONTRACTOR_NAME") == null ? "" : map.get("CONTRACTOR_NAME").toString());
			bean.setPaybleAmt(map.get("PAYBLE_AMOUNT") == null ? "" : map.get("PAYBLE_AMOUNT").toString());
			bean.setEntryDate(map.get("ENTRY_DATE") == null ? "" : map.get("ENTRY_DATE").toString());
			bean.setPaymentReqDate(map.get("PAYMENT_REQ_DATE") == null ? "" : map.get("PAYMENT_REQ_DATE").toString());
			bean.setBillNo(map.get("BILL_ID") == null ? "" : map.get("BILL_ID").toString());
			bean.setContractorId(map.get("CONTRACTOR_ID") == null ? "" : map.get("CONTRACTOR_ID").toString());
			bean.setPaymentType(map.get("PAYMENT_TYPE") == null ? "" : map.get("PAYMENT_TYPE").toString());
			String empName = map.get("PENDING_EMP_NAME") == null ? bean.getPaymentType() + " Completed": map.get("PENDING_EMP_NAME").toString();
	
			bean.setPendingEmpId(empName);
			log.info(bean.getPendingEmpId());
			billPaymentlist.add(bean);
		}
		return billPaymentlist;
	}
	
/*	@Override
	public List<ContractorQSBillBean> getBillsForUpdate(String siteId, String billType) {
		List<ContractorQSBillBean> billPaymentlist = new ArrayList<ContractorQSBillBean>();
		
		String query = "";
		log.info("billType " + billType);
		List<Map<String, Object>> list = null;
		query = "select QCBT.PERMANANT_BILL_NUMBER, QCBT.QS_WORKORDER_ISSUE_ID,(select concat(WORK_ORDER_NUMBER,'&&'||nvl(WORK_ORDER_NAME,'-'))  from QS_WORKORDER_ISSUE  where QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID) AS WORK_ORDER_NUMBER,QCBT.STATUS,QCBT.CONTRACTOR_ID,CREATED_BY, TEMP_BILL_ID ,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME,(SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QCBT.PENDING_EMP_ID) as PENDING_EMP_NAME,QCBT.SITE_ID ,PAYBLE_AMOUNT ,QCBT.STATUS ,PENDING_EMP_ID ,PENDING_DEPT_ID ,"
				+ " to_char(PAYMENT_REQ_DATE,'dd-MON-yy') as PAYMENT_REQ_DATE ,to_char(ENTRY_DATE,'dd-MON-yy hh:MM:ss') as ENTRY_DATE  ,PAYMENT_TYPE ,QS_WORKORDER_ISSUE_ID  from  QS_CONTRACTOR_BILL_TEMP QCBT,SUMADHURA_CONTRACTOR SC "
				+ " where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID and SITE_ID=? and PENDING_EMP_ID!='END' and QCBT.status='A'";
		list = jdbcTemplate.queryForList(query, siteId);
		
		billPaymentlist=extractBillsData(list);
		return billPaymentlist;
	}*/
	
/**
 * used same method for extracting map data 
 * @param list contains data which should extract into ContractorQSBillBean bean object
 * @return
 */
	public static List<ContractorQSBillBean> extractBillsData(List<Map<String, Object>> list){
	log.info("ContractorBillGeneraterDaoImpl.extractBillsData()");
		List<ContractorQSBillBean> billPaymentlist = new ArrayList<ContractorQSBillBean>();
		for (Map<String, Object> map : list) {
			String workOrderNumerAndNAme = map.get("WORK_ORDER_NUMBER") == null ? "": map.get("WORK_ORDER_NUMBER").toString();
			ContractorQSBillBean bean = new ContractorQSBillBean();
			if (workOrderNumerAndNAme.contains("&&")) {
				//workOrderNumerAndNAme is contains work order number and work order name so we are extracting to them in there respective setter method
				String[] str = workOrderNumerAndNAme.split("&&");
				bean.setWorkOrderNo(str[0]);
				bean.setWorkOrderName(str[1]);
			}
			String status=map.get("STATUS") == null ? "" : map.get("STATUS").toString();
			if(status.length()!=0){
			//checking status type and setting short cut word into full word
				if(status.equals("R")){
					status="Rejected";
				}else if(status.equals("I")){
					status="Completed";
				}else if(status.equals("A")){
					status="Active";
				}if(status.equals("T")){
					status="Temporary Rejected While Revise WO.";
				}
				bean.setStatus(status);	
			}
			
			bean.setPermanentBillNo(map.get("PERMANANT_BILL_NUMBER") == null ? "" : map.get("PERMANANT_BILL_NUMBER").toString());
			bean.setWorkOrderIssueId(map.get("QS_WORKORDER_ISSUE_ID") == null ? "" : map.get("QS_WORKORDER_ISSUE_ID").toString());
			bean.setSiteId(map.get("SITE_ID") == null ? "" : map.get("SITE_ID").toString());
			bean.setSiteName(map.get("SITE_NAME") == null ? "" : map.get("SITE_NAME").toString());
			bean.setFromEmpId(map.get("CREATED_BY") == null ? "" : map.get("CREATED_BY").toString());
			bean.setContractorName(map.get("CONTRACTOR_NAME") == null ? "" : map.get("CONTRACTOR_NAME").toString());
			bean.setPaybleAmt(map.get("PAYBLE_AMOUNT") == null ? "" : map.get("PAYBLE_AMOUNT").toString());
			bean.setEntryDate(map.get("ENTRY_DATE") == null ? "" : map.get("ENTRY_DATE").toString());
			bean.setPaymentReqDate(map.get("PAYMENT_REQ_DATE") == null ? "" : map.get("PAYMENT_REQ_DATE").toString());
			bean.setTempBillNo(map.get("TEMP_BILL_ID") == null ? "" : map.get("TEMP_BILL_ID").toString());
			bean.setContractorId(map.get("CONTRACTOR_ID") == null ? "" : map.get("CONTRACTOR_ID").toString());
			bean.setPaymentType(map.get("PAYMENT_TYPE") == null ? "" : map.get("PAYMENT_TYPE").toString());
			String empName = map.get("PENDING_EMP_NAME") == null ? bean.getPaymentType() + " Completed"	: map.get("PENDING_EMP_NAME").toString();
			bean.setPendingEmpId(empName);
			billPaymentlist.add(bean);
		}
		return billPaymentlist;
	}
	
	
	/**
	 * @description loading all the pending bill's for approval.
	 * this data will be loaded based on used_id.
	 * this data is loaded for using user_id and also without user_id. 
	 * here we are changing the query based on condition if user_id is "N/A" then load data using site_id
	 */
	@Override
	public List<ContractorQSBillBean> getPendingWorkOrderBils(String user_id, String siteId) {
		List<ContractorQSBillBean> billPaymentlist = new ArrayList<ContractorQSBillBean>();
		StringBuffer query = null;
		List<Map<String, Object>> list = null;
		// if user id is N/A then show status of the bills
		if (user_id.equals("showBillStatus")) {
			query =new StringBuffer("select  QCBT.PERMANANT_BILL_NUMBER,QCBT.QS_WORKORDER_ISSUE_ID,(select concat(WORK_ORDER_NUMBER,'&&'||nvl(WORK_ORDER_NAME,'-'))  from QS_WORKORDER_ISSUE  where QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID) AS WORK_ORDER_NUMBER,QCBT.STATUS,QCBT.CONTRACTOR_ID,CREATED_BY, TEMP_BILL_ID ,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME,(SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=QCBT.PENDING_EMP_ID) as PENDING_EMP_NAME,QCBT.SITE_ID  ,PAYBLE_AMOUNT ,QCBT.STATUS ,PENDING_EMP_ID ,PENDING_DEPT_ID ,")
					.append(" to_char(PAYMENT_REQ_DATE,'dd-MM-yyyy') as PAYMENT_REQ_DATE ,to_char(ENTRY_DATE,'dd-MM-yyyy hh24:mi:ss') as ENTRY_DATE  ,PAYMENT_TYPE ,QS_WORKORDER_ISSUE_ID  from  QS_CONTRACTOR_BILL_TEMP QCBT,SUMADHURA_CONTRACTOR SC ")
					.append(" where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID and SITE_ID=? and QCBT.STATUS in('R','A','T')");// and QCBT.PAYMENT_TYPE in('RA','ADV')and QCBT.STATUS in('R','A')
			list = jdbcTemplate.queryForList(query.toString(), siteId);
		} else {
			query = new StringBuffer("select  QCBT.PERMANANT_BILL_NUMBER,QCBT.QS_WORKORDER_ISSUE_ID,(select concat(WORK_ORDER_NUMBER,'&&'||nvl(WORK_ORDER_NAME,'-'))  from QS_WORKORDER_ISSUE  where QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID) AS WORK_ORDER_NUMBER, QCBT.STATUS,QCBT.CONTRACTOR_ID,QCBT.CREATED_BY, TEMP_BILL_ID ,CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,QCBT.SITE_ID ,si.SITE_NAME,PAYBLE_AMOUNT ,QCBT.STATUS ,PENDING_EMP_ID ,PENDING_DEPT_ID ,")
						.append(" to_char(PAYMENT_REQ_DATE,'dd-MM-yyyy') as PAYMENT_REQ_DATE ,to_char(ENTRY_DATE,'dd-MM-yyyy hh24:mi:ss') as ENTRY_DATE   ,PAYMENT_TYPE ,QS_WORKORDER_ISSUE_ID  from  QS_CONTRACTOR_BILL_TEMP QCBT,SUMADHURA_CONTRACTOR SC,SITE si ")
						.append(" where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID and si.SITE_ID=QCBT.SITE_ID and QCBT.status='A' and PENDING_EMP_ID=?  and QCBT.SITE_ID=?");
			list = jdbcTemplate.queryForList(query.toString(), user_id,siteId);
		}
		query=null;
		//extractBillsData is the common method for loading the data 
		billPaymentlist=extractBillsData(list);
		return billPaymentlist;
	}

	/**
	 * @description this method is used for inserting the approve reject details
	 * ("C"-Create,"R"-rejected,"A"-approve)
	 */
	@Override
	public int insertTempBillApprRejDetail(ContractorQSBillBean bean, String operType) {
		int QS_BILL_APPR_REJ_DTLS_SEQ = jdbcTemplate.queryForObject("SELECT QS_BILL_APPR_REJ_DTLS_SEQ.NEXTVAL FROM DUAL", Integer.class);
		StringBuffer query = new StringBuffer("INSERT INTO  QS_CONT_TMP_BILL_APPR_REJ_DTLS")
				.append("(QS_BILL_APPR_REJ_DTLS,TEMP_BILL_ID,EMP_ID,STATUS,REMARKS,SITE_ID,CREATED_DATE) ")
				.append(" VALUES(?,?,?,?,?,?,sysdate)");
		Object[] params = { QS_BILL_APPR_REJ_DTLS_SEQ, bean.getTempBillNo(), bean.getUserId(), operType,bean.getRemarks(), bean.getSiteId() };
		int result = jdbcTemplate.update(query.toString(), params);
		query=null;
		return result;
	}
	
	/**
	 * @description this method used to load the site address using the site_id
	 * we are loading the site address from the properties file
	 */
	@Override
	public List<String> loadSiteAddress(ContractorQSBillBean qsBillBean) {
		 List<String> siteAddr=new ArrayList<String>();
		String query="SELECT SITE_NAME,ADDRESS FROM SITE where SITE_ID = '"+qsBillBean.getSiteId()+"'  and STATUS = 'ACTIVE' ";
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query);
		for (Map<String, Object> map : list) {
			
		//	siteAddress.add(map.get("ADDRESS")==null?"":map.get("ADDRESS").toString());
			if(map.get("ADDRESS").toString().equals("HYD")){
				String siteAddress = UIProperties.validateParams.getProperty("BILLING_ADDRESSS_HYDERABAD") == null	? "" : UIProperties.validateParams.getProperty("BILLING_ADDRESSS_HYDERABAD").toString();
				siteAddr.add(siteAddress);
			}else if(map.get("ADDRESS").toString().equals("BANGLORE")){
				String siteAddress = UIProperties.validateParams.getProperty("BILLING_ADDRESSS_BENGALORE") == null	? "" : UIProperties.validateParams.getProperty("BILLING_ADDRESSS_BENGALORE").toString();
				siteAddr.add(siteAddress);
			}
			break;
		}
		return siteAddr;
	}
	
	/**
	 * @description this method will load the completed RA/ADV/NMR bill details 
	 * using bill number
	 */
	@Override
	public List<ContractorQSBillBean> getCompltedWorkOrderContractorBillDetailById(ContractorQSBillBean qsBillBean) {
		String billNo = qsBillBean.getBillNo();
		String workOrderNo=qsBillBean.getWorkOrderNo();
		List<Map<String, Object>> list = null;
		String billType=qsBillBean.getPaymentType();
		boolean flag = Boolean.valueOf(qsBillBean.getStatus());
		String query = "";
		
			query = "select (select CONCAT(WORK_ORDER_NUMBER,'@@'||TOTAL_WO_AMOUNT) from QS_WORKORDER_ISSUE where QS_WORKORDER_ISSUE_ID=QCBTBU.QS_WORKORDER_ISSUE_ID) as OLD_WORK_ORDER_NUMBER_N_AMOUNT, QCBT.WORK_BLOCK_NAMES "
					+ ",s.SITE_NAME,QCBT.BILL_INVOICE_NUMBER,QCBT.BILL_ID,QWI.TOTAL_WO_AMOUNT,QCBT.CERTIFIED_AMOUNT,QCBT.CONTRACTOR_ID,QWI.WORK_ORDER_NUMBER,QCBT.CREATED_BY, QCBT.TEMP_BILL_ID "
					+ ",CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,SC.ADDRESS,SC.GSTIN,SC.PAN_NUMBER,SC.MOBILE_NUMBER,SC.BANK_ACC_NUMBER,SC.BANK_NAME,SC.IFSC_CODE ,QCBT.SITE_ID , "
					+ "QCBT.PAYBLE_AMOUNT ,QCBT.STATUS ,QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID,to_char(QCBT.PAYMENT_REQ_DATE,'dd-MM-yyyy') as PAYMENT_REQ_DATE,QCBT.ENTRY_DATE ,QCBT.PAYMENT_TYPE ,QCBT.QS_WORKORDER_ISSUE_ID "
					+ " from    Site s,QS_CONTRACTOR_BILL QCBT LEFT JOIN QS_CONTRACTOR_BILL_BACK_UP  QCBTBU on  QCBT.CONT_SEQ_BILL_NO=QCBTBU.CONT_SEQ_BILL_NO,QS_WORKORDER_ISSUE QWI,SUMADHURA_CONTRACTOR SC "
					+ " where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID   and S.SITE_ID=QCBT.SITE_ID   and QWI.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID "
					+ " and QCBT.BILL_ID='"+ billNo + "'  AND QCBT.QS_WORKORDER_ISSUE_ID =  (select QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE  WORK_ORDER_NUMBER = '"+workOrderNo+"' and SITE_ID='"+qsBillBean.getSiteId()+"') and rownum=1 ";
			
			list = jdbcTemplate.queryForList(query);
		
		if (list.size() == 0 && billType.equals("NMR")) {
			query = "select  QCBT.BILL_INVOICE_NUMBER,QCBT.WORK_BLOCK_NAMES,QCBT.BILL_ID,SC.SITE_NAME,QWI.TOTAL_WO_AMOUNT,QCBT.CERTIFIED_AMOUNT,QCBT.CONTRACTOR_ID,QWI.WORK_ORDER_NUMBER,QCBT.CREATED_BY, QCBT.TEMP_BILL_ID ,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME  ,SC.ADDRESS,SC.GSTIN,SC.PAN_NUMBER,SC.MOBILE_NUMBER,SC.BANK_ACC_NUMBER,SC.BANK_NAME,SC.IFSC_CODE,QCBT.SITE_ID  ,"
					+ "PAYBLE_AMOUNT ,QCBT.STATUS ,QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID ,to_char(QCBT.PAYMENT_REQ_DATE,'dd-MM-yyyy') as PAYMENT_REQ_DATE,QCBT.ENTRY_DATE ,QCBT.PAYMENT_TYPE ,QCBT.QS_WORKORDER_ISSUE_ID "
					+ "from   QS_CONTRACTOR_BILL QCBT,QS_WORKORDER_ISSUE QWI,SUMADHURA_CONTRACTOR SC,SITE SC "
					+ "where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID  and SC.SITE_ID=QCBT.SITE_ID and QWI.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID "
					+ "and  QCBT.BILL_ID=? and  QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = ? and SITE_ID=?) ";
					
			list = jdbcTemplate.queryForList(query, billNo,workOrderNo,qsBillBean.getSiteId());
		} 
		List<ContractorQSBillBean> billPaymentlist = new ArrayList<ContractorQSBillBean>();

		for (Map<String, Object> map : list) {
			ContractorQSBillBean bean = new ContractorQSBillBean();
			String blocks=map.get("WORK_BLOCK_NAMES") == null ? "" : map.get("WORK_BLOCK_NAMES").toString();
			bean.setNMRBillBlocks(blocks);
			bean.setBillNo(map.get("BILL_ID") == null ? "" : map.get("BILL_ID").toString());
			bean.setPermanentBillNo(map.get("BILL_ID") == null ? "" : map.get("BILL_ID").toString());
			bean.setBillInvoiceNumber(map.get("BILL_INVOICE_NUMBER") == null ? "-" : map.get("BILL_INVOICE_NUMBER").toString());
			bean.setTotalAmount(map.get("TOTAL_WO_AMOUNT") == null ? "" : map.get("TOTAL_WO_AMOUNT").toString());
			bean.setTotalCurrentCerti(map.get("CERTIFIED_AMOUNT") == null ? "" : map.get("CERTIFIED_AMOUNT").toString());
			bean.setSiteId(map.get("SITE_ID") == null ? "" : map.get("SITE_ID").toString());
			bean.setFromEmpId(map.get("CREATED_BY") == null ? "" : map.get("CREATED_BY").toString());
			//bean.setRaBillNo(map.get("RA_BILL_NO") == null ? "" : map.get("RA_BILL_NO").toString());
			
			bean.setTypeOfWork(map.get("WO_WORK_DESCRIPTION") == null ? "" : map.get("WO_WORK_DESCRIPTION").toString());
			bean.setPaybleAmt(map.get("PAYBLE_AMOUNT") == null ? "" : map.get("PAYBLE_AMOUNT").toString());
			bean.setWorkOrderNo(map.get("WORK_ORDER_NUMBER") == null ? "" : map.get("WORK_ORDER_NUMBER").toString());
			String workOrderNoAndAmount=map.get("OLD_WORK_ORDER_NUMBER_N_AMOUNT") == null ? "" : map.get("OLD_WORK_ORDER_NUMBER_N_AMOUNT").toString();
			if(workOrderNoAndAmount.length()!=0){
				if(workOrderNoAndAmount.contains("@@")){
					String[] tempStr=workOrderNoAndAmount.split("@@");
					bean.setOldWorkOrderNo(tempStr[0]);
					bean.setOldWorkOrderTotalAmount(tempStr[1]);
				}
			}
					
			bean.setSiteName(map.get("SITE_NAME") == null ? "" : map.get("SITE_NAME").toString());
			bean.setEntryDate(map.get("ENTRY_DATE") == null ? "" : map.get("ENTRY_DATE").toString());
			bean.setPaymentReqDate(	map.get("PAYMENT_REQ_DATE") == null ? "" : map.get("PAYMENT_REQ_DATE").toString().substring(0, 10));
			System.out.println("getPaymentReqDate 	w " + bean.getPaymentReqDate());
			bean.setTempBillNo(map.get("TEMP_BILL_ID") == null ? "" : map.get("TEMP_BILL_ID").toString());
			
			bean.setContractorId(map.get("CONTRACTOR_ID") == null ? "" : map.get("CONTRACTOR_ID").toString());
			bean.setContractorName(map.get("CONTRACTOR_NAME") == null ? "" : map.get("CONTRACTOR_NAME").toString());
			bean.setContractorAddress(map.get("ADDRESS") == null ? "-" : map.get("ADDRESS").toString());
			bean.setGSTIN(map.get("GSTIN") == null ? "-" : map.get("GSTIN").toString());
			bean.setContractorPanNo(map.get("PAN_NUMBER") == null ? "-" : map.get("PAN_NUMBER").toString());
			bean.setContractorPhoneNo(map.get("MOBILE_NUMBER") == null ? "-" : map.get("MOBILE_NUMBER").toString());
			bean.setContractorBankName(map.get("BANK_NAME") == null ? "-" : map.get("BANK_NAME").toString());
			bean.setContractorBankAccNumber(map.get("BANK_ACC_NUMBER") == null ? "-" : map.get("BANK_ACC_NUMBER").toString());
			bean.setContractorIFSCCode(map.get("IFSC_CODE") == null ? "-" : map.get("IFSC_CODE").toString());
			
			
			bean.setPaymentType(map.get("PAYMENT_TYPE") == null ? "" : map.get("PAYMENT_TYPE").toString());
			//bean.setRemarks(map.get("PAYMENT_DESC") == null ? "" : map.get("PAYMENT_DESC").toString());
			
			billPaymentlist.add(bean);
		}
	
		//Remarks code IMP
		 try {
			query="select REMARKS,SED.EMP_NAME from QS_CONT_TMP_BILL_APPR_REJ_DTLS DTLS,SUMADHURA_EMPLOYEE_DETAILS SED "
					+ "where SED.EMP_ID=DTLS.EMP_ID and TEMP_BILL_ID=?";
			List<Map<String, Object>> remarksList = jdbcTemplate.queryForList(query, new Object[] { billPaymentlist.get(0).getTempBillNo() });
			String remarks = "";
			for (Map<String, Object> prods2 : remarksList) {
				String empName = prods2.get("EMP_NAME") == null ? "" : prods2.get("EMP_NAME").toString();
				String purpose = prods2.get("REMARKS") == null ? "" : prods2.get("REMARKS").toString();
				purpose=purpose.trim();
				if(purpose.length()!=0)
				remarks +=empName+" - "+ purpose+"\n";
			}		
			billPaymentlist.get(0).setRemarks(remarks);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return billPaymentlist;

	}
/**
 * @description this method used for loading bill details like bill number,bill date,work order number, work order amount by using temporary bill number 
 */
	@Override
	public List<ContractorQSBillBean> getPendingWorkOrderContractorBillDetailById(String tmpBillNo, String userId,
			String billType, String status) {
		tmpBillNo = tmpBillNo.trim();
		String query = "";
		List<Map<String, Object>> list = null;
		boolean flag = Boolean.valueOf(status);
		//if flag is true the the request is from Approve Page
		if (flag) {
			query="select QCBT.TYPE_OF_WORK,QCBT.BILL_INVOICE_NUMBER, QCBT.WORK_BLOCK_NAMES, STE.SITE_NAME,QCBT.PERMANANT_BILL_NUMBER,QWI.TOTAL_WO_AMOUNT, QCBT.CERTIFIED_AMOUNT,QCBT.CONTRACTOR_ID,QWI.QS_WORKORDER_ISSUE_ID,QWI.WORK_ORDER_NUMBER,QCBT.CREATED_BY, QCBT.TEMP_BILL_ID ,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,SC.ADDRESS,SC.GSTIN,SC.PAN_NUMBER,SC.MOBILE_NUMBER,SC.BANK_ACC_NUMBER,SC.BANK_NAME,SC.IFSC_CODE,QCBT.SITE_ID , "
					+ " PAYBLE_AMOUNT ,QCBT.STATUS ,QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID ,to_char(QCBT.PAYMENT_REQ_DATE,'dd-MM-yyyy') as PAYMENT_REQ_DATE,QCBT.ENTRY_DATE ,QCBT.PAYMENT_TYPE ,QCBT.QS_WORKORDER_ISSUE_ID "
					+ " from  QS_CONTRACTOR_BILL_TEMP QCBT,QS_WORKORDER_ISSUE QWI,SUMADHURA_CONTRACTOR SC,SITE STE "
					+ "	where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID and STE.SITE_ID=QCBT.SITE_ID   and QWI.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID  and QCBT.TEMP_BILL_ID=?"; 
			list = jdbcTemplate.queryForList(query, tmpBillNo);
			} else {
			query = "select QCBT.TYPE_OF_WORK,QCBT.BILL_INVOICE_NUMBER,QCBT.WORK_BLOCK_NAMES, STE.SITE_NAME,QCBT.PERMANANT_BILL_NUMBER,QWI.TOTAL_WO_AMOUNT,QCBT.CERTIFIED_AMOUNT,QCBT.CONTRACTOR_ID,QWI.QS_WORKORDER_ISSUE_ID,QWI.WORK_ORDER_NUMBER,QWW.WO_WORK_DESCRIPTION,TIAA.WO_WORK_AREA_ID,TIAA.AMOUNT,TIAA.ALLOCATED_QTY,TIAA.RATE,QCBT.CREATED_BY, QCBT.TEMP_BILL_ID ,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,SC.ADDRESS,SC.GSTIN,SC.PAN_NUMBER,SC.MOBILE_NUMBER,SC.BANK_ACC_NUMBER,SC.BANK_NAME,SC.IFSC_CODE,QCBT.SITE_ID , "
					+ "PAYBLE_AMOUNT ,QCBT.STATUS ,QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID ,to_char(QCBT.PAYMENT_REQ_DATE,'dd-MM-yyyy') as  PAYMENT_REQ_DATE,QCBT.ENTRY_DATE ,QCBT.PAYMENT_TYPE ,QCBT.QS_WORKORDER_ISSUE_ID "
					+ "from  QS_CONTRACTOR_BILL_TEMP QCBT,QS_WORKORDER_ISSUE QWI,SUMADHURA_CONTRACTOR SC,SITE STE,QS_TEMP_INV_AGN_AREA_PMT_DTL TIAA,QS_BOQ_AREA_MAPPING WAM,QS_WO_WORKDESC QWW "
					+ "where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID and STE.SITE_ID=QCBT.SITE_ID   and QWI.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and TIAA.TEMP_BILL_ID=QCBT.TEMP_BILL_ID and WAM.WO_WORK_AREA_ID=TIAA.WO_WORK_AREA_ID and QWW.WO_WORK_DESC_ID=WAM.WO_WORK_DESC_ID and QCBT.TEMP_BILL_ID=? and PENDING_EMP_ID=?";
			
			query="select   QCBT.TYPE_OF_WORK,QCBT.BILL_INVOICE_NUMBER,QCBT.WORK_BLOCK_NAMES,STE.SITE_NAME,QCBT.PERMANANT_BILL_NUMBER,QWI.TOTAL_WO_AMOUNT,QCBT.CERTIFIED_AMOUNT,QCBT.CONTRACTOR_ID,QWI.WORK_ORDER_NUMBER,QCBT.CREATED_BY, QCBT.TEMP_BILL_ID ,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,SC.ADDRESS,SC.GSTIN,SC.PAN_NUMBER,SC.MOBILE_NUMBER,SC.BANK_ACC_NUMBER,SC.BANK_NAME,SC.IFSC_CODE,QCBT.SITE_ID, "
					+ "PAYBLE_AMOUNT ,QCBT.STATUS ,QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID ,to_char(QCBT.PAYMENT_REQ_DATE,'dd-MM-yyyy') as  PAYMENT_REQ_DATE,QCBT.ENTRY_DATE ,QCBT.PAYMENT_TYPE ,QCBT.QS_WORKORDER_ISSUE_ID "
					+ "	from  QS_CONTRACTOR_BILL_TEMP QCBT,QS_WORKORDER_ISSUE QWI,SUMADHURA_CONTRACTOR SC,SITE STE "
					+ "where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID and STE.SITE_ID=QCBT.SITE_ID   and QWI.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and QCBT.TEMP_BILL_ID=? and PENDING_EMP_ID=?";
			
			list = jdbcTemplate.queryForList(query, tmpBillNo, userId);
			}
		List<ContractorQSBillBean> billPaymentlist = new ArrayList<ContractorQSBillBean>();
	/*if (list.size() == 0 && billType.equals("ADV")) {
			query = "select QCBT.PERMANANT_BILL_NUMBER,QWI.TOTAL_WO_AMOUNT,   ,QCBT.CERTIFIED_AMOUNT,QCBT.CONTRACTOR_ID,QWI.WORK_ORDER_NUMBER,QCBT.CREATED_BY, QCBT.TEMP_BILL_ID ,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,QCBT.SITE_ID  ,PAYBLE_AMOUNT ,QCBT.STATUS ,QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID ,trunc(QCBT.PAYMENT_REQ_DATE) as PAYMENT_REQ_DATE,QCBT.ENTRY_DATE ,QCBT.PAYMENT_TYPE ,QCBT.QS_WORKORDER_ISSUE_ID "
					+ "	from  QS_CONTRACTOR_BILL_TEMP QCBT,QS_WORKORDER_ISSUE QWI,SUMADHURA_CONTRACTOR SC "
					+ "	where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID  and QWI.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and  QCBT.TEMP_BILL_ID=? and PENDING_EMP_ID=?";
			list = jdbcTemplate.queryForList(query, tmpBillNo, userId);
		} */
		if (list.size() == 0 && billType.equals("NMR")) {
			query = "select   QCBT.TYPE_OF_WORK,QCBT.BILL_INVOICE_NUMBER,QCBT.WORK_BLOCK_NAMES,STE.SITE_NAME,QCBT.PERMANANT_BILL_NUMBER,QWI.TOTAL_WO_AMOUNT,QCBT.CERTIFIED_AMOUNT,QCBT.CONTRACTOR_ID,QWI.WORK_ORDER_NUMBER,QCBT.CREATED_BY, QCBT.TEMP_BILL_ID ,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,SC.ADDRESS,SC.GSTIN,SC.PAN_NUMBER,SC.MOBILE_NUMBER,SC.BANK_ACC_NUMBER,SC.BANK_NAME,SC.IFSC_CODE,QCBT.SITE_ID,"
					+ "PAYBLE_AMOUNT ,QCBT.STATUS ,QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID ,to_char(QCBT.PAYMENT_REQ_DATE,'dd/MM/yyyy') as PAYMENT_REQ_DATE,QCBT.ENTRY_DATE ,QCBT.PAYMENT_TYPE ,QCBT.QS_WORKORDER_ISSUE_ID "
					+ "	from  QS_CONTRACTOR_BILL_TEMP QCBT,QS_WORKORDER_ISSUE QWI,SUMADHURA_CONTRACTOR SC,SITE STE "
					+ "	where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID and STE.SITE_ID=QCBT.SITE_ID and QWI.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and  QCBT.TEMP_BILL_ID=? and PENDING_EMP_ID=?";
			list = jdbcTemplate.queryForList(query, tmpBillNo, userId);
		} 
		// if status is true then show the data SIte wise
/*		if (list.size() == 0 && billType.equals("ADV") && status.equals("true")) {
			query = "select QCBT.PERMANANT_BILL_NUMBER,QWI.TOTAL_WO_AMOUNT,QCBT.CERTIFIED_AMOUNT,QCBT.CONTRACTOR_ID,QWI.WORK_ORDER_NUMBER,QCBT.CREATED_BY, QCBT.TEMP_BILL_ID ,CONCAT(FIRST_NAME,' '||LAST_NAME)  as CONTRACTOR_NAME ,QCBT.SITE_ID ,PAYBLE_AMOUNT ,QCBT.STATUS ,QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID ,trunc(QCBT.PAYMENT_REQ_DATE) as PAYMENT_REQ_DATE,QCBT.ENTRY_DATE ,QCBT.PAYMENT_TYPE ,QCBT.QS_WORKORDER_ISSUE_ID "
					+ "	from  QS_CONTRACTOR_BILL_TEMP QCBT,QS_WORKORDER_ISSUE QWI,SUMADHURA_CONTRACTOR SC "
					+ "	where SC.CONTRACTOR_ID=QCBT.CONTRACTOR_ID  and QWI.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and  QCBT.TEMP_BILL_ID=? ";
			list = jdbcTemplate.queryForList(query, tmpBillNo);
		}
*/
		
		for (Map<String, Object> map : list) {
			ContractorQSBillBean bean = new ContractorQSBillBean();
			String blocks=map.get("WORK_BLOCK_NAMES") == null ? "" : map.get("WORK_BLOCK_NAMES").toString();
			bean.setContractorAddress(map.get("ADDRESS") == null ? "-" : map.get("ADDRESS").toString());
			bean.setGSTIN(map.get("GSTIN") == null ? "-" : map.get("GSTIN").toString());
			bean.setContractorPanNo(map.get("PAN_NUMBER") == null ? "-" : map.get("PAN_NUMBER").toString());
			bean.setContractorPhoneNo(map.get("MOBILE_NUMBER") == null ? "-" : map.get("MOBILE_NUMBER").toString());
			bean.setContractorBankName(map.get("BANK_NAME") == null ? "-" : map.get("BANK_NAME").toString());
			bean.setContractorBankAccNumber(map.get("BANK_ACC_NUMBER") == null ? "-" : map.get("BANK_ACC_NUMBER").toString());
			bean.setContractorIFSCCode(map.get("IFSC_CODE") == null ? "-" : map.get("IFSC_CODE").toString());
			bean.setTypeOfWork(map.get("TYPE_OF_WORK") == null ? "-" : map.get("TYPE_OF_WORK").toString());
			bean.setNMRBillBlocks(blocks.trim());
			bean.setBillInvoiceNumber(map.get("BILL_INVOICE_NUMBER") == null ? "-" : map.get("BILL_INVOICE_NUMBER").toString());
			bean.setPermanentBillNo(map.get("PERMANANT_BILL_NUMBER") == null ? "" : map.get("PERMANANT_BILL_NUMBER").toString());
			bean.setBillNo(map.get("PERMANANT_BILL_NUMBER") == null ? "" : map.get("PERMANANT_BILL_NUMBER").toString());
			bean.setPendingEmpId(map.get("PENDING_EMP_ID") == null ? "" : map.get("PENDING_EMP_ID").toString());
			bean.setTotalAmount(map.get("TOTAL_WO_AMOUNT") == null ? "" : map.get("TOTAL_WO_AMOUNT").toString());
			bean.setTotalCurrentCerti(map.get("CERTIFIED_AMOUNT") == null ? "" : map.get("CERTIFIED_AMOUNT").toString());
			bean.setSiteId(map.get("SITE_ID") == null ? "" : map.get("SITE_ID").toString());
			bean.setSiteName(map.get("SITE_NAME") == null ? "" : map.get("SITE_NAME").toString());
			bean.setFromEmpId(map.get("CREATED_BY") == null ? "" : map.get("CREATED_BY").toString());
		//	bean.setRaBillNo(map.get("RA_BILL_NO") == null ? "" : map.get("RA_BILL_NO").toString());
			bean.setContractorName(map.get("CONTRACTOR_NAME") == null ? "" : map.get("CONTRACTOR_NAME").toString());
			
			bean.setPaybleAmt(map.get("PAYBLE_AMOUNT") == null ? "" : map.get("PAYBLE_AMOUNT").toString());
			bean.setWorkOrderNo(map.get("WORK_ORDER_NUMBER") == null ? "" : map.get("WORK_ORDER_NUMBER").toString());
			bean.setQSWorkOrderIssueId(map.get("QS_WORKORDER_ISSUE_ID") == null ? "" : map.get("QS_WORKORDER_ISSUE_ID").toString());
			bean.setEntryDate(map.get("ENTRY_DATE") == null ? "" : map.get("ENTRY_DATE").toString());
			String tempDate=map.get("PAYMENT_REQ_DATE").toString();
			
			bean.setPaymentReqDate(map.get("PAYMENT_REQ_DATE") == null ? "" : tempDate);
			log.info("getPaymentReqDate 	w " + bean.getPaymentReqDate());
			bean.setTempBillNo(map.get("TEMP_BILL_ID") == null ? "" : map.get("TEMP_BILL_ID").toString());
			bean.setContractorId(map.get("CONTRACTOR_ID") == null ? "" : map.get("CONTRACTOR_ID").toString());
			bean.setPaymentType(map.get("PAYMENT_TYPE") == null ? "" : map.get("PAYMENT_TYPE").toString());
		//	bean.setRemarks(map.get("PAYMENT_DESC") == null ? "" : map.get("PAYMENT_DESC").toString());
			billPaymentlist.add(bean);
		}
		log.info("billPaymentlist " + billPaymentlist);
		String timeStamp="";
		try {
			query="select REMARKS,SED.EMP_NAME,to_char( DTLS.CREATED_DATE,'dd-MM-yy hh:MM:ss')  as TIME_STAMP  from QS_CONT_TMP_BILL_APPR_REJ_DTLS DTLS,SUMADHURA_EMPLOYEE_DETAILS SED "
					+ "where SED.EMP_ID=DTLS.EMP_ID and TEMP_BILL_ID=? order by QS_BILL_APPR_REJ_DTLS";
			List<Map<String, Object>> remarksList = jdbcTemplate.queryForList(query, new Object[] { tmpBillNo });
			String remarks = "";
			for (Map<String, Object> prods2 : remarksList) {
				String empName = prods2.get("EMP_NAME") == null ? "" : prods2.get("EMP_NAME").toString();
				String purpose = prods2.get("REMARKS") == null ? "" : prods2.get("REMARKS").toString().trim();
				String ttimeStamp= prods2.get("TIME_STAMP") == null ? "" : prods2.get("TIME_STAMP").toString();
				timeStamp=empName+"\n"+ ttimeStamp+"@@";
				if(purpose.length()!=0)
					remarks +=empName+" - "+ purpose+"\n";
			}		
		billPaymentlist.get(0).setRemarks(remarks);
		billPaymentlist.get(0).setDescription1(timeStamp);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return billPaymentlist;
	}
	@Override
	public List<Map<String, Object>> loadPermanentWorkOrderArea(String billNo, String workDesc, String workOrderNo,
			String siteId, String userId, String billType) {/*
				String query="";	
		query="select QWIA.WO_WORK_ISSUE_AREA_DTLS_ID,(select  LISTAGG(concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT1 WHERE QCBT1.PAYMENT_TYPE='RA'  AND   QCBT1.BILL_ID=QSINA.BILL_ID  AND QCBT1.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID  AND QCBT1.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = '"+workOrderNo+"' AND SITE_ID='"+siteId+"' )  AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID  and  QCBT1.BILL_ID<'"+billNo+"' group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea, "
				+ " QWIA.AREA_ALOCATED_QTY,QWIA.PRICE,QCBT.QS_WORKORDER_ISSUE_ID,TD.ACTUAL_AREA,TD.ALLOCATED_QTY,QWI.TOTAL_WO_AMOUNT AS BASIC_AMOUNT,TD.AMOUNT ,TD.RATE , "
				+ "QWAM.WO_WORK_AREA_ID,QWMM.WO_MAJORHEAD_ID,QWMM.WO_MAJORHEAD_DESC,QWM.WO_MINORHEAD_DESC,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,SI.SITE_NAME ,B.NAME as BLOCK_NAME,F.NAME AS FLOOR_NAME,FLAT.name as FLATNAME "
				+ " ,(case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID "
				+ " from QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK B on B.BLOCK_ID = QWAM.BLOCK_ID "
				+ "left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID "
				+ " left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID ,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,QS_WO_MINORHEAD QWM,QS_WO_MAJORHEAD QWMM,SITE SI,QS_INV_AGN_AREA_PMT_DTL TD "
				+ "	 ,QS_WORKORDER_ISSUE_DETAILS TD1,QS_CONTRACTOR_BILL QCBT ,QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_AREA_DETAILS QWIA  "
				+ "where TD1.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and QWIA.WO_WORK_ISSUE_DTLS_ID=TD1.WO_WORK_ISSUE_DTLS_ID and QCBT.QS_WORKORDER_ISSUE_ID=Qwi.QS_WORKORDER_ISSUE_ID and QWIA.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID and QCBT.QS_WORKORDER_ISSUE_ID=TD.QS_WORKORDER_ISSUE_ID  AND   QCBT.BILL_ID=TD.BILL_ID "
				+ " and td.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID and  QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID  and QWM.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID AND QWM.WO_MAJORHEAD_ID=QWMM.WO_MAJORHEAD_ID  "
				//.append(" AND  trim(QWIA.RECORD_TYPE) not in ('MATERIAL') ")
				+ "AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID  and TD.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID "
				+ "AND SI.SITE_ID=QWAM.SITE_ID  and QCBT.SITE_ID='"+siteId+"'  and QWAM.status='A' and QCBT.BILL_ID='"+billNo+"' AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER= '"+workOrderNo+"'  and SITE_ID='"+siteId+"')  ORDER BY QWMM.WO_MAJORHEAD_DESC,QWW.WO_WORK_DESCRIPTION,(case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),(case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),(case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)";
		
		List<Map<String, Object>> areaMappingList = jdbcTemplate.queryForList(query);
		
		List<String> workAreaIdList=new ArrayList<String>();
		for (Map<String, Object> map : areaMappingList) {
			workAreaIdList.add("'" + map.get("WO_WORK_ISSUE_AREA_DTLS_ID").toString() + "'");
		}
		
		
		//this query for loading non selected area
		List<Map<String, Object>> nonSelectedPaymentArea = new ArrayList<Map<String, Object>>();
		//checking the bill type and changing the query based on bill
		if(billType.equals("ADV")){
			//||'-'||QSINA.BILL_ID||'-'||QCBT.QS_WORKORDER_ISSUE_ID // AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 
			//query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(nvl(ALLOCATED_QTY,0), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID group by QSINA.WO_WORK_AREA_ID)as prevArea,";
			query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')    WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER ='"+workOrderNo+"' AND SITE_ID='"+siteId+"' )   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID    and  QCBT.BILL_ID<'"+billNo+"'  group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,";
		}else if(billType.equals("RA")){
			query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE  QCBT.PAYMENT_TYPE='"+billType+"'  and  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = '"+workOrderNo+"'  AND SITE_ID='"+siteId+"')   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID   and  QCBT.BILL_ID<'"+billNo+"'  group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,";
		}
		query+=" QWAM.WO_WORK_DESC_ID,minor.WO_MINORHEAD_ID,minor.WO_MINORHEAD_DESC,major.WO_MAJORHEAD_ID,major.WO_MAJORHEAD_DESC,QWIA.AREA_ALOCATED_QTY,QWIA.PRICE,QWIA.WO_WORK_ISSUE_AREA_DTLS_ID,QWW.WO_WORK_DESCRIPTION,(SELECT TOTAL_WO_AMOUNT FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) as BASIC_AMOUNT,TD.TOTAL_AMOUNT ,TD.CREATED_DATE ,TD.STATUS ,TD.WO_WORK_ISSUE_DTLS_ID ,TD.QS_WORKORDER_ISSUE_ID ,TD.WO_COMMENT,QWAM.WO_WORK_AREA_ID, 	QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,    F.NAME AS FLOOR_NAME,B.NAME as BLOCK_NAME,FLAT.name as FLATNAME,   (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID,    SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL"
			 + " from QS_WORKORDER_ISSUE_DETAILS TD, QS_WORKORDER_ISSUE_AREA_DETAILS QWIA left join QS_TEMP_INV_AGN_AREA_PMT_DTL QSPM   on QWIA.WO_WORK_AREA_ID=QSPM.WO_WORK_AREA_ID, QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK B on B.BLOCK_ID = QWAM.BLOCK_ID  		left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID 		 left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,"
			 + " SITE SI,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW ,QS_WO_MINORHEAD minor,QS_WO_MAJORHEAD major"
			 + " where QWIA.WO_WORK_ISSUE_DTLS_ID=TD.WO_WORK_ISSUE_DTLS_ID and QWAM.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 		AND SI.SITE_ID=QWAM.SITE_ID"
			 + " AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID AND QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID 		and minor.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID  and major.WO_MAJORHEAD_ID=minor.WO_MAJORHEAD_ID "
			// .append(" AND QWIA.RECORD_TYPE not in ('MATERIAL') ")
			 + " and QWIA.WO_WORK_ISSUE_AREA_DTLS_ID not in ("+workAreaIdList.toString().replace("[", "").replace("]", "")+") "
			 + " AND TD.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) AND TD.STATUS='A' and QWIA.status='A' ORDER BY major.WO_MAJORHEAD_DESC,QWW.WO_WORK_DESCRIPTION,"
			 //this case used bcoz of there is change that floor id,flat_id will be null that's why we are assigning zero value if any flat or floor id is null
			 //so it's helping to sort the data
			 + " (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),"
			 + " (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),"
			 + " (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)";
		
		nonSelectedPaymentArea = jdbcTemplate.queryForList(query, workOrderNo,workOrderNo);
		areaMappingList.addAll(nonSelectedPaymentArea);
		
		return areaMappingList;
*/
		StringBuffer query=new StringBuffer("select (select  RTRIM(XMLAGG(XMLELEMENT(E,concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE),'@@').EXTRACT('//text()') ORDER BY INV_AGAINST_PMT_DTLS_ID).GetClobVal(),'@@') from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT1 WHERE QCBT1.STATUS IN ('A','I') AND QCBT1.PAYMENT_TYPE=?  AND   QCBT1.BILL_ID=QSINA.BILL_ID  AND QCBT1.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID  AND QCBT1.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER =? AND SITE_ID=? )  AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID  and  QCBT1.BILL_ID<? group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea, QWIA.WO_WORK_ISSUE_AREA_DTLS_ID, ")
				.append(" QWIA.AREA_ALOCATED_QTY,QWIA.PRICE,QCBT.QS_WORKORDER_ISSUE_ID,TD.ACTUAL_AREA,TD.ALLOCATED_QTY,QWI.TOTAL_WO_AMOUNT AS BASIC_AMOUNT,TD.AMOUNT ,TD.RATE , ")
				.append(" QWAM.WO_WORK_AREA_ID,QWMM.WO_MAJORHEAD_ID,QWMM.WO_MAJORHEAD_DESC,QWM.WO_MINORHEAD_DESC,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,SI.SITE_NAME ,B.NAME as BLOCK_NAME,F.NAME AS FLOOR_NAME,FLAT.name as FLATNAME ")
				.append(" ,(case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID ")
				.append(" from QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK B on B.BLOCK_ID = QWAM.BLOCK_ID ")
				.append(" left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID ")
				.append(" left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID ,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,QS_WO_MINORHEAD QWM,QS_WO_MAJORHEAD QWMM,SITE SI,QS_INV_AGN_AREA_PMT_DTL TD ")
				.append(" ,QS_WORKORDER_ISSUE_DETAILS TD1,QS_CONTRACTOR_BILL QCBT ,QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_AREA_DETAILS QWIA  ")
				.append(" where TD1.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and QWIA.WO_WORK_ISSUE_DTLS_ID=TD1.WO_WORK_ISSUE_DTLS_ID and QCBT.QS_WORKORDER_ISSUE_ID=Qwi.QS_WORKORDER_ISSUE_ID and QWIA.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID and QCBT.QS_WORKORDER_ISSUE_ID=TD.QS_WORKORDER_ISSUE_ID  AND   QCBT.BILL_ID=TD.BILL_ID ")
				.append(" and td.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID and  QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID  and QWM.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID AND QWM.WO_MAJORHEAD_ID=QWMM.WO_MAJORHEAD_ID  ")
				//.append(" AND  trim(QWIA.RECORD_TYPE) not in ('MATERIAL') ")
				.append(" AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID  and TD.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID ")
				.append(" AND SI.SITE_ID=QWAM.SITE_ID  and QCBT.SITE_ID=?  and QWAM.status='A' and QCBT.BILL_ID=? AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER= ? and SITE_ID=?)  ORDER BY QWMM.WO_MAJORHEAD_DESC,QWW.WO_WORK_DESCRIPTION,(case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),(case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),(case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");
		 Object obj[]={billType,workOrderNo,siteId,billNo,siteId,billNo,workOrderNo,siteId};
				
		List<Map<String, Object>> areaMappingList = jdbcTemplate.queryForList(query.toString(),obj);
		
		List<String> workAreaIdList=new ArrayList<String>();
		for (Map<String, Object> map : areaMappingList) {
			workAreaIdList.add("'" + map.get("WO_WORK_ISSUE_AREA_DTLS_ID").toString() + "'");
		}
		//if(workAreaIdList.size()==0){workAreaIdList.add("'00'");}
		
		//this query for loading non selected area
		List<Map<String, Object>> nonSelectedPaymentArea = new ArrayList<Map<String, Object>>();
		//checking the bill type and changing the query based on bill
		if(billType.equals("ADV")){
			//||'-'||QSINA.BILL_ID||'-'||QCBT.QS_WORKORDER_ISSUE_ID // AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 
			//query="Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(nvl(ALLOCATED_QTY,0), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID group by QSINA.WO_WORK_AREA_ID)as prevArea,";
			query=new StringBuffer(" Select (select RTRIM(XMLAGG(XMLELEMENT(E,concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE),'@@').EXTRACT('//text()') ORDER BY INV_AGAINST_PMT_DTLS_ID).GetClobVal(),'@@') from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE  QCBT.STATUS IN ('A','I') AND QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER =? AND SITE_ID=? )   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID    and  QCBT.BILL_ID<?  group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,(QWAM.WO_WORK_AREA_ID),");
		}else if(billType.equals("RA")){
			query=new StringBuffer(" Select (select RTRIM(XMLAGG(XMLELEMENT(E,concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE),'@@').EXTRACT('//text()') ORDER BY INV_AGAINST_PMT_DTLS_ID).GetClobVal(),'@@') from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE  QCBT.STATUS IN ('A','I') AND QCBT.PAYMENT_TYPE='"+billType+"'  and  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = ?  AND SITE_ID=?)   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID   and  QCBT.BILL_ID<?  group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,(QWAM.WO_WORK_AREA_ID),");
		}
			query.append(" QWAM.WO_WORK_DESC_ID,minor.WO_MINORHEAD_ID,minor.WO_MINORHEAD_DESC,major.WO_MAJORHEAD_ID,major.WO_MAJORHEAD_DESC,QWIA.AREA_ALOCATED_QTY,QWIA.PRICE,QWIA.WO_WORK_ISSUE_AREA_DTLS_ID,QWW.WO_WORK_DESCRIPTION,(SELECT TOTAL_WO_AMOUNT FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID=? ) as BASIC_AMOUNT,TD.TOTAL_AMOUNT ,TD.CREATED_DATE ,TD.STATUS ,TD.WO_WORK_ISSUE_DTLS_ID ,TD.QS_WORKORDER_ISSUE_ID ,TD.WO_COMMENT,QWAM.WO_WORK_AREA_ID, 	QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,    F.NAME AS FLOOR_NAME,B.NAME as BLOCK_NAME,FLAT.name as FLATNAME,   (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID, SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL ")
			 .append(" from QS_WORKORDER_ISSUE_DETAILS TD, QS_WORKORDER_ISSUE_AREA_DETAILS QWIA, QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK B on B.BLOCK_ID = QWAM.BLOCK_ID  		left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID 		 left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,")
			 .append(" SITE SI,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW ,QS_WO_MINORHEAD minor,QS_WO_MAJORHEAD major")
			 .append(" where QWIA.WO_WORK_ISSUE_DTLS_ID=TD.WO_WORK_ISSUE_DTLS_ID and QWAM.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 		AND SI.SITE_ID=QWAM.SITE_ID")
			 .append(" AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID AND QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID 		and minor.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID  and major.WO_MAJORHEAD_ID=minor.WO_MAJORHEAD_ID  ")
			 .append(" AND QWIA.RECORD_TYPE not in ('MATERIAL') ")
			 .append(" and QWIA.WO_WORK_ISSUE_AREA_DTLS_ID not in ("+workAreaIdList.toString().replace("[", "").replace("]", "")+") ")
			 .append(" AND TD.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID=? ) AND TD.STATUS='A' and QWIA.status='A' ORDER BY major.WO_MAJORHEAD_DESC,QWW.WO_WORK_DESCRIPTION,")
			 //this case used bcoz of there is change that floor id,flat_id will be null that's why we are assigning zero value if any flat or floor id is null
			 //so it's helping to sort the data
			 .append(" (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),")
			 .append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),")
			 .append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");
		
		nonSelectedPaymentArea = jdbcTemplate.queryForList(query.toString(),workOrderNo,siteId,billNo,workOrderNo,siteId,workOrderNo,siteId );
		areaMappingList.addAll(nonSelectedPaymentArea);
		
		return areaMappingList;
		
	}

	/**
	 * @description this method is used to load the data for bill abstract it may RA bill or ADV bill
	 * this data will be loaded based on contractor and work order number 
	 */
	@Override
	public List<Map<String, Object>> loadWorkOrderAreaForBill(String contractorId, String workDesc, String workOrderNo,
			String siteId, String userId, String billType) {/*
		String query = "";
		String workOrderNum = "";
		
		if (workOrderNo.contains("$")) {
			workOrderNum = workOrderNo.split("\\$")[0];
		} else {
			workOrderNum = workOrderNo.trim();
		}
		List<Map<String, Object>> areaMappingList = new ArrayList<Map<String, Object>>();
		//checking the bill type and changing the query based on bill
		if(billType.equals("ADV")){
			//||'-'||QSINA.BILL_ID||'-'||QCBT.QS_WORKORDER_ISSUE_ID // AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 
			//query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(nvl(ALLOCATED_QTY,0), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID group by QSINA.WO_WORK_AREA_ID)as prevArea,";
			query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')    WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER ='"+workOrderNum+"' AND SITE_ID='"+siteId+"' )   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,";
		}else if(billType.equals("RA")){
			query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE  QCBT.PAYMENT_TYPE='"+billType+"'  and  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = '"+workOrderNum+"'  AND SITE_ID='"+siteId+"')   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,";
		}
		query+=" QWAM.WO_WORK_DESC_ID,minor.WO_MINORHEAD_ID,minor.WO_MINORHEAD_DESC,major.WO_MAJORHEAD_ID,major.WO_MAJORHEAD_DESC,QWIA.AREA_ALOCATED_QTY,QWIA.PRICE,QWIA.WO_WORK_ISSUE_AREA_DTLS_ID,QWW.WO_WORK_DESCRIPTION,(SELECT TOTAL_WO_AMOUNT FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) as BASIC_AMOUNT,TD.TOTAL_AMOUNT ,TD.CREATED_DATE ,TD.STATUS ,TD.WO_WORK_ISSUE_DTLS_ID ,TD.QS_WORKORDER_ISSUE_ID ,TD.WO_COMMENT,QWAM.WO_WORK_AREA_ID, 	QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,    F.NAME AS FLOOR_NAME,B.NAME as BLOCK_NAME,FLAT.name as FLATNAME,   (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID,    SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL"
			 + " from QS_WORKORDER_ISSUE_DETAILS TD, QS_WORKORDER_ISSUE_AREA_DETAILS QWIA left join QS_TEMP_INV_AGN_AREA_PMT_DTL QSPM   on QWIA.WO_WORK_AREA_ID=QSPM.WO_WORK_AREA_ID, QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK B on B.BLOCK_ID = QWAM.BLOCK_ID  		left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID 		 left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,"
			 + " SITE SI,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW ,QS_WO_MINORHEAD minor,QS_WO_MAJORHEAD major"
			 + " where QWIA.WO_WORK_ISSUE_DTLS_ID=TD.WO_WORK_ISSUE_DTLS_ID and QWAM.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 		AND SI.SITE_ID=QWAM.SITE_ID"
			 + " AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID AND QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID 		and minor.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID  and major.WO_MAJORHEAD_ID=minor.WO_MAJORHEAD_ID"
			 + " AND  trim(QWIA.RECORD_TYPE) not in ('MATERIAL') "
			 + " AND TD.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) AND TD.STATUS='A' and QWIA.status='A' ORDER BY major.WO_MAJORHEAD_DESC,QWW.WO_WORK_DESCRIPTION,"
			 //this case used bcoz of there is change that floor id,flat_id will be null that's why we are assigning zero value if any flat or floor id is null
			 //so it's helping to sort the data
			 + " (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),"
			 + " (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),"
			 + " (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)";
		
		areaMappingList = jdbcTemplate.queryForList(query, workOrderNum,workOrderNum);
		
		return areaMappingList;
	*/
		StringBuffer query = null;
		String workOrderNum = "";
		
		if (workOrderNo.contains("$")) {
			workOrderNum = workOrderNo.split("\\$")[0];
		} else {
			workOrderNum = workOrderNo.trim();
		}
		List<Map<String, Object>> areaMappingList = new ArrayList<Map<String, Object>>();
		//checking the bill type and changing the query based on bill
		if(billType.equals("ADV")){
			//||'-'||QSINA.BILL_ID||'-'||QCBT.QS_WORKORDER_ISSUE_ID // AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 
			//query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(nvl(ALLOCATED_QTY,0), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID group by QSINA.WO_WORK_AREA_ID)as prevArea,";
			query=new StringBuffer(" Select (select RTRIM(XMLAGG(XMLELEMENT(E,concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE),'@@').EXTRACT('//text()') ORDER BY INV_AGAINST_PMT_DTLS_ID).GetClobVal(),'@@') from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE  QCBT.STATUS in('A','I') AND  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER =? AND SITE_ID=? )   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID )as prevArea,(QWAM.WO_WORK_AREA_ID),");
		}else if(billType.equals("RA")){
			query=new StringBuffer(" Select  (select  RTRIM(XMLAGG(XMLELEMENT(E,concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE),'@@').EXTRACT('//text()') ORDER BY INV_AGAINST_PMT_DTLS_ID).GetClobVal(),'@@') from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE QCBT.STATUS in('A','I') AND  QCBT.PAYMENT_TYPE='"+billType+"'  and  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = ?  AND SITE_ID=?)   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID )as prevArea,(QWAM.WO_WORK_AREA_ID),");
		}
		query.append(" QWAM.WO_WORK_DESC_ID,minor.WO_MINORHEAD_ID,minor.WO_MINORHEAD_DESC,major.WO_MAJORHEAD_ID,major.WO_MAJORHEAD_DESC,QWIA.AREA_ALOCATED_QTY,QWIA.PRICE,QWIA.WO_WORK_ISSUE_AREA_DTLS_ID,QWW.WO_WORK_DESCRIPTION,(SELECT TOTAL_WO_AMOUNT FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID=? ) as BASIC_AMOUNT,TD.TOTAL_AMOUNT ,TD.CREATED_DATE ,TD.STATUS ,TD.WO_WORK_ISSUE_DTLS_ID ,TD.QS_WORKORDER_ISSUE_ID ,TD.WO_COMMENT,QWAM.WO_WORK_AREA_ID, 	QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,    F.NAME AS FLOOR_NAME,B.NAME as BLOCK_NAME,FLAT.name as FLATNAME,  ")
			 .append(" (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID,    SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL")
			 .append(" from QS_WORKORDER_ISSUE_DETAILS TD, QS_WORKORDER_ISSUE_AREA_DETAILS QWIA , QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK B on B.BLOCK_ID = QWAM.BLOCK_ID  		left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID 		 left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,")
			 .append(" SITE SI,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW ,QS_WO_MINORHEAD minor,QS_WO_MAJORHEAD major")
			 .append(" where QWIA.WO_WORK_ISSUE_DTLS_ID=TD.WO_WORK_ISSUE_DTLS_ID and QWAM.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 		AND SI.SITE_ID=QWAM.SITE_ID")
			 .append(" AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID AND QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID 		and minor.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID  and major.WO_MAJORHEAD_ID=minor.WO_MAJORHEAD_ID ")
			 .append(" AND  trim(QWIA.RECORD_TYPE) not in ('MATERIAL') ")
			 .append(" AND TD.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID=? ) AND TD.STATUS='A' and QWIA.status='A' ORDER BY major.WO_MAJORHEAD_DESC,QWW.WO_WORK_DESCRIPTION,")
			 //this case used bcoz of there is change that floor id,flat_id will be null that's why we are assigning zero value if any flat or floor id is null
			 //so it's helping to sort the data
			 .append(" (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),")
			 .append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),")
			 .append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");
		
		areaMappingList = jdbcTemplate.queryForList(query.toString(), workOrderNum,siteId,workOrderNum,siteId,workOrderNum,siteId);
		
		return areaMappingList;
	
	}
	
	/**
	 * @description this method is used load the abstract data of the bill in approval level and status of the page.
	 * the data will be loaded basis on temporary bill no
	 */
	@Override
	public List<Map<String, Object>> loadWorkOrderAreaForApprovelBill(String tempBillNo, String billType,
			String workOrderNo, String siteId, String userId) {/*
		tempBillNo = tempBillNo.trim();
		String query ="";
		List<Map<String, Object>> areaMappingList = new ArrayList<Map<String, Object>>(); 
		if(billType.equals("ADV")){
			//query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(nvl(ALLOCATED_QTY,0), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID group by QSINA.WO_WORK_AREA_ID)as prevArea,";
			query="    Select  distinct(TD.WO_WORK_ISSUE_AREA_DTLS_ID),(QWAM.WO_WORK_AREA_ID),(select  LISTAGG(concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')    WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = '"+workOrderNo+"'  AND SITE_ID='"+siteId+"' )   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,";
		}else if(billType.equals("RA")){
			query="    Select  distinct(TD.WO_WORK_ISSUE_AREA_DTLS_ID),(QWAM.WO_WORK_AREA_ID),(select LISTAGG(concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE  QCBT.PAYMENT_TYPE='"+billType+"'  and  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = '"+workOrderNo+"' AND SITE_ID='"+siteId+"' )    AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,";
		}
	//	query="select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(nvl(ALLOCATED_QTY,0), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE  QCBT.PAYMENT_TYPE='"+billType+"' AND   QCBT.BILL_ID=QSINA.BILL_ID and  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=?  and SITE_ID='"+siteId+"') AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID group by QSINA.WO_WORK_AREA_ID)as prevArea, "
				query+= "QWIA.AREA_ALOCATED_QTY,QWIA.PRICE,QCBT.QS_WORKORDER_ISSUE_ID,td.TEMP_BILL_ID,TD.ACTUAL_AREA,TD.ALLOCATED_QTY,QWI.TOTAL_WO_AMOUNT AS BASIC_AMOUNT,TD.AMOUNT ,TD.RATE , "
				+ " QWMM.WO_MAJORHEAD_ID,QWMM.WO_MAJORHEAD_DESC,QWM.WO_MINORHEAD_DESC,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,F.NAME AS FLOOR_NAME,FLAT.name as FLATNAME,block.NAME as BLOCK_NAME,  (case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID,SI.SITE_NAME "
				+ " from QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK block on block.BLOCK_ID = QWAM.BLOCK_ID "
				+ "	left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID "
				+ "	 left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,QS_WO_MINORHEAD QWM,QS_WO_MAJORHEAD QWMM,SITE SI,QS_TEMP_INV_AGN_AREA_PMT_DTL TD "
				+ "	,QS_WORKORDER_ISSUE_DETAILS TD1,QS_CONTRACTOR_BILL_TEMP QCBT ,QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_AREA_DETAILS QWIA "
				+ "	where TD1.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID and QWIA.WO_WORK_ISSUE_DTLS_ID=TD1.WO_WORK_ISSUE_DTLS_ID and QCBT.QS_WORKORDER_ISSUE_ID=Qwi.QS_WORKORDER_ISSUE_ID  and TD.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID  and QWIA.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID and QCBT.TEMP_BILL_ID=TD.TEMP_BILL_ID "
				+ "	and td.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID and  QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID  and QWM.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID AND QWM.WO_MAJORHEAD_ID=QWMM.WO_MAJORHEAD_ID "
				+ " AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID "
				+ "	AND SI.SITE_ID=QWAM.SITE_ID and QWAM.status='A' and QCBT.TEMP_BILL_ID=?   ORDER BY QWMM.WO_MAJORHEAD_DESC,QWW.WO_WORK_DESCRIPTION,"
				+ "	(case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end),"
				+ " (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),"
				+ "	(case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)";
		areaMappingList = jdbcTemplate.queryForList(query, tempBillNo);
	
		List<String> workAreaIdList=new ArrayList<String>();
		for (Map<String, Object> map : areaMappingList) {
			workAreaIdList.add("'" + map.get("WO_WORK_ISSUE_AREA_DTLS_ID").toString() + "'");
		}
		
		
		//this query for loading non selected area
		List<Map<String, Object>> nonSelectedPaymentArea = new ArrayList<Map<String, Object>>();
		//checking the bill type and changing the query based on bill
		if(billType.equals("ADV")){
			//||'-'||QSINA.BILL_ID||'-'||QCBT.QS_WORKORDER_ISSUE_ID // AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 
			//query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(nvl(ALLOCATED_QTY,0), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID group by QSINA.WO_WORK_AREA_ID)as prevArea,";
			query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')    WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER ='"+workOrderNo+"' AND SITE_ID='"+siteId+"' )   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,";
		}else if(billType.equals("RA")){
			query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE  QCBT.PAYMENT_TYPE='"+billType+"'  and  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = '"+workOrderNo+"'  AND SITE_ID='"+siteId+"')   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,";
		}
		query+=" QWAM.WO_WORK_DESC_ID,minor.WO_MINORHEAD_ID,minor.WO_MINORHEAD_DESC,major.WO_MAJORHEAD_ID,major.WO_MAJORHEAD_DESC,QWIA.AREA_ALOCATED_QTY,QWIA.PRICE,QWIA.WO_WORK_ISSUE_AREA_DTLS_ID,QWW.WO_WORK_DESCRIPTION,(SELECT TOTAL_WO_AMOUNT FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) as BASIC_AMOUNT,TD.TOTAL_AMOUNT ,TD.CREATED_DATE ,TD.STATUS ,TD.WO_WORK_ISSUE_DTLS_ID ,TD.QS_WORKORDER_ISSUE_ID ,TD.WO_COMMENT,QWAM.WO_WORK_AREA_ID, 	QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,    F.NAME AS FLOOR_NAME,B.NAME as BLOCK_NAME,FLAT.name as FLATNAME,   (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID,    SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL"
			 + " from QS_WORKORDER_ISSUE_DETAILS TD, QS_WORKORDER_ISSUE_AREA_DETAILS QWIA left join QS_TEMP_INV_AGN_AREA_PMT_DTL QSPM   on QWIA.WO_WORK_AREA_ID=QSPM.WO_WORK_AREA_ID, QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK B on B.BLOCK_ID = QWAM.BLOCK_ID  		left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID 		 left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,"
			 + " SITE SI,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW ,QS_WO_MINORHEAD minor,QS_WO_MAJORHEAD major"
			 + " where QWIA.WO_WORK_ISSUE_DTLS_ID=TD.WO_WORK_ISSUE_DTLS_ID and QWAM.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 		AND SI.SITE_ID=QWAM.SITE_ID"
			 + " AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID AND QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID 		and minor.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID  and major.WO_MAJORHEAD_ID=minor.WO_MAJORHEAD_ID "
			 + " AND  TRIM(QWIA.RECORD_TYPE) not in ('MATERIAL') "
			 + " and QWIA.WO_WORK_ISSUE_AREA_DTLS_ID not in ("+workAreaIdList.toString().replace("[", "").replace("]", "")+") "
			 + " AND TD.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) AND TD.STATUS='A' and QWIA.status='A' ORDER BY major.WO_MAJORHEAD_DESC,QWW.WO_WORK_DESCRIPTION,"
			 //this case used bcoz of there is change that floor id,flat_id will be null that's why we are assigning zero value if any flat or floor id is null
			 //so it's helping to sort the data
			 + " (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),"
			 + " (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),"
			 + " (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)";
		
		nonSelectedPaymentArea = jdbcTemplate.queryForList(query, workOrderNo,workOrderNo);
		areaMappingList.addAll(nonSelectedPaymentArea);
		return areaMappingList;
	*/
		tempBillNo = tempBillNo.trim();
		StringBuffer query =null;
		List<Map<String, Object>> areaMappingList = new ArrayList<Map<String, Object>>(); 
		if(billType.equals("ADV")){
			query=new StringBuffer("Select (select RTRIM(XMLAGG(XMLELEMENT(E,concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE),'@@').EXTRACT('//text()') ORDER BY INV_AGAINST_PMT_DTLS_ID).GetClobVal(),'@@') from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE QCBT.STATUS in('A','I') AND  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER =? AND SITE_ID=?)   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,(TD.WO_WORK_ISSUE_AREA_DTLS_ID),(QWAM.WO_WORK_AREA_ID),");
		}else if(billType.equals("RA")){
			query=new StringBuffer("Select (select RTRIM(XMLAGG(XMLELEMENT(E,concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE),'@@').EXTRACT('//text()') ORDER BY INV_AGAINST_PMT_DTLS_ID).GetClobVal(),'@@') from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE QCBT.STATUS in('A','I') AND  QCBT.PAYMENT_TYPE='"+billType+"'  and  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER =? AND SITE_ID=?)    AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,(TD.WO_WORK_ISSUE_AREA_DTLS_ID),(QWAM.WO_WORK_AREA_ID),");
		}
	//	query="select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(nvl(ALLOCATED_QTY,0), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE  QCBT.PAYMENT_TYPE='"+billType+"' AND   QCBT.BILL_ID=QSINA.BILL_ID and  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=?  and SITE_ID='"+siteId+"') AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID group by QSINA.WO_WORK_AREA_ID)as prevArea, "
		query.append("QWIA.AREA_ALOCATED_QTY,QWIA.PRICE,QCBT.QS_WORKORDER_ISSUE_ID,td.TEMP_BILL_ID,TD.ACTUAL_AREA,TD.ALLOCATED_QTY,QWI.TOTAL_LABOUR_AMOUNT AS BASIC_AMOUNT,TD.AMOUNT ,TD.RATE , ")
				.append(" QWMM.WO_MAJORHEAD_ID,QWMM.WO_MAJORHEAD_DESC,QWM.WO_MINORHEAD_DESC,QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,F.NAME AS FLOOR_NAME,FLAT.name as FLATNAME,block.NAME as BLOCK_NAME,  (case when block.BLOCK_ID  is null then '0'  else block.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID,SI.SITE_NAME ")
				.append(" from QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK block on block.BLOCK_ID = QWAM.BLOCK_ID ")
				.append(" LEFT OUTER JOIN FLOOR F ON F.FLOOR_ID = QWAM.FLOOR_ID ")
				.append(" LEFT OUTER JOIN FLAT FLAT ON FLAT.FLAT_ID = QWAM.FLAT_ID,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW,QS_WO_MINORHEAD QWM,QS_WO_MAJORHEAD QWMM,SITE SI,QS_TEMP_INV_AGN_AREA_PMT_DTL TD ")
				.append(" ,QS_WORKORDER_ISSUE_DETAILS TD1,QS_CONTRACTOR_BILL_TEMP QCBT ,QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_AREA_DETAILS QWIA ")
				.append(" WHERE TD1.QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID AND QWIA.WO_WORK_ISSUE_DTLS_ID=TD1.WO_WORK_ISSUE_DTLS_ID AND QCBT.QS_WORKORDER_ISSUE_ID=QWI.QS_WORKORDER_ISSUE_ID  AND TD.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID  AND QWIA.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID AND QCBT.TEMP_BILL_ID=TD.TEMP_BILL_ID ")
				.append(" AND TD.WO_WORK_AREA_ID=QWAM.WO_WORK_AREA_ID AND  QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID  AND QWM.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID AND QWM.WO_MAJORHEAD_ID=QWMM.WO_MAJORHEAD_ID ")
				.append(" AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID ")
				.append(" AND SI.SITE_ID=QWAM.SITE_ID and QWAM.status='A' and QCBT.TEMP_BILL_ID=?   ORDER BY QWMM.WO_MAJORHEAD_DESC,QWW.WO_WORK_DESCRIPTION,")
				.append(" (CASE WHEN BLOCK.BLOCK_ID  IS NULL THEN '0'  ELSE BLOCK.BLOCK_ID END),")
				.append(" (CASE WHEN  F.FLOOR_ID  IS NULL THEN  '0'  ELSE F.FLOOR_ID END),")
				.append(" (CASE WHEN FLAT.FLAT_ID  IS NULL THEN '0' ELSE  FLAT.FLAT_ID  END)");
		areaMappingList = jdbcTemplate.queryForList(query.toString(),workOrderNo,siteId, tempBillNo);
	
		List<String> workAreaIssueIdList=new ArrayList<String>();
		for (Map<String, Object> map : areaMappingList) {
			workAreaIssueIdList.add("'" + map.get("WO_WORK_ISSUE_AREA_DTLS_ID").toString() + "'");
		}
		
		//this query for loading non selected area
		List<Map<String, Object>> nonSelectedPaymentArea = new ArrayList<Map<String, Object>>();
		//checking the bill type and changing the query based on bill
		if(billType.equals("ADV")){
			//||'-'||QSINA.BILL_ID||'-'||QCBT.QS_WORKORDER_ISSUE_ID // AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 
			//query="    Select distinct(QWAM.WO_WORK_AREA_ID),(select LISTAGG(nvl(ALLOCATED_QTY,0), '@@')   WITHIN GROUP (ORDER BY INV_AGAINST_PMT_DTLS_ID) from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID='"+siteId+"' ) AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID group by QSINA.WO_WORK_AREA_ID)as prevArea,";
			query=new StringBuffer("Select (select RTRIM(XMLAGG(XMLELEMENT(E,concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE),'@@').EXTRACT('//text()') ORDER BY INV_AGAINST_PMT_DTLS_ID).GetClobVal(),'@@') from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.STATUS in('A','I') AND   QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER =? AND SITE_ID=? )   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,(QWAM.WO_WORK_AREA_ID),");
		}else if(billType.equals("RA")){
			query=new StringBuffer("Select (select RTRIM(XMLAGG(XMLELEMENT(E,concat(nvl(ALLOCATED_QTY,0),'-'||QCBT.PAYMENT_TYPE||'-'||RATE),'@@').EXTRACT('//text()') ORDER BY INV_AGAINST_PMT_DTLS_ID).GetClobVal(),'@@') from QS_INV_AGN_AREA_PMT_DTL  QSINA ,QS_CONTRACTOR_BILL QCBT WHERE   QCBT.STATUS in('A','I') AND   QCBT.PAYMENT_TYPE='"+billType+"'  and  QCBT.QS_WORKORDER_ISSUE_ID=QSINA.QS_WORKORDER_ISSUE_ID   AND   QCBT.BILL_ID=QSINA.BILL_ID AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER =?  AND SITE_ID=?)   AND  QSINA.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID  AND  QSINA.WO_WORK_ISSUE_AREA_DTLS_ID=QWIA.WO_WORK_ISSUE_AREA_DTLS_ID group by QSINA.WO_WORK_ISSUE_AREA_DTLS_ID)as prevArea,(QWAM.WO_WORK_AREA_ID),");
		}
		query.append(" QWAM.WO_WORK_DESC_ID,minor.WO_MINORHEAD_ID,minor.WO_MINORHEAD_DESC,major.WO_MAJORHEAD_ID,major.WO_MAJORHEAD_DESC,QWIA.AREA_ALOCATED_QTY,QWIA.PRICE,QWIA.WO_WORK_ISSUE_AREA_DTLS_ID,QWW.WO_WORK_DESCRIPTION,(SELECT TOTAL_LABOUR_AMOUNT FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID=? ) as BASIC_AMOUNT,TD.TOTAL_AMOUNT ,TD.CREATED_DATE ,TD.STATUS ,TD.WO_WORK_ISSUE_DTLS_ID ,TD.QS_WORKORDER_ISSUE_ID ,TD.WO_COMMENT,QWAM.WO_WORK_AREA_ID, 	QWAM.WO_WORK_DESC_ID,WO_WORK_DESCRIPTION,QWAM.WO_MEASURMENT_ID,QWM.WO_MEASURMEN_NAME,    F.NAME AS FLOOR_NAME,B.NAME as BLOCK_NAME,FLAT.name as FLATNAME, ")
			 .append(" (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end)as BLOCK_ID,   (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end)as FLOOR_ID,    (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)as  FLAT_ID,    SI.SITE_NAME,QWAM.WO_WORK_AREA,QWAM.WO_WORK_AVAILABE_AREA,QWAM.WO_PERCENTAGE_AVAIL")
			 .append(" from QS_WORKORDER_ISSUE_DETAILS TD, QS_WORKORDER_ISSUE_AREA_DETAILS QWIA , QS_BOQ_AREA_MAPPING QWAM left outer join  BLOCK B on B.BLOCK_ID = QWAM.BLOCK_ID  		left outer join FLOOR F on F.FLOOR_ID = QWAM.FLOOR_ID 		 left outer join FLAT FLAT on FLAT.FLAT_ID = QWAM.FLAT_ID,")
			 .append(" SITE SI,QS_WO_MEASURMENT QWM,QS_WO_WORKDESC QWW ,QS_WO_MINORHEAD minor,QS_WO_MAJORHEAD major")
			 .append(" where QWIA.WO_WORK_ISSUE_DTLS_ID=TD.WO_WORK_ISSUE_DTLS_ID and QWAM.WO_WORK_AREA_ID=QWIA.WO_WORK_AREA_ID 		AND SI.SITE_ID=QWAM.SITE_ID")
			 .append(" AND QWM.WO_MEASURMENT_ID=QWAM.WO_MEASURMENT_ID AND QWW.WO_WORK_DESC_ID=QWAM.WO_WORK_DESC_ID 		and minor.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID  and major.WO_MAJORHEAD_ID=minor.WO_MAJORHEAD_ID ")
			 .append(" AND  trim(QWIA.RECORD_TYPE) not in ('MATERIAL') ")
			 .append(" and QWIA.WO_WORK_ISSUE_AREA_DTLS_ID not in ("+workAreaIssueIdList.toString().replace("[", "").replace("]", "")+") ")
			 .append(" AND TD.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID=? ) AND TD.STATUS='A' and QWIA.status='A' ORDER BY major.WO_MAJORHEAD_DESC,QWW.WO_WORK_DESCRIPTION,")
			 //this case used bcoz of there is change that floor id,flat_id will be null that's why we are assigning zero value if any flat or floor id is null
			 //so it's helping to sort the data
			 .append(" (case when B.BLOCK_ID  is null then '0'  else B.BLOCK_ID end),")
			 .append(" (case when  F.FLOOR_ID  is null then  '0'  else F.FLOOR_ID end),")
			 .append(" (case when FLAT.FLAT_ID  is null then '0' else  FLAT.FLAT_ID  end)");
		
		nonSelectedPaymentArea = jdbcTemplate.queryForList(query.toString(), workOrderNo,siteId,workOrderNo,siteId,workOrderNo,siteId);
		areaMappingList.addAll(nonSelectedPaymentArea);
		query=null;
		return areaMappingList;
	
	}

	/**
	 * @description this method will store all the changed details of RA or ADV bill
	 */
	@Override
	public int saveChangedAdvBillDetails(ContractorQSBillBean actualBill, ContractorQSBillBean changedBill) {
		String query = "";
		int result = 0;
		// checking bill type
		//query="update QS_CONTRACTOR_BILL set DEDUCTION_AMOUNT='"++"' where BILL_ID= ";
		
		if (actualBill.getPaymentType().equals("RA")) {
			int seq = jdbcTemplate.queryForObject("SELECT QS_CONT_TMP_BILL_CHANGED_DTLS_SEQ.NEXTVAL FROM DUAL",
					Integer.class);
			
			query = "INSERT INTO QS_CONT_TMP_BILL_CHANGED_DTLS "
					+ "(QS_BILL_CHANGED_DTLS,ACTUAL_BILL_AMOUNT,CHANGED_BILL_AMOUNT,"
					+ "ACTUAL_ADVANCE_AMOUNT,CHANGED_ADVANCE_AMOUNT,ACTUAL_SECURIRT_DEP_AMOUNT,"
					+ "CHANGED_SECURIRT_DEP_AMOUNT,REMARKS,REATED_DATE,TEMP_BILL_NO) " + "VALUES(?,?,?,?,?,?,?,?,SYSDATE,?)";
			Object[] queryParams = { seq, actualBill.getPaybleAmt(), changedBill.getPaybleAmt(), "", "",
					"", "", changedBill.getRemarks(), actualBill.getTempBillNo() };
			result = jdbcTemplate.update(query, queryParams);
		} else {

			query = "INSERT INTO QS_CONT_TMP_BILL_CHANGED_DTLS "
					+ "(QS_BILL_CHANGED_DTLS,ACTUAL_BILL_AMOUNT,CHANGED_BILL_AMOUNT,"
					+ "ACTUAL_ADVANCE_AMOUNT,CHANGED_ADVANCE_AMOUNT,ACTUAL_SECURIRT_DEP_AMOUNT,"
					+ "CHANGED_SECURIRT_DEP_AMOUNT,REMARKS,REATED_DATE,TEMP_BILL_NO) "
					+ "VALUES(QS_CONT_TMP_BILL_CHANGED_DTLS_SEQ.NEXTVAL,?,?,?,?,?,?,?,SYSDATE,?)";
			Object[] queryParams = { "", "", actualBill.getPaybleAmt(), changedBill.getPaybleAmt(), "",
					"", changedBill.getRemarks(), actualBill.getTempBillNo() };
			result = jdbcTemplate.update(query, queryParams);
		}

		return result;
	}

	/**
	 * @description this method will move the temporary bill to next level if any next level is availble,
	 * if this is the final level for the bill we need to insert all the temporary details into permanent table
	 * 
	 */
	@Override
	public int updateBillLevel(ContractorQSBillBean changedBill) throws Exception {
		String query = "";
		// if next level employee id is not available then store the data into
		// permanent table
		int count = 0;
		int result = 0;
		String contractorBillSeq = "";
		String WOWisebillNo="";	
		String tempBillNo = "";
		// checking pending employee id if END then insert into permanent table
		if (changedBill.getPendingEmpId().equals("END")) {
			StringBuffer queryForGeneratingBillNumber=new StringBuffer("  SELECT  MAX(NVL(TO_NUMBER(REGEXP_SUBSTR (BILL_ID, '[^/]+', 1, 2)),0))+1    AS BILL_ID ")
					.append(" FROM QS_CONTRACTOR_BILL  WHERE  QS_WORKORDER_ISSUE_ID =  ")
					.append(" (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER =?  AND SITE_ID=?) AND PAYMENT_TYPE=? ");	

			
			String workOrderNo=changedBill.getWorkOrderNo().trim();String siteId=changedBill.getSiteId();
			String workOrderIssueId=jdbcTemplate.queryForObject("SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? and SITE_ID=?", new Object[]{workOrderNo,changedBill.getSiteId()}, String.class);
			String permanentBillNo="";
			//here we are updating the temporary bills and making inactive in last approval of bill's
			if (changedBill.getPaymentType().equals("RA")) {
				query = "UPDATE QS_CONTRACTOR_BILL_TEMP SET PENDING_EMP_ID=?,PAYBLE_AMOUNT=?,CERTIFIED_AMOUNT=?,DEDUCTION_AMOUNT=?,status='I' WHERE TEMP_BILL_ID=?";
				Object[] queryParams = { changedBill.getPendingEmpId(), changedBill.getPaybleAmt(),
						changedBill.getTotalCurrentCerti(), changedBill.getTotalCurrntDeducAmt(),
						changedBill.getTempBillNo() };
				count = jdbcTemplate.update(query, queryParams);
			} else {
				changedBill.setBillNo(changedBill.getTempBillNo());
				query = "UPDATE QS_CONTRACTOR_BILL_TEMP SET PENDING_EMP_ID=?,PAYBLE_AMOUNT=?,CERTIFIED_AMOUNT=?,status='I' WHERE TEMP_BILL_ID=?";
				Object[] queryParams = { changedBill.getPendingEmpId(), changedBill.getPaybleAmt(),
						changedBill.getPaybleAmt(), changedBill.getTempBillNo() };
				count = jdbcTemplate.update(query, queryParams);
			}
			
			tempBillNo = changedBill.getTempBillNo();
			// generating permanent bill id based on Payment Type
			String	contractorPKSeqNumber = jdbcTemplate.queryForObject("SELECT QS_CONTRACTOR_BILL_SEQ.NEXTVAL FROM DUAL",String.class);
			int accCntPaymentSeq = jdbcTemplate.queryForObject("select ACC_CNT_PAYMENT_SEQ.NEXTVAL FROM DUAL",Integer.class);
			permanentBillNo=changedBill.getPermanentBillNo();
			if(permanentBillNo==null){
				permanentBillNo="";
			}
			if (changedBill.getPaymentType().equals("ADV")) {
				if(permanentBillNo.contains("ADV/")){
					contractorBillSeq=permanentBillNo;
				}else{
					Object[] obj={workOrderNo,changedBill.getSiteId(),changedBill.getPaymentType()};
					contractorBillSeq=jdbcTemplate.queryForObject(queryForGeneratingBillNumber.toString(),obj,String.class);	
					//if the contractorBillSeq is null then put default value 1 
					//if the bill creating first time then only we will get the null output from query
					if(contractorBillSeq==null){
						contractorBillSeq="1";	
					}
					int num=Integer.valueOf(contractorBillSeq);
							//adding one digit before the number eg. (1) then (01) so the bill no is like ADV/01, RA/01,NMR/01,ADV/02........
					contractorBillSeq = changedBill.getPaymentType()+"/" + String.format("%02d", num);
				}
				//throw new RuntimeException("hi issue in final approval");
			} else if (changedBill.getPaymentType().equals("RA")) {
				if(permanentBillNo.contains("RA/")){
					contractorBillSeq=permanentBillNo;
				}else{
					Object[] obj={workOrderNo,changedBill.getSiteId(),changedBill.getPaymentType()};
					contractorBillSeq=jdbcTemplate.queryForObject(queryForGeneratingBillNumber.toString(),obj,String.class);	
					//if the contractorBillSeq is null then put default value 1 
					//if the bill creating first time then only we will get the null output from query
					if(contractorBillSeq==null){
						contractorBillSeq="1";	
					}
					int num=Integer.valueOf(contractorBillSeq);
							//adding one digit before the number eg. (1) then (01) so the bill no is like ADV/01, RA/01,NMR/01,ADV/02........
					contractorBillSeq = changedBill.getPaymentType()+"/" + String.format("%02d", num);
				}
				//throw new RuntimeException("hi issue in final approval");
			}
			
			//Copying Temporary data into permanent table
			String queryForContractorBill = "INSERT INTO QS_CONTRACTOR_BILL(CONT_SEQ_BILL_NO,BILL_ID,BILL_NO,CONTRACTOR_ID,SITE_ID, "
					+ "PAYBLE_AMOUNT,STATUS,PAYMENT_REQ_DATE,ENTRY_DATE,PAYMENT_TYPE,CERTIFIED_AMOUNT,"
					+ " DEDUCTION_AMOUNT,DEDUCTION_REF_ID,TEMP_BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_STATUS,CREATED_BY,PAYMENT_TYPE_OF_WORK,BILL_INVOICE_NUMBER,TYPE_OF_WORK)"
					+ " select '"+contractorPKSeqNumber+"','" + contractorBillSeq+ "','"+WOWisebillNo+"',CONTRACTOR_ID,SITE_ID, "
					+ "PAYBLE_AMOUNT,'A',PAYMENT_REQ_DATE,ENTRY_DATE,PAYMENT_TYPE,CERTIFIED_AMOUNT," /*changedBill.getTotalCurrentCerti()*/
					+ " DEDUCTION_AMOUNT,DEDUCTION_REF_ID," + tempBillNo
					+ ",QS_WORKORDER_ISSUE_ID,'PENDING',CREATED_BY,PAYMENT_TYPE_OF_WORK,BILL_INVOICE_NUMBER,TYPE_OF_WORK from QS_CONTRACTOR_BILL_TEMP where TEMP_BILL_ID="
					+ tempBillNo + "";
			 result = jdbcTemplate.update(queryForContractorBill);

			String queryForAccPayment = "";
			int accResult = 0;
			int siteWisePaymentNo = jdbcTemplate.queryForObject("select count(SITE_ID)+1 from ACC_CNT_PAYMENT where SITE_ID='" + changedBill.getSiteId() + "'",Integer.class);
			String str = getWorkOrderPendingEmployeeId(changedBill.getUserId(),changedBill.getSiteId());

			//inserting data in Recovery
			int QS_WORKORDER_ISSUE_ID=jdbcTemplate.queryForObject("(select QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER=? and SITE_ID=? ) ", new Object[]{workOrderNo,changedBill.getSiteId()}, Integer.class);
			//load all the recovery data
			List<Map<String, Object>> listOfRecoverys=jdbcTemplate.queryForList("select CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_AMOUNT,QS_WORKORDER_ISSUE_ID from QS_TEMP_BILL_RECOVERY where TEMP_BILL_ID=? ",tempBillNo);
			String child_product_id="";
			String measurement_id="";
			int recoveryResult=0;
			for (Map<String, Object> map : listOfRecoverys) {
				child_product_id=map.get("CHILD_PRODUCT_ID")==null?"":map.get("CHILD_PRODUCT_ID").toString();
				measurement_id=map.get("MEASUREMENT_ID")==null?"":map.get("MEASUREMENT_ID").toString();
				
				//checking is this record exists or not if not not insert record or update the existing record using child_id
				String queryForRecovery="select count(*) from  QS_BILL_RECOVERY where  CHILD_PRODUCT_ID=? and MEASUREMENT_ID=? and QS_WORKORDER_ISSUE_ID=?";
				result=jdbcTemplate.queryForObject(queryForRecovery,new Object[]{child_product_id,measurement_id,QS_WORKORDER_ISSUE_ID}, Integer.class);
 
				if(result!=0){
					Double recoveryAmount=jdbcTemplate.queryForObject("select RECOVERY_AMOUNT from  QS_TEMP_BILL_RECOVERY where TEMP_BILL_ID='"+tempBillNo+"' and  CHILD_PRODUCT_ID=? and MEASUREMENT_ID=?   ",new Object[]{child_product_id,measurement_id}, Double.class);
					queryForRecovery="update  QS_BILL_RECOVERY set RECOVERY_AMOUNT=RECOVERY_AMOUNT+? where CHILD_PRODUCT_ID=? and MEASUREMENT_ID=? and QS_WORKORDER_ISSUE_ID=? ";
					recoveryResult	=jdbcTemplate.update(queryForRecovery,recoveryAmount,child_product_id,measurement_id,QS_WORKORDER_ISSUE_ID);
				}else if(result==0){
					recoveryResult=	jdbcTemplate.update("insert into QS_BILL_RECOVERY(CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,QS_WORKORDER_ISSUE_ID)"
							+ " select CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,QS_WORKORDER_ISSUE_ID "
							+ " from QS_TEMP_BILL_RECOVERY where TEMP_BILL_ID='"+tempBillNo+"' and CHILD_PRODUCT_ID='"+child_product_id+"' and MEASUREMENT_ID='"+measurement_id+"'");
					
				}
			}
			
			//inserting all records in QS_BILL_RECOVERY_HISTORY from QS_TEMP_BILL_RECOVERY_HISTORY
			//Copying Temporary data into permanent table
			String queryForRecoveryHistory="insert into QS_BILL_RECOVERY_HISTORY(CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,BILL_ID,QS_WORKORDER_ISSUE_ID) "
										 + "select                CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,'"+contractorBillSeq+"',QS_WORKORDER_ISSUE_ID from  QS_TEMP_BILL_RECOVERY_HISTORY  where TEMP_BILL_ID='"+tempBillNo+"'";
			recoveryResult=jdbcTemplate.update(queryForRecoveryHistory);
			//data copying completed 
			//inserting data in ACC_CNT_PAYMENT
			if (changedBill.getPaymentType().equals("RA")) {
				queryForAccPayment = "insert into ACC_CNT_PAYMENT(BILL_NUMBER,CNT_PAYMENT_ID,CREATED_DATE,BILL_AMOUNT,PAYMENT_REQ_AMOUNT,BILL_DATE, "
					+ "WO_AMOUNT,WO_DATE,WO_NUMBER,SITE_ID,STATUS,REMARKS,CONTRACTOR_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE,"
					+ " REQUEST_PENDING_DEPT_ID,REQUEST_PENDING_EMP_ID,SITEWISE_PAYMENT_NO,PARTICULAR,PAYMENT_REQ_DATE) "
					+ "select  '"+contractorPKSeqNumber+"','" + accCntPaymentSeq+ "',sysdate,CERTIFIED_AMOUNT,PAYBLE_AMOUNT,to_date(PAYMENT_REQ_DATE,'dd-MM-yy') ,"
					+ " (select TOTAL_WO_AMOUNT from QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+ changedBill.getWorkOrderNo()+ "'  and SITE_ID='"+changedBill.getSiteId()+"' )"
							+ ",(select to_date(QS_WORKORDER_DATE,'dd-MM-yyyy') from QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+ changedBill.getWorkOrderNo() + "'  and SITE_ID='"+changedBill.getSiteId()+"'),'" + changedBill.getWorkOrderNo()+ "',SITE_ID,'A','',CONTRACTOR_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE," + " '-','" + str	+ "','" + siteWisePaymentNo + "','REGULAR',to_date(PAYMENT_REQ_DATE,'dd-MM-yy') "
					+ " from QS_CONTRACTOR_BILL where QS_WORKORDER_ISSUE_ID =(select QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER='"+workOrderNo+"' and SITE_ID='"+changedBill.getSiteId()+"' )  and BILL_ID='" + contractorBillSeq + "' and PAYMENT_TYPE='"
					+ changedBill.getPaymentType() + "'";
			System.out.println(queryForAccPayment);
			accResult = jdbcTemplate.update(queryForAccPayment);
			} else if (changedBill.getPaymentType().equals("ADV")) {
				queryForAccPayment = "";
				queryForAccPayment = " insert into ACC_CNT_PAYMENT(BILL_NUMBER,CNT_PAYMENT_ID,CREATED_DATE,BILL_AMOUNT,PAYMENT_REQ_AMOUNT,BILL_DATE,"
						+ " WO_AMOUNT,WO_DATE,WO_NUMBER,SITE_ID,STATUS,REMARKS,CONTRACTOR_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE, REQUEST_PENDING_DEPT_ID,REQUEST_PENDING_EMP_ID,SITEWISE_PAYMENT_NO,PARTICULAR,PAYMENT_REQ_DATE) "
						+ "	select  '"+contractorPKSeqNumber+"','" + accCntPaymentSeq+ "',sysdate,CERTIFIED_AMOUNT,PAYBLE_AMOUNT,PAYMENT_REQ_DATE 	,"
						+ "(select TOTAL_WO_AMOUNT from QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+ changedBill.getWorkOrderNo()+ "'  and SITE_ID='"+changedBill.getSiteId()+"'),"
								+ "(select to_date(QS_WORKORDER_DATE,'dd-MM-yyyy') from QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+ changedBill.getWorkOrderNo() + "'  and SITE_ID='"+changedBill.getSiteId()+"'),'" + changedBill.getWorkOrderNo()+ "',SITE_ID,'A','',CONTRACTOR_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE, " + "	'-','" + str
						+ "','" + siteWisePaymentNo
						+ "','ADVANCE',PAYMENT_REQ_DATE from QS_CONTRACTOR_BILL where  QS_WORKORDER_ISSUE_ID =   (select QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER='"+workOrderNo+"'  and SITE_ID='"+changedBill.getSiteId()+"')  and BILL_ID='" + contractorBillSeq
						+ "' and PAYMENT_TYPE='" + changedBill.getPaymentType() + "'";

				accResult = jdbcTemplate.update(queryForAccPayment);
			}
			//for payment area Copying Temporary data into permanent table
			String queryForBillPaymentArea = "INSERT INTO QS_INV_AGN_AREA_PMT_DTL  (INV_AGAINST_PMT_DTLS_ID,WO_WORK_AREA_ID,RATE,AMOUNT,BILL_ID,ALLOCATED_QTY,ACTUAL_AREA,WO_WORK_ISSUE_AREA_DTLS_ID,QS_WORKORDER_ISSUE_ID) "
					+ " SELECT QS_INV_AGN_AREA_PMT_DTL_SEQ.NEXTVAL,WO_WORK_AREA_ID,RATE,AMOUNT,'"
					+ contractorBillSeq + "',ALLOCATED_QTY,ACTUAL_AREA,WO_WORK_ISSUE_AREA_DTLS_ID,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+workOrderNo+"'  and SITE_ID='"+changedBill.getSiteId()+"') "
					+ " FROM QS_TEMP_INV_AGN_AREA_PMT_DTL WHERE TEMP_BILL_ID=" + tempBillNo + "";
			result = jdbcTemplate.update(queryForBillPaymentArea);

			if (changedBill.getPaymentType().equals("RA")) {
				//for the RA bill only we have deduction's so here we are storing temporary deduction details into permanent table
				query = "INSERT INTO   QS_BILL_DEDUCTION_DTLS (DEDUCTION_ID,CONTRACTOR_BILL_ID,DEDUCTION_DESC,DEDUCTION_AMOUNT,TYPE_OF_DEDUCTION,QS_WORKORDER_ISSUE_ID) "
						+ " SELECT QS_BILL_DEDUCTION_DTLS_SEQ.NEXTVAL,?,DEDUCTION_DESC,DEDUCTION_AMOUNT,TYPE_OF_DEDUCTION,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+workOrderNo+"'  and SITE_ID='"+changedBill.getSiteId()+"') "
						+ " FROM QS_TEMP_BILL_DEDUCTION_DTLS WHERE TEMP_BILL_ID=? ";
				Object[] obj1 = { contractorBillSeq, tempBillNo };
				result = jdbcTemplate.update(query, obj1);

				query = "UPDATE QS_CONTRACTOR_BILL_TEMP SET PENDING_EMP_ID=?,PAYBLE_AMOUNT=?,CERTIFIED_AMOUNT=?,DEDUCTION_AMOUNT=? WHERE TEMP_BILL_ID=?";
				Object[] queryParams = { changedBill.getPendingEmpId(), changedBill.getPaybleAmt(),
						changedBill.getTotalCurrentCerti(), changedBill.getTotalCurrntDeducAmt(),
						changedBill.getTempBillNo() };
				count = jdbcTemplate.update(query, queryParams);
				changedBill.setBillNo(contractorBillSeq);
			}
		//adding permanent bill number to the changedBill object of view page
		changedBill.setBillNo(String.valueOf(contractorBillSeq));
		} else {
			// checking bill type and based on the bill type updating the temporary bill data
			changedBill.setBillNo(String.valueOf(changedBill.getTempBillNo()));
			if (changedBill.getPaymentType().equals("RA")) {
				query = "UPDATE QS_CONTRACTOR_BILL_TEMP SET PENDING_EMP_ID=?,PAYBLE_AMOUNT=?,CERTIFIED_AMOUNT=?,DEDUCTION_AMOUNT=? WHERE TEMP_BILL_ID=?";
				Object[] queryParams = { changedBill.getPendingEmpId(), changedBill.getPaybleAmt(),
						changedBill.getTotalCurrentCerti(), changedBill.getTotalCurrntDeducAmt(),
						changedBill.getTempBillNo() };
				count = jdbcTemplate.update(query, queryParams);
			}else {
				changedBill.setBillNo(changedBill.getTempBillNo());
				query = "UPDATE QS_CONTRACTOR_BILL_TEMP SET PENDING_EMP_ID=?,PAYBLE_AMOUNT=?,CERTIFIED_AMOUNT=? WHERE TEMP_BILL_ID=?";
				Object[] queryParams = { changedBill.getPendingEmpId(), changedBill.getPaybleAmt(),
					 changedBill.getPaybleAmt(), changedBill.getTempBillNo() };
				count = jdbcTemplate.update(query, queryParams);
			}
		}
		//
		return count;
	}

	/**
	 * @description this method is used for validate the used enterd bill number
	 * if the bill number is not valid return false
	 */
	@Override
	public boolean isValidTempBillNo(String[] data) {
		// if status if not null then then the data is selecting based on temp
		// bill no and site wise
		// else data is selecting for approving time based on temp bill no and
		// user id and site wise
		if (data[3].length() != 0) {
			Object[] obj = { data[0], data[2] };
			String query = "SELECT COUNT (*) FROM  QS_CONTRACTOR_BILL_TEMP QCBT WHERE QCBT.TEMP_BILL_ID=? AND QCBT.SITE_ID=?";
			if(data[3].equals("false")){
				 query = "SELECT COUNT (*) FROM  QS_CONTRACTOR_BILL_TEMP QCBT WHERE QCBT.TEMP_BILL_ID=? AND QCBT.SITE_ID=?  AND QCBT.PENDING_EMP_ID='"+data[1].trim()+"'  and STATUS='A' ";
			}
			int count = jdbcTemplate.queryForObject(query, obj, Integer.class);
			if (count > 0) {
				return true;
			} else {
				return false;
			}
		}

		String query = "SELECT COUNT (*) FROM  QS_CONTRACTOR_BILL_TEMP QCBT WHERE QCBT.TEMP_BILL_ID=? AND QCBT.PENDING_EMP_ID=? AND QCBT.SITE_ID=?";
		Object[] obj = { data[0], data[1], data[2] };
		int count = jdbcTemplate.queryForObject(query, obj, Integer.class);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @description this method will return is the current processing bill number is valid or not
	 */
	@Override
	public boolean isValidCompltedTempBillNo(String[] data) {
		String tempBillNo = data[0];
		String siteId = data[2];
		String workOrderNo=data[4];
		StringBuffer query = new StringBuffer("SELECT COUNT (*) FROM  QS_CONTRACTOR_BILL QCBT WHERE QCBT.BILL_ID=? AND QCBT.SITE_ID=? ")
				.append("AND QCBT.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER = ? AND SITE_ID=? )");
		Object[] params={tempBillNo,siteId,workOrderNo,siteId};
		int count = jdbcTemplate.queryForObject(query.toString(),params, Integer.class);
		if (count > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @description this method is used to reject the bill, updating status as "R" means rejected
	 */
	@Override
	public int rejectAdvanceGeneratedBill(String tempBillNo) {
		String query = "UPDATE  QS_CONTRACTOR_BILL_TEMP SET  STATUS='R' where TEMP_BILL_ID=?";
		Object[] params = { tempBillNo };
		int result = jdbcTemplate.update(query, params);
		return result;
	}
	/**
	 * @description this method used for loading all the changed details of RA/ADV/NMR bill
	 */
	@Override
	public List<Map<String, Object>> loadBillChangedDetails(String tmpBillNo, String siteId, String userId) {
		String queryForLoadAdvBillChangedDtls = "SELECT QS_BILL_CHANGED_DTLS ,ACTUAL_BILL_AMOUNT ,CHANGED_BILL_AMOUNT ,ACTUAL_ADVANCE_AMOUNT ,CHANGED_ADVANCE_AMOUNT ,ACTUAL_SECURIRT_DEP_AMOUNT ,CHANGED_SECURIRT_DEP_AMOUNT ,REMARKS ,REATED_DATE,TEMP_BILL_NO  FROM QS_CONT_TMP_BILL_CHANGED_DTLS WHERE TEMP_BILL_NO=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(queryForLoadAdvBillChangedDtls, tmpBillNo.trim());
		return list;
	}

	/**
	 * @description this method is used for loading all the bill deduction details  by using temp bill number
	 */
	@Override
	public List<Map<String, Object>> getRaAdvDeductionDetails(String tempBillNo, String userId, String status) {
		tempBillNo = tempBillNo.trim();
		String queryForAmtDeductionDtls = "SELECT DEDUCTION_ID,DEDUCTION_AMOUNT ,TYPE_OF_DEDUCTION  FROM QS_TEMP_BILL_DEDUCTION_DTLS WHERE TEMP_BILL_ID=?";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(queryForAmtDeductionDtls, tempBillNo);
		return list;
	}
	 @Override
	public List<Map<String, Object>> userAbleToCreateBillTypes(String user_id, WorkOrderBean workOrderBean) {
			List<Map<String, Object>> dbBillTypeDts = null;
			String query = "SELECT SAMD.MODULE_TYPE FROM  SUMADHURA_APPROVER_MAPPING_DTL SAMD WHERE SAMD.SITE_ID=? AND SAMD.EMP_ID = ? AND  SAMD.MODULE_TYPE IN ('WOB','WOB_CONSULTANT') ";
			Object[] obj={ workOrderBean.getSiteId(),user_id};
			dbBillTypeDts = jdbcTemplate.queryForList(query,obj);
			return dbBillTypeDts;
	}

	/**
	 * @description this method will return the complted RA bill deduction data
	 */
	@Override
	public List<Map<String, Object>> getRaAdvCompletedDeductionDetails(ContractorQSBillBean bean) {
		String billNo = bean.getBillNo();
		String siteId=bean.getSiteId();
		String workOrderNo=bean.getWorkOrderNo();
		String queryForAmtDeductionDtls = "select DEDUCTION_ID,DEDUCTION_AMOUNT ,TYPE_OF_DEDUCTION  from QS_BILL_DEDUCTION_DTLS where CONTRACTOR_BILL_ID=? and QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=?  and SITE_ID='"+siteId+"')";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(queryForAmtDeductionDtls, billNo,workOrderNo);
		return list;
	}

	/**
	 * @description this method is for updating all the deduction types of RA bill
	 * if any modification happens we need to update in DB
	 */
	@Override
	public int updateChangedRADeductBillDetails(ContractorQSBillBean actualBill, ContractorQSBillBean changedBill) {
		int result=0;
		if(changedBill.getPettyExpensesCurrentCerti().length()!=0){
			String query = "update QS_TEMP_BILL_DEDUCTION_DTLS set  DEDUCTION_AMOUNT=?  where TEMP_BILL_ID=? and TYPE_OF_DEDUCTION='PETTY'";
			Object[] secQueryParams = { changedBill.getPettyExpensesCurrentCerti(), actualBill.getTempBillNo() };
			 result = jdbcTemplate.update(query, secQueryParams);
		}
		if(changedBill.getOtherAmout().length()!=0){
			String query = "update QS_TEMP_BILL_DEDUCTION_DTLS set  DEDUCTION_AMOUNT=?  where TEMP_BILL_ID=? and TYPE_OF_DEDUCTION='OTHER'";
			Object[] secQueryParams = { changedBill.getOtherAmout(), actualBill.getTempBillNo() };
			 result = jdbcTemplate.update(query, secQueryParams);
		}
		if(changedBill.getRecovery_amount().length()!=0){
			String query = "update QS_TEMP_BILL_DEDUCTION_DTLS set  DEDUCTION_AMOUNT=?  where TEMP_BILL_ID=? and TYPE_OF_DEDUCTION='RECOVERY'";
			Object[] secQueryParams = { changedBill.getRecovery_amount(), actualBill.getTempBillNo() };
			result = jdbcTemplate.update(query, secQueryParams);
		}
		
		String query = "update QS_TEMP_BILL_DEDUCTION_DTLS set  DEDUCTION_AMOUNT=?  where TEMP_BILL_ID=? and TYPE_OF_DEDUCTION='SEC'";
		Object[] secQueryParams = { changedBill.getSecDepositCurrentCerti(), actualBill.getTempBillNo() };
		 result = jdbcTemplate.update(query, secQueryParams);

		// checking is security deposit is exists or not if not then insert the
		// security deposit
		if (result == 0) {
			
		}
		// **********************************************************
		query = "update QS_TEMP_BILL_DEDUCTION_DTLS set  DEDUCTION_AMOUNT=?  where TEMP_BILL_ID=? and TYPE_OF_DEDUCTION='ADV'";
		Object[] advQueryParams = { changedBill.getRaDeductionAmt(), actualBill.getTempBillNo() };
		result = jdbcTemplate.update(query, advQueryParams);
		// checking is Advance deduction is exists or not if not then insert the
		// advance deduction
		if (result == 0) {}
		return result;
	}

	/**
	 * @description this method will load all the deduction values
	 */
	@Override
	public List<Map<String, Object>> getAllDedcutedAmount(ContractorQSBillBean bean) {
		String query = "SELECT sum(nvl(QSDD.DEDUCTION_AMOUNT,0)) as DEDUCTION_AMOUNT,TYPE_OF_DEDUCTION 			 FROM QS_CONTRACTOR_BILL QCBT left join QS_BILL_DEDUCTION_DTLS QSDD on QSDD.CONTRACTOR_BILL_ID=QCBT.BILL_ID "
				+ "WHERE CONTRACTOR_ID=?   and PAYMENT_TYPE='RA' AND QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID 		FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? and SITE_ID='"+bean.getSiteId()+"') and TYPE_OF_DEDUCTION is not null group by TYPE_OF_DEDUCTION";
		
		query = "SELECT sum(nvl(QSDD.DEDUCTION_AMOUNT,0)) as DEDUCTION_AMOUNT,TYPE_OF_DEDUCTION 			 FROM QS_CONTRACTOR_BILL QCBT left join QS_BILL_DEDUCTION_DTLS QSDD on QSDD.CONTRACTOR_BILL_ID=QCBT.BILL_ID "
				+ "WHERE CONTRACTOR_ID=?   and bill_id!=? and PAYMENT_TYPE='RA' AND QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID 		FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? and SITE_ID='"+bean.getSiteId()+"') and TYPE_OF_DEDUCTION is not null group by TYPE_OF_DEDUCTION";
	
		String workOrderNo = bean.getWorkOrderNo();
		String contractorId = bean.getContractorId();
		String billNo=bean.getBillNo();
		Object[] params = { contractorId, billNo,workOrderNo};
		List<Map<String, Object>> list = jdbcTemplate.queryForList(query, params);
		return list;
	}
	/**no use
	 * @description this method will load the previous paid amount to the contractor for this work order
	 */
	@Override
	public int getContractorPaidAmount(ContractorQSBillBean bean) {
		String query = "SELECT sum(QCBT.CERTIFIED_AMOUNT) PreviousPaid FROM QS_CONTRACTOR_BILL QCBT "
				+ "WHERE CONTRACTOR_ID=? and PAYMENT_TYPE='RA'    AND QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID 		FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? and SITE_ID='"+bean.getSiteId()+"')";
      	String workOrderNo = bean.getWorkOrderNo();
		String contractorId = bean.getContractorId();
		Object[] params = { contractorId, workOrderNo};
		int paidAmount = jdbcTemplate.queryForObject(query, params,Integer.class);
		return paidAmount;
	}
/**
 * @description this method is used for loading the next level approval of current user_id
 */
	@Override
	public WorkOrderBean getWorkOrderFromAndToDetails(String userId, WorkOrderBean workOrderBean) {
		List<Map<String, Object>> dbIndentDts = null;
		String moduleType=workOrderBean.getTypeOfWork();
		if(moduleType==null||moduleType.length()==0){
			moduleType="WOB";
		}
		String query = "";//" select ((SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID =?)) as emp_name, SED.EMP_NAME as approver_name,SED.EMP_ID as approver_id FROM SUMADHURA_EMPLOYEE_DETAILS SED ,SUMADHURA_APPROVER_MAPPING_DTL  SAMD WHERE SED.EMP_ID = SAMD.APPROVER_EMP_ID and SAMD.EMP_ID = ? AND SAMD.MODULE_TYPE='WOB'  AND SAMD.SITE_ID = ? ";
		query="SELECT SAMD.APPROVER_EMP_ID, SAMD.SITE_ID FROM  SUMADHURA_APPROVER_MAPPING_DTL  SAMD WHERE SAMD.EMP_ID = ? AND  SAMD.SITE_ID=? AND SAMD.MODULE_TYPE=? AND SAMD.STATUS='A' ";
		Object[] obj = {  userId, workOrderBean.getSiteId(),moduleType};
		dbIndentDts = jdbcTemplate.queryForList(query, obj);
		for (Map<String, Object> prods : dbIndentDts) {
			log.info("Mapping Details :: " + prods);
			workOrderBean.setApproverEmpId(prods.get("APPROVER_EMP_ID") == null ? "" : prods.get("APPROVER_EMP_ID").toString());
		}
		return workOrderBean;
	}
	

	/**
	 * @param string 
	 * @throws Exception 
	 * @description this method will return the site account level emp id  
	 */
	@Override
	public String getWorkOrderPendingEmployeeId(String user_id, String site_id) throws Exception {
		String strApproverEmpId = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT APPROVER_EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL WHERE EMP_ID = ?  AND SITE_ID=? AND MODULE_TYPE='LABOURPAYMENT'";
		/*try {
			strApproverEmpId = jdbcTemplate.queryForObject(query, new Object[] { user_id,site_id }, String.class);
		} catch (Exception e) {
			throw new Exception("check site account mappings");
		}*/
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] { user_id ,site_id});
		for (Map<String, Object> prods : dbIndentDts) {
			strApproverEmpId = prods.get("APPROVER_EMP_ID") == null ? "" : prods.get("APPROVER_EMP_ID").toString();
		}
		
		return strApproverEmpId;
	}
	
	/**
	 * @description this method is for getting the creator and approver names and designation for bill signature
	 */
	@Override
	public List<Map<String, Object>> getTempBillVerifiedEmpNames(ContractorQSBillBean billBean) {
		//String siteId=billBean.getSiteId();
		String tempBillId=billBean.getTempBillNo();
		String billDate=billBean.getPaymentReqDate()+" 00:00:00";
		String date="03-04-2019 00:00:00";
		//String date="12-04-2019 00:00:00";
		int condition=compareDates(date,billDate);
		Object[] obj={tempBillId};
		String query="";
		//this query for getting max created date
		//SELECT max(to_char(QWCAD.CREATED_DATE,'dd-MM-yyyy hh:MM:ss AM')) as CREATED_DATE 		 FROM QS_CONT_TMP_BILL_APPR_REJ_DTLS  QWCAD 		 where  TEMP_BILL_ID = 206 order by QS_BILL_APPR_REJ_DTLS;
		
		//if bill is created before the date="06-03-2019 12:00:00" then you will get only three signature of bill
		//if bill is created after the date then you will get all the approvals of the bill signature
		if(condition==1){
			 query="SELECT upper(SED.EMP_NAME) as EMP_NAME,upper(SED.EMP_DESIGNATION) as EMP_DESIGNATION,QWCAD.STATUS,to_char(QWCAD.CREATED_DATE,'dd-MM-yyyy hh24:mi:ss') as CREATED_DATE "
					+ "FROM SUMADHURA_EMPLOYEE_DETAILS SED,QS_CONT_TMP_BILL_APPR_REJ_DTLS  QWCAD "
					+ "where  QWCAD.EMP_ID=SED.EMP_ID and TEMP_BILL_ID = '"+tempBillId+"'  order by QWCAD.CREATED_DATE  FETCH FIRST 3 ROWS ONLY";
		}else if(condition==2){
			 query="SELECT upper(SED.EMP_NAME) as EMP_NAME,upper(SED.EMP_DESIGNATION) as EMP_DESIGNATION,QWCAD.STATUS,to_char(QWCAD.CREATED_DATE,'dd-MM-yyyy hh24:mi:ss') as CREATED_DATE "
					+ "FROM SUMADHURA_EMPLOYEE_DETAILS SED,QS_CONT_TMP_BILL_APPR_REJ_DTLS  QWCAD "
					+ "where  QWCAD.EMP_ID=SED.EMP_ID and TEMP_BILL_ID = '"+tempBillId+"' order by QWCAD.CREATED_DATE";
		}else if(condition==0){
			 query="SELECT upper(SED.EMP_NAME) as EMP_NAME,upper(SED.EMP_DESIGNATION) as EMP_DESIGNATION,QWCAD.STATUS,to_char(QWCAD.CREATED_DATE,'dd-MM-yyyy hh24:mi:ss') as CREATED_DATE "
					+ "FROM SUMADHURA_EMPLOYEE_DETAILS SED,QS_CONT_TMP_BILL_APPR_REJ_DTLS  QWCAD "
					+ "where  QWCAD.EMP_ID=SED.EMP_ID and TEMP_BILL_ID = '"+tempBillId+"' order by QWCAD.CREATED_DATE";
		}
	
		
		List<Map<String, Object>> list=jdbcTemplate.queryForList(query);
		return list;
	}
	
	/**
	 * @description this method is for checking date<br>
	 * which date is greater or lesser, based the condition output we have perform the query's
	 * @param d1
	 * @param d2
	 * @return
	 */
    public static int compareDates(String d1,String d2)
    {
        try{
            // If you already have date objects then skip 1

            //1
            // Create 2 dates starts
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            System.out.println("Date1 "+sdf.format(date1));
            System.out.println("Date2 "+sdf.format(date2));System.out.println();

            // Create 2 dates ends
            //1

            // Date object is having 3 methods namely after,before and equals for comparing
            // after() will return true if and only if date1 is after date 2
            if(date1.after(date2)){
              return 1;
            }
            // before() will return true if and only if date1 is before date2
            if(date1.before(date2)){
            	return 2;
            }

            //equals() returns true if both the dates are equal
            if(date1.equals(date2)){
                return 0;
            }

            System.out.println();
        }
        catch(ParseException ex){
            ex.printStackTrace();
        }
		return 0;
    }
	
	
	
	/**
	 * this method will load the deduction types
	 */
	@Override
	public List<Map<String, Object>> loadDeductionTypes(String data) {
		List<Map<String, Object>> deductionList = null;
		String query="select DEDUCTION_ID,DEDUCTION_TYPE from QS_BILL_DEDUCTIONS where DEDUCTION_ID not in ("+data+")";
		deductionList = jdbcTemplate.queryForList(query);
		return deductionList;
	}
	/**
	 * @description this method will return the details  of recovery of bill
	 * with the previous bill details and current bill details
	 */
	@Override
	public List<Map<String, Object>> loadPermanentRecoveryAreaDetails(String[] data) {
		List<Map<String, Object>> recoveryAmount = null;
		String siteId=data[2];
		String workOrderNo=data[1];
		String contractorId = data[0];
		//String approvePage=data[3];
		String tempBillNo=data[4];
		String billNo=data[5];
		
		String query="";
		query=" select (select sum(QBR.RECOVERY_AMOUNT) from  QS_BILL_RECOVERY_HISTORY QBR,QS_CONTRACTOR_BILL QBL  where QBL.BILL_ID=QBR.BILL_ID AND QBL.QS_WORKORDER_ISSUE_ID=QBR.QS_WORKORDER_ISSUE_ID AND QBR.CHILD_PRODUCT_ID=SPL.CHILD_PRODUCT_ID "
			+ " AND SPL.PRICE_ID=IED.PRICE_ID and  QBL.TEMP_BILL_ID<'"+tempBillNo+"'    AND  QBR.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+workOrderNo+"' and SITE_ID='"+siteId+"') group by   QBR.CHILD_PRODUCT_ID ) as RECOVERY_AMOUNT, 	"
			+ " (select QTBR.RECOVERY_AMOUNT  from QS_BILL_RECOVERY_HISTORY QTBR WHERE  QTBR.BILL_ID='"+billNo+"'  and  QTBR.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+workOrderNo+"' AND SITE_ID='"+siteId+"' ) and  QTBR.CHILD_PRODUCT_ID=SPL.CHILD_PRODUCT_ID  ) CURRENTRECOVERY," 				
			+ " CP.CHILD_PRODUCT_ID,CP.NAME child_prod_name ,IED.ISSUED_QTY,SPL.PRICE_ID,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,ROUND(NVL((SPL.AMOUNT_PER_UNIT_AFTER_TAXES*IED.ISSUED_QTY) ,0),2)AS TOTAL_AMOUNT,nvl(SPL.TAX,0) as TAX, "
			+ " SPL.UNITS_OF_MEASUREMENT,UOM.NAME mesurment_name,IED.INDENT_ENTRY_ID,SPL.PRICE_ID "
			+ " from INDENT_ENTRY_DETAILS IED, SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY IE ,CHILD_PRODUCT CP,MEASUREMENT UOM " 
			+ " where  UOM.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT  AND CP.CHILD_PRODUCT_ID=SPL.CHILD_PRODUCT_ID "
			+ " AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID  AND SPL.PRICE_ID=IED.PRICE_ID AND IED.IS_RECOVERABLE='Yes' "
			+ " AND IE.CONTRACTOR_ID=? AND IE.WORK_ORDER_NUMBER=? and  IE.SITE_ID='"+siteId+"' order by CP.NAME";
		Object[] params={contractorId,workOrderNo};
		//selecting data using site id and work order number and contractor id
		recoveryAmount = jdbcTemplate.queryForList(query,params);
		return recoveryAmount;
	}
	
	/**
	 * @description this method used to load the recovery details of the bill
	 * and it will load the previous recovered amount and current amount if exist
	 */
	@Override
	public List<Map<String, Object>> loadRecoveryAreaDetails(String[] data) {
		List<Map<String, Object>> recoveryAmount = null;
		String siteId=data[2];
		String workOrderNo=data[1];
		String contractorId = data[0];
		String approvePage=data[3];
		String tempBillNo=data[4];
		String query="";
		//if this is approve page then load the data in approval level using temporary bill id
		if(!approvePage.equals("false")){
			query="select (select sum(QBR.RECOVERY_AMOUNT) from  QS_BILL_RECOVERY_HISTORY QBR,QS_CONTRACTOR_BILL QBL  where QBL.BILL_ID=QBR.BILL_ID AND QBL.QS_WORKORDER_ISSUE_ID=QBR.QS_WORKORDER_ISSUE_ID AND QBR.CHILD_PRODUCT_ID=SPL.CHILD_PRODUCT_ID "
					+ "  AND SPL.PRICE_ID=IED.PRICE_ID and  QBL.TEMP_BILL_ID<'"+tempBillNo+"'    AND  QBR.QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = '"+workOrderNo+"' and SITE_ID='"+siteId+"') group by   QBR.CHILD_PRODUCT_ID ) as RECOVERY_AMOUNT, 	"
					+ "(select QTBR.RECOVERY_AMOUNT  from QS_TEMP_BILL_RECOVERY QTBR WHERE TEMP_BILL_ID='"+tempBillNo+"' and  QTBR.CHILD_PRODUCT_ID=SPL.CHILD_PRODUCT_ID  ) CURRENTRECOVERY," 				
					+ " CP.CHILD_PRODUCT_ID,CP.NAME child_prod_name ,IED.ISSUED_QTY,SPL.PRICE_ID,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,ROUND(NVL((SPL.AMOUNT_PER_UNIT_AFTER_TAXES*IED.ISSUED_QTY) ,0),2)AS TOTAL_AMOUNT,nvl(SPL.TAX,0) as TAX, "
					+ " SPL.UNITS_OF_MEASUREMENT,UOM.NAME mesurment_name,IED.INDENT_ENTRY_ID,SPL.PRICE_ID "
					+ " from INDENT_ENTRY_DETAILS IED, SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY IE ,CHILD_PRODUCT CP,MEASUREMENT UOM " 
					+ " where  UOM.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT  AND CP.CHILD_PRODUCT_ID=SPL.CHILD_PRODUCT_ID "
					+ " AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID  AND SPL.PRICE_ID=IED.PRICE_ID AND IED.IS_RECOVERABLE='Yes' "
					+ " AND IE.CONTRACTOR_ID=? AND IE.WORK_ORDER_NUMBER = ? and  IE.SITE_ID='"+siteId+"' order by CP.NAME";
			Object[] params={contractorId,workOrderNo};
			recoveryAmount = jdbcTemplate.queryForList(query,params);
		}else{
			//this condition will execute if this request came from create bill 
			//this query will return the previous recoverd amount
			query="select (select sum(QBR.RECOVERY_AMOUNT)  from  QS_BILL_RECOVERY_HISTORY QBR  where  QBR.CHILD_PRODUCT_ID=SPL.CHILD_PRODUCT_ID "
					+ "AND SPL.PRICE_ID=IED.PRICE_ID and  QS_WORKORDER_ISSUE_ID in (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER = '"+workOrderNo+"'  and SITE_ID='"+siteId+"')  group by   QBR.CHILD_PRODUCT_ID ) as RECOVERY_AMOUNT, " 
					+ " CP.CHILD_PRODUCT_ID,CP.NAME child_prod_name ,IED.ISSUED_QTY,SPL.PRICE_ID,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,ROUND(NVL((SPL.AMOUNT_PER_UNIT_AFTER_TAXES*IED.ISSUED_QTY) ,0),2)AS TOTAL_AMOUNT,nvl(SPL.TAX,0) as TAX, "
					+ " SPL.UNITS_OF_MEASUREMENT,UOM.NAME mesurment_name,IED.INDENT_ENTRY_ID,SPL.PRICE_ID "
					+ " from INDENT_ENTRY_DETAILS IED, SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY IE ,CHILD_PRODUCT CP,MEASUREMENT UOM " 
					+ " where  UOM.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT  AND CP.CHILD_PRODUCT_ID=SPL.CHILD_PRODUCT_ID "
					+ " AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID  AND SPL.PRICE_ID=IED.PRICE_ID AND IED.IS_RECOVERABLE='Yes' "
					+ " AND IE.CONTRACTOR_ID=? AND IE.WORK_ORDER_NUMBER = ? and  IE.SITE_ID='"+siteId+"' order by CP.NAME";
			//selecting data using contractor id and work order number
			Object[] params={contractorId,workOrderNo};
			recoveryAmount = jdbcTemplate.queryForList(query,params);
		}
			return recoveryAmount;
	}
	
	/**
	 * @description this method inserting the bill recovery amount in temporary table 
	 */
	@Override
	public int insertIntoBoqRecovery(ContractorQSBillBean billBean) {
/*	String queryForCheckingRecord="SELECT COUNT(*) FROM QS_TEMP_BILL_RECOVERY WHERE CHILD_PRODUCT_ID=? AND QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? and SITE_id='"+billBean.getSiteId()+"')";
	Object[] queryParams1={billBean.getChild_product_id(),billBean.getWorkOrderNo()};
	int count=jdbcTemplate.queryForObject(queryForCheckingRecord,queryParams1, Integer.class);
		if(count==0){*/
			String insertQuery="INSERT INTO QS_TEMP_BILL_RECOVERY(CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,QS_WORKORDER_ISSUE_ID,TEMP_BILL_ID) "
					+ "VALUES(?,?,?,?,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+billBean.getWorkOrderNo()+"' and SITE_id='"+billBean.getSiteId()+"'),?)";
			Object[] queryParams2={billBean.getChild_product_id(),billBean.getMeasurement_id(),billBean.getRecovery_quantity(),billBean.getRecovery_amount(),billBean.getTempBillNo()};
		int	count=jdbcTemplate.update(insertQuery,queryParams2);
	/*	}else{
			String updateQuery="UPDATE QS_TEMP_BILL_RECOVERY SET RECOVERY_AMOUNT=RECOVERY_AMOUNT+?  WHERE CHILD_PRODUCT_ID=?  AND QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? and SITE_id='"+billBean.getSiteId()+"')";
			count=jdbcTemplate.update(updateQuery,billBean.getRecovery_amount(),billBean.getChild_product_id(),billBean.getWorkOrderNo());
		}*/
		return count;
	}
	
	/**
	 *@description this method is used for inserting the recovery amount of the bill 
	 */
	@Override
	public int insertIntoBoqRecoveryHistory(ContractorQSBillBean billBean) {
		String query="insert into QS_TEMP_BILL_RECOVERY_HISTORY( CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,QS_WORKORDER_ISSUE_ID,TEMP_BILL_ID) values(?,?,?,?,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+billBean.getWorkOrderNo()+"' and SITE_id='"+billBean.getSiteId()+"'),?)";
		Object[] queryParams={billBean.getChild_product_id(),billBean.getMeasurement_id(),billBean.getRecovery_quantity(),billBean.getRecovery_amount(),billBean.getTempBillNo()};
		int count=jdbcTemplate.update(query,queryParams);
		return count;
	}
	
	/**
	 * @description this method is used for deleting the temporary details of recovery
	 */
	@Override
	public int deleteFromBoqRecoveryHistory(ContractorQSBillBean billBean) {
		String query="delete from QS_TEMP_BILL_RECOVERY_HISTORY where TEMP_BILL_ID=?";
		Object[] queryParams={billBean.getTempBillNo()};
		int count=jdbcTemplate.update(query,queryParams);
		return count;
	}
	/**
	 * @description this method is for descreasing the recovery amount, if the amount is removec
	 */
	@Override
	public int removeAmountFromBoqRecovery(ContractorQSBillBean billBean) {
		String updateQuery="UPDATE QS_TEMP_BILL_RECOVERY SET RECOVERY_AMOUNT=RECOVERY_AMOUNT-?  WHERE CHILD_PRODUCT_ID=? AND QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+billBean.getWorkOrderNo()+"' and SITE_id='"+billBean.getSiteId()+"')";
		Object[] params={billBean.getRecovery_amount(),billBean.getChild_product_id()};
		int count=jdbcTemplate.update(updateQuery,params);
		return count;
	}
	
	@Override
	public int updateBoqRecovery(ContractorQSBillBean billBean, int condition) {
		int count=0;
		//if the condition equal to 1 we need to increase recovery amount
		if(condition==1){
			String updateQuery="UPDATE QS_TEMP_BILL_RECOVERY SET RECOVERY_AMOUNT=RECOVERY_AMOUNT+?  WHERE TEMP_BILL_ID=? and  CHILD_PRODUCT_ID=?";
			Object[] obj={billBean.getRecovery_amount(),billBean.getTempBillNo(),billBean.getChild_product_id()};
			count=jdbcTemplate.update(updateQuery,obj);
		}else if(condition==2){
			//if the condition equal to 2 we need to decrease recovery amount
			String updateQuery="UPDATE QS_TEMP_BILL_RECOVERY SET RECOVERY_AMOUNT=RECOVERY_AMOUNT-?  WHERE  TEMP_BILL_ID=? and  CHILD_PRODUCT_ID=?";
			Object[] obj={billBean.getRecovery_amount(),billBean.getTempBillNo(),billBean.getChild_product_id()};
			count=jdbcTemplate.update(updateQuery,obj);
		}
		//if the count equal to 0 then we need to check is this record is exists in table ot not if not then we need to insert the record
		if(count==0){
			String updateQuery="SELECT COUNT(*) FROM  QS_TEMP_BILL_RECOVERY  WHERE TEMP_BILL_ID=?";
			updateQuery="SELECT COUNT(*) FROM  QS_TEMP_BILL_RECOVERY  WHERE TEMP_BILL_ID='"+billBean.getTempBillNo()+"'  and CHILD_PRODUCT_ID='"+billBean.getChild_product_id()+"'";
			count=jdbcTemplate.queryForObject(updateQuery,Integer.class);
			if(count==0){
			String insertQuery="INSERT INTO QS_TEMP_BILL_RECOVERY(CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,QS_WORKORDER_ISSUE_ID,TEMP_BILL_ID) "
					+ "VALUES(?,?,?,?,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+billBean.getWorkOrderNo()+"' and SITE_id='"+billBean.getSiteId()+"'),?)";
			Object[] queryParams2={billBean.getChild_product_id(),billBean.getMeasurement_id(),billBean.getRecovery_quantity(),billBean.getRecovery_amount1(),billBean.getTempBillNo()};
			count=jdbcTemplate.update(insertQuery,queryParams2);
			}
	
		}
		return count;
	}
	/**
	 * @description this 
	 */
	@Override
	public int updateBoqRecoveryHistory(ContractorQSBillBean billBean, int condition) {
		int count=0;
		
		//if the condition equal to 1 we need to increase recovery amount
		if(condition==1){
			String updateQuery="UPDATE QS_TEMP_BILL_RECOVERY_HISTORY SET RECOVERY_AMOUNT=RECOVERY_AMOUNT+?  WHERE TEMP_BILL_ID=?  and CHILD_PRODUCT_ID=?";
			Object[] obj={billBean.getRecovery_amount(),billBean.getTempBillNo(),billBean.getChild_product_id()};
			count=jdbcTemplate.update(updateQuery,obj);
		}else if(condition==2){
			//if the condition equal to 2 we need to decrease recovery amount
			String updateQuery="UPDATE QS_TEMP_BILL_RECOVERY_HISTORY SET RECOVERY_AMOUNT=RECOVERY_AMOUNT-?  WHERE TEMP_BILL_ID=? and CHILD_PRODUCT_ID=?";
			Object[] obj={billBean.getRecovery_amount(),billBean.getTempBillNo(),billBean.getChild_product_id()};
			count=jdbcTemplate.update(updateQuery,obj);
		}
		//if the count equal to 0 then we need to check is this record is exists in table ot not if not then we need to insert the record
		if(count==0){
			String updateQuery="SELECT COUNT(*) FROM  QS_TEMP_BILL_RECOVERY_HISTORY  WHERE TEMP_BILL_ID='"+billBean.getTempBillNo()+"'  and CHILD_PRODUCT_ID='"+billBean.getChild_product_id()+"'";
			count=jdbcTemplate.queryForObject(updateQuery,Integer.class);
			if(count==0){
			String query="insert into QS_TEMP_BILL_RECOVERY_HISTORY(CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,QS_WORKORDER_ISSUE_ID,TEMP_BILL_ID) values(?,?,?,?,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+billBean.getWorkOrderNo()+"' and SITE_id='"+billBean.getSiteId()+"'),?)";
			Object[] queryParams={billBean.getChild_product_id(),billBean.getMeasurement_id(),billBean.getRecovery_quantity(),billBean.getRecovery_amount1(),billBean.getTempBillNo()};
			count=jdbcTemplate.update(query,queryParams);
		}
		}
		return count;
	}
	
	/**
	 * this method is checking is the user manually entered bill no is already exist in tables or not
	 * if exist then show the error message
	 */
	@Override
	public String checkBillNoExistsOrNot(ContractorQSBillBean billBean) {
		String billNo=billBean.getBillNo();
		//checking is this bill number in temporary tables
		String query="select count(*) FROM QS_CONTRACTOR_BILL_TEMP WHERE STATUS='A' AND PERMANANT_BILL_NUMBER='"+billNo+"' and QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+billBean.getWorkOrderNo()+"' and SITE_ID='"+billBean.getSiteId()+"')";
		int count=jdbcTemplate.queryForObject(query, Integer.class);
		if(count==0){
			//if this bill number not exist in temporary tables then check in permanent tables
			 query="select count(*) FROM QS_CONTRACTOR_BILL WHERE  BILL_ID='"+billNo+"' and QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+billBean.getWorkOrderNo()+"' and SITE_ID='"+billBean.getSiteId()+"')";
			 count=jdbcTemplate.queryForObject(query, Integer.class);
			 if(count==0){
					return "false";		
				}else{
					return "true";
				}
		}else{
			return "true";
		}
	}
	
	/**
	 * @description bill invoice number is generated on contractor id and site id
	 * so here we are validating this invoice bill number already exists or not if exists throw error 
	 */
	@Override
	public String checkInvoiceBillNoExistsOrNot(ContractorQSBillBean billBean) {
		String billNo=billBean.getBillNo();
		//checking is this bill number in temporary tables
		String query="select count(*) FROM QS_CONTRACTOR_BILL_TEMP WHERE STATUS='A' AND BILL_INVOICE_NUMBER=? AND CONTRACTOR_ID=? AND SITE_ID=?";
		int count=jdbcTemplate.queryForObject(query, new Object[]{billNo,billBean.getContractorId(),billBean.getSiteId()}, Integer.class);
		if(count==0){
			//if this bill number not exist in temporary tables then check in permanent tables
			 query="select count(*) FROM QS_CONTRACTOR_BILL WHERE  BILL_INVOICE_NUMBER=?  AND CONTRACTOR_ID=? AND SITE_ID=?";
			 count=jdbcTemplate.queryForObject(query,new Object[]{billNo,billBean.getContractorId(),billBean.getSiteId()}, Integer.class);
			 if(count==0){
					return "false";		
				}else{
					return "true";
				}
		}else{
			return "true";
		}
	}
	 /* this method will generate the permanent bill number and will return to the service layer from there it will goto controller and then view page
	 */
	@Override
	public String loadAdvRAPermanentBillNo(ContractorQSBillBean billBean,String siteNameInitials) {
		int num=0;
		String contractorBillSeq="";
		String contractorBillGSTNumSeq="";
		String contractorId=billBean.getContractorId();
		String contractorGSTNumber=billBean.getGSTIN();
		StringBuffer query=null;
		String workOrderNo=billBean.getWorkOrderNo();
		 		
		query=new StringBuffer("  SELECT  MAX(NVL(TO_NUMBER(REGEXP_SUBSTR (BILL_ID, '[^/]+', 1, 2)),0))+1    AS BILL_ID ")
				.append(" FROM QS_CONTRACTOR_BILL  WHERE  QS_WORKORDER_ISSUE_ID =  ")
				.append(" (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER =?  AND SITE_ID=?) AND PAYMENT_TYPE=? ");	
		try {
			Object[] obj={workOrderNo,billBean.getSiteId(),billBean.getPaymentType()};
			contractorBillSeq=jdbcTemplate.queryForObject(query.toString(),obj,String.class);	
			//if the contractorBillSeq is null then put default value 1 
			//if the bill creating first time then only we will get the null output from query
			if(contractorBillSeq==null){
				contractorBillSeq="1";	
			}
			if((contractorGSTNumber!=null&&contractorGSTNumber.equals("-"))||contractorGSTNumber.equals("")){
				query=new StringBuffer("SELECT COUNT(*)+1 AS BILL_ID  FROM QS_CONTRACTOR_BILL_TEMP WHERE CONTRACTOR_ID = ? AND SITE_ID = ? AND PAYMENT_TYPE = ? AND STATUS IN ('A','I')");
						contractorBillGSTNumSeq=jdbcTemplate.queryForObject(query.toString(), new Object[]{contractorId,billBean.getSiteId(),billBean.getPaymentType()},String.class);
			}else{
				query=new StringBuffer("SELECT COUNT(*)+1 AS BILL_ID  FROM QS_CONTRACTOR_BILL_TEMP WHERE CONTRACTOR_ID = ? AND SITE_ID = ?  AND STATUS IN ('A','I')");
				contractorBillGSTNumSeq=jdbcTemplate.queryForObject(query.toString(), new Object[]{contractorId,billBean.getSiteId()},String.class);
			}
			
			if(contractorBillGSTNumSeq==null){
				contractorBillGSTNumSeq="1";	
			}
			num=Integer.valueOf(contractorBillGSTNumSeq);
			//generating invoice number for non GST contractor
			if((contractorGSTNumber!=null&&contractorGSTNumber.equals("-"))||contractorGSTNumber.equals("")){
				contractorBillGSTNumSeq=siteNameInitials+"/" +billBean.getPaymentType()+"/" + String.format("%03d", num);
			}else{
				//generating invoice number for GST contractor
				contractorBillGSTNumSeq= siteNameInitials+"/" + String.format("%03d", num);
			}
			//jdbcTemplate.queryForObject("SELECT    EXTRACT (YEAR FROM ADD_MONTHS (DATE '2018-03-31', -3)) || '-' || EXTRACT (YEAR FROM ADD_MONTHS (DATE '2019-03-31', 9)) FROM DUAL;", String.class);
			String financialYears=jdbcTemplate.queryForObject("SELECT EXTRACT (YEAR FROM ADD_MONTHS (SYSDATE, -3))|| '-'|| EXTRACT (YEAR FROM ADD_MONTHS (SYSDATE, 9)) FROM DUAL", String.class);
			String financialStartYear=financialYears.split("-")[0];
			String financialEndYear=financialYears.split("-")[1];
			//check invoice number in permanent table
			int count=jdbcTemplate.queryForObject("SELECT COUNT(*) FROM QS_CONTRACTOR_BILL WHERE CONTRACTOR_ID =? AND SITE_ID=?  AND BILL_INVOICE_NUMBER=?  AND TO_DATE(PAYMENT_REQ_DATE) BETWEEN TO_DATE('01-04-"+financialStartYear+"','dd-MM-yyyy') AND TO_DATE('31-03-"+financialEndYear+"','dd-MM-yyyy')", new Object[]{contractorId,billBean.getSiteId(),contractorBillGSTNumSeq}, Integer.class);
			if(count==0){//if count is zero then check invoice number in temporary table
				count=jdbcTemplate.queryForObject("SELECT COUNT(*) FROM QS_CONTRACTOR_BILL_TEMP WHERE STATUS in ('A','I') AND CONTRACTOR_ID =? AND SITE_ID=?  AND BILL_INVOICE_NUMBER=?  AND TO_DATE(PAYMENT_REQ_DATE) BETWEEN TO_DATE('01-04-"+financialStartYear+"','dd-MM-yyyy') AND TO_DATE('31-03-"+financialEndYear+"','dd-MM-yyyy')", new Object[]{contractorId,billBean.getSiteId(),contractorBillGSTNumSeq}, Integer.class);
				if(count==0){
					
				}else{
					contractorBillGSTNumSeq="";
				}
			}else{
				contractorBillGSTNumSeq="";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		 num=Integer.valueOf(contractorBillSeq);
		//adding one digit before the number eg. (1) then (01) so the bill no is like ADV/01, RA/01,NMR/01,ADV/02........
		contractorBillSeq = billBean.getPaymentType()+"/" + String.format("%02d", num);
		
		return contractorBillSeq+"_"+contractorBillGSTNumSeq;
	}
	
	/**
	 * @description this method will load the data by using work order number
	 */
	@Override
	public List<Map<String, Object>> loadNMRBillData(ContractorQSBillBean billBean) {		
	String queryforNMRData="select (select LISTAGG(concat(QWP.ALLOCATED_QTY,'&&'||QWP.NO_OF_HOURS||'&&'||QWP.RATE),'@@')  WITHIN GROUP (ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID)  from QS_INV_AGN_WORK_PMT_DTL_HR QWP,QS_CONTRACTOR_BILL QCB where QCB.BILL_ID=QWP.BILL_ID AND QCB.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID AND QWP.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID  AND QWP.WO_WORK_DESC_ID=QWD.WO_WORK_DESC_ID      )as PrevQty," // AND QWP.WO_MINORHEAD_ID=QWD.WO_MINORHEAD_ID   AND QWP.WO_ROW_CODE=QWD.WO_ROW_CODE 
			+ "(QWD.TOTAL_AMOUNT/QWD.QUANTITY) AS RATE,QWD.WO_ROW_CODE,QWD.QUANTITY,QWI.CONTRACTOR_ID,QWI.SITE_ID,QWI.QS_WORKORDER_ISSUE_ID ,QWI.TYPE_OF_WORK ,QWD.WO_WORK_ISSUE_DTLS_ID ,QWD.WO_WORK_DESC_ID ,QWD.TOTAL_AMOUNT,QWD.WO_COMMENT  , MAJOR.WO_MAJORHEAD_ID,MAJOR.WO_MAJORHEAD_DESC,MINOR.WO_MINORHEAD_ID,MINOR.WO_MINORHEAD_DESC  ,WORKDESC.WO_WORK_DESCRIPTION,WORKDESC.WO_WORK_DESC_ID,  MESUR.WO_MEASURMENT_ID,MESUR.WO_MEASURMEN_NAME"
			+ " from QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_DETAILS QWD   ,QS_WO_WORKDESC WORKDESC,QS_WO_MEASURMENT MESUR,QS_WO_MINORHEAD MINOR,QS_WO_MAJORHEAD MAJOR "
			+ "  WHERE    WORKDESC.WO_MINORHEAD_ID=MINOR.WO_MINORHEAD_ID  and QWD.WO_MINORHEAD_ID=MINOR.WO_MINORHEAD_ID  AND MINOR.WO_MAJORHEAD_ID=MAJOR.WO_MAJORHEAD_ID    AND MESUR.WO_WORK_DESC_ID=WORKDESC.WO_WORK_DESC_ID AND QWD.WO_WORK_DESC_ID=WORKDESC.WO_WORK_DESC_ID AND  QWI.WORK_ORDER_NUMBER='"+billBean.getWorkOrderNo()+"' AND QWI.SITE_ID='"+billBean.getSiteId()+"' AND QWI.TYPE_OF_WORK='"+billBean.getTypeOfWork()+"' AND QWI.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID order by WORKDESC.WO_WORK_DESCRIPTION ";
	
	queryforNMRData="select (select RTRIM(XMLAGG(XMLELEMENT(E,concat(QWP.ALLOCATED_QTY,'##'||QWP.NO_OF_HOURS||'##'||QWP.RATE),'@@').EXTRACT('//text()') ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID).GetClobVal(),'@@') "
			+ "from QS_INV_AGN_WORK_PMT_DTL_HR QWP,QS_CONTRACTOR_BILL QCB where  QWP.ALLOCATED_QTY!=0 and   QCB.BILL_ID=QWP.BILL_ID  AND QCB.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID AND QWP.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID  AND QWP.WO_WORK_DESC_ID=QWD.WO_WORK_DESC_ID      )as PrevQty,"
			+ "(QWD.TOTAL_AMOUNT/QWD.QUANTITY) AS RATE,QWD.WO_ROW_CODE,QWD.QUANTITY,QWI.CONTRACTOR_ID,QWI.SITE_ID,QWI.QS_WORKORDER_ISSUE_ID ,QWI.TYPE_OF_WORK ,QWD.WO_WORK_ISSUE_DTLS_ID ,QWD.WO_WORK_DESC_ID ,QWD.TOTAL_AMOUNT,QWD.WO_COMMENT  , MAJOR.WO_MAJORHEAD_ID,MAJOR.WO_MAJORHEAD_DESC,MINOR.WO_MINORHEAD_ID,MINOR.WO_MINORHEAD_DESC  ,WORKDESC.WO_WORK_DESCRIPTION,WORKDESC.WO_WORK_DESC_ID,  MESUR.WO_MEASURMENT_ID,MESUR.WO_MEASURMEN_NAME "
			+ "from QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_DETAILS QWD   ,QS_WO_WORKDESC WORKDESC,QS_WO_MEASURMENT MESUR,QS_WO_MINORHEAD MINOR,QS_WO_MAJORHEAD MAJOR "
			+ "WHERE    WORKDESC.WO_MINORHEAD_ID=MINOR.WO_MINORHEAD_ID  and MESUR.WO_MEASURMENT_ID=QWD.UOM  and QWD.WO_MINORHEAD_ID=MINOR.WO_MINORHEAD_ID  AND MINOR.WO_MAJORHEAD_ID=MAJOR.WO_MAJORHEAD_ID    AND MESUR.WO_WORK_DESC_ID=WORKDESC.WO_WORK_DESC_ID AND QWD.WO_WORK_DESC_ID=WORKDESC.WO_WORK_DESC_ID AND  QWI.WORK_ORDER_NUMBER='"+billBean.getWorkOrderNo()+"' AND QWI.SITE_ID='"+billBean.getSiteId()+"' AND QWI.TYPE_OF_WORK='"+billBean.getTypeOfWork()+"' "
			+ "AND QWI.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID order by WORKDESC.WO_WORK_DESCRIPTION ";
	
	List<Map<String, Object>> list=jdbcTemplate.queryForList(queryforNMRData);	
		return list;
	}
	/**
	 * @description loading all the completed bill data by using work order number
	 */
	@Override
	public List<Map<String, Object>> loadPermanentNMRBillDetailsData(ContractorQSBillBean billBean) {
		String billNo=billBean.getBillNo();
		String queryforNMRData="select QCBT.BILL_ID ,QCBT.CONTRACTOR_ID ,QCBT.SITE_ID ,QCBT.PAYBLE_AMOUNT ,QCBT.STATUS , "
		+ " (SELECT LISTAGG(TYPE_OF_DEDUCTION, '@@') WITHIN GROUP (ORDER BY TYPE_OF_DEDUCTION) FROM QS_BILL_DEDUCTION_DTLS WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID and QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID ";
		if(billBean.getIsApprovePage().equals("true"))
		queryforNMRData+= " and QCBT.BILL_ID<='"+billNo+"' ";

		queryforNMRData+= " ) as TYPE_OF_DEDUCTION,";
		queryforNMRData+= " (SELECT LISTAGG(DEDUCTION_AMOUNT, '@@') WITHIN GROUP (ORDER BY TYPE_OF_DEDUCTION) FROM QS_BILL_DEDUCTION_DTLS WHERE CONTRACTOR_BILL_ID =QCBT.BILL_ID and QS_WORKORDER_ISSUE_ID=QCBT.QS_WORKORDER_ISSUE_ID ";
		if(billBean.getIsApprovePage().equals("true"))
		queryforNMRData+= " and QCBT.BILL_ID<='"+billNo+"' ";

		queryforNMRData+= " ) as DEDUCTION_AMOUNT1, "

		+ " QCBT.PENDING_EMP_ID ,QCBT.PENDING_DEPT_ID ,to_char(QCBT.PAYMENT_REQ_DATE ,'dd-MM-yyyy') PAYMENT_REQ_DATE ,to_char(QCBT.ENTRY_DATE,'dd-MM-yyyy') as ENTRY_DATE, "
		+ " QCBT.PAYMENT_TYPE ,QCBT.CERTIFIED_AMOUNT ,QCBT.DEDUCTION_AMOUNT ,QCBT.TEMP_BILL_ID , "
		+ " QCBT.QS_WORKORDER_ISSUE_ID ,QCBT.PAYMENT_STATUS ,QCBT.CREATED_BY ,QCBT.DEDUCTION_REF_ID , "
		+ " QCBT.REMARKS ,QCBT.BILL_NO ,QCBT.CONT_SEQ_BILL_NO ,QCBT.PAYMENT_TYPE_OF_WORK ,QCBT.WORK_BLOCK_NAMES ";
		queryforNMRData+= " from QS_CONTRACTOR_BILL QCBT WHERE QCBT.PAYMENT_TYPE='"+billBean.getTypeOfWork()+"' ";
		if(billBean.getIsApprovePage().equals("true"))
			queryforNMRData+= " and QCBT.BILL_ID<='"+billNo+"' ";
		queryforNMRData+= " and QCBT.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+billBean.getWorkOrderNo()+"' AND SITE_ID='"+billBean.getSiteId()+"' ) AND QCBT.SITE_ID='"+billBean.getSiteId()+"' ORDER BY CONT_SEQ_BILL_NO ";	
		List<Map<String, Object>> list=jdbcTemplate.queryForList(queryforNMRData);	
		return list;}
	
	/**
	 * @description this method used for load data in approval level's, 
	 * the NMR details will be loaded using temporary id
	 */
	@Override
	public List<Map<String, Object>> loadNMRBillDataForApproval(ContractorQSBillBean billBean) {
		String query="";
				//checking condition based on the condition changing the query
				//if condition is equal to "nmrDetailsPrint" then select the NMR details sorting by date wise
				//else select data by sorting minor head and work description name
		if(billBean.getCondition().equals("nmrDetailsPrint")){
		query=" select (select  RTRIM(XMLAGG(XMLELEMENT(E,concat(QWP.ALLOCATED_QTY,'##'||QWP.NO_OF_HOURS||'##'||QWP.RATE),'@@').EXTRACT('//text()') ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID).GetClobVal(),'@@')  from QS_INV_AGN_WORK_PMT_DTL_HR QWP,QS_CONTRACTOR_BILL QCB where   QWP.ALLOCATED_QTY!=0 and  QCB.BILL_ID=QWP.BILL_ID AND QCB.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID AND QWP.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID  AND QWP.WO_WORK_DESC_ID=QWD.WO_WORK_DESC_ID    AND QWP.WO_MINORHEAD_ID=QWD.WO_MINORHEAD_ID    AND QWP.WO_ROW_CODE=QWD.WO_ROW_CODE  )as PrevQty, QWW.WO_WORK_DESC_ID,QTWP.WO_ROW_CODE,QTWP.NMRROWNUM ,QWD.QUANTITY,QTWP.ALLOCATED_QTY , "
				+ "	TO_CHAR(QTWP.WORK_DATE  ,'dd-MM-yyyy') as WORK_DATE,QTWP.REMARKS,QCBT.TEMP_BILL_ID ,QCBT.PAYBLE_AMOUNT ,QCBT.PAYMENT_REQ_DATE ,QCBT.CERTIFIED_AMOUNT ,QCBT.QS_WORKORDER_ISSUE_ID QS_WORKORDER_ISSUE_ID, "
				+ " QTWP.QS_INV_AGN_WORK_PMT_DTL_ID ,(QWD.TOTAL_AMOUNT/QWD.QUANTITY) as RATE,QTWP.AMOUNT AMOUNT,QTWP.WORK_DAYS ,QTWP.FROM_TIME , "
				+ " QTWP.NO_OF_HOURS ,QTWP.TO_TIME ,QTWP.MANUAL_DESC ,QWH.WO_MAJORHEAD_ID ,QWH.WO_MAJORHEAD_DESC ,QWMT.WO_MEASURMENT_ID ,QWMT.WO_MEASURMEN_NAME , "
				+ " QWMH.WO_MINORHEAD_ID ,QWMH.WO_MINORHEAD_DESC ,QWW.WO_WORK_DESCRIPTION "
				+ " from QS_CONTRACTOR_BILL_TEMP QCBT,QS_WORKORDER_ISSUE_DETAILS QWD,QS_TEMP_INV_AGN_WORK_PMT_DTL_HR QTWP,QS_WO_MAJORHEAD QWH,QS_WO_MEASURMENT QWMT, "
				+ " QS_WO_MINORHEAD QWMH ,QS_WO_WORKDESC QWW "
				+ " WHERE   QWD.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID  and QWMT.WO_MEASURMENT_ID=QWD.UOM and QWD.WO_ROW_CODE=QTWP.WO_ROW_CODE  AND QWD.WO_MINORHEAD_ID=QWMH.WO_MINORHEAD_ID AND QCBT.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID and QTWP.TEMP_BILL_ID=QCBT.TEMP_BILL_ID AND  QTWP.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID AND QWMH.WO_MINORHEAD_ID=QTWP.WO_MINORHEAD_ID AND "
				+ " QWW.WO_WORK_DESC_ID=QWMT.WO_WORK_DESC_ID AND QWMH.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID AND QWMH.WO_MAJORHEAD_ID=QWH.WO_MAJORHEAD_ID AND QWH.TYPE_OF_WORK='NMR' AND QWW.TYPE_OF_WORK='NMR' AND QCBT.PAYMENT_TYPE='NMR' AND QCBT.TEMP_BILL_ID=? AND QCBT.SITE_ID=?   order by TO_DATE(TO_CHAR(QTWP.WORK_DATE  ,'dd-MM-yyyy'),'dd-MM-yyyy')";//QTWP.WO_ROW_CODE,
	}else{
		query=" select (select  RTRIM(XMLAGG(XMLELEMENT(E,concat(QWP.ALLOCATED_QTY,'##'||QWP.NO_OF_HOURS||'##'||QWP.RATE),'@@').EXTRACT('//text()') ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID).GetClobVal(),'@@')  from QS_INV_AGN_WORK_PMT_DTL_HR QWP,QS_CONTRACTOR_BILL QCB where  QWP.ALLOCATED_QTY!=0 and  QCB.BILL_ID=QWP.BILL_ID AND QCB.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID AND QWP.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID  AND QWP.WO_WORK_DESC_ID=QWD.WO_WORK_DESC_ID     )as PrevQty, QWW.WO_WORK_DESC_ID,QTWP.WO_ROW_CODE,QTWP.NMRROWNUM ,QWD.QUANTITY,QTWP.ALLOCATED_QTY , "  //AND QWP.WO_MINORHEAD_ID=QWD.WO_MINORHEAD_ID    AND QWP.WO_ROW_CODE=QWD.WO_ROW_CODE
				+ " to_char(QTWP.WORK_DATE,'dd-MM-yyyy') as WORK_DATE,QTWP.REMARKS,QCBT.TEMP_BILL_ID ,QCBT.PAYBLE_AMOUNT ,QCBT.PAYMENT_REQ_DATE ,QCBT.CERTIFIED_AMOUNT ,QCBT.QS_WORKORDER_ISSUE_ID QS_WORKORDER_ISSUE_ID, "
				+ " QTWP.QS_INV_AGN_WORK_PMT_DTL_ID ,(QWD.TOTAL_AMOUNT/QWD.QUANTITY) as RATE,QTWP.AMOUNT AMOUNT,QTWP.WORK_DAYS ,QTWP.FROM_TIME , "
				+ " QTWP.NO_OF_HOURS ,QTWP.TO_TIME ,QTWP.MANUAL_DESC ,QWH.WO_MAJORHEAD_ID ,QWH.WO_MAJORHEAD_DESC ,QWMT.WO_MEASURMENT_ID ,QWMT.WO_MEASURMEN_NAME , "
				+ " QWMH.WO_MINORHEAD_ID ,QWMH.WO_MINORHEAD_DESC ,QWW.WO_WORK_DESCRIPTION "
				+ " from QS_CONTRACTOR_BILL_TEMP QCBT,QS_WORKORDER_ISSUE_DETAILS QWD,QS_TEMP_INV_AGN_WORK_PMT_DTL_HR QTWP,QS_WO_MAJORHEAD QWH,QS_WO_MEASURMENT QWMT, "
				+ " QS_WO_MINORHEAD QWMH ,QS_WO_WORKDESC QWW "
				+ " WHERE   QWD.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID  and QWMT.WO_MEASURMENT_ID=QWD.UOM and QWD.WO_ROW_CODE=QTWP.WO_ROW_CODE and QWD.WO_ROW_CODE=QTWP.WO_ROW_CODE  AND QWD.WO_MINORHEAD_ID=QWMH.WO_MINORHEAD_ID AND QCBT.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID and QTWP.TEMP_BILL_ID=QCBT.TEMP_BILL_ID AND  QTWP.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID AND QWMH.WO_MINORHEAD_ID=QTWP.WO_MINORHEAD_ID AND "
				+ " QWW.WO_WORK_DESC_ID=QWMT.WO_WORK_DESC_ID AND QWMH.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID AND QWMH.WO_MAJORHEAD_ID=QWH.WO_MAJORHEAD_ID AND QWH.TYPE_OF_WORK='NMR' AND QWW.TYPE_OF_WORK='NMR' AND QCBT.PAYMENT_TYPE='NMR' AND QCBT.TEMP_BILL_ID=? AND QCBT.SITE_ID=?  order by QWW.WO_WORK_DESCRIPTION ";
	}
		
		Object[] params={billBean.getTempBillNo(),billBean.getSiteId()};
		List<Map<String, Object>> NMRDataList=jdbcTemplate.queryForList(query,params);
		/*List<String> wo_minorhead_idList = new ArrayList<String>();
		List<String> wo_row_codeList = new ArrayList<String>();*/
		String addingCondition="";
		// WO_PERCENTAGE_AVAIL
		for (Map<String, Object> map : NMRDataList) {
			String wo_minorhead_id= map.get("WO_MINORHEAD_ID")==null?"":map.get("WO_MINORHEAD_ID").toString();
			String wo_row_code=map.get("WO_ROW_CODE")==null?"":map.get("WO_ROW_CODE").toString();
			/*wo_minorhead_idList.add("'" + wo_minorhead_id + "'");
			wo_row_codeList.add("'" + wo_row_code + "'");*/
			addingCondition+=" AND (QWD.WO_MINORHEAD_ID !='"+wo_minorhead_id+"' or QWD.WO_ROW_CODE!='"+wo_row_code+"') ";
		}
		
	/*	if(wo_minorhead_idList.size()==0){
			wo_minorhead_idList.add("'00'");
		}
		if(wo_row_codeList.size()==0){
			wo_row_codeList.add("'00'");
		}*/
		
		
		/*this is for unselected data IMP code don't remove*/
		query="	select (select  RTRIM(XMLAGG(XMLELEMENT(E,concat(QWP.ALLOCATED_QTY,'##'||QWP.NO_OF_HOURS||'##'||QWP.RATE),'@@').EXTRACT('//text()') ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID).GetClobVal(),'@@')  from QS_INV_AGN_WORK_PMT_DTL_HR QWP,QS_CONTRACTOR_BILL QCB where QWP.ALLOCATED_QTY!=0 and  QCB.BILL_ID=QWP.BILL_ID AND QCB.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID AND QWP.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID  AND QWP.WO_WORK_DESC_ID=QWD.WO_WORK_DESC_ID    )as PrevQty,"
			+" (QWD.TOTAL_AMOUNT/QWD.QUANTITY) AS RATE,QWD.WO_ROW_CODE,QWD.QUANTITY,QWI.CONTRACTOR_ID,QWI.SITE_ID,QWI.QS_WORKORDER_ISSUE_ID ,QWI.TYPE_OF_WORK ,QWD.WO_WORK_ISSUE_DTLS_ID ,QWD.WO_WORK_DESC_ID ,QWD.TOTAL_AMOUNT,QWD.WO_COMMENT  , MAJOR.WO_MAJORHEAD_ID,MAJOR.WO_MAJORHEAD_DESC,MINOR.WO_MINORHEAD_ID,MINOR.WO_MINORHEAD_DESC  ,WORKDESC.WO_WORK_DESCRIPTION,WORKDESC.WO_WORK_DESC_ID,  MESUR.WO_MEASURMENT_ID,MESUR.WO_MEASURMEN_NAME "
			+" from QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_DETAILS QWD   ,QS_WO_WORKDESC WORKDESC,QS_WO_MEASURMENT MESUR,QS_WO_MINORHEAD MINOR,QS_WO_MAJORHEAD MAJOR "
			+" WHERE  WORKDESC.WO_MINORHEAD_ID=MINOR.WO_MINORHEAD_ID  and MESUR.WO_MEASURMENT_ID=QWD.UOM and QWD.WO_MINORHEAD_ID=MINOR.WO_MINORHEAD_ID  AND MINOR.WO_MAJORHEAD_ID=MAJOR.WO_MAJORHEAD_ID    AND MESUR.WO_WORK_DESC_ID=WORKDESC.WO_WORK_DESC_ID AND QWD.WO_WORK_DESC_ID=WORKDESC.WO_WORK_DESC_ID AND  QWI.WORK_ORDER_NUMBER=? AND QWI.SITE_ID=? AND QWI.TYPE_OF_WORK='NMR' " 
		
			//+" AND QWD.WO_MINORHEAD_ID not in("+wo_minorhead_idList.toString().replace("[", "").replace("]", "")+") "
			//+" AND QWD.WO_ROW_CODE not in("+wo_row_codeList.toString().replace("[", "").replace("]", "")+") " 
			
			+" "+addingCondition+" " 
			+ " AND QWI.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID order by MINOR.WO_MINORHEAD_DESC,QWD.WO_ROW_CODE,WORKDESC.WO_WORK_DESCRIPTION ";
	 
		Object[] queryParams={billBean.getWorkOrderNo(),billBean.getSiteId()};
		List<Map<String, Object>> unSelectedItemnList=	jdbcTemplate.queryForList(query,queryParams);
		NMRDataList.addAll(unSelectedItemnList);
		return NMRDataList;
	}	
	
	/**
	 * @description this controller used to load NMR bill details using bill numebr and site_id
	 */
	@Override
	public List<Map<String, Object>> loadPermanentNMRBillData(ContractorQSBillBean billBean) {
		String	query="";
		//checking condition based on the condition changing the query
		//if condition is equal to "nmrDetailsPrint" then select the NMR details sorting by date wise
		//else select data by sorting minor head and work description name
		if(billBean.getCondition().equals("nmrDetailsPrint")){
			query="select (select  RTRIM(XMLAGG(XMLELEMENT(E,concat(QWP.ALLOCATED_QTY,'##'||QWP.NO_OF_HOURS||'##'||QWP.RATE),'@@').EXTRACT('//text()') ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID).GetClobVal(),'@@')   from QS_INV_AGN_WORK_PMT_DTL_HR QWP,QS_CONTRACTOR_BILL QCB where  QWP.ALLOCATED_QTY!=0 and   QCB.BILL_ID=QWP.BILL_ID AND  QWP.BILL_ID<'"+billBean.getBillNo()+"' AND QCB.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID AND QWP.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID  AND QWP.WO_WORK_DESC_ID=QWD.WO_WORK_DESC_ID    AND QWP.WO_MINORHEAD_ID=QWD.WO_MINORHEAD_ID    AND QWP.WO_ROW_CODE=QWD.WO_ROW_CODE  )as PrevQty,  QWW.WO_WORK_DESC_ID,QTWP.WO_ROW_CODE,QTWP.NMRROWNUM,(QWD.TOTAL_AMOUNT/QWD.QUANTITY) as RATE,QTWP.RATE as BillRATE,QWD.QUANTITY,QTWP.ALLOCATED_QTY , "
					+ "	TO_CHAR(QTWP.WORK_DATE  ,'dd-MM-yyyy') as WORK_DATE,QTWP.REMARKS,QCBT.TEMP_BILL_ID ,QCBT.PAYBLE_AMOUNT ,QCBT.PAYMENT_REQ_DATE ,QCBT.CERTIFIED_AMOUNT ,QCBT.QS_WORKORDER_ISSUE_ID QS_WORKORDER_ISSUE_ID, "
					+ " QTWP.QS_INV_AGN_WORK_PMT_DTL_ID ,QTWP.AMOUNT AMOUNT,QTWP.WORK_DAYS ,QTWP.FROM_TIME , "
					+ " QTWP.NO_OF_HOURS ,QTWP.TO_TIME ,QTWP.MANUAL_DESC ,QWH.WO_MAJORHEAD_ID ,QWH.WO_MAJORHEAD_DESC ,QWMT.WO_MEASURMENT_ID ,QWMT.WO_MEASURMEN_NAME , "
					+ " QWMH.WO_MINORHEAD_ID ,QWMH.WO_MINORHEAD_DESC ,QWW.WO_WORK_DESCRIPTION "
					+ " from QS_CONTRACTOR_BILL QCBT,QS_WORKORDER_ISSUE_DETAILS QWD,QS_INV_AGN_WORK_PMT_DTL_HR QTWP,QS_WO_MAJORHEAD QWH,QS_WO_MEASURMENT QWMT, "
					+ " QS_WO_MINORHEAD QWMH ,QS_WO_WORKDESC QWW "
					+ " WHERE   QWD.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID  and QWMT.WO_MEASURMENT_ID=QWD.UOM  and QWD.WO_ROW_CODE=QTWP.WO_ROW_CODE   AND QWD.WO_MINORHEAD_ID=QWMH.WO_MINORHEAD_ID AND QCBT.QS_WORKORDER_ISSUE_ID=QTWP.QS_WORKORDER_ISSUE_ID AND QCBT.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID and QTWP.BILL_ID=QCBT.BILL_ID AND  QTWP.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID AND QWMH.WO_MINORHEAD_ID=QTWP.WO_MINORHEAD_ID AND "
					+ " QWW.WO_WORK_DESC_ID=QWMT.WO_WORK_DESC_ID AND QWMH.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID AND QWMH.WO_MAJORHEAD_ID=QWH.WO_MAJORHEAD_ID AND QWH.TYPE_OF_WORK='NMR' AND QWW.TYPE_OF_WORK='NMR' AND QCBT.PAYMENT_TYPE='NMR' AND QCBT.TEMP_BILL_ID=? AND QCBT.SITE_ID=?  order by TO_DATE(TO_CHAR(QTWP.WORK_DATE  ,'dd-MM-yyyy'),'dd-MM-yyyy')";
		}else{
			query="select (select  RTRIM(XMLAGG(XMLELEMENT(E,concat(QWP.ALLOCATED_QTY,'##'||QWP.NO_OF_HOURS||'##'||QWP.RATE),'@@').EXTRACT('//text()') ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID).GetClobVal(),'@@')  from QS_INV_AGN_WORK_PMT_DTL_HR QWP,QS_CONTRACTOR_BILL QCB where  QWP.ALLOCATED_QTY!=0 and  QCB.BILL_ID=QWP.BILL_ID AND  QWP.BILL_ID<'"+billBean.getBillNo()+"' AND QCB.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID AND QWP.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID  AND QWP.WO_WORK_DESC_ID=QWD.WO_WORK_DESC_ID   )as PrevQty, QWW.WO_WORK_DESC_ID,QTWP.WO_ROW_CODE,QTWP.NMRROWNUM,(QWD.TOTAL_AMOUNT/QWD.QUANTITY) as RATE,QTWP.RATE as BillRATE,QWD.QUANTITY,QTWP.ALLOCATED_QTY ,  "
						+ " to_char(QTWP.WORK_DATE,'dd-MM-yyyy') as WORK_DATE,QTWP.REMARKS,QCBT.TEMP_BILL_ID ,QCBT.PAYBLE_AMOUNT ,QCBT.PAYMENT_REQ_DATE ,QCBT.CERTIFIED_AMOUNT ,QCBT.QS_WORKORDER_ISSUE_ID QS_WORKORDER_ISSUE_ID, "
						+ " QTWP.QS_INV_AGN_WORK_PMT_DTL_ID ,QTWP.AMOUNT AMOUNT,QTWP.WORK_DAYS ,QTWP.FROM_TIME , "
						+ " QTWP.NO_OF_HOURS ,QTWP.TO_TIME ,QTWP.MANUAL_DESC ,QWH.WO_MAJORHEAD_ID ,QWH.WO_MAJORHEAD_DESC ,QWMT.WO_MEASURMENT_ID ,QWMT.WO_MEASURMEN_NAME , "
						+ " QWMH.WO_MINORHEAD_ID ,QWMH.WO_MINORHEAD_DESC ,QWW.WO_WORK_DESCRIPTION "
						+ " from QS_CONTRACTOR_BILL QCBT,QS_WORKORDER_ISSUE_DETAILS QWD,QS_INV_AGN_WORK_PMT_DTL_HR QTWP,QS_WO_MAJORHEAD QWH,QS_WO_MEASURMENT QWMT, "
						+ " QS_WO_MINORHEAD QWMH ,QS_WO_WORKDESC QWW "
						+ " WHERE   QWD.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID  and QWMT.WO_MEASURMENT_ID=QWD.UOM  and QWD.WO_ROW_CODE=QTWP.WO_ROW_CODE   AND QWD.WO_MINORHEAD_ID=QWMH.WO_MINORHEAD_ID AND QCBT.QS_WORKORDER_ISSUE_ID=QTWP.QS_WORKORDER_ISSUE_ID AND QCBT.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID and QTWP.BILL_ID=QCBT.BILL_ID AND  QTWP.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID AND QWMH.WO_MINORHEAD_ID=QTWP.WO_MINORHEAD_ID AND "
						+ " QWW.WO_WORK_DESC_ID=QWMT.WO_WORK_DESC_ID AND QWMH.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID AND QWMH.WO_MAJORHEAD_ID=QWH.WO_MAJORHEAD_ID AND QWH.TYPE_OF_WORK='NMR' AND QWW.TYPE_OF_WORK='NMR' AND QCBT.PAYMENT_TYPE='NMR' AND QCBT.TEMP_BILL_ID=? AND QCBT.SITE_ID=?  order by QWW.WO_WORK_DESCRIPTION";//and  QTWP.NMRROWNUM is not null 
		}
		Object[] params={billBean.getTempBillNo(),billBean.getSiteId()};
		System.out.println(query);
		List<Map<String, Object>> NMRDataList=jdbcTemplate.queryForList(query,params);
		
	/*	List<String> wo_minorhead_idList = new ArrayList<String>();*/
		String addingCondition="",condition="";
		//Retrieve minor head and work desk id for selecting non selected data when creation of NMR Bill's 
		for (Map<String, Object> map : NMRDataList) {
			String wo_minorhead_id= map.get("WO_MINORHEAD_ID")==null?"":map.get("WO_MINORHEAD_ID").toString();
			String wo_row_code=map.get("WO_ROW_CODE")==null?"":map.get("WO_ROW_CODE").toString();
			String wo_work_desc_id=map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString();
			condition=" AND (QWD.WO_MINORHEAD_ID !='"+wo_minorhead_id+"' or WORKDESC.WO_WORK_DESC_ID!='"+wo_work_desc_id+"' or QWD.WO_ROW_CODE!='"+wo_row_code+"' )";
			if(!addingCondition.contains(condition)){
				addingCondition+=condition;	
			}
		}
		
		//if wo_minorhead_idList add default values zero's
	/*	if(wo_minorhead_idList.size()==0){
			wo_minorhead_idList.add("'00'");
		}*/
		
		/*this is for unselected data IMP code don't remove*/
		query="	select (select  RTRIM(XMLAGG(XMLELEMENT(E,concat(QWP.ALLOCATED_QTY,'##'||QWP.NO_OF_HOURS||'##'||QWP.RATE),'@@').EXTRACT('//text()') ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID).GetClobVal(),'@@')  from QS_INV_AGN_WORK_PMT_DTL_HR QWP,QS_CONTRACTOR_BILL QCB where  QWP.ALLOCATED_QTY!=0 and   QCB.BILL_ID=QWP.BILL_ID AND QCB.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID AND QWP.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID  AND QWP.WO_WORK_DESC_ID=QWD.WO_WORK_DESC_ID AND  QWP.BILL_ID<'"+billBean.getBillNo()+"'  )as PrevQty,"
			+ "(QWD.TOTAL_AMOUNT/QWD.QUANTITY) AS RATE, QWD.WO_ROW_CODE,QWD.QUANTITY,QWI.CONTRACTOR_ID,QWI.SITE_ID,QWI.QS_WORKORDER_ISSUE_ID ,QWI.TYPE_OF_WORK ,QWD.WO_WORK_ISSUE_DTLS_ID ,QWD.WO_WORK_DESC_ID ,QWD.TOTAL_AMOUNT,QWD.WO_COMMENT  , MAJOR.WO_MAJORHEAD_ID,MAJOR.WO_MAJORHEAD_DESC,MINOR.WO_MINORHEAD_ID,MINOR.WO_MINORHEAD_DESC  ,WORKDESC.WO_WORK_DESCRIPTION,WORKDESC.WO_WORK_DESC_ID,  MESUR.WO_MEASURMENT_ID,MESUR.WO_MEASURMEN_NAME "
			+ " from QS_WORKORDER_ISSUE QWI,QS_WORKORDER_ISSUE_DETAILS QWD ,QS_WO_WORKDESC WORKDESC,QS_WO_MEASURMENT MESUR,QS_WO_MINORHEAD MINOR,QS_WO_MAJORHEAD MAJOR "
			+ " WHERE    WORKDESC.WO_MINORHEAD_ID=MINOR.WO_MINORHEAD_ID  and MESUR.WO_MEASURMENT_ID=QWD.UOM and QWD.WO_MINORHEAD_ID=MINOR.WO_MINORHEAD_ID  AND MINOR.WO_MAJORHEAD_ID=MAJOR.WO_MAJORHEAD_ID    AND MESUR.WO_WORK_DESC_ID=WORKDESC.WO_WORK_DESC_ID AND QWD.WO_WORK_DESC_ID=WORKDESC.WO_WORK_DESC_ID "
			+ " AND  QWI.WORK_ORDER_NUMBER=? AND QWI.SITE_ID=? AND QWI.TYPE_OF_WORK='NMR' "
			//selecting non selected records ,while showing of NMR Bills
		//	+ " AND QWD.WO_MINORHEAD_ID not in("+wo_minorhead_idList.toString().replace("[", "").replace("]", "")+")"
			+ ""+addingCondition+""
			+ " AND QWI.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID order by MINOR.WO_MINORHEAD_DESC,WORKDESC.WO_WORK_DESCRIPTION ";
			Object[] queryParams={billBean.getWorkOrderNo(),billBean.getSiteId()};
			List<Map<String, Object>> unSelectedItemnList=	jdbcTemplate.queryForList(query,queryParams);
		
		NMRDataList.addAll(unSelectedItemnList);
		return NMRDataList;
	}
	/**
	 * not in use
	 */
	@Override
	public List<Map<String, Object>> loadNMRBillDeatilsByBillNo(ContractorQSBillBean billBean) {
	String tempBillNo=	billBean.getTempBillNo();
		String minorHeadId=billBean.getMinorHeadId1();
		String workDate=billBean.getWorkDate1();
		String query="select distinct QTWP.WO_WORK_DESC_ID,QTWP.ALLOCATED_QTY,QTWP.WO_MINORHEAD_ID ,QWW.WO_WORK_DESCRIPTION from QS_TEMP_INV_AGN_WORK_PMT_DTL_HR QTWP,QS_WO_WORKDESC QWW where  QTWP.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID and trunc(QTWP.WORK_DATE) = to_date(?,'dd/MM/yy') and  QTWP.WO_MINORHEAD_ID=? and QTWP.TEMP_BILL_ID=?";
		Object[] params={workDate,minorHeadId,tempBillNo};
		
		return jdbcTemplate.queryForList(query, params);
	}
	/**
	 * @description this method will return all the work description name and id using minor head id and using work order number
	*/
	@Override
	public List<Map<String, Object>> loadWorkDescNMRBills(ContractorQSBillBean billBean) {
		 //query="select distinct QWW.WO_WORK_DESC_ID,QTWP.WO_ROW_CODE,QTWP.NMRROWNUM,(QWD.TOTAL_AMOUNT/QWD.QUANTITY) as RATE,QTWP.RATE as BillRATE,QWD.QUANTITY,QTWP.ALLOCATED_QTY ,(select LISTAGG(concat(QWP.ALLOCATED_QTY,'&&'||QWP.NO_OF_HOURS||'&&'||QWP.RATE),'@@')  WITHIN GROUP (ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID)  from QS_INV_AGN_WORK_PMT_DTL_HR QWP,QS_CONTRACTOR_BILL QCB where QCB.BILL_ID=QWP.BILL_ID AND  QWP.BILL_ID<'"+billBean.getBillNo()+"' AND QCB.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID AND QWP.QS_WORKORDER_ISSUE_ID=QWD.QS_WORKORDER_ISSUE_ID  AND QWP.WO_WORK_DESC_ID=QWD.WO_WORK_DESC_ID    AND QWP.WO_MINORHEAD_ID=QWD.WO_MINORHEAD_ID    AND QWP.WO_ROW_CODE=QWD.WO_ROW_CODE  )as PrevQty,  "
		
		String  query="select (select  RTRIM(XMLAGG(XMLELEMENT(E,concat(QWP.ALLOCATED_QTY,'##'||QWP.NO_OF_HOURS||'##'||QWP.RATE),'@@').EXTRACT('//text()') ORDER BY QS_INV_AGN_WORK_PMT_DTL_ID).GetClobVal(),'@@')  from QS_INV_AGN_WORK_PMT_DTL_HR QWP,QS_CONTRACTOR_BILL QCB where QWP.ALLOCATED_QTY!=0 AND QCB.BILL_ID=QWP.BILL_ID AND  QWP.BILL_ID<'"+billBean.getBillNo()+"' AND QCB.QS_WORKORDER_ISSUE_ID=QWID.QS_WORKORDER_ISSUE_ID AND QWP.QS_WORKORDER_ISSUE_ID=QWID.QS_WORKORDER_ISSUE_ID  AND QWP.WO_WORK_DESC_ID=QWID.WO_WORK_DESC_ID    AND QWP.WO_MINORHEAD_ID=QWID.WO_MINORHEAD_ID    AND QWP.WO_ROW_CODE=QWID.WO_ROW_CODE  )as PrevQty, "
			+ " QWID.WO_ROW_CODE,QWID.WO_WORK_DESC_ID,QWID.QUANTITY,QWID.WO_MINORHEAD_ID ,QWW.WO_WORK_DESCRIPTION,(QWID.TOTAL_AMOUNT/QWID.QUANTITY) as RATE"
			+ " from QS_WORKORDER_ISSUE_DETAILS QWID,QS_WO_WORKDESC QWW  "
			+ " where  QWID.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID and  QWID.WO_MINORHEAD_ID=QWW.WO_MINORHEAD_ID "
			+ " and  QWID.WO_MINORHEAD_ID=? and QWID.WO_ROW_CODE=? "
			+ " AND QWID.QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? AND SITE_ID=? )";
	Object[] params={billBean.getMinorHeadId1(),billBean.getWoRowCode(),billBean.getWorkOrderNo(),billBean.getSiteId()};
	List<Map<String, Object>> list=jdbcTemplate.queryForList(query,params);
	return list;
}
	
	/**
	 * @description this method will return actual quantity, amount using work order number and minor head, work description id
	 */
	@Override
	public String getWordDescNameAndId(ContractorQSBillBean bean) {
		String query="SELECT concat(QWW.WO_WORK_DESCRIPTION,'@@'||QWW.WO_WORK_DESC_ID||'@@'||QWD.QUANTITY||'@@'||QWD.TOTAL_AMOUNT/QWD.QUANTITY) FROM QS_WO_WORKDESC QWW,QS_WORKORDER_ISSUE_DETAILS QWD "
				+ "WHERE QWD.WO_WORK_DESC_ID=QWW.WO_WORK_DESC_ID and QWD.WO_MINORHEAD_ID='"+bean.getMinorHeadId1()+"' and  QWD.WO_WORK_DESC_ID='"+bean.getWorkDescId1()+"' and QWD.WO_ROW_CODE='"+bean.getWoRowCode()+"' and rownum=1  and "
				+ "QS_WORKORDER_ISSUE_ID=(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+bean.getWorkOrderNo()+"'  and SITE_ID='"+bean.getSiteId()+"') ";
		String result=jdbcTemplate.queryForObject(query, String.class);
	return result;
	}
	/**
	 * @description this method is inserting NMR bill data like bill date,  bill number, work order number, contractor name
	 */
	@Override
	public List<Object> insertNMRBillData(ContractorQSBillBean bean) throws Exception {
		List<Object> response = new ArrayList<Object>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
		Date date=null;
		String workOrderIssueId="";
		int result = 0;
		int billTempSeqNum =0;
		try {
			//converting the simple date into date and time 
			String[] str=timeStamp.split(" ");
			String	raBillDate=bean.getBillDate().trim();
			raBillDate=raBillDate+" "+str[1].trim();
			bean.setBillDate(raBillDate);
			date=dateFormat.parse(raBillDate);
		} catch (Exception e) {
			log.info("Error Msg "+e.getMessage());
		}

		 billTempSeqNum = jdbcTemplate.queryForObject("SELECT QS_BILL_TEMP_SEQ.NEXTVAL FROM DUAL", Integer.class);
			try {
				 workOrderIssueId=jdbcTemplate.queryForObject("SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER=? AND SITE_ID=?", new Object[]{ bean.getWorkOrderNo().split("\\$")[0],bean.getSiteId()}, String.class);
				 if(workOrderIssueId==null){
					 throw new Exception("missing work order issue id");
				 }
			} catch (Exception e) {
				throw new Exception("missing work order issue id");
			}
		StringBuffer query = new StringBuffer("INSERT INTO QS_CONTRACTOR_BILL_TEMP(WORK_BLOCK_NAMES,TEMP_BILL_ID,PERMANANT_BILL_NUMBER,CONTRACTOR_ID,SITE_ID,")
				.append("PAYBLE_AMOUNT, STATUS,CREATED_BY,PENDING_EMP_ID,")
				.append("PENDING_DEPT_ID,PAYMENT_REQ_DATE,ENTRY_DATE, PAYMENT_TYPE,CERTIFIED_AMOUNT,DEDUCTION_AMOUNT,")
				.append("QS_WORKORDER_ISSUE_ID,BILL_INVOICE_NUMBER,TYPE_OF_WORK) ")
				.append("values(?,?,?,?,?,?,?,?,?,?,?,SYSDATE,?,?,?,?,?,?)");

		Object[] queryParams = {bean.getNMRBillBlocks(), billTempSeqNum,bean.getOldBillNo(), bean.getContractorId(), bean.getSiteId(),
				bean.getPaybleAmt(), "A", bean.getFromEmpId(), bean.getApproverEmpId(), "0",
				new java.sql.Date(date.getTime()), bean.getPaymentType(), bean.getTotalAmtCumulativeCertified(), bean.getRecovery_amount()
				,workOrderIssueId,bean.getBillInvoiceNumber(),"WOB"};
		
		log.info("Insert NMR Bill Query Params "+queryParams.length);
		 result = jdbcTemplate.update(query.toString(), queryParams);
		
		query = new StringBuffer("INSERT INTO QS_TEMP_BILL_DEDUCTION_DTLS ")
				.append("(DEDUCTION_ID,TEMP_BILL_ID,DEDUCTION_DESC,DEDUCTION_AMOUNT,TYPE_OF_DEDUCTION) ")
				.append("VALUES(QS_TEMP_BILL_DEDUCTION_DTLS_SEQ.NEXTVAL,?,?,?,?)");
		
		if (bean.getPettyExpensesCurrentCerti().length() != 0) {// &&!bean.getPettyExpensesCurrentCerti().equals("0")
			Object[] params1 = { billTempSeqNum, "Petty deduction desc", bean.getPettyExpensesCurrentCerti(), "PETTY" };
			result = jdbcTemplate.update(query.toString(), params1);
		}
		if (bean.getOtherAmout().length() != 0) {// &&!bean.getOtherAmout().equals("0")
			Object[] params1 = { billTempSeqNum, "Other Amount deduction desc", bean.getOtherAmout(), "OTHER"};
			result = jdbcTemplate.update(query.toString(), params1);
		}
		
		if(bean.getRecovery_amount().length()!=0){
			Object[] params1 = { billTempSeqNum, "Recovery Deduction Amount", bean.getRecovery_amount(), "RECOVERY"};
			result = jdbcTemplate.update(query.toString(), params1);
		}
		
		response.add(billTempSeqNum);
		return response;
	}
	
	/**
	 * @description this method used to insert NMR details rows data
	 * here we used Spring JdbcTemplate batchUpdate() insert all data only once
	 */
	@Override
	public int insertNMRData(final List<ContractorQSBillBean> listOfNMRData) {
		//getting the list size for spring jdbc batch update
		final int len = listOfNMRData.size();
		String query = "INSERT INTO QS_TEMP_INV_AGN_WORK_PMT_DTL_HR"
				+ "(QS_INV_AGN_WORK_PMT_DTL_ID,WO_WORK_DESC_ID,WO_MINORHEAD_ID,RATE,AMOUNT,TEMP_BILL_ID,ALLOCATED_QTY,WORK_DAYS,"
				+ "FROM_TIME,NO_OF_HOURS,TO_TIME,MANUAL_DESC,WORK_DATE,REMARKS,NMRROWNUM,QS_WORKORDER_ISSUE_ID,WO_ROW_CODE)"
				+ " values(QS_TEMP_INV_AGN_WORK_PMT_DTL_HR_SEQ.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER=? and SITE_ID=?),?)";
		int result[] = jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			@Override
			public int getBatchSize() {
				return len;
			}
			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ContractorQSBillBean bean=	 listOfNMRData.get(i);
				ps.setString(1,bean.getWorkDescId1());
				ps.setString(2,bean.getMinorHeadId1());
				ps.setString(3,bean.getRate1());
				ps.setString(4,bean.getAmount1());
				ps.setString(5,bean.getTempBillNo());
				ps.setString(6,bean.getNoOfworkers1());
				ps.setString(7,bean.getWorkDays1());
				ps.setString(8,bean.getFromTime1());
				ps.setString(9,bean.getNoOfHours1());
				ps.setString(10,bean.getToTime1());
				ps.setString(11, bean.getManualDesc1());
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
				String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
				Date date=null;
				try {
				String[] str=timeStamp.split(" ");
				String	NMRBillDate=bean.getWorkDate1().trim();
				NMRBillDate=NMRBillDate+" "+str[1].trim();
				bean.setBillDate(NMRBillDate);
				date=dateFormat.parse(NMRBillDate);
				} catch (Exception e) {
					System.out.println("Error Msg "+e.getMessage());
				}
				ps.setDate(12, new java.sql.Date(date.getTime()));
				ps.setString(13, bean.getRemarks());
				ps.setString(14,bean.getNMRRowIndex1());
				ps.setString(15,bean.getWorkOrderNo());	
				ps.setString(16,bean.getSiteId());	
				ps.setString(17, bean.getWoRowCode());
			}
		});

		System.out.println(result.length);
		return result.length;
	}
	/**
	 * @description this method will update the temporary bill
	 * if this the the final level of the bill we need to insert all the temporary table data into the permanent table
	 */
	@Override
	public int approveNMRBill(ContractorQSBillBean changedBill) throws Exception {
	String query="";
	int count=0;
	
		if(changedBill.getRecovery_amount().length()!=0){
			 query = "update QS_TEMP_BILL_DEDUCTION_DTLS set  DEDUCTION_AMOUNT=?  where TEMP_BILL_ID=? and TYPE_OF_DEDUCTION='RECOVERY'";
			Object[] secQueryParams = { changedBill.getRecovery_amount(), changedBill.getTempBillNo() };
			count = jdbcTemplate.update(query, secQueryParams);
		}
	
		if (changedBill.getApproverEmpId().equals("END")) {
			//updating the temporary bill status and pending emp id
			query = "UPDATE QS_CONTRACTOR_BILL_TEMP SET WORK_BLOCK_NAMES=?,PENDING_EMP_ID=?,PAYBLE_AMOUNT=?,CERTIFIED_AMOUNT=?,DEDUCTION_AMOUNT=?,STATUS='I' WHERE TEMP_BILL_ID=?";
			Object[] queryParams = {changedBill.getNMRBillBlocks(), changedBill.getApproverEmpId(), changedBill.getPaybleAmt(),
					changedBill.getTotalAmtCumulativeCertified(),changedBill.getTotalCurrntDeducAmt(),
					changedBill.getTempBillNo()};
			count = jdbcTemplate.update(query, queryParams);
			
			String contractorBillSeq = "";
			String WOWisebillNo="";
			String workOrderNo=changedBill.getWorkOrderNo().trim();
			String tempBillNo=changedBill.getTempBillNo().trim();
			String	contractorPKSeqNumber = jdbcTemplate.queryForObject("SELECT QS_CONTRACTOR_BILL_SEQ.NEXTVAL FROM DUAL",String.class);
			 
			
			String permanentBillNo=changedBill.getBillNo();
			if(permanentBillNo==null){
				permanentBillNo="";
			}
			if(permanentBillNo.contains("NMR/")){
				contractorBillSeq=permanentBillNo;
			}else {
			query="  SELECT   MAX(NVL(REGEXP_SUBSTR (BILL_ID, '[^/]+', 1, 2),0))+1    AS part_2 "
				 + "FROM    QS_CONTRACTOR_BILL  where  QS_WORKORDER_ISSUE_ID =   (select QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER='"+workOrderNo+"'  and SITE_ID='"+changedBill.getSiteId()+"') AND PAYMENT_TYPE='NMR'";	
				try {
					contractorBillSeq=jdbcTemplate.queryForObject(query,String.class);	
					if(contractorBillSeq==null){
						contractorBillSeq="1";	
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
					WOWisebillNo=contractorBillSeq;
					int num=Integer.valueOf(contractorBillSeq);
					contractorBillSeq = "NMR/" + String.format("%02d", num);
			}
					//inserting temporary bill data into the permanent table
					String queryForContractorBill = "INSERT INTO QS_CONTRACTOR_BILL(CONT_SEQ_BILL_NO,BILL_ID,BILL_NO,CONTRACTOR_ID,SITE_ID, "
							+ "PAYBLE_AMOUNT,STATUS,PAYMENT_REQ_DATE,ENTRY_DATE,PAYMENT_TYPE,CERTIFIED_AMOUNT,"
							+ " DEDUCTION_AMOUNT,DEDUCTION_REF_ID,TEMP_BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_STATUS,CREATED_BY,WORK_BLOCK_NAMES,BILL_INVOICE_NUMBER,TYPE_OF_WORK)"
							+ " select '"+contractorPKSeqNumber+"','" + contractorBillSeq+ "','"+WOWisebillNo+"',CONTRACTOR_ID,SITE_ID, "
							+ "PAYBLE_AMOUNT,'A',PAYMENT_REQ_DATE,ENTRY_DATE,PAYMENT_TYPE,CERTIFIED_AMOUNT," /*changedBill.getTotalCurrentCerti()*/
							+ " DEDUCTION_AMOUNT,DEDUCTION_REF_ID," + tempBillNo
							+ ",QS_WORKORDER_ISSUE_ID,'PENDING',CREATED_BY,WORK_BLOCK_NAMES,BILL_INVOICE_NUMBER,TYPE_OF_WORK   from   QS_CONTRACTOR_BILL_TEMP where TEMP_BILL_ID="
							+ tempBillNo + "";
					int result = jdbcTemplate.update(queryForContractorBill);
						
					//inserting all the temporary details into the permanent table by using temporary bill number
					String queryForWorkDetail="insert into QS_INV_AGN_WORK_PMT_DTL_HR (QS_INV_AGN_WORK_PMT_DTL_ID,WO_ROW_CODE,NMRRowNum,WO_WORK_DESC_ID,RATE,AMOUNT,BILL_ID,"
							+ " ALLOCATED_QTY,WORK_DAYS,FROM_TIME,NO_OF_HOURS,TO_TIME,QS_WORKORDER_ISSUE_ID,WO_MINORHEAD_ID,MANUAL_DESC,WORK_DATE,REMARKS)"
							+ " select                          QS_INV_AGN_WORK_PMT_DTL_HR_SEQ.NEXTVAL,WO_ROW_CODE,NMRRowNum,WO_WORK_DESC_ID,RATE,AMOUNT,'"+contractorBillSeq+"',"
							+ " ALLOCATED_QTY,WORK_DAYS,FROM_TIME,NO_OF_HOURS,TO_TIME,QS_WORKORDER_ISSUE_ID,WO_MINORHEAD_ID,MANUAL_DESC,WORK_DATE,REMARKS "
							+ " from QS_TEMP_INV_AGN_WORK_PMT_DTL_HR where TEMP_BILL_ID="+tempBillNo+" AND ALLOCATED_QTY!='0'";
					result=jdbcTemplate.update(queryForWorkDetail);
					
					query = "INSERT INTO   QS_BILL_DEDUCTION_DTLS (DEDUCTION_ID,CONTRACTOR_BILL_ID,DEDUCTION_DESC,DEDUCTION_AMOUNT,TYPE_OF_DEDUCTION,QS_WORKORDER_ISSUE_ID) "
							+ " SELECT QS_BILL_DEDUCTION_DTLS_SEQ.NEXTVAL,?,DEDUCTION_DESC,DEDUCTION_AMOUNT,TYPE_OF_DEDUCTION,(SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+workOrderNo+"'  and SITE_ID='"+changedBill.getSiteId()+"') "
							+ " FROM QS_TEMP_BILL_DEDUCTION_DTLS WHERE TEMP_BILL_ID=? ";
					Object[] obj1 = { contractorBillSeq, tempBillNo };
					result = jdbcTemplate.update(query, obj1);

					
					//inserting data in Recovery
					int QS_WORKORDER_ISSUE_ID=jdbcTemplate.queryForObject("(select QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER='"+workOrderNo+"' and SITE_ID='"+changedBill.getSiteId()+"' ) ", Integer.class);
					List<Map<String, Object>> listOfRecoverys=jdbcTemplate.queryForList("select CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_AMOUNT,QS_WORKORDER_ISSUE_ID from QS_TEMP_BILL_RECOVERY where TEMP_BILL_ID=? ",tempBillNo);
					String child_product_id="";
					String measurement_id="";
					int recoveryResult=0;
					for (Map<String, Object> map : listOfRecoverys) {
						child_product_id=map.get("CHILD_PRODUCT_ID")==null?"":map.get("CHILD_PRODUCT_ID").toString();
						measurement_id=map.get("MEASUREMENT_ID")==null?"":map.get("MEASUREMENT_ID").toString();
						
						//checking is this record exists or not if not not insert record or update the existing record using child_id
						String queryForRecovery="select count(*) from  QS_BILL_RECOVERY where  CHILD_PRODUCT_ID=? and MEASUREMENT_ID=? and QS_WORKORDER_ISSUE_ID=?";
						result=jdbcTemplate.queryForObject(queryForRecovery,new Object[]{child_product_id,measurement_id,QS_WORKORDER_ISSUE_ID}, Integer.class);
						//if the result not zero 
						if(result!=0){
							Double recoveryAmount=jdbcTemplate.queryForObject("select RECOVERY_AMOUNT from  QS_TEMP_BILL_RECOVERY where TEMP_BILL_ID='"+tempBillNo+"' and  CHILD_PRODUCT_ID=? and MEASUREMENT_ID=?   ",new Object[]{child_product_id,measurement_id}, Double.class);
							queryForRecovery="update  QS_BILL_RECOVERY set RECOVERY_AMOUNT=RECOVERY_AMOUNT+? where CHILD_PRODUCT_ID=? and MEASUREMENT_ID=? and QS_WORKORDER_ISSUE_ID=? ";
							recoveryResult	=jdbcTemplate.update(queryForRecovery,recoveryAmount,child_product_id,measurement_id,QS_WORKORDER_ISSUE_ID);
						}else if(result==0){
								recoveryResult=	jdbcTemplate.update("insert into QS_BILL_RECOVERY(CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,QS_WORKORDER_ISSUE_ID)"
									+ " select CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,QS_WORKORDER_ISSUE_ID "
									+ " from QS_TEMP_BILL_RECOVERY where TEMP_BILL_ID='"+tempBillNo+"' and CHILD_PRODUCT_ID='"+child_product_id+"' and MEASUREMENT_ID='"+measurement_id+"'");
						}
					}
					
					//inserting all records in QS_BILL_RECOVERY_HISTORY from QS_TEMP_BILL_RECOVERY_HISTORY
					String queryForRecoveryHistory="insert into QS_BILL_RECOVERY_HISTORY(CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,BILL_ID,QS_WORKORDER_ISSUE_ID) "
												 + "select                CHILD_PRODUCT_ID,MEASUREMENT_ID,RECOVERY_QUANTITY,RECOVERY_AMOUNT,'"+contractorBillSeq+"',QS_WORKORDER_ISSUE_ID from  QS_TEMP_BILL_RECOVERY_HISTORY  where TEMP_BILL_ID='"+tempBillNo+"'";
					recoveryResult=jdbcTemplate.update(queryForRecoveryHistory);
					
				//data copying completed 
					
				String strNextLevelApprove = getWorkOrderPendingEmployeeId(changedBill.getUserId(),changedBill.getSiteId());
				int accCntPaymentSeq = jdbcTemplate.queryForObject("select ACC_CNT_PAYMENT_SEQ.NEXTVAL FROM DUAL",Integer.class);
				int siteWisePaymentNo = jdbcTemplate.queryForObject("select count(SITE_ID)+1 from ACC_CNT_PAYMENT where SITE_ID='" + changedBill.getSiteId() + "'",Integer.class);
				//inserting the bill data to the account table
				//so the site account people will approve and the bill will go for the payment
				String 	queryForAccPayment = "insert into ACC_CNT_PAYMENT(BILL_NUMBER,CNT_PAYMENT_ID,CREATED_DATE,BILL_AMOUNT,PAYMENT_REQ_AMOUNT,BILL_DATE, "
								+ "WO_AMOUNT,WO_DATE,WO_NUMBER,SITE_ID,STATUS,REMARKS,CONTRACTOR_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE,"
								+ " REQUEST_PENDING_DEPT_ID,REQUEST_PENDING_EMP_ID,SITEWISE_PAYMENT_NO,PARTICULAR,PAYMENT_REQ_DATE) "
								+ "select  '"+contractorPKSeqNumber+"','" + accCntPaymentSeq+ "',sysdate,CERTIFIED_AMOUNT,PAYBLE_AMOUNT,PAYMENT_REQ_DATE ,"
								+ " (select TOTAL_WO_AMOUNT from QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+ changedBill.getWorkOrderNo()+ "'  and SITE_ID='"+changedBill.getSiteId()+"' )"
								+ ",(select  to_date(QS_WORKORDER_DATE,'dd-MM-yyyy')  from QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+ changedBill.getWorkOrderNo() + "'  and SITE_ID='"+changedBill.getSiteId()+"'),'" + changedBill.getWorkOrderNo()+ "',SITE_ID,'A','REMARKS REGULAR',CONTRACTOR_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE," + " '-','" + strNextLevelApprove	+ "','" + siteWisePaymentNo + "','REGULAR',PAYMENT_REQ_DATE "
								+ " from QS_CONTRACTOR_BILL where QS_WORKORDER_ISSUE_ID =(select QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE WHERE WORK_ORDER_NUMBER='"+workOrderNo+"' and SITE_ID='"+changedBill.getSiteId()+"' )  and BILL_ID='" + contractorBillSeq + "' and PAYMENT_TYPE='"+ changedBill.getPaymentType() + "'";
				int	accResult = jdbcTemplate.update(queryForAccPayment);
				System.out.println(accResult+" accResult");
				//updating the bill rate
				String updatePrice="update QS_INV_AGN_WORK_PMT_DTL_HR QSINA "
						+ " set QSINA.RATE =(select (TOTAL_AMOUNT/QUANTITY) from QS_WORKORDER_ISSUE_DETAILS QSID "
						+ " where  QSINA.WO_MINORHEAD_ID=QSID.WO_MINORHEAD_ID AND  QSINA.WO_WORK_DESC_ID=QSID.WO_WORK_DESC_ID  and QSINA.WO_ROW_CODE=QSID.WO_ROW_CODE  and QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+ changedBill.getWorkOrderNo()+ "'  and SITE_ID='"+changedBill.getSiteId()+"' )) "
						+ " where QS_WORKORDER_ISSUE_ID = (SELECT QS_WORKORDER_ISSUE_ID FROM QS_WORKORDER_ISSUE where WORK_ORDER_NUMBER='"+ changedBill.getWorkOrderNo()+ "'  and SITE_ID='"+changedBill.getSiteId()+"' ) and QSINA.BILL_ID='"+contractorBillSeq+"' ";
				accResult = jdbcTemplate.update(updatePrice);
				
				String s=null;
				//s.trim();
				//contractorBillSeq is holding the permanent bill number this permanent bill number we need to show in view page
				changedBill.setBillNo(contractorBillSeq);
		} else {
				//updating bill with next leval approval id and payable amount etc..
			query = "UPDATE QS_CONTRACTOR_BILL_TEMP SET WORK_BLOCK_NAMES=?,PENDING_EMP_ID=?,PAYBLE_AMOUNT=?,CERTIFIED_AMOUNT=?,DEDUCTION_AMOUNT=? WHERE TEMP_BILL_ID=?";
				Object[] queryParams = { changedBill.getNMRBillBlocks(),changedBill.getApproverEmpId(), changedBill.getPaybleAmt(),
						changedBill.getTotalAmtCumulativeCertified(),changedBill.getTotalCurrntDeducAmt(),
						changedBill.getTempBillNo() };
				count = jdbcTemplate.update(query, queryParams);
		}
		return count;
	}
	
	/**
	 * @description this method is updating the NMR bill status
	 * after updating the NMR status it won't go to next level
	 */
	@Override
	public int rejectNMRBill(ContractorQSBillBean changedBill) {
		String	query = "UPDATE QS_CONTRACTOR_BILL_TEMP SET PENDING_EMP_ID=?,STATUS='R' WHERE TEMP_BILL_ID=?";
		Object[] queryParams = { changedBill.getUserId(),changedBill.getTempBillNo() };
		int count = jdbcTemplate.update(query, queryParams);
		return count;
	}
	
	/**
	 * @description this method is for updating the remarks of the NMR details popup row using the temp bill no and NMRROWNUM
	 */
	@Override
	public int updateNMRROWDataRemarks(ContractorQSBillBean changedNMRBillData) {
		int result=0;
		//String tempWorkPMT_DTL_ID=changedNMRBillData.getTempWorkPMTDtlId();
		String query="Update QS_TEMP_INV_AGN_WORK_PMT_DTL_HR set REMARKS=? where TEMP_BILL_ID=?  and NMRROWNUM=?";
		Object[] params={changedNMRBillData.getRemarks(),changedNMRBillData.getTempBillNo(),changedNMRBillData.getNMRRowIndex1()};
	
		result=jdbcTemplate.update(query,params);
		return result;
	}
	/**
	 * @description this method used for updating the NMR details popup row data if changed is happens
	 */
	@Override
	public int updateNMRROWData(ContractorQSBillBean changedNMRBillData) {
		int result=0;
		//String tempWorkPMT_DTL_ID=changedNMRBillData.getTempWorkPMTDtlId();
		String query="Update QS_TEMP_INV_AGN_WORK_PMT_DTL_HR set ALLOCATED_QTY=?,WORK_DAYS=?,FROM_TIME=?,"
				+ "NO_OF_HOURS=?,TO_TIME=? where  WO_MINORHEAD_ID=?  and WO_WORK_DESC_ID=? and trunc(WORK_DATE) = to_date(?,'dd-MM-yy')  and TEMP_BILL_ID=?  and NMRROWNUM=?";
		Object[] params={changedNMRBillData.getNoOfworkers1(),changedNMRBillData.getWorkDays1(),
				changedNMRBillData.getFromTime1(),changedNMRBillData.getNoOfHours1(),changedNMRBillData.getToTime1(),
				changedNMRBillData.getMinorHeadId1(),changedNMRBillData.getWorkDescId1(),changedNMRBillData.getWorkDate1(),changedNMRBillData.getTempBillNo(),changedNMRBillData.getNMRRowIndex1()};
	
		result=jdbcTemplate.update(query,params);
		return result;
	}
}
