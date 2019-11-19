function addLocation(id) {
	calculateOtherCharges();
	var childProduct=$("#locationChildProduct"+id).val();
	var prodQuantity;
	var prodAmountAfterTax;
	var childProductId = childProduct.split("$")[0];
	var childProductName = childProduct.split("$")[1];
	var totalAmount;
	$(".ChildProduct").each(function(){debugger;
	if($(this).val()==childProductName){debugger;
		 var splitId=$(this).attr("id").split("ChildProduct")[1];
		 $("#locationQuantity"+id).val($("#QuantityId"+splitId).val()==""?0:$("#QuantityId"+splitId).val());
		 prodQuantity=$("#QuantityId"+splitId).val()==""?0:$("#QuantityId"+splitId).val();
		 prodAmountAfterTax =$("#AmountAfterTaxId"+splitId).val()==""?0:$("#AmountAfterTaxId"+splitId).val();
		 totalAmount=$("#TotalAmountId"+splitId).val()==""?0:$("#TotalAmountId"+splitId).val();
	}
	});
	var url = "loadAndSetLocationData.spring?childProductId="+childProductId;	
	 $.ajax({
	 url : url,
	 type : "get",
	 Cdata : "",
	 contentType : "application/json",
	 success : function(data) {
		  console.log(data);
		  debugger;
		var options='<option>--select--</option>';
		  $.each(data,function(key,value){
			  options+='<option value="'+key+"$"+value+'">'+value+'</option>';		  
		});  
		  $("#location_mar"+id).html(options);
		  var result=(totalAmount/prodQuantity);
		  $("#total_Amount"+id).val(parseFloat(totalAmount).toFixed(2));
		  $("#locationQuantity"+id).val();
		  $("#price_Aftertax"+id).val(result.toFixed(2));	
	 },
	 error:  function(data, status, er){
		 // alert(data+"_"+status+"_"+er);
		  }
	 });
};

//from date and to date method
function fromDateChange(id){
	var x=$('#from_date_location'+id).datepicker("getDate");
	$("#to_date_location"+id).datepicker( "option", "minDate",x ); 	
}
function toDateChange(id){
	var x=$('#to_date_location'+id).datepicker("getDate");
	$("#from_date_location"+id).datepicker( "option", "maxDate",x ); 	
}

//totalAmnt = Math.round(totalAmnt * 100)/100;

//validating location duration table
function validateLocationDurationTable(){
	var error=true;
	$(".location").each(function(){
		var id=$(this).attr("id").split("location")[1];
		if($("#locationChildProduct"+id).val()==""){
			swal("Error!", "Please select child product.", "error");
			$("#locationChildProduct"+id).focus();
			return error=false;
		}
		if($("#locationQuantity"+id).val()==""){
			swal("Error!", "Please enter quantity.", "error");
			$("#locationQuantity"+id).focus();
			return error=false;
		}
		if($("#site_Name"+id).val()==""){
			swal("Error!", "Please enter site name.", "error");
			$("#site_Name"+id).focus();
			return error=false;
		}
		if($("#price_Aftertax"+id).val()==""){
			swal("Error!", "Please enter price per unit after tax.", "error");
			$("#price_Aftertax"+id).focus();
			return error=false;
		}
		if($("#total_Amount"+id).val()==""){
			swal("Error!", "Please enter total amount.", "error");
			$("#total_Amount"+id).focus();
			return error=false;
		}
		if($("#from_date_location"+id).val()==""){
			swal("Error!", "Please select from date.", "error");
			$("#from_date_location"+id).focus();
			return error=false;
		}
	});
	return error;
}


function validateProductRow(){
	debugger;
	var error=true;
	$(".productchargesrow").each(function(){
		var currentId=$(this).attr("id").split("tr-class")[1];
		if($("#Product"+currentId).val()==''){
			alert("Please select product.");
			$("#Product"+currentId).focus();
			return  error=false;
		}
		if($("#SubProduct"+currentId).val()==''){
			alert("Please select sub product.");
			$("#SubProduct"+currentId).focus();
			return  error=false;
		}
		if($("#ChildProduct"+currentId).val()==''){
			alert("Please select child product.");
			$("#ChildProduct"+currentId).focus();
			return  error=false;
		}	
		if($("#UnitsOfMeasurementId"+currentId).val()==''){
			alert("Please select UOM.");
			$("#UnitsOfMeasurementId"+currentId).focus();
			return  error=false;
		}	
		if($("#QuantityId"+currentId).val()==''){
			alert("Please enter quantity.");
			$("#QuantityId"+currentId).focus();
			return  error=false;
		}	
		if($("#PriceId"+currentId).val()==''){
			alert("Please enter price.");
			$("#PriceId"+currentId).focus();
			return  error=false;
		}
		if($("#Discount"+currentId).val()==''){
			alert("Please enter discount.");
			$("#Discount"+currentId).focus();
			return  error=false;
		}
		if($("#TaxId"+currentId).val()==''){
			alert("Please select tax.");
			$("#TaxId"+currentId).focus();
			return  error=false;
		}
		
	})
	return  error;
}


function validateChargesRow(){
	//debugger;
	var error=true;
	$(".chargesrow").each(function(){debugger;
		var currentId=$(this).attr("id").split("chargesrow")[1];
		if($("#Conveyance"+currentId).val()==''){
			alert("Please select conveyance.");
			$("#Conveyance"+currentId).focus();
			return  error=false;
		}
		if($("#ConveyanceAmount"+currentId).val()==''){
			alert("Please enter conveyance amount.");
			$("#ConveyanceAmount"+currentId).focus();
			return  error=false;
		}
		if($("#GSTTax"+currentId).val()==''){
			alert("Please select GSTTax.");
			$("#GSTTax"+currentId).focus();
			return  error=false;
		}	
	})
	return  error;
}



