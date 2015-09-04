
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ include file="/jScripts/capLock.js" %>
<link href="./jsp/theme/airtel.css" rel="stylesheet" type="text/css" />
<script language="javascript"> 


 
function callOnload()
{
	document.userBean.userLoginId.focus();
}

</script>

<html:form action="/login" focus="userId">
<input type="hidden" value="TRUE" name="CSR">
<table width="778" border="0" align="center" cellpadding="0" cellspacing="0" style="background-image:url(./images/bg_1.jpg); background-repeat:repeat-y; background-position:center top;">
  <tr>
    <td width="177" height="91" align="right"><img src="./images/iportel_animation.gif" alt="iPortal" width="158" height="44" /></td>
    <td width="470">&nbsp;</td>
    <td width="131"><img src="./images/airtel_logo.jpg" alt="Airtel" width="107" height="80" /></td>
  </tr>
  <tr>
    <td colspan="3" align="center"><img src="./images/master_head.jpg" alt="" width="770" height="127" /></td>
  </tr>
  <tr>
    <td height="36" colspan="3"><table width="770" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="36" align="center" style="background-image:url(./images/top_heading_heading.jpg); background-repeat:repeat-x; background-position:left top;"><img src="./images/welcome_to_iportal.jpg" alt="" width="155" height="16" /></td>
  </tr>
</table></td>
  </tr>
  <tr>
    <td height="265" colspan="3" align="center"><table width="400" border="0" cellpadding="0" cellspacing="0" bgcolor="#D9EEF6">
      <tr>
        <td width="9" height="4" ><img src="./images/box_cov-left-top.jpg" alt="" width="6" height="4" /></td>
        <td colspan="2"></td>
        <td width="10" align="right"><img src="./images/box_cov-right-top.jpg" alt="" width="6" height="4" /></td>
      </tr>
      <tr>
        <td height="19">&nbsp;</td>
        <td width="20" height="19" valign="top"><span class="login_heading"><img src="./images/lock.jpg" alt="" width="15" height="17" /></span><br /> </td>
        <td width="361" align="left" valign="top" style="padding-top:3px;"><span class="login_heading"> KM Portal Login</span></td>
        <td width="10">&nbsp;</td>
      </tr>
      <tr>
				<td colspan="4" class="aria_11_black">
				<strong> <html:errors  /></strong></td>
		</tr>
		<tr>
				<td colspan="4" class="aria_11_black">
				<strong> <bean:write name="kmLoginForm" property="message" /></strong></td>
		</tr> 
      <tr>
        <td>&nbsp;</td>
        <td colspan="2" valign="top"><table width="100%" border="0" cellpadding="0" cellspacing="0">
          
          <tr>
            <td width="127" height="24" align="right" class="aria_11_black"><bean:message key="login.loginId" /></td>
            <td width="8">&nbsp;</td>
            <td width="246" align="left"><html:text name="kmLoginForm"  property="userId" style="WIDTH: 60%" maxlength="25" /></td>
          </tr>
          <tr>
            <td height="3" align="right"></td>
            <td height="3"></td>
            <td height="3" align="left"></td>
          </tr>
          <tr>
            <td width="127" height="23" align="right" class="aria_11_black"><bean:message key="login.password" /></td>
            <td width="8">&nbsp;</td>
            <td width="246" align="left"><html:password name="kmLoginForm" 	property="password" redisplay="false" style="WIDTH: 60%" onkeypress="checkCapsLock(this,event )" /></td>
          </tr>
          <tr>
            <td width="127" height="5" align="right"></td>
            <td width="8" height="5" align="left"></td>
            <td width="246" height="5" align="left"></td>
          </tr>
          
		  <tr>
            <td width="127" height="27" align="right">&nbsp;</td>
            <td width="8">   </td>
            <td width="246" align="left"><input type="image" name="button" id="button" src="./images/submit_button.jpg" /></td>
          </tr>
          <tr>
            <td colspan="3" class="text">
							<strong> <bean:write name="kmLoginForm" property="message" /></strong></td>
						
          </tr>
          <tr><td></td></tr>
          <tr align="left">
							<td colspan="4" class="aria_11_black"><a href="initForgotPassword.do">Forgot password</a></td>
			</tr>  
        </table></td>
        <td width="10">&nbsp;</td>
      </tr>
      
      <tr>
        <td height="4"><img src="./images/box_cov-left-bottom.jpg" alt="" width="6" height="4" /></td>
        <td colspan="2"></td>
        <td width="10" align="right"><img src="./images/box_cov-right-bottom.jpg" alt="" width="6" height="4" /></td>
      </tr>
    </table></td>
  </tr>
  <tr>
    <td height="55" colspan="3" align="center"><table width="771" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="55" align="left" valign="top" class="whttext" style="background-image:url(./images/bottom_bg.jpg); background-repeat:repeat-x; background-position:left top; padding-left:8px; padding-top:8px;">&copy; 2008 IBM Bharti, All Right Reserved.</td>
  </tr>
</table></td>
  </tr>
</table>
</html:form>
