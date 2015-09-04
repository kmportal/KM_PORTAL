<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="java.io.PrintWriter"%>
<%@ page language="java" import="java.sql.Blob,java.io.OutputStream;"  %>
<script type="text/javascript" src="common/js/jquery-1.8.2.min.js"></script>
<script type="text/javascript" src="common/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="common/js/jquery.jscrollpane.min.js"></script>
<script type="text/javascript">
			$(function()
			{
				$('.scroll-pane').jScrollPane(

				);
			});
		</script>
<html:form action="/employeeAppreciation" >
    <html:hidden property="methodName" value="insert"/>
	<html:hidden property="createdBy" styleId="createdBy"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<html:hidden property="token" styleId="token"/>
	
     <div class="box2" style="line-height: 1;">
	   <FONT color="red"><html:errors/></FONT>
        <div class="content-upload clearfix">
          <h1><span class="text">Employee Appreciation</span></h1>
          
           <div id="scroll1">
            <div class="scroll-pane">
             <ul class="list5">
			  <logic:notEmpty name="EMP_APPRECIATION_LIST">
			  <bean:define id="appreciationList" name="EMP_APPRECIATION_LIST" type="java.util.ArrayList" scope="request" />
			  </logic:notEmpty>
			
			  <logic:notEmpty name="appreciationList" >
				<logic:iterate name="appreciationList" id="report" indexId="i" type="com.ibm.km.dto.EmployeeAppreciationDTO">
				<%
					if (i % 2 == 0) {
				%>
				<li class="clearfix">
						<div class="fll main-img">
							<div class="inner box4"><img src="<%=report.getEmpImageName()%>"
							alt="Employee Image" height="80px" width="84px" /></div>
						<h6><bean:write name="report"	property="employeeName" /></h6>
						</div>
					<div class="box4 fll description"><span class="arrow1" >&nbsp;</span>
						<h4><bean:write name="report"	property="appreciationHeader" /></h4>
						<p> <%=(report.getAppreciationContent().replaceAll("&gt;",">")).replaceAll("&lt;","<") %></p>
					</div>
				</li>

				<%
					} else {
				%>
	                <li class="clearfix alt">
                          <div class="box4 fll description"> <span class="arrow2">&nbsp;</span>
                    <h4><bean:write name="report"	property="appreciationHeader" /></h4>
                    <p><%=(report.getAppreciationContent().replaceAll("&gt;",">")).replaceAll("&lt;","<") %></p>
                  </div>
                          <div class=" fll main-img ">
                    <div class="inner box4"><img src="<%=report.getEmpImageName()%>" alt="Employee Image" height="80px" width="84px"/></div>
                    <h6><bean:write name="report"	property="employeeName" /></h6>
                  </div>
                        </li>
				 <%
				 	}
				 %>
			</logic:iterate>
		</logic:notEmpty>
      </ul>
    </div>
   </div>
   
  </div>
 </div>



</html:form>


