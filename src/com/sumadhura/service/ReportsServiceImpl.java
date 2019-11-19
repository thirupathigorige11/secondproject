package com.sumadhura.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import com.sumadhura.dto.PriceMasterDTO;
import com.sumadhura.transdao.ReportsDao;


/**
 * 
 * @author Aniket Chavan
 * @since 5/31/2018 4:24
 * @category Generatring Reports
 *
 */
@Service(value = "guiReportService")

public class ReportsServiceImpl implements ReportsService {

	@Autowired
	PlatformTransactionManager transactionManager;

	@Autowired
	@Qualifier("guireportdao")
	ReportsDao dao;

	@Override
	public String getProdcutsDetail(String siteId) {
		String allProductData = dao.getAllProductsDetail(siteId);
		return allProductData;
	}
	
	@Override
	public String getChildProdcutsDetailByName(String siteId, String prodName) {
		String childProdData = dao.getChildProdcutsDetailByName(siteId,prodName);
		return childProdData;
	}

	@Override
	public Set<PriceMasterDTO> getProductPriceListBySite(String siteId, String childProdName,String monthYear, String priceMasterType) {
		Set<PriceMasterDTO> latestChildProdPriceData = dao.getProductPriceListBySite(siteId.trim(),childProdName,monthYear,priceMasterType);
		return latestChildProdPriceData;
	}

