

//append location row written by ashok
function addLocationRow(btn){
	debugger;
	 //validating duration table
	var validateFieldlocationTableRow=validateFieldlocationRow();
	if(validateFieldlocationTableRow==false){
		return false;
	}
	var rowLCount = btn+1;
	var rowCount=$(".location").length;
	console.log(rowCount);
	var rowNew = "<tr  id='location"+rowLCount+"' class='location'><td style='width:50px;'>"+rowLCount+"</td><td>" +
			"<select class='form-control' id='locationChildProduct"+rowLCount+"' onchange='addLocation("+rowLCount+")' name='locationChildProduct"+rowLCount+"' >" +
			/*"<option>Acid</option>" +
			"<option>Air Spray</option><option>Mobile Phones</option>" +*/
			"</select>" +
			"</td><td><input type='hidden' placeholder='' class='form-control' id='location_Id"+rowLCount+"' name='location_Id"+rowLCount+"'/><select placeholder='' class='form-control' id='location_mar"+rowLCount+"' name='location_mar"+rowLCount+"'><option>--Select---</option></select></td><td><div class='input-group'><input type='text' placeholder='Select from date' class='form-control readonly-color' id='from_date_location"+rowLCount+"'onchange='fromDateChange("+rowLCount+")' name='from_date_location"+rowLCount+"' autocomplete='off' readonly='true'/><label class='input-group-addon btn input-group-addon-border' for='from_date_location"+rowLCount+"'><span class='fa fa-calendar'></span></label></div></td><td><div class='input-group'><input type='text' placeholder='Select to date' class='form-control readonly-color' onchange='toDateChange("+rowLCount+")' id='to_date_location"+rowLCount+"' name='to_date_location"+rowLCount+"' autocomplete='off' readonly='true'/><label class='input-group-addon btn input-group-addon-border' for='to_date_location"+rowLCount+"'><span class='fa fa-calendar'></span></label></div></td><td><div class='input-group'><input type='text'  class='form-control readonly-color' id='timepicker"+rowLCount+"' style='z-index:0' name='timepicker"+rowLCount+"' placeholder='Select time' autocomplete='off' readonly='true'/><label class='input-group-addon btn input-group-addon-border' for='timepicker"+rowLCount+"'><span class='fa fa-clock-o'></span></label></div></td><td><input type='text'  class='form-control pasteDisable' id='locationQuantity"+rowLCount+"' onkeyup='calculateLocationAmount("+rowLCount+")' name='locationQuantity"+rowLCount+"' placeholder='Please Enter Quantity' autocomplete='off'/></td><td><select class='form-control pasteDisable' id='site_Name"+rowLCount+"' name='site_Name"+rowLCount+"'></select></td><td><input type='text'   class='form-control pasteDisable' id='price_Aftertax"+rowLCount+"' name='price_Aftertax"+rowLCount+"' onkeyup='locationAndDurationTablePricePerUnitAfterTax("+rowLCount+")'  placeholder='Please Enter Price Per Unit After Tax' autocomplete='off'/></td><td><input type='text'   class='form-control' id='total_Amount"+rowLCount+"' name='total_Amount"+rowLCount+"' autocomplete='off'  placeholder='Please Enter Total Amount'  onkeyup='durationTotalAmopuntChange("+rowLCount+")'/></td><td><button type='button' id='addLocationPlusBtn"+rowLCount+"' onclick='addLocationRow("+rowLCount+")' class='btnaction'><i class='fa fa-plus'></i></button><button type='button'  id='addLocationDeleteBtn"+rowLCount+"' onclick='deleteLocationRow("+rowLCount+")' class='btnaction'><i class='fa fa-trash'></i></button></td></tr>";
    $("#FieldLocation_durationtable tbody").append(rowNew);
    $('.pasteDisable').bind('paste', function (e) { e.preventDefault();	});
    var PoId=$("#poToId").val();
	  if(PoId=="SiteWise"){
		  $("#site_Name"+rowLCount).html("<option>"+$("#siteNameId").val()+"</option>");
		  $("#site_Name"+rowLCount).attr("readonly");
	  }
	  if(PoId=="LocationWise"){debugger;
		  var tempOptions="<option value=''>--select--</option>";
		for(var i=0;i<PODATA.length;i++){
			 tempOptions+="<option value='"+PODATA[i]+"'>"+PODATA[i].split("$")[1]+"</option>";
		}  
		 $("#site_Name"+rowLCount).html(tempOptions);
	  }
	  if(PoId=="BrandingWise"){
		  var tempOptions="<option value=''>--select--</option>";
			for(var i=0;i<BrandingData.length;i++){
				 tempOptions+="<option value='"+BrandingData[i]+"'>"+BrandingData[i].split("$")[1]+"</option>";
			}  
			 $("#site_Name"+rowLCount).html(tempOptions);
	  }
    $("#addLocationPlusBtn"+btn).hide();
    $("#addLocationDeleteBtn"+btn).show();
    $("#from_date_location"+rowLCount).datepicker({dateFormat: 'dd-M-y',
		 changeMonth: true,
	      changeYear: true});
    $("#to_date_location"+rowLCount).datepicker({dateFormat: 'dd-M-y',
		 changeMonth: true,
	      changeYear: true});
    $("#timepicker"+rowLCount).timepicker();
    //$("#locationQuantity"+rowLCount).val($("#QuantityId1").val());
    if(rowCount==1){
		$("#addLocationDeleteBtn"+btn).show();
     }
    var childDataLength=childData.length;
	if(childDataLength>=0){debugger;	
	var selectoption="<option>--select--</option>"
	for(var i=0;i<childDataLength;i++){
		selectoption+="<option value='"+childData[i]+"'>"+childData[i].split("$")[1]+"</option>"
	}
	$("#locationChildProduct"+rowLCount).html(selectoption);
	}
	//focusing on last row first coloum
	$("#locationChildProduct"+rowLCount).focus();
}

//delete a row in location and duration table
function deleteLocationRow(btn){
	var CanIDelet=window.confirm("Do you want to delete?");
	if(CanIDelet==false){
		return false;
	}
	var rowCount=$(".location").length;
	//checking row count
	if(rowCount==1){
		alert("This row can't be deleted.");
		return false;
	}	
	//deleting row
    $("#location"+btn).remove();
	 //finding last row id
	   debugger;
	 var lastRowId=$(".location:last").attr("id");
	 var splitId=lastRowId.split("location")[1];
	 if(rowCount==2){
			$("#addLocationDeleteBtn"+splitId).hide();
	 }
	 if(splitId<btn){		
			$("#addLocationPlusBtn"+splitId).show();
	 }	
}
var childData;
function loadLocationChildProduct(){
	debugger;
	childData=[];	
	$(".hiddenchild").each(function(){debugger;
		if($(this).val()!=''){
			childData.push($(this).val());
		}
	});
	/*console.log("childData: "+childData);*/debugger;
	/*var childDataLength=childData.length;
	if(childDataLength>=0){debugger;	
	var selectoption="<option>--select--</option>"
	for(var i=0;i<childDataLength;i++){
		selectoption+="<option value="+childData[i]+">"+childData[i].split("$")[1]+"</option>"
	}
	$("#locationChildProduct1").html(selectoption);
	}*/
}

function openLocationAndDurationTable(){
	    var VendorNameId=$("#VendorNameId").val();
	    var GSTINNumber=$("#GSTINNumber").val();
		var VendorAddress=$("#VendorAddress").val();
		var selectPoTo = $("#poToId").val();
		var siteNameIdsitewise = $("#siteNameId").val();
		var siteNameLocationwise = $("#site").val();
		if(VendorNameId==""){
			alert("Please enter vendor name.");
			$("#VendorNameId").focus();
			return false;
		} 
		if(GSTINNumber=="" || VendorAddress==''){
			alert("Please enter valid vendor name.");
			$("#VendorNameId").focus();
			return false;
		} 
		if(selectPoTo == "select"){
			alert("Please select PO To");
			$("#poToId").focus();
			return false;
		}
		if(selectPoTo == "SiteWise"){
			if(siteNameIdsitewise == ""){
				alert("Please select site")
				$("#siteNameId").focus();
				return false;
			}
		}
		if(selectPoTo == "LocationWise"){
			if(siteNameLocationwise == ""){
				alert("Please select location")
				$("#site").focus();
				return false;
			}
		}
		//validating product table
	var validateProductTableRow=validateProductRow();
	if(validateProductTableRow==false){
		return false;
	}
	//validating charges table
	var validateChargesTableRow=validateChargesRow();
	if(validateChargesTableRow==false){
		return false;
	}
	//calculation 
	calculateOtherCharges();
	 if($('.table_hide_show').css('display') == 'block'){
		 $('.table_hide_show').hide();
		}else{
			 $('.table_hide_show').show();
		}
	  var childDataLength=childData.length;
  	if(childDataLength>=0){debugger;	
  	var selectoption="<option>--select--</option>"
  	for(var i=0;i<childDataLength;i++){
  		selectoption+="<option value='"+childData[i]+"'>"+childData[i].split("$")[1]+"</option>"
  	}
  	$("#locationChildProduct1").html(selectoption);
  	}
  	$("#locationChildProduct1").focus();
}

