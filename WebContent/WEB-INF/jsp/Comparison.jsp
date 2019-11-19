<%@page import="java.util.Map"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="java.util.List,java.util.ArrayList,java.util.LinkedList,java.util.Collections,java.util.Comparator,java.util.LinkedHashMap"%>
<%@page import="java.util.Set,java.util.HashSet,java.util.SortedSet,java.util.TreeSet"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map,java.util.TreeMap"%>
<%@page import="java.util.Map,java.util.HashMap"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.util.*"%>
<%@page import="java.text.Format"%>
<%-- <%@page import="java.util.Map,java.util.TreeMap"%> --%>


<!DOCTYPE html>
<html>
<head>
<jsp:include page="CacheClear.jsp" />  
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!-- <link href="css/bootstrap.min.css" rel="stylesheet"> -->
		<!-- Font Awesome -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet">		
<title>Comparison Sheet</title>

<style>
.signAut{width:33.3% !important;float:left !important;text-align:center;}
.clearfixclass{clear:both !important;}
.btncls{
    width: 110px;
    margin-top: 20px;
    height: 35px;
    background: #f38120;
    padding: 10px;
    border-radius: 9px;
    border: 1px solid #f38120;
    color: #fff;

}
.empty-border{
border:none;
}
.border-none{
border: none;
}
	table{ 	
		margin:6px auto;
		border:0;
		border-spacing:0;
	}
	table tr td{
		border:1px solid #000;
		padding:8px;
		height:20px;
		text-align:center;
		font-weight:bold;
		min-width:36px;
		font-size:14px;
	}
	.text-center{
		text-align:center;
	}
	.text-right{
		text-align:right;
	}
	.text-left{
		text-align:left;
	}
	.lasttd{
		height: 135px;
		text-align: center;
		vertical-align: bottom;
	}
	.br0{
		border-right:0;
	}
	.bt0{
		border-top:0;
	}
	.bb0{
		border-bottom:0;
	}
	.bl0{
		border-left:0;
	}
	.ht150{
		height:150px ;
	}
	.verticalbott{
		vertical-align:bottom;
	}
	.verticaltop{
		vertical-align:top;
	}
	.verticalmiddle{
		vertical-align:middle
	}
	.width11{
		width:11%;
	}
	.width20{
		width:20%;
	}
	.width85{
		min-width:85px;
	}
	.width60{
		min-width:60px
	}
	.width75{
		min-width:75px;
	}
	.pbl0{
		padding-bottom:0;
		padding-left:0;	
	}
	.pbr0{
		padding-bottom:0;
		padding-right:0;
	}
	.pb0{
		padding-bottom:0;
	}
	.fontnormal{
		font-weight:normal;
	}
	.width160{
		min-width:160px;
	}
	.ptitle{
		font-size:21px;
		font-weight:600;
		text-align:center;
	}
	.totalamt{
		font-size:18px;
	}
	.trheadings td{
		font-size:16px;
	}
	.n-b td{font-weight:200}
	.site_title{
	    margin-left: 238px;
	}
	.color-change{
	color:red;
	}
	.alignt-text{
	margin-left: 303px;
	}
	.alignt-text1{
	margin-left: 220px;
	}
/* 	.l3{
	display: none;
	} */
	
/* code written by thirupathi  */

.addbtncls{
    padding: 5px;
    width: 40px;
    margin-left:10px;
}
.conclusiontxt{
        float: left;
    width: 440px;
    padding: 5px;
    border-radius: 5px;
    /* border-width: 0px 0px 1px 0px; */
    border: 1px solid #ccc;
    height: 20px;
}
.tooltip {
    text-decoration:none;
    position:relative;
    cursor:pointer;
}
.tooltip div {
    display:none;
    z-index:99999;
}
.tooltip:hover div {
    display:block;
    position:fixed;
    overflow:hidden;
}
@media print{
	#printpage, #Home_img, .hideInprintCls, #saveData, .hideInPrint{
	display:none;
	}
	   .showInPrintforConclusion{
	display:block !important;
   }
}

