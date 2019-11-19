<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.List"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>


<html>
<head>
<style> 
.mybtnstyles{height:25px;width:25px;color:#000;}
.breadcrumb{margin-bottom:0px !important;}
.anchor-back{margin-top:5px;margin-bottom:15px;}
.anchor-back a i{font-size:12px;}
.add-more{margin-right:10px;}
.finalAmountDiv{
    color: #f0ad4e;
    font-size: 24px;
}
.form-group label{
    text-align: left
}
</style>
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet">
		<link href="css/custom.css" rel="stylesheet">
		<link href="css/topbarres.css" rel="stylesheet">
		<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
		<title>Sumadhura-IMS</title>
        <link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
	<style>
	#finalAmntDiv{
	 font-size: 24px;
    font-weight: bold;
	
	}
	</style>
<script type="text/javascript">
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
<script>
function giveVendorNameNumbers(){debugger;
	var num=[];
	$(".Vendor-Name-Id").each(function(){
		var id=$(this).attr("id").split("VendorNameId")[1];
		num.push(id);
	});
	return num;
}

function createPO() {
	$(".loader-ims").show();
	var valvendor=validateVendors();
	var vendorlength=$(".Vendor-Name-Id").length;
	if(vendorlength>1){
		$(".loader-ims").hide();
		alert('Unable to create a PO,as more than one vendor was selected.');
		return false;
	}
	if(valvendor==false){
		$(".loader-ims").hide();
		return false;
	}
	
	 if($('input[type=checkbox]:checked').length == 0){
		 $(".loader-ims").hide();
	        alert('Please select atleast one product');
	        return false;
	    }
	 
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		$(".loader-ims").hide();
		return;
	}	
	var vendorNames=giveVendorNameNumbers();
	$("#vendorNo").val(vendorNames);
	console.log("Num: "+vendorNames);
	
	document.getElementById("CreateCentralIndentFormId").action = "createPODetails.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}
	/* ===========================================check the vendor data start============================================== */
function validateSendEnquiry(){
	debugger;
	var valvendor=validateVendors();
	if(valvendor==false){
		return false;
	}
	 if($('input[type=checkbox]:checked').length == 0) {
	        alert('Please select atleast one product');
	        return false;
	    }
		 $("#saveBtnId").attr("data-toggle","modal");
		 $("#saveBtnId").attr("data-target","#modal-modifytemPo");
 
}

function sendEnquiry() {
	
	$(".loader-ims").show();	 
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		$(".loader-ims").hide();
		return;
	}
	var vendorNames=giveVendorNameNumbers();
	$("#vendorNo").val(vendorNames);
	console.log("Num: "+vendorNames)
	document.getElementById("CreateCentralIndentFormId").action="sendenquiry.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}
