<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<%@page import="com.ibm.km.dto.KmUserMstr"%><script language="javascript">

	//window.location.href="linkUploadMstr.do?methodName="+document.getElementById("").value;
	function selectedLOB(){
		//window.location.href="linkUploadMstr.do?methodName=initExecute";
		if( document.getElementById("quicklinks").style.display=='none' ){
			document.getElementById("quicklinks").style.display=='';
		}
		document.kmLinkMstrFormBean.methodName.value="initExecute";
		document.kmLinkMstrFormBean.submit();
	}
	
	

	function submitSelect(){
		if(document.kmLinkMstrFormBean.elementId.value == ""){
			alert("Select LOB first");
			return false;
		}
		document.kmLinkMstrFormBean.methodName.value="initExecute";
		document.kmLinkMstrFormBean.submit();
		}

	function submitUpdate(){
		if(document.kmLinkMstrFormBean.elementId.value == ""){
			alert("Select LOB first");
			return false;
		}
		
		<%-- Changes by Abu Shama from here --%>
		
		for(var i=0;i<counter;i++){
		if(i==0){
			if(document.getElementsByName("linkMst["+i+"].linkTitle")[0].value==""){
				alert("Please Enter Link Title :"+(i+1));
			}
			if(document.getElementsByName("linkMst["+i+"].linkPath")[0].value==""){
				alert("Please Enter Link Path :"+(i+1));
			}
		}
		//alert(i);
		if(!(document.getElementsByName("linkMst["+i+"].linkTitle")[0].value==""))
		{
			var count1 = i+1;
			//alert("Please Enter Link Title :"+count1);
			if(document.getElementsByName("linkMst["+i+"].linkPath")[0].value=="")
		{
			alert("Please Enter Link Path :"+count1);
			return false;
		}
		}

		if(document.getElementsByName("linkMst["+i+"].linkTitle")[0].value.length > 25 )
		{
			var count1 = i+1;
			alert("Please enter text length maximum to 25 characters for link title at "+count1);
			return false;
		}

	}
		for(var j=0;j<counter;j++){
		//alert(i);
		if(!(document.getElementsByName("linkMst["+j+"].linkPath")[0].value==""))
		{
			var count2 = j+1;
			if(document.getElementsByName("linkMst["+j+"].linkTitle")[0].value=="")
		{
			alert("Please Enter Link Title :"+count2);
			return false;
		}
		}

		if(document.getElementsByName("linkMst["+j+"].linkPath")[0].value.length > 255)
		{
			var count2 = j+1;
			alert("Please enter text length maximum to 255 characters for URL link at "+count2);
			return false;
		}

	}
			
		<%-- Changes by Abu Shama till here --%>
		
		document.kmLinkMstrFormBean.methodName.value="update";
		document.kmLinkMstrFormBean.submit();
	}
	function clearFields(){
	//alert("clear");
		for(var i=0;i<counter;i++){
		//alert(i);
			document.getElementsByName("linkMst["+i+"].linkTitle")[0].value="";
			document.getElementsByName("linkMst["+i+"].linkPath")[0].value="";
		}
		return false;
	}

</script>


      
      <html:form action="/linkUploadMstr">
         <div class="box2">
        <div class="content-upload">
        
<html:hidden property="methodName" />

          <h1>Configure Quick Links</h1>
          <table width="100%" class="mTop30" align="center" cellspacing="0" cellpadding="0">
          <tr><td align="center"><html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
             		</html:messages></td></tr>
          </table>
           <ul>
          
          <div id="displayLob"> 
           <li class="clearfix alt padd10-0">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
          <%
          KmUserMstr sessionUserBean =
			(KmUserMstr) session.getAttribute("USER_INFO");
			String actorId = sessionUserBean.getKmActorId();
			
           %>
          <%
          if(actorId.equals("5"))
          {                
           %>
          <div class="text3"><b><bean:message key="linkupload.selectCricle" /></b></div><FONT color="red" >*</FONT>&nbsp;
          <%
          }
          else{        
           %>
          <div class="text3"><b><bean:message key="linkupload.selectLOB"/></b></div><FONT color="red" >*</FONT>&nbsp;
          <% } %>
        
          
          <html:select property="elementId" name="kmLinkMstrFormBean" onchange="selectedLOB()"  styleClass="select1">
				<html:option value="" styleClass="select1">Select</html:option>
				<html:option value="1" styleClass="select1">Top Link</html:option>
					<logic:notEmpty name="kmLinkMstrFormBean" property="elementList" >
						<bean:define id="elements" name="kmLinkMstrFormBean" property="elementList" /> 
							<html:options labelProperty="elementName" property="elementId"  collection="elements" />
					</logic:notEmpty>
			</html:select> &nbsp;&nbsp;&nbsp;&nbsp;
			<a>
			<img src="common/images/go-button3.gif" alt="go" onclick="submitSelect()" width="30" height="25" border="0" align="center" />
			</a> &nbsp; &nbsp;  
			</li>
          
          </div>
          		
			
            </ul>
            
            <ul class="list2 form1 ">
            <logic:notEmpty name="kmLinkMstrFormBean" property="linkList" scope="session">

		<logic:iterate id="linkMst" name="kmLinkMstrFormBean" property="linkList" scope="session" indexId="ctr">
		<%
		if(ctr % 2 == 1) {
		 %>
				<li class="clearfix padd10-0">
				<%
				}
				else
				{
				 %>
				<li class="clearfix alt padd10-0">
				 
				 <%
				 }
				  %>
					<span class="text1 fll"> </span>
					<script type="text/javascript"> var counter=<%=ctr+1%>;</script>

                <p class="clearfix fll margin-r20">
                <span class="text3 fll">
					Link Title <%if(ctr==0){%><FONT color=red>*</FONT><%} %>
					</span>
				</p>					
                <p class="clearfix fll margin-r20">
					<span class="textbox6" >
                    	<span class="textbox6-inner" >
					<html:text name="linkMst" property="linkTitle" styleClass="textbox3" indexed="true"/>
					</span></span>
				</p>					

                <p class="clearfix fll margin-r20">
                <span class="text3 fll">
					Link URL <%if(ctr==0){%><FONT color=red>*</FONT><%} %>
					</span>
				</p>					
					
                <p class="clearfix fll margin-r20">
					<span class="textbox6">
                    	<span class="textbox6-inner">
					<html:text name="linkMst" property="linkPath" styleClass="textbox8" indexed="true"/>
					<span class="text2 fll width80" style="padding-left:20px;">  </span>
					</span></span>
				</p>
					
				</li>	
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="kmLinkMstrFormBean" property="linkList" scope="session">
				<li class="clearfix alt padd10-0">
					
					
				</li>
		</logic:empty>
		<li class="clearfix" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
		<span class="text2 fll width80" style="padding-left:150px;">
		<input class="red-btn fll" src="images/submit.jpg" name="" type="image" onclick="javascript:return submitUpdate();"/> 
		</span>
		
		<span class="text2 fll width80" style="padding-left:30px;">
		<input class="red-btn fll" src="images/clear.jpg" name="" type="image" alt="clear" onclick="javascript:return clearFields();"/>
		</span>
		
		</li>
		</ul>
		</div>
        </div>
        </html:form>

