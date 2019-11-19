<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.ResourceBundle"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@page import="java.util.List"%>
<%@page import="com.sumadhura.bean.ProductDetails"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<html>

<head>
    <jsp:include page="CacheClear.jsp" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">
    <link href="css/custom.min.css" rel="stylesheet">
    <link href="css/style.css" rel="stylesheet" type="text/css">
    <link href="css/custom.css" rel="stylesheet" type="text/css">
    <link href="css/topbarres.css" rel="stylesheet" type="text/css">
    <link href="js/jquery-ui.css" rel="stylesheet" type="text/css" />
    <link href="js/inventory.css" rel="stylesheet" type="text/css" />
    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.min.js"></script>

    <title>Sumadhura-IMS</title>
    <link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">
    <style>
		@media print{
			.btn-warning, .breadcrumb, .left_col, .nav_menu{
				display:none;
			}
			#debitNoteTable{
				margin-top:-100px !important;
			}
		}
    </style>
</head>

<body class="nav-md" onload="amt_in_words()">
    <noscript>
        <h3 align="center" style="font-weight:bold;">JavaScript is turned off in your web browser. Turn it on and then
            refresh the page.</h3>
        <style>
            #mainDivId {
                display: none;
            }
        </style>
    </noscript>

    <div class="container body" id="mainDivId">
        <div class="main_container" id="main_container">
            <div class="col-md-3 left_col" id="left_col">
                <div class="left_col scroll-view">
                    <div class="clearfix"></div>
                    <jsp:include page="SideMenu.jsp" />
                </div>
            </div>
            <jsp:include page="TopMenu.jsp" />


            <!-- page content -->
            <div class="right_col" role="main">
                <div>
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="#">Home</a></li>
                        <li class="breadcrumb-item active">Inwards From PO</li>
                    </ol>
                </div>
                <div class="loader-sumadhura" style="z-index:99;display:none;">
                    <div class="lds-facebook">
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                        <div></div>
                    </div>
                    <div id="loadingMessage">Loading...</div>
                </div>
				<table id="debitNoteTable"style="border:1px solid #000;width:100%;">
					<tbody>
						<tr>
							<td colspan="4" style="text-align:center;"><img src="images/SumadhuraLogo2015.png"/></td>
							<td colspan="12" style="text-align:center;font-size: 20px;"><span><strong>
							SUMADHURA INFRACON PVT LTD <br>
							<font style="font-size: 12px;">${address}<br>
							GSTIN:${gstinNo} </font>
							</strong></span></td>
						</tr>
						
						<tr>
							<td colspan="16" style="padding: 15px;"></td>
						</tr>
						<tr style="background-color:#ccc;">
							<td colspan="16" style="text-align:center;"><strong>Debit Note</strong></td>
						</tr>
						<tr>
							<td colspan="8">Document No: ${CreditNote.documentNo}</td>
							<td colspan="8">Against invoice: ${InvoiceDetails.invoiceId}</td>
						</tr>
						<tr>
							<td colspan="8">Date of Issue: ${CreditNote.entryDate}</td>
							<td colspan="8">Date of Invoice: ${InvoiceDetails.invoiceDate}</td>
						</tr>
							<tr>
							<td colspan="6">State: ${SiteDetailsFromVendor.siteState}</td>
							<td colspan="1">Code:</td>
							<td colspan="1">${SiteDetailsFromVendor.siteStateCode}</td>
							<td colspan="8"></td>
						</tr>
						<tr>
							<td colspan="16" style="padding: 15px;"></td>
						</tr>
						<tr style="background-color:#ccc;">
							<td colspan="8" style="text-align:center;"><strong>Bill to Party</strong></td>
							<td colspan="8"><strong>Ship to Party</strong></td>
						</tr>
						<tr>
							<td colspan="8">Name:${VendorDetails.vendorName}</td>
							<td colspan="8">Name:${VendorDetails.vendorName}</td>
						</tr>
						<tr>
							<td colspan="8">Address: ${VendorDetails.address}</td>
							<td colspan="8">Address: ${VendorDetails.address}</td>
						</tr>
						<tr>
							<td colspan="8">GSTIN: ${VendorDetails.gsinNumber}</td>
							<td colspan="8">GSTIN: ${VendorDetails.gsinNumber}</td>
						</tr>
						<tr>
							<td colspan="6">State: ${VendorDetails.state}</td>
							<td colspan="1">Code:</td>
							<td colspan="1">${VendorDetails.stateCode}</td>
							<td colspan="6">State: ${VendorDetails.state}</td>
							<td colspan="1">Code:</td>	
							<td colspan="1">${VendorDetails.stateCode}</td>
						</tr>
					</tbody>
					<tbody>
					   <c:if test="${not empty CreditNoteDetails}">
						<tr style="background-color:#ccc;">
						   <th rowspan="2" class="text-center vertical-alignment">S. No.</th>
						   <th rowspan="2" class="text-center vertical-alignment">Product Description</th>
						   <th rowspan="2" class="text-center vertical-alignment">HSN code</th>
						   <th rowspan="2" class="text-center vertical-alignment">UOM</th>
						   <th rowspan="2" class="text-center vertical-alignment">Qty</th>
						   <th rowspan="2" class="text-center vertical-alignment">Rate</th>
						   <th rowspan="2" class="text-center vertical-alignment">Amount</th>
						   <th rowspan="2" class="text-center vertical-alignment">Discount</th>
						   <th rowspan="2" class="text-center vertical-alignment">Taxable Value</th>
						   <th colspan="2" class="text-center">CGST</th>
						   <th colspan="2" class="text-center">SGST</th>
						   <th colspan="2" class="text-center">IGST</th>
						   <th rowspan="2" class="text-center vertical-alignment">Total</th>
						</tr>	
						<tr style="background-color:#ccc;">
						   <th class="text-center">Rate</th>
						   <th class="text-center">Amount</th>
						   <th class="text-center">Rate</th>
						   <th class="text-center">Amount</th>
						   <th class="text-center">Rate</th>
						   <th class="text-center">Amount</th>
						</tr>
					   </c:if>				
					</tbody>	
					<tbody>
					<c:choose>
					<c:when test="${not empty CreditNoteDetails}">
					<c:forEach var="element" items="${CreditNoteDetails}">
						<tr>
							<td>${element.sno}</td>
							<td>${element.childProductName}</td>
							<td>${element.hsnCode}</td>
							<td>${element.measurementName}</td>
							<td style="text-align:right;">${element.receivedQty}</td>
							<td style="text-align:right;">${element.price}</td>
							<td style="text-align:right;">${element.basicAmount}</td>
							<td style="text-align:right;">${element.discount}</td>
							<td style="text-align:right;">${element.taxableValue}</td>
							<td style="text-align:right;">${element.cgstRate}</td>
							<td style="text-align:right;">${element.cgst}</td>
							<td style="text-align:right;">${element.sgstRate}</td>
							<td style="text-align:right;">${element.sgst}</td>
							<td style="text-align:right;">${element.igstRate}</td>
							<td style="text-align:right;">${element.igst}</td>
							<td style="text-align:center;">${element.totalAmount}</td>
						</tr>
					</c:forEach>
					
						
						
						<tr>
							<td colspan="4" style="text-align: center;" class="text-center;">Total</td>							
							<td style="text-align:right;">${Totals.totalQty}</td>
							<td style="text-align:right;"></td>
							<td style="text-align:right;">${Totals.totalBasicAmt}</td>
							<td style="text-align:right;"></td>  <%-- ${Totals.totalDiscount} --%>
							<td style="text-align:right;">${Totals.totalAmountBeforeTax}</td>
							<td style="text-align:right;"></td>
							<td style="text-align:right;">${Totals.addCgst}</td>
							<td style="text-align:right;"></td>
							<td style="text-align:right;">${Totals.addSgst}</td>
							<td style="text-align:right;"></td>
							<td style="text-align:right;">${Totals.addIgst}</td>
							<td style="text-align:center;">${Totals.totalAmountAfterTax}</td>
						</tr>
						<tr>
							<td colspan="9" id="finalAmtInwords">Total amount in words: </td>
							<td colspan="6">Total Amount before Tax</td>
							<td style="text-align:center;">${Totals.totalAmountBeforeTax}</td>
						</tr>
						<tr>
							<td rowspan="5" colspan="9"></td>
							<td colspan="6">Add: CGST</td>
							<td style="text-align:center;">${Totals.addCgst}</td>
						</tr>
						<tr>
							<td colspan="6">Add: SGST</td>
							<td style="text-align:center;">${Totals.addSgst}</td>
						</tr>
						<tr>
							<td colspan="6">Add: IGST</td>
							<td style="text-align:center;">${Totals.addIgst}</td>
						</tr>
						<tr>
							<td colspan="6">Total Tax Amount</td>
							<td style="text-align:center;">${Totals.totalTaxAmount}</td>
						</tr>
						<tr>
							<td colspan="6">Total Amount after Tax:</td>
							<td style="text-align:center;">${Totals.totalAmountAfterTax}</td>
						</tr>
						</c:when>
						
						<c:otherwise>
						<tr>
							<td colspan="9" id="finalAmtInwords">Total amount in words: </td>
							<td colspan="6">Total Amount </td>
							<td style="text-align:center;">${Totals.totalAmountAfterTax}</td>
						</tr>
						</c:otherwise>
						</c:choose>
						
						<tr>
							<td colspan="6">Bank Details</td>
							<td rowspan="4" colspan="3"></td>
							<td colspan="7">Ceritified that the particulars given above are true and correct</td>
						</tr>
						<tr>
							<td colspan="6">Bank A/C:</td>
							<td colspan="7">For Sumadhura</td>
						</tr>
						<tr>
							<td colspan="6">Bank IFSC: </td>
							<td rowspan="2" colspan="7"></td>
						</tr>
						<tr>
							<td rowspan="2" colspan="6">Terms & conditions</td>
						</tr>
						<tr>
							<td colspan="3" style="text-align:center;">Common Seal</td>
							<td colspan="7" style="text-align:center;">Authorised signatory</td>
						</tr>
					</tbody>
							
				</table>
				<div class="col-md-12 text-center center-block" style="margin-bottom:30px;margin-top:30px;">
					<button type="button" class="btn btn-warning" onclick="printPage()">print</button>
				</div>
            </div>
        </div>


    </div>


    <script src="js/custom.js"></script>
    <script src="js/jquery-ui.js" type="text/javascript"></script>
    <script src="js/sidebar-resp" type="text/javascript"></script>
	<script>
		function printPage() {
		    window.print();
		}
		
		function amt_in_words(){debugger;
			var amountInWords = eval('('+'${Totals.totalAmountAfterTax}'+')'); 	
			//var amountInWords=($("#finalAmntDiv").text());
			var nums = amountInWords.toString().split('.');
			console.log(nums.length);
		    var whole = convertNumberToWords(nums[0]);
		    if (nums.length == 2 && nums[1]!=0) {
		        var fraction = convertNumberToWords(nums[1]);
		        
		       // return whole + 'and ' + fraction;
		        $("#finalAmtInwords").append(whole + 'Rupees and ' + fraction +'paisa');
		        
		    } else {
		        //return whole;
		    	 $("#finalAmtInwords").append(whole+'Rupees');
		    }
		}
		
		function convertNumberToWords(amount) {
			debugger;
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
		            	debugger;
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
		    return words_string;
		}
	</script>
</body>

</html>