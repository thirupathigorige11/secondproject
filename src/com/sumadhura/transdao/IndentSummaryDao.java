package com.sumadhura.transdao;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.ProductDetails;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.UIProperties;

@Repository
public class IndentSummaryDao extends UIProperties {


	//	public List<ProductDetails> getProductList(String StrSiteId, HttpSession session) {
	//
	//		List<Map<String, Object>> productList = null;
	//		List<ProductDetails> listProductDetails = new ArrayList<ProductDetails>();
	//		ProductDetails objProductDetails = null;
	//		-
	//		JdbcTemplate template = null;
	//		String productName = "";
	//		String subProductName = "";
	//		String childProductName = "";
	//		String productNameChk = "";
	//		String subProductNameChk = "";
	//		String childProductNameChk = "";
	//		Double recivedQty = 0.0;
	//		Double recivedQtyChk = 0.0;
	//		String measurement = "";
	//		String measurementChk = "";
	//		String issuedQty = "";
	//		String amount = "";
	//		double quantity = 0.0;
	//		String totalAmount = "";
	//		String sql = "";
	//		String strPrice = "";
	//		Double grandTotalAmount = 0.0;
	//		String issueDate="";
	//		StringBuilder strDCAvailabulity = new StringBuilder();
	//		NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
	//		DecimalFormat decimalFormat = new DecimalFormat("##.##");
	//		try {
	//
	//			template = new JdbcTemplate(DBConnection.getDbConnection());
	////this is for invoice details
	//			if (StrSiteId.equalsIgnoreCase("ALL")) {
	//
	//				sql = "SELECT IED.RECEVED_QTY AS QTY,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES, SPL.AMOUNT_PER_UNIT_AFTER_TAXES, SPL.AVAILABLE_QUANTITY, P.NAME AS PRODUCT_NAME,S.NAME AS SUB_PRODUCT_NAME, C.NAME AS CHILD_PRODUCT_NAME, M.NAME AS MESURMENT_NAME,IED.ENTRY_DATE, ST.SITE_NAME FROM INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED WHERE  P.PRODUCT_ID =  IEA.PRODUCT_ID AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND M.MEASUREMENT_ID = IEA.MESURMENT_ID AND ST.SITE_ID = IEA.SITE_ID AND IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND SPL.STATUS='A' AND SPL.INDENT_ENTRY_DETAILS_ID=IED.INDENT_ENTRY_DETAILS_ID ORDER BY IEA.SITE_ID DESC, P.NAME asc, S.NAME asc, C.NAME asc";
	//
	//				
	//			/*	strDCAvailabulity.append("SELECT IED.RECEVED_QTY AS QTY,IED.PRICE, SPL.AMOUNT, SPL.RECIVED_QUANTITY,");
	//				strDCAvailabulity.append("P.NAME AS PRODUCT_NAME,S.NAME AS SUB_PRODUCT_NAME, C.NAME AS CHILD_PRODUCT_NAME, ");
	//				strDCAvailabulity.append("M.NAME AS MESURMENT_NAME, ST.SITE_NAME FROM INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S,");
	//				strDCAvailabulity.append("CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL,DC_FORM IED");
	//				strDCAvailabulity.append("WHERE  P.PRODUCT_ID =  IEA.PRODUCT_ID AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND ");
	//				strDCAvailabulity.append("C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND M.MEASUREMENT_ID = IEA.MESURMENT_ID AND ");
	//				strDCAvailabulity.append("ST.SITE_ID = IEA.SITE_ID AND IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND");
	//				strDCAvailabulity.append("SPL.STATUS='A' AND SPL.DC_FORM_ENTRY_ID=IED.DC_ENTRY_ID ORDER BY");
	//				strDCAvailabulity.append("IEA.SITE_ID DESC, P.NAME asc, S.NAME asc, C.NAME asc");
	//*/
	//			} else {
	//				sql = "SELECT IED.RECEVED_QTY AS QTY, SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.AMOUNT_PER_UNIT_AFTER_TAXES, SPL.AVAILABLE_QUANTITY, P.NAME AS PRODUCT_NAME, S.NAME AS SUB_PRODUCT_NAME, C.NAME AS CHILD_PRODUCT_NAME, M.NAME as MESURMENT_NAME,IED.ENTRY_DATE, ST.SITE_NAME FROM INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL, INDENT_ENTRY_DETAILS IED WHERE P.PRODUCT_ID =  IEA.PRODUCT_ID AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND M.MEASUREMENT_ID = IEA.MESURMENT_ID AND ST.SITE_ID = IEA.SITE_ID AND ST.SITE_ID="+ StrSiteId +" AND IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND SPL.STATUS='A' AND SPL.INDENT_ENTRY_DETAILS_ID=IED.INDENT_ENTRY_DETAILS_ID ORDER BY IEA.SITE_ID DESC, P.NAME asc, S.NAME asc, C.NAME asc";
	//				
	//				/*strDCAvailabulity.append("SELECT IED.RECEVED_QTY AS QTY, IED.PRICE,SPL.AMOUNT, SPL.RECIVED_QUANTITY,");
	//				strDCAvailabulity.append("P.NAME AS PRODUCT_NAME, S.NAME AS SUB_PRODUCT_NAME, C.NAME AS CHILD_PRODUCT_NAME,");
	//				strDCAvailabulity.append("M.NAME as MESURMENT_NAME, ST.SITE_NAME FROM INDENT_AVAILABILITY IEA,PRODUCT P,");
	//				strDCAvailabulity.append("SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL, ");
	//				strDCAvailabulity.append("DC_FORM IED WHERE P.PRODUCT_ID =  IEA.PRODUCT_ID");
	//				strDCAvailabulity.append("AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND");
	//				strDCAvailabulity.append("M.MEASUREMENT_ID = IEA.MESURMENT_ID AND ST.SITE_ID = IEA.SITE_ID AND ST.SITE_ID=999 AND");
	//				strDCAvailabulity.append("IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND SPL.STATUS='A' AND");
	//				strDCAvailabulity.append("SPL.DC_FORM_ENTRY_ID=IED.DC_ENTRY_ID ORDER BY IEA.SITE_ID DESC, P.NAME asc, S.NAME asc, C.NAME asc");
	//*/
	//			
	//			}
	//			//System.out.println("query for get Indent Summary or All Indend SUmmary "+sql);
	//			productList = template.queryForList(sql, new Object[]{});
	//			DecimalFormat df = new DecimalFormat("#.00");
	//			if (null != productList && productList.size() > 0) {
	//				
	//				double amtt= 0.0;
	//				double total = 0.0;
	//				SimpleDateFormat dt = null;
	//				Date Date = null;
	//				SimpleDateFormat dt1 = null;
	//				dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	//				dt1 = new SimpleDateFormat("yyyy-MM-dd");
	//				for (Map ProductDetails : productList) {
	//
	//					objProductDetails = new ProductDetails();
	//
	//					productName = ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString().trim();
	//					subProductName =ProductDetails.get("SUB_PRODUCT_NAME") == null ? "": ProductDetails.get("SUB_PRODUCT_NAME").toString().trim();
	//					childProductName = ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "": ProductDetails.get("CHILD_PRODUCT_NAME").toString();
	//					measurement = ProductDetails.get("MESURMENT_NAME") == null ? "": ProductDetails.get("MESURMENT_NAME").toString().trim();
	//					recivedQty = Double.valueOf(ProductDetails.get("QTY") == null ? "" : ProductDetails.get("QTY").toString());
	//					quantity = Double.parseDouble(ProductDetails.get("AVAILABLE_QUANTITY") == null ? "": ProductDetails.get("AVAILABLE_QUANTITY").toString());
	//					amount = ProductDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString().replace(",","").trim();
	//					objProductDetails.setRecivedQty(String.valueOf(recivedQty));
	//				
	//					strPrice = ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString();
	//				    strPrice = df.format(Double.valueOf(strPrice));					
	//					objProductDetails.setBasicAmt(strPrice);
	//					objProductDetails.setProductName(ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString());
	//					objProductDetails.setSub_ProductName(ProductDetails.get("SUB_PRODUCT_NAME") == null ? "": ProductDetails.get("SUB_PRODUCT_NAME").toString());
	//					objProductDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "": ProductDetails.get("CHILD_PRODUCT_NAME").toString());
	//					objProductDetails.setMeasurementName(ProductDetails.get("MESURMENT_NAME") == null ? "": ProductDetails.get("MESURMENT_NAME").toString());
	//					objProductDetails.setQuantity(String.valueOf(Double.parseDouble(ProductDetails.get("AVAILABLE_QUANTITY") == null ? "": ProductDetails.get("AVAILABLE_QUANTITY").toString())));
	//					objProductDetails.setSite_Id(ProductDetails.get("SITE_NAME") == null ? "" : ProductDetails.get("SITE_NAME").toString());
	//					 issueDate=ProductDetails.get("ENTRY_DATE") == null ? "" : ProductDetails.get("ENTRY_DATE").toString();
	//					
	//					
	//					String amt=df.format(Double.valueOf(amount)).toString();
	//					 amtt=Double.valueOf(amt);
	//					amt=numberFormat.format(amtt);
	//					objProductDetails.setPricePerUnit(amt);
	//
	//
	//					issuedQty = decimalFormat.format(Math.abs(recivedQty) - Math.abs(quantity));
	//
	//					objProductDetails.setIssuedQty(issuedQty);
	//					
	//					 
	//					if(issueDate!=null && issueDate!=""){
	//
	//						 Date = dt.parse(issueDate);
	//					//	Date invoice_date = dt.parse(invoicedate);
	//						 
	//						issueDate = dt1.format(Date);
	//						objProductDetails.setLastissuedDate(issueDate);
	//					}else{
	//						
	//						objProductDetails.setLastissuedDate("0000-00-00");	
	//					}
	//						
	//					//System.out.print(Double.valueOf(df.format(d)));
	//					
	//					//doubleAmount = df.format(Double.valueOf(amount) * quantity);
	//					
	//					totalAmount = String.valueOf(df.format(Double.valueOf(amount) * quantity));
	//					grandTotalAmount += Double.valueOf(totalAmount);
	//					
	//					 total=Double.valueOf(totalAmount);
	//				
	//				//	NumberFormat numberFormat = NumberFormat.getNumberInstance(Locale.US);
	//					totalAmount=numberFormat.format(total);
	//					
	//					
	//					objProductDetails.setAmount(totalAmount);
	//					listProductDetails.add(objProductDetails);
	//
	//					productNameChk = productName;
	//					subProductNameChk = subProductName;
	//					childProductNameChk = childProductName;
	//					recivedQtyChk = recivedQty;
	//					measurementChk = measurement;
	//				}
	//			}
	//            //this for dc details
	//			
	//			if (StrSiteId.equalsIgnoreCase("ALL")) {
	//
	//				sql = "SELECT DF.RECEVED_QTY AS QTY, SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.AMOUNT_PER_UNIT_AFTER_TAXES, SPL.AVAILABLE_QUANTITY, P.NAME AS PRODUCT_NAME, S.NAME AS SUB_PRODUCT_NAME,  C.NAME AS CHILD_PRODUCT_NAME, M.NAME as MESURMENT_NAME,IED.ENTRY_DATE, ST.SITE_NAME FROM INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL, DC_FORM DF,INDENT_ENTRY_DETAILS IED WHERE P.PRODUCT_ID =  IEA.PRODUCT_ID AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND M.MEASUREMENT_ID = IEA.MESURMENT_ID AND  ST.SITE_ID = IEA.SITE_ID  AND IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND SPL.STATUS='A'  AND SPL.DC_FORM_ENTRY_ID=DF.DC_ENTRY_ID  and DF.STATUS = 'A' ORDER BY IEA.SITE_ID DESC, P.NAME asc, S.NAME asc, C.NAME asc";
	//
	//			} else {
	//				sql = "SELECT DF.RECEVED_QTY AS QTY, SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.AMOUNT_PER_UNIT_AFTER_TAXES, SPL.AVAILABLE_QUANTITY, P.NAME AS PRODUCT_NAME, S.NAME AS SUB_PRODUCT_NAME,  C.NAME AS CHILD_PRODUCT_NAME, M.NAME as MESURMENT_NAME,IED.ENTRY_DATE, ST.SITE_NAME FROM INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL, DC_FORM DF,INDENT_ENTRY_DETAILS IED WHERE P.PRODUCT_ID =  IEA.PRODUCT_ID AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND M.MEASUREMENT_ID = IEA.MESURMENT_ID AND  ST.SITE_ID = IEA.SITE_ID AND ST.SITE_ID="+ StrSiteId +" AND IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND SPL.STATUS='A'  AND SPL.DC_FORM_ENTRY_ID=DF.DC_ENTRY_ID  and DF.STATUS = 'A' ORDER BY IEA.SITE_ID DESC, P.NAME asc, S.NAME asc, C.NAME asc";
	//			
	//			
	//			}
	//		//	System.out.println("query for get Indent Summary or All Indend SUmmary "+sql);
	//			productList = template.queryForList(sql, new Object[]{});
	//			 df = new DecimalFormat("#.00");
	//			if (null != productList && productList.size() > 0) {
	//				
	//				double amtt= 0.0;
	//				double total = 0.0;
	//				SimpleDateFormat dt = null;
	//				Date Date = null;
	//				SimpleDateFormat dt1 = null;
	//				 dt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	//				 dt1 = new SimpleDateFormat("yyyy-MM-dd");
	//				for (Map ProductDetails : productList) {
	//
	//					objProductDetails = new ProductDetails();
	//
	//					productName = ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString().trim();
	//					subProductName =ProductDetails.get("SUB_PRODUCT_NAME") == null ? "": ProductDetails.get("SUB_PRODUCT_NAME").toString().trim();
	//					childProductName = ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "": ProductDetails.get("CHILD_PRODUCT_NAME").toString();
	//					measurement = ProductDetails.get("MESURMENT_NAME") == null ? "": ProductDetails.get("MESURMENT_NAME").toString().trim();
	//					recivedQty = Double.valueOf(ProductDetails.get("QTY") == null ? "" : ProductDetails.get("QTY").toString());
	//					quantity = Double.parseDouble(ProductDetails.get("AVAILABLE_QUANTITY") == null ? "": ProductDetails.get("AVAILABLE_QUANTITY").toString());
	//					amount = ProductDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString().replace(",","").trim();
	//					objProductDetails.setRecivedQty(String.valueOf(recivedQty));
	//				
	//					strPrice = ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString();
	//				    strPrice = df.format(Double.valueOf(strPrice));					
	//					objProductDetails.setBasicAmt(strPrice);
	//					objProductDetails.setProductName(ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString());
	//					objProductDetails.setSub_ProductName(ProductDetails.get("SUB_PRODUCT_NAME") == null ? "": ProductDetails.get("SUB_PRODUCT_NAME").toString());
	//					objProductDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "": ProductDetails.get("CHILD_PRODUCT_NAME").toString());
	//					objProductDetails.setMeasurementName(ProductDetails.get("MESURMENT_NAME") == null ? "": ProductDetails.get("MESURMENT_NAME").toString());
	//					objProductDetails.setQuantity(String.valueOf(Double.parseDouble(ProductDetails.get("AVAILABLE_QUANTITY") == null ? "": ProductDetails.get("AVAILABLE_QUANTITY").toString())));
	//					objProductDetails.setSite_Id(ProductDetails.get("SITE_NAME") == null ? "" : ProductDetails.get("SITE_NAME").toString());
	//					 issueDate=ProductDetails.get("ENTRY_DATE") == null ? "" : ProductDetails.get("ENTRY_DATE").toString();
	//					
	//					String amt=df.format(Double.valueOf(amount)).toString();
	//					 amtt=Double.valueOf(amt);
	//					amt=numberFormat.format(amtt);
	//					objProductDetails.setPricePerUnit(amt);
	//
	//					
	//					
	//					if(issueDate!=null && issueDate!=""){
	//
	//						Date Date1 = dt.parse(issueDate);
	//						
	//					//	Date invoice_date = dt.parse(issueDate);
	//						 
	//						issueDate = dt1.format(Date1);
	//						objProductDetails.setLastissuedDate(issueDate);
	//					}
	//					else{
	//						
	//
	//					//	Date Date1 = dt.parse(issueDate);
	//						
	//					//	Date invoice_date = dt.parse(issueDate);
	//					//	SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
	//					//	issueDate = dt1.format(Date1);
	//						objProductDetails.setLastissuedDate("0000-00-00");
	//						
	//						
	//					}
	//						
	//					objProductDetails.setPricePerUnit(df.format(Double.valueOf(amount)));
	//					issuedQty = decimalFormat.format(Math.abs(recivedQty) - Math.abs(quantity));
	//					objProductDetails.setIssuedQty(issuedQty);
	//					totalAmount = String.valueOf(df.format(Double.valueOf(amount) * quantity));
	//					grandTotalAmount += Double.valueOf(totalAmount);
	//					
	//					 total=Double.valueOf(totalAmount);
	//					
	//					totalAmount=numberFormat.format(total);
	//					
	//					
	//					
	//					objProductDetails.setAmount(totalAmount);
	//					listProductDetails.add(objProductDetails);
	//					productNameChk = productName;
	//					subProductNameChk = subProductName;
	//					childProductNameChk = childProductName;
	//					recivedQtyChk = recivedQty;
	//					measurementChk = measurement;
	//				}
	//			}
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//			//if exception occured it showing partial data to avoid that we are claring the list
	//			listProductDetails.clear();
	//		} finally {
	//			System.out.println("Grand Total Amount "+grandTotalAmount);
	//			
	//			String grdtotal=decimalFormat.format(grandTotalAmount).toString();
	//			double gamt=Double.valueOf(grdtotal);
	//			String grdtotalamt=numberFormat.format(gamt);
	//			session.setAttribute("grandTotalAmount",grdtotalamt);
	//		}
	//		return listProductDetails;
	//
	//	}

