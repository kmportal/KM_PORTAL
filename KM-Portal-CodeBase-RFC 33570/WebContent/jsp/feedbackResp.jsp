
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table class="mTop30" align="center">
<tr class="textwhite">
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="feedback.SNO" /></span> </td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Feedback Comment</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Feedback Response</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Feedback Created Date </span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Feedback Updated Date </span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Status(I/R) </span></td>
		</tr>
<logic:notEmpty name="kmFeedbackMstrFormBean" property="feedbackList">
<logic:iterate name="kmFeedbackMstrFormBean" property="feedbackList" id="feedbackResp" indexId="i">
<TR class="">
<TD  align="center"><%=(i.intValue() + 1)%>.</TD>
<TD  align="center"><bean:write name="feedbackResp" property="comment" /></TD>
<TD  align="center"><bean:write name="feedbackResp" property="feedbackResponse" /></TD>
<TD  align="center"><bean:write name="feedbackResp" property="createdDate" /></TD>
<TD  align="center"><bean:write name="feedbackResp" property="updatedDate" /></TD>
<TD  align="center"><bean:write name="feedbackResp" property="readStatus" /></TD>
</tr>


</logic:iterate>
</logic:notEmpty>
</table>	
