<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:notEqual name="kmDocumentMstrForm" property="initStatus" value="true">
 <logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
 </logic:notEmpty>
 </logic:notEqual>

<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
	
	function editDetails(srNo)
	{
	  document.getElementById("kmDocumentMstrFormMainSearchResult").srNo.value = srNo; 
	  document.getElementById("kmDocumentMstrFormMainSearchResult").submit();
	}
	
	function deleteRow()
	{
	var ans = confirm("Are you sure to delete this record?");
	return ans;
	}
	
	function sendSMS(srNo){
		//alert("Sending SMS ##"+document.kmDocumentMstrForm);
		document.getElementById("kmDocumentMstrFormMainSearchResult").srNo.value = srNo; 
	  	document.getElementById("kmDocumentMstrFormMainSearchResult").submit();
	  	
		}
		function validateMobileNumber(e){
			var mobNo = document.forms[0].mobileNo.value;
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
	
			e.href = e.href + "&mobileNo="+document.forms[0].mobileNo.value;
			//var url = document.getElementById("sms").href;
			//url=url+"&mobileNo="+document.forms[0].mobileNo.value;
			//document.getElementById("sms").href = url;
	  		return true;
		}
			
	
</script>
<form name="kmDocumentMstrFormMainSearchResult" id="kmDocumentMstrFormMainSearchResult" method="post" >
	<bean:define id="keyword" name="kmDocumentMstrForm" property="keyword" />
	<bean:define id="mainOption" name="kmDocumentMstrForm" property="mainOptionValue" />
	<bean:define id="subOption" name="kmDocumentMstrForm" property="selectedSubOptionValue" />
	<input type="hidden" name="srNo" value="">

