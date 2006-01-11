<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="javascript">
function submitForm(srNo)
{
	document.getElementById("kmDocumentMstrFormMainUpdate").srNo.value = srNo; 
	document.getElementById("kmDocumentMstrFormMainUpdate").methodName.value = "updateDetails"; 
	document.getElementById("kmDocumentMstrFormMainUpdate").submit();
	return true;
}

</script>

<form name="kmDocumentMstrFormMainUpdate" id="kmDocumentMstrFormMainUpdate" method="post" action="/KM/documentAction.do" >
<bean:define id="keyword" name="kmDocumentMstrForm" property="keyword" />
<bean:define id="mainOption" name="kmDocumentMstrForm" property="mainOptionValue" />
<bean:define id="subOption" name="kmDocumentMstrForm" property="selectedSubOptionValue" />
<input type="hidden" name="srNo" value="">
<input type="hidden" name="methodName" value="updateDetails">
   <div class="boxt2">
    <div class="content-upload clearfix">
     <h1 class="clearfix" style="margin-bottom:0px;"><span class="text">Edit Result</span> </h1>
     <div class="content-table-type2 clearfix">
     <logic:notEqual  name="kmDocumentMstrForm" property="message" value="">
<table width="100%" border="1">
		<TR class="lightBg">
<logic:equal  name="kmDocumentMstrForm" property="message" value="success">
<TD colspan="3" class="error" align="center"><FONT color="red"><bean:message key="SUCCESSMSG" /></FONT></TD>
</logic:equal>
<logic:equal  name="kmDocumentMstrForm" property="message" value="fail">
<TD colspan="3" class="error" align="center"><FONT color="red"><bean:message key="FAILMSG" /></FONT></TD>
</logic:equal>
<logic:equal  name="kmDocumentMstrForm" property="message" value="deletionSuccess">
<TD colspan="3" class="error" align="center"><FONT color="red"><bean:message key="DELETESUCCESSMSG" /></FONT></TD>
</logic:equal>
<logic:equal  name="kmDocumentMstrForm" property="message" value="deletionFail">
<TD colspan="3" class="error" align="center"><FONT color="red"><bean:message key="DELETEFAILMSG" /></FONT></TD>
</logic:equal>
		</TR>
	</table>
