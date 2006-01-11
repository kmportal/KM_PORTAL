
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%>

<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">

<TITLE>Download Page</TITLE>


</HEAD>
<logic:notEmpty name="CURRENT_PAGE">
<logic:equal name="CURRENT_PAGE" value="DOCUMENT_ERROR"> 
<BODY>


<table>
<tr align="center">
<td>
<FONT color="red">&nbsp;FILE NOT FOUND.</FONT>
</td>
</tr>
</table>


</BODY>
</logic:equal>
 </logic:notEmpty>
</html:html>