//calculating location total amount by changing price 
function locationAndDurationTablePricePerUnitAfterTax(id){debugger;
	//checking number or not
	if(isNaN($("#price_Aftertax"+id).val())){
		alert("Please enter valid data.");
		$("#price_Aftertax"+id).val("");
		$("#price_Aftertax"+id).focus();
		return false;
	}
	var locationChildProduct=$("#locationChildProduct"+id).val().split("$")[0];
	var price_Aftertax=parseFloat($("#price_Aftertax"+id).val()==''?0:$("#price_Aftertax"+id).val());
	var locationQuantity=parseFloat($("#locationQuantity"+id).val()==''?0:$("#locationQuantity"+id).val());
	var durationTotalAmount=price_Aftertax*locationQuantity;
    $("#total_Amount"+id).val(durationTotalAmount.toFixed(2));
	$(".hiddenchild").each(function(){debugger;
	if($(this).val().split("$")[0]==locationChildProduct){debugger;
	var productChildID=$(this).val().split("$")[0];
		 var splitId1=$(this).attr("id").split("comboboxsubSubProd")[1];
		 var productTotalAmount=parseFloat($("#TotalAmountId"+splitId1).val());
		 var locationTotalAmount=0;
		 $(".location").each(function(){
			var splitId=$(this).attr("id").split("location")[1];
			if(productChildID==$("#locationChildProduct"+splitId).val().split("$")[0]){
				locationTotalAmount+=parseFloat($("#total_Amount"+splitId).val());
			}
		 })
		 if(locationTotalAmount>productTotalAmount){
			 $("#total_Amount"+id).val("0");
			 $("#price_Aftertax"+id).val("0");
			 alert("You can't initiate more than invoice amount.")
			 return false;
		 }
	  }
	});
	
	
}

//duration table total Amount Change
function durationTotalAmopuntChange(id){
	//checking number or not
	if(isNaN($("#total_Amount"+id).val())){
		alert("Please enter valid data.");
		$("#total_Amount"+id).val("");
		$("#total_Amount"+id).focus();
		return false;
	}
	var locationChildProduct=$("#locationChildProduct"+id).val().split("$")[0];
	var total_Amount=parseFloat($("#total_Amount"+id).val()==''?0:$("#total_Amount"+id).val());
	var locationQuantity=parseFloat($("#locationQuantity"+id).val()==''?0:$("#locationQuantity"+id).val());
	var durationTotalAmount=total_Amount/locationQuantity;
	if($("#locationQuantity"+id).val()=='' || $("#locationQuantity"+id).val()=='0'){
		durationTotalAmount=0;
	}
    $("#price_Aftertax"+id).val(durationTotalAmount.toFixed(2));
	$(".hiddenchild").each(function(){debugger;
	if($(this).val().split("$")[0]==locationChildProduct){debugger;
	var productChildID=$(this).val().split("$")[0];
		 var splitId1=$(this).attr("id").split("comboboxsubSubProd")[1];
		 var productTotalAmount=parseFloat($("#TotalAmountId"+splitId1).val());
		 var locationTotalAmount=0;
		 $(".location").each(function(){
			var splitId=$(this).attr("id").split("location")[1];
			if(productChildID==$("#locationChildProduct"+splitId).val().split("$")[0]){
				locationTotalAmount+=parseFloat($("#total_Amount"+splitId).val());
			}
		 })
		 if(locationTotalAmount>productTotalAmount){
			 $("#total_Amount"+id).val("0");
			 $("#price_Aftertax"+id).val("0");
			 alert("You can't initiate more than invoice amount.")
			 return false;
		 }
	  }
	});
}

//checking invoice amount with duration table amount based on child product
function validateInvoiceAMountWithDurationTable(){
	var error=true;
	$(".hiddenchild").each(function(){debugger;
	     var productChildID=$(this).val().split("$")[0];
		 var splitId1=$(this).attr("id").split("comboboxsubSubProd")[1];
		 var productTotalAmount=parseFloat($("#TotalAmountId"+splitId1).val());
		 var productTotalQty=parseFloat($("#QuantityId"+splitId1).val());
		 var locationTotalAmount=0;
		 var chilQtyVal=0;
		 $(".location").each(function(){
			var splitId=$(this).attr("id").split("location")[1];
			if(productChildID==$("#locationChildProduct"+splitId).val().split("$")[0]){
				locationTotalAmount+=parseFloat($("#total_Amount"+splitId).val());
				chilQtyVal+=parseFloat($("#locationQuantity"+splitId).val());
			}
		 })
		 if(chilQtyVal>productTotalQty){
			 alert("You can't initiate more than childproduct quantity in Field Location & Duration Details table.");
			 return error=false;
		 }
		 if(locationTotalAmount>productTotalAmount){
			 alert("You can't initiate more than invoice amount.")
			 return error=false;
		 }
	  
	});
	return error;
}




