<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<LINK href="theme/text.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="jScripts/KmValidations.js" type="text/javascript"> </script>
<script language="JavaScript" type="text/javascript">
function validateData(){
	
/* validation on Publish end date */
  var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;
		
		
		var reg=/^[0-9a-zA-Z_ ]*$/;
		
		
	   
	if(document.forms[0].publishEndDt.value =="")
		{
		 	alert("Publish End Date cannot be Blank ");
			return false;			
		}else if(document.forms[0].publishEndDt.value < curr_dt)
		{
		 	alert("Publish End Date cannot be a past date ");
			return false;			
		}
		//Change made against Defect Id MASDB00105327  
		
		if(isEmpty(document.forms[0].documentDisplayName)){
			alert("Please enter document title");
			return false;	
		} 
		else if(!reg.test(document.forms[0].documentDisplayName.value))
		{
		alert("Special characters except underscore not allowed in Document Title");
		return false;	
	   }
	else
		{
        	document.forms[0].methodName.value="editDocument";
	 		document.forms[0].submit();
		}
}

</script>
<html:form action="/documentAction">
<html:hidden name="kmDocumentMstrForm" property="documentId" />
<html:hidden name="kmDocumentMstrForm" property="docName" />
<html:hidden name="kmDocumentMstrForm" property="methodName"  /> 
<div class="box2">
    <div class="content-upload">
		<h1>Edit Document</h1>
	<table align="center">
		<tr>
			<td align="left"><br>
			<strong> <html:errors /></strong></td>
		</tr>

	</table>
	<ul class="list2 form1">		
		<li class="clearfix">
			<span class="text2 fll width160"><strong><bean:message key="editDocument.documentId" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text property="documentId"
				name="kmDocumentMstrForm" disabled="true" styleClass="textbox7"/></span> </span> </p>
		</li>
		<li class="clearfix alt">
			<span class="text2 fll width160"><strong><bean:message key="editDocument.documentName" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text property="docName"
				name="kmDocumentMstrForm" disabled="true" styleClass="textbox7"/></span> </span> </p>
		</li>
		<li class="clearfix">
			<span class="text2 fll width160"><strong><bean:message key="editDocument.title" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text property="documentDisplayName"
				name="kmDocumentMstrForm"  styleClass="textbox7" maxlength="500"/></span> </span> </p>
		</li>
		
		<li class="clearfix alt">
			<span class="text2 fll width160"><strong><bean:message key="editDocument.documentLoc" /></strong></span>
			<bean:write name="kmDocumentMstrForm" property="documentStringPath" />
		</li>
		<li class="clearfix">
			<span class="text2 fll width160"><strong><bean:message key="editDocument.keywords" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="width100"
				name="kmDocumentMstrForm" property="keyword" maxlength="230" styleClass="textbox7"/></span> </span> </p>
		</li>
		<li class="clearfix alt">
			<span class="text2 fll width160"><strong><bean:message key="editDocument.documentDesc" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="width100"
				name="kmDocumentMstrForm" property="documentDesc" maxlength="40" styleClass="textbox7"/></span> </span> </p>
		</li>
		<%-- Added by Atul --%>
		<li class="clearfix">
			<span class="text2 fll width160"><strong><bean:message key="editDocument.documentEndDt" /></strong></span>
			<input type="text" class="tcal calender2 fll" readonly="readonly" name="publishEndDt" value="<bean:write property='publishEndDt' name='kmDocumentMstrForm'/>"/>
		</li>
		
		<li class="clearfix" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
			 <html:button property="Submit" value="Update" styleClass="red-btn" onclick="return validateData();">  </html:button> 
		</li>
		</ul>
	
	</div>
	</div>
</html:form>

