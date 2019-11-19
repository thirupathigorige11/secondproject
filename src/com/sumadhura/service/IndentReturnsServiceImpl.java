package com.sumadhura.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.sumadhura.bean.IndentIssueBean;
import com.sumadhura.bean.IndentReturnBean;
import com.sumadhura.transdao.IndentReceiveDao;
import com.sumadhura.transdao.IndentReturnsDao;



@Service("iretuenServiceClass")
public class IndentReturnsServiceImpl  implements IndentReturnsService{


	@Autowired
	private IndentReturnsDao indentReturnDaoClass;

	@Autowired
	PlatformTransactionManager transactionManager;



	@Override
	public List<IndentReturnBean> getIssueList(String RequestId, String EmployeName,String ContractorName, String siteId,String fromDate, String toDate) {

		List<IndentReturnBean> listIssueList = null;

		try {

			listIssueList = indentReturnDaoClass.getIssueList(RequestId, EmployeName,ContractorName, siteId ,  fromDate,  toDate);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return listIssueList;

	}




	public String updateReturns(String []  RequestId, String [] strQuantity,String strSiteId){


		String strResponse = "";
		String strReturnQuantity = "";
		String strRequestId = "";

		String strTotalAmount = "";
		Double doubleTotalAmount = 0.0;
		//String strTotalAmount = "";


		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in InRt_updRet, ");
		try{
			for(int i=0;i<strQuantity.length;i++)
			{
				strReturnQuantity = strQuantity[i];
				if(strReturnQuantity !=null && !strReturnQuantity.equals("")){
					strRequestId = RequestId[i];
					strTotalAmount = indentReturnDaoClass.updateReturns(strRequestId, strReturnQuantity,strSiteId);
					WriteTrHistory.write("Site:"+strSiteId+" , User:N/A"+" , Date:"+new java.util.Date()+" , RequestId:"+RequestId);

					//doubleTotalAmount = doubleTotalAmount+Double.parseDouble(strTotalAmount.replace(",",""));
				
				
					strTotalAmount = String.valueOf(Double.parseDouble(strTotalAmount.replace(",","")));
					String strIndentEntryId = indentReturnDaoClass.strGetIndentEntryId(strRequestId);
					indentReturnDaoClass.updateFinalAmount( strIndentEntryId,strTotalAmount);
				}
			}
			
			//doubleTotalAmount
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");
			strResponse="success";
		}catch(Exception e){
			e.printStackTrace();
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
		}
		return strResponse;
	}

}
