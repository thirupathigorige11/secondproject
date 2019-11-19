//for selecting multiple option to block
$(document).ready(function () {
	$("#BlockID").select2();
});
//to open calender when you click on calender icon in abstract page
function openCalender(){
	$("#billDate").focus();
}


var serialno = 2;
var serialNumber=2;
var workDescNames;
var minorHeadNames;
if (typeof(Storage) !== "undefined") {
    // Store  Retrieve
  data=sessionStorage.getItem("NMRDATA");
  lengthOfRows=sessionStorage.getItem("lengthOfRows");
  workDescNames=sessionStorage.getItem("workDescNames");
  minorHeadNames=sessionStorage.getItem("minorHeadNames");

} else {
   alert("Sorry, your browser does not support Web Storage...");
};

function appendRow(id) {	
	debugger;
	var data;
	var lengthOfRows;
	
	if (typeof(Storage) !== "undefined") {
	    // Store  Retrieve
	  data=sessionStorage.getItem("NMRDATA");
	  lengthOfRows=sessionStorage.getItem("lengthOfRows");
	  workDescNames=sessionStorage.getItem("workDescNames");
	  minorHeadNames=sessionStorage.getItem("minorHeadNames");
	 
	} else {
	   alert("Sorry, your browser does not support Web Storage...");
	};
	
	var tempminorHeadNames=minorHeadNames.split(",");
	var tempworkDescNames=workDescNames.split(",");
	var apendrow = '';
	var tempRowData = '';
	
	var Date=$("#Date"+id).val();
	var Description=$("#Description"+id).val();
	var From=$("#From"+id).val();
	var To=$("#To"+id).val();
	var Noofhours=$("#Noofhours"+id).val();
	
	if(Date==''){
		alert("Please select Date.");
		$("#Description"+id).val('');
		$("#Date"+id).focus();
		return false;
	}

	if(Description==''){
		alert("Please select Description.");
		$("#Description"+id).focus();
		return false;
	}

	if(From==''){
		alert("Please enter  From.");
		$("#From"+id).focus();
		return false;
	}

	if(To==''){
		alert("Please enter To.");
		$("#To"+id).focus();
		return false;
	}

	if(Noofhours==''){
		alert("Please enter No of hours.");
		$("#Noofhours"+id).focus();
		return false;
	}
	//validatind description textbox
	var descriptionVal=validatedescriptionValues();
	if(descriptionVal==false){
		return false;
	}
	//hiding add button
	$("#AddNMRBtn"+id).hide();
	//display hidden delete button		
	var tableRowsLength=$('.tablerowcls').length;
	if(tableRowsLength==1){
		var getLastRowId=$('.tablerowcls:last').attr('id');	
		var res = getLastRowId.split("tablerow")[1];
		$("#DeleteNMRBtn"+res).show();
	}
	
    apendrow += "<tr id='tablerow"+serialNumber+"'  class='tablerowcls' style='width:100%;'>";
	apendrow += "<td style='text-align:center;padding:5px;'>"+serialNumber+"</td>";
	apendrow += "<td style='text-align:center;padding:5px;'><div class=''><input type='text' class='form-control Date' id='Date"+ serialNumber+ "' name='WorkDate"+ serialNumber+ "' onchange='NMRDescriptionCompare(\"date\","+serialNumber+")' onkeydown='return false' placeholder='dd-mm-yy' style='width:67%;margin-left:2%;float:left;'><label class='input-group-addon btn datepicker-paymentreq-fromdate' id='datepickerIcon' onclick='openCalenderInNMRDetails("+ serialNumber+ ")' for='fromDate'><span class='fa fa-calendar'></span></label></div><input type='hidden' id='Datehidden"+ serialNumber+ "'> </td> ";// onchange='NMRDateCompare("+serialNumber+")'
	apendrow += "<td style='text-align:center;padding:5px;'><input type='hidden' id='Descriptionhidden"+ serialNumber+"'> <select class='form-control Description' id='Description"+ serialNumber+ "'  name='Description"+ serialNumber+ "' onchange='NMRDescriptionCompare(\"description\","+serialNumber+")' style='padding:0px'>";//
	apendrow +="<option value=''>--select--</option>";
	var tempMinorHeadName="";
	for ( var index = 0; index < tempminorHeadNames.length; index++) {
		debugger;
		let str=tempminorHeadNames[index].split("@@");
		 var regExpr = /[^a-zA-Z0-9-. ]/g;
		let minorHeadName=str[0].replace(regExpr, "");
		//WO_WORK_DESCRIPTION.replace("\"","");
		if(tempMinorHeadName!=(minorHeadName+str[4])){
			tempMinorHeadName=(minorHeadName+str[4]);
			apendrow += "<option value='"+minorHeadName+"@@"+str[1]+'@@' + str[4] + "'>"+str[0]+"("+str[4]+")</option>";
		}
		tempRowData += "<input type='hidden' name='minorWDId"+ serialNumber+ "' value='"+str[1]+"@@"+str[2]+"@@" + str[4] + "'>";
	}
	apendrow += "</select><i class='fa fa-question-circle red-tooltip'  id='WDDwtailsTable"+ serialNumber+ "'  data-toggle='tooltip' data-placement='auto' title=''></i></td>";
	apendrow += "<td style='text-align:center;padding:5px;'><input type='text' class='form-control ManuvalDescription'  placeholder='manual Description' id='ManuvalDescription"+ serialNumber+ "' name='ManuvalDescription"+ serialNumber+ "'>";
	apendrow +=tempRowData;
	apendrow +="</td>";
	apendrow += "<td style='text-align:center;padding:5px;'><input type='text' class='form-control From' id='From"+ serialNumber+ "'  name='FromTime"+ serialNumber+ "' onkeypress='return HoursNumberCheck(this, event)' onfocusout='calHours("+ serialNumber+")' onkeyup='fromToDurationKeyUp("+ serialNumber+")' value='9.00'><input type='hidden' value='9.00' id='FromHidden"+ serialNumber+ "'/></td>";
	apendrow += "<td style='text-align:center;padding:5px;'><input type='text' class='form-control To' id='To"+ serialNumber+ "' name='toTime"+ serialNumber+ "' onkeypress='return HoursNumberCheck(this, event)' onfocusout='calHours("+ serialNumber+")' value='18.00' onkeyup='fromToDurationKeyUp("+ serialNumber+")'><input type='hidden' value='9.00' id='ToHidden"+ serialNumber+ "'/></td>";
	apendrow += "<td style='text-align:center;padding:5px;'><input type='text' class='form-control Noofhours' id='Noofhours"+ serialNumber+ "' name='Noofhours"+ serialNumber+ "' value='9' onkeypress='return HoursNumberCheck(this, event)' onkeyup='NoOFHoursChange("+ serialNumber+")'></td>";
	var strTempFirstRowData="";
	var strTempSecondRowData="";
	
	
	for ( var index = 0; index < tempworkDescNames.length; index++) {
		var str=tempworkDescNames[index].split("@@");
		let WO_WORK_DESCRIPTION=str[0];
		
		//strTempFirstRowData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/></td>';
	//	strTempFirstRowData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control prevWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="prevWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/></td>';

		strTempFirstRowData+='<td style="text-align:center;padding:5px;"><input type="text" class="form-control validatefield" value=""  name="'+(WO_WORK_DESCRIPTION+serialNumber)+'"  id="'+(WO_WORK_DESCRIPTION+serialNumber)+'"  onkeypress="return isNumberCheck(this, event)"   onkeyup="calculateData(this.className, this.id, this.value, '+serialNumber+')" readonly/>';
		
		strTempFirstRowData+='<input type="hidden" class="form-control IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="IndividualWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/><input type="hidden" class="form-control prevWDTotalQTY'+ (WO_WORK_DESCRIPTION )+ '" id="prevWDTotalQTY'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/><input type="hidden" class="form-control prevWDTotalAmount'+ (WO_WORK_DESCRIPTION )+ '" id="prevWDTotalAmount'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value=""  readonly/>';
		
		strTempFirstRowData+='<input type="hidden" name="workDescId'+ serialNumber+'" id="workDescId'+ serialNumber+'" value="'+str[1]+'"></td>';
		strTempFirstRowData += '<td style="text-align:center;padding:5px;"><input type="text" class="form-control WDAmount'+ (WO_WORK_DESCRIPTION )+ '" id="WDRate'+ (WO_WORK_DESCRIPTION + serialNumber)+ '" value="0" readonly/></td>';
		strTempSecondRowData+='<td style="text-align:center;padding:5px;"><input type="text" class="form-control '+WO_WORK_DESCRIPTION+'" value="0" name="'+serialNumber+(WO_WORK_DESCRIPTION+serialNumber+serialNumber)+'"  id="'+serialNumber+(WO_WORK_DESCRIPTION+serialNumber+serialNumber)+'" readonly/><input type="hidden" class="RowWDAmount'+WO_WORK_DESCRIPTION+'" id="RowAmount'+serialNumber+ (WO_WORK_DESCRIPTION+ serialNumber + serialNumber)+ '" ></td>';
	};
	
	apendrow+=strTempFirstRowData;
	apendrow+=strTempSecondRowData;
	
	apendrow += "<td style='text-align:center;padding:5px;'><input type='text' class='form-control' value='' id='Remarks' name='Remarks"+ serialNumber+"'/></td>";
    apendrow += "<td style='text-align:center;padding:5px;'><button type='button'  style='float:left;'  id='AddNMRBtn"+ serialNumber+"'  class='btnaction' onclick='appendRow("+ serialNumber+")'><i class='fa fa-plus'></i></button>";
    apendrow += "<button type='button' style='float:left;' class='btnaction' id='DeleteNMRBtn"+ serialNumber+"' onclick='DeleteNMRBillRow("+ serialNumber+")'><i class='fa fa-trash'></i></button></td>";
	
	apendrow += "</tr>";
	var getLastRowId =$('.tablerowcls:last').attr('id');	
	
	$("#"+getLastRowId).after(apendrow);	
	$("#noOfRowsToIterate").val(serialNumber);
	$("#Date"+serialNumber).datepicker({dateFormat: 'dd-mm-yy', maxDate:0 });
	$(function() {
	    $("#Description"+serialNumber).combobox();    	        
	}); 
	$(".From, .To, .Noofhours").bind('paste', function (e) {
		e.preventDefault();
	});
	serialNumber++;	
}
//calculating WD valus when you change 
function calculateData(current, id, value, serialno){
	debugger;
	/*if(value=="0"||value=="0.00"||value==""){
		return false;
	}*/
	var pageType=$("#statusOfPage").val();
	if ($("#"+id).is('[readonly]') && pageType=="NMRcreatePage") {
		return false;
	}
var currentclass=current.split("form-control ")[1].split(" validatefield")[0];
var WDAmount=current.split("form-control ")[1].split(" validatefield")[1];
var Id=id;
var splitId=Id.split(/([0-9]+)/)[0];
var currentdata=value;

var Date=$("#Date"+serialno).val();
var Description=$("#Description"+serialno).val();
var From=$("#From"+serialno).val();
var To=$("#To"+serialno).val();
var Noofhours=$("#Noofhours"+serialno).val();

if(Date==''){
	alert("Please select Date.");
	$("#"+id).val('');
	$("#Date"+serialno).focus();
	return false;
}
if(Description==''){
	alert("Please select Description.");
	$("#"+id).val('');
	$("#Description"+serialno).focus();
	return false;
}
if(From==''){
	alert("Please enter  From.");
	$("#"+id).val('');
	$("#From"+serialno).focus();
	return false;
}
if(To==''){
	alert("Please enter To.");
	$("#"+id).val('');
	$("#To"+serialno).focus();
	return false;
}
if(Noofhours==''||Noofhours=='0'||Noofhours=='0.00'){
	
	alert("Please enter No of hours.");
	$("#"+id).val('');
	$("#Noofhours"+serialno).focus();
	return false;
}

var currentcalQty=0;

var TotalQtyCal=0;
var prevWDTotalAmount=0;

debugger;
try{
	currentclass=currentclass.trim();
	//TotalQtyCal=parseFloat($("#"+splitId+"TotalQty").text()==""?0:$("#"+splitId+"TotalQty").text());	
	TotalQtyCal=parseFloat($("#IndividualWDTotalQTY"+id).val()==""?0:$("#IndividualWDTotalQTY"+id).val());
	prevWDTotalAmount=parseFloat($("#prevWDTotalAmount"+id).val()==""?0:$("#prevWDTotalAmount"+id).val());
	
}catch(e){
	
}


$("."+currentclass).each(function(){
	var id=$(this).attr("id").match(/\d+/g);
	currentcalQty+=parseFloat(($(this).val()*$("#Noofhours"+id).val()));
})
currentcalQty=currentcalQty/8;

//var previousCumcalQty=parseFloat($("#PC"+splitId+"Qty").text());
var previousCumcalQty=parseFloat($("#prevWDTotalQTY"+id+"").val()==""?0:$("#prevWDTotalQTY"+id+"").val());

var canIexecute=0;
if(TotalQtyCal<(currentcalQty+previousCumcalQty)){	
	alert("Unable to initiate more than available quantity");
	var actualval=$("#actual"+id).val()==undefined?0:$("#actual"+id).val();
	$("#"+id).val(actualval);
	$("#"+id+serialno).val('0');	
	assignvalu=parseFloat(0);
	AssignWDAmount=parseFloat(0);
	canIexecute=1;
}

debugger;
currentdata=$("#"+id).val();
var assignvalu=Noofhours*currentdata;
var asignedId=splitId+serialno;
var assignedId=serialno+splitId+serialno+serialno;
var AssignWDAmount=parseFloat(assignvalu)*parseFloat(WDAmount==undefined?0:WDAmount);
var totalWDAmount=(TotalQtyCal*parseFloat(WDAmount==undefined?0:WDAmount)).toFixed(2);
debugger;
if(totalWDAmount<((currentcalQty*parseFloat(WDAmount==undefined?0:WDAmount))+prevWDTotalAmount) && canIexecute=="0"){
	alert("Initiated more Amount than in Work Order for "+splitId+" in "+($("#Description"+serialno).val().split("@@"))[0]);
	var actualval=$("#actual"+id).val()==undefined?0:$("#actual"+id).val();
	$("#"+id).val(actualval);
	$("#"+assignedId).val("0.00");
	$("#"+id+serialno).val('0');	
	assignvalu=parseFloat(0);
	AssignWDAmount=parseFloat(0);
}

$("#"+assignedId).val(assignvalu.toFixed(2));
$("#RowAmount"+assignedId).val(AssignWDAmount.toFixed(2));
$("#"+assignedId).attr("title", assignvalu.toFixed(2));
var tablelength=$("#NMRBillstableId > tbody > tr").length;	
var totalvalamount=0;

$("#"+splitId+"Days").val('0');
//calculate description values
$(".tablerowcls" ).each(function() {
	
	var rowId=$(this).attr("id");
	var res = rowId.split("tablerow")[1];
	var totalval=parseFloat($("#"+splitId+res).val()==""?0:$("#"+splitId+res).val());
	if(totalval=="" || totalval===NaN){
		totalval=0;
	}
	var NoOfHours=$("#Noofhours"+res).val();
	if(NoOfHours=='' || NoOfHours===NaN){
		NoOfHours=0;
	}
	debugger;
	var totalNumberofHours=totalval*NoOfHours;
	totalvalamount+=totalNumberofHours;
	var calDays=totalNumberofHours/8;
	//$("#"+splitId+"Days").val((parseFloat($("#"+splitId+"Days").val())+calDays).toFixed(2));
		calDays=parseFloat(calDays);
			var tempCalDays=calDays.toFixed(2);
			tempCalDays=parseFloat(tempCalDays);
			var tempDays=parseFloat($("#"+splitId+"Days").val());
			/*$("#"+splitId+"Days").val((tempCalDays+tempDays).toFixed(2));
			$("#"+splitId+"Days").attr("title", $("#"+splitId+"Days").val());*/
});

	$("#"+splitId+"finalHours").val(totalvalamount==""?"0.00": totalvalamount.toFixed(2));
	$("#"+splitId+"finalHours").attr("title", $("#"+splitId+"finalHours").val());
	var calDays=totalvalamount/8;
	var getRate=$("#"+splitId+"Rate").text();
	var calQtyRate=calDays*getRate;
	$("#"+splitId+"Days").val(parseFloat(totalvalamount/8).toFixed(3));
	$("#"+splitId+"Days").attr("title", $("#"+splitId+"Days").val());
	
	
	var WDFinalAmount=0;
	$(".RowWDAmount"+splitId).each(function(){
		var currentAmount=parseFloat($(this).val()==''?0:$(this).val());
		WDFinalAmount+=currentAmount;
	});
	
	$("#FinalWDAmount"+splitId).val(WDFinalAmount.toFixed(2));
	
	var sumOfWDAmount=0;
	$(".WDAmount"+splitId).each(function(){
		var currentAmount=parseFloat($(this).val()==''?0:$(this).val());
		sumOfWDAmount+=currentAmount;
	});
	
	$("#sumOfWDAmount"+splitId).val(sumOfWDAmount.toFixed(2));
}

