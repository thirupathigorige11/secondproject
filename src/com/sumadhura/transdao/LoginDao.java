package com.sumadhura.transdao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.SimpleByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.sumadhura.bean.MenuDetails;
import com.sumadhura.bean.userDetails;
import com.sumadhura.service.DCFormService;
import com.sumadhura.util.DBConnection;


@Repository
public class LoginDao {
	//JdbcTemplate jt = null;


	@Autowired(required = true)
	private JdbcTemplate jt;



	/*public boolean validate(userDetails ud, HttpServletRequest request) {

		List<Map<String, Object>> empDetailsList = null;

		JdbcTemplate jt = null;
		String strDBPassword = "";
		String strSiteId = "";
		String strProductId = "";
		String strUserName = "";
		boolean isPassValid = false;

		try {

			jt = new JdbcTemplate(DBConnection.getDbConnection());

			String sql = "select pass,SITE_ID,USERNAME from login_dummy where UNAME='" + ud.getUname() + "'";

			System.out.println(" in dao  " + ud.getUname());
			empDetailsList = jt.queryForList(sql);

			if (empDetailsList.size() > 0) {

				for (Map empDtlsList : empDetailsList) {

					strDBPassword = empDtlsList.get("pass") == null ? "" : empDtlsList.get("pass").toString();
					strSiteId = empDtlsList.get("SITE_ID") == null ? "" : empDtlsList.get("SITE_ID").toString();
					strUserName = empDtlsList.get("USERNAME") == null ? "" : empDtlsList.get("USERNAME").toString();




					String strProduct = request.getParameter("PRODUCT_NAME") == null ? "" : request.getParameter("PRODUCT_NAME").toString();


					System.out.println("strSiteId  " + strSiteId);
					System.out.println("PRODUCT_NAME  " + strProduct);


					if (strDBPassword.equals(ud.getPass())) {

						isPassValid = true;
						HttpSession session = request.getSession(true);
						session.setAttribute("SiteId", strSiteId);
						session.setAttribute("UserId", ud.getUname());
						session.setAttribute("UserName", strUserName);
						//request.setAttribute("SiteId", strSiteId);
						List<MenuDetails> list = null;
						list = getMenuDetails();
						//List<MenuDetails> list = dcFormService.getMD();
						session.setAttribute("menu", list);

					} else {
						request.setAttribute("ErrorMessage", "invalid Password");
					}
				}

			} else {

				request.setAttribute("ErrorMessage", "invalid userid");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isPassValid;

	}*/


