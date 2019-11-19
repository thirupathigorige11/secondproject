<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page isELIgnored="false"%>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">
<jsp:include page="CacheClear.jsp" />

</head>

<style>
.input-group[class*=col-] {
    float: none;
     padding-right: 0px !important; 
     padding-left: 0px !important; 
}
.control-label-acdc {
	margin-top: 4px;
}

.container1 {
	display: none;
}

.media-style {
	width: 39% !important;
}

@media screen and (min-width: 480px) {
	.media-style {
		width: auto;
	}
	.submitstyle {
		margin-top: 20px;
		width: 100% !important;
	}
}

.no-padding-left {
	padding-left: 0px;
}

.no-padding-right {
	padding-right: 0px;
}
.pro-table thead tr th {
border:1px solid #000;
background-color:#ddd;
}
.pro-table tbody tr td{
border:1px solid #000;
}
.pro-table{
border:1px solid #000;
}
.pro-table>thead>tr>th{
border-top:1px solid #000 !important;
}
a.ui-button{
 padding: 14px;
    margin-bottom: 4px;
}

.abc {
	color: red;
}

.btn-ward {
	height: 29px;
	width: 121px;
	color: white;
	background-color: #ef7e2d;
	position: absolute;
	margin-left: 465px;
	margin-top: 48px;
}
/
#WithoutPricingData {
	margin-top: 31px;
}

.form-content {
	margin-top: 30px;
}

.bottom-class {
	margin-top: 22px;
}

.fields-space-with {
	margin-top: 26px;
}

.fields-space {
	margin-right: 25px;
}

.fields1-space {
	margin-left: 6px;
}

.withoutPricing-class {
	margin-bottom: 10px;
}

.formShow {
	border: 1px solid #e4e2e2;
	margin-top: 35px;
	background: #fff;
	overflow:hidden;
}
.DCNumber {
	color: black;
	font-weight: bold;
}
.full-width {
	width: 100%;
}

.icons-bg {
	padding: 3px 10px;
}

