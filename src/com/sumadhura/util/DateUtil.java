/**
 * 
 */
package com.sumadhura.util;

import static java.lang.String.format;
import static java.util.logging.Level.FINER;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Logger;

import org.apache.commons.lang.StringUtils;



public class DateUtil {



	/**
	 * Facebook "long" date format (IETF RFC 3339). Example: {@code 2010-02-28T16:11:08+0000}
	 */
	public static final String FACEBOOK_LONG_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";

	/**
	 * Facebook "long" date format (IETF RFC 3339) without a timezone component. Example: {@code 2010-02-28T16:11:08}
	 */
	public static final String FACEBOOK_LONG_DATE_FORMAT_WITHOUT_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ss";

	/**
	 * Facebook short date format. Example: {@code 04/15/1984}
	 */
	public static final String FACEBOOK_SHORT_DATE_FORMAT = "MM/dd/yyyy";

	/**
	 * Facebook alternate short date format. Example: {@code 2012-09-15}
	 */
	public static final String FACEBOOK_ALTERNATE_SHORT_DATE_FORMAT = "yyyy-MM-dd";

	/**
	 * Facebook month-year only date format. Example: {@code Example: 2007-03}
	 */
	public static final String FACEBOOK_MONTH_YEAR_DATE_FORMAT = "yyyy-MM";

	/**
	 * Logger.
	 */
	private static final Logger logger = Logger.getLogger(DateUtil.class.getName());

	/**
	 * Returns a Java representation of a Facebook "long" {@code date} string, or the number of seconds since the epoch.
	 * <p>
	 * Supports dates with or without timezone information.
	 * 
	 * @param date
	 *          Facebook {@code date} string.
	 * @return Java date representation of the given Facebook "long" {@code date} string or {@code null} if {@code date}
	 *         is {@code null} or invalid.
	 */
	public static Date toDateFromLongFormat(String date) {
		if (date == null)
			return null;

		// Is this an all-digit date? Then assume it's the "seconds since epoch"
		// variant
		if (date.trim().matches("\\d+"))
			return new Date(Long.valueOf(date) * 1000L);

		Date parsedDate = toDateWithFormatString(date, FACEBOOK_LONG_DATE_FORMAT);

		// Fall back to variant without timezone if the initial parse fails
		if (parsedDate == null)
			parsedDate = toDateWithFormatString(date, FACEBOOK_LONG_DATE_FORMAT_WITHOUT_TIMEZONE);

		return parsedDate;
	}

	/**
	 * Returns a Java representation of a Facebook "short" {@code date} string.
	 * 
	 * @param date
	 *          Facebook {@code date} string.
	 * @return Java date representation of the given Facebook "short" {@code date} string or {@code null} if {@code date}
	 *         is {@code null} or invalid.
	 */
	public static Date toDateFromShortFormat(String date) {
		if (date == null)
			return null;

		Date parsedDate = toDateWithFormatString(date, FACEBOOK_SHORT_DATE_FORMAT);

		// Fall back to variant if initial parse fails
		if (parsedDate == null)
			parsedDate = toDateWithFormatString(date, FACEBOOK_ALTERNATE_SHORT_DATE_FORMAT);

		return parsedDate;
	}

	/**
	 * Returns a Java representation of a Facebook "month-year" {@code date} string.
	 * 
	 * @param date
	 *          Facebook {@code date} string.
	 * @return Java date representation of the given Facebook "month-year" {@code date} string or {@code null} if
	 *         {@code date} is {@code null} or invalid.
	 */
	public static Date toDateFromMonthYearFormat(String date) {
		if (date == null)
			return null;

		if ("0000-00".equals(date))
			return null;

		return toDateWithFormatString(date, FACEBOOK_MONTH_YEAR_DATE_FORMAT);
	}

	/**
	 * Returns a Java representation of a {@code date} string.
	 * 
	 * @param date
	 *          Date in string format.
	 * @return Java date representation of the given {@code date} string or {@code null} if {@code date} is {@code null}
	 *         or invalid.
	 */
	private static Date toDateWithFormatString(String date, String format) {
		if (date == null)
			return null;

		try {
			return new SimpleDateFormat(format).parse(date);
		} catch (ParseException e) {
			if (logger.isLoggable(FINER))
				logger.fine(format("Unable to parse date '%s' using format string '%s': %s", date, format, e));

			return null;
		}
	}


	public static String toTweetShortStr(Timestamp date) {
		if (date == null)
			return null;
		String parsedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
		return parsedDate;
	}






