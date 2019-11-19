<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Map"%>
<html>
<head>

 <link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="js/inventory.css" rel="stylesheet" type="text/css" />
 
 
        <jsp:include page="../CacheClear.jsp" />  
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link href="css/bootstrap.min.css" rel="stylesheet">
		<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
		<link href="css/custom.min.css" rel="stylesheet">
		<link href="css/style.css" rel="stylesheet" type="text/css">
		<link href="css/custom.css" rel="stylesheet" type="text/css">
		<link href="css/marketing/jquery.timepicker.min.css" rel="stylesheet" type="text/css">
		<link href="css/topbarres.css" rel="stylesheet" type="text/css">
		<script src="js/momment.js"></script>
		<link href="css/sweetalert2.css" rel="stylesheet" type="text/css"> 
		<script src="js/sweetalert2.js" type="text/javascript"></script>
		<script src="js/numberCheck.js" type="text/javascript"></script>
			      
		<title>Sumadhura-IMS</title>
		<link rel="shortcut icon" href="images/favicon.jpg" type="image/x-icon">
<style>
   .btnaction{margin-right:5px;}
   .breadcrumb {background: #eaeaea !important;}
   .btn{padding: 4px 12px !important;}
   #expenditure_table thead, #expenditure_table tbody tr{table-layout:fixed;display:table;width:100%;}
 #expenditure_table thead tr th:first-child, #expenditure_table tbody tr td:first-child{width:46px;text-align: center;}
 #expenditure_table tbody tr td{border-top:0px solid #000 !important;}
€‹
 
 </style>
</head>
<body class="nav-md" id="body-refresh">
<noscript>
	<h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then refresh the page.</h3>
	<style>
		#mainDivId {
			display : none;
		}
		
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
							<li class="breadcrumb-item active">Create Available Area</li>
						</ol>
			       </div>
			       <!-- Loader  -->
				          <div class="loader-sumadhura" style="display: none;z-index:999;">
								<div class="lds-facebook">
									<div></div><div></div><div></div><div></div><div></div><div></div>
								</div>
								<div id="loadingMessage">Loading...</div>
				          </div>
				      <!-- Loader -->
			 <div>
			 
			 <h3 class="text-center"><span><font color=green size=4 face="verdana">${message}</font></span></h3>
			 
				<div class="col-md-12">
				  <!-- page content -->
				  <div class="border-inwards-box">
				   <form  id="createAvailable" class="form-inline">
				    <div class="form-group">
				     <label>Month:</label>
				     </div>
				      <div class="form-group input-group">
				     <input type="text" id="monthYear" name = "monthYear" class="form-control readonly-color"  autocomplete="off" readonly="true"/>
				     <label class="input-group-addon btn input-group-addon-border" for="monthYear"><span class="fa fa-calendar"></span></label>
				    </div>
				   <!--  <button type="submit" class="btn btn-warning" id="btn-search" >Submit</button> -->
				    <input type="button" class="btn btn-warning" value="Submit" name = "Get Daetails" onclick="submitAvailable()">
				   </form>
				   </div>
				   
				   <form action="" class="form-inline" method = "" id="updateavalArea">
				 
				      
				      <%
				      
				      String strShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				    
				     System.out.println("showGrid : "+strShowGrid);
				      
				      if(strShowGrid.equals("true")){
				      
				      %>
				       <div class="table-responsive"> 
				     <div  class="col-md-12" style="padding: 15px;border: 1px solid #000;margin-bottom: 20px;">
					     <div class="col-md-1 no-padding-right no-padding-left">
					     	 <b style="font-size:18px;">Note : </b>
					     </div>
					     <div class="col-md-11 no-padding-right no-padding-left">
					     	<p class="text-justify" style="font-size: 16px;"><b>Please give Available are of all active sites, Active sites are : </b><c:out value="${sitesList}"/></p>
					     </div>
				     </div>
				        </div>
				        <div id="Markettable_hideshow">
				        <div class="table-responsive">
				    
				         <input type="hidden" id="totalNoOfRows" name="totalNoOfRows"    value="">
				         <input type="hidden" id="month_year" name="month_year"  value="${month_year}">
				      
				      <table class="table table-new" id="expenditure_table" style="width:1350px;">
						    <thead class="cal-thead-inwards">
						     <tr>
						       <th>S.No</th>
						       <th>Site</th>						       
						       <th>UOM</th>
						       <th style="width:150px;">Status</th>	
						       <th>Total Area</th>
						       <th>Available Area</th>							       				    
						       <th style="width:150px;">Action</th>
						     </tr>
						    </thead>
						    <tbody class="tbl-fixedheader-tbody addavilableTbl">
						    <tr id="tr-class1" class="tr-class">
						     <td>1</td>
						     <td><select class="form-control sitenameclass" name="site1" id="select_site1" onchange="siteNamesCompare(1)">
								<!-- <option value="-1" id="select_site1">--Select Site--</option> -->
								<option value="" >--Select Site--</option>
								<c:forEach items="${site}" var="site">
								<c:if test="${site.key!='996'}">
                               <option value="${site.key}$${site.value}">${site.value}</option>
                               </c:if>
                               </c:forEach>
							</select></td>
						     <td><input type="text" class="form-control unitofmeasurement" id="unitofMeasurement1" name="unitOfMeasurement1" value="SQFT" readonly/></td>
						        <td style="width:150px;">
							     <select class="form-control" id="status1" name="status1" onchange="statusChange(1)">
							     <option>--select--</option>
							     <option value="A">Active</option><option value="I">Inactive</option>
							     </select>
						     </td>
						     <td><input type="text" class="form-control" id="totalArea1" name="totalArea1" onkeypress="return isNumberCheck(this, event)" placeholder="Total Area" onfocusout="checkingNumberOnPasteInput(this.id, 1)"/></td>
						     <td><input type="text" class="form-control" id="availableArea1" name="availableArea1" onkeypress="return isNumberCheck(this, event)" placeholder="Available Area" onfocusout="checkingNumberOnPasteInput(this.id, 1)"></td>
						    <td style="width:150px;"> 
						      <button type="button" class="btnaction btnpadd" onclick="appendRow(1)" id="addRowBtn1"><i class="fa fa-plus"></i></button>
     		                  <button type="button" style="display:none;" class="btnaction" id="deleteRowBtn1"  onclick="deleteRow(1)"><i class="fa fa-trash"></i></button> 
     		                 </td>
						    </tr>
						    </tbody>
				     </table>
				     </div>
				   <div class="col-md-12 text-center center-block" style="margin-top: 30px;">
				    <input type ="button" id="Add_btn" class="btn btn-warning" value ="Add" onclick="addAvalibleAreaSubmit()">
				   </div>
				    </div>
				     <% } %>
				   
				    
				    </form>
				  <!-- page content -->
				</div>
	        </div>
	      </div>
	</div>
