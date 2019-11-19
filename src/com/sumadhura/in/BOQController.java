package com.sumadhura.in;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.sumadhura.bean.BOQBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;
import com.sumadhura.dto.MultiObject;
import com.sumadhura.service.BOQService;
import com.sumadhura.service.ContractorPaymentProcessService;
import com.sumadhura.service.WorkOrderService;
import com.sumadhura.transdao.PaymentProcessDaoImpl;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
public class BOQController extends UIProperties {
	
	@Autowired
	BOQService objBOQService;
	@Autowired
	@Qualifier("workControllerService")
	WorkOrderService workControllerService;
	
	/**************************************************** BOQ upload - start ************************************************/
	@RequestMapping(value = "/uploadBOQExcel.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView uploadBOQExcel( HttpServletRequest request,HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.addObject("pageHighlightURL","uploadBOQExcel.spring");
		model.setViewName("boq/UploadBOQExcel");
		return model;
	}
	@RequestMapping(value = "/uploadSiteWiseBOQExcel.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView uploadSiteWiseBOQExcel( HttpServletRequest request,HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.addObject("pageHighlightURL","uploadSiteWiseBOQExcel.spring");
		model.addObject("showSiteSelection",true);
		model.setViewName("boq/UploadBOQExcel");
		return model;
	}
	@RequestMapping(value = "/SaveBOQExcelData", method = RequestMethod.POST)
	public ModelAndView SaveBOQExcelData(  HttpServletRequest request, HttpSession session, @RequestParam("file") MultipartFile[] files,  RedirectAttributes redir) throws IllegalStateException, IOException {
		ModelAndView model = new ModelAndView();
		model.addObject("pageHighlightURL",request.getParameter("pageHighlightURL"));
		redir.addFlashAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		model.addObject("isNotRefeshed", "true");
		String boqSiteId = null;
		String boqSiteName = null;
		String session_site_id = session.getAttribute("SiteId").toString();
		String session_siteName = session.getAttribute("SiteName").toString();
		String dropdown_SiteId = request.getParameter("dropdown_SiteId");
		if(dropdown_SiteId==null){
			boqSiteId = session_site_id;
			boqSiteName = session_siteName;
		}
		else{
			model.addObject("showSiteSelection",true);
			if(StringUtils.isBlank(dropdown_SiteId)){
				model.setViewName("boq/UploadBOQExcel");
				model.addObject("displayErrMsg", "Please select Site.");
				return model;
			}
			boqSiteId = dropdown_SiteId;
			boqSiteName = objBOQService.getSiteNameBySiteId(boqSiteId);
		}
		String user_id = session.getAttribute("UserId").toString();
		String response = "";
		List<String> errMsg = new ArrayList<String>();
		
		
		for (int index = 0; index < files.length; index++) {
			MultipartFile multipartFile = files[index];
			
			if(!multipartFile.isEmpty()){
				if(multipartFile.getOriginalFilename().endsWith("xlsx")){
					List<String> tempBOQNoForController = new ArrayList<String>();
					response = objBOQService.saveBOQ(multipartFile,errMsg,user_id,boqSiteId,tempBOQNoForController);
					//response="Success";tempBOQNoForController=143;
					// Save Excel file in Server - start
					if (response.equalsIgnoreCase("Success")) {
						try {

							int portNumber=request.getLocalPort();
					        String rootFilePath = null;
					        if(portNumber==80){
					        	rootFilePath = validateParams.getProperty("BOQ_EXCEL_PATH") == null ? "" : validateParams.getProperty("BOQ_EXCEL_PATH").toString();
					        }
					        else{
					        	rootFilePath = validateParams.getProperty("BOQ_EXCEL_PATH_DEV") == null ? "" : validateParams.getProperty("BOQ_EXCEL_PATH_DEV").toString();
					        }
					        File dir = new File(rootFilePath+boqSiteId+"//temp");
							if (!dir.exists())
								dir.mkdirs();

							String filePath = dir.getAbsolutePath()
							+ File.separator +"BOQ_"+ boqSiteName+"_"+tempBOQNoForController.get(0)+".xlsx"; 
							
							multipartFile.transferTo(new File(filePath));


							System.out.println("BOQ Uploaded");
							//return "You successfully uploaded file" ;
						} catch (Exception e) {
							System.out.println("BOQ NOT Uploaded");
							//return "You failed to upload " ;
						}
					} 
					// Save Excel file in Server - end
					//model.setViewName("boq/UploadBOQResponse");
					model = new ModelAndView("redirect:/UploadBOQResponse.spring");
					//model.addObject("isNotRefeshed", "true");
					//model.addObject("response", response);
					//model.addObject("errMsg", errMsg);
					redir.addFlashAttribute("isNotRefeshed", "true");
					redir.addFlashAttribute("response", response);
					redir.addFlashAttribute("errMsg", errMsg);
					
					
						
					
				}
				else{
					model.setViewName("boq/UploadBOQExcel");
					model.addObject("displayErrMsg", "Upload Excel File of type (.xlsx)");
				}
			}
			else{
				model.setViewName("boq/UploadBOQExcel");
				model.addObject("displayErrMsg", "Please Upload Excel File.");
			}

		}
		
		return model;
	}
	/***************************************************** BOQ upload - end ***************************************************/
	@RequestMapping(value = "/UploadBOQResponse.spring",  method = RequestMethod.GET)
	public ModelAndView UploadBOQResponse(){
	    return new ModelAndView("boq/UploadBOQResponse");
	}
	/*********************************************** NMR BOQ upload - start **************************************************/
	@RequestMapping(value = "/uploadNMR.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView uploadNMRBOQ( HttpServletRequest request,HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.addObject("pageHighlightURL","uploadNMR.spring");
		model.setViewName("boq/UploadNMR");
		return model;
	}
	@RequestMapping(value = "/uploadSiteWiseNMR.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView uploadSiteWiseNMRBOQ( HttpServletRequest request,HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.addObject("pageHighlightURL","uploadSiteWiseNMR.spring");
		model.addObject("showSiteSelection",true);
		model.setViewName("boq/UploadNMR");
		return model;
	}
	@RequestMapping(value = "/SaveNMR", method = RequestMethod.POST)
	public ModelAndView SaveNMR(  HttpServletRequest request, HttpSession session, @RequestParam("file") MultipartFile[] files,  RedirectAttributes redir) throws IllegalStateException, IOException {
		ModelAndView model = new ModelAndView();
		model.addObject("pageHighlightURL",request.getParameter("pageHighlightURL"));
		redir.addFlashAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		String boqSiteId = null;
		String boqSiteName = null;
		String session_site_id = session.getAttribute("SiteId").toString();
		String session_siteName = session.getAttribute("SiteName").toString();
		String dropdown_SiteId = request.getParameter("dropdown_SiteId");
		if(dropdown_SiteId==null){
			boqSiteId = session_site_id;
			boqSiteName = session_siteName;
		}
		else{
			model.addObject("showSiteSelection",true);
			if(StringUtils.isBlank(dropdown_SiteId)){
				model.setViewName("boq/UploadNMR");
				model.addObject("displayErrMsg", "Please select Site.");
				return model;
			}
			boqSiteId = dropdown_SiteId;
			boqSiteName = objBOQService.getSiteNameBySiteId(boqSiteId);
		}
		String user_id = session.getAttribute("UserId").toString();
		String response = "";
		List<String> errMsg = new ArrayList<String>();
		
		
		for (int index = 0; index < files.length; index++) {
			MultipartFile multipartFile = files[index];
			
			if(!multipartFile.isEmpty()){
				if(multipartFile.getOriginalFilename().endsWith("xlsx")){
					List<String> tempBOQNoForController = new ArrayList<String>();
					response = objBOQService.saveNMR(multipartFile,errMsg,user_id,boqSiteId,tempBOQNoForController);
					//response="Success";tempBOQNoForController=143;
					// Save Excel file in Server - start
					if (response.equalsIgnoreCase("Success")) {
						try {

							int portNumber=request.getLocalPort();
					        String rootFilePath = null;
					        if(portNumber==80){
					        	rootFilePath = validateParams.getProperty("BOQ_EXCEL_PATH") == null ? "" : validateParams.getProperty("BOQ_EXCEL_PATH").toString();
					        }
					        else{
					        	rootFilePath = validateParams.getProperty("BOQ_EXCEL_PATH_DEV") == null ? "" : validateParams.getProperty("BOQ_EXCEL_PATH_DEV").toString();
					        }
					        File dir = new File(rootFilePath+boqSiteId+"//temp");
							if (!dir.exists())
								dir.mkdirs();

							String filePath = dir.getAbsolutePath()
							+ File.separator +"BOQ_"+ boqSiteName+"_"+tempBOQNoForController.get(0)+".xlsx"; 
							
							multipartFile.transferTo(new File(filePath));


							System.out.println("NMR Uploaded");
							//return "You successfully uploaded file" ;
						} catch (Exception e) {
							System.out.println("NMR NOT Uploaded");
							//return "You failed to upload " ;
						}
					} 
					// Save Excel file in Server - end
					model = new ModelAndView("redirect:/UploadBOQResponse.spring");
					//model.setViewName("boq/UploadBOQResponse");
					//model.addObject("response", response);
					//model.addObject("errMsg", errMsg);
					redir.addFlashAttribute("isNotRefeshed", "true");
					redir.addFlashAttribute("response", response);
					redir.addFlashAttribute("errMsg", errMsg);
					
				}
				else{
					model.setViewName("boq/UploadNMR");
					model.addObject("displayErrMsg", "Upload Excel File of type (.xlsx)");
				}
			}
			else{
				model.setViewName("boq/UploadNMR");
				model.addObject("displayErrMsg", "Please Upload Excel File.");
			}

		}
		
		return model;
	}
	/************************************************ NMR BOQ upload - end *************************************************/
	
