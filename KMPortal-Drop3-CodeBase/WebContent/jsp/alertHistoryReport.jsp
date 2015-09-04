
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<LINK href="theme/css.css" rel="stylesheet" type="text/css">

<logic:notEmpty name="ALERT_HISTORY">
<bean:define  id="alertList" name="ALERT_HISTORY" type="java.util.ArrayList" scope="request"/>
</logic:notEmpty>


<SCRIPT>
function importExcel()
{
document.kmAlertMstrFormBean.methodName.value="importExcelReport";
document.kmAlertMstrFormBean.submit();
}
function listFiles()
{
var circleId=document.kmAlertMstrFormBean.selectedCircleId.value;
if(circleId=="")
{
alert("Please Select the Circle");
}
else{
document.kmAlertMstrFormBean.methodName.value="alertReport";
document.kmAlertMstrFormBean.submit();
}
}
</SCRIPT>


<html:form action="/kmAlertMstr" >
<html:hidden name="kmAlertMstrFormBean" property="methodName" /> 
<html:hidden name="kmAlertMstrFormBean" property="kmActorId" />	

	<div class="box2">
     <div class="content-upload">
	<h1><bean:message key="alertHistory.title" /></h1>
	<table width="95%" class="mTop30" align="center">
		
          <logic:equal name="kmAlertMstrFormBean" property="kmActorId" value="5">
            <tr align="center">
				<td colspan="2" width="556">
				<table id="table_0" width="454">
					
					<tbody>
					<tr  align="center" class="lightBg" >
					<td  align="right" class="text" width="283"><BR><bean:message
					key="alertHistory.circle" /> <FONT color="red"> *</FONT>&nbsp;&nbsp;</TD>
				    <TD  colspan="2" align="left" width="165"><BR>&nbsp;
					<html:select property="selectedCircleId" name="kmAlertMstrFormBean" >
					<html:option value="">-Select Circle-</html:option>
					<logic:notEmpty name="kmAlertMstrFormBean" property="circleList">
					<bean:define id="circle" name="kmAlertMstrFormBean"	property="circleList" /> 
					<html:options labelProperty="elementName" property="elementId" collection="circle" />
					</logic:notEmpty>
					</html:select>
				 	</TD>  
		</tr>
														
					</tbody>
				</table>
				</td>
			</tr>
			
			
				  
			
			<tr align="left" >
							<td class="pLeft10 pTop2" align="center"><img src="images/submit_button.jpg"
					onclick="listFiles();"></td>
							<td class="pTop2" colspan="2"></td>
					</tr>
					
					<tr>
						<td colspan="2" class="pTop4 pLeft10" width="556">
								<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
									<tr align="left">
										<td class=""><font color="#0B8E7C"><strong>
										<html:messages id="msg" message="true">
               								<bean:write name="msg"/>  
             							</html:messages></strong></font></td>
									</tr>
								</table>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="pTop4 pLeft10" width="556">
							<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
							<tr align="left">
							<td class=""><font color="#0B8E7C"><strong>
							<html:errors/></strong></font>
						</tr>
				</table>
			</td>
		</tr>
		</logic:equal>
		
	</table>	
	
	
<table width="100%" class="mTop30" align="center">

	<logic:present name="ALERT_HISTORY" >
	<tr align="right" >
							<td width="147" class="pLeft10 pTop2"></td>
							<td width="636" class="pTop2" colspan="5"><img  src="images/excel.gif" width="59" height="35" border="0" onclick="importExcel();" /></td>
         </tr>	
         <tr>
         <td  colspan="10" align="left" width="90%"><FONT size="2"><B>Circle  :</B></FONT>&nbsp;&nbsp;<bean:write name="kmAlertMstrFormBean" property="documentPath" /></td>	
         </tr>
				<tr class="text white">
				<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">S.No</span></td>
				<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Date</span></td>
			
				<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Time of Alert Creation</span></td>
				<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Alert Source</span></td>
				<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Content</span></td>
				</tr>
              	<logic:notEmpty name="ALERT_HISTORY">
				<logic:iterate name="alertList" id="file" indexId="i" type="com.ibm.km.dto.KmAlertMstr">
				
				
				<TR class="lightBg">
				<TD class="text" align="left"><%=(i.intValue() + 1)%>.</TD>
				<TD class="text" align="left"><bean:write name="file" property="createdDt" /></TD>

				<TD class="text" align="left"><bean:write name="file" property="createdTime" /></TD>
				<TD class="text" align="left"><bean:write name="file" property="alertSource" /></TD>
                <TD align="left"><html:textarea  disabled="true"  rows="3" cols="30" name="file" property="message"/></td>
				</TR>
				</logic:iterate>
				</logic:notEmpty>
		     <logic:empty name="ALERT_HISTORY"  >
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
			</logic:empty>
	</logic:present>	
</table>
</div></div>

</html:form>
