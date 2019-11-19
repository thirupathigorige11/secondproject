<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.sumadhura.bean.GetInvoiceDetailsBean"%>
<%@page import="java.util.*"%>
<%@ page isELIgnored="false"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="com.sumadhura.util.NumberToWord"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"> -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css">

<jsp:include page="CacheClear.jsp" />  
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<title>Sumadhura_IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
</head>

<style>
table.dataTable{border-collapse:collapse !important;}
label{font-weight:bold;font-size:13px; font-family: sans-serif;}
</style>
 
<body class="nav-md" style="padding-right: 0px;">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId {
			display : none;
		}
 
	</style>
</noscript>
<input type="hidden" id="refreshed" value="no">
<div class="container body" id="mainDivId">
			<div class="main_container" id="main_container">
				<div class="col-md-3 left_col" id="left_col">
					<div class="left_col scroll-view">
             
						<div class="clearfix"></div>

						<jsp:include page="SideMenu.jsp" />  
						
					</div>
					</div>
					<jsp:include page="TopMenu.jsp" />  
				<!-- page content -->
				<div class="right_col" role="main">
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Inwards</a></li>
							<li class="breadcrumb-item active">Get Invoice Details</li>
							<li class="breadcrumb-item active">Invoice Numbers</li>
						</ol>
					</div>
					<div class="loader-sumadhura" style="z-index:99;display:none;">
						<div class="lds-facebook">
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
							<div></div>
						</div>
						<div id="loadingMessage">Loading...</div>
					</div>
				<form name="myForm" action="doGetInvoiceDetails.spring"> </form>
					 	
			<div class="col-md-12 inwardInvoice">
				<div align="" class="formShow" >
				 <form:form modelAttribute="DCModelForm" id="" class="form-horizontal" >
				 <c:forEach var="getInvoiceDetails" items="${requestScope['listIssueToOtherSiteInwardLists']}"> 
						 <div class="border-inwards">
				 		 	<div class="col-md-12 bottom-class">
					        <div class="col-md-4">
						         <div class="form-group">
										<label class="col-md-6 col-xs-12" >Invoice Number :</label>
										<span class="col-md-6 col-xs-12 wordbreak">${getInvoiceDetails.invoiceNumber}</span>	
								</div>
					        </div>
							<div class="col-md-4">
								<div class="form-group">
									<label class="col-md-6 col-xs-12">Invoice Date :</label>
									<span class="col-md-6 col-xs-12 wordbreak">${getInvoiceDetails.invoiceDate}</span>
								  </div>
							</div>
							<div class="col-md-4">
								 <div class="form-group">
									  <label  class="col-md-6 col-xs-12" >Received Date :</label>
										<span class="col-md-6 col-xs-12 wordbreak">${getInvoiceDetails.receivedDate}</span>
								</div>
							 </div>
							
							<div class="col-md-4">
									  <div class="form-group">
										<label  class="col-md-6 col-xs-12">Employee Name:</label>
										<span class="col-md-6 col-xs-12 wordbreak">${getInvoiceDetails.employeeName}</span>
									 </div>
								</div>
							<div class="col-md-4">
								  <div class="form-group">
									<label  class="col-md-6 col-xs-12">Employee Id :</label>
									<span class="col-md-6 col-xs-12 wordbreak">${getInvoiceDetails.employeeId}</span>
								 </div>
							</div>
							
						  <div class="col-md-4">
								<div class="form-group">
									 <label class="col-md-6 col-xs-12"> Vendor Name :</label>
									 <span class="col-md-6 col-xs-12 wordbreak">${getInvoiceDetails.vendorName}</span>							
								</div>
						 </div>
						 
						 <div class="col-md-4">
								 <div class="form-group">
								   <label  class="col-md-6 col-xs-12">GSTIN :</label>
								   <span class="col-md-6 col-xs-12 wordbreak">${getInvoiceDetails.gsinNo}</span>
								</div>
							</div>
						 <div class="col-md-4">
							 <div class="form-group">
							  <label  class="col-md-6 col-xs-12">Vendor Address :</label>
									<span class="col-md-6 col-xs-12 wordbreak" >
										${getInvoiceDetails.vendorAdress}
									</span>
							 </div>
						 </div>
						<c:set value="${getInvoiceDetails.note}" var="note" scope="page"></c:set> 	
							</div>
					</div>
					
					</c:forEach>		
					<% double value =0.0; 
					String totalamt ="";%>
						<div class="clearfix"></div>
						<div class="">
						<div class="table-responsive  Mrgtop20"> <!-- protbldiv -->
						<table id="doInventoryTableId" class="table table-new">
							<thead>
								 <tr>
									<th>S NO</th>
									<th>HSN Code</th>
				    				<th>Child Product</th> 								
				    				<th>UOM</th>
				    				<th>Quantity</th> 
				    				<th>Price</th>
				    				<th>Basic Amount</th>
					    			<th>Tax</th>
					    			<th>Tax Amount</th>
				    				<th>Amount After Tax</th>
				    				<th>Other or Transport Charges</th>
				    				<th>Tax On Other or Transport Charges</th>
				    				<th>Other Or Transport Charges After Tax</th>
				    				<th>Total Amount	</th>
				    				<th>Expire Date</th>
				    				<th>Remarks</th>
				  				</tr>
							</thead>
							<tbody>
				  				<input type="hidden" id="noofrows" value="${requestScope['listOfGetProductDetails'].size()}" />
				  				<c:forEach var="GetProductDetails" items="${requestScope['listOfGetProductDetails']}" step="1" begin="0">
								
								<tr>
									<td> <div id="snoDivId1">${GetProductDetails.strSerialNo}</div> </td>			
									<td class="tablDisply"> ${GetProductDetails.hsnCode} </td>
									<td> ${GetProductDetails.childProduct} </td>
									<td> ${GetProductDetails.unitsOfMeasurement} </td>
									<td  class="s-50"> ${GetProductDetails.quantity} </td>	
									<td class="tablDisply"> ${GetProductDetails.price} </td>
									<td class="tablDisply"> ${GetProductDetails.basicAmount} </td>
									<td class="tablDisply"> ${GetProductDetails.tax} </td>						
									<td class="tablDisply"> ${GetProductDetails.taxAmount} </td>
									<td class="tablDisply"> ${GetProductDetails.amountAfterTax} </td>
									<td class="tablDisply"> ${GetProductDetails.otherOrTransportCharges} </td>
									<td class="tablDisply"> ${GetProductDetails.taxOnOtherOrTransportCharges} </td>
									<td class="tablDisply"> ${GetProductDetails.otherOrTransportChargesAfterTax} </td>
									<td class="tablDisply"> ${GetProductDetails.totalAmount} </td>
									<td class="tablDisply"> ${GetProductDetails.expireDate} </td>
									<td class="tablDisply"> ${GetProductDetails.remarks} </td>
								</tr>
									
										
								<input type="hidden" id="fa${GetProductDetails.strSerialNo}" value="${GetProductDetails.finalAmountDiv}"/>
								 <c:set var="total" value="${GetProductDetails.finalAmountDiv}"/>
				 				<%
				 					value = value +Double.valueOf((String)pageContext.getAttribute("total"));   //No exception.
				 				%> 
										
								</c:forEach>
								<% 
								com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
								 totalamt = format.format(value);
								totalamt=totalamt.replaceAll("Rs","");
									%>
								</tbody>
						  </table>
						</div>
						</div>
						<div class="col-md-6 Mrgtop20 no-padding-left-inwards-note">
						 <div class="form-group">
						
								<%
								//List<GetInvoiceDetailsBean> listIssueToOtherSiteList =(List<GetInvoiceDetailsBean>) request.getAttribute("listOfGetProductDetails"); 
								//for(Map<?, ?> GetInvoiceDetailsInwardBean :listIssueToOtherSiteList){ %>
								
								<label class="col-md-3">Note:</label>
								<div class="col-md-9 ">
									<textarea name="Note" id="NoteId" class="form-control resize-vertical" placeholder="<c:out value="${note}"/>"></textarea>
			 
								</div>
						</div>
						</div>
						<%-- <label class="control-label col-sm-1  col-xs-12" Style="font-weight: bold;color:Black;margin-top: 11px;">
							Remarks:<br><c:out value="${note}"/>
								
						</label> --%>
						<%//} %>
						<div class="col-md-6 Mrgtop20 no-padding-left-inwards-note">
						 <div class="form-group">
						<label class="col-md-4 Mrgtop10"> Final Amount :</label>
						<h4><strong><span id="finalAmntDiv" class="col-md-8"><%=totalamt%></span></strong></h4>
						<div class="clearfix"></div>
						<label class="col-md-4 Mrgtop10">Final Amount In Words:</label>
					    <h4><strong><span id="finalAmtInwords" class="col-md-8"></span></strong></h4>
						</div></div>
						<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
						<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
						<input type="hidden" name="ttlAmntForIncentEntry" value="" id="ttlAmntForIncentEntryId">
						<input type="hidden" name="VendorId" value="" id="vendorIdId">
				
						<div class="clearfix"></div>
						<!-- ***************************************************grn purpose write this start*********************************************************** -->
							<!-- this is common file for showing images  -->
								<%-- <%@include file="../ImgPdfCommonJsp.jsp" %> --%>
								
								
						<form id="getInvoiceDetailsId" method="post">
							<% List<String> list=(List<String>) request.getAttribute("getGrnValues");  %>
							<input type="hidden" name="invoiceNumber" value="<%=list.get(0) %>" id="countOfRows">
							<input type="hidden" name="vendorId" value="<%=list.get(1) %>" id="countOfRows">
							<input type="hidden" name="invoiceDate" value="<%=list.get(2) %>" id="countOfRows">
							<input type="hidden" name="indentEntryId" value="<%=list.get(3) %>" id="countOfRows">
							<input type="hidden" name="siteId" value="<%=list.get(4) %>" id="countOfRows">
							<input type="hidden" name="url" value="${url}" id="url">					
							<c:if test="${indentType ne 'INU' }">
						    	<button type="button" class="btn btn-warning Mrgbtm20" 	 onclick="myFunc()">View GRN</button>
						    </c:if>
						    
				      </form>
				    <!-- this is for mis po repoerts time use it --> 
				     <!-- ***************************************************grn purpose write this end*********************************************************** -->
			  
			  <jsp:include page="WorkOrder/ImgPdfCommonJsp.jsp" />  
			   </form:form>
			</div>
			<div class="clearfix"></div>
				<!-- user341 -->
		<%-- 		<div class="">
				
				<% int count = Integer.parseInt(request.getAttribute("count").toString());%>
				<% if(count!=0){%>
				<h3 style="color: orange;font-weight:bold;">You can download Invoice here</h3>
				<%} %>
	          <% if(0<count){%>
				         <div class="col-md-3 container-1" style="float:left;">
							<img class="img-responsive img-table-getinvoice" id="myImg" alt="img" data-toggle="modal" data-target="#myModals0"  src="${image0}" />
				             <div class="middle">
							   <div class="columns download">
						          <p>
						             <a  class="button btn-dwn btn-success btn-xs" download onclick="toDataURL('${image0}', this)"><i class="fa fa-download"></i> Download</a>
						          </p>
					          </div>
				          </div>
				       </div>
			       <%} %>
	       
	       
	         <% if(1<count){%>
			       <div class="col-md-3 container-1">
						<img class="img-responsive img-table-getinvoice"  alt="img" data-toggle="modal" data-target="#myModals1"  src="${image1}"/>
						<div class="middle">
					           <div class="columns download">
						          <p>
						             <a  class="button btn-dwn btn-success btn-xs" download onclick="toDataURL('${image1}', this)"><i class="fa fa-download"></i> Download</a>
						          </p>
					         </div>
					  </div>
					</div>	
			       <%} %>
	      
	      
	         <% if(2<count){%>
				       <div class="col-md-3 container-1">
							<img class="img-responsive img-table-getinvoice"   alt="img" data-toggle="modal" data-target="#myModals2"  src="${image2}"/>
							<div class="middle">
							 <div class="columns download">
						          <p>
						             <a  class="button btn-dwn btn-success btn-xs" download onclick="toDataURL('${image2}', this)"><i class="fa fa-download"></i> Download</a>
						          </p>
				              </div>
							</div>
						</div>
			       <%} %>
	       
	        <% if(3<count){%>
			        <div class="col-md-3 container-1">
						<img class="img-responsive img-table-getinvoice"  alt="img" data-toggle="modal" data-target="#myModals3"  src="${image3}"/>
						<div class="middle">
						 <div class="columns download">
			          <p>
			             <a class="button btn-dwn btn-success btn-xs" download onclick="toDataURL('${image3}', this)"><i class="fa fa-download"></i> Download</a>
			          </p>
						</div>
			       </div>
			       </div>
			       <%} %>
	      
	         <% if(4<count){%>
			         <div class="col-md-3 container-1">
						<img class="img-responsive img-table-getinvoice" alt="img" data-toggle="modal" data-target="#myModals4"  src="${image4}"/>
						<div class="middle">
							 <div class="columns download">
						          <p >
						             <a class="button btn-dwn btn-success btn-xs" download onclick="toDataURL('${image4}', this)"><i class="fa fa-download"></i> Download</a>
						          </p>
							</div>
			         </div>
			        </div>
			       <% } %>
	
	       
	          <% if(5<count){%>
			       <div class="col-md-3 container-1">
						<img class="img-responsive img-table-getinvoice" data-toggle="modal" data-target="#myModals5"  alt="img" src="${image5}"/>
						<div class="middle">
							 <div class="columns download">
					          <p>
					             <a  class="button btn-dwn btn-success btn-xs" download onclick="toDataURL('${image5}', this)"><i class="fa fa-download"></i> Download</a>
					          </p>
					       </div>
						</div>
						 </div>
			       <%} %>	 
		       </div> --%>
	        </div>
	      </div>
	    </form>	
	  </div>
	</div>
	
