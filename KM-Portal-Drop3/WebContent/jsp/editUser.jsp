
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<LINK href="theme/text.css" rel="stylesheet" type="text/css">

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
	</script>
<script language="JavaScript" type="text/javascript">
function validateData(){
	var reg=/^[0-9]*$/;
	var mob=document.kmUserMstrFormBean.userMobileNumber.value;
	var names=/^[a-zA-Z]*$/;
	var len=mob.length;
	if(isEmpty(document.forms[0].userFname))
	{
			alert("Please enter User First Name");
			document.kmUserMstrFormBean.userFname.focus();
			return false;
	}
	if(isEmpty(document.forms[0].userLname))
	{
			alert("Please enter User Last Name");
			document.kmUserMstrFormBean.userLname.focus();
			return false;
	}
	if(isEmpty(document.forms[0].userEmailid))
	{
			alert("Please enter User Email Id");
			document.kmUserMstrFormBean.userLname.focus();
			return false;
	}
	if(!names.test(document.kmUserMstrFormBean.userFname.value)){
		alert("Special Characters are not Allowed in First Name");
		document.kmUserMstrFormBean.userFname.focus();
		document.kmUserMstrFormBean.userFname.value="";
		return false;	
	}
	//Bug resolved MASDB00064782
	if(!isEmpty(document.forms[0].userMname))
	{
		if(!names.test(document.kmUserMstrFormBean.userMname.value)){
			alert("Special Characters are not Allowed in Middle Name");
			document.kmUserMstrFormBean.userMname.focus();
			document.kmUserMstrFormBean.userMname.value="";
			return false;	
		}
	}
	
	if(!names.test(document.kmUserMstrFormBean.userLname.value)){
		alert("Special Characters are not Allowed in Last Name");
		document.kmUserMstrFormBean.userLname.focus();
		document.kmUserMstrFormBean.userLname.value="";
		return false;	
	}
	if(!isEmailAddress(document.kmUserMstrFormBean.userEmailid)){
			if(!isEmpty(document.kmUserMstrFormBean.userEmailid))
			{
				alert("Please enter Valid Email-ID");
				document.kmUserMstrFormBean.userEmailid.focus();
				return false;
			}	
	} 
	
	if(!reg.test(document.kmUserMstrFormBean.userMobileNumber.value)){
		alert("Please enter Valid User Mobile Number");
		document.kmUserMstrFormBean.userMobileNumber.focus();
		document.kmUserMstrFormBean.userMobileNumber.value="";
		return false;	
	}
	if(len<10||len>11)
	{		if(!isEmpty(document.kmUserMstrFormBean.userMobileNumber))
			{	
				
				alert("Please enter Valid User Mobile Number");
				document.kmUserMstrFormBean.userMobileNumber.focus();
				return false;
			}
	
	} 

	
	
	document.forms[0].methodName.value="editUser";
	document.forms[0].submit();	
	
}

</script>

<html:form action="/kmUserMstr">

	<html:hidden name="kmUserMstrFormBean" property="userId" />
	<html:hidden name="kmUserMstrFormBean" property="userLoginId" />
	<html:hidden name="kmUserMstrFormBean" property="methodName"  /> 
	<div class="box2">
        <div class="content-upload">

			<h1><bean:message key="updateUser.updateUser" /></h1>
	<table align="center">
		<tr>
			<td align="left"><br>
			<strong> <html:errors /></strong></td>
		</tr>
		<tr>
			<td align="center"><br>
			<logic:notEmpty name="kmUserMstrFormBean" property="userStatus">
			<FONT color="red"><bean:write name="kmUserMstrFormBean" property="userStatus" /></FONT>
		</logic:notEmpty></td>
		</tr>
		<tr> <td align="center"><logic:notEmpty name="kmUserMstrFormBean" property="userStatus">
			<FONT color="red"><bean:write name="kmUserMstrFormBean" property="userStatus" /></FONT>
		</logic:notEmpty></TD>
		</tr>
	</table>
	<ul class="list2 form1 ">
		
		<li class="clearfix" id="actor" >
				<span class="text2 fll width160"><strong><bean:message
				key="updateUser.UserLoginId" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text property="userLoginId"
				name="kmUserMstrFormBean" disabled="true" styleClass="textbox7" /></span> </span> </p>
		</li>
		<li class="clearfix" id="actor" >
				<span class="text2 fll width160"><strong><bean:message
				key="updateUser.FirstName" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="width100"
				property="userFname" maxlength="40" styleClass="textbox7" /></span> </span> </p>
		</li>
		<li class="clearfix" id="actor" >
				<span class="text2 fll width160"><strong><bean:message
				key="updateUser.MiddleName" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="width100"
				property="userMname" maxlength="40" styleClass="textbox7" /></span> </span> </p>
		</li>
		<li class="clearfix" id="actor" >
				<span class="text2 fll width160"><strong><bean:message
				key="updateUser.LastName" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="width100"
				property="userLname" maxlength="40" styleClass="textbox7" /></span> </span> </p>
		</li>
		<li class="clearfix" id="actor" >
				<span class="text2 fll width160"><strong><bean:message key="updateUser.E-Mail" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="width100"
				property="userEmailid" maxlength="40" styleClass="textbox7" /></span> </span> </p>
		</li>
		<li class="clearfix" id="actor" >
				<span class="text2 fll width160"><strong><bean:message key="updateUser.Mobile" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="width100"
				property="userMobileNumber" maxlength="11" styleClass="textbox7"/></span> </span> </p>
		</li>
		<li class="clearfix" id="actor" >
				<span class="text2 fll width160"><strong><bean:message key="updateUser.Status" /></strong></span>
			<html:select property="status" styleClass="select1">
				<html:option value="A">
					<bean:message key="updateUser.Active" />
				</html:option>
				<html:option value="D">
					<bean:message key="updateUser.Deactive" />
				</html:option>
			</html:select></li>
	<!-- <logic:equal name="kmUserMstrFormBean" property="kmActorId"
			value="7">
			<tr class="lightBg">
					<td align="right" class="text"><BR><bean:message key="updateUser.favCategory" />&nbsp;&nbsp;
									</TD>
									
					<logic:notEmpty name="kmUserMstrFormBean" property="elementList">
					<TD align="left" colspan="3"><BR>
					<bean:define id="categories" name="kmUserMstrFormBean"
									property="elementList" /> 
									<html:select property="categoryId" name="kmUserMstrFormBean"  >
									<html:option value="0">-Select Category-</html:option>
									<html:options labelProperty="elementName" property="elementId"
										collection="categories" />
					</html:select></TD>  </logic:notEmpty>
					<logic:empty name="kmUserMstrFormBean" property="elementList">
					<TD align="left" colspan="3"><BR>
					
									<html:select property="categoryId" name="kmUserMstrFormBean" >
									<html:option value="0">-Select Category-</html:option>
					</html:select>&nbsp;&nbsp;&nbsp;</TD>  
					
					 
					</logic:empty>
				
			</TR>
		</logic:equal> -->	
		
		<li class="clearfix" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
		    <input class="red-btn fll"  style="margin-right:10px;" value="Update" alt="Update" onclick="validateData();"/>		
		</li>
		
		</ul>
	
	</div>
	</div>
</html:form>

