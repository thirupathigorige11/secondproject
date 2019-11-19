package com.sumadhura.in;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.userDetails;
import com.sumadhura.transdao.IndentAvailabulityDao;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.util.CheckSessionValidation;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;


@org.springframework.stereotype.Controller
public class IndentAvailabulityController extends  UIProperties{

	@Autowired

	private IndentAvailabulityDao dao;

	@Autowired
	private UtilDao utilDao;

	@RequestMapping("/showIndentavalabulityOptions.spring")
	public ModelAndView Show(HttpServletRequest request, userDetails userDts, HttpSession session) {

		ModelAndView mav = new ModelAndView();
		String userid = "0";
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		try{
			userid = (String) CheckSessionValidation.validateUserSession(mav, session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			if (!userid.equals("0")) {
				List<Map<String, Object>> totalProductList = utilDao.getTotalProducts(sessionSite_id);
	
				List<Map<String, Object>> allSitesList = utilDao.getAllSites();
	
				String strAdminUserId = validateParams.getProperty("AdminId");
				if (userid.equals(strAdminUserId)) {
					request.setAttribute("SEARCHTYPE", "ADMIN");
				}
				request.setAttribute("allSitesList", allSitesList);
				request.setAttribute("totalProductsList", totalProductList);
				mav.setViewName("IndentAvailabulity");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			audit.auditLog("0",user_id,"View Product quantity Viewed","success",sessionSite_id);
			
		
		
		return mav;

	}



	@RequestMapping("/centralview.spring")
	public ModelAndView getProductDetails(HttpServletRequest request, HttpServletResponse response) throws Exception {

		System.out.println("central view controller");

		String strProduct = request.getParameter("combobox_Product") == null ? "" : request.getParameter("combobox_Product").toString();
		String strSubProduct = request.getParameter("combobox_SubProduct") == null ? "" : request.getParameter("combobox_SubProduct").toString();
		String strChildProduct = request.getParameter("combobox_ChildProduct") == null ? "" : request.getParameter("combobox_ChildProduct").toString();

		String userid = "0";
		String strProductId = "";
		String strSubProductId = "";
		String strChildProductId = "";


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
		strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();

		String strAdminUserId = validateParams.getProperty("AdminId");


		if (strSiteId.equals(strAdminUserId)) {
			request.setAttribute("SEARCHTYPE", "ADMIN");
			strSiteId = request.getParameter("dropdown_SiteId") == null ? "" : request.getParameter("dropdown_SiteId");



			if(strSiteId.equals("")){

				strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			}
		}

		System.out.println("Indent Avaliable controller product id " + strProductId +"  sub product id child"+strSubProductId +" child product id "+strChildProductId+" site id"+strSiteId);


		List<ProductDetails> productList = dao.getProductList(strProductId, strSubProductId, strChildProductId,strSiteId);

		request.setAttribute("productList", productList);
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"View Product Quantity clicked submit","success",site_id1);
			
		

		return new ModelAndView("IndentAvailabulityView");

	}
	
	
	@RequestMapping("/getSubProducts.spring")
	public  @ResponseBody List<ProductDetails> getAllSubProductList(@RequestParam(value = "prodId") String prodId){

		System.out.println("get Total Products and sub products..."+prodId);
		List<ProductDetails> listAllProductsList = new ArrayList<ProductDetails>();
		try{

			if(prodId.contains("@@")){

				String productArr[] = prodId.split("@@");

				if(productArr != null && productArr.length>=1 ){
					prodId = productArr[0].trim();
					//strProductName = productArr[1].trim();
				}
			}

			listAllProductsList = utilDao.getAllSubProducts(prodId);
			System.out.println("get All Products List..." + listAllProductsList.size()); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return listAllProductsList;
	}
	@RequestMapping("/getChildProducts.spring")
	public  @ResponseBody List<ProductDetails> getAllChildProductList(@RequestParam(value = "subProdId") String strSubProdId, HttpServletResponse response) throws Exception{

		System.out.println("get Total Products and sub products..."+strSubProdId);
		List<ProductDetails> listAllProductsList = new ArrayList<ProductDetails>();
		
		try{

			if(strSubProdId.contains("@@")){

				String productArr[] = strSubProdId.split("@@");

				if(productArr != null && productArr.length>=1 ){
					strSubProdId = productArr[0].trim();
					//strProductName = productArr[1].trim();
				}
			}

			listAllProductsList = utilDao.getAllChildProducts(strSubProdId);
			System.out.println("get All Products List..." + listAllProductsList.size()); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return listAllProductsList;
	}
	@RequestMapping("/getChildProductGroups.spring")
	public  @ResponseBody List<ProductDetails> getChildProductGroups( HttpServletResponse response) throws Exception{

		List<ProductDetails> listAllProductsList = new ArrayList<ProductDetails>();
		
		try{

			

			listAllProductsList = utilDao.getAllChildProductGroups();
			System.out.println("get All Products List..." + listAllProductsList.size()); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return listAllProductsList;
	}



	@RequestMapping("/TotalReceivedQuantity.spring")
	public String TotalReceivedQuantity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//int num=1;
		
		String responseValue="";
		String childProductId=request.getParameter("strChildProductId");
		String strSiteId=request.getParameter("strSiteId");
		String fromDate=request.getParameter("fromDate");
		String toDate=request.getParameter("toDate");
		List<ProductDetails> productList=dao.TotalReceivedQuantity(childProductId,strSiteId,fromDate,toDate);
		List<ProductDetails> productList1=dao.TotalReceivedQuantityForDc(childProductId,strSiteId,fromDate,toDate);
		System.out.println("the product List is"+productList.size());
		request.setAttribute("ReceivedQuantity","true");
		
		request.setAttribute("productList", productList);
		request.setAttribute("productList1", productList1);
		responseValue="TotalReceivedQuantity";
		
		
	//	dao.ViewIssuedQuantity();
		
		
		return responseValue;
		
	}
	
	
	@RequestMapping("/TotalIssuedQuantity.spring")
	public String TotalIssuedQuantity(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//int num=1;
		
		String responseValue="";
		String childProductId=request.getParameter("strChildProductId");
		String strSiteId=request.getParameter("strSiteId");
		String fromDate=request.getParameter("fromDate");
		String toDate=request.getParameter("toDate");
		List<ProductDetails> productList=dao.TotalIssuedQuantity(childProductId,strSiteId,fromDate,toDate);
		System.out.println("the product List is"+productList.size());
		request.setAttribute("ReceivedQuantity","false");
		request.setAttribute("productList", productList);
		responseValue="TotalReceivedQuantity";
		
		
	//	dao.ViewIssuedQuantity();
		
		
		return responseValue;
		
	}
	
	
	
	

}
