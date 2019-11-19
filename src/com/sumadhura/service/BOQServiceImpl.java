package com.sumadhura.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.FormulaParser;

import com.sumadhura.bean.BOQBean;
import com.sumadhura.bean.IndentCreationBean;
import com.sumadhura.bean.MaterialDetails;
import com.sumadhura.bean.PaymentBean;
import com.sumadhura.bean.WDRateAnalysis;
import com.sumadhura.dto.BOQAreaMappingDto;
import com.sumadhura.dto.BOQDetailsDto;
import com.sumadhura.dto.BOQProductDetailsDto;
import com.sumadhura.dto.MultiObject;
import com.sumadhura.dto.ReviseBOQQtyChangedDtlsDto;
import com.sumadhura.transdao.BOQDao;
import com.sumadhura.util.UIProperties;

import oracle.net.aso.u;


@Service
public class BOQServiceImpl extends UIProperties implements BOQService{
	
	@Autowired
	BOQDao objBOQDao;

	@Autowired
	PlatformTransactionManager transactionManager;

	

	@SuppressWarnings("resource")
	@Override
	public String saveBOQ(MultipartFile multipartFile,List<String> errMsg,String user_id, String boqSiteId, List<String> tempBOQNoForController) throws IOException {

		String typeOfWork = "PIECEWORK";
		String response = null;
		InputStream inp =  new BufferedInputStream(multipartFile.getInputStream());
		Workbook wb = null;
		int rowNumMail = 0;
		List<Exception> ex_list = new ArrayList<Exception>();
		try {
			wb = WorkbookFactory.create(inp);
		} catch (Exception e) {
			e.printStackTrace();
			ex_list.add(e);
			mailException(e,rowNumMail+1,ex_list);
			return "Exception";
		}
		System.out.println("Transaction Opened");
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try{
			/*long startTime1 = System.currentTimeMillis();
			dummy();
			long stopTime1 = System.currentTimeMillis();
		    long elapsedTime1 = stopTime1 - startTime1;
		    System.out.println("final elapsed time: "+elapsedTime1);
		    int err=10/0;*/
			long startTime1 = System.currentTimeMillis();
			
		    boolean isBoqAlreadyPresent = objBOQDao.isBoqAlreadyPresent(boqSiteId,typeOfWork);
			if(isBoqAlreadyPresent){
				errMsg.add("BOQ is already uploaded for this Site");
				throw new RuntimeException("BOQ is already uploaded for this Site");
			}
			
			/*** Getting BlockIds,FloorIds,MajorHeadIds,MinorHeadIds,WorkDescriptions,MeasurementIds ***/
			long startTime = System.currentTimeMillis();
		    HashMap<String,String> BlockIdsMap = objBOQDao.getBlockIdsMap(boqSiteId);
			HashMap<String,HashMap<String,String>> FloorIdsMap = objBOQDao.getFloorIdsMap(boqSiteId);
			HashMap<String,String> MajorHeadIdsMap = objBOQDao.getMajorHeadIdsMap(typeOfWork);
			HashMap<String,HashMap<String,String>> MinorHeadIdsMap = objBOQDao.getMinorHeadIdsMap(typeOfWork);
			HashMap<String,HashMap<String,String>> WorkDescriptionIdsMap = objBOQDao.getWorkDescriptionIdsMap(typeOfWork);
		    HashMap<String,HashMap<String,String>> MeasurementIdsMap = objBOQDao.getMeasurementIdsMap(typeOfWork);
		    System.out.println("All ok");
		   
		    //String del1 = objBOQDao.getMajorHeadTblSeqId();
		    //String del2 = objBOQDao.getMinorHeadTblSeqId();
		    long stopTime = System.currentTimeMillis();
		    long elapsedTime = stopTime - startTime;
		    System.out.println("final elapsed time: "+elapsedTime);
		    /*int ter = 1;
		    if(ter==1){
		    	throw new RuntimeException();
		    }*/
			/*** Getting BlockIds,FloorIds,.....    END    ***/
			
			
			Sheet sheet = wb.getSheet("BOQ");
			Sheet sheet2 = wb.getSheet("RA");
			
			
			DataFormatter formatter = new DataFormatter();
			int rowsCount = sheet.getLastRowNum();
			//rowsCount = 427;
			int BlockRowIndex = 2;
			int BlockNamesRowIndex = 3;
			
			final int IMScodeColumnIndex = 1;
			final int DescriptionColumnIndex = 2;
			final int UnitOfMeasurementColumnIndex = 3;
			int MaterialRatePerUnitColumnIndex = 4;
			int LaborRatePerUnitColumnIndex = 5;
			
			Row row = sheet.getRow(BlockRowIndex);
			int noOfColumns = row.getLastCellNum();
	
			boolean isBlockKeywordGiven = false;
			boolean isLaborAmountGiven = false;
			boolean isMaterialAmountGiven = false;
			boolean isCumulativeAmountGiven = false;
			for (int i = 0; i <= noOfColumns; i++) {
				Cell cell = row.getCell(i);
				String cellValue = null;
				switch(i){

				case IMScodeColumnIndex:
					cellValue = row.getCell(i).getStringCellValue().toLowerCase();
					if(cellValue.contains("ims code")){break;}
					else{
						errMsg.add("Check IMS CODE is in Right Column");
						throw new RuntimeException("Check IMS CODE is in Right Column");
					}
				case DescriptionColumnIndex:
					cellValue = row.getCell(i).getStringCellValue().toLowerCase();
					if(cellValue.contains("description")){break;}
					else{
						errMsg.add("Check Description is in Right Column");
						throw new RuntimeException("Check Description is in Right Column");
					}
				case UnitOfMeasurementColumnIndex:	
					cellValue = row.getCell(i).getStringCellValue().toLowerCase();
					if(cellValue.contains("uom")){break;}
					else{
						errMsg.add("Check UOM is in Right Column");
						throw new RuntimeException("Check UOM is in Right Column");
					}

				default: 
					if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						continue;
					}
					else{
						cellValue = row.getCell(i).getStringCellValue().toLowerCase();
						if(cellValue.contains("block")){
							isBlockKeywordGiven = true;
						}
						else if(cellValue.contains("labor")&&cellValue.contains("amount")){
							isLaborAmountGiven = true;
						}
						else if(cellValue.contains("material")&&cellValue.contains("amount")){
							isMaterialAmountGiven = true;
						}
						else if(cellValue.contains("cumulative")&&cellValue.contains("amount")){
							isCumulativeAmountGiven = true;
						}
					}
				}//switch-end

			}
			if(!isBlockKeywordGiven){
				errMsg.add("Check BLOCK keywords is Given");
				throw new RuntimeException("Check BLOCK keywords is Given");
			}
			if(!isLaborAmountGiven){
				errMsg.add("Check LABOR AMOUNT keyword is Given");
				throw new RuntimeException("Check LABOR AMOUNT keyword is Given");
			}
			if(!isMaterialAmountGiven){
				errMsg.add("Check MATERIAL AMOUNT keyword is Given");
				throw new RuntimeException("Check MATERIAL AMOUNT keyword is Given");
			}
			if(!isCumulativeAmountGiven){
				errMsg.add("Check CUMULATIVE AMOUNT keyword is Given");
				throw new RuntimeException("Check CUMULATIVE AMOUNT keyword is Given");
			}
			List<Integer> blockColumnIndexList = new ArrayList<Integer>();
			int laborTotalColumnIndex = -1;
			int materialTotalColumnIndex = -1;
			int cumulativeTotalColumnIndex = -1;
			for (int i = 0; i <= noOfColumns; i++) {
				Cell cell = row.getCell(i);
				if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					continue;
				}
				else{
					String cellValue = row.getCell(i).getStringCellValue().toLowerCase();
					if(cellValue.contains("block")){
						blockColumnIndexList.add(i);
					}
					else if(cellValue.contains("labor")&&cellValue.contains("amount")){
						laborTotalColumnIndex = i;
					}
					else if(cellValue.contains("material")&&cellValue.contains("amount")){
						materialTotalColumnIndex = i;
					}
					else if(cellValue.contains("cumulative")&&cellValue.contains("amount")){
						cumulativeTotalColumnIndex = i;
					}
				}
			}
			row = sheet.getRow(BlockNamesRowIndex);
			String LaborKeyword = row.getCell(LaborRatePerUnitColumnIndex).getStringCellValue().toLowerCase();
			String MaterialKeyword = row.getCell(MaterialRatePerUnitColumnIndex).getStringCellValue().toLowerCase();
			if(!LaborKeyword.contains("labor")){
				errMsg.add("Check LABOR RATE/UNIT is in Right Column");
				throw new RuntimeException("Check RATE/UNIT is in Right Column");
			}
			if(!MaterialKeyword.contains("material")){
				errMsg.add("Check MATERIAL RATE/UNIT is in Right Column");
				throw new RuntimeException("Check RATE/UNIT is in Right Column");
			}
			/**************************************** GETTING BLOCK IDs - START *********************************************/
			//Map<Integer,String> blockNames = new HashMap<Integer,String>();
			List<String> blockIdsList = new ArrayList<String>();
			Map<Integer,String> blockIds = new HashMap<Integer,String>();
			for (int index : blockColumnIndexList) {
				String blockName = row.getCell(index).getStringCellValue().trim().toLowerCase();
				//blockNames.put(index,blockName);
				String blockId = null;
				try{
					//blockId = objBOQDao.getBlockId(blockName,boqSiteId);
					blockId = BlockIdsMap.get(blockName);
					if(blockId==null){throw new RuntimeException();}
				}catch(Exception e){
					errMsg.add("Check '"+blockName+"' Spelling");
					throw new RuntimeException("Problem in Getting BlockId");
				}
				blockIds.put(index, blockId);
				blockIdsList.add(blockId);
			}
			/**************************************** GETTING BLOCK IDs - END *********************************************/
			
			/******************* Comparing Both BOQ & RA sheets *****************************************/
			HashMap<String, String> BOQ_Sheet_MaterialWDs = getBOQSheetMaterialWDs(sheet,errMsg,rowsCount,IMScodeColumnIndex,DescriptionColumnIndex,UnitOfMeasurementColumnIndex,MaterialRatePerUnitColumnIndex,false,-1,"");
			System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
			System.out.println(BOQ_Sheet_MaterialWDs);
			HashMap<String,WDRateAnalysis> RA_Sheet_Data = getRASheetData(sheet2,errMsg,false,"");
			System.out.println(RA_Sheet_Data);
			Set<String> RA_Sheet_MaterialWDs = RA_Sheet_Data.keySet();
			Set<String> MaterialWdsHasNoRAData = BOQ_Sheet_MaterialWDs.keySet();
			MaterialWdsHasNoRAData.removeAll(RA_Sheet_MaterialWDs);
			System.out.println(MaterialWdsHasNoRAData);
			if(!MaterialWdsHasNoRAData.isEmpty()){
				errMsg.add("Below Work Descriptions are Not Found in RA Sheet");
				for(String wd : MaterialWdsHasNoRAData){
					errMsg.add(BOQ_Sheet_MaterialWDs.get(wd));
				}
				throw new RuntimeException("");
			}
			
			/**************************************************************************/
			String pendingEmpId = objBOQDao.getPendingEmpId(user_id,boqSiteId);
			int tempBOQNo = objBOQDao.getTempBOQNo();
			tempBOQNoForController.add(String.valueOf(tempBOQNo));
			objBOQDao.insertQsTempBOQ(tempBOQNo,user_id,boqSiteId,pendingEmpId,typeOfWork,0,"NORMAL");
			
			//int err =10/0;
			//System.out.print(err);
			
			String majorHeadDesc = null;
			String majorHeadId = null;
			String minorHeadDesc = null;
			String minorHeadId = null;
			String scopeOfWork = null;
			String workDescription = null;
			String workDescriptionId = null;
			String measurementName = null;
			String measurementId = null;
			double laborRatePerUnit = 0.0;
			double materialRatePerUnit = 0.0;
			
			double currentCumulativeMajorHeadTotal = 0.0;
			double currentLaborMajorHeadTotal = 0.0;
			double currentMaterialMajorHeadTotal = 0.0;
			double GrandTotal = 0.0;
			double LaborTotal = 0.0;
			double MaterialTotal = 0.0;
			
			boolean isGrandTotalVerified = false;
			
			int TotalnoofMajorHeads = 0;
			int TotalnoofMinorHeads = 0;
			int TotalnoofWorkDescriptions = 0;
			
			List<String> workList = new ArrayList<String>();
			List<String> blockWiseWorkList = new ArrayList<String>();
			List<String> floorWiseWorkList = new ArrayList<String>();
			HashMap<String,String> workGroupList = new HashMap<String,String>();
			
