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
<logic:notEmpty name="SUB_CATEGORY_LIST">
<bean:define id="subCatList" name="SUB_CATEGORY_LIST" type="java.util.ArrayList" scope="request"  />
</logic:notEmpty>

<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href="./theme/airtel.css" rel="stylesheet" type="text/css">
<LINK href="jsp/theme/css.css" rel="stylesheet" type="text/css">
<LINK href="jsp/theme/SelfcareIIStyleSheet.css" rel="stylesheet" type="text/css">
<TITLE></TITLE>
<html:errors />

<script language="JavaScript">
	
function openTree(subCatId)
{
opener.location.href = "documentAction.do?methodName=openSubCategoryTree&subCatId="+subCatId;
self.close();
}

</script> 
</HEAD>





	<table width="99%" class="border mTop1" align="center" cellspacing="0" cellpadding="0" bgcolor="#EEEEEE">
		
	
			<logic:notEmpty name="SUB_CATEGORY_LIST" >
				<logic:iterate name="subCatList" type="com.ibm.km.dto.KmElementMstr" id="report" indexId="i">
					
					<tr align="left" class="height19">
				<td width="27" class="pTop2" align="right"><img src="images/bullet.gif"></td>
				<td width="756" class="pLeft5 pTop2"><a href="#" class="green" onclick="openTree('<bean:write name="report" property="elementId" />')"><bean:write name="report" property="elementName" /></a></td>
				</tr>
			</logic:iterate>  
				
			</logic:notEmpty>
			</table>

</html:html>