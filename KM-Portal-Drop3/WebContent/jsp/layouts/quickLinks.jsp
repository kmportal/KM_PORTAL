<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="com.ibm.km.dto.KmLinkMstrDto"%>
<%@page import="java.util.ArrayList"%>
<%@ page 
language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
import="com.ibm.km.dto.KmUserMstr"
%><%int titleLength; %>
<script language="javascript">
function clickLink(url){
		window.open(url,'_blank',"resizable=yes,location=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,width=750,height=600,left=10,top=10");
}
</script>
<%
int pixLen = 9;
int padding = 10;
int initialIndex = 80;	
 String len1 = "0";
 String len2 = "0";
 int link2Index = 0;
 int link3Index = 0;
  %>
<bean:define id="KmLinkMstrList" name="LINKS_LIST"  type="java.util.ArrayList" scope="session" />
 <logic:notEmpty name="KmLinkMstrList">
 
 
	<logic:iterate id="kmLinkMstrBean" name="KmLinkMstrList" indexId="i" type="com.ibm.km.dto.KmLinkMstrDto">
		<div class="quick-links" id="quick-links<%=(i+1)%>" style="">
        	<a class="button8" style="width:50px;" href="javascript:clickLink('<bean:write name="kmLinkMstrBean" property="linkPath"/>');">
        	<span style=" border-color: black">
        	<bean:write name="kmLinkMstrBean" property="linkTitle"/>
        	</span>
        	</a>
  		</div>
  		<%
	  		titleLength = kmLinkMstrBean.getLinkTitle().length(); 
	  		titleLength = titleLength * pixLen + padding;
	  		switch (i+1) 
	  		{
	  		case 1: len1 = initialIndex + ""; link2Index = Integer.parseInt(len1) + titleLength ;  break; 
	  		case 2: len2 = link2Index + ""; link3Index = Integer.parseInt(len2) + titleLength ; break;
	  		}
  		%>
  	</logic:iterate>
	
</logic:notEmpty>     
<script language="javascript">
function styleChange(){
document.getElementById("quick-links1").style.top="<%=(initialIndex+"")%>"+"px";
document.getElementById("quick-links2").style.top="<%=(link2Index+"")%>"+"px";
document.getElementById("quick-links3").style.top="<%=(link3Index+"")%>"+"px";
}
styleChange();
</script>
   