<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<SCRIPT>
function killEnter(evt)
{
if(evt.keyCode == 13 || evt.which == 13)
{
return false;
}
return true;
}
function limitText(textArea) {
var length=300;
if (textArea.value.length > length) {
alert ("Please limit Briefing to "+length+" characters.");
textArea.value = textArea.value.substr(0,length-1);
return false;
}
return true;
}
function addQuestion(){
	alert("add question method");
	count=document.getElementById("count").value;
	if(count>=5)
	{
		alert("No More Boxes can be Added");
		return false;
	}
	count++;
	//Code change after UAT observation
    var body = document.getElementById("briefingTable");
    var row = document.createElement("TR");
    var td1 = document.createElement("TD");
    var input1= document.createElement('textarea');
    input1.setAttribute('name',"briefingDetails["+count+"]");
	input1.setAttribute('id',"briefingDetails["+count+"]");
	input1.setAttribute('rows',"3"); 
	input1.setAttribute('cols',"41"); 
//	input1.setAttribute('onkeydown',"alert();");
   // input1.setAttribute('onkeypress',"return killEnter(event);"); 
    input1.onkeypress=function(){if ((event.keyCode == 13 ) || (event.which == 13)) event.returnValue = false; limitText(this)};
   	
   	td1.appendChild(input1);	
    row.appendChild(td1);
    body.tBodies[0].appendChild(row);
	document.getElementById("count").value=count;
	return true;
}

function formValidate()
{

	var firstAnswer=document.forms[0].firstAnswer.value;
	var secondAnswer= document.forms[0].secondAnswer.value;
	var thirdAnswer=document.forms[0].thirdAnswer.value;
	var fourthAnswer=document.forms[0].fourthAnswer.value;	
	
	if(document.forms[0].question.value=="")
	{
		alert("Please Enter Question");
		return false;
	}
	if(document.forms[0].firstAnswer.value=="")
	{
		alert("Please Enter First Answer");
		return false;
	}	
	
	if(document.forms[0].secondAnswer.value==""){
		alert("Please Enter Second Answer");
		return false;
	}
	
	if(document.forms[0].thirdAnswer.value==""){
		alert("Please Enter Third Answer");
		return false;
	}
	if(document.forms[0].fourthAnswer.value==""){
		alert("Please Enter Fourth Answer");
		return false;
	}
	var correctAnswer=document.forms[0].correctAnswer.value;
		
	
	
	document.forms[0].methodName.value = "createQuestion";
	document.forms[0].submit();	
	return true;
	
	
}

function resetFields()
{	
 document.forms[0].reset();	
}
</SCRIPT>
<html:form action="/kmBriefingMstr">

	<html:hidden property="methodName" value="insert"/>
	<html:hidden property="kmActorId" />
	<div class="box2">
     <div class="content-upload">
	<h1><bean:message key="briefing.addQues" /></h1>
	<TABLE align="center"> 
			<TBODY>
			<tr>
				<td colspan="2" align="center" class="error" >
					<strong> <font size="2" color="red">
          			<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
                          
             		</html:messages>
            		</font></strong>
            	</td>
			</tr>
				
			
		</TBODY>
	</TABLE>
	
			<ul class="list2 form1">
			
			
		<br/><br/><br/>
		
		<li class="clearfix">
		<a  href="./kmBriefingMstr.do?methodName=addQuestion"><storng><font size="3" color="red"><u>Add More Questions</u></font></storng></a>
		
		</li>
		  <br/><br/><br/><br/>             
        </ul> 
<html:hidden property="count" styleId="count"/>	
</div>
  <jsp:include page="Disclaminer.jsp"></jsp:include>
</div>
</html:form>

