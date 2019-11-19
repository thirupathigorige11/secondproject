package com.sumadhura.transdao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.naming.NamingException;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;


import com.sumadhura.bean.ContractorBean;
import com.sumadhura.util.DBConnection;
import com.sumadhura.util.UIProperties;

/**
 * @author Madhu Tokala
 * @Company Veeranki's Pvt Ltd.
 * @Date Aug 21, 2017 10:30:39 PM
 */

//@Repository
public class SchedulerDao  extends UIProperties {

	static Logger log = Logger.getLogger(SchedulerDao.class);

	//	@Autowired
	//PlatformTransactionManager transactionManager;

	private PlatformTransactionManager transactionManager;
	private JdbcTemplate template;
	public SchedulerDao(PlatformTransactionManager transactionManager,JdbcTemplate template) {
		this.transactionManager = transactionManager;
		this.template = template;
	}
	@Autowired
	private UtilDao utilDao;

	//11th - 09 -2017 anwards executing proprly

	@SuppressWarnings({ "null", "deprecation" })
	public void statScheduler() throws NamingException {

		log.debug("Inside statScheduler() in SchedulerDao class..");
		List<Map<String, Object>> dbProductsList = null;
		Date date = null;
		String inputDate = "";
		String productId = "";
		String subProductId = "";
		String childProductId = "";
		String measurementId = "";
		String siteId = "";
		String quantity = "";
		String amount = "";
		String invoiceNumber="";
		String DCNumber;
		String query = "";

		//JdbcTemplate template = null;

		Calendar c = Calendar.getInstance(); 

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");

		date = new Date();
		inputDate = simpleDateFormat.format(date);

		Date myDate = null;//new Date("25-AUG-17");

		//template = new JdbcTemplate(DBConnection.getDbConnection());

		query = "SELECT MAX(TO_CHAR(TRUNC(DATE_AND_TIME))) AS DATE_TIME FROM SUMADHU_CLOSING_BAL_BY_PRODUCT";
		dbProductsList = template.queryForList(query);

		if (null != dbProductsList || dbProductsList.size() > 0) {
			for (Map<String, Object> prods : dbProductsList) {

				String maxDate = prods.get("DATE_TIME") == null ? "" : prods.get("DATE_TIME").toString();
				if(StringUtils.isNotBlank(maxDate)) {
					myDate = new Date(maxDate+" 23:59:59");
					long diff = date.getTime() - myDate.getTime();
					Long ii = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
					int k = ii.intValue();
					if (ii > 1) {/*
					query = "SELECT * FROM SUMADHU_CLOSING_BAL_BY_PRODUCT WHERE DATE_AND_TIME = '"+maxDate+"'";
					dbProductsList = template.queryForList(query);

					if (null != dbProductsList || dbProductsList.size() > 0) {
						for (Map<String, Object> prods1 : dbProductsList) {
							quantity = prods1.get("QUANTITY") == null ? "" : prods1.get("QUANTITY").toString();
							amount = prods1.get("TOTAL_AMOUNT") == null ? "0" : prods1.get("TOTAL_AMOUNT").toString();
							`productId = prods1.get("PRODUCT_ID") == null ? "" : prods1.get("PRODUCT_ID").toString();
							subProductId = prods1.get("SUB_PRODUCT_ID") == null ? "" : prods1.get("SUB_PRODUCT_ID").toString();
							childProductId = prods1.get("CHILD_PRODUCT_ID") == null ? "" : prods1.get("CHILD_PRODUCT_ID").toString();
							measurementId = prods1.get("MESURMENT_ID") == null ? "" : prods1.get("MESURMENT_ID").toString();
							siteId = prods1.get("SITE_ID") == null ? "" : prods1.get("SITE_ID").toString();
						}
					}

					for (int h=1; h<=k-1; h++) {

						c.setTime(myDate); 
						c.add(Calendar.DATE, h);
						Date yourDate1 = c.getTime();
						query = "INSERT INTO SUMADHU_CLOSING_BAL_BY_PRODUCT (CLOSING_BAL_BY_PRODUCT_ID, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, QUANTITY, SITE_ID, TOTAL_AMOUNT, DATE_AND_TIME, MEASUREMENT_ID) VALUES (SUMA_CLOSING_BAL_BY_PRODUCT.NEXTVAL,?,?,?,?,?,?,?,?)";
						template.update(query, new Object[] { productId, subProductId, childProductId, quantity, siteId, amount, yourDate1, measurementId });
					}
					 */}
				}
			}
		}


		query = "SELECT SPL.PRODUCT_ID, SPL.SUB_PRODUCT_ID, SPL.CHILD_PRODUCT_ID,SPL.SITE_ID,SPL.UNITS_OF_MEASUREMENT, SPL.AVAILABLE_QUANTITY ,SPL.AMOUNT_PER_UNIT_AFTER_TAXES,SPL.INVOICE_NUMBER,SPL.DC_NUMBER FROM SUMADHURA_PRICE_LIST SPL where STATUS = 'A'";
		//	query = "SELECT P.NAME AS PRODUCT_NAME, S.NAME AS SUBPRODUCT_NAME, C.NAME AS CHILDPRODUCT_NAME, M.NAME AS MEASUREMENT_NAME, ST.SITE_NAME AS SITE_NAME, SCB.QUANTITY AS QUANTITY, SCB.TOTAL_AMOUNT AS TOTAL_AMT, SCB.PRODUCT_ID, SCB.SUB_PRODUCT_ID, SCB.CHILD_PRODUCT_ID, SCB.MEASUREMENT_ID,SCB.SITE FROM SUMADHURA_CLOSING_BALANCE SCB LEFT JOIN PRODUCT P ON SCB.PRODUCT_ID = P.PRODUCT_ID LEFT JOIN SUB_PRODUCT S ON SCB.SUB_PRODUCT_ID = S.SUB_PRODUCT_ID LEFT JOIN CHILD_PRODUCT C ON SCB.CHILD_PRODUCT_ID = C.CHILD_PRODUCT_ID LEFT JOIN MEASUREMENT M ON SCB.MEASUREMENT_ID = M.MEASUREMENT_ID LEFT JOIN SITE ST ON SCB.SITE = ST.SITE_ID INNER JOIN (SELECT P1.NAME AS TMP_PRODUCT_NAME, S1.NAME AS TMP_SUBPRODUCT_NAME, C1.NAME AS TMP_CHILDPRODUCT_NAME, M1.NAME AS TMP_MEASUREMENT_NAME, ST1.SITE_NAME AS TMP_SITE_NAME, MAX(SCB1.DATE_AND_TIME) AS TMP_DATE_AND_TIME FROM SUMADHURA_CLOSING_BALANCE SCB1 LEFT JOIN PRODUCT P1 ON SCB1.PRODUCT_ID = P1.PRODUCT_ID LEFT JOIN SUB_PRODUCT S1 ON SCB1.SUB_PRODUCT_ID = S1.SUB_PRODUCT_ID LEFT JOIN CHILD_PRODUCT C1 ON SCB1.CHILD_PRODUCT_ID = C1.CHILD_PRODUCT_ID LEFT JOIN MEASUREMENT M1 ON SCB1.MEASUREMENT_ID = M1.MEASUREMENT_ID LEFT JOIN SITE ST1 ON SCB1.SITE = ST1.SITE_ID WHERE  TRUNC(SCB1.DATE_AND_TIME) <= TO_DATE('" + inputDate + "', 'dd-MM-yy') GROUP BY P1.NAME, S1.NAME, C1.NAME, M1.NAME, ST1.SITE_NAME)  TMP ON P.NAME = TMP.TMP_PRODUCT_NAME AND S.NAME = TMP.TMP_SUBPRODUCT_NAME AND C.NAME = TMP.TMP_CHILDPRODUCT_NAME AND M.NAME = TMP.TMP_MEASUREMENT_NAME AND  ST.SITE_NAME = TMP.TMP_SITE_NAME AND SCB.DATE_AND_TIME = TMP.TMP_DATE_AND_TIME";
		dbProductsList = template.queryForList(query);

		if (null != dbProductsList || dbProductsList.size() > 0) {
			logger.info("Products list"+ dbProductsList);
			TransactionDefinition def = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager.getTransaction(def);
			for (Map<String, Object> prods : dbProductsList) {

				query = "";
				quantity = prods.get("AVAILABLE_QUANTITY") == null ? "" : prods.get("AVAILABLE_QUANTITY").toString();
				productId = prods.get("PRODUCT_ID") == null ? "" : prods.get("PRODUCT_ID").toString();
				subProductId = prods.get("SUB_PRODUCT_ID") == null ? "" : prods.get("SUB_PRODUCT_ID").toString();
				childProductId = prods.get("CHILD_PRODUCT_ID") == null ? "" : prods.get("CHILD_PRODUCT_ID").toString();
				measurementId = prods.get("UNITS_OF_MEASUREMENT") == null ? "" : prods.get("UNITS_OF_MEASUREMENT").toString();
				siteId = prods.get("SITE_ID") == null ? "" : prods.get("SITE_ID").toString();
				amount = prods.get("AMOUNT_PER_UNIT_AFTER_TAXES") == null ? "" : prods.get("AMOUNT_PER_UNIT_AFTER_TAXES").toString();
				invoiceNumber=prods.get("INVOICE_NUMBER") == null ? "" : prods.get("INVOICE_NUMBER").toString();
				DCNumber=prods.get("DC_NUMBER") == null ? "" : prods.get("DC_NUMBER").toString();
				Float productQuantity    =  Float.parseFloat(quantity); 
				Float unitPrice = Float.parseFloat(amount);
				Float totalAmount = unitPrice * productQuantity;
				String totalAmountAsString = Float.toString(totalAmount);			

				logger.info("quantity"+quantity+":productId"+productId+":totalAmountAsString"+totalAmountAsString);

				/*
				AMOUNT

				//query = "SELECT SUM(AMOUNT) AS AMOUNT FROM SUMADHURA_PRICE_LIST WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND UNITS_OF_MEASUREMENT=? AND STATUS = 'A' AND SITE_ID=?";
				query = "SELECT SUM(TOTAL_AMOUNT) AS TM FROM (SELECT RECIVED_QUANTITY*AMOUNT AS TOTAL_AMOUNT FROM SUMADHURA_PRICE_LIST WHERE PRODUCT_ID=? AND SUB_PRODUCT_ID=? AND CHILD_PRODUCT_ID=? AND UNITS_OF_MEASUREMENT=? AND STATUS = 'A' AND SITE_ID=?)";
				dbProductsList = template.queryForList(query, new Object[]{productId, subProductId, childProductId, measurementId, siteId});

				for (Map<String, Object> prods1 : dbProductsList) {
					amount = prods1.get("TM") == null ? "0" : prods1.get("TM").toString();
				}*/
				try{
					query = "INSERT INTO SUMADHU_CLOSING_BAL_BY_PRODUCT (CLOSING_BAL_BY_PRODUCT_ID, PRODUCT_ID, SUB_PRODUCT_ID, CHILD_PRODUCT_ID, QUANTITY, SITE_ID,PRICE, TOTAL_AMOUNT, DATE_AND_TIME, MEASUREMENT_ID,INVOICE_NUMBER,DC_NUMBER) VALUES (SUMA_CLOSING_BAL_BY_PRODUCT.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?)";
					template.update(query, new Object[] { productId, subProductId, childProductId, quantity, siteId, amount, totalAmountAsString,inputDate, measurementId,invoiceNumber,DCNumber});

				}catch (Exception e) {
					e.printStackTrace();
					// TestEmailFunction objTestEmailFunction = new TestEmailFunction();
					transactionManager.rollback(status);
					log.error("Exception Occured while inserting the data into Closeing Blance Table",e); 
				}
			}
			transactionManager.commit(status);

			log.info("Data Inserted Successfully.  Inside statScheduler() in SchedulerDao class..");
		}
	}

