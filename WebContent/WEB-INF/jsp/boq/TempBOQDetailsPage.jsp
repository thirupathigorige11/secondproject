<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.*"%>
<%@page import = "java.util.ResourceBundle" %>

<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<%
	//Loading Indent Issue Table Column Headers/Labels - Start
	ResourceBundle resource = (ResourceBundle)request.getAttribute("columnHeadersMap");

/* label.M.head=Major Head 
label.Minor_head=Minor Head
label.work_desc=Work Desc
label.scope_of_work=Scope Of Work
label.B.Price=Price
label.Quantity=Quantity
label.Block =Block
label.floor=Floor
label.flat=Flat */

/* String colon = resource.getString("label.colon") */;
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
String areaPricePerUnit=resource.getString("label.areaPricePerUnit");
%>

<html>
<head>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
	    <jsp:include page="../CacheClear.jsp" />  
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<!-- <link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />  -->
		<link type="text/css" href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" rel="stylesheet" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">		
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/loader.css" rel="stylesheet" type="text/css">
		<link href="css/style.css" rel="stylesheet" type="text/css">
        <link href="css/custom.css" rel="stylesheet" type="text/css">
 		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
 		<link href="css/topbarres.css" rel="stylesheet">
 		
 		<!-- <link href="js/inventory.css" rel="stylesheet" type="text/css" /> -->
    <title>BOQ For Approve</title>
    <style>
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

<script type="text/javascript">
(function( $ ) {
    $.widget( "custom.combobox", {
      _create: function() {
        this.wrapper = $( "<span>" )
          .addClass( "custom-combobox" )
          .insertAfter( this.element );
 
        this.element.hide();
        this._createAutocomplete();
        this._createShowAllButton();
      },
 
      _createAutocomplete: function() {
        var selected = this.element.children( ":selected" ),
          value = selected.val() ? selected.text() : "";
 
        this.input = $( "<input>" )
          .appendTo( this.wrapper )
          .val( value )
          .attr( "title", "" )        
          
          .attr("id", this.element[0].name)
          //Enable below line to get input box background color - Start
          //.addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
          //Enable below line to get input box background color - End
          .autocomplete({
            delay: 0,
            minLength: 0,
            source: $.proxy( this, "_source" )
          })
          .tooltip({
            tooltipClass: "ui-state-highlight"
          });
 
        this._on( this.input, {
          autocompleteselect: function( event, ui ) {
            ui.item.option.selected = true;
            //alert(this.element[0].name);
            //alert(ui.item.option.value);
            //alert(ui.item);
            
            //alert(this.input.id);
            //var id = this.input.text();
            //alert(this.input);
            
            //var count = 1;
            //var isProdSel = false;
            
            var prodId = "";
            var prodName = "";
            
            prodId = ui.item.option.value;
            prodName = ui.item.value;
            
            var ele = this.element[0].name;
            //alert(ele);
            
            //Removing numbers from the header names
            var str1 = ele.replace(/[0-9]/g, '');
            //alert("After removing numbers = "+str1);
            
	        var productColumn =  "<%= majorHead %>";
		    productColumn = formatColumns(productColumn);
		    //alert(productColumn);
		  	 
		  	var subProductColumn =  "<%= minorHead %>";
		  	subProductColumn = formatColumns(subProductColumn);
		 	//alert(subProductColumn);
		     
		 	var childProductColumn =  "<%= work_desc %>";
		 	childProductColumn = formatColumns(childProductColumn);
			//alert(childProductColumn);
			
			var rowNum = ele.match(/\d+/g);
			//alert(rowNum);
			           debugger;
            if(str1 == productColumn) {
            	prodId = ui.item.option.value;
                prodName = ui.item.value;
                debugger;
                loadSubProds(prodId, rowNum);
            }            
            else if(str1 == subProductColumn) {
            	prodId = ui.item.option.value;
                prodName = ui.item.value;
                loadSubSubProducts(prodId, rowNum);
            }
            else if(str1 == childProductColumn) {
            	prodId = ui.item.option.value;
                prodName = ui.item.value;
                loadUnits(prodId, rowNum);
            }
            
            this._trigger( "select", event, {
              item: ui.item.option
            });
          },
          autocompletechange: "_removeIfInvalid"
        });
      },
      
      
 	//Enable below code to create Show All Button - Start
        _createShowAllButton: function() {
        var input = this.input,
          wasOpen = false;
 
        $( "<a>" )
          .attr( "tabIndex", -1 )
          .attr( "title", "Show All Items" )
          .tooltip()
          .appendTo( this.wrapper )
          .button({
            icons: {
              primary: "ui-icon-triangle-1-s"
            },
            text: false
          })
          .removeClass("ui-corner-all" )
          .addClass("custom-combobox-toggle ui-corner-right")
          .mousedown(function() {
            wasOpen = input.autocomplete( "widget" ).is( ":visible" );
          })
          .click(function() {
            input.focus();
 
            // Close if already visible
            if ( wasOpen ) {
              return;
            }
 
            // Pass empty string as value to search for, displaying all results
            input.autocomplete( "search", "" );
          });
      },  
    //Enable below code to create Show All Button - End 
 
      _source: function( request, response ) {
        var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
        response( this.element.children( "option" ).map(function() {
          var text = $( this ).text();
          if ( this.value && ( !request.term || matcher.test(text) ) )
            return {
              label: text,
              value: text,
              option: this
            };
        }) );
      },
 
      _removeIfInvalid: function( event, ui ) {
 
        // Selected an item, nothing to do
        if ( ui.item ) {
          return;
        }
 
        // Search for a match (case-insensitive)
        var value = this.input.val(),
          valueLowerCase = value.toLowerCase(),
          valid = false;
        this.element.children( "option" ).each(function() {
          if ( $( this ).text().toLowerCase() === valueLowerCase ) {
            this.selected = valid = true;
            return false;
          }
        });
 
        // Found a match, nothing to do
        if ( valid ) {
          return;
        }
        
        // Remove invalid value
        this.input
          .val( "" )
          .attr( "title", value + " didn't match any item" )
          .tooltip( "open" );
        this.element.val( "" );
        this._delay(function() {
          this.input.tooltip( "close" ).attr( "title", "" );
        }, 2500 );
        //this.input.autocomplete( "instance" ).term = "";
      }, 
      _destroy: function() {
        this.wrapper.remove();
        this.element.show();
      }
      
    });
  })( jQuery ); 



  
