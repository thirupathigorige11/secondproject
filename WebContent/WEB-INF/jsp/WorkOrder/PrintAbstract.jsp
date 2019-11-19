<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet" type="text/css">
<link href="css/custom.css" rel="stylesheet" type="text/css">
<link href="css/loader.css" rel="stylesheet" type="text/css">
<link href="css/topbarres.css" rel="stylesheet" type="text/css">

<jsp:include page="./../CacheClear.jsp" />
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<script src="js/sidebar-resp.js" type="text/javascript"></script>
<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
<style>
@media print{
@page {size: landscape}
 .btn-custom-width{display:none;}
}
/* fixed header table css for modal*/
.scroll-rabill-tbl{
 width: calc(100% - 17px) !important;
 height:70px;
}.scroll-rabill-tbl1{
/*  width: calc(100% - 17px) !important; */
 height:250px;
}
 .fixed-header-tbl{
 width:100%;
 border:1px solid #000;
 }
 .fixed-header-tbl tbody tr td{
 padding:5px;
 border:1px solid #000;
 }

.abc {
	color: red;
}
.btn-ward{
  	height: 29px;
    width: 121px;
    color: white;
    background-color: #ef7e2d;
    position: absolute;
    margin-left: 465px;
    margin-top: 48px;

}
.inward-invoice{
	color: #726969;
    margin-left: 377px;
    font-size: 24px;

}
.media-style{
width:39% !important;

}
.submitstyle{
margin-top: 20px;
width: 23% !important;
}
@media screen and (min-width: 480px) {
    .media-style {
       width:auto;
    }
    .submitstyle{
margin-top: 20px;
width: 100% !important;
}
}
.td-active input{
background-color:#dbecf6;
}
.td-active{
background-color:#dbecf6;
}
.td-active input:focus{

}
.td-active input{
font-weight:bold;
}
.border-none input{
border:none !important;
}
.border-none{border:none;}
.input-outline input{
outline:none;
}
.table-border-certificateofpayment thead tr th, .table-border-certificateofpayment tbody tr td{
 border:1px solid #000;
 font-weight: bold;
}
.table-border-certificateofpayment{
border:1px solid #000;

}
.mrg-Top{
margin-top:5px;
}
@media print{
  .breadcrumb, .nav_menu, .showraadvstatusBtn, .tbl-raadvstatus, .payment-areaprint{ display:none; }
  thead { display: table-row-group;}
  .border-none{ border:none; }
}
</style>
</head>
<body class="nav-md">
      <div class="overlay_ims"></div>
	  <div class="loader-ims" id="loaderId" > <!--  -->
			<div class="lds-ims">
				<div></div><div></div><div></div><div></div><div></div><div></div></div>
			<div id="loadingimsMessage">Loading...</div>
	 </div>
	 <div class="container body">
		<div class="main_container" id="main_container">
			<div class="col-md-3 left_col" id="left_col">
				<div class="left_col scroll-view">
					<div class="clearfix"></div>
					<jsp:include page="../SideMenu.jsp" />
				</div>
			</div>
			<jsp:include page="../TopMenu.jsp" />
			<div class="right_col" role="main">
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item"><a href="#">View Contractor Bill Status</a></li>
						<li class="breadcrumb-item active">Print Abstract</li>
					</ol>
				</div>
				<input type="hidden" name="workOrderNo" id="workOrderNo" value="${billBean.workOrderNo}">
				<input type="hidden" name="contractorId" id="contractorId" value="${contractorId}">
			    <input type="hidden" name="approvePage" id="approvePage" value="true">
			    <input type="hidden" name="workOrderNo" id="workOrderNo" value="${workOrderNo}">
			    <input type="hidden" name="site_id" id="site_id" value="${billBean.siteId}">
			    <input type="hidden" name="tempBillNo" id="tempBillNo" value=" ${tempBillNo } " readonly="readonly">
			    <input type="hidden" name="billType" id="billType" value="${billType}">
			    <input type="hidden"  id="ContractorId" name="ContractorId" value="${contractorId }">
			    <input type="hidden" id="billDate" value="${billBean.paymentReqDate}">
			    <input type="hidden" name="nextLevelApprovelEmpID" id="nextLevelApprovelEmpID" value="${nextLevelApprovelEmpID}">
			    
			    
			    
       <form:form   modelAttribute="billBean"   id="contractorRABill" class="form-horizontal" ><div class="col-md-12">
    
         <table style="margin-bottom: -1px;border:1px solid #000;width:100%;font-weight:bold;">
          <tbody>
            <tr>
              <td rowspan="2" style="width:20%;border:1px solid #000;padding:5px;"><img src="images/SumadhuraLogo2015.png" class="img-responsive center-block" style="width: 100px;" /></td>
              <td  style="width:80%;border:1px solid #000;padding:5px;" class="text-center bck-ground"><h4><strong>SUMADHURA INFRACON PVT LTD </strong></h4></td>
            </tr>
             <c:set value="ADV" var="ADV"></c:set>
            <tr><td colspan="2" style="width:80%;border:1px solid #000;padding:5px;" class="text-center bck-ground"><h5>
              <strong  style="">Abstract</strong></h5></td>
            </tr>
         	</tbody>
          </table>
          
          <table  style="margin-bottom: -1px;width:100%;font-weight:bold;" >
          <tbody>
            <tr>
              <td style="width:50%;border:1px solid #000;padding:7px;">
              <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>Contractor&nbsp;Name</strong></div><div class=" mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top" style="width:45%;float:left;">
              <label id="ContractorName">${billBean.contractorName}</label>
              <input type="hidden"  id="ContractorId" name="ContractorId" value="${billBean.contractorId }"   /></div></div>
			  <div class="clearfix"></div>
			   <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>Pan Card No</strong> </div><div class=" mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top" style="width:45%;float:left;" id="">${billBean.contractorPanNo}</div></div>
			  <div class="clearfix"></div>
			  
			  <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>Account No</strong> </div><div class=" mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top" style="width:45%;float:left;" id="">${billBean.contractorBankAccNumber}</div></div>
			  <div class="clearfix"></div>
              <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;"><strong>IFSC & Bank Name</strong>  </div><div class=" mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top" style="width:45%;float:left;" id="">
                 <c:set value="-" var="delimiter"></c:set>
              
              <c:choose>
              <c:when test="${billBean.contractorIFSCCode eq delimiter}">${billBean.contractorBankName}</c:when>
              <c:when test="${billBean.contractorBankName eq delimiter}">${billBean.contractorIFSCCode}</c:when>
              <c:when test="${billBean.contractorIFSCCode eq  delimiter && billBean.contractorBankName eq delimiter}">-</c:when>
              <c:when test="${billBean.contractorIFSCCode ne  delimiter && billBean.contractorBankName ne delimiter}"> ${billBean.contractorIFSCCode}&nbsp; <span>&</span> &nbsp;${billBean.contractorBankName}</c:when>
              </c:choose></div></div>
			  <div class="clearfix"></div>
              <div class=" mrg-Top" style="width:100%;"><div class="  mrg-Top" style="width:45%;float:left;" ><strong>Mobile No  </strong></div><div class=" mrg-Top" style="width:10%;float:left;"><strong>:</strong></div><div class=" mrg-Top" style="width:45%;float:left;" id=""> ${billBean.contractorPhoneNo}</div></div>
			  <div class="clearfix"></div> 
             
              </td>
              <td style="width:50%;border:1px solid #000;padding:7px;">
             
              <div class="" style="width:100%;">
			  <div class=" mrg-Top"  style="width:45%;float:left;">Project</div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;"> <strong  style=""><input type="text" value="${billBean.siteName}" style="border:none;" readonly /></strong>  <input type="hidden"  name = "siteId" id = "siteId" value="${SiteId}"  /></div>
			  </div>

			
			    <div class="" style="width:100%;">
			    <div class=" mrg-Top"  style="width:45%;float:left;">Work order No</div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;">
			    
			    <input type="hidden" name="nextLevelApprovelEmpID" id="nextLevelApprovelEmpID" value="${nextLevelApprovedId}">
			    <input type="hidden" name="approvePage" id="approvePage" value="${approvePage}">
			    <input type="hidden" name="raPage" id="raPage" value="${raPage}">
			     <input type="hidden" name="advPage" id="advPage" value="false">
			    <input type="hidden" name="billType" id="billType" value="${billType}">
			    <input type="hidden" name="tempBillNo" id="tempBillNo" value=" ${billBean.tempBillNo } " readonly="readonly">
			    <input type="hidden" name="isRAStatusPage" id="isRAStatusPage" value=" ${isRAStatusPage} " readonly="readonly">
			    <input type="hidden" name="isAdvStatusPage" id="isAdvStatusPage" value=" ${isAdvStatusPage} " readonly="readonly">
			   
			   <label id="workOrderNo">${billBean.workOrderNo}</label>
			   
			    </div>
			    </div>
              	<div class="" style="width:100%;">
	             <div class=" mrg-Top"  style="width:45%;float:left;">Type Of Work </div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;">
	                <label id="typeOfWork" style="float: left;word-break: break-all;"></label>
             </div>
              </div>
              	<div class="" style="width:100%;">
              	 	<div style="width:45%;float:left;" class=" mrg-Top">Bill No </div><div class="mrg-Top" style="width:10%;float:left;">:</div><div style="width:45%;float:left;" class="mrg-Top"><input type="text" id="billNo"  class="border-none" style="display:inline;"  value=" ${billBean.billNo }" readonly="readonly"/></div>
              	</div>
              	<div class="" style="width:100%;">
              	<div class=" mrg-Top"  style="width:45%;float:left;">WO Amount </div><div class=" mrg-Top"  style="width:10%;float:left;">:</div><div class=" mrg-Top"  style="width:45%;float:left;">
              <span id="totalAmtToPay">${billBean.totalAmount}</span>
          
              	</div>
                </div>
                
              </td>
            </tr>
          </tbody>
        </table>
        <table style="margin-bottom:10px;border:1px solid #000;width:100%;font-weight:bold;">
        <thead>
         <tr>
             <th rowspan="2" class="vertical-alignment" style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Description of Work</th>
              <th rowspan="2" class="vertical-alignment" style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Total Quantity</th>
              <th rowspan="2" class="vertical-alignment" style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Rate</th>
              <th rowspan="2" class="vertical-alignment" style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Unit</th>
              <th colspan="2" style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Cumulative Certified</th>
              <th colspan="2" style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Previous Certified</th>
              <th colspan="2" style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Current Certified</th>
            </tr>
            <tr>
              <th style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Quantity </th>
              <th style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Amount</th>
            
              <th style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Quantity </th>
              <th style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Amount</th>
            
              <th style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Quantity </th>
              <th style="border:1px solid #000;background-color:#ccc;padding:5px;text-align:center;">Amount</th>
            </tr>
        </thead>      
        <tbody id="paymentByArea" >

        </tbody>
       </table>
        <div class="clearFix"></div>
        <div class="col-md-12 text-center center-block" style="margin-top: 10px;">
         <input type="button" class="btn btn-warning btn-custom-width" id=printAbstractjsp"	onclick="printAbstractjsp()" value="Print" />
       </div> 
 </form:form>
 </div>
 
