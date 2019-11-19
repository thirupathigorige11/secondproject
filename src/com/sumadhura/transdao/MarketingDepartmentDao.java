package com.sumadhura.transdao;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.ProductDetails;

import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.TransportChargesDto;

public interface MarketingDepartmentDao {

	public List<Map<String, Object>> getVendorOrSiteAddress(String siteId);

	public int insertPOEntry(MarketingDeptBean productDetails);

	public int insertTempPOEntry(MarketingDeptBean productDetails,String tempuserId, String ccEmailId, String subject,String selectedSite,String siteNumberOrNot);

	public List<String> getBillingAddGstin(String receiverState);

	public String getTemproryuser(String strUserId);

	public String getPermenentPoNumber(String poState, String type,String siteId, String financialyear);
	public Set<String> getLocationDetails();
	public String loadAndSetSiteInfo(String siteName);
	public int insertPOTransportDetails(int poTransChrgsSeqNo, MarketingDeptBean productDetails, TransportChargesDto transportChargesDto);
	public int insertPOTempTransportDetails(int poTransChrgsSeqNo, MarketingDeptBean productDetails, TransportChargesDto transportChargesDto);
	List<String> calculateGstAmount(HttpServletRequest request,String tax,String strVendorGSTIN,String receiverState,String taxAmount);
	public int insertPOEntryDetails(MarketingDeptBean productDetails,int poEntrySeqNo);
	public int insertTempPOEntryDetails(MarketingDeptBean productDetails,String poEntrySeqNo);
	public int insertPoAreaWiseData(int poEntryId ,String type_Po,String location_Wise);
	public int saveLocationdetailsData(MarketingDeptBean productDetails,String po_entryDetailsId,String poNumber);
	public int saveTempLocationdetailsData(MarketingDeptBean productDetails,String po_entryDetailsId,String poNumber);
	public String getDeptId(String userId);
	public Map<String,String> loadAndSetLocationData(String childProductId);
	public List<IndentCreationBean> ViewPoPendingforApproval(String fromDate, String toDate, String strUserId,String tempPoNumber);
	public String getPendingVendorDetails(String poNumber, String siteId,HttpServletRequest request,String siteName); // get temporary vendor details
	public String getPendingProductDetails(String poNumber, String siteId,HttpServletRequest request,String deliverySiteState);
	public String getPendingTransportChargesList(String poNumber,String strSiteId,String gstinumber,HttpServletRequest request,String deliverySiteState);
	public String getPendingTempLocationList(String poNumber);
	public String getMarketingVendorDetails(String poNumber, String siteId,HttpServletRequest request,String siteName,String vedorId);// get perminent vendor details
	public String getMarketingProductDetails(String poNumber, String siteId,HttpServletRequest request,String receiverState);// get perminet product details
	public String getMarketingTransportChargesListForGRN(String poEntryId,String strSiteId,String gstinumber,HttpServletRequest request,String poNumber,String receiverState); // get perminent transportation details 
	public String getPendingperminentLocationList(String poNumber);// PERMINET LOCATION DETAILS
	public int updateTempMarketingPoEntry(String approvalEmpId,String poNumber,String ccmailId,String passwdForMail,String deliveryDate);
	public String getAndMarketingsaveVendorDetails(String tempPONumber, String siteId,String user_id,HttpServletRequest request,String revision_No,String oldPoNumber,String siteName,String deliveryDate);
	public Map<String,String> getAndsaveMarketingPoProductDetails(String tempPONumber, String siteId,HttpServletRequest request,String premPONumber,int intPOEntrySeqId,String old_Po_Number); // to save product details 
	public String getAndsaveMarketingTransportChargesList(String tempPONumber,String strSiteId,HttpServletRequest request,int poEntryId);
	public String getAndsaveMarketingLocationDetailsList(String tempPONumber,Map<String, String> LocationDetails,String permPoNumber);// to save maketing details perminent
	public List<String>  getAllEmployeeEmailsMarketingDepartment(String deptId); // to get marketing employes emails for send mails
	public Map<String, String> loadProds(); //get the products for marketing po
	public int updateMarketingpoEntrydetails(String poNumber,String passwdForMail,String deliveryDate);

	public List<MarketingDeptBean> getAvailableAreaForSaleOnMonthWise(String strMonth,String siteIds);


	public Map<String, String> getLocationData(String siteName);

	public   String getAvailableAreaForSale(  String strLocation,String month_year);
	public int insertNewAvailableArea(String strCreatedBy,String strSiteId,String strUOM,double doubleTotalArea,double doubleAvailArea,String strMonth_Year,String status);
	public int updateAvailableArea(String strUpdatedBy,String strSiteId,String strUOM,double doubleTotalArea,double doubleAvailArea,String strMonth_Year,String strStatus);
	public boolean inactiveAvailableArea(String strUpdatedBy,String strSiteId,String strMonth_Year,String strStatus,List<String> hydList,List<String> bngList,HttpServletRequest request);

