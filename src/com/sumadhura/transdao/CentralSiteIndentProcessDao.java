package com.sumadhura.transdao;

import java.util.List;
import java.util.Map;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.ProductDetails;
import com.sumadhura.dto.IndentCreationDto;

public interface CentralSiteIndentProcessDao {

	public int getSiteIdByName(String reqSiteName);

	public IndentCreationDto getCentralIndentProcessData(int indentCreationDetailsId);

	public int getPurchaseIndentProcessSequenceNumber();

	public int insertPurchaseIndentProcess(int indentProcessId, IndentCreationBean changedIndentDetails,
			int IndentCreationDetailsId, int indentReqSiteId, String reqReceiveFrom,IndentCreationDto centralTableData);

	public IndentCreationBean getIndentFromAndToDetails(String user_id, IndentCreationBean icb);

	public List<IndentCreationBean> getPurchaseIndentDetailsLists(int indentNumber, String strSiteId);

	public List<IndentCreationBean> getCentralIndentDetailsLists(int indentNumber, String siteId);

	public int getCentralIndentRequestDetailsSequenceNumber();

	public int getIndentProcessIdCount(int indentProcessId, int senderSite);

	public int updateRequestToOtherSite(IndentCreationBean indentCreationBean, int centralIndentReqDetailsSeqNum,
			int senderSite);

	public int requestToOtherSite(IndentCreationBean indentCreationBean, int centralIndentReqDetailsSeqNum, int senderSite);

	public int updateInitiatedQuantityInCentralTable(String aQuantity, int indentProcessId);

	public IndentCreationBean getCreateAndRequiredDates(int indentNumber);

	public List<IndentCreationBean> getIndentFromAndToDetails(String centralDeptId);

	public List<IndentCreationBean> getSpecificSiteIndentFromAndToDetails(String centralDeptId, String strSiteId);

	public List<ProductDetails> getPurchaseIndentDtlsLists(String indentCreationDetailsIdForenquiry,String strVendorId);

	public String getAddressOfSite(int site_id);

	public String getSiteNameWhereIndentCreated(int indentNumber);

	public boolean validateSiteQuantity(IndentCreationBean indentCreationBean, int indentProcessId, int senderSite);

	public List<Map<String, Object>> getIndentCreatedEmpName(int reqSiteId, int indentNumber);

}
