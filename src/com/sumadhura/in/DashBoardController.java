package com.sumadhura.in;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


import com.sumadhura.bean.userDetails;
import com.sumadhura.transdao.LoginDao;


@Controller
public class DashBoardController  {

	@Autowired
	private LoginDao dao;

	

	@RequestMapping("/dashboard.spring")
	public ModelAndView dashBoradPage(HttpServletRequest requset, HttpServletResponse response) throws Exception {
		
				return new ModelAndView("DashBoard");
			
	}


	

}
