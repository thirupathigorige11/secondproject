package com.sumadhura.transdao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.ProductDetails;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.UIProperties;

@Repository
public class IndentAvailabulityDao extends UIProperties {


	public List<ProductDetails> getProductList(String strProductId, String strSubProductId, String strChildProductId, String StrSiteId) {

		List<Map<String, Object>> productList = null;
		List<ProductDetails> listProductDetails  = new ArrayList<ProductDetails>();
		JdbcTemplate jt = null;
		String sql = "";
		StringBuilder str = new StringBuilder();

		try {

			logger.info("Indent Avaliable dao product id " + strProductId +"  sub product id child"+strSubProductId +" child product id "+strChildProductId+" site id"+StrSiteId);
			jt = new JdbcTemplate(DBConnection.getDbConnection());

			String strAdminUserId = validateParams.getProperty("AdminId");


			if(!strAdminUserId.equals(StrSiteId)){



				if (!strProductId.equals("") && !strSubProductId.equals("") && !strChildProductId.equals("")) {



					str.append("select IEA.PRODUCT_ID,IEA.SUB_PRODUCT_ID,IEA.CHILD_PRODUCT_ID,IEA.PRODUT_QTY ,P.NAME as PRODUCT_NAME,S.NAME as SUB_PRODUCT_NAME ,C.NAME as CHILD_PRODUCT_NAME,M.NAME as MESURMENT_NAME,ST.SITE_NAME ");
					str.append("from INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C,MEASUREMENT M,SITE ST ");  
					str.append("where      P.PRODUCT_ID =  IEA.PRODUCT_ID and  S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID and C.CHILD_PRODUCT_ID = IEA.CHILD_PRODUCT_ID and ");
					str.append(" M.MEASUREMENT_ID   = IEA.MESURMENT_ID and   ST.SITE_ID = IEA.SITE_ID and IEA.PRODUCT_ID = ? ");
					str.append("and IEA.SUB_PRODUCT_ID = ? ");
					str.append("and IEA.CHILD_PRODUCT_ID = ? ");
					str.append(" and IEA.SITE_ID = ? ");
					productList = jt.queryForList(str.toString(), new Object[] { strProductId, strSubProductId, strChildProductId,StrSiteId });




				} else if (!strProductId.equals("") && !strSubProductId.equals("") && strChildProductId.equals("")) {


					str.append("select IEA.PRODUCT_ID,IEA.SUB_PRODUCT_ID,IEA.CHILD_PRODUCT_ID,IEA.PRODUT_QTY ,P.NAME as PRODUCT_NAME,S.NAME as SUB_PRODUCT_NAME ,C.NAME as CHILD_PRODUCT_NAME,M.NAME as MESURMENT_NAME,ST.SITE_NAME ");
					str.append("from INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C,MEASUREMENT M,SITE ST ");  
					str.append("where      P.PRODUCT_ID =  IEA.PRODUCT_ID and  S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID and C.CHILD_PRODUCT_ID = IEA.CHILD_PRODUCT_ID and ");
					str.append(" M.MEASUREMENT_ID   = IEA.MESURMENT_ID and   ST.SITE_ID = IEA.SITE_ID and IEA.PRODUCT_ID = ? ");
					str.append("and IEA.SUB_PRODUCT_ID = ? ");
					/*str.append("and IEA.CHILD_PRODUCT_ID = ? ");*/
					str.append(" and IEA.SITE_ID = ? ");


					productList = jt.queryForList(str.toString(), new Object[] { strProductId, strSubProductId,StrSiteId });

				} else if (!strProductId.equals("") && strSubProductId.equals("") && strChildProductId.equals("")) {


					str.append("select IEA.PRODUCT_ID,IEA.SUB_PRODUCT_ID,IEA.CHILD_PRODUCT_ID,IEA.PRODUT_QTY ,P.NAME as PRODUCT_NAME,S.NAME as SUB_PRODUCT_NAME ,C.NAME as CHILD_PRODUCT_NAME,M.NAME as MESURMENT_NAME,ST.SITE_NAME ");
					str.append("from INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C,MEASUREMENT M,SITE ST ");  
					str.append("where      P.PRODUCT_ID =  IEA.PRODUCT_ID and  S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID and C.CHILD_PRODUCT_ID = IEA.CHILD_PRODUCT_ID and ");
					str.append(" M.MEASUREMENT_ID   = IEA.MESURMENT_ID and   ST.SITE_ID = IEA.SITE_ID and IEA.PRODUCT_ID = ? ");
					/*str.append("and IEA.SUB_PRODUCT_ID = ? ");*/
					/*str.append("and IEA.CHILD_PRODUCT_ID = ? ");*/
					str.append(" and IEA.SITE_ID = ? ");

					productList = jt.queryForList(str.toString(), new Object[] { strProductId,StrSiteId });

				}



				for (Map ProductDetails : productList) {

					ProductDetails objProductDetails  = new ProductDetails();

					objProductDetails.setProductName(ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString());
					objProductDetails.setSub_ProductName(ProductDetails.get("SUB_PRODUCT_NAME") == null ? "" : ProductDetails.get("SUB_PRODUCT_NAME").toString());
					objProductDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "" : ProductDetails.get("CHILD_PRODUCT_NAME").toString());
					objProductDetails.setMeasurementName(ProductDetails.get("MESURMENT_NAME") == null ? "" : ProductDetails.get("MESURMENT_NAME").toString());
					objProductDetails.setQuantity(ProductDetails.get("PRODUT_QTY") == null ? "" 	: ProductDetails.get("PRODUT_QTY").toString());

					listProductDetails.add(objProductDetails);

				}


			}else{

				if (!strProductId.equals("") && !strSubProductId.equals("") && !strChildProductId.equals("")) {



					str.append("select IEA.PRODUCT_ID,IEA.SUB_PRODUCT_ID,IEA.CHILD_PRODUCT_ID,IEA.PRODUT_QTY ,P.NAME as PRODUCT_NAME,S.NAME as SUB_PRODUCT_NAME ,C.NAME as CHILD_PRODUCT_NAME,M.NAME as MESURMENT_NAME,ST.SITE_NAME,ST.SITE_ID ");
					str.append("from INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C,MEASUREMENT M,SITE ST ");  
					str.append("where      P.PRODUCT_ID =  IEA.PRODUCT_ID and  S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID and C.CHILD_PRODUCT_ID = IEA.CHILD_PRODUCT_ID and ");
					str.append(" M.MEASUREMENT_ID   = IEA.MESURMENT_ID and   ST.SITE_ID = IEA.SITE_ID and IEA.PRODUCT_ID = ? ");
					str.append("and IEA.SUB_PRODUCT_ID = ? ");
					str.append("and IEA.CHILD_PRODUCT_ID = ? ");
					/*str.append(" and IEA.SITE_ID = ? ");*/
					str.append("order  by  IEA.PRODUCT_ID,IEA.SUB_PRODUCT_ID,IEA.CHILD_PRODUCT_ID ,ST.SITE_ID asc");
					productList = jt.queryForList(str.toString(), new Object[] { strProductId, strSubProductId, strChildProductId });




				} else if (!strProductId.equals("") && !strSubProductId.equals("") && strChildProductId.equals("")) {


					str.append("select IEA.PRODUCT_ID,IEA.SUB_PRODUCT_ID,IEA.CHILD_PRODUCT_ID,IEA.PRODUT_QTY ,P.NAME as PRODUCT_NAME,S.NAME as SUB_PRODUCT_NAME ,C.NAME as CHILD_PRODUCT_NAME,M.NAME as MESURMENT_NAME,ST.SITE_NAME,ST.SITE_ID ");
					str.append("from INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C,MEASUREMENT M,SITE ST ");  
					str.append("where      P.PRODUCT_ID =  IEA.PRODUCT_ID and  S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID and C.CHILD_PRODUCT_ID = IEA.CHILD_PRODUCT_ID and ");
					str.append(" M.MEASUREMENT_ID   = IEA.MESURMENT_ID and   ST.SITE_ID = IEA.SITE_ID and IEA.PRODUCT_ID = ? ");
					str.append("and IEA.SUB_PRODUCT_ID = ? ");
					/*str.append("and IEA.CHILD_PRODUCT_ID = ? ");*/
					/*str.append(" and IEA.SITE_ID = ? ");*/
					str.append("order  by  IEA.PRODUCT_ID,IEA.SUB_PRODUCT_ID,IEA.CHILD_PRODUCT_ID ,ST.SITE_ID asc");

					productList = jt.queryForList(str.toString(), new Object[] { strProductId, strSubProductId });

				} else if (!strProductId.equals("") && strSubProductId.equals("") && strChildProductId.equals("")) {


					str.append("select IEA.PRODUCT_ID,IEA.SUB_PRODUCT_ID,IEA.CHILD_PRODUCT_ID,IEA.PRODUT_QTY ,P.NAME as PRODUCT_NAME,S.NAME as SUB_PRODUCT_NAME ,C.NAME as CHILD_PRODUCT_NAME,M.NAME as MESURMENT_NAME,ST.SITE_NAME,ST.SITE_ID  ");
					str.append("from INDENT_AVAILABILITY IEA,PRODUCT P,SUB_PRODUCT S, CHILD_PRODUCT C,MEASUREMENT M,SITE ST ");  
					str.append("where      P.PRODUCT_ID =  IEA.PRODUCT_ID and  S.SUB_PRODUCT_ID = IEA.SUB_PRODUCT_ID and C.CHILD_PRODUCT_ID = IEA.CHILD_PRODUCT_ID and ");
					str.append(" M.MEASUREMENT_ID   = IEA.MESURMENT_ID and   ST.SITE_ID = IEA.SITE_ID and IEA.PRODUCT_ID = ? ");
					/*str.append("and IEA.SUB_PRODUCT_ID = ? ");*/
					/*str.append("and IEA.CHILD_PRODUCT_ID = ? ");*/
					/*str.append(" and IEA.SITE_ID = ? ");*/
					str.append("order  by  IEA.PRODUCT_ID,IEA.SUB_PRODUCT_ID,IEA.CHILD_PRODUCT_ID ,ST.SITE_ID asc");
					productList = jt.queryForList(str.toString(), new Object[] { strProductId });

				}

				String strUpdatedstrProductId = "";
				String strUpdatedSubProductId = "";
				String strUpdatedChildProductId = "";
				String strSiteName = "";
				String strQuantity = "";
				String strMouserOverData = "";
				int intCount = 0;
				String strMissingData = "";
				String strDBSiteId = "";

				for (Map ProductDetails : productList) {

					ProductDetails objProductDetails  = new ProductDetails();

					objProductDetails.setProductName(ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString());
					objProductDetails.setSub_ProductName(ProductDetails.get("SUB_PRODUCT_NAME") == null ? "" : ProductDetails.get("SUB_PRODUCT_NAME").toString());
					objProductDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "" : ProductDetails.get("CHILD_PRODUCT_NAME").toString());
					objProductDetails.setMeasurementName(ProductDetails.get("MESURMENT_NAME") == null ? "" : ProductDetails.get("MESURMENT_NAME").toString());
					objProductDetails.setQuantity(ProductDetails.get("PRODUT_QTY") == null ? "" 	: ProductDetails.get("PRODUT_QTY").toString());

					strProductId = ProductDetails.get("PRODUCT_ID") == null ? "" : ProductDetails.get("PRODUCT_ID").toString();
					strSubProductId = ProductDetails.get("SUB_PRODUCT_ID") == null ? "" : ProductDetails.get("SUB_PRODUCT_ID").toString();
					//String subId = validateParams.getProperty("CONV"+strSubProductId);
					
				/*	if (StringUtils.isNotBlank(subId)) {
							strSubProductId = subId;
					}*/
					
					
					strChildProductId = ProductDetails.get("CHILD_PRODUCT_ID") == null ? "" : ProductDetails.get("CHILD_PRODUCT_ID").toString();
					strSiteName = ProductDetails.get("SITE_NAME") == null ? "" : ProductDetails.get("SITE_NAME").toString();
					strQuantity = ProductDetails.get("PRODUT_QTY") == null ? "" : ProductDetails.get("PRODUT_QTY").toString();
					strDBSiteId = ProductDetails.get("SITE_ID") == null ? "" : ProductDetails.get("SITE_ID").toString();


					if(intCount == 0 ){
						strMouserOverData = strMouserOverData+strSiteName+" =  "+strQuantity+ "&#13;&#13;";
						strUpdatedstrProductId = strProductId;
						strUpdatedSubProductId = strSubProductId;
						strUpdatedChildProductId = strChildProductId;
						intCount =1;
					}
					else{

						if(strProductId.equals(strUpdatedstrProductId) && strSubProductId.equals(strUpdatedSubProductId) && strChildProductId.equals(strUpdatedChildProductId)){
							strMouserOverData = strMouserOverData+strSiteName+" =  "+strQuantity+ "&#13;&#13;";
						}else{
							strMouserOverData = "";
						}

						strUpdatedstrProductId = strProductId;
						strUpdatedSubProductId = strSubProductId;
						strUpdatedChildProductId = strChildProductId;

					}

					if(strMouserOverData.equals("") && intCount == 1){

						strMissingData  = strMouserOverData+strSiteName+" =  "+strQuantity+ "&#13;&#13;";
					}

					

					
					//adding if data starts from admin

					if(strAdminUserId.equals(strDBSiteId)){
						objProductDetails.setStrOtherSiteQtyDtls(strMouserOverData+strMissingData);
						listProductDetails.add(objProductDetails);
					}

				}

logger.info("Indent Avaliable dao at query ::"+str.toString());
			}
			return listProductDetails;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public List<ProductDetails> getProductDetailsByDates(String productId, String subProductId, String childProductId, String siteId, String fromDate, String toDate) {
		
		List<ProductDetails> list = null;
		List<Map<String, Object>> productList = null;
		ProductDetails prodDetails = null;
		JdbcTemplate jt = null;
		StringBuilder sql = new StringBuilder();
		try {
			
		//	sql = "SELECT QUANTITY, TOTAL_AMOUNT,PRODUCT_NAME, SUB_PRODUCT_NAME, CHILD_PRODUCT_NAME,MEASUREMENT_NAME, SITE_NAME FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND SITE_ID=? AND TRUNC(DATE_AND_TIME) <= TO_DATE('"+toDate+"','dd-MM-yy') AND TRUNC(DATE_AND_TIME) >= TO_DATE('"+fromDate+"','dd-MM-yy')";
			
				sql.append("SELECT SCBP.QUANTITY, SCBP.DATE_AND_TIME, SCBP.TOTAL_AMOUNT,PROD.NAME AS PRODUCT_NAME, SPROD.NAME AS SUB_PRODUCT_NAME, CP.NAME AS CHILD_PRODUCT_NAME,MST.NAME AS MEASUREMENT_NAME, SI.SITE_NAME FROM SUMADHU_CLOSING_BAL_BY_PRODUCT SCBP, PRODUCT PROD, SUB_PRODUCT SPROD, CHILD_PRODUCT CP, SITE SI, MEASUREMENT MST WHERE SCBP.SITE_ID=? AND SCBP.PRODUCT_ID=PROD.PRODUCT_ID AND SCBP.SUB_PRODUCT_ID=SPROD.SUB_PRODUCT_ID AND SCBP.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID AND SCBP.SITE_ID=SI.SITE_ID AND SCBP.MEASUREMENT_ID = MST.MEASUREMENT_ID ");
				
				if (StringUtils.isNotBlank(productId)) {
					sql.append("AND SCBP.PRODUCT_ID = '"+productId+"'");
				}  if (StringUtils.isNotBlank(subProductId)) {
					sql.append(" AND SCBP.SUB_PRODUCT_ID = '"+subProductId+"'");
				}  if (StringUtils.isNotBlank(childProductId)) {
					sql.append(" AND SCBP.CHILD_PRODUCT_ID = '"+childProductId+"'");
				}  if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
					sql.append(" AND TRUNC(SCBP.DATE_AND_TIME) <= TO_DATE('"+toDate+"','dd-MM-yy') AND TRUNC(SCBP.DATE_AND_TIME) >= TO_DATE('"+fromDate+"','dd-MM-yy')");
				}  if (StringUtils.isNotBlank(fromDate) && StringUtils.isBlank(toDate)) {
					sql.append(" AND TRUNC(SCBP.DATE_AND_TIME) = TO_DATE('"+fromDate+"','dd-MM-yy')");
				}
				sql.append(" ORDER BY SCBP.DATE_AND_TIME ASC");
				logger.debug("Query In IndentAvailabilityDao ----->  "+sql.toString());
				logger.info("Query In IndentAvailabilityDao ----->  "+sql.toString());
				
				
				
				
				jt = new JdbcTemplate(DBConnection.getDbConnection());
			productList = jt.queryForList(sql.toString(), new Object[] {siteId});
			
			list = new ArrayList<ProductDetails>();
			if (null != productList || productList.size() > 0) {
				
				for (Map ProductDetails : productList) {
				prodDetails = new ProductDetails();
				
				prodDetails.setQuantity(ProductDetails.get("QUANTITY") == null ? "" : ProductDetails.get("QUANTITY").toString());
				prodDetails.setAmount(ProductDetails.get("TOTAL_AMOUNT") == null ? "" : ProductDetails.get("TOTAL_AMOUNT").toString());
				prodDetails.setProductName(ProductDetails.get("PRODUCT_NAME") == null ? "" : ProductDetails.get("PRODUCT_NAME").toString());
				prodDetails.setSub_ProductName(ProductDetails.get("SUB_PRODUCT_NAME") == null ? "" : ProductDetails.get("SUB_PRODUCT_NAME").toString());
				prodDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "" : ProductDetails.get("CHILD_PRODUCT_NAME").toString());
				prodDetails.setMeasurementName(ProductDetails.get("MEASUREMENT_NAME") == null ? "" : ProductDetails.get("MEASUREMENT_NAME").toString());
				prodDetails.setSite_Id(ProductDetails.get("SITE_NAME") == null ? "" : ProductDetails.get("SITE_NAME").toString());
				
				String date = ProductDetails.get("DATE_AND_TIME") == null ? "" : ProductDetails.get("DATE_AND_TIME").toString();
				if (StringUtils.isNotBlank(date)) {
					date = DateUtil.dateConversion(date);
				} else {
					date = "";
				}
				prodDetails.setDate(date);		
				
				list.add(prodDetails);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		return list;
	}
	
	public List<ProductDetails> getCumulativeProductList(String strProductId, String strSubProductId, String strChildProductId, String StrSiteId,String fromDate,String toDate) {

		List<Map<String, Object>> productList = null;
		List<Map<String, Object>> cumulativeList = null;
		List<ProductDetails> listProductDetails  = new ArrayList<ProductDetails>();
		JdbcTemplate jt = null;
		String sql = "";
		double actualQuantity=0;
		double issuedQuantity=0;
		double currentQuantity=0;
		String productId="";
		String subProductId="";
		String childProductId="";
		String measureMentId="";
		String  value="";
		double dc_Quantity=0.0;
		String productQty = "";
		StringBuilder str = new StringBuilder();

		try {

			logger.info("Indent Avaliable dao product id " + strProductId +"  sub product id child"+strSubProductId +" child product id "+strChildProductId+" site id"+StrSiteId);
			jt = new JdbcTemplate(DBConnection.getDbConnection());

		//-- Getting Total Present Quantity --	
			String queryToGetPresentQuantity = "select sum(SPL.AVAILABLE_QUANTITY) as PRODUT_QTY ,SPL.PRODUCT_ID,SPL.SUB_PRODUCT_ID,SPL.CHILD_PRODUCT_ID,SPL.UNITS_OF_MEASUREMENT "
					+ " from SUMADHURA_PRICE_LIST SPL "
					+ " where SPL.STATUS = 'A' and SPL.SITE_ID = '"+StrSiteId+"'"; 
			
			if(StringUtils.isNotBlank(strProductId)){
				queryToGetPresentQuantity=queryToGetPresentQuantity+" and SPL.PRODUCT_ID = '"+strProductId+"'";
			}
			if(StringUtils.isNotBlank(strSubProductId)){
				queryToGetPresentQuantity=queryToGetPresentQuantity+" and SPL.SUB_PRODUCT_ID = '"+strSubProductId+"'";
			}
			if(StringUtils.isNotBlank(strChildProductId)){
				queryToGetPresentQuantity=queryToGetPresentQuantity+" and SPL.CHILD_PRODUCT_ID = '"+strChildProductId+"'";
			}
			queryToGetPresentQuantity=queryToGetPresentQuantity+" group by SPL.SITE_ID,SPL.PRODUCT_ID,SPL.SUB_PRODUCT_ID,SPL.CHILD_PRODUCT_ID,SPL.UNITS_OF_MEASUREMENT";
			
			List<Map<String, Object>> productPresentQuantityList = null;
			productPresentQuantityList = jt.queryForList(queryToGetPresentQuantity, new Object[] {});
			
			HashMap<String,String> PresentQuantityMap = new HashMap<String,String>();
			for(Map<String, Object> ProductDetails : productPresentQuantityList){
				productId=ProductDetails.get("PRODUCT_ID").toString();
				subProductId=ProductDetails.get("SUB_PRODUCT_ID").toString();
				childProductId=ProductDetails.get("CHILD_PRODUCT_ID").toString();
				measureMentId=ProductDetails.get("UNITS_OF_MEASUREMENT").toString();
				productQty=ProductDetails.get("PRODUT_QTY").toString();
				
				PresentQuantityMap.put(StrSiteId+"@@"+productId+"@@"+subProductId+"@@"+childProductId+"@@"+measureMentId, productQty);
			}
			
			
		//-- End --
			
		//	String strAdminUserId = validateParams.getProperty("AdminId");
	//	if(!strAdminUserId.equals(StrSiteId)){
			if(fromDate!=null && !fromDate.equals("") && toDate!=null && !toDate.equals("")){



				if (!strProductId.equals("") && !strSubProductId.equals("") && !strChildProductId.equals("")) {



					sql="WITH CUMULATIVE AS"
						+" (select sum(IED.RECEVED_QTY) as ACTUAL_QUANTITY,sum(IED.ISSUED_QTY) as issued_quantity,IED.PRODUCT_ID,IED.SUB_PRODUCT_ID,"
						+" IED.CHILD_PRODUCT_ID,P.NAME as PROD_NAME,SP.NAME as CUM_SUB_PROD_NAME,CP.NAME as CUM_CHP_NAME,IED.MEASUR_MNT_ID,M.NAME as CUM_MEA_NAME"
						+" from INDENT_ENTRY IE,INDENT_ENTRY_DETAILS IED,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID"
						+" and IE.SITE_ID ='"+StrSiteId+"' and IED.PRODUCT_ID= '"+strProductId+"' and IED.SUB_PRODUCT_ID='"+strSubProductId+"' and IED.CHILD_PRODUCT_ID = '"+strChildProductId+"' AND CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = IED.SUB_PRODUCT_ID AND P.PRODUCT_ID=IED.PRODUCT_ID AND M.MEASUREMENT_ID=IED.MEASUR_MNT_ID"
						+" and TRUNC(IE.RECEIVED_OR_ISSUED_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')"
						+" and IE.INDENT_ENTRY_ID not  in (select DCE.INDENT_ENTRY_ID from DC_ENTRY DCE where DCE.INDENT_ENTRY_ID is not null) "
						+" group by IED.MEASUR_MNT_ID,M.NAME,IED.PRODUCT_ID,P.NAME, IED.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,IED.SUB_PRODUCT_ID order by IED.PRODUCT_ID),"
						+" receive as(select sum(DCF.RECEVED_QTY) as ACTUAL_QUANTITY_DC,DCF.PRODUCT_ID,DCF.SUB_PRODUCT_ID,DCF.CHILD_PRODUCT_ID,"
						+" P.NAME as PROD_NAME,SP.NAME as SUB_PROD_NAME,CP.NAME as REC_CHP_NAME,DCF.MEASUR_MNT_ID,M.NAME AS MEA_NAME"
						+" from DC_ENTRY IE,DC_FORM DCF,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.DC_ENTRY_ID = DCF.DC_ENTRY_ID and IE.SITE_ID ='"+StrSiteId+"'"
						+" and DCF.PRODUCT_ID= '"+strProductId+"' and DCF.SUB_PRODUCT_ID='"+strSubProductId+"' and DCF.CHILD_PRODUCT_ID = '"+strChildProductId+"' AND CP.CHILD_PRODUCT_ID=DCF.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = DCF.SUB_PRODUCT_ID AND P.PRODUCT_ID=DCF.PRODUCT_ID AND M.MEASUREMENT_ID=DCF.MEASUR_MNT_ID"
						+" and TRUNC(IE.RECEIVED_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')"
						+" group by DCF.MEASUR_MNT_ID,M.NAME,DCF.PRODUCT_ID, P.NAME, DCF.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,DCF.SUB_PRODUCT_ID order by DCF.PRODUCT_ID)"
						+" select ACTUAL_QUANTITY,issued_quantity,ACTUAL_QUANTITY_DC,CM.CHILD_PRODUCT_ID,CM.PRODUCT_ID,CM.SUB_PRODUCT_ID,CM.PROD_NAME,CM.CUM_SUB_PROD_NAME,"
						+" CM.MEASUR_MNT_ID,CM.CUM_MEA_NAME,CM.CUM_CHP_NAME ,R.REC_CHP_NAME as DC_CHILD_PRODUCT_NAME ,(R.CHILD_PRODUCT_ID)AS DC_CHILD_PRODUCT_ID,"
						+" (R.SUB_PRODUCT_ID)AS DC_SUB_PRODUCT_ID,(R.SUB_PROD_NAME)AS DC_SUB_PRODUCT_NAME,(R.PRODUCT_ID)AS DC_PRODUCT_ID,"
						+" (R.PROD_NAME)AS DC_PRODUCT_NAME,(R.MEASUR_MNT_ID)AS DC_MEASUR_MNT_ID,(R.MEA_NAME)AS DC_MEASUR_MNT_NAME"
						+" from CUMULATIVE CM full OUTER JOIN receive R ON R.CHILD_PRODUCT_ID = CM.CHILD_PRODUCT_ID" ;
					
					
					productList = jt.queryForList(sql, new Object[] {});
					


				} else if (!strProductId.equals("") && !strSubProductId.equals("") && strChildProductId.equals("")) {


					sql="WITH CUMULATIVE AS"
						+" (select sum(IED.RECEVED_QTY) as ACTUAL_QUANTITY,sum(IED.ISSUED_QTY) as issued_quantity,IED.PRODUCT_ID,IED.SUB_PRODUCT_ID,"
						+" IED.CHILD_PRODUCT_ID,P.NAME as PROD_NAME,SP.NAME as CUM_SUB_PROD_NAME,CP.NAME as CUM_CHP_NAME,IED.MEASUR_MNT_ID,M.NAME as CUM_MEA_NAME"
						+" from INDENT_ENTRY IE,INDENT_ENTRY_DETAILS IED,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID"
						+" and IE.SITE_ID ='"+StrSiteId+"' and IED.PRODUCT_ID= '"+strProductId+"' and IED.SUB_PRODUCT_ID='"+strSubProductId+"' AND CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = IED.SUB_PRODUCT_ID AND P.PRODUCT_ID=IED.PRODUCT_ID AND M.MEASUREMENT_ID=IED.MEASUR_MNT_ID"
						+" and TRUNC(IE.RECEIVED_OR_ISSUED_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')"
						+" and IE.INDENT_ENTRY_ID not  in (select DCE.INDENT_ENTRY_ID from DC_ENTRY DCE where DCE.INDENT_ENTRY_ID is not null) "
						+" group by IED.MEASUR_MNT_ID,M.NAME,IED.PRODUCT_ID,P.NAME, IED.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,IED.SUB_PRODUCT_ID order by IED.PRODUCT_ID),"
						+" receive as(select sum(DCF.RECEVED_QTY) as ACTUAL_QUANTITY_DC,DCF.PRODUCT_ID,DCF.SUB_PRODUCT_ID,DCF.CHILD_PRODUCT_ID,"
						+" P.NAME as PROD_NAME,SP.NAME as SUB_PROD_NAME,CP.NAME as REC_CHP_NAME,DCF.MEASUR_MNT_ID,M.NAME AS MEA_NAME"
						+" from DC_ENTRY IE,DC_FORM DCF,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.DC_ENTRY_ID = DCF.DC_ENTRY_ID and IE.SITE_ID ='"+StrSiteId+"'"
						+" and DCF.PRODUCT_ID= '"+strProductId+"' and DCF.SUB_PRODUCT_ID='"+strSubProductId+"' AND CP.CHILD_PRODUCT_ID=DCF.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = DCF.SUB_PRODUCT_ID AND P.PRODUCT_ID=DCF.PRODUCT_ID AND M.MEASUREMENT_ID=DCF.MEASUR_MNT_ID"
						+" and TRUNC(IE.RECEIVED_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')"
						+" group by DCF.MEASUR_MNT_ID,M.NAME,DCF.PRODUCT_ID, P.NAME, DCF.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,DCF.SUB_PRODUCT_ID order by DCF.PRODUCT_ID)"
						+" select ACTUAL_QUANTITY,issued_quantity,ACTUAL_QUANTITY_DC,CM.CHILD_PRODUCT_ID,CM.PRODUCT_ID,CM.SUB_PRODUCT_ID,CM.PROD_NAME,CM.CUM_SUB_PROD_NAME,"
						+" CM.MEASUR_MNT_ID,CM.CUM_MEA_NAME,CM.CUM_CHP_NAME ,R.REC_CHP_NAME as DC_CHILD_PRODUCT_NAME ,(R.CHILD_PRODUCT_ID)AS DC_CHILD_PRODUCT_ID,"
						+" (R.SUB_PRODUCT_ID)AS DC_SUB_PRODUCT_ID,(R.SUB_PROD_NAME)AS DC_SUB_PRODUCT_NAME,(R.PRODUCT_ID)AS DC_PRODUCT_ID,"
						+" (R.PROD_NAME)AS DC_PRODUCT_NAME,(R.MEASUR_MNT_ID)AS DC_MEASUR_MNT_ID,(R.MEA_NAME)AS DC_MEASUR_MNT_NAME"
						+" from CUMULATIVE CM full OUTER JOIN receive R ON R.CHILD_PRODUCT_ID = CM.CHILD_PRODUCT_ID" ;
						

					productList = jt.queryForList(sql, new Object[] { });
					
					

				} else if (!strProductId.equals("") && strSubProductId.equals("") && strChildProductId.equals("")) {


						sql="WITH CUMULATIVE AS"
						+" (select sum(IED.RECEVED_QTY) as ACTUAL_QUANTITY,sum(IED.ISSUED_QTY) as issued_quantity,IED.PRODUCT_ID,IED.SUB_PRODUCT_ID,"
						+" IED.CHILD_PRODUCT_ID,P.NAME as PROD_NAME,SP.NAME as CUM_SUB_PROD_NAME,CP.NAME as CUM_CHP_NAME,IED.MEASUR_MNT_ID,M.NAME as CUM_MEA_NAME"
						+" from INDENT_ENTRY IE,INDENT_ENTRY_DETAILS IED,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID"
						+" and IE.SITE_ID ='"+StrSiteId+"' and IED.PRODUCT_ID= '"+strProductId+"'  AND CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = IED.SUB_PRODUCT_ID AND P.PRODUCT_ID=IED.PRODUCT_ID AND M.MEASUREMENT_ID=IED.MEASUR_MNT_ID"
						+" and TRUNC(IE.RECEIVED_OR_ISSUED_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')"
						+" and IE.INDENT_ENTRY_ID not  in (select DCE.INDENT_ENTRY_ID from DC_ENTRY DCE where DCE.INDENT_ENTRY_ID is not null) "
						+" group by IED.MEASUR_MNT_ID,M.NAME,IED.PRODUCT_ID,P.NAME, IED.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,IED.SUB_PRODUCT_ID order by IED.PRODUCT_ID),"
						+" receive as(select sum(DCF.RECEVED_QTY) as ACTUAL_QUANTITY_DC,DCF.PRODUCT_ID,DCF.SUB_PRODUCT_ID,DCF.CHILD_PRODUCT_ID,"
						+" P.NAME as PROD_NAME,SP.NAME as SUB_PROD_NAME,CP.NAME as REC_CHP_NAME,DCF.MEASUR_MNT_ID,M.NAME AS MEA_NAME"
						+" from DC_ENTRY IE,DC_FORM DCF,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.DC_ENTRY_ID = DCF.DC_ENTRY_ID and IE.SITE_ID ='"+StrSiteId+"'"
						+" and DCF.PRODUCT_ID= '"+strProductId+"'  AND CP.CHILD_PRODUCT_ID=DCF.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = DCF.SUB_PRODUCT_ID AND P.PRODUCT_ID=DCF.PRODUCT_ID AND M.MEASUREMENT_ID=DCF.MEASUR_MNT_ID"
						+" and TRUNC(IE.RECEIVED_DATE) BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')"
						+" group by DCF.MEASUR_MNT_ID,M.NAME,DCF.PRODUCT_ID, P.NAME, DCF.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,DCF.SUB_PRODUCT_ID order by DCF.PRODUCT_ID)"
						+" select ACTUAL_QUANTITY,issued_quantity,ACTUAL_QUANTITY_DC,CM.CHILD_PRODUCT_ID,CM.PRODUCT_ID,CM.SUB_PRODUCT_ID,CM.PROD_NAME,CM.CUM_SUB_PROD_NAME,"
						+" CM.MEASUR_MNT_ID,CM.CUM_MEA_NAME,CM.CUM_CHP_NAME ,R.REC_CHP_NAME as DC_CHILD_PRODUCT_NAME ,(R.CHILD_PRODUCT_ID)AS DC_CHILD_PRODUCT_ID,"
						+" (R.SUB_PRODUCT_ID)AS DC_SUB_PRODUCT_ID,(R.SUB_PROD_NAME)AS DC_SUB_PRODUCT_NAME,(R.PRODUCT_ID)AS DC_PRODUCT_ID,"
						+" (R.PROD_NAME)AS DC_PRODUCT_NAME,(R.MEASUR_MNT_ID)AS DC_MEASUR_MNT_ID,(R.MEA_NAME)AS DC_MEASUR_MNT_NAME"
						+" from CUMULATIVE CM full OUTER JOIN receive R ON R.CHILD_PRODUCT_ID = CM.CHILD_PRODUCT_ID" ;
					
						productList = jt.queryForList(sql, new Object[] {});
					
					
					
					
				}



				for (Map ProductDetails : productList) {

					ProductDetails objProductDetails  = new ProductDetails();
					
					
					
					productId=(ProductDetails.get("PRODUCT_ID") == null ? ProductDetails.get("DC_PRODUCT_ID").toString() : ProductDetails.get("PRODUCT_ID").toString());
					subProductId=(ProductDetails.get("SUB_PRODUCT_ID") == null ? ProductDetails.get("DC_SUB_PRODUCT_ID").toString() : ProductDetails.get("SUB_PRODUCT_ID").toString());
					childProductId=(ProductDetails.get("CHILD_PRODUCT_ID") == null ? ProductDetails.get("DC_CHILD_PRODUCT_ID").toString() : ProductDetails.get("CHILD_PRODUCT_ID").toString());
					measureMentId=(ProductDetails.get("MEASUR_MNT_ID") == null ? ProductDetails.get("DC_MEASUR_MNT_ID").toString() : ProductDetails.get("MEASUR_MNT_ID").toString());
					objProductDetails.setProductName(ProductDetails.get("PROD_NAME") == null ? ProductDetails.get("DC_PRODUCT_NAME").toString() : ProductDetails.get("PROD_NAME").toString());
					objProductDetails.setSub_ProductName(ProductDetails.get("CUM_SUB_PROD_NAME") == null ? ProductDetails.get("DC_SUB_PRODUCT_NAME").toString() : ProductDetails.get("CUM_SUB_PROD_NAME").toString());
					objProductDetails.setChild_ProductName(ProductDetails.get("CUM_CHP_NAME") == null ? ProductDetails.get("DC_CHILD_PRODUCT_NAME").toString() : ProductDetails.get("CUM_CHP_NAME").toString());
					
					
					
					
					
					objProductDetails.setMeasurementName(ProductDetails.get("CUM_MEA_NAME") == null ? ProductDetails.get("DC_MEASUR_MNT_NAME").toString() : ProductDetails.get("CUM_MEA_NAME").toString());
					actualQuantity=Double.parseDouble(ProductDetails.get("ACTUAL_QUANTITY") == null ? "0" 	: ProductDetails.get("ACTUAL_QUANTITY").toString());
					issuedQuantity=Double.parseDouble(ProductDetails.get("issued_quantity") == null ? "0" 	: ProductDetails.get("issued_quantity").toString());
					dc_Quantity=Double.parseDouble(ProductDetails.get("ACTUAL_QUANTITY_DC") == null ? "0"	: ProductDetails.get("ACTUAL_QUANTITY_DC").toString());
					
				//	dc_Quantity=ViewDcReceivedQuantity(productId,subProductId,childProductId,StrSiteId,fromDate,toDate);
					//actualQuantity=(Double.valueOf(totalAvailbleQty.split("&&")[0]))+(dc_Quantity);
					actualQuantity=(actualQuantity)+(dc_Quantity);
					//currentQuantity=(actualQuantity)-(issuedQuantity);
					// above currentQuantity is commented because current Quantity has to display live product quantity in the site now
					try{
						currentQuantity = Double.parseDouble(PresentQuantityMap.get(StrSiteId+"@@"+productId+"@@"+subProductId+"@@"+childProductId+"@@"+measureMentId));
					}catch(NullPointerException e){
						currentQuantity = 0;
					}
					System.out.println("the current quantity"+currentQuantity);
					objProductDetails.setProductId(productId);
					objProductDetails.setSub_ProductId(subProductId);
					objProductDetails.setChild_ProductId(childProductId);
					objProductDetails.setMeasurementId(measureMentId);
					objProductDetails.setActualQuantity(actualQuantity);
					objProductDetails.setIssuedQuantity(issuedQuantity);
					objProductDetails.setCurrentQuantity(currentQuantity);
					value=ViewIssuedQuantity(productId,subProductId,childProductId,measureMentId,fromDate,toDate,StrSiteId);
					objProductDetails.setOutIssue(value);
					
					listProductDetails.add(objProductDetails);

				}
			}
			else{
				
				if (!strProductId.equals("") && !strSubProductId.equals("") && !strChildProductId.equals("")) {



					sql="WITH CUMULATIVE AS"
						+" (select sum(IED.RECEVED_QTY) as ACTUAL_QUANTITY,sum(IED.ISSUED_QTY) as issued_quantity,IED.PRODUCT_ID,IED.SUB_PRODUCT_ID,"
						+" IED.CHILD_PRODUCT_ID,P.NAME as PROD_NAME,SP.NAME as CUM_SUB_PROD_NAME,CP.NAME as CUM_CHP_NAME,IED.MEASUR_MNT_ID,M.NAME as CUM_MEA_NAME"
						+" from INDENT_ENTRY IE,INDENT_ENTRY_DETAILS IED,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID"
						+" and IE.SITE_ID ='"+StrSiteId+"' and IED.PRODUCT_ID= '"+strProductId+"' and IED.SUB_PRODUCT_ID='"+strSubProductId+"' and IED.CHILD_PRODUCT_ID = '"+strChildProductId+"' AND CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = IED.SUB_PRODUCT_ID AND P.PRODUCT_ID=IED.PRODUCT_ID AND M.MEASUREMENT_ID=IED.MEASUR_MNT_ID"
						+" and IE.INDENT_ENTRY_ID not  in (select DCE.INDENT_ENTRY_ID from DC_ENTRY DCE where DCE.INDENT_ENTRY_ID is not null) "
						
						+" group by IED.MEASUR_MNT_ID,M.NAME,IED.PRODUCT_ID,P.NAME, IED.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,IED.SUB_PRODUCT_ID order by IED.PRODUCT_ID),"
						+" receive as(select sum(DCF.RECEVED_QTY) as ACTUAL_QUANTITY_DC,DCF.PRODUCT_ID,DCF.SUB_PRODUCT_ID,DCF.CHILD_PRODUCT_ID,"
						+" P.NAME as PROD_NAME,SP.NAME as SUB_PROD_NAME,CP.NAME as REC_CHP_NAME,DCF.MEASUR_MNT_ID,M.NAME AS MEA_NAME"
						+" from DC_ENTRY IE,DC_FORM DCF,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.DC_ENTRY_ID = DCF.DC_ENTRY_ID and IE.SITE_ID ='"+StrSiteId+"'"
						+" and DCF.PRODUCT_ID= '"+strProductId+"' and DCF.SUB_PRODUCT_ID='"+strSubProductId+"' and DCF.CHILD_PRODUCT_ID = '"+strChildProductId+"' AND CP.CHILD_PRODUCT_ID=DCF.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = DCF.SUB_PRODUCT_ID AND P.PRODUCT_ID=DCF.PRODUCT_ID AND M.MEASUREMENT_ID=DCF.MEASUR_MNT_ID"
						
						+" group by DCF.MEASUR_MNT_ID,M.NAME,DCF.PRODUCT_ID, P.NAME, DCF.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,DCF.SUB_PRODUCT_ID order by DCF.PRODUCT_ID)"
						+" select ACTUAL_QUANTITY,issued_quantity,ACTUAL_QUANTITY_DC,CM.CHILD_PRODUCT_ID,CM.PRODUCT_ID,CM.SUB_PRODUCT_ID,CM.PROD_NAME,CM.CUM_SUB_PROD_NAME,"
						+" CM.MEASUR_MNT_ID,CM.CUM_MEA_NAME,CM.CUM_CHP_NAME ,R.REC_CHP_NAME as DC_CHILD_PRODUCT_NAME ,(R.CHILD_PRODUCT_ID)AS DC_CHILD_PRODUCT_ID,"
						+" (R.SUB_PRODUCT_ID)AS DC_SUB_PRODUCT_ID,(R.SUB_PROD_NAME)AS DC_SUB_PRODUCT_NAME,(R.PRODUCT_ID)AS DC_PRODUCT_ID,"
						+" (R.PROD_NAME)AS DC_PRODUCT_NAME,(R.MEASUR_MNT_ID)AS DC_MEASUR_MNT_ID,(R.MEA_NAME)AS DC_MEASUR_MNT_NAME"
						+" from CUMULATIVE CM full OUTER JOIN receive R ON R.CHILD_PRODUCT_ID = CM.CHILD_PRODUCT_ID" ;
					
						
					
					productList = jt.queryForList(sql, new Object[] {});
					


				} else if (!strProductId.equals("") && !strSubProductId.equals("") && strChildProductId.equals("")) {


					sql="WITH CUMULATIVE AS"
						+" (select sum(IED.RECEVED_QTY) as ACTUAL_QUANTITY,sum(IED.ISSUED_QTY) as issued_quantity,IED.PRODUCT_ID,IED.SUB_PRODUCT_ID,"
						+" IED.CHILD_PRODUCT_ID,P.NAME as PROD_NAME,SP.NAME as CUM_SUB_PROD_NAME,CP.NAME as CUM_CHP_NAME,IED.MEASUR_MNT_ID,M.NAME as CUM_MEA_NAME"
						+" from INDENT_ENTRY IE,INDENT_ENTRY_DETAILS IED,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID"
						+" and IE.SITE_ID ='"+StrSiteId+"' and IED.PRODUCT_ID= '"+strProductId+"' and IED.SUB_PRODUCT_ID='"+strSubProductId+"' AND CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = IED.SUB_PRODUCT_ID AND P.PRODUCT_ID=IED.PRODUCT_ID AND M.MEASUREMENT_ID=IED.MEASUR_MNT_ID"
						+" and IE.INDENT_ENTRY_ID not  in (select DCE.INDENT_ENTRY_ID from DC_ENTRY DCE where DCE.INDENT_ENTRY_ID is not null) "
						
						+" group by IED.MEASUR_MNT_ID,M.NAME,IED.PRODUCT_ID,P.NAME, IED.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,IED.SUB_PRODUCT_ID order by IED.PRODUCT_ID),"
						+" receive as(select sum(DCF.RECEVED_QTY) as ACTUAL_QUANTITY_DC,DCF.PRODUCT_ID,DCF.SUB_PRODUCT_ID,DCF.CHILD_PRODUCT_ID,"
						+" P.NAME as PROD_NAME,SP.NAME as SUB_PROD_NAME,CP.NAME as REC_CHP_NAME,DCF.MEASUR_MNT_ID,M.NAME AS MEA_NAME"
						+" from DC_ENTRY IE,DC_FORM DCF,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.DC_ENTRY_ID = DCF.DC_ENTRY_ID and IE.SITE_ID ='"+StrSiteId+"'"
						+" and DCF.PRODUCT_ID= '"+strProductId+"' and DCF.SUB_PRODUCT_ID='"+strSubProductId+"' AND CP.CHILD_PRODUCT_ID=DCF.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = DCF.SUB_PRODUCT_ID AND P.PRODUCT_ID=DCF.PRODUCT_ID AND M.MEASUREMENT_ID=DCF.MEASUR_MNT_ID"
						
						+" group by DCF.MEASUR_MNT_ID,M.NAME,DCF.PRODUCT_ID, P.NAME, DCF.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,DCF.SUB_PRODUCT_ID order by DCF.PRODUCT_ID)"
						+" select ACTUAL_QUANTITY,issued_quantity,ACTUAL_QUANTITY_DC,CM.CHILD_PRODUCT_ID,CM.PRODUCT_ID,CM.SUB_PRODUCT_ID,CM.PROD_NAME,CM.CUM_SUB_PROD_NAME,"
						+" CM.MEASUR_MNT_ID,CM.CUM_MEA_NAME,CM.CUM_CHP_NAME ,R.REC_CHP_NAME as DC_CHILD_PRODUCT_NAME ,(R.CHILD_PRODUCT_ID)AS DC_CHILD_PRODUCT_ID,"
						+" (R.SUB_PRODUCT_ID)AS DC_SUB_PRODUCT_ID,(R.SUB_PROD_NAME)AS DC_SUB_PRODUCT_NAME,(R.PRODUCT_ID)AS DC_PRODUCT_ID,"
						+" (R.PROD_NAME)AS DC_PRODUCT_NAME,(R.MEASUR_MNT_ID)AS DC_MEASUR_MNT_ID,(R.MEA_NAME)AS DC_MEASUR_MNT_NAME"
						+" from CUMULATIVE CM full OUTER JOIN receive R ON R.CHILD_PRODUCT_ID = CM.CHILD_PRODUCT_ID" ;
					
						
					productList = jt.queryForList(sql, new Object[] {});
					
					

				} else if (!strProductId.equals("") && strSubProductId.equals("") && strChildProductId.equals("")) {


					sql="WITH CUMULATIVE AS"
						+" (select sum(IED.RECEVED_QTY) as ACTUAL_QUANTITY,sum(IED.ISSUED_QTY) as issued_quantity,IED.PRODUCT_ID,IED.SUB_PRODUCT_ID,"
						+" IED.CHILD_PRODUCT_ID,P.NAME as PROD_NAME,SP.NAME as CUM_SUB_PROD_NAME,CP.NAME as CUM_CHP_NAME,IED.MEASUR_MNT_ID,M.NAME as CUM_MEA_NAME"
						+" from INDENT_ENTRY IE,INDENT_ENTRY_DETAILS IED,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID"
						+" and IE.SITE_ID ='"+StrSiteId+"' and IED.PRODUCT_ID= '"+strProductId+"'  AND CP.CHILD_PRODUCT_ID=IED.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = IED.SUB_PRODUCT_ID AND P.PRODUCT_ID=IED.PRODUCT_ID AND M.MEASUREMENT_ID=IED.MEASUR_MNT_ID"
						+" and IE.INDENT_ENTRY_ID not  in (select DCE.INDENT_ENTRY_ID from DC_ENTRY DCE where DCE.INDENT_ENTRY_ID is not null) "
						
						+" group by IED.MEASUR_MNT_ID,M.NAME,IED.PRODUCT_ID,P.NAME, IED.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,IED.SUB_PRODUCT_ID order by IED.PRODUCT_ID),"
						+" receive as(select sum(DCF.RECEVED_QTY) as ACTUAL_QUANTITY_DC,DCF.PRODUCT_ID,DCF.SUB_PRODUCT_ID,DCF.CHILD_PRODUCT_ID,"
						+" P.NAME as PROD_NAME,SP.NAME as SUB_PROD_NAME,CP.NAME as REC_CHP_NAME,DCF.MEASUR_MNT_ID,M.NAME AS MEA_NAME"
						+" from DC_ENTRY IE,DC_FORM DCF,CHILD_PRODUCT CP,SUB_PRODUCT SP,PRODUCT P,MEASUREMENT M where IE.DC_ENTRY_ID = DCF.DC_ENTRY_ID and IE.SITE_ID ='"+StrSiteId+"'"
						+" and DCF.PRODUCT_ID= '"+strProductId+"'  AND CP.CHILD_PRODUCT_ID=DCF.CHILD_PRODUCT_ID"
						+" and SP.SUB_PRODUCT_ID = DCF.SUB_PRODUCT_ID AND P.PRODUCT_ID=DCF.PRODUCT_ID AND M.MEASUREMENT_ID=DCF.MEASUR_MNT_ID"
						
						+" group by DCF.MEASUR_MNT_ID,M.NAME,DCF.PRODUCT_ID, P.NAME, DCF.CHILD_PRODUCT_ID,SP.NAME, CP.NAME,DCF.SUB_PRODUCT_ID order by DCF.PRODUCT_ID)"
						+" select ACTUAL_QUANTITY,issued_quantity,ACTUAL_QUANTITY_DC,CM.CHILD_PRODUCT_ID,CM.PRODUCT_ID,CM.SUB_PRODUCT_ID,CM.PROD_NAME,CM.CUM_SUB_PROD_NAME,"
						+" CM.MEASUR_MNT_ID,CM.CUM_MEA_NAME,CM.CUM_CHP_NAME ,R.REC_CHP_NAME as DC_CHILD_PRODUCT_NAME ,(R.CHILD_PRODUCT_ID)AS DC_CHILD_PRODUCT_ID,"
						+" (R.SUB_PRODUCT_ID)AS DC_SUB_PRODUCT_ID,(R.SUB_PROD_NAME)AS DC_SUB_PRODUCT_NAME,(R.PRODUCT_ID)AS DC_PRODUCT_ID,"
						+" (R.PROD_NAME)AS DC_PRODUCT_NAME,(R.MEASUR_MNT_ID)AS DC_MEASUR_MNT_ID,(R.MEA_NAME)AS DC_MEASUR_MNT_NAME"
						+" from CUMULATIVE CM full OUTER JOIN receive R ON R.CHILD_PRODUCT_ID = CM.CHILD_PRODUCT_ID" ;
					
						
					productList = jt.queryForList(sql, new Object[] {});
						
				}
					for (Map ProductDetails : productList) {

					ProductDetails objProductDetails  = new ProductDetails();
					productId=(ProductDetails.get("PRODUCT_ID") == null ? ProductDetails.get("DC_PRODUCT_ID").toString() : ProductDetails.get("PRODUCT_ID").toString());
					subProductId=(ProductDetails.get("SUB_PRODUCT_ID") == null ? ProductDetails.get("DC_SUB_PRODUCT_ID").toString() : ProductDetails.get("SUB_PRODUCT_ID").toString());
					childProductId=(ProductDetails.get("CHILD_PRODUCT_ID") == null ? ProductDetails.get("DC_CHILD_PRODUCT_ID").toString() : ProductDetails.get("CHILD_PRODUCT_ID").toString());
					measureMentId=(ProductDetails.get("MEASUR_MNT_ID") == null ? ProductDetails.get("DC_MEASUR_MNT_ID").toString() : ProductDetails.get("MEASUR_MNT_ID").toString());
					objProductDetails.setProductName(ProductDetails.get("PROD_NAME") == null ? ProductDetails.get("DC_PRODUCT_NAME").toString() : ProductDetails.get("PROD_NAME").toString());
					objProductDetails.setSub_ProductName(ProductDetails.get("CUM_SUB_PROD_NAME") == null ? ProductDetails.get("DC_SUB_PRODUCT_NAME").toString() : ProductDetails.get("CUM_SUB_PROD_NAME").toString());
					objProductDetails.setChild_ProductName(ProductDetails.get("CUM_CHP_NAME") == null ? ProductDetails.get("DC_CHILD_PRODUCT_NAME").toString() : ProductDetails.get("CUM_CHP_NAME").toString());
					
					objProductDetails.setMeasurementName(ProductDetails.get("CUM_MEA_NAME") == null ? ProductDetails.get("DC_MEASUR_MNT_NAME").toString() : ProductDetails.get("CUM_MEA_NAME").toString());
					actualQuantity=Double.parseDouble(ProductDetails.get("ACTUAL_QUANTITY") == null ? "0" 	: ProductDetails.get("ACTUAL_QUANTITY").toString());
					issuedQuantity=Double.parseDouble(ProductDetails.get("issued_quantity") == null ? "0" 	: ProductDetails.get("issued_quantity").toString());
					dc_Quantity=Double.parseDouble(ProductDetails.get("ACTUAL_QUANTITY_DC") == null ? "0" 	: ProductDetails.get("ACTUAL_QUANTITY_DC").toString());
						
				//	dc_Quantity=ViewDcReceivedQuantity(productId,subProductId,childProductId,StrSiteId,fromDate,toDate);
					actualQuantity=(actualQuantity)+(dc_Quantity);
					
					//currentQuantity=(actualQuantity)-(issuedQuantity);
					// above currentQuantity is commented because current Quantity has to display live product quantity in the site now
					try{
						currentQuantity = Double.parseDouble(PresentQuantityMap.get(StrSiteId+"@@"+productId+"@@"+subProductId+"@@"+childProductId+"@@"+measureMentId));
					}catch(NullPointerException e){
						currentQuantity = 0;
					}
					System.out.println("the current quantity"+currentQuantity);
					objProductDetails.setProductId(productId);
					objProductDetails.setSub_ProductId(subProductId);
					objProductDetails.setChild_ProductId(childProductId);
					objProductDetails.setMeasurementId(measureMentId);
					objProductDetails.setActualQuantity(actualQuantity);
					objProductDetails.setIssuedQuantity(issuedQuantity);
					objProductDetails.setCurrentQuantity(currentQuantity);
					 value=ViewIssuedQuantity(productId,subProductId,childProductId,measureMentId,fromDate,toDate,StrSiteId);
					 objProductDetails.setOutIssue(value);
					
					listProductDetails.add(objProductDetails);

				}
				
				
				
				
				
			}

			return listProductDetails;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public String ViewIssuedQuantity(String productId,String subProductId,String childProductId,String measurementId,String fromDate,String toDate,String strSiteId) throws NamingException{
		String query="";
		String sql=""; 
		JdbcTemplate jt = null;
		List<Map<String, Object>> outList = null;
		List<Map<String, Object>> outoList = null;
		String outVal="";
		String outoVal="";
		String siteIssuedQuantity="";
		String otherSiteIssuedQuantity="";
		String response="";
		jt = new JdbcTemplate(DBConnection.getDbConnection());
	//	ProductDetails objProductDetails  = new ProductDetails();
		if(fromDate!=null && !fromDate.equals("") && toDate!=null && !toDate.equals("")){
			
			try{
			
			
			query="select sum(IED.ISSUED_QTY) AS ISSUED_QUANTITY from INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE"
				+" WHERE IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' and IE.SITE_ID= ? and IED.PRODUCT_ID=?"
				+" AND IED.SUB_PRODUCT_ID= ? AND IED.CHILD_PRODUCT_ID= ? AND IED.MEASUR_MNT_ID= ?  and"
				+" TRUNC(IED.ENTRY_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
			
			outList=jt.queryForList(query, new Object[] {strSiteId,productId,subProductId,childProductId,measurementId });
			
			for (Map ProductDetails : outList) {
				
				outVal=(ProductDetails.get("ISSUED_QUANTITY") == null ? "" : ProductDetails.get("ISSUED_QUANTITY").toString());
			
				
			}
			siteIssuedQuantity = outVal;
			
			
			sql="select sum(IED.ISSUED_QTY) AS ISSUED_QUANTITY from INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE"
				+" WHERE IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUTO' and IE.SITE_ID= ? and IED.PRODUCT_ID=?"
				+" AND IED.SUB_PRODUCT_ID= ? AND IED.CHILD_PRODUCT_ID= ? AND IED.MEASUR_MNT_ID= ?  and"
				+" TRUNC(IED.ENTRY_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy')";
			
			outoList=jt.queryForList(sql, new Object[] {strSiteId,productId,subProductId,childProductId,measurementId });
			
			for (Map ProductDetails : outoList) {
				
				outoVal=(ProductDetails.get("ISSUED_QUANTITY") == null ? "" : ProductDetails.get("ISSUED_QUANTITY").toString());
				}
			
			otherSiteIssuedQuantity = outoVal;
			
			response = "Internal Issued : "+siteIssuedQuantity+ "$$$Issued To Others : "+otherSiteIssuedQuantity;
			
			}catch(Exception e){
				
			}
		}
		else{
		
		
		try{

		query="select sum(IED.ISSUED_QTY) AS ISSUED_QUANTITY from INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE"
			+" WHERE IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUT' and IE.SITE_ID= ? and IED.PRODUCT_ID= ?"
			+" AND IED.SUB_PRODUCT_ID= ? AND IED.CHILD_PRODUCT_ID=? AND IED.MEASUR_MNT_ID= ?";

		outList=jt.queryForList(query, new Object[] {strSiteId,productId,subProductId,childProductId,measurementId });
		for (Map ProductDetails : outList) {

		
		outVal=(ProductDetails.get("ISSUED_QUANTITY") == null ? "" : ProductDetails.get("ISSUED_QUANTITY").toString());
		}
		
		siteIssuedQuantity = outVal;
		
		sql="select sum(IED.ISSUED_QTY) AS ISSUED_QUANTITY from INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE"
				+" WHERE IED.INDENT_ENTRY_ID=IE.INDENT_ENTRY_ID AND IE.INDENT_TYPE='OUTO' and IE.SITE_ID= ? and IED.PRODUCT_ID= ?"
				+" AND IED.SUB_PRODUCT_ID= ? AND IED.CHILD_PRODUCT_ID=? AND IED.MEASUR_MNT_ID= ?";

		outoList=jt.queryForList(sql, new Object[] {strSiteId,productId,subProductId,childProductId,measurementId});
		for (Map ProductDetails : outoList) {
		
		outoVal=(ProductDetails.get("ISSUED_QUANTITY") == null ? "" : ProductDetails.get("ISSUED_QUANTITY").toString());
		}	
		
		otherSiteIssuedQuantity = outoVal;
		
		response = "Internal Issued : "+siteIssuedQuantity+ "$$$Issued To Others : "+otherSiteIssuedQuantity;
		
		}//try
		
		
		
	catch (Exception e) {
			e.printStackTrace();
		}
		}//else
		return response;
	}
	
	public List<ProductDetails> TotalReceivedQuantity(String childProductId,String strSiteId,String fromDate,String toDate) throws NamingException{
		List<ProductDetails> listProductDetails  = new ArrayList<ProductDetails>();
		List<Map<String, Object>> productList = null;
		JdbcTemplate jt = null;
		jt = new JdbcTemplate(DBConnection.getDbConnection());
		String query="";
		String indentNumber="";
		String childProductName="";
		String mesuarementName="";
		String receivedDate="";
		String price="";
		
		if((fromDate==null && toDate==null)|| (fromDate.equals("")&&toDate.equals(""))){
		
		query="select DISTINCT(SPL.INVOICE_NUMBER),IED.CHILD_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.RECEVED_QTY,IE.RECEIVED_OR_ISSUED_DATE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TOTAL_AMOUNT,SPL.BASIC_AMOUNT,SPL.TAX_AMOUNT,SPL.OTHER_CHARGES_AFTER_TAX  from SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE "
				+ " WHERE IE.INDENT_TYPE IN('IN','IND','INO') and IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID AND IED.CHILD_PRODUCT_ID=?  AND SPL.SITE_ID =?";

		}else{
			
		query="select DISTINCT(SPL.INVOICE_NUMBER),IED.CHILD_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.RECEVED_QTY,IE.RECEIVED_OR_ISSUED_DATE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TOTAL_AMOUNT,SPL.BASIC_AMOUNT,SPL.TAX_AMOUNT,SPL.OTHER_CHARGES_AFTER_TAX  from SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE "
					+ " WHERE IE.INDENT_TYPE IN('IN','IND','INO') and IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID AND IED.CHILD_PRODUCT_ID=?  AND SPL.SITE_ID =? "
					+ " AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') ";

		}
		// extra condition
		if(StringUtils.isNotBlank(query)){
			query = query + " and IE.INDENT_ENTRY_ID not  in (select DCE.INDENT_ENTRY_ID from DC_ENTRY DCE where DCE.INDENT_ENTRY_ID is not null)";
		}
		// above extra condition is added newly for not getting products from invoice which is converted from DC
		
		productList=jt.queryForList(query, new Object[] {childProductId,strSiteId});
		for (Map ProductDetails : productList) {
			ProductDetails objProductDetails  = new ProductDetails();
			
			objProductDetails.setInvoiceNumber(ProductDetails.get("INVOICE_NUMBER") == null ? "" : ProductDetails.get("INVOICE_NUMBER").toString());
			objProductDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "" : ProductDetails.get("CHILD_PRODUCT_NAME").toString());
			objProductDetails.setMeasurementName(ProductDetails.get("MEASUR_MNT_NAME") == null ? "" : ProductDetails.get("MEASUR_MNT_NAME").toString());
			objProductDetails.setDate(ProductDetails.get("RECEIVED_OR_ISSUED_DATE") == null ? "" : ProductDetails.get("RECEIVED_OR_ISSUED_DATE").toString());
			objProductDetails.setAmount(ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
			objProductDetails.setRecivedQty(ProductDetails.get("RECEVED_QTY") == null ? "0" : ProductDetails.get("RECEVED_QTY").toString());
			objProductDetails.setBasicAmt(ProductDetails.get("BASIC_AMOUNT") == null ? "0" : ProductDetails.get("BASIC_AMOUNT").toString());
			objProductDetails.setTaxAmount(ProductDetails.get("TAX_AMOUNT") == null ? "0" : ProductDetails.get("TAX_AMOUNT").toString());
			objProductDetails.setTotalAmount(ProductDetails.get("TOTAL_AMOUNT") == null ? "0" : ProductDetails.get("TOTAL_AMOUNT").toString());
			objProductDetails.setOtherchargesaftertax1(ProductDetails.get("OTHER_CHARGES_AFTER_TAX") == null ? "0" : ProductDetails.get("OTHER_CHARGES_AFTER_TAX").toString());
			listProductDetails.add(objProductDetails);
			
			}	
			
		System.out.println("in the dao list is "+listProductDetails);
		return listProductDetails;
	}
	
	
	public List<ProductDetails> TotalIssuedQuantity(String childProductId,String strSiteId,String fromDate,String toDate) throws NamingException{
		List<ProductDetails> listProductDetails  = new ArrayList<ProductDetails>();
		List<Map<String, Object>> productList = null;
		JdbcTemplate jt = null;
		jt = new JdbcTemplate(DBConnection.getDbConnection());
		String query="";
		String indentNumber="";
		String childProductName="";
		String mesuarementName="";
		String receivedDate="";
		String price="";
		
		if((fromDate==null && toDate==null)|| (fromDate.equals("")&&toDate.equals(""))){
		
		query="select DISTINCT(SPL.INVOICE_NUMBER),SPL.DC_NUMBER,IED.CHILD_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.ISSUED_QTY,IED.ENTRY_DATE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TOTAL_AMOUNT,SPL.BASIC_AMOUNT,SPL.TAX,SPL.OTHER_CHARGES_AFTER_TAX,IE.RECEIVED_OR_ISSUED_DATE  from SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE" 
			+" WHERE IE.INDENT_TYPE IN('OUT','OUTO') AND IED.PRICE_ID=SPL.PRICE_ID AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND IED.CHILD_PRODUCT_ID=?  AND SPL.SITE_ID =?";

		}else{
			
			query="select DISTINCT(SPL.INVOICE_NUMBER),SPL.DC_NUMBER,IED.CHILD_PRODUCT_NAME,IED.MEASUR_MNT_NAME,IED.ISSUED_QTY,IED.ENTRY_DATE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TOTAL_AMOUNT,SPL.BASIC_AMOUNT,SPL.TAX,SPL.OTHER_CHARGES_AFTER_TAX,IE.RECEIVED_OR_ISSUED_DATE  from SUMADHURA_PRICE_LIST SPL,INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE" 
					+" WHERE IE.INDENT_TYPE IN('OUT','OUTO') AND IED.PRICE_ID=SPL.PRICE_ID AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND IED.CHILD_PRODUCT_ID=?  AND SPL.SITE_ID =?"
					+" AND TRUNC(IE.RECEIVED_OR_ISSUED_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') ";

			
			
			
			
		}
		productList=jt.queryForList(query, new Object[] {childProductId,strSiteId});
		for (Map ProductDetails : productList) {
			ProductDetails objProductDetails  = new ProductDetails();
			
			objProductDetails.setInvoiceNumber(ProductDetails.get("INVOICE_NUMBER") == null ? "" : ProductDetails.get("INVOICE_NUMBER").toString());
			objProductDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "" : ProductDetails.get("CHILD_PRODUCT_NAME").toString());
			objProductDetails.setMeasurementName(ProductDetails.get("MEASUR_MNT_NAME") == null ? "" : ProductDetails.get("MEASUR_MNT_NAME").toString());
			objProductDetails.setDate(ProductDetails.get("RECEIVED_OR_ISSUED_DATE") == null ? "" : ProductDetails.get("RECEIVED_OR_ISSUED_DATE").toString());
			objProductDetails.setAmount(ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
			objProductDetails.setRecivedQty(ProductDetails.get("ISSUED_QTY") == null ? "0" : ProductDetails.get("ISSUED_QTY").toString());
			objProductDetails.setBasicAmt(ProductDetails.get("BASIC_AMOUNT") == null ? "0" : ProductDetails.get("BASIC_AMOUNT").toString());
			objProductDetails.setTax(ProductDetails.get("TAX") == null ? "0" : ProductDetails.get("TAX").toString());
			objProductDetails.setTotalAmount(ProductDetails.get("TOTAL_AMOUNT") == null ? "0" : ProductDetails.get("TOTAL_AMOUNT").toString());
			objProductDetails.setDcNumber("DCNO_"+(ProductDetails.get("DC_NUMBER") == null ? "0" : ProductDetails.get("DC_NUMBER").toString()));
			
			listProductDetails.add(objProductDetails);
			
			}	
			
		System.out.println("in the dao list is "+listProductDetails);
		return listProductDetails;
	}

	public List<ProductDetails> TotalReceivedQuantityForDc(String childProductId,String strSiteId,String fromDate,String toDate) throws NamingException{
		List<ProductDetails> listProductDetails  = new ArrayList<ProductDetails>();
		List<Map<String, Object>> productList = null;
		JdbcTemplate jt = null;
		jt = new JdbcTemplate(DBConnection.getDbConnection());
		String query="";
		String indentNumber="";
		String childProductName="";
		String mesuarementName="";
		String receivedDate="";
		String price="";
		String invoiceNo = "";
		String dcNumber = "";
		
		if((fromDate==null && toDate==null)|| (fromDate.equals("")&&toDate.equals(""))){
			
		query="SELECT  SPL.PRICE_ID,SPL.DC_NUMBER,SPL.INVOICE_NUMBER,DF.CHILD_PRODUCT_NAME,DF.MEASUR_MNT_NAME,DF.RECEVED_QTY,SPL.CREATED_DATE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TOTAL_AMOUNT,SPL.BASIC_AMOUNT,SPL.TAX_AMOUNT,SPL.OTHER_CHARGES_AFTER_TAX,DE.RECEIVED_DATE from SUMADHURA_PRICE_LIST SPL,DC_FORM DF,DC_ENTRY DE"
				+" WHERE DE.DC_ENTRY_ID=DF.DC_ENTRY_ID and SPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID AND DF.CHILD_PRODUCT_ID=? and SPL.SITE_ID = ?";
		}else{
			
			query="SELECT  SPL.PRICE_ID,SPL.DC_NUMBER,SPL.INVOICE_NUMBER,DF.CHILD_PRODUCT_NAME,DF.MEASUR_MNT_NAME,DF.RECEVED_QTY,SPL.CREATED_DATE,SPL.AMOUNT_PER_UNIT_BEFORE_TAXES,SPL.TOTAL_AMOUNT,SPL.BASIC_AMOUNT,SPL.TAX_AMOUNT,SPL.OTHER_CHARGES_AFTER_TAX,DE.RECEIVED_DATE from SUMADHURA_PRICE_LIST SPL,DC_FORM DF,DC_ENTRY DE"
					+" WHERE DE.DC_ENTRY_ID=DF.DC_ENTRY_ID and SPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID AND DF.CHILD_PRODUCT_ID=? and SPL.SITE_ID = ?"
					+ " AND TRUNC(SPL.CREATED_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') ";
			
			
		}
		// " AND DF.STATUS='A' " removed from above queries to get all DC' products active or inactive
		
		productList=jt.queryForList(query, new Object[] {childProductId,strSiteId});
		for (Map ProductDetails : productList) {
			ProductDetails objProductDetails  = new ProductDetails();
			
			invoiceNo = ProductDetails.get("INVOICE_NUMBER") == null ? "" : ProductDetails.get("INVOICE_NUMBER").toString();
			dcNumber = ProductDetails.get("DC_NUMBER") == null ? "0" : ProductDetails.get("DC_NUMBER").toString();
			if(StringUtils.isNotBlank(invoiceNo)){
				objProductDetails.setDcNumber(invoiceNo);
			} else{
				objProductDetails.setDcNumber("DCNO_"+dcNumber);
			}	
			objProductDetails.setChild_ProductName(ProductDetails.get("CHILD_PRODUCT_NAME") == null ? "0" : ProductDetails.get("CHILD_PRODUCT_NAME").toString());
			objProductDetails.setMeasurementName(ProductDetails.get("MEASUR_MNT_NAME") == null ? "0" : ProductDetails.get("MEASUR_MNT_NAME").toString());
			objProductDetails.setDate(ProductDetails.get("RECEIVED_DATE") == null ? "0" : ProductDetails.get("RECEIVED_DATE").toString());
			objProductDetails.setAmount(ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "0" : ProductDetails.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
			objProductDetails.setRecivedQty(ProductDetails.get("RECEVED_QTY") == null ? "0" : ProductDetails.get("RECEVED_QTY").toString());
			objProductDetails.setBasicAmt(ProductDetails.get("BASIC_AMOUNT") == null ? "0" : ProductDetails.get("BASIC_AMOUNT").toString());
			objProductDetails.setTaxAmount(ProductDetails.get("TAX_AMOUNT") == null ? "0" : ProductDetails.get("TAX_AMOUNT").toString());
			objProductDetails.setTotalAmount(ProductDetails.get("TOTAL_AMOUNT") == null ? "0" : ProductDetails.get("TOTAL_AMOUNT").toString());
			objProductDetails.setOtherchargesaftertax1(ProductDetails.get("OTHER_CHARGES_AFTER_TAX") == null ? "0" : ProductDetails.get("OTHER_CHARGES_AFTER_TAX").toString());
			
			listProductDetails.add(objProductDetails);
			
			}	
			
		System.out.println("in the dao list is "+listProductDetails);
		return listProductDetails;
	}
	
	public double ViewDcReceivedQuantity(String productId,String subProductId,String childProductId,String StrSiteId,String fromDate,String toDate) throws NamingException{
		String query="";
		String sql=""; 
		JdbcTemplate jt = null;
		List<Map<String, Object>> dcValue = null;
		List<Map<String, Object>> outoList = null;
		double dcNumber=0.0;
		String outoVal="";
		String siteIssuedQuantity="";
		String otherSiteIssuedQuantity="";
		String response="";
		jt = new JdbcTemplate(DBConnection.getDbConnection());
	//	ProductDetails objProductDetails  = new ProductDetails();
		
			
			try{
			
				if((fromDate==null && toDate==null)|| (fromDate.equals("")&&toDate.equals(""))){
			
			query="select sum(DC.RECEVED_QTY) as DC_QUAN FROM DC_FORM DC,INDENT_ENTRY IE WHERE IE.INDENT_ENTRY_ID=DC.INDENT_ENTRY_ID" 
				+" AND DC.STATUS='A' AND DC.PRODUCT_ID=? and  DC.SUB_PRODUCT_ID=? and DC.CHILD_PRODUCT_ID=? and IE.SITE_ID =?";

				}else{
					
					query="select sum(DC.RECEVED_QTY) as DC_QUAN FROM DC_FORM DC,INDENT_ENTRY IE WHERE IE.INDENT_ENTRY_ID=DC.INDENT_ENTRY_ID" 
							+" AND DC.STATUS='A' AND DC.PRODUCT_ID=? and  DC.SUB_PRODUCT_ID=? and DC.CHILD_PRODUCT_ID=? and IE.SITE_ID =?"
							+ " and TRUNC(IE.RECEIVED_OR_ISSUED_DATE)  BETWEEN TO_DATE('"+fromDate+"','dd-MM-yy') AND TO_DATE('"+toDate+"','dd-MM-yy') ";

					
				}
			dcValue=jt.queryForList(query, new Object[] {productId,subProductId,childProductId,StrSiteId });
			
			for (Map ProductDetails : dcValue) {
				
				dcNumber=Double.parseDouble(ProductDetails.get("DC_QUAN") == null ? "0" : ProductDetails.get("DC_QUAN").toString());
			
				
			}
	
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return dcNumber;
	
	}
	
}

