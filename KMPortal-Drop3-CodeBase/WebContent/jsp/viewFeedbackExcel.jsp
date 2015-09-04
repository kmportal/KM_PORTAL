
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >


<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:html>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<TITLE></TITLE>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition",
			"attachment;filename=viewFeedbackExcel.xls");

	
	
%>
</HEAD>
<body>
<table width="100%" class="mTop30" align="center">
      <TR align="center">
	           			<td width="100%" colspan="6" height="28"align="center"><font size="3"><B><bean:message	key="feedback.viewFeedback" /></B></font></td>
	</TR>
  <tr></tr>
   <tr class="lightBg">
		<td  colspan="10" align="left" width="90%"><FONT size="2"><B>Search Location :</B></FONT><bean:write name="kmFeedbackMstrFormBean" property="elementFolderPath"/></td></tr>
		<tr>

					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B><bean:message key="feedback.SNO" /></B></font></span>
					</td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B><bean:message key="feedback.comment" /></B></font>
					</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Feedback On</B></font>
					</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B><bean:message key="feedback.createdBy" /></B></font>
					</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B><bean:message key="feedback.createdDt" /></B></font>
					</span></td>
					<!--<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Feedback Response</B></font>
					</span></td>-->
					
					
				</tr>
<tr></tr>
     
	<logic:notEqual name="kmFeedbackMstrFormBean" property="initStatus" value="true">
			<logic:empty name="kmFeedbackMstrFormBean" property="feedbackList">
						
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
			
		</logic:empty>
			
		<logic:notEmpty name="kmFeedbackMstrFormBean" property="feedbackList">
			<logic:iterate name="kmFeedbackMstrFormBean" id="report" indexId="i"
				property="feedbackList">
				<TR class="lightBg">
					
					<TD class="text" align="center" width="5%"><%=(i.intValue() + 1)%>.</TD>
					<TD class="text" align="center"  width="25%" ><bean:write name="report"	property="comment" /></TD>
					<TD class="text" align="left" width="45%" ><bean:write name="report" property="feedbackStringPath" /></TD>
					<TD class="text" align="center" width="10%" ><bean:write name="report" property="createdBy" /></TD>
					<TD class="text" align="center" width="15%" ><bean:write name="report" property="createdDate" />&nbsp;&nbsp;</TD>
					<!--<TD class="text" align="center"  width="25%" > <bean:write name="kmFeedbackMstrFormBean" property="feedbackResp"/></TD> -->
				    				
				
</TR>
			</logic:iterate>  

		
			</logic:notEmpty>
   </logic:notEqual>
</table>
</body>
</html:html>
