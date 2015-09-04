
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script language="JavaScript" src="jScripts/KmValidations.js" type="text/javascript"> </script>
<script language="JavaScript" >
function formValidate(num){

	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;//January is 0!
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
	var count=num;
	var i=0;
	
	if(document.forms[0].briefingHeading.value=="")
	{
		alert("Please Enter Briefing Heading");
		document.forms[0].briefingHeading.focus();
		return false;
	}
	if(document.forms[0].displayDt.value=="")
	{
		alert("Please Select a Display Date");
		return false;
	}
	if(document.forms[0].displayDt.value<curr_dt)
	{
		alert("Display date should not be a past date");
		return false;
	}
	if(document.forms[0].displayDt.value<curr_dt)
	{
		alert("Display date should not be before today");
		return false;
	}

	/*	while(i<=count1){
			if(document.getElementByName("briefingDetails["+i+"]").value==""){
				alert("Briefing cannot be blank");
				document.getElementById("briefingDetails["+i+"]").focus();
				return false;	
			}
			
			var charBrief=i+1;
			length=300;
			if (document.getElementById("briefingDetails["+i+"]").value.length > length) {
			alert ("Please limit Briefing No :"+charBrief + "\n to "+length+" characters.");
	
			return false;
			}
			
			i++;
			
		}	*/

	document.forms[0].methodName.value="submitUpdatedBriefing";
	document.forms[0].submit();
	// return true;	
}
	
function limitText(textArea) {
var length=300;
if (textArea.value.length > length) {
alert ("Please limit Briefing to "+length+" characters.");
//textArea.value = textArea.value.substr(0,length);
}
}
function handleEnter (field, event) {
		var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		if (keyCode == 13) {
			var i;
			for (i = 0; i < field.form.elements.length; i++)
				if (field == field.form.elements[i])
					break;
			i = (i + 1) % field.form.elements.length;
			return false;
		} 
		else
		return true;
}   

function validate(){

var updateError= '<%= (String)request.getAttribute("UPDATE_ERROR") %>';
if (updateError !='null'){
	alert("All Briefing details cannot be blank");
}
}

validate();
</script>



<html:form action="/editBriefings">
<html:hidden name="kmBriefingMstrFormBean" property="methodName"  /> 
<div class="box2">
        <div class="content-upload">
		<h1>Update Briefings</h1>
	  <table class="mTop30" align="center">
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
		</table>
		 <ul class="list2 form1 ">
		<li class="clearfix">
			<span class="text2 fll width160"><strong>Briefing ID</strong></span>
				<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text property="briefingId" readonly="true" name="kmBriefingMstrFormBean" styleClass="textbox7"/></span> </span> </p>
		</li>
			<li class="clearfix alt">
			<span class="text2 fll width160"><strong>Briefing Heading</strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text property="briefingHeading"  name="kmBriefingMstrFormBean" styleClass="textbox7"/></span> </span> </p>
			</li>
			<li class="clearfix">
			<span class="text2 fll width160"><strong>Publishing Date</strong></span>
			  <input type="text" class="tcal calender2 fll" readonly="readonly" name="displayDt" value="<bean:write property='displayDt' name='kmBriefingMstrFormBean'/>"/>
            		
                	 
			</li>
			<li class="clearfix">
						
				<TABLE  class="mTop30" align="left">
					<TBODY>
						<% int i=0; %>
						<logic:iterate id="briefingarr_id"
							name="kmBriefingMstrFormBean" property="briefingDetails"
							indexId="index">							
							<% i++; %>
						<tr class="lightBg">
						<td align="left" class="text">Briefing Details</td>
						
						
								<td align="left" class="text"><html:textarea cols="50" rows="12"  onkeypress="return handleEnter(this, event)" onkeydown="limitText(this);"  name="kmBriefingMstrFormBean"
									styleId='<%= "briefingDetails[" + index + "]" %>'  property='<%= "briefingDetails[" + index + "]" %>' /></TD>
						
						</tr>	
						</logic:iterate>
						
					</TBODY>
				</TABLE>
			</li>
			
		<li class="clearfix" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
			<html:button property="Submit" value="Update" styleClass="red-btn" onclick="javascript:return formValidate('<%= i %>');">  </html:button> 
			              <%--
               	<input type="image" src="images/reset.gif" width="48" height="17" border="0"onclick="javascript:return resetFields('<%= i %>');"/></TD>
			  	--%>				
			</li>
			</ul>
	</div>
	</div>
</html:form>
