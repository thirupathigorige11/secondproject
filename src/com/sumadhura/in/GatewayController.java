package com.sumadhura.in;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;



@Controller
public class GatewayController extends HandleRequest
{
	


	private String getRequestParamter(HttpServletRequest request) {
		@SuppressWarnings("unchecked")
		Enumeration<String> keys = request.getParameterNames();
		StringBuilder requestParmeter = new StringBuilder();
		while (keys.hasMoreElements())
		{
			String key = (String)keys.nextElement();
			String value = request.getParameter(key);
			requestParmeter.append(key + "=" + value + "&");
		}
		if(requestParmeter.length()>0){
			requestParmeter.append(requestParmeter.substring(0, requestParmeter.length() - 1));
		}
		return requestParmeter.toString().trim();
	}
}