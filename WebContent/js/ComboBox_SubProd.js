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
.addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
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
//var str1 = ele.replace(/.$/,"");

//alert("str1 "+ele);

//alert("okss ");





var prodId = ui.item.option.value;




//alert("ok 1"+prodId);

var  prodName = ui.item.value;

//alert("ok 2 "+prodName);


if(ele == "combobox_Delete_Product") {
	//alert("if matched")
	
loadSubProds(prodId);
}
else if(ele == "product_Delete") {
	//alert("if matched")
	
	getSiteRelatedSubProductsProducts();
}else if(ele == "combobox_Delete_Product1") {
		//alert("if matched")
		
		loadSubProds(prodId);
}else if(ele == "combobox_Product1") {
			//alert("if matched")
			
	loadSubProds(prodId);
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

$(function() {
$( "#combobox_Product" ).combobox();
$( "#toggle" ).click(function() {
$( "#combobox_Product" ).toggle();
});
});
$(function() {
	$( "#product_Add" ).combobox();
	$( "#toggle" ).click(function() {
	$( "#product_Add" ).toggle();
	});
	});
$(function() {
	$( "#combobox_Delete_Product" ).combobox();
	$( "#toggle" ).click(function() {
	$( "#combobox_Delete_Product" ).toggle();
	});
	});

$(function() {
	$( "#product_delete" ).combobox();
	$( "#toggle" ).click(function() {
	$( "#product_delete" ).toggle();
	});
	});

$(function() {
	$( "#combobox_delete_SubProd" ).combobox();
	$( "#toggle" ).click(function() {
	$( "#combobox_delete_SubProd" ).toggle();
	});
	});


	});


function loadSubProds(prodId) {
	
	//alert("loading sub product")
	
	/*var url="indentIssueSubProducts.spring?mainProductId="+prodId;
	  
	if(window.XMLHttpRequest) {
		request = new XMLHttpRequest();	  
	}  
	else if(window.ActiveXObject) {
		request = new ActiveXObject("Microsoft.XMLHTTP");  
	}	  
	try {
		request.onreadystatechange = getSubProdsInfo; 
		request.open("POST", url, false);
		request.send(null); 
	}
	catch(e) {
		alert("Unable to connect to server!");
	}*/
	
	 $.ajax({
			type: 'POST',
			//dataType: 'json',
			url: 'getSubProducts.spring',
			data: {'prodId':prodId},
			success : function(data) {
				//alert("Success");  
				console.log(data);
				var trHTML = '';
				  var dataLen = data.length;
				 // alert(dataLen);
				 
				
	              for (i=0; i<dataLen; i++) {
	            	  trHTML += '<option value=' + data[i].sub_ProductId + '@@'  + data[i].sub_ProductName + '>'+  data[i].sub_ProductName + '</option>' ;
	              
	            	 // <option value="<%= strProductId+ "@@"+ strProductName%>"><%= strProductName%></option>
	              
	              }	
	              //alert(trHTML);
	              
	              $('#combobox_delete_SubProd').empty();
	             $('#combobox_delete_SubProd').html(trHTML);
	             $('#subproduct').empty();
	             $('#subproduct').html(trHTML);
				},  
			error : function(e) {  
				alert('Error: ' + e);   
				} 
		});
	
}

