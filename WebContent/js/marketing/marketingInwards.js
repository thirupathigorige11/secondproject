var childData;

//for combobox
/*$(function() {
	$( "#exapendChildproduct1").combobox();	
  });  
  $(function() {
	$("#exapendLocation1").combobox();
  });*/
  
function appendModalRow(id){debugger;
	var newId=id+1;
	var addRow='<tr><td>'+newId+'</td><td><select id="exapendChildproduct'+newId+'" class="form-control"></select></td><td><select id="exapendLocation'+newId+'" class="form-control"><option>Hyderabad</option><option>Banglore</option></select></td><td><input type="text" class="form-control"/></td><td><input type="text" class="form-control"/></td><td><input type="text" class="form-control" id="sitewisefromDate'+newId+'" placeholder="Select From Date"/></td><td><input type="text" class="form-control" id="sitewisetoDate'+newId+'" placeholder="Select To Date"/></td><td><select class="form-control"><option>-- Please Select Site --</option><option>Acropolis</option><option>Eden Gardens</option><option>Soham</option><option>Kondapur</option></select></td><td><button class="btnaction btnpadd" onclick="appendModalRow(1)"><i class="fa fa-plus"></i></button><button class="btnaction"><i class="fa fa-trash"></i></button></td></tr>';
	$("#expenditure"+id).after(addRow);
	var appendData="<option>--select--</option>";
	for(var i=0;i<childData.length;i++){
		appendData+="<option>"+childData[0]+"</option>";
	}
	
	//for combobox
	/*$(function() {
	  $( "#exapendChildproduct"+newId).combobox();	
	});	  
	$(function() {
	  $("#exapendLocation"+newId).combobox();
	});*/
	//for datepicker
	$("#sitewisefromDate"+newId).datepicker();
	$("#sitewisetoDate"+newId).datepicker();
	//append child product
	$("#exapendChildproduct"+newId).append(appendData);
	
}
//load child product 
function loadchildDataInModal(){
	childData=[];
	$(".childproduct").each(function(){
		if($(this).val()!=''){
			childData.push($(this).val());
		}
	});
	//console.log("childData: "+childData);
}

function openCalExp(){debugger;
	//load child data
	loadchildDataInModal();
	//opening modal
	$("#anchorMarket").attr("data-toggle", "modal");
	$("#anchorMarket").attr("data-target", "#modal-marketing");	
	//append select box data 
	var appendData="<option>--select--</option>";
	for(var i=0;i<childData.length;i++){
		appendData+="<option>"+childData[0]+"</option>";
	}	
	$("#exapendChildproduct1").append(appendData);
	
}