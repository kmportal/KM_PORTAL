<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
	var textVal;
function populateElements()
{
var circleID = document.getElementById("circle");
 document.getElementById("methodName").value = "findElementsInCircle"; 
	document.getElementById("kmManageElementOrder").submit();
return true;
}
function submitForm()
{
//*******************
// adding by pratap start
  	 var dataTable=document.getElementById("dataTable");
	var Tablerows=document.getElementById("dataTable").rows;
	var totalTableValues="";
		var id ;
		var value;
		var idAndValue;
		var totalVal;
		var regNumOnly = /^[0-9]*$/;
	for(rowcount=1;rowcount < Tablerows.length-1 ;rowcount++)
	{
		if (window.ActiveXObject) // code for IE6, IE5
		{
		//totalTableValues=totalTableValues+Tablerows[rowcount].cells[1].innerText+"="+totalTableValues+Tablerows[rowcount].cells[2].innerText;
		id = Tablerows[rowcount].cells[2].innerText;
		value = document.getElementById(id).value;

		if(value=="")
		{
			alert("Please fill all values ");
			document.getElementById(id).focus();
			return false;
		}
		
		if(!regNumOnly.test(trim(document.getElementById(id).value))){
		alert("Please provide correct sequence number (only numeric values allowed) ");
		return false;
		}
	
		idAndValue = id+"#"+value;
		totalTableValues=totalTableValues+idAndValue+"##";
		}
		else if (window.XMLHttpRequest) // code for IE7+, Firefox, Chrome, Opera, Safari
		{
			id = Tablerows[rowcount].cells[2].innerHTML;
			value = document.getElementById(id).value;
			if(value=="")
				{
				alert("Please fill all values ");
				document.getElementById(id).focus();
				return false;
				}
			idAndValue = id+"#"+value;
			totalTableValues=totalTableValues+idAndValue+"##";
		}
		else if (window.XDomainRequest) // code for IE8
		{
			//totalTableValues=totalTableValues+Tablerows[rowcount].cells[1].innerText+"="+totalTableValues+Tablerows[rowcount].cells[2].innerText;
			id = Tablerows[rowcount].cells[2].innerText;
			value = document.getElementById(id).value;
			if(value=="")
			{
				alert("Please fill all values ");
				document.getElementById(id).focus();
				return false;
			}		
		idAndValue = id+"#"+value;
		totalTableValues=totalTableValues+idAndValue+"##";
		}
		
	}
//	alert("total values :"+totalTableValues);
  	if(totalTableValues == "")
  	{
  	return false;
  	}
	document.getElementById("totalValues").value = totalTableValues;
//	alert("total values on form :"+document.getElementById("totalValues").value);
    document.getElementById("methodName").value ="updateElementSequence";
   	document.getElementById("kmManageElementOrder").submit();
   	return true;
}

function isNumber(evt,val) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
    	alert("Please provide only numeric values");
        return false;
    }
    return true;
}
function isZero(val,id)
{
if(val != ""){
if(val == '0'){
alert("Please don't put zero(0) or less than zero sequence no.");
document.getElementById(id).focus();
document.getElementById(id).value="";
return false;}
}
else
{
return true;
}
}
function saveValue(val)
{
textVal = val;
}
</script>
<form  action="elementAction.do" method="post" id="kmManageElementOrder">
<input type="hidden" name="methodName" value="" id="methodName">
<bean:define id="msg" property="message" name="kmElementMstrFormBean"/>
<input type="hidden" name="totalValues" value="" id="totalValues">
     <div class="boxt2">
        <div class="content-upload clearfix">
          <h1 class="clearfix" style="margin-bottom:0px;"><span class="text">Manage Element Sequence</span> </h1>
          <div class="content-table-type2 clearfix">
          <center>
          <logic:notEqual name="kmElementMstrFormBean" property="message" value="">
	<tr  style="font-size:11px;font-weight:bold;align:center" class="alt">
	<TD align="center" colspan="3" valign="top"  style="word-wrap: break-word;"><FONT color="red">Element sequence updated successfully!!</FONT></td>
	</tr>
	</logic:notEqual>
          </center>
          <logic:notEmpty name="kmElementMstrFormBean" property="circleList">
          <% System.out.println("******* circle list not blank ****************"); %>
          <table width="100%" cellspacing="0" cellpadding="2" style="table-layout: fixed;" >
		 <tr style="font-size:12px;font-weight:bold;">
                <td width="15%" align="right" valign="top">Select Circle :</td>
                <td>
            
			<html:select property="circle" name="kmElementMstrFormBean" onchange="javascript:return populateElements();">
			<html:option value="-1" >--select circle--</html:option>
			<logic:iterate name="kmElementMstrFormBean" type="com.ibm.km.dto.KmElementMstr" property="circleList" id="listDto" indexId="i">
			<html:option value="<%=listDto.getElementId()%>"><%=listDto.getElementName()%> (<%= listDto.getLobName()%>)</html:option>
			</logic:iterate>
			</html:select>
			
                </td>
                </tr>
		</TABLE>
		</logic:notEmpty>
		<logic:notEmpty name="kmElementMstrFormBean" property="elementsUnderCircle">
		<font size="2" color="red">Note : Please don't give zero(0) or less than zero sequence no.</font><br><br>
	<table width="100%" cellspacing="0" cellpadding="2" style="table-layout: fixed;" id="dataTable">
	<tr class="alt"  style="font-size:11px;font-weight:bold;align:center">
	<TD align="left" valign="top"  style="word-wrap: break-word">Element Name</td>
	<TD align="left" valign="top"  style="word-wrap: break-word">Current Sequence</td>
	<td></td>
	</tr>
	<logic:iterate name="kmElementMstrFormBean" type="com.ibm.km.dto.KmElementMstr" property="elementsUnderCircle" id="listDto" indexId="i">
	<bean:define id="zero" name="listDto" property="sequenceNo" type="java.lang.Integer"/>
	<bean:define id="elementN" name="listDto" property="elementName" type="java.lang.String"/>
	
	<%	
	System.out.println("Sequence :"+elementN +" :"+zero);
	String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>"  style="font-size:10px;">
	<TD align="left" valign="top"  style="word-wrap: break-word"><bean:write name="listDto" property="elementName"/></td>
	<TD align="left" valign="top"  style="word-wrap: break-word">
	<%
	if(zero == 0)
	{
	%>
	<input type="text" id="<bean:write name="listDto" property="elementId"/>" value="<bean:write name="listDto" property="sequenceNo"/>" onblur="javascript : return isZero(this.value,this.id);" onkeypress="return isNumber(event);"></td>
	<%
	}
	else
	{
	 %>
		<input type="text" id="<bean:write name="listDto" property="elementId"/>" value="<bean:write name="listDto" property="sequenceNo"/>"  onblur="javascript : return isZero(this.value,this.id);" onkeypress="return isNumber(event);"></td>
		<%
		}
		 %>
	<td style="display:none;"><bean:write name="listDto" property="elementId"/></td>
	<td align="left" valign="top"  style="word-wrap: break-word"></td>
	</tr>
	</logic:iterate>
	<tr>
	<TD align="center" valign="top"  style="word-wrap: break-word" colspan="2"><input class="red-btn" value="Submit" alt="Submit" onclick="javascript:return submitForm();cellContent();"/></TD>
	</tr>
	</table>
	</logic:notEmpty>
	</div>
        </div>
      </div>
</form>