<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@page import="java.util.ArrayList" %>
<%@page import="com.ibm.km.dto.KmLinkMstrDto" %>

<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />
<style type="text/css"> 
		.HL {background: #ffff99; 
		color: #000000;}  
</style>  
<script language="javascript">
function highlight(container,what,spanClass) {
		var content = container.innerHTML;
		var	pattern = new RegExp('(>[^<.]*)(' + what + ')([^<.]*)','g');
		var replaceWith = '$1<span ' + ( spanClass ? 'class="' + spanClass + '"' : '' ) + '>$2</span>$3';
		var highlighted = content.replace(pattern,replaceWith);
    return (container.innerHTML = highlighted) !== content;
}
var searchReq = getXmlHttpRequestObject();
var logoutRequest = getXmlHttpRequestObject();

function newWindow(url){
		window.open(url,'_blank',"resizable=yes,location=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,width=750,height=600,left=10,top=10");
}


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
   if (event.clientY < 0) {
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

var message ="This action is not Allowed!";


	

function click(e) {
if (document.all) {
if (event.button == 2) {
 alert(message)
 return false;
}
}
if (document.layers) {
if (e.which == 3) {
 alert(message)
 return false;
}
}
}
if (document.layers) {
document.captureEvents(Event.MOUSEDOWN);
}
document.onmousedown=click; 

//sessionUpdate();

function timer(){
	
	showAlert();
	
	window.setTimeout('timer3()',1200000);
	
	
}
timer();
function timer3(){
	
	showAlert();
	
	//window.setTimeout('timer()',10000);
	//window.setTimeout('timer()',1200000);
	
	
}


/*function timer1(){
	
	showSessionExpiryAlert();
	
	window.setTimeout('timer1()',1505000);
	
	
}*/

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



function showSessionExpiryAlert()
{
	
	
	
	var url="kmAlertMstr.do?methodName=sessionExpiryAlert";
	if(window.XmlHttpRequest) {
		req = new XmlHttpRequest();
		
	}else if(window.ActiveXObject) {
		req=new ActiveXObject("Microsoft.XMLHTTP");
		
	}
	if(req==null) {
		alert("Browser Doesnt Support AJAX");
		return;
	}
	req.onreadystatechange = getSessionAlert;
	req.open("POST",url,true);
	req.send();
	
}


function showAlert()
{
	

	
	var url="kmAlertMstr.do?methodName=showAlertMessages";
	if(window.XmlHttpRequest) {
		req1 = new XmlHttpRequest();
		
	}else if(window.ActiveXObject) {
		req1=new ActiveXObject("Microsoft.XMLHTTP");
		
	}
	if(req1==null) {
		alert("Browser Doesnt Support AJAX");
		return;
	}
	
	req1.onreadystatechange = getAlertMessages;
	
	req1.open("POST",url,true);
	req1.send();
	

	
	
}




function sessionUpdate()
{
	var url;
	url="kmAlertMstr.do?methodName=sessionUpdate";
	if(window.XmlHttpRequest) {
		req = new XmlHttpRequest();
		
	}else if(window.ActiveXObject) {
		req=new ActiveXObject("Microsoft.XMLHTTP");
		
	}
	if(req==null) {
		alert("Browser Doesnt Support AJAX");
		return;
	}
	req.onreadystatechange = sessionMessage;
	req.open("POST",url,true);
	req.send();
	
}

function sessionMessage()
{
	
	if (req.readyState==4 || req.readyState=="complete") {
		
		 //alert(req.responseText);
	}	
		

}


function getAlertMessages()
{
	
	
	if (req1.readyState==4 || req1.readyState=="complete") {
		
		var alertMessages=req1.responseText;
		if(alertMessages=="none")
		{
			
		}
		else{
	
		
			if(alertMessages.length<1000)
			{
				
				alert(alertMessages);
			}
		}
	}	
}		
		
function getSessionAlert()
{
	

	if (req.readyState==4 || req.readyState=="complete") {
		sessionExpiry=req.responseText;
		if(sessionExpiry=="none")
		{
	
		}
		else if(sessionExpiry=="")
		{
			self.close();
		}
		else{
		
			if(sessionExpiry.length<1000)
			{
				if(confirm(sessionExpiry)){
				window.location.href = window.location;
				
								
				}else{
				self.close();
					//window.location.href="Logout.do";
				}
			}
		}
	}	
}

function clickLink(url){
		window.open(url,'_blank',"resizable=yes,location=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,width=750,height=300,left=10,top=10");
}



</script>




<div class="logo-area">
<div class="logo_iportal">
    	<img src="<%=request.getContextPath()%>/common/images/iportel_animation.gif" width="158" height="44" alt="iportal" />
    </div>

    <div class="logo_airtel">
    	<a href="http://www.airtel.in/wps/wcm/connect/airtel.in/airtel.in/home/"><img src="common/images/logo-airtel.jpg" alt="airtel" width="106" height="28" border="0" /></a>
    </div>
    </div>
    
<div class="admin clearfix">
    <div class="admin-content">
      <h6>welcome <span><bean:write name="kmUserBean"	property="userFname" /></span></h6>
      &nbsp;| last login :  <span class="white10" id="clock"><SCRIPT language="javascript" src="./jScripts/DateTime.js"></SCRIPT> <SCRIPT>getthedate();dateTime();</SCRIPT> </span></div>
      <ul class="flr right-nav clearfix">
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
        <li style="border:0px;height:15px"><a href="javascript:clickLink('http://www.airtel.in');">Airtel Home</a></li>
           <li style="border:0px;height:15px"><a href="javascript:clickLink('http://www.airtel.in/wps/wcm/connect/airtel.in/airtel.in/home/foryou/mobile/prepaid+services/roaming/international/');">International Roaming</a></li>
        <li style="border:0px;height:15px"><a href="javascript:clickLink('http://www.airtel.in/wps/wcm/connect/about+bharti+airtel/Bharti+Airtel/Contact+Us/');">Reach us</a></li>
        -->
        
    <li class="last"><a href="./Logout.do"><img src="common/images/logout.png" alt="" height="21" width="25" style="border:0px"/></a></li>
      </ul>
  </div>

