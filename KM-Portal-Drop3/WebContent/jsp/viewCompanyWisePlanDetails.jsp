<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="com.ibm.km.forms.KmCompanyWisePlanFormBean"%>
<!-- 

Code 	Updated by     	  Date		  CSR NO				 Defect ID		    Description
---------------------------------------------------------------------------------------------------------------
0001	Anveeksha Varma   6 MAY 09	  20081211-00-03016	     MASDB00111524  	Handling error on Enter Key

-->
<html:html>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="theme/text.css" rel="stylesheet" type="text/css">
<TITLE></TITLE>

<script type="text/javascript">
window.onerror=do_err

/*
**	Call Action Method to get rate details of selected bill plan
*/
function getRate(str)
{
	var plan=str;
	//window.open('kmCompanyWisePlanAction.do?methodName=viewBillPlanRateDetails&plan='+plan,'dummydate',"resizable=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,modal=yes,location=no,width="+(screen.width-15)+",height="+(screen.height-65)+",left=0,top=0,");
	
	window.open('kmCompanyWisePlanAction.do?methodName=viewBillPlanRateDetails&plan='+plan,'_blank',"resizable=yes,toolbar=no,scrollbars=yes,menubar=no,status=no,directories=no,width=1000,height=700,left=0,top=0");
}

/*
**	Call Action Method to get list of companies
*/
function getCompanyList()
{
	document.kmCompanyWisePlanFormBean.methodName.value='listCompany';
	
}
	
/*
**	Call Action Method to populate companies based on search value entered by user 
*/	
function doSubmit()
{
	if(document.kmCompanyWisePlanFormBean.selectCompany.value=='')
	{
		alert("please select a company !!");
		return false;
	}
	else
	{
		document.kmCompanyWisePlanFormBean.methodName.value='listCompanyDetailsAndPlans';
		document.kmCompanyWisePlanFormBean.billPlanSearchCompId.value = document.kmCompanyWisePlanFormBean.selectCompany.value;	
	}
}

/*
**	Called onLoad to set inital values
*/
function focusSet()
{
	document.kmCompanyWisePlanFormBean.companySearchValue.focus();
	document.kmCompanyWisePlanFormBean.companySearchValue.value=document.kmCompanyWisePlanFormBean.companySearchValue.value;
	if(document.kmCompanyWisePlanFormBean.selectCompany)
	{
		document.kmCompanyWisePlanFormBean.selectCompany.value=document.kmCompanyWisePlanFormBean.billPlanSearchCompId.value;	
	}
}

function getKey()
{
/*
	var e;
	e=window.event;
	if(e.keyCode) code=e.keyCode;
	else if(e.which) code = e.which;
	var character = String.fromCharCode(code);
	alert(character);
	alert(event.keyCode);
*/	
	if(event.keyCode==17)
	{
		alert("This action is not allowed");
		return false;
	}
	else
	{
		return true;
	}
}


function checkMouseEvent()
{
	if(event.button==2)
	{
		alert("This action is not allowed")
		return false;
	}
	else
	{
		return true;
	}
}

function do_err(errorMessage, url, line)
{
	return true;
}


function no_cp()
{
	clipboardData.clearData();setTimeout("no_cp()",100)
}
no_cp();

</script>
</HEAD>

