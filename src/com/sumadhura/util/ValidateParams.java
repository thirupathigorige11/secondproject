/**
 * 
 */
package com.sumadhura.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.SocketException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

import javax.naming.NameNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.lang.StringUtils;






//import com.snapwork.util.AppSensorProperties;

/**
 * @author  Sudhakar Tangellapalli
 * @Company Snapwork Technologies
 */
public class ValidateParams {
	private static final AppLogger log=AppLogger.loggerObj();
	private StringBuilder sbl = null ;
	private static HashMap<String,Object> hashMap;
	public static ValidateParams validateParams; 
	Properties prop;
	 /**
	  * This is for for Preventing the Multiple Instance Creation .
	  * */
	 private ValidateParams(){
	        try {
	        	//System.out.println("util package...validate params...FileName...1");
	            // Read properties file.
	            prop = new Properties();
	            try {
	            	
	            	File f = new File("D://validationproperties.properties");
	            	            	
	            	if(!f.exists()){
	            		
	            		prop.load(this.getClass().getClassLoader().getResourceAsStream("validationproperties.properties"));
	            	}else{
	            		
	            		prop.load(new FileInputStream(f));

	            	}
	            
	            } catch (IOException e) {
	            	e.printStackTrace();
	                //System.out.println("appsensor: Error in reading properties file: " +  e.toString());
	            }
	            
	            populateMap();
	            
	        } catch (Exception se) {
	        	se.printStackTrace();
	            //System.out.println("appsensor: Failed in ReadConfigFile constructor : " + se.toString());
	        }
	    
	 }
	 private void populateMap(){
		 try{
			 String key = "";
	 		 String Value = "";
	         hashMap = new HashMap<String,Object>();
			 Set keySet =  prop.keySet();
			 for(Object K : keySet){
					key = (String)K;
					Value = prop.getProperty(key+"");
					hashMap.put(key, Value);
			 }
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }
	}
	 public String getProperty(String keyName) {
	         String key = "";
			 if(hashMap != null){
				 key = hashMap.get(keyName) == null ? "" : hashMap.get(keyName).toString();
			 }else{
				 populateMap();
			 }
			 if("".equals(key) || key == null){
				 key = prop.getProperty(keyName);
			 }
			 return key;
	    }

	    /**
	     * Returns the enumeration of all keys.
	     * in the configuration file.
	     *
	     * @return  Enumeration
	     */
	    public Enumeration getPropertyNames() {
	        return prop.propertyNames();
	    }
	    /**
	     * This will return the list of data Mapped to the Particular Key as  Array of String
	     * */
	    public ArrayList<String> getAppsensorKeyData(String key) {
	    	ArrayList<String> values=new ArrayList<String>();
	        String result = getProperty(key);
	         for (String string : result.split(",")) {
				values.add(string);
			}
	        return values;

	    }
	 
	 
	 /**
	  * This will create the only one indtance of the Class object 
	  * */
	 public static ValidateParams validateParamsObj(){
		 /**
		  * If the <strong>DBUtilities</strong> class object is null this condition will create object and return 
		  * */
		 if(validateParams == null){
			 validateParams = new ValidateParams();
		 }
		 return validateParams;
	 }
	 