<!-- /page content -->   
<!-- modal popup for download for invoice start-->
 <!-- Modal -->
  <div class="modal fade" id="myModals0" role="dialog">
    <div class="modal-dialog custmodal modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal" onclick=""> &times;</button>
          <h4 class="modal-title text-center">Modal Header</h4>
        </div>
        <div class="modal-body cust-modal-body">
        <img class="img-responsive img-full-width" src="${image0}"/>
          <img class="img-responsive img-full-width src="" style="display:none"/>
           <img class="img-responsive img-full-width" src="" style="display:none"/>
            <img class="img-responsive img-full-width" src="" style="display:none"/>
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
<!-- modal popup for download for invoice end -->     
					<!-- modal popup for download for invoice start-->
 <!-- Modal -->
  <div class="modal fade" id="myModals1" role="dialog">
    <div class="modal-dialog custmodal modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal" onclick=""> &times;</button>
          <h4 class="modal-title text-center">Modal Header</h4>
        </div>
        <div class="modal-body cust-modal-body">
        <img class="img-responsive img-full-width" src="${image1}"/>
          <img class="img-responsive img-full-width" src="" style="display:none"/>
           <img class="img-responsive img-full-width" src="" style="display:none"/>
            <img class="img-responsive img-full-width" src="" style="display:none"/>
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
<!-- modal popup for download for invoice end -->     
					<!-- modal popup for download for invoice start-->
 <!-- Modal -->
  <div class="modal fade" id="myModals2" role="dialog">
    <div class="modal-dialog custmodal modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal" onclick=""> &times;</button>
          <h4 class="modal-title text-center">Modal Header</h4>
        </div>
        <div class="modal-body cust-modal-body">
        <img class="img-responsive img-full-width" src="${image2}"/>
          <img class="img-responsive img-full-width" src="" style="display:none"/>
           <img class="img-responsive img-full-width" src="" style="display:none"/>
            <img class="img-responsive img-full-width" src="" style="display:none"/>
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
<!-- modal popup for download for invoice end -->     
					<!-- modal popup for download for invoice start-->
 <!-- Modal -->
  <div class="modal fade" id="myModals3" role="dialog">
    <div class="modal-dialog custmodal modal-lg custom-modal-lg">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header cust-modal-header">
          <button type="button" class="close" data-dismiss="modal" onclick=""> &times;</button>
          <h4 class="modal-title text-center">Modal Header</h4>
        </div>
        <div class="modal-body cust-modal-body">
        <img class="img-responsive img-full-width" src="${image3}"/>
          <img class="img-responsive img-full-width" src="" style="display:none"/>
           <img class="img-responsive img-full-width" src="" style="display:none"/>
            <img class="img-responsive img-full-width" src="" style="display:none"/>
        </div>
        <div class="modal-footer cust-modal-footer">
          <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>
  
