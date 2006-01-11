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
<logic:notEmpty name="DOCUMENT_LIST">
<bean:define id="networkFaultList" name="DOCUMENT_LIST" type="java.util.ArrayList" scope="request"  />
</logic:notEmpty>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<bean:define id="csrHomeBean" name="CSR_HOME_BEAN"  type="com.ibm.km.forms.KmCSRHomeBean" scope="session" /> 
<LINK href="<%=request.getContextPath()%>/jsp/theme/css.css" rel="stylesheet" type="text/css">

<TITLE></TITLE>
<html:errors />
<script language="JavaScript" src="<%=request.getContextPath()%>/jsp/jScripts/versionBox.js"
	type="text/javascript"	>
</script>
<script language="JavaScript" src="<%=request.getContextPath()%>/jsp/jScripts/KmValidations.js"
	type="text/javascript"	>
</script>
<script language="JavaScript">

function displayDocument(doc,versionCount){
	if(versionCount=="0")
	{
		window.location.href='documentAction.do?methodName=displayDocument&docID='+doc;
	}
	else{
		var url='documentAction.do?methodName=displayVersions&docID='+doc;
		window.open(url,'_blank',"resizable=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,width=510,height=250,left=250,top=250");
	}
}	
</script>
<SCRIPT>
function showScrollerMessage()
	{
		var message='<div style="margin-top:2px;" ><font face=arial size=2 color="white"><B><bean:write name="csrHomeBean" property="message" /></B></font></font></DIV>';
		document.getElementById('slider').innerHTML=setMessage(message);
		timer();
			
	}

function timer(){
	getAlerts()
	window.setTimeout('timer()',30000)
	}

function getAlerts()
{
	var url="kmAlertMstr.do?methodName=view";
	if(window.XmlHttpRequest) {
		req = new XmlHttpRequest();
		
	}else if(window.ActiveXObject) {
		req=new ActiveXObject("Microsoft.XMLHTTP");
		
	}
	if(req==null) {
		alert("Browser Doesnt Support AJAX");
		return;
	}
	req.onreadystatechange = getOnChange;
	req.open("POST",url,true);
	req.send();
	
}
function getOnChange()
{
	if (req.readyState==4 || req.readyState=="complete") {
	if(req.responseText=="none")
	{
	
	}
	else{
	//alert("YOU HAVE A NEW MESSAGE");
	document.getElementById("alert").value = req.responseText;
	
	}
}
}

</SCRIPT>
</HEAD>



<body >

	<table width="100%" class="mTop0" align="center" border="0" cellspacing="0" cellpadding="0">	
				<tr>
	     			 <td width="100%" height="40" align="center" valign="top" class="pTop5" id="slider"><script src="<%=request.getContextPath()%>/jsp/jScripts/iecodea.js" type="text/javascript"></script>
	     			 <strong>Network Fault Log </strong>
	     			 </td>
	   			</tr>
 	</table>	
	<table width="100%" class="mLeft5" align="center">
		<tr align="left">
							<td colspan="2"><img src="<%=request.getContextPath()%>/images/search-result.gif" width="505" height="22"></td>
		</tr>
		</table>
		<table width="95%" cellpadding="3" border="1">
		<tr>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="viewAllFiles.SNO" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="viewNetworkFault.circle" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35" ><span class="mLeft5 mTop5"><bean:message key="viewNetworkFault.affectedArea" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="viewNetworkFault.problem" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="viewNetworkFault.reportedTime" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="viewNetworkFault.tat" /></span></td>
		</tr>
	<logic:empty name="networkFaultList" >
			<TR class="lightBg">
				<TD colspan="6" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
			
		</logic:empty>
			
		<logic:notEmpty name="networkFaultList" >
			<logic:iterate name="networkFaultList" type="com.ibm.km.dto.NetworkErrorLogDto" id="report" indexId="i">
				<tr  align="left" class="tr-outputdata-gray" >
					<TD  class="td-text-cellcontent-left height19" align="left" ><span class="mLeft5 mTop5 mBot5"><%=(i.intValue() + 1)%>.</span></TD>
					<TD  class="td-text-cellcontent-left height19" align="left" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report" property="circleName" /></span></TD>
					<TD  class="td-text-cellcontent-left height19" align="left" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report" property="areaAffected" /></span></TD>
					<TD  class="td-text-cellcontent-left height19" align="left" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report" property="problemDesc" /></span></TD>
					<TD  class="td-text-cellcontent-left height19" align="left" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report" property="loggingTime" /></span></TD>
					<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report" property="tat" /></span></TD>
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
	</body>
</html:html>