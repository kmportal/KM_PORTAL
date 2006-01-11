<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">
<LINK href="theme/text.css" rel="stylesheet" type="text/css">

<script language="JavaScript" type="text/javascript">
	function validate()
	{
	if(document.kmViewReportsFormBean.reportType.value == ""){
		alert("Please select a report");
		return false;
	}
	
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;//January is 0!
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
	var date=document.forms[0].reportDate.value;
	if(date==''){
		alert("Please Select Date");
		return false;
	}
	if(date > curr_dt){
		alert("Selected Date Cannot be a future date");
		return false;
	}
		document.kmViewReportsFormBean.methodName.value = "displayReports";
		document.kmViewReportsFormBean.submit();
}
</script>

<html:form action="/viewReports" >
<html:hidden property="methodName"/>
	
	   <div class="box2">
        <div class="content-upload">
        <h1>View Reports </h1>
        
        	<center><font color="#FF0000"><strong>
										<html:messages id="msg" message="true">
               								<bean:write name="msg"/>  
             							</html:messages></strong></font></center>

		<ul class="list2 form1 ">
					<li class="clearfix" id="element">
					<span class="text2 fll width160"><strong><bean:message key="addfile.publishDate" /> </strong></span>		
					<input type="text" class="tcal calender2 fll" readonly="readonly" name="reportDate" value="<bean:write property='reportDate' name='kmViewReportsFormBean'/>"/>
                	
				</li>
                <li class="clearfix alt" >
				<span class="text2 fll width160"><strong><bean:message key="addBanner.selectReport" />
				</strong></span>
                       	<html:select property="reportType" styleId="initialSelectBox" styleClass="select2" >
                        	<html:option value="">Select</html:option>
                        	<html:option value="noUsageReport">No Usage Report</html:option>
                        	<html:option value="scrollerUpdationReport">Scroller Updation Report</html:option>
                        	<html:option value="contentStatusReport">Content Status Report</html:option>
                        	<html:option value="obsoleteIdsReport">Obsolete Ids Report</html:option>
                        	<html:option value="contentExpiryReport">Content Expiry Report</html:option>
                        	<html:option value="documentHitCountReport">Document Hits Count Report</html:option>
                        	<html:option value="iportalFeedbackReport">iPortal Feedback Report</html:option>
                        	<html:option value="userLoginStatusReport">User Login Status Report</html:option>
                        </html:select>
                  </li>   
                 <li class="clearfix" style="padding-left:170px;">
				<span class="text2 fll">&nbsp;</span>
                      	<INPUT type="Image" src="images/submit.jpg" onclick="return validate();">
             	</li>
         </ul>

  </div>
  </div>
</html:form>

 