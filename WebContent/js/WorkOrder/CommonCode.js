
function inrFormat(nStr) { // nStr is the input string
nStr += '';
x = nStr.split('.');
x1 = x[0];
x2 = x.length > 1 ? '.' + x[1] : '';
var rgx = /(\d+)(\d{3})/;
var z = 0;
var len = String(x1).length;
var num = parseInt((len/2)-1);

while (rgx.test(x1)){
if(z > 0){
x1 = x1.replace(rgx, '$1' + ',' + '$2');
}
else{
x1 = x1.replace(rgx, '$1' + ',' + '$2');
rgx = /(\d+)(\d{2})/;
}
z++;
num--;
if(num == 0){
break;
}
}
return x1 + x2;
}
//amount in words 
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
				    if(amount=="0"||amount==0||amount=="0.00"){
				    	return "Zero Rupees Only"; 
				    }

				    return words_string+" Rupees Only";
				}

function validateFileExtention(){
					var ext="";
					var kilobyte=1024;
					var countUploadedFiles=0;
					var len=$('input[type=file]').val().length;
					var count=0;
				
					  $('input[type=file]').each(function () {
					        var fileName = $(this).val().toLowerCase(),
					         regex = new RegExp("(.*?)\.(pdf|jpeg|png|jpg)$");
					        	debugger;
							 if(fileName.length!=0){
					        	if((this.files[0].size/kilobyte)<=kilobyte){
					        		countUploadedFiles++;
								}else{
									if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
								        ext= fileName.substring(fileName.lastIndexOf(".")+1); 
									alert("Please Upload Below 1 MB "+ext+" File");
									count++;
									//alert('Maximum file size exceed, This file size is: ' + this.files[0].size + "KB");
									$(this).val('');
								return false;
								}
					        	
						        if (!(regex.test(fileName))) {
						            $(this).val('');
						            alert('Please select correct file format');
						            count++;
						            return false; 	
						        }
					        }
					    });
					/*  if(countUploadedFiles==0){
							 alert('Please select at least one file.');
							 return false;
						}*/
					  if(count!=0){
						  return false;  
					  }else{
						  return true;
					  }
					 }
				
				/**  thirupathi
				 * 
				 */

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
//dnt remove
				/*var strAamountInWords="";
				function pw( n, ch)
				{
				/////n=n.toFixed(0);
				n=parseInt(n);
					//console.log(n+ " " + ch);
					debugger;
					var one = [ " ", " One", " Two", " Three", " Four", " Five", " Six", " Seven", " Eight", " Nine", " Ten",
							" Eleven", " Twelve", " Thirteen", " Fourteen", "Fifteen", " Sixteen", " Seventeen", " Eighteen",
							" Nineteen"];

					var ten = [ " ", " ", " Twenty", " Thirty", " Forty", " Fifty", " Sixty", " Seventy", " Eighty", " Ninety"];

					if (n > 19)
					{
					var temp=n / 10;
					temp=parseInt(temp.toFixed(0));
						//console.log((temp ) + " " + (n % 10));
						//console.log(ten[temp ] + " " + one[n % 10]);
						
						strAamountInWords+=ten[temp] + " " + one[n % 10];
					}
					else
					{
						//console.log(one[n]);
						strAamountInWords+=one[n];
					}
					if (n > 0){
						//console.log(ch);
						strAamountInWords+=ch;
						}
				}
				var amount=1000;
				main(amount);
				 amount=1512;
				main(amount);
				 amount=10521;
				main(amount);
				 amount=1244521;
				main(amount);

				var amountInWords=[];
				var i=0;
				function main(n)
				{
				debugger;	
				//if(i==0)
				//amountInWords=new Array();

				i++;
				strAamountInWords="";
					if (n <= 0)
					{
						console.log("Enter numbers greater than 0");
					}
					else
					{
						pw((n / 1000000000), " Hundred");
						
				debugger;	
				debugger;	
						pw((n / 10000000) % 100, " crore");
				debugger;	
						pw(((n / 100000) % 100).toFixed(2), " lakh");
						//console.log(((n / 100000) % 100));
				debugger;	
						pw(((n / 1000) % 100).toFixed(2), " thousand");
				debugger;			
						pw(((n / 100) % 10).toFixed(2), " hundred and ");
				debugger;			
						pw((n % 100).toFixed(2), " ");
						
					}
					document.getElementById("AmountInWords").innerHTML+="<br>"+strAamountInWords+" Rs." +" \n "+n+"<br>";
				}*/