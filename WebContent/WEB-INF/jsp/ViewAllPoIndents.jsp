<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
<!-- Meta, title, CSS, favicons, etc. -->
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<jsp:include page="CacheClear.jsp" />  
<meta name="viewport" content="width=device-width, initial-scale=1">
<!-- Bootstrap -->
<link href="css/bootstrap.min.css" rel="stylesheet">
<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
<!-- Custom Theme Style -->

<link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
<link href="css/custom.min.css" rel="stylesheet">
<link href="css/style.css" rel="stylesheet">

<link href="css/dataTables.bootstrap.min.css" rel="stylesheet">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>View GRN Details</title>
<style>

.success,.error {
	text-align: center;
	font-size: 14px;
}
.popup {
	position: relative;
	display: inline-block;
	cursor: pointer;
	-webkit-user-select: none;
	-moz-user-select: none;
	-ms-user-select: none;
	user-select: none;
}

/* The actual popup */
.popup .popuptext {
	visibility: hidden;
	width: 160px;
	background-color: #555;
	color: #fff;
	text-align: center;
	border-radius: 6px;
	padding: 8px 0;
	position: absolute;
	z-index: 1;
	bottom: 125%;
	left: 50%;
	margin-left: -80px;
}

/* Popup arrow */
.popup .popuptext::after {
	content: "";
	position: absolute;
	top: 100%;
	left: 50%;
	margin-left: -5px;
	border-width: 5px;
	border-style: solid;
	border-color: #555 transparent transparent transparent;
}

/* Toggle this class - hide and show the popup */
.popup .show {
	visibility: visible;
	-webkit-animation: fadeIn 1s;
	animation: fadeIn 1s;
}

/* Add animation (fade in the popup) */
@
-webkit-keyframes fadeIn {
	from {opacity: 0;
}

to {
	opacity: 1;
}

}
@
keyframes fadeIn {
	from {opacity: 0;
}

to {
	opacity: 1;
}
</style>

<script type="text/javascript">


	function validate() {
		var from = document.getElementById("ReqDateId1").value;
		var to = document.getElementById("ReqDateId2").value;

		if (from == "" && to == "") {
			alert("Please select From Date or To Date !");
			return false;
		}
	}
</script>
</head>
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
						<li class="breadcrumb-item"><a href="#">Home</a></li>
						<li class="breadcrumb-item active">Library</li>
					</ol>
				</div>

<div align="center">
<div class="container" style="background-color: #ccc; width: 62%; margin: 5% auto; padding: 30px; text-align: center; font-size: 16px; height: auto; border: 1px solid gray; box-shadow: 2px 3px;">
<label class="success"><c:out value="${requestScope['succMessage']}"></c:out> </label> 
				<%-- 		<form:form  id="indentIssueFormId" action="getIndentReciveViewDts.spring">
											<td><strong>From Date :</strong> <input id="ReqDateId1" type="text" name="fromDate" value="${fromDate}" autocomplete="off">
											</td>
											<td><strong>To Date :</strong> <input id="ReqDateId2" type="text" name="toDate" value="${toDate}" autocomplete="off">
											</td>
											<td>
												
											</td> <br/><br/><br/>
				<input type="submit" value="Submit" id="saveBtnId" class="btn btn-warning" style="width: 136px" onclick="return validate();">
				<div style="margin-top: 36px">${displayErrMsg}</div>
				</form:form>
				 --%>
				
	<form class="form-inline"  action="ViewMyIndents.spring" >
   
     <div class="form-group">
	    <label for="date">From Date :</label>
	   <input type="text" id="ReqDateId1"  name="fromDate" value="${fromDate}" autocomplete="off"">
  	</div>
  	 <div class="form-group">
	    <label for="todate">To Date :</label>
	    <input type="text" id="ReqDateId2"  name="toDate" value="${toDate}" autocomplete="off">
  	</div>
 
   
    <div class="form-group">        
      <div class="col-sm-offset-2 col-sm-10">
        <button type="submit"  value="Submit" id="saveBtnId" class="btn btn-warning btn-lg media-style" style=" margin-top: 36px" onclick="return validate();">Submit</button>
        <div style="margin-top: 36px">${displayErrMsg}</div>
      </div>
    </div>
  </form>
				
				
				
				
				
				
				
</div>
</div>

				<%
				   String isShowGrid = request.getAttribute("showGrid") == null ? "" : request.getAttribute("showGrid").toString();
				   if(isShowGrid.equals("true")){
				%>
				<br></br>
			<div class="table-responsive container1">
					<table id="tblnotification"	class="table table-striped table-bordered st-table" cellspacing="0">
						<thead style="color: black;">

							<tr>
							<th>Creation Date</th>		
    				
    				<th>Indent Number</th>
    				
    				<th>Status</th>
    				
    				<th>Schedule Date</th>
    				
				</tr>

						</thead>
						<tbody>
				<c:forEach items="${indentIssueData}" var="element">  
				<tr>
				    <td style="color: black;">${element.receivedDate}</td>
					
					<td><a href="getIndentDetails.spring?indentNumber=${element.requesterId}" style="text-decoration: underline;color: blue;">${element.requesterId}</a></td>
					
				 	<td style="color: black;">${element.status}</td>
    				
					
					<td style="color: black;">${element.strInvoiceDate}</td>
				</tr>
				</c:forEach>
				
						<tbody>
				
				
				</tbody>
					</table>
				</div>
           <%
				   }
           %>
				<!-- /page content -->
			</div>
		</div>
	</div>

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
	<script>
	$(function() {
		  $("#ReqDateId1").datepicker({
			  dateFormat: 'dd-M-y',
			maxDate: new Date()
		  });
		  $("#ReqDateId2").datepicker({
			  dateFormat: 'dd-M-y',
			  maxDate: new Date()
		  });
		  return false;
	});
		$(document).ready(
				
				function() {
					$(".up_down").click(
							function() {
								$(this).find('span').toggleClass(
										'fa-chevron-up fa-chevron-down');
								$(this).find('span').toggleClass(
										'fa-chevron-right fa-chevron-left');
							});
					$('#tblnotification').DataTable();
				});

		$(function() {
			var div1 = $(".right_col").height();
			var div2 = $(".left_col").height();
			var div3 = Math.max(div1, div2);
			$(".right_col").css('max-height', div3);
			$(".left_col").css('min-height', $(document).height() - 65 + "px");
		});
		$('#tblnotification').stacktable({myClass:'stacktable small-only'});
	</script>

</body>
</html>
