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
			"attachment;filename=DocumentHitCountReport.xls");
%>
</HEAD>
<body >

<html:form action="/viewReports" >
	<html:hidden property="methodName" /> 
	
	<table width="95%"  align="center">
		
					
				
		<tr>
                          
		<td colspan="2" align="center" class="error">
		<strong><font color=red> <html:errors /></font></strong>
				</td>
		
        </tr> 
		
		<tr>

	
			<td colspan="9" align="center"><span class="heading"><BR><BR><b>Documents Hits Counts Report</b><BR><BR></span></td>
		</tr>
		</table>
   <table width="95%" class="mTop30" align="center">
   <logic:notEqual name="kmViewReportsFormBean" property="initStatus" value="true">	
  
		
<tr class="textwhite">
			
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="28" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><B><bean:message key="feedback.SNO" /></B></span> </td>

				
				
		<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="154" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Circle</b></span> </td>
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="208" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>LOB</b></span> </td>
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="214" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Document Path</b></span> </td>
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="223" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Document Name</b></span></td>
			<td	style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="214" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Document Id</b> </span></td>				
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="208" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Uploaded Time</b></span> </td>
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="154" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Uploaded Date</b></span> </td>
			<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="214" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Last Update Date</b></span> </td>
		<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="223" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>Uploaded By</b></span></td>
					<td style="background-image:url(images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" width="214" height="49" valign="top" align="center"><span class="mLeft5 mTop5"><b>No. of Hits</b></span> </td>
			
			
		
		
		<!--	<td bgcolor="a90000" align="center"></td>
			<td bgcolor="a90000" align="center">&nbsp;</td>
			<td bgcolor="a90000" align="center">&nbsp;</td> -->
		</tr>
		<logic:empty name="kmViewReportsFormBean" property="reportList">
			
			<TR class="lightBg">
					<TD colspan="2" class="error" align="left" height="15"><FONT
						color="red"><bean:message key="viewAllFiles.NotFound" /></FONT></TD>
				</TR>
			
		</logic:empty>
			
		<logic:notEmpty name="kmViewReportsFormBean" property="reportList">
			<logic:iterate name="kmViewReportsFormBean" id="report" indexId="i"
				property="reportList">
				<TR class="lightBg">
					
					<TD class="text" align="center" width="3%"><%=(i.intValue() + 1)%>.</TD>
						<TD class="text" align="center" width="22%"><bean:write name="report" 	property="circle" /></TD>
						<TD class="text" align="center" width="15%"><bean:write name="report" 	property="lob" />&nbsp;&nbsp;</TD>
						  <TD class="text" align="center" width="21%"><bean:write name="report" 	property="documentPath" /></TD> 
					
					<TD class="text" align="center" width="22%"><bean:write name="report" 	property="documentName" /></TD>
					<TD class="text" align="center" width="21%"><bean:write name="report" property="documentId" /></TD>
					
					<TD class="text" align="center" width="20%"><bean:write name="report" property="uploadedTime" /></TD> 
					<TD class="text" align="center" width="15%"><bean:write name="report" 	property="uploadedDate" />&nbsp;&nbsp;</TD>
						<TD class="text" align="center" width="20%"><bean:write name="report" property="lastUpdateDate" /></TD> 
				 
					<TD class="text" align="center" width="21%"><bean:write name="report" property="uploadedBy" /></TD>
						<TD class="text" align="center" width="21%"><bean:write name="report" property="noofHits" /></TD>
				
					
				

					
					</TR>
			</logic:iterate>  

		
			</logic:notEmpty>
			
			
	</logic:notEqual>		
	</table>
</html:form>
</body>
</html:html>