	public List<ProductDetails> getProductList(String StrSiteId, HttpSession session) {

		List<Map<String, Object>> productList = null;
		List<ProductDetails> listProductDetails = new ArrayList<ProductDetails>();
		ProductDetails objProductDetails = null;
		ProductDetails objProductDetails1 = null;
		JdbcTemplate template = null;
		String productName = "";
		String subProductName = "";
		String childProductName = "";
		String productNameChk = "";
		String subProductNameChk = "";
		String childProductNameChk = "";
		Double recivedQty = 0.0;
		Double recivedQtyChk = 0.0;
		String measurement = "";
		String measurementChk = "";
		String issuedQty = "";
		String amount = "";
		double quantity = 0.0;
		String totalAmount = "";
		String sql = "";
		String strPrice = "";
		Double grandTotalAmount = 0.0;
		String issueDate="";
		String avlquantity="";
		double avquantity=0.0;
		double pricePerUnit = 0.0; 
		double totalAmt = 0.0;
		String strTotalAmt = ""; 
		StringBuilder strDCAvailabulity = new StringBuilder();

		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		DecimalFormat df1 = new DecimalFormat(".##");
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			//this is for invoice details
			if (StrSiteId.equalsIgnoreCase("ALL")) {

				sql = "SELECT IED.RECEVED_QTY AS QTY,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES, SPL.AMOUNT_PER_UNIT_AFTER_TAXES, SPL.AVAILABLE_QUANTITY, P.NAME AS PRODUCT_NAME,S.NAME AS SUB_PRODUCT_NAME, C.NAME AS CHILD_PRODUCT_NAME, M.NAME AS MESURMENT_NAME, ST.SITE_NAME,SPL.TOTAL_AMOUNT,NVL(EXPIRY_DATE, '-') as EXPIRY_DATE FROM INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED WHERE  P.PRODUCT_ID =  IEA.PRODUCT_ID AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND M.MEASUREMENT_ID = IEA.MESURMENT_ID AND ST.SITE_ID = IEA.SITE_ID AND IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND SPL.STATUS='A' AND SPL.INDENT_ENTRY_DETAILS_ID=IED.INDENT_ENTRY_DETAILS_ID  ORDER BY IEA.SITE_ID DESC, P.NAME asc, S.NAME asc, C.NAME asc,IED.EXPIRY_DATE ";


				/*	strDCAvailabulity.append("SELECT IED.RECEVED_QTY AS QTY,IED.PRICE, SPL.AMOUNT, SPL.RECIVED_QUANTITY,");
				strDCAvailabulity.append("P.NAME AS PRODUCT_NAME,S.NAME AS SUB_PRODUCT_NAME, C.NAME AS CHILD_PRODUCT_NAME, ");
				strDCAvailabulity.append("M.NAME AS MESURMENT_NAME, ST.SITE_NAME FROM INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S,");
				strDCAvailabulity.append("CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL,DC_FORM IED");
				strDCAvailabulity.append("WHERE  P.PRODUCT_ID =  IEA.PRODUCT_ID AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND ");
				strDCAvailabulity.append("C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND M.MEASUREMENT_ID = IEA.MESURMENT_ID AND ");
				strDCAvailabulity.append("ST.SITE_ID = IEA.SITE_ID AND IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND");
				strDCAvailabulity.append("SPL.STATUS='A' AND SPL.DC_FORM_ENTRY_ID=IED.DC_ENTRY_ID ORDER BY");
				strDCAvailabulity.append("IEA.SITE_ID DESC, P.NAME asc, S.NAME asc, C.NAME asc");
				 */
			} else {
				sql = "SELECT IED.RECEVED_QTY AS QTY, SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.AMOUNT_PER_UNIT_AFTER_TAXES, SPL.AVAILABLE_QUANTITY, P.NAME AS PRODUCT_NAME, S.NAME AS SUB_PRODUCT_NAME, C.NAME AS CHILD_PRODUCT_NAME, M.NAME as MESURMENT_NAME, ST.SITE_NAME,SPL.TOTAL_AMOUNT,NVL(EXPIRY_DATE, '-') as EXPIRY_DATE FROM INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL, INDENT_ENTRY_DETAILS IED WHERE P.PRODUCT_ID =  IEA.PRODUCT_ID AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND M.MEASUREMENT_ID = IEA.MESURMENT_ID AND ST.SITE_ID = IEA.SITE_ID AND ST.SITE_ID="+ StrSiteId +" AND IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND SPL.STATUS='A' AND SPL.INDENT_ENTRY_DETAILS_ID=IED.INDENT_ENTRY_DETAILS_ID  ORDER BY IED.EXPIRY_DATE ";

				/*strDCAvailabulity.append("SELECT IED.RECEVED_QTY AS QTY, IED.PRICE,SPL.AMOUNT, SPL.RECIVED_QUANTITY,");
				strDCAvailabulity.append("P.NAME AS PRODUCT_NAME, S.NAME AS SUB_PRODUCT_NAME, C.NAME AS CHILD_PRODUCT_NAME,");
				strDCAvailabulity.append("M.NAME as MESURMENT_NAME, ST.SITE_NAME FROM INDENT_AVAILABILITY IEA,PRODUCT P,");
				strDCAvailabulity.append("SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL, ");
				strDCAvailabulity.append("DC_FORM IED WHERE P.PRODUCT_ID =  IEA.PRODUCT_ID");
				strDCAvailabulity.append("AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND");
				strDCAvailabulity.append("M.MEASUREMENT_ID = IEA.MESURMENT_ID AND ST.SITE_ID = IEA.SITE_ID AND ST.SITE_ID=999 AND");
				strDCAvailabulity.append("IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND SPL.STATUS='A' AND");
				strDCAvailabulity.append("SPL.DC_FORM_ENTRY_ID=IED.DC_ENTRY_ID ORDER BY IEA.SITE_ID DESC, P.NAME asc, S.NAME asc, C.NAME asc");
				 */

			}
			System.out.println("query for get Indent Summary or All Indend SUmmary "+sql);
			productList = template.queryForList(sql, new Object[]{});
			DecimalFormat df = new DecimalFormat("#.00");
			if (null != productList && productList.size() > 0) {
				for (Map ProductDetails : productList) {

					objProductDetails = new ProductDetails();

					productName = ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString().trim();
					subProductName =ProductDetails.get("SUB_PRODUCT_NAME") == null ? "": ProductDetails.get("SUB_PRODUCT_NAME").toString().trim();
					childProductName = ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "": ProductDetails.get("CHILD_PRODUCT_NAME").toString();
					measurement = ProductDetails.get("MESURMENT_NAME") == null ? "": ProductDetails.get("MESURMENT_NAME").toString().trim();
					recivedQty = Double.valueOf(ProductDetails.get("QTY") == null ? "" : ProductDetails.get("QTY").toString());
					quantity = Double.parseDouble(ProductDetails.get("AVAILABLE_QUANTITY") == null ? "": ProductDetails.get("AVAILABLE_QUANTITY").toString());
					amount = ProductDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString().replace(",","").trim();
					objProductDetails.setRecivedQty(String.valueOf(recivedQty));

					strPrice = ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString();
					strPrice = df.format(Double.valueOf(strPrice));					
					objProductDetails.setBasicAmt(strPrice);
					objProductDetails.setProductName(ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString());
					objProductDetails.setSub_ProductName(ProductDetails.get("SUB_PRODUCT_NAME") == null ? "": ProductDetails.get("SUB_PRODUCT_NAME").toString());
					objProductDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "": ProductDetails.get("CHILD_PRODUCT_NAME").toString());
					objProductDetails.setMeasurementName(ProductDetails.get("MESURMENT_NAME") == null ? "": ProductDetails.get("MESURMENT_NAME").toString());
					objProductDetails.setExpiryDate(ProductDetails.get("EXPIRY_DATE") == null ? "-": ProductDetails.get("EXPIRY_DATE").toString());// they need expire of product dates
					avquantity=(Double.parseDouble(ProductDetails.get("AVAILABLE_QUANTITY") == null ? "": ProductDetails.get("AVAILABLE_QUANTITY").toString()));
					strTotalAmt=(ProductDetails.get("TOTAL_AMOUNT") == null ? "0": ProductDetails.get("TOTAL_AMOUNT").toString());
					avlquantity=df1.format(avquantity);

					objProductDetails.setQuantity(String.valueOf(avlquantity));

					objProductDetails.setSite_Id(ProductDetails.get("SITE_NAME") == null ? "" : ProductDetails.get("SITE_NAME").toString());


					pricePerUnit=Double.valueOf(amount);
					pricePerUnit=Double.parseDouble(new DecimalFormat("##.##").format(pricePerUnit));
				//	totalAmt = Double.valueOf(strTotalAmt);
					 totalAmt=(quantity)*(pricePerUnit);
					totalAmt=Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
					strTotalAmt = String.format("%.2f",totalAmt);

					objProductDetails.setTotalAmount(strTotalAmt);
					objProductDetails.setPricePerUnit(df.format(pricePerUnit));


					issuedQty = decimalFormat.format(Math.abs(recivedQty) - Math.abs(quantity));

					objProductDetails.setIssuedQty(issuedQty);


					//System.out.print(Double.valueOf(df.format(d)));

					//doubleAmount = df.format(Double.valueOf(amount) * quantity);


					//totalAmount = ProductDetails.get("TOTAL_AMOUNT") == null ? "": ProductDetails.get("TOTAL_AMOUNT").toString();

					//	totalAmount = String.valueOf(df.format(Double.valueOf(amount) * quantity));
					//grandTotalAmount += Double.valueOf(strTotalAmt);
					grandTotalAmount += totalAmt;
					objProductDetails.setAmount(totalAmount);
					listProductDetails.add(objProductDetails);

					productNameChk = productName;
					subProductNameChk = subProductName;
					childProductNameChk = childProductName;
					recivedQtyChk = recivedQty;
					measurementChk = measurement;
				}
			}
			//this for dc details

