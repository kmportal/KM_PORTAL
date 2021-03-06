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
			"attachment;filename=QuizReport.xls");

%>
</head>
<body>
	<table width="75%" class="mTop30" align="center" border="1">
	
		<tr class="text white" >
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">SNO</span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">Correct Answers</span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">Wrong Answers</span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">Skip Questions </span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">Result</span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">UserId </span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">Circle Name</span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">Ud Id </span></th>
			<th style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" ><span class="mLeft5 mTop5">LOB</span></th>

			
		<!--	<th bgcolor="a90000" align="center">&nbsp;</th>
			<th bgcolor="a90000" align="center">&nbsp;</th>  -->
		</tr>
		<logic:present name="quizList" scope="request">		
		<logic:empty  scope="request" name="quizList">
			<TR class="lightBg">
				<TD class="text" align="center" colspan="11">Report Not Found</TD>
			</TR>
		</logic:empty>
		</logic:present>
		<logic:present name="quizList" scope="request">	
		<logic:notEmpty name="quizList" scope="request">
			<logic:iterate id="report" indexId="i" name="quizList"scope="request"  >
				
				<TR class="lightBg">
					<TD class="text" align="center"><%=(i.intValue() + 1)%>.</TD>
					<TD class="txt" align="center"><bean:write name="report"property="correct_answers" /></TD>
					<TD class="txt" align="center"><bean:write name="report"property="wrong_answers" /></TD>
					<TD class="txt" align="center"><bean:write name="report"property="skipQuesions" /></TD>
					<TD class="txt" align="center"><bean:write name="report"property="result" /></TD>
					<TD class="txt" align="center"><bean:write name="report"property="user_id" /></TD>
					<TD class="txt" align="center"><bean:write name="report"property="circle_name" /></TD>
						<TD class="txt" align="center"><bean:write name="report"property="ud_id" /></TD>
					<TD class="txt" align="center"><bean:write name="report"property="lob" /></TD>
					
				</TR>
			</logic:iterate>
		</logic:notEmpty>
	</logic:present>
	</table>
</body>
</html>