.showInPrintforConclusion{
display:none;
}
</style>
</head>

<body>
	<div class="loginbox" id="Home_img">
					<div style="border: 0;" class=" nav_title">
								<a class="site_title" href="dashboard.spring"><img src="images/home.png"></a>
				</div>
			</div>
<div id="table-div">
   
                     

            <div class="header-content">
            <div><img src="images/logo.png" style="width:150px; position: absolute;"/></div>
            <h2 style="margin-left: 209px;margin-top: 16PX;">Sumadhura Infracon Pvt Ltd</h2>
             <h3 style="margin-left: 209px;">Project Name: Sumadhura ${siteName}</h3>
              <h4 style="margin-left: 209px;">Price Comparison List(Materials:...)</h4>
              <h5 style="margin-left: 209px;">Indent Date:  ${indentDate}</h5>
            
            </div>
         	 <tr>
           </tr>
            
            <table style="border-collapse: collapse;">
            
            
            
            
  <tr>
  <td colspan="4"></td>
   <%
                            List<Map<String, Object>> totalProductList =  null;
   				Format format = com.ibm.icu.text.NumberFormat.getNumberInstance(new Locale("en", "in"));
                            double  AmountAftertax  = 0.0;
                            double amountAfterDiscount = 0.0;
                            double taxAmount = 0.0;
                            totalProductList = (List<Map<String, Object>>) request.getAttribute("productList");
                           // System.out.println("totalProductList: "+totalProductList);
            				//creating TreeMap  vendorName as key and totalBasicAmount as value
            				Map<String,Double> map = new TreeMap<String,Double>();
            				Map<String,Double> map2 = new TreeMap<String,Double>();
            				Map<String,Double> taxAmountMap = new TreeMap<String,Double>();
            				List<String> notCompletedVendorList = new ArrayList<String>();
            				Map<String,Double> notCompletedVendorMap = new TreeMap<String,Double>();
            				if (totalProductList != null) {
                        		for (Map<String, Object> prods : totalProductList) {
                        			String vendor_Name = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
                        		   amountAfterDiscount = Double.parseDouble((prods.get("AMOUNT_AFTER_DISCOUNT") == null || prods.get("AMOUNT_AFTER_DISCOUNT").equals("NaN")) ? "0" : prods.get("AMOUNT_AFTER_DISCOUNT").toString());
                        			
                        			//amountAfterDiscount =  Double.valueOf(prods.get("AMOUNT_AFTER_DISCOUNT")==null ? "" :   prods.get("AMOUNT_AFTER_DISCOUNT").toString());
                        		
					                AmountAftertax =  Double.valueOf((prods.get("AMOUNT_AFTER_TAX")==null || prods.get("AMOUNT_AFTER_TAX").equals("NaN")) ? "0" :   prods.get("AMOUNT_AFTER_TAX").toString());
					                if(AmountAftertax==0){if(!notCompletedVendorList.contains(vendor_Name)){notCompletedVendorList.add(vendor_Name);}}
					               // amountAfterDiscount =  Double.valueOf(prods.get("AMOUNT_AFTER_DISCOUNT")==null ? "" :   prods.get("AMOUNT_AFTER_DISCOUNT").toString());
					               System.out.println(AmountAftertax+"-"+amountAfterDiscount); 
					               taxAmount  = AmountAftertax - amountAfterDiscount;
					
					               
					                taxAmount =Double.parseDouble(new DecimalFormat("##.##").format(taxAmount));
                        			double total_amount = Double.parseDouble(prods.get("TOTAL_AMOUNT") == null ? "0" : prods.get("TOTAL_AMOUNT").toString());
                        			if(map.containsKey(vendor_Name)){
                        				double value = map2.get(vendor_Name);
                        				map2.put(vendor_Name,amountAfterDiscount+value);
                        				double value2 = map.get(vendor_Name);
                        				map.put(vendor_Name,AmountAftertax+value2);
                        				double prevTaxValue = taxAmountMap.get(vendor_Name);
                        				taxAmountMap.put(vendor_Name,taxAmount+prevTaxValue);
                        			}
                        			else{
                        				map2.put(vendor_Name,amountAfterDiscount);
                        				map.put(vendor_Name,AmountAftertax);
                        				taxAmountMap.put(vendor_Name,taxAmount);
                        				
                        			}
                        		}
                        	}
            				for(String notCompletedVendor : notCompletedVendorList)
            				{
            					notCompletedVendorMap.put(notCompletedVendor, map.get(notCompletedVendor));
            					map.remove(notCompletedVendor);
            				}
            				//System.out.println("amountAfterTaxMap before sorting -> "+map);
            				//System.out.println("amountAfterDiscountMap before sorting -> "+map2);
            				//System.out.println("taxAmountMap before sorting -> "+taxAmountMap);
            				//------------ Sorting treemap based on values
            				List<Map.Entry<String, Double>> list =
            		                new LinkedList<Map.Entry<String, Double>>(map.entrySet());
            				Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            		            public int compare(Map.Entry<String, Double> o1,
            		                               Map.Entry<String, Double> o2) {
            		                return (o1.getValue()).compareTo(o2.getValue());
            		            }
            		        });
            				Map<String, Double> sortedMap = new LinkedHashMap<String, Double>();
            		        for (Map.Entry<String, Double> entry : list) {
            		            sortedMap.put(entry.getKey(), entry.getValue());
            		        }
            		        for (int i=0;i<notCompletedVendorMap.size();i++) {
            		            sortedMap.put(notCompletedVendorList.get(i),notCompletedVendorMap.get(notCompletedVendorList.get(i)));
            		        }
            		        map=sortedMap;
            		        System.out.println("treemap after sorting -> "+map);
            		        //------------
            
                        %>
           
    <% 
    {
    	
      double subTotal = 0;
      double TotalTaxAmount = 0;
      double grandTotal = 0;
    	
    	
    
	for(int i=0;i<map.size();i++) {
		
		String vendor_Name =  String.valueOf(map.keySet().toArray()[i]);
	   out.println("<td colspan='4' style='color:red'>"+vendor_Name+"</td>");
   }%>
   <td style="text-align: center;color:red;" colspan="3">Least  price for Product</td>
   <!-- <td></td>
   <td></td> -->
    </tr>
 
  <tr    style=" background-color: #c4ccdc;">
	   <td>SL NO</td>
	   <td>Item Description</td>
	   <td>Unit</td>
	   <td>Qty/unit</td>
    
    <% 
   for(int i=0;i<map.size();i++){
    
	  
	  
	   out.println("<td>MRP/Unit</td>");
	   out.println(" <td>Disc%</td>");
	   out.println(" <td>Disc rate/unit</td>");
	   out.println(" <td>Amount</td>");
   }
    %>
    <th style="border:1px solid #000;">Vendor Name</th>
    <th style="border:1px solid #000;">Disc rate/unit</th>
    <th style="border:1px solid #000;">Amount</th>
  </tr>
  
  
  <% 
  
 
		
	

			if (totalProductList != null) {
              
				String tool_tip_id = "";
				String childProductId = "";
				String vendor_Name = "";
				String least_price_vendor = "";
				String least_discRatePerUnit = "";
				String least_amountAfterDiscount = "";
				String strProductName  = "";
				String customer_Item_Description = "";
				String Uunit = "";
				String strQuantity = "";
				String strCustomerQuantity = "";
				String customer_Unit = "";
				String price = "";
				String basic_Amount = "";
				
				String discuount = "";
				double discountAmount = 0.0;
				double discRatePerUnit = 0.0;
				
				String tax = "";
				
				String strTotalAmount = "";
				int intCount = 0;
				List<String> vendorList = new ArrayList<String>();
				String changedValue="";
				String strPrevVendorName = "";
				Map<String,String> hashMap = new HashMap<String,String>();
				List<String> childProductList = (List<String>) request.getAttribute("childProductList");
				int productCount = 0;
				for(String childProductId_main : childProductList){
					out.println("<tr>");
					productCount++;
					intCount =0;
			 for(int i=0;i<map.size();i++)    {  
				String l_count = "L"+(i+1);
	          	String vendor_name_key = String.valueOf(map.keySet().toArray()[i]);
	          	//String childProductId_main = childProductList.get(0);
	           	
				for(Map<String, Object> prods : totalProductList) {

					tool_tip_id = prods.get("TOOL_TIP_ID")==null ? "" :   prods.get("TOOL_TIP_ID").toString();
					childProductId = prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString();
					strProductName = prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString();
					vendor_Name =  prods.get("VENDOR_NAME")==null ? "" :   prods.get("VENDOR_NAME").toString();
					customer_Item_Description = prods.get("CHILDPROD_CUST_DESC")==null ? "0" :   prods.get("CHILDPROD_CUST_DESC").toString();
					Uunit =  prods.get("MEASUREMENT_NAME")==null ? "" :   prods.get("MEASUREMENT_NAME").toString();
					strQuantity = prods.get("PURCHASE_DEPT_REQ_QUANTITY")==null ? "" :   prods.get("PURCHASE_DEPT_REQ_QUANTITY").toString();
					strCustomerQuantity =  prods.get("VENDOR_MENTIONED_QTY")==null ? "" :   prods.get("VENDOR_MENTIONED_QTY").toString();
					price = prods.get("PRICE")==null ? "0" :   prods.get("PRICE").toString();
					basic_Amount =  prods.get("BASIC_AMOUNT")==null ? "0" :   prods.get("BASIC_AMOUNT").toString();
					discuount = prods.get("DISCOUNT")==null ? "0" :   prods.get("DISCOUNT").toString();
					amountAfterDiscount =  Double.valueOf(prods.get("AMOUNT_AFTER_DISCOUNT")==null ? "0" :   prods.get("AMOUNT_AFTER_DISCOUNT").toString());
					
					discountAmount =    Double.valueOf(basic_Amount) - amountAfterDiscount;
					
					discRatePerUnit = amountAfterDiscount/Double.valueOf(strQuantity);
					
					tax = prods.get("TAX")==null ? "" :   prods.get("TAX").toString();
					AmountAftertax =  Double.valueOf(prods.get("AMOUNT_AFTER_TAX")==null ? "0" :   prods.get("AMOUNT_AFTER_TAX").toString());
					
					
					taxAmount  = AmountAftertax - amountAfterDiscount;
					
					strTotalAmount = prods.get("TOTAL_AMOUNT")==null ? "0" :   prods.get("TOTAL_AMOUNT").toString();

					if(vendor_Name.equals(vendor_name_key)&&childProductId.equals(childProductId_main)){
						
						least_price_vendor =  prods.get("LEAST_PRICE_VENDOR")==null ? "" :   prods.get("LEAST_PRICE_VENDOR").toString();
						least_discRatePerUnit =  prods.get("LEAST_DISC_RATE_PER_UNIT")==null ? "" :   prods.get("LEAST_DISC_RATE_PER_UNIT").toString();
						least_amountAfterDiscount =  prods.get("LEAST_AMOUNT_AFTER_DISCOUNT")==null ? "0.00" :   prods.get("LEAST_AMOUNT_AFTER_DISCOUNT").toString();
						least_discRatePerUnit=String.format("%.2f",Double.valueOf(least_discRatePerUnit));
						least_amountAfterDiscount=String.format("%.2f",Double.valueOf(least_amountAfterDiscount));
							
							
						intCount = intCount+1;
						System.out.println(" intCountt value "+intCount);
                         if(intCount == 1){
	                           System.out.println(" entered in int count "+map);
	 
                                 out.println("<td>"+productCount+"</td>");
                                 out.println("<td>");
                                 out.println("<span id='childSpan"+tool_tip_id+"' style='color:blue;'  class='tooltip' onmousemove='showTitle(event, "+tool_tip_id+")' onclick='openTitleNewWindow("+tool_tip_id+")'>"+strProductName+"<div id='titileDiv"+tool_tip_id+"'></div></span>");                                 
                                 out.println("</td>"); 
                                 out.println("<td>");
                                 out.println(Uunit);
                                 out.println("</td>"); 
                                 out.println("<td>");
                                 out.println(String.format("%.2f",Double.valueOf(strQuantity)));
                                 out.println("</td>");  
                           }
 
                    System.out.println(" skipped in int count "+map);
 
	                out.println("<td>");
	                out.println(price.equals("0")?"0.00":String.format("%.2f",Double.valueOf(price)));
	                out.println("</td>"); 
	
	                out.println("<td>");
	                out.println(price.equals("0")?"0.00":String.format("%.2f",Double.valueOf(discuount)));
	                out.println("</td>"); 
	
	
	                 out.println("<td>");
	                 out.println(price.equals("0")?"0.00":String.format("%.2f",discRatePerUnit));
	                 out.println("</td>"); 
	                 out.println("<td>");
	                 changedValue=price.equals("0")?"0.00":String.format("%.2f",amountAfterDiscount);
	                 if(!changedValue.equals("")){
	                	 out.println(format.format(Double.valueOf(changedValue))); 
	                 }else{
	                	 out.println(changedValue);
	                 }
	                 
	                 out.println("</td>"); 
	                  
					}
					
				}//ffor
			 }//for
			 out.println("<td>");
             out.println(least_price_vendor);//Least price for Product
             out.println("</td>");
             out.println("<td>");
             out.println(least_discRatePerUnit);//Disc rate/unit
             out.println("</td>");
             out.println("<td>");
             out.println(format.format(Double.valueOf(least_amountAfterDiscount)));//Amount
             out.println("</td>");
			 out.println("</tr>"); 
			}//childProdList for
			}//if
%>
  
 
  <tr>
 <td bgcolor="#FFFFFF" style="line-height:10px;border:none;" colspan=2>&nbsp;</td>
  <td bgcolor="#FFFFFF" style="line-height:10px;" colspan=2>Sub total</td>
  
 <%--  <%  for(int i=0;i<totalProductList.size();i++){ %>
  <td bgcolor="#FFFFFF" style="line-height:10px;border:none;"colspan=3>&nbsp;</td>
  <% 
  out.println("<td bgcolor='#FFFFFF' style='line-height:10px;' colspan=1>&nbsp;");
  out.println( subTotal);
  out.println("</td>"); 
   }%>
 
  
  </tr> --%>
  
  
      <% 
        
        
        String vendor_Name = "";
      	String strSubTotal="";
        for(int i=0;i<map.size();i++)    {  
	          	String vendor_name_key = String.valueOf(map.keySet().toArray()[i]);
	          		strSubTotal=String.format("%.2f",map2.get(vendor_name_key));
		               out.println("<td bgcolor='#FFFFFF' style='line-height:10px;border:none;'colspan=3>&nbsp;</td>");
		                out.println(" <td bgcolor=''#FFFFFF' style='line-height:10px;' colspan=1>&nbsp; "+format.format(Double.valueOf(strSubTotal))+"</td>");
				
        }
		
		%>
	<td style="border:none;"colspan=2></td>
	<!-- <td style="border:1px solid #000;"></td> -->
	<td style="border:1px solid #000;"><%=format.format(Double.valueOf(request.getAttribute("LeastAmountAfterDiscountForAllProducts").toString()))%></td>
    <tr>
  <td bgcolor="#FFFFFF" style="line-height:10px;border:none;" colspan=2>&nbsp;</td>
  <td bgcolor="#FFFFFF" style="line-height:10px;" colspan=2>GST tax</td>
   <% 
        
        
       
        for(int i=0;i<map.size();i++)    {  
	          	String vendor_name_key = String.valueOf(map.keySet().toArray()[i]);
				
		               out.println("<td bgcolor='#FFFFFF' style='line-height:10px;border:none;'colspan=3>&nbsp;</td>");
		                out.println(" <td bgcolor=''#FFFFFF' style='line-height:10px;' colspan=1>&nbsp; "+String.format("%.2f",taxAmountMap.get(vendor_name_key))+"</td>");
				
        }
		
		%>
    <td style="border:none;"colspan=2></td>
	<!-- <td style="border:1px solid #000;"></td> -->
	<td style="border:1px solid #000;"><%=String.format("%.2f",Double.valueOf(request.getAttribute("LeastGSTAmountForAllProducts").toString()))%></td>
  </tr>

    <tr>
       <td bgcolor="#FFFFFF" style="line-height:10px;border:none;" colspan=2>&nbsp;</td>
       <td bgcolor="#FFFFFF" style="line-height:10px;" colspan=2>Freight</td>
        <%  for(int i=0;i<map.size();i++){ %>
            <td bgcolor="#FFFFFF" style="line-height:10px;border:none;"colspan=3>&nbsp;</td>
            <td bgcolor="#FFFFFF" style="line-height:10px;" colspan=1>&nbsp; 0.00 </td>
        <%  }%>
   <td style="border:none;"colspan=2></td>
	<!-- <td style="border:1px solid #000;"></td> -->
	<td style="border:1px solid #000;">0.00</td>
  </tr>

  <tr>
         <td bgcolor='#FFFFFF' style='line-height:10px;border:none;' colspan=2>&nbsp;</td>
         <td bgcolor='#FFFFFF' style='line-height:10px;' colspan=2>Grand Total</td>
        <% 
        
        
       // String vendor_Name = "";
        String strGrandTotal="";
        for(int i=0;i<map.size();i++)    {  
	          	String vendor_name_key = String.valueOf(map.keySet().toArray()[i]);
	          		strGrandTotal=String.format("%.2f",map.get(vendor_name_key));
		               out.println("<td bgcolor='#FFFFFF' style='line-height:10px;border:none;'colspan=3>&nbsp;</td>");
		                out.println(" <td bgcolor=''#FFFFFF' style='line-height:10px;' colspan=1>&nbsp; "+format.format(Double.valueOf(strGrandTotal)) +"</td>");
				
        }
		
		%>
	<td style="border:none;"colspan=2></td>
	<!-- <td style="border:1px solid #000;"></td> -->
	<td style="border:1px solid #000;"><%=format.format(Double.valueOf(request.getAttribute("LeastTotalAmountForAllProducts").toString()))%></td>
  </tr>
  <tr>
		<td bgcolor='#FFFFFF' style='line-height:10px;border:none;' colspan=2>&nbsp;</td>
	    <td bgcolor='#FFFFFF' style='line-height:10px;' colspan=2>FinalDecision</td>
		 
		
		
		 <%  
		 
		 int level = 1;
		 for(int i=0;i<map.size();i++){ 
		   out.println("<td bgcolor='#FFFFFF' style='line-height:10px;'colspan=3>L "+ level+"</td>");
		   out.println("<td bgcolor='#FFFFFF' style='line-height:10px;' colspan=1>&nbsp;</td>");		 
		   level = level+1;
		 }
		 
			}
		   %>								
    <td style="border:1px solid #000;"></td>
	<td style="border:1px solid #000;"></td>
	<td style="border:1px solid #000;"></td>    
    </tr>
    
