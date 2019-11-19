//debugger;
var screenwidth=screen.width;
//alert("screenwidth: "+screenwidth);
var pageURl=document.URL;
var requestDate=pageURl.split("requestDate=")[1];
//console.log(requestDate);
var sitenames='';
var url = "http://129.154.74.18/Sumadhura/getAllSitesList.spring?requestDate="+requestDate+"";
	$.ajax({
		url : url,
		type : "POST",
		dataType : "json",	
		success : function(response) {
			//console.log("response: "+JSON.stringify(response.XML.SITE));
			var sitedata=response.XML.SITE;
			//console.log("sitename: "+sitedata.SITENAME);
			var sitedatalength=response.XML.SITE.length;
			for(var i=0;i<sitedatalength;i++){
				sitenames+='<div class="card"><span class="project1Anchor" onclick="loadsitedataTable('+sitedata[i].SITEID+')">'+sitedata[i].SITENAME+'</span> <div class="loader" id="loader'+sitedata[i].SITEID+'" style="display:none"></div></div><input type="hidden" id="'+sitedata[i].SITENAME+'" value="'+sitedata[i].SITENAME+'">';
				sitenames+='<div class="sitedetailstable" id="'+sitedata[i].SITEID+'toggle" style="display:none;">';
				sitenames+='<div class="sitedetailstable hidden-xs hidden-sm" id="'+sitedata[i].SITEID+'"></div>';
				sitenames+='<div class="sitedetailstable hidden-xs hidden-sm" id="'+sitedata[i].SITEID+'1" ></div>';
				sitenames+='<div class="sitedetailstable hidden-md hidden-lg" id="Mobile'+sitedata[i].SITEID+'" style="overflow:hidden;"></div>';
				sitenames+='<div class="sitedetailstable hidden-md hidden-lg" id="Mobile'+sitedata[i].SITEID+'1" style="overflow:hidden;"></div>';
				sitenames+='</div>';
			}			
			$("#sitedatadetails").append(sitenames);			
			//$("#sitedatadetailsMobile").append(sitenames);
		} 
	});	
