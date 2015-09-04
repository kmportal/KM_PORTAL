
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="theme/text.css" rel="stylesheet" type="text/css">

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
	</script>
<script language="JavaScript" type="text/javascript">
function ValidateCircleMstr() 
{ 
	var searchChars="`~!$^&*()=+><{}[]+|=?':;\\\"-&-,@";


	if(document.forms(0).circleName.value=="")
	{
		alert("Please enter Circle Name");	
		document.forms(0).circleName.focus();	
		return false;		
	}		


	if(isEmpty(document.kmCircleMstrFormBean.circleName))
	{
			alert("Please enter Circle Name");
			document.kmCircleMstrFormBean.circleName.focus();
			document.kmCircleMstrFormBean.circleName.value="";
			return false;
	}

   
 
	return true;
 }
 function clearfields()
 {
 	
 	document.forms[0].circleName.value="";
 	document.forms[0].focus();
 	return false;
 }


</script>

<html:form action="/kmCircleMstr">
	<html:hidden property="methodName" value="insert" />
	<html:hidden property="hidCircleId" name="kmCircleMstrFormBean" />
	
	<table width="55%" class="mTop30" align="center" cellspacing="0"
		cellpadding="0">
			<tr align="center">
				<td colspan="5" class="lightBg"><span class="heading"><bean:message
					key="circleMaster.title" /></span></td>
			</tr>
			<tr>
				<td colspan="2" align="center" class="error">
					<strong> 
          			<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
                          
             		</html:messages>
            		</strong>
            	</td>
			</tr>
			
			<tr class="lightBg">
				<td align="right" class="pRight5"><bean:message
					key="circleMaster.CircleName" />&nbsp;:</td>
				<td><span class="width250"> <span class="pTop10 pBot2"><span
					class="width250 pTop5"><span class="width250 pleft5"><html:text
					styleClass="width100" property="circleName"
					name="kmCircleMstrFormBean" maxlength="32" /></span></span></span> </span></td>
			</tr>
	


	

			<tr class="lightBg">
				<td class="width150 pLeft10 pTop2 pBot5">&nbsp;</td>
				<td colspan="2" class="wid250 pTop2 pBot5"><span class="width250"><BR><input
					type="image" src="images/submit_button.jpg" onclick="return ValidateCircleMstr();" />&nbsp;&nbsp;&nbsp;&nbsp;<input 
				type="image" src="images/reset_button.jpg" class="btnActive" onclick="return clearfields();"/> </span></td>
			</tr>
	
	
	</table>
	<table width="75%" class="mTop30" align="center">
		<tr align="center">
			<td colspan="6"><span class="heading"><bean:message
				key="circleMasterReport.title" /></span></td>
		</tr>

		<tr class="text white">
			<td bgcolor="a90000"  align="center"><font color="white"><bean:message
				key="circleMaster.sno" /></font></TD>
			<td bgcolor="a90000" align="center"><font color="white"><bean:message
				key="circleMaster.CirName" /></font></TD>
			<td bgcolor="a90000" align="center"><font color="white"><bean:message
				key="circleMaster.CirID" /></font></TD>
			<td bgcolor="a90000" align="center"><font color="white"><bean:message
				key="circleMaster.crtdt" /></font></TD>
			<td bgcolor="a90000" align="center"><font color="white"><bean:message
				key="circleMaster.status" /></font></TD>

		</TR>
		<logic:notEmpty property="circleReportList" name="kmCircleMstrFormBean">
			<logic:iterate name="kmCircleMstrFormBean" id="report" indexId="i"
				property="circleReportList">
				<TR class="lightBg">
					<TD class="pRight5" align="center"><%=(i.intValue() + 1)%>.</TD>
					<TD class="txt" align="center">
					<A><bean:write name="report" property="circleName" /></A></TD>
					<TD class="txt" align="center"><bean:write name="report"
						property="circleId" /></TD>
					<TD class="txt" align="center"><bean:write name="report"
						property="createdDt" /></TD>
					<TD class="txt" align="center"><bean:write name="report"
						property="status" /></TD>

				</TR>

			</logic:iterate>
		</logic:notEmpty>
		<logic:empty property="circleReportList" name="kmCircleMstrFormBean">
			<TR align="center">
				<TD height="27" align="center" colspan="13" class="txt"><font
					color="red"><bean:message key="record.norecords" /></font></TD>
			</TR>
		</logic:empty>
	</table>
</html:form>

