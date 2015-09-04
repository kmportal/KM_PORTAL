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


</SCRIPT>
<html:form action="/kmQuizReport">
	<div class="box2">
     <div class="content-upload">
	<h1>Quiz Report</h1>
	<ul class="list2 form1">
	<li class="clearfix alt">
	
	<logic:empty name="REPORT_LIST">
	<tr><TD class="text" align="center" colspan="11" ><font size="4" color="red"><bean:message
				
					key="quiz.report" /></font></TD></tr><br/><br/>
	
	</logic:empty>
				<logic:notEmpty name="REPORT_LIST">
			<bean:define id="reportList" name="REPORT_LIST" type="java.util.ArrayList" scope="request"  />
		</logic:notEmpty>	
		<logic:notEmpty name="reportList" >
		 <div class="boxt2">
	      <div class="content-upload clearfix">
	       <div class="content-table-type2 clearfix">
	       
			<table width="100%" border="0" cellspacing="0" cellpadding="2">
			<TR >
                <TH width="1%"  align="left" valign="top">SL#</TH>
                <TH width="10%" align="left" valign="top">Correct Answers </TH>
                <TH width="5%" align="left" valign="top">Wrong Answers </TH>
                <TH width="5%" align="left" valign="top">Skip Questions </TH>
                <TH width="5%" align="left" valign="top">Result </TH>
				<TH width="5%" align="left" valign="top">User_id</TH>
				<TH width="5%" align="left" valign="top">Circle Name</TH>
                <TH width="5%" align="left" valign="top">Ud_id</TH>
                  <TH width="5%" align="left" valign="top">LOB</TH>
				</TR>
		
			<logic:iterate name="reportList" type="com.ibm.km.dto.QuizReportDto" id="report" indexId="i">
			 <!--  String cssName = ""; if( i%2==1){cssName = "alt";}%> -->
				<TR ><!-- class="=cssName%>"> -->	
					<TD class="txt" width="1%" align="left" valign="top" style="font-size:12px;" ><%=(i.intValue() + 1)%>.</TD>					
					<TD class="txt" width="10%" align="left" valign="top"  style="font-size:12px;" ><bean:write name="report" property="correct_answers"  /></TD>
					<TD class="txt" width="10%" align="left" valign="top"  style="font-size:12px;" ><bean:write name="report" property="wrong_answers"  /></TD>
					<TD class="txt" width="10%" align="left" valign="top"  style="font-size:12px;" ><bean:write name="report" property="skipQuesions"  /></TD>
					<TD class="txt" width="10%" align="left" valign="top"  style="font-size:12px;" ><bean:write name="report" property="result" /></TD>
					<TD class="txt" width="10%" align="left" valign="top"  style="font-size:12px;" ><bean:write name="report" property="user_id" /></TD>
					<TD class="txt" width="10%" align="left" valign="top"  style="font-size:12px;" ><bean:write name="report" property="circle_name" /></TD>
					<TD class="txt" width="10%" align="left" valign="top"  style="font-size:12px;" ><bean:write name="report" property="ud_id" /></TD>
					<TD class="txt" width="10%" align="left" valign="top"  style="font-size:12px;" ><bean:write name="report" property="lob" /></TD>
					
					
				</TR>
			</logic:iterate>  
		</table>
	   </div>
	  </div>
     </div>
	</logic:notEmpty>
  		
		
</div>
 
</div>
</html:form>

