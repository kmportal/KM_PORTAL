<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page 
language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
import="com.ibm.km.dto.KmUserMstr"
%>
<%@page import="java.util.ArrayList" %>
<%@page import="com.ibm.km.dto.KmLinkMstrDto" %>

<script type="text/javascript" src="jScripts/DateTime.js"></script>
<style type="text/css"> 
		.HL {background: #ffff99; 
		color: #000000;}  
</style>  
<script language="javascript">
function clickLink(url){
		window.open(url,'_blank',"resizable=yes,location=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,width=750,height=300,left=10,top=10");
}

function highlight(container,what,spanClass) {
		var content = container.innerHTML;
		var	pattern = new RegExp('(>[^<.]*)(' + what + ')([^<.]*)','g');
		var replaceWith = '$1<span ' + ( spanClass ? 'class="' + spanClass + '"' : '' ) + '>$2</span>$3';
		var highlighted = content.replace(pattern,replaceWith);
    return (container.innerHTML = highlighted) !== content;
}
</script>
<bean:define id="kmUserBean" name="USER_INFO"  type="KmUserMstr" scope="session" />



<div class="logo-area">
    <div class="logo_iportal"><img src="images/iportel_animation.gif" width="158" height="44" alt="iportal"/></div>
    <div class="logo_airtel"><img src="images/logo-airtel.jpg" alt="airtel" width="106" height="28" border="0" /></div><br><br><br>
    </div>
    <div class="admin clearfix">
    <div class="admin-content">
      <h6>welcome <span><bean:write name='kmUserBean' property="userFname" /></span>&nbsp;(<bean:write name='kmUserBean' property="kmActorName" />)</h6>
      &nbsp;| last login : <span class="white10" id="clock"><!--<SCRIPT language="javascript" src="./jScripts/DateTime.js"></SCRIPT> <SCRIPT>getthedate();dateTime();</SCRIPT>lastLoginTime -->
      <bean:write name='kmUserBean' property="lastLoginTime" />
      </span></div>
      <ul class="flr right-nav clearfix" style="margin-bottom: 0px; padding: 0px;">
<%
ArrayList<KmLinkMstrDto> topLinkList = (ArrayList<KmLinkMstrDto>) session.getAttribute("TOP_LINKS_LIST");
if(!topLinkList.equals(null) )
{

 for(int i=0;i<topLinkList.size();i++)
 {
 KmLinkMstrDto dto = topLinkList.get(i);
 %>
 
 <li style="border:0px;height:15px"  >
        			<a href="javascript:clickLink('<%=dto.getLinkPath() %>')"> <%=dto.getLinkTitle()%> </a>
  			</li>
 <%
  }
  }
 %>
 <!--
     <li style="border:0px;height:15px"  ><a href="javascript:clickLink('http://www.airtel.in');">Airtel Home</a></li>
        <li style="border:0px;height:15px"  ><a href="javascript:clickLink('http://www.airtel.in');">International Roaming</a></li>
        <li style="border:0px;height:15px"  ><a href="javascript:clickLink('http://www.airtel.in');">Reach us</a></li>
        -->
        
        <li class="last"  ><a href="./Logout.do"><img src="common/images/logout.png" alt="" height="25" width="25" border="0"  /></a></li>
      </ul>      
  </div>
  



<div class="navigation">
<div class="navigation-left"></div>
<div class="navigation-middle">
<ul>
<li><a href="./home.do">Home</a></li>
<li><a href="./documentAction.do?methodName=showHelpDocument">Help</a></li>
<li><a href="./kmChangePassword.do?methodName=init" >Change Password</a></li>

</ul>
</div>
<div class="navigation-right"></div>
</div>



