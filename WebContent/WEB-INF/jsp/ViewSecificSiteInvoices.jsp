<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="js/inventory.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
  <style>
     .st-table thead tr th, .table-viewmypending thead tr th {  background-color: #ddd;border: 0px solid #000 !important;border-right: 1px solid #000 !important;}
     .form-control{height:36px;}
     .success,.error {text-align: center;font-size: 14px;}
     .
  .info td{border: 1px solid #000 !important; border-bottom: none !important;}
  table.dataTable {border-collapse:collapse !important;}
 </style> 
</head>
<body class="nav-md">
	<div class="container body">
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

			<div class="col-md-12">
			<div class="container border-inwards-box">
			<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 				
			<form class="form-inline"  action="getInvoiceReciveViewDts.spring" >      			
		          <div class="form-group">
				    <label for="date" class="control-label">From Date :</label>
				   </div>
				    <div class="form-group input-group">
				    <input type="text" id="ReqDateId1" class="form-control readonly-color" name="fromDate" value="${fromDate}" autocomplete="off" readonly="true">	
				    <label class="input-group-addon btn input-group-addon-border" for="ReqDateId1"><span class="fa fa-calendar"></span></label>	     	
			        </div>
			         <div class="form-group">
			        <label for="todate" class="control-label">To Date :</label>
			        </div>
			         <div class="form-group input-group">
			        <input type="text" id="ReqDateId2" class="form-control readonly-color"  name="toDate" value="${toDate}" autocomplete="off" readonly="true">
		  	        <label class="input-group-addon btn input-group-addon-border" for="ReqDateId2"><span class="fa fa-calendar"></span></label>
		  	    </div>
              <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning" onclick="return validate();">Submit</button>
            </form>	
          </div>
	           <%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				%>
				
				<div class="table-responsive"> <!--  container1 -->
					<table id="tblnotification"	class="table table-new" cellspacing="0">
						<thead>
						   <tr>
							<th>S NO</th>
							<th>PO No</th>
							<th>PO Date</th>
							<th>Invoice No</th>
							<th>Invoice Date</th>
							<th>Vendor Name</th>
							<th>Total Amount</th>
							<th>Received Date</th>					
    				        <th>Received Time</th>	
				          </tr>
						</thead>
						<tbody>
						
						<%
							int	count=0;
				        %>
				
				       <c:forEach items="${indentIssueData}" var="element">  
						<tr>
						  <td><%=++count%></td>
			              <td><a href="getPoDetailsList.spring?poNumber=${element.poNo}&siteId=${element.siteId}" class="anchor-class" target="_blank">${element.poNo}</a></td>
						  <td>${element.poDate}</td>
			              <td><a href="getInvoiceDetails.spring?invoiceNumber=${element.requesterId}&vendorName=${element.vendorName}&vendorId=${element.vendorId}&IndentEntryId=${element.indentEntryId}&SiteId=${element.siteId}&invoiceDate=${element.strInvoiceDate}&indentType=${indentType}" style="text-decoration: underline;color: blue;" target="_blank">${element.requesterId}</a></td>
						  <td>${element.strInvoiceDate}</td>
						  <td>${element.vendorName}</td>
						  <td class="valor2 text-right" id="">${element.strTotalAmount}</td>
			              <td>${element.receivedDate}</td>
					      <td>${element.time}</td>
						</tr>
				      </c:forEach>
				    </tbody>
				   <tfoot>
			        <tr class="info">
			            <td colspan="6" class="text-right subTtl">SUB TOTAL:</td>
			            <td class="total text-right subTtl"></td>
			            <td></td>
			             <td></td>
			        </tr>
			      </tfoot>
				 </table>
				</div>
              <%
				   }
               %>
				<!-- /page content -->
				</div>
			</div>
		</div>
	</div>

	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>
	<script src="js/sidebar-resp.js"></script>
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
		$(document).ready(function() {
			$(".up_down").click(function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			});
			$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
			$(".anchor-class").click(function(ev){
				if(ev.ctrlKey==false && ev.shiftKey==false){
					//$(".loader-sumadhura").show();
				}
			});
		});
		
	/* 	$('#tblnotification').stacktable({myClass:'stacktable small-only'});
 */		/* *************** SUB Total calculations ********* */
		
		$(document).ready(function () {
			var val = $('#tblnotification').find('tbody').find('tr');
			var tAmount = 0;
			jQuery.each(val,function(index,item){
				tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') ));
			});
			var numberwithcomma=inrFormat(tAmount.toFixed(2));
			$(".total").text(numberwithcomma);
			
			$(document).on("click", " .pagination",function(){
				  var val = $('#tblnotification').find('tbody').find('tr');
				  var tAmount = 0;
				  jQuery.each(val,function(index,item){
				    tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				  });
			
				  var numberwithcomma=inrFormat(tAmount.toFixed(2));
					$(".total").text(numberwithcomma);
			});
			
			$(document).on("keyup", ".input-sm",function(){
				var val = $('#tblnotification').find('tbody').find('tr');
				var tAmount = 0;				
				jQuery.each(val,function(index,item){
					tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				});
				var numberwithcomma=inrFormat(tAmount.toFixed(2));
				$(".total").text(numberwithcomma);
			});
			
			$(document).on("change", " .dataTables_length",function(){
				var val = $('#tblnotification').find('tbody').find('tr');
				var tAmount = 0;				
				jQuery.each(val,function(index,item){
					tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				});
				var numberwithcomma=inrFormat(tAmount.toFixed(2));
				$(".total").text(numberwithcomma);
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
	 //checking from date and to date
	  function validate() {
		 var from = document.getElementById("ReqDateId1").value;
		 var to = document.getElementById("ReqDateId2").value;
		

		 if (from == "" && to == "" ) {
			alert("Please select From Date or To Date !");
			return false;
		 }
		 $(".loader-sumadhura").show();
	  }
 </script>
</body>
</html>
