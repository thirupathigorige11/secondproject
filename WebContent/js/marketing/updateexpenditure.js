$(document).ready(function(){$(".up_down").click(  function() {
	$(this).find('span').toggleClass('fa-chevron-up fa-chevron-down');
	$(this).find('span').toggleClass('fa-chevron-right fa-chevron-left');
 });	
});
//for childproduct data  
var childproducttemp;
//for child product total amount
var childproductTotalAmount;
//for child product Quantity
var childprdctquantity;
//for child product sitenames
var tempchildprdctsitename;
//for child product locations
var tempchildproductlocation;
//for child product from dates
var tempsitefromdate;
//for child product to dates
var tempsitetodate;
//for child product siteId
var tempsiteId;
//for child product time
var temptime;
//for child product hoarding Id
var temphoadingId;
//for child product childproduct id
var tempchildproductIds;

//for site name which are comming from ajax call
var siteNamesFromAjax;

//model poup function start
function openCalExp(){ 
	childproducttemp = [];
    childprdctquantity = [];
	childproductTotalAmount = [];
	tempchildprdctsitename = [];
	tempchildproductlocation = [];
	tempsitefromdate = [];
	tempsitetodate = [];
	
	tempsiteId = [];
	temptime = [];
	temphoadingId = [];
	tempchildproductIds = [];
	//storing hording details into  temphoadingId variable
	$(".hodingId").each(function(){
	 	var temp = $(this).text();
    	temphoadingId.push(temp);
	});
	//storing child product id's into  tempchildproductIds variable
	$(".childproductIds").each(function(){
	 	var temp = $(this).text();
    	tempchildproductIds.push(temp);
	});
	//storing time details into  temptime variable
   $(".sitetime").each(function(){
    	var temp = $(this).text();
    	temptime.push(temp);
    });
   //storing site id's details into  tempsiteId variable
	$(".siteIds").each(function(){
    	var temp = $(this).text();
    	tempsiteId.push(temp);
    
    });
	//storing child products  into  childproducttemp variable
	$(".childprdctcls").each(function(){
    	var temp = $(this).text();
    	childproducttemp.push(temp);
    
    });
	//removing duplicates of child product and getting unique values 
	var uniqueNames = [];
	for(i = 0; i< childproducttemp.length; i++){    
	    if(uniqueNames.indexOf(childproducttemp[i]) === -1){
	        uniqueNames.push(childproducttemp[i]);        
	    }        
	}
	childproducttemp=uniqueNames;
	
	//storing site quantity   into  childprdctquantity variable
    $(".sitequantity").each(function(){
    	var temp = $(this).text();
    	childprdctquantity.push(temp);
    });
   //storing childproduct amount  into  childprdctquantity variable
	$(".siteamountcls").each(function(){
		var tempval = $(this).text();
		childproductTotalAmount.push(tempval);
	}); 
	//storing site names   into  tempchildprdctsitename variable
	$(".childproductsitename").each(function(){
		var tempval = $(this).text();
		tempchildprdctsitename.push(tempval);
	}); 
	//storing locations  into  tempchildproductlocation variable
	$(".childproductlocation").each(function(){
		var tempval = $(this).text();
		tempchildproductlocation.push(tempval);
	}); 
	//storing site from date   into  tempsitefromdate variable
	$(".sitefromdate").each(function(){
		var tempval = $(this).text();
		tempsitefromdate.push(tempval);
	});
	//storing site to date   into  tempsitetodate variable
	$(".sitetodate").each(function(){
		var tempval = $(this).text();
		tempsitetodate.push(tempval);
	});
	//opening modal popup
	 $("#anchorMarket").attr("data-toggle", "modal");
	 $("#anchorMarket").attr("data-target", "#modal-marketing");
}  //openCalExp function close
getsiteNames();

//load site wise trable
function loadsiteWiseTable(){
	var addrow='';
	 var addmultirow='';
	 var addlocationwise='';
	 var newId = 0;
	 
	 getSiteNamesForMulti(childproducttemp.length);
	 for(var i=0; i<childproducttemp.length;i++){
	 	newId=i+1;
	 	 addrow += '<tr id="siteAdd'+newId+'" class="siteclass"><td ><input type="text" id="singleSiteChild'+newId+'" name="singleSiteChild'+newId+'" class="form-control" data-toggle="tooltip" title="'+childproducttemp[i].split("$")[1]+'" value="'+childproducttemp[i].split("$")[1]+'" readonly><input type="hidden" id="hiddensingleSiteChild'+newId+'" name="hiddensingleSiteChild'+newId+'" class="form-control" value="'+childproducttemp[i]+'"><select style="display:none;" class="form-control sitecls" onchange="expenditureChildProductChange('+newId+', this)" name="childdata'+newId+'" id="childdata'+newId+'"></select></td><td><select class="form-control sitearea" name="singleSiteAreaName'+newId+'" id="singleSiteAreaName'+newId+'" onchange="siteWiseLocationChange('+newId+')"></select></td><td><input type="text" class="form-control ExpiryDate" name ="SingleSiteFromDate'+newId+'" id="SingleSiteFromDate'+newId+'" placeholder="Select From Date" autocomplete="off"/></td><td><input type="text" class="form-control ExpiryDate " id="sitewisetoDate'+newId+'" name="singleSiteToDate'+newId+'" placeholder="Select To Date" autocomplete="off"/></td><td><input class="form-control"  type="text"  name="sitewisetime'+newId+'" id="sitewisetime'+newId+'" placeholder="Select Time" autocomplete="off"></td><td><input type="text" id="sitequanty'+newId+'" name="singleSiteQuantity'+newId+'" onkeyup="siteWiseQtyAndTotalAmount('+newId+',this)" class="form-control" autocomplete="off" readonly></td><td><input type="text" class="form-control" name="singleSiteRate'+newId+'" id="splitamount'+newId+'" onkeyup="siteWiseAmountChange('+newId+')" autocomplete="off" readonly></td><td><input type="text" class="form-control totalamountclass" name="singleSiteAmount'+newId+'" id="totalamountsite'+newId+'" onkeyup="siteWiseQtyAndTotalAmount('+newId+',this)" autocomplete="off" readonly></td><td><input type="text" class="form-control siteneme"  name="sitename'+newId+'" id="sitename'+newId+'" data-toggle="tooltip"  readonly></td><input type="hidden" class="hoardingId'+newId+'" id="hoardingId'+newId+'" name="singleSiteHoardingId'+newId+'"><input type="hidden" class="childproductid'+newId+'" id="childproductidd'+newId+'" name="singleSiteChildProductId'+newId+'"><input type="hidden" class="siteIdd'+newId+'" id="singleSiteSiteId'+newId+'" name="singleSiteSiteId'+newId+'"></tr>';
	   }
	 $("#siteAdd").html(addrow);
	 $('[data-toggle="tooltip"]').tooltip(); 
	 
	 var addchilddata = "<option>--Select Child Product--</option>";
	 for(var i=0;i<childproducttemp.length;i++){
	 	addchilddata+='<option value="'+childproducttemp[i]+'">'+childproducttemp[i].split("$")[1]+'</option>';
	 }
	 var newId = 0;
	 for(var i=0; i<childproducttemp.length; i++){
		    newId=i+1;
		    $("#childdata"+newId).html(addchilddata);
			$("#sitewisetime"+(i+1)).timepicker({
				 timeFormat: 'h:mm p',
				 interval: 10,
				 minTime: '00.00AM',
				 maxTime: '11.59PM',
				 startTime: '00:00',
				 dynamic: false,
				 dropdown: true,
				 scrollbar: true
			});
	 }
	 $(".ExpiryDate").datepicker({ 
		  dateFormat: 'dd-M-y',
		 /* minDate:0,*/
		  changeMonth: true,
	      changeYear: true
	});
	 
	 var singleSiteTable=$("#siteWiseSite").find("tbody").find("tr").length;
	 var SingleSiteName=$("#siteWiseSitename").val();
	 for(var i=0; i<singleSiteTable-1;i++){
		var singleSiteChild=$("#hiddensingleSiteChild"+(i+1)).val();
		var expenditure_table=$("#expenditure_table1").find("tbody").find("tr").length;
		var expenditureTotalAmount=0;
		var loadornot=0;
		for(var j=0;j<expenditure_table;j++){
			if(singleSiteChild==$("#HiddenChildId"+(j+1)).text()){
				$("#SingleSiteFromDate"+(i+1)).val($("#siteFromDate"+(j+1)).text());
				$("#sitewisetoDate"+(i+1)).val($("#siteToDate"+(j+1)).text());
				$("#sitewisetime"+(i+1)).val($("#siteTime"+(j+1)).text());
				$("#sitequanty"+(i+1)).val($("#sitequantity"+(j+1)).text());
				expenditureTotalAmount+=parseFloat($("#siteamount"+(j+1)).text());
			}
			$("#totalamountsite"+(i+1)).val(expenditureTotalAmount.toFixed(2));			
		}
		$(".childproductsitename").each(function(){
			var expendatureChildname=$(this).text();
			var splitId=$(this).attr("id").split("siteSiteName")[1];
			if(SingleSiteName.split("$")[1]==expendatureChildname){
				if($("#siteLocationName"+(j+1)).text()!=""){
					$("#singleSiteAreaName"+(i+1)).html("<option value='"+$("#expendatureLocationName"+splitId).val()+"'>"+$("#siteLocationName"+splitId).text()+"</option>");
					$("#singleSiteAreaName"+(i+1)).val($("#expendatureLocationName"+splitId).val());
				}
				/*if($("#siteLocationName"+splitId).text()!=''){*/
					//loadornot++;
					//Areaforall($("#hiddensingleSiteChild"+(i+1)).val(), (i+1));
				/*}*/
				
			}
		})
		/*if(loadornot==0){
			Areaforall($("#hiddensingleSiteChild"+(i+1)).val(), (i+1));
		}*/
		Areaforall($("#hiddensingleSiteChild"+(i+1)).val(), (i+1));
		siteWiseQtyAndTotalAmount((i+1), "val");
	}
	 
}


