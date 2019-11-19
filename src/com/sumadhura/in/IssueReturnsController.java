package com.sumadhura.in;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.IndentIssueBean;
import com.sumadhura.bean.IndentReturnBean;
import com.sumadhura.service.IndentReturnsService;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
public class IssueReturnsController extends UIProperties {


	@Autowired
	@Qualifier("iretuenServiceClass")
	IndentReturnsService ireturns;

	@RequestMapping(value = "/doIndentReturns", method = RequestMethod.GET)
	public ModelAndView doIndentReturn(HttpSession session){

		ModelAndView model = null;

		try {
			model = new ModelAndView();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("indentReturns");
		}
		

		SaveAuditLogDetails audit=new SaveAuditLogDetails();
	//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"Returns Viewed","success",site_id);
		
		
		
		
		return model;
	}

	@RequestMapping(value = "/doIndentIssueReturns.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView doIndentIssueReturn(HttpServletRequest request, HttpSession session){

		ModelAndView model = null;
		String requestId = "";
		String siteId = "";
		String EmployeName = "";
		String contractorId="";
		String fromDate="";
		String toDate="";
		String ContractorName = "";
		String response="";
		List<IndentReturnBean> ReturnList = null;
		try {
			model = new ModelAndView();
			requestId = request.getParameter("RequestId") == null ? "" : request.getParameter("RequestId");
			EmployeName = request.getParameter("EmployeName") == null ? "" : request.getParameter("EmployeName").toUpperCase();
			fromDate = request.getParameter("fromDate")==null?"":request.getParameter("fromDate");
			toDate = request.getParameter("toDate")==null?"":request.getParameter("toDate");
			ContractorName = request.getParameter("ContracterName") == null ? "" : request.getParameter("ContracterName").toUpperCase();
			contractorId=request.getParameter("contractorId") == null ? "" : request.getParameter("contractorId").toUpperCase();
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

			if (StringUtils.isNotBlank(siteId)){
				ReturnList = ireturns.getIssueList(requestId, EmployeName,contractorId, siteId,fromDate,toDate);
				//ACP
				if(ReturnList.size()==0){
					model.setViewName("indentReturns");
					model.addObject("noData", "No data availbale.");
					response="failure";
					return model;
				}
				
				model.addObject("prodList", ReturnList);
				model.addObject("RequestId", requestId);
				model.addObject("EmployeName", EmployeName);
				model.addObject("ContractorName", ContractorName);
				model.addObject("showGrid", true);

				
				
				System.out.println("RequestId "+requestId+" EmployeName "+EmployeName);
				response="success";
				model.setViewName("Returns");
			} else {
				request.setAttribute("Message", "Session Expired, Please Login Again!");
				response="failure";
				model.setViewName("index");
			}
		} catch (Exception ex) {
			response="failure";
			ex.printStackTrace();
		} 
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Returns clicked Submit",response,site_id);
		return model;
	}
	@RequestMapping(value = "/indentReturnsController.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView doIndentSave(HttpServletRequest request,HttpServletResponse res, HttpSession session) throws IOException{

		ModelAndView model = null;
		List<IndentIssueBean> ReturnListUpdate = null;

		try {
			model = new ModelAndView();

			String[] RETURNID = request.getParameterValues("indentEntry");
			System.out.println(RETURNID);

			String[] returns = request.getParameterValues("RETURNS");
			String[] quantity = request.getParameterValues("quantity");
			//model.addObject("urlForActivateSubModule", "doIndentReturns.spring");
			//if this values are null means this is the error so we need to forward this request to again to request page with error message
			if(returns==null&&quantity==null&&RETURNID==null){
				model.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				model.setViewName("response");return model;
			}
			String returnValue = "";
			String quantityValue = "";
			for (int i =0; i < returns.length;i++){
				returnValue = returns[i]; 
				quantityValue = quantity[i];
				if (StringUtils.isBlank(returnValue)) {	
					} else {	
						
					if (Math.abs(Double.parseDouble(quantityValue)) >= Math.abs(Double.parseDouble(returnValue))) {
					} else {
						model.addObject("message1", "Return Quantity Should not be greater than Recived Quantity");
						return model;
					}
				}
			}
			try{
				session = request.getSession(true);
				String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				String status1=ireturns.updateReturns(RETURNID, returns,strSiteId);
				model.addObject("updateList", ReturnListUpdate);
				if(status1.equals("success")){
					model.addObject("message", "Return Quantity sucessfully");}
				else{
					model.addObject("message1", "Return Quantity Failed");
					}
			} catch(Exception e){
				e.printStackTrace();
				model.addObject("message1", "Return Quantity Failed");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("response");
		}
		return model;
	}
	/*@RequestMapping(value = "/indentReturnsController.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView doIndentSave(HttpServletRequest request,HttpServletResponse res, HttpSession session) throws IOException{

		ModelAndView model = null;
		List<IndentIssueBean> ReturnListUpdate = null;

		try {
			model = new ModelAndView();

			String[] RETURNID = request.getParameterValues("indentEntry");
			System.out.println(RETURNID);

			String[] RETURNS = request.getParameterValues("RETURNS");
			System.out.println(RETURNS);

			
			
			session = request.getSession(true);
			 
			String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			//String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();				
	


			String strMessgae = ireturns.updateReturns(RETURNID, RETURNS,strSiteId);
			
			
			model.addObject("updateList", ReturnListUpdate);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("Returns");
		}
		return model;

	}*/
	
}