<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="theme/text.css" rel="stylesheet" type="text/css">
<LINK href="theme/text.css" rel="stylesheet" type="text/css">

<div class="box2">               
	<table width="100%" align="center" border="0" cellpadding="0" cellspacing="0" style="table-layout: fixed;">		
		<tr class="textwhite">
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class="mTop5"><b>Topic</b></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class="mTop5"><b>Header</b></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class="mTop5"><b>Content</b></span></td>
			<td width="20%"  style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class="mTop5" ><b>Path</b></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class="mTop5"><b>From Date</b></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class="mTop5"><b>To Date</b></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class="mTop5"><b>Circle</b></span></td>
		</tr>
		<logic:iterate name="BILL_PLAN" id="bp" indexId="i">
					<%
				String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
					<tr class="<%=cssName %>">
				<td class="txt" align="left" style="word-wrap:break-word;"><bean:write name="bp" property="topic" /></td>
				<td class="txt" align="left" style="word-wrap:break-word;"><bean:write name="bp" property="header" /></td>
				<td class="txt" align="left" style="word-wrap:break-word;"><bean:write name="bp" property="content" /></td>
				<td width ="20%" class="txt" align="left" style="word-wrap:break-word;"><bean:write name="bp" property="path" /></td>
				<td class="txt" align="left" style="word-wrap:break-word;"><bean:write name="bp" property="fromDate" /></td>
				<td class="txt" align="left" style="word-wrap:break-word;"><bean:write name="bp" property="toDate" /></td>
				<td class="txt" align="left" style="word-wrap:break-word;"><bean:write name="bp" property="circleName" /></td>
			</tr>
		</logic:iterate>
	</table>
</div>
