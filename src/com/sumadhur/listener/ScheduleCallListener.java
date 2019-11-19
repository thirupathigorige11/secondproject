package com.sumadhur.listener;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.Query;
import javax.management.ReflectionException;
import javax.naming.NamingException;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sumadhura.service.EmailFunction;
import com.sumadhura.transdao.SchedulerDao;
import com.sumadhura.util.UIProperties;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Madhu Tokala
 * @Company Veeranki's Pvt Ltd.
 * @Date Aug 21, 2017 10:30:39 PM
 */

public class ScheduleCallListener extends UIProperties implements ServletContextListener {

	private JdbcTemplate template;
	ServletContext context;
	@SuppressWarnings("deprecation")
	public void contextInitialized(ServletContextEvent contextEvent) {

		context = contextEvent.getServletContext();
		System.out.println("contex" +context);
		final int hour=Integer.valueOf(validateParams.getProperty("PMR_TXN_SCHDTIME"));
		final int minutes=Integer.valueOf(validateParams.getProperty("PMR_TXN_SCHDMINUTS"));


		final int StoreReportHour =Integer.valueOf(validateParams.getProperty("STORE_REPORT_SCHDTIME"));
		final int StoreReportMinute =Integer.valueOf(validateParams.getProperty("STORE_REPORT__SCHDMINUTS"));
		final String StoreReporEmail = validateParams.getProperty("STORE_REPORT__EMAILS_TO");
		final String StoreReporEmailCC = validateParams.getProperty("STORE_REPORT__EMAILS_CC");
		
		
		final int marketingHour=Integer.valueOf(validateParams.getProperty("MARKETING_MORNING_AREATIME"));
		final int marketingMinutes=Integer.valueOf(validateParams.getProperty("MARKETING_MORNING_AREAMINUTS"));
		
		final int marketingEveningHour=Integer.valueOf(validateParams.getProperty("MARKETING_EVENING_AREATIME"));
		final int marketingEveningMinutes=Integer.valueOf(validateParams.getProperty("MARKETING_EVENING_AREAMINUTS"));
		
		final String MarketingEmail = validateParams.getProperty("MARKETING_REPORT__EMAILS_TO");
		final String MarketingEmailCC = validateParams.getProperty("MARKETING_REPORT__EMAILS_CC");
		
		final String uat_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_UAT").toString();
		final String cug_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_CUG").toString();
		final String live_image_path=validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE") == null ? "" : validateParams.getProperty("PURCHASE_IMAGE_PATH_LIVE").toString();

		logger.info("time     :"+hour);
		logger.info("minutes  :"+minutes);
		logger.info("********* Sumadhura Recive and Issue Transactions ServletContextListener Start *********");
		int delay = 1000;
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {

				Date now = new Date();
				SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy");
				logger.info("Formatted date :   " + dateFormatter.format(now) + "current time : " + now.getHours() + " minute : " + now.getMinutes());

				String addMinZeroDigit = String.valueOf(now.getMinutes()).length() == 1 ? "0" : "";
				String addHourZeroDigit = String.valueOf(now.getHours()).length() == 1 ? "0" : "";
	                      	/******************************** marketing report start**************************************************/
				ApplicationContext context = new FileSystemXmlApplicationContext
				//("E:/Sumadhura_CUG/Sumadhura_CUG/WebContent/WEB-INF/applicationContext.xml");
				//("C:/Sumadhura_CUG/Sumadhura_CUG/Sumadhura_CUG/WebContent/WEB-INF/applicationContext.xml");
				("C:/Rakesh/Sumadhura/WebContent/WEB-INF/applicationContext.xml");
				SchedulerDao obj1 = (SchedulerDao) context.getBean("schedularDao");
				boolean marketingStatus=false;
				String data="";
				String sitesList="";
				if ((now.getHours() == marketingHour && now.getMinutes() == marketingMinutes) || (now.getHours() == marketingEveningHour && now.getMinutes() == marketingEveningMinutes)) {
					try {
						data=obj1.marketingAvailableArea();
						marketingStatus=Boolean.parseBoolean(data.split("@@@")[0]);
						if(marketingStatus){
						sitesList=data.split("@@@")[1];}
						} catch (NamingException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
					if(marketingStatus){
						
					
					//to get the port number
					
					List<String> endpoints = null ;
					try {
						endpoints = getEndPoints();
					}catch(MalformedObjectNameException  e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (AttributeNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InstanceNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MBeanException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ReflectionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					System.out.println("end point...................."+ endpoints );

					String strPort = endpoints.get(0);


					if(strPort.equals("80")){

						logger.info("Notification Time:" + addHourZeroDigit + now.getHours() + ":" + addMinZeroDigit + now.getMinutes());

						try {
							String date = new SimpleDateFormat("MMM-yyyy").format(new Date());
							//String strReportUrl = "http://129.154.74.18:8078/Sumadhura_UAT/StoreDailyReport.jsp?requestDate="+date+"";

							String emailBodyMsgTxt = "<form method='POST'>";
							emailBodyMsgTxt += "<p style=\"color:#15c;font-size:14px;\">Hello All,</p>";
							emailBodyMsgTxt+= "<p style=\"color:#15c;font-size:14px;\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Available Area of following projects were not updated in IMS for Month "+date+", in Marketing Module. Please update Available Area of all Active sites, so that can select Expenditure type (Cost center) as Location wise or Branding wise while inwarding a Marketing Tax Invoice.</p><br>";
							emailBodyMsgTxt += "<p style=\"color:#15c;font-size:14px;\">For Projects "+sitesList+" should update Available area.</p>";
							emailBodyMsgTxt+= "<div style=\"text-align:center;\"> ";
							//"<input type=\"submit\" style=\"color: #fff;background-color: #f0ad4e;border-color: #eea236;padding:6px 12px;background-image: none;border: 1px solid transparent;border-radius: 4px;\" value=\"View the report\" /></div>";
							//emailBodyMsgTxt+= "<a href='"+strReportUrl+"' style=\"color: #fff;background-color: #f0ad4e;border-color: #eea236;padding:8px 12px;background-image: none;border: 1px solid transparent;border-radius: 4px;\">View Report</a>";
							emailBodyMsgTxt+= "</div><br>" ;
							//emailBodyMsgTxt+= "<p></p> <p></p> <p style=\"color:#15c;font-size:14px;\">Thanks & Regards</p> <p> - </p></form>";
							emailBodyMsgTxt+="<p style=\"font-family: Calibri;margin-top: 20px;font-size: 15px;font-weight:bold;\">";
							emailBodyMsgTxt+="<span style=\"border-bottom:3px solid #ea2127;float:left;\">SUMADHURA&nbsp;</span>";
							emailBodyMsgTxt+="<span style=\"border-bottom:3px solid #f38020;float:left;\">INFRACON&nbsp;</span>";
							emailBodyMsgTxt+="<span style=\"border-bottom:3px solid #00a44f;float:left;\">PRIVATE LIMITED</span>";
							emailBodyMsgTxt+="</p><br><br>";
							emailBodyMsgTxt+="<p style=\"margin-top: 5px;margin-bottom: 5px;\">43, CKB Plaza, 2nd Floor, Varthur Main Road, Marathahalli, Bangalore, Karnataka 560037</p>";
							emailBodyMsgTxt+="<p style=\"margin-top: 5px;margin-bottom: 5px;\"><span style=\"font-weight: bold;font-family: Calibri;\">Tel:</span><span>080-42126699,&nbsp;</span></p>";
							emailBodyMsgTxt+="<p style=\"margin-top: 5px;margin-bottom: 5px;\"><span style=\"font-weight: bold;font-family: Calibri;\">Email:</span><span> info@sumadhuragroup.com,&nbsp;</span></p>";
							emailBodyMsgTxt+="<p style=\"margin-top: 5px;margin-bottom: 5px;\"><span style=\"font-weight: bold;font-family: Calibri;\">URL:</span><span> sumadhuragroup.com,&nbsp;</span></p>";
							emailBodyMsgTxt+="<p style=\"margin-top: 5px;margin-bottom: 5px;\"><span style=\"font-weight: bold;font-family: Calibri;\">CIN:</span><span> U45200KA2012PTC062071&nbsp;</span></p>";
							
							emailBodyMsgTxt+="<img class=\"img-responsive img-table-getinvoice\" alt=\"img\"   style=\"width: 151px;height: 139px;\" src=\""+live_image_path+"\">";
													
							String emailSubjectTxt = "Regarding Available Area updation in IMS for Month "+date+", in Marketing Module.";//"Sumadhura's "+date+" marketing available Area.";
							
						
							
						String emailFromAddress = "sumadhura5949@gmail.com";
						String[] sendTo =  MarketingEmail.split(",") ;
						String[] sendCC =  MarketingEmailCC.split(",") ;

							EmailFunction objEmailFunction = new EmailFunction();
							objEmailFunction.sendEmail(emailBodyMsgTxt,emailSubjectTxt,emailFromAddress,sendTo, sendCC,"");

						} catch (Exception ex) {

							ex.printStackTrace();
							logger.info("Exception while running Scheduler class for store report :" + ex.getMessage());
						}
					}
					}// second if end
				}
				
				/**********************************marketing report end*******************************************************/
				if (now.getHours() == hour && now.getMinutes() == minutes) {

					logger.info("Notification Time:" + addHourZeroDigit + now.getHours() + ":" + addMinZeroDigit + now.getMinutes());

					try {




						//ApplicationContext context = new FileSystemXmlApplicationContext
						//("C:/Rakesh/Sumadhura/WebContent/WEB-INF/applicationContext.xml");
						//("F:/Sumadhara/Sumadhura/WebContent/WEB-INF/applicationContext.xml");

						SchedulerDao obj = (SchedulerDao) context.getBean("schedularDao");
						obj.statScheduler();


						//new SchedulerDao().statScheduler();
						logger.info("Scheduler Running Successfully....");
					} catch (Exception ex) {
						try {

							String emailBodyMsgTxt = "Closing balance Schedular is running failed, Please contact with support team on urgent basis";
							String emailSubjectTxt = "Closing balance schedular running failed";
							String emailFromAddress = "sumadhura5949@gmail.com";
							String[] sendTo = { "pavan45662@gmail.com", "sumadhura5949@gmail.com","vericherlav@gmail.com" };

							EmailFunction objEmailFunction = new EmailFunction();
							objEmailFunction.sendEmail(emailBodyMsgTxt,emailSubjectTxt,emailFromAddress,sendTo, new String[0],"");
						} catch (Exception e) {
							e.printStackTrace();
						}
						ex.printStackTrace();
						logger.info("Exception while running Scheduler class :" + ex.getMessage());
					}
				}


				if (now.getHours() == StoreReportHour && now.getMinutes() == StoreReportMinute) {



					//to get the port number
					List<String> endpoints = null ;
					try {
						endpoints = getEndPoints();
					} catch (MalformedObjectNameException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (AttributeNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (InstanceNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (UnknownHostException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MBeanException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ReflectionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					System.out.println("end point...................."+ endpoints );

					String strPort = endpoints.get(0);


					if(strPort.equals("80")){

						logger.info("Notification Time:" + addHourZeroDigit + now.getHours() + ":" + addMinZeroDigit + now.getMinutes());

						try {
							String date = new SimpleDateFormat("dd-MM-yy").format(new Date());
							String strReportUrl = "http://129.154.74.18/Sumadhura/StoreDailyReport.jsp?requestDate="+date+"";



							String emailBodyMsgTxt = "<form action='"+strReportUrl+"' method='POST'>";
							emailBodyMsgTxt += "<p style=\"color:#15c;font-size:14px;\">Hi All,</p>";
							emailBodyMsgTxt+= "<p style=\"color:#15c;font-size:14px;\">&nbsp;&nbsp;&nbsp;IMS status report on inwards and issues of Sumadhura stores, dated "+date+". For more details, Please click the button below.</p><br>";
							emailBodyMsgTxt+= "<div style=\"text-align:center;\">" ;
							//"<input type=\"submit\" style=\"color: #fff;background-color: #f0ad4e;border-color: #eea236;padding:6px 12px;background-image: none;border: 1px solid transparent;border-radius: 4px;\" value=\"View the report\" /></div>";
							emailBodyMsgTxt+= "<a href='"+strReportUrl+"' style=\"color: #fff;background-color: #f0ad4e;border-color: #eea236;padding:8px 12px;background-image: none;border: 1px solid transparent;border-radius: 4px;\">View Report</a>";
							emailBodyMsgTxt+= "</div><br>" ;
							//emailBodyMsgTxt+= "<p></p> <p></p> <p style=\"color:#15c;font-size:14px;\">Thanks & Regards</p> <p> - </p></form>";
							emailBodyMsgTxt+="<p style=\"font-family: Calibri;margin-top: 20px;font-size: 15px;font-weight:bold;\">";
							emailBodyMsgTxt+="<span style=\"border-bottom:3px solid #ea2127;float:left;\">SUMADHURA&nbsp;</span>";
							emailBodyMsgTxt+="<span style=\"border-bottom:3px solid #f38020;float:left;\">INFRACON&nbsp;</span>";
							emailBodyMsgTxt+="<span style=\"border-bottom:3px solid #00a44f;float:left;\">PRIVATE LIMITED</span>";
							emailBodyMsgTxt+="</p><br><br>";
							emailBodyMsgTxt+="<p style=\"margin-top: 5px;margin-bottom: 5px;\">43, CKB Plaza, 2nd Floor, Varthur Main Road, Marathahalli, Bangalore, Karnataka 560037</p>";
							emailBodyMsgTxt+="<p style=\"margin-top: 5px;margin-bottom: 5px;\"><span style=\"font-weight: bold;font-family: Calibri;\">Tel:</span><span>080-42126699,&nbsp;</span></p>";
							emailBodyMsgTxt+="<p style=\"margin-top: 5px;margin-bottom: 5px;\"><span style=\"font-weight: bold;font-family: Calibri;\">Email:</span><span> info@sumadhuragroup.com,&nbsp;</span></p>";
							emailBodyMsgTxt+="<p style=\"margin-top: 5px;margin-bottom: 5px;\"><span style=\"font-weight: bold;font-family: Calibri;\">URL:</span><span> sumadhuragroup.com,&nbsp;</span></p>";
							emailBodyMsgTxt+="<p style=\"margin-top: 5px;margin-bottom: 5px;\"><span style=\"font-weight: bold;font-family: Calibri;\">CIN:</span><span> U45200KA2012PTC062071&nbsp;</span></p>";
													
							String emailSubjectTxt = "Sumadhura's "+date+" stores Inwards and Issues report.";
						
							
						String emailFromAddress = "sumadhura5949@gmail.com";
						String[] sendTo =  StoreReporEmail.split(",") ;
						String[] sendCC =  StoreReporEmailCC.split(",") ;

							EmailFunction objEmailFunction = new EmailFunction();
							objEmailFunction.sendEmail(emailBodyMsgTxt,emailSubjectTxt,emailFromAddress,sendTo, sendCC,"");

						} catch (Exception ex) {

							ex.printStackTrace();
							logger.info("Exception while running Scheduler class for store report :" + ex.getMessage());
						}
					}
				}



			} // End of Run
		}, delay, 60000);
		context.setAttribute("timer", timer);

		logger.info("********* PMR Transactions ServletContextListener End *********");
	}

	public void contextDestroyed(ServletContextEvent contextEvent) {

		Timer timer = (Timer) context.getAttribute("timer");

		// cancel all pending tasks in the timers queue
		if (timer != null)
			timer.cancel();

		// remove the timer from the servlet context
		context.removeAttribute("timer");
		logger.info("********* Sumadhura Recive and Issue Transactions ServletContextListener End *********");
	}


	private List<String> getEndPoints() throws MalformedObjectNameException, UnknownHostException, AttributeNotFoundException, InstanceNotFoundException, MBeanException, ReflectionException {

		MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
		Set<ObjectName> objs = mbs.queryNames( new ObjectName( "*:type=Connector,*" ),
				Query.match( Query.attr( "protocol" ), Query.value( "HTTP/1.1" ) ) );
		String hostname = InetAddress.getLocalHost().getHostName();



		InetAddress[] addresses = InetAddress.getAllByName( hostname );
		ArrayList<String> endPoints = new ArrayList<String>();
		for ( ObjectName obj : objs ) {
			String scheme = mbs.getAttribute( obj, "scheme" ).toString();
			String port = obj.getKeyProperty( "port" );

			System.out.println("port ..............."+ port );

			for ( InetAddress addr : addresses ) {
				// String host = addr.getHostAddress();
				String ep =  port;
				endPoints.add( ep );
			}
		}
		return endPoints;



	}

}
