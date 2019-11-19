//checking number number 
		function isNumberCheck(el, evt) {
			debugger;
		    var charCode = (evt.which) ? evt.which : event.keyCode;
		    var num=el.value;
		    var number = el.value.split('.');
		    if (charCode != 46 && charCode > 31 && (charCode < 48 || charCode > 57) ||  charCode == 13) {
		        return false;
		    }
		    //just one dot
		    if((number.length > 1 && charCode == 46) || ( el.value=='' && charCode == 46)) {
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
		
		//checking number or not on paste
		function checkingNumberOnPaste(id){
			var reqAmnt=$("#"+id).val();
			if(isNaN(reqAmnt)){
				alert("Please enter valid data.");
				$("#"+id).val("");
				$("#"+id).focus();
				return false;
			}
		}