//load multi site wise trable
function loadMultiSiteWiseTable(){
	var addrow='';
	 var addmultirow='';
	 var addlocationwise='';
	 var newId = 0;
	 
	 getSiteNamesForMulti(childproducttemp.length);
	 for(var i=0; i<childproducttemp.length;i++){
	 	newId=i+1; // onchange="multiSiteSiteNameChange('+newId+')"
	 	addmultirow += '<tr id="multisiteAdd'+newId+'" class="MultipleSite"><td>'+newId+'</td><td><select name="exapendChildproduct'+newId+'" id="exapendChildproduct'+newId+'" class="form-control childprdctclsmulti" onchange="expendituremultiChildProductChange('+newId+', this)"></select></td><td><select  class="form-control multiSiteAreaname" name="multiareaname'+newId+'" id="multiareaname'+newId+'" onchange="multiSiteSiteNameChange('+newId+')"></select></td><td><select id="sitenameid'+newId+'"  name="multiSieSiteName'+newId+'" class="form-control multsitenamecls" onchange="multiSiteSiteNameChange('+newId+')"></select></td><td><input onkeyup="multisiteQtyAndTotalChange('+newId+',this)" name="multiSiteQuanty'+newId+'" id="multisitequanty'+newId+'" type="text"class="form-control"/></td><td><input type="text" class="form-control ExpiryDate" id="multiwisefromDate'+newId+'"   name="multiSiteFromDate'+newId+'" placeholder="Select From Date" autocomplete="off"/></td><td><input type="text" class="form-control ExpiryDate" id="multsideTodate'+newId+'"   name="multiSiteTodate'+newId+'" placeholder="Select To Date" autocomplete="off"/></td><td><input class="form-control"  type="text"  name="multisitewisetime'+newId+'" id="multisitewisetime'+newId+'" placeholder="Select Time" autocomplete="off"></td><td><input id="multisplitamount'+newId+'"  name="multiSiteRate'+newId+'" type="text" class="form-control" onkeyup="multisiteWiseAmountChange('+newId+')" autocomplete="off"/></td><td><input id="totalamountmultsite'+newId+'"    name="multiSiteTotalAmount'+newId+'"  type="text" class="form-control totalamountmultisiteclass" onkeyup="multisiteQtyAndTotalChange('+newId+',this)" autocomplete="off"/></td><td><button type="button" class="btnaction btnpadd" id="addBtn'+newId+'" onclick="appendModalRow('+newId+')"><i class="fa fa-plus"></i></button><button type="button" class="btnaction" id="deleteBtn'+newId+'" onclick="deleteRow('+newId+')"><i class="fa fa-trash"></i></button></td><input type="hidden" class="siteIdd'+newId+'" id="siteIdd'+newId+'" name="siteId'+newId+'"><input type="hidden" class="hoardingId'+newId+'" id="hoardingId'+newId+'" name="hoardingId'+newId+'"><input type="hidden" class="childproductid'+newId+'" id="childproductidd'+newId+'" name="childproductid'+newId+'"><input type="hidden" class="siteIdd'+newId+'" id="siteIdd'+newId+'" name="siteId'+newId+'"></tr>';
	 	
	 }
	 $("#multisiteAdd").html(addmultirow);
	 
	 var addchilddata = "<option>--Select Child Product--</option>";
	 for(var i=0;i<childproducttemp.length;i++){
	 	addchilddata+='<option value="'+childproducttemp[i]+'">'+childproducttemp[i].split("$")[1]+'</option>';
	 }
	 var newId = 0;
	 var templength = $(".view_expendature").length;
	 for(var i=0; i<templength; i++){
		    newId=i+1;
		   $("#exapendChildproduct"+newId).html(addchilddata);		
			$("#multisitewisetime"+(i+1)).timepicker({
				 timeFormat: 'h:mm p',
				    interval: 10,
				    minTime: '00.00AM',
				    maxTime: '11.59PM',
				    startTime: '00:00',
				    dynamic: false,
				    dropdown: true,
				    scrollbar: true
			});
	 }
	 $(".ExpiryDate").datepicker({ 
		  dateFormat: 'dd-M-y',
		/*  minDate:0,*/
		  changeMonth: true,
	     changeYear: true
	});
	 
	 //get sitenames for single site
	 getsiteNames()
}

//multisite Quantity And Total Amount Change
function multisiteQtyAndTotalChange(id, val){
	var tempamount = parseFloat($("#totalamountmultsite"+(id)).val()==''?0:$("#totalamountmultsite"+(id)).val()/ $("#multisitequanty"+(id)).val());
	if($("#multisitequanty"+(id)).val()=='' || $("#multisitequanty"+(id)).val()=="0"){ 	tempamount=0; }
	$("#multisplitamount"+id).val(tempamount.toFixed(2));
	multiSiteGrandTotal();
}


//multi site wise grand total
function multiSiteGrandTotal(){
	var grandTotal1=0;
	$(".totalamountmultisiteclass").each(function(){
	if($(this).val() == ""){
		grandTotal1 += 0;
	}else{
		grandTotal1  +=  parseFloat($(this).val());	
	}
  });
	$("#grandTotalmultisite").html(grandTotal1); 
}



//get location based on childproduct Id
function Areaforall(val, id){
$.ajax({
	url:"loadAndSetAreaData.spring?childProductId="+val.split("$")[0],
	type:"GET",
	success:function(response){
			var result = eval('(' + response + ')');
			 var addsitearea = "<option>--Select Location--</option>";
			 if(result.xml){
				 for(var i=0; i<result.xml.Area.length;i++){						 
					 addsitearea+='<option value="'+result.xml.Area[i].AreaId+"$"+result.xml.Area[i].AreaName+'">'+result.xml.Area[i].AreaName+'</option>';
				}
			 } 
			 else{
				 console.log("no locations;");
			 } 
			var siteType=$("#expendatureForId").val();
			if(siteType=="SiteWise"){
				$("#singleSiteAreaName"+id).append(addsitearea); 
			}else{
				$("#multiareaname"+id).html(addsitearea);
			}
		}
	}); 
}

//get site names 
function getsiteNames(){
	$.ajax({
	 	url:"siteNameDetails.spring",
		type:"GET",
		success:function(response){
			    siteNamechild =response ;
				var result = eval('(' + response + ')');
				var addsitedata = "<option value='0'>--Please Select Site--</option>";
				for(var i=0; i<result.xml.site.length;i++){
					addsitedata+='<option value="'+result.xml.site[i].SITEID+'$'+result.xml.site[i].SITENAME+'">'+result.xml.site[i].SITENAME+'</option>';
				}
				$("#siteWiseSitename").html(addsitedata); 
		}
	}); 
}

