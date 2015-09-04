
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >

<%@ page errorPage="ExceptionHandler.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html>
<logic:notEmpty name="AGENT_LIST">
<bean:define  id="agentList" name="AGENT_LIST" type="java.util.ArrayList" scope="request"/>
</logic:notEmpty>
<HEAD>

<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition",
			"attachment;filename=agentWiseExcelReport.xls");

	
	
%>
</HEAD>
<body>
<table width="100%" class="mTop30" align="center">
      <TR align="center">
	           			<td width="100%" colspan="6" height="28"align="center"><font size="3"><B><bean:message
					key="agentIdWise.title" /></B></font></td>
	</TR>
	<tr></tr>
	 <tr class="lightBg">
		<td  colspan="10" align="left" width="90%"><FONT size="2"><B>Search Location :</B></FONT>&nbsp;&nbsp;<bean:write name="kmUserReportForm" property="documentPath" />
		&nbsp;&nbsp;&nbsp;<FONT size="2"><B>Partner:</B></FONT>&nbsp;&nbsp;<bean:write name="kmUserReportForm" property="partnerName"/></td>
	</tr>	
  <tr></tr>
		<tr>

					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>S.No.</B></font></span>
					</td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>User LoginId</B></font>
					</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>First Name</B></font>
					</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Last Name</B></font></span></td>

				</tr>
<tr></tr>
      <logic:notEmpty name="AGENT_LIST">
						<logic:iterate name="agentList" id="file" indexId="i" type="com.ibm.km.dto.KmUserMstr">

					<TR class="lightBg">

						<TD class="text" align="left"><%=(i.intValue() + 1)%>.</TD>

						<TD class="text" align="left"><bean:write name="file"
							property="userId" /></TD>
						<TD class="text" align="left"><bean:write name="file"
							property="firstName" /></TD>
						<TD class="text" align="left"><bean:write name="file"
							property="lastName" /></TD>
					
					</TR>
				</logic:iterate>
                </logic:notEmpty>
					<logic:empty name="AGENT_LIST">
					<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
			</logic:empty>
			
		
	
</table>
</body>
</html:html>
