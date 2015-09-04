<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>iPortal</title>
<style type="text/css">
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%>

</style>
<link href="./jsp/theme/css.css" rel="stylesheet" type="text/css" />
</head>

<body>
<table width="778" border="0" align="center" cellpadding="0" cellspacing="0" style="background-image:url(./images/bg_1.jpg); background-repeat:repeat-y; background-position:center top;">
  <tr>
    <td width="177" height="91" align="right"><img src="./images/iportel_animation.gif" alt="iPortal" width="158" height="44" /></td>
    <td width="470">&nbsp;</td>
    <td width="131"><img src="./images/airtel_logo.jpg" alt="Airtel" width="107" height="80" /></td>
  </tr>
  
  <tr>
    <td height="36" colspan="3"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="36" style="background-image:url(./images/top_heading_heading.jpg); background-repeat:repeat-x; background-position:left top;"><table width="275" border="0" align="right" cellpadding="0" cellspacing="0">
      <tr>
        <td width="20"><img src="./images/time_machine.jpg" width="19" height="26" /></td>
        <td class="arialboldblack" style="padding-left:2px;">Server Time: <span class="black10" id="clock"><SCRIPT language="javascript" src="./jScripts/DateTime.js"></SCRIPT> <SCRIPT>getthedate();dateTime();</SCRIPT> </span>  </td>
      </tr>
    </table></td>
  </tr>
</table></td>
  </tr>
  <tr>
    <td colspan="3" align="center" style="padding-top:20px; text-align:left"><table width="95%" border="0" align="center" cellpadding="0" cellspacing="0">
      <tr>
        <td width="50%"><img src="./images/error_page.jpg" width="115" height="28" /></td>
        <td width="50%" rowspan="4" valign="top"><img src="./images/timed_out_image.jpg" alt="" width="401" height="163" /></td>
      </tr>
	   <script>
	     var seconds=15; var newpage=""; window.setTimeout("countDown()",seconds*1000); function countDown() { document.location.href=newpage; }
	</script>
      <tr>
        <td width="50%" class="arial11grey" ><p>Your session has been timed out...<br />
          Please login again...<br />
          In 15 seconds, you will be automatically redirected to the home page of <a href="initCSRLogin.do" class="arial11blue"> iPortal Login page</a>.</p>
          <p>&nbsp;</p>
          <p>» You can also click on this link: <a href="initCSRLogin.do" class="arial11blue"  style="color: blue;"><b>iPortal Login page</b></a>.</p></td>
        </tr>
      <tr>
        <td width="50%">&nbsp;</td>
        </tr>
      <tr>
        <td width="50%">&nbsp;</td>
        </tr>
    </table></td>
  </tr>
  <tr>
    <td height="50" colspan="3" align="center"><table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
  <tr>
    <td height="50" align="left" valign="top" class="whttext" style="background-image:url(./images/bottom_bg.jpg); background-repeat:repeat-x; background-position:left top; padding-left:8px; padding-top:8px;">&copy; 2008 IBM Bharti, All Right Reserved.</td>
  </tr>
</table></td>
  </tr>
</table>
</body>
</html>