<!-- modal popup for download for invoice end -->     

	
	<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="rowId">
	<script src="js/custom.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
		
		
<script>
		$(document).ready(
			function() {
				$(".up_down").click(
				   function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});
				$('#doInventoryTableId').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
			});

		//this code for download server images
		function toDataURL(url, current) {
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
		
/* Method for button show and show function
						 */

				 	 $(document).ready(function(){
							$("#withoutPricing").click(function(){
								$("#withPricingData").hide();
								$("#WithoutPricingData").show();
								
								
							});
							$("#withPricing").click(function(){
								$("#withPricingData").show();
								$("#WithoutPricingData").hide();
								$("#cantainer").hide();
							});
							var amountInWords=($("#finalAmntDiv").text());
							var nums = amountInWords.toString().split('.');
							console.log(nums.length);
						    var whole = convertNumberToWords(nums[0]);
						    if (nums.length == 2 && nums[1]!=0) {
						        var fraction = convertNumberToWords(nums[1]);
						        
						       // return whole + 'and ' + fraction;
						        $("#finalAmtInwords").text(whole + 'Rupees and ' + fraction +'paisa');
						        
						    } else {
						        //return whole;
						    	 $("#finalAmtInwords").text(whole+'Rupees');
						    }
							/* var amountInWords=	convertNumberToWords($("#finalAmntDiv").text());
							$("#finalAmtInwords").text(amountInWords); */
						 });	 
						 
						
/* Method for toggle the search button */	
	$(document).on("click", ".viewDCForm",function(){
	  	/*  $(".viewDCForm").parent().parent().find(".formShow").toggle();   */
 	 $(this).parent().parent().find(".formShow").slideToggle('slow' );  
		
 });
 
 
 
/*  **************** Loop Code*************** */
 $(document).on("click", ".add_new",function(){
 var regex = /^(.+?)(\d+)$/i;
 var cloneIndex = $("#parent_witghoutpricing").length;
    var rowId =  parseInt($(this).closest('.withoutPricing-class').attr('uda-rowId'));
    var newRowId = rowId+1;
    
    var clone = $(".withoutPricing-class:last").clone();
    clone.attr("uda-rowId", rowId+1);
	clone.find("#DCNumber"+rowId).removeAttr("id").attr("id","DCNumber"+newRowId);
	clone.find("#DCInvoice"+rowId).removeAttr("id").attr("id","DCInvoice"+newRowId);
	clone.find("#DCDate"+rowId).removeAttr("id").attr("id","DCDate"+newRowId);
    clone.appendTo( "#parent_witghoutpricing" ).after('</>');
 //  $("#parent_witghoutpricing").clone().attr('#parent_witghoutpricing', '#parent_witghoutpricing'+ rowId++).insertAfter("#parent_witghoutpricing");
    $(this).css("visibility", "hidden");
 
});
 /*  $(document).ready(function(){
//Get the modal
var modal = document.getElementById('myModal');

// Get the image and insert it inside the modal - use its "alt" text as a caption
var img = document.getElementById('myImg');
var modalImg = document.getElementById("img01");

img.onclick = function(){
    modal.style.display = "block";
    modalImg.src = this.src;
  
}

// Get the <span> element that closes the modal
var span = document.getElementsByClassName("close")[0];

// When the user clicks on <span> (x), close the modal
span.onclick = function() { 
    modal.style.display = "none";
}
}); 
*/

function convertNumberToWords(amount) {
	debugger;
    var words = new Array();
    words[0] = '';
    words[1] = 'One';
    words[2] = 'Two';
    words[3] = 'Three';
    words[4] = 'Four';
    words[5] = 'Five';
    words[6] = 'Six';
    words[7] = 'Seven';
    words[8] = 'Eight';
    words[9] = 'Nine';
    words[10] = 'Ten';
    words[11] = 'Eleven';
    words[12] = 'Twelve';
    words[13] = 'Thirteen';
    words[14] = 'Fourteen';
    words[15] = 'Fifteen';
    words[16] = 'Sixteen';
    words[17] = 'Seventeen';
    words[18] = 'Eighteen';
    words[19] = 'Nineteen';
    words[20] = 'Twenty';
    words[30] = 'Thirty';
    words[40] = 'Forty';
    words[50] = 'Fifty';
    words[60] = 'Sixty';
    words[70] = 'Seventy';
    words[80] = 'Eighty';
    words[90] = 'Ninety';
    amount = amount.toString();
    var atemp = amount.split(".");
    var number = atemp[0].split(",").join("");
    var n_length = number.length;
    var words_string = "";
    if (n_length <= 9) {
        var n_array = new Array(0, 0, 0, 0, 0, 0, 0, 0, 0);
        var received_n_array = new Array();
        for (var i = 0; i < n_length; i++) {
            received_n_array[i] = number.substr(i, 1);
        }
        for (var i = 9 - n_length, j = 0; i < 9; i++, j++) {
            n_array[i] = received_n_array[j];
        }
        for (var i = 0, j = 1; i < 9; i++, j++) {
            if (i == 0 || i == 2 || i == 4 || i == 7) {
                if (n_array[i] == 1) {
                    n_array[j] = 10 + parseInt(n_array[j]);
                    n_array[i] = 0;
                }
            }
        }
        value = "";
        for (var i = 0; i < 9; i++) {
            if (i == 0 || i == 2 || i == 4 || i == 7) {
                value = n_array[i] * 10;
            } else {
                value = n_array[i];
            }
            if (value != 0) {
                words_string += words[value] + " ";
            }
            if ((i == 1 && value != 0) || (i == 0 && value != 0 && n_array[i + 1] == 0)) {
                words_string += "crore";
            }
            if ((i == 3 && value != 0) || (i == 2 && value != 0 && n_array[i + 1] == 0)) {
            	debugger;
                words_string += "Lakhs ";
            }
            if ((i == 5 && value != 0) || (i == 4 && value != 0 && n_array[i + 1] == 0)) {
                words_string += "Thousand ";
            }
            if (i == 6 && value != 0 && (n_array[i + 1] != 0 && n_array[i + 2] != 0)) {
                words_string += "Hundred and ";
            } else if (i == 6 && value != 0) {
                words_string += "Hundred ";
            }
        }
        words_string = words_string.split("  ").join(" ");
    }
    return words_string;
}
function myFunc(){debugger;
	
	//alert("hai");
	document.getElementById("getInvoiceDetailsId").action = "getGrnDetails.spring";
	document.getElementById("getInvoiceDetailsId").method = "POST";
	document.getElementById("getInvoiceDetailsId").submit();	
	
	}
/* $('ul.nav a')
.unbind("click") // to remove your binding in my console
.bind('click',function(event){ // rebind event handler
    var $anchor = $(this);
    $('html, body').stop().animate({
      scrollLeft: $($anchor.attr('href')).offset().left
    }, 1500,'easeInOutExpo');
  event.preventDefault();

  // this is a trick:
  location.href = $anchor.attr('href');
  return false;
}); */
</script>

</body>
</html>