	@SuppressWarnings({ "null", "deprecation" })
	public  void startReadExcelData() throws NamingException {

		log.debug("Inside statScheduler() in SchedulerDao class..");
		List<Map<String, Object>> dbProductsList = null;
		Date date = null;
		String inputDate = "";

		String DCNumber;
		String query = "";

		//JdbcTemplate template = null;

		Calendar c = Calendar.getInstance(); 

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");

		date = new Date();
		inputDate = simpleDateFormat.format(date);

		Date myDate = null;//new Date("25-AUG-17");

		//template = new JdbcTemplate(DBConnection.getDbConnection());


		String strProductId = "";
		String strSubProductId = "";
		String strChildProductId  = "";
		String strMeasurmentId = "";
		int strIndentEntryId = 0;
		int strIndentEntryDetailsId = 0;
		String strUserId = "1000001";
		String strVendorId = "999";
		String strSiteId = "112";
		String strInvoiceNo = "29012019";
		double strPrice = 0;
		double strAmount = 0;
		double strTotalAmount = 0;
		String strRemarks = "Uploaded from excel";
		String strProductName = "";
		String strSubProductName = "";
		String strCholdProductName = "";
		String strMeasurmentName = "";
		double doubleQuantity = 0.0;
		String strState = "Local";
		String strReceiveDate = "29012019";
		double doubleFinalAmount = 0;
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		double doubleBasicAmount = 0.0;
		double doubleCGST = 0.0;
		double doubleIGST =  0.0;
		double Tax = 0.0;
		double TaxAmount = 0.0;
		int count = 0;
		double doublePricePerUnitBeforeTax = 0.0;
		double doublePricePerUnitAfterTax = 0.0;
		double productTotalAmount = 0;
		double tax = 0.0;
		String strProductTotalAmount = "";
		String StringPricePerUnitAfterTax = "";
		String StringPricePerUnitBeforeTax = "";
		String stringBasicAmount = "";
		String strCGST = "";
		String strIGST = "";
		double amountAfterTax = 0.0;
		try{


			SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
			Date parsedDate = dateFormat.parse(strReceiveDate);
			Timestamp receiveTimestamp = new java.sql.Timestamp(parsedDate.getTime());

			String FilePath = "";

			FilePath = "C:\\Users\\Administrator\\Desktop\\Central_adjustment_data_2.xls";


			FileInputStream fs = new FileInputStream(FilePath);
			Workbook wb = Workbook.getWorkbook(fs);

			// TO get the access to the sheet
			Sheet sh = wb.getSheet("ADD PRODUCT AND UPLOAD");

			// To get the number of rows present in sheet
			int totalNoOfRows = sh.getRows();

			totalNoOfRows = totalNoOfRows-2;

			// To get the number of columns present in sheet
			int totalNoOfCols = sh.getColumns();

			for (int row = 1; row <=totalNoOfRows; row++) {

				//for (int col = 0; col < totalNoOfCols; col++) {

				System.out.print(sh.getCell(0, row).getContents() + "\t");

				strProductId = sh.getCell(0, row).getContents();
				strProductName = sh.getCell(1, row).getContents();
				strSubProductId = sh.getCell(2, row).getContents();
				strSubProductName = sh.getCell(3, row).getContents();
				strChildProductId = sh.getCell(4, row).getContents();
				strCholdProductName = sh.getCell(5, row).getContents();
				strMeasurmentId = sh.getCell(6, row).getContents();
				strMeasurmentName = sh.getCell(7,row).getContents();
				
				if(strProductId == null ||strProductId.equals("")){
					break;
				}
				
				doubleQuantity = Double.valueOf(sh.getCell(9, row).getContents());
				//doubleBasicAmount = Double.valueOf(sh.getCell(9, row).getContents());
				strCGST = sh.getCell(10, row).getContents();

				
				
				
				
				if(strCGST.contains(",")){
					strCGST = strCGST.replace(",","");

				}
				doubleCGST = Double.valueOf(strCGST);


				strIGST = sh.getCell(11, row).getContents();

				if(strIGST.contains(",")){
					strIGST = strIGST.replace(",","");

				}

				doubleIGST = Double.valueOf(strIGST);


				stringBasicAmount = sh.getCell(10, row).getContents();

				if(stringBasicAmount.contains(",")){
					stringBasicAmount = stringBasicAmount.replace(",","");

				}
				doubleBasicAmount = Double.valueOf(stringBasicAmount);

				doubleBasicAmount = doubleBasicAmount*doubleQuantity;
				doubleBasicAmount = Double.parseDouble(new DecimalFormat("##.##").format(doubleBasicAmount));

				Tax = Double.valueOf(sh.getCell(12, row).getContents());
				//TaxAmount = doubleBasicAmount * Tax/100;	

				TaxAmount = Double.valueOf(sh.getCell(14, row).getContents());

				//productTotalAmount =  Double.valueOf(sh.getCell(13, row).getContents());





				strProductTotalAmount = sh.getCell(15, row).getContents();
				if(strProductTotalAmount.contains(",")){
					strProductTotalAmount = strProductTotalAmount.replace(",","");

				}
				productTotalAmount = Double.valueOf(strProductTotalAmount);




				//doublePricePerUnitBeforeTax = doubleBasicAmount/doubleQuantity;
				//doublePricePerUnitAfterTax = productTotalAmount/doubleQuantity;


				//doublePricePerUnitAfterTax = Double.valueOf(sh.getCell(9, row).getContents());

				StringPricePerUnitBeforeTax = sh.getCell(10, row).getContents();
				if(StringPricePerUnitBeforeTax.contains(",")){
					StringPricePerUnitBeforeTax = StringPricePerUnitBeforeTax.replace(",","");

				}
				doublePricePerUnitBeforeTax = Double.valueOf(StringPricePerUnitBeforeTax);



				//doublePricePerUnitBeforeTax = Double.valueOf(sh.getCell(12, row).getContents());
				StringPricePerUnitAfterTax = sh.getCell(11, row).getContents();  //StringPricePerUnitAfterTax ,doublePricePerUnitBeforeTax
				if(StringPricePerUnitAfterTax.contains(",")){
					StringPricePerUnitAfterTax = StringPricePerUnitAfterTax.replace(",","");

				}

				doublePricePerUnitAfterTax = Double.valueOf(StringPricePerUnitAfterTax);


				amountAfterTax = doubleBasicAmount + TaxAmount;
				amountAfterTax = Double.parseDouble(new DecimalFormat("##.##").format(amountAfterTax));



				//strTotalA
				//strTotalAmount = Double.valueOf(strQuantity)*Double.valueOf(strPrice);
				//strPrice = strPrice/Double.valueOf(strQuantity);


				query = "SELECT count(1) FROM PRODUCT where PRODUCT_ID = ?";
				count = template.queryForInt(query , new Object[]{strProductId});

				if(count  > 0){


					query = "SELECT count(1) FROM SUB_PRODUCT where SUB_PRODUCT_ID = ?";
					count = template.queryForInt(query , new Object[]{strSubProductId});
					if(count  > 0){

						query = "SELECT count(1) FROM CHILD_PRODUCT where CHILD_PRODUCT_ID = ?";
						count = template.queryForInt(query , new Object[]{strChildProductId});
						if(count  > 0){

							query = "SELECT count(1) FROM MEASUREMENT where MEASUREMENT_ID = ?";
							count = template.queryForInt(query , new Object[]{strMeasurmentId});
							if(count  > 0){
								/*query = "update INDENT_AVAILABILITY set  PRODUT_QTY = PRODUT_QTY - ? where CHILD_PRODUCT_ID = ? and  MESURMENT_ID= ? and SITE_ID = ?";
								count = template.update(query , new Object[]{strQuantity,strChildProductId,strMeasurmentId,strSiteId});

								System.out.println(row);*/


								if(row == 2){

									strIndentEntryId = template.queryForInt("SELECT INDENT_ENTRY_SEQ.NEXTVAL FROM DUAL");



									String invoiceInsertionQry = "INSERT INTO INDENT_ENTRY (INDENT_ENTRY_ID, USER_ID, SITE_ID, " +
									"INVOICE_ID, ENTRY_DATE, VENDOR_ID, INDENT_TYPE, NOTE, RECEIVED_OR_ISSUED_DATE," +
									" INVOICE_DATE," +
									" STATE)" +
									" VALUES (?, ?, ?,?, sysdate, ?, ?,?, ?,?,?)";
									log.debug("Query for invoice insertion = "+invoiceInsertionQry);

									count = template.update(invoiceInsertionQry, new Object[] {strIndentEntryId,
											strUserId,strSiteId,strInvoiceNo,strVendorId,"IN",strRemarks,receiveTimestamp,receiveTimestamp,strState});

								}
								strIndentEntryDetailsId = template.queryForInt("SELECT INDENT_ENTRY_DETAILS_SEQ.NEXTVAL FROM DUAL");


								String indentIssueQry = "INSERT INTO INDENT_ENTRY_DETAILS (INDENT_ENTRY_DETAILS_ID, " +
								"INDENT_ENTRY_ID, PRODUCT_ID, PRODUCT_NAME, SUB_PRODUCT_ID, SUB_PRODUCT_NAME, CHILD_PRODUCT_ID," +
								" CHILD_PRODUCT_NAME, RECEVED_QTY, MEASUR_MNT_ID, MEASUR_MNT_NAME, " +

								" ENTRY_DATE) VALUES (" +
								" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,sysdate)";


								//strTotalAmount = strPrice*strAmount;


								count = template.update(indentIssueQry, new Object[] { 
										strIndentEntryDetailsId, strIndentEntryId,strProductId,strProductName,strSubProductId,strSubProductName,

										strChildProductId,strCholdProductName,doubleQuantity,strMeasurmentId,strMeasurmentName
								});


								int IndentAvailabulityId = 0;
								String isPresentInIndentAvailabulity = "select INDENT_AVAILABILITY_ID from INDENT_AVAILABILITY where CHILD_PRODUCT_ID = ? and MESURMENT_ID = ? and SITE_ID = ?";
								log.debug("Query for new indent entry in indent availability = "+isPresentInIndentAvailabulity);

								try{

									IndentAvailabulityId = template.queryForInt(isPresentInIndentAvailabulity, new Object[] {strChildProductId,
											strMeasurmentId, strSiteId} );

								}catch(Exception e){
									//e.printStackTrace();
								}
								if(IndentAvailabulityId ==0 ){

									IndentAvailabulityId = template.queryForInt("SELECT INDENT_AVAILABILITY_SEQ.NEXTVAL FROM DUAL");



									String requesterQry = "INSERT INTO INDENT_AVAILABILITY (INDENT_AVAILABILITY_ID, PRODUCT_ID," +
									" SUB_PRODUCT_ID, CHILD_PRODUCT_ID, PRODUT_QTY, MESURMENT_ID, SITE_ID)" +
									" VALUES (?, ?, ?, ?, ?, ?, ?)";
									log.debug("Query for new indent entry in indent availability = "+requesterQry);

									count = template.update(requesterQry, new Object[] {IndentAvailabulityId,
											strProductId, strSubProductId,strChildProductId,doubleQuantity,strMeasurmentId,strSiteId } );


								}
								else{
									String requesterQry = "update INDENT_AVAILABILITY set PRODUT_QTY = PRODUT_QTY+"+doubleQuantity+" where CHILD_PRODUCT_ID = ? and MESURMENT_ID = ? and SITE_ID = ? ";
									log.debug("Query for new indent entry in indent availability = "+requesterQry);

									count = template.update(requesterQry, new Object[] {
											strChildProductId,
											strMeasurmentId, strSiteId} );

								}
								query = "INSERT INTO SUMADHURA_PRICE_LIST(PRICE_ID, INVOICE_NUMBER, PRODUCT_ID, " +
								"SUB_PRODUCT_ID, CHILD_PRODUCT_ID, INDENT_AVAILABILITY_ID, STATUS, " +
								"AVAILABLE_QUANTITY, AMOUNT_PER_UNIT_AFTER_TAXES, SITE_ID, UNITS_OF_MEASUREMENT,INDENT_ENTRY_DETAILS_ID," +
								"CREATED_DATE,BASIC_AMOUNT,TOTAL_AMOUNT,AMOUNT_PER_UNIT_BEFORE_TAXES,TAX,TAX_AMOUNT,AMOUNT_AFTER_TAX,RECEVED_QTY) VALUES (SUMADHU_PRICE_LIST.NEXTVAL,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

								//logger.info("Query for save recive details into sumadhura price list table " + query);
								template.update(query, new Object[] {strInvoiceNo,strProductId,strSubProductId,strChildProductId,
										IndentAvailabulityId,"A",doubleQuantity,doublePricePerUnitAfterTax,strSiteId,strMeasurmentId,strIndentEntryDetailsId,receiveTimestamp,
										doubleBasicAmount,productTotalAmount,doublePricePerUnitBeforeTax,Tax,TaxAmount,amountAfterTax,doubleQuantity});





							}else{
								logger.info("strMeasurmentIddoes not exist "+strMeasurmentId);
								transactionManager.rollback(status);
								break;
							}
						}else{
							logger.info("child  id does not exist "+strChildProductId);
							transactionManager.rollback(status);
							break;
						}



					}else{
						logger.info("sub prduct id does not exist "+strSubProductId);
						transactionManager.rollback(status);
						break;
					}

				}else{

					logger.info("prduct id does not exist "+strProductId);
					transactionManager.rollback(status);
					break;
				}

				doubleFinalAmount = doubleFinalAmount+productTotalAmount;


				if(row == totalNoOfRows-2){
					System.out.println("ok");
				}
				System.out.println(row);

				if(row == totalNoOfRows-1){
					BigDecimal bigDecimalTotalProductCost =	BigDecimal.valueOf(doubleFinalAmount);

					query = "update INDENT_ENTRY set TOTAL_AMOUNT = ? where INVOICE_ID = ?";
					template.update(query, new Object[] {bigDecimalTotalProductCost,strInvoiceNo});

				}

			}
			//}
		}catch(Exception e){
			e.printStackTrace();
			transactionManager.rollback(status);
		}
		transactionManager.commit(status);

	}
	@SuppressWarnings({ "null", "deprecation" })
	public   void startReadExcelDataForProducts() throws NamingException {

		log.debug("Inside statScheduler() in SchedulerDao class..");
		List<Map<String, Object>> dbProductsList = null;
		Date date = null;
		String inputDate = "";

		String DCNumber;
		String query = "";

		//JdbcTemplate template = null;

		Calendar c = Calendar.getInstance(); 

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");

		date = new Date();
		inputDate = simpleDateFormat.format(date);

		Date myDate = null;//new Date("25-AUG-17");

		//template = new JdbcTemplate(DBConnection.getDbConnection());


		String strProductId = "";
		String strSubProductId = "";
		String strChildProductId  = "";
		String strMeasurmentId = "";
		int strIndentEntryId = 0;
		int strIndentEntryDetailsId = 0;
		String strUserId = "Soham";
		String strVendorId = "105";
		String strSiteId = "105";
		String strInvoiceNo = "28092017";
		double strPrice = 0;
		double strAmount = 0;
		double strTotalAmount = 0;
		String strRemarks = "Uploaded from excel";
		String strProductName = "";
		String strSubProductName = "";
		String strCholdProductName = "";
		String strMeasurmentName = "";
		String strQuantity = "";
		String strState = "Local";
		double doubleFinalAmount = 0;
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//	TransactionStatus status = transactionManager.getTransaction(def);

		int count = 0;

		try{




			String FilePath = "C:\\Users\\pavan\\Desktop\\Sumadhura_Docs\\Central_closing_balance\\Updated_Central_closing_stock(04-01-2018).XLS";
			FileInputStream fs = new FileInputStream(FilePath);
			Workbook wb = Workbook.getWorkbook(fs);

			// TO get the access to the sheet
			Sheet sh = wb.getSheet("measurement");

			// To get the number of rows present in sheet
			int totalNoOfRows = sh.getRows();

			// To get the number of columns present in sheet
			int totalNoOfCols = sh.getColumns();

			for (int row = 1; row < totalNoOfRows; row++) {

				//for (int col = 0; col < totalNoOfCols; col++) {

				System.out.print(sh.getCell(0, row).getContents() + "\t");

				//strProductId = sh.getCell(0, row).getContents();
				//	strProductName = sh.getCell(1, row).getContents();
				//strSubProductId = sh.getCell(0, row).getContents();
				//strSubProductName = sh.getCell(2, row).getContents();
				strChildProductId = sh.getCell(0, row).getContents();
				//strCholdProductName = sh.getCell(2, row).getContents();
				strMeasurmentId = sh.getCell(1, row).getContents();
				strMeasurmentName = sh.getCell(2,row).getContents();
				/*strQuantity = sh.getCell(9, row).getContents();
				strPrice = Double.valueOf(sh.getCell(19, row).getContents());
				strAmount =  Double.valueOf(sh.getCell(19, row).getContents());
				strTotalAmount = Double.valueOf(sh.getCell(19, row).getContents());
				strPrice = strPrice/Double.valueOf(strQuantity);*/


				if(strMeasurmentId !=null ||! strMeasurmentId.equals("")){

					String indentIssueQry = "insert into MEASUREMENT(MEASUREMENT_ID,NAME,CREATED_DATE,CHILD_PRODUCT_ID,STATUS)values(?,?,sysdate,?,?)";
					template.update(indentIssueQry, new Object[] {strMeasurmentId,strMeasurmentName,strChildProductId,"A"});
				}

			}
			//}
		}catch(Exception e){
			e.printStackTrace();
			//transactionManager.rollback(status);
		}
		//transactionManager.commit(status);

	}
	public static void main(String [] args) throws BiffException, IOException, NamingException, ParseException{


		String strReceiveDate = "17042018";
		SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
		Date parsedDate = dateFormat.parse(strReceiveDate);
		Timestamp receiveTimestamp = new java.sql.Timestamp(parsedDate.getTime());

		System.out.println(receiveTimestamp);
		String strAmount = "21707073.09";

		double doubleSubTotal = Double.valueOf(strAmount);


		long l = (new Double(doubleSubTotal)).longValue();

		BigDecimal b = BigDecimal.valueOf(doubleSubTotal);

		System.out.println("double=" + doubleSubTotal + ", long=" + l +"mm "+b);


		//startReadExcelDataForProducts();


		/*


		String FilePath = "C:\\Users\\SYSTEM@2\\Desktop\\Sumadhura_docs\\update_product_list(9-9-2017)_DB.xls";
		FileInputStream fs = new FileInputStream(FilePath);
		Workbook wb = Workbook.getWorkbook(fs);

		// TO get the access to the sheet
		Sheet sh = wb.getSheet("Product_list");

		// To get the number of rows present in sheet
		int totalNoOfRows = sh.getRows();

		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();

		for (int row = 0; row < totalNoOfRows; row++) {

			for (int col = 0; col < totalNoOfCols; col++) {
				System.out.print(sh.getCell(col, row).getContents() + "\t");
			}
			//logger.info();
		}
		 */}

