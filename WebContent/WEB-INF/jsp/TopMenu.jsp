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
<style>
 .toggle {padding-left:15px;}
/*  .nav.side-menu>li {border-bottom:1px solid #ccc;} */
.nav_title {width: 184px;}
.navbar-left {width:47%;}
 @media only screen and (min-width:320px) and (max-width:767px){.nav_title {width: 50%;margin-left:15px;}}
 /* loader css  */
#loadingimsMessage{
    display: inline-block;
  position: absolute;
  top:58%;
  left:58%;
}
#loadingimsMessage{
	display: inline-block;
    position: absolute;
    margin-top: 60px;
    margin-left:17px;
}

.lds-ims {
  display: inline-block;
  position: absolute;
  width: 84px;
  height: 64px;
  background-color:#ef7e2d;
  padding:5px;

}
.loader-ims{
	position: fixed;
   left:58%;
   top:53%;
   z-index:99999;
}
/* .loader-ims{ 
	position: fixe;
   left:58%;
   top:50%;
   } */
    @media only screen and (min-width:320px) and (max-width:767px){
    .loader-ims {
    position: absolute;
    left: 40%;
    top: 75%;
}
   }
.lds-ims div {
  display: inline-block;
  position: absolute;
  left: 6px;
  width: 5px;
  background: #fff;
  animation: lds-facebook 1.2s cubic-bezier(0, 0.5, 0.5, 1) infinite;
}
.lds-ims div:nth-child(1) {
  left: 16px;
  padding-left:5px;
  animation-delay: -0.60s;
}
.lds-ims div:nth-child(2) {
  left: 25px;
  
  animation-delay: -0.48s;
}
.lds-ims div:nth-child(3) {
  left: 35px;
  
  animation-delay: -0.36s;
}
.lds-ims div:nth-child(4) {
  left: 45px;
  animation-delay: -0.24s;
  
}
.lds-ims div:nth-child(5) {
  left: 55px;
  animation-delay: -0.12s;
  
}
.lds-ims div:nth-child(6) {
  left: 65px;
  animation-delay: 0;
  
}
@keyframes lds-ims {
  0% {
    top: 6px;
    height: 51px;
  }
  50%, 100% {
    top: 19px;
    height: 26px;
  }
}
.overlay_ims{
  background: #e9e9e9;  
  opacity: 0.5; 
  position: fixed;
  z-index: 999;
  height: 2em;
  width: 2em;
  overflow: show;
  margin: auto;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  width:100%;
  height:auto;
}

/* loader css end */
</style>
<div class="top_nav">
					<div class="nav_menu">
						<nav>
						    <div class="nav toggle">
								<a id="menu_toggle" class="badge"><i class="fa fa-bars"></i></a>
							</div>
							<div style="border: 0;" class=" nav_title">
								<a class="site_title" href="dashboard.spring"><img src="images/logo.png"> </a>
							</div>
							<ul class="nav navbar-nav navbar-left">
								<li>
									<div class="fa2">
										<marquee scrolldelay="100"><i class="fa fa-newspaper-o" aria-hidden="true"></i><span class="news">Latest News:</span>&nbsp;&nbsp;<span style="color:#000000; font-weight: bold">Hello All, 1) Enabled Modify Work Order option for QS, A work order in approvals can send back to QS for any modifitions in work order. 2) Added Credit Note option for Stores, A Debit Note will be generated and can take details of Credit Note from Vendor.</span>
										
										</marquee>
									</div>
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
										
										<span class=" fa1">Hello <%= strUserName.toUpperCase()%>  !</span><img alt="" src="images/usericon.png">
										<span class=" fa fa-angle-down"></span><br>
											<span style="color: green;">${SiteName}</span>
									</a>
									<ul class="dropdown-menu dropdown-usermenu pull-right">
										<!-- <li><a href="javascript:;"> Profile</a></li>
										<li>
										  <a href="javascript:;">
											<span class="badge bg-red pull-right">50%</span>
											<span>Settings</span>
										  </a>
										</li>
										<li><a href="javascript:;">Help</a></li> -->
										<li><a href="logout.spring"><i class="fa fa-sign-out pull-right"></i> Log Out</a></li>
								  </ul>
								</li>
								<li style="display: none;">
									<!-- <a href="javascript:void(0);" style="margin-top: 12%;"><i class="fa fa-bell-o play" aria-hidden="true"><span class="badge" id="notificationBell">3</span></i><br></a> -->
									<a href="javascript:void(0);" style="margin-top: 12%;"><i class="fa fa-bell-o play" aria-hidden="true"></i><br></a>
								</li>
									
							</ul>
						</nav>
					</div>
				</div>
				<div class="clearfix"></div>
					<!-- <img src="images/spinner.gif" class="spinnercls" id="spinner" style=""></img> -->	
			   <div class="loader-ims" id="loaderId" style="display:none;"> <!--  -->
		         <div class="lds-ims">
			       <div></div><div></div><div></div><div></div><div></div><div></div></div>
		        <div id="loadingimsMessage">Loading...</div>
	           </div>
					
	