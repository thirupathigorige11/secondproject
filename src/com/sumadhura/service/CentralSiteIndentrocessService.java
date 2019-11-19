package com.sumadhura.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;

public interface CentralSiteIndentrocessService {

	public String sendToPD(Model model, HttpServletRequest request, int site_id, String user_id,String userName);
	

	
	
	
	public List<IndentCreationBean> getCentralIndentDetailsLists(int indentNumber, String siteId);
	
	public String requestToOtherSite(Model model, HttpServletRequest request, int site_id, String user_id, HttpSession session);
	
	public List<IndentCreationBean> getPurchaseIndentDetailsLists(int indentNumber,String strReqSiteId);



	public List<IndentCreationBean> getAllCentralIndents();





	public List<IndentCreationBean> getIndentCreationLists(int indentNumber);





	List<ProductDetails> getPurchaseIndentDtlsLists(String indentCreationDetailsIdForenquiry,String strVendorId);





	String getIndentLevelComments(int indentNo);
	
	
}