</logic:notEqual>
     <logic:equal name="kmDocumentMstrForm" property="mainOptionValue" value="arc" ><!-- Start of ARC -->
		<table width="100%" >
		<logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
			<logic:iterate name="kmDocumentMstrForm" type="com.ibm.km.dto.KmSearchDetailsDTO" property="searchDetailsList" id="listDto" indexId="i">
			<%	String cssName = "alt";				
				%>
				<TR style="font-size:12px;font-weight:bold;" class="alt" >	
					<TD ><%=(i.intValue() + 1)%>.</TD><TD><bean:message key="ARC.CIRCLE" /></TD><TD ><INPUT type="text" value="<bean:write name="listDto" property="circle"/>" name="circle" id="circle"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;">
					<% i++; %>
					<TD ><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="ARC.ZONE" /></TD><TD ><INPUT type="text" value="<bean:write name="listDto" property="zone"/>" name="zone" id="zone"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt">
					<% i++; %>
					<TD ><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="ARC.ARCOR" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="arcOr"/>" name="arcOr" id="arcOr"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;">
					<% i++; %>
					<TD ><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="ARC.CITY" /></TD><TD ><INPUT type="text" value="<bean:write name="listDto" property="city" />" name="city" id="city"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt">
					<% i++; %>
					<TD ><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="ARC.PIN" /></TD><TD ><INPUT type="text" value="<bean:write name="listDto" property="pincode" />" name="pinCode" id="pinCode" maxlength="6"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;">
					<% i++; %>
					<TD ><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="ARC.ARC" /></TD><TD ><INPUT type="text" value="<bean:write name="listDto" property="arc" />" name="arc" id="arc"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt">
					<% i++; %>
					<TD ><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="ARC.ADD" /></TD><TD ><INPUT type="text" value="<bean:write name="listDto" property="address" />" name="address" id="address"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;">
					<% i++; %>
					<TD ><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="ARC.TIMING" /></TD><TD ><INPUT type="text" value="<bean:write name="listDto" property="showRoomTiming" />" name="showRoomTiming" id="showRoomTiming"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt">
					<% i++; %>
					<TD ><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="ARC.AVAILCCDC" /></TD><TD ><INPUT type="text" value="<bean:write name="listDto" property="availabilityOfCcDcMachine" />" name="availabilityOfCcDcMachine" id="availabilityOfCcDcMachine"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;">
					<% i++; %>
					<TD ><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="ARC.AVAILOFDOC" /></TD><TD ><INPUT type="text" value="<bean:write name="listDto" property="availabilityOfDoctorSim" />" name="availabilityOfDoctorSim" id="availabilityOfDoctorSim"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt">
					<% i++; %>
					<TD colspan="3" class="error" align="center"><FONT color="red"><a class="red-btn fll" href='javascript: submitForm(<bean:write name="listDto" property="srNo"/>)'>Update</a>
					</FONT>
					</TD>
				 </TR>
			</logic:iterate> 
		</logic:notEmpty>
		</table>
		</logic:equal><!-- End of ARC -->
		<logic:equal name="kmDocumentMstrForm" property="mainOptionValue" value="dist"><!-- Start of Dist -->
		<table width="100%">
		<logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
			<logic:iterate name="kmDocumentMstrForm" type="com.ibm.km.dto.KmSearchDetailsDTO" property="searchDetailsList" id="listDto" indexId="i">
			<%	String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<TR style="font-size:12px;font-weight:bold;" class="alt" >	
					<TD ><%=(i.intValue() + 1)%>.</TD><TD  ><bean:message key="DIST.PINCODE" /></TD><TD ><INPUT type="text" value="<bean:write name="listDto" property="pincode"/>" name="pinCode" id="pinCode" maxlength="6"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;">
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message key="DIST.STATE" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="stateName" />" name="stateName" id="stateName"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message key="DIST.CITY" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="city" />" name="city" id="city"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;">
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.PINC" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="pinCatagory" />" name="pinCatagory" id="pinCatagory"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.AREA" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="area" />" name="area" id="area"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.HUB" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="hub" />" name="hub" id="hub"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.CIRCLE" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="circle" />" name="circle" id="circle"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.SFANDSSDN" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="sfAndSSDName" />" name="sfAndSSDName" id="sfAndSSDName"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.SFANDSSDEM" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="sfAndSSDEmail" />" name="sfAndSSDEmail" id="sfAndSSDEmail"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.SFANDSSDCONT" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="sfAndSSDContNo" />" name="sfAndSSDContNo" id="sfAndSSDContNo" maxlength="10"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.TSM" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="tsmName"/>" name="tsmName" id="tsmName"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;">
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.TSMEM" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="tsmMailId"/>" name="tsmMailId" id="tsmMailId"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.TSMCONT" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="tsMContNo" />" name="tsMContNo" id="tsMContNo" maxlength="10"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.SRMNGR" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="srMngr" />" name="srMngr" id="srMngr"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message key="DIST.SRMNGREM" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="srMngrMailId" />" name="srMngrMailId" id="srMngrMailId"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;">
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.SRMNGRCONT" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="srMngrContNo" />" name="srMngrContNo" id="srMngrContNo" maxlength="10"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.ASM" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="asmName" />" name="asmName" id="asmName"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.ASMEM" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="asmMailId" />" name="asmMailId" id="asmMailId"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" >
					<% i++; %>
					<TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="DIST.ASMCONT" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="asmContNo" />" name="asmContNo" id="asmContNo" maxlength="10"></TD>
					</TR>
					<TR style="font-size:12px;font-weight:bold;" >
					<% i++; %>
 					<TD colspan="3" align="center"><a class="red-btn fll" href='javascript: submitForm(<bean:write name="listDto" property="srNo"/>)'>Update</a></TD>
					</TR>
			</logic:iterate> 
		</logic:notEmpty>
		</table>
		</logic:equal><!-- End of Dist -->
			<logic:equal name="kmDocumentMstrForm" property="mainOptionValue" value="coords"><!-- Start of COORDS -->
			<table width="100%">
		 <logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
			<logic:iterate name="kmDocumentMstrForm" type="com.ibm.km.dto.KmSearchDetailsDTO" property="searchDetailsList" id="listDto" indexId="i">
					<TR style="font-size:12px;font-weight:bold;" class="alt" ><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message key="COORD.CIRCLE" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="circle"/>" name="circle" id="circle"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;"><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="COORD.COMPNAME" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="compName" />" name="compName" id="compName"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD ><bean:message	key="COORD.SPOC" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="compSpocName" />" name="compSpocName" id="compSpocName"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="COORD.SPOCEM" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="compSpocMail" />" name="compSpocMail" id="compSpocMail"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="COORD.SPOCCONT" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="compSpocCont" />" name="compSpocCont" id="compSpocCont" maxlength="10"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message key="COORD.RM" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="rmMgr" />" name="rmMgr" id="rmMgr"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="COORD.RMMAIL" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="rmMailId" />" name="rmMailId" id="rmMailId"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="COORD.RMCONT" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="rmCont" />" name="rmCont" id="rmCont" maxlength="10"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="COORD.REQNAME" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="requestor" />" name="requestor" id="requestor"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="COORD.REQLOC" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="requestorLocation" />" name="requestorLocation" id="requestorLocation"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="COORD.REQCONT" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="requestorCont"/>" name="requestorCont" id="requestorCont" maxlength="10"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="COORD..REQON" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="requestedOn"/>" name="requestedOn" id="requestedOn"></TD></TR>
					<TR><TD class="alt" align="center" colspan="3"><a class="red-btn fll" href='javascript: submitForm(<bean:write name="listDto" property="srNo"/>)'>Update</a></TD></TR>
			</logic:iterate> 
		</logic:notEmpty>
		</table>
		</logic:equal><!-- End of COORDS -->
		<!-- VAS Details -->
		<logic:equal name="kmDocumentMstrForm" property="mainOptionValue" value="vas"><!-- Start of COORDS -->
			<table width="100%">
		 <logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
			<logic:iterate name="kmDocumentMstrForm" type="com.ibm.km.dto.KmSearchDetailsDTO" property="searchDetailsList" id="listDto" indexId="i">
					<TR style="font-size:12px;font-weight:bold;" class="alt" ><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message key="VAS.VASNAME" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="vasName"/>" name="vasName" id="vasName"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;"><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="VAS.ACTIVATIONCODE" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="activationCode" />" name="activationCode" id="activationCode"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD ><bean:message	key="VAS.DEACTIVATIONCODE" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="deactivationCode" />" name="deactivationCode" id="deactivationCode"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="VAS.CHARGES" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="charges" />" name="charges" id="charges"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message key="VAS.BENEFITS" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="benefits" />" name="benefits" id="benefits"></TD></TR>
					<TR style="font-size:12px;font-weight:bold;" class="alt" ><% i++; %><TD><%=(i.intValue() + 1)%>.</TD><TD><bean:message	key="VAS.CONSTRUCT" /></TD><TD><INPUT type="text" value="<bean:write name="listDto" property="construct" />" name="construct" id="construct"></TD></TR>
					<TR><TD class="alt" align="center" colspan="3"><a class="red-btn fll" href='javascript: submitForm(<bean:write name="listDto" property="srNo"/>)'>Update</a></TD></TR>
			</logic:iterate> 
		</logic:notEmpty>
		</table>
		</logic:equal>
		<!-- End -->
		
		
		
	</div>
    </div>
   </div>
   </form> 