	public   void startReadExcelDataForAddChildProducts() throws NamingException {

		log.debug("Inside statScheduler() in SchedulerDao class..");
		List<Map<String, Object>> dbProductsList = null;
		Date date = null;
		String inputDate = "";

		String DCNumber;
		String query = "";

		//JdbcTemplate template = null;

		Calendar c = Calendar.getInstance(); 

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");

		date = new Date();
		inputDate = simpleDateFormat.format(date);

		Date myDate = null;//new Date("25-AUG-17");

		//template = new JdbcTemplate(DBConnection.getDbConnection());

		String insertProducts = "";
		String insertSubProducts = "";
		String strProductId = "";
		String strSubProductId = "";
		String strChildProductId  = "";
		String strMeasurmentId = "";
		int strIndentEntryId = 0;
		int strIndentEntryDetailsId = 0;
		String strUserId = "Soham";
		String strVendorId = "105";
		String strSiteId = "105";
		String strInvoiceNo = "28092017";
		double strPrice = 0;
		double strAmount = 0;
		double strTotalAmount = 0;
		String strRemarks = "Uploaded from excel";
		String strProductName = "";
		String strSubProductName = "";
		String strCholdProductName = "";
		String strMeasurmentName = "";
		String strQuantity = "";
		String strState = "Local";
		double doubleFinalAmount = 0;
		//TransactionDefinition def = new DefaultTransactionDefinition();
		//	TransactionStatus status = transactionManager.getTransaction(def);

		int count = 0;
		int queryCount = 0;
		try{




			String FilePath = "F:\\Sumadhara\\Final_1.xls";
			FileInputStream fs = new FileInputStream(FilePath);
			Workbook wb = Workbook.getWorkbook(fs);

			// TO get the access to the sheet
			Sheet sh = wb.getSheet("Sheet1");

			// To get the number of rows present in sheet
			int totalNoOfRows = sh.getRows();

			// To get the number of columns present in sheet
			int totalNoOfCols = sh.getColumns();

			for (int row = 1; row < totalNoOfRows; row++) {

				//for (int col = 0; col < totalNoOfCols; col++) {



				String strIndentAvailabulityId = "";


				strProductId = sh.getCell(0, row).getContents();
				strProductName = sh.getCell(1, row).getContents().trim();
				strSubProductId = sh.getCell(2, row).getContents();
				strSubProductName = sh.getCell(3, row).getContents().trim();
				strChildProductId = sh.getCell(4, row).getContents();
				strCholdProductName = sh.getCell(5, row).getContents().trim();
				strMeasurmentId = sh.getCell(6, row).getContents();
				//	strIndentAvailabulityId = sh.getCell(0,row).getContents().trim();
				//strQuantity = sh.getCell(6, row).getContents().trim();
				//strSiteId = sh.getCell(4, row).getContents().trim();
				strMeasurmentName =  sh.getCell(7, row).getContents().trim();
				/*strQuantity = sh.getCell(9, row).getContents();
				strPrice = Double.valueOf(sh.getCell(19, row).getContents());
				strAmount =  Double.valueOf(sh.getCell(19, row).getContents());
				strTotalAmount = Double.valueOf(sh.getCell(19, row).getContents());
				strPrice = strPrice/Double.valueOf(strQuantity);*/


				//System.out.print(sh.getCell(0, row).getContents() + "\t");

				//String productCount = "insert into INDENT_AVAILABILITY(INDENT_AVAILABILITY_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,SITE_ID,MESURMENT_ID,PRODUT_QTY)values(?,?,?,?,?,?,?)";
				//int queryCount = template.update(productCount, new Object[] {strIndentAvailabulityId,strProductId,strSubProductId,strChildProductId,
				//		strSiteId,strMeasurmentId,strQuantity});

				//count++;

				query = "SELECT count(1) FROM PRODUCT where PRODUCT_ID = ?";
				queryCount = template.queryForInt(query , new Object[]{strProductId});


				//System.out.println("total number of record inserted is "+count);
				if(queryCount > 0) {

					String subProductCount = "select count(1) from  sub_product where PRODUCT_ID = ? and SUB_PRODUCT_ID = ? and NAME = ? and status = ?";
					queryCount = template.queryForInt(subProductCount, new Object[] {strProductId,strSubProductId,strSubProductName,"A"});

					if(queryCount > 0 ) {

						String childProductCount = "select count(1) from  child_product where SUB_PRODUCT_ID = ? and CHILD_PRODUCT_ID = ? and NAME = ? and status = ?";
						queryCount = template.queryForInt(childProductCount, new Object[] {strSubProductId,strChildProductId,strCholdProductName,"A"});

						if(queryCount  == 0) {

							String measurmentCount = "select count(1) from  measurement where MEASUREMENT_ID = ? and NAME = ? and CHILD_PRODUCT_ID = ?  and status = ?";
							queryCount = template.queryForInt(measurmentCount, new Object[] {strMeasurmentId,strMeasurmentName,strChildProductId,"A"});

							if(queryCount  == 0) {

								count++;
								String indentIssueQry = "insert into CHILD_PRODUCT(CHILD_PRODUCT_ID,NAME,SUB_PRODUCT_ID,STATUS)values(?,?,?,?)";
								template.update(indentIssueQry, new Object[] {strChildProductId,strCholdProductName,strSubProductId,"A"});



								//
								indentIssueQry = "insert into MEASUREMENT(MEASUREMENT_ID,NAME,CREATED_DATE,CHILD_PRODUCT_ID,STATUS)values(?,?,sysdate,?,?)";
								template.update(indentIssueQry, new Object[] {strMeasurmentId,strMeasurmentName,strChildProductId,"A"});

							}else{
								System.out.println("this measurment all ready exst "+strMeasurmentId+strMeasurmentName);
							}

						}else{
							System.out.println("this child product all ready exst "+strChildProductId+strCholdProductName);
						}


					}

					else{
						System.out.println("this sub  product not exst "+strSubProductId+strSubProductName);

						//insertProducts = "insert into SUB_PRODUCT(SUB_PRODUCT_ID,PRODUCT_ID,NAME,STATUS)values(?,?,?,'A')";
						//template.update(insertProducts, new Object[] {strSubProductId,strProductId,strSubProductName});

					}


				}else{
					System.out.println("this product not exst "+strProductId + strProductName);
					//insertProducts = "insert into PRODUCT(CREATED_DATE,NAME,PRODUCT_DEPT,PRODUCT_ID,STATUS)values(sysdate,?,'MARKETING',?,?)";
					//template.update(insertProducts, new Object[] {strProductName,strProductId,"A"});

				}


			}

			System.out.println("total products "+count);

			//}
		}catch(Exception e){
			e.printStackTrace();
			//transactionManager.rollback(status);
		}
		//transactionManager.commit(status);

	}

