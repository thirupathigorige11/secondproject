package com.sumadhura.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.bean.WorkOrderBean;
import com.sumadhura.bean.userDetails;
import com.sumadhura.dto.IndentCreationDetailsDto;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.dto.IssueToOtherSiteDto;
import com.sumadhura.util.AESDecrypt;
import com.sumadhura.util.UIProperties;
//@Controller
public class EmailFunction extends UIProperties{

	// @Autowired
	// private SalesReport sr;
	// @Autowired
	// private SalesReportDetails srdtls;

	private static final String SMTP_HOST_NAME = "smtp.gmail.com";
	private static final String SMTP_PORT = "465";
	//	private static final String emailMsgTxt = "Closing balance Schedular is running failed, Please contact with support team on urgent basis";
	//	private static final String emailSubjectTxt = "Closing balance schedular running failed";
	//	private static final String emailFromAddress = "sumadhura5949@gmail.com";
	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	//	private static final String[] sendTo = { "pavan45662@gmail.com", "sumadhura5949@gmail.com","vericherlav@gmail.com" };

	public  void sendEmail(String emailBodyMsgText,String emailSubjectText,String emailFromAddress,String [] emailToAddress,String[] ccTo,String strIndentSiteId) throws Exception {

		// Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		//	TestEmailFunction sendMailThroughJava = new TestEmailFunction();

		String strUserName  = ""; String strPassword = "";


		if(strIndentSiteId != null && !strIndentSiteId.equals("")){
			if(strIndentSiteId.equals("102")){
				strUserName = validateParams.getProperty(strIndentSiteId+"_USERNAME");
				strPassword = validateParams.getProperty(strIndentSiteId+"_PASSWORD");
			}else{
				strUserName = validateParams.getProperty("KARNATAKA_USERNAME");
				strPassword = validateParams.getProperty("KARNATAKA_PASSWORD");
			}
		}else{
			strUserName = validateParams.getProperty("OTHER_USERNAME");
			strPassword = validateParams.getProperty("OTHER_PASSWORD");
		}
		sendSSLMessage(emailToAddress, emailSubjectText,
				emailBodyMsgText, emailFromAddress, ccTo, strUserName, strPassword);
		System.out.println("Sucessfully sent mail to all Users");
	}