	public double getTotalAvailableAreaInMonth(String strMonth,String siteIds);
	public List<Map<String, Object>> getExpendatureDetilsInvoiceProductWise(int strExpendatureId,List<String> removeList);
	public List<Map<String, Object>> getMonthlyWiseExpendatureInvoices(String strExpendatureType,String strMonth_Year);
	public int insertOrUpdateExpendaturedtlsTable( int intExpendatureId,String strChildProductId,double doubleQty,double doubleRate,double doubleAmount,String strSiteId,String userId);


	public List<Map<String,Object>> getAllViewExpenditures(String invoiceId);
	public List<Map<String,Object>> getAllViewExpenditures(String invoiceFromDate,String invoiceToDate);
	public List<Map<String,Object>> getViewMyHoardingDetails(String fromDate,String toDate,String Site);
	public void updateExpenditure();
	public int getExpenditureId();
	public int getExpenditureDetailsId();
	public int insertMarketExpenditure(MarketingDeptBean objMarketingExpenditure);
	public int insertMarketExpenditureDtls(MarketingDeptBean objMarketingExpenditureDtls);

	public  List<Map<String,Object>> expenditureDetails(Integer expendatureId);

	public LinkedList getAllViewExpenditures(String invoiceId,String invoiceToDate,String invoiceFromDate );

	public int[] insertMarketExpenditureDtls( final List<MarketingDeptBean> objMarketingExpenditureDtlsList);

	public int insertAndUpdatePrevMarketExpenditure(MarketingDeptBean objMarketingExpenditure);
	public int[] insertMarketExpenditureDtlsDuringUpdateExpendatureTime( final List<MarketingDeptBean> objMarketingExpenditureDtlsList);
	public List<Map<String,Object>> getAllViewExpendituresWithVendorData(String invoiceId,String vendorId,String invoiceDate); //this is for vendor id and view expenditure
	public List<String> getApproveMailDetails(String tempPoNumber,String userId);
	public int updateTablesOnTempPORejection(String ponumber,String status);
	public List<Map<String,Object>> getAllViewExpendituresDates(String invoiceFromDate,String invoiceToDate,String siteIds,String invoiceDate,String vendorId);// this is only for expenditure
	public List<Map<String,Object>> getAllViewExpendituresWithInvoiceData(String invoiceId,String vendorId,String invoiceDate,String siteIds,String productId,String subProductId,String childproductId,String fromDate,String toDate); // THIS IS FOR SITEId ALSO ADDEDWITH INVOICE ID
	public List<Map<String,Object>> getAllViewExpendituresForVendorName(String invoiceId,String vendorId,String invoiceDate); // this is for update expenditure time vendor give only invoice or vendor name
	public List<String> getEmployeesEmailsForMD(String tempPONumber);
	public List<String> getProjectAddGstin(String receiverState);// this is for delivery address as per client requirement
	public Map<String,String> getLocationBrandingData(List<String> list);
	public double getTotalAvailableAreaInMonthForHyd(String strMonth,boolean isHyd);
	public int insertOrUpdateExpendaturedtlsTableLocation( int intExpendatureId,String strChildProductId,double doubleQty,double doubleRate,double doubleAmount,String strSiteId,String userId);
	public List<ProductDetails> getLocationFieldData(String poNumber,boolean isModify)throws ParseException;
	public String getPermenentRevisedPoNumber(String editPonumber);
	public boolean checkAvailableAreaCreatedOrnot(String month);
	public String gettingSiteNameForSiteWise(List<ProductDetails> poDetails);
	public Map<String,Double> getRemoveAreaProductWise(int strExpendatureId,List<String> removeList);
	public String  getpendingEmpId(String poNumber,String  user_Id);
	public List<String> getApprovalEmpMails(String tempPoNumber); // this is for getting the email ids for only marketing people at the time modify temp po
	public String updateReceivedQuantity(Map<String,String> locationDetails,String oldPoEntryId);
	public List<ProductDetails> getModifyMarketingTempPODetails(String poNumber, String reqSiteId);
	public int updateMarketingTempPo(String temp_Po_Number,String siteId);
	public List<String> gettingPOEntryId(String oldPONumber);
	public List<ProductDetails> gettingOldPoReceivedQuantity(String poNumber,String reqSiteId,String oldPoEntryId);
	public String gettingProductData(String fromDate,String toDate);
	public String selectedProductDetailsForGraph(String fromDate,String toDate,String SiteData);
	
}
