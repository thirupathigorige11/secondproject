package com.sumadhura.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.ui.Model;

import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.dto.IndentCreationDetailsDto;
import com.sumadhura.dto.IndentCreationDto;
import com.sumadhura.transdao.IndentCreationDao;
import com.sumadhura.util.CommonUtilities;
import com.sumadhura.util.UIProperties;

public class IndentCreationApprovalEmailFunction extends UIProperties {

	@Autowired
	private IndentCreationDao icd;
	private JdbcTemplate jdbcTemplate;

	public IndentCreationApprovalEmailFunction(JdbcTemplate template) {

		this.jdbcTemplate = template;
	}

	public String approveIndentCreationFromMail(HttpServletRequest request,
			int site_id, String user_id, String userName) {
		String response = "";
		boolean isSendMail = true;
		String strIndentTo = "";
		String strIndentFrom = "";
		int reqSiteId = 0;

		// int indentNumber = 0;
		String pendingEmpId = "";
		String strPurpose = "";
		final List<IndentCreationDetailsDto> listProductDetails = new ArrayList<IndentCreationDetailsDto>();
		final List<IndentCreationBean> editList = new ArrayList<IndentCreationBean>();
		final IndentCreationDto indentCreationDtoForMail = new IndentCreationDto();
		int indentNumber = 0;
		int siteWiseIndentNo = 0;
		int portNo = 0;
		String centralstrpendingEmpId = "";
		String centralpendingEmployeeId = "";
		String indentname="";
		Map<String,String> groupIdsSortMap = new HashMap<String,String>();
		Map<String,String> childSortMap = new HashMap<String,String>();
		Map<String,String> childProductMap = new HashMap<String,String>();;
		String childProductList="";
		String measurementList="";
		double receivedQuantity=0.0;
		double issuedQuantity=0.0;
		double indentPendingQuantity=0.0;
		double totalBOQQuantity=0.0;
		double tempQuantity = 0.0;
		double totalQuantity=0.0;
		try {
			indentNumber = Integer.parseInt(request
					.getParameter("indentNumber"));
			siteWiseIndentNo = getSiteWiseIndentNo(indentNumber);
			System.out.println(request.getParameter("tempPass") + "-"
					+ getTempPasswordOfIndent(indentNumber));
			if (request.getParameter("tempPass").equals(
					getTempPasswordOfIndent(indentNumber))) {
			} else {

				return "WrongPassword";
			}

			indentname=request.getParameter("indentname");
			strIndentFrom = getIndentFrom(indentNumber);
			strIndentTo = getIndentTo(user_id);
			List<IndentCreationBean> indentDetails = getIndentCreationLists(indentNumber);
			List<IndentCreationBean> productslist = getIndentCreationDetailsLists(indentNumber);
			IndentCreationDto indentCreationDto = new IndentCreationDto();
			reqSiteId = indentDetails.get(0).getSiteId();

			String strEditComments = indentDetails.get(0).getMaterialEditComment();

			if (strEditComments.contains("@@@")) {
				String strEditCommentsArr[] = strEditComments.split("@@@");
				for (int j = 0; j < strEditCommentsArr.length; j++) {
					IndentCreationBean objCommentIndentCreationBean = new IndentCreationBean();
					objCommentIndentCreationBean
							.setMaterialEditComment(strEditCommentsArr[j]);
					editList.add(objCommentIndentCreationBean);
				}


			}


			indentCreationDto.setSiteId(reqSiteId);//reqSiteId
			indentCreationDtoForMail.setSiteId(reqSiteId);
			indentCreationDto.setUserId(user_id);
			String comment=request.getParameter("comment");
			indentCreationDto.setPurpose(comment);//purpose
			String temppass = CommonUtilities.getStan();
			indentCreationDto.setTempPass(temppass);
			indentCreationDtoForMail.setTempPass(temppass);
			int indentCreationApprovalSeqNum = getIndentCreationApprovalSequenceNumber();

			portNo=request.getLocalPort();
			int response1 = insertIndentCreationApprovalAsApprove(indentCreationApprovalSeqNum, indentNumber, indentCreationDto);
			pendingEmpId = getPendingEmployeeId(user_id,reqSiteId);
			//pendingEmpId = user_id;
			indentCreationDtoForMail.setPendingEmpId(pendingEmpId);
			String pendingDeptId = "-";
			if(pendingEmpId.equals("-"))
			{
				pendingDeptId = getPendingDeptId(user_id,reqSiteId);
			}
			/*if (site_id == 999) {
				centralstrpendingEmpId = getPendingEmployeeId(user_id, site_id);// this
																				// is
																				// for
																				// central
																				// indent
				centralpendingEmployeeId = getPendingEmployeeId(
						centralstrpendingEmpId, site_id);
			}
*/
			int noofrows = Integer.parseInt(request.getParameter("noofRowsTobeProcessed"));

			String centralDeptId = validateParams
					.getProperty("CENTRAL_DEPT_ID") == null ? ""
					: validateParams.getProperty("CENTRAL_DEPT_ID").toString();
			String purchaseDeptId = validateParams
					.getProperty("PURCHASE_DEPT_ID") == null ? ""
					: validateParams.getProperty("PURCHASE_DEPT_ID").toString();
			/*=================================check the quantity for material BOQ purpose start============================================*/
			for (int num = 1; num <= noofrows; num++) {
				//num = num.trim();
				//String actionValue = request.getParameter("actionValue"+num)==null ? "" : request.getParameter("actionValue"+num);
				//String childProduct=request.getParameter("ChildProduct"+num)==null ? request.getParameter("ChildProductId"+num)==null ? "" : request.getParameter("ChildProductId"+num) : request.getParameter("ChildProduct"+num);
				//measurement=request.getParameter("UnitsOfMeasurement"+num)==null ? "" : request.getParameter("UnitsOfMeasurement"+num);
				
				String childProdId = productslist.get(num - 1).getChildProductId1();
				String childProdName= productslist.get(num - 1).getChildProduct1();
				String measurementId=productslist.get(num - 1).getUnitsOfMeasurementId1();
				String measurementName=productslist.get(num - 1).getUnitsOfMeasurement1();
				
				String groupId=productslist.get(num - 1).getGroupId1();
				//double enteredQuantity=Double.valueOf(productslist.get(num - 1).getRequiredQuantity1());
				 //}
				if(!groupId.equals("0") && groupId!=null){
					//			/childProductList
					if(!childProductMap.containsKey(childProdId)){
						childSortMap=getChildProductsWithGroupId(groupId);
						for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
							childProductList += entry.getKey() +",";
							measurementList += entry.getValue() +",";
						}
						
						if(childProductList!=null && !childProductList.equals("")){
							childProductList =  childProductList.substring(0,childProductList.length()-1);
						}if(measurementList!=null && !measurementList.equals("")){
							measurementList =  measurementList.substring(0,measurementList.length()-1);
						}
							receivedQuantity=getindentAndDcReceivedQuantity(childProductList,String.valueOf(reqSiteId),measurementList);
							issuedQuantity=getIssuedQuantity(childProductList,String.valueOf(reqSiteId),measurementList);
							indentPendingQuantity=getIndentPendingQuantity(childProductList,String.valueOf(reqSiteId),measurementList);
							totalBOQQuantity=getBOQQuantity(groupId,String.valueOf(reqSiteId));
							for ( Map.Entry<String,String> entry : childSortMap.entrySet()) {
								childProductMap.put(entry.getKey(),receivedQuantity+"_"+issuedQuantity+"_"+indentPendingQuantity+"_"+totalBOQQuantity);
							}
						}else{
							String temp_Quantity =(childProductMap.get(childProdId));
							String info[]=temp_Quantity.split("_");
							 receivedQuantity=Double.valueOf(info[0]);
							 issuedQuantity=Double.valueOf(info[1]);
							 indentPendingQuantity=Double.valueOf(info[2]);
							 totalBOQQuantity=Double.valueOf(info[3]);
						}
					/*if(!groupIdsSortMap.containsKey(groupId)){
						groupIdsSortMap.put(groupId,String.valueOf(enteredQuantity));
						tempQuantity=enteredQuantity; // curretly entered quantity
					}else{
						double DoublePrevQuantity = Double.valueOf(groupIdsSortMap.get(groupId));
						tempQuantity = Double.valueOf(enteredQuantity)+DoublePrevQuantity; // currently entered quantity
						groupIdsSortMap.put(groupId, String.valueOf(tempQuantity));
					}*/
					totalQuantity=receivedQuantity-(issuedQuantity)+indentPendingQuantity;//<=totalBOQQuantity;

					if(totalQuantity<=totalBOQQuantity){
						continue;
					}else{
						return "You can not initiate Child Product \""+childProdName+"\"more than "+totalBOQQuantity+" "+measurementName+". As it is exceeding BOQ Quantity.";
						//response="You can not initiate Child Product "+childProdName+"more than "+totalBOQQuantity+" "+measurementName+". As it is exceeding BOQ Quantity.";
						//break;
					}

				}
				
				
			}
			/*===================================check the quantity for BOQ PurpoSe End================================================*/
			for (int num = 1; num <= noofrows; num++) {
				IndentCreationDetailsDto indentCreationDetailsDto = new IndentCreationDetailsDto();
				indentCreationDetailsDto.setProdId(productslist.get(num-1).getProductId1());
				indentCreationDetailsDto.setProdName(productslist.get(num-1).getProduct1());
				indentCreationDetailsDto.setSubProdId(productslist.get(num-1).getSubProductId1());
				indentCreationDetailsDto.setSubProdName(productslist.get(num-1).getSubProduct1());
				indentCreationDetailsDto.setChildProdId(productslist.get(num-1).getChildProductId1());
				indentCreationDetailsDto.setChildProdName(productslist.get(num-1).getChildProduct1());
				indentCreationDetailsDto.setMeasurementId(productslist.get(num-1).getUnitsOfMeasurementId1());
				indentCreationDetailsDto.setMeasurementName(productslist.get(num-1).getUnitsOfMeasurement1());

				indentCreationDetailsDto.setRequiredQuantity(productslist.get(num-1).getRequiredQuantity1());
				indentCreationDetailsDto.setRemarks(productslist.get(num-1).getRemarks1());
indentCreationDetailsDto.setGroupId(productslist.get(num - 1)
						.getGroupId1());
				listProductDetails.add(indentCreationDetailsDto);




				strPurpose = getIndentLevelComments(indentNumber);
				request.setAttribute("indentLevelComments", "strPurpose");



				// To Give Indent to Central
				if(pendingEmpId.equals("-")&&pendingDeptId.equals(centralDeptId))
				{
					int centralIndentProcessId = getCentralIndentProcessSequenceNumber();
					insertCentralIndentProcess(centralIndentProcessId,productslist.get(num-1),productslist.get(num-1).getIndentCreationDetailsId(),reqSiteId);
				}else if(pendingEmpId.equals("-")&&pendingDeptId.equals(purchaseDeptId))
				{
					int indentProcessId = getCentralIndentProcessSequenceNumber();
					insertPurchaseIndentProcess(indentProcessId,productslist.get(num-1),productslist.get(num-1).getIndentCreationDetailsId(),reqSiteId,user_id);
				}
				if (site_id == 999 ) {

					/*
					 * IndentCreationBean changedIndentDetails = new
					 * IndentCreationBean();
					 * changedIndentDetails.setProductId1(prodId);
					 * changedIndentDetails.setSubProductId1(subProdId);
					 * changedIndentDetails.setChildProductId1(childProdId);
					 * changedIndentDetails
					 * .setUnitsOfMeasurementId1(measurementId);
					 * changedIndentDetails
					 * .setRequiredQuantity1(requiredQuantity);
					 */

					/*int centralIndentProcessId = getCentralIndentProcessSequenceNumber();
					insertCentralIndentProcess(centralIndentProcessId,
							productslist.get(num - 1), productslist
									.get(num - 1).getIndentCreationDetailsId(),
							site_id);*/
					// icd.updateIndentCreationForCentral(pendingEmployeeId,site_id,indentNumber);//for
					// central indent
					/*indentCreationDtoForMail.setPendingDeptId(String
							.valueOf(site_id));
					indentCreationDtoForMail.setPendingEmpId("-");
					pendingEmpId = "-";
					pendingDeptId = String.valueOf(site_id);*/
					// checkCentralIndentOrNot="true";

				}
					int response2 = updateIndentCreation(indentNumber, pendingEmpId, pendingDeptId, indentCreationDto,"approving from Mail");

			}

			System.out.println("Indent Approval Success via EMail");
			response = "Success";
		}
		catch(Exception e){

			System.out.println("Indent Approval Failed via EMail");
			response = "Failed";
			isSendMail = false;
			e.printStackTrace();
		}
		if(isSendMail){
			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{

				final String finalPendingEmpId = pendingEmpId;
				final String finalStrIndentFrom =strIndentFrom;
				final String finalStrIndentTo = strIndentTo;
				final String finalStrPurpose = strPurpose;
				final int indentNumber_final = indentNumber;
				final int siteWiseIndentNo_final = siteWiseIndentNo;
				final int portNo_final = portNo;
				final String strUserName = userName;
				final String strindentname=indentname;
				executorService.execute(new Runnable() {
					public void run() {



						//sendEmail( "", indentNumber, strIndentFrom, strIndentTo);

						IndentCreationServiceImpl objIndentCreationServiceImpl = new IndentCreationServiceImpl();
						sendEmailDetails(finalPendingEmpId, indentNumber_final,
								finalStrIndentFrom, finalStrIndentTo,
								listProductDetails, indentCreationDtoForMail,
								finalStrPurpose, editList,
								siteWiseIndentNo_final, portNo_final,
								strUserName,strindentname);

					}


				});




				executorService.shutdown();
			}catch(Exception e){
				e.printStackTrace();
				executorService.shutdown();
			}
		}
		/*if(isSendMail){


			//sendEmail( "", indentNumber, strIndentFrom, strIndentTo);
			sendEmailDetails( pendingEmpId, indentNumber, strIndentFrom, strIndentTo,listProductDetails, indentCreationDtoForMail,strPurpose,editList);
		}*/
		return response;
	}	



	public List<IndentCreationBean> getIndentCreationLists(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		String strPendingEmployeeName = "";
		String strPendingDeptName = "";


		String query = "SELECT SIC.SITEWISE_INDENT_NO,SIC.SCHEDULE_DATE ,SIC.REQUIRED_DATE, SIC.INDENT_CREATE_EMP_ID ,SIC.PENDING_EMP_ID, S.SITE_NAME,S.SITE_ID,"+
		"( SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=SIC.INDENT_CREATE_EMP_ID and INDENT_CREATION_ID = ?) as INDENT_CREATE_EMP_NAME, "+
		"( SELECT SED.EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS SED WHERE SED.EMP_ID=SIC.PENDING_EMP_ID and INDENT_CREATION_ID = ?) as PENDING_EMP_NAME,METERIAL_EDIT_COMMENT , "+
		"( SELECT SDD.DEPT_NAME FROM SUMADHURA_DEPARTMENT_DETAILS SDD WHERE SDD.DEPT_ID=SIC.PENDIND_DEPT_ID and INDENT_CREATION_ID = ?) as PENDING_DEPT_NAME "+
		" FROM SUMADHURA_INDENT_CREATION SIC, SITE S WHERE SIC.INDENT_CREATION_ID = ? AND S.SITE_ID = SIC.SITE_ID";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {
				indentNumber,indentNumber,indentNumber,indentNumber
		});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();
			indentCreationBean.setIndentNumber(indentNumber);
			String strScheduleDate = prods.get("SCHEDULE_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("SCHEDULE_DATE").toString();
			String strRequiredDate = prods.get("REQUIRED_DATE")==null ? "0000-00-00 00:00:00.000" : prods.get("REQUIRED_DATE").toString();
			Date scheduleDate = null;
			Date requiredDate = null;
			try {
				scheduleDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strScheduleDate);
				requiredDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(strRequiredDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			strScheduleDate = new SimpleDateFormat("dd-MMM-yy").format(scheduleDate);
			strRequiredDate = new SimpleDateFormat("dd-MMM-yy").format(requiredDate);
			indentCreationBean.setStrScheduleDate(strScheduleDate);
			indentCreationBean.setStrRequiredDate(strRequiredDate);
			indentCreationBean.setSiteWiseIndentNo(Integer.parseInt(prods.get("SITEWISE_INDENT_NO")==null ? "0" :   prods.get("SITEWISE_INDENT_NO").toString()));
			indentCreationBean.setIndentFrom(prods.get("INDENT_CREATE_EMP_NAME")==null ? "" :   prods.get("INDENT_CREATE_EMP_NAME").toString());

			indentCreationBean.setSiteName(prods.get("SITE_NAME")==null ? "" :   prods.get("SITE_NAME").toString());
			indentCreationBean.setSiteId(Integer.parseInt(prods.get("SITE_ID")==null ? "" :   prods.get("SITE_ID").toString()));
			indentCreationBean.setIndentNumber(indentNumber);
			indentCreationBean.setMaterialEditComment(prods.get("METERIAL_EDIT_COMMENT")==null ? "" :   prods.get("METERIAL_EDIT_COMMENT").toString());

			strPendingEmployeeName = prods.get("PENDING_EMP_NAME")==null ? "" :   prods.get("PENDING_EMP_NAME").toString();

			strPendingDeptName = prods.get("PENDING_DEPT_NAME")==null ? "" :   prods.get("PENDING_DEPT_NAME").toString();


			if(strPendingEmployeeName.equals("")){
				indentCreationBean.setIndentTo(strPendingDeptName);
			}else{
				indentCreationBean.setIndentTo(strPendingEmployeeName);
			}






			//indentCreationBean.setPurpose("PO");
			list.add(indentCreationBean);
		}
		List<Map<String, Object>> dbIndentDts2 = null;
		String query2 = "SELECT SED.EMP_NAME,SICAD.PURPOSE from  SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED "
			+ " where SICAD.INDENT_CREATION_ID = ? AND SED.EMP_ID=SICAD.INDENT_CREATE_APPROVE_EMP_ID";
		dbIndentDts2 = jdbcTemplate.queryForList(query2, new Object[] {
				indentNumber
		});
		String purposeView="";
		for(Map<String, Object> prods2 : dbIndentDts2) {
			String empName = prods2.get("EMP_NAME")==null ? "" : prods2.get("EMP_NAME").toString();
			String purpose = prods2.get("PURPOSE")==null ? "" : prods2.get("PURPOSE").toString();
			purposeView+=empName+" - "+purpose+"<br>";
		}
		list.get(0).setPurpose(purposeView);
		return list;
	}

	public int getSiteWiseIndentNo(int indentNumber) {
		String query = "select SITEWISE_INDENT_NO from SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = ? ";
		int result = jdbcTemplate.queryForInt(query, new Object[] {indentNumber});
		return result;
	}

	public String getIndentFrom(int indentNumber) {
		String indentFrom = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT  SED.EMP_ID,SED.EMP_NAME FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD,SUMADHURA_EMPLOYEE_DETAILS SED where SICAD.INDENT_CREATE_APPROVE_EMP_ID = SED.EMP_ID  AND SICAD.INT_CREATION_APPROVAL_DTLS_ID in"
			+ "(SELECT max(SICAD.INT_CREATION_APPROVAL_DTLS_ID) FROM SUM_INT_CREATION_APPROVAL_DTLS SICAD where SICAD.INDENT_CREATION_ID = ? GROUP BY SICAD.INDENT_CREATION_ID)";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {
			indentFrom = prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString();		
		}
		return indentFrom;
	}

	public String getIndentTo(String user_id) {
		String indentTo = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT  SED.EMP_ID,SED.EMP_NAME FROM SUMADHURA_APPROVER_MAPPING_DTL SAMD,SUMADHURA_EMPLOYEE_DETAILS SED where SAMD.EMP_ID = ? AND SAMD.APPROVER_EMP_ID = SED.EMP_ID AND MODULE_TYPE='INDENT'";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id});
		for(Map<String, Object> prods : dbIndentDts) {
			indentTo = prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString();		
		}
		return indentTo;
	}

	public List<IndentCreationBean> getIndentCreationDetailsLists(int indentNumber) {
		List<IndentCreationBean> list = new ArrayList<IndentCreationBean>();
		List<Map<String, Object>> dbIndentDts = null;
		int strSerialNumber = 0;
		String query = "SELECT P.NAME as PRODUCT_NAME,SP.NAME as SUB_PRODUCT_NAME,CP.NAME as CHILD_PRODUCT_NAME,MST.NAME as MEASUREMENT_NAME,"+
		"SICD.PRODUCT_ID,SICD.SUB_PRODUCT_ID,SICD.CHILD_PRODUCT_ID,SICD.MEASUREMENT_ID,"+
		"SICD.REQ_QUANTITY,SICD.REMARKS,SICD.INDENT_CREATION_DETAILS_ID FROM SUMADHURA_INDENT_CREATION_DTLS SICD, PRODUCT P,SUB_PRODUCT SP,CHILD_PRODUCT CP,MEASUREMENT MST "+
		"WHERE SICD.PRODUCT_ID=P.PRODUCT_ID AND SICD.SUB_PRODUCT_ID=SP.SUB_PRODUCT_ID AND SICD.CHILD_PRODUCT_ID=CP.CHILD_PRODUCT_ID "+
		"AND SICD.MEASUREMENT_ID=MST.MEASUREMENT_ID AND SICD.INDENT_CREATION_ID= ? ";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {indentNumber});
		for(Map<String, Object> prods : dbIndentDts) {
			IndentCreationBean indentCreationBean = new IndentCreationBean();

			indentCreationBean.setProductId1(prods.get("PRODUCT_ID")==null ? "" :   prods.get("PRODUCT_ID").toString());
			indentCreationBean.setSubProductId1(prods.get("SUB_PRODUCT_ID")==null ? "" :   prods.get("SUB_PRODUCT_ID").toString());
			indentCreationBean.setChildProductId1(prods.get("CHILD_PRODUCT_ID")==null ? "" :   prods.get("CHILD_PRODUCT_ID").toString());
			indentCreationBean.setUnitsOfMeasurementId1(prods.get("MEASUREMENT_ID")==null ? "" :   prods.get("MEASUREMENT_ID").toString());
			indentCreationBean.setProduct1(prods.get("PRODUCT_NAME")==null ? "" :   prods.get("PRODUCT_NAME").toString());
			indentCreationBean.setSubProduct1(prods.get("SUB_PRODUCT_NAME")==null ? "" :   prods.get("SUB_PRODUCT_NAME").toString());
			indentCreationBean.setChildProduct1(prods.get("CHILD_PRODUCT_NAME")==null ? "" :   prods.get("CHILD_PRODUCT_NAME").toString());
			indentCreationBean.setUnitsOfMeasurement1(prods.get("MEASUREMENT_NAME")==null ? "" :   prods.get("MEASUREMENT_NAME").toString());
			indentCreationBean.setRequiredQuantity1(prods.get("REQ_QUANTITY")==null ? "" :   prods.get("REQ_QUANTITY").toString());
			indentCreationBean.setRemarks1(prods.get("REMARKS")==null ? "" :   prods.get("REMARKS").toString());
			indentCreationBean.setIndentCreationDetailsId(Integer.parseInt(prods.get("INDENT_CREATION_DETAILS_ID")==null ? "" :   prods.get("INDENT_CREATION_DETAILS_ID").toString()));
indentCreationBean.setGroupId1(prods.get("MATERIAL_GROUP_ID") == null ? "0"
							: prods.get("MATERIAL_GROUP_ID").toString());
			strSerialNumber++;
			indentCreationBean.setStrSerialNumber(String.valueOf(strSerialNumber));
			list.add(indentCreationBean);
		}	
		return list;
	}

	public String getTempPasswordOfIndent(int indentNumber) {
		String query = "SELECT TEMPPASS FROM SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = "+indentNumber;
		String result = jdbcTemplate.queryForObject(query,String.class);  
		return result;

	}

	public int getIndentCreationApprovalSequenceNumber() {
		int indentCreationDetailsSeqNum = jdbcTemplate.queryForInt("SELECT INDENT_CREATION_APPROVAL_SEQ.NEXTVAL FROM DUAL");
		return indentCreationDetailsSeqNum;
	}


	public int insertIndentCreationApprovalAsApprove(int indentCreationApprovalSeqNum, int indentNumber,
			IndentCreationDto indentCreationDto) {
		String query = "INSERT INTO SUM_INT_CREATION_APPROVAL_DTLS(INT_CREATION_APPROVAL_DTLS_ID ,INDENT_CREATION_ID ,"
			+ "INDENT_TYPE ,creation_date ,SITE_ID ,INDENT_CREATE_APPROVE_EMP_ID, PURPOSE  ) "+
			"VALUES(?, ?, ?, sysdate, ?, ?, ?)";
		int result = jdbcTemplate.update(query, new Object[] {
				indentCreationApprovalSeqNum, 	indentNumber,
				"A", indentCreationDto.getSiteId(),indentCreationDto.getUserId(),indentCreationDto.getPurpose()
		});
		return result;
	}


	public int updateIndentCreation(int indentCreationSeqNum, String pendingEmpId, String pendingDeptId,IndentCreationDto indentCreationDto,String strFinalChangedComments) {

		int result = 0;
		if(strFinalChangedComments.equals("approving from Mail")){
			String query = "UPDATE SUMADHURA_INDENT_CREATION set PENDING_EMP_ID = ?, MODIFYDATE= sysdate , PENDIND_DEPT_ID = ? ,TEMPPASS = ?   "+
			"WHERE INDENT_CREATION_ID = ?";
			result = jdbcTemplate.update(query, new Object[] {
					pendingEmpId, pendingDeptId, indentCreationDto.getTempPass(), indentCreationSeqNum
			});
		}else{
			String query = "UPDATE SUMADHURA_INDENT_CREATION set PENDING_EMP_ID = ?, MODIFYDATE= sysdate , PENDIND_DEPT_ID = ? ,TEMPPASS = ? , METERIAL_EDIT_COMMENT = ? "+
			"WHERE INDENT_CREATION_ID = ?";
			result = jdbcTemplate.update(query, new Object[] {
					pendingEmpId, pendingDeptId, indentCreationDto.getTempPass(),strFinalChangedComments,indentCreationSeqNum
			});
		}






		return result;
	}

	public int updateIndentCreation(int indentCreationSeqNum, String pendingEmpId, String pendingDeptId) {
		String query = "UPDATE SUMADHURA_INDENT_CREATION set PENDING_EMP_ID = ? , MODIFYDATE= sysdate , PENDIND_DEPT_ID = ? "+
		"WHERE INDENT_CREATION_ID = ?";
		int result = jdbcTemplate.update(query, new Object[] {
				pendingEmpId, pendingDeptId, indentCreationSeqNum
		});
		return result;
	}

	public String getPendingEmployeeId(String user_id,int reqSiteId) {

		String strApproverEmpId = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT APPROVER_EMP_ID FROM SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? AND MODULE_TYPE='INDENT' AND SITE_ID=?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id,reqSiteId});  



		for(Map<String, Object> prods : dbIndentDts) {
			strApproverEmpId = prods.get("APPROVER_EMP_ID")==null ? "" :   prods.get("APPROVER_EMP_ID").toString();
			//icb.setFromEmpName(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());		
		}

		return strApproverEmpId;
	}

	public String getPendingDeptId(String user_id,int siteId) {



		String strApproverDepId = "";
		List<Map<String, Object>> dbIndentDts = null;
		String query = "SELECT APPROVER_DEPT_ID FROM SUMADHURA_APPROVER_MAPPING_DTL where EMP_ID = ? AND MODULE_TYPE='INDENT' AND SITE_ID=?";
		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {user_id,siteId});  



		for(Map<String, Object> prods : dbIndentDts) {
			strApproverDepId = prods.get("APPROVER_DEPT_ID")==null ? "" :   prods.get("APPROVER_DEPT_ID").toString();
			//icb.setFromEmpName(prods.get("EMP_NAME")==null ? "" :   prods.get("EMP_NAME").toString());		
		}

		return strApproverDepId;


	}

	public String  getIndentLevelComments(int indentNo) {


		List<Map<String, Object>> getcommentDtls = null;
		String strEmployeName = "";
		String strComments = "";
		String StrIndentLevelComments = "";

		String query = "select PURPOSE,SED.EMP_NAME from SUM_INT_CREATION_APPROVAL_DTLS SICAD , SUMADHURA_EMPLOYEE_DETAILS SED where "+
		" SED.EMP_ID = SICAD.INDENT_CREATE_APPROVE_EMP_ID "+
		" and INDENT_CREATION_ID = ? ";
		getcommentDtls = jdbcTemplate.queryForList(query, new Object[] {indentNo});

		if(getcommentDtls != null && getcommentDtls.size() > 0){
			for(Map<String, Object> prods : getcommentDtls) {

				strEmployeName = prods.get("EMP_NAME")==null ? "" : prods.get("EMP_NAME").toString();
				strComments = prods.get("PURPOSE")==null ? "" : prods.get("PURPOSE").toString();

				if((strEmployeName!=null && strComments!=null) && (!strEmployeName.equals("") && !strComments.equals(""))){

					StrIndentLevelComments +=  strEmployeName + " :  "+strComments +"   ,";
				}

			}
			if(StrIndentLevelComments!=null && !StrIndentLevelComments.equals("")){
				StrIndentLevelComments =  StrIndentLevelComments.substring(0,StrIndentLevelComments.length()-1);
			}

		}


		return StrIndentLevelComments;

	}
	public int getCentralIndentProcessSequenceNumber() {
		int centralIndentProcessSeqNum = jdbcTemplate.queryForInt("SELECT CENTRAL_INDENT_PROCESS_SEQ.NEXTVAL FROM DUAL");
		return centralIndentProcessSeqNum;
	}

	public int insertCentralIndentProcess(int indentProcessId,IndentCreationBean changedIndentDetails,int IndentCreationDetailsId,int site_id)
	{
		String query = "INSERT INTO SUMADHURA_CNTL_INDENT_PROCESS(INDENT_PROCESS_ID,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,CENTRAL_REQ_QUANTITY,ALLOCATED_QUANTITY,PENDING_QUANTIY,INTIATED_QUANTITY,STATUS"+
		",INDENT_REQ_SITE_ID,INDENT_CREATION_DETAILS_ID,CREATION_DATE,MEASUREMENT_ID,INDENT_REQ_QUANTITY) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,sysdate,?,?)";
		int result = jdbcTemplate.update(query, new Object[] {
				indentProcessId,
				changedIndentDetails.getProductId1(),
				changedIndentDetails.getSubProductId1(),
				changedIndentDetails.getChildProductId1(),
				changedIndentDetails.getRequiredQuantity1(),"0",
				changedIndentDetails.getRequiredQuantity1(),"0",
				"A",site_id,IndentCreationDetailsId,
				changedIndentDetails.getUnitsOfMeasurementId1(),
				changedIndentDetails.getRequiredQuantity1()
		});
		return result;
	}

	public int insertPurchaseIndentProcess(int purchaseIndentProcessId,IndentCreationBean purchaseIndentDetails,int IndentCreationDetailsId,int indentReqSiteId,String reqReceiveFrom)
	{
		String query = "INSERT INTO SUM_PURCHASE_DEPT_INDENT_PROSS(PURCHASE_DEPT_INDENT_PROSS_SEQ,PRODUCT_ID,SUB_PRODUCT_ID,CHILD_PRODUCT_ID,MEASUREMENT_ID,"+
		"PURCHASE_DEPT_REQ_QUANTITY, ALLOCATED_QUANTITY, PENDING_QUANTIY, PO_INTIATED_QUANTITY,"+
		"STATUS,INDENT_REQ_SITE_ID,REQ_RECEIVE_FROM,CREATION_DATE,INDENT_CREATION_DETAILS_ID,INDENT_REQ_QUANTITY,MATERIAL_GROUP_ID,CLOSED_INDENT_QUANT) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,sysdate,?,?,?,?)";
		int result = jdbcTemplate.update(query, new Object[] {
				purchaseIndentProcessId,
				purchaseIndentDetails.getProductId1(),
				purchaseIndentDetails.getSubProductId1(),
				purchaseIndentDetails.getChildProductId1(),
				purchaseIndentDetails.getUnitsOfMeasurementId1(),
				purchaseIndentDetails.getRequiredQuantity1(),"0",
				purchaseIndentDetails.getRequiredQuantity1(),"0",
				"A",indentReqSiteId,reqReceiveFrom,IndentCreationDetailsId,
				purchaseIndentDetails.getRequiredQuantity1(),purchaseIndentDetails.getGroupId1(),"0"
		});
		return result;
	}

	public void sendEmailDetails(String pendingEmpId, int indentNumber,
			String indentFrom, String indentTo,
			List<IndentCreationDetailsDto> listProductDetails,
			IndentCreationDto indentCreationDto, String strIndentLevelComments,
			List<IndentCreationBean> strProductsChangedComments,
			int siteWiseIndentNo, int portNo, String userName,String indentname) {

		try{
			List<Map<String, Object>> indentCreationDtls = null;
			List<String> toMailListArrayList = new ArrayList<String>();

			if(!pendingEmpId.equals("-"))
			{
				indentCreationDtls = getIndentCreationDetails(indentNumber, "approvalToEmployee");
			}else if(pendingEmpId.equals("-"))
			{
				indentCreationDtls = getIndentCreationDetails(indentNumber,"approvalToDept");

				toMailListArrayList = getAllEmployeeEmailsUnderDepartment(indentCreationDto.getPendingDeptId());
			}




			String strIndentFromSite = "";
			String strIndentFromDate = "";
			String strEmailAddress =   "";
			String strScheduleDate = "";
			for(Map<String, Object> objIndentCreationDtls : indentCreationDtls) {



				strIndentFromSite = objIndentCreationDtls.get("SITE_NAME")==null ? "" :   objIndentCreationDtls.get("SITE_NAME").toString();
				strIndentFromDate = objIndentCreationDtls.get("CREATE_DATE")==null ? "" :   objIndentCreationDtls.get("CREATE_DATE").toString();
				strEmailAddress = objIndentCreationDtls.get("Email_id")==null ? "" :   objIndentCreationDtls.get("Email_id").toString();
				strScheduleDate = objIndentCreationDtls.get("scheduleDate")==null ? "" :   objIndentCreationDtls.get("scheduleDate").toString();
			}

			if(strEmailAddress.contains(",")){
				for(String strEmailAddress1 : strEmailAddress.split(","))
				{
					toMailListArrayList.add(strEmailAddress1);
				}
			}
			else{
			toMailListArrayList.add(strEmailAddress);
			}
			if(toMailListArrayList.size() > 0){
				String emailto [] = null ;
				emailto = new String[toMailListArrayList.size()];
				toMailListArrayList.toArray(emailto);



				List<IndentCreationBean> list =  getndentChangedDetails(indentNumber);


				EmailFunction objEmailFunction = new EmailFunction();

				objEmailFunction.sendIndentCreationApprovalMailDetails(
						indentTo, indentNumber, indentFrom, strIndentFromSite,
						strIndentFromDate, strScheduleDate, emailto,
						listProductDetails, indentCreationDto,
						strIndentLevelComments, list, siteWiseIndentNo, portNo,
						userName,indentname);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public 	List<Map<String, Object>> getIndentCreationDetails( int intIndentNumber,String aprroverTo) {



		List<Map<String, Object>> dbIndentProcessDts = null;
		String query = "";
		if(aprroverTo.equals("approvalToEmployee")){


			query = " select SITE_NAME,CREATE_DATE,EMP_EMAIL as Email_id,SCHEDULE_DATE as scheduleDate from  SITE S, SUMADHURA_INDENT_CREATION SIC,SUMADHURA_EMPLOYEE_DETAILS SED where "+
			"SIC.SITE_ID = S.SITE_ID and SIC.PENDING_EMP_ID = SED.EMP_ID and SIC.INDENT_CREATION_ID = ?";

		}else{
			query = " select SITE_NAME,CREATE_DATE,DEPT_EMAIL as Email_id,SCHEDULE_DATE as scheduleDate from  SITE S, SUMADHURA_INDENT_CREATION SIC,SUMADHURA_DEPARTMENT_DETAILS SDD where "+
			" SIC.SITE_ID = S.SITE_ID and SIC.PENDIND_DEPT_ID = SDD.DEPT_ID and SIC.INDENT_CREATION_ID = ? ";

		}

		dbIndentProcessDts = jdbcTemplate.queryForList(query, new Object[] {intIndentNumber});

		return dbIndentProcessDts;

	}


	
	public List<String>  getAllEmployeeEmailsUnderDepartment(String deptId) {

		List<Map<String, Object>> dbIndentDts = null;
		String strEmailId = "";
		List<String> objList = new ArrayList<String>();
		//thisIsAStringArray[5] = "FFF";
		String query = " select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS SED where DEPT_ID = ? ";


		dbIndentDts = jdbcTemplate.queryForList(query, new Object[] {deptId});

		if(dbIndentDts!= null){



			for(Map<String, Object> prods : dbIndentDts) {



				strEmailId = prods.get("EMP_EMAIL")==null ? "" :   prods.get("EMP_EMAIL").toString();


				if(!strEmailId.equals("")){
					if(strEmailId.contains(",")){
						for(String strEmailId1 : strEmailId.split(","))
						{
							objList.add(strEmailId1);
						}
					}
					else{
					objList.add(strEmailId);
					}
				}

			}	
		}

		return objList;
	}
	
	public List<IndentCreationBean>  getndentChangedDetails(int indentNo) {


		List<Map<String, Object>> getcommentDtls = null;
		String strEmployeName = "";
		String strComments = "";
		String METERIAL_EDIT_COMMENT = "";
		List<IndentCreationBean> editList = new  ArrayList<IndentCreationBean>();
		try{

			String query = "  select METERIAL_EDIT_COMMENT from SUMADHURA_INDENT_CREATION where INDENT_CREATION_ID = ? ";
			getcommentDtls = jdbcTemplate.queryForList(query, new Object[] {indentNo});

			if(getcommentDtls != null && getcommentDtls.size() > 0) {
				for(Map<String, Object> prods : getcommentDtls) {

					strEmployeName = prods.get("METERIAL_EDIT_COMMENT")==null ? "  " : prods.get("METERIAL_EDIT_COMMENT").toString();


				}
				//METERIAL_EDIT_COMMENT =  strEmployeName.substring(0,strEmployeName.length()-1);
			}




			if(strEmployeName.contains("@@@")){
				String strEditCommentsArr [] = strEmployeName.split("@@@");
				for(int j = 0; j< strEditCommentsArr.length;j++){
					IndentCreationBean objCommentIndentCreationBean  = new IndentCreationBean();
					objCommentIndentCreationBean.setMaterialEditComment(strEditCommentsArr[j]);
					editList.add(objCommentIndentCreationBean);
				}


			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return editList;

	}
	
	//================ above is approval function
	
	//================ below is rejection function
	

	public String rejectIndentCreationFromMail(HttpSession session,
			HttpServletRequest request) {
		String response = "";
		String user_Name = "";
		String indentName="";
		try {
			int indentNumber = Integer.parseInt(request
					.getParameter("indentNumber"));

			if (request.getParameter("tempPass").equals(
					getTempPasswordOfIndent(indentNumber))) {
			} else {
				return "WrongPassword";
			}
			String userId = request.getParameter("userId");
			int site_id = Integer.parseInt(request.getParameter("siteId"));
			String siteWiseIndentNo = request.getParameter("siteWiseIndentNo");
			indentName=request.getParameter("indentName");
			String Remarks = request.getParameter("indentRemarks");
			user_Name = session.getAttribute("UserName") == null ? "" : session
					.getAttribute("UserName").toString();
			IndentCreationDto indentCreationDto = new IndentCreationDto();
			indentCreationDto.setTempPass(CommonUtilities.getStan());
			int indentCreationApprovalSeqNum = getIndentCreationApprovalSequenceNumber();
			indentCreationDto
					.setSequenccce_Number(indentCreationApprovalSeqNum);
			indentCreationDto.setUserId(userId);
			indentCreationDto.setSiteId(site_id);
			indentCreationDto.setPurpose(Remarks);
			int response2 = rejectIndentCreation(indentNumber,
					indentCreationDto);
			/*
			 *  ********************************************************for
			 * Reject Mail purpose
			 * start*****************************************************
			 */

			if (response2 > 0) {

				EmailFunction objEmailFunction = new EmailFunction();
				final String user_name_final = user_Name;
				final int getLocalPort = request.getLocalPort();
				final String strIndentNumber = String.valueOf(indentNumber);
				final String strRemarks = Remarks;

				String[] emailToAddress = getEmailsOfEmployeesInAllLowerDeptOfThisEmployee(String.valueOf(indentNumber));
				String rejectEmpDesignation = getEmpdesignation(userId);
				String userName = getuserName(userId);
				String siteName=getSiteName(site_id);
				objEmailFunction.sendMailForRejectIndent(siteWiseIndentNo,
						emailToAddress, getLocalPort, userName, strRemarks,
						rejectEmpDesignation,siteName,indentName);

			}
			/********************************************************* For Reject Mail End ***********************************************************/
			System.out.println("Indent Rejected via EMail");
			response = "Success";
		} catch (Exception e) {

			System.out.println("Indent Rejection Failed via Email");
			response = "Failed";
			e.printStackTrace();
		}
		return response;
	}

	public int rejectIndentCreation(int indentCreationSeqNum,
			IndentCreationDto indentCreationDto) {
		int result = 0;

		String query = "UPDATE SUMADHURA_INDENT_CREATION set STATUS = 'I', MODIFYDATE= sysdate, TEMPPASS = ?  "
				+ "WHERE INDENT_CREATION_ID = ?";
		result = jdbcTemplate.update(query,
				new Object[] { indentCreationDto.getTempPass(),
						indentCreationSeqNum });
		if (result > 0) {
			String sql = "INSERT INTO SUM_INT_CREATION_APPROVAL_DTLS(INT_CREATION_APPROVAL_DTLS_ID ,INDENT_CREATION_ID ,"
					+ "INDENT_TYPE ,creation_date ,SITE_ID ,INDENT_CREATE_APPROVE_EMP_ID, PURPOSE  ) "
					+ "VALUES(?, ?, ?, sysdate, ?, ?, ?)";
			result = jdbcTemplate.update(
					sql,
					new Object[] { indentCreationDto.getSequenccce_Number(),
							indentCreationSeqNum, "R",
							indentCreationDto.getSiteId(),
							indentCreationDto.getUserId(),
							indentCreationDto.getPurpose() });
		}
		return result;
	}

	public String getuserName(String user_Id) {
		String query = "SELECT EMP_NAME FROM SUMADHURA_EMPLOYEE_DETAILS where EMP_ID ='"
				+ user_Id + "'";
		String userName = jdbcTemplate.queryForObject(query, String.class);
		return userName;

	}

	
	public String[] getEmailsOfEmployeesInAllLowerDeptOfThisEmployee(String indentNumber) {
		
		
		List<String> emailList = new ArrayList<String>();
		
		List<Map<String, Object>> dbIndentDts = null;
		
			
			String query = "select EMP_EMAIL from SUMADHURA_EMPLOYEE_DETAILS where EMP_ID in ( "
						+" select distinct(INDENT_CREATE_APPROVE_EMP_ID) from SUM_INT_CREATION_APPROVAL_DTLS where "
						+" INDENT_CREATION_ID =?)";

			dbIndentDts = jdbcTemplate.queryForList(query, new Object[]{indentNumber});
			for(Map<String, Object> prods : dbIndentDts) {
				String empEmail = prods.get("EMP_EMAIL")==null ? "" : prods.get("EMP_EMAIL").toString();
				if(StringUtils.isNotBlank(empEmail)){
					if(empEmail.contains(",")){
						String[] emailArr= empEmail.split(",");
						for(String email:emailArr){
							emailList.add(email);
						}
					}
					else{emailList.add(empEmail);}
				}
			}
			
		String[] emailArray = new String[emailList.size()];
		emailList.toArray(emailArray);
		return emailArray;
	}

	public String getEmpdesignation(String user_Id) {
		String query = "SELECT EMP_DESIGNATION FROM SUMADHURA_EMPLOYEE_DETAILS where EMP_ID ='"
				+ user_Id + "'";
		String designation = jdbcTemplate.queryForObject(query, String.class);
		return designation;

	}
	public String getSiteName(int site_id) {
		String query = "SELECT SITE_NAME FROM SITE where SITE_ID ='"+site_id+"'";
		String siteName = jdbcTemplate.queryForObject(query, String.class);
		return siteName;

	}
	
	/*=========================================Material BOQ started ============================================================================*/
	// this is for getting the child produts for based in the group id
	public Map<String,String> getChildProductsWithGroupId(String groupId) {

		Map<String,String> childIds =new HashMap<String,String>();
		List<Map<String, Object>> dbIndentDts = null;
		String childProdId="";
		//String childProductList="";
		String measumentId="";
		if(!groupId.equals("") && groupId!=null){
			String sql="select C.CHILD_PRODUCT_ID,M.MEASUREMENT_ID from CHILD_PRODUCT C,MEASUREMENT M WHERE C.MATERIAL_GROUP_ID=? AND M.CHILD_PRODUCT_ID=C.CHILD_PRODUCT_ID";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {groupId});
			for(Map<String, Object> prods : dbIndentDts) {
				childProdId=(prods.get("CHILD_PRODUCT_ID")==null ? "" : prods.get("CHILD_PRODUCT_ID").toString());
				measumentId=(prods.get("MEASUREMENT_ID")==null ? "" : prods.get("MEASUREMENT_ID").toString());
				childIds.put(childProdId,measumentId);
				/*if(!childProdId.equals("") && childProdId!=null){
					childProductList += childProdId +",";
				}*/

			}
		}
		/*if(childProductList!=null && !childProductList.equals("")){
			childProductList =  childProductList.substring(0,childProductList.length()-1);
		}*/
		return childIds;

	}
	// getting the received quantity for receive and dc form based on the child product ids
	public double getindentAndDcReceivedQuantity(String childProductList,String siteId,String measurementList) {

		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbDcDts = null;
		double indent_Quantity=0.0;
		double dc_Quantity=0.0;
		double totalQuantity=0.0;
		//String childProductList="";
		if(!childProductList.equals("") && childProductList!=null){
			childProductList=childProductList.replace(",","','");
			measurementList=measurementList.replace(",","','");
			String sql="SELECT SUM(IED.RECEVED_QTY) as RECEIVED_QUANTITY FROM INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE WHERE IED.CHILD_PRODUCT_ID in ('"+childProductList+"') AND IED.MEASUR_MNT_ID in('"+measurementList+"') and IE.SITE_ID=? AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {siteId});

			String query="SELECT SUM(DF.RECEVED_QTY) as DC_QUANTITY FROM DC_FORM DF,DC_ENTRY DE WHERE DF.CHILD_PRODUCT_ID in ('"+childProductList+"') AND DF.MEASUR_MNT_ID in('"+measurementList+"') and DE.SITE_ID=? AND DF.STATUS='A' AND DF.DC_ENTRY_ID=DE.DC_ENTRY_ID";
			dbDcDts = jdbcTemplate.queryForList(query, new Object[] {siteId});

			for(Map<String, Object> prods : dbIndentDts) {
				indent_Quantity=Double.valueOf(prods.get("RECEIVED_QUANTITY")==null ? "0" : prods.get("RECEIVED_QUANTITY").toString());
			}
			for(Map<String, Object> prod : dbDcDts) {
				dc_Quantity=Double.valueOf(prod.get("DC_QUANTITY")==null ? "0" : prod.get("DC_QUANTITY").toString());
			}
			totalQuantity=indent_Quantity+dc_Quantity;
		}

		return totalQuantity;

	}
	// this is used to getting the issued quantity i.e issue to other site,issue to scrap,issue to theft
	public double getIssuedQuantity(String childProductList,String siteId,String measurementList) {
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbDcDts = null;
		double issued_Quantity=0.0;
		if(!childProductList.equals("") && childProductList!=null){
			childProductList=childProductList.replace(",","','");
			measurementList=measurementList.replace(",","','");
			String sql="SELECT SUM(IED.ISSUED_QTY) as ISSUED_QUANTITY FROM INDENT_ENTRY_DETAILS IED,INDENT_ENTRY IE  WHERE IED.CHILD_PRODUCT_ID in ('"+childProductList+"') AND IED.MEASUR_MNT_ID in('"+measurementList+"') AND IE.INDENT_TYPE IN('OUTO','OUTS','OUTT') AND IE.INDENT_ENTRY_ID=IED.INDENT_ENTRY_ID AND IE.SITE_ID=?";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {siteId});
			for(Map<String, Object> prods : dbIndentDts) {
				issued_Quantity=Double.valueOf(prods.get("ISSUED_QUANTITY")==null ? "0" : prods.get("ISSUED_QUANTITY").toString());
			}
		}

		return issued_Quantity;

	}
	// this is used to getting the indent pending quantity i.e reqQuantity and receiveQuantity 
	public double getIndentPendingQuantity(String childProductList,String siteId,String measurementList) {
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbDcDts = null;
		double indent_available_Quantity=0.0;
		if(!childProductList.equals("") && childProductList!=null){
			childProductList=childProductList.replace(",","','");
			measurementList=measurementList.replace(",","','");
			String sql="SELECT SUM(SICD.REQ_QUANTITY)-SUM(SICD.RECEIVE_QUANTITY) as AVAILABLE_QUANTITY FROM SUMADHURA_INDENT_CREATION SIC,SUMADHURA_INDENT_CREATION_DTLS SICD WHERE SIC.INDENT_CREATION_ID=SICD.INDENT_CREATION_ID AND SICD.CHILD_PRODUCT_ID in ('"+childProductList+"') AND SICD.MEASUREMENT_ID in('"+measurementList+"') AND SIC.SITE_ID=? AND SIC.STATUS='A'";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {siteId});
			for(Map<String, Object> prods : dbIndentDts) {
				indent_available_Quantity=Double.valueOf(prods.get("AVAILABLE_QUANTITY")==null ? "0" : prods.get("AVAILABLE_QUANTITY").toString());
			}
		}

		return indent_available_Quantity;

	}
	// getting the TOTAL boq Quantity from qs_product_dtls
	public double getBOQQuantity(String groupId,String siteId) {
		List<Map<String, Object>> dbIndentDts = null;
		List<Map<String, Object>> dbDts = null;
		
		double total_BOQ_Quantity=0.0;
		double buffer_Quantity=0.0;
		double totalQuantity=0.0;
		if(!groupId.equals("") && groupId!=null){
			String sql="SELECT SUM(TOTAL_QUANTITY) TOTAL FROM QS_BOQ_PRODUCT_DTLS  WHERE MATERIAL_GROUP_ID=? AND  SITE_ID=?";
			dbIndentDts = jdbcTemplate.queryForList(sql, new Object[] {groupId,siteId});

			String query="SELECT SUM(QUANTITY) BUFFER FROM QS_BUFFER_STOCK  WHERE MATERIAL_GROUP_ID=? AND  SITE_ID=?";
			dbDts = jdbcTemplate.queryForList(query, new Object[] {groupId,siteId});
			if(dbIndentDts!=null && dbIndentDts.size()>0){
			for(Map<String, Object> prods : dbIndentDts) {
				total_BOQ_Quantity=Double.valueOf(prods.get("TOTAL")==null ? "0" : prods.get("TOTAL").toString());
			}
			}
			if(dbDts!=null && dbDts.size()>0){
			for(Map<String, Object> prods : dbDts) {
				buffer_Quantity=Double.valueOf(prods.get("BUFFER")==null ? "0" : prods.get("BUFFER").toString());
			}
			}
			totalQuantity=total_BOQ_Quantity+buffer_Quantity;
		}

		return totalQuantity;

	}
	

}
