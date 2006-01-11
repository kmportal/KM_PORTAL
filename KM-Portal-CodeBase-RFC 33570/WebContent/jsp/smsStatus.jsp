<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!-- Page Created by ABU to display RC SMS status -->

<form>
<bean:define id="kmRCContentUploadFormBean" name="kmRCContentUploadFormBean" type="com.ibm.km.forms.KmRCContentUploadFormBean" scope="request"/>
	<br/><br/>
<div id="elementLabel">
<%String smsStatus = kmRCContentUploadFormBean.getSmsStatus();
String color="";
System.out.print("SMS Status in Status page    ------     "+ smsStatus);
if(smsStatus!=null){
if(smsStatus.equals("SMS Successfully sent to the user...!!!")){
	color = "green";	
	}else{ 
	 color = "red";
	}
	} %>
	
</div>

<font color = <%=color%>><strong><bean:write name="kmRCContentUploadFormBean" property="smsStatus" scope="request"/> </strong></font>

<form>