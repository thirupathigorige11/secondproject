package com.sumadhura.in;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.sumadhura.util.CommonUtilities;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.transdao.UtilDao;
import com.sumadhura.transdao.VendorDao;
import com.sumadhura.util.SaveAuditLogDetails;
import com.sumadhura.util.UIProperties;

@Controller
public class VendorController extends UIProperties{
	@Autowired
	private VendorDao dao;

	@Autowired
	private UtilDao utilDao;

	@RequestMapping(value = "/AddVendor.spring")
	public ModelAndView showform(HttpServletRequest request,HttpSession session) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("AddVendor");
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		// HttpSession session=request.getSession(true);  
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Vendor Master Viewed","success",site_id1);
		
			return mav;
	}
List<VendorDetails> vendorList=new ArrayList<VendorDetails>();
	@RequestMapping(value = "/savevendor.spring", method = RequestMethod.POST)
	public ModelAndView save(HttpServletRequest request,HttpSession session, @RequestParam("file") MultipartFile[] files) throws Exception {

		ModelAndView model = new ModelAndView();
		System.out.println("controler");
		String strVendorName = "";
		String straddress = "";
		String strmobilenumber = "";
		String strgsinnumber = "";
		String strvendorstate = "";
		String strvendoremail = "";
		String landline = "";
		String statecode = "";
		String vendorcontactperson = "";
		String response="";
		String vendorContactPerson_2="";
		String mobile_number_2="";
		String type_Of_Business="";
		String nature_Of_Business="";
		String Pan_number="";
		String bank_Name="";
		String acc_Type="";
		String acc_Holder_Name="";
		long acc_Number=0;
		String ifsc_Code="";
		String strResponse="";
		String manfuturer="";
		String authorizedAgent="";
		String trader="";
		String consultingCompany="";
		String other="";
		String gstinNumber="";
		String tmpAccountNumber="";
		int intCount = 0;
		/************************************************for newly added fields*********************************************************/
		String parentCompany="";
		String overseasesRepresentative="";
		String establishedYear="";
		String noOfEmployees="";
		String PFRegNo="";
		String EsiNo="";
		String totalSalesForLastThreeYears="";
		String branchName="";
		String bankcity="";
		String bankState="";
		String listOfWorkCompleted="";
		String majorCustomer="";
		String whetherISO="";
		String otherInformation="";
		String date="";
		String filePath ="";
		int portNumber=request.getLocalPort();
		String rootFilePath ="";
		if(portNumber==80){rootFilePath=validateParams.getProperty("VENDOR_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_IMAGE_PATH").toString();}else{
			rootFilePath=validateParams.getProperty("VENDOR_CUG_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_CUG_IMAGE_PATH").toString();	
		}
		try {
		String strServiceType = request.getParameter("ServiceType");
		if (StringUtils.isNotBlank(strServiceType)) {
			
			if (strServiceType.equals("add")) {
				vendorList=null;
				vendorList=new ArrayList<VendorDetails>();
				strVendorName = request.getParameter("vendor");
				 straddress = request.getParameter("address");
				 strmobilenumber = request.getParameter("mobilenumber");
				 strgsinnumber = request.getParameter("gsinnumber");
				 strvendorstate = request.getParameter("state");
				 strvendoremail = request.getParameter("email");
				 landline = request.getParameter("landnumber");
				 statecode = request.getParameter("statecode");
				 vendorcontactperson = request.getParameter("vendorContactPerson");
				 vendorContactPerson_2 = request.getParameter("vendorContactPerson2");
				 type_Of_Business = request.getParameter("typeOfBusiness");
				 authorizedAgent= request.getParameter("Authorized")==null ? "":request.getParameter("Authorized").toString();
				 trader= request.getParameter("Trader")==null ? "":request.getParameter("Trader").toString();
				 consultingCompany= request.getParameter("Consulting")==null ? "":request.getParameter("Consulting").toString();
				 other= request.getParameter("natureOfBusiness")==null ? "":request.getParameter("natureOfBusiness").toString();
				 
				 mobile_number_2 = request.getParameter("vendorMobileNumber2");
				 manfuturer = request.getParameter("Manufacturer")==null ? "":request.getParameter("Manufacturer").toString();
				 bank_Name = request.getParameter("bankName");
				 acc_Type = request.getParameter("accountType");
				 acc_Holder_Name = request.getParameter("accountHolderName");
				 acc_Number=Long.parseLong(request.getParameter("accountNumber"));
				// acc_Number =Long.parseLong(tmpAccountNumber);
				 ifsc_Code=request.getParameter("ifscCode");
				 Pan_number=request.getParameter("panNumber");
				/* ******************************************************newl added fields********************************************************/
				 	 parentCompany=request.getParameter("parentCompany")==null ? "":request.getParameter("parentCompany").toString();
					 overseasesRepresentative=request.getParameter("overseases")==null ? "":request.getParameter("overseases").toString();
					 establishedYear=request.getParameter("yearEstablish")==null ? "":request.getParameter("yearEstablish").toString();
					 noOfEmployees=request.getParameter("noOfEmployees")==null ? "":request.getParameter("noOfEmployees").toString();
					 PFRegNo=request.getParameter("pfNo")==null ? "":request.getParameter("pfNo").toString();
					 EsiNo=request.getParameter("ESiNo")==null ? "":request.getParameter("ESiNo").toString();
					 totalSalesForLastThreeYears=request.getParameter("totalsales")==null ? "":request.getParameter("totalsales").toString();
					 branchName=request.getParameter("branchName")==null ? "":request.getParameter("branchName").toString();
					 bankcity=request.getParameter("bankCity")==null ? "":request.getParameter("bankCity").toString();
					 bankState=request.getParameter("bankState")==null ? "":request.getParameter("bankState").toString();
					 listOfWorkCompleted=request.getParameter("listOfWork")==null ? "":request.getParameter("listOfWork").toString();
					 majorCustomer=request.getParameter("majorCustomers")==null ? "":request.getParameter("majorCustomers").toString();
					 whetherISO=request.getParameter("ISOcertified")==null ? "":request.getParameter("ISOcertified").toString();
					 otherInformation=request.getParameter("OtherInfo")==null ? "":request.getParameter("OtherInfo").toString();
					 date=request.getParameter("date")==null ? "":request.getParameter("date").toString();
					 /* ****************************************************************newly added fields****************************************************/
				 
				 
				 nature_Of_Business = authorizedAgent +" "+trader +" "+consultingCompany +" "+manfuturer +" "+other;
				/* if(nature_Of_Business.contains(" ")){
					 nature_Of_Business=nature_Of_Business.replace(" ",",");
				 
				 }*/
				 VendorDetails vd = new VendorDetails();
				 vd.setAddress(straddress);
				 vd.setMobile_number(strmobilenumber);
				 vd.setGsin_number(strgsinnumber);
				 vd.setState_code(statecode);
				 vd.setVendor_state(strvendorstate);
				 vd.setVendor_email(strvendoremail);
				 vd.setVendorcontact_person(vendorcontactperson);
				 vd.setLandline(landline);
				 vd.setVendor_Contact_Person_2(vendorContactPerson_2);
				 vd.setMobile_Number_2(mobile_number_2);
				 vd.setAcc_Holder_Name(acc_Holder_Name);
				 vd.setAcc_Number(acc_Number);
				 vd.setAcc_Type(acc_Type);
				 vd.setBank_name(bank_Name);
				 vd.setType_Of_Business(type_Of_Business);
				 vd.setNature_Of_Business(nature_Of_Business);
				 vd.setVendor_name(strVendorName);
				 vd.setPan_Number(Pan_number);
				
				 vd.setIfsc_Code(ifsc_Code);
				 
				 /***************************************************************newly Added ones start*********************************************************/
				 vd.setParentCompany(parentCompany);
				 vd.setOverseasesRepresentative(overseasesRepresentative);
				 vd.setEstablishedYear(establishedYear);
				 vd.setNoOfEmployees(noOfEmployees);
				 vd.setPFRegNo(PFRegNo);
				 vd.setEsiNo(EsiNo);
				 vd.setTotalSalesForLastThreeYears(totalSalesForLastThreeYears);
				 vd.setBranchName(branchName);
				 vd.setBankcity(bankcity);
				 vd.setBankState(bankState);
				 vd.setListOfWorkCompleted(listOfWorkCompleted);
				 vd.setMajorCustomer(majorCustomer);
				 vd.setWhetherISO(whetherISO);
				 vd.setOtherInformation(otherInformation);
				 vd.setDate(date);
				 /***************************************************************newly Added ones start end*********************************************************/
				 gstinNumber=dao.checkGstinNumber(strgsinnumber);
				 strResponse=dao.checkMandatoryData(vd);
	 
	
				 if (strResponse.equalsIgnoreCase("true") && gstinNumber.equalsIgnoreCase("false")) {
					
					 int getVendorId = dao.getVendorSeqNo("vendor");
					String strVendorId = "VND" + getVendorId;
					 vd.setVendor_Id(strVendorId);
					 
					
					
					 System.out.println("strVendor name " + strVendorName);
						System.out.println("strVendor Id " + strVendorId);
						int existCount =0;// dao.checkVendorAvailableOrNot(strVendorName);
						
					if (existCount == 0) {
						intCount = dao.save(strVendorName,vd);
						if (intCount == 1) {
							for (int i = 0,j=0; i < files.length; i++) {
								MultipartFile multipartFile = files[i];
								if(!multipartFile.isEmpty()){
								try {
										//String rootFilePath = validateParams.getProperty("VENDOR_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_IMAGE_PATH").toString();
										File dir = new File(rootFilePath+strVendorId);
										if (!dir.exists())
											dir.mkdirs();
										if(multipartFile.getOriginalFilename().endsWith(".pdf")){
											 filePath = dir.getAbsolutePath()+ File.separator + strVendorId+"_Part"+j+".pdf"; 
											 j++;
										}else{
											filePath = dir.getAbsolutePath()+ File.separator + strVendorId+"_Part"+j+".jpg"; 
											j++;
										}
										/*String filePath = dir.getAbsolutePath()
										+ File.separator + strVendorId+"_Part"+j+".jpg"; */
										
										File convFile = new File(filePath);
									    convFile.createNewFile(); 
									    FileOutputStream fos = new FileOutputStream(convFile); 
									    fos.write(multipartFile.getBytes());
									    fos.close(); 
										/*if(!multipartFile.isEmpty()){
											multipartFile.transferTo(new File(filePath));
											
										}*/
										
										

										System.out.println("Image Uploaded");
										//return "You successfully uploaded file" ;
									} catch (Exception e) {
										System.out.println("Image NOT Uploaded");
										//return "You failed to upload " ;
									}
								

							}  

	
							}
							
							model.addObject("message", "Vendor added successfully");
							response="success";
						} else if (intCount == 9) {
							model.addObject("message1", "Vendor Name Already Exist in Database");
							response="failure";
						} else {
							model.addObject("message1", "Vendor adding failed");
							response="failure";
						}
					} else {
						model.addObject("message1", "Vendor Name was already Added, Please try with Another Vendor Name");
						response="failure";
					
					}
					
				 } else {
					 model.addObject("message1", "All Fileds are Mandatory");
				 }
			} else if (strServiceType.equals("delete")) {
				vendorList=null;
				vendorList=new ArrayList<VendorDetails>();
				strVendorName = request.getParameter("VendarName_Delete");
				intCount = dao.deletevendor(strVendorName);
				if (intCount > 0) {
					model.addObject("message", "Vendor deleted successfully");
					
					response="failure";
					
					
				} else {
					model.addObject("message1", "Vendor deleting failed");
					response="failure";
				}
			}
		} else {
			model.addObject("message1", "ServiceType not empty.");
		}
		}catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			model.setViewName("response");
		}
		
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Vendor Master clicked submit",response,site_id1);
			
		
		
		
		
		return model;
	}

	@RequestMapping("/ViewVendor.spring")
	public @ResponseBody List<VendorDetails> getAllVendorsList(HttpServletRequest request,HttpSession session,Model model) {

		System.out.println("get Total vendors...");
		String strVendorName = request.getParameter("vendor");
		List<VendorDetails> listAllVendorsList = new ArrayList<VendorDetails>();
		try {
			//Checking vendorList is empty or not if not empty return existing object
			if(vendorList.size()>0){
				System.out.println("List is not empty "+vendorList.size());
				//for avoiding thread inconsistency
				synchronized (this) {
					return vendorList;
				}
			}
			listAllVendorsList = dao.getAllVendors();
			vendorList=listAllVendorsList;
			System.out.println("get All vendors List..."+ listAllVendorsList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Vendor Master Viewed Details","success",site_id1);
			
		return listAllVendorsList;
	}


	@RequestMapping("/viewVendorMaster.spring")
	public ModelAndView getAllVendorsListForView(HttpServletRequest request, HttpSession session, Model model) {
		ModelAndView mav = new ModelAndView();
		System.out.println("VendorController.getAllVendorsList()");
		String strVendorName = request.getParameter("vendor");
		List<VendorDetails> listAllVendorsList = new ArrayList<VendorDetails>();
		try {
			listAllVendorsList = dao.getAllVendors();
			System.out.println("get All vendors List..." + listAllVendorsList.size());
			mav.addObject("isVendorDetailEditable", "false");
			mav.addObject("AllVendorsList", listAllVendorsList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails audit = new SaveAuditLogDetails();
		// String
		// indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
		String user_id = String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0", user_id, "Vendor Master Viewed Details", "success", site_id1);
		mav.setViewName("ShowAllVendors");
		return mav;
	}

	@RequestMapping("/autoSearchvendor.spring")
	public @ResponseBody String getAutoSearchVendorNames(
			@RequestParam(value = "term") String term) {
		System.out.println("get Total Products...");
		List<String> listAllVendorsList = new ArrayList<String>();
		String searchvendorList = "";
		try {
			listAllVendorsList = utilDao.getVendorsBySearch(term);
			searchvendorList = new Gson().toJson(listAllVendorsList);
			System.out.println("get Products..." + listAllVendorsList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return searchvendorList;
	}
	@RequestMapping("/getVendorDetails.spring")
	public String getVendorsList(HttpServletRequest request,HttpSession session,Model model) {

		System.out.println("get Total vendors...");
		String vendorId=request.getParameter("vendor_Id");
		String isVendorDetailEditable=request.getParameter("edt");
		request.setAttribute("isVendorDetailEditable", isVendorDetailEditable);
		List<VendorDetails> listAllVendorsList = new ArrayList<VendorDetails>();
		try {
			listAllVendorsList = dao.getAllVendorsList(vendorId);
			int count = 0;
			loadVendorImgAndPdfFiles(vendorId,model, request);
			
			/*for(int i=0;i<8;i++){
				String rootFilePath = validateParams.getProperty("VENDOR_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_IMAGE_PATH").toString();
				File f = new File(rootFilePath+vendorId+"/"+vendorId+"_Part"+i+".jpg");

				if(f.exists()){
					count++;
					DataInputStream dis = new DataInputStream(new FileInputStream(f));
					byte[] barray = new byte[(int) f.length()];
					dis.readFully(barray); 
					byte[] encodeBase64 = Base64.encodeBase64(barray);
					String base64Encoded = new String(encodeBase64, "UTF-8");
					model.addAttribute("image"+i,base64Encoded);
				}
			}
			request.setAttribute("imagecount", count);*/
			String kiloByte=validateParams.getProperty("KILO_BYTE") == null ? "" : validateParams.getProperty("KILO_BYTE").toString();
			model.addAttribute("KILO_BYTE",kiloByte);
			
			model.addAttribute("listAllVendorsList",listAllVendorsList);
			System.out.println("get All vendors List..."
					+ listAllVendorsList.size());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaveAuditLogDetails audit=new SaveAuditLogDetails();
		//	String indentEntrySeqNum=session.getAttribute("indentEntrySeqNum").toString();
			String user_id=String.valueOf(session.getAttribute("UserId"));
			String site_id1 = String.valueOf(session.getAttribute("SiteId"));
			audit.auditLog("0",user_id,"Vendor Master Viewed Details","success",site_id1);
			
		return "ViewAllVendors";
	}


@RequestMapping("/editAndSaveVendorDetails.spring")
public ModelAndView editSaveVendorDetails(HttpServletRequest request,HttpSession session, @RequestParam("file") MultipartFile[] files) {
System.out.println("VendorController.editSaveVendorDetails()");
	String response = "";
	int result =0;
	int noOfRecords=0;
	String filePath ="";
	ModelAndView model = new ModelAndView();
	System.out.println("Edit & Save vendor...");
	String vendorId=request.getParameter("vendorId");
	String rootFilePath ="";
	int portNumber=request.getLocalPort();
	String movePath="";
	if(portNumber==80){rootFilePath=validateParams.getProperty("VENDOR_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_IMAGE_PATH").toString();
	movePath=validateParams.getProperty("VENDOR_MOVE_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_MOVE_IMAGE_PATH").toString();
	}else{
		movePath=validateParams.getProperty("VENDOR_CUG_MOVE_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_CUG_MOVE_IMAGE_PATH").toString();
		rootFilePath=validateParams.getProperty("VENDOR_CUG_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_CUG_IMAGE_PATH").toString();	
	}
	//String rootFilePath = validateParams.getProperty("VENDOR_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_IMAGE_PATH").toString();
	
	String vendorName=request.getParameter("vendorName");
	String gsinNumber=request.getParameter("gsinNumber");
	String vendorState=request.getParameter("vendorState");
	String vendorEmail=request.getParameter("vendorEmail");
	String landline=request.getParameter("landline");
	String stateCode=request.getParameter("stateCode");
	String contactPerson=request.getParameter("contactPerson");
	String mobileNumber=request.getParameter("mobileNumber");
	String address=request.getParameter("address");
	
	
	String vendorContactPerson_2 = request.getParameter("vendorContactPerson_2");
	String type_Of_Business = request.getParameter("typeOfBusiness");
	String mobile_number_2 = request.getParameter("mobileNumber_2");
	String nature_Of_Business = request.getParameter("natureOfBusiness");
	String bank_Name = request.getParameter("bankName");
	String acc_Type = request.getParameter("accountType");
	String acc_Holder_Name = request.getParameter("accountHolderName");
	long acc_Number = Long.parseLong(request.getParameter("bankAccountNumber"));
	 String ifsc_Code=request.getParameter("ifscCode");
	 String panNumber=request.getParameter("panNumber");
	 String strGsinNumber=request.getParameter("strGsinNumber");
	 String responseGstin="";
	/* *****************************************************newly added start******************************************************************/
	 	String parentCompany=request.getParameter("parentCompany");
		String overseasesRepresentative=request.getParameter("OverSeases");
		String establishedYear=request.getParameter("EstablishYear");
		String noOfEmployees=request.getParameter("noOfEmployees");
		String PFRegNo=request.getParameter("pfNo");
		String EsiNo=request.getParameter("esino");
		String totalSalesForLastThreeYears=request.getParameter("totalSales");
		String branchName=request.getParameter("branchName");
		String bankcity=request.getParameter("branchCity");
		String bankState=request.getParameter("bankState");
		String listOfWorkCompleted=request.getParameter("ListOfWorkCompleted");
		String majorCustomer=request.getParameter("majorCustomer");
		String whetherISO=request.getParameter("iso");
		String otherInformation=request.getParameter("OtherInfo");
		String date=request.getParameter("date");
	 
	 
	
	VendorDetails vd = new VendorDetails();
	vd.setVendor_Id(vendorId);
	vd.setVendor_name(vendorName);
	vd.setGsin_number(gsinNumber);
	vd.setVendor_state(vendorState);
	vd.setVendor_email(vendorEmail);
	vd.setLandline(landline);
	vd.setState_code(stateCode);
	vd.setVendorcontact_person(contactPerson);
	vd.setMobile_number(mobileNumber);
	vd.setAddress(address);
	
	 vd.setVendor_Contact_Person_2(vendorContactPerson_2);
	 vd.setMobile_Number_2(mobile_number_2);
	 vd.setAcc_Holder_Name(acc_Holder_Name);
	 vd.setAcc_Number(acc_Number);
	 vd.setAcc_Type(acc_Type);
	 vd.setBank_name(bank_Name);
	 vd.setType_Of_Business(type_Of_Business);
	 vd.setNature_Of_Business(nature_Of_Business);
	 vd.setIfsc_Code(ifsc_Code);
	 vd.setPan_Number(panNumber);
	
	 vd.setParentCompany(parentCompany);
	 vd.setOverseasesRepresentative(overseasesRepresentative);
	 vd.setEstablishedYear(establishedYear);
	 vd.setNoOfEmployees(noOfEmployees);
	 vd.setPFRegNo(PFRegNo);
	 vd.setEsiNo(EsiNo);
	 vd.setTotalSalesForLastThreeYears(totalSalesForLastThreeYears);
	 vd.setBranchName(branchName);
	 vd.setBankcity(bankcity);
	 vd.setBankState(bankState);
	 vd.setListOfWorkCompleted(listOfWorkCompleted);
	 vd.setMajorCustomer(majorCustomer);
	 vd.setWhetherISO(whetherISO);
	 vd.setOtherInformation(otherInformation);
	 vd.setDate(date);
	 vendorList=null;
		//making VendorList size 0 to load again changes
			vendorList=new ArrayList<VendorDetails>();
	/* if(!strGsinNumber.equals(gsinNumber)){
		responseGstin=dao.checkGstinNumber(gsinNumber);
		if(responseGstin.equalsIgnoreCase("false")){result = dao.editAndSaveVendor(vd);}
	  } *///this is for edit purpose}
	 //else{
	  result = dao.editAndSaveVendor(vd);
	if (result > 0) {
		model.addObject("message", "Vendor Saved successfully");
		 
		response="success";
		
		int imagesAlreadyPresent = Integer.parseInt(request.getParameter("imagesAlreadyPresent"));
		try{
			
			/*	**********************************************delete the pdf and images start****************************************************************/
			for(int k=0;k<8;k++){
				String imgOrPdfNumber=request.getParameter("deletePdf"+k);	
			            File file1 = new File(rootFilePath+vendorId+"//"+vendorId+"_part"+imgOrPdfNumber+".pdf");
			            File file2 = new File(rootFilePath+vendorId+"//"+vendorId+"_part"+imgOrPdfNumber+".jpg");
			            if(file1.exists()){
			                if(file1.delete()){
			                    System.out.println(file1.getName() + " Was deleted!");
			                    response="success";
			                }
					}if(file2.exists()){
						if(file2.delete()){
		                    //System.out.println(file1.getName() + " Was deleted!");
		                    response="success";
		                }
					}
					}
			/*	**********************************************delete the pdf and images end****************************************************************/
			/*	**********************************************move the pdf and images to change the pdf or image number start****************************************************************/	
			for (int i = 0,j=0; i < 8; i++) {
				File imgFile = new File(rootFilePath+vendorId+"//"+vendorId+"_part"+i+".jpg");
				File pdfFile = new File(rootFilePath+vendorId+"//"+vendorId+"_part"+i+".pdf");
				//long count1=file.length();
				if(pdfFile.exists()){
					Files.move(Paths.get(movePath+"\\"+vendorId+"\\"+vendorId+"_Part"+i+".pdf"),Paths.get(movePath+"\\"+vendorId+"\\"+vendorId+"_Part"+noOfRecords+".pdf"));
				noOfRecords++;
				}if(imgFile.exists()){
					Files.move(Paths.get(movePath+"\\"+vendorId+"\\"+vendorId+"_Part"+i+".jpg"),Paths.get(movePath+"\\"+vendorId+"\\"+vendorId+"_Part"+noOfRecords+".jpg"));
					noOfRecords++;
				}
				}
			/*	**********************************************move the pdf and images to change the pdf or image number end****************************************************************/
		for (int i = 0,j=noOfRecords; i < files.length; i++) {
			MultipartFile multipartFile = files[i];
			if(!multipartFile.isEmpty()){
				//String rootFilePath = validateParams.getProperty("VENDOR_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_IMAGE_PATH").toString();
				File dir = new File(rootFilePath+vendorId);
				if (!dir.exists())
					dir.mkdirs();
				if(multipartFile.getOriginalFilename().endsWith(".pdf")){
					 filePath = dir.getAbsolutePath()+ File.separator + vendorId+"_Part"+j+".pdf"; 
					 j++;
				}else{
					filePath = dir.getAbsolutePath()+ File.separator + vendorId+"_Part"+j+".jpg"; 
					j++;
				}
				/*String filePath = dir.getAbsolutePath()
				+ File.separator + strVendorId+"_Part"+j+".jpg"; */
				
				File convFile = new File(filePath);
			    convFile.createNewFile(); 
			    FileOutputStream fos = new FileOutputStream(convFile); 
			    fos.write(multipartFile.getBytes());
			    fos.close(); 
				
			}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
			
	} else {
		model.addObject("message1", "Vendor Saving failed");
		response="failure";
	}
	SaveAuditLogDetails audit=new SaveAuditLogDetails();
		String user_id=String.valueOf(session.getAttribute("UserId"));
		String site_id1 = String.valueOf(session.getAttribute("SiteId"));
		audit.auditLog("0",user_id,"Vendor Master Edit & Save Details",response,site_id1);
		
		model.setViewName("response");
		return model;
}

@RequestMapping("/ViewAllProductList.spring")
public String  getAllProductDetailsList(HttpServletRequest request,HttpSession session,Model model) {

	 List<Map<String,Object>> listAllProductList = new ArrayList<Map<String,Object>>();
	
	try {
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		listAllProductList = dao.getAllProductDetails(site_id,"");
	
		model.addAttribute("allProductlist", listAllProductList);
		request.setAttribute("siteWise",true);
		System.out.println("get All product List..."
				+ listAllProductList.size());
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return "ViewAllProductList";
	
}

/***********************************************View all products Details List start***************************************************/
@RequestMapping("/ViewAllProductDetailsList.spring")
public String  ViewAllProductDetailsList(HttpServletRequest request,HttpSession session,Model model) {

	 List<Map<String,Object>> listAllProductList = new ArrayList<Map<String,Object>>();
	
	try {
		String site_id = String.valueOf(session.getAttribute("SiteId"));
		listAllProductList = dao.getAllProductDetails(site_id,"ALL");
	
		model.addAttribute("allProductlist", listAllProductList);
		request.setAttribute("all",true);
		System.out.println("get All product List..."
				+ listAllProductList.size());
	} catch (Exception e) {
		e.printStackTrace();
	}
	
	return "ViewAllProductList";
	
}
/*****************************************************view All Products Details end***********************************************************/

@RequestMapping(value = "/vendor_registration.spring")
public ModelAndView Vendor_Registration(HttpServletRequest request,HttpSession session,Model model) {
	ModelAndView mav = new ModelAndView();
	String kiloByte=validateParams.getProperty("KILO_BYTE") == null ? "" : validateParams.getProperty("KILO_BYTE").toString();
	model.addAttribute("KILO_BYTE",kiloByte);
	mav.setViewName("Vendor_Registration");
	
	return mav;
}



@RequestMapping(value = "/getGstinNumber", method = RequestMethod.POST)
@ResponseBody
public String getGstinNumber(@RequestParam("GstiNumber") String strGstinNumber) {
	
	String response="";
	String strValue="";
	strValue=dao.checkGstinNumber(strGstinNumber);
	if(strValue.equalsIgnoreCase("true")){
		response="true";
		
	}else{
		response="false";
	}
	
	return response;
}

/*****************************************************for vendor Name**************************************************************************
*/
@RequestMapping(value = "/validateCompanyName", method = RequestMethod.POST)
@ResponseBody
public String validateCompanyName(@RequestParam("vendor") String vendor) {
	
	String response="";
	String strValue="";
	strValue=dao.checkVendorName(vendor);
	if(strValue.equalsIgnoreCase("true")){
		response="true";
		
	}else{
		response="false";
	}
	
	return response;
}
/******************************************************pdf and images download purpose start*****************************************************************/
public static void loadVendorImgAndPdfFiles(String vendorId, Model model, HttpServletRequest request){
	try{
	int imageCount = 0;
	int pdfCount=0;
	//System.out.println("WorkOrderController.loadWOImgAndPdfFiles()");
	DataInputStream dis=null;
	String rootFilePath ="";
	int portNumber=request.getLocalPort();
	if(portNumber==80){rootFilePath=validateParams.getProperty("VENDOR_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_IMAGE_PATH").toString();}else{
		rootFilePath=validateParams.getProperty("VENDOR_CUG_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_CUG_IMAGE_PATH").toString();	
	}
	//String rootFilePath = UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH") == null ? "" : UIProperties.validateParams.getProperty("UPDATE_WORKORDER_IMAGE_PATH").toString();
	//String rootFilePath = validateParams.getProperty("VENDOR_IMAGE_PATH") == null ? "" : validateParams.getProperty("VENDOR_IMAGE_PATH").toString();
	for(int i=0;i<8;i++){
		//File dir = new File(rootFilePath+siteId+"\\"+siteWiseNO);
		File f = new File(rootFilePath+vendorId+"/"+vendorId+"_Part"+i+".jpg");
		File imageFile = new File(rootFilePath+vendorId+"/"+vendorId+"_Part"+i+".jpg");
		String pdfFileName=rootFilePath+vendorId+"/"+vendorId+"_Part"+i+".pdf";
		File pdfFile = new File(rootFilePath+vendorId+"/"+vendorId+"_Part"+i+".pdf");
		pdfFileName=pdfFile.toString();
		if(pdfFile.exists()){
			System.out.println(pdfFile);
			pdfCount++;
			try {
				dis = new DataInputStream(new FileInputStream(pdfFile));
				String pathForDeleteFile = pdfFile.getAbsolutePath().replace("\\", "$");
				byte[] barray = new byte[(int) pdfFile.length()];
				dis.readFully(barray);
				byte[] encodeBase64 = Base64.encodeBase64(barray);
				String base64Encoded = new String(encodeBase64, "UTF-8");
				
				String encoded=CommonUtilities.encodeFileToBase64Binary(pdfFileName);
				model.addAttribute("pdf"+i,encoded);
				//model.addAttribute("pdf" + i, base64Encoded);
				model.addAttribute("PathdeletePdf" + i, pathForDeleteFile);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					dis.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		}
	
		if(imageFile.exists()){
			System.out.println("f.getName() "+imageFile.getName());
			imageCount++;
			try {
					dis = new DataInputStream(new FileInputStream(imageFile));
					String pathForDeleteFile = imageFile.getAbsolutePath().replace("\\", "$");
					byte[] barray = new byte[(int) imageFile.length()];
					dis.readFully(barray);
					byte[] encodeBase64 = Base64.encodeBase64(barray);
					String base64Encoded = new String(encodeBase64, "UTF-8");
					model.addAttribute("image" + i, base64Encoded);
					model.addAttribute("delete" + i, pathForDeleteFile);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						dis.close();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
				}
			
		}
	}
	request.setAttribute("imagecount", imageCount);	
	request.setAttribute("pdfcount", pdfCount);	
	
	} catch (Exception e) {
		e.printStackTrace();
	}
}

@RequestMapping(value = "/deleteVendor", method = RequestMethod.POST)
@ResponseBody
public String deleteVendor(@RequestParam("VendarName_Delete") String vendorName,HttpServletRequest request) {
	vendorList=null;
	vendorList=new ArrayList<VendorDetails>();
	String response="";	 
	//request.setAttribute("succMessage", "Vendor deleted successfully");
	int intCount = dao.deletevendor(vendorName);
	if (intCount > 0) {
		response="true";

	} else {
		response="false";
		
	}
	return response;
}


}
