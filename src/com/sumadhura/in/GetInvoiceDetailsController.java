package com.sumadhura.in;

import java.io.DataInputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.sumadhura.bean.GetInvoiceDetailsBean;
import com.sumadhura.bean.IndentReceiveBean;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.service.GetTransportChargesListService;
import com.sumadhura.service.IndentReceiveService;
import com.sumadhura.service.InwardToGetInvoiceDetailsService;
import com.sumadhura.transdao.IndentIssueDaoImpl;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;
@Controller
public class GetInvoiceDetailsController extends UIProperties{

	@Autowired
	@Qualifier("irsClass")
	IndentReceiveService irs;
	@Autowired
	InwardToGetInvoiceDetailsService inwardToGetInvoiceDetails;
	GetTransportChargesListService transportChargesList;



	@RequestMapping(value = "/inwardGetInvoiceDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView doInwardGetInvoiceDetails(HttpSession session){

		ModelAndView model = null;

		try {
			model = new ModelAndView();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("indentGetInvoiceDetails");
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Update Invoice Details View","success",site_id1);
		
		
		return model;

	}
	//this method is loading all venor Names
	@RequestMapping(value = "/loadVendorNames.spring", method = RequestMethod.GET)
	public @ResponseBody String loadVendorNames(HttpServletResponse response, HttpServletRequest request, HttpSession session) {
		System.out.println("GetInvoiceDetailsController.loadVendorNames()");
		String resp = "";
		try {
			String vendorName=request.getParameter("vendorName")==null?"":request.getParameter("vendorName");
			System.out.println(vendorName);
			List<String> childProductList = inwardToGetInvoiceDetails.loadAllVendorNames(vendorName);
			Gson gson=new Gson();
			resp =gson.toJson(childProductList);
			System.out.println(resp);
			return resp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resp;
	}

	@RequestMapping(value = "/doGetInvoiceDetails.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String doIndentIssueToOtherSiteInwards(HttpServletRequest request, HttpSession session,Model model){

		String invoiceNumber = "";
		String siteId = "";
		String response="";
		String vendorName1="";
		String indentEntryId = "";
		String  vendorName =  "";
		String invoiceDate="";
		String viewToSelected="";
		try {

			invoiceNumber = request.getParameter("invoiceNumber") == null ? "" : request.getParameter("invoiceNumber").trim();
			vendorName1=request.getParameter("vendorName") == null ? "" : request.getParameter("vendorName");
			invoiceDate=request.getParameter("invoiceDate") == null ? "" : request.getParameter("invoiceDate");
			System.out.println(vendorName1+" controller "+invoiceNumber);
			siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			
			//if the site is not coming from front end site then take the site id from session
			//dnt knwA siteId=request.getParameter("siteId")==null?siteId.toString():request.getParameter("siteId");
			
			//dnt knw
			
			String response1=inwardToGetInvoiceDetails.getVendorNameAndIndentEntryId(invoiceNumber,vendorName1,invoiceDate);
			if(response1.equals("") || response1==null){
				model.addAttribute("Message1","<font style=color:red>Please Enter Valid data.</font>");
				viewToSelected="indentGetInvoiceDetails";
			}else{
			String data[]=response1.split("\\|");
			
			
			if(data != null && data.length >= 2){

				indentEntryId=data[0];
				vendorName=data[1];

			}
			
			
			if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(indentEntryId)){

				List<GetInvoiceDetailsBean> list=inwardToGetInvoiceDetails.getIssuesToOtherLists(indentEntryId, siteId,vendorName1,invoiceDate);
				if(list.size() > 0){

					model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
					model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
					model.addAttribute("listIssueToOtherSiteInwardLists",list);
					model.addAttribute("listOfGetProductDetails",inwardToGetInvoiceDetails.getGetInvoiceDetailsLists(indentEntryId, siteId,vendorName1));
					model.addAttribute("listOfTransportChargesDetails",inwardToGetInvoiceDetails.getTransportChargesList(invoiceNumber, siteId,indentEntryId));
					model.addAttribute("gstMap", irs.getGSTSlabs());
					model.addAttribute("chargesMap", irs.getOtherCharges());
					model.addAttribute("vendorName",vendorName1);
					model.addAttribute("siteId",siteId);
					System.out.println("InvoiceNumber "+invoiceNumber);
					response="success";
					int count=0;
					String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
					String imgname = vendorName1+"_"+invoiceNumber+"_"+indentEntryId;
					loadInvoiceImgAndPdfFiles(rootFilePath,imgname,siteId,model,request); // call the getting pdf and images for showing purpose

				}
				else{
					request.setAttribute("Message", "Invoice number doesn't available in our record");
                	response="failed";
					return "indentGetInvoiceDetails";
				}

			} 
			model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
			viewToSelected="getInvoiceDetailsInwards";
		}
		} catch(Exception ex) {
			response="failed";
			ex.printStackTrace();
		} 
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Update Invoice Details click submit",response,site_id1);
		
		return viewToSelected;
	}

	@RequestMapping(value = "/getInvoiceDetails.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String getInvoiceDetails(HttpServletRequest request, HttpSession session,Model model){
		String invoiceNumber = "";
		String vendorName = "";
		String vendorId = "";
		String siteId = "";
		String strIndentEntryId  = ""; 
		String invoiceDate = "";
		String indentType="";
		boolean imageClick = false;
		List<GetInvoiceDetailsBean> GetInvoiceDetailsInward = new ArrayList<GetInvoiceDetailsBean>();
		List<String> getGrn=new ArrayList<String>();
		String strUrlName="";
		String urlName="";
		String toDate="";
		try {

			invoiceNumber = request.getParameter("invoiceNumber") == null ? "" : request.getParameter("invoiceNumber");
			if(invoiceNumber.contains("@")){invoiceNumber=invoiceNumber.replace('@','&');}
			invoiceDate = request.getParameter("invoiceDate") == null ? "" : request.getParameter("invoiceDate");
			indentType=request.getParameter("indentType")==null?"":request.getParameter("indentType");
			//vendorName = request.getParameter("vendorName") == null ? "" : request.getParameter("vendorName");
			vendorId = request.getParameter("vendorId") == null ? "" : request.getParameter("vendorId");
			strIndentEntryId = request.getParameter("IndentEntryId") == null ? "" : request.getParameter("IndentEntryId");
			siteId = request.getParameter("SiteId") == null ? request.getParameter("siteId") == null ? "" : request.getParameter("siteId") : request.getParameter("SiteId");
			//siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			imageClick = request.getParameter("imageClick") == null ? false : Boolean.valueOf(request.getParameter("imageClick"));
			urlName=request.getParameter("url") == null ? "" : request.getParameter("url");
			toDate=request.getParameter("toDate") == null ? "" : request.getParameter("toDate");
			//strUrlName =  request.getParameter("url");
			
			if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(invoiceNumber)){
				
				GetInvoiceDetailsInward=inwardToGetInvoiceDetails.getInvoiceDetails(invoiceNumber,siteId,vendorId,invoiceDate,indentType);
				
				getGrn.add(invoiceNumber);getGrn.add(vendorId);getGrn.add(invoiceDate);getGrn.add(strIndentEntryId);getGrn.add(siteId);
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				model.addAttribute("listIssueToOtherSiteInwardLists",GetInvoiceDetailsInward);
				model.addAttribute("listOfGetProductDetails",inwardToGetInvoiceDetails.getGetInvoiceDetailsLists2(invoiceNumber, siteId,vendorId,invoiceDate,indentType));
				model.addAttribute("listOfTransportChargesDetails",inwardToGetInvoiceDetails.getTransportChargesList2(invoiceNumber, siteId,vendorId,invoiceDate,indentType));
				model.addAttribute("taxInvoiceSubmisssionDetails",inwardToGetInvoiceDetails.getTaxInvoiceSubmissionDetails(strIndentEntryId,invoiceNumber, siteId,vendorId));
				model.addAttribute("gstMap", irs.getGSTSlabs());
				model.addAttribute("chargesMap", irs.getOtherCharges());
				model.addAttribute("getGrnValues",getGrn);
				model.addAttribute("indentType", indentType);
				if(irs.isIndentEntryIdHasCreditNote(strIndentEntryId)){
					model.addAttribute("hasCreditNote", "true");
				}
				System.out.println("InvoiceNumber "+invoiceNumber);
				//--------------------
				
				if(toDate!=null && !toDate.equals("null")&& !toDate.equals("")){
					urlName=urlName.concat("&toDate="+toDate);
				}if(!urlName.equals("")){
					model.addAttribute("pageOutSide",true);
				}if(siteId!=null && !siteId.equals("null") && !siteId.equals("")){
					urlName=urlName.concat("&siteId="+siteId);
				}
				vendorName=GetInvoiceDetailsInward.get(0).getVendorName();
				strUrlName="getInvoiceDetails.spring?invoiceNumber="+invoiceNumber+"&vendorName="+vendorName+"&vendorId="+vendorId+"&IndentEntryId="+strIndentEntryId+
				"&SiteId="+siteId+"&invoiceDate="+invoiceDate+"&url="+urlName+"&indentType="+indentType;
				System.out.println("vendor name"+vendorName);
				model.addAttribute("url",strUrlName);
				model.addAttribute("urlName",urlName);
				String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
				String imgname = vendorName+"_"+invoiceNumber+"_"+strIndentEntryId;
				loadInvoiceImgAndPdfFiles(rootFilePath,imgname,siteId,model,request); // getting the images and pdf for showing purpose
				if(!imageClick){
					model.addAttribute("show",true);
					
				}
				
			} 
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		if(imageClick){
			return "GetInvoiceDetailsOnImageClick";
		}
		else{
			if(indentType.equals("INU")){
				return "GetMaterialAdjustedInvoiceDetails";
			}
			return "GetInvoiceDetails";
		}
		
	}

	@RequestMapping(value = "/inwardViewInvoiceDetails.spring", method = {RequestMethod.GET,RequestMethod.POST})
	public ModelAndView doInwardVIewInvoiceDetails(){

		ModelAndView model = null;

		try {
			model = new ModelAndView();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("ViewAllInvoices");
		}
		return model;
	}

	@RequestMapping(value = "/doInvoiceDetails.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public ModelAndView updateInvoiceDetails() {

		ModelAndView model = null;
		try {

			model = new ModelAndView();
		} catch (Exception ex) {

		}
		return model;
	}



	@RequestMapping(value = "/getGrnDetails.spring", method ={ RequestMethod.POST,RequestMethod.GET})
	public String getGrnDetails(HttpServletRequest request, HttpSession session,Model model){
		String invoiceNumber = "";
		String siteId = "";
		String viewToBeSelected = "";
		String strVendorId = "";
		String strUrlName="";
		String toDate="";
		try{
			invoiceNumber = request.getParameter("invoiceNumber") == null ? "" : request.getParameter("invoiceNumber");
			toDate=request.getParameter("toDate") == null ? "" : request.getParameter("toDate");
			String childProdName = request.getParameter("childProdName") == null ? "" : request.getParameter("childProdName");
			String monthYear = request.getParameter("monthYear")==null?"":request.getParameter("monthYear");
			String PriceMaster=request.getParameter("PriceMaster")==null?"":request.getParameter("PriceMaster");
			
			strUrlName =  request.getParameter("url");
			if(invoiceNumber.contains("@")){invoiceNumber=invoiceNumber.replace('@','&');}
			//strVendorId = request.getParameter("vendorId") == null ? "" : request.getParameter("vendorId");
			//siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
			//if(siteId.equals("998")){
				siteId = request.getParameter("siteId") == null ? "" : request.getParameter("siteId");
			//}
			if (StringUtils.isNotBlank(siteId) && StringUtils.isNotBlank(invoiceNumber)){
				model.addAttribute("columnHeadersMap", ResourceBundle.getBundle("validationproperties"));
				model.addAttribute("invoiceDetailsModelForm", new GetInvoiceDetailsBean());
				
				String response=inwardToGetInvoiceDetails.getGrnDetails(invoiceNumber, siteId,request);
				
				if(!toDate.equalsIgnoreCase("null") && toDate!=null && !toDate.equals("")){
					strUrlName=strUrlName.concat("&toDate="+toDate);
					//strUrlName="&toDate="+toDate;
				}if(!childProdName.equalsIgnoreCase("null") && childProdName!=null && !childProdName.equals("")){
					strUrlName=strUrlName.concat("&childProdName="+childProdName);
				}if(!monthYear.equalsIgnoreCase("null") && monthYear!=null && !monthYear.equals("")){
					strUrlName=strUrlName.concat("&monthYear="+monthYear);
				}if(!PriceMaster.equalsIgnoreCase("null") && PriceMaster!=null && !PriceMaster.equals("")){
					strUrlName=strUrlName.concat("&PriceMaster="+PriceMaster);
				}
				model.addAttribute("urlName",strUrlName);
				if(request.getParameter("fromGrnButtonsPage")!=null){
					model.addAttribute("urlName",request.getParameter("urlName"));
				}
				if(response.equalsIgnoreCase("Success")) {
					viewToBeSelected = "viewGRN";
				}
			}	
		}catch(Exception ex) {
			ex.printStackTrace();
		} 
	 return viewToBeSelected;
	}

	@RequestMapping(value = "/updateInvoice", method ={RequestMethod.POST,RequestMethod.GET})
	public String updateInvoice(@ModelAttribute("invoiceDetailsModelForm")IndentReceiveBean indentRecModel, BindingResult result, Model model, HttpServletRequest request, HttpSession session,@RequestParam(value="file",required = false) MultipartFile[] files) throws IOException  {
		
		boolean status=deleteInvoicePdfandImage(request);
		String indentEntryId=request.getParameter("indentEntryId")== null ? "" : request.getParameter("indentEntryId");
		String strIndentEntryId=String.valueOf(session.getAttribute("strIndentEntryId") ==null ? "" : session.getAttribute("strIndentEntryId"));
		String invoiceNumber = request.getParameter("invoiceNumber")== null ? "" : request.getParameter("invoiceNumber");
		String vendorName = request.getParameter("vendorName")== null ? "" : request.getParameter("vendorName");
		
		if((indentEntryId==null && invoiceNumber==null) || (indentEntryId.equals("") && invoiceNumber.equals(""))){
			model.addAttribute("message1","Oops !!! There was a improper request found.Please click on the sub-module and continue your Operation.");
			return "response";
		}
		String response = inwardToGetInvoiceDetails.updateInvoice(model, request, session);
		
		
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		
		String imgname=vendorName+"_"+invoiceNumber+"_"+indentEntryId;
		String viewToBeSelected = "";
		model.addAttribute("urlName","inwardGetInvoiceDetails.spring");
	
		imgname = imgname.replace("/","$$"); // here any invoice number contain the / then it will replace with $$
		String rootFilePath = validateParams.getProperty("INVOICE_IMAGE_PATH") == null ? "" : validateParams.getProperty("INVOICE_IMAGE_PATH").toString();
		//String imgname = vendorName+"_"+invoiceNumber+"_"+strIndentEntryId;
		//loadInvoiceImgAndPdfFiles(rootFilePath,imgname,site_id,model,request);
		
		int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent")); // here taking the active images presently taken 
		int pdfAlreadyPresent= Integer.parseInt(request.getParameter("pdfAlreadyPresent")); // here taken the active pdf presently
		int imgCount=imagesAlreadyPresent;
		int pdfCount=pdfAlreadyPresent;
		
		for (int i =0; i < files.length; i++) {
			
			MultipartFile multipartFile = files[i];
			if(!multipartFile.isEmpty()){
			try {
				//file directory name is constructing here 
					File dir = new File(rootFilePath+site_id);
				//checking is the directory exits or not if not create the directory for string images
					if (!dir.exists())
						dir.mkdirs();
					
					String filePath ="";
					System.out.println(multipartFile.getOriginalFilename());
					//checking the extension of file and creating the file based on extension 
					if(multipartFile.getOriginalFilename().endsWith(".pdf")){
						filePath = dir.getAbsolutePath()+ File.separator + imgname+"_Part"+pdfCount+".pdf";
						pdfCount++;
					}else{
						filePath = dir.getAbsolutePath()+ File.separator + imgname+"_Part"+imgCount+".jpg";
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
					model.addAttribute("message1", "invoice updated Successfully. But Image NOT Uploaded");
					request.setAttribute("message1","invoice updated Successfully. But Image NOT Uploaded");
				}
			}
		}//For Loop
			
		if(response.equalsIgnoreCase("Success")) {

			viewToBeSelected = "viewGRN";
		}
		else if(response.equalsIgnoreCase("Failed")){
			viewToBeSelected = "indentReceiveResponse";
		} else if (response.equalsIgnoreCase("SessionFailed")){
			request.setAttribute("Message", "Session Expired, Please Login Again");
			viewToBeSelected = "index";
		}else if(response.equalsIgnoreCase("response")){
			viewToBeSelected = "response";
		}


		return viewToBeSelected;
	}


	@RequestMapping(value ="getGrnViewDts.spring", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView getGrnViewDts(HttpServletRequest request,HttpSession session) {

		String site_id = "";
		String toDate = "";
		String fromDate = "";
		String response="";
		ModelAndView model = null;
		List<ViewIndentIssueDetailsBean> indentIssueData = null;
		try {
			model = new ModelAndView();
			fromDate = request.getParameter("fromDate");
			toDate = request.getParameter("toDate");
			if (StringUtils.isNotBlank(fromDate) || StringUtils.isNotBlank(toDate)) {
				 session = request.getSession(false);
				site_id = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();				
				System.out.println("From Date "+fromDate +"To Date "+toDate +"Site Id "+site_id);
				if (StringUtils.isNotBlank(site_id)) {
					indentIssueData = inwardToGetInvoiceDetails.getGrnViewDetails(fromDate, toDate, site_id);
					if(indentIssueData != null && indentIssueData.size() >0){
						request.setAttribute("showGrid", "true");
						response="success";
					} else {
						model.addObject("succMessage","The above Dates Data Not Available");
						response="failure";
						
					}
					model.addObject("indentIssueData",indentIssueData);
					model.addObject("fromDate",fromDate);
					model.addObject("toDate", toDate);
					model.addObject("url","getGrnViewDts.spring?");
					model.setViewName("reports/ViewAllGRN");

				} else {
					model.addObject("Message","Session Expired, Please Login Again");
					model.setViewName("index");
					response="failure";
					return model;
				}
			} else {
				model.addObject("displayErrMsg", "Please Select From Date or To Date!");
				model.addObject("indentIssueData",indentIssueData);
				model.addObject("fromDate",fromDate);
				model.addObject("toDate", toDate);
				response="success";
				model.setViewName("reports/ViewAllGRN");
			}
		} catch (Exception ex) {
			response="failure";
			ex.printStackTrace();
		} 
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Get Grn Details View",response,site_id1);
			
		return model;
	}
	
	/******************************************************to get the images and pdf for show purpose **************************************************/
	public static void loadInvoiceImgAndPdfFiles(String rootFilePath,String imgname,String siteId,Model model, HttpServletRequest request){
		try{
		int imageCount = 0;
		int pdfCount=0;
	
		DataInputStream dis=null;
	
		int getLocalPort=request.getLocalPort();
		String strContextAndPort = "";
		String path = "";
		if(getLocalPort == 8086){ //Local
			strContextAndPort = UIProperties.validateParams.getProperty("UAT_IP_PORT");
		}else if(getLocalPort == 8078){ //local machine
			strContextAndPort = UIProperties.validateParams.getProperty("UAT_IP_PORT");
		}else if(getLocalPort == 8079){ //CUG
			strContextAndPort = UIProperties.validateParams.getProperty("CUG_IP_PORT");
		}else if(getLocalPort == 80){ //LIVE
			strContextAndPort = UIProperties.validateParams.getProperty("LIVE_IP_PORT");
		}

		imgname = imgname.replace("/","$$"); // if the invoice number contain the '/' then it will replacxe with $$
		
		for(int i=0;i<8;i++){
			File dir = new File(rootFilePath+siteId+"/"+siteId);
			
			File imageFile = new File(rootFilePath+siteId+"/"+imgname+"_Part"+i+".jpg");
			//String pdfFileName=rootFilePath+siteId+"\\"+siteWiseNO+"/"+siteWiseNO+"_Part"+i+".pdf";
			File pdfFile = new File(rootFilePath+siteId+"/"+imgname+"_Part"+i+".pdf");
			
			//if pdf is exists the pdf will load and converted into the Base64 object and it will add in model object so we can carry this images and pdf so we can show in view page
			if(pdfFile.exists()){
				pdfCount++;
				try {
					String pathForDeleteFile = pdfFile.getAbsolutePath().replace("\\", "@@"); // here in jsp delete function call time // not taken so replace to @@
					path=strContextAndPort+siteId+"/"+imgname+"_Part"+i+".pdf";
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
						path=strContextAndPort+siteId+"/"+imgname+"_Part"+i+".jpg";
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
	
	/************************************************************this is used to delete and add the images and pdf start**********************************/
	/**
	 * @description this method is used for delete the images by using the path of image
	 * 
	 */
		@RequestMapping(value = "/deleteInvoicePermanentImage.spring", method = RequestMethod.GET)
		public @ResponseBody static boolean deleteInvoicePdfandImage( HttpServletRequest request) {
			
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
