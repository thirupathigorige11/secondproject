package com.sumadhura.transdao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.TransportChargesBean;
import com.sumadhura.util.DBConnection;
import com.sun.istack.internal.logging.Logger;
@Repository("TransportChargesDetailsDaoClass")
public class TransportChargesDetailsDaoImpl implements TransportChargesDetailsDao{
	
static Logger log = Logger.getLogger(TransportChargesDetailsDaoImpl.class);
	
	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<TransportChargesBean> getTransportChargesList(String invoiceNumber, String siteId) {
    List<Map<String, Object>> productList = null;
		
		List<TransportChargesBean> GetTransportChargesListDetails = new ArrayList<TransportChargesBean>();
		TransportChargesBean objGetTransportChargesDetails=null;
		JdbcTemplate template = null;
		String sql = "";

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			if (StringUtils.isNotBlank(invoiceNumber) ) {
				sql = "select * from SUMADHURA_TRANSPORT_CHARGES where site_id=? AND INDENT_ENTRY_INVOICE_ID=?";
				productList = template.queryForList(sql, new Object[] {invoiceNumber, siteId});
			} 
			if (null != productList && productList.size() > 0) {
				for (Map<?, ?> GetTransportChargesDetails : productList) {

					objGetTransportChargesDetails = new TransportChargesBean();

					objGetTransportChargesDetails.setTransportId(GetTransportChargesDetails.get("TRANSPORT_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_ID").toString());
					objGetTransportChargesDetails.setTransportGstPercentage(GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_PERCENTAGE").toString());
					objGetTransportChargesDetails.setTransportGstAmount(GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_GST_AMOUNT").toString());
					objGetTransportChargesDetails.setTotalAmountAfterGstTax(GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX") == null ? "" : GetTransportChargesDetails.get("TOTAL_AMOUNT_AFTER_GST_TAX").toString());
					objGetTransportChargesDetails.setTransportInvoiceId(GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_INVOICE_ID").toString());
					objGetTransportChargesDetails.setIndentEntryInvoiceId(GetTransportChargesDetails.get("INDENT_ENTRY_INVOICE_ID") == null ? "" : GetTransportChargesDetails.get("INDENT_ENTRY_INVOICE_ID").toString());
					objGetTransportChargesDetails.setDateAndTime(GetTransportChargesDetails.get("DATE_AND_TIME") == null ? "": GetTransportChargesDetails.get("DATE_AND_TIME").toString());
					objGetTransportChargesDetails.setTransportAmount(GetTransportChargesDetails.get("TRANSPORT_AMOUNT") == null ? "" : GetTransportChargesDetails.get("TRANSPORT_AMOUNT").toString());
					
					
					GetTransportChargesListDetails.add(objGetTransportChargesDetails);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return GetTransportChargesListDetails;
	}

}
