<%@page import="java.util.Map"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page import="java.util.List,java.util.ArrayList,java.util.LinkedList,java.util.Collections,java.util.Comparator,java.util.LinkedHashMap"%>
<%@page import="java.util.Set,java.util.HashSet,java.util.SortedSet,java.util.TreeSet"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map,java.util.TreeMap"%>


<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		
<title>Comparison Sheet</title>

<style>
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
</style>
</head>

<body>
	<div class="loginbox">
					<div style="border: 0;" class=" nav_title">
								<a class="site_title" href="dashboard.spring"><img src="images/home.png"></a>
				</div>
			</div>
<div id="table-div">
    <table class="table table-bordered" style="font-family: Calibri">
        <tbody>
                     
            <tr>
                <td colspan="2" rowspan="4" class="verticalmiddle"><img src="images/logo.png" /></td>
                <td colspan="11" class="text-center">Sumadhura Infracon Pvt Ltd</td>
            </tr>
         	 <tr>
                <td colspan="11" class="text-center">Project Name: <span class="color-change">Sumadhura ${siteName}</span></td>
            </tr>
         	 <tr>
                <td colspan="11" class="text-center">Price and Comparison List(Materials:..)</td>
            </tr>
             <tr>
                <td colspan="11" class="text-center">Indent No:${indentNo}., Indent Date:-<%=new java.util.Date()%></td>
            </tr>

           
        <!--     <tr>
                <td colspan="4" ></td><td colspan="4" ><span class="color-change">TBDB Tech</span></td><td colspan="4"><span class="color-change">Navayuga</span></td><td colspan="4"><span class="color-change l3">Bharathi</span></td>
            </tr> -->

             <tr class="" style="background: #d6cece;">	
                <td>SL NO</td>
                <td>Vendor Name</td>
                <td >Customer Item Description</td>
                <td >Unit</td>
                <td> Quantity</td>
                <td>Customer Quantity</td>
                <td>Price</td>
                <td>Basic Amount</td>
                <td>Discount</td>
                <td>Discount After Amount</td>
                <td>Tax</td>
                <td>Tax After Amount</td>
                <td>Total Amount</td>
              
               
                
            </tr> 
            <div class="prod-wise">
            
            <%
          				    List<Map<String, Object>> totalProductList = (List<Map<String, Object>>) request.getAttribute("productList");
            				//creating TreeMap  vendorName as key and totalBasicAmount as value
            				Map<String,Double> map = new TreeMap<String,Double>();
            				Map<String,Double> map2 = new TreeMap<String,Double>();
            				if (totalProductList != null) {
                        		for (Map<String, Object> prods : totalProductList) {
                        			String vendor_Name = prods.get("VENDOR_NAME") == null ? "" : prods.get("VENDOR_NAME").toString();
                        			double basic_amount = Double.parseDouble(prods.get("BASIC_AMOUNT") == null ? "0" : prods.get("BASIC_AMOUNT").toString());
                        			double total_amount = Double.parseDouble(prods.get("TOTAL_AMOUNT") == null ? "0" : prods.get("TOTAL_AMOUNT").toString());
                        			if(map.containsKey(vendor_Name)){
                        				double value = map.get(vendor_Name);
                        				map.put(vendor_Name,basic_amount+value);
                        				double value2 = map2.get(vendor_Name);
                        				map2.put(vendor_Name,total_amount+value2);
                        			}
                        			else{
                        				map.put(vendor_Name,basic_amount);
                        				map2.put(vendor_Name,total_amount);
                        			}
                        		}
                        	}
            				System.out.println("treemap before sorting -> "+map);
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
            		        map=sortedMap;
            		        System.out.println("treemap after sorting -> "+map);
            		        //------------
            
                        %>
            <%
           		
            			
					
								if (totalProductList != null) {
                                    
									String vendor_Name = "";
									String strProductName  = "";
									String customer_Item_Description = "";
									String Uunit = "";
									String strQuantity = "";
									String strCustomerQuantity = "";
									String customer_Unit = "";
									String price = "";
									String basic_Amount = "";
									
									String discuount = "";
									String discountAmount = "";
									String tax = "";
									String taxAmount = "";
									String strTotalAmount = "";
									int intCount = 0;
									List<String> vendorList = new ArrayList<String>();
								 for(int i=0;i<map.size();i++)    {  
									String l_count = "L"+(i+1);
						           	String vendor_name_key = String.valueOf(map.keySet().toArray()[i]);
						           	double total_BasicAmount = map.get(vendor_name_key);
						           	
									for(Map<String, Object> prods : totalProductList) {

										intCount = intCount+1;
										strProductName = prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString();
										vendor_Name =  prods.get("VENDOR_NAME")==null ? "" :   prods.get("VENDOR_NAME").toString();
										customer_Item_Description = prods.get("CHILDPROD_CUST_DESC")==null ? "0" :   prods.get("CHILDPROD_CUST_DESC").toString();
										Uunit =  prods.get("Measurment_name")==null ? "" :   prods.get("Measurment_name").toString();
										strQuantity = prods.get("INDENT_QTY")==null ? "" :   prods.get("INDENT_QTY").toString();
										strCustomerQuantity =  prods.get("VENDOR_MENTIONED_QTY")==null ? "" :   prods.get("VENDOR_MENTIONED_QTY").toString();
										price = prods.get("PRICE")==null ? "" :   prods.get("PRICE").toString();
										basic_Amount =  prods.get("BASIC_AMOUNT")==null ? "" :   prods.get("BASIC_AMOUNT").toString();
										discuount = prods.get("DISCOUNT")==null ? "" :   prods.get("DISCOUNT").toString();
										discountAmount =  prods.get("AMOUNT_AFTER_DISCOUNT")==null ? "" :   prods.get("AMOUNT_AFTER_DISCOUNT").toString();
										tax = prods.get("TAX")==null ? "" :   prods.get("TAX").toString();
										taxAmount =  prods.get("AMOUNT_AFTER_TAX")==null ? "" :   prods.get("AMOUNT_AFTER_TAX").toString();
										strTotalAmount = prods.get("TOTAL_AMOUNT")==null ? "" :   prods.get("TOTAL_AMOUNT").toString();

								if(vendor_Name.equals(vendor_name_key)){
										if(!vendorList.contains(vendor_Name)){
											vendorList.add(vendor_Name);
											intCount = 1;
											
											
											
											
											out.println("<tr>");
											out.println("<td colspan=\"9\" class=\"text-left\" style=\"color:red;\">"+vendor_Name+"  ("+l_count+")</td>");
											out.println("</tr>");
										}
										
										 	/* out.println("<tr>");
											out.println("<td colspan=\"13\" class=\"text-left\" style=\"color:red;\">"+strProductName+"</td>");
											out.println("</tr>");  */
										
										
										out.println("<tr>");
										out.println("<td>");
										out.println(intCount);
										out.println("</td>"); 
										
										
										out.println("<td>");
										out.println(strProductName);
										out.println("</td>"); 
										
										out.println("<td>");
										out.println(customer_Item_Description);
										out.println("</td>"); 
										
									
										out.println("<td>");
										out.println(Uunit);
										out.println("</td>"); 
										
										out.println("<td>");
										out.println(strQuantity);
										out.println("</td>"); 
										
										
										out.println("<td>");
										out.println(strCustomerQuantity);
										out.println("</td>"); 
										
										out.println("<td>");
										out.println(price);
										out.println("</td>"); 
										
										
										out.println("<td>");
										out.println(basic_Amount);
										out.println("</td>"); 
										
										out.println("<td>");
										out.println(discuount);
										out.println("</td>"); 
										
										
										out.println("<td>");
										out.println(discountAmount);
										out.println("</td>"); 
										
										out.println("<td>");
										out.println(tax);
										out.println("</td>"); 
										
									
										out.println("<td>");
										out.println(taxAmount);
										out.println("</td>"); 
										
										out.println("<td>");
										out.println(strTotalAmount);
										out.println("</td>"); 
										
										out.println("</tr>");
										
										
										
										
										
          /*    <tr>
                <td colspan="9" class="text-left" style="color:red;">Product: Blockwave</td>
            </tr>

	                <tr>
	               <td>01</td>
	               <td>Adminstrative</td>
	               <td>Soham</td>
	               <td>200</td>
	               <td>TBDB Technologies</td>
	               <td>1500</td>
	               <td>10</td>
	               <td>150</td>
	               <td>1350</td>
	                </tr> */
	                
									}
									}
									//here
									out.println("<tr>");
											out.println("<td class='border-none'>");
											out.println();
											out.println("</td>"); 
											
											
											out.println("<td class='border-none'>");
											out.println();
											out.println("</td>"); 
											
											out.println("<td class='border-none'>");
											out.println();
											out.println("</td>"); 
											
										
											out.println("<td class='border-none'>");
											out.println();
											out.println("</td>"); 
											
											out.println("<td class='border-none'>");
											out.println();
											out.println("</td>"); 
											
											
											out.println("<td class='border-none'>");
											out.println();
											out.println("</td>"); 
											
											out.println("<td >");
											out.println("Sub Total");
											out.println("</td >"); 
											
											
											out.println("<td >");
											out.println(total_BasicAmount);
											out.println("</td>"); 
											
											out.println("<td >");
											out.println();
											out.println("</td>"); 
											
											
											out.println("<td >");
											out.println();
											out.println("</td>"); 
											
											out.println("<td style='border-left: none;'>");
											out.println();
											out.println("</td>"); 
											
										
											out.println("<td>");
											out.println("Sub Total");
											out.println("</td>"); 
											
											out.println("<td>");
											out.println(map2.get(vendor_name_key));
											out.println("</td>"); 
											
											out.println("</tr>");
									//upto here
								}
           		 }
					
								//List<Map<String, Object>> totalProductList1 = (List<Map<String, Object>>) request.getAttribute("productList1");
								
								
								 
					
				%>
				
				<%
				
				
				%>
	         </div>       
 <     
	                
      
	                

 	           <!--                 	        <tr>
 	              
 	               <td class="empty-border"></td>
 	               <td class="empty-border"></td>
 	               <td colspan="2">Final Desicion: L1/L2/L3</td>
 	              <td colspan="4">L1</td><td colspan="4">L2</td><td class="l3" colspan="4">L3</td>
 	                </tr> -->
	      <tr>
               
            </tr>

        </tbody>
    </table>
  </div>
    <div class="footer-content" style="font-family: Calibri">
    <p class="alignt-text1">Conclusion: 1. We issued PO to L1 as the discount% is more when compared to L2 and L3.</p>
     <p  class="color-change alignt-text"> 2. We preferred L1 over L2 and L3 because of on time delivery.	</p>
     <p  class="color-change alignt-text"> 3. Ease of procuring materials is best with L1 over L2 and L3.	</p>
      <p  class="color-change alignt-text"> 4. L1 is more approachable than L2 and L3 in terms of doing business.	</p>
    
    </div>
    <div class="regards-content" style="font-family: Calibri;     margin-left: 220px;">Sumadhura Infracon Pvt Ltd</div>
    <div class="regards-content" style="font-family: Calibri;     margin-top: 63px; margin-left: 220px;">
    <span>Authorized Signatory</span>    <span style="margin-left: 334px;">Prepared By</span>    <span  style="margin-left: 300px;">Verified By</span>
    
    </div>
    
    <button onclick="myFunction()" class="btn btn-warning print-style" style="width: 110px; height: 28px;margin-left: 591px;background: #b3b5ff;">Print</button>
    
    		<!-- jQuery -->
		<script src="js/jquery.min.js"></script>
		<!-- Bootstrap -->
		<script src="js/bootstrap.min.js"></script>
		<!-- Custom Theme Scripts -->
		<script src="js/custom.js"></script>
		<script src="js/jquery-ui.js" type="text/javascript"></script>
		<script src="js/indentReceive.js" type="text/javascript"></script>
		
	<script>
	function myFunction() {
	    window.print();
	}
	
	</script>
</body>
</html>