		 /**
		  * This will Validate the Request Parameters and return the Response . If any error the Exception will 
		  * throw to the calling place.
		  * */
		 public String getSafeParamValues(HttpServletRequest request,String paramName)throws UnsupportedEncodingException,Exception{
	         String parmValue = "";
	         try {
	       	 String whiteListCharacters = validateParams.getProperty("regexforparamvalidation");
	       	 
	        	 	if(paramName != null && !paramName.equals("MSG")){
	        	 	if(StringUtils.isNotEmpty(request.getParameter(paramName))){
	        	 
	                
	                	 parmValue = request.getParameter(paramName);
	                	 parmValue = parmValue.trim();
	                	 // Taking the List from the Properties file for validating against ParamValue
	                	 /*String[] whiteListArray=whiteListCharacters.split(",");
	                	 if(whiteListArray!=null && whiteListArray.length>0){
	                		 for (String whiteListData : whiteListArray) {
	                			 if(parmValue.indexOf(whiteListData)!=-1){
	                				 System.out.println("There is an Un Supported Character in the ParamValue"+parmValue);
	                				 throw new UnsupportedEncodingException(validateParams.getProperty("exceptio.UnsupportedEncodingException"));
	                			 }
							}
	                	 }*/
	    	         }
	        	 	}else{
	    	        	 parmValue = "";
	    	             //return parmValue;
	    	         }
	         } /*catch (UnsupportedEncodingException e) {
	        	 System.out.println("UnsupportedEncodingException :::>"+e);
	                 throw new UnsupportedEncodingException(validateParams.getProperty("exceptio.UnsupportedEncodingException"));
	         }*/catch(IllegalArgumentException e){
		    	  //System.out.println("her Illegal Argumente");
		    	  throw new UnsupportedEncodingException(validateParams.getProperty("exceptio.UnsupportedEncodingException"));
		      }catch(Exception exception){
	        	 System.out.println("Exception in the getSafeParamValues :::>"+exception+"paramName::"+paramName);
	        	 	throw new Exception("exception.exception");
	         }
	         
	         return parmValue;
		 }
		 
		 public static  int getSafeParamIntValues(HttpServletRequest request,String paramName)throws UnsupportedEncodingException,Exception{
	         String parmValue = "";
	         int result = 0;
	         try {
	        	 parmValue = request.getParameter(paramName) == null?"0":request.getParameter(paramName).trim();
	        	 if(StringUtils.isNotEmpty(parmValue)){
	        		 result = DatatTypeUtil.getIntValueFromReq(parmValue);
	        	 }
	         }catch(IllegalArgumentException e){
		    	  //System.out.println("her Illegal Argumente");
		    	  throw new UnsupportedEncodingException(validateParams.getProperty("exceptio.UnsupportedEncodingException"));
		      }catch(Exception exception){
	        	 System.out.println("Exception in the getSafeParamValues :::>"+exception+"paramName::"+paramName);
	        	 	throw new Exception("exception.exception");
	         }
	         
	         return result;
		 }
		 
		 public static  float getSafeParamFloatValues(HttpServletRequest request,String paramName)throws UnsupportedEncodingException,Exception{
	         String parmValue = "";
	         float result = 0;
	         try {
	        	 parmValue = request.getParameter(paramName)== null?"0.0":request.getParameter(paramName).trim();
	        	 if(StringUtils.isNotEmpty(parmValue)){
	        		 result= DatatTypeUtil.getFloatValueFromReq(parmValue);
	        	 }
	         }catch(IllegalArgumentException e){
		    	  //System.out.println("her Illegal Argumente");
		    	  throw new UnsupportedEncodingException(validateParams.getProperty("exceptio.UnsupportedEncodingException"));
		      }catch(Exception exception){
	        	 System.out.println("Exception in the getSafeParamValues :::>"+exception+"paramName::"+paramName);
	        	 	throw new Exception("exception.exception");
	         }
	         
	         return result;
		 }
		 
		 public static  double getSafeParamDoubleValues(HttpServletRequest request,String paramName)throws UnsupportedEncodingException,Exception{
	         String parmValue = "";
	         double result = 0.0;
	         try {
	        	 parmValue = request.getParameter(paramName)== null?"0.0":request.getParameter(paramName).trim().replaceAll(",", "");
	        	 if(StringUtils.isNotEmpty(parmValue)){
	        		 result=  DatatTypeUtil.getDoubleValueFromReq(parmValue);
	        	 }
	         }catch(IllegalArgumentException e){
		    	  //System.out.println("her Illegal Argumente");
		    	  throw new UnsupportedEncodingException(validateParams.getProperty("exceptio.UnsupportedEncodingException"));
		      }catch(Exception exception){
	        	 System.out.println("Exception in the getSafeParamValues :::>"+exception+"paramName::"+paramName);
	        	 	throw new Exception("exception.exception");
	         }
	         
	         return result;
		 }
		 
