<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >

<%@ page errorPage="ExceptionHandler.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html>
<logic:notEmpty name="ALERT_HISTORY">
<bean:define  id="alertList" name="ALERT_HISTORY" type="java.util.ArrayList" scope="request"/>
</logic:notEmpty>
<HEAD>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition",
			"attachment;filename=AlertHistoryReport.xls");

	
	
%>
</HEAD>
<body>
<table width="100%" class="mTop30" align="center">
      <TR align="center">
	           			<td width="100%" colspan="6" height="28"align="center"><font size="3"><B><bean:message
					key="alertHistory.title" /></B></font></td>
	</TR>
  <tr></tr>
   <tr>
         <td  colspan="10" align="left" width="90%"><FONT size="2"><B>Circle :</B></FONT>&nbsp;&nbsp;<bean:write name="kmAlertMstrFormBean" property="documentPath" /></td>	
         </tr>
    
    <tr></tr>
			
			<tr class="text white">
			
				<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><b>S.NO</b></font></span></td>
				<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><b>Date</b></font></span></td>
				
				<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><b>Time of Alert Creation</b></font></span></td>
				<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Alert Source</span></td>
				<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><b>Content</b></font></span></td>
			</tr>
			<tr></tr>
   	<logic:notEmpty name="ALERT_HISTORY">
		<logic:iterate name="alertList" id="file" indexId="i" type="com.ibm.km.dto.KmAlertMstr">
			<TR class="lightBg">
				<TD class="text" align="left"><%=(i.intValue() + 1)%>.</TD>
				<TD class="text" align="left"><bean:write name="file" property="createdDt" /></TD>
				
				<TD class="text" align="left"><bean:write name="file" property="createdTime" /></TD>
				<TD class="text" align="left"><bean:write name="file" property="alertSource" /></TD>
				<TD class="text" align="left"><bean:write name="file" property="message" /></TD>
               
			</TR>
    	</logic:iterate>
    </logic:notEmpty>	
   <logic:empty name="ALERT_HISTORY">
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
	</logic:empty>
	
</table>
</body>
</html:html>
