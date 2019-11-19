package com.sumadhura.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.itextpdf.text.pdf.codec.Base64;
import java.io.File;



import java.util.Random;
import java.util.TreeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;

import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;

import com.itextpdf.text.pdf.codec.Base64;
import com.sumadhura.bean.SiteDetails;


@Repository
public class CommonUtilities  {
	
	static Logger log = Logger.getLogger(CommonUtilities.class);



	@Autowired
	private  JdbcTemplate jdbcTemplate;
	
	public static String getStan()
	{
		String val = "";
		Random random = new Random();
		int randomInt = random.nextInt(8999) + 1000;		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		String todayStr = format.format(today);
		val = todayStr + randomInt;
		return val;
	}
	
	
	
	
	 // pad with " " to the right to the given length (n)
	  public static String padRight(String s, int n) {
	    return String.format("%1$-" + n + "s", s);
	  }

	  // pad with " " to the left to the given length (n)
	  public static String padLeft(String s, int n) {
	    return String.format("%1$" + n + "s", s);
	  }
	  public static String breakString(String param, char specialChar,int afterNoOfspecialChar){
			String result = "";
			//Controlled loop to count characters
			int count =1;
			for (int i = 0; i < param.length(); i++)
			{
				if (param.charAt(i) == specialChar)
				{
					count++;
					if(count == afterNoOfspecialChar){
						result += String.valueOf("&");
						count =1;
					}else{
						result += String.valueOf(param.charAt(i));
					}
				}else{
					result += String.valueOf(param.charAt(i));
				}
			}
			////System.out.println("Count Special Char:"+count);
			return result;
		}

	
	
	  public   Map<String, String> getSiteList() {

			Map<String, String> gst = null;
			List<Map<String, Object>> dbSiteList = null;

			gst = new TreeMap<String, String>();

			String gstSlabsQry = "SELECT SITE_ID, SITE_NAME FROM SITE where STATUS = 'ACTIVE' ";
			log.debug("Query to fetch gst slab = "+gstSlabsQry);

			dbSiteList = jdbcTemplate.queryForList(gstSlabsQry, new Object[]{});

			for(Map<String, Object> gstSlabs : dbSiteList) {
				gst.put(String.valueOf(gstSlabs.get("SITE_ID")).trim(), String.valueOf(gstSlabs.get("SITE_NAME")).trim());
			}
			return gst;
		}

/*****************************************************************po pdf download start**********************************************************/
		public static String encodeFileToBase64Binary(String fileName)
	    throws IOException {

	    	File file = new File(fileName);
	    	byte[] bytes = loadFile(file);
	    	String encoded = Base64.encodeBytes(bytes);
	    	String encodedString = new String(encoded);

	    	return encodedString;
	    }

