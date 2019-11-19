/**
 * 
 */
package com.sumadhura.util;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.SimpleLayout;

/**
 * @author  Sudhakar Tangellapalli
 * @Company Snapwork Technologies
 */
public class AppLogger {

	public static volatile AppLogger appLogger;
	private static Properties prop=null;
	public static Logger logger = Logger.getLogger(AppLogger.class.getName()); 
	/**
	 * This is for for Preventing the Multiple Instance Creation .
	 * */
	private AppLogger(){
		try {
			prop = new Properties();
			try {
				prop.load(this.getClass().getClassLoader().getResourceAsStream("log4j.properties"));
				String logPath=prop.getProperty("log4j.appender.file.File");
				logger.addAppender(new FileAppender(new SimpleLayout(), logPath));
				//logger.info("Loggers is Created");
			} catch (IOException e) {
				logger.info("Log4J Initializing : Failed in IOException  : " + e);
			}
		} catch (Exception se) {
			logger.info("Log4J Initializing : Failed in  : " + se.toString());
		}

		PropertyConfigurator.configure(prop);
	}
	/**
	 * This will create the only one indtance of the Class object 
	 * */
	public static synchronized AppLogger loggerObj(){
		/**
		 * If the <strong>DBUtilities</strong> class object is null this condition will create object and return 
		 * */
		if(appLogger == null){
			appLogger = new AppLogger();
		}
		return appLogger;
	}

	public void info(String msg){
		if(prop.getProperty("logger.info").equalsIgnoreCase("no")){
			logger.info(msg);
		}
	}
	public void warn(String msg){
		if(prop.getProperty("logger.warn").equalsIgnoreCase("no")){
			logger.warn(msg);
		}
	}
	public void debug(String msg){
		if(prop.getProperty("logger.debug").equalsIgnoreCase("no")){
			logger.debug(msg);
		}
	}

	public void error(String msg){
		if(prop.getProperty("logger.error").equalsIgnoreCase("no")){
			logger.error(msg);
		}
	}

	public void fatal(String msg){
		if(prop.getProperty("logger.fatal").equalsIgnoreCase("no")){
			logger.fatal(msg);
		}
	}

	public void info(String msg,Exception exception){
		if(prop.getProperty("logger.info").equalsIgnoreCase("no")){
			logger.info(msg,exception);
		}
	}
	public void warn(String msg,Exception exception){
		if(prop.getProperty("logger.warn").equalsIgnoreCase("no")){
			logger.warn(msg,exception);
		}
	}
	public void debug(String msg,Exception exception){
		if(prop.getProperty("logger.debug").equalsIgnoreCase("no")){
			logger.debug(msg,exception);
		}
	}

	public void error(String msg,Error errors){
		if(prop.getProperty("logger.error").equalsIgnoreCase("no")){
			logger.error(msg,errors);
		}
	}
	public void error(String msg,Exception exception){
		if(prop.getProperty("logger.error").equalsIgnoreCase("no")){
			logger.error(msg,exception);
		}
	}
	public void fatal(String msg,Exception exception){
		if(prop.getProperty("logger.fatal").equalsIgnoreCase("no")){
			logger.fatal(msg,exception);
		}
	}


}
