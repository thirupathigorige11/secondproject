package com.sumadhura.in;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;


import com.sumadhura.bean.userDetails;
import com.sumadhura.service.EmailFunction;
import com.sumadhura.service.WriteTrHistory;
import com.sumadhura.transdao.LoginDao;
import com.sumadhura.util.SaltPassword;


@Controller
public class LoginController implements org.springframework.web.servlet.mvc.Controller {

	@Autowired
	private LoginDao dao;
	@Autowired
	PlatformTransactionManager transactionManager;



	@Override

	@RequestMapping("/login.spring")
	public ModelAndView handleRequest(HttpServletRequest requset, HttpServletResponse response) throws Exception {
	//	System.out.println("login confirm "+requset.getParameter("IndentApproval"));
		String indentNumber = requset.getParameter("IndentApproval");
		String uname1 = requset.getParameter("uname") == null ? "" : requset.getParameter("uname").toString();
		String pass1 = requset.getParameter("pass") == null ? "" : requset.getParameter("pass").toString();
		String site_id = requset.getParameter("site_id") == null ? "" : requset.getParameter("site_id").toString();
		userDetails objuserDetails = new userDetails();

		objuserDetails.setUname(uname1);
		objuserDetails.setPass(pass1);
		objuserDetails.setStrSiteId(site_id);
		
		try {

			
					
			boolean isValidLogin = dao.validate(objuserDetails, requset);

			if (isValidLogin) {
				if(indentNumber!=null&&!indentNumber.equals("null"))
				{
					return new ModelAndView("redirect:/IndentCreationDetailsShow.spring?indentNumber="+indentNumber);
				}
				return new ModelAndView("DashBoard");
			}else{
				requset.setAttribute("loginFailed","true");
				requset.setAttribute("Message","invalid Credentials");
				return new ModelAndView("index");
			}

		} 
	//	}
		
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}


