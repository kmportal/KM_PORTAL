
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
	</script>
	<script type="text/javascript"
	src="jScripts/jquery-1.4.2.min.js"></script>
<script language="JavaScript" type="text/javascript">



function validateData(){
 
 	if(isEmpty(document.kmChangePasswordForm.oldPassword)){
			alert("Please enter Old Password");
			document.kmChangePasswordForm.oldPassword.focus();
			return false;
	}
	var reg=/[\s]+/;
	//alert(":"+document.kmUserMstrFormBean.userPassword.value+":");
	if(reg.test(document.kmChangePasswordForm.newPassword.value)||reg.test(document.kmChangePasswordForm.confirmPassword.value)){
		alert("Space is not allowed in the Password");
		document.kmChangePasswordForm.newPassword.value="";
		document.kmChangePasswordForm.confirmPassword.value="";
		document.kmChangePasswordForm.newPassword.focus();
		return false;
	}	
	if(isEmpty(document.kmChangePasswordForm.newPassword)){
			alert("Please enter New Password");
			document.kmChangePasswordForm.newPassword.focus();
			return false;
	}
	if(isEmpty(document.kmChangePasswordForm.confirmPassword)){
			alert("Please enter Confirm Password");
			document.kmChangePasswordForm.confirmPassword.focus();
			return false;
	}
	if(document.kmChangePasswordForm.newPassword.value != document.kmChangePasswordForm.confirmPassword.value)
		{
			alert("The Password and Confirm password do not match!");
			document.kmChangePasswordForm.newPassword.focus();
			document.kmChangePasswordForm.newPassword.value="";
			document.kmChangePasswordForm.confirmPassword.value="";			
			return false;
		} 
	document.kmChangePasswordForm.methodName.value="changePassword";
	//document.kmChangePasswordForm.submit();	
	
}
function setFocus()
{
	document.kmChangePasswordForm.oldPassword.focus();
	
}


</script>
<body>
<html:form action="/kmChangePassword"> 
	<html:hidden property="methodName" />
	

 <% String csrflag = (String)request.getAttribute("csrFirstLogin") ; 
	String memberFlag = (String)request.getAttribute("FirstLogin") ; 
       if(csrflag==null) {
       	 	csrflag="false" ; 
       	 } 
       	  if(memberFlag==null) {
       	 	memberFlag="false" ; 
       	 }
       	
  %>
  	
	<input type="hidden"  name="flagLogin" value="<%=csrflag %>" />
	<input type="hidden"  name="memberLogin" value="<%=memberFlag %>" />       
	



<div class="inner-content">
	<div >	&nbsp;
        	
			</div>   
			<div class="content-upload">
			
        	<h1><bean:message key="changePassword.changepassword" /></h1>
			</div>        
		
	
		<ul class="list2 form1 ">		
		
		<li>
			<strong> 
          	<html:messages id="msg" message="true">
                 <bean:write name="msg"/>  <a href="./Logout.do"><font color="blue">click...</font></a>              
             </html:messages>
            </strong>
		</li>
		  <li>
            	<strong> <html:errors /></strong>
            
            </li>
            
		<li class="clearfix">
          	<span class="text2 fll width160"><strong><bean:message
				key="changePassword.oldPassword" /></strong></span>
		<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:password styleClass="box"
				property="oldPassword" maxlength="250"  styleClass="textbox7" /></span> </span> </p>
			</li>
		<li class="clearfix alt">
          	<span class="text2 fll width160"><strong><bean:message
				key="changePassword.newPassword" /></strong></span>
		<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:password styleClass="box"
				property="newPassword" maxlength="255" styleClass="textbox7" /></span> </span> </p>
			</li>
			<li class="clearfix">
          	<span class="text2 fll width160"><strong><bean:message
				key="changePassword.confirmPassword" /></strong></span>
		<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:password styleClass="box"
				property="confirmPassword"  maxlength="255" styleClass="textbox7" /></span> </span> </p>
			</li>
		
		<li class="clearfix alt" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
			<input
				type="image" src="images/submit.jpg" class="btnActive"
				onclick="return validateData();" />
			
		</li>
		<li class="text2 fll"  >
		<br><br><b>Password Selection Criteria: </b><br><br><br>
		1. Contain at least 8 characters.<br>
		2. Contain at least one alphabetic and one non-alphabetic.<br>
		3. Contain at least one upper case character.<br>
		4. Non-numeric in first and last position.<br>
		5. Maximum consecutive identical character from any position in the previous password<br>
		   must be 3.<br>
		6. Maximum identical consecutive characters 2.<br>
		7. Must not contain the UserID as part of the password.<br>
		8. Number of password changes for which the password cannot be reused is 4.<br>
		
		<br>
		<br>
		<b>Notes:</b><BR><BR>
		1. Password will expire after 45 days.<br>
		2. After 3 login attempts with invalid passwords the user account will be locked.<br>
		
		</li>
		</ul>
	<BR><BR>
			
  </div>
		
</html:form>

<script>
$(document).ready(function(){
    $(document).keydown(function(event) {
        if (event.ctrlKey==true && (event.which == '118' || event.which == '86')) {
            alert('Do not PASTE!');
            event.preventDefault();
         }
    });
});


setFocus();
</script>
