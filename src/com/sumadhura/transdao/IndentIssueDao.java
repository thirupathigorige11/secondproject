package com.sumadhura.transdao;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.sumadhura.bean.IndentIssueBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentIssueDto;
import com.sumadhura.dto.IndentIssueRequesterDto;

public interface IndentIssueDao {

	public Map<String, String> loadProds(String siteId);
	public String loadSubProds(String prodId);
	public String loadChildProds(String subProductId);
	public String loadIndentIssueMeasurements(String childProdId,String siteId);
	public int insertRequesterData(int indentEntryId, String userId, String siteId, IndentIssueRequesterDto reqDto);
	public int insertIndentIssueData(int indentEntrySeqNum, IndentIssueDto inIsDto, String basicAmt, String totAmt, String priceId, String userId, String siteId,String expiryDate);
	public int updateIndentAvalibility(IndentIssueDto indentIssuDto, String siteId);
	public void updateIndentAvalibilityWithNewIndent(IndentIssueDto indentIssDto, String siteId);
	public int getIndentEntrySequenceNumber();
	public String getProductAvailability(String prodId, String subProductId, String childProdId, String measurementId,String requesteddate,String siteId,String goupId,String isReceieve);
	public Map<String, String> loadBlockDetails(String strSiteId);
	public String getProjectName(HttpSession session);
	public String getFloorDetails(String blockId);
	public String getFlatDetails(String floorId);
	public List<IndentIssueDto>  getPriceListDetails(IndentIssueDto issueDto, String siteId);
	public List<IndentIssueDto>  getPriceListDetails(IndentIssueDto issueDto, String siteId, String All);
	public void updatePriceListDetails(String quantity, String status, String priceId);
	public void updateIndentEntryAmountColumn(String amount, String quantity, String indentId);
	public void updateIssueDetailsIntoSumadhuraCloseBalance(IndentIssueDto issueDto, String strSiteId, String priceId,  Double IssueQty);
	public void updateIssueDetailsSumadhuClosingBalByProduct(IndentIssueDto issueDto, String strSiteId, String priceId, Double IssueQty) ;
	public int getIssuesCount(String strSlipNo,String strSiteId,String strFromDate,String strToDate);
	public int getIssuesCount1(String strSlipNumber, String strSiteId, String strReqDate);
	public String getContractorInfo(String contractorName);
	public String getEmployerInfo(String employeeName);
	public List<ViewIndentIssueDetailsBean> getViewInvoiceIssueDetails(String fromDate, String toDate, String siteId,
			String val);
	public String getEmployerid(String employeeName);
	public String getEmployerName(String employeeid);
	public String getWdDetails(String workOrderId, String siteId);
	public String getWDblockDetails(String wdId, String materialGroupId, String workOrderNo, String siteId); // this is for getting the block id
	public String getFloorDataDetails(String wdId);
	public String getFlatDataDetails(String wdId);
	public double gettingWorkorderQuantity(IndentIssueDto issueDto);
	public String isWorkOrderExistsInSite(IndentIssueBean iib);
}
