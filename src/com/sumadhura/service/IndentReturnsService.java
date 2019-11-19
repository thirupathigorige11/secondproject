package com.sumadhura.service;

import java.util.List;

import com.sumadhura.bean.IndentIssueBean;
import com.sumadhura.bean.IndentReturnBean;

public interface IndentReturnsService {
	
	

	public List<IndentReturnBean> getIssueList(String RequestId, String EmployeName,String ContractorName ,String siteId, String fromDate, String toDate);
	
	public String updateReturns(String[] RequestId, String [] strQuantity,String strSiteId);
	
}
