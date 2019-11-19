<%@page import="org.springframework.beans.factory.annotation.Autowired"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="org.springframework.context.support.FileSystemXmlApplicationContext"%>
<%@page import="com.sumadhura.service.PaymentCreationApprovalEmailFunction"%>
<%@page import="com.sumadhura.transdao.PurchaseDepartmentIndentProcessDaoImpl"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>temp Po reject Or cancel</title>
 <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<!-- <script type="text/javascript" src="js/JQuery.js"> </script>
<script type="text/javascript">
</script> -->
</head>
<body>

<%
String userId=request.getParameter("userId");
String password=request.getParameter("password");
String siteId=request.getParameter("siteId");
String strPONumber=request.getParameter("strPONumber");
String typeOfPo=request.getParameter("cancelOrReject");
String port=request.getParameter("port");
%>

<form action="" id="CreateCentralIndentFormId"  method="post">

  <div class="modal fade" id="mymodal-cancel-temppo" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">Comments!</h4>
        </div>
        <div class="modal-body">
      <%--   <% if(typeOfPo.equalsIgnoreCase("cancel")){ %>
         <input type="text" class="form-control" name="Remarks_cancel" id="Remarks_cancel" value="" style="width: 184px;"/>
  	<button type="button" class="btn btn-warning 	 form-cancel" style="margin-right: 18px;width: 121px;margin-top: 47px;"  onclick="Cancel()">Submit</button>
  	<%} else { %> --%>
  	
  <!-- 	<input type="text" class="form-control" name="Remarks_reject" id="Remarks_reject" value="" style="width: 184px;"/>
  	<button type="button" class="btn btn-warning 	 form-cancel" style="margin-right: 18px;width: 121px;margin-top: 47px;"  onclick="Reject()">Submit</button>
  	 -->
  	<%-- <%} %> --%>
          <!-- <p>Some text in the modal.</p> -->
        </div>
       <!--  <div class="modal-footer">
          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        </div> -->
      </div>
      
    </div>
  </div>

<input type="hidden" name="userId" id="userId" value="<%=userId%>">
<input type="hidden" name="password" id="password" value="<%=password%>">
<input type="hidden" name="siteId" id="siteId" value="<%=siteId%>">
<input type="hidden" name="strPONumber" id="strPONumber" value="<%=strPONumber%>">
<%!
@Autowired(required = true)
PurchaseDepartmentIndentProcessDaoImpl 	obj;
%>
 <%//PurchaseDepartmentIndentProcessDaoImpl obj=ApplicationContext context = null;
 int getLocalPort =Integer.parseInt(port);
 ApplicationContext context = null;
 //ge=8027

 if(getLocalPort == 8027){ //local machine

 context = new FileSystemXmlApplicationContext("C:/Users/admin/Desktop/New folder/SUMADHURA/WebContent/WEB-INF/applicationContext.xml");
 }
 else if(getLocalPort == 8078){ //UAT
 	

 context = new FileSystemXmlApplicationContext("C:/testing/Sumadhura_UAT/WebContent/WEB-INF/applicationContext.xml");
 }
 else if(getLocalPort == 8079){ //CUG
	 	

	 context = new FileSystemXmlApplicationContext("E:/Sumadhura_CUG/Sumadhura_CUG/WebContent/WEB-INF/applicationContext.xml");
	 }

 else if(getLocalPort == 80){ //LIVE

 context = new FileSystemXmlApplicationContext("C:/Rakesh/Sumadhura/WebContent/WEB-INF/applicationContext.xml");
 }
 PurchaseDepartmentIndentProcessDaoImpl obj = (PurchaseDepartmentIndentProcessDaoImpl) context.getBean("purchaseDeptIndentrocessDao1");
	String cancel=obj.getCancelOrNot(strPONumber);
	String checkval=obj.checkApproveStatus(strPONumber);
	String dbPasswd=obj.getTempPOPassword(strPONumber,false);
	log(cancel);
	log(checkval+" checkval" );
	
if(!cancel.equalsIgnoreCase("CANCEL") && checkval.equalsIgnoreCase("A") && dbPasswd.equalsIgnoreCase(password)){
	
%> 

 <div class="col-md-12 text-center center-block" style="margin-top:20px;">
 <div class="form-group">
 <lable class="col-md-12">Please Enter The Comment</lable>
 <div class="col-md-12" "><textarea class="form-control " name="Remarks" id="Remarks" placeholder="Please enter comment"  style="width: 350px; margin: 15px auto;"></textarea></div>
 </div>
 </div>
 <% if(typeOfPo.equalsIgnoreCase("cancel")){ %>
 <div class="col-md-12 text-center center-block">
  <button type="button" class="btn btn-warning" data-toggle="modal" onclick="Cancel()">Submit</button>
  </div>
 <%} else { %>
  <div class="col-md-12 text-center center-block">
  <button type="button" class="btn btn-warning" data-toggle="modal" onclick="Reject()">Submit</button>
  </div>
 
 <%} 
 } else { 
	 log("else");%>

 <center><font color="red" size="5"><c:out value="Already Approved or Rejected or Cancelled"></c:out> </font></center>
 <%} %>
 
<div class="w3-container w3-center w3-animate-top" style="display:none;height: 180px;width: 236px;border: 1px solid; margin-left: 658px;position: absolute;margin-top: -292px;">
  <h3>Comments!</h3>
 
  <button type="button" class="btn btn-warning 	 form-cancel" style="margin-right: 18px;width: 121px;margin-top: 47px;"  onclick="Cancel()">Submit</button>
</div>


<!-- ========================================================reject temp Po========================================================================= -->

<div class="w3-container w3-center w3-animate-top" style="display:none;height: 180px;width: 236px;border: 1px solid; margin-left: 658px;position: absolute;margin-top: -292px;">
  <h3>Comments!</h3>
  <input type="text" class="form-control" name="Remarks_reject" id="Remarks_reject" style="width: 184px;"/>
  <button type="button" class="btn btn-warning 	 form-cancel" style="margin-right: 18px;width: 121px;margin-top: 47px;" data-dismiss="modal" onclick="Reject()">Submit</button>
</div>


<!-- ==============================================================model popup =============================================================== -->








</form>
<!-- custom modal popup start-->

 <!-- Trigger the modal with a button -->
  

  <!-- Modal -->
 
  
</div>

 <!-- custom modal popup end -->
<script>
function Cancel() {

	document.getElementById("CreateCentralIndentFormId").action = "TempPoMailcancelPO.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}

function Reject() {

	document.getElementById("CreateCentralIndentFormId").action = "RejectMailTempPO.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}

</script>
 
 
</body>
</html>