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
						<center><font color="red" size="5"><c:out value="${requestScope['message']}"></c:out> </font></center>
					</div>
				</div>
				<div class="clearfix"></div>
						
	