</div>
         <script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
        <script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/marketing/jquery.ui.monthpicker.js" type="text/javascript"></script> 
        <script src="js/marketing/jquery.timepicker.min.js"></script>         
        <script src="js/sidebar-resp.js" type="text/javascript"></script>
        
 <script>
  $(document).ready(function () {
	$('#monthYear').monthpicker({changeYear:true,dateFormat:"mm-yy"});
});
 function  submitAvailable(){
	 var monthyearSel = $('#monthYear').val();
	 if(monthyearSel == ""){
	 swal("Error!", "Please select month.", "error");
	 $('#monthYear').focus();
	 return false;
	 }
	 
	 $('.loader-sumadhura').show();
	 document.getElementById("createAvailable").action = "createAvailableArea.spring";
 	 document.getElementById("createAvailable").method = "POST";
 	 document.getElementById("createAvailable").submit();
 }
  function validateRows(){
	  var error=true;
		 $(".tr-class").each(function(){
		 var currentId=$(this).attr("id").split("tr-class")[1];
		 var siteSele = $("#select_site"+currentId).val();
		 var totalAreatbl = $("#totalArea"+currentId).val();
		 var availableAreatbl = $("#availableArea"+currentId).val();
		 var status = $("#status"+currentId).val();
		  if(siteSele == ""){
			  swal("Error!", "Please select site.", "error");
			  $("#select_site"+currentId).focus();
			  return error=false;
		  }
		  if(totalAreatbl == ""){
			  swal("Error!", "Please enter total area.", "error");
			  $("#totalArea"+currentId).focus();
			  return error=false;
		  }
		  if(availableAreatbl == ""){
			  swal("Error!", "Please enter available area.", "error");
			  $("#availableArea"+currentId).focus();
			  return error=false;
		  }
		  if(status == ""){
			  swal("Error!", "Please select status.", "error");
			  $("#status"+currentId).focus();
			  return error=false;
		  }
		  
		 });
		 return error;
  }
  function addAvalibleAreaSubmit(){debugger;
  
  
	 var noOfRows=[];
	 var error=true;
	 $(".tr-class").each(function(){
	 var currentId=$(this).attr("id").split("tr-class")[1];
	 noOfRows.push(currentId);
	 var siteSele = $("#select_site"+currentId).val();
	 var totalAreatbl = $("#totalArea"+currentId).val();
	 var availableAreatbl = $("#availableArea"+currentId).val();
	 var status = $("#status"+currentId).val();
	  if(siteSele == ""){
		  swal("Error!", "Please select site.", "error");
		  $("#select_site"+currentId).focus();
		  return error=false;
	  }
	  if(totalAreatbl == ""){
		  swal("Error!", "Please enter total area.", "error");
		  $("#totalArea"+currentId).focus();
		  return error=false;
	  }
	 	if(availableAreatbl == ""){
		  swal("Error!", "Please enter available area.", "error");
		  $("#availableArea"+currentId).focus();
		  return error=false;
	  }
	 	 if(status == ""){
			  swal("Error!", "Please select status.", "error");
			  $("#status"+currentId).focus();
			  return error=false;
		  }
	  
	 });
	 if(error == false){
		 return false;
	 }
	 //console.log("chargesRowCountNum: "+chargesRowCountNum);
	 $('#totalNoOfRows').val(noOfRows);
  var canISubmit = window.confirm("Do you want to update?");
	
	 if(canISubmit == false) {
		return false;
	 }
	 $('.loader-sumadhura').show();
	 document.getElementById("updateavalArea").action = "addAvailableArea.spring";
	 document.getElementById("updateavalArea").method = "POST";
	 document.getElementById("updateavalArea").submit();
} 
  /* =================================================Create Avialabel area================================= */
  function appendRow(id){
	  debugger;
	  console.log("siteData"+JSON.stringify(siteData));
	  var valRow=validateRows();
	  if(valRow==false){
		  return false;
	  }
	  var tbllength=$('#expenditure_table').find('tr').length;
		 /*alert(tbllength);*/
		 var tid=$('#expenditure_table tr:last').attr('id');
		 	var res = tid.split("tr-class")[1];
			 if(tbllength==2){		 	
			 	$("#deleteRowBtn"+res).show();
			 }
	  newId = id+1;
	  var tid=$('#expenditure_table tr:last').attr('id');	
		 var res = tid.split("tr-class")[1];
		 $("#addRowBtn"+res).hide();
	  $(".addavilableTbl").append('<tr id="tr-class'+newId+'" class="tr-class"><td>'+newId+'</td><td><select id="select_site'+newId+'" onchange="siteNamesCompare('+newId+')" class="form-control sitenameclass" name="site'+newId+'" ></select></td><td><input type="text" class="form-control unitofmeasurement" id="unitofMeasurement'+newId+'" name="unitOfMeasurement'+newId+'" value="SQFT" readonly/></td><td style="width:150px;"><select id="status'+newId+'" name="status'+newId+'" class="form-control" onchange="statusChange('+newId+')"><option>--select--</option><option value=A>Active</option><option value=I>Inactive</option></select></td><td><input type="text" class="form-control" id="totalArea'+newId+'" name="totalArea'+newId+'"  placeholder="Total Area" onblur="checkingNumberOnPasteInput(this.id, '+newId+')"/></td><td><input type="text" class="form-control" id="availableArea'+newId+'" name="availableArea'+newId+'"  placeholder="Available Area"  onblur="checkingNumberOnPasteInput(this.id, '+newId+')"/></td><td style="width:150px;"> <button type="button" class="btnaction btnpadd" onclick="appendRow('+newId+')" id="addRowBtn'+newId+'"><i class="fa fa-plus"></i></button><button type="button" class="btnaction" id="deleteRowBtn'+newId+'" onclick="deleteRow('+newId+')" ><i class="fa fa-trash"></i></button> </td></tr>');
	 var tempOptions="<option value=''>--Select--</option>";
	  for(var i=0;i<siteData.xml.site.length;i++){
		  if(siteData.xml.site[i].SITEID!='996'){
		  tempOptions+="<option value='"+siteData.xml.site[i].SITEID+"$"+siteData.xml.site[i].SITENAME+"'>"+siteData.xml.site[i].SITENAME+"</option>";
	  	}
	 }
	  $("#select_site"+newId).html(tempOptions);
  }
 /* =================================================Create Avialabel area================================= */
 /* =================================================remove row start================================= */
 //delete the added  row
 function deleteRow(id){
	 swal({
		    title: "Are you sure?",
		    text: "Do you want to delete row?",
		    type: "warning",
		    showCancelButton: true,
		    confirmButtonColor: '#DD6B55',
		    confirmButtonText: 'Yes, Delete!',
		    cancelButtonText: "No, cancel it!",
		    closeOnConfirm: false,
		    closeOnCancel: false
		 }).then(function(isConfirm){debugger;

		   if (isConfirm){
			   var rowscount=$('#expenditure_table').find('tr').length;
				//removing row
				
				if(rowscount==2){
					swal("Error!", "This row con't be deleted.", "error");
					return false;
				}
			   $("#tr-class"+id).remove();
			   swal("Deleted!", "Table row successfully deleted!", "success");
				var tid=$('#expenditure_table tr:last').attr('id');	
				var res = tid.split("tr-class")[1];
				if(rowscount==3){
					$("#deleteRowBtn"+res).hide();
				}
				if(res<id){		
					$("#addRowBtn"+res).show();
				}	     

		    } else {
		    	swal.close();
		    }
		 });
	/*    var CanIDelete=window.confirm("Do you want to delete row?");
		 if(CanIDelete==false){
			 return false;
		 } */
			
	 
 }
 /* =================================================remove row end================================= */
 var siteData;
  $.ajax({
  url :"siteNameDetails.spring",
  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
  type : "get",
  Cdata : "",
  contentType : "application/json",
  success : function(data) {
 console.log("data "+JSON.stringify(data));
 siteData= eval('(' + data + ')');
  },
  error:  function(data, status, er){
	  alert(data+"_"+status+"_"+er);
	  }
  });
  //site name compare on change written by thirupathi
	function siteNamesCompare(id){debugger;
		var rv = true;
		var tablelength=$("#expenditure_table > tbody > tr").length;
		var currentSiteId=$("#select_site"+id).val().split("$")[0];
		if(tablelength>1){
			jQuery('.sitenameclass').each(function() {
				  var currentId =parseInt($(this).attr("id").split("select_site")[1]); 
				  var siteId = $(this).val().split("$")[0];
				  if(siteId==currentSiteId && id!=currentId){		
					swal("Error!", ""+$("#select_site"+id).val().split("$")[1]+" is already exist, Please choose different site.", "error");
					$("#select_site"+id).val("");
					return rv = false;
				  }else{					  
				    return rv = true;
				  }
			});			
		}
		 return rv;
	}
	//checking number or not on paste
	function checkingNumberOnPasteInput(id, num){debugger;
		var reqAmnt=$("#"+id).val();
		if((isNaN(reqAmnt) || reqAmnt<0) && reqAmnt!=""){
			swal("Error!", "Please enter valid data.", "error");
			$("#"+id).val("");
			$("#"+id).focus();
			return false;
		}
		var totalQty=$("#totalArea"+num).val();
		var availArea=$("#availableArea"+num).val();
		if(totalQty!="" && availArea!="" && parseFloat(totalQty)<parseFloat(availArea)){
			swal("Error!", "Available area should be less than or equal to total area.", "error");
			$("#availableArea"+num).val('');
			return false;
		}
		/* if(totalQty=="0" && availArea=="0"){
			$("#status"+num).html("<option value='I'>Inactive</option>");
			$("#status"+num).val("I");
		}else{
			$("#status"+num).html("<option value=''>--select--</option><option value='A'>Active</option><option value='I'>Inactive</option>");
		}
		
		 */
	}
	//status change if you select Inactive ..total quantity and available quantity should be readonly with 0.00
	function statusChange(id){
		var status=$("#status"+id).val();
		if(status=="I"){
			$("#totalArea"+id).val("0.00");
			$("#availableArea"+id).val("0.00");
			$("#totalArea"+id).attr("readonly", true);
			$("#availableArea"+id).attr("readonly", true);
			
		}else{
			$("#totalArea"+id).val("");
			$("#availableArea"+id).val("");
			$("#totalArea"+id).removeAttr("readonly", true);
			$("#availableArea"+id).removeAttr("readonly", true);
		}
	}
 </script>
</body>
</html>