	public static  boolean compareDatesByCompareTo(DateFormat df, Date expiredDate, Date todayDate) {
		boolean flag = false;
		if (expiredDate.compareTo(todayDate) == 0) {
			//	        	//System.out.println(df.format(expiredDate) + " and " + df.format(todayDate) + " are equal to each other");
			flag= true;
		}//checking if expiredDate is less than todayDate
		if (expiredDate.compareTo(todayDate) < 0) {
			//	            //System.out.println(df.format(expiredDate) + " is less than " + df.format(todayDate));
			flag=false;
		}

		/*//how to check if expiredDate is greater than todayDate in java
		if (expiredDate.compareTo(todayDate) > 0) {
			//	        	//System.out.println(df.format(expiredDate) + " is greater than " + df.format(todayDate));
			flag=true;
		}*/
		return flag;
	}

	public static String generateNumer(String df){
		SimpleDateFormat sdf=new SimpleDateFormat(df);
		Date d=new Date();
		String txn_date_time=sdf.format(d).trim();
		return txn_date_time;
	}

	/*public static void main(String args[]){
		String generateUniqueNumber = generateNumer("yyyymmddhhmmssSSS");
		//System.out.println("Unique Number is:"+generateUniqueNumber);

		Calendar now = Calendar.getInstance();

		 int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
		//System.out.println(hour);

		 int minute = now.get(Calendar.MINUTE);
		 //System.out.println("Time:"+hour + ":" + minute);
		 Date date = parseDate(hour + ":" + minute);
		 Date dateCompareOne = parseDate("23:00");
		 Date dateCompareTwo = parseDate("23:12");

		 String expirytTimeLimit ="00:15";

		 if (dateCompareOne.before( date ) && dateCompareTwo.after(date)) {
		    //your logic
			 //System.out.println("Date1");
		 }else{
			 //System.out.println("Date2");
		 }
	 }*/

	public static Date parseDate(String date) {

		final String inputFormat = "HH:mm";
		SimpleDateFormat inputParser = new SimpleDateFormat(inputFormat, Locale.US);
		try {
			return inputParser.parse(date);
		} catch (java.text.ParseException e) {
			return new Date(0);
		}
	}


