package com.sumadhura.transdao;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.ClosingBalanceBean;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.DateUtil;
import com.sumadhura.util.UIProperties;

@Repository
public class ClosingBalanceDetailsDao extends UIProperties {

	static Logger log = Logger.getLogger(ClosingBalanceDetailsDao.class);

	public List<ClosingBalanceBean> getClosingBalanceDetails(String date, String siteId, HttpSession session) {

		String query = "";
		String dateConversion = "";
		Double grandTotalAmount = 0.0;
		List<Map<String, Object>> dbProductsList = null;
		List<ClosingBalanceBean> list = null;
		JdbcTemplate template = null;
		ClosingBalanceBean bean = null;
		DecimalFormat decimalFormat = new DecimalFormat("##.##");
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());
			if (siteId.equalsIgnoreCase("All")) {
				query = "SELECT P.NAME AS PRODUCT_NAM, S.NAME AS SUBPRODUCT_NAME, C.NAME AS CHILD_PRODUCT_NAME, M.NAME AS MEASUREMENT_NAME, S.SITE_NAME AS SITE_NAME, SCB.QUANTITY AS QUANTITY, SCB.TOTAL_AMOUNT AS TOTAL_AMT, SCB.DATE_AND_TIME AS DATE_TIME FROM SUMADHURA_CLOSING_BALANCE SCB, PRODUCT P, SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE S WHERE TRUNC(SCB.DATE_AND_TIME) <= TO_DATE('"+date+"','dd-MM-yy') AND SCB.PRODUCT_ID=P.PRODUCT_ID AND SCB.SUB_PRODUCT_ID = S.SUB_PRODUCT_ID AND SCB.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID AND SCB.MEASUREMENT_ID=M.MEASUREMENT_ID AND SCB.SITE = S.SITE_ID";
				//query = "SELECT P.NAME AS PRODUCT_NAM, S.NAME AS SUBPRODUCT_NAME, C.NAME AS CHILD_PRODUCT_NAME, M.NAME AS MEASUREMENT_NAME, S.SITE_NAME AS SITE_NAME, MAX(SCB.QUANTITY) AS QUANTITY, MAX(SCB.TOTAL_AMOUNT) AS TOTAL_AMT, MAX(SCB.DATE_AND_TIME) AS DATE_TIME FROM SUMADHURA_CLOSING_BALANCE SCB, PRODUCT P, SUB_PRODUCT S, CHILD_PRODUCT C, MEASUREMENT M, SITE S WHERE TRUNC(SCB.DATE_AND_TIME) <= TO_DATE('"+date+"','dd-MM-yy') AND SCB.PRODUCT_ID=P.PRODUCT_ID AND SCB.SUB_PRODUCT_ID = S.SUB_PRODUCT_ID AND SCB.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID AND SCB.MEASUREMENT_ID=M.MEASUREMENT_ID AND SCB.SITE = S.SITE_ID group by P.NAME,S.NAME,C.NAME,M.NAME,S.SITE_NAME";
				
				dbProductsList = template.queryForList(query);
			} else {
				query = "SELECT P.NAME AS PRODUCT_NAME, S.NAME AS SUBPRODUCT_NAME, C.NAME AS CHILDPRODUCT_NAME, M.NAME AS MEASUREMENT_NAME, ST.SITE_NAME AS SITE_NAME, SCB.QUANTITY AS QUANTITY, SCB.TOTAL_AMOUNT AS TOTAL_AMT, SCB.DATE_AND_TIME AS DATE_AND_TIME FROM SUMADHURA_CLOSING_BALANCE SCB LEFT JOIN PRODUCT P ON SCB.PRODUCT_ID = P.PRODUCT_ID LEFT JOIN SUB_PRODUCT S ON SCB.SUB_PRODUCT_ID = S.SUB_PRODUCT_ID LEFT JOIN CHILD_PRODUCT C ON SCB.CHILD_PRODUCT_ID = C.CHILD_PRODUCT_ID LEFT JOIN MEASUREMENT M ON SCB.MEASUREMENT_ID = M.MEASUREMENT_ID LEFT JOIN SITE ST ON SCB.SITE = ST.SITE_ID INNER JOIN (SELECT P1.NAME AS TMP_PRODUCT_NAME, S1.NAME AS TMP_SUBPRODUCT_NAME, C1.NAME AS TMP_CHILDPRODUCT_NAME, M1.NAME AS TMP_MEASUREMENT_NAME, ST1.SITE_NAME AS TMP_SITE_NAME, MAX(SCB1.DATE_AND_TIME) AS TMP_DATE_AND_TIME FROM SUMADHURA_CLOSING_BALANCE SCB1 LEFT JOIN PRODUCT P1 ON SCB1.PRODUCT_ID = P1.PRODUCT_ID LEFT JOIN SUB_PRODUCT S1 ON SCB1.SUB_PRODUCT_ID = S1.SUB_PRODUCT_ID LEFT JOIN CHILD_PRODUCT C1 ON SCB1.CHILD_PRODUCT_ID = C1.CHILD_PRODUCT_ID LEFT JOIN MEASUREMENT M1 ON SCB1.MEASUREMENT_ID = M1.MEASUREMENT_ID LEFT JOIN SITE ST1 ON SCB1.SITE = ST1.SITE_ID  WHERE  TRUNC(SCB1.DATE_AND_TIME) <= TO_DATE('"+date+"', 'dd-MM-yy') AND  SCB1.SITE = ? GROUP BY P1.NAME, S1.NAME, C1.NAME, M1.NAME, ST1.SITE_NAME)  TMP ON 	P.NAME = TMP.TMP_PRODUCT_NAME AND S.NAME = TMP.TMP_SUBPRODUCT_NAME AND C.NAME = TMP.TMP_CHILDPRODUCT_NAME AND M.NAME = TMP.TMP_MEASUREMENT_NAME AND  ST.SITE_NAME = TMP.TMP_SITE_NAME AND SCB.DATE_AND_TIME = TMP.TMP_DATE_AND_TIME";
				dbProductsList = template.queryForList(query, siteId);
			}
			
