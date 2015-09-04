<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
 <logic:notEmpty name="kmSmsReportFormBean" property="smsReportList">
 </logic:notEmpty>
 <script>
 	function importExcel(){
 	document.forms[0].methodName.value = "importExcel";
	document.forms[0].submit();	
	}
	function retrieveSMSReport(){
		if(validate()){
			document.forms[0].methodName.value = "retrieveSMSReport";
			document.forms[0].submit();	
			return true;
		}
		
	}
	
	function validate(){
		var fromdt = document.kmSmsReportFormBean.fromDate.value;
		var todt = document.kmSmsReportFormBean.toDate.value;
		if(todt < fromdt){
				alert("From Date should not be greater than To Date ");
				return false;			
		}
		var frmdt =  parseISO8601(fromdt);
		var tdt = 	parseISO8601(todt);
		var fdt = new Date(frmdt);
		var tdate = new Date(tdt);
		var ONE_DAY = 1000 * 60 * 60 * 24;
		var date1 = fdt.getTime();
    	var date2 = tdate.getTime();
    	var difference = Math.abs(date1 - date2);
    	var noOfDays =  Math.round(difference/ONE_DAY);
    	if(noOfDays > 7 ){
    		alert(" Date Range cannot exceed more than 7 days");
    		return false;
    	}
    	else{
    		return true;
    	}
    	
	}
	
	function parseISO8601(dateStringInRange) {
    var isoExp = /^\s*(\d{4})-(\d\d)-(\d\d)\s*$/,
        date = new Date(NaN), month,
        parts = isoExp.exec(dateStringInRange);

    if(parts) {
      month = +parts[2];
      date.setFullYear(parts[1], month - 1, parts[3]);
      if(month != date.getMonth() + 1) {
        date.setTime(NaN);
      }
    }
    return date;
  }
</script>
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	
	</head>
	<html:form action="/getSMSReport">
	<input type = "hidden" name ="methodName">
		<div class="box2">
     		<div class="content-upload">
				<h1>SMS Report</h1>
					<ul class="list2 clearfix">
						<li class="clearfix padd10-0">
							<span class="text5 fll">
								<bean:message key="sms.report.from.date" />
							</span> 
							<html:text styleClass="tcal calender1 fll" property="fromDate" readonly="true"	style="margin-right:15px;" /> 
							<span class="text5 fll">
								<bean:message key="sms.report.to.date" />
							</span> 
							<html:text styleClass="tcal calender1 fll" property="toDate" readonly="true" /> 
							<div class="button">
            					<a class="red-btn" onclick="javascript:return retrieveSMSReport();">Retrieve</a>
            				</div>
						</li>
					</ul>
					<ul class="list2 form1">
						<li class="clearfix alt">
							<li class="clearfix">
								<span class="text2 fll width160"><strong><font size="2"> Download to Excel :</font></strong> </span>	
									<a href="./getSMSReport.do?methodName=importExcel"><strong><font size="3" color="red" ><img src="images/excel.gif" width="39" height="35" border="0"/></font></strong></a> 
							</li>
					<logic:present name="smsReportList">
							<logic:notEmpty name="kmSmsReportFormBean" property="smsReportList">
								<div class="boxt2">
						     		<div class="content-upload clearfix">
						       			<div class="content-table-type2 clearfix">
	        								<table width="100%" border="0" cellspacing="0" cellpadding="2" style="table-layout: fixed";>
												<TR> 
									                <TH width="2%"  align="left" valign="top">SL#</TH>
									                <TH width="3%" align="left" valign="top">OLM ID </TH>
									                <TH width="3%" align="left" valign="top">SMS Sender </TH>
									                <TH width="3%" align="left" valign="top">UD ID </TH>
									                <TH width="3%" align="left" valign="top">Mobile Number </TH>
									                <TH width="5%" align="left" valign="top">SMS Content </TH>
									                <TH width="3%" align="left" valign="top">SMS Category </TH>
									                <TH width="3%" align="left" valign="top">SMS Create Date </TH>
									                <TH width="3%" align="left" valign="top">Circle Name </TH>
									                <TH width="3%" align="left" valign="top">Partner Name </TH>
									                <TH width="3%" align="left" valign="top">Location </TH>
									                <!-- <TH width="3%" align="left" valign="top">Total SMS Sent </TH> -->
									               
									            </TR>
												<logic:iterate name="kmSmsReportFormBean" type="com.ibm.km.dto.SMSReportDTO" property="smsReportList" id="report" indexId="i">
													<%	String cssName = "";				
														if( i%2==1)
														{			
														cssName = "alt";
														}	
													%>
			 										<TR class="<%=cssName%>"  style="font-size:10px;">	
														<TD  valign="top" style="font-weight:bold;"><%=(i.intValue() + 1)%>.</TD>
														<TD class="txt" width="3%" align="left" valign="top"  style="font-size:10px;" ><bean:write name="report" property="olmId"  /></TD>
			 											<TD class="txt" width="3%" align="left" valign="top"  style="font-size:9px;" ><bean:write name="report" property="smsSender"  /></TD>
			 											<TD class="txt" width="3%" align="left" valign="top"  style="font-size:9px;" ><bean:write name="report" property="udId"  /></TD>
														<TD class="txt" width="3%" align="left" valign="top"  style="font-size:10px;" ><bean:write name="report" property="mobileNo"  /></TD>
														<TD class="txt" width="3%" align="left" valign="top"  style="font-size:10px;" ><bean:write name="report" property="smsContent"  /></TD>
														<TD class="txt" width="3%" align="left" valign="top"  style="font-size:10px;" ><bean:write name="report" property="smsCategory"  /></TD>
														<TD class="txt" width="3%" align="left" valign="top"  style="font-size:10px;" ><bean:write name="report" property="smsCreatedDate"  /></TD>
														<TD class="txt" width="3%" align="left" valign="top"  style="font-size:10px;" ><bean:write name="report" property="circleName"  /></TD>
														<TD class="txt" width="3%" align="left" valign="top"  style="font-size:10px;" ><bean:write name="report" property="patnerName"  /></TD>
														<TD class="txt" width="3%" align="left" valign="top"  style="font-size:10px;" ><bean:write name="report" property="location"  /></TD>
														<!-- <TD class="txt" width="3%" align="left" valign="top"  style="font-size:8px;" ><bean:write name="report" property="totalSMSCount"  /></TD> -->
														
																										
													</TR>
												</logic:iterate>  
											</table>
	   									</div>
	  								</div>
	    						</div>
							</logic:notEmpty>
								<logic:empty name = "kmSmsReportFormBean" property="smsReportList">
									<tr>
										<TD class="text" align="center" colspan="11" >
											<font size="4" color="red">
												<bean:message key="no.sms.report.found" />
											</font>
										</TD>
									</tr>
									<br/>
									<br/>
								</logic:empty>
							</logic:present>
							
					</ul>
  				</div>
 			</div>
	</html:form>
</html>