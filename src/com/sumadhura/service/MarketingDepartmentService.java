package com.sumadhura.service;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.MarketingDeptBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.IndentCreationDto;

public interface MarketingDepartmentService {

	public String SaveMarketingPoDetails(Model model,HttpServletRequest request, HttpSession session,String strFinacialYear);
	public String loadAndSetSiteInfo(String siteName);
	public Map<String, String> loadAndSetLocationData(String childProductId);
	public List<IndentCreationBean> ViewPoPendingforApproval(String fromDate, String toDate, String strUserId,String tempPoNumber);
	public String getDetailsforPoApproval(String poNumber, String siteId,HttpServletRequest request);
	public String getMarketingPoDetailsList(String poNumber, String siteId,HttpServletRequest request);
	public Map<Double,List<MarketingDeptBean>> getAllViewExpenditures(String invoiceId);
	public List<MarketingDeptBean> getAllViewExpenditures(String invoiceFromDate,String invoiceToDate);
	public  Map<Double,List<MarketingDeptBean>> getViewMyHoardingDetails(String fromDate,String toDate,String Site);	
	public void updateExpenditure();

	public List<MarketingDeptBean> getAvailableAreaForSaleOnMonthWise(String strMonth); 

	public   String getAvailableAreaForSale(  String strLocation,String month_year);
	public   String updateAvailableArea(HttpSession session,HttpServletRequest request);		
	public Map<String, String> getLocationData(String siteName);

	public int getExpenditureId();
	public int getExpenditureDetailsId();
	public int insertMarketExpenditure(MarketingDeptBean objMarketingExpenditure);
	public int insertMarketExpenditureDtls(MarketingDeptBean objMarketingExpenditureDtls);
	public MarketingDeptBean expenditureDetails(Integer expendatureId);
	public LinkedList getAllViewExpenditures(String invoiceId,String invoiceToDate,String invoiceFromDate );
	public String SaveMarketingPoApproveDetails(final String tempPONumber, String siteId,String userId,HttpServletRequest request,String isCancelTempPO);
	public int[] insertMarketExpenditureDtls( final List<MarketingDeptBean> objMarketingExpenditureDtlsList);
	public String processingMarketingPOasInvoice(Model model, HttpServletRequest request, HttpSession session);
	public String modifyMarketingPo(HttpSession session,HttpServletRequest request,String temp_Po_Number,String user_id,String site_id);// modify temp 
	public   String addAvailableArea(HttpSession session,HttpServletRequest request);// add available area
	public int insertAndUpdatePrevMarketExpenditure(MarketingDeptBean objMarketingExpenditure);
	public Map<Double,List<MarketingDeptBean>> getAllViewExpendituresWithVendorData(String invoiceId,String vendorId,String invoiceDate);// this is for with vendor data also added
	public String cancelMarketingPoDetails(HttpSession session ,HttpServletRequest request);
	public List<MarketingDeptBean> getAllViewExpendituresDates(String invoiceFromDate,String invoiceToDate,String siteIds,String invoiceDate,String vendorId); // only view purpose
	public Map<Double,List<MarketingDeptBean>> getAllViewExpendituresWithInvoiceData(String invoiceId,String vendorId,String invoiceDate,String siteIds,String productId,String subProductId,String childPRoductId,String fromDate,String toDate);
	public List<MarketingDeptBean> getAllViewExpendituresForVendorName(String invoiceId,String vendorId,String invoiceDate);// this is update expenditure time invoice date and vendor name
	public String RejectMailTempPO(HttpSession session ,HttpServletRequest request); 
	public List<ProductDetails> getLocationFieldData(String poNumber,boolean isModify)throws ParseException;
	public   boolean checkAvailableAreaCreatedOrnot(String month);
	public String modifyTempPo(HttpSession session,HttpServletRequest request,String temp_Po_Number,String user_id,String site_id);
	public boolean checkIsMarketingUpdateOrNot(HttpSession session,HttpServletRequest request,boolean isRevised,boolean isModifyPo);
	public void sendMarketingTempPoMailCommonData(String temp_Po_Number,String mailComments,List<String> listOfDetails,String subject,String type,int intPortNo,String [] ccMails); // this is for send mail for modify temp po time to previius emp
	public List<ProductDetails> getModifyMarketingTempPODetails(String poNumber, String reqSiteId);
	public String gettingProductData(String fromdate,String toDate);
	public String selectedProductDetailsForGraph(String fromDate,String toDate,String SiteData);
	
}