//get the location dropdown values for location wise site
function getLOcation(){
 $.ajax({
	 	url:"siteLocationDetails.spring",
		type:"GET",
		success:function(response){
			    locationChild = response;
				var result = eval('(' + response + ')');
				var addlocationdata = "<option>--Select Location--</option>";
				for(var i=0; i<result.xml.locationname.area.length;i++){
					addlocationdata+='<option>'+result.xml.locationname.area[i]+'</option>';
				}
				$("#stateSelc").html(addlocationdata); 
				$(".multiloccls").html(addlocationdata); 
		}
		}); 
}

$("#stateSelc").change(function(){
 var stateId=$(this).val();

 CallingperSiteCalculation(stateId, "MultipleSite");
});

//single site child product change
function expenditureChildProductChange(id, val){
	Areaforall($(val).val().split("$$")[0], id);
	var TempChildProductValue = $(val).val().split("$")[0];
	var MainrowLength = $(".view_expendature").length;
	var tempgranttotal = 0;
	var tempamount = 0;
	
	for(var i=0;i<MainrowLength;i++){
		var tempdownchild = $("#HiddenChildId"+(i+1)).text().split("$")[0];
	
		if(TempChildProductValue ==tempdownchild ){
			$("#sitequanty"+id).val($("#sitequantity"+(i+1)).text()); 
			$("#totalamountsite"+id).val($("#siteamount"+(i+1)).text());
			$("#areaname"+id).val(tempchildproductlocation[i+1]);
			$("#sitewisefromDate"+id).val(tempsitefromdate[i+1]);
			$("#sitewisetoDate"+id).val(tempsitetodate[i+1]);
			$("#siteIdd"+id).val(tempsiteId[i+1]);
			$("#sitewisetime"+id).val(temptime[i+1]);
			$("#hoardingId"+id).val(temphoadingId[i+1]);
			$("#childproductidd"+id).val(tempchildproductIds[i+1]);
		
			tempamount = $("#siteamount"+(i+1)).text()/$("#sitequantity"+(i+1)).text();
			$("#splitamount"+id).val(tempamount.toFixed(2));
			var grandTotal1=0;
			$(".totalamountclass").each(function(){
			if($(this).val() == ""){
				grandTotal1 = 0;
			}else{
				grandTotal1  +=  parseFloat($(this).val());
				$("#grandTotalsite").html(grandTotal1); 
			}
		 });
		} // TempChildProductValue condition close
	}  // for loop close
	 
}  // expenditureChildProductChange function close

//multi site child product change
function expendituremultiChildProductChange(id,val){
	
	Areaforall($(val).val().split("$$")[0], id);

	//multiSiteSiteNameChange(id);
	var TempChildProductValue = $(val).val().split("$")[0]; //.split("$$")[1]
	var MainrowLength = $(".view_expendature").length;
	var tempgranttotal = 0;
	var tempamount = 0;
	var tempchildData = childproducttemp;
	
	for(var i=0;i<tempchildData.length;i++){
		 var expenditureTotal=0;
		 var childQty=0;
			var expenditure_tableLength=$("#expenditure_table1").find("tbody").find("tr").length;
			for(var k=0;k<expenditure_tableLength;k++){
		        if(childproducttemp[i].split("$")[0]==$("#HiddenChildId"+(k+1)).text().split("$")[0]){
		        	expenditureTotal+=parseFloat($("#siteamount"+(k+1)).text());
		        	childQty+=parseFloat($("#sitequantity"+(k+1)).text());
				}
			}
		var tempdownchild = $("#HiddenChildId"+(i+1)).text().split("$")[0];
		if(TempChildProductValue == tempdownchild){
			$("#multisitequanty"+id).val(childQty.toFixed(2)); 
			$("#totalamountmultsite"+id).val(expenditureTotal.toFixed(2));
			$("#multisiteid"+id).val(tempchildprdctsitename);
			$("#multiLocation"+id).val(tempchildproductlocation);
			$("#siteIdd"+id).val(tempsiteId);			
			$("#hoardingId"+id).val(temphoadingId);
			$("#childproductidd"+id).val(tempchildproductIds);
			
			var tempAmount =expenditureTotal/$("#multisitequanty"+id).val();
			if($("#multisitequanty"+id).val()=='' || $("#multisitequanty"+id).val()==0){
				tempAmount=0;
			}
			$("#multisplitamount"+id).val(tempAmount.toFixed(2));
			multiSiteGrandTotal();
		}
	}
	multiSiteSiteNameChange(id);
}