function fillForm() {
	$(".loader-ims").show();
	var valvendor=validateVendors();
	//var valvendor=validateVendors();
	var vendorlength=$(".Vendor-Name-Id").length;
	if(vendorlength>1){
		$(".loader-ims").hide();
		alert('Unable to create a PO,as more than one vendor was selected.');
		return false;
	}
	if(valvendor==false){
		$(".loader-ims").hide();
		return false;
	}
	
	 if($('input[type=checkbox]:checked').length == 0)
	    {
		 $(".loader-ims").hide();
	        alert('Please select atleast one product');
	        return false;
	    }
	 
	var canISubmit = window.confirm("Do you want to Submit?");
	
	if(canISubmit == false) {
		$(".loader-ims").hide();
		return;
	}
	var vendorNames=giveVendorNameNumbers();
	$("#vendorNo").val(vendorNames);
	console.log("Num: "+vendorNames)
	document.getElementById("CreateCentralIndentFormId").action="fillForm.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}

function printIndent() {
	
	$(".loader-sumadhura").show();
	
	document.getElementById("CreateCentralIndentFormId").action = "printIndentForPurchase.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();//printIndentForPurchase
}

function viewComparision() {
	/* if($('input[type=checkbox]:checked').length == 0)
    {
	 $(".loader-ims").hide();
        alert('Please select atleast one product.');
        return false;
    } */
	var valvendor=validateVendors();
	var vendorlength=$(".Vendor-Name-Id").length;
	if(vendorlength>1){
		$(".loader-ims").hide();
		alert('Unable to create a PO,as more than one vendor was selected.');
		return false;
	}
	$(".loader-sumadhura").show();
    var vendorNames=giveVendorNameNumbers();
	$("#vendorNo").val(vendorNames);
	console.log("Num: "+vendorNames)
	document.getElementById("CreateCentralIndentFormId").action = "getComparisionStatement.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}

function ViewQuatation() {
	var valvendor=validateVendors();
	var vendorlength=$(".Vendor-Name-Id").length;
	if(vendorlength>1){
		$(".loader-ims").hide();
		alert('Unable to create a PO,as more than one vendor was selected.');
		return false;
	}
	$(".loader-ims").show();
	var valvendor=validateVendors();
	if(valvendor==false){
		$(".loader-ims").hide();
		return false;
	}
	
	 if($('input[type=checkbox]:checked').length == 0)
	    {
		 $(".loader-ims").hide();
	        alert('Please select atleast one product');
	        return false;
	    }
	
	//$(".loader-sumadhura").hide();
	document.getElementById("CreateCentralIndentFormId").action = "getQuatationDetails.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}

function closeIndent() {
	$(".loader-ims").show();
	if($('input[type=checkbox]:checked').length == 0)
    {
		$(".loader-ims").hide();
        alert('Please select atleast one product');
        return false;
    }

	
	document.getElementById("CreateCentralIndentFormId").action = "closeIndent.spring";
	document.getElementById("CreateCentralIndentFormId").method = "POST";
	document.getElementById("CreateCentralIndentFormId").submit();
}
//validating vendor name
function validateVendors(){
	var error=true;
	$(".Vendor-Name-Id").each(function(){
		var id=$(this).attr("id").split("VendorNameId")[1];
		var venName =  $("#VendorNameId"+id).val();
		var GSTINNumber=$("#GSTINNumber"+id).val();
		if(venName == "" || venName == null || venName == '') {
			alert("Please enter the vendor name");
			$("#VendorNameId"+id).focus();
			return error=false;
		}
		if(GSTINNumber ==''){
			alert("Please enter valid vendor name");
			$("#VendorNameId"+id).focus();
			return error=false;
		}
	})
	return error;
}


</script>
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
					<div>
						<ol class="breadcrumb">
							<li class="breadcrumb-item"><a href="#">Home</a></li>
							<li class="breadcrumb-item active">Indent Details</li>
						</ol>
					</div>
					<div class="col-md-12 anchor-back">
					 <!-- <a  class="btn btn-info btn-xs"><i class="fa fa-arrow-left"></i> Back</a> -->
					 <a href="${requestScope['urlName']}&showPrevPaginationNo=true">Back</a>
					</div>
					
					<div>
	<div class="">
		<form:form modelAttribute="CreatePOModelForm" id="CreateCentralIndentFormId"   class="form-horizontal" enctype="multipart/form-data">
		<!-- <div class="container"> -->
	 <!-- <div class="row"> -->
	  <div class="col-md-12 ">
		
			<c:forEach items="${IndentDtls}" var="element">
			<div class="border-indent">
			<div class="col-md-6">
			 <div class="form-group">
			  <label class="control-label col-md-6">Site wise Indent Number :</label>
			<div class="col-md-6" >
			<input name="indentNumber" id="indentNumberId" type="hidden" readonly="true" class="form-control" value="${element.indentNumber}"}/>
			<input name="siteWiseIndentNo" id="siteWiseIndentNo"  readonly="true" class="form-control" value="${element.siteWiseIndentNo}"}/>
			</div>
			</div>
			</div>
			<div class="col-md-6">
			  <div class="form-group">
			  <label class="control-label col-md-6">Site Name :</label>
			<input type="hidden" class="form-control"  name="versionNo" value="${element.versionNo}">
			<input type="hidden" class="form-control"  name="reference_No" value="${element.reference_No}">
			<input type="hidden"  class="form-control"  	name="issue_date" value="${element.issue_date}">
			
			<div class="col-md-6" >
				<input name="siteName" id="siteNameId"  readonly="true" class="form-control" data-toggle="tooltip"  title="<c:out value='${element.siteName}' />" value="${element.siteName}"/>
				
				<input type="hidden" name="siteId" id="siteIdId"   class="form-control" value="${element.siteId}"}/>
			</div>
			</div>
			</div>
			</div>
			</c:forEach>
		
		
		  <div class="">
	
		<input type="hidden" name="count" value="1" />
        <div id="fields" class="border-indent">
        	<input type="hidden" id="noofvendorsproccessed" name="noofvendorsproccessed" value="1" />
                       <div class="controls" id="profs1"> 
                <div class="input-append row vendor1">
                    <div class="col-md-3">
                      <div class="form-group">
                    <label class="control-label col-md-6">Vendor Name : </label>
                    <div class="col-md-6" >
                    <input id="VendorNameId1" name="vendorName1" class="form-control Vendor-Name-Id" autocomplete="off" uda-form-no="1" />
                    </div>
                    </div>
                    </div>
                    <div class="col-md-3">
                     <div class="form-group">
                    <label class="control-label col-md-6">GSTIN : </label>
                    <div class="col-md-6" >
                    <input id="GSTINNumber1" name="strGSTINNumber1" class="form-control" autocomplete="off" readonly="true" />
                    </div>
                    </div>
                    </div>
                    <div class="col-md-3">
                     <div class="form-group">
                    <label class="control-label col-md-6">Vendor Address : </label>
                    <div class="col-md-6" >  
                    <input id="VendorAddress1" name="vendorAddress1" class="form-control" readonly="true" />
                    </div>
                    </div>
                    </div>
                    <div class="col-md-3 text-center center-block">
                      <button id="b1" class="add-more mybtnstyles" type="button"><span class="glyphicon glyphicon-plus"></span></button>
                      <button id="remove" class="remove-me mybtnstyles"  onclick="deleteVendor(1)" type="button"><span class="glyphicon glyphicon-minus"></span></button>
                    </div>
                    </div>
                    <div class="col-md-1" >
					<input type="hidden" id="vendorId1" name="vendorId1" class="form-control"  />
					<input type="hidden" name="strCreationDate" id="strCreationDate"   class="form-control" value="${strCreationDate}"}/>
					<input type="hidden" name="indentDate" id="indentDate"   class="form-control" value="${strCreationDate}"}/>
					<input type="hidden"  class="form-control"  name="url" value="${requestScope['url']}">
					  <input type="hidden" id="vendorNo" name="vendorNumber" class="form-control" autocomplete="off" value=""/>
					</div>
					<div class="clearfix"></div>
                </div>
           
            </div>
          
	</div>
