 <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>  
 <%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 
 <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
 
<script language="javascript" type="text/javascript">

function csrHomeSubmit(cirId){
	if(document.kmSearchFormBean.circleId.value == ""){
			alert("Select Circle");
			return false;
	}
	window.location.href="csrAction.do?methodName=viewCSRHome&circleId="+cirId;
	
}
function getCircles(lobId){
if(document.kmSearchFormBean.lobId.value == ""){
		alert("Select LOB");
		return false;
}
document.kmSearchFormBean.submit();
}
</script>
 
 
 <logic:notEmpty name="CSR_HOME_BEAN" >      
<bean:define id="csrHomeBean" name="CSR_HOME_BEAN"  type="com.ibm.km.forms.KmCSRHomeBean" scope="session" />
 <logic:notEmpty name="CSR_HOME_BEAN" property="lobList" >
<bean:define id="lobList" name="CSR_HOME_BEAN"  property="lobList" type="java.util.ArrayList" scope="session" />
</logic:notEmpty>
<logic:notEmpty name="CSR_HOME_BEAN" property="circlelist">
<bean:define id="allCircleList" name="CSR_HOME_BEAN"  property="circlelist" type="java.util.ArrayList" scope="session" />
</logic:notEmpty>
</logic:notEmpty>
  
  <html:form action="/kmSearch">
  <html:hidden name="kmSearchFormBean" property="methodName" value="loadCircle" />
<%String UD=(String)session.getAttribute("ud"); %>

          <p>
            <html:select styleClass="select2" styleId="currentLobId" property="lobId" name="CSR_HOME_BEAN" onchange="return getCircles(this.value);">
								<logic:notEmpty name="CSR_HOME_BEAN"  property="lobList" >	
									
									<%if(("UDlogin").equals(UD) || UD!=null) {%>
									<html:options labelProperty="elementName" property="elementId"	collection="lobList" />
									<%}else{ %>
									<html:option value=""> Select LOB </html:option>
									<html:options labelProperty="elementName" property="elementId"	collection="lobList" />
								<%} %>
								</logic:notEmpty>	
			</html:select>
          </p>
        
        
          <p>
         	<html:select styleClass="select2" styleId="currentCircleId" property="circleId"
					name="csrHomeBean" onchange="csrHomeSubmit(this.value)">
					<logic:notEmpty name="CSR_HOME_BEAN" property="circlelist">
					<%if(("UDlogin").equals(UD) || UD!=null) {%>
					<html:options labelProperty="elementName" property="elementId"
						collection="allCircleList" />
					<%}else{ %>
					<html:option value=""> Select Circle </html:option>
					<html:options labelProperty="elementName" property="elementId"
						collection="allCircleList" />
						<%} %>
						</logic:notEmpty>
			
				</html:select>
          </p>
          </html:form>

    