</script>

</head>

<body class="nav-md">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId {
			display : none;
		}
	
	</style>
</noscript>
<div class="container body" id="mainDivId">
			<div class="main_container" id="main_container">
				<div class="col-md-3 left_col" id="left_col">
					<div class="left_col scroll-view">
             
						<div class="clearfix"></div>

						<jsp:include page="./../SideMenu.jsp" />  
						
					</div>
					</div>
						<jsp:include page="./../TopMenu.jsp" />  
				
	
				<!-- page content -->
				<div class="right_col" role="main">
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">BOQ For Approval</li>
						</ol>
					</div>
					
					
					<div>
				<!-- loader -->
			    <!--  <div class="overlay_ims" ></div>
				 <div class="loader-ims" id="loaderId"> 
					<div class="lds-ims">
						<div></div><div></div><div></div><div></div><div></div><div></div></div>
					<div id="loadingimsMessage">Loading...</div>
				</div> -->
				<div class="overlay_ims" style="display:none;"></div>
					 <div class="loader-ims" id="loaderId" style="display:none;z-index:999999;"> 
						<div class="lds-ims">
							<div></div><div></div><div></div><div></div><div></div><div></div></div>
						<div id="loadingimsMessage">Loading...</div>
					</div>
					
 <div class="col-md-12">
		<form:form modelAttribute="BOQBean" id="boqForm" class="form-horizontal">
		<div class="border-form-indent">
		<span><font color=red size=4 face="verdana">${responseMessage}</font></span>
		<span style="color:red;">${noStock}</span><br/>
		 <c:forEach var="boqMainList" items="${requestScope['boqData']}">
	 	   <div class="col-md-6">
	 	    <div class="form-group">
			<label class="control-label col-md-6">Temp BOQ Number :</label>
			<div class="col-md-6" >
				<input  id="tempBOQNo" name="tempBOQNo"  readonly="readonly" value="${boqMainList.intTempBOQNo}" class="form-control"/>
			</div>
			</div>
	 	   </div>
	  		<div class="col-md-6">
	   		<div class="form-group">
				<label class="control-label col-md-6">Temp BOQ From :</label>
				<div class="col-md-6" >
				<input type="hidden" name="lowerLevelEmpId" value="${lowerLevelEmpId}">
				<input type="hidden" name="boqCreatedEmpId" value="${boqMainList.strTepBOQCreatedEmployeId}">
				<input  id="boqFrom" name="boqFrom" readonly="readonly" class="form-control" value="${lowerLevelEmpName}"/>
				</div>
			</div>
	  		</div>
			<div class="col-md-6">
			 <div class="form-group">
			<label class="control-label col-md-6">Temp BOQ TO :</label>
			<div class="col-md-6" >
			
			<input type="hidden" name="nextLevelApproverEmpId" value="${nextLevelApproveEmpId}">
			<input type="hidden" name="boqApproverEmpId" value="${boqMainList.strTempBOQApproveEmployeeId}">
				<input  id="approverEmpId" name="approverEmpId" readonly="readonly" class="form-control" value="${nextLevelApproveEmpName}"/>
				<%-- <input  id="approverEmpId" name="approverEmpId" readonly="readonly" class="form-control" value="${boqMainList.strBoqTo}"/> --%>
			</div>
		   </div> 
			</div>
			<div class="col-md-6">
	   		<div class="form-group">
				<label class="control-label col-md-6">Type of Work :</label>
				<div class="col-md-6" >
				<input  id="typeOfWork" name="typeOfWork" readonly="readonly" class="form-control" value="${boqMainList.typeOfWork}"/>
				</div>
			</div>
	  		</div>	
	  		<div class="col-md-6">
	   		<div class="form-group">
				<label class="control-label col-md-6">BOQ Material Amount :</label>
				<div class="col-md-6" >
				<input  id="boqTotal" name="boqTotal" readonly="readonly" class="form-control" value="${boqMainList.boqMaterialAmount}"/>
				</div>
			</div>
	  		</div>	
	  		<div class="col-md-6">
	   		<div class="form-group">
				<label class="control-label col-md-6">BOQ Labor Amount :</label>
				<div class="col-md-6" >
				<input  id="boqTotal" name="boqTotal" readonly="readonly" class="form-control" value="${boqMainList.boqLaborAmount}"/>
				</div>
			</div>
	  		</div>
	  		<div class="col-md-6">
	   		<div class="form-group">
				<label class="control-label col-md-6">BOQ Total :</label>
				<div class="col-md-6" >
				<input  id="boqTotal" name="boqTotal" readonly="readonly" class="form-control" value="${boqMainList.boqTotalAmount}"/>
				</div>
			</div>
	  		</div>
	  		<c:if test="${not showCompleteBOQ}">
	 	             <div class="col-md-6">
	   			<div class="form-group">
				<label class="control-label col-md-6">Selected Work Total :</label>
				<div class="col-md-6" >
				<input  id="boqTotal" name="boqTotal" readonly="readonly" class="form-control" value="${boqMainList.selectedWorkTotal}"/>
				</div>
				</div>
	  			</div>
	 	     </c:if>	
		    <div class="col-md-6">
		     <div class="form-group">
			<label style="visibility:hidden" class="control-label col-md-6">Approver Emp Id :</label>
			<div class="col-md-6" >
				<input type="hidden"  id="approverEmpIdId"  readonly="true" class="form-control"/>
			</div>
			<input type="hidden"  name="SiteName"  value="${boqMainList.strSiteName}" />
			<input type="hidden"  name="siteId"  value="${boqMainList.strSiteId}" />
		    <input type="hidden" id="pageType" name="pageHighlightURL" value="${pageHighlightURL}">
		   </div> 
		    </div>
		    
		    </c:forEach>
	  	     </div>
	  	     
	  	     
	<!-- /*for combobox dropdown*/ -->
	 <div class="col-md-12 border-inwards-box Mrgtop10">	      
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
										%> <select id="combobox_Product" name="combobox_Product" class="form-control">
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
							<div class="col-md-6"><select class="form-control" id="combobox_SubProduct"  name="combobox_SubProduct">
									<option value=""></option>
								</select></div>
								
							
						</div>
						</div>
						<div class="col-md-4">
						<div class="form-group">
							<label class="col-xs-12 col-md-5" style="">Work Description :</label>
							<div class="col-xs-12 col-md-6">
								<select class="form-control" id="combobox_ChildProduct" name="combobox_ChildProduct">
									<option value=""></option>

								</select>
							</div>
						</div>
						</div>
						<div class="col-md-12 text-center center-block">
						 <!-- <button class="btn btn-warning">Submit</button> -->
						 <button type="button" onclick="return formValidator()" value="Submit" class="btn btn-warning">Submit</button>
						</div>
						<c:forEach var="boqMainList" items="${requestScope['boqData']}">
							<input type="hidden" name="boqType" value="${boqMainList.boqType}"/>
						 </c:forEach>
	</div>
	<!-- /*for combobox dropdown*/ -->
	<!-- table -->
	<!-- <div class="table-responsive Mrgtop10"> -->
		<table id="boqTableId" class="table table-striped table-bordered st-table stacktable large-only dataTable no-footer "><!-- tblprodindiss -->
		<thead >
				<tr>
					<th class="width-indent-th"><%= serialNumber %></th>
    				<th width=213px;><%= majorHead %></th>
    				<th width=213px;><%= minorHead %></th>
    				<th width=213px;><%= work_desc %></th>
    				<th width=213px;><%= measurement %></th>     				
    				<th width=213px;><%= scope_of_work %></th>
    				<th><%= price %></th>
    				<th  width=213px;class="w-70"><%= quantity %></th>
    				<th  width=213px;class="w-70"><%= block %></th>
    				<th  width=213px;class="w-70"><%= floor %></th>
    				<th  width=213px;class="w-70"><%= flatNumber  %></th>
    				<th>Total Amount</th>
    				<th>Record Type</th>
    			</tr>
  			</thead>
  			<tbody>
  			
  			 <c:forEach var="boqData" items="${requestScope['TempBoqWorkDeatils']}">
  			<tr>
  					<td>${boqData.intSerialNumber}</td>
  					<td>${boqData.strMajorHeadDesc }</td>
  					<td>${boqData.strMinorHeadDesc }</td>
  					<td>
  					<%-- ${boqData.strWorkDescription } --%>
  					<c:choose>
  						<c:when test = "${boqData.recordType == 'MATERIAL' }">
  							<a class="anchor-class"href="getTempBOQMaterialDetails.spring?tempBOQNo=${tempBOQNo}&workAreaId=${boqData.workAreaId}&siteId=${boqSiteId}&boqType=NORMAL&viewTempBoq=${viewTempBoq}&pageHighlightURL=${pageHighlightURL}"  title="Click on the product name to see the details">${boqData.strWorkDescription }</a>
  						</c:when>
  						<c:otherwise>
  							${boqData.strWorkDescription }
  						</c:otherwise>
  						</c:choose>
  					</td>
  					<td>${boqData.strMeasurementName }</td>
  					<td>${boqData.strScopeOfWork } </td><!-- -->
  					<td>${boqData.doubleLaborRatePerUnit }</td>
  					<td>${boqData.strArea }</td>
  					<td>${boqData.strBlock }</td>
  					<td>${boqData.strFloor }</td>
  					<td>${boqData.strFlat }</td>
  					<td id="totalAmount" class="totalAmountCls">${boqData.singleWorkCost }</td>
  					<td>${boqData.recordType }</td>
  					
  			</tr>
  			</c:forEach>

				
					</tbody>			
			</table>
			<!-- </div> -->
	<!-- table - end -->
	
	
			<div class="row"> <!-- tblindentissunote -->
				
				<div class="col-md-12">
				  <label class="control-label col-md-2" >Note: </label>
						<div class="col-md-4" style="    position: relative;margin-bottom: 10px;" >
						<input type="hidden" name="actualNote" value="${BOQLevelCommentsList}">
							<form:textarea path="" href="#" data-toggle="tooltip" title="${BOQLevelCommentsList}"   id="NoteId" name="Note" class="form-control" autocomplete="off" placeholder="${BOQLevelCommentsList}"></form:textarea>
						</div>
				</div>
	            <div class="col-md-12">
	               <h2 style="font-weight:bold;">Modification Details:</h2>	
				  <div class="modifydetails" >
				  <c:forEach var="indentCreationComments" items="${requestScope['materialEditCommentList']}" step="1" begin="0">	
	        	 	
	        	 	<div style="float:left;font-size: 15px; font-weight: bold; margin-top: 12px;" >		
	        	 	 <ul class="check_list" name="editComments" style="" >
	        	 	   <li class="Comment_content" style=""> ${indentCreationComments.materialEditComment}</li>
			         </ul>
			       </div>
				</c:forEach>
				</div>
	            </div>
				 
				
				
			</div>
			<c:if test="${ not viewTempBoq}">
					<div class="col-md-12 text-center center-block ">
					<input type="button" style="width: 133px;" class="btn btn-warning Mrgtop10" value="Approve" id="saveBtnId" onclick="saveRecords('SaveClicked')">
						<!-- <input type="button" style="width: 133px;" class="btn btn-warning Mrgtop10"value="Reject" id="saveBtnId" onclick="reject('SaveClicked')"> -->
				 <input type="button"  style="width: 133px;" class="btn btn-warning Mrgtop10" value="Reject"  onclick="reject('SaveClicked')" id="saveBtnId"/>
						
				</div>
				</c:if>
				<br><br><br><br>
				<c:forEach var="ObjBOQDetails" items="${requestScope['boqData']}">
				<a href="downloadBOQ.spring?tempBOQNo=${ObjBOQDetails.intTempBOQNo}&siteId=${ObjBOQDetails.strSiteId}&siteName=${ObjBOQDetails.strSiteName}&temporaryBOQ=true&pageHighlightURL=${pageHighlightURL}"  class="btn btn-warning" >DOWNLOAD BOQ</a>
				</c:forEach>	

			
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			
			
			  <input type="hidden" id="materialEditComment" name="materialEditComment" value="${requestScope['materialEditComment']}" />
				  
				 
				
		</form:form>
		<!-- Modal - start -->