</div>

	<!-- Loader  -->
				          <div class="loader-sumadhura" style="display: none;z-index:999;">
								<div class="lds-facebook">
									<div></div><div></div><div></div><div></div><div></div><div></div>
								</div>
								<div id="loadingMessage">Loading...</div>
				          </div>
						<!-- Loader -->	

	<input type="hidden" name="numberOfRows"   value="${requestScope['IndentDetails'].size()}" />
<!-- *************** Table 01 grid***************** -->
				<!-- <div class="container1"> -->
				<div class="col-md-12">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead>
			<tr>
				<th id="checkedbox"><input type="checkbox" id="selectall" />&nbsp;Select All</th>
				<th>Product Name</th>
				<th>SubProduct Name</th>
				<th>ChildProduct Name</th>
				<th>Measurement Name</th>
				<th style="width: 155px;">PO Pending Quantity</th>
				<th>Po Req Quantity</th>
				<th>PO Initiated Quantity</th>
				<th>Remarks</th>
			</tr>
						</thead>
						<tbody>
						
						 
						
				<c:forEach items="${IndentDetails}" var="element"> 
				<div style="display: none;">
				
				<input type="hidden" name="productId${element.strSerialNumber}" value="${element.productId1}" />
				<input type="hidden" name="subProductId${element.strSerialNumber}" value="${element.subProductId1}" />
				<input type="hidden" name="childProductId${element.strSerialNumber}" value="${element.childProductId1}" />
				<input type="hidden" name="unitsOfMeasurementId${element.strSerialNumber}" value="${element.unitsOfMeasurementId1}" />
				<input type="hidden" name="product${element.strSerialNumber}" value="${element.product1}" />
				<input type="hidden" name="subProduct${element.strSerialNumber}" value="${element.subProduct1}" />
				 <input type="hidden" name="childProduct${element.strSerialNumber}" value="${element.childProduct1}" /> 
				<input type="hidden" name="unitsOfMeasurement${element.strSerialNumber}" value="${element.unitsOfMeasurement1}" />
				<input type="hidden" name="strRequestQuantity${element.strSerialNumber}" value="${element.strRequestQuantity}" />
				<%--  <input type="hidden" name="requiredQuantity1${element.strSerialNumber}" value="${element.printQuantity}" />  --%>
				<input type="hidden" name="pendingQuantity${element.strSerialNumber}" value="${element.pendingQuantity}" />
				<input type="hidden" name="purchaseDepartmentIndentProcessSeqId${element.strSerialNumber}" value="${element.purchaseDepartmentIndentProcessSeqId}" />
				<input type="hidden" name="poIntiatedQuantity${element.strSerialNumber}" value="${element.poIntiatedQuantity}" />
				<input type="hidden" name="indentCreationDetailsId${element.strSerialNumber}" value="${element.indentCreationDetailsId}" />
				<input type="hidden" name="groupId${element.strSerialNumber}" value="${element.groupId1}" />
				
			
				</div>
				
		   		<tr>
					<td><input type="checkbox"   class="case" name="checkboxname${element.strSerialNumber}" value="checked"></input></td>				
					<td class="tiptext"  class="case">${element.product1}</td>
					<td class="case">${element.subProduct1}</td>
					<td class="case"><input type="text" class='form-control' data-toggle="tooltip" title="<c:out value='${element.childProduct1}' />" name="childProductPuchaseDeptDisc${element.strSerialNumber}" value="<c:out value='${element.childProduct1}' />" readonly/></td>
					<td class="case">${element.unitsOfMeasurement1}</td>
					<td  class="case"><input type="text"  class='form-control' style="width: 67px;" id="POPendingQuantity${element.strSerialNumber}" onfocusout="validateText(this)" name="POPendingQuantity${element.strSerialNumber}" value="${element.requiredQuantity1}" autocomplete="off" onkeypress="return validateNumbers(this, event)" onblur="checkData(${element.strSerialNumber})" size="3" /><input class='form-control' name="productAvailability${element.strSerialNumber}" type="text" style="background-color: #dddddd;;margin-left: 7px;border-radius: 5px; width: 60px;margin-left: 69px;margin-top: -30px;" size="2" readonly="true" data-toggle="tooltip" title="<c:out value='${element.productAvailability1}' />" value="${element.productAvailability1}" /></td>
					<!-- ${element.productAvailability1} -->
					<td class="case">${element.pendingQuantity}</td><input type="hidden"  class='form-control' style="width: 67px;" name="requiredQuantity1${element.strSerialNumber}" data-toggle="tooltip" title="<c:out value='${element.pendingQuantity}' />" value="${element.pendingQuantity}" size="3" />	
					<input type="hidden"  class='form-control' style="width: 67px;" name="requestedQuan${element.strSerialNumber}" value="${element.pendingQuantity}" size="3" />
					<td class="case">${element.poIntiatedQuantity}</td>
				
				
					<td class="case" >${element.remarks1}</td><input  type="hidden" name="remarks${element.strSerialNumber}" style="background-color:PapayaWhip" size="15" readonly="true" value="<c:out value="${element.remarks1}"/>" />	
			
				</tr>
				</c:forEach>
			</tbody>
		</table>
		<!-- </div>	 -->
	
				<div class="col-sm-3 pt-10 text-center center-block">
				
					<input type="button" class="btn btn-warning" value="Send Enquiry" id="saveBtnId"  onclick="validateSendEnquiry()" style="width: 139px;">
					
				</div>    
				<div class="col-sm-3 pt-10 text-center center-block">
					<input type="button" class="btn btn-warning" value="Fill Quotation" id="saveBtnId" style="width: 139px;" onclick="fillForm()">
					
				</div>  
				
				<div class="col-sm-3 pt-10 text-center center-block">
					<input type="button" class="btn btn-warning" value="View Quotation" id="saveBtnId" style="width: 139px;" onclick="ViewQuatation()">
					
				</div>  
				
				
				<div class="col-sm-3 pt-10 text-center center-block"> 
					<input type="button" class="btn btn-warning" value="Create PO" id="saveBtnId" style="width: 139px;" onclick="createPO()">
				</div>
				<div class="col-sm-3 pt-10 text-center center-block"> 
					<input type="button" class="btn btn-warning btn-bottom" value="Print Indent" id="saveBtnId" style="width: 139px;" onclick="printIndent()">
				</div>
				
				<div class="col-sm-3 pt-10 text-center center-block"> 
					<input type="button" class="btn btn-warning btn-bottom" value="view Comparison" id="saveBtnId" style="width: 139px;" onclick="viewComparision()">
				</div>
				
				<div class="col-sm-3 pt-10 text-center center-block"> 
					<input type="button" class="btn btn-warning btn-bottom" data-toggle="modal" data-target="#myModal" value="Close Indent" id="saveBtnId" style="width: 139px;" >
				</div>
	<!--  -->
		
		<!--	<div class="form-group col-xs-12" style="margin-bottom:22px;">
		</div>-->
	</div>
