package com.sumadhura.util;

import java.util.Random;



public class EncryptionUtil {
		
	public static boolean compareEncryptPassword(String encryptedpin,String PIN, String UID, String SALTKEY){
		boolean result = false;
		try {

			String genencrypted = generateEncryptPassword(PIN,UID ,SALTKEY);
			if(genencrypted.equals(encryptedpin)){
				result = true;
			}
			//System.out.println("Encrypted Password:genencrypted >"+genencrypted);
			//System.out.println("Encrypted Password:encryptedpin >"+encryptedpin);
			//System.out.println("Encrypted Password:>"+result);
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		return result;
	}
	
	public static String generateEncryptPassword(String PIN,String UID ,String SALTKEY){
		String encryptedpin = "";
		try {

			/*RandomNumberGenerator numberGenerator=new SecureRandomNumberGenerator();
			String passwordsalt=numberGenerator.nextBytes().toBase64();
			System.out.println("Password Salt:"+passwordsalt);*/
			//hashMap.put("PASSSALT", saltkey);
			/**
			 * Encrypting the New Password
			 * */
			//System.out.println("PIN Encryption "+encryptedpin);
			
		} catch (Exception e) {
			e.printStackTrace();
			encryptedpin=PIN;
			
		}
		return encryptedpin;
	}
	
      public static String generateSha1EncryptPassword(String PIN,String SALTKEY){
		String encryptedpin = "";
		try {

			/*RandomNumberGenerator numberGenerator=new SecureRandomNumberGenerator();
			String passwordsalt=numberGenerator.nextBytes().toBase64();
			System.out.println("Password Salt:"+passwordsalt);*/
			//hashMap.put("PASSSALT", saltkey);
			/**
			 * Encrypting the New Password
			 * */
				
			
			
		} catch (Exception e) {
			e.printStackTrace();
			encryptedpin=PIN;
			
		}
		return encryptedpin;
	}	
public static String getRandomKey(int len) {
		
		//
		
		final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		Random rnd = new Random();
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		//Log.e("key :: ", sb.toString());
		return sb.toString();
	}

public static String convertStringToHex(String str){
	  
	  char[] chars = str.toCharArray();
	  
	  StringBuffer hex = new StringBuffer();
	  for(int i = 0; i < chars.length; i++){
	    hex.append(Integer.toHexString((int)chars[i]));
	  }
	  
	  return hex.toString();
}
public static void main(String args[]){
	byte[] bytes = "6883".getBytes();
	System.out.println(EncryptionUtil.convertStringToHex("6883"));
}

}
