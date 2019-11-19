package com.sumadhura.transdao;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.UIProperties;

@Repository
public class ContractorPaymentProcessDaoImpl extends UIProperties implements ContractorPaymentProcessDao{
	
	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;


	@Override
	public List<PaymentBean> getAccDeptContractorPaymentPendingDetails(String strUserId) {
		
		//Because we changed dept id 997 to 997_B_1 & 997_H_1  , so we have to know which dept id is specifically.
		String  accDeptId = jdbcTemplate.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+strUserId+"'", String.class);
		
		
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		List<Map<String, Object>> paymentDtls = null;
		double workOrderAmount=0.0;
		int i = 0;

		String query = "select ACADPP.ACCOUNTS_DEPT_PMT_PROSS_ID,ACADPP.ACCOUNT_DEPT_REQ_REC_AMOUNT,ACADPP.ALLOCATED_AMOUNT,"
					 + "ACADPP.CREATION_DATE,ACADPP.CNT_PAYMENT_ID,ACADPP.PAYMENT_REQ_SITE_ID,ACADPP.PENDING_AMOUNT,"
					 + "ACADPP.PROCESS_INTIATE_AMOUNT,ACADPP.REQ_RECEIVE_FROM,ACP.CREATED_DATE,"
					 + "ACP.PAYMENT_REQ_AMOUNT,ACP.WO_AMOUNT,ACP.WO_DATE,ACP.WO_NUMBER,"
					 + "ACP.BILL_DATE,ACP.BILL_NUMBER,ACP.BILL_AMOUNT,"
					 + "ACP.REMARKS,ACP.SITE_ID,ACADPP.STATUS,ACP.CONTRACTOR_ID,ACP.BILL_ID,ACP.PAYMENT_TYPE,ACP.REQUEST_PENDING_DEPT_ID,"
					 + "ACP.REQUEST_PENDING_EMP_ID,ACP.SITEWISE_PAYMENT_NO,ACP.PARTICULAR,ACP.PAYMENT_REQ_DATE,SC.FIRST_NAME||' '||SC.LAST_NAME as CONTRACTOR_NAME,"
					 + "S.SITE_NAME,QCB.DEDUCTION_AMOUNT,ACP.QS_WORKORDER_ISSUE_ID, "
					 + ""
					 + ""
					 /*=============  to get remarks from QS_CONT_TMP_BILL_APPR_REJ_DTLS  ===========*/
					 + "(SELECT LISTAGG(CONCAT(CONCAT(SED1.EMP_NAME,'  :  '),QCTBARD.REMARKS), '@@@')"
					 + "WITHIN GROUP (ORDER BY QCTBARD.QS_BILL_APPR_REJ_DTLS) "
					 + "from QS_CONT_TMP_BILL_APPR_REJ_DTLS QCTBARD,QS_CONTRACTOR_BILL QCB1,SUMADHURA_EMPLOYEE_DETAILS SED1 " 	
					 + "where SED1.EMP_ID = QCTBARD.EMP_ID and QCTBARD.TEMP_BILL_ID = QCB1.TEMP_BILL_ID and QCB1.BILL_ID = ACP.BILL_ID and QCB1.CONT_SEQ_BILL_NO = ACP.BILL_NUMBER and regexp_like(QCTBARD.REMARKS, '[a-zA-Z0-9]')) as QS_APR_REJ_REMARKS , "
					 + ""
					 /*=============  to get remarks from ACC_CNT_PMT_SITE_APPR_REJ_DTLS  ===========*/
					 + "(SELECT LISTAGG(CONCAT(CONCAT(SED3.EMP_NAME,'  :  '),ACPSARD.REMARKS), '@@@')"
					 + "WITHIN GROUP (ORDER BY ACPSARD.PAYMENT_APROV_REJECT_SEQ) "
					 + "from ACC_CNT_PMT_SITE_APPR_REJ_DTLS ACPSARD,SUMADHURA_EMPLOYEE_DETAILS SED3 "
					 + "where SED3.EMP_ID = ACPSARD.EMP_ID and ACPSARD.CNT_PAYMENT_ID = ACADPP.CNT_PAYMENT_ID and regexp_like(ACPSARD.REMARKS, '[a-zA-Z0-9]') ) as SITE_PMT_APP_REJ_REMARKS ,  "
					 + ""
					 /*=============  to get remarks from ACC_CNT_PMT_APPR_REJECT_DTLS  ===========*/
					 + "(SELECT LISTAGG(CONCAT(CONCAT(SED2.EMP_NAME,'  :  '),ACPARD.REMARKS), '@@@')"
					 + "WITHIN GROUP (ORDER BY ACPARD.PAYMENT_APROV_REJECT_SEQ) " 
					 + "from ACC_CNT_PMT_APPR_REJECT_DTLS ACPARD,SUMADHURA_EMPLOYEE_DETAILS SED2 "
					 + "where SED2.EMP_ID = ACPARD.EMP_ID and ACPARD.CNT_PAYMENT_ID = ACADPP.CNT_PAYMENT_ID and regexp_like(ACPARD.REMARKS, '[a-zA-Z0-9]') ) as ACC_APP_REJ_REMARKS  "
					 + ""
					 + ""
					 + ""
					 + "from ACC_CNT_ACCOUNTS_DEPT_PMT_PROSS ACADPP,ACC_CNT_PAYMENT ACP,QS_CONTRACTOR_BILL QCB,SUMADHURA_CONTRACTOR SC,SITE S  "
					 + "where ACP.CNT_PAYMENT_ID = ACADPP.CNT_PAYMENT_ID and ACP.CONTRACTOR_ID = SC.CONTRACTOR_ID "
					 + "and QCB.BILL_ID = ACP.BILL_ID and QCB.CONT_SEQ_BILL_NO = ACP.BILL_NUMBER and ACP.SITE_ID = S.SITE_ID "
					 + "and ACP.REQUEST_PENDING_DEPT_ID = ? and ACADPP.STATUS = 'A' "
					 + "order by CONTRACTOR_NAME asc, S.SITE_NAME asc, ACADPP.CREATION_DATE asc ";
					//In query we are taking results as per ContractorName & site name ascending order to do grouping easily.
					//WARNING: If results are not in contractor name & site name ascending order, below code does not work.
		paymentDtls = jdbcTemplate.queryForList(query, new Object[] {accDeptId});
		int contractorGroupSerialNo = 0;
		String currentContractorId="-";
		String currentSiteId="-";
		List<PaymentBean> raBillNoList = new ArrayList<PaymentBean>();
		List<String>workOrderNoList = new ArrayList<String>();
		for(Map<String, Object> paymentReqs : paymentDtls) {
			
			double reqRecAmount = Double.valueOf(paymentReqs.get("ACCOUNT_DEPT_REQ_REC_AMOUNT")==null ? "0" :   paymentReqs.get("ACCOUNT_DEPT_REQ_REC_AMOUNT").toString());
			double procIntiateAmount = Double.valueOf(paymentReqs.get("PROCESS_INTIATE_AMOUNT")==null ? "0" :   paymentReqs.get("PROCESS_INTIATE_AMOUNT").toString());
			if(reqRecAmount<=procIntiateAmount){
				if(reqRecAmount==0){
					try{jdbcTemplate.queryForObject("select CNT_PAYMENT_ID from ACC_CNT_TEMP_PAYMENT_TRANSACTIONS where CNT_PAYMENT_ID = ? ", new Object[]{paymentReqs.get("CNT_PAYMENT_ID")},String.class);
						continue;
					} catch(EmptyResultDataAccessException e){}
				}
				else{
					continue;
				}
			}
			i = i+1;
			String contractorId = paymentReqs.get("CONTRACTOR_ID")==null ? "" : paymentReqs.get("CONTRACTOR_ID").toString();
			String contractorName = paymentReqs.get("CONTRACTOR_NAME")==null ? "" : paymentReqs.get("CONTRACTOR_NAME").toString();
			String site_id = paymentReqs.get("SITE_ID")==null ? "" : paymentReqs.get("SITE_ID").toString();
			String site_name = paymentReqs.get("SITE_NAME")==null ? "" : paymentReqs.get("SITE_NAME").toString();
			String workOrderNo = paymentReqs.get("WO_NUMBER")==null ? "" : paymentReqs.get("WO_NUMBER").toString();
			double reqAmount = reqRecAmount-procIntiateAmount;
			workOrderAmount = Double.valueOf(paymentReqs.get("WO_AMOUNT")==null ? "0" : paymentReqs.get("WO_AMOUNT").toString());
			
			String billNumber = paymentReqs.get("BILL_NUMBER")==null ? "" : paymentReqs.get("BILL_NUMBER").toString();
			double billAmount = Double.valueOf(paymentReqs.get("BILL_AMOUNT")==null ? "0" : paymentReqs.get("BILL_AMOUNT").toString());
			String billDate = paymentReqs.get("BILL_DATE")==null ? "" : paymentReqs.get("BILL_DATE").toString();
			String paymentType = paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString();
			
			String raBillNo,advBillNo,sdBillNo,nmrBillNo,raBillDate,advBillDate,sdBillDate,nmrBillDate;
			raBillNo=advBillNo=sdBillNo=nmrBillNo=raBillDate=advBillDate=sdBillDate=nmrBillDate = "";
			double raBillAmount,advBillAmount,sdBillAmount,nmrBillAmount;
			raBillAmount=advBillAmount=sdBillAmount=nmrBillAmount = 0;
			if(paymentType.equals("RA")){
				raBillNo = billNumber;
				raBillAmount = billAmount; 
				raBillDate = billDate;
			}
			if(paymentType.equals("ADV")){
				advBillNo = billNumber;
				advBillAmount = billAmount;  
				advBillDate = billDate;
			}
			if(paymentType.equals("SEC")){
				sdBillNo = billNumber;
				sdBillAmount = billAmount; 
				sdBillDate = billDate; 
			}
			if(paymentType.equals("NMR")){
				nmrBillNo = billNumber;
				nmrBillAmount = billAmount; 
				nmrBillDate = billDate;
			}
			
			
			// if contractor & site is different, creating separate Group object storing details of whole payments related to that contractor & add it to list.
			if(!currentContractorId.equals(contractorId)||!currentSiteId.equals(site_id))
			{
				contractorGroupSerialNo++;
				//this object stores for contractorGroup details.
				PaymentBean indentObj = new PaymentBean();
				indentObj.setStrContractorId(contractorId);
				indentObj.setStrContractorName(contractorName);
				indentObj.setStrSiteId(site_id);
				indentObj.setStrSiteName(site_name);
				indentObj.setContractorHeader(true);
				indentObj.setContractorGroupSerialNo(contractorGroupSerialNo);
				list.add(indentObj);
				currentContractorId=contractorId;
				currentSiteId=site_id;

			}
			//taking contractorGroup object related to this contractor in list.
			//add invoice amount of this contractor, to the contractorGroup object
			for(PaymentBean e:list){
				if(e.isContractorHeader()&&e.getStrContractorId().equals(contractorId)&&e.getStrSiteId().equals(site_id)){
					e.setIntNoofPaymentsVendorWise(e.getIntNoofPaymentsVendorWise()+1);
					//add req amount of this contractor, to the contractorGroup object
					e.setRequestedAmount(String.format("%.2f",(Double.valueOf(e.getRequestedAmount()==null?"0":e.getRequestedAmount())+reqAmount)));
					if(!workOrderNoList.contains(workOrderNo)){
						//add workOrderAmount of this contractor, to the contractorGroup object
						double additionOfWoAmount = e.getDoubleWorkOrderAmount()+workOrderAmount;
						e.setDoubleWorkOrderAmount(additionOfWoAmount);
						e.setStrWorkOrderAmount(String.format("%.2f",additionOfWoAmount));
						workOrderNoList.add(workOrderNo);
					}
										
				}
			}

			PaymentBean objPaymentBean = new PaymentBean();

			objPaymentBean.setIntSerialNo(i);
			objPaymentBean.setContractorGroupSerialNo(contractorGroupSerialNo);
			objPaymentBean.setWorkOrderNo(workOrderNo);
			objPaymentBean.setDoubleAmountToBeReleased(reqAmount);
			objPaymentBean.setStrContractorId(contractorId);
			objPaymentBean.setStrContractorName(contractorName);
			objPaymentBean.setStrSiteId(site_id);
			objPaymentBean.setStrSiteName(site_name);
			objPaymentBean.setDoubleWorkOrderAmount(workOrderAmount);
			objPaymentBean.setStrWorkOrderAmount(String.format("%.2f",workOrderAmount));
			
			objPaymentBean.setIntAccDeptPaymentProcessId(Integer.parseInt(paymentReqs.get("ACCOUNTS_DEPT_PMT_PROSS_ID")==null ? "" : paymentReqs.get("ACCOUNTS_DEPT_PMT_PROSS_ID").toString()));
			objPaymentBean.setIntCntPaymentId(Integer.parseInt(paymentReqs.get("CNT_PAYMENT_ID")==null ? "" :   paymentReqs.get("CNT_PAYMENT_ID").toString()));
			objPaymentBean.setRequestReceiveFrom(paymentReqs.get("REQ_RECEIVE_FROM")==null ? "" : paymentReqs.get("REQ_RECEIVE_FROM").toString());
			String remarks = "";
			String qs_app_rej_remarks = paymentReqs.get("QS_APR_REJ_REMARKS")==null ? "" :   paymentReqs.get("QS_APR_REJ_REMARKS").toString();
			String site_app_rej_remarks = paymentReqs.get("SITE_PMT_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("SITE_PMT_APP_REJ_REMARKS").toString();
			String acc_app_rej_remarks = paymentReqs.get("ACC_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("ACC_APP_REJ_REMARKS").toString();
			if(StringUtils.isNotBlank(qs_app_rej_remarks)){remarks+=qs_app_rej_remarks;}
			if(StringUtils.isNotBlank(site_app_rej_remarks)){
				if(StringUtils.isBlank(remarks)){remarks=site_app_rej_remarks;} else{remarks+="@@@"+site_app_rej_remarks;}
			}
			if(StringUtils.isNotBlank(acc_app_rej_remarks)){
				if(StringUtils.isBlank(remarks)){remarks=acc_app_rej_remarks;} else{remarks+="@@@"+acc_app_rej_remarks;}
			}
			objPaymentBean.setStrRemarks(remarks);
			objPaymentBean.setStrRemarksForView(remarks.replace("@@@", ","));
			objPaymentBean.setStrRemarksForTitle(remarks.replace("@@@", "&#013;"));
			objPaymentBean.setStrBillId(paymentReqs.get("BILL_ID")==null ? "" : paymentReqs.get("BILL_ID").toString());
			objPaymentBean.setQsWorkorderIssueId(paymentReqs.get("QS_WORKORDER_ISSUE_ID")==null ? "" : paymentReqs.get("QS_WORKORDER_ISSUE_ID").toString());
			objPaymentBean.setPaymentType(paymentType);
			
			objPaymentBean.setBillNumber(billNumber);
			objPaymentBean.setDoubleBillAmount(billAmount);
			objPaymentBean.setStrBillAmount(String.format("%.2f",billAmount));
			objPaymentBean.setRaBillNo(raBillNo);
			objPaymentBean.setDoubleRaBillAmount(raBillAmount);
			objPaymentBean.setDoubleAdvBillAmount(advBillAmount);
			objPaymentBean.setStrAdvBillNo(advBillNo);
			objPaymentBean.setDoubleSdBillAmount(sdBillAmount);
			objPaymentBean.setStrSdBillNo(sdBillNo);
			objPaymentBean.setStrNmrBillNo(nmrBillNo);
			objPaymentBean.setDoubleNmrBillAmount(nmrBillAmount);
			
			objPaymentBean.setDoubleDeductionAmount(Double.valueOf(paymentReqs.get("DEDUCTION_AMOUNT")==null ? "0" : paymentReqs.get("DEDUCTION_AMOUNT").toString()));
			String workOrderDate = paymentReqs.get("WO_DATE")==null ? "" : paymentReqs.get("WO_DATE").toString();
			String paymentReqDate = paymentReqs.get("PAYMENT_REQ_DATE")==null ? "" :   paymentReqs.get("PAYMENT_REQ_DATE").toString();
			String paymentRequestReceivedDate = paymentReqs.get("CREATION_DATE")==null ? "" : paymentReqs.get("CREATION_DATE").toString();
			//paymentReqs.get("ALLOCATED_AMOUNT")==null ? "" : paymentReqs.get("ALLOCATED_AMOUNT").toString();
			//paymentReqs.get("PAYMENT_REQ_SITE_ID")==null ? "" : paymentReqs.get("PAYMENT_REQ_SITE_ID").toString();
			//paymentReqs.get("PENDING_AMOUNT")==null ? "" : paymentReqs.get("PENDING_AMOUNT").toString();
			//paymentReqs.get("CREATED_DATE")==null ? "" : paymentReqs.get("CREATED_DATE").toString();
			//paymentReqs.get("STATUS")==null ? "" : paymentReqs.get("STATUS").toString();
			//paymentReqs.get("REQUEST_PENDING_DEPT_ID")==null ? "" : paymentReqs.get("REQUEST_PENDING_DEPT_ID").toString();
			//paymentReqs.get("REQUEST_PENDING_EMP_ID")==null ? "" : paymentReqs.get("REQUEST_PENDING_EMP_ID").toString();
			objPaymentBean.setIntSiteWisePaymentId(Integer.parseInt(paymentReqs.get("SITEWISE_PAYMENT_NO")==null ? "0" : paymentReqs.get("SITEWISE_PAYMENT_NO").toString()));
			    
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			try{
				
				SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat time1 = new SimpleDateFormat("hh:mm aa");
				
				if(!raBillDate.equals("")){

					Date raBill_Date = dt.parse(raBillDate);
					raBillDate = dt1.format(raBill_Date);
					}
				if(!workOrderDate.equals("")){

				Date workOrder_Date = dt.parse(workOrderDate);
				workOrderDate = dt1.format(workOrder_Date);
				}
				if(!advBillDate.equals("")){

					Date advBill_Date = dt.parse(advBillDate);
					advBillDate = dt1.format(advBill_Date);
					}
				if(!sdBillDate.equals("")){

				Date secBill_Date = dt.parse(sdBillDate);
				sdBillDate = dt1.format(secBill_Date);
				}
				if(!nmrBillDate.equals("")){
		    		Date received_date = dt.parse(nmrBillDate);
		    		nmrBillDate = dt1.format(received_date);
		    		}
		    	if(!billDate.equals("")){
		    		Date received_date = dt.parse(billDate);
		    		billDate = dt1.format(received_date);
		    		}
				if(!paymentReqDate.equals("")){

					Date paymentReq_Date = dt.parse(paymentReqDate);
					paymentReqDate = dt1.format(paymentReq_Date);
					}
				if(!paymentRequestReceivedDate.equals("")){

					Date paymentRequest_ReceivedDate = dt.parse(paymentRequestReceivedDate);
					paymentRequestReceivedDate = dt1.format(paymentRequest_ReceivedDate);
					paymentRequestReceivedDate +=" "+ time1.format(paymentRequest_ReceivedDate);
					}
				
			}
		
			catch(Exception e){
				e.printStackTrace();
			}
			objPaymentBean.setStrRaBillDate(raBillDate);
			objPaymentBean.setStrWorkOrderDate(workOrderDate);
			objPaymentBean.setStrAdvBillDate(advBillDate);
			objPaymentBean.setStrSdBillDate(sdBillDate);
			objPaymentBean.setStrNmrBillDate(nmrBillDate);
		    objPaymentBean.setStrBillDate(billDate);
		    objPaymentBean.setStrPaymentReqDate(paymentReqDate);
			objPaymentBean.setStrPaymentRequestReceivedDate(paymentRequestReceivedDate);			
			
			list.add(objPaymentBean);
		}
		return list;
	}
	


	@Override
	public String getApproverEmpIdInAccounts(String strEmpId) {

		List<Map<String, Object>> pendingEmpId = null;
		String strPendingEmpId = "";
		try {
			jdbcTemplate = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query = "select APPROVER_EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? and STATUS='A' and MODULE_TYPE = 'ACCOUNTS'";

		pendingEmpId = jdbcTemplate.queryForList(query, new Object[] {strEmpId});
		for(Map<String, Object> pendingEmployeeId : pendingEmpId) {

			strPendingEmpId = pendingEmployeeId.get("APPROVER_EMP_ID")==null ? "" :   pendingEmployeeId.get("APPROVER_EMP_ID").toString();
			}
			
		
		return strPendingEmpId;
	}

	@Override
	public String getApproverDeptIdInAccounts(String strEmpId) {

		List<Map<String, Object>> pendingEmpId = null;
		String strPendingDeptId = "";
		try {
			jdbcTemplate = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query = "select APPROVER_DEPT_ID from SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? and STATUS='A' and MODULE_TYPE = 'ACCOUNTS'";

		pendingEmpId = jdbcTemplate.queryForList(query, new Object[] {strEmpId});
		for(Map<String, Object> pendingEmployeeId : pendingEmpId) {
			strPendingDeptId = pendingEmployeeId.get("APPROVER_DEPT_ID")==null ? "" :   pendingEmployeeId.get("APPROVER_DEPT_ID").toString();
		}
		return strPendingDeptId;
	}

	@Override
	public int getAccCntTempPaymentTransactionSeqNo() {

		String query = "select  ACC_CNT_TEMP_PMT_TRANS_SEQ.nextval from dual";

		return jdbcTemplate.queryForInt(query);
		
		
	}
	@Override
	public int getAccCntPaymentTransactionSeqNo() {

		String query = "select  ACC_CNT_PMT_TRANSACTIONS_SEQ.nextval from dual";

		return jdbcTemplate.queryForInt(query);
		
		
	}

	@Override
	public int insertCntTempPaymentTransactionsTbl(PaymentBean objPaymentBean, int intTempPaymentTransactionId) {
		String query = " insert into ACC_CNT_TEMP_PAYMENT_TRANSACTIONS ( TEMP_PAYMENT_TRANSACTIONS_ID, ACCOUNTS_DEPT_PMT_PROSS_ID, "
						+ " CREATION_DATE, CNT_PAYMENT_ID, PAYMENT_MODE, PAYMENT_REQ_DATE, REMARKS, "
						+ " REQUEST_PENDING_EMP_ID, REQ_AMOUNT, STATUS, UTR_CHQNO, TRANSACTION_TYPE ) "
						+ "values ( ?, ?, sysdate, ?, ?, ?, ?, ?, ?, ?, ?, ? ) ";
		
				int result = jdbcTemplate.update(query,new Object[]{
							intTempPaymentTransactionId, objPaymentBean.getIntAccDeptPaymentProcessId(), 
							objPaymentBean.getIntCntPaymentId(), objPaymentBean.getPaymentMode(),
							DateUtil.convertToJavaDateFormat(objPaymentBean.getStrPaymentReqDate()), objPaymentBean.getStrRemarks(), 
							objPaymentBean.getStrPendingDeptId(), objPaymentBean.getDoubleAmountToBeReleased(),
							"A", objPaymentBean.getUtrChequeNo(), objPaymentBean.getPaymentType()});
		
		return result;
	}

	@Override
	public int updateCntAccDeptPaymentProcsstbl(PaymentBean objPaymentBean) {
		String query = " update ACC_CNT_ACCOUNTS_DEPT_PMT_PROSS set PROCESS_INTIATE_AMOUNT = PROCESS_INTIATE_AMOUNT+? "
						+ " where ACCOUNTS_DEPT_PMT_PROSS_ID = ? ";

				int result = jdbcTemplate.update(query, new Object[] {
						objPaymentBean.getDoubleAmountToBeReleased(),
						objPaymentBean.getIntAccDeptPaymentProcessId()
				});
				
		return result;
	}

	@Override
	public int saveCntAccountApprovalDetails(PaymentBean objPaymentBean, String strUserId, int intTempPaymentTransactionId, String siteId) {
		String query = " insert into ACC_CNT_PMT_APPR_REJECT_DTLS ( PAYMENT_APROV_REJECT_SEQ, ACCOUNTS_DEPT_PMT_PROSS_ID, "
						+ "TEMP_PAYMENT_TRANSACTIONS_ID, EMP_ID, STATUS, REMARKS, SITE_ID, CREATED_DATE, OPERATION_TYPE, CNT_PAYMENT_ID)"
						+ " values ( ACC_CNT_PMT_APPR_REJECT_SEQ_ID.nextval, ?, ?, ?, ?, ?, ?, sysdate, ?, ? )";
		
				int result = jdbcTemplate.update(query,new Object[]{
						objPaymentBean.getIntAccDeptPaymentProcessId(),
						intTempPaymentTransactionId,strUserId,"A",objPaymentBean.getStrRemarks(), siteId, "A", objPaymentBean.getIntCntPaymentId()
				});
		return result;
	}

	@Override
	public int setInactiveRowInCntAccDeptPmtProcessTbl(int accDeptPmtProcessId) {
		int count=0;
		
		String query = "update ACC_CNT_ACCOUNTS_DEPT_PMT_PROSS set STATUS='I'  where ACCOUNTS_DEPT_PMT_PROSS_ID = ?";
		count = jdbcTemplate.update(query, new Object[] {
				accDeptPmtProcessId
				});
		
		return count;
	}

	@Override
	public int saveCntAccountRejectDetails(PaymentBean objPaymentBean, String strUserId, String siteId) {
		String query = " insert into ACC_CNT_PMT_APPR_REJECT_DTLS ( PAYMENT_APROV_REJECT_SEQ, ACCOUNTS_DEPT_PMT_PROSS_ID,TEMP_PAYMENT_TRANSACTIONS_ID, "
				+ " EMP_ID, STATUS, REMARKS, SITE_ID, CREATED_DATE, OPERATION_TYPE, CNT_PAYMENT_ID)"
				+ " values ( ACC_CNT_PMT_APPR_REJECT_SEQ_ID.nextval, ?, ?, ?, ?, ?, ?,sysdate, ?, ? )";

		int result = jdbcTemplate.update(query,new Object[]{
				objPaymentBean.getIntAccDeptPaymentProcessId(),objPaymentBean.getIntTempPaymentTransactionId(),
				strUserId,"A",objPaymentBean.getStrRemarks(), siteId, "R", objPaymentBean.getIntCntPaymentId()
		});
		return result;
	}
	
	@Override
	public String[] getEmailsOfEmpByDeptId(String pendingDeptId) {
		List<String> emailList = new ArrayList<String>();
		
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select SED.EMP_EMAIL  from SUMADHURA_EMPLOYEE_DETAILS SED where SED.DEPT_ID = ? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{pendingDeptId});
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
	public String[] getEmailsOfEmpByEmpId(String empId) {
		List<String> emailList = new ArrayList<String>();
		
		List<Map<String, Object>> dbIndentDts = null;
		String query = "select SED.EMP_EMAIL  from SUMADHURA_EMPLOYEE_DETAILS SED where SED.EMP_ID = ? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{empId});
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
	public int setInactiveRowInAccCntPaymentTbl(int cntPaymentId) {
		int count=0;
		
		String query = "update ACC_CNT_PAYMENT set STATUS='I'  where CNT_PAYMENT_ID = ?";
		count = jdbcTemplate.update(query, new Object[] {
				cntPaymentId
				});
		
		return count;
	}

	@Override
	public int setInactiveRowInQsContractorBill(String billId,String qsWorkorderIssueId,String siteId) {
		int count=0;
		
		String query = "update QS_CONTRACTOR_BILL set STATUS='R'  where BILL_ID = ? and QS_WORKORDER_ISSUE_ID = ? and SITE_ID = ?";
		count = jdbcTemplate.update(query, new Object[] {
				billId, qsWorkorderIssueId, siteId
				});
		
		return count;
	}

	@Override
	public List<PaymentBean> getCntAccDeptPaymentApprovalDetails(String siteId, String strUserId) {

		//Because we changed dept id 997 to 997_B_1 & 997_H_1  , so we have to know which dept id is specifically.
		String  accDeptId = jdbcTemplate.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+strUserId+"'", String.class);


		List<PaymentBean> list = new ArrayList<PaymentBean>();
		List<Map<String, Object>> paymentDtls = null;
		double workOrderAmount=0.0;
		int i = 0;
		String query = " select ACTPT.TEMP_PAYMENT_TRANSACTIONS_ID,ACTPT.ACCOUNTS_DEPT_PMT_PROSS_ID,ACTPT.CNT_PAYMENT_ID,"
				+ " ACTPT.PAYMENT_MODE,ACTPT.PAYMENT_REQ_DATE,ACTPT.REQ_AMOUNT,ACTPT.UPDATE_DATE,ACTPT.UTR_CHQNO,"
				+ " ACTPT.TRANSACTION_TYPE,ACADPP.CREATION_DATE,ACADPP.PAYMENT_REQ_SITE_ID,"
				+ " ACP.PAYMENT_REQ_AMOUNT,ACP.WO_AMOUNT,ACP.WO_DATE,ACP.WO_NUMBER,"
				 + "ACP.BILL_DATE,ACP.BILL_NUMBER,ACP.BILL_AMOUNT,"
				+ " ACP.SITE_ID,ACP.CONTRACTOR_ID,ACP.BILL_ID,ACP.QS_WORKORDER_ISSUE_ID,ACP.PAYMENT_TYPE,ACP.SITEWISE_PAYMENT_NO,ACP.PARTICULAR,"
				+ " S.SITE_NAME,SC.FIRST_NAME||' '||SC.LAST_NAME as CONTRACTOR_NAME,APM.NAME as PAYMENT_MODE_NAME,QCB.DEDUCTION_AMOUNT,QCB.TEMP_BILL_ID,  "
				+ ""
				 + ""
				 /*=============  to get remarks from QS_CONT_TMP_BILL_APPR_REJ_DTLS  ===========*/
				 + "(SELECT LISTAGG(CONCAT(CONCAT(SED1.EMP_NAME,'  :  '),QCTBARD.REMARKS), '@@@')"
				 + "WITHIN GROUP (ORDER BY QCTBARD.QS_BILL_APPR_REJ_DTLS) "
				 + "from QS_CONT_TMP_BILL_APPR_REJ_DTLS QCTBARD,QS_CONTRACTOR_BILL QCB1,SUMADHURA_EMPLOYEE_DETAILS SED1 " 	
				 + "where SED1.EMP_ID = QCTBARD.EMP_ID and QCTBARD.TEMP_BILL_ID = QCB1.TEMP_BILL_ID and QCB1.BILL_ID = ACP.BILL_ID and QCB1.CONT_SEQ_BILL_NO = ACP.BILL_NUMBER and regexp_like(QCTBARD.REMARKS, '[a-zA-Z0-9]')) as QS_APR_REJ_REMARKS , "
				 + ""
				 /*=============  to get remarks from ACC_CNT_PMT_SITE_APPR_REJ_DTLS  ===========*/
				 + "(SELECT LISTAGG(CONCAT(CONCAT(SED3.EMP_NAME,'  :  '),ACPSARD.REMARKS), '@@@')"
				 + "WITHIN GROUP (ORDER BY ACPSARD.PAYMENT_APROV_REJECT_SEQ) "
				 + "from ACC_CNT_PMT_SITE_APPR_REJ_DTLS ACPSARD,SUMADHURA_EMPLOYEE_DETAILS SED3 "
				 + "where SED3.EMP_ID = ACPSARD.EMP_ID and ACPSARD.CNT_PAYMENT_ID = ACADPP.CNT_PAYMENT_ID and regexp_like(ACPSARD.REMARKS, '[a-zA-Z0-9]') ) as SITE_PMT_APP_REJ_REMARKS ,  "
				 + ""
				/*=============  to get remarks from ACC_CNT_PMT_APPR_REJECT_DTLS  ===========*/
				 + "(SELECT LISTAGG(CONCAT(CONCAT(SED2.EMP_NAME,'  :  '),ACPARD.REMARKS), '@@@')"
				 + "WITHIN GROUP (ORDER BY ACPARD.PAYMENT_APROV_REJECT_SEQ) " 
				 + "from ACC_CNT_PMT_APPR_REJECT_DTLS ACPARD,SUMADHURA_EMPLOYEE_DETAILS SED2 "
				 + "where SED2.EMP_ID = ACPARD.EMP_ID and ACPARD.TEMP_PAYMENT_TRANSACTIONS_ID = ACTPT.TEMP_PAYMENT_TRANSACTIONS_ID and regexp_like(ACPARD.REMARKS, '[a-zA-Z0-9]') ) as ACC_APP_REJ_REMARKS  "
				 + ""
				 + ""
				 + ""
				+ " from ACC_CNT_TEMP_PAYMENT_TRANSACTIONS ACTPT LEFT OUTER JOIN ACC_PAYMENT_MODES APM on APM.VALUE = ACTPT.PAYMENT_MODE ,"
				+ " ACC_CNT_ACCOUNTS_DEPT_PMT_PROSS ACADPP,ACC_CNT_PAYMENT ACP,"
				+ " QS_CONTRACTOR_BILL QCB,SITE S,SUMADHURA_CONTRACTOR SC "
				+ " where ACTPT.STATUS = 'A' and ACTPT.REQUEST_PENDING_EMP_ID = '"+accDeptId+"' "
				+ " and ACTPT.ACCOUNTS_DEPT_PMT_PROSS_ID = ACADPP.ACCOUNTS_DEPT_PMT_PROSS_ID "
				+ " and ACTPT.CNT_PAYMENT_ID = ACP.CNT_PAYMENT_ID and ACP.SITE_ID = S.SITE_ID "
				+ " and ACP.CONTRACTOR_ID = SC.CONTRACTOR_ID and QCB.BILL_ID = ACP.BILL_ID  " 
				+ " and QCB.CONT_SEQ_BILL_NO = ACP.BILL_NUMBER "
				+ " order by CONTRACTOR_NAME asc, S.SITE_NAME asc, ACADPP.CREATION_DATE asc ";
				//In query we are taking results as per ContractorName & site name ascending order to do grouping easily.
				//WARNING: If results are not in contractor name & site name ascending order, below code does not work.
		paymentDtls = jdbcTemplate.queryForList(query, new Object[] {});
		int contractorGroupSerialNo = 0;
		String currentContractorId="-";
		String currentSiteId="-";
		List<PaymentBean> raBillNoList = new ArrayList<PaymentBean>();
		List<String>workOrderNoList = new ArrayList<String>();
		for(Map<String, Object> paymentReqs : paymentDtls) {

			i = i+1;
			String contractorId = paymentReqs.get("CONTRACTOR_ID")==null ? "" : paymentReqs.get("CONTRACTOR_ID").toString();
			String contractorName = paymentReqs.get("CONTRACTOR_NAME")==null ? "" : paymentReqs.get("CONTRACTOR_NAME").toString();
			String site_id = paymentReqs.get("SITE_ID")==null ? "" : paymentReqs.get("SITE_ID").toString();
			String site_name = paymentReqs.get("SITE_NAME")==null ? "" : paymentReqs.get("SITE_NAME").toString();
			String workOrderNo = paymentReqs.get("WO_NUMBER")==null ? "" : paymentReqs.get("WO_NUMBER").toString();
			double reqAmount = Double.valueOf(paymentReqs.get("REQ_AMOUNT")==null ? "" : paymentReqs.get("REQ_AMOUNT").toString());
			workOrderAmount = Double.valueOf(paymentReqs.get("WO_AMOUNT")==null ? "0" : paymentReqs.get("WO_AMOUNT").toString());

			String billNumber = paymentReqs.get("BILL_NUMBER")==null ? "" : paymentReqs.get("BILL_NUMBER").toString();
			double billAmount = Double.valueOf(paymentReqs.get("BILL_AMOUNT")==null ? "0" : paymentReqs.get("BILL_AMOUNT").toString());
			String billDate = paymentReqs.get("BILL_DATE")==null ? "" : paymentReqs.get("BILL_DATE").toString();
			String paymentType = paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString();
			
			String raBillNo,advBillNo,sdBillNo,nmrBillNo,raBillDate,advBillDate,sdBillDate,nmrBillDate;
			raBillNo=advBillNo=sdBillNo=nmrBillNo=raBillDate=advBillDate=sdBillDate=nmrBillDate = "";
			double raBillAmount,advBillAmount,sdBillAmount,nmrBillAmount;
			raBillAmount=advBillAmount=sdBillAmount=nmrBillAmount = 0;
			if(paymentType.equals("RA")){
				raBillNo = billNumber;
				raBillAmount = billAmount; 
				raBillDate = billDate;
			}
			if(paymentType.equals("ADV")){
				advBillNo = billNumber;
				advBillAmount = billAmount;  
				advBillDate = billDate;
			}
			if(paymentType.equals("SEC")){
				sdBillNo = billNumber;
				sdBillAmount = billAmount; 
				sdBillDate = billDate; 
			}
			if(paymentType.equals("NMR")){
				nmrBillNo = billNumber;
				nmrBillAmount = billAmount; 
				nmrBillDate = billDate;
			}
			
			// if contractor & site is different, creating separate Group object storing details of whole payments related to that contractor & add it to list.
			if(!currentContractorId.equals(contractorId)||!currentSiteId.equals(site_id))
			{
				contractorGroupSerialNo++;
				//this object stores for contractorGroup details.
				PaymentBean indentObj = new PaymentBean();
				indentObj.setStrContractorId(contractorId);
				indentObj.setStrContractorName(contractorName);
				indentObj.setStrSiteId(site_id);
				indentObj.setStrSiteName(site_name);
				indentObj.setContractorHeader(true);
				indentObj.setContractorGroupSerialNo(contractorGroupSerialNo);
				list.add(indentObj);
				currentContractorId=contractorId;
				currentSiteId=site_id;

			}
			//taking contractorGroup object related to this contractor in list.
			//add invoice amount of this contractor, to the contractorGroup object
			for(PaymentBean e:list){
				if(e.isContractorHeader()&&e.getStrContractorId().equals(contractorId)&&e.getStrSiteId().equals(site_id)){
					e.setIntNoofPaymentsVendorWise(e.getIntNoofPaymentsVendorWise()+1);
					//add req amount of this contractor, to the contractorGroup object
					e.setRequestedAmount(String.format("%.2f",(Double.valueOf(e.getRequestedAmount()==null?"0":e.getRequestedAmount())+reqAmount)));
					if(!workOrderNoList.contains(workOrderNo)){
						//add workOrderAmount of this contractor, to the contractorGroup object
						double additionOfWoAmount = e.getDoubleWorkOrderAmount()+workOrderAmount;
						e.setDoubleWorkOrderAmount(additionOfWoAmount);
						e.setStrWorkOrderAmount(String.format("%.2f",additionOfWoAmount));
						workOrderNoList.add(workOrderNo);
					}
					
					
				}
			}

			PaymentBean objPaymentBean = new PaymentBean();

			objPaymentBean.setIntSerialNo(i);
			objPaymentBean.setContractorGroupSerialNo(contractorGroupSerialNo);
			objPaymentBean.setWorkOrderNo(workOrderNo);
			objPaymentBean.setDoubleAmountToBeReleased(reqAmount);
			objPaymentBean.setStrContractorId(contractorId);
			objPaymentBean.setStrContractorName(contractorName);
			objPaymentBean.setStrSiteId(site_id);
			objPaymentBean.setStrSiteName(site_name);
			objPaymentBean.setDoubleWorkOrderAmount(workOrderAmount);
			objPaymentBean.setStrWorkOrderAmount(String.format("%.2f",workOrderAmount));
			
			objPaymentBean.setIntTempPaymentTransactionId(Integer.parseInt(paymentReqs.get("TEMP_PAYMENT_TRANSACTIONS_ID")==null ? "" : paymentReqs.get("TEMP_PAYMENT_TRANSACTIONS_ID").toString()));
			objPaymentBean.setIntAccDeptPaymentProcessId(Integer.parseInt(paymentReqs.get("ACCOUNTS_DEPT_PMT_PROSS_ID")==null ? "" : paymentReqs.get("ACCOUNTS_DEPT_PMT_PROSS_ID").toString()));
			objPaymentBean.setIntCntPaymentId(Integer.parseInt(paymentReqs.get("CNT_PAYMENT_ID")==null ? "" :   paymentReqs.get("CNT_PAYMENT_ID").toString()));
			String remarks = "";
			String qs_app_rej_remarks = paymentReqs.get("QS_APR_REJ_REMARKS")==null ? "" :   paymentReqs.get("QS_APR_REJ_REMARKS").toString();
			String site_app_rej_remarks = paymentReqs.get("SITE_PMT_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("SITE_PMT_APP_REJ_REMARKS").toString();
			String acc_app_rej_remarks = paymentReqs.get("ACC_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("ACC_APP_REJ_REMARKS").toString();
			if(StringUtils.isNotBlank(qs_app_rej_remarks)){remarks+=qs_app_rej_remarks;}
			if(StringUtils.isNotBlank(site_app_rej_remarks)){
				if(StringUtils.isBlank(remarks)){remarks=site_app_rej_remarks;} else{remarks+="@@@"+site_app_rej_remarks;}
			}
			if(StringUtils.isNotBlank(acc_app_rej_remarks)){
				if(StringUtils.isBlank(remarks)){remarks=acc_app_rej_remarks;} else{remarks+="@@@"+acc_app_rej_remarks;}
			}
			objPaymentBean.setStrRemarks(remarks);
			objPaymentBean.setStrRemarksForView(remarks.replace("@@@", ","));
			objPaymentBean.setStrRemarksForTitle(remarks.replace("@@@", "&#013;"));
			objPaymentBean.setStrBillId(paymentReqs.get("BILL_ID")==null ? "" : paymentReqs.get("BILL_ID").toString());
			objPaymentBean.setQsWorkorderIssueId(paymentReqs.get("QS_WORKORDER_ISSUE_ID")==null ? "" : paymentReqs.get("QS_WORKORDER_ISSUE_ID").toString());
			objPaymentBean.setStrTempBillId(paymentReqs.get("TEMP_BILL_ID")==null ? "" : paymentReqs.get("TEMP_BILL_ID").toString());
			objPaymentBean.setPaymentType(paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString());
			objPaymentBean.setPaymentType(paymentType);
			objPaymentBean.setBillNumber(billNumber);
			objPaymentBean.setDoubleBillAmount(billAmount);
			objPaymentBean.setStrBillAmount(String.format("%.2f",billAmount));
			
			objPaymentBean.setRaBillNo(raBillNo);
			objPaymentBean.setDoubleRaBillAmount(raBillAmount);
			objPaymentBean.setDoubleAdvBillAmount(advBillAmount);
			objPaymentBean.setStrAdvBillNo(advBillNo);
			objPaymentBean.setDoubleSdBillAmount(sdBillAmount);
			objPaymentBean.setStrSdBillNo(sdBillNo);
			objPaymentBean.setStrNmrBillNo(nmrBillNo);
			objPaymentBean.setDoubleNmrBillAmount(nmrBillAmount);
			
			objPaymentBean.setDoubleDeductionAmount(Double.valueOf(paymentReqs.get("DEDUCTION_AMOUNT")==null ? "0" : paymentReqs.get("DEDUCTION_AMOUNT").toString()));
			String workOrderDate = paymentReqs.get("WO_DATE")==null ? "" : paymentReqs.get("WO_DATE").toString();
			String paymentReqDate = paymentReqs.get("PAYMENT_REQ_DATE")==null ? "" :   paymentReqs.get("PAYMENT_REQ_DATE").toString();
			String paymentRequestReceivedDate = paymentReqs.get("CREATION_DATE")==null ? "" : paymentReqs.get("CREATION_DATE").toString();
			//paymentReqs.get("ALLOCATED_AMOUNT")==null ? "" : paymentReqs.get("ALLOCATED_AMOUNT").toString();
			//paymentReqs.get("PAYMENT_REQ_SITE_ID")==null ? "" : paymentReqs.get("PAYMENT_REQ_SITE_ID").toString();
			//paymentReqs.get("PENDING_AMOUNT")==null ? "" : paymentReqs.get("PENDING_AMOUNT").toString();
			//paymentReqs.get("CREATED_DATE")==null ? "" : paymentReqs.get("CREATED_DATE").toString();
			//paymentReqs.get("STATUS")==null ? "" : paymentReqs.get("STATUS").toString();
			//paymentReqs.get("REQUEST_PENDING_DEPT_ID")==null ? "" : paymentReqs.get("REQUEST_PENDING_DEPT_ID").toString();
			//paymentReqs.get("REQUEST_PENDING_EMP_ID")==null ? "" : paymentReqs.get("REQUEST_PENDING_EMP_ID").toString();
			objPaymentBean.setIntSiteWisePaymentId(Integer.parseInt(paymentReqs.get("SITEWISE_PAYMENT_NO")==null ? "0" : paymentReqs.get("SITEWISE_PAYMENT_NO").toString()));
			objPaymentBean.setUtrChequeNo(paymentReqs.get("UTR_CHQNO")==null ? "" : paymentReqs.get("UTR_CHQNO").toString());
			String paymentMode = paymentReqs.get("PAYMENT_MODE")==null ? "" : paymentReqs.get("PAYMENT_MODE").toString();
			objPaymentBean.setPaymentMode(paymentMode);
			if(StringUtils.isNotBlank(paymentMode)){objPaymentBean.setPaymentModeName(paymentReqs.get("PAYMENT_MODE_NAME")==null ? "" : paymentReqs.get("PAYMENT_MODE_NAME").toString());}
			else{objPaymentBean.setPaymentModeName("--SELECT--");}
			
			
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try{

				SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat time1 = new SimpleDateFormat("hh:mm aa");

				if(!raBillDate.equals("")){

					Date raBill_Date = dt.parse(raBillDate);
					raBillDate = dt1.format(raBill_Date);
				}
				if(!workOrderDate.equals("")){

					Date workOrder_Date = dt.parse(workOrderDate);
					workOrderDate = dt1.format(workOrder_Date);
				}
				if(!advBillDate.equals("")){

					Date advBill_Date = dt.parse(advBillDate);
					advBillDate = dt1.format(advBill_Date);
				}
				if(!sdBillDate.equals("")){

					Date secBill_Date = dt.parse(sdBillDate);
					sdBillDate = dt1.format(secBill_Date);
				}
				if(!nmrBillDate.equals("")){
		    		Date received_date = dt.parse(nmrBillDate);
		    		nmrBillDate = dt1.format(received_date);
		    		}
		    	if(!billDate.equals("")){
		    		Date received_date = dt.parse(billDate);
		    		billDate = dt1.format(received_date);
		    		}
				if(!paymentReqDate.equals("")){

					Date paymentReq_Date = dt.parse(paymentReqDate);
					paymentReqDate = dt1.format(paymentReq_Date);
				}
				if(!paymentRequestReceivedDate.equals("")){

					Date paymentRequest_ReceivedDate = dt.parse(paymentRequestReceivedDate);
					paymentRequestReceivedDate = dt1.format(paymentRequest_ReceivedDate);
					paymentRequestReceivedDate +=" "+ time1.format(paymentRequest_ReceivedDate);
				}

			}

			catch(Exception e){
				e.printStackTrace();
			}
			objPaymentBean.setStrRaBillDate(raBillDate);
			objPaymentBean.setStrWorkOrderDate(workOrderDate);
			objPaymentBean.setStrAdvBillDate(advBillDate);
			objPaymentBean.setStrSdBillDate(sdBillDate);
			objPaymentBean.setStrNmrBillDate(nmrBillDate);
		    objPaymentBean.setStrBillDate(billDate);
		    objPaymentBean.setStrPaymentReqDate(paymentReqDate);
			objPaymentBean.setStrPaymentRequestReceivedDate(paymentRequestReceivedDate);			

			list.add(objPaymentBean);
		}

		return list;
	}

	@Override
	public List<PaymentModesBean> getPaymentModes() {
		JdbcTemplate template = null;
		try {
			template  = new JdbcTemplate(DBConnection.getDbConnection());
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<Map<String, Object>> dbIndentDts = null;
		List<PaymentModesBean> list = new ArrayList<PaymentModesBean>();
		String query = "select NAME,VALUE from ACC_PAYMENT_MODES where STATUS='A' order by ID asc";
		dbIndentDts = template.queryForList(query, new Object[]{});
		for(Map<String, Object> prods : dbIndentDts) {
			PaymentModesBean objBean = new PaymentModesBean();
			objBean.setName(prods.get("NAME")==null ? "" : prods.get("NAME").toString());
			objBean.setValue(prods.get("VALUE")==null ? "" : prods.get("VALUE").toString());
			list.add(objBean);
		}
			
		return list;
	}

	
	@Override
	public int updateCntTempPaymentTransactionsTbl(PaymentBean objPaymentBean,String strPendingEmpId) {
		String query = "update ACC_CNT_TEMP_PAYMENT_TRANSACTIONS set REQUEST_PENDING_EMP_ID = ?,PAYMENT_MODE=?,UTR_CHQNO=?,REMARKS=? "
				+ "where TEMP_PAYMENT_TRANSACTIONS_ID = ? ";

				int result = jdbcTemplate.update(query, new Object[] {
						strPendingEmpId,
						objPaymentBean.getPaymentMode(),
						objPaymentBean.getUtrChequeNo(),
						objPaymentBean.getStrRemarks(),
						objPaymentBean.getIntTempPaymentTransactionId()
						


				});
		return result;
	}

	@Override
	public String getLowerEmpIdInAccounts(String strUserId) {
		String lowerEmpId="";
		String query =    "select distinct(DEPT_ID) from ("
						+ "select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in ("
						+ "select EMP_ID from SUMADHURA_APPROVER_MAPPING_DTL where APPROVER_DEPT_ID in("
						+ "select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID= '"+strUserId+"') and STATUS='A' ))";
		lowerEmpId = jdbcTemplate.queryForObject(query, String.class);
		
		return lowerEmpId;
	}

	@Override
	public int updateRowInCntAccTempPaymentTransactions(PaymentBean objPaymentBean) {
		int count=0;
		String query = "update ACC_CNT_TEMP_PAYMENT_TRANSACTIONS set STATUS = 'I' , REMARKS = ? where TEMP_PAYMENT_TRANSACTIONS_ID=? ";
		count = jdbcTemplate.update(query, new Object[] {objPaymentBean.getStrRemarks(),objPaymentBean.getIntTempPaymentTransactionId()});

		return count;
	}

	@Override
	public int updateIntiateAmountInCntAccDeptPaymentProcesstbl(PaymentBean objPaymentBean) {
		String query = "update ACC_CNT_ACCOUNTS_DEPT_PMT_PROSS set PROCESS_INTIATE_AMOUNT = PROCESS_INTIATE_AMOUNT-? "
				+ "where ACCOUNTS_DEPT_PMT_PROSS_ID = ? ";

				int result = jdbcTemplate.update(query, new Object[] {
						objPaymentBean.getDoubleAmountToBeReleased(),objPaymentBean.getIntAccDeptPaymentProcessId()

				});
		return result;
	}

	@Override
	public int insertCntPaymentTransactionsTbl(PaymentBean objPaymentBean, int intPaymentTransactionId) {
		String query = " insert into ACC_CNT_PAYMENT_TRANSACTIONS (PAYMENT_TRANSACTIONS_ID,CREATION_DATE,PAID_AMOUNT,PAYMENT_DATE,"
					 + " CNT_PAYMENT_ID,PAYMENT_MODE,REMARKS,IS_UPDATED,UTR_CHQNO,TRANSACTION_TYPE) values (?,sysdate,?,sysdate,?,?,?,?,?,?)";
				int result = jdbcTemplate.update(query, new Object[] {
						intPaymentTransactionId,objPaymentBean.getDoubleAmountToBeReleased(),
						objPaymentBean.getIntCntPaymentId(),objPaymentBean.getPaymentMode(),objPaymentBean.getStrRemarks(),
						"N",objPaymentBean.getUtrChequeNo(),objPaymentBean.getPaymentType()
				});
		return result;
	}

	@Override
	public int insertCntAdvanceCreditHistory(PaymentBean objPaymentBean) {
		String query = " insert into ACC_CNT_ADVANCE_HISTORY (CNT_ADVANCE_HISTORY_ID,WO_NUMBER,AMOUNT,ENTRY_DATE,ADVANCE_BILL_NO,RA_BILL_NO,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID) "
					 + " values (CNT_ADVANCE_HISTORY_SEQ.nextval,?,?,sysdate,?,?,?,?,?)";
			int result = jdbcTemplate.update(query, new Object[] {
					objPaymentBean.getWorkOrderNo(),objPaymentBean.getDoubleAmountToBeReleased(),objPaymentBean.getStrAdvBillNo(),
					objPaymentBean.getRaBillNo(),"CR",objPaymentBean.getStrBillId(),objPaymentBean.getQsWorkorderIssueId()
			});
		return result;
	}

	@Override
	public int insertCntInvoiceHistory(PaymentBean objPaymentBean, int intPaymentTransactionId) {
		String query = " insert into ACC_CNT_INVOICE_HISTORY (CNT_INVOICE_HISTORY_ID,WO_NUMBER,BILL_NO,BILL_AMOUNT,PAID_AMOUNT,ENTRY_DATE,"
					 + " PAYMENT_MODE,REF_NO,PAYMENT_TRANSACTIONS_ID,BILL_ID,QS_WORKORDER_ISSUE_ID,PAYMENT_TYPE) values (CNT_INVOICE_HISTORY_SEQ.nextval,?,?,?,?,sysdate,?,?,?,?,?,?)";
			int result = jdbcTemplate.update(query, new Object[] {
				objPaymentBean.getWorkOrderNo(),objPaymentBean.getBillNumber(),
				objPaymentBean.getDoubleBillAmount(),objPaymentBean.getDoubleAmountToBeReleased(),
				objPaymentBean.getPaymentMode(),objPaymentBean.getUtrChequeNo(),intPaymentTransactionId,
				objPaymentBean.getStrBillId(),objPaymentBean.getQsWorkorderIssueId(),
				objPaymentBean.getPaymentType()
			});
		return result;
	}
	
	@Override
	public int insertCntSecDepositCreditHistory(PaymentBean objPaymentBean) {
		String query = " insert into ACC_CNT_SEC_DEPOSIT_HISTORY (CNT_SEC_DEPOSIT_HISTORY_ID,SD_AMOUNT,CONTRACTOR_ID,RA_BILL_NO,ADV_PMT_BILL_NO,STATUS,WORK_ORDER_NO,SITE_ID,CREATION_DATE,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID) "
					 + " values (CNT_SEC_DEPOSIT_HISTORY_SEQ.nextval,?,?,?,?,?,?,?,sysdate,?,?,?)";
			int result = jdbcTemplate.update(query, new Object[] {
					objPaymentBean.getDoubleAmountToBeReleased(),objPaymentBean.getStrContractorId(),
					objPaymentBean.getRaBillNo(),objPaymentBean.getStrAdvBillNo(),
					"A",objPaymentBean.getWorkOrderNo(),objPaymentBean.getStrSiteId(),"CR",
					objPaymentBean.getStrBillId(),objPaymentBean.getQsWorkorderIssueId()
			});
		return result;
	}

	@Override
	public int insertOrUpdateAdvancePaidAmountInCntAdvanceDtlsTable(PaymentBean objPaymentBean) {
		
		int result=0;
		//checking is there any row on this workorder number already. 
		String query = "select count(*) from ACC_CNT_ADV_DETAILS where WO_NUMBER=? ";	
		int count = jdbcTemplate.queryForInt(query,objPaymentBean.getWorkOrderNo());
		if(count==0){
		
		//if there is no row, inserting ACC_CNT_ADV_DETAILS  table.
			String query1 = " insert into ACC_CNT_ADV_DETAILS(CNT_ADV_DETAILS_ID,WO_NUMBER,WO_AMOUNT,PAID_AMOUNT,ADJUSTED_AMOUNT,REMAINING_AMOUNT,"
						  + " INTIATED_AMOUNT,CREATED_DATE,BILL_ID,QS_WORKORDER_ISSUE_ID)  values(CNT_ADV_DETAILS_SEQ.nextval,?,?,?,?,?,?,sysdate,?,?)";

							result = jdbcTemplate.update(query1, new Object[] {
									objPaymentBean.getWorkOrderNo(),objPaymentBean.getDoubleWorkOrderAmount(),objPaymentBean.getDoubleAmountToBeReleased(),
									0.0,objPaymentBean.getDoubleAmountToBeReleased(),0.0,objPaymentBean.getStrBillId(),objPaymentBean.getQsWorkorderIssueId()
									
							});	
		}
		else{
			//if there is a row already, updating ACC_CNT_ADV_DETAILS  table on that workOrder number.
			String query2 = "update ACC_CNT_ADV_DETAILS set PAID_AMOUNT=PAID_AMOUNT+? ,REMAINING_AMOUNT=REMAINING_AMOUNT+?  "
					+ " where WO_NUMBER = ? and BILL_ID = ? and QS_WORKORDER_ISSUE_ID = ?";

							result = jdbcTemplate.update(query2, new Object[] {
									objPaymentBean.getDoubleAmountToBeReleased(),objPaymentBean.getDoubleAmountToBeReleased(),
									objPaymentBean.getWorkOrderNo(),objPaymentBean.getStrBillId(),objPaymentBean.getQsWorkorderIssueId()
									
							});	
		}
			
		
		
		return result;
	}

	@Override
	public int revertPendingApprovalToLowerEmployee(String strLowerEmpId, int intTempPaymentTransactionId,String comments) {
		int count=0;
		String query = "update ACC_CNT_TEMP_PAYMENT_TRANSACTIONS set REQUEST_PENDING_EMP_ID=?,REMARKS=? where TEMP_PAYMENT_TRANSACTIONS_ID=? ";
		count = jdbcTemplate.update(query, new Object[] {strLowerEmpId,comments,intTempPaymentTransactionId});

		
		
		return count;
	}
	
	@Override
	public int updateAllocatedAmountInCntAccDeptPaymentProcesstbl(PaymentBean objPaymentBean) {
		String query = "update ACC_CNT_ACCOUNTS_DEPT_PMT_PROSS set PENDING_AMOUNT = PENDING_AMOUNT-?,ALLOCATED_AMOUNT = ALLOCATED_AMOUNT+? "
				+ "where ACCOUNTS_DEPT_PMT_PROSS_ID = ? ";

				int result = jdbcTemplate.update(query, new Object[] {
						objPaymentBean.getDoubleAmountToBeReleased(),objPaymentBean.getDoubleAmountToBeReleased(),objPaymentBean.getIntAccDeptPaymentProcessId()

				});
		return result;
	}

	@Override
	public int inactivePaymentAfterCheck(PaymentBean objPaymentBean) {
		//checking is all the req.amount is allocated or not
		boolean doInactive = false;
		String query1 = "select ACCOUNT_DEPT_REQ_REC_AMOUNT,ALLOCATED_AMOUNT from ACC_CNT_ACCOUNTS_DEPT_PMT_PROSS where ACCOUNTS_DEPT_PMT_PROSS_ID = ?";
		Map<String, Object> paymentDtls = jdbcTemplate.queryForMap(query1, new Object[] {objPaymentBean.getIntAccDeptPaymentProcessId()});
		double reqRecAmount = Double.valueOf(paymentDtls.get("ACCOUNT_DEPT_REQ_REC_AMOUNT")==null ? "0" :   paymentDtls.get("ACCOUNT_DEPT_REQ_REC_AMOUNT").toString());
		double allocatedAmount = Double.valueOf(paymentDtls.get("ALLOCATED_AMOUNT")==null ? "0" :   paymentDtls.get("ALLOCATED_AMOUNT").toString());
		if((reqRecAmount-allocatedAmount)<=0){doInactive = true;}
		
		
		//if allocated do Inactive
		if(doInactive){
			String query2 = "update ACC_CNT_ACCOUNTS_DEPT_PMT_PROSS set STATUS = 'I' where ACCOUNTS_DEPT_PMT_PROSS_ID = ?";
			int result2 = jdbcTemplate.update(query2, new Object[] {objPaymentBean.getIntAccDeptPaymentProcessId()});
			
			String query3 = "update ACC_CNT_PAYMENT set STATUS = 'I' where CNT_PAYMENT_ID = ?";
			int result3 = jdbcTemplate.update(query3, new Object[] {objPaymentBean.getIntCntPaymentId()});
			
			String query4 = "update QS_CONTRACTOR_BILL set STATUS = 'I', PAYMENT_STATUS = 'PAID' where BILL_ID = ?";
			int result4 = jdbcTemplate.update(query4, new Object[] {objPaymentBean.getStrBillId()});
			
			return result2+result3+result4;
		}
		
		return 0;
	}

	@Override
	public Map<String, String> getDeductionAmountDetails(String tempBillId) {
		Map<String, String> deductionAmounts = new HashMap<String, String>();
		String query = "select  * from QS_TEMP_BILL_DEDUCTION_DTLS where TEMP_BILL_ID = ?"; 
		List<Map<String, Object>> paymentDtls = jdbcTemplate.queryForList(query, new Object[] {tempBillId});
		for(Map<String, Object> paymentReqs : paymentDtls) {
			deductionAmounts.put(paymentReqs.get("TYPE_OF_DEDUCTION")==null ? "" : paymentReqs.get("TYPE_OF_DEDUCTION").toString(),paymentReqs.get("DEDUCTION_AMOUNT")==null ? "0" : paymentReqs.get("DEDUCTION_AMOUNT").toString());
		}
		return deductionAmounts;
	}

	@Override
	public int insertCntAdvanceDebitHistory(PaymentBean objPaymentBean, double advanceDeductionAmount) {
		String query = " insert into ACC_CNT_ADVANCE_HISTORY (CNT_ADVANCE_HISTORY_ID,WO_NUMBER,AMOUNT,ENTRY_DATE,ADVANCE_BILL_NO,RA_BILL_NO,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID) "
				 + " values (CNT_ADVANCE_HISTORY_SEQ.nextval,?,?,sysdate,?,?,?,?,?)";
		int result = jdbcTemplate.update(query, new Object[] {
				objPaymentBean.getWorkOrderNo(),advanceDeductionAmount,objPaymentBean.getStrAdvBillNo(),
				objPaymentBean.getRaBillNo(),"DBT",objPaymentBean.getStrBillId(),objPaymentBean.getQsWorkorderIssueId()
		});
	return result;
}

	@Override
	public int insertCntSecDepositDebitHistory(PaymentBean objPaymentBean, double securityDepositDeductionAmount) {
		String query = " insert into ACC_CNT_SEC_DEPOSIT_HISTORY (CNT_SEC_DEPOSIT_HISTORY_ID,SD_AMOUNT,CONTRACTOR_ID,RA_BILL_NO,ADV_PMT_BILL_NO,STATUS,WORK_ORDER_NO,SITE_ID,CREATION_DATE,TRANSACTION_TYPE,BILL_ID,QS_WORKORDER_ISSUE_ID) "
				 	 + " values (CNT_SEC_DEPOSIT_HISTORY_SEQ.nextval,?,?,?,?,?,?,?,sysdate,?,?,?)";
		int result = jdbcTemplate.update(query, new Object[] {
				securityDepositDeductionAmount,objPaymentBean.getStrContractorId(),objPaymentBean.getRaBillNo(),
				objPaymentBean.getStrAdvBillNo(),"A",
				objPaymentBean.getWorkOrderNo(),objPaymentBean.getStrSiteId(),"DBT",
				objPaymentBean.getStrBillId(),objPaymentBean.getQsWorkorderIssueId()
		});
	return result;
}

	@Override
	public int minusDeductionAmountFromAdvanceAmountInCntAdvanceDtlsTable(PaymentBean objPaymentBean,double advanceDeductionAmount) {
		String query = " update ACC_CNT_ADV_DETAILS set ADJUSTED_AMOUNT = ADJUSTED_AMOUNT+?, "
					 + " REMAINING_AMOUNT = REMAINING_AMOUNT-?   where  WO_NUMBER = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				advanceDeductionAmount,advanceDeductionAmount,objPaymentBean.getWorkOrderNo()
				});
		return result;
	}

	@Override
	public int depositDeductionAmountUnderSecDepositInCntSecDepositTable(PaymentBean objPaymentBean,double securityDepositDeductionAmount) {
		
		int result=0;
		//checking is there any row on this workorder number already. 
		String query = "select count(*) from ACC_CNT_SEC_DEPOSIT where WORK_ORDER_NO=? ";	
		int count = jdbcTemplate.queryForInt(query,objPaymentBean.getWorkOrderNo());
		if(count==0){
		
		//if there is no row, inserting ACC_CNT_SEC_DEPOSIT  table.
			String query1 = " insert into ACC_CNT_SEC_DEPOSIT(CNT_SEC_DEPOSIT_ID,SD_AMOUNT,CONTRACTOR_ID,STATUS,WORK_ORDER_NO,SITE_ID,CREATION_DATE,BILL_ID,QS_WORKORDER_ISSUE_ID) "
						  + " values(CNT_SEC_DEPOSIT_SEQ.nextval,?,?,?,?,?,sysdate,?,?)";

							result = jdbcTemplate.update(query1, new Object[] {
									securityDepositDeductionAmount,objPaymentBean.getStrContractorId(),"A",
									objPaymentBean.getWorkOrderNo(),objPaymentBean.getStrSiteId(),objPaymentBean.getStrBillId(),objPaymentBean.getQsWorkorderIssueId()
									
							});	
		}
		else{
			//if there is a row already, updating ACC_CNT_SEC_DEPOSIT  table on that workOrder number.
			String query2 = "update ACC_CNT_SEC_DEPOSIT set SD_AMOUNT=SD_AMOUNT+?   where WORK_ORDER_NO = ? and BILL_ID = ? and QS_WORKORDER_ISSUE_ID = ?";

							result = jdbcTemplate.update(query2, new Object[] {securityDepositDeductionAmount,objPaymentBean.getWorkOrderNo(),objPaymentBean.getStrBillId(),objPaymentBean.getQsWorkorderIssueId()});	
		}
			
		
		
		return result;
	}

	@Override
	public int inactiveSecDepositInCntSecDepositTable(PaymentBean objPaymentBean) {
		String query2 = "update ACC_CNT_SEC_DEPOSIT set STATUS = 'I'  where WORK_ORDER_NO = ?";
		int result = jdbcTemplate.update(query2, new Object[] {objPaymentBean.getWorkOrderNo()});
		return result;	
	}

	@Override
	public List<PaymentBean> getViewContractorPaymentDetails(String fromDate, String toDate, String selected_contractorId,String user_id, String dropdown_SiteId, String selected_workOrderNo) {

		String query = "";
		List<Map<String, Object>> paymentDtls = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null;

		try {
				query = " select ACPT.PAID_AMOUNT,ACPT.UTR_CHQNO,ACPT.PAYMENT_DATE,ACPT.CREATION_DATE,ACPT.PAYMENT_MODE,"
					  + " ACPT.CNT_PAYMENT_ID,ACPT.REMARKS,"
					  + "ACP.BILL_DATE,ACP.BILL_NUMBER,ACP.BILL_AMOUNT,"
					  + " ACP.PAYMENT_REQ_AMOUNT,ACP.WO_AMOUNT,ACP.WO_DATE,ACP.WO_NUMBER,"
					  + " ACP.SITE_ID,ACP.CONTRACTOR_ID,ACP.BILL_ID,ACP.PAYMENT_TYPE,ACP.SITEWISE_PAYMENT_NO,ACP.PARTICULAR,S.SITE_NAME,SC.FIRST_NAME||''||SC.LAST_NAME as CONTRACTOR_NAME ,"
         
         
					  /*=============  to get remarks from QS_CONT_TMP_BILL_APPR_REJ_DTLS  ===========*/
					  + " (SELECT LISTAGG(CONCAT(CONCAT(SED1.EMP_NAME,'  :  '),QCTBARD.REMARKS), '@@@')"
					  + " WITHIN GROUP (ORDER BY QCTBARD.QS_BILL_APPR_REJ_DTLS) "
					  + " from QS_CONT_TMP_BILL_APPR_REJ_DTLS QCTBARD,QS_CONTRACTOR_BILL QCB1,SUMADHURA_EMPLOYEE_DETAILS SED1" 
					  + " where SED1.EMP_ID = QCTBARD.EMP_ID and QCTBARD.TEMP_BILL_ID = QCB1.TEMP_BILL_ID and QCB1.BILL_ID = ACP.BILL_ID and QCB1.CONT_SEQ_BILL_NO = ACP.BILL_NUMBER  and regexp_like(QCTBARD.REMARKS, '[a-zA-Z0-9]')) as QS_APR_REJ_REMARKS ," 
				 
					  /*=============  to get remarks from ACC_CNT_PMT_SITE_APPR_REJ_DTLS  ===========*/
					  + "(SELECT LISTAGG(CONCAT(CONCAT(SED3.EMP_NAME,'  :  '),ACPSARD.REMARKS), '@@@')"
					  + "WITHIN GROUP (ORDER BY ACPSARD.PAYMENT_APROV_REJECT_SEQ) "
					  + "from ACC_CNT_PMT_SITE_APPR_REJ_DTLS ACPSARD,SUMADHURA_EMPLOYEE_DETAILS SED3 "
					  + "where SED3.EMP_ID = ACPSARD.EMP_ID and ACPSARD.CNT_PAYMENT_ID = ACP.CNT_PAYMENT_ID and regexp_like(ACPSARD.REMARKS, '[a-zA-Z0-9]') ) as SITE_PMT_APP_REJ_REMARKS ,  "
					  + ""
					  /*=============  to get remarks from ACC_CNT_PMT_APPR_REJECT_DTLS  ===========*/
					  + " (SELECT LISTAGG(CONCAT(CONCAT(SED2.EMP_NAME,'  :  '),ACPARD.REMARKS), '@@@')"
					  + " WITHIN GROUP (ORDER BY ACPARD.PAYMENT_APROV_REJECT_SEQ) "
					  + " from ACC_CNT_PMT_APPR_REJECT_DTLS ACPARD,SUMADHURA_EMPLOYEE_DETAILS SED2" 
					  + " where SED2.EMP_ID = ACPARD.EMP_ID and ACPARD.CNT_PAYMENT_ID = ACPT.CNT_PAYMENT_ID and regexp_like(ACPARD.REMARKS, '[a-zA-Z0-9]')  ) as ACC_APP_REJ_REMARKS"  
				 
         
					  + " from ACC_CNT_PAYMENT_TRANSACTIONS ACPT,ACC_CNT_PAYMENT ACP,SUMADHURA_CONTRACTOR SC,SITE S  "
					  + " where ACP.CNT_PAYMENT_ID = ACPT.CNT_PAYMENT_ID and ACP.SITE_ID = S.SITE_ID "
					  + " and  ACP.CONTRACTOR_ID = SC.CONTRACTOR_ID ";
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = query + " and TRUNC(ACPT.CREATION_DATE)   BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = query + " and TRUNC(ACPT.CREATION_DATE)   =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = query + " and TRUNC(ACPT.CREATION_DATE)   <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
				}
			
				if(StringUtils.isNotBlank(selected_contractorId)){
					query = query+" and ACP.CONTRACTOR_ID='"+selected_contractorId+"'";
				}
				if(StringUtils.isNotBlank(dropdown_SiteId)){
					query = query+" and ACP.SITE_ID='"+dropdown_SiteId+"'";
				}
				if (StringUtils.isNotBlank(selected_workOrderNo)){
					query = query + " and ACP.WO_NUMBER = '"+selected_workOrderNo+"'";
				}
				
				
				// this block of code for - to display payments as per AccountsDepartment is HYD or BANGLORE.
				/** take dept_id of login employee.
				 ** decide address hyd or banglore.
				 ** take list of sites in that address.
				 ** form a String using sites to pass into query.
				 *
				 *for Hyderabad sites should show hyderabad sites only for banglore can show everyone(hyderabad and banglore
				
				 *Site level also using same query site accountent will not available dept_id
				 */
				String dept_id = jdbcTemplate.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+user_id+"'", new Object[]{},String.class);
				if(dept_id != null && dept_id.contains("H")){
					String address="HYD";
					List<String> siteIdList = jdbcTemplate.queryForList("select SITE_ID from SITE where ADDRESS like '%"+address+"%'", new Object[]{},String.class);
					if(siteIdList.size()>0){
						String groupOfSites = "";
						for(String site:siteIdList){
							groupOfSites +="'"+site+"',";
						}
						groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
						query = query + " and ACP.SITE_ID in("+groupOfSites+")";
					}
				}
				/***************************************/


			paymentDtls = jdbcTemplate.queryForList(query, new Object[]{});
			int i = 0;
			for(Map<String, Object> paymentReqs : paymentDtls) {
				i = i+1;
				String contractorId = paymentReqs.get("CONTRACTOR_ID")==null ? "" : paymentReqs.get("CONTRACTOR_ID").toString();
				String contractorName = paymentReqs.get("CONTRACTOR_NAME")==null ? "" : paymentReqs.get("CONTRACTOR_NAME").toString();
				String site_id = paymentReqs.get("SITE_ID")==null ? "" : paymentReqs.get("SITE_ID").toString();
				String site_name = paymentReqs.get("SITE_NAME")==null ? "" : paymentReqs.get("SITE_NAME").toString();
				String workOrderNo = paymentReqs.get("WO_NUMBER")==null ? "" : paymentReqs.get("WO_NUMBER").toString();
				double paidAmount = Double.valueOf(paymentReqs.get("PAID_AMOUNT")==null ? "" : paymentReqs.get("PAID_AMOUNT").toString());
				double workOrderAmount = Double.valueOf(paymentReqs.get("WO_AMOUNT")==null ? "0" : paymentReqs.get("WO_AMOUNT").toString());

				String billNumber = paymentReqs.get("BILL_NUMBER")==null ? "" : paymentReqs.get("BILL_NUMBER").toString();
				double billAmount = Double.valueOf(paymentReqs.get("BILL_AMOUNT")==null ? "0" : paymentReqs.get("BILL_AMOUNT").toString());
				String billDate = paymentReqs.get("BILL_DATE")==null ? "" : paymentReqs.get("BILL_DATE").toString();
				String paymentType = paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString();
				
				String raBillNo,advBillNo,sdBillNo,raBillDate,advBillDate,sdBillDate;
				raBillNo=advBillNo=sdBillNo=raBillDate=advBillDate=sdBillDate = "";
				double raBillAmount,advBillAmount,sdBillAmount;
				raBillAmount=advBillAmount=sdBillAmount = 0;
				if(paymentType.equals("RA")){
					raBillNo = billNumber;
					raBillAmount = billAmount; 
					raBillDate = billDate;
				}
				if(paymentType.equals("ADV")){
					advBillNo = billNumber;
					advBillAmount = billAmount;  
					advBillDate = billDate;
				}
				if(paymentType.equals("SEC")){
					sdBillNo = billNumber;
					sdBillAmount = billAmount; 
					sdBillDate = billDate; 
				}
				
				objPaymentBean = new PaymentBean();

				objPaymentBean.setIntSerialNo(i);
				objPaymentBean.setWorkOrderNo(workOrderNo);
				objPaymentBean.setDoublePaidAmount(paidAmount);
				objPaymentBean.setStrContractorId(contractorId);
				objPaymentBean.setStrContractorName(contractorName);
				objPaymentBean.setStrSiteId(site_id);
				objPaymentBean.setStrSiteName(site_name);
				objPaymentBean.setDoubleWorkOrderAmount(workOrderAmount);
				
				objPaymentBean.setIntCntPaymentId(Integer.parseInt(paymentReqs.get("CNT_PAYMENT_ID")==null ? "" :   paymentReqs.get("CNT_PAYMENT_ID").toString()));
				String remarks = "";
				String qs_app_rej_remarks = paymentReqs.get("QS_APR_REJ_REMARKS")==null ? "" :   paymentReqs.get("QS_APR_REJ_REMARKS").toString();
				String site_app_rej_remarks = paymentReqs.get("SITE_PMT_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("SITE_PMT_APP_REJ_REMARKS").toString();
				String acc_app_rej_remarks = paymentReqs.get("ACC_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("ACC_APP_REJ_REMARKS").toString();
				if(StringUtils.isNotBlank(qs_app_rej_remarks)){remarks+=qs_app_rej_remarks;}
				if(StringUtils.isNotBlank(site_app_rej_remarks)){
					if(StringUtils.isBlank(remarks)){remarks=site_app_rej_remarks;} else{remarks+="@@@"+site_app_rej_remarks;}
				}
				if(StringUtils.isNotBlank(acc_app_rej_remarks)){
					if(StringUtils.isBlank(remarks)){remarks=acc_app_rej_remarks;} else{remarks+="@@@"+acc_app_rej_remarks;}
				}
				objPaymentBean.setStrRemarks(remarks);
				objPaymentBean.setStrRemarksForView(remarks.replace("@@@", ","));
				objPaymentBean.setStrRemarksForTitle(remarks.replace("@@@", "&#013;"));
				objPaymentBean.setPaymentType(paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString());
				String workOrderDate = paymentReqs.get("WO_DATE")==null ? "" : paymentReqs.get("WO_DATE").toString();
				String paymentReqDate = paymentReqs.get("PAYMENT_REQ_DATE")==null ? "" :   paymentReqs.get("PAYMENT_REQ_DATE").toString();
				String paymentDate = paymentReqs.get("PAYMENT_DATE")==null ? "" : paymentReqs.get("PAYMENT_DATE").toString();
				objPaymentBean.setIntSiteWisePaymentId(Integer.parseInt(paymentReqs.get("SITEWISE_PAYMENT_NO")==null ? "0" : paymentReqs.get("SITEWISE_PAYMENT_NO").toString()));
				objPaymentBean.setUtrChequeNo(paymentReqs.get("UTR_CHQNO")==null ? "" : paymentReqs.get("UTR_CHQNO").toString());
				objPaymentBean.setStrBillId(paymentReqs.get("BILL_ID")==null ? "" : paymentReqs.get("BILL_ID").toString());
				String paymentMode = paymentReqs.get("PAYMENT_MODE")==null ? "" : paymentReqs.get("PAYMENT_MODE").toString();
				objPaymentBean.setPaymentMode(paymentMode);
				objPaymentBean.setPaymentType(paymentType);
				objPaymentBean.setBillNumber(billNumber);
				objPaymentBean.setDoubleBillAmount(billAmount);
				objPaymentBean.setRaBillNo(raBillNo);
				objPaymentBean.setDoubleRaBillAmount(raBillAmount);
				objPaymentBean.setDoubleAdvBillAmount(advBillAmount);
				objPaymentBean.setStrAdvBillNo(advBillNo);
				objPaymentBean.setDoubleSdBillAmount(sdBillAmount);
				objPaymentBean.setStrSdBillNo(sdBillNo);
				
				
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				try{

					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat time1 = new SimpleDateFormat("hh:mm aa");

					if(!raBillDate.equals("")){

						Date raBill_Date = dt.parse(raBillDate);
						raBillDate = dt1.format(raBill_Date);
					}
					if(!workOrderDate.equals("")){

						Date workOrder_Date = dt.parse(workOrderDate);
						workOrderDate = dt1.format(workOrder_Date);
					}
					if(!advBillDate.equals("")){

						Date advBill_Date = dt.parse(advBillDate);
						advBillDate = dt1.format(advBill_Date);
					}
					if(!sdBillDate.equals("")){

						Date secBill_Date = dt.parse(sdBillDate);
						sdBillDate = dt1.format(secBill_Date);
					}
					if(!paymentDate.equals("")){

						Date payment_Date = dt.parse(paymentDate);
						paymentDate = dt1.format(payment_Date);
					}
					

				}

				catch(Exception e){
					e.printStackTrace();
				}
				objPaymentBean.setStrRaBillDate(raBillDate);
				objPaymentBean.setStrWorkOrderDate(workOrderDate);
				objPaymentBean.setStrAdvBillDate(advBillDate);
				objPaymentBean.setStrSdBillDate(sdBillDate);
				objPaymentBean.setStrPaymentReqDate(paymentReqDate);
				objPaymentBean.setStrPaymentDate(paymentDate);			

				
				list.add(objPaymentBean);
			}





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			objPaymentBean = null; 
			
		}
		return list;
	}

	@Override
	public List<PaymentBean> viewMyContractorPayment(String fromDate, String toDate, String loginSiteId, String user_id,
			String selected_contractorId, String selected_workOrderNo,String dropdown_SiteId) {

		String query = "";
		List<Map<String, Object>> paymentDtls = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null; 

		try {

				query = "select ACTPT.TEMP_PAYMENT_TRANSACTIONS_ID,ACTPT.ACCOUNTS_DEPT_PMT_PROSS_ID,ACP.CNT_PAYMENT_ID,"
					  + " ACTPT.PAYMENT_MODE,ACTPT.PAYMENT_REQ_DATE,ACTPT.REQ_AMOUNT as ACC_REQ_AMOUNT,ACTPT.UPDATE_DATE,ACTPT.UTR_CHQNO,"
					  + " ACTPT.TRANSACTION_TYPE,ACTPT.REQUEST_PENDING_EMP_ID as ACC_REQUEST_PENDING_EMP_ID,ACP.REQUEST_PENDING_EMP_ID ,ACP.REQUEST_PENDING_DEPT_ID,"
					  + "ACP.BILL_DATE,ACP.BILL_NUMBER,ACP.BILL_AMOUNT,"
					  + " ACP.CREATED_DATE,ACP.STATUS,"
					  + " ACP.PAYMENT_REQ_AMOUNT,ACP.WO_AMOUNT,ACP.WO_DATE,ACP.WO_NUMBER,"
					  + " ACP.SITE_ID,ACP.CONTRACTOR_ID,ACP.BILL_ID,ACP.PAYMENT_TYPE,ACP.SITEWISE_PAYMENT_NO,ACP.PARTICULAR,"
					  + " S.SITE_NAME,SC.FIRST_NAME||' '||SC.LAST_NAME as CONTRACTOR_NAME,SED.EMP_NAME,  "
				
					  /*=============  to get remarks from QS_CONT_TMP_BILL_APPR_REJ_DTLS  ===========*/
					  + " (SELECT LISTAGG(CONCAT(CONCAT(SED1.EMP_NAME,'  :  '),QCTBARD.REMARKS), '@@@')"
					  + " WITHIN GROUP (ORDER BY QCTBARD.QS_BILL_APPR_REJ_DTLS) "
					  + " from QS_CONT_TMP_BILL_APPR_REJ_DTLS QCTBARD,QS_CONTRACTOR_BILL QCB1,SUMADHURA_EMPLOYEE_DETAILS SED1 "  	
					  + " where SED1.EMP_ID = QCTBARD.EMP_ID and QCTBARD.TEMP_BILL_ID = QCB1.TEMP_BILL_ID and QCB1.BILL_ID = ACP.BILL_ID and QCB1.CONT_SEQ_BILL_NO = ACP.BILL_NUMBER   and regexp_like(QCTBARD.REMARKS, '[a-zA-Z0-9]') ) as QS_APR_REJ_REMARKS ," 
				 
					  /*=============  to get remarks from ACC_CNT_PMT_SITE_APPR_REJ_DTLS  ===========*/
					  + "(SELECT LISTAGG(CONCAT(CONCAT(SED3.EMP_NAME,'  :  '),ACPSARD.REMARKS), '@@@')"
					  + "WITHIN GROUP (ORDER BY ACPSARD.PAYMENT_APROV_REJECT_SEQ) "
					  + "from ACC_CNT_PMT_SITE_APPR_REJ_DTLS ACPSARD,SUMADHURA_EMPLOYEE_DETAILS SED3 "
					  + "where SED3.EMP_ID = ACPSARD.EMP_ID and ACPSARD.CNT_PAYMENT_ID = ACP.CNT_PAYMENT_ID and regexp_like(ACPSARD.REMARKS, '[a-zA-Z0-9]') ) as SITE_PMT_APP_REJ_REMARKS ,  "
					  + ""
					  /*=============  to get remarks from ACC_CNT_PMT_APPR_REJECT_DTLS  ===========*/
					  + " (SELECT LISTAGG(CONCAT(CONCAT(SED2.EMP_NAME,'  :  '),ACPARD.REMARKS), '@@@')"
					  + " WITHIN GROUP (ORDER BY ACPARD.PAYMENT_APROV_REJECT_SEQ)  "
					  + " from ACC_CNT_PMT_APPR_REJECT_DTLS ACPARD,SUMADHURA_EMPLOYEE_DETAILS SED2" 
					  + " where SED2.EMP_ID = ACPARD.EMP_ID and ACPARD.TEMP_PAYMENT_TRANSACTIONS_ID = ACTPT.TEMP_PAYMENT_TRANSACTIONS_ID   and regexp_like(ACPARD.REMARKS, '[a-zA-Z0-9]') ) as ACC_APP_REJ_REMARKS"  
				 

					  + " from SUMADHURA_CONTRACTOR SC,SITE S,ACC_CNT_PAYMENT ACP LEFT OUTER JOIN ACC_CNT_TEMP_PAYMENT_TRANSACTIONS ACTPT on ACP.CNT_PAYMENT_ID = ACTPT.CNT_PAYMENT_ID and ACTPT.STATUS='A'"  
					  + " LEFT OUTER JOIN SUMADHURA_EMPLOYEE_DETAILS SED on SED.EMP_ID = ACP.REQUEST_PENDING_EMP_ID "
					  + " where ACP.CONTRACTOR_ID = SC.CONTRACTOR_ID  and ACP.SITE_ID = S.SITE_ID "
					  + " and (ACTPT.REQUEST_PENDING_EMP_ID NOT IN ('VND') OR ACTPT.REQUEST_PENDING_EMP_ID IS NULL)  ";
						
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = query + " and TRUNC(ACP.CREATED_DATE)   BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = query + " and TRUNC(ACP.CREATED_DATE)   =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = query + " and TRUNC(ACP.CREATED_DATE)   <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
				}
			
				if (StringUtils.isNotBlank(selected_contractorId)){
					query = query + " and ACP.CONTRACTOR_ID = '"+selected_contractorId+"'";
				}
				if (StringUtils.isNotBlank(selected_workOrderNo)){
					query = query + " and ACP.WO_NUMBER = '"+selected_workOrderNo+"'";
				}
				

			String accDeptId = validateParams.getProperty("ACCOUNTS_DEPT_ID") == null ? "" : validateParams.getProperty("ACCOUNTS_DEPT_ID").toString();

			if(!loginSiteId.equals(accDeptId)&&!loginSiteId.equals("998")){
				query = query + " and ACP.SITE_ID = '"+loginSiteId+"'";
			}
			else if(loginSiteId.equals(accDeptId)){
				// this block of code for - to display payments as per AccountsDepartment is HYD or BANGLORE.
				/** take dept_id of login employee.
				 ** decide address hyd or banglore.
				 ** take list of sites in that address.
				 ** form a String using sites to pass into query.
				 *for Hyderabad sites should show hyderabad sites only for banglore can show everyone(hyderabad and banglore
				
				 *Site level also using same query site accountent will not available dept_id
				*/
				String dept_id = jdbcTemplate.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+user_id+"'", new Object[]{},String.class);
				
				if(dept_id.contains("H")){
					String address="HYD";
					List<String> siteIdList = jdbcTemplate.queryForList("select SITE_ID from SITE where ADDRESS like '%"+address+"%'", new Object[]{},String.class);
					if(siteIdList.size()>0){
						String groupOfSites = "";
						for(String site:siteIdList){
							groupOfSites +="'"+site+"',";
						}
						groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
						query = query + " and ACP.SITE_ID in("+groupOfSites+")";
					}
				}
				if(StringUtils.isNotBlank(dropdown_SiteId)){
					query = query + " and ACP.SITE_ID in("+dropdown_SiteId+")";
				}
			}
			
			paymentDtls = jdbcTemplate.queryForList(query, new Object[] {});
			int contractorGroupSerialNo = 0;
			String currentContractorId="-";
			String currentSiteId="-";
			List<PaymentBean> raBillNoList = new ArrayList<PaymentBean>();
			List<String>workOrderNoList = new ArrayList<String>();
			int i=0;
			for(Map<String, Object> paymentReqs : paymentDtls) {
				
				i = i+1;
				String contractorId = paymentReqs.get("CONTRACTOR_ID")==null ? "" : paymentReqs.get("CONTRACTOR_ID").toString();
				String contractorName = paymentReqs.get("CONTRACTOR_NAME")==null ? "" : paymentReqs.get("CONTRACTOR_NAME").toString();
				String site_id = paymentReqs.get("SITE_ID")==null ? "" : paymentReqs.get("SITE_ID").toString();
				String site_name = paymentReqs.get("SITE_NAME")==null ? "" : paymentReqs.get("SITE_NAME").toString();
				String workOrderNo = paymentReqs.get("WO_NUMBER")==null ? "" : paymentReqs.get("WO_NUMBER").toString();
				double reqAmount = Double.valueOf(paymentReqs.get("PAYMENT_REQ_AMOUNT")==null ? "0" : paymentReqs.get("PAYMENT_REQ_AMOUNT").toString());
				double workOrderAmount = Double.valueOf(paymentReqs.get("WO_AMOUNT")==null ? "0" : paymentReqs.get("WO_AMOUNT").toString());

				String billNumber = paymentReqs.get("BILL_NUMBER")==null ? "" : paymentReqs.get("BILL_NUMBER").toString();
				double billAmount = Double.valueOf(paymentReqs.get("BILL_AMOUNT")==null ? "0" : paymentReqs.get("BILL_AMOUNT").toString());
				String billDate = paymentReqs.get("BILL_DATE")==null ? "" : paymentReqs.get("BILL_DATE").toString();
				String paymentType = paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString();
				
				String raBillNo,advBillNo,sdBillNo,raBillDate,advBillDate,sdBillDate;
				raBillNo=advBillNo=sdBillNo=raBillDate=advBillDate=sdBillDate = "";
				double raBillAmount,advBillAmount,sdBillAmount;
				raBillAmount=advBillAmount=sdBillAmount = 0;
				if(paymentType.equals("RA")){
					raBillNo = billNumber;
					raBillAmount = billAmount; 
					raBillDate = billDate;
				}
				if(paymentType.equals("ADV")){
					advBillNo = billNumber;
					advBillAmount = billAmount;  
					advBillDate = billDate;
				}
				if(paymentType.equals("SEC")){
					sdBillNo = billNumber;
					sdBillAmount = billAmount; 
					sdBillDate = billDate; 
				}

				objPaymentBean = new PaymentBean();

				objPaymentBean.setIntSerialNo(i);
				objPaymentBean.setWorkOrderNo(workOrderNo);
				objPaymentBean.setDoubleAmountToBeReleased(reqAmount);
				objPaymentBean.setStrContractorId(contractorId);
				objPaymentBean.setStrContractorName(contractorName);
				objPaymentBean.setStrSiteId(site_id);
				objPaymentBean.setStrSiteName(site_name);
				objPaymentBean.setDoubleWorkOrderAmount(workOrderAmount);
				
				objPaymentBean.setIntTempPaymentTransactionId(Integer.parseInt(paymentReqs.get("TEMP_PAYMENT_TRANSACTIONS_ID")==null ? "0" : paymentReqs.get("TEMP_PAYMENT_TRANSACTIONS_ID").toString()));
				objPaymentBean.setIntCntPaymentId(Integer.parseInt(paymentReqs.get("CNT_PAYMENT_ID")==null ? "0" :   paymentReqs.get("CNT_PAYMENT_ID").toString()));
				String remarks = "";
				String qs_app_rej_remarks = paymentReqs.get("QS_APR_REJ_REMARKS")==null ? "" :   paymentReqs.get("QS_APR_REJ_REMARKS").toString();
				String site_app_rej_remarks = paymentReqs.get("SITE_PMT_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("SITE_PMT_APP_REJ_REMARKS").toString();
				String acc_app_rej_remarks = paymentReqs.get("ACC_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("ACC_APP_REJ_REMARKS").toString();
				if(StringUtils.isNotBlank(qs_app_rej_remarks)){remarks+=qs_app_rej_remarks;}
				if(StringUtils.isNotBlank(site_app_rej_remarks)){
					if(StringUtils.isBlank(remarks)){remarks=site_app_rej_remarks;} else{remarks+="@@@"+site_app_rej_remarks;}
				}
				if(StringUtils.isNotBlank(acc_app_rej_remarks)){
					if(StringUtils.isBlank(remarks)){remarks=acc_app_rej_remarks;} else{remarks+="@@@"+acc_app_rej_remarks;}
				}
				objPaymentBean.setStrRemarks(remarks);
				objPaymentBean.setStrRemarksForView(remarks.replace("@@@", ","));
				objPaymentBean.setStrRemarksForTitle(remarks.replace("@@@", "&#013;"));
				objPaymentBean.setIntCntPaymentId(Integer.parseInt(paymentReqs.get("CNT_PAYMENT_ID")==null ? "" :   paymentReqs.get("CNT_PAYMENT_ID").toString()));
				objPaymentBean.setStrBillId(paymentReqs.get("BILL_ID")==null ? "" : paymentReqs.get("BILL_ID").toString());
				objPaymentBean.setStrTempBillId(paymentReqs.get("TEMP_BILL_ID")==null ? "" : paymentReqs.get("TEMP_BILL_ID").toString());
				objPaymentBean.setPaymentType(paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString());
				objPaymentBean.setDoubleDeductionAmount(Double.valueOf(paymentReqs.get("DEDUCTION_AMOUNT")==null ? "0" : paymentReqs.get("DEDUCTION_AMOUNT").toString()));
				String workOrderDate = paymentReqs.get("WO_DATE")==null ? "" : paymentReqs.get("WO_DATE").toString();
				String paymentReqDate = paymentReqs.get("PAYMENT_REQ_DATE")==null ? "" :   paymentReqs.get("PAYMENT_REQ_DATE").toString();
				String paymentIntiatedDate = paymentReqs.get("CREATED_DATE")==null ? "" : paymentReqs.get("CREATED_DATE").toString();
				//paymentReqs.get("ALLOCATED_AMOUNT")==null ? "" : paymentReqs.get("ALLOCATED_AMOUNT").toString();
				//paymentReqs.get("PAYMENT_REQ_SITE_ID")==null ? "" : paymentReqs.get("PAYMENT_REQ_SITE_ID").toString();
				//paymentReqs.get("PENDING_AMOUNT")==null ? "" : paymentReqs.get("PENDING_AMOUNT").toString();
				//paymentReqs.get("CREATED_DATE")==null ? "" : paymentReqs.get("CREATED_DATE").toString();
				//paymentReqs.get("STATUS")==null ? "" : paymentReqs.get("STATUS").toString();
				//paymentReqs.get("REQUEST_PENDING_DEPT_ID")==null ? "" : paymentReqs.get("REQUEST_PENDING_DEPT_ID").toString();
				//paymentReqs.get("REQUEST_PENDING_EMP_ID")==null ? "" : paymentReqs.get("REQUEST_PENDING_EMP_ID").toString();
				objPaymentBean.setIntSiteWisePaymentId(Integer.parseInt(paymentReqs.get("SITEWISE_PAYMENT_NO")==null ? "0" : paymentReqs.get("SITEWISE_PAYMENT_NO").toString()));
				objPaymentBean.setUtrChequeNo(paymentReqs.get("UTR_CHQNO")==null ? "" : paymentReqs.get("UTR_CHQNO").toString());
				String paymentMode = paymentReqs.get("PAYMENT_MODE")==null ? "" : paymentReqs.get("PAYMENT_MODE").toString();
				objPaymentBean.setPaymentMode(paymentMode);
				objPaymentBean.setStatus(paymentReqs.get("STATUS")==null ? "" : (paymentReqs.get("STATUS").toString().equals("I")?"REJECTED":"ACTIVE"));
				
				String pendingEmpId = paymentReqs.get("REQUEST_PENDING_EMP_ID")==null ? "" : paymentReqs.get("REQUEST_PENDING_EMP_ID").toString();
				String pendingEmpName = paymentReqs.get("EMP_NAME")==null ? "" : paymentReqs.get("EMP_NAME").toString();
				String pendingDeptId = paymentReqs.get("REQUEST_PENDING_DEPT_ID")==null ? "" : paymentReqs.get("REQUEST_PENDING_DEPT_ID").toString();
				String accPendingEmpId = paymentReqs.get("ACC_REQUEST_PENDING_EMP_ID")==null ? "" : paymentReqs.get("ACC_REQUEST_PENDING_EMP_ID").toString();
				double accTransactionAmount = Double.valueOf(paymentReqs.get("ACC_REQ_AMOUNT")==null ? "0" : paymentReqs.get("ACC_REQ_AMOUNT").toString());
				
				objPaymentBean.setDoubleAmountToBeReleased(reqAmount);
				if(accTransactionAmount!=0){
					objPaymentBean.setDoubleAmountToBeReleased(accTransactionAmount);
				}
				//objPaymentBean.setDoubleTransactionAmount(accTransactionAmount);
				if(StringUtils.isNotBlank(pendingEmpId) && !pendingEmpId.equals("-")){
					objPaymentBean.setStrPendingDeptName("SITE");
					objPaymentBean.setStrPendingEmpName(pendingEmpName);
				}
				else if(StringUtils.isNotBlank(pendingDeptId) && pendingDeptId.contains(accDeptId)){
					objPaymentBean.setStrPendingDeptName("Accounts Department");
					
					if(accPendingEmpId.contains("2")){
						objPaymentBean.setStrPendingEmpName("Second Level");
					}
					else if(accPendingEmpId.contains("3")){
						objPaymentBean.setStrPendingEmpName("Third Level");
					}
					else {
						objPaymentBean.setStrPendingEmpName("First Level");
					}
					
					if(accPendingEmpId.equals("VND")){
						objPaymentBean.setStrPendingDeptName("VND");
						objPaymentBean.setStrPendingEmpName("VND");
					}
				}    
				
				objPaymentBean.setPaymentType(paymentType);
				objPaymentBean.setBillNumber(billNumber);
				objPaymentBean.setDoubleBillAmount(billAmount);
				objPaymentBean.setRaBillNo(raBillNo);
				objPaymentBean.setDoubleRaBillAmount(raBillAmount);
				objPaymentBean.setDoubleAdvBillAmount(advBillAmount);
				objPaymentBean.setStrAdvBillNo(advBillNo);
				objPaymentBean.setDoubleSdBillAmount(sdBillAmount);
				objPaymentBean.setStrSdBillNo(sdBillNo);
				
				
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				try{
					
					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat time1 = new SimpleDateFormat("hh:mm aa");
					
					if(!raBillDate.equals("")){

						Date raBill_Date = dt.parse(raBillDate);
						raBillDate = dt1.format(raBill_Date);
						}
					if(!workOrderDate.equals("")){

					Date workOrder_Date = dt.parse(workOrderDate);
					workOrderDate = dt1.format(workOrder_Date);
					}
					if(!advBillDate.equals("")){

						Date advBill_Date = dt.parse(advBillDate);
						advBillDate = dt1.format(advBill_Date);
						}
					if(!sdBillDate.equals("")){

					Date secBill_Date = dt.parse(sdBillDate);
					sdBillDate = dt1.format(secBill_Date);
					}
					if(!paymentReqDate.equals("")){

						Date paymentReq_Date = dt.parse(paymentReqDate);
						paymentReqDate = dt1.format(paymentReq_Date);
						}
					if(!paymentIntiatedDate.equals("")){

						Date paymentIntiated_Date = dt.parse(paymentIntiatedDate);
						paymentIntiatedDate = dt1.format(paymentIntiated_Date);
						}
					
				}
			
				catch(Exception e){
					e.printStackTrace();
				}
				objPaymentBean.setStrRaBillDate(raBillDate);
				objPaymentBean.setStrWorkOrderDate(workOrderDate);
				objPaymentBean.setStrAdvBillDate(advBillDate);
				objPaymentBean.setStrSdBillDate(sdBillDate);
				objPaymentBean.setStrPaymentReqDate(paymentReqDate);
				objPaymentBean.setStrCreatedDate(paymentIntiatedDate);			
				
				list.add(objPaymentBean);
		

			}





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			objPaymentBean = null;
			
		}
		return list;
	}

	@Override
	public List<PaymentBean> getContractorPaymentDetailsToUpdate(String user_id, String fromDate,String toDate, String selected_contractorId, String selected_workOrderNo, String dropdown_SiteId) {

		String query = "";
		List<Map<String, Object>> paymentDtls = null;
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null;

		try {

			
			
				query = " select ACPT.PAYMENT_TRANSACTIONS_ID,ACPT.PAID_AMOUNT,ACPT.UTR_CHQNO,ACPT.PAYMENT_DATE,ACPT.CREATION_DATE,ACPT.PAYMENT_MODE,"
					  + " ACPT.CNT_PAYMENT_ID,ACPT.REMARKS,"
					  + "ACP.BILL_DATE,ACP.BILL_NUMBER,ACP.BILL_AMOUNT,"
					  + " ACP.PAYMENT_REQ_AMOUNT,ACP.WO_AMOUNT,ACP.WO_DATE,ACP.WO_NUMBER,"
					  + " ACP.SITE_ID,ACP.CONTRACTOR_ID,ACP.BILL_ID,ACP.PAYMENT_TYPE,ACP.SITEWISE_PAYMENT_NO,ACP.PARTICULAR,S.SITE_NAME,SC.FIRST_NAME||' '||SC.LAST_NAME as CONTRACTOR_NAME ,"
         
         
					  /*=============  to get remarks from QS_CONT_TMP_BILL_APPR_REJ_DTLS  ===========*/
					  + " (SELECT LISTAGG(CONCAT(CONCAT(SED1.EMP_NAME,'  :  '),QCTBARD.REMARKS), '@@@')"
					  + " WITHIN GROUP (ORDER BY QCTBARD.QS_BILL_APPR_REJ_DTLS) "
					  + " from QS_CONT_TMP_BILL_APPR_REJ_DTLS QCTBARD,QS_CONTRACTOR_BILL QCB1,SUMADHURA_EMPLOYEE_DETAILS SED1" 
					  + " where SED1.EMP_ID = QCTBARD.EMP_ID and QCTBARD.TEMP_BILL_ID = QCB1.TEMP_BILL_ID and QCB1.BILL_ID = ACP.BILL_ID and QCB1.CONT_SEQ_BILL_NO = ACP.BILL_NUMBER  and regexp_like(QCTBARD.REMARKS, '[a-zA-Z0-9]')) as QS_APR_REJ_REMARKS ," 
				 
					  /*=============  to get remarks from ACC_CNT_PMT_SITE_APPR_REJ_DTLS  ===========*/
					  + "(SELECT LISTAGG(CONCAT(CONCAT(SED3.EMP_NAME,'  :  '),ACPSARD.REMARKS), '@@@')"
					  + "WITHIN GROUP (ORDER BY ACPSARD.PAYMENT_APROV_REJECT_SEQ) "
					  + "from ACC_CNT_PMT_SITE_APPR_REJ_DTLS ACPSARD,SUMADHURA_EMPLOYEE_DETAILS SED3 "
					  + "where SED3.EMP_ID = ACPSARD.EMP_ID and ACPSARD.CNT_PAYMENT_ID = ACP.CNT_PAYMENT_ID and regexp_like(ACPSARD.REMARKS, '[a-zA-Z0-9]') ) as SITE_PMT_APP_REJ_REMARKS ,  "
					  + ""
					  /*=============  to get remarks from ACC_CNT_PMT_APPR_REJECT_DTLS  ===========*/
					  + " (SELECT LISTAGG(CONCAT(CONCAT(SED2.EMP_NAME,'  :  '),ACPARD.REMARKS), '@@@')"
					  + " WITHIN GROUP (ORDER BY ACPARD.PAYMENT_APROV_REJECT_SEQ) "
					  + " from ACC_CNT_PMT_APPR_REJECT_DTLS ACPARD,SUMADHURA_EMPLOYEE_DETAILS SED2" 
					  + " where SED2.EMP_ID = ACPARD.EMP_ID and ACPARD.CNT_PAYMENT_ID = ACPT.CNT_PAYMENT_ID and regexp_like(ACPARD.REMARKS, '[a-zA-Z0-9]')  ) as ACC_APP_REJ_REMARKS"  
				 
         
					  + " from ACC_CNT_PAYMENT_TRANSACTIONS ACPT,ACC_CNT_PAYMENT ACP,SUMADHURA_CONTRACTOR SC,SITE S  "
					  + " where ACP.CNT_PAYMENT_ID = ACPT.CNT_PAYMENT_ID and ACP.SITE_ID = S.SITE_ID "
					  + " and  ACP.CONTRACTOR_ID = SC.CONTRACTOR_ID and ACPT.IS_UPDATED = 'N'";
				if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					query = query + " and TRUNC(ACPT.CREATION_DATE)   BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
					//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
				} else if (StringUtils.isNotBlank(fromDate)) {
					query = query + " and TRUNC(ACPT.CREATION_DATE)   =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
				} else if(StringUtils.isNotBlank(toDate)) {
					query = query + " and TRUNC(ACPT.CREATION_DATE)   <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
				}
			
				if(StringUtils.isNotBlank(selected_contractorId)){
					query = query+" and ACP.CONTRACTOR_ID='"+selected_contractorId+"'";
				}
				if(StringUtils.isNotBlank(dropdown_SiteId)){
					query = query+" and ACP.SITE_ID='"+dropdown_SiteId+"'";
				}
				if (StringUtils.isNotBlank(selected_workOrderNo)){
					query = query + " and ACP.WO_NUMBER = '"+selected_workOrderNo+"'";
				}
				
				
				// this block of code for - to display payments as per AccountsDepartment is HYD or BANGLORE.
				/** take dept_id of login employee.
				 ** decide address hyd or banglore.
				 ** take list of sites in that address.
				 ** form a String using sites to pass into query.
				 *
				 *for Hyderabad sites should show hyderabad sites only for banglore can show everyone(hyderabad and banglore
				
				 *Site level also using same query site accountent will not available dept_id
				 */
				String dept_id = jdbcTemplate.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+user_id+"'", new Object[]{},String.class);
				String address = "";
				if(dept_id.contains("B")){
					address="BANGLORE";
				}
				else if(dept_id.contains("H")){
					address="HYD";
				}
				List<String> siteIdList = jdbcTemplate.queryForList("select SITE_ID from SITE where ADDRESS like '%"+address+"%'", new Object[]{},String.class);
				if(siteIdList.size()>0){
					String groupOfSites = "";
					for(String site:siteIdList){
						groupOfSites +="'"+site+"',";
					}
					groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
					query = query + " and ACP.SITE_ID in("+groupOfSites+")";
				}
				/***************************************/


			paymentDtls = jdbcTemplate.queryForList(query, new Object[]{});
			int i = 0;
			for(Map<String, Object> paymentReqs : paymentDtls) {
				i = i+1;
				String contractorId = paymentReqs.get("CONTRACTOR_ID")==null ? "" : paymentReqs.get("CONTRACTOR_ID").toString();
				String contractorName = paymentReqs.get("CONTRACTOR_NAME")==null ? "" : paymentReqs.get("CONTRACTOR_NAME").toString();
				String site_id = paymentReqs.get("SITE_ID")==null ? "" : paymentReqs.get("SITE_ID").toString();
				String site_name = paymentReqs.get("SITE_NAME")==null ? "" : paymentReqs.get("SITE_NAME").toString();
				String workOrderNo = paymentReqs.get("WO_NUMBER")==null ? "" : paymentReqs.get("WO_NUMBER").toString();
				double paidAmount = Double.valueOf(paymentReqs.get("PAID_AMOUNT")==null ? "" : paymentReqs.get("PAID_AMOUNT").toString());
				double workOrderAmount = Double.valueOf(paymentReqs.get("WO_AMOUNT")==null ? "0" : paymentReqs.get("WO_AMOUNT").toString());

				String billNumber = paymentReqs.get("BILL_NUMBER")==null ? "" : paymentReqs.get("BILL_NUMBER").toString();
				double billAmount = Double.valueOf(paymentReqs.get("BILL_AMOUNT")==null ? "0" : paymentReqs.get("BILL_AMOUNT").toString());
				String billDate = paymentReqs.get("BILL_DATE")==null ? "" : paymentReqs.get("BILL_DATE").toString();
				String paymentType = paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString();
				
				String raBillNo,advBillNo,sdBillNo,raBillDate,advBillDate,sdBillDate;
				raBillNo=advBillNo=sdBillNo=raBillDate=advBillDate=sdBillDate = "";
				double raBillAmount,advBillAmount,sdBillAmount;
				raBillAmount=advBillAmount=sdBillAmount = 0;
				if(paymentType.equals("RA")){
					raBillNo = billNumber;
					raBillAmount = billAmount; 
					raBillDate = billDate;
				}
				if(paymentType.equals("ADV")){
					advBillNo = billNumber;
					advBillAmount = billAmount;  
					advBillDate = billDate;
				}
				if(paymentType.equals("SEC")){
					sdBillNo = billNumber;
					sdBillAmount = billAmount; 
					sdBillDate = billDate; 
				}

				objPaymentBean = new PaymentBean();

				objPaymentBean.setIntSerialNo(i);
				objPaymentBean.setWorkOrderNo(workOrderNo);
				objPaymentBean.setDoublePaidAmount(paidAmount);
				objPaymentBean.setStrContractorId(contractorId);
				objPaymentBean.setStrContractorName(contractorName);
				objPaymentBean.setStrSiteId(site_id);
				objPaymentBean.setStrSiteName(site_name);
				objPaymentBean.setDoubleWorkOrderAmount(workOrderAmount);
				
				objPaymentBean.setIntPaymentTransactionId(Integer.parseInt(paymentReqs.get("PAYMENT_TRANSACTIONS_ID")==null ? "" : paymentReqs.get("PAYMENT_TRANSACTIONS_ID").toString()));
				objPaymentBean.setIntCntPaymentId(Integer.parseInt(paymentReqs.get("CNT_PAYMENT_ID")==null ? "" :   paymentReqs.get("CNT_PAYMENT_ID").toString()));
				String remarks = "";
				String qs_app_rej_remarks = paymentReqs.get("QS_APR_REJ_REMARKS")==null ? "" :   paymentReqs.get("QS_APR_REJ_REMARKS").toString();
				String site_app_rej_remarks = paymentReqs.get("SITE_PMT_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("SITE_PMT_APP_REJ_REMARKS").toString();
				String acc_app_rej_remarks = paymentReqs.get("ACC_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("ACC_APP_REJ_REMARKS").toString();
				if(StringUtils.isNotBlank(qs_app_rej_remarks)){remarks+=qs_app_rej_remarks;}
				if(StringUtils.isNotBlank(site_app_rej_remarks)){
					if(StringUtils.isBlank(remarks)){remarks=site_app_rej_remarks;} else{remarks+="@@@"+site_app_rej_remarks;}
				}
				if(StringUtils.isNotBlank(acc_app_rej_remarks)){
					if(StringUtils.isBlank(remarks)){remarks=acc_app_rej_remarks;} else{remarks+="@@@"+acc_app_rej_remarks;}
				}
				objPaymentBean.setStrRemarks(remarks);
				objPaymentBean.setStrRemarksForView(remarks.replace("@@@", ","));
				objPaymentBean.setStrRemarksForTitle(remarks.replace("@@@", "&#013;"));
				objPaymentBean.setPaymentType(paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString());
				String workOrderDate = paymentReqs.get("WO_DATE")==null ? "" : paymentReqs.get("WO_DATE").toString();
				String paymentReqDate = paymentReqs.get("PAYMENT_REQ_DATE")==null ? "" :   paymentReqs.get("PAYMENT_REQ_DATE").toString();
				String paymentDate = paymentReqs.get("PAYMENT_DATE")==null ? "" : paymentReqs.get("PAYMENT_DATE").toString();
				objPaymentBean.setIntSiteWisePaymentId(Integer.parseInt(paymentReqs.get("SITEWISE_PAYMENT_NO")==null ? "0" : paymentReqs.get("SITEWISE_PAYMENT_NO").toString()));
				objPaymentBean.setUtrChequeNo(paymentReqs.get("UTR_CHQNO")==null ? "" : paymentReqs.get("UTR_CHQNO").toString());
				objPaymentBean.setStrBillId(paymentReqs.get("BILL_ID")==null ? "" : paymentReqs.get("BILL_ID").toString());
				String paymentMode = paymentReqs.get("PAYMENT_MODE")==null ? "" : paymentReqs.get("PAYMENT_MODE").toString();
				objPaymentBean.setPaymentMode(paymentMode);
				
				objPaymentBean.setPaymentType(paymentType);
				objPaymentBean.setBillNumber(billNumber);
				objPaymentBean.setDoubleBillAmount(billAmount);
				objPaymentBean.setStrBillAmount(String.format("%.2f",billAmount));
				objPaymentBean.setRaBillNo(raBillNo);
				objPaymentBean.setDoubleRaBillAmount(raBillAmount);
				objPaymentBean.setDoubleAdvBillAmount(advBillAmount);
				objPaymentBean.setStrAdvBillNo(advBillNo);
				objPaymentBean.setDoubleSdBillAmount(sdBillAmount);
				objPaymentBean.setStrSdBillNo(sdBillNo);
				
				
				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

				try{

					SimpleDateFormat dt1 = new SimpleDateFormat("dd/MM/yyyy");
					SimpleDateFormat time1 = new SimpleDateFormat("hh:mm aa");

					if(!raBillDate.equals("")){

						Date raBill_Date = dt.parse(raBillDate);
						raBillDate = dt1.format(raBill_Date);
					}
					if(!workOrderDate.equals("")){

						Date workOrder_Date = dt.parse(workOrderDate);
						workOrderDate = dt1.format(workOrder_Date);
					}
					if(!advBillDate.equals("")){

						Date advBill_Date = dt.parse(advBillDate);
						advBillDate = dt1.format(advBill_Date);
					}
					if(!sdBillDate.equals("")){

						Date secBill_Date = dt.parse(sdBillDate);
						sdBillDate = dt1.format(secBill_Date);
					}
					if(!billDate.equals("")){
			    		Date received_date = dt.parse(billDate);
			    		billDate = dt1.format(received_date);
			    	}
					if(!paymentDate.equals("")){

						Date payment_Date = dt.parse(paymentDate);
						paymentDate = dt1.format(payment_Date);
					}
					

				}

				catch(Exception e){
					e.printStackTrace();
				}
				objPaymentBean.setStrRaBillDate(raBillDate);
				objPaymentBean.setStrWorkOrderDate(workOrderDate);
				objPaymentBean.setStrAdvBillDate(advBillDate);
				objPaymentBean.setStrSdBillDate(sdBillDate);
				objPaymentBean.setStrBillDate(billDate);
				objPaymentBean.setStrPaymentReqDate(paymentReqDate);
				objPaymentBean.setStrPaymentDate(paymentDate);			

				
				list.add(objPaymentBean);
			}





		} catch (Exception ex) {
			ex.printStackTrace();
			//log.debug("Exception = "+ex.getMessage());
			//logger.info("Exception Occured Inside getViewIndentIssueDetails() in IndentIssueDao class --"+ex.getMessage());
		} finally {
			query = "";
			objPaymentBean = null; 
			
		}
		return list;
	}

	@Override
	public int updateRefNoInAccDeptContractorTransaction(String strRefNo, int intPaymentTransactionId,
			String paymentMode, String paymentDate) {
		String query = "update ACC_CNT_PAYMENT_TRANSACTIONS set UTR_CHQNO = ?,PAYMENT_MODE = ?,PAYMENT_DATE = ?,IS_UPDATED='Y'  where PAYMENT_TRANSACTIONS_ID = ? ";

		int	count = jdbcTemplate.update(query, new Object[] {
				strRefNo, paymentMode, DateUtil.convertToJavaDateFormat(paymentDate), intPaymentTransactionId

			});
	return count;
}

	@Override
	public int updateRefNoInContractorInvoiceHistory(String strRefNo, String paymentMode, int intPaymentTransactionId) {
		String query = "update ACC_CNT_INVOICE_HISTORY set REF_NO = ?,PAYMENT_MODE = ?  where PAYMENT_TRANSACTIONS_ID = ? ";

		int	count = jdbcTemplate.update(query, new Object[] {
				strRefNo, paymentMode, intPaymentTransactionId

			});
		return count;
	}

	@Override
	public List<PaymentBean> downloadInvoicePaymentsToExcelFileForUpdate(String user_id,String fromDate, String toDate) {

		String query = "";
		String convertpodate = "";
		double invoiceAmount=0.0;
		double poAmount=0.0;
		List<Map<String, Object>> dbIndentDts = null;
		
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean indentObj = null;
		
		

		try {
			
			/******************************************** Invoice Payments *************************************************/
			

					query = " select APT.PAYMENT_TRANSACTIONS_ID,APT.PAYMENT_DETAILS_ID,APT.PAID_AMOUNT,APT.PAYMENT_TYPE,"
							+"APT.PAYMENT_DATE,APT.PAYMENT_MODE,APT.UTR_CHQNO,APT.REMARKS,APT.SITEWISE_PAYMENT_NO,"
							+"S.SITE_NAME,VD.VENDOR_NAME,AP.INVOICE_NUMBER,AP.INVOICE_AMOUNT,AP.PAYMENT_ID,AP.INVOICE_DATE,AP.INVOICE_RECEIVED_DATE,AP.PAYMENT_REQ_UPTO,AP.PAYMENT_DONE_UPTO,"
							+"AP.PO_NUMBER,AP.PO_DATE,AP.PO_AMOUNT,AP.CREDIT_NOTE_NUMBER,AP.CREDIT_TOTAL_AMOUNT,"
							+"AP.VENDOR_ID,AP.SITE_ID,AP.INDENT_ENTRY_ID " 
							+"from ACC_PAYMENT_TRANSACTIONS APT,ACC_PAYMENT_DTLS APD,SITE S,VENDOR_DETAILS VD,"
							+"ACC_PAYMENT AP " 
							+"where  AP.PAYMENT_ID = APD.PAYMENT_ID " 
							+"and APD.PAYMENT_DETAILS_ID = APT.PAYMENT_DETAILS_ID " 
							+"and S.SITE_ID = AP.SITE_ID " 
							+"and VD.VENDOR_ID = AP.VENDOR_ID "
							+ "and APT.TYPE_OF_PAYMENT_SETTLEMENT = 'REGULAR'"
							+ "and APT.UPDATION = 'A' and APT.PAYMENT_MODE = 'ONLINE'";
							
					if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
						query = query + " AND TRUNC(APT.CREATION_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
						//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
					} else if (StringUtils.isNotBlank(fromDate)) {
						query = query + " AND TRUNC(APT.CREATION_DATE) =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
					} else if(StringUtils.isNotBlank(toDate)) {
						query = query + " AND TRUNC(APT.CREATION_DATE) <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
					}

					// this block of code for - to display payments as per AccountsDepartment is HYD or BANGLORE.
					/** take dept_id of login employee.
					 ** decide address hyd or banglore.
					 ** take list of sites in that address.
					 ** form a String using sites to pass into query.
					 **for Hyderabad sites should show hyderabad sites only for banglore can show everyone(hyderabad and banglore
				
					 *Site level also using same query site accountent will not available dept_id
					 */
					String dept_id = jdbcTemplate.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+user_id+"'", new Object[]{},String.class);
					String address = "";
					if(dept_id.contains("B")){
						address="BANGLORE";
					}
					else if(dept_id.contains("H")){
						address="HYD";
					}
					List<String> siteIdList = jdbcTemplate.queryForList("select SITE_ID from SITE where ADDRESS like '%"+address+"%'", new Object[]{},String.class);
					if(siteIdList.size()>0){
						String groupOfSites = "";
						for(String site:siteIdList){
							groupOfSites +="'"+site+"',";
						}
						groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
						query = query + " and AP.SITE_ID in("+groupOfSites+")";
					}
					/***************************************/

					query = query +" order by APT.CREATION_DATE asc";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{});
			int serialNo = 0;
			for(Map<String, Object> prods : dbIndentDts) {
				serialNo++;
				indentObj = new PaymentBean();
				indentObj.setIntSerialNo(serialNo);
				indentObj.setIsVNDorCNT("VND");
				indentObj.setStrInvoiceNo(prods.get("INVOICE_NUMBER")==null ? "" : prods.get("INVOICE_NUMBER").toString());
				indentObj.setStrPONo(prods.get("PO_NUMBER")==null ? "" : prods.get("PO_NUMBER").toString());
				String invoicedate=prods.get("INVOICE_DATE")==null ? "" : prods.get("INVOICE_DATE").toString();
				String podate=prods.get("PO_DATE")==null ? "" : prods.get("PO_DATE").toString();
				indentObj.setStrVendorName(prods.get("VENDOR_NAME")==null ? "" : prods.get("VENDOR_NAME").toString());
				indentObj.setStrVendorId(prods.get("VENDOR_ID")==null ? "" : prods.get("VENDOR_ID").toString());
				indentObj.setStrSiteId(prods.get("SITE_ID")==null ? "" : prods.get("SITE_ID").toString());
				invoiceAmount=Double.parseDouble(prods.get("INVOICE_AMOUNT")==null ? "" : prods.get("INVOICE_AMOUNT").toString());
				poAmount=Double.parseDouble(prods.get("PO_AMOUNT")==null ? "" : prods.get("PO_AMOUNT").toString());
				indentObj.setDoublePaidAmount(Double.parseDouble(prods.get("PAID_AMOUNT")==null ? "" : prods.get("PAID_AMOUNT").toString()));
				//payment date in db is not updated by client. it is 3rd level approval date
				//that why we commented below line. later it will update in excel by client
				//--After Few days 
				//AGAIN ENABLED as per client request only for excel download
				String  paymentDate = prods.get("PAYMENT_DATE")==null ? "" : prods.get("PAYMENT_DATE").toString();
				String receviedDate=prods.get("INVOICE_RECEIVED_DATE")==null ? "" : prods.get("INVOICE_RECEIVED_DATE").toString();
				String remarks = prods.get("REMARKS")==null ? "" : prods.get("REMARKS").toString();
				indentObj.setStrRemarks(remarks);
				indentObj.setStrRemarksForView(remarks.replace("@@@", ","));
				indentObj.setIntPaymentId(Integer.parseInt(prods.get("PAYMENT_ID")==null ? "0" : prods.get("PAYMENT_ID").toString()));
				
				indentObj.setIntPaymentDetailsId(Integer.parseInt(prods.get("PAYMENT_DETAILS_ID")==null ? "0" : prods.get("PAYMENT_DETAILS_ID").toString()));
				indentObj.setIntSiteWisePaymentId(Integer.parseInt(prods.get("SITEWISE_PAYMENT_NO")==null ? "0" : prods.get("SITEWISE_PAYMENT_NO").toString()));
				indentObj.setStrSiteName(prods.get("SITE_NAME")==null ? "" : prods.get("SITE_NAME").toString());
				String paymentType = prods.get("PAYMENT_TYPE")==null ? "" : prods.get("PAYMENT_TYPE").toString();
				indentObj.setPaymentType(paymentType);
				String paymentMode = prods.get("PAYMENT_MODE")==null ? "" : prods.get("PAYMENT_MODE").toString();
				indentObj.setPaymentMode(paymentMode);
				
				indentObj.setUtrChequeNo(prods.get("UTR_CHQNO")==null ? "" : prods.get("UTR_CHQNO").toString());
								
				indentObj.setDoubleInvoiceAmount(invoiceAmount);
				indentObj.setDoublePOTotalAmount(poAmount);
				
				indentObj.setIntPaymentTransactionId(Integer.parseInt(prods.get("PAYMENT_TRANSACTIONS_ID")==null ? "" : prods.get("PAYMENT_TRANSACTIONS_ID").toString()));
				
				indentObj.setDoubleCreditTotalAmount(Double.valueOf(prods.get("CREDIT_TOTAL_AMOUNT")==null ? "0" : prods.get("CREDIT_TOTAL_AMOUNT").toString()));
				indentObj.setStrCreditNoteNumber(prods.get("CREDIT_NOTE_NUMBER")==null ? "" : prods.get("CREDIT_NOTE_NUMBER").toString());
				
				indentObj.setIntIndentEntryId(Integer.parseInt(prods.get("INDENT_ENTRY_ID")==null ? "0" : prods.get("INDENT_ENTRY_ID").toString()));
			    
				
				indentObj.setDoublePaymentDoneUpto(Double.valueOf(prods.get("PAYMENT_DONE_UPTO")==null ? "0" : prods.get("PAYMENT_DONE_UPTO").toString()));
			    indentObj.setDoublePaymentRequestedUpto(Double.valueOf(prods.get("PAYMENT_REQ_UPTO")==null ? "0" : prods.get("PAYMENT_REQ_UPTO").toString()));
			    

				SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				
				try{

					/*Date receive_date = dt.parse(receviedDate);*/
					
					SimpleDateFormat dt2 = new SimpleDateFormat("dd-MMM-yy");
					SimpleDateFormat time1 = new SimpleDateFormat("HH:mm:ss");
					if(!paymentDate.equals("")){

						Date p_date = dt.parse(paymentDate);
						paymentDate = dt2.format(p_date);
					}
					
				}
			
				catch(Exception e){
					e.printStackTrace();
				}
			
				indentObj.setStrPaymentDate(paymentDate);
				//in ADVANCE, emptying Invoice details
				if(paymentType.equals("ADVANCE")){
					indentObj.setStrInvoiceNo("");
					indentObj.setStrInvoiceDate("");
					indentObj.setStrReceiveDate("");
					indentObj.setStrInvoiceAmount("");
					indentObj.setDoubleInvoiceAmount(0);
					indentObj.setStrCreditNoteNumber("");
					indentObj.setDoubleCreditTotalAmount(0);
				}
				list.add(indentObj);
			}
			/******************************************** End of Invoice Payments *************************************************/
			
		} catch (Exception ex) {
			ex.printStackTrace();
		  } finally {
			query = "";
			indentObj = null;
			dbIndentDts = null;
		}
		return list;

}
	@Override
	public List<PaymentBean> downloadContractorPaymentsToExcelFileForUpdate(String user_id,String fromDate, String toDate) {

		String query = "";
		String convertpodate = "";
		double invoiceAmount=0.0;
		double poAmount=0.0;
		List<Map<String, Object>> paymentDtls = null;
		
		List<PaymentBean> list = new ArrayList<PaymentBean>();
		PaymentBean objPaymentBean = null;

		

		try {			

			/******************************************** Contractor Payments *************************************************/
			

			
			
			query = " select ACPT.PAYMENT_TRANSACTIONS_ID,ACPT.PAID_AMOUNT,ACPT.UTR_CHQNO,ACPT.PAYMENT_DATE,ACPT.CREATION_DATE,ACPT.PAYMENT_MODE,"
				  + " ACPT.CNT_PAYMENT_ID,ACPT.REMARKS, "
				  + "ACP.BILL_DATE,ACP.BILL_NUMBER,ACP.BILL_AMOUNT,"
				  + " ACP.PAYMENT_REQ_AMOUNT,ACP.WO_AMOUNT,ACP.WO_DATE,ACP.WO_NUMBER,"
				  + " ACP.SITE_ID,ACP.CONTRACTOR_ID,ACP.BILL_ID,ACP.PAYMENT_TYPE,ACP.SITEWISE_PAYMENT_NO,ACP.PARTICULAR,S.SITE_NAME,SC.FIRST_NAME||' '||SC.LAST_NAME as CONTRACTOR_NAME ,"
     
     
				  /*=============  to get remarks from QS_CONT_TMP_BILL_APPR_REJ_DTLS  ===========*/
				  + " (SELECT LISTAGG(CONCAT(CONCAT(SED1.EMP_NAME,'  :  '),QCTBARD.REMARKS), '@@@')"
				  + " WITHIN GROUP (ORDER BY QCTBARD.QS_BILL_APPR_REJ_DTLS) "
				  + " from QS_CONT_TMP_BILL_APPR_REJ_DTLS QCTBARD,QS_CONTRACTOR_BILL QCB1,SUMADHURA_EMPLOYEE_DETAILS SED1" 
				  + " where SED1.EMP_ID = QCTBARD.EMP_ID and QCTBARD.TEMP_BILL_ID = QCB1.TEMP_BILL_ID and QCB1.BILL_ID = ACP.BILL_ID and QCB1.CONT_SEQ_BILL_NO = ACP.BILL_NUMBER  and regexp_like(QCTBARD.REMARKS, '[a-zA-Z0-9]')) as QS_APR_REJ_REMARKS ," 
			 
				  /*=============  to get remarks from ACC_CNT_PMT_SITE_APPR_REJ_DTLS  ===========*/
				  + "(SELECT LISTAGG(CONCAT(CONCAT(SED3.EMP_NAME,'  :  '),ACPSARD.REMARKS), '@@@')"
				  + "WITHIN GROUP (ORDER BY ACPSARD.PAYMENT_APROV_REJECT_SEQ) "
				  + "from ACC_CNT_PMT_SITE_APPR_REJ_DTLS ACPSARD,SUMADHURA_EMPLOYEE_DETAILS SED3 "
				  + "where SED3.EMP_ID = ACPSARD.EMP_ID and ACPSARD.CNT_PAYMENT_ID = ACP.CNT_PAYMENT_ID and regexp_like(ACPSARD.REMARKS, '[a-zA-Z0-9]') ) as SITE_PMT_APP_REJ_REMARKS ,  "
				  + ""
				  /*=============  to get remarks from ACC_CNT_PMT_APPR_REJECT_DTLS  ===========*/
				  + " (SELECT LISTAGG(CONCAT(CONCAT(SED2.EMP_NAME,'  :  '),ACPARD.REMARKS), '@@@')"
				  + " WITHIN GROUP (ORDER BY ACPARD.PAYMENT_APROV_REJECT_SEQ) "
				  + " from ACC_CNT_PMT_APPR_REJECT_DTLS ACPARD,SUMADHURA_EMPLOYEE_DETAILS SED2" 
				  + " where SED2.EMP_ID = ACPARD.EMP_ID and ACPARD.CNT_PAYMENT_ID = ACPT.CNT_PAYMENT_ID and regexp_like(ACPARD.REMARKS, '[a-zA-Z0-9]')  ) as ACC_APP_REJ_REMARKS"  
			 
     
				  + " from ACC_CNT_PAYMENT_TRANSACTIONS ACPT,ACC_CNT_PAYMENT ACP,SUMADHURA_CONTRACTOR SC,SITE S  "
				  + " where ACP.CNT_PAYMENT_ID = ACPT.CNT_PAYMENT_ID and ACP.SITE_ID = S.SITE_ID "
				  + " and  ACP.CONTRACTOR_ID = SC.CONTRACTOR_ID and ACPT.IS_UPDATED = 'N' and ACPT.PAYMENT_MODE = 'ONLINE'";
			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query = query + " and TRUNC(ACPT.CREATION_DATE)   BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
				//query = "SELECT LD.USERNAME, IE.REQUESTER_NAME, IE.REQUESTER_ID, IED.PRODUCT_NAME, IED.SUB_PRODUCT_NAME, IED.CHILD_PRODUCT_NAME, IED.ISSUED_QTY FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED, LOGIN_DUMMY LD WHERE IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' AND IE.SITE_ID='"+siteId+"' AND LD.UNAME=IE.USER_ID AND IE.ENTRY_DATE BETWEEN '"+fromDate+"' AND '"+toDate+"'";
			} else if (StringUtils.isNotBlank(fromDate)) {
				query = query + " and TRUNC(ACPT.CREATION_DATE)   =TO_DATE('"+fromDate+"', 'dd-MM-yy')";
			} else if(StringUtils.isNotBlank(toDate)) {
				query = query + " and TRUNC(ACPT.CREATION_DATE)   <=TO_DATE('"+toDate+"', 'dd-MM-yy')";
			}
		
			
			
			
			// this block of code for - to display payments as per AccountsDepartment is HYD or BANGLORE.
			/** take dept_id of login employee.
			 ** decide address hyd or banglore.
			 ** take list of sites in that address.
			 ** form a String using sites to pass into query.
			 *
			 *for Hyderabad sites should show hyderabad sites only for banglore can show everyone(hyderabad and banglore
			
			 *Site level also using same query site accountent will not available dept_id
			 */
			String dept_id = jdbcTemplate.queryForObject("select DEPT_ID from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID = '"+user_id+"'", new Object[]{},String.class);
			String address = "";
			if(dept_id.contains("B")){
				address="BANGLORE";
			}
			else if(dept_id.contains("H")){
				address="HYD";
			}
			List<String> siteIdList = jdbcTemplate.queryForList("select SITE_ID from SITE where ADDRESS like '%"+address+"%'", new Object[]{},String.class);
			if(siteIdList.size()>0){
				String groupOfSites = "";
				for(String site:siteIdList){
					groupOfSites +="'"+site+"',";
				}
				groupOfSites = groupOfSites.substring(0,groupOfSites.length()-1);
				query = query + " and ACP.SITE_ID in("+groupOfSites+")";
			}
			/***************************************/


		paymentDtls = jdbcTemplate.queryForList(query, new Object[]{});
		int i = 0;
		for(Map<String, Object> paymentReqs : paymentDtls) {
			i = i+1;
			String contractorId = paymentReqs.get("CONTRACTOR_ID")==null ? "" : paymentReqs.get("CONTRACTOR_ID").toString();
			String contractorName = paymentReqs.get("CONTRACTOR_NAME")==null ? "" : paymentReqs.get("CONTRACTOR_NAME").toString();
			String site_id = paymentReqs.get("SITE_ID")==null ? "" : paymentReqs.get("SITE_ID").toString();
			String site_name = paymentReqs.get("SITE_NAME")==null ? "" : paymentReqs.get("SITE_NAME").toString();
			String workOrderNo = paymentReqs.get("WO_NUMBER")==null ? "" : paymentReqs.get("WO_NUMBER").toString();
			double paidAmount = Double.valueOf(paymentReqs.get("PAID_AMOUNT")==null ? "" : paymentReqs.get("PAID_AMOUNT").toString());
			double workOrderAmount = Double.valueOf(paymentReqs.get("WO_AMOUNT")==null ? "0" : paymentReqs.get("WO_AMOUNT").toString());

			String billNumber = paymentReqs.get("BILL_NUMBER")==null ? "" : paymentReqs.get("BILL_NUMBER").toString();
			double billAmount = Double.valueOf(paymentReqs.get("BILL_AMOUNT")==null ? "0" : paymentReqs.get("BILL_AMOUNT").toString());
			String billDate = paymentReqs.get("BILL_DATE")==null ? "" : paymentReqs.get("BILL_DATE").toString();
			String paymentType = paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString();
			
			String raBillNo,advBillNo,sdBillNo,raBillDate,advBillDate,sdBillDate;
			raBillNo=advBillNo=sdBillNo=raBillDate=advBillDate=sdBillDate = "";
			double raBillAmount,advBillAmount,sdBillAmount;
			raBillAmount=advBillAmount=sdBillAmount = 0;
			if(paymentType.equals("RA")){
				raBillNo = billNumber;
				raBillAmount = billAmount; 
				raBillDate = billDate;
			}
			if(paymentType.equals("ADV")){
				advBillNo = billNumber;
				advBillAmount = billAmount;  
				advBillDate = billDate;
			}
			if(paymentType.equals("SEC")){
				sdBillNo = billNumber;
				sdBillAmount = billAmount; 
				sdBillDate = billDate; 
			}
			objPaymentBean = new PaymentBean();

			objPaymentBean.setIntSerialNo(i);
			objPaymentBean.setIsVNDorCNT("CNT");
			objPaymentBean.setWorkOrderNo(workOrderNo);
			objPaymentBean.setDoublePaidAmount(paidAmount);
			objPaymentBean.setStrContractorId(contractorId);
			objPaymentBean.setStrContractorName(contractorName);
			objPaymentBean.setStrSiteId(site_id);
			objPaymentBean.setStrSiteName(site_name);
			objPaymentBean.setDoubleWorkOrderAmount(workOrderAmount);
			
			objPaymentBean.setIntPaymentTransactionId(Integer.parseInt(paymentReqs.get("PAYMENT_TRANSACTIONS_ID")==null ? "" : paymentReqs.get("PAYMENT_TRANSACTIONS_ID").toString()));
			objPaymentBean.setIntCntPaymentId(Integer.parseInt(paymentReqs.get("CNT_PAYMENT_ID")==null ? "" :   paymentReqs.get("CNT_PAYMENT_ID").toString()));
			objPaymentBean.setStrBillId(paymentReqs.get("BILL_ID")==null ? "" : paymentReqs.get("BILL_ID").toString());
			String remarks = "";
			String qs_app_rej_remarks = paymentReqs.get("QS_APR_REJ_REMARKS")==null ? "" :   paymentReqs.get("QS_APR_REJ_REMARKS").toString();
			String site_app_rej_remarks = paymentReqs.get("SITE_PMT_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("SITE_PMT_APP_REJ_REMARKS").toString();
			String acc_app_rej_remarks = paymentReqs.get("ACC_APP_REJ_REMARKS")==null ? "" :   paymentReqs.get("ACC_APP_REJ_REMARKS").toString();
			if(StringUtils.isNotBlank(qs_app_rej_remarks)){remarks+=qs_app_rej_remarks;}
			if(StringUtils.isNotBlank(site_app_rej_remarks)){
				if(StringUtils.isBlank(remarks)){remarks=site_app_rej_remarks;} else{remarks+="@@@"+site_app_rej_remarks;}
			}
			if(StringUtils.isNotBlank(acc_app_rej_remarks)){
				if(StringUtils.isBlank(remarks)){remarks=acc_app_rej_remarks;} else{remarks+="@@@"+acc_app_rej_remarks;}
			}
			objPaymentBean.setStrRemarks(remarks);
			objPaymentBean.setStrRemarksForView(remarks.replace("@@@", ","));
			objPaymentBean.setStrRemarksForTitle(remarks.replace("@@@", "&#013;"));
			objPaymentBean.setPaymentType(paymentReqs.get("PAYMENT_TYPE")==null ? "" :   paymentReqs.get("PAYMENT_TYPE").toString());
			String workOrderDate = paymentReqs.get("WO_DATE")==null ? "" : paymentReqs.get("WO_DATE").toString();
			String paymentReqDate = paymentReqs.get("PAYMENT_REQ_DATE")==null ? "" :   paymentReqs.get("PAYMENT_REQ_DATE").toString();
			String paymentDate = paymentReqs.get("PAYMENT_DATE")==null ? "" : paymentReqs.get("PAYMENT_DATE").toString();
			objPaymentBean.setIntSiteWisePaymentId(Integer.parseInt(paymentReqs.get("SITEWISE_PAYMENT_NO")==null ? "0" : paymentReqs.get("SITEWISE_PAYMENT_NO").toString()));
			objPaymentBean.setUtrChequeNo(paymentReqs.get("UTR_CHQNO")==null ? "" : paymentReqs.get("UTR_CHQNO").toString());
			String paymentMode = paymentReqs.get("PAYMENT_MODE")==null ? "" : paymentReqs.get("PAYMENT_MODE").toString();
			objPaymentBean.setPaymentMode(paymentMode);
			
			objPaymentBean.setPaymentType(paymentType);
			objPaymentBean.setBillNumber(billNumber);
			objPaymentBean.setDoubleBillAmount(billAmount);
			objPaymentBean.setRaBillNo(raBillNo);
			objPaymentBean.setDoubleRaBillAmount(raBillAmount);
			objPaymentBean.setDoubleAdvBillAmount(advBillAmount);
			objPaymentBean.setStrAdvBillNo(advBillNo);
			objPaymentBean.setDoubleSdBillAmount(sdBillAmount);
			objPaymentBean.setStrSdBillNo(sdBillNo);
			
			
			SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			try{

				SimpleDateFormat dt2 = new SimpleDateFormat("dd-MMM-yy");
				SimpleDateFormat time1 = new SimpleDateFormat("hh:mm aa");

				
				if(!paymentDate.equals("")){

					Date payment_Date = dt.parse(paymentDate);
					paymentDate = dt2.format(payment_Date);
				}
				

			}

			catch(Exception e){
				e.printStackTrace();
			}
			objPaymentBean.setStrPaymentDate(paymentDate);			

			
			list.add(objPaymentBean);
		}





	/******************************************** End of Contractor Payments *************************************************/
		} catch (Exception ex) {
			ex.printStackTrace();
		  } finally {
			query = "";
			objPaymentBean = null;
			paymentDtls = null;
		}
		return list;

}

	@Override
	public int updateVendorPaymentRefNoFromExcel(String strRefNo, int intPaymentTransactionId,	java.util.Date paymentDate) {
		String query = "update ACC_PAYMENT_TRANSACTIONS set UTR_CHQNO = ?,PAYMENT_DATE = ?,UPDATION='I'  where PAYMENT_TRANSACTIONS_ID = ? and UPDATION='A' ";

			int	count = jdbcTemplate.update(query, new Object[] {
					strRefNo, paymentDate, intPaymentTransactionId

				});
		return count;
	}
	
	@Override
	public int updateContractorPaymentRefNoFromExcel(String strRefNo, int intPaymentTransactionId, java.util.Date paymentDate) {
		String query = "update ACC_CNT_PAYMENT_TRANSACTIONS set UTR_CHQNO = ?,PAYMENT_DATE = ?,IS_UPDATED='Y'  where PAYMENT_TRANSACTIONS_ID = ? and IS_UPDATED='N' ";

		int	count = jdbcTemplate.update(query, new Object[] {
				strRefNo, paymentDate, intPaymentTransactionId

			});
	return count;
}
	@Override
	public int revertPendingEmployeeInAccCntPayment(int accDeptPmtProcessId, int cntPaymentId) {
		String query = "select REQ_RECEIVE_FROM from ACC_CNT_ACCOUNTS_DEPT_PMT_PROSS where ACCOUNTS_DEPT_PMT_PROSS_ID = ? ";
		String pendingEmpId = jdbcTemplate.queryForObject(query,new Object[]{accDeptPmtProcessId} , String.class);
		
		String sql = "update ACC_CNT_PAYMENT set REQUEST_PENDING_EMP_ID = ?, REQUEST_PENDING_DEPT_ID='-' where CNT_PAYMENT_ID = ? ";
		int count = jdbcTemplate.update(sql,new Object[]{pendingEmpId,cntPaymentId});
		return count;
	}

}
