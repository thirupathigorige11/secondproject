package com.sumadhura.transdao;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.jdbc.core.JdbcTemplate;

import com.sumadhura.bean.BOQBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;
import com.sumadhura.bean.VendorDetails;
import com.sumadhura.bean.ViewIndentIssueDetailsBean;
import com.sumadhura.bean.WDRateAnalysis;
import com.sumadhura.dto.BOQAreaMappingDto;
import com.sumadhura.dto.BOQDetailsDto;
import com.sumadhura.dto.BOQProductDetailsDto;
import com.sumadhura.dto.MultiObject;
import com.sumadhura.dto.PaymentDto;
import com.sumadhura.dto.ReviseBOQQtyChangedDtlsDto;

public interface BOQDao {

	public String getMajorHeadId(String majorHead, String typeOfWork);
	public String getMajorHeadTblSeqId();
	public int insertMajorHead(String majorHeadId, String majorHeadDesc, String user_id, String typeOfWork);
	public String getMinorHeadId(String minorHeadDesc, String majorHeadId, String typeOfWork);
	public String getMinorHeadTblSeqId();
	public int insertMinorHead(String minorHeadId, String minorHeadDesc, String user_id, String majorHeadId, String typeOfWork);
	public String getWorkDescriptionId(String workDescription, String minorHeadId, String typeOfWork);
	public String getWorkDescriptionTblSeqId();
	public int insertWorkDescription(String workDescription, String workDescriptionId, String minorHeadId,String user_id, String levelOfWork, String typeOfWork);
	public String getmeasurementId(String measurementName, String workDescriptionId, String typeOfWork);
	public String getMeasurementTblSeqId();
	public int insertMeasurement(String measurementId, String measurementName, String workDescriptionId,String user_id, String typeOfWork);
	public int getTempBOQNo();
	public int insertQsTempBOQ(int tempBOQNo, String user_id, String site_id, String pendingEmpId, String typeOfWork, double nmrGrandTotal, String string);
	public int getTempBOQDetailsId();
	public int insertQsTempBOQDetails(int tempBOQDetailsId, String workDescriptionId, String measurementId,double totalAreaOfAllBlocks, double totalAmountForAllBlocks, int tempBOQNo, String scopeOfWork, String minorHeadId, String action, String recordType);
	public String getBlockId(String blockName, String site_id);
	public String getWorkAreaSeqId();
	public int insertQsTempBOQAreaMapping(String workAreaId, String workDescriptionId, String measurementId,String blockId, double blockArea, int tempBOQDetailsId, double blockAreaAmount, String floorId, double laborRatePerUnit, int tempBOQNo, String Action, String recordType, String workAreaGroupId);
	public String getFloorId(String floorName, String blockId);
	public int getTempBOQDetailsId(String workDescriptionId, int tempBOQNo, String measurementId, String recordType);
	public int updateQsTempBOQDetails(int tempBOQDetailsId, double totalAreaOfAllBlocks,double totalAmountForAllBlocks);
	public String getPendingEmpId(String user_id, String site_id);
	public int getBlockIdIfItIsThere(String blockName, String site_id);
	public int getMaxBlockIdInBlockTable();
	public int createBlockId(int blockId, String blockName, String site_id);
	public int getFloorIdIfItIsThere(String floorName, int blockId);
	public int getMaxFloorIdInFloorTable();
	public int createFloorId(int floorId, String floorName, int blockId);


	public List<BOQBean> getPendingForApprovalBOQList( String user_id,String siteId, boolean isViewTempBoq);
	public List<BOQBean> getBOQForApprovalByID(BOQBean bean);
	public List<BOQBean> getBOQData(BOQBean bean);

	public BOQBean getBOQFromAndToDetails(BOQBean objBOQBean, String userId);
	List<BOQBean> getBOQWorkDetails(String siteId, String BOQNumber, String typeOfWork);
	public int approveTempBOQ(BOQBean bean, BOQBean objForOnlyPermanentBoqNumber);
	public int approveRejectDeatilsForBOQ(BOQBean bean);
	public int rejectTempBOQ(BOQBean bean);
	public String getBOQLevelComments(BOQBean bean);
	
		public List<BOQBean> getMyBOQList( String user_id);
	public List<BOQBean> getBOQWorkDetails(int strBoqSeqNo,String strSiteId,BOQBean objBOQDetails, String majorHead, String minorHead, String workDescription);
	
