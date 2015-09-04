<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
	
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
	for(rowcount=1;rowcount < Tablerows.length-1 ;rowcount++)
	{
		if (window.ActiveXObject) // code for IE6, IE5
		{
		//totalTableValues=totalTableValues+Tablerows[rowcount].cells[1].innerText+"="+totalTableValues+Tablerows[rowcount].cells[2].innerText;
		id = Tablerows[rowcount].cells[2].innerText;
			value = document.getElementById(id).value;
		idAndValue = id+"#"+value;
		totalTableValues=totalTableValues+idAndValue+"##";
		}
		else if (window.XMLHttpRequest) // code for IE7+, Firefox, Chrome, Opera, Safari
		{
			id = Tablerows[rowcount].cells[2].innerHTML;
			value = document.getElementById(id).value;
		idAndValue = id+"#"+value;
		totalTableValues=totalTableValues+idAndValue+"##";
		}
		else if (window.XDomainRequest) // code for IE8
		{
			//totalTableValues=totalTableValues+Tablerows[rowcount].cells[1].innerText+"="+totalTableValues+Tablerows[rowcount].cells[2].innerText;
			id = Tablerows[rowcount].cells[2].innerText;
			value = document.getElementById(id).value;
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
</script>
<form  action="elementAction.do" method="post" id="kmManageElementOrder">
<input type="hidden" name="methodName" value="" id="methodName">
<bean:define id="msg" property="message" name="kmElementMstrFormBean"/>
<input type="hidden" name="totalValues" value="" id="totalValues">
     <div class="boxt2">
        <div class="content-upload clearfix">
          <h1 class="clearfix" style="margin-bottom:0px;"><span class="text">Manage Element Sequence</span> </h1>
          <div class="content-table-type2 clearfix">
          
           <table width="100%" border="1">
		<TR class="lightBg">
 	<TD colspan="3" class="error" align="center"><FONT color="red">Element sequence updated successfully!!</FONT></td>
	</tr>
	</table>
       
          
		
	</div>
        </div>
      </div>
</form>