//load location wise table
function CallingperSiteCalculation(stateId, expendatureType){
 var month_year=$("#invoiceDateHiddenId").val();
 $.ajax({
	 	url:"getAvailableAreaForSale.spring",
		type:"POST",
		Contenttype:" application/json",
		data : {
			 "location": ""+stateId,
			"expendatureType": ""+expendatureType,
			"month_year" : month_year
		},
		success:function(response){
			var global_response;
			var addbrandwise ='';
			var newId = 0;
			var num = 0;
			var tempchildData = childproducttemp;
			var tempchildquantity = childprdctquantity;
			var tempchildproductTotalAmount = childproductTotalAmount;
			var tempresp = eval('('+response+')');
			$("#tempshowhide").show();
			if(!tempresp.xml){
				$("#locationAdd").html("<tr><td style='text-align:center;border:1px solid #000;' colspan='9'>No Data</td></tr>");
				$("#locationGrandTotal").hide();
				return false;
			}
			global_response = tempresp.xml.site;
			var childproductlength = $(".childprdctcls").length;
			var AmountForProductForSite = 0;
			var grandTotal =0;
			var addlocationwise = '';
			 for(var i=0;i<tempchildData.length;i++){
				 var expenditureTotal=0;
					var expenditure_tableLength=$("#expenditure_table1").find("tbody").find("tr").length;
					for(var k=0;k<expenditure_tableLength;k++){
				        if(childproducttemp[i].split("$")[0]==$("#HiddenChildId"+(k+1)).text().split("$")[0]){
				        	expenditureTotal+=parseFloat($("#siteamount"+(k+1)).text())
						}
					}
				 if(global_response.siteId){
							newId=num+1;
							var AmountForProductForSite = expenditureTotal * (tempresp.xml.site.availableArea/tempresp.xml.site.totalArea);
							var basicAmount  =  AmountForProductForSite/tempchildquantity[i];				
							grandTotal += AmountForProductForSite;				
							var siteId = tempresp.xml.site.siteId;
							addlocationwise  += '<tr  id="locationAdd'+newId+'" class="locationWiseclass"><td ><input  type="text" class="form-control childprdctsLocation" name="childdata'+newId+'" id="childdata'+newId+'" data-toggle="tooltip" title="'+childproducttemp[i].split('$')[1]+'"  value="'+childproducttemp[i].split('$')[1]+'" readonly><input  type="hidden" class="LocationChildcls" name="LocationChildId'+newId+'" id="LocationChildId'+newId+'" value="'+childproducttemp[i]+'"></td><td><select class="form-control" id="LocationLocationId'+newId+'" name="LocationLocationId'+newId+'" onchange="locationWiseLocationChange('+newId+')"></select></td><td><input type="text" class="form-control ExpiryDate" value="" id="locationWiseFromDate'+newId+'" name="locationWiseFromDate'+newId+'" placeholder="Select From Date" autocomplete="off"/></td><td><input type="text" class="form-control ExpiryDate" id="locationWiseToDate'+newId+'"  name="locationWiseToDate'+newId+'"  placeholder="Select To Date" autocomplete="off"/></td><td><input class="form-control"  type="text"  name="locationwisetime'+newId+'" id="locationwisetime'+newId+'" placeholder="Select Time" autocomplete="off"></td><td><input onkeyup="locationquantityAndTotalAmount('+newId+')"  id="locationquantity'+newId+'"  name="locationWiseQuantity'+newId+'"  type="text" class="form-control" value="'+tempchildquantity[i]+'" autocomplete="off" readonly></td><td><input type="text" class="form-control" name="locationWistRate'+newId+'"   id="locationtotalAmount'+newId+'" value="'+basicAmount.toFixed(2)+'" onkeyup="locationWiseAmountChange('+newId+')" autocomplete="off" readonly></td><td><input value="'+AmountForProductForSite.toFixed(2)+'"  id="locationsplitamount'+newId+'" name="locationWiseTotalAmount'+newId+'"  onkeyup="locationquantityAndTotalAmount('+newId+')" type="text" class="form-control locationWiseTotalCls" autocomplete="off" readonly></td><td ><input type="text" class="form-control sitenamelocationwise" name="locationWiseSiteName'+newId+'" id="locationlocationwise'+newId+'" data-toggle="tooltip" title="'+tempresp.xml.site.siteName+'" value="'+tempresp.xml.site.siteName+'" readonly></td><input type="hidden" class="hoardingId'+newId+'" id="hoardingId'+newId+'" value="'+temphoadingId[i]+'"  name="locationWiseHoardingId'+newId+'" ><input type="hidden" class="childproductid'+newId+'" id="childproductidd'+newId+'" value="'+childproducttemp[i]+'"  name="locationWiseChildproductid'+newId+'"><input type="hidden" class="siteIdd'+newId+'" id="siteIdd'+newId+'" value="'+tempresp.xml.site.siteId+'"  name="locationWiseSiteId'+newId+'"></tr>';
							getLocationForLocandBranding(tempchildData[i], num);
							num++;
				 }
				 else{
					 for(var j=0; j<global_response.length;j++){
							newId=num+1;
							var AmountForProductForSite = expenditureTotal * (tempresp.xml.site[j].availableArea/tempresp.xml.site[j].totalArea);
							var basicAmount  =  AmountForProductForSite/tempchildquantity[i];				
							grandTotal += AmountForProductForSite;				
							var siteId = tempresp.xml.site[j].siteId;
							addlocationwise  += '<tr  id="locationAdd'+newId+'" class="locationWiseclass"><td ><input  type="text" class="form-control childprdctsLocation" name="childdata'+newId+'" id="childdata'+newId+'" data-toggle="tooltip" title="'+childproducttemp[i].split('$')[1]+'" value="'+childproducttemp[i].split('$')[1]+'" readonly><input  type="hidden" class="LocationChildcls" name="LocationChildId'+newId+'" id="LocationChildId'+newId+'"  value="'+childproducttemp[i]+'"></td><td><select class="form-control" id="LocationLocationId'+newId+'" name="LocationLocationId'+newId+'" onchange="locationWiseLocationChange('+newId+')"></select></td><td><input type="text" class="form-control ExpiryDate" value="" id="locationWiseFromDate'+newId+'" name="locationWiseFromDate'+newId+'" placeholder="Select From Date" autocomplete="off"/></td><td><input type="text" class="form-control ExpiryDate" id="locationWiseToDate'+newId+'"  name="locationWiseToDate'+newId+'"  placeholder="Select To Date" autocomplete="off"/></td><td><input class="form-control"  type="text"  name="locationwisetime'+newId+'" id="locationwisetime'+newId+'" placeholder="Select Time" autocomplete="off"></td><td><input onkeyup="locationquantityAndTotalAmount('+newId+')"  id="locationquantity'+newId+'"  name="locationWiseQuantity'+newId+'"  type="text" class="form-control" value="'+tempchildquantity[i]+'" autocomplete="off" readonly></td><td><input type="text" class="form-control" name="locationWistRate'+newId+'"   id="locationtotalAmount'+newId+'" value="'+basicAmount.toFixed(2)+'" onkeyup="locationWiseAmountChange('+newId+')" autocomplete="off" readonly></td><td><input value="'+AmountForProductForSite.toFixed(2)+'"  id="locationsplitamount'+newId+'" name="locationWiseTotalAmount'+newId+'" onkeyup="locationquantityAndTotalAmount('+newId+')" type="text" class="form-control locationWiseTotalCls" autocomplete="off" readonly></td><td ><input type="text" class="form-control sitenamelocationwise" name="locationWiseSiteName'+newId+'" id="locationlocationwise'+newId+'" data-toggle="tooltip" title="'+tempresp.xml.site[j].siteName+'" value="'+tempresp.xml.site[j].siteName+'" readonly></td><input type="hidden" class="hoardingId'+newId+'" id="hoardingId'+newId+'" value="'+temphoadingId[i]+'"  name="locationWiseHoardingId'+newId+'" ><input type="hidden" class="childproductid'+newId+'" id="childproductidd'+newId+'" value="'+tempchildproductIds[i]+'"  name="locationWiseChildproductid'+newId+'"><input type="hidden" class="siteIdd'+newId+'" id="siteIdd'+newId+'" value="'+tempresp.xml.site[j].siteId+'"  name="locationWiseSiteId'+newId+'"></tr>';
							num++;
						}
					 getLocationForLocandBranding(tempchildData[i], num);
				 }
				
			}
			$("#locationAdd").html(addlocationwise);
			$('[data-toggle="tooltip"]').tooltip(); 
			$("#grandTotal").html(grandTotal.toFixed(2));
			
			for(var i=0;i<num;i++){
					$("#locationWiseFromDate"+(i+1)).datepicker({ 
						  dateFormat: 'dd-M-y',
						 /* minDate:0,*/
						  changeMonth: true,
					      changeYear: true
					 });
					$("#locationWiseToDate"+(i+1)).datepicker({ 
						  dateFormat: 'dd-M-y',
						 /* minDate:0,*/
						  changeMonth: true,
					      changeYear: true
					 });
					$("#locationwisetime"+(i+1)).timepicker({
						 timeFormat: 'h:mm p',
						    interval: 10,
						    minTime: '00.00AM',
						    maxTime: '11.59PM',
						    startTime: '00:00',
						    dynamic: false,
						    dropdown: true,
						    scrollbar: true
					});
				}
			var LocationTable=$("#tempshowhide").find("tbody").find("tr").length;
			for(var i=0;i<LocationTable-1;i++){
				var expenditure_table=$("#expenditure_table1").find("tbody").find("tr").length;
				var LocationChild=$("#LocationChildId"+(i+1)).val();
				var LocationSite=$("#locationlocationwise"+(i+1)).val();
				var HiddenLocationChild=$("#LocationChildId"+(i+1)).val().split("$")[0];
				var testChild=0;
				$(".childprdctcls").each(function(){
					var currentVal=$(this).text().split("$")[0];
					if(HiddenLocationChild==currentVal){
						testChild++;
					}
				})
				for(var j=0;j<expenditure_table;j++){
					if(LocationChild==$("#HiddenChildId"+(j+1)).text() && LocationSite==$("#siteSiteName"+(j+1)).text()){
						$("#locationWiseFromDate"+(i+1)).val($("#siteFromDate"+(j+1)).text());	
						$("#locationWiseToDate"+(i+1)).val($("#siteToDate"+(j+1)).text());	
						$("#locationwisetime"+(i+1)).val($("#siteTime"+(j+1)).text());	
						if($("#siteLocationName"+(j+1)).text()!=""){
							$("#LocationLocationId"+(i+1)).html("<option value='"+$("#expendatureLocationName"+(j+1)).val()+"'>"+$("#siteLocationName"+(j+1)).text()+"</option>");	
							$("#LocationLocationId"+(i+1)).val($("#expendatureLocationName"+(j+1)).val());	
						}
					}
				}
				if(testChild==1){
					for(var j=0;j<expenditure_table;j++){
						if(LocationChild==$("#HiddenChildId"+(j+1)).text()){
							$("#locationWiseFromDate"+(i+1)).val($("#siteFromDate"+(j+1)).text());	
							$("#locationWiseToDate"+(i+1)).val($("#siteToDate"+(j+1)).text());	
							$("#locationwisetime"+(i+1)).val($("#siteTime"+(j+1)).text());	
							if($("#siteLocationName"+(j+1)).text()!=""){
								$("#LocationLocationId"+(i+1)).html("<option value='"+$("#expendatureLocationName"+(j+1)).val()+"'>"+$("#siteLocationName"+(j+1)).text()+"</option>");
								$("#LocationLocationId"+(i+1)).val($("#expendatureLocationName"+(j+1)).val());	
							}
							break;
						}
					}
				}
			}
			
		}
	}); 

}
//location wise quantity And TotalAmount change
function locationquantityAndTotalAmount(id){
	var splitAmount=parseFloat($("#locationsplitamount"+id).val()==''?0:$("#locationsplitamount"+id).val())/parseFloat($("#locationquantity"+id).val());
	if($("#locationquantity"+id).val()=="" || $("#locationquantity"+id).val()=="0"){ splitAmount=0; }
	$("#locationtotalAmount"+id).val(splitAmount.toFixed(2));
	locationWiseGrandTotal();
}


