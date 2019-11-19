package com.sumadhura.transdao;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.PriceMasterDTO;

public interface ReportsDao {

	String getAllProductsDetail(String siteId);

	Set<PriceMasterDTO> getProductPriceListBySite(String siteId, String childProdName,String  monthYear, String priceMasterType);

	Set<PriceMasterDTO> getLastThreeMonthPriceMasterDetail(String childProductId, String prodName, String priceId,
			String site_id, String monthYear);

	String getRequestedAmountReportBySite(String siteId, String tillDatePaymentReq);
	
	List<Map<String, Object>> loadAllChildProducts(String prodName,String site_id,String type);

	String purhaseTypesAndotalCost(String siteId, String tillDatePaymentReq);

	String getChildProdcutsDetailByName(String siteId, String prodName);
	
	public String getTotalReceivedAndIssuedQuantity(String siteId,String strDate);

	List<Map<String, Object>> getLastMonthsNamesForPriceMaster(String childProductId, String prodName, String priceId, String site_id, String monthYear);

	
	
}