(function( $ ) {
    $.widget( "custom.combobox", {
  _create: function() {
    this.wrapper = $( "<span>" )
    .addClass( "custom-combobox" )
        .insertAfter( this.element );
 
        this.element.hide();
        this._createAutocomplete();
        this._createShowAllButton();
      },
 	
      _createAutocomplete: function() {
        var selected = this.element.children( ":selected" ),
      value = selected.val() ? selected.text() : "";
    this.input = $( "<input>" )
      .appendTo( this.wrapper )
      .val( value )
      .attr("title", "")
      .attr("id", this.element[0].name)
      //Enable below line to get input box background color - Start
      //.addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
      //Enable below line to get input box background color - End
      .autocomplete({
        delay: 0,
        minLength: 0,
        source: $.proxy( this, "_source" )
      })
      .tooltip({
        tooltipClass: "ui-state-highlight"
          });
 
        this._on( this.input, {
          autocompleteselect: function( event, ui ) {
            ui.item.option.selected = true;
        
        var prodId = "";
        var prodName = "";        
        prodId = ui.item.option.value;
        prodName = ui.item.value;        
        var ele = this.element[0].name;        
        var rowNum = ele.match(/\d+/g);
        
        //Removing numbers from the header names
        var str1 = ele.replace(/[0-9]/g, '');
        
        if(str1 == "Product") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
            loadSubProds(prodId, rowNum);
            this._trigger( "select", event, {
                item: ui.item.option
              });
        }            
        else if(str1 == "SubProduct") {
        	prodId = ui.item.option.value;
            prodName = ui.item.value;
            loadSubSubProducts(prodId, rowNum);
            this._trigger( "select", event, {
                item: ui.item.option
              });       
        }
        else if(str1 == "ChildProduct") {
            $("#ChildProduct"+rowNum).addClass("ChildProduct");
        	prodId = ui.item.option.value;
            prodName = ui.item.value;        
        	var tablelength=$("#doInventoryTableId > tbody > tr").length;
          	if(tablelength==1){
          		 this._trigger( "select", event, {
                       item: ui.item.option
                     });
          		 loadUnits(prodId, rowNum);  
          		 loadLocationChildProduct();
          	}
          	else{
          		var childstatus=childcampare(prodName, rowNum);
          		if(childstatus==true){
                  	this._trigger( "select", event, {
                          item: ui.item.option
                        }); 
                  	 loadUnits(prodId, rowNum); 
                  	loadLocationChildProduct();
                  }
                  else{
                  	 //loadUnits(prodId, rowNum); 
                  	$("#ChildProduct"+rowNum).val('');
                  	var emptychild=$("#ChildProduct"+rowNum).val();
                  	loadUnits(emptychild, rowNum); 
                  	return false;
                  }
          	}   
          	//loadLocationChildProduct();
        }
        
      },
      autocompletechange: "_removeIfInvalid"
    });
  },      
  
  //Enable below code to create Show All Button - Start
    _createShowAllButton: function() {
        var input = this.input,
          wasOpen = false;
 
        $( "<a>" )
      .attr( "tabIndex", -1 )
      .attr( "title", "Show All Items" )
      .tooltip()
      .appendTo( this.wrapper )
      .button({
        icons: {
          primary: "ui-icon-triangle-1-s"
        },
        text: false
      })
      .removeClass( "ui-corner-all" )
      .addClass( "custom-combobox-toggle ui-corner-right" )
      .mousedown(function() {
        wasOpen = input.autocomplete( "widget" ).is( ":visible" );
      })
      .click(function() {
        input.focus(); 
        // Close if already visible
        if ( wasOpen ) {
          return;
        } 
        // Pass empty string as value to search for, displaying all results
        input.autocomplete( "search", "" );
      });
 },  
 //Enable below code to create Show All Button - End 
  _source: function( request, response ) {
    var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
    response( this.element.children( "option" ).map(function() {
      var text = $( this ).text();
      if ( this.value && ( !request.term || matcher.test(text) ) )
        return {
          label: text,
          value: text,
          option: this
        };
    }) );
  }, 
  _removeIfInvalid: function( event, ui ) { 
    // Selected an item, nothing to do
    if ( ui.item ) {
      return;
    } 
    // Search for a match (case-insensitive)
    var value = this.input.val(),
      valueLowerCase = value.toLowerCase(),
      valid = false;
    this.element.children( "option" ).each(function() {
          if ( $( this ).text().toLowerCase() === valueLowerCase ) {
            this.selected = valid = true;
            return false;
          }
        });
 
        // Found a match, nothing to do
    if ( valid ) {
      return;
    }        
    // Remove invalid value
    this.input
      .val( "" )
      .attr( "title", value + " didn't match any item" )
      .tooltip( "open" );
    this.element.val( "" );
    this._delay(function() {
      this.input.tooltip( "close" ).attr( "title", "" );
    }, 2500 );
    //this.input.autocomplete( "instance" ).term = "";
      }, 
      _destroy: function() {
        this.wrapper.remove();
        this.element.show();
      }      
    });
  })( jQuery ); 

  $(function() {	  
    $("#combobox1").combobox();    
    $( "#toggle").click(function() {
  $( "#combobox1").toggle();
    });
  });
  
  $(function() {
	$( "#comboboxsubProd1").combobox();	
  });
  
  $(function() {
	$("#comboboxsubSubProd1").combobox();
  });
  
  $(function() {
	  $("#InvoiceDateId").datepicker({ 
		  dateFormat: 'dd-M-y',
		  maxDate: new Date(),
	  changeMonth: true,
      changeYear: true
   //   showButtonPanel: true,
   // maxDate: '@maxDate',
   //   minDate: '@minDate'  
	  });
  });
  
  $(function() {
	  $("#expireDateId1").datepicker({
		  dateFormat: 'dd-M-y',
		  minDate:0,
		  changeMonth: true,
	      changeYear: true
	  });
  });
  $(function() {
	  $("#poDateId").datepicker({
		  
		 dateFormat: 'dd-M-y',
		 maxDate: new Date(),
		  changeMonth: true,
	      changeYear: true
	  });
  });
  $(function() {
	  var staffId=$("#hiddenPODate").val();
	  
	  $("#receivedDate").datepicker({
		  dateFormat: 'dd-M-y',
		  minDate: '-'+ staffId+'d',
		 maxDate: new Date() ,
		 onSelect: function(dateText) {
		    // alert("Selected date: " + dateText + "; input's current value: " + this.value);
		 	
		 $("#saveBtnId").disabled = true;
		 var invoiceId=$("#InvoiceNumberId").val();
		 var vendname=$("#vendorIdId").val();
		 var recedate=dateText;
		 var vName = "VND03";
	/*	 var requestData = {
				 vendorId: invoiceId,
				 venName: vendname,
				 receiDate: recedate
		 };*/
		// var url = "loadAndSetVendorInfo.spring?vendName="+vName;
		 var url = "getReceiveCount.spring?invoiceNumber="+invoiceId+"&vendorname="+vendname+"&receiveDate="+recedate;
		 $.ajax({
		  url : url,
		  //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
		  type : "post",
		  contentType : "application/json",
		  success : function(data) {
			 	 if (data>0){
					 //server sent response as false, not valid data
					$("#errorMessageInvoiceNumber").show();
					 $("#InvoiceNumberId").focus();
					 $('.btn-visibilty').attr('disabled', 'disabled');
	  				    $('.btn-visibilty1').closest('td').find('input').attr('disabled', 'disabled');
	 				    $('.btn-visibilty1').closest('td').find('.custom-combobox-toggle').addClass('hide');
					 $("#doInventoryTableId").find("input,button,textarea,select").attr("disabled", "disabled");
					//$("#doInventoryTableId").find("*").attr("disabled", "disabled");
					 $("#saveBtnId").disabled = true;
					 
					 //hide error message
					// $("#errorMessageInvoiceNumber").hide();
				 }
		  },
		  error:  function(vName, status, er){
			  alert(data+"_"+status+"_"+er);
			  }
		  });
		 
		 
	}
	  });
  });

 /* ======================================================SiteWise details start=========================================================================*/
  function populateSite(id) {debugger;
  var siteName=$("#siteNameId").val();
  var url = "loadAndSetSiteNameInfo.spring?siteName="+siteName;

   $.ajax({
   url : url,
   //url : "${pageContext.request.contextPath}/getVendorDetails.spring",
   type : "get",
   Cdata : "",
   contentType : "application/json",
   success : function(data) {
 	  console.log(data);
 	  Cdata = data.split("@@");
 	  
 	  var resultArray = [];
 	  for(var i=0;i<Cdata.length;i++){
 	      resultArray.push(Cdata[i].split("@@")[0]);
 	  }
 	  $("#siteNameId").autocomplete({
 	  		source : resultArray,
 	  		change: function (event, ui) {
                 if(!ui.item){
                	//if list item not selected then make the text box null	
                	 $("#siteNameId").val("");
                 }
               }/*,
 	  		select: function (event, ui) {
                 AutoCompleteSelectHandler(event, ui);
             }*/

 	  	});
   },
   error:  function(data, status, er){
 	 // alert(data+"_"+status+"_"+er);
 	  }
   });

 //code for selected text
 	function AutoCompleteSelectHandler(event, ui)
 	{/*               
 	    var selectedObj = ui.item;       
 	    isTextSelect="true";
 	  
 	  var contractorName=selectedObj.value;
 	 
 		 var url = "loadAndSetVendorInfoForWO.spring";
 		 $.ajax({
 			  url : url,
 			  type : "get",
 			 data:{
 				 contractorName:contractorName,
 				 loadcontractorData:true	 
 			 },
 			  contentType : "application/json",
 			  success : function(data) {
 				  $("#contractorName").val(contractorName);
 				  if(data!=""||data!="null"){
 debugger;
 					var contractorData=data[0].split("@@");
 					var contractorId=contractorData[0];
 					
 					$("#contractorId").val(contractorId);
 					var str="<option value='0'>Select Work Order No</option>";
 					var siteid=$("#SiteId").val();
 					$.ajax({
 						url:"getWorkOrderNo.spring?workDescId=0&siteId="+siteid+"&contractorId="+contractorId+"&typeOfWork=PW",
 						type : "POST",
 						contentType : "application/json",
 						success:function(resp){
 							debugger;	
 							var array=new Array();
 							array=resp.split("|");
 							for (var i = 0; i < array.length; i++) {
 								var array_element = array[i].split("_");
 								if(array_element!="")
 								str+="<option value="+array_element[0]+">"+array_element[0]+"</option>";
 								
 							}
 							$("#workOrderNo").html(str);
 						
 						}
 					});
 					
 					
 						 $.ajax({
 							  url : "getWorkOrderNo.spring?workDescId=0&siteId="+siteid+"&contractorId="+contractorId,
 							  type : "get",
 							
 							  contentType : "application/json",
 							  success : function(data1) {
 								  debugger;
 								  for(var j=0; j<data1.length; j++) {
 							    		available[j] = data[j];
 							    	}
 							  },
 							  error:  function(err){
 								  alert(err);
 								  }
 							  });
 						
 					}
 			  },
 			  error:  function(data, status, er){
 				  alert(data+"_"+status+"_"+er);
 				  }
 			  });
 		 var available = new Array();
 	    	

 		 
 	*/}

 	
 
 
};
  
	  /* ======================================================SiteWise details end=========================================================================*/  
 
/*================================================================set location details start====================================================================*/
function addLocation(id) {
	debugger;
var childProduct=$("#locationChildProduct"+id).val();
//var prodAmountAfterTax=$("#AmountAfterTaxId"+id).val();
//var prodQuantity=$("#QuantityId"+id).val();
var prodQuantity;
var prodAmountAfterTax;




var childProductId = childProduct.split("$")[0];
var childProductName = childProduct.split("$")[1];
//var totalAmount=$("#TotalAmountId"+id).val();
var totalAmount;
//var tempID;
$(".ChildProduct").each(function(){debugger;
if($(this).val()==childProductName){debugger;
	 var splitId=$(this).attr("id").split("ChildProduct")[1];
	 $("#locationQuantity"+id).val($("#QuantityId"+splitId).val());
	 prodQuantity=$("#QuantityId"+splitId).val();
	 prodAmountAfterTax =$("#AmountAfterTaxId"+splitId).val();
	 totalAmount=$("#TotalAmountId"+splitId).val();
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
	 // Cdata = data.split("@@");
	  
	 /* var resultArray = [];
	  for(var i=0;i<Cdata.length;i++){
	      resultArray.push(Cdata[i].split("@@")[0]);
	  }*/
	var options='<option>--select--</option>';
	  $.each(data,function(key,value){
		  options+='<option value="'+key+"$"+value+'">'+value+'</option>';		  
	});  
	  $("#location_mar"+id).html(options);
	  var result=(totalAmount/prodQuantity);
	  $("#total_Amount"+id).val(parseFloat(totalAmount).toFixed(2));
	  
	  
	  $("#locationQuantity"+id).val();
	  $("#price_Aftertax"+id).val(result.toFixed(2));
	  
	  /*({
	  $("#location_mar"+id).val(data);
	  		source : resultArray,
	  		change: function (event, ui) {
               if(!ui.item){
              	//if list item not selected then make the text box null	
              	 $("#siteNameId").val("");
               }
             },
	  		select: function (event, ui) {
               AutoCompleteSelectHandler(event, ui);
           }

	  	});*/
 },
 error:  function(data, status, er){
	 // alert(data+"_"+status+"_"+er);
	  }
 });


 
//code for selected text
	function AutoCompleteSelectHandler(event, ui)
	{}
};

/*==============================================================calculate amount for model pop start=====================================================*/
function calculateLocationAmount(id) {debugger;
	//checking number or not
	if(isNaN($("#locationQuantity"+id).val())){
		alert("Please enter valid data.");
		$("#locationQuantity"+id).val("");
		$("#locationQuantity"+id).focus();
		return false;
	}
    var locationChildProduct=$("#locationChildProduct"+id).val().split("$")[0];
	var Quantity=parseFloat($("#locationQuantity"+id).val()==''?0:$("#locationQuantity"+id).val());
	var totalAmount=parseFloat($("#total_Amount"+id).val()==""?0:$("#total_Amount"+id).val());
	var totalAmnt;
	if(Quantity==0 || Quantity=="0"){
		totalAmnt = 0;
	}else{
		totalAmnt = (totalAmount/Quantity);
	}	
	
	$("#price_Aftertax"+id).val(totalAmnt.toFixed(2));
	$(".hiddenchild").each(function(){debugger;
	if($(this).val().split("$")[0]==locationChildProduct){debugger;
	     var productChildID=$(this).val().split("$")[0];
		 var splitId1=$(this).attr("id").split("comboboxsubSubProd")[1];
		 var productTotalAmount=parseFloat($("#TotalAmountId"+splitId1).val());
		 var productQty=parseFloat($("#QuantityId"+splitId1).val());
		 var locationTotalAmount=0;
		 var locationQty=0;
		 $(".location").each(function(){
			var splitId=$(this).attr("id").split("location")[1];
			if(productChildID==$("#locationChildProduct"+splitId).val().split("$")[0]){
				locationTotalAmount+=parseFloat($("#total_Amount"+splitId).val());
				locationQty+=parseFloat($("#locationQuantity"+splitId).val());
			}
		 })
		 if(Quantity>productQty){
			 $("#locationQuantity"+id).val("0");
			 $("#price_Aftertax"+id).val("0");
			 $("#total_Amount"+id).val("0");
			 alert("You can't initiate more than childproduct quantity.")
			 return false;
		 }
		 if(locationTotalAmount>productTotalAmount){
			 $("#locationQuantity"+id).val("0");
			 $("#price_Aftertax"+id).val("0");
			 $("#total_Amount"+id).val("0")
			 alert("You can't initiate more than invoice amount.")
			 return false;
		 }
	  }
	});
	
	//totalAmnt = Math.round(totalAmnt * 100)/100;
}


