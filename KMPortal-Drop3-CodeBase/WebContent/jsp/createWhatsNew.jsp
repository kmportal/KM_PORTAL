
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script language="JavaScript" type="text/javascript">

</script>



<body class="mLeft10 mTop2 mRight10" background="images/bg_main.gif"  onload="javascript: updateFile();">
<SCRIPT>
function updateFile()
{
	if(document.forms[0].status.value=="DUPLICATE")
			if (confirm("File with same name already exists!!! Do you want to update?"))
			{
					document.forms[0].methodName.value = "updateFile";
					document.forms[0].submit();
			}
			else
					document.forms[0].documentDesc.value="";
					
}
function formSubmit()
{
	document.forms[0].methodName.value = "insertFile";
	return true;
}
</SCRIPT>
<html:form action="/kmWhatsNew" enctype="multipart/form-data" >
<html:hidden property="methodName" />
<html:hidden property="status"/>
<BR>
<table width="75%" class="mLeft5" align="center" cellspacing="0" cellpadding="0">
    <tr>
						<td colspan="2" align="center" class="error">
						<strong> 
          					<html:messages id="msg" message="true">
                 				<bean:write name="msg"/>  
                          
             				</html:messages>
            			</strong>
            			</td>
					</tr>
					
  					<tr>
                          
						  <td colspan="2" align="center" class="error">
						  		<strong><font color=red> <html:errors /></font></strong>
						  </td>
						  <td></td>
                    </tr> 
    <tr align="center">
    		<td width="831"><span class="heading"><bean:message key="whatsNew.createWhatsNew"/> </span></td>
    </tr>
</table>
  <table width="55%" align="center" border="0" cellspacing="0" cellpadding="0">
  					
			
                    <tr class="lightBg" id="favCategoryTr" >
						<td align="right" class="text"><BR><bean:message key="createUser.category" />&nbsp;&nbsp;</TD>
						<TD align="left" colspan="3"><BR>
							<html:select property="categoryId" name="kmWhatsNewFormBean" >
								<html:option value="">-Select Category-</html:option>
								<logic:notEmpty name="kmWhatsNewFormBean" property="categoryList">
									<bean:define id="categories" name="kmWhatsNewFormBean"	property="categoryList" /> 
									<html:options labelProperty="elementName" property="elementId" collection="categories" />
								</logic:notEmpty>
							</html:select>
						</TD>  
					</tr>
                    <tr>
					  <TD class="lightBg" align="right"><bean:message key="whatsNew.selectFile" /><FONT color="red">*</FONT>&nbsp; </TD>
					  <td width="238" align="left" class="pBot2"><span class="width250">
                      <span class="width150"><font color="#003399">
                     <input type="file" name="newFile" />
                      </font></span></span>
                       </td>
                    </tr> 
                     
                    <tr>
					  <TD class="lightBg" align="right"><bean:message key="whatsNew.docDesc" /> &nbsp;&nbsp;</TD>
					  <td width="238" align="left" class="pBot2"><span class="width250">
                      <span class="width150" ><font color="#003399">
                      	<html:text property="documentDesc" styleClass="width230" size="25" maxlength="150"/>
                      </font></span>                      </span> </td>
                    </tr>
					
					
					<tr>
                          <td class="width125 text pLeft15">&nbsp;</td>
                          <td align="left" class="pTop5"><span class="wid250 pBot5"><span class="width250">
                          <INPUT type="Image" src="images/submit_button.jpg" onclick="javascript: return formSubmit();">
                          </span></span></td>
                    </tr>

                   
		    </table>
			

    
  

</html:form>
</body>