<script src="js/jquery-ui.js"></script>
<script src="js/custom.js"></script>

<c:set value="END" var="BillStatus"/>
<script type="text/javascript">

	function closeView(){
		goBack() ;
	}
	function goBack() {
	    window.history.back();
	}
    $(document).ready(function(){
	  loadContractorData();
	   setTimeout( function(){
			workOrderNumChanged();
	   }, 500) 
	  //loading data
	   function workOrderNumChanged(){
			var ContractorId=$("#ContractorId").val();
			var ContractorName=$("#ContractorName").val();
			var workOrderNo=$("#workOrderNo").val();
			var billDate=$("#billDate").val();			
			var billType=$("#billType").val();
			var approvePage=$("#approvePage").val();
			var tempBillNo=$("#tempBillNo").val();
			var site_id=$("#site_id").val();
			var billNo=$("#billNo").val();
			var d = new Date();
			var n = d.toLocaleDateString();
			
			var nextLevelApprovelEmpID=$("#nextLevelApprovelEmpID").val();
			var Url="";
			 if(nextLevelApprovelEmpID=="END"){
				Url= "loadPermanentWorkOrderArea.spring";
			
			}else{ 
				Url="loadWorkOrderArea.spring";
			}
			 
			$.ajax({
				url : Url,
				type : "GET",
				data : {
					contractorId : ContractorId,
					workOrderNo:workOrderNo,
					approvePage:approvePage,
					tempBillNo:tempBillNo,
					billType:billType,
					site_id:site_id,
					billNo:billNo
				},
				success : function(data	) {
					str="";
					var majorHeadNames=new Array();
					var i=1;
					var basic_amount=0;
					var price=0;
					var area_alocated_qty=0;
					var majorHeadName="";
					var minorHeadName="";
					var workDescName="";
					var majorHeadLength=1;
					var textHeading=65;
					var increIndex=1;
					var workdescIncre=1;					
					var sumOfTotalQunatity=0;
					var sumOfPrevQty=0;
					var sumOfCumulativeQTy=0;
					var sumOfPrevAmount=0;
					var sumOfCumulativeAmount=0;
					var sumOfAllocatedQuantity=0;
					var sumOfCurrentAmount=0;
					$.each(data,function(key,value){
						basic_amount=(value.TOTAL_WO_AMOUNT==null?"0":value.TOTAL_WO_AMOUNT);
					    var blockName=(value.BLOCK_NAME==null?"-":value.BLOCK_NAME);
					    var floorName=(value.FLOOR_NAME==null?"-":value.FLOOR_NAME);
					    var flatName=(value.FLAT_ID==null?"-":value.FLAT_ID);
				
					   price=value.PRICE;
					   area_alocated_qty+=value.AREA_ALOCATED_QTY;
					   if(approvePage=="true"){
					 	var htmlData="";
						htmlData+="<input type='hidden'  name='actualWorkAreaId' id='actualWorkOrderAreaId' style='border:none; text-align:center;' value='"+value.WO_WORK_AREA_ID+"' readonly/>";
						htmlData+="<input type='hidden'  name='actualQty' id='actualQty' style='border:none; text-align:center;' value='"+value.ALLOCATED_QTY+"' readonly/>";
						$("#appendBillDetails").append(htmlData);
			
						var pravArea=value.PREVAREA==""?0:value.PREVAREA;
						pravArea=pravArea==null?0:pravArea;
						//cheking is mejor head name is already printed or not 
						if(majorHeadName!=value.WO_MAJORHEAD_DESC){
							majorHeadLength++;
							//stroing major head name and work desc in temp variable
							majorHeadName=value.WO_MAJORHEAD_DESC;
							workDescName=value.WO_WORK_DESCRIPTION;
							minorHeadName=value.WO_MINORHEAD_DESC;
							majorHeadNames.push(value.WO_MAJORHEAD_DESC);
							increIndex=1;
							str+=" <tr> " +
							"  <td class='text-center' style='border:1px solid #000;padding:5px;width:271px;'word-break: break-all;' colspan='10'><strong>"+value.WO_MAJORHEAD_DESC+"</strong></td>" +
			 				"  </tr>";

							str+=" <tr> " +
							"  <td class='text-center' style='border:1px solid #000;padding:5px;width:271px;'word-break: break-all; ' colspan='10'><strong>"+value.WO_MINORHEAD_DESC+"</strong></td>" +
							"  </tr>";
							
							str+=" <tr> " +
								"  <td class='text-center'style='border:1px solid #000;padding:5px;word-break: break-all;' colspan='10'>"+value.WO_WORK_DESCRIPTION+"</td>" +
								"   </tr>";
						//checking is work description is changed or not if changed execute this block to print the work description
						}else if(minorHeadName!=value.WO_MINORHEAD_DESC){
							minorHeadName=value.WO_MINORHEAD_DESC;
							
							str+=" <tr> " +
							"  <td class='text-left'style='border:1px solid #000;padding:5px;width:271px;'word-break: break-all;' colspan='10'><strong>"+value.WO_MINORHEAD_DESC+"</strong></td>" +
							"  </tr>";
						}
						
						
						if(workDescName!=value.WO_WORK_DESCRIPTION){
							//storing work description name in another temp veriable for next time checking work desc is same or diff you can use debugger to know how it is printing
							
							workDescName=value.WO_WORK_DESCRIPTION;
							str+=" <tr> " +
							"  <td class='text-center'style='border:1px solid #000;padding:5px;word-break: break-all;' colspan='10'>"+value.WO_WORK_DESCRIPTION+"</td>" +
							"   </tr>";
						}
						var flatName=value.NAME==null?"-":value.NAME;
						var floorName=value.FLOOR_NAME==null?"-":value.FLOOR_NAME;
						var txtid=value.WO_WORK_AREA_ID;
						
						var blockNames='';
						if(value.FLATNAME!=null){
							blockNames=blockNames+" , "+value.FLATNAME;
						}
						if(value.FLOOR_NAME!=null){
							blockNames=blockNames+" , "+value.FLOOR_NAME;
						}
						
						var currentQuantity=value.ALLOCATED_QTY==""?0:parseFloat(value.ALLOCATED_QTY);
						var pravArea=0;
						var prevAreaQuantity=new Array();
						var lengthOfThePrevArea=0;
						var customMsg="";
						var previousBillAmount=0;

						var multiplyArea=(parseFloat(pravArea)+currentQuantity);
						
						// 
						//cheking is this ra bill is completed or not 
						debugger;
//==================================================Completed Bill's===========================================================================
						if(nextLevelApprovelEmpID=="END"){
							var pravArea=0;
							var prevAreaQuantity=new Array();
							var lengthOfThePrevArea=0;
							try {
								var index=value.PREVAREA.search("@@");
								if(index>=0){
									prevAreaQuantity=value.PREVAREA.split("@@");
									for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
										let tempQtyAndRate = prevAreaQuantity[ind].split("-");
										
										lengthOfThePrevArea++;
										pravArea+=parseFloat(tempQtyAndRate[0]);
										var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
										if(ind<prevAreaQuantity.length-1){
											customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+"),";	
										}else{
											customMsg+="(Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+")";
										}
										previousBillAmount+=tempQtyAndRate[0]*rate;
									}
								}else{
									if(billType==value.PREVAREA.split("-")[1]){
										let tempQtyAndRate = value.PREVAREA.split("-");
										pravArea+=parseFloat(tempQtyAndRate[0]);
										var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
										previousBillAmount+=tempQtyAndRate[0]*rate;
									}else{
										RA_amountTotalQTY+=parseFloat(value.PREVAREA.split("-")[0]);
										let tempQtyAndRate = value.PREVAREA.split("-");
										
										pravArea+=parseFloat(tempQtyAndRate[0]);
										var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
										previousBillAmount+=tempQtyAndRate[0]*rate;
									}
								}
							} catch (e) {
								console.log(e);
							}
						
							var cumulativeQuantity=0;
							var currentQuantity=parseFloat(value.ALLOCATED_QTY==undefined?0:value.ALLOCATED_QTY);
							var previousQuantity=pravArea;
						
							multiplyArea=(previousQuantity+currentQuantity);
							
							 sumOfPrevQty+=+previousQuantity;
							 sumOfCumulativeQTy+=+multiplyArea;
							 sumOfPrevAmount+=+(previousBillAmount);
							 sumOfCumulativeAmount+=+(currentQuantity*value.PRICE+previousBillAmount);
							 sumOfTotalQunatity+=+value.AREA_ALOCATED_QTY;
							 
							 sumOfAllocatedQuantity+=+currentQuantity;
							 sumOfCurrentAmount+=+(currentQuantity*value.PRICE);
							str+=" <tr> " ;
							if(lengthOfThePrevArea>=2)
								str+="  <td class='text-left'style='border:1px solid #000;padding:5px;;word-break: break-all;'>"+value.BLOCK_NAME+blockNames+"<br>"+customMsg+"<input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY"+i+"' id='ActualQTY"+i+"'>  </td>" ;
							else
								str+="  <td class='text-left'style='border:1px solid #000;padding:5px;;word-break: break-all;'>"+value.BLOCK_NAME+blockNames+"<input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY"+i+"' id='ActualQTY"+i+"'>  </td>" ;
							
							str+="  <td class='text-right'style='border:1px solid #000;padding:5px;' ><span id='TotalQty"+i+"'>"+value.AREA_ALOCATED_QTY+"</span> </td>" +// <input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY'>
								"  <td class='text-right'style='border:1px solid #000;padding:5px;' ><span id='TotalRate"+i+"'>"+value.PRICE+"</span> </td>" +//<input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.WO_WORK_AREA_ID+"' name='ActualQTY'>
								"  <td class='text-right'style='border:1px solid #000;padding:5px;' ><span id='Unit"+i+"'>"+value.WO_MEASURMEN_NAME+"</span></td>" +
								"  <td class='text-right'style='border:1px solid #000;padding:5px;'> <span id='CBQty"+i+"'>"+multiplyArea.toFixed(2)+"</span> <input type='hidden' name='workAreaId"+i+"' id='workAreaId"+i+"' value='"+value.WO_WORK_AREA_ID+"'></td>" +
								"  <td class='text-right'style='border:1px solid #000;padding:5px;'><span id='CBAmount"+i+"'>"+((currentQuantity*value.PRICE+previousBillAmount).toFixed(2))+"</span><input type='hidden' name='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' id='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' value='"+value.WO_WORK_ISSUE_AREA_DTLS_ID+"'></td>" +
								"  <td class='text-right'style='border:1px solid #000;padding:5px;'><span id='PCQty"+i+"'>"+(previousQuantity.toFixed(2))+"</span></td>" +
								"  <td class='text-right'style='border:1px solid #000;padding:5px;'><span id='PCAmount"+i+"'>"+((previousBillAmount).toFixed(2))+"</span></td>" +
								"   <td class='text-right'style='border:1px solid #000;padding:5px;'><span>"+currentQuantity+"</span> </td>" +
								"   <td class='text-right'style='border:1px solid #000;padding:5px;'><span id='CCAmount"+i+"'>"+(currentQuantity*value.PRICE)+"</span></td>"+
								"   </tr>";

					}else{//status page
//========================================================Uncompleted Bills===============================================================================					
						
						var customMsg="";
						var previousBillAmount=0;
						var RA_amountTotalQTY=0;
						
						try {
							var index=value.PREVAREA.search("@@");
							if(index>=0){
								prevAreaQuantity=value.PREVAREA.split("@@");
								for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
									let array_element = prevAreaQuantity[ind];
									
									if(billType=="ADV"){
										let tempQtyAndRate=array_element.split("-");
										if(tempQtyAndRate[1]=="ADV"){
											pravArea+=parseFloat(tempQtyAndRate[0]);
											var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
											previousBillAmount+=tempQtyAndRate[0]*rate;
											if(ind<prevAreaQuantity.length-1){
												customMsg+="("+tempQtyAndRate[1]+" - Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+"),";	
											}else{
												customMsg+="("+tempQtyAndRate[1]+" - Qty = "+tempQtyAndRate[0]+" , Rate = "+tempQtyAndRate[2]+")";
											}
										}else 	if(tempQtyAndRate[1]=="RA"){
											debugger;
											RA_amountTotalQTY+=parseFloat(tempQtyAndRate[0]);
										}
										lengthOfThePrevArea++;
									}else{
										var tempQtyAndRate=array_element.split("-");
										pravArea+=parseFloat(tempQtyAndRate[0]);//array position (0) getting prev QTY
										var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate 
										
										previousBillAmount+=tempQtyAndRate[0]*rate;
										if(ind<prevAreaQuantity.length-1){
											customMsg+="(Qty = "+inrFormat(tempQtyAndRate[0])+" , Rate = "+tempQtyAndRate[2]+"),";	
										}else{
											customMsg+="(Qty = "+inrFormat(tempQtyAndRate[0])+" , Rate = "+tempQtyAndRate[2]+")";
										}
										lengthOfThePrevArea++;
									}
								}

							}else{
								if(billType==value.PREVAREA.split("-")[1]){
									let tempQtyAndRate = value.PREVAREA.split("-");
									pravArea+=parseFloat(tempQtyAndRate[0]);
									var rate=parseFloat(tempQtyAndRate[2]);//array position (1) getting prev Rate
									previousBillAmount+=tempQtyAndRate[0]*rate;
								}else{
									RA_amountTotalQTY+=parseFloat(value.PREVAREA.split("-")[0]);									
								}
							}
						} catch (e) {
							console.log(e);
						}
						
						debugger;
						currentQuantity=value.ALLOCATED_QTY==undefined?0:parseFloat(value.ALLOCATED_QTY);
						     var multiplyArea=(pravArea+currentQuantity);
						
							 sumOfTotalQunatity+=+value.AREA_ALOCATED_QTY;
							 sumOfCumulativeQTy+=+multiplyArea;
							 sumOfCumulativeAmount+=+(previousBillAmount+(currentQuantity*value.PRICE));
							 sumOfPrevQty+=+pravArea;
							 sumOfPrevAmount+=+(previousBillAmount);
							
							 sumOfAllocatedQuantity+=+currentQuantity;
							 sumOfCurrentAmount+=+(currentQuantity*value.PRICE);
						str+=" <tr> " +
								"  <td class='text-left'style='border:1px solid #000;padding:5px;word-break: break-all;'>"+value.BLOCK_NAME+blockNames+"    <br>"+customMsg+"<input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY"+i+"' id='ActualQTY"+i+"'>  </td>" +
								"  <td class='text-right'style='border:1px solid #000;padding:5px;' ><span id='TotalQty"+i+"'>"+value.AREA_ALOCATED_QTY+"</span> </td>" +// <input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY'>
								"  <td class='text-right' style='border:1px solid #000;padding:5px;'><span id='TotalRate"+i+"'>"+value.PRICE+"</span> </td>" +//<input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.WO_WORK_AREA_ID+"' name='ActualQTY'>
								"  <td class='text-right' style='border:1px solid #000;padding:5px;'><span id='Unit"+i+"'>"+value.WO_MEASURMEN_NAME+"</span></td>" +
								"  <td class='text-right' style='border:1px solid #000;padding:5px;'><span id='CBQty"+i+"'>"+(multiplyArea)+"</span> <input type='hidden' name='workAreaId"+i+"' id='workAreaId"+i+"' value='"+value.WO_WORK_AREA_ID+"'></td>" +
								"  <td class='text-right' style='border:1px solid #000;padding:5px;'><span id='CBAmount"+i+"'>"+(currentQuantity*value.PRICE+previousBillAmount)+"</span><input type='hidden' name='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' id='WO_WORK_ISSUE_AREA_DTLS_ID"+i+"' value='"+value.WO_WORK_ISSUE_AREA_DTLS_ID+"'></td>" +
								"  <td class='text-right' style='border:1px solid #000;padding:5px;'><span id='PCQty"+i+"'>"+(pravArea)+"</span></td>" +
								"  <td class='text-right' style='border:1px solid #000;padding:5px;'><span id='PCAmount"+i+"'>"+((previousBillAmount))+"</span></td>" +
								"   <td class='text-right' style='border:1px solid #000;padding:5px;'><span>"+currentQuantity+"</span></td>" +
								"   <td class='text-right' style='border:1px solid #000;padding:5px;'><span id='CCAmount"+i+"'>"+(currentQuantity*value.PRICE)+"</span></td>"+
								"   </tr>";
						}
					}//Approve Page True Condition
					i++;	
					});
					
					str+=" <tr>  " +
						"  <td style='border:1px solid #000;padding:15px;'class='text-left' colspan='10'> </td>" +
						"   </tr>";
					str+=" <tr style='background-color:#ddd;'>  " +
						"  <td style='border:1px solid #000;padding:5px;'class='text-left' ><strong>Total Amount/Quantity</strong> </td>" +
						"  <td style='border:1px solid #000;padding:5px;'class='text-right' ><span style='display:none;'>"+sumOfTotalQunatity.toFixed(2)+"</span></td>" +// <input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' name='ActualQTY'>
						"  <td style='border:1px solid #000;padding:5px;' class='text-right' > </td>" +//<input type='text' class='form-control' value='"+value.AREA_ALOCATED_QTY+"' id='"+value.WO_WORK_AREA_ID+"QTY'><input type='hidden' class='form-control' value='"+value.WO_WORK_AREA_ID+"' name='ActualQTY'>
						"  <td style='border:1px solid #000;padding:5px;' class='text-right' ></td>" +
						"  <td style='border:1px solid #000;padding:5px;' class='text-right'><span id='cumulativeQuantity'  style='display:none'>"+sumOfCumulativeQTy.toFixed(2)+"</span></td>" +
						"  <td style='border:1px solid #000;padding:5px;' class='text-right'><span id='cumulativeAmount'>"+sumOfCumulativeAmount.toFixed(2)+"</span></td>" +
						"  <td style='border:1px solid #000;padding:5px;' class='text-right'><span id='previousQuantity'  style='display:none'>"+sumOfPrevQty.toFixed(2)+"</span></td>" +
						"  <td style='border:1px solid #000;padding:5px;' class='text-right'><span id='previousAmount'>"+sumOfPrevAmount.toFixed(2)+"</span></td>" +
						"   <td style='border:1px solid #000;padding:5px;' class='text-right'><span id='currentQuantity'  style='display:none'>"+sumOfAllocatedQuantity.toFixed(2)+"</span></td>" +
						"   <td style='border:1px solid #000;padding:5px;'class='text-right'><span id='currentAmount'>"+sumOfCurrentAmount.toFixed(2)+"</span></td>"+
						"   </tr>";

					
					$("#paymentByArea").html(str);
					var newchar = '\\';
					majorHeadNames = majorHeadNames.toString().split(',').join(newchar);
						
					$("#typeOfWork").html(majorHeadNames);
					$("#hideIt").show();
					
					$('span').each(function(){
						// Do your magic here
					    if (isNum($(this).text())){ // regular expression for numbers only.
					    	var tempVal=parseFloat($(this).text());
					    	if(this.id!="site_id"&&this.id!="tempBillNo"&&this.id!="AccountNo"&&this.id!="MobileNo"&&this.id!="notificationBell")
					    		$(this).text(inrFormat(tempVal.toFixed(2)));
				           }
					});
					
					$(".overlay_ims").hide();	
					$(".loader-ims").hide();
				},
				error : function(err) {
					//alert(err);
				}
			});
		}
  });

	function isNum(value) {
		  var numRegex=/^[0-9.]+$/;
		  return numRegex.test(value)
	}
	function printAbstractjsp(){
		window.print();
	}
	function loadPrevPage(){
		goBack() ;
	}
	function goBack() {
	    window.history.back();
	}

  //For load contractor Details
  function loadContractorData(){
	$(".overlay_ims").show();	
	$(".loader-ims").show();
		var url = "loadAndSetVendorInfoForWO.spring";
  }
	var urlToActive="";
	var isRequestFromStatus="${nextLevelApprovelEmpID}";
	
	if(isRequestFromStatus!="END"){
		//if isRequestFromStatus is not equals to END then this request coming from contractor bills for status page
		urlToActive="viewContractorBillStatus.spring";
		if("${billBean.isSiteWiseStatusPage}"=="true"){
			urlToActive="siteWiseContractorBillStatus.spring";
	  	}
		//alert("${billBean.isSiteWiseStatusPage}");
	}else if(isRequestFromStatus=="END"){
		//if isRequestFromStatus is  equals to END then this request coming from view contractor bill 
		urlToActive="viewCompletedBills.spring";
	}
	
	//this code for to active the side menu 
	var referrer=urlToActive;
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
