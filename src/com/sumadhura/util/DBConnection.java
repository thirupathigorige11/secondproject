package com.sumadhura.util;

import java.io.BufferedInputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import org.springframework.jndi.JndiTemplate;

public class DBConnection {
	public static  final ValidateParams validateParams = ValidateParams.validateParamsObj();
	public static  DataSource getDbConnection() throws NamingException {
	    JndiTemplate jndiTemplate = new JndiTemplate();
	    //System.out.println("DB Connectio......17");
	    DataSource dataSource
	       /* = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/IMobile");*/
	    = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/Sumadhura");
	    //System.out.println("DB Connectio......21");
	    return dataSource;
	}
	public static Connection getJndiConnection() throws NamingException,SQLException,Exception{
	    Connection connection=null;
	    Context context;
	    
		try {
			 //System.out.println("DB Connectio......29");
				context = (Context)new InitialContext().lookup(validateParams.getProperty("initialcontextfactory").trim());
				 //System.out.println("DB Connection context......31");
				DataSource source=(DataSource)context.lookup(validateParams.getProperty("jndiname").trim());
				connection=source.getConnection();
				//System.out.println("Connection Object is created::: 34");
				
			//connection=getConnectionFromIP();
			
			if(connection==null){
				//System.out.println("Exception in DBUtilities Connection oBj is null::>");
//				throw new SQLException(validateParams.getProperty("exception.sqlexception"));
				//connection=getConnectionFromIP();
			}
		} /*catch (NamingException e) {
			logger.error("Exception in DBUtilities ::>"+e);
//				throw new NamingException(validateParams.getProperty("error.NamingException"));
				connection=getConnectionFromIP();
		}catch(SQLException se){
			logger.error("Exception in DBUtilities ::>"+se);
//				throw new SQLException(validateParams.getProperty("exception.sqlexception"));
				connection=getConnectionFromIP();
		}*/catch (Exception e) {
			//System.out.println("Exception in DBUtilities ::>"+e);
//			throw new Exception(validateParams.getProperty("exception.exception"));
			//connection=getConnectionFromIP();
		}
	 return connection;
 }
public static Connection getSDBConnection()
{
	Connection connection = null;
	   try
	   {
	   	JndiTemplate jndiTemplate = new JndiTemplate();
	   //System.out.println("Sagam DB Connection start");
	   DataSource dataSource = (DataSource) jndiTemplate.lookup("java:comp/env/jdbc/Sumadhura");
	   connection = dataSource.getConnection();
	   //System.out.println("Sagam DB Connection end "+connection);
	   }
	   catch (Exception e)
	   {
	     //System.out.println("Sangam Exception::"+e);
	     e.printStackTrace();
	   }
	   return connection;
	} 
	  public String getDsnName(String key)
	  {
	    String val = "";
	    try
	    {
	      Properties prop = new Properties();
	      prop.load(new BufferedInputStream(getClass().getResourceAsStream("log4j.properties")));
	      val = prop.getProperty(key);
	    }
	    catch (Exception e)
	    {
	      e.printStackTrace();
	      //System.out.println("Exception occured:: " + e.getMessage());
	    }
	    return val;
	  }
}