//site details table
function loadsitedataTable(siteId){
	$("#loader"+siteId).show();
	$("#"+siteId+"toggle").toggle();
	var url = "http://129.154.74.18/Sumadhura/TotalReceiveIssueAmount.spring?siteId="+siteId+"&reqDate="+requestDate+"";
	$.ajax({
		url : url,
		type : "POST",
		dataType : "json",
		// contentType:"xml",
		success : function(response) {
			//console.log("response: "+JSON.stringify(response.XML));
			
			var sitedetailsdata1=response.XML.INWARDS;
			console.log("data: "+JSON.stringify(sitedetailsdata1));
			
			var sitedetailsdataINWARD1=response.XML.ISSUEDTOTALAMOUNT;
			//console.log("data: "+sitedetailsdata1.INWARD);
			var sitedetailsdata2=response.XML.ISSUES;
			console.log("data: "+JSON.stringify(sitedetailsdata2));
			  debugger;
			  var MobileViewTable1='';
			  var MobileViewTable2='';
			    
			
			//Inwards  table						
			var sitedetailstable1='<div class="clearfix"></div>';
			   sitedetailstable1+='<div class="text-center"><h4>Inwards Details</h4></div>';
			   sitedetailstable1+='<div class="table-responsive tbltopbottom project1AnchorTbl">';
			   sitedetailstable1+='<table id="tblnotification" class="table table-bordered table-striped tblBckcls">';
			   sitedetailstable1+='<thead>';
			   sitedetailstable1+='<tr>';
			   sitedetailstable1+='<th>S.No</th>';
			   sitedetailstable1+='<th>Invoice / DC Number</th>';
			   sitedetailstable1+='<th style="width:90px;">Invoice Date</th>';
			   sitedetailstable1+='<th>Type Of Purchase</th>';
			   sitedetailstable1+='<th>PO Number</th>';
			   sitedetailstable1+='<th style="width:90px;">PO Date</th>';
			   sitedetailstable1+='<th>Vendor Name</th>';
			 
			   sitedetailstable1+='<th>Desc. Of Materials </th>';
			   sitedetailstable1+='<th>Invoice Amount</th>';
			   sitedetailstable1+='</tr>';
			   sitedetailstable1+='</thead>';
			   sitedetailstable1+='<tbody>';  
//			   debugger;
//			   console.log(sitedetailsdata1.INWARD);
			 //for no data
			   if(sitedetailsdata1.INWARD==undefined || sitedetailsdata1.INWARD==null  ){
				   //hidden-xs hidden-sm
				   	if(screenwidth>1023){   					   
				    sitedetailstable1+='<tr class=" ">';
					sitedetailstable1+='<td colspan="9" class="text-center">Material inwards are not found.</td>';					
					sitedetailstable1+='</tr>';
					}else{
					 MobileViewTable2+='<div class="text-center"><h4>Inwards Details</h4></div>';
					 MobileViewTable2+='<table  class="table table-bordered table-striped">';	           
				     MobileViewTable2+='<tbody>';
					 MobileViewTable2+='<tr>';					
					 MobileViewTable2+='<td colspan="2" class="text-center">Material inwards are not found.</td>';
					 MobileViewTable2+='</tr>';
					}
			   }
			   //for single inwards data
			   
			   else if(sitedetailsdata1.INWARD.INVOICEAMOUNT!=null){					    
				    if(screenwidth>1023){  
				    sitedetailstable1+='<tr>'; 
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD.SERIALNO+'</td>'; 
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD.INVOICENO_DCNO+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD.INVOICEDATE+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD.TYPEOFPURCHASE+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD.PONUMBER+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD.PODATE+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD.VENDORNAME+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD.MATERIALNAME+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD.INVOICEAMOUNT+'</td>';
				    
					//hidden-xs hidden-sm
					sitedetailstable1+='<tr class=" ">';
					sitedetailstable1+='<td colspan="8" class="totalAmountcls"><strong>Total Amount</strong></td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARDSGRANDTOTAL+'</td>';
					sitedetailstable1+='</tr>';
					sitedetailstable1+='<tr>'; 		
				    }
				    else{
				    	 MobileViewTable2+='<div class="text-center"><h4>Inwards Details</h4></div>';
						    MobileViewTable2+='<table  class="table table-bordered table-striped">';	           
						    MobileViewTable2+='<tbody>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>S.No</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD.SERIALNO+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Invoice / DC Number</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD.INVOICENO_DCNO+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Invoice Date</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD.INVOICEDATE+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Type Of Purchase</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD.TYPEOFPURCHASE+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>PO Number</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD.PONUMBER+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>PO Date</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD.PODATE+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Vendor Name</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD.VENDORNAME+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Desc. Of Materials</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD.MATERIALNAME+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Invoice Amount</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD.INVOICEAMOUNT+'</td>';
						    MobileViewTable2+='</tr>';
						   
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td colspan="2" class="text-center"><strong>Total Amount: '+sitedetailsdata1.INWARDSGRANDTOTAL+'</strong></td>';
//						    MobileViewTable2+='<td>'+sitedetailsdata1.INWARD.INVOICEAMOUNT+'</td>';
						    MobileViewTable2+='</tr>';
				    }
			   }
			 //for multiple inwards data
			   else{
				   MobileViewTable2+='<div class="text-center"><h4>Inwards Details</h4></div>';
			 
			 for(var j=0;j<Object.keys(sitedetailsdata1.INWARD).length;j++)
					{
				    
				    if(screenwidth>1023){  
					sitedetailstable1+='<tr>'; 
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD[j].SERIALNO+'</td>'; 
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD[j].INVOICENO_DCNO+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD[j].INVOICEDATE+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD[j].TYPEOFPURCHASE+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD[j].PONUMBER+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD[j].PODATE+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD[j].VENDORNAME+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD[j].MATERIALNAME+'</td>';
					sitedetailstable1+='<td>'+sitedetailsdata1.INWARD[j].INVOICEAMOUNT+'</td>';
					sitedetailstable1+='<tr>'; 		
				    }
				    else{
				    	   MobileViewTable2+='<table  class="table table-bordered table-striped">';	           
						    MobileViewTable2+='<tbody>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td style="width:31%;">S.No</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD[j].SERIALNO+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td class="storedailyreportth">Invoice / DC Number</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD[j].INVOICENO_DCNO+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Invoice Date</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD[j].INVOICEDATE+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Type Of Purchase</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD[j].TYPEOFPURCHASE+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>PO Number</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD[j].PONUMBER+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>PO Date</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD[j].PODATE+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Vendor Name</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD[j].VENDORNAME+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Desc. Of Materials</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD[j].MATERIALNAME+'</td>';
						    MobileViewTable2+='</tr>';
						    MobileViewTable2+='<tr>';
						    MobileViewTable2+='<td>Invoice Amount</td>';
						    MobileViewTable2+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata1.INWARD[j].INVOICEAMOUNT+'</td>';
						    MobileViewTable2+='</tr>';
				    }
					
				}
			 
			 
			 //hidden-xs hidden-sm
			 MobileViewTable2+='<tr>';
			 MobileViewTable2+='<td colspan="2" class="text-center"><strong>Total Amount: '+sitedetailsdata1.INWARDSGRANDTOTAL+'</strong></td>';
//			    MobileViewTable2+='<td>'+sitedetailsdata1.INWARD.INVOICEAMOUNT+'</td>';
			 MobileViewTable2+='</tr>';	
			 
			 
				sitedetailstable1+='<tr class=" ">';
				sitedetailstable1+='<td colspan="8" class="totalAmountcls"><strong>Total Amount</strong></td>';
				sitedetailstable1+='<td>'+sitedetailsdata1.INWARDSGRANDTOTAL+'</td>';
				sitedetailstable1+='</tr>';
			   }
			    sitedetailstable1+='</tbody>';
			    sitedetailstable1+='</table>'; 
			    $("#loader"+siteId).hide();
//			    $('.datatable').DataTable();
//			    $('.datatable').stacktable({myClass:'stacktable small-only'}); 
			  $("#"+siteId).html(sitedetailstable1);
			  $("#Mobile"+siteId).html(MobileViewTable2);
			  
			 //issue table
			  var sitedetailstable2='<div class="clearfix"></div>';
			  sitedetailstable2+='<div  class="text-center"><h4>Issue Details</h4></div>';
			  sitedetailstable2+='<div  class="table-responsive tbltopbottom project1AnchorTbl">';
			  sitedetailstable2+='<table id="tblnotification" class="table table-bordered table-striped tblBckcls">';
			  sitedetailstable2+='<thead>';
			  sitedetailstable2+='<tr>';
			  sitedetailstable2+='<th>S.No</th>';
			  sitedetailstable2+='<th>Issued to</th>';
			  sitedetailstable2+='<th>No.of Slips</th>';
			  sitedetailstable2+='<th>Type of Issue</th>';
			  sitedetailstable2+='<th>Amount</th>';
			  sitedetailstable2+='</tr>';
			  sitedetailstable2+='</thead>';
			  sitedetailstable2+='<tbody>';
			  debugger;
			  //console.log(sitedetailsdata2);
			  console.log(sitedetailsdata2.ISSUE);
			//for no issue data
			 /* $("#Mobile"+siteId).html('');*/
			
			  if(sitedetailsdata2.ISSUE==undefined || sitedetailsdata2.ISSUE==null){
				  //hidden-xs hidden-sm
				  if(screenwidth>1023){ 
					  sitedetailstable2+='<tr class="">';
					  sitedetailstable2+='<td colspan="5" class="text-center">Material issues are not found.</td>';
					  sitedetailstable2+='</tr>';
				  }
				  else{
				
				    MobileViewTable1+='<div  class="text-center"><h4>Issue Details</h4></div>';
				    MobileViewTable1+='<table  class="table table-bordered table-striped">';	           
				    MobileViewTable1+='<tbody>';
				    MobileViewTable1+='<tr>';
				    MobileViewTable1+='<td colspan="2" class="text-center">Material issues are not found.</td>';				    
				    MobileViewTable1+='</tr>';
					  
				  }
			  }
			//for single issue data
			  else if(sitedetailsdata2.ISSUE.ISSUEAMOUNT!=null){				   
				    //for web	
				    if(screenwidth>1023){ 
				    sitedetailstable2+='<tr>'; 
					sitedetailstable2+='<td>'+1+'</td>'; 
					sitedetailstable2+='<td>'+sitedetailsdata2.ISSUE.ISSUEDTO+'</td>';
					sitedetailstable2+='<td>'+sitedetailsdata2.ISSUE.TOTALNOFSLIPS+'</td>';
					sitedetailstable2+='<td>'+sitedetailsdata2.ISSUE.TYPEOFISSUE+'</td>';
					sitedetailstable2+='<td>'+sitedetailsdata2.ISSUE.ISSUEAMOUNT+'</td>';
					sitedetailstable2+='</tr>'; 
					//hidden-xs hidden-sm
					
					sitedetailstable2+='<tr class="">';
					sitedetailstable2+='<td colspan="4" class="totalAmountcls"><strong>Total Amount</strong></td>';
					sitedetailstable2+='<td>'+sitedetailsdata2.ISSUEDTOTALAMOUNT+'</td>';
					sitedetailstable2+='</tr>';
				    }
				    else{
				    	//for mobile
					    MobileViewTable1+='<div  class="text-center"><h4>Issue Details</h4></div>';
					    MobileViewTable1+='<table  class="table table-bordered table-striped">';	           
					    MobileViewTable1+='<tbody>';
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td>S.No</td>';
					    MobileViewTable1+='<td style="width:69%;word-break:break-all;">1</td>';
					    MobileViewTable1+='</tr>';
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td>Issued to</td>';
					    MobileViewTable1+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata2.ISSUE.ISSUEDTO+'</td>';
					    MobileViewTable1+='</tr>';
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td>	No.of Slips</td>';
					    MobileViewTable1+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata2.ISSUE.TOTALNOFSLIPS+'</td>';
					    MobileViewTable1+='</tr>';
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td>Type OF Issue</td>';
					    MobileViewTable1+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata2.ISSUE.TYPEOFISSUE+'</td>';
					    MobileViewTable1+='</tr>';
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td>Amount</td>';
					    MobileViewTable1+='<td>'+sitedetailsdata2.ISSUE.ISSUEAMOUNT+'</td>';
					    MobileViewTable1+='</tr>';				    
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td class="text-center" colspan="2"><strong>Total Amount: '+sitedetailsdata2.ISSUEDTOTALAMOUNT+'</strong></td>';
					   // MobileViewTable1+='<td>'+sitedetailsdata2.ISSUEDTOTALAMOUNT+'</td>';
					    MobileViewTable1+='</tr>';
				    }
			  }
			  //for multiple issue data
			  else{			  
				  MobileViewTable1+='<div  class="text-center"><h4>Issue Details</h4></div>';
				  for(var j=0;j<Object.keys(sitedetailsdata2.ISSUE).length;j++)
					{	if(screenwidth>1023){
					    sitedetailstable2+='<tr>'; 
						sitedetailstable2+='<td>'+[j+1]+'</td>'; 
						sitedetailstable2+='<td>'+sitedetailsdata2.ISSUE[j].ISSUEDTO+'</td>';
						sitedetailstable2+='<td>'+sitedetailsdata2.ISSUE[j].TOTALNOFSLIPS+'</td>';
						sitedetailstable2+='<td>'+sitedetailsdata2.ISSUE[j].TYPEOFISSUE+'</td>';
						sitedetailstable2+='<td>'+sitedetailsdata2.ISSUE[j].ISSUEAMOUNT+'</td>';
					}
					else{
						MobileViewTable1+='<table  class="table table-bordered table-striped">';	           
					    MobileViewTable1+='<tbody>';
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td>S.No</td>';
					    MobileViewTable1+='<td style="width:69%;word-break:break-all;">'+[j+1]+'</td>';
					    MobileViewTable1+='</tr>';
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td>Issued to</td>';
					    MobileViewTable1+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata2.ISSUE[j].ISSUEDTO+'</td>';
					    MobileViewTable1+='</tr>';
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td>	No.of Slips</td>';
					    MobileViewTable1+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata2.ISSUE[j].TOTALNOFSLIPS+'</td>';
					    MobileViewTable1+='</tr>';
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td>Type of Issue</td>';
					    MobileViewTable1+='<td style="width:69%;word-break:break-all;">'+sitedetailsdata2.ISSUE[j].TYPEOFISSUE+'</td>';
					    MobileViewTable1+='</tr>';
					    MobileViewTable1+='<tr>';
					    MobileViewTable1+='<td>Amount</td>';
					    MobileViewTable1+='<td>'+sitedetailsdata2.ISSUE[j].ISSUEAMOUNT+'</td>';
					    MobileViewTable1+='</tr>';	
					}
					   	
					}
				  //hidden-xs hidden-sm
			  sitedetailstable2+='<tr class="">';
			  sitedetailstable2+='<td colspan="4" class="totalAmountcls"><strong>Total Amount</strong></td>';
			  sitedetailstable2+='<td>'+sitedetailsdata2.ISSUEDTOTALAMOUNT+'</td>';
			  sitedetailstable2+='</tr>';
			  MobileViewTable1+='<tr>';
			  MobileViewTable1+='<td class="text-center" colspan="2"><strong>Total Amount: '+sitedetailsdata2.ISSUEDTOTALAMOUNT+'</strong></td>';
			   // MobileViewTable1+='<td>'+sitedetailsdata2.ISSUEDTOTALAMOUNT+'</td>';
			  MobileViewTable1+='</tr>';	
			  }
			  sitedetailstable2+='</tbody>';
			  sitedetailstable2+='</table>'; 
			  MobileViewTable1+='</tbody>';
			  MobileViewTable1+='</table>';	
			  $("#"+siteId+"1").html(sitedetailstable2);
			  debugger;
			  $("#Mobile"+siteId+"1").html(MobileViewTable1);  
			
		}	
		});
	
	$('#tblnotification').stacktable({myClass:'stacktable small-only'});
}