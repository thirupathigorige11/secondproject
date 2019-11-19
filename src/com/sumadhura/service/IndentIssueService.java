package com.sumadhura.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.sumadhura.bean.IndentIssueBean;
import com.sumadhura.dto.IndentIssueDto;
import com.sumadhura.dto.IndentIssueRequesterDto;
import com.sumadhura.dto.IndentReceiveDto;

public interface IndentIssueService {

	public Map<String, String> loadProds(String siteId);
	public String loadSubProds(String prodId);
	public String loadChildProds(String subProductId);
	public String loadIndentIssueMeasurements(String childProdId,String strSiteId);
	public int insertRequesterData(int indentEntryId, String userId, String siteId, IndentIssueRequesterDto reqDto);
	public int insertIndentIssueData(int indentEntrySeqNum, IndentIssueDto isDto);
	public int updateIndentAvalibility(IndentIssueDto indIssueDto, String siteId);
	public void updateIndentAvalibilityWithNewIndent(IndentIssueDto indIssueDto, String siteId);
	public int getIndentEntrySequenceNumber();
	public String indentIssueProcess(Model model, HttpServletRequest request, HttpSession session);
	public String loadProductAvailability(String prodId, String subProductId, String childProdId, String measurementId,String requesteddate, HttpServletRequest request, HttpSession session,String groupId,String isReceive);
	public Map<String, String> loadBlockDetails(String siteId);
	public String getProjectName(HttpSession session);
	public String getFloorDetails(String blockId);
	public String getFlatDetails(String floorId);
	public void updateIssueDetailsIntoSumadhuraCloseBalance(IndentReceiveDto irdto, String siteId);
	public void updateIssueDetailsSumadhuClosingBalByProduct(IndentIssueDto issueDto, String strSiteId, String priceId, Double IssueQty) ;
	public int getIssueCount( String  strSlipNumber, String strSiteId,String strReqMonthStart, String strReceiveDate);
	public int getIssueCount1(HttpServletRequest request);
	public String getContractorInfo(String contractorName);
	public String getEmployerInfo(String employeeName);
	public String getEmployerid(String employeeName);
	public String getEmployerName(String employeeid);
	public String checkExpiryDate(String prodId, String subProductId, String childProdId, String siteId,String requesteddate,double issuedQuantity,HttpServletRequest request, HttpSession session); // this is for expiry date
	public String getWdDetails(String workOrderId, String siteId);
	public String getWDblockDetails(HttpServletRequest request);
	public String getFloorDataDetails(String wdId);
	public String getFlatDataDetails(String wdId);
	public String isWorkOrderExistsInSite(IndentIssueBean iib);
	
}
