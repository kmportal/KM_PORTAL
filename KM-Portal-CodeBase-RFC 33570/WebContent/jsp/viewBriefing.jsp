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

/*$(function()
{
	$('.scroll-pane').jScrollPane(
	);
});
*/
 window.history.forward(1);

function searchSubmit(){
	var date=document.getElementById("briefingDate").value;
	window.location.href="kmBriefingMstr.do?methodName=viewCSRBriefing&briefingDate="+date;
	
}

function csrLogout()
{
opener.location.href='./Logout.do';
self.close();

}
function viewCSRHome()
{
var ud=document.getElementById("ud").value;

if(document.getElementById("ud")!=null && document.getElementById("ud").value=='UDlogin/')
{

window.location.href="csrAction.do?methodName=viewCSRHome1";
}
	else if (confirm("I have read and understood the briefing for the day and will abide by it. Thanks."))
	{
		window.location.href="csrAction.do?methodName=viewCSRHome";
	}
	else
	{
		return false;
	}
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

</SCRIPT>

<bean:define id="briefingDate" name="briefingDate" type="java.lang.String"	scope="request" /> 
<%String UD=(String)request.getAttribute("ud"); %>
<input type="hidden" id="ud" value=<%=UD%>/>
<!-- Code change after UAT observation -->

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
	<div class="navigation-middle">
	
	</div>
	<div class="navigation-right"></div>
	</div>
	          <div class="starting-line">iPortal is the central repository for all the documents required by CSR for Customer Help.</div>
          
      <div class="inner-content">
         <br><h2 class="new">Welcome to KM</h2><p>
         <div class="two-column-layout1 clearfix">
		    <h3  style = "font-family:Arial;" >Briefings</h3><br>
		    
		  <logic:present name="BRIEFING_LIST">
		   <logic:iterate name="BRIEFING_LIST" id="briefing" type="com.ibm.km.forms.KmBriefingMstrFormBean">
		      <bean:define name="briefing" property="count" id="count"/>
               <div class="column1 box6 fll" style="margin-right:20px; ">
			      <h3 class="heading1"><bean:write name="briefing" property="briefingHeading"/>  </h3>
                	<ul class="list8" style ="padding-top:0px;"> 
							<c:forEach begin="0" end="${count}" var="i" step="1" >
							
							<pre style="font-size: medium; word-wrap: break-word">
							<br><bean:write name="briefing" property='<%="briefingDetails[" + ((Integer)(pageContext.getAttribute("i"))).intValue()+ "]"%>'/>
							</pre>
							</c:forEach>
					</ul>
					<span style="font-size:12px; font-weight:bold;  ">Created : <bean:write name="briefing" property="createdDt"/> </span>
				</div>
            </logic:iterate>

            
          <logic:empty name="BRIEFING_LIST">
	          <ul class="list8"> <li>
	          <FONT color="red"><B>No Briefing Found for the Day</B></FONT>
	          </li>
	          <br/><br/><li>

</li>
</ul>
	          <br><br>
          </logic:empty>          
		</logic:present>	

		</div>
			
		<p align="right" style="margin-bottom:50px;"><a href="./kmBriefingMstr.do?methodName=CSRQuiz" ><strong><font size="6"><img alt="" src="images/continueQuiz.jpg" border="0"/></font></strong></a></p>
	    <p align="right" style="margin-bottom:50px;"><a href="#" onclick="return viewCSRHome();">&nbsp;<strong><font size="6"><img alt="" src="images/continue.jpg" border="0"/></font></strong></a></p>
          </div>
      
      <div class="starting-line">iPortal is the central repository for all the documents required by CSR for Customer Help.</div>
      <div class="footer">� airtel India 2012, All Rights Reserved.</div>
  </div>

