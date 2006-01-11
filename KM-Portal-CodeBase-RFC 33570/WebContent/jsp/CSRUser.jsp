<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>
<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">
<html>
<head>
<% 		HttpSession Session = request.getSession(false);
String csrfFlag = com.ibm.km.common.Utility.getRandomCode();
			String csrf = csrfFlag;
			Session.setAttribute("csrfFlag" , csrfFlag);
%>
<SCRIPT>
function formValidate(){
				
		var mail = document.kmCSRuserFormBean.email.value;
//		alert("email is "+mail);
        var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        var len=500;
        var comment =  document.kmCSRuserFormBean.comment.value;
        
        if(isEmpty(document.kmCSRuserFormBean.email))
			{
				alert("Please enter Email-ID");
				document.kmCSRuserFormBean.email.focus();
				return false;
			}
			  if(isEmpty(document.kmCSRuserFormBean.olm_id))
			{
				alert("Please enter OLM ID");
			    document.kmCSRuserFormBean.olm_id.focus();
			    return false;
			}
		/*if (!filter.test(document.kmCSRuserFormBean.email)) {
             alert('Please provide a valid email address');
             document.kmCSRuserFormBean.email.focus();
             return false;
            } */
 
 if(!isEmailAddress(document.kmCSRuserFormBean.email))
{
alert("Please select a Email address");
return false;
}
      if(document.kmCSRuserFormBean.comment.value=="")
		{
                 alert("Please enter Comment");
        return false;
        }    
        if (comment.length > len) {
                alert ("Please limit comments to "+len+" characters.");
                //textArea.value = textArea.value.substr(0,length-1);
                return false;
            }
         /*  else{
           document.forms[0].submit();
           }*/
        var csrf=document.getElementById("csrf").value;
		document.forms[0].action = document.forms[0].action//+"&csrf="+csrf;
        
		document.forms[0].submit();
		
		
		}
		
		
</SCRIPT>
</head>
<body>
<html:form action="/kmCSRUser"  name="kmCSRuserFormBean" type="com.ibm.km.forms.KmCSRuserFormBean" scope="request">
<html:hidden property="methodName" value="unspecified"/>
<input type="hidden" value="<%=csrf%>" name="csrf" id="csrf" />


	<TABLE align="center"> 
			<TBODY>
			<tr>
               <td height="44" width="34%"><b><font size="3">E-mail To</font></b> </td>
               <td height="44" width="198"> 
               <bean:define id="emailList" name="emailList"  type="java.util.ArrayList" scope="request"/>
               <bean:define id="descList" name="descList"  type="java.util.ArrayList" scope="request"/>
               <html:select  styleId="email" name="kmCSRuserFormBean" property="selectedEmail">
					<option value="0" selected="selected">-----Select Email-----</option>
					<logic:iterate name="emailList" id="email" indexId="i" type="java.lang.String">
									<html:option value='<%=email%>'> <%= descList.get(i) %></html:option>
					</logic:iterate>
			   </html:select>
             </td>
			
			
			
			<tr>
			
               <td height="44" width="34%"><b><font size="3">Olm Id</font></b> </td> 
               <td height="45" width="198"> <input type="text"
					name="olm_id" id="olm_id" value="" size="40" /></td></tr>
               
             <tr>
               <td width="34%" height="103"><b><font size="3">Comment</font> </b> </td> 
               <td width="198" height="103"><div><textarea
					name="comment" cols="38" rows="6" ></textarea></div></td></tr>
					
					
		<li class="clearfix">
		<div class="button" style="margin-left: 160px">
		</div>
		</li>
		
		<logic:messagesPresent message="true" name="messages">		
        	<html:messages id="messages" message="true"> <li type="square"><b><Font size="3" color="red"><bean:write name="messages" /></Font><b/></li></html:messages>
        </logic:messagesPresent>
        
        <c:catch>
        <!--<logic:messagesPresent message="true" name="usermessages">
		
        	<html:messages id="usermessages" message="true"> <li type="square"><b><Font size="3" color="red"><bean:write name="usermessages" /></Font><b/></li></html:messages>
        </logic:messagesPresent>
        </c:catch>
                            
       --></TBODY>
	</TABLE>
					             
    </html:form>

<p><input src="images/submit.jpg" name="" type="image"	onclick="return formValidate();"></p>
</body>
</html>