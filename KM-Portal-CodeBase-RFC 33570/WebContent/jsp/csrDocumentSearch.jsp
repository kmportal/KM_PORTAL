
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<logic:notEmpty name="DOCUMENT_LIST">
<bean:define id="documentList" name="DOCUMENT_LIST" type="java.util.ArrayList" scope="request"  />
</logic:notEmpty>

<bean:define id="csrHomeBean" name="CSR_HOME_BEAN"  type="com.ibm.km.forms.KmCSRHomeBean" scope="session" /> 

<html:errors />
<script language="JavaScript" src="jScripts/versionBox.js"
	type="text/javascript"	>
</script>
<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript"	>
</script>
<script language="JavaScript">

function displayDocument(docViewer,doc,versionCount){
	if(versionCount=="0")
	{
		window.location.href=docViewer+'&docID='+doc;
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


	<table width="100%" class="mTop0" align="center" border="0" cellspacing="0" cellpadding="0">	
				<tr>
	     			 <td width="100%" height="40" align="left" valign="top" class="pTop5" id="slider"><script src="jScripts/iecodea.js" type="text/javascript"></script>
	     			 </td>
	   			</tr>
 	</table>	
	<table width="100%" class="mLeft5" align="center">
		<tr align="left">
							<td colspan="2"><img src="images/search-result.gif" width="505" height="22"></td>
		</tr>
		</table>
		<table width="95%" cellpadding="0" border="1" cellspacing="0">
		<tr>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="viewAllFiles.SNO" /></span></td>
			
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.DocumentName" /></span></td>
			
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.path" /></span></td>
			
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Publishing End Dt</span></td>
		<!--	<td class="darkRedBg height19"><span class="mLeft5 mTop5"><bean:message
				key="viewAllFiles.ApprovalDate" /></span></td> -->
		<!--	<td bgcolor="a90000" align="center"></td>
			<td bgcolor="a90000" align="center">&nbsp;</td>
			<td bgcolor="a90000" align="center">&nbsp;</td> -->
		</tr>
		<logic:empty name="documentList" >
			<TR class="lightBg">
				<TD colspan="4" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
			
		</logic:empty>
			
		<logic:notEmpty name="documentList" >
			<logic:iterate name="documentList" type="com.ibm.km.dto.KmDocumentMstr" id="report" indexId="i">
				<tr  align="left" class="tr-outputdata-gray"  >
					
					<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><%=(i.intValue() + 1)%>.</span></TD>
					<TD  class="td-text-cellcontent-left height19" align="center"><span class="mLeft5 mTop5 mBot5"><A href="#" class="Red11"   onclick="displayDocument('<bean:write name="report" property="documentViewer"/>','<bean:write name="report" property="documentId"/>','<bean:write name="report" property="versionCount"/>');"><bean:write name="report"
						property="documentDisplayName" /></A></span></TD>
					<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="documentStringPath" /></span></TD>
					
					<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="publishEndDt" /></span></TD>
			<!--		<TD  class="td-text-cellcontent-left height19" align="center" ><span class="mLeft5 mTop5 mBot5"><bean:write name="report"
						property="approvalRejectionDate" /></span></TD> -->
				
					
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
