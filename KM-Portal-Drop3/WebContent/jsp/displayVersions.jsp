
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<logic:notEmpty name="DOCUMENT_LIST">
<bean:define id="documentList" name="DOCUMENT_LIST" type="java.util.ArrayList" scope="request"  />
</logic:notEmpty>
<bean:define id="documentId" name="DOCUMENT_ID" type="java.lang.String" scope="request"  />

<LINK href="./theme/css.css" rel="stylesheet" type="text/css">

<html:errors />

<script language="JavaScript">
	
function displayDocument(docVersion,doc){
	window.close();
	opener.location.href='documentAction.do?methodName=displayDocument&docID='+docVersion;
	opener.resizeTo(1000,1000);
}
function openOriginalDocument(doc)
{	
	window.close();
	opener.location.href='documentAction.do?methodName=displayDocument&docID='+doc;
	opener.resizeTo(1000,1000);
	
}
</script> 


	<table width="99%" class="border mTop5" align="center" cellspacing="0" cellpadding="0" bgcolor="#EEEEEE">
	<tr align="left">
			<td colspan="2"><img src="images/doc_versions.gif" width="505" height="22"></td>
	</tr>
		<logic:empty name="documentList" >
	<tr><td>No versions found</td></tr>
		</logic:empty>
		<logic:notEmpty name="documentList" >
			<logic:iterate name="documentList" type="com.ibm.km.dto.KmDocumentMstr" id="report" indexId="i">
				
				<tr align="left" class="height19">
			<td width="27" class="pTop2" align="right"><img src="images/bullet.gif"></td>
			<td width="756" class="pLeft5 pTop2"><a href="#" class="Red11" onclick="displayDocument('<bean:write name="report" property="documentId"/>','<bean:write name="documentId" />');"><bean:write name="report" property="documentName" /></a></td>
			</tr>
		</logic:iterate>  
			
		</logic:notEmpty>
		
		
		</table>
		<table>
		<TR>
		<td align="center" colspan="3" ><A href="#" onclick="openOriginalDocument('<bean:write name="documentId" />');">Current Version</A></td>
		</TR>
		</table>
