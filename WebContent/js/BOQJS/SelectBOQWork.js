$(document).ready(function()
{	
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
.attr( "title", "" )

.attr("id", this.element[0].name)
.addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left "+this.element[0].name)
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


var ele = this.element[0].name;
//var str1 = ele.replace(/.$/,"");

//alert("str1 "+ele);

//alert("okss ");





var prodId = ui.item.option.value;
var siteId = $('input[name="siteId"]').val();
var typeOfWork = $('input[name="typeOfWork"]').val();

var  prodName = ui.item.value;



if(ele == "combobox_Product") {
	//alert("if matched")
	$(".combobox_SubProduct").val("");
	$(".combobox_ChildProduct").val("");
loadSubProds(prodId,siteId,typeOfWork)
}
else if(ele == "combobox_SubProduct"){
	$(".combobox_ChildProduct").val("");
	loadChildProds(prodId,siteId,typeOfWork)
}

 

this._trigger( "select", event, {
item: ui.item.option
});
},

autocompletechange: "_removeIfInvalid"
});
},

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

_removeIfInvalid: function( event, ui) {

	
  
	
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



// Remove invalid value
this.input
.val( "" )
.attr( "title", value + " didn't match any item" )
.tooltip( "open" );
this.element.val( "" );
this._delay(function() {
this.input.tooltip( "close" ).attr( "title", "" );
}, 2500 );
this.input.data( "ui-autocomplete" ).term = "";
},

_destroy: function() {
this.wrapper.remove();
this.element.show();
}
});
})( jQuery );
debugger;
$("#combobox_Product").val("");
$(function() {
$( "#combobox_Product" ).combobox();
$( "#toggle" ).click(function() {
	
	
$( "#combobox_Product" ).toggle();
});
});

$(function() {
	$( "#combobox_SubProduct" ).combobox();
	$( "#toggle" ).click(function() {
		
		
	$( "#combobox_SubProduct" ).toggle();
	});
	});

$(function() {
	$( "#combobox_ChildProduct" ).combobox();
	$( "#toggle" ).click(function() {
		
		
	$( "#combobox_ChildProduct" ).toggle();
	});
	});

	});


function loadSubProds(prodId,siteId,typeOfWork) {debugger;
	prodId = prodId.split("@@")[0];
	 $.ajax({
			type: 'POST',
			//dataType: 'json',
			url: 'workOrderSubProducts.spring',
			data: {'mainProductId':prodId, 'siteId':siteId, 'typeOfWork':typeOfWork, 'requestFrom':'BOQ'},
			success : function(data) {
				//alert("Success");  
				var trHTML = '';
				  //var dataLen = data.length;
				 // alert(dataLen);
				  var spltData = data.split("|");
				
	              for (i=0; i<spltData.length; i++) {
	            	  data = spltData[i].split("_");
	            	  if(data[0] != "" && data[0] != null && data[0] != '') {
	            		  trHTML += '<option value=' + data[0] + '@@'  + data[1] + '>'+  data[1] + '</option>' ;
	            	  }
	            	 // <option value="<%= strProductId+ "@@"+ strProductName%>"><%= strProductName%></option>
	              
	              }	
	             // alert(trHTML);
	              
	              $('#combobox_SubProduct').empty();
	             $('#combobox_SubProduct').append(trHTML);
				},  
			error : function(e) {  
				alert('Error: ' + e);   
				} 
		});
}


function loadChildProds(prodId,siteId,typeOfWork) {debugger;
	prodId = prodId.split("@@")[0];
	 $.ajax({
			type: 'POST',
			//dataType: 'json',
			url: 'workOrderChildProducts.spring',
			data: {'subProductId':prodId, 'siteId':siteId, 'typeOfWork':typeOfWork, 'requestFrom':'BOQ'},
			success : function(data) {debugger;
				//alert("Success");  
				var trHTML = '';
				 // var dataLen = data.length;
				 // alert(dataLen);
				  var spltData = data.split("|");
				
	              for (i=0; i<spltData.length; i++) {
	            	  data = spltData[i].split("_");
	            	  if(data[0] != "" && data[0] != null && data[0] != '') {
	            		  trHTML += '<option value=' + data[0] + '@@'  + data[1] + '>'+  data[1] + '</option>' ;
	            	  }
	            	 // <option value="<%= strProductId+ "@@"+ strProductName%>"><%= strProductName%></option>
	              
	              }	
	             // alert(trHTML);
	              
	              $('#combobox_ChildProduct').empty();
	             $('#combobox_ChildProduct').append(trHTML);
				},  
			error : function(e) {  
				alert('Error: ' + e);   
				} 
		});
}
function myCumlative(){
	comboproduct = $("#combobox_Product").val();
	if(comboproduct == "" && comboproduct == null){
		alert("Please fill the product");
		return false;
	}
}

