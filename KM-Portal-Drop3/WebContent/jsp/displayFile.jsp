<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!-- 

Code 	Updated by     	  Date		  CSR NO				 Defect ID		    Description
---------------------------------------------------------------------------------------------------------------
0001	Neeraj Aggarwal   6 MAY 09	  20081211-00-03016	     MASDB00111526  	Adding confirm Box on Abort operation

-->
<html:html>
<HEAD>
<base target="_self">
<LINK href="./theme/text.css" rel="stylesheet" type="text/css">
<TITLE>Process Excel</TITLE>
<script>
/*
**	To return the status at the time of closing of window
*/
function terminate()
{
	window.returnValue=document.forms[0].status.value;
}
/*
**	Confirm Box for aborting the operation
*/
function confirmAbort()
{
	//0001  asking user to confirm before aborting and close window if he aborts
	if(window.confirm("Are you Sure ?"))
	{
		window.close();
	}
	else
	{
		return false;
	}
}

/*
**	Call Action Method to save data to master tables
*/
function startSavingData()
{
	kmExcelUploadFormBean.action="kmExcelAction.do?methodName=saveData";
	kmExcelUploadFormBean.submit();
}
/*
**	Change status on getting an error
*/
function doCloseOnError()
{
	document.forms[0].status.value="invalidSheet";
	window.close();
}

/*
**	Call Action Method to save data from excel to temporary tables
*/
function startExcelProcessing()
{
	if(document.forms[0].onloadCheck.value==1)
	{
		kmExcelUploadFormBean.action="kmExcelAction.do?methodName=processExcel";
		kmExcelUploadFormBean.submit();
	}	
	if(document.forms[0].onloadCheck.value==3)
	{
		document.forms[0].status.value="complete";
		window.close();
	}
}