</div>
</div>
				
		<div class="col-md-6 no-padding-left" style="margin-top: 40px;">
			 <label class="control-label col-md-3 no-padding-left">Note :</label>
					<div class="col-md-9 no-padding-left">
						<textarea name="Note" id="NoteId" class="form-control" style="resize:vertical;" readonly="true">${requestScope['closedProducts']}</textarea> 
					</div>
			</div>
			

<!-- **************************************************************		 -->
			
		
			<br/>
			<div id="myModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
			<div class="form-group">
			  <label for="sel1">Select list:</label>
			  <select class="form-control" id="sel1" name="typeOfPurchase" style="width: 460px" onchange='CheckColors(this.value);'>
			    <option name="typeOfPurchase" value="marketPurchase">Market Purchase</option>
			    <option name="typeOfPurchase" value="localPurchase"> Local Purchase</option>
			    <option name="typeOfPurchase" value="others">others</option>
			  
			  </select>
			</div>
			<input type="text" name="typeOfPurchaseOthers" class="form-control" placeholder="Please enter the reson" id="othersContent" style='display:none;width: 460px'/>
      </div>

      <div class="modal-footer">
        <button type="button" class="btn btn-warning" data-dismiss="modal" style="margin-right: 256px;" onclick="closeIndent()">Submit</button>
      </div>
    </div>

  </div>
