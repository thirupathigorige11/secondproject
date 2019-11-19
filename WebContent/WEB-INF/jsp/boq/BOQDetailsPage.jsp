<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import = "java.util.ResourceBundle" %>
	<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%
	
ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");
String serialNumber = resource.getString("label.serialNumber");
String majorHead = resource.getString("label.Major_head");
String minorHead = resource.getString("label.Minor_head");
String work_desc = resource.getString("label.work_desc");
String measurement = resource.getString("label.measurement");
String scope_of_work = resource.getString("label.scope_of_work");

String price = resource.getString("label.BoqPrice");
String quantity = resource.getString("label.Quantity");
String block = resource.getString("label.Block");
String floor = resource.getString("label.floor");
String flatNumber= resource.getString("label.flat");
String remarks = resource.getString("label.remarks");
String actions = resource.getString("label.actions");
String tableHead = resource.getString("label.indentIssueTableHead");
%>
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
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
<jsp:include page="../CacheClear.jsp" />  

<link type="text/css" href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" rel="stylesheet" />
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="js/inventory.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet">
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet"> 

<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">

<style>

.mrg-bottom{margin-bottom:10px;}
 table.dataTable {border-collapse:collapse !important;}
 .table>thead:first-child>tr:first-child>th {border-bottom:0 !important;}
 .bckBoqbtn{right:35px;top:100px;position:absolute;}
 @media only screen and (min-width:320px)and (max-width:767px){
 .bckBoqbtn{right:35px;top:217px;position:absolute;}
 }
 @media only screen and (min-width:768px)and (max-width:1023px){
 .bckBoqbtn{right:35px;top:158px;position:absolute;}
 }
 .table>tbody+tbody {border-top: 1px solid #000;}
 .table-fixed-header{border:1px solid #000;}
.table-fixed-header thead tr th{border: 1px solid #000;background-color:#ccc;}
.table-fixed-header tbody tr td{border:1px solid #000 !important;}
.dataTables_wrapper.no-footer .dataTables_scrollBody{border-bottom:0px !important;}
.table>thead:first-child>tr:first-child>th {border-bottom:0;}
 .custom-combobox {
	position: relative;
	display: inline-block;
}

.custom-combobox-toggle {
	position: absolute;
	top: 0;
	bottom: 0;
	margin-left: -1px;
	padding: 0;
	/* support: IE7 */
	*height: 1.7em;
	*top: 0.1em;
}

.custom-combobox-input {
	margin: 0;
	padding: 0.3em;
	width:100%;
}

#dropdown {
	width: 88%;
	padding: 3px;
	border-color: rgb(169, 169, 169);
} 
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
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">BOQ Details</li>
					</ol>
					<!-- <a href="viewMyBOQ.spring" class="bckBoqbtn btn btn-warning btn-xs"><i class="fa fa-chevron-left" aria-hidden="true"></i> Back</a> -->
				</div>
				<!-- Loader  -->
			          <div class="loader-sumadhura" style="display: none;z-index:999;">
							<div class="lds-facebook">
								<div></div><div></div><div></div><div></div><div></div><div></div>
							</div>
							<div id="loadingMessage">Loading...</div>
			          </div>
				<!-- Loader -->
				
               <!--  <div class="col-md-12 mrg-bottom"><a href="viewMyBOQ.spring" class="anchor-class">Back</a></div> -->
           <center><span><font color=green size=4 face="verdana">${responseMessage}</font></span></center>
              <center><span><font color=red size=4 face="verdana">${responseMessage1}</font></span></center>
			
				<div class="">
				<!--  container1 -->
				
				  <div class="col-md-12 border-indent">
	 	             <div class="col-md-4">
	 	              <div class="form-group">
			            <label class="control-label col-md-6">BOQ Number :</label>
			        <div class="col-md-6" >
			            <p><strong>${ObjBOQDetails.strBOQNo}</strong></p>
				        <%-- <input  id="tempBOQNo" name="tempBOQNo"  readonly="readonly" value="${boqMainList.intTempBOQNo}" class="form-control"/>
			            <input  id="tempBOQNo" name="tempBOQNo"  readonly="readonly" value="${ObjBOQDetails.strBOQNo}" class="form-control"/>  --%>
			          </div> 
			      </div>
	 	             </div>
			       <div class="col-md-4">
			        <div class="form-group">
			            <label class="control-label col-md-6">Version Number :</label>
			       <div class="col-md-6" >
			             <p><strong>${ObjBOQDetails.strVersionNo}</strong></p>				            
			          </div> 
			      </div>
			       </div>
			       <div class="col-md-4">
			        <div class="form-group">
			            <label class="control-label col-md-6">Creation Date :</label>
			        <div class="col-md-6" >
			               <p><strong>${ObjBOQDetails.strBOQCreationDate}</strong></p>
				         
			          </div> 
			      </div>
			       </div>
			        <div class="col-md-4">
	 	              <div class="form-group">
			            <label class="control-label col-md-6">Type of Work :</label>
			            <div class="col-md-6">
			              <p><strong>${ObjBOQDetails.typeOfWork}</strong></p>
			            </div>
			      </div>
	 	             </div>
	 	              <div class="col-md-4">
	 	              <div class="form-group">
			            <label class="control-label col-md-6">BOQ Total :</label>
			            <div class="col-md-6">
			              <p><strong>${ObjBOQDetails.boqTotalAmount}</strong></p>
			            </div>
			      </div>
			      
	 	             </div>
	 	             <div class="col-md-4">
		 	              <div class="form-group">
					            <label class="control-label col-md-6">BOQ Material Amount :</label>
					            <div class="col-md-6">
					              <p><strong>${ObjBOQDetails.boqMaterialAmount}</strong></p>
					            </div>
				         </div>
	 	             </div>
	 	             <div class="col-md-4">
		 	              <div class="form-group">
					            <label class="control-label col-md-6">BOQ Labor Amount :</label>
					            <div class="col-md-6">
					              <p><strong>${ObjBOQDetails.boqLaborAmount}</strong></p>
					            </div>
				         </div>
	 	             </div>
	 	             <c:if test="${not showCompleteBOQ}">
	 	              <div class="col-md-4">
	 	              <div class="form-group">
			            <label class="control-label col-md-6">Selected Work Total :</label>
			            <div class="col-md-6">
			              <p><strong>${ObjBOQDetails.selectedWorkTotal}</strong></p>
			            </div>
			      </div>
	 	             </div>
	 	             </c:if>
			      
			      
			      
	 	      </div>
	 	      
	 	        <!-- DropDown Options - start -->
				<div class="col-md-12 border-inwards-box">
				  	<form action="getBOQDetails.spring" onsubmit="return formValidator()"
						class="horizantal-form" method="post" >
					<!-- <div class="prodt_tbl col-xs-12 col-md-12"> -->
					<div class="col-md-4">
						<div class="form-group">
						<label class="col-md-6">Major Head :</label>
						<div class="col-xs-12 col-md-6">
													<%
											List<Map<String, Object>> totalProductList = (List<Map<String, Object>>) request
																									.getAttribute("totalProductsList");
																							if (totalProductList.size() > 0) {
																								String strProductName = "";
																								String strProductId = "";
										%> <select id="combobox_Product" name="combobox_Product">
											<option value=""></option>
											<%
												for (Map productList : totalProductList) {
																												strProductId = productList.get("WO_MAJORHEAD_ID") == null ? ""
																														: productList.get("WO_MAJORHEAD_ID").toString();
																												strProductName = productList.get("WO_MAJORHEAD_DESC") == null ? "" : productList.get("WO_MAJORHEAD_DESC").toString();
											%>

											<option value="<%=strProductId + "@@" + strProductName%>"><%=strProductName%></option>
											<%
												}
																										}
											%>
									</select>
						</div>
						</div>
						</div>
						<div class="col-md-4">
						<div class="form-group">
							<label class="col-md-6" style="font-size:14px;">Minor Head :</label>
							<div class="col-md-6"><select id="combobox_SubProduct"  name="combobox_SubProduct">
									<option value=""></option>
								</select></div>
								
							
						</div>
						</div>
						<div class="col-md-4">
						<div class="form-group">
							<label class="col-xs-12 col-md-5" style="">Work Description :</label>
							<div class="col-xs-12 col-md-6">
								<select id="combobox_ChildProduct" name="combobox_ChildProduct">
									<option value=""></option>

								</select>
							</div>
						</div>
						</div>
							<div class="col-md-12 text-center center-block Mrgtop10">
							<button type="submit" value="Submit" class="btnname btn btn-warning">Submit</button>
							</div>
							<input type="hidden" name="BOQNo" value="${ObjBOQDetails.strBOQNo}"/>
							<input type="hidden" name="BOQDate" value="${ObjBOQDetails.strBOQCreationDate}"/>
							<input type="hidden" name="BOQSeqNo" value="${ObjBOQDetails.strBOQSeqNo}"/>
							<input type="hidden" name="siteId" value="${ObjBOQDetails.strSiteId}"/>
							<input type="hidden" name="siteName" value="${ObjBOQDetails.strSiteName}"/>
							<input type="hidden" name="versionNo" value="${ObjBOQDetails.strVersionNo}"/>
							<input type="hidden" name="typeOfWork" value="${ObjBOQDetails.typeOfWork}"/>
							<input type="hidden" id="pageType" name="pageHighlightURL" value="${pageHighlightURL}">

						<!-- </div> -->

					</form>
				</div>
				<!-- DropDown Options - end -->
	 	      
				<div class="clearfix"></div>
			<div class="table-responsive">	
			<table id="tblnotification"	class="table table-striped table-bordered table-fixed-header display" cellspacing="0">
			<thead >
				<tr>
					<th><%= serialNumber %></th>
    				<th><%= majorHead %></th>
    				<th><%= minorHead %></th>
    				<th><%= work_desc %></th>
    				<th>UOM</th>    				
    				<th><%= scope_of_work %></th>
    				<th><%= quantity %></th>
    				<th><%= price %></th>
    				<th><%= block %></th>
    				<th><%= floor %></th>
    				<th><%= flatNumber  %></th>
    				<th>Total Amount</th>
    				<th>Record Type</th>
  				</tr>
  			</thead>
  			<tbody>
  			
  			 <c:forEach var="boqData" items="${requestScope['BOQDetailsList']}">
  			<tr>
  					<td>${boqData.intSerialNumber}</td>
  					<td>${boqData.strMajorHeadDesc }</td>
  					<td>${boqData.strMinorHeadDesc }</td>
  					<td>
  						<c:choose>
  						<c:when test = "${boqData.recordType == 'MATERIAL' }">
  							<a class="anchor-class"href="getBOQMaterialDetails.spring?BOQSeqNo=${ObjBOQDetails.strBOQSeqNo}&workAreaId=${boqData.workAreaId}&pageHighlightURL=${pageHighlightURL}"  title="Click on the product name to see the details">${boqData.strWorkDescription }</a>
  						</c:when>
  						<c:otherwise>
  							${boqData.strWorkDescription }
  						</c:otherwise>
  						</c:choose>
  					</td>
  					<td>${boqData.strMeasurementName }</td>
  					<td>${boqData.strScopeOfWork}</td>
  					<td class="Areacls">${boqData.strArea }</td>
  					<td>${boqData.doubleLaborRatePerUnit }</td>
  					<td>${boqData.strBlock }</td>
  					<td>${boqData.strFloor }</td>
  					<td>${boqData.strFlat }</td>
  					<td id="totalAmount" class="totalAmountCls">${boqData.singleWorkCost }</td>
  					<td>${boqData.recordType }</td>
  					
  			</tr>
  			</c:forEach>		
			</tbody>
			<tbody>
			     <tr class="info">
					 <td colspan="6" class="text-right subTtl">SUB TOTAL:</td>
					 <td id="qtyTotal"></td>
					 <td></td>
					 <td></td>
					 <td></td>
					 <td></td>
					 <td class="total text-center subTtl"></td>
					 <td></td>
				 </tr>
			 </tbody>
			</table>
			
				
			<div class="col-md-12 text-center center-block"><a href="getBOQDetailsSelection.spring?BOQSeqNo=${ObjBOQDetails.strBOQSeqNo}&siteId=${ObjBOQDetails.strSiteId}&pageHighlightURL=${pageHighlightURL}"  class="btn btn-warning btn-bottom">Back To BOQ Main Page</a>
				</div>
			</div>
           </div>
				<!-- /page content -->
			</div>
		</div>
	</div>

	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>
	<!-- Bootstrap -->
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/jquery-ui.js"></script>
	<script type="text/javascript" src="js/BOQJS/SelectBOQWork.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
    <script src="js/dataTables.bootstrap.min.js"></script>
     <script src="js/sidebar-resp.js"></script>
	<script>

		$(document).ready(
				
				function() {
					$(".up_down").click(
							function() {
								$(this).find('span').toggleClass(
										'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass(
										'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
				        "scrollY": "500px",
				        "scrollX": "3000px",
				        "scrollCollapse": true,
				        "paging": true
				    });
				});

		$(document).ready(function(){
			  $('.SubProduct1').keyup(function(){
			    $(this).attr('title',$(this).val());
			  });
			  $('.btn-bottom').click(function(){
				  $('.loader-sumadhura').show();
			  });
			});
	</script>
	<script>
	var showHide=window.location.href.split("&&show=")[1];
        $(document).ready(function(){ debugger;
           	   $(".info td").show();
           	   //default page load calculation
		 	   var val = $('#tblnotification').find('tbody').find('tr');
		       var tAmount = 0;
		       var AreaAmount=0;
			   jQuery.each(val,function(index,item){debugger;
			     tAmount = tAmount + (parseFloat(jQuery(item).find('.totalAmountCls').text().replace(/,/g,'') || 0));
			     AreaAmount = AreaAmount + (parseFloat(jQuery(item).find('.Areacls').text().replace(/,/g,'') || 0));
			   });
			  $(".total").text(tAmount.toFixed(2)); //formatNumber(tAmount)
			  if(showHide=="true"){
				  $("#qtyTotal").text(AreaAmount.toFixed(2));
			  }
			 
			/*   function formatNumber (num) {
				    return num.toString().replace(/(\d)(?=(\d{3})+(?!\d))/g, "$1,")
				}  */
     	 	$(document).on("keyup", ".dataTables_wrapper .dataTables_filter input, .paginate_button ",function(){
     	 		var val = $('#tblnotification').find('tbody').find('tr');
     			var tAmount = 0;
     			var AreaAmount=0;
     		    jQuery.each(val,function(index,item){
     		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.totalAmountCls').text().replace(/,/g,'') || 0));
     		    	AreaAmount = AreaAmount + (parseFloat(jQuery(item).find('.Areacls').text().replace(/,/g,'') || 0));
     		    });
     		   if(showHide=="true"){
 				  $("#qtyTotal").text(AreaAmount.toFixed(2));
 			  }
     			$(".total").text(tAmount.toFixed(2));
     		});
     		$(document).on("click", " .paginate_button",function(){
 		 		debugger;
 		 		 var val = $('#tblnotification').find('tbody').find('tr');
 				 var tAmount = 0;
 				var AreaAmount=0;
 			    jQuery.each(val,function(index,item){
 			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.totalAmountCls').text().replace(/,/g,'') || 0));
 			    	AreaAmount = AreaAmount + (parseFloat(jQuery(item).find('.Areacls').text().replace(/,/g,'') || 0));
 			    });
 			   if(showHide=="true"){
 				  $("#qtyTotal").text(AreaAmount.toFixed(2));
 			  }
 				$(".total").text(tAmount.toFixed(2));
 			});
     		$(document).on("change", " .dataTables_length",function(){
			 		var val = $('#tblnotification').find('tbody').find('tr');
					var tAmount = 0;
					var AreaAmount=0;
				    jQuery.each(val,function(index,item){
				    	tAmount = tAmount + (parseFloat(jQuery(item).find('.totalAmountCls').text().replace(/,/g,'') || 0));
				    	AreaAmount = AreaAmount + (parseFloat(jQuery(item).find('.Areacls').text().replace(/,/g,'') || 0));
				    });
				    if(showHide=="true"){
						  $("#qtyTotal").text(AreaAmount.toFixed(2));
					  }
					$(".total").text(tAmount.toFixed(2));
				});
     	});
        
      //this code for to active the side menu 									
        var referrer=$("#pageType").val();debugger;
        if(referrer==''||referrer==null){referrer="empty";}
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
