package com.sumadhura.util;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sumadhura.bean.AuditLogDetailsBean;

public class SaveAuditLogDetails {

	static JdbcTemplate template = null;
	public static void saveAuditLogDetails(AuditLogDetailsBean aldb)  {

		String auditLogQuery = "";
		
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			auditLogQuery = "INSERT INTO SUMADHURA_AUDIT_LOGS (LOG_ID , LOGIN_ID, SITE_ID, OPERATION_NAME, OPERATION_DATE, STATUS,ENTRY_DETAILS_ID) VALUES (audit_log_id.NEXTVAL,?,?,?,SYSDATE,?,?)";
			template.update(auditLogQuery, new Object[] {
					aldb.getLoginId(), aldb.getSiteId(), aldb.getOperationName(), aldb.getStatus(), aldb.getEntryDetailsId()
			});
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	


	

public static void auditLog(String indentEntrySeqNum,String userId,String strOperationName,String info,String siteId){
	AuditLogDetailsBean auditBean=new AuditLogDetailsBean();
	//String indentEntrySeqNum="";
    auditBean = new AuditLogDetailsBean();
	auditBean.setEntryDetailsId(indentEntrySeqNum);
	auditBean.setLoginId(userId);
	auditBean.setOperationName(strOperationName);
	auditBean.setStatus(info);
	auditBean.setSiteId(siteId);
    saveAuditLogDetails(auditBean);

}


	
	
	public int getIndentEntryDetailsId() {
		
		int indentEntryDetailsId = 0;
		String query = "";
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			query = "SELECT INDENT_ENTRY_DETAILS_ID FROM INDENT_ENTRY_DETAILS WHERE ROWNUM <= 1 ORDER BY INDENT_ENTRY_DETAILS_ID DESC";
			indentEntryDetailsId = template.queryForInt(query);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return indentEntryDetailsId;
	}
}
