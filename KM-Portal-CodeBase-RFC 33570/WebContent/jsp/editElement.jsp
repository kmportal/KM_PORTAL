
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="theme/text.css" rel="stylesheet" type="text/css">

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
	</script>
<script language="JavaScript" type="text/javascript">
function validateData(){
	

	var form=document.forms[0];
	// Fixed for defect id : MASDB00186295
	//var reg=/^[0-9a-zA-Z_]*$/;
	var reg=/^[0-9a-zA-Z_ ]*$/;
	
		if(form.elementName.value==""){
			alert("Please Enter a Element name");
			form.elementName.focus();
			return false;
		}	
			
		if(isEmpty(form.elementName)){
			alert("Please Enter a Element name");
			form.elementName.value="";
			form.elementName.focus();
			return false;
		}
	
		//alert(reg.test(form.elementName.value)+" "+form.elementName.value);

		if(!reg.test(form.elementName.value)){
			alert("Please Enter Alpha-Numeric Characters")
			form.elementName.value="";
			form.elementName.focus();
			return false;	
		}
	document.forms[0].methodName.value="editElement";
	 
	document.forms[0].submit();
	
}

</script>

<html:form action="/elementAction">

	<html:hidden name="kmElementMstrFormBean" property="elementId" />
	<html:hidden name="kmElementMstrFormBean" property="methodName"  /> 
	<table align="center">
		<tr>
			<td align="left"><br>
			<strong> <html:errors /></strong></td>
		</tr>

	</table>
	<div class="box2">
        <div class="content-upload">
        	<h1>Edit Element</h1>
        	 <ul class="list2 form1 ">

		<li class="clearfix">
			<span class="text2 fll width160"><strong><bean:message key="editElement.elementId" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text property="elementId"
				name="kmElementMstrFormBean" disabled="true" styleClass="textbox7" /></span> </span> </p>
		</li>
		<li class="clearfix alt">
			<span class="text2 fll width160"><strong><bean:message key="editElement.elementLoc" /></strong></span>
			<bean:write name="kmElementMstrFormBean" property="elementStringPath"/>
			
		</li>

			<li class="clearfix">
			<span class="text2 fll width160"><strong><bean:message key="editElement.elementName" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text property="elementName"
				name="kmElementMstrFormBean" maxlength="50"  styleClass="textbox7"/></span> </span> </p>
		</li>

		<li class="clearfix alt">
			<span class="text2 fll width160"><strong><bean:message key="editDocument.documentDesc" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="width100"
				name="kmElementMstrFormBean" property="elementDesc" maxlength="50" styleClass="textbox7"/></span> </span> </p>
		</li>
		
		
		<li class="clearfix" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
			 <html:button property="Submit" value="Update" styleClass="red-btn" onclick="return validateData();">  </html:button> 
			</li>
		
	</ul>
	</div>
	</div>
</html:form>

