<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-nested.tld" prefix="nested" %>
<%@page import="com.ibm.km.dto.KmDynamicIndexedPageDto"%>

<script language="javascript">
var path = '<%=request.getContextPath()%>';
//var result = "";
</script>
<div class="box2">
<div class="content-upload clearfix">

<html:form action="/kmDynamicIndexPage">
<html:hidden property="methodName" />
<div id="scroll1">
<div class="scroll-pane">

	<ul class="list9" style="display:block;">
			<nested:iterate id="KmDynamicIndexPageFormBean" name="kmDynamicIndexPageFormBean" property="categories" scope="session" indexId="ctr">
				<!--<li style="display:block;">
					--><logic:notEqual name="KmDynamicIndexPageFormBean" property="elementLevelId" value="0">
						<h4 style="display:block;background:#f00000; height:20px; line-height:20px; padding-left:10px; font-size:12px; color:#fff; margin-bottom:2px;"><bean:write name="KmDynamicIndexPageFormBean" property="categoryName"/></h4>
					</logic:notEqual>
					<logic:equal name="KmDynamicIndexPageFormBean" property="elementLevelId" value="0">
						<h4 style="display:block;background:#f00000; height:20px; line-height:20px; padding-left:10px; font-size:12px; color:#fff; margin-bottom:2px;"><a href="<nested:write name="KmDynamicIndexPageFormBean" property="documentViewUrl"/>"><bean:write name="KmDynamicIndexPageFormBean" property="categoryName"/></a></h4>
					</logic:equal>
											
				<%int outsideLoopVar = 0; %>

				<nested:iterate id="kmDynamicIndexedPageDto" name="KmDynamicIndexPageFormBean" property="categories" scope="page" indexId="ctr">
					 <bean:define id="remainder" value="<%=Integer.toString(ctr % 2) %>"/>
					 <bean:define id="lastIteration" value="<%=Integer.toString(ctr+1) %>"/>
					 <bean:size id="size" name="KmDynamicIndexPageFormBean" property="categories"/>
					 
					 
					<%if ((ctr+1)%4 == 1 ){ %>
					    <ul class="list10 clearfix" style="display:list-item;">
					<%} %>
					<logic:notEqual name="size" value='<%=(ctr+1)+"" %>'>
						<logic:equal name="remainder" value="1">
							<li><span style="display:list-item;">
						</logic:equal>
						<logic:equal name="remainder" value="0">
							<li class="alt"><span style="display:list-item;">
						</logic:equal>
					</logic:notEqual>
					<logic:equal name="size" value='<%=(ctr+1)+"" %>'>
						<li class="last alt"><span style="display:list-item;">
					</logic:equal>
					<%
					String temp = "";
					temp =  ((KmDynamicIndexedPageDto) kmDynamicIndexedPageDto).getCategoryName();
					if(temp != null && temp.length() > 18)
						temp = temp.substring(0,17)+"...";
					 %>
					
					<logic:notEqual name="kmDynamicIndexedPageDto" property="elementLevelId" value="0">
						<a href="kmDynamicIndexPage.do?methodName=loadCategories&firstiteration=true&categoryId=<bean:write name="kmDynamicIndexedPageDto" property="categoryId"/>" title="<bean:write name="kmDynamicIndexedPageDto" property="categoryName"/>"><%=temp %></a></span>
						 </li> 
					</logic:notEqual>
					<logic:equal name="kmDynamicIndexedPageDto" property="elementLevelId" value="0">
						<a href="<bean:write name="kmDynamicIndexedPageDto" property="documentViewUrl"/>" title='<bean:write name="kmDynamicIndexedPageDto" property="categoryName"/>'"><%=temp %></a></span> </li> 
					</logic:equal>
					
					<% outsideLoopVar = ctr;
					if((ctr+1)%4 == 0){ %>
					 <!-- </tr> --> </ul>
					<%} %>
					
				</nested:iterate>
				<!--  </ul>-->
				<%if(outsideLoopVar != 0 || (outsideLoopVar+1) ==1){ %>
					<!-- </tr> --></ul>
				<%} %>
				<!-- </li> -->
				</nested:iterate>
				</ul>
	</div>
	</div>
</html:form>
</div>
</div>