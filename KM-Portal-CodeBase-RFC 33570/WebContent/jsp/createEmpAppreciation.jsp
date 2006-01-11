<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<script language="javascript"> 
var arr = new Array();
var arrIndex = 0;
function Init(s){
	s.document.designMode = 'On';
	arr[arrIndex]=s;
	arrIndex++;
}
function validateImage(obj){ 
	var imageName = obj.value; 
	var imageType = imageName.substring(imageName.lastIndexOf("."));
	if(imageType == ".jpg" || imageType == ".JPG" ||  imageType == ".gif" ||  imageType == ".GIF" ){
		return true;
	}
	else{
		alert("Please select only jpg or gif Employee Image");
		obj.select();
		return false;	   
	}
}
function InitEditModeFrames(s){
	var iFrm = s.contentWindow;
	Init(iFrm);
}
function addMoreAppreciationFields(){
	var ul = document.getElementById("appreciationList");
	var li = document.createElement("li");
	var liLength = ul.getElementsByTagName("li").length;
	var liClass = "clearfix  padd10-0"; 
	if( liLength == 25){
		alert("Maximum 25 rows can be added");
       	return false;
	}   
	if( liLength %2 ==1){
       liClass = "clearfix padd10-0 alt";
    }
    li.setAttribute("class", liClass);
    var contentText = ""; 
   
   contentText += "<p class='clearfix fll margin-r10'> <span class='textbox6'> <span class='textbox6-inner'>";
   contentText += "<input type='text' name='employeeAppreciationList["+arrIndex+"].employeeName' value='' class='textbox1'/> ";
   contentText += "</span> </span> </p>";
   contentText += "<p class='clearfix fll margin-r10'> <span class='textbox6'> <span class='textbox6-inner'>";
   contentText += "<input type='text' name='employeeAppreciationList["+arrIndex+"].appreciationHeader' value='' class='textbox1'/> ";
   contentText += "</span> </span> </p>";
   contentText += "<p class='clearfix fll margin-r10'> <input type='file' name='employeeAppreciationList["+arrIndex+"].employeeImage' value='' onchange='validateImage(this)' id='employeeImage"+arrIndex+"' /></p>";
   
   contentText += "<p class='clearfix fll margin-r10'><input type='textarea' name='employeeAppreciationList["+arrIndex+"].appreciationContent' class='textarea1' value='' id='content" + arrIndex + "'/>";
   contentText += "<input type='hidden' name='employeeAppreciationList["+arrIndex+"].appreciationContent' value=''  id='content" + arrIndex + "'/></p>";
   li.innerHTML = contentText;
   ul.appendChild(li); 
    
   var iViewObj = "iView"+arrIndex;
   var winObj = window.document;
   var obj = winObj.getElementById(iViewObj);
   InitEditModeFrames(obj);
 }
function submitForm(){ 
	var  tobeSubmitted = false;
	for (ii=0; ii<=arrIndex; ii++){  
    	if(undefined != document.getElementsByName("employeeAppreciationList["+ii+"].employeeName")[0]){    
    		if(document.getElementsByName("employeeAppreciationList["+ii+"].employeeName")[0].value != ""){  
	            var reg=/^[a-zA-Z ]*$/;
				if(!reg.test(document.getElementsByName("employeeAppreciationList["+ii+"].employeeName")[0].value )){
					alert("Special characters are not allowed in Employee Name");
					document.getElementsByName("employeeAppreciationList["+ii+"].employeeName")[0].select();				
					return false;	
				}
			}
	      	else{
				alert("Please enter value for Name");
				document.getElementsByName("employeeAppreciationList[0].employeeName")[0].focus();
				return false;	    
			}
	   		if(document.getElementsByName("employeeAppreciationList["+ii+"].appreciationHeader")[0].value == ""){
				alert("Please enter value for Appreciation Header");
				document.getElementsByName("employeeAppreciationList["+ii+"].appreciationHeader")[0].focus();
				return false;	    
		    }	   
	        if(document.getElementsByName("employeeAppreciationList["+ii+"].appreciationContent")[0].value == ""){
				alert("Please enter Appreciation Content for employee : "+document.getElementsByName("employeeAppreciationList["+ii+"].employeeName")[0].value);
				return false;	    
		    }
			if(document.getElementsByName("employeeAppreciationList["+ii+"].employeeImage")[0].value != ""){
				if(!validateImage(document.getElementsByName("employeeAppreciationList["+ii+"].employeeImage")[0])){
			  		return false;
				}
        	}
        	tobeSubmitted = true; 
		}
	} 
       
	if(tobeSubmitted){
		document.forms[0].submit();
   	}
}
function clearForm(){
	document.forms[0].reset();
	for (ii=0; ii<document.forms[0].elements.length; ii++) {  
  		if(undefined != document.getElementsByName("employeeAppreciationList["+ii+"].appreciationContent")[0]){ 
    		document.getElementsByName("employeeAppreciationList["+ii+"].appreciationContent")[0].value = "";
    		arr[ii].document.body.innerHTML = "";
		}
  	}
}
 