	@RequestMapping(value = "/getPendingForApprovalBOQList.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String getPendingForApprovalBOQList( Model model,HttpServletRequest request,HttpSession session) {
		
		List<BOQBean> listofPendingBOQForApproval = null;
		try {
			listofPendingBOQForApproval = objBOQService.getPendingForApprovalBOQList(request,session,false);
			model.addAttribute("listofPendingBOQForApproval",listofPendingBOQForApproval);
			model.addAttribute("pageHighlightURL","getPendingForApprovalBOQList.spring");
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "boq/listPendingBOQForApproval";
	}
	@RequestMapping(value = "/viewTempBOQ.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String viewTempBOQ( Model model,HttpServletRequest request,HttpSession session) {
		String pageHighlightURL=request.getParameter("pageHighlightURL");
		if(pageHighlightURL!=null){model.addAttribute("pageHighlightURL",pageHighlightURL);}
		else{model.addAttribute("pageHighlightURL","viewTempBOQ.spring");}
		boolean isViewTempBoq = true;
		List<BOQBean> listofPendingBOQForApproval = null;
		try {
			listofPendingBOQForApproval = objBOQService.getPendingForApprovalBOQList(request,session,isViewTempBoq);
			model.addAttribute("listofPendingBOQForApproval",listofPendingBOQForApproval);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "boq/listTempBOQForView";
	}
	@RequestMapping(value = "/getSiteWisePendingForApprovalBOQList.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String getSiteWisePendingForApprovalBOQList( Model model,HttpServletRequest request,HttpSession session) {
		model.addAttribute("showSiteSelection",true);
		String pageHighlightURL=request.getParameter("pageHighlightURL");
		if(pageHighlightURL!=null){model.addAttribute("pageHighlightURL",pageHighlightURL);}
		else{model.addAttribute("pageHighlightURL","getSiteWisePendingForApprovalBOQList.spring");}
		String siteId = request.getParameter("dropdown_SiteId");
		List<BOQBean> listofPendingBOQForApproval = null;
		try {
			listofPendingBOQForApproval = objBOQService.getSiteWisePendingForApprovalBOQList(request,session,siteId,false);
			model.addAttribute("listofPendingBOQForApproval",listofPendingBOQForApproval);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "boq/listPendingBOQForApproval";
	}
	@RequestMapping(value = "/viewSiteWiseTempBOQ.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String viewSiteWiseTempBOQ( Model model,HttpServletRequest request,HttpSession session) {
		boolean isViewTempBoq = true;
		String pageHighlightURL=request.getParameter("pageHighlightURL");
		if(pageHighlightURL!=null){model.addAttribute("pageHighlightURL",pageHighlightURL);}
		else{model.addAttribute("pageHighlightURL","viewSiteWiseTempBOQ.spring");}
		model.addAttribute("showSiteSelection",true);
		String siteId = request.getParameter("dropdown_SiteId");
		List<BOQBean> listofPendingBOQForApproval = null;
		try {
			listofPendingBOQForApproval = objBOQService.getSiteWisePendingForApprovalBOQList(request,session,siteId,isViewTempBoq);
			model.addAttribute("listofPendingBOQForApproval",listofPendingBOQForApproval);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return "boq/listTempBOQForView";
	}
	@RequestMapping(value = "/getPendingForApprovalBOQDetails.spring", method = RequestMethod.GET)
	public String ViewMyRaisedIndentsDetails(Model model, HttpServletRequest request,HttpSession session) {
		BOQBean objBOQBean = new BOQBean();
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		try {
			String boqType = request.getParameter("boqType");
			if(!boqType.equals("NORMAL")){
				throw new RuntimeException("Not a Normal Boq");
			}
			
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String boqSiteId = request.getParameter("siteId");
			objBOQBean.setStrSiteId(boqSiteId);
			String	viewTempBoq = request.getParameter("viewTempBoq") == null ? "" : request.getParameter("viewTempBoq");
			model.addAttribute("viewTempBoq", viewTempBoq);
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			objBOQBean=objBOQService.getBOQFromAndToDetails(objBOQBean,userId);
			if (objBOQBean.getStrApproverEmpId() == null) {
				objBOQBean.setStrApproverEmpId("END");
				System.out.println(objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpId", "END");
				model.addAttribute("nextLevelApproveEmpName", "END");
				
			}else{
				model.addAttribute("nextLevelApproveEmpId", objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpName", objBOQBean.getStrBoqTo());
				
			}
			model.addAttribute("lowerLevelEmpId", objBOQBean.getStrLowerEmpId());
			model.addAttribute("lowerLevelEmpName", objBOQBean.getStrBoqFrom());
			
			String note=objBOQService.getBOQLevelComments(request);
			
			List<BOQBean> mainList= objBOQService.getBOQData(request,session);
			model.addAttribute("boqData", mainList);//means the list on top of the table
		//	List<BOQBean> subList=  objBOQService.getBOQForApprovalByID(request,session);
		//  model.addAttribute("subList", subList);//means the list in the table this is the table data
			
			/*List<BOQBean> tempBOQWorkDeatils=	objBOQService.getBOQWorkDetails(request,session,mainList.get(0).getTypeOfWork());
			model.addAttribute("boqWorkDeatils", tempBOQWorkDeatils);//means the list in the table this is the table data
			*/
			model.addAttribute("BOQLevelCommentsList", note);
			
			model.addAttribute("BOQBean", objBOQBean);
			System.out.println(objBOQBean.getStrBoqFrom()+""+objBOQBean.getStrBoqTo());
			model.addAttribute("BOQRequestTo", objBOQBean.getStrBoqTo());
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		
			Map<String, String> WO_QSList = workControllerService.loadQSProducts(siteId,mainList.get(0).getTypeOfWork());
			model.addAttribute("productsMap", WO_QSList);
			
			List<Map<String, Object>> totalProductsList = objBOQService.getTempBOQMajorHeads(request,mainList.get(0).getTypeOfWork());
			model.addAttribute("totalProductsList",totalProductsList);//it is for dropDown major head list
			
			List<BOQBean> majorHeadsDetails = objBOQService.getTempBOQMajorHeadsDetails(request,mainList.get(0).getTypeOfWork());
			model.addAttribute("majorHeadsDetails",majorHeadsDetails);
			
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "boq/ApproveTempBOQPage";
	}
	
	@RequestMapping(value = "/getTempMinorHeads.spring", method = RequestMethod.GET)
	public String getTempMinorHeads(Model model, HttpServletRequest request,HttpSession session) {
		BOQBean objBOQBean = new BOQBean();
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		try {
			String boqType = request.getParameter("boqType");
			if(!boqType.equals("NORMAL")){
				throw new RuntimeException("Not a Normal Boq");
			}
			
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String boqSiteId = request.getParameter("siteId");
			objBOQBean.setStrSiteId(boqSiteId);
			String	viewTempBoq = request.getParameter("viewTempBoq") == null ? "" : request.getParameter("viewTempBoq");
			model.addAttribute("viewTempBoq", viewTempBoq);
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			objBOQBean=objBOQService.getBOQFromAndToDetails(objBOQBean,userId);
			if (objBOQBean.getStrApproverEmpId() == null) {
				objBOQBean.setStrApproverEmpId("END");
				System.out.println(objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpId", "END");
				model.addAttribute("nextLevelApproveEmpName", "END");
				
			}else{
				model.addAttribute("nextLevelApproveEmpId", objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpName", objBOQBean.getStrBoqTo());
				
			}
			model.addAttribute("lowerLevelEmpId", objBOQBean.getStrLowerEmpId());
			model.addAttribute("lowerLevelEmpName", objBOQBean.getStrBoqFrom());
			
			String note=objBOQService.getBOQLevelComments(request);
			
			List<BOQBean> mainList= objBOQService.getBOQData(request,session);
			model.addAttribute("boqData", mainList);//means the list on top of the table
		//	List<BOQBean> subList=  objBOQService.getBOQForApprovalByID(request,session);
		//  model.addAttribute("subList", subList);//means the list in the table this is the table data
			
			/*List<BOQBean> tempBOQWorkDeatils=	objBOQService.getBOQWorkDetails(request,session,mainList.get(0).getTypeOfWork());
			model.addAttribute("boqWorkDeatils", tempBOQWorkDeatils);//means the list in the table this is the table data
			*/
			model.addAttribute("BOQLevelCommentsList", note);
			
			model.addAttribute("BOQBean", objBOQBean);
			System.out.println(objBOQBean.getStrBoqFrom()+""+objBOQBean.getStrBoqTo());
			model.addAttribute("BOQRequestTo", objBOQBean.getStrBoqTo());
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		
			Map<String, String> WO_QSList = workControllerService.loadQSProducts(siteId,mainList.get(0).getTypeOfWork());
			model.addAttribute("productsMap", WO_QSList);
			
			List<Map<String, Object>> totalProductsList = objBOQService.getTempBOQMajorHeads(request,mainList.get(0).getTypeOfWork());
			model.addAttribute("totalProductsList",totalProductsList);//it is for dropDown major head list
			
			List<BOQBean> minorHeadsDetails = objBOQService.getTempBOQMinorHeadsDetails(request,mainList.get(0).getTypeOfWork());
			model.addAttribute("minorHeadsDetails",minorHeadsDetails);
			
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "boq/ListTempMinorHeads";
	}
	
	@RequestMapping(value = "/getTempBOQDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String getTempBOQDetails(Model model, HttpServletRequest request,HttpSession session) {
		BOQBean objBOQBean = new BOQBean();
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		try {
			String boqType = request.getParameter("boqType");
			if(!boqType.equals("NORMAL")){
				throw new RuntimeException("Not a Normal Boq");
			}
			
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String boqSiteId = request.getParameter("siteId");
			objBOQBean.setStrSiteId(boqSiteId);
			String	viewTempBoq = request.getParameter("viewTempBoq") == null ? "" : request.getParameter("viewTempBoq");
			model.addAttribute("viewTempBoq", viewTempBoq);
			int tempBOQNo =  Integer.parseInt(request.getParameter("tempBOQNo"));
			model.addAttribute("tempBOQNo", tempBOQNo);
			model.addAttribute("boqSiteId", boqSiteId);
			
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			objBOQBean=objBOQService.getBOQFromAndToDetails(objBOQBean,userId);
			if (objBOQBean.getStrApproverEmpId() == null) {
				objBOQBean.setStrApproverEmpId("END");
				System.out.println(objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpId", "END");
				model.addAttribute("nextLevelApproveEmpName", "END");
				
			}else{
				model.addAttribute("nextLevelApproveEmpId", objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpName", objBOQBean.getStrBoqTo());
				
			}
			model.addAttribute("lowerLevelEmpId", objBOQBean.getStrLowerEmpId());
			model.addAttribute("lowerLevelEmpName", objBOQBean.getStrBoqFrom());
			
			String note=objBOQService.getBOQLevelComments(request);
			
			List<BOQBean> mainList= objBOQService.getBOQData(request,session);
			model.addAttribute("boqData", mainList);//means the list on top of the table
		//	List<BOQBean> subList=  objBOQService.getBOQForApprovalByID(request,session);
		//  model.addAttribute("subList", subList);//means the list in the table this is the table data
			
			List<BOQBean> listTempBOQDetails = objBOQService.getTempBOQWorkDetails(request,mainList.get(0));
			model.addAttribute("TempBoqWorkDeatils", listTempBOQDetails);//means the list in the table this is the table data
			
			model.addAttribute("BOQLevelCommentsList", note);
			
			model.addAttribute("BOQBean", objBOQBean);
			System.out.println(objBOQBean.getStrBoqFrom()+""+objBOQBean.getStrBoqTo());
			model.addAttribute("BOQRequestTo", objBOQBean.getStrBoqTo());
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		
			Map<String, String> WO_QSList = workControllerService.loadQSProducts(siteId,mainList.get(0).getTypeOfWork());
			model.addAttribute("productsMap", WO_QSList);
			
			List<Map<String, Object>> totalProductsList = objBOQService.getTempBOQMajorHeads(request,mainList.get(0).getTypeOfWork());
			model.addAttribute("totalProductsList",totalProductsList);//it is for dropDown major head list
			
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "boq/TempBOQDetailsPage";
	}
	
	@RequestMapping(value = "/getTempBOQMaterialDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String getTempBOQMaterialDetails(Model model, HttpServletRequest request,HttpSession session) {
		BOQBean objBOQBean = new BOQBean();
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		try {
			String boqType = request.getParameter("boqType");
			if(!boqType.equals("NORMAL")){
				throw new RuntimeException("Not a Normal Boq");
			}
			
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String boqSiteId = request.getParameter("siteId");
			objBOQBean.setStrSiteId(boqSiteId);
			String	viewTempBoq = request.getParameter("viewTempBoq") == null ? "" : request.getParameter("viewTempBoq");
			model.addAttribute("viewTempBoq", viewTempBoq);
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			objBOQBean=objBOQService.getBOQFromAndToDetails(objBOQBean,userId);
			if (objBOQBean.getStrApproverEmpId() == null) {
				objBOQBean.setStrApproverEmpId("END");
				System.out.println(objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpId", "END");
				model.addAttribute("nextLevelApproveEmpName", "END");
				
			}else{
				model.addAttribute("nextLevelApproveEmpId", objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpName", objBOQBean.getStrBoqTo());
				
			}
			model.addAttribute("lowerLevelEmpId", objBOQBean.getStrLowerEmpId());
			model.addAttribute("lowerLevelEmpName", objBOQBean.getStrBoqFrom());
			
			String note=objBOQService.getBOQLevelComments(request);
			
			List<BOQBean> mainList= objBOQService.getBOQData(request,session);
			model.addAttribute("boqData", mainList);//means the list on top of the table
		//	List<BOQBean> subList=  objBOQService.getBOQForApprovalByID(request,session);
		//  model.addAttribute("subList", subList);//means the list in the table this is the table data
			
			/*List<BOQBean> listTempBOQDetails = objBOQService.getTempBOQWorkDetails(request,mainList.get(0));
			model.addAttribute("TempBoqWorkDeatils", listTempBOQDetails);//means the list in the table this is the table data
			*/
			//BOQBean ObjBOQDetails = objBOQService.getBOQDetails(request);
			List<BOQBean> listBOQDetails = objBOQService.getTempBOQMaterialDetails(request,mainList.get(0));
			model.addAttribute("BOQDetailsList",listBOQDetails);
			
			//==
			model.addAttribute("BOQLevelCommentsList", note);
			
			model.addAttribute("BOQBean", objBOQBean);
			System.out.println(objBOQBean.getStrBoqFrom()+""+objBOQBean.getStrBoqTo());
			model.addAttribute("BOQRequestTo", objBOQBean.getStrBoqTo());
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		
			Map<String, String> WO_QSList = workControllerService.loadQSProducts(siteId,mainList.get(0).getTypeOfWork());
			model.addAttribute("productsMap", WO_QSList);
			
			List<Map<String, Object>> totalProductsList = objBOQService.getTempBOQMajorHeads(request,mainList.get(0).getTypeOfWork());
			model.addAttribute("totalProductsList",totalProductsList);//it is for dropDown major head list
			
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "boq/TempBOQMaterialDetails";
	}
	 
	@RequestMapping(value = "/approveTempBOQCreation.spring", method = RequestMethod.POST)
	public String approveTempBOQCreation(Model model, HttpServletRequest request, HttpSession session,  RedirectAttributes redir) {
		System.out.println("BOQController.approveTempBOQCreation()");
		BOQBean objBOQBean = new BOQBean();
		redir.addFlashAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		String response="";
		redir.addFlashAttribute("isNotRefeshed", "true");
		try {
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
			boolean isPendingOnUser = objBOQService.checkingIsItReallyPendindOnCurrentUser(userId,tempBOQNumber);
			if(!isPendingOnUser){
				response = "This Temp BOQ already Approved.";
				redir.addFlashAttribute("message1", response);
				return "redirect:/BOQResponse.spring";
			}
		/*	 objBOQBean = objBOQService.getBOQFromAndToDetails(objBOQBean,userId);
			if (objBOQBean.getStrApproverEmpId() == null) {
				objBOQBean.setStrApproverEmpId("END");
				System.out.println(objBOQBean.getStrApproverEmpId());
				request.setAttribute("nextLevelApproverEmpId", "END");
			}*/
			BOQBean objForOnlyPermanentBoqNumber = new BOQBean();
			 response = objBOQService.approveTempBOQ(request, session, objForOnlyPermanentBoqNumber);
			 if (response.equals("success")) {
				response = "Temp BOQ Approved Successfully.";
				if(StringUtils.isNotBlank(objForOnlyPermanentBoqNumber.getStrBOQNo())){
					response += "Permanent BOQ Number: "+objForOnlyPermanentBoqNumber.getStrBOQNo();
				}
				//model.addAttribute("message", response);
				redir.addFlashAttribute("message", response);
			} else {
				response = "Approve Temp BOQ  Failed.";
				//model.addAttribute("message1", response);
				redir.addFlashAttribute("message1", response);
			}
			//model.addAttribute("responseMessage", response);
			redir.addFlashAttribute("responseMessage", response);
		} catch (Exception e) {
			e.printStackTrace();
		response = "Approve Temp BOQ  Failed.";
			//model.addAttribute("message1", response);
			redir.addFlashAttribute("message1", response);
		}

		return "redirect:/BOQResponse.spring";//"response";
	}
	@RequestMapping(value = "/BOQResponse.spring",  method = RequestMethod.GET)
	public ModelAndView BOQResponse(){
	    return new ModelAndView("boq/BOQResponse");
	}
	@RequestMapping(value = "/rejectTempBOQCreation.spring", method = RequestMethod.POST)
	public String rejectTempBOQCreation(Model model, HttpServletRequest request, HttpSession session,  RedirectAttributes redir) {
		System.out.println("BOQController.rejectTempBOQCreation()");
		BOQBean objBOQBean = new BOQBean();
		redir.addFlashAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		String response="";
		redir.addFlashAttribute("isNotRefeshed", "true");
		try {
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
			boolean isPendingOnUser = objBOQService.checkingIsItReallyPendindOnCurrentUser(userId,tempBOQNumber);
			if(!isPendingOnUser){
				response = "This Temp BOQ already Approved.";
				redir.addFlashAttribute("message1", response);
				return "redirect:/BOQResponse.spring";
			}
			/* objBOQBean = objBOQService.getBOQFromAndToDetails(objBOQBean,userId);
			if (objBOQBean.getStrApproverEmpId() == null) {
				objBOQBean.setStrApproverEmpId("END");
				System.out.println(objBOQBean.getStrApproverEmpId());
				request.setAttribute("nextLevelApproverEmpId", "END");
			}*/
			 response = objBOQService.rejectTempBOQ(request, session);
			if (response.equals("success")) {
				response = "Temp BOQ Rejected Successfully.";
				//model.addAttribute("message", response);
				redir.addFlashAttribute("message", response);
			} else {
				response = "Failed Temp BOQ  Rejection.";
				//model.addAttribute("message1", response);
				redir.addFlashAttribute("message1", response);
			}
			//model.addAttribute("responseMessage", response);
			redir.addFlashAttribute("responseMessage", response);
			
		} catch (Exception e) {
			e.printStackTrace();
			response = "Failed Temp BOQ  Rejection.";
			//model.addAttribute("message1", response);
			redir.addFlashAttribute("message1", response);
		}
		return "redirect:/BOQResponse.spring";//"response";
	}
	
	@RequestMapping(value = "/viewSiteWiseBOQ.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String viewSiteWiseBOQ( Model model,HttpServletRequest request,HttpSession session) {
		String pageHighlightURL=request.getParameter("pageHighlightURL");
		if(pageHighlightURL!=null){model.addAttribute("pageHighlightURL",pageHighlightURL);}
		else{model.addAttribute("pageHighlightURL","viewSiteWiseBOQ.spring");}

		List<BOQBean> listofBOQ = null;
		try {
			listofBOQ = objBOQService.getSitewiseBOQList(request,session);
			model.addAttribute("listofBOQ",listofBOQ);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "boq/SitewiseBOQList";
	
	}
	
	
		@RequestMapping(value = "/viewMyBOQ.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String viewMyBOQ( Model model,HttpServletRequest request,HttpSession session) {
		//
			String pageHighlightURL=request.getParameter("pageHighlightURL");
			if(pageHighlightURL!=null){model.addAttribute("pageHighlightURL",pageHighlightURL);}
			else{model.addAttribute("pageHighlightURL","viewMyBOQ.spring");}

			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			
			String siteName = session.getAttribute("SiteName") == null ? "" : session.getAttribute("SiteName").toString();
			String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
			String enableWOSubModules = UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor") == null ? "00" : UIProperties.validateParams.getProperty("openWorkOrderSubModuleFor").toString();
			
			List<String> enableWOSubModulesSiteList=Arrays.asList(enableWOSubModules.split(","));
			if(!enableWOSubModulesSiteList.contains(siteId)){
				model.addAttribute("message1", "Hello "+strUserName+" As of now "+siteName+" Site can not access WORK ORDER & Contractor Bills. We will get back to you as soon as possible.");
				return "response";
			}
			//
		
		List<BOQBean> listofBOQ = null;
		try {
			listofBOQ = objBOQService.getMyBOQList(request,session);
			model.addAttribute("listofBOQ",listofBOQ);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return "boq/BOQList";
	
	}
		@RequestMapping(value = "/getBOQDetailsSelection.spring", method = RequestMethod.GET)
		public String getBOQDetailsSelection(Model model, HttpServletRequest request,HttpSession session) {
			model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));

			int site_Id = 0;
			String strBOQNumber = "";
			String strBOQDate = "";
			try{
			
				session = request.getSession(true);
				strBOQNumber =  request.getParameter("BOQNo");
				strBOQDate =  request.getParameter("BOQDate");
			
				
				BOQBean ObjBOQDetails = objBOQService.getBOQDetails(request);
				List<Map<String, Object>> totalProductsList = objBOQService.getBOQMajorHeads(request,ObjBOQDetails.getTypeOfWork());
				List<BOQBean> majorHeadsDetails = objBOQService.getBOQMajorHeadsDetails(request,ObjBOQDetails.getTypeOfWork());
				model.addAttribute("majorHeadsDetails",majorHeadsDetails);
				
				
				
				request.setAttribute("ObjBOQDetails", ObjBOQDetails);
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("strBOQNumber",strBOQNumber);
				model.addAttribute("strBOQDate",strBOQDate);
				model.addAttribute("totalProductsList",totalProductsList);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			
			return "boq/SelectBOQWork";
		}
		@RequestMapping(value = "/getMinorHeads.spring", method = RequestMethod.GET)
		public String getBOQMinorHeadsDetails(Model model, HttpServletRequest request,HttpSession session) {
			model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
			try{
				
				session = request.getSession(true);

				BOQBean ObjBOQDetails = objBOQService.getBOQDetails(request);
				List<BOQBean> minorHeadsDetails = objBOQService.getBOQMinorHeadsDetails(request,ObjBOQDetails.getTypeOfWork());
				model.addAttribute("minorHeadsDetails",minorHeadsDetails);
				
				request.setAttribute("ObjBOQDetails", ObjBOQDetails);
				
				List<Map<String, Object>> totalProductsList = objBOQService.getBOQMajorHeads(request,ObjBOQDetails.getTypeOfWork());
				model.addAttribute("totalProductsList",totalProductsList);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

			
			
			return "boq/ListMinorHeads";
		}
	@RequestMapping(value = "/getBOQDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String getBOQDetails(Model model, HttpServletRequest request,HttpSession session) {

		int site_Id = 0;
		String strBOQNumber = "";
		String strBOQDate = "";
		String pageHighlightURL=request.getParameter("pageHighlightURL");
		if(pageHighlightURL!=null){model.addAttribute("pageHighlightURL",pageHighlightURL);}
		else{model.addAttribute("pageHighlightURL","getBOQDetails.spring");}
		try{
		
			session = request.getSession(true);
		//	site_Id=Integer.parseInt(request.getParameter("siteId"));
			//site_Id = 112;
			strBOQNumber =  request.getParameter("BOQNo");
			strBOQDate =  request.getParameter("BOQDate");
			
			String showCompleteBOQ = request.getParameter("showCompleteBOQ");
			if(showCompleteBOQ!=null&&showCompleteBOQ.equals("true")){
				model.addAttribute("showCompleteBOQ",true);
			}
			else{
				model.addAttribute("showCompleteBOQ",false);
			}
		
			//model.addAttribute("indentCreationModelForm", icb);
			
			BOQBean ObjBOQDetails = objBOQService.getBOQDetails(request);
			List<BOQBean> listBOQDetails = objBOQService.getBOQWorkDetails(request,ObjBOQDetails);
			
			
		/*	for(int i =0 ;i< list.size();i++ ){
				
				IndentCreationBean objIndentCreationBean = list.get(i);
				model.addAttribute("materialEditComment", objIndentCreationBean.getMaterialEditComment());
				strEditComments = objIndentCreationBean.getMaterialEditComment();
				
				if(strEditComments.contains("@@@")){
					String strEditCommentsArr [] = strEditComments.split("@@@");
					for(int j = 0; j< strEditCommentsArr.length;j++){
						IndentCreationBean objCommentIndentCreationBean  = new IndentCreationBean();
						objCommentIndentCreationBean.setMaterialEditComment(strEditCommentsArr[j]);
						editList.add(objCommentIndentCreationBean);
					}
					
					model.addAttribute("materialEditCommentList", editList);
				}
			
			}*/
			
			
			request.setAttribute("ObjBOQDetails", ObjBOQDetails);
			//model.addAttribute("ObjBOQDetails", ObjBOQDetails);
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("strBOQNumber",strBOQNumber);
			model.addAttribute("strBOQDate",strBOQDate);
			model.addAttribute("BOQDetailsList",listBOQDetails);
			
			List<Map<String, Object>> totalProductsList = objBOQService.getBOQMajorHeads(request,ObjBOQDetails.getTypeOfWork());
			model.addAttribute("totalProductsList",totalProductsList);
			//model.addAttribute("deletedProductDetailsList",ics.getDeletedProductDetailsLists(indentNumber));
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		return "boq/BOQDetailsPage";
	}
	@RequestMapping(value = "/getBOQMaterialDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public String getBOQMaterialDetails(Model model, HttpServletRequest request,HttpSession session) {
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));

		int site_Id = 0;
		String strBOQNumber = "";
		String strBOQDate = "";
		try{
		
			session = request.getSession(true);
			strBOQNumber =  request.getParameter("BOQNo");
			strBOQDate =  request.getParameter("BOQDate");
			
			BOQBean ObjBOQDetails = objBOQService.getBOQDetails(request);
			List<BOQBean> listBOQDetails = objBOQService.getBOQMaterialDetails(request,ObjBOQDetails);
			//==
			
			
			request.setAttribute("ObjBOQDetails", ObjBOQDetails);
			//model.addAttribute("ObjBOQDetails", ObjBOQDetails);
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			model.addAttribute("strBOQNumber",strBOQNumber);
			model.addAttribute("strBOQDate",strBOQDate);
			model.addAttribute("BOQDetailsList",listBOQDetails);
			
			List<Map<String, Object>> totalProductsList = objBOQService.getBOQMajorHeads(request,ObjBOQDetails.getTypeOfWork());
			model.addAttribute("totalProductsList",totalProductsList);
			//model.addAttribute("deletedProductDetailsList",ics.getDeletedProductDetailsLists(indentNumber));
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		
		return "boq/BOQMaterialDetails";
	}
	@RequestMapping(value = "/createBlockIdAndFloorId.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView createBlockIdAndFloorId( HttpServletRequest request,HttpSession session) {
		ModelAndView model = new ModelAndView();
		String site_id = session.getAttribute("SiteId").toString();
		String user_id = session.getAttribute("UserId").toString();
		
		objBOQService.createBlockIdAndFloorId(user_id,site_id);
		return model;
	
	}
	@RequestMapping(value = "/downloadBOQ.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView downloadBOQ( HttpServletRequest request,HttpServletResponse response,HttpSession session) {
		ModelAndView model=new ModelAndView();
		List<String> fileName = new ArrayList<String>();
        String contentType = null;
        
        
        byte[] file = getFileOnServer(request,fileName);
        
        if (file.length==0) {
            try {
				throw new RuntimeException("File not found");
			} catch (Exception e) {
				e.printStackTrace();
			}
            model.addObject("message1", "File not found");
            model.setViewName("response");
            return model;
        }


        response.setHeader("Content-disposition", "attachment;filename=" + fileName.get(0));
        response.setHeader("charset", "iso-8859-1");
        response.setContentType(contentType);
        response.setContentLength(file.length);
        response.setStatus(HttpServletResponse.SC_OK);

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(file, 0, file.length);
            outputStream.flush();
            outputStream.close();
            response.flushBuffer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
		return null;
    }
	private byte[] getFileOnServer(HttpServletRequest request,List<String> fileName) {
		FileInputStream fileInputStream = null;
		byte[] bytesArray = null;
		String BOQNumber = request.getParameter("BOQNumber");
		String tempBOQNo = request.getParameter("tempBOQNo");
        String siteId = request.getParameter("siteId");
        String siteName = request.getParameter("siteName");
        String permanentBOQ = request.getParameter("permanentBOQ");
        String temporaryBOQ = request.getParameter("temporaryBOQ");
        String versionNo = request.getParameter("versionNo");
        int portNumber=request.getLocalPort();
        String rootFilePath = null;
        if(portNumber==80){
        	rootFilePath = validateParams.getProperty("BOQ_EXCEL_PATH") == null ? "" : validateParams.getProperty("BOQ_EXCEL_PATH").toString();
        }
        else{
        	rootFilePath = validateParams.getProperty("BOQ_EXCEL_PATH_DEV") == null ? "" : validateParams.getProperty("BOQ_EXCEL_PATH_DEV").toString();
        }

		try {
			String FilePath =  "";
			String FileName = "";
			if(permanentBOQ!=null){
				FilePath =  rootFilePath+siteId+File.separator;
				FileName = "BOQ_"+siteName+"_"+BOQNumber.replace("/","$$")+"_"+versionNo+".xlsx";
			}
			else if(temporaryBOQ!=null){
				FilePath =  rootFilePath+siteId+File.separator+"temp"+File.separator;
				FileName = "BOQ_"+siteName+"_"+tempBOQNo+".xlsx";
			}
			fileName.add(FileName);
			File file = new File(FilePath+FileName);
			bytesArray = new byte[(int) file.length()];

			//read file into bytes[]
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bytesArray);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fileInputStream != null) {
				try {
					fileInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return bytesArray;
	}
	/************************************************ Revise BOQ Upload - start ************************************************/
	@RequestMapping(value = "/uploadReviseBOQ.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView uploadReviseBOQ( HttpServletRequest request,HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.addObject("pageHighlightURL","uploadReviseBOQ.spring");
		model.setViewName("boq/UploadReviseBOQ");
		return model;
	}
	@RequestMapping(value = "/uploadSiteWiseReviseBOQ.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView uploadSiteWiseReviseBOQ( HttpServletRequest request,HttpSession session) {
		ModelAndView model = new ModelAndView();
		model.addObject("pageHighlightURL","uploadSiteWiseReviseBOQ.spring");
		model.addObject("showSiteSelection",true);
		model.setViewName("boq/UploadReviseBOQ");
		return model;
	}
	@RequestMapping(value = "/SaveReviseBOQ.spring", method = RequestMethod.POST)
	public ModelAndView SaveReviseBOQ(  HttpServletRequest request, HttpSession session, @RequestParam("file") MultipartFile[] files,  RedirectAttributes redir) throws IllegalStateException, IOException {
		ModelAndView model = new ModelAndView();
		model.addObject("pageHighlightURL",request.getParameter("pageHighlightURL"));
		redir.addFlashAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		String boqSiteId = null;
		String boqSiteName = null;
		String session_site_id = session.getAttribute("SiteId").toString();
		String session_siteName = session.getAttribute("SiteName").toString();
		String dropdown_SiteId = request.getParameter("dropdown_SiteId");
		if(dropdown_SiteId==null){
			boqSiteId = session_site_id;
			boqSiteName = session_siteName;
		}
		else{
			model.addObject("showSiteSelection",true);
			if(StringUtils.isBlank(dropdown_SiteId)){
				model.setViewName("boq/UploadReviseBOQ");
				model.addObject("displayErrMsg", "Please select Site.");
				return model;
			}
			boqSiteId = dropdown_SiteId;
			boqSiteName = objBOQService.getSiteNameBySiteId(boqSiteId);
		}
		String user_id = session.getAttribute("UserId").toString();
		String response = "";
		List<String> errMsg = new ArrayList<String>();
		
		
		for (int index = 0; index < files.length; index++) {
			MultipartFile multipartFile = files[index];
			
			if(!multipartFile.isEmpty()){
				if(multipartFile.getOriginalFilename().endsWith("xlsx")){
					List<String> tempBOQNoForController = new ArrayList<String>();
					response = objBOQService.reviseBOQ(multipartFile,errMsg,user_id,boqSiteId,tempBOQNoForController);
					//response="Success";tempBOQNoForController=143;
					// Save Excel file in Server - start
					if (response.equalsIgnoreCase("Success")) {
						try {

							int portNumber=request.getLocalPort();
					        String rootFilePath = null;
					        if(portNumber==80){
					        	rootFilePath = validateParams.getProperty("BOQ_EXCEL_PATH") == null ? "" : validateParams.getProperty("BOQ_EXCEL_PATH").toString();
					        }
					        else{
					        	rootFilePath = validateParams.getProperty("BOQ_EXCEL_PATH_DEV") == null ? "" : validateParams.getProperty("BOQ_EXCEL_PATH_DEV").toString();
					        }
					        File dir = new File(rootFilePath+boqSiteId+"//temp");
							if (!dir.exists())
								dir.mkdirs();

							String filePath = dir.getAbsolutePath()
							+ File.separator +"BOQ_"+ boqSiteName+"_"+tempBOQNoForController.get(0)+".xlsx"; 
							
							multipartFile.transferTo(new File(filePath));


							System.out.println("BOQ Uploaded");
							//return "You successfully uploaded file" ;
						} catch (Exception e) {
							System.out.println("BOQ NOT Uploaded");
							//return "You failed to upload " ;
						}
					} 
					// Save Excel file in Server - end
					model = new ModelAndView("redirect:/UploadBOQResponse.spring");
					//model.setViewName("boq/UploadBOQResponse");
					//model.addObject("response", response);
					//model.addObject("errMsg", errMsg);
					redir.addFlashAttribute("isNotRefeshed", "true");
					redir.addFlashAttribute("response", response);
					redir.addFlashAttribute("errMsg", errMsg);
					
				}
				else{
					model.setViewName("boq/UploadReviseBOQ");
					model.addObject("displayErrMsg", "Upload Excel File of type (.xlsx)");
				}
			}
			else{
				model.setViewName("boq/UploadReviseBOQ");
				model.addObject("displayErrMsg", "Please Upload Excel File.");
			}

		}
		
		return model;
	}
	/************************************************* Revise BOQ Upload- end ***************************************************/
	
	@RequestMapping(value = "/getPendingForApprovalReviseBOQMajorHeads.spring", method = RequestMethod.GET)
	public String getPendingForApprovalReviseBOQMajorHeads(Model model, HttpServletRequest request,HttpSession session) {
		BOQBean objBOQBean = new BOQBean();
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		try {
			String typeOfWork = "PIECEWORK";
			String boqType = request.getParameter("boqType");
			if(!boqType.equals("REVISED")){
				throw new RuntimeException("Not a Revised Boq");
			}
			
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String boqSiteId = request.getParameter("siteId");
			objBOQBean.setStrSiteId(boqSiteId);
			String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
			String	viewTempBoq = request.getParameter("viewTempBoq") == null ? "" : request.getParameter("viewTempBoq");
			model.addAttribute("viewTempBoq", viewTempBoq);
			
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			objBOQBean=objBOQService.getBOQFromAndToDetails(objBOQBean,userId);
			if (objBOQBean.getStrApproverEmpId() == null) {
				objBOQBean.setStrApproverEmpId("END");
				System.out.println(objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpId", "END");
				model.addAttribute("nextLevelApproveEmpName", "END");
				
			}else{
				model.addAttribute("nextLevelApproveEmpId", objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpName", objBOQBean.getStrBoqTo());
				
			}
			model.addAttribute("lowerLevelEmpId", objBOQBean.getStrLowerEmpId());
			model.addAttribute("lowerLevelEmpName", objBOQBean.getStrBoqFrom());
			
			String note=objBOQService.getBOQLevelComments(request);
			
			List<BOQBean> mainList= objBOQService.getBOQData(request,session);
			model.addAttribute("boqData", mainList);//means the list on top of the table
		//	List<BOQBean> subList=  objBOQService.getBOQForApprovalByID(request,session);
		//  model.addAttribute("subList", subList);//means the list in the table this is the table data
			MultiObject multiObj = objBOQService.getPreviousBOQTotal(tempBOQNumber);
			String prevBoqTotal = multiObj.getString1();
			String prevMaterialTotal = multiObj.getString2();
			String prevLaborTotal = multiObj.getString3();
			model.addAttribute("prevBoqTotal", prevBoqTotal);
			model.addAttribute("prevMaterialTotal", prevMaterialTotal);
			model.addAttribute("prevLaborTotal", prevLaborTotal);
			
			List<BOQBean> MajorHeadDetails = objBOQService.getTempBOQMajorHeadsDetails(request, typeOfWork);
			model.addAttribute("MajorHeadDetails", MajorHeadDetails);
			
			/*List<BOQBean> tempBOQWorkDeatils=	objBOQService.getReviseBOQWorkDetails(request,session,mainList.get(0).getTypeOfWork(),recordType);
			model.addAttribute("boqWorkDeatils", tempBOQWorkDeatils);//means the list in the table this is the table data
			model.addAttribute("recordType", recordType);*/
			model.addAttribute("BOQLevelCommentsList", note);
			
			//List<BOQBean> ReviseBOQChangedDetails=	objBOQService.getReviseBOQChangedWorkDetails(request,session,mainList.get(0).getTypeOfWork(),tempBOQNumber);
			//model.addAttribute("ReviseBOQChangedDetails", ReviseBOQChangedDetails);
			
			//List<BOQBean> SOWChangedDetails=	objBOQService.getSOWChangedWorkDetails(request,session,mainList.get(0).getTypeOfWork(),tempBOQNumber);
			//model.addAttribute("SOWChangedDetails", SOWChangedDetails);
			
			
			model.addAttribute("BOQBean", objBOQBean);
			System.out.println(objBOQBean.getStrBoqFrom()+""+objBOQBean.getStrBoqTo());
			model.addAttribute("BOQRequestTo", objBOQBean.getStrBoqTo());
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		
			Map<String, String> WO_QSList = workControllerService.loadQSProducts(siteId,"PIECEWORK");
			model.addAttribute("productsMap", WO_QSList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "boq/ApproveTempReviseBOQMajorHeads";
	}
	
	@RequestMapping(value = "/getPendingForApprovalReviseBOQDetails.spring", method = RequestMethod.GET)
	public String getPendingForApprovalReviseBOQDetails(Model model, HttpServletRequest request,HttpSession session) {
		BOQBean objBOQBean = new BOQBean();
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		try {
			String boqType = request.getParameter("boqType");
			if(!boqType.equals("REVISED")){
				throw new RuntimeException("Not a Revised Boq");
			}
			
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String boqSiteId = request.getParameter("siteId");
			objBOQBean.setStrSiteId(boqSiteId);
			String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
			String recordType = request.getParameter("recordType")==null?"":request.getParameter("recordType");
			String majorHeadId = request.getParameter("majorHeadId")==null?"":request.getParameter("majorHeadId");
			String	viewTempBoq = request.getParameter("viewTempBoq") == null ? "" : request.getParameter("viewTempBoq");
			model.addAttribute("viewTempBoq", viewTempBoq);
			
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			objBOQBean=objBOQService.getBOQFromAndToDetails(objBOQBean,userId);
			if (objBOQBean.getStrApproverEmpId() == null) {
				objBOQBean.setStrApproverEmpId("END");
				System.out.println(objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpId", "END");
				model.addAttribute("nextLevelApproveEmpName", "END");
				
			}else{
				model.addAttribute("nextLevelApproveEmpId", objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpName", objBOQBean.getStrBoqTo());
				
			}
			model.addAttribute("lowerLevelEmpId", objBOQBean.getStrLowerEmpId());
			model.addAttribute("lowerLevelEmpName", objBOQBean.getStrBoqFrom());
			
			String note=objBOQService.getBOQLevelComments(request);
			
			List<BOQBean> mainList= objBOQService.getBOQData(request,session);
			model.addAttribute("boqData", mainList);//means the list on top of the table
		//	List<BOQBean> subList=  objBOQService.getBOQForApprovalByID(request,session);
		//  model.addAttribute("subList", subList);//means the list in the table this is the table data
			MultiObject multiObj = objBOQService.getPreviousBOQTotal(tempBOQNumber);
			String prevBoqTotal = multiObj.getString1();
			String prevMaterialTotal = multiObj.getString2();
			String prevLaborTotal = multiObj.getString3();
			model.addAttribute("prevBoqTotal", prevBoqTotal);
			model.addAttribute("prevMaterialTotal", prevMaterialTotal);
			model.addAttribute("prevLaborTotal", prevLaborTotal);
			
			List<BOQBean> tempBOQWorkDeatils=	objBOQService.getReviseBOQWorkDetails(request,session,mainList.get(0).getTypeOfWork(),recordType,majorHeadId);
			model.addAttribute("boqWorkDeatils", tempBOQWorkDeatils);//means the list in the table this is the table data
			model.addAttribute("recordType", recordType);
			model.addAttribute("majorHeadId", majorHeadId);
			model.addAttribute("BOQLevelCommentsList", note);
			
			//List<BOQBean> ReviseBOQChangedDetails=	objBOQService.getReviseBOQChangedWorkDetails(request,session,mainList.get(0).getTypeOfWork(),tempBOQNumber);
			//model.addAttribute("ReviseBOQChangedDetails", ReviseBOQChangedDetails);
			
			//List<BOQBean> SOWChangedDetails=	objBOQService.getSOWChangedWorkDetails(request,session,mainList.get(0).getTypeOfWork(),tempBOQNumber);
			//model.addAttribute("SOWChangedDetails", SOWChangedDetails);
			
			
			model.addAttribute("BOQBean", objBOQBean);
			System.out.println(objBOQBean.getStrBoqFrom()+""+objBOQBean.getStrBoqTo());
			model.addAttribute("BOQRequestTo", objBOQBean.getStrBoqTo());
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		
			Map<String, String> WO_QSList = workControllerService.loadQSProducts(siteId,"PIECEWORK");
			model.addAttribute("productsMap", WO_QSList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "boq/ApproveTempReviseBOQPage";
	}
	@RequestMapping(value = "/getModificationDetails.spring", method = RequestMethod.GET)
	public String getModificationDetails(Model model, HttpServletRequest request,HttpSession session) {
		BOQBean objBOQBean = new BOQBean();
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		try {
			String boqType = request.getParameter("boqType");
			if(!boqType.equals("REVISED")){
				throw new RuntimeException("Not a Revised Boq");
			}
			
			String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			String boqSiteId = request.getParameter("siteId");
			objBOQBean.setStrSiteId(boqSiteId);
			String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
			String recordType = request.getParameter("recordType")==null?"":request.getParameter("recordType");
			String majorHeadId = request.getParameter("majorHeadId")==null?"":request.getParameter("majorHeadId");
			String	viewTempBoq = request.getParameter("viewTempBoq") == null ? "" : request.getParameter("viewTempBoq");
			model.addAttribute("viewTempBoq", viewTempBoq);
			
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			objBOQBean=objBOQService.getBOQFromAndToDetails(objBOQBean,userId);
			if (objBOQBean.getStrApproverEmpId() == null) {
				objBOQBean.setStrApproverEmpId("END");
				System.out.println(objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpId", "END");
				model.addAttribute("nextLevelApproveEmpName", "END");
				
			}else{
				model.addAttribute("nextLevelApproveEmpId", objBOQBean.getStrApproverEmpId());
				model.addAttribute("nextLevelApproveEmpName", objBOQBean.getStrBoqTo());
				
			}
			model.addAttribute("lowerLevelEmpId", objBOQBean.getStrLowerEmpId());
			model.addAttribute("lowerLevelEmpName", objBOQBean.getStrBoqFrom());
			
			String note=objBOQService.getBOQLevelComments(request);
			
			List<BOQBean> mainList= objBOQService.getBOQData(request,session);
			model.addAttribute("boqData", mainList);//means the list on top of the table
		//	List<BOQBean> subList=  objBOQService.getBOQForApprovalByID(request,session);
		//  model.addAttribute("subList", subList);//means the list in the table this is the table data
			MultiObject multiObj = objBOQService.getPreviousBOQTotal(tempBOQNumber);
			String prevBoqTotal = multiObj.getString1();
			String prevMaterialTotal = multiObj.getString2();
			String prevLaborTotal = multiObj.getString3();
			model.addAttribute("prevBoqTotal", prevBoqTotal);
			model.addAttribute("prevMaterialTotal", prevMaterialTotal);
			model.addAttribute("prevLaborTotal", prevLaborTotal);
			
			//List<BOQBean> tempBOQWorkDeatils=	objBOQService.getReviseBOQWorkDetails(request,session,mainList.get(0).getTypeOfWork());
			//model.addAttribute("boqWorkDeatils", tempBOQWorkDeatils);//means the list in the table this is the table data
			model.addAttribute("BOQLevelCommentsList", note);
			
			List<BOQBean> ReviseBOQChangedDetails=	objBOQService.getReviseBOQChangedWorkDetails(request,session,mainList.get(0).getTypeOfWork(),tempBOQNumber,recordType,majorHeadId);
			model.addAttribute("ReviseBOQChangedDetails", ReviseBOQChangedDetails);
			
			List<BOQBean> SOWChangedDetails=	objBOQService.getSOWChangedWorkDetails(request,session,mainList.get(0).getTypeOfWork(),tempBOQNumber);
			model.addAttribute("SOWChangedDetails", SOWChangedDetails);
			
			
			model.addAttribute("BOQBean", objBOQBean);
			System.out.println(objBOQBean.getStrBoqFrom()+""+objBOQBean.getStrBoqTo());
			model.addAttribute("BOQRequestTo", objBOQBean.getStrBoqTo());
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
		
			Map<String, String> WO_QSList = workControllerService.loadQSProducts(siteId,"PIECEWORK");
			model.addAttribute("productsMap", WO_QSList);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "boq/ModificationDetails";
	}
	@RequestMapping(value = "/approveReviseTempBOQCreation.spring", method = RequestMethod.POST)
	public String approveReviseTempBOQCreation(Model model, HttpServletRequest request, HttpSession session,  RedirectAttributes redir) {
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		redir.addFlashAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		redir.addFlashAttribute("isNotRefeshed", "true");
		System.out.println("BOQController.approveTempBOQCreation()");
		BOQBean objBOQBean = new BOQBean();
		String response="";
		List<String> errMsg = new ArrayList<String>();
		try {
			
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
			boolean isPendingOnUser = objBOQService.checkingIsItReallyPendindOnCurrentUser(userId,tempBOQNumber);
			if(!isPendingOnUser){
				response = "This Temp BOQ already Approved.";
				redir.addFlashAttribute("message1", response);
				return "redirect:/BOQResponse.spring";
			}
			/*	 objBOQBean = objBOQService.getBOQFromAndToDetails(objBOQBean,userId);
			if (objBOQBean.getStrApproverEmpId() == null) {
				objBOQBean.setStrApproverEmpId("END");
				System.out.println(objBOQBean.getStrApproverEmpId());
				request.setAttribute("nextLevelApproverEmpId", "END");
			}*/
			BOQBean objForOnlyPermanentBoqNumber = new BOQBean();
			 response = objBOQService.approveReviseTempBOQ(request, session, objForOnlyPermanentBoqNumber, errMsg);
			 if (response.equals("success")) {
				response = "Temp BOQ Approved Successfully.";
				if(StringUtils.isNotBlank(objForOnlyPermanentBoqNumber.getStrBOQNo())){
					response += "Permanent BOQ Number: "+objForOnlyPermanentBoqNumber.getStrBOQNo();
				}
				//model.addAttribute("message", response);
				redir.addFlashAttribute("message", response);
				
			} else {
				response = "Approve Temp BOQ  Failed.";
				//model.addAttribute("message1", response);
				redir.addFlashAttribute("message1", response);
			}
			//model.addAttribute("responseMessage", response);
			 redir.addFlashAttribute("responseMessage", response);
		} catch (Exception e) {
			e.printStackTrace();
		response = "Approve Temp BOQ  Failed.";
			//model.addAttribute("message1", response);
		redir.addFlashAttribute("message1", response);
		}
		redir.addFlashAttribute("errMsg", errMsg);
		return "redirect:/BOQResponse.spring";//"response";
	}
	
	@RequestMapping(value = "/getBOQChangedWorkDetails.spring", method = RequestMethod.GET)
	public String getBOQChangedWorkDetails(Model model, HttpServletRequest request,HttpSession session) {
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		BOQBean objBOQBean = new BOQBean();
		try {
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			
			BOQBean ObjBOQDetails = objBOQService.getBOQDetailsFromBackup(request);
			request.setAttribute("ObjBOQDetails", ObjBOQDetails);
			
			//String tempBOQNumber = String.valueOf(ObjBOQDetails.getIntTempBOQNo());
			List<BOQBean> ReviseBOQChangedDetails=	objBOQService.getReviseBOQChangedWorkDetailsBasedOnVerNo(request,session,ObjBOQDetails.getTypeOfWork());
			model.addAttribute("ReviseBOQChangedDetails", ReviseBOQChangedDetails);
			if(ReviseBOQChangedDetails.isEmpty()){
				model.addAttribute("showChanges", false);
			}
			else{
				model.addAttribute("showChanges", true);
			}
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "boq/ShowBOQChangedDetails";
	}
	@RequestMapping(value = "/getBOQAllVersions.spring", method = RequestMethod.GET)
	public String getBOQAllVersions(Model model, HttpServletRequest request,HttpSession session) {
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		BOQBean objBOQBean = new BOQBean();
		try {
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			
			BOQBean ObjBOQDetails = objBOQService.getBOQDetails(request);
			request.setAttribute("ObjBOQDetails", ObjBOQDetails);
			
			List<BOQBean> BOQAllVersions=	objBOQService.getBOQAllVersions(request,session);
			model.addAttribute("BOQAllVersions", BOQAllVersions);
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "boq/Versions";
	}
	
	@RequestMapping(value = "/viewAllBlocksFloorsFlats.spring", method = RequestMethod.GET)
	public String viewAllBlocksFloorsFlats(Model model, HttpServletRequest request,HttpSession session) {
		model.addAttribute("pageHighlightURL",request.getParameter("pageHighlightURL"));
		BOQBean objBOQBean = new BOQBean();
		try {
			String siteId = request.getParameter("siteId");
			if(StringUtils.isBlank(siteId)){
				siteId = session.getAttribute("SiteId").toString();
			}
			String siteName = objBOQService.getSiteNameBySiteId(siteId);
			model.addAttribute("siteName", siteName);
			List<BOQBean> AllBlocksFloorsFlats=	objBOQService.viewAllBlocksFloorsFlats(siteId);
			model.addAttribute("AllBlocksFloorsFlats", AllBlocksFloorsFlats);
			
			
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "boq/ViewAllBlocksFloorsFlats";
	}
	
	
	
}