			boolean givenMaterialWork = false;
			boolean givenLaborWork = false;
			
			
			for (int rowNum = 0; rowNum <= rowsCount; rowNum++) {
				System.out.println(rowNum);
				row = sheet.getRow(rowNum);
				 
				if(row==null){
					continue;
				}
				Cell cell = row.getCell(IMScodeColumnIndex,Row.RETURN_BLANK_AS_NULL);
				if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					continue;
				}
				String headerName = cell.getStringCellValue().toLowerCase();
				if(headerName.contains("major")&&headerName.contains("head")&&!headerName.contains("total")){
					
					majorHeadDesc = row.getCell(DescriptionColumnIndex).getStringCellValue().trim();
					System.out.println(majorHeadDesc);
					//checking major head is in DB
					//majorHeadId = objBOQDao.getMajorHeadId(majorHeadDesc,typeOfWork);
					majorHeadId = MajorHeadIdsMap.get(majorHeadDesc.toLowerCase());
					System.out.println(majorHeadId);
					if(majorHeadId==null){
						majorHeadId = objBOQDao.getMajorHeadTblSeqId();
						objBOQDao.insertMajorHead(majorHeadId,majorHeadDesc,user_id,typeOfWork);
						MajorHeadIdsMap.put(majorHeadDesc.toLowerCase(), majorHeadId);
					}
					if(StringUtils.isBlank(majorHeadDesc)||majorHeadId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", Major Head is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", Major Head is Not given");
					}
					scopeOfWork = null;
					minorHeadDesc = null;
					minorHeadId = null;
					workDescription = null;
					workDescriptionId = null;
					currentCumulativeMajorHeadTotal = 0.0;
					currentLaborMajorHeadTotal = 0.0;
					currentMaterialMajorHeadTotal = 0.0;
					TotalnoofMajorHeads += 1;
					
				}
				if(headerName.contains("minor")&&headerName.contains("head")){
					if(StringUtils.isBlank(majorHeadDesc)||majorHeadId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", Major Head is Not given for this Minor Head");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", Major Head is Not given for this Minor Head");
					}
					minorHeadDesc = row.getCell(DescriptionColumnIndex).getStringCellValue().trim();
					//minorHeadId = objBOQDao.getMinorHeadId(minorHeadDesc,majorHeadId,typeOfWork);
					minorHeadId = MinorHeadIdsMap.get(majorHeadId)==null?null:MinorHeadIdsMap.get(majorHeadId).get(minorHeadDesc.toLowerCase());
					if(minorHeadId==null){
						minorHeadId = objBOQDao.getMinorHeadTblSeqId();
						objBOQDao.insertMinorHead(minorHeadId,minorHeadDesc,user_id,majorHeadId,typeOfWork);
						if(MinorHeadIdsMap.get(majorHeadId)==null){
							MinorHeadIdsMap.put(majorHeadId,new HashMap<String,String>());
						}
						MinorHeadIdsMap.get(majorHeadId).put(minorHeadDesc.toLowerCase(),minorHeadId);
					}
					if(StringUtils.isBlank(minorHeadDesc)||minorHeadId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", Minor Head is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", Minor Head is Not given");
					}
					TotalnoofMinorHeads += 1;
					workDescription = null;
					workDescriptionId = null;
				}
				if(headerName.contains("sow")){
					scopeOfWork = row.getCell(DescriptionColumnIndex).getStringCellValue();
				}
				/************************************ IF IT IS  BLOCK - START  **************************************/
				if(headerName.contains("wd")&&headerName.contains("block")){
					Cell cell4 = row.getCell(LaborRatePerUnitColumnIndex);
					Cell cell7 = row.getCell(MaterialRatePerUnitColumnIndex);
					if (cell4 == null || cell4.getCellType() == Cell.CELL_TYPE_BLANK) {
						//errMsg.add("Labor Rate/Unit is Blank in Line "+(rowNum+1));
						//throw new RuntimeException("Labor Rate per Unit is Blank.");
						laborRatePerUnit = 0.0;
						
					}else{
						laborRatePerUnit = row.getCell(LaborRatePerUnitColumnIndex).getNumericCellValue();
					}
					if (cell7 == null || cell7.getCellType() == Cell.CELL_TYPE_BLANK) {
						//errMsg.add("Material Rate/Unit is Blank in Line "+(rowNum+1));
						//throw new RuntimeException("Material Rate per Unit is Blank.");
						materialRatePerUnit = 0.0;
					}else{
						materialRatePerUnit = row.getCell(MaterialRatePerUnitColumnIndex).getNumericCellValue();
					}
					if(laborRatePerUnit==0.0 && materialRatePerUnit==0.0){
						continue;
					}
					
					String levelOfWork = "B"; //Block level
					if(StringUtils.isBlank(minorHeadDesc)||minorHeadId==null){
						errMsg.add("In BOQ sheet, Line no. "+(rowNum+1)+", MinorHead is not given for this work.");
						throw new RuntimeException("In BOQ sheet, Line no. "+(rowNum+1)+", MinorHead is not given for this work.");
					}
					workDescription = row.getCell(DescriptionColumnIndex).getStringCellValue().trim();
					//workDescriptionId = objBOQDao.getWorkDescriptionId(workDescription,minorHeadId,typeOfWork);
					workDescriptionId = WorkDescriptionIdsMap.get(minorHeadId)==null?null:WorkDescriptionIdsMap.get(minorHeadId).get(workDescription.toLowerCase());
					if(workDescriptionId==null){
						workDescriptionId = objBOQDao.getWorkDescriptionTblSeqId();
						objBOQDao.insertWorkDescription(workDescription,workDescriptionId,minorHeadId,user_id,levelOfWork,typeOfWork);
						if(WorkDescriptionIdsMap.get(minorHeadId)==null){
							WorkDescriptionIdsMap.put(minorHeadId,new HashMap<String,String>());
						}
						WorkDescriptionIdsMap.get(minorHeadId).put(workDescription.toLowerCase(),workDescriptionId);
						
					}
					
					if(StringUtils.isBlank(workDescription)||workDescriptionId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
					}
					measurementName = row.getCell(UnitOfMeasurementColumnIndex).getStringCellValue().trim();
					//measurementId = objBOQDao.getmeasurementId(measurementName,workDescriptionId,typeOfWork);
					measurementId = MeasurementIdsMap.get(workDescriptionId)==null?null:MeasurementIdsMap.get(workDescriptionId).get(measurementName.toLowerCase());
					if(measurementId==null){
						measurementId = objBOQDao.getMeasurementTblSeqId();
						objBOQDao.insertMeasurement(measurementId,measurementName,workDescriptionId,user_id,typeOfWork);
						if(MeasurementIdsMap.get(workDescriptionId)==null){
							MeasurementIdsMap.put(workDescriptionId,new HashMap<String,String>());
						}
						MeasurementIdsMap.get(workDescriptionId).put(measurementName.toLowerCase(),measurementId);
					}
					if(StringUtils.isBlank(measurementName)||measurementId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", UOM is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", UOM is Not given");
					}
					
					//laborRatePerUnit = row.getCell(LaborRatePerUnitColumnIndex).getNumericCellValue();
					//materialRatePerUnit = row.getCell(MaterialRatePerUnitColumnIndex).getNumericCellValue();
					if(laborRatePerUnit!=0.0){givenLaborWork = true;}else{givenLaborWork = false;}
					if(materialRatePerUnit!=0.0){givenMaterialWork = true;}else{givenMaterialWork = false;}
					
					Cell cell2 = row.getCell(laborTotalColumnIndex);
					Cell cell8 = row.getCell(materialTotalColumnIndex);
					Cell cell12 = row.getCell(cumulativeTotalColumnIndex);
					if(cell2.getCellType() == Cell.CELL_TYPE_FORMULA || cell8.getCellType() == Cell.CELL_TYPE_FORMULA){
						response =  "FormulaProblem";
						throw new RuntimeException("FormulaProblem");
					}
					
					double givenLaborTotalForVerification = cell2.getNumericCellValue();
					double givenMaterialTotalForVerification = cell8.getNumericCellValue();
					double givenCumulativeTotalForVerification = cell12.getNumericCellValue();
					
					double totalAreaOfAllBlocks = 0.0;
					for(int index : blockColumnIndexList){
						double blockArea = 0.0;
						Cell cell1 = row.getCell(index,Row.RETURN_BLANK_AS_NULL);
						if (cell1 == null || cell1.getCellType() == Cell.CELL_TYPE_BLANK) {
							continue;
						}
						else{
							if(cell1.getCellType() == Cell.CELL_TYPE_NUMERIC){
								blockArea = cell1.getNumericCellValue();
							}
							else{
								blockArea = 0.0;
							}

							totalAreaOfAllBlocks += blockArea;
						}
					}
					
					double totalLaborAmountForAllBlocks = totalAreaOfAllBlocks*laborRatePerUnit;
					double totalMaterialAmountForAllBlocks = totalAreaOfAllBlocks*materialRatePerUnit;
					double totalCumulativeAmountForAllBlocks = totalLaborAmountForAllBlocks+totalMaterialAmountForAllBlocks;
					for(int i=1;i<=3;i++){
						String xxx = "";
						double xxxTotalinExcel = 0.0;
						double xxxTotal = 0.0;
						if(i==1){ xxx = "Material"; xxxTotalinExcel = givenMaterialTotalForVerification; 	xxxTotal = totalMaterialAmountForAllBlocks;}
						if(i==2){ xxx = "Labor"; 	xxxTotalinExcel = givenLaborTotalForVerification; 		xxxTotal = totalLaborAmountForAllBlocks;}
						if(i==3){ xxx = "Cumulative"; 	xxxTotalinExcel = givenCumulativeTotalForVerification; 	xxxTotal = totalCumulativeAmountForAllBlocks;}

						if(Math.abs(xxxTotalinExcel-xxxTotal)>0.001){
							response =  "VerificationFailed";
							System.out.println("given"+xxx+"TotalForVerification & IMS value: "+xxxTotalinExcel+","+xxxTotal);
							errMsg.add("In line "+(rowNum+1));
							errMsg.add(xxx+" Amounts in Excel & IMS: "+xxxTotalinExcel+","+xxxTotal);
							throw new RuntimeException("VerificationFailed");
						}
					}
					
					for(int recordTypeNo=1;recordTypeNo<=2;recordTypeNo++){
						String recordType = null;
						double ratePerUnit = 0.0;
						double totalAmountForAllBlocks = 0.0;
						double givenTotalForVerification = 0.0;
						if(recordTypeNo==1 && givenLaborWork){
							recordType = "LABOR";
							ratePerUnit = laborRatePerUnit;
							totalAmountForAllBlocks = totalLaborAmountForAllBlocks;
							givenTotalForVerification = givenLaborTotalForVerification;
						}
						else if(recordTypeNo==2 && givenMaterialWork){
							recordType = "MATERIAL";
							ratePerUnit = materialRatePerUnit;
							totalAmountForAllBlocks = totalMaterialAmountForAllBlocks;
							givenTotalForVerification = givenMaterialTotalForVerification;
						}
						else {continue;}
						
						//checking for safety
						//if(recordType==null || ratePerUnit == 0.0 || totalAmountForAllBlocks == 0.0 || givenTotalForVerification == 0.0){throw new RuntimeException("recordType="+recordType+",ratePerUnit="+ratePerUnit+",totalAmountForAllBlocks="+totalAmountForAllBlocks+",givenTotalForVerification="+givenTotalForVerification);}

						int tempBOQDetailsId = objBOQDao.getTempBOQDetailsId(workDescriptionId,tempBOQNo,measurementId,recordType);
						if(tempBOQDetailsId==0){
							tempBOQDetailsId = objBOQDao.getTempBOQDetailsId();
							objBOQDao.insertQsTempBOQDetails(tempBOQDetailsId,workDescriptionId,measurementId,totalAreaOfAllBlocks,totalAmountForAllBlocks,tempBOQNo,scopeOfWork,minorHeadId,null,recordType);
							/*final BOQDetailsDto objBOQDetailsDto = new BOQDetails();
							objBOQDetailsDto.setTempBOQDetailsId(tempBOQDetailsId);
							objBOQDetailsDto.setWorkDescriptionId(workDescriptionId);
							objBOQDetailsDto.setMeasurementId(measurementId);
							objBOQDetailsDto.setTotalAreaOfAllBlocks(new DecimalFormat("##.##").format(totalAreaOfAllBlocks));
							objBOQDetailsDto.setTempBOQNo(tempBOQNo);
							objBOQDetailsDto.setTotalAmountForAllBlocks(new DecimalFormat("##.##").format(totalAmountForAllBlocks));
							objBOQDetailsDto.setScopeOfWork(scopeOfWork);
							objBOQDetailsDto.setMinorHeadId(minorHeadId);
							objBOQDetailsDto.setAction(null);
							objBOQDetailsDto.setRecordType(recordType);
							ExecutorService executorService = Executors.newFixedThreadPool(10);
							try{executorService.execute(new Runnable() {public void run() {
								objBOQDao.insertQsTempBOQDetails(objBOQDetailsDto);
							}});executorService.shutdown();}catch(Exception e){e.printStackTrace();executorService.shutdown();}
							*/
						}
						else{
							System.out.println("totalAreaOfAllBlocks:"+totalAreaOfAllBlocks+"Line "+(rowNum+1));
							objBOQDao.updateQsTempBOQDetails(tempBOQDetailsId,totalAreaOfAllBlocks,totalAmountForAllBlocks);
							/*final BOQDetailsDto objBOQDetailsDto = new BOQDetails();
							objBOQDetailsDto.setTempBOQDetailsId(tempBOQDetailsId);
							objBOQDetailsDto.setDoubleTotalAreaOfAllBlocks(totalAreaOfAllBlocks);
							objBOQDetailsDto.setDoubleTotalAmountForAllBlocks(totalAmountForAllBlocks);
							ExecutorService executorService = Executors.newFixedThreadPool(10);
							try{executorService.execute(new Runnable() {public void run() {
								objBOQDao.updateQsTempBOQDetails(objBOQDetailsDto);
							}});executorService.shutdown();}catch(Exception e){e.printStackTrace();executorService.shutdown();}
							*/
						}
						/*********************************** LOOPING  EACH BLOCK - START  ************************************/
						double additionOfAllIndividualBlockAreaAmounts = 0;
						for(int index : blockColumnIndexList){
							Cell cell1 = row.getCell(index,Row.RETURN_BLANK_AS_NULL);
							if(cell1 != null && cell1.getCellType() == Cell.CELL_TYPE_NUMERIC){
								double blockArea = cell1.getNumericCellValue();
								if(blockArea==0.0){continue;}
								double blockAreaAmount = blockArea*ratePerUnit;
								String blockId = blockIds.get(index);
								String floorId = null;
								String workAreaId = objBOQDao.getWorkAreaSeqId();
								String workAreaGroupId = null;
								String currentWorkGroup = workDescriptionId+"@@"+blockId+"@@"+floorId;
								if(workGroupList.containsKey(currentWorkGroup)){
									workAreaGroupId = workGroupList.get(currentWorkGroup);
								}
								else{
									workAreaGroupId = objBOQDao.getWorkAreaGroupSeqId();
									workGroupList.put(currentWorkGroup,workAreaGroupId);
								}
								if(floorWiseWorkList.contains(workDescriptionId)){
									errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",This work is given already in Floor wise.");
									throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+",This work is given already in Floor wise.");
								}
								else{
									blockWiseWorkList.add(workDescriptionId);
								}
								String currentWork = recordType+"@@"+workDescriptionId+"@@"+blockId+"@@"+floorId;
								if(workList.contains(currentWork)){
									errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",Duplicate Work Found.");
									throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+"Duplicate Work Found");
								}
								else{
									workList.add(currentWork);
								}
								objBOQDao.insertQsTempBOQAreaMapping(workAreaId,workDescriptionId,measurementId,blockId,blockArea,tempBOQDetailsId,blockAreaAmount,floorId,ratePerUnit,tempBOQNo,null,recordType,workAreaGroupId);
								/*final BOQAreaMappingDto objBOQAreaMappingDto = new BOQAreaMappingDto(); 
								objBOQAreaMappingDto.setWorkAreaId(workAreaId);
								objBOQAreaMappingDto.setWorkDescriptionId(workDescriptionId);
								objBOQAreaMappingDto.setMeasurementId(measurementId);
								objBOQAreaMappingDto.setBlockId(blockId);
								objBOQAreaMappingDto.setBlockArea(new DecimalFormat("##.##").format(blockArea));
								objBOQAreaMappingDto.setTempBOQDetailsId(tempBOQDetailsId);
								objBOQAreaMappingDto.setFloorId(floorId);
								objBOQAreaMappingDto.setRatePerUnit(new DecimalFormat("##.##").format(ratePerUnit));
								objBOQAreaMappingDto.setBlockAreaAmount(new DecimalFormat("##.##").format(blockAreaAmount));
								objBOQAreaMappingDto.setTempBOQNo(tempBOQNo);
								objBOQAreaMappingDto.setAction(null);
								objBOQAreaMappingDto.setRecordType(recordType);
								objBOQAreaMappingDto.setWorkAreaGroupId(workAreaGroupId);
								ExecutorService executorService = Executors.newFixedThreadPool(10);
								try{executorService.execute(new Runnable() {public void run() {
									objBOQDao.insertQsTempBOQAreaMapping(objBOQAreaMappingDto);
								}});executorService.shutdown();}catch(Exception e){e.printStackTrace();executorService.shutdown();}
								*/
								if(recordType.equals("MATERIAL")){
									WDRateAnalysis objWDRA = RA_Sheet_Data.get(workDescription.toLowerCase()+"@@"+measurementName.toLowerCase()+"@@"+ratePerUnit);
									objBOQDao.insertQsTempBOQProductDtls(workAreaId,blockArea,objWDRA,boqSiteId,tempBOQNo,null);
								}
								additionOfAllIndividualBlockAreaAmounts += blockAreaAmount;
								currentCumulativeMajorHeadTotal += blockAreaAmount;
								GrandTotal += blockAreaAmount;
								if(recordType.equals("LABOR")){LaborTotal += blockAreaAmount; currentLaborMajorHeadTotal+=blockAreaAmount;}
								if(recordType.equals("MATERIAL")){MaterialTotal += blockAreaAmount; currentMaterialMajorHeadTotal+=blockAreaAmount;}
							}
						}
						if(Math.abs(givenTotalForVerification-additionOfAllIndividualBlockAreaAmounts)>0.001){
							response =  "VerificationFailed";
							System.out.println("given"+recordType+"TotalForVerification,additionOfAllIndividualBlockAreaAmounts: "+givenTotalForVerification+","+additionOfAllIndividualBlockAreaAmounts);
							errMsg.add("In line "+(rowNum+1));
							errMsg.add(recordType+" Amounts in Excel & IMS: "+givenTotalForVerification+","+additionOfAllIndividualBlockAreaAmounts);
							throw new RuntimeException("VerificationFailed");
						}
						/*********************************** LOOPING  EACH BLOCK - END ************************************/
						
					}
					TotalnoofWorkDescriptions += 1;
				}
				/**************************************** IF IT IS   BLOCK - END  ***************************************/
				
				if(headerName.contains("wd")&&headerName.contains("floor")){
					String levelOfWork = "F"; //Floor level
					if(StringUtils.isBlank(minorHeadDesc)||minorHeadId==null){
						errMsg.add("In BOQ sheet, Line no. "+(rowNum+1)+", MinorHead is not given for this work.");
						throw new RuntimeException("In BOQ sheet, Line no. "+(rowNum+1)+", MinorHead is not given for this work.");
					}
					workDescription = row.getCell(DescriptionColumnIndex).getStringCellValue().trim();
					//workDescriptionId = objBOQDao.getWorkDescriptionId(workDescription,minorHeadId,typeOfWork);
					workDescriptionId = WorkDescriptionIdsMap.get(minorHeadId)==null?null:WorkDescriptionIdsMap.get(minorHeadId).get(workDescription.toLowerCase());
					if(workDescriptionId==null){
						workDescriptionId = objBOQDao.getWorkDescriptionTblSeqId();
						objBOQDao.insertWorkDescription(workDescription,workDescriptionId,minorHeadId,user_id,levelOfWork,typeOfWork);
						if(WorkDescriptionIdsMap.get(minorHeadId)==null){
							WorkDescriptionIdsMap.put(minorHeadId,new HashMap<String,String>());
						}
						WorkDescriptionIdsMap.get(minorHeadId).put(workDescription.toLowerCase(),workDescriptionId);
					}
					if(StringUtils.isBlank(workDescription)||workDescriptionId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
					}
					TotalnoofWorkDescriptions += 1;
				}
				
				/************************************* IF IT IS  FLOOR - START  **************************************/
				if(headerName.contains("floor")&&!headerName.contains("wd")){
					Cell cell5 = row.getCell(LaborRatePerUnitColumnIndex);
					Cell cell9 = row.getCell(MaterialRatePerUnitColumnIndex);
					if (cell5 == null || cell5.getCellType() == Cell.CELL_TYPE_BLANK) {
						//errMsg.add("Labor Rate/Unit is Blank in Line "+(rowNum+1));
						//throw new RuntimeException("Labor Rate per Unit is Blank.");
						laborRatePerUnit = 0.0;
					}else{
						laborRatePerUnit = row.getCell(LaborRatePerUnitColumnIndex).getNumericCellValue();
					}
					if (cell9 == null || cell9.getCellType() == Cell.CELL_TYPE_BLANK) {
						//errMsg.add("Material Rate/Unit is Blank in Line "+(rowNum+1));
						//throw new RuntimeException("Material Rate per Unit is Blank.");
						materialRatePerUnit = 0.0;
					}else{
						materialRatePerUnit = row.getCell(MaterialRatePerUnitColumnIndex).getNumericCellValue();
					}
					if(laborRatePerUnit==0.0 && materialRatePerUnit==0.0){
						continue;
					}
					
					
					String floorName = row.getCell(DescriptionColumnIndex).getStringCellValue();
					if(floorName.contains(" - ")){
						floorName = floorName.split(" - ")[0];
					}
					floorName = floorName.trim().toLowerCase();
					Map<String,String> BlockIdAndFloorIdMap = FloorIdsMap.get(floorName);
					
					if(workDescriptionId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
					}
					measurementName = row.getCell(UnitOfMeasurementColumnIndex).getStringCellValue().trim();
					//measurementId = objBOQDao.getmeasurementId(measurementName,workDescriptionId,typeOfWork);
					measurementId = MeasurementIdsMap.get(workDescriptionId)==null?null:MeasurementIdsMap.get(workDescriptionId).get(measurementName.toLowerCase());
					if(measurementId==null){
						measurementId = objBOQDao.getMeasurementTblSeqId();
						objBOQDao.insertMeasurement(measurementId,measurementName,workDescriptionId,user_id,typeOfWork);
						if(MeasurementIdsMap.get(workDescriptionId)==null){
							MeasurementIdsMap.put(workDescriptionId,new HashMap<String,String>());
						}
						MeasurementIdsMap.get(workDescriptionId).put(measurementName.toLowerCase(),measurementId);
					}
					if(StringUtils.isBlank(measurementName)||measurementId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", UOM is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", UOM is Not given");
					}
					
					//laborRatePerUnit = row.getCell(LaborRatePerUnitColumnIndex).getNumericCellValue();
					//materialRatePerUnit = row.getCell(MaterialRatePerUnitColumnIndex).getNumericCellValue();
					if(laborRatePerUnit!=0.0){givenLaborWork = true;}else{givenLaborWork = false;}
					if(materialRatePerUnit!=0.0){givenMaterialWork = true;}else{givenMaterialWork = false;}
					
					Cell cell2 = row.getCell(laborTotalColumnIndex);
					Cell cell10 = row.getCell(materialTotalColumnIndex);
					Cell cell11 = row.getCell(cumulativeTotalColumnIndex);
					if(cell2.getCellType() == Cell.CELL_TYPE_FORMULA || cell10.getCellType() == Cell.CELL_TYPE_FORMULA){
						response =  "FormulaProblem";
						throw new RuntimeException("FormulaProblem");
					}
					
					double givenLaborTotalForVerification = cell2.getNumericCellValue();
					double givenMaterialTotalForVerification = cell10.getNumericCellValue();
					double givenCumulativeTotalForVerification = cell11.getNumericCellValue();
					
					double totalAreaOfAllBlocks = 0.0;
					for(int index : blockColumnIndexList){
						double blockArea = 0.0;
						Cell cell1 = row.getCell(index,Row.RETURN_BLANK_AS_NULL);
						if (cell1 == null || cell1.getCellType() == Cell.CELL_TYPE_BLANK) {
							continue;
						}
						else{
							if(cell1.getCellType() == Cell.CELL_TYPE_NUMERIC){
								blockArea = cell1.getNumericCellValue();
							}
							else{
								blockArea = 0.0;
							}

							totalAreaOfAllBlocks += blockArea;
						}
					}
					
					double totalLaborAmountForAllBlocks = totalAreaOfAllBlocks*laborRatePerUnit;
					double totalMaterialAmountForAllBlocks = totalAreaOfAllBlocks*materialRatePerUnit;
					double totalCumulativeAmountForAllBlocks = totalLaborAmountForAllBlocks+totalMaterialAmountForAllBlocks;
					for(int i=1;i<=3;i++){
						String xxx = "";
						double xxxTotalinExcel = 0.0;
						double xxxTotal = 0.0;
						if(i==1){ xxx = "Material"; xxxTotalinExcel = givenMaterialTotalForVerification; 	xxxTotal = totalMaterialAmountForAllBlocks;}
						if(i==2){ xxx = "Labor"; 	xxxTotalinExcel = givenLaborTotalForVerification; 		xxxTotal = totalLaborAmountForAllBlocks;}
						if(i==3){ xxx = "Cumulative"; 	xxxTotalinExcel = givenCumulativeTotalForVerification; 	xxxTotal = totalCumulativeAmountForAllBlocks;}

						if(Math.abs(xxxTotalinExcel-xxxTotal)>0.001){
							response =  "VerificationFailed";
							System.out.println("given"+xxx+"TotalForVerification & IMS value: "+xxxTotalinExcel+","+xxxTotal);
							errMsg.add("In line "+(rowNum+1));
							errMsg.add(xxx+" Amounts in Excel & IMS: "+xxxTotalinExcel+","+xxxTotal);
							throw new RuntimeException("VerificationFailed");
						}
					}
					
					for(int recordTypeNo=1;recordTypeNo<=2;recordTypeNo++){
						String recordType = null;
						double ratePerUnit = 0.0;
						double totalAmountForAllBlocks = 0.0;
						double givenTotalForVerification = 0.0;
						if(recordTypeNo==1 && givenLaborWork){
							recordType = "LABOR";
							ratePerUnit = laborRatePerUnit;
							totalAmountForAllBlocks = totalLaborAmountForAllBlocks;
							givenTotalForVerification = givenLaborTotalForVerification;
						}
						else if(recordTypeNo==2 && givenMaterialWork){
							recordType = "MATERIAL";
							ratePerUnit = materialRatePerUnit;
							totalAmountForAllBlocks = totalMaterialAmountForAllBlocks;
							givenTotalForVerification = givenMaterialTotalForVerification;
						}
						else {continue;}

						//checking for safety
						//if(recordType==null || ratePerUnit == 0.0 || totalAmountForAllBlocks == 0.0 || givenTotalForVerification == 0.0){throw new RuntimeException("recordType="+recordType+",ratePerUnit="+ratePerUnit+",totalAmountForAllBlocks="+totalAmountForAllBlocks+",givenTotalForVerification="+givenTotalForVerification);}



						int tempBOQDetailsId = objBOQDao.getTempBOQDetailsId(workDescriptionId,tempBOQNo,measurementId,recordType);
						if(tempBOQDetailsId==0){
							tempBOQDetailsId = objBOQDao.getTempBOQDetailsId();
							objBOQDao.insertQsTempBOQDetails(tempBOQDetailsId,workDescriptionId,measurementId,totalAreaOfAllBlocks,totalAmountForAllBlocks,tempBOQNo,scopeOfWork,minorHeadId,null,recordType);
							/*final BOQDetailsDto objBOQDetailsDto = new BOQDetails();
							objBOQDetailsDto.setTempBOQDetailsId(tempBOQDetailsId);
							objBOQDetailsDto.setWorkDescriptionId(workDescriptionId);
							objBOQDetailsDto.setMeasurementId(measurementId);
							objBOQDetailsDto.setTotalAreaOfAllBlocks(new DecimalFormat("##.##").format(totalAreaOfAllBlocks));
							objBOQDetailsDto.setTempBOQNo(tempBOQNo);
							objBOQDetailsDto.setTotalAmountForAllBlocks(new DecimalFormat("##.##").format(totalAmountForAllBlocks));
							objBOQDetailsDto.setScopeOfWork(scopeOfWork);
							objBOQDetailsDto.setMinorHeadId(minorHeadId);
							objBOQDetailsDto.setAction(null);
							objBOQDetailsDto.setRecordType(recordType);
							ExecutorService executorService = Executors.newFixedThreadPool(10);
							try{executorService.execute(new Runnable() {public void run() {
								objBOQDao.insertQsTempBOQDetails(objBOQDetailsDto);
							}});executorService.shutdown();}catch(Exception e){e.printStackTrace();executorService.shutdown();}
							*/
						}
						else{
							System.out.println("totalAreaOfAllBlocks:"+totalAreaOfAllBlocks+"Line "+(rowNum+1));
							objBOQDao.updateQsTempBOQDetails(tempBOQDetailsId,totalAreaOfAllBlocks,totalAmountForAllBlocks);
							/*final BOQDetailsDto objBOQDetailsDto = new BOQDetails();
							objBOQDetailsDto.setTempBOQDetailsId(tempBOQDetailsId);
							objBOQDetailsDto.setDoubleTotalAreaOfAllBlocks(totalAreaOfAllBlocks);
							objBOQDetailsDto.setDoubleTotalAmountForAllBlocks(totalAmountForAllBlocks);
							ExecutorService executorService = Executors.newFixedThreadPool(10);
							try{executorService.execute(new Runnable() {public void run() {
								objBOQDao.updateQsTempBOQDetails(objBOQDetailsDto);
							}});executorService.shutdown();}catch(Exception e){e.printStackTrace();executorService.shutdown();}
							*/
						}
						/********************************* LOOPING  EACH BLOCK - START  ************************************/
						double additionOfAllIndividualBlockAreaAmounts = 0;
						for(int index : blockColumnIndexList){
							Cell cell1 = row.getCell(index,Row.RETURN_BLANK_AS_NULL);
							if(cell1 != null && cell1.getCellType() == Cell.CELL_TYPE_NUMERIC){
								double blockArea = cell1.getNumericCellValue();
								if(blockArea==0.0){continue;}
								double blockAreaAmount = blockArea*ratePerUnit;
								String blockId = blockIds.get(index);
								String floorId = BlockIdAndFloorIdMap==null?null:BlockIdAndFloorIdMap.get(blockId);
								if(floorId==null){
									errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) +", Floor name '"+ floorName+"' not found in Master Data, Please Check once.");
									throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) +", Floor name '"+ floorName+"' not found in Master Data, Please Check once.");
								}
								String workAreaId = objBOQDao.getWorkAreaSeqId();
								String workAreaGroupId = null;
								String currentWorkGroup = workDescriptionId+"@@"+blockId+"@@"+floorId;
								if(workGroupList.containsKey(currentWorkGroup)){
									workAreaGroupId = workGroupList.get(currentWorkGroup);
								}
								else{
									workAreaGroupId = objBOQDao.getWorkAreaGroupSeqId();
									workGroupList.put(currentWorkGroup,workAreaGroupId);
								}
								if(blockWiseWorkList.contains(workDescriptionId)){
									errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",This work is given already in Block wise.");
									throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+",This work is given already in Block wise.");
								}
								else{
									floorWiseWorkList.add(workDescriptionId);
								}
								String currentWork = recordType+"@@"+workDescriptionId+"@@"+blockId+"@@"+floorId;
								if(workList.contains(currentWork)){
									errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",Duplicate Work Found.");
									throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+"Duplicate Work Found");
								}
								else{
									workList.add(currentWork);
								}
								objBOQDao.insertQsTempBOQAreaMapping(workAreaId,workDescriptionId,measurementId,blockId,blockArea,tempBOQDetailsId,blockAreaAmount,floorId,ratePerUnit,tempBOQNo,null,recordType,workAreaGroupId);
								/*final BOQAreaMappingDto objBOQAreaMappingDto = new BOQAreaMappingDto(); 
								objBOQAreaMappingDto.setWorkAreaId(workAreaId);
								objBOQAreaMappingDto.setWorkDescriptionId(workDescriptionId);
								objBOQAreaMappingDto.setMeasurementId(measurementId);
								objBOQAreaMappingDto.setBlockId(blockId);
								objBOQAreaMappingDto.setBlockArea(new DecimalFormat("##.##").format(blockArea));
								objBOQAreaMappingDto.setTempBOQDetailsId(tempBOQDetailsId);
								objBOQAreaMappingDto.setFloorId(floorId);
								objBOQAreaMappingDto.setRatePerUnit(new DecimalFormat("##.##").format(ratePerUnit));
								objBOQAreaMappingDto.setBlockAreaAmount(new DecimalFormat("##.##").format(blockAreaAmount));
								objBOQAreaMappingDto.setTempBOQNo(tempBOQNo);
								objBOQAreaMappingDto.setAction(null);
								objBOQAreaMappingDto.setRecordType(recordType);
								objBOQAreaMappingDto.setWorkAreaGroupId(workAreaGroupId);
								ExecutorService executorService = Executors.newFixedThreadPool(10);
								try{executorService.execute(new Runnable() {public void run() {
									objBOQDao.insertQsTempBOQAreaMapping(objBOQAreaMappingDto);
								}});executorService.shutdown();}catch(Exception e){e.printStackTrace();executorService.shutdown();}
								*/
								if(recordType.equals("MATERIAL")){
									WDRateAnalysis objWDRA = RA_Sheet_Data.get(workDescription.toLowerCase()+"@@"+measurementName.toLowerCase()+"@@"+ratePerUnit);
									objBOQDao.insertQsTempBOQProductDtls(workAreaId,blockArea,objWDRA,boqSiteId,tempBOQNo,null);
								}
								additionOfAllIndividualBlockAreaAmounts += blockAreaAmount;
								currentCumulativeMajorHeadTotal += blockAreaAmount;
								GrandTotal += blockAreaAmount;
								if(recordType.equals("LABOR")){LaborTotal += blockAreaAmount; currentLaborMajorHeadTotal+=blockAreaAmount;}
								if(recordType.equals("MATERIAL")){MaterialTotal += blockAreaAmount; currentMaterialMajorHeadTotal+=blockAreaAmount;}
							}
						}
						if(Math.abs(givenTotalForVerification-additionOfAllIndividualBlockAreaAmounts)>0.001){
							response =  "VerificationFailed";
							System.out.println("given"+recordType+"TotalForVerification,additionOfAllIndividualBlockAreaAmounts: "+givenTotalForVerification+","+additionOfAllIndividualBlockAreaAmounts);
							errMsg.add("In line "+(rowNum+1));
							errMsg.add(recordType+" Amounts in Excel & IMS: "+givenTotalForVerification+","+additionOfAllIndividualBlockAreaAmounts);
							throw new RuntimeException("VerificationFailed");
						}
						/********************************* LOOPING  EACH BLOCK - END ************************************/

					}
				}
				/************************************* IF IT IS  FLOOR - END  **************************************/
				if(headerName.contains("major")&&headerName.contains("head")&&headerName.contains("total")){
					for(int i=1;i<=3;i++){
						String xxx = "";
						int xxxTotalColumnIndex = -1;
						double xxxTotal = 0.0;
						if(i==1){ xxx = "Material"; xxxTotalColumnIndex = materialTotalColumnIndex; 	xxxTotal = currentMaterialMajorHeadTotal;}
						if(i==2){ xxx = "Labor"; 	xxxTotalColumnIndex = laborTotalColumnIndex; 		xxxTotal = currentLaborMajorHeadTotal;}
						if(i==3){ xxx = "Cumulative"; 	xxxTotalColumnIndex = cumulativeTotalColumnIndex; 	xxxTotal = currentCumulativeMajorHeadTotal;}
						Cell cell2 = row.getCell(xxxTotalColumnIndex);
						if(cell2.getCellType() == Cell.CELL_TYPE_FORMULA){
							response =  "FormulaProblem";
							throw new RuntimeException("FormulaProblem");
						}
						double givenxxxMajorHeadTotalForVerification = cell2.getNumericCellValue();
						if(Math.abs(givenxxxMajorHeadTotalForVerification-xxxTotal)>0.001){
							response =  "VerificationFailed";
							System.out.println("given"+xxx+"MajorHeadTotalForVerification,IMS "+xxx+"MajorHeadTotal: "+givenxxxMajorHeadTotalForVerification+","+xxxTotal);
							errMsg.add("Check In MajorHead '"+majorHeadDesc+"', WD(BLOCK) or FLOOR spellings maybe incorrect (or) not given.");
							errMsg.add("MajorHeadTotal Amounts in Excel & IMS: "+givenxxxMajorHeadTotalForVerification+","+xxxTotal);
							throw new RuntimeException("VerificationFailed");
						}
						System.out.println("givenMajorHeadTotalForVerification,currentMajorHeadTotal: "+givenxxxMajorHeadTotalForVerification+","+xxxTotal);
					}
					currentCumulativeMajorHeadTotal = 0.0;
					currentLaborMajorHeadTotal = 0.0;
					currentMaterialMajorHeadTotal = 0.0;
				}
				long stopTime1 = System.currentTimeMillis();
			    long elapsedTime1 = stopTime1 - startTime1;
			    System.out.println("final elapsed time: "+elapsedTime1);
				if(headerName.contains("grand")&&headerName.contains("total")){
					isGrandTotalVerified = true;
					double givenMaterialTotalForVerification = 0.0;
					double givenLaborTotalForVerification = 0.0;
					double givenGrandTotalForVerification = 0.0;
					for(int i=1;i<=3;i++){
						String xxx = "";
						int xxxTotalColumnIndex = -1;
						double xxxTotal = 0.0;
						if(i==1){ xxx = "Material"; xxxTotalColumnIndex = materialTotalColumnIndex; 	xxxTotal = MaterialTotal;}
						if(i==2){ xxx = "Labor"; 	xxxTotalColumnIndex = laborTotalColumnIndex; 		xxxTotal = LaborTotal;}
						if(i==3){ xxx = "Grand"; 	xxxTotalColumnIndex = cumulativeTotalColumnIndex; 	xxxTotal = GrandTotal;}
						Cell cell2 = row.getCell(xxxTotalColumnIndex);
						if(cell2.getCellType() == Cell.CELL_TYPE_FORMULA){
							response =  "FormulaProblem";
							throw new RuntimeException("FormulaProblem");
						}
						double givenxxxTotalForVerification = cell2.getNumericCellValue();
						if(i==1){ givenMaterialTotalForVerification=givenxxxTotalForVerification;}
						if(i==2){ givenLaborTotalForVerification=givenxxxTotalForVerification;}
						if(i==3){ givenGrandTotalForVerification=givenxxxTotalForVerification;}
						if(Math.abs(givenxxxTotalForVerification-xxxTotal)>0.001){
							response =  "VerificationFailed";
							System.out.println("given"+xxx+"TotalForVerification,"+xxx+"Total: "+givenxxxTotalForVerification+","+xxxTotal);
							errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", "+xxx+" Total Does not Match");
							throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", "+xxx+" Total Does not Match");
						}
						
					}
					objBOQDao.updateGrandTotalInQsTempBOQ(tempBOQNo,givenGrandTotalForVerification,givenLaborTotalForVerification,givenMaterialTotalForVerification);
				}
				
			}//for - end
			
			if(!isGrandTotalVerified){
				errMsg.add("GrandTotal Not Given for IMS code In Excel");
				throw new RuntimeException("GrandTotal Not Given In Excel");
			}
			errMsg.add("Temporary BOQ No: "+tempBOQNo);
			errMsg.add("Total no.of Major Heads: "+TotalnoofMajorHeads);
			errMsg.add("Total no.of Minor Heads: "+TotalnoofMinorHeads);
			errMsg.add("Total no.of WorkDescriptions: "+TotalnoofWorkDescriptions);
			
			response = "Success";
			System.out.println("response: "+response);
			transactionManager.commit(status);
			System.out.println("Committed");
			
		}
		catch(Exception e){
			transactionManager.rollback(status);
			System.out.println("Rollbacked");
			if(response==null){
				response = "Exception";
			}
			System.out.println("response: "+response);
			if(errMsg.size()==0){errMsg.add("Exception at Excel line no. "+(rowNumMail+1));}
			mailException(e,rowNumMail+1,ex_list);
			e.printStackTrace();
		}
		return response;
	}



	



	@Override
	public String createBlockIdAndFloorId(String user_id, String site_id) {
		String[] blockNames = new String[]{"Block A","Block B","Block C","Block D","Block E","Block F","Block G","Block H","Block I","Block J","Block L","Block K","Ext. area [M]"};
		String[] floorNames = new String[]{"Basement 2","Basement 1","Ground floor","1st floor","2nd floor","3rd floor","4th floor","5th floor","6th floor","7th floor","8th floor","9th floor","10th floor","11th floor","12th floor","13th floor","14th floor","Above Terrace","Other Area"};
		
		int maxBlockId = objBOQDao.getMaxBlockIdInBlockTable();
		int maxFloorId = objBOQDao.getMaxFloorIdInFloorTable();
		System.out.println("maxBlockId: "+maxBlockId);
		System.out.println("maxFloorId: "+maxFloorId);
		for(String blockName : blockNames){
			int blockId = objBOQDao.getBlockIdIfItIsThere(blockName,site_id);
			if(blockId==0){
				maxBlockId += 1;
				blockId = maxBlockId;
				System.out.println(blockId+","+blockName+","+site_id);
				objBOQDao.createBlockId(blockId,blockName,site_id);
			}
			for(String floorName : floorNames){
				int floorId = objBOQDao.getFloorIdIfItIsThere(floorName,blockId);
				if(floorId==0){
					maxFloorId += 1;
					floorId = maxFloorId;
					System.out.println(floorId+","+floorName+","+blockId);
					objBOQDao.createFloorId(floorId,floorName,blockId);
				}
			}
		}
		return null;
	}

	
	@Override
	public List<BOQBean> getPendingForApprovalBOQList(HttpServletRequest request,HttpSession session, boolean isViewTempBoq) {
		
		String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String siteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		return objBOQDao.getPendingForApprovalBOQList(strUserId,siteId,isViewTempBoq);
	}
	@Override
	public List<BOQBean> getSiteWisePendingForApprovalBOQList(HttpServletRequest request,HttpSession session,String siteId,boolean isViewTempBoq ) {
		
		String strUserId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		return objBOQDao.getSiteWisePendingForApprovalBOQList(strUserId,siteId, isViewTempBoq );
	}
	@Override
	public BOQBean getBOQFromAndToDetails(BOQBean objBOQBean, String userId) {
		
		return objBOQDao.getBOQFromAndToDetails(objBOQBean,userId);
	}
	
	@Override
	public List<BOQBean> getBOQForApprovalByID(HttpServletRequest request, HttpSession session) {
		String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
		String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
		BOQBean bean=new BOQBean();
		bean.setStrSiteId(siteId);
		bean.setStrUserId(userId);
		bean.setIntTempBOQNo(Integer.valueOf(tempBOQNumber));
	return 	objBOQDao.getBOQForApprovalByID(bean);
		}
	
	@Override
	public List<BOQBean> getBOQWorkDetails(HttpServletRequest request, HttpSession session, String typeOfWork) {
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
		String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
	
		return		objBOQDao.getBOQWorkDetails(siteId,tempBOQNumber,typeOfWork);
		
	}
	@Override
	public List<BOQBean> getReviseBOQWorkDetails(HttpServletRequest request, HttpSession session, String typeOfWork, String recordType, String majorHeadId) {
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
		String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
	
		return		objBOQDao.getReviseBOQWorkDetails(siteId,tempBOQNumber,typeOfWork,recordType,majorHeadId);
		
	}
	@Override
	public List<BOQBean> getReviseBOQChangedWorkDetails(HttpServletRequest request, HttpSession session, String typeOfWork,String tempBOQNumber,String recordType,String majorHeadId) {
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
		//String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
	
		return		objBOQDao.getReviseBOQChangedWorkDetails(siteId,tempBOQNumber,typeOfWork,recordType,majorHeadId);
		
	}
	@Override
	public List<BOQBean> getReviseBOQChangedWorkDetailsBasedOnVerNo(HttpServletRequest request, HttpSession session, String typeOfWork) {
		int BOQSeqNo =  Integer.parseInt(request.getParameter("BOQSeqNo"));
		String siteId =  request.getParameter("siteId");
		String versionNo = request.getParameter("versionNo");
		
		return		objBOQDao.getReviseBOQChangedWorkDetailsBasedOnVerNo(siteId,BOQSeqNo,versionNo,typeOfWork);
		
	}
	@Override
	public List<BOQBean> getSOWChangedWorkDetails(HttpServletRequest request, HttpSession session, String typeOfWork,String tempBOQNumber) {
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
		//String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
	
		return		objBOQDao.getSOWChangedWorkDetails(siteId,tempBOQNumber,typeOfWork);
		
	}
	@Override
	public List<BOQBean> getBOQData(HttpServletRequest request, HttpSession session) {
		String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
		String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
		String	viewTempBoq = request.getParameter("viewTempBoq") == null ? "" : request.getParameter("viewTempBoq");
		BOQBean bean=new BOQBean();
		bean.setStrSiteId(siteId);
		bean.setStrUserId(userId);
		bean.setIntTempBOQNo(Integer.valueOf(tempBOQNumber));
		bean.setViewTempBoq(viewTempBoq);
	return	objBOQDao.getBOQData(bean);
	}
	@Override
	public String getBOQLevelComments(HttpServletRequest request) {
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
		String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
		BOQBean bean=new BOQBean();
		bean.setStrSiteId(siteId);
		bean.setIntTempBOQNo(Integer.valueOf(tempBOQNumber));
		return  objBOQDao.getBOQLevelComments(bean);
		
	}
	
	@Override
	public String approveTempBOQ(HttpServletRequest request, HttpSession session,BOQBean objForOnlyPermanentBoqNumber) {

		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		List<Exception> ex_list = new ArrayList<Exception>();
		String note="Note";
		String actualNote="Note";	
		String responce="";
		try {
			 note=request.getParameter("Note") == null ? "" : request.getParameter("Note");
			 actualNote=request.getParameter("actualNote") == null ? "" : request.getParameter("actualNote");
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String siteName = request.getParameter("SiteName") == null ? "" : request.getParameter("SiteName").toString();
			String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
			String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
			String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
			String nextLevelApproverEmpId=request.getParameter("nextLevelApproverEmpId")==null?"":request.getParameter("nextLevelApproverEmpId").toString();
			String boqApproverEmpId=request.getParameter("boqApproverEmpId")==null?"":request.getParameter("boqApproverEmpId").toString();
			String boqCreatedEmpId=request.getParameter("boqCreatedEmpId")==null?"":request.getParameter("boqCreatedEmpId").toString();
			String typeOfWork=request.getParameter("typeOfWork");
			
			/*boolean isBoqAlreadyPresent = objBOQDao.isBoqAlreadyPresent(siteId,typeOfWork);
			if(isBoqAlreadyPresent){
				throw new RuntimeException("BOQ is already uploaded for this Site");
			}*/
			
			BOQBean bean=new BOQBean();
			bean.setTypeOfWork(typeOfWork);
			bean.setStrSiteId(siteId);
			bean.setStrSiteName(siteName);
			bean.setStrUserId(userId);
			bean.setIntTempBOQNo(Integer.valueOf(tempBOQNumber));
			bean.setStrApproverEmpId(nextLevelApproverEmpId);
			bean.setStrTepBOQCreatedEmployeId(boqCreatedEmpId);
			bean.setStrTempBOQApproveEmployeeId(boqApproverEmpId);
			bean.setStrOperationType("A");
			int portNumber=request.getLocalPort();
			bean.setPortNumber(portNumber);
			if(!note.equals(actualNote)){
				//note = actualNote + "@@" + strUserName + " - " + note + "@@";
				note = strUserName + " - " + note ;
				bean.setStrRemarks(note);
			}
			
			int result=objBOQDao.approveRejectDeatilsForBOQ(bean);
			
			
			result=objBOQDao.approveTempBOQ(bean,objForOnlyPermanentBoqNumber);
			if(StringUtils.isNotBlank(objForOnlyPermanentBoqNumber.getStrBOQNo())){
				objBOQDao.finalValidationOfGrandTotalInEveryTable(objForOnlyPermanentBoqNumber.getStrBOQNo()); 
			}
			//String s=null;s.trim();
			transactionManager.commit(status);
			 responce="success";
		} catch (Exception e) {
			transactionManager.rollback(status);
			mailException(e,0,ex_list);
			e.printStackTrace();
			 responce="failed";

		}
		return responce;
	}
	@Override
	public String rejectTempBOQ(HttpServletRequest request, HttpSession session) {
		String responce="";
		BOQBean bean=new BOQBean();
		List<Exception> ex_list = new ArrayList<Exception>();
		
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		String note="Note";
		String actualNote="Note";
		
		try {
			note=request.getParameter("Note") == null ? "" : request.getParameter("Note");
			actualNote=request.getParameter("actualNote") == null ? "" : request.getParameter("actualNote");
			
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
			String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
			String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
			String nextLevelApproverEmpId=request.getParameter("nextLevelApproverEmpId")==null?"":request.getParameter("nextLevelApproverEmpId").toString();
			String boqApproverEmpId=request.getParameter("boqApproverEmpId")==null?"":request.getParameter("boqApproverEmpId").toString();
			String boqCreatedEmpId=request.getParameter("boqCreatedEmpId")==null?"":request.getParameter("boqCreatedEmpId").toString();
			
			bean.setStrSiteId(siteId);
			bean.setStrUserId(userId);
			bean.setIntTempBOQNo(Integer.valueOf(tempBOQNumber));
			bean.setStrApproverEmpId(nextLevelApproverEmpId);
			bean.setStrTepBOQCreatedEmployeId(boqCreatedEmpId);
			bean.setStrTempBOQApproveEmployeeId(boqApproverEmpId);
			bean.setStrOperationType("R");
			if(!note.equals(actualNote)){
				//note = actualNote + "@@" + strUserName + " - " + note + "@@";
				note = strUserName + " - " + note ;
				bean.setStrRemarks(note);
			}
			int result=objBOQDao.approveRejectDeatilsForBOQ(bean);
			result=objBOQDao.rejectTempBOQ(bean);
			objBOQDao.deleteAllRejectedTempBoqRecordsInDB(bean.getIntTempBOQNo());
			transactionManager.commit(status);
			responce="success";
		} catch (Exception e) {
			transactionManager.rollback(status);
			e.printStackTrace();
			ex_list.add(e);
			 responce="success";
		}
		return responce;
	}
	
	
		@Override
	public List<BOQBean> getMyBOQList(HttpServletRequest request,HttpSession session) {
		
		String strSiteId = session.getAttribute("SiteId") == null ? "" : session.getAttribute("SiteId").toString();
		//String strBONo = request.getParameter("")
		return objBOQDao.getMyBOQList(strSiteId);
	}
		@Override
		public List<BOQBean> getSitewiseBOQList(HttpServletRequest request,HttpSession session) {
			
			String strSiteId = request.getParameter("dropdown_SiteId");
			
			return objBOQDao.getMyBOQList(strSiteId);
		}
	
	@Override
	public List<BOQBean> getBOQWorkDetails(HttpServletRequest request, BOQBean ObjBOQDetails/*ObjBOQDetails.getTypeOfWork()*//*String typeOfWork*/){
		int intBOQSeqNumber =  Integer.parseInt(request.getParameter("BOQSeqNo"));
		String strSiteId =  request.getParameter("siteId");
		String majorHead = request.getParameter("combobox_Product");
		String minorHead = request.getParameter("combobox_SubProduct");
		String workDescription = request.getParameter("combobox_ChildProduct");
		List<BOQBean> list = objBOQDao.getBOQWorkDetails(intBOQSeqNumber,strSiteId,ObjBOQDetails,majorHead,minorHead,workDescription);
		return list;
	}
	
	@Override
	public List<BOQBean> getTempBOQWorkDetails(HttpServletRequest request, BOQBean ObjBOQDetails/*ObjBOQDetails.getTypeOfWork()*//*String typeOfWork*/){
		int intBOQSeqNumber =  Integer.parseInt(request.getParameter("tempBOQNo"));
		String strSiteId =  request.getParameter("siteId");
		String majorHead = request.getParameter("combobox_Product");
		String minorHead = request.getParameter("combobox_SubProduct");
		String workDescription = request.getParameter("combobox_ChildProduct");
		List<BOQBean> list = objBOQDao.getTempBOQWorkDetails(intBOQSeqNumber,strSiteId,ObjBOQDetails,majorHead,minorHead,workDescription);
		return list;
	}
	
	@Override
	public List<BOQBean> getBOQMaterialDetails(HttpServletRequest request, BOQBean ObjBOQDetails){
		int intBOQSeqNumber =  Integer.parseInt(request.getParameter("BOQSeqNo"));
		String workAreaId =  request.getParameter("workAreaId");
		
		/*String strSiteId =  request.getParameter("siteId");
		String majorHead = request.getParameter("combobox_Product");
		String minorHead = request.getParameter("combobox_SubProduct");
		String workDescription = request.getParameter("combobox_ChildProduct");*/
		List<BOQBean> list = objBOQDao.getBOQMaterialDetails(intBOQSeqNumber,workAreaId,ObjBOQDetails);
		return list;
	}
	@Override
	public List<BOQBean> getTempBOQMaterialDetails(HttpServletRequest request, BOQBean ObjBOQDetails){
		int tempBOQNo =  Integer.parseInt(request.getParameter("tempBOQNo"));
		String workAreaId =  request.getParameter("workAreaId");
		
		/*String strSiteId =  request.getParameter("siteId");
		String majorHead = request.getParameter("combobox_Product");
		String minorHead = request.getParameter("combobox_SubProduct");
		String workDescription = request.getParameter("combobox_ChildProduct");*/
		List<BOQBean> list = objBOQDao.getTempBOQMaterialDetails(tempBOQNo,workAreaId,ObjBOQDetails);
		return list;
	}
	@Override
	public List<Map<String, Object>> getBOQMajorHeads(HttpServletRequest request, String typeOfWork){
		int intBOQSeqNumber =  Integer.parseInt(request.getParameter("BOQSeqNo"));
		String strSiteId =  request.getParameter("siteId");
		List<Map<String, Object>> list = objBOQDao.getBOQMajorHeads(intBOQSeqNumber,strSiteId,typeOfWork);
		return list;
	}
	@Override
	public List<Map<String, Object>> getTempBOQMajorHeads(HttpServletRequest request, String typeOfWork){
		int	tempBOQNumber = request.getParameter("tempBOQNo") == null ? 0 : Integer.parseInt(request.getParameter("tempBOQNo").toString());
		String strSiteId =  request.getParameter("siteId");
		List<Map<String, Object>> list = objBOQDao.getTempBOQMajorHeads(tempBOQNumber,strSiteId,typeOfWork);
		return list;
	}
	@Override
	public List<BOQBean> getBOQMajorHeadsDetails(HttpServletRequest request, String typeOfWork){
		int intBOQSeqNumber =  Integer.parseInt(request.getParameter("BOQSeqNo"));
		String strSiteId =  request.getParameter("siteId");
		List<BOQBean> list = objBOQDao.getBOQMajorHeadsDetails(intBOQSeqNumber,strSiteId,typeOfWork);
		return list;
	}
	@Override
	public List<BOQBean> getTempBOQMajorHeadsDetails(HttpServletRequest request, String typeOfWork){
		int tempBOQNo =  Integer.parseInt(request.getParameter("tempBOQNo"));
		String strSiteId =  request.getParameter("siteId");
		List<BOQBean> list = objBOQDao.getTempBOQMajorHeadsDetails(tempBOQNo,strSiteId,typeOfWork);
		return list;
	}
	@Override
	public List<BOQBean> getBOQMinorHeadsDetails(HttpServletRequest request, String typeOfWork){
		int intBOQSeqNumber =  Integer.parseInt(request.getParameter("BOQSeqNo"));
		String majorHeadId =  request.getParameter("majorHeadId");
		String strSiteId =  request.getParameter("siteId");
		List<BOQBean> list = objBOQDao.getBOQMinorHeadsDetails(intBOQSeqNumber,strSiteId,typeOfWork,majorHeadId);
		return list;
	}
	@Override
	public List<BOQBean> getTempBOQMinorHeadsDetails(HttpServletRequest request, String typeOfWork){
		int tempBOQNo =  Integer.parseInt(request.getParameter("tempBOQNo"));
		String majorHeadId =  request.getParameter("majorHeadId");
		String strSiteId =  request.getParameter("siteId");
		List<BOQBean> list = objBOQDao.getTempBOQMinorHeadsDetails(tempBOQNo,strSiteId,typeOfWork,majorHeadId);
		return list;
	}
	
	@Override
	public BOQBean getBOQDetails(HttpServletRequest request){
		int intBOQSeqNumber =  Integer.parseInt(request.getParameter("BOQSeqNo"));
		String strSiteId =  request.getParameter("siteId");
		BOQBean objBOQDetails = objBOQDao.getBOQDetails(intBOQSeqNumber,strSiteId);

		return objBOQDetails;
	}
	@Override
	public BOQBean getBOQDetailsFromBackup(HttpServletRequest request){
		int intBOQSeqNumber =  Integer.parseInt(request.getParameter("BOQSeqNo"));
		String strSiteId =  request.getParameter("siteId");
		String versionNo = request.getParameter("versionNo");
		BOQBean objBOQDetails = objBOQDao.getBOQDetailsFromBackup(intBOQSeqNumber,strSiteId,versionNo);

		return objBOQDetails;
	}
	@Override
	public String getSiteNameBySiteId(String siteId){
		return objBOQDao.getSiteNameBySiteId(siteId);
	}
	@SuppressWarnings("resource")
	@Override
	public String saveNMR(MultipartFile multipartFile,List<String> errMsg,String user_id, String boqSiteId, List<String> tempBOQNoForController) throws IOException {

		String typeOfWork = "NMR";
		String response = null;
		List<Exception> ex_list = new ArrayList<Exception>();
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try{
			InputStream inp =  new BufferedInputStream(multipartFile.getInputStream());

			Workbook wb = null;
			try {
				wb = WorkbookFactory.create(inp);
			} catch (Exception e) {
				e.printStackTrace();
				ex_list.add(e);
				mailException(e,0,ex_list);
			}
			Sheet sheet = wb.getSheet("NMR");
			boolean isBoqAlreadyPresent = objBOQDao.isBoqAlreadyPresent(boqSiteId,typeOfWork);
			if(isBoqAlreadyPresent){
				errMsg.add("BOQ is already uploaded for this Site");
				throw new RuntimeException("BOQ is already uploaded for this Site");
			}
			int tempBOQNo = objBOQDao.getTempBOQNo();
			tempBOQNoForController.add(String.valueOf(tempBOQNo));
			String pendingEmpId = objBOQDao.getPendingEmpId(user_id,boqSiteId);
			
			DataFormatter formatter = new DataFormatter();
			int rowsCount = sheet.getLastRowNum();
			//rowsCount = 427;
			
			int MajorHeadColumnIndex = 0;
			int MinorHeadColumnIndex = 1;
			int WorkDescriptionColumnIndex = 2;
			int UnitOfMeasurementColumnIndex = 3;
			int NMRGrandTotalColumnIndex = 1;
			
			int NMRGrandTotalRowNumber = 0;
			int DataStartingRowNumber = 2;
			
			
			//int err =10/0;
			//System.out.print(err);
			
			String majorHeadDesc = null;
			String majorHeadId = null;
			String minorHeadDesc = null;
			String minorHeadId = null;
			String scopeOfWork = null;
			String workDescription = null;
			String workDescriptionId = null;
			String measurementName = null;
			String measurementId = null;
			
			int TotalnoofMajorHeads = 0;
			int TotalnoofMinorHeads = 0;
			int TotalnoofWorkDescriptions = 0;
			
			List<String> workList = new ArrayList<String>();
			
			Row row = null;
			row = sheet.getRow(NMRGrandTotalRowNumber);
			Cell cell5 = row.getCell(NMRGrandTotalColumnIndex,Row.RETURN_BLANK_AS_NULL);
			if (cell5 == null || cell5.getCellType() == Cell.CELL_TYPE_BLANK) {
				errMsg.add("NMR GrandTotal not given in Line "+(NMRGrandTotalColumnIndex+1));
				throw new RuntimeException("Major Head is Blank.");
			}
			double nmrGrandTotal = row.getCell(NMRGrandTotalColumnIndex).getNumericCellValue();
			objBOQDao.insertQsTempBOQ(tempBOQNo,user_id,boqSiteId,pendingEmpId,typeOfWork,nmrGrandTotal,"NORMAL");
			
			
			for (int rowNum = DataStartingRowNumber; rowNum <= rowsCount; rowNum++) {
				System.out.println(rowNum);
				row = sheet.getRow(rowNum);

				if(row==null){
					continue;
				}
				

				Cell cell1 = row.getCell(MajorHeadColumnIndex,Row.RETURN_BLANK_AS_NULL);
				if (cell1 == null || cell1.getCellType() == Cell.CELL_TYPE_BLANK) {
					errMsg.add("Major Head is Blank in Line "+(rowNum+1));
					throw new RuntimeException("Major Head is Blank.");
				}
				majorHeadDesc = row.getCell(MajorHeadColumnIndex).getStringCellValue();
				System.out.println(majorHeadDesc);
				//checking major head is in DB
				majorHeadId = objBOQDao.getMajorHeadId(majorHeadDesc,typeOfWork);
				System.out.println(majorHeadId);
				if(majorHeadId==null){
					majorHeadId = objBOQDao.getMajorHeadTblSeqId();
					objBOQDao.insertMajorHead(majorHeadId,majorHeadDesc,user_id,typeOfWork);
				}					
				TotalnoofMajorHeads+=1;
				
				
				Cell cell2 = row.getCell(MinorHeadColumnIndex,Row.RETURN_BLANK_AS_NULL);
				if (cell2 == null || cell2.getCellType() == Cell.CELL_TYPE_BLANK) {
					errMsg.add("Minor Head is Blank in Line "+(rowNum+1));
					throw new RuntimeException("Minor Head is Blank.");
				}
				minorHeadDesc = row.getCell(MinorHeadColumnIndex).getStringCellValue();
				minorHeadId = objBOQDao.getMinorHeadId(minorHeadDesc,majorHeadId,typeOfWork);
				if(minorHeadId==null){
					minorHeadId = objBOQDao.getMinorHeadTblSeqId();
					objBOQDao.insertMinorHead(minorHeadId,minorHeadDesc,user_id,majorHeadId,typeOfWork);
				}
				TotalnoofMinorHeads+=1;
				
				
				Cell cell3 = row.getCell(WorkDescriptionColumnIndex,Row.RETURN_BLANK_AS_NULL);
				if (cell3 == null || cell3.getCellType() == Cell.CELL_TYPE_BLANK) {
					errMsg.add("Work Description is Blank in Line "+(rowNum+1));
					throw new RuntimeException("Work Description is Blank.");
				}
				workDescription = row.getCell(WorkDescriptionColumnIndex).getStringCellValue();
				workDescriptionId = objBOQDao.getWorkDescriptionId(workDescription,minorHeadId,typeOfWork);
				if(workDescriptionId==null){
					workDescriptionId = objBOQDao.getWorkDescriptionIdIndependentOnMinorHeadId(workDescription,minorHeadId,typeOfWork);
					if(workDescriptionId==null){
						workDescriptionId = objBOQDao.getWorkDescriptionTblSeqId();
					}
					objBOQDao.insertWorkDescription(workDescription,workDescriptionId,minorHeadId,user_id,"-",typeOfWork);
				}
				TotalnoofWorkDescriptions+=1;
				
				
				Cell cell4 = row.getCell(UnitOfMeasurementColumnIndex,Row.RETURN_BLANK_AS_NULL);
				if (cell4 == null || cell4.getCellType() == Cell.CELL_TYPE_BLANK) {
					errMsg.add("Unit Of Measurement is Blank in Line "+(rowNum+1));
					throw new RuntimeException("Unit Of Measurement is Blank.");
				}	
				measurementName = row.getCell(UnitOfMeasurementColumnIndex).getStringCellValue();
				measurementId = objBOQDao.getmeasurementId(measurementName,workDescriptionId,typeOfWork);
				if(measurementId==null){
					measurementId = objBOQDao.getMeasurementTblSeqId();
					objBOQDao.insertMeasurement(measurementId,measurementName,workDescriptionId,user_id,typeOfWork);
				}

				String currentWork = workDescriptionId+"@@"+measurementId+"@@"+minorHeadId;
				if(workList.contains(currentWork)){
					errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",Duplicate Work Found.");
					throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+"Duplicate Work Found");
				}
				else{
					workList.add(currentWork);
				}
				int tempBOQDetailsId = objBOQDao.getTempBOQDetailsId();
				objBOQDao.insertQsTempBOQDetails(tempBOQDetailsId,workDescriptionId,measurementId,0,0,tempBOQNo,scopeOfWork,minorHeadId,null,"LABOR");


			}//for - end NMRGrandTotalColumnIndex
			errMsg.add("Temporary BOQ No: "+tempBOQNo);
			errMsg.add("Total no.of Major Heads: "+TotalnoofMajorHeads);
			errMsg.add("Total no.of Minor Heads: "+TotalnoofMinorHeads);
			errMsg.add("Total no.of WorkDescriptions: "+TotalnoofWorkDescriptions);
			
			response = "Success";
			System.out.println("response: "+response);
			transactionManager.commit(status);
			System.out.println("Committed");
		}
		catch(Exception e){
			transactionManager.rollback(status);
			System.out.println("Rollbacked");
			if(response==null){
				response = "Exception";
			}
			System.out.println("response: "+response);
			e.printStackTrace();
			ex_list.add(e);
			mailException(e,0,ex_list);
		}
		return response;
	}
	
	
	@SuppressWarnings("resource")
	@Override
	public String reviseBOQ(MultipartFile multipartFile,List<String> errMsg,String user_id, String boqSiteId, List<String> tempBOQNoForController) throws IOException {

		String typeOfWork = "PIECEWORK";
		String response = null;
		InputStream inp =  new BufferedInputStream(multipartFile.getInputStream());
		int rowNumMail = 0;
		List<Exception> ex_list = new ArrayList<Exception>();
		Workbook wb = null;
		try {
			wb = WorkbookFactory.create(inp);
		} catch (Exception e) {
			e.printStackTrace();
			ex_list.add(e);
			mailException(e,0,ex_list);
			return "Exception";
		}
		
		System.out.println("Transaction Opened");
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try{
			
			Sheet sheet = wb.getSheet("BOQ");
			Sheet sheet2 = wb.getSheet("RA");
			int BOQSeqNo = objBOQDao.getBOQSeqNoOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork);
			String BOQNumber = objBOQDao.getBOQNoOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork);
			double presentBOQTotal = Double.parseDouble(objBOQDao.getBOQTotalOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork));
			double presentBOQLaborTotal = Double.parseDouble(objBOQDao.getBOQLaborTotalOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork));
			double presentBOQMaterialTotal = Double.parseDouble(objBOQDao.getBOQMaterialTotalOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork));
			/*boolean isAnyWOActive = objBOQDao.isAnyWorkOrderCreatingOrRevisingOnThisBoqNo(BOQNumber);
			if(isAnyWOActive){
				errMsg.add("WorkOrders Created On this BOQ are in Pending At Approvals.");
				throw new RuntimeException("WorkOrders Created On this BOQ are in Pending At Approvals.");
			}*/
			int presentVersionNo = Integer.parseInt(objBOQDao.getVersionNoOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork));
			int newVersionNo=presentVersionNo+1;
			int presentTempBOQNo = objBOQDao.getTempBOQNoOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork);
			//delete all records from table on above tempBoqNo
			String deletedData = objBOQDao.deleteAllTempBoqRecordsInDBForThisSite(boqSiteId,typeOfWork);
			int tempBOQNo = objBOQDao.getTempBOQNo();
			tempBOQNoForController.add(String.valueOf(tempBOQNo));
			String pendingEmpId = objBOQDao.getPendingEmpId(user_id,boqSiteId);
			//=======================================
			//int err =10/0;
			objBOQDao.insertQsTempBOQ(tempBOQNo,user_id,boqSiteId,pendingEmpId,typeOfWork,0,"REVISED");
			DataFormatter formatter = new DataFormatter();
			int rowsCount = sheet.getLastRowNum();
			//rowsCount = 427;
			int ActionRowIndex = 2;
			int BlockRowIndex = 3;
			int BlockNamesRowIndex = 4;
			
			final int ActionColumnIndex = 1;
			final int IMScodeColumnIndex = 2;
			final int DescriptionColumnIndex = 3;
			final int UnitOfMeasurementColumnIndex = 4;
			int MaterialRatePerUnitColumnIndex = 5;
			int LaborRatePerUnitColumnIndex = 6;
			int FBIndex = 7;// FBIndex - FirstBlockIndex
			
			HashMap<Integer,String> BlockLetters = new HashMap<Integer,String>();
			String[] BlockIdentificationLetters = {"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
			for(int i=0;i<26;i++){
				BlockLetters.put((FBIndex+i),BlockIdentificationLetters[i]);
			}
			/*BlockLetters.put(FBIndex,"A");
			BlockLetters.put(FBIndex+1,"B"); //this static code commented, because we are dynamically adding using for loop in the above
			BlockLetters.put(FBIndex+2,"C");
			.....
			*/
			
			Row row = sheet.getRow(BlockRowIndex);
			int noOfColumns = row.getLastCellNum();
			
			boolean isBlockKeywordGiven = false;
			boolean isLaborAmountGiven = false;
			boolean isMaterialAmountGiven = false;
			boolean isCumulativeAmountGiven = false;
			for (int i = 0; i <= noOfColumns; i++) {
				Cell cell = row.getCell(i);
				String cellValue = null;
				switch(i){

				case ActionColumnIndex:
					cellValue = row.getCell(i).getStringCellValue().toLowerCase();
					if(cellValue.contains("action")){break;}
					else{
						errMsg.add("Check ACTION is in Right Column");
						throw new RuntimeException("Check ACTION is in Right Column");
					}
				case IMScodeColumnIndex:
					cellValue = row.getCell(i).getStringCellValue().toLowerCase();
					if(cellValue.contains("ims code")){break;}
					else{
						errMsg.add("Check IMS CODE is in Right Column");
						throw new RuntimeException("Check IMS CODE is in Right Column");
					}
				case DescriptionColumnIndex:
					cellValue = row.getCell(i).getStringCellValue().toLowerCase();
					if(cellValue.contains("description")){break;}
					else{
						errMsg.add("Check Description is in Right Column");
						throw new RuntimeException("Check Description is in Right Column");
					}
				case UnitOfMeasurementColumnIndex:	
					cellValue = row.getCell(i).getStringCellValue().toLowerCase();
					if(cellValue.contains("uom")){break;}
					else{
						errMsg.add("Check UOM is in Right Column");
						throw new RuntimeException("Check UOM is in Right Column");
					}

				default: 
					if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
						continue;
					}
					else{
						cellValue = row.getCell(i).getStringCellValue().toLowerCase();
						if(cellValue.contains("block")){
							isBlockKeywordGiven = true;
						}
						else if(cellValue.contains("labor")&&cellValue.contains("amount")){
							isLaborAmountGiven = true;
						}
						else if(cellValue.contains("material")&&cellValue.contains("amount")){
							isMaterialAmountGiven = true;
						}
						else if(cellValue.contains("cumulative")&&cellValue.contains("amount")){
							isCumulativeAmountGiven = true;
						}
					}
				}//switch-end

			}
			if(!isBlockKeywordGiven){
				errMsg.add("Check BLOCK keywords is Given");
				throw new RuntimeException("Check BLOCK keywords is Given");
			}
			if(!isLaborAmountGiven){
				errMsg.add("Check LABOR AMOUNT keyword is Given");
				throw new RuntimeException("Check LABOR AMOUNT keyword is Given");
			}
			if(!isMaterialAmountGiven){
				errMsg.add("Check MATERIAL AMOUNT keyword is Given");
				throw new RuntimeException("Check MATERIAL AMOUNT keyword is Given");
			}if(!isCumulativeAmountGiven){
				errMsg.add("Check CUMULATIVE AMOUNT keyword is Given");
				throw new RuntimeException("Check CUMULATIVE AMOUNT keyword is Given");
			}
			List<Integer> blockColumnIndexList = new ArrayList<Integer>();
			int laborTotalColumnIndex = -1;
			int materialTotalColumnIndex = -1;
			int cumulativeTotalColumnIndex = -1;
			for (int i = 0; i <= noOfColumns; i++) {
				Cell cell = row.getCell(i);
				if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					continue;
				}
				else{
					String cellValue = row.getCell(i).getStringCellValue().toLowerCase();
					if(cellValue.contains("block")){
						blockColumnIndexList.add(i);
					}
					else if(cellValue.contains("labor")&&cellValue.contains("amount")){
						laborTotalColumnIndex = i;
					}
					else if(cellValue.contains("material")&&cellValue.contains("amount")){
						materialTotalColumnIndex = i;
					}
					else if(cellValue.contains("cumulative")&&cellValue.contains("amount")){
						cumulativeTotalColumnIndex = i;
					}
				}
			}
			String newBlocks = "";
			row = sheet.getRow(ActionRowIndex);
			for (int index : blockColumnIndexList) {
				String ActionName = row.getCell(index).getStringCellValue().trim();
				if(StringUtils.isNotBlank(ActionName)){
					if(ActionName.equals("NEW")){
						newBlocks = newBlocks + BlockLetters.get(index);
					}
					else{
						errMsg.add("Action is not matched given in Action Row");
						throw new RuntimeException("Action is not matched given in Action Row");
					}
				}
			}
			
			row = sheet.getRow(BlockNamesRowIndex);
			String LaborKeyword = row.getCell(LaborRatePerUnitColumnIndex).getStringCellValue().toLowerCase();
			if(!LaborKeyword.contains("labor")){
				errMsg.add("Check LABOR RATE/UNIT is in Right Column");
				throw new RuntimeException("Check LABOR RATE/UNIT is in Right Column");
			}
			String MaterialKeyword = row.getCell(MaterialRatePerUnitColumnIndex).getStringCellValue().toLowerCase();
			if(!MaterialKeyword.contains("material")){
				errMsg.add("Check MATERIAL RATE/UNIT is in Right Column");
				throw new RuntimeException("Check MATERIAL RATE/UNIT is in Right Column");
			}
			
			/*** Getting BlockIds,FloorIds,MajorHeadIds,MinorHeadIds,WorkDescriptions,MeasurementIds ***/
			HashMap<String,String> BlockIdsMap = objBOQDao.getBlockIdsMap(boqSiteId);
			HashMap<String,HashMap<String,String>> FloorIdsMap = objBOQDao.getFloorIdsMap(boqSiteId);
			HashMap<String,String> MajorHeadIdsMap = objBOQDao.getMajorHeadIdsMap(typeOfWork);
			HashMap<String,HashMap<String,String>> MinorHeadIdsMap = objBOQDao.getMinorHeadIdsMap(typeOfWork);
			HashMap<String,HashMap<String,String>> WorkDescriptionIdsMap = objBOQDao.getWorkDescriptionIdsMap(typeOfWork);
		    HashMap<String,HashMap<String,String>> MeasurementIdsMap = objBOQDao.getMeasurementIdsMap(typeOfWork);
		    
		    /*** Getting BlockIds,FloorIds,.....    END    ***/
		    /*** for revise boq ***/
		    Map<String,Double> WDtotals = objBOQDao.getWDtotals_inDB(BOQSeqNo);
		    Set<String> WDsinPresentBOQ = WDtotals.keySet();
		    Map<String,Double> MAJtotals = objBOQDao.getMAJtotals_inDB(BOQSeqNo);
		    Set<String> MAJheadsinPresentBOQ = MAJtotals.keySet();
		    Map<String,String> PresentBoqWorkAreaGroupIdsMap = objBOQDao.getWorkAreaGroupIdsMap(BOQSeqNo);
		    MultiObject multiObj = objBOQDao.getMajorHeadWiseWDs(BOQSeqNo);
		    Map<String,List<String>> MajorHeadWiseWDs = multiObj.getMapOfStringAndList1();
		    Map<String, String> WDids = multiObj.getMapOfStrings1();
			
		    /*** for revise boq - END***/
			
			/**************************************** GETTING BLOCK IDs - START *********************************************/
			//Map<Integer,String> blockNames = new HashMap<Integer,String>();
			List<String> blockIdsList = new ArrayList<String>();
			Map<Integer,String> blockIds = new HashMap<Integer,String>();
			for (int index : blockColumnIndexList) {
				String blockName = row.getCell(index).getStringCellValue().trim().toLowerCase();
				//blockNames.put(index,blockName);
				String blockId = null;
				try{
					//blockId = objBOQDao.getBlockId(blockName,boqSiteId);
					blockId = BlockIdsMap.get(blockName);
					if(blockId==null){throw new RuntimeException();}
				}catch(Exception e){
					errMsg.add("Check '"+blockName+"' Spelling");
					throw new RuntimeException("Problem in Getting BlockId");
				}
				blockIds.put(index, blockId);
				blockIdsList.add(blockId);
			}
			/**************************************** GETTING BLOCK IDs - END *********************************************/
			/******************* Comparing Both BOQ & RA sheets *****************************************/
			HashMap<String,String> BOQ_Sheet_MaterialWDs = new HashMap<String,String>();
			try{
				BOQ_Sheet_MaterialWDs = getBOQSheetMaterialWDs(sheet,errMsg,rowsCount,IMScodeColumnIndex,DescriptionColumnIndex,UnitOfMeasurementColumnIndex,MaterialRatePerUnitColumnIndex,true,ActionColumnIndex,newBlocks);
			} catch (Exception e) {
				errMsg.add("Please Check Actions in BOQ. Give actions to MajorHeads,MinorHeads,WDs also.");
				e.printStackTrace();
				ex_list.add(e);
				throw new RuntimeException("Please Check Actions in BOQ. Give actions to MajorHeads,MinorHeads,WDs also.");
			}
			HashMap<String,WDRateAnalysis> RA_Sheet_Data = getRASheetData(sheet2,errMsg,true,newBlocks);
			Set<String> RA_Sheet_MaterialWDs = RA_Sheet_Data.keySet();
			Set<String> MaterialWdsHasNoRAData = BOQ_Sheet_MaterialWDs.keySet();
			System.out.println("MaterialWdsHasNoRAData: "+MaterialWdsHasNoRAData);
			System.out.println("RA_Sheet_MaterialWDs: "+RA_Sheet_MaterialWDs);
			MaterialWdsHasNoRAData.removeAll(RA_Sheet_MaterialWDs);
			if(!MaterialWdsHasNoRAData.isEmpty()){
				errMsg.add("Below Work Descriptions are Not Found in RA Sheet");
				for(String wd : MaterialWdsHasNoRAData){
					errMsg.add(BOQ_Sheet_MaterialWDs.get(wd));
				}
				throw new RuntimeException("");
			}
			
			/**************************************************************************/
			
			//int err =10/0;
			//System.out.print(err);
			
			String majorHeadDesc = null;
			String majorHeadId = null;
			String minorHeadDesc = null;
			String minorHeadId = null;
			String scopeOfWork = null;
			String workDescription = null;
			String workDescriptionId = null;
			String measurementName = null;
			String measurementId = null;
			double laborRatePerUnit = 0.0;
			double materialRatePerUnit = 0.0;
			
			//double currentCumulativeMajorHeadTotal = 0.0;
			//double currentLaborMajorHeadTotal = 0.0;
			//double currentMaterialMajorHeadTotal = 0.0;
			double GrandTotal = 0.0;
			double LaborTotal = 0.0;
			double MaterialTotal = 0.0;
			
			boolean isGrandTotalVerified = false;
			
			int TotalnoofMajorHeads = 0;
			int TotalnoofMinorHeads = 0;
			int TotalnoofWorkDescriptions = 0;
			
			List<String> workList = new ArrayList<String>();
			List<String> blockWiseWorkList = new ArrayList<String>();
			List<String> floorWiseWorkList = new ArrayList<String>();
			//HashMap<String,String> workGroupList = new HashMap<String,String>();
			
			int ModificationsDone = 0;
			String actionInBeforeRow = "";
			String floorNameInBeforeRow = "";
			String WDInBeforeRow = "";
			String MinorHeadInBeforeRow = "";
			String recordTypeInBeforeRow = "";
			boolean isSOWEdited = false;
			List<String> listOfWDsSOWEdited = new ArrayList<String>();
			
			
			boolean givenMaterialWork = false;
			boolean givenLaborWork = false;
			
			double reviseBOQTotalDiff = 0.0;
			double reviseBOQLaborTotalDiff = 0.0;
			double reviseBOQMaterialTotalDiff = 0.0;
			
			double WDtotal_inExcel = 0.0;
			double WDtotal_inDB = 0.0;
			double WDtotal_Diff = 0.0;
			
			double MAJtotal_inDB = 0.0;
			double MAJtotal_Diff = 0.0;
			
			List<String> MajorHead_WDids_inExcel = new ArrayList<String>();
			List<String> MajorHead_WDids_inDB = new ArrayList<String>();
			
			HashMap<String,BOQBean> workAreaChangesInPrevWD = new HashMap<String,BOQBean>();
			HashMap<String,BOQBean> workAreaNoChangesInPrevWD = new HashMap<String,BOQBean>();
			List<String> workAreaGroupChangesInPrevWD = new ArrayList<String>();
			List<String> workAreaGroupNoChangesInPrevWD = new ArrayList<String>();
			for (int rowNum = 0; rowNum <= rowsCount; rowNum++) {
				System.out.println(rowNum);rowNumMail=rowNum;
				row = sheet.getRow(rowNum);
				 
				if(row==null){
					continue;
				}
				Cell cell = row.getCell(IMScodeColumnIndex,Row.RETURN_BLANK_AS_NULL);
				if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
					continue;
				}
				String Action = null;
				Cell cell6 = row.getCell(ActionColumnIndex,Row.RETURN_BLANK_AS_NULL);
				if (cell6 == null || cell6.getCellType() == Cell.CELL_TYPE_BLANK) {
					if(StringUtils.isNotBlank(newBlocks)){
						Action = "EDIT-MATERIAL-"+newBlocks;
					}
				}
				else{
					Action = cell6.getStringCellValue();
					Action = Action.trim();
					Action = Action.toUpperCase();
					if(!Action.equals("ACTION")&&!Action.contains("EDIT")&&!Action.equals("DEL")&&!Action.equals("NEW")&&!Action.contains("EDIT-")&&!Action.equals("NOACTION")){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",check the keyword in Action column.");
						throw new RuntimeException("Action Keyword wrong.");
					}
					/*if(Action.equals("EDIT")||Action.equals("DEL")||Action.equals("NEW")||Action.contains("EDIT-")){
						ModificationsDone++;
					}*/
				}
				
				//boolean mustRead = false;
				String headerName = cell.getStringCellValue().toLowerCase();
				//if(headerName.contains("grand")&&headerName.contains("total")){mustRead = true;}
				
				if (StringUtils.isBlank(Action)) {
					/*if(mustRead){  Read the row }
					else{
						//continue;		/* Read the row*/
					//}
				}
				else{
					if(Action.contains(" ")){
						errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ", Remove spaces in Action Keyword.");
						throw new RuntimeException("Remove spaces in Action Keyword.");
					}
				}
				
				if(headerName.contains("major")&&headerName.contains("head")&&!headerName.contains("total")){
					
					majorHeadDesc = row.getCell(DescriptionColumnIndex).getStringCellValue().trim();
					System.out.println(majorHeadDesc);
					//checking major head is in DB
					//majorHeadId = objBOQDao.getMajorHeadId(majorHeadDesc,typeOfWork);
					majorHeadId = MajorHeadIdsMap.get(majorHeadDesc.toLowerCase());
					System.out.println(majorHeadId);
					if(majorHeadId==null){
						majorHeadId = objBOQDao.getMajorHeadTblSeqId();
						objBOQDao.insertMajorHead(majorHeadId,majorHeadDesc,user_id,typeOfWork);
						MajorHeadIdsMap.put(majorHeadDesc.toLowerCase(), majorHeadId);
					}
					
					if (StringUtils.isBlank(Action)) {
						if(!MAJheadsinPresentBOQ.contains(majorHeadId)){
							errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", Action not given for Major Head. Please fill up ACTION column.");
							throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", Action not given for Major Head. Please fill up ACTION column.");
						}
					}
					if(MAJheadsinPresentBOQ.contains(majorHeadId)){
						MAJtotal_inDB = MAJtotals.get(majorHeadId);
						MajorHead_WDids_inDB = MajorHeadWiseWDs.get(majorHeadId);
					}
					
					if(StringUtils.isBlank(majorHeadDesc)||majorHeadId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", Major Head is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", Major Head is Not given");
					}
					scopeOfWork = null;
					minorHeadDesc = null;
					minorHeadId = null;
					workDescription = null;
					workDescriptionId = null;
					//currentCumulativeMajorHeadTotal = 0.0;
					//currentLaborMajorHeadTotal = 0.0;
					//currentMaterialMajorHeadTotal = 0.0;
					TotalnoofMajorHeads += 1;
					
				}
				if(headerName.contains("minor")&&headerName.contains("head")){
					if(StringUtils.isBlank(majorHeadDesc)||majorHeadId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", Major Head is Not given for this Minor Head");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", Major Head is Not given for this Minor Head");
					}
					minorHeadDesc = row.getCell(DescriptionColumnIndex).getStringCellValue().trim();
					//minorHeadId = objBOQDao.getMinorHeadId(minorHeadDesc,majorHeadId,typeOfWork);
					minorHeadId = MinorHeadIdsMap.get(majorHeadId)==null?null:MinorHeadIdsMap.get(majorHeadId).get(minorHeadDesc.toLowerCase());
					if(minorHeadId==null){
						minorHeadId = objBOQDao.getMinorHeadTblSeqId();
						objBOQDao.insertMinorHead(minorHeadId,minorHeadDesc,user_id,majorHeadId,typeOfWork);
						if(MinorHeadIdsMap.get(majorHeadId)==null){
							MinorHeadIdsMap.put(majorHeadId,new HashMap<String,String>());
						}
						MinorHeadIdsMap.get(majorHeadId).put(minorHeadDesc.toLowerCase(),minorHeadId);
					}
					if(StringUtils.isBlank(minorHeadDesc)||minorHeadId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", Minor Head is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", Minor Head is Not given");
					}
					TotalnoofMinorHeads += 1;
					workDescription = null;
					workDescriptionId = null;
				}
				if(headerName.contains("sow")){
					scopeOfWork = row.getCell(DescriptionColumnIndex).getStringCellValue();
					if(Action==null){
						isSOWEdited = false;
					}
					else if(Action.equals("EDIT")){
						isSOWEdited = true;
						ModificationsDone++;
					}
					else{
						isSOWEdited = false;
					}
				}
				
				/************************************ IF IT IS  BLOCK - START  **************************************/
				//if(headerName.contains("wd")&&headerName.contains("block")){}
				/**************************************** IF IT IS   BLOCK - END  ***************************************/
				/************************************* IF IT IS  FLOOR - START  **************************************/
				//if(headerName.contains("floor")&&!headerName.contains("wd")){}
				/************************************* IF IT IS  FLOOR - END  **************************************/
				
				
				if(headerName.contains("wd")&&headerName.contains("floor")){
					validatePreviousWDtotal(WDtotal_inExcel,WDtotal_inDB,WDtotal_Diff,errMsg,rowNum);
					validateAreaRatiosOfLaborAndMaterial(workAreaChangesInPrevWD,errMsg,workAreaGroupChangesInPrevWD,BOQSeqNo,rowNum,null,false,workAreaNoChangesInPrevWD,workAreaGroupNoChangesInPrevWD);//,
					WDtotal_inExcel = WDtotal_inDB = WDtotal_Diff = 0;
					
					String levelOfWork = "F"; //Floor level
					if(StringUtils.isBlank(minorHeadDesc)||minorHeadId==null){
						errMsg.add("In BOQ sheet, Line no. "+(rowNum+1)+", MinorHead is not given for this work.");
						throw new RuntimeException("In BOQ sheet, Line no. "+(rowNum+1)+", MinorHead is not given for this work.");
					}
					workDescription = row.getCell(DescriptionColumnIndex).getStringCellValue().trim();
					//workDescriptionId = objBOQDao.getWorkDescriptionId(workDescription,minorHeadId,typeOfWork);
					workDescriptionId = WorkDescriptionIdsMap.get(minorHeadId)==null?null:WorkDescriptionIdsMap.get(minorHeadId).get(workDescription.toLowerCase());
					if(workDescriptionId==null){
						workDescriptionId = objBOQDao.getWorkDescriptionTblSeqId();
						objBOQDao.insertWorkDescription(workDescription,workDescriptionId,minorHeadId,user_id,levelOfWork,typeOfWork);
						if(WorkDescriptionIdsMap.get(minorHeadId)==null){
							WorkDescriptionIdsMap.put(minorHeadId,new HashMap<String,String>());
						}
						WorkDescriptionIdsMap.get(minorHeadId).put(workDescription.toLowerCase(),workDescriptionId);
					}
					
					MajorHead_WDids_inExcel.add(workDescriptionId);
					if (StringUtils.isBlank(Action)) {
						if(!WDsinPresentBOQ.contains(workDescriptionId)){
							errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", Action not given for WD. Please fill up ACTION column.");
							throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", Action not given for WD. Please fill up ACTION column.");
						}
					}
					if(WDsinPresentBOQ.contains(workDescriptionId)){
						WDtotal_inDB = WDtotals.get(workDescriptionId);
					}
					
					if(StringUtils.isBlank(workDescription)||workDescriptionId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
					}
					TotalnoofWorkDescriptions += 1;
					floorNameInBeforeRow = "";
					recordTypeInBeforeRow = "";
					actionInBeforeRow = null;
				}
				
				
				/**************************************** BLOCK or FLOOR - START********************************************/
				if((headerName.contains("wd")&&headerName.contains("block")) || (headerName.contains("floor")&&!headerName.contains("wd"))){
					String WORK_LEVEL = "";
					if(headerName.contains("wd")&&headerName.contains("block")){	WORK_LEVEL = "BLOCK";	}
					if(headerName.contains("floor")&&!headerName.contains("wd")){	WORK_LEVEL = "FLOOR";	}
					if(StringUtils.isBlank(WORK_LEVEL)){throw new RuntimeException();} //no need this line, just for safety
					

					Cell cell4 = row.getCell(LaborRatePerUnitColumnIndex);
					Cell cell7 = row.getCell(MaterialRatePerUnitColumnIndex);
					if (cell4 == null || cell4.getCellType() == Cell.CELL_TYPE_BLANK) {
						//errMsg.add("Labor Rate/Unit is Blank in Line "+(rowNum+1));
						//throw new RuntimeException("Labor Rate per Unit is Blank.");
						laborRatePerUnit = 0.0;
						
					}else{
						laborRatePerUnit = row.getCell(LaborRatePerUnitColumnIndex).getNumericCellValue();
					}
					if (cell7 == null || cell7.getCellType() == Cell.CELL_TYPE_BLANK) {
						//errMsg.add("Material Rate/Unit is Blank in Line "+(rowNum+1));
						//throw new RuntimeException("Material Rate per Unit is Blank.");
						materialRatePerUnit = 0.0;
					}else{
						materialRatePerUnit = row.getCell(MaterialRatePerUnitColumnIndex).getNumericCellValue();
					}
					if(laborRatePerUnit==0.0 && materialRatePerUnit==0.0){
						 if(Action != null && Action.equals("DEL")){ // this DEL condition added newly.because client facing problems at applying formulas
							 //executes below code
						 }
						 else{
							 continue;
						 }
					}
					
					Map<String,String> BlockIdAndFloorIdMap = new HashMap<String,String>();
					String floorName = null;
					if(WORK_LEVEL.equals("BLOCK")){
						String workDescriptionInNextRow = "";try {workDescriptionInNextRow = sheet.getRow(rowNum-1).getCell(DescriptionColumnIndex).getStringCellValue().trim();} catch (Exception e) {}
						workDescription = row.getCell(DescriptionColumnIndex).getStringCellValue().trim();
						if(!workDescriptionInNextRow.equals(workDescription)){
							validatePreviousWDtotal(WDtotal_inExcel,WDtotal_inDB,WDtotal_Diff,errMsg,rowNum);
							validateAreaRatiosOfLaborAndMaterial(workAreaChangesInPrevWD,errMsg,workAreaGroupChangesInPrevWD,BOQSeqNo,rowNum,null,false,workAreaNoChangesInPrevWD,workAreaGroupNoChangesInPrevWD);//
							WDtotal_inExcel = WDtotal_inDB = WDtotal_Diff = 0;
						}
						String levelOfWork = "B"; //Block level
						if(StringUtils.isBlank(minorHeadDesc)||minorHeadId==null){
							errMsg.add("In BOQ sheet, Line no. "+(rowNum+1)+", MinorHead is not given for this work.");
							throw new RuntimeException("In BOQ sheet, Line no. "+(rowNum+1)+", MinorHead is not given for this work.");
						}
						workDescription = row.getCell(DescriptionColumnIndex).getStringCellValue().trim();
						/*//for WD in consecutive rows with different price - start
						String currentAction = Action==null?"":Action;
						if(WDInBeforeRow.equals(workDescription)&&MinorHeadInBeforeRow.equals(minorHeadDesc)){
							checkingEDIT_hyphenGivenOrNotForSameConsecutiveFloors( newBlocks, actionInBeforeRow, currentAction, rowNum, errMsg );
						}
						WDInBeforeRow = workDescription;
						MinorHeadInBeforeRow = minorHeadDesc;
						actionInBeforeRow = currentAction;//for WD in consecutive rows with different price - end
						*/
						//workDescriptionId = objBOQDao.getWorkDescriptionId(workDescription,minorHeadId,typeOfWork);
						workDescriptionId = WorkDescriptionIdsMap.get(minorHeadId)==null?null:WorkDescriptionIdsMap.get(minorHeadId).get(workDescription.toLowerCase());
						if(workDescriptionId==null){
							workDescriptionId = objBOQDao.getWorkDescriptionTblSeqId();
							objBOQDao.insertWorkDescription(workDescription,workDescriptionId,minorHeadId,user_id,levelOfWork,typeOfWork);
							if(WorkDescriptionIdsMap.get(minorHeadId)==null){
								WorkDescriptionIdsMap.put(minorHeadId,new HashMap<String,String>());
							}
							WorkDescriptionIdsMap.get(minorHeadId).put(workDescription.toLowerCase(),workDescriptionId);
						}
						
						MajorHead_WDids_inExcel.add(workDescriptionId);
						if (StringUtils.isBlank(Action)) {
							if(!WDsinPresentBOQ.contains(workDescriptionId)){
								errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", Action not given for WD. Please fill up ACTION column.");
								throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", Action not given for WD. Please fill up ACTION column.");
							}
						}
						if(WDsinPresentBOQ.contains(workDescriptionId)){
							WDtotal_inDB = WDtotals.get(workDescriptionId);
						}
						
						if(StringUtils.isBlank(workDescription)||workDescriptionId==null){
							errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
							throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
						}
						
					}
					if(WORK_LEVEL.equals("FLOOR")){
						
						floorName = row.getCell(DescriptionColumnIndex).getStringCellValue();
						if(floorName.contains(" - ")){
							floorName = floorName.split(" - ")[0];
						}
						floorName = floorName.trim().toLowerCase();
						/*//for floorName in consecutive rows with different price - start
						String currentAction = Action==null?"":Action;
						if(floorNameInBeforeRow.equals(floorName)){
							checkingEDIT_hyphenGivenOrNotForSameConsecutiveFloors( newBlocks, actionInBeforeRow, currentAction, rowNum, errMsg );
						}
						floorNameInBeforeRow = floorName;
						actionInBeforeRow = currentAction;//for floorName in consecutive rows with different price - end*/						
						BlockIdAndFloorIdMap = FloorIdsMap.get(floorName);
						
						if(workDescriptionId==null){
							errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
							throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", WD is Not given");
						}
						
					}
					
					
					///--=========--------------===========------------=========--------
					

					measurementName = row.getCell(UnitOfMeasurementColumnIndex).getStringCellValue().trim();
					//measurementId = objBOQDao.getmeasurementId(measurementName,workDescriptionId,typeOfWork);
					measurementId = MeasurementIdsMap.get(workDescriptionId)==null?null:MeasurementIdsMap.get(workDescriptionId).get(measurementName.toLowerCase());
					if(measurementId==null){
						measurementId = objBOQDao.getMeasurementTblSeqId();
						objBOQDao.insertMeasurement(measurementId,measurementName,workDescriptionId,user_id,typeOfWork);
						if(MeasurementIdsMap.get(workDescriptionId)==null){
							MeasurementIdsMap.put(workDescriptionId,new HashMap<String,String>());
						}
						MeasurementIdsMap.get(workDescriptionId).put(measurementName.toLowerCase(),measurementId);
					}
					if(StringUtils.isBlank(measurementName)||measurementId==null){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", UOM is Not given");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", UOM is Not given");
					}
					
					if(laborRatePerUnit!=0.0){givenLaborWork = true;}else{givenLaborWork = false;}
					if(materialRatePerUnit!=0.0){givenMaterialWork = true;}else{givenMaterialWork = false;}
					
					Cell cell2 = row.getCell(laborTotalColumnIndex);
					Cell cell8 = row.getCell(materialTotalColumnIndex);
					Cell cell12 = row.getCell(cumulativeTotalColumnIndex);
					if(cell2.getCellType() == Cell.CELL_TYPE_FORMULA || cell8.getCellType() == Cell.CELL_TYPE_FORMULA){
						response =  "FormulaProblem";
						throw new RuntimeException("FormulaProblem");
					}
					
					double givenLaborTotalForVerification = cell2.getNumericCellValue();
					double givenMaterialTotalForVerification = cell8.getNumericCellValue();
					double givenCumulativeTotalForVerification = cell12.getNumericCellValue();
					
					double totalAreaOfAllBlocks = 0.0;
					for(int index : blockColumnIndexList){
						double blockArea = 0.0;
						Cell cell1 = row.getCell(index,Row.RETURN_BLANK_AS_NULL);
						if (cell1 == null || cell1.getCellType() == Cell.CELL_TYPE_BLANK) {
							continue;
						}
						else{
							if(cell1.getCellType() == Cell.CELL_TYPE_NUMERIC){
								blockArea = cell1.getNumericCellValue();
							}
							else{
								blockArea = 0.0;
							}

							totalAreaOfAllBlocks += blockArea;
						}
					}
					
					double totalLaborAmountForAllBlocks = totalAreaOfAllBlocks*laborRatePerUnit;
					double totalMaterialAmountForAllBlocks = totalAreaOfAllBlocks*materialRatePerUnit;
					double totalCumulativeAmountForAllBlocks = totalLaborAmountForAllBlocks+totalMaterialAmountForAllBlocks;
					if(Action!=null&&Action.equals("DEL")){
						totalLaborAmountForAllBlocks = totalMaterialAmountForAllBlocks = totalCumulativeAmountForAllBlocks = 0;
					}
					
					for(int i=1;i<=3;i++){
						String xxx = "";
						double xxxTotalinExcel = 0.0;
						double xxxTotal = 0.0;
						if(i==1){ xxx = "Material"; xxxTotalinExcel = givenMaterialTotalForVerification; 	xxxTotal = totalMaterialAmountForAllBlocks;}
						if(i==2){ xxx = "Labor"; 	xxxTotalinExcel = givenLaborTotalForVerification; 		xxxTotal = totalLaborAmountForAllBlocks;}
						if(i==3){ xxx = "Cumulative"; 	xxxTotalinExcel = givenCumulativeTotalForVerification; 	xxxTotal = totalCumulativeAmountForAllBlocks;}

						if(Math.abs(xxxTotalinExcel-xxxTotal)>1){
							response =  "VerificationFailed";
							System.out.println("given"+xxx+"TotalForVerification & IMS value: "+xxxTotalinExcel+","+xxxTotal);
							errMsg.add("In line "+(rowNum+1));
							errMsg.add(xxx+" Amounts in Excel & IMS: "+xxxTotalinExcel+","+xxxTotal);
							throw new RuntimeException("VerificationFailed");
						}
					}
					
					WDtotal_inExcel += totalCumulativeAmountForAllBlocks;
					if (StringUtils.isBlank(Action)) {
						/*if(mustRead){  Read the row }
						else{*/
							continue;
						//}
					}
					
					
					for (int recordTypeNo = 1; recordTypeNo <= 2; recordTypeNo++) {
						String recordType = null;
						double ratePerUnit = 0.0;
						double totalAmountForAllBlocks = 0.0;
						double givenTotalForVerification = 0.0;
						if (recordTypeNo == 1 && givenLaborWork) {
							recordType = "LABOR";
							ratePerUnit = laborRatePerUnit;
							totalAmountForAllBlocks = totalLaborAmountForAllBlocks;
							givenTotalForVerification = givenLaborTotalForVerification;
						} else if (recordTypeNo == 2 && givenMaterialWork) {
							recordType = "MATERIAL";
							ratePerUnit = materialRatePerUnit;
							totalAmountForAllBlocks = totalMaterialAmountForAllBlocks;
							givenTotalForVerification = givenMaterialTotalForVerification;
						} else {
							continue;
						}
						
						//for consecutive rows with same WD or floor - start
						if(WORK_LEVEL.equals("BLOCK")){
							//for WD in consecutive rows with different price - start
							String currentAction = Action==null?"":Action;
							if(WDInBeforeRow.equals(workDescription)&&MinorHeadInBeforeRow.equals(minorHeadDesc)&&recordTypeInBeforeRow.equals(recordType)){
								checkingEDIT_hyphenGivenOrNotForSameConsecutiveFloors( newBlocks, actionInBeforeRow, currentAction, rowNum, errMsg );
							}
							WDInBeforeRow = workDescription;
							MinorHeadInBeforeRow = minorHeadDesc;
							actionInBeforeRow = currentAction;//for WD in consecutive rows with different price - end
						}
						if(WORK_LEVEL.equals("FLOOR")){
							//for floorName in consecutive rows with different price - start
							String currentAction = Action==null?"":Action;
							if(floorNameInBeforeRow.equals(floorName)&&recordTypeInBeforeRow.equals(recordType)){
								checkingEDIT_hyphenGivenOrNotForSameConsecutiveFloors( newBlocks, actionInBeforeRow, currentAction, rowNum, errMsg );
							}
							floorNameInBeforeRow = floorName;
							actionInBeforeRow = currentAction;//for floorName in consecutive rows with different price - end
						}
						recordTypeInBeforeRow = recordType;
						//for consecutive rows with same WD or floor - end
						
						int tempBOQDetailsId = objBOQDao.getTempBOQDetailsId(workDescriptionId, tempBOQNo, measurementId, recordType);
						if (tempBOQDetailsId == 0) {
							tempBOQDetailsId = objBOQDao.getTempBOQDetailsId();
							if (Action != null && (Action.contains("EDIT") || Action.equals("NEW") || Action.equals("DEL") || Action.contains("EDIT-"))) {
								String Action1 = Action;
								if (Action.contains("EDIT-")) {
									Action1 = "EDIT";
								}
								objBOQDao.insertQsTempBOQDetails(tempBOQDetailsId, workDescriptionId, measurementId, totalAreaOfAllBlocks, totalAmountForAllBlocks, tempBOQNo, scopeOfWork, minorHeadId, Action1 + "-AREA", recordType);
							}
						} else {
							System.out.println("totalAreaOfAllBlocks:" + totalAreaOfAllBlocks + "Line " + (rowNum + 1));
							if (Action != null && (Action.contains("EDIT") || Action.equals("NEW") || Action.equals("DEL") || Action.contains("EDIT-"))) {
								String Action1 = "";
								String DBAction = objBOQDao.getDBActionOfTempBoqDetailsId(tempBOQDetailsId);
								if (Action.equals("NEW")) {
									DBAction = "NEW-" + DBAction.split("-")[1];
								}
								if (!DBAction.contains("AREA")) {
									DBAction = DBAction + "/AREA";
									Action1 = DBAction;
								} else {
									Action1 = DBAction;
								}
								objBOQDao.updateQsTempBOQDetailsWithAction(tempBOQDetailsId, totalAreaOfAllBlocks, totalAmountForAllBlocks, Action1);
							}
						}
						if (isSOWEdited && !listOfWDsSOWEdited.contains(workDescriptionId + "@@" + measurementId)) {
							tempBOQDetailsId = objBOQDao.getTempBOQDetailsId(workDescriptionId, tempBOQNo, measurementId, recordType);
							if (tempBOQDetailsId == 0) {
								tempBOQDetailsId = objBOQDao.getTempBOQDetailsId();
								objBOQDao.insertQsTempBOQDetails(tempBOQDetailsId, workDescriptionId, measurementId, totalAreaOfAllBlocks, totalAmountForAllBlocks, tempBOQNo, scopeOfWork, minorHeadId, "EDIT-SOW", recordType);
								listOfWDsSOWEdited.add(workDescriptionId + "@@" + measurementId);
							} else {
								String Action1 = "";
								String DBAction = objBOQDao.getDBActionOfTempBoqDetailsId(tempBOQDetailsId);
								if (!DBAction.contains("SOW")) {
									DBAction = DBAction + "/SOW";
									Action1 = DBAction;
								} else {
									Action1 = DBAction;
								}
								objBOQDao.updateQsTempBOQDetailsWithSOWandAction(tempBOQDetailsId, scopeOfWork, Action1);
								listOfWDsSOWEdited.add(workDescriptionId + "@@" + measurementId);
							}
						}
						/*********************************** LOOPING  EACH BLOCK - START  ************************************/
						double additionOfAllIndividualBlockAreaAmounts = 0;
						for (int index : blockColumnIndexList) {
							Cell cell1 = row.getCell(index, Row.RETURN_BLANK_AS_NULL);
							if (cell1 != null && cell1.getCellType() == Cell.CELL_TYPE_NUMERIC) {
								double blockArea = cell1.getNumericCellValue();
								if (Action == null && blockArea == 0.0) {
									continue;
								}
								double blockAreaAmount = blockArea * ratePerUnit;
								String blockId = blockIds.get(index);
								String floorId = null;
								String tempFloorId = "";//this is use for - some don't have floorId in DB. but they don't have area also. so they can allow.
								
								if(WORK_LEVEL.equals("BLOCK")){
									floorId = null;
								}
								if(WORK_LEVEL.equals("FLOOR")){
									floorId = BlockIdAndFloorIdMap == null ? null : BlockIdAndFloorIdMap.get(blockId);
									if (floorId == null) {
										floorId = floorName;
										tempFloorId = null;
									}
								}
								

								String currentWork = recordType+"@@"+workDescriptionId + "@@" + blockId + "@@" + floorId;
								if (blockArea != 0) {
									if(WORK_LEVEL.equals("BLOCK")){
										if (floorWiseWorkList.contains(workDescriptionId)) {
											errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",This work is given already in Floor wise.");
											throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) + ",This work is given already in Floor wise.");
										} else {
											if (!(Action != null && (Action.equals("DEL") || Action.equals("EDIT") || Action.equals("EDIT-MATERIAL")))) {
												blockWiseWorkList.add(workDescriptionId);
											}
										}
									}
									if(WORK_LEVEL.equals("FLOOR")){
										if (blockWiseWorkList.contains(workDescriptionId)) {
											errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",This work is given already in Block wise.");
											throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) + ",This work is given already in Block wise.");
										} else {
											floorWiseWorkList.add(workDescriptionId);
										}
									}
									if (workList.contains(currentWork)) {
										errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Duplicate Work Found.");
										throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) + "Duplicate Work Found");
									} else {
										if(Action!=null&&Action.equals("DEL")){}
										else{
											workList.add(currentWork);
										}
									}
								}
							
								
								if (Action != null && Action.contains("EDIT")) {
									boolean doEdit = true;
									if (Action.contains("EDIT-MATERIAL-")) {
										if (!Action.split("EDIT-MATERIAL-")[1].contains(BlockLetters.get(index))) {
											doEdit = false;
										}
									}
									else if (Action.contains("EDIT-") && !Action.equals("EDIT-MATERIAL")) {
										if (!Action.split("EDIT-")[1].contains(BlockLetters.get(index))) {
											doEdit = false;
										}
									}
									if (doEdit) {
										BOQBean bean = new BOQBean();
										try {
											bean = objBOQDao.getBOQAreaMappingOfParticularBlock(BOQSeqNo, workDescriptionId, measurementId, blockId, floorId, recordType);
										} catch (EmptyResultDataAccessException e) {
											errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Can not modify WD or UOM.");
											throw e;
										}
										double presentPrice = Double.valueOf(bean.getQsAreaPricePerUnit());
										double presentArea = Double.valueOf(bean.getWoWorkArea());
										double WOIntiateArea = Double.valueOf(bean.getWoWorkInitiateArea());
										double woIssueAreaPrice = Double.valueOf(bean.getWoIssueAreaPrice());
										String presentWorkAreaId = bean.getWorkAreaId();
										if (!(blockArea == 0 && presentArea == 0 && presentPrice == 0)) {
											if (roundToTwo(ratePerUnit) != presentPrice || blockArea != presentArea) {
												if (roundToTwo(ratePerUnit) < presentPrice) {
													if (roundToTwo(ratePerUnit) < woIssueAreaPrice) {
														if (Double.valueOf(WOIntiateArea) > 0) {
															errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Can not decrease Price, As already Work Order generated on this Work Description.");
															throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Can not decrease Price, As already Work Order generated on this Work Description.");
														}
													}
												}
												if (blockArea < WOIntiateArea) {
													errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Can not decrease Quantity, As already Work Order generated on this Work Description.");
													throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Can not decrease Quantity, As already Work Order generated on this Work Description.");
												}
												if(      blockArea != presentArea ) {
													bean.setStrArea(String.valueOf(blockArea));
													bean.setRecordType(recordType);
													workAreaChangesInPrevWD.put(currentWork, bean);
													workAreaGroupChangesInPrevWD.add(workDescriptionId+"@@"+blockId+"@@"+floorId);
												}
												
												//wo materials validation
												WDRateAnalysis objWDRA_empty = new WDRateAnalysis();
												objWDRA_empty.setMaterialsList(new ArrayList<MaterialDetails>());
												WDRateAnalysis revised_objWDRA = objWDRA_empty;
												if(recordType.equals("MATERIAL") ){
													WDRateAnalysis wo_objWDRA = StringUtils.isBlank(presentWorkAreaId) ? objWDRA_empty : objBOQDao.getWOMaterialDetails(presentWorkAreaId);
													revised_objWDRA = RA_Sheet_Data.get(workDescription.toLowerCase()+"@@"+measurementName.toLowerCase()+"@@"+ratePerUnit);
													if(revised_objWDRA==null){
													System.out.println("RA sheet does not have "+workDescription.toLowerCase()+"@@"+measurementName.toLowerCase()+"@@"+ratePerUnit);
													}
													if(Action.contains("MATERIAL")){
														validateWorkOrderMaterials(wo_objWDRA,revised_objWDRA,errMsg,rowNum);
													}
												}
												
												String workAreaId = objBOQDao.getWorkAreaSeqId();
												String workAreaGroupId = null;
												String currentWorkGroup = workDescriptionId+"@@"+blockId+"@@"+floorId;
												if(PresentBoqWorkAreaGroupIdsMap.containsKey(currentWorkGroup)){
													workAreaGroupId = PresentBoqWorkAreaGroupIdsMap.get(currentWorkGroup);
												}
												else{
													workAreaGroupId = objBOQDao.getWorkAreaGroupSeqId();
													PresentBoqWorkAreaGroupIdsMap.put(currentWorkGroup,workAreaGroupId);
												}
												String Action1 = Action;
												if (Action.contains("EDIT-")) {
													Action1 = "EDIT";
												}
												if(WORK_LEVEL.equals("FLOOR")){
													if (tempFloorId == null) {
														errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) +", Floor name '"+ floorName+"' not found in Master Data, Please Check once.");
														throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) +", Floor name '"+ floorName+"' not found in Master Data, Please Check once.");
													}
												}
												objBOQDao.insertQsTempBOQAreaMapping(workAreaId, workDescriptionId, measurementId, blockId, blockArea == 0 ? presentArea : blockArea, tempBOQDetailsId, blockAreaAmount, floorId, ratePerUnit, tempBOQNo, blockArea == 0 ? "DEL" : Action1, recordType, workAreaGroupId);
												int reviseBOQChangedDetailsId = objBOQDao.getReviseBOQChangedDetailsId();
												objBOQDao.insertReviseBOQChangedDetails(reviseBOQChangedDetailsId, tempBOQNo, BOQSeqNo, workAreaId, workDescriptionId, measurementId, floorId, blockId, boqSiteId, presentArea, blockArea, presentPrice, ratePerUnit, presentVersionNo, newVersionNo, blockArea == 0 ? "DEL" : (presentArea == 0 ? "NEW" : Action1), recordType);
												reviseBOQTotalDiff = reviseBOQTotalDiff + (blockArea*ratePerUnit)-(presentArea*presentPrice);
												if(recordType.equals("LABOR")){
													reviseBOQLaborTotalDiff = reviseBOQLaborTotalDiff + (blockArea*ratePerUnit)-(presentArea*presentPrice);
												}if(recordType.equals("MATERIAL")){
													reviseBOQMaterialTotalDiff = reviseBOQMaterialTotalDiff + (blockArea*ratePerUnit)-(presentArea*presentPrice);
												}
												WDtotal_Diff = WDtotal_Diff + (blockArea*ratePerUnit)-(presentArea*presentPrice);
												MAJtotal_Diff = MAJtotal_Diff + (blockArea*ratePerUnit)-(presentArea*presentPrice);
												
												if(recordType.equals("MATERIAL")){
													if(!Action.contains("MATERIAL")){
														if (roundToTwo(ratePerUnit) != presentPrice) {
															if(presentArea!=0){
																System.out.println("ratePerUnit,presentPrice"+ratePerUnit+","+presentPrice);
																errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ", Material rate/unit is changed. if u edit Materials in Rate Analysis for this Work, please add 'MATERIAL' keyword in ACTION column in BOQ sheet");
																throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) + ", Material rate/unit is changed. if u edit Materials in Rate Analysis for this Work, please add 'MATERIAL' keyword in ACTION column in BOQ sheet");
															}
														}
													}
													
													//objBOQDao.insertQsTempBOQProductDtls(workAreaId, (blockArea == 0 ? presentArea : blockArea), (blockArea == 0 ? present_objWDRA : revised_objWDRA) , boqSiteId, tempBOQNo);      //    , blockArea == 0 ? "DEL" : Action1, present_objWDRA, revised_objWDRA);
													//compareAndInsertReviseBOQQuantityChangedDetails(present_objWDRA,blockArea == 0 ? objWDRA_empty : revised_objWDRA,reviseBOQChangedDetailsId,tempBOQNo,blockArea);
													WDRateAnalysis present_objWDRA = StringUtils.isBlank(presentWorkAreaId) ? objWDRA_empty : objBOQDao.getPresentMaterialDetails(presentWorkAreaId);
													
													if(Action.contains("MATERIAL")){
														compareAndInsertTempProdDtlsAndQtyChgDtls(workAreaId, reviseBOQChangedDetailsId, presentArea, blockArea, presentArea == 0 ? objWDRA_empty : present_objWDRA, blockArea == 0 ? objWDRA_empty : revised_objWDRA, boqSiteId, tempBOQNo, blockArea == 0 ? "DEL" : (presentArea == 0 ? "NEW" : Action1), false);
													}else{
														compareAndInsertTempProdDtlsAndQtyChgDtls(workAreaId, reviseBOQChangedDetailsId, presentArea, blockArea, presentArea == 0 ? objWDRA_empty : present_objWDRA, blockArea == 0 ? objWDRA_empty : revised_objWDRA, boqSiteId, tempBOQNo, blockArea == 0 ? "DEL" : (presentArea == 0 ? "NEW" : Action1), true);
													}
												}
												ModificationsDone++;
											}
										} //
									} //doEdit
								}
								if (Action != null && Action.equals("NEW")) {
									if (blockArea == 0.0) {
										continue;
									}
									
									BOQBean bean = new BOQBean();
									bean.setWoWorkArea(String.valueOf(0));
									bean.setWoWorkInitiateArea(String.valueOf(0));
									bean.setStrArea(String.valueOf(blockArea));
									bean.setRecordType(recordType);
									workAreaChangesInPrevWD.put(currentWork, bean);
									workAreaGroupChangesInPrevWD.add(workDescriptionId+"@@"+blockId+"@@"+floorId);
									
									String workAreaId = objBOQDao.getWorkAreaSeqId();
									String workAreaGroupId = null;
									String currentWorkGroup = workDescriptionId+"@@"+blockId+"@@"+floorId;
									if(PresentBoqWorkAreaGroupIdsMap.containsKey(currentWorkGroup)){
										workAreaGroupId = PresentBoqWorkAreaGroupIdsMap.get(currentWorkGroup);
									}
									else{
										workAreaGroupId = objBOQDao.getWorkAreaGroupSeqId();
										PresentBoqWorkAreaGroupIdsMap.put(currentWorkGroup,workAreaGroupId);
									}
									int workcount = objBOQDao.checkIsThisWorkIsAlreadyInPresentBoq(workDescriptionId, measurementId, blockId, floorId, boqSiteId, BOQSeqNo, recordType);
									if (workcount > 0) {
										boolean isDeleting = objBOQDao.checkIsThisWorkIsDeletingNow(workDescriptionId, measurementId, blockId, floorId, boqSiteId, tempBOQNo, recordType);
										if (!isDeleting) {
											errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",This Work is not a new Work. This Work is already in the present BOQ.");
											throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) + ",This Work is not a new Work. This Work is already in the present BOQ.");
										}
									}
									if(WORK_LEVEL.equals("FLOOR")){
										if (tempFloorId == null) {
											errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) +", Floor name '"+ floorName+"' not found in Master Data, Please Check once.");
											throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) +", Floor name '"+ floorName+"' not found in Master Data, Please Check once.");
										}
									}
									
									objBOQDao.insertQsTempBOQAreaMapping(workAreaId, workDescriptionId, measurementId, blockId, blockArea, tempBOQDetailsId, blockAreaAmount, floorId, ratePerUnit, tempBOQNo, Action, recordType, workAreaGroupId);
									int reviseBOQChangedDetailsId = objBOQDao.getReviseBOQChangedDetailsId();
									objBOQDao.insertReviseBOQChangedDetails(reviseBOQChangedDetailsId, tempBOQNo, BOQSeqNo, workAreaId, workDescriptionId, measurementId, floorId, blockId, boqSiteId, 0, blockArea, 0, ratePerUnit, presentVersionNo, newVersionNo, Action, recordType);
									reviseBOQTotalDiff = reviseBOQTotalDiff + (blockArea*ratePerUnit);
									if(recordType.equals("LABOR")){
										reviseBOQLaborTotalDiff = reviseBOQLaborTotalDiff + (blockArea*ratePerUnit);
									}if(recordType.equals("MATERIAL")){
										reviseBOQMaterialTotalDiff = reviseBOQMaterialTotalDiff + (blockArea*ratePerUnit);
									}
									WDtotal_Diff = WDtotal_Diff + (blockArea*ratePerUnit);
									MAJtotal_Diff =MAJtotal_Diff + (blockArea*ratePerUnit);
									
									
									/*if(recordType.equals("MATERIAL") && Action.contains("MATERIAL")){
										WDRateAnalysis objWDRA_empty = new WDRateAnalysis();
										objWDRA_empty.setMaterialsList(new ArrayList<MaterialDetails>());
										WDRateAnalysis revised_objWDRA = RA_Sheet_Data.get(workDescription.toLowerCase()+"@@"+measurementName.toLowerCase()+"@@"+ratePerUnit);
										objBOQDao.insertQsTempBOQProductDtls(workAreaId,blockArea,revised_objWDRA,boqSiteId,tempBOQNo);
										compareAndInsertReviseBOQQuantityChangedDetails(objWDRA_empty,revised_objWDRA,reviseBOQChangedDetailsId,tempBOQNo,blockArea);
									}*/
									WDRateAnalysis objWDRA_empty = new WDRateAnalysis();
									objWDRA_empty.setMaterialsList(new ArrayList<MaterialDetails>());
									WDRateAnalysis revised_objWDRA = null;
									
									if(recordType.equals("MATERIAL")){
										
										revised_objWDRA = RA_Sheet_Data.get(workDescription.toLowerCase()+"@@"+measurementName.toLowerCase()+"@@"+ratePerUnit);
										
										compareAndInsertTempProdDtlsAndQtyChgDtls(workAreaId, reviseBOQChangedDetailsId, 0, blockArea, objWDRA_empty, revised_objWDRA, boqSiteId, tempBOQNo, "NEW", false);
										
									}
									ModificationsDone++;
								}
								if (Action != null && Action.equals("DEL")) {
									if (blockArea == 0.0) {
										continue;
									}
									double presentPrice = Double.valueOf(objBOQDao.getParticularBlockPrice(BOQSeqNo, workDescriptionId, measurementId, blockId, floorId, recordType));
									double presentArea = Double.valueOf(objBOQDao.getParticularBlockArea(BOQSeqNo, workDescriptionId, measurementId, blockId, floorId, recordType));
									double WOIntiateArea = Double.valueOf(objBOQDao.getWorkOrderIntiateArea(BOQSeqNo, workDescriptionId, measurementId, blockId, floorId, recordType));
									String presentWorkAreaId = objBOQDao.getPresentWorkAreaId(BOQSeqNo, workDescriptionId, measurementId, blockId, floorId, recordType);
									
									BOQBean bean = new BOQBean();
									bean.setWoWorkArea(String.valueOf(presentArea));
									bean.setWoWorkInitiateArea(String.valueOf(WOIntiateArea));
									bean.setStrArea(String.valueOf(0));
									bean.setRecordType(recordType);
									workAreaChangesInPrevWD.put(currentWork, bean);
									workAreaGroupChangesInPrevWD.add(workDescriptionId+"@@"+blockId+"@@"+floorId);
									
									if (WOIntiateArea > 0) {
										errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Work Description Can not be deleted, As already Work Order generated on this Work Description.");
										throw new RuntimeException("Work Order intiated on this Area. This Row can't be deleted.");
									}
									String workAreaId = objBOQDao.getWorkAreaSeqId();
									String workAreaGroupId = null;
									String currentWorkGroup = workDescriptionId+"@@"+blockId+"@@"+floorId;
									if(PresentBoqWorkAreaGroupIdsMap.containsKey(currentWorkGroup)){
										workAreaGroupId = PresentBoqWorkAreaGroupIdsMap.get(currentWorkGroup);
									}
									else{
										workAreaGroupId = objBOQDao.getWorkAreaGroupSeqId();
										PresentBoqWorkAreaGroupIdsMap.put(currentWorkGroup,workAreaGroupId);
									}
									if(WORK_LEVEL.equals("FLOOR")){
										if (tempFloorId == null) {
											errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) +", Floor name '"+ floorName+"' not found in Master Data, Please Check once.");
											throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) +", Floor name '"+ floorName+"' not found in Master Data, Please Check once.");
										}
									}
									objBOQDao.insertQsTempBOQAreaMapping(workAreaId, workDescriptionId, measurementId, blockId, presentArea/*blockArea*/, tempBOQDetailsId, blockAreaAmount, floorId, presentPrice/*ratePerUnit*/, tempBOQNo, Action, recordType, workAreaGroupId);
									int reviseBOQChangedDetailsId = objBOQDao.getReviseBOQChangedDetailsId();
									objBOQDao.insertReviseBOQChangedDetails(reviseBOQChangedDetailsId, tempBOQNo, BOQSeqNo, workAreaId, workDescriptionId, measurementId, floorId, blockId, boqSiteId, presentArea, 0, presentPrice, 0, presentVersionNo, newVersionNo, Action, recordType);
									reviseBOQTotalDiff = reviseBOQTotalDiff - (presentArea*presentPrice);
									if(recordType.equals("LABOR")){
										reviseBOQLaborTotalDiff = reviseBOQLaborTotalDiff - (presentArea*presentPrice);
									}if(recordType.equals("MATERIAL")){
										reviseBOQMaterialTotalDiff = reviseBOQMaterialTotalDiff - (presentArea*presentPrice);
									}
									WDtotal_Diff = WDtotal_Diff - (presentArea*presentPrice);
									MAJtotal_Diff =MAJtotal_Diff - (presentArea*presentPrice);
									
									/*if(recordType.equals("MATERIAL") && Action.contains("MATERIAL")){
										WDRateAnalysis objWDRA_empty = new WDRateAnalysis();
										objWDRA_empty.setMaterialsList(new ArrayList<MaterialDetails>());
										WDRateAnalysis present_objWDRA = objBOQDao.getPresentMaterialDetails(presentWorkAreaId);
										objBOQDao.insertQsTempBOQProductDtls(workAreaId,presentArea,present_objWDRA,boqSiteId,tempBOQNo);
										compareAndInsertReviseBOQQuantityChangedDetails(present_objWDRA,objWDRA_empty,reviseBOQChangedDetailsId,tempBOQNo,blockArea);
									}*/
									WDRateAnalysis objWDRA_empty = new WDRateAnalysis();
									objWDRA_empty.setMaterialsList(new ArrayList<MaterialDetails>());
									
									if(recordType.equals("MATERIAL")){
										WDRateAnalysis present_objWDRA = objBOQDao.getPresentMaterialDetails(presentWorkAreaId);
										
										compareAndInsertTempProdDtlsAndQtyChgDtls(workAreaId, reviseBOQChangedDetailsId, presentArea, 0, present_objWDRA, objWDRA_empty, boqSiteId, tempBOQNo, "DEL", false);
										
									}
									ModificationsDone++;
								}
								if (Action == null || !Action.equals("DEL")) {
									additionOfAllIndividualBlockAreaAmounts += blockAreaAmount;
									//currentCumulativeMajorHeadTotal += blockAreaAmount;
									GrandTotal += blockAreaAmount;
									if(recordType.equals("LABOR")){LaborTotal += blockAreaAmount; }//currentLaborMajorHeadTotal+=blockAreaAmount;}
									if(recordType.equals("MATERIAL")){MaterialTotal += blockAreaAmount; }//currentMaterialMajorHeadTotal+=blockAreaAmount;}
								}
							}
						}
						
						if(Math.abs(givenTotalForVerification-additionOfAllIndividualBlockAreaAmounts)>0.001){
							response =  "VerificationFailed";
							System.out.println("given"+recordType+"TotalForVerification,additionOfAllIndividualBlockAreaAmounts: "+givenTotalForVerification+","+additionOfAllIndividualBlockAreaAmounts);
							errMsg.add("In line "+(rowNum+1));
							errMsg.add(recordType+" Amounts in Excel & IMS: "+givenTotalForVerification+","+additionOfAllIndividualBlockAreaAmounts);
							throw new RuntimeException("VerificationFailed");
						}
						/*********************************** LOOPING  EACH BLOCK - END ************************************/
					} 
					if(WORK_LEVEL.equals("BLOCK")){
						TotalnoofWorkDescriptions += 1;
					}
				
				}
				
				/**************************************** BLOCK or FLOOR - END ********************************************/
				
				
				if(headerName.contains("major")&&headerName.contains("head")&&headerName.contains("total")){

					Cell cell2 = row.getCell(cumulativeTotalColumnIndex);
					if(cell2.getCellType() == Cell.CELL_TYPE_FORMULA){
						response =  "FormulaProblem";
						throw new RuntimeException("FormulaProblem");
					}
					double givenCumulativeMajorHeadTotalForVerification = cell2.getNumericCellValue();
					if(Math.abs(givenCumulativeMajorHeadTotalForVerification-(MAJtotal_inDB+MAJtotal_Diff))>1){
						response =  "VerificationFailed";
						System.out.println("givenCumulativeMajorHeadTotalForVerification,IMSCumulativeMajorHeadTotal: "+givenCumulativeMajorHeadTotalForVerification+","+(MAJtotal_inDB+MAJtotal_Diff));
						errMsg.add("Check In MajorHead '"+majorHeadDesc+"', WD(BLOCK) or FLOOR spellings maybe incorrect (or) not given.");
						errMsg.add("MajorHeadTotal Amounts in Excel & IMS: "+givenCumulativeMajorHeadTotalForVerification+","+(MAJtotal_inDB+MAJtotal_Diff));
						verifyWDsOfMajorHeadInDBandExcel(MajorHead_WDids_inDB,MajorHead_WDids_inExcel,errMsg,WDids);
						throw new RuntimeException("VerificationFailed");
					}
					System.out.println("givenMajorHeadTotalForVerification,currentMajorHeadTotal: "+givenCumulativeMajorHeadTotalForVerification+","+(MAJtotal_inDB+MAJtotal_Diff));

					MAJtotal_inDB = 0;
					MAJtotal_Diff = 0;
					MajorHead_WDids_inExcel.clear();
					MajorHead_WDids_inDB.clear();
					
				}
				if(headerName.contains("grand")&&headerName.contains("total")){
					validateAreaRatiosOfLaborAndMaterial(workAreaChangesInPrevWD,errMsg,workAreaGroupChangesInPrevWD,BOQSeqNo,rowNum,null,false,workAreaNoChangesInPrevWD,workAreaGroupNoChangesInPrevWD);//
					isGrandTotalVerified = true;
					double givenMaterialTotalForVerification = 0.0;
					double givenLaborTotalForVerification = 0.0;
					double givenGrandTotalForVerification = 0.0;
					double IMS_MaterialTotalForVerification = 0.0;
					double IMS_LaborTotalForVerification = 0.0;
					double IMS_GrandTotalForVerification = 0.0;
					
					for(int i=1;i<=3;i++){
						String xxx = "";
						int xxxTotalColumnIndex = -1;
						double xxxTotal = 0.0;
						double xxxDiff = 0.0;
						if(i==1){ xxx = "Material"; xxxTotalColumnIndex = materialTotalColumnIndex; 	xxxTotal = presentBOQMaterialTotal;	xxxDiff = reviseBOQMaterialTotalDiff;}
						if(i==2){ xxx = "Labor"; 	xxxTotalColumnIndex = laborTotalColumnIndex; 		xxxTotal = presentBOQLaborTotal;	xxxDiff = reviseBOQLaborTotalDiff;}
						if(i==3){ xxx = "Grand"; 	xxxTotalColumnIndex = cumulativeTotalColumnIndex; 	xxxTotal = presentBOQTotal;			xxxDiff = reviseBOQTotalDiff;}

						Cell cell2 = row.getCell(xxxTotalColumnIndex);
						if(cell2.getCellType() == Cell.CELL_TYPE_FORMULA){
							response =  "FormulaProblem";
							throw new RuntimeException("FormulaProblem");
						}
						double givenxxxTotalForVerification = cell2.getNumericCellValue();
						if(i==1){ givenMaterialTotalForVerification=givenxxxTotalForVerification; IMS_MaterialTotalForVerification=(xxxTotal+xxxDiff);}
						if(i==2){ givenLaborTotalForVerification=givenxxxTotalForVerification; IMS_LaborTotalForVerification=(xxxTotal+xxxDiff);}
						if(i==3){ givenGrandTotalForVerification=givenxxxTotalForVerification; IMS_GrandTotalForVerification=(xxxTotal+xxxDiff);}
						if(Math.abs(givenxxxTotalForVerification-(xxxTotal+xxxDiff))>1){
							response =  "VerificationFailed";
							System.out.println("given"+xxx+"TotalForVerification,GrandTotal: "+givenxxxTotalForVerification+","+(xxxTotal+xxxDiff));
							errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", "+xxx+" Total Does not Match");
							throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", "+xxx+" Total Does not Match");
						}
					}
					objBOQDao.updateGrandTotalInQsTempBOQ(tempBOQNo,IMS_GrandTotalForVerification,IMS_LaborTotalForVerification,IMS_MaterialTotalForVerification);
				}
				
			}//for - end
			if(ModificationsDone==0){
				errMsg.add("No Modifications Found.");
				throw new RuntimeException("No Modifications Found.");
			}
			
			//re-confirm	
			if(!isGrandTotalVerified){
				errMsg.add("GrandTotal Not Given for IMS code In Excel");
				throw new RuntimeException("GrandTotal Not Given In Excel");
			}
			errMsg.add("Temporary BOQ No: "+tempBOQNo);
			//errMsg.add("Total no.of Major Heads: "+TotalnoofMajorHeads);
			//errMsg.add("Total no.of Minor Heads: "+TotalnoofMinorHeads);
			//errMsg.add("Total no.of WorkDescriptions: "+TotalnoofWorkDescriptions);
			
			response = "Success";
			System.out.println("response: "+response);
			transactionManager.commit(status);
			System.out.println("Committed");
		}
		catch(Exception e){
			transactionManager.rollback(status);
			System.out.println("Rollbacked");
			if(response==null){
				response = "Exception";
			}
			System.out.println("response: "+response);
			if(errMsg.size()==0){errMsg.add("Exception at Excel line no. "+(rowNumMail+1));}
			mailException(e,rowNumMail+1,ex_list);
			e.printStackTrace();
		}
		return response;
	}

	
	
	
	
	
	
	
	





	





	//below method to check this condition : If any area is completed in work order then Labor or Material alone can’t be increased in that Area. If want to increase should increase both. (NOT about ratios as per in method name)
	private void validateAreaRatiosOfLaborAndMaterial(HashMap<String, BOQBean> workAreaChangesInPrevWD,
			 									List<String> errMsg, List<String> workAreaGroupChangesInPrevWD,int BOQSeqNo, int rowNum, String currentWork, boolean isApprovals,HashMap<String, BOQBean> workAreaNoChangesInPrevWD, List<String> workAreaGroupNoChangesInPrevWD) { //
		//if(errMsg.size()!=100){return;}	// just in case to skip this method functionality
		System.out.println("workAreaGroupChangesInPrevWD.size() - "+workAreaGroupChangesInPrevWD.size());System.out.println(">"+workAreaChangesInPrevWD);System.out.println(">"+workAreaGroupChangesInPrevWD);
		if(workAreaGroupChangesInPrevWD.size()==0){return;}
		String workDescriptionId = workAreaGroupChangesInPrevWD.get(0).split("@@")[0];
		objBOQDao.getWDdataInDB(workDescriptionId,BOQSeqNo,workAreaNoChangesInPrevWD,workAreaGroupNoChangesInPrevWD);
		//recordType+"@@"+workDescriptionId + "@@" + blockId + "@@" + floorId;
		List<String> delete_workAreaGroup = new ArrayList<String>();
		for(String workAreaGroup : workAreaGroupNoChangesInPrevWD){
			boolean deleteCondition1 = false;
			boolean deleteCondition2 = false;
			if(workAreaNoChangesInPrevWD.get("LABOR@@"+workAreaGroup)!=null){
				if(workAreaChangesInPrevWD.get("LABOR@@"+workAreaGroup)!=null){
					deleteCondition1 = true;
				}else{deleteCondition1 = false;}
			}
			else{deleteCondition1 = true;}
			if(workAreaNoChangesInPrevWD.get("MATERIAL@@"+workAreaGroup)!=null){
				if(workAreaChangesInPrevWD.get("MATERIAL@@"+workAreaGroup)!=null){
					deleteCondition2 = true;
				}else{deleteCondition1 = false;}
			}else{deleteCondition1 = true;}
			if(deleteCondition1 && deleteCondition2){
				delete_workAreaGroup.add(workAreaGroup);
			}
		}
		for(String workAreaGroup : delete_workAreaGroup){
			workAreaGroupNoChangesInPrevWD.removeAll(Collections.singleton(workAreaGroup));
		}
		
		Set<String> keySet = workAreaChangesInPrevWD.keySet();
		for(String key : keySet){
			workAreaNoChangesInPrevWD.remove(key);
		}
		System.out.println("workAreaNoChangesInPrevWD:"+workAreaNoChangesInPrevWD);
		// TODO Auto-generated method stub
		/*for(String workAreaGroup : workAreaGroupChangesInPrevWD){
			if(workAreaGroupNoChangesInPrevWD.contains(workAreaGroup)){
				double wointiateArea1 = 0;
				double wointiateArea2 = 0;
				if(workAreaChangesInPrevWD.get("LABOR@@"+workAreaGroup)!=null){
					wointiateArea1 =  Double.valueOf(workAreaChangesInPrevWD.get("LABOR@@"+workAreaGroup)==null?"0":workAreaChangesInPrevWD.get("LABOR@@"+workAreaGroup).getWoWorkInitiateArea());
					wointiateArea2 = Double.valueOf(workAreaNoChangesInPrevWD.get("MATERIAL@@"+workAreaGroup)==null?"0":workAreaNoChangesInPrevWD.get("MATERIAL@@"+workAreaGroup).getWoWorkInitiateArea());
				}
				else{
					wointiateArea1 =  Double.valueOf(workAreaNoChangesInPrevWD.get("LABOR@@"+workAreaGroup)==null?"0":workAreaNoChangesInPrevWD.get("LABOR@@"+workAreaGroup).getWoWorkInitiateArea());
					wointiateArea2 = Double.valueOf(workAreaChangesInPrevWD.get("MATERIAL@@"+workAreaGroup)==null?"0":workAreaChangesInPrevWD.get("MATERIAL@@"+workAreaGroup).getWoWorkInitiateArea());
				}
				if(wointiateArea1>0 || wointiateArea2>0){
					System.out.println("wointiateArea1:"+wointiateArea1);
					System.out.println("wointiateArea2:"+wointiateArea2);
					throw new RuntimeException("Work Area Ratio changed on which WorkOrder generated");
				}
			}
		}*/
		
		for(String workAreaGroup : workAreaGroupChangesInPrevWD){
			BOQBean bean1 = null;
			BOQBean bean2 = null;
			if(workAreaChangesInPrevWD.get("LABOR@@"+workAreaGroup)!=null){
				bean1 = workAreaChangesInPrevWD.get("LABOR@@"+workAreaGroup);
			}
			if(workAreaChangesInPrevWD.get("MATERIAL@@"+workAreaGroup)!=null){
				bean2 = workAreaChangesInPrevWD.get("MATERIAL@@"+workAreaGroup);
			}
			if(bean1==null){
				if(workAreaNoChangesInPrevWD.get("LABOR@@"+workAreaGroup)!=null){
					bean1 = workAreaNoChangesInPrevWD.get("LABOR@@"+workAreaGroup);
				}
			}
			if(bean2==null){
				if(workAreaNoChangesInPrevWD.get("MATERIAL@@"+workAreaGroup)!=null){
					bean2 = workAreaNoChangesInPrevWD.get("MATERIAL@@"+workAreaGroup);
				}
			}
			if(bean1==null || bean2==null){
				System.out.println("bean1:"+bean1+"bean2:"+bean2);
				continue;
			}
			else{
				double wointiateArea1 = Double.valueOf(bean1.getWoWorkInitiateArea());
				double wointiateArea2 = Double.valueOf(bean2.getWoWorkInitiateArea());

				double presentArea1 = Double.valueOf(bean1.getWoWorkArea());
				double presentArea2 = Double.valueOf(bean2.getWoWorkArea());
				double blockArea1 = Double.valueOf(bean1.getStrArea());
				double blockArea2 = Double.valueOf(bean2.getStrArea());
				System.out.println("presentArea1: "+presentArea1);
				System.out.println("presentArea2: "+presentArea2);
				System.out.println("wointiateArea1: "+wointiateArea1);
				System.out.println("wointiateArea2: "+wointiateArea2);
				System.out.println("blockArea1: "+blockArea1);
				System.out.println("blockArea2: "+blockArea2);
				
				if(blockArea1>0&&blockArea2>0){
					if((blockArea1-wointiateArea1)==0&&(blockArea2-wointiateArea2)!=0 || (blockArea1-wointiateArea1)!=0&&(blockArea2-wointiateArea2)==0){
						if(!isApprovals){
							errMsg.add("In BOQ sheet, Line no. "+(rowNum+1)+", Work Order generated on complete Area. If want to increase area, then increase area in both LABOR & MATERIAL");
							throw new RuntimeException("In BOQ sheet, Line no. "+(rowNum+1)+", Work Order generated on complete Area. If want to increase area, then increase area in both LABOR & MATERIAL");
						}
						else{
							String workLocationName = getWorkLocationName(currentWork);
							errMsg.add(workLocationName + "Work Order generated on complete Area. If want to increase area, then increase area in both LABOR & MATERIAL");
							throw new RuntimeException(workLocationName + "Work Order generated on complete Area. If want to increase area, then increase area in both LABOR & MATERIAL");
						}
					}
				}


			}
			
			
		}
		workAreaChangesInPrevWD.clear();
		workAreaNoChangesInPrevWD.clear();
		workAreaGroupChangesInPrevWD.clear();
		workAreaGroupNoChangesInPrevWD.clear();
		
		
	}







	public void verifyWDsOfMajorHeadInDBandExcel(List<String> majorHead_WDids_inDB, List<String> majorHead_WDids_inExcel, List<String> errMsg, Map<String, String> wDids) {
		String wds_notFound = ""; 
		for(String wd_DB : majorHead_WDids_inDB){
			if(!majorHead_WDids_inExcel.contains(wd_DB)){
				wds_notFound += "'"+wDids.get(wd_DB)+"',";//
			}
		}
		if(StringUtils.isNotBlank(wds_notFound)){
			wds_notFound = wds_notFound.substring(0,wds_notFound.length()-1);
			errMsg.add("WorkDescription"+(wds_notFound.contains(",")?"s":"")+" '"+wds_notFound+"' are in DB. But they are not found in revised BOQ Excel.");
		}
	}







	public void validatePreviousWDtotal(double WDtotal_inExcel, double WDtotal_inDB, double WDtotal_Diff, List<String> errMsg, int rowNum) {
		System.out.println("WD Totals in Excel & IMS are "+WDtotal_inExcel+","+(WDtotal_inDB+WDtotal_Diff));
		if(Math.abs(WDtotal_inExcel-(WDtotal_inDB+WDtotal_Diff))>1){
			errMsg.add("Above Line no."+(rowNum+1)+", WD total is not Matched.");
			errMsg.add("WD Totals in Excel & IMS are "+WDtotal_inExcel+","+(WDtotal_inDB+WDtotal_Diff));
			System.out.println("WD total is not Matched. "+WDtotal_inExcel+","+"("+WDtotal_inDB+"+"+WDtotal_Diff+")");
			throw new RuntimeException("WD total is not Matched. "+WDtotal_inExcel+","+(WDtotal_inDB+WDtotal_Diff));
		}
		
	}







	public void checkingEDIT_hyphenGivenOrNotForSameConsecutiveFloors(String newBlocks,String actionInBeforeRow,String currentAction,int rowNum,List<String> errMsg){
		if(StringUtils.isNotBlank(newBlocks)){
			if(actionInBeforeRow.equals("EDIT")||actionInBeforeRow.equals("EDIT-MATERIAL")||currentAction.equals("EDIT")||currentAction.equals("EDIT-MATERIAL")||StringUtils.isBlank(actionInBeforeRow)||StringUtils.isBlank(currentAction)){
				errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",floor name appeared 2nd time. please give Action as EDIT-(blocknames) in both lines");
				throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+",floor name appeared 2nd time. please give Action as EDIT-(blocknames) in both lines");
			}
			if(actionInBeforeRow.contains("EDIT-")&&currentAction.contains("EDIT-")&&!actionInBeforeRow.equals("EDIT-MATERIAL")&&!currentAction.equals("EDIT-MATERIAL")){
				String eblocks1= actionInBeforeRow.contains("MATERIAL") ? actionInBeforeRow.split("EDIT-MATERIAL-")[1] : actionInBeforeRow.split("EDIT-")[1];
				String eblocks2= currentAction.contains("MATERIAL") ? currentAction.split("EDIT-MATERIAL-")[1] : currentAction.split("EDIT-")[1];
				for(char ch:eblocks1.toCharArray()){
					if(eblocks2.indexOf(ch)>=0){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",same block is editing 2nd time.");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+",same block is editing 2nd time.");
					}
				}
				for(char ch:newBlocks.toCharArray()){
					if(eblocks1.indexOf(ch)<0&&eblocks2.indexOf(ch)<0){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",new block is not editing.");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+",new block is not editing.");
					}
				}
			}
		}
		else{
			if(actionInBeforeRow.equals("EDIT")||currentAction.equals("EDIT")||actionInBeforeRow.equals("EDIT-MATERIAL")||currentAction.equals("EDIT-MATERIAL")){
				errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",floor name appeared 2nd time. please give Action as EDIT-(blocknames) in changed lines");
				throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+",floor name appeared 2nd time. please give Action as EDIT-(blocknames) in changed lines");
			}
			if(actionInBeforeRow.contains("EDIT-")&&currentAction.contains("EDIT-")&&!actionInBeforeRow.equals("EDIT-MATERIAL")&&!currentAction.equals("EDIT-MATERIAL")){
				String eblocks1= actionInBeforeRow.contains("MATERIAL") ? actionInBeforeRow.split("EDIT-MATERIAL-")[1] : actionInBeforeRow.split("EDIT-")[1];
				String eblocks2= currentAction.contains("MATERIAL") ? currentAction.split("EDIT-MATERIAL-")[1] : currentAction.split("EDIT-")[1];
				for(char ch:eblocks1.toCharArray()){
					if(eblocks2.indexOf(ch)>=0){
						errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+",same block is editing 2nd time.");
						throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+",same block is editing 2nd time.");
					}
				}
			}
		}
	}

	


	@Override
	public MultiObject getPreviousBOQTotal(String tempBOQNumber) {
		return objBOQDao.getPreviousBOQTotal(tempBOQNumber);
	}
	@Override
	public String approveReviseTempBOQ(HttpServletRequest request, HttpSession session,BOQBean objForOnlyPermanentBoqNumber, List<String> errMsg) {

		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		List<Exception> ex_list = new ArrayList<Exception>();
		String note="Note";
		String actualNote="Note";	
		String responce="";
		try {
			 note=request.getParameter("Note") == null ? "" : request.getParameter("Note");
			 actualNote=request.getParameter("actualNote") == null ? "" : request.getParameter("actualNote");
			String userId = session.getAttribute("UserId") == null ? "" : session.getAttribute("UserId").toString();
			String siteName = request.getParameter("SiteName") == null ? "" : request.getParameter("SiteName").toString();
			String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
			String strUserName = session.getAttribute("UserName") == null ? "": session.getAttribute("UserName").toString();
			String	tempBOQNumber = request.getParameter("tempBOQNo") == null ? "0" : request.getParameter("tempBOQNo");
			String nextLevelApproverEmpId=request.getParameter("nextLevelApproverEmpId")==null?"":request.getParameter("nextLevelApproverEmpId").toString();
			String boqApproverEmpId=request.getParameter("boqApproverEmpId")==null?"":request.getParameter("boqApproverEmpId").toString();
			String boqCreatedEmpId=request.getParameter("boqCreatedEmpId")==null?"":request.getParameter("boqCreatedEmpId").toString();
			String typeOfWork=request.getParameter("typeOfWork");
			
			BOQBean bean=new BOQBean();
			bean.setTypeOfWork(typeOfWork);
			bean.setStrSiteId(siteId);
			bean.setStrSiteName(siteName);
			bean.setStrUserId(userId);
			bean.setIntTempBOQNo(Integer.valueOf(tempBOQNumber));
			bean.setStrApproverEmpId(nextLevelApproverEmpId);
			bean.setStrTepBOQCreatedEmployeId(boqCreatedEmpId);
			bean.setStrTempBOQApproveEmployeeId(boqApproverEmpId);
			bean.setStrOperationType("A");
			int portNumber=request.getLocalPort();
			bean.setPortNumber(portNumber);
			if(!note.equals(actualNote)){
				//note = actualNote + "@@" + strUserName + " - " + note + "@@";
				note = strUserName + " - " + note ;
				bean.setStrRemarks(note);
			}
			//checking conditions related to work order at approvals also (same conditions that are at the time of uploading). 
			validateAreaAndPricesOfBoqAndWorkOrder(Integer.valueOf(tempBOQNumber),typeOfWork,siteId,errMsg);
			int result=objBOQDao.approveRejectDeatilsForBOQ(bean);
			
			
			result=objBOQDao.approveReviseTempBOQ(bean,objForOnlyPermanentBoqNumber);
			if(StringUtils.isNotBlank(objForOnlyPermanentBoqNumber.getStrBOQNo())){
				objBOQDao.finalValidationOfGrandTotalInEveryTable(objForOnlyPermanentBoqNumber.getStrBOQNo()); 
			}
			//String s=null;s.trim();
			transactionManager.commit(status);
			 responce="success";
		} catch (Exception e) {
			transactionManager.rollback(status);
			mailException(e,0,ex_list);
			e.printStackTrace();
			 responce="failed";

		}
		return responce;
	}



	@Override
	public List<BOQBean> getBOQAllVersions(HttpServletRequest request, HttpSession session) {
		String siteId = request.getParameter("siteId") == null ? "0" : request.getParameter("siteId");
		String BOQSeqNo = request.getParameter("BOQSeqNo");
		
		return		objBOQDao.getBOQAllVersions(siteId,BOQSeqNo);
		
	}



	@Override
	public List<BOQBean> viewAllBlocksFloorsFlats(String siteId) {
		return		objBOQDao.viewAllBlocksFloorsFlats(siteId);
	}

	
		
	public void checkQuantityRateAndAmount(double quantity, double rate, double nos, double amount, int lineNo, List<String> errMsg) {
		if(Math.abs(((quantity*rate)/nos)-amount)>0.001){
			String msg = "In RA Sheet, Line no."+lineNo+", Amount is wrong for given Quantity & Rate";
			errMsg.add(msg);
			throw new RuntimeException(msg);
		}
		
	}
	//in this method, only TRIM is there, no LOWERCASE , because material names has to store in DB as per given in excel sheet
	//use LOWERCASE method where comparing both BOQ & RA sheets.
	public Object getCellValue(Cell cell, String cellType, String cellPropertyName, int lineNo, List<String> errMsg, boolean isValueBlankThrowMsg, boolean isThisRASheet) {
		Object cellValue = null;
		String errMsg_blank = "Line no. "+lineNo+", "+cellPropertyName+" is Empty.";
		if(isThisRASheet){ errMsg_blank="In RA Sheet, ".concat(errMsg_blank); }
		else{ errMsg_blank="In BOQ Sheet, ".concat(errMsg_blank); }
		
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			if(isValueBlankThrowMsg){
				errMsg.add(errMsg_blank);
				throw new RuntimeException(errMsg_blank);
			}
			else{
				if(cellType.equals("String")){
					return "";
				}
				if(cellType.equals("double")){
					return 0.0d;
				}
			}

		}
		if(cellType.equals("String")){
			String s = cell.getStringCellValue();
			if(StringUtils.isBlank(s)){
				if(isValueBlankThrowMsg){
					errMsg.add(errMsg_blank);
					throw new RuntimeException(errMsg_blank);
				}
				else{
					s = "";
				}
			}
			cellValue = s.trim();
		}
		if(cellType.equals("double")){
			double d = cell.getNumericCellValue();
			cellValue = d;
		}
		
		return cellValue;
	}
	
	/*public String trimValue(String inputValue) {
		try {
			inputValue = inputValue.trim();
		}
		catch (NullPointerException e) {
			inputValue = "";
		}
		return inputValue;
	}
	*/
	public HashMap<String,WDRateAnalysis> getRASheetData(Sheet sheet2, List<String> errMsg,boolean isReviseBOQ, String newBlocks){
		HashMap<String,WDRateAnalysis> RA_Sheet_Data = new HashMap<String,WDRateAnalysis>();
		HashSet<String> RA_Sheet_Data_DuplicateKeyCheck = new HashSet<String>();
		HashMap<String,String> MaterialGroupIdsMap = objBOQDao.getMaterialGroupIdsMap();
		HashMap<String,HashMap<String,String>> MaterialGroupMeasurementIdsMap = objBOQDao.getMaterialGroupMeasurementIdsMap();
		final int RA_IMScodeColumnIndex,MaterialColumnIndex,UnitColumnIndex,NosColumnIndex,QuantityColumnIndex,RateColumnIndex,AmountColumnIndex,RemarksColumnIndex,RA_ActionColumnIndex;
		if (!isReviseBOQ) {
			RA_ActionColumnIndex = -1;
			RA_IMScodeColumnIndex = 1;
			MaterialColumnIndex = 2;
			UnitColumnIndex = 3;
			NosColumnIndex = 4;
			QuantityColumnIndex = 5;
			RateColumnIndex = 6;
			AmountColumnIndex = 7;
			RemarksColumnIndex = 8;
		}
		else{
			RA_ActionColumnIndex = 1;
			RA_IMScodeColumnIndex = 2;
			MaterialColumnIndex = 3;
			UnitColumnIndex = 4;
			NosColumnIndex = 5;
			QuantityColumnIndex = 6;
			RateColumnIndex = 7;
			AmountColumnIndex = 8;
			RemarksColumnIndex = 9;
		}
		int rowsCount = sheet2.getLastRowNum();
		Row row = null;
		//int noOfColumns = row.getLastCellNum();
		
		String workDescription = null;
		double wdMaterialAmountPerUnit = 0.0;
		boolean isWDEdited = false; //for revise boq
		
		for (int rowNum = 0; rowNum <= rowsCount; rowNum++) {
			System.out.println(rowNum);
			row = sheet2.getRow(rowNum);
			
			if(row==null){
				continue;
			}
			Cell cell = row.getCell(RA_IMScodeColumnIndex,Row.RETURN_BLANK_AS_NULL);
			if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				continue;
			}
			String headerName = cell.getStringCellValue().trim().toLowerCase();
			/******************** this is only for revise boq ***********************/
			if (isReviseBOQ && StringUtils.isBlank(newBlocks)) {
				String Action = null;
				if (headerName.contains("wd")) {
					Cell cell6 = row.getCell(RA_ActionColumnIndex, Row.RETURN_BLANK_AS_NULL);
					if (cell6 == null || cell6.getCellType() == Cell.CELL_TYPE_BLANK) {
						if (StringUtils.isNotBlank(newBlocks)) {
							Action = "EDIT-" + newBlocks;
						}
					} else {
						Action = cell6.getStringCellValue();
						Action = Action.trim();
						Action = Action.toUpperCase();
						if (!Action.equals("ACTION") && !Action.equals("EDIT") && !Action.equals("DEL")
								&& !Action.equals("NEW") && !Action.contains("EDIT-") && !Action.equals("NOACTION")) {
							errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",check the keyword in Action column.");
							throw new RuntimeException("Action Keyword wrong.");
						}
					} 

					if (StringUtils.isNotBlank(Action)) {
						if(Action.contains("EDIT")){
							isWDEdited = true;
						}
						else{
							isWDEdited = false;
						}
					}
					else{
						isWDEdited = false;
					}
				
				}
				if(!isWDEdited){
					continue;  //this is the final goal of this revise boq code
				}
			}
			
			/***************** for revise boq - END *******************/			
			if(headerName.contains("wd")){
				wdMaterialAmountPerUnit = 0.0;
				workDescription = (String) getCellValue(row.getCell(MaterialColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Work Description",rowNum+1,errMsg,true,true);
				WDRateAnalysis objWD= new WDRateAnalysis();
				objWD.setWorkDescription(workDescription);
				List<MaterialDetails> materialsList = new ArrayList<MaterialDetails>();
				objWD.setMaterialsList(materialsList);
				if(RA_Sheet_Data.containsKey(workDescription)){throw new RuntimeException("WorkDescription duplicate in Rate Analysis Sheet");}
				RA_Sheet_Data.put(workDescription.toLowerCase(), objWD);
				if(RA_Sheet_Data_DuplicateKeyCheck.add(workDescription.toLowerCase())==false){
					errMsg.add("In RA Sheet, Line no."+rowNum+1+", WD '"+workDescription+"' is Duplicate."); // is Duplicate or Blank
					throw new RuntimeException();
				}
			}
			else if(headerName.contains("menu")&&rowNum<10){
				validateRASheetMenu(row.getCell(MaterialColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Material",rowNum+1,errMsg,true,true);
				validateRASheetMenu(row.getCell(UnitColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Unit",rowNum+1,errMsg,true,true);
				validateRASheetMenu(row.getCell(NosColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Nos",rowNum+1,errMsg,true,true);
				validateRASheetMenu(row.getCell(QuantityColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Quantity",rowNum+1,errMsg,true,true);
				validateRASheetMenu(row.getCell(RateColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Rate",rowNum+1,errMsg,true,true);
				validateRASheetMenu(row.getCell(AmountColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Amount",rowNum+1,errMsg,true,true);
				validateRASheetMenu(row.getCell(RemarksColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Remarks",rowNum+1,errMsg,true,true);
			}
			else if(headerName.contains("material")){
				String material = (String) getCellValue(row.getCell(MaterialColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Material",rowNum+1,errMsg,true,true);
				String unit = (String) getCellValue(row.getCell(UnitColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Unit",rowNum+1,errMsg,true,true);
				double nos = (Double) getCellValue(row.getCell(NosColumnIndex,Row.RETURN_BLANK_AS_NULL),"double","Nos",rowNum+1,errMsg,true,true);
				double quantity = (Double) getCellValue(row.getCell(QuantityColumnIndex,Row.RETURN_BLANK_AS_NULL),"double","Quantity",rowNum+1,errMsg,true,true); 
				double rate = (Double) getCellValue(row.getCell(RateColumnIndex,Row.RETURN_BLANK_AS_NULL),"double","Rate",rowNum+1,errMsg,true,true); 
				double amount = (Double) getCellValue(row.getCell(AmountColumnIndex,Row.RETURN_BLANK_AS_NULL),"double","Amount",rowNum+1,errMsg,true,true); 
				String remarks = (String) getCellValue(row.getCell(RemarksColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Remarks",rowNum+1,errMsg,false,true); 
				checkQuantityRateAndAmount(quantity,rate,nos,amount,rowNum+1,errMsg);
				wdMaterialAmountPerUnit += amount; 
				MaterialDetails objMaterialDetails= new MaterialDetails();
				objMaterialDetails.setMaterial(material);
				String materialGroupId = MaterialGroupIdsMap.get(material.toLowerCase());
				if(materialGroupId==null){errMsg.add("In RA Sheet, Line no. "+(rowNum+1)+", Material '"+material+"' not found in Master Data, Please Check once.");throw new RuntimeException();}
				objMaterialDetails.setMaterialGroupId(materialGroupId);
				objMaterialDetails.setUnit(unit);
				String materialGroupMeasurementId = MaterialGroupMeasurementIdsMap.get(materialGroupId).get(unit.toLowerCase());
				if(materialGroupMeasurementId==null){errMsg.add("In RA Sheet, Line no. "+(rowNum+1)+", UOM '"+unit+"' not found in Master Data, Please Check once.");throw new RuntimeException();}
				objMaterialDetails.setMaterialGroupMeasurementId(materialGroupMeasurementId);
				objMaterialDetails.setNos(nos);
				objMaterialDetails.setPerUnitQuantity(quantity);
				objMaterialDetails.setRate(rate);
				objMaterialDetails.setPerUnitAmount(amount);
				objMaterialDetails.setRemarks(remarks);
				RA_Sheet_Data.get(workDescription.toLowerCase()).getMaterialsList().add(objMaterialDetails);
				if(workDescription==null){throw new RuntimeException();} //no need this line, just for safety
			}
			else if(headerName.contains("total")){
				String unit = (String) getCellValue(row.getCell(UnitColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Unit",rowNum+1,errMsg,true,true);
				double amount = (Double) getCellValue(row.getCell(AmountColumnIndex,Row.RETURN_BLANK_AS_NULL),"double","Amount",rowNum+1,errMsg,true,true); 
				if(Math.abs(wdMaterialAmountPerUnit-amount)>0.001){
					String msg = "In RA Sheet, Line no."+(rowNum+1)+", Total Amount is given wrong.";
					errMsg.add(msg);
					throw new RuntimeException(msg);
				}
				RA_Sheet_Data.get(workDescription.toLowerCase()).setWdMaterialUOM(unit);
				RA_Sheet_Data.get(workDescription.toLowerCase()).setWdMaterialAmountPerUnit(amount);	
				RA_Sheet_Data.put(workDescription.toLowerCase()+"@@"+unit.toLowerCase()+"@@"+amount, RA_Sheet_Data.remove(workDescription.toLowerCase()));
				if(workDescription==null){throw new RuntimeException();} //no need this line, just for safety
				
				workDescription=null;
			}
		}
		System.out.println("RA sheet reading completed");
		//throw new RuntimeException();
		return RA_Sheet_Data;
	}
	
	
	







	public void dummy() {
		int QS_TEMP_BOQ_PRODUCT_DTLS_ID = 1;
		String WO_WORK_AREA_ID = "WOAREA1111";
		String WO_WORK_AREA = "10";
		String MATERIAL_GROUP_ID = "MG001";
		String MATERIAL_GROUP_MEASUREMENT_ID = "MGM001";
		double PER_UNIT_QUANTITY = 0.1222;
		double PER_UNIT_AMOUNT = 0.5;
		double TOTAL_QUANTITY = 10.56;
		double TOTAL_AMOUNT = 100.78;
		String STATUS = "A";
		String REMARKS = "--";
		String SITE_ID = "007";
		for(int i =1;i<=1000;i++)
		{
			objBOQDao.dummy(QS_TEMP_BOQ_PRODUCT_DTLS_ID,WO_WORK_AREA_ID,WO_WORK_AREA,MATERIAL_GROUP_ID,MATERIAL_GROUP_MEASUREMENT_ID,PER_UNIT_QUANTITY,PER_UNIT_AMOUNT,TOTAL_QUANTITY,TOTAL_AMOUNT,STATUS,REMARKS,SITE_ID);
		}
	}

	public HashMap<String,String> getBOQSheetMaterialWDs(Sheet sheet,List<String> errMsg,int rowsCount,int IMScodeColumnIndex,int DescriptionColumnIndex,int UnitOfMeasurementColumnIndex,int MaterialRatePerUnitColumnIndex,boolean isReviseBOQ,int ActionColumnIndex, String newBlocks){
		/********************************** GETTING WDs & Validations - START *********************************/
		HashMap<String,String> BOQ_Sheet_MaterialWDs = new HashMap<String,String>();
		boolean isMajorHeadTotalGiven = true;
		boolean isGrandTotalGiven = false;
		String workDescription = null;
		String measurementName = null;
		double materialRatePerUnit = 0.0;
		for (int rowNum = 0; rowNum <= rowsCount; rowNum++) {
			System.out.println(rowNum);
			Row row = sheet.getRow(rowNum);
			 
			if(row==null){
				continue;
			}
			
			Cell cell = row.getCell(IMScodeColumnIndex,Row.RETURN_BLANK_AS_NULL);
			if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
				continue;
			}
			String headerName = cell.getStringCellValue().toLowerCase();
			
			if(headerName.contains("major")&&headerName.contains("head")&&!headerName.contains("total")){
				if(isMajorHeadTotalGiven==true){isMajorHeadTotalGiven = false;}
				else{
					errMsg.add("Give MajorHeadTotals for all MajorHeads");
					throw new RuntimeException("Give MajorHeadTotals for all MajorHeads");
				}
			}
			if(headerName.contains("major")&&headerName.contains("head")&&headerName.contains("total")){
				if(isMajorHeadTotalGiven==false){isMajorHeadTotalGiven = true;}
				else{
					//errMsg.add("Major head spelling is improper in IMS CODE");
					errMsg.add("In BOQ Sheet, Line no. "+(rowNum+1)+", IMS code is improper.");
					throw new RuntimeException("In BOQ Sheet, Line no. "+(rowNum+1)+", IMS code is improper. Major head spelling is improper in IMS CODE.");
				}
			}
			if(headerName.contains("grand")&&headerName.contains("total")){
				if(isMajorHeadTotalGiven==true){}
				else{
					errMsg.add("Give MajorHeadTotals for all MajorHeads");
					throw new RuntimeException("Give MajorHeadTotals for all MajorHeads");
				}
				isGrandTotalGiven = true;
			}
			
			//this is only for revise boq
			if (isReviseBOQ && StringUtils.isBlank(newBlocks)) {
				String Action = null;
				Cell cell6 = row.getCell(ActionColumnIndex, Row.RETURN_BLANK_AS_NULL);
				if (cell6 == null || cell6.getCellType() == Cell.CELL_TYPE_BLANK) {
					if (StringUtils.isNotBlank(newBlocks)) {
						Action = "EDIT-" + newBlocks;
					}
				} else {
					Action = cell6.getStringCellValue();
					Action = Action.trim();
					Action = Action.toUpperCase();
					if (!Action.equals("ACTION") && !Action.equals("EDIT") && !Action.equals("DEL")
							&& !Action.equals("NEW") && !Action.contains("EDIT-") && !Action.equals("NOACTION")) {
						errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",check the keyword in Action column.");
						throw new RuntimeException("Action Keyword wrong.");
					}
				}
				if (StringUtils.isBlank(Action)) {
					continue;
				}
				else{
					if(Action.contains(" ")){
						errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ", Remove spaces in Action Keyword.");
						throw new RuntimeException("Remove spaces in Action Keyword.");
					}
				}
				if((Action.contains("EDIT")&&!Action.contains("MATERIAL")) || (Action.contains("DEL"))) {
					continue;
				}
			}
			//-----END-----
			if(headerName.contains("wd")&&headerName.contains("block")){
				workDescription = null;
				measurementName = null;
				materialRatePerUnit = 0.0;
				workDescription = (String) getCellValue(row.getCell(DescriptionColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Work Description",rowNum+1,errMsg,true,false);
				measurementName = (String) getCellValue(row.getCell(UnitOfMeasurementColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","UOM",rowNum+1,errMsg,true,false);
				materialRatePerUnit = (Double) getCellValue(row.getCell(MaterialRatePerUnitColumnIndex,Row.RETURN_BLANK_AS_NULL),"double","Material Rate per Unit",rowNum+1,errMsg,true,false); 
				if(materialRatePerUnit != 0){
					BOQ_Sheet_MaterialWDs.put(workDescription.toLowerCase()+"@@"+measurementName.toLowerCase()+"@@"+materialRatePerUnit,workDescription);
				}
			}
			if(headerName.contains("wd")&&headerName.contains("floor")){
				workDescription = null;
				measurementName = null;
				materialRatePerUnit = 0.0;
				workDescription = (String) getCellValue(row.getCell(DescriptionColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","Work Description",rowNum+1,errMsg,true,false);
				
			}
			if(headerName.contains("floor")&&!headerName.contains("wd")){
				measurementName = null;
				materialRatePerUnit = 0.0;
				measurementName = (String) getCellValue(row.getCell(UnitOfMeasurementColumnIndex,Row.RETURN_BLANK_AS_NULL),"String","UOM",rowNum+1,errMsg,true,false);
				materialRatePerUnit = (Double) getCellValue(row.getCell(MaterialRatePerUnitColumnIndex,Row.RETURN_BLANK_AS_NULL),"double","Material Rate per Unit",rowNum+1,errMsg,true,false); 
				if(materialRatePerUnit != 0){
					BOQ_Sheet_MaterialWDs.put(workDescription.toLowerCase()+"@@"+measurementName.toLowerCase()+"@@"+materialRatePerUnit,workDescription);
				}
				
			}
			
		}
		if(!isGrandTotalGiven){
			errMsg.add("GrandTotal Not Given for IMS code In Excel");
			throw new RuntimeException("GrandTotal Not Given In Excel");
		}
		/**********************************  GETTING WDs & Validations - END  *********************************/
		return BOQ_Sheet_MaterialWDs;
	}

	public void compareAndInsertTempProdDtlsAndQtyChgDtls(String workAreaId, int reviseBOQChangedDetailsId, double presentArea, double blockArea,
			WDRateAnalysis present_objWDRA, WDRateAnalysis revised_objWDRA, String boqSiteId, int tempBOQNo,
			String Action_in_AM, boolean onlyAreaEdited_MaterialsNotEdited) {
		List<BOQProductDetailsDto> QsTempBOQProductDtls_Insert = new ArrayList<BOQProductDetailsDto>();
		List<ReviseBOQQtyChangedDtlsDto> ReviseBOQQtyChangedDtls_Insert = new ArrayList<ReviseBOQQtyChangedDtlsDto>();
		
		List<MaterialDetails> presentMaterials = present_objWDRA.getMaterialsList();
		List<MaterialDetails> revisedMaterials = null;
		if(onlyAreaEdited_MaterialsNotEdited && presentArea!=0){
			revisedMaterials = new ArrayList<MaterialDetails>();
			revisedMaterials.addAll(presentMaterials);
		}else{
			revisedMaterials = revised_objWDRA.getMaterialsList();	
		}
		
		if(!Action_in_AM.equals("NEW")){	//if 'NEW' don't iterate this present materials data
			for(MaterialDetails objPresentMD : presentMaterials){
				int reviseBOQQuantityChangedDetailsId = objBOQDao.getReviseBOQQuantityChangedDetailsId();
				String oldMaterialGroupId = objPresentMD.getMaterialGroupId();
				String oldMaterialGroupMeasurementId = objPresentMD.getMaterialGroupMeasurementId();
				double oldPerUnitQuantity = objPresentMD.getPerUnitQuantity();
				double oldPerUnitAmount = objPresentMD.getPerUnitAmount();
				double oldTotalQuantity = objPresentMD.getTotalQuantity();
				double oldTotalAmount = objPresentMD.getTotalAmount();
				double newPerUnitQuantity = 0.0;
				double newPerUnitAmount = 0.0;
				double newTotalQuantity = 0.0;
				double newTotalAmount = 0.0;
				String action = "DEL";
				String remarks = "";
				String status = "A";
				if(!Action_in_AM.equals("DEL")){	//if 'DEL' don't execute this while loop comparing
					Iterator<MaterialDetails> itr = revisedMaterials.iterator();
					while(itr.hasNext()){
						MaterialDetails objRevisedMD = itr.next();
						String newMaterialGroupId = objRevisedMD.getMaterialGroupId();
						String newMaterialGroupMeasurementId = objRevisedMD.getMaterialGroupMeasurementId();
						if(oldMaterialGroupId.equals(newMaterialGroupId) && oldMaterialGroupMeasurementId.equals(newMaterialGroupMeasurementId)){
							newPerUnitQuantity = objRevisedMD.getPerUnitQuantity();
							newPerUnitAmount = objRevisedMD.getPerUnitAmount();
							newTotalQuantity = newPerUnitQuantity*blockArea;
							newTotalAmount = newPerUnitAmount*blockArea;
							remarks = objRevisedMD.getRemarks();
							itr.remove();
							action = "EDIT";
							break;
						}

					}
				}

				//insert temp prod
				BOQProductDetailsDto objQsTempBOQProductDtlsDto = new BOQProductDetailsDto();
				int qsTempBOQProductDetailsId = objBOQDao.getQsTempBOQProductDetailsId();
				objQsTempBOQProductDtlsDto.setQsTempBOQProductDetailsId(qsTempBOQProductDetailsId);
				objQsTempBOQProductDtlsDto.setWorkAreaId(workAreaId);
				objQsTempBOQProductDtlsDto.setWorkArea(blockArea);
				objQsTempBOQProductDtlsDto.setMaterialGroupId(oldMaterialGroupId);
				objQsTempBOQProductDtlsDto.setMaterialGroupMeasurementId(oldMaterialGroupMeasurementId);
				objQsTempBOQProductDtlsDto.setPerUnitQuantity(action.equals("DEL")?oldPerUnitQuantity:newPerUnitQuantity);
				objQsTempBOQProductDtlsDto.setPerUnitAmount(action.equals("DEL")?oldPerUnitAmount:newPerUnitAmount);
				objQsTempBOQProductDtlsDto.setTotalQuantity(action.equals("DEL")?oldTotalQuantity:newTotalQuantity);
				objQsTempBOQProductDtlsDto.setTotalAmount(action.equals("DEL")?oldTotalAmount:newTotalAmount);
				objQsTempBOQProductDtlsDto.setStatus(status);
				objQsTempBOQProductDtlsDto.setRemarks(remarks);
				objQsTempBOQProductDtlsDto.setSiteId(boqSiteId);
				objQsTempBOQProductDtlsDto.setTempBOQNo(tempBOQNo);
				objQsTempBOQProductDtlsDto.setAction(action);
				QsTempBOQProductDtls_Insert.add(objQsTempBOQProductDtlsDto);


				//insert qty chnges
				ReviseBOQQtyChangedDtlsDto objReviseBOQQtyChangedDtlsDto = new ReviseBOQQtyChangedDtlsDto();
				objReviseBOQQtyChangedDtlsDto.setReviseBOQQuantityChangedDetailsId(reviseBOQQuantityChangedDetailsId);
				objReviseBOQQtyChangedDtlsDto.setReviseBOQChangedDetailsId(reviseBOQChangedDetailsId);
				objReviseBOQQtyChangedDtlsDto.setMaterialGroupId(oldMaterialGroupId);
				objReviseBOQQtyChangedDtlsDto.setMaterialGroupMeasurementId(oldMaterialGroupMeasurementId);
				objReviseBOQQtyChangedDtlsDto.setOldPerUnitQuantity(oldPerUnitQuantity);
				objReviseBOQQtyChangedDtlsDto.setNewPerUnitQuantity(newPerUnitQuantity);
				objReviseBOQQtyChangedDtlsDto.setOldPerUnitAmount(oldPerUnitAmount);
				objReviseBOQQtyChangedDtlsDto.setNewPerUnitAmount(newPerUnitAmount);
				objReviseBOQQtyChangedDtlsDto.setOldTotalQuantity(oldTotalQuantity);
				objReviseBOQQtyChangedDtlsDto.setNewTotalQuantity(newTotalQuantity);
				objReviseBOQQtyChangedDtlsDto.setOldTotalAmount(oldTotalAmount);
				objReviseBOQQtyChangedDtlsDto.setNewTotalAmount(newTotalAmount);
				objReviseBOQQtyChangedDtlsDto.setRemarks(remarks);
				objReviseBOQQtyChangedDtlsDto.setStatus(status);
				objReviseBOQQtyChangedDtlsDto.setAction(action);
				objReviseBOQQtyChangedDtlsDto.setTempBOQNo(tempBOQNo);
				ReviseBOQQtyChangedDtls_Insert.add(objReviseBOQQtyChangedDtlsDto);
			}
		}
		if(!Action_in_AM.equals("DEL")){	//if 'DEL' don't iterate this revised materials data
			for(MaterialDetails objRevisedMD : revisedMaterials){
				int reviseBOQQuantityChangedDetailsId = objBOQDao.getReviseBOQQuantityChangedDetailsId();
				String newMaterialGroupId = objRevisedMD.getMaterialGroupId();
				String newMaterialGroupMeasurementId = objRevisedMD.getMaterialGroupMeasurementId();
				double newPerUnitQuantity = objRevisedMD.getPerUnitQuantity();
				double newPerUnitAmount = objRevisedMD.getPerUnitAmount();
				double newTotalQuantity = newPerUnitQuantity*blockArea;
				double newTotalAmount = newPerUnitAmount*blockArea;
				double oldPerUnitQuantity = 0.0;
				double oldPerUnitAmount = 0.0;
				double oldTotalQuantity = 0.0;
				double oldTotalAmount = 0.0;
				String action = "NEW";
				String remarks = objRevisedMD.getRemarks();
				String status = "A";

				//insert temp prod
				BOQProductDetailsDto objQsTempBOQProductDtlsDto = new BOQProductDetailsDto();
				int qsTempBOQProductDetailsId = objBOQDao.getQsTempBOQProductDetailsId();
				objQsTempBOQProductDtlsDto.setQsTempBOQProductDetailsId(qsTempBOQProductDetailsId);
				objQsTempBOQProductDtlsDto.setWorkAreaId(workAreaId);
				objQsTempBOQProductDtlsDto.setWorkArea(blockArea);
				objQsTempBOQProductDtlsDto.setMaterialGroupId(newMaterialGroupId);
				objQsTempBOQProductDtlsDto.setMaterialGroupMeasurementId(newMaterialGroupMeasurementId);
				objQsTempBOQProductDtlsDto.setPerUnitQuantity(newPerUnitQuantity);
				objQsTempBOQProductDtlsDto.setPerUnitAmount(newPerUnitAmount);
				objQsTempBOQProductDtlsDto.setTotalQuantity(newTotalQuantity);
				objQsTempBOQProductDtlsDto.setTotalAmount(newTotalAmount);
				objQsTempBOQProductDtlsDto.setStatus(status);
				objQsTempBOQProductDtlsDto.setRemarks(remarks);
				objQsTempBOQProductDtlsDto.setSiteId(boqSiteId);
				objQsTempBOQProductDtlsDto.setTempBOQNo(tempBOQNo);
				objQsTempBOQProductDtlsDto.setAction(action);
				QsTempBOQProductDtls_Insert.add(objQsTempBOQProductDtlsDto);



				//insert qty chnges
				ReviseBOQQtyChangedDtlsDto objReviseBOQQtyChangedDtlsDto = new ReviseBOQQtyChangedDtlsDto();
				objReviseBOQQtyChangedDtlsDto.setReviseBOQQuantityChangedDetailsId(reviseBOQQuantityChangedDetailsId);
				objReviseBOQQtyChangedDtlsDto.setReviseBOQChangedDetailsId(reviseBOQChangedDetailsId);
				objReviseBOQQtyChangedDtlsDto.setMaterialGroupId(newMaterialGroupId);
				objReviseBOQQtyChangedDtlsDto.setMaterialGroupMeasurementId(newMaterialGroupMeasurementId);
				objReviseBOQQtyChangedDtlsDto.setOldPerUnitQuantity(oldPerUnitQuantity);
				objReviseBOQQtyChangedDtlsDto.setNewPerUnitQuantity(newPerUnitQuantity);
				objReviseBOQQtyChangedDtlsDto.setOldPerUnitAmount(oldPerUnitAmount);
				objReviseBOQQtyChangedDtlsDto.setNewPerUnitAmount(newPerUnitAmount);
				objReviseBOQQtyChangedDtlsDto.setOldTotalQuantity(oldTotalQuantity);
				objReviseBOQQtyChangedDtlsDto.setNewTotalQuantity(newTotalQuantity);
				objReviseBOQQtyChangedDtlsDto.setOldTotalAmount(oldTotalAmount);
				objReviseBOQQtyChangedDtlsDto.setNewTotalAmount(newTotalAmount);
				objReviseBOQQtyChangedDtlsDto.setRemarks(remarks);
				objReviseBOQQtyChangedDtlsDto.setStatus(status);
				objReviseBOQQtyChangedDtlsDto.setAction(action);
				objReviseBOQQtyChangedDtlsDto.setTempBOQNo(tempBOQNo);
				ReviseBOQQtyChangedDtls_Insert.add(objReviseBOQQtyChangedDtlsDto);

			}
		}
		objBOQDao.insertQsTempBOQProductDetails(QsTempBOQProductDtls_Insert);
		objBOQDao.insertReviseBOQQuantityChangedDetails(ReviseBOQQtyChangedDtls_Insert);

	}
	//not using
	public void beforeWriting_compareAndInsertTempProdDtlsAndQtyChgDtls(String workAreaId, int reviseBOQChangedDetailsId, double presentArea, double blockArea,
			WDRateAnalysis present_objWDRA, WDRateAnalysis revised_objWDRA, String boqSiteId, int tempBOQNo,
			String Action_in_AM) {
		List<BOQProductDetailsDto> QsTempBOQProductDtls_Insert = new ArrayList<BOQProductDetailsDto>();
		List<ReviseBOQQtyChangedDtlsDto> ReviseBOQQtyChangedDtls_Insert = new ArrayList<ReviseBOQQtyChangedDtlsDto>();
		
		List<MaterialDetails> presentMaterials = present_objWDRA.getMaterialsList();
		List<MaterialDetails> revisedMaterials = revised_objWDRA.getMaterialsList();
		for(MaterialDetails objPresentMD : presentMaterials){
			int reviseBOQQuantityChangedDetailsId = objBOQDao.getReviseBOQQuantityChangedDetailsId();
			String oldMaterialGroupId = objPresentMD.getMaterialGroupId();
			String oldMaterialGroupMeasurementId = objPresentMD.getMaterialGroupMeasurementId();
			double oldPerUnitQuantity = objPresentMD.getPerUnitQuantity();
			double oldPerUnitAmount = objPresentMD.getPerUnitAmount();
			double oldTotalQuantity = objPresentMD.getTotalQuantity();
			double oldTotalAmount = objPresentMD.getTotalAmount();
			double newPerUnitQuantity = 0.0;
			double newPerUnitAmount = 0.0;
			double newTotalQuantity = 0.0;
			double newTotalAmount = 0.0;
			String action = "DEL";
			String remarks = "";
			String status = "A";
			Iterator<MaterialDetails> itr = revisedMaterials.iterator();
			while(itr.hasNext()){
				MaterialDetails objRevisedMD = itr.next();
				String newMaterialGroupId = objRevisedMD.getMaterialGroupId();
				String newMaterialGroupMeasurementId = objRevisedMD.getMaterialGroupMeasurementId();
				newPerUnitQuantity = objRevisedMD.getPerUnitQuantity();
				newPerUnitAmount = objRevisedMD.getPerUnitAmount();
				newTotalQuantity = newPerUnitQuantity*blockArea;
				newTotalAmount = newPerUnitAmount*blockArea;
				remarks = objRevisedMD.getRemarks();
				if(oldMaterialGroupId.equals(newMaterialGroupId) && oldMaterialGroupMeasurementId.equals(newMaterialGroupMeasurementId)){

					//insert temp prod
					BOQProductDetailsDto objQsTempBOQProductDtlsDto = new BOQProductDetailsDto();
					int qsTempBOQProductDetailsId = objBOQDao.getQsTempBOQProductDetailsId();
					objQsTempBOQProductDtlsDto.setQsTempBOQProductDetailsId(qsTempBOQProductDetailsId);
					objQsTempBOQProductDtlsDto.setWorkAreaId(workAreaId);
					objQsTempBOQProductDtlsDto.setWorkArea(blockArea);
					objQsTempBOQProductDtlsDto.setMaterialGroupId(newMaterialGroupId);
					objQsTempBOQProductDtlsDto.setMaterialGroupMeasurementId(newMaterialGroupMeasurementId);
					objQsTempBOQProductDtlsDto.setPerUnitQuantity(newPerUnitQuantity);
					objQsTempBOQProductDtlsDto.setPerUnitAmount(newPerUnitAmount);
					objQsTempBOQProductDtlsDto.setTotalQuantity(newTotalQuantity);
					objQsTempBOQProductDtlsDto.setTotalAmount(newTotalAmount);
					objQsTempBOQProductDtlsDto.setStatus(status);
					objQsTempBOQProductDtlsDto.setRemarks(remarks);
					objQsTempBOQProductDtlsDto.setSiteId(boqSiteId);
					objQsTempBOQProductDtlsDto.setTempBOQNo(tempBOQNo);
					objQsTempBOQProductDtlsDto.setAction(action);
					QsTempBOQProductDtls_Insert.add(objQsTempBOQProductDtlsDto);


					//insert qty chnges
					ReviseBOQQtyChangedDtlsDto objReviseBOQQtyChangedDtlsDto = new ReviseBOQQtyChangedDtlsDto();
					objReviseBOQQtyChangedDtlsDto.setReviseBOQQuantityChangedDetailsId(reviseBOQQuantityChangedDetailsId);
					objReviseBOQQtyChangedDtlsDto.setReviseBOQChangedDetailsId(reviseBOQChangedDetailsId);
					objReviseBOQQtyChangedDtlsDto.setMaterialGroupId(oldMaterialGroupId);
					objReviseBOQQtyChangedDtlsDto.setMaterialGroupMeasurementId(oldMaterialGroupMeasurementId);
					objReviseBOQQtyChangedDtlsDto.setOldPerUnitQuantity(oldPerUnitQuantity);
					objReviseBOQQtyChangedDtlsDto.setNewPerUnitQuantity(newPerUnitQuantity);
					objReviseBOQQtyChangedDtlsDto.setOldPerUnitAmount(oldPerUnitAmount);
					objReviseBOQQtyChangedDtlsDto.setNewPerUnitAmount(newPerUnitAmount);
					objReviseBOQQtyChangedDtlsDto.setOldTotalQuantity(oldTotalQuantity);
					objReviseBOQQtyChangedDtlsDto.setNewTotalQuantity(newTotalQuantity);
					objReviseBOQQtyChangedDtlsDto.setOldTotalAmount(oldTotalAmount);
					objReviseBOQQtyChangedDtlsDto.setNewTotalAmount(newTotalAmount);
					objReviseBOQQtyChangedDtlsDto.setRemarks(remarks);
					objReviseBOQQtyChangedDtlsDto.setStatus(status);
					objReviseBOQQtyChangedDtlsDto.setAction(action);
					objReviseBOQQtyChangedDtlsDto.setTempBOQNo(tempBOQNo);
					ReviseBOQQtyChangedDtls_Insert.add(objReviseBOQQtyChangedDtlsDto);




					itr.remove();
					action = "EDIT";
					break;
				}
				
			}
			
		}
		for(MaterialDetails objRevisedMD : revisedMaterials){
			int reviseBOQQuantityChangedDetailsId = objBOQDao.getReviseBOQQuantityChangedDetailsId();
			String newMaterialGroupId = objRevisedMD.getMaterialGroupId();
			String newMaterialGroupMeasurementId = objRevisedMD.getMaterialGroupMeasurementId();
			double newPerUnitQuantity = objRevisedMD.getPerUnitQuantity();
			double newPerUnitAmount = objRevisedMD.getPerUnitAmount();
			double newTotalQuantity = newPerUnitQuantity*blockArea;
			double newTotalAmount = newPerUnitAmount*blockArea;
			double oldPerUnitQuantity = 0.0;
			double oldPerUnitAmount = 0.0;
			double oldTotalQuantity = 0.0;
			double oldTotalAmount = 0.0;
			String action = "NEW";
			String remarks = objRevisedMD.getRemarks();
			String status = "A";
			
			
			
			//insert temp prod
			BOQProductDetailsDto objQsTempBOQProductDtlsDto = new BOQProductDetailsDto();
			int qsTempBOQProductDetailsId = objBOQDao.getQsTempBOQProductDetailsId();
			objQsTempBOQProductDtlsDto.setQsTempBOQProductDetailsId(qsTempBOQProductDetailsId);
			objQsTempBOQProductDtlsDto.setWorkAreaId(workAreaId);
			objQsTempBOQProductDtlsDto.setWorkArea(blockArea);
			objQsTempBOQProductDtlsDto.setMaterialGroupId(newMaterialGroupId);
			objQsTempBOQProductDtlsDto.setMaterialGroupMeasurementId(newMaterialGroupMeasurementId);
			objQsTempBOQProductDtlsDto.setPerUnitQuantity(newPerUnitQuantity);
			objQsTempBOQProductDtlsDto.setPerUnitAmount(newPerUnitAmount);
			objQsTempBOQProductDtlsDto.setTotalQuantity(newTotalQuantity);
			objQsTempBOQProductDtlsDto.setTotalAmount(newTotalAmount);
			objQsTempBOQProductDtlsDto.setStatus(status);
			objQsTempBOQProductDtlsDto.setRemarks(remarks);
			objQsTempBOQProductDtlsDto.setSiteId(boqSiteId);
			objQsTempBOQProductDtlsDto.setTempBOQNo(tempBOQNo);
			objQsTempBOQProductDtlsDto.setAction(action);
			QsTempBOQProductDtls_Insert.add(objQsTempBOQProductDtlsDto);
			
			
			
			//insert qty chnges
			ReviseBOQQtyChangedDtlsDto objReviseBOQQtyChangedDtlsDto = new ReviseBOQQtyChangedDtlsDto();
			objReviseBOQQtyChangedDtlsDto.setReviseBOQQuantityChangedDetailsId(reviseBOQQuantityChangedDetailsId);
			objReviseBOQQtyChangedDtlsDto.setReviseBOQChangedDetailsId(reviseBOQChangedDetailsId);
			objReviseBOQQtyChangedDtlsDto.setMaterialGroupId(newMaterialGroupId);
			objReviseBOQQtyChangedDtlsDto.setMaterialGroupMeasurementId(newMaterialGroupMeasurementId);
			objReviseBOQQtyChangedDtlsDto.setOldPerUnitQuantity(oldPerUnitQuantity);
			objReviseBOQQtyChangedDtlsDto.setNewPerUnitQuantity(newPerUnitQuantity);
			objReviseBOQQtyChangedDtlsDto.setOldPerUnitAmount(oldPerUnitAmount);
			objReviseBOQQtyChangedDtlsDto.setNewPerUnitAmount(newPerUnitAmount);
			objReviseBOQQtyChangedDtlsDto.setOldTotalQuantity(oldTotalQuantity);
			objReviseBOQQtyChangedDtlsDto.setNewTotalQuantity(newTotalQuantity);
			objReviseBOQQtyChangedDtlsDto.setOldTotalAmount(oldTotalAmount);
			objReviseBOQQtyChangedDtlsDto.setNewTotalAmount(newTotalAmount);
			objReviseBOQQtyChangedDtlsDto.setRemarks(remarks);
			objReviseBOQQtyChangedDtlsDto.setStatus(status);
			objReviseBOQQtyChangedDtlsDto.setAction(action);
			objReviseBOQQtyChangedDtlsDto.setTempBOQNo(tempBOQNo);
			ReviseBOQQtyChangedDtls_Insert.add(objReviseBOQQtyChangedDtlsDto);
			
		}
		
	}

	//not using
	public void compareAndInsertReviseBOQQuantityChangedDetails(WDRateAnalysis present_objWDRA, WDRateAnalysis revised_objWDRA, int reviseBOQChangedDetailsId,int tempBOQNo, double blockArea) {
		List<ReviseBOQQtyChangedDtlsDto> ReviseBOQQtyChangedDtls_Insert = new ArrayList<ReviseBOQQtyChangedDtlsDto>();
		
		List<MaterialDetails> presentMaterials = present_objWDRA.getMaterialsList();
		List<MaterialDetails> revisedMaterials = revised_objWDRA.getMaterialsList();
		for(MaterialDetails objPresentMD : presentMaterials){
			int reviseBOQQuantityChangedDetailsId = objBOQDao.getReviseBOQQuantityChangedDetailsId();
			String oldMaterialGroupId = objPresentMD.getMaterialGroupId();
			String oldMaterialGroupMeasurementId = objPresentMD.getMaterialGroupMeasurementId();
			double oldPerUnitQuantity = objPresentMD.getPerUnitQuantity();
			double oldPerUnitAmount = objPresentMD.getPerUnitAmount();
			double oldTotalQuantity = objPresentMD.getTotalQuantity();
			double oldTotalAmount = objPresentMD.getTotalAmount();
			double newPerUnitQuantity = 0.0;
			double newPerUnitAmount = 0.0;
			double newTotalQuantity = 0.0;
			double newTotalAmount = 0.0;
			String action = "DEL";
			String remarks = "";
			String status = "A";
			Iterator<MaterialDetails> itr = revisedMaterials.iterator();
			while(itr.hasNext()){
				MaterialDetails objRevisedMD = itr.next();
				String newMaterialGroupId = objRevisedMD.getMaterialGroupId();
				String newMaterialGroupMeasurementId = objRevisedMD.getMaterialGroupMeasurementId();
				if(oldMaterialGroupId.equals(newMaterialGroupId) && oldMaterialGroupMeasurementId.equals(newMaterialGroupMeasurementId)){
					newPerUnitQuantity = objRevisedMD.getPerUnitQuantity();
					newPerUnitAmount = objRevisedMD.getPerUnitAmount();
					newTotalQuantity = newPerUnitQuantity*blockArea;
					newTotalAmount = newPerUnitAmount*blockArea;
					remarks = objRevisedMD.getRemarks();
					itr.remove();
					action = "EDIT";
					break;
				}
				
			}
			ReviseBOQQtyChangedDtlsDto objReviseBOQQtyChangedDtlsDto = new ReviseBOQQtyChangedDtlsDto();
			objReviseBOQQtyChangedDtlsDto.setReviseBOQQuantityChangedDetailsId(reviseBOQQuantityChangedDetailsId);
			objReviseBOQQtyChangedDtlsDto.setReviseBOQChangedDetailsId(reviseBOQChangedDetailsId);
			objReviseBOQQtyChangedDtlsDto.setMaterialGroupId(oldMaterialGroupId);
			objReviseBOQQtyChangedDtlsDto.setMaterialGroupMeasurementId(oldMaterialGroupMeasurementId);
			objReviseBOQQtyChangedDtlsDto.setOldPerUnitQuantity(oldPerUnitQuantity);
			objReviseBOQQtyChangedDtlsDto.setNewPerUnitQuantity(newPerUnitQuantity);
			objReviseBOQQtyChangedDtlsDto.setOldPerUnitAmount(oldPerUnitAmount);
			objReviseBOQQtyChangedDtlsDto.setNewPerUnitAmount(newPerUnitAmount);
			objReviseBOQQtyChangedDtlsDto.setOldTotalQuantity(oldTotalQuantity);
			objReviseBOQQtyChangedDtlsDto.setNewTotalQuantity(newTotalQuantity);
			objReviseBOQQtyChangedDtlsDto.setOldTotalAmount(oldTotalAmount);
			objReviseBOQQtyChangedDtlsDto.setNewTotalAmount(newTotalAmount);
			objReviseBOQQtyChangedDtlsDto.setRemarks(remarks);
			objReviseBOQQtyChangedDtlsDto.setStatus(status);
			objReviseBOQQtyChangedDtlsDto.setAction(action);
			objReviseBOQQtyChangedDtlsDto.setTempBOQNo(tempBOQNo);
			ReviseBOQQtyChangedDtls_Insert.add(objReviseBOQQtyChangedDtlsDto);
		}
		for(MaterialDetails objRevisedMD : revisedMaterials){
			int reviseBOQQuantityChangedDetailsId = objBOQDao.getReviseBOQQuantityChangedDetailsId();
			String newMaterialGroupId = objRevisedMD.getMaterialGroupId();
			String newMaterialGroupMeasurementId = objRevisedMD.getMaterialGroupMeasurementId();
			double newPerUnitQuantity = objRevisedMD.getPerUnitQuantity();
			double newPerUnitAmount = objRevisedMD.getPerUnitAmount();
			double newTotalQuantity = newPerUnitQuantity*blockArea;
			double newTotalAmount = newPerUnitAmount*blockArea;
			double oldPerUnitQuantity = 0.0;
			double oldPerUnitAmount = 0.0;
			double oldTotalQuantity = 0.0;
			double oldTotalAmount = 0.0;
			String action = "NEW";
			String remarks = objRevisedMD.getRemarks();
			String status = "A";
			ReviseBOQQtyChangedDtlsDto objReviseBOQQtyChangedDtlsDto = new ReviseBOQQtyChangedDtlsDto();
			objReviseBOQQtyChangedDtlsDto.setReviseBOQQuantityChangedDetailsId(reviseBOQQuantityChangedDetailsId);
			objReviseBOQQtyChangedDtlsDto.setReviseBOQChangedDetailsId(reviseBOQChangedDetailsId);
			objReviseBOQQtyChangedDtlsDto.setMaterialGroupId(newMaterialGroupId);
			objReviseBOQQtyChangedDtlsDto.setMaterialGroupMeasurementId(newMaterialGroupMeasurementId);
			objReviseBOQQtyChangedDtlsDto.setOldPerUnitQuantity(oldPerUnitQuantity);
			objReviseBOQQtyChangedDtlsDto.setNewPerUnitQuantity(newPerUnitQuantity);
			objReviseBOQQtyChangedDtlsDto.setOldPerUnitAmount(oldPerUnitAmount);
			objReviseBOQQtyChangedDtlsDto.setNewPerUnitAmount(newPerUnitAmount);
			objReviseBOQQtyChangedDtlsDto.setOldTotalQuantity(oldTotalQuantity);
			objReviseBOQQtyChangedDtlsDto.setNewTotalQuantity(newTotalQuantity);
			objReviseBOQQtyChangedDtlsDto.setOldTotalAmount(oldTotalAmount);
			objReviseBOQQtyChangedDtlsDto.setNewTotalAmount(newTotalAmount);
			objReviseBOQQtyChangedDtlsDto.setRemarks(remarks);
			objReviseBOQQtyChangedDtlsDto.setStatus(status);
			objReviseBOQQtyChangedDtlsDto.setAction(action);
			objReviseBOQQtyChangedDtlsDto.setTempBOQNo(tempBOQNo);
			ReviseBOQQtyChangedDtls_Insert.add(objReviseBOQQtyChangedDtlsDto);
			
		}
		objBOQDao.insertReviseBOQQuantityChangedDetails(ReviseBOQQtyChangedDtls_Insert);
	}


/*	if(recordType.equals("MATERIAL")&&Action.contains("MATERIAL")){
		WDRateAnalysis objWDRA = RA_Sheet_Data.get(workDescription.toLowerCase()+"@@"+measurementName.toLowerCase()+"@@"+ratePerUnit);
		objBOQDao.insertQsTempBOQProductDtls(workAreaId,blockArea,objWDRA,boqSiteId);
		compareAndInsertReviseBOQQuantityChangedDetails(presentWorkAreaId,objWDRA,reviseBOQChangedDetailsId);
	}*/
	
	
	

	public void validateWorkOrderMaterials(WDRateAnalysis wo_objWDRA, WDRateAnalysis revised_objWDRA, List<String> errMsg, int rowNum) {
		List<MaterialDetails> woMaterials = wo_objWDRA.getMaterialsList();
		List<MaterialDetails> revisedMaterials = revised_objWDRA.getMaterialsList();
		for(MaterialDetails objwoMD : woMaterials){
			String wo_MG_ID = objwoMD.getMaterialGroupId();
			String wo_MGM_ID = objwoMD.getMaterialGroupMeasurementId();
			double wo_PUQ = objwoMD.getPerUnitQuantity();
			boolean matchFound = false;
			for(MaterialDetails objrevisedMD : revisedMaterials){
				String rev_MG_ID = objrevisedMD.getMaterialGroupId();
				String rev_MGM_ID = objrevisedMD.getMaterialGroupMeasurementId();
				double rev_PUQ = objrevisedMD.getPerUnitQuantity();
				if(wo_MG_ID.equals(rev_MG_ID)&&wo_MGM_ID.equals(rev_MGM_ID)){
					matchFound = true;
					if(rev_PUQ<wo_PUQ){
						errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Can not decrease Per Unit Quantity for materials, As already Work Order generated on this Work Description.");
						throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Can not decrease Per Unit Quantity for materials, As already Work Order generated on this Work Description.");
					}
				}
				
			}
			if(!matchFound){
				errMsg.add("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Can not remove materials, As already Work Order generated on this Work Description.");
				throw new RuntimeException("In BOQ Sheet, Line no. " + (rowNum + 1) + ",Can not remove materials, As already Work Order generated on this Work Description.");
			}
		}
		
	}


	public void mailException(final Exception e, final int rowNumMail, final List<Exception> ex_list){
		
		String emails = UIProperties.validateParams.getProperty("BOQ_MAIL") == null ? "" : UIProperties.validateParams.getProperty("BOQ_MAIL").toString();
		final String [] emailToAddress = emails.split(",");
		
		if(emailToAddress.length!=0){

			ExecutorService executorService = Executors.newFixedThreadPool(10);
			try{
				executorService.execute(new Runnable() {
					public void run() {
						EmailFunction objEmailFunction = new EmailFunction();
						objEmailFunction.mailEx(e,emailToAddress,rowNumMail,ex_list);
					}
				});
				executorService.shutdown();
			}catch(Exception e2){
				e2.printStackTrace();
				executorService.shutdown();
			}

		}
	}

	public void validateRASheetMenu(Cell cell, String cellType, String cellPropertyName, int lineNo, List<String> errMsg, boolean isValueBlankThrowMsg, boolean isThisRASheet) {
		String errMsg_blank = "Line no. "+lineNo+", "+cellPropertyName+" is Empty.";
		if(isThisRASheet){ errMsg_blank="In RA Sheet, ".concat(errMsg_blank); }
		else{ errMsg_blank="In BOQ Sheet, ".concat(errMsg_blank); }
		
		if (cell == null || cell.getCellType() == Cell.CELL_TYPE_BLANK) {
			if(isValueBlankThrowMsg){
				errMsg.add(errMsg_blank);
				throw new RuntimeException(errMsg_blank);
			}
		}
		if(cellType.equals("String")){
			String s = cell.getStringCellValue();
			if(StringUtils.isBlank(s)){
				if(isValueBlankThrowMsg){
					errMsg.add(errMsg_blank);
					throw new RuntimeException(errMsg_blank);
				}
			}
			else if(!s.equalsIgnoreCase(cellPropertyName)){
				errMsg.add("In RA Sheet, "+"Line no. "+lineNo+", '"+cellPropertyName+"' keyword does not found in the MENU.");
				throw new RuntimeException("In RA Sheet, "+"Line no. "+lineNo+", '"+cellPropertyName+"' keyword does not found in the MENU.");
				
			}
		}
		
		
	}

	public double roundToTwo(double input){
		BigDecimal bd = new BigDecimal(input).setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
    }







	@Override
	public boolean checkingIsItReallyPendindOnCurrentUser(String userId, String tempBOQNumber) {
		return objBOQDao.checkingIsItReallyPendindOnCurrentUser(userId, tempBOQNumber);
	}
	
	
	public void validateAreaAndPricesOfBoqAndWorkOrder(int tempBOQNo, String typeOfWork, String boqSiteId, List<String> errMsg){
		//other
				int rowNum=-1; //this rowNum does not work actually. just for to know this is not from uploading process.
				boolean isApprovals = true;
				//List<String> errMsg = new ArrayList<String>();
				HashMap<String,BOQBean> workAreaChangesInPrevWD = new HashMap<String,BOQBean>();
				List<String> workAreaGroupChangesInPrevWD = new ArrayList<String>();
				HashMap<String,BOQBean> workAreaNoChangesInPrevWD = new HashMap<String,BOQBean>();
				List<String> workAreaGroupNoChangesInPrevWD = new ArrayList<String>();
				BOQBean bean= new BOQBean();
				//String currentWork = "";
				
		//inputs
		int BOQSeqNo = objBOQDao.getBOQSeqNoOfCurrentActivePermanentBOQ(boqSiteId,typeOfWork);
				
		//int BOQSeqNo=0;
		//int tempBOQNo=0;
		
		//Get from Temp tables
		/*double blockArea=0;
		double ratePerUnit=0;
		String recordType = "";
		String workDescriptionId = "";
		String measurementId = "";
		String blockId = "";
		String floorId = "";
		String action = "";*/
		
		//delete ABOVE ALL that commented. they are just for error free.
		
		
		
		
		
		
		

		List<Map<String,Object>> list = objBOQDao.getTempBOQAreaMappingDataInOrderOfWD(tempBOQNo);
		String workDescriptionId_current = "";
		String currentWork = "";
		for(Map<String, Object> map : list){
			//String tempWorkAreaId = map.get("WO_WORK_AREA_ID")==null?"":map.get("WO_WORK_AREA_ID").toString();
			//String QS_TEMP_BOQ_DETAILS_ID = map.get("QS_TEMP_BOQ_DETAILS_ID")==null?"":map.get("QS_TEMP_BOQ_DETAILS_ID").toString();
			String workDescriptionId = map.get("WO_WORK_DESC_ID")==null?"":map.get("WO_WORK_DESC_ID").toString();
			if(workDescriptionId_current.equals("")){
				workDescriptionId_current = workDescriptionId;
			}
			else if(!workDescriptionId_current.equals(workDescriptionId)){
				System.out.println("clear check before"+workAreaChangesInPrevWD.size()+","+workAreaGroupChangesInPrevWD.size());
				validateAreaRatiosOfLaborAndMaterial(workAreaChangesInPrevWD,
						errMsg,  workAreaGroupChangesInPrevWD, BOQSeqNo,rowNum,currentWork,isApprovals, workAreaNoChangesInPrevWD, workAreaGroupNoChangesInPrevWD);//
				System.out.println("clear check after"+workAreaChangesInPrevWD.size()+","+workAreaGroupChangesInPrevWD.size());
				workDescriptionId_current = workDescriptionId;
			}
			
			String measurementId = map.get("WO_MEASURMENT_ID")==null?"":map.get("WO_MEASURMENT_ID").toString();
			String floorId = map.get("FLOOR_ID")==null?null:map.get("FLOOR_ID").toString();
			String blockId = map.get("BLOCK_ID")==null?"":map.get("BLOCK_ID").toString();
			String area = map.get("WO_WORK_AREA")==null?"":map.get("WO_WORK_AREA").toString();
			String price = map.get("QS_AREA_PRICE_PER_UNIT")==null?"":map.get("QS_AREA_PRICE_PER_UNIT").toString();
			String action = map.get("ACTION")==null?"":map.get("ACTION").toString();
			String recordType = map.get("RECORD_TYPE")==null?"":map.get("RECORD_TYPE").toString();
			double blockArea = Double.valueOf(area);
			double ratePerUnit = Double.valueOf(price);
			currentWork = recordType+"@@"+workDescriptionId + "@@" + blockId + "@@" + floorId;
			
			if(action.equals("EDIT")){
				bean = objBOQDao.getBOQAreaMappingOfParticularBlock(BOQSeqNo, workDescriptionId, measurementId, blockId, floorId, recordType);

				double presentPrice = Double.valueOf(bean.getQsAreaPricePerUnit());
				double presentArea = Double.valueOf(bean.getWoWorkArea());
				double WOIntiateArea = Double.valueOf(bean.getWoWorkInitiateArea());
				double woIssueAreaPrice = Double.valueOf(bean.getWoIssueAreaPrice());
				//String presentWorkAreaId = bean.getWorkAreaId();
				if (!(blockArea == 0 && presentArea == 0 && presentPrice == 0)) {
					if (roundToTwo(ratePerUnit) != presentPrice || blockArea != presentArea) {
						if (roundToTwo(ratePerUnit) < presentPrice) {
							if (roundToTwo(ratePerUnit) < woIssueAreaPrice) {
								if (Double.valueOf(WOIntiateArea) > 0) {
									String workLocationName = getWorkLocationName(currentWork);
									errMsg.add(workLocationName + "Can not decrease Price, As already Work Order generated on this Work Description.");
									throw new RuntimeException(workLocationName + "Can not decrease Price, As already Work Order generated on this Work Description.");
								}
							}
						}
						if (blockArea < WOIntiateArea) {
							String workLocationName = getWorkLocationName(currentWork);
							errMsg.add(workLocationName + "Can not decrease Quantity, As already Work Order generated on this Work Description.");
							throw new RuntimeException(workLocationName + "Can not decrease Quantity, As already Work Order generated on this Work Description.");
							//tested
						}
						if(      blockArea != presentArea ) {
							bean.setStrArea(String.valueOf(blockArea));
							bean.setRecordType(recordType);
							workAreaChangesInPrevWD.put(currentWork, bean);
							workAreaGroupChangesInPrevWD.add(workDescriptionId+"@@"+blockId+"@@"+floorId);
						}
					}
				}

			}//EDIT
			if (action.equals("NEW")) {
				
				bean.setWoWorkArea(String.valueOf(0));
				bean.setWoWorkInitiateArea(String.valueOf(0));
				bean.setStrArea(String.valueOf(blockArea));
				bean.setRecordType(recordType);
				workAreaChangesInPrevWD.put(currentWork, bean);
				workAreaGroupChangesInPrevWD.add(workDescriptionId+"@@"+blockId+"@@"+floorId);
			}//NEW
			if (action.equals("DEL")) {
				//double presentPrice = Double.valueOf(objBOQDao.getParticularBlockPrice(BOQSeqNo, workDescriptionId, measurementId, blockId, floorId, recordType));
				double presentArea = Double.valueOf(objBOQDao.getParticularBlockArea(BOQSeqNo, workDescriptionId, measurementId, blockId, floorId, recordType));
				double WOIntiateArea = Double.valueOf(objBOQDao.getWorkOrderIntiateArea(BOQSeqNo, workDescriptionId, measurementId, blockId, floorId, recordType));
				//String presentWorkAreaId = objBOQDao.getPresentWorkAreaId(BOQSeqNo, workDescriptionId, measurementId, blockId, floorId, recordType);
				
				bean.setWoWorkArea(String.valueOf(presentArea));
				bean.setWoWorkInitiateArea(String.valueOf(WOIntiateArea));
				bean.setStrArea(String.valueOf(0));
				bean.setRecordType(recordType);
				workAreaChangesInPrevWD.put(currentWork, bean);
				workAreaGroupChangesInPrevWD.add(workDescriptionId+"@@"+blockId+"@@"+floorId);
				
				if (WOIntiateArea > 0) {
					String workLocationName = getWorkLocationName(currentWork);
					errMsg.add(workLocationName + "Work Description Can not be deleted, As already Work Order generated on this Work Description.");
					throw new RuntimeException(workLocationName + "Work Order intiated on this Area. This Row can't be deleted.");
					//tested
				}
			}//DEL
		}//for-loop
		//after for loop also, this below method has to call to verify last WD.
		validateAreaRatiosOfLaborAndMaterial(workAreaChangesInPrevWD,
				errMsg,  workAreaGroupChangesInPrevWD, BOQSeqNo,rowNum,currentWork,isApprovals, workAreaNoChangesInPrevWD, workAreaGroupNoChangesInPrevWD);//
		
		
		
		
		
		
		
		
				

	}
	
	public String getWorkLocationName(String currentWork){
		String WorkLocationName = "";
		System.out.println("currentWork: "+currentWork);
		try{
			String recordType = currentWork.split("@@")[0];
			String workDescriptionId = currentWork.split("@@")[1];
			String blockId = currentWork.split("@@")[2];
			String floorId = currentWork.split("@@")[3];

			List<Map<String, Object>> list = objBOQDao.getWorkLocationName(recordType,workDescriptionId,blockId,floorId);
			System.out.println("size of list which return from the getWorkLocationName(): "+list.size());
			for(Map<String, Object> map : list) {
				String majorHeadDesc = map.get("WO_MAJORHEAD_DESC")==null ? "" : map.get("WO_MAJORHEAD_DESC").toString();
				String minorHeadDesc = map.get("WO_MINORHEAD_DESC")==null ? "" : map.get("WO_MINORHEAD_DESC").toString();
				String workDescription = map.get("WO_WORK_DESCRIPTION")==null ? "" : map.get("WO_WORK_DESCRIPTION").toString();
				String block = map.get("BLOCK_NAME")==null ? "" : map.get("BLOCK_NAME").toString();
				String floor = map.get("FLOOR_NAME")==null ? "" : map.get("FLOOR_NAME").toString();
				WorkLocationName = "In MajorHead '"+majorHeadDesc+"', MinorHead '"+minorHeadDesc+"', WorkDescription '"+workDescription+"', Block '"+block+"', ";
				if(StringUtils.isNotBlank(floor)){
					WorkLocationName += "Floor '"+floor+"', ";
				}
				WorkLocationName += "in "+recordType+" work, ";
				break;
			}
		}catch(Exception e){e.printStackTrace();}
		return WorkLocationName;
	}
	
	
}