</div>
		
		<!-- ************************************************modal pop up for send enquiry start**************************************************** -->
		<div id="modal-modifytemPo" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title text-center">CC MAILS</h4>
      </div>
      <div class="modal-body" style="overflow:hidden;">
       <div class="col-md-12">
        <input type="text" name="ccmails" id="ccmails" value="${ccEmails}" class="form-control" />
       </div>
      </div>
      <div class="modal-footer text-center">
        <!-- <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button> -->
        <button type="button" class="btn btn-warning form-cancel" style="margin-right: 230px;width: 121px;margin-top: 47px;" data-dismiss="modal" onclick="sendEnquiry()">Submit</button>
      </div>
    </div>

  </div>
</div>
		
		<!-- ******************************************************send enquiry end*************************************************************** -->	
			
			</form:form>
		</div>
	</div>
	</div>
	</div>
	
	
					
					<!-- /page content -->        
				</div>
			
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
        <script src="js/AllIndent.js" type="text/javascript"></script>
         <script src="js/sidebar-resp.js" type="text/javascript"></script>
	
		
		<script>
		
			$(document).ready(function() {	
				$(".up_down").click(function(){ 
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
				}); 
				
			});
			var next = 2;
			$(document).ready(function(){
			    
			    $(document).on('click','.add-more',function(e){
			    	debugger;
			    	//$(this).closest('button').addClass('hide');
			        e.preventDefault();
			        var addto = "#VendorNameId" + next;
			        var addto2 = "#GSTINNumber" + next;
			        var addto3 = "#VendorAddress" + next;
			        var addRemove = "#VendorNameId" + (next);
			        var addremove2 = "#GSTINNumber" +(next);
			        var addremove3 = "#VendorAddress" + (next);
			       
                 
                
			        var newIn = '<div class="input-append vendor'+next+'"  id="profs'+next+'">'
			        +'<div class="row">'
			        +'<div id="field' + next + '" class="mydiv col-md-3">'
			        +'<div class="form-group">'
			        +'<label class="control-label col-md-6">Vendor Name : </label>'
			        +'<div class="col-md-6">'
			        +'<input autocomplete="off" class="input form-control myinputstyles Vendor-Name-Id" id="VendorNameId' + next + '" name="vendorName' + next + '" type="text" uda-form-no='+next+'>'
			        +'</div>'
			        +'</div>'
			        +'</div>'
			        +'<div id="field' + next + '" class="mydiv col-md-3">'
			        +'<div class="form-group">'
			        +'<label class="control-label col-md-6">GSTIN : </label>'			       
			        +'<div class="col-md-6">'
			        +'<input autocomplete="off" class="input form-control myinputstyles" id="GSTINNumber' + next + '" name="strGSTINNumber' + next + '" type="text" readonly>'
			        +'</div>'
			        +'</div>'
			        +'</div>'
			        +'<div id="field' + next + '" class="mydiv col-md-3">'
			        +'<div class="form-group">'
			        +'<label class="control-label col-md-6">Vendor Address : </label>'
			        +'<div class="col-md-6">'
			        +'<input autocomplete="off" class="input form-control myinputstyles" id="VendorAddress' + next + '" name="vendorAddress' + next + '" type="text" readonly>'
			        +'<input type="hidden" id="vendorId' + next + '" name="vendorId' + next + '" class="form-control"  readonly/> '
			        +'</div>'
			        +'</div>'
			        +'</div>'
			        +'<div  class="col-md-3 text-center center-block">'
			        +'<button id="b1" class="add-more mybtnstyles" type="button"><span class="glyphicon glyphicon-plus" ></span></button>'
			        +'<button id="remove" class=" remove-me mybtnstyles"  onclick="deleteVendor('+ next +')" type="button"><span class="glyphicon glyphicon-minus"></span></button>';
			        +'</div>'
			        +'</div>'
			        +'<div class="clearfix"></div>'
			        
			        var newInput = $(newIn);
			        $("#noofvendorsproccessed").val(next); 
			        /* alert(next); */
			       
			       // $(addto).after(newInput);
			       $(".input-append").last().after(newInput);
			       
			       next = next + 1;
			        
			        setvendor(next);
			        
			        
			       // $("#field"+next).after(removeButton);
			        $("#VendorNameId" + next).attr('data-source',$(addto).attr('data-source'));
			        $("#GSTINNumber" + next).attr('data-source',$(addto).attr('data-source'));
			        $("#VendorAddress" + next).attr('data-source',$(addto).attr('data-source'));
			        $("#count").val(next);  
			        
			            
			    });
			    
			    /* $('.remove-me').click(function(e){
	            	next = next - 1;
	                /* e.preventDefault();
	                var fieldNum = this.id.charAt(this.id.length-1);
	                var fieldID = "#VendorNameId" + fieldNum;
	                var fieldId2 = "#GSTINNumber" + fieldNum;
	                var fieldId3 = "#VendorAddress" + fieldNum;
	                $(this).remove();
	                $(fieldID).remove();
	                $(fieldId2).remove();
	                $(fieldId3).remove(); */
	                
	               
	           /*  }); */ 
			    setvendor(1);
			    
			});
			/* $(function(){
				var div1 = $(".right_col").height();
				var div2 = $(".left_col").height();
				var div3 = Math.max(div1,div2);
				$(".right_col").css('max-height', div3);
				$(".left_col").css('min-height', $(document).height()-65+"px");
			}); */
		/* ***************** Method for closed indents popup method*************** */
		
					function CheckColors(val){
				 var element=document.getElementById("othersContent");
				 if(val=='others')
				   element.style.display='block';
				 else  
				   element.style.display='none';
				}
		
			$("#sttle-id1").click(function(){
				var tr    = $(this).closest('tr');
				var clone = tr.clone().insertAfter("#tblnotification" ).after('</>');
				console.log(clone);
				});
			$(function(){

				 // add multiple select / deselect functionality
				 $("#selectall").click(function () {
					 $(".case").prop('checked', $(this).prop('checked'));
				  /*   $('.case').attr('checked', this.checked); */
				 });

				 // if all checkbox are selected, check the selectall checkbox
				 // and viceversa
				 $(".case").click(function(){

				  if($(".case").length == $(".case:checked").length) {
				   $("#selectall").attr("checked", "checked");
				  } else {
				   $("#selectall").removeAttr("checked");
				  }

				 });
				});
			/* $('#saveBtnId1').click(function(){
			      window.open('CreatePO.spring');
			   });*/
			
			   function deleteVendor(id){
				   debugger;
			    	var rowLengthcount = $(".input-append").length;
	                if(rowLengthcount == 1){
	                	alert("This row can't be deleted.");
	                	return false;
	                }
	                else{
	                	 $(".vendor"+id).remove();
	                }
			    	
			    }
			    function validateText(current){
				   var input=$(current).val();
				   if(isNaN(input)){
					   alert("Please enter valid data.");
					   $(current).val('');
					   $(current).focus();
					   return false;
				   }
			   }
			   var referrer="${url}";
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
