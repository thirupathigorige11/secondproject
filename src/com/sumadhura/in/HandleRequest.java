package com.sumadhura.in;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sumadhura.util.UIProperties;

public class HandleRequest extends UIProperties{

	private String requestHeaders;
	
	public HandleRequest() {
		// TODO Auto-generated constructor stub
	}
	
	private void iniBean(){
		requestHeaders = "";
	}
	
	public boolean handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException,
				IOException,Exception {
		boolean _isuserAgentExist = false;
		requestHeaders = "";
		try {
			
		/*	response.setHeader("Cache-control", "no-cache, no-store, must-revalidate, pre-check=0, post-check=0, max-age=0, s-maxage=0");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "-1");*/
			
			/*MultiValueMap multiValueMap = new MultiValueMap();
			List<String> headerNames = Collections.list((Enumeration<String>)request.getHeaderNames());
			for(String headerNameIs:headerNames){
				if(request.getHeader(headerNameIs)!=null){
					requestHeaders=requestHeaders+headerNameIs+"="+request.getHeader(headerNameIs)+"&";
				}
			}*/
			//logger.info	("Mobile Request Header:"+requestHeaders);
			
		
			String userAgent = validateParams.getSafeHeaderParamValues(request, "User-Agent");
			if(userAgent != null){
				_isuserAgentExist = validateParams.validateUserAgentExistOrNot(userAgent);
			
			}
			
		}
		
		catch (NullPointerException ne) {
			throw new NullPointerException("exception.NullPointerException");
		} 
		catch(Exception exception){
			throw new Exception(validateParams.getProperty("exception.exception"));
		}
	
		
		return _isuserAgentExist;
	}

	


}
