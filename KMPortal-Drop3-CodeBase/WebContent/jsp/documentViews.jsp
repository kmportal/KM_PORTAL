<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>



<%@page import="com.ibm.km.common.Utility"%><bean:define id="csrHomeBean" name="CSR_HOME_BEAN"  type="com.ibm.km.forms.KmCSRHomeBean" scope="session" /> 


<script language="JavaScript" type="text/javascript">

	function getList()
	{
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;
	//	alert(today);
		var fromDate=document.kmFileReportForm.fromDate.value;
		var toDate=document.kmFileReportForm.toDate.value;
		var diff=getDaysBetween(fromDate,toDate);
		
		if(document.kmFileReportForm.fromDate.value == "")
		{
			alert("Please Enter the From Date ");
			return false;			
		}
		if(document.kmFileReportForm.toDate.value == "")
		{
			alert("Please Enter To Date");
			return false;			
		}
		if(document.kmFileReportForm.fromDate.value > curr_dt)
		{
			alert("From Date can not be future date");
			document.kmFileReportForm.fromDate.select();
			return false;
		}
		if(document.kmFileReportForm.toDate.value > curr_dt)
		{
			alert("To Date can not be future date");
			document.kmFileReportForm.toDate.select();
			return false;
		}					
		
		if(document.kmFileReportForm.fromDate.value > document.kmFileReportForm.toDate.value)
		{
			alert("To Date should not be less than From Date ");
			document.kmFileReportForm.toDate.select();
			return false;			
		}
		if(diff>30){
			alert("Difference between From Date and To Date should not be greater than 30 ");
			document.kmFileReportForm.toDate.select();
			return false;
		}

		document.kmFileReportForm.methodName.value="showDocumentViews";
		
	}
	function getDaysBetween( sDate_1, sDate_2 )
      {
       
       var aDate_1 = sDate_1.split( "-" );
       var aDate_2 = sDate_2.split( "-" );
       
       var oDate_1 = new Date( aDate_1[ 0 ], aDate_1[ 1 ], aDate_1[ 2 ] );
       var oDate_2 = new Date( aDate_2[ 0 ], aDate_2[ 1 ], aDate_2[ 2 ] );
       
       var iTime_1 = oDate_1.getTime();
       var iTime_2 = oDate_2.getTime();
       
       var iDiff = Math.abs( iTime_1 - iTime_2 );
       var iDaysBetween = iDiff / 86400000;
       return iDaysBetween;
      }
	

function displayDocument(doc,versionCount){
	if(versionCount=="0")
	{
		window.location.href='documentAction.do?methodName=displayDocument&docID='+doc;
	}
	else{
		var url='documentAction.do?methodName=displayVersions&docID='+doc;
		window.open(url,'_blank',"resizable=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,width=510,height=250,left=250,top=250");
	}
}	
function showScrollerMessage()
	{
		var message='<div style="margin-top:2px;" ><font face=arial size=2 color="white"><B><bean:write name="csrHomeBean" property="message" /></B></font></font></DIV>';
		document.getElementById('slider').innerHTML=setMessage(message);
		timer();
			
	}

function timer(){
	getAlerts()
	window.setTimeout('timer()',30000)
	}

function getAlerts()
{
	var url="kmAlertMstr.do?methodName=view";
	if(window.XmlHttpRequest) {
		req = new XmlHttpRequest();
		
	}else if(window.ActiveXObject) {
		req=new ActiveXObject("Microsoft.XMLHTTP");
		
	}
	if(req==null) {
		alert("Browser Doesnt Support AJAX");
		return;
	}
	req.onreadystatechange = getOnChange;
	req.open("POST",url,true);
	req.send();
	
}
function getOnChange()
{
	if (req.readyState==4 || req.readyState=="complete") {
	if(req.responseText=="none")
	{
	
	}
	else{
	//alert("YOU HAVE A NEW MESSAGE");
	document.getElementById("alert").value = req.responseText;
	
	}
}
}