<div id="myModal-pdf" class="modal fade" role="dialog">
  <div class="modal-dialog modal-lg-width">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center"><strong>Full Width Excel</strong></h4>
      </div>
      <div class="modal-body">
         <!-- <iframe src="Print Work Order.pdf"style="height:100%;width:100%;"></iframe> -->
		 <!-- <iframe  allow="fullscreen" style="height:800px;width:800px;"></iframe> -->
		 <embed src="data:application/vnd.ms-excel;base64,${requestScope['excel']}" style="height:500px;width:100%";>
      </div>
      <div class="modal-footer">
       <p class="text-center">
	     <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
	   </p>
      </div>
    </div>

  </div>
</div>
		<!-- Modal - end -->
	</div>
	
	</div>
	<!-- /page content -->        
</div>
</div>
</div>
	<!-- jQuery -->
	<script src="js/jquery.min.js"></script>	
	<script src="js/bootstrap.min.js"></script>
	<!-- Custom Theme Scripts -->
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/custom.js"></script>
	<script src="js/stacktable.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>	
	<script src="js/BOQJS/boq.js" type="text/javascript"></script>
	<script type="text/javascript" src="js/BOQJS/SelectBOQWork.js"></script>
	<script type="text/javascript" src="js/sidebar-resp.js"></script>

	
  <script>
		
			$(document).ready($(function() {

				$("#boqTableId").DataTable();
				/* 
				for(i=1;i<=100;i++){	
					$("#combobox"+i).combobox();    
					$( "#toggle").click(function() { $( "#combobox"+i).toggle();  });
					$( "#comboboxsubProd"+i).combobox();
					$("#comboboxsubSubProd"+i).combobox(); 
					$("#UnitsOfMeasurementId"+i).combobox(); 
					} */
					    
					  }),
					$(function() {	
				
			
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				for(i=1;i<=100;i++){  /* Here 100 is statically given. get the value dynamically from database.*/
				$('.btn-visibilty'+i).closest('td').find('input').attr('disabled', 'disabled');
			    $('.btn-visibilty'+i).closest('td').find('.custom-combobox-toggle').addClass('hide');
			    }
			}));
			$(document).ready(function(){
			    $('[data-toggle="tooltip"]').tooltip();   
			});
			$(document).ready(function(){
				  $('.commmntstooltip').keyup(function(){
				    $(this).attr('title',$(this).val());
				  });
				});
			
			$(document).ready(function(){
				  $('.form-control').keyup(function(){
				    $(this).attr('title',$(this).val());
				  });
				});
			
			//to ditroy loaders
			$(window).load(function () {
				 $(".overlay_ims").hide();	
				 $(".loader-ims").hide();
			});
			
			function formValidator() {debugger;
			var show=false;
			var product = document.getElementById('combobox_Product').value;
			var subProduct=document.getElementById('combobox_SubProduct').value;
			var childProduct=document.getElementById('combobox_ChildProduct').value;
			if (product == '' || product == null || product == '@@') {
				alert("please enter product name");
				return false;

			}
			if(product!=='' && subProduct!='' && childProduct.split("@@")[1]!=''){
				show=true;
			}
			
			document.getElementById("boqForm").action = "getTempBOQDetails.spring?&&show="+show;
			document.getElementById("boqForm").method = "POST";
			document.getElementById("boqForm").submit();
			
			
		}
			
			//this code for to active the side menu 									
			var referrer=$("#pageType").val();
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