</script>

<html:form action="/employeeAppreciation" enctype="multipart/form-data">
	<html:hidden property="methodName" value="insert" />
	<html:hidden property="createdBy" styleId="createdBy" />
	<html:hidden property="levelCount" styleId="levelCount" />
	<html:hidden property="elementLevel" styleId="elementLevel" />
	<html:hidden property="token" styleId="token" />

	<div class="box2">
	<div class="content-upload">
	<h1>Create Appreciation</h1>
	<center><strong class="list1 clearfix"> <html:messages
		id="msg" message="true">
		<bean:write name="msg" />
	</html:messages> </strong></center>
	<div class="box1 header1">

	<ul class="list1 clearfix">
		<li class="first">Name</li>
		<li class="first fll">Header</li>
		<li class="second">Attach image</li>
		<li class="second" style="margin-left: 100px;">Content</li>
	</ul>
	</div>


	<ul class="list2 form1" id="appreciationList">
		<logic:iterate id="employeeAppreciationList"
			name="kmEmployeeAppreciationFormBean" indexId="rowCount"
			property="employeeAppreciationList">
			<% 
				if (rowCount<6)
				{
				String cssName = "clearfix";	
				if( rowCount%2==1)
				{			
				cssName = "clearfix alt";
				}	
				%>
			<li class="<%=cssName%>">
				<p class="clearfix fll margin-r10">
					<span class="textbox6">	
						<span class="textbox6-inner"> 
							<html:text name="employeeAppreciationList" property="employeeName" styleClass="textbox1" indexed="true" value="" />
						</span> 
					</span>
				</p>
				<p class="clearfix fll margin-r10">
					<span class="textbox6">
						<span class="textbox6-inner"> 
							<html:text name="employeeAppreciationList" property="appreciationHeader" indexed="true" styleClass="textbox1" value="" /> 
						</span> 
					</span>
				</p>
				<p class="clearfix fll margin-r10">
				<span>
					<html:file name="employeeAppreciationList" property="employeeImage" styleId='<%= "employeeImage"+rowCount%>'
						onchange="validateImage(this)" indexed="true" /> 
			</span>
				</p>
				<p class="clearfix fll margin-r10"> 
				<span>
		             	<html:textarea name="employeeAppreciationList" style="overflow:hidden" property="appreciationContent" indexed="true"	styleClass="textarea1 flr" />
		                <html:hidden name="employeeAppreciationList" property="appreciationContent" indexed="true" styleId='<%= "content"+rowCount%>' value="" /> 
		         </span>
		         </p>
	   </li>
			<%
			}
			%>
		</logic:iterate>
	</ul>
	<br>
	<a class="add-more-btn flr" onclick="addMoreAppreciationFields()">&nbsp;</a>
	<br>
	<h6>&nbsp;&nbsp;Supported Image: jpg or gif; Standard Dimention:
	height 80px, width 84px; Size maximum: 100 KB</h6>

	</div>
	</div>

	<div class="button-area">
	<div class="button"><input class="submit-btn1 red-btn" name=""
		value="" onclick="return submitForm();" /></div>
	<div class="button"><a class="red-btn"
		onclick="return clearForm();">clear</a></div>
	</div>
</html:form>
