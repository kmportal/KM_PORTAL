<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
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
			"attachment;filename=feedbackExcelReport.xls");
%>
</HEAD>
<body >

<html:form action="/kmFeedbackMstr" >
	<html:hidden property="methodName" /> 
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	
	<table width="95%"  align="center">
		
					
				
		<tr>
                          
		<td colspan="2" align="center" class="error">
		<strong><font color=red> <html:errors /></font></strong>
				</td>
		
        </tr> 
		
		<tr>

	
			<td colspan="9" align="center"><span class="heading"><BR><BR><b>Feedback Report</b><BR><BR></span></td>
		</tr>
		</table>
   <table width="95%" class="mTop30" align="center">
   <logic:notEqual name="kmFeedbackMstrFormBean" property="initStatus" value="true">	
  
         <tr class="lightBg">
		
		<td  colspan="10" align="left"><FONT size="2"><B>Search Location :</B></FONT><bean:write name="kmFeedbackMstrFormBean" property="elementFolderPath"/></td></tr>
		
		<tr class="textwhite">
			
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="28" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><B><bean:message key="feedback.SNO" /></B></span> </td>

				<td
					style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="214" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Date Feedback Given</b> </span></td>
				<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="223" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Date Action Taken</b></span></td>
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="154" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>User ID</b></span> </td>
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="208" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b><bean:message key="feedback.comment" /></b></span> </td>
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="214" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Action Taken(Accepted/Rejected)</b></span> </td>
		<!--	<td bgcolor="a90000" align="center"></td>
			<td bgcolor="a90000" align="center">&nbsp;</td>
			<td bgcolor="a90000" align="center">&nbsp;</td> -->
		</tr>
		<logic:empty name="kmFeedbackMstrFormBean" property="feedbackList">
			
			<TR class="lightBg">
					<TD colspan="2" class="error" align="left" height="15"><FONT
						color="red"><bean:message key="viewAllFiles.NotFound" /></FONT></TD>
				</TR>
			
		</logic:empty>
			
		<logic:notEmpty name="kmFeedbackMstrFormBean" property="feedbackList">
			<logic:iterate name="kmFeedbackMstrFormBean" id="report" indexId="i"
				property="feedbackList">
				<TR class="lightBg">
					
					<TD class="text" align="center" width="3%"><%=(i.intValue() + 1)%>.</TD>
					<TD class="text" align="center" width="21%"><bean:write name="report" property="createdDate" /></TD>
					<TD class="text" align="center" width="22%"><bean:write name="report" 	property="updatedDate" /></TD>
					<TD class="text" align="center" width="15%"><bean:write name="report" 	property="createdBy" />&nbsp;&nbsp;</TD>
					<TD class="text" align="center" width="20%"><bean:write name="report" property="comment" /></TD> 
				   <TD class="text" align="center" width="21%"><bean:write name="report" 	property="readStatus" /></TD> 
					</TR>
			</logic:iterate>  

		
			</logic:notEmpty>
			
			
	</logic:notEqual>		
	</table>
</html:form>
</body>
</html:html>
