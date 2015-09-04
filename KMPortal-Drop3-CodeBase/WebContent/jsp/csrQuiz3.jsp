<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>

<bean:define id="kmUserBean" name="USER_INFO"  type="KmUserMstr" scope="session" />

<%@ page 
language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"
import="com.ibm.km.dto.KmUserMstr,com.ibm.km.dto.KmDocumentMstr"
%>
<link href="common/css/style.css" type="text/css" rel="stylesheet" />
<link href="common/font/stylesheet.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="jScripts/DateTime.js"></script>		
<script type="text/javascript" src="WebContent/common/js/jquery.mousewheel.js"></script>
<script type="text/javascript" src="WebContent/common/js/jquery.jscrollpane.min.js"></script>
<script type="text/javascript">
$(function()
{
	$('.scroll-pane').jScrollPane(
	);
});


function csrLogout()
{
opener.location.href='./Logout.do';
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


function timer1(){
	
	window.setTimeout('showSessionAlert()',1505000);
	
	
}
function showSessionAlert(){

	if(confirm("Your session will be expired within 5 minutes. You want to open a new session ?")){
		window.location.href = window.location;
	}	
	else{
			try{
					window.opener.location.href = "Logout.do"
					self.close();
				}
				catch(e){
					self.close();
				}
	}
		
}

timer1();
function getSelectedValue(answer)
{
	for(var i=0; i<answer.length;i++)
	{
		if(answer[i].checked)
			return answer[i].value;
	}
}
function formValidate()
{
	
var answer=document.forms[0].answer;
	
	if(getSelectedValue(answer) == null)
	{
		alert("Please select  Answer");
		return false;
	}	
	document.forms[0].methodName.value = "thirdQuestion";
	document.forms[0].submit();	
}

function formSkip()
{
	document.forms[0].methodName.value = "thirdSkip";
	document.forms[0].submit();	
}

</SCRIPT>


<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style>

  <div class="wrapper">
      <div class="logo-area">
    	<div class="logo_iportal"><img src="common/images/iportel_animation.gif" width="158" height="44" alt="iportal" /></div>
    	<div class="logo_airtel"><a href="http://www.airtel.in/wps/wcm/connect/airtel.in/airtel.in/home/"><img src="common/images/logo-airtel.jpg" alt="airtel" width="106" height="28" border="0" /></a></div>
  	  </div>
  	  
      <div class="admin">
		<div class="admin-content"><h6>welcome <span><bean:write name='kmUserBean' property="userFname" /></span></h6>&nbsp;&nbsp;
			<span class="white10bold">Time :&nbsp;</span><span class="white10" id="clock"><SCRIPT language="javascript" src="./jScripts/DateTime.js"></SCRIPT> <SCRIPT>getthedate();dateTime();</SCRIPT> </span>
		</div>
		<div class="flr">
		<a href="./Logout.do"><img src="common/images/logout.png" alt="Logout" height="25" width="25" border="0"  /></a>
		</div>
	  </div>
	  
	<div class="navigation">
	<div class="navigation-left"></div>
	<div class="navigation-middle"></div>
	<div class="navigation-right"></div></div>
	
	          <div class="starting-line">iPortal is the central repository for all the documents required by CSR for Customer Help.</div><br>
         <html:form action="/kmBriefingMstr" name="kmBriefingMstrFormBean" type="com.ibm.km.forms.KmBriefingMstrFormBean">
         <html:hidden property="methodName" value="" />
         <html:hidden property="questionId" />
         <div class="box2">
     <div class="content-upload">
	
	<TABLE align="center"> 
			<TBODY>
			<tr>
				<td colspan="2" align="center" class="error">
					<strong> 
          			<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
                          
             		</html:messages>
            		</strong>
            	</td>
			</tr>
				
			
		</TBODY>
	</TABLE>
			<ul class="list2 form1">
	
         	<li class="clearfix alt">
				<strong><font color="red" size="3">Question 3:</font></strong>
			<font size="3" color="red">&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<storng> <bean:write name="kmBriefingMstrFormBean" property="question" /></storng></font>
		<li>
		<input type="radio" name="answer"     value="<bean:write name="kmBriefingMstrFormBean" property="firstAnswer" />" />&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<bean:write name="kmBriefingMstrFormBean" property="firstAnswer" />
		</li>
			<li>
		<input type="radio" name="answer"     value="<bean:write name="kmBriefingMstrFormBean" property="secondAnswer" />" />&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<bean:write name="kmBriefingMstrFormBean" property="secondAnswer" />
		</li>
		<li>
		<input type="radio" name="answer" value="<bean:write name="kmBriefingMstrFormBean" property="thirdAnswer" />" />&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<bean:write name="kmBriefingMstrFormBean" property="thirdAnswer" />
		</li>
		<li>
		<input type="radio" name="answer" value="<bean:write name="kmBriefingMstrFormBean" property="fourthAnswer" />" />&nbsp; &nbsp; &nbsp; &nbsp; &nbsp;<bean:write name="kmBriefingMstrFormBean" property="fourthAnswer" />
		</li>
			
		<li>              
      <div class="button-area">
	            <div class="button"><a  class="red-btn" onclick="javascript:return formValidate();">NEXT</a></div>
	             <div class="button"><a  class="red-btn" onclick="javascript:return formSkip();">SKIP</a></div>
	       
	      </div>   
         </ul></div></div>
         </html:form>
         
      <div class="starting-line">iPortal is the central repository for all the documents required by CSR for Customer Help.</div>
      <div class="footer">© airtel India 2012, All Rights Reserved.</div>


