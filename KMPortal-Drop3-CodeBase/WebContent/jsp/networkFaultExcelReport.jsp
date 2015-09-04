
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
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
<logic:notEmpty name="FAULT_REPORT">
<bean:define id="userList" name="FAULT_REPORT" type="java.util.ArrayList" scope="request" />
</logic:notEmpty>

<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition",
			"attachment;filename=networkFaultExcelReport.xls");

	
	
%>
</HEAD>

<BODY>
      
<table width="100%" class="mTop30" align="center">
<TR align="center">
	           			<td width="100%" colspan="8" height="28"align="center"><font size="3"><B><bean:message
					key="networkFaultReport.title" /> </B></font></td>
	</TR>
  <tr></tr>
  <tr class="lightBg">
		<td  colspan="8" align="left" width="90%"><FONT size="2"><B><bean:message key="networkFaultReport.search.Location"/> :&nbsp;&nbsp;<bean:write name="networkFaultReportForm" property="elementPath" />
		&nbsp;&nbsp;&nbsp;  <bean:write name="networkFaultReportForm" property="reportName" /></B></td>
	</tr>
	<tr></tr>
	
				<tr class="text white">

					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>S.No</B></font></span>
					</td>
				<logic:present name="superAdmin" scope="request">
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Circle </B></font></span></td>
				</logic:present>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Date</B></font></span>
					</td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Time</B></font></span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Problem</B></font></span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Affected Area</B></font></span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>TAT (HH:MM)</B></font></span></td>
					<!--<td class="darkRedBg height19"><span class="mLeft5 mTop5">Last LogIn Time</span></td>-->
					

				</tr><tr></tr>
					
 	 <logic:notEmpty name="FAULT_REPORT">
 	 <logic:iterate name="userList" id="file" indexId="i" type="com.ibm.km.dto.NetworkFaultReportDto">

 
		
                        <TR class="lightBg">
						<TD class="text" align="left"><%=(i.intValue() + 1)%>.</TD>
						
						<logic:present name="superAdmin" scope="request">
						<TD class="text" align="left"><bean:write name="file" property="circleName" /></TD>
						</logic:present>
						<TD class="text" align="left"><bean:write name="file" property="reportedDate" /></TD>
						<TD class="text" align="left"><bean:write name="file" property="reportedTime" /></TD>
						<TD class="text" align="left"><bean:write name="file" property="faultDescription" /></TD>
						<TD class="text" align="left"><bean:write name="file" property="affectedArea" /></TD>
						<TD class="text" align="left">
						<bean:write
						name="file" property="tatHours" />:<bean:write name="file" property="tatMinutes" />&nbsp;
						</TD>
						

						
					
					</TR>
					
			
				</logic:iterate>
		</logic:notEmpty>
					<logic:empty name="FAULT_REPORT">
					<TR align="center">
					<TD height="27" align="center" colspan="8" class="txt"><font
						color="red"><bean:message
					key="networkFaultReport.noRecords" /></font></TD>
			</TR>
			</logic:empty>
			
		</table>
       
</BODY>
</html:html>