<body onload="focusSet();" onkeydown="javascript:getKey();" onmousedown="javascript:checkMouseEvent();">
	<html:form action="/kmCompanyWisePlanAction">
		<html:hidden property="methodName" value=""/>
		<html:hidden property="billPlanSearchCompId"/>
		<html:hidden property="billPlanDetailSearchBillPlanName" value=""/>
		<TABLE border="0" align="center" width="40%">
			<TR>
				<td colspan="2"><br><br></td>
			</TR>
			
			<TR>
				<td colspan="2" align="center"><span class="heading">VIEW BILL PLAN AND RATES</span><BR></td>
			</TR>
			<TR><TD><br></TD></TR>
			<TR><TD><br></TD></TR>
			<TR>
				<td nowrap="nowrap">Enter Keyword to search Company : </td>
				<td>
				<!-- 0001 Handling Enter Key-->
					<html:text property="companySearchValue" size="60" onkeydown="if ((event.which &amp;&amp; event.which == 13) ||  (event.keyCode &amp;&amp; event.keyCode == 13)) {getCompanyList();} else return true;"/>
				</td>
			</TR>
			<TR>	
				<td colspan="2" align="center"><input type="image" name="submit" src="images/search.jpg" onclick="javascript:getCompanyList();"></td>
			</TR>
		</TABLE>	
		
		<logic:equal value="1" name="kmCompanyWisePlanFormBean" property="checkForCompanyList" >
			<TABLE border="0" align="center" width="40%">	
				<logic:empty name="kmCompanyWisePlanFormBean" property="compList">
					<TR>
						<TD align="center"><font color="RED" size="2"><strong>No Companies Found</strong></font></TD>
					</TR>					
				</logic:empty>
				<logic:notEmpty name="kmCompanyWisePlanFormBean" property="compList">
					<TR>
						<td align="center"><font face="ARIAL" size="2"><B>Select Company to view Plan</B></font></td>
					</TR>
					<TR>	
						<td>
							<select size="5" name="selectCompany" style="width:600px">
								<logic:iterate name="kmCompanyWisePlanFormBean" id="report" indexId="i"	property="compList">	
									<%=(i.intValue() + 1)%>
									<option value=<bean:write name="report" property="companyId" />><bean:write name="report" property="companyName" /></option>
								</logic:iterate>
							</select>
						</td>
					</TR>
					<TR>
						<td align="center"><input type="image" src="images/submit_button.jpg" onclick="javascript:return doSubmit();"></td>
					</TR>
					<TR>
						<td><br></td>
					</TR>
				</logic:notEmpty>	
			</TABLE>
			<logic:equal value="1" name="kmCompanyWisePlanFormBean" property="checkForDetails">
				<logic:notEmpty name="kmCompanyWisePlanFormBean" property="companyDetailsList">
					<TABLE border="1" bordercolor="#C3C3C3" align="center" width="80%">
								<tr>
									<TD align="center"><font face="ARIAL" size="2"><B>COMPANY DETAILS</B></font></TD>
								</tr>
								<tr>
									<TD align="center">
									<div style="height:150px; overflow-y:auto; overflow-x:auto; width:615px; ">
										 <TABLE border="1" bordercolor="#C3C3C3" align="center" >
									
										   <tr class="text white">
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.SCANNING_CODE" />
											</td>	
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.DT" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.COMPANY_NAME" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.PARENT_NAME" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.REMARKS" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.ABBREVIATION" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.SERVICE_TAX_EXEMPTION" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.HIGH_RISK_SEGMENT" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.ACCOUNT_MANAGER" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.CODE" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.REGIONAL_SALES_HEAD" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.AUTHORIZED_SIGNATORY_NAME" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.REC_DATE" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.DESIGNATION" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.CONTACT_NO" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.SECURITY_DEPOSIT" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.ITEMISED" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.ROAMING_RENTAL" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.CLIP" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.CALL_CONF" />
											</td>
											<td style="background-image:url(./images/heading.jpg); background-repeat:repeat-x;" height="35" nowrap="nowrap">
												<bean:message key="viewCompanyDetails.FAX_AND_DATA" />
											</td>					
										</tr>
										
										<tr>
											<logic:iterate name="kmCompanyWisePlanFormBean" id="companyReport" property="companyDetailsList">
												<td class="text" align="center"><bean:write name="companyReport" property="companyDetails" /></td>				
											</logic:iterate>
										</tr>
									</TABLE>
								</div>
							</td>					
						</tr>
					</TABLE>
				</logic:notEmpty>	 	
				<BR><BR>	
				<TABLE border="1" bordercolor="#C3C3C3" align="center" width="80%">
					<logic:empty name="kmCompanyWisePlanFormBean" property="planList">
						<TR>
							<TD align="center" colspan="3"><font color="RED" size="2"><strong>No Bill Plans Found for this Company</strong></font></TD>
						</TR>
					</logic:empty>
					<logic:notEmpty name="kmCompanyWisePlanFormBean" property="planList">
						<tr>
							<TD align="center" colspan="3"><font face="ARIAL" size="2"><B>BILL PLANS</B></font></TD>
						</tr>
						<tr class="text white">
							<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35">
								<bean:message key="viewBillPlan.SNO" /></td>
							<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35">
								<bean:message key="viewBillPlan.BillPlan" /></td>
							<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35">
								<bean:message key="viewBillPlan.BillPlanRemarks" /></td>
						</tr>		
						<logic:iterate name="kmCompanyWisePlanFormBean" id="report" indexId="j"	property="planList">
						<TR class="lightBg">
							<TD class="text" align="center"><%=(j.intValue() + 1)%>.</TD>
							<logic:notEmpty name="report" property="planName">
								<TD class="text" align="left"><a href="javascript:getRate('<bean:write name="report" property="planName"/>');"><bean:write name="report" property="planName"/></a></TD>
							</logic:notEmpty>
							<logic:empty name="report" property="planName">
								<TD onclick="javascript:getRate('<bean:write name="report" property="planName"/>');"></TD>
							</logic:empty>
							<TD class="text" align="left"><bean:write name="report" property="remarks" /></TD>
						</TR>
						</logic:iterate>
						<TR>
							<td colspan="3" align="center"><font face="ARIAL" color="red" size="1">* Click on the Plan to view Rates</font></td>	
						</TR>
					</logic:notEmpty>	
				</TABLE>
			</logic:equal>
		</logic:equal>		
	</html:form>
</BODY>
</html:html>