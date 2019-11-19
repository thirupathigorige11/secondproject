package com.sumadhura.service;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;

import com.sumadhura.bean.IssueToOtherSiteInwardBean;
import com.sumadhura.dto.IssueToOtherSiteDto;


public interface IssueToOtherSiteService {
	
	public Map<String, String> issueToOtherSiteProducts(String siteId); // siteId for store or marketing dept
	public String issueToOtherSiteSubProducts(String prodId);
	public String issueToOtherSiteChildProducts(String subProductId);
	public String issueToOtherSiteMeasurements(String childProdId);
	public int insertRequesterData(int indentEntryId, String userId, String siteId, IssueToOtherSiteDto reqDto,String strToSite);
	public int insertIndentIssueData(int indentEntrySeqNum, IssueToOtherSiteDto isDto);
	public int updateIndentAvalibility(IssueToOtherSiteDto indIssueDto, String siteId);
	public void updateIndentAvalibilityWithNewIndent(IssueToOtherSiteDto indIssueDto, String siteId);
	public int getIndentEntrySequenceNumber();
	public String doIndentIssueToOtherSite(Model model, HttpServletRequest request, HttpSession session);
	public String getIssueToOtherSiteProductAvailability(String prodId, String subProductId, String childProdId, String measurementId, HttpServletRequest request, HttpSession session);
	public Map<String, String> loadSites();
	
	//inward issue to other service
	
	public List<IssueToOtherSiteInwardBean> getIssuesToOtherLists(HttpSession session,String RequestId,String siteId,String strIssueType);

	public List<IssueToOtherSiteInwardBean> getGetProductDetailsLists(HttpSession session,String  RequestId, String siteId,String strIssueType);

	public List<IssueToOtherSiteInwardBean> getTransportChargesList(String requestId, String siteId);
	
	public String doIndentInwardsFromOtherSite(Model model, HttpServletRequest request, HttpSession session);
	
	public Map<String, String> loadSitesInformationForIssueToOtherSite();
}
