<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="theme/css.css" rel="stylesheet" type="text/css">
<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">

<logic:notEmpty name="ALERT_LIST">
<bean:define  id="alertList" name="ALERT_LIST" type="java.util.ArrayList" scope="request"/>
</logic:notEmpty>


<SCRIPT>
var count;
var rowEdited;
function method(){
count= parseInt(document.kmAlertMstrFormBean.alertCount.value);
}

function alertEdit(msgId,message,text,edit,done)
{
var reg=/^[0-9a-zA-Z_@*!$#%?><=,.:;|+*-{}(}[/\] ]*$/;
 rowEdited=text;
  if(document.getElementById(edit).value=="EDIT"){
	
 document.getElementById(edit).value="DONE"
 document.getElementById(text).disabled=false;
 document.getElementById(edit).disabled = true;
  for(var i=1;i<=count;i++)
   {
    if(i!==text)
      {
       document.getElementById("edit" +i).disabled = true;
      }
   }
   document.getElementById(edit).disabled=false;
   }
   else{
    
   	var msg = document.getElementById(text).value;
   	if(msg=="")
   	{
   	alert("Blank  Alert is not Accepted");
   	return false;
   	}else if(!reg.test(msg))
		{
		alert("Please avoid using apostrophes in message box");
		return false;	
	   }	
   	else{
   	
   	document.getElementById(edit).value="EDIT"
   	var url="kmAlertMstr.do?methodName=editAlerts&&messageId="+msgId+"&message="+msg;
	if(window.XmlHttpRequest) {
		req = new XmlHttpRequest();
		
	}else if(window.ActiveXObject) {
		req=new ActiveXObject("Microsoft.XMLHTTP");
		
	}
	if(req==null) {
		alert("Browser Doesnt Support AJAX");
		return;
	}
	req.onreadystatechange = getOnChange;
	req.open("POST",url,true);
	req.send();
   }
   }
 }
/* function alertDone(msgId,message,row)
 {
 var msg = document.getElementById(row).value;
 document.kmAlertMstrFormBean.action="/KM/kmAlertMstr.do?methodName=editAlerts&&messageId="+msgId+"&message="+msg;
 document.kmAlertMstrFormBean.submit();
 }  */

function getOnChange()
{
	
	if (req.readyState==4 || req.readyState=="complete") 
	{
		var alertResponse=req.responseText;
		
		if(parseInt(alertResponse)== -1){
			alert("Alert not edited. Please edit again");
		}
		 for(var i=1;i<=count;i++)
  		 {
    
     		document.getElementById("edit" +i).disabled = false;
    		 document.getElementById(i).disabled=true;
   		}

	}
	

}

function listFiles()
{
var circleId=document.kmAlertMstrFormBean.selectedCircleId.value;
if(circleId=="")
{
alert("Please Select the Circle");
}
else{
document.kmAlertMstrFormBean.methodName.value="viewAlerts";
document.kmAlertMstrFormBean.submit();
}
}
</SCRIPT>
<script language="JavaScript" type="text/JavaScript">
function limitText(textArea, length) {
if (textArea.value.length > length) {
alert ("Please limit comments length to "+length+" characters.");
textArea.value = textArea.value.substr(0,length);
}
}
</script>

<html:form action="/kmAlertMstr" >
<html:hidden name="kmAlertMstrFormBean" property="methodName" /> 
<html:hidden name="kmAlertMstrFormBean" property="kmActorId" />	
<html:hidden name="kmAlertMstrFormBean" property="alertCount" />	
	<div class="box2">
       <div class="content-upload">
		<h1><bean:message key="alert.title.edit" /></h1>
		<table width="100%" class="mTop10" align="center">
          <logic:equal name="kmAlertMstrFormBean" property="kmActorId" value="1">
            <tr>
			  <td colspan="2">
				<table id="table_0" class="form1">
				 <tbody>
				  <tr class="lightBg" >
					<th width="160" class="text" >&nbsp;&nbsp;<bean:message key="alertHistory.circle"/><FONT color="red">*</FONT></Th>
				    <TD><html:select property="selectedCircleId" styleClass="select1" name="kmAlertMstrFormBean" >
						  <html:option value="">Select Circle</html:option>
							<logic:notEmpty name="kmAlertMstrFormBean" property="circleList">
							<bean:define id="circle" name="kmAlertMstrFormBean"	property="circleList" /> 
							<html:options labelProperty="elementName" property="elementId" collection="circle" />
							</logic:notEmpty>
						</html:select>
				 	</TD>  
				 </tr>
				</tbody>
			</table>
			</td>
		</tr>
		<tr align="center" >
			<td width="636" class="pTop2" colspan="2"><img  src="images/submit.jpg"  onclick="listFiles();"/></td>
		</tr>
		<tr>
			<td colspan="2" class="pTop4 pLeft10">
				<table width="100%" border="0" cellpadding="0" cellspacing="0" class="text">
					<tr align="left">
						<td class=""><font color="#0B8E7C"><strong>
						<html:messages id="msg" message="true">
 								<bean:write name="msg"/>  
						</html:messages></strong></font></td>
					</tr>
				</table>
			 </td>
			</tr>
			<tr>
			 <td colspan="2" class="pTop4 pLeft10">
			  <table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
				<tr align="left">
				<td class=""><font color="#0B8E7C"><strong>
				<html:errors/></strong></font>
				</tr>
			  </table>
			</td>
	       </tr>
		</logic:equal>
		  <logic:equal name="kmAlertMstrFormBean" property="kmActorId" value="5">
            <tr align="center">
				<td colspan="2">
				<table id="table_0" class="form1">
					
					<tbody>
					<tr  align="center" class="lightBg" >
					<th  align="right" class="text"><BR><bean:message key="alertHistory.circle" /> <FONT color="red"> *</FONT>&nbsp;&nbsp;</Th>
				    <TD  colspan="2" align="left"><BR>&nbsp;
					<html:select property="selectedCircleId" styleClass="select1" name="kmAlertMstrFormBean" >
					<html:option value="">Select Circle</html:option>
					<logic:notEmpty name="kmAlertMstrFormBean" property="circleList">
					<bean:define id="circle" name="kmAlertMstrFormBean"	property="circleList" /> 
					<html:options labelProperty="elementName" property="elementId" collection="circle" />
					</logic:notEmpty>
					</html:select>
				 	</TD>  
		</tr>
														
					</tbody>
				</table>
				</td>
			</tr>
			<tr align="center"  >
				<td width="636" class="pTop2" colspan="2"><img  src="images/submit_button.jpg"  onclick="listFiles();"/></td>			
			</tr>
			<tr>
				<td colspan="2" class="pTop4 pLeft10" height="25">
				<table width="100%" border="0" cellpadding="5" cellspacing="0"
					class="text">
					<tr align="left">
						<td class=""><font color="#0B8E7C"><strong> <html:messages
							id="msg" message="true">
							<bean:write name="msg" />
						</html:messages></strong></font></td>
					</tr>
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="2" class="pTop4 pLeft10">
					<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
					<tr align="left">
					<td class=""><font color="#0B8E7C"><strong>
					<html:errors/></strong></font>
			</tr>
		  </table>
		 </td>
		</tr>
		</logic:equal>
	</table>	
  <table width="100%" class="mTop10 form1" align="center" cellpadding="0" cellspacing="0">
	<logic:present name="ALERT_LIST" >
        <tr class="text white">
				<td width="6%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mTop5">S.No.</span></td>
				<td width="12%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mTop5">Date</span></td>
				<td width="10%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mTop5">Circle</span></td>
				<td width="20%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mTop5">Time of Alert Creation</span></td>
				<td width="15%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mTop5">Alert Source</span></td>
				<td width="30%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35">Alert Script</td>
				<td width="7%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35">Action</td>
		</tr>
        <logic:notEmpty name="ALERT_LIST">
		 <logic:iterate name="alertList" id="file" indexId="i" type="com.ibm.km.dto.KmAlertMstr">
		 	<%	String cssName = ""; if( i%2==1){cssName = "alt";}%>
			 <TR class="<%=cssName%>">	
				<TD class="text" align="left">&nbsp;<%=(i.intValue() + 1)%>.</TD>				
				<TD class="text" align="left"><bean:write name="file" property="createdDt" /></TD>
				<TD class="text" align="left"><bean:write name="file" property="circleId" /></TD>
				<TD class="text" align="left"><bean:write name="file" property="createdTime" /></TD>
				<TD class="text" align="left"><bean:write name="file" property="alertSource" /></TD>
                <TD align="left"><html:textarea styleClass="textarea1" styleId='<%=(i.intValue() + 1)+""%>'   name="file" property="message" disabled="true" onkeydown="limitText(this,250);"/></td>
             
                <logic:notEqual name="file" property="alertSource" value="GUI">
                <TD><input type="button"  style="display: none"  id='<%="edit"+(i.intValue() + 1)+""%>' value="EDIT"  onclick="javascipt:alertEdit('<bean:write name="file" property="messageId" />','<bean:write name="file" property="message" />','<%=(i.intValue() + 1)%>','<%="edit"+(i.intValue() + 1)%>','<%="done"+(i.intValue() + 1)%>');"></TD>
                </logic:notEqual>
                <logic:equal name="file" property="alertSource" value="GUI">
                <TD><input type="button" id='<%="edit"+(i.intValue() + 1)+""%>' value="EDIT"  onclick="javascipt:alertEdit('<bean:write name="file" property="messageId" />','<bean:write name="file" property="message" />','<%=(i.intValue() + 1)%>','<%="edit"+(i.intValue() + 1)%>','<%="done"+(i.intValue() + 1)%>');"></TD>
				</logic:equal>
		    </TR>
		  </logic:iterate>
		</logic:notEmpty>
	     <logic:empty name="ALERT_LIST"  >
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
		</logic:empty>
	</logic:present>	
  </table>
 <script>
 method();
</script>
</div>
</div>
<jsp:include page="Disclaminer.jsp"></jsp:include>
</html:form>