	public static String compareTime(String startTime, String endTime){
		String result = "";

		Calendar now = Calendar.getInstance();

		int hour = now.get(Calendar.HOUR_OF_DAY); // Get hour in 24 hour format
		int minute = now.get(Calendar.MINUTE);
		//System.out.println("Time:"+hour + ":" + minute);
		Date date = DateUtil.parseDate(hour + ":" + minute);

		Date dateCompareOne = DateUtil.parseDate(startTime);
		String time1 [] = startTime.split(":");
		String time2 [] = endTime.split(":");
		int hours = Integer.parseInt(time1[0]) + Integer.parseInt(time2[0]);
		int minutes = Integer.parseInt(time1[1]) + Integer.parseInt(time2[1]);
		Date dateCompareTwo = DateUtil.parseDate(hours+":"+minutes);

		if (dateCompareOne.before( date ) && dateCompareTwo.after(date)) {
			result = "Y";
		}else{
			result = "N";
		}

		return result;
	}
	public static String getSRDateFormat(String date){
		String formateddate = "";
		try{
			Date dt1 = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
			SimpleDateFormat sef = new SimpleDateFormat("dd-MM-yyyy");

			dt1 = sdf.parse(date);
			formateddate = sef.format(dt1);

			//System.out.println("ss " + date);


		}catch(Exception ex){
			formateddate = date;
		}
		return formateddate;
	}
	public static String getSRDateFormat2(String date){
		String formateddate = "";
		try{
			Date dt1 = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			SimpleDateFormat sef = new SimpleDateFormat("dd-MM-yyyy");

			dt1 = sdf.parse(date);
			formateddate = sef.format(dt1);

			//System.out.println("ss " + date);


		}catch(Exception ex){
			formateddate = date;
		}
		return formateddate;
	}
	public static String getMiniStatementDateFormat(String date){
		String formateddate = "";
		try{
			Date dt1 = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat sef = new SimpleDateFormat("dd/MM/yyyy");

			dt1 = sdf.parse(date);
			formateddate = sef.format(dt1);

			//System.out.println("ss " + date);


		}catch(Exception ex){
			formateddate = date;
		}
		return formateddate;
	}
	public static boolean ispreviousORequalsDate(String strdate1,String strdate2){
		boolean dateflag =false ;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date1 = sdf.parse(strdate1);
			Date date2 = sdf.parse(strdate2);

			if(date1.before(date2)||date1.equals(date2)){
				dateflag = true;
			}


		}catch(Exception ex){
			dateflag = false ;
		}
		return dateflag;
	}

	/*public static void main(String args[]){
		System.out.println(DateUtil.dateConversionForDCForm("19-09-17"));
	}*/


	public static String dateConversion(String strDate) {
		try {
			// create SimpleFOrmat object with source date format
			SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			// parse the string into Date object
			Date date = sdfSource.parse(strDate);

			// create SimpleDateFormat object with desired date format
			SimpleDateFormat sdfDestination = new SimpleDateFormat("dd/MM/yyyy");
			// parse the date into another format
			strDate = sdfDestination.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
	// this is common methode to getting date in dd-mm-yy
	public static String dateConversionForPO(String strDate) {
		try {
			// create SimpleFOrmat object with source date format
			SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			// parse the string into Date object
			Date date = sdfSource.parse(strDate);

			// create SimpleDateFormat object with desired date format
			SimpleDateFormat sdfDestination = new SimpleDateFormat("dd-MM-yyyy");
			// parse the date into another format
			strDate = sdfDestination.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
	public static Date convertToJavaDateFormat(String strDate) {
		Date date = null;
		if(strDate.equals("-")){return date;}
		try {
			SimpleDateFormat sdfSource1 = new SimpleDateFormat("dd-MMM-yy HH:mm:ss.SSS");
			date = sdfSource1.parse(strDate);
		} catch (Exception e1) {
			try {
				SimpleDateFormat sdfSource2 = new SimpleDateFormat("dd-MMM-yy");
				date = sdfSource2.parse(strDate);
			}
			catch (Exception e2) {
				try {
					SimpleDateFormat sdfSource3 = new SimpleDateFormat("dd/MM/yyyy");
					date = sdfSource3.parse(strDate);
				}
				catch (Exception e3) {
					try {
						SimpleDateFormat sdfSource4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						date = sdfSource4.parse(strDate);
					}
					catch (Exception e4) {
						try {
							SimpleDateFormat sdfSource5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							date = sdfSource5.parse(strDate);
						}
						catch (Exception e5) {
							try {
								SimpleDateFormat sdfSource6 = new SimpleDateFormat("dd-MM-yyyy");
								date = sdfSource6.parse(strDate);
							}
							catch (Exception e6) {
								e6.printStackTrace();
							}
						}
					}
				}
			}
		}
		return date;
	}
	public static java.sql.Date convertToSqlDateFormat(String strDate) {
		java.sql.Date  sqldate = null;
		Date date = null;
		if(strDate.equals("-")){return null;}
		try {
			SimpleDateFormat sdfSource1 = new SimpleDateFormat("dd-MMM-yy HH:mm:ss.SSS");
			date = sdfSource1.parse(strDate);
		} catch (Exception e1) {
			try {
				SimpleDateFormat sdfSource2 = new SimpleDateFormat("dd-MMM-yy");
				date = sdfSource2.parse(strDate);
			}
			catch (Exception e2) {
				try {
					SimpleDateFormat sdfSource3 = new SimpleDateFormat("dd/MM/yyyy");
					date = sdfSource3.parse(strDate);
				}
				catch (Exception e3) {
					try {
						SimpleDateFormat sdfSource4 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
						date = sdfSource4.parse(strDate);
					}
					catch (Exception e4) {
						try {
							SimpleDateFormat sdfSource5 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							date = sdfSource5.parse(strDate);
						}
						catch (Exception e5) {
							try {
								SimpleDateFormat sdfSource6 = new SimpleDateFormat("dd-MM-yyyy");
								date = sdfSource6.parse(strDate);
							}
							catch (Exception e6) {
								e6.printStackTrace();
							}
						}
					}
				}
			}
		}
		sqldate = new java.sql.Date(date.getTime()); 
		return sqldate;
	}
	public static  boolean isReceiveDateLesstheTodayDate(DateFormat df, Date expiredDate, Date todayDate) {
		boolean flag = false;
		if (expiredDate.compareTo(todayDate) == 0) {
			//	        	//System.out.println(df.format(expiredDate) + " and " + df.format(todayDate) + " are equal to each other");
			flag= false;
		}//checking if expiredDate is less than todayDate
		if (expiredDate.compareTo(todayDate) < 0) {
			//	            //System.out.println(df.format(expiredDate) + " is less than " + df.format(todayDate));
			flag=true;
		}

		/*//how to check if expiredDate is greater than todayDate in java
		if (expiredDate.compareTo(todayDate) > 0) {
			//	        	//System.out.println(df.format(expiredDate) + " is greater than " + df.format(todayDate));
			flag=true;
		}*/
		return flag;
	}
	
	
	public static String dateConversionForIssueToOtherSite(String strDate) {
		try {
			// create SimpleFOrmat object with source date format
			SimpleDateFormat sdfSource = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
			// parse the string into Date object
			Date date = sdfSource.parse(strDate);

			// create SimpleDateFormat object with desired date format
			SimpleDateFormat sdfDestination = new SimpleDateFormat("dd-MMM-yy");
			// parse the date into another format
			strDate = sdfDestination.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}
	/*public static void main(String [] args) throws ParseException{


		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy");
		String rd = "21-09-2017";
		Date dcDate = dateFormat.parse(rd);


		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yy");
		//String pd = "11-09-2017";
		//Date podate = format.parse(pd);




		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-yy");//dd/MM/yyyy
		Date now = new Date();
		String strDate = sdfDate.format(now);
		Date podate1 = format.parse(strDate);
		System.out.println(podate1);
		
		

		System.out.println(compareDatesByCompareTo( format, dcDate, podate1));
		

		
	}
*/

	public static Date convertCalenderDateFormatToUtil(String format) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
		Date date=null;
		try {
			date=sdf.parse(format);
			return date;
		} catch (ParseException e) {
			System.out.println("DateUtil.convertCalenderDateFormatToUtil() catch1");
			sdf = new SimpleDateFormat("M-d-yyyy");
			try {
				date=sdf.parse(format);
				return date;
			} catch (ParseException e1) {
				System.out.println("DateUtil.convertCalenderDateFormatToUtil() catch 2");
				sdf = new SimpleDateFormat("M-dd-yyyy");
				try {
					date=sdf.parse(format);
					return date;
				} catch (ParseException e2) {
					System.out.println("DateUtil.convertCalenderDateFormatToUtil() catch 3");
					sdf = new SimpleDateFormat("MM-dd-yyyy");
					try {
						date=sdf.parse(format);
						return date;
					} catch (ParseException e21) {
						System.out.println("DateUtil.convertCalenderDateFormatToUtil() catch 4");
						e21.printStackTrace();
					}
				}
			}
			e.printStackTrace();
		}
		return null;
	}

	/*
	public static String dateConversionForDCForm(String strDate) {
		try {
			// create SimpleFOrmat object with source date format19-09-17
			SimpleDateFormat sdfSource = new SimpleDateFormat("dd-MM-yy");
			// parse the string into Date object
			Date date = sdfSource.parse(strDate);

			// create SimpleDateFormat object with desired date format
			SimpleDateFormat sdfDestination = new SimpleDateFormat("dd-MM-yy  HH:mm:ss.SSS");
			// parse the date into another format
			strDate = sdfDestination.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDate;
	}

	 */
	
	
	public static String MM_DD_YYYYToDD_MM_YY(String source) {
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yy");
		String target = null;
		Date s1 = null;
		try {
			s1 = sdf.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		target = sdf1.format(s1);
		return target;
	}
	
	//12-Nov-18
	public static String DD_MMM_YYToDD_MM_YY(String source) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy");
		SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yy");
		String target = null;
		Date s1 = null;
		try {
			s1 = sdf.parse(source);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		target = sdf1.format(s1);
		return target;
	}
	
	
	public static String Time(String source){
		String s = source;
		
		if(source.startsWith("0")){
			s = source.replaceFirst("0","");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		SimpleDateFormat sdf1 = new SimpleDateFormat("hh:mm:ss.SSS aa");
		
		String time = null;
		try {
		Date date = sdf.parse(s);
		logger.info("***** The source date is *****"+date);
		time = sdf1.format(date);
		logger.info("**** The Target Time is **** "+time);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return time ;
	}
	
	public static String convertDBDateInAnotherFormat(String strDate,String format){
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dt1 = new SimpleDateFormat(format);
	    
	    try{
	    	
	    	if(!strDate.equals("-") && StringUtils.isNotBlank(strDate) && !strDate.equals("0") ){
	    		Date date = dt.parse(strDate);
	    		strDate = dt1.format(date);
	    	}
	    }catch(Exception e){e.printStackTrace();}
		
	    return strDate;
	}
	
	public static void main(String ...strings ) throws Exception {
	
		
		
		
		/*System.out.println("*******88888888888*************");
		Date d = new Date();
		System.out.println(d.getTime());
		
		String now = new SimpleDateFormat("hh:mm aa").format(new java.util.Date().getTime());
		System.out.println(now);
		
		
		String source = "23:59:59.000000 ";
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
		System.out.println(sdf.parse(source));*/
	}
	// this is common methode to getting date in dd-mm-yy
	

}
