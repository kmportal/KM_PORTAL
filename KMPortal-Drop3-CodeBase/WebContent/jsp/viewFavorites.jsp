<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%@page import="com.ibm.km.common.Utility"%><bean:define id="csrHomeBean" name="CSR_HOME_BEAN"  type="com.ibm.km.forms.KmCSRHomeBean" scope="session" /> 

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
</script>

	<table width="100%" class="mTop0" align="center" border="0" cellspacing="0" cellpadding="0">	
		<tr>
	     			 <td width="100%" align="left" valign="top" class="pTop5" id="slider"><script src="jScripts/iecodea.js" type="text/javascript"></script>
	     			 </td>
	   	</tr>
    </table>	  
    <div class="box2">
  		<h1>My Favourite Files</h1>	
        <table width="100%" class="mLeft5" cellspacing="0" cellpadding="0" style="table-layout: fixed;">
		 <tr align="left">
			<td width="10%"  align="center" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="25"><span class="mLeft5 mTop5"><bean:message key="favorites.SNO" /></span></td>
		<!--	<td class="darkRedBg height19"><span class="mLeft5 mTop5"><bean:message
				key="favorites.documentId" /></span></td>   -->
			<td width="30%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="25"><span class="mLeft5 mTop5"><bean:message
				key="favorites.documentName" /></span></td>
			<td width="30%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="25"><span class="mLeft5 mTop5"><bean:message
				key="favorites.documentPath" /></span></td>
			<td width="12%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="25"><span class="mLeft5 mTop5"><bean:message
				key="favorites.addedOn" /></span></td>
			<td width="18%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="25"><span class="mLeft5 mTop5">Publishing End Date </span></td>	
			
		<!--	<td bgcolor="a90000" align="center"></td>
			<td bgcolor="a90000" align="center">&nbsp;</td>
			<td bgcolor="a90000" align="center">&nbsp;</td> -->
		</tr>
		
		<logic:empty name="favoriteList" scope="request">
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
		</logic:empty>
	
		<logic:notEmpty name="favoriteList" scope="request">
			<logic:iterate name="favoriteList" id="report" indexId="i" scope="request" type="java.util.HashMap">
				<%
				String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>">
				
					<TD height="20"  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><%=(i.intValue() + 1)%>.</span></TD>
			<!--	<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="DOCUMENT_ID" /></span></TD> -->
					<TD   class="td-text-cellcontent-left height19" style="word-wrap: break-word"><span class="mLeft5 mTop5 mBot5"><A href='<%=Utility.getDocumentViewURL(""+report.get("DOCUMENT_ID"),  Integer.parseInt(""+report.get("DOC_TYPE")))%>' class="Red11"><bean:write name="report"
						property="DOCUMENT_NAME" /></A></span></TD>
					<TD   class="td-text-cellcontent-left height19" style="word-wrap: break-word"><span class="mLeft5 mTop5 mBot5"><bean:write name="report"	property="DOCUMENT_STRING_PATH" /></span></TD>
					<TD   class="td-text-cellcontent-left height19" style="word-wrap: break-word"><span class="mLeft5 mTop5 mBot5"> <bean:write name="report"	property="UPDATED_ON" /></span></TD>
	                <TD   class="td-text-cellcontent-left height19" style="word-wrap: break-word"><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="PUBLISHING_END_DT" /></span></TD>			
				</TR>
			</logic:iterate>  
<%--		<tr class="lightBg">
			<td class="width150 pLeft10 pTop2 pBot5">&nbsp;</td>
			<td colspan="2" class="wid250 pTop2 pBot5"><span class="width250"><BR><BR><input
				type="image" src="./images/update2.gif" class="btnActive"
				onclick="return validateData();" /></span></td> --%>
			</logic:notEmpty>
		  </table>		  
  </DIV>