		 public static  long getSafeParamLongValues(HttpServletRequest request,String paramName)throws UnsupportedEncodingException,Exception{
	         String parmValue = "";
	         long result = 0;
	         try {
	        	 parmValue = request.getParameter(paramName)== null?"0.0":request.getParameter(paramName).trim();
	        	 if(StringUtils.isNotEmpty(parmValue)){
	         	 result = DatatTypeUtil.getLongValueFromReq(parmValue);
	        	 }
	         }catch(IllegalArgumentException e){
		    	  //System.out.println("her Illegal Argumente");
		    	  throw new UnsupportedEncodingException(validateParams.getProperty("exceptio.UnsupportedEncodingException"));
		      }catch(Exception exception){
	        	 System.out.println("Exception in the getSafeParamValues :::>"+exception+"paramName::"+paramName);
	        	 	throw new Exception("exception.exception");
	         }
	         
	         return result;
		 }
		 
		 public String getSafeHeaderParamValues(HttpServletRequest request,String paramName){
			 String parmValue = "";
			 try {				 
				 if(StringUtils.isNotEmpty(request.getHeader(paramName))){
					 parmValue = request.getHeader(paramName);
				 }else{
					 parmValue="";
				 }
				 
			 } catch (Exception e) {
				 return "";
			 }			 
			 return parmValue;
		 }
		 
		 
		 /**
		  * This Method will Validate the User Agent is Exist or not in the List<String>. if parameter values 
		  * is null then this will throw {@link NullPointerException} to the calling place . If the
		  * User Agent is not there  in the List<String> then this will return the "false"
		  * @param userAgent
		  * */
		 public boolean validateUserAgentExistOrNot(String userAgent) throws NullPointerException{
			boolean isExist=false;
			try {
				 ArrayList<String> userAgentList=validateParams.getAppsensorKeyData("useragents");
				 isExist=true;
//				 if(userAgentList.contains(userAgent)){
//					 isExist=true;
//				 }else{
//					 throw new UserAagentException("exception.UserAagentException");
//				 }
			} catch (NullPointerException ne) {
				throw new NullPointerException("exception.NullPointerException");
			}
//				catch (UserAagentException e) {
//				throw new UserAagentException("exception.UserAagentException");
//			}
			 return isExist;
			 
		 }
		 
		 /**
		  * This method validate request parameter value.
		  * @param request
		  * @param paramName
		  * @return
		  */
		 public  String getSafeValues(HttpServletRequest request,String paramName){
	         String parmValue = "";
	         try {
	        	 	if(StringUtils.isNotEmpty(request.getParameter(paramName))){
	        	 		parmValue=URLDecoder.decode(request.getParameter(paramName),"UTF-8");
	        	 	}else{
	        	 		parmValue="";
	        	 	}	                 
	         	} catch(Exception exception){
	        	 System.out.println("Exception in the getSafeParamValues :::>"+exception+"paramName::"+paramName);
	        	 	parmValue="";
	        	 	return parmValue;
	         }
	         
	         return parmValue;
		 }
		 
		 /*
		  * This method will form the xml exception. 
		  * @param request
		  * @param errorCode
		  * @return
		  * @throws Exception
		  */
		 @SuppressWarnings("unchecked")
		
		
		 public boolean emailValidation(String params){
			 boolean result = false;
			 String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
			 result = params.matches(EMAIL_REGEX);
			 return result;
		 }
		 public static void main(String[] args) {
			 String regex = "[0-9]+";
			 String data = "23343s453";
			 System.out.println(data.matches(regex));
		}
		 
}
