package com.sumadhura.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.sumadhura.bean.BOQBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.PaymentModesBean;
import com.sumadhura.dto.MultiObject;


public interface BOQService {

	public String saveBOQ(MultipartFile multipartFile, List<String> errMsg, String user_id, String boqSiteId, List<String> tempBOQNoForController) throws IOException;
	
	public List<BOQBean> getPendingForApprovalBOQList( HttpServletRequest request,HttpSession session, boolean isViewTempBoq);

	public List<BOQBean> getBOQForApprovalByID(HttpServletRequest request, HttpSession session);

	public List<BOQBean> getBOQData(HttpServletRequest request, HttpSession session);

	public BOQBean getBOQFromAndToDetails(BOQBean objBOQBean, String userId);

	public String approveTempBOQ(HttpServletRequest request, HttpSession session, BOQBean objForOnlyPermanentBoqNumber);

	public List<BOQBean> getBOQWorkDetails(HttpServletRequest request, HttpSession session, String typeOfWork);

	public String rejectTempBOQ(HttpServletRequest request, HttpSession session);

	public String getBOQLevelComments(HttpServletRequest request);
	
public List<BOQBean> getMyBOQList( HttpServletRequest request,HttpSession session);
	
	public List<BOQBean> getBOQWorkDetails(HttpServletRequest request, BOQBean objBOQDetails);
	
	public BOQBean getBOQDetails(HttpServletRequest request);
	
	public String createBlockIdAndFloorId(String user_id, String site_id);

	public List<BOQBean> getSiteWisePendingForApprovalBOQList(HttpServletRequest request, HttpSession session, String siteId, boolean isViewTempBoq);

	public String getSiteNameBySiteId(String siteId);

	public String saveNMR(MultipartFile multipartFile, List<String> errMsg, String user_id, String boqSiteId,
			List<String> tempBOQNoForController) throws IOException;

	public List<Map<String, Object>> getBOQMajorHeads(HttpServletRequest request, String typeOfWork);
	public List<BOQBean> getBOQMajorHeadsDetails(HttpServletRequest request, String typeOfWork);

	public List<BOQBean> getBOQMinorHeadsDetails(HttpServletRequest request, String typeOfWork);

	public List<BOQBean> getSitewiseBOQList(HttpServletRequest request, HttpSession session);

	public String reviseBOQ(MultipartFile multipartFile, List<String> errMsg, String user_id, String boqSiteId,
			List<String> tempBOQNoForController) throws IOException;

	public MultiObject getPreviousBOQTotal(String tempBOQNumber);

	public List<BOQBean> getReviseBOQWorkDetails(HttpServletRequest request, HttpSession session, String typeOfWork, String recordType, String majorHeadId);

	public List<BOQBean> getReviseBOQChangedWorkDetails(HttpServletRequest request, HttpSession session,
			String typeOfWork, String tempBOQNo, String recordType, String majorHeadId);

	public String approveReviseTempBOQ(HttpServletRequest request, HttpSession session, BOQBean objForOnlyPermanentBoqNumber, List<String> errMsg);

	public BOQBean getBOQDetailsFromBackup(HttpServletRequest request);

	public List<BOQBean> getBOQAllVersions(HttpServletRequest request, HttpSession session);

	public List<BOQBean> getSOWChangedWorkDetails(HttpServletRequest request, HttpSession session, String typeOfWork,
			String tempBOQNumber);

	public List<BOQBean> getReviseBOQChangedWorkDetailsBasedOnVerNo(HttpServletRequest request, HttpSession session,
			String typeOfWork);

	public List<BOQBean> viewAllBlocksFloorsFlats(String siteId);

	public List<Map<String, Object>> getTempBOQMajorHeads(HttpServletRequest request, String typeOfWork);

	public List<BOQBean> getTempBOQWorkDetails(HttpServletRequest request, BOQBean ObjBOQDetails);

	public List<BOQBean> getTempBOQMajorHeadsDetails(HttpServletRequest request, String typeOfWork);

	public List<BOQBean> getTempBOQMinorHeadsDetails(HttpServletRequest request, String typeOfWork);

	List<BOQBean> getBOQMaterialDetails(HttpServletRequest request, BOQBean ObjBOQDetails);

	List<BOQBean> getTempBOQMaterialDetails(HttpServletRequest request, BOQBean ObjBOQDetails);

	public boolean checkingIsItReallyPendindOnCurrentUser(String userId, String tempBOQNumber);

}
