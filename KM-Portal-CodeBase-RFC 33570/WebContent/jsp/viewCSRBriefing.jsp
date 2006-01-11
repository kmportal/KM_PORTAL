<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<link  href="common/css/jquery.jscrollpane.css" rel="stylesheet" type="text/css" media="all" />
<link href="common/css/style.css" type="text/css" rel="stylesheet" />
<link href="common/font/stylesheet.css" rel="stylesheet" type="text/css" />

<%!
public String getSpaces(int numSpaces)
{
  StringBuffer buffer = new StringBuffer(numSpaces);
  for(int i = 0; i < numSpaces; i++)
    buffer.append(" ");
  return buffer.toString();
}
%>
<html:form action="/kmBriefingMstr" >
 <div class="inner-content">
	<div class="two-column-layout1 clearfix"><br>
		<h3 style = "font-family:Arial;">Briefings</h3><br>
		  <logic:present name="BRIEFING_LIST">
		   <logic:iterate name="BRIEFING_LIST" id="briefing" type="com.ibm.km.forms.KmBriefingMstrFormBean">
		   <bean:define name="briefing" property="count" id="count"/>
            <div class="column1 box6 fll">
			    <h1 class="heading1"><bean:write name="briefing" property="briefingHeading"/> </h1>
                	<ul class="list8" style ="padding-top:0px;"> 
						<table> 
							<tr>
								<c:forEach begin="0" end="${count}" var="i" step="1" >
								<br> <img src="./images/arow_icon.jpg" width="3" height="5"><bean:write name="briefing" property='<%="briefingDetails[" + ((Integer)(pageContext.getAttribute("i"))).intValue()+ "]"%>'/>
								</c:forEach>
							</tr>
						</table>
					 </ul>
					<span style="font-size:12px; font-weight:bold;  ">Display Date : <bean:write name="briefing" property="createdDt"/> </span>
					</div>
            </logic:iterate>
          <logic:empty name="BRIEFING_LIST">
          <ul class="list8"> <li>
          <FONT color="red"><B>No Briefing Found for the Day</B></FONT>
          </li></ul>
          </logic:empty>
          
		</logic:present>	
   </div>
 </div>
</html:form>

