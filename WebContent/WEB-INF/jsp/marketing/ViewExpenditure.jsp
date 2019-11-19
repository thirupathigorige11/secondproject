<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<html>
<head>

  <!-- <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script> -->

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
		<link href="css/marketing/jquery.timepicker.min.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
        <!-- 		<link href="css/jquery.dataTables.min.css" rel="stylesheet" type="text/css"> -->
       <link rel="stylesheet" type="text/css" href="css/dataTables.bootstrap.min.css"/>

		<link type="text/css" href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" rel="stylesheet" />
		<link href="js/inventory.css" rel="stylesheet" type="text/css" />
		
		  <script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		
	 <style>
  div.dataTables_scrollBody table { border-top: none;  margin-top: -2px !important; margin-bottom: 0 !important;}
  div.dataTables_wrapper div.dataTables_paginate { margin: 7px 0px;white-space: nowrap;text-align: right;}
   div.dataTables_wrapper {width: 1800px;margin: 0 auto;}
 .table>tbody+tbody {border-top: 1px solid #000 !important;}
 .border-inwards-box {text-align:left !important;}	
 .input-group-addon{border:1px solid #ccc !important;}
 .form-control{height:34px;border:1px solid #ccc !important;} 
 .ui-state-default, .ui-widget-content .ui-state-default, .ui-widget-header .ui-state-default{border:1px solid #ccc !important;}
 .custom-combobox input{height:34px;width:86% !important;border:1px solid #ccc !important;}
 .chexkbox_siteall {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;margin-right: 5px !important;}
 .chexkbox_siteall1 {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;margin-right: 5px !important;}
 .chexkbox_site {top: 0;left: 0;height: 25px;width: 25px;background-color: #eee;float: left;}
 .checkbox_sitelabel {margin-top: 6px;margin-right: 15px;float: left;font-size: 15px;}
 .display_flex {display: inline-flex;word-break: break-word;}
 .breadcrumb {background: #eaeaea !important;}
 #btn-search{padding:6px 12px;width:100px;}
 table.dataTable{border-collapse: collapse !important;}
 label {text-align:left !important;}
.custom-combobox-toggle {position: absolute;top: 0;bottom: 0;margin-left: -1px;padding: 0;}
.custom-combobox-input {width: 80%;height: 30px;border-radius: 3px;border: 1px solid #ccc;padding: 5px;}
 #dropdown {width: 88%;padding: 3px;border-color: rgb(169, 169, 169);} 

 </style>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
</head>
<body class="nav-md" id="body-refresh">
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

						<jsp:include page="../SideMenu.jsp" />  
						
					</div>
			</div>
						<jsp:include page="../TopMenu.jsp" />  
				
	
				<!-- page content -->
			<div class="right_col" role="main">
					<div class="col-md-12">
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Marketing</a></li>
							<li class="breadcrumb-item active">View Expenditure</li>
						</ol>
			       </div>
			 <div>
			   <div class="loader-sumadhura" style="display: none;z-index:9999;">
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
		<h3 class="text-center"><span><font color=red size=4 face="verdana">${AvailableDateOrNot}</font></span></h3>	 
			<div class="col-md-12">
			 <div class="border-inwards-box">
				   <form:form modelAttribute="marketingExpenditureModelForm" class="form-horizontal" action="getAllViewOnlyExpenditures.spring" method="post">
				    <input type="hidden" id="referer" name="referer" value="ViewExpenditures">
				    <div class="col-md-12">
				     <div class="col-md-4">
				      <div class="form-group">
				     <label class="col-md-6 control-label">Invoice From Date :</label>
				     <div class="col-md-6 input-group">
				      <input type="text" class="form-control readonly-color" name="datepickerfrom" id="datepickerfrom" onchange="fromDateChange()" onkeypress="return false;" autocomplete="off" readonly/>
				      <label class="input-group-addon btn datepickerfrom" for="datepickerfrom">
							<span class="fa fa-calendar"></span>
				      </label>
				     </div>
				    </div>
				    </div>
				    <div class="col-md-4">
				      <div class="form-group">
				     <label class="col-md-6 control-label">Invoice To Date :</label>
				     <div class="col-md-6 input-group">
				      <input type="text" class="form-control readonly-color" name="datepickerto" id="datepickerto" onchange="toDateChange()" onkeypress="return false;" autocomplete="off" readonly/>
				      <label class="input-group-addon btn datepickerto" for="datepickerto">
								 <span class="fa fa-calendar"></span>
				      </label>
				     </div>
				    </div>
				    </div>
				 </div>
				<!--  <div class="col-md-12 no-padding-left no-padding-right"><div class="col-md-5 col-xs-5 no-padding-left no-padding-right"><hr class="hr-line-white"/></div><div class="col-md-1 col-xs-1">(Or)</div><div class="col-md-6 col-xs-5 no-padding-left no-padding-right" ><hr class="hr-line-white"/></div></div> -->
				    <div class="col-md-12">
				      <div class="col-md-4">
				            <div class="form-group">
				               <label class="col-md-6 control-label">Invoice No:</label>
				               <div class="col-md-6">
				                  <input type="text" class="form-control" name="invoiceid" id="invoiceid"/>
				                </div>
				            </div>
				        </div>
				     
				     <div class="col-md-4">
				            <div class="form-group">
				               <label class="col-md-6 control-label">Invoice Date:</label>
				               <div class="col-md-6 input-group">
				                  <input type="text" class="form-control" name="invoiceDate" id="invoiceDate" autocomplete="off"/>
				                   <label class="input-group-addon btn invoiceDate" for="invoiceDate">
								      <span class="fa fa-calendar"></span>
				                   </label>
				                </div>
				            </div>
				     </div>
				     <div class="col-md-4">				        
				            <div class="form-group">
				               <label class="col-md-6 control-label">Vendor Name :</label>
				               <div class="col-md-6">
				              	<form:input path="vendorName" id="VendorNameId" class="form-control vendor" autocomplete="off" onkeyup="getVendorId()"/>
				                 <input type="hidden" name="VendorId" id="vendorIdId" class="form-control" value="" autocomplete="off"/>
				                </div>
				            </div>
				     </div>
				     </div>
				     <div class="col-md-12">
				      <div class="col-md-4">
				            <div class="form-group">
				               <label class="col-md-6 control-label">Product Name :</label>
				               <div class="col-md-6">
								<!-- <select id="comboboxProd" name="product" class="form-control">
									<option></option>
								</select>
				                </div> -->
				                <%
											List<Map<String, Object>> totalProductList = (List<Map<String, Object>>) request.getAttribute("totalProductsList");
											if (totalProductList.size() > 0) {
											String strProductName = "";
											String strProductId = "";
										%> <select id="comboboxProd" name="combobox_Product" class="form-control" onchange="getSubProducts()">
											<option value=""></option>
											<%
												for (Map productList : totalProductList) {
												strProductId = productList.get("PRODUCT_ID") == null ? "": productList.get("PRODUCT_ID").toString();
												strProductName = productList.get("NAME") == null ? "" : productList.get("NAME").toString();
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
				               <label class="col-md-6 control-label">Sub Product Name:</label>
				               <div class="col-md-6">
				                <select id="comboboxSubProd" name="subproduct" class="form-control" onchange="getChildroducts()">
				                 <option></option>
				                </select>
				                </div>
				            </div>
				     </div>
				     <div class="col-md-4">				        
				            <div class="form-group">
				               <label class="col-md-6 control-label">Child Product Name:</label>
				               <div class="col-md-6">
				              	<select id="comboboxSubSubProd" name="childproduct" class="form-control">
				              		<option></option>
				              	</select>
				                </div>
				            </div>
				     </div>
				    
				     
				     
				      <div class="col-md-12 col-sm-12 no-padding-left">
							 <!-- <div class="col-md-12"><div class="col-md-12 text-left"><h4><strong>Select site :</strong></h4></div></div> -->
								<div class="col-md-12 text-left"><div class="col-md-12"><input type="checkbox" class="chexkbox_siteall" /><label class="checkbox_sitelabel "><strong>Show Sites</strong></label></div></div>
								<div class="col-md-12 text-left"><div class="col-md-12 hide_select_site" style="display:none;"><input type="checkbox" class=" chexkbox_siteall1"><label class="checkbox_sitelabel"> Select All</label></div></div>
								<div class="col-md-12 text-left hide_select_site" style="display:none;">
								<%-- <c:forEach items="${siteDetails}" var="siteDetails"> --%>
								<c:forEach items="${siteDetails}" var="site">
                               <%-- <option value="${site.key}">${site.value}</option> --%>
                              
								<%-- <%
								Map<String, String> siteDetails =(Map<String, String>) request.getAttribute("siteDetails");
								//String strEliminatedSitesFromReport =  UIProperties.validateParams.getProperty("ELIMINATE_SITES_FROM_STORE_REPORT"); 
								for(Map.Entry<String, String> retVal : siteDetails.entrySet()) { 
								//String strSiteId=retVal.getKey(); 
								//String siteName=retVal.getValue();;
									//if((!strEliminatedSitesFromReport.contains(strSiteId))){ 
									
								
								%> --%>
								 <%--  <div class="col-md-4 col-xs-12"><input type="checkbox" name="checkbox_site_name" class="chexkbox_site" value="<%=strSiteId%>"><label class="checkbox_sitelabel"><%=siteName%></label></div>   --%>
								  <div class="col-md-4 col-xs-12 no-padding-left no-padding-right display_flex"><div class="col-md-1 col-xs-1"><input type="checkbox" name="checkbox_site_name" class="chexkbox_site" value="${site.key}"></div><div class="col-md-11"><label class="checkbox_sitelabel">${site.value}</label></div></div>
								   </c:forEach>
								   <%--  </c:forEach> --%>
						<%-- 		<%} %>	 --%>
							  </div>
							  </div>
				     <input type="hidden" class="form-control" id="siteNames" name="siteIds"  value="" autocomplete="off"/>
				   </div>
				    <div class="col-md-12 text-center center-block">
				      <button type="submit"  class="btn btn-warning" id="btn-search" onclick="return validate()">Search</button>
				    </div>
				   </form:form>
			   </div>
			   
			   <!-- page content -->
	
	
	
	 <c:if test="${requestScope.isShowAll}"> 
	          <div class="">
			  <div class="table-responsive">
			      <table class="table table-new table-Market display nowrap Mrg-total" style="width:100%;border-collapse: collapse !important;" id="expenditure">
			    <thead>
			     <tr>
				       <th>S.No</th>
				       <th>Product Name</th>
				       <th>Sub Product Name</th>
				       <th>Child Product Name</th>
				       <th>UOM</th>
				       <th>Site Name</th>
				       <th>Vendor Name</th>
				       <th>Location</th>
				       <th>Quantity</th>
				       <th>From Date</th>
				       <th>To Date</th>
				       <th>Time</th>
				       <th>Amount</th>

			     </tr>
			    </thead>
			    <tbody>
			    
			  <c:forEach var="expendaturesDetailsList" items="${requestScope['expendaturesList']}">     
			    
			       <tr>
			            <td>${expendaturesDetailsList.serialno}</td>
			            <td>${expendaturesDetailsList.productName}</td>
			            <td>${expendaturesDetailsList.sub_ProductName}</td>
			            
			            <td>${expendaturesDetailsList.child_ProductName}</td>
			            <td>${expendaturesDetailsList.measurementName}</td>
			            <td>${expendaturesDetailsList.siteName}</td>
			            <td>${expendaturesDetailsList.vendorName}</td>
			            <td>${expendaturesDetailsList.location}</td>  
			            <td class="text-right tableQty">${expendaturesDetailsList.quantity}</td>
			            <td>${expendaturesDetailsList.fromDate}</td>
			            <td>${expendaturesDetailsList.toDate}</td>
			         <%--    <td>${marketingPOProductDetails.toDate}</td> --%>
			             <td>${expendaturesDetailsList.time}</td> 
			            <td class="text-right tableAmount">${expendaturesDetailsList.amount}</td>
	               </tr>
	               
	            </c:forEach>        
	            
			    </tbody>
			   <tbody>
			   	   <tr style="background-color: #d9edf7;">
	               	<td colspan="8" class="text-right">SUB TOTAL:</td>
	               	<td id="subTotalQty" class="text-right"></td>
	               	<td></td>
	               	<td></td>
	               	<td></td>
	               	<td id="subTotalAmount" class="text-right"></td>
	               </tr>
			   
			   </tbody>
			   </table>
			 </div>
			 </div>
			  <c:if test="${requestScope.totalAmount != null}"> 
			  <div class="col-md-12 text-right"><h4><strong>Total Amount :<c:out value="${totalAmount}"></c:out></strong></h4></div>
			 </c:if>   
			 
			 
		  </c:if> 
		 
		 
		 <c:if test="${requestScope.isShow}">
	          <div class="">
			  <div class="table-responsive">
			        <table class="table table-new table-Market display table-fixed-header" style="width:100%;border-collapse: collapse !important;" id="expenditure1">
			    <thead>
			     <tr>
				       <th style="width:45px;">S.NO</th>
				       <th>INVOICE ID</th>
				       <th style="width:100px;">INVOICE DATE</th>
				        <th>SITE NAME</th>
				        <th>VENDOR NAME</th>
				        <th>AMOUNT</th>
			     </tr>
			    </thead>
			    <tbody>
			    
			 <c:forEach var="marketingPOProductDetails" items="${requestScope['marketingPOProductDetailsList']}">    
			    
			       <tr>
			            
			            <td>${marketingPOProductDetails.serialno}</td>
			            <td>
			             <c:url value="getAllMarketingExpenditures.spring" var="getAllViewExpenditures">
                         <c:param name="invoiceid" value="${marketingPOProductDetails.invoiceNumber}" />
                         <c:param name="referer" value="ViewExpenditures" />
                        </c:url>
			            <a class="anchor-class" href="<c:out value="${getAllViewExpenditures}"/>">${marketingPOProductDetails.invoiceNumber}</a>
			             </td>
			            
			            <td>${marketingPOProductDetails.invoiceDate}</td>  
			            <td>${marketingPOProductDetails.siteName}</td>
			            <td>${marketingPOProductDetails.vendorName}</td>
			            <td class="valor2">${marketingPOProductDetails.invoiceAmount}</td>
			            
	               </tr>
	               
	           </c:forEach>       
	               
			    </tbody>
			   <tbody>
			     <tr class="info">
								<td colspan="5" class="text-right subTtl">SUB TOTAL:</td>
								<td class="total subTtl"></td>
							</tr>
			   </tbody>
			   </table>
			   
			 </div>
			 </div>
		 </c:if>
		 
		<%-- page content --%>	 			 
			</div>
	        </div>
	      </div>
	</div>
</div>
<!-- scripts -->
 <script src="js/custom.js"></script>
	<!-- 	<script src="js/jquery-ui.js" type="text/javascript"></script> -->
	
		<script src="js/jquery-ui.js" type="text/javascript"></script>	
		<script src="js/jquery.dataTables.min.js"></script>
		<script src="js/dataTables.bootstrap.min.js"></script>
        <script src="js/marketing/InwardsfromCreatePO.js" type="text/javascript"></script>
        <script src="js/jquery.timepicker.min.js"></script>  
        <script src="js/sidebar-resp.js" type="text/javascript"></script> 
        <script src="js/marketing/updateexpenditure.js" type="text/javascript"></script>
        <!-- <script src="js/jqueryFixedheader-3.3.1.js" type="text/javascript"></script>  -->
     
        <script>
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
              .attr("title", "")
              .attr("id", this.element[0].name)
              .autocomplete({
                delay: 0,
                minLength: 0,
                source: $.proxy( this, "_source" )
              })
              .tooltip({
                tooltipClass: "ui-state-highlight"
               });
         
                this._on( this.input, {
                  autocompleteselect: function( event, ui ) {debugger;
                  ui.item.option.selected = true;
                 
                var prodId = "";
                var prodName = "";
                
                prodId = ui.item.option.value;
                prodName = ui.item.value;
                
                var ele = this.element[0].name;
                
                var rowNum = ele.match(/\d+/g); 
                
                //Removing numbers from the header names
                var str1 = ele.replace(/[0-9]/g, '');
                
                if(str1 == "combobox_Product") {
                	prodId = ui.item.option.value;
                    prodName = ui.item.value;
                    getSubProducts();
                    //loadSubProds(prodId, rowNum);
                }            
                else if(str1 == "subproduct") {
                	prodId = ui.item.option.value;
                    prodName = ui.item.value;
                    getChildroducts();
                   // loadSubSubProducts(prodId, rowNum);
                }
                /* else if(str1 == "ChildProduct") {
                	prodId = ui.item.option.value;
                    prodName = ui.item.value;
                    loadUnits(prodId, rowNum);
                }    
                else if(str1 == "UnitsOfMeasurement") {
                	prodId = ui.item.option.value;
                    prodName = ui.item.value;
                    appendvales( prodId ,prodName,rowNum);
                }  */
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
              .removeClass( "ui-corner-all" )
              .addClass( "custom-combobox-toggle ui-corner-right" )
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

        $(function() {	  
            $("#comboboxProd").combobox();    
            $( "#toggle").click(function() {
          $( "#comboboxProd").toggle();
            });
          });
          
          $(function() {
        	$( "#comboboxSubProd").combobox();	
          });
          
          $(function() {
        	$("#comboboxSubSubProd").combobox();
          });
          
        $(document).ready(function(){$(".up_down").click(  function() {
    		$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
    		$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
    		
    		$("#datepickerfrom").datepicker({dateFormat: 'dd-M-y',
   			 changeMonth: true,
   		      changeYear: true});
   		 $("#datepickerto").datepicker({dateFormat: 'dd-M-y',
   			 changeMonth: true,
   		      changeYear: true});
    		
    				});
      /*   $('#expenditure').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]}); */
       var tableheadfix = $('#expenditure').DataTable( {"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
    	     "scrollY": "300px",
    	     "scrollCollapse": true,
    	     "scrollX": true,
    	     "paging": true
		    });
       var tableheadfix1 = $('#expenditure1').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
    	     "scrollY": "300px",
    	     "scrollX": true,
    	     "scrollCollapse": true,
    	     "paging": true
	    });
        $("#datepickerfrom").datepicker({
        	dateFormat: 'dd-M-y',
        	  changeMonth: true,
            changeYear: true
          });
        $("#datepickerto").datepicker({
        	dateFormat: 'dd-M-y',
        	  changeMonth: true,
            changeYear: true
          });
        $("#invoiceDate").datepicker({
        	dateFormat: 'dd-M-y',
        	  changeMonth: true,
            changeYear: true
          });
        $(".chexkbox_siteall1").click(function () {
   	     $('.chexkbox_site').not(this).prop('checked', this.checked);
   	 });
        
        });
        function myFunction(){
        //	$("#expenditure").show();
        return true;
        }
        /*================================================================set location details end====================================================================*/

        function getVendorId(){
        		$("#btn-search").attr("disabled", true);
        	  $.ajax({
        	  url : "./getVendorDetails.jsp",
        	  type : "get",
        	  contentType : "application/json",
        	  success : function(data) {
        	  		$("#VendorNameId").autocomplete({
        		  		source : data,
	    		  		select: function( event, ui ) { debugger;
	    		  			$('.loader-sumadhura').show();
	    	    			var value = ui.item.value;
	    			  		value = value.replace("&", "$$$");
	    			  		setVendorData(value);
	    		  		 }
        		  	});
        	  },
        	  error:  function(data, status, er){
        		  //alert(data+"_"+status+"_"+er);
        		  }
        	  });
        	};
      

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
			$("#btn-search").removeAttr("disabled");
        		$('.loader-sumadhura').hide();
        		$("#vendorIdId").val(vendorId);
        		$("#VendorAddress").val(vendorAddress);
        		$("#GSTINNumber").val(vendorGsinNo);			
        	}
        }

        /*================================================================location details for start=============================================*/
 
        function validate() {debugger;
       
        
		var invoiceFromDate = document.getElementById("datepickerfrom").value;
		var invoiceToDate = document.getElementById("datepickerto").value;
		var invoiceNo = document.getElementById("invoiceid").value;
		var invoiceDate = document.getElementById("invoiceDate").value;
		var vendorName = document.getElementById("VendorNameId").value;
		
		var product=document.getElementById("comboboxProd").value;
		var subProduct=document.getElementById("comboboxSubProd").value;
		var childProduct=document.getElementById("comboboxSubSubProd").value;
		var checkBoxCheck = $('input.chexkbox_siteall').is(':checked')
		 var SiteData=[];
        $(".chexkbox_site").each(function(){debugger;
        	//debugger;
        	if($(this).prop("checked") == true){
        	var currentSite=$(this).val();
        	SiteData.push(currentSite);
        	}
        });
        console.log("SiteData: "+SiteData);
        console.log("SiteData: "+SiteData.length);
        $('#siteNames').val(SiteData);
        
		 
		
		 if (invoiceFromDate == "" && invoiceToDate == "" && invoiceNo =="" && invoiceDate =="" && vendorName =="" && checkBoxCheck == false 
				 && product =="" && subProduct =="" && childProduct =="") { 
			 /*if (invoiceFromDate == "" && invoiceToDate == "" && invoiceNo =="" && invoiceDate =="" && vendorName =="" && siteIds =="") {*/
			alert("Please Select any one of the Input !");
			return false;
		}
		/* if (invoiceNo != "" && invoiceDate == "" && vendorName=="") {
			alert("Please Enter  invoiceDate And VendorName !");
			return false;
		} */
		/* if (invoiceNo != "" && invoiceDate != "" && vendorName=="") {
			alert("Please Enter  VendorName !");
			return false;
		}
		if (invoiceNo == "" && invoiceDate != "" && vendorName=="") {
			alert("Please Enter  InvoiceNo And VendorName !");
			return false;
		}
		if (invoiceNo == "" && invoiceDate == "" && vendorName!="") {
			alert("Please Enter  invoiceNo And invoiceDate !");
			return false;
		}
		if (invoiceNo == "" && invoiceDate != "" && vendorName!="") {
			alert("Please Enter  invoiceNo !");
			return false;
		}
		if (invoiceNo != "" && invoiceDate == "" && vendorName!="") {
			alert("Please Enter  invoiceDate !");
			return false;
		} */
		
	
		/* var siteId=document.getElementById("dropdown_SiteId").value;
		if(siteId==""||siteId==null){
			alert("Please select the Site !");
			return false;	
		} */
	}
        
        </script>
        <script>
        $('.chexkbox_siteall').click(function(){
            if($(this).prop("checked") == true){
            	$(".hide_select_site").toggle(500);
            	$(".checkbox_sitelabel1").toggle(500);
            }
            else if($(this).prop("checked") == false){
            	$(".hide_select_site").hide(500);
            	$(".checkbox_sitelabel1").hide(500);
            }
        });
        
       
       /*  $(".chexkbox_site").each(function(){debugger;
        	//debugger;
        	if($(this).prop("checked") == true){
        	var currentSite=$(this).val();
        	SiteData.push(currentSite);
        	}
        });
        console.log("SiteData: "+SiteData);
        console.log("SiteData: "+SiteData.length);
        $('#siteNames').val(SiteData); */
        
         $(document).ready(function(){ 
           	   $(".info td").show();
           	   //default page load calculation
           	   $(".subTtl").show();
		 	   $(".text-right").show();
		 	   var val = $('#expenditure').find('tbody').find('tr');
		       var tAmount = 0;
			   jQuery.each(val,function(index,item){debugger;
			     tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			   });
			  $(".total").text(tAmount.toFixed(2));
           	   
     	 	$(document).on("keyup", ".dataTables_wrapper .dataTables_filter input, .paginate_button ",function(){
     	 		$(".subTtl").show();
     	 		$(".text-right").show();
     	 		var val = $('#expenditure').find('tbody').find('tr');
     			var tAmount = 0;
     		    jQuery.each(val,function(index,item){
     		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
     		    });
     			$(".total").text(tAmount.toFixed(2));
     		});
     		$(document).on("click", " .paginate_button",function(){
 		 		debugger;
 		 		$(".subTtl").show();
 		 		$(".text-right").show();
 		 		 var val = $('#expenditure').find('tbody').find('tr');
 				 var tAmount = 0;
 			    jQuery.each(val,function(index,item){
 			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
 			    });
 				$(".total").text(tAmount.toFixed(2));
 			});
     		$(document).on("change", " .dataTables_length",function(){
			 		$(".subTtl").show();
			 		$(".text-right").show();
			 		var val = $('#expenditure').find('tbody').find('tr');
					var tAmount = 0;
				    jQuery.each(val,function(index,item){
				    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				    });
					$(".total").text(tAmount.toFixed(2));
				});
     	});
        $(document).ready(function(){ 
     	   debugger;         
        	   $(".info td").show();
        	   //default page load calculation
        	   $(".subTtl").show();
		 	   $(".text-right").show();
		 	   var val = $('#expenditure1').find('tbody').find('tr');
		       var tAmount = 0;
			   jQuery.each(val,function(index,item){debugger;
			     tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			   });
			  $(".total").text(tAmount.toFixed(2));
        	   
  	 	$(document).on("keyup", ".dataTables_wrapper .dataTables_filter input, .paginate_button ",function(){
  	 		$(".subTtl").show();
  	 		$(".text-right").show();
  	 		var val = $('#expenditure1').find('tbody').find('tr');
  			var tAmount = 0;
  		    jQuery.each(val,function(index,item){
  		    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
  		    });
  			$(".total").text(tAmount.toFixed(2));
  		});
  		$(document).on("click", " .paginate_button",function(){
		 		debugger;
		 		$(".subTtl").show();
		 		$(".text-right").show();
		 		 var val = $('#expenditure1').find('tbody').find('tr');
				 var tAmount = 0;
			    jQuery.each(val,function(index,item){
			    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
			    });
				$(".total").text(tAmount.toFixed(2));
			});
  		$(document).on("change", " .dataTables_length",function(){
			 		$(".subTtl").show();
			 		$(".text-right").show();
			 		var val = $('#expenditure1').find('tbody').find('tr');
					var tAmount = 0;
				    jQuery.each(val,function(index,item){
				    	tAmount = tAmount + (parseFloat(jQuery(item).find('.valor2').text().replace(/,/g,'') || 0));
				    });
					$(".total").text(tAmount.toFixed(2));
				});
  	});
      //****** end  calculating subtotal Invoice Amount  *****

     /*  ************************************************add sub product details start************************************************************** */
         function getSubProducts(){debugger;
			var prod_id=$("#comboboxProd").val();
			//$('#combobox_delete_SubProd').html('');
			//$('#combobox_delete_SubProd').val('');
			 $.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'getSubProducts.spring?prodId='+prod_id,
					success : function(data) {
						var trHTML = '';
						var dataLen = data.length;
						  console.log(data);
			              for (i=0; i<dataLen; i++) {
			            	  trHTML += '<option value="' + data[i].sub_ProductId + '@@'  + data[i].sub_ProductName + '">'+  data[i].sub_ProductName + '</option>' ;
			              }	
			              
			              $('#comboboxSubProd').html(trHTML);
						},  
					error : function(e) {  
						alert('Error: ' + e);   
						} 
				});
		}	
      
      
      /* **********************************************add cheild product details start******************************************************* */
      
         function getChildroducts(){debugger;
			var SubprodId=$("#comboboxSubProd").val();
			//$('#combobox_delete_SubProd').html('');
			//$('#combobox_delete_SubProd').val('');
			 $.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'getChildProducts.spring?subProdId='+SubprodId,
					success : function(data) {
						var trHTML = '';
						var dataLen = data.length;
						  console.log(data);
			              for (i=0; i<dataLen; i++) {
			            	  trHTML += '<option value="' + data[i].child_ProductId + '@@'  + data[i].child_ProductName + '">'+  data[i].child_ProductName + '</option>' ;
			              }	
			              
			              $('#comboboxSubSubProd').html(trHTML);
						},  
					error : function(e) {  
						alert('Error: ' + e);   
						} 
				});
		}	
		
         $(document).ready(function(){ 
            	   //default page load calculation
 		 	   var val = $('#expenditure').find('tbody').find('tr');
 		       var tQty = 0;
 		       var tAmount = 0;
 			   jQuery.each(val,function(index,item){
 			  	tQty = tQty + (parseFloat(jQuery(item).find('.tableQty').text().replace(/,/g,'') || 0));
 			    tAmount = tAmount + (parseFloat(jQuery(item).find('.tableAmount').text().replace(/,/g,'') || 0));
 			   });
 			  $("#subTotalQty").text(inrFormat(tQty.toFixed(2)));
 			 $("#subTotalAmount").text(inrFormat(tAmount.toFixed(2)));
            	   
      	 	$(document).on("keyup", ".dataTables_wrapper .dataTables_filter input, .paginate_button ",function(){
      	 	 var val = $('#expenditure').find('tbody').find('tr');
		       var tQty = 0;
		       var tAmount = 0;
			   jQuery.each(val,function(index,item){
			  	tQty = tQty + (parseFloat(jQuery(item).find('.tableQty').text().replace(/,/g,'') || 0));
			    tAmount = tAmount + (parseFloat(jQuery(item).find('.tableAmount').text().replace(/,/g,'') || 0));
			   });
			  $("#subTotalQty").text(inrFormat(tQty.toFixed(2)));
			 $("#subTotalAmount").text(inrFormat(tAmount.toFixed(2)));
      		});
      	 	
      		$(document).on("click", " .paginate_button",function(){
      			 var val = $('#expenditure').find('tbody').find('tr');
   		       var tQty = 0;
   		       var tAmount = 0;
   			   jQuery.each(val,function(index,item){
   			  	tQty = tQty + (parseFloat(jQuery(item).find('.tableQty').text().replace(/,/g,'') || 0));
   			    tAmount = tAmount + (parseFloat(jQuery(item).find('.tableAmount').text().replace(/,/g,'') || 0));
   			   });
   			  $("#subTotalQty").text(inrFormat(tQty.toFixed(2)));
   			 $("#subTotalAmount").text(inrFormat(tAmount.toFixed(2)));
  			});
      		
      		$(document).on("change", " .dataTables_length",function(){
      		   var val = $('#expenditure').find('tbody').find('tr');
   		       var tQty = 0;
   		       var tAmount = 0;
   			   jQuery.each(val,function(index,item){
   			  	tQty = tQty + (parseFloat(jQuery(item).find('.tableQty').text().replace(/,/g,'') || 0));
   			    tAmount = tAmount + (parseFloat(jQuery(item).find('.tableAmount').text().replace(/,/g,'') || 0));
   			   });
   			  $("#subTotalQty").text(inrFormat(tQty.toFixed(2)));
   			  $("#subTotalAmount").text(inrFormat(tAmount.toFixed(2)));
 		   });
      	});
      	
         
//for indian rupees format
         function inrFormat(nStr) { // nStr is the input string
         debugger;
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
         function fromDateChange(){
     		var x=$('#datepickerfrom').datepicker("getDate");
     		$("#datepickerto").datepicker( "option", "minDate",x ); 	
     	}
     	function toDateChange(){
     		var x=$('#datepickerto').datepicker("getDate");
     		$("#datepickerfrom").datepicker( "option", "maxDate",x ); 	
     	}  
        </script>
</body>
</html>
