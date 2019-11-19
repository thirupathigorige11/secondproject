

/*$(document).ready(function(){
	$(".overlay_ims").show();
	$(".loader-ims").show();
});*/
	
//this code is for temp po for price master, comparing price
var newWindow ;
function loadPriceMasterData(childProdId,childProdName){}
var countSiteAjaxCall=0;
$(document).ready(function(){
	
	$("#viewReport").click(function(){
		debugger;
		$(".toggleSites").toggle();
		$(".hideshowReport").hide();
		
		//ajax call for site id
		var siteNames;	
		if(countSiteAjaxCall==0){
			$(".loader-ims").show();
			$(".overlay_ims").show();		
		$.ajax({
			  url : "./loadAllSites.spring",
			  type : "GET",
			  dataType : "json",
			  success : function(resp) {
				  debugger;
				  $("#siteNames").html("");				  
				  $.each(resp,function(index,value){
					 siteNames= '<div class="col-md-3"><input type="checkbox" name="siteName" id='+resp[index].SITE_ID+' class="checkboxsites" value="'+resp[index].SITE_ID+'$$'+resp[index].SITE_NAME+'">&nbsp;'+resp[index].SITE_NAME+'</div>'
					 $("#siteNames").append(siteNames);
				  });
				  $(".hideshowReport").show();
				 // $("#appendPriceMasterData").html("");
				 // $("#isShowHideNote").append("<p class='Mrgtop20'><strong >Note :</strong> No data available of <strong><span id='childprodName'></span></strong> for Projects <span id='nositeNote'></span></p>");
					$(".loader-ims").hide();
					$(".overlay_ims").hide();
					countSiteAjaxCall++;
			  },
			  error:  function(data, status, er){
				//  alert(data+"_"+status+"_"+er);
				  }
			  }); 
		}
		  $(".hideshowReport").show();
	});
	 $("#checkboxsites").click(function () {
	     $('input:checkbox').not(this).prop('checked', this.checked);
	     $(".hideshowSitedata").show();
	 });
	 /*ajax call for load sites*/
	
		
});
var siteArray;
var emptySiteNames;