<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
<div id="elementLabel"> 	<span class="text2 fll width160"><strong>Mobile Number</strong> </span>
	<% String mobileNo = (String)request.getAttribute("mobileNo");
	if(mobileNo.equals("") || mobileNo == null){
%>
	<html:text name="kmDocumentMstrForm" property="mobileNo" />
	<% }else{ %>
	<html:text name="kmDocumentMstrForm" property="mobileNo" value="<%=mobileNo%>" readonly="true"/>
<%} %>
</div>
</logic:notEqual>
     <div class="boxt2" id="div1">
        <div class="content-upload clearfix" id="div2">
        	
        	<h1 class="clearfix" style="margin-bottom:0px;"><span class="text">Search Result</span> </h1>
          <div class="content-table-type2 clearfix">
          <logic:equal name="kmDocumentMstrForm" property="mainOptionValue" value="arc" ><!-- Start of ARC -->
		<table width="100%" border="1" cellspacing="0" cellpadding="2" style="table-layout: fixed;" >
		 <tr class="alt"  style="font-size:9px;font-weight:bold;align:center">
                <td width="8%" align="left" valign="top">Sr.No</td>
                <td width="8%" align="left" valign="top">CIRCLE</td>
                <td width="8%" align="left" valign="top">ZONE</td>
				<td width="8%" align="left" valign="top">ARC/OR</td>
				<td width="8%" align="left" valign="top">CITY</td>
				<td width="8%" align="left" valign="top">PIN</td>
				<td width="8%" align="left" valign="top">ARC</td>
				<td width="8%" align="left" valign="top">ADD</td>
				<td width="8%" align="left" valign="top">TIMING</td>
				<td width="8%" align="left" valign="top">Avail Of CC/DC Machine</td>
				<td width="8%" align="left" valign="top">Avail Of Doctor SIM </td>
				<logic:equal name="kmDocumentMstrForm" property="actorId" value="1">
				<td width="5%" align="left" valign="top">Action</td>
				</logic:equal>
				<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
				<td width="5%" align="left" valign="top">Send SMS</td>
				</logic:notEqual>
              </tr>
		<logic:empty name="kmDocumentMstrForm" property="searchDetailsList">
			<TR class="lightBg">
				<TD colspan="12" class="error" align="center"><FONT color="red">No Record(s) Found</FONT></TD>
			</TR>
		</logic:empty>
		<logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
			<logic:iterate name="kmDocumentMstrForm" type="com.ibm.km.dto.KmSearchDetailsDTO" property="searchDetailsList" id="listDto" indexId="i">
			<%	String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>"  style="font-size:10px;">	
					<TD align="left" valign="top" style="font-weight:bold;"><%=(i.intValue() + 1)%>.</TD>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="circle"/></td>
					<td align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="zone" /></td>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="arcOr" /></TD>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="city" /></TD>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="pincode" /></TD>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="arc" /></TD>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="address" /></TD>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="showRoomTiming" /></TD>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="availabilityOfCcDcMachine" /></TD>
					<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="availabilityOfDoctorSim" /></TD>
					<logic:equal name="kmDocumentMstrForm" property="actorId" value="1">
					<td align="left" valign="top"  style="word-wrap: break-word">
					<a href="/KM/documentAction.do?methodName=editDetails&keyword=<%=keyword%>&mainOptionValue=<%=mainOption%>&subOptionValue=<%=subOption%>&srno=<bean:write name="listDto" property="srNo"/>">Edit</a><br>
					<a href="/KM/documentAction.do?methodName=deleteDetails&mainOptionValue=<%=mainOption%>&srno=<bean:write name="listDto" property="srNo" />" onclick='javascript :return deleteRow();'>Delete</a><br>
					</logic:equal>
					<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
						<td align="left" valign="top"  style="word-wrap: break-word">
						<a id="sms" href="/KM/documentAction.do?methodName=sendSMS&keyword=<%=keyword%>&mainOptionValue=<%=mainOption%>&subOptionValue=<%=subOption%>&srno=<bean:write name="listDto" property="srNo" />"onclick='javascript :return validateMobileNumber(this);'>Send SMS</a></td>
						
					</logic:notEqual>
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
		</logic:equal><!-- End of ARC -->
		<logic:equal name="kmDocumentMstrForm" property="mainOptionValue" value="dist"><!-- Start of Dist -->
		<table width="100%" border="1" cellspacing="0" cellpadding="2" style="table-layout: fixed;" align="left">
		 <tr class="alt"  style="font-size:9px;font-weight:bold;">
                <td width="4.76%"  valign="top">Sr.No</td>
                <td width="4.76%"  valign="top">PIN</td>
                <td width="4.76%"  valign="top">STATE</td>
				<td width="4.76%"  valign="top">CITY</td>
				<td width="4.76%"  valign="top">PIN CAT</td>
				<td width="5%"  valign="top">AREA</td>
				<td width="4.76%"  valign="top">HUB</td>
				<td width="4.76%"  valign="top">CIRCLE</td>
				<td width="4.76%"  valign="top">SF & SSD NAME</td>
				<td width="4.76%"  valign="top">SF AND & MAIL</td>
				<td width="4.76%"  valign="top">SF & SSD CONT</td>
				<td width="4.76%"  valign="top">TSM NAME</td>
				<td width="4.76%"  valign="top">TSM MAIL</td>
				<td width="4.76%"  valign="top">TSM CONT</td>
				<td width="4.76%"  valign="top">SR MNGR</td>
				<td width="4.76%"  valign="top">SR MNGR MAIL</td>
				<td width="4.76%"  valign="top">SR MNGR CONT</td>
				<td width="4.76%"  valign="top">ASM NAME</td>
				<td width="4.76%"  valign="top">ASM MAIL</td>
				<td width="4.76%"  valign="top">ASM CONTs</td>
				<logic:equal name="kmDocumentMstrForm" property="actorId" value="1">
				<td width="4.76%"  valign="top">Action</td>
				</logic:equal>
				<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
				<td width="5%" align="left" valign="top">Send SMS</td>
				</logic:notEqual>
