<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<!-- <title>Insert title here</title> -->
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">



<style type="text/css">
  /* css for iframe modal popup */
  .btn-downloaddelete{float: right;position: relative;bottom: 24px;right: 18px;}
.btn-small{padding: 5px;border-radius: 5px;}
.btn-small1{padding: 3px;border-radius: 5px;}
.pdf-cls {
    position: relative;
    width: 100%;
	margin:auto;
}

.iframe-pdf {
  opacity: 1;
  display: block;
  width: 100%;
  height: auto;
  transition: .5s ease;
  backface-visibility: hidden;
}

.middle {
  transition: .5s ease;
  opacity: 0;
  position: absolute;
  top: 50%;
  left: 50%;
  width:100%;
  transform: translate(-50%, -50%);
  -ms-transform: translate(-50%, -50%);
  text-align: center;
}

.pdf-cls:hover .iframe-pdf {
  opacity: 0.3;
}

.pdf-cls:hover .middle {
  opacity: 1;
}
.modal-lg-width{
width:95%;
}
/* text {
 background-color: #4CAF50;
 color: white; 
  font-size: 16px;
 padding: 16px 32px;
} */
.btn-fullwidth:hover{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:-45px;
}
.btn-fullwidth{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
margin-top:-45px;
}
.btn-fullwidth:active:focus, .btn-fullwidth:active:hover{
color: transparent;
    background-color: transparent;
    border-color: transparent;
}
 .btn-fullwidth:active{
 color: transparent;
    background-color: transparent;
    border-color: transparent;
 }
 .btn-fullwidth.focus, .btn-fullwidth:focus {
    color: transparent;
    background-color: transparent;
    border-color: transparent;
}
/*css for iframe modal popup*/
</style>
<jsp:include page="./../CacheClear.jsp" />
</head>
<body>



	<!-- ***********************************************this is for pdf file download start******************************************************** -->

<div class="col-md-12" style="margin-top: 10px;" id="PDFView">
<!-- 	<h3>You can see the PDF</h3> -->
<%int pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
if(pdfcount==0){
	%>
	<!-- <h3>No PDF</h3> -->
	<%
}else{
	%>
<h3 style="float:left;color: #ffa500;">You can see the PDF(s) below :</h3>	
<div class="clearfix"></div>
	<%
}
for(int i=0;i<pdfcount;i++){
  	String pdfName="pdf"+i;
  	log(pdfName);

%>
   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
 <%
		if(request.getAttribute(pdfName)!=null){
%>
			 <div class="col-md-3 pdfcount pdf-delete<%=i%>">
			  <div class="pdf-cls" style="margin-bottom:15px;"> 
			  <!-- <img src="img_avatar.png" alt="Avatar" class="image" style="width:100%"> -->
			  <iframe class="iframe-pdf" src="${requestScope[pdfBase64]}" allow="fullscreen" style="height:200px;width:100%;border:1px solid #000;"></iframe>
			  <div class="middle">
			   <p>
			  
				<button type="button" class="btn btn-danger btn-fullwidth" data-toggle="modal" data-target="#myModalpdf<%=i%>"><i class="fa fa-close"></i></button>
			   </p>
			</div>
			<p class="btn-downloaddelete">
			 <a class="btn btn-success btn-xs" download onclick="toDataURL('${requestScope[pdfBase64]}',this)"><i class="fa fa-download"></i>&nbsp;Download</a>
			</p>
			</div>
			
			 </div>
<%} %>
<%} %>
</div>
 
 		<!-- ***********************************************this is for pdf download end************************************************** -->		
		<div class="col-md-12 Mrgtop20" id="IMAGEView">
	
			<!-- 	<h3>You can see the Images</h3> -->
						<%
										int imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
										int pdfCount= Integer.parseInt(String.valueOf(request.getAttribute("pdfcount")));
						 				if(imagecount==0){
						 					%>
						 					<!-- <h3>No Images</h3> -->
						 					<%
						 				}else{
						 				%>
						 						<h3 style="float:left;color: #ffa500;">You can see the Images below :</h3>
						 						<div class="clearfix"></div>
						 				<%
						 				}				
 									for (int i = 0; i < imagecount; i++) {
												String imageB64="image"+i;	
												String deleteB64="delete"+i;
												log(imageB64);
												out.print("<div class='col-md-4 Mrgtop20'>");
									%>
									       <c:set value="<%=imageB64 %>" var="index"></c:set>
									        <c:set value="<%=deleteB64 %>" var="delete"></c:set>
																		<%
									//	if (i == 0) {
										if(request.getAttribute(imageB64)!=null){
									%>
									 
									 <div class="container-1">
													<img class="img-responsive img-table-getinvoice"  alt="img" src="${requestScope[index]}"  data-toggle="modal" data-target="#uploadinvoice-img<%=i%>" />
													 <div class="middle-1">
													<div class="columns download">
										           <p>
										             <a class="btn btn-dwn btn-success btn-xs" download onclick="toDataURL('${requestScope[index]}',this)"><i class="fa fa-download"></i>&nbsp;Download</a>
										       <%--       <button onclick="deleteWOFile('<%=imageB64 %>','${requestScope[delete]}')" type="button" class="button btn-dwn btn-danger btn-xs"><i class="fa fa-remove"></i> &nbsp;Delete</button> --%>
										          </p>
										       </div>
										       </div>
										       </div>
									<%
										}
									//	}
									%>
								<%
									out.print("</div>");
								%>
								<%}%>
							
							<input type="hidden" name="imagesAlreadyPresent"	value="<%=imagecount%>" />
							<input type="hidden" name="pdfAlreadyPresent" value="<%=pdfCount%>">
