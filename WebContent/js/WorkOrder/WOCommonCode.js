function showWDGroupWiseMaterialDetails(rowsIncre){
	debugger;
	console.log("showWDGroupWiseMaterialDetails from WOCommonCode.js");
 	var WD=$("#comboboxsubSubProd"+rowsIncre).val();
 	if(WD==null||WD.length==0){
 		WD=$("#WODESC"+rowsIncre).val();
 	}
	if(WD==null||WD.length==0){
		alert("Please select WD.");
		return false;
	}
	WD=WD.split("$")[0];
	 var siteId=$("#siteId").val();
	 var workOrderTypeOfWork=$("#typeOfWork").val();
	 var WorkOrderNo=$("#WorkOrderNo").val()==undefined?$("#workOrderNo").val():$("#WorkOrderNo").val();
	 var operType=$("#operType").val();
	 if(operType=="reviseWorkOrder"){
		 siteId=$("#site_id").val();
		 WorkOrderNo=$("#workOrderId").val();
	 }
	 var url="viewMaterialProductWDDetails.spring";
	 
	$.ajax({
		  url : url,
		  type : "get",
		  contentType : "application/json",
			data:{
				workDescId:WD,
				siteId:siteId,
				typeOfWork:workOrderTypeOfWork
			},
		  success : function(data) {
				//alert(data);
				debugger;
				var htmlData="";
				var rows=1;
				$.each(data,function(key,value){
					debugger;
					htmlData+="<tr>";
					htmlData+="<td>"+rows+"</td>";
					htmlData+="<td>"+value.WO_WORK_DESCRIPTION+"</td>";
					htmlData+="<td class='anchor-class' style='cursor: pointer;' onclick='loadAreaWiseWDMaterialDetails("+rowsIncre+",\""+value.MATERIAL_GROUP_ID+"\")'>"+value.MATERIAL_GROUP_NAME+"</td>";
					htmlData+="<td>"+value.MATERIAL_GROUP_MST_NAME+"</td>";
					htmlData+="<td>"+value.TOTAL_QUANTITY+"</td>";
					htmlData+="</tr>";
					rows++;
				});
				$("#showWDGroupWiseMaterialDetails").html(htmlData);
				if(htmlData.length==0){
					$("#materialAreaDetails").html("<h3>No material data found.</h3>");
				}
				$("#ViewMaterialProductDetails").modal();
			}
	});
}

function loadAreaWiseWDMaterialDetails(rowsIncre,materialGroupId){
	debugger;
	console.log("loadAreaWiseWDMaterialDetails from WOCommonCode.js");
	var WD=$("#comboboxsubSubProd"+rowsIncre).val();
	if(WD==null||WD.length==0){
 		WD=$("#WODESC"+rowsIncre).val();
 	}
	if(WD==null||WD.length==0){
		alert("Please select WD.");
		return false;
	}
	WD=WD.split("$")[0];
	var siteId=$("#siteId").val();
	var workOrderTypeOfWork=$("#typeOfWork").val();
	var WorkOrderNo=$("#WorkOrderNo").val();
	 var operType=$("#operType").val();
	 if(operType=="reviseWorkOrder"){
		 siteId=$("#site_id").val();
		 WorkOrderNo=$("#workOrderId").val();
	 }
	var urlForAreaWiseDivisionDetails="viewMaterialAreaWiseDetails.spring";
	
	$.ajax({
		  url : urlForAreaWiseDivisionDetails,
		  type : "get",
		  contentType : "application/json",
			data:{
				workDescId:WD,
				siteId:siteId,
				typeOfWork:workOrderTypeOfWork,
				materialGroupId:materialGroupId
			},
		  success : function(data) {
				//alert(data);
				debugger;
				var htmlData="";
				var rows=1;
				$.each(data,function(key,value){
					htmlData+="<tr>";
					htmlData+="<td>"+rows+"</td>";
					htmlData+="<td>"+value.WO_WORK_DESCRIPTION+"</td>";
					htmlData+="<td>"+value.MATERIAL_GROUP_NAME+"</td>";
					htmlData+="<td>"+value.MATERIAL_GROUP_MST_NAME+"</td>";
					htmlData+="<td>"+value.BLOCK_NAME+"</td>";
					htmlData+="<td>"+(value.FLOOR_NAME==null?'-':value.FLOOR_NAME)+"</td>";
					htmlData+="<td>"+(value.FLAT_NAME==null?'-':value.FLAT_NAME)+"</td>"; 
					htmlData+="<td>"+value.TOTAL_QUANTITY+"</td>";
					/* htmlData+="<td>0</td>";
					htmlData+="<td>"+value.TOTAL_QUANTITY+"</td>"; */
					htmlData+="</tr>";
					rows++;
				});
				$("#showAreaWiseWDGroupMaterialDetails").html(htmlData);
				$("#ViewAreaWiseMaterialDetails").modal();
				// now make it draggable
				$('#ViewAreaWiseMaterialDetails').draggable();  
			}
	});
}