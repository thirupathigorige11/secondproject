package com.sumadhura.util;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

public class DedupTransactionHandler {
	
	static Logger logger = Logger.getLogger(DedupTransactionHandler.class);
	
	@SuppressWarnings("unchecked")
	public void saveTransaction(String trankey,HttpSession session){
		try{
			List<String> tranarr = (List<String>)session.getAttribute("Transactions");
			if(tranarr != null){
				tranarr.add(trankey);
			}else{
				tranarr = new ArrayList<String>();
				tranarr.add(trankey);
			}
			session.setAttribute("Transactions",tranarr);
			//System.out.println("saveTransaction ... "+tranarr);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception saveTransaction "+ex.getMessage());
		}
	}
		
	@SuppressWarnings("unchecked")
	public boolean isDedupTransaction(String trankey,HttpSession session){
		boolean isdedup = false;
		try{
			List<String> tranarr = (List<String>)session.getAttribute("Transactions");
			if(trankey != null && tranarr != null){
				
				if(tranarr.contains(trankey)){
					isdedup = true;
				}
			}
			//System.out.println("Total Transactions ... "+tranarr);
			//System.out.println("current trankey ... "+trankey);
			//System.out.println("isDedupTransaction ... "+isdedup);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception saveTransaction "+ex.getMessage());
		}
		return isdedup;
	}
	
	@SuppressWarnings("unchecked")
	public void deleteTransaction(String trankey,HttpSession session){
		try{
			List<String> tranarr = (List<String>)session.getAttribute("Transactions");
			if(tranarr != null){
				tranarr.remove(trankey);
			}
			session.setAttribute("Transactions",tranarr);
			//System.out.println("deleting trankey because of exception ... "+trankey);
			//System.out.println("Total Transactions ... "+tranarr);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("Exception deleteTransaction "+ex.getMessage());
		}
	}

	
}
