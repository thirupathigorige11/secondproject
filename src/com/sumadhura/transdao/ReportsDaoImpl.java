package com.sumadhura.transdao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.sumadhura.dto.PriceMasterDTO;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.UIProperties;

/**
 * 
 * @author Aniket Chavan
 * @since 5/31/2018 4:24
 * @category Generating Reports
 */
@Repository("guireportdao")
public class ReportsDaoImpl implements ReportsDao {

	static Logger log = Logger.getLogger(ReportsDaoImpl.class);
	
	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;

	private static final String QUERY_FOR_PRODUCT_DETAIL = "select sum(ival.total_amount) as  sumOfPrice,pd.product_id,pd.name from SUMADHURA_PRICE_LIST ival left join product pd on ival.product_id=pd.product_id  group by pd.name,pd.product_id";
	
	private static final String QUERY_FOR_CHILD_PRODUCT_PRICE_LIST = "select nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),si.SITE_NAME,OSPL.SITE_ID,OSPL.CHILD_PRODUCT_ID,max(to_char(OSPL.CREATED_DATE,'dd-MON-yy hh:MM:ss')), ROUND(TO_CHAR(AMOUNT_PER_UNIT_BEFORE_TAXES),2) as AMOUNT_PER_UNIT_BEFORE_TAXES, ROUND(TO_CHAR(AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES , "
			+ " OSPL.BASIC_AMOUNT ,CP.NAME,mmt.name as MEASUREMENT_NAME,OSPL.PRICE_ID "
			+ " from SUMADHURA_PRICE_LIST OSPL left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=OSPL.INDENT_ENTRY_DETAILS_ID "
			+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID   "
			+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
			+ " left outer join DC_FORM DF on OSPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID "
			+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "// and DE.INDENT_ENTRY_ID is null    
			+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID "
			+ " ,SITE si ,CHILD_PRODUCT CP,MEASUREMENT mmt "//and (IE.INDENT_TYPE!='INO' or IE.INDENT_TYPE is null)
			+ " where OSPL.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID      and si.SITE_ID=OSPL.SITE_ID  and mmt.MEASUREMENT_ID=OSPL.UNITS_OF_MEASUREMENT and OSPL.site_id=? and OSPL.CREATED_DATE = (select max(CREATED_DATE) from SUMADHURA_PRICE_LIST ispl where ispl.CHILD_PRODUCT_ID = OSPL.CHILD_PRODUCT_ID and ispl.site_id= ?) "
			+ " group by nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),si.SITE_NAME,OSPL.SITE_ID,OSPL.CHILD_PRODUCT_ID,OSPL.CREATED_DATE,OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES,OSPL.BASIC_AMOUNT,CP.NAME,mmt.name, OSPL.AMOUNT_PER_UNIT_AFTER_TAXES,OSPL.PRICE_ID "
			+ " ORDER BY OSPL.CHILD_PRODUCT_ID,OSPL.CREATED_DATE,OSPL.PRICE_ID DESC";
 	
	/*private static final String QUERY_FOR_CHILD_PRODUCT_PRICE_BY_ID = "select SPL.price_id,cp.CHILD_PRODUCT_ID,(cp.NAME),mmt.name as MEASUREMENT_NAME,ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2)as  AMOUNT_PER_UNIT_BEFORE_TAXES ,  ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES,SPL.AVAILABLE_QUANTITY, NVL(ROUND(TO_CHAR(SPL.BASIC_AMOUNT),2), '0.0') as BASIC_AMOUNT,        NVL(ROUND(TO_CHAR(SPL.TOTAL_AMOUNT),2), '0.0') as TOTAL_AMOUNT,NVL(SPL.AMOUNT_AFTER_TAX, SPL.BASIC_AMOUNT) as AMOUNT_AFTER_TAX,to_char(SPL.created_date,'MONyy') as createdDate,to_char(SPL.created_date,'MM') as monthNumber,SPL.site_id         from  CHILD_PRODUCT cp,SUMADHURA_PRICE_LIST SPL,MEASUREMENT mmt     where  SPL.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID and mmt.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT  AND SPL.CHILD_PRODUCT_ID=?     and SPL.site_id=? and SPL.created_date "
			+ " between to_char(trunc(add_months(sysdate,-8)),'dd-MON-yy') and sysdate  "
			+ " order by SPL.child_product_id ,SPL.created_date";*/
	
	private static final String QUERY_FOR_CHILD_PRODUCT_PRICE_BY_ID ="select  nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),initCap(to_char(SPL.created_date,'MONTH-yyyy')) as MonthName,nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(SPL.INVOICE_NUMBER,''),SPL.price_id,cp.CHILD_PRODUCT_ID,(cp.NAME),mmt.name as MEASUREMENT_NAME,ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2)as  AMOUNT_PER_UNIT_BEFORE_TAXES ,  ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES,SPL.AVAILABLE_QUANTITY, "
			+ " NVL(ROUND(TO_CHAR(SPL.BASIC_AMOUNT),2), '0.0') as BASIC_AMOUNT,NVL(ROUND(TO_CHAR(SPL.TOTAL_AMOUNT),2), '0.0') as TOTAL_AMOUNT, "
			+ " NVL(SPL.AMOUNT_AFTER_TAX, SPL.BASIC_AMOUNT) as AMOUNT_AFTER_TAX,to_char(SPL.created_date,'MONyy') as createdDate, "
			+ " to_char(SPL.created_date,'MM') as monthNumber,SPL.site_id  "
			+ " from  CHILD_PRODUCT cp,SUMADHURA_PRICE_LIST SPL   left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID "
			+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID    "
			+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
			+ " left outer join DC_FORM DF on SPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID "// and DF.status='A'
			+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "//and DE.INDENT_ENTRY_ID is null 
			+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID ,MEASUREMENT mmt "
			+ " where  SPL.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID and mmt.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT "
			+ " AND SPL.CHILD_PRODUCT_ID=?     and SPL.site_id=? and SPL.created_date "
			+ " between to_char(trunc(add_months(sysdate,-8)),'dd-MON-yy') and sysdate "
			+ " order by SPL.child_product_id ,SPL.created_date"; 
			
	private static final String QUERY_FOR_CHILD_PRODUCT_PRICE_MONTH_NAME = "select to_char(SPL.created_date,'MONyy') as createdDate,to_char(SPL.created_date,'MM') as monthNumber,to_char(SPL.created_date,'yyyy') as yearNumber      from  CHILD_PRODUCT cp,SUMADHURA_PRICE_LIST SPL,MEASUREMENT mmt     where  SPL.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID and mmt.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT "
			+ " AND SPL.CHILD_PRODUCT_ID=? and SPL.site_id=? and SPL.created_date "
			+ " between to_char(trunc(add_months(sysdate,-8)),'dd-MON-yy') and sysdate "
			+ " order by SPL.child_product_id ,SPL.created_date";

	/**
	 * @author Aniket Chavan
	 * @description here we will get all quantity based on product name
	 */

	@Override
	public String getAllProductsDetail(final String siteId) {
		String queryForProductPriceDetails = "select pd.product_id,pd.name,sum(ROUND(NVL(spl.AMOUNT_PER_UNIT_AFTER_TAXES*spl.AVAILABLE_QUANTITY,0),2)) as  sumOfPrice from SUMADHURA_PRICE_LIST spl  join product pd on spl.product_id=pd.product_id and spl.site_id=? and spl.status='A'  group by pd.name,pd.product_id order by pd.product_id ";
		final String queryForSubProductPriceDetails = "select spd.SUB_PRODUCT_ID,spd.name,sum(ROUND(NVL(spl.AMOUNT_PER_UNIT_AFTER_TAXES*spl.AVAILABLE_QUANTITY,0),2)) as  sumOfPrice from SUMADHURA_PRICE_LIST spl  join SUB_PRODUCT spd on spl.SUB_PRODUCT_ID=spd.SUB_PRODUCT_ID and spl.status='A' and spl.product_id=? and spl.site_id=?  group by spd.SUB_PRODUCT_ID,spd.name order by spd.SUB_PRODUCT_ID desc";
		//		final String queryForChildProductPriceDetails = "select cpd.CHILD_PRODUCT_ID, cpd.SUB_PRODUCT_ID,cpd.name,sum(ROUND(NVL(spl.AMOUNT_AFTER_TAX,0),2)) as  sumOfPrice from SUMADHURA_PRICE_LIST spl  join CHILD_PRODUCT cpd on spl.CHILD_PRODUCT_ID=cpd.CHILD_PRODUCT_ID and spl.status='A' and spl.SUB_PRODUCT_ID=? and spl.site_id=?  group by cpd.SUB_PRODUCT_ID,cpd.CHILD_PRODUCT_ID,cpd.name order by cpd.SUB_PRODUCT_ID desc";
		final String queryForChildProductPriceDetails = "select cpd.CHILD_PRODUCT_ID, cpd.SUB_PRODUCT_ID,cpd.name,sum(ROUND(NVL( spl.AMOUNT_PER_UNIT_AFTER_TAXES*spl.AVAILABLE_QUANTITY ,0),2)) as  sumOfPrice from SUMADHURA_PRICE_LIST spl  join CHILD_PRODUCT cpd on spl.CHILD_PRODUCT_ID=cpd.CHILD_PRODUCT_ID and spl.status='A' and spl.SUB_PRODUCT_ID=? and spl.site_id=?  group by cpd.SUB_PRODUCT_ID,cpd.CHILD_PRODUCT_ID,cpd.name order by cpd.SUB_PRODUCT_ID desc";
		Object[] obj = { siteId };
		final StringBuffer xmlData = new StringBuffer();
		final StringBuffer bufferChildProd = new StringBuffer();
		final StringBuffer bufferSubProd = new StringBuffer();
		List<PriceMasterDTO> productList = jdbcTemplate.query(queryForProductPriceDetails, obj,
				new ResultSetExtractor<List<PriceMasterDTO>>() {
			List<PriceMasterDTO> mainProdList = new ArrayList<PriceMasterDTO>();
			StringBuffer bufferMainProd = new StringBuffer();

			@Override
			public List<PriceMasterDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
				xmlData.append("<xml>");
				bufferMainProd.append("<ProductList>");
				bufferMainProd.append("<Products>");

				while (rs.next()) {
					String product_id = rs.getString("product_id");
					String product_name = rs.getString("name");
					String productPrice = rs.getString("sumOfPrice");
					PriceMasterDTO mainProductList = new PriceMasterDTO();
					String replacedname = product_name.trim().replace(" ", "_");
					mainProductList.setChild_product_id(product_id);
					mainProductList.setChild_product_name(replacedname);
					mainProductList.setAmount_after_tax(productPrice);
					if (Integer.valueOf((int) Math.round(Double.valueOf(productPrice))) > 0) {
						bufferMainProd.append("<" + replacedname + ">" + productPrice + "</" + replacedname + ">");
					}

					// one List and return it
					mainProdList.add(mainProductList);
				} // while loop
				bufferMainProd.append("</Products>");
				bufferMainProd.append("</ProductList>");
				xmlData.append(bufferMainProd);
				return mainProdList;
			}
		});

		bufferChildProd.append("<ChildProductList>");
		bufferChildProd.append("<Products>");
		bufferSubProd.append("<SubProductList>");
		bufferSubProd.append("<Products>");
		final List<String> tempProdName=new ArrayList<String>();

