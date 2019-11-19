<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<html>
<head>
<h1>Product Details</h1> 
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<jsp:include page="CacheClear.jsp" />  
<title>Insert title here</title>
</head>
<body>
<a href="AddProductDetails.spring">Add Product</a> 
<form action="ViewProduct.spring" method="post">


<table border="2" width="70%" cellpadding="2">  
			<tr>

				<th>
					Product 
				</th>
				<th>
					Sub-Product
				</th>
				<th>
					Child-Product
				</th>
			</tr>

			<%
				List<Map<String, Object>> totalProductHistory = (List<Map<String, Object>>) request.getAttribute("TotalProductList");
				
			if(totalProductHistory!=null){
				
				String product_Name = "";
				String sub_Product = "";
				String child_product = "";
			
			for (Map ProductDetails : totalProductHistory) {
				
			
					
					product_Name =  ProductDetails.get("product_name") == null ? "" : ProductDetails.get("product_name").toString();
					sub_Product =  ProductDetails.get("sub_product") == null ? "" : ProductDetails.get("sub_product").toString();
					child_product =  ProductDetails.get("child_product") == null ? "" : ProductDetails.get("child_product").toString();
				
					
					
					out.println("<tr>");
					out.println("<td>");
					out.println("<a href=\"GetProductDetails.spring?productName="+product_Name+"&brandName="+product_Name+"&companyName="+product_Name+"\">"+product_Name+"</a>");
					out.println("</td>");

					//out.println("<br></br>");

					out.println("<td>");
					out.println("<a href=\"GetProductDetails.spring?productName="+sub_Product+"&brandName="+sub_Product+"&companyName="+sub_Product+"\">"+sub_Product+"</a>");
					out.println("</td>");

					out.println("<td>");
					out.println("<a href=\"GetProductDetails.spring?productName="+child_product+"&brandName="+child_product+"&companyName="+child_product+"\">"+child_product+"</a>");
					out.println("</td>");
					out.println("</tr>");

				}
			}
			%>
		</table>


</form>
</body>
</html>