/*==============================================================calculate amount for model pop end=====================================================*/


/*================================================================set location details end====================================================================*/

  $(function() {
  	$('#VendorNameId').keypress(function () {
	  $.ajax({
	  url : "./getVendorDetails.jsp",
	  type : "get",
	  data : "",
	  contentType : "application/json",
	  success : function(data) {
	  		$("#VendorNameId").autocomplete({
		  		source : data
		  	});
	  },
	  error:  function(data, status, er){
		  //alert(data+"_"+status+"_"+er);
		  }
	  });
  	});
  	$('#VendorNameId').on('change', function(){
  		var value = $(this).val();  		
  		value = value.replace("&", "$$$");  		
  		setVendorData (value); //pass the value as paramter
	 });
  });
  
  /*function setVendorData(vName) {
	  
	var url = "loadAndSetVendorInfo.spring?vendName="+vName;
	  
	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();	  
	}  
	else if(window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");  
	}	  
	try {
		request.onreadystatechange = setVedData;
		request.open("POST", url, true);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function setVedData() {
	if(request.readyState == 4 && request.status == 200) {
		var resp = request.responseText;
		resp = resp.trim();
		var vendorId = resp.split("|")[0];
		var vendorAddress = resp.split("|")[1];
		var vendorGsinNo = resp.split("|")[2];
		
		$("#vendorId").val(vendorId);
		$("#VendorAddress").val(vendorAddress);
		$("#GSTINNumber").val(vendorGsinNo);			
	}
}
*/
/*================================================================location details for start=============================================*/
function setVendorData(vName) {
	var url = "loadAndSetVendorInfo.spring?vendName="+vName;
	  
	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();	  
	}  
	else if(window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");  
	}	  
	try {
		request.onreadystatechange = setVedData;
		request.open("POST", url, true);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function setVedData() {
	if(request.readyState == 4 && request.status == 200) {
		var resp = request.responseText;
		resp = resp.trim();
		var vendorId = resp.split("|")[0];
		var vendorAddress = resp.split("|")[1];
		var vendorGsinNo = resp.split("|")[2];
		
		$("#vendorId").val(vendorId);
		$("#VendorAddress").val(vendorAddress);
		$("#GSTINNumber").val(vendorGsinNo);	
		$("#VendorAddress").attr("title", vendorAddress);
		$("#GSTINNumber").attr("title", vendorGsinNo);
	    $('[data-toggle="tooltip"]').tooltip(); 
	}
}


/*==================================================================location details for end===============================================*/
//append row to the HTML table
function appendRow() {
	
	var validateProductTableRow=validateProductRow();
	if(validateProductTableRow==false){
		return false;
	}
	
	var tbllength=$('#doInventoryTableId').find('tr').length;
	if(tbllength==2){
		var tid=$('#doInventoryTableId tr:last').attr('id');
		var res = tid.split("productchargesrow")[1];
		$("#addDeleteItemBtnId"+res).show();
	}
	var pressedKey = window.event;
	
	if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {
		
		btn = pressedKey.target || pressedKey.srcElement;
		var buttonId = btn.id;
		
		if(buttonId.includes("addNewItemBtnId")) {
			document.getElementById("hiddenSaveBtnId").value = "";
		}
		
		var hiddenSaveBtn = document.getElementById("hiddenSaveBtnId").value;
		
	    var tbl = document.getElementById("doInventoryTableId");
	    
	    calcuLateFinalAmount();
	   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
		    
		    var	row = tbl.insertRow(tbl.rows.length);
			
		    var i;
		    
		    var tableColumnName = "";
		    var columnToBeFocused = "";
		    var rowNum = getLastRowNum();
		    
		   $("#addNewItemBtnId"+rowNum).hide();
		    
		    rowNum = rowNum+1;
		    var rowid="productchargesrow"+rowNum;
			$(row).attr("id", rowid);
			$(row).attr("class", "productchargesrow");
		    for (i = 0; i < tbl.rows[0].cells.length; i++) {
		    	
		    	var x = document.getElementById("doInventoryTableId").rows[0].cells;
		    	tableColumnName = x[i].innerText;
		    	tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
		    	tableColumnName = tableColumnName.replace(/\./g,'');
		    	columnToBeFocused = x[1].innerText;
		    	columnToBeFocused = columnToBeFocused.replace(/ /g,'');	    	
		        createCell(row.insertCell(i), i, "row", rowNum, tbl.rows[0].cells.length, tableColumnName);	    	
		    }
		    
		    var lastDiv = getLastRowNum();
		    //alert(lastDiv);
		    
		    document.getElementById("Product"+lastDiv).focus();
		}
	}
}

//Append Row for other charges


function appendChargesRow() {
	
	//validating charges table
	var validateChargesTableRow=validateChargesRow();
	if(validateChargesTableRow==false){
		return false;
	}
	
	var tbllength=$('#doInventoryChargesTableId').find('tr').length;
	if(tbllength==2){
		var tid=$('#doInventoryChargesTableId tr:last').attr('id');
		var res = tid.split("chargesrow")[1];
		$("#addDeleteChargesItemBtnId"+res).show();
	}
	
	var pressedKey = window.event;
	if(pressedKey.keyCode == 13 || pressedKey.keyCode == undefined || pressedKey.keyCode == "undefined") {
		
		btn = pressedKey.target || pressedKey.srcElement;
		var buttonId = btn.id;
		
		if(buttonId.includes("addNewChargesItemBtnId1")) {
			document.getElementById("hiddenSaveChargesBtnId").value = "";
		}
		
		var hiddenSaveBtn = document.getElementById("hiddenSaveChargesBtnId").value;		
	    var tbl = document.getElementById("doInventoryChargesTableId");
	    
	    calcuLateFinalAmount();   
	    if(hiddenSaveBtn == "" || hiddenSaveBtn == '' || hiddenSaveBtn == null) {
		    
		    var	row = tbl.insertRow(tbl.rows.length);			
		    var i;		    
		    var tableColumnName = "";
		    var columnToBeFocused = "";
		    var rowChargesNum = getLastChargesRowNum();
		    
		    $("#addNewChargesItemBtnId"+rowChargesNum).hide();
		    
		    rowChargesNum = rowChargesNum+1;
		    var rowid="chargesrow"+rowChargesNum;
			$(row).attr("id", rowid);	
			$(row).attr("class", "chargesrow");
		    for (i = 0; i < tbl.rows[0].cells.length; i++) {		    	
		    	var x = document.getElementById("doInventoryChargesTableId").rows[0].cells;
		    	tableColumnName = x[i].innerText;
		    	tableColumnName = tableColumnName.replace(/ /g,'');//Replacing all white spaces in a given string.
		    	tableColumnName = tableColumnName.replace(/\./g,'');
		    	columnToBeFocused = x[1].innerText;
		    	columnToBeFocused = columnToBeFocused.replace(/ /g,'');	    	
		    	createChargesCell(row.insertCell(i), i, "row", rowChargesNum, tbl.rows[0].cells.length, tableColumnName);	    	
		    }		    
		    var lastDiv = getLastChargesRowNum();
		    document.getElementById("Conveyance"+lastDiv).focus();
		}
	}
}
/********************** Calculate Discount Amount**************************/

function calculateDiscountAmount(strSerialNumber){
	
	
	var discountnum="Discount"+strSerialNumber;    
	 var discountval = document.getElementById(discountnum).value;
	 if(isNaN(discountval)){
		 alert("Please enter valid data.");
		 $("#"+discountnum).val("");
		 $("#"+discountnum).focus();
		 return false;
	 }
	 	if (discountval.includes("%")){
	 		discval = discountval.substring(0, discountval.length-1);
	 	}else if(discountval.includes("")){
	 		discval = discountval;
	 		
	 	}
	 
	if(discountval > 100){
		alert('Please enter the discount 100 below');
		$("#"+discountnum).val("");
		 $("#"+discountnum).focus();
		 return false;
	}
	
	var Bamt= $("#BasicAmountId"+strSerialNumber).val();
	var  BAmnt = $("#taxAmount"+strSerialNumber).val(DamtAfrTax);
	var numbers = $("#Discount"+strSerialNumber).val();;
	var DamtAfrTax=Bamt - ( Bamt*discval/100 );
	$("#amtAfterDiscount"+strSerialNumber).val(parseFloat(DamtAfrTax).toFixed(2));
	calculateTaxAmount(strSerialNumber);
}
   
//************ End method for to append data to Other Charges************** 


function getLastRowNum() {
	
	var allElements = document.getElementsByTagName("*");	
	var allIds = [];
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoDivId") != -1) {
				allIds[i] = el.id;
			}
	  	}
	}		
	var lastDiv = (allIds.length) - 1;
	var lastDivVal = document.getElementById(allIds[lastDiv]).textContent;
	return parseInt(lastDivVal);
}

//********** Get Last Row for Charges****************

