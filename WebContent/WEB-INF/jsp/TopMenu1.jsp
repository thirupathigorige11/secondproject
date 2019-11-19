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
								<a class="site_title" href="dashboard.spring"><img src="images/logo.png"></a>
							</div>
				
				<!-- 			<div class="nav toggle">
								<a id="menu_toggle" class="badge"><i class="fa fa-bars"></i></a>
							</div> -->

							<ul class="nav navbar-nav navbar-left">
								<li>
									<!-- <div class="fa2">
										<marquee scrolldelay="100"><i class="fa fa-newspaper-o" aria-hidden="true"></i><span class="news">Latest News:</span>&nbsp;&nbsp;<a href="#"><span style="color:#000000; font-weight: bold"> Getting the vendor wise invoice total amount in Get invoice details page,invoice image preview added </span></marquee></a>
									</div> -->
									<!-- <div class="fa3">
										<a href="#"><i class="fa fa-newspaper-o" aria-hidden="true"></i></a>
									</div> -->
								</li>
							</ul>
							<ul class="nav navbar-nav navbar-right">
								<li class="">
									<a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown" aria-expanded="false">
										
										<%
										    String strUserName = session.getAttribute("UserName") == null ? "" : session.getAttribute("UserName").toString();
										%>
										
										<span class=" fa1">Hello <%= strUserName%>  !</span><img alt="" src="images/img.jpg">
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
								<!-- <li>
									<a href="javascript:void(0);"><i class="fa fa-bell-o play" aria-hidden="true"><span class="badge">3</span></i></a>
								</li> -->
									
							</ul>
						</nav>
					</div>
				</div>
				<div class="clearfix"></div>
						
	