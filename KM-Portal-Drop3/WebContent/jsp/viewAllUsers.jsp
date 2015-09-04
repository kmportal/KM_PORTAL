
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="theme/text.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">

<SCRIPT language="JavaScript" type="text/javascript">

function importExcel()
{


document.forms[0].methodName.value= "excelImport";
document.forms[0].submit();

}
function formSubmit(userId)
{

document.forms[0].selectedUserId.value=userId;
document.forms[0].methodName.value= "initEdit";
document.forms[0].submit();
return false;
}

function test()
{
	 	document.forms[0].methodName.value="viewUserList";
		return true;
}

function selectOption(){
	
	if(document.forms[0].kmActorId.value == "-1")
			{
				alert("Please Select the User Type");
				
			}
}
</SCRIPT>


<html:form action="/kmUserMstr" >
	<html:hidden property="methodName" />
	<html:hidden property="selectedUserId" />
	
	
	
					<div class="flr">		<img  src="images/excel.gif" width="39" height="35" border="0" onclick="importExcel();" /></div>
                <br><br><br>
      <div class="box2">               
		<div class="content-upload" style="margin-bottom: 0px;">
          <h1><bean:message key="viewAllUser.ViewUsers" /></h1>
        </div>
        
        
        
    <br><br>    
        <table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
        
		<li class="clearfix ">
				<span class="text2 fll width160"><strong>User Type<FONT color="red" size="1">*</FONT></strong></span>
				<html:select property="kmActorId" name="kmUserMstrFormBean" styleId="selectedActorId" styleClass="select1" >
						<option value="-1" >Select User Type</option>
						
						<logic:notEmpty name="kmUserMstrFormBean" property="actorList" >
									<bean:define id="elements" name="kmUserMstrFormBean" property="actorList" /> 
									<html:options labelProperty="kmActorName" property="kmActorName"  collection="elements" />
									
						
						</logic:notEmpty>
				</html:select>
		</li> 
		
		
		
		 <div class="button-area">
            <div class="button">
              <input class="submit-btn1 red-btn" type="submit" value="" onclick="selectOption(); return test();" />
            </div>
          
	  </div>
		
		
		
		<tr align="left">
			<td colspan="10"><strong><bean:write property="message"
				name="kmUserMstrFormBean" /></strong></td>
		</tr>
		<tr>
				<td colspan="10" align="center" class="error">
					<strong> 
          			<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
                          
             		</html:messages>
            		</strong>
            	</td>
			</tr>
        		        
        <tr class="textwhite">
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" width="1%"><span class="mTop5">
			&nbsp;SL.</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" width="15%"><span class="mTop5"><bean:message
				key="viewAllUser.UserLoginId" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" width="19%"><span class="mTop5"><bean:message
				key="viewAllUser.FirstName" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" width="15%"><span class="mTop5"><bean:message
				key="viewAllUser.LastName" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" width="15%"><span class="mTop5">
			  Mobile No.</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" width="15%"><span class="mTop5"><bean:message
				key="viewAllUser.EmailId" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" width="5%"><span class="mTop5"><bean:write 
			    name="kmUserMstrFormBean"	property="elementType" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" width="5%"><span class="mTop5"><bean:message
				key="viewAllUser.UserType" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" width="5%"><span class="mTop5"><bean:message
				key="viewAllUser.Status" /></span>&nbsp;</td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" width="5%"><span class="mTop5">Action</span></td>
		
		</tr>
        

            
		<logic:empty name="kmUserMstrFormBean" property="userList">
			<TR class="lightBg">
				<TD class="text" align="center"><bean:message
					key="viewAllUser.NotFound" /></TD>
			</TR>
		</logic:empty>
		
		<logic:notEmpty name="kmUserMstrFormBean" property="userList">
			<logic:iterate name="kmUserMstrFormBean" id="report" indexId="i" property="userList">
				<%
				String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				
				<tr class="<%=cssName %>">
					<TD class="txt" align="left"   style="word-wrap:break-word;"> &nbsp;<%=(i.intValue() + 1)%>.&nbsp;</TD>
					<TD class="txt" align="left"  style="word-wrap:break-word;"><bean:write name="report"
						property="userLoginId" /></TD>
					<TD class="txt" align="left"  style="word-wrap:break-word;"><bean:write name="report"
						property="userFname" /></TD>
					<TD class="txt" align="left" style="word-wrap:break-word;"><bean:write name="report"
						property="userLname" /></TD>
					<TD class="txt" align="left" style="word-wrap:break-word;"><bean:write name="report"
						property="userMobileNumber" /></TD>
					<TD class="txt" align="left" style="word-wrap:break-word;;"><bean:write name="report"
						property="userEmailid" /></TD>
					<TD class="txt" align="left" style="word-wrap:break-word;"><bean:write name="report"
						property="elementName" /></TD>
					<TD class="txt" align="left" style="word-wrap:break-word;"><bean:write name="report"
						property="userType" /></TD>
					<TD class="txt" align="left"  style="word-wrap:break-word;"><bean:write name="report"
						property="status" /></TD>
					<TD class="txt" align="left" style="word-wrap:break-word;"><A
						href="javascript: formSubmit('<bean:write name="report" property="userId"/>');" ><FONT color="red"><bean:message
						key="viewAllUser.edit" /></FONT></A></TD>
				
				</TR>
			</logic:iterate>
		</logic:notEmpty>
	
	</table>
	<br>
 </div>
</html:form>

