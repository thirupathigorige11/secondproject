<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false"%>
<html lang="en">
<head>


<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<jsp:include page="CacheClear.jsp" />  

<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">
<link href="css/custom.css" rel="stylesheet">
<link href="css/topbarres.css" rel="stylesheet">

<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/jquery-ui.js" type="text/javascript"></script>
<title>Sumadhura-IMS</title>
<link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">

</head>

<style>
.abc {
	color: red;
}
</style>





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
				<div>
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#">Issue</a></li>
						<li class="breadcrumb-item active">Returns</li>
					</ol>
				</div>


				<div>
                  

<form name="myForm" action="doIndentIssueReturns.spring" class="form-horizontal border-inwards-box">
<label  id="nodata"><span style="color:red; font-size: 20px;">${noData}</span></label>
<div class="col-md-12">


<div class="col-md-12">
		   						<div class="col-md-6">
									<div class="form-group">
										<label for="date" class="col-md-4 control-label">From :</label>
										<div class="col-md-6 input-group">
											<input type="text" id="ReqDateId1" class="form-control readonly-color"
												name="fromDate" value="${fromDate}" autocomplete="off" readonly="true"><!-- onkeydown="return false" -->
												<label class="input-group-addon btn input-group-addon-border" for="ReqDateId1"> 	<span class="fa fa-calendar"></span></label>
											
										</div>
									</div>
								 </div>			
								 <div class="col-md-6">
									<div class="form-group">
										<label for="todate" class="col-md-4 control-label">To:</label>
										<div class="col-md-6 input-group">
										  <input type="text"
											id="ReqDateId2" class="form-control readonly-color" name="toDate" 
											value="${toDate}" autocomplete="off" readonly="true"><!--  onkeydown="return false" -->
											<label class="input-group-addon btn input-group-addon-border" for="ReqDateId2"> 	<span class="fa fa-calendar"></span></label>
										</div>
									</div>
								 </div>		
</div>

<div class="col-md-12  col-xs-12">
<hr class="hr-line-white"/>
</div>




<div class="col-md-4 col-md-offset-4">
<div class="form-group">
    <label for="registerId" class="control-label col-md-6"> Requester Id:</label>
    <div class="col-md-6">
       <input type="text" name="RequestId" id="RequestId"  class="form-control"/>
    </div>
</div>
</div>
<div class="col-md-6 col-md-offset-3 col-xs-12">
<div class="col-md-5 no-padding-left col-xs-5"><hr class="hr-line-white" /></div><div class="col-md-1 col-xs-1 Mrgtop10"><strong>(Or)</strong></div><div class="col-md-5 no-padding-right col-xs-5"><hr class="hr-line-white" /></div>
</div>


<div class="col-md-4 col-md-offset-4">
<div class="form-group">
   <label for="registerName" class="control-label col-md-6">Requester Name:</label>
   <div class="col-md-6">
     <input type="text" name="EmployeName" id="EmployeName" autocomplete="off" onkeyup="populateEmployee()" class="form-control"/>
   </div>
</div>
</div>
<div class="col-md-6 col-md-offset-3 col-xs-12">
<div class="col-md-5 no-padding-left col-xs-5"><hr class="hr-line-white" /></div><div class="col-md-1 col-xs-1 Mrgtop10"><strong>(Or)</strong></div><div class="col-md-5 no-padding-right col-xs-5"><hr class="hr-line-white" /></div>
</div>
<div class="col-md-4 col-md-offset-4">
<div class="form-group">
  <label for="contractorName" class="col-md-6 control-label">Contractor Name:</label>
 <div class="col-md-6">
   <input type="hidden" name="contractorId" id="contractorId">
   <input type="text" name="ContracterName" id="ContracterName" onkeyup="populateContractor()" autocomplete="off" class="form-control"/>
 </div>
</div>
</div>
<div class="col-md-12 text-center center-block">
  <button type="submit" value="submit" onclick="return validate()" class="btn btn-warning ">Submit</button>
</div>
</div>
</form>
  
<script src="js/jquery-ui.js"></script>
<script src="js/custom.js"></script>

<script type="text/javascript">