	    public static byte[] loadFile(File file) throws IOException {
	    	FileInputStream is = new FileInputStream(file);

	    	long length = file.length();
	    	if (length > Integer.MAX_VALUE) {
	    		// File is too large
	    	}
	    	byte[] bytes = new byte[(int)length];

	    	int offset = 0;
	    	int numRead = 0;
	    	while (offset < bytes.length
	    			&& (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
	    		offset += numRead;
	    		}

	    		if (offset < bytes.length) {
	    			throw new IOException("Could not completely read file "+file.getName());
	    		}

	    		is.close();
	    		return bytes;
	    	}
		
		
	/************************************************************po pdf download ended******************************************************************/
	public static double round(double value, int places) {
		    if (places < 0) throw new IllegalArgumentException();

		    BigDecimal bd = new BigDecimal(value);
		    bd = bd.setScale(places, RoundingMode.HALF_UP);
		    return bd.doubleValue();
		}

 /*=================================================================site Location Details======================================================*/
	  
	  public static String getsiteLocationDetails() {
		  List<Map<String, Object>> productList = null;
		  final StringBuffer xmlData = new StringBuffer();
		  String bufferLocationdata="";
		  JdbcTemplate template = null;
		  String location = "";
			String sql = "";
		  try {
				template = new JdbcTemplate(DBConnection.getDbConnection());
				sql = "SELECT DISTINCT(ADDRESS) FROM SITE WHERE STATUS='ACTIVE'";
				productList = template.queryForList(sql, new Object[] {});
				if (null != productList && productList.size() > 0) {
					xmlData.append("<xml>");
					xmlData.append("<locationname>");
					for (Map siteId : productList) {
						location = siteId.get("ADDRESS") == null ? "" : siteId.get("ADDRESS").toString();
						if(location!=null && !location.equals("")){
							xmlData.append("<area>"+location+"</area>");
						}
							
					}
					xmlData.append("</locationname>");
					xmlData.append("</xml>");
				}
				int PRETTY_PRINT_INDENT_FACTOR = 4;
				JSONObject xmlJSONObj;
				xmlJSONObj = XML.toJSONObject(xmlData.toString());
				bufferLocationdata = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
				System.out.println(bufferLocationdata);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		  
		  return bufferLocationdata;
	  }
	 /* =======================================================get siteNames and Location Details==============================================*/
	  
	  public static String getSiteDetails() {

			String siteId = "";
			String siteName = "";
			String sql = "";
			List<Map<String, Object>> productList = null;
			final StringBuffer xmlData = new StringBuffer();
			String bufferLocationdata="";
			//Map<String, String> map = null;
			JdbcTemplate template = null;

			try {

				template = new JdbcTemplate(DBConnection.getDbConnection());
				sql = "SELECT SITE_ID, SITE_NAME FROM SITE WHERE STATUS='ACTIVE'";
				productList = template.queryForList(sql, new Object[] {});
				if (null != productList && productList.size() > 0) {
					xmlData.append("<xml>");
					
					for (Map siteDets : productList) {
						xmlData.append("<site>");
						siteId = siteDets.get("SITE_ID") == null ? "" : siteDets.get("SITE_ID").toString();
						siteName = siteDets.get("SITE_NAME") == null ? "" : siteDets.get("SITE_NAME").toString();
						xmlData.append("<SITEID>" +siteId+"</SITEID>");
						xmlData.append("<SITENAME><![CDATA["+siteName+"]]></SITENAME>");
						xmlData.append("</site>");
					}
					
					//xmlData.append(bufferMainProd);
					xmlData.append("</xml>");
				}
				int PRETTY_PRINT_INDENT_FACTOR = 4;
				JSONObject xmlJSONObj;
				xmlJSONObj = XML.toJSONObject(xmlData.toString());
				bufferLocationdata = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
				System.out.println(bufferLocationdata);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return bufferLocationdata;
		}
	  public static String loadAndSetMarketingLocationData() {

			String siteId = "";
			String siteName = "";
			String sql = "";
			List<Map<String, Object>> productList = null;
			final StringBuffer xmlData = new StringBuffer();
			String bufferLocationdata="";
			//Map<String, String> map = null;
			JdbcTemplate template = null;

			try {

				template = new JdbcTemplate(DBConnection.getDbConnection());
				sql = "SELECT MONTHLY_AVAIL_AREA_ID,SITE_ID,TOTAL_AREA,AVAILABLE_AREA  FROM MRKT_MONTHLY_AVAIL_AREA WHERE STATUS='A' and MONTH_YEAR='11-2018'";
				productList = template.queryForList(sql, new Object[] {});
				if (null != productList && productList.size() > 0) {
					xmlData.append("<xml>");
					
					for (Map siteDets : productList) {
						siteId = siteDets.get("SITE_ID") == null ? "" : siteDets.get("SITE_ID").toString();
						String monyhlyavailableId = siteDets.get("MONTHLY_AVAIL_AREA_ID") == null ? "" : siteDets.get("MONTHLY_AVAIL_AREA_ID").toString();
						String totalArea=siteDets.get("TOTAL_AREA") == null ? "" : siteDets.get("TOTAL_AREA").toString();
						String availablearea=siteDets.get("AVAILABLE_AREA") == null ? "" : siteDets.get("AVAILABLE_AREA").toString();
						
						
						xmlData.append("<MONTHLYAVAILABLEID>" + monyhlyavailableId+"</MONTHLYAVAILABLEID>");
						xmlData.append("<SITE_ID>" + siteId+"</SITE_ID>");
						xmlData.append("<TOTAL_AREA>" + totalArea+"</TOTAL_AREA>");
						xmlData.append("<AVAILABLE_AREA>" + availablearea+"</AVAILABLE_AREA>");
					}
					
					xmlData.append("</xml>");
				}
				int PRETTY_PRINT_INDENT_FACTOR = 4;
				JSONObject xmlJSONObj;
				xmlJSONObj = XML.toJSONObject(xmlData.toString());
				bufferLocationdata = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
				System.out.println(bufferLocationdata);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return bufferLocationdata;
		}
	  
	  /*=================================================get Area details start==========================================================*/
	  public static String loadAndSetMarketingAreaData(String childProductId) {

			String hoardingId = "";
			String hoardingArea = "";
			String sql = "";
			List<Map<String, Object>> productList = null;
			final StringBuffer xmlData = new StringBuffer();
			String bufferLocationdata="";
			//Map<String, String> map = null;
			JdbcTemplate template = null;

			try {

				template = new JdbcTemplate(DBConnection.getDbConnection());
				sql = "select HOARDING_ID,HOARDING_AREA from MRKT_HOARDING_DTLS where CHILD_PRODUCT_ID='"+childProductId+"'";
				productList = template.queryForList(sql, new Object[] {});
				if (null != productList && productList.size() > 0) {
					xmlData.append("<xml>");
					
					for (Map siteDets : productList) {
						xmlData.append("<Area>");
						
						hoardingId = siteDets.get("HOARDING_ID") == null ? "" : siteDets.get("HOARDING_ID").toString();
						hoardingArea = siteDets.get("HOARDING_AREA") == null ? "" : siteDets.get("HOARDING_AREA").toString();
						xmlData.append("<AreaName>" +hoardingArea+"</AreaName>");
						xmlData.append("<AreaId>" + hoardingId+"</AreaId>");
						xmlData.append("</Area>");
						
					}
					
					xmlData.append("</xml>");
				}
				int PRETTY_PRINT_INDENT_FACTOR = 4;
				JSONObject xmlJSONObj;
				xmlJSONObj = XML.toJSONObject(xmlData.toString());
				bufferLocationdata = xmlJSONObj.toString(PRETTY_PRINT_INDENT_FACTOR);
				System.out.println(bufferLocationdata);
				
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			return bufferLocationdata;
		}




	public static Boolean returnTrueIfAnyValueInMapIsNotBlank(Map<String, String> map) {
		
		Collection<String> list =  map.values();
		for(String value:list){
			if(StringUtils.isNotBlank(value)){
				return true;
			}
		}
		return false;
		
	}
	
	public String[] cleanArray(final String[] v) {
		  int r, w;
		  final int n = r = w = v.length;
		  while (r > 0) {
		    final String s = v[--r];
		    if (!s.trim().equals("")&&!s.equals("null")&&s!=null) {
		      v[--w] = s.trim();
		    }
		  }
		  return Arrays.copyOfRange(v, w, n);
		}
	  
	public  List<SiteDetails> getAllSiteDetails() {
		JdbcTemplate template = null;
		List<SiteDetails> siteDetails = new ArrayList<SiteDetails>();
		String siteId = "";
		String siteName = "";
		String address = "";
		SiteDetails objSiteDetails;
		try {

			template = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select SITE_ID, SITE_NAME, ADDRESS  from  SITE where STATUS = 'ACTIVE'";
			
			List<Map<String,Object>> list = template.queryForList(sql, new Object[] {});
			
			for(Map<String,Object> map : list){
				siteId = map.get("SITE_ID") == null ? "" : map.get("SITE_ID").toString();
				siteName = map.get("SITE_NAME") == null ? "" : map.get("SITE_NAME").toString();
				address = map.get("ADDRESS") == null ? "" : map.get("ADDRESS").toString();
				objSiteDetails = new SiteDetails();
				objSiteDetails.setSiteId(siteId);
				objSiteDetails.setSiteName(siteName);
				objSiteDetails.setAddress(address);
				siteDetails.add(objSiteDetails);
			}

		} catch (Exception e) {
			e.printStackTrace();

		}

		return siteDetails;
	}
}
