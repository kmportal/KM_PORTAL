

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
<logic:notEmpty name="HIT_COUNT_LIST">
<bean:define  id="hitCountList" name="HIT_COUNT_LIST" type="java.util.ArrayList" scope="request"/>
</logic:notEmpty>

<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition",
			"attachment;filename=hitCountExcelReport.xls");

	
	
%>
</HEAD>

<BODY>
<table width="100%" class="mTop30" align="center">
             <TR align="center">
	           			<td width="100%" colspan="6" height="28"align="center"><font size="3"><B>Document Hits Count Report</B></font></td>
	</TR>
  <tr></tr>     
                
			<tr class="textwhite">
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">SNo.</span> </td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Document Name</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Document Path</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">No. of Hits</span></td>
		</tr>
		
		<logic:notEmpty name="HIT_COUNT_LIST">
		<logic:iterate name="hitCountList" id="hitReportList" indexId="i" type="com.ibm.km.dto.KmHitCountReportDto">
			<TR class="lightBg">
			<TD  class="text" align="left"><%=(i.intValue() + 1)%>.</TD> 
			<TD  class="text" align="left"><bean:write name="hitReportList" property="documentName" /></TD>
			<TD class="text" align="left"><bean:write  name="hitReportList" property="documentPath" /></TD>
			<TD  class="text" align="center"><bean:write name="hitReportList" property="noHits" /></TD>
			
			</TR>
			</logic:iterate>
		</logic:notEmpty>
		 <logic:empty name="HIT_COUNT_LIST">
			<tr>
					<td>No Record Found</td>
			<tr>
			</logic:empty>
		
		</table>
		
		</body>
</html:html>