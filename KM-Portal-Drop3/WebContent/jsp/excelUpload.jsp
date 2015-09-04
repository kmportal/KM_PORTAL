<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html>
<HEAD>
<LINK href="./theme/text.css" rel="stylesheet" type="text/css">
<TITLE>Excel Upload</TITLE>

<script language="javascript" src="jScripts/KmValidations.js">
</script>
<script>
function checkMouseEvent()
{
	if(event.button==2)
	{
		alert("This action is not Allowed!");
		return false;
	}
	else
	{
		return true;
	}
}

/*
**	function to check file selected fro upload and submit that file
*/
function formsubmit(){
	if(document.kmExcelUploadFormBean.newFile.value==""){
		alert("Please Select the file");
		return false;
	}else if(!(/^.*\.xls$/.test(document.kmExcelUploadFormBean.newFile.value))) {
			//Extension not proper.
			alert("Please select a .xls file only.");
			return false;
				
	}else{ 
		document.forms[0].filePath.value=document.kmExcelUploadFormBean.newFile.value;
		document.forms[0].methodName.value='initExcelProcessPopUp';
		return true;
	}
}

/*
**	method called onLoad to check message displayed and open processing page
*/
function getFile()
{
	filePath = document.kmExcelUploadFormBean.filePath.value;
	if((filePath!='0')&&(filePath!=''))
	{
		var status="incomplete";
		status=window.showModalDialog('kmExcelAction.do?methodName=initProcessExcel','dummydate',"resizable=yes,toolbar=yes,scrollbars=yes,menubar=yes,status=no,directories=no,modal=yes,location=no,width=500,height=500,left=5,top=10");
		if(status=='complete')
		{
			document.getElementById('completeProcessing').style.display='';
		}
		else if(status=='invalidSheet')
		{
			document.getElementById('invalidProcessing').style.display='';
		}
		else
		{	
			document.getElementById('incompleteProcessing').style.display='';
		}
	}
}

</script>

</HEAD>
<BODY onload="getFile();">

	<html:form action="/kmExcelAction" enctype="multipart/form-data">
		
		<html:hidden property="methodName" />
		<html:hidden property="filePath"/>
		<html:hidden property="detailStatus"/>
		<TABLE border="0" align="center" width="40%">
			<tr>
				<td colspan="2"><br><br></td>
			</tr>
			<tr align="center">
				<td colspan="2"><span class="heading">Waiver Matrix Uploading</span><BR></td>
			</tr>
			<tr align="center">
				<td colspan="2"  class="error"><strong>
					<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  
                	</html:messages>
            	</strong></td>
		   	</tr>
		   	<TR align="center">
				<TD  colspan="2" >
					<font color="RED" ><strong>
						<html:errors/>
	 				</strong></font>
				<TD >
			</TR>
			<tr>
				<td colspan="2"><BR></td></tr>
			<tr>
			<TR id="incompleteProcessing" style="display: none;">
				<TD  colspan="2" align="center" nowrap="nowrap">
					<font color="RED" size="2"><strong>Operation Cancelled By User !!</strong></font>
				<TD >
			</TR>
			<TR id="completeProcessing" style="display: none">
				<TD  colspan="2" align="center" nowrap="nowrap">
					<font color="GREEN" size="2"><strong>Data Saved Successfully</strong></font>
				<TD >
			</TR>
			<TR id="invalidProcessing" style="display: none">
				<TD  colspan="2" align="center" nowrap="nowrap">
					<font color="RED" size="2"><strong>File Uploaded does not match the Template !!</strong></font>
				<TD >
			</TR>
			<tr>
				<td colspan="2"><BR></td></tr>
			<tr>
			
			</tr>
			<TR>
				<TD align="center">Select File : </TD>
				<TD align="center">						
						<html:file property="newFile" onkeypress="javascript: return false;" onkeydown="javascript: return false;" onkeyup="javascript: return false;" onmousedown="javascript:checkMouseEvent();" />
				</TD>
			</TR>
			<TR>
				<TD colspan="2">&nbsp;&nbsp;</TD>
			</TR>
			<TR>
				<TD colspan="2">&nbsp;&nbsp;</TD>
			</TR>
			<tr align="center">
				<td align="center" colspan="2">
					<input type="image" src="images/submit_button.jpg"  onclick="return formsubmit();"/>
				</td>
			</TR>
		</TABLE>
		
		<BR>
		<BR>
		<TABLE border="0" align="center" width="40%">
			<tr>
				<td colspan="2" nowrap="nowrap">
					<span style="color: red">*</span>AES 1 sheet is mandatory
				</td>
			</tr>
			<tr>
				<td colspan="2" nowrap="nowrap">
					<span style="color: red">*</span>BILLPLAN sheet is mandatory 
				</td>
			</tr>
			<tr>
				<td colspan="2" nowrap="nowrap">
					<span style="color: red">*</span>Sheets with company and bill plan mapping details should be named beginning with AES 
				</td>
			</tr>
			<tr><td colspan="2"><br></td></tr>
			<TR align="center">
				<td colspan="2"><font color="red" size="2">To Download Waiver Matrix Template </font><a href="kmExcelAction.do?methodName=downloadDocument">click here</a></td>
			</TR>
		</TABLE>
	</html:form>
	
</BODY>
</html:html>