	/*public void updatePriceListIdInIndentEntryTable(){
		List<Map<String, Object>> dbProductsList = null;
		String strPriceListId = "";
		String strIndentEntryId = "";
		try{
			String prodsQry = "select SPL.PRICE_ID as SPL_PRICE_ID ,SPL.INDENT_ENTRY_DETAILS_ID as IED_ENTRY_DETAILS_ID from SUMADHURA_PRICE_LIST SPL ,INDENT_ENTRY_DETAILS IED where IED.INDENT_ENTRY_DETAILS_ID =SPL.INDENT_ENTRY_DETAILS_ID";
			log.debug("Query to fetch product = "+prodsQry);

			dbProductsList = template.queryForList(prodsQry, new Object[]{});

			for(Map<String, Object> prods : dbProductsList) {
				strPriceListId = String.valueOf(prods.get("SPL_PRICE_ID") == null ? "" : prods.get("SPL_PRICE_ID"));
				strIndentEntryId = String.valueOf(prods.get("IED_ENTRY_DETAILS_ID") == null ? "" : prods.get("IED_ENTRY_DETAILS_ID"));

				 prodsQry = "update INDENT_ENTRY_DETAILS IED set IED.PRICE_ID  = ? where IED.INDENT_ENTRY_DETAILS_ID = ?" ;


				 template.update(prodsQry, new Object[]{strPriceListId,strIndentEntryId});




			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}*/
	public void updatePriceListIdInIndentEntryTable(){
		List<Map<String, Object>> dbProductsList = null;
		String strEntryId = "";
		String strEntryDate = "";
		try{
			String prodsQry = "select Distinct(IED.INDENT_ENTRY_ID) as ENTRY_ID,IED.ENTRY_DATE as ENTRY_DATE from INDENT_ENTRY IE,INDENT_ENTRY_DETAILS IED "+
			" WHERE IED.INDENT_ENTRY_ID = IE.INDENT_ENTRY_ID and IE.INDENT_TYPE in ('OUT','OUTO')";
			log.debug("Query to fetch product = "+prodsQry);

			dbProductsList = template.queryForList(prodsQry, new Object[]{});

			for(Map<String, Object> prods : dbProductsList) {
				strEntryId = String.valueOf(prods.get("ENTRY_ID") == null ? "" : prods.get("ENTRY_ID"));
				strEntryDate = String.valueOf(prods.get("ENTRY_DATE") == null ? "" : prods.get("ENTRY_DATE"));

				prodsQry = "UPDATE INDENT_ENTRY SET ENTRY_DATE = ? where INDENT_ENTRY_ID = ?" ;

				Timestamp ts = Timestamp.valueOf(strEntryDate);
				System.out.println(ts);
				template.update(prodsQry, new Object[]{ts,strEntryId});




			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	public void insertVendorDetails(){





		log.debug("Inside statScheduler() in SchedulerDao class..");
		List<Map<String, Object>> dbProductsList = null;
		Date date = null;
		String inputDate = "";

		String DCNumber;
		String query = "";

		//JdbcTemplate template = null;

		Calendar c = Calendar.getInstance(); 

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy");

		date = new Date();
		inputDate = simpleDateFormat.format(date);

		Date myDate = null;//new Date("25-AUG-17");

		//template = new JdbcTemplate(DBConnection.getDbConnection());

		String strVendorName = "";
		String strVendorAddress = "";
		String strPhone = "";
		String strEmail = "";
		String strMobileNo  = "";
		String strContactPerson = "";
		String strTypeOfBusiness = "";
		String strNatureOfBusiness = "";
		String strParntCompany = "";
		String strOversisReprosentative = "";
		String strYearOfEstablshment = "";
		String strNoEmployees = "";
		String strGSTIN = "";
		String strPanNo = "";
		String strBankAccNo = "";
		String strAccHolderName = "";
		String strAccountType = "";
		String strBankName = "";
		String strIFSC = "";
		String strBranchName = "";
		String strBankCity = "";
		String strBankState = "";

		int count = 0;
		int queryCount = 0;
		int intVendorId = 0;
		try{




			String FilePath = "C:\\Users\\pavan\\Downloads\\Marketing Vendors.xls";
			FileInputStream fs = new FileInputStream(FilePath);
			Workbook wb = Workbook.getWorkbook(fs);

			// TO get the access to the sheet
			Sheet sh = wb.getSheet("Sheet1");

			// To get the number of rows present in sheet
			int totalNoOfRows = sh.getRows();

			// To get the number of columns present in sheet
			int totalNoOfCols = sh.getColumns();

			for (int row = 1; row < totalNoOfRows; row++) {

				//for (int col = 0; col < totalNoOfCols; col++) {



				String strIndentAvailabulityId = "";


				//	strProductId = sh.getCell(0, row).getContents();
				strVendorName = sh.getCell(1, row).getContents().trim();
				
				
				if(strVendorName.equals("")){
					break;	
				}
				
				strVendorAddress = sh.getCell(2, row).getContents();
				strPhone = sh.getCell(3, row).getContents().trim();
				strEmail = sh.getCell(4, row).getContents();
				strMobileNo = sh.getCell(5, row).getContents().trim();
				strContactPerson = sh.getCell(6, row).getContents();
				strTypeOfBusiness = sh.getCell(7,row).getContents().trim();
				strNatureOfBusiness = sh.getCell(8, row).getContents().trim();
				strParntCompany = sh.getCell(9, row).getContents().trim();
				strOversisReprosentative = sh.getCell(10, row).getContents().trim();
				strYearOfEstablshment =  sh.getCell(11, row).getContents().trim();
				strNoEmployees = sh.getCell(12, row).getContents();
				strGSTIN =  sh.getCell(13, row).getContents();
				strPanNo =   sh.getCell(14, row).getContents();


				strBankAccNo =  sh.getCell(18, row).getContents();
				strAccHolderName = sh.getCell(19, row).getContents();
				strAccountType = sh.getCell(20, row).getContents();
				strBankName = sh.getCell(21, row).getContents();
				strIFSC = sh.getCell(22, row).getContents();

				strBranchName = sh.getCell(23, row).getContents();
				strBankCity = sh.getCell(24, row).getContents();
				strBankState = sh.getCell(25, row).getContents();

				String strVendorId = "";





				query = "SELECT count(1) FROM VENDOR_DETAILS where VENDOR_NAME = ?";
				queryCount = template.queryForInt(query , new Object[]{strVendorName});


				//System.out.println("total number of record inserted is "+count);
				//queryCount = 1;
				if(queryCount == 0) {

					String subProductCount = "SELECT count(1) FROM VENDOR_DETAILS where GSIN_NUMBER = ?";
					queryCount = template.queryForInt(subProductCount, new Object[] {strGSTIN });

					if(queryCount == 0 ) {
						//queryCount = 0;
						String childProductCount = "SELECT count(1) FROM VENDOR_DETAILS where PAN_NUMBER = ?";
						queryCount = template.queryForInt(childProductCount, new Object[] {strPanNo});



						if(queryCount  == 0) {

							query = "select VENDORID_SEQ.nextval from dual";
							intVendorId = template.queryForInt(query);


							count++;
							strVendorId = "VND"+intVendorId;

							String indentIssueQry = "insert into VENDOR_DETAILS(VENDOR_NAME,ADDRESS,LANDLINE_NO,EMP_EMAIL,MOBILE_NUMBER,VENDOR_CON_PER_NAME,TYPE_OF_BUSINESS, "+
							" NATURE_OF_BUSINESS,PARENT_COMPANY,OVERSEASES_REPRESENTATIVE,ESTABLISHED_YEAR,NO_OF_EMPLOYEES,GSIN_NUMBER, "+
							" PAN_NUMBER,BANK_ACC_NUMBER,ACC_HOLDER_NAME,ACC_TYPE,BANK_NAME,IFSC_CODE,STATUS,VENDOR_ID,BRANCH_NAME,BRANCH_CITY,BRANCH_STATE)" +
							"values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
							template.update(indentIssueQry, new Object[] {strVendorName,strVendorAddress,strPhone,strEmail,
									strMobileNo,strContactPerson,strTypeOfBusiness,strNatureOfBusiness,strParntCompany,strOversisReprosentative,
									strYearOfEstablshment,strNoEmployees,strGSTIN,strPanNo,strBankAccNo,strAccHolderName,strAccountType,
									strBankName,strIFSC,"A",strVendorId,strBranchName,strBankCity,strBankState});




						}else{
							System.out.println("this strPanNo exst "+strPanNo);
						}


					}

					else{
						System.out.println("this strGSTINt exst "+strGSTIN);

						//insertProducts = "insert into SUB_PRODUCT(SUB_PRODUCT_ID,PRODUCT_ID,NAME,STATUS)values(?,?,?,'A')";
						//template.update(insertProducts, new Object[] {strSubProductId,strProductId,strSubProductName});

					}


				}else{
					System.out.println("this strVendorName  exst "+strVendorName );
					//insertProducts = "insert into PRODUCT(CREATED_DATE,NAME,PRODUCT_DEPT,PRODUCT_ID,STATUS)values(sysdate,?,'MARKETING',?,?)";
					//template.update(insertProducts, new Object[] {strProductName,strProductId,"A"});

				}
			
			}

			System.out.println("total products "+count);

			//}
		}catch(Exception e){
			e.printStackTrace();
			//transactionManager.rollback(status);
		}
		//transactionManager.commit(status);



	}
	public void startReadContractorData() throws BiffException, IOException {
		System.out.println("SchedulerDao.startReadContractorData()");


		//code for insert data in SUMADHURA_CONTRACTOR
		try {

			int contratorSeqNum = template.queryForInt("select SUMADHURA_CONTRACTOR_SEQ.NEXTVAL from dual");
			String str = "SIPL/CON/" + contratorSeqNum;

			String query="select distinct(CONTRACTOR_NAME) from INDENT_ENTRY WHERE CONTRACTOR_NAME IS NOT NULL";
			List<Map<String, Object>> list=template.queryForList(query);
			System.out.println(list.size());

			List<String> querysForBacth=new ArrayList<String>();
			for (Map<String, Object> map : list) {
				contratorSeqNum = template.queryForInt("select SUMADHURA_CONTRACTOR_SEQ.NEXTVAL from dual");
				str = "SIPL/CON/" + contratorSeqNum;

				String contractor_name=(String)map.get("CONTRACTOR_NAME");
				String queryInsert="";//insert into SUMADHURA_CONTRACTOR(CONTRACTOR_NAME,CONTRACTOR_ID) values('"+contractor_name+"','"+str+"')";
				int index=contractor_name.indexOf(" ");
				String first_name="";
				String last_name="";
				if(index>0){
					first_name=contractor_name.substring(0, index);
					last_name=contractor_name.substring(index, contractor_name.length());
				}else{
					first_name=contractor_name;
					last_name="";
				}
				queryInsert="insert into SUMADHURA_CONTRACTOR(CONTRACTOR_NAME,FIRST_NAME,LAST_NAME,STATUS,CONTRACTOR_ID) values('"+contractor_name+"','"+first_name+"','"+last_name+"','I','"+str+"')";

				int i=template.update(queryInsert);

				System.out.println(queryInsert+" querry "+i);
				querysForBacth.add(queryInsert);
			}	

			//int arr[]=template.batchUpdate((String[]) querysForBacth.toArray());
			System.out.println("hi");
			//transactionManager.commit(status);
			System.out.println("SchedulerDao.startReadContractorData() " +list.size());
		} catch (Exception e) {
			//transactionManager.rollback(status);
			e.printStackTrace();

		}
		System.out.println("SchedulerDao.startReadContractorData() done" );


		//code for update the contractor id to INDENT_ENTRY
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String queryforContractorName="select CONTRACTOR_NAME,CONTRACTOR_ID from  SUMADHURA_CONTRACTOR";
			List<Map<String, Object>> list=template.queryForList(queryforContractorName);
			System.out.println(list.size());

			for (Map<String, Object> map : list) {
				String contractor_name=(String)map.get("CONTRACTOR_NAME");
				String id=(String)map.get("CONTRACTOR_ID");
				String query="Update INDENT_ENTRY set CONTRACTOR_ID='"+id+"' WHERE CONTRACTOR_NAME ='"+contractor_name+"'";
				int i=template.update(query);
				System.out.println(query+" "+i);
			}
			transactionManager.commit(status);
			System.out.println(list.size());

		} catch (Exception e){
			transactionManager.rollback(status);
			e.printStackTrace();
		}


		/*


		String contractor_id="";
		 String contractor_name="";
		 String first_name="";
		 String last_name="";
		 String address="";
		 String pan_number="";
		 String mobile_number="";
		 String alternate_mobile_number="";

		 String bank_acc_number="";
		 String bank_name="";
		 String ifsc_code="";
		 String email="";
		 String GSTIN="";



		String FilePath = "";

		FilePath = "";
		FileInputStream fs = null;
		Workbook wb = null;


			try {
				FilePath = "D:\\CONTRACTOR.xls";//CONTRACTOR.xlsx
				File file = new File("D:\\");


				 fs = new FileInputStream(FilePath);
				 wb = Workbook.getWorkbook(fs);

			} catch (Exception e) {
				e.printStackTrace();
			}

		// TO get the access to the sheet
		Sheet sh = wb.getSheet("Sheet1");

		// To get the number of rows present in sheet
		int totalNoOfRows = sh.getRows();

		totalNoOfRows = totalNoOfRows-2;

		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();

		for (int row = 1; row <=totalNoOfRows; row++) {

			System.out.print(sh.getCell(0, row).getContents() + "\t");
			ContractorBean bean=new ContractorBean();
			contractor_name = sh.getCell(1, row).getContents();
			int index=contractor_name.indexOf(" ");
			first_name=contractor_name.substring(0, index);
			last_name=contractor_name.substring(index, contractor_name.length());
			address = sh.getCell(2, row).getContents();
			pan_number = sh.getCell(3, row).getContents();
			mobile_number = sh.getCell(4, row).getContents();
			bank_acc_number = sh.getCell(5, row).getContents();
			bank_name = sh.getCell(6, row).getContents();
			ifsc_code = sh.getCell(7,row).getContents();
			alternate_mobile_number =sh.getCell(8, row).getContents();

			bean.setContractor_name(contractor_name==null?"":contractor_name);
			bean.setFirst_name(first_name);
			bean.setLast_name(last_name);
			bean.setAddress(address==null?"":address);
			bean.setPan_number(pan_number==null?"":pan_number);
			bean.setMobile_number(mobile_number==null?"":mobile_number);
			bean.setBank_acc_number(bank_acc_number==null?"":bank_acc_number);
			bean.setBank_name(bank_name==null?"":bank_name);
			bean.setIfsc_code(ifsc_code==null?"":ifsc_code);
			bean.setAlternate_mobile_number(alternate_mobile_number==null?"":alternate_mobile_number);
			System.out.println(bean);
			TransactionDefinition def = new DefaultTransactionDefinition();
			TransactionStatus status = transactionManager.getTransaction(def);
			try {

				int contratorSeqNum = template.queryForInt("select SUMADHURA_CONTRACTOR_SEQ.NEXTVAL from dual");
				String str = "SIPL/CON/" + contratorSeqNum;

			String query="select distinct(CONTRACTOR_NAME) from INDENT_ENTRY WHERE CONTRACTOR_NAME IS NOT NULL";
			List<Map<String, Object>> list=template.queryForList(query);
			List<String> querysForBacth=new ArrayList<String>();
			for (Map<String, Object> map : list) {
				String name=(String)map.get("CONTRACTOR_NAME");
				String queryInsert="insert into SUMADHURA_CONTRACTOR(CONTRACTOR_NAME,CONTRACTOR_ID) values('"+name+"','"+str+"')";
				querysForBacth.add(queryInsert);
			}	
			int arr[]=template.batchUpdate((String[]) querysForBacth.toArray());
			System.out.println(arr.length);


				 query = "insert into SUMADHURA_CONTRACTOR(CONTRACTOR_ID,FIRST_NAME,LAST_NAME,ADDRESS,PAN_NUMBER,MOBILE_NUMBER,ALTERNATE_MOBILE_NUMBER,BANK_ACC_NUMBER,BANK_NAME,IFSC_CODE,STATUS,GSTIN,EMAIL)"
						+ "  values(?,?,?,?,?,?,?,?,?,?,'A',?,?)";

				System.out.println(str);


				Object[] queryParams = { str,bean.getFirst_name(),bean.getLast_name(), 
									bean.getAddress(), bean.getPan_number(),
									bean.getMobile_number(),bean.getAlternate_mobile_number(), 
									bean.getBank_acc_number(), bean.getBank_name(), 
									bean.getIfsc_code(),bean.getGSTIN(),
									bean.getEmail() };
							int result = template.update(query, queryParams);
								String response="";
							if (result != 0) {
								response = "Registration done Successfully.";
							} else {
								response = "Registration Failed.";
							}
							System.out.println(response);

			transactionManager.commit(status);
			} catch (Exception e) {
				System.out.println(sh.getCell(5, row).getCellFormat().toString());
				System.out.println(e.getMessage());
				transactionManager.rollback(status);
				System.out.println("trnsaction rollbacked "+bean);
			}

		}//for
	System.out.println("row inserted "+count);

	}*/
}
//this method is for updating the GRN NO format
	public void updateGRNNO() {/*
		int result=0;
		String updateDC_GRN_NO_Query="select DC_ENTRY_ID,DC_GRN_NO,trunc(ENTRY_DATE),to_char(ENTRY_DATE,'MM') MonthNo,to_char(ENTRY_DATE,'yy') yearNo "
				+ "from DC_ENTRY where to_date(to_char(ENTRY_DATE,'MM-yyyy'),'MM-yyyy') >=TO_DATE('01-2019', 'MM-yyyy')";
		
		select DC_ENTRY_ID,DC_GRN_NO,trunc(ENTRY_DATE),to_char(ENTRY_DATE,'MM') MonthNo,to_char(ENTRY_DATE,'yy') yearNo 
		from DC_ENTRY where to_date(to_char(ENTRY_DATE,'MM-yyyy'),'MM-yyyy') >=TO_DATE('01-2018', 'MM-yyyy') and  to_date(to_char(ENTRY_DATE,'MM-yyyy'),'MM-yyyy') <=TO_DATE('03-2018', 'MM-yyyy'); 
		
		List<Map<String, Object>> listOfDCGRNNo=jdbcTemplate.queryForList(updateDC_GRN_NO_Query);
		for (Map<String, Object> map : listOfDCGRNNo) {
			System.out.println(map);
			String indentEntryId=map.get("DC_ENTRY_ID")==null?"":map.get("DC_ENTRY_ID").toString();
			String MonthNo=map.get("MonthNo")==null?"0":map.get("MonthNo").toString();
			String yearNo=map.get("yearNo")==null?"0":map.get("yearNo").toString();
			String GRN_NO=map.get("DC_GRN_NO")==null?"":map.get("DC_GRN_NO").toString();
			
			String[] strArrayGRNNo=GRN_NO.split("/");
			String formattedGRNNO=strArrayGRNNo[0].trim()+"/"+strArrayGRNNo[1];
			int monthNo=Integer.valueOf(MonthNo);
			int year=Integer.valueOf(yearNo);
			String grnNo="";
			//checking month number 
			if(monthNo<=3){
				System.out.println("The current Month is less than the april");
				grnNo+=(year-1)+"-"+year;
			}else if(monthNo>=4){
				System.out.println("The current Month is greater than the april");
				grnNo+=(year)+"-"+(year+1);
			}
			formattedGRNNO=formattedGRNNO+"/"+grnNo;
			String updateGRN_NOQuery="update DC_ENTRY set DC_GRN_NO='"+formattedGRNNO+"' where DC_ENTRY_ID='"+indentEntryId+"'";
			if(GRN_NO.length()!=0)
			result=	jdbcTemplate.update(updateGRN_NOQuery);
			else{
				
			}
		}
		
		String updateInvoiceGRNNoquery="select INDENT_ENTRY_ID,GRN_NO,to_char(ENTRY_DATE,'MM') MonthNo,to_char(ENTRY_DATE,'yy') yearNo,INDENT_TYPE "
				+ " from INDENT_ENTRY where INDENT_TYPE in ('IN','IND') and  to_date(to_char(ENTRY_DATE,'MM-yyyy'),'MM-yyyy') >=TO_DATE('01-2019', 'MM-yyyy')";
		List<Map<String, Object>> listOfGRNNo=jdbcTemplate.queryForList(updateInvoiceGRNNoquery);
		for (Map<String, Object> map : listOfGRNNo) {
			System.out.println(map);
			String indentEntryId=map.get("INDENT_ENTRY_ID")==null?"":map.get("INDENT_ENTRY_ID").toString();
			String MonthNo=map.get("MonthNo")==null?"0":map.get("MonthNo").toString();
			String yearNo=map.get("yearNo")==null?"0":map.get("yearNo").toString();
			String GRN_NO=map.get("GRN_NO")==null?"0":map.get("GRN_NO").toString();
			String MonthNo=map.get("MonthNo").toString();
			String yearNo=map.get("yearNo").toString();
			String GRN_NO=map.get("GRN_NO").toString();
			
			if("17503".equals(indentEntryId)){
				System.out.println(map);
			}
			
			String[] strArrayGRNNo=GRN_NO.split("/");
			String formattedGRNNO=strArrayGRNNo[0].trim()+"/"+strArrayGRNNo[1];
			int monthNo=Integer.valueOf(MonthNo);
			int year=Integer.valueOf(yearNo);
			String grnNo="";
			//checking month number 
			if(monthNo<=3){
				System.out.println("The current Month is less than the april");
				grnNo+=(year-1)+"-"+year;
			}else if(monthNo>=4){
				System.out.println("The current Month is greater than the april");
				grnNo+=(year)+"-"+(year+1);
			}
			formattedGRNNO=formattedGRNNO+"/"+grnNo;
			String updateGRN_NOQuery="update INDENT_ENTRY set GRN_NO='"+formattedGRNNO+"' where INDENT_ENTRY_ID='"+indentEntryId+"'";
			if(GRN_NO.length()!=0)
			result=	jdbcTemplate.update(updateGRN_NOQuery);
		}
	*/}

	public void updateChildProductName() {
		 String contractor_id="";
		 String contractor_name="";
		 String first_name="";
		 String last_name="";
		 String address="";
		 String pan_number="";
		 String mobile_number="";
		 String alternate_mobile_number="";
		 String bank_acc_number="";
		 String bank_name="";
		 String ifsc_code="";
		 String email="";
		 String GSTIN="";

		String FilePath = "";
		FilePath = "";
		FileInputStream fs = null;
		Workbook wb = null;
			try {
				FilePath = "D:\\CONTRACTOR.xls";//CONTRACTOR.xlsx
				FilePath ="D:/Document/cp_edit_in_db_sheet.xls";
				File file = new File("D:\\");
				 fs = new FileInputStream(FilePath);
				 wb = Workbook.getWorkbook(fs);
			} catch (Exception e) {
				e.printStackTrace();
			}

		// TO get the access to the sheet
		Sheet sh = wb.getSheet("Sheet");

		// To get the number of rows present in sheet
		int totalNoOfRows = sh.getRows();

		totalNoOfRows = totalNoOfRows-2;

		// To get the number of columns present in sheet
		int totalNoOfCols = sh.getColumns();

		for (int row = 3; row <=totalNoOfRows; row++) {

	/*		System.out.print(sh.getCell(0, row).getContents() + "\t");
			System.out.print(sh.getCell(5, row).getContents() + "\t");
			System.out.print(sh.getCell(6, row).getContents() + "\t");
			System.out.print(sh.getCell(9, row).getContents() + "\t\n");
*/			
			String updateIndentEntryChildProdName  ="update INDENT_ENTRY_DETAILS set CHILD_PRODUCT_NAME='"+sh.getCell(9, row).getContents().trim() +"' "
					+ " where CHILD_PRODUCT_ID='"+sh.getCell(5, row).getContents()+"' and  CHILD_PRODUCT_NAME='"+sh.getCell(6, row).getContents()+"' ";
			String updateChilsProdNames ="update CHILD_PRODUCT set NAME='"+sh.getCell(9, row).getContents().trim() +"' "
					+ " where CHILD_PRODUCT_ID='"+sh.getCell(5, row).getContents()+"' and  NAME='"+sh.getCell(6, row).getContents() +"'";
			if("CHP1857".equals(sh.getCell(5, row).getContents())){
				System.out.println();
			}
			
			int result = template.update(updateIndentEntryChildProdName);
			if(result==0){
				
			}
			
			int result1 = template.update(updateChilsProdNames);	
			if(result==0&&result1==0){
				System.out.println("INDENT_ENTRY_DETAILS "+updateIndentEntryChildProdName+": Result = "+ result);
				System.out.println("CHILD_PRODUCT "+updateChilsProdNames+" : Result = "+ result);
			}
			
		/*	ContractorBean bean=new ContractorBean();
			contractor_name = sh.getCell(1, row).getContents();
			int index=contractor_name.indexOf(" ");
			first_name=contractor_name.substring(0, index);
			last_name=contractor_name.substring(index, contractor_name.length());
			address = sh.getCell(2, row).getContents();
			pan_number = sh.getCell(3, row).getContents();
			mobile_number = sh.getCell(4, row).getContents();
			bank_acc_number = sh.getCell(5, row).getContents();
			bank_name = sh.getCell(6, row).getContents();
			ifsc_code = sh.getCell(7,row).getContents();*/
		

		}//for
	}
	/*@SuppressWarnings({ "null", "deprecation" })
	public  boolean marketingAvailableArea() throws NamingException {
		boolean status=false;
		List<Map<String, Object>> dbProductsList = null;
		try{
			String date = new SimpleDateFormat("MM-yyyy").format(new Date());
			String prodsQry = "select DISTINCT(MONTH_YEAR) AS MONTH  from MRKT_MONTHLY_AVAIL_AREA WHERE STATUS='A'";
			log.debug("Query to fetch product = "+prodsQry);

			dbProductsList = template.queryForList(prodsQry, new Object[]{});
			for (Map<String, Object> map : dbProductsList) {
				//System.out.println(map);
				String strMonth=map.get("MONTH")==null?"":map.get("MONTH").toString();
				if(date.equals(strMonth)){
					status=true;
					break;
				}
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return status;
		
	}*/
	@SuppressWarnings({ "null", "deprecation" })
	public  String marketingAvailableArea() throws NamingException {
		boolean status=false;
		List<Map<String, Object>> dbProductsList = null;
		Map<String, String> siteDetails = null;
		List<String> site=new ArrayList<String>();
		String siteNames="";
		boolean result=false;
		String resultData="";
		String allSites=validateParams.getProperty("MARKETING_AVILABLE_AREA_DISCARD_SITES") == null ? "" : validateParams.getProperty("MARKETING_AVILABLE_AREA_DISCARD_SITES").toString();
		try{
			siteDetails = new IndentSummaryDao().getSiteDetails();
			//request.setAttribute("site",siteDetails);

			String date = new SimpleDateFormat("MM-yyyy").format(new Date());
			String prodsQry = "select DISTINCT(MONTH_YEAR) AS MONTH,SITE_ID  from MRKT_MONTHLY_AVAIL_AREA";
			log.debug("Query to fetch product = "+prodsQry);

			dbProductsList = template.queryForList(prodsQry, new Object[]{});
			for (Map<String, Object> map : dbProductsList) {
				//System.out.println(map);
				String strMonth=map.get("MONTH")==null?"":map.get("MONTH").toString();
				String siteId=map.get("SITE_ID")==null?"":map.get("SITE_ID").toString();

				if(date.equals(strMonth)){
					site.add(siteId);
					status=true;
				}
			}
			if(status){
				if(siteDetails.size()!=site.size()){
					for(Map.Entry<String, String> entry:siteDetails.entrySet()){
						if(site.contains(entry.getKey())){

						}else if(!entry.getKey().equals("996") && !allSites.contains(entry.getKey())){
							siteNames+=entry.getValue()+", ";
						}
					}
					//					
				}
			}else{
				for(Map.Entry<String, String> entry:siteDetails.entrySet()){
					if(!entry.getKey().equals("996") && !allSites.contains(entry.getKey())){
					siteNames+=entry.getValue()+", ";}
				}

			}

			if(siteNames!=null && !siteNames.equals("")){
				siteNames=siteNames.substring(0, siteNames.length()-2);
				result=true;
			}
			resultData=result+"@@@"+siteNames;

		}catch(Exception e){
			e.printStackTrace();
		}
		return resultData;
		
	}
}