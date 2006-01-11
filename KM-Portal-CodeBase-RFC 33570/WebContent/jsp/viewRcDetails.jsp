<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<!-- Added by ABU for Mobile number validation -->


<script type="text/javascript">
function validateMobileNumber(){
			//alert("Validating ....");
			var mobNo = document.getElementById("mobileNo").value;
			//alert("Mobile No: "+mobNo);
			var len=mobNo.length;
			var regmob=/^[0-9]*$/;
			if(mobNo == ""){
				alert("Please Enter Mobile Number");
				return false;
			}
			
			if(!regmob.test(mobNo)){
				alert("Please enter Valid User Mobile Number");
				//document.kmDocumentMstrForm.mobileNo.focus();
				//document.kmDocumentMstrFormMainSearchResult.mobileNo.value="";
				
				return false;	
			}
			if(mobNo=='00000000000' || mobNo=='0000000000'){
				alert("Please enter Valid User Mobile Number");
				return false;	
			}
			if(len<10||len>10)
			{	
				alert("Please enter Valid User Mobile Number");
				//document.kmUserMstrFormBean.userMobileNumber.focus();
				return false;
			}
	
	  		return true;
		}

</script>

<!-- End of Mobile number validation -->

<html:form action="/rcContentUpload" >
<html:hidden property="methodName" value="viewRCData"/>
<html:hidden property="parentId" styleId="parentId"/>
<html:hidden property="elementLevel" styleId="elementLevel"/>
<html:hidden property="elementFolderPath" styleId="elementFolderPath"/>
</html:form>
<FONT color="red"><html:errors/></FONT>
	<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />  
	<bean:define id="kmRCContentUploadFormBean" name="kmRCContentUploadFormBean" type="com.ibm.km.forms.KmRCContentUploadFormBean" scope="request" />
										<center><font color="#FF0000"><strong>
										<html:messages id="msg" message="true">
               								<bean:write name="msg"/>  
             							</html:messages></strong></font></center>

<div id="mainContent">
	<div class="box2">
        <div class="content-upload clearfix">
            <% if ( kmUserBean.isCsr()) { %>
    	    <span id="fav">
			<%						
  			session.setAttribute("docId", kmRCContentUploadFormBean.getDocId());
  			 %>
  			 <jsp:include page="AddToFavourite.jsp"></jsp:include>
  			
  			 </span>
  		  <%} %>	
           <h1 class="clearfix"><span class="text"><bean:write name="kmRCContentUploadFormBean"	property="topic" /> </span></h1>
           <div class="bill-upload-head2 box3 form1">
            <ul class="list2 clearfix">
              <li class="clearfix" style="margin-bottom:0px;"> 
                <span class="width120 text5 fll" style="font-size:14px;">RC Value :</span>
                <span class="width120 text5 fll" style="font-size:14px;"><bean:write name="kmRCContentUploadFormBean"	property="rechargeValue" /></span>
