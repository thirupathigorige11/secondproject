package com.sumadhura.in;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.ProductDetails;
import com.sumadhura.transdao.IndentAvailabulityDao;
import com.sumadhura.transdao.IndentSummaryDao;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
public class IndentSummaryController extends UIProperties {
	@Autowired

	private IndentAvailabulityDao dao;

	@Autowired
	private UtilDao utilDao;


	@RequestMapping(value = "/showIndentsummary.spring")
	public ModelAndView Show(HttpServletRequest request) {

		List<ProductDetails> list = null;
		ModelAndView model = new ModelAndView();
		String strSiteId = "";
		HttpSession session = request.getSession(true);
		try {

			

			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			System.out.println("siteid" + strSiteId);
			list = new IndentSummaryDao().getProductList(strSiteId, session);
			
			if(list != null && list.size() >0){
				request.setAttribute("showGrid", "true");
			}
			
			
			model.addObject("list", list);
			System.out.println("get All  List..." + list.size()); 

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.addObject("grandTotalAmount",session.getAttribute("grandTotalAmount").toString());
			model.setViewName("IndentSummaryView");		
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Stock Availability Viewed","success",site_id1);
		
		return model;
	}
	
	
	@RequestMapping(value = "/showAllIndentsummary.spring")
	public ModelAndView showAllIndentsummary(HttpServletRequest request, HttpSession session) {

		ModelAndView model = new ModelAndView();
		Map<String, String> siteDetails = null;
		
		try {
			 siteDetails = new IndentSummaryDao().getSiteDetails();
			 model.addObject("siteDetails", siteDetails);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.setViewName("AllIndentSummaryView");
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"All Site Summary Viewed","success",site_id1);
				
		return model;
	}
	
	@RequestMapping(value = "/getAllSiteProductDetails.spring")
	public ModelAndView showAllIndentsummaryDetails(HttpServletRequest request, HttpSession session) {
		
		String site = "";
		List<ProductDetails> list = null;
		Map<String, String> siteDetails = null;
		ModelAndView model = new ModelAndView();
		
		try {
			
			site = request.getParameter("site");
			list = new IndentSummaryDao().getProductList(site, session);
			
			
			if(list != null && list.size() >0){
				request.setAttribute("showGrid", "true");
			}
			model.addObject("list", list);
			System.out.println("get All  List..." + list.size());

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			model.addObject("grandTotalAmount",session.getAttribute("grandTotalAmount").toString());
			siteDetails = new IndentSummaryDao().getSiteDetails();
			model.addObject("siteDetails", siteDetails);
			model.setViewName("AllIndentSummaryView");
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"All Sites Summary cliked submit","success",site_id1);
			
		
		
		
		return model;
	}
}
