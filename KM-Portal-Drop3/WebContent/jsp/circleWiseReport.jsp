<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:notEmpty name="kmUserReportForm"  property="circleWiseReport">
<bean:define id="circleList" name="kmUserReportForm" property="circleWiseReport" type="java.util.ArrayList" />
</logic:notEmpty>
<script>
function importExcel()
{
document.kmUserReportForm.methodName.value="circleWiseExcelReport";
document.kmUserReportForm.submit();
}
function showReport(){

	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;//January is 0!
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
	var date=document.forms[0].date.value;
	if(date==''){
		alert("Please Select Date");
		return false;
	}
	if(date > curr_dt){
		alert("Selected Date Cannot be a future date");
		return false;
	}
document.kmUserReportForm.methodName.value="circleWiseReport";
document.kmUserReportForm.submit();
}

</script>

<html:form action="/userReport" >
<html:hidden property="methodName" /> 
	<div class="box2">
        <div class="content-upload">
		<h1><bean:message key="userLoginCount.title" /></h1>

								<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
									<tr align="left">
										<td class=""><font color="#0B8E7C"><strong>
										<html:messages id="msg" message="true">
               								<bean:write name="msg"/>  
             							</html:messages></strong></font></td>
									</tr>
									<tr align="left">
										<td class=""><font color="#0B8E7C"><strong>
										<html:errors/></strong></font>
									</tr>
								</table>
	
	<ul class="list2 form1">
			<li class="clearfix alt">
				<span class="text2 fll width160"><strong> Select Date</strong></span>
				<input type="text" class="tcal calender2 fll" readonly="readonly" name="date" value="<bean:write property='date' name='kmUserReportForm'/>"/>
            </li>
	</ul>
	
	 <input class="red-btn"  style="margin-left:300px;" value="Submit" alt="Submit" onclick="javascript:return showReport();" readonly="readonly"/>
 
	<logic:notEqual name="kmUserReportForm" property="initStatus" value="true">
	    <div class="flr">
	       <img  src="images/excel.gif" width="59" height="35" border="0" onclick="importExcel();" />&nbsp;&nbsp;&nbsp;
		</div>
	<table width="100%" cellpadding="5" cellspacing="0" border="0">
	 <tr>
		<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span>&nbsp;S.No</span>		</td>
		<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span>Circle Name		</span></td>
		<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span>LOB Name		</span></td>
		<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span>Total Users</span></td>
	 </tr>
     <logic:notEmpty name="circleList" >
		<logic:iterate name="circleList" id="file" indexId="i" type="com.ibm.km.dto.KmUserMstr">
			<TR class="lightBg">
				<TD class="text" align="left">&nbsp;<%=(i.intValue() + 1)%>.</TD>
				<TD class="text" align="left"><bean:write name="file" property="circleName" /></TD>
				<TD class="text" align="left"><bean:write name="file" property="lobName" /></TD>
				<TD class="text" align="left"><bean:write name="file" property="noOfLoggedInUser" /></TD>
			</TR>
		</logic:iterate>
        <tr>	
        	<td></td><td></td><td></td><td align="left">--------------</td> 
        </tr>
		<tr>
			<td></td><td  align="left" colspan="2"><B>Grand Total</B></td>
			<td><font size="2"><B><bean:write name="file"property="total" /></B></font></td>
		</tr>
	  </logic:notEmpty>
			
	  <tr><td colspan="2" class="error" align="left"><html:messages id="msg"
		message="true">
		<bean:write name="msg" />
		</html:messages></td>
	  </tr>
		<logic:empty name="circleList"  >
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
		</logic:empty>
    </table>
   </logic:notEqual>
  </div>
 </div>
</html:form>
