package com.sumadhura.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.TransportChargesBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;

public interface InwardToGetInvoiceDetailsService {

	List<GetInvoiceDetailsBean> getIssuesToOtherLists(String indentEntryId, String siteId, String vendorName,String invoiceDate);
	List<GetInvoiceDetailsBean> getGetInvoiceDetailsLists(String invoiceNumber, String siteId, String vendorName);
	List<GetInvoiceDetailsBean> getGetInvoiceDetailsLists2(String invoiceNumber, String siteId,String vendorId,String invoiceDate, String indentType);
	List<GetInvoiceDetailsBean> getTransportChargesList(String invoiceNumber, String siteId,String indentEntryId);
	public String updateInvoice(Model model, HttpServletRequest request, HttpSession session);
	public String getVendorNameAndIndentId(String invoiceNumber, String siteId, String vendorName);
	public String getGrnDetails(String invoiceNumber, String siteId,HttpServletRequest request);
	public List<ViewIndentIssueDetailsBean> getGrnViewDetails(String fromDate, String toDate, String siteId);
	//public String getIssuesToOtherListsGrn(String invoiceNumber, String siteId);
	List<GetInvoiceDetailsBean> getInvoiceDetails(String invoiceNumber, String siteId, String vendorId,String invoiceDate, String indentType);
	List<GetInvoiceDetailsBean> getTransportChargesList2(String invoiceNumber, String siteId, String vendorId,String invoiceDate, String indentType);
	List<String> loadAllVendorNames(String vendorName);
	public String  getVendorNameAndIndentEntryId(String invoiceNumber,String vendorName1,String invoiceDate);
	List<GetInvoiceDetailsBean>  getTaxInvoiceSubmissionDetails(String strIndentEntryId, String invoiceNumber, String siteId,
			String vendorId);
}