</tr>
		<logic:empty name="kmDocumentMstrForm" property="searchDetailsList">
			<TR class="lightBg">
				<TD colspan="21" class="error" align="center"><FONT color="red">No Record(s) Found</FONT></TD>
			</TR>
		</logic:empty>
		<logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
			<logic:iterate name="kmDocumentMstrForm" type="com.ibm.km.dto.KmSearchDetailsDTO" property="searchDetailsList" id="listDto" indexId="i">
			<%	String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>"  style="font-size:10px;">	
					<TD  valign="top" style="font-weight:bold;"><%=(i.intValue() + 1)%>.</TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="pincode"/></td>
					<td  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="stateName" /></td>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="city" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="pinCatagory" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="area" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="hub" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="circle" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="sfAndSSDName" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="sfAndSSDEmail" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="sfAndSSDContNo" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="tsmName"/></td>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="tsmMailId"/></td>
					<td  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="tsMContNo" /></td>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="srMngr" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="srMngrMailId" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="srMngrContNo" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="asmName" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="asmMailId" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="asmContNo" /></TD>
					<logic:equal name="kmDocumentMstrForm" property="actorId" value="1">
					<td  valign="top"  ><a href="/KM/documentAction.do?methodName=editDetails&keyword=<%=keyword%>&mainOptionValue=<%=mainOption%>&subOptionValue=<%=subOption%>&srno=<bean:write name="listDto" property="srNo"/>">Edit</a><br>
					<a href="/KM/documentAction.do?methodName=deleteDetails&mainOptionValue=<%=mainOption%>&srno=<bean:write name="listDto" property="srNo"/>" onclick='javascript :return deleteRow();'>Delete</a><br>
					
					</logic:equal>
					<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
						<td align="left" valign="top"  style="word-wrap: break-word">
						<a id="sms" href="/KM/documentAction.do?methodName=sendSMS&keyword=<%=keyword%>&mainOptionValue=<%=mainOption%>&subOptionValue=<%=subOption%>&srno=<bean:write name="listDto" property="srNo" />"onclick='javascript :return validateMobileNumber(this);'>Send SMS</a></td>
						
					</logic:notEqual>
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
		</logic:equal><!-- End of Dist -->
			<logic:equal name="kmDocumentMstrForm" property="mainOptionValue" value="coords"><!-- Start of COORDS -->
		<table width="100%" border="1" cellspacing="0" cellpadding="2" style="table-layout: fixed;" >
		 <tr class="alt"  style="font-size:9px;font-weight:bold;align:center">
                <td width="7%"  valign="top">Sr.No</td>
                <td width="7%"  valign="top">CIRCLE</td>
                <td width="7%"  valign="top">COMPANY NAME</td>
				<td width="7%"  valign="top">COMPANY SPOC NAME</td>
				<td width="7%"  valign="top">COMP SPOC MAIL</td>
				<td width="7%"  valign="top">COMP SPOC CONT.</td>
				<td width="7%"  valign="top">RM</td>
				<td width="7%"  valign="top">RM MAIL ID</td>
				<td width="7%"  valign="top">RM PH#</td>
				<td width="7%"  valign="top">REQUESTOR</td>
				<td width="7%"  valign="top">REQ LOCATION</td>
				<td width="7%"  valign="top">REQ CONT</td>
				<td width="7%"  valign="top">REQ ON</td>
				<logic:equal name="kmDocumentMstrForm" property="actorId" value="1">
				<td width="7%"  valign="top">Action</td>
				</logic:equal>
				<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
				<td width="5%" align="left" valign="top">Send SMS</td>
				</logic:notEqual>
		</tr>
		<logic:empty name="kmDocumentMstrForm" property="searchDetailsList">
			 <TR class="lightBg">
				<TD colspan="21" class="error" align="center"><FONT color="red">No Record(s) Found</FONT></TD>
			</TR>
		</logic:empty>
		<logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
			<logic:iterate name="kmDocumentMstrForm" type="com.ibm.km.dto.KmSearchDetailsDTO" property="searchDetailsList" id="listDto" indexId="i">
			<%	String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>"  style="font-size:10px;">	
					<TD  valign="top" style="font-weight:bold;"><%=(i.intValue() + 1)%>.</TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="circle"/></td>
					<td  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="compName" /></td>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="compSpocName" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="compSpocMail" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="compSpocCont" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="rmMgr" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="rmMailId" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="rmCont" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="requestor" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="requestorLocation" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="requestorCont"/></td>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="requestedOn"/></td>
					<logic:equal name="kmDocumentMstrForm" property="actorId" value="1">
					<td  valign="top"  ><a href="/KM/documentAction.do?methodName=editDetails&keyword=<%=keyword%>&mainOptionValue=<%=mainOption%>&subOptionValue=<%=subOption%>&srno=<bean:write name="listDto" property="srNo"/>">Edit</a><br>
					<a href="/KM/documentAction.do?methodName=deleteDetails&mainOptionValue=<%=mainOption%>&srno=<bean:write name="listDto" property="srNo"/>" onclick='javascript :return deleteRow();'>Delete</a><br>
					
					</logic:equal>
					<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
						<td align="left" valign="top"  style="word-wrap: break-word">
						<a id="sms" href="/KM/documentAction.do?methodName=sendSMS&keyword=<%=keyword%>&mainOptionValue=<%=mainOption%>&subOptionValue=<%=subOption%>&srno=<bean:write name="listDto" property="srNo" />"onclick='javascript :return validateMobileNumber(this);'>Send SMS</a></td>
						
					</logic:notEqual>
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
		</logic:equal><!-- End of COORDS -->
		<logic:equal name="kmDocumentMstrForm" property="mainOptionValue" value="pack"><!-- Start of PACK -->
		<table width="100%" border="1" cellspacing="0" cellpadding="2" style="table-layout: fixed;" >
		 <tr class="alt"  style="font-size:9px;font-weight:bold;align:center">
                <td width="14%"  valign="top">Sr No.</td>
                <td width="14%"  valign="top">TOPIC</td>
                <td width="14%"  valign="top">HEADER</td>
				<td width="14%"  valign="top">CONTENTS</td>
				<td width="14%"  valign="top">CIRCLE NAME </td>
				<td width="14%"  valign="top">VALID FROM</td>
				<td width="14%"  valign="top">VALID TO</td>
		</tr>
		<logic:empty name="kmDocumentMstrForm" property="searchDetailsList">
			 <TR class="lightBg">
				<TD colspan="21" class="error" align="center"><FONT color="red">No Record(s) Found</FONT></TD>
			</TR>
		</logic:empty>
		<logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
			<logic:iterate name="kmDocumentMstrForm" type="com.ibm.km.dto.KmSearchDetailsDTO" property="searchDetailsList" id="listDto" indexId="i">
			<%	String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>"  style="font-size:10px;">	
					<TD  valign="top" style="font-weight:bold;"><%=(i.intValue() + 1)%>.</TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="topic"/></td>
					<td  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="header" /></td>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="contents" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="circle" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="fromDt" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="toDt" /></TD>
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
		</logic:equal><!-- End of PACK -->
		
		<logic:equal name="kmDocumentMstrForm" property="mainOptionValue" value="vas"><!-- Start of VAS -->
		<table width="100%" border="1" cellspacing="0" cellpadding="2" style="table-layout: fixed;" align="left">
		 <tr class="alt"  style="font-size:9px;font-weight:bold;">
                <td width="7%"  valign="top">Sr.No</td>
                <td width="7%"  valign="top">VAS NAME</td>
                <td width="7%"  valign="top">Activation Code</td>
				<td width="7%"  valign="top">Deactivation Code</td>
				<td width="7%"  valign="top">Charges</td>
				<td width="7%"  valign="top">Benefits</td>
				<td width="7%"  valign="top">Construct</td>
				<logic:equal name="kmDocumentMstrForm" property="actorId" value="1">
				<td width="7%"  valign="top">Action</td>
				</logic:equal>
				<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
				<td width="5%" align="left" valign="top">Send SMS</td>
				</logic:notEqual>
		</tr>
		<logic:empty name="kmDocumentMstrForm" property="searchDetailsList">
			<TR class="lightBg">
				<TD colspan="21" class="error" align="center"><FONT color="red">No Record(s) Found</FONT></TD>
			</TR>
		</logic:empty>
		<logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
			<logic:iterate name="kmDocumentMstrForm" type="com.ibm.km.dto.KmSearchDetailsDTO" property="searchDetailsList" id="listDto" indexId="i">
			<%	String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>"  style="font-size:10px;">	
					<TD  valign="top" style="font-weight:bold;"><%=(i.intValue() + 1)%>.</TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="vasName"/></td>
					<td  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="activationCode" /></td>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="deactivationCode" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="charges" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="benefits" /></TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="construct" /></TD>
					
					<logic:equal name="kmDocumentMstrForm" property="actorId" value="1">
					<td  valign="top"  ><a href="/KM/documentAction.do?methodName=editDetails&keyword=<%=keyword%>&mainOptionValue=<%=mainOption%>&subOptionValue=<%=subOption%>&srno=<bean:write name="listDto" property="srNo"/>">Edit</a><br><a href="/KM/documentAction.do?methodName=deleteDetails&mainOptionValue=<%=mainOption%>&srno=<bean:write name="listDto" property="srNo"/>" onclick='javascript :return deleteRow();'>Delete</a><br>
					
					</logic:equal>
					<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
						<td align="left" valign="top"  style="word-wrap: break-word">
						<a id="sms" href="/KM/documentAction.do?methodName=sendSMS&keyword=<%=keyword%>&mainOptionValue=<%=mainOption%>&subOptionValue=<%=subOption%>&srno=<bean:write name="listDto" property="srNo" />"onclick='javascript :return validateMobileNumber(this);'>Send SMS</a></td>
						
					</logic:notEqual>
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
		</logic:equal><!-- End of Vas -->
		
		<!-- Added by Saanya for Schemes and Benefits -->
		<logic:equal name="kmDocumentMstrForm" property="mainOptionValue" value="snb">
		<table width="100%" border="1" cellspacing="0" cellpadding="2" style="table-layout: fixed;" >
		 <tr class="alt"  style="font-size:9px;font-weight:bold;align:center">
                <td width="10%"  valign="top">Sr No.</td>
                <td width="30%"  valign="top">SCHEME TYPE</td>
                <td width="60%"  valign="top">DESCRIPTION</td>
				<!-- <td width="15%"  valign="top">IS CONFIGURABLE</td> -->
				<%-- <logic:equal name="kmDocumentMstrForm" property="actorId" value="1">
				<td width="14%"  valign="top">Action</td>
				</logic:equal> --%>
				<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
				<td width="14%" align="left" valign="top">Send SMS</td>
				</logic:notEqual>
		</tr>
		<logic:empty name="kmDocumentMstrForm" property="searchDetailsList">
			 <TR class="lightBg">
				<TD colspan="5" class="error" align="center"><FONT color="red">No Record(s) Found</FONT></TD>
			</TR>
		</logic:empty>
		<logic:notEmpty name="kmDocumentMstrForm" property="searchDetailsList">
			<logic:iterate name="kmDocumentMstrForm" type="com.ibm.km.dto.KmSearchDetailsDTO" property="searchDetailsList" id="listDto" indexId="i">
			<%	String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>"  style="font-size:10px;">	
					<TD  valign="top" style="font-weight:bold;"><%=(i.intValue() + 1)%>.</TD>
					<TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="schemeType"/></td>
					<td  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="schemeDesc" /></td>
					<%-- <TD  valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="schemeDisplayFlag" /></TD>
					<logic:equal name="kmDocumentMstrForm" property="actorId" value="1">
					<td  valign="top"  ><a href="/KM/documentAction.do?methodName=editDetails&keyword=<%=keyword%>&mainOptionValue=<%=mainOption%>&subOptionValue=<%=subOption%>&srno=<bean:write name="listDto" property="srNo"/>">Edit</a><br><a href="/KM/documentAction.do?methodName=deleteDetails&mainOptionValue=<%=mainOption%>&srno=<bean:write name="listDto" property="srNo"/>" onclick='javascript :return deleteRow();'>Delete</a><br>
					</logic:equal> --%>
					<logic:notEqual name="kmDocumentMstrForm" property="actorId" value="1">
						<td align="left" valign="top"  style="word-wrap: break-word">
						<a id="sms" href="/KM/documentAction.do?methodName=sendSMS&keyword=<%=keyword%>&mainOptionValue=<%=mainOption%>&subOptionValue=<%=subOption%>&srno=<bean:write name="listDto" property="srNo" />"onclick='javascript :return validateMobileNumber(this);'>Send SMS</a></td>
						 
					</logic:notEqual>
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
		</logic:equal> 
		<!-- End of Schemes and Benefits -->
	</div>
        </div>
      </div>
      
      </form>
