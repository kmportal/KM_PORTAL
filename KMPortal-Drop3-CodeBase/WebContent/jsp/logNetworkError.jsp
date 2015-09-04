<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="javascript">  
function resetFields(){
	document.forms[0].elementName.value="";
	document.forms[0].elementDesc.value="";
	return false;
}
var circle;
function formValidate(){
	if(validate()){
		document.forms[0].methodName.value = "insert";
		return true;
	}else{
		return false;
	}	
}
function hasSpecialChar(objEntry, searchChars,fieldName)
{
	if (isEmpty(objEntry))
	{
		return false;
	}

	theInput = trimValue(objEntry);
	theLength = theInput.length;

	// theNoisyString Is a special Characters String
	var theNoisyString;

	theNoisyString = searchChars;


	for (var i = 0; i < theLength ; i++)
	{
		// Check that current character isn't noisy.
		theChar = theInput.charAt(i);
	
		if ((theNoisyString.indexOf(theChar) != -1) )
		{
		  alert("Special Charactar "+theChar+" is not allowed for "+fieldName);
		  return true;
		}
	}//for loop ends

	return false;
 } // function hasSpecialChars(theEntry) ends
 function isNumeric(objInput)
{
	theInput = trimValue(objInput); 
	theLength = theInput.length ;
  	for (var i = 0 ; i < theLength ; i++)
  	{
    	if (theInput.charAt(i) < '0' || theInput.charAt(i) > '9' )
    	{ 
      		 //the text field has a non numeric entry
        	return false;
    	}
	}// for ends

  	return true;
}// function isInteger ends
function validate(){
	var searchCharsProbDesc="`~$^*=+><{}[]|=?'\"#_@!\\";
	var searchCharsAreaAfct="`~$^*=+><{}[]|=?'\"#_@";
	//var reg=/^[0-9a-zA-Z_ ]*$/;
	
    if( isEmpty(document.getElementById("problemDesc"))){
		alert("Please Enter Problem Description");
		document.getElementById("problemDesc").focus();
		return false;
	}	
    if( hasSpecialChar(document.getElementById("problemDesc"), searchCharsProbDesc,"Problem Description")){
		document.getElementById("problemDesc").focus();
		return false;
	}	
	if( document.getElementById("problemDesc").value.length >400){
		alert("Problem Description maximum character limit is 400.");
		document.getElementById("problemDesc").select();
		document.getElementById("problemDesc").focus();
		return false;
	}
    if( isEmpty(document.getElementById("areaAffected"))){
		alert("Please Enter Affected Area");
		document.getElementById("areaAffected").focus();
		return false;
	}	
	if( hasSpecialChar(document.getElementById("areaAffected"), searchCharsAreaAfct,"Affected Area")){
		document.getElementById("areaAffected").focus();
		return false;
	}	
	if( document.getElementById("areaAffected").value.length >400){
		alert("Area Affected maximum character limit is 400.");
		document.getElementById("areaAffected").select();
		document.getElementById("areaAffected").focus();
		return false;
	}	
	if(!isNumeric(document.getElementById("resoTATHH"))){
		alert("Please Enter Numeric resolution TAT Hours");
		document.getElementById("resoTATHH").focus();
		return false;
	}	
	
	if(!isNumeric(document.getElementById("resoTATMM"))){
		alert("Please Enter Numeric resolution TAT Minutes");
		document.getElementById("resoTATMM").focus();
		return false;
	}		
	if(!document.getElementById("resoTATMM").value=="" && ( document.getElementById("resoTATMM").value < 0 || document.getElementById("resoTATMM").value >=60)){
		alert("Resolution Minutes should be between 0-60");
		document.getElementById("resoTATMM").focus();
		return false;
	}		
	return true;
}
</script>
<html:form action="/networkErrorAction" >
	<html:hidden property="methodName" value="insert"/>
	
	<div class="box2">
     <div class="content-upload">
     <H1>Log Network Fault</H1>
	<TABLE align="center"> 
			<TBODY>
			<tr>
			<td colspan="2" align="center" class="error">
			<strong> 
          		<html:messages id="msg" message="true">
           		 	<bean:write name="msg"/>  
             	</html:messages>
            </strong>
            </td>
		   </tr>
		   <tr >
			<td colspan="2" class="error" align="center" ><font color='red'><strong><html:errors property="errors.networkFaultLoggingFailed"  /></strong></font></td>
		    </tr>
		   		</TBODY>
			</TABLE>
			<ul class="list2 form1">
			<li class="clearfix">
			<span class="text2 fll width160"><strong>Problem Description<font color=red>*</font></strong></span>
				<html:textarea styleClass="textarea1 fll" styleId="problemDesc" rows="3" cols="40" name="kmNetworkErrorFormBean" property="problemDesc"  /><font color='red'> <html:errors property="errors.problem_desc_invalid"  /></font>
			</li>
			<li class="clearfix alt">
			<span class="text2 fll width160"><strong>Area Affected<font color=red>*</font></strong></span>
				<html:textarea  styleClass="textarea1 fll" styleId="areaAffected" rows="3" cols="40" name="kmNetworkErrorFormBean" property="areaAffected" /><font color='red'> <html:errors property="errors.area_affected_invalid"  /></font>
			
			</li>
			<li class="clearfix">
			<span class="text2 fll width160"><strong>Resolution TAT: Hours </strong></span>
				     <p class="clearfix fll"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77" styleId="resoTATHH" property="resoTATHH" name="kmNetworkErrorFormBean" maxlength="4" size="4" /><font color='red'> </font></span></span>&nbsp;<h6>(HH)</h6> <html:errors property="errors.resolutionTATHH"  /> </p>
			</li>
				<li class="clearfix alt">
			<span class="text2 fll width160"><strong style="padding-left: 50px">TAT : Minutes &nbsp; &nbsp; </strong></span>
				<p class="clearfix fll"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77" styleId="resoTATMM" property="resoTATMM" name="kmNetworkErrorFormBean" maxlength="2" size="4" /> <font color='red'></font></span></span>&nbsp;<h6>(MM)</h6> <html:errors property="errors.resolutionTATMM"  /></p>
			</li>
				<li class="clearfix" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span><input type="image" src="<%=request.getContextPath()%>/images/submit.jpg"  value="submit" onclick="return formValidate();"/>
			</li></ul>

	
	</div>
	</div>
	
</html:form>

