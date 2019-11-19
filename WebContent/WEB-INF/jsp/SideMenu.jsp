<%@page import="java.util.List,com.sumadhura.bean.MenuDetails"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

 <%

		 List<MenuDetails> list  = (List<MenuDetails>)session.getAttribute("menu");
 //System.out.println("in JSP page: "+list.get(0).getMajorHeadId()+","+list.get(1).getMajorHeadId()+","+list.get(2).getMajorHeadId()+","+list.get(3).getMajorHeadId()+",");
 
 %>
 
<div id="sidebar-menu" class="main_menu_side hidden-print main_menu main_menu_scroll" style="margin-top: -16%;">

							<div class="menu_section" >
							
								<ul class="nav side-menu" >
								<%
								for(int head=1;head<=20;head++)
								{	
									
									
								  if(list != null){
									
									for(int i=0;i<list.size();i++)
									{
										
									
										if(Integer.parseInt(list.get(i).getMajorHeadId())== head)
											{
											%>
											<li ><a class='up_down'><i class="<%= list.get(i).getIconName() %>" aria-hidden="true"></i> <%=list.get(i).getMajorHeadName()%> 
												<span class="fa fa-chevron-down fa4"></span>
												<span class="fa fa-chevron-right fa5"></span></a>
												<ul class="nav child_menu">
											
											<% 
												for(int j=0;j<list.size();j++)
												{
													if(Integer.parseInt(list.get(j).getMajorHeadId())== head)
													{
											
								%>
									
											<li><a href="<%=list.get(j).getMappingLink()%>">  <%=list.get(j).getMinorHeadName()%> </a></li>
											
											
										
									<%				}
												}%>
											</ul>
											</li><%
												break;
											}
									}
								  }
								}
									
									%>
								
									
									
									
								</ul>
							</div>
						</div>
						
		