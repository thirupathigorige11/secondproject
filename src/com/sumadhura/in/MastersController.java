package com.sumadhura.in;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.codec.binary.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.sumadhura.bean.ContractorBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.service.ExcelUploadProductsReceiveIssueService;
import com.sumadhura.dto.AdminTempChildProductStatusDto;

import com.sumadhura.transdao.ProductDao;
import com.sumadhura.transdao.SchedulerDao;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.util.CheckSessionValidation;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
import com.sumadhura.bean.TransportorBean;

@Controller
public class MastersController extends UIProperties{
	@Autowired
	private ProductDao dao;

	@Autowired
	private UtilDao utilDao;
	
	
	@Autowired
	private SchedulerDao schDao;
	
	@Autowired
	private ExcelUploadProductsReceiveIssueService objExcelUploadProductsReceiveIssueService;


	@RequestMapping("/AddProduct.spring")
	public  ModelAndView addform(HttpServletRequest request,HttpSession session){  
		ModelAndView mav = new ModelAndView();
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));

		//List<ProductDetails> listProductsList = new ArrayList<ProductDetails>();
		try{

			List<Map<String,Object>> totalProductList = utilDao.getTotalProducts(site_id1);
			System.out.println("ADD PRODUCT "+totalProductList.size());
			request.setAttribute("totalProductsList",totalProductList);
			request.setAttribute("NormalSelection",true);

		}catch(Exception e){
			e.printStackTrace();
		}
		mav.setViewName("AddProduct");
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			audit.auditLog("0",user_id,"Product Master Viewed","success",site_id1);
		
		return mav;  
	}  
	
	
	
	@RequestMapping(value = "/saveproduct.spring", method = RequestMethod.POST)
	public ModelAndView saveproduct(HttpServletRequest request,HttpSession session) throws Exception {

		System.out.println("product controler");
		String strServiceType = "";
		String strProductName = "";
		String productType="";
		ModelAndView model = new ModelAndView();
		List<Map<String, Object>> totalProductList = null;
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		
		try {
			
			strServiceType = request.getParameter("ServiceType");
			int intProductId = dao.getSeqNo("combobox_Product");
			String strProductId = "PRD" + intProductId;

			System.out.println("strProduct name " + strProductName);
			System.out.println("strProduct name " + strProductId);
			int intCount = 0;
			
			if (strServiceType.equals("add")) {

				strProductName = request.getParameter("product") == null ? "" : request.getParameter("product").toString();
				
				// this is used for user given already existed product given then check and show already existed at that time user again select add product so use it add product sub module
				
				int res = dao.checkProductNameAvailableOrNot(strProductName,site_id,"");
				if (res == 0) {
					intCount = dao.saveproduct(strProductName, strProductId,"",site_id);
					request.setAttribute("succMessage", "Product Name has been added successfully");
				} else {
					request.setAttribute("errMessage", "Product Name was already Added, Please try with Another Product Name");
				}
			} else if (strServiceType.equals("delete")) {

				String strProduct = request.getParameter("product_delete1") == null ? "" : request.getParameter("product_delete1").toString();
				if (strProduct.contains("@@")) {
					String productArr[] = strProduct.split("@@");
					if (productArr != null && productArr.length >= 1) {
						strProductId = productArr[0].trim();
						strProductName = productArr[1].trim();
					}
				}
			
				intCount = dao.deleteproduct(strProductId,site_id,productType);
				if (intCount > 0) {

					request.setAttribute("succMessage", "Product deleted successfully");
				} else {
					request.setAttribute("errMessage", "Can not Delete Product, As Product was used.");
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("AddProduct");
			totalProductList = utilDao.getTotalProducts(site_id);
			request.setAttribute("totalProductsList", totalProductList);
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Product Master clicked submit","success",site_id1);
			
		
		
		
		return model;
	}
	
	
	
	@RequestMapping("/viewaddedproduct.spring")
	public  @ResponseBody List<ProductDetails> getproductList(@RequestParam("AllProducts") String AllProducts,HttpSession session){

		System.out.println("get Total Products...");
		List<ProductDetails> listProductsList = new ArrayList<ProductDetails>();
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		try{
			listProductsList = dao.getTotalProducts(sessionSite_id,AllProducts);
			System.out.println("get Total Products..." + listProductsList.size()); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			audit.auditLog("0",user_id," Product Master Viewed All details","success",sessionSite_id);
		
		return listProductsList;
	}



	@RequestMapping(value="/AddSubProduct.spring")  
	public  ModelAndView addsubform(HttpServletRequest request,HttpSession session){  
		ModelAndView mav = new ModelAndView();
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		List<Map<String,Object>> totalProductList = utilDao.getTotalProducts(site_id1);
		request.setAttribute("totalProductsList",totalProductList);

		/*List<Map<String,Object>> totalSubProductList = utilDao.getTotalSubProducts();
		request.setAttribute("totalSubProductsList", totalSubProductList);*/

		mav.setViewName("AddSubProduct");
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			audit.auditLog("0",user_id,"Sub Product Master Viewed","success",site_id1);
			
		
		return mav;  
	} 
	
	
	@RequestMapping(value = "/savesubproduct.spring", method = RequestMethod.POST)
	public ModelAndView savesubproduct(HttpServletRequest request,HttpSession session) throws Exception {

		int intCount = 0;
		String strProdId = "";
		System.out.println("Master controler for save subproduct");
		String strProduct = "";
		String strProductId = "";
		String strSubProdId = "";
		String strSubProduct = "";
		String strServiceType = "";
		String strSubProductName = "";
		ModelAndView model = new ModelAndView();
		List<Map<String, Object>> totalProductList = null;
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		String selectedSiteDataOrNot="";

		try {

			strServiceType = request.getParameter("ServiceType");
			selectedSiteDataOrNot=request.getParameter("selectedSiteData") == null ? "" : request.getParameter("selectedSiteData").toString();
			
			if(selectedSiteDataOrNot.equalsIgnoreCase("true")){
				model.setViewName("AddSubProductMarketingandStore");
			}else{
				model.setViewName("AddSubProduct");
			}

			if (strServiceType.equals("add")) {

				strProduct = request.getParameter("combobox_Product1") == null ? request.getParameter("product_Add1") == null ? "" : request.getParameter("product_Add1").toString() : request.getParameter("combobox_Product1").toString();
				strSubProductName = request.getParameter("subproduct") == null ? request.getParameter("subproduct") == null ? "" : request.getParameter("subproduct").toString() : request.getParameter("subproduct").toString();
				
				
				if (strProduct.contains("@@")) {

					String productArr[] = strProduct.split("@@");

					if (productArr != null && productArr.length >= 1) {
						strProductId = productArr[0].trim();
					}
				}

				int count = dao.checkSubProductNameAvailableOrNot(strProductId, strSubProductName);
				if (count == 0) {
					int getSubProductId = dao.getSeqNo("delete_SubProd");
					String strSubProductId = "SUB" + getSubProductId;
					intCount = dao.savesubproduct(strProductId, strSubProductId, strSubProductName);
					if (intCount > 0) {
						request.setAttribute("succMessage", "Sub Product Name has been added successfully");
					} else {
						request.setAttribute("errMessage", "Sub Product Name Not added.");
					}
				} else {
					request.setAttribute("errMessage", "Sub Product Name was already Added, Please try with Another Sub Product Name.");
				}

			} else if (strServiceType.equals("delete")) {

				strProduct = request.getParameter("combobox_Delete_Product1") == null ? request.getParameter("product_Delete") == null ? "": request.getParameter("product_Delete").toString(): request.getParameter("combobox_Delete_Product1").toString();
				strSubProduct = request.getParameter("combobox_delete_SubProd1") == null ? request.getParameter("combobox_delete_SubProd") == null ? "": request.getParameter("combobox_delete_SubProd").toString() : request.getParameter("combobox_delete_SubProd1").toString();
				if (strProduct.contains("@@")) {
					String productArr[] = strProduct.split("@@");
					if (productArr != null && productArr.length >= 1) {
						strProdId = productArr[0].trim();
					}
				}
				if (strSubProduct.contains("@@")) {
					String subProductArr[] = strSubProduct.split("@@");
					if (subProductArr != null && subProductArr.length >= 1) {
						strSubProdId = subProductArr[0].trim();
					}
				}
				intCount = dao.deletesubproduct(strProdId, strSubProdId);
				if (intCount == 1) {

					request.setAttribute("succMessage", "Sub Product Name deleted successfully");
				} else {
					request.setAttribute("errMessage", "Can not Delete Sub Product, As Sub Product was used.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Inside Save Subproduct() in Master Controller class   "+ex.getMessage());
		} finally {
			totalProductList = utilDao.getTotalProducts(sessionSite_id);
			request.setAttribute("totalProductsList", totalProductList);
			
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			audit.auditLog("0",user_id,"Sub Product Master clicked submit","success",sessionSite_id);
			
		
		
		
		return model;
	}
	
	
	
	
	
	@RequestMapping("/ViewSubProduct.spring")
	public  @ResponseBody List<ProductDetails> getSubProductList(HttpSession session,@RequestParam("AllProducts") String AllProducts){

		System.out.println("get Total Products and sub products...");
		List<ProductDetails> listSubProductsList = new ArrayList<ProductDetails>();
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		try{
			listSubProductsList = dao.getTotalSubProducts(sessionSite_id,AllProducts);
			System.out.println("get Total Products and sub products..." + listSubProductsList.size()); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			audit.auditLog("0",user_id,"sub product Viewed All Details","success",sessionSite_id);
		
		
		
		
		
		return listSubProductsList;
	}

	@RequestMapping(value="/AddChildProduct.spring")  
	public  ModelAndView showform(HttpServletRequest request,HttpSession session){  
		ModelAndView mav = new ModelAndView();
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		List<Map<String,Object>> totalProductList = utilDao.getTotalProducts(sessionSite_id);
		request.setAttribute("totalProductsList",totalProductList);

		/*List<Map<String,Object>> allProductList = utilDao.getAllChildProducts();
		request.setAttribute("totalChildProductsList", allProductList);*/

		mav.setViewName("AddChildProduct");
		
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			audit.auditLog("0",user_id,"child product Viewed","success",sessionSite_id);
				
			return mav;  
	}  

	@RequestMapping(value = "/save.spring", method = RequestMethod.POST)
	public ModelAndView saveChildProduct(HttpServletRequest request,HttpSession session) throws Exception {

		int intCount = 0;
		String strproduct = "";
		String strProductId ="";
		String strProductName = "";
		String strsubproduct = "";
		String strServiceType = "";
		String strSubProductId = "";
		String strSubProductName = "";
		String strChildProduct = "";
		String strmeasurementId = "";
		String strChildProductId = "";
		String strmeasurementName = "";
		String strChildProductName = "";
		String materialGroupId = "";
		String remarks = "";
		ModelAndView model = new ModelAndView();
		List<Map<String, Object>> totalProductList = null;
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String selectedSiteDataOrNot="";

		try {

			strServiceType = request.getParameter("ServiceType");
			selectedSiteDataOrNot=request.getParameter("selectedSiteData") == null ? "" : request.getParameter("selectedSiteData").toString();
			
			if(selectedSiteDataOrNot.equalsIgnoreCase("true")){
				model.setViewName("AddChildProductMarketingandStore");
			}else{
				model.setViewName("AddChildProduct");
			}
			if (strServiceType.equals("add")) {

				strproduct = request.getParameter("combobox_Product1") == null ? (request.getParameter("product_Add1")==null?"":request.getParameter("product_Add1")) : request.getParameter("combobox_Product1").toString();
				strsubproduct = request.getParameter("combobox_SubProd1") == null ? "" : request.getParameter("combobox_SubProd1").toString();
				
				if (strproduct.contains("@@")) {

					String productArr[] = strproduct.split("@@");

					if (productArr != null && productArr.length >= 1) {
						strProductId = productArr[0].trim();
						strProductName = productArr[1].trim();
					}
				}
				if (strsubproduct.contains("@@")) {

					String subproductArr[] = strsubproduct.split("@@");

					if (subproductArr != null && subproductArr.length >= 1) {
						strSubProductId = subproductArr[0].trim();
						strSubProductName = subproductArr[1].trim();
					}
				}
				strChildProductName = request.getParameter("childproduct");
				strmeasurementName = request.getParameter("measurement");
				materialGroupId = request.getParameter("materialGroupId");
				remarks = request.getParameter("remarks");
				int count = dao.checkChildProductNameAvailableOrNot(strSubProductId, strChildProductName);
				if (count == 0) {
					int getChildProductId = dao.getSeqNo("childproduct");
					strChildProductId = "CHP" + getChildProductId;

					int getmeasurementId = dao.getSeqNo("measurement");
					strmeasurementId = "MST" + getmeasurementId;

					System.out.println("strsubProduct name " + strSubProductId);
					//intCount = dao.saveChildProduct(strChildProductName, strChildProductId, strSubProductId, strmeasurementName, strmeasurementId);
					//--Rafi
					String pendingEmployeeId = dao.getPendingEmpIdForCP_APPR(user_id, sessionSite_id);
					AdminTempChildProductStatusDto tempChildProductDto = new AdminTempChildProductStatusDto();
					int tempChildProductId = dao.geTempChildproductSeq();
					tempChildProductDto.setTempChildProductId(String.valueOf(tempChildProductId));
					tempChildProductDto.setProductId(strProductId);
					tempChildProductDto.setProductName(strProductName);
					tempChildProductDto.setSubProductId(strSubProductId);
					tempChildProductDto.setSubProductName(strSubProductName);
					tempChildProductDto.setChildProductId(strChildProductId);
					tempChildProductDto.setChildProductName(strChildProductName);
					tempChildProductDto.setMeasurementId(strmeasurementId);
					tempChildProductDto.setMeasurementName(strmeasurementName);
					tempChildProductDto.setPendingEmployeeId(pendingEmployeeId);
					tempChildProductDto.setChildProductStatus("A");
					tempChildProductDto.setMaterialGroupId(materialGroupId);
					if(pendingEmployeeId.equals("END")){
						intCount = dao.saveChildProduct(strChildProductName, strChildProductId, strSubProductId, strmeasurementName, strmeasurementId, materialGroupId);
					}
					else{
						intCount = dao.saveTempChildProduct(tempChildProductDto);
					}
					dao.saveTempChildProdAprRejDtls(String.valueOf(tempChildProductId),user_id,"C",remarks);
					//------
					
					if (intCount == 1) {
						request.setAttribute("succMessage", "Child Product Name has been Added successfully");
					} else {
						request.setAttribute("Message", "Child Product Name Not Added.");
					}
				} else {

					request.setAttribute("errMessage", "Child Product Name was already Added, Please try with Another Child Product Name.");
				}

			} else if (strServiceType.equals("delete")) {

				strChildProduct = request.getParameter("combobox_delete_ChildProd1") == null ? "" : request.getParameter("combobox_delete_ChildProd1").toString();
				strChildProductId = "";

				if (strChildProduct.contains("@@")) {

					String childProductArr[] = strChildProduct.split("@@");

					if (childProductArr != null && childProductArr.length >= 1) {
						strChildProductId = childProductArr[0].trim();
					}
				}
				 strsubproduct = request.getParameter("combobox_delete_SubProd1") == null ? "" : request.getParameter("combobox_delete_SubProd1").toString();

				if (strsubproduct.contains("@@")) {

					String subproductArr[] = strsubproduct.split("@@");

					if (subproductArr != null && subproductArr.length >= 1) {
						strSubProductId = subproductArr[0].trim();
					}
				}

				intCount = dao.deletechildproduct(strSubProductId, strChildProductId);
				if (intCount == 1) {

					request.setAttribute("succMessage", "child Product Name Deleted successfully");
				} else {
					request.setAttribute("errMessage", "Can not Delete Child Product, As Child Product was used.");
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			totalProductList = utilDao.getTotalProducts(sessionSite_id);
			request.setAttribute("totalProductsList", totalProductList);
			//model.setViewName("AddChildProduct");
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			//String user_id=String.valueOf(session.getAttribute("UserId"));
			
			audit.auditLog("0",user_id,"Child Product Master cicked submit","success",sessionSite_id);
				
		return model;
	}
	
	@RequestMapping(value = "/getPendingChildProductsForApproval.spring", method = RequestMethod.GET)
	public ModelAndView getPendingChildProductsForApproval(HttpServletRequest request,HttpSession session) throws Exception {
		ModelAndView model = new ModelAndView();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		List<AdminTempChildProductStatusDto> ListOfTempChildProducts = dao.getPendingChildProductsForApproval(user_id);
		model.addObject("ListOfTempChildProducts",ListOfTempChildProducts);
		model.addObject("ListSize",ListOfTempChildProducts.size());
		model.setViewName("TempChildProductsList");
		return model;
	}
	@RequestMapping(value = "/getTempChildProductRequestStatus.spring", method = RequestMethod.GET)
	public ModelAndView getTempChildProductRequestStatus(HttpServletRequest request,HttpSession session) throws Exception {
		ModelAndView model = new ModelAndView();
		List<AdminTempChildProductStatusDto> ListOfTempChildProducts = dao.getTempChildProductRequestStatus();
		model.addObject("ListOfTempChildProducts",ListOfTempChildProducts);
		model.addObject("ListSize",ListOfTempChildProducts.size());
		model.setViewName("TempChildProductRequestStatus");
		return model;
	}
	@RequestMapping(value = "/getTempChildProductDetails.spring", method = RequestMethod.GET)
	public ModelAndView getTempChildProductDetails(HttpServletRequest request,HttpSession session) throws Exception {
		ModelAndView model = new ModelAndView();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String tempChildProductId = request.getParameter("tempChildProductId")==null?"":request.getParameter("tempChildProductId");
		AdminTempChildProductStatusDto tempChildProductDto = dao.getTempChildProductDetails(user_id, tempChildProductId);
		
		model.addObject("element",tempChildProductDto);
		model.setViewName("TempChildProductDetails");
		
		return model;
	}
	@RequestMapping(value = "/approveTempChildProduct.spring", method = RequestMethod.POST)
	public String approveTempChildProduct(HttpServletRequest request, HttpSession session,  RedirectAttributes redir) throws Exception {
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		List<String> errMsg = new ArrayList<String>();
		List<String> successMsg = new ArrayList<String>();
		int noOfProds = Integer.parseInt(request.getParameter("noOfTempChildProducts"));
		String tempChildProductId = "";
		String childProductName = "";
		String subProductId = "";
		String remarks = "";
		String checkboxValue = "";
		for(int i = 1 ; i <= noOfProds ; i++){
			checkboxValue = request.getParameter("chkbox"+i)==null?"":request.getParameter("chkbox"+i);
			if(!checkboxValue.equals("checked")){continue;}
			tempChildProductId = request.getParameter("tempChildProductId"+i)==null?"":request.getParameter("tempChildProductId"+i);
			childProductName = request.getParameter("childProductName"+i)==null?"":request.getParameter("childProductName"+i);
			subProductId = request.getParameter("subProductId"+i)==null?"":request.getParameter("subProductId"+i);
			
			remarks = request.getParameter("remarks");
			int count = dao.checkChildProductNameAvailableOrNot(subProductId, childProductName);
			if (count == 0) {
				dao.approveTempChildProduct(tempChildProductId,childProductName,user_id,sessionSite_id,errMsg,successMsg,remarks);
			}
			else {
				errMsg.add("It seems there was already a Child Product with same name as '"+childProductName+"', Check once.");
			}
		}
		redir.addFlashAttribute("errMsg", errMsg);
		redir.addFlashAttribute("successMsg", successMsg);
		return "redirect:/TempChildProductApprovalResponse.spring";
	}
	@RequestMapping(value = "/rejectTempChildProduct.spring", method = RequestMethod.POST)
	public String rejectTempChildProduct(HttpServletRequest request, HttpSession session,  RedirectAttributes redir) throws Exception {
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		List<String> errMsg = new ArrayList<String>();
		List<String> successMsg = new ArrayList<String>();
		int noOfProds = Integer.parseInt(request.getParameter("noOfTempChildProducts"));
		String tempChildProductId = "";
		String childProductName = "";
		String remarks = "";
		String checkboxValue = "";
		for(int i = 1 ; i <= noOfProds ; i++){
			checkboxValue = request.getParameter("chkbox"+i)==null?"":request.getParameter("chkbox"+i);
			if(!checkboxValue.equals("checked")){continue;}
			tempChildProductId = request.getParameter("tempChildProductId"+i)==null?"":request.getParameter("tempChildProductId"+i);
			childProductName = request.getParameter("childProductName"+i)==null?"":request.getParameter("childProductName"+i);
			remarks = request.getParameter("remarks");
			dao.rejectTempChildProduct(tempChildProductId,childProductName,user_id,sessionSite_id,errMsg,successMsg,remarks);
		}
		redir.addFlashAttribute("errMsg", errMsg);
		redir.addFlashAttribute("successMsg", successMsg);
		return "redirect:/TempChildProductApprovalResponse.spring";
	}
	@RequestMapping(value = "/TempChildProductApprovalResponse.spring",  method = RequestMethod.GET)
	public ModelAndView category(){
	    return new ModelAndView("TempChildProductApprovalResponse");
	}
	
	@RequestMapping("/ViewChildProduct.spring")
	public  @ResponseBody List<ProductDetails> getAllChildProductList(HttpSession session,@RequestParam("AllProducts") String AllProducts){

		System.out.println("get Total Products and sub products...");
		List<ProductDetails> listAllProductsList = new ArrayList<ProductDetails>();
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		try{
			listAllProductsList = dao.getAllChildProducts(sessionSite_id,AllProducts);
			System.out.println("get All Products List..." + listAllProductsList.size()); 
		}catch(Exception e){
			e.printStackTrace();
		}
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			audit.auditLog("0",user_id,"Child Product Master viewed All details","success",sessionSite_id);
				
		
		
		
		return listAllProductsList;
	}





	@RequestMapping("/autoSearchproduct.spring")
	public @ResponseBody String getAutoSearchproductNames(HttpSession session,@RequestParam(value = "term") String term,@RequestParam(value = "AllProducts") String AllProducts)  {
		System.out.println("get Total Products...");
		List<String> listProductsList = new ArrayList<String>();
		String searchproductList = "" ;
		try{
			
			String site_id = String.valueOf(session.getAttribute("SiteId"));
			System.out.println("Auto Search for Product Name "+term);
			listProductsList = utilDao.getProductsBySearch(term,site_id,AllProducts);
			System.out.println("Get Product Name List Size "+listProductsList.size());
			searchproductList = new Gson().toJson(listProductsList);
			System.out.println("get Products..." + searchproductList); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return searchproductList;
	}

	@RequestMapping("/autoSearchSubProduct.spring")
	public @ResponseBody String getAutoSearchSubproductNames(@RequestParam(value = "term") String term)  {
		System.out.println("get Total subProducts...");
		List<String> listSubProductList = new ArrayList<String>();
		String searchsubproductList = "" ;
		try{
			listSubProductList = utilDao.getSubProductsBySearch(term);
			searchsubproductList = new Gson().toJson(listSubProductList);
			System.out.println("get subProducts..." + listSubProductList.size()); 
		}catch(Exception e){
			e.printStackTrace();
		}
		return searchsubproductList;
	}
	
	
	@RequestMapping("/autoSearchchildproduct.spring")
	public @ResponseBody String getAutoSearchNames(@RequestParam(value = "term") String term,@RequestParam(value = "subProductId") String subProductId)  {
		List<String> listAllProductsList = new ArrayList<String>();
		String searchList = "";
		String strSubProductId = "";
		try{
			
			if(subProductId.contains("@@")){
				String subProductIdArr [] = subProductId.split("@@");
				strSubProductId = subProductIdArr[0];
			}
			
			listAllProductsList = utilDao.getchildProductsBySearch(strSubProductId,term);
			searchList = new Gson().toJson(listAllProductsList);
			System.out.println("get vendor List..." + listAllProductsList.size()); 

		}catch(Exception e){
			e.printStackTrace();
		}
		return searchList;
	}
	
	
	
	@RequestMapping("/uploadExcel.spring")
	public  ModelAndView uploadExcelData(HttpServletRequest request,HttpSession session){  
		ModelAndView mav = new ModelAndView();

		//List<ProductDetails> listProductsList = new ArrayList<ProductDetails>();
		try{

		  // schDao.startReadExcelDataForAddChildProducts() ;
			//schDao.startReadExcelData();
			
   	objExcelUploadProductsReceiveIssueService.indentProcess( request,  session);
			//schDao.updatePriceListIdInIndentEntryTable();
			//schDao.updatePriceListIdInIndentEntryTable();

		}catch(Exception e){
			e.printStackTrace();
		}
		mav.setViewName("AddProduct");
		return mav;
	}

	@RequestMapping(value = "/getContractorDetails.spring", method = RequestMethod.GET)
	public String getContractorDetails(@RequestParam("contractorId") String contractorId, Model model,
			HttpServletRequest request) {
		try {
			String isContractorEditable=request.getParameter("isContractorEditable")==null?"true":request.getParameter("isContractorEditable");
			model.addAttribute("isContractorEditable", isContractorEditable);
			List<ContractorBean> contractorBean = utilDao.getAllContratorDetails(contractorId);
			ContractorBean bean = contractorBean.get(0);
			model.addAttribute("ContractorBean", bean);
			String conId=bean.getContractor_id().replace("/", "-");
			int count = 0;
			int getLocalPort=request.getLocalPort();
			String strContextAndPort = "";
			String path = "";
			if(getLocalPort == 8080){ //Local
				strContextAndPort = UIProperties.validateParams.getProperty("CONTRACTOR_LOCAL_IP_PORT");
			}else if(getLocalPort == 8078){ //local machine
				strContextAndPort = UIProperties.validateParams.getProperty("CONTRACTOR_UAT_IP_PORT");
			}else if(getLocalPort == 8079){ //CUG
				strContextAndPort = UIProperties.validateParams.getProperty("CONTRACTOR_CUG_IP_PORT");
			}else if(getLocalPort == 80){ //LIVE
				strContextAndPort = UIProperties.validateParams.getProperty("CONTRACTOR_LIVE_IP_PORT");
			}
			for(int i=0;i<8;i++){
				String rootFilePath = UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH").toString();
				File dir = new File(rootFilePath+bean.getContractor_id().replace("/", "-"));
				File file = new File(rootFilePath+conId+"/"+conId+"_Part"+i+".jpg");
	 			if(file.exists()){
					count++;
					DataInputStream dis = new DataInputStream(new FileInputStream(file));
					byte[] barray = new byte[(int) file.length()];
					dis.readFully(barray); 
					byte[] encodeBase64 = Base64.encodeBase64(barray);
					String base64Encoded = new String(encodeBase64, "UTF-8");
					model.addAttribute("image"+i,base64Encoded);
					path=strContextAndPort+conId+"/"+conId+"_Part"+i+".jpg";
					String pathForDeleteFile = file.getAbsolutePath().replace("\\", "$");
					//adding image path
					model.addAttribute("imageFilePath" + i, path);
					model.addAttribute("deleteFilePath" + i, pathForDeleteFile);
					if(dis!=null){
						try {
							dis.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			request.setAttribute("imagecount", count);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "ContractorUpdatePage";
	}

	@RequestMapping(value = "/updateContractorDetail.spring", method = RequestMethod.GET)
	public String updateContractorDetail(Model model, HttpServletRequest request) {
		try {
			String isContractorEditable=request.getParameter("isContractorEditable")==null?"true":request.getParameter("isContractorEditable");
			List<ContractorBean> contractorBeans = utilDao.getAllContratorDetails("");
			System.out.println("MastersController.updateContractorDetail()" + contractorBeans);
			
			model.addAttribute("contractorList", contractorBeans);
			model.addAttribute("isContractorEditable", isContractorEditable);
			model.addAttribute("succMessage",request.getParameter("succMessage") == null ? " " : request.getParameter("succMessage"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ShowAllContractors";
	}
	
	
	@RequestMapping(value = "/viewContractors.spring", method = RequestMethod.GET)
	public String viewContractors(Model model, HttpServletRequest request) {
		try {
			List<ContractorBean> contractorBeans = utilDao.getAllContratorDetails("");
			System.out.println("MastersController.updateContractorDetail()" + contractorBeans);
			model.addAttribute("contractorList", contractorBeans);
			model.addAttribute("isContractorEditable", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ShowAllContractors";
		}
	
	@RequestMapping(value = "/contractorRegistration.spring", method = RequestMethod.GET)
	public String contractorRegistration(Model model, HttpServletRequest request) {
		System.out.println("MastersController.contractorRegistration()");
		ContractorBean bean = new ContractorBean();
		model.addAttribute("ContractorBean", bean);
		
		model.addAttribute("succMessage",request.getParameter("succMessage") == null ? " " : request.getParameter("succMessage"));
		
		return "ContractorRegistrationPage";
	}
	
	@RequestMapping(value = "/validatContractorname.spring", method = RequestMethod.GET)
	public @ResponseBody boolean validatContractorname(HttpSession session,HttpServletRequest request){
		String first_name=request.getParameter("first_name")==null?"":request.getParameter("first_name");
		String last_name=request.getParameter("last_name")==null?"":request.getParameter("last_name");
		String condition=request.getParameter("condition")==null?"":request.getParameter("condition");
		String contractor_id=request.getParameter("contractor_id")==null?"":request.getParameter("contractor_id");
		boolean flag= utilDao.validateContractorName(first_name.trim(),last_name.trim(),condition.trim(),contractor_id.trim());
		return flag;
		
	}
	@RequestMapping(value = "/validatContractorpanNumber.spring", method = RequestMethod.GET)
	public @ResponseBody boolean validatContractorMobileNumber(Model model,	@RequestParam("panNumber") String panNumber,@RequestParam("condition") String condition,HttpServletRequest  request) {
		String contractorId=request.getParameter("contractor_id")==null?"":request.getParameter("contractor_id");
		System.out.println("MastersController.validatContractorMobileNumber() " + panNumber+" "+condition+" "+contractorId);
		model.addAttribute("ContractorBean", new ContractorBean());
		model.addAttribute("succMessage", " ");
		boolean resp = utilDao.validatePanNumber(panNumber.trim(),condition,contractorId);
		return resp;
	}

	@RequestMapping(value = "/saveContractorDetail.spring", method = RequestMethod.POST)
	public ModelAndView saveContractorDetail(@ModelAttribute("ContractorBean") ContractorBean contractorBean,@RequestParam("file") MultipartFile[] files,
			HttpServletRequest request, HttpSession session) {
		System.out.println("MastersController.saveContractorDetail()");
		ModelAndView mav = new ModelAndView();
		String response = "";
		try {
			System.out.println(contractorBean);
			ContractorBean bean = (ContractorBean) contractorBean.clone();
			ContractorBean bean1 = new ContractorBean();
			
			if(bean.getFirst_name()==null&&bean.getLast_name()==null&&bean.getMobile_number()==null){
				mav.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				mav.setViewName("response");
				return mav;
			}

			String buttonType=request.getParameter("submitBtn")==null?"":request.getParameter("submitBtn");
		
			String siteId = (String) CheckSessionValidation.validateUserSession(mav,session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			if(buttonType.equals("Delete")){
				System.out.println(buttonType);
				try {
					response=	utilDao.deleteContractorDeteail(bean, siteId, user_id, request, session);
					mav.setViewName("response");
					List<ContractorBean> contractorBeans = utilDao.getAllContratorDetails("");
					System.out.println("MastersController.updateContractorDetail()" + contractorBeans);
					mav.addObject("contractorList", contractorBeans);
					mav.addObject("succMessage", response);
					request.setAttribute("message",response);
				//mav.setViewName("ShowAllContractors");
					mav.addObject("ContractorBean", bean1);
					String rootFilePath = UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH").toString();
					File file=new File(rootFilePath);
					File[] file1=file.listFiles();
							for (File file2 : file1) {
								if(file2.equals(bean.getPan_number())){
									System.out.println("Got File "+file2.delete());
								}
							}
					File dir = new File(rootFilePath+bean.getPan_number());
				
					//System.out.println("File Deleted "+(dir.delete())+" Folder Name is "+bean.getPan_number());
				
				} catch (Exception e) {
					response="Failed to delete contractor.";
					request.setAttribute("message1",response);
					mav.setViewName("response");
				
				}
				return mav;
				
			}else if(buttonType.equals("Update")){
				System.out.println(buttonType);
				try {
					mav.setViewName("response");	
					response = utilDao.updateContractorDetail(bean, siteId, user_id, request, session);
					List<ContractorBean> contractorBeans = utilDao.getAllContratorDetails("");
					System.out.println("MastersController.updateContractorDetail()" + contractorBeans);
					mav.addObject("contractorList", contractorBeans);
					mav.addObject("succMessage", response);
					mav.addObject("ContractorBean", bean1);
					request.setAttribute("message",response);
					int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent"));
					int k=imagesAlreadyPresent;
					
					String PdfPathtodelete[]=request.getParameterValues("PdfPathtodelete");
					String imgPathtoDelete[]=request.getParameterValues("imgPathtoDelete");
					if(imgPathtoDelete!=null)
					for (String imgPath : imgPathtoDelete) {
						request.setAttribute("imagePath", imgPath.split("&")[0].split("=")[1]);
						request.setAttribute("isRequestFromController", true);
						deleteContractorImage(request);
						
					}
					if(PdfPathtodelete!=null)
					for (String pdfPath : PdfPathtodelete) {
						request.setAttribute("imagePath", pdfPath.split("&")[0].split("=")[1]);
						request.setAttribute("isRequestFromController", true);
						deleteContractorImage(request);
					}
					
					for (int i = imagesAlreadyPresent; i < files.length; i++) {
						MultipartFile multipartFile = files[i];
						if(!multipartFile.isEmpty()){
						try {
								String rootFilePath = UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH").toString();
								File dir = new File(rootFilePath+bean.getContractor_id().replace("/", "-"));
								if (!dir.exists())
									dir.mkdirs();

								String filePath = dir.getAbsolutePath()
								+ File.separator + bean.getContractor_id().replace("/", "-")+"_Part"+k+".jpg"; 
								k++;
								File convFile = new File(filePath);
							    convFile.createNewFile(); 
							    FileOutputStream fos = new FileOutputStream(convFile); 
							    fos.write(multipartFile.getBytes());
							    fos.close(); 
							
								System.out.println("Image Uploaded");
								//return "You successfully uploaded file" ;
							} catch (Exception e) {
								System.out.println("Image NOT Uploaded");
								//return "You failed to upload " ;
								mav.addObject("succMessage", "Contrctor Updated successfully. But Image NOT Uploaded");
								request.setAttribute("message","Contrctor Updated successfully. But Image NOT Uploaded");
								mav.setViewName("response");
								return mav;
							}
						}
					}//For Loop					
				} catch (Exception e) {
					mav.setViewName("response");	
					response = "Contractor Update Failed.";
					request.setAttribute("message1",response);
					e.printStackTrace();
				}
				//mav.setViewName("ShowAllContractors");
				return mav;
			}else if (buttonType.equals("Submit")) {
				System.out.println(buttonType);
				System.out.println();
				try {
					response = utilDao.doContractorRegistration(bean, siteId, user_id, request, session);
					request.setAttribute("message",response);
					mav.addObject("succMessage", response);		
					mav.addObject("ContractorBean", bean1);
					mav.setViewName("response");
					int k=0;
					
					for (int i = 0; i < files.length; i++) {
						MultipartFile multipartFile = files[i];
						if(!multipartFile.isEmpty()){
						try {
								String rootFilePath = UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH").toString();
								File dir = new File(rootFilePath+bean.getContractor_id().replace("/", "-"));
								System.out.println(bean.getContractor_id().replace("/", "-"));
								if (!dir.exists())
									dir.mkdirs();

								String filePath = dir.getAbsolutePath()
								+ File.separator + bean.getContractor_id().replace("/", "-")+"_Part"+k+".jpg"; 
								k++;
								File convFile = new File(filePath);
							    convFile.createNewFile(); 
							    FileOutputStream fos = new FileOutputStream(convFile); 
							    fos.write(multipartFile.getBytes());
							    fos.close(); 
							
								System.out.println("Image Uploaded");
								//return "You successfully uploaded file" ;
							} catch (Exception e) {
								System.out.println("Image NOT Uploaded");
								//return "You failed to upload " ;
								mav.addObject("succMessage", "Contrctor Saved successfully. But Image NOT Uploaded");
								request.setAttribute("message","Contrctor Saved successfully. But Image NOT Uploaded");
								mav.setViewName("response");
								return mav;
							}
						}
					}//For Loop

				} catch (Exception e) {
					e.printStackTrace();
					response = "Registration Failed.";
					request.setAttribute("message1",response);
					mav.setViewName("response");
				}
			} 

		} catch (Exception e) {
			e.printStackTrace();
			response = "Current Operation Failed. plz login again.";
			mav.setViewName("response");
		}
		return mav;
	}
	
	private boolean deleteContractorImage(HttpServletRequest request) {
		String filePath = "";
		String contractor_id=request.getParameter("contractor_id")==null?"":request.getParameter("contractor_id").replace("/", "-");
		 
		String isRequestFromController=request.getAttribute("isRequestFromController")==null?"":request.getAttribute("isRequestFromController").toString();
		if(isRequestFromController.equals("true")){
			filePath=request.getAttribute("imagePath")==null?"":request.getAttribute("imagePath").toString().replace("$", "/").trim();
		 }
		File  file=new File(filePath);
		//getting the extension type to delete the particular files
		String fileExtenstion=getFileExtension(file);
		//deleting the file
		boolean isFileDeleted=file.delete();
		String siteWiseNO=contractor_id;
		//loading the path of the images
		String rootFilePath = UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH").toString();
		
		File fileSource = null;
		File destinaiton = null;
		//if file is deleting then next file name is rename with the this deleted file
		//eg. if file part1.jpg is deleted then part2.jpg will rename into part1.jpg same like the next file name will rename
		for (int i = 0; i < 8; i++) {
			//this the current file name which is deleted (isFileDeleted variable )
			 fileSource = new File(rootFilePath+"/"+siteWiseNO+"/"+siteWiseNO+"_Part"+i+"."+fileExtenstion);
			if(!fileSource.exists()){
				destinaiton = new File(rootFilePath+"/"+siteWiseNO+"/"+siteWiseNO+"_Part"+(i+1)+"."+fileExtenstion);
				//if destination file exists then rename the file 
				 if(destinaiton.exists()){
					  if(destinaiton.renameTo(fileSource)){
						  System.out.println("File name \n"+destinaiton+" changed to \n"+fileSource);
				      }else{
				    	  System.out.println("File rename failed");
				      }
				 }
			}
		}
		
	  
		return isFileDeleted;
	}



	private String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return "";
    }



	/************************************************************* this is for both marketing and store both shown start******************************/
	
	@RequestMapping("/AddProducts.spring")
	public  ModelAndView addProducts(HttpServletRequest request,HttpSession session){  
		ModelAndView mav = new ModelAndView();
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));

		//List<ProductDetails> listProductsList = new ArrayList<ProductDetails>();
		try{

			List<Map<String,Object>> totalProductList = utilDao.getTotalProductsList(site_id1);
			System.out.println("ADD PRODUCT "+totalProductList.size());
			request.setAttribute("totalProductsList",totalProductList);
			request.setAttribute("AllProducts",true);
			request.setAttribute("Delete",true);

		}catch(Exception e){
			e.printStackTrace();
		}
		mav.setViewName("AddMarketingandStoreProduct");
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
	
			audit.auditLog("0",user_id,"Add Product Viewed","success",site_id1);
		
		return mav;  
	}  
	/******************************************this is applicable only when the director level login it is used start********************************/
	@RequestMapping(value="/AddSubProducts.spring")  
	public  ModelAndView AddSubProducts(HttpServletRequest request,HttpSession session){  
		ModelAndView mav = new ModelAndView();
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		List<Map<String,Object>> totalProductList = utilDao.getTotalProducts(site_id1);
		request.setAttribute("totalProductsList",totalProductList);
		request.setAttribute("AllProducts",true);
		mav.setViewName("AddSubProductMarketingandStore");
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		audit.auditLog("0",user_id,"AddSubProducts Viewed","success",site_id1);
			
		return mav;  
	} 

	/******************************************************this is applicalble for directory level login it is used in child product*************************/
	@RequestMapping(value="/AddChildProducts.spring")  
	public  ModelAndView AddChildProducts(HttpServletRequest request,HttpSession session){  
		ModelAndView mav = new ModelAndView();
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		List<Map<String,Object>> totalProductList = utilDao.getTotalProducts(sessionSite_id);
		request.setAttribute("totalProductsList",totalProductList);
		request.setAttribute("AllProducts",true);
		mav.setViewName("AddChildProductMarketingandStore");
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		audit.auditLog("0",user_id,"AddChildProducts Viewed","success",sessionSite_id);
				
		return mav;  
	}  
	
	/***********************************************View all products Details List start***************************************************/
	@RequestMapping("/getSiteRelatedProducts.spring")
	public @ResponseBody List<Map<String,Object>> getSiteRelatedProducts(HttpSession session,@RequestParam(value = "Product_Type") String Product_Type)  {
		//String searchproductList = "" ;
		List<Map<String,Object>> listProductsList=null;
		try{
			if(!Product_Type.equals("")){
				listProductsList = utilDao.getSiteProducts(Product_Type);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return listProductsList;
	}
	
	/****************************************************save all product details store and marketing **********************************************/
	@RequestMapping(value = "/savestoreandMarketingproduct.spring", method = RequestMethod.POST)
	public ModelAndView savestoreandMarketingproduct(HttpServletRequest request,HttpSession session) throws Exception {

		System.out.println("product controler");
		String strServiceType = "";
		String strProductName = "";
		String productType="";
		ModelAndView model = new ModelAndView();
		List<Map<String, Object>> totalProductList = null;
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		
		try {
			
			strServiceType = request.getParameter("ServiceType");
			int intProductId = dao.getSeqNo("combobox_Product");
			String strProductId = "PRD" + intProductId;

			System.out.println("strProduct name " + strProductName);
			System.out.println("strProduct name " + strProductId);
			int intCount = 0;
			productType=request.getParameter("Product_Type") == null ? "" : request.getParameter("Product_Type").toString();
			if(productType.equals("")){
				productType=request.getParameter("delete_Product_Type") == null ? "" : request.getParameter("delete_Product_Type").toString();
			}
			//if(!productType.equals("")){
				request.setAttribute("AllProducts",true);
				request.setAttribute("Delete",true);
			//}
			if (strServiceType.equals("add")) {

				strProductName = request.getParameter("product") == null ? "" : request.getParameter("product").toString();
				
				// this is used for user given already existed product given then check and show already existed at that time user again select add product so use it add product sub module
				
				int res = dao.checkProductNameAvailableOrNot(strProductName,site_id,productType);
				if (res == 0) {
					intCount = dao.saveproduct(strProductName, strProductId,productType,site_id);
					request.setAttribute("succMessage", "Product Name has been added successfully");
				} else {
					request.setAttribute("errMessage", "Product Name was already Added, Please try with Another Product Name");
				}
			} else if (strServiceType.equals("delete")) {

				String strProduct = request.getParameter("product_delete") == null ? request.getParameter("product_Delete") == null ? "" : request.getParameter("product_Delete").toString() : request.getParameter("product_delete").toString();
				if (strProduct.contains("@@")) {
					String productArr[] = strProduct.split("@@");
					if (productArr != null && productArr.length >= 1) {
						strProductId = productArr[0].trim();
						strProductName = productArr[1].trim();
					}
				}
				//dao.checkDeletedProductReceive();
				intCount = dao.deleteproduct(strProductId,site_id,productType);
				if (intCount > 0) {

					request.setAttribute("succMessage", "Product deleted successfully");
				} else {
					request.setAttribute("errMessage", "Can not Delete Product, As Product was used.");
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("AddMarketingandStoreProduct");
			totalProductList = utilDao.getTotalProducts(site_id);
			request.setAttribute("totalProductsList", totalProductList);
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"add product clicked submit","success",site_id1);
	
		return model;
	}
	/************************************************view all product details marketing and store start**************************************************/
	@RequestMapping("/viewaddedAllproduct.spring")
	public  @ResponseBody List<ProductDetails> viewaddedAllproduct(@RequestParam("AllProducts") String AllProducts,HttpSession session){

		System.out.println("get Total Products...");
		List<ProductDetails> listProductsList = new ArrayList<ProductDetails>();
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		try{
			listProductsList = dao.getTotalProducts(sessionSite_id,AllProducts);
			System.out.println("get Total Products..." + listProductsList.size()); 
		}catch(Exception e){
			e.printStackTrace();
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			
			audit.auditLog("0",user_id," Product Master Viewed All details","success",sessionSite_id);
		
		return listProductsList;
	}
	
	/*************************************************************get the auto complete data start**************************************************/
	@RequestMapping(value = "/ViewChildProductForAutoComplete", method = RequestMethod.GET)
	@ResponseBody
	public String ViewChildProductForAutoComplete(@RequestParam("childProduct") String childProduct) {

		String childProductName = dao.ViewChildProductForAutoComplete(childProduct);
		//System.out.println("In controller resulted data :"+empData+"|");
		return childProductName;
	}
	@RequestMapping(value = "/ViewandGetChildProduct", method = RequestMethod.GET)
	@ResponseBody
	public List<ProductDetails> ViewandGetChildProduct(@RequestParam("childProductName") String childProduct) {
		List<ProductDetails> listAllProductsList = new ArrayList<ProductDetails>();

		 listAllProductsList = dao.ViewandGetChildProduct(childProduct);
		//System.out.println("In controller resulted data :"+empData+"|");
		return listAllProductsList;
	}
	/**********************************only product start*********************************************************/
	@RequestMapping(value = "/ViewProductForAutoComplete", method = RequestMethod.GET)
	@ResponseBody
	public String ViewProductForAutoComplete(@RequestParam("Product") String Product,@RequestParam("Product_Type") String Product_Type) {

		String ProductName = dao.ViewProductForAutoComplete(Product,Product_Type);
		//System.out.println("In controller resulted data :"+empData+"|");
		return ProductName;
	}
	// this is for masters sub product start
	@RequestMapping(value = "/getSubProductsforAutoComplete", method = RequestMethod.GET)
	@ResponseBody
	public String getSubProductsforAutoComplete(@RequestParam("Product") String Product,@RequestParam("subprodId") String subProduct) {

		String ProductName = dao.getSubProductsforAutoComplete(Product,subProduct);
		//System.out.println("In controller resulted data :"+empData+"|");
		return ProductName;
	}
	// this is for giving child product and get measurement
	@RequestMapping(value = "/getmeasurementforAutoComplete", method = RequestMethod.GET)
	@ResponseBody
	public String getmeasurementforAutoComplete(@RequestParam("childProduct") String childProduct,@RequestParam("measurement") String measurement) {

		String measurementName = dao.getmeasurementforAutoComplete(childProduct,measurement);
		//System.out.println("In controller resulted data :"+empData+"|");
		return measurementName;
	}
	@RequestMapping(value = "/getProductforAutoComplete", method = RequestMethod.GET)
	@ResponseBody
	public String getProductforAutoComplete(@RequestParam("product") String Product,HttpSession session) {
		String sessionSite_id = String.valueOf(session.getAttribute("SiteId"));
		String productName = dao.getProductforAutoComplete(Product,sessionSite_id);
		//System.out.println("In controller resulted data :"+empData+"|");
		return productName;
	}
	/******************************************* TRASPORATOR REGISTRATION START***********************************************************************************/
	@RequestMapping(value = "/transportorRegistration.spring", method = RequestMethod.GET)
	public String transportorRegistration(Model model, HttpServletRequest request) {
		//System.out.println("MastersController.transportorRegistration()");
		logger.info("********************MastersController.transportorRegistration()**************************");
		TransportorBean bean = new TransportorBean();
		model.addAttribute("TransportorBean", bean);
		
		model.addAttribute("succMessage",request.getParameter("succMessage") == null ? " " : request.getParameter("succMessage"));
		
		return "TransportorRegistrationPage";
	}
	
	// getting the transportor details start*************************************
	
	@RequestMapping(value = "/getTransportorDetails.spring", method = RequestMethod.GET)
	public String getTransportorDetails(@RequestParam("transportorId") String transportorId, Model model,
			HttpServletRequest request) {
		int localport=request.getLocalPort();
		String rootFilePath="";
		try {
			if(localport==80){
				rootFilePath=UIProperties.validateParams.getProperty("TRANSPORTOR_LIVE_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("TRANSPORTOR_LIVE_IMAGE_PATH").toString();
			}else{
				rootFilePath=UIProperties.validateParams.getProperty("TRANSPORTOR_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("TRANSPORTOR_IMAGE_PATH").toString();
			}
			String isTransportorEditable=request.getParameter("isTransportorEditable")==null?"true":request.getParameter("isTransportorEditable");
			model.addAttribute("isContractorEditable", isTransportorEditable);
			List<TransportorBean> transportorBean = utilDao.getAllTransportorDetails(transportorId);
			TransportorBean bean = transportorBean.get(0);
			model.addAttribute("ContractorBean", bean);
			String conId=bean.getContractor_id().replace("/", "-");
			loadTransportorImgAndPdfFiles(rootFilePath,conId,model,request);
			//int count = 0;
			/*for(int i=0;i<8;i++){
				//String rootFilePath = UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH").toString();
				File dir = new File(rootFilePath+bean.getContractor_id().replace("/", "-"));
				File f = new File(rootFilePath+conId+"/"+conId+"_Part"+i+".jpg");
				if(f.exists()){
					count++;
					DataInputStream dis = new DataInputStream(new FileInputStream(f));
					byte[] barray = new byte[(int) f.length()];
					dis.readFully(barray); 
					byte[] encodeBase64 = Base64.encodeBase64(barray);
					String base64Encoded = new String(encodeBase64, "UTF-8");
					model.addAttribute("image"+i,base64Encoded);
				}
			}*/
			//request.setAttribute("imagecount", count);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "TransportorUpdatePage";
	}
	
	
	// save transportor details start***********************
	@RequestMapping(value = "/saveTransportorDetails.spring", method ={RequestMethod.POST,RequestMethod.GET})
	public ModelAndView saveTransportorDetail(@ModelAttribute("TransportorBean") TransportorBean transportorBean,@RequestParam(value="file",required = false) MultipartFile[] files,
		HttpServletRequest request, HttpSession session) {
		logger.info("********************MastersController.savetransportorDetails()**************************");
		//System.out.println("MastersController.saveContractorDetail()");
		ModelAndView mav = new ModelAndView();
		String response = "";
		int localport=request.getLocalPort();
		String rootFilePath ="";
		int imgCount=0;
		int pdfCount=0;
		String serverFile="";
		try {
			logger.info("********************MastersController.transportorRegistrationsave()**************************"+transportorBean);
			TransportorBean bean = (TransportorBean) transportorBean.clone();
			TransportorBean bean1 = new TransportorBean();
			
			if(bean.getFirst_name()==null&&bean.getLast_name()==null&&bean.getMobile_number()==null){
				mav.addObject("message1", "Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
				mav.setViewName("response");
				return mav;
			}

			String buttonType=request.getParameter("submitBtn")==null? request.getParameter("strSubmitBtn")==null?"":request.getParameter("strSubmitBtn"):request.getParameter("submitBtn");
			//String strButtonType=request.getParameter("strSubmitBtn")==null?"":request.getParameter("strSubmitBtn");
			
			String siteId = (String) CheckSessionValidation.validateUserSession(mav,session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId"));
			String user_id = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			if(localport==80){
				rootFilePath=UIProperties.validateParams.getProperty("TRANSPORTOR_LIVE_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("TRANSPORTOR_LIVE_IMAGE_PATH").toString();
			}else{
				rootFilePath=UIProperties.validateParams.getProperty("TRANSPORTOR_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("TRANSPORTOR_IMAGE_PATH").toString();
			}
			
			if(buttonType.equals("Delete")){
				System.out.println(buttonType);
				try {
					response=	utilDao.deleteTransportorDeteails(bean, siteId, user_id, request, session);
					mav.setViewName("response");
					List<TransportorBean> transportorBeans = utilDao.getAllTransportorDetails("");
					logger.debug("********************MastersController.updateContractorDetail()************"+transportorBeans);
					//System.out.println("MastersController.updateContractorDetail()" + transportorBeans);
					mav.addObject("transportorList", transportorBeans);
					mav.addObject("succMessage", response);
					request.setAttribute("message",response);
				//mav.setViewName("ShowAllContractors");
					mav.addObject("TransportorBean", bean1);
					//String rootFilePath = UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH").toString();
					File file=new File(rootFilePath);
					File[] file1=file.listFiles();
							for (File file2 : file1) {
								if(file2.equals(bean.getPan_number())){
									System.out.println("Got File "+file2.delete());
								}
							}
					File dir = new File(rootFilePath+bean.getPan_number());
				
					//System.out.println("File Deleted "+(dir.delete())+" Folder Name is "+bean.getPan_number());
				
				} catch (Exception e) {
					response="Failed to delete transporter.";
					request.setAttribute("message1",response);
					mav.setViewName("response");
				
				}
				return mav;
				
			}else if(buttonType.equals("Update")){
				logger.debug("********************MastersController.Type Of Operation************"+buttonType);
				//System.out.println(buttonType);
				try {
					mav.setViewName("response");	
					response = utilDao.updateTransportorDetails(bean, siteId, user_id, request, session);
					boolean status=deletettransportorPdfandImage(request);
					List<TransportorBean> transportorBeans = utilDao.getAllTransportorDetails("");
					logger.debug("********************MastersController.updatetransportorDetails()***********"+transportorBeans);
					//System.out.println("MastersController.updatetransportorDetails()" + transportorBeans);
					mav.addObject("contractorList", transportorBeans);
					mav.addObject("succMessage", response);
					mav.addObject("TransportorBean", bean1);
					request.setAttribute("message",response);
					int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent")); // here taking the active images presently taken 
					int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent")); // here taken the active pdf presently
					 imgCount=imagesAlreadyPresent;
					 pdfCount=pdfAlreadyPresent;
					
					for (int i =0; i < files.length; i++) {
						
						MultipartFile multipartFile = files[i];
						if(!multipartFile.isEmpty()){
						try {
							//file directory name is constructing here 
								File dir = new File(rootFilePath+bean.getContractor_id());
							//checking is the directory exits or not if not create the directory for string images
								if (!dir.exists())
									dir.mkdirs();
								
								String filePath ="";
								System.out.println(multipartFile.getOriginalFilename());
								//checking the extension of file and creating the file based on extension 
								if(multipartFile.getOriginalFilename().endsWith(".pdf")){
									filePath = dir.getAbsolutePath()+ File.separator + bean.getContractor_id()+"_Part"+pdfCount+".pdf";
									pdfCount++;
								}else{
									filePath = dir.getAbsolutePath()+ File.separator + bean.getContractor_id()+"_Part"+imgCount+".jpg";
									imgCount++;
								}
							
								//giving file path to file object
								File convFile = new File(filePath);
								//creating the image or pdf file 
							    convFile.createNewFile(); 
							    FileOutputStream fos = new FileOutputStream(convFile); 
							    fos.write(multipartFile.getBytes());
							    fos.close(); 
							
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("Image NOT Uploaded");
								//return "You failed to upload " ;
								//request.addAttribute("message1", "invoice updated Successfully. But Image NOT Uploaded");
								request.setAttribute("message1","invoice updated Successfully. But Image NOT Uploaded");
							}
						}
					}//For Loop
					/*int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent"));
					int k=imagesAlreadyPresent;
					for (int i = imagesAlreadyPresent; i < files.length; i++) {
						MultipartFile multipartFile = files[i];
						if(!multipartFile.isEmpty()){
						try {
								//String rootFilePath = UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH").toString();
								File dir = new File(rootFilePath+bean.getContractor_id().replace("/", "-"));
								if (!dir.exists())
									dir.mkdirs();

								String filePath = dir.getAbsolutePath()
								+ File.separator + bean.getContractor_id().replace("/", "-")+"_Part"+k+".jpg"; 
								k++;
								File convFile = new File(filePath);
							    convFile.createNewFile(); 
							    FileOutputStream fos = new FileOutputStream(convFile); 
							    fos.write(multipartFile.getBytes());
							    fos.close(); 
							
								System.out.println("Image Uploaded");
								//return "You successfully uploaded file" ;
							} catch (Exception e) {
								System.out.println("Image NOT Uploaded");
								//return "You failed to upload " ;
								mav.addObject("succMessage", "Transporotor Updated successfully. But Image NOT Uploaded");
								request.setAttribute("message","Transporotor Updated successfully. But Image NOT Uploaded");
								mav.setViewName("response");
								return mav;
							}
						}
					}//For Loop					
*/				} catch (Exception e) {
					mav.setViewName("response");	
					response = "transporter Update Failed.";
					request.setAttribute("message1",response);
					e.printStackTrace();
				}
				//mav.setViewName("ShowAllContractors");
				return mav;
			}else if (buttonType.equals("Submit")) {
				logger.debug("********************MastersController.click on submit button***********"+buttonType);
				//System.out.println(buttonType);
				//System.out.println();
				try {
					response = utilDao.doTransportorRegistration(bean, siteId, user_id, request, session);
					request.setAttribute("message",response);
					mav.addObject("succMessage", response);		
					mav.addObject("TransportorBean", bean1);
					mav.setViewName("response");
					int k=0;
					
					for (int i = 0; i < files.length; i++) {
						MultipartFile multipartFile = files[i];
						if(!multipartFile.isEmpty()){
						try {
								//String rootFilePath = UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("CONTRACTOR_INAGE_PATH").toString();
								File dir = new File(rootFilePath+bean.getContractor_id());//.replace("/", "-")
								//System.out.println(bean.getContractor_id().replace("/", "-"));
								if (!dir.exists())
									dir.mkdirs();

								if (multipartFile.getOriginalFilename().endsWith(".pdf")) {
									serverFile = (dir.getAbsolutePath() + File.separator+ bean.getContractor_id() + "_Part" + pdfCount + ".pdf");
									pdfCount++;
								} else {
									serverFile = dir.getAbsolutePath() + File.separator+ bean.getContractor_id() + "_Part" + imgCount + ".jpg";
									//file.transferTo(new File(serverFile));
									imgCount++;
								}
								File convFile = new File(serverFile);
								convFile.createNewFile();
								FileOutputStream fos = new FileOutputStream(convFile);
								fos.write(multipartFile.getBytes());
								fos.close();
								/*String filePath = dir.getAbsolutePath()
								+ File.separator + bean.getContractor_id().replace("/", "-")+"_Part"+k+".jpg"; 
								k++;
								File convFile = new File(filePath);
							    convFile.createNewFile(); 
							    FileOutputStream fos = new FileOutputStream(convFile); 
							    fos.write(multipartFile.getBytes());
							    fos.close(); */
							
								System.out.println("Image Uploaded");
								//return "You successfully uploaded file" ;
							} catch (Exception e) {
								System.out.println("Image NOT Uploaded");
								//return "You failed to upload " ;
								mav.addObject("succMessage", "Transporter Saved successfully. But Image NOT Uploaded");
								request.setAttribute("message","Transporter Saved successfully. But Image NOT Uploaded");
								mav.setViewName("response");
								return mav;
							}
						}
					}//For Loop

				} catch (Exception e) {
					e.printStackTrace();
					response = "Registration Failed.";
					request.setAttribute("message1",response);
					mav.setViewName("response");
				}
			} 

		} catch (Exception e) {
			e.printStackTrace();
			response = "Current Operation Failed. plz login again.";
			mav.setViewName("response");
		}
		return mav;
	}
	/*********************************** only view the Transportor details start******************************************************/
	@RequestMapping(value = "/viewTransportors.spring", method = RequestMethod.GET)
	public String viewTransportors(Model model, HttpServletRequest request) {
		try {
			List<TransportorBean> transportorBeans = utilDao.getAllTransportorDetails("");
			logger.debug("*********************MastersController.viewTransportorDetail()****************"+transportorBeans);
			//System.out.println("MastersController.updateContractorDetail()" +transportorBeans);
			model.addAttribute("transportorList", transportorBeans);
			model.addAttribute("isTransportorEditable", false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ShowAllTransportors";
		}
	/*********************************** only view the Transportor details End******************************************************/

	@RequestMapping(value = "/updateTransportorDetails.spring", method = RequestMethod.GET)
	public String updateTransportorDetails(Model model, HttpServletRequest request) {
		try {
			String isContractorEditable=request.getParameter("isTransportorEditable")==null?"true":request.getParameter("isTransportorEditable");
			List<TransportorBean> transportorBeans = utilDao.getAllTransportorDetails("");
			logger.debug("*********************MastersController.viewTransportorDetail()****************"+transportorBeans);
			//System.out.println("MastersController.updateContractorDetail()" + contractorBeans);
			
			model.addAttribute("transportorList", transportorBeans);
			model.addAttribute("isTransportorEditable", isContractorEditable);
			model.addAttribute("succMessage",request.getParameter("succMessage") == null ? " " : request.getParameter("succMessage"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "ShowAllTransportors";
	}
	/**********************************************AJAX CALL FOR CHECKING THE TRANSPORTOR DATA START******************************************/

	@RequestMapping(value = "/validatTransportorname.spring", method = RequestMethod.GET)
	public @ResponseBody boolean validatTransportorname(HttpSession session,HttpServletRequest request){
		String first_name=request.getParameter("first_name")==null?"":request.getParameter("first_name");
		String last_name=request.getParameter("last_name")==null?"":request.getParameter("last_name");
		String condition=request.getParameter("condition")==null?"":request.getParameter("condition");
		String contractor_id=request.getParameter("contractor_id")==null?"":request.getParameter("contractor_id");
		boolean flag= utilDao.validatTransportorname(first_name.trim(),last_name.trim(),condition.trim(),contractor_id.trim());
		return flag;
		
	}
	/************************************ VALIDATE PAN NUMBER START********************************************************/
	@RequestMapping(value = "/validatTransPortorpanNumber.spring", method = RequestMethod.GET)
	public @ResponseBody boolean validatTransPortorMobileNumber(Model model,	@RequestParam("panNumber") String panNumber,@RequestParam("condition") String condition,HttpServletRequest  request) {
		String contractorId=request.getParameter("contractor_id")==null?"":request.getParameter("contractor_id");
		System.out.println("MastersController.validatContractorMobileNumber() " + panNumber+" "+condition+" "+contractorId);
		model.addAttribute("ContractorBean", new ContractorBean());
		model.addAttribute("succMessage", " ");
		boolean resp = utilDao.validateTransportorPanNumber(panNumber.trim(),condition,contractorId);
		return resp;
	}
	
	/******************************************************to get the images and pdf for show purpose **************************************************/
	public static void loadTransportorImgAndPdfFiles(String rootFilePath,String transportorId,Model model, HttpServletRequest request){
		try{
		int imageCount = 0;
		int pdfCount=0;
	
		DataInputStream dis=null;
	
		int getLocalPort=request.getLocalPort();
		String strContextAndPort = "";
		String path = "";
		if(getLocalPort == 8080){ //Local
			strContextAndPort = UIProperties.validateParams.getProperty("TRANSPORT_PDFIMG_PATH_UAT");
		}else if(getLocalPort == 8078){ //local machine
			strContextAndPort = UIProperties.validateParams.getProperty("TRANSPORT_PDFIMG_PATH_UAT");
		}else if(getLocalPort == 8079){ //CUG
			strContextAndPort = UIProperties.validateParams.getProperty("TRANSPORT_PDFIMG_PATH_CUG");
		}else if(getLocalPort == 80){ //LIVE
			strContextAndPort = UIProperties.validateParams.getProperty("TRANSPORT_PDFIMG_PATH_LIVE");
		}

		//po_Number = po_Number.replace("/","$"); // if the invoice number contain the '/' then it will replacxe with $$
		
		for(int i=0;i<8;i++){
			File dir = new File(rootFilePath+transportorId);
			
			File imageFile = new File(rootFilePath+transportorId+"/"+transportorId+"_Part"+i+".jpg");
			//String pdfFileName=rootFilePath+siteId+"\\"+siteWiseNO+"/"+siteWiseNO+"_Part"+i+".pdf";
			File pdfFile = new File(rootFilePath+transportorId+"/"+transportorId+"_Part"+i+".pdf");
			
			//if pdf is exists the pdf will load and converted into the Base64 object and it will add in model object so we can carry this images and pdf so we can show in view page
			if(pdfFile.exists()){
				pdfCount++;
				try {
					String pathForDeleteFile = pdfFile.getAbsolutePath().replace("\\", "@@"); // here in jsp delete function call time // not taken so replace to @@
					path=strContextAndPort+transportorId+"/"+transportorId+"_Part"+i+".pdf";
					//adding pdf base64 format into model attribute so we can show the image in view and can download it
					model.addAttribute("pdf" + i, path);
					//adding the path of the pdf so if user deleting the pdf by path they can delete
					model.addAttribute("PathdeletePdf" + i, pathForDeleteFile);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			
			}
		
			if(imageFile.exists()){
				imageCount++;
				try {
					String pathForDeleteFile = imageFile.getAbsolutePath().replace("\\", "@@");
					path=strContextAndPort+transportorId+"/"+transportorId+"_Part"+i+".jpg";
					model.addAttribute("image" + i, path);
					//adding the path of the image so if user deleting the image or pdf by path they can delete
					model.addAttribute("delete" + i, pathForDeleteFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
		}
		//adding the image count in request object so we can control the images update file type in view while updating temporary or permanent work order
		request.setAttribute("imagecount", imageCount);	
		request.setAttribute("pdfcount", pdfCount);	
		System.out.print("images count"+imageCount);
		System.out.print("pdf count"+pdfCount);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*********************************************** DELETE THE IMAGES AND PDFS **************************************************/
	/************************************************************this is used to delete and add the images and pdf start**********************************/
	/**
	 * @description this method is used for delete the images by using the path of image
	 * 
	 */
		@RequestMapping(value = "/deletettransportorPdfandImage.spring", method = RequestMethod.GET)
		public @ResponseBody static boolean deletettransportorPdfandImage( HttpServletRequest request) {
			
			String filePath="";
			String PdfPathtodelete[]=request.getParameterValues("PdfPathtodelete");
			String imgPathtoDelete[]=request.getParameterValues("imgPathtoDelete");
			File fileSource = null;
			File destinaiton = null;
			// only images deleted and sort the images numbers i.e img0,1,2 like if u delete middle then already existed sort and new one is added
			if(imgPathtoDelete!=null ){
			for(int k=0;k<imgPathtoDelete.length;k++){
				if(imgPathtoDelete[k]!= null && !imgPathtoDelete[k].equals("")){
					filePath=imgPathtoDelete[k].replace("@@","\\");  // here replace with // instead of @@
					File  file=new File(filePath);

					String fileExtenstion=WorkOrderController.getFileExtension(file); // to get the extension to check the pdf and image
					String imgPdf=filePath.split("_Part")[0];

					boolean isFileDeleted=file.delete();
					//if file is deleting then next file name is rename with the this deleted file
					//eg. if file part1.jpg is deleted then part2.jpg will renaem into part1.jpg same like the next file name will rename

					for (int i = 0; i < 8; i++) {
						//this the current file name which is deleted (isFileDeleted variable )
						fileSource = new File(imgPdf+"_Part"+i+"."+fileExtenstion);
						if(i!=0&&!fileSource.exists()){
							destinaiton = new File(imgPdf+"_Part"+(i+1)+"."+fileExtenstion);
							//if destination file exists then rename the file 
							if(destinaiton.exists()){ // rename the already existed file name
								if(destinaiton.renameTo(fileSource)){
									System.out.println("File rename success Destination is \n"+destinaiton+" Dource is \n"+fileSource);
								}else{
									System.out.println("File rename failed");
								}
							}
						}else if(!fileSource.exists()&&i==0){
							destinaiton = new File(imgPdf+"_Part"+(i+1)+"."+fileExtenstion);
							if(destinaiton.exists())
								if(destinaiton.renameTo(fileSource)){
									System.out.println("File rename success \n"+destinaiton+" \n"+fileSource);
								}else{
									System.out.println("File rename failed");
								}
						}
					}
				}
			}
			}
			/**********************************************pdf delete purpose start********************************************************/
			// only pdfs deleted and sort the images numbers i.e img0,1,2 like if u delete middle then already existed sort and new one is added
			if(PdfPathtodelete!=null ){
			for(int k=0;k<PdfPathtodelete.length;k++){
				if(PdfPathtodelete[k]!= null && !PdfPathtodelete[k].equals("")){
					filePath=PdfPathtodelete[k].replace("@@","\\"); // here replace with // instead of @@
					File  file=new File(filePath);

					String fileExtenstion=WorkOrderController.getFileExtension(file); // to get the extension to check the pdf and image
					String imgPdf=filePath.split("_Part")[0];
					boolean isFileDeleted=file.delete();
					//if file is deleting then next file name is rename with the this deleted file
					//eg. if file part1.jpg is deleted then part2.jpg will renaem into part1.jpg same like the next file name will rename

					for (int i = 0; i < 8; i++) {
						//this the current file name which is deleted (isFileDeleted variable )
						fileSource = new File(imgPdf+"_Part"+i+"."+fileExtenstion);
						if(i!=0&&!fileSource.exists()){
							destinaiton = new File(imgPdf+"_Part"+(i+1)+"."+fileExtenstion);
							//if destination file exists then rename the file 
							if(destinaiton.exists()){ // rename the already existed file name
								if(destinaiton.renameTo(fileSource)){
									System.out.println("File rename success Destination is \n"+destinaiton+" Dource is \n"+fileSource);
								}else{
									System.out.println("File rename failed");
								}
							}
						}else if(!fileSource.exists()&&i==0){
							destinaiton = new File(imgPdf+"_Part"+(i+1)+"."+fileExtenstion);
							if(destinaiton.exists())
								if(destinaiton.renameTo(fileSource)){
									System.out.println("File rename success \n"+destinaiton+" \n"+fileSource);
								}else{
									System.out.println("File rename failed");
								}
						}
					}
				}
			}
			}
			return true;
		}
	
	
}