function sitesDatasubmit(){
	
	debugger;	
	if($(".checkboxsites:checkbox:checked").length==0){
		alert("Please select at least one site");
		return false;
	}
	
	var isChecked;
	var ajaxCount=0;
	 var childProdId=$("#childProdId").val();
	 var selectedSiteData=$("#selectedSiteData").val();
	 var childProdName=$("#childProdName").val();
	 $("#isShowHideNote").hide();
	 
	 $("#childprodName").html(childProdName);
	 
	 $("#nositeNote").html("");	 
	$(".checkboxsites").each(function(){
		if($(this).is(":checked")){
			
		}
		/*else{
			alert("Please select atleast one project.")
		}*/
	});
	siteArray = [];
	emptySiteNames=[];
	var count=1;
	$("#appendPriceMasterData").html("");
	$(".checkboxsites").each(function(){
		
		/*if($(this).val().split("$$")[0]==selectedSiteData){
			count++;
		}*/
		//if($(this).val().split("$$")[0]!=selectedSiteData){
			
		if($(this).is(":checked")){
			$(".loader-ims").show();
			$(".overlay_ims").show();
			siteArray.push($(this).val().split("$$")[0]);
		debugger;
		//alert( $(".checkboxsites:checkbox:checked").length );
			var priceMasterTableHead="";
			var priceMasterTableBody="";
			var PriceMaterTable="";
			 
			PriceMaterTable+= '<div class="clearfix"></div>';
			/*$("#priceMasterTableHead").html("");
			$("#priceMasterTableBody").html("");*/
			 PriceMaterTable+= '<h4  id="siteId'+$(this).val().split("$$")[0]+'" style="display:none;">'+$(this).val().split("$$")[1]+'</h4 >';
			 PriceMaterTable+= '<div class="table-responsive" id="divSiteId'+$(this).val().split("$$")[0]+'" style="display:none;">'
			 PriceMaterTable+= '<table id="priceMasterTable'+$(this).val().split("$$")[0]+'"	class="table table-bordered  newwindow-table"  cellspacing="0" style="width:2500px;">'
			 PriceMaterTable+= '<thead id="'+$(this).val().split("$$")[0]+'priceMasterTableHead'+$(this).val().split("$$")[0]+'" >'
			 PriceMaterTable+= '</thead>';
			 PriceMaterTable+= '<tbody id="'+$(this).val().split("$$")[0]+'priceMasterTableBody'+$(this).val().split("$$")[0]+'" >';
			 PriceMaterTable+= '</tbody>';
			 PriceMaterTable+= '</table>';
			 PriceMaterTable+= '</div>';
			
			$("#appendPriceMasterData").append(PriceMaterTable);
			
			priceMasterTableHead+='<tr>';
			priceMasterTableHead+='<th rowspan="2" style="vertical-align:middle">Child Product<input type="hidden" name="childProdId" id="childProdId" value="'+childProdId+'"> </th>';
			priceMasterTableHead+='<th rowspan="2" style="vertical-align:middle">Measurement</th>';
			$.ajax({
				 url : "./getProductDetailsForPO.spring",
				  type : "GET",
				  data:{
					  CHILD_PRODUCT_ID:childProdId,
					  SITE_ID:$(this).val().split("$$")[0],
					  SITE_NAME:$(this).val().split("$$")[1],
					  NAME:childProdName
				  },
				dataType : "json",
				success : function(data) {
					$(".loader-ims").show();
					$(".overlay_ims").show();
					
					let countMonthAvailbleData=0;
					let getChildProdNameLength=0;
					
					
					debugger;
					let thirdMonth="";
					let secondMonth="";
					let firstMonth="";
					var siteId="";
					//$("#childProductName").modal("show");	
					console.log(data);
					ajaxCount++;
					try{	
						
						
						//this code is for printing month names
							thirdMonth=data[0].length
							if(thirdMonth>0){
								priceMasterTableHead+='<th style="text-align:center;">'+data[0][0].month_name+'</th>';
								siteId=data[0][0].site_id;
								countMonthAvailbleData++;
							}
							secondMonth=data[1].length
							if(secondMonth>0){
								priceMasterTableHead+='<th style="text-align:center;">'+data[1][0].month_name+'</th>';
								siteId=data[1][0].site_id;
								countMonthAvailbleData++;
							}
							firstMonth=data[2].length
							if(firstMonth>0){
								priceMasterTableHead+='<th style="text-align:center;">'+data[2][0].month_name+'</th>';
								siteId=data[2][0].site_id;
								countMonthAvailbleData++;
							}
							if(countMonthAvailbleData==1){
								countMonthAvailbleData=1.2;
							}
							$("#priceMasterTable"+siteId).css({
								"width":1100*countMonthAvailbleData
							});
							
							//this code is for printing child prod name and mesurment
							if(thirdMonth>0){
								getChildProdNameLength=data[0][0].child_product_name.length;
								$("#childprodName").html(data[0][0].child_product_name);
								priceMasterTableBody+='<td style="text-align:center;border: 1px solid;padding-top: 10px;vertical-align: text-bottom;"  id="txtchildProductName'+siteId+'">'+data[0][0].child_product_name+'</td>';
								priceMasterTableBody+='<td style="text-align:center;border: 1px solid;vertical-align: text-bottom;">'+data[0][0].measurement_name+'</td>';
							}else if(secondMonth>0){
								getChildProdNameLength=data[1][0].child_product_name.length;
								$("#childprodName").html(data[1][0].child_product_name);
								priceMasterTableBody+='<td style="text-align:center;border: 1px solid;padding-top: 10px;vertical-align: text-bottom;"  id="txtchildProductName'+siteId+'">'+data[1][0].child_product_name+'</td>';
								priceMasterTableBody+='<td style="text-align:center;border: 1px solid;vertical-align: text-bottom;">'+data[1][0].measurement_name+'</td>';
							}else if(firstMonth>0){
								getChildProdNameLength=data[2][0].child_product_name.length;
								$("#childprodName").html(data[2][0].child_product_name);
								priceMasterTableBody+='<td style="text-align:center;border: 1px solid;padding-top: 10px;vertical-align: text-bottom;"  id="txtchildProductName'+siteId+'">'+data[2][0].child_product_name+'</td>';
								priceMasterTableBody+='<td style="text-align:center;border: 1px solid;vertical-align: text-bottom;">'+data[2][0].measurement_name+'</td>';
							}
							
							var forRowSpan=0;
							if (firstMonth >= secondMonth && firstMonth >= thirdMonth) {
								console.log("First number is largest. ");
								forRowSpan = firstMonth;
							} else if (secondMonth >= firstMonth && secondMonth >= thirdMonth) {
								console.log("Second number is largest.");
								forRowSpan = secondMonth;
							} else if (thirdMonth >= firstMonth && thirdMonth >= secondMonth) {
								console.log("Third number is largest.");
								forRowSpan = thirdMonth;
							} else{
								console.log("Entered numbers are not distinct.");
							}
							console.log(forRowSpan);
					}catch(e){
						console.log(e);
					}
					if(getChildProdNameLength>250){
						if(forRowSpan<3){
							forRowSpan=3;
						}
					}
					priceMasterTableHead+='</tr>';
					
					debugger;
					if (thirdMonth == 0 && secondMonth == 0 && firstMonth== 0) {
						emptySiteNames.push(data[3][0].site_id+"@@"+data[3][0].site_name);
						$("#isShowHideNote").show();
						$("#childprodName").html(data[3][0].child_product_name);
						var noSites;
						debugger;
						//here we are displaying site names with comma based on condition
						if(siteArray.length!=1){
							if(siteArray.length==ajaxCount){
								noSites = "<span>and "+data[3][0].site_name+".</span>";
							}else if((siteArray.length-1)==ajaxCount){
								noSites = "<span>"+data[3][0].site_name+" </span>";
							}else{
								noSites = "<span>"+data[3][0].site_name+", </span>";
							}
						}else{
							noSites = "<span>"+data[3][0].site_name+".</span>";
						}
						$("#nositeNote").append(noSites);
					//	$("#"+data[3][0].site_id+"priceMasterTableBody"+data[3][0].site_id).html("No Data available from past months");
					}
					else{
						$("#siteId"+data[3][0].site_id).show();
						$("#divSiteId"+data[3][0].site_id).show();
					}
					
					
					if(thirdMonth>0){
						
						priceMasterTableBody+='<td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid;">';
						priceMasterTableBody+='<table border="1"  style="width: 100%;text-align: center;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;">';
						priceMasterTableBody+='<tr><th class="text-center" style="height:30px;">Amount/unit Before Taxes</th><th scope="col" style="height:30px;"class="text-center"> Amount/Unit After Taxes </th><th style="height:30px;"class="text-center"> Invoice No/DC No </th><th class="text-center">Vendor Name</th></tr>'; 
							for (var incre = 0; incre < forRowSpan; incre++) {
							debugger;
								if(incre<thirdMonth){
									priceMasterTableBody+='<tr style="height: 30px;">';
									priceMasterTableBody+='<td>'+data[0][incre].amount_per_unit_before_taxes+'</td>';
									priceMasterTableBody+='<td>'+data[0][incre].amount_per_unit_after_taxes+'</td>';
									priceMasterTableBody+='<td>';
									if(data[0][incre].invoice_number!=""&&data[0][incre].dc_number!=""){
										priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[0][incre].invoice_number+'&vendorId='+data[0][incre].vendor_id+'&invoiceDate='+data[0][incre].indent_recive_date+'&indentEntryId='+data[0][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[0][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV00'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[0][incre].invoice_number+'&vendorId='+data[0][incre].vendor_id+'&invoiceDate='+data[0][incre].indent_recive_date+'&indentEntryId='+data[0][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[0][incre].site_id+'\')">INV_'+data[0][incre].invoice_number+'</a>&nbsp/&nbsp';
										priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[0][incre].dc_number+'&vendorId='+data[0][incre].vendor_id+'&dcDate='+data[0][incre].dc_recive_date+'&dcEntryId='+data[0][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[0][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC00'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[0][incre].dc_number+'&vendorId='+data[0][incre].vendor_id+'&dcDate='+data[0][incre].dc_recive_date+'&dcEntryId='+data[0][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[0][incre].site_id+'\')">DC_'+data[0][incre].dc_number+'</a>';
									}else if(data[0][incre].invoice_number!=""){
										priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[0][incre].invoice_number+'&vendorId='+data[0][incre].vendor_id+'&invoiceDate='+data[0][incre].indent_recive_date+'&indentEntryId='+data[0][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[0][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV00'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[0][incre].invoice_number+'&vendorId='+data[0][incre].vendor_id+'&invoiceDate='+data[0][incre].indent_recive_date+'&indentEntryId='+data[0][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[0][incre].site_id+'\')">INV_'+data[0][incre].invoice_number+'</a>';
									}else if(data[0][incre].dc_number!=""){
										priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[0][incre].dc_number+'&vendorId='+data[0][incre].vendor_id+'&dcDate='+data[0][incre].dc_recive_date+'&dcEntryId='+data[0][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[0][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC00'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[0][incre].dc_number+'&vendorId='+data[0][incre].vendor_id+'&dcDate='+data[0][incre].dc_recive_date+'&dcEntryId='+data[0][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[0][incre].site_id+'\')">DC_'+data[0][incre].dc_number+'</a>';
									}
								   
									priceMasterTableBody+='</td>';
									priceMasterTableBody+='<td>'+data[0][incre].vendor_name+'</td>';
									priceMasterTableBody+='</tr>';
								}else{
									priceMasterTableBody+='<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>';
								}
							}
							priceMasterTableBody+='</table>';
							priceMasterTableBody+='</td>';
							
						}
							if(secondMonth>0){
							
							priceMasterTableBody+='<td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid;">';
							priceMasterTableBody+='<table border="1"  style="width: 100%;text-align: center;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;">';
							priceMasterTableBody+='<tr><th class="text-center" style="height:30px;">Amount/unit Before Taxes</th><th scope="col" style="height:30px;"class="text-center"> Amount/Unit After Taxes </th><th style="height:30px;"class="text-center"> Invoice No/DC No </th><th class="text-center">Vendor Name</th></tr>'; 
						
							
							for (var incre = 0; incre < forRowSpan; incre++) {
								debugger;
									if(incre<secondMonth){
										priceMasterTableBody+='<tr style="height: 30px;">';
										priceMasterTableBody+='<td>'+data[1][incre].amount_per_unit_before_taxes+'</td>';
										priceMasterTableBody+='<td>'+data[1][incre].amount_per_unit_after_taxes+'</td>';
										priceMasterTableBody+='<td>';
										if(data[1][incre].invoice_number!=""&&data[1][incre].dc_number!=""){
											priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[1][incre].invoice_number+'&vendorId='+data[1][incre].vendor_id+'&invoiceDate='+data[1][incre].indent_recive_date+'&indentEntryId='+data[1][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[1][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV11'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[1][incre].invoice_number+'&vendorId='+data[1][incre].vendor_id+'&invoiceDate='+data[1][incre].indent_recive_date+'&indentEntryId='+data[1][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[1][incre].site_id+'\')">INV_'+data[1][incre].invoice_number+'</a>&nbsp/&nbsp';
											priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[1][incre].dc_number+'&vendorId='+data[1][incre].vendor_id+'&dcDate='+data[1][incre].dc_recive_date+'&dcEntryId='+data[1][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[1][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC11'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[1][incre].dc_number+'&vendorId='+data[1][incre].vendor_id+'&dcDate='+data[1][incre].dc_recive_date+'&dcEntryId='+data[1][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[1][incre].site_id+'\')">DC_'+data[1][incre].dc_number+'</a>';
										}else if(data[1][incre].invoice_number!=""){
											priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[1][incre].invoice_number+'&vendorId='+data[1][incre].vendor_id+'&invoiceDate='+data[1][incre].indent_recive_date+'&indentEntryId='+data[1][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[1][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV11'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[1][incre].invoice_number+'&vendorId='+data[1][incre].vendor_id+'&invoiceDate='+data[1][incre].indent_recive_date+'&indentEntryId='+data[1][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[1][incre].site_id+'\')">INV_'+data[1][incre].invoice_number+'</a>';
										}else if(data[1][incre].dc_number!=""){
											priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[1][incre].dc_number+'&vendorId='+data[1][incre].vendor_id+'&dcDate='+data[1][incre].dc_recive_date+'&dcEntryId='+data[1][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[1][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC11'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[1][incre].dc_number+'&vendorId='+data[1][incre].vendor_id+'&dcDate='+data[1][incre].dc_recive_date+'&dcEntryId='+data[1][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[1][incre].site_id+'\')">DC_'+data[1][incre].dc_number+'</a>';
										}
									   
										priceMasterTableBody+='</td>';
										priceMasterTableBody+='<td>'+data[1][incre].vendor_name+'</td>';
										priceMasterTableBody+='</tr>';
									}else{
										priceMasterTableBody+='<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>';
									}
								}
							priceMasterTableBody+='</table>';
							priceMasterTableBody+='</td>'; 
						}
						
						
						if(firstMonth>0){
							priceMasterTableBody+='<td class="tbl-inner-td" style="padding: 0px;margin:0px;border:1px solid;">';
							priceMasterTableBody+='<table border="1"  style="width: 100%;text-align: center;border-top-style: hidden;border-right-style: hidden;border-left-style: hidden;border-bottom-style: hidden;">';
							priceMasterTableBody+='<tr><th class="text-center" style="height:30px;">Amount/unit Before Taxes</th><th scope="col" style="height:30px;"class="text-center"> Amount/Unit After Taxes </th><th style="height:30px;"class="text-center"> Invoice No/DC No </th><th class="text-center">Vendor Name</th></tr>'; 
						
							for (var incre = 0; incre < forRowSpan; incre++) {
								debugger;
									if(incre<firstMonth){
										priceMasterTableBody+='<tr style="height: 30px;">';
										priceMasterTableBody+='<td>'+data[2][incre].amount_per_unit_before_taxes+'</td>';
										priceMasterTableBody+='<td>'+data[2][incre].amount_per_unit_after_taxes+'</td>';
										priceMasterTableBody+='<td>';
										if(data[2][incre].invoice_number!=""&&data[2][incre].dc_number!=""){
											priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[2][incre].invoice_number+'&vendorId='+data[2][incre].vendor_id+'&invoiceDate='+data[2][incre].indent_recive_date+'&indentEntryId='+data[2][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[2][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV22'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[2][incre].invoice_number+'&vendorId='+data[2][incre].vendor_id+'&invoiceDate='+data[2][incre].indent_recive_date+'&indentEntryId='+data[2][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[2][incre].site_id+'\')">INV_'+data[2][incre].invoice_number+'</a>&nbsp/&nbsp';
											priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[2][incre].dc_number+'&vendorId='+data[2][incre].vendor_id+'&dcDate='+data[2][incre].dc_recive_date+'&dcEntryId='+data[2][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[2][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC22'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[2][incre].dc_number+'&vendorId='+data[2][incre].vendor_id+'&dcDate='+data[2][incre].dc_recive_date+'&dcEntryId='+data[2][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[2][incre].site_id+'\')">DC_'+data[2][incre].dc_number+'</a>';
										}else  if(data[2][incre].invoice_number!=""){
											priceMasterTableBody+='<a href="getGrnDetails.spring?invoiceNumber='+data[2][incre].invoice_number+'&vendorId='+data[2][incre].vendor_id+'&invoiceDate='+data[2][incre].indent_recive_date+'&indentEntryId='+data[2][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[2][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="INV22'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getGrnDetails.spring?invoiceNumber='+data[2][incre].invoice_number+'&vendorId='+data[2][incre].vendor_id+'&invoiceDate='+data[2][incre].indent_recive_date+'&indentEntryId='+data[2][incre].indent_entry_id+'&type=invoicePriceMaster&siteId='+data[2][incre].site_id+'\')">INV_'+data[2][incre].invoice_number+'</a>';
										}else if(data[2][incre].dc_number!=""){
											priceMasterTableBody+='<a href="getDcFormGrnViewDts.spring?invoiceNumber='+data[2][incre].dc_number+'&vendorId='+data[2][incre].vendor_id+'&dcDate='+data[2][incre].dc_recive_date+'&dcEntryId='+data[2][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[2][incre].site_id+'" style="text-decoration: underline;color: blue;"  id="DC22'+siteId+incre+'" onclick="openNewWindowinnewwindow(this.id,\'getDcFormGrnViewDts.spring?invoiceNumber='+data[2][incre].dc_number+'&vendorId='+data[2][incre].vendor_id+'&dcDate='+data[2][incre].dc_recive_date+'&dcEntryId='+data[2][incre].dc_entry_id+'&type=dcGrnPriceMaster&SiteId='+data[2][incre].site_id+'\')">DC_'+data[2][incre].dc_number+'</a>';
										}
									   
										priceMasterTableBody+='</td>';
										priceMasterTableBody+='<td>'+data[2][incre].vendor_name+'</td>';
										priceMasterTableBody+='</tr>';
									}else{
										priceMasterTableBody+='<tr style="height: 30px;"><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td><td>&emsp;</td></tr>';
									}
								}
									 	
							priceMasterTableBody+='</table>';
							priceMasterTableBody+='</td>';
						}
				priceMasterTableBody+='</tr>';
				priceMasterTableBody+='</tbody>';
				priceMasterTableBody+='</table>';


				
				if (thirdMonth == 0 && secondMonth == 0 && firstMonth== 0) {
				//	$("#"+siteId+"priceMasterTableBody"+siteId).html("No Data available from past months");
					//$("#priceMasterTableHead").html("No Data available from past months");
				}else{
					$("#"+siteId+"priceMasterTableHead"+siteId).append(priceMasterTableHead);
					$("#"+siteId+"priceMasterTableBody"+siteId).append(priceMasterTableBody);
					
				if(getChildProdNameLength<=20){
					$("#txtchildProductName"+siteId).css({
						"width":"250px"
					});
				}else if(getChildProdNameLength<=30){
					$("#txtchildProductName"+siteId).css({
						"width":"300px"
					});
				}else if(getChildProdNameLength<=50){
					$("#txtchildProductName"+siteId).css({
						"width":"350px"
					});
				}else if(getChildProdNameLength<=80){
					$("#txtchildProductName"+siteId).css({
						"width":"400px"
					});
				}else{
					$("#txtchildProductName"+siteId).css({
						"width":"500px"
					});
				} 
				} 
				
				debugger;
				if( $(".checkboxsites:checkbox:checked").length==count){
					 $(".loader-ims").hide();
					 $(".overlay_ims").hide();
				}
				count++;
				}
				 
			});
		
		}
	//}site id checking condition
	});
}
/*hyperlink in new tab*/
function openNewWindowinnewwindow(id,url) {
	 debugger;
	 var childProductNameUrl=document.getElementById(id).getAttribute("href");
	 document.getElementById(id).setAttribute("href","javascript:void(0);");
    window.open(url+"&showHomebtn=false", '_blank', 'width=1500,height=600', 'toolbar=0,location=0,menubar=0');
//   document.getElementById(id).setAttribute("href",childProductNameUrl);
     /*  popup.document.write('<html><head><title>Sumadhura-IMS</title><link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg"><meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1"><link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css"><style>.tbl{border:1px solid #000;}.tbl thead tr th{border:1px solid #000;}#tblnotification thead tr th{background-color:#ccc;border:1px solid #000;text-align:center;}</style></head>'+
				'<body id="printPriceMasterData" style="margin:25px;">'+divToPrint.innerHTML+'</body></html>');  */
  //   return false;
	}


