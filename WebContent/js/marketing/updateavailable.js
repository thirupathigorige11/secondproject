 var siteData;
  $.ajax({
  url : "siteNameDetails.spring",
  type : "get",
  contentType : "application/json",
  success : function(data) {	  
	  siteData=eval('('+data+')');	 
	  siteData=siteData.xml.siteNames.site;
  }
 });
  
$("#btn-search").click(function(){
	 var monthYear = $("#monthYear").val();
	 if(monthYear == ""){
		 swal("Error!", "Please select month and year.", "error");
		 $("#monthYear").focus();
		 return false;
	 }
	 else{
		 $("#Markettable_hideshow").show();
		 return true;
		 }
	 
 });
 /*$('#monthYear').datepicker({
     changeMonth: true,
     changeYear: true,
     dateFormat: 'MM yy',
       
     onClose: function() {
        var iMonth = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
        var iYear = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
        $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
     },
       
     beforeShow: function() {
       if ((selDate = $(this).val()).length > 0) 
       {
          iYear = selDate.substring(selDate.length - 4, selDate.length);
          iMonth = jQuery.inArray(selDate.substring(0, selDate.length - 5), $(this).datepicker('option', 'monthNames'));
          $(this).datepicker('option', 'defaultDate', new Date(iYear, iMonth, 1));
           $(this).datepicker('setDate', new Date(iYear, iMonth, 1));
       }
    }
  });*/

 function appendRow(id){
	 //validate table
	 var valTable=valUpdateArea();
	 if(valTable==false){
		 return false;
	 }
	 var tbllength=$('#expenditure_table').find('tr').length;
	 var tid=$('#expenditure_table tr:last').attr('id');
	 var res = tid.split("tr-class")[1];
	 if(tbllength==2){		 	
		 	$("#deleteRowBtn"+res).show();
	 }
	 var rowCount = parseInt(res)+1;
	 var addTblRow = '<tr id="tr-class'+rowCount+'" class="tr-class">'
	                  +'<td>'+rowCount+'</td>'
	                  +'<td><select class="form-control sitenameclass" id="siteName'+rowCount+'" name="siteName'+rowCount+'" style="width:100%;" onchange="siteCompare('+rowCount+')"></select></td>'
	                  +'<td><input type="text" id="UOM'+rowCount+'" name="UOM'+rowCount+'"    value="SQFT"  class="form-control" readonly="true"></td>'
	                  +'<td><select id="status'+rowCount+'" name="status'+rowCount+'" class="form-control" onchange="statusChange('+rowCount+')"></select><input type="hidden" name="areaActionValue'+rowCount+'" id="areaActionValue'+rowCount+'" value="N"> </td>'
	                  +'<td><input type="text" id="totalArea'+rowCount+'" name="totalArea'+rowCount+'"  onkeypress="return isNumberCheck(this, event)" placeholder="Total Area" onfocusout="checkValue('+rowCount+')" value="" class="form-control pasteDisable"></td>'
	                  +'<td><input type="text" class="form-control pasteDisable" id="availableArea'+rowCount+'" onkeypress="return isNumberCheck(this, event)" placeholder="Available Area" onfocusout="checkValue('+rowCount+')" name="availableArea'+rowCount+'"/> '
	                  +'<td><button type="button" class="btnaction" onclick="appendRow('+rowCount+')" id="addRowBtn'+rowCount+'"><i class="fa fa-plus"></i></button><button type="button" class="btnaction" id="deleteRowBtn'+rowCount+'" onclick="deleteRow('+rowCount+')"><i class="fa fa-trash"></i></button></td>'
	                  +'</tr>';
	 var tid=$('#expenditure_table tr:last').attr('id');	
	 var res = tid.split("tr-class")[1];
	 $("#addRowBtn"+res).hide();
	 //appending row
	 $("#expenditure_table tbody").append(addTblRow);
	 $('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
	  var siteOptions="<option value=''>--select--</option>";
	 for(var i=0;i<siteData.xml.site.length;i++){
		 if(siteData.xml.site[i].SITEID!='996'){
		 siteOptions+="<option value='"+siteData.xml.site[i].SITEID+"$"+siteData.xml.site[i].SITENAME+"'>"+siteData.xml.site[i].SITENAME+"</option>";
		 }
	 }
	 var currentOptions="<option value=''>--select--</option><option value='A'>Active</option><option value='I'>Inactive</option>";
	 $("#status"+rowCount).html(currentOptions);
	 $("#siteName"+rowCount).html(siteOptions);
 }
 
 //delete the existing row
 function deleteRowStrikeOut(id){
	 var n=0;
	 $(".tr-class").each(function(){debugger;
	 	var id=$(this).attr("id").split("tr-class")[1];
	 	n++;
	 	
	 });

	 if(n==1){
	 	swal("Error!", "This row can't be deleted.", "error");
	 	return false;
	 }
//	 var CanIStrike=window.confirm("Do you want to delete row?");
//	 if(CanIStrike==false){
//		 return false;
//	 }
	 swal({
		  title: 'Are you sure?',
		  text: "Do you want to delete row?",
		  type: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Yes, Delete it!'
		}).then(function(result){
		  if (result==true) {
			  $("#tr-class"+id).addClass('strikeout');
			  $("#editItem"+id).attr("disabled", true).css('cursor','not-allowed');
			  $("#deleteRowBtn"+id).attr("disabled", true).css('cursor','not-allowed');
			  $("#areaActionValue"+id).val("R"); $("#tr-class"+id).addClass('strikeout');
			  $("#editItem"+id).attr("disabled", true).css('cursor','not-allowed');
			  $("#deleteRowBtn"+id).attr("disabled", true).css('cursor','not-allowed');
			  $("#areaActionValue"+id).val("R");
		  }
	});	
 }
 
 //delete the added  row
 function deleteRow(id){debugger;
	/* var n=0;
	 $(".tr-class").each(function(){debugger;
	 	var id=$(this).attr("id").split("tr-class")[1];
	 	n++;
	 	
	 });

	 if(n==1){
	 	alert("This row can't be deleted.");
	 	return false;
	 }*/
 	var rowscount=$('#expenditure_table').find('tr').length;
 	if(((rowscount)-1)==2){
		swal("Error!", "This row con't be deleted.", "error");
		return false;
	}
	   //removing row
		 swal({
			  title: 'Are you sure?',
			  text: "Do you want to delete row?",
			  type: 'warning',
			  showCancelButton: true,
			  confirmButtonColor: '#3085d6',
			  cancelButtonColor: '#d33',
			  confirmButtonText: 'Yes, Delete it!'
			}).then(function(result){
			  if (result==true) {
				  $("#tr-class"+id).remove();
					
					var tid=$('#expenditure_table tr:last').attr('id');	
					var res = tid.split("tr-class")[1];
					if(rowscount==3){
						$("#deleteRowBtn"+res).hide();
					}
					if(res<id){		
						$("#addRowBtn"+res).show();
					}	
			  }
		})
		
	  
	 
 }
//edit the row
 function editAvalibleRow(id){
	
	 swal({
		  title: 'Are you sure?',
		  text: "Do you want to edit row?",
		  type: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Yes, Edit it!'
		}).then(function(result){
		  if (result==true) {
			  /*$("#siteName"+id).removeAttr("readonly");
				 $("#UOM"+id).removeAttr("readonly");*/
				 $("#availableArea"+id).removeAttr("readonly");
				 $("#totalArea"+id).removeAttr("readonly");
				 $("#status"+id).removeAttr("readonly");	
				 $("#areaActionValue"+id).val("E");
		  }
	});	
	
 }
 function StatusChange(id){debugger;

/*	 var tempStatus=$("#status"+id).val();
	 if(tempStatus=='A'){
		 $("#status"+id).html("<option>--select--</option><option value='B'>B</option>")
		 console.log("A");
	 }
	 else{
		 $("#status"+id).html("<option>--select--</option><option value='A'>A</option>")
		 console.log("B");
	 }
	 console.log("hello")*/
 }
 
 function updateAvalibleAreaSubmit(){debugger;
	//validate table
	 var valTable=valUpdateArea();
	 if(valTable==false){
		 return false;
	 }
	
	 var noOfRows=[];
	 $(".tr-class").each(function(){
	 var currentId=$(this).attr("id").split("tr-class")[1];
	 noOfRows.push(currentId);
	 });
	 debugger;
	 //console.log("chargesRowCountNum: "+chargesRowCountNum);
	 $('#totalNoOfRows').val(noOfRows);
    /* var canISubmit = window.confirm("Do you want to update?");
	
	 if(canISubmit == false) {
		return false;
	 }*/
	 swal({
		  title: 'Are you sure?',
		  text: "Do you want to update?",
		  type: 'warning',
		  showCancelButton: true,
		  confirmButtonColor: '#3085d6',
		  cancelButtonColor: '#d33',
		  confirmButtonText: 'Yes, Update it!'
		}).then(function(result){
		  if (result==true) {
			  $('.loader-sumadhura').show();
				 $('#update_btn').attr("disabled", true);
				 document.getElementById("updateavalArea").action = "updateAvailableArea.spring";
				 document.getElementById("updateavalArea").method = "POST";
				 document.getElementById("updateavalArea").submit();
		  }
	})
	 
}

//comparing site name for unique site
function siteCompare(id){
	var currentSite=$("#siteName"+id).val().split("$")[1];
	var error=true;
	$(".sitenameclass").each(function(){
		if($(this).val()==currentSite){			
			swal("Error!", ""+$("#siteName"+id).val().split("$")[1]+" is already exist, Please choose different site.", "error");
			$("#siteName"+id).val("");
			$("#siteName"+id).focus();
			return  error=false;
		}
	});
	if(error==false){
		return false;
	}
	
}

function checkValue(strSerialNumber){
	var totalArea = $("#totalArea" +strSerialNumber).val()==''?0:$("#totalArea" +strSerialNumber).val();
	var availableArea = $("#availableArea" +strSerialNumber).val()==''?0:$("#availableArea" +strSerialNumber).val();
	if (isNaN(totalArea)) {
		   swal("Error!", "Please enter valid data.", "error");
		   $("#totalArea" +strSerialNumber).val("");
		   $("#totalArea" +strSerialNumber).focus();
		   return false;
	}
	if (isNaN(availableArea)) {
		   swal("Error!", "Please enter valid data.", "error");
		   $("#availableArea" +strSerialNumber).val("");
		   $("#availableArea" +strSerialNumber).focus();
		   return false;
	}
	if(totalArea!="" && availableArea!="" && parseFloat(totalArea)<parseFloat(availableArea)){
		swal("Error!", "Available area should be less than or equal to total area.", "error");
		$("#availableArea"+strSerialNumber).val('');
		return false;
	}
}


//validate update available area table

function valUpdateArea(){
	var error=true;
	$(".tr-class").each(function(){
		var id=$(this).attr("id").split("tr-class")[1];
		debugger;
		if($("#areaActionValue"+id).val()!="R"){
			if($("#siteName"+id).val()==""){
				swal("Error!", "Please select site.", "error");
				$("#siteName"+id).focus();
				return error=false;
			}
			if($("#totalArea"+id).val()==""){
				swal("Error!", "Please enter total area.", "error");
				$("#totalArea"+id).focus();
				return error=false;
			}
			if($("#availableArea"+id).val()==""){
				swal("Error!", "Please enter available area.", "error");
				$("#availableArea"+id).focus();
				return error=false;
			}
			if($("#status"+id).val()==""){
				swal("Error!", "Please select status.", "error");
				$("#status"+id).focus();
				return error=false;
			}
		}		
	});
	return error;
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