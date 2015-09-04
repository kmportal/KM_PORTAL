<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<LINK href="theme/text.css" rel="stylesheet" type="text/css">
<script language="JavaScript" src="jScripts/KmValidations.js" type="text/javascript"> </script>
<script language="JavaScript" type="text/javascript">
	
	function IsEmpty(objInput)
	{
		var whitespace = " \b\t\n\r";
 		theInput = trimValue(objInput);
		theLength = theInput.length;
		if((theInput == null) || (theLength == 0))
			return (true);
  		for (var i = 0; i < theLength ; i++)
  		{
    		var theChar = theInput.charAt(i);
			if (whitespace.indexOf(theChar) == -1)
    			return (false);
   		 }
			return (true);
	}

	function getForm() {
		return document.forms[0];
	}
	
	var isDirty = false;
	
	function selectAll(){
			var objForm = getForm();
			//var itemCount = getFormElementCount(objForm, 'chkAssign');
			for (var i=0; i<document.forms[0].approveFileList.length; i++) {
				getFormIndexedElement(objForm, 'approveFileList', i).checked = true;
				//getFormIndexedElement(objForm, 'approveFileList', i).value = 'true';
			}
			return false;
		}	
		
		function getFormElementCount(objForm, elementName) {
			return getElementCount(objForm.elements[elementName]);
		}
		
		function getElementCount(element) {
			if (element == null) {
				return 0;
			} else if (element.length == null) {
				return 1;
			} else {
				return element.length;
			}
	}
	
	function getFormIndexedElement(objForm, elementName, index) {
		return getIndexedElement(objForm.elements[elementName], index);
	}
	
	function getIndexedElement(element, index) {
		if (element == null) {
			return null;
		} else if (element.length == null) {
			return element;
		} else {
			return element[index];
		}
	}
	
		function clearAll(){
			var objForm = getForm();
			if(document.approveFileFormBean.approveFileList!=null){
				for (var i=0; i<document.approveFileFormBean.approveFileList.length; i++) {
					getFormIndexedElement(objForm, 'approveFileList', i).checked = false;
					//getFormIndexedElement(objForm, 'approveFileList', i).value = 'false';
				}
			}
			return false;
		}
	
	function submitForApproval()
	{

		document.forms[0].methodName.value = "approveFiles";
		 var j =0;

	
		for(var i = 0;i < document.approveFileFormBean.approveFileList.length;i++)
		{
			if(document.approveFileFormBean.approveFileList[i].checked)
			{
				j = j + 1;

			} 
	
		}
		if(j==0)
		{
			alert("Please Select Files To Approve" );
			return false;
		}
		return true;
		
	}

	function submitForRejection()
	{
		document.forms[0].methodName.value = "rejectFiles";
		
		var j = 0;

		for(var i = 0;i < document.approveFileFormBean.approveFileList.length;i++)
		{
			if(document.approveFileFormBean.approveFileList[i].checked)
			{
				j = j + 1;
		
				
			}
		
		}
		if(j==0)
		{
			alert("Please Select Files To Reject" );
			return false;
		}
		return true;
		
	}

	function getList()
	{
		clearAll();
		document.forms[0].methodName.value = "getFileList";
		return true;
	}
	
	function showFile(filePath)
	{
		
		document.forms[0].methodName.value = "displayFile";
		document.forms[0].displayFilePath.value = filePath;
		document.forms[0].submit();
	//	window.frames["files"].document.forms[0].submit();	
	
	//	alert(filePath);
		return false;
	}
	
	
</script>




<html:form action="/approveFile" name="approveFileFormBean" type="com.ibm.km.forms.KmApproveFileFormBean">
<html:hidden property="methodName" />
<html:hidden property="displayFilePath"/>

<table width="99%" align="center"  cellpadding="0" cellspacing="0" class="border">
<TR>
<td width="80%" rowspan="2" align="right" valign="top">
	
	<logic:notEqual name="approveFileFormBean" property="showFile" value="false">
		<% System.out.println("Hello world ");%>
		<IFRAME src='.<bean:write name="approveFileFormBean" property="filePath" />' width="100%" height="450" align="right"></IFRAME>
	<%--	<% 
			String filePath = "C:/KMWQ/KM/WebContent/test/Mini SEF Fields.doc" ;
		
			java.io.File file = new java.io.File(filePath);
			//file.delete();	
		%> --%>
	</logic:notEqual>
	
	<logic:equal name="approveFileFormBean" property="showFile" value="false">
  		<table width="100%" class="mLeft5" align="right" cellspacing="0" cellpadding="0">
   			 <tr align="right" >
      			<td width="100%" height="378" align="right" valign="top" class="pRight10 pTop5">
   <%--   <p class="rightAlign pRight10"><span class="black10Bold">View Report</span>
    <select name=Searchn onchange="">
      <option value="" selected>Choose Report</option>
      <option value="">Files Added/Edited/Delete for any selected day</option>
      <option value="">Order Number</option>
      <option value="">Invoice Number</option>
      <option value="">Receipt Number</option>
    </select>
