<%@page import="org.json.JSONArray"%>
<%@page import="com.sumadhura.util.DBConnection"%>
<%@page import="org.springframework.jdbc.core.JdbcTemplate"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>

<%	
	response.setContentType("application/json");
    response.getWriter().print(getVendorDetails());
%>

<%!
	public static JSONArray getVendorDetails() {
	
		JdbcTemplate template = null;
		List<Map<String, Object>> dbVendotList = null;
		List<String> li = null;
		JSONArray json = null;
		try {
			template = new JdbcTemplate(DBConnection.getDbConnection());			
			String vendorQry = "SELECT VENDOR_NAME FROM VENDOR_DETAILS WHERE STATUS = 'A'";			
			dbVendotList = template.queryForList(vendorQry, new Object[]{});			
			li = new ArrayList<String>();			
			for(Map<String, Object> vendor : dbVendotList) {
				li.add(String.valueOf(vendor.get("VENDOR_NAME")).trim());
			}			
			json = new JSONArray(li);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return json;
	}	
%>