//checking date
function NMRDateCompare(serialNo){
	debugger;
	var CurrentCompareDate=$("#Date"+serialNo).val();
	$("#Datehidden"+serialNo).val(CurrentCompareDate);
	var hiddenDate=$("#Datehidden"+serialNo).val();	
	$(".Date" ).each(function() {
		debugger;
		$("#Date"+serialNo).val('');
		var current=$(this);
		var currentValue=current.val();
		if(CurrentCompareDate==currentValue){
			alert("Date Should not be same.");
			return false;
		}
		else{
			$("#Date"+serialNo).val(hiddenDate);
		}
	});
}

var minorHeadNameAndDate=new Array();
//checking Description
function NMRDescriptionCompare(condition,serialNo){
	debugger;
	var workDescNames=sessionStorage.getItem("workDescNames");
	var tempworkDescNames=workDescNames.split(",");	
	if(condition!="date"){
		for ( var index = 0; index < tempworkDescNames.length; index++) {
			var str=tempworkDescNames[index].split("@@");
			let WO_WORK_DESCRIPTION=str[0];
			$("#"+WO_WORK_DESCRIPTION+(serialNo)).prop("readonly",true);
			$("#"+WO_WORK_DESCRIPTION+(serialNo)).val("0");
			$("#Noofhours"+serialNo).val("0");
			$("#"+serialNo+WO_WORK_DESCRIPTION+serialNo+serialNo).val("0.00");
			$("#"+serialNo+WO_WORK_DESCRIPTION+serialNo+serialNo).attr("title", $("#"+serialNo+WO_WORK_DESCRIPTION+serialNo+serialNo).val());
			$("#WDRate"+WO_WORK_DESCRIPTION+serialNo).val("0");
			$("#WDRate"+WO_WORK_DESCRIPTION+serialNo).attr("title", $("#WDRate"+WO_WORK_DESCRIPTION+serialNo).val());
			$("#RowAmount"+serialNo+WO_WORK_DESCRIPTION+serialNo+serialNo).val("0");
			$("#prevWDTotalQTY"+WO_WORK_DESCRIPTION+(serialNo)).val("0.000");
			$("#IndividualWDTotalQTY"+WO_WORK_DESCRIPTION+(serialNo)).val("0.000");
			//$("#WDRate"+WO_WORK_DESCRIPTION+(serialNo)).val("0");
			
		};
	}
	var CurrentCompareDescription=$("#Description"+serialNo).val();
	var CurrentCompareDate=$("#Date"+serialNo).val();
	//CurrentCompareDescription=CurrentCompareDescription.trim();
	$("#Descriptionhidden"+serialNo).val(CurrentCompareDescription);
	var hiddenDescription=$("#Descriptionhidden"+serialNo).val();
	 if(CurrentCompareDate==""){
			alert("Please Select Date");
			$("#Description"+serialNo).val('');
			$("#Date"+serialNo).focus();
			return false;
		}
		$("#Description"+serialNo).val(hiddenDescription);
		var workOrderNo=$("#workOrderNo").val();
		var approvePage=$("#approvePage").val();
		var typeOfWork="NMR";
		var siteId=$("#siteId").val();
		var NMRBillNo=$("#NMRBillNo").val();
		debugger;
		//if we are changing date that time i backend, so it's not required to call 
		//so if description changed then only call backend for enabling specific text boxes
		if(condition!="date"){
		$.ajax({
			url : "loadWorkDescNMRBills.spring",
			type : "GET",
			data : {
				minorHeadId:hiddenDescription,		
				workOrderNo:workOrderNo,
				siteId:siteId,
				billNo:NMRBillNo
			},
			success : function(data) {
				var WDDwtailsTable="<table class='table table-bordered WDDwtailsTablecls' style='border:1px solid #000'><thead><th>S.NO</th><th>Work Description</th><th>Quantity</th><th>Allocated Quantity</th><th>Available Quantity</th></thead><tbody>";
				var WDDwtailsTableSerial_Num=1;
				$.each(data,function(key,value){
				debugger;
				WO_WORK_DESCRIPTION=value.WO_WORK_DESCRIPTION.replace(/ /g,'');
				var regExpr = /[^a-zA-Z0-9-. ]/g;
		 		WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(regExpr,"");  
				//$("#"+WO_WORK_DESCRIPTION+(serialNo)).val(value.ALLOCATED_QTY);
		 		//
		 		$("#IndividualWDTotalQTY"+WO_WORK_DESCRIPTION+(serialNo)).val(value.QUANTITY);
				$("#"+WO_WORK_DESCRIPTION+(serialNo)).prop("readonly",false);
				$("#"+WO_WORK_DESCRIPTION+(serialNo)).bind('paste', function (e) {
					e.preventDefault();
				});
				$("#"+WO_WORK_DESCRIPTION+(serialNo)).attr("class", "form-control "+value.WO_MINORHEAD_ID+value.WO_ROW_CODE+WO_WORK_DESCRIPTION+" validatefield "+value.RATE+" ");
				$("#WDRate"+WO_WORK_DESCRIPTION+(serialNo)).val(value.RATE);
				$("#WDRate"+WO_WORK_DESCRIPTION+(serialNo)).attr("title", $("#WDRate"+WO_WORK_DESCRIPTION+(serialNo)).val());
				$("#Noofhours"+serialNo).addClass(value.WO_MINORHEAD_ID+WO_WORK_DESCRIPTION+"NoOfHrs");
				
				
				
				var previousQty = 0;
				var prevAreaQuantity = new Array();
				var lengthOfThePrevArea = 0;
				var previousNMRBillAmount=0;
				var printPreviousRateAndQty="";											
				try {	debugger;
					var index = value.PREVQTY.search("@@");
					if (index >= 0) {
					prevAreaQuantity = value.PREVQTY.split("@@");
					var tempPreviousQty=0;
					var tempAmount=0;
						for (var ind = 0; ind < prevAreaQuantity.length; ind++) {
							let	array_element = prevAreaQuantity[ind].split("##");
							var noOfWorker=parseFloat(array_element[0]);
							var noOfHrs=parseFloat(array_element[1]);
							var rate=parseFloat(array_element[2]);
							tempPreviousQty += parseFloat(((noOfWorker* noOfHrs)));
							tempAmount+= parseFloat(((noOfWorker* noOfHrs*rate)));
														
							previousQty=tempPreviousQty;
							previousNMRBillAmount=tempAmount;
						}
					} else {
						debugger;
						prevAreaQuantity = value.PREVQTY.split("##");
						previousQty += parseFloat((prevAreaQuantity[0] * prevAreaQuantity[1]));
						previousQty=previousQty.toFixed(3);
						previousNMRBillAmount=parseFloat(previousQty)*prevAreaQuantity[2];
						
					}
						previousNMRBillAmount=((previousNMRBillAmount)/8).toFixed(2);
			} catch (e) {
				console.log(e);
			}
			try {
				previousQty=(previousQty/8).toFixed(3);
			} catch (e) {
				console.log(e);
			}	$("#prevWDTotalAmount"+WO_WORK_DESCRIPTION+(serialNo)).val(previousNMRBillAmount);
				$("#prevWDTotalQTY"+WO_WORK_DESCRIPTION+(serialNo)).val(previousQty);
				WDDwtailsTable+="<tr>"
				WDDwtailsTable+="<td>"+WDDwtailsTableSerial_Num+"</td>"
				WDDwtailsTable+="<td>"+value.WO_WORK_DESCRIPTION+"</td>";	
				WDDwtailsTable+="<td>"+inrFormat(parseFloat(value.QUANTITY).toFixed(3))+"</td>";	
				WDDwtailsTable+="<td>"+inrFormat(parseFloat(previousQty).toFixed(3))+"</td>"	;
				WDDwtailsTable+="<td>"+inrFormat(parseFloat((value.QUANTITY-previousQty)).toFixed(3))+"</td>"	;
				WDDwtailsTable+="</tr>";
				//increasing serial number for WDDwtailsTable;
				WDDwtailsTableSerial_Num++;
			});
				WDDwtailsTable+="</tbody></table>";
			$("#WDDwtailsTable"+serialNo).attr("title", WDDwtailsTable);
			$("#WDDwtailsTable"+serialNo).tooltip();			}
			});
		}
		if(CurrentCompareDescription!='' && condition!="date"){
			calculateBtlClick();
		}
}

