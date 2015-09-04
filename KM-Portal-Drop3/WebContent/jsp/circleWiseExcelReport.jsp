<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >

<%@ page errorPage="ExceptionHandler.jsp"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:html>
<logic:notEmpty name="kmUserReportForm"  property="circleWiseReport">
<bean:define id="circleList" name="kmUserReportForm" property="circleWiseReport" type="java.util.ArrayList" />
</logic:notEmpty>
<HEAD>
<%
	response.setContentType("application/vnd.ms-excel");
	response.setHeader("content-Disposition",
			"attachment;filename=circleWiseExcelReport.xls");

	
	
%>
</HEAD>
<body>
<table width="100%" class="mTop30" align="center">
      <TR align="center">
	           			<td width="100%" colspan="6" height="28"align="center"><font size="3"><B><bean:message
					key="userLoginCount.title" />	</B></font></td>
	</TR>
  <tr></tr>
		<tr>

					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>S.No.</B></font></span>
					</td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Circle Name</B></font>
					</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>LOB Name</B></font>
					</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5"><font size="2"><B>Total Users</B></font></span></td>

				</tr>
<tr></tr>
      <logic:notEmpty name="CIRCLE_WISE_REPORT">
						<logic:iterate name="circleList" id="file" indexId="i" type="com.ibm.km.dto.KmUserMstr">

					<TR class="lightBg">

						<TD class="text" align="left"><%=(i.intValue() + 1)%>.</TD>

						<TD class="text" align="left"><bean:write name="file"
							property="circleName" /></TD>
							<TD class="text" align="left"><bean:write name="file"
							property="lobName" /></TD>
						<TD class="text" align="left"><bean:write name="file"
							property="noOfLoggedInUser" /></TD>
						</TR>
				</logic:iterate>
                <tr>	<td></td><td></td><td></td><td align="left">------------------</td> </tr>
				<tr><td></td><td  align="left" ><B>Grand Total</B></td><td></td>
				<td align="left"><font size="2"><B><bean:write name="file"property="total" /></B></font></td></tr>
			</logic:notEmpty>
			
			<tr><td colspan="2" class="error" align="left"><html:messages id="msg"
					message="true">
					<bean:write name="msg" />
				</html:messages></td></tr>
			<logic:empty name="circleList"  >
				<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
			</logic:empty>
		
	
</table>
</body>
</html:html>