	public static  void sendSSLMessage(String recipients[], String subject,
			String message, String from, String ccRecipients[],final String strUserName,final String strPassword ) throws MessagingException {
		boolean debug = true;

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.starttls.enable","true"); 

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				//return new PasswordAuthentication("jobsintbdb@gmail.com", "amaravadhi1");
				//System.out.println("mail authenticator ...................."+strUserName);
				return new PasswordAuthentication("purchase.sumadhura@gmail.com", "sumadhura_5949");
			}
		});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);
		// String fileName="c:/text/name";
		//System.out.println("ccRecipients length: "+ccRecipients.length);
		//System.out.println("-----------------");
		//System.out.print("To Email: ");
		for (int i = 0; i < recipients.length; i++) {
			if(i!=0){
				//System.out.print(",");
			}	
			//System.out.print(recipients[i]);
		}
		//System.out.println();
		//	System.out.print("cc Emails: ");
		for (int i = 0; i < ccRecipients.length; i++) {
			if(i!=0){

				//System.out.print(",");

			}	
			//System.out.print(">>"+ccRecipients[i]+"<<");
		}
		//System.out.println();
		//	System.out.println("-----------------");
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		String strToEmailId = "";
		for (int i = 0; i < recipients.length; i++) {


			strToEmailId = recipients[i];


			if(strToEmailId != null && !strToEmailId.equals("") && !strToEmailId.equals(" ")){

				addressTo[i] = new InternetAddress(strToEmailId);
			}

		}
		boolean there_is_no_EmailTo_address = false;
		//System.out.println("recipients.length: "+recipients.length);
		if(recipients.length==0){
			there_is_no_EmailTo_address = true;
		}
		else if(recipients.length!=0){
			if(recipients[0] == null){
				there_is_no_EmailTo_address = true;
			}
			else{
				msg.setRecipients(Message.RecipientType.TO, addressTo);  //vendor mail id
			}
		}

		if(ccRecipients.length>0){


			InternetAddress[] addressCC = new InternetAddress[ccRecipients.length];
			String strccToEmailId = "";
			for (int i = 0; i < ccRecipients.length; i++) {


				strccToEmailId = ccRecipients[i];


				if(strccToEmailId != null && !strccToEmailId.equals("") && !strccToEmailId.equals(" ")){

					addressCC[i] = new InternetAddress(strccToEmailId);
				}
			}

			//if(!ccRecipients[0].equals("")){
			/*InternetAddress[] addressCC = new InternetAddress[ccRecipients.length];
				for (int i = 0; i < addressCC.length; i++) {
					addressCC[i] = new InternetAddress(ccRecipients[i]);
				}*/
			//pruchase deprtment and specific person
			if(there_is_no_EmailTo_address){
				msg.setRecipients(Message.RecipientType.TO, addressCC);
			}else{
				msg.setRecipients(Message.RecipientType.CC, addressCC);
			}
			//}
		}

		// Setting the Subject and Content Type

		BodyPart messageBodyPart = new MimeBodyPart();

		// Now set the actual message
		messageBodyPart.setText("vinod this is for you");

		// Create a multipar message
		Multipart multipart = new MimeMultipart();

		multipart.addBodyPart(messageBodyPart);
		messageBodyPart = new MimeBodyPart();
		multipart.addBodyPart(messageBodyPart);

		msg.setSubject(subject);
		msg.setContent(multipart);
		msg.setContent(message, "text/html");
		Transport.send(msg);

	}

	/****************************************************************temp po mail for start**************************************************************************/
	public static  void sendSSLMessageForTempPO(String recipients[], String subject,
			Object message[], String from, String ccRecipients[],final String url,final String ApproveUrl,final String RejectUrl,String cancelUrl,String path) throws MessagingException, IOException {
		boolean debug = true;

		Properties props = new Properties();
		props.put("mail.smtp.host", SMTP_HOST_NAME);
		props.put("mail.smtp.auth", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.port", SMTP_PORT);
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.starttls.enable","true"); 

		Session session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				//return new PasswordAuthentication("jobsintbdb@gmail.com", "amaravadhi1");
				//System.out.println("mail authenticator ...................."+strUserName);
				return new PasswordAuthentication("purchase.sumadhura@gmail.com", "sumadhura_5949");
			}
		});

		session.setDebug(debug);

		Message msg = new MimeMessage(session);
		InternetAddress addressFrom = new InternetAddress(from);
		msg.setFrom(addressFrom);

		for (int i = 0; i < recipients.length; i++) {
			if(i!=0){
				//System.out.print(",");
			}			
		}

		for (int i = 0; i < ccRecipients.length; i++) {
			if(i!=0){

			}	

		}

		//	System.out.println("-----------------");
		InternetAddress[] addressTo = new InternetAddress[recipients.length];
		String strToEmailId = "";
		for (int i = 0; i < recipients.length; i++) {
			strToEmailId = recipients[i];


			if(strToEmailId != null && !strToEmailId.equals("") && !strToEmailId.equals(" ")){

				addressTo[i] = new InternetAddress(strToEmailId);
			}

		}
		boolean there_is_no_EmailTo_address = false;
		//System.out.println("recipients.length: "+recipients.length);
		if(recipients.length==0){
			there_is_no_EmailTo_address = true;
		}
		else if(recipients.length!=0){
			if(recipients[0] == null){
				there_is_no_EmailTo_address = true;
			}
			else{
				msg.setRecipients(Message.RecipientType.TO, addressTo);  //vendor mail id
			}
		}

		if(ccRecipients.length>0){


			InternetAddress[] addressCC = new InternetAddress[ccRecipients.length];
			String strccToEmailId = "";
			for (int i = 0; i < ccRecipients.length; i++) {
				strccToEmailId = ccRecipients[i];
				if(strccToEmailId != null && !strccToEmailId.equals("") && !strccToEmailId.equals(" ")){

					addressCC[i] = new InternetAddress(strccToEmailId);
				}
			}


			//pruchase deprtment and specific person
			if(there_is_no_EmailTo_address){
				msg.setRecipients(Message.RecipientType.TO, addressCC);
			}else{
				msg.setRecipients(Message.RecipientType.CC, addressCC);
			}
			//}
		}

		// Setting the Subject and Content Type

		BodyPart messageBodyPart = new MimeBodyPart();

		// Now set the actual message
		//messageBodyPart.setText("vinod this is for you");



		// Create a multipar message
		Multipart multipart = new MimeMultipart();

		/*	***********************************************************new mail**********************************************************************/
		BufferedReader br = null;
		FileReader fr = null;


		//br = new BufferedReader(new FileReader(FILENAME));F:\\Sumadhura\\Acropolis\\bootstrapExample.html

		fr = new FileReader(path);
		br = new BufferedReader(fr);



		String sCurrentLine;
		String outCome="";

		while ((sCurrentLine = br.readLine()) != null) {
			System.out.println(sCurrentLine);//localhost:8027/Sumadhura/
			outCome+="\n"+sCurrentLine;
		}
		outCome = outCome.replace("ApproveName",String.valueOf(message[11]));
		outCome = outCome.replace("DummyDate",String.valueOf(message[5]));
		outCome = outCome.replace("url",url+"&siteId="+String.valueOf(message[2]+"&poNumber="+String.valueOf(message[6])+"&siteLevelPoPreparedBy="+String.valueOf(message[12])+"&siteName="+String.valueOf(message[8])+
				"&password="+String.valueOf(message[13])+"&siteWiseIndentNo="+String.valueOf(message[7])+"&userId="+String.valueOf(message[1])+
				"&vendorId="+String.valueOf(message[15])+"&oldPOEntryId="+String.valueOf(message[16])+"&oldPODate="+String.valueOf(message[17])));
		outCome = outCome.replace("strPONum",String.valueOf(message[6]));
		outCome = outCome.replace("strSitewiseIndentNo",String.valueOf(message[7]));
		outCome = outCome.replace("tempsiteName",String.valueOf(message[8]));
		outCome = outCome.replace("VendorName",String.valueOf(message[9]));
		outCome = outCome.replace("DummyTotal",String.valueOf(message[10]));
		outCome = outCome.replace("approveUrl",ApproveUrl+"&strIndentNo="+String.valueOf(message[0])+"&userId="+String.valueOf(message[1])+"&siteId="+String.valueOf(message[2]+"&revision_No="+String.valueOf(message[3])+
				"&oldPoNumber="+String.valueOf(message[4])+"&strPONumber="+String.valueOf(message[6])+"&siteName="+String.valueOf(message[8])+"&siteLevelPoPreparedBy="+String.valueOf(message[12])+"&password="+String.valueOf(message[13])+
				"&vendorId="+String.valueOf(message[15])+"&oldPOEntryId="+String.valueOf(message[16])+"&oldPODate="+String.valueOf(message[17])+"&mail=true"));
		
		outCome = outCome.replace("rejectUrl",RejectUrl+"&strPONumber="+String.valueOf(message[6])+"&siteId="+String.valueOf(message[2])+"&password="+String.valueOf(message[13])+"&userId="+String.valueOf(message[1])+"&cancelOrReject=reject"+"&port="+String.valueOf(message[14])+"&Email=true");
		outCome = outCome.replace("cancelUrl",cancelUrl+"&strPONumber="+String.valueOf(message[6])+"&siteId="+String.valueOf(message[2])+"&password="+String.valueOf(message[13])+"&userId="+String.valueOf(message[1])+"&cancelOrReject=cancel"+"&port="+String.valueOf(message[14])+"&Email=true");
		//outCome = outCome.replace("MailSignatureText", javaEditedSignatureText);
		//outCome = outCome.replace("strNumber",String.valueOf(message[0]));
		outCome = outCome.replace("strComment",String.valueOf(message[1]));
		outCome = outCome.replace("type",String.valueOf(message[12]));
		outCome = outCome.replace("RejectOrCancel",url+"&siteId="+String.valueOf(message[2])+"&poNumber="+String.valueOf(message[6])+"&siteName="+String.valueOf(message[8])+
				"&password="+String.valueOf(message[13])+"&siteWiseIndentNo="+String.valueOf(message[7])+"&userId="+String.valueOf(message[1])+"&mail=true");
		messageBodyPart.setContent(outCome, "text/html");




		/********************************************************************mailend*******************************************************************/	
		//multipart.addBodyPart(messageBodyPart);
		multipart.addBodyPart(messageBodyPart);
		messageBodyPart = new MimeBodyPart();
		//multipart.addBodyPart(messageBodyPart);

		msg.setSubject(subject);
		msg.setContent(multipart);
		//msg.setContent(message, "text/html");
		Transport.send(msg);

	}



	/*************************************************************temp po mail for end ssl***************************************************************************/
	public  void  sendIndentCreationApprovalMail(String strApproverName,int strIndentNumber,String strIndentFrom,
			String strIndentSite,String strIndentCreationDate,String [] emailToAddress ){

		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();

			//String emailFromAddress = "jobsintbdb@gmai.com";

			String emailSubjectText = "Indent Process Request";

			String emailBodyMsgTxt = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
				+ "<title>Email Format</title><style> table tr td{ border:1px solid #000; height:29px; } </style> "
				+ "</head> <body> <div class=\"desc-icon-box\" style=\"margin-top: 29px; font-family: Calibri;\"> "
				+ "<div style='font-family: Calibri;font-size:14px;' class=\"greting1\">Dear "+strApproverName+"</div><br> <div style='font-family: Calibri;font-size:14px;' class=\"greting2\">Greetings!!!</div><br>"
				+ " <div style='font-family: Calibri;font-size:14px;' class=\"greting3\">You have a indent for Approval !!!</div><br></div> "
				+ "<table class=\"table\" class=\"table-bordered\"style=\"width:898px;margin:6px auto;border:0;border-spacing:0;font-family: Calibri;border:1px solid #000;\">"
				+ "<tbody><tr class=\"bckcolor\" style=\"background: #b4d4e3;height:20px;border:1px solid #000;\"> <td  class=\"text-left\">Indent Number </td> "
				+ "<td  class=\"text-left\"> Indent From </td> <td  class=\"text-left\">Indent Site</td> "
				+ " <td  class=\"text-left\">Date</td></tr>  <tr><td  class=\"text-left\"><span class=\"contentBold\">"+strIndentNumber+"</span> "
				+ " </td>  <td  class=\"text-left\"><span class=\"contentBold\">"+strIndentFrom+"</span></td> <td   class=\"text-left\">"+strIndentSite+" </td>"
				+ "  <td   class=\"text-left\">"+strIndentCreationDate+"<span class=\"contentBold\"></span></td> </tr></tbody></table><div>"
				+ "<img  class=\"text-img\" style=\"margin-left: 179 !important;	float: left;position: relative;margin-left: 178px;bottom: -26px;right: -38px;\" src=\"cid:my-image-id\"  />"
				+ "</div><div class=\"footer-section\" style=\"margin-left: 29px;margin-top: 101px;font-family: Calibri;\">"
				+ " <div>Arun Kumar B A | Billing Engineer</div><div>Sumadhura Infracon Pvt. Ltd | Web: www.sumadhuragroup.com</div>"
				+ "<div><div>No. 43, C.K.B Plaza, 2nd Floor, Marathahalli, Bengaluru, Karnataka. India. 560037.</div></div>"
				+ "	<div><div>T:  91 7022009206 |  E: billingengineer@sumadhuragroup.com </div></div>  </div></body></html>";

			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, new String[0],"");


			//public static void sendEmail(String emailBodyMsgText,String emailSubjectText,String emailFromAddress,String [] emailToAddress) throws Exception {





		}catch(Exception e){
			e.printStackTrace();
		}
	}


	//ravi writing code 2


	public  void  sendIndentCreationApprovalMailDetails(String strApproverName,int strIndentNumber,String strIndentFrom,
			String strIndentSite,String strIndentCreationDate,String strScheduleDate,String [] emailToAddress,List<IndentCreationDetailsDto> listProductDetails, IndentCreationDto indentCreationDto,String strIndentLevelComments, List<IndentCreationBean> listProductChangesList, int siteWiseIndentNo,int getLocalPort,
			String userName,String indentName){
		//List<IndentCreationDetailsDto> list=indentCreationDetailsDto;
		int count=1;
		String strRemarks="";
		String productDetails="";
		String url="";
		// List<IndentCreationDetailsDto> listProductDetails = new ArrayList<IndentCreationDetailsDto>();

		// indentCreationDetailsDto = new IndentCreationDetailsDto();
		try{
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();

			//String emailFromAddress = "jobsintbdb@gmai.com";

			String emailSubjectText = "Indent Process Request";
			String emailBodyMsgTxt="";

			String ApproveUrl = validateParams.getProperty("MAIL_APPROVE_URL") == null ? "" : validateParams.getProperty("MAIL_APPROVE_URL").toString();
			String RejectUrl = validateParams.getProperty("MAIL_REJECT_URL") == null ? "" : validateParams.getProperty("MAIL_REJECT_URL").toString();
			String EditAndApproveUrl = validateParams.getProperty("MAIL_EDIT_AND_APPROVE_URL") == null ? "" : validateParams.getProperty("MAIL_EDIT_AND_APPROVE_URL").toString();


			//	int getLocalPort = request.getLocalPort();
			/*ApplicationContext ApproveUrl = null;
			ApplicationContext RejectUrl = null;
			ApplicationContext EditAndApproveUrl = null;*/

			if(getLocalPort == 8078){ //local machine
				ApproveUrl = "http://106.51.38.64:8078/Sumadhura_UAT/IndentApprovalRejectMailFunction.jsp?indentNumber="+strIndentNumber+"&portNo="+getLocalPort+"&siteId="+indentCreationDto.getSiteId()+"&userId="+indentCreationDto.getPendingEmpId()+"&tempPass="+indentCreationDto.getTempPass()+"&noofRowsTobeProcessed="+listProductDetails.size()+"&operationtype=Approve";

				RejectUrl="http://106.51.38.64:8078/Sumadhura_UAT/IndentApprovalRejectMailFunction.jsp?indentNumber="+strIndentNumber+"&portNo="+getLocalPort+"&siteId="+indentCreationDto.getSiteId()+"&userId="+indentCreationDto.getPendingEmpId()+"&tempPass="+indentCreationDto.getTempPass()+"&operationtype=Reject";

				EditAndApproveUrl="http://106.51.38.64:8078/Sumadhura_UAT/index.jsp?IndentApproval=";
				url="http://106.51.38.64:8078/Sumadhura_UAT/";
			}
			else if(getLocalPort == 8079){ //CUG
				ApproveUrl = "http://129.154.74.18:8079/Sumadhura_CUG/IndentApprovalRejectMailFunction.jsp?indentNumber="+strIndentNumber+"&portNo="+getLocalPort+"&siteId="+indentCreationDto.getSiteId()+"&userId="+indentCreationDto.getPendingEmpId()+"&tempPass="+indentCreationDto.getTempPass()+"&noofRowsTobeProcessed="+listProductDetails.size()+"&operationtype=Approve";

				RejectUrl="http://129.154.74.18:8079/Sumadhura_CUG/IndentApprovalRejectMailFunction.jsp?indentNumber="+strIndentNumber+"&portNo="+getLocalPort+"&siteId="+indentCreationDto.getSiteId()+"&userId="+indentCreationDto.getPendingEmpId()+"&tempPass="+indentCreationDto.getTempPass()+"&operationtype=Reject";

				EditAndApproveUrl="http://129.154.74.18:8079/Sumadhura_CUG/index.jsp?IndentApproval=";
				url="http://129.154.74.18:8079/Sumadhura_CUG/";

			}
			else if(getLocalPort == 80){ //LIVE
				ApproveUrl = "http://129.154.74.18/Sumadhura/IndentApprovalRejectMailFunction.jsp?indentNumber="+strIndentNumber+"&portNo="+getLocalPort+"&siteId="+indentCreationDto.getSiteId()+"&userId="+indentCreationDto.getPendingEmpId()+"&tempPass="+indentCreationDto.getTempPass()+"&noofRowsTobeProcessed="+listProductDetails.size()+"&operationtype=Approve";

				RejectUrl="http://129.154.74.18/Sumadhura/IndentApprovalRejectMailFunction.jsp?indentNumber="+strIndentNumber+"&portNo="+getLocalPort+"&siteId="+indentCreationDto.getSiteId()+"&userId="+indentCreationDto.getPendingEmpId()+"&tempPass="+indentCreationDto.getTempPass()+"&operationtype=Reject";

				EditAndApproveUrl="http://129.154.74.18/Sumadhura/index.jsp?IndentApproval=";

				url="http://129.154.74.18/Sumadhura/";

			}

			/****************************************************/
			BufferedReader br = null;
			FileReader fr = null;

			fr = new FileReader("C:\\testing\\MailTemplates\\indentApprovalMail.html");
			//fr = new FileReader("D:\\ravi\\indentApprovalMail.html");
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}

			emailBodyMsgTxt = emailBodyMsgTxt.replace("Approver_name",strApproverName);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("indent_no",String.valueOf(siteWiseIndentNo));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("schedule_date",strScheduleDate);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("indent_from",strIndentFrom);
			//emailBodyMsgTxt = emailBodyMsgTxt.replace("Approver_name",strApproverName);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("required_Date", strIndentCreationDate);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("indent_site", strIndentSite);

			

			/****************************/

			

			/****************************************************/		
			String tableData = "<div style=\"margin-top:30px;\"> <table class=\"table\" style=\"width:90%;margin-top:20px;border: 0;border-spacing: 0;font-family: Calibri;border: 1px solid #000;\">"
				+"<thead>  <tr style=\"background: #b4d4e3;border: 1px solid #000;\">"
				+"<th style=\"text-align:center;border:1px solid #000\">S.No</th>"
				+"<th style=\"text-align:center;border:1px solid #000\">Product</th>"
				+"<th style=\"text-align:center;border:1px solid #000\">Sub Product</th>"
				+"<th style=\"text-align:center;border:1px solid #000\">Child Product</th>"
				+"<th style=\"text-align:center;border:1px solid #000\">Unit of Measurement</th>"
				+"<th style=\"text-align:center;border:1px solid #000\">Quantity</th>"
				+"<th style=\"text-align:center;border:1px solid #000\">Remarks</th>"
				+"</tr>  </thead> <tbody>";


			for(int i=0;i<listProductDetails.size();i++){
				//	System.out.println("---->"+listProductDetails.get(i).getProdName());

				String strdetailedRemarks = "";
				strRemarks=listProductDetails.get(i).getRemarks();//.toString();
				if(strRemarks!=null){
					if(strRemarks.contains("@@@")){
						String strRemarksArr[] = strRemarks.split("@@@");

						for(int j =0 ; j< strRemarksArr.length;j++){

							strdetailedRemarks += " "+(j+1)+". "+strRemarksArr [j];

						}
						strRemarks = strdetailedRemarks;
					}	
				}
				tableData+= " <tr>"
					+"<td style=\"text-align:center;border:1px solid #000\">"+count+"</td>"
					+"<td style=\"text-align:center;border:1px solid #000\">"+listProductDetails.get(i).getProdName()+"</td>"
					+"<td style=\"text-align:center;border:1px solid #000\">"+listProductDetails.get(i).getSubProdName()+"</td>"
					+"<td style=\"text-align:center;border:1px solid #000\">"+listProductDetails.get(i).getChildProdName()+"</td>"
					+"<td style=\"text-align:center;border:1px solid #000\">"+listProductDetails.get(i).getMeasurementName()+"</td>"
					+"<td style=\"text-align:center;border:1px solid #000\">"+listProductDetails.get(i).getRequiredQuantity()+"</td>"
					+"<td style=\"text-align:center;border:1px solid #000\">"+strRemarks+"</td>"
					+"</tr>";
				count++;

			}


			tableData+="</tbody></table></div><br><br><br><br>";

			/****************************************************/
		
			String strEditComments = "";
			if(listProductChangesList != null){

				for(int i =0 ;i< listProductChangesList.size();i++ ){

					IndentCreationBean objIndentCreationBean = listProductChangesList.get(i);
					strEditComments += objIndentCreationBean.getMaterialEditComment()+"<br>";
				}
			}
			emailBodyMsgTxt = emailBodyMsgTxt.replace("table_data",tableData);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Approved_comments", strIndentLevelComments);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("modification_comments", strEditComments);
			
			String ApproveData="<div style=\"margin-top:30px;\">"
							+"<a href="+ApproveUrl+" style=\"float:left;display:inline;width:130px;background:#337ab7;border:1px solid #2e6da4;text-decoration:none;text-align:center;padding:6px 12px;color:#fff;border-radius:5px;margin-right:10px;margin-bottom: 10px;\" target=\"_blank\">Approve</a>"
							+"<a href="+RejectUrl+" style=\"float:left;display:inline;width:130px;background:#337ab7;border:1px solid #2e6da4;text-decoration:none;text-align:center;padding:6px 12px;color:#fff;border-radius:5px;margin-right:10px;margin-bottom: 10px;\" target=\"_blank\">Cancel Indent</a>"
							+"<a href="+EditAndApproveUrl+strIndentNumber+" style=\"float:left;display:inline;width:130px;background:#337ab7;border:1px solid #2e6da4;text-decoration:none;text-align:center;padding:6px 12px;color:#fff;border-radius:5px;margin-right:10px;margin-bottom: 10px;\" target=\"_blank\">Edit & Approve</a>"
							+"</div>";
			
			if(indentCreationDto.getPendingEmpId().equals("-"))
			{
				emailBodyMsgTxt = emailBodyMsgTxt.replace("Approve_data","");
				//emailBodyMsgTxt=emailBodyMsgTxt+productDetails+Enddetails1+Enddetails2;
			}
			else{
				emailBodyMsgTxt = emailBodyMsgTxt.replace("Approve_data",ApproveData);
				//emailBodyMsgTxt=emailBodyMsgTxt+productDetails+Enddetails1+Buttons+Enddetails2;
			}


			if(!emailToAddress.equals("")){

				sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, new String[0],"");
			}
		
			//	String link = "<a href='http://localhost:8081/Sumadhura/IndentApprovalRejectMailFunction.jsp?indentNumber="+strIndentNumber+"&siteId="+indentCreationDto.getSiteId()+"&userId="+indentCreationDto.getPendingEmpId()+"&tempPass="+indentCreationDto.getTempPass()+"'>check link</a>";

			
			//System.out.println("IP Address: "+InetAddress.getLocalHost().getHostAddress());
			//public static void sendEmail(String emailBodyMsgText,String emailSubjectText,String emailFromAddress,String [] emailToAddress) throws Exception {





		}catch(Exception e){
			e.printStackTrace();
		}
	}




	public  void  sendMailToVendor(String strIndentRaised,int strIndentNumber,String strIndentFrom,
			String strIndentSite,String strIndentCreationDate,String [] emailToAddress ,VendorDetails vendorDetails ,
			String indentCreationDetailsIdForenquiry,String address,int siteWiseIndentNo,String strIndentSiteId,String sendEnquiryEmpDetails,
			int getLocalPort,String [] ccTo){

		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String VendorLoginUrl  = validateParams.getProperty("VENDOR_LOGIN_URL") == null ? "" : validateParams.getProperty("VENDOR_LOGIN_URL").toString();

			if(getLocalPort == 8078){

				VendorLoginUrl="http://129.154.74.18:8078/Sumadhura_UAT/vendorloginsubmit.spring";

			}else if(getLocalPort == 8079){
				VendorLoginUrl="http://106.51.38.64:8079/Sumadhura_CUG/vendorloginsubmit.spring";

			}else if(getLocalPort == 80){

				VendorLoginUrl="http://129.154.74.18/Sumadhura/vendorloginsubmit.spring";

			}

			/*if(InetAddress.getLocalHost().getHostAddress().equals("192.168.1.104"))
			{
				VendorLoginUrl=VendorLoginUrl.replace("localhost:8032","117.213.180.168");
			}
			else if(InetAddress.getLocalHost().getHostAddress().equals("129.154.74.18"))
			{
				VendorLoginUrl=VendorLoginUrl.replace("localhost:8032","129.154.74.18");
			}
			else
			{
				VendorLoginUrl=VendorLoginUrl.replace("localhost:8032","localhost:8032");
			}
			 *///String emailFromAddress = "jobsintbdb@gmai.com";

			//	VendorLoginUrl="http://129.154.74.18/Sumadhura/vendorloginsubmit.spring";

			//VendorLoginUrl="http://localhost:8081/Sumadhura/vendorlogin.spring";

			String emailSubjectText = "Material Quotation Request";

			String data[] = sendEnquiryEmpDetails.split(",");
			String strEmpName=data[0];
			String strEmpEmailId=data[1];
			String strMobiloeNumber=data[2];


			AESDecrypt encrypt=new AESDecrypt();


			String userDate = "indentNumber="+strIndentNumber+"$$$vendordata="+vendorDetails.getVendor_name()+"@@"+vendorDetails.getGsin_number()+"@@"+vendorDetails.getAddress()+"@@"+vendorDetails.getVendor_Id()+
			"$$$uname="+vendorDetails.getVendor_Id()+"$$$pass="+vendorDetails.getVendor_Pass()+"$$$indentCreationDetailsIdForenquiry="+indentCreationDetailsIdForenquiry+"$$$address="+address;

			String strEncryptionData =encrypt.encrypt("AMARAVADHIS12345",userDate);

			String emailBodyMsgTxt = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
				+ "<title>Email Format</title><style> table tr td{ border:1px solid #000; height:29px; } </style> "
				+ "</head> <body> <div class=\"desc-icon-box\" style=\"margin-top: 29px; font-family: Calibri;\"> "
				+ "<div style='font-family: Calibri;font-size:14px;'class=\"greting1\">Dear Vendor</div><br> <div style='font-family: Calibri;font-size:14px;' class=\"greting2\">Greetings!!!</div><br>"
				+ " <div style='font-family: Calibri;font-size:14px;' class=\"greting3\">Please login and fill the enquiry form!!!</div><br></div> "

				+ "<table class=\"table\" class=\"table-bordered\"style=\"width:898px;border:0;border-spacing:0;font-family: Calibri;border:1px solid #000;border-collapse: collapse;\">"
				+ "<tbody><tr class=\"bckcolor\" style=\"background: #ccc;height:20px;border:1px solid #000;\"> <td style=\"text-align: center;font-weight: 700;\">Sitewise Indent Number </td> "
				+ "<td style=\"text-align: center;font-weight: 700;\"> Indent From </td> <td style=\"text-align: center;font-weight: 700;\">Indent Site</td> "
				+ " <td style=\"text-align: center;font-weight: 700;\">Date</td></tr>  <tr><td  style=\"text-align: center;\" class=\"text-left\"><span class=\"contentBold\">"+siteWiseIndentNo+"</span> "
				+ " </td>  <td  class=\"text-left\"  style=\"text-align: center;\" ><span class=\"contentBold\">"+strIndentFrom+"</span></td> <td  style=\"text-align: center;\"  class=\"text-left\">"+strIndentRaised+" </td>"
				+ "  <td  style=\"text-align: center;\">"+strIndentCreationDate+"<span class=\"contentBold\"></span></td> </tr></tbody></table>"
				+ "<div style=\"text-align:center\">"
				/*+ "<b style='display: block;margin-top:30px;margin-bottom:20px;color:blue;'>Your username: "+"<span style='color:red;'>"+vendorDetails.getVendor_Id()+"</span>"+"</b><br>"
				+ "<b style='color:blue;'>Your password: "+"<span style='color:red;'>" + vendorDetails.getVendor_Pass()+"</span>"+"</b>"
				 */
				+ "<form method='POST' action='"+VendorLoginUrl+"' />"
				+ "<input type='hidden' name='indentNumber' value='"+strIndentNumber+"' />"
				+ "<input type='hidden' name='vendordata' value='"+vendorDetails.getVendor_name()+"@@"+vendorDetails.getGsin_number()+"@@"+vendorDetails.getAddress()+"@@"+vendorDetails.getVendor_Id()+"' />"
				+ "<input type='hidden' name='uname' value='"+vendorDetails.getVendor_Id()+"' />"
				+ "<input type='hidden' name='pass' value='"+vendorDetails.getVendor_Pass()+"' />"
				+"<input type='hidden' name='submitButton' value='true' />"
				+ "<input type='hidden' name='indentCreationDetailsIdForenquiry' value='"+indentCreationDetailsIdForenquiry+"' />"
				+ "<input type='hidden' name='address' value='"+address+"' />"
				+ "<input type='submit' value='Click to login'style=\"color: #fff;background-color: #f0ad4e;border-color: #eea236;padding:6px 12px;border-radius:5px; margin-top: 50px;margin-right:10px;border:1px solid #eea236;\" />"	
				+ "</form>"


				+ "<a href='"+VendorLoginUrl+"?data="+strEncryptionData+"'>Click this link</a>"
				+ "<img  class=\"text-img\" style=\"margin-left: 179 !important;	float: left;position: relative;margin-left: 178px;bottom: -26px;right: -38px;\" src=\"cid:my-image-id\"  />"
				+ "</div><div class=\"footer-section\" style=\"margin-top: 101px;font-family: Calibri;\">"
				+ " <div>"+strEmpName+"</div><div>Sumadhura Infracon Pvt. Ltd | Web: www.sumadhuragroup.com</div>"
				+ "<div><div>No. 43, C.K.B Plaza, 2nd Floor, Marathahalli, Bengaluru, Karnataka. India. 560037.</div></div>"
				+ "	<div><div>T:"+strMobiloeNumber+"|  E:"+strEmpEmailId+"</div></div>  </div></body></html>";

			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, ccTo,strIndentSiteId);


			//public static void sendEmail(String emailBodyMsgText,String emailSubjectText,String emailFromAddress,String [] emailToAddress) throws Exception {





		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public  void  sendMailToVendorForPO(String strIndentRaised,int strIndentNumber,String strIndentFrom,
			String strIndentSite,String strIndentCreationDate,String [] emailToAddress ,VendorDetails vendorDetails ,
			String poNumber, String[] ccTo,String subject,String poCreatedEmpName,int getLocalPort,String contact_person_Name,String revisedOrNot,
			String oldpoNumber,String poDate,String acc_Note_Email){

		String url="";

		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String VendorLoginUrl  = validateParams.getProperty("VENDOR_LOGIN_URL") == null ? "" : validateParams.getProperty("VENDOR_LOGIN_URL").toString();
			if(getLocalPort == 8078){

				VendorLoginUrl="http://129.154.74.18:8078/Sumadhura_UAT/showPODetailsToVendor.spring";
				url="http://129.154.74.18:8078/Sumadhura_UAT/showPODetailsToVendor.spring";

			}else if(getLocalPort == 8079){
				VendorLoginUrl="http://106.51.38.64:8079/Sumadhura_CUG/showPODetailsToVendor.spring";
				url="http://106.51.38.64:8079/Sumadhura_CUG/showPODetailsToVendor.spring";

			}else if(getLocalPort == 80){

				VendorLoginUrl="http://129.154.74.18/Sumadhura/showPODetailsToVendor.spring";
				url="http://129.154.74.18/Sumadhura/showPODetailsToVendor.spring";
			}

			/*if(InetAddress.getLocalHost().getHostAddress().equals("192.168.1.104"))
			{
				VendorLoginUrl=VendorLoginUrl.replace("localhost:8032","117.213.180.168");
			}
			else if(InetAddress.getLocalHost().getHostAddress().equals("129.154.74.18"))
			{
				VendorLoginUrl=VendorLoginUrl.replace("localhost:8032","129.154.74.18");
			}
			else
			{
				VendorLoginUrl=VendorLoginUrl.replace("localhost:8032","localhost:8032");
			}
			 *///String emailFromAddress = "jobsintbdb@gmai.com";

			//	VendorLoginUrl="http://129.154.74.18/Sumadhura/showPODetailsToVendor.spring?poNumber="+poNumber;

			//VendorLoginUrl="http://localhost:8081/Sumadhura/showPODetailsToVendor.spring?poNumber="+poNumber;
			String emailSubjectText = "";
			String emailBodyMsgTxt ="";
			if(subject==null || subject.equals("") || subject.equals("-")){
				emailSubjectText = "Sumadhura PO";
			}
			else{
				emailSubjectText = subject;
			}

			String data[] = poCreatedEmpName.split(",");
			String strEmpName=data[0];
			String strEmpEmailId=data[1];
			String strMobiloeNumber=data[2];
			//	String vendorId=vendorDetails.getVendor_Id();

			//System.out.println("email site id....."+strIndentFrom);
			AESDecrypt encrypt=new AESDecrypt();


			String userData="poNumber="+poNumber+"&indentSiteId="+strIndentFrom;

			String strEncryptionData =encrypt.encrypt("AMARAVADHIS12345",userData);
			strEncryptionData =  strEncryptionData.replaceAll("[\\t\\n\\r]+","");
			
			if(revisedOrNot.equalsIgnoreCase("true")){
				/**************************************this is for revised po start******************************************************/
				BufferedReader br = null;
				FileReader fr = null;

				fr = new FileReader("C:\\testing\\MailTemplates\\RevisedPo.html");
				//fr = new FileReader("F:\\RevisedPo.html");
				br = new BufferedReader(fr);

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					System.out.println(sCurrentLine);
					emailBodyMsgTxt+="\n"+sCurrentLine;
				}
				emailBodyMsgTxt = emailBodyMsgTxt.replace("VENDOR_NAME",contact_person_Name);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_DATE",poDate);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("OLD_PO_NUMBER ",oldpoNumber);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("REVISED_PO_NUMBER",poNumber);
				
				emailBodyMsgTxt = emailBodyMsgTxt.replace("VendorLoginUrl",VendorLoginUrl);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("url",url);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("strEncryptionData",strEncryptionData);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("po_Number",poNumber);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("strIndentFrom",strIndentFrom);
				
				emailBodyMsgTxt = emailBodyMsgTxt.replace("strEmpName",strEmpName);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("strMobiloeNumber",strMobiloeNumber);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("strEmpEmailId",strEmpEmailId);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("Note_payment",acc_Note_Email);
				/**************************************this is for revised po end******************************************************/

			}
			else{
				emailBodyMsgTxt = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
					+ "<title>Email Format</title><style> table tr td{ border:1px solid #000; height:29px; } </style> "
					+ "</head> <body> <div class=\"desc-icon-box\" style=\"margin-top: 29px; font-family: Calibri;\"> "
					+ "<div style='font-family: Calibri;font-size:14px;'class=\"greting1\">Dear "+contact_person_Name+"</div><br> <div style='font-family: Calibri;font-size:14px;' class=\"greting2\">Greetings!!!</div><br>"
					+ " <div style='font-family: Calibri;font-size:14px;' class=\"greting3\">You got a Purchase Order from Sumadhura Infracon Pvt Lmtd. For more details, click the link (or) button below.</div><br></div> "
					+ " <div style='font-family: Calibri;font-size:14px;color:red;font-weight:bold;margin-bottom:-15px;' class=\"greting3\">Note</div><ol><li style='color:red;font-weight:bold;'>Mention PO Ref No. and PO date in Tax Invoice.</li><li style='color:red;font-weight:bold;'> Tax Invoice without PO Ref No. and PO date will not be accepted.</li></ol><br>"
					+ "<div>"
					+" <a href='"+VendorLoginUrl+"?poNumber="+poNumber+"&indentSiteId="+strIndentFrom+"&submitButton=true' class=\"btn btn-warning btn-margin\" style=\"padding:6px 12px;text-decoration:none;background-color: #337ab7;border-color: #2e6da4;color:#fff;border-radius:5px;\">Click Here</a>"
					/*+ "<form method='POST' action='"+VendorLoginUrl+"' />"
					+ "<input type='hidden' name='poNumber' value='"+poNumber+"' />"
					+ "<input type='hidden' name='indentSiteId' value='"+strIndentFrom+"' />"

					+"<input type='hidden' name='submitButton' value='true' />"*/
					/*+ "<input type='submit' value='Click Here'style=\"background: #b4d4e3;width: 158px;height: 29px;margin-left: 423px;margin-top: 50px;\" />"*/	
					/*+"<input type='submit' value='Click Here'/>"
					+ "</form>"*/
	                +"<div style='clear:both;margin-top:10px;padding-left:23px;'>Or</div>"
					+ "  <a href='"+url+"?data="+strEncryptionData+"'>Click this link</a>"
					+ "<img  class=\"text-img\" style=\"margin-left: 179 !important;	float: left;position: relative;margin-left: 178px;bottom: -26px;right: -38px;\" src=\"cid:my-image-id\"  />"
					+ "</div><div class=\"footer-section\" style=\"margin-top: 58px;font-family: Calibri;font-size: 14px;\">"
					+ " <div>"+strEmpName+"</div><div>Sumadhura Infracon Pvt. Ltd | Web: www.sumadhuragroup.com</div>"
					+ "<div><div>No. 43, C.K.B Plaza, 2nd Floor, Marathahalli, Bengaluru, Karnataka. India. 560037.</div></div>"
					+ "	<div><div>T:"+strMobiloeNumber+"|  E:"+strEmpEmailId+"</div></div>  </div></body></html>";
				}

			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, ccTo,strIndentFrom);


			//public static void sendEmail(String emailBodyMsgText,String emailSubjectText,String emailFromAddress,String [] emailToAddress) throws Exception {





		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public  void  sendOtpToUser(String [] emailToAddress,String password,int rand_Num){
		String subject="";

		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String emailSubjectText = "";
			if(subject==null || subject.equals("")){
				emailSubjectText = "Hello User New OTP";

				String emailBodyMsgTxt = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
					+ "<title>Email Format</title><style> table tr td{ border:1px solid #000; height:29px; } </style> "
					+ "</head> <body> <div class=\"desc-icon-box\" style=\"margin-top: 29px; font-family: Calibri;\"> "
					+ "<div style='font-family: Calibri;font-size:14px;'class=\"greting1\">Dear User</div><br> <div style='font-family: Calibri;font-size:14px;' class=\"greting2\">Greetings!!!</div><br>"
					+ " <div style='font-family: Calibri;font-size:14px;' class=\"greting3\">You have New OTP</div><br></div> New OTP:"+rand_Num
					+ "<form>"
					+ "<input type='hidden' name='password' value='"+password+"' />"
					+ "<input type='hidden' name='rand_Num' value='"+rand_Num+"' />"
					+ "</form>"

					//+ "<a href='http://localhost:8081/Sumadhura/vendorlogin.spring?indentNumber="+strIndentNumber+"&vendordata="+vendorDetails.getVendor_name()+"@@"+vendorDetails.getGsin_number()+"@@"+vendorDetails.getAddress()+"@@"+vendorDetails.getVendor_Id()+"'>click this link</a>"
					+ "<img  class=\"text-img\" style=\"margin-left: 179 !important;	float: left;position: relative;margin-left: 178px;bottom: -26px;right: -38px;\" src=\"cid:my-image-id\"  />"
					+ "</div><div class=\"footer-section\" style=\"margin-left: 29px;margin-top: 101px;font-family: Calibri;\">"
					+ " <div>Arun Kumar B A | Billing Engineer</div><div>Sumadhura Infracon Pvt. Ltd | Web: www.sumadhuragroup.com</div>"
					+ "<div><div>No. 43, C.K.B Plaza, 2nd Floor, Marathahalli, Bengaluru, Karnataka. India. 560037.</div></div>"
					+ "	<div><div>T:  91 7022009206 |  E: billingengineer@sumadhuragroup.com </div></div>  </div></body></html>";



				sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress,emailToAddress,new String[0],"");
			}



		}catch(Exception e){
			e.printStackTrace();
		}


	}


	public  void  sendEnquiryFormFillMailToPurchaseDept( String[] ccTo, String siteWiseIndentNo, String vendorName, String siteName){


		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String emailSubjectText = "";

			emailSubjectText = "Successfully Details Submit By Vendor ";

			String emailBodyMsgTxt = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
				+ "<title>Email Format</title><style> table tr td{ border:1px solid #000; height:29px; } </style> "
				+ "</head> <body> <div class=\"desc-icon-box\" style=\"margin-top: 29px; font-family: Calibri;\"> "
				+ "<div style='font-family: Calibri;font-size:14px;'class=\"greting1\">Dear User</div><br> <div style='font-family: Calibri;font-size:14px;' class=\"greting2\">Greetings!!!</div><br>"
				+ " <div style='font-family: Calibri;font-size:14px;' class=\"greting3\">Successfully Submit By Vendor Data"

				+ "<table class=\"table\" class=\"table-bordered\"style=\"width:898px;margin:6px auto;border:0;border-spacing:0;font-family: Calibri;border:1px solid #000;\">"
				+ "<tbody><tr class=\"bckcolor\" style=\"background: #b4d4e3;height:20px;border:1px solid #000;\"> <td  class=\"text-left\">Sitewise Indent Number </td> "
				+ "<td  class=\"text-left\">Indent Site</td> "
				+ " <td  class=\"text-left\">Vendor</td></tr>  <tr><td  class=\"text-left\"><span class=\"contentBold\">"+siteWiseIndentNo+"</span>  </td> "
				+ " <td   class=\"text-left\">"+siteName+" </td>"
				+ "  <td   class=\"text-left\">"+vendorName+"<span class=\"contentBold\"></span></td> </tr></tbody></table>"

				+ "</div><br></div>" 
				+ "<form>"


				+ "</form>"

				//+ "<a href='http://localhost:8081/Sumadhura/vendorlogin.spring?indentNumber="+strIndentNumber+"&vendordata="+vendorDetails.getVendor_name()+"@@"+vendorDetails.getGsin_number()+"@@"+vendorDetails.getAddress()+"@@"+vendorDetails.getVendor_Id()+"'>click this link</a>"
				+ "<img  class=\"text-img\" style=\"margin-left: 179 !important;	float: left;position: relative;margin-left: 178px;bottom: -26px;right: -38px;\" src=\"cid:my-image-id\"  />"
				+ "</div><div class=\"footer-section\" style=\"margin-left: 29px;margin-top: 101px;font-family: Calibri;\">"
				+ " <div>Arun Kumar B A | Billing Engineer</div><div>Sumadhura Infracon Pvt. Ltd | Web: www.sumadhuragroup.com</div>"
				+ "<div><div>No. 43, C.K.B Plaza, 2nd Floor, Marathahalli, Bengaluru, Karnataka. India. 560037.</div></div>"
				+ "	<div><div>T:  91 7022009206 |  E: billingengineer@sumadhuragroup.com </div></div>  </div></body></html>";



			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress,new String[0],ccTo,"");




		}catch(Exception e){
			e.printStackTrace();
		}


	}




	public  void  sendMailToClosedIndents( String[] ccTo,userDetails objUserDetails ){


		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String emailSubjectText = "";

			emailSubjectText = "Indent Closed";

			String emailBodyMsgTxt = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
				+ "<title>Email Format</title><style> table tr td{ border:1px solid #000; height:29px; } </style> "
				+ "</head> <body> <div class=\"desc-icon-box\" style=\"margin-top: 29px; font-family: Calibri;\"> "
				+ "<div style='font-family: Calibri;font-size:14px;'class=\"greting1\">Dear User</div><br> <div style='font-family: Calibri;font-size:14px;' class=\"greting2\">Greetings!!!</div><br>"
				+ " <div style='font-family: Calibri;font-size:14px;' class=\"greting3\">Dear User, "+objUserDetails.getStrMesage()+"</div><br></div>" 
				+ "<form>"


				+ "</form>"

				//+ "<a href='http://localhost:8081/Sumadhura/vendorlogin.spring?indentNumber="+strIndentNumber+"&vendordata="+vendorDetails.getVendor_name()+"@@"+vendorDetails.getGsin_number()+"@@"+vendorDetails.getAddress()+"@@"+vendorDetails.getVendor_Id()+"'>click this link</a>"
				+ "<img  class=\"text-img\" style=\"margin-left: 179 !important;	float: left;position: relative;margin-left: 178px;bottom: -26px;right: -38px;\" src=\"cid:my-image-id\"  />"
				+ "</div><div class=\"footer-section\" style=\"margin-left: 29px;margin-top: 101px;font-family: Calibri;\">"
				+ " <div>"+objUserDetails.getUserName()+"</div><div>Sumadhura Infracon Pvt. Ltd | Web: www.sumadhuragroup.com</div>"
				+ "<div><div>No. 43, C.K.B Plaza, 2nd Floor, Marathahalli, Bengaluru, Karnataka. India. 560037.</div></div>"
				+ "	<div><div>T:  91 "+objUserDetails.getMobileNo()+" |  E: "+objUserDetails.getEmailId()+" </div></div>  </div></body></html>";



			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress,new String[0],ccTo,"");




		}catch(Exception e){
			e.printStackTrace();
		}


	}

	/**************************************************************************************************************/
	/***********************************         PAYMENT MODULE           *****************************************/
	/**************************************************************************************************************/

	public void sendPaymentApprovalMailToAccountsAllLevels(List<PaymentBean> successDataListToMail, String[] emailToAddress, int getLocalPort, String user_name){

		try {
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();

			String emailSubjectText = "Payment For Approval";
			//emailToAddress = new String[]{"mrafi@amaravadhis.com","mohammadrafishaik4@gmail.com"};//
			String emailBodyMsgTxt = "";
			/****************************************************/		
			String tableData = "<div> <table class=\"table \" style=\"border:1px solid #000;border-spacing: 0;border-collapse: collapse;width:100%;\">"
				+"<thead>  <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Payment Details ID</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Site/Department</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Vendor Name</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Invoice No</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">PO Number</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Payment Type</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Req Amount</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Remarks</th>"
				+"</tr>  </thead> <tbody>";


			for(PaymentBean successData : successDataListToMail){
				tableData+= " <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getIntPaymentDetailsId()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrSiteName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrVendorName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrInvoiceNo()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrPONo()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getPaymentType()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getDoubleAmountToBeReleased()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrRemarks()+"</td>"
					+"</tr>";

			}


			tableData+="</tbody></table></div><br><br><br><br>";

			/****************************************************/
			BufferedReader br = null;
			FileReader fr = null;

			fr = new FileReader("C:\\testing\\MailTemplates\\PaymentApprovalMail_AccountLevel.html");
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}

			emailBodyMsgTxt = emailBodyMsgTxt.replace("REQUESTED_EMPLOYEE", user_name);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("TABLE", tableData);



			/****************************/
			DynamicEmailService objDES = new DynamicEmailService();
			objDES.sendEmployeeEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, new String[0],"");


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void sendPaymentApprovalStatusMailToLowerEmployee(List<PaymentBean> successDataListToMail, String[] emailToAddress, int getLocalPort, String user_name){

		try {
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();

			String emailSubjectText = "Your Payment Approved";
			//emailToAddress = new String[]{"rafi341@yahoo.com","mrafi@amaravadhis.com"};//
			String emailBodyMsgTxt = "";
			/****************************************************/		
			String tableData = "<div> <table class=\"table \" style=\"border:1px solid #000;border-spacing: 0;border-collapse: collapse;width:100%;\">"
				+"<thead>  <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Payment Details ID</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Site/Department</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Vendor Name</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Invoice No</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">PO Number</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Payment Type</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Req Amount</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Remarks</th>"
				+"</tr>  </thead> <tbody>";


			for(PaymentBean successData : successDataListToMail){
				tableData+= " <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getIntPaymentDetailsId()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrSiteName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrVendorName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrInvoiceNo()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrPONo()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getPaymentType()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getDoubleAmountToBeReleased()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrRemarks()+"</td>"
					+"</tr>";

			}


			tableData+="</tbody></table></div><br><br><br><br>";

			/****************************************************/
			BufferedReader br = null;
			FileReader fr = null;

			fr = new FileReader("C:\\testing\\MailTemplates\\PaymentApprovedStatusMail.html");
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}

			emailBodyMsgTxt = emailBodyMsgTxt.replace("APPROVED_EMPLOYEE", user_name);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("TABLE", tableData);



			/****************************/
			DynamicEmailService objDES = new DynamicEmailService();
			objDES.sendEmployeeEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, new String[0],"");


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void sendPaymentApprovalMailToSiteSecondAndThirdLevel(List<PaymentBean> successDataListToMail, String[] emailToAddress, int getLocalPort, String strSiteId, String pendingEmpId, String user_name, String session_siteId){

		try {
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();

			/* if more than one Payments are came for approval,  groupOfPaymentDetailsId is combination of 
			 * all paymentDetailsId 's seperated with comma(,)  */
			String groupOfPaymentDetailsId = ""; 
			for(PaymentBean successData : successDataListToMail){
				groupOfPaymentDetailsId+=String.valueOf(successData.getIntPaymentDetailsId())+",";
			}
			if(StringUtils.isNotBlank(groupOfPaymentDetailsId)){
				groupOfPaymentDetailsId = groupOfPaymentDetailsId.substring(0,groupOfPaymentDetailsId.length()-1);
			}

			String url="";
			String ApproveUrl = validateParams.getProperty("MAIL_APPROVE_URL") == null ? "" : validateParams.getProperty("MAIL_APPROVE_URL").toString();
			String RejectUrl = validateParams.getProperty("MAIL_REJECT_URL") == null ? "" : validateParams.getProperty("MAIL_REJECT_URL").toString();
			String EditAndApproveUrl = validateParams.getProperty("MAIL_EDIT_AND_APPROVE_URL") == null ? "" : validateParams.getProperty("MAIL_EDIT_AND_APPROVE_URL").toString();

			if(getLocalPort == 8027){ //local machine
				ApproveUrl = "http://localhost:8027/Sumadhura/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Approve'";

				RejectUrl="http://localhost:8027/Sumadhura/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Reject";

				//EditAndApproveUrl="http://localhost:8027/Sumadhura/index.jsp?IndentApproval=";
				EditAndApproveUrl="http://localhost:8027/Sumadhura/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Edit";
				url="http://localhost:8027/Sumadhura/";
			}
			else if(getLocalPort == 8078){ //UAT
				ApproveUrl = "http://129.154.74.18:8078/Sumadhura_UAT/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Approve'";

				RejectUrl="http://129.154.74.18:8078/Sumadhura_UAT/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Reject";

				//EditAndApproveUrl="http://129.154.74.18:8078/Sumadhura_UAT/index.jsp?IndentApproval=";
				EditAndApproveUrl="http://129.154.74.18:8078/Sumadhura_UAT/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Edit";
				url="http://129.154.74.18:8078/Sumadhura_UAT/";
			}
			else if(getLocalPort == 8079){ //CUG
				ApproveUrl = "http://106.51.38.64:8079/Sumadhura_CUG/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Approve'";

				RejectUrl="http://106.51.38.64:8079/Sumadhura_CUG/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Reject";

				//EditAndApproveUrl="http://129.154.74.18:8079/Sumadhura_CUG/index.jsp?IndentApproval=";
				EditAndApproveUrl="http://106.51.38.64:8079/Sumadhura_CUG/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Edit";
				url="http://106.51.38.64:8079/Sumadhura_CUG/";

			}
			else if(getLocalPort == 80){ //LIVE
				ApproveUrl = "http://129.154.74.18/Sumadhura/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Approve'";

				RejectUrl="http://129.154.74.18/Sumadhura/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Reject";

				//EditAndApproveUrl="http://129.154.74.18/Sumadhura/index.jsp?IndentApproval=";
				EditAndApproveUrl="http://129.154.74.18/Sumadhura/PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&operationtype=Edit";

				url="http://129.154.74.18/Sumadhura/";

			}
			String emailSubjectText = "Payment For Approval";
			//emailToAddress = new String[]{"rafi341@yahoo.com","mrafi@amaravadhis.com","mohammadrafishaik4@gmail.com"};//
			String emailBodyMsgTxt = "";

			/****************************************************/		
			String tableData = "<div> <table class=\"table \" style=\"border:1px solid #000;border-spacing: 0;border-collapse: collapse;width:100%;\">"
				+"<thead>  <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Payment Details ID</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Site/Department</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Vendor Name</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Invoice No</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">PO Number</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Payment Type</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Req Amount</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Remarks</th>"
				+"</tr>  </thead> <tbody>";


			for(PaymentBean successData : successDataListToMail){
				tableData+= " <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getIntPaymentDetailsId()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrSiteName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrVendorName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">";
				if(successData.getStrInvoiceNo().equals("-")){
					tableData+="-";
				}
				else{
				tableData+= "<a href='"+url+"getInvoiceDetails.spring?invoiceNumber="+successData.getStrInvoiceNo()+"&vendorName="+successData.getStrVendorName()+"&vendorId="+successData.getStrVendorId()+"&IndentEntryId="+successData.getIntIndentEntryId()+"&SiteId="+successData.getStrSiteId()+"&invoiceDate="+successData.getStrInvoiceDate()+"'>"
					+successData.getStrInvoiceNo()
					+"</a>";
				}
				tableData+=  "</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">";
				if(successData.getStrPONo().equals("-")){
					tableData+="-";
				}	
				else{
				tableData+= "<a href='"+url+"getPoDetailsList.spring?poNumber="+successData.getStrPONo()+"&siteId="+successData.getStrSiteId()+"&fromMail=true'>"
					+successData.getStrPONo()
					+"</a> ";
				}
				tableData+=  "</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getPaymentType()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getDoubleAmountToBeReleased()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrRemarks()+"</td>"
					+"</tr>";

			}


			tableData+="</tbody></table></div><br><br><br><br>";

			/****************************************************/

			String Buttons    ="<div class='col-md-12 text-center center-block'> <form action='"+ApproveUrl+"' method='POST'>"
			/*+ "<span>Leave your comment : </span><input type='textarea' name='comment' plaholder='please leave a comment' 'style=' margin-top: 20px; style='font-weight:bold' />"*/
			+ "<input type='hidden' name='paymentDetailsId' value='"+groupOfPaymentDetailsId+"' />"
			+ "<input type='hidden' name='portNo' value='"+getLocalPort+"' />"
			+ "<input type='hidden' name='siteId' value='"+strSiteId+"' />"
			+ "<input type='hidden' name='sessionSiteId' value='"+session_siteId+"' />"
			+ "<input type='hidden' name='userId' value='"+pendingEmpId+"' />"
			+ "<input type='hidden' name='homePage' value='"+url+"' />"


			+"<div class='col-xs-4'>"+"<a href='"+url+"PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&homePage="+url+"&operationtype=Approve' class='btn btn-primary approve-btn-mail'style='width:75px;padding:6px;background-color:#337ab7;color:#fff;float:left;margin-right:5px;margin-left:5px;margin-top:10px;border-radius:5px;text-align:center;text-decoration:none;'>Approve</a>"+"</div>"
			//+ "<input type='submit' class='btn btn-primary' value='Approve' id='saveBtnId'  style='float: left;display:inline;width:130px;height:31px;background:#b4d4e3;margin-bottom: -28px;margin-top: -28px;'>"
			+ "</form>"

			+ "<form action='"+RejectUrl+"' method='POST'>"
			+ "<input type='hidden' name='paymentDetailsId' value='"+groupOfPaymentDetailsId+"' />"
			+ "<input type='hidden' name='portNo' value='"+getLocalPort+"' />"
			+ "<input type='hidden' name='siteId' value='"+strSiteId+"' />"
			+ "<input type='hidden' name='sessionSiteId' value='"+session_siteId+"' />"
			+ "<input type='hidden' name='userId' value='"+pendingEmpId+"' />"
			+ "<input type='hidden' name='homePage' value='"+url+"' />"

			+"<div class='col-xs-4'>"+"<a href='"+url+"PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&homePage="+url+"&operationtype=Reject' class='btn btn-primary reject-btn-mail' style='width:65px;padding:6px;background-color:#337ab7;color:#fff;float:left;margin-right:5px;margin-left:5px;margin-top:10px;border-radius:5px;text-align:center;text-decoration:none;'>Reject</a>"+"</div>"
			//+"<input type='submit' class='btn btn-primary myclass' value='Reject' id='saveBtnId' style='float: left;margin-left: 28px;display:inline;margin-top: -34px;width:130px;height:31px;background:#b4d4e3;'>"
			+ "</form>"
			
			+ "<form action='"+EditAndApproveUrl+"' method='POST'>"
			+ "<input type='hidden' name='paymentDetailsId' value='"+groupOfPaymentDetailsId+"' />"
			+ "<input type='hidden' name='portNo' value='"+getLocalPort+"' />"
			+ "<input type='hidden' name='siteId' value='"+strSiteId+"' />"
			+ "<input type='hidden' name='sessionSiteId' value='"+session_siteId+"' />"
			+ "<input type='hidden' name='userId' value='"+pendingEmpId+"' />"
			+ "<input type='hidden' name='homePage' value='"+url+"' />"

			+"<div class='col-xs-4'>"+"<a href='"+url+"PaymentApprovalRejectMailFunction.jsp?paymentDetailsId="+groupOfPaymentDetailsId+"&portNo="+getLocalPort+"&siteId="+strSiteId+"&sessionSiteId="+session_siteId+"&userId="+pendingEmpId+"&homePage="+url+"&operationtype=Edit'  style='width:125px;padding:6px;background-color:#337ab7;color:#fff;float:left;margin-right:5px;margin-left:5px;margin-top:10px;border-radius:5px;text-align:center;text-decoration:none;'>Edit & Approve</a>"+"</div>"
			
			+ "</form>"+"</div>";

			/****************************************************/
			BufferedReader br = null;
			FileReader fr = null;

			fr = new FileReader("C:\\testing\\MailTemplates\\PaymentApprovalMail_SiteLevel.html");
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}

			emailBodyMsgTxt = emailBodyMsgTxt.replace("REQUESTED_EMPLOYEE", user_name);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("TABLE", tableData);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("BUTTONS", Buttons);




			DynamicEmailService objDES = new DynamicEmailService();
			objDES.sendEmployeeEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, new String[0],"");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//reject Mail
	public void sendPaymentRejectedStatusMail(List<PaymentBean> successDataListToMail, String[] emailToAddress, int getLocalPort, String user_name,String EmailBodyMessage){

		try {
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();

			String emailSubjectText = "Payment Rejected";
			//emailToAddress = new String[]{"rafi341@yahoo.com","mrafi@amaravadhis.com"};//
			String emailBodyMsgTxt = "";
			/****************************************************/		
			String tableData = "<div> <table class=\"table \" style=\"border:1px solid #000;border-spacing: 0;border-collapse: collapse;width:100%;\">"
				+"<thead>  <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Payment Details ID</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Site/Department</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Vendor Name</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Invoice No</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">PO Number</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Payment Type</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Req Amount</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Remarks</th>"
				+"</tr>  </thead> <tbody>";


			for(PaymentBean successData : successDataListToMail){
				tableData+= " <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getIntPaymentDetailsId()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrSiteName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrVendorName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrInvoiceNo()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrPONo()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getPaymentType()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getDoubleAmountToBeReleased()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getStrRemarks()+"</td>"
					+"</tr>";

			}


			tableData+="</tbody></table></div><br><br><br><br>";

			/****************************************************/
			BufferedReader br = null;
			FileReader fr = null;

			fr = new FileReader("C:\\testing\\MailTemplates\\PaymentRejectedStatusMail.html");
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}

			emailBodyMsgTxt = emailBodyMsgTxt.replace("BODY_MESSAGE", EmailBodyMessage);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("REJECTED_EMPLOYEE", user_name);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("TABLE", tableData);



			/****************************/
			DynamicEmailService objDES = new DynamicEmailService();
			objDES.sendEmployeeEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, new String[0],"");


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/*======================================================================TEMP PO MAIL START===========================================================================*/

	public  void  sendMailForTempApprove(String strApproverName,String approveToEmail[],int getLocalPort,Object []ApproveData){

		int count=1;
		String strRemarks="";
		String productDetails="";
		String url="";
		String ApproveUrl ="";
		String RejectUrl ="";
		String EditAndApproveUrl ="";
		String cancelUrl="";
		try{
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String path=""; //C:\\Sumadhura\\template\\TempPoMail.html";
			String emailSubjectText = "You Have A Po For Approval";
			if(getLocalPort==8027){ //LOCAL

				url="http://localhost:8027/Sumadhura/getDetailsforPoApproval.spring?";
				ApproveUrl="http://localhost:8027/Sumadhura/SavePoApproveDetails.spring?";
				RejectUrl="http://localhost:8027/Sumadhura/TempPoRejectCancel.jsp?";
				cancelUrl="http://localhost:8027/Sumadhura/TempPoRejectCancel.jsp?";
				path="C:\\Sumadhura\\template\\TempPoMail.html";
			}else if(getLocalPort == 8078){ //UAT

				url="http://129.154.74.18:8078/Sumadhura_UAT/getDetailsforPoApproval.spring?";
				ApproveUrl="http://129.154.74.18:8078/Sumadhura_UAT/SavePoApproveDetails.spring?";
				RejectUrl="http://129.154.74.18:8078/Sumadhura_UAT/TempPoRejectCancel.jsp?";
				cancelUrl="http://129.154.74.18:8078/Sumadhura_UAT/TempPoRejectCancel.jsp?";
				path="C:\\testing\\MailTemplates\\TempPoMail.html";
			}else if(getLocalPort == 8079){ //CUG

				url="http://106.51.38.64:8079/Sumadhura_CUG/getDetailsforPoApproval.spring?";
				ApproveUrl="http://106.51.38.64:8079/Sumadhura_CUG/SavePoApproveDetails.spring?";
				RejectUrl="http://106.51.38.64:8079/Sumadhura_CUG/TempPoRejectCancel.jsp?";
				cancelUrl="http://106.51.38.64:8079/Sumadhura_CUG/TempPoRejectCancel.jsp?";
				path="C:\\testing\\MailTemplates\\TempPoMail.html";
			}else if(getLocalPort == 80){ //LIVE

				url="http://129.154.74.18/Sumadhura/getDetailsforPoApproval.spring?";
				ApproveUrl="http://129.154.74.18/Sumadhura/SavePoApproveDetails.spring?";
				RejectUrl="http://129.154.74.18/Sumadhura/TempPoRejectCancel.jsp?";
				cancelUrl="http://129.154.74.18/Sumadhura/TempPoRejectCancel.jsp?";
				path="C:\\testing\\MailTemplates\\TempPoMail.html";
			}

			sendSSLMessageForTempPO(approveToEmail, emailSubjectText,
					ApproveData, emailFromAddress, new String[0],url,ApproveUrl,RejectUrl,cancelUrl,path);

			//sendEmail(ApproveData, emailSubjectText, emailFromAddress, approveToEmail, new String[0],"");
			//	}

		}catch(Exception e){
			e.printStackTrace();
		}
	}



	/*======================================================================TEMP PO MAIL END===========================================================================*/
	/*===============================================================TEMP PO REJECT START ========================================================================*/

	public void sendMailForTempPoReject(String strApproveName,String []approveToEmailAddress,Object [] ApproveData,String subject,int strPortNo){
		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String path="";
			String url = ""; 

			if(strPortNo == 8078){ 
				path="C:\\testing\\MailTemplates\\reject.html";
				 url="http://129.154.74.18:8078/Sumadhura_UAT/getDetailsforPoApproval.spring?";
			}
			else if(strPortNo == 8079){ 
				path="C:\\testing\\MailTemplates\\reject.html";
				 url="http://106.51.38.64:8079/Sumadhura_CUG/getDetailsforPoApproval.spring?";

			}
			else if(strPortNo == 80){ 
				path="C:\\testing\\MailTemplates\\reject.html";
				 url="http://129.154.74.18:80/Sumadhura/getDetailsforPoApproval.spring?";

			}
			
			sendSSLMessageForTempPO(approveToEmailAddress, subject,
					ApproveData, emailFromAddress, new String[0],url,"","","",path);

		}catch(Exception e){
			e.printStackTrace();
		}	

	}


	/*===============================================================TEMP PO REJECT END ========================================================================*/
	//========================================================================================================================================		
	
	/***************************************************************indent Reject mails start*******************************************************************/
	public void sendMailForRejectIndent(String strIndentNumber,String []ToEmailAddress,int strPortNo,String user_name,String strRemarks,String rejectEmpDesignation,String siteName,String indentName){
		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String emailSubjectText = "Indent Rejected";
			String emailBodyMsgTxt = "";
			//String EmailBodyMessage=strIndentNumber;
			
			/****************************************************/		
			String tableData = "<div> <table class=\"table \" style=\"border:1px solid #000;border-spacing: 0;border-collapse: collapse;width:100%;\">"
				+"<thead>  <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">INDENT NUMBER</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">REJECTED EMPLOYEE NAME</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">REJECTED SITE NAME</th>"
				+"</tr>  </thead> <tbody>";

			tableData+= " <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+strIndentNumber+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+user_name+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+siteName+"</td>"
					+"</tr>";
			tableData+="</tbody></table></div><br><br><br><br>";

			/****************************************************/
			
			
			/****************************************************/
			BufferedReader br = null;
			FileReader fr = null;
			//C:\\testing\\MailTemplates\\PaymentRejectedStatusMail.html
			fr = new FileReader("C:\\testing\\MailTemplates\\IndentRejectStatus.html");
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}

			//emailBodyMsgTxt = emailBodyMsgTxt.replace("INDENT_NUMBER", strIndentNumber);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("REJECTED_EMPLOYEE", user_name);
			//emailBodyMsgTxt = emailBodyMsgTxt.replace("DESIGNATION", rejectEmpDesignation);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("COMMENT",strRemarks);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("TABLE",tableData);

			/****************************/
			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, ToEmailAddress, new String[0],"");

		}catch(Exception e){
			e.printStackTrace();
		}	

	}
	
	/*====================================================MARKETING PO MAILS===========================================================================*/
	public  void  sendMailToVendorForMarketingPO(String strIndentRaised,int strIndentNumber,String strIndentFrom,
			String strIndentSite,String strIndentCreationDate,String [] emailToAddress ,VendorDetails vendorDetails ,
			String poNumber, String[] ccTo,String subject,String poCreatedEmpName,int getLocalPort,String contact_person_Name,String revisedOrNot,
			String oldpoNumber,String poDate,String acc_Note_Email){

		String url="";

		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String VendorLoginUrl  = validateParams.getProperty("VENDOR_LOGIN_URL") == null ? "" : validateParams.getProperty("VENDOR_LOGIN_URL").toString();
			if(getLocalPort == 8078){

				VendorLoginUrl="http://129.154.74.18:8078/Sumadhura_UAT/showMarketingPODetailsToVendor.spring";
				url="http://129.154.74.18:8078/Sumadhura_UAT/showMarketingPODetailsToVendor.spring";

			}else if(getLocalPort == 8079){
				VendorLoginUrl="http://106.51.38.64:8079/Sumadhura_CUG/showMarketingPODetailsToVendor.spring";
				url="http://106.51.38.64:8079/Sumadhura_CUG/showMarketingPODetailsToVendor.spring";

			}else if(getLocalPort == 80){

				VendorLoginUrl="http://129.154.74.18/Sumadhura/showMarketingPODetailsToVendor.spring";
				url="http://129.154.74.18/Sumadhura/showMarketingPODetailsToVendor.spring";
			}

			String emailSubjectText = "";
			String emailBodyMsgTxt ="";
			if(subject==null || subject.equals("") || subject.equals("-")){
				emailSubjectText = "Sumadhura PO";
			}
			else{
				emailSubjectText = subject;
			}

			String data[] = poCreatedEmpName.split(",");
			String strEmpName=data[0];
			String strEmpEmailId=data[1];
			String strMobiloeNumber=data[2];
			//	String vendorId=vendorDetails.getVendor_Id();

			//System.out.println("email site id....."+strIndentFrom);
			AESDecrypt encrypt=new AESDecrypt();


			String userData="poNumber="+poNumber+"&indentSiteId="+strIndentFrom;

			String strEncryptionData =encrypt.encrypt("AMARAVADHIS12345",userData);
			strEncryptionData =  strEncryptionData.replaceAll("[\\t\\n\\r]+","");
			
			if(revisedOrNot.equalsIgnoreCase("true")){
				/**************************************this is for revised po start******************************************************/
				BufferedReader br = null;
				FileReader fr = null;

				fr = new FileReader("C:\\testing\\MailTemplates\\RevisedPo.html");
				//fr = new FileReader("F:\\RevisedPo.html");
				br = new BufferedReader(fr);

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					System.out.println(sCurrentLine);
					emailBodyMsgTxt+="\n"+sCurrentLine;
				}
				emailBodyMsgTxt = emailBodyMsgTxt.replace("VENDOR_NAME",contact_person_Name);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_DATE",poDate);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("OLD_PO_NUMBER ",oldpoNumber);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("REVISED_PO_NUMBER",poNumber);
				
				emailBodyMsgTxt = emailBodyMsgTxt.replace("VendorLoginUrl",VendorLoginUrl);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("url",url);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("strEncryptionData",strEncryptionData);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("po_Number",poNumber);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("strIndentFrom",strIndentFrom);
				
				emailBodyMsgTxt = emailBodyMsgTxt.replace("strEmpName",strEmpName);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("strMobiloeNumber",strMobiloeNumber);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("strEmpEmailId",strEmpEmailId);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("Note_payment",acc_Note_Email);
				/**************************************this is for revised po end******************************************************/

			}
			else{
				emailBodyMsgTxt = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />"
					+ "<title>Email Format</title><style> table tr td{ border:1px solid #000; height:29px; } </style> "
					+ "</head> <body> <div class=\"desc-icon-box\" style=\"margin-top: 29px; font-family: Calibri;\"> "
					+ "<div style='font-family: Calibri;font-size:14px;'class=\"greting1\">Dear "+contact_person_Name+"</div><br> <div style='font-family: Calibri;font-size:14px;' class=\"greting2\">Greetings!!!</div><br>"
					+ " <div style='font-family: Calibri;font-size:14px;' class=\"greting3\">You have one PO, for more details click below Button</div><br></div> "

					+ "<div>"
					+" <a href='"+VendorLoginUrl+"?poNumber="+poNumber+"&indentSiteId="+strIndentFrom+"&submitButton=true' class=\"btn btn-warning btn-margin\" style=\"padding:6px 12px;text-decoration:none;background-color: #337ab7;border-color: #2e6da4;color:#fff;border-radius:5px;\">Click Here</a>"
					/*+ "<form method='POST' action='"+VendorLoginUrl+"' />"
					+ "<input type='hidden' name='poNumber' value='"+poNumber+"' />"
					+ "<input type='hidden' name='indentSiteId' value='"+strIndentFrom+"' />"

					+"<input type='hidden' name='submitButton' value='true' />"*/
					/*+ "<input type='submit' value='Click Here'style=\"background: #b4d4e3;width: 158px;height: 29px;margin-left: 423px;margin-top: 50px;\" />"*/	
					/*+"<input type='submit' value='Click Here'/>"
					+ "</form>"*/
	                +"<div style='clear:both;margin-top:10px;padding-left:23px;'>Or</div>"
					+ "  <a href='"+url+"?data="+strEncryptionData+"'>Click this link</a>"
					+ "<img  class=\"text-img\" style=\"margin-left: 179 !important;	float: left;position: relative;margin-left: 178px;bottom: -26px;right: -38px;\" src=\"cid:my-image-id\"  />"
					+ "</div><div class=\"footer-section\" style=\"margin-top: 58px;font-family: Calibri;font-size: 14px;\">"
					+ " <div>"+strEmpName+"</div><div>Sumadhura Infracon Pvt. Ltd | Web: www.sumadhuragroup.com</div>"
					+ "<div><div>No. 43, C.K.B Plaza, 2nd Floor, Marathahalli, Bengaluru, Karnataka. India. 560037.</div></div>"
					+ "	<div><div>T:"+strMobiloeNumber+"|  E:"+strEmpEmailId+"</div></div>  </div></body></html>";
				}

			sendEmail(emailBodyMsgTxt,emailSubjectText,emailFromAddress,emailToAddress,ccTo,strIndentFrom);

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/*=============================================marketing po cancel  start=================================================================*/
	public void sendMailForTempMarketingPoReject(String strApproveName,String []approveToEmailAddress,Object [] ApproveData,String subject,int strPortNo){
		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String path="";
			String url = ""; 

			if(strPortNo == 8078){ 
				path="C:\\testing\\MailTemplates\\reject.html";
				 url="http://129.154.74.18:8078/Sumadhura_UAT/getDetailsforMarketingPoApproval.spring?";
			}
			else if(strPortNo == 8079){ 
				path="C:\\testing\\MailTemplates\\reject.html";
				 url="http://106.51.38.64:8079/Sumadhura_CUG/getDetailsforMarketingPoApproval.spring?";

			}
			else if(strPortNo == 80){ 
				path="C:\\testing\\MailTemplates\\reject.html";
				 url="http://129.154.74.18/Sumadhura/getDetailsforMarketingPoApproval.spring?";

			}
			
			sendSSLMessageForTempPO(approveToEmailAddress, subject,
					ApproveData, emailFromAddress, new String[0],url,"","","",path);

		}catch(Exception e){
			e.printStackTrace();
		}	

	}