//location wise grand total
function locationWiseGrandTotal(){
	var grandTotal1=0;
	$(".locationWiseTotalCls").each(function(){
	if($(this).val() == ""){
		grandTotal1 += 0;
	}else{
		grandTotal1  +=  parseFloat($(this).val());
	}
	});
	$("#grandTotal").html(grandTotal1.toFixed(2)); 
}
//load brand wise table
function callingPerBrandwiseCalculation(id, expendaturetype){
	var month_year=$("#invoiceDateHiddenId").val();
	 $.ajax({
		
		 	url:"getAvailableAreaForSale.spring",
			type:"POST",
			Contenttype:" application/json",
			data : {
				 "location": ""+id,
				"expendatureType": ""+expendaturetype,
				"month_year" : month_year
			},
			success:function(response){
				var global_response;
				var addbrandwise ='';
				var newId = 0;
				var num = 0;
				var tempchildData = childproducttemp;
				var tempchildquantity = childprdctquantity;
				var tempchildproductTotalAmount = childproductTotalAmount;
				var tempresp = eval('('+response+')');
				$("#tempshowhide").show();
				if(!tempresp.xml){
					$("#brandwiseAdd").html("<tr><td style='text-align:center;border:1px solid #000;' colspan='9'>No Data</td></tr>");
					$("#brandwiseGrandTotal").hide();
					return false;
				}
				global_response = tempresp.xml.site;
				var childproductlength = $(".childprdctcls").length;
				var AmountForProductForSite = 0;
				var grandTotal =0;
				 var brandWiseSerialno  = 0;
				 for(var i=0;i<tempchildData.length;i++){
					 var expenditureTotal=0;
						var expenditure_tableLength=$("#expenditure_table1").find("tbody").find("tr").length;
						for(var k=0;k<expenditure_tableLength;k++){
					        if(childproducttemp[i].split("$")[0]==$("#HiddenChildId"+(k+1)).text().split("$")[0]){
					        	expenditureTotal+=parseFloat($("#siteamount"+(k+1)).text())
							}
						}
					for(var j=0; j<global_response.length;j++){
						newId = num+1;
						var AmountForProductForSite =parseFloat(expenditureTotal *( tempresp.xml.site[j].availableArea/tempresp.xml.site[j].totalArea)).toFixed(2);
						var basicAmount  =  AmountForProductForSite/tempchildquantity[i];
						grandTotal += AmountForProductForSite;
						addbrandwise += '<tr id="brandingtable'+newId+'" class="brandingtablecls"><td ><input type="text" class="form-control childprdctsLocation" name="childdata'+newId+'" id="childdata'+newId+'" data-toggle="tooltip"  title="'+childproducttemp[i].split('$')[1]+'"  value="'+childproducttemp[i].split('$')[1]+'" readonly><input type="hidden" class="BrandingChildCls" name="BrandingChildId'+newId+'" id="BrandingChildId'+newId+'" value="'+childproducttemp[i]+'"></td><td><select class="form-control" id="brandingWiseLocationName'+newId+'" name="brandingWiseLocationName'+newId+'" onchange="brandWiseLocationChange('+newId+')"></select></td><td><input type="text" class="form-control ExpiryDate"  id="brandWiseFromDate'+newId+'" name="brandWiseFromDate'+newId+'" placeholder="Select From Date" autocomplete="off"/></td><td><input type="text" class="form-control ExpiryDate" id="brandwiseToDate'+newId+'"  name="brandwiseToDate'+newId+'" placeholder="Select To Date" autocomplete="off"/></td><td><input class="form-control"  type="text"  name="brandingwisetime'+newId+'" id="brandingwisetime'+newId+'" placeholder="Select Time" autocomplete="off"></td><td><input onkeyup="brandingquantityAndTotalAmount('+newId+')" id="brandingquantity'+newId+'" name="brandWiseQuantity'+newId+'" type="text" class="form-control" value="'+tempchildquantity[i]+'" autocomplete="off" readonly></td><td><input type="text" class="form-control" name="brandWiseProductRate'+newId+'" id="brandWiseProductRate'+newId+'" value="'+basicAmount.toFixed(2)+'" onkeyup="brandingWiseAmountChange('+newId+')" autocomplete="off" readonly></td><td><input value="'+AmountForProductForSite+'"  id="brandingTotalamount'+newId+'"   name="brandwiseTotalAmount'+newId+'"  onkeyup="brandingquantityAndTotalAmount('+newId+')"   type="text" class="form-control brandingWiseTotalCls" autocomplete="off" readonly></td><td ><input type="text" class="form-control sitenamelocationwise" name="brandwiseSiteName'+newId+'" id="brandnamelocationwise'+newId+'" data-toggle="tooltip"  title="'+tempresp.xml.site[j].siteName+'"  value="'+tempresp.xml.site[j].siteName+'" readonly></td><input type="hidden" class="hoardingId'+newId+'" id="hoardingId'+newId+'" value="'+temphoadingId[i]+'"name="brandwiseHoardingId'+newId+'"><input type="hidden" class="childproductid'+newId+'" id="childproductidd'+newId+'" value="'+childproducttemp[i].split('$')[0]+'"name="brandwiseChildproductid'+newId+'"><input type="hidden" class="siteIdd'+newId+'" id="siteIdd'+newId+'" value="'+tempresp.xml.site[j].siteId+'"name="brandwiseSiteId'+newId+'"></tr>';
						num++;
					}
					getLocationForLocandBranding(tempchildData[i], num);
				}				 
				$("#brandwiseAdd").html(addbrandwise);
				$("#grandTotalbrand").html(grandTotal);
				$('[data-toggle="tooltip"]').tooltip(); 
				for(var j=0; j<num;j++){
					$('#brandWiseFromDate'+(j+1)).datepicker({
						 dateFormat: 'dd-M-y',
						/* minDate:0,*/
					     changeMonth: true,
				         changeYear: true
					});
						  
					$("#brandwiseToDate"+(j+1)).datepicker({ 
						  dateFormat: 'dd-M-y',
						 /* minDate:0,*/
						  changeMonth: true,
					      changeYear: true
					 });
					$("#brandingwisetime"+(j+1)).timepicker({
						 timeFormat: 'h:mm p',
						    interval: 10,
						    minTime: '00.00AM',
						    maxTime: '11.59PM',
						    startTime: '00:00',
						    dynamic: false,
						    dropdown: true,
						    scrollbar: true
					});
				}
				var brandingTable=$("#brandingTable").find("tbody").find("tr").length;
				for(var i=0;i<brandingTable-1;i++){
					var expenditure_table=$("#expenditure_table1").find("tbody").find("tr").length;
					var BrandingChild=$("#BrandingChildId"+(i+1)).val().split("$")[0];
					var BrandingSite=$("#brandnamelocationwise"+(i+1)).val();
					var HiddenBrandingChild=$("#BrandingChildId"+(i+1)).val().split("$")[0];
					var testChild=0;
					//checking one child product have how may child products are there
					$(".childprdctcls").each(function(){
						var currentVal=$(this).text().split("$")[0];
						if(HiddenBrandingChild==currentVal){
							testChild++;
						}
					})
					for(var j=0;j<expenditure_table;j++){
						if(BrandingChild==$("#HiddenChildId"+(j+1)).text().split("$")[0] && BrandingSite==$("#siteSiteName"+(j+1)).text()){
							$("#brandWiseFromDate"+(i+1)).val($("#siteFromDate"+(j+1)).text());	
							$("#brandwiseToDate"+(i+1)).val($("#siteToDate"+(j+1)).text());	
							$("#brandingwisetime"+(i+1)).val($("#siteTime"+(j+1)).text());
							//if expendature location empty
							if($("#siteLocationName"+(j+1)).text()!=""){
								$("#brandingWiseLocationName"+(i+1)).html("<option value='"+$("#expendatureLocationName"+(j+1)).val()+"'>"+$("#siteLocationName"+(j+1)).text()+"</option>");
								$("#brandingWiseLocationName"+(i+1)).val($("#expendatureLocationName"+(j+1)).val());
							}
						}
						
					}
					//if child name have 1 row in expendature
					if(testChild==1){
						for(var j=0;j<expenditure_table;j++){
							if(BrandingChild==$("#HiddenChildId"+(j+1)).text().split("$")[0]	){
								$("#brandWiseFromDate"+(i+1)).val($("#siteFromDate"+(j+1)).text());	
								$("#brandwiseToDate"+(i+1)).val($("#siteToDate"+(j+1)).text());	
								$("#brandingwisetime"+(i+1)).val($("#siteTime"+(j+1)).text());	
								if($("#siteLocationName"+(j+1)).text()!=""){
									$("#brandingWiseLocationName"+(i+1)).html("<option value='"+$("#expendatureLocationName"+(j+1)).val()+"'>"+$("#siteLocationName"+(j+1)).text()+"</option>");
									$("#brandingWiseLocationName"+(i+1)).val($("#expendatureLocationName"+(j+1)).val());
								}
								break;
							}
						}
					}
				}
				brandingWiseGrandTotal();
			}
	 });
}
//branding table quantity and total amount chaange method
function brandingquantityAndTotalAmount(id){
	var splitAmount=parseFloat($("#brandingTotalamount"+id).val()==''?0:$("#brandingTotalamount"+id).val())/parseFloat($("#brandingquantity"+id).val());
	if($("#brandingquantity"+id).val()=="" || $("#brandingquantity"+id).val()=="0"){
		splitAmount=0;
	}
	$("#brandWiseProductRate"+id).val(splitAmount.toFixed(2));
	brandingWiseGrandTotal();
}

