<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.context.support.FileSystemXmlApplicationContext"%>
<%@page import="com.sumadhura.service.IndentCreationApprovalEmailFunction"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script type="text/javascript" src="js/JQuery.js"> </script>
<script type="text/javascript">
</script>
</head>
<body>
<form action="" id="CreateCentralIndentFormId"  method="post">

<%

  String strIndentNumber = request.getParameter("indentNumber");
  int  site_id = Integer.parseInt(request.getParameter("siteId"));
  String strUserId = request.getParameter("userId");
  String siteWiseIndentNo=request.getParameter("siteWiseIndentNo");
  
  String strOperationType = request.getParameter("operationtype");
  String tempPass=request.getParameter("tempPass");
  String userName=request.getParameter("userName");
  String indentName=request.getParameter("indentName");
  
  String strResponse = "";
  System.out.println("strIndentNumber  "+strIndentNumber);
  
  System.out.println("site_id  "+site_id);
  
  System.out.println("strUserId  "+strUserId);
  
  int indentNumber=Integer.parseInt(strIndentNumber);
 // String strIndentNumber = request.getParameter("");
int getLocalPort =Integer.parseInt(request.getParameter("portNo"));
ApplicationContext context = null;

if (getLocalPort == 8078) { //local machine

	context = new FileSystemXmlApplicationContext(
			"C:/testing/Sumadhura_UAT/WebContent/WEB-INF/applicationContext.xml");
} else if (getLocalPort == 8079) { //CUG

	context = new FileSystemXmlApplicationContext(
			"E:/Sumadhura_CUG/Sumadhura_CUG/WebContent/WEB-INF/applicationContext.xml");
} else if (getLocalPort == 80) { //LIVE

	context = new FileSystemXmlApplicationContext(
			"C:/Rakesh/Sumadhura/WebContent/WEB-INF/applicationContext.xml");
}
 
  
// ApplicationContext context = new FileSystemXmlApplicationContext
						//("E:/pavan/Sumadhura UAT/Sumadhura/WebContent/WEB-INF/applicationContext.xml");
					//	("F:/Sumadhara/Sumadhura/WebContent/WEB-INF/applicationContext.xml");

						IndentCreationApprovalEmailFunction obj = (IndentCreationApprovalEmailFunction) context.getBean("objIndentCreationApprovalEmailFunction");
						System.out.println("strOperationType"+strOperationType);
						if(strOperationType.equals("Approve'")){strOperationType="Approve";}
						if(strOperationType.equals("Approve")){
						
						    strResponse = obj.approveIndentCreationFromMail(request, site_id, strUserId,userName);
						    
						    if(strResponse.equals("WrongPassword"))
							{
						    	%>
								   <center><font color="green" size="5">Already Submitted</font></center>
								  <% 
						    	//out.println("Already Approved");
							}
							else if(strResponse.equals("Success"))
							{
								%>
								   <center><font color="green" size="5">Indent approved successfully </font></center>
								  <%
								//out.println("Indent Approved Successfully");
							}
							else{
								%>
								   <center><font color="green" size="5">Sorry indent not approved please try again</font></center>
								  <%
								//out.println("Failed. Indent NOT Approved");
							}

						}else if(strOperationType.equals("Reject")){
							if(tempPass.equals(obj.getTempPasswordOfIndent(indentNumber))){
							%>
							<input type="hidden" name="indentNumber" id="userId" value="<%=strIndentNumber%>">
							<input type="hidden" name="tempPass" id="tempPass" value="<%=tempPass%>">
							<input type="hidden" name="siteId" id="userId" value="<%=site_id%>">
							<input type="hidden" name="userId" id="userId" value="<%=strUserId%>">
							<input type="hidden" name="userId" id="userName" value="<%=userName%>">
							<input type="hidden" name="siteWiseIndentNo" id="siteWiseIndentNo" value="<%=siteWiseIndentNo%>">
							<input type="hidden" name="indentName" id="indentNameId" value="<%=indentName%>">
							
    						<div class="col-md-12 text-center center-block" style="margin-top:20px;">
 							<div class="form-group">
 							<lable class="col-md-12">Please Enter The Comment</lable>
 							<div class="col-md-12" "><textarea class="form-control " name="indentRemarks" id="Remarks" placeholder="Please enter comment"  style="width: 350px; margin: 15px auto;"></textarea></div>
 							</div>
 							</div>
 							<div class="col-md-12 text-center center-block">
  							<button type="button" class="btn btn-warning" data-toggle="modal" onclick="Reject()">Submit</button>
  							</div>
  							
  							<%
  							// strResponse = obj.rejectIndentCreationFromMail(request);
							}else{
  							
  							%>
							
							<% 
							
							 strResponse = obj.rejectIndentCreationFromMail(session,request);
							 
							 
							 if(strResponse.equals("WrongPassword"))
								{
								 
								 %>
								   <center><font color="green" size="5">Already Submitted</font></center>
								  <% 
								   
								   //out.println("Already Rejected");
								}
								else if(strResponse.equals("Success"))
								{
									%>
									 <center><font color="green" size="5">Indent rejected successfully</font></center>
									<% 	
									//out.println("Indent Rejected Successfully");
								}
								else{
									%>
									 <center><font color="red" size="5">Sorry indent not rejected please try again</font></center>
									<% 
									//out.println("Failed. Indent NOT Rejected");
								}
						}
						}
                        // out.println("strResponse  "+strResponse);
%>

<!-- modal popup for indent reject start-->
	 <div class="modal fade" id="mymodal-cancel-temppo" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <!-- <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Comments!</h4>
        </div>
        <div class="modal-body">
          </div> -->
      
      </div>
      
    </div>
  </div>
   </div>
<!-- modal popup for indent reject end -->
<script>
function Reject() {

	document.getElementById("CreateCentralIndentFormId").action = "IndentRejectFromMail.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}
</script>
</form>
 
</body>
</html>