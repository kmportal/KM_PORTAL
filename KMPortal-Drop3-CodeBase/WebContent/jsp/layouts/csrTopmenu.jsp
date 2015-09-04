<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@ page 
language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
import="com.ibm.km.dto.KmUserMstr,com.ibm.km.dto.KmDocumentMstr"
%>

<logic:notEmpty name="USER_INFO">
<bean:define id="kmUserBean" name="USER_INFO"  type="KmUserMstr" scope="session" />
</logic:notEmpty>
<% String flag= (String)request.getAttribute("csrFirstLogin") ; %>
      <% if(flag==null) { flag="false" ; } 
       if(!flag.equals("true")) { %>
<logic:notEmpty name="CSR_HOME_BEAN" property="panIndiaCategoryMap">
<bean:define id="panIndiaCategoryMap" name="CSR_HOME_BEAN" property="panIndiaCategoryMap"  type="java.util.HashMap" scope="session" />
</logic:notEmpty>
<% } %>



<logic:notEmpty name="CSR_HOME_BEAN" property="lobList" >
<bean:define id="lobList" name="CSR_HOME_BEAN"  property="lobList" type="java.util.ArrayList" scope="session" />

</logic:notEmpty>
<SCRIPT>
function csrLogout()
{
opener.location.href="./Logout.do";
self.close();

}

var searchReq = getXmlHttpRequestObject();
var logoutRequest = getXmlHttpRequestObject();

//Gets the browser specific XmlHttpRequest Object
function getXmlHttpRequestObject() {
	if (window.XMLHttpRequest) {
		return new XMLHttpRequest();
	} else if(window.ActiveXObject) {
		return new ActiveXObject("Microsoft.XMLHTTP");
	} else {
		alert("Browser Doesn't Support AJAX");
	}
}

function handleOnClose() {
   if((window.event.clientX<0) ||(window.event.clientY<0)){
	logoutonBrowserClose();
	}	
  
}

function logoutonBrowserClose() 
{
	opener.location.href="./Logout.do";
	if(window.XmlHttpRequest) {
		logoutRequest = new XmlHttpRequest();
	}else if(window.ActiveXObject) {
		logoutRequest=new ActiveXObject("Microsoft.XMLHTTP");
	}
	if(logoutRequest==null) {
		alert("Browser Doesn't Support AJAX");
		return;
	}
		url="Logout.do";
		searchReq.open("GET",url,true);
		searchReq.send();
}

function refreshPage(){

if(!(window.location.pathname=="/KM/kmSearch.do" || window.location.pathname=="/KM/documentAction.do")){
window.location.href = window.location;
}

}

</SCRIPT>


<div class="navigation-left"></div>
    <div class="navigation-middle clearfix" style="z-index: 500;">
      <ul class="list4 clearfix fll">

 <%   if(!flag.equals("true")) { %>
           				

        <li class="first"><a href='./csrAction.do?methodName=viewCSRHome&fromHome=yes'>home </a></li>
      
       <% } %>  
      
       
       <logic:notEmpty name="CSR_HOME_BEAN" property="panIndiaCategoryMap">
       
					
						
				
	<% String flag1= (String)request.getAttribute("csrFirstLogin") ; %>
      <% if(flag1==null) { flag1="false" ; } 
       //if(!flag1.equals("true")) { %>
					<!--<logic:iterate id="element" name="panIndiaCategoryMap" indexId="ctr">
							<logic:lessThan value="6" name="ctr">
								<bean:define name="element" property="key" id="categoryElement"/>
								<li ><a href="kmDynamicIndexPage.do?methodName=loadCategories&firstIteration=false&categoryId=<bean:write name="categoryElement" property="value" />"><bean:write name="categoryElement" property="label" /></a>
								
								<ul class="child">
									<logic:iterate id="listElement" name="element" property="value" >
									<li ><a href="kmDynamicIndexPage.do&firstIteration=false&categoryId=<bean:write name="listElement" property="value" />"><bean:write name="listElement" property="label" /></a></li>
									</logic:iterate>
								</ul>
								</li>
							</logic:lessThan>
						</logic:iterate>
							--><!-- <% //} %>-->	
	  
		 </logic:notEmpty>
		 </ul>
						<ul class="flr right-nav1">
				<li><a href="javascript:history.back();">Back</a></li>
				<li><a href="javaScript:refreshPage();">Refresh</a></li>
				<li><a href="./documentAction.do?methodName=showHelpDocument">Help</a></li>
						</ul>
        </div>
        
        
        