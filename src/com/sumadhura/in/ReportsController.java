package com.sumadhura.in;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.ibm.wsdl.util.StringUtils;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.userDetails;
import com.sumadhura.dto.PriceMasterDTO;
import com.sumadhura.service.ReportsService;
import com.sumadhura.transdao.IndentAvailabulityDao;
import com.sumadhura.transdao.IndentReceiveDaoImpl;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.util.CheckSessionValidation;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
/**
 * @author Aniket Chavan
 * @since 5/31/2018 4:24
 * @category Generatring Reports
 *
 */
public class ReportsController {

	static Logger log = Logger.getLogger(ReportsController.class);
	
	@Autowired
	private IndentAvailabulityDao dao;

	@Autowired
	private UtilDao utilDao;

	@Autowired
	@Qualifier("guiReportService")
	ReportsService reportsService;

	@Autowired
	CommonUtilities objCommonUtilities;

	/**
	 * @author Aniket Chavan
	 * @param request
	 * @since 04-may-2018
	 * @return
	 */
	@RequestMapping(value = "/productstock.spring", method = RequestMethod.GET)
	public @ResponseBody String getProductStockDetailAndPrice(HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
	//System.out.println("ReportsController.getProductStockDetailAndPrice()");
		String siteId = (String) CheckSessionValidation.validateUserSession(mav,
				session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
		String productDetail = "";
		try {
			productDetail = reportsService.getProdcutsDetail(siteId);
			int PRETTY_PRINT_INDENT_FACTOR = 4;
			JSONObject xmlJSONObj = XML.toJSONObject(productDetail);
			productDetail = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
			//System.out.println(productDetail);

		} catch (JSONException je) {
			System.out.println(je.toString());
		}

		//System.out.println("ProductDetail Json " + productDetail);
		// productDetail =
		// "{\"xml\":{\"ProductList\":{\"Products\":{\"Assets\":\"1000\",\"BlockWork\":\"12000\",\"Plastering\":\"8000\"}},\"SubProductList\":{\"Products\":{\"Assets\":{\"BatchingPlant\":\"10000\",\"vibrator\":\"50000\"},\"BlockWork\":{\"Brick\":\"20000\",\"HelloWBrick\":\"60000\"},\"Plastering\":{\"cement\":\"20000\"}}},\"ChildProductList\":{\"Products\":{\"Assets\":{\"BatchingPlant\":\"10000\",\"vibrator\":\"50000\",\"Shoeshinemachine\":\"50000\"},\"BlockWork\":{\"Brick\":\"20000\",\"HelloWBrick\":\"60000\",\"FlyAshBlock\":\"60000\"},\"Plastering\":{\"cement\":\"20000\"}}}}}";

		return productDetail;
	}

	//getChildProdDetails.spring
	@RequestMapping(value = "/getChildProdDetails.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String  getChildProdDetails(HttpServletRequest request,HttpSession session) {
		//System.out.println("ReportsController.getChildProdDetails()");
		ModelAndView mav = new ModelAndView();
		String siteId = (String) CheckSessionValidation.validateUserSession(mav,session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
		String productDetail = "";
		try {
			String prodName=request.getParameter("prodName")==null?"":request.getParameter("prodName");
			
			productDetail = reportsService.getChildProdcutsDetailByName(siteId,prodName);
			int PRETTY_PRINT_INDENT_FACTOR = 4;
			JSONObject xmlJSONObj = XML.toJSONObject(productDetail);
			productDetail = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
			//System.out.println(productDetail);
		} catch (JSONException je) {
			System.out.println(je.toString());
		}

		//System.out.println("Sub ProductDetail Json " + productDetail);
		return productDetail;
	}
	
	/**
	 * @author Aniket Chavan
	 * @param request
	 * @since 04-may-2018
	 * @return
	 */
	@RequestMapping(value = "/getSiteWiseCumulativeStock.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getSiteWiseCumulativeStock(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		ModelAndView mav = new ModelAndView();
		String siteId = "0";
		try {
			siteId = (String) CheckSessionValidation.validateUserSession(mav,
					session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			if (!siteId.equals("0")) {
				List<Map<String, Object>> totalProductList = utilDao.getTotalProducts(siteId);

				List<Map<String, Object>> allSitesList = utilDao.getAllSites();
				request.setAttribute("SEARCHTYPE", "ADMIN");
				//System.out.println("SearchType is admin");

				request.setAttribute("allSitesList", allSitesList);
				request.setAttribute("totalProductsList", totalProductList);
				mav.setViewName("reports/CumulativeStock");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SaveAuditLogDetails audit = new SaveAuditLogDetails();
		// String
		// indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id = String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0", user_id, "View Cumulative Stock Viewed", "success", site_id1);

		return mav;
	}

	@RequestMapping("/ViewCumulativeStockList.spring")
	public String getCumulativeStockDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//System.out.println("central view controller");

		String strProduct = request.getParameter("combobox_Product") == null ? ""
				: request.getParameter("combobox_Product").toString();
		String strSubProduct = request.getParameter("combobox_SubProduct") == null ? ""
				: request.getParameter("combobox_SubProduct").toString();
		String strChildProduct = request.getParameter("combobox_ChildProduct") == null ? ""
				: request.getParameter("combobox_ChildProduct").toString();

		String userid = "0";
		String strProductId = "";
		String strSubProductId = "";
		String strChildProductId = "";
		String cumulativeStock = "";

		String fromDate = request.getParameter("fromDate");
		String toDate = request.getParameter("toDate");

		if (strProduct.contains("@@")) {

			String productArr[] = strProduct.split("@@");

			if (productArr != null && productArr.length >= 1) {
				strProductId = productArr[0].trim();
			}
		}

		if (strSubProduct.contains("@@")) {

			String productArr[] = strSubProduct.split("@@");

			if (productArr != null && productArr.length >= 1) {
				strSubProductId = productArr[0].trim();
			}
		}

		if (strChildProduct.contains("@@")) {

			String productArr[] = strChildProduct.split("@@");

			if (productArr != null && productArr.length >= 1) {
				strChildProductId = productArr[0].trim();
			}
		}
		String strSiteId = "";

		HttpSession session = request.getSession(true);
		// strSiteId = session.getAttribute("SiteId") == null ? "" :
		// session.getAttribute("SiteId").toString();
		String note = UIProperties.validateParams.getProperty("CUM_" + strSiteId);

		request.setAttribute("note", note);
		// String strAdminUserId = validateParams.getProperty("AdminId");
		strSiteId = request.getParameter("dropdown_SiteId") == null ? "" : request.getParameter("dropdown_SiteId");
		if (!strSiteId.equals("")) {
			request.setAttribute("SEARCHTYPE", "ADMIN");
		} else if (strSiteId.equals("")) {
			strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		}
		//System.out.println("Indent Avaliable controller product id " + strProductId + "  sub product id child"
		//		+ strSubProductId + " child product id " + strChildProductId + " site id" + strSiteId);

		List<ProductDetails> productList = dao.getCumulativeProductList(strProductId, strSubProductId,
				strChildProductId, strSiteId, fromDate, toDate);
		request.setAttribute("fromDate", fromDate);
		request.setAttribute("toDate", toDate);
		request.setAttribute("strSiteId", strSiteId);

		if (productList.size() > 0) {

			request.setAttribute("productList", productList);
			cumulativeStock = "reports/CumulativeStockView";

		} else {

			request.setAttribute("message1", "No Stock Available With This Product");
			cumulativeStock = "response";
		}

		SaveAuditLogDetails audit = new SaveAuditLogDetails();
		// String
		// indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id = String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0", user_id, "View cumulative Stock clicked submit", "success", site_id1);

		return cumulativeStock;

	}

	@RequestMapping("/CumulativeStock.spring")
	public ModelAndView getCumulativeStock(HttpServletRequest request, userDetails userDts, HttpSession session) {

		ModelAndView mav = new ModelAndView();
		String siteId = "0";
		try {
			siteId = (String) CheckSessionValidation.validateUserSession(mav,
					session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			if (!siteId.equals("0")) {
				List<Map<String, Object>> totalProductList = utilDao.getTotalProducts(siteId);

				// List<Map<String, Object>> allSitesList =
				// utilDao.getAllSites();

				// request.setAttribute("allSitesList", allSitesList);
				request.setAttribute("totalProductsList", totalProductList);
				mav.setViewName("reports/CumulativeStock");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		SaveAuditLogDetails audit = new SaveAuditLogDetails();
		// String
		// indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id = String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0", user_id, "View Cumulative Stock Viewed", "success", site_id1);
		return mav;
	}

	@RequestMapping(value = "/priceMasterSiteWise.spring", method = RequestMethod.GET)
	public ModelAndView getPriceMasterSiteWise(HttpServletRequest request, HttpSession session) {
		//System.out.println("ReportsController.getProductPriceListDetail()");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("reports/PriceMaster");
		try {
			request.setAttribute("SEARCHTYPE", "ADMIN");
			List<Map<String, Object>> allSitesList = utilDao.getAllSites();
			mav.addObject("allSitesList", allSitesList);
			String siteId = (String) CheckSessionValidation.validateUserSession(mav,
					session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));

			mav.addObject("childProdName", "");
			mav.addObject("strSiteId", "");
			mav.addObject("priceMaster", "PriceMaster");
			System.out.println("ReportsController.getProductPriceListDetail() executed");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return mav;
	}
	
	//departmentWisePriceMaster.spring
	
/**
 * @description this controller is used for showing the data department wise<br>
 * This is same as Price Master, only difference is this will be mapped Department wise, i.e. Marketing and Stores. As we have two types of Products marketing and Stores<br>
 * We have three types of products. In Product table, we have three types of Departments (Tags) i.e. ALL, MARKETING and STORE.
 * ALL – This will be used by both Marketing and Stores.<br>
 * MARKETING – This will be used by marketing only.<br>
 * STORE – This will be used by Stores only.<br>
 * This Module will take condition on Login ID, if any one got login from Marketing will be considering as Marketing, others all will be treated as Stores.<br>
 * For Marketing login, they can select only site MARKETING.<br>
 * For Others, They can select sites other than MARKETING.<br>
 * @param request
 * @param session
 * @return
 */
	@RequestMapping(value = "/departmentWisePriceMaster.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView departmentWisePriceMaster(HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		
		String siteId = (String) CheckSessionValidation.validateUserSession(mav,
				session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
		String str[]=new String[5];
	/*	if(siteId.equals("996")){
			str[0]="'MARKETING','ALL'";
		}else if(!siteId.equals("996")){
			List<Map<String, Object>> allSitesList = utilDao.getAllSites();
			str[0]="'STORE','ALL'";
			mav.addObject("allSitesList", allSitesList);
		}else {
			str[0]="''";
		}
	*/
		mav.addObject("priceMaster", "DeptWisePriceMaster");
		request.setAttribute("SEARCHTYPE", "ADMIN");
		mav.setViewName("reports/DepartmentWisePriceMaster");
		return mav;
	}

	/**
	 * @param request
	 * @param session
	 * @description this method will give you the last 3 months data
	 * @return
	 */
	@RequestMapping(value = "/ViewThreeMonthProductPrice.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getUpdatdProductPriceList(HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("reports/PriceMaster");
		String strSiteId = request.getParameter("dropdown_SiteId") == null ? ""	: request.getParameter("dropdown_SiteId");
		String childProdName = request.getParameter("childProdName") == null ? "" : request.getParameter("childProdName");
		String monthYear = request.getParameter("monthYear")==null?"":request.getParameter("monthYear");
		//String siteName=request.getParameter("monthYear")==null?"":request.getParameter("monthYear");
		String site_name = request.getParameter("SITE_NAME")==null?"":request.getParameter("SITE_NAME");
		String priceMasterType = request.getParameter("PriceMaster")==null?"":request.getParameter("PriceMaster");
		request.setAttribute("SEARCHTYPE", "ADMIN");
	request.setAttribute("url","ViewThreeMonthProductPrice.spring?dropdown_SiteId="+strSiteId+"&childProdName="+childProdName+"&monthYear="+monthYear+"&PriceMaster="+priceMasterType);
		List<Map<String, Object>> allSitesList = utilDao.getAllSites();
		//List<Map<String, Object>> allSitesList = utilDao.getAllSites();
		mav.addObject("allSitesList", allSitesList);
		
		if(childProdName.length() != 0){
			mav.addObject("childProdName", childProdName);
		}else{
			mav.addObject("childProdName", "");
		}
		
		mav.addObject("strSiteId", strSiteId);
		mav.addObject("monthYear", monthYear);
		mav.addObject("priceMaster", priceMasterType);
		try {
			Set<PriceMasterDTO> childPriceListBySite = reportsService.getProductPriceListBySite(strSiteId, childProdName,monthYear,priceMasterType);
			request.setAttribute("tempSiteId", strSiteId);
			
			if (childPriceListBySite != null && childPriceListBySite.size() > 0) {
				request.setAttribute("showGrid", "true");
				request.setAttribute("ShowDataTable1", "false");
			} else {
			
				String monthNum="";
				if(monthYear.length()!=0){
							String[] strMonthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
					
							monthNum=strMonthNames[Integer.valueOf(monthYear.split("-")[0])-1];
							monthYear=monthNum+"-"+monthYear.split("-")[1];
				}
				
				if(strSiteId.length()!=0&&childProdName.length()==0&&monthYear.length()==0){
					mav.addObject("succMessage","No Products were received in Project \""+site_name+"\"");
				}else if(strSiteId.length()!=0&&childProdName.length()!=0&&monthYear.length()==0){
					mav.addObject("succMessage","Selected Product "+childProdName+" was not received for Project  \""+site_name+"\"");
				}else if(strSiteId.length()!=0&&childProdName.length()==0&&monthYear.length()!=0){
					mav.addObject("succMessage","No Products were received in "+monthYear+" for Project \""+site_name+"\"");
				}else if(strSiteId.length()!=0&&childProdName.length()!=0&&monthYear.length()!=0){
					mav.addObject("succMessage","Selected Product "+childProdName+" was not received in "+monthYear+" for Project \""+site_name+"\"");
				}else if(strSiteId.length()==0&&childProdName.length()!=0&&monthYear.length()!=0){
					mav.addObject("succMessage","Selected Product "+childProdName+" was not received in "+monthYear+" for any Project");
				}else if(strSiteId.length()==0&&childProdName.length()!=0&&monthYear.length()==0){
					mav.addObject("succMessage","Selected Product "+childProdName+" was not received for any Project");
				}
				
				
/*				if(childProdName.length()!=0&&strSiteId.length()==0){
					mav.addObject("succMessage","Selected Products were not received in any Project.");
				}else if(childProdName.length()!=0&&strSiteId.length()!=0){
					mav.addObject("succMessage","no data");
				}else if(childProdName.length()==0&&strSiteId.length()!=0){
					mav.addObject("succMessage","No Products were received in Project \""+site_name+"\"");
				}else if(strSiteId.length()!=0&&monthYear.length()!=0){
					mav.addObject("succMessage","Selected Product was not received in "+monthYear+" for Project \""+site_name+"\"");
				}else if(childProdName.length()!=0&&monthYear.length()!=0){
					mav.addObject("succMessage","Selected Product was not received in "+monthYear+" for any Project ");
				}*/
			}
			mav.addObject("childPriceListBySite", childPriceListBySite);
			return mav;
		} catch (Exception e) {
			mav.addObject("succMessage", "Selected Products were not received in Project.");
		}
	return null;
	}

	@RequestMapping(value = "/loadAllSites.spring", method = RequestMethod.GET)
	public @ResponseBody String loadAllSites(HttpServletResponse response, HttpServletRequest request,
			HttpSession session) {
		//System.out.println("ReportsController.loadAllSites()");
		List<Map<String, Object>> allSitesList = utilDao.getAllSites();
		Gson gson = new Gson();
		String allSites = gson.toJson(allSitesList);
		//System.out.println(allSites);
		return allSites;
	}

	@RequestMapping(value = "/loadChildProductLatestPriceNames.spring", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> loadChildProductLatestPrice(HttpServletResponse response, HttpServletRequest request,
			HttpSession session) {
	//	System.out.println("ReportsController.loadChildProductLatestPrice()");
//		String resp = "";
		try {
			String prodName = request.getParameter("prodName")==null?"":request.getParameter("prodName");
			String type=request.getParameter("type")==null?"":request.getParameter("type");
			String site_id = request.getParameter("SITE_ID")==null?"":request.getParameter("SITE_ID");
//			String site_name = request.getParameter("SITE_NAME")==null?"":request.getParameter("SITE_NAME");
			System.out.println(prodName);
			List<Map<String, Object>> childProductList = reportsService.loadAllChildProducts(prodName,site_id,type);
//			Gson gson = new Gson();
//			resp = gson.toJson(childProductList);
//			System.out.println(resp);
			return childProductList;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * @description this controller used for PO ajax call to load the data based on product names
	 * @param request
	 * @param session
	 * @param mav
	 * @return
	 */
	@RequestMapping(value = "/getProductDetailsForPO.spring", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public List<List<PriceMasterDTO>> getLastThreeMonthsPriceMasterDetailForPO(HttpServletRequest request, HttpSession session,Model mav) {
		String childProductId = request.getParameter("CHILD_PRODUCT_ID")==null?"":request.getParameter("CHILD_PRODUCT_ID");
		//String strSiteId = request.getParameter("dropdown_SiteId") == null ? ""	: request.getParameter("dropdown_SiteId");
		String childProdName= request.getParameter("NAME")==null?"":request.getParameter("NAME");	
		String monthYear = request.getParameter("monthYear")==null?"":request.getParameter("monthYear");
		String PRICE_ID = request.getParameter("PRICE_ID")==null?"":request.getParameter("PRICE_ID");
		String site_id = request.getParameter("SITE_ID")==null?"":request.getParameter("SITE_ID");
		String site_name = request.getParameter("SITE_NAME")==null?"":request.getParameter("SITE_NAME");
		
		List<List<PriceMasterDTO>> lastThreeMonthData=new ArrayList<List<PriceMasterDTO>>();
		Map<String, Collection<PriceMasterDTO>> lastThreeMonthsPriceMasterDetail = null;
		try {
			lastThreeMonthsPriceMasterDetail = reportsService.getLastThreeMonthPriceMasterDetail(childProductId, childProdName, PRICE_ID, site_id,monthYear);
			if (lastThreeMonthsPriceMasterDetail != null && lastThreeMonthsPriceMasterDetail.size() > 0) {
				request.setAttribute("showGrid", "true");
				List<PriceMasterDTO> firstMonthData = (List<PriceMasterDTO>) lastThreeMonthsPriceMasterDetail.get("firstMonth");
				List<PriceMasterDTO> secondMonthData = (List<PriceMasterDTO>) lastThreeMonthsPriceMasterDetail.get("secondMonth");
				List<PriceMasterDTO> thirdMonthData = (List<PriceMasterDTO>) lastThreeMonthsPriceMasterDetail.get("thirdMonth");
				lastThreeMonthData.add(firstMonthData);
				lastThreeMonthData.add(secondMonthData);
				lastThreeMonthData.add(thirdMonthData);
				//adding site name and id for displaying that there is no data in this site
				PriceMasterDTO d=new PriceMasterDTO();
				d.setSite_id(site_id);
				d.setSite_name(site_name);
				d.setChild_product_name(childProdName);
				List<PriceMasterDTO> siteIdOnly=new ArrayList<PriceMasterDTO>();
				siteIdOnly.add(d);
				lastThreeMonthData.add(siteIdOnly);
				}
		} catch (Exception e) {
			e.printStackTrace();
			lastThreeMonthData.add(new ArrayList<PriceMasterDTO>());
			lastThreeMonthData.add(new ArrayList<PriceMasterDTO>());
			lastThreeMonthData.add(new ArrayList<PriceMasterDTO>());
			//adding site name and id for displaying that there is no data in this site
			PriceMasterDTO d=new PriceMasterDTO();
			d.setSite_id(site_id);
			d.setSite_name(site_name);
			d.setChild_product_name(childProdName);
			List<PriceMasterDTO> siteIdOnly=new ArrayList<PriceMasterDTO>();
			siteIdOnly.add(d);
			lastThreeMonthData.add(siteIdOnly);
		}
		return lastThreeMonthData;
	}
	
	/**
	 * @description this controller used to load the data price master sub module
	 * @param request
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/getProductDetails.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView getLastThreeMonthsPriceMasterDetail(HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView();
		String childProductId = request.getParameter("CHILD_PRODUCT_ID")==null?"":request.getParameter("CHILD_PRODUCT_ID");
		String strSiteId = request.getParameter("dropdown_SiteId") == null ? ""	: request.getParameter("dropdown_SiteId");
		String childProdName= request.getParameter("NAME")==null?"":request.getParameter("NAME");	
		String monthYear = request.getParameter("monthYear")==null?"":request.getParameter("monthYear");
		String PRICE_ID = request.getParameter("PRICE_ID");
		String site_id = request.getParameter("SITE_ID")==null?"":request.getParameter("SITE_ID");
		String site_name = request.getParameter("SITE_NAME")==null?"":request.getParameter("SITE_NAME");
		//String childProductId = CHILD_PRODUCT_ID == null ? "" : CHILD_PRODUCT_ID.toString();
		String PriceMasterType= request.getParameter("PriceMasterType")==null?"":request.getParameter("PriceMasterType");
		//String prodName =childProdName;
		String priceId = PRICE_ID == null ? "" : PRICE_ID.toString();
		List<Map<String, Object>> allSitesList = utilDao.getAllSites();

		if (!strSiteId.equals("")) {
		//	request.setAttribute("selectBoxOption", strSiteId);
			mav.addObject("strSiteId", strSiteId);
		}else if(!site_id.equals("")){
			//request.setAttribute("selectBoxOption", site_id);
			mav.addObject("strSiteId", site_id);
		} else if (strSiteId.equals("")) {
			//strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		}
		
		
		request.setAttribute("monthYear", monthYear);
		request.setAttribute("SEARCHTYPE", "ADMIN");
		request.setAttribute("allSitesList", allSitesList);
		request.setAttribute("priceMaster", PriceMasterType);
		try {
			//calling service layer to load the last any three months data
			Map<String, Collection<PriceMasterDTO>> lastThreeMonthsPriceMasterDetail = reportsService.getLastThreeMonthPriceMasterDetail(childProductId, childProdName, priceId, site_id,monthYear);
			if (lastThreeMonthsPriceMasterDetail != null && lastThreeMonthsPriceMasterDetail.size() > 0) {
				request.setAttribute("showGrid", "true");
				List<PriceMasterDTO> firstMonthData = (List<PriceMasterDTO>) lastThreeMonthsPriceMasterDetail.get("firstMonth");
				List<PriceMasterDTO> secondMonthData = (List<PriceMasterDTO>) lastThreeMonthsPriceMasterDetail.get("secondMonth");
				List<PriceMasterDTO> thirdMonthData = (List<PriceMasterDTO>) lastThreeMonthsPriceMasterDetail.get("thirdMonth");
				int forRowSpan = 0;
				if (firstMonthData.size() == 0 && secondMonthData.size() == 0 && thirdMonthData.size() == 0) {
					mav.setViewName("reports/viewPastMonthsChildProductPriceDetails");
					request.setAttribute("SEARCHTYPE", "ADMIN");
					mav.addObject("succMessage","Selected Products were not received in Project '"+site_name+"'.");
					request.setAttribute("showGrid", "false");
					return mav;
				} 
				int firstMonthDataSize = firstMonthData.size();
				int secondMonthDataSize = secondMonthData.size();
				int thirdMonthDataSize = thirdMonthData.size();
				//checking which one is having highest size based on that in frond end site we are adding empty column's
				if (firstMonthDataSize >= secondMonthDataSize && firstMonthDataSize >= thirdMonthDataSize) {
					//System.out.println("First number is largest. ");
					forRowSpan = firstMonthDataSize;
					childProdName=firstMonthData.get(0).getChild_product_name();
				} else if (secondMonthDataSize >= firstMonthDataSize && secondMonthDataSize >= thirdMonthDataSize) {
					//System.out.println("Second number is largest.");
					forRowSpan = secondMonthDataSize;
					childProdName=secondMonthData.get(0).getChild_product_name();
				} else if (thirdMonthDataSize >= firstMonthDataSize && thirdMonthDataSize >= secondMonthDataSize) {
 				    //System.out.println("Third number is largest.");
					forRowSpan = thirdMonthDataSize;
					childProdName=thirdMonthData.get(0).getChild_product_name();
				} else{
					//System.out.println("Entered numbers are not distinct.");
				}
				if(childProdName.length() != 0){
					mav.addObject("childProdName", childProdName);
					mav.addObject("childProductId", childProductId);
				}else{
					mav.addObject("childProdName", "");
					mav.addObject("childProductId", "");
				}
				//adding all the months individually in model object
				mav.addObject("firstMonth", firstMonthData);
				mav.addObject("secondMonth", secondMonthData);
				mav.addObject("thirdMonth", thirdMonthData);
				mav.addObject("rowSpanCol", forRowSpan);
				mav.addObject("childProductListDetailById", lastThreeMonthsPriceMasterDetail);
			}
		} catch (Exception e) {
			request.setAttribute("allSitesList", allSitesList);
			mav.setViewName("reports/PriceMaster");
			if(childProdName.length() != 0){
				mav.addObject("childProdName", childProdName);
				mav.addObject("childProductId", childProductId);
			}else{
				mav.addObject("childProdName", "");
				mav.addObject("childProductId", "");
			}
			mav.addObject("succMessage", "Selected Products were not received in Project '"+site_name+"'.");
			e.printStackTrace();
		}
		mav.setViewName("reports/viewPastMonthsChildProductPriceDetails");
		return mav;
	}

	@RequestMapping(value = "/GuiReports.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public String GuiReports() {
		return "reports/BIReports";
	}

	
	/**
	 * @description this method id for getting the total requested amount location wise
	 * @param request
	 * @param session
	 * @return
	 */
	
	
	@Async
	@RequestMapping(value = "/purchaseReport.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String requestedAmountReports(HttpServletRequest request, HttpSession session) {
		//System.out.println("ReportsController.requestedAmountReports()");
		String response = "";
		String tillDatePaymentReq = "7";
		
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		if (siteId.equals("") || siteId == null) {
			response = "redirect:/index";
			return response;
		}
		try {
			//method for loading the requested amount location wise 
			
			String reportData = reportsService.getRequestedAmountReportBySite(siteId, tillDatePaymentReq);
			if (reportData != null)
				if (reportData.length() == 0 || reportData.isEmpty()) {
					response = "No requested amount present in this week.";
				}
			try {
				int PRETTY_PRINT_INDENT_FACTOR = 4;
				JSONObject xmlJSONObj = XML.toJSONObject(reportData);
				response = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
				
			} catch (JSONException je) {
				System.out.println(je.toString());
			}
			//System.out.println("Requested Amount Data\t" + response);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	@Async
	@RequestMapping(value = "/getAllSitesList.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getAllSitesList(HttpServletRequest request, HttpSession session)
	throws JSONException {
		//System.out.println("ReportsController.purhaseTypesAndotalCost() 00");
		String response = "";
		String strSiteId = "";
		String strSiteName = "";
		try {

			String strDate = request.getParameter("requestDate");
			List<Map<String, Object>> allSitesList = utilDao.getAllSitesBasedOnCreationDate(strDate);
			String strEliminatedSitesFromReport =  UIProperties.validateParams.getProperty("ELIMINATE_SITES_FROM_STORE_REPORT");

			for(Map productList : allSitesList){ 

				strSiteName = productList.get("SITE_NAME") == null ? "" : productList.get("SITE_NAME").toString();
				strSiteId = productList.get("SITE_ID") == null ? "" : productList.get("SITE_ID").toString();
				
				
				if(!strEliminatedSitesFromReport.contains(strSiteId)){

					response += "<SITE><SITEID>"+strSiteId+"</SITEID><SITENAME><![CDATA["+strSiteName+"]]></SITENAME></SITE>";
				}
			}
			response = "<XML>"+response+"</XML>";


		} catch (Exception e) {
			e.printStackTrace();
		}

		int PRETTY_PRINT_INDENT_FACTOR = 4;
		JSONObject xmlJSONObj = XML.toJSONObject(response);
		response = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
		
		return response;
		
	}


	@RequestMapping(value = "/TotalReceiveIssueAmount.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String getTotalReceivedAndIssuedQuantity(@RequestParam("reqDate") String strDate,
			@RequestParam("siteId") String strSiteId) throws JSONException
	{
		String response = "";
		try {

			response = reportsService.getTotalReceivedAndIssuedQuantity(strSiteId, strDate);


		} catch (Exception e) {
			e.printStackTrace();
		}
		int PRETTY_PRINT_INDENT_FACTOR = 4;
		JSONObject xmlJSONObj = XML.toJSONObject(response);
		response = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
		
		return response;

		
	}
	
	@Async
	@RequestMapping(value = "/purhaseTypesAndotalCost.spring", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody String purhaseTypesAndotalCost(HttpServletRequest request, HttpSession session)
			throws JSONException {
		//System.out.println("ReportsController.purhaseTypesAndotalCost() 00");
		String response = "";
		String tillDatePaymentReq = "7";

		response = "<xml><MarketPurchase><Month>05</Month><Cost>30000</Cost></MarketPurchase><MarketPurchase><Month>06</Month><Cost>20000</Cost></MarketPurchase><MarketPurchase><Month>07</Month><Cost>25000</Cost></MarketPurchase><MarketPurchase><Month>08</Month><Cost>10000</Cost></MarketPurchase><LocalPurchase><Month>05</Month><Cost>15000</Cost></LocalPurchase><LocalPurchase><Month>06</Month><Cost>20000</Cost></LocalPurchase><LocalPurchase><Month>07</Month><Cost>30000</Cost></LocalPurchase><LocalPurchase><Month>08</Month><Cost>5000</Cost></LocalPurchase><POPurchase><Month>05</Month><Cost>30000</Cost></POPurchase><POPurchase><Month>06</Month><Cost>25000</Cost></POPurchase><POPurchase><Month>07</Month><Cost>30000</Cost></POPurchase><POPurchase><Month>08</Month><Cost>3000</Cost></POPurchase></xml>";
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

		String reportData = "";
		try {
			//method for showing Market,Local,PO purchase for last three months
			reportData = reportsService.purhaseTypesAndotalCost(siteId, tillDatePaymentReq);
			//System.out.println(reportData);
		} catch (Exception e) {
			e.printStackTrace();
		}

		int PRETTY_PRINT_INDENT_FACTOR = 4;
		JSONObject xmlJSONObj = XML.toJSONObject(reportData);
		reportData = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);

		//System.out.println("reportData " + reportData + "\n\n");
		return reportData;
	}
	
	public static void main(String[] args) {

		String s = "";
		System.out.println(s.length());
		System.exit(0);
		int PRETTY_PRINT_INDENT_FACTOR = 4;
		String productDetail = new Gson().toJson(
				"{\"xml\": { \"label\": [ { \"name\": \"10-11-2018\", \"Hyderabad\": \"30\", \"Banglore\": \"20\" }, { \"name\": \"11-11-2018\", \"Hyderabad\": \"40\", \"Banglore\": \"20\" },{ \"11-12-2018\": \"ww\", \"Hyderabad\": \"40\", \"Banglore\": \"20\" },{ \"11-12-2018\": \"ww\", \"Hyderabad\": \"40\", \"Banglore\": \"20\" },{ \"11-12-2018\": \"ww\", \"Hyderabad\": \"40\", \"Banglore\": \"20\" },{ \"11-12-2018\": \"ww\", \"Hyderabad\": \"40\", \"Banglore\": \"20\" } ]}}");

		System.out.println(productDetail);
		String xmlData = "<root><element><Country>United States</Country><CustomerId>1</CustomerId><Name>John Hammond</Name><Orders><element><Freight>32.38</Freight><OrderId>10248</OrderId><ShipCountry>France</ShipCountry></element><element><Freight>12.43</Freight><OrderId>10249</OrderId><ShipCountry>Japan</ShipCountry></element><element><Freight>66.35</Freight><OrderId>10250</OrderId><ShipCountry>Russia</ShipCountry></element></Orders></element>"
				+ "<element><Country>India</Country><CustomerId>2</CustomerId><Name>Mudassar Khan</Name><Orders><element><Freight>77.0</Freight><OrderId>10266</OrderId><ShipCountry>Argentina</ShipCountry></element><element><Freight>101.12</Freight><OrderId>10267</OrderId><ShipCountry>Australia</ShipCountry></element><element><Freight>11.61</Freight><OrderId>10268</OrderId><ShipCountry>Germany</ShipCountry></element></Orders></element></root>";
		try {
			JSONObject xmlJSONObj = XML.toJSONObject(xmlData);
			String jsonPrettyPrintString = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
			System.out.println(jsonPrettyPrintString);
		} catch (JSONException je) {
			System.out.println(je.toString());
		}

	}
}