function getLastChargesRowNum() {
	
	var allElements = document.getElementsByTagName("*");
	
	var allIds = [];
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoChargesDivId") != -1) {
				allIds[i] = el.id;
			}
	  	}
	}		
	var lastDiv = (allIds.length) - 1;
	var lastDivVal = document.getElementById(allIds[lastDiv]).textContent;
	return parseInt(lastDivVal);
}
//deleting chargers row
function deleteRow(btn, currentRow) {
	debugger;
	var CanIDelet=window.confirm("Do you want to delete?");
	if(CanIDelet==false){
		return false;
	}	
	document.getElementById("hiddenSaveBtnId").value = "";
	var rowscount=$('#doInventoryChargesTableId').find('tr').length;
	
	if(rowscount==2){
		alert("this row can't be deleted.");
		return false;
	}
	//removing row
   $("#chargesrow"+currentRow).remove();
	
	var tid=$('#doInventoryChargesTableId tr:last').attr('id');	
	var res = tid.split("chargesrow")[1];
	if(rowscount==3){
		$("#addDeleteChargesItemBtnId"+res).hide();
	}
	if(res<currentRow){		
		$("#addNewChargesItemBtnId"+res).show();
	}	
	calcuLateFinalAmount();
}

//deleting product table 
function deleteproductRow(btn, currentRow) {
	var CanIDelet=window.confirm("Do you want to delete?");
	if(CanIDelet==false){
		return false;
	}	
	//If delete row button clicked then restting the Save hidden field value to empty.
	document.getElementById("hiddenSaveBtnId").value = "";
	var rowscount=$('#doInventoryTableId').find('tr').length;
	//removing row
	if(rowscount==2){
		alert("This row Can't be Deleted.");
		return false;
	}
	$("#productchargesrow"+currentRow).remove();
	
	var tid=$('#doInventoryTableId tr:last').attr('id');
	
	var res = tid.split("productchargesrow")[1];
	if(rowscount==3){
		$("#addDeleteItemBtnId"+res).hide();
		/*return false;*/
	}
	if(res<currentRow){
		$("#addNewItemBtnId"+res).show();
	}
	calcuLateFinalAmount();
}


/*************start cal price amount *******************/
function calculatePriceAmount(qtyNum) {
	var qty = "QuantityId"+qtyNum;
	var tAmnt = "BasicAmountId"+qtyNum;
	
	
	var price = "PriceId"+qtyNum; 
	
	
	
	var quantity = document.getElementById(qty).value;
	if(quantity == "" || quantity == null || quantity == '') {
		//document.getElementById(qty).removeEventListener("blur", "");
		return false;
	}
	
	if(quantity == 0 || quantity == 0.0 || quantity == 0.00 || quantity == '0' || quantity == '0.0' || quantity == '0.00' || quantity == "0" || quantity == "0.0" || quantity == "0.00") {
		alert("Please enter valid quantity.");
		document.getElementById(qty).value = "";
		document.getElementById(qty).focus();
		return false;
	}
	
	
	
	var amount = document.getElementById(tAmnt).value;
	
	if(amount == "" || amount == null || amount == '') {
		//document.getElementById(tAmnt).removeEventListener("blur", "");
		return false;
	}
	
	
	var basicAmt = (amount/quantity);
	basicAmt = Math.round(basicAmt * 100)/100;
	
	document.getElementById(price).value = parseFloat(basicAmt).toFixed(2);
	
	
}
/*************end cal price amount *******************/

function calculateTotalAmount(qtyNum) {
	debugger;
	var textData = $("#childProductVendorDesc"+qtyNum).val();
	$("#childProductVendorDesc"+qtyNum).attr("title", textData);
	var qty = "QuantityId"+qtyNum;
	var amnt = "PriceId"+qtyNum;
	var tAmnt = "BasicAmountId"+qtyNum;
	
	var quantity = document.getElementById(qty).value;
	var amount = document.getElementById(amnt).value;	
	if(isNaN(quantity)){
		alert("Please enter valid data.");
		$("#QuantityId"+qtyNum).val("");
		$("#QuantityId"+qtyNum).focus();
		return false;
	}
	if(isNaN(amount)){
		alert("Please enter valid data.");
		$("#PriceId"+qtyNum).val("");
		$("#PriceId"+qtyNum).focus();
		return false;
	}
	if(quantity == "" || quantity == null || quantity == '') {
		//document.getElementById(qty).focus();
		//document.getElementById(qty).removeEventListener("blur", "");
		return false;
	}
	if(quantity == 0 || quantity == 0.0 || quantity == 0.00 || quantity == '0' || quantity == '0.0' || quantity == '0.00' || quantity == "0" || quantity == "0.0" || quantity == "0.00") {
		alert("Please enter valid quantity.");
		document.getElementById(qty).value = "";
		document.getElementById(qty).focus();
		return false;
	}
	
	if(amount == "" || amount == null || amount == '') {
		return false;
	}
	
	var totalAmnt = (quantity*amount);
	totalAmnt = Math.round(totalAmnt * 100)/100;	
	document.getElementById(tAmnt).value = parseFloat(totalAmnt).toFixed(2);
	calculateDiscountAmount(qtyNum);
}

function calcuLateFinalAmount() {
	
	document.getElementById("hiddenSaveBtnId").value = "";	
	var allElements = document.getElementsByTagName("*");	
	var pipeData = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("TotalAmountId") != -1) {
				pipeData = pipeData+(el.id)+"|";
			}
	  	}
	}
	
	var data = pipeData.split("|");
	var lastDiv = data.length-1;
	var tempAmnt = 0;
	
	for(var x=0; x < lastDiv; x++) {
    	var fldName = data[x];
    	//alert(fldName);
    	
    	if(fldName.indexOf("TotalAmountId") != -1) {
    		var fldAmnt = document.getElementById(fldName).value;
    		if(fldAmnt != "" && fldAmnt != null && fldAmnt != '') {
        		tempAmnt = parseFloat(tempAmnt) + parseFloat(fldAmnt);
        		tempAmnt = Math.round(tempAmnt * 100)/100;
    		}
		}	
    }
    //alert("Final Amount = "+tempAmnt);
	tempAmnt = Math.round(tempAmnt * 100)/100;
/*    document.getElementById("finalAmntDiv").innerHTML = tempAmnt;
    document.getElementById("ttlAmntForIncentEntryId").value = tempAmnt;*/
}

/*START 31-AUG-17*/
function getAllChargesCount() {
	
	var allElements = document.getElementsByTagName("*");
	
	var pipeData = "";
	
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoChargesDivId") != -1) {
				var numberPattern = /\d+/g;
				var snosid = ask.match(numberPattern);
				pipeData = pipeData+snosid+"|";
			}
	  	}
	}
	return pipeData;
}
/*END 31-AUG-17*/
function getAllProdsCount() {
	
	var allElements = document.getElementsByTagName("*");
	
	var pipeData = "";
	
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoDivId") != -1) {
				var numberPattern = /\d+/g;
				var snosid = ask.match(numberPattern);
				pipeData = pipeData+snosid+"|";
			}
	  	}
	}
	return pipeData;
}

function loadSubProds(prodId, rowNum) {
	
	prodId = prodId.split("$")[0];	
	var url="indentReceiveSubProducts.spring?mainProductId="+prodId;	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				$("#SubProduct"+rowNum).val("");
				$("#ChildProduct"+rowNum).val("");
				$("#UnitsOfMeasurementId"+rowNum).val("");				
				$("#TaxId"+rowNum).val("");
				
				var resp = request.responseText;
				resp = resp.trim();
				var spltData = resp.split("|");
				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var subProdToSelect = "comboboxsubProd"+rowNum;
		    	var selectBox = document.getElementById(subProdToSelect);
		    	
			    //Removing previous options from select box - Start
			    if(document.getElementById(subProdToSelect) != null && document.getElementById(subProdToSelect).options.length > 0) {
			    	document.getElementById(subProdToSelect).options.length = 0;
			    }
			    //Removing previous options from select box - End
			    
			    initOpt = document.createElement("option");
			    initOpt.text = "--Select--";
			    initOpt.value = "";
			    selectBox.appendChild(initOpt);
			    
			    var defaultOption;
			    var data;
			    
			    for(var i=0; i<available.length; i++) {
			    	defaultOption = document.createElement("option");
			    	data = available[i].split("_");
			    	if(data[0] != "" && data[0] != null && data[0] != '') {
			    		var prodIdAndName = data[0]+"$"+data[1];			    		
			    		defaultOption.text = data[1];
			    	    defaultOption.value = prodIdAndName;
			    	    selectBox.appendChild(defaultOption);
			    	}
			    }				
			}
		};		
		request.open("POST", url, false);
		request.send(null); 
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function loadSubSubProducts(subProdId, rowNum) {
	
	subProdId = subProdId.split("$")[0];	
	var url="indentReceiveChildProducts.spring?subProductId="+subProdId;	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				$("#ChildProduct"+rowNum).val("");
				$("#UnitsOfMeasurementId"+rowNum).val("");
				$("#TaxId"+rowNum).val("");
				
				var resp = request.responseText;
				resp = resp.trim();
				var spltData = resp.split("|");
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var subSubProdToSelect = "comboboxsubSubProd"+rowNum;
		    	var selectBox = document.getElementById(subSubProdToSelect);
		    	
			    //Removing previous options from select box - Start
			    if(document.getElementById(subSubProdToSelect) != null && document.getElementById(subSubProdToSelect).options.length > 0) {
			    	document.getElementById(subSubProdToSelect).options.length = 0;
			    }
			    //Removing previous options from select box - End
			    
			    initOpt = document.createElement("option");
			    initOpt.text = "--Select--";
			    initOpt.value = "";
			    selectBox.appendChild(initOpt);
			    
			    var defaultOption;
			    var data;
			    
			    for(var i=0; i<available.length; i++) {
			    	defaultOption = document.createElement("option");
			    	data = available[i].split("_");
			    	if(data[0] != "" && data[0] != null && data[0] != '') {			    		
			    		var prodIdAndName = data[0]+"$"+data[1];			    		
			    		defaultOption.text = data[1];
			    	    defaultOption.value = prodIdAndName;
			    	    selectBox.appendChild(defaultOption);
			    	}
			    }		
			}	
		};		
		request.open("POST", url, false);
		request.send(null); 
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function loadUnits(prodId, rowNum) {
	
	prodId = prodId.split("$")[0];	
	var url = "listIndentReciveUnitsOfChildProducts.spring?productId="+prodId;	  
	var request = getAjaxObject();
	
	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				var resp = request.responseText;
				resp = resp.trim();
				
				var spltData = resp.split("|");
				
		    	available = new Array();
		    	for(var j=0; j<spltData.length; j++) {
		    		available[j] = spltData[j];
		    	}
				
		    	var unitsToSelect = "UnitsOfMeasurementId"+rowNum;
		    	var selectBox = document.getElementById(unitsToSelect);
			    
			    //Removing previous options from select box - Start
			    if(document.getElementById(unitsToSelect) != null && document.getElementById(unitsToSelect).options.length > 0) {
			    	document.getElementById(unitsToSelect).options.length = 0;
			    }
			    //Removing previous options from select box - End
			    
			    initOpt = document.createElement("option");
			    initOpt.text = "--Select--";
			    initOpt.value = "";
			    selectBox.appendChild(initOpt);
			    
			    var defaultOption;
			    var data;
			    
			    for(var i=0; i<available.length; i++) {
			    	defaultOption = document.createElement("option");
			    	data = available[i].split("_");
			    	if(data[0] != "" && data[0] != null && data[0] != '') {
			    		var unitsIdAndType = data[0]+"$"+data[1];
			    		defaultOption.text = data[1];
			    	    defaultOption.value = unitsIdAndType;
			    	    selectBox.appendChild(defaultOption);
			    	}
			    }
			}	
		};		
		request.open("POST", url, false);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}
