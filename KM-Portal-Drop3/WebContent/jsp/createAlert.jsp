<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="com.ibm.km.dto.KmAlertMstr" %>
<HEAD>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">
<LINK href="theme/text.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="jScripts/KmValidations.js" type="text/javascript"> </script>
<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
</script>
<script language="JavaScript" type="text/javascript">
function newXMLHttpRequest() {
    var xmlreq = false;
    if (window.XMLHttpRequest) {
        // Create XMLHttpRequest object in non-Microsoft browsers
        xmlreq = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        // Create XMLHttpRequest via MS ActiveX
        try {
            // Try to create XMLHttpRequest in later versions
            // of Internet Explorer
            xmlreq = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e1) {
            // Failed to create required ActiveXObject
            try {
                // Try version supported by older versions
                // of Internet Explorer
                xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e2) {
                // Unable to create an XMLHttpRequest with ActiveX
            }
        }
    }
    return xmlreq;
}

function loadPreviousMessage()
{
	currentCircleId = document.getElementById("currentCircleId").value;
	 if (currentCircleId != "") {
		req = newXMLHttpRequest();
	    if (!req) {
	        alert("Your browser does not support AJAX! " +
	              "Please check the KM requirements and contact your System Administrator");
	        return;
	    }
	    req.onreadystatechange = returnJson;
	    var url=path+"/kmAlertMstr.do?methodName=loadPreviousMessage&currentCircleId="+currentCircleId;
	    req.open("GET", url, true);
	    req.send(null);
	  }
}

function returnJson() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            var json = eval('(' + req.responseText + ')');
			var message = json.message;	
			document.getElementById("message").value = message; 
        }
    }
}

function formValidate()
{
	var reg=/^[0-9a-zA-Z_@*!$#%?><=,.:;|+*-{}(}[/\] ]*$/;
		  
		if(document.forms[0].kmActorId.value=='1'||document.forms[0].kmActorId.value=='5'){
			if(document.forms[0].circleId.value==""){
				alert("Please Select a Circle");
				return false;	
			}
		}	
		if(isEmpty(document.forms[0].message))
		{
			alert("Please Enter a Text for Alert" );
			return false;
		}else if(!reg.test(document.forms[0].message.value))
		{
		alert("Please avoid using apostrophes in message box");
		return false;	
	   }		
	document.forms[0].methodName.value = "insert";
	document.forms[0].submit();
}
function resetFields()
{	
    if(document.forms[0].kmActorId.value=='1'||document.forms[0].kmActorId.value=='5')
    {
	document.forms[0].circleId.value="";
	}
    document.forms[0].message.value="";
	return false;
}
function limitText(textArea, length) {
	if (textArea.value.length > length) {
	alert ("Please limit comments length to "+length+" characters.");
	textArea.value = textArea.value.substr(0,length);
	}
}
</script>
</HEAD>
<html:form action="/kmAlertMstr" >
<html:hidden name="kmAlertMstrFormBean" property="methodName" value="insert" />
<html:hidden property="kmActorId"/>
  <div class="box2">
   <div class="content-upload">
	<h1><bean:message key="alert.title" /></h1>
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
		<tr>
			<td colspan="2" align="center" class="error">
				<strong> 
         			<html:errors/>
           		</strong>
           	</td>
		</tr>
	 </TBODY>
	</TABLE>
	<ul class="list2 form1">
			<logic:notEqual name="kmAlertMstrFormBean" property="kmActorId" value="2">
				<logic:notEqual name="kmAlertMstrFormBean" property="kmActorId" value="3">
					<logic:notEmpty name="CIRCLE_LIST">
						<bean:define id="circleList" name="CIRCLE_LIST" type="java.util.ArrayList" scope="request"/>
					</logic:notEmpty>	
					<li class="clearfix alt">
		          		<span class="text2 fll width160"><strong>Circle  <font color="red">*</font></strong> </span>
						<html:select property="circleId" styleId="currentCircleId" name="kmAlertMstrFormBean" onchange="javascript:loadPreviousMessage();" styleClass="select1">
							<html:option value="">Select Circle</html:option>
							<logic:notEmpty name="CIRCLE_LIST">
								<bean:define id="circles" name="circleList"	 /> 
								<html:options labelProperty="elementName" property="elementId" collection="circles" />
							</logic:notEmpty>
						</html:select>
					 </li>
				</logic:notEqual>
			</logic:notEqual>
			<li class="clearfix">
          		<span class="text2 fll width160"><strong><bean:message key="alert.message"/></strong> </span>
				<html:textarea styleId="message" property="message" styleClass="textarea2" name="kmAlertMstrFormBean" cols="25" rows="3" onkeydown="limitText(this,150);"></html:textarea>
				<html:hidden property="messageId"/>
			</li>
			
			<li>
		      <span class="fll"> <input class="red-btn" style="margin-left: 160px;" value="Submit" alt="Submit" onclick="javascript:return formValidate();" readonly="readonly"/></span>
		      <span class="fll" style="margin-left: 20px;"> <input class="red-btn"  value="Clear" alt="Clear" onclick="javascript:return resetFields();" readonly="readonly"/></span>
		    </li>
		    <br> <br><br><br>
	</ul>
     
   </div>
  </div>
 
 <jsp:include page="Disclaminer.jsp"></jsp:include><br>
     
</html:form>

