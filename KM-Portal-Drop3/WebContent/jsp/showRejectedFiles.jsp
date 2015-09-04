<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="JavaScript" type="text/javascript">

		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;	
		
	function getList()
	{
	
		if(document.getElementById("date_selector").value == "")
		{
			alert("Please select From date");
			return false;
		}
		if(document.getElementById("date_selector2").value == "")
		{
			alert("Please select To date");
			return false;
		}
		
		//Bug : MASDB00064495 resolved
		if(document.getElementById("date_selector").value > document.getElementById("date_selector2").value)
		{
			alert("From Date Cannot Be Greater Than To Date" );
			return false;
		}
		if(document.getElementById("date_selector").value > curr_dt)
		{
			alert("From Date cannot be a future date ");
			return false;			
		}
		if(document.getElementById("date_selector2").value > curr_dt)
		{
			alert("To Date cannot be a future date ");
			return false;			
		}
		
		return true;
	}
	
	function showFile(filePath)
	{
		
		document.forms[0].methodName.value = "displayFile";
		document.forms[0].displayFilePath.value = filePath;
		document.forms[0].submit();
	//	window.frames["files"].document.forms[0].submit();	
	
		
		return false;
	}
</script>


<body class="mLeft10 mTop2 mRight10"  >
<html:form action="/rejectedFile" name="rejectedFileFormBean" type="com.ibm.km.forms.KmRejectedFileFormBean">
<html:hidden property="methodName" value="getFileList"/>
<html:hidden property="displayFilePath"/>

<table width="99%" align="center"  cellpadding="0" cellspacing="0" >
<tr>
<td width="80%" rowspan="2" align="right" valign="top">
<logic:notEqual name="rejectedFileFormBean" property="showFile" value="false">
		<% System.out.println("Hello world ");%>
	<%--	<IFRAME src='.<bean:write name="rejectedFileFormBean" property="filePath" />' width="100%" height="450" align="right"></IFRAME> --%>
	<%--	<% 
			String filePath = "C:/KMWQ/KM/WebContent/test/Mini SEF Fields.doc" ;
		
			java.io.File file = new java.io.File(filePath);
			//file.delete();	
		%> --%>
	</logic:notEqual>
	
	<logic:equal name="rejectedFileFormBean" property="showFile" value="false">
  <table width="100%" class="mLeft5" align="left" cellspacing="0" cellpadding="0">
    <tr align="right" >
      <td width="100%" height="378" align="left" valign="top" class="pRight10 pTop5">
 
    	<span class="content-upload"><h1>Rejected File List</h1></span>
    
			<ul class="list2 form1">
				
					     <li class="clearfix padd10-0" >
					     	<center><h3>Please select file uploaded date</h3></center>
					     </li>
					     
					     	<li class="clearfix alt" >
			<span class="text2 fll" style="width:165px"><strong><bean:message key="scroller.startDate"/></strong></span>							
				<span class="text2 fll width150">
				<input type="text" class="tcal calender2 fll" id="date_selector" readonly="readonly" name="startDate" value="<bean:write property="fromDate" name="rejectedFileFormBean" />"/> </span>
                	
       	
			<span class="text2 fll" style="margin-left:40px;width:165px "><strong><bean:message key="scroller.endDate"/></strong></span>			
				<span class="text2 fll width150">
				<input type="text" class="tcal calender2 fll" id="date_selector2" readonly="readonly" name="endDate" value="<bean:write property="toDate" name="rejectedFileFormBean" />"/> </span>
                	 
                	
		</li>
          	<li class="clearfix" >
			<span class="text2 fll">&nbsp;</span>
				<center><INPUT type="Image" src="images/submit.jpg"  onclick="return getList();"/>
				</center>
              </li>
             	
         			 </ul>
					
					
			
				<table width="75%" align="center" border="0" cellspacing="0" cellpadding="0"> 
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
	<logic:notEmpty name="rejectedFileFormBean" property="fileList" >
			
		<table width="100%" class="mTop30" align="center">

		<tr class="text white">
			<td class = "darkRedBg height19"><span class="mLeft5 mTop5" >S.NO</span> </td>
			<td class = "darkRedBg height19"><span class="mLeft5 mTop5" >Document Name</span> </td>
			<td class = "darkRedBg height19"><span class="mLeft5 mTop5" >Document Path</span> </td>
			<td class = "darkRedBg height19"><span class="mLeft5 mTop5" >Rejection Comment</span> </td>
		</tr>
		<logic:empty name="rejectedFileFormBean" property="fileList" >
			<TR class="lightBg">
				<TD class="text" align="center">No File Found Under Rejection</TD>
			</TR>
		</logic:empty>
		<logic:iterate name="rejectedFileFormBean" id="file" indexId="i"
				property="fileList"  >
				<TR class="lightBg">
					
					
					<TD class="text" align="center"><%=(i.intValue() + 1)%>.</TD>
					<TD class="txt" align="center"><font color="red"><A href="documentAction.do?methodName=displayDocument&docID=<bean:write name="file" property="documentId"/>" ><bean:write name="file"
						property="docName" /></A></font></TD>
					<TD class="txt" align="center"><bean:write name="file"
						property="docCompletePath"/> </TD>
				
					<TD class="txt" align="center"><bean:write name="file"
						property="comment"/> </TD>
				</TR>
		</logic:iterate>
		
	</table>
				
	</logic:notEmpty>                    
			  <br>
		    <DIV class="scrollacc" style="OVERFLOW: auto; WIDTH: 100%; HEIGHT: 240px" align="center">		    </DIV>
</td>
    </tr>
  </table>
  </logic:equal>
  </td>

</table>

</html:form>
</body>