/*=========================================================cancelo marketing po page end=======================================================*/
	/*======================================================================MARKETING TEMP PO MAIL START===========================================================================*/

	public  void  sendMailForMarketingTempApprove(String strApproverName,String approveToEmail[],int getLocalPort,Object []ApproveData){

		int count=1;
		String strRemarks="";
		String productDetails="";
		String url="";
		String ApproveUrl ="";
		String RejectUrl ="";
		String EditAndApproveUrl ="";
		String cancelUrl="";
		String emailBodyMsgTxt ="";
		try{
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String path=""; //C:\\Sumadhura\\template\\TempPoMail.html";
			String emailSubjectText = "You Have A Po For Approval";
			if(getLocalPort==8080){ //LOCAL

				url="http://localhost:8080/Sumadhura_UAT/getDetailsforMarketingPoApproval.spring?";
				ApproveUrl="http://localhost:8080/Sumadhura_UAT/SaveMarketingPoApproveDetails.spring?";
				RejectUrl="http://localhost:8080/Sumadhura_UAT/TempMarketingPoRejectCancel.jsp?";
				cancelUrl="http://localhost:8080/Sumadhura_UAT/TempMarketingPoRejectCancel.jsp?";
				//path="C:\\Sumadhura\\template\\TempPoMail.html";
			}else if(getLocalPort == 8078){ //UAT

				url="http://129.154.74.18:8078/Sumadhura_UAT/getDetailsforMarketingPoApproval.spring?";
				ApproveUrl="http://129.154.74.18:8078/Sumadhura_UAT/SaveMarketingPoApproveDetails.spring?";
				RejectUrl="http://129.154.74.18:8078/Sumadhura_UAT/TempMarketingPoRejectCancel.jsp?";
				cancelUrl="http://129.154.74.18:8078/Sumadhura_UAT/TempMarketingPoRejectCancel.jsp?";
				//path="C:\\testing\\MailTemplates\\TempPoMail.html";
			}else if(getLocalPort == 8079){ //CUG

				url="http://106.51.38.64:8079/Sumadhura_CUG/getDetailsforMarketingPoApproval.spring?";
				ApproveUrl="http://106.51.38.64:8079/Sumadhura_CUG/SaveMarketingPoApproveDetails.spring?";
				RejectUrl="http://106.51.38.64:8079/Sumadhura_CUG/TempMarketingPoRejectCancel.jsp?";
				cancelUrl="http://106.51.38.64:8079/Sumadhura_CUG/TempMarketingPoRejectCancel.jsp?";
				//path="C:\\testing\\MailTemplates\\TempPoMail.html";
			}else if(getLocalPort == 80){ //LIVE

				url="http://129.154.74.18/Sumadhura/getDetailsforMarketingPoApproval.spring?";
				ApproveUrl="http://129.154.74.18/Sumadhura/SaveMarketingPoApproveDetails.spring?";
				RejectUrl="http://129.154.74.18/Sumadhura/TempMarketingPoRejectCancel.jsp?";
				cancelUrl="http://129.154.74.18/Sumadhura/TempMarketingPoRejectCancel.jsp?";
				//path="C:\\testing\\MailTemplates\\TempPoMail.html";
			}
			
			BufferedReader br = null;
			FileReader fr = null;

			fr = new FileReader("C:\\testing\\MailTemplates\\TempMarkertingPoMail.html");
			//fr = new FileReader("F:\\RevisedPo.html");
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}
			emailBodyMsgTxt = emailBodyMsgTxt.replace("ApproveName",String.valueOf(ApproveData[11]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("DummyDate",String.valueOf(ApproveData[5]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("url",url+"&siteId="+String.valueOf(ApproveData[2]+"&poNumber="+String.valueOf(ApproveData[6])+"&siteLevelPoPreparedBy="+String.valueOf(ApproveData[12])+"&siteName="+String.valueOf(ApproveData[8])+
					"&password="+String.valueOf(ApproveData[13])+"&siteWiseIndentNo="+String.valueOf(ApproveData[7])+"&userId="+String.valueOf(ApproveData[1])+
					"&vendorId="+String.valueOf(ApproveData[15])+"&oldPOEntryId="+String.valueOf(ApproveData[16])+"&oldPODate="+String.valueOf(ApproveData[17])));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("strPONum",String.valueOf(ApproveData[6]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("strSitewiseIndentNo",String.valueOf(ApproveData[7]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("tempsiteName",String.valueOf(ApproveData[8]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("VendorName",String.valueOf(ApproveData[9]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("DummyTotal",String.valueOf(ApproveData[10]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("approveUrl",ApproveUrl+"&strIndentNo="+String.valueOf(ApproveData[0])+"&userId="+String.valueOf(ApproveData[1])+"&siteId="+String.valueOf(ApproveData[2]+"&revision_No="+String.valueOf(ApproveData[3])+
					"&oldPoNumber="+String.valueOf(ApproveData[4])+"&strPONumber="+String.valueOf(ApproveData[6])+"&siteName="+String.valueOf(ApproveData[8])+"&siteLevelPoPreparedBy="+String.valueOf(ApproveData[12])+"&password="+String.valueOf(ApproveData[13])+"&Email=true")+
					"&vendorId="+String.valueOf(ApproveData[15])+"&oldPOEntryId="+String.valueOf(ApproveData[16])+"&oldPODate="+String.valueOf(ApproveData[17]));
			
			emailBodyMsgTxt = emailBodyMsgTxt.replace("rejectUrl",RejectUrl+"&strPONumber="+String.valueOf(ApproveData[6])+"&siteId="+String.valueOf(ApproveData[2])+"&password="+String.valueOf(ApproveData[13])+"&userId="+String.valueOf(ApproveData[1])+"&cancelOrReject=reject"+"&port="+String.valueOf(ApproveData[14])+"&Email=true");
			emailBodyMsgTxt = emailBodyMsgTxt.replace("cancelUrl",cancelUrl+"&strPONumber="+String.valueOf(ApproveData[6])+"&siteId="+String.valueOf(ApproveData[2])+"&password="+String.valueOf(ApproveData[13])+"&userId="+String.valueOf(ApproveData[1])+"&cancelOrReject=cancel"+"&port="+String.valueOf(ApproveData[14])+"&Email=true");
			
			emailBodyMsgTxt = emailBodyMsgTxt.replace("strComment",String.valueOf(ApproveData[1]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("type",String.valueOf(ApproveData[12]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("RejectOrCancel",url+"&siteId="+String.valueOf(ApproveData[2])+"&poNumber="+String.valueOf(ApproveData[6])+"&siteName="+String.valueOf(ApproveData[8])+
					"&password="+String.valueOf(ApproveData[13])+"&siteWiseIndentNo="+String.valueOf(ApproveData[7])+"&mail=true");
			
			//sendSSLMessageForTempPO(approveToEmail, emailSubjectText,
					//ApproveData, emailFromAddress, new String[0],url,ApproveUrl,RejectUrl,cancelUrl,path);

			sendEmail(emailBodyMsgTxt,emailSubjectText,emailFromAddress,approveToEmail,new String[0],"");
			//	}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	/*****************************************cancel po mail send to vendor for after cancel po**********************************************************/
	public void sendMailForVendorCancelPo(String poNumber,String[] ccTo,String contact_person_Name,String poDate,String siteName,String vendorLevelComments,
			String [] emailto,String paymentLevelComment,String mailData,int portNumber){
		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String uat_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT").toString();
			String cug_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG").toString();
			String live_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE").toString();
			
			String emailSubjectText = "";
			String emailBodyMsgTxt = "";
			
			String src="";
			if(portNumber == 8078){
				src=uat_image_path;
			}else if(portNumber == 8079){
				src=cug_image_path;	
			}else if(portNumber == 80){
				src=live_image_path;
			}
			
			String data[] = mailData.split(",");
			String strEmpName=data[0];
			String strEmpEmailId=data[1];
			String strMobileNumber=data[2];
			
			BufferedReader br = null;
			FileReader fr = null;
			if(paymentLevelComment.equals("-")){
				 emailSubjectText = "Regarding Cancellation of Purchase Order from Sumadhura Infracon Pvt Lmtd.";
				fr = new FileReader("C:\\testing\\MailTemplates\\perminentPoCancel.html");
			}else{
			 emailSubjectText = "Regarding Cancellation of Purchase Order from Sumadhura Infracon Pvt Lmtd.";
		fr = new FileReader("C:\\testing\\MailTemplates\\perminentPoCancelAccounts.html");
			}
			br = new BufferedReader(fr);

			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}

			//emailBodyMsgTxt = emailBodyMsgTxt.replace("SUBJECT", emailSubjectText);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Number", poNumber);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("VENDORNAME",contact_person_Name); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Date",poDate); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("SITE_NAME",siteName); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("VENDOR_COMMENTS",vendorLevelComments);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("strEmpName",strEmpName);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("strMobiloeNumber",strMobileNumber);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("strEmpEmailId",strEmpEmailId);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("src_image",src);
			//emailBodyMsgTxt = emailBodyMsgTxt.replace("PAYMENT_NOTE",paymentLevelComment); // payment time it will effect

			/****************************/
			sendEmail(emailBodyMsgTxt,emailSubjectText,emailFromAddress,emailto,ccTo,"");

		}catch(Exception e){
			e.printStackTrace();
		}	

	}
	
	public void sendMailForRejectCancelPerminentPo(String [] emailto,String comments,Object[] rejectedData,String type){ // here type is used to approve or reject why because same html use here
		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String emailSubjectText = String.valueOf(rejectedData[4]);
			String emailBodyMsgTxt = "";
			
			
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader("C:\\testing\\MailTemplates\\PermanentCancelPoReject.html");
			br = new BufferedReader(fr);

			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}

			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Number",String.valueOf(rejectedData[0]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Sir",String.valueOf(rejectedData[3]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Rejected_Name",String.valueOf(rejectedData[6]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Vendor_Name",String.valueOf(rejectedData[5])); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("TYPE",type); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Date",String.valueOf(rejectedData[1])); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("site_Name",String.valueOf(rejectedData[2])); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("COMMENTS",comments);
			

			/****************************/
			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress,emailto,new String[0],"");

		}catch(Exception e){
			e.printStackTrace();
		}	

	}
	
	/************************************************perminent cancel poApproval**********************************************************************
*/
	
	public void sendMailForPerminentPOCancelApprove(String strApproveName,String []approveToEmailAddress,int getLocalPort,Object [] ApproveData){
		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String emailSubjectText = "Permanent Cancel Po Approval";
			String emailBodyMsgTxt = "";
			//String EmailBodyMessage=strIndentNumber;
			String path="";
			String ApproveUrl="";
			String RejectUrl="";
			String url="";
			if(getLocalPort==8086){ //LOCAL
				url="http://localhost:8086/Sumadhura/showPerminentPODetailsToCancel.spring?";
				ApproveUrl="http://localhost:8086/Sumadhura/PerminentCancelPoApproveReject.jsp?";
				RejectUrl="http://localhost:8086/Sumadhura/PerminentCancelPoApproveReject.jsp?";
				
			}else if(getLocalPort == 8078){ //UAT
				url="http://129.154.74.18:8078/Sumadhura_UAT/showPerminentPODetailsToCancel.spring?";
				ApproveUrl="http://129.154.74.18:8078/Sumadhura_UAT/PerminentCancelPoApproveReject.jsp?";
				RejectUrl="http://129.154.74.18:8078/Sumadhura_UAT/PerminentCancelPoApproveReject.jsp?";
				
			}else if(getLocalPort == 8079){ //CUG
				url="http://106.51.38.64:8079/Sumadhura_CUG/showPerminentPODetailsToCancel.spring?";
				ApproveUrl="http://106.51.38.64:8079/Sumadhura_CUG/PerminentCancelPoApproveReject.jsp?";
				RejectUrl="http://106.51.38.64:8079/Sumadhura_CUG/PerminentCancelPoApproveReject.jsp?";
				
			}else if(getLocalPort == 80){ //LIVE
				url="http://129.154.74.18/Sumadhura/showPerminentPODetailsToCancel.spring?";
				ApproveUrl="http://129.154.74.18/Sumadhura/PerminentCancelPoApproveReject.jsp?";
				RejectUrl="http://129.154.74.18/Sumadhura/PerminentCancelPoApproveReject.jsp?";
				
			}

			/****************************************************/
			
			
			/****************************************************/
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader("C:\\testing\\MailTemplates\\ApproveCancelPeminentPo.html");
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}
			
			emailBodyMsgTxt = emailBodyMsgTxt.replace("ApproveName",strApproveName);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("DummyDate",String.valueOf(ApproveData[1]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("strPONum",String.valueOf(ApproveData[0]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("tempsiteName",String.valueOf(ApproveData[2]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("VENDOR_NAME",String.valueOf(ApproveData[10]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("DummyTotal",String.valueOf(ApproveData[3]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("INITIATE_NAME",String.valueOf(ApproveData[12]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("INTERNAL_COMMENTS",String.valueOf(ApproveData[11]));
			
			emailBodyMsgTxt = emailBodyMsgTxt.replace("url",url+"&poNumber="+String.valueOf(ApproveData[0])+"&siteId="+String.valueOf(ApproveData[4])+"&mailPassword="+String.valueOf(ApproveData[5])+"&siteName="+String.valueOf(ApproveData[2])+"&userId="+String.valueOf(ApproveData[7])+"&poType="+String.valueOf(ApproveData[13])+"&isApprove=true&isMail=true");
			emailBodyMsgTxt = emailBodyMsgTxt.replace("approveUrl",ApproveUrl+"&poNumber="+String.valueOf(ApproveData[0])+"&siteId="+String.valueOf(ApproveData[4])+"&mailPassword="+String.valueOf(ApproveData[5])+"&userId="+String.valueOf(ApproveData[7])+
					"&siteName="+String.valueOf(ApproveData[2])+"&poDate="+String.valueOf(ApproveData[1])+"&vendorId="+String.valueOf(ApproveData[8])+"&port="+String.valueOf(getLocalPort)+"&poEntryId="+String.valueOf(ApproveData[6])+"&poType="+String.valueOf(ApproveData[13])+"&indentNumber="+String.valueOf(ApproveData[9])+"&typeOfPo=Approve");
			emailBodyMsgTxt = emailBodyMsgTxt.replace("rejectUrl",RejectUrl+"&poEntryId="+String.valueOf(ApproveData[6])+"&mailPassword="+String.valueOf(ApproveData[5])+"&port="+String.valueOf(getLocalPort)+"&poNumber="+String.valueOf(ApproveData[0])+"&indentNumber="+String.valueOf(ApproveData[9])+"&poType="+String.valueOf(ApproveData[13])+
					"&siteName="+String.valueOf(ApproveData[2])+"&poDate="+String.valueOf(ApproveData[1])+"&vendorId="+String.valueOf(ApproveData[8])+"&userId="+String.valueOf(ApproveData[7])+"&typeOfPo=Reject");

			/****************************/
			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress,approveToEmailAddress, new String[0],"");

		}catch(Exception e){
			e.printStackTrace();
		}	

	}
	
	/*
	 * @description this method is for sending the settlement email's to indent site and sender site
	 */
	public void sendMailForIndentSettlement(IndentCreationBean indentCreationBean , int reqSiteId,  List<String>  ccMailsOfIndentEmployee, List<String> senderSiteMail, StringBuffer tableDataIndentSettlementForMail, StringBuffer indentSettlementSubjectForMail, String condition) {
		try{
			int portNumber=8080;
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			
			//this is the image path
	/*		String uat_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT").toString();
			String cug_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG").toString();
			String live_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE").toString();*/
			String emailSubjectText = indentSettlementSubjectForMail.toString();
			String emailBodyMsgTxt = "";
			
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader("C:\\testing\\MailTemplates\\mail_central.html");
			if(condition.equals("false")){
				fr = new FileReader("C:\\testing\\MailTemplates\\mail_central -indent.html");	
			}else if(condition.equals("ViewMyReqPage")){
				fr = new FileReader("C:\\testing\\MailTemplates\\mail_central - view my req.html");
			}
			br = new BufferedReader(fr);
			/*String src="";
			if(portNumber==8078){
				src=uat_image_path;
			}else if(portNumber==8079){
				src=cug_image_path;	
			}else if(portNumber==80){
				src=live_image_path;
			}*/
				
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}
			if(condition.equals("true")||condition.equals("ViewMyReqPage")){
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Site wise Indent number//",indentCreationBean.getSiteWiseIndentNumber());
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Indent Site//",indentCreationBean.getSiteName()); 
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Indent site//",indentCreationBean.getSiteName()); 
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Sender site//",indentCreationBean.getSenderSiteName()); 
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Sender Site//",indentCreationBean.getSenderSiteName()); 
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Indent Site name as Vendor Details table//",indentCreationBean.getSiteName());
				emailBodyMsgTxt = emailBodyMsgTxt.replace("SETTLEMENT_TABLE_DATA",tableDataIndentSettlementForMail);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Settled Employee Name//",indentCreationBean.getIndentSettledEmployeeName());
				
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Settled employee number//",indentCreationBean.getCurrentUserMobileNumber()==null?"":indentCreationBean.getCurrentUserMobileNumber());
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Settled employee Mail//",indentCreationBean.getCurrentUserEemail()==null?"":indentCreationBean.getCurrentUserEemail());
			}else{
				emailBodyMsgTxt = emailBodyMsgTxt.replace("SETTLEMENT_TABLE_DATA",tableDataIndentSettlementForMail);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Receiver Site//",indentCreationBean.getSiteName());
				
			}
		
			//T:- ://Settled employee number// E://Settled employee Mail// 
			/*emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Amount",String.valueOf(ApproveData[2])); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Request_Amount",String.valueOf(ApproveData[3]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("INTERNAL_COMMENTS",String.valueOf(ApproveData[4]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("VENDOR_COMMENTS",String.valueOf(ApproveData[5]));*/
			
			
			//payment time it will effectsrc_image
			//emailBodyMsgTxt = emailBodyMsgTxt.replace("src_image",src);
			
			String emailToAddress[]=senderSiteMail.toArray(new String[0]);
			
			String ccTo[]=ccMailsOfIndentEmployee.toArray(new String[0]);
			/****************************/
			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress,emailToAddress,ccTo,"");

		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	

	public void sendMailForIndentSettlement(IssueToOtherSiteDto issueToOtherSiteDtoForMail, int reqSiteId,List<String>  ccMailsOfIndentEmployee, List<String> senderSiteMail, StringBuffer tableDataIndentSettlementForMail,
			StringBuffer indentSettlementSubjectForMail, String condition) {
		try{
			int portNumber=8080;
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
		
			String emailSubjectText = indentSettlementSubjectForMail.toString();
			String emailBodyMsgTxt = "";
			
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader("C:\\testing\\MailTemplates\\mail_central inwards from other site.html");

			br = new BufferedReader(fr);
			
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}
			
	
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Indent Number//",issueToOtherSiteDtoForMail.getSiteWiseIndentNumber());//
				emailBodyMsgTxt = emailBodyMsgTxt.replaceAll("//Indent Site//",issueToOtherSiteDtoForMail.getSiteName()); 
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Indent site//",issueToOtherSiteDtoForMail.getSiteName()); 
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Sender site//",issueToOtherSiteDtoForMail.getSenderSiteName()); 
				//emailBodyMsgTxt = emailBodyMsgTxt.replace("//Sender Site//",issueToOtherSiteDtoForMail.getSenderSiteName()); 
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Indent Site name as Vendor Details table//",issueToOtherSiteDtoForMail.getSiteName());
				emailBodyMsgTxt = emailBodyMsgTxt.replace("SETTLEMENT_TABLE_DATA",tableDataIndentSettlementForMail);
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Settled Employee Name//",issueToOtherSiteDtoForMail.getIndentSettledEmployeeName());
				
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Settled employee number//",issueToOtherSiteDtoForMail.getCurrentUserMobileNumber()==null?"":issueToOtherSiteDtoForMail.getCurrentUserMobileNumber());
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Settled employee Mail//",issueToOtherSiteDtoForMail.getCurrentUserEemail()==null?"":issueToOtherSiteDtoForMail.getCurrentUserEemail());
			
			
		
			//T:- ://Settled employee number// E://Settled employee Mail// 
			/*emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Amount",String.valueOf(ApproveData[2])); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Request_Amount",String.valueOf(ApproveData[3]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("INTERNAL_COMMENTS",String.valueOf(ApproveData[4]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("VENDOR_COMMENTS",String.valueOf(ApproveData[5]));*/
			
			
			//payment time it will effectsrc_image
			//emailBodyMsgTxt = emailBodyMsgTxt.replace("src_image",src);
			
			String emailToAddress[]=senderSiteMail.toArray(new String[0]);
			
			String ccTo[]=ccMailsOfIndentEmployee.toArray(new String[0]);
			/****************************/
			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress,emailToAddress,ccTo,"");

		}catch(Exception e){
			e.printStackTrace();
		}	
	}
	
	
	/*****************************************cancel po mail send to Account level Approve Time**********************************************************/
	public void sendMailForAccountsDeptinApprovalTime(Object []ApproveData,String []ccTo,String VendorName,String siteName,int portNumber,boolean isCancel){
		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String uat_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT").toString();
			String cug_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG").toString();
			String live_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE").toString();
			String emailSubjectText ="";
			String msg="";
			String emailBodyMsgTxt = "";
			emailSubjectText = "Regarding Cancellation of Payment Request of "+VendorName+" for Project "+siteName;
			
			BufferedReader br = null;
			FileReader fr = null;
			if(isCancel){
			fr = new FileReader("C:\\testing\\MailTemplates\\perminentPoCancelAccountsApproval.html");
			msg="cancel";
			//fr = new FileReader("D:\\ravi\\perminentPoCancelAccountsApproval.html");
			}else{
				if(Boolean.valueOf(String.valueOf(ApproveData[6]))){
					msg="Up_date";
					//emailSubjectText = "Regarding Updation of Payment Request of "+VendorName+" for Project "+siteName;
					//emailBodyMsgTxt = emailBodyMsgTxt.replace("Up_date","Up_date");
				}else if(Boolean.valueOf(String.valueOf(ApproveData[7]))){
					msg="Revised";
					//emailSubjectText = "Regarding Revision of Payment Request of "+VendorName+" for Project "+siteName;
					//emailBodyMsgTxt = emailBodyMsgTxt.replace("Up_date","Revised");
				}
				fr = new FileReader("C:\\testing\\MailTemplates\\updatePoAccountsApproval.html");
				//fr = new FileReader("D:\\ravi\\updatePoAccountsApproval.html");
			}
			br = new BufferedReader(fr);
			String src="";
			if(portNumber==8078){
				src=uat_image_path;
			}else if(portNumber==8079){
				src=cug_image_path;	
			}else if(portNumber==80){
				src=live_image_path;
			}
				
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Number",String.valueOf(ApproveData[0]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("VENDOR_NAME",VendorName); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Date",String.valueOf(ApproveData[1])); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("SITE_NAME",siteName); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Amount",String.valueOf(ApproveData[2])); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Request_Amount",String.valueOf(ApproveData[3]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("INTERNAL_COMMENTS",String.valueOf(ApproveData[4]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("VENDOR_COMMENTS",String.valueOf(ApproveData[5])); // payment time it will effectsrc_image
			if(isCancel){
			emailBodyMsgTxt = emailBodyMsgTxt.replace("cancel",msg);
			}else{
				emailBodyMsgTxt = emailBodyMsgTxt.replace("Up_date",msg);
			}
			emailBodyMsgTxt = emailBodyMsgTxt.replace("src_image",src);
			/****************************/
			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress,ccTo,new String[0],"");

		}catch(Exception e){
			e.printStackTrace();
		}	

	}
	
	/*============================================ update po start here ===========================================================================*/
	public void sendUpdatePOApproval(List<ProductDetails> SuccessDataListToMail, String[] emailToAddress, int getLocalPort, String mailPoNumber,
			String emailPODate,String strSiteName,String strApproveName,String strVendorName,Object[] ApproveData){

		try {
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();

			String emailSubjectText ="A Request to Update PO with reference number "+mailPoNumber+" of Project "+strSiteName;
			String emailBodyMsgTxt = "";
			int i=1;
			String ApproveUrl="";
			String RejectUrl="";
			String url="";
			if(getLocalPort==8080){ //LOCAL

				url="http://localhost:8080/Sumadhura/getDetailsforPoApproval.spring?";
				ApproveUrl="http://localhost:8080/Sumadhura/UpdatedTempPoApproval.spring?";
				RejectUrl="http://localhost:8080/Sumadhura/UpdatedTempPoRejectCancel.jsp?";
				//cancelUrl="http://localhost:8087/Sumadhura/TempPoRejectCancel.jsp?";
				//path="D:\\Sumadhura\\template\\TempPoMail.html";
			}else if(getLocalPort == 8078){ //UAT

				url="http://129.154.74.18:8078/Sumadhura_UAT/getDetailsforPoApproval.spring?";
				ApproveUrl="http://129.154.74.18:8078/Sumadhura_UAT/UpdatedTempPoApproval.spring?";
				RejectUrl="http://129.154.74.18:8078/Sumadhura_UAT/UpdatedTempPoRejectCancel.jsp?";
				//cancelUrl="http://129.154.74.18:8078/Sumadhura_UAT/TempPoRejectCancel.jsp?";
				//path="C:\\testing\\MailTemplates\\TempPoMail.html";
			}else if(getLocalPort == 8079){ //CUG

				url="http://106.51.38.64:8079/Sumadhura_CUG/getDetailsforPoApproval.spring?";
				ApproveUrl="http://106.51.38.64:8079/Sumadhura_CUG/UpdatedTempPoApproval.spring?";
				RejectUrl="http://106.51.38.64:8079/Sumadhura_CUG/UpdatedTempPoRejectCancel.jsp?";
				//cancelUrl="http://106.51.38.64:8079/Sumadhura_CUG/TempPoRejectCancel.jsp?";
				//path="C:\\testing\\MailTemplates\\TempPoMail.html";
			}else if(getLocalPort == 80){ //LIVE

				url="http://129.154.74.18/Sumadhura/getDetailsforPoApproval.spring?";
				ApproveUrl="http://129.154.74.18/Sumadhura/UpdatedTempPoApproval.spring?";
				RejectUrl="http://129.154.74.18/Sumadhura/UpdatedTempPoRejectCancel.jsp?";
				//cancelUrl="http://129.154.74.18/Sumadhura/TempPoRejectCancel.jsp?";
				//path="C:\\testing\\MailTemplates\\TempPoMail.html";
			}
			
			
			/****************************************************/		
			String tableData = "<div> <table class=\"table \" style=\"border:1px solid #000;border-spacing: 0;border-collapse: collapse;width:100%;\">"
				+"<thead>  <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">S.No</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">PO Number</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Item Description</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">UOM</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">PO Quantity</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Updated Quantity</th>"
				+"</tr>  </thead> <tbody>";


			for(ProductDetails successData : SuccessDataListToMail){
				
				tableData+= " <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+i+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getPoNumber()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getChild_ProductName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getMeasurementName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getRecivedQty()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getRequestQantity()+"</td>"
					+"</tr>";
				i++;

			}
			tableData+="</tbody></table></div><br><br><br><br>";

			/****************************************************/
			BufferedReader br = null;
			FileReader fr = null;
			//fr = new FileReader("D:\\ravi\\updatePOApproval.html");
			fr = new FileReader("C:\\testing\\MailTemplates\\updatePOApproval.html");
			br = new BufferedReader(fr);

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}
			
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Approval_Name", strApproveName);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Number", mailPoNumber);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Date", emailPODate);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_Site", strSiteName);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Vendor_Name",strVendorName);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_NUMBER",String.valueOf(ApproveData[5]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("TABLE", tableData);
			
			emailBodyMsgTxt = emailBodyMsgTxt.replace("url",url+"&siteId="+String.valueOf(ApproveData[2]+"&poNumber="+String.valueOf(ApproveData[5])+"&siteLevelPoPreparedBy="+String.valueOf(ApproveData[11])+"&siteName="+String.valueOf(ApproveData[7])+
					"&password="+String.valueOf(ApproveData[12])+"&siteWiseIndentNo="+String.valueOf(ApproveData[6])+"&userId="+String.valueOf(ApproveData[1])+"&isUpdate=true"));
			
			emailBodyMsgTxt = emailBodyMsgTxt.replace("approveUrl",ApproveUrl+"&strIndentNo="+String.valueOf(ApproveData[0])+"&userId="+String.valueOf(ApproveData[1])+"&siteId="+String.valueOf(ApproveData[2]+"&oldPOEntryId="+String.valueOf(ApproveData[14])+
					"&oldPoNumber="+String.valueOf(ApproveData[3])+"&strPONumber="+String.valueOf(ApproveData[5])+"&siteName="+String.valueOf(ApproveData[7])+"&siteLevelPoPreparedBy="+String.valueOf(ApproveData[11])+"&password="+String.valueOf(ApproveData[12])+"&port="+getLocalPort+"&oldPODate="+emailPODate+
					"&deliveryDate="+String.valueOf(ApproveData[15])+"&ccEmailId2="+String.valueOf(ApproveData[16])+"&strVendorName="+strVendorName)+"&vendorId="+String.valueOf(ApproveData[17])+"&mail=true");
			
			emailBodyMsgTxt = emailBodyMsgTxt.replace("rejectUrl",RejectUrl+"&siteId="+String.valueOf(ApproveData[2])+"&poNumber="+String.valueOf(ApproveData[5])+"&siteName="+String.valueOf(ApproveData[7])+
					"&password="+String.valueOf(ApproveData[12])+"&siteWiseIndentNo="+String.valueOf(ApproveData[6])+"&userId="+String.valueOf(ApproveData[1])+"&mail=true"+"&port="+getLocalPort+"&cancelOrReject=Reject");


			/****************************/
			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, new String[0],"");


		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*===================================================update po time mail send to final start=======================================*/
	public void sendMailForUpdatePo(List<ProductDetails> SuccessDataListToMail,Object []ApproveData,String [] emailto){ // here type is used to approve or reject why because same html use here
		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String emailSubjectText = "Updated Purchase Order with reference number "+String.valueOf(ApproveData[0]);
			String emailBodyMsgTxt = "";
			int i=1;
			String uat_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT").toString();
			String cug_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG").toString();
			String live_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE").toString();
			int portNumber=Integer.parseInt(String.valueOf(ApproveData[4]));
			String src="";
			if(portNumber==8078){
				src=uat_image_path;
			}else if(portNumber==8079){
				src=cug_image_path;	
			}else if(portNumber==80){
				src=live_image_path;
			}
			
			String tableData = "<div> <table class=\"table \" style=\"border:1px solid #000;border-spacing: 0;border-collapse: collapse;width:100%;\">"
				+"<thead>  <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">S.No</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Item Description</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">UOM</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">PO Quantity</th>"
				+"<th style=\"border:1px solid #000;background-color:#ddd;text-align:center;padding:7px;\">Updated Quantity</th>"
				+"</tr>  </thead> <tbody>";


			for(ProductDetails successData : SuccessDataListToMail){
				
				tableData+= " <tr style=\"display: table-row;vertical-align: inherit;border-color: inherit;\">"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+i+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getChild_ProductName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getMeasurementName()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getRecivedQty()+"</td>"
					+"<td style=\"border:1px solid #000;text-align:center;padding:7px;\">"+successData.getRequestQantity()+"</td>"
					+"</tr>";
				i++;

			}
			tableData+="</tbody></table></div><br><br><br><br>";

			
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader("C:\\testing\\MailTemplates\\updatePO.html");
			//fr = new FileReader("D:\\ravi\\updatePO.html");
			br = new BufferedReader(fr);

			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}

			emailBodyMsgTxt = emailBodyMsgTxt.replace("Old_PO_Number",String.valueOf(ApproveData[0]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("PO_date",String.valueOf(ApproveData[1]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Vendor_Name",String.valueOf(ApproveData[3]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("New_PO_Number",String.valueOf(ApproveData[2]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("TABLE", tableData);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("COMMENT",String.valueOf(ApproveData[5]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("src_image",src);
			

			/****************************/
			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress,emailto,new String[0],"");

		}catch(Exception e){
			e.printStackTrace();
		}	

	}

	/*================================================== update invoice time amount in account level start======================================*/
	/*****************************************cancel po mail send to Account level Approve Time**********************************************************/
	public void sendMailForAccountsDeptinApprovalTimeAtInvoiceUpdate(Object []ApproveData,String []ccTo,String VendorName,String siteName,int portNumber){
		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String uat_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT").toString();
			String cug_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG").toString();
			String live_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE").toString();
			String emailSubjectText ="";
			
			String emailBodyMsgTxt = "";
			emailSubjectText = "Regarding Cancellation of Payment Request of "+VendorName+ " for Project "+siteName;
			
			BufferedReader br = null;
			FileReader fr = null;
			fr = new FileReader("C:\\testing\\MailTemplates\\UpdateInvoiceInAccountsLevel.html");
			//fr = new FileReader("D:\\ravi\\UpdateInvoiceInAccountsLevel.html");
			
			br = new BufferedReader(fr);
			String src="";
			if(portNumber==8078){
				src=uat_image_path;
			}else if(portNumber==8079){
				src=cug_image_path;	
			}else if(portNumber==80){
				src=live_image_path;
			}
				
			String sCurrentLine;
			
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt+="\n"+sCurrentLine;
			}
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Invoice_Number",String.valueOf(ApproveData[0]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("VENDOR_NAME",VendorName); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("invoice_Date",String.valueOf(ApproveData[1])); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("SITE_NAME",siteName); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Invoice_Amount",String.valueOf(ApproveData[2])); 
			emailBodyMsgTxt = emailBodyMsgTxt.replace("Request_Amount",String.valueOf(ApproveData[3]));
			emailBodyMsgTxt = emailBodyMsgTxt.replace("src_image",src);
			/****************************/
			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress,ccTo,new String[0],"");

		}catch(Exception e){
			e.printStackTrace();
		}	

	}
	
	/*===================================================update po approval mail end here=========================================================*/

	/*======================================================================MARKETING TEMP PO MAIL END===========================================================================*/
	/******************************************************************indent reject mails End
	 * @param rowNumMail 
	 * @param ex_list *************************************************************/
	
	public void mailEx(Exception ex, String[] emailToAddress, int rowNumMail, List<Exception> ex_list){

		try {
			String emailSubjectText = "BOQ Exception";
			//emailToAddress = new String[]{"rafi341@yahoo.com","mrafi@amaravadhis.com","mohammadrafishaik4@gmail.com"};//
			StringWriter sw = new StringWriter();
            ex.printStackTrace(new PrintWriter(sw));
            String emailBodyMsgTxt = "Excel line no: "+rowNumMail+"\n"+sw.toString();
            for(Exception e : ex_list){
            	StringWriter sw1 = new StringWriter();
                e.printStackTrace(new PrintWriter(sw1));
                emailBodyMsgTxt += "EXTRA :: \n"+sw1.toString();
                
            }
            
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			
			
			/****************************************************/
			DynamicEmailService objDES = new DynamicEmailService();
			objDES.sendEmployeeEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, new String[0],"");
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@SuppressWarnings("unchecked")
	public void sendMailWorkOrderCreation(WorkOrderBean workOrderBean, HttpServletRequest request, StringBuffer subjectNameForMail, StringBuffer emailBodyContent
			, List<String> to_EmailAddress,List<String>  ccMailsAddressOfEmployee, String operationType) {
		try{
			int portNumber=8080;
			portNumber=request.getLocalPort();
			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			List<String> billingSiteAddress =  (List<String>) request.getAttribute("SiteAddress");
			String siteAddress=billingSiteAddress.get(0);
			String src="";
			String approveUrl = "";
			String rejectUrl = "";
			String modifyUrl = "";
			String hyperLinkOfWorkOrder = "";
			String editAndApproveUrl = "";

			String localUrlPath= validateParams.getProperty("URL_PATH_LOCAL") == null ? "" : validateParams.getProperty("URL_PATH_LOCAL").toString();
			String uatUrlPath= validateParams.getProperty("URL_PATH_UAT") == null ? "" : validateParams.getProperty("URL_PATH_UAT").toString();
			String cugUrlPath= validateParams.getProperty("URL_PATH_CUG") == null ? "" : validateParams.getProperty("URL_PATH_CUG").toString();
			String liveUrlPath= validateParams.getProperty("URL_PATH_LIVE") == null ? "" : validateParams.getProperty("URL_PATH_LIVE").toString();

			if (!operationType.equals("rejectOrModify")) {	
				AESDecrypt encrypt = new AESDecrypt();
				// Encrypting data so the user can not see the actual data
				// while requesting data from URL we have to again decrypt it
				String approveEmpId = workOrderBean.getApproverEmpId();
				String approveEmpIdEncryptionData = encrypt.encrypt("AMARAVADHIS12345", approveEmpId);
				approveEmpIdEncryptionData = approveEmpIdEncryptionData.replaceAll("[\\t\\n\\r]+", "");

				String siteWiseWorkOrderNo = workOrderBean.getSiteWiseWONO();
				String siteWiseWorkOrderNoEncryptionData = encrypt.encrypt("AMARAVADHIS12345", siteWiseWorkOrderNo);
				siteWiseWorkOrderNoEncryptionData = siteWiseWorkOrderNoEncryptionData.replaceAll("[\\t\\n\\r]+", "");

				String tempIssueId = workOrderBean.getQS_Temp_Issue_Id();
				String tempIssueIdEncryptionData = encrypt.encrypt("AMARAVADHIS12345", tempIssueId);
				tempIssueIdEncryptionData = tempIssueIdEncryptionData.replaceAll("[\\t\\n\\r]+", "");

				approveUrl = "approveWorkOrderFromMail.spring?siteWiseWONO="+siteWiseWorkOrderNoEncryptionData+"&siteId="+workOrderBean.getSiteId()+"&QS_Temp_Issue_Id="+tempIssueIdEncryptionData+"&approverEmpId="+approveEmpIdEncryptionData+"&typeOfWork="+workOrderBean.getTypeOfWork()+"&status=false&workOrderStatus=A&isSaveOrUpdateOperation=Approve&requestFromMail=true";
				rejectUrl  = "rejectOrModifyWorkOrderFromMail.spring?siteWiseWONO="+siteWiseWorkOrderNoEncryptionData+"&siteId="+workOrderBean.getSiteId()+"&QS_Temp_Issue_Id="+tempIssueIdEncryptionData+"&approverEmpId="+approveEmpIdEncryptionData+"&typeOfWork="+workOrderBean.getTypeOfWork()+"&status=false&workOrderStatus=A&isSaveOrUpdateOperation=Reject&requestFromMail=true";
				modifyUrl  = "rejectOrModifyWorkOrderFromMail.spring?siteWiseWONO="+siteWiseWorkOrderNoEncryptionData+"&siteId="+workOrderBean.getSiteId()+"&QS_Temp_Issue_Id="+tempIssueIdEncryptionData+"&approverEmpId="+approveEmpIdEncryptionData+"&typeOfWork="+workOrderBean.getTypeOfWork()+"&status=false&workOrderStatus=A&isSaveOrUpdateOperation=Modify&requestFromMail=true";
				hyperLinkOfWorkOrder="getMyCompletedWorkOrder.spring?WorkOrderNo="+workOrderBean.getWorkOrderNo()+"&workOrderIssueNo=&site_id="+workOrderBean.getSiteId()+"&operType=1&isUpdateWOPage=false&requestFromMail=true";
			}
			

			/*	approveUrl=	URLEncoder.encode(approveUrl, "UTF-8" );  
			rejectUrl =	URLEncoder.encode(rejectUrl, "UTF-8" );
			modifyUrl= URLEncoder.encode(modifyUrl, "UTF-8" );*/

			//if (!operationType.equals("rejectOrModify")) 	
			if(portNumber==8080){
				src = validateParams.getProperty("LOGO_IMAGE_PATH_LOCAL") == null ? "" : validateParams.getProperty("LOGO_IMAGE_PATH_LOCAL").toString();
				approveUrl = localUrlPath + approveUrl;
				rejectUrl = localUrlPath + rejectUrl;
				modifyUrl = localUrlPath + modifyUrl;
				hyperLinkOfWorkOrder = localUrlPath + hyperLinkOfWorkOrder;
				editAndApproveUrl = localUrlPath + "index.jsp";
				//url = localUrlPath;
			}else if(portNumber == 8078){ //UAT machine
				src = validateParams.getProperty("LOGO_IMAGE_PATH_UAT") == null ? "" : validateParams.getProperty("LOGO_IMAGE_PATH_UAT").toString();
				approveUrl = uatUrlPath + approveUrl;
				rejectUrl = uatUrlPath + rejectUrl;
				modifyUrl = uatUrlPath + modifyUrl;
				hyperLinkOfWorkOrder = uatUrlPath + hyperLinkOfWorkOrder;
				editAndApproveUrl = uatUrlPath + "index.jsp";
				//url = "http://106.51.38.64:8078/Sumadhura_UAT/";
			} else if(portNumber == 8079){ //CUG
				src=validateParams.getProperty("LOGO_IMAGE_PATH_CUG") == null ? "" : validateParams.getProperty("LOGO_IMAGE_PATH_CUG").toString();
				approveUrl = cugUrlPath + approveUrl;
				rejectUrl = cugUrlPath + rejectUrl;
				modifyUrl = cugUrlPath + modifyUrl;
				hyperLinkOfWorkOrder = cugUrlPath + hyperLinkOfWorkOrder;
				editAndApproveUrl = cugUrlPath + "index.jsp";
				//url = cugUrlPath;
			} else if(portNumber == 80){ //LIVE
				src = validateParams.getProperty("LOGO_IMAGE_PATH_LIVE") == null ? "" : validateParams.getProperty("LOGO_IMAGE_PATH_LIVE").toString();
				approveUrl = liveUrlPath + approveUrl;
				rejectUrl = liveUrlPath + rejectUrl;
				modifyUrl = liveUrlPath + modifyUrl;
				hyperLinkOfWorkOrder = liveUrlPath + hyperLinkOfWorkOrder;
				editAndApproveUrl = liveUrlPath + "index.jsp";
				//url = liveUrlPath;
			}

			String emailSubjectText = subjectNameForMail.toString();
			String emailBodyMsgTxt = "";

			BufferedReader br = null;
			FileReader fr = null;
			if (operationType.equals("rejectOrModify")) {
				if (workOrderBean.getIsSaveOrUpdateOperation().equals("Modify")) {
					fr = new FileReader("C:\\testing\\MailTemplates\\MODIFY_WORK_ORDER.html");
				} else {
					fr = new FileReader("C:\\testing\\MailTemplates\\REJECT_WORK_ORDER.html");
				}
			} else {
				if (workOrderBean.getApproverEmpId().equals("END") && !workOrderBean.getRevision().equals("0")) {
					fr = new FileReader("C:\\testing\\MailTemplates\\AFTER_WORK_ORDER_REVISION.html");
				} else if (workOrderBean.getApproverEmpId().equals("END")) {
					fr = new FileReader("C:\\testing\\MailTemplates\\AFTER_WORK_ORDER_CREATION.html");
				} else {
					fr = new FileReader("C:\\testing\\MailTemplates\\CreateWorkOrderMailTemplate.html");
				}
			}
			br = new BufferedReader(fr);
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				System.out.println(sCurrentLine);
				emailBodyMsgTxt += "\n" + sCurrentLine;
			}
			if (operationType.equals("rejectOrModify")) {
				emailBodyMsgTxt = emailBodyMsgTxt.replace("//Rejected_Employee_Name//",workOrderBean.getWorkorderFrom());
			} else {
				if (workOrderBean.getApproverEmpId().equals("END")) {
					emailBodyMsgTxt = emailBodyMsgTxt.replace("//Contractor_Name//", workOrderBean.getContractorName());
					emailBodyMsgTxt = emailBodyMsgTxt.replaceAll("//Site_Name//", workOrderBean.getSiteName());
					emailBodyMsgTxt = emailBodyMsgTxt.replaceAll("#WorkOrderLink", hyperLinkOfWorkOrder);
				} else {
					emailBodyMsgTxt = emailBodyMsgTxt.replace("//Approver_Employee_Name//", workOrderBean.getWorkorderTo());
					emailBodyMsgTxt = emailBodyMsgTxt.replaceAll("//Previous_Approved_Name//", workOrderBean.getWorkorderFrom());
	
					emailBodyMsgTxt = emailBodyMsgTxt.replace("#Approve", approveUrl);
					emailBodyMsgTxt = emailBodyMsgTxt.replace("#Reject", rejectUrl);
					emailBodyMsgTxt = emailBodyMsgTxt.replace("#Modify", modifyUrl);
					emailBodyMsgTxt = emailBodyMsgTxt.replace("#EditAndApprove", editAndApproveUrl);
				}
			}
			emailBodyMsgTxt = emailBodyMsgTxt.replace("WORKORDER_TABLE_DATA", emailBodyContent);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("SumadhuraLogo2015.png", src);
			emailBodyMsgTxt = emailBodyMsgTxt.replace("BillingAddress", siteAddress);

			String emailToAddress[] = to_EmailAddress.toArray(new String[0]);
			String ccTo[] = ccMailsAddressOfEmployee.toArray(new String[0]);
			sendEmail(emailBodyMsgTxt, emailSubjectText, emailFromAddress, emailToAddress, ccTo, "");
			try {
				if (br != null)
					br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e){
			e.printStackTrace();
		}	
	}

	/*===============================================================TEMP MARKETING PO REJECT START ========================================================================*/

	public void sendMailForMarketingTempPoReject(String strApproveName,String []approveToEmailAddress,Object [] ApproveData,String subject,int strPortNo,String [] ccmails){
		try{

			String emailFromAddress  = validateParams.getProperty("EMAILFROMADDRESS") == null ? "" : validateParams.getProperty("EMAILFROMADDRESS").toString();
			String path="";
			String url = ""; 

			if(strPortNo == 8078){ 
				path="C:\\testing\\MailTemplates\\reject.html";
				 url="http://106.51.38.64:8078/Sumadhura_UAT/getDetailsforMarketingPoApproval.spring?";
			}
			else if(strPortNo == 8079){ 
				path="C:\\testing\\MailTemplates\\reject.html";
				 url="http://129.154.74.18:8079/Sumadhura_CUG/getDetailsforMarketingPoApproval.spring?";

			}
			else if(strPortNo == 8080){ 
				path="D:\\ravi\\reject.html";
				 url="http://localhost:8080/Sumadhura_CUG/getDetailsforMarketingPoApproval.spring?";

			}
			
			sendSSLMessageForTempPO(approveToEmailAddress, subject,
					ApproveData, emailFromAddress,ccmails,url,"","","",path);

		}catch(Exception e){
			e.printStackTrace();
		}	

	}


	/*===============================================================TEMP marketing PO REJECT END ========================================================================*/
	
	/*======================================================================MARKETING TEMP PO MAIL END===========================================================================*/
	/******************************************************************indent reject mails End*************************************************************/
	public static void main(String [] args) throws Exception {

		// Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
		//TestEmailFunction sendMailThroughJava = new TestEmailFunction();

		String [] emailToAddress = {};
		String emailSubjectText = "";
		String emailBodyMsgText = "";
		String emailFromAddress = "";
		String [] ccTo = {};

		/*sendSSLMessage(emailToAddress, emailSubjectText,
				emailBodyMsgText, emailFromAddress,ccTo,"","");*/


		System.out.println("Sucessfully sent mail to all Users");
	}
}