//location wise grand total
function brandingWiseGrandTotal(){
	var grandTotal1=0;
	$(".brandingWiseTotalCls").each(function(){
	if($(this).val() == ""){
		grandTotal1 += 0;
	}else{
		grandTotal1  +=  parseFloat($(this).val());		
	}
	});
	$("#grandTotalbrand").html(grandTotal1.toFixed(2)); 
}

//single site Quantity Change
function siteWiseQtyAndTotalAmount(id, val){ 
	    var tempamount = 0;
	    var siteQuantity=$("#sitequanty"+(id)).val();
	    if(siteQuantity=='' || siteQuantity==0){
	    	tempamount = 0;
	    }else{
	    	 tempamount = parseFloat($("#totalamountsite"+id).val()==''?0:$("#totalamountsite"+id).val() / $("#sitequanty"+id).val());
	    }
		$("#splitamount"+id).val(tempamount.toFixed(2));
		siteWiseGrandTotal();
}
//single site grand total
function siteWiseGrandTotal(){
	var grandTotal1=0;
	$(".totalamountclass").each(function(){ if($(this).val() == ""){  	grandTotal1 += 0; }else{ grandTotal1  +=  parseFloat($(this).val()); }	});
	$("#grandTotalsite").html(grandTotal1); 
}

var newId=0;
var addRow;
//append row for multi site table add button
function appendModalRow(id){
	 var newId=parseInt($(".MultipleSite:last").attr("id").split("multisiteAdd")[1])+1;
	 addRow = '<tr id="multisiteAdd'+newId+'" class="MultipleSite"><td>'+newId+'</td><td><select name="exapendChildproduct'+newId+'" id="exapendChildproduct'+newId+'" class="form-control childprdctclsmulti"  onchange="expendituremultiChildProductChange('+newId+', this)"></select></td><td><select class="form-control multiSiteAreaname" name="multiareaname'+newId+'" id="multiareaname'+newId+'" onchange="multiSiteSiteNameChange('+newId+')"></select></td><td><select name="multiSieSiteName'+newId+'" id="sitenameid'+newId+'" class="form-control multsitenamecls" onchange="multiSiteSiteNameChange('+newId+')"></select></td><td><input onkeyup="multisiteQtyAndTotalChange('+newId+',this)" name="multiSiteQuanty'+newId+'" id="multisitequanty'+newId+'" type="text"class="form-control" autocomplete="off"/></td><td><input type="text" class="form-control ExpiryDate" name="multiSiteFromDate'+newId+'" id="multiwisefromDate'+newId+'" placeholder="Select From Date" autocomplete="off"/></td><td><input type="text" class="form-control ExpiryDate" name="multiSiteTodate'+newId+'" id="multsideTodate'+newId+'" placeholder="Select To Date" autocomplete="off"/></td><td><input class="form-control"  type="text"  name="multisitewisetime'+newId+'" id="multisitewisetime'+newId+'" placeholder="Select Time" autocomplete="off"></td><td><input id="multisplitamount'+newId+'" name="multiSiteRate'+newId+'" type="text" class="form-control" onkeyup="multisiteWiseAmountChange('+newId+')" autocomplete="off"/></td><td><input name="multiSiteTotalAmount'+newId+'" id="totalamountmultsite'+newId+'" type="text" class="form-control totalamountmultisiteclass" onkeyup="multisiteQtyAndTotalChange('+newId+',this)" autocomplete="off"/></td><td><button type = "button" id="addBtn'+newId+'" class="btnaction btnpadd" onclick="appendModalRow('+newId+')"><i class="fa fa-plus"></i></button><button type="button" id="deleteBtn'+newId+'" onclick="deleteRow('+newId+')" class="btnaction"><i class="fa fa-trash"></i></button></td><input type="hidden" class="hoardingId'+newId+'" id="hoardingId'+newId+'" name="hoardingId'+newId+'"><input type="hidden" class="childproductid'+newId+'" id="childproductidd'+newId+'" name="childproductid'+newId+'"><input type="hidden" class="siteIdd'+newId+'" id="siteIdd'+newId+'" name="siteId'+newId+'"></tr>';
	
	$("#multisiteAdd").append(addRow);
	
	$('#multiwisefromDate'+newId).datepicker({
		 dateFormat: 'dd-M-y',
		/* minDate:0,*/
	     changeMonth: true,
         changeYear: true
	});
		  
	$("#multsideTodate"+newId).datepicker({ 
		  dateFormat: 'dd-M-y',
		 /* minDate:0,*/
		  changeMonth: true,
	      changeYear: true
	 });
	$("#multisitewisetime"+newId).timepicker({
		 timeFormat: 'h:mm p',
		    interval: 10,
		    minTime: '00.00AM',
		    maxTime: '11.59PM',
		    startTime: '00:00',
		    dynamic: false,
		    dropdown: true,
		    scrollbar: true
	});
		
	var addsiteName = "<option>--Please Select Site--</option>";
	 if(siteNamesFromAjax.xml.site.SITEID){
		  addsiteName+='<option value="'+siteNamesFromAjax.xml.site.SITEID+"$"+siteNamesFromAjax.xml.site.SITENAME+'">'+siteNamesFromAjax.xml.site.SITENAME+'</option>';
	  }else{
		  for(var i=0; i<siteNamesFromAjax.xml.site.length;i++){
			 addsiteName+='<option value="'+siteNamesFromAjax.xml.site[i].SITEID+"$"+siteNamesFromAjax.xml.site[i].SITENAME+'">'+siteNamesFromAjax.xml.site[i].SITENAME+'</option>';
		  } 
	  }
	 $("#sitenameid"+newId).html(addsiteName);
	var selectoptionmulti = "<option>--select--</option>";
	for(var j=0;j<childproducttemp.length;j++){		
		selectoptionmulti+='<option value="'+childproducttemp[j]+'">'+childproducttemp[j].split("$")[1]+'</option>';	
	}
	$("#exapendChildproduct"+newId).html(selectoptionmulti);
}
	
