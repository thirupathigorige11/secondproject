package com.sumadhura.util;
//package com.snapwork.service;
import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.sumadhura.util.UIProperties;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
public class AESDecrypt extends UIProperties {

public String encrypt(String Key,String Value) throws Exception{
		   byte[] abyte2 = null;
	       String plainText = Value;
	        byte s1[]=plainText.getBytes();
			//Newly added code-End
	        byte[] abyte1 = Key.getBytes();
	        SecretKeySpec secretkeyspec = new  SecretKeySpec(abyte1, "AES");

	        SecretKeySpec secretkeyspec1 = secretkeyspec;

	        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");

	        cipher.init(1, secretkeyspec1);

	        abyte2 = cipher.doFinal(s1);

	        BASE64Encoder encoder = new BASE64Encoder();

	        String base64 = encoder.encode(abyte2);
	        //System.out.println("Encrypt data:"+base64);
			return base64;
    }
	

	
	public static String decrypt(String strKey, String Value) throws Exception{
		String result = "";
		//logger.info(strKey+" common Decr  "+Value);
		  SecretKeySpec skeySpec = new SecretKeySpec(strKey.getBytes(), "AES");
	        Key secretKey = skeySpec;
	        Cipher aesCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	        aesCipher.init(2, secretKey);
	        byte[] plaintext = (byte[])null;
	        BASE64Decoder encoder = new BASE64Decoder();
	        byte[] base64 = encoder.decodeBuffer(Value);
	        plaintext = aesCipher.doFinal(base64);
	        result = new String(plaintext);
	       // logger.info("D String ----------------" + result);
	        
		return result;
	}
	
	 
	


		public static void main(String[] args) throws Exception{
		        AESDecrypt encrypt=new AESDecrypt();

		        String poNumber="PO/SIPL/BNG/160/32/2018-19/17";
		        String strIndentFrom="111";

		        String userData="poNumber="+poNumber+"&indentSiteId="+strIndentFrom;
		        
		       // String userData="poNumber="+poNumber+"&indentSiteId=1345";

		        String strEncryptionData =encrypt.encrypt("AMARAVADHIS12345",userData);
		        
		       
		        
		       strEncryptionData =  strEncryptionData.replaceAll("[\\t\\n\\r]+","");
		        
		        System.out.println("the strEncryptionData data  "+strEncryptionData.trim());
		        AESDecrypt decrypt=new AESDecrypt();
		    //    String strData ="aL5LifzfWT6l/lXvebZN/WBZ2aWCyHFBZdZGBpWqbkR2EMe6Vhvieg4qkgyCXr3dMeRk6aFo3EWs";                 //request.getParameter("data");

		        strEncryptionData = strEncryptionData.replace(" ","+");
		        String strDecrypt =decrypt.decrypt("AMARAVADHIS12345","aL5LifzfWT6l/lXvebZN/WBZ2aWCyHFBZdZGBpWqbkR2EMe6Vhvieg4qkgyCXr3d/Ug7MdOa6Rej9quRi0ab3A==");

		        System.out.println("the decrypt data"+strDecrypt);

		    }
	
}

