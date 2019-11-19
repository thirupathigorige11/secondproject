<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
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
<style>
.table-responsive .table {
    max-width: none;
     -webkit-overflow-scrolling: touch;
}
.form-group label{
    text-align: left
}
table { border-collapse: collapse; empty-cells: show; }

td { position: relative; }

tr.strikeout td:before {
  content: " ";
  position: absolute;
  top: 50%;
  left: 0;
  border-bottom: 1px solid red;
  width: 100%;
    z-index: 100px;
}

tr.strikeout td:after {
  content: "\00B7";
  font-size: 1px;
  z-index: 100px;
}

/ Extra styling /
td { width: 100px; }
th { text-align: left; }
.Btnbackground{
background:-webkit-linear-gradient(top, rgba(255,246,234,1) 0%, rgba(255,255,238,1) 7%, rgba(176,123,111,1) 11%, rgba(176,123,111,1) 28%, rgba(255,255,255,1) 31%, rgba(255,255,255,1) 56%, rgba(54,38,33,1) 60%, rgba(193,185,182,1) 100%);
}
.closeiconforimg {
    position: relative;
    top: -88px;
    right: 13px;
    z-index: 100;
    background-color: #e0e0e0;
    padding: 5px 5px;
    color: #000;
    font-weight: bold;
    cursor: pointer;
    text-align: center;
    font-size: 17px;
    line-height: 10px;
    border-radius: 50%;
    border: 1px solid blue;
    color: blue;
    opacity: 2;
}

table.dataTable{border-collapse: collapse !important;
}

/* css for iframe modal popup - start */
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
}
.btn-fullwidth{
background-color:transparent;
border-color:transparent;
color:transparent;
height:200px;
width:100%;
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

.container-header {
	font-size: 22px;
	background: #b4d4e3;
}

.moveimage {
	position: absolute;
	left: 41px;
	bottom: 170px;
}

.bckcolor {
	background: #b4d4e3;
	font-size: 18px;
	font-weight: bold;
}

.text-img {
	float: left;
	position: relative;
	bottom: -26px;
	right: -38px;
}

.header-content {
	font-weight: bold;
}

.footer-bottom {
	padding-bottom: 50px;
}

.headerTitle {
	line-height: 7px;
}

.content-bold {
	font-weight: bold;
}

.addres-split {
	width: 230px;
}

.text-alignment-content {
	font-size: 16px;
	font-weight: bold;
	margin-right: 10px;
}

.colon-conent {
	margin-right: 10px;
}

.viewQuote{
	width: 139px;
    margin-left: 500px;
    margin-top: 10px;
    height: 37px;
    background: #f3ba52;
    border-radius: 8px;
}
.viewComp{

	width: 139px;
    margin-left: 349px;
    margin-top: -63px;
    height: 37px;
    background: #f3ba52;
    border-radius: 8px;

}


@media print {
	.viewQuote {
		display: none;
	}
}

@media print {
	.viewComp {
		display: none;
	}
}

@media print {
	#printPageButton {
		display: none;
	}
}

@media print {
	#printPageButton {
		display: none;
	}
}

