
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<LINK href="./jsp/theme/main.css" rel=stylesheet>
<script language="javascript"> 
  
function callOnload()
{
	document.userBean.userLoginId.focus();
}
<%@ include file="/jScripts/capLock.js" %>
</script>
<BODY background="images/image/bg_main.gif" leftMargin=0 topMargin="0" marginheight="0" marginwidth="0">
<html:form action="/csrPageLogin" focus="userId">
<input type="hidden" value="TRUE" name="CSR">
<TABLE cellSpacing=0 cellPadding=0 width=776 align=center background="images/image/member-bgbgbg-blue.gif" border=0 style="margin-top:25px">
  <TBODY>
  <TR>
    <TD>
      <TABLE height=42 cellSpacing=1 cellPadding=1 width=776 background="images/image/member-top-blue.gif" border=0>
        <TBODY>
        <TR>
          <TD vAlign=bottom align=right>
            <TABLE cellSpacing=3 cellPadding=1 width="100%" border=0>
              <TBODY>
              <TR>
                <TD vAlign=baseline align=right>
                  <TABLE class=menustyle style="BORDER-RIGHT: 0px; BORDER-TOP: 0px; Z-INDEX: 1000; BORDER-LEFT: 0px; BORDER-BOTTOM: 0px; BORDER-COLLAPSE: collapse" cellSpacing=0 cellPadding=0 border=0>
                    <TBODY>
                    <TR>
                      <TD class=menuitem>&nbsp;</TD>
                     <TD class=menuitem><IMG src="images/image/member-link02.gif" alt="" width="13" height="15" border=0><a href="initForgotPassword.do">Forgot Password</a></TD>
                      <TD class=menuitem>&nbsp;</TD>
                    </TR></TBODY></TABLE></TD></TR></TBODY></TABLE></TD>
          <TD width=5></TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD align=middle>
      <TABLE cellSpacing=0 cellPadding=0 width=760 align=center border=0>
        <TBODY>
        <TR>
          <TD><IMG height=100 src="images/image/member-bigpic-blues3.jpg" width=760></TD>
		</TR>
		</TBODY>
	  </TABLE>
      <TABLE cellSpacing=1 cellPadding=0 width=760 align=center bgColor="#4f4f4f" border=0>
        <TBODY>
        <TR bgColor=#4a97d5>
          <TD width=450 bgcolor="#b0dfd9"><IMG height=25 src="images/image/arrow-001g.gif" width=22> <SPAN class="topmenu">Welcome to KM Portal !!</SPAN></TD>
          <TD bgcolor="#b0dfd9"><IMG height=25 src="images/image/arrow-001y.gif" width=22 align=absMiddle> <SPAN class="download_name4" style="FONT-WEIGHT: bold">KM-Portal CSR Login</SPAN></TD>
        </TR>
        <TR bgColor=#ffffff>
          <TD width=450 align="center" vAlign=middle bgcolor="000000"><img src="images/image/login-pic.gif" width="433" height="309"></TD>
          <TD vAlign=top>
            <TABLE height=135 cellSpacing=5 cellPadding=0 width="100%" background="images/image/member-bgbgbgbg.jpg" border=0>
              <TBODY>
              <TR>
                <TD vAlign=top align=middle>
                  <TABLE cellSpacing=0 cellPadding=3 width="97%" border=0>
                    <TBODY>
                    <tr>
						<td colspan="2" align="left" >
            			<strong>
						 <FONT color="red">
          					<html:messages id="msg" message="true">
                 				<bean:write name="msg"/>  
                          
             				</html:messages>
            			</FONT>
            			</strong>
            			</td>
					</tr>       
                    <TR>
                      <TD class=normal>
					  	<IMG height="7" src="images/image/img001_07.gif" width=7 align="absMiddle"> <SPAN class="contact-1"><bean:message key="login.loginId" /></SPAN><BR><SPAN class="normal">&nbsp;</SPAN> 
                        <html:text name="kmLoginForm"  property="userId" style="WIDTH: 98%" maxlength="25" /></TD>
					</TR>
                    <TR>
                      <TD class=sty01><IMG height="7" src="images/image/img001_07.gif" width="7" align="absMiddle"> <SPAN class="contact-1"><bean:message key="login.password" /></SPAN><BR><SPAN class="normal">&nbsp;</SPAN>
                        <html:password name="kmLoginForm" 	property="password" redisplay="false" style="WIDTH: 98%" onkeypress="checkCapsLock(this,event )" /> </TD>
					</TR>
                    <TR>
                      <TD>
                        <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
                          <TBODY>
						<tr>
						
							<td class="error" style="color: red"><html:errors property="errors.login.user_invalid" /></td>
                            <TD align=right>
                            <html:image src="./images/image/btn_submit.gif" property="submit" alt="CSR Login" style="cursor:pointer; border: 0px;"
									title="CSR Login"  ></html:image></TD>
                          </TR></TBODY></TABLE></TD></TR></TBODY></TABLE></TD></TR></TBODY></TABLE>
            <TABLE cellSpacing=1 cellPadding=7 width="100%" border=0>
              <TBODY>
						<tr>
							<td colspan="4" class="text">
							<strong> <bean:write name="kmLoginForm" property="message" /></strong></td>
						</tr>  
					             
              <TR>
                <TD class=word1 vAlign=top>&nbsp;</TD>
              </TR></TBODY></TABLE>
            <TABLE cellSpacing=1 cellPadding=7 width="100%" border=0>
              <TBODY>
              <TR>
                <TD vAlign=top width="26%"><p>&nbsp;</p>
                  <p>&nbsp;</p>
                  <p>&nbsp;</p></TD>
                <TD vAlign=top>&nbsp;</TD>
              </TR></TBODY></TABLE>
            <TABLE cellSpacing=1 cellPadding=7 width="100%" border=0>
              <TBODY>
              <TR>
                <TD vAlign=top>&nbsp;</TD>
              </TR></TBODY>
			</TABLE>
		</TD></TR></TBODY></TABLE></TD></TR>
  <TR>
    <TD align=middle><IMG height=50 src="images/image/member-bottom-blue.gif" width=776></TD></TR></TBODY></TABLE>
</html:form>
</BODY>

