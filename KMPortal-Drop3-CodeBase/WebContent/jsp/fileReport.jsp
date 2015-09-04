<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:errors />
<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%=request.getServerPort()%>';
	var serverName = '<%=request.getServerName()%>';

function $id(id) {
	return document.getElementById(id);
}
 
function loadCategories()
{
	//alert("asdashd");
	document.forms[0].methodName.value="loadCategory";
	document.forms[0].submit();
		
}
function loadSubCategories()
{

	document.forms[0].methodName.value="loadSubCategory";
	document.forms[0].submit();
	document.forms[0].subCategoryId.value="0";
	
}
function getList()
{	
		if(document.forms[0].reportType.value=="")
		{
			alert("Please Select a Report Type");
			return false;
		}
		if(document.forms[0].fromDate.value == "")
		{
			alert("Please Select the Date" );
			return false;
		}
		
	var txtdt1=document.forms[0].fromDate.value;	
	y1=txtdt1.charAt(0)+txtdt1.charAt(1)+txtdt1.charAt(2)+txtdt1.charAt(3)
	m1=  ""+txtdt1.charAt(5)+txtdt1.charAt(6)	
		if(txtdt1.charAt(8)!=0)
			d1=txtdt1.charAt(8)+txtdt1.charAt(9);
     else 
			d1=txtdt1.charAt(9)
			
		//	alert("date"+d1);

	
	
	if(m1=="01")
	m1=0
	if(m1=="02")
	m1=1
	if(m1=="03")
	m1=2
	if(m1=="04")
	m1=3
	if(m1=="05")
	m1=4
	if(m1=="06")
	m1=5
	if(m1=="07")
	m1=6
	if(m1=="08")
	m1=7
	if(m1=="09")
	m1=8
	if(m1=="10")
	m1=9
	if(m1=="11")
	m1=10
	if(m1=="12")
	m1=11
	var date1 =new Date(y1, m1, d1);
	var currDate = new Date();
	//currDate.setDate(currDate.getDate());
	if(date1 > currDate) {
		//Bug MASDB00064489  Resolved
		alert("Selected Date Cannot be A Future Date");
		return false;
	}
	
	   document.forms[0].methodName.value = "viewReport";
		return true;	

		
		
}
	
function checkSelectedOption(e) {
    if(document.forms[0].reportType.value == "totalFileCount")  {
       return true;

     }	
   }
function importExcel1(){
document.kmFileReportForm.methodName.value="FilesDeletedExcelReport";
document.kmFileReportForm.submit();
 
 }
 function importExcel2(){
document.kmFileReportForm.methodName.value="FileAddedExcelListReport";
document.kmFileReportForm.submit();
 
 }
 function importExcel(){
 document.kmFileReportForm.methodName.value="FilesAddedExcelReport";
 document.kmFileReportForm.submit();
 }
