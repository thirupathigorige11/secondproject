<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<jsp:include page="../CacheClear.jsp" />  
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<%-- <jsp:include page="CacheClear.jsp" /> --%>
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">
<link href="js/inventory.css" rel="stylesheet">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
<style>
<style>
.form-control{border:1px solid #000 !important;}
.success,.error {text-align: center;font-size: 14px;}
.selectAllchk span { font-size: 16px; font-weight: bold;}
.selectAllchk .selectAllchkinput {height: 22px;width: 22px;vertical-align: bottom;}
table.dataTable{border-collapse:collapse !important;}
.table>tbody+tbody { border-top: 1px solid #000;}
.form-control{border:1px solid #000;}
</style>


</head>
<body class="nav-md">
	<div class="container body">
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
				<div class="col-md-12">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Payment</a></li>
						<li class="breadcrumb-item active">View Transaction Details</li>
					</ol>
				</div>
				<!-- loader -->
				<div class="loader-ims" style="display: none;">
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
<div class="col-md-12">
<div class="invoice-payment-heading">
 <h4 class="text-center">TRANSACTION DETAILS</h4>
</div>
</div>
<div class="col-md-12">
<div class="container container-viewmypayment-box-1">
<center><label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> </center>
				
	<form class="form-horizontal"  action="viewMyTransactionDetails.spring" style="margin-top: 30px;"  >
	
	
   <div class="col-md-12">
     <div class="col-md-6">
     <div class="form-group">
	    <label for="date" class="col-md-6 col-xs-12 control-label">Payment Done From  :</label>
	     <div class="col-md-6 col-xs-12 input-group"> 
	       <input type="text" id="ReqDateId1" class="form-control readonly-color" name="fromDate" value="${fromDate}" autocomplete="off" readonly="true">
	         <label class="input-group-addon btn" for="ReqDateId1"> <span class="fa fa-calendar"></span> </label>
	        </div>
  	</div></div>
  	 <div class="col-md-6">
  	 <div class="form-group">
	    <label for="todate" class="col-md-6 col-xs-12 control-label">Payment Done To :</label>
	    <div class="col-md-6 col-xs-12 input-group"><input type="text" id="ReqDateId2" class="form-control readonly-color" name="toDate" value="${toDate}" autocomplete="off" readonly="true">
	        <label class="input-group-addon btn" for="ReqDateId2"> <span class="fa fa-calendar"></span> </label>
	      
	    </div>
  	</div></div>

  	<div class="col-md-6">
  	<div class="form-group">
	    <label for="invoiceNumber" class="col-md-6 col-xs-12 control-label">Invoice Number :</label>
	   <div class="col-md-6 col-xs-12">
	    <input type="text" id="invoiceNumber" class="form-control"  name="invoiceNumber" value="${invoiceNumber}" autocomplete="off">
	    </div>
  	</div>
  	</div>
  	<div class="col-md-6">
  	
  	 <div class="form-group">
	    <label for="poNumber" class="col-md-6 col-xs-12 control-label">PO Number :</label>
	   <div class="col-md-6 col-xs-12"><input type="text" id="poNumber" class="form-control"  name="poNumber" value="${poNumber}" autocomplete="off"></div>
  	</div>
  	</div>
  	  	 <div class="col-md-6">
  	<%--  <div class="form-group">
  	  <label class="col-md-6 col-xs-12">Site :</label> <!-- removed class commlbl  -->
	  <div class="col-md-6 col-xs-12">
	  <%
											List<Map<String, Object>> totalSiteList = (List<Map<String, Object>>) request .getAttribute("allSitesList");
																				String strSiteId = "";
																				String strSiteName = "";
										%> <select id="dropdown_SiteId" name="siteIdAndName"
										class="custom-combobox form-control"><!-- /* indentavailselect*/ removed -->
											<option value="${site_id}@@${site_name}">${site_name}</option>
											<option value="">--None--</option>
											<%
												for (Map siteList : totalSiteList) {
																						strSiteId = siteList.get("SITE_ID") == null ? "" : siteList.get("SITE_ID").toString();
																						strSiteName = siteList.get("SITE_NAME") == null ? "" : siteList.get("SITE_NAME").toString();
											%>

											<option value="<%=strSiteId%>@@<%=strSiteName%>"><%=strSiteName%></option>
											<%
												}
											%>

									</select>
	  </div>
									
  	 									
  	 </div> --%>
  	</div>	
  		
  							
			<div class="col-md-12 text-center selectAllchk">
				<input type="checkbox" class="selectAllchkinput" id="selectAll" name="selectAll" value="" /><span>&nbsp;Select All</span>
				<input type="hidden" id="hiddenSelectAll" name="hiddenSelectAll" value="" />
			</div>								
			<div class="clearfix"></div>						

  	</div>
    <!-- <div class="form-group">   -->      
      <div class="col-md-12 col-xs-12 col-sm-12 text-center center-block Mrgtop10">
        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning media-style"  onclick="return validate();">Submit</button>
        <div style="display: none;">${displayErrMsg}</div>
      </div>
   <!--  </div> -->
  </form>
				
</div>
</div>
<!-- <div class="loader"></div> -->	
<div class="loader-purchase" style="display: none;">
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
				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				%>
				<div class="clearfix"></div>
				<div class="col-md-12">
				<div class="table-responsive Mrgtop20"> <!-- container1 -->
					<table id="tblnotification"	class="table table-new" cellspacing="0">
						<thead>

							<tr>
							<th>Payment Done Date</th>
							<th>Payment Details ID</th>		
		    				<th>Vendor Name</th>
		    				<th>Amount Paid</th>
		    				<th>UTR Cheque No</th>
		    				<th>Site/Department</th>
		    				<th>Invoice Number</th>
		    				<th>Invoice Date</th>
		    				<th>PO Number</th>
		    				<th>PO Date</th>
		    				<th>Remarks</th>
				            </tr>

						</thead>
						<tbody>
						<c:forEach items="${listOfPayments}" var="element">  
						<tr>
							<td>${element.strPaymentDate}</td>
						        <td>${element.intPaymentDetailsId}</td>
							<td>${element.strVendorName}</td>
							<td class="valor2">${element.paidAmount_WithCommas}</td>
							<td>${element.utrChequeNo}</td>
							<td>${element.strSiteName}</td>
							<td><a  target="_blank" href="getInvoiceDetails.spring?invoiceNumber=${element.strInvoiceNo}&vendorName=${element.strVendorName}&vendorId=${element.strVendorId}&IndentEntryId=${element.intIndentEntryId}&SiteId=${element.strSiteId}&invoiceDate=${element.strInvoiceDate}" style="text-decoration: underline;color: blue;">${element.strInvoiceNo}</a></td>
							<td>${element.strInvoiceDate}</td>
							<td><a  target="_blank" href="getPoDetailsList.spring?poNumber=${element.strPONo}&siteId=${element.strSiteId}" style="text-decoration: underline;color: blue;"> ${element.strPONo} </a></td>
							<td>${element.strPODate}</td>
							<td>${element.strRemarksForView}</td>
							
						</tr>
						</c:forEach>
				    </tbody>
		<tr class="info">
            <td colspan="3" class="text-right">SUB TOTAL:</td>
            <td class="total text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
            <td class="text-right"></td>
        </tr>
					</table>
				</div>
				</div>
           <%
				   }
           %>
				<!-- /page content -->
			</div>
		</div>
	</div>

	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/indentReceive.js" type="text/javascript"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script>
	
	<script>
	$(function() {
		  $("#ReqDateId1").datepicker({
			  dateFormat: 'dd-M-y',
			  maxDate: new Date(),
			  changeMonth: true,
		      changeYear: true,
		      onSelect: function(selected) {
	     	        $("#ReqDateId2").datepicker("option","minDate", selected)
	     	        }

		  });
		  $("#ReqDateId2").datepicker({
			  dateFormat: 'dd-M-y',
			  maxDate: new Date(),
			  changeMonth: true,
		      changeYear: true,
		      onSelect: function(selected) {
	            	$("#ReqDateId1").datepicker("option","maxDate", selected)
	            	        }

		  });
		  return false;
	});
		$(document).ready(
				
				function() {
					$(".up_down").click(
							function() {
								$(this).find('span').toggleClass(
										'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass(
										'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
				});

		
		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});
		
		/* *************** calculations for filter data***********************	 */
		

	/* *************** calculations for filter data***********************	 */
	$(document).ready(function () {
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tAmount = 0;
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
		    });
			 $(".total").text(inrFormat(tAmount.toFixed(2)));
		 	$(document).on("keyup", ".input-sm, .paginate_button ",function(){
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 $(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			    });
			 
				$(".total").text(inrFormat(tAmount.toFixed(2)));
			});
		 	$(document).on("click", " .pagination",function(){
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 $(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			    });
			 
				$(".total").text(inrFormat(tAmount.toFixed(2)));
			});
		 	$(document).on("change", " .dataTables_length",function(){
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 $(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			    });			 
				$(".total").text(inrFormat(tAmount.toFixed(2)));
			});
		 	
		 	
		});
		//converting number with comma
		function inrFormat(nStr) { // nStr is the input string
		     nStr += '';
		     x = nStr.split('.');
		     x1 = x[0];
		     x2 = x.length > 1 ? '.' + x[1] : '';
		     var rgx = /(\d+)(\d{3})/;
		     var z = 0;
		     var len = String(x1).length;
		     var num = parseInt((len/2)-1);
		 
		      while (rgx.test(x1)){
		        if(z > 0){
		          x1 = x1.replace(rgx, '$1' + ',' + '$2');
		        }
		        else{
		          x1 = x1.replace(rgx, '$1' + ',' + '$2');
		          rgx = /(\d+)(\d{2})/;
		        }
		        z++;
		        num--;
		        if(num == 0){
		          break;
		        }
		      }
		     return x1 + x2;
		 } 

		
	</script>
<!-- script for loader start-->

<script type="text/javascript">

function validate() {
	

	var PaymentDoneFrom=$("#ReqDateId1").val();
    var PaymentDoneTo=$("#ReqDateId2").val();
    var InvoiceNumber=$("#invoiceNumber").val();
    var PONumber=$("#poNumber").val();
    var Site =$("#dropdown_SiteId").val();
	var SelectAll =$("#selectAll").prop("checked"); 
    
    $("#hiddenSelectAll").val(SelectAll);
    //trim method 
    var PaymentDoneFromdate=PaymentDoneFrom.trim();
    var PaymentDoneTodate=PaymentDoneTo.trim();
    var InvoiceNumberId=InvoiceNumber.trim();
    var PONumberId=PONumber.trim();
  
	if (!PaymentDoneFromdate && !PaymentDoneTodate && !InvoiceNumberId && !PONumberId && SelectAll==false ) {
		alert("Atleast one filed is required.");
		return false;
	}
	else{
		$('.loader-ims').show();
		return true;
	}

}
</script>


<!-- script for loader end -->
</body>
</html>
