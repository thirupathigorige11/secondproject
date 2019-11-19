package com.sumadhura.util;

import org.springframework.web.servlet.ModelAndView;

public class CheckSessionValidation {

	public static String validateUserSession(ModelAndView model, Object sessionId) {
		
		String id = "0";
		try {
			if (sessionId == null) {
				model.addObject("Message", "Session Expired, Please Login Again!");
				model.setViewName("index");
			} else {
				id = (String) sessionId;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return id;
	}
}