</p> --%>
<table width="75%" class="mLeft5" align="center" cellspacing="0" cellpadding="0">
    <tr align="center">
    		<td width="831"><span class="heading">Approve/Reject Files </span></td>
    </tr>
</table>
  <table width="55%" align="center" border="0" cellspacing="0" cellpadding="0">
				
  		<logic:notEqual name="approveFileFormBean" property="status" value="init">
 		<tr>
			
			<td colspan="2" align="left" class="error"><br>
			<strong> <html:errors /></strong></td>
		</tr>
		<tr>
				<td colspan="4" align="center" class="error">
					<strong> 
          			<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
                          
             		</html:messages>
            		</strong>
            	</td>
			</tr>
		</logic:notEqual>
	 </table>
	<logic:notEmpty name="approveFileFormBean" property="fileList" >
			
		<table width="100%" class="mTop30" align="left">

	<%--	<tr align="center">
			<td colspan="9"><span class="heading"><bean:message
				key="viewAllUser.ViewUsers" /></span></td>
		</tr>
		<tr align="left">
			<td colspan="9"><strong><bean:write property="message"
				name="arcViewUserFormBean" /></strong></td>
		</tr> --%>
		
		<TR>
		<TD>
		<html:multibox property="approveFileList" name="approveFileFormBean" style="visibility:hidden">0</html:multibox>
		</TD>
		
		<html:textarea property="commentList" name="approveFileFormBean" cols="10" rows="2" style="visibility:hidden"></html:textarea>
		
		</TR><tr>
		  <td> 
          	<INPUT type="Image" src="images/btnSelectAll.gif" width="59" height="17" border="0" onclick=" return selectAll();">
          </td>
          <td>
          	<INPUT type="Image" src="images/btnClearAll.gif" width="59" height="17" border="0" onclick="return clearAll();">
          </td>
          
          
          </tr>              
		<tr class="text white">
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"> Select</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">S.NO</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Document Name</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Document Path</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Uploaded By</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Uploaded Dt</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Comment</span></td>
			
		</tr>
		
		
		
		<logic:iterate name="approveFileFormBean" id="file" indexId="i"
				property="fileList">
				
				<TR class="lightBg">
					<TD class="text" align="center">
					
					
					<html:multibox property="approveFileList" name="approveFileFormBean" >
						<bean:write name="file" property="documentId"/>
					</html:multibox>
					</TD>
					
					<TD class="text" align="left" width="20"><%=(i.intValue() + 1)%>.</TD>
					<TD class="text" align="left" width="160"><A Href="documentAction.do?methodName=displayDocument&docID=<bean:write name="file" property="documentId"/>" ><bean:write name="file"
						property="docName" /></A></TD>
					<TD align="left"><html:textarea  disabled="true"  rows="3" cols="30" name="file" property="docStringPath"/></td>	
					<TD class="text" align="left" width="80"><bean:write name="file"
						property="userName" /></TD>	
					<TD class="text" align="left" width="100" ><bean:write name="file"
						property="uploadedDt" /></TD>
					<TD class="text" align="left" width="120">
					
					<html:textarea property="commentList" name="approveFileFormBean" cols="10" rows="2"></html:textarea>
				</TD>
		<%--		<TD class="text" align="center">
								
					
					
					<IFRAME SRC="\\10.13.72.67\anil\hibernate.doc">My File</IFRAME>
					
				</TD> --%>
				</TR>
		</logic:iterate>
		<tr>
                          <td class="width125 text pLeft15" colspan="2">&nbsp;</td>
                          <td align="center" class="pTop5" width="107"><span class="wid250 pBot5"><span class="width250">
                          <INPUT type="Image" src="images/approve_blue_but.jpg" onclick="return submitForApproval();">
                          </span></span></td>
                          <td align="left" class="pTop5" width="312"><span class="wid250 pBot5"><span class="width250">
                          <INPUT type="Image" src="images/reject_blue_but.jpg" onclick="return submitForRejection();">
                          </span></span></td>
         </tr>

	</table>
	
	
	
	</logic:notEmpty>                    
			  <br>
</td>
    </tr>
  </table>
  </logic:equal>
  </td>

</table>
</html:form>

<logic:equal name="approveFileFormBean" property="showFile" value="false">

</logic:equal>

