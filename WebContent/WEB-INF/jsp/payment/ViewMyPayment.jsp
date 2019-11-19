<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

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
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="js/inventory.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
.table>tbody+tbody {border-top:0px;}
table.dataTable {border-collapse:collapse !important};
.success,.error {text-align: center;font-size: 14px;}
.form-control {border:1px solid #000;}
.border-class{border: 2px solid #ccc;}
.selectAllchk span { font-size: 16px; font-weight: bold;}
.selectAllchk .selectAllchkinput {height: 22px;width: 22px;vertical-align: bottom;}
</style>

<script type="text/javascript">


function validate() {
	

	var PaymentInitiatedFrom=$("#ReqDateId1").val();
    var PaymentInitiatedTo =$("#ReqDateId2").val();
    var InvoiceNumber=$("#invoiceNumber").val();
    var PONumber=$("#poNumber").val();
    var SelectAll =$("#selectAll").prop("checked"); 
    
    $("#hiddenSelectAll").val(SelectAll);
    
	
    //trim method 
    var PaymentInitiatedFromdate=PaymentInitiatedFrom.trim();
    var PaymentInitiatedTodate=PaymentInitiatedTo.trim();
    var InvoiceNumberId=InvoiceNumber.trim();
    var PONumberId=PONumber.trim();
  
	if (!PaymentInitiatedFromdate && !PaymentInitiatedTodate && !InvoiceNumberId && !PONumberId && SelectAll==false) {
		alert("Atleast one filed is required.");
		return false;
	}
	else{
		$(".loader-purchase").show();
		return true;
	}

}
</script>
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
								<li class="breadcrumb-item active">View My Payment Request Status</li>
							</ol>
						</div>
						<div class="col-md-12">
							<div class="invoice-payment-heading">
							 <h4 class="text-center">PAYMENT REQUEST STATUS</h4>
							</div>
						</div>
						<div class="col-md-12">
						  <div class="container container-viewmypayment-box-1"> 
						     <label class="success text-center center-block"><c:out value="${requestScope['succMessage']}"></c:out> </label> 										
							 <form class="form-horizontal"  action="viewMyPayment.spring" style="margin-top: 30px;" >
							    <div class="col-md-12">
								     <div class="col-md-6 Mrgtop10">
									       <div class="form-group">
											    <label for="date" class="col-md-6">Payment Initiated From :</label>
											    <div class="col-md-6 input-group"> <input type="text"  class="form-control readonly-color" id="ReqDateId1"  name="fromDate" value="${fromDate}" autocomplete="off" readonly="true">
											    <label class="input-group-addon btn" for="ReqDateId1"> <span class="fa fa-calendar"></span> </label>
											    </div>
									  	  </div>
								     </div>
								  	 <div class="col-md-6 Mrgtop10">
									  	 <div class="form-group">
										    <label for="todate" class="col-md-6">Payment Initiated To  :</label>
										    <div class="col-md-6 input-group"> <input type="text" class="form-control readonly-color" id="ReqDateId2"  name="toDate" value="${toDate}" autocomplete="off" readonly="true">
									  	     <label class="input-group-addon btn" for="ReqDateId2"> <span class="fa fa-calendar"></span></label>
									  	    </div>
									  	  </div>
								  	</div>
								  	<div class="col-md-6">
									  	<div class="form-group">
										    <label for="invoiceNumber" class="col-md-6">Invoice Number :</label>
										   <div class="col-md-6"> <input type="text"class="form-control" id="invoiceNumber"  name="invoiceNumber" value="${invoiceNumber}" autocomplete="off"></div>
									  	</div>
								  	</div>
								  	<div class="col-md-6">
									  	<div class="form-group">
										    <label for="poNumber" class="col-md-6 ">PO Number :</label>
										   <div class="col-md-6"> <input type="text" id="poNumber" class="form-control "  name="poNumber" value="${poNumber}" autocomplete="off"></div>
									  	</div>
								  	</div>
							  	   <div class="col-md-12 text-center selectAllchk">
										  <input type="checkbox" class="selectAllchkinput" id="selectAll" name="selectAll" value="" /><span>&nbsp;Select All</span>
										  <input type="hidden" id="hiddenSelectAll" name="hiddenSelectAll" value="" />																
								   </div>
								   <div class="form-group">        
								      <div class="col-md-12 col-sm-12 col-xs-12 text-center center-block">
								        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning Mrgtop10"  onclick="return validate();">Submit</button>
								        <div style="display:none;">${displayErrMsg}</div>
								      </div>
								   </div>
							    </div>
						  </form>										
						</div>
					</div>
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
	              <!-- <div class="loader"></div>	 -->	
				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   
				   
				if(isShowGrid.equals("true")){
				%>
				<div class="clearfix"></div>
				<div class="col-md-12">
				 <div class="table-responsive" style="padding-top:20px;"><!-- container -->
					<table id="tblnotification"	class="table table-new" cellspacing="0">
						<thead>
							<tr> <!-- class="tblheaderall" -->
								<th>Created Date</th>
								<th>Payment Details ID</th>		
			    				<th>Vendor Name</th>
			    				<th>Invoice Number</th>
			    				<th>Invoice Date</th>
			    				<th>PO Number</th>
			    				<th>Amount to be Released</th>
			    				<th>Transaction Amount</th>
			    				<th>Pending Dept</th>
			    				<th>Pending Emp</th>
			    				<th>Remarks</th>
			    				<%if(Boolean.valueOf(request.getAttribute("isAccounts").toString())){ %>
			    				<th>Site/Department</th>
			    				<%} %>
			    				<th>Status</th>
			    				<th>Payment Req Date</th>
			    				<th>Acc Dept Received Date</th>
				            </tr>
						</thead>
						<tbody>
				        <c:forEach items="${listOfPayments}" var="element">  
							<tr>
								<td class="myborder border-class" >${element.strCreatedDate}</td>
							    <td class="myborder border-class">${element.intPaymentDetailsId}</td>
								<td class="myborder border-class">${element.strVendorName}</td>
								<td class="myborder border-class"><a  target="_blank" href="getInvoiceDetails.spring?invoiceNumber=${element.strInvoiceNo}&vendorName=${element.strVendorName}&vendorId=${element.strVendorId}&IndentEntryId=${element.intIndentEntryId}&SiteId=${element.strSiteId}&invoiceDate=${element.strInvoiceDate}" style="text-decoration: underline;color: blue;">${element.strInvoiceNo}</a></td>
								<td class="myborder border-class">${element.strInvoiceDate}</td>
								<td class="myborder border-class"><a  target="_blank" href="getPoDetailsList.spring?poNumber=${element.strPONo}&siteId=${element.strSiteId}" style="text-decoration: underline;color: blue;"  > ${element.strPONo} </a></td>
								<td class="myborder AmntToBeReleased border-class">${element.amountToBeReleased_WithCommas}</td>
								<td class="myborder  border-class">${element.transactionAmount_WithCommas}</td>
								<td class="myborder border-class">${element.strPendingDeptName}</td>
								<td class="myborder border-class">${element.strPendingEmpName}</td>
								<td class="myborder border-class">${element.strRemarksForView}</td>
								<%if(Boolean.valueOf(request.getAttribute("isAccounts").toString())){ %>
			    				<td class="myborder border-class">${element.strSiteName}</td>
								<%} %>
								<td class="myborder border-class">${element.status}</td>
								<td class="myborder border-class">${element.strPaymentReqDate}</td>
								<td class="myborder border-class">${element.strPaymentRequestReceivedDate}</td>
								
							</tr>
				      </c:forEach>
				</tbody>
				<tr class="info">
		            <td colspan="5" class="text-right">SUB TOTAL:</td>		            
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
					$(".up_down").click( function() {
								$(this).find('span').toggleClass( 'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass( 'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});});
		//$('#tblnotification').stacktable({myClass:'stacktable small-only'});
		
		/* *************** calculations for filter data***********************	 */
		

	/* *************** calculations for filter data***********************	 */
		$(document).ready(function () {
			 var val = $('#tblnotification').find('tbody').find('tr');
			 var tAmount = 0;
		    jQuery.each(val,function(index,item){
		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.AmntToBeReleased').text().replace(/,/g,'') || 0));
		    });
			 $(".total").text(inrFormat(tAmount.toFixed(2)));
		 	$(document).on("keyup", ".input-sm, .paginate_button ",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 $(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.AmntToBeReleased').text().replace(/,/g,'') || 0));
			    });
			 
				$(".total").text(inrFormat(tAmount.toFixed(2)));
			});
		 	$(document).on("click", " .pagination",function(){
		 		/* $(".subTtl").show(); */
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 $(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.AmntToBeReleased').text().replace(/,/g,'') || 0));
			    });
			 
				$(".total").text(inrFormat(tAmount.toFixed(2)));
			});
		 	$(document).on("change", " .dataTables_length",function(){
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#tblnotification').find('tbody').find('tr');
				 var tAmount = 0;
				 $(".total").text(0);
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.AmntToBeReleased').text().replace(/,/g,'') || 0));
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

</body>
</html>