			if (StrSiteId.equalsIgnoreCase("ALL")) {

				sql = "SELECT DF.RECEVED_QTY AS QTY, SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.AMOUNT_PER_UNIT_AFTER_TAXES, SPL.AVAILABLE_QUANTITY, P.NAME AS PRODUCT_NAME, S.NAME AS SUB_PRODUCT_NAME,  C.NAME AS CHILD_PRODUCT_NAME, M.NAME as MESURMENT_NAME, ST.SITE_NAME,SPL.TOTAL_AMOUNT,NVL(DF.EXPIRY_DATE, '-') as EXPIRY_DATE FROM INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL, DC_FORM DF WHERE P.PRODUCT_ID =  IEA.PRODUCT_ID AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND M.MEASUREMENT_ID = IEA.MESURMENT_ID AND  ST.SITE_ID = IEA.SITE_ID  AND IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND SPL.STATUS='A'  AND SPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID  and DF.STATUS = 'A'  ORDER BY IEA.SITE_ID DESC, P.NAME asc, S.NAME asc, C.NAME asc, DF.EXPIRY_DATE ";

			} else {
				sql = "SELECT DF.RECEVED_QTY AS QTY, SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.AMOUNT_PER_UNIT_AFTER_TAXES, SPL.AVAILABLE_QUANTITY, P.NAME AS PRODUCT_NAME, S.NAME AS SUB_PRODUCT_NAME,  C.NAME AS CHILD_PRODUCT_NAME, M.NAME as MESURMENT_NAME, ST.SITE_NAME,SPL.TOTAL_AMOUNT,NVL(DF.EXPIRY_DATE, '-') as EXPIRY_DATE FROM INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE ST, SUMADHURA_PRICE_LIST SPL, DC_FORM DF WHERE P.PRODUCT_ID =  IEA.PRODUCT_ID AND S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID AND C.CHILD_PRODUCT_ID=IEA.CHILD_PRODUCT_ID AND M.MEASUREMENT_ID = IEA.MESURMENT_ID AND  ST.SITE_ID = IEA.SITE_ID AND ST.SITE_ID="+ StrSiteId +" AND IEA.INDENT_AVAILABILITY_ID=SPL.INDENT_AVAILABILITY_ID AND SPL.STATUS='A'  AND SPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID  and DF.STATUS = 'A'  ORDER BY  DF.EXPIRY_DATE";


			}
			System.out.println("query for get Indent Summary or All Indend SUmmary "+sql);
			productList = template.queryForList(sql, new Object[]{});
			df = new DecimalFormat("#.00");
			if (null != productList && productList.size() > 0) {
				for (Map ProductDetails : productList) {

					objProductDetails = new ProductDetails();

					productName = ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString().trim();
					subProductName =ProductDetails.get("SUB_PRODUCT_NAME") == null ? "": ProductDetails.get("SUB_PRODUCT_NAME").toString().trim();
					childProductName = ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "": ProductDetails.get("CHILD_PRODUCT_NAME").toString();
					measurement = ProductDetails.get("MESURMENT_NAME") == null ? "": ProductDetails.get("MESURMENT_NAME").toString().trim();
					recivedQty = Double.valueOf(ProductDetails.get("QTY") == null ? "" : ProductDetails.get("QTY").toString());
					quantity = Double.parseDouble(ProductDetails.get("AVAILABLE_QUANTITY") == null ? "": ProductDetails.get("AVAILABLE_QUANTITY").toString());
					amount = ProductDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString().replace(",","").trim();
					objProductDetails.setRecivedQty(String.valueOf(recivedQty));

					strPrice = ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString();
					strPrice = df.format(Double.valueOf(strPrice));					
					objProductDetails.setBasicAmt(strPrice);
					objProductDetails.setProductName(ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString());
					objProductDetails.setSub_ProductName(ProductDetails.get("SUB_PRODUCT_NAME") == null ? "": ProductDetails.get("SUB_PRODUCT_NAME").toString());
					objProductDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "": ProductDetails.get("CHILD_PRODUCT_NAME").toString());
					objProductDetails.setMeasurementName(ProductDetails.get("MESURMENT_NAME") == null ? "": ProductDetails.get("MESURMENT_NAME").toString());
					objProductDetails.setExpiryDate(ProductDetails.get("EXPIRY_DATE") == null ? "-": ProductDetails.get("EXPIRY_DATE").toString());// they need expire of product dates
					avquantity=(Double.parseDouble(ProductDetails.get("AVAILABLE_QUANTITY") == null ? "": ProductDetails.get("AVAILABLE_QUANTITY").toString()));
					 strTotalAmt=(ProductDetails.get("TOTAL_AMOUNT") == null ? "0": ProductDetails.get("TOTAL_AMOUNT").toString());

					avlquantity=df1.format(avquantity);

					objProductDetails.setQuantity(String.valueOf(avlquantity));
					objProductDetails.setSite_Id(ProductDetails.get("SITE_NAME") == null ? "" : ProductDetails.get("SITE_NAME").toString());

					pricePerUnit=Double.valueOf(amount);
                 	pricePerUnit=Double.parseDouble(new DecimalFormat("##.##").format(pricePerUnit));
					 totalAmt=(quantity)*(pricePerUnit);
				
					//double totalAmt=(quantity)*(pricePerUnit);

				//	totalAmt = Double.valueOf(strTotalAmt);
					totalAmt=Double.parseDouble(new DecimalFormat("##.##").format(totalAmt));
					strTotalAmt=String.format("%.2f",totalAmt);

					objProductDetails.setTotalAmount(strTotalAmt);
					objProductDetails.setPricePerUnit(df.format(pricePerUnit));

					//	objProductDetails.setPricePerUnit(df.format(Double.valueOf(amount)));
					issuedQty = decimalFormat.format(Math.abs(recivedQty) - Math.abs(quantity));
					objProductDetails.setIssuedQty(issuedQty);

					//totalAmount = ProductDetails.get("TOTAL_AMOUNT") == null ? "": ProductDetails.get("TOTAL_AMOUNT").toString();

					//totalAmount = String.valueOf(df.format(Double.valueOf(amount) * quantity));
					grandTotalAmount += totalAmt;
					objProductDetails.setAmount(totalAmount);
					listProductDetails.add(objProductDetails);
					productNameChk = productName;
					subProductNameChk = subProductName;
					childProductNameChk = childProductName;
					recivedQtyChk = recivedQty;
					measurementChk = measurement;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			//if exception occured it showing partial data to avoid that we are claring the list
			listProductDetails.clear();
		} finally {
			System.out.println("Grand Total Amount "+grandTotalAmount);
			session.setAttribute("grandTotalAmount", decimalFormat.format(grandTotalAmount));
		}
		return listProductDetails;

	}

	public Map<String, String> getSiteDetails() {

		String siteId = "";
		String siteName = "";
		String sql = "";
		List<Map<String, Object>> productList = null;
		Map<String, String> map = null;
		JdbcTemplate template = null;

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			sql = "SELECT SITE_ID, SITE_NAME FROM SITE WHERE STATUS='ACTIVE'";
			productList = template.queryForList(sql, new Object[] {});

			if (null != productList && productList.size() > 0) {
				map = new HashMap<String, String>();
				for (Map siteDets : productList) {
					siteId = siteDets.get("SITE_ID") == null ? "" : siteDets.get("SITE_ID").toString();
					siteName = siteDets.get("SITE_NAME") == null ? "" : siteDets.get("SITE_NAME").toString();
					map.put(siteId, siteName);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	/*===================================== this is for gettingf the perticular site start===========================================*/
	public Map<String, String> getSingleSiteDetails(String strSiteId) {

		String siteId = "";
		String siteName = "";
		String sql = "";
		List<Map<String, Object>> productList = null;
		Map<String, String> map = null;
		JdbcTemplate template = null;

		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());
			sql = "SELECT SITE_ID, SITE_NAME FROM SITE WHERE STATUS='ACTIVE' and SITE_ID='"+strSiteId+"'";
			productList = template.queryForList(sql, new Object[] {});

			if (null != productList && productList.size() > 0) {
				map = new HashMap<String, String>();
				for (Map siteDets : productList) {
					siteId = siteDets.get("SITE_ID") == null ? "" : siteDets.get("SITE_ID").toString();
					siteName = siteDets.get("SITE_NAME") == null ? "" : siteDets.get("SITE_NAME").toString();
					map.put(siteId, siteName);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}
	
	public static void main(String [] args){
		String d = "1.2";
		DecimalFormat df = new DecimalFormat("#.00");
		//System.out.print(Double.valueOf(df.format(d)));

		System.out.println(String.format("%.2f", d));

	}

}