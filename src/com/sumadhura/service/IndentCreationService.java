package com.sumadhura.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.dto.IndentCreationDto;


public interface IndentCreationService {

	public String indentCreation(Model model, HttpServletRequest request, int site_id, String user_id,String user_Name);

	public String approveIndentCreation(Model model, HttpServletRequest request, int site_id, String user_id,HttpSession session);

	public String rejectIndentCreation(Model model, HttpServletRequest request, int site_id, String user_id,String username,String siteName);

	public List<ProductDetails> getAllSubProducts(String prodId);

	public List<ProductDetails> getAllChildProducts(String strSubProdId);

	public List<IndentCreationBean> getViewMyRequestIndents(HttpServletRequest request, String loadType);
	
	public List<IndentCreationBean> getViewissedIndentDetailsLists(int indentNumber,String siteId);
	public List<IndentCreationBean> getViewAllMyRequestIndents(HttpServletRequest request,int indentNumber);
	public String sendissued(Model model, HttpServletRequest request,HttpSession session, int site_id, String user_id, String site_Name);
	public String RejectQuantity(Model model, HttpServletRequest request, int site_id, String user_id);
	public List<IndentCreationBean> getViewMyReceiveIndents(HttpServletRequest request);
	public List<IndentCreationBean> getViewAllMyRequestedIndent(HttpServletRequest request,int indentNumber);
	public List<IndentCreationBean> getViewReceivedIndentDetailsLists(int indentNumber);
	public String sendReceivedQuantity(Model model, HttpServletRequest request, int site_id, String user_id);
	public String RejectIssueQuantity(Model model, HttpServletRequest request, int site_id, String user_id);



	public String acceptIndentReceive( HttpServletRequest request, int site_id, String user_id,String num);
	
	public String checkisIndentClose( HttpServletRequest request, int site_id, String user_id);

	public void getIndentFromDetails(int indentNumber, List<IndentCreationBean> list);

	public void getIndentToDetails(String strSiteID,String user_id, List<IndentCreationBean> list);

	public IndentCreationBean getIndentFromAndToDetails(String user_id, IndentCreationBean icb,int siteId);

	public int getIndentCreationSequenceNumber();

	public List<IndentCreationBean> getAllCentralIndents() ;

	public List<IndentCreationDto> getPendingIndents(String user_id, String strSiteId);
	
	public List<IndentCreationBean> getIndentCreationDetailsLists(int indentNumber);

	public boolean checkIndentNumberIsValidForEmployee(int indentNumber, String user_id);

	public List<IndentCreationBean> getIndentCreationLists(int indentNumber);

	public String approveIndentCreationFromMail( HttpServletRequest request, int site_id, String user_id,String userName);

	public String rejectIndentCreationFromMail(Model model, HttpServletRequest request,int site_id, String user_id);
	public List<IndentCreationBean> getAllSiteCentralIndents(String site);
	public List<ViewIndentIssueDetailsBean> getRaisedIndentDetails(String fromDate, String toDate, String siteId,String indentNumber);
	//public String getGrnDetails(String invoiceNumber, String siteId,HttpServletRequest request);
	public String getPoDetailsList(String invoiceNumber, String siteId,HttpServletRequest request);
	public List<IndentCreationBean> getPoDetails(String fromDate, String toDate, String siteId,String poNumber,String sessionId,boolean allPoOrNot,String siteIds,String vendorName);
	public List<IndentCreationBean> getClosedIndents(String fromDate, String toDate, String deptId,String indentNumber,String sessionId,String sites);
	public String  getIndentLevelComments(int indentNo);

	public List<IndentCreationBean> getDeletedProductDetailsLists(int indentNumber);

	public int getMaxOfSiteWiseIndentNumber(int siteId);

	public int getIndentNumber(int siteWiseIndentNumber, int site_Id);
	public List<IndentCreationBean> viewIndentProductDetails(String fromDate,String toDate,String siteId);
	public List<IndentCreationBean> getRejectedIndentsList(String userId);
	public String  getIndentRejectedComments(int oldIndentNo);
	public String approveRejectedIndentCreation(Model model, HttpServletRequest request, int site_id, String user_id,String user_Name);
public String getPoLevelComments(String poNumber,String FinalPoOrNot);
public String getAndCheckBOQQuantity(HttpSession session,HttpServletRequest request,String siteId,boolean approveOrNot);
public String gettingBoqQuantityAjax(String groupId,String siteId);
}