</script>
</HEAD>
<BODY onload="javascript: return startExcelProcessing();" onunload="javascript: return terminate();">

	<html:form action="/kmExcelAction" enctype="multipart/form-data" >
	<logic:equal value="initExcelProcessPopUp" name="kmExcelUploadFormBean" property="methodName">
		<input type="hidden" name="onloadCheck" value="1"/>
	</logic:equal>
	<logic:equal value="saveData" name="kmExcelUploadFormBean" property="methodName">
		<input type="hidden" name="onloadCheck" value="3"/>
	</logic:equal>
	<logic:notEqual value="initExcelProcessPopUp" name="kmExcelUploadFormBean" property="methodName">
		<logic:notEqual value="saveData" name="kmExcelUploadFormBean" property="methodName">
			<input type="hidden" name="onloadCheck" value="2"/>
		</logic:notEqual> 	
	</logic:notEqual> 
	<input type="hidden" name="status"/>
	<TABLE border="0" align="center" width="40%">
			<tr>
				<td colspan="2"><br><br></td>
			</tr>
			<tr align="center">
				<td colspan="2"  class="error"><strong>
					<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
                	</html:messages>
            	</strong></td>
		   	</tr>
			<TR align="center">
				<TD  colspan="2" nowrap="nowrap">
					<font color="RED" ><strong>
						<html:errors/>
	 				</strong></font>
				<TD >
			</TR>
		   </table>
		<logic:equal value="0" name="kmExcelUploadFormBean" property="detailStatus">
			<TABLE border="0" align="center" width="40%">
				<TR>
						<TD align="center"><span style="color: red">PLEASE WAIT WHILE THE FILE IS BEING VALIDATED</span></TD>
				</TR>
				<TR>
					<TD><img src="./images/loading_wh.gif"></TD>
				</TR>
			</TABLE>
		</logic:equal>	
		<logic:equal value="1" name="kmExcelUploadFormBean" property="detailStatus">
			<TABLE border="0" align="center" width="80%">	
				<TR>
					<TD>Proceed with saving the data ?</TD>
					<TD>
						<input type="image" src="images/button_proceed.jpg" onclick="javascript:return startSavingData();">
					</TD>
					<TD>
						<input type="image" src="images/button_abort.jpg" onclick="javascript:return confirmAbort();">
					</TD>
				</TR>
			</TABLE>
			<BR>
			<logic:notEmpty name="kmExcelUploadFormBean" property="missingPlanRateData">
				<TABLE align="center" width="80%" >
					<tr class="lightBg">
						<td colspan="2">
								<span style="color: red">The Details for the following Bill Plans are not available </span>
						</td>
					</tr>
				</TABLE>
				<TABLE border="1" bordercolor="#C3C3C3"  align="center" width="80%" >
					<tr class="text white">
						<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
						    key="viewBillPlan.SNO" /></span></td>
						<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
							key="viewBillPlan.BillPlan" /></span></td>
					<logic:iterate name="kmExcelUploadFormBean" id="report" indexId="j"	property="missingPlanRateData">	
						<TR class="lightBg">
							<TD class="text" align="center"><%=(j.intValue() + 1)%>.</TD>
							<TD class="text" align="center"><bean:write name="report" property="billPlanName" /></TD>
						</TR>
					</logic:iterate>
					</TABLE>
				</logic:notEmpty>
			<BR>
			<logic:notEmpty name="kmExcelUploadFormBean" property="duplicateBillPlanRates">
				<TABLE align="center" width="80%" >
					<tr class="lightBg">
						<td colspan="2">
								<span style="color: red">Duplicate Entries for the following bill plans are found in BillPlan Sheet</span>
						</td>
					</tr>
				</TABLE>
				<TABLE border="1" bordercolor="#C3C3C3"  align="center" width="80%" >
					<tr class="text white">
						<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
						    key="viewBillPlan.SNO" /></span></td>
						<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
							key="viewBillPlan.BillPlan" /></span></td>
					<logic:iterate name="kmExcelUploadFormBean" id="report" indexId="j"	property="duplicateBillPlanRates">	
						<TR class="lightBg">
							<TD class="text" align="center"><%=(j.intValue() + 1)%>.</TD>
							<TD class="text" align="center"><bean:write name="report" property="billPlanName" /></TD>
						</TR>
					</logic:iterate>
					</TABLE>
				</logic:notEmpty>
				
				<BR>
			<logic:notEmpty name="kmExcelUploadFormBean" property="duplicateBillPlanRatesPackagId">
				<TABLE align="center" width="80%" >
					<tr class="lightBg">
						<td colspan="2">
								<span style="color: red">Duplicate Entries for the following Package IDs are found in BillPlan Sheet</span>
						</td>
					</tr>
				</TABLE>
				<TABLE border="1" bordercolor="#C3C3C3"  align="center" width="80%" >
					<tr class="text white">
						<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
						    key="viewBillPlan.SNO" /></span></td>
						<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
							key="viewBillPlan.BillPlan" /></span></td>
					<logic:iterate name="kmExcelUploadFormBean" id="report" indexId="j"	property="duplicateBillPlanRatesPackagId">	
						<TR class="lightBg">
							<TD class="text" align="center"><%=(j.intValue() + 1)%>.</TD>
							<TD class="text" align="center"><bean:write name="report" property="billPlanPackageIdDuplicate" /></TD>
						</TR>
					</logic:iterate>
					</TABLE>
				</logic:notEmpty>
				
			<BR><BR>
			<TABLE border="0" align="center" width="80%">
				<tr>
					<td colspan="2" align="center">
						<input type="image" src="images/button_close.jpg" onclick="javascript:window.close();">
					</td>
				</tr>
			</TABLE>
		</logic:equal>		
		<logic:equal value="2" name="kmExcelUploadFormBean" property="detailStatus">
			<TABLE border="0" align="center" width="80%">
				<tr>
					<td colspan="2" align="center">
						<input type="image" src="images/button_close.jpg" onclick="javascript:return doCloseOnError();">
					</td>
				</tr>
			</TABLE>
		</logic:equal>
	</html:form>
</BODY>
</html:html>
