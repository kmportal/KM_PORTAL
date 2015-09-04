
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<TITLE>CreateSubCategory.jsp</TITLE>
<html:errors />

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
	</script>
	
	 
<script language="javascript">  


function resetFields()
{
	document.forms[0].circleId.value="-1";
	document.forms[0].categoryId.value="-1";
	document.forms[0].subCategoryName.value="";
	document.forms[0].subCategoryDesc.value="";
	document.forms[0].categoryId.length="1";
	return false;
	
}

var circle;
function formValidate(){
	
	var form=document.forms[0];
	var searchChars="`~!$^&*()=+><{}[]+|=?':;\\\"-&-,@";
	if(form.circleId.value=="-1")
	{
	   alert("Please Select a Circle");
	   return false;
	}
	if(form.categoryId.value=="-1")
	{
		alert("Please Select a Category");
		return false;
	}
	if(form.subCategoryName.value=="")
	{
		alert("Please Enter a Sub-Category name");
		form.subCategoryName.focus();
		return false;
	}	

	if(isEmpty(document.kmSubCategoryMstrFormBean.subCategoryName))
	{
				alert("Please Enter a Sub-Category name");
				document.kmSubCategoryMstrFormBean.subCategoryName.value="";
				document.kmSubCategoryMstrFormBean.subCategoryName.focus();
				return false;
				
	}
		
	
	return true;
}

function loadCategories(circleId)
{
		
	//alert("Load Categoried for CircleId :"+circleId);
	document.forms[0].methodName.value="loadCategory";
	document.forms[0].submit(); 
	//clearFileds();	
}

</script>



<FONT color="red"><html:errors/></FONT>
<html:form action="/kmSubCategoryMstr" >
	<html:hidden property="methodName" value="insert"/>
     

	<BR><BR><BR><BR>
	<TABLE align="center"> 
			<TBODY>
			<TR align="center">
				<td colspan="5" class="lightBg"><span class="heading"><bean:message
					key="createSubCategory.title" /></span></td>
			</TR>
			<tr>
			<td colspan="2" align="center" class="error">
			<strong> 
          	<html:messages id="msg" message="true">
                 <bean:write name="msg"/>  
                          
             </html:messages>
            </strong>
            </td>
		   </tr>
				
				<TR>
					<TD align="left" class="lightBg"><bean:message key="createSubCategory.circle" />&nbsp;
									</TD>
									
					<logic:notEmpty name="kmSubCategoryMstrFormBean" property="circleList">
					<TD align="left" colspan="3">
					<bean:define id="circles" name="kmSubCategoryMstrFormBean"
									property="circleList" /> 
									<html:select property="circleId" name="kmSubCategoryMstrFormBean" onchange="javascript:loadCategories(this.value);">
									<html:option value="-1">-Select Circle-</html:option>
									<html:options labelProperty="circleName" property="circleId"
										collection="circles" />
					</html:select></TD>  </logic:notEmpty>
					<logic:empty name="kmSubCategoryMstrFormBean" property="circleList">
					<TD align="left" colspan="3">
					
									<html:select property="circleId" name="kmSubCategoryMstrFormBean" >
									<html:option value="-1">-Select Circle-</html:option>
					</html:select></TD>  
					  
					</logic:empty>
					
				</TR>
			<TR>
					<TD align="left" class="lightBg"><bean:message key="createSubCategory.category" />&nbsp;
									</TD>
									
					<logic:notEmpty name="kmSubCategoryMstrFormBean" property="categoryList">
					<TD align="left" colspan="3">
					<bean:define id="categories" name="kmSubCategoryMstrFormBean"
									property="categoryList" /> 
									<html:select property="categoryId" name="kmSubCategoryMstrFormBean"  >
									<html:option value="-1">-Select Category-</html:option>
									<html:options labelProperty="categoryName" property="categoryId"
										collection="categories" />
					</html:select></TD>  </logic:notEmpty>
					<logic:empty name="kmSubCategoryMstrFormBean" property="categoryList">
					<TD align="left" colspan="3">
					
									<html:select property="categoryId" name="kmSubCategoryMstrFormBean" >
									<html:option value="-1">-Select Category-</html:option>
					</html:select>&nbsp;&nbsp;&nbsp;</TD>  
					
					 
					</logic:empty>
				
			</TR>

			<TR>
				<TD><bean:message key="createSubCategory.subCategoryName" /> </TD>
				<TD><html:text property="subCategoryName" name="kmSubCategoryMstrFormBean" maxlength="60"/></TD>
			</TR>
			<TR>
				<TD><bean:message key="createSubCategory.subCategoryDesc" /></TD>
				<TD><html:text property="subCategoryDesc" name="kmSubCategoryMstrFormBean" maxlength="150"/></TD>
			
			</TR>

			
			<TR>
				<TD><BR>&nbsp;&nbsp;&nbsp;<FONT color="red"></FONT><BR><BR>&nbsp;&nbsp;&nbsp;
				<input type="image" src="images/submit_button.jpg" onclick="return formValidate();"/>
                          	<a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="image" src="images/reset_button.jpg"  value="Reset" height="20" border="0" onclick="return resetFields();"></a></TD>
				 <logic:empty name="kmSubCategoryMstrFormBean" property="circleList">
				
				<TD align="left">
					
				</TD>  
				</logic:empty>
			</TR>
			
		</TBODY>
	</TABLE>
	
	
</html:form>
