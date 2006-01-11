<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page 
language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
import="com.ibm.km.dto.KmUserMstr,com.ibm.km.dto.KmDocumentMstr"
%>
<style >
.legend
{
color:white;
background-color:#DB0909;
font-size:12px;
display:inline-block;
padding-left:3px;
}
</style>
    
<bean:define id="kmUserBean" name="USER_INFO"  type="KmUserMstr" scope="session" />
 <% String flag= (String)request.getAttribute("csrFirstLogin") ; %>
      <% if(flag==null) { flag="false" ; } 
       if(!flag.equals("true")) { %>
      <h1> ____ _______</h1>
      <div id="right-frame2">
        <ul id="right-frame2-ul" class="scroll-pane rite-side" >
				<logic:iterate id="moduleMap" name="kmUserBean" property="moduleList">
	                <% java.util.HashMap mMap=(java.util.HashMap)moduleMap;			
					%>
					  <% if(kmUserBean.isRestricted()==true){ %>                
	          
	                   <li>
	                   <a href='<bean:write name="moduleMap" property="MODULE_URL"/>' class="aria_11_black"><bean:write name="moduleMap" property="MODULE_NAME"/></a>
	                   </li>
	              
	                <%} else{ if(mMap.get("STATUS").equals("A") || mMap.get("STATUS").equals("B")){ %>
	                <li> <a href='<bean:write name="moduleMap" property="MODULE_URL"/>' class="aria_11_black"><bean:write name="moduleMap" property="MODULE_NAME"/></a>
	              </li>	                
	                <%}} %>	                
				</logic:iterate>
	</ul>
	</div>
 <% } %> 
 