		for (PriceMasterDTO priceMasterDTO : productList) {
			String product_id = priceMasterDTO.getChild_product_id();
			final String product_name = priceMasterDTO.getChild_product_name();
			Object[] obData = { product_id, siteId };
			jdbcTemplate.query(queryForSubProductPriceDetails, obData, new ResultSetExtractor<List<PriceMasterDTO>>() {

				@Override
				public List<PriceMasterDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
					bufferSubProd.append("<" + product_name + ">");
					while (rs.next()) {
						String sub_product_id = rs.getString("SUB_PRODUCT_ID");
						final String sub_product_name = rs.getString("name");
						String productPrice = rs.getString("sumOfPrice");
						tempProdName.add(0,sub_product_id);
						tempProdName.add(1,sub_product_name);

						PriceMasterDTO dto = new PriceMasterDTO();

						String replacedProductName = sub_product_name.trim().replace(":", "_").replace("%", "_")
						.replace("&", "_").replace("+", "_").replace("=", "_").replace("'", "_")
						.replace(",", "_").replace(".", "_").replace("\"", "_").replace(" ", "_")
						.replace("/", "").replace("(", "_").replace(")", "_");
						dto.setChild_product_id(sub_product_id);
						dto.setChild_product_name(sub_product_name);
						dto.setAmount_after_tax(productPrice);
						if (Integer.valueOf((int) Math.round(Double.valueOf(productPrice))) > 0) {
							bufferSubProd.append("<" + replacedProductName + ">" + productPrice + "</" + replacedProductName + ">");
						}

						/*Object[] obData = { sub_product_id, siteId };
						jdbcTemplate.query(queryForChildProductPriceDetails, obData,new ResultSetExtractor<List<PriceMasterDTO>>() {
									public boolean isLeadingDigit(final String value) {
										final char c = value.charAt(0);
										return (c >= '0' && c <= '9');
									}

									@Override
									public List<PriceMasterDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
										bufferChildProd.append("<" + product_name + ">");
										bufferChildProd.append(" <" + sub_product_name.replace(" ", "_") + ">0 </"+ sub_product_name.replace(" ", "_") + ">");

										while (rs.next()) {
											String child_product_id = rs.getString("CHILD_PRODUCT_ID");
											String product_name = rs.getString("name");
											if (isLeadingDigit(product_name)) {
												product_name = "_" + product_name;
											}

											String replacedProductName = product_name.trim().replace(":", "_").replace("%", "_")
													.replace("&", "_").replace("+", "_").replace("=", "_").replace("'", "_")
													.replace(",", "_").replace(".", "_").replace("\"", "_").replace(" ", "_")
													.replace("/", "").replace("(", "_").replace(")", "_");
											String productPrice = rs.getString("sumOfPrice");
											PriceMasterDTO dto = new PriceMasterDTO();
											dto.setChild_product_id(child_product_id);
											dto.setChild_product_name(product_name);
											dto.setAmount_after_tax(productPrice);
											if (Integer.valueOf((int) Math.round(Double.valueOf(productPrice))) > 0) {

												bufferChildProd.append(" <" + replacedProductName + ">" + productPrice + " </"
														+ replacedProductName + ">");
											}
										}
										bufferChildProd.append("</" + product_name + ">");
										return null;
									}
								});// Child product fetching code
						 */			}

					bufferSubProd.append("</" + product_name + ">");

					// xmlData.append(bufferSubProd);
					return null;
				}
			});// Sub Products fetching code
		}
		bufferChildProd.append("</Products>");
		bufferChildProd.append("</ChildProductList>");
		bufferSubProd.append("</Products>");
		bufferSubProd.append("</SubProductList>");
		xmlData.append(bufferSubProd);
		xmlData.append(bufferChildProd);

		xmlData.append("</xml>");

		return xmlData.toString();
	}

	@Override
	public String getChildProdcutsDetailByName(String siteId, final String product_name1) {
		final String product_name=product_name1.replace("_", " ");
		final String product_name_for_print=product_name1;

		final String queryForChildProductPriceDetails = "select cpd.CHILD_PRODUCT_ID, cpd.SUB_PRODUCT_ID,cpd.name,sum(ROUND(NVL( spl.AMOUNT_PER_UNIT_AFTER_TAXES*spl.AVAILABLE_QUANTITY ,0),2)) as  sumOfPrice from SUMADHURA_PRICE_LIST spl  join CHILD_PRODUCT cpd on spl.CHILD_PRODUCT_ID=cpd.CHILD_PRODUCT_ID and spl.status='A' and spl.SUB_PRODUCT_ID=? and spl.site_id=?  group by cpd.SUB_PRODUCT_ID,cpd.CHILD_PRODUCT_ID,cpd.name order by cpd.SUB_PRODUCT_ID desc";
		final String quryForSubProdName="select SPD.SUB_PRODUCT_ID,PD.PRODUCT_ID,SPD.NAME from SUB_PRODUCT SPD,PRODUCT PD WHERE  PD.PRODUCT_ID=SPD.PRODUCT_ID AND PD.NAME='"+product_name+"'";

		List<Map<String, Object>> subProNameList=jdbcTemplate.queryForList(quryForSubProdName);
		final StringBuffer bufferChildProd = new StringBuffer();
		System.out.println(subProNameList);
		for (Map<String, Object> map : subProNameList) {
			String subProdId=map.get("SUB_PRODUCT_ID").toString();
			String ProdId=	map.get("PRODUCT_ID").toString();
			final String sub_product_name=map.get("NAME").toString();
			Object[] obData = { subProdId, siteId };
			jdbcTemplate.query(queryForChildProductPriceDetails, obData,new ResultSetExtractor<List<PriceMasterDTO>>() {
				public boolean isLeadingDigit(final String value) {
					final char c = value.charAt(0);
					return (c >= '0' && c <= '9');
				}

				@Override
				public List<PriceMasterDTO> extractData(ResultSet rs) throws SQLException, DataAccessException {
					final StringBuffer tempbufferChildProd = new StringBuffer();
					tempbufferChildProd.append("<" +product_name_for_print + ">");
					tempbufferChildProd.append(" <" + sub_product_name.replace(" ", "_") + ">0 </"+ sub_product_name.replace(" ", "_") + ">");
					String productPrice ="";
					while (rs.next()) {
						String child_product_id = rs.getString("CHILD_PRODUCT_ID");
						String 	 product_name = rs.getString("name");
						if (isLeadingDigit(product_name)) {
							product_name = "_" + product_name;
						}

						String replacedProductName = product_name.trim().replace(":", "_").replace("%", "_")
						.replace("&", "_").replace("+", "_").replace("=", "_").replace("'", "_")
						.replace(",", "_").replace(".", "_").replace("\"", "_").replace(" ", "_")
						.replace("/", "").replace("(", "_").replace(")", "_");
						productPrice = rs.getString("sumOfPrice");
						PriceMasterDTO dto = new PriceMasterDTO();
						dto.setChild_product_id(child_product_id);
						dto.setChild_product_name(product_name);
						dto.setAmount_after_tax(productPrice);
						if (Integer.valueOf((int) Math.round(Double.valueOf(productPrice))) > 0) {
							tempbufferChildProd.append(" <" + replacedProductName + ">" + productPrice + " </"+ replacedProductName + ">");
						}
					}
					tempbufferChildProd.append("</" + product_name_for_print + ">");
					if(productPrice.length()!=0){
						bufferChildProd.append(tempbufferChildProd);					
					}
					return null;
				}

			});// Child product fetching code
		}
		return bufferChildProd.toString();
	}

	class ClassForProductPriceList implements ResultSetExtractor<Set<PriceMasterDTO>> {
		@Override
		public Set<PriceMasterDTO> extractData(ResultSet rs)
		throws SQLException, DataAccessException {
			Set<PriceMasterDTO> latestChildProdPriceData = new HashSet<PriceMasterDTO>();
			while (rs.next()) {
				String price_id = rs.getString("PRICE_ID");
				String child_product_id = rs.getString("CHILD_PRODUCT_ID");
				String name = rs.getString("NAME");
				String amount_per_unit_before_taxes = rs.getString("AMOUNT_PER_UNIT_BEFORE_TAXES");
				String amount_per_unit_after_taxes = rs.getString("AMOUNT_PER_UNIT_AFTER_TAXES");
				String measurement_name = rs.getString("MEASUREMENT_NAME")==null?"":rs.getString("MEASUREMENT_NAME");
				
				String site_id = rs.getString("site_id");
				String site_name = rs.getString("site_name");
				String invoice_number = rs.getString("nvl(ospl.INVOICE_NUMBER,'')")==null?"":rs.getString("nvl(ospl.INVOICE_NUMBER,'')");
				String dc_number = rs.getString("nvl(DE.DC_NUMBER,'')")==null?"":rs.getString("nvl(DE.DC_NUMBER,'')");
				String vendor_id = rs.getString("nvl(IE.VENDOR_ID,DE.VENDOR_ID)")==null?"":rs.getString("nvl(IE.VENDOR_ID,DE.VENDOR_ID)");
				String vendorName=rs.getString("nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME)")==null?"":rs.getString("nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME)");
				String indent_entry_id=rs.getString("INDENT_ENTRY_ID")==null?"":rs.getString("INDENT_ENTRY_ID"); 
				String dc_entry_id=rs.getString("DC_ENTRY_ID")==null?"":rs.getString("DC_ENTRY_ID");
				String indentType=rs.getString("INDENT_TYPE")==null?"":rs.getString("INDENT_TYPE");
				/*if(dc_number.equals("123123")){
					System.out.println(dc_number);
				}*/
				
				String indent_recive_date	=rs.getString("INVOICE_DATE")==null?"":rs.getString("INVOICE_DATE");
				String dc_recive_date=rs.getString("DC_DATE")==null?"":rs.getString("DC_DATE");
				
				if (StringUtils.isNotBlank(indent_recive_date)) {
					try {
						// create SimpleFOrmat object with source date format
						SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd");
						// parse the string into Date object
						Date date = sdfSource.parse(indent_recive_date.split(" ")[0]);

						// create SimpleDateFormat object with desired date format
						SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
						// parse the date into another format
						indent_recive_date = sdfDestination.format(date);
					} catch (Exception e) {
						log.info(e.getMessage());
					}
					
					//indent_recive_date = DateUtil.dateConversion(indent_recive_date);
				} else {
					indent_recive_date = "";
				}
				
				if (StringUtils.isNotBlank(dc_recive_date)) {
					try {
						// create SimpleFOrmat object with source date format
						SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd");
						// parse the string into Date object
						Date date = sdfSource.parse(dc_recive_date.split(" ")[0]);

						// create SimpleDateFormat object with desired date format
						SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
						// parse the date into another format
						dc_recive_date = sdfDestination.format(date);
					} catch (Exception e) {
						log.info(e.getMessage());
					}
				} else {
					dc_recive_date = "";
				}
				PriceMasterDTO pto=		new PriceMasterDTO(price_id == null ? "" : price_id.toString(),
						child_product_id == null ? "" : child_product_id.toString(), name == null ? "" : name.toString(), 
						measurement_name == null ? ""	: measurement_name.toString(),
						amount_per_unit_before_taxes == null ? "": amount_per_unit_before_taxes.toString(),
						amount_per_unit_after_taxes == null ? "": amount_per_unit_after_taxes.toString(), site_id,site_name
						,invoice_number,dc_number,vendor_id,vendorName,indent_entry_id,dc_entry_id,indent_recive_date,dc_recive_date);
				//System.out.println(pto);
				pto.setIndentType(indentType);
				latestChildProdPriceData.add(pto);
			}
			return latestChildProdPriceData;
		}
}

	@Override
	public Set<PriceMasterDTO> getProductPriceListBySite(String siteId,	String childProdName,String monthYear, String priceMasterType) {
		Set<PriceMasterDTO> latestChildProdPriceData = null;
		try {
/*			Object[] obj123={childProdName};
			String childProdName12=jdbcTemplate.queryForObject("select CHILD_PRODUCT_ID from CHILD_PRODUCT where NAME=?",obj123 ,String.class);
*/			 //this condition for selecting all the site max price of invoice 
			 if(childProdName.length() != 0&&siteId.length()==0&&monthYear.length()!=0){
			
				 String	queryForSelectAllSiteMaxPrice="select nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,OSPL.site_id,OSPL.CHILD_PRODUCT_ID,max(to_char(OSPL.CREATED_DATE,'dd-MON-yy hh:MM:ss')), ROUND(TO_CHAR(OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2) as AMOUNT_PER_UNIT_BEFORE_TAXES, ROUND(TO_CHAR(AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES ,OSPL.BASIC_AMOUNT ,CP.NAME,mmt.name as MEASUREMENT_NAME,OSPL.PRICE_ID "
								+ " from SUMADHURA_PRICE_LIST OSPL left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=OSPL.INDENT_ENTRY_DETAILS_ID "
								+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID  "
								+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
								+ " left outer join DC_FORM DF on OSPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID "
								+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "
								+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID "
								+ " ,SITE S ,CHILD_PRODUCT CP,MEASUREMENT mmt "
								+ " where OSPL. CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID ";
								queryForSelectAllSiteMaxPrice+= " and S.site_id=OSPL.site_id and mmt.MEASUREMENT_ID=OSPL.UNITS_OF_MEASUREMENT ";
								if(priceMasterType.equals("DeptWisePriceMaster")){
									if(siteId.length()==0||siteId.equals("996"))
									queryForSelectAllSiteMaxPrice+= " and OSPL.site_id!='996' ";
								}
								queryForSelectAllSiteMaxPrice+= " and lower(CP.NAME)=lower(?)      and OSPL.CREATED_DATE in (select max(ispl.CREATED_DATE) from SUMADHURA_PRICE_LIST ispl where ispl.CHILD_PRODUCT_ID = OSPL.CHILD_PRODUCT_ID and  ispl.site_id=OSPL.site_id   and  to_date(to_char(ispl.created_date,'MM-yyyy'),'MM-yyyy') =TO_DATE('"+monthYear+"', 'MM-yyyy')   group by ispl.site_id,ispl.CHILD_PRODUCT_ID  ) "
								+ " group by nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,OSPL.site_id,OSPL.CHILD_PRODUCT_ID,OSPL.CREATED_DATE,OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES,OSPL.BASIC_AMOUNT,CP.NAME,mmt.name,  AMOUNT_PER_UNIT_AFTER_TAXES,OSPL.PRICE_ID ORDER BY OSPL.site_id,OSPL.CREATED_DATE,OSPL.PRICE_ID DESC";
					
				 
							Object[] objData = {childProdName};
						latestChildProdPriceData = jdbcTemplate.query(queryForSelectAllSiteMaxPrice,objData, new ClassForProductPriceList());
						return  latestChildProdPriceData;
				}else if(childProdName.length() != 0&&siteId.length()!=0&&monthYear.length()!=0){
					String	queryForSelectAllSiteMaxPrice="select nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,OSPL.site_id,OSPL.CHILD_PRODUCT_ID,max(to_char(OSPL.CREATED_DATE,'dd-MON-yy hh:MM:ss')), ROUND(TO_CHAR(OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2) as AMOUNT_PER_UNIT_BEFORE_TAXES, ROUND(TO_CHAR(AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES ,OSPL.BASIC_AMOUNT ,CP.NAME,mmt.name as MEASUREMENT_NAME,OSPL.PRICE_ID "
							+ " from SUMADHURA_PRICE_LIST OSPL left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=OSPL.INDENT_ENTRY_DETAILS_ID "
							+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID   "
							+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
							+ " left outer join DC_FORM DF on OSPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID "
							+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "
							+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID "
							+ " ,SITE S ,CHILD_PRODUCT CP,MEASUREMENT mmt "
							+ " where OSPL. CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID "
							+ " and S.site_id=OSPL.site_id and mmt.MEASUREMENT_ID=OSPL.UNITS_OF_MEASUREMENT "
							+ " and lower(CP.NAME)=lower(?)      and ospl.site_id='"+siteId+"' and OSPL.CREATED_DATE in (select max(ispl.CREATED_DATE) from SUMADHURA_PRICE_LIST ispl where ispl.CHILD_PRODUCT_ID = OSPL.CHILD_PRODUCT_ID and  ispl.site_id=OSPL.site_id  and  to_date(to_char(ispl.created_date,'MM-yyyy'),'MM-yyyy') =TO_DATE('"+monthYear+"', 'MM-yyyy')  and ispl.site_id='"+siteId+"'   group by ispl.site_id,ispl.CHILD_PRODUCT_ID  ) "
							+ " group by nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,OSPL.site_id,OSPL.CHILD_PRODUCT_ID,OSPL.CREATED_DATE,OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES,OSPL.BASIC_AMOUNT,CP.NAME,mmt.name,  AMOUNT_PER_UNIT_AFTER_TAXES,OSPL.PRICE_ID ORDER BY OSPL.site_id,OSPL.CREATED_DATE,OSPL.PRICE_ID DESC";
					Object[] objData = {childProdName};
					latestChildProdPriceData = jdbcTemplate.query(queryForSelectAllSiteMaxPrice,objData, new ClassForProductPriceList());
					return  latestChildProdPriceData;

				} else if(childProdName.length() != 0&&siteId.length()==0&&monthYear.length()==0){
				      String queryForSelectAllSiteMaxPrice="";
				      queryForSelectAllSiteMaxPrice="select nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,OSPL.site_id,OSPL.CHILD_PRODUCT_ID,max(to_char(OSPL.CREATED_DATE,'dd-MON-yy hh:MM:ss')), ROUND(TO_CHAR(OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2) as AMOUNT_PER_UNIT_BEFORE_TAXES, ROUND(TO_CHAR(AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES ,OSPL.BASIC_AMOUNT ,CP.NAME,mmt.name as MEASUREMENT_NAME,OSPL.PRICE_ID "
									+ " from SUMADHURA_PRICE_LIST OSPL left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=OSPL.INDENT_ENTRY_DETAILS_ID "
									+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID   "// and IE.INDENT_TYPE!='INO'
									+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
									+ " left outer join DC_FORM DF on OSPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID "
									+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "
									+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID "
									+ " ,SITE S ,CHILD_PRODUCT CP,MEASUREMENT mmt "
									+ " where OSPL. CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID ";//      
									queryForSelectAllSiteMaxPrice+= " and S.site_id=OSPL.site_id and mmt.MEASUREMENT_ID=OSPL.UNITS_OF_MEASUREMENT ";
									if(priceMasterType.equals("DeptWisePriceMaster")){
										if(siteId.length()==0||siteId.equals("996"))
										queryForSelectAllSiteMaxPrice+= " and OSPL.site_id!='996' ";
									
									}
									
									queryForSelectAllSiteMaxPrice+= " and lower(CP.NAME)=lower(?)  and OSPL.CREATED_DATE in (select max(ispl.CREATED_DATE) from SUMADHURA_PRICE_LIST ispl where  ispl.CHILD_PRODUCT_ID = OSPL.CHILD_PRODUCT_ID and  ispl.site_id=OSPL.site_id group by ispl.site_id,ispl.CHILD_PRODUCT_ID  ) "
									+ " group by nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,OSPL.site_id,OSPL.CHILD_PRODUCT_ID,OSPL.CREATED_DATE,OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES,OSPL.BASIC_AMOUNT,CP.NAME,mmt.name,  AMOUNT_PER_UNIT_AFTER_TAXES,OSPL.PRICE_ID ORDER BY OSPL.site_id,OSPL.CREATED_DATE,OSPL.PRICE_ID DESC";
				Object[] objData = {childProdName};
				latestChildProdPriceData = jdbcTemplate.query(queryForSelectAllSiteMaxPrice,objData, new ClassForProductPriceList());
				return  latestChildProdPriceData;
			}else if(childProdName.length() != 0&&monthYear.length()!=0){
				String	queryForSelectAllSiteMaxPrice="select nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,OSPL.site_id,OSPL.CHILD_PRODUCT_ID,max(to_char(OSPL.CREATED_DATE,'dd-MON-yy hh:MM:ss')), ROUND(TO_CHAR(OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2) as AMOUNT_PER_UNIT_BEFORE_TAXES, ROUND(TO_CHAR(AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES ,OSPL.BASIC_AMOUNT ,CP.NAME,mmt.name as MEASUREMENT_NAME,OSPL.PRICE_ID "
						+ " from SUMADHURA_PRICE_LIST OSPL left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=OSPL.INDENT_ENTRY_DETAILS_ID "
						+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID     "
						+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
						+ " left outer join DC_FORM DF on OSPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID "
						+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "
						+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID "
						+ " ,SITE S ,CHILD_PRODUCT CP,MEASUREMENT mmt "
						+ " where OSPL. CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID "
						+ " and S.site_id=OSPL.site_id and mmt.MEASUREMENT_ID=OSPL.UNITS_OF_MEASUREMENT "
						+ " and lower(CP.NAME)=lower(?)      and OSPL.CREATED_DATE in (select max(ispl.CREATED_DATE) from SUMADHURA_PRICE_LIST ispl where ispl.CHILD_PRODUCT_ID = OSPL.CHILD_PRODUCT_ID and  ispl.site_id=OSPL.site_id   and  to_date(to_char(ispl.created_date,'MM-yyyy'),'MM-yyyy') =TO_DATE('"+monthYear+"', 'MM-yyyy')   group by ispl.site_id,ispl.CHILD_PRODUCT_ID  ) "
						+ " group by nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,OSPL.site_id,OSPL.CHILD_PRODUCT_ID,OSPL.CREATED_DATE,OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES,OSPL.BASIC_AMOUNT,CP.NAME,mmt.name,  AMOUNT_PER_UNIT_AFTER_TAXES,OSPL.PRICE_ID ORDER BY OSPL.site_id,OSPL.CREATED_DATE,OSPL.PRICE_ID DESC";
				Object[] objData = {childProdName};
				latestChildProdPriceData = jdbcTemplate.query(queryForSelectAllSiteMaxPrice,objData, new ClassForProductPriceList());
				return  latestChildProdPriceData;
			}else if(siteId.length()!=0&&monthYear.length()!=0){
				String	queryForSelectAllSiteMaxPrice="select nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,OSPL.site_id,OSPL.CHILD_PRODUCT_ID,max(to_char(OSPL.CREATED_DATE,'dd-MON-yy hh:MM:ss')), ROUND(TO_CHAR(OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2) as AMOUNT_PER_UNIT_BEFORE_TAXES, ROUND(TO_CHAR(AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES ,OSPL.BASIC_AMOUNT ,CP.NAME,mmt.name as MEASUREMENT_NAME,OSPL.PRICE_ID "
						+ " from SUMADHURA_PRICE_LIST OSPL left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=OSPL.INDENT_ENTRY_DETAILS_ID "
						+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID    "
						+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
						+ " left outer join DC_FORM DF on OSPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID "
						+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "
						+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID "
						+ " ,SITE S ,CHILD_PRODUCT CP,MEASUREMENT mmt "
						+ " where OSPL. CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID "
						+ " and S.site_id=OSPL.site_id and mmt.MEASUREMENT_ID=OSPL.UNITS_OF_MEASUREMENT "
						+ " and ospl.site_id='"+siteId+"'      and OSPL.CREATED_DATE in (select max(ispl.CREATED_DATE) from SUMADHURA_PRICE_LIST ispl where ispl.CHILD_PRODUCT_ID = OSPL.CHILD_PRODUCT_ID and  ispl.site_id=OSPL.site_id  and  to_date(to_char(ispl.created_date,'MM-yyyy'),'MM-yyyy') =TO_DATE('"+monthYear+"', 'MM-yyyy')  and ispl.site_id='"+siteId+"'   group by ispl.site_id,ispl.CHILD_PRODUCT_ID  ) "
						+ " group by nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,OSPL.site_id,OSPL.CHILD_PRODUCT_ID,OSPL.CREATED_DATE,OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES,OSPL.BASIC_AMOUNT,CP.NAME,mmt.name,  AMOUNT_PER_UNIT_AFTER_TAXES,OSPL.PRICE_ID ORDER BY OSPL.site_id,OSPL.CREATED_DATE,OSPL.PRICE_ID DESC";
				Object[] objData = {};
				latestChildProdPriceData = jdbcTemplate.query(queryForSelectAllSiteMaxPrice,objData, new ClassForProductPriceList());
				return  latestChildProdPriceData;

			}
			
			if (!(childProdName.length() == 0)) {
				Object[] objData = { siteId, childProdName,siteId };
						String queryByChildProdNameAndSite="select nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,ospl.site_id,ospl.CHILD_PRODUCT_ID,max(to_char(ospl.CREATED_DATE,'dd-MON-yy hh:MM:ss')), ROUND(TO_CHAR(OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2) as AMOUNT_PER_UNIT_BEFORE_TAXES, ROUND(TO_CHAR(OSPL.AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES "
						+ " ,OSPL.BASIC_AMOUNT ,CP.NAME,mmt.name as MEASUREMENT_NAME,ospl.PRICE_ID "
						+ " from SUMADHURA_PRICE_LIST ospl left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=ospl.INDENT_ENTRY_DETAILS_ID "
						+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID   "
						+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
						+ " left outer join DC_FORM DF on ospl.DC_FORM_ENTRY_ID=DF.DC_FORM_ID and DF.status='A' "
						+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID and DE.INDENT_ENTRY_ID is null "
						+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID ,SITE S ,CHILD_PRODUCT CP,MEASUREMENT mmt "
						+ " where ospl.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID and S.site_id=ospl.site_id  and mmt.MEASUREMENT_ID=ospl.UNITS_OF_MEASUREMENT and ospl.site_id=? "
						+ " and lower(CP.NAME)=lower(?)      and ospl.CREATED_DATE = "
						+ " (select max(CREATED_DATE) from SUMADHURA_PRICE_LIST ispl where ispl.CHILD_PRODUCT_ID = ospl.CHILD_PRODUCT_ID and  ispl.site_id=OSPL.site_id  and ispl.site_id=?) "
						+ " group by nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_TYPE,IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(OSPL.INVOICE_NUMBER,''),s.site_name,ospl.site_id,ospl.CHILD_PRODUCT_ID,ospl.CREATED_DATE,OSPL.AMOUNT_PER_UNIT_BEFORE_TAXES,OSPL.BASIC_AMOUNT,CP.NAME,mmt.name, "
						+ " OSPL.AMOUNT_PER_UNIT_AFTER_TAXES,ospl.PRICE_ID ORDER BY OSPL.CHILD_PRODUCT_ID,OSPL.CREATED_DATE,OSPL.PRICE_ID DESC";
				latestChildProdPriceData = jdbcTemplate.query(queryByChildProdNameAndSite,objData, new ClassForProductPriceList());
				return  latestChildProdPriceData;
			} else {
				Object[] objData1 = { siteId, siteId };
				latestChildProdPriceData = jdbcTemplate.query(QUERY_FOR_CHILD_PRODUCT_PRICE_LIST, objData1,new ClassForProductPriceList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return latestChildProdPriceData;
	}

	// this class for extracting data from resultset
	static class LastThreeMonthsDataExtractor implements ResultSetExtractor<Set<PriceMasterDTO>> {
		static {
			//System.out.println("ReportsDaoImpl.LastThreeMonthsDataExtractor.LastThreeMonthsDataExtractor()");
		}

		public LastThreeMonthsDataExtractor() {
			//System.out.println("ReportsDaoImpl.LastThreeMonthsDataExtractor.LastThreeMonthsDataExtractor()");
		}

		@Override
		public Set<PriceMasterDTO> extractData(ResultSet rs)throws SQLException, DataAccessException {
			Set<PriceMasterDTO> threeMonthsData = new HashSet<PriceMasterDTO>();
			String monthNumber,price_id, child_product_id, name, measurement_name, amount_per_unit_before_taxes, amount_per_unit_after_taxes, available_quantity, basic_amount, total_amount, amount_after_tax, createddate, site_id;
			while (rs.next()) {
				price_id = rs.getString("PRICE_ID");
				child_product_id = rs.getString("CHILD_PRODUCT_ID");
				name = rs.getString("NAME");
				measurement_name = rs.getString("MEASUREMENT_NAME");
				amount_per_unit_before_taxes = rs.getString("AMOUNT_PER_UNIT_BEFORE_TAXES")==null?"0":rs.getString("AMOUNT_PER_UNIT_BEFORE_TAXES");
				amount_per_unit_after_taxes = rs.getString("AMOUNT_PER_UNIT_AFTER_TAXES")==null?rs.getString("AMOUNT_PER_UNIT_BEFORE_TAXES"):rs.getString("AMOUNT_PER_UNIT_AFTER_TAXES");
				available_quantity = rs.getString("AVAILABLE_QUANTITY")==null?"03":rs.getString("AVAILABLE_QUANTITY");
				basic_amount = rs.getString("BASIC_AMOUNT")==null?"0": rs.getString("BASIC_AMOUNT");
				total_amount = rs.getString("TOTAL_AMOUNT")==null?"0":rs.getString("TOTAL_AMOUNT");
				amount_after_tax = rs.getString("AMOUNT_AFTER_TAX")==null?"0":rs.getString("AMOUNT_AFTER_TAX");
				createddate = rs.getString("createdDate");
				monthNumber = rs.getString("monthNumber");
				site_id = rs.getString("SITE_ID");
				String monthName=rs.getString("MonthName")==null?"":rs.getString("MonthName");
				String invoice_number = rs.getString("nvl(SPL.INVOICE_NUMBER,'')")==null?"":rs.getString("nvl(SPL.INVOICE_NUMBER,'')");
				String dc_number = rs.getString("nvl(DE.DC_NUMBER,'')")==null?"":rs.getString("nvl(DE.DC_NUMBER,'')");
				String vendor_id = rs.getString("nvl(IE.VENDOR_ID,DE.VENDOR_ID)")==null?"":rs.getString("nvl(IE.VENDOR_ID,DE.VENDOR_ID)");
				String vendorName=rs.getString("nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME)")==null?"":rs.getString("nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME)");
				String indent_entry_id=rs.getString("INDENT_ENTRY_ID")==null?"":rs.getString("INDENT_ENTRY_ID"); 
				String dc_entry_id=rs.getString("DC_ENTRY_ID")==null?"":rs.getString("DC_ENTRY_ID");
				/*if(dc_number.equals("123123")){
					System.out.println(dc_number);
				}*/
				
				String indent_recive_date	=rs.getString("INVOICE_DATE")==null?"":rs.getString("INVOICE_DATE");
				String dc_recive_date=rs.getString("DC_DATE")==null?"":rs.getString("DC_DATE");
				
				if (StringUtils.isNotBlank(indent_recive_date)) {
					try {
						// create SimpleFOrmat object with source date format
						SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd");
						// parse the string into Date object
						Date date = sdfSource.parse(indent_recive_date.split(" ")[0]);

						// create SimpleDateFormat object with desired date format
						SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
						// parse the date into another format
						indent_recive_date = sdfDestination.format(date);
					} catch (Exception e) {
						log.info(e.getMessage());
					}
					
					//indent_recive_date = DateUtil.dateConversion(indent_recive_date);
				} else {
					indent_recive_date = "";
				}
				
				if (StringUtils.isNotBlank(dc_recive_date)) {
					try {
						// create SimpleFOrmat object with source date format
						SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd");
						// parse the string into Date object
						Date date = sdfSource.parse(dc_recive_date.split(" ")[0]);

						// create SimpleDateFormat object with desired date format
						SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
						// parse the date into another format
						dc_recive_date = sdfDestination.format(date);
					} catch (Exception e) {
						log.info(e.getMessage());
					}
				} else {
					dc_recive_date = "";
				}
				
				threeMonthsData.add(new PriceMasterDTO(price_id == null ? "": price_id.toString(), child_product_id == null ? "": child_product_id.toString(), name == null ? "" : name.toString(),
						measurement_name == null ? "" : measurement_name.toString(),
						amount_per_unit_before_taxes == null ? "" : amount_per_unit_before_taxes.toString(),
						amount_per_unit_after_taxes == null ? "" : amount_per_unit_after_taxes.toString(),
						available_quantity == null ? "" : available_quantity .toString(), 
						basic_amount == null ? ""	: basic_amount.toString(),
						total_amount == null ? "" : total_amount.toString(),
						amount_after_tax == null ? "" : amount_after_tax.toString(),
						createddate == null ? "": createddate.toString(),
						site_id == null ? ""	: site_id.toString(), monthName,null,monthNumber,invoice_number,dc_number,vendor_id,vendorName,indent_entry_id,dc_entry_id,indent_recive_date,dc_recive_date));
			}
			return threeMonthsData;
		}
	}

	@Override
	public List<Map<String, Object>> getLastMonthsNamesForPriceMaster(String childProductId, String prodName, String priceId,
			String site_id,String monthYear) {
		Object[] obj = { childProductId, site_id };
		if(monthYear.length()!=0&&prodName.length()!=0){
			String query="select to_char(SPL.created_date,'MONyy') as createdDate,to_char(SPL.created_date,'MM') as monthNumber ,to_char(SPL.created_date,'yyyy') as yearNumber    from  CHILD_PRODUCT cp,SUMADHURA_PRICE_LIST SPL,MEASUREMENT mmt     where  SPL.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID and mmt.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT "
					+ " AND SPL.CHILD_PRODUCT_ID=? and SPL.site_id=? and  to_date(to_char(SPL.created_date,'MM-yyyy'),'MM-yyyy') <=TO_DATE(?, 'MM-yyyy') "
					+ " order by SPL.child_product_id ,SPL.created_date";
			Object[] obj1 = { childProductId, site_id,monthYear };	
			return 	jdbcTemplate.queryForList(query, obj1);
		}
		
		if(prodName.length()==0){
			String query="select to_char(SPL.created_date,'MONyy') as createdDate,to_char(SPL.created_date,'MM') as monthNumber,to_char(SPL.created_date,'yyyy') as yearNumber      from  CHILD_PRODUCT cp,SUMADHURA_PRICE_LIST SPL,MEASUREMENT mmt     where  SPL.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID and mmt.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT "
			+ " AND SPL.CHILD_PRODUCT_ID=? and SPL.site_id=? and SPL.created_date "
			+ " between to_char(trunc(add_months(sysdate,-18)),'dd-MON-yy') and to_char(trunc(add_months(sysdate,-8)),'dd-MON-yy')  "
			+ " order by SPL.child_product_id ,SPL.created_date";
			return 	jdbcTemplate.queryForList(query, obj);	
		}else{
			return 	jdbcTemplate.queryForList(QUERY_FOR_CHILD_PRODUCT_PRICE_MONTH_NAME, obj);
		}
	}
	
	@Override
	public Set<PriceMasterDTO> getLastThreeMonthPriceMasterDetail( String childProductId, String prodName, String priceId, String site_id, String monthYear) {
		Object[] obj = { childProductId, site_id };

		Set<PriceMasterDTO> lastThreeMonthsData = null;
		if(monthYear.length()!=0&&prodName.length()!=0){
			String queryUsingChildIdAndMonth="";
			if(!site_id.equals("996")){
			queryUsingChildIdAndMonth="select nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),initCap(to_char(SPL.created_date,'MONTH-yyyy')) as MonthName,nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(spl.INVOICE_NUMBER,''),SPL.price_id,cp.CHILD_PRODUCT_ID,(cp.NAME),mmt.name as MEASUREMENT_NAME,ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2)as  AMOUNT_PER_UNIT_BEFORE_TAXES ,  ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES,SPL.AVAILABLE_QUANTITY, NVL(ROUND(TO_CHAR(SPL.BASIC_AMOUNT),2), '0.0') as BASIC_AMOUNT,        NVL(ROUND(TO_CHAR(SPL.TOTAL_AMOUNT),2), '0.0') as TOTAL_AMOUNT,NVL(SPL.AMOUNT_AFTER_TAX, SPL.BASIC_AMOUNT) as AMOUNT_AFTER_TAX,to_char(SPL.created_date,'MONyy') as createdDate,to_char(SPL.created_date,'MM') as monthNumber,SPL.site_id "
					+ " From  CHILD_PRODUCT cp,SUMADHURA_PRICE_LIST SPL "
					+ " left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID "
					+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID   "
					+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
					+ " left outer join DC_FORM DF on SPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID  "//and DF.status='A'
					+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "//and DE.INDENT_ENTRY_ID is null 
					+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID ,MEASUREMENT mmt "
					+ " where  SPL.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID and mmt.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT "
					+ " AND SPL.CHILD_PRODUCT_ID=? and SPL.site_id=? and       to_date(to_char(SPL.created_date,'MM-yyyy'),'MM-yyyy') =TO_DATE('"+monthYear+"', 'MM-yyyy') "
					+ " order by SPL.child_product_id ,SPL.created_date"; 
			}else{
				queryUsingChildIdAndMonth="select nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),initCap(to_char(SPL.created_date,'MONTH-yyyy')) as MonthName,nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(spl.INVOICE_NUMBER,''),SPL.price_id,cp.CHILD_PRODUCT_ID,(cp.NAME),mmt.name as MEASUREMENT_NAME,ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2)as  AMOUNT_PER_UNIT_BEFORE_TAXES ,  ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES,SPL.AVAILABLE_QUANTITY, NVL(ROUND(TO_CHAR(SPL.BASIC_AMOUNT),2), '0.0') as BASIC_AMOUNT,        NVL(ROUND(TO_CHAR(SPL.TOTAL_AMOUNT),2), '0.0') as TOTAL_AMOUNT,NVL(SPL.AMOUNT_AFTER_TAX, SPL.BASIC_AMOUNT) as AMOUNT_AFTER_TAX,to_char(SPL.created_date,'MONyy') as createdDate,to_char(SPL.created_date,'MM') as monthNumber,SPL.site_id "
					+ " From  CHILD_PRODUCT cp,MRKT_EXPENDATURE  MRKT_EXPD,SUMADHURA_PRICE_LIST SPL "
					+ " left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID "
					+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID   "
					+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
					+ " left outer join DC_FORM DF on SPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID  "//and DF.status='A'
					+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "//and DE.INDENT_ENTRY_ID is null 
					+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID ,MEASUREMENT mmt "
					+ " where  SPL.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID and mmt.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT and  MRKT_EXPD.STATUS = 'A' and SPL.INVOICE_NUMBER=MRKT_EXPD.INVOICE_ID "
					+ " AND SPL.CHILD_PRODUCT_ID=? and SPL.site_id=? and       to_date(to_char(SPL.created_date,'MM-yyyy'),'MM-yyyy') =TO_DATE('"+monthYear+"', 'MM-yyyy') "
					+ " order by SPL.child_product_id ,SPL.created_date";
			}
			
			lastThreeMonthsData = jdbcTemplate.query(queryUsingChildIdAndMonth, obj,	new LastThreeMonthsDataExtractor());
			return lastThreeMonthsData;
		}
		if (prodName.length()!=0) {
			lastThreeMonthsData = jdbcTemplate.query(QUERY_FOR_CHILD_PRODUCT_PRICE_BY_ID, obj,	new LastThreeMonthsDataExtractor());
		} else {
			System.out.println(prodName);
			String query="";
			if(!site_id.equals("996")){
			 query="select  nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),initCap(to_char(SPL.created_date,'MONTH-yyyy')) as MonthName,nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(SPL.INVOICE_NUMBER,''),SPL.price_id,cp.CHILD_PRODUCT_ID,(cp.NAME),mmt.name as MEASUREMENT_NAME,ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2)as  AMOUNT_PER_UNIT_BEFORE_TAXES ,  ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES,SPL.AVAILABLE_QUANTITY, "
					+ " NVL(ROUND(TO_CHAR(SPL.BASIC_AMOUNT),2), '0.0') as BASIC_AMOUNT,NVL(ROUND(TO_CHAR(SPL.TOTAL_AMOUNT),2), '0.0') as TOTAL_AMOUNT, "
					+ " NVL(SPL.AMOUNT_AFTER_TAX, SPL.BASIC_AMOUNT) as AMOUNT_AFTER_TAX,to_char(SPL.created_date,'MONyy') as createdDate, "
					+ " to_char(SPL.created_date,'MM') as monthNumber,SPL.site_id  "
					+ " from  CHILD_PRODUCT cp,SUMADHURA_PRICE_LIST SPL   left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID "
					+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID    "
					+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
					+ " left outer join DC_FORM DF on SPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID "// and DF.status='A'
					+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "// and DE.INDENT_ENTRY_ID is null 
					+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID ,MEASUREMENT mmt "
					+ " where  SPL.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID and mmt.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT "
					+ " AND SPL.CHILD_PRODUCT_ID=?         and SPL.site_id=? and SPL.created_date "
					+ " between to_char(trunc(add_months(sysdate,-18)),'dd-MON-yy') and to_char(trunc(add_months(sysdate,-8)),'dd-MON-yy')   "
					+ " order by SPL.child_product_id ,SPL.created_date";
			}else{
				 query="select  nvl(VD.VENDOR_NAME,VD1.VENDOR_NAME),initCap(to_char(SPL.created_date,'MONTH-yyyy')) as MonthName,nvl(IE.VENDOR_ID,DE.VENDOR_ID),DE.DC_ENTRY_ID,DE.DC_DATE,nvl(DE.DC_NUMBER,''),IE.INDENT_ENTRY_ID,IE.INVOICE_DATE,nvl(SPL.INVOICE_NUMBER,''),SPL.price_id,cp.CHILD_PRODUCT_ID,(cp.NAME),mmt.name as MEASUREMENT_NAME,ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_BEFORE_TAXES),2)as  AMOUNT_PER_UNIT_BEFORE_TAXES ,  ROUND(TO_CHAR(SPL.AMOUNT_PER_UNIT_AFTER_TAXES),2) as AMOUNT_PER_UNIT_AFTER_TAXES,SPL.AVAILABLE_QUANTITY, "
						+ " NVL(ROUND(TO_CHAR(SPL.BASIC_AMOUNT),2), '0.0') as BASIC_AMOUNT,NVL(ROUND(TO_CHAR(SPL.TOTAL_AMOUNT),2), '0.0') as TOTAL_AMOUNT, "
						+ " NVL(SPL.AMOUNT_AFTER_TAX, SPL.BASIC_AMOUNT) as AMOUNT_AFTER_TAX,to_char(SPL.created_date,'MONyy') as createdDate, "
						+ " to_char(SPL.created_date,'MM') as monthNumber,SPL.site_id  "
						+ " from  CHILD_PRODUCT cp,MRKT_EXPENDATURE  MRKT_EXPD,SUMADHURA_PRICE_LIST SPL   left outer join INDENT_ENTRY_DETAILS IED on IED.INDENT_ENTRY_DETAILS_ID=SPL.INDENT_ENTRY_DETAILS_ID "
						+ " left outer join INDENT_ENTRY IE on IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID    "
						+ " left outer join VENDOR_DETAILS VD on VD.VENDOR_ID=IE.VENDOR_ID "
						+ " left outer join DC_FORM DF on SPL.DC_FORM_ENTRY_ID=DF.DC_FORM_ID "// and DF.status='A'
						+ " left outer join DC_ENTRY DE on  DF.DC_ENTRY_ID=DE.DC_ENTRY_ID "// and DE.INDENT_ENTRY_ID is null 
						+ " left outer join VENDOR_DETAILS VD1 on VD1.VENDOR_ID=DE.VENDOR_ID ,MEASUREMENT mmt "
						+ " where  SPL.CHILD_PRODUCT_ID=cp.CHILD_PRODUCT_ID and mmt.MEASUREMENT_ID=SPL.UNITS_OF_MEASUREMENT "
						+ " AND SPL.CHILD_PRODUCT_ID=?         and SPL.site_id=? and  MRKT_EXPD.STATUS = 'A' and SPL.INVOICE_NUMBER=MRKT_EXPD.INVOICE_ID and SPL.created_date "
						+ " between to_char(trunc(add_months(sysdate,-18)),'dd-MON-yy') and to_char(trunc(add_months(sysdate,-8)),'dd-MON-yy')   "
						+ " order by SPL.child_product_id ,SPL.created_date";
			}
			lastThreeMonthsData = jdbcTemplate.query(query,obj, new LastThreeMonthsDataExtractor());
		}
		return lastThreeMonthsData;
	}
	
	public static Date addDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return cal.getTime();
	}
	
	@Override
	public String getRequestedAmountReportBySite(String siteId,String tillDatePaymentReq) {
		java.util.Date currentDate=new java.util.Date();
		String dateFormat=new SimpleDateFormat("dd-MM-yy").format(currentDate);
		System.out.println("currentDate "+dateFormat);
		String queryForRequestedAmountReport = "SELECT SUM(AADPP.ACCOUNT_DEPT_REQ_REC_AMOUNT) as TOTAL_REQUESTED_AMT,to_char(ACD.PAYMENT_REQ_DATE,'DD-mm-yy') AS REQUESTED_DATE ,S.ADDRESS "
			+ " From ACC_ACCOUNTS_DEPT_PMT_PROSS AADPP,ACC_PAYMENT_DTLS ACD,ACC_PAYMENT AC,SITE S "
			+ " Where AADPP.PAYMENT_DETAILS_ID=ACD.PAYMENT_DETAILS_ID AND AC.PAYMENT_ID=ACD.PAYMENT_ID AND S.SITE_ID=AC.SITE_ID "  /*AND AADPP.PROCESS_INTIATE_AMOUNT=0*/
			+ "	AND AADPP.STATUS='A' and ACD.PAYMENT_REQ_DATE BETWEEN to_date('"+dateFormat+"','dd-MM-yy') and sysdate+6 "
			+ " group by  to_char(ACD.PAYMENT_REQ_DATE,'DD-mm-yy'), S.ADDRESS  order by to_char(ACD.PAYMENT_REQ_DATE,'DD-mm-yy')";

		String queryForLocation = "SELECT MAX(S.SITE_ID),S.ADDRESS FROM SITE S,ACC_PAYMENT AC WHERE AC.SITE_ID=S.SITE_ID GROUP BY S.ADDRESS";


		final StringBuffer xmlreqAMTRpt = new StringBuffer();
		xmlreqAMTRpt.append("<xml>");

		List<Map<String, Object>> locations = jdbcTemplate.queryForList(queryForLocation);
		List<Map<String, Object>> paymentRequestData = jdbcTemplate.queryForList(queryForRequestedAmountReport);
		//   get the next 7 days data 
		int days=0;
		double sumOfAmountDateWise=0;

		List<String> nextSevenDays=new ArrayList<String>();
		List<String> nextSevenDaysFromDB=new ArrayList<String>();

		Date date = new Date();
		int day=0;//24-09-18
		while(day<=6){
			Date newDate = addDays(date, day);
			String newdate=new SimpleDateFormat("dd-MM-yy").format(newDate);
			nextSevenDays.add(newdate);
			day++;
		}

		for (int i = 0; i < paymentRequestData.size()&&days<Integer.valueOf(tillDatePaymentReq); i++) {
			sumOfAmountDateWise=0;
			StringBuffer tempXmlreqAMTRpt = new StringBuffer();
			List<String> locationNames = new ArrayList<String>();
			tempXmlreqAMTRpt.append("<label>");
			Map<String, Object> firstRecord = paymentRequestData.get(i);
			String requested_date = String.valueOf(firstRecord.get("REQUESTED_DATE"));
			String location_name = String.valueOf(firstRecord.get("ADDRESS"));
			locationNames.add(location_name);

			if(!nextSevenDaysFromDB.contains(requested_date)){
				nextSevenDaysFromDB.add(requested_date);
			}
			String total_requested_amt = String.valueOf(firstRecord.get("TOTAL_REQUESTED_AMT"));
			sumOfAmountDateWise+=Double.valueOf(total_requested_amt);
			tempXmlreqAMTRpt.append("<name>" + requested_date.toString() + "</name>");
			tempXmlreqAMTRpt.append("<" + location_name + ">" + total_requested_amt + "</" + location_name + ">");
			for (int j = i + 1; j < paymentRequestData.size(); j++) {
				Map<String, Object> secondRecord = paymentRequestData.get(j);
				String next_requested_date = String.valueOf(secondRecord.get("REQUESTED_DATE"));
				// this if condition is for if the current date and next date is
				// same or not if same add in same object 
				if (requested_date.equals(next_requested_date)) {
					String next_total_requested_amt = String.valueOf(secondRecord.get("TOTAL_REQUESTED_AMT"));
					sumOfAmountDateWise+=Double.valueOf(next_total_requested_amt);
					String next_location_name = String.valueOf(secondRecord.get("ADDRESS"));
					locationNames.add(next_location_name);
					tempXmlreqAMTRpt.append("<" + next_location_name + ">" + next_total_requested_amt + "</"+ next_location_name + ">");
					i++;
				} else {
					break;
				}
			}

			// this for loop is for if the current location and location if
			// added don't add it again and which is not added add it in same
			// object but with value 0.0
			for (Map<String, Object> map : locations) {
				String site_address = String.valueOf(map.get("ADDRESS"));
				if(!locationNames.contains(site_address)){
					tempXmlreqAMTRpt.append("<" + site_address + ">    0</"+ site_address + ">");
					//sumOfAmountDateWise+=0;
				}
			}
			//xmlreqAMTRpt.append("<Total_amount>" + sumOfAmountDateWise + "</Total_amount>");//IMP Code
			tempXmlreqAMTRpt.append("</label>");
			//if(sumOfAmountDateWise!=0||sumOfAmountDateWise!=0.0){
			xmlreqAMTRpt.append(tempXmlreqAMTRpt);
			//}

			days++;
		} // For Loop


		StringBuffer nonExistingDatesInPayment=new StringBuffer();
		for (String requested_date : nextSevenDays) {
			List<String> locationNames = new ArrayList<String>();
			if(!nextSevenDaysFromDB.contains(requested_date)){
				nonExistingDatesInPayment.append("<label>");
				nonExistingDatesInPayment.append("<name>" + requested_date + "</name>");

				for (Map<String, Object> location : locations) {
					String site_address = String.valueOf(location.get("ADDRESS"));
					//	if(!locationNames.contains(site_address)){
					nonExistingDatesInPayment.append("<" + site_address + ">    0</"+ site_address + ">");
					//	}
				}
				nonExistingDatesInPayment.append("</label>");
			}
		}
		xmlreqAMTRpt.append(nonExistingDatesInPayment);


		xmlreqAMTRpt.append("</xml>");
		System.out.println(xmlreqAMTRpt);

		return xmlreqAMTRpt.toString();
	}

	@Override
	public List<Map<String, Object>> loadAllChildProducts(String prodName, String site_id, String type) {
		System.out.println(prodName + "in DAO");
		String counts = UIProperties.validateParams.getProperty("loadChildProductLimits") == null ? "00" : UIProperties.validateParams.getProperty("loadChildProductLimits").toString();
		String childProdQuery = "SELECT CP.NAME,CP.CHILD_PRODUCT_ID  FROM CHILD_PRODUCT CP WHERE  STATUS = 'A' and lower(NAME) "
			+ " like lower('%"+ prodName + "%') and rownum <"+counts+" ORDER BY NAME ASC";

		if(type.equals("DeptWisePriceMaster")){
			if(site_id.equals("996")){
				childProdQuery="select CP.NAME,CP.CHILD_PRODUCT_ID from CHILD_PRODUCT CP,SUB_PRODUCT SP, PRODUCT PROD "
						+ " WHERE PROD.PRODUCT_ID=SP.PRODUCT_ID AND SP.SUB_PRODUCT_ID=CP.SUB_PRODUCT_ID  "
						+ " AND upper(PROD.PRODUCT_DEPT) IN ('MARKETING','ALL') "
						+ " and lower(CP.NAME)  like lower('%"+ prodName + "%')  and CP.STATUS='A' and rownum <"+counts+" ORDER BY CP.NAME ASC";	
			}else{
				childProdQuery="select CP.NAME,CP.CHILD_PRODUCT_ID from CHILD_PRODUCT CP,SUB_PRODUCT SP, PRODUCT PROD "
						+ " WHERE PROD.PRODUCT_ID=SP.PRODUCT_ID AND SP.SUB_PRODUCT_ID=CP.SUB_PRODUCT_ID"
						+ " AND upper(PROD.PRODUCT_DEPT) IN ('STORE','ALL') "
						+ " and lower(CP.NAME)  like lower('%"+ prodName + "%')  and CP.STATUS='A'  and rownum <"+counts+" ORDER BY CP.NAME ASC";
			}
		}else if(type.equals("PriceMaster")){
			childProdQuery="select CP.NAME,CP.CHILD_PRODUCT_ID from CHILD_PRODUCT CP,SUB_PRODUCT SP, PRODUCT PROD "
					+ " WHERE PROD.PRODUCT_ID=SP.PRODUCT_ID AND SP.SUB_PRODUCT_ID=CP.SUB_PRODUCT_ID "
					+ " AND upper(PROD.PRODUCT_DEPT) IN ('STORE','ALL') "
					+ " and lower(CP.NAME)  like lower('%"+ prodName + "%')  and CP.STATUS='A'  and rownum <"+counts+" ORDER BY CP.NAME ASC";
		}
		List<Map<String, Object>> list=jdbcTemplate.queryForList(childProdQuery);
		System.out.println("LIST "+list );
	
/*		List<String> childProdData = jdbcTemplate.query(childProdQuery,	new ResultSetExtractor<List<String>>() {

			@Override
			public List<String> extractData(ResultSet rs)
			throws SQLException, DataAccessException {
				List<String> list = new ArrayList<String>();
				while (rs.next()) {
					list.add(rs.getString(1));
				}
				return list;
			}
		});*/
		
		return list;
	}

	@Override
	public String purhaseTypesAndotalCost(String siteId, String tillDatePaymentReq) {
		System.out.println("ReportsDaoImpl.purhaseTypesAndotalCost()");
		String query ="";
		String site_IDs = UIProperties.validateParams.getProperty("siteIdForPaymentForNextSevenDays") == null ? "00" : UIProperties.validateParams.getProperty("siteIdForPaymentForNextSevenDays").toString();
		query="select sum(TOTAL_AMOUNT) as PURCHASE_AMOUNT,TYPE_OF_PURCHASE,to_char(CREATED_DATE,'MM') from SUMADHURA_PRICE_LIST"
			+ "    where TYPE_OF_PURCHASE in('PO','marketPurchase','localPurchase') and  TYPE_OF_PURCHASE is not null "
			+ "    and created_date between to_char(trunc(add_months(sysdate,-4)) - (to_number(to_char(sysdate,'DD')) -1),'dd-MON-yy') and LAST_DAY(SYSDATE) "
			+ "  and SITE_ID not in ("+site_IDs+")   group by TYPE_OF_PURCHASE, to_char(CREATED_DATE,'MM') order by   to_char(CREATED_DATE,'MM') desc";

		List<Map<String, Object>> list = jdbcTemplate.queryForList(query);
		List<String> months=new ArrayList<String>();
		String[] monthNames={"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
		for (Map<String, Object> map : list) {
			System.out.println(map);
			String month = map.get("TO_CHAR(CREATED_DATE,'MM')") == null ? "" : map.get("TO_CHAR(CREATED_DATE,'MM')").toString().trim();
			/*  if(month.startsWith("0")){
            	  month=monthNames[Integer.valueOf(month.replaceFirst("0", ""))-1];
    		}else{
    			  month=monthNames[Integer.valueOf(month)-1];
    		}*/

			month=monthNames[Integer.valueOf(month)-1];
			if(!months.contains(month))
				months.add(month);
		}

		StringBuffer mainItem = new StringBuffer();
		StringBuffer subItem = new StringBuffer();
		StringBuffer localPurchase = new StringBuffer();
		StringBuffer marketPurchase = new StringBuffer();
		StringBuffer po = new StringBuffer();

		mainItem.append("<xml>");

		int localSum=0;
		int marketSum=0;
		int poSum=0;
		int threeMonthsData=0;
		String currentMonth="";
		String changedMonth="";
		int i=0;
		/* Collections.reverse(months); 
        Collections.reverse(list); 
		 */
		for (int j1 = 0; j1 < months.size()&&threeMonthsData<3; j1++) {
			String month = months.get(j1);

			localSum = 0;
			poSum = 0;
			marketSum = 0;
			localPurchase = new StringBuffer();
			marketPurchase = new StringBuffer();
			po = new StringBuffer();

			subItem.append("<data>");
			currentMonth = "";
			for (int j = i; j < list.size(); j++) {
				Map<String, Object> map = list.get(j);
				String purchase_amount = map.get("PURCHASE_AMOUNT") == null ? "": map.get("PURCHASE_AMOUNT").toString();
				String type_of_purchase = map.get("TYPE_OF_PURCHASE") == null ? "": map.get("TYPE_OF_PURCHASE").toString();
				String date = map.get("TO_CHAR(CREATED_DATE,'MM')") == null ? "": map.get("TO_CHAR(CREATED_DATE,'MM')").toString().trim();
				date = monthNames[Integer.valueOf(date) - 1];
				if (!month.equals(date)) {
					break;
				}
				i++;
				if (currentMonth.length() == 0) {
					subItem.append("<timescale>" + date + "</timescale>");
					currentMonth = date;
				}
				if (month.equals(date)) {
					if (type_of_purchase.equals("localPurchase")) {
						localPurchase.append("<LocalPurchase>" + purchase_amount + "</LocalPurchase>");
						subItem.append(localPurchase);
						localSum++;
					} else if (type_of_purchase.equals("marketPurchase")) {
						marketPurchase.append("    <MarketPurchase>" + purchase_amount + "</MarketPurchase>");
						subItem.append(marketPurchase);
						marketSum++;
					} else if (type_of_purchase.equals("PO")) {
						po.append("<POPurchase>" + purchase_amount + "</POPurchase>");
						subItem.append(po);
						poSum++;
					}
				}
			}//inner for
			if (localSum == 0) {
				localPurchase.append("<LocalPurchase>0.0</LocalPurchase>");
				subItem.append(localPurchase);
			}
			if (marketSum == 0) {
				marketPurchase.append("<MarketPurchase>0.0</MarketPurchase>");
				subItem.append(marketPurchase);
			}
			if (poSum == 0) {
				po.append("<POPurchase>0.0</POPurchase>");
				subItem.append(po);
			}
			threeMonthsData++;
			subItem.append("</data>");
		}//Months for

		System.out.println(marketPurchase);
		System.out.println(localPurchase);
		System.out.println(po);

		mainItem.append(subItem);
		mainItem.append("</xml>");
		//System.out.println("main data of purchase " + mainItem);
		// System.out.println("ReportsDaoImpl.purhaseTypesAndotalCost()");
		return mainItem.toString();
	}

	public String getTotalReceivedAndIssuedQuantity(String siteId,String strDate){

		String strResponse = "";
		String query = "";
		String strInvoiceNo = "";
		String strInvoiceDate = "";
		String strPONumber = "";
		String strPODate = "";
		String strVendorName = "";
		double InvoiceAmount = 0.0;
		double InwrdsGrandTotal = 0.0;
		double IssuesGrandTotal = 0.0;
		String strPrevMaterial = "";
		String strMaterialName = "";
		String strPrevInvoiceNo = "";
		int intSerNo = 0;
		int intInvoiceCount = 1;
		int intDCCount = 0;


		String strPrevInvoiceDate = "";
		String strPrevPONumber = "";
		String strPrevPODate = "";
		String strPrevVendorName = "";
		String strPrevMaterialName  = "";
		String strPrevTypeOfPurchase  = "";
		double strPrevinvoiceAmount = 0.0;
		String strInvoiceResponse ="";
		String strDCResponse ="";
		String strTypeOfPurchase = "";
		String strInvoiceAmtWithCommaPrevOne="";
		String strInvoiceAmtWithComma="";
		String strGrandTotalWithComma="";


		List<Map<String, Object>> list = null;
		//Set setMaterialNames = new HashSet();
		Map<String, Object> mapMaterialNames = new HashMap<String, Object>();
		com.ibm.icu.text.NumberFormat format = com.ibm.icu.text.NumberFormat.getCurrencyInstance(new Locale("en", "in"));
		

		//	siteId = "112";
		//	strDate = "17-10-18";
		String strCurrentKey = "";
		List strCurrentValue = new ArrayList();
		List materialList = new ArrayList();
		try{

			query = "SELECT IE.INVOICE_ID ,IE.INVOICE_DATE,IE.PO_ID,TO_CHAR(IE.PODATE, 'dd-mm-yy') as PODATE,VD.VENDOR_NAME, "+
			" IE.TOTAL_AMOUNT ,IED.CHILD_PRODUCT_NAME,IED.PRODUCT_NAME,( CASE SPL.TYPE_OF_PURCHASE "+
			" WHEN 'localPurchase' THEN 'Local Purchase' "+
			" WHEN 'marketPurchase' THEN 'Market Purchase' "+
			" WHEN 'OtherSite' THEN 'Other Site' "+
			" ELSE SPL.TYPE_OF_PURCHASE"+
			"  END) as TYPE_OF_PURCHASE "+
			"FROM INDENT_ENTRY IE, VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED,INDENT_ENTRY_DETAILS IED ,SUMADHURA_PRICE_LIST SPL WHERE  "+
			"IE.INDENT_ENTRY_ID = IED.INDENT_ENTRY_ID  and  IED.INDENT_ENTRY_DETAILS_ID = SPL.INDENT_ENTRY_DETAILS_ID and  IE.INDENT_TYPE IN ('IN','IND','INO') AND IE.SITE_ID= ? "+
			"AND IE.VENDOR_ID=VD.VENDOR_ID  and IE.USER_ID = SED.EMP_ID "+
			"AND TRUNC(IE.ENTRY_DATE)  = TO_DATE(?,'dd-MM-yy') "+
			"and IE.INDENT_ENTRY_ID not in  "+
			"(select distinct INDENT_ENTRY_ID from DC_ENTRY DE   where DE.SITE_ID= ? and  DE.INDENT_ENTRY_ID is not null)  order by IE.INVOICE_ID";


			list = jdbcTemplate.queryForList(query,new Object[]{siteId, strDate, siteId} );

			strResponse = "<XML><INWARDS>";
			for (Map<String, Object> map : list) {


				strInvoiceNo = String.valueOf(map.get("INVOICE_ID") == null ? "-" : map.get("INVOICE_ID"));
				strInvoiceDate = String.valueOf(map.get("INVOICE_DATE") == null ? "-" : map.get("INVOICE_DATE"));
				strPONumber = String.valueOf(map.get("PO_ID") == null ? "-" : map.get("PO_ID"));
				strPODate = String.valueOf(map.get("PODATE") == null ? "-" : map.get("PODATE"));
				strVendorName = String.valueOf(map.get("VENDOR_NAME") == null ? "-" : map.get("VENDOR_NAME"));
				strMaterialName  = String.valueOf(map.get("PRODUCT_NAME") == null ? "-" : map.get("PRODUCT_NAME"));
				InvoiceAmount = Double.valueOf(String.valueOf(map.get("TOTAL_AMOUNT") == null ? "-" : map.get("TOTAL_AMOUNT")));
				strTypeOfPurchase = String.valueOf(map.get("TYPE_OF_PURCHASE") == null ? "-" : map.get("TYPE_OF_PURCHASE"));
				/*if(strPrevMaterial.startsWith(",")){
					strPrevMaterial = strPrevMaterial.substring(1,strPrevMaterial.length());
				}*/



				if(mapMaterialNames.containsKey(strInvoiceNo)){

					for (Entry<String, Object> entry : mapMaterialNames.entrySet())  {

						//entry.getValue();
						strCurrentKey = entry.getKey();
						strCurrentValue = (List) entry.getValue(); 
						if(strCurrentKey.equals(strInvoiceNo) && strCurrentValue.contains(strMaterialName)){
							strMaterialName = "";
						}


					} 

					if(!strMaterialName.equals("")){
						materialList.add(strMaterialName);
						mapMaterialNames.put(strInvoiceNo, materialList);
					}
				}else{

					materialList.clear();
					materialList.add(strMaterialName);
					mapMaterialNames.put(strInvoiceNo, materialList);
				}

				/*if(!setMaterialNames.contains(strMaterialName) ){ // if it  new material name need to go inside loop. If it is same material name should not add in the list

					setMaterialNames.add(strMaterialName);

				}else{
					strMaterialName = "";
				}*/



				if(strPrevInvoiceNo.equals(strInvoiceNo)){
					strPrevMaterial = strPrevMaterial+","+strMaterialName;
				}

				else{

					if(intSerNo > 0){

						InwrdsGrandTotal = InwrdsGrandTotal+strPrevinvoiceAmount;
						InwrdsGrandTotal = Double.parseDouble(new DecimalFormat("##.##").format(InwrdsGrandTotal));
						strPrevinvoiceAmount = Double.parseDouble(new DecimalFormat("##.##").format(strPrevinvoiceAmount));
						strInvoiceAmtWithCommaPrevOne=format.format(strPrevinvoiceAmount).replaceAll("Rs","");
						strGrandTotalWithComma=format.format(InwrdsGrandTotal).replaceAll("Rs","");
						
						//if it is more then ,, symbols. like  Adiminstrative,,assets,,,,concrete
						strPrevMaterial = strPrevMaterial.replace(",,",",");
						strPrevMaterial = strPrevMaterial.replace(",,",",");
						strPrevMaterial = strPrevMaterial.replace(",,",",");
						
						if(strPrevMaterial.startsWith(",")){
							strPrevMaterial = strPrevMaterial.replace(",,", ","); //if material name empty we will get like ,,
							strPrevMaterial = strPrevMaterial.substring(1,strPrevMaterial.length());
						}
						/*if(strPrevMaterial.endsWith(",,")){
							strPrevMaterial = strPrevMaterial.substring(0,strPrevMaterial.length()-2);
						}*/
						if(strPrevMaterial.endsWith(",")){
							strPrevMaterial = strPrevMaterial.substring(0,strPrevMaterial.length()-1);
						}
						strInvoiceResponse += "<INWARD>";
						strInvoiceResponse += "<SERIALNO><![CDATA["+intInvoiceCount+"]]></SERIALNO>";
						strInvoiceResponse += "<INVOICENO_DCNO><![CDATA["+strPrevInvoiceNo+"]]></INVOICENO_DCNO>";
						strInvoiceResponse += "<INVOICEDATE><![CDATA["+strPrevInvoiceDate+"]]></INVOICEDATE>";
						strInvoiceResponse += "<PONUMBER><![CDATA["+strPrevPONumber+"]]></PONUMBER>";
						strInvoiceResponse += "<PODATE><![CDATA["+strPrevPODate+"]]></PODATE>";
						strInvoiceResponse += "<VENDORNAME><![CDATA["+strPrevVendorName+"]]></VENDORNAME>";
						strInvoiceResponse += "<MATERIALNAME><![CDATA["+strPrevMaterial+"]]></MATERIALNAME>";
						strInvoiceResponse += "<INVOICEAMOUNT><![CDATA["+strInvoiceAmtWithCommaPrevOne+"]]></INVOICEAMOUNT>";
						strInvoiceResponse += "<TYPEOFPURCHASE><![CDATA["+strPrevTypeOfPurchase+"]]></TYPEOFPURCHASE>";
						strInvoiceResponse += "</INWARD>";
						intInvoiceCount = intInvoiceCount+1;
						strPrevMaterial = "";
						//setMaterialNames.clear(); //clearing privoius material and adding current material
						//setMaterialNames.add(strMaterialName);

					}
					strPrevInvoiceNo = strInvoiceNo;
					strPrevInvoiceDate = strInvoiceDate;
					strPrevPONumber =strPONumber;
					strPrevPODate = strPODate;
					strPrevVendorName = strVendorName;
					strPrevMaterialName  = strMaterialName;
					strPrevinvoiceAmount = InvoiceAmount;
					strPrevTypeOfPurchase = strTypeOfPurchase;
					//setMaterialNames.add(strMaterialName);
					//strPrevMaterial = "";
					strPrevMaterial = strPrevMaterial+","+strPrevMaterialName;
				}




				strPrevInvoiceNo = strInvoiceNo;
				intSerNo = intSerNo+1;

			}

			// first time it will not appending the response..ex liste size =1
			//if(strInvoiceResponse.equals("") && list.size() > 0){
			if( list.size() > 0){

				InwrdsGrandTotal = InwrdsGrandTotal+InvoiceAmount;
				InwrdsGrandTotal = Double.parseDouble(new DecimalFormat("##.##").format(InwrdsGrandTotal));
				InvoiceAmount = Double.parseDouble(new DecimalFormat("##.##").format(InvoiceAmount));
				strInvoiceAmtWithComma=format.format(InvoiceAmount).replaceAll("Rs","");
				strGrandTotalWithComma=format.format(InwrdsGrandTotal).replaceAll("Rs","");
				strPrevMaterial = strPrevMaterial.replace(",,",",");
				strPrevMaterial = strPrevMaterial.replace(",,",",");
				strPrevMaterial = strPrevMaterial.replace(",,",",");
				
				if(strPrevMaterial.startsWith(",")){
					strPrevMaterial = strPrevMaterial.substring(1,strPrevMaterial.length());
				}
				/*if(strPrevMaterial.endsWith(",,")){
					strPrevMaterial = strPrevMaterial.substring(0,strPrevMaterial.length()-2);
				}*/
				if(strPrevMaterial.endsWith(",")){
					strPrevMaterial = strPrevMaterial.substring(0,strPrevMaterial.length()-1);
				}
				strInvoiceResponse += "<INWARD>";
				strInvoiceResponse += "<SERIALNO><![CDATA["+intInvoiceCount+"]]></SERIALNO>";
				strInvoiceResponse += "<INVOICENO_DCNO><![CDATA["+strInvoiceNo+"]]></INVOICENO_DCNO>";
				strInvoiceResponse += "<INVOICEDATE><![CDATA["+strInvoiceDate+"]]></INVOICEDATE>";
				strInvoiceResponse += "<PONUMBER><![CDATA["+strPONumber+"]]></PONUMBER>";
				strInvoiceResponse += "<PODATE><![CDATA["+strPODate+"]]></PODATE>";
				strInvoiceResponse += "<VENDORNAME><![CDATA["+strVendorName+"]]></VENDORNAME>";
				strInvoiceResponse += "<MATERIALNAME><![CDATA["+strPrevMaterial+"]]></MATERIALNAME>";
				strInvoiceResponse += "<INVOICEAMOUNT><![CDATA["+strInvoiceAmtWithComma+"]]></INVOICEAMOUNT>";
				strInvoiceResponse += "<TYPEOFPURCHASE><![CDATA["+strTypeOfPurchase+"]]></TYPEOFPURCHASE>";
				strInvoiceResponse += "</INWARD>";
				strPrevMaterial = "";
				//setMaterialNames.clear();
				intInvoiceCount = intInvoiceCount+1;
			}




			// DC Details
			strPrevInvoiceNo = "";
			strPrevInvoiceDate = "";
			strPrevPONumber = "";
			strPrevPODate = "";
			strPrevVendorName = "";
			strPrevMaterialName  = "";
			strPrevinvoiceAmount = 0.0;

			query = " SELECT VD.VENDOR_NAME,SED.EMP_NAME, DF.CHILD_PRODUCT_NAME, DF.PRODUCT_NAME, "+
			"DE.INVOICE_ID,DE.RECEIVED_DATE as RECEIVED_OR_ISSUED_DATE, "+
			"DE.DC_NUMBER,DE.DC_DATE,DE.TOTAL_AMOUNT,DE.PO_ID,TO_CHAR(DE.PODATE, 'dd-mm-yy') as PODATE,( CASE SPL.TYPE_OF_PURCHASE "+
			" WHEN 'localPurchase' THEN 'Local Purchase' "+
			" WHEN 'marketPurchase' THEN 'Market Purchase' "+
			" WHEN 'OtherSite' THEN 'Other Site' "+
			" ELSE SPL.TYPE_OF_PURCHASE"+
			"  END) as TYPE_OF_PURCHASE "+
			" FROM DC_ENTRY DE,DC_FORM DF, VENDOR_DETAILS VD,SUMADHURA_EMPLOYEE_DETAILS SED  ,SUMADHURA_PRICE_LIST SPL "+
			" WHERE DE.DC_ENTRY_ID = DF.DC_ENTRY_ID  and  DF.DC_FORM_ID = SPL.DC_FORM_ENTRY_ID  AND DE.SITE_ID= ?  AND DE.VENDOR_ID=VD.VENDOR_ID "+
			"and DE.USER_ID = SED.EMP_ID   "+
			" AND TRUNC(DE.ENTRY_DATE)  = TO_DATE(?,'dd-MM-yy') order by DE.DC_NUMBER ";


			list = jdbcTemplate.queryForList(query,new Object[]{siteId, strDate} );
			for (Map<String, Object> map : list) {


				strInvoiceNo = "DC_"+(String.valueOf(map.get("DC_NUMBER") == null ? "-" : map.get("DC_NUMBER")));
				strInvoiceDate = String.valueOf(map.get("DC_DATE") == null ? "-" : map.get("DC_DATE"));
				strPONumber = String.valueOf(map.get("PO_ID") == null ? "-" : map.get("PO_ID"));
				strPODate = String.valueOf(map.get("PODATE") == null ? "-" : map.get("PODATE"));
				strVendorName = String.valueOf(map.get("VENDOR_NAME") == null ? "-" : map.get("VENDOR_NAME"));
				strMaterialName  = String.valueOf(map.get("PRODUCT_NAME") == null ? "-" : map.get("PRODUCT_NAME"));
				InvoiceAmount = Double.valueOf(String.valueOf(map.get("TOTAL_AMOUNT") == null ? "-" : map.get("TOTAL_AMOUNT")));
				strTypeOfPurchase = String.valueOf(map.get("TYPE_OF_PURCHASE") == null ? "-" : map.get("TYPE_OF_PURCHASE"));




				/*if(!setMaterialNames.contains(strMaterialName) ){ // if it  new material name need to go inside loop. If it is same material name should not add in the list

					setMaterialNames.add(strMaterialName);

				}else{
					strMaterialName = "";
				}*/

				if(mapMaterialNames.containsKey(strInvoiceNo)){

					for (Entry<String, Object> entry : mapMaterialNames.entrySet())  {

						//entry.getValue();
						strCurrentKey = entry.getKey();
						strCurrentValue = (List) entry.getValue(); 
						if(strCurrentKey.equals(strInvoiceNo) && strCurrentValue.contains(strMaterialName)){
							strMaterialName = "";
						}


					} 

					if(!strMaterialName.equals("")){
						materialList.add(strMaterialName);
						mapMaterialNames.put(strInvoiceNo, materialList);
					}
				}else{

					materialList.clear();
					materialList.add(strMaterialName);
					mapMaterialNames.put(strInvoiceNo, materialList);
				}

				if(strPrevInvoiceNo.equals(strInvoiceNo)){
					strPrevMaterial = strPrevMaterial+","+strMaterialName;
				}

				else{

					if(intDCCount > 0){

						InwrdsGrandTotal = InwrdsGrandTotal+strPrevinvoiceAmount;
						InwrdsGrandTotal = Double.parseDouble(new DecimalFormat("##.##").format(InwrdsGrandTotal));
						strPrevinvoiceAmount = Double.parseDouble(new DecimalFormat("##.##").format(strPrevinvoiceAmount));
						strInvoiceAmtWithCommaPrevOne=format.format(strPrevinvoiceAmount).replaceAll("Rs","");
						strGrandTotalWithComma=format.format(InwrdsGrandTotal).replaceAll("Rs","");
					
						strPrevMaterial = strPrevMaterial.replace(",,",",");
						strPrevMaterial = strPrevMaterial.replace(",,",",");
						strPrevMaterial = strPrevMaterial.replace(",,",",");
						if(strPrevMaterial.startsWith(",")){
							strPrevMaterial = strPrevMaterial.replace(",,", ","); //if material name empty we will get like ,,
							strPrevMaterial = strPrevMaterial.substring(1,strPrevMaterial.length());
						}
						/*if(strPrevMaterial.endsWith(",,")){
							strPrevMaterial = strPrevMaterial.substring(0,strPrevMaterial.length()-2);
						}*/
						if(strPrevMaterial.endsWith(",")){
							strPrevMaterial = strPrevMaterial.substring(0,strPrevMaterial.length()-1);
						}
						strDCResponse += "<INWARD>";
						strDCResponse += "<SERIALNO><![CDATA["+intInvoiceCount+"]]></SERIALNO>";
						strDCResponse += "<INVOICENO_DCNO><![CDATA["+strPrevInvoiceNo+"]]></INVOICENO_DCNO>";
						strDCResponse += "<INVOICEDATE><![CDATA["+strPrevInvoiceDate+"]]></INVOICEDATE>";
						strDCResponse += "<PONUMBER><![CDATA["+strPrevPONumber+"]]></PONUMBER>";
						strDCResponse += "<PODATE><![CDATA["+strPrevPODate+"]]></PODATE>";
						strDCResponse += "<VENDORNAME><![CDATA["+strPrevVendorName+"]]></VENDORNAME>";
						strDCResponse += "<MATERIALNAME><![CDATA["+strPrevMaterial+"]]></MATERIALNAME>";
						strDCResponse += "<INVOICEAMOUNT><![CDATA["+strInvoiceAmtWithCommaPrevOne+"]]></INVOICEAMOUNT>";
						strDCResponse += "<TYPEOFPURCHASE><![CDATA["+strPrevTypeOfPurchase+"]]></TYPEOFPURCHASE>";
						strDCResponse += "</INWARD>";
						intInvoiceCount = intInvoiceCount+1;
						strPrevMaterial = "";
						//setMaterialNames.clear();
						//setMaterialNames.add(strMaterialName);


					}

					strPrevInvoiceNo = strInvoiceNo;
					strPrevInvoiceDate = strInvoiceDate;
					strPrevPONumber =strPONumber;
					strPrevPODate = strPODate;
					strPrevVendorName = strVendorName;
					strPrevMaterialName  = strMaterialName;
					strPrevinvoiceAmount = InvoiceAmount;
					strPrevTypeOfPurchase = strTypeOfPurchase;
					//strPrevMaterial = "";
					strPrevMaterial = strPrevMaterial+","+strMaterialName;
				}




				strPrevInvoiceNo = strInvoiceNo;
				intDCCount = intDCCount+1;
				//intSerNo = intSerNo+1;

			}

			//if(strInvoiceResponse.equals("") && list.size() > 0){
			if( list.size() > 0){

				
				strPrevMaterial = strPrevMaterial.replace(",,",",");
				strPrevMaterial = strPrevMaterial.replace(",,",",");
				strPrevMaterial = strPrevMaterial.replace(",,",",");
				
				if(strPrevMaterial.startsWith(",")){
					strPrevMaterial = strPrevMaterial.substring(1,strPrevMaterial.length());
				}
				
				/*if(strPrevMaterial.endsWith(",,")){
					strPrevMaterial = strPrevMaterial.substring(0,strPrevMaterial.length()-2);
				}*/
				if(strPrevMaterial.endsWith(",")){
					strPrevMaterial = strPrevMaterial.substring(0,strPrevMaterial.length()-1);
				}

				InwrdsGrandTotal = InwrdsGrandTotal+InvoiceAmount;
				InwrdsGrandTotal = Double.parseDouble(new DecimalFormat("##.##").format(InwrdsGrandTotal));
				InvoiceAmount = Double.parseDouble(new DecimalFormat("##.##").format(InvoiceAmount));
				strInvoiceAmtWithComma=format.format(InvoiceAmount).replaceAll("Rs","");
				strGrandTotalWithComma=format.format(InwrdsGrandTotal).replaceAll("Rs","");
				
				strDCResponse += "<INWARD>";
				strDCResponse += "<SERIALNO><![CDATA["+intInvoiceCount+"]]></SERIALNO>";
				strDCResponse += "<INVOICENO_DCNO><![CDATA["+strInvoiceNo+"]]></INVOICENO_DCNO>";
				strDCResponse += "<INVOICEDATE><![CDATA["+strInvoiceDate+"]]></INVOICEDATE>";
				strDCResponse += "<PONUMBER><![CDATA["+strPONumber+"]]></PONUMBER>";
				strDCResponse += "<PODATE><![CDATA["+strPODate+"]]></PODATE>";
				strDCResponse += "<VENDORNAME><![CDATA["+strVendorName+"]]></VENDORNAME>";
				strDCResponse += "<MATERIALNAME><![CDATA["+strPrevMaterial+"]]></MATERIALNAME>";
				strDCResponse += "<INVOICEAMOUNT><![CDATA["+strInvoiceAmtWithComma+"]]></INVOICEAMOUNT>";
				strDCResponse += "<TYPEOFPURCHASE><![CDATA["+strTypeOfPurchase+"]]></TYPEOFPURCHASE>";
				strDCResponse += "</INWARD>";
				strPrevMaterial = "";
				//setMaterialNames.clear();
			}

			strResponse = strResponse+strInvoiceResponse+strDCResponse;
			//InwrdsGrandTotal
			strResponse += "<INWARDSGRANDTOTAL>"+strGrandTotalWithComma+"</INWARDSGRANDTOTAL></INWARDS>";
		}catch(Exception e){
			e.printStackTrace();
		}


		//=================================================================================================================


		String strTypeOfIssue = "";
		String strSiteName = "";
		String strTotalNoOfIssues = "";
		double issueAmount = 0.0;
		String strIssueResponse = "";
		String strIssueAmtWithComma="";
		String strIssueGrandAmtWithComma="";
		
		//internal issues
		query = "select "+
		"count(1) as NO_OF_ISSUES,SUM(replace(TOTAL_AMOUNT,',','')) as TOTAL_AMOUNT  from INDENT_ENTRY where "+
		"INDENT_TYPE in('OUT') and  SITE_ID = ?   and TRUNC(ENTRY_DATE) = TO_DATE(?, 'dd-MM-yy')    "+
		"group by INDENT_TYPE ";


		list = jdbcTemplate.queryForList(query,new Object[]{siteId, strDate} );

		strIssueResponse += "<ISSUES>";
		for (Map<String, Object> map : list) {


			strTypeOfIssue = "Internal";
			//strSiteName = String.valueOf(map.get("SITE_NAME") == null ? "-" : map.get("SITE_NAME"));
			strTotalNoOfIssues = String.valueOf(map.get("NO_OF_ISSUES") == null ? "-" : map.get("NO_OF_ISSUES"));
			issueAmount = Double.valueOf(map.get("TOTAL_AMOUNT") == null ? "0" : map.get("TOTAL_AMOUNT").toString());
			issueAmount = Double.parseDouble(new DecimalFormat("##.##").format(issueAmount));
			strIssueAmtWithComma=format.format(issueAmount).replaceAll("Rs","");
			
			IssuesGrandTotal = IssuesGrandTotal+ issueAmount;
			strIssueResponse += "<ISSUE>";
			strIssueResponse += "<TYPEOFISSUE>"+strTypeOfIssue+"</TYPEOFISSUE>";
			strIssueResponse += "<ISSUEDTO>"+strSiteName+"</ISSUEDTO>";
			strIssueResponse += "<TOTALNOFSLIPS>"+strTotalNoOfIssues+"</TOTALNOFSLIPS>";
			strIssueResponse += "<ISSUEAMOUNT>"+strIssueAmtWithComma+"</ISSUEAMOUNT>";
			strIssueResponse += "</ISSUE>";

		}


		//other site
		query = "select REQUESTER_NAME  "+
		",count(1) as NO_OF_ISSUES,SUM(replace(TOTAL_AMOUNT,',','')) as TOTAL_AMOUNT  from INDENT_ENTRY where "+
		"INDENT_TYPE in( 'OUTO') and  SITE_ID = ?   and TRUNC(ENTRY_DATE) = TO_DATE(?, 'dd-MM-yy')   "+
		" group by INDENT_TYPE,REQUESTER_NAME ";

		list = jdbcTemplate.queryForList(query,new Object[]{siteId, strDate} );

		//strIssueResponse += "<ISSUES>";
		for (Map<String, Object> map : list) {


			strTypeOfIssue = "Other site";
			strSiteName = String.valueOf(map.get("REQUESTER_NAME") == null ? "-" : map.get("REQUESTER_NAME"));
			strTotalNoOfIssues = String.valueOf(map.get("NO_OF_ISSUES") == null ? "-" : map.get("NO_OF_ISSUES"));
			issueAmount = Double.valueOf(map.get("TOTAL_AMOUNT") == null ? "0" : map.get("TOTAL_AMOUNT").toString());
			issueAmount = Double.parseDouble(new DecimalFormat("##.##").format(issueAmount));
			strIssueAmtWithComma=format.format(issueAmount).replaceAll("Rs","");
			
			IssuesGrandTotal = IssuesGrandTotal+ issueAmount;

			strIssueResponse += "<ISSUE>";
			strIssueResponse += "<TYPEOFISSUE>"+strTypeOfIssue+"</TYPEOFISSUE>";
			strIssueResponse += "<ISSUEDTO>"+strSiteName+"</ISSUEDTO>";
			strIssueResponse += "<TOTALNOFSLIPS>"+strTotalNoOfIssues+"</TOTALNOFSLIPS>";
			strIssueResponse += "<ISSUEAMOUNT>"+strIssueAmtWithComma+"</ISSUEAMOUNT>";
			strIssueResponse += "</ISSUE>";

		}
		IssuesGrandTotal = Double.parseDouble(new DecimalFormat("##.##").format(IssuesGrandTotal));
		strIssueGrandAmtWithComma=format.format(IssuesGrandTotal).replaceAll("Rs","");
		strIssueResponse += "<ISSUEDTOTALAMOUNT>"+strIssueGrandAmtWithComma+"</ISSUEDTOTALAMOUNT></ISSUES></XML>";


		return strResponse+strIssueResponse;

	}

}