@media print {
	.loginbox {
		display: none;
	}
	.pdf-cls{
	display:none;
	}
}
/* css for iframe modal popup - end*/
</style>
<html>
<head>
<script src="js/BOQJS/ReviseBoq.js" type="text/javascript"></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<!-- Meta, title, CSS, favicons, etc. -->
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		
	<jsp:include page="../CacheClear.jsp" />  
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">		
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
        <link href="css/custom.css" rel="stylesheet" type="text/css">
        <link href="css/topbarres.css" rel="stylesheet">
        
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<title>BOQ For Approve</title>

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
		.tooltipClass{
		    width: 120px;
    background-color: #555;
    color: #fff;
    text-align: center;
    border-radius: 6px;
    padding: 5px 0;
    position: absolute;
    z-index: 1;
    left: 50%;
    opacity: 1;
    transition: opacity 0.3s;
    }
    .tooltipClass::after{
    content: "";
    position: absolute;
    top: 100%;
    left: 50%;
    margin-left: -5px;
    border-width: 5px;
    border-style: solid;
    border-color: #555 transparent transparent transparent;
    
    }
    table.dataTable{
        border-collapse: collapse!important;
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
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">BOQ For Approval</li>
						</ol>
					</div>
					
					
					<div>
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
				<label class="control-label col-md-6">BOQ Total :</label>
				<div class="col-md-6" >
				<input  id="boqTotal" name="boqTotal" readonly="readonly" class="form-control" value="${boqMainList.boqTotalAmount}"/>
				</div>
			</div>
	  		</div>	
	  			<div class="col-md-6">
	   		<div class="form-group">
				<label class="control-label col-md-6">Prev BOQ Amount :</label>
				<div class="col-md-6" >
				<input  id="boqTotal" name="boqTotal" readonly="readonly" class="form-control" value="${prevBoqTotal}"/>
				</div>
			</div>
	  		</div>	
	  		<div class="col-md-6">
	   		<div class="form-group">
				<label class="control-label col-md-6">BOQ Material Total :</label>
				<div class="col-md-6" >
				<input  id="boqTotal" name="boqTotal" readonly="readonly" class="form-control" value="${boqMainList.boqMaterialAmount}"/>
				</div>
			</div>
	  		</div>	
	  			<div class="col-md-6">
	   		<div class="form-group">
				<label class="control-label col-md-6">Prev BOQ Material Total :</label>
				<div class="col-md-6" >
				<input  id="boqTotal" name="boqTotal" readonly="readonly" class="form-control" value="${prevMaterialTotal}"/>
				</div>
			</div>
	  		</div>	
	  		<div class="col-md-6">
	   		<div class="form-group">
				<label class="control-label col-md-6">BOQ Labor Total :</label>
				<div class="col-md-6" >
				<input  id="boqTotal" name="boqTotal" readonly="readonly" class="form-control" value="${boqMainList.boqLaborAmount}"/>
				</div>
			</div>
	  		</div>	
	  			<div class="col-md-6">
	   		<div class="form-group">
				<label class="control-label col-md-6">Prev BOQ Labor Total :</label>
				<div class="col-md-6" >
				<input  id="boqTotal" name="boqTotal" readonly="readonly" class="form-control" value="${prevLaborTotal}"/>
				</div>
			</div>
	  		</div>	
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
	  	     
	  	     
	  	     
	  	     <div class="col-md-12" style="margin-top:20px;">
				<div class="clearfix" ></div>
					<table id="example"	class="table table-striped table-bordered st-table"  cellspacing="0">
						<thead>
							<tr>
								<th>Major Head</th>
								<th>Labor Changes</th>
								<th>Material Changes</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${MajorHeadDetails}" var="element">
								<tr>
									<td>${element.strMajorHeadDesc}</td>
									<td><a href="getPendingForApprovalReviseBOQDetails.spring?tempBOQNo=${element.intTempBOQNo}&siteId=${element.strSiteId}&boqType=REVISED&recordType=LABOR&majorHeadId=${element.strMajorHeadId}&viewTempBoq=${viewTempBoq}&pageHighlightURL=${pageHighlightURL}" >Labor Changes</a>  </td>
									<td><a href="getPendingForApprovalReviseBOQDetails.spring?tempBOQNo=${element.intTempBOQNo}&siteId=${element.strSiteId}&boqType=REVISED&recordType=MATERIAL&majorHeadId=${element.strMajorHeadId}&viewTempBoq=${viewTempBoq}&pageHighlightURL=${pageHighlightURL}" >Material Changes</a>  </td>
								</tr>
							</c:forEach>	
				       </tbody>
					</table>
				</div>
		 </div>
		    <%-- <input type="hidden" name="noofRowsTobeProcessed" value="${requestScope['IndentCreationDetailsList'].size()}"/> --%>
		<%-- <div class="table-responsive Mrgtop10">
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
    				<th  width=213px;class="w-70">Actions</th>
    			</tr>
  			</thead>
  			<tbody>
  			
  			 <c:forEach var="boqData" items="${requestScope['boqWorkDeatils']}">
  			<tr>
  					<td>${boqData.strSerialNumber}</td>
  					<td>${boqData.strMajorHeadDesc }</td>
  					<td>${boqData.strMinorHeadDesc }</td>
  					<td>${boqData.strWorkDescription }</td>
  					<td>${boqData.strMeasurementName }</td>
  					<td> ${boqData.strScopeOfWork } </td><!-- -->
  					<td>${boqData.doubleLaborRatePerUnit }</td>
  					<td>${boqData.strArea }</td>
  					<td>${boqData.strBlock }</td>
  					<td>${boqData.strFloor }</td>
  					<td>${boqData.strFlat }</td>
  					<td>${boqData.action }</td>
  					
  			</tr>
  			</c:forEach>

				
					</tbody>			
			</table>
			</div> --%>
	<!-- *********** Commments Model*************	 -->
<!-- 			 <div class="modal fade" id="myModal" role="dialog">
 				   <div class="modal-dialog">
    
    			 Modal content
			      <div class="modal-content">
			        <div class="modal-header">
			          <button type="button" class="close" data-dismiss="modal">&times;</button>
			          <h4 class="modal-title">Remarks</h4>
			        </div>
			        <div class="modal-body" >
			        
			        </div>
			        <div class="modal-footer">
			          <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
			        </div>
			      </div>
			      
			    </div>
			         
			    </div> -->
			<%-- <c:forEach var="boqMainList" items="${requestScope['boqData']}">
				<a href="getModificationDetails.spring?tempBOQNo=${boqMainList.intTempBOQNo}&siteId=${boqMainList.strSiteId}&boqType=REVISED&recordType${recordType}" class="btn btn-warning">Modification Details</a>
			</c:forEach>
			<br></br> --%>
			<%-- <div class="row">
				<div class="form-group">
				  <label class="control-label col-md-2" >Modification Details: </label>
						<div class="col-md-4" style="margin-bottom: 10px;" >
							<ol type="1">
							<c:forEach var="ChangedDetails" items="${requestScope['ReviseBOQChangedDetails']}">
							  <li>${ChangedDetails.modificationDetails}</li>
							 </c:forEach>
							 <c:forEach var="sow" items="${requestScope['SOWChangedDetails']}">
							  <li>${sow.modificationDetails}</li>
							 </c:forEach>
							</ol>
						</div>
				</div>
			
			</div> --%>
			<div class="row"> <!-- tblindentissunote -->
				<%--  <c:forEach var="getIndentCreation" items="${requestScope['IndentCreationList']}"> --%>
		
					
					<label class="control-label col-md-2" >Note: </label>
						<div class="col-md-4" style="    position: relative;margin-bottom: 10px;" >
						<input type="hidden" name="actualNote" value="${BOQLevelCommentsList}">
							<form:textarea path="" href="#" data-toggle="tooltip" title="${BOQLevelCommentsList}"   id="NoteId" name="Note" class="form-control" autocomplete="off" placeholder="${BOQLevelCommentsList}"></form:textarea>
						</div>
			
			</div>
			<c:if test="${ not viewTempBoq}">
			   <div class="col-md-12 text-center center-block ">
					<input type="button" style="width: 133px;" class="btn btn-warning Mrgtop10" value="Approve" id="saveBtnId" onclick="saveRecords('SaveClicked')">
						<!-- <input type="button" style="width: 133px;" class="btn btn-warning Mrgtop10"value="Reject" id="saveBtnId" onclick="reject('SaveClicked')"> -->
				    <input type="button"  style="width: 133px;" class="btn btn-warning Mrgtop10" value="Reject"  onclick="reject('SaveClicked')" id="saveBtnId"/>
						
				</div>
				</c:if>
				<div class="row" style="margin-top:100px !important">
				<c:forEach var="ObjBOQDetails" items="${requestScope['boqData']}">
				<a href="getBOQDetailsSelection.spring?BOQSeqNo=${ObjBOQDetails.strBOQSeqNo}&siteId=${ObjBOQDetails.strSiteId}&pageHighlightURL=${pageHighlightURL}" class="btn btn-warning" >View Present BOQ</a>
				
				 <a href="downloadBOQ.spring?BOQNumber=${ObjBOQDetails.strBOQNo}&siteId=${ObjBOQDetails.strSiteId}&siteName=${ObjBOQDetails.strSiteName}&permanentBOQ=true&versionNo=${ObjBOQDetails.strVersionNo}&pageHighlightURL=${pageHighlightURL}"  class="btn btn-warning">DOWNLOAD BOQ</a>
				 </c:forEach>	
			    </div>
			<input type="hidden" name="numbeOfRowsToBeProcessed" value="" id="countOfRows">
			<input type="hidden" name="isSaveBtnClicked" value="" id="hiddenSaveBtnId">
			
			
			  <input type="hidden" id="materialEditComment" name="materialEditComment" value="${requestScope['materialEditComment']}" />
				  
				   <h2 style= "margin-left: -909px;font-weight: bold; margin-top: 102px;">Modification Details:</h2>
				 
				  
				  <div class="modifydetails" >
				  <c:forEach var="indentCreationComments" items="${requestScope['materialEditCommentList']}" step="1" begin="0">	
	        	 	
	        	 	<div style="float:left;margin-left: 11px;    margin-left: -367px;font-size: 15px; font-weight: bold; margin-top: 12px;" >		
	        	 	 <ul class="check_list" name="editComments" style="margin-left: 349px;margin-top: 14px;" >
	        	 	   <li class="Comment_content" style=""> ${indentCreationComments.materialEditComment}</li>
			         </ul>
			       </div>
				</c:forEach>
				</div>
				 <!-- modal popup for indent reject start-->
<!-- 					<div id="modalCentralIndent-reject" class="modal fade" role="dialog">
					  <div class="modal-dialog">
					
					    Modal content
					    <div class="modal-content">
					      <div class="modal-header">
					        <button type="button" class="close" data-dismiss="modal">&times;</button>
					        <h4 class="modal-title">Please Enter The Comment</h4>
					      </div>
					      <div class="modal-body" style="overflow:hidden;">
					      <textarea class="form-control" style="resize:vertical;" name="indentRemarks">
					      </textarea>
					        
					      </div>
					      <div class="modal-footer">
					       <div class="col-md-12 text-center center-block">
					        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					        <button type="button" class="btn btn-warning" onclick="reject('SaveClicked')">Submit</button>
					       </div>
					      </div>
					    </div>
					
					  </div>
					</div> -->
					<!-- modal popup for indent reject end -->
				
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
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
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
	<style>
	   table.dataTable{
        border-collapse: collapse!important;
   		 }
	</style>
  <script>
		
			$(document).ready($(function() {

				$("#boqTableId").DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]]});
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
			/* $(function(){
				var div1 = $(".right_col").height();
				var div2 = $(".left_col").height();
				var div3 = Math.max(div1,div2);
				$(".right_col").css('max-height', div3);
				$(".left_col").css('min-height', $(document).height()-65+"px");
			}); */
			
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
