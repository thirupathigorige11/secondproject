package com.sumadhura.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.PriceMasterDTO;

public interface ReportsService {

	public String getProdcutsDetail(String siteId);

	Set<PriceMasterDTO> getProductPriceListBySite(String strSiteId, String childProdName,String monthYear, String priceMasterType);

	Map<String, Collection<PriceMasterDTO>> getLastThreeMonthPriceMasterDetail(String childProductId, String prodName,
			String priceId, String site_id, String monthYear);

	public String getRequestedAmountReportBySite(String siteId, String tillDatePaymentReq);

	public List<Map<String, Object>> loadAllChildProducts(String prodName, String site_id, String type);

	public String purhaseTypesAndotalCost(String siteId, String tillDatePaymentReq);

	public String getChildProdcutsDetailByName(String siteId, String prodName);
	
	public String getTotalReceivedAndIssuedQuantity(String siteId,String strDate);

}
