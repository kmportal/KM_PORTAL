
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">
<logic:notEqual name="kmBriefingMstrFormBean" property="kmActorId" value="1">
</logic:notEqual>
<script language="JavaScript" >
function formValidate(){
    var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;
	//	alert(today);
		if(document.kmBriefingMstrFormBean.kmActorId.value != "1"){
			var fromDate=document.kmBriefingMstrFormBean.fromDate.value;
			var toDate=document.kmBriefingMstrFormBean.toDate.value;
		}
		//var diff=getDaysBetween(fromDate,toDate);
		//Bug resolved : MASDB00103920
		if(document.kmBriefingMstrFormBean.kmActorId.value == 1 || document.kmBriefingMstrFormBean.kmActorId.value == 5)
		{
			if(document.kmBriefingMstrFormBean.circleId.value == ""){
				alert("Please Select a Circle ");
				return false;	
			}		
		}
		if(document.kmBriefingMstrFormBean.kmActorId.value != "1"){
			if(document.kmBriefingMstrFormBean.fromDate.value == "")
			{
				alert("Please Enter the From Date ");
				return false;			
			}
			if(document.kmBriefingMstrFormBean.toDate.value == "")
			{
				alert("Please Enter To Date");
				return false;			
			}
			
			if(document.kmBriefingMstrFormBean.toDate.value < curr_dt || document.kmBriefingMstrFormBean.fromDate.value < curr_dt)
			{
				alert("Dates cannot be past dates");
				document.kmBriefingMstrFormBean.fromDate.value="";
				document.kmBriefingMstrFormBean.toDate.value="";
				return false;
			}					
			
			if(document.kmBriefingMstrFormBean.fromDate.value > document.kmBriefingMstrFormBean.toDate.value)
			{
				alert("Start Date should greater than End Date ");
				document.kmBriefingMstrFormBean.fromDate.value="";
				document.kmBriefingMstrFormBean.toDate.value="";
				return false;			
			}
		}
	/*	if(diff>30){
			alert("Difference between From Date and To Date should not be greater than 30 ");
			document.kmBriefingMstrFormBean.fromDate.value="";
			document.kmBriefingMstrFormBean.toDate.value="";
			return false;
		}
	*/	


	document.kmBriefingMstrFormBean.methodName.value="editBriefing";
	return true;	
}

function resetFields(){
	
	var form=document.kmBriefingMstrFormBean;
	
	return false;
	
}
</script>

<html:form action="/editBriefings">
<html:hidden name="kmBriefingMstrFormBean" property="methodName"  /> 
<html:hidden name="kmBriefingMstrFormBean" property="kmActorId" />

	<div class="box2">
        <div class="content-upload">
			<h1>View/Edit Briefings</h1>

        <table class="mTop30" align="center">
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

			<logic:notEqual name="kmBriefingMstrFormBean" property="kmActorId" value="2">
			<logic:notEmpty name="CIRCLE_LIST">
			<bean:define id="circleList" name="CIRCLE_LIST" type="java.util.ArrayList" scope="request"/>
			</logic:notEmpty>	
				<tr class="lightBg"  >
						<td class="lightBg" align ="right"><BR>Circle <FONT color="red">*</FONT></TD>
						<TD align="left" colspan="3"><BR>
							<html:select property="circleId" name="kmBriefingMstrFormBean" >
								<html:option value="">-Select Circle-</html:option>
								<logic:notEmpty name="CIRCLE_LIST">
									<bean:define id="circles" name="circleList"	 /> 
									<html:options labelProperty="elementName" property="elementId" collection="circles" />
								</logic:notEmpty>
							</html:select>
						</TD>  
			</tr>
			</logic:notEqual> 
			
				
			
			<tr> <td></td> </tr>
			</table>
			
			<logic:notEqual name="kmBriefingMstrFormBean" property="kmActorId" value="1">
			
			<ul class="list2 form1">
			<li class="clearfix alt">
				<span class="text2 fll width160"><strong>Start Display Dt</strong></span>
 				<input type="text" class="tcal calender2 fll" readonly="readonly" name="fromDate" value="<bean:write property='fromDate' name='kmBriefingMstrFormBean'/>"/>
			</li>
				<li class="clearfix alt">
				<span class="text2 fll width160"><strong>End Display Dt</strong></span>
				<input type="text" class="tcal calender2 fll" readonly="readonly" name="toDate" value="<bean:write property='toDate' name='kmBriefingMstrFormBean'/>"/>
			</li>
			</ul>
			</logic:notEqual>
			<table class="mTop30" align="center">
			<tr> <td> </td>  </tr>
			<TR align="center">
			<td></td><td></td><TD align="left"><input type="image" src="images/submit.jpg"  onclick="return formValidate();"/></td>
		    </TR>
		    </table>
			<table width="95%" class="mTop30" align="center">
			<logic:notEqual name="kmBriefingMstrFormBean" property="initStatus" value="true">
			<tr class="textwhite">
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35" width="124"><span class="mLeft5 mTop5">SNo.</span> </td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35" width="124"><span class="mLeft5 mTop5">Briefing Heading</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35" width="124"><span class="mLeft5 mTop5">Category</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35" width="124"><span class="mLeft5 mTop5">Display Date</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35" width="124"><span class="mLeft5 mTop5">Edit Briefing </span></td>		
		</tr>
		<logic:empty name="kmBriefingMstrFormBean" property="briefingList">
			
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
			
		</logic:empty>
			
			<logic:notEmpty property="briefingList" name="kmBriefingMstrFormBean">
		
			<logic:iterate property="briefingList" name="kmBriefingMstrFormBean"  id="briefList" indexId="i">
			<TR class="">
			<TD  align="center"><%=(i.intValue() + 1)%>.</TD> 
			
			<TD  align="center"><bean:write name="briefList" property="briefingHeading" /></TD>
			<TD  align="center"><bean:write name="briefList" property="favCategory" /></TD>
			<TD  align="center"><bean:write name="briefList" property="displayDt" /></TD>
			<TD  align="center"><A href="editBriefings.do?methodName=updateBriefing&briefId=<bean:write name="briefList" property="briefingId" />" ><FONT color="red">View/Edit</FONT></A></TD>
			
			</tr>
			</logic:iterate>
			
			</logic:notEmpty>
		</logic:notEqual>	
</table>
</div>
  <jsp:include page="Disclaminer.jsp"></jsp:include>
</div>
</html:form>