//get the location for location wise  && Branding wise
function getLocationForLocandBranding(childname, length){
	$.ajax({
	 	url:"loadAndSetAreaData.spring?childProductId="+childname.split('$')[0],
		type:"GET",
		success:function(response){
				var result = eval('(' + response + ')');
				var addLocation = "<option value='0'>--Select Location--</option>";
				 if(result.xml){
					 for(var i=0; i<result.xml.Area.length;i++){						 
						 addLocation+='<option value="'+result.xml.Area[i].AreaId+"$"+result.xml.Area[i].AreaName+'">'+result.xml.Area[i].AreaName+'</option>';
					}					
					 var expendatureName=$("#expendatureForId").val();
					 if(expendatureName=="LocationWise"){
						 $(".LocationChildcls").each(function(){
							if($(this).val().split('$')[0]==childname.split('$')[0]){
								var id=$(this).attr("id").split("LocationChildId")[1];
								/*if($("#LocationLocationId"+id).val()==0 || $("#LocationLocationId"+id).val()==null){*/
									$("#LocationLocationId"+id).append(addLocation);
									//break;
								/*}*/
							}
						})
					 }
					 else{
						 $(".BrandingChildCls").each(function(){
							if($(this).val().split('$')[0]==childname.split('$')[0]){
								var id=$(this).attr("id").split("BrandingChildId")[1];		
								/*if($("#brandingWiseLocationName"+id).val()==0 || $("#brandingWiseLocationName"+id).val()==null){*/
									$("#brandingWiseLocationName"+id).append(addLocation);
								/*}*/
							}
						})
					 }
				 } //1 st is empty
				 else{
					 var addLocation = "<option>--Select Location--</option>";
					 var expendatureName=$("#expendatureForId").val();
					 if(expendatureName=="LocationWise"){
						 $(".LocationChildcls").each(function(){
							if($(this).val().split('$')[0]==childname.split('$')[0]){
								var id=$(this).attr("id").split("LocationChildId")[1];
								/*if($("#LocationLocationId"+id).val()==0 || $("#LocationLocationId"+id).val()==null){*/
									$("#LocationLocationId"+id).append(addLocation);
							/*	}*/
							}
						})
					 }
					 else{
						 $(".BrandingChildCls").each(function(){
							if($(this).val().split('$')[0]==childname.split('$')[0]){
								var id=$(this).attr("id").split("BrandingChildId")[1];	
								/*if($("#brandingWiseLocationName"+id).val()==0 || $("#brandingWiseLocationName"+id).val()==null){*/
									$("#brandingWiseLocationName"+id).append(addLocation);
								/*}*/
							}
						})
					 }
				 }
		}
		}); 
}			
//get site names for multi site wise table	
function getSiteNamesForMulti(templength){
	var addsiteName = "<option>--Please Select Site--</option>";
	$.ajax({
		  url : "siteNameDetails.spring",
		  type : "get",
		  contentType : "application/json",
		  success : function(data) {
			  siteNamesFromAjax = eval('(' + data + ')');
			  if(siteNamesFromAjax.xml.site.SITEID){
				  addsiteName+='<option value="'+siteNamesFromAjax.xml.site.SITEID+"$"+siteNamesFromAjax.xml.site.SITENAME+'">'+siteNamesFromAjax.xml.site.SITENAME+'</option>';
			  }
			  else{
				  for(var i=0; i<siteNamesFromAjax.xml.site.length;i++){
					 addsiteName+='<option value="'+siteNamesFromAjax.xml.site[i].SITEID+"$"+siteNamesFromAjax.xml.site[i].SITENAME+'">'+siteNamesFromAjax.xml.site[i].SITENAME+'</option>';
				  } 
			  }
			  for(var i=0;i<templength;i++){
					$("#sitenameid"+(i+1)).html(addsiteName);
				}
		  }
	});
}
//in single site site name change 
function siteWiseSitenameChange(){
	//loading site wise table
	loadsiteWiseTable();
	
	var siteName=$("#siteWiseSitename").val().split("$")[1];
	for(var j=0;j<childproducttemp.length;j++){	
		$("#sitename"+(j+1)).val(siteName);
		$("#sitename"+(j+1)).attr("title", siteName);
		$("#singleSiteSiteId"+(j+1)).val($("#siteWiseSitename").val().split("$")[0]);
	}
	$("#single_site").show();
}
 
//for deleteling a row in multi site wise table
function deleteRow(currentRow) {
	var CanIDelete=window.confirm("Do you want to delete row?");
    if(CanIDelete==false){
    	return false;
    }
	var rowscount=$('.MultipleSite').length;
	if(rowscount==1){
		alert("this row con't be deleted.");
		return false;
	}
	//removing row
   $("#multisiteAdd"+currentRow).remove();
   //calculate grand total
   multiSiteGrandTotal();
}

//to get Quantity, Dates and Time
function multiSiteSiteNameChange(id){
	
	var multiSiteChild=$("#exapendChildproduct"+id).val();
	var multiSitelocation=$("#multiareaname"+id).val();
	var multiSiteSite=$("#sitenameid"+id).val();
	if(multiSiteChild==""  || multiSiteSite==""){ //|| multiSitelocation=="" 
		return false;
	}
	var expendatureTableLength=$(".view_expendature").length;
		for(var j=0;j<expendatureTableLength;j++){
			if(multiSiteChild.split("$")[0]==$("#HiddenChildId"+(j+1)).text().split("$")[0] && multiSiteSite.split("$")[0]==$("#expendatureSiteName"+(j+1)).val().split("$")[0]  && multiSitelocation.split("$")[0]==$("#expendatureLocationName"+(j+1)).val().split("$")[0]){  //
				$("#multiwisefromDate"+id).val($("#siteFromDate"+(j+1)).text());
				$("#multsideTodate"+id).val($("#siteToDate"+(j+1)).text());
				$("#multisitewisetime"+id).val($("#siteTime"+(j+1)).text());
			/*	$("#multisitequanty"+id).val($("#sitequantity"+(j+1)).text());
				var splitAmount=parseFloat($("#totalamountmultsite"+id).val())/parseFloat($("#multisitequanty"+id).val());
				if($("#multisitequanty"+id).val()=="" || $("#multisitequanty"+id).val()=="0"){ 	splitAmount=0;	}
				$("#multisplitamount"+id).val(splitAmount.toFixed(2));	*/
				break;
			}
			else{
				$("#multiwisefromDate"+id).val("");
				$("#multsideTodate"+id).val("");
				$("#multisitewisetime"+id).val("");
				//$("#multisplitamount"+id).val("0.00");
			/*	for(var k=0;k<expendatureTableLength;k++){
					if(multiSiteChild.split("$")[0]==$("#HiddenChildId"+(k+1)).text().split("$")[0]){
						$("#multisitequanty"+id).val($("#sitequantity"+(k+1)).text());
						var splitAmount=parseFloat($("#totalamountmultsite"+id).val())/parseFloat($("#multisitequanty"+id).val());
						if($("#multisitequanty"+id).val()=="" || $("#multisitequanty"+id).val()=="0"){
							splitAmount=0;
						}
						$("#multisplitamount"+id).val(splitAmount.toFixed(2));
				}
			}*/
		}
	}
	

}

//validate site wise data
function valSiteWiseData(){

	var sitewiseTableLength=$("#siteWiseSite").find("tbody").find("tr").length;
	for(var i=0;i<sitewiseTableLength-1;i++){
		var expenditureChildTotalAmount=0;
		var siteWiseChild=$("#hiddensingleSiteChild"+(i+1)).val().split("$")[0];
		var siteWiseSingleTotal=parseFloat($("#totalamountsite"+(i+1)).val());
		var expenditureTableLength=$("#expenditure_table1").find("tbody").find("tr").length;
		var tempTotal=0;
		for(var j=0;j<expenditureTableLength;j++){
			if(siteWiseChild==$("#HiddenChildId"+(j+1)).text().split("$")[0]){
				expenditureChildTotalAmount+=parseFloat($("#siteamount"+(j+1)).text());
			}
		}
		var tempVal=parseInt(siteWiseSingleTotal)-parseInt(expenditureChildTotalAmount);
		if(tempVal!=-1 && tempVal!=1 && tempVal!=0){
			alert("Please give a proper amount.");
			//$("#totalamountsite"+(i+1)).focus();
			return false;
		}
	}
	
}

//validate multi site wise data
function valMultiSiteWiseData(){
	var MultiSiteTable=$("#expenditureTable").find("tbody").find("tr").length;
	for(var i=0;i<childproducttemp.length;i++){	
		var ChildData=childproducttemp[0].split("$")[0];
		var MultiSiteTableTotal=0;
		$(".MultipleSite").each(function(){
			var currentId=$(this).attr("id").split("multisiteAdd")[1];
			if(ChildData==$("#exapendChildproduct"+currentId).val().split("$")[0]){
				MultiSiteTableTotal+=parseFloat($("#totalamountmultsite"+currentId).val());
			}
		})
		var expendatureStatus=expenDatureTotal(ChildData, MultiSiteTableTotal);
		if(expendatureStatus==false){
			return false;
		}
	}
}

//validate Branding wise data
function valBrandingWiseData(){
	var brandingTable=$("#brandingTable").find("tbody").find("tr").length;
	for(var i=0;i<childproducttemp.length;i++){	
		var ChildData=childproducttemp[0].split("$")[0];
		var BrandingTotal=0;
		for(var j=0;j<brandingTable-1;j++){
			if(ChildData==$("#BrandingChildId"+(j+1)).val().split("$")[0]){
				BrandingTotal+=parseFloat($("#brandingTotalamount"+(j+1)).val());
			}
		}
		var expendatureStatus=expenDatureTotal(ChildData, BrandingTotal);
		if(expendatureStatus==false){
			return false;
		}
	}
}

//validate location  wise data
function vallocationWiseData(){
	var locationTable=$("#tempshowhide").find("tbody").find("tr").length;
	for(var i=0;i<childproducttemp.length;i++){	
		var ChildData=childproducttemp[0].split("$")[0];
		var locationTotal=0;
		for(var j=0;j<locationTable-1;j++){
			if(ChildData==$("#LocationChildId"+(j+1)).val().split("$")[0]){
				locationTotal+=parseFloat($("#locationsplitamount"+(j+1)).val());
			}
		}
		var expendatureStatus=expenDatureTotal(ChildData, locationTotal);
		if(expendatureStatus==false){
			return false;
		}
	}
}