//********* Tax calculation for Grid one ***************
function calculateTaxAmount(rowNum) {
	debugger;
	var e = document.getElementById("TaxId"+rowNum);	
	var selectedTax = e.options[e.selectedIndex].value;
	
	if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
		selectedTax = selectedTax.split("$")[1];
		selectedTax = selectedTax.substring(0, selectedTax.length - 1);
		selectedTax = selectedTax.trim();
		//alert("Selected Tax = "+selectedTax);
		
		var basicAmnt = document.getElementById("amtAfterDiscount"+rowNum).value;
		//alert("Basic Amount = "+basicAmnt);
		
		percentage(basicAmnt, selectedTax, rowNum);
	}
	else {
		document.getElementById("TaxAmountId"+rowNum).value = "";
		document.getElementById("AmountAfterTaxId"+rowNum).value = "";
	}	
}
//********** End Tax calculation for grid one***************




//********************** Tax calculation GST for second grid*****************

function calculateGSTTaxAmount(rowNum) {
	debugger;
	var convenceAmount=$("#ConveyanceAmount"+rowNum).val();
	if(isNaN(convenceAmount)){
		alert("Please enter valid data.");
		$("#ConveyanceAmount"+rowNum).val("");
		$("#ConveyanceAmount"+rowNum).focus();
		return false;
	}
	var e = document.getElementById("GSTTax"+rowNum);
	var selectedTax = e.options[e.selectedIndex].value;
	
	if(selectedTax != "" && selectedTax != '' && selectedTax != null) {
		selectedTax = selectedTax.split("$")[1];
		selectedTax = selectedTax.substring(0, selectedTax.length - 1);
		selectedTax = selectedTax.trim();
		var basicAmnt = document.getElementById("ConveyanceAmount"+rowNum).value;
		
		GSTpercentage(basicAmnt, selectedTax, rowNum);
	}
}
function formatChargesColumns(colName) {
	var colNm = colName.replace(/ /g,'');
	return colNm.replace(/\./g,'');
}


//********************** End Tax calculation GST for second grid*****************
function percentage(basicAmnt, tax, rowNum) {
	debugger;
	var taxAmnt = (basicAmnt/100) * tax;
	taxAmnt = Math.round(taxAmnt * 100)/100;
	document.getElementById("TaxAmountId"+rowNum).value = parseFloat(taxAmnt).toFixed(2);
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("AmountAfterTaxId"+rowNum).value = parseFloat(amntaftTx).toFixed(2);
}


//************ Calculation for Conveyance AMount*******

function GSTpercentage(basicAmnt, tax, rowNum) {
	debugger;
	var taxAmnt = (basicAmnt/100) * tax;
	taxAmnt = Math.round(taxAmnt * 100)/100;
	document.getElementById("GSTAmount"+rowNum).value = parseFloat(taxAmnt).toFixed(2);
	var amntaftTx = parseFloat(basicAmnt) + parseFloat(taxAmnt);
	amntaftTx = Math.round(amntaftTx * 100)/100;
	document.getElementById("AmountAfterTaxx"+rowNum).value = parseFloat(amntaftTx).toFixed(2);
}

//*****************************************************
function calculateOtherCharges() {

	debugger;
	var validateProductTableRow=validateProductRow();
	if(validateProductTableRow==false){
		return false;
	}
	//validating charges table
	var validateChargesTableRow=validateChargesRow();
	if(validateChargesTableRow==false){
		return false;
	}
	var resetSno = getTotalSNOS();
	var resetSpltdData = resetSno.split("|");
	var resetSnoId = resetSpltdData.length - 1;
	for(var x=0; x<resetSnoId; x++) {

		var resetval = resetSpltdData[x];
		 document.getElementById("OtherOrTransportChargesId"+resetval).value = "";
		 document.getElementById("TaxOnOtherOrTransportChargesId"+resetval).value = "";
		 document.getElementById("OtherOrTransportChargesAfterTaxId"+resetval).value = "";
		 document.getElementById("TotalAmountId"+resetval).value = "";
	}
	
	 var convenceCharges=0;
	 var convenceTaxAmt=0;
	 var convenceAmtAfterTax=0;
	 
	 var chargeSno = getChargeTotalSNOS();
		var snoSpltdData = chargeSno.split("|");
		var chargeSnoId = snoSpltdData.length - 1;

		for(var y=0; y<chargeSnoId; y++) {
		
			var chargeVal = snoSpltdData[y];
			 convenceCharges = parseFloat($('#ConveyanceAmount'+chargeVal+'').val().trim());
			 convenceTaxAmt = $('#GSTTax'+chargeVal+'').val().trim();
			 var array = convenceTaxAmt.split("$");
			 var taxPercentage = array[1];
			 taxPercentage = taxPercentage.replace("%", "");

	 console.log("taxPercentage "+taxPercentage);
	 convenceCharges = Math.round(convenceCharges * 100)/100;
	var rsltTtlBscAmnt = calculateTotalBasicAmount();
	
	var snos = getTotalSNOS();
	
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;
	var fnlAmnt = 0;	
	for(var x=0; x<snoId; x++) {
		var val = spltdData[x];
		var individualBasicAmount = $("#amtAfterDiscount"+val).val()==""?0:$("#amtAfterDiscount"+val).val();
		individualBasicAmount = individualBasicAmount.trim();
		individualBasicAmount = parseFloat(individualBasicAmount);
		individualBasicAmount = Math.round(individualBasicAmount * 100)/100;		
		//var charges = (individualBasicAmount * convenceCharges) / rsltTtlBscAmnt;
		if(rsltTtlBscAmnt!=0){
		var charges = (individualBasicAmount * convenceCharges) / rsltTtlBscAmnt;
		}else{
		var charges = 0;
		}
		
		if (snoSpltdData[y] > 1) {
			var existVal = $("#OtherOrTransportChargesId"+val).val();
			var taxOnOtherCharges = $("#TaxOnOtherOrTransportChargesId"+val).val();
		}
		var amount = 0;
		 var result = 0;
		if (existVal == null || existVal == '' || existVal=="") {
		    document.getElementById("OtherOrTransportChargesId"+val).value = parseFloat(charges).toFixed(2);
		    result = parseFloat(charges).toFixed(2);
		} else {
			existVal = Math.round(existVal * 100)/100;
			result = parseFloat(charges)+parseFloat(existVal);
			document.getElementById("OtherOrTransportChargesId"+val).value = result.toFixed(2);

		}
		var taxAmount = (charges/100)*taxPercentage;
		
		if (taxOnOtherCharges == null || taxOnOtherCharges == '' || taxOnOtherCharges=="") {
			var otherChaVal = parseFloat(taxAmount) +  parseFloat(result);
			document.getElementById("TaxOnOtherOrTransportChargesId"+val).value = taxAmount.toFixed(2);
			document.getElementById("OtherOrTransportChargesAfterTaxId"+val).value =  otherChaVal.toFixed(2);
		} else {
			var addedCharge = parseFloat(taxAmount)+parseFloat(taxOnOtherCharges);
			document.getElementById("TaxOnOtherOrTransportChargesId"+val).value = addedCharge.toFixed(2);
			var otherChaVal = parseFloat(taxAmount) +  parseFloat(result) +  parseFloat(taxOnOtherCharges);;
			document.getElementById("OtherOrTransportChargesAfterTaxId"+val).value =  otherChaVal.toFixed(2);
		}
		
		//Setting Total Amount - Start
		var amntAfterTax = document.getElementById("AmountAfterTaxId"+val).value;
		amntAfterTax = parseFloat(amntAfterTax);
		
		var othOrTransChrgsAfterTax = document.getElementById("OtherOrTransportChargesAfterTaxId"+val).value;
		othOrTransChrgsAfterTax = parseFloat(othOrTransChrgsAfterTax);
		
		var ttlAmnt = parseFloat(amntAfterTax) + parseFloat(othOrTransChrgsAfterTax);
		ttlAmnt = Math.round(ttlAmnt * 100)/100;
		
		document.getElementById("TotalAmountId"+val).value = parseFloat(ttlAmnt).toFixed(2);
		//Setting Total Amount - End
		
		//Setting Final Amount - Start
		fnlAmnt = parseFloat(fnlAmnt);
		fnlAmnt = fnlAmnt + ttlAmnt;
		fnlAmnt = Math.round(fnlAmnt * 100)/100;
		//Setting Final Amount - End
	}
	//Setting Final Amount - Start
	fnlAmnt = Math.round(fnlAmnt * 100)/100;
	document.getElementById("finalAmntDiv").innerHTML = parseFloat(fnlAmnt).toFixed(2);
	//Setting Final Amount - End
}
}

