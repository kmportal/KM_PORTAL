<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script type="text/javascript">

</script>

<FONT color="red"><html:errors/></FONT>
<html:form action="/rcContentUpload" >

	

	<TABLE align="center"> 
			<TBODY>
			<tr align="left">
										<td colspan="4" class=""><font color="#FF0000"><strong>
										<html:messages id="msg" message="true">
               								<bean:write name="msg"/>  
             							</html:messages></strong></font></td>
									</tr>
			
		</TBODY>
	</TABLE>
	
	
</html:form>