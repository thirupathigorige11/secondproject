package com.sumadhura.util;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class DownloadExcel {

	public void downloadExcel(HttpServletResponse response,String fileName,List<?> list, String[] columnHeadings,int[] columnWidths, String[] beanProperties, String beanClassName, HashMap<String, Object> extraDataMap ) throws Exception {

		String filename = generateFileName(fileName);
		File file = new File(filename+".xlsx"); 
		XSSFWorkbook workbook = new XSSFWorkbook(); 

		writeDataToExcelSheet(workbook,list,columnHeadings,columnWidths,beanProperties,beanClassName,extraDataMap);
		FileOutputStream out = new FileOutputStream(file);
		workbook.write(out);

		writeFileDataToResponse(response,file);

		out.close();
		//workbook.close(); 



	}
	
	public String generateFileName(String fileName) {
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH.mm.ss");  
	    Date date = new Date();  
	    return fileName+" "+formatter.format(date); 
	}
	
	public void writeDataToExcelSheet(XSSFWorkbook workbook, List<?> list, String[] columnHeadings,int[] columnWidths, String[] beanProperties, String beanClassName, HashMap<String, Object> extraDataMap) throws ClassNotFoundException {
		XSSFSheet spreadsheet = workbook.createSheet("Sheet1");
		int rowCount = -1;
		
        //spreadsheet.setDefaultColumnWidth(20);
		spreadsheet.createFreezePane(0, 1); 
		
		
		
		for(int i=0;i<columnHeadings.length;i++){
			spreadsheet.setColumnWidth(i, columnWidths[i]);
		}
       
		XSSFCellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		
		// create style for header cells
		font.setFontName("Arial");
		style.setFillForegroundColor(HSSFColor.BLUE.index);
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setColor(HSSFColor.WHITE.index);
		style.setFont(font);
        
		
		
		if(extraDataMap!=null){
			forBothPurchaseAndContractorTaxReportCommon(workbook, spreadsheet, style, font, rowCount, extraDataMap); rowCount=rowCount+3;
		}
		
		
		
		// create header row
		XSSFRow header = spreadsheet.createRow(++rowCount);
		
		for(int i=0;i<columnHeadings.length;i++){
			header.createCell(i).setCellValue(columnHeadings[i]);
			header.getCell(i).setCellStyle(style);
			header.getCell(i).setCellType ( Cell.CELL_TYPE_STRING ) ; 
		}
		
		
		
		// create data rows
		
		Class<?> clazz = Class.forName(beanClassName);
		
		for (int index = 0; index < list.size(); index++) {
			
			XSSFRow aRow = spreadsheet.createRow(++rowCount);
				PropertyDescriptor pd = null;
				Method getter = null;
				Object f = null;
				
				for (int i = 0; i < beanProperties.length; i++) {
					try {
						pd = new PropertyDescriptor(beanProperties[i], clazz);
						getter = pd.getReadMethod();
						f = getter.invoke(list.get(index));
					} catch (Exception e) {
						e.printStackTrace();
					}
					aRow.createCell(i).setCellValue((String) f);
				}
		}

		
	}
	
	public void writeFileDataToResponse(HttpServletResponse response, File file) {

		response.setContentType("application/vnd.ms-excel");
        response.addHeader("content-disposition", "attachment; filename="+file.getName());
        response.setHeader("Pragma", "public");
        response.setHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "max-age=0");
        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        final int size = 1024;
        try {
            response.setContentLength(fin.available());
            final byte[] buffer = new byte[size];
            ServletOutputStream outputStream = null;

            outputStream = response.getOutputStream();
            int length = 0;
            while ((length = fin.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }
            fin.close();
            outputStream.flush();
            outputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
	}

	public void forBothPurchaseAndContractorTaxReportCommon(XSSFWorkbook workbook, XSSFSheet spreadsheet, XSSFCellStyle style, Font font, int rowCount, HashMap<String, Object> extraDataMap){
		
			spreadsheet.createFreezePane(0, 4);
			
	        // create style for header cells
			style = workbook.createCellStyle();
			font = workbook.createFont();
			font.setFontName("Arial");
			//style.setFillForegroundColor(HSSFColor.BLUE.index);
			//style.setFillPattern(CellStyle.SOLID_FOREGROUND);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			//font.setColor(HSSFColor.WHITE.index);
			//font.setColor(HSSFColor.BLUE.index);
			style.setFont(font);
			
			
			
			
			
			
			XSSFCellStyle style1 = workbook.createCellStyle();
			style1=(XSSFCellStyle)style.clone();
			style1.setAlignment(CellStyle.ALIGN_CENTER);
			
			
			
			XSSFRow sumadhura = spreadsheet.createRow(++rowCount);
			sumadhura.createCell(0).setCellValue("SUMADHURA INFRACON PRIVATE LIMITED");
			spreadsheet.addMergedRegion(new CellRangeAddress(rowCount,rowCount,0,6));
			sumadhura.getCell(0).setCellStyle(style1);
			XSSFRow tinNo = spreadsheet.createRow(++rowCount);
			tinNo.createCell(0).setCellValue("TIN No :"+extraDataMap.get("tinNo").toString());
			spreadsheet.addMergedRegion(new CellRangeAddress(rowCount,rowCount,0,3));
			tinNo.getCell(0).setCellStyle(style);
			tinNo.createCell(4).setCellValue("GSTIN No :"+extraDataMap.get("gtinNo").toString());
			spreadsheet.addMergedRegion(new CellRangeAddress(rowCount,rowCount,4,7));
			tinNo.getCell(4).setCellStyle(style1);
			XSSFRow dates = spreadsheet.createRow(++rowCount);
			dates.createCell(0).setCellValue(extraDataMap.get("dates").toString());
			spreadsheet.addMergedRegion(new CellRangeAddress(rowCount,rowCount,0,6));
			dates.getCell(0).setCellStyle(style);
			
			
			
	}
	
	
}