function calculateTotalBasicAmount() {
	var snos = getTotalSNOS();
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;
	var totalBasicAmount = 0;
	for(var x=0; x<snoId; x++) {
	
		var val = spltdData[x];
		var basicAmnt = document.getElementById("amtAfterDiscount"+val).value;
		basicAmnt = basicAmnt.trim();
		basicAmnt = parseFloat(basicAmnt);
		basicAmnt = Math.round(basicAmnt * 100)/100;
		totalBasicAmount = parseFloat(totalBasicAmount) + basicAmnt;
		totalBasicAmount = Math.round(totalBasicAmount * 100)/100;
	}
	return totalBasicAmount;
}

function getChargeTotalSNOS() {
	
	var allElements = document.getElementsByTagName("*");
	var snos = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoChargesDivId") != -1) {
				snos = snos+document.getElementById(el.id).textContent+"|";
			}
	  	}
	}
	return snos;
}




function getTotalSNOS() {
	
	var allElements = document.getElementsByTagName("*");
	var snos = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoDivId") != -1) {
				snos = snos+document.getElementById(el.id).textContent+"|";
			}
	  	}
	}
	return snos;
}

/********* Method for adding Transport charges****************/
function getTotalChargesSNOS() {
	
	var allElements = document.getElementsByTagName("*");
	var snos = "";
	for (var i = 0, n = allElements.length; i < n; ++i) {
	  	var el = allElements[i];
	  	if (el.id) {
			var ask = el.id;
			if(ask.indexOf("snoChargesDivId") != -1) {
				snos = snos+document.getElementById(el.id).textContent+"|";
			}
	  	}
	}
	return snos;
}

/****************************************************************/
function getHighestTax() {
	
	var snos = getTotalSNOS();
	var spltdData = snos.split("|");
	var snoId = spltdData.length - 1;	
	var snoids = [];

	for(var x=0; x<snoId; x++) {	
		var val = spltdData[x];
		var selectedTax = document.getElementById("TaxId"+val);
		selectedTax = selectedTax.options[selectedTax.selectedIndex].value;
		selectedTax = selectedTax.split("$")[1];
		selectedTax = selectedTax.substring(0, selectedTax.length - 1);
		selectedTax = selectedTax.trim();
		snoids[x] = selectedTax;
	}
	var sortedSnos = snoids.sort(function(a, b){return a - b;});
	//alert(sortedSnos);
	
	var last_element = sortedSnos[sortedSnos.length - 1];
	return last_element;
}

//Field Validation started
function validateRowData() {	
	  
	var invoiceNum = document.getElementById("InvoiceNumberId").value;
	
	if(invoiceNum == "" || invoiceNum == null || invoiceNum == '') {
		alert("Please enter Invoice Number.");
		document.getElementById("InvoiceNumberId").focus();
		return false;
	}				
	  
	var invoiceDate = document.getElementById("InvoiceDateId").value;
	
	if(invoiceDate == "" || invoiceDate == null || invoiceDate == '') {
		alert("Please Choose Invoice Date.");
		document.getElementById("InvoiceDateId").focus();
		return false;
	}	  
	  
	var vendorName = document.getElementById("VendorNameId").value;
	
	if(vendorName == "" || vendorName == null || vendorName == '') {
		alert("Please enter Vendor Name.");
		document.getElementById("VendorNameId").focus();
		return false;
	} 			
	  
	var vendorAdd = document.getElementById("VendorAddress").value;
	
	if(vendorAdd == "" || vendorAdd == null || vendorAdd == '') {
		alert("Please enter Vendor Address.");
		document.getElementById("VendorAddress").focus();
		return false;
	}
	  
	var gsinNum = document.getElementById("GSTINNumber").value;
	
	if(gsinNum == "" || gsinNum == null || gsinNum == '') {
		alert("Please enter GRN NO.");
		document.getElementById("GSTINNumber").focus();
		return false;
	}	 
	
    var receivedDate = document.getElementById("receivedDate").value;
    if(receivedDate == "" || receivedDate == '' || receivedDate == null) {
		alert("Please Choose ReciveD Date .");
		document.getElementById("receivedDate").focus();
		return false;
	}
    
    
    var elementList = document.getElementsByTagName("*");    
    var rowNums = getAllProdsCount();    
    var splitedRows = rowNums.split("|");    
    for(var x=0; x < rowNums.length; x++) {
    	
    	var curRow = splitedRows[x];    	
    	var prodId = "Product"+curRow;    	
    	var subProdId = "SubProduct"+curRow;		
    	var childProdId = "ChildProduct"+curRow;    	
    	var quantityId = "QuantityId"+curRow;    	
    	var unitsOfMeasurementId = "UnitsOfMeasurementId"+curRow;
    	var priceId = "PriceId"+curRow;
    	var basicAmountId = "BasicAmountId"+curRow;
    	var taxId = "TaxId"+curRow;
    	var hSNCodeId = "HSNCodeId"+curRow;
    	    	
    	for (var i in elementList) {
        	
    		if (elementList[i].id != "") {
	    	  
	    	  if(elementList[i].id == prodId) {
	    		  var pro = document.getElementById(prodId).value;
	    		  if(pro == "" || pro == null || pro == '') {
	    				alert("Please select product.");
	    				document.getElementById(prodId).focus();
	    				return false;
	    		  }
	    	  } 
		  	  else if(elementList[i].id == subProdId) {
		  		  var subPro = document.getElementById(subProdId).value;
	    		  if(subPro == "" || subPro == null || subPro == '') {
	    			  alert("Please select sub product.");
	    			  document.getElementById(subProdId).focus();
	    			  return false;
	    		  }
	    	  }  	  
		  	  else if(elementList[i].id == childProdId) {
		  		  var childPro = document.getElementById(childProdId).value;
		  		  if(childPro == "" || childPro == null || childPro == '') {
		  			  alert("Please select child product.");
		  			  document.getElementById(childProdId).focus();
		  			  return false;
		  		  }
	    	  }
	    	  else if(elementList[i].id == quantityId) {
	    		  var qty = document.getElementById(quantityId).value;
	    		  if(qty == "" || qty == null || qty == '' || qty == 0 || qty == '0' || qty == "0") {
	    			  alert("Please enter quantity.");
	    			  document.getElementById(quantityId).focus();
	    			  return false;
	    		  }
	    	  }
	    	  else if(elementList[i].id == unitsOfMeasurementId) {
	    		  var units = document.getElementById(unitsOfMeasurementId).value;
	    		  if(units == "" || units == null || units == '') {
	    			  alert("Please enter UOM.");
	    			  document.getElementById(unitsOfMeasurementId).focus();
	    			  return false;
	    		  }
	    	  }
	    	  else if(elementList[i].id == priceId) {
	    		  var amnt = document.getElementById(priceId).value;
	    		  if(amnt == "" || amnt == null || amnt == '' || amnt == 0 || amnt == 0.0 || amnt == '0.0' || amnt == "0.00") {
	    			  alert("Please enter price.");
	    			  document.getElementById(priceId).focus();
	    			  return false;
	    		  }
	    	  }
	    	  else if(elementList[i].id == basicAmountId) {
	    		  var cmdxval = document.getElementById(basicAmountId).value;
	    		  if(cmdxval == "" || cmdxval == null || cmdxval == '' || cmdxval == 0.0 || cmdxval == '0.0' || cmdxval == "0.0") {
	    			  alert("Please enter total amount.");
	    			  document.getElementById(basicAmountId).focus();
	    			  return false;
	    		  }
	    	  }
	    	  else if(elementList[i].id == taxId) {
	    		  var taxVal = document.getElementById(taxId).value;
	    		  if(taxVal == "" || taxVal == null || taxVal == '') {
	    			  alert("Please select tax.");
	    			  document.getElementById(taxId).focus();
	    			  return false;
	    		  }
			 }
			  else if(elementList[i].id == "state") { 
	    		var vendorstate = document.getElementById("state").value;
				if(vendorstate == "" || vendorstate == null || vendorstate == '') {
					alert("Please select state.");
					document.getElementById("state").focus();
					return false;
				}
		  }
	    	 else if(elementList[i].id == hSNCodeId) {
	    		 var hsnCodeVal = document.getElementById(hSNCodeId).value;
	    		 if(hsnCodeVal == "" || hsnCodeVal == null || hsnCodeVal == '') {
	    			 alert("Please enter HSN code.");
	    			 document.getElementById(hSNCodeId).focus();
	    			 return false;
	    		 }
	    	 }
	      }
    	}
	}
}
//Field Validation started

//Quantity and Price Validation - Start
function validateNumbers(el, evt) {

    var charCode = (evt.which) ? evt.which : event.keyCode;
    var number = el.value.split('.');
    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    //just one dot
    if(number.length > 1 && charCode == 46) {
         return false;
    }
    //get the carat position
    var caratPos = getSelectionStart(el);
    var dotPos = el.value.indexOf(".");
    if( caratPos > dotPos && dotPos>-1 && (number[1].length > 1)){
        return false;
    }
    return true;
}
function getSelectionStart(o) {
	if (o.createTextRange) {
		var r = document.selection.createRange().duplicate();
		r.moveEnd('character', o.value.length);
		if (r.text == '') return o.value.length;
		return o.value.lastIndexOf(r.text);
	} else return o.selectionStart;
}
//Quantity and Price Validation - End

