
package com.sumadhura.util;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.shiro.crypto.RandomNumberGenerator;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.SimpleByteSource;
import org.springframework.jdbc.core.JdbcTemplate;

import com.sumadhura.transdao.IndentReceiveDaoImpl;

public class SaltPassword {

	/**
	 * @param args
	 * 
	 * 
	 * 
	 * 
	 */
	private static JdbcTemplate jdbcTemplate;
	static Logger log = Logger.getLogger(IndentReceiveDaoImpl.class);

	public static void main1(String[] args) {
		// TODO Auto-generated method stub
		try{
			/*String result ="";
			String request = "1746b0f4-d15f-366e-9b1a-a8d53b7be9d8,49089eac-8c24-3839-a00f-24eebcb05370,80c115a1-4a07-37ab-b85d-dd64f8223e41,7e1676f9-bc8e-3215-8e3f-f47b27529b04,98bb7bfe-1a94-3b4b-8cad-02c222375e2f,d738adcd-93bf-328d-a1aa-b0b37421b94f,5c7c9adc-dc33-3ceb-937b-45547d2d9052,56487855,23d35423-c185-37f4-9f93-93e1abe8b989,97d9d8f7-9188-30b3-82e6-8055dd5e8dea,207c1dd2-c2fb-3aee-a14c-8adb2f62ae56,0d62fd21-a180-36e3-b94b-40c6f51dda27,8fab6f15-634f-3ac9-9783-d91b8563ecea,1a4912c6-bb5e-3462-9bef-cc96ed6615a8,5cb7a94c-37d2-37a8-90ef-1d132f4726e9,2cb2ac08-22e3-3759-8490-8c31c1a72aa5,1a4912c6-bb5e-3462-9bef-cc96ed6615a8,d738adcd-93bf-328d-a1aa-b0b37421b94f,23d35423-c185-37f4-9f93-93e1abe8b989,e4f4f764-1fd2-312f-b7a0-347bd911e7ef,8fab6f15-634f-3ac9-9783-d91b8563ecea,15400ad7-a0bb-3ddc-bf4b-ca55470cc9d4,1a4912c6-bb5e-3462-9bef-cc96ed6615a8,e6503c1e-e971-38e0-bc24-0f3c19120405,a985f5f3-2353-331c-a0c7-e794868295c1,21c0de5d-010d-31d9-ad2c-3ef80c204ef0,a0369e4d-554b-318d-99c7-9b490faa6929,b50fb796-ec6c-3af2-aadc-fe40e421d83a,7e01e7de-3910-3a86-a910-787c8762bdbf,60b476e4-b7a9-356b-8150-8a5cab922eb5,2cb2ac08-22e3-3759-8490-8c31c1a72aa5,b324416a-e809-3a39-a216-9a0d5c267d9f,1eab1812-a449-31ea-a5ad-336d31e68860,3addbd20-543f-3017-86c8-83a1c41b8871,272b02fe-2f32-3ca0-baac-e159f017c557,5f8a1608-4f5a-3fb8-90b5-9e0aafef7ee9,fbd62439-7a6b-3add-af94-74522bd7dee8,6e242a0b-1286-3bd9-9f02-130e6ff4c0f1,a48f9ff2-bd35-347f-98bb-095707ce7908,testing678,testing679,ed6a7203-1370-375b-a4c6-5cc4331c77da,380e7615-3fc4-3d61-885e-b1c7d5b8e6bf,40329fad-01e1-3173-b0d2-d39808251edf,243b4d63-e420-3654-bd0b-2c5cf2b6d7ce,bdf525fc-9716-3650-9a2e-5ae0ee0702c9,b8b938b0-cb81-3de8-b52e-8b5f94a17bbc,4062b844-b055-3b96-8028-99ec39d30f96,efd33690-d3f9-3a8d-b11d-1db68193aea9,a15f49b6-96d5-3834-92f4-684e89c98a75,0a6591c8-ec10-360e-b535-0880dcb8cdf4,9cc26826-becf-3fb0-8a35-96c6d8425745,4ebd8640-07c8-3066-a8ec-59e55a5e3053,d733fa65-5c3e-33f8-aaa5-6a5bc67d9c30";
			String requestArray[] = request.split("\\,");
			for(int i=0;i<requestArray.length;i++){
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap = generateEncryptPassword("1234",requestArray[i]);
			}
			String password = compareEncryptPassword("1234","mukhtari",hashMap.get("PASSSALT").toString());
			if(hashMap.get("PASSSWORD").toString().equals(password)){
				
				System.out.println("True");
			}else{
				System.out.println("False");
			}
			System.out.println("New Encrypted Password is::>"+password);
			*/
		}catch(Exception ex){
			ex.printStackTrace();
		}

	}
	
	public static String compareEncryptPassword(String _password, String _loginName, String passSalt){
		String result = "";
		try {

			Sha256Hash sha256Hash = new Sha256Hash((String)_password, (new SimpleByteSource(passSalt + (String)_loginName)).getBytes());
			result = sha256Hash.toHex();
						System.out.println("Encrypted Password:: in of old password >"+result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return result;
	}
	
	public static  HashMap<String, String> generateEncryptPassword(String _password, String _loginName){
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {

			RandomNumberGenerator numberGenerator=new SecureRandomNumberGenerator();
			String passwordsalt=numberGenerator.nextBytes().toBase64();
			System.out.println("Password Salt:"+passwordsalt);
			hashMap.put("PASSSALT", passwordsalt);
			/**
			 * Encrypting the New Password
			 * */
			//Sha256Hash sha256Hash = new Sha256Hash((String)_password, (new SimpleByteSource(passwordsalt + (String)_loginName)).getBytes());
			Sha256Hash sha256Hash = new Sha256Hash((String)_password, (new SimpleByteSource( (String)_loginName)).getBytes());
			String userNewPassword = sha256Hash.toHex();
			System.out.println("New Encrypted Password is::>"+userNewPassword);
			hashMap.put("PASSSWORD", userNewPassword);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return hashMap;
	}
	
	
	public static void main(String [] args){
		
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap = generateEncryptPassword("sumadhura@5949","hydmkt1"); //password,login name
		
		String strSaltPassword = hashMap.get("PASSSWORD");
		
		int result=0;

	//	String updatelogin = "UPDATE SUMADHURA_LOGIN  SET SALT_PASSWORD='"+strSaltPassword+"' where USER_ID='ravi'and PASSWORD='sumadhura_5949'";
	//	log.debug("Query for update indent avalibility = "+updatelogin);

		//Object jdbcTemplate;
//	result = jdbcTemplate.update(updatelogin, new Object[] {});
			
		//log.debug("Result = "+result);
		//.out.println(hashMap.get("PASSSWORD"));
		System.out.println("the encrypted password   "+strSaltPassword);
		
		
		//////////////above generate
		
		//String password = compareEncryptPassword("ravi","sumadhura_5949",hashMap.get("PASSSALT").toString());
		
		//System.out.println("the new ravi password"+password);
		
		
		/*if(strSaltPassword.toString().equals(password)){
			
			System.out.println("true");
		}*/
	}

}
