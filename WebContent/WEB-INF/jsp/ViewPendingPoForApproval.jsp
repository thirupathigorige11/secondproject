<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>


<html lang="en">
<head>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />

<jsp:include page="./CacheClear.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/Sortingstyle.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="css/select2.min.css"  rel="stylesheet" type="text/css">
<script src="js/momment.js"></script>
<link href="css/sweetalert2.css" rel="stylesheet" type="text/css"> 
<script src="js/sweetalert2.js" type="text/javascript"></script>

<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
.selectAllchk span { font-size: 16px; font-weight: bold;}
.selectAllchk .selectAllchkinput {height: 22px;width: 22px;vertical-align: bottom;}
.amount-payments-sum, .amount-payments-sum-right{width:33.3% !important;}
.form-control {border:1px solid #000 !important;}
@media only screen and (max-width:768px){
.amount-payments-sum, .amount-payments-sum-right{width:100% !important;}

}
/* spinner stles */
.spinnercls{
    display: inline;  
    position: absolute;
    margin-left: 40%;
    top: -40px;
    display:none; 
}

</style>
<script src="js/jquery.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/stacktable.js"></script>
<script src="js/jquery.dataTables.min.js"></script>
<script src="js/dataTables.bootstrap.min.js"></script>
<script src="js/select2.min.js"></script>
</head>
<body class="nav-md">
	<noscript>
		<h3 align="center" style="font-weight: bold;">JavaScript is
			turned off in your web browser. Turn it on and then refresh the page.</h3>
		<style>
#mainDivId {
	display: none;
}
</style>
	</noscript>
	<div class="container body" id="mainDivId">
		<div class="main_container" id="main_container">
			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">
					<div class="clearfix"></div>
					<jsp:include page="./SideMenu.jsp" />
				</div>
			</div>
			<jsp:include page="./TopMenu.jsp" />
			<div class="right_col" role="main">
				<div id="mydiv" class="hide-show-iframe">
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Payment</a></li>
							<li class="breadcrumb-item active">View Pending Po For Approval</li>
						</ol>
					</div>
					
						<div class="col-md-12" id="hide-show-iframe">
						<div class="invoice-payment-heading">
							<h4 class="text-center">PO</h4>
						</div>
						<div class="body-invoice-payment">					
							<form class="form-horizontal" action="ViewPoPendingforApproval.spring">
								<div class="col-md-12 col-xs-12 col-sm-12">
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-4 col-xs-12"><strong>PO From</strong></label>
											<div class="col-md-1">:</div>
											<div class="col-md-7 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color"  id="datepicker-paymentreq-fromdate" name="fromDate" value="${fromDate}" autocomplete="off" readonly="true"/>
												 <label class="input-group-addon btn" for="datepicker-paymentreq-fromdate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-4 col-xs-12"><strong>PO To</strong></label>
											<div class="col-md-1">:</div>
											<div class="col-md-7 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color" id="datepicker-paymentreq-todate" name="toDate" value="${toDate}" autocomplete="off" readonly="true"/>
													 <label class="input-group-addon btn" for="datepicker-paymentreq-todate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-4 col-xs-12"><strong>PO Number</strong></label>
											<div class="col-md-1">:</div>
											<div class="col-md-7 col-xs-12">
												<input type="text" class="form-control control-text-height" name="tempPoNumber" id="InvoiceNumber" value="${invoiceNumber}" />
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-4 col-xs-12"><strong>Vendor Name</strong></label>
											<div class="col-md-1">:</div>
											<div class="col-md-7 col-xs-12">
												<input type="text" class="form-control control-text-height" id="VendorNameId" name="vendorName" value="${vendorName}"/>
											</div>
										</div>
									</div>
								<div class="col-md-6 col-xs-12">
								<div class="form-group">
								<label class="col-md-4 col-xs-12" style="margin-top: 2px;">Site Names</label>
								<div class="col-md-1">:</div>
								 <div class="col-md-7 col-xs-12"> <select   name="sitenames"  id="sitenames" data-placeholder="Select an option" class="select2-offscreen" multiple style="width:100%;">
		           				</select></div>
		           				</div>		           				
		           				</div>	
		           			<!-- 	<div class="col-md-6 col-xs-12">
								<div class="form-group">
								<label class="col-md-4 col-xs-12" style="margin-top: 2px;">PO Types</label>
								<div class="col-md-1">:</div>
								 <div class="col-md-7 col-xs-12"> 
								 	<select class="form-control" id="POType">
								 		<option value="">--Select--</option>
								 		<option value="NormalPO">Normal PO</option>
								 		<option value="RevisePO">Revise PO</option>
								 		<option value="ModifiedPO">Modified PO</option>
								 		<option value="UpdatePO">Update PO</option>
								 		<option value="CancelPO">Cancel PO</option>
								 	</select>
								 </div>
		           				</div>		           				
		           				</div> -->							
  								<div class="clearfix"></div>
  								<div class="col-md-12 col-xs-12 col-sm-12 col-lg-12 text-center center-block">
										<button class="btn btn-warning submit-form-invoice Mrgtop10" onclick="return validate()">Submit</button>
								</div>
								</div>
							</form>

						</div>
						<%-- <%
							String url = "";
							if (request.getLocalPort() == 8078) {
								url = "http://129.154.74.18:8078/Sumadhura_UAT";
							} else if (request.getLocalPort() == 8079) {
								url = "http://129.154.74.18:8079/Sumadhura_CUG";
							} else if (request.getLocalPort() == 80) {
								url = "http://129.154.74.18:80/Sumadhura";
							} else if (request.getLocalPort() == 8027) {
								url = "http://localhost:8027/Sumadhura";
							}
						%> --%>
						<br></br>
						 <%
							String isShowGrid = request.getAttribute("showGrid") == null
									? ""
									: request.getAttribute("showGrid").toString();
							if (isShowGrid.equals("true")) {
								/* int intTotalInvoices = Integer.parseInt(request.getAttribute("listTotalInvoicesSize") == null
										? ""
										: request.getAttribute("listTotalInvoicesSize").toString()); */
						%> 
						
						<div class="clearfix"></div>
						<form class="form-horizontal" id="paymentIntiateFormId">
							<c:forEach items="${PendingPoDetails}" var="element">
								<div class="form-invoice-data" id="slide-form-fileds${element.intSerialNo}">	
								  <div class="col-md-12">
								  	<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-5">Action </label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													<select class="form-control actionChange" id="actionType${element.intSerialNo}">
														<option value="">--Select--</option>
												 		<option value="NormalPO">Normal PO</option>
												 		<option value="RevisePO">Revise PO</option>
												 		<option value="ModifiedPO">Modified PO</option>
												 		<option value="UpdatePO">Update PO</option>
												 		<option value="CancelPO">Cancel PO</option>	
													</select>
												</div>
											</div>
										</div>
								  </div>
								  
								  <div class="clearfix"></div>							 
									<%-- <label class="" style="z-index:1;"> 
										<input type="checkbox" style="height:20px;width:20px;margin-top:0px;" name="checkboxname${element.intSerialNo}" id="chbox${element.intSerialNo}" value="checked">
									</label> --%>
									<div class="col-md-12">
									
										<img src="images/spinner.gif" class="spinnercls"  id="spinner${element.intSerialNo}">
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-5">S.No</label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													 <p>
														<strong>${element.intSerialNo}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-6 col-lg-5">Created Date</label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													<p>
														<strong>${element.getStrScheduleDate()}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-6 col-lg-5">Created By</label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													<p>
														<strong>${element.getCreatedBY()}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="clearfix visible-lg"></div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-6 col-lg-5">PO Number</label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													<p class="color-blue">
													<c:choose>
													<c:when test="${element.getType_Of_Purchase()=='CANCEL PO'}">
													<a  href="showPerminentPODetailsToCancel.spring?poNumber=${element.getPonumber()}&siteId=${element.getSiteId()}&indentNumber=${element.getIndentNumber()}&siteWiseIndentNo=${element.getSiteWiseIndentNumber()}&siteName=${element.getSiteName()}&fromdate=${element.getFromDate()}&toDate=${element.getToDate()}&poType=${element.getPreparedBy()}&isApprove=true" target="_blank" class="anchor-class" id="ponumber">${element.getPonumber()}</a>
													</c:when>
													<c:when test="${element.getType_Of_Purchase() == 'UPDATE PO'}">
													<a href="getDetailsforPoApproval.spring?poNumber=${element.getPonumber()}&siteId=${element.getSiteId()}&indentNumber=${element.getIndentNumber()}&siteWiseIndentNo=${element.getSiteWiseIndentNumber()}&siteName=${element.getSiteName()}&fromdate=${element.getFromDate()}&toDate=${element.getToDate()}&isUpdate=true" target="_blank" style="text-decoration: underline;color: blue;" class="poNumber">${element.getPonumber()}</a>
													</c:when>
													
													 <c:when test="${element.getType_Of_Purchase()!= 'MARKETING DEPT'}">
													<a href="getDetailsforPoApproval.spring?poNumber=${element.getPonumber()}&siteId=${element.getSiteId()}&indentNumber=${element.getIndentNumber()}&siteWiseIndentNo=${element.getSiteWiseIndentNumber()}&siteName=${element.getSiteName()}&fromdate=${element.getFromDate()}&toDate=${element.getToDate()}" target="_blank" class="anchor-class">${element.getPonumber()}</a>
													</c:when>  
													<c:otherwise>
													 <a href="getDetailsforMarketingPoApproval.spring?poNumber=${element.getPonumber()}&siteId=${element.getSiteId()}&indentNumber=${element.getIndentNumber()}&siteWiseIndentNo=${element.getSiteWiseIndentNumber()}&siteName=${element.getSiteName()}&fromdate=${element.getFromDate()}&toDate=${element.getToDate()}" target="_blank" class="anchor-class">${element.getPonumber()}</a>
													 </c:otherwise>
													
													</c:choose>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-6 col-lg-5">Vendor Name</label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													<p style="word-break: break-word;">
														 <strong>${element.getVendorName()}</strong> 
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-6 col-lg-5">Site Name</label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													<p class="color-blue">
														 <strong>${element.getSiteName()}</strong> 
													</p>
												</div>
											</div>
										</div>
										<div class="clearfix visible-lg"></div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-6 col-lg-5">Site wise Indent Number</label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													<p style="color:blue;">
														 <strong>${element.getSiteWiseIndentNumber()}</strong> 
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-6 col-lg-5">Type of PO</label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													<p>
														 <strong>${element.getType_Of_Purchase()}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-6 col-lg-5">Pending Employee Name</label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													<p class="color-blue">
														<strong>${element.getPending_Emp_Name()}</strong>
													</p>
												</div>
											</div>
										</div>
										<div class="clearfix visible-lg"></div>
										<div class="col-md-6 col-lg-4">
											<div class="form-group">
												<label class="col-md-6 col-lg-5">PO Amount</label>
												<div class="col-md-1">:</div>
												<div class="col-md-5 col-lg-6">
													<p class="color-blue">
														 <strong>${element.getPoAmount()}</strong> 
													</p>
												</div>
											</div>
										</div>									
									</div>
								</div>
							</c:forEach>							
						</form>
						<%
							}
							else{
						%>
						<c:if test="${not isItFirstRequest}">
						<div class="col-md-offset-3 col-md-6 text-center"><font color="red" size="5" >Data not available.</font></div>
						</c:if>
						<%
							}
						%>
					</div>
					<!-- Main Content of The Page end  -->
					<!-- iFrame Start -->
					<div class="loader-sumadhura" style="display: none;">
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
					<div class="cancel-iframe" id="" style="display: none;">
						<iframe src="" id="myframe"></iframe>
						<div class="col-md-12 text-center center-block">
							<button class="btn btn-warning" id="cancel-btn">Close</button>
						</div>
					</div>
					<div class="cancel-image-iframe" id="" style="display: none;">
						<iframe id="myframe-1" src="" style="padding-bottom: 50px;">
						</iframe>


						<div class="col-md-12 text-center center-block">
							<button class="btn btn-warning" id="cancel-btn-1">Close</button>
						</div>
					</div>
					<div id="myModal" class="modal fade" role="dialog">
					  <div class="modal-dialog">
					    <div class="modal-content">
					      <div class="modal-header">					      	
					        <button type="button" class="close" data-dismiss="modal">&times;</button>
					        <h4 class="modal-title" id="titleForModal"></h4>
					      </div>
					      <div class="modal-body" style="height: 75px;padding: 24px;">
					      <input type="hidden" id="serialNum">
					      <input type="hidden" id="actionTypeInHidden">
					      	<div class="col-md-12 text-center center-block" id="modalBody" style="height:75px;">
					      		
					      	</div>
					      </div>
					      <div class="center-block text-center" style="padding: 15px;border-top: 1px solid #e5e5e5;">
					        <button type="button" class="btn btn-warning" onclick="submitData()">Submit</button>
					      </div>
					    </div>					
					  </div>
					</div>
				</div>
			</div>
		</div>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/sidebar-resp.js" type="text/javascript"></script>
		<script src="js/jquery.simpleTableSort.js" type="text/javascript"></script>
		<script>
		
		$(function() {
		  	$('#VendorNameId').keypress(function () {debugger;
			  $.ajax({
			  url : "./getVendorDetails.jsp",
			  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
			  type : "get",
			  data : "",
			  contentType : "application/json",
			  success : function(data) {
			  		$("#VendorNameId").autocomplete({
				  		source : data
				  	});
			  },
			  error:  function(data, status, er){
				  //alert(data+"_"+status+"_"+er);
				  }
			  });
		  	});
		  	$('#VendorNameId').on('change', function(){
		  		var value = $(this).val();
		  		value = value.replace("&", "$$$");
		  		setVendorData (value); //pass the value as paramter
			 });
		  });
		  
		  function setVendorData(vName) {				
			var url = "loadAndSetVendorInfo.spring?vendName="+vName;			  
			if(window.XMLHttpRequest) {
				request = new XMLHttpRequest();	  
			}  
			else if(window.ActiveXObject) {
				request = new ActiveXObject("Microsoft.XMLHTTP");  
			}	  
			try {
				request.onreadystatechange = setVedData;
				request.open("POST", url, true);
				request.send();  
			}
			catch(e) {
				alert("Unable to connect to server!");
			}
		}

		function setVedData() {
			if(request.readyState == 4 && request.status == 200) {
				var resp = request.responseText;
				resp = resp.trim();
				var vendorId = resp.split("|")[0];
				var vendorAddress = resp.split("|")[1];
				var vendorGsinNo = resp.split("|")[2];				
				$("#vendorIdId").val(vendorId);
				$("#vendorAddress").val(vendorAddress);
			}
		}
		
		$(document).ready(function() {
			$("#sitenames").select2();
			$(".vendName").text().toLowerCase();
			$(".up_down").click(function() {
				$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
				$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			});
		});
		
			$(function() {				
				$("#datepicker-paymentreq-fromdate").datepicker({
					changeMonth: true,
				    changeYear: true,
					dateFormat : "dd-M-y",
					maxDate : new Date(),
					onSelect: function(selected) {
			     	        $("#datepicker-paymentreq-todate").datepicker("option","minDate", selected)
			     	}

				});
				$("#datepicker-paymentreq-todate").datepicker({
					changeMonth: true,
				    changeYear: true,
					dateFormat : "dd-M-y",
					maxDate : new Date(),
					onSelect: function(selected) {
		            	$("#datepicker-paymentreq-fromdate").datepicker("option","maxDate", selected)
		            }


				});		
				
				$(".actionChange").change(function(){
					  var id=$(this).attr("id").split("actionType")[1];	
					  var value=$(this).val();
					  var header="";
					  var body="";
					  if(value=="NormalPO"){
						  header="CC Emails";
						  body="<input type='text' id='email' name='email' class='form-control' placeholder='Please enter email.'>";
					  }
					  if(value=="ModifiedPO"){
						  header="Modify Temp PO Remarks";
						  body="<input type='text' id='modifyTempPoRemarks' name='modifyTempPoRemarks' class='form-control' placeholder='Please enter modify temp po remarks.'>";				
					  }
					  if(value=="CancelPO"){
						  header="Cancel Temp PO Remarks";
						  body="<input type='text' id='cancelTempPoRemarks' name='cancelTempPoRemarks' class='form-control' placeholder='Please enter cancel temp po remarks.'>";				  
					  }
					  
					  $("#titleForModal").html(header);
					  $("#serialNum").val(id);
					  $("#modalBody").html(body);
					  $("#actionTypeInHidden").val(value);					  
					  $("#myModal").modal();					  
				})
				
				    
				
			});
			
			function submitData(){
				console.log("Clicked.....!");
				var idFromInput=$("#serialNum").val();
				$("#spinner"+idFromInput).show();
				$("#myModal").modal('hide');
				$("#slide-form-fileds"+idFromInput).fadeOut(2500);		
				setTimeout(function(){ 
					$("#slide-form-fileds"+idFromInput).remove().fadeOut("slow");					
					swal("Good job!", "Po aprroved successfully!", "success");
				}, 3000);				
			}
			/*script for datepickers*/

			function convertDate(d) {
				var p = d.split("/");
				return +(p[2] + p[1] + p[0]);
			}
			
			$(document).ready(function() {
				$("#PONumberConatiner").show();
				$("#PONumber").css("background-color", "white");
				$("#PONumber").click(function() {
					$("#PONumber").css("background-color", "white");
					$("#POInvoice").css("background-color", "#c7b9b9;");
					$("#POInvoiceConatiner").hide();
					$("#PONumberConatiner").show();
				});
				$("#POInvoice").click(function() {
					$("#POInvoice").css("background-color", "white");
					$("#PONumber").css("background-color", "#c7b9b9;");
					$("#POInvoiceConatiner").show();
					$("#PONumberConatiner").hide();
					$("#container1").hide();
					$("#paymentIntiateButton").hide();
				});
			});

			$(document).ready(function() {
				$("#saveId").click(function() {
					$("#container1").show();
				});
			});

			$(document).ready(function() {
						debugger;
						$("#tblnotification").simpleTableSort({
							order : 'asc',
							excludeSortColumns : [ 1 ],
							dynamic : true,
							multiSortStates : false,
							onBeforeSort : function(index) {
								$('#sort-loading').show();
							},

							onAfterSort : function(index) {
								$('#sort-loading').animate({
									opacity : 0
								}, 100, function() {
									$(this).css({
										display : 'none',
										opacity : .5
									});
								});
							},

							sortMethods : {
								customMail : function(a, b) {
									a = a.split('@')[1];
									b = b.split('@')[1];

									return a > b ? 1 : -1;
								}
							}
						});

						$('#desc-link').on('click', function() {
							var pre = $('#desc-link').find('.pre');
							$('#desc').slideToggle(function() {
								pre.text(pre.text() == '+' ? '-' : '+');
							});
						});

						$('#delete-row').on('click', function() {
							$('#paymentTable').find('tbody').children().first().eq(0).remove();
						});
					});
			function saveRecords() { debugger;
				$('.loader-sumadhura').show();
				if ($('input[type=checkbox]:checked').length == 0) {
					alert('Please initiate atleast one payment against invoice.');
					$('.loader-sumadhura').hide();
					return false;
				}

				if ($('#myMessage').val() == '') {
					alert('Input can not be left blank');
				}
				var status = validateRowData();
				if(!status){
					$('.loader-sumadhura').hide();
				     return status;				
				}
				var canISubmit = window.confirm("Do you want to submit?");

				if (canISubmit == false) {
					$('.loader-sumadhura').hide();
					return false;
				}
                $("#paymentIntiateButton").attr("disabled", true);
				document.getElementById("paymentIntiateButton").disabled = true;
				document.getElementById("paymentIntiateFormId").action = "savePaymentIntiateDetails.spring";
				document.getElementById("paymentIntiateFormId").method = "POST";
				document.getElementById("paymentIntiateFormId").submit();
			}
			
			/* **************** Valdiate from date and Todate************ */

			function validateRowData() {debugger;
			
				var noOfRecords= $('#noOfRecords').val();				
				var AmnttoBeRelsed = "";
				var Amntreqdate = "";
				var isSelectAnyone = ""; 
				var paymentReqUpto = "";
				var paymentDoneUpto = "";
				var AdjustAmountFromAdvance = "";
				var invoiceAmount = "";
				var remainingPOAdvance = "";
			    var payBalanceInPo = "";
			    var paymentDoneOnMultipleInvoices = "";
			    var adjustedAmountFromPo = "";
			    var paymentRequestedOnPO = "";
			    var poAmount = "";
			    
				for (i = 1; i <= noOfRecords; i++) { 					
					var checkedValue = $('#chbox'+i+':checked').val();
					
					 AmnttoBeRelsed = $("#AmountToBeReleased" + i).val(); 
					 Amntreqdate = $("#PaymentreqDateId" + i).val();
					 paymentReqUpto = $("#paymentReqUpto" + i).val();
					 paymentDoneUpto = $("#paymentDoneUpto" + i).val();
					 AdjustAmountFromAdvance = $("#AdjustAmountFromAdvance" + i).val(); 
					 invoiceAmount = $("#invoiceAmount" + i).val();
					 remainingPOAdvance = $('input[name="remainingPOAdvance'+i+'"]').val();
					 payBalanceInPo = $('input[name="payBalanceInPo'+i+'"]').val();
					 paymentDoneOnMultipleInvoices = $('input[name="paymentDoneOnMultipleInvoices'+i+'"]').val();
					 adjustedAmountFromPo = $('input[name="adjustedAmountFromPo'+i+'"]').val();
					 paymentRequestedOnPO = $('input[name="paymentRequestedOnPO'+i+'"]').val();
					 poAmount = $('input[name="poAmount'+i+'"]').val();
					    
			      if(checkedValue == "checked" ) {
			    	  debugger;
					 if(AmnttoBeRelsed == "" || AmnttoBeRelsed == null || AmnttoBeRelsed == '') {
				          alert("Please enter Amount to be released in S.No:" +i);
				          document.getElementById("AmountToBeReleased"+i).focus(); 
				          return false;

			          }
					 
					 if(Amntreqdate == "" || Amntreqdate == null || Amntreqdate == '') {
				          alert("Please select payment requested date in S.No:" +i);
				          document.getElementById("PaymentreqDateId"+i).focus(); 
				          return false;

			          }
					 var payingAmt = (+paymentReqUpto + +paymentDoneUpto + +AmnttoBeRelsed + +AdjustAmountFromAdvance);// used the unary plus operator to convert them to numbers first.
					 if(+payingAmt > +invoiceAmount){
						  alert("Total of Intiated Amount & Paid Amount is greater than Invoice Amount in S.No:" +i);
				          document.getElementById("AmountToBeReleased"+i).focus(); 
				          return false;
					 }
					 if(+AdjustAmountFromAdvance > +remainingPOAdvance){
							alert("Adjust Amount is greater than Advance in S.No:" +i);
							$('input[name="AdjustAmountFromAdvance'+i+'"]').focus();
			          		return false;
					 }
					 if(payBalanceInPo != 'NO_ADVANCE'){
					 	if(+AmnttoBeRelsed > +payBalanceInPo){
							alert("Total of Intiated Amount & Paid Amount is greater than PO Amount in S.No:" +i);
							$('input[name="AmountToBeReleased'+i+'"]').focus();
			          		return false;
					 	}
					 	if(+AmnttoBeRelsed > +payBalanceInPo - (+paymentDoneOnMultipleInvoices - +adjustedAmountFromPo) /* - +paymentRequestedOnPO */){
							alert("Total of Intiated Amount & Paid Amount is greater than PO Amount in S.No:" +i);
							$('input[name="AmountToBeReleased'+i+'"]').focus();
			          		return false;
					 	}
					 }
					 if(payBalanceInPo == 'NO_ADVANCE' && poAmount!=0){
						 	if(+AmnttoBeRelsed > +poAmount - +paymentDoneOnMultipleInvoices - +paymentRequestedOnPO){
								alert("Total of Intiated Amount & Paid Amount is greater than PO Amount in S.No:" +i);
								$('input[name="AmountToBeReleased'+i+'"]').focus();
				          		return false;
						 	}
						 }
					 
			 		}
					 //end
				}
				return true;
			}
			//**********Method for validation for PO Number*************
			function validate() {debugger;
	
				 var InvoiceFrom=$("#datepicker-paymentreq-fromdate").val();
			     var InvoiceTo=$("#datepicker-paymentreq-todate").val();
			     var InvoiceNumber=$("#InvoiceNumber").val();
			     var VendorName=$("#VendorNameId").val();
			     
			     var SelectAll =$("#selectAll").prop("checked"); 
				 $("#hiddenSelectAll").val(SelectAll);
				 
				 var ReqDateFrom=$("#paymentReqDatefrom").val();
			     var ReqDateTo=$("#paymentReqDateto").val();
			     
				 var isPaymentReqDateChecked = $("#paymentReqDateCheck").prop("checked");
				 $("#hiddenpaymentReqDateCheck").val(isPaymentReqDateChecked);  
			     
				 //trim method 
			      var InvoiceFromdate=InvoiceFrom.trim();
			     var InvoiceTodate=InvoiceTo.trim();
			     var InvoiceNumberId=InvoiceNumber.trim();
			     var VendorNameId=VendorName.trim();
			   
				if (!InvoiceFromdate && !InvoiceTodate && !InvoiceNumberId && !VendorNameId && SelectAll==false && (!ReqDateFrom || isPaymentReqDateChecked==false) && (!ReqDateTo || isPaymentReqDateChecked==false)) {
					alert("Atleast one filed is required.");
					return false;
				}
				else{
					$('.loader-sumadhura').show();
					return true;
				}

			}

			/* ********** Method for sorting table************ */

			/* ****** Method for validating the amount requested amnt to PO Amont******** */

			function validateAmnt(rowNum) {
				debugger;
				var OriganlaPoamnt = $("#InvAmnt" + rowNum).text();
				var OringinalPOAnt = parseInt(OriganlaPoamnt);
				var AmnttoBeRelsed = $("#AmountToBeReleased" + rowNum).val();
				var compareVal = parseInt(AmnttoBeRelsed);

				if (compareVal > OringinalPOAnt) {
					alert('Please enter lesser amont');
					$("#AmountToBeReleased" + rowNum).val('');
					document.getElementById("AmnttoBeRelsed").focus();
				}
			};

			/* ****** Method for validating the amount requested amnt to POINvoice******** */

			function validateAmnt(rowNum) {
				debugger;
				var OriganlaPoamnt = $("#POAmnt" + rowNum).text();
				var OringinalPOAnt = parseInt(OriganlaPoamnt);
				var AmnttoBeRelsed = $("#AmountToBeReleased" + rowNum).val();
				var compareVal = parseInt(AmnttoBeRelsed);

				if (compareVal > OringinalPOAnt) {
					alert('Please enter lesser amont');
					$("#AmountToBeReleased" + rowNum).val('');
					document.getElementById("AmnttoBeRelsed").focus();
				}
			};

			/* =========================== */
			//To select country name
			function selectCountry(val) {
				$("#search-box").val(val);
				$("#suggesstion-box").hide();
			}
			/* ********* Method for Filer the table**************** */

			function tableSearch1() {
				debugger;

				var value = $("#myInput1").val();
				$('table tbody tr').hide();
				var sum = 0;
				var tquantity = 0;
				var PaidAmnt = 0;
				var POAount = 0;
				$('table tbody tr td:contains("' + value + '")').each(function(index, item) {
							$(this).closest('tr').show();
							var reqamount = $(this).closest('tr').find('.poAmnt').text();
							sum = sum + parseFloat(reqamount);
							$("#subtotalpoAmount1").text(sum);
							$(".info").show();

							var reqamount1 = $(this).closest('tr').find('.Tillamnt').text();
							tquantity = tquantity + parseFloat(reqamount1);
							$("#subtotalpoAmount2").text(tquantity);
							$(".info").show();

							var reqamount2 = $(this).closest('tr').find('.paidnmnt').text();
							PaidAmnt = PaidAmnt + parseFloat(reqamount2);
							$("#subtotalpoAmount3").text(PaidAmnt);
							$(".info").show();
						});

			};
		</script>
		<script>
			/* url sending to page start and hide and show iframe */
			function SetPage(url) {
				var z = document.getElementById("myframe").setAttribute("src", url);
				$('.loader-sumadhura').show();
			}
			$('#myframe').load(function() {
				$('.loader-sumadhura').hide();

			});

			$(".anchor-invoice").click(function() {
				$("#hide-show-iframe").hide();
				$(".cancel-iframe").show();
			});
			$("#cancel-btn").click(function() {
				$("#hide-show-iframe").show();
				$(".cancel-iframe").hide();
				var z = document.getElementById("myframe").setAttribute("src", '');
			});
			/* url sending to page end and hide and show iframe */
			/* full image sending to page start and hide and show iframe */
			function SetPageImage(url) {
				var f = document.getElementById("myframe-1").setAttribute("src", url);
				$("#myframe-1").contents().find("#Home_img").hide();
		        $("#myframe-1").contents().find("#left_col").remove();
		        $("#myframe-1").contents().find(".top_nav").remove();
		        //$("#myframe-1").contents().find(".nav-md .container.body .right_col").css({"margin-left":"0px !important"});
				$('.loader-sumadhura').show();
			}
			$('#myframe-1').load(function() {
				$('.loader-sumadhura').hide();

			});

			$(".ponumber-anchor").click(function() {
				$("#hide-show-iframe").hide();
				$(".cancel-image-iframe").show();

			});
			$("#cancel-btn-1").click(function() {
				 $("#hide-show-iframe").show();
				 $(".cancel-image-iframe").hide();
				var f = document.getElementById("myframe-1").setAttribute("src", '');
			});
			/* full image sending to page end and hide and show iframe */
				
		 $(".submit-form-invoice").click(function(){
			 var a=$("#datepicker-paymentreq-fromdate").val();
			 var b=$("#datepicker-paymentreq-todate").val();
		});
			
		//validating the amount tobe release wether number or not
		function validateAmountToBeRelease(id){
			var AmountToBeReleased=$("#AmountToBeReleased"+id).val();
			if(isNaN(AmountToBeReleased)){
				alert("Please enter valid data.");
				$("#AmountToBeReleased"+id).val("");
				$("#AmountToBeReleased"+id).focus();
				return false;
			}
		}
		//validating the adjusted amount tobe release wether number or not
		function validateAdjustamountformadvance(id){
			var AdjustAmountFromAdvance=$("#AdjustAmountFromAdvance"+id).val();
			if(isNaN(AdjustAmountFromAdvance)){
				alert("Please enter valid data.");
				$("#AdjustAmountFromAdvance"+id).val("");
				$("#AdjustAmountFromAdvance"+id).focus();
				return false;
			}
		}
		
		//checking number number 
		function isNumberCheck(el, evt) {
			debugger;
		    var charCode = (evt.which) ? evt.which : event.keyCode;
		    var num=el.value;
		    var number = el.value.split('.');
		    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57) ||  charCode == 13) {
		        return false;
		    }
		    //just one dot
		    if((number.length > 1 && charCode == 46) || ( el.value=='' && charCode == 46)) {
		         return false;
		    }
		    //get the carat position
		    var caratPos = getSelectionStart(el);
		    var dotPos = el.value.indexOf(".");
		    if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){
		        return false;
		    }
		    return true;
		}
		function getSelectionStart(o) {
			if (o.createTextRange) {
				var r = document.selection.createRange().duplicate();
				r.moveEnd('character', o.value.length);
				if (r.text == '') return o.value.length;
				return o.value.lastIndexOf(r.text);
			} else return o.selectionStart;
		}	
		
		/* Reload the site when reached via browsers back button */
		if(!!window.performance && window.performance.navigation.type == 2)
		{
		    window.location.reload();
		}
	    /* Reload the site when reached via browsers back button */
		loadSiteNames();
		 function loadSiteNames(){
			 $.ajax({
					url:"siteNameDetails.spring",
					type:"GET",
					success:function(response){debugger;
							var response=JSON.parse(response).xml.site;
							var selectdata='';
			        		for(var i=0;i<response.length;i++){
			        			 selectdata+='<option value='+response[i].SITEID+'>'+response[i].SITENAME+'</option>';
			        		}
							$("#sitenames").html(selectdata);
					}
				});
		 }
		
		
		</script>
</body>
</html>
