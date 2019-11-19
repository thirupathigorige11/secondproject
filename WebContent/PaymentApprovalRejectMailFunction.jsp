<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.context.support.FileSystemXmlApplicationContext"%>
<%@page import="com.sumadhura.service.PaymentCreationApprovalEmailFunction"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/JQuery.js"> </script>
<script type="text/javascript">
</script>
</head>
<body>

<%

  String strPaymentDetailsId = request.getParameter("paymentDetailsId");
  String  site_id = request.getParameter("siteId");
  String  session_siteId = request.getParameter("sessionSiteId");
  String strUserId = request.getParameter("userId");
  
  String strOperationType = request.getParameter("operationtype");
  
  //String strResponse = "";
  System.out.println("strPaymentDetailsId  "+strPaymentDetailsId);
  
  System.out.println("site_id  "+site_id);
  
  System.out.println("session_siteId  "+session_siteId);
  
  System.out.println("strUserId  "+strUserId);
  
 // String strIndentNumber = request.getParameter("");
int getLocalPort =Integer.parseInt(request.getParameter("portNo"));
ApplicationContext context = null;

if (getLocalPort == 8078) { //UAT

	context = new FileSystemXmlApplicationContext(
			"C:/testing/Sumadhura_UAT/WebContent/WEB-INF/applicationContext.xml");
} else if (getLocalPort == 8079) { //CUG

	context = new FileSystemXmlApplicationContext(
			"C:/Sumadhura_CUG/Sumadhura_CUG/Sumadhura_CUG/WebContent/WEB-INF/applicationContext.xml");
}
else if(getLocalPort == 80){ //LIVE

context = new FileSystemXmlApplicationContext("C:/Rakesh/Sumadhura/WebContent/WEB-INF/applicationContext.xml");
}
 
  
// ApplicationContext context = new FileSystemXmlApplicationContext
						//("E:/pavan/Sumadhura UAT/Sumadhura/WebContent/WEB-INF/applicationContext.xml");
					//	("F:/Sumadhara/Sumadhura/WebContent/WEB-INF/applicationContext.xml");

						PaymentCreationApprovalEmailFunction obj = (PaymentCreationApprovalEmailFunction) context.getBean("objPaymentCreationApprovalEmailFunction");
						
						if(strOperationType.equals("Approve")){
						
						    List<String> responseList = obj.approvePaymentFromMail( request, response, site_id, strUserId,strPaymentDetailsId,strOperationType,session_siteId);
						    
						    for(String strResponse : responseList){
						    	if(strResponse.contains("AlreadyApproved"))
								{
						    		%>
									   <font color="red" size="5"><%=strResponse.substring(0,strResponse.length()-15)%></font><br>
									  <% 
							    }
							    else if(strResponse.contains("Failed")){
									%>
									   <font color="red" size="5"><%=strResponse.substring(0,strResponse.length()-6)%></font><br>
									  <%
								}
								else if(strResponse.contains("Success"))
								{
									%>
									   <font color="green" size="5"><%=strResponse.substring(0,strResponse.length()-7)%></font><br>
									  <%
								}
								else if(strResponse.contains("Exception")){
									%>
									   <font color="red" size="5"> Exception Occured. </font><br>
									  <%
								}
						    }
							

						}else if(strOperationType.equals("Reject")){
						
						    List<String> responseList = obj.approvePaymentFromMail( request, response, site_id, strUserId,strPaymentDetailsId,strOperationType,session_siteId);
						    
						    for(String strResponse : responseList){
							    if(strResponse.contains("AlreadyApproved"))
								{
							    	%>
									   <font color="red" size="5"><%=strResponse.substring(0,strResponse.length()-15)%></font><br>
									  <% 
							    }
							    else if(strResponse.contains("Failed")){
									%>
									   <font color="red" size="5"><%=strResponse.substring(0,strResponse.length()-6)%></font><br>
									  <%
								}
								else if(strResponse.contains("Success"))
								{
									%>
									   <font color="red" size="5"><%=strResponse.substring(0,strResponse.length()-7)%></font><br>
									  <%
								}
								else if(strResponse.contains("Exception")){
									%>
									   <font color="red" size="5"> Exception Occured. </font><br>
									  <%
								}
						    }

						}else if(strOperationType.equals("Edit")){
						
						    List<String> responseList = obj.approvePaymentFromMail( request, response, site_id, strUserId,strPaymentDetailsId,strOperationType,session_siteId);
						    
						    for(String strResponse : responseList){
							    if(strResponse.contains("AlreadyApproved"))
								{
							    	%>
									   <font color="red" size="5"><%=strResponse.substring(0,strResponse.length()-15)%></font><br>
									  <% 
							    }
							    else if(strResponse.contains("Failed")){
									%>
									   <font color="red" size="5"><%=strResponse.substring(0,strResponse.length()-6)%></font><br>
									  <%
								}
								else if(strResponse.contains("Success"))
								{
									%>
									   <font color="green" size="5"><%=strResponse.substring(0,strResponse.length()-7)%></font><br>
									  <%
								}
								else if(strResponse.contains("Exception")){
									%>
									   <font color="red" size="5"> Exception Occured. </font><br>
									  <%
								}
						    }

						}
%>



 </div>
</body>
</html>