function populateEmployee() {
	debugger;
	var empName=$("#EmployeName").val();

	 var url = "loadAndSetEmployerInfo.spring?employeeName="+empName;

	  $.ajax({
	  url : url,
	  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
	  type : "get",
	  Edata : "",
	  contentType : "application/json",
	  success : function(data) {
		  console.log(data);
		  Edata = data.split("@@");
		  
		  var resultArray = [];
		  for(var i=0;i<Edata.length;i++){
		      resultArray.push(Edata[i].split("@@")[0]);
		  }
	  		$("#EmployeName").autocomplete({
		  		source : resultArray
		  	});
	  },
	  error:  function(data, status, er){
		  alert(data+"_"+status+"_"+er);
		  }
	  });


	  $('#RequesterNameId').on('change', function(){debugger;
			var value = $(this).val();
			 	
			/*value = value.replace("&", "$$$");*/
			//alert(value);
			
			setEmpData (value); //pass the value as paramter
	 });
	};

function populateContractor() {
	
	debugger;
	var contractorName=$("#ContracterName").val();
var nameRegex=/^[a-zA-z ]+$/;
if(contractorName.length>0){

if(!nameRegex.test(contractorName)){
	alert("please enter Contractor name.");
	//$("#contractorName").val("");
	return false;
}
}else{
	return false;
}
	 var url = "loadAndSetVendorInfoForWO.spring";
	 debugger;
  $.ajax({
	  url : url,
	  type : "get",
	data:{
		contractorName:contractorName,
		indentReturns:'autoCompleteFunction',
		loadcontractorData:false		
	},
	  contentType : "application/json",
	  success : function(data) {
	
	  		$("#ContracterName").autocomplete({
		  		source : data,
		  		change: function (event, ui) {
	                if(!ui.item){
	               	//if list item not selected then make the text box null	
	               	 $("#ContracterName").val("");
	                }
	              },
		  		select: function (event, ui) {
	                AutoCompleteSelectHandler(event, ui);
	            }
		  	});
	  },
	  error:  function(data, status, er){
		  alert(data+"_"+status+"_"+er);
		  }
	  });
}

//code for selected text
	function AutoCompleteSelectHandler(event, ui)
	{               
	    var selectedObj = ui.item;       
	    isTextSelect="true";
	  
	  var contractorName=selectedObj.value;
	 
		 var url = "loadAndSetVendorInfoForWO.spring";
		 $.ajax({
			  url : url,
			  type : "get",
			 data:{
				 contractorName:contractorName,
				 indentReturns:'getContractorId',
				 loadcontractorData:true	 
			 },
			  contentType : "application/json",
			  success : function(data) {
				  $("#contractorName").val(contractorName);
				  if(data!=""||data!="null"){

					var contractorData=data[0].split("@@");
					var contractorId=contractorData[0];
					
					$("#contractorId").val(contractorId);
					}
			  },
			  error:  function(data, status, er){
				  alert(data+"_"+status+"_"+er);
				  }
			  });
	}

    function validate(){
	var reqId = document.getElementById("RequestId").value;
		var reqName = document.getElementById("EmployeName").value;
		var ContracterName = document.getElementById("ContracterName").value;
		if((reqId == '' || reqId == 'undefined') && (reqName == '' || reqName == 'undefined')&& (ContracterName == '' || ContracterName == 'undefined')) {
			alert("Please Enter Request Id or Requester Name or Contracter Name");
			return false;
		}
}
</script>

					<script>
						$(document).ready(	function() {
							$(".up_down").click(function() {
								$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
							});

							
							$(function() {
								  $("#ReqDateId1").datepicker({
									dateFormat: 'dd-mm-yy',
									maxDate: new Date(),
									 onSelect: function(selected) {
							     	        $("#ReqDateId2").datepicker("option","minDate", selected)
							     	        }

								  });
								  $("#ReqDateId2").datepicker({
									dateFormat: 'dd-mm-yy',
									maxDate: new Date(),
									 onSelect: function(selected) {
							     	        $("#ReqDateId2").datepicker("option","minDate", selected)
							     	        }

								  });
								  return false;
							});
						});

						
					</script>
</body>
</html>