function validateProductAvailability(unitsFld) {
	
	var measurementId = unitsFld.id;
	
	var currentRowNum = measurementId.match(/\d+/g);
	//alert(currentRowNum);
	
	measurementId = document.getElementById(measurementId);
	measurementId = measurementId.options[measurementId.selectedIndex].value;
	//alert(measurementId);
	
	measurementId = measurementId.split("$")[0];
	//alert(measurementId);
	
	if(measurementId != "" && measurementId != '' && measurementId != null) {
	
		var pro = $("#combobox"+currentRowNum).val();
		pro = pro.split("$")[0];
		//alert(pro);
		
		var subPro = $("#comboboxsubProd"+currentRowNum).val();
		subPro = subPro.split("$")[0];
		//alert(subPro);
		
		var childPro = $("#comboboxsubSubProd"+currentRowNum).val();
		childPro = childPro.split("$")[0];
		//alert(childPro);	
		var requestedDate=$("#receivedDate").val();
		//productAvailability(pro, subPro, childPro, measurementId, currentRowNum,requestedDate);
	}
	else {
    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = "";
	}
}

function productAvailability(mainProdId, subProdId, childProdId, measurementId, currentRowNum,requestedDate) {
	
	var url = "getProductAvailability.spring?prodId="+mainProdId+"&subProductId="+subProdId+"&childProdId="+childProdId+"&measurementId="+measurementId+"&requesteddate="+requestedDate;
	//alert(url);
	
	var	request = getAjaxObject();

	try {
		request.onreadystatechange = function() {
			
			if(request.readyState == 4 && request.status == 200) {
				
				var resp = request.responseText;
				resp = resp.trim();
				//alert(resp);
				
				if(resp == "" || resp == '' || resp == "null" || resp == null) {
					resp = "0";
				}		    	
		    	document.getElementById("ProductAvailabilityId"+currentRowNum).value = resp;		
			}		
		};
		request.open("POST", url, false);
		request.send();  
	}
	catch(e) {
		alert("Unable to connect to server!");
	}
}

function getAjaxObject() {
	
	var request = null;
	
	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();
	}  
	else if(window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");  
	}
	return request;
}

function formatColumns(colName) {
	var colNm = colName.replace(/ /g,'');
	return colNm.replace(/\./g,'');
}

function saveRecords(saveBtnClicked) {
	
	document.getElementById("hiddenSaveBtnId").value = saveBtnClicked;
	calculateOtherCharges();
	
	var valStatus = appendRow();
	
	if(valStatus == false) {
    	return;
	}
	
    var elementList = document.getElementsByTagName("*");
    
    var rowNums = getAllProdsCount();
    //alert(rowNums);
    
    var splitedRows = rowNums.split("|");
    
    for(var x=0; x < rowNums.length; x++) {
    	
    	var curRow = splitedRows[x];
    	
var expireDateId = "expireDateId"+curRow;
    	
    	for (var i in elementList) {
        	
    		if (elementList[i].id != "") {
    	
    			if(elementList[i].id == expireDateId) {
  	    		  var expireDateId = document.getElementById(expireDateId).value;
  	    		  if(expireDateId == "" || expireDateId == null || expireDateId == '') {
  	    				alert("Please Choose Expire Date.");
  	    				document.getElementById(expireDateId).focus();
  	    				return false;
  	    		  }
  	    		 var invoiceDate = document.getElementById("InvoiceDateId").value;
  	    	    if(invoiceDate == "" || invoiceDate == '' || invoiceDate == null) {
  	    			alert("Please Choose invoice Date .");
  	    			document.getElementById("InvoiceDateId").focus();
  	    			return false;
  	    		}
  	    		  
  	    	  var vendorName = document.getElementById("VendorNameId").value;
	    	    if(vendorName == "" || vendorName == '' || vendorName == null) {
	    			alert("Please Choose vendor Name  .");
	    			document.getElementById("VendorNameId").focus();
	    			return false;
	    		}
  	    	    
	    	    var state = document.getElementById("state").value;
	    	    if(state == "" || state == '' || state == null) {
	    			alert("Please Choose state  .");
	    			document.getElementById("state").focus();
	    			return false;
	    		}
  	    	    
	    	    var invoiceNumber = document.getElementById("InvoiceNumberId").value;
	    	    if(invoiceNumber == "" || invoiceNumber == '' || invoiceNumber == null) {
	    			alert("Please Choose Invoice Number  .");
	    			document.getElementById("InvoiceNumberId").focus();
	    			return false;
	    		}
  	    	  } 
    		}
    	}
    }
	
	var canISubmit = window.confirm("Do you want to Save?");
	
	if(canISubmit == false) {
		return false;
	}
	
	document.getElementById("saveBtnId").disabled = true;	
	document.getElementById("countOfRows").value = getAllProdsCount();
	document.getElementById("countOfChargesRows").value = getAllChargesCount();
	
	document.getElementById("doInventoryFormId").action = "doIndentReceive.spring";
	document.getElementById("doInventoryFormId").method = "POST";
	document.getElementById("doInventoryFormId").submit();
}
//childproduct duplicate changes written by thirupathi
function childcampare(childname, rowNum){
	$("#ChildProduct"+rowNum).val("");
	var tablelength=$("#doInventoryTableId > tbody > tr").length;	
	if(tablelength>1){
		debugger;
		jQuery('.ChildProduct').each(function() {
			  var currentElement = $(this);
			      value = currentElement.val();
			  if(value==childname){
				$("#comboboxsubSubProd"+rowNum).val("");
				alert("This child product is already exist, Please choose different child product.");				
				return rv = false;
			   }     
			  else{			
				  //loadLocationChildProduct();
				  return rv = true;
			  }
		});
		 return rv;
	}
}
/*===================================script for multiple file uplaod===============================*/
function dynamic(){
	debugger;
	var classlength=$(".selectCount").length;
	var classLastId=$(".selectCount:last").attr("id").split("file_select")[1];
	if($("#file_select"+classLastId).val()==""){
	 alert("please select file");
	 $("#file_select"+classLastId).focus();
	 return false;
	}
	if(classlength>7){
	alert("You can't uoload more than eight files");
	return false;
	}

	var btnid = $(".selectCount:last").attr("id").split("file_select")[1];
	var dynamicId = parseInt(classLastId) + 1;

	 $(".col-md-12 .files_place").append('<div class="clearfix"></div><div class="market_file_style"><input type="file" id="file_select'+dynamicId+'" accept="application/pdf,image/*" name="file" class="selectCount" style="float:left;" onchange="filechange('+dynamicId+')"/><button type="button" class="btn btn-danger" id="close_btn'+dynamicId+'" style="float:left;display:none;" onclick="filedelete('+dynamicId+')"><i class="fa fa-close"></i></button></div>');

	}

	function filechange(id){debugger;
	var size_file = ($("#file_select"+id))[0].files[0]
	file_size = size_file.size;
	if((size_file.type)=='application/pdf'){
		if((file_size/1024)<=1024 && (size_file.type)=='application/pdf'){$("#close_btn"+id).show();
		$("#Add").show();}
		else{
			alert("Your file size"+file_size+ "So Please upload Below this 1MB");
			$("#file_select"+id).val("");
		    return false;	
		}
	}
	else{$("#close_btn"+id).show();
	$("#Add").show();}
	
	}
	function filedelete(id){
	var classlength=$(".selectCount").length;
	if(classlength==1){
		//alert("You can't delte this files");
		$(".selectCount").val("");
		$("#Add").hide();
		$("#close_btn"+id).hide();
		return false;
	}
	$("#file_select"+id).remove() ;
	$("#close_btn"+id).remove();
	}
/*===================================script for multiple file uplaod===============================*/
	
	function validateProductRow(){
		//debugger;
		var error=true;
		$(".productchargesrow").each(function(){
			var currentId=$(this).attr("id").split("productchargesrow")[1];
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
		$(".chargesrow").each(function(){
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
	/*validate filed and location*/
	function validateFieldlocationRow(){
		//debugger;
		var error=true;
		$(".location").each(function(){
			debugger;
			var currentId=$(this).attr("id").split("location")[1];
			if($("#locationChildProduct"+currentId).val()=='--select--'){
				alert("Please select child product.");
				$("#locationChildProduct"+currentId).focus();
				return  error=false;
			}
			/*if($("#location_mar"+currentId).val()=='--select--'){
				alert("please select location.");
				$("#location_mar"+currentId).focus();
				return  error=false;
			}*/
			if($("#from_date_location"+currentId).val()==''){
				alert("Please select from date.");
				$("#from_date_location"+currentId).focus();
				return  error=false;
			}
			/*if($("#to_date_location"+currentId).val()==''){
				alert("please select to date.");
				$("#to_date_location"+currentId).focus();
				return  error=false;
			}*/
			/*if($("#timepicker"+currentId).val()==''){
				alert("please select time.");
				$("#timepicker"+currentId).focus();
				return  error=false;
			}*/
			if($("#locationQuantity"+currentId).val()=='' || $("#locationQuantity"+currentId).val()<=0){
				alert("Please enter quantity.");
				$("#locationQuantity"+currentId).focus();
				return  error=false;
			}
			if($("#price_Aftertax"+currentId).val()=='' || $("#price_Aftertax"+currentId).val()<=0){
				alert("Please enter price per unit after tax.");
				$("#price_Aftertax"+currentId).focus();
				return  error=false;
			}
			if($("#total_Amount"+currentId).val()=='' || $("#total_Amount"+currentId).val()<=0){
				alert("Please enter total amount.");
				$("#total_Amount"+currentId).focus();
				return  error=false;
			}
			
			
			if($("#site_Name"+currentId).val()==''){
				alert("please select site.");
				$("#site_Name"+currentId).focus();
				return  error=false;
			}
			
		});
		
		return  error;
	}
	/*validate filed and location*/
	/*=======second row child product validations===*/
