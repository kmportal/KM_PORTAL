<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page session="false"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>airtel</title>
<link href="common/css/style.css" type="text/css" rel="stylesheet" />
<link href="common/font/stylesheet.css" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="jScripts/jquery-1.4.2.min.js"></script>

<!-- the jScrollPane script -->
<script language="javascript">

<%@ include file="/jScripts/capLock.js" %>
<!--//



function myPopup(url,windowname,w,h,x,y){
//window.open(url,'_blank',"resizable=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,width="+w+",height="+h+",left="+x+",top="+y+"");
	if (window.showModalDialog) {
		window.open(url,'dummydate',"resizable=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,modal=yes,location=no,width="+w+",height="+h+",left=5,top=5");
		//window.showModalDialog(url,'dummydate',"dialogWidth:1000px;dialogHeight:1000px");
	} else {
		window.showModalDialog(url,'dummydate',"dialogWidth:1000px;dialogHeight:1000px");
		//window.open(url,'dummydate',"resizable=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,modal=yes,location=no,width="+w+",height="+h+",left=5,top=5");
	}
}
//-->
</script>
<script> 


  
function callOnload()
{
	document.kmLoginForm.userId.focus();
}
function validate()
{
	
    var userid =dotandspace(document.getElementById("userId").value);
    var password =dotandspace(document.getElementById("password").value);
	
  if((document.kmLoginForm.userId.value=="")&&(document.kmLoginForm.password.value==""))
 	{
 	alert("Please Enter User Credentials ");
 	return false;
  	}
  	else if((document.kmLoginForm.userId.value=="User Name")&&(document.kmLoginForm.password.value=="Password"))
 	{
 	alert("Please Enter User Credentials ");
 	return false;
  	}
  	else if((document.kmLoginForm.userId.value=="")&&(document.kmLoginForm.userId.value.length==0))
 	{
 	alert("Please Enter User Credentials ");
 	return false;
  	}
  	else if((document.kmLoginForm.password.value=="")&&(document.kmLoginForm.password.value.length==0))
 	{
 	alert("Please Enter User Credentials ");
 	return false;
  	}
  	else if(userid==false)
  	{
  	alert("Please Enter User Credentials ");
  	return false;
  	}else if(password==false)
  	{
  	alert("Please Enter User Credentials ");
  	return false;
  	}  	
  	else
  	{
 	return true;
 	}
}

function dotandspace(txtboxvalue)
{
 var flag=0;
 var strText = txtboxvalue;
 if (strText!="")
 {
 var strArr = new Array();
 strArr = strText.split(" ");
 for(var i = 0; i < strArr.length ; i++)
 {
 if(strArr[i] == "")
 {
  flag=1;              
  break;
 }
 } 
 if (flag==1)
 {
 return false;
  }
 }        
}	  


</script>

<script type="text/javascript" language="javascript">   
function disableBackButton()
{
window.history.forward()
}  
disableBackButton();  
window.onload=disableBackButton();  
window.onpageshow=function(evt) { if(evt.persisted) disableBackButton() }  
window.onunload=function() { void(0) }  
</script>
</head>
<body onload="disableBackButton();"> 
<html:form action="/login" >
<input type="hidden" value="FALSE" name="CSR"/>
<div class="wrapper">
  <div class="logo-area">
    <div class="logo_iportal"><img src="<%=request.getContextPath()%>/common/images/iportel_animation.gif" width="158" height="44" alt="iportal" /></div>
    <div class="logo_airtel"><a href="http://www.airtel.in/wps/wcm/connect/airtel.in/airtel.in/home/"><img src="common/images/logo-airtel.jpg" alt="airtel" width="106" height="28" border="0" /></a></div>
  </div>
  <div class="navigation">
    <div class="navigation-left"></div>
    <div class="navigation-middle clearfix"> </div>
  </div>
   <div class="two-column-layout3 clearfix">
    <div class="column1"> 
    	<img src="<bean:message key="addBanner.bannerName.loginPage" />" alt="login-banner" height="328px" width="665px"/> </div>
     <div class="column2">
      <h1><img src="common/images/welcome-iportal.gif" /></h1>
       <div class="inner">
        <ul class="list16" style="margin-bottom:0px" >
		 <li style="margin-bottom:0px">
				<span>
				<strong> <html:errors  /></strong></span>
		 </li>
		 <li>
				<span>
				<strong> <html:errors name="errors.login.invalid_id"  /></strong></span>
		 </li>
		 <li>
				<span>
				<strong> <bean:write name="kmLoginForm" property="message" /></strong></span>
		 </li>         
         <li>               
          <span class="textbox12">
             <html:text name="kmLoginForm" property="userId" styleId="userId" maxlength="50" styleClass="textbox12-inner" value="User Name" onblur="if(this.value=='') this.value='User Name';" onfocus="if(this.value=='User Name') this.value='';" ></html:text>
            </span>
          </li>
          <li>         
           <span class="textbox12" >
            <html:password name="kmLoginForm"  styleId="password" maxlength="50" property="password"   redisplay="false" styleClass="textbox12-inner" value="Password" onblur="if(this.value=='') this.value='Password';" onfocus="if(this.value=='Password') this.value='';"></html:password>
           </span>
          </li>
          <li>
           	<html:submit styleClass="sign-in" style="font-size:0px;" onclick="return validate();"  property="submitId" styleId="submitId"></html:submit>
          </li>
            <li><a href="initForgotPassword.do?KmForgot=true">Forgot password</a></li>
        </ul>
        
        
      </div>
    </div>
  </div>
  <div class="footer">© airtel India 2012, All Rights Reserved.</div>
</div>

</html:form>
 
<script>
$(document).ready(function(){
    $(document).keydown(function(event) {
        if (event.ctrlKey==true && (event.which == '118' || event.which == '86')) {
            alert('Do not PASTE!');
            event.preventDefault();
         }
    });

});
</script>
</body>
</html>


