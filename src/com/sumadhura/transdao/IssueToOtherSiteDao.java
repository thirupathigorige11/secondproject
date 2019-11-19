package com.sumadhura.transdao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.sumadhura.bean.IssueToOtherSiteInwardBean;
import com.sumadhura.dto.IssueToOtherSiteDto;


public interface IssueToOtherSiteDao {
	
	public Map<String, String> issueToOtherSiteProducts(String siteId);
	public String issueToOtherSiteSubProducts(String prodId);
	public String issueToOtherSiteChildProducts(String subProductId);
	public String issueToOtherSiteMeasurements(String childProdId);
	public int insertRequesterData(int indentEntryId, String userId, String siteId, IssueToOtherSiteDto reqDto,String strToSite);
	public int insertIndentIssueData(int indentEntrySeqNum, IssueToOtherSiteDto isDto);
	public int updateIndentAvalibility(IssueToOtherSiteDto indIssueDto, String siteId);
	public void updateIndentAvalibilityWithNewIndent(IssueToOtherSiteDto indIssueDto, String siteId);
	public int getIndentEntrySequenceNumber();
	public String doIndentIssueToOtherSite(Model model, HttpServletRequest request, HttpSession session);
	public String getIssueToOtherSiteProductAvailability(String prodId, String subProductId, String childProdId, String measurementId, String siteId);
	public Map<String, String> loadSitesInformation();
	public List<IssueToOtherSiteDto> getPriceListDetails(IssueToOtherSiteDto issueDto, String strSiteId);
	public void updateIndentEntryAmountColumn(String valueOf, String valueOf2, String valueOf3);
	public int insertIndentIssueData(int indentEntrySeqNum, IssueToOtherSiteDto issueDto, String amount, String valueOf,
			String priceId, String strUserId, String strSiteId,String expiryDate);
	public void updatePriceListDetails(String string, String string2, String priceId);
	public void updateIssueDetailsSumadhuClosingBalByProduct(IssueToOtherSiteDto issueDto, String strSiteId,
			String amount, double issuQuantiy);
	public List<IssueToOtherSiteDto> getPriceListDetails(IssueToOtherSiteDto issueDto, String strSiteId, String string);
	public int insertIntremidiatoryTabbleData(int indentEntryDetailsSeqNum, IssueToOtherSiteDto issueDto, String basicAmt, String quantity, String priceId,int IndentEntrySeqNo);
	
	public int insertIndentReceiveData(int intIndentEntryDetailsSeqNum,int indentEntrySeqNum, IssueToOtherSiteDto irdto, String userId, String site_id,int inrSumadhuraPriceListSeqNo);
	public int updateIndentAvalibilityInwards(IssueToOtherSiteDto irdto,String strSite_id);
	public String getIndentAvailableId(IssueToOtherSiteDto irdto, String site_id);
	public void saveReciveDetailsIntoSumduraPriceList(int intIndentEntryDetailsSeqNum,IssueToOtherSiteDto irdto, String invoiceNumber, String siteId, String id,int intSumadhuraPriceListSeqNo);
	public String getProductAvailabilitySequenceNumber();
	public void saveReceivedDataIntoSumadhuClosingBalByProduct(IssueToOtherSiteDto irdto, String siteId); 
	public int insertInvoiceData(int indentEntrySeqNum, IssueToOtherSiteDto objIssueToOtherSiteDto);
	public int  updateIntermidiatoryTabledata(String strIntrmidiatendentEntryId);
	public void updateInvoiceOtherCharges(IssueToOtherSiteDto objIssueToOtherSiteDto,String strSiteId,int strindentEntryId);


	//receiving from other site
	
	public List<IssueToOtherSiteInwardBean> getIssueLists(HttpSession session,String requestId, String siteId, String strIssueType);

	public List<IssueToOtherSiteInwardBean> getGetProductDetailsLists(HttpSession session,String requestId, String siteId,String strIssueType);


	public List<IssueToOtherSiteInwardBean> getTransportChargesList(String requestId, String siteId);
	
	public List<Map<String, Object>> getSiteDetails(String requesterId);
	
	public int getEntryDetailsSequenceNumber();
	
	public String getVendorState(String fromSite, String toSite);

	public Map<String, String> loadSitesInformationForIssueToOtherSite();
	
	public String getVendorOrContractorState(String strSiteId, String string, String moduleName, String vendorId, String indentType);
	public List<Map<String, Object>> getIndentCreatedEmpName(String string, String indentNumber);
	public String loadIndentRequestedQuantity(
			IssueToOtherSiteDto objIssueToOtherSiteDto, String invoiceNumber);
}