<!--                  
                <span class="width85 text5 fll" style="font-size:14px;">Start Date :</span>
                <span class="width120 text5 fll" style="font-size:14px;"><bean:write name="kmRCContentUploadFormBean"	property="startDt" /></span>
                
                <span class="width85 text5 fll" style="font-size:14px;">End Date :</span>
                <span class="width120 text5 fll" style="font-size:14px;"><bean:write name="kmRCContentUploadFormBean"	property="endDt" /></span>

              </li>
               <li class="clearfix" style="margin-bottom:0px;">
 -->               
                  <span class="width120 text5 fll" style="font-size:13px;">Combo Type :</span>
                  <span class="text5 fll" style="font-size:13px;"><bean:write name="kmRCContentUploadFormBean"	property="selectedCombo" /></span>
              </li>
            </ul>
          </div>

					<table class="list2 form1" width=100% style="table-layout: fixed;" cellspacing="1" cellpadding="3">
			
			 <bean:define id="headerTexts" name="kmRCContentUploadFormBean" property="headers" type="java.lang.String[]" />
			 <bean:define id="contentTexts" name="kmRCContentUploadFormBean" property="contents" type="java.lang.String[]" />
	 		 <logic:iterate name="headerTexts" id="headers" indexId="rowCount" >
	 		 <% String liClass;
						if(rowCount%2 == 0)
							liClass = "clearfix";
						else
							liClass = "clearfix alt";
				%>
						
					<tr class='<%=liClass %>'>
					<td style="font-size:16px;font-family:Arial, Helvetica, sans-serif;" width=250px>	
              		<%= headerTexts[rowCount] %> : 
                	</td>
					<td style="font-size:16px;font-family:Arial, Helvetica, sans-serif;">
                	<%= contentTexts[rowCount] %>
					</td>
            		</tr>
						
			 </logic:iterate>
			 <tr class="clearfix alt">
					<td style="font-size:16px;font-family:Arial, Helvetica, sans-serif;" width=250px>	
              		Valid From : 
                	</td>
                	<td style="font-size:16px;font-family:Arial, Helvetica, sans-serif;">
                	<%=kmRCContentUploadFormBean.getStartDt()%>
					</td>
			</tr>
			<tr class="clearfix">
					<td style="font-size:16px;font-family:Arial, Helvetica, sans-serif;" width=250px>	
              		Valid Till : 
                	</td>
                	<td style="font-size:16px;font-family:Arial, Helvetica, sans-serif;">
                	<%=kmRCContentUploadFormBean.getEndDt()%>
					</td>
			</tr>
			</table>
	   </div>
     </div>
	
	<p><br><br>
	
	
	<!-- Added by ABU so that CSR can send RC data as SMS-->
	
	<%String expired = request.getParameter("expired");
	if(expired!=null){
			if(expired.equalsIgnoreCase("Y")){
			}
			else if(expired.equalsIgnoreCase("N")){
	 %>
	<html:form action="/rcContentUpload">
	<html:hidden property="methodName" value="sendRCSMS"/>
	<logic:equal name="USER_INFO" property="kmActorId" value="4">
	<div id="elementLabel"> 	<span class="text2 fll width160"><strong>Mobile Number:    </strong> </span></div>
	<%
	String docId = (String)request.getAttribute("docID");
	System.out.print("Docid set from request  -------  "+ request.getAttribute("docID"));
	%>
	<html:text name="kmRCContentUploadFormBean" property="mobileNo" styleId="mobileNo" />
	<html:hidden name="kmRCContentUploadFormBean" property="docId" value="<%=docId%>"/>
	
        <div class="button-area"  >
        <input type="submit" value="Send as SMS" alt="Send as SMS" onclick="return validateMobileNumber();" readonly="readonly"/>
        </div>
	</logic:equal>
	</html:form>
	
	<%	}
	} else{ %>
	<html:form action="/rcContentUpload">
	<html:hidden property="methodName" value="sendRCSMS"/>
	<logic:equal name="USER_INFO" property="kmActorId" value="4">
	<div id="elementLabel"> 	<span class="text2 fll width160"><strong>Mobile Number:    </strong> </span></div>
	<%
	String docId = (String)request.getAttribute("docID");
	System.out.print("Docid set from request  -------  "+ request.getAttribute("docID"));
	%>
	<html:text name="kmRCContentUploadFormBean" property="mobileNo" styleId="mobileNo" />
	<html:hidden name="kmRCContentUploadFormBean" property="docId" value="<%=docId%>"/>
	
        <div class="button-area"  >
        <input type="submit" value="Send as SMS" alt="Send as SMS" onclick="return validateMobileNumber();" readonly="readonly"/>
        </div>
	</logic:equal>
	</html:form>
	<%} %>
	
	

	<!-- End of SMS form -->


	<p></p><br>
     <div id="multiDocViewer"> 
     
     </div>
   
      <% 
        if ( !kmUserBean.isCsr())
        { 
        %>
        
   	   <div class="button-area2 clearfix">
     	  <div class="button2"><a href="rcContentUpload.do?methodName=editRcData&docID=<bean:write name="kmRCContentUploadFormBean" property="docId"/>"  class="red-btn" >Edit</a></div>    
   	   </div>
       <%
        }
        else
        {
       %>
        <div id="stayConnectLabel" style="height: 30px">&nbsp;</div>
      	<div class="button-area2 clearfix" id="stayConnectDiv">
    	 <div class="button2"><img src="common/images/button-stay-red.jpg" alt="stay" onclick="addToStay('documentAction.do?methodName=addToStay&viewerAction=rcContentUploadConnect.do&viewerMethodName=viewRCData&docID=<%= kmRCContentUploadFormBean.getDocId()%>')" width="76" height="35" border="0" /></div>  
    	 <div class="button2"><img src="common/images/button-connect.jpg" alt="connect" onclick=connect('<%= session.getAttribute("stayURL")%>') width="76" height="35" border="0" /></div>
     	</div>
   	   <%
   	   }
       %>
      
</div> 
    
<script type="text/javascript">
highlight(document.getElementById("mainContent"),'<%=request.getParameter("searchKeyword") %>' , "HL");
</script>
