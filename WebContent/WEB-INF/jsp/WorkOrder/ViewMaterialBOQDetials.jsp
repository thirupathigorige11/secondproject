 <!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.Map"%>
<html lang="en">
<head>

<!-- <title>Approve RA Bill</title> -->
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

<link href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/css/select2.min.css"  rel="stylesheet" type="text/css">

<jsp:include page="./../CacheClear.jsp" />  

<script src="js/sidebar-resp.js" type="text/javascript"></script>
<!-- <script src="js/WorkOrder/CertificateOfPayment.js" type="text/javascript"></script> -->
<script src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.5/js/select2.min.js"></script>

<%-- <c:set value="END" var="isCompletedBill"></c:set>
<c:if test="${billBean.approverEmpId eq isCompletedBill}">
	<c:if test="${billBean.paymentType ne 'NMR'}">
		<script src="js/WorkOrder/ShowCompletedRAAdv.js" type="text/javascript"></script>
	</c:if>
	<c:if test="${billBean.paymentType eq 'NMR'}">
		<script src="js/WorkOrder/NMRCompleted.js" type="text/javascript"></script>
	</c:if>
</c:if>

<c:if test="${billBean.approverEmpId  ne isCompletedBill}">
	<c:if test="${billBean.paymentType ne 'NMR'}">
			<script src="js/WorkOrder/ShowRaAdvStatus.js" type="text/javascript"></script>
	</c:if>
	<!--  based on the payment type loading JS files  -->
	<c:if test="${billBean.paymentType eq 'NMR'}">
			<script src="js/WorkOrder/NMRStatus.js" type="text/javascript"></script>
	</c:if>
</c:if> --%>

<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>

</head>