//calculating  expendature table child wise total Amount
function expenDatureTotal(ChildData, totalAmount){
	var expenditureTotal=0;
	var expenditure_tableLength=$("#expenditure_table1").find("tbody").find("tr").length;
	var tempVal=0;
	for(var k=0;k<expenditure_tableLength;k++){
        if(ChildData==$("#HiddenChildId"+(k+1)).text().split("$")[0]){
        	expenditureTotal+=parseFloat($("#siteamount"+(k+1)).text())
		}
	}
	var tempVal=parseInt(totalAmount)-parseInt(expenditureTotal);
	/*if(parseInt(totalAmount)!=parseInt(expenditureTotal)){*/
		if(tempVal!=-1 && tempVal!=1 && tempVal!=0){
		alert("Please give a proper amount.");
		return false;
	}
}
//location location change
function siteWiseLocationChange(id){
	var childName=$("#hiddensingleSiteChild"+id).val().split("$")[0];
	var LocationName=$("#singleSiteAreaName"+id).val().split("$")[0];
	var SiteName=$("#singleSiteSiteId"+id).val();
	var expenditure_tableLength=$("#expenditure_table1").find("tbody").find("tr").length;
	for(var k=0;k<expenditure_tableLength;k++){
		if(childName==$("#HiddenChildId"+(k+1)).text().split("$")[0] && LocationName==$("#expendatureLocationName"+(k+1)).val().split("$")[0] && SiteName==$("#expendatureSiteName"+(k+1)).val().split("$")[0]){
			$("#SingleSiteFromDate"+id).val($("#siteFromDate"+(k+1)).text());
			$("#sitewisetoDate"+id).val($("#siteToDate"+(k+1)).text());
			$("#sitewisetime"+id).val($("#siteTime"+(k+1)).text());
		}
	}	
}


//location location change
function locationWiseLocationChange(id){
	var childName=$("#LocationChildId"+id).val().split("$")[0];
	var LocationName=$("#LocationLocationId"+id).val().split("$")[0];
	var SiteName=$("#locationWiseSiteId"+id).val();
	var expenditure_tableLength=$("#expenditure_table1").find("tbody").find("tr").length;
	for(var k=0;k<expenditure_tableLength;k++){
		if(childName==$("#HiddenChildId"+(k+1)).text().split("$")[0] && LocationName==$("#expendatureLocationName"+(k+1)).val().split("$")[0] && SiteName==$("#expendatureSiteName"+(k+1)).val().split("$")[0]){
			$("#locationWiseFromDate"+id).val($("#siteFromDate"+(k+1)).text());
			$("#locationWiseToDate"+id).val($("#siteToDate"+(k+1)).text());
			$("#locationwisetime"+id).val($("#siteTime"+(k+1)).text());
		}
	}	
}
//branding location change
function brandWiseLocationChange(id){
	var childName=$("#BrandingChildId"+id).val().split("$")[0];
	var LocationName=$("#brandingWiseLocationName"+id).val().split("$")[0];
	var SiteName=$("#siteIdd"+id).val();
	var expenditure_tableLength=$("#expenditure_table1").find("tbody").find("tr").length;
	for(var k=0;k<expenditure_tableLength;k++){
		if(childName==$("#HiddenChildId"+(k+1)).text().split("$")[0] && LocationName==$("#expendatureLocationName"+(k+1)).val().split("$")[0] && SiteName==$("#expendatureSiteName"+(k+1)).val().split("$")[0]){
			$("#brandWiseFromDate"+id).val($("#siteFromDate"+(k+1)).text());
			$("#brandwiseToDate"+id).val($("#siteToDate"+(k+1)).text());
			$("#brandingwisetime"+id).val($("#siteTime"+(k+1)).text());
		}
	}	
}

//site wise amount change
function siteWiseAmountChange(id){
	var amount=parseFloat($("#splitamount"+id).val()==''?0:$("#splitamount"+id).val());
	var quantity=parseFloat($("#sitequanty"+id).val()==''?0:$("#sitequanty"+id).val());
	var totalAmount=amount*quantity;
	$("#totalamountsite"+id).val(totalAmount.toFixed(2));
	siteWiseGrandTotal();
}
//multi site wise amount change
function multisiteWiseAmountChange(id){
	var amount=parseFloat($("#multisplitamount"+id).val()==''?0:$("#multisplitamount"+id).val());
	var quantity=parseFloat($("#multisitequanty"+id).val()==''?0:$("#multisitequanty"+id).val());	
	var totalAmount=amount*quantity;
	$("#totalamountmultsite"+id).val(totalAmount.toFixed(2));
	multiSiteGrandTotal();
}
//location site wise amount change
function locationWiseAmountChange(id){
	var amount=parseFloat($("#locationtotalAmount"+id).val()==''?0:$("#locationtotalAmount"+id).val());
	var quantity=parseFloat($("#locationquantity"+id).val()==''?0:$("#locationquantity"+id).val());	
	var totalAmount=amount*quantity;
	$("#locationsplitamount"+id).val(totalAmount.toFixed(2));
	locationWiseGrandTotal();
}

//location site wise amount change
function brandingWiseAmountChange(id){
	var amount=parseFloat($("#brandWiseProductRate"+id).val()==''?0:$("#brandWiseProductRate"+id).val());
	var quantity=parseFloat($("#brandingquantity"+id).val()==''?0:$("#brandingquantity"+id).val());	
	var totalAmount=amount*quantity;
	$("#brandingTotalamount"+id).val(totalAmount.toFixed(2));
	brandingWiseGrandTotal();
}

//add function 
 function siteAddFunction(){
  		var tableRowsCount;
  		var typeOfSite=$("#expendatureForId").val();
  		if(typeOfSite=="SiteWise"){
  			tableRowsCount=[];
  			var error=true;
  			$(".siteclass").each(function(){
  				var currentId=$(this).attr("id").split("siteAdd")[1];
  				tableRowsCount.push(currentId);	
  				if($("#sitename"+currentId).val()==""){
  					return error=false;
  				}
  			});
  			if(error==false){
  				alert("Please select site.");
  				$("#siteWiseSitename").focus();
  				return false;
  			}
  			var siteStatus=valSiteWiseData();
  			if(siteStatus==false){
  				return false;
  			}
  		}
  		if(typeOfSite=="MultipleSite"){
  			tableRowsCount=[];
  			var error=true;
  			var childEmpty=true;
  			$(".MultipleSite").each(function(){
  				var currentId=$(this).attr("id").split("multisiteAdd")[1];
  				tableRowsCount.push(currentId);	
  				if($("#exapendChildproduct"+currentId).val()=="--Select Child Product--"){
  					$("#exapendChildproduct"+currentId).focus();
  					return childEmpty=false;
  				}
  				if($("#sitenameid"+currentId).val()=="--Please Select Site--"){
  					$("#sitenameid"+currentId).focus();
  					return error=false;
  				}
  			});
  			if(childEmpty==false){
  				alert("Please select Child Product.");
  				return false;
  			}
  			if(error==false){
  				alert("Please select site.");
  				return false;
  			}
  			var multiSiteStatus=valMultiSiteWiseData();
  			if(multiSiteStatus==false){
  				return false;
  			}
  		}
  		if(typeOfSite=="LocationWise"){
  			tableRowsCount=[];
  			$(".locationWiseclass").each(function(){
  				var currentId=$(this).attr("id").split("locationAdd")[1];
  				tableRowsCount.push(currentId);		
  			});
  			
  			var locationStatus=vallocationWiseData();
  			if(locationStatus==false){
  				return false;
  			}
  		}
  		if(typeOfSite=="BrandingWise"){
  			tableRowsCount=[];
  			$(".brandingtablecls").each(function(){
  				var currentId=$(this).attr("id").split("brandingtable")[1];
  				tableRowsCount.push(currentId);		
  			});
  			var brandingStatus=valBrandingWiseData();
  			if(brandingStatus==false){
  				return false;
  			}  			
  		}
  		var CanIUpdate=window.confirm("Do you want to update?");
  		if(CanIUpdate==false){
  			return false;
  		}
  		$("#expendatureTableCount").val(tableRowsCount);
  		
  		document.getElementById("calculateexpendature").action = "addupdateExpenditure.spring";
  		document.getElementById("calculateexpendature").method = "POST";
  		document.getElementById("calculateexpendature").submit();
 	} 
