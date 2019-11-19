package com.sumadhura.in;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.service.IndentIssueService;
import com.sumadhura.transdao.IndentIssueDaoImpl;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.util.CheckSessionValidation;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
public class ViewIndentIssueDetailsController extends UIProperties {
	
	@Autowired
	private UtilDao utilDao;

	@Autowired
	@Qualifier("iisClass")
	IndentIssueService iis;
	/**
	 * @author Aniket Chavan
	 * @param request
	 * @param session
	 * @since 04-may-2018
	 * @return
	 */
	@RequestMapping(value="/getSiteWiseIssueDetails.spring", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getSiteWiseIssueDetails(HttpServletRequest request,HttpSession session){
		//System.out.println("ViewIndentIssueDetailsController.getSiteWiseIssueDetails()");
		ModelAndView mav = new ModelAndView();
		String site_id = "0";
		try {
			site_id = (String) CheckSessionValidation.validateUserSession(mav,
					session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			if (!site_id.equals("0")) {
				List<Map<String, Object>> allSitesList = utilDao.getAllSites();
				request.setAttribute("SEARCHTYPE", "ADMIN");
				System.out.println("SearchType is admin");
				request.setAttribute("allSitesList", allSitesList);
				request.setAttribute("blocksMap", iis.loadBlockDetails(site_id));
				request.setAttribute("flatMap", "");
				request.setAttribute("floorMap", "");
				
				String issueTypeSelectBoxOption="";
				
				issueTypeSelectBoxOption += "<option value=''>--Select--</option>";
				issueTypeSelectBoxOption += "<option value='All'>All</option>";
				issueTypeSelectBoxOption += "<option value='NonReturnable'>Non Returnable</option>";
				issueTypeSelectBoxOption += "<option value='Returnable'>Returnable</option>";
				issueTypeSelectBoxOption += "<option value='Repair'>Repair</option>";
				issueTypeSelectBoxOption += "<option value='Scrap'>Scrap</option>";
				issueTypeSelectBoxOption += "<option value='Loss-Theft'>Loss-Theft</option>";
				request.setAttribute("selectBoxOption", issueTypeSelectBoxOption);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			mav.setViewName("ViewIndentIssueDetails");
		}
		//System.out.println("ViewIndentIssueDetailsController.getSiteWiseIssueDetails() executed..." );

		return mav;
	}
	
	
	@RequestMapping(value="getIssueDetails.spring", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getIssueDetails(HttpServletRequest request,HttpSession session) {
		
		ModelAndView model = null;
		
		try {
			//ACP
			model = new ModelAndView();
			model.setViewName("GetIndentIssueDetails");
			model.addObject("urlForActivateSubModule", "getIssueDetails.spring");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			//model.setViewName("ViewIndentIssueDetails");
		}
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		
		String issueTypeSelectBoxOption="";
	
		issueTypeSelectBoxOption += "<option value=''>--Select--</option>";
		issueTypeSelectBoxOption += "<option value='All'>All</option>";
		issueTypeSelectBoxOption += "<option value='NonReturnable'>Non Returnable</option>";
		issueTypeSelectBoxOption += "<option value='Returnable'>Returnable</option>";
		issueTypeSelectBoxOption += "<option value='Repair'>Repair</option>";
		issueTypeSelectBoxOption += "<option value='Scrap'>Scrap</option>";
		issueTypeSelectBoxOption += "<option value='Loss-Theft'>Loss-Theft</option>";
		model.addObject("selectBoxOption", issueTypeSelectBoxOption);
		
		
	/*	request.setAttribute("blocksMap", iis.loadBlockDetails(site_id));
		request.setAttribute("flatMap", "");
		request.setAttribute("floorMap", "");*/
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
	//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		audit.auditLog("0",user_id,"Get issue Details View","success",site_id);
		return model;
	}
	
	
	@RequestMapping(value="viewIndentIssueDetails.spring", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewIndentIssueDetails(HttpServletRequest request,HttpSession session) {
		
		ModelAndView model = null;
		
		try {
			model = new ModelAndView();
			model.addObject("urlForActivateSubModule", "viewIndentIssueDetails.spring");
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("ViewIndentIssueDetails");
		}
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		
		
		String issueTypeSelectBoxOption="";
		
		issueTypeSelectBoxOption += "<option value=''>--Select--</option>";
		issueTypeSelectBoxOption += "<option value='All'>All</option>";
		issueTypeSelectBoxOption += "<option value='NonReturnable'>Non Returnable</option>";
		issueTypeSelectBoxOption += "<option value='Returnable'>Returnable</option>";
		issueTypeSelectBoxOption += "<option value='Repair'>Repair</option>";
		issueTypeSelectBoxOption += "<option value='Scrap'>Scrap</option>";
		issueTypeSelectBoxOption += "<option value='Loss-Theft'>Loss-Theft</option>";
		model.addObject("selectBoxOption", issueTypeSelectBoxOption);
		
		request.setAttribute("blocksMap", iis.loadBlockDetails(site_id));
		request.setAttribute("flatMap", "");
		request.setAttribute("floorMap", "");
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
	//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		audit.auditLog("0",user_id,"View issue Details View","success",site_id);
		return model;
	}
	
	@RequestMapping(value ="getIndentViewDts.spring", method = { RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getIndentViewDts(HttpServletRequest request,HttpSession session) {

		String site_id = "";
		String toDate = "";
		String fromDate = "";
		//ACP
		String blockId="";
		String floorId="";
		String flatId="";
		String issueType="";
		String contractorId="";
		String contractorName="";
		String moduleName="";
		String indentEntryId="";
		ModelAndView model = null;
		String response="";
		List<ViewIndentIssueDetailsBean> indentIssueData = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			//ACP
			blockId = request.getParameter("blockIdName")==null?"":request.getParameter("blockIdName").split("\\$")[0];
			floorId = request.getParameter("floorIdName")==null?"":request.getParameter("floorIdName").split("\\$")[0];
			flatId  = request.getParameter("flatIdName")==null?"":request.getParameter("flatIdName").split("\\$")[0];
			issueType = request.getParameter("issueType")==null?"":request.getParameter("issueType");
			indentEntryId= request.getParameter("indentEntryId")==null?"":request.getParameter("indentEntryId");
			contractorName=request.getParameter("contractorName")==null?"":request.getParameter("contractorName").trim();
			if(contractorName.length()!=0)
			contractorId = request.getParameter("contractorId")==null?"":request.getParameter("contractorId");
			
			moduleName=request.getParameter("moduleName")==null?"":request.getParameter("moduleName");
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
				
				 session = request.getSession(false);
				//site_id = String.valueOf(session.getAttribute("SiteId"));
				//ACP
				site_id = request.getParameter("dropdown_SiteId") == null ? "" : request.getParameter("dropdown_SiteId");
				if (!site_id.equals("")) {
					List<Map<String, Object>> allSitesList = utilDao.getAllSites();
					request.setAttribute("SEARCHTYPE", "ADMIN");
				//	System.out.println("SearchType is admin");
					request.setAttribute("allSitesList", allSitesList);
					request.setAttribute("strSiteId", site_id);
				//	System.out.println("IndentReceiveController.getIndentViewDts() DropDown value is not empty");
				} else if (site_id.equals("")) {
				//	System.out.println("IndentReceiveController.getIndentViewDts() DropDown value is empty");
					site_id =  session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
				}
				String data[]={blockId,floorId,flatId,(issueType.equals("All")?"":issueType),contractorId,indentEntryId};
				indentIssueData = new IndentIssueDaoImpl().getViewIndentIssueDetails(fromDate, toDate, site_id, moduleName,data);
				
				if(!moduleName.equals("GetIssueDetails")){
					request.setAttribute("blocksMap", iis.loadBlockDetails(site_id));
					request.setAttribute("floorMap",iis.getFloorDetails(blockId));
					request.setAttribute("flatMap",iis.getFlatDetails(floorId));
				}
				String issueTypeSelectBoxOption="";
				//if(issueType.length()!=0){
					issueTypeSelectBoxOption += "<option value=''>--Select--</option>";
					
					issueTypeSelectBoxOption += "<option value='All' "+(issueType.equals("All")?"selected='selected'":"")+">All</option>";
					issueTypeSelectBoxOption += "<option value='NonReturnable' "+(issueType.equals("NonReturnable")?"selected='selected'":"")+">Non Returnable</option>";
					issueTypeSelectBoxOption += "<option value='Returnable'  "+(issueType.equals("Returnable")?"selected='selected'":"")+">Returnable</option>";
					issueTypeSelectBoxOption += "<option value='Repair'  "+(issueType.equals("Repair")?"selected='selected'":"")+">Repair</option>";
					issueTypeSelectBoxOption += "<option value='Scrap'  "+(issueType.equals("Scrap")?"selected='selected'":"")+">Scrap</option>";
					issueTypeSelectBoxOption += "<option value='Loss-Theft'  "+(issueType.equals("Loss-Theft")?"selected='selected'":"")+">Loss-Theft</option>";
			//	}
				model.addObject("selectBoxOption", issueTypeSelectBoxOption);
				if(indentIssueData != null && indentIssueData.size() >0){
					request.setAttribute("showGrid", "true");
					response="success";
				} else {
					model.addObject("succMessage","The above Dates Data Not Available");
					response="failure";
				}
			
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
			}
		} catch (Exception ex) {
			response="failure";
			ex.printStackTrace();
		} finally {
			model.addObject("indentIssueData",indentIssueData);
			model.addObject("fromDate",fromDate);
			model.addObject("toDate", toDate);
			model.addObject("contractorName", contractorName);
			model.addObject("contractorId", contractorId);
			model.addObject("blockId", blockId);
			model.addObject("floorId", floorId);
			model.addObject("flatId", flatId);
			model.addObject("issueType", issueType);
			
			model.setViewName("ViewIndentIssueDetails");
			if(moduleName.equals("GetIssueDetails")){
				model.setViewName("GetIndentIssueDetails");
			}
		}

		SaveAuditLogDetails audit=new SaveAuditLogDetails();
	//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"View issue Details Clicked Submit",response,site_id1);
		
		return model;
	}

	
	@RequestMapping(value = "/getIndentIssuedDetails.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView getInvoiceDetails(HttpServletRequest request, HttpSession session){
		String indentEntryId  = ""; 
		String indentType="";
		ModelAndView model = new ModelAndView();
		boolean imageClick = false;
		List<ViewIndentIssueDetailsBean> indentIssueData = null;
		List<ViewIndentIssueDetailsBean> indentIssueData1=new ArrayList<ViewIndentIssueDetailsBean>();
		try {
			indentEntryId= request.getParameter("indentEntryId")==null?"":request.getParameter("indentEntryId");
			String site_id = request.getParameter("dropdown_SiteId") == null ? "" : request.getParameter("dropdown_SiteId");
			String data[]={"","","","","",indentEntryId};
			indentIssueData = new IndentIssueDaoImpl().getViewIndentIssueDetails("N/A", "N/A", site_id, "",data);
				
			//delete this method
			model.addObject("listOfGetProductDetails",new IndentIssueDaoImpl().getGetIssuedDetailsLists(indentEntryId, site_id,indentType,""));
			//model.addObject("listOfGetProductDetails",indentIssueData);
			indentIssueData1.add(indentIssueData.get(0));
			model.addObject("indentIssueData",indentIssueData1);
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		model.setViewName("ViewIssuedDetails");
		return model;
	}
	
	
	
	
}