</div>

  	<!-- model popup for pdf start  -->
	<%
	 pdfcount = Integer.parseInt(String.valueOf(request.getAttribute("pdfcount"))); 
	for(int i=0;i<pdfcount;i++){
  	String pdfName="pdf"+i;
  	String PathdeletePdf="PathdeletePdf"+i;
  	log(pdfName);

%>
   <c:set value="<%=pdfName %>" var="pdfBase64"></c:set>
   <c:set value="<%=PathdeletePdf %>" var="deletePdf"> </c:set>
 <%
		if(request.getAttribute(pdfName)!=null){
%>
			<div id="myModalpdf<%=i%>" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width PDF<%=i+1 %></strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="${requestScope[pdfBase64]}" style="height:500px;width:100%;">
      </div>
      <div class="modal-footer">
       <div class="col-md-12 center-block text-center">
	     <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
	     <%-- <button type="button" class="btn btn-danger" id="deletePdf" onclick="deletepdf(<%=i %>,'${requestScope[deletePdf]}')" data-dismiss="modal">Delete</button> --%>
	   
      </div>
    </div>

  </div>
</div>
</div>
<%} %>
<%} %>


<!-- pdf model popup end  -->
	
	
<!-- modal popup for image pop start-->
<!-- Modal -->
	 <!-- Modal -->
	<%	  imagecount = Integer.parseInt(String.valueOf(request.getAttribute("imagecount")));
			for (int i = 0; i < imagecount; i++) { 
		String index="image"+i;				
		log(index);
		%>
	  <div class="modal fade custmodal" id="uploadinvoice-img<%=i %>" role="dialog">
    <div class="modal-dialog modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
        </div>
        <div class="modal-body cust-modal-body">
    	<c:set value="<%=index %>" var="i"></c:set>
    	  <img style="height: auto;width: 100%" id="myImg" alt="img"  class="img-responsive invoiceupload-popup-img center-block"  src="${requestScope[i]}" />
        </div>
        <div class="modal-footer text-center center-block">
          <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
        </div>
      </div>
    </div>
  </div>
  <%} %>
<!-- modal popup for invoice image end --> 
 
 <script>
//this code for download server images
	function toDataURL(url, current) {
		debugger;
	    var httpRequest = new XMLHttpRequest();
	    httpRequest.onload = function() {
	       var fileReader = new FileReader();
	          fileReader.onloadend = function() {
	             console.log("File : "+fileReader.result);
					 $(current).removeAttr("onclick");
					 $(current).attr("href", fileReader.result);
					 $(current)[0].click();
					 $(current).removeAttr("href");
					 $(current).attr("onclick", "toDataURL('"+url+"', this)");
	          }
	          fileReader.readAsDataURL(httpRequest.response);
	    };
	    httpRequest.open('GET', url);
	    httpRequest.responseType = 'blob';
	    httpRequest.send();
	 }
	//this code for download server images	
 </script>
 
 
</body>
</html>