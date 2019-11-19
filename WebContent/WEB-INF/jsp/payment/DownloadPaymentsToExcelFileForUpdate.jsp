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


<jsp:include page="../CacheClear.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.min.css"/>

<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
.form-control {border:1px solid #000 !important;}
table.dataTable {border-collapse:collapse !important;}
.success{font-size:18px;}
</style>


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

					<jsp:include page="../SideMenu.jsp" />

				</div>
			</div>
			<jsp:include page="../TopMenu.jsp" />


			<!-- page content -->
			<div class="right_col" role="main">
				<div id="mydiv" class="hide-show-iframe">
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Payment</a></li>
							<li class="breadcrumb-item active">Download Payments in Excel</li>
						</ol>
					</div>
					<!-- loader -->
					<div class="loader-sumadhura" style="display: none;z-index:999;">
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
					<!-- loader -->
					<!-- Main Content of The Page Starts from Here -->
					<div class="col-md-12" id="hide-show-iframe">
						<div class="invoice-payment-heading">
							<h4 class="text-center">UPDATE PAYMENT</h4>
						</div>
						<div class="body-invoice-payment">
						    <div id = "resp-msg">
						    <center><label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label></center> 
						    </div><br>
							<form class="form-horizontal" id="paymentIntiateFormId">
								<div class="col-md-12 col-xs-12 col-sm-12">
									<div class="col-md-6 col-sm-6 col-xs-12">
										<div class="form-group">
											<label class="col-md-6 col-sm-6 col-xs-12"><strong>Payment Done From  :</strong></label>
											<div class="col-md-6 col-sm-6 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color"
													id="datepicker-paymentreq-fromdate" name="fromDate"
													value="${fromDate}"  autocomplete="off" readonly="true"/>
													<label class="input-group-addon btn" for="datepicker-paymentreq-fromdate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
									<div class="col-md-6 col-xs-12 col-sm-6">
										<div class="form-group">
											<label class="col-md-6 col-sm-6 col-xs-12"><strong>Payment Done To  :</strong></label>
											<div class="col-md-6 col-sm-6 col-xs-12 input-group">
												<input type="text" class="form-control control-text-height readonly-color"
													id="datepicker-paymentreq-todate" name="toDate"
													value="${toDate}"  autocomplete="off" readonly="true"/>
													<label class="input-group-addon btn" for="datepicker-paymentreq-todate"> <span class="fa fa-calendar"></span> </label>
											</div>
										</div>
									</div>
								</div>
								<div class="col-md-12 col-xs-12 col-sm-12 text-center center-block Mrgtop20">
									<input class="btn btn-warning" type="button" value="View" id="paymentIntiateButton1"  onclick="saveRecords('SaveClicked')">
									<input class="btn btn-warning" type="button" value="Download Excel" id="paymentIntiateButton2"  onclick="saveRecords1('SaveClicked')">
							   </div>
							</form>

						</div>
						<%
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
						%>
						<br></br>
						<%
							String isShowGrid = request.getAttribute("showGrid") == null
									? ""
									: request.getAttribute("showGrid").toString();
							if (isShowGrid.equals("true")) {
								int intPendingPayments = Integer.parseInt(request.getAttribute("listTotalPaymentReqSize") == null
										? "0"
										: request.getAttribute("listTotalPaymentReqSize").toString());
						%>
						<div class="table-responsive">
						<table class="table table-new" id="tblnotification">
						<thead>
							<tr>
								<th>Vendor / Contractor</th>
								<th>Invoice No</th>
								<th>PO Number</th>
								<th>WorkOrder No</th>
								<th>Paid Amount</th>
								<th>UTR/Cheque No</th>
								<th>Payment Done Date</th>
								<th>PaymentTransaction Id</th>
								<th>Payment Id</th>
								<th>VND / CNT</th>
							</tr>
						</thead>
						<tbody>
						<c:forEach items="${listofPendingPayments}" var="element">
							<tr>
								<c:if test="${element.isVNDorCNT.equals('VND')}">
									<td>${element.strVendorName}</td>
									<td>${element.strInvoiceNo}</td>
									<td>${element.strPONo}</td>
									<td></td>
									<td>${element.doublePaidAmount}</td>
									<td>${element.utrChequeNo}</td>
									<td>${element.strPaymentDate}</td>
									<td>${element.intPaymentTransactionId}</td>
									<td>${element.intPaymentDetailsId}</td>
									<td>${element.isVNDorCNT}</td>
								</c:if>
								<c:if test="${element.isVNDorCNT.equals('CNT')}">
									<td>${element.strContractorName}</td>
									<td></td>
									<td></td>
									<td>${element.workOrderNo}</td>
									<td>${element.doublePaidAmount}</td>
									<td>${element.utrChequeNo}</td>
									<td>${element.strPaymentDate}</td>
									<td>${element.intPaymentTransactionId}</td>
									<td>${element.intCntPaymentId}</td>
									<td>${element.isVNDorCNT}</td>
								</c:if>
							</tr>
						</c:forEach>
						</tbody>
						</table>
						</div>
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
							<button class="btn btn-warning" id="cancel-btn">Cancel</button>
						</div>
					</div>
					<!-- iFrame End -->
					<!-- iFrame for already loaded images start-->
					<div class="cancel-image-iframe" id="" style="display: none;">
						<iframe id="myframe-1" src="" style="padding-bottom: 50px;">
						</iframe>


						<div class="col-md-12 text-center center-block">
							<button class="btn btn-warning" id="cancel-btn-1">Cancel</button>
						</div>
					</div>
					<!-- iFrame for already loaded images end -->

				</div>
			</div>
		</div>
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/jquery.simpleTableSort.js" type="text/javascript"></script> 
		<script src="js/jquery.dataTables.min.js"></script>
        <script src="js/dataTables.bootstrap.min.js"></script> 
         <script src="js/sidebar-resp.js"></script>
		<script>
		$(function() {
			$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]}); 
			
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
		  		$('.loader-sumadhura').show();
		  		value = value.replace("&", "$$$");
		  		//alert(value);
		  		
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
				//alert(resp);
				var vendorId = resp.split("|")[0];
				var vendorAddress = resp.split("|")[1];
				var vendorGsinNo = resp.split("|")[2];
				
				$("#vendorIdId").val(vendorId);
				$("#vendorAddress").val(vendorAddress);
				$('.loader-sumadhura').hide();
			}
		}
			$(document)
					.ready(
							function() {
								$(".vendName").text().toLowerCase();
								$(".up_down")
										.click(
												function() {
													$(this)
															.find('span')
															.toggleClass(
																	'fa-chevron-up fa-chevron-down');
													$(this)
															.find('span')
															.toggleClass(
																	'fa-chevron-right fa-chevron-left');
												});

							});
			
			/*script for datepickers*/
			$(function() {
				$('.datepicker-paymentreq-class').each(function(){
				    $(this).datepicker({
						dateFormat : "dd-M-y",
						/* maxDate : new Date(), */
						changeMonth: true,
					      changeYear: true
					     

					});
				});
				$("#datepicker-paymentreq-fromdate").datepicker({
					dateFormat : "dd-M-y",
					maxDate : new Date(),
					changeMonth: true,
				    changeYear: true,
				    onSelect: function(selected) {
			     	        $("#datepicker-paymentreq-todate").datepicker("option","minDate", selected)
			     	        }
				});
				$("#datepicker-paymentreq-todate").datepicker({
					dateFormat : "dd-M-y",
					maxDate : new Date(),
					changeMonth: true,
				    changeYear: true,
				    onSelect: function(selected) {
			            	$("#datepicker-paymentreq-fromdate").datepicker("option","maxDate", selected)
			            	        }

				});
			});
			/*script for datepickers*/

			function convertDate(d) {
				var p = d.split("/");
				return +(p[2] + p[1] + p[0]);
			}

			$(function() {
				$(".ReqDateId1").datepicker({
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});
				$(".ReqDateId2").datepicker({
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});

				$(".invoiceReqDateId1").datepicker({
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});

				$(".invoiceReqDateId2").datepicker({
					dateFormat : 'dd-M-y',
					maxDate : new Date()
				});
				return false;
			});

			

			$(function() {
				$(".PaymentDateId").datepicker({
					dateFormat : 'dd-M-y',
					minDate : 0,
					changeMonth : true,
					changeYear : true
				//   showButtonPanel: true,
				// maxDate: '@maxDate',
				//   minDate: '@minDate'  
				}).attr('readonly', 'readonly');

			});

			$(document).ready(function() {
				$("#PONumberConatiner").show();
				$("#PONumber").css("background-color", "white");
				$("#PONumber").click(function() {
					$("#PONumber").css("background-color", "white");
					$("#POInvoice").css("background-color", "#c7b9b9;");
					$("#POInvoiceConatiner").hide();
					//$("#temporary-class").show();
					$("#PONumberConatiner").show();
					// $("#paymentIntiateButton").show();

				});
				$("#POInvoice").click(function() {
					$("#POInvoice").css("background-color", "white");
					$("#PONumber").css("background-color", "#c7b9b9;");
					$("#POInvoiceConatiner").show();
					//$('#withoutpricingvendorwise').show();
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

			$(document).ready(
					function() {
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

						$('#delete-row').on(
								'click',
								function() {
									$('#paymentTable').find('tbody').children()
											.first().eq(0).remove();
								});
					});
			function saveRecords(saveClicked, rowId) {
                $(".loader-sumadhura").show();
				document.getElementById("resp-msg").style.display = "none";			   
				document.getElementById("paymentIntiateFormId").action = "viewDownloadablePayments.spring";
				document.getElementById("paymentIntiateFormId").method = "POST";
				document.getElementById("paymentIntiateFormId").submit();
			}

			function saveRecords1(saveClicked, rowId) {
				 $(".loader-sumadhura").show();
				document.getElementById("resp-msg").style.display = "none";
				   
				var canISubmit = window.confirm("Do you want to download?");

				if (canISubmit == false) {
					return false;
				}
				$(".loader-sumadhura").hide(); 
				document.getElementById("paymentIntiateFormId").action = "downloadPaymentsToExcelFileForUpdate.spring";
				document.getElementById("paymentIntiateFormId").method = "POST";
				document.getElementById("paymentIntiateFormId").submit();
				
			}
			/* **************** Valdiate from date and Todate************ */

			function validateRowData(rowId) {
				var AmnttoBeRelsed = $("#AmountToBeReleased" + rowId).val();
				var Amntreqdate = $("#PaymentreqDateId" + rowId).val();

				/* if(AmnttoBeRelsed == "" || AmnttoBeRelsed == null || AmnttoBeRelsed == ''|| Amntreqdate == "" || Amntreqdate == null || Amntreqdate == '') {
				 alert("Please enter Amount to be released and date");
				 document.getElementById("AmnttoBeRelsed").focus(); */

			}
			//**********Method for validation for PO Number*************
			function validate() {

				document.getElementById("resp-msg").style.display = "none";
			   

				var PaymentDoneFrom=$("#datepicker-paymentreq-fromdate").val();
			    var PaymentDoneTo=$("#datepicker-paymentreq-todate").val();
			    var InvoiceNumber=$("#invoiceNumber").val();
			    var PONumber=$("#poNumber").val();
			    var VendorName=$("#VendorNameId").val();
			    /* var Site =$("#dropdown_SiteId").val(); */
			    
			    
				
			    //trim method 
			    var PaymentDoneFromdate=PaymentDoneFrom.trim();
			    var PaymentDoneTodate=PaymentDoneTo.trim();
			    var InvoiceNumberId=InvoiceNumber.trim();
			    var VendorNameId=VendorName.trim();
			    var PONumberId=PONumber.trim();
			  
				if (!PaymentDoneFromdate && !PaymentDoneTodate && !InvoiceNumberId && !PONumberId && !VendorNameId ) {
					alert("Atleast one filed is required.");
					return false;
				}
				else{
					return true;
				}

}
			/* ********** Method for sorting table************ */

			//**********Method for validation for PO Invoice*************  
			function validate2() {

				var InvfrmDate = $("#invoiceReqDateId1").val();
				var InvtoDate = $("#invoiceReqDateId2").val();

				if (InvfrmDate == "" || InvfrmDate == null || InvfrmDate == ''
						|| InvtoDate == "" || InvtoDate == null
						|| InvtoDate == '') {
					alert("Please enter from date and to date");

				}

			}

			/* $(function() {
				$('#VendorNameId').keypress(function() {
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
						error : function(data, status, er) {
							alert(data + "_" + status + "_" + er);
						}
					});
				});
				$('#VendorNameId').on('change', function() {
					var value = $(this).val();

					value = value.replace("&", "$$$");
					//alert(value);

					setVendorData1(value); //pass the value as paramter
				});
			});

			function setVendorData1(vName) {

				var url = "loadAndSetVendorInfo.spring?vendName=" + vName;

				if (window.XMLHttpRequest) {
					request = new XMLHttpRequest();
				} else if (window.ActiveXObject) {
					request = new ActiveXObject("Microsoft.XMLHTTP");
				}
				try {
					request.onreadystatechange = setVedData1;
					request.open("POST", url, true);
					request.send();
				} catch (e) {
					alert("Unable to connect to server!");
				}
			}

			function setVedData1() {
				if (request.readyState == 4 && request.status == 200) {
					var resp = request.responseText;
					resp = resp.trim();
					alert(resp);
					var vendorId = resp.split("|")[0];
					var vendorAddress = resp.split("|")[1];
					var vendorGsinNo = resp.split("|")[2];

					$("#vendorIdId1").val(vendorId);

				}
			}

			$(function() {
				$('#VendorNameId-Numberwise').keypress(function() {
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
						error : function(data, status, er) {
							alert(data + "_" + status + "_" + er);
						}
					});
				});
				$('#VendorNameId-Numberwise').on('change', function() {
					var value = $(this).val();

					value = value.replace("&", "$$$");
					//alert(value);

					setVendorData2(value); //pass the value as paramter
				});
			});

			function setVendorData2(vName) {

				var url = "loadAndSetVendorInfo.spring?vendName=" + vName;

				if (window.XMLHttpRequest) {
					request = new XMLHttpRequest();
				} else if (window.ActiveXObject) {
					request = new ActiveXObject("Microsoft.XMLHTTP");
				}
				try {
					request.onreadystatechange = setVedData2;
					request.open("POST", url, true);
					request.send();
				} catch (e) {
					alert("Unable to connect to server!");
				}
			}

			function setVedData2() {
				if (request.readyState == 4 && request.status == 200) {
					var resp = request.responseText;
					resp = resp.trim();
					alert(resp);
					var vendorId = resp.split("|")[0];
					var vendorAddress = resp.split("|")[1];
					var vendorGsinNo = resp.split("|")[2];

					$("#vendorIdId2").val(vendorId);

				}
			}
			 */
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
				$('table tbody tr td:contains("' + value + '")').each(
						function(index, item) {
							$(this).closest('tr').show();
							var reqamount = $(this).closest('tr').find(
									'.poAmnt').text();
							sum = sum + parseFloat(reqamount);
							$("#subtotalpoAmount1").text(sum);
							$(".info").show();

							var reqamount1 = $(this).closest('tr').find(
									'.Tillamnt').text();
							tquantity = tquantity + parseFloat(reqamount1);
							$("#subtotalpoAmount2").text(tquantity);
							$(".info").show();

							var reqamount2 = $(this).closest('tr').find(
									'.paidnmnt').text();
							PaidAmnt = PaidAmnt + parseFloat(reqamount2);
							$("#subtotalpoAmount3").text(PaidAmnt);
							$(".info").show();
						});

			};
		</script>
		<script>
			/* url sending to page start and hide and show iframe */
			function SetPage(url) {
				var z = document.getElementById("myframe").setAttribute("src",
						url);
				$('.loader-sumadhura').show();
			}
			$('#myframe').load(function() {
				$('.loader-sumadhura').hide();

			});

			$(".anchor-invoice").click(function() {
				$("#hide-show-iframe").hide();
				$(".cancel-iframe").show();
			});
			$("#cancel-btn").click(
					function() {
						$("#hide-show-iframe").show();
						$(".cancel-iframe").hide();
						var z = document.getElementById("myframe")
								.setAttribute("src", '');
					});
			/* url sending to page end and hide and show iframe */
			/* full image sending to page start and hide and show iframe */
			function SetPageImage(url) {
				var f = document.getElementById("myframe-1").setAttribute(
						"src", url);
				$('.loader-sumadhura').show();
			}
			$('#myframe-1').load(function() {
				$('.loader-sumadhura').hide();

			});

			$(".ponumber-anchor").click(function() {
				$("#hide-show-iframe").hide();
				$(".cancel-image-iframe").show();

			});
			$("#cancel-btn-1").click(
					function() {
						$("#hide-show-iframe").show();
						$(".cancel-image-iframe").hide();
						var f = document.getElementById("myframe-1")
								.setAttribute("src", '');
					});
			/* full image sending to page end and hide and show iframe */
			
			/* for sidemodule highlight */
			var referrer="beforeDownloadExcel.spring";
			$SIDEBAR_MENU.find('a').filter(function () {
			var urlArray=this.href.split( '/' );
			for(var i=0;i<urlArray.length;i++){
			if(urlArray[i]==referrer) {
			return this.href;
			}
			}
			}).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');

		</script>
</body>
</html>