	public boolean validate(userDetails ud, HttpServletRequest request) {

		List<Map<String, Object>> empDetailsList = null;
		HashMap<String, String> hashMap = new HashMap<String, String>();


		JdbcTemplate jt = null;
		String strDBPassword = "";
		String strSiteId = "";
		String sitename = "";
		String strProductId = "";
		String strUserName = "";
		boolean isPassValid = false;
		int intCount = 0; 
		int intRoleId = 0;
		String strEmpId = "";
		String profileType="";
		try {
			
			String username=request.getParameter("uname");
			String password=request.getParameter("pass");
			
			hashMap = generateEncryptPassword(password,username);
			
	
			
			String strSaltPassword = hashMap.get("PASSSWORD");
			
			
			
			jt = new JdbcTemplate(DBConnection.getDbConnection());
			
			if(password!=null && !password.equals("")){
			String sql = "select SL.ROLE_ID,SED.EMP_NAME,SED.USER_PROFILE,SED.EMP_ID,S.SITE_NAME from  SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_LOGIN SL,SITE S where "+
					 "SL.SALT_PASSWORD='"+strSaltPassword+"' and SL.SITE_ID = ? and SL.STATUS = ? and SED.STATUS = ? and "+
					 "S.SITE_ID = SL.SITE_ID and SL.EMPLOYEEID = SED.EMP_ID ";

			System.out.println(" in dao  " + ud.getUname());
			empDetailsList = jt.queryForList(sql,new Object[]{ud.getStrSiteId(),"A","A"});

			if (empDetailsList.size() > 0) {
				for (Map empDtlsList : empDetailsList) {

					strDBPassword = ud.getPass();//empDtlsList.get("pass") == null ? "" : empDtlsList.get("pass").toString();
                    //strSiteId = empDtlsList.get("SITE_ID") == null ? "" : empDtlsList.get("SITE_ID").toString();
					strUserName = empDtlsList.get("EMP_NAME") == null ? "" : empDtlsList.get("EMP_NAME").toString();
					intRoleId = Integer.parseInt(empDtlsList.get("ROLE_ID") == null ? "" : empDtlsList.get("ROLE_ID").toString());
					strEmpId = empDtlsList.get("EMP_ID") == null ? "" : empDtlsList.get("EMP_ID").toString();
					sitename = empDtlsList.get("SITE_NAME") == null ? "" : empDtlsList.get("SITE_NAME").toString();
					profileType=empDtlsList.get("USER_PROFILE")==null?"":empDtlsList.get("USER_PROFILE").toString();

					String strProduct = request.getParameter("PRODUCT_NAME") == null ? "" : request.getParameter("PRODUCT_NAME").toString();


					System.out.println("strSiteId  " + strSiteId);
					System.out.println("PRODUCT_NAME  " + strProduct);


					if (strDBPassword.equals(ud.getPass())) {

						isPassValid = true;
						HttpSession current_session = request.getSession(false);
						if (current_session != null) {
				        	current_session.invalidate();
				        }
						HttpSession session = request.getSession(true);
						session.setAttribute("SiteId", ud.getStrSiteId());
						session.setAttribute("UserId", strEmpId);
						session.setAttribute("SiteName", sitename);
						session.setAttribute("UserName", strUserName);
						session.setAttribute("UserProfileType", profileType);
						//request.setAttribute("SiteId", strSiteId);
						session.setAttribute("Roleid",intRoleId);
						List<MenuDetails> list = null;
						list = getMenuDetails(intRoleId);
						//List<MenuDetails> list = dcFormService.getMD();
						session.setAttribute("menu", list);

					} else {
						request.setAttribute("ErrorMessage", "invalid Password");
					}
				}

			} else {

				request.setAttribute("ErrorMessage", "invalid userid");

			}
			
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isPassValid;

	}

	public List<MenuDetails> getMenuDetails(int intRoleId){  

		String strSql = "";
		List<MenuDetails> list = null;
		try{

			/*strSql+= "select SMM.MODULE_NAME,SMSM.SUB_MODULE_NAME,SMSM.SUB_MODULE_ID,SMM.MODULE_ID,SMSM.PAGE_LINK from ";
			strSql+= "SUMADHURA_LOGIN SL, ";
			strSql+= "SUMADHURA_ROLES SR, ";
			strSql+= "SUMADHURA_MENU_ROLE_GROUP SMRG, ";
			strSql+= "SUMADHURA_MENU_MODULE SMM,SUMADHURA_MENU_SUB_MODULE SMSM ";
			strSql+= "where SL.ROLE_ID = "+intRoleId+" ";
			strSql+= "and SL.ROLE_ID = SR.ID ";
			strSql+= "and SR.ID = SMRG.ROLE_ID ";
			strSql+= "and SMRG.MODULE_ID = SMM.MODULE_ID ";
			strSql+= "and SMRG.SUB_MODULE_ID = SMSM.SUB_MODULE_ID ";
			strSql+= "and SMM.MODULE_ID = SMSM.MODULE_ID ";
			strSql+= "and SR.STATUS = 'A' ";
			strSql+= "and SMM.STATUS = 'A' ";
			strSql+= "and SMSM.STATUS = 'A' ";*/
			
			strSql+= "select SMM.MODULE_NAME,SMM.MODULE_ICON,SMSM.SUB_MODULE_NAME,SMSM.SUB_MODULE_ID,SMM.MODULE_ID,SMSM.PAGE_LINK from  ";
			strSql+= "SUMADHURA_ROLES SR, ";
			strSql+= "SUMADHURA_MENU_ROLE_GROUP SMRG, ";
			strSql+= "SUMADHURA_MENU_MODULE SMM,SUMADHURA_MENU_SUB_MODULE SMSM ";
			strSql+= "where SR.ID ="+intRoleId+" ";
			strSql+= "and SR.ID = SMRG.ROLE_ID  ";
			strSql+= "and SMRG.MODULE_ID = SMM.MODULE_ID "; 
			strSql+= "and SMRG.SUB_MODULE_ID = SMSM.SUB_MODULE_ID ";
		    strSql+= "and SMM.MODULE_ID = SMSM.MODULE_ID ";
			strSql+= "and SR.STATUS = 'A' ";
			strSql+= "and SMM.STATUS = 'A' ";
			strSql+= "and SMSM.STATUS = 'A'  and SMRG.STATUS ='A'";

			return jt.query(strSql,new ResultSetExtractor<List<MenuDetails>>(){  
				@Override  
				public List<MenuDetails> extractData(ResultSet rs) throws SQLException, DataAccessException { 
					List<MenuDetails> list=new ArrayList<MenuDetails>(); 

					while(rs.next()){  
						//System.out.println("in whileloop :"+rs.getString(5));
						MenuDetails md=new MenuDetails(); 
					//	md.setMenuId(rs.getString(1));  
						md.setMajorHeadId(rs.getString("MODULE_ID")); 
						md.setMajorHeadName(rs.getString("MODULE_NAME"));
						md.setMinorHeadId(rs.getString("SUB_MODULE_ID"));  
						md.setMinorHeadName(rs.getString("SUB_MODULE_NAME"));
						//md.setUserId(rs.getString(6));
						md.setMappingLink(rs.getString("PAGE_LINK"));
						md.setIconName(rs.getString("MODULE_ICON"));
						list.add(md);
					}

					return list;  
				}  

			}); 

		}catch(Exception e){
			e.printStackTrace();
		}

		return list; 
	} 
	


	public static  HashMap<String, String> generateEncryptPassword(String _password, String _loginName){
		HashMap<String, String> hashMap = new HashMap<String, String>();
		try {

		/*	RandomNumberGenerator numberGenerator=new SecureRandomNumberGenerator();
			String passwordsalt=numberGenerator.nextBytes().toBase64();
			System.out.println("Password Salt:"+passwordsalt);
			hashMap.put("PASSSALT", passwordsalt);*/
			/**
			 * Encrypting the New Password
			 * */
			//Sha256Hash sha256Hash = new Sha256Hash((String)_password, (new SimpleByteSource(passwordsalt + (String)_loginName)).getBytes());
			Sha256Hash sha256Hash = new Sha256Hash((String)_password, (new SimpleByteSource( (String)_loginName)).getBytes());
			String userNewPassword = sha256Hash.toHex();
			//System.out.println("New Encrypted Password is::>"+userNewPassword);
			hashMap.put("PASSSWORD", userNewPassword);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
		}
		return hashMap;
	}

	public String empEmailAndPassword(String userName,String empId,String site_id){
		String query="";
		String strUserName="";
		String strEmpId="";
		String strEmailAddress="";
		String strSaltPassword="";
		String response="";
		List<Map<String, Object>> empDetailsList = null;
		try{

			if(!userName.equals("") && !empId.equals("")){

				query="select DISTINCT(IED.EMP_ID),IED.EMP_NAME,IED.EMP_EMAIL,SL.SALT_PASSWORD from SUMADHURA_EMPLOYEE_DETAILS IED,SUMADHURA_LOGIN SL"
						+" where IED.EMP_NAME= ? and IED.EMP_ID=? AND SL.EMPLOYEEID=IED.EMP_ID AND SL.SITE_ID=? ";

				empDetailsList=jt.queryForList(query,new Object[]{userName,empId,site_id});

				for (Map empDtlsList : empDetailsList) {
					strEmailAddress=empDtlsList.get("EMP_EMAIL") == null ? "" : empDtlsList.get("EMP_EMAIL").toString();	
					strSaltPassword=empDtlsList.get("SALT_PASSWORD") == null ? "" : empDtlsList.get("SALT_PASSWORD").toString();	
					response=" "+strEmailAddress+"$$$"+strSaltPassword;

				}


			}


			else if(userName!=null && !userName.equals("")){

				query="select DISTINCT(IED.EMP_ID),IED.EMP_NAME,IED.EMP_EMAIL,SL.SALT_PASSWORD from SUMADHURA_EMPLOYEE_DETAILS IED,SUMADHURA_LOGIN SL"
						+" where IED.EMP_NAME= ? AND SL.EMPLOYEEID=IED.EMP_ID or SL.SITE_ID= ? ";

				empDetailsList=jt.queryForList(query,new Object[]{userName,site_id});

				for (Map empDtlsList : empDetailsList) {
					strEmailAddress=empDtlsList.get("EMP_EMAIL") == null ? "" : empDtlsList.get("EMP_EMAIL").toString();	
					strSaltPassword=empDtlsList.get("SALT_PASSWORD") == null ? "" : empDtlsList.get("SALT_PASSWORD").toString();	
					response=" "+strEmailAddress+"$$$"+strSaltPassword;

				}

			}else if(empId!=null && !empId.equals(""))
			{

				query="select DISTINCT(IED.EMP_ID),IED.EMP_NAME,IED.EMP_EMAIL,SL.SALT_PASSWORD from SUMADHURA_EMPLOYEE_DETAILS IED,SUMADHURA_LOGIN SL"
						+" where IED.EMP_ID= ? AND SL.EMPLOYEEID=IED.EMP_ID AND SL.SITE_ID=? ";

				empDetailsList=jt.queryForList(query,new Object[]{empId,site_id});
				for (Map empDtlsList : empDetailsList) {

					strEmailAddress=empDtlsList.get("EMP_EMAIL") == null ? "" : empDtlsList.get("EMP_EMAIL").toString();	
					strSaltPassword=empDtlsList.get("SALT_PASSWORD") == null ? "" : empDtlsList.get("SALT_PASSWORD").toString();	

					response=" "+strEmailAddress+"$$$"+strSaltPassword;
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		
		return response;
	}
	
	public String getUserName(String site_id,String empId){
		List<Map<String, Object>> empDetailsList = null;

		String query="";
		String empName="";
		String strvalue="";
		String saltPassword="";
		query="select SED.EMP_NAME,SED.EMP_ID,SL.SITE_ID,SL.SALT_PASSWORD from SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_LOGIN SL"
				+" WHERE SED.EMP_ID=SL.EMPLOYEEID AND  SL.EMPLOYEEID=? AND SL.SITE_ID=?";

		empDetailsList=jt.queryForList(query,new Object[]{empId,site_id});
		for (Map empDtlsList : empDetailsList) {

			empName=empDtlsList.get("EMP_NAME") == null ? "" : empDtlsList.get("EMP_NAME").toString();	
			saltPassword=empDtlsList.get("SALT_PASSWORD") == null ? "" : empDtlsList.get("SALT_PASSWORD").toString();
		//	strvalue=empId+"$$$"+saltPassword;
		}

		return empName;

	}
	
	public int  updatePassword(String empId,String site_id,String newPassword,String strSaltPassword){

		List<Map<String, Object>> empDetailsList = null;
		String query="";
		query="update SUMADHURA_LOGIN set SALT_PASSWORD= ? WHERE EMPLOYEEID= ? and SITE_ID=? ";

		int val=jt.update(query,new Object[]{strSaltPassword,empId,site_id});

		return val;
	}
	
	public String getEmpId(String site_id,String userName){
		List<Map<String, Object>> empDetailsList = null;

		String query="";
		String empId="";
		String strvalue="";
		String saltPassword="";
		query="select SED.EMP_NAME,SED.EMP_ID,SL.SITE_ID,SL.SALT_PASSWORD from SUMADHURA_EMPLOYEE_DETAILS SED,SUMADHURA_LOGIN SL"
				+" WHERE SED.EMP_ID=SL.EMPLOYEEID AND  SED.EMP_NAME=? AND SL.SITE_ID=?";

		empDetailsList=jt.queryForList(query,new Object[]{userName,site_id});
		
		for (Map empDtlsList : empDetailsList) {

			empId=empDtlsList.get("EMP_ID") == null ? "" : empDtlsList.get("EMP_ID").toString();
			saltPassword=empDtlsList.get("SALT_PASSWORD") == null ? "" : empDtlsList.get("SALT_PASSWORD").toString();
			
		//	strvalue=empId+"$$$"+saltPassword;
		}

		return empId;

	}
	
	
	
}