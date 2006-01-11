<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<LINK href="theme/text.css" rel="stylesheet" type="text/css">

<script>
	function submitForm(a)
	{
		document.forms[0].documentId.value=a;
		document.forms[0].methodName.value="retrieveData";
		document.forms[0].submit();
		return true;
	}
</script>


<html:form action='/bpUpload' method="post">
	<html:hidden property="methodName"/>
	<html:hidden property="documentId" value="" />
		<div> 
		<logic:notEmpty name="BILL_PLAN_HITS">
			<table align="center" width="100%" align="center" border="0" cellpadding="0" cellspacing="0" style="table-layout: fixed;">
				<tr class="textwhite">
					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class="mTop5"><b>Topic</b></span></td>
				</tr>
				
				<logic:iterate name="BILL_PLAN_HITS" id="bp" indexId="i">
					<%
				String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
					<tr class="<%=cssName %>">
						<td class="txt" align="left" style="word-wrap:break-word;">
							<span class="mTop5"> 
								<a href="#"
									onclick="return submitForm('<bean:write name="bp" property="documentId"/>')">
									<bean:write name="bp" property="billPlan" />
								</a>
							</span>
						</td>
					</tr>
				</logic:iterate>
			
			</table>
	 	</logic:notEmpty>
	 	<logic:empty name="BILL_PLAN_HITS">
	 	<p align="center" style="color:red;"><b>NO RECORDS FOUND !!</b></p>"
	 	</logic:empty>
		</div>
</html:form>