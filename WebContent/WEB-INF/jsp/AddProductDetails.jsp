<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="CacheClear.jsp" />  
<title>Insert title here</title>
<script type="text/javascript" src="js/JQuery.js"> </script>
<script type="text/javascript">
$(document).ready(function()
		{
	$("#Product_add").hide();
	 $("#SubProduct_add").hide();
	 $("#ChildProduct_add").hide();
	 $("#SubProductsList").hide();
	 $("#ChildProduct_delete").hide();
	$("#Submit").hide();
	$('select#ServiceType').change(function(){

		 var varServiceType = document.getElementById('ServiceType').options[document.getElementById('ServiceType').selectedIndex].value;
	     if(varServiceType == 'add'){
	    	 $("#Product_add").show();
	    	 $("#SubProduct_add").show();
	    	 $("#ChildProduct_add").show();
	    	 $("#SubProductsList").hide();
	    	 $("#ChildProduct_delete").hide();
	    	$("#Submit").show();
	     }else if(varServiceType == 'View'){
		    	
	    	 $("#Product_add").hide();
	    	 $("#SubProduct_add").hide();
	    	 $("#ChildProduct_add").hide();
	    	 $("#SubProductsList").show();
	    	 $("#ChildProduct_delete").hide();
	    	$("#Submit").hide();
	    
	    	 $.ajax({
					type: 'POST',
					//dataType: 'json',
					url: 'ViewProduct.spring',
					//data: {'size':productName},
					success : function(data) {
						//alert("Success");  
						var trHTML = '';
						  var dataLen = data.length;
						//  alert(dataLen);
						  $('#records_table').empty();
						  trHTML += ' <tr><th>Product</th><th>SubProduct</th><th>ChildProduct</th></tr>';
			              for (i=0; i<dataLen; i++) {
			            	  trHTML += '<tr><td>' + data[i].product_Name + '</td>' + '<td>' + data[i].sub_Product + '</td>'+ '<td>' + data[i].child_Product + '</td></tr>' ;
			              }	
			              $('#records_table').append(trHTML);
						},  
					error : function(e) {  
						alert('Error: ' + e);   
						} 
				});
	     }else if(varServiceType == 'delete'){
	    	 $("#Product_add").hide();
	    	 $("#SubProduct_add").hide();
	    	 $("#ChildProduct_add").hide();
	    	 $("#SubProductsList").hide();
	    	 $("#ChildProduct_delete").show();
	    	$("#Submit").show();
	     }else{
	    	 $("#Product_add").hide();
	    	 $("#SubProduct_add").hide();
	    	 $("#ChildProduct_add").hide();
	    	 $("#SubProductsList").hide();
	    	 $("#ChildProduct_delete").hide();
	    	$("#Submit").hide();
	     }
	});
	});


</script>
</head>
<body>
<form action="save.spring" method="post">

<div class="row">
     	<div class="row">
     	<div class="input_div">
					<label>ServiceType</label>
						<select id="ServiceType" name="ServiceType">
                            <option value="">select</option>
						    <option value="add">add</option>
						    <option value="View">View</option>
						    <option value="delete">delete</option>
						</select>
		</div>
</div>


<div id="Product_add">
     <div class="row">
          <div class="input_div">
              <label> ProductName</label>
              <input type="text" name="product">
          </div>
     </div>
</div>

<div id="SubProduct_add">
     <div class="row">
         <div class="input_div">
              <label> SubProduct</label>
              <input type="text" name="subproduct">
         </div>
    </div>
</div>

<div id="ChildProduct_add">
     <div class="row">
         <div class="input_div">
              <label> ChildProduct</label>
              <input type="text" name="childproduct">
         </div>
    </div>
</div>

<div id="ChildProduct_delete">
     <div class="row">
          <div class="input_div">
              <label> Child Products : </label>
              <input type="text" name="ChildProducts_Delete">
          </div>
     </div>
</div>

<div id="submitbutton">
<input type="submit" value="Submit" id="Submit" class="btn small" name="Submit">
</div>

</form>


<br></br>
<div id="SubProductsList">
<table id="records_table" border="2" width="50%" cellpadding="2">
    
 </table>
 </div>
</body>
</html>