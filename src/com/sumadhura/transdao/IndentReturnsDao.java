package com.sumadhura.transdao;

import java.util.List;

import com.sumadhura.bean.IndentReturnBean;


public interface IndentReturnsDao{
	
	
	public List<IndentReturnBean> getIssueList(String RequestId, String EmployeName,  String ContractorName,String siteId, String fromDate, String toDate);
	public String updateReturns(String RequestId, String strQuantity,String strSiteId);
	public String updateFinalAmount(String RequestId, String TotalAmount);
	public String strGetIndentEntryId(String RequestId);
}




