<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<LINK href="theme/css.css" rel="stylesheet" type="text/css">
<logic:notEmpty name="DOCUMENT_LIST">
<bean:define id="documentList" name="DOCUMENT_LIST" type="java.util.ArrayList" scope="request"  />
</logic:notEmpty>
<script language="JavaScript" src="jScripts/KmValidations.js"	type="text/javascript"	>
</script> 

<SCRIPT><!--
function download(){

	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;//January is 0!
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
	var then = new Date( today.getFullYear(), today.getMonth(), today.getDate() - 7 );
	var tdd = then.getDate();
	var tmm = then.getMonth()+1;//January is 0!
	var tyyyy = then.getFullYear();
	if(tdd<10){dd='0'+dd}
	if(tmm<10){mm='0'+mm}
	var tcurr_dt=tyyyy+'-'+tmm+'-'+tdd;
	//alert(tcurr_dt);
	var selectdate=document.forms[0].selectDate.value
	if(selectdate >= curr_dt) {
		alert("Please Select a Past Date");
		return false;
	
	}
	/*alert(tcurr_dt+" " +selectdate);
	if(selectdate > tcurr_dt){
		alert("Only Last 7 Days Report Are Available");
		return false;
	}*/
	document.forms[0].methodName.value="downloadReport";
	

}

--></SCRIPT> 
<html:form action="/fileReport"  method="POST" >
	<html:hidden property="methodName" /> 
	<div class="box2">
     <div class="content-upload">
     <h1>CSRWise Document Count Report</h1>
	<table width="95%" class="mTop30" align="center">
		<tr align="center">
			<td colspan="9"><strong><font color="red" ><html:errors /></font></strong></td>
		</tr>
									<tr>
						<td colspan="2" class="pTop4 pLeft10">
							
								<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
									<tr align="left">
										<td class=""><font color="#0B8E7C"><strong>
										<html:messages id="msg" message="true">
               								<bean:write name="msg"/>  
                          
             							</html:messages></strong></font></td>
									</tr>
								</table>
								
						</td>
					</tr>
		
	</table>
		<ul class="list2 form1">
		<li class="clearfix alt">
			<span class="text2 fll width160"><strong>Select Date</strong></span>
				<span class="text2 fll width180">
				<input type="text" class="tcal calender2 fll" readonly="readonly" name="selectDate" value="<bean:write property='selectDate' name='kmFileReportForm'/>"/> </span>
		</li>
		
		<li class="clearfix alt" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
				<input type="image" src="images/submit.jpg" onclick="JavaScript: return download();"/>		
		</li></ul>


</div></div>
</html:form>


