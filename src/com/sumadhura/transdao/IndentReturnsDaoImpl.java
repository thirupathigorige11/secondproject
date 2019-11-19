package com.sumadhura.transdao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.IndentReturnBean;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.UIProperties;


@Repository("indentReturnDaoClass")
public class IndentReturnsDaoImpl extends UIProperties implements IndentReturnsDao{

	@Autowired(required = true)
	private JdbcTemplate jdbcTemplate;



	public List<IndentReturnBean> getIssueList(String RequestId, String EmployeName,String ContractorName, String siteId, String fromDate, String toDate) {

		List<Map<String, Object>> productList = null;
		List<IndentReturnBean> listIndentReturnBean = new ArrayList<IndentReturnBean>();
		IndentReturnBean objIndentReturnBean = null;
		JdbcTemplate template = null;
		StringBuffer sql = new StringBuffer();
		String query="";
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			
			if (StringUtils.isNotBlank(fromDate) && StringUtils.isNotBlank(toDate)) {
				query=" AND TRUNC(IE.ENTRY_DATE)   BETWEEN TO_DATE('" + fromDate + "','dd-MM-yy') AND TO_DATE('"+ toDate + "','dd-MM-yy') ";
			} else if (StringUtils.isNotBlank(fromDate)) {
				query="  AND TRUNC(IE.ENTRY_DATE)  =TO_DATE('" + fromDate + "', 'dd-MM-yy')  ";
			} else if (StringUtils.isNotBlank(toDate)) {
				query="  AND TRUNC(IE.ENTRY_DATE)  <=TO_DATE('" + toDate + "', 'dd-MM-yy')  ";
			}
			
			
			if (StringUtils.isNotBlank(RequestId) && StringUtils.isNotBlank(EmployeName)&& StringUtils.isNotBlank(ContractorName)) {
				sql.append("SELECT IE.ENTRY_DATE AS ENTRY_DATE,  IE.SLIP_NUMBER, IE.REQUESTER_NAME, CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME , IED.PRODUCT_NAME,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME ,IED.INDENT_ENTRY_DETAILS_ID,IED.ISSUED_QTY,IED.MEASUR_MNT_NAME, IED.HSN_CODE FROM"
						+ "IE, INDENT_ENTRY_DETAILS IED,SUMADHURA_CONTRACTOR SC WHERE IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND SC.CONTRACTOR_ID=IE.CONTRACTOR_ID     AND IE.INDENT_ENTRY_ID=? AND  IE.REQUESTER_ID=? AND  IE.REQUESTER_NAME=? AND  upper(IE.CONTRACTOR_ID)=upper(?) AND IE.SITE_ID=? AND IE.INDENT_TYPE in ('OUT','OUTR')");
				sql.append(query+"  AND IED.ISSUED_QTY!=0    Order by IE.ENTRY_DATE ");
				productList = template.queryForList(sql.toString(), new Object[] {RequestId, EmployeName,ContractorName, siteId});
			}else if (StringUtils.isNotBlank(RequestId)) {
				//ACP Changed Request Id to Indent Entry Id
				sql.append("SELECT IE.ENTRY_DATE AS ENTRY_DATE,IE.SLIP_NUMBER, IE.REQUESTER_NAME, CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME , IED.PRODUCT_NAME,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME ,IED.INDENT_ENTRY_DETAILS_ID,IED.ISSUED_QTY,IED.MEASUR_MNT_NAME, IED.HSN_CODE FROM INDENT_ENTRY IE  left outer join SUMADHURA_CONTRACTOR SC on SC.CONTRACTOR_ID=IE.CONTRACTOR_ID, INDENT_ENTRY_DETAILS IED  WHERE IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND IE.INDENT_ENTRY_ID=? AND IE.SITE_ID=? AND IE.INDENT_TYPE  in ('OUT','OUTR')");
				sql.append(query+"  AND IED.ISSUED_QTY!=0     Order by IE.ENTRY_DATE ");
				productList = template.queryForList(sql.toString(), new Object[] { RequestId, siteId});
				if(productList.size()==0){
				/*	sql = new StringBuffer();
					sql.append("SELECT IE.ENTRY_DATE AS ENTRY_DATE,IE.SLIP_NUMBER, IE.REQUESTER_NAME , IED.PRODUCT_NAME,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME ,IED.INDENT_ENTRY_DETAILS_ID,IED.ISSUED_QTY,IED.MEASUR_MNT_NAME, IED.HSN_CODE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED  WHERE IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND IE.INDENT_ENTRY_ID=? AND IE.SITE_ID=? AND IE.INDENT_TYPE  in ('OUT','OUTR')");
					sql.append(query+"  AND IED.ISSUED_QTY!=0     Order by IE.ENTRY_DATE ");
					productList = template.queryForList(sql.toString(), new Object[] { RequestId, siteId});	*/
					/*if(productList.size()==0){
						sql = new StringBuffer().append("SELECT IE.ENTRY_DATE AS ENTRY_DATE,IE.SLIP_NUMBER, IE.REQUESTER_NAME , IED.PRODUCT_NAME,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME ,IED.INDENT_ENTRY_DETAILS_ID,IED.ISSUED_QTY,IED.MEASUR_MNT_NAME, IED.HSN_CODE ")
						   .append( "FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED WHERE IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND IE.REQUESTER_ID=? and SITE_ID=?    AND IE.INDENT_TYPE  in ('OUT','OUTR')");
						sql.append(query+"  AND IED.ISSUED_QTY!=0     Order by IE.ENTRY_DATE ");
						productList = template.queryForList(sql.toString(), new Object[] { RequestId,siteId});
					}*/
				}
				//sql = "SELECT IE.ENTRY_DATE AS ENTRY_DATE,IE.SLIP_NUMBER, IE.REQUESTER_NAME, CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME , IED.PRODUCT_NAME,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME ,IED.INDENT_ENTRY_DETAILS_ID,IED.ISSUED_QTY,IED.MEASUR_MNT_NAME, IED.HSN_CODE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED ,SUMADHURA_CONTRACTOR SC WHERE IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND SC.CONTRACTOR_ID=IE.CONTRACTOR_ID AND IE.REQUESTER_ID=? AND IE.SITE_ID=?";			
			}
			else if(StringUtils.isNotBlank(EmployeName)){
				sql.append("SELECT IE.ENTRY_DATE AS ENTRY_DATE,  IE.SLIP_NUMBER, IE.REQUESTER_NAME, CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME , IED.PRODUCT_NAME,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME ,IED.INDENT_ENTRY_DETAILS_ID,IED.ISSUED_QTY,IED.MEASUR_MNT_NAME, IED.HSN_CODE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED  ,SUMADHURA_CONTRACTOR SC WHERE IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND SC.CONTRACTOR_ID=IE.CONTRACTOR_ID AND  IE.REQUESTER_NAME=? AND IE.SITE_ID=? AND IE.INDENT_TYPE in ('OUT','OUTR')");
				sql.append(query+"  AND IED.ISSUED_QTY!=0    Order by IE.ENTRY_DATE ");
				productList = template.queryForList(sql.toString(), new Object[] { EmployeName.toUpperCase(), siteId});
				if(productList.size()==0){
					sql = new StringBuffer();
					sql .append("SELECT IE.ENTRY_DATE AS ENTRY_DATE,  IE.SLIP_NUMBER, IE.REQUESTER_NAME , IED.PRODUCT_NAME,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME ,IED.INDENT_ENTRY_DETAILS_ID,IED.ISSUED_QTY,IED.MEASUR_MNT_NAME, IED.HSN_CODE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED  WHERE IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND  IE.REQUESTER_NAME=? and SITE_ID=? AND IE.INDENT_TYPE in ('OUT','OUTR')");
					sql.append(query+"  AND IED.ISSUED_QTY!=0    Order by IE.ENTRY_DATE ");
					productList = template.queryForList(sql.toString(), new Object[] { EmployeName.toUpperCase(),siteId});
				}			
			} else {
					sql .append( "SELECT IE.ENTRY_DATE AS ENTRY_DATE,  IE.SLIP_NUMBER, IE.REQUESTER_NAME, CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,IED.PRODUCT_NAME,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME ,IED.INDENT_ENTRY_DETAILS_ID,IED.ISSUED_QTY,IED.MEASUR_MNT_NAME, IED.HSN_CODE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED ,SUMADHURA_CONTRACTOR SC WHERE IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND SC.CONTRACTOR_ID=IE.CONTRACTOR_ID   AND  upper(IE.CONTRACTOR_ID)=upper(?) AND IE.SITE_ID=? AND IE.INDENT_TYPE in ('OUT','OUTR')");
					/*	String id=jdbcTemplate.queryForObject("select contractor_id from SUMADHURA_CONTRACTOR where contractor_name =?", String.class);
							System.out.println("id "+id);
							sql = "SELECT IE.ENTRY_DATE AS ENTRY_DATE,  IE.SLIP_NUMBER, IE.REQUESTER_NAME, CONCAT(SC.FIRST_NAME,' '||SC.LAST_NAME)  as CONTRACTOR_NAME ,IED.PRODUCT_NAME,IED.SUB_PRODUCT_NAME,IED.CHILD_PRODUCT_NAME ,IED.INDENT_ENTRY_DETAILS_ID,IED.ISSUED_QTY,IED.MEASUR_MNT_NAME, IED.HSN_CODE FROM INDENT_ENTRY IE, INDENT_ENTRY_DETAILS IED ,SUMADHURA_CONTRACTOR SC WHERE IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND SC.CONTRACTOR_ID=IE.CONTRACTOR_ID   AND  IE.CONTRACTOR_ID =? AND IE.SITE_ID=?";
					 */	System.out.println(ContractorName);
					 sql.append(query+"  AND IED.ISSUED_QTY!=0    Order by IE.ENTRY_DATE ");
					 productList = template.queryForList(sql.toString(), new Object[] {ContractorName, siteId});
		}
			
			if (null != productList && productList.size() > 0) {
				for (Map IndentIssueBean : productList) {
					objIndentReturnBean = new IndentReturnBean();
					objIndentReturnBean.setStrIndententrtDetailsId(Integer.parseInt(IndentIssueBean.get("INDENT_ENTRY_DETAILS_ID") == null ? "": IndentIssueBean.get("INDENT_ENTRY_DETAILS_ID").toString()));
					objIndentReturnBean.setStrProductName(IndentIssueBean.get("PRODUCT_NAME") == null ? "" : IndentIssueBean.get("PRODUCT_NAME").toString());
					objIndentReturnBean.setStrSubProductName(IndentIssueBean.get("SUB_PRODUCT_NAME") == null ? "" : IndentIssueBean.get("SUB_PRODUCT_NAME").toString());
					objIndentReturnBean.setStrChildProductName(IndentIssueBean.get("CHILD_PRODUCT_NAME") == null ? "" : IndentIssueBean.get("CHILD_PRODUCT_NAME").toString());
					objIndentReturnBean.setStrQuantity(IndentIssueBean.get("ISSUED_QTY") == null ? "" : IndentIssueBean.get("ISSUED_QTY").toString());
					objIndentReturnBean.setStrMesurmentName(IndentIssueBean.get("MEASUR_MNT_NAME") == null ? "" : IndentIssueBean.get("MEASUR_MNT_NAME").toString());
					objIndentReturnBean.setStrQuantity(IndentIssueBean.get("ISSUED_QTY") == null ? "" : IndentIssueBean.get("ISSUED_QTY").toString());
					objIndentReturnBean.setIssueSlipNumber(IndentIssueBean.get("SLIP_NUMBER") == null ? "" : IndentIssueBean.get("SLIP_NUMBER").toString());
					objIndentReturnBean.setContractorName(IndentIssueBean.get("CONTRACTOR_NAME") == null ? "" : IndentIssueBean.get("CONTRACTOR_NAME").toString());
					objIndentReturnBean.setRequesterName(IndentIssueBean.get("REQUESTER_NAME") == null ? "" : IndentIssueBean.get("REQUESTER_NAME").toString());
					objIndentReturnBean.setBlockName(IndentIssueBean.get("BLOCK_NAME") == null ? "" : IndentIssueBean.get("BLOCK_NAME").toString());
					String date = IndentIssueBean.get("ENTRY_DATE") == null ? "" : IndentIssueBean.get("ENTRY_DATE").toString();
					if (StringUtils.isNotBlank(date)) {
						date = DateUtil.dateConversion(date);
					} else {
						date = "";
					}
					objIndentReturnBean.setDate(date);
					listIndentReturnBean.add(objIndentReturnBean);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return listIndentReturnBean;
	}


	public String updateReturns(String RequestId, String strQuantity,String strSiteId){


		String strSql = "";
		double doublePrice = 0.0; 
		double doubleAmount = 0.0;
		String strPrice = ""; 
		String strAmount ="";
		String strToalAmount = "";
		String strProductId = "";
		String strSubProductId = "";
		String strChildProductId = "";
		String strMesurmentId = "";
		String strIssuedQantity = "";
		String priceId = "";
		Double quantity = 0.0;
		Double totalAmt = 0.0;
		Double totalAmtForOneQty = 0.0;
		List<Map<String, Object>> productList = null;
		String strPrevRemarks = "";
		String availability_Id="";
		double return_Form_Quantity=0.0;
		double return_Quan=0.0;
double amountForIndentEntryTable = 0.0;
		//strSql = "select PRICE,TOTAL_AMOUNT,ISSUED_QTY,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MEASUR_MNT_ID, PRICE_ID from INDENT_ENTRY_DETAILS where INDENT_ENTRY_DETAILS_ID = ?";
		
	List<Map<String, Object>> dbProductDetailsList = null;
		strSql = "select SP.AMOUNT_PER_UNIT_AFTER_TAXES,SP.AMOUNT_PER_UNIT_BEFORE_TAXES,IED.ISSUED_QTY,IED.PRODUCT_ID,IED.SUB_PRODUCT_ID,IED.CHILD_PRODUCT_ID,IED.MEASUR_MNT_ID, IED.PRICE_ID ,IED.REMARKS from INDENT_ENTRY_DETAILS IED , SUMADHURA_PRICE_LIST SP where IED.PRICE_ID = SP.PRICE_ID and IED.INDENT_ENTRY_DETAILS_ID = ?";
		productList = jdbcTemplate.queryForList(strSql, new Object[] {RequestId});


		if (null != productList && productList.size() > 0) {
			for (Map IndentIssueBean : productList) {
				priceId =  IndentIssueBean.get("PRICE_ID") == null ? "0" : IndentIssueBean.get("PRICE_ID").toString();
				//doublePrice = Double.parseDouble(IndentIssueBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES") == null ? "0" : IndentIssueBean.get("AMOUNT_PER_UNIT_BEFORE_TAXES").toString());
				doublePrice = Double.parseDouble(IndentIssueBean.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "0" : IndentIssueBean.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString());
				
				strProductId = IndentIssueBean.get("PRODUCT_ID") == null ? "0" : IndentIssueBean.get("PRODUCT_ID").toString();
				strSubProductId = IndentIssueBean.get("SUB_PRODUCT_ID") == null ? "0" : IndentIssueBean.get("SUB_PRODUCT_ID").toString();
				strChildProductId = IndentIssueBean.get("CHILD_PRODUCT_ID") == null ? "0" : IndentIssueBean.get("CHILD_PRODUCT_ID").toString();
				strMesurmentId = IndentIssueBean.get("MEASUR_MNT_ID") == null ? "0" : IndentIssueBean.get("MEASUR_MNT_ID").toString();
				strIssuedQantity = IndentIssueBean.get("ISSUED_QTY") == null ? "0" : IndentIssueBean.get("ISSUED_QTY").toString();
				strPrevRemarks = IndentIssueBean.get("REMARKS") == null ? "" : IndentIssueBean.get("REMARKS").toString();
				strToalAmount = String.valueOf(doublePrice*Double.valueOf(strIssuedQantity));



			}
		}






		strPrice = String.valueOf(doublePrice);
		doubleAmount = Double.parseDouble(strQuantity)*doublePrice;
		strAmount = String.valueOf(doubleAmount);

		amountForIndentEntryTable = (Double.valueOf(strIssuedQantity) - Double.valueOf(strQuantity))*doublePrice;
		
		//String fianlQuantity = String.valueOf(Double.parseDouble(strIssuedQantity)-Double.parseDouble(strQuantity));



		strSql = "insert into SUMADHURA_INDENT_ENT_DTLS_HIST(ENTRY_HIST_ID,INDENT_ENTRY_DETAILS_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MESURMENT_ID,ISSUED_QTY,PRICE,TOTAL_AMOUNT,INSERT_DATE) "
			+ "values(SUM_INDENT_ENT_DTLS_HIST_SEQ.nextval,?,?,?,?,?,?,?,?,sysdate)";
		jdbcTemplate.update(strSql, new Object[] {RequestId,strProductId,strSubProductId,strChildProductId,strMesurmentId,strIssuedQantity,strPrice,strToalAmount});


		strSql = "insert into SUMADHURA_RETURNS(RETURN_ID,INDENT_ENTRY_DETAILS_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MESURMENT_ID,RETURN_QUANTITY,PRICE,TOTAL_AMOUNT,RETURN_DATE) "
			+ "values(SUM_RETURNS_SEQ.nextval,?,?,?,?,?,?,?,?,sysdate)";
		jdbcTemplate.update(strSql, new Object[] {RequestId,strProductId,strSubProductId,strChildProductId,strMesurmentId,strQuantity,strPrice,strAmount});

		String strRemarks = "This product "+ strIssuedQantity +" issued ,  "+strQuantity+ " returned and final Qty "+ String.valueOf( Double.valueOf(strIssuedQantity)-Double.valueOf(strQuantity));
		
		strRemarks = "   " +strPrevRemarks + " " + strRemarks;
		
		
		strSql = "update INDENT_ENTRY_DETAILS set  ISSUED_QTY = ISSUED_QTY-"+strQuantity+" ,REMARKS = ? where INDENT_ENTRY_DETAILS_ID = ?";
		jdbcTemplate.update(strSql, new Object[] {strRemarks,RequestId});

		String query="SELECT PRODUT_QTY,INDENT_AVAILABILITY_ID FROM INDENT_AVAILABILITY WHERE PRODUCT_ID='"+strProductId+"' AND SUB_PRODUCT_ID='"+strSubProductId+"' AND CHILD_PRODUCT_ID='"+strChildProductId+"' AND MESURMENT_ID='"+strMesurmentId+"' AND SITE_ID='"+strSiteId+"' ";
		
		dbProductDetailsList = jdbcTemplate.queryForList(query, new Object[] {});
	
		if (null != dbProductDetailsList && dbProductDetailsList.size() > 0) {
			for (Map<String, Object> prods : dbProductDetailsList) {
				return_Quan = Double.parseDouble(prods.get("PRODUT_QTY") == null ? "" : prods.get("PRODUT_QTY").toString());
				availability_Id = (prods.get("INDENT_AVAILABILITY_ID") == null ? "" : prods.get("INDENT_AVAILABILITY_ID").toString());
			}
		}

		return_Form_Quantity=Double.parseDouble(strQuantity);
		return_Quan=return_Quan+return_Form_Quantity;

		strSql = "UPDATE INDENT_AVAILABILITY SET PRODUT_QTY ='"+return_Quan+"' WHERE INDENT_AVAILABILITY_ID ='"+availability_Id+"'";
		jdbcTemplate.update(strSql, new Object[] {});



		strSql = "update SUMADHURA_PRICE_LIST set  STATUS = ?, AVAILABLE_QUANTITY = AVAILABLE_QUANTITY+"+strQuantity+" where PRICE_ID = ?";
		jdbcTemplate.update(strSql, new Object[] {"A", priceId});


		/*strSql = "SELECT QUANTITY,TOTAL_AMOUNT,TO_CHAR(DATE_AND_TIME,'dd-MM-yy') AS DATE_AND_TIME FROM SUMADHURA_CLOSING_BALANCE WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND MEASUREMENT_ID=? AND SITE=? AND ROWNUM < =1 ORDER BY DATE_AND_TIME DESC";
		productList = jdbcTemplate.queryForList(strSql, new Object[] {strProductId, strSubProductId, strChildProductId, strMesurmentId, strSiteId});
		String dateTime = "";
		if (null != productList && productList.size() > 0) {
			for (Map IndentIssueBean : productList) {
				quantity = Double.parseDouble(IndentIssueBean.get("QUANTITY") == null ? "" : IndentIssueBean.get("QUANTITY").toString());
				totalAmt = Double.parseDouble(IndentIssueBean.get("TOTAL_AMOUNT") == null ? "" : IndentIssueBean.get("TOTAL_AMOUNT").toString());
				dateTime = IndentIssueBean.get("DATE_AND_TIME") == null ? "" : IndentIssueBean.get("DATE_AND_TIME").toString();
			}
			}
		totalAmtForOneQty = Double.valueOf(totalAmt / quantity);
		logger.info("totalAmtForOneQty  "+totalAmtForOneQty);
		quantity = Double.valueOf(quantity + Double.valueOf(strQuantity));
		totalAmtForOneQty = totalAmtForOneQty * quantity;
		strSql = "UPDATE SUMADHURA_CLOSING_BALANCE SET QUANTITY=?, TOTAL_AMOUNT=? WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND MEASUREMENT_ID=? AND SITE=? AND TRUNC(DATE_AND_TIME) >= TO_DATE('"+dateTime+"','dd-MM-yy')" ;
		jdbcTemplate.update(strSql, new Object[] {quantity,totalAmtForOneQty,strProductId, strSubProductId, strChildProductId, strMesurmentId, strSiteId});
		 */	return String.valueOf(amountForIndentEntryTable);

	}


	public String strGetIndentEntryId(String RequestId){



		String strSql = "   select  distinct(IE.INDENT_ENTRY_ID) from INDENT_ENTRY IE,INDENT_ENTRY_DETAILS IED where IED.INDENT_ENTRY_ID = IE.INDENT_ENTRY_ID and  IED.INDENT_ENTRY_DETAILS_ID = ?";
		int intIndentEntryId = jdbcTemplate.queryForInt(strSql, new Object[] {RequestId});

		String strIndentEntryId = String.valueOf(intIndentEntryId);
		return strIndentEntryId;


	}


	public String updateFinalAmount(String strIndentEntryId, String TotalAmount){
		String strSql = "update INDENT_ENTRY set TOTAL_AMOUNT  = ? where INDENT_ENTRY_ID = ?";
		jdbcTemplate.update(strSql, new Object[] {TotalAmount,strIndentEntryId});
		return "";


	}
}