	public BOQBean getBOQDetails(int strBOQSeqNo,String strSiteId);
	public List<BOQBean> getSiteWisePendingForApprovalBOQList(String user_id, String siteId, boolean isViewTempBoq);
	public String getSiteNameBySiteId(String siteId);
	public int updateGrandTotalInQsTempBOQ(int tempBOQNo, double givenGrandTotalForVerification, double givenLaborTotalForVerification, double givenMaterialTotalForVerification);
	public String getWorkDescriptionIdIndependentOnMinorHeadId(String workDescription, String minorHeadId, String typeOfWork);
	public List<Map<String, Object>> getBOQMajorHeads(int intBOQSeqNumber, String strSiteId, String typeOfWork);
	public List<BOQBean> getBOQMajorHeadsDetails(int intBOQSeqNumber, String strSiteId, String typeOfWork);
	public List<BOQBean> getBOQMinorHeadsDetails(int intBOQSeqNumber, String strSiteId, String typeOfWork, String majorHeadId);
	public int getTempBOQNoOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork);
	public int getBOQSeqNoOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork);
	public String getParticularBlockPrice(int bOQSeqNo, String workDescriptionId,String measurementId, String blockId, String floorId, String recordType);
	public String getParticularBlockArea(int bOQSeqNo, String workDescriptionId,String measurementId, String blockId, String floorId, String recordType);
	public String getWorkOrderIntiateArea(int bOQSeqNo, String workDescriptionId,String measurementId, String blockId, String floorId, String recordType);
	public String getVersionNoOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork);
	public int insertReviseBOQChangedDetails(int reviseBOQChangedDetailsId, int tempBOQNo, int bOQSeqNo, String workAreaId,String workDescriptionId,
			String measurementId, String floorId, String blockId, String boqSiteId, double presentArea,
			double blockArea, double presentPrice, double laborRatePerUnit, int presentVersionNo, int newVersionNo,
			String string, String recordType);
	public MultiObject getPreviousBOQTotal(String tempBOQNumber);
	public List<BOQBean> getReviseBOQWorkDetails(String siteId, String BOQNumber, String typeOfWork, String recordType, String majorHeadId);
	public List<BOQBean> getReviseBOQChangedWorkDetails(String siteId, String BOQNumber, String typeOfWork, String recordType, String majorHeadId);
	public int approveReviseTempBOQ(BOQBean bean, BOQBean objForOnlyPermanentBoqNumber);
	public String getBOQNoOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork);
	public MultiObject getTotalAmountOfCurrentTempBOQ(int tempBOQNo);
	public BOQBean getBOQAreaMappingOfParticularBlock(int BOQSeqNo, String workDescriptionId, String measurementId, String blockId,
			String floorId, String recordType);
	public int updateQsTempBOQDetailsWithAction(int tempBOQDetailsId, double totalAreaOfAllBlocks,double totalAmountForAllBlocks, String action);
	public int checkIsThisWorkIsAlreadyInPresentBoq(String workDescriptionId, String measurementId, String blockId,
			String floorId, String boqSiteId, int BOQSeqNo, String recordType);
	public boolean isAnyWorkOrderCreatingOrRevisingOnThisBoqNo(String bOQNumber);
	public boolean checkIsThisWorkIsDeletingNow(String workDescriptionId, String measurementId, String blockId,
			String floorId, String boqSiteId, int tempBOQNo, String recordType);
	public int insertQsTempBOQDetailsForSOW(int tempBOQDetailsId, String workDescriptionId, String measurementId,
			double totalAreaOfAllBlocks, double totalAmountForAllBlocks, int tempBOQNo, String scopeOfWork,
			String minorHeadId, String Action);
	public BOQBean getBOQDetailsFromBackup(int intBOQSeqNo, String siteId, String versionNo);
	public List<BOQBean> getBOQAllVersions(String siteId, String BOQSeqNo);
	public String deleteAllTempBoqRecordsInDBForThisSite(String boqSiteId, String typeOfWork);
	public List<BOQBean> getSOWChangedWorkDetails(String siteId, String BOQNumber, String typeOfWork);
	public String getDBActionOfTempBoqDetailsId(int tempBOQDetailsId);
	public int updateQsTempBOQDetailsWithSOWandAction(int tempBOQDetailsId, String scopeOfWork, String action1);
	public List<BOQBean> getReviseBOQChangedWorkDetailsBasedOnVerNo(String siteId, int BOQSeqNo, String versionNo,
			String typeOfWork);
	public List<BOQBean> viewAllBlocksFloorsFlats(String siteId);
	public List<Map<String, Object>> getTempBOQMajorHeads(int tempBOQNumber, String strSiteId, String typeOfWork);
	public List<BOQBean> getTempBOQWorkDetails(int tempBOQNo, String strSiteId, BOQBean objBOQDetails, String majorHead,
			String minorHead, String workDescription);
	public List<BOQBean> getTempBOQMajorHeadsDetails(int intBOQSeqNumber, String strSiteId, String typeOfWork);
	public List<BOQBean> getTempBOQMinorHeadsDetails(int tempBOQNo, String strSiteId, String typeOfWork, String majorHeadId);
	public boolean isBoqAlreadyPresent(String boqSiteId, String typeOfWork);
	public HashMap<String, String> getMajorHeadIdsMap(String typeOfWork);
	public HashMap<String, HashMap<String,String>> getMinorHeadIdsMap(String typeOfWork);
	public HashMap<String, HashMap<String, String>> getWorkDescriptionIdsMap(String typeOfWork);
	public HashMap<String, HashMap<String, String>> getMeasurementIdsMap(String typeOfWork);
	public HashMap<String, String> getBlockIdsMap(String boqSiteId);
	public HashMap<String, HashMap<String, String>> getFloorIdsMap(String boqSiteId);
	public HashMap<String, String> getMaterialGroupIdsMap();
	public HashMap<String, HashMap<String, String>> getMaterialGroupMeasurementIdsMap();
	public int[] insertQsTempBOQProductDtls(String workAreaId, double blockArea, WDRateAnalysis objWDRA, String boqSiteId, int tempBOQNo, String string);
	public String getWorkAreaGroupSeqId();
	public void dummy(int qS_TEMP_BOQ_PRODUCT_DTLS_ID, String wO_WORK_AREA_ID, String wO_WORK_AREA,
			String mATERIAL_GROUP_ID, String mATERIAL_GROUP_MEASUREMENT_ID, double pER_UNIT_QUANTITY,
			double pER_UNIT_AMOUNT, double tOTAL_QUANTITY, double tOTAL_AMOUNT, String sTATUS, String rEMARKS,
			String sITE_ID);
	public int insertQsTempBOQDetails(BOQDetailsDto objBOQDetailsDto);
	public int updateQsTempBOQDetails(BOQDetailsDto objBOQDetailsDto);
	public int insertQsTempBOQAreaMapping(BOQAreaMappingDto objBOQAreaMappingDto);
	public int getReviseBOQChangedDetailsId();
	public WDRateAnalysis getPresentMaterialDetails(String presentWorkAreaId);
	public int getReviseBOQQuantityChangedDetailsId();
	public int[] insertReviseBOQQuantityChangedDetails(List<ReviseBOQQtyChangedDtlsDto> reviseBOQQtyChangedDtls_Insert);
	public String getPresentWorkAreaId(int BOQSeqNo, String workDescriptionId, String measurementId, String blockId, String floorId, String recordType);
	public String getBOQTotalOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork);
	public WDRateAnalysis getWOMaterialDetails(String presentWorkAreaId);
	String getBOQLaborTotalOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork);
	String getBOQMaterialTotalOfCurrentActivePermanentBOQ(String boqSiteId, String typeOfWork);
	String deleteAllRejectedTempBoqRecordsInDB(int tempBOQNo);
	int getQsTempBOQProductDetailsId();
	public Map<String,Double> getWDtotals_inDB(int bOQSeqNo);
	public Map<String, Double> getMAJtotals_inDB(int bOQSeqNo);
	public int[] insertQsTempBOQProductDetails(List<BOQProductDetailsDto> qsTempBOQProductDtls_Insert);
	public Map<String, String> getWorkAreaGroupIdsMap(int bOQSeqNo);
	public MultiObject getMajorHeadWiseWDs(int bOQSeqNo);
	public List<BOQBean> getBOQMaterialDetails(int intBOQSeqNumber, String WorkAreaId, BOQBean objBOQDetails);
	public List<BOQBean> getTempBOQMaterialDetails(int intBOQSeqNumber, String workAreaId, BOQBean objBOQDetails);
	double getBOQTotalinBOQAreaMapping(int BOQSeqNo);
	double getBOQMaterialTotalinBOQProductDetails(int BOQSeqNo);
public void getWDdataInDB(String workDescriptionId, int bOQSeqNo, HashMap<String, BOQBean> workAreaNoChangesInPrevWD, List<String> workAreaGroupNoChangesInPrevWD);
public boolean checkingIsItReallyPendindOnCurrentUser(String userId, String tempBOQNumber);
public void finalValidationOfGrandTotalInEveryTable(String BOQNumber);
public List<Map<String, Object>> getTempBOQAreaMappingDataInOrderOfWD(int tempBOQNo);
public List<Map<String, Object>> getWorkLocationName(String recordType, String workDescriptionId, String blockId, String floorId);
	
	
}