<style>
/*css code for print */
th{text-align:center !important;}
tbody{color:#000;font-weight:bold;}
@media print{
.print-tnlrowcolor{background-color:#ccc !important;}
.nav_menu, .print_hide, .breadcrumb, #printshowRa{
display:none;
}
.modal_recovery_class{display:none;}
.border-none{border:0px !important;}
.btn-print-row{
display:none;}
thead {
display: table-row-group;
}
}
/*css code for print */
/* fixed header table css for modal*/
.scroll-rabill-tbl{
 width: calc(100% - 17px) !important;
 height:70px;
}
/* .scroll-rabill-tbl1{
 width: calc(100% - 17px) !important;
 height:70px;
} */
 .fixed-header-tbl{
 width:100%;
 border:1px solid #000;
 }
 .fixed-header-tbl tbody tr td{
 padding:5px;
 border:1px solid #000;
 }
 
 
/* fixed header table css for modal*/
.modal-body-scroll{
max-height:450px;
overflow-y:auto;
}
@media only screen and (min-width:1350px){
.modal-rabill-customwidth{
width:1300px;
}
}
.scroll-rabill-tbl>table>thead>tr>th{
border:1px solid #000;
background-color:#ccc;
color:#000;
border-top:1px solid #000 !important;
text-align:center;
}
.scroll-rabill-tbl>table>tbody>tr>td{
border:1px solid #000;
}
@media only screen and (min-width:1350px){
.modal-rabill-customwidth{
width:1300px;
}
}

.rabill-class-select{
width:130px;
}
.mrg-Top{
margin-top:5px;
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
/* written by thirupathi */
.deduction{
	width: 200px;
	 text-align:center;
	/*  padding-left:8%; */
	 margin-left:30%;
}
.deduction .lst{
  text-align:center;
}
.hidebtn1{
    visibility: hidden;
}
.deletebtn{
	margin-left:2%;
}
.heightthirty{
  height:30px;
}
</style>
<script>
		if (typeof (Storage) !== "undefined") {
			debugger;

			var i = parseInt(sessionStorage.getItem("${UserId}tempRowsIncre12"));
			if (i == 2) {
				sessionStorage.setItem("${UserId}tempRowsIncre12", 1);
				window.location.assign("viewRABilsforapprovles.spring");
			}
		} else {
			alert("Sorry, your browser does not support Web Storage...");
		}
</script>

<body class="nav-md">
	 <!-- <div class="overlay_ims"></div>
	 <div class="loader-ims" id="loaderId"> 
		<div class="lds-ims">
			<div></div><div></div><div></div><div></div><div></div><div></div></div>
		<div id="loadingimsMessage">Loading...</div>
	</div> -->
	
	
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
					<li class="breadcrumb-item active">Work Order </li>
				</ol>
		</div>

 
 <div class="clearfix"></div>
			<div class="col-md-12">
			<div class="table-responsive Mrgtop10">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead style="color: black;">
							<tr>
								<th>S.No</th>
								<th>Work Description</th>
								<th>Group Name</th>
								<th>UOM</th>
								<th>Block</th>
								<th>Floor</th>
								<th>Flat</th>
								<th>Total Quantity</th>
								<th>Issued Quantity</th>
								<th>Available Quantity</th>
							</tr>
						</thead>
						<tbody>
						<c:set value="1" var="count"></c:set>
						<c:set value="1" var="serialNumber"></c:set>
						<c:forEach var="productDetails" items="${MaterialBOQProductDetails}">
							<tr>
								<td  class="text-center">${serialNumber} <c:set value="${count+1}" var="count"></c:set></td>
								<td>${productDetails.WO_WORK_DESCRIPTION}&nbsp;<c:set value="${serialNumber+1}" var="serialNumber"></c:set></td>
								<td class="anchor-class" onclick='showTitle(event, ${count})'>
								<a href="javascript:void(0);"  class="anchor-class">
									${productDetails.MATERIAL_GROUP_NAME }
								</a>
									<input type="hidden" name="blockId" value="${productDetails.BLOCK_ID }">
									<input type="hidden" name="floorId" value="${productDetails.FLOOR_ID }">
									<input type="hidden" name="flatId" value="${productDetails.FLAT_ID }">
								</td>
								<td>&nbsp;${productDetails.MATERIAL_GROUP_MST_NAME}</td>
								<td>&nbsp;${productDetails.BLOCK_NAME }</td>
								<td>
									<c:if test="${not empty productDetails.FLOOR_NAME}">&nbsp;${productDetails.FLOOR_NAME}</c:if>
									<c:if test="${empty productDetails.FLOOR_NAME}">-</c:if>
								</td>
								<td><c:if test="${not empty productDetails.FLAT_NAME}">&nbsp;${productDetails.FLAT_NAME}</c:if>
									<c:if test="${empty productDetails.FLAT_NAME}">-</c:if></td>
								<td class="addDecimalPoints">${productDetails.TOTAL_QUANTITY} <c:set value="${count+1}" var="count"></c:set></td>
								
								<td class="addDecimalPoints anchor-class" onclick='showTitle(event, ${count})'  >  
									<a href="javascript:void(0);"  class="anchor-class">
										<c:if test='${not empty productDetails.ISSUED_QTY}'>${productDetails.ISSUED_QTY}</c:if>
										<c:if test='${empty productDetails.ISSUED_QTY}'>0.0</c:if>
									</a>  
								
							</td>
								<td class="addDecimalPoints">${productDetails.TOTAL_QUANTITY-productDetails.ISSUED_QTY }</td>
							</tr>
							 
						</c:forEach>
						<!-- <tbody>
							<tr>
							<td  colspan="7" class="text-right">Sub Quantity</td>
								<td colspan=""  class="total totalWoAmount subTtl " id="subPageTotalQuantity"></td>
								<td class="totalWoAmount"  id="subPageIssuedQuantity"></td>
								<td class="totalWoAmount"   id="subPageAvailableQuantity"></td>
							 
							</tr>
				     </tbody> -->
					</table>
				</div>
			</div>
		</div>	
	</div>
	</div>		
			<%-- <div class="col-md-12 text-right Mrgtop10">
			   <div class="col-md-4 col-md-offset-8"><div class="col-md-6"><strong>Total Quantity:</strong></div><div class="col-md-6 amountTotal"><span id="totalWOQuantity" class="totalWoAmount">${totalAmount}</span></div></div>
			   <div class="col-md-4 col-md-offset-8"><div class="col-md-6"><strong>WO Issued Quantity:</strong></div><div class="col-md-6 amountTotal"><span id="totalWOIssuedQuantity" class="totalWoAmount">${woBilledmount}</span></div></div>
			   <div class="col-md-4 col-md-offset-8"><div class="col-md-6"><strong>WO Available Quantity:</strong></div><div class="col-md-6 amountTotal"><span id="totalWOAvailQuantity" class="totalWoAmount">${woPaidAmount }</span></div></div>
			</div> --%>
			
			<div>
			<c:set value="1" var="count"></c:set>
				<c:forEach var="productDetails" items="${MaterialBOQProductDetails}">
						<c:set value="${count+1}" var="count"></c:set>
 						
 						<div id="titleDiv${count}" style="display:none;">
							<%-- <c:if test="${productDetails.QtyAndProdName.size()>0}">	 --%>
								<table class='table table-bordered  table-new'><thead><tr><th>Child Product Name</th></tr></thead>
									<tbody style='background-color:#fff;'>
										<c:if test="${productDetails.QtyAndProdName.size()==0 || empty productDetails.QtyAndProdName.size()}">
											<tr><td  class="text-center">No Data</td></tr>	
										</c:if>
										<c:forEach var="productAndQty" items="${productDetails.QtyAndProdName}">
											<tr><td>${productAndQty.NAME }</td></tr>
										</c:forEach>
									</tbody>
								</table>
						<%-- 	</c:if> --%>
				 		</div>	
				 		<c:set value="${count+1}" var="count"></c:set>
				 		
				 		<div id="titleDiv${count}" style="display:none;">
							<%-- <c:if test="${productDetails.QtyAndProdName.size()>0}">	 --%>
								<table class='table table-bordered table-new'>
											<thead><tr><th>Child Product Name</th><th>Quantity</th></tr></thead>
											<tbody style='background-color:#fff;'>
											<c:if test="${productDetails.QtyAndProdName.size()==0  || empty productDetails.QtyAndProdName.size()}">
												<tr><td colspan="2" class="text-center">No Data</td></tr>	
											</c:if>
											<c:forEach var="productAndQty" items='${productDetails.QtyAndProdName}'>
												<tr><td>${productAndQty.NAME } </td>
												<td>${productAndQty.ISSUED_QTY }</td>
												</tr>
											</c:forEach></tbody>
								</table>
								<%-- </c:if> --%>
						</div>
						
				 </c:forEach>		
			</div>
			
			<%-- <div>
			<c:set value="1" var="count"></c:set>
			<c:forEach var="productDetails" items="${MaterialBOQProductDetails}">
				<c:set value="${count+1}" var="count"></c:set>
				<div id="titleDiv${count}" style="display:none;">
								<table class='table table-bordered table-new'>
											<thead><tr><th>Child Product Name</th><th>Quantity</th></tr></thead>
											<tbody>
											<c:forEach var="productAndQty" items='${productDetails.QtyAndProdName}'><tr><td>${productAndQty.NAME }</td>
												<td>${productAndQty.ISSUED_QTY }</td></tr>
											</c:forEach></tbody>
								</table>
				</div>
			 </c:forEach>		
			</div> --%>
			  <div class="modal" id="childProductModal">
			    <div class="modal-dialog">
			      <div class="modal-content">
			        <div class="modal-header">
			          <button type="button" class="close" data-dismiss="modal">&times;</button>
			        </div>
			        <div class="modal-body">
			          <div id="modalbody">
			          </div>
			        </div>
			        <div class="modal-footer">
			         <div class="text-center center-block">
			          <button type="button" class="btn btn-warning" data-dismiss="modal">Close</button>
			         </div>
			        </div>       
			      </div>
			    </div>
			  </div>	
			
 <div class="col-md-12 text-center center-block Mrgtop20">
 <label>&nbsp;</label>
 </div>
 <div class="col-md-12 text-center center-block Mrgtop20">
   <!-- <input type="button" id="printshowRa" class="btn btn-warning" value="Print" onclick="myPrint()">
  -->
 </div>

<!-- Modal popup for approve RABill End -->
<input type="hidden" id="hiddenpaymentareaAmt" name="hiddenpaymentareaAmt">
</body>

   	<script src="js/jquery.min.js"></script>
	<script src="js/jquery-ui.js" type="text/javascript"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/custom.js"></script>
	<script src="js/jquery.dataTables.min.js"></script>
	<script src="js/dataTables.bootstrap.min.js"></script>		
	<!-- <script src="js/stacktable.js"></script> -->
	<script type="text/javascript" src="js/WorkOrder/CommonCode.js"></script>
   <script>
   $(document).ready(function() {
   $(window).load(function () {
		$(".addDecimalPoints").each(function(){
			var currentVal=parseFloat($(this).text()==""?0:parseFloat($(this).text().replace(/,/g,'')));
			$(this).text(currentVal.toFixed(2));
		});
   });
   $(function () {
	    $("[data-toggle='tooltip']").tooltip();
	});
   var searchData="";
    
	var table=$('#tblnotification').DataTable({"aLengthMenu": [[10, 25, 50, 100, -1], [10, 25, 50, 100,  "All"]],
		 /*  "search": {
			  
			    "search": $('#tblnotification').mouseup(function (e){ debugger;  text = window.getSelection().toString();
				 return text;
				})
			  }, */
		"footerCallback": function ( row, data, start, end, display ) {
        var api = this.api(), data;
		 debugger;
		 
       // Remove the formatting to get integer data for summation
       var intVal = function ( i ) {
           return typeof i === 'string' ?
               i.replace(/[\$,]/g, '')*1 :
               typeof i === 'number' ?
                   i : 0;
       };
       debugger;
       // Total over all pages
/*        totalWOQuantity = api
           .column(7)
           .data()
           .reduce( function (a, b) {
             debugger;
           	return intVal(a) + intVal(b);
       }, 0 );
       
       totalWOBilledQuantity = api
       .column(8)
       .data()
       .reduce( function (a, b) {
         debugger;
       	return intVal(a) + intVal(b);
       }, 0 );
       
       totalWOAvailQuantity = api
       .column(9)
       .data()
       .reduce( function (a, b) {
         debugger;
       	return intVal(a) + intVal(b);
       }, 0 );
      // alert(totalWOAvailQuantity);
       
       
       
       debugger;
       // Total over this page
       subPageTotalQuantity = api
           .column( 7, { page: 'current'} )
           .data()
           .reduce( function (a, b) {
           	debugger;
           	return intVal(a) + intVal(b);
           }, 0 );
       
       subPageIssuedQuantity = api
       .column( 8, { page: 'current'} )
       .data()
       .reduce( function (a, b) {
       	debugger;
       	return intVal(a) + intVal(b);
       }, 0 );
       
       subPageAvailableQuantity = api
       .column( 9, { page: 'current'} )
       .data()
       .reduce( function (a, b) {
       	debugger;
       	return intVal(a) + intVal(b);
       }, 0 );
       
       
       $("#totalWOQuantity").html(totalWOQuantity.toFixed(2));
       $("#totalWOIssuedQuantity").html((totalWOQuantity-totalWOAvailQuantity).toFixed(2));
       $("#totalWOAvailQuantity").html(totalWOAvailQuantity.toFixed(2));
       
       
	   	$("#subPageTotalQuantity").html((subPageTotalQuantity.toFixed(2)));
	    $("#subPageIssuedQuantity").html((subPageTotalQuantity-subPageAvailableQuantity).toFixed(2));
	    $("#subPageAvailableQuantity").html((subPageAvailableQuantity.toFixed(2))); */
       
 
       	$(".addDecimalPoints").each(function(){
				var currentVal=$(this).text()==""?0:parseFloat($(this).text().replace(/,/g,''));
				//$(this).text((currentVal.toFixed(2)));
				$(this).text(inrFormat(currentVal.toFixed(2)));
				debugger;
			});
      
     //$( api.column(5).footer() ).html(''+pageTotal.toFixed(2) +' ('+ total.toFixed((2)) +' total)');
   }	
});
	  var data = table
	   .rows()
	   .data();

	//alert( 'The table has '+data.length+' records' );
//if user selecting text from jquery data table then it will be get searched automatically
 	 /*  $('#tblnotification').mouseup(function (e){
		 debugger;
 		  text = window.getSelection().toString();
		    alert(text.trim().replace(/\s\s+/g, ' '));
		   // $('#myInputTextField').keyup(function(){
		    	table.search(text.trim().replace(/\s\s+/g, ' ')).draw() ;
		 // })
		  });  */
});
   
   
 
	//this code for to active the side menu 
	var referrer="${urlForActivateSubModule}";
	if(referrer.length!=0){
		$SIDEBAR_MENU.find('a').filter(function () {
	           var urlArray=this.href.split( '/' );
	           for(var i=0;i<urlArray.length;i++){
	        	 if(urlArray[i]==referrer) {
	        		 return this.href;
	        	 }
	           }
	    }).parent('li').addClass('current-page').parents('ul').slideDown().parent().addClass('active');
	}
	$(".anchor-class") .each(function() {
		  $( this ).tooltip();
	});
	
	function showTitle(id, current){
		$(current).attr("title", $("#titleDiv"+id).html());
	}
	function showTitle(e, id){debugger;
		/* $("#titleDiv"+id).show(); */
		/*   var x = e.clientX,
	        y = e.clientY;
		$("#titleDiv"+id).css({"top" :(y+14) + 'px'});
		$("#titleDiv"+id).css({"left" :(x+10) + 'px'});
		$("#titleDiv"+id).css({"position" :'absolute'});
		$("#titleDiv"+id).css({"z-index" :'99999'}); */
		$("#modalbody").html($("#titleDiv"+id).html());
		$("#childProductModal").modal();
	}
	function hideTitle(id){
		$("#titleDiv"+id).hide();
	}
	   </script>
</html>