//Deleting NMR Details row
function DeleteNMRBillRow(serialNo){
	debugger;
	var canIDelete = window.confirm("Do you want to Delete?");
	if(canIDelete == false) { return false;     }	
	
	var tableRowsLength=$('.tablerowcls').length;
	if(tableRowsLength==1){
		alert("this row con't be deleted.");
		return false;
	}
	//removing row
	$("#tablerow"+serialNo).remove();
	
	var getLastRowId=$('.tablerowcls:last').attr('id');	
	var res = getLastRowId.split("tablerow")[1];
	if(tableRowsLength==2){
		$("#DeleteNMRBtn"+res).hide();
	}
	if(res<serialNo){		
		$("#AddNMRBtn"+res).show();
	}	
	calculateBtlClick();
}

//calculating NMR abstract values
function calculateBtlClick(){	
	debugger;
	var validateStatus=validateAllAbstractRows();	
	if(validateStatus==false){ return false;	}	
	
	workDescNames=sessionStorage.getItem("workDescNames");
	var tempworkDescNames=workDescNames.split(",");	
	for ( var index = 0; index < tempworkDescNames.length; index++) {		
		var totalHours=0;
		var str=tempworkDescNames[index].split("@@");
		let WO_WORK_DESCRIPTION=str[0];
		$("."+WO_WORK_DESCRIPTION).each(function(){
			var currentHours=$(this).val();
			if(currentHours==''){
				currentHours=0;
				alert("please enter the hours.");
				$(this).focus();
				return false;
			}
			else{
				totalHours+=parseFloat(currentHours);
			}
		});
		$("#"+WO_WORK_DESCRIPTION+"finalHours").val(totalHours.toFixed(2))		
		var totaldays=totalHours/8;
		$("#"+WO_WORK_DESCRIPTION+"Days").val(totaldays.toFixed(3));

		var WDFinalAmount=0;
		$(".RowWDAmount"+WO_WORK_DESCRIPTION).each(function(){
			var currentAmount=parseFloat($(this).val()==''?0:$(this).val());
			WDFinalAmount+=currentAmount;
		});
		
		$("#FinalWDAmount"+WO_WORK_DESCRIPTION).val(WDFinalAmount.toFixed(2));
		
		var sumOfWDAmount=0;
		$(".WDAmount"+WO_WORK_DESCRIPTION).each(function(){
			var currentAmount=parseFloat($(this).val()==''?0:$(this).val());
			sumOfWDAmount+=currentAmount;
		});
		
		$("#sumOfWDAmount"+WO_WORK_DESCRIPTION).val(sumOfWDAmount.toFixed(2));
	}
}
//validating description values at the time of submit 
function validatedescriptionValues(){
	var error=true;
	$(".validatefield").each(function(){
		if($(this).val()==''){
			alert("please enter value");
			var id=$(this).attr("id");
			$("#"+id).focus();
			return  error=false;
		}
	});
	return  error;
}
//No Of Hours change function for reset and calculating NMR details 
function NoOFHoursChange(id){
	getWorkDesc(id);
	if($("#Noofhours"+id).val()!=""){
		calculateBtlClick();
	}
}
var dateDate;
var dateDate1;
//NMR Details table submit button function .......calculating NMR deatils and printing NMR abstract table
function NMRCreateTableSubmit(){
	debugger;
	dateDate=[];
	dateDate1=[];	
	var descriptionVal=validatedescriptionValues();
	if(descriptionVal==false){ return false; }
	
	$(".Date").each(function(){
		if($(this).val()!=''){
			var date = moment($(this).val(), "DD-MM-YYYY").toDate();
			var currentDate=$(this).val();
			dateDate.push(new Date($(this).val()));
			dateDate1.push(date);
		}
	});
	  workDescNames=sessionStorage.getItem("workDescNames");
	var tempworkDescNames=workDescNames.split(",");
	
	 $(".Description" ).each(function() {
		debugger;
		 var current=$(this);
			  var currentId=current.attr('id');
			  var splitId=currentId.split("Description")[1];
			 var currentValue=current.val();
			 try {
				 currentValue=currentValue.split("@@")[1]+currentValue.split("@@")[2];
				
			} catch (e) {
				
			}	
			var noOfHours=$("#Noofhours"+splitId).val();
			
			var tect=0;
		for ( var j = 0; j < tempworkDescNames.length; j++) {debugger;
			var str=tempworkDescNames[j].split("@@");
			 var regExpr = /[^a-zA-Z0-9-. ]/g;
		 	WO_WORK_DESCRIPTION=str[0].replace(regExpr,"");  
		 	WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(/ /g, '');
		 	var str1=currentValue+WO_WORK_DESCRIPTION;
		 	$("#"+str1+"Qty").text('0');		
			$("#"+str1+"Amount").text('0');		
		}
	});
	var validateStatus=validateAllAbstractRows();	
	if(validateStatus==false){
		return false;
	}
	workDescNames=sessionStorage.getItem("workDescNames");
	$("#NMRCreateBtn").attr("data-dismiss", "modal");
	
	var RowsCount=[];			
	var tableRowsLength=$('.tablerowcls').length;	
	/*$(".Description" ).each(function() {
		debugger; 
		var current=$(this);
		  var currentId=current.attr('id');
		  var splitId=currentId.split("Description")[1];
		 var currentValue=current.val();
		 try {
			 currentValue=currentValue.split("@@")[1]+currentValue.split("@@")[2];			
		} catch (e) {
			// TODO: handle exception
		}	
	for ( var j = 0; j < tempworkDescNames.length; j++) {
		var str=tempworkDescNames[j].split("@@");	
		var str1=currentValue+str[0];
		$("#"+str1+"Qty").text('');		
		
	}
	});*/
	
	for ( var j = 0; j < tempworkDescNames.length; j++) {debugger;
		var str=tempworkDescNames[j].split("@@");
		var regExpr = /[^a-zA-Z0-9-. ]/g;
		var str1=str[0].replace(/ /g, '');
		var regExpr = /[^a-zA-Z0-9-. ]/g;
		 var  WO_WORK_DESCRIPTION=str[0].replace(regExpr, "");
		$("#"+str1+"Amount").text(inrFormat(parseFloat($("#FinalWDAmount"+str1).val()/8).toFixed(2)));		
		$("#"+str1+"Qty").text($("#"+str1+"Days").val()==''?"0.000":$("#"+str1+"Days").val());
		
		
		if($("#"+str1+"finalHours").val()!="0.00"){
			$("#"+str1+"AvgRate").text(inrFormat(parseFloat(($("#FinalWDAmount"+str1).val()==''?0:$("#FinalWDAmount"+str1).val())/($("#"+str1+"finalHours").val()==''?0:$("#"+str1+"finalHours").val())).toFixed(2)));
		}else{
			//alert("0.00");
			$("#"+str1+"AvgRate").text("-");
		}		
		$("#CB"+str1+"Qty").text(($("#PC"+str1+"Qty").text()==""?0:parseFloat($("#PC"+str1+"Qty").text())+parseFloat($("#"+str1+"Qty").text()==''?0:$("#"+str1+"Qty").text())).toFixed(3));		
		$("#CB"+str1+"Amount").text(inrFormat(($("#PC"+str1+"Amount").text().replace(/,/g,'')==""?0:parseFloat($("#PC"+str1+"Amount").text().replace(/,/g,''))+parseFloat($("#"+str1+"Amount").text().replace(/,/g,''))).toFixed(2)));
		
		
	}
	
	/*$(".Description" ).each(function() {
		debugger;	 
		var current=$(this);
			 var currentId=current.attr('id');
			 var splitId=currentId.split("Description")[1];
			 var currentValue=current.val();
			 try {
				 currentValue=currentValue.split("@@")[1]+currentValue.split("@@")[2];
			} catch (e) {
				// TODO: handle exception
			}	
			var noOfHours=$("#Noofhours"+splitId).val();
			
			var tect=0;
		for ( var j = 0; j < tempworkDescNames.length; j++) {
			var str=tempworkDescNames[j].split("@@");
			 var regExpr = /[^a-zA-Z0-9-. ]/g;
			 
		 	WO_WORK_DESCRIPTION=str[0].replace(regExpr,"");  
		 	WO_WORK_DESCRIPTION=WO_WORK_DESCRIPTION.replace(/ /g, '');
			var NoOfPersons=$("#"+WO_WORK_DESCRIPTION+splitId).val();
			
			var calPersons=parseFloat((noOfHours*NoOfPersons)/8);
			try{
			calPersons=calPersons.toFixed(2);
			calPersons=parseFloat(calPersons);
			
			}catch(e){
				console.log(e);
			}
			
			var str1=currentValue+WO_WORK_DESCRIPTION;
			tect=$( "#"+str1+"Qty" ).text();
			var Rate=$("#"+str1+"Rate").text();
			var tempPersons=tect==""?0:parseFloat(tect);
			debugger;
			$("#"+str1+"Qty").text((calPersons+tempPersons).toFixed(2));		
			$("#"+str1+"Amount").text((parseFloat($("#"+str1+"Qty").text()).toFixed(2)*Rate).toFixed(2));		
			
			$("#CB"+str1+"Qty").text(($("#PC"+str1+"Qty").text()==""?0:parseFloat($("#PC"+str1+"Qty").text())+parseFloat($("#"+str1+"Qty").text()==''?0:$("#"+str1+"Qty").text())).toFixed(2));		
			$("#CB"+str1+"Amount").text(($("#PC"+str1+"Amount").text()==""?0:parseFloat($("#PC"+str1+"Amount").text())+parseFloat($("#"+str1+"Qty").text()*Rate)).toFixed(2));
			
		}
		});*/

		CalculateTotalAbstract();
		debugger;
		var statusOfPage=$("#statusOfPage").val();
		if (statusOfPage=="NMRcreatePage") {
			$("#pettyExpensesCurrentCerti").removeAttr("readonly");
			$("#other").removeAttr("readonly");
		}
	
		//to append mindate and maxdate to from and to dates
		var sorted = dateDate.sort(sortDates);
		var minDate = sorted[0];
		var maxDate = sorted[sorted.length-1];
		var sorted1 = dateDate1.sort(sortDates);
		var minDate1 = sorted1[0];
		var maxDate1 = sorted1[sorted.length-1];
		$("#fromDate").text(minDate.getDate()+"-"+(minDate.getMonth()+1)+"-"+minDate.getFullYear());
		$("#toDate").text(maxDate.getDate()+"-"+(maxDate.getMonth()+1)+"-"+maxDate.getFullYear());
		
		$("#fromDate1").text(minDate1.getDate()+"-"+(minDate1.getMonth()+1)+"-"+minDate1.getFullYear());
		$("#toDate1").text(maxDate1.getDate()+"-"+(maxDate1.getMonth()+1)+"-"+maxDate1.getFullYear());
		
		//hide loader
		$(".overlay_ims").hide();
		$(".loader-ims").hide();

}
//get the min date and max date 
function sortDates(a, b){
    return a.getTime() - b.getTime();
}

