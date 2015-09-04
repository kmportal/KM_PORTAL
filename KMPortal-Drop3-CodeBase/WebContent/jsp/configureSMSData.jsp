<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	</head>
	<logic:notEmpty name="columnList" scope="request">
		<bean:define id="columnList" name="columnList" type="java.util.ArrayList" />
	</logic:notEmpty>

	<script>
		function formsubmit(){
			var id ="";
			var flag = "";
			var idValue="";
			var idFlag="";
			if(document.forms[0].docType.value == "1"){
				alert("Please Select the Document Type");
				return false;
			}
			for (var ii=0; ii<document.forms[0].isConfigurable.length; ii++){
				
				if(document.configureSMSDataFormBean.isConfigurable[ii].checked==false){
					flag=	'N';
				}
				else{
					flag = 'Y';
				}
				 id= document.configureSMSDataFormBean.columnId[ii].value;
				idValue = id+"#"+flag;
				idFlag = idFlag+idValue+"##";
				//alert(idFlag);
			}  
			document.getElementById("idFlag").value = idFlag;
			document.configureSMSDataFormBean.methodName.value="updateConfigurableColumnsForSMS";
			document.configureSMSDataFormBean.submit();
			return true;
		} 
		function fetchColumns(){
			document.configureSMSDataFormBean.selectValue.value=document.configureSMSDataFormBean.docType.value;
			document.configureSMSDataFormBean.methodName.value="fetchColumnForSMS";
			document.configureSMSDataFormBean.submit();
			return true;
			
		}
	</script>
	<body>
	<html:form action="/configureDataForSMS" enctype="multipart/form-data" onsubmit="return formsubmit();">
		<html:hidden property="methodName" value="configureDataForSMS"/>
		<html:hidden property="selectValue"/>
		<input type="hidden" name="idFlag" value="" id="idFlag">
		<div class="box2">
        	<div class="content-upload">
                <H1>Configure Data For SMS</H1>
			     <table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
					<tr>
						<td colspan="2" align="center" class="error">
							<font color="blue"><strong><html:messages id="msg" message="true"><bean:write name="msg"/></html:messages></strong></font>
					    </td>
					</tr>
					<tr>
					  <td colspan="2" align="center" class="error">
					  		<strong><font color=red> <html:errors /></font></strong>
					  </td>
					  <td></td>
					</tr> 
				  </table>
                <ul class="list2 form1 ">
			 		<li class="clearfix">
						<span class="text2 fll width160">
							<strong>Select Document Type:</strong> 
						</span>
						<p class="clearfix fll margin-r20"> 
								<html:select  property="docType" styleClass="select1 fll width180" onchange="return fetchColumns();">
									<html:option value="1">-- Select Document Type --</html:option>
									<html:options collection="docTypeList" name="docTypeList" property="docTypeName" labelProperty="docTypeName" />
								</html:select>
				
			 			</p>
					</li>
				
					<div class="box1 header1">
						<!-- <table width="100%" cellpadding="5" cellspacing="0" border="0">-->
	            			<ul class="list1 clearfix">
	              				<li style="width:345px;">Column Names</li>
	              				<li>isConfigurable</li>
	            			</ul>
	            		
          			</div>
          			<logic:empty name="columnList" >
						<bean:message key="table.column.not.found" />
						
					</logic:empty>
					<table width="100%" cellpadding="5" cellspacing="0" border="0" colspan = "2"  id="dataTable">
						<logic:notEmpty name="columnList">
							<bean:define id="columnList" name="columnList" type="java.util.ArrayList" scope="request" />
							<logic:iterate name="columnList" id="columnList1" indexId="i" type = "com.ibm.km.dto.KmConfigureDataForSMSDto">
								
								<tr> 
									<td align="left" class="pBot2" width="258">
										<p class="clearfix fll margin-r20">
				                			<span class="text2 fll" style="width:120px; font-weight:normal; color:#181818;">
				             					<bean:write name="columnList1" property="columnName"/>
				             					<html:hidden name="columnList1" property="columnId" />
				             				</span>
			            				</p>
		            				</td>
		            				<td align="right" class="pBot2" width="258">
		            					<p class="clearfix fll margin-r20">
			             					<span class="textbox6">
					                 			<span class="textbox6-inner">
					                 				<% String val = columnList1.getIsConfigurable();
					                 					if(val.equals('Y')){
					                 				 %>
					                 				<html:checkbox name="columnList1"  property="isConfigurable" value="N" /></b>
					                 				<%}else{ %>
					                 					<html:checkbox name="columnList1"  property="isConfigurable" value="Y" /></b>
					                 				<%} %>
					                       			<!--<html:text name="columnList1" property="isConfigurable" />
					                       		--></span>
					                   		</span>
					                	</p>     
					                </td>                           
		            			</tr>
							</logic:iterate>
						</logic:notEmpty>
					</table>
					<logic:notEmpty name="columnList">
	                	<li class="clearfix" style="padding-left:170px;">
							<span class="text2 fll">&nbsp;</span>
							<input type="image" src="images/submit.jpg" style="margin-right:10px;" onclick="return formsubmit();" styleClass="red-btn fll"/>&nbsp;&nbsp;&nbsp;&nbsp;	
						</li>
					</logic:notEmpty>
					
					
				</ul>
			</div>
		</div>
	</html:form>
	</body>
</html>