function reset()
{
	
	return true;
}
</SCRIPT>
<html:form action="/fileReport" name="kmFileReportForm" type="com.ibm.km.forms.KmFileReportFormBean">
<html:hidden property="methodName" />
<html:hidden property="parentId" styleId="parentId" />
<html:hidden property="levelCount" styleId="levelCount" />
<html:hidden property="elementLevel" styleId="elementLevel" />
<html:hidden property="buttonType" />
<div class="box2">
 <div class="content-upload">
  <H1>File Report</H1>
	<ul class="list2 form1">
	    <li class="clearfix alt">
				<span class="text2 fll width160"><strong>File Report Type<font
				color="red" size="2"> *</font></strong></span>
			<html:select property="reportType" styleClass="select1" name="kmFileReportForm">
				<html:option value="">Select</html:option>
				<%--        	<html:option value="approvedReport">Approved Files</html:option> --%>
				<%--    	<html:option value="hitReport">No. of Hits</html:option>  --%>
				<html:option value="filesAdded">Files Added Circle Wise</html:option>
				<html:option value="filesDeleted">Files Deleted Circle Wise</html:option>
				<html:option value="filesAddedCircle">Files Added by Users</html:option>
			</html:select>
		</li>

		<li class="clearfix">
				<span class="text2 fll width160"><strong>Select Date</strong></span>
			
			<input type="text" class="tcal calender2 fll" readonly="readonly" name="fromDate" value="<bean:write property='fromDate' name='kmFileReportForm'/>"/>
		</li>
		<li class="clearfix" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span><INPUT type="Image" src="images/submit.jpg" onclick="return getList();"><br><strong> <html:errors /></strong>
		</li>
	</ul>

	<logic:equal name="kmFileReportForm" property="reportType" value="approvedReport">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<logic:notEmpty name="kmFileReportForm" property="approverList">
				<tr class="text white">
					<td class="darkRedBg height19"><span class="mLeft5 mTop5">S.No</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5">Approver Id</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5">Name</span></td>
					<td class="darkRedBg height19"><span class="mLeft5 mTop5">No.Of	Files Approved</span></td>
				</tr>
				<logic:iterate name="kmFileReportForm" id="file" indexId="i" property="approverList">
					<% String cssName = "";	if( i%2==1){cssName = "alt";}%>
					<tr class="<%=cssName%>">
						<TD class="text" align="center">&nbsp;&nbsp;<%=(i.intValue() + 1)%>.</TD>
						<TD class="text" align="center"><bean:write name="file" property="approvalId" /></TD>
						<TD class="text" align="center"><bean:write name="file"	property="approverName" /></TD>
						<TD class="text" align="center"><bean:write name="file"	property="noOfApproved" /></TD>
					</TR>
				</logic:iterate>
				<font color="red" size="2">Total Files=<bean:write name="file"
					property="total" /></font>
			</logic:notEmpty>
			<tr>
				<td colspan="2" class="error" align="left"><html:messages id="msg" message="true"><bean:write name="msg" /></html:messages></td>
			</tr>
		</table>

	</logic:equal>

	<logic:equal name="kmFileReportForm" property="reportType"
		value="filesAdded1">

		<TABLE width="100%"  cellpadding="0" cellspacing="0" border="0">
			<logic:notEmpty name="kmFileReportForm" property="hitList">
				<TR class="text white">
					<TD class="darkRedBg height19"><SPAN class="mLeft5 mTop5">&nbsp;S.No</SPAN></TD>
					<TD	style="background-image: url(./images/left-nav-heading-bg_grey.jpg); background-repeat: repeat-x" height="35"><SPAN class="mLeft5 mTop5">Document Name</SPAN></TD>
					<TD style="background-image: url(./images/left-nav-heading-bg_grey.jpg); background-repeat: repeat-x" height="35"><SPAN class="mLeft5 mTop5">Number of Hits</SPAN></TD>
				</TR>
				<logic:iterate name="kmFileReportForm" id="file" indexId="i" property="hitList">
					<% String cssName = "";	if( i%2==1){cssName = "alt";}%>
					 <tr class="<%=cssName%>">
						<TD class="text" align="left">&nbsp;&nbsp;<%=(i.intValue() + 1)%>.</TD>
						<TD class="text" align="left"><bean:write name="file" property="docName" /></TD>
						<TD class="text" align="left"><bean:write name="file" property="noOfHits" /></TD>
					</TR>
				</logic:iterate>
			</logic:notEmpty>
			<TBODY>
				<TR>
					<TD colspan="2" class="error" align="left"><html:messages id="msg" message="true"><bean:write name="msg" /></html:messages></TD>
				</TR>
			</TBODY>
		</TABLE>
	</logic:equal>

	<logic:equal name="kmFileReportForm" property="reportType" value="filesApprovedList">
		<table width="100%"  cellpadding="0" cellspacing="0" border="0">
			<tr class="text white">
				<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">&nbsp;S.No</span></td>
				<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Approved By</span></td>
				<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Document Count</span></td>
			</tr>
			<logic:empty name="kmFileReportForm" property="filesAddedList">
				<tr>
					<td class="text" align="center" colspan="3">No Record Found</td>
				</tr>
			</logic:empty>

			<logic:notEmpty name="kmFileReportForm" property="filesAddedList">
				<logic:iterate name="kmFileReportForm" id="file" indexId="i" property="filesAddedList">
				  <% String cssName = "";	if( i%2==1){cssName = "alt";}%>
					<tr class="<%=cssName%>">
						<TD class="text" align="left">&nbsp;&nbsp;<%=(i.intValue() + 1)%>.</TD>
						<TD class="text" align="left"><bean:write name="file" property="approverName" /></TD>
						<TD class="text" align="left"><bean:write name="file" property="noOfDocuments" /></TD>
					</TR>
				</logic:iterate>
			</logic:notEmpty>
		</table>
	</logic:equal>

  <logic:equal name="kmFileReportForm" property="reportType" value="filesAddedCircle">
   <logic:notEmpty name="kmFileReportForm" >
    <table width="100%" border="0" cellpadding="0" cellspacing="0">			
	 <tr align="right" >
			<td width="147" class="pLeft10 pTop2"></td>
			<td width="636" class="pTop2" colspan="5"><img  src="images/excel.gif" width="39" height="35" border="0" onclick="importExcel2();" /></td>
          </tr>
	<tr class="text white">
	  <td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">&nbsp;S.No</span></td>
      <td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Uploaded By</span></td>
 	  <td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Login Id</span></td>
	  <td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Element Name</span></td>
	  <td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Document Count</span></td>
 	</tr>
		<logic:notEmpty name="kmFileReportForm" property="filesAddedList">
			<logic:iterate name="kmFileReportForm" id="file" indexId="i" property="filesAddedList">
				<% String cssName = "";	if( i%2==1){cssName = "alt";}%>
					<tr class="<%=cssName%>">
					<TD class="text" align="left" height="25">&nbsp;&nbsp;<%=(i.intValue() + 1)%>.</TD>	
					<TD class="text" align="left"><bean:write name="file" property="uploadedByName" /></TD>
					<TD class="text" align="left"><bean:write name="file" property="uploadedByLoginId" /></TD>
					<TD class="text" align="left"><bean:write name="file" property="circleName" /></TD>
					<TD class="text" align="left"><bean:write name="file" property="noOfDocuments" /></TD>
				</TR>
			</logic:iterate>
		</logic:notEmpty>
	<logic:empty name="kmFileReportForm" property="filesAddedList">
		<tr>
			<td><font color="red">No Record Found</font></td>
		</tr>
	</logic:empty>
   </table>
  </logic:notEmpty>
 </logic:equal>
 
 
 	<logic:equal name="kmFileReportForm" property="reportType" value="filesAdded">
		<table width="100%"  cellpadding="0" cellspacing="0" border="0">
			<logic:notEmpty name="kmFileReportForm" property="filesAddedList">
				<tr align="right" >
					<td width="147" class="pLeft10 pTop2"></td>
					<td width="636" class="pTop2" colspan="5"><img  src="images/excel.gif" width="39" height="35" border="0" onclick="importExcel();" /></td>
                </tr>
				<tr class="text white">
					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">&nbsp;S.No</span></td>
					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Circle Name</span></td>
					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Document Count</span></td>
				</tr>
				<logic:iterate name="kmFileReportForm" id="file" indexId="i" property="filesAddedList">
					<% String cssName = "";	if( i%2==1){cssName = "alt";}%>
					<tr class="<%=cssName%>">	
						<TD class="text" align="left" height="25">&nbsp;&nbsp;<%=(i.intValue() + 1)%>.</TD>
						<TD class="text" align="left"><bean:write name="file" property="elementName" /></TD>
						<TD class="text" align="left"><bean:write name="file" property="documentCount" /></TD>
				</TR>
			  </logic:iterate>
			</logic:notEmpty>
		</table>
	</logic:equal>
	
	
	<logic:equal name="kmFileReportForm" property="reportType" value="filesDeleted">
		<table width="100%"  cellpadding="0" cellspacing="0" border="0">
			<logic:notEmpty name="kmFileReportForm" property="filesDeletedList">
				<tr align="right" >
					<td width="147" class="pLeft10 pTop2"></td>
					<td width="636" class="pTop2" colspan="5"><img  src="images/excel.gif" width="39" height="35" border="0" onclick="importExcel1();" /></td>
                </tr>
				<tr class="text white">
					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">&nbsp;S.No</span></td>
					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Circle Name</span></td>
					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Document Count</span></td>
				</tr>
				<logic:iterate name="kmFileReportForm" id="file" indexId="i" property="filesDeletedList">
					<% String cssName = "";	if( i%2==1){cssName = "alt";}%>
					<tr class="<%=cssName%>">	
						<TD class="text" align="left" height="25">&nbsp;&nbsp;<%=(i.intValue() + 1)%>.</TD>
						<TD class="text" align="left"><bean:write name="file" property="elementName" /></TD>
						<TD class="text" align="left"><bean:write name="file" property="documentCount" /></TD>
					</TR>
				</logic:iterate>
			</logic:notEmpty>
		</table>
	</logic:equal>
	
	
	</div>
  </div>
</html:form>
<logic:equal name="kmFileReportForm" property="showFile" value="false">
</logic:equal>