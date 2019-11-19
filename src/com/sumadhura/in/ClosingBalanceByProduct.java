package com.sumadhura.in;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.userDetails;
import com.sumadhura.transdao.IndentAvailabulityDao;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
public class ClosingBalanceByProduct extends UIProperties{

	@Autowired
	private IndentAvailabulityDao dao;
	
	@Autowired
	private UtilDao utilDao;
	
	
	@RequestMapping("/showClosingBalanceByProduct.spring")
	public ModelAndView showClosingBalanceByProduct (HttpServletRequest request, userDetails userDts, HttpSession session) {

		ModelAndView mav = new ModelAndView();
		String userid = "0";
		try{
			userid = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			if (StringUtils.isNotBlank(userid)) {
				List<Map<String, Object>> totalProductList = utilDao.getTotalProducts(userid);
	
				List<Map<String, Object>> allSitesList = utilDao.getAllSites();
				request.setAttribute("allSitesList", allSitesList);
				request.setAttribute("totalProductsList", totalProductList);
				request.setAttribute("siteId", userid);
				String strAdminUserId = validateParams.getProperty("AdminId");
				if (userid.equals(strAdminUserId)) {
					request.setAttribute("SEARCHTYPE", "ADMIN");
				}
				mav.setViewName("ClosingBalanceByProduct");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Closing Balance by Product Viewed","success",site_id1);
			
			return mav;

	}
	
	@RequestMapping("/getClosingDetailsBasedOnProduct.spring")
	public ModelAndView getClosingDetailsBasedOnProduct(HttpSession session, HttpServletRequest request) {
		
		String userid = "";
		String productId = "";
		String subProductId = "";
		String childProductId = "";
		String siteId = "";
		String fromDate = "";
		String toDate = "";
		ModelAndView mav = null;
		List<ProductDetails> list = null;
		try {
			
			mav = new ModelAndView();
			userid = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			productId = request.getParameter("combobox_Product");
			subProductId = request.getParameter("combobox_SubProduct");
			childProductId = request.getParameter("combobox_ChildProduct");
			siteId = request.getParameter("dropdown_SiteId");
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			if (StringUtils.isNotBlank(fromDate))	{
				
				if (StringUtils.isBlank(siteId)) {
					siteId = userid;
				} 
				
				if (subProductId.contains("@@")) {

					String productArr[] = subProductId.split("@@");

					if (productArr != null && productArr.length >= 1) {
						subProductId = productArr[0].trim();
					} else {
						subProductId = "";
					}
				}
				if (childProductId.contains("@@")) {

					String productArr[] = childProductId.split("@@");

					if (productArr != null && productArr.length >= 1) {
						childProductId = productArr[0].trim();
					} else {
						childProductId = "";
					}
				}
				list = dao.getProductDetailsByDates(productId, subProductId, childProductId, siteId, fromDate, toDate);
				if (null != list && list.size() > 0) {
					mav.addObject("list", list);
					List<Map<String, Object>> totalProductList = utilDao.getTotalProducts(userid);
					
					List<Map<String, Object>> allSitesList = utilDao.getAllSites();
					request.setAttribute("allSitesList", allSitesList);
					request.setAttribute("totalProductsList", totalProductList);
					request.setAttribute("siteId", userid);
					String strAdminUserId = validateParams.getProperty("AdminId");
					if (userid.equals(strAdminUserId)) {
						request.setAttribute("SEARCHTYPE", "ADMIN");
					}
					mav.setViewName("GETClosingBalanceDetailsByProduct");
				} else {
					List<Map<String, Object>> totalProductList = utilDao.getTotalProducts(userid);
					
					List<Map<String, Object>> allSitesList = utilDao.getAllSites();
					request.setAttribute("allSitesList", allSitesList);
					request.setAttribute("totalProductsList", totalProductList);
					request.setAttribute("siteId", userid);
					String strAdminUserId = validateParams.getProperty("AdminId");
					if (userid.equals(strAdminUserId)) {
						request.setAttribute("SEARCHTYPE", "ADMIN");
					}
					mav.addObject("errMessage", "No Data Available On Selected Dates.");
					mav.setViewName("ClosingBalanceByProduct");
				}
			} else {
				mav.addObject("errMessage", "Please provide valid Inputs.");
				mav.setViewName("ClosingBalanceByProduct");
			}
		} catch (Exception ex) {
			ex.printStackTrace();		
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Closing Balance by Product clicked submit","success",site_id1);
			
		
		
		
		
		return mav;
	}
}
