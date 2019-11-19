/**
 * 
 */
package com.sumadhura.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;

/**
 * @author  Sudhakar Tangellapalli
 * @Company Snapwork Technologies Pvt Ltd.
 * @Date    Jan 10, 2014  5:23:18 PM
 */
public class ReadProperties {

	private static ReadProperties readProperties;
	public static final String  REQUEST_MAPPING="requestidmapping.properties";
	private Properties properties;
	private static HashMap<String,Object> hashMap;
	/**
	 * 
	 */
	private ReadProperties() {
		 try {
	            // Read properties file.
	            properties = new Properties();
	            try {
	            	File f = new File("/app/iMobile/properties/requestidmapping.properties");
	            	if(!f.exists()){
	            		properties.load(this.getClass().getClassLoader().getResourceAsStream("requestidmapping.properties"));
	            	}else{
	            		properties.load(new FileInputStream(f));
	            	}
	            } catch (IOException e) {
	                //System.out.println("loadPropertiesFileFromResource: Error in reading properties file: " +  e.toString());
	            }
	            populateMap();
	        } catch (Exception se) {
	            //System.out.println("loadPropertiesFileFromResource: Failed in ReadConfigFile constructor : " + se.toString());
	        }

	}
	private void populateMap(){
		 try{
			 String key = "";
	 		 String Value = "";
	         hashMap = new HashMap<String,Object>();
			Set keySet =  properties.keySet();
			for(Object K : keySet){
					key = (String)K;
					Value = properties.getProperty(key+"");
					hashMap.put(key, Value);
			}
		 }catch(Exception ex){
			 ex.printStackTrace();
		 }
	}
	
	 /**
	  * @return {String} value of the Particular Key.
	  * Pass the File Name and Key Name this will read the Data from the Class Path.  
	  * @param keyName
	  * @param fileName 
	  * */
	 public String getKeyValue(Object keyName){
		 String key = "";
		 if(hashMap != null){
			 key = hashMap.get(keyName) == null ? "" : hashMap.get(keyName).toString();
		 }else{
			 populateMap();
		 }
		 if("".equals(key) || key == null){
			 key = properties.getProperty(keyName+"");
		 }
		 return key;
	 }

	/**
	  * This will create the only one instance of the Class object 
	  * */
	 public static ReadProperties readPropertiesObj(){
		 /**
		  * If the <strong>DBUtilities</strong> class object is null this condition will create object and return 
		  * */
		 if(readProperties==null){
			 readProperties=new ReadProperties();
		 }
		 return readProperties;
	 }
	 
	 
	
	
}
