

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
<logic:notEmpty name="FILE_COUNT_DTO">
<bean:define id="fileCount" name="FILE_COUNT_DTO" type="com.ibm.km.dto.FileReportDto" scope="request"/>
</logic:notEmpty>

<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition",
			"attachment;filename=fileCountExcelReport.xls");

	
	
%>
</HEAD>

<BODY>
<logic:notEmpty name="fileCount" >
<table width="100%" class="mTop30" align="center">
<tr align="center">
	           			<td width="100%" colspan="6" height="28"align="center"><font size="3"><B>FILE COUNT REPORT	</B></font></td>
	</tr><tr></tr>
  <tr class="lightBg">
		<td  align="left" width="90%"><FONT size="2"><B>File Count for : &nbsp;&nbsp;<bean:write name="fileCount" property="elementPath" /></B></FONT> 
		</td>
	</tr><tr></tr>
				
	<tr class="lightBg">
		  <td  align="left" width="220"><font size="2"><b> Approved Files</b></font> 
		  <td class="text" align="center">
		  <bean:write name="fileCount" property="approvedFileCount" />
			</td>
	</tr>
	<tr class="lightBg">
		  <td  align="left" width="220"><font size="2"><b> Rejected Files</b></font> 
		  <td class="text" align="center">
		  <bean:write name="fileCount" property="rejectedFileCount" />
			</td>
	</tr>
		<tr class="lightBg">
		  <td  align="left" width="220"> <font size="2"><b>Pending Files</b></font> 
		  <td class="text" align="center">
		  <bean:write name="fileCount" property="pendingFileCount" />
			</td>
	</tr>
		<tr class="lightBg">
		  <td  align="left" width="220"> <font size="2"><b>OldFiles Files</b></font>
		  <td class="text" align="center">
		  <bean:write name="fileCount" property="oldFileCount" />
			</td>
	</tr>
	<tr class="lightBg">
		  <td  align="left" width="220"> <font size="2"><b>Deleted Files</b></font> 
		  <td class="text" align="center">
		  <bean:write name="fileCount" property="deletedFileCount" />
			</td>
	</tr>
	<tr><td>---------------------</td><td align="center">----------------------------</td></tr>
	<tr>
	<td ><B>Total Files</B></td>
	<td align="center"><B><bean:write name="fileCount" property="total" /></B></td>
	</tr>
	</table>
	</logic:notEmpty>
			
		
       
</BODY>
</html:html>
