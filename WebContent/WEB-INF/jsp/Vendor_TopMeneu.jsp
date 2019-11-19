<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>  
<%-- <%
	HttpSession s1 = request.getSession(false);
	s1.setMaxInactiveInterval(2 * 600);
	System.out.println("in----"+s1.isNew());
	if (s1.isNew()) {
		s1.invalidate();
		System.out.println("inside in----");
		
	%>
	
		<jsp:forward page="index.jsp" />
		
		<%
		response.sendRedirect("../index.jsp");
	}
	response.setHeader("Refresh", "1200;url=./index.jsp");
	if (s1 == null) {
		System.out.println("in");
		response.sendRedirect("../index.jsp");
	}
	//String strName = s1.getAttribute("username") == null ? "" : session.getAttribute("username").toString();
	/* if(strName.equals("")){
	
	 } */


%> --%>
<div class="top_nav">
					<div class="nav_menu">
						<nav>
							<div style="border: 0;" class=" nav_title">
								<a class="site_title"><img src="images/logo.png"></a> <!-- href="dashboard.spring" -->
							</div>

							<ul class="nav navbar-nav navbar-right">
								<li class="">
									<a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
										
										<%
										    String strUserName = request.getAttribute("vendorName") == null ? "" : request.getAttribute("vendorName").toString();
										%>
										
										<span class=" fa1">Hello <%= strUserName.toUpperCase()%>  !</span><img alt="" src="images/img.jpg">
										<span class=" fa fa-angle-down"></span>
									</a>
									<ul class="dropdown-menu dropdown-usermenu pull-right">
										<li><a href="javascript:;"> Profile</a></li>
										<li>
										  <a href="javascript:;">
											<span class="badge bg-red pull-right">50%</span>
											<span>Settings</span>
										  </a>
										</li>
										<li><a href="javascript:;">Help</a></li>
										<li><a href="logout.spring"><i class="fa fa-sign-out pull-right"></i> Log Out</a></li>
								  </ul>
								</li>
									
							</ul>
						</nav>
					</div>
				</div>
				<div class="clearfix"></div>
						
	