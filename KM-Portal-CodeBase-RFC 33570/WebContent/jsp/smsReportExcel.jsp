<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta name="GENERATOR" content="Rational Software Architect">

<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition",
			"attachment;filename=SMSReport.xls");

%>
</head>
<body>
	<table width="75%" class="mTop30" align="center" border="1">
	
		<tr class="text white" >
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">SNO</span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">OLM ID</span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">SMS Sender</span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">UD ID</span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">Mobile Number</span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">SMS Content </span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">SMS Category </span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">SMS Create Date </span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">Circle Name </span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">Partner Name </span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">Location </span></th>
						
		</tr>
		<logic:present name="smsReportList" scope="request">		
		<logic:empty  scope="request" name="smsReportList">
			<TR class="lightBg">
				<TD class="text" align="center" colspan="11">Report Not Found</TD>
			</TR>
		</logic:empty>
		</logic:present>
		<logic:present name="smsReportList" scope="request">	
		<logic:notEmpty name="smsReportList" scope="request">
			<logic:iterate id="report" indexId="i" name="smsReportList"scope="request"  >
				
				<TR class="lightBg">
					<TD class="text" align="center"><%=(i.intValue() + 1)%>.</TD>
					<TD class="txt" align="center" ><bean:write name="report" property="olmId"  /></TD>
					<TD class="txt" align="center"><bean:write name="report" property="smsSender" /></TD>
					<TD class="txt" align="center" ><bean:write name="report" property="udId"  /></TD>
					<TD class="txt" align="center"><bean:write name="report" property="mobileNo" /></TD>
					<TD class="txt" align="center"><bean:write name="report" property="smsContent" /></TD>
					<TD class="txt" align="center"><bean:write name="report" property="smsCategory"  /></TD>
					<TD class="txt" align="center"><bean:write name="report" property="smsCreatedDate" /></TD>
					<TD class="txt" align="center"><bean:write name="report" property="circleName" /></TD>
					<TD class="txt" align="center"><bean:write name="report" property="patnerName" /></TD>
					<TD class="txt" align="center"><bean:write name="report" property="location" /></TD>
					
				</TR>
			</logic:iterate>
		</logic:notEmpty>
	</logic:present>
	</table>
</body>
</html>