function validateAllAbstractRows(){
	var err=true;
	$(".tablerowcls").each(function(){
		var id=$(this).attr("id").split("tablerow")[1];
		if($("#Date"+id).val()==''){
			alert("Please select date.");
			$("#Date"+id).focus();
			return err=false;
		}
		if($("#Description"+id).val()==''){
			alert("Please select description.");
			$("#Description"+id).focus();
			return err=false;
		}
		if($("#From"+id).val()==''){
			alert("Please enter from duration.");
			$("#From"+id).focus();
			return err=false;
		}
		if($("#To"+id).val()==''){
			alert("Please enter to duration.");
			$("#To"+id).focus();
			return err=false;
		}
		if($("#Noofhours"+id).val()==''){
			alert("Please enter number of hours.");
			$("#Noofhours"+id).focus();
			return err=false;
		}
	})
	return err;
	
}

//caluculating NMR abstract cumilative, previous and current quantity and amount
function CalculateTotalAbstract(){
debugger;
	var totoalCCQty=0;
	var totoalCCAmount=0;
	var totoalPCQty=0;
	var totoalPCAmount=0;
	var totoalCBQty=0;
	var totoalCBAmount=0;
	//calculating total CC Quantity
	$(".CCQtyClass" ).each(function() {	
		 var current=$(this);
		 var currentValue=current.text();
		 if(currentValue==''){
			 currentValue=0;
		 }
		 totoalCCQty+=parseFloat(currentValue.replace(/,/g,''));
	});
	//calculating total CC Amount
	$(".CCAmountClass" ).each(function() {		
		 var current=$(this);
		 var currentValue=current.text();
		 if(currentValue==''){
			 currentValue=0;
		 }
		 totoalCCAmount+=parseFloat(currentValue.replace(/,/g,''));
	});
	//calculating total PC Quantity
	$(".PCQtyClass" ).each(function() {
		
		 var current=$(this);
		 var currentValue=current.text();
		 if(currentValue==''){
			 currentValue=0;
		 }
		 totoalPCQty+=parseFloat(currentValue.replace(/,/g,''));
	});
	//calculating total PC Amount
	$(".PCAmountClass" ).each(function() {		
		 var current=$(this);
		 var currentValue=current.text();
		 if(currentValue==''){
			 currentValue=0;
		 }
		 totoalPCAmount+=parseFloat(currentValue.replace(/,/g,''));
	});
	//calculating total CB Quantity
	$(".CBQtyClass" ).each(function() {		
		 var current=$(this);
		 var currentValue=current.text();
		 if(currentValue==''){
			 currentValue=0;
		 }
		 totoalCBQty+=parseFloat(currentValue.replace(/,/g,''));
	});
	//calculating total CB Amount
	$(".CBAmountClass" ).each(function() {		
		 var current=$(this);
		 var currentValue=current.text();
		 if(currentValue==''){
			 currentValue=0;
		 }
		 totoalCBAmount+=parseFloat(currentValue.replace(/,/g,''));
	});
	
	var pettyExpensesCurrentCerti=parseFloat($("#pettyExpensesCurrentCerti").val()==""?0:$("#pettyExpensesCurrentCerti").val().replace(/,/g,''));
	var pettyExpensesPrevCerti=parseFloat($("#pettyExpensesPrevCerti").val()==""?0:$("#pettyExpensesPrevCerti").val().replace(/,/g,''));
	var pettyExpensesCumulative=parseFloat($("#pettyExpensesCumulative").val()==""?0:$("#pettyExpensesCumulative").val().replace(/,/g,''));
	
	var other=parseFloat($("#other").val()==""?0:$("#other").val().replace(/,/g,''));
	var otherAmtPrevCerti=parseFloat($("#otherAmtPrevCerti").val()==""?0:$("#otherAmtPrevCerti").val().replace(/,/g,''));
	var otherAmtCumulative=parseFloat($("#otherAmtCumulative").val()==""?0:$("#otherAmtCumulative").val().replace(/,/g,''));
	
	
	var currentRecoveryAmount=parseFloat($("#currentRecoveryAmount").val()==""?0:$("#currentRecoveryAmount").val().replace(/,/g,''));
	var previousRecovery=parseFloat($("#previousRecovery").val()==""?0:$("#previousRecovery").val().replace(/,/g,''));
	var cumulativeRecovery=parseFloat($("#cumulativeRecovery").val()==""?0:$("#cumulativeRecovery").val().replace(/,/g,''));

	//display Total A values
	$("#TotalACBQty").text(totoalCBQty.toFixed(3));
	$("#TotalACBAmt").text(inrFormat(totoalCBAmount.toFixed(2)));
	$("#TotalAPCQty").text(totoalPCQty.toFixed(3));
	$("#TotalAPCAmt").text(inrFormat(totoalPCAmount.toFixed(2)));
	$("#TotalACCQty").text(totoalCCQty.toFixed(3));
	$("#TotalACCAmt").text(inrFormat(totoalCCAmount.toFixed(2)));
	$("#CertifiedAmount").val(totoalCCAmount.toFixed(2));

	//assigning values
	$("#CCTotalQty").text(totoalCCQty.toFixed(3));
	$("#CCToatalAmount").text(inrFormat((totoalCCAmount-currentRecoveryAmount-pettyExpensesCurrentCerti-other).toFixed(2)));
	$("#paybleAmount").val((totoalCCAmount-currentRecoveryAmount-pettyExpensesCurrentCerti-other).toFixed(2));
	$("#finalAmt").val(inrFormat((totoalCCAmount-currentRecoveryAmount-pettyExpensesCurrentCerti-other).toFixed(2)));	
	$("#actualPaybleAmount").val((totoalCCAmount-currentRecoveryAmount).toFixed(2));
	
	$("#PCTotalQty").text(totoalPCQty.toFixed(3));
	$("#PCToatalAmount").text(inrFormat((totoalPCAmount-previousRecovery-pettyExpensesPrevCerti-otherAmtPrevCerti).toFixed(2)));
	$("#CBTotalQty").text(totoalCBQty.toFixed(3));
	$("#CBToatalAmount").text(inrFormat((totoalCBAmount-cumulativeRecovery-pettyExpensesCumulative-otherAmtCumulative).toFixed(2)));

	var amountInWords=	convertNumberToWords((totoalCCAmount-currentRecoveryAmount-pettyExpensesCurrentCerti-other).toFixed(2));
	$("#finalTotalWorkOrderAmountInWords").text(amountInWords);
	$("#printfinalTotalWorkOrderAmountInWords").text(convertNumberToWords((totoalCCAmount).toFixed(2)));
}
//converting number to words 
function convertNumberToWords(amount) {
    var words = new Array();
    words[0] = '';
    words[1] = 'One';
    words[2] = 'Two';
    words[3] = 'Three';
    words[4] = 'Four';
    words[5] = 'Five';
    words[6] = 'Six';
    words[7] = 'Seven';
    words[8] = 'Eight';
    words[9] = 'Nine';
    words[10] = 'Ten';
    words[11] = 'Eleven';
    words[12] = 'Twelve';
    words[13] = 'Thirteen';
    words[14] = 'Fourteen';
    words[15] = 'Fifteen';
    words[16] = 'Sixteen';
    words[17] = 'Seventeen';
    words[18] = 'Eighteen';
    words[19] = 'Nineteen';
    words[20] = 'Twenty';
    words[30] = 'Thirty';
    words[40] = 'Forty';
    words[50] = 'Fifty';
    words[60] = 'Sixty';
    words[70] = 'Seventy';
    words[80] = 'Eighty';
    words[90] = 'Ninety';
    amount = amount.toString();
    var atemp = amount.split(".");
    var number = atemp[0].split(",").join("");
    var n_length = number.length;
    var words_string = "";
    if (n_length <= 9) {
        var n_array = new Array(0, 0, 0, 0, 0, 0, 0, 0, 0);
        var received_n_array = new Array();
        for (var i = 0; i < n_length; i++) {
            received_n_array[i] = number.substr(i, 1);
        }
        for (var i = 9 - n_length, j = 0; i < 9; i++, j++) {
            n_array[i] = received_n_array[j];
        }
        for (var i = 0, j = 1; i < 9; i++, j++) {
            if (i == 0 || i == 2 || i == 4 || i == 7) {
                if (n_array[i] == 1) {
                    n_array[j] = 10 + parseInt(n_array[j]);
                    n_array[i] = 0;
                }
            }
        }
        value = "";
        for (var i = 0; i < 9; i++) {
            if (i == 0 || i == 2 || i == 4 || i == 7) {
                value = n_array[i] * 10;
            } else {
                value = n_array[i];
            }
            if (value != 0) {
                words_string += words[value] + " ";
            }
            if ((i == 1 && value != 0) || (i == 0 && value != 0 && n_array[i + 1] == 0)) {
                words_string += "crore";
            }
            if ((i == 3 && value != 0) || (i == 2 && value != 0 && n_array[i + 1] == 0)) {
                words_string += "Lakhs ";
            }
            if ((i == 5 && value != 0) || (i == 4 && value != 0 && n_array[i + 1] == 0)) {
                words_string += "Thousand ";
            }
            if (i == 6 && value != 0 && (n_array[i + 1] != 0 && n_array[i + 2] != 0)) {
                words_string += "Hundred and ";
            } else if (i == 6 && value != 0) {
                words_string += "Hundred ";
            }
        }
        words_string = words_string.split("  ").join(" ");
    }
    return words_string+" Rupees Only";
}


