package com.sumadhura.in;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.ClosingBalanceBean;
import com.sumadhura.transdao.ClosingBalanceDetailsDao;
import com.sumadhura.transdao.IndentSummaryDao;
import com.sumadhura.util.CheckSessionValidation;

@Controller
public class ClosingBalanceController {

	@RequestMapping(value="viewClosingBalance.spring", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView viewClosingBalance(HttpServletRequest request, HttpSession session) {
		
		String userid = "0";
		ModelAndView model = null;
		Map<String, String> siteDetails = null;
		try {
			 model = new ModelAndView();
			userid = (String) CheckSessionValidation.validateUserSession(model, session.getAttribute("SiteId"));
			if (!userid.equals("0")) {
				if (userid.equals("999"))	{
					 siteDetails = new IndentSummaryDao().getSiteDetails();
					 model.addObject("siteDetails", siteDetails);
					 model.setViewName("ClosingBalance");
				} else {
					 model.setViewName("ClosingBalanceForOtherSites");
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return model;
	}
	
	@RequestMapping(value="getClosingBalanceDetails.spring", method={RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getClosingBalanceDetails(HttpServletRequest request, HttpSession session) {
		
		ModelAndView model = null;
		String userid = "0";
		String toDate = "";
		String site_id = "";
		String loginSiteId = "";
		Map<String, String> siteDetails = null;
		List<ClosingBalanceBean> list = null;
		try {
			toDate = request.getParameter("toDate");
			site_id = request.getParameter("site");
			model = new ModelAndView();
			userid = (String) CheckSessionValidation.validateUserSession(model, session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			if (!userid.equals("0")) {
				if (StringUtils.isNotBlank(toDate)) {
					loginSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
						
					if (loginSiteId.equals("999")) {
					    list = new ClosingBalanceDetailsDao().getClosingBalanceDetails(toDate, site_id, session);
						
						if(list != null && list.size() >0){
							model.addObject("closingBalanceDts", list);
							request.setAttribute("showGrid", "true");
						} else {
							model.addObject("succMessage","The above Dates Data Not Available");
						}
						model.setViewName("ClosingBalance");
					} else {
						 list = new ClosingBalanceDetailsDao().getClosingBalanceDetails(toDate, loginSiteId, session);
							
							if(list != null && list.size() >0){
								model.addObject("closingBalanceDts", list);
								request.setAttribute("showGrid", "true");
							} else {
								model.addObject("succMessage","The above Dates Data Not Available");
							}
						model.setViewName("ClosingBalanceForOtherSites");
					}
				} else {
					model.addObject("displayErrMsg", "Please Select To Date!");
				}
	
				 model.addObject("toDate", toDate);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			 siteDetails = new IndentSummaryDao().getSiteDetails();
			 model.addObject("siteDetails", siteDetails);
			 model.addObject("grandTotalAmount",session.getAttribute("grandTotalAmount").toString());
		}
		return model;
	}	
	
	
}
