
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >

<%@ page errorPage="ExceptionHandler.jsp"%>
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
			"attachment;filename=fileAddedExcelReport.xls");

	
	
%>
</HEAD>
<body>
<table width="100%" class="mTop30" align="center">
      <TR align="center">
	           			<td width="100%" colspan="6" height="28"align="center"><font size="3"><B>FILES ADDED CIRCLE WISE REPORT	</B></font></td>
	</TR>
  <tr></tr>
		<tr>

					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>S.No.</B></font></span>
					</td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Circle Name</B></font>
					</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Document Count</B></font>
					</span></td>
					
				</tr>
<tr></tr>
     
	<logic:equal name="kmFileReportForm" property="reportType"  value="filesAdded">
		

			<logic:notEmpty name="kmFileReportForm" property="filesAddedList">
				<logic:iterate name="kmFileReportForm" id="file" indexId="i" property="filesAddedList">
                   <TR>
					<TD class="text" align="center"><%=(i.intValue() + 1)%>.</TD>
					<TD class="text" align="center"><bean:write name="file" property="elementName" /></TD>
					<TD class="text" align="center"><bean:write name="file" property="documentCount" /></TD>
					</TR>
				</logic:iterate>
			</logic:notEmpty>



			
	</logic:equal>

</table>
</body>
</html:html>
