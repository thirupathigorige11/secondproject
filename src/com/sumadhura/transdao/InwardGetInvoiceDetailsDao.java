package com.sumadhura.transdao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.TransportChargesBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentReceiveDto;

public interface InwardGetInvoiceDetailsDao {

	List<GetInvoiceDetailsBean> getIssueLists(String indentEntryId, String siteId, String vendorName,String invoiceDate);
	
	List<GetInvoiceDetailsBean> getGetInvoiceDetailsLists(String invoiceNumber, String siteId, String vendorName);
	List<GetInvoiceDetailsBean> getTransportChargesList(String invoiceNumber, String siteId,String indentEntryId);
	public void updateInvoiceOtherCharges(GetInvoiceDetailsBean irdto,String strSiteId,String strindentEntryId);
	public void updateInvoiceIndentEntry(GetInvoiceDetailsBean irdto,String vendorId);
	public void updateInvoiceIndentEntryDetails(GetInvoiceDetailsBean irdto);
//	public String getTransportChargesListForGRN(String invoiceNumber, String siteId, String state);
	public String getTransportChargesListForGRN(String invoiceNumber,String strSiteId,String state, String strVendorId, String invoiceDate,String indent_entry_Id);
	public String getVendorNameAndIndentId(String invoiceNumber, String siteId, String vendorName);
	
	public String getProductDetails(String invoiceNumber, String siteId,String strVendorId, String invoiceDate);
	public String getVendorDetails(String invoiceNumber, String siteId,String strVendorId, String invoiceDate, String selectIndentType); 
	public List<ViewIndentIssueDetailsBean> getGrnViewDetails(String fromDate, String toDate, String siteId);

	List<GetInvoiceDetailsBean> getInvoiceDetails(String invoiceNumber, String siteId, String vendorId,String invoiceDate, String indentType);

	List<GetInvoiceDetailsBean> getGetInvoiceDetailsLists2(String invoiceNumber, String siteId, String vendorId,String invoiceDate, String indentType);

	List<GetInvoiceDetailsBean> getTransportChargesList2(String invoiceNumber, String siteId, String vendorId,String invoiceDate, String indentType);

	public String getGrnNumberForGRN(String indentEntryId, String site_id,String vendorId);

	List<String> loadAllVendorNames(String vendorName);
	
	public String getVendorNameAndIndentEntryId(String invoiceNumber,String vendorName1,String invoiceDate);
	public int updateIndentIndentAvailability(GetInvoiceDetailsBean irdto,double strQuantity);
	public int updatePoentryDetailsQuantity(GetInvoiceDetailsBean irdto,String poNo,String strQuantity);
	public int inActivePOentrytbl(String indent_Entry_Id,String poNumber);
	public int updateInvoiceOtherChargesEdited(GetInvoiceDetailsBean objGetInvoiceDetailsBean,String id);// tranport details in update invoice details
	public int updateInvoiceOtherChargesDelete(String id);// transport details in update invoice details
	public boolean checkPaymentDoneOrNotOnInvoice(String invoiceNumber,String site_id,String vendorId,String receviedDate,String siteName,int portNumber,String indentEntryId);
	public boolean  updateInvoiceAccPaymenttbl(HttpServletRequest request,String invoiceNumber,String site_id,String vendorId,String ttlAmntForIncentEntryTbl,String indentEntryId);
List<GetInvoiceDetailsBean> getTaxInvoiceSubmissionDetails(String indentEntryId, String invoiceNumber, String siteId);
	
}
