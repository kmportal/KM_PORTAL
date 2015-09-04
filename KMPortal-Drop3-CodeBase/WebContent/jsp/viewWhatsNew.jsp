
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="whatsNewList" name="WHATS_NEW_LIST" type="java.util.ArrayList" scope="request"  />
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<TITLE></TITLE>
<html:errors />
<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript"	>
</script> 

<SCRIPT>
function displaySubmit(documentPath)
{
	alert(document.kmWhatsNewFormBean.selectedDocumentPath.value);
	document.kmWhatsNewFormBean.selectedDocumentPath.value=documentPath;
	document.kmWhatsNewFormBean.methodName.value= "displayDocument";
	document.kmWhatsNewFormBean.submit();

}
</SCRIPT>
<html:form action="/kmWhatsNew" >
	<html:hidden property="methodName"/>
	
	
	<table width="100%" class="mTop30" align="center">
		
		<tr align="left">
							<td colspan="2"><img src="images/whats-new.gif" width="505" height="22"></td>
		</tr>
		</table>
		<table width="95%" class="mLeft5">
		<tr align="left">
			<td class="darkRedBg height19"><span class="mLeft5 mTop5"><bean:message key="viewAllFiles.SNO" /></span></td>
			
			<td class="darkRedBg height19"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.DocumentName" /></span></td>
			<td class="darkRedBg height19"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.DocumentDescription" /></span></td>
			<td class="darkRedBg height19"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.UploadedDate" /></span></td>
			<td class="darkRedBg height19"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.UpdatedDate" /></span></td>
		<!--	<td bgcolor="a90000" align="center"></td>
			<td bgcolor="a90000" align="center">&nbsp;</td>
			<td bgcolor="a90000" align="center">&nbsp;</td> -->
		</tr>
		<logic:empty name="whatsNewList" >
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>

		</logic:empty>
			
			<logic:present name="whatsNewList" >
			<logic:iterate name="whatsNewList" id="report" indexId="i" type="com.ibm.km.dto.KmWhatsNew">
				<tr  align="left" class="tr-outputdata-gray">
					
					<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><%=(i.intValue() + 1)%>.</span></TD>
				<!--	<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><A HREF="kmWhatsNew.do?methodName=displayDocument&documentPath=<bean:write name="report" property="documentPath"/>" class="Red11"><bean:write name="report"
						property="documentDisplayName" /></A></span></TD>  -->
					<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="documentDesc" /></span></TD>
					<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="uploadedDate" />&nbsp;&nbsp;</span></TD>
					<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="updatedDate" /></span></TD>
				
					
				</TR>
			</logic:iterate>  
			</logic:present>
			
		</table>	
			
	
</html:form>