	@RequestMapping(value = "/logout.spring")
	public ModelAndView logout(HttpServletRequest request) {
		try{
			System.out.println("logout()");
			HttpSession httpSession = request.getSession();
			httpSession.invalidate();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		request.setAttribute("loginFailed","false");
		return new ModelAndView("index");
	}

	@RequestMapping("/forGetPassword.spring")
	public String forGetPassword(HttpServletRequest requset, HttpServletResponse response) throws Exception {

		String returnVal=""; 
		String value="";
		String emailAddress ="";
		String dbSaltPassword="";
		EmailFunction objEmailFunction = new EmailFunction();
		List<String> toMailListArrayList = new ArrayList<String>();
		String userName=requset.getParameter("userName")  == null ? "" : requset.getParameter("userName").toString();
		String empId=requset.getParameter("empId")  == null ? "" : requset.getParameter("empId").toString();
		String site_id = requset.getParameter("site_id") == null ? "" : requset.getParameter("site_id").toString();
		requset.setAttribute("site_id",site_id);
		 Random rand = new Random();
			
		 try{

			 if((userName!=null && !userName.equals("")) || (empId!=null && !empId.equals(""))){
				 int rand_Number = rand.nextInt(1000000);
				 String randamNumber=String.valueOf(rand_Number);
				// System.out.println("the random number"+rand_Number);
				 requset.setAttribute("rand_Num",randamNumber);
				 requset.setAttribute("userName",userName);
				 requset.setAttribute("empId",empId);
				 value=dao.empEmailAndPassword(userName,empId,site_id);
				 if(!value.equals("") && value!=null){

					 String strValue[] = value.split("\\$");					
					 emailAddress = strValue[0];
					 dbSaltPassword  = strValue[3];
					 requset.setAttribute("dbSaltPassword",dbSaltPassword);	 

					 toMailListArrayList.add(emailAddress);
					 if(toMailListArrayList.size() > 0){
						 String emailto [] = null ;
						 emailto = new String[toMailListArrayList.size()];
						 toMailListArrayList.toArray(emailto);

						 objEmailFunction.sendOtpToUser(emailto,dbSaltPassword,rand_Number);	

					 }

					 requset.setAttribute("showPage","true");

					 returnVal="ForGetPassword";

				 }	else{

					requset.setAttribute("Message","please Enter Correct Credentials");

					 requset.setAttribute("showPage","false");
					 returnVal="ForGetPassword";
				 }

			} else{
				requset.setAttribute("Message","please Enter Correct Credentials");
				requset.setAttribute("showPage","false");
				returnVal="ForGetPassword";
			}

		 }catch(Exception e){
			e.printStackTrace();
		}
		
 		return returnVal;
	}
	
	@RequestMapping("/changeToNewPassword.spring")
	public String  changeToNewPassword(HttpServletRequest requset, HttpServletResponse response) throws Exception {
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		WriteTrHistory.write("Tr_Opened in LogC_chNPwd, ");
		
		String newPassword=requset.getParameter("newPassword");
		String conformPassword=requset.getParameter("conformPassword");
		String strOtp=requset.getParameter("otp");
		String rand_Number=requset.getParameter("rand_Num");
		String userName=requset.getParameter("userName");
		String empId=requset.getParameter("empId");
		String siteId=requset.getParameter("site_id");
		String dbSaltPassword=requset.getParameter("dbSaltPassword");
		String strSaltPassword ="";
	//	String strEmpId="";
		int strvalue=0;
		String returnVal="";
	//	String value="";
		
		SaltPassword salt=new SaltPassword();
		try{

			if(!empId.equals("") && empId!=null && userName.equals("")){

				userName=dao.getUserName(siteId,empId);
				
			}
			else if(!userName.equals("") && empId.equals("")){

				empId=dao.getEmpId(siteId,userName);
								
			}

			if(newPassword.equals(conformPassword)){

				if(	strOtp.equals(rand_Number)){


					HashMap<String, String> hashMap = new HashMap<String, String>();
					hashMap =salt.generateEncryptPassword(newPassword,userName); //password,login name

					strSaltPassword = hashMap.get("PASSSWORD");
					
					if(!strSaltPassword.equals(dbSaltPassword)){
						
					

					strvalue=dao.updatePassword(empId,siteId,newPassword,strSaltPassword);

						if(strvalue>0){
							requset.setAttribute("loginFailed","false");
							requset.setAttribute("Message","Your Password Successfully Changed");
							returnVal="index";

						}
						else{
							requset.setAttribute("loginFailed","true");
							requset.setAttribute("Message","Your Password Successfully Changed");
							returnVal="index";
						}

					}else{
						
						requset.setAttribute("Message","please enter New Password");
						requset.setAttribute("showPage","true");
						requset.setAttribute("userName",userName);
						requset.setAttribute("empId",empId);
						requset.setAttribute("rand_Num",rand_Number);
						requset.setAttribute("site_id",siteId);
						requset.setAttribute("dbSaltPassword",dbSaltPassword);
						returnVal="ForGetPassword";
					}
					
					
					
				}

				else{
					requset.setAttribute("Message","please enter Correct OTP");
					requset.setAttribute("showPage","true");
					requset.setAttribute("userName",userName);
					requset.setAttribute("empId",empId);
					requset.setAttribute("rand_Num",rand_Number);
					requset.setAttribute("site_id",siteId);
					requset.setAttribute("dbSaltPassword",dbSaltPassword);

					returnVal="ForGetPassword";
					
				}
			}
			else{
				requset.setAttribute("Message","please Enter Correct Password");
				requset.setAttribute("showPage","true");
				requset.setAttribute("userName",userName);
				requset.setAttribute("empId",empId);
				requset.setAttribute("rand_Num",rand_Number);
				requset.setAttribute("site_id",siteId);
				requset.setAttribute("dbSaltPassword",dbSaltPassword);
				returnVal="ForGetPassword";
				
			}
			transactionManager.commit(status);
			WriteTrHistory.write("Tr_Completed");

		}catch(Exception e){
			
			transactionManager.rollback(status);
			WriteTrHistory.write("Tr_Completed");
			e.printStackTrace();
		}
	
		return returnVal;
	}
	
	
	
	@RequestMapping("/loginRestServices.spring")
	@ResponseBody
	public String loinRestServices(HttpServletRequest requset, HttpServletResponse response) throws Exception {

		System.out.println("logout()");
		HttpSession httpSession = requset.getSession();
		httpSession.invalidate();

		HttpSession session = requset.getSession(true);
		session.setAttribute("UserId", "1000001");


		String strReqMessage = "<Xml><ServiceName>LoginService</ServiceName></Xml>";

		System.out.println(strReqMessage);


		return "<Xml><Message>success</Message><ResponseCode>0</ResponseCode></Xml>"; 


	}

	@RequestMapping("/getPurchaseEntry.spring")
	@ResponseBody
	public String getPurchaseEntry(HttpServletRequest requset, HttpServletResponse response) throws Exception {



		HttpSession session = requset.getSession(true);
		System.out.println(session.getAttribute("UserId"));

		String strReqMessage = "<Xml><ServiceName>Purchase Entry</ServiceName></Xml>";

		System.out.println(strReqMessage);
		return "<Xml><VendorName>SV Enter prises</VendorName><InvoiceNo>35623</InvoiceNo><InvoiceAmount>10000</InvoiceAmount><Message>success</Message><ResponseCode>0</ResponseCode></Xml>"; 


	}


	/*@RequestMapping("/sendMailWithoutLogin.spring")
	public String  sendMailWithoutLogin(HttpServletRequest requset, HttpServletResponse response) throws Exception {
		{

			EmailService_CustomerMails objEmailService_CustomerMails = new EmailService_CustomerMails();
			objEmailService_CustomerMails.sendmailsTOCustomer(requset);
			return "response";
		}
	}*/
}