.pricing-box {
	background: rgba(33, 32, 31, 0.2);
	display: flow-root;
	padding: 10px;
	border: solid 1px #000;
}
.form-dctoinvoice {
 	padding:30px;
}
.font-14{
font-size: 15px;
}
.btnaction {
    padding: 4px 8px !important;
}
.clearbtn{
    right: 2px !important;
    bottom: 3px !important;
}
</style>


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
					<jsp:include page="SideMenu.jsp" />
				</div>
			</div>
			<jsp:include page="TopMenu.jsp" />
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Inwards</a></li>
						<li class="breadcrumb-item active">Convert DC to Invoice</li>
					</ol>
				</div>
				<center><font size=5 >${Message} </font></center>
				<div class="col-md-12 inwardInvoice">
				<form id="WithoutPricingData" style="margin-bottom: 20px;" methode="POST">
					<div class="container" align="">
						<div class="form-horizontal form-dctoinvoice"
							id="withoutpricingvendorwise">
							<div class="col-md-12">
								<div class="col-md-4">
									<div class="form-group">

										<label class="control-label col-md-6 font-14"> Vendor Name :</label>
										<div class="col-md-6 no-padding-left no-padding-right">
											<input id="VendorNameId" class="form-control" placeholder="Vendor Name" autocomplete="off" onkeyup="vendorNameKeyup()"/>
											<button type="button" class="btn clearbtn vendorclearbtn" title="Clear Search" onclick="clearText('Vendor')">x</button>
										</div>
									</div>

								</div>
								<div class="col-md-4">
									<div class="form-group"> 
										<label class="control-label col-md-6 font-14">GSTIN :</label>
										<div class="col-md-6 no-padding-left no-padding-right">
											<input id="GSTINNumber" class="form-control" placeholder="GSTIN Number" autocomplete="off" readonly/>
										</div>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="control-label col-md-6 font-14">Vendor Address :</label>
										<div class="col-md-6 no-padding-left no-padding-right">
											<input id="VendorAddress" class="form-control" placeholder="Vendor Address" autocomplete="off" readonly/>
											<input type="hidden" name="siteId" id="siteId" value="${siteId}">
											<input type="hidden" name="VendorId" value="" id="vendorIdId"> 
											<input type="hidden" name="length" id="chargesLength" value="">
										</div>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group"> 
										<label class="control-label col-md-6 font-14">Invoice Number : 	</label>
										<div class="col-md-6 no-padding-left no-padding-right">
											<input id="invoice_Number" class="form-control" name="invoice_Number" placeholder="Invoice Number" onkeyup="invoice_NumberKeyup()" autocomplete="off"/>
											<button type="button" class="btn clearbtn invoiceclearbtn" title="Clear Search" onclick="clearText('Invoice')">x</button>
										</div>
									</div>
								</div>
								<div class="col-md-4">
									<div class="form-group">
										<label class="control-label col-md-6 font-14">Invoice Date :</label>
										<div class="col-md-6 input-group">
											<input id="invoice_Date" name="invoice_Date" class="form-control readonly-color" placeholder="Invoice Date" readonly="true" autocomplete="off"/>
											<label class="input-group-addon btn input-group-addon-border"  for="invoice_Date"><span class="fa fa-calendar"></span></label>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
					
						<div id="maintable" cellpadding="0" cellspacing="0" class="pdzn_tbl1">
							<div id="parent_witghoutpricing">
								<div class="withoutPricing-class pricing-box" id='1' style="padding: 15px;">
									<div class="col-md-12 no-padding-left no-padding-right">
										<div class="col-md-offset-2 col-md-7 no-padding-left no-padding-right convertdcLength">
											<div class="col-md-6 no-padding-left no-padding-right">
												<label class="DCNumber col-md-6 no-padding-left no-padding-right control-label-acdc">DC Number :</label>
												<div class="col-md-6 no-padding-left no-padding-right">
													<input class="form-control" id="DCNumber1" name="DCNumber1" onkeyup="dcNumKeyup(1)" placeholder="DC Number" type="text" onkeyup="validateDCdate(this);" autocomplete="off"/>
													<button type="button" class="btn clearbtn clearDcNum1" title="Clear Search" onclick="clearDcNum(1)">x</button>
												</div>
												<span id="errormsg1" style="display: none">*DC Date shouldn't be same</span>
											</div>
											<div class="col-md-6 no-padding-left no-padding-right">
												<label class="DCDate col-md-6 no-padding-right control-label-acdc">DC Date :</label>
												<div class="col-md-6 no-padding-left no-padding-right input-group">
													<input type="text" class="form-control Dcdate readonly-color" placeholder="DC Date" id="DCDate1" onchange="checkDcNumber(1)" name="DCDate1" readonly="true"/>
													<label class="input-group-addon btn input-group-addon-border" id="labelDate1" for="DCDate1"><span class="fa fa-calendar"></span></label>
												</div>
											</div>

										</div>
										<div class="col-md-offset-1 col-md-2">
											<button type="button" id="addNewItemBtnId1" class="btnaction add_new"><i class="fa fa-plus" 	style="font-size: 1.2em;"></i></button>
											<button type="button" id="addDeleteItemBtnId1" style="margin-left:3%;display:none;" class="btnaction"  onclick="deleteRow(1)"><i class="fa fa-trash"></i></button>
										</div>
										<input type="hidden" name="numbeOfRowsToBeProcessed" value="1" id="rowId">
									</div>
								</div>
							</div>
						</div>
						<div class="btn-submit text-center center-block">
							<button class="btn btn-warning" id="btn-submitt" type="submit" style="margin-top: 21px; margin-bottom: 20px;" onclick="return validateSubmit()">SUBMIT</button>
						</div>
					</form>
					<div id="withPricingData" style="display: none">
						<div class="container " style="background-color: #ccc;  height: auto; padding:10px; border: 1px solid #000;">
							<form id="withPricingData" class="form-horizontal withdata" action="InwardsFromDcForm.spring">
								<div class="col-md-8 col-md-offset-2">
									<div class="col-md-6">
									 <div class="form-group">
										<label class="DCNumber col-md-4">DC Number : </label>
										<div class="col-md-8">
											<input type="text" class="form-control" name="DCNumber" id="DCNumber" autocomplete="off" />
										</div>
									</div>
									</div>
									<div class="col-md-6">
									 <div class="form-group">
										<label class="DCDate col-md-4 col-xs-12">DC Date :</label>
										<div class="col-md-8 col-xs-12 input-group">
											<input type="text" class="form-control Dcdate" id="DCDate2"		name="DCDate1" />
											<label class="input-group-addon btn input-group-addon-border" id="labelDate1" for="DCDate2"><span class="fa fa-calendar"></span></label>
										</div>
									</div>
									</div>
								</div>
						</div>
						<div class="btn-submit col-md-12 text-center center-block">
							<button class="btn btn-warning" type="submit" style="margin-top: 21px;">SUBMIT</button>
						</div>
						<input type="hidden" name="VendorIdDCSearch" value="" id="VendorIdDCSearch">
						
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>
	</div>
	</div>


	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/CovertDCTOInvoice.js" type="text/javascript"></script>
	<script src="js/sidebar-resp.js" type="text/javascript"></script>

	<script>
		$(document).ready(function() {
			$(".up_down").click(function() {
					$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
					$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
			});
			//$(".clearDcNum").show();
		});

		

		/* Method for button show and show function
		 */

		$(document).ready(function() {
			$("#withoutPricing").click(function() {
				$("#withPricingData").hide();

				//$("#temporary-class").show();
				$("#WithoutPricingData").show();
				$("#withoutpricingvendorwise").show();

			});
			$("#withPricing").click(function() {
				$("#withPricingData").show();
				$('#withoutpricingvendorwise').show();
				$("#WithoutPricingData").hide();

				//$("#temporary-class").hide();
				$("#cantainer").hide();
			});

		});

		/* Method for toggle the search button */
		$(document).on("click", ".viewDCForm", function() {
			/*  $(".viewDCForm").parent().parent().find(".formShow").toggle();   */
			$(this).parent().parent().parent().find(".formShow").slideToggle('slow');
		});

		$(document).on("click", ".add_new",function(rowId){
			 debugger;
			 var regex = /^(.+?)(\d+)$/i;
			 var cloneIndex = $("#parent_witghoutpricing").length;
			    var rowId =  parseInt($(this).closest('.withoutPricing-class').attr('id'));
				 var DCNum = "DCNumber"+rowId;
				 var DCInvoice = "DCInvoice"+rowId;
				 var DCdate = "DCDate"+rowId;
				
					  var DCNumval = document.getElementById(DCNum).value;
					  if(DCNumval == "" || DCNumval == null || DCNumval == '') {
							alert("Please enter DC Number");
							document.getElementById(DCNum).focus();
							return false;
					  } 
					  var DCdateval = document.getElementById(DCdate).value;
					  if(DCdateval == "" || DCdateval == null || DCdateval == '') {
							alert("Please enter DC Date");
							document.getElementById(DCdate).focus();
							return false;
					  }
					  var newRowId = rowId+1;
					  
					 var str='<div class="withoutPricing-class pricing-box" id="'+newRowId+'" style="padding: 15px;">'
					 	 str+='<div class="col-md-12 no-padding-left no-padding-right">'
					 	 str+='<div class="col-md-offset-2 col-md-7 no-padding-left no-padding-right convertdcLength">'
					 	 str+='<div class="col-md-6 no-padding-left no-padding-right">'
					 	 str+='<label class="DCNumber col-md-6 no-padding-left no-padding-right control-label-acdc">DC Number :</label>'
					 	 str+='<div class="col-md-6 no-padding-left no-padding-right">'
					 	 str+='<input class="form-control" id="DCNumber'+newRowId+'" name="DCNumber'+newRowId+'" onkeyup="dcNumKeyup('+newRowId+')" placeholder="DC Number" type="text" onkeyup="validateDCdate(this);" autocomplete="off"/><button type="button" class="btn clearbtn clearDcNum'+newRowId+'" title="Clear Search" onclick="clearDcNum('+newRowId+')">x</button>'
					 	 str+='</div>'
					 	 str+='<span id="errormsg'+newRowId+'" style="display: none">*DC Date shouldnt be same</span>'
					 	 str+='</div>'
					 	 str+='<div class="col-md-6 no-padding-left no-padding-right">'
					 	 str+='<label class="DCDate col-md-6 no-padding-right control-label-acdc">DC Date :</label>'
					 	 str+='<div class="col-md-6 no-padding-left no-padding-right input-group">'
					 	 str+='<input type="text" class="form-control Dcdate readonly-color" placeholder="DC Date" id="DCDate'+newRowId+'" name="DCDate'+newRowId+'" onchange="checkDcNumber('+newRowId+')" readonly="true"/>'
					 	 str+='<label class="input-group-addon btn input-group-addon-border" id="labelDate'+newRowId+'" for="DCDate'+newRowId+'"><span class="fa fa-calendar"></span></label>'
					 	 str+='</div>'
					 	 str+='</div>'
					 	 str+='</div>'
					 	 str+='<div class="col-md-offset-1 col-md-2">'
					 	 str+='<button type="button" id="addNewItemBtnId'+newRowId+'" class="btnaction add_new"><i class="fa fa-plus" 	style="font-size: 1.2em;"></i></button>'
					 	 str+='<button type="button" id="addDeleteItemBtnId'+newRowId+'" style="margin-left:3%;" class="btnaction"  onclick="deleteRow('+newRowId+')"><i class="fa fa-trash"></i></button>'
					 	 str+='</div>'
					 	 str+='<input type="hidden" name="numbeOfRowsToBeProcessed" value="'+newRowId+'" id="rowId">'
					 	 str+='</div>'
					 	 str+='</div>' 
					  
					  $("#parent_witghoutpricing").append(str);
					  $("#addNewItemBtnId"+rowId).hide();
					  var length=$(".pricing-box").length;
					  if(length==2){
						  $("#addDeleteItemBtnId"+rowId).show();
					  }
					  $("#DCDate"+newRowId).datepicker({ 
					  	   dateFormat: 'dd-M-y',
					   	   maxDate: new Date(),
					       changeMonth: true,
					       changeYear: true
					    });
			});

			 $(function() {
				  $(".Dcdate").datepicker({ 
					  dateFormat: 'dd-M-y',
					  maxDate: new Date(),
				      changeMonth: true,
			         changeYear: true
				  });
			 });
			 $(function() {
				  $("#invoice_Date").datepicker({ 
					  dateFormat: 'dd-M-y',
					  maxDate: new Date(),
				      changeMonth: true,
			          changeYear: true
				  });
			 });
			 
			 
	 
	