</table>
            
 
  </div>
  
   <!-- tool tip table - START -->
   <c:forEach var="i" begin="1" end="${noOfProducts}" step="1">
  
	
   <div class="table table-responsive" id="toolTipTable${i}" style="display:none;">
    	<table class='table table-new WDDwtailsTablecls'  style='border:1px solid #000; background: #f2f2f2;'>
    		<thead style="background: #c5c9da;">
		    	<tr>
			    	<th class="text-center" style='border:1px solid #000;font-size: 14px;'>Price per unit after Tax (In INR)</th>
			    	
			    	<c:forEach items="${firstRowOfsitesMap[i]}" var="site"> 
			    		<c:set var="Parts" value="${fn:split(site, '@@')}" />
			    			<th class="text-center" style='border:1px solid #000;font-size: 14px;' colspan="${Parts[1]}">${Parts[0]}</th>
			    	</c:forEach>
			    	
		    	</tr>
    		</thead>
    		<tbody>
    			<tr>
    				<td class="text-center">In Present PO</td>
    				
    				<c:forEach items="${secondRowOfMonthsMap[i]}" var="month"> 
    					<td class="text-center" style="font-size: 12px;font-weight:700;">${month}</td>
    				</c:forEach>    				
    			</tr>
    			
    			<c:forEach items="${lastRowsOfPricesMap[i]}" var="listOfPrices"> 
    				<tr><!-- <td>000</td> -->
    					<c:forEach items="${listOfPrices}" var="price"> 
    						<td class="text-center" style="font-size: 12px;font-weight:700;">${price}</td>
    					</c:forEach>
    				</tr>
    			</c:forEach> 
    		</tbody>    	
    	</table>
    </div>
    
    </c:forEach>
    <!-- tool tip table - END -->
 <!-- conclusions - start -->
 <c:if test="${not empty conclusions}">
 <div class="" style="font-family: Calibri">
 	<div class="col-md-12" id="conclusionHeader"style="font-family: Calibri;margin-top: 120px; margin-left: 10%;font-weight:1000;">Conclusions:</div>
 	<!-- <div id="addconclusiontxt">
 	
 	
 	</div> -->
 	<div class="col-md-6 conclusionMainDivCls" style="margin-left: 12%;margin-top:10px;" id="conclusionMainDiv1">
 	<!-- <div class="hideInprintCls">
 		<div class="col-md-10">
 			<input type="text" class="form-control conclusiontxt" id="conclusionId1">
 		</div>
 		<div class="col-md-2">
 			<button type="button"  id="conclusionAddBtn"  onclick="appendConclusionTxtBx()" class="addbtncls "><i class="fa fa-plus"></i></button>
 		</div>
 		</div> -->
 		<div class="">
 		<ol>
 			<c:forEach items="${conclusions}" var="conclusion"> 
 				<li>${conclusion}</li>
 			</c:forEach>
 		</ol>
 		</div>
 	</div>    
 </div> 
 </c:if>
 <!-- conclusions - end -->
    
    <div class="regards-content" style="font-family: Calibri;margin-top: 120px;width:100%; ">
     <div class="signAut" >Authorized Signatory</div>
     <div class="signAut" id="preparedBy">Prepared By</div>    
     <div class="signAut" id="verifedBy">Verified By</div>    
    </div>
    <div class="clearfixclass"></div>
    
    
      <% String StrReceiverState = request.getAttribute("receiverState") == null ? "" : request.getAttribute("receiverState").toString();



        if(StrReceiverState.equalsIgnoreCase("Telangana")){

    %>

     <div class="footer-content">
     <h4 style="text-decoration: underline;font-family: Calibri;margin-top: 68px;">SUMADHURA INFRACON PRIVATE LIMITED</h4>
     <span style="font-family: Calibri;"> Sy no 148-2E,Nanakram Guda,Serilingampally Mandal, Hyderabad, Telangana 500008</span><br>
     <span style="font-family: Calibri;"> URL: Sumadhuragroup.org <span style="font-weight:bold;">CIN </span>: U45200KA2012PTC062071</span><br>

     </div>


     <%}else{ %>
     <div class="footer-content">
     <h4 style="text-decoration: underline;font-family: Calibri;margin-top: 68px;">SUMADHURA INFRACON PRIVATE LIMITED</h4>
     <span style="font-family: Calibri;">43, CKB Plaza, 2nd Floor, Varthur Main Road, Marathahalli, Bengaluru, Karnataka 560037</span><br>
     <span style="font-family: Calibri;">Phone:080-421621470, E info@sumadhuragroup.org, URL: Sumadhuragroup.org, CIN:U45200KA2012PTC062071</span><br>

     </div>

     <%} %>
    
    <div style="text-align: center;">
       <!--  <button onclick="save()" class="btn btn-warning btncls print-style" id="saveData">Save</button> -->
    	<button onclick="myFunction()" class="btn btn-warning btncls print-style" id="printpage">Print</button>     
    </div>
    
    		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/indentReceive.js" type="text/javascript"></script>
		
	<script>
	function showTitle(e, id){debugger;
		$("#titileDiv"+id).html($("#toolTipTable"+id).html());
		  var x = e.clientX,
	        y = e.clientY;
		$("#titileDiv"+id).css({"top" :(y + 14) + 'px'});
		$("#titileDiv"+id).css({"left" :(x + 10) + 'px'});
	}
	function openTitleNewWindow(id){
		var table=$("#toolTipTable"+id).html();
		if(table==undefined){
			return false;
		}
		var popup = window.open('', 'example', 'width=800,height=400', 'toolbar=0,location=0,menubar=0');
		popup.document.write('<html><head><title>Sumadhura-IMS</title><link rel="shortcut icon" type="image/x-icon" href="images/favicon.jpg">'
			    +'<meta charset="utf-8"><meta name="viewport" content="width=device-width, initial-scale=1">'
			    +'<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.0/css/bootstrap.min.css">'
			    +'<link rel="stylesheet" type="text/css" href="css/style.css">'
			    +'<link rel="stylesheet" type="text/css" href="css/custom.css">'
			    +'<link rel="stylesheet" type="text/css" href="css/loader.css">'
			    +'<style>.tbl{border:1px solid #000;}.tbl thead tr th{border:1px solid #000;}#tblnotification thead tr th, .newwindow-table thead tr th{background-color:#ccc;border:1px solid #000 !important;text-align:center;}.newwindow-table tbody tr td{border:1px solid #000;text-align:center;}.Mrgtop20{margin-top:20px;}.checkboxsites,.checkboxsitesall{height: 24px;width: 24px;vertical-align: bottom;}.loader-ims {position: fixed;left: 50% !important;top: 50% !important; z-index: 99999;}</style>'
			    +'<script src="js/jquery.min.js"><'+'/script>'
			    +'<script src="js/bootstrap.min.js"><\/script>'	
			    +'</head>'
			    +'<body id="printPriceMasterData" style="margin:25px;"><div>'+table+'</div>'			   
				+'</body>'
				+'</html>'); 	
		popup.document.close(); 
	}
	function myFunction() {debugger;
	    window.print();
	}
	var id=1;
	function appendConclusionTxtBx(){
		var firstTextVal=$("#conclusionId1").val();
		if(firstTextVal.trim()==''){
			alert("please enter conclusion");
			$("#conclusionId1").focus();
			return false;
		}
		var lastTextboxId=$(".conclusionMainDivCls").attr("id").split("conclusionMainDiv")[1];
		id++;
		var addConclusionTxtBx=	'<div class="col-md-6 conclusionMainDivCls" id="conclusionMainDiv'+id+'" style="margin-left: 12%;margin-top:10px;"><div class="hideInprintCls"><div class="col-md-10"><input type="text" class="form-control conclusiontxt" id="conclusionId'+id+'"></div><div class="col-md-2"><button type="button"  id="conclusionDeleteBtn"  onclick="deleteConclusionTxt('+id+')" class="addbtncls "><i class="fa fa-remove"></i></button></div></div></div>';
		$("#addconclusiontxt").append(addConclusionTxtBx);
		
		$("#conclusionId"+id).val($("#conclusionId1").val());
		$("#conclusionId1").val('');	
		$("#conclusionId1").focus();		
	}
	function deleteConclusionTxt(id){
		var canIDelete=window.confirm("Do you want to delete a conclusion?");
		if(canIDelete==false){
			return false;
		}
		var id=parseInt(id);
		$("#conclusionMainDiv"+id).remove();
	}
	</script>
</body>
</html>