function inrFormat(nStr) { // nStr is the input string
    nStr += '';
    x = nStr.split('.');
    x1 = x[0];
    x2 = x.length > 1 ? '.' + x[1] : '';
    var rgx = /(\d+)(\d{3})/;
    var z = 0;
    var len = String(x1).length;
    var num = parseInt((len/2)-1);

     while (rgx.test(x1))
     {
       if(z > 0)
       {
         x1 = x1.replace(rgx, '$1' + ',' + '$2');
       }
       else
       {
         x1 = x1.replace(rgx, '$1' + ',' + '$2');
         rgx = /(\d+)(\d{2})/;
       }
       z++;
       num--;
       if(num == 0)
       {
         break;
       }
     }
    return x1 + x2;
}
//opening calender in nmr details popup when you click on calender icon
function openCalenderInNMRDetails(id){
	$("#Date"+id).focus();
}

function pettyAndOtherChange(currentInpuId){debugger;
		debugger;
		var TotalACCAmt = parseFloat($("#TotalACCAmt").text());
		if (TotalACCAmt == '0') {
			alert("Please enter abstract.");
			$("#NMRBillTableModal").modal();
		}
		
		var CCPettyVal=parseFloat($("#pettyExpensesCurrentCerti").val()==''?0:$("#pettyExpensesCurrentCerti").val().replace(/,/g,''));
		var CumCpettyVal=parseFloat($("#pettyExpensesPrevCerti").val()==''?0:$("#pettyExpensesPrevCerti").val().replace(/,/g,''))+CCPettyVal;
		$("#pettyExpensesCumulative").val(inrFormat(CumCpettyVal.toFixed(2)));
	
		var CCotherVal=parseFloat($("#other").val()==''?0:$("#other").val().replace(/,/g,''));
		var CumCotherVal=parseFloat($("#otherAmtPrevCerti").val()==''?0:$("#otherAmtPrevCerti").val().replace(/,/g,''))+CCotherVal;
		$("#otherAmtCumulative").val(inrFormat(CumCotherVal.toFixed(2)));
		
		var CurrentDed=0;
		$(".currentCAmnt").each(function(){
			debugger;
			CurrentDed+=parseFloat($(this).val()==''?0:$(this).val().replace(/,/g,''));
		});
		$("#totalAmtCurntDeduc").val(inrFormat(CurrentDed.toFixed(2)));
		
		var PrevDed=0;						
		$(".PreviousCAmnt").each(function(){
			PrevDed+=parseFloat($(this).val()==''?0:$(this).val().replace(/,/g,''));
		});
		
		$("#totalAmtPrevCerti").val(inrFormat((PrevDed).toFixed(2)));
		var CumilativeDed=0;
		$(".CumilativeCAmount").each(function(){
			CumilativeDed+=parseFloat($(this).val()==''?0:$(this).val().replace(/,/g,''));
		});
		$("#totalAmtCumulative").val(inrFormat(CumilativeDed.toFixed(2)));
		
		
		CalculateTotalAbstract();
		var NMRWoAmount=$("#printtotalAmtToPay").text().replace(/,/g,'');
		if($("#CCToatalAmount").text().replace(/,/g,'')<0){
			$("#"+currentInpuId).val("0");
			alert("Initiated amount is greater than actual amount.");
			pettyAndOtherChange(currentInpuId);
			CalculateTotalAbstract();
			return false;
		}
}