function validateSubmit() {debugger;
	var VendorNameId=$("#VendorNameId").val();
	var gstin=$("#GSTINNumber").val();
	var address=$("#VendorAddress").val();
	var chargesRowCountNum=[];
	$(".pricing-box").each(function(){debugger;
	var currentId=$(this).attr("id");
	chargesRowCountNum.push(currentId);
	});
$('#chargesLength').val(chargesRowCountNum);	
	if(VendorNameId==null || VendorNameId=='')
	{
		 alert('Please enter vendor name.');
		return false;
	}if(gstin==null || gstin==''){
		 alert('Please enter valid vendor name.');
		 $("#VendorNameId").focus();
			return false;
	}
	var error=true;
	var length=$(".convertdcLength").length;
	$(".pricing-box").each(function(){
		var i=$(this).attr("id");
		var DCNumber=$("#DCNumber"+i).val();
		var DCDate=$("#DCDate"+i).val();
		 if(DCNumber==null || DCNumber==''){
			 alert('Please enter dc number.');
			 $("#DCNumber"+i).focus();
		    	return error=false;
		}
		 if(DCDate==null || DCDate==''){
			 alert('Please enter dc date.');
			 $("#DCDate"+i).focus();
		    	return error=false;
		}
	})
	if(error==false){
		return false;
	}
	
		//$(".loader-sumadhura").show();
		document.getElementById("WithoutPricingData").action = "CovertDcWithoutPricing.spring";
		document.getElementById("WithoutPricingData").method = "POST";
		document.getElementById("WithoutPricingData").submit();
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

	function invoice_NumberKeyup(){
		 var invoice_Number = $("#invoice_Number").val().trim();
			if(invoice_Number == ""){
				$(".invoiceclearbtn").hide();
			}else{
				$(".invoiceclearbtn").show();
			}
	}
	function clearText(type){
		if(type=="Vendor"){
			$("#VendorNameId").val("");
			$("#GSTINNumber").val("");
			$("#VendorAddress").val("");	
			$(".vendorclearbtn").hide();
		}else{
			$("#invoice_Number").val("");
			$(".invoiceclearbtn").hide();
		}
	}
	function clearDcNum(id){
		$("#DCNumber"+id).val("");
		$(".clearDcNum"+id).hide();
	}
	function dcNumKeyup(id){
		var dcNum=$("#DCNumber"+id).val();
		if(dcNum == ""){
			$(".clearDcNum"+id).hide();
		}else{
			$(".clearDcNum"+id).show();
		}
	}

</script>
</body>
</html>
