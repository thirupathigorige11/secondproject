package com.sumadhura.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author ANIKET DATE 03-APR-2019 TIME 08.30 PM
 */

@Component
@PropertySource(value = { "classpath:validationproperties.properties" })
public class MobileSMS  extends UIProperties {
 
	private final static Logger logger = Logger.getLogger(MobileSMS.class);
	private void mai() throws IOException, JSONException {
		MobileSMS mobileSMS=new MobileSMS();
		List<String> mobilesList=new ArrayList<String>();
		mobileSMS.sendSMS(mobilesList, "");
	}
	
	public void sendSMS(List<String> mobilesList,String message) throws IOException, JSONException {
		
			String authkey=UIProperties.validateParams.getProperty("MOBILE_SMS_AUTHENTICATION_KEY") == null ? "" : validateParams.getProperty("MOBILE_SMS_AUTHENTICATION_KEY").toString();
			String senderId=UIProperties.validateParams.getProperty("MOBILE_SMS_SENDER_ID") == null ? "" : validateParams.getProperty("MOBILE_SMS_SENDER_ID").toString();
			String route=UIProperties.validateParams.getProperty("MOBILE_SMS_ROUTE") == null ? "" : validateParams.getProperty("MOBILE_SMS_ROUTE").toString();
			String smsSendingUrl=validateParams.getProperty("MOBILE_SMS_URL") == null ? "" : validateParams.getProperty("MOBILE_SMS_URL").toString();
			System.out.println("MobileOTPSMS.sendOTP() "+authkey+" "+senderId+" "+route+" "+smsSendingUrl);
		
		// public static void main(String ...strings ) {

		// Your authentication key 
		// String authkey = "c75154b9a8667a7dd686804098c88cf";

		// Multiple mobile numbers separated by comma
		// String mobile = "8500085263,9441321175,7299999765,8106436035";
		String mobiles = mobileNumberUtil(mobilesList);

		// Sender ID,While using route4 sender id should be 6 characters long.
		// String senderId = "SUMAPP";

		// define route
		// String route = "10";

		//final Integer otp = generateOTP();

		// Your message to send, Add URL encoding here.
	/*	String message1 = "Dear Customer,Welcome to sumadhura customer app. Here you go with OTP " + otp
				+ " to register into customer app ";

		String message = "Dear Customer,Welcome to sumadhura customer app. " + otp
				+ " is your OTP (One Time Password) for completing your registration. It is usable only once. Please do not share OTP with anyone.";
*/
		/*
		 * logger.info("** The values for the sending otp authkey is *****"
		 * +authkey); logger.info(
		 * "** The values for the sending otp senderId is *****"+senderId);
		 * logger.info("** The values for the sending otp route is *****"
		 * +route); logger.info(
		 * "** The values for the sending otp message is *****"+message);
		 */
		// Properties properties = Properties new
		// Thread.currentThread().getContextClassLoader().getResourceAsStream("applicationname.properties");

		// Prepare Url
		URLConnection myURLConnection = null;
		URL myURL = null;
		BufferedReader reader = null;

		// encoding message
		String encoded_message = URLEncoder.encode(message);

		// Send SMS API
		
		String mainUrl = smsSendingUrl+"?AUTH_KEY=" + authkey + "&message="
				+ encoded_message + "&senderId=" + senderId + "&routeId=10&mobileNos=" + mobiles
				+ "&smsContentType=english";

		// Prepare parameter string
		StringBuilder sbPostData = new StringBuilder(mainUrl);
		sbPostData.append("authkey=" + authkey);
		sbPostData.append("&mobiles=" + mobiles);
		sbPostData.append("&message=" + encoded_message);
		sbPostData.append("&route=" + route);
		sbPostData.append("&sender=" + senderId);

		// final string
		mainUrl = sbPostData.toString();

		// try {
		// prepare connection
		myURL = new URL(mainUrl);
		myURLConnection = myURL.openConnection();
		myURLConnection.connect();
		reader = new BufferedReader(new InputStreamReader(myURLConnection.getInputStream()));
		// reading response
		String response;
		// while ((response = reader.readLine()) != null)

		if ((response = reader.readLine()) != null)
			// print response
			// System.out.println(response);
			logger.info("** The response is ****" + response);

		// try {
		JSONObject jsonObj = new JSONObject(response);
		String responseCode = (String) jsonObj.get("responseCode");
		logger.info("** The response code is ****" + responseCode);

		if (responseCode.equalsIgnoreCase("3001")) {
			System.out.println("SMS send successfully...");
			//long pk = registrationServiceImpl.saveOTP(registrationInfo);

		}
		
		
		// } catch (JSONException e) {
		// TODO Auto-generated catch block
		// e.printStackTrace();
		// }

		// finally close connection
		reader.close();
		// } catch (IOException e) {
		// e.printStackTrace();
		// }

	}

	public String mobileNumberUtil(List<String> mobileList) {
		String mobileString = mobileList.toString().replaceAll("\\s", "");
		// System.out.println(mobileString.substring(1,
		// mobileString.length()-1));
		return mobileString.substring(1, mobileString.length() - 1);
	}

/*	public int generateOTP() {
		int randomNumber = 100000 + new Random().nextInt(80000);
		return randomNumber;
	}
*/
}