</script> 


<html:form action="/documentViews" >
<html:hidden name="kmFileReportForm" property="methodName" />
<div class="box2">
 <div class="content-upload">
   <h1>Most Viewed Files</h1>
    <logic:notEmpty name="msg" scope="request">
    <table width="100%" class="mTop0" align="center" border="0" cellspacing="0" cellpadding="0">
          <tr>
			<td colspan="2" align="left" class="error"><br>
			<strong> <html:errors /></strong></td>
		</tr>
		<tr>
				<td colspan="4" align="center" class="error">
					<strong> 
          			<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
             		</html:messages>
            		</strong>
            	</td>
			</tr>
   </table> 	
   	</logic:notEmpty>
	
   <ul class="list2 form1 ">
					
		<li class="clearfix">
				<span class="text2 fll width120"><strong><bean:message key="file.fromDate" /></strong></span>
				<input type="text" class="tcal calender2 fll"  readonly="readonly" name="fromDate" value="<bean:write property='fromDate' name='kmFileReportForm'/>"/>
				<span class="text2 fll width120" style="margin-left: 120px;"><strong><bean:message key="file.toDate" /></strong></span>
				<input type="text" class="tcal calender2 fll" readonly="readonly" name="toDate" value="<bean:write property='toDate' name='kmFileReportForm'/>"/>
		</li>
		<li class="clearfix" style="padding-left:360px;">
			<span class="text2 fll">&nbsp;</span><INPUT type="Image" src="images/submit.jpg" onclick="return getList();">
				
		 </li>
        </ul>

  <logic:notEqual name="kmFileReportForm" property="status" value="init">
	<table width="100%" class="mLeft5" cellspacing="0">
		  <tr align="left">
			<td align="center" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35">
				<span class="mLeft5 mTop5" ><bean:message key="documentViews.SNO" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35">
				<span class="mLeft5 mTop5"><bean:message key="documentViews.documentName"/></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35">
				<span class="mLeft5 mTop5"><bean:message key="documentViews.path" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35">
				<span class="mLeft5 mTop5"><bean:message key="documentViews.count" /></span></td>
		  </tr>
		<logic:empty name="documentViewsList" scope="request">
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
		</logic:empty>
			
		<logic:notEmpty name="documentViewsList" scope="request">
		  <logic:iterate name="documentViewsList" id="report" indexId="i" scope="request" type="java.util.HashMap">
		  <%
			String cssName = "clearfix";				
			if( i%2==1)
			{			
			cssName = "clearfix alt";
			}	
			%>
		   <tr  align="left" class="<%=cssName%>">	
			<TD align="center" width="8%"><span class="mLeft5 mTop5 mBot5"><%=(i.intValue() + 1)%>.</span></TD>
			<TD width="40%"><span class="mLeft5 mTop5 mBot5">
			    <!--<A href="#" class="Red11"   onclick="displayDocument('<bean:write name="report" property="DOCUMENT_ID"/>','<bean:write name="report" property="VERSION_COUNT"/>');">
			      <bean:write name="report" property="DOCUMENT_NAME" /></A>-->
			      
			      <a href='<%=Utility.getDocumentViewURL(""+report.get("DOCUMENT_ID"),  Integer.parseInt(""+report.get("DOC_TYPE")))%>'><bean:write name="report" property="DOCUMENT_NAME" /></a>
						
			      </span></TD>
			<TD width="40%"><span class="mLeft5 mTop5 mBot5"><bean:write name="report"	property="DOCUMENT_STRING_PATH"/></span></TD>	
			<TD width="12%"><span class="mLeft5 mTop5 mBot5"><bean:write name="report" property="COUNT" /></span></TD>
		   </TR>
		 </logic:iterate>  
    	</logic:notEmpty>
	</table>
  </logic:notEqual>
 </div>
</div>
</html:form>