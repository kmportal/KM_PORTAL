<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<LINK href="theme/text.css" rel="stylesheet" type="text/css">
<script language="JavaScript" type="text/javascript">
	
	function submitForm()
	{
		document.forms[0].methodName.value = "submitFiles";
		
		var j = 0;
		for(var i = 0;i < document.forms[0].submitFileList.length;i++)
		{
			if(document.forms[0].submitFileList[i].checked)
			{
				j = j + 1;
				
			}
		}
		if(j==0)
		{
			alert("Please Select Files To Submit" );
			return false;
		}
		return true;
	}
	
	function getList()
	{
		document.forms[0].methodName.value = "getFileList";
	//	alert("Date value is " + document.forms[0].dateValue.value);
		if(document.forms[0].fromDate.value == "")
		{
			alert("Please Select From Date" );
			return false;
		}
		if(document.forms[0].toDate.value == "")
		{
			alert("Please Select To Date" );
			return false;
		}
		if(document.forms[0].fromDate.value > document.forms[0].toDate.value)
		{
			alert("From Date Cannot Be Greater Than To Date" );
			return false;
		}
		return true;
	}
	
</script>

<html:form action="/submitFile" >
<html:hidden property="methodName" value="submitFiles"/>

<table width="99%" align="center" background="images/bg_main.gif" cellpadding="0" cellspacing="0" class="border">
 <tr>
<td width="80%" rowspan="2" align="right" valign="top">
  <table width="100%" class="mLeft5" align="right" cellspacing="0" cellpadding="0" >
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
    		<td width="831"><span class="heading"><bean:message key="file.submit" /> </span></td>
    </tr>
</table>
  <table width="55%" align="center" border="0" cellspacing="0" cellpadding="0">
					<tr>
					  <TD colspan="4" align="center" height="20"><h3>Please select file uploaded date</h3></TD>
                    </tr>
					<tr>
						<td colspan="2"><bean:message key="file.fromDate" />&nbsp;<html:text property="fromDate" name="submitFileFormBean" readonly="true"
							styleId="date_field" size="14" styleClass="box" /> <IMG src="images/cal.gif" name="selectDate"
							width="16" height="16" alt="Select Date" id="date_selector"></td>

						<td colspan="2"><bean:message key="file.toDate" /> <html:text
							property="toDate" name="submitFileFormBean" readonly="true"
							styleId="date_field2" size="14" styleClass="box" /> <img
							src="images/cal.gif" name="selectDate" width="16" height="16"
							alt="Select Date" id="date_selector2"></td>
					</tr>
					<tr>
						<TD colspan="4" align="center" height="20"><INPUT
							type="Image" src="images/submit_button.jpg"  onclick="return getList();"></td>
					</tr>
  
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
	 </table>
	<logic:notEmpty name="submitFileFormBean" property="fileList" scope="request">
			
		<table width="75%" class="mTop30" align="center" border="0">

	<%--	<tr align="center">
			<td colspan="9"><span class="heading"><bean:message
				key="viewAllUser.ViewUsers" /></span></td>
		</tr>
		<tr align="left">
			<td colspan="9"><strong><bean:write property="message"
				name="arcViewUserFormBean" /></strong></td>
		</tr> --%>
		<TR class="lightBg">
					<TD class="text" align="center">
					<html:multibox property="submitFileList"
					name="submitFileFormBean" style="visibility:hidden">0</html:multibox>
					</TD>
					
					<TD class="text" align="center"></TD>
					<TD class="txt" align="center"></TD>
					<TD class="txt" align="center"></TD>
					<TD class="txt" align="center"></TD>
		</TR>
		<tr class="text white">
			<td bgcolor="a90000" align="center"><FONT color="white"> Select</FONT></td>
			<td bgcolor="a90000" align="center"><FONT color="white">S.NO</FONT> </td>
			<td bgcolor="a90000" align="center"><FONT color="white">Document Name</FONT> </td>
			<td bgcolor="a90000" align="center"><FONT color="white">Category</FONT> </td>
			<td bgcolor="a90000" align="center"><FONT color="white">Sub-Category</FONT> </td>
		</tr>
		
		<logic:iterate name="submitFileFormBean" id="file" indexId="i"
				property="fileList">
				<TR class="lightBg">
					<TD class="text" align="center">
					<html:multibox property="submitFileList"
					name="submitFileFormBean"><bean:write name="file" property="documentId"/></html:multibox>
					</TD>
					
					<TD class="text" align="center"><%=(i.intValue() + 1)%>.</TD>
					<TD class="txt" align="center"><bean:write name="file"
						property="docName" /></TD>
					<TD class="txt" align="center"><bean:write name="file"
						property="categoryName" /></TD>
					<TD class="txt" align="center"><bean:write name="file"
						property="subCategoryName" /></TD>
				</TR>
		</logic:iterate>
		<tr>
                          <td class="width125 text pLeft15" colspan="3">&nbsp;</td>
                          <td align="left" class="pTop5"><span class="wid250 pBot5"><span class="width250">
                          <INPUT type="Image" src="images/submit_button.jpg" onclick="javascript: return submitForm();">
                          </span></span></td>
         </tr>

	</table>
				
	</logic:notEmpty>                    
			  <br>
</td>
    </tr>
  </table></td>
  </tr>
</table>

</html:form>

<script>
Calendar.setup({
        inputField     :    "date_field",      // id of the input field
        ifFormat       :    "%Y-%m-%d",       // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "date_selector",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1
    });
Calendar.setup({
        inputField     :    "date_field2",      // id of the input field
        ifFormat       :    "%Y-%m-%d",       // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "date_selector2",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1
    });

</script>