	@Override
	/**
	 * @author Aniket
	 * @since 07/06/2018 7:00
	 */
	public Map<String, Collection<PriceMasterDTO>> getLastThreeMonthPriceMasterDetail(String childProductId,
			String prodName, String priceId, String site_id, String monthYear) {
	
		List<PriceMasterDTO> firstMonth = new ArrayList<PriceMasterDTO>();
		List<PriceMasterDTO> secondMonth = new ArrayList<PriceMasterDTO>();
		List<PriceMasterDTO> thirdMonth = new ArrayList<PriceMasterDTO>();

		
		String[] strMonthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
		Map<String, Collection<PriceMasterDTO>> monthWiseData = new HashMap<String, Collection<PriceMasterDTO>>();
		String strMonthYear=monthYear.split("-")[0];
		int countLoadedDataMonths=0;
	
		int count=12;
		
		
		for (int i = 0; i < count; i++,countLoadedDataMonths++) {
			
			Calendar cal = Calendar.getInstance();
			if(monthYear.length()!=0){
				SimpleDateFormat dateFormat=new SimpleDateFormat("MM-yyyy");
				try {
					Date date=dateFormat.parse(monthYear);
					cal.setTime(date);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			
			cal.add(Calendar.MONTH, -countLoadedDataMonths);
			Date result = cal.getTime();
			SimpleDateFormat dateFormat=new SimpleDateFormat("MM-yyyy");
			String date=dateFormat.format(result);
			System.out.println(date);
			//if(monthYear.length()==0){
				strMonthYear=date.split("-")[0];
				//loading last months data
				Set<PriceMasterDTO> threeMonthData = dao.getLastThreeMonthPriceMasterDetail(childProductId, prodName, priceId,site_id,date);
				if(i==11){
					count=18;
				}else if(i==17){
					monthWiseData.put("firstMonth", firstMonth);
					monthWiseData.put("secondMonth", secondMonth);
					monthWiseData.put("thirdMonth", thirdMonth);
					return monthWiseData;
				}
				if(firstMonth.size()==0&&threeMonthData.size()!=0){
					firstMonth.addAll(threeMonthData);
				}else if(secondMonth.size()==0&&threeMonthData.size()!=0){
					secondMonth.addAll(threeMonthData);
				}else if(thirdMonth.size()==0&&threeMonthData.size()!=0){
					thirdMonth.addAll(threeMonthData);
					monthWiseData.put("firstMonth", firstMonth);
					monthWiseData.put("secondMonth", secondMonth);
					monthWiseData.put("thirdMonth", thirdMonth);
					return monthWiseData;
				}else {
				
				}
		//	}
		}
		
		
		
		Set<PriceMasterDTO> threeMonthData = dao.getLastThreeMonthPriceMasterDetail(childProductId, prodName, priceId,site_id,monthYear);
		
		
		//loading last months data
	//	Set<PriceMasterDTO> threeMonthData = dao.getLastThreeMonthPriceMasterDetail(childProductId, prodName, priceId,site_id,monthYear);
		//loading months names
		List<Map<String, Object>> monthNames= dao.getLastMonthsNamesForPriceMaster(childProductId, prodName, priceId,site_id,monthYear);
	
		List<String> monthNumbers=new ArrayList<String>();
		//avoiding duplicate entry in list object
		for (Map<String, Object> map : monthNames) {
			String createdDate=	map.get("CREATEDDATE").toString();
			String monthNumber=map.get("MONTHNUMBER").toString();
			String YEARNUMBER=map.get("YEARNUMBER").toString();
			if(!monthNumbers.contains(createdDate+"@@"+monthNumber+"@@"+YEARNUMBER)){
				monthNumbers.add(createdDate+"@@"+monthNumber+"@@"+YEARNUMBER);
			}
		}
		

	/*	Calendar now = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("MMMyy");*/
	
		String currentMonthDateAndYear = "";
		String currentMonthDate = "";
		
		String previousMonthDateAndYear ="";
		String previousMonthDate = "";
		
		String twoMonthBeforeDateYear ="";
		String twoMonthBeforeDate ="";
		String temp[];
		if(monthNumbers.size()>=1){
			temp=monthNumbers.get(monthNumbers.size()-1).split("@@");
			currentMonthDateAndYear = temp[0];
			//currentMonthDate = currentMonthDateAndYear.substring(0, 3);
			currentMonthDate = strMonthNames[Integer.valueOf(temp[1])-1]+" - "+temp[2];
		}
		if(monthNumbers.size()>=2){
			temp=monthNumbers.get(monthNumbers.size()-2).split("@@");
			previousMonthDateAndYear =temp[0];
			previousMonthDate = strMonthNames[Integer.valueOf(temp[1])-1]+" - "+temp[2];
		}
		if(monthNumbers.size()>=3){
			temp=monthNumbers.get(monthNumbers.size()-3).split("@@");
			twoMonthBeforeDateYear =temp[0];
			twoMonthBeforeDate =strMonthNames[Integer.valueOf(temp[1])-1]+" - "+temp[2];
		}
		//extracting one collection object into three object's
		monthWiseData = extractMonthData(threeMonthData, site_id, twoMonthBeforeDateYear, twoMonthBeforeDate,
				previousMonthDateAndYear, previousMonthDate, currentMonthDateAndYear, currentMonthDate);
		//Retrieving all the months data and checking the size of it
		firstMonth = (List<PriceMasterDTO>) monthWiseData.get("firstMonth");
		secondMonth = (List<PriceMasterDTO>) monthWiseData.get("secondMonth");
		thirdMonth = (List<PriceMasterDTO>) monthWiseData.get("thirdMonth");
		if (firstMonth.size() != 0 || secondMonth.size() != 0 || thirdMonth.size() != 0){
			return monthWiseData;
		}
		
		//IMP Code
		//if the last three month data is not exists select the again last three months data
		if (firstMonth.size() == 0 && secondMonth.size() == 0 && thirdMonth.size() == 0) {
			System.out.println("last three months data is empty... "+childProductId+ " "+ priceId+" "+ site_id+" "+ monthYear);
			threeMonthData = dao.getLastThreeMonthPriceMasterDetail(childProductId, "", priceId, site_id,monthYear);
			monthNames= dao.getLastMonthsNamesForPriceMaster(childProductId, "", priceId,site_id,monthYear);
			//monthWiseData= getLastThreeMonthPriceMasterDetail(childProductId, "", priceId, site_id, monthYear);
			
			 monthNumbers=new ArrayList<String>();
			for (Map<String, Object> map : monthNames) {
				String createdDate=	map.get("CREATEDDATE").toString();
				String monthNumber=map.get("MONTHNUMBER").toString();
				String YEARNUMBER=map.get("YEARNUMBER").toString();
				if(!monthNumbers.contains(createdDate+"@@"+monthNumber+"@@"+YEARNUMBER)){
					monthNumbers.add(createdDate+"@@"+monthNumber+"@@"+YEARNUMBER);
				}
			}
			 
			
			if(monthNumbers.size()>=1){
				temp=monthNumbers.get(monthNumbers.size()-1).split("@@");
				currentMonthDateAndYear = temp[0];
				//currentMonthDate = currentMonthDateAndYear.substring(0, 3);
				currentMonthDate = strMonthNames[Integer.valueOf(temp[1])-1]+" - "+temp[2];
			}
			if(monthNumbers.size()>=2){
				temp=monthNumbers.get(monthNumbers.size()-2).split("@@");
				previousMonthDateAndYear =temp[0];
				previousMonthDate = strMonthNames[Integer.valueOf(temp[1])-1]+" - "+temp[2];
			}
			if(monthNumbers.size()>=3){
				temp=monthNumbers.get(monthNumbers.size()-3).split("@@");
				twoMonthBeforeDateYear =temp[0];
				twoMonthBeforeDate =strMonthNames[Integer.valueOf(temp[1])-1]+" - "+temp[2];
			}
			monthWiseData = extractMonthData(threeMonthData, site_id, twoMonthBeforeDateYear, twoMonthBeforeDate,
					previousMonthDateAndYear, previousMonthDate, currentMonthDateAndYear, currentMonthDate);
			return monthWiseData;
		}

		return monthWiseData;
	}

	private Map<String, Collection<PriceMasterDTO>> extractMonthData(Set<PriceMasterDTO> allData, String site_id,
			String twoMonthBeforeDateYear, String twoMonthBeforeDate, String previousMonthDateAndYear,
			String previousMonthDate, String currentMonthDateAndYear, String currentMonthDate) {

		Map<String, Collection<PriceMasterDTO>> monthWiseData = new HashMap<String, Collection<PriceMasterDTO>>();

		List<PriceMasterDTO> firstMonth = new ArrayList<PriceMasterDTO>();
		List<PriceMasterDTO> secondMonth = new ArrayList<PriceMasterDTO>();
		List<PriceMasterDTO> thirdMonth = new ArrayList<PriceMasterDTO>();
		for (PriceMasterDTO object : allData) {
		
			if (twoMonthBeforeDateYear.equals(object.getCreated_date())) {
			
				thirdMonth.add(new PriceMasterDTO(object.getPrice_id() == null ? "" : object.getPrice_id().toString(),
						object.getChild_product_id() == null ? "" : object.getChild_product_id().toString(),
						object.getChild_product_name() == null ? "" : object.getChild_product_name().toString(),
						object.getMeasurement_name() == null ? "" : object.getMeasurement_name().toString(),
						object.getAmount_per_unit_before_taxes() == null ? ""
								: object.getAmount_per_unit_before_taxes().toString(),
						object.getAmount_per_unit_after_taxes() == null ? ""
								: object.getAmount_per_unit_after_taxes().toString(),
						object.getAvailable_quantity() == null ? "" : object.getAvailable_quantity().toString(),
						object.getBasic_amount() == null ? "" : object.getBasic_amount().toString(),
						object.getTotal_amount() == null ? "" : object.getTotal_amount().toString(),
						object.getAmount_after_tax() == null ? "" : object.getAmount_after_tax().toString(),
						object.getCreated_date() == null ? "" : object.getCreated_date().toString(),
						site_id == null ? "" : site_id.toString(),
						twoMonthBeforeDate == null ? "" : twoMonthBeforeDate.toString(),null,null,
						object.getInvoice_number()==null?"":object.getInvoice_number(),
						object.getDc_number()==null?"":object.getDc_number(),
						object.getVendor_id()==null?"":object.getVendor_id(),
						object.getVendor_name()==null?"":object.getVendor_name(),
						object.getIndent_entry_id()==null?"":object.getIndent_entry_id(),
						object.getDc_entry_id()==null?"":object.getDc_entry_id(),
						object.getIndent_recive_date()==null?"":object.getIndent_recive_date(),
						object.getDc_recive_date()==null?"":object.getDc_recive_date()));
			}
			if (previousMonthDateAndYear.equals(object.getCreated_date())) {

				secondMonth.add(new PriceMasterDTO(object.getPrice_id() == null ? "" : object.getPrice_id().toString(),
						object.getChild_product_id() == null ? "" : object.getChild_product_id().toString(),
						object.getChild_product_name() == null ? "" : object.getChild_product_name().toString(),
						object.getMeasurement_name() == null ? "" : object.getMeasurement_name().toString(),
						object.getAmount_per_unit_before_taxes() == null ? ""
								: object.getAmount_per_unit_before_taxes().toString(),
						object.getAmount_per_unit_after_taxes() == null ? ""
								: object.getAmount_per_unit_after_taxes().toString(),
						object.getAvailable_quantity() == null ? "" : object.getAvailable_quantity().toString(),
						object.getBasic_amount() == null ? "" : object.getBasic_amount().toString(),
						object.getTotal_amount() == null ? "" : object.getTotal_amount().toString(),
						object.getAmount_after_tax() == null ? "" : object.getAmount_after_tax().toString(),
						object.getCreated_date() == null ? "" : object.getCreated_date().toString(),
						site_id == null ? "" : site_id.toString(),
						previousMonthDate == null ? "" : previousMonthDate.toString(),null,null,
						object.getInvoice_number()==null?"":object.getInvoice_number(),
						object.getDc_number()==null?"":object.getDc_number(),
						object.getVendor_id()==null?"":object.getVendor_id(),
						object.getVendor_name()==null?"":object.getVendor_name(),
						object.getIndent_entry_id()==null?"":object.getIndent_entry_id(),
						object.getDc_entry_id()==null?"":object.getDc_entry_id(),
						object.getIndent_recive_date()==null?"":object.getIndent_recive_date(),
						object.getDc_recive_date()==null?"":object.getDc_recive_date()));
			}
			if (currentMonthDateAndYear.equals(object.getCreated_date())) {

				firstMonth.add(new PriceMasterDTO(object.getPrice_id() == null ? "" : object.getPrice_id().toString(),
						object.getChild_product_id() == null ? "" : object.getChild_product_id().toString(),
						object.getChild_product_name() == null ? "" : object.getChild_product_name().toString(),
						object.getMeasurement_name() == null ? "" : object.getMeasurement_name().toString(),
						object.getAmount_per_unit_before_taxes() == null ? ""
								: object.getAmount_per_unit_before_taxes().toString(),
						object.getAmount_per_unit_after_taxes() == null ? ""
								: object.getAmount_per_unit_after_taxes().toString(),
						object.getAvailable_quantity() == null ? "" : object.getAvailable_quantity().toString(),
						object.getBasic_amount() == null ? "" : object.getBasic_amount().toString(),
						object.getTotal_amount() == null ? "" : object.getTotal_amount().toString(),
						object.getAmount_after_tax() == null ? "" : object.getAmount_after_tax().toString(),
						object.getCreated_date() == null ? "" : object.getCreated_date().toString(),
						site_id == null ? "" : site_id.toString(),
						currentMonthDate == null ? "" : currentMonthDate.toString(),null,null,
						object.getInvoice_number()==null?"":object.getInvoice_number(),
						object.getDc_number()==null?"":object.getDc_number(),
						object.getVendor_id()==null?"":object.getVendor_id(),
						object.getVendor_name()==null?"":object.getVendor_name(),
						object.getIndent_entry_id()==null?"":object.getIndent_entry_id(),
						object.getDc_entry_id()==null?"":object.getDc_entry_id(),
						object.getIndent_recive_date()==null?"":object.getIndent_recive_date(),
						object.getDc_recive_date()==null?"":object.getDc_recive_date()));
			}
		}
		monthWiseData.put("firstMonth", firstMonth);
		monthWiseData.put("secondMonth", secondMonth);
		monthWiseData.put("thirdMonth", thirdMonth);

		//System.out.println("firstMonth " + firstMonth.size());
		//System.out.println(("secondMonth " + secondMonth.size()));
		//System.out.println(("thirdMonth " + thirdMonth.size()));

		return monthWiseData;
	}

	@Override
	public String getRequestedAmountReportBySite(String siteId, String tillDatePaymentReq) {
		return dao.getRequestedAmountReportBySite(siteId,tillDatePaymentReq);
	}

	@Override
	public List<Map<String, Object>> loadAllChildProducts(String prodName, String site_id, String type) {
		return dao.loadAllChildProducts(prodName,site_id,type);
	}
	
	@Override
	public String purhaseTypesAndotalCost(String siteId, String tillDatePaymentReq) {
		return dao.purhaseTypesAndotalCost( siteId,  tillDatePaymentReq);
	}
	
	@Override
	public String getTotalReceivedAndIssuedQuantity(String siteId,String strDate) {
		return dao.getTotalReceivedAndIssuedQuantity( siteId,  strDate);
	}
	
}