			if (null != dbProductsList || dbProductsList.size() > 0) {
				list = new ArrayList<ClosingBalanceBean>();
				for (Map<String, Object> prods : dbProductsList) {
					bean = new ClosingBalanceBean();
					bean.setSiteName(prods.get("SITE_NAME") == null ? "" : prods.get("SITE_NAME").toString());
					bean.setMeasurementid(prods.get("MEASUREMENT_NAME") == null ? "" : prods.get("MEASUREMENT_NAME").toString());
					bean.setProductName(prods.get("PRODUCT_NAME") == null ? "" : prods.get("PRODUCT_NAME").toString());
					bean.setSubProductName(prods.get("SUBPRODUCT_NAME") == null ? "" : prods.get("SUBPRODUCT_NAME").toString());
					bean.setChildproductName(prods.get("CHILDPRODUCT_NAME") == null ? "" : prods.get("CHILDPRODUCT_NAME").toString());
					bean.setQuantity(prods.get("QUANTITY") == null ? "" : prods.get("QUANTITY").toString());
					bean.setTotalAmt(decimalFormat.format(Double.valueOf(prods.get("TOTAL_AMT") == null ? "0.0" : prods.get("TOTAL_AMT").toString())));
					dateConversion = prods.get("DATE_AND_TIME") == null ? "" : prods.get("DATE_AND_TIME").toString();
					
					grandTotalAmount +=  Double.parseDouble(prods.get("TOTAL_AMT") == null ? "0.0" : prods.get("TOTAL_AMT").toString());
					if (StringUtils.isNotBlank(dateConversion)) {
						dateConversion = DateUtil.dateConversion(dateConversion);
					} else {
						dateConversion = "";
					}
					bean.setDate(dateConversion);
					
					list.add(bean);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			session.setAttribute("grandTotalAmount",decimalFormat.format(grandTotalAmount));
		}
		return list;
	}
}
