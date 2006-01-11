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
function addRow(){
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
	var count1=document.getElementById("count").value;
	var i=0;
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;//January is 0!
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
	var length;
	
	 var reg=/^[0-9a-zA-Z_@*!&^$#%?><=,.:;|+*-{}()?[/\] ]*$/;
	
	if(document.forms[0].kmActorId.value=='1'||document.forms[0].kmActorId.value=='5'){
		if(document.forms[0].circleId.value==""){
			alert("Please Select a Circle");
			return false;	
		}
	
	}
	if(document.forms[0].briefingHeading.value=="")
	{
		alert("Please Enter Briefing Heading");
		document.forms[0].briefingHeading.focus();
		return false;
	}
		while(i<=count1){
			if(document.getElementById("briefingDetails["+i+"]").value==""){
				alert("Briefing cannot be blank");
				document.getElementById("briefingDetails["+i+"]").focus();
				return false;	
			  }
			// Bug resolved MASDB00064879
			var charBrief=i+1;
			length=300;
			if (document.getElementById("briefingDetails["+i+"]").value.length > length) {
			alert ("Please limit Briefing No :"+charBrief + "\n to "+length+" characters.");
			document.getElementById("briefingDetails["+i+"]").focus();
				return false;
			}
			
			i++;
			//alert("checking again for count:"+i); 
		}	
	
	var fromdt = document.kmBriefingMstrFormBean.fromDate.value;
	var todt = document.kmBriefingMstrFormBean.toDate.value;
	if(todt < fromdt){
				alert("From Date should not be greater than To Date ");
				return false;			
		}
		
	if(curr_dt > fromdt){
		alert("From Date cannot be a past date (day before today) ");
				return false;
		}
	
	
	if(document.forms[0].categoryId.value==""){
		alert("Please Select a Category");
		return false;
		
	}
	document.forms[0].submit();	
}

function resetFields()
{	
 document.forms[0].reset();	
}

function getCategories()
{
	document.forms[0].methodName.value = "getCategories";
	document.forms[0].submit();
}

</SCRIPT>
<%HttpSession Session = request.getSession(false);
String csrfFlag = com.ibm.km.common.Utility.getRandomCode();
			String csrf = csrfFlag;
			Session.setAttribute("csrfFlag" , csrfFlag);
 %>
<html:form action="/kmBriefingMstr">

	<html:hidden property="methodName" value="insert"/>
	<input type="hidden" value="<%=csrf%>" name="csrf" id="csrf" />
	<html:hidden property="kmActorId" />
	<div class="box2">
     <div class="content-upload">
	<h1><bean:message key="briefing.title" /></h1>
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
				
			
		</TBODY>
	</TABLE>
			<ul class="list2 form1">
			<logic:notEqual name="kmBriefingMstrFormBean" property="kmActorId" value="2">
			<logic:notEmpty name="CIRCLE_LIST">
			<bean:define id="circleList" name="CIRCLE_LIST" type="java.util.ArrayList" scope="request"/>
			</logic:notEmpty>	
		
			<li class="clearfix">
			<span class="text2 fll width160"><strong>Circle <font color="red" size="2">*</font></strong></span>
						
							<html:select property="circleId" name="kmBriefingMstrFormBean"  onchange="javascript:getCategories();" styleClass="select1">
								<html:option value="">Select Circle</html:option>
								<logic:notEmpty name="CIRCLE_LIST">
									<bean:define id="circles" name="circleList"	 /> 
									<html:options labelProperty="elementName" property="elementId" collection="circles" />
								</logic:notEmpty>
							</html:select>
			
			</li>
			</logic:notEqual>	
		<li class="clearfix alt" id="favCategoryTr">
			<span class="text2 fll width160"><strong><bean:message key="createUser.category" />&nbsp;&nbsp;</strong></span>
					
							<html:select property="categoryId" name="kmBriefingMstrFormBean" styleClass="select1">
								<html:option value="">Select Category</html:option>
								<html:option value="0">Select All</html:option>
								<logic:notEmpty name="kmBriefingMstrFormBean" property="categoryList">
									<bean:define id="categories" name="kmBriefingMstrFormBean"	property="categoryList" /> 
									<html:options labelProperty="elementName" property="elementId" collection="categories" />
								</logic:notEmpty>
							</html:select>
						
		</li>
		<li class="clearfix">
			<span class="text2 fll width160"><strong><bean:message key="briefing.heading" /> </strong></span>
				<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text property="briefingHeading" styleClass="textbox7" name="kmBriefingMstrFormBean" maxlength="40"/>
			</span></span></p></li>
			
			
		<li class="clearfix alt">
			<span class="text2 fll width160"><strong><bean:message key="briefing.FromDate" /></strong></span>
				<html:text styleClass="tcal calender1 fll" property="fromDate" readonly="true"	style="margin-right:15px;" /> 
				 
				<span class="text2 fll width160"><strong><bean:message key="briefing.ToDate" /></strong></span>
				<html:text styleClass="tcal calender1 fll" property="toDate" readonly="true" />
		</li>
		<li class="clearfix">
		    <span class="fll width160"> <strong><bean:message key="briefing.details" /></strong></span>
			<TABLE border="0" align="left"  width="50%" id="briefingTable">						
				<c:forEach begin="0" end="${count}" var="i" step="1" >
					<TR>
					  <TD align="left" >
						<html:textarea  styleClass="textarea2"   onkeydown="return limitText(this);" property='<%="briefingDetails[" + ((Integer)(pageContext.getAttribute("i"))).intValue()+ "]"%>' styleId='<%="briefingDetails[" + ((Integer)(pageContext.getAttribute("i"))).intValue()+ "]"%>' /></TD>
					</TR>
				</c:forEach>
			</TABLE>
		</li>
		<li class="clearfix">
		<a  href="./kmBriefingMstr.do?methodName=addQuestion"><storng><font size="3" color="red"><u>Add Questions</u></font></storng></a>
			<span class="text2 flr width160"><a class="add-more-btn flr" onclick="addRow()"></a></span>
		</li>
		                 
          <div class="button-area">
	       <div class="button"><input class="submit-btn1 red-btn" name="" value="" onclick="javascript:return formValidate();" />       </div>
	       <div class="button"> <a class="red-btn" onclick="return resetFields();">clear</a></div>
	      </div>          
        </ul>

	
<html:hidden property="count" styleId="count"/>	
</div>
  <jsp:include page="Disclaminer.jsp"></jsp:include>
</div>
</html:form>

