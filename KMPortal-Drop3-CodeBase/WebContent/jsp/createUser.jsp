<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />
<bean:define id="loginActorId" name="LOGIN_USER_ACTOR_ID" type="java.lang.String" scope="session" />

<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">

<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
</script>
	
<script language="JavaScript" type="text/javascript">


function selectCategory()
{

	if(document.kmUserMstrFormBean.kmLoginActorId.value=="1" || document.kmUserMstrFormBean.kmLoginActorId.value=="5"){
		document.getElementById("element").disabled = false;
		document.getElementById("actor").disabled = true;
	}
	
	if(document.forms[0].kmActorId.value=='6'){
		document.getElementById("favCategoryTr").style.visibility="visible";
		document.getElementById("partner").style.visibility="visible";
		document.getElementById("pbxId").style.visibility="visible";
		document.getElementById("businessSegment").style.visibility="visible";
		document.getElementById("activity").style.visibility="visible";
		document.getElementById("role").style.visibility="visible";
	
	}else if(document.forms[0].kmActorId.value=='3'){
		document.getElementById("favCategoryTr").style.visibility="hidden";	
		document.getElementById("partner").style.visibility="hidden";
		document.getElementById("pbxId").style.visibility="hidden";
		document.getElementById("businessSegment").style.visibility="hidden";
		document.getElementById("activity").style.visibility="hidden";
		document.getElementById("role").style.visibility="hidden";
		
	}else if(document.forms[0].kmActorId.value=='4'){
		document.getElementById("favCategoryTr").style.visibility="hidden";	
		document.getElementById("partner").style.visibility="visible";
		document.getElementById("pbxId").style.visibility="visible";
		document.getElementById("businessSegment").style.visibility="visible";
		document.getElementById("activity").style.visibility="visible";
		document.getElementById("role").style.visibility="visible";
			
	}else if(document.forms[0].kmActorId.value==''){
		document.getElementById("favCategoryTr").style.visibility="hidden";
		document.getElementById("partner").style.visibility="hidden";
		document.getElementById("pbxId").style.visibility="hidden";
		document.getElementById("businessSegment").style.visibility="hidden";
		document.getElementById("activity").style.visibility="hidden";
		document.getElementById("role").style.visibility="hidden";

	}else if(document.forms[0].kmActorId.value=='5'){
		document.getElementById("favCategoryTr").style.visibility="hidden";
		document.getElementById("partner").style.visibility="hidden";
		document.getElementById("pbxId").style.visibility="hidden";
		document.getElementById("businessSegment").style.visibility="hidden";
		document.getElementById("activity").style.visibility="hidden";
		document.getElementById("role").style.visibility="hidden";

	}else if(document.forms[0].kmActorId.value=='2'){
		document.getElementById("favCategoryTr").style.visibility="hidden";
		document.getElementById("partner").style.visibility="hidden";
		document.getElementById("pbxId").style.visibility="hidden";
		document.getElementById("businessSegment").style.visibility="hidden";
		document.getElementById("activity").style.visibility="hidden";
		document.getElementById("role").style.visibility="hidden";
		
	}
}

function selectCategory1()
{
	if(document.forms[0].kmActorId.value=='6'){
		document.getElementById("favCategoryTr1").style.visibility="visible";	
		document.getElementById("partner").style.visibility="visible";
		document.getElementById("pbxId").style.visibility="visible";
		document.getElementById("businessSegment").style.visibility="visible";
		document.getElementById("activity").style.visibility="visible";
		document.getElementById("role").style.visibility="visible";	
	}else if(document.forms[0].kmActorId.value=='3'){
		document.getElementById("favCategoryTr1").style.visibility="hidden";
		document.getElementById("partner").style.visibility="hidden";
		document.getElementById("pbxId").style.visibility="hidden";
		document.getElementById("businessSegment").style.visibility="hidden";
		document.getElementById("activity").style.visibility="hidden";
		document.getElementById("role").style.visibility="hidden";				
	}else if(document.forms[0].kmActorId.value=='4'){
		document.getElementById("favCategoryTr1").style.visibility="hidden";	
		document.getElementById("partner").style.visibility="hidden";
		document.getElementById("pbxId").style.visibility="hidden";
		document.getElementById("businessSegment").style.visibility="hidden";
		document.getElementById("activity").style.visibility="hidden";
		document.getElementById("role").style.visibility="hidden";			
	}else if(document.forms[0].kmActorId.value==''){
		document.getElementById("favCategoryTr1").style.visibility="hidden";	
		document.getElementById("partner").style.visibility="hidden";
		document.getElementById("pbxId").style.visibility="hidden";
		document.getElementById("businessSegment").style.visibility="hidden";
		document.getElementById("activity").style.visibility="hidden";
		document.getElementById("role").style.visibility="hidden";
	}
	document.forms[0].favCategoryId.value="";
	///document.forms[0].submit();
}		
function validateData(){

	if(document.kmUserMstrFormBean.kmActorId.value=="")
	{
		alert("Please Select A User Type");
		return false;
	}
	if(document.kmUserMstrFormBean.kmLoginActorId.value=="1" || document.kmUserMstrFormBean.kmLoginActorId.value=="5"){
		if(document.kmUserMstrFormBean.elementId.value==""){
			alert("Please Select "+document.kmUserMstrFormBean.selectedCombo.value); 
			return false;
		
		}
	} 
	
	if(document.kmUserMstrFormBean.kmLoginActorId.value=="1"){
		if(document.forms[0].kmActorId.value!='5'){
		if(document.getElementById("level_3").value=="-1"){
			alert("Please Select Circle"); 
			return false;
		
		}
		}
	} 
	

	var mob=document.kmUserMstrFormBean.userMobileNumber.value;
	var len=mob.length;
	var searchChars="`~!$^&*()=+><{}[]+|=?':;\\\"-&-,@ ";
	var searchChars1="`~!$^_&*()=+><{}[]+|=?':;\\\"-&-,@ ";
	var reg=/^[0-9a-zA-Z_]*$/;
	var regmob=/^[0-9]*$/;
	var names=/^[a-zA-Z_]*$/;
	//alert(document.kmUserMstrFormBean.userLoginId.value);

	if(isEmpty(document.kmUserMstrFormBean.userLoginId)){
			alert("Please enter User Login Id");
			document.kmUserMstrFormBean.userLoginId.focus();
			return false;
	}
	if(document.kmUserMstrFormBean.userLoginId.value.length < 8){
			alert("User Login Id should not be less then 8 characters");
			document.kmUserMstrFormBean.userLoginId.focus();
			return false;
	}
	if(!reg.test(document.forms[0].userLoginId.value)){
		alert("Special characters except underscore not allowed in User Login Id");
		document.forms[0].userLoginId.value="";
		document.forms[0].userLoginId.focus();
		return false;	
	}

	if(isEmpty(document.kmUserMstrFormBean.userFname)){
			alert("Please enter User First Name");
			document.kmUserMstrFormBean.userFname.focus();
			return false;
	}
	if(!names.test(document.forms[0].userFname.value)){
		alert("Please Enter only Characters for First name");
		document.forms[0].userFname.value="";
		document.forms[0].userFname.focus();
		return false;	
	}
	
	if(isEmpty(document.kmUserMstrFormBean.userLname)){
			alert("Please enter User Last Name");
			document.kmUserMstrFormBean.userLname.focus();
			return false;
	}  
	if(!names.test(document.forms[0].userLname.value)){
		alert("Please Enter only Characters for Last name");
		document.forms[0].userLname.value="";
		document.forms[0].userLname.focus();
		return false;	
	}
	if(!isEmpty(document.forms[0].userMname))
	{
		if(!names.test(document.kmUserMstrFormBean.userMname.value)){
			alert("Special Characters are not Allowed in Middle Name");
			document.kmUserMstrFormBean.userMname.focus();
			document.kmUserMstrFormBean.userMname.value="";
			return false;	
		}
	}
	if(isEmpty(document.kmUserMstrFormBean.userEmailid))
			{
				alert("Please enter Email-ID");
				document.kmUserMstrFormBean.userEmailid.focus();
				return false;
			}	
	if(!isEmailAddress(document.kmUserMstrFormBean.userEmailid)){
			if(!isEmpty(document.kmUserMstrFormBean.userEmailid))
			{
				alert("Please enter Valid Email-ID");
				document.kmUserMstrFormBean.userEmailid.focus();
				return false;
			}	
	} 

	if(!regmob.test(document.kmUserMstrFormBean.userMobileNumber.value)){
		alert("Please enter Valid User Mobile Number");
		document.kmUserMstrFormBean.userMobileNumber.focus();
		document.kmUserMstrFormBean.userMobileNumber.value="";
		return false;	
	}
	//Bug resolved MASDB00064245
	if(mob=='00000000000' || mob=='0000000000'){
		alert("Please enter Valid User Mobile Number");
		document.kmUserMstrFormBean.userMobileNumber.focus();
		document.kmUserMstrFormBean.userMobileNumber.value="";
		return false;	
	}
	if(len<10||len>11)
	{		if(!isEmpty(document.kmUserMstrFormBean.userMobileNumber))
			{	
				
				alert("Please enter Valid User Mobile Number");
				document.kmUserMstrFormBean.userMobileNumber.focus();
				return false;
			}
	
	} 
	
	var reg=/_[\s]+/;

	if(document.kmUserMstrFormBean.kmActorId.value=="6")
		{
			if(document.kmUserMstrFormBean.favCategoryId.value=="")
			{
				alert("Please Select a Category");
				return false;
			}
		}	
		
  	

  	 if(confirm("Do you want to create a new user?"))
  	 {
  	 	if(document.kmUserMstrFormBean.kmLoginActorId.value=="1"){
  	 	if(document.forms[0].kmActorId.value!='5'){

  	 		document.forms[0].circleElementId.value = document.getElementById("level_3").value;	

		}	
		
		}
		document.kmUserMstrFormBean.roleId.value =	document.forms[0].kmActorId.value;

  	 	document.forms[0].methodName.value="insert";
		return true;
     }
      else{
        clearFields();
        return false;
      } 	
}




function clearFields(){
	
	var form=document.forms[0];
	
	form.userLoginId.value="";
	form.userFname.value="";
	form.userMname.value="";
	form.userLname.value="";
	form.userMobileNumber.value="";
	form.userEmailid.value="";
	document.getElementById("actor").disabled = false;
	form.kmActorId.value="";
	
	
	if(form.kmLoginActorId.value=="2" || form.kmLoginActorId.value=="3"){
		
		if(form.kmLoginActorId.value=="3"){
			document.getElementById("favCategoryTr1").style.visibility="hidden";
			
	}else if(form.kmLoginActorId.value=="2"){
			document.getElementById("favCategoryTr").style.visibility="hidden";
		}
		form.kmActorId.value="";
	}else {
		form.elementId.value="";
	}

	form.favCategoryId.value="";
	form.userLoginId.focus();
	
	return false;
	
}   
function focusSet()
{

	document.kmUserMstrFormBean.userLoginId.focus();
}

</script>


<script language="JavaScript" type="text/JavaScript">
function limitText(textArea, length) {
if (textArea.value.length > length) {
alert ("Please limit comments length to "+length+" characters.");
textArea.value = textArea.value.substr(0,length);
}
}

var req=null; 
var ctr=1;

var levelCount=0;
var initialElementPath=<%= request.getAttribute("allParentIdString") %>;
var elementPath = initialElementPath;
var initialParentId=<bean:write property="parentId" name="kmUserMstrFormBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmUserMstrFormBean"/>;
var parentLevel=initialLevel-1;
var elementLevelNames=new Array();
var childLevel;
var lk=0;
var emptyChildFlag=0;
var flag=0;

elementLevelNames[0] = 'Document';
elementLevelNames[1] = 'Country';
elementLevelNames[2] = 'LOB';
elementLevelNames[3] = 'Circle';
elementLevelNames[4] = 'Category';
elementLevelNames[5] = 'Sub-Category';
elementLevelNames[6] = 'SubSub-Category';
elementLevelNames[7] = 'SSS Category';
elementLevelNames[8] = 'SSSS Category';
elementLevelNames[9] = 'SSSSS Category';

var level=elementLevelNames[initialLevel];

function newXMLHttpRequest() {

    var xmlreq = false;

    if (window.XMLHttpRequest) {
        // Create XMLHttpRequest object in non-Microsoft browsers
        xmlreq = new XMLHttpRequest();

    } else if (window.ActiveXObject) {

        // Create XMLHttpRequest via MS ActiveX
        try {
            // Try to create XMLHttpRequest in later versions
            // of Internet Explorer

            xmlreq = new ActiveXObject("Msxml2.XMLHTTP");

        } catch (e1) {

            // Failed to create required ActiveXObject

            try {
                // Try version supported by older versions
                // of Internet Explorer

                xmlreq = new ActiveXObject("Microsoft.XMLHTTP");

            } catch (e2) {

                // Unable to create an XMLHttpRequest with ActiveX
            }
        }
    }

    return xmlreq;
}

function loadDropdown(selectBox,e)
{
	flag=0;
	var countLevel=levelCount;

	e= e || event;
	
	var tempLevelCount=levelCount;
	
	if ((e.srcElement || e.target).id!="initialSelectBox") {
		var elementId=(e.srcElement || e.target).id;
		var currentElementLevel=elementId.substring(6);
		var table=$id("table_0");
		parentLevel=currentElementLevel;
		
		for (i=parseInt(currentElementLevel)+1;i<=parseInt(levelCount);i++) {
			
			table.tBodies[0].removeChild($id("tr_level_"+i));
			
			elementPath=elementPath.substring(0,elementPath.lastIndexOf("/"));
			tempLevelCount--;
		}
		if(emptyChildFlag==1){
			
			elementPath=elementPath.substring(0,elementPath.lastIndexOf("/"));
			emptyChildFlag=0;
		}
		
	}
	else if ((e.srcElement || e.target).id=="initialSelectBox"){
		var table=$id("table_0");
		elementPath=initialElementPath;
		parentLevel=initialLevel;
		
		for (i=initialLevel+1;i<=levelCount;i++) {
			table.tBodies[0].removeChild($id("tr_level_"+i));
			tempLevelCount--;
		}
	}
	levelCount=tempLevelCount;
	
	
	parentId=$id((e.srcElement || e.target).id).value;
		
	if (parentId!="") {
		//Obtain an XMLHttpRequest instance
	    req = newXMLHttpRequest();
	    //alert (req);
	    if (!req) {
	        alert("Your browser does not support AJAX! Add Files module is accessible only by browsers that support AJAX. " +
	              "Please check the KM requirements and contact your System Administrator");
	        return;
	    }
	    
	    req.onreadystatechange = returnJson;
	    elementPath=elementPath+"/"+parentId;
	    //alert("Submitting with element Path: "+elementPath);
		
	    var url=path+"/kmScrollerMstr.do?methodName=loadElementDropDown&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    //alert(url);
	    req.open("GET", url, true);
	    req.send(null);
	}else if(parentId==""){
		//showTextBoxes();
		//changeTrLabel(parentLevel);
		parentLevel=parseInt(parentLevel)-1;
		if((e.srcElement || e.target).id=='initialSelectBox'){
			parentId=initialParentId;
		}else if(parentLevel==initialLevel){
			parentId=$id("initialSelectBox").value;
		}else{
			parentId=$id("level_"+parentLevel).value;
		}
	}
}
   function cleanSelectBox(selectBox)
  {
  	var obj = document.getElementById(selectBox);
    if (obj == null) return;
	if (obj.options == null) return;
	while (obj.options.length > 0) {
		obj.remove(0);
	}
  }
  

function returnJson() {
    if (req.readyState == 4) {
        if (req.status == 200) {
        	//alert(req.responseText);
            var json = eval('(' + req.responseText + ')');
            
			var elements = json.elements;
			level = json.level;
			
			
			//Bug resolved 
			if(window.element){
			}
			else{
				level1=parentLevel;
			}
			childLevel=level;
			var maxlevel=elementLevelNames.length-1;
			var currentLevel=parseInt(parentLevel)+1;
			
			var actorId = document.forms[0].kmActorId.value;
		    var addOption = function (value, text, selectBox){
               var optn = document.createElement("OPTION");
               optn.text = text;
               optn.value = value;
               selectBox.options.add(optn);
	            }
	            
			
			if(actorId == "5")
			{
					if(currentLevel == 4 && document.forms[0].kmActorId.value=='6')
					{
					var selectDropDown = document.getElementById("favCategoryId");
					cleanSelectBox("favCategoryId");
					addOption("", "Select Category ", selectDropDown);
					for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		            }
		        }
				return;
			}
			else{
				if(currentLevel>3)
				{
				 	if(currentLevel == 4 && document.forms[0].kmActorId.value=='6')
					{				
				 	var selectDropDown = document.getElementById("favCategoryId");				 	
				 	cleanSelectBox("favCategoryId");
				 	addOption("", "Select Category ", selectDropDown);
					for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		            }
		        }
				 	return;
				}
			
			}
			if (elements.length!=0) {

	          
	           var form=document.forms[0];

	            var id="level_"+level;
	            levelCount=level;
	            var name="element_"+(++ctr);
	            var selectDropDown = document.createElement("SELECT");
	            selectDropDown.setAttribute("id",id);
	            selectDropDown.setAttribute("name",name);
	            selectDropDown.setAttribute("class","select1");
				if (selectDropDown.addEventListener){
					selectDropDown.addEventListener('change', loadDropdown, false); 
				} else if (selectDropDown.attachEvent){
					selectDropDown.attachEvent('onchange', loadDropdown);
				}
				addOption("-1",'Select '+elementLevelNames[level], selectDropDown);
				for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		        }
		        var table=$id("table_0");
		        var tr=document.createElement("tr");
		        var st=document.createElement("strong"); 
		        var font=document.createElement("font"); 
		        var td1=document.createElement("td");
		        var td2=document.createElement("td");
		        var trId="tr_"+id;
		  
		        tr.setAttribute("id",trId);
		        td2.appendChild(selectDropDown);
		        var text=""+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        var star = document.createTextNode("*");
		        td1.setAttribute("align","left");
		        font.setAttribute("color","red");		        
		        td1.appendChild(newNode);
   		        table.tBodies[0].appendChild(tr);
   		        font.appendChild(star);
   		        td1.appendChild(font);
   		        st.appendChild(td1);   		        
		        tr.appendChild(st);
		        tr.appendChild(td2);
		        emptyChildFlag=0;
		    }else{
				levelCount=level1;
				childLevel=parseInt(levelCount)+1;
				emptyChildFlag=1;
		    }
        }
    }
  
}
function $id(id) {
	return document.getElementById(id);
	
}

function initialFavCategory()
{
		var addOption = function (value, text, selectBox){
               var optn = document.createElement("OPTION");
               optn.text = text;
               optn.value = value;
               selectBox.options.add(optn);
	            }
		if(document.forms[0].kmLoginActorId.value=="1" || document.forms[0].kmLoginActorId.value=="5")
		{	
				var selectDropDown = document.getElementById("favCategoryId");
				cleanSelectBox("favCategoryId");	
				addOption("", "Select Category ", selectDropDown);
		}
}


</script>

<html:form action="/kmUserMstr">

	<html:hidden name="kmUserMstrFormBean" property="methodName" />
	<html:hidden name="kmUserMstrFormBean" property="roleId" />
	<html:hidden property="selectedCombo" />
	<html:hidden name="kmUserMstrFormBean" property="circleElementId"/>

	<html:hidden property="kmLoginActorId"/>
	
 <div class="box2">
  <div class="content-upload">
	<table width="100%" align="center" cellspacing="0"
		cellpadding="0">		
		<tr>
				<td colspan="2" align="center" class="error">
					<strong> 
          			<html:messages id="msg" message="true">
                 		<bean:write name="msg"/>  <br>                          
             		</html:messages>
            		</strong>
            	</td>
		</tr>
		<tr >
			<td colspan="4" class="error" align="center"><strong><html:errors/></strong></td>
		</tr>
		<logic:notEqual name="loginActorId" value="2">
		<logic:notEqual name="loginActorId" value="3">
		<tr>
			<td colspan="4" class="content-upload"><h1><bean:message
				key="createUser.NewUser" /></h1></td>
		</tr>	
		</logic:notEqual>
		</logic:notEqual>
		
		<logic:notEqual name="loginActorId" value="1">
		<logic:notEqual name="loginActorId" value="5">
		<tr>
			<td colspan="4" class="content-upload"><h1><bean:message
				key="createUser.NewUser" /></h1></td>		
		</tr>
		</logic:notEqual>
		</logic:notEqual>	
		</table>  	
		 <ul class="list2 form1 ">
		  <!--  Added By Parnika -->	
		<logic:equal name="loginActorId"  value="1">
		<li class="clearfix" id="actor" >
				<span class="text2 fll width160"><strong><bean:message key="createUser.UserType" /></strong></span>
				<html:select property="kmActorId" name="kmUserMstrFormBean" styleId="selectedActorId" onchange="return selectCategory();" styleClass="select1">
						<option value="" ><bean:message key="createUser.select" /></option>
						<option value="5" >LOB Admin</option>
						<option value="2" >Circle Admin</option>
						<option value="3" >Circle User</option>
						<option value="7" >Report Admin</option>
						<option value="<bean:message key = 'TSGUser' />">TSG User </option>
						<option value="4">Circle CSR</option>
						<option value="6">Category CSR </option>
						<bean:message key = 'TSGUser' />
				</html:select>
		</li>
		</logic:equal>	
		<logic:equal name="loginActorId"  value="5">
		<li class="clearfix" id="actor" >
				<span class="text2 fll width160"><strong><bean:message key="createUser.UserType" /></strong></span>
				<html:select property="kmActorId" name="kmUserMstrFormBean" styleId="selectedActorId" onchange="return selectCategory();" styleClass="select1">
						<option value="" ><bean:message key="createUser.select" /></option>
						<option value="2" >Circle Admin</option>
						<option value="3" >Circle User</option>
						<option value="7" >Report Admin</option>
						<option value="<bean:message key = 'TSGUser' />">TSG User </option>
						<option value="4">Circle CSR</option>
						<option value="6">Category CSR </option>
						<bean:message key = 'TSGUser' />
				</html:select>
		</li>
		</logic:equal>	
		
		<logic:equal name="loginActorId"  value="2">
		<li class="clearfix alt" id="actor" >
				<span class="text2 fll width160"><strong><bean:message
					key="createUser.UserType" /></strong></span>
				<html:select property="kmActorId" name="kmUserMstrFormBean" styleId="selectedActorId" onchange="return selectCategory();" styleClass="select1">
						<option value="" ><bean:message key="createUser.select" /></option>
						<option value="3" >Circle User</option>
						<option value="7" >Report Admin</option>
						<option value="<bean:message key = 'TSGUser' />">TSG User </option>
						<option value="4">Circle CSR</option>
						<option value="6">Category CSR </option>											
						<bean:message key = 'TSGUser' />
			</html:select>
		</li>
		</logic:equal>
		
		<!-- End of changes by Parnika -->
		<logic:notEqual name="loginActorId" value="2">
		<logic:notEqual name="loginActorId" value="3">
		<li class="clearfix alt" id="element" disabled="disabled">
		<table id="table_0">
			<tr>
			 <td height="40px">
			 <span class="text2 fll" style="width: 155px;"><bean:write name="kmUserMstrFormBean" property="selectedCombo" /><FONT color="red" size="1">*</FONT></span></td>
					<td height="40px"><html:select property="elementId" styleId="initialSelectBox" name="kmUserMstrFormBean" styleClass="select1" onchange="javascript:loadDropdown(this);">
								<html:option value="">Select <bean:write name="kmUserMstrFormBean" property="selectedCombo" />&nbsp;&nbsp;</html:option>
								<logic:notEmpty name="kmUserMstrFormBean" property="elementList" >
									<bean:define id="elements" name="kmUserMstrFormBean" property="elementList" /> 
										<html:options labelProperty="elementName" property="elementId"  collection="elements" />
								</logic:notEmpty>
					</html:select></td>
			</tr>
		 </table>					
		</li>
		</logic:notEqual>
		</logic:notEqual>
		<logic:equal name="loginActorId"  value="3">
		<li class="clearfix" id="actor">
				<span class="text2 fll width160"><strong><bean:message
					key="createUser.UserType" /></strong></span>
	
				<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner">
				<html:select property="kmActorId" name="kmUserMstrFormBean" styleId="selectedActorId" onchange="return selectCategory1();" styleClass="select1">
						<option value="" ><bean:message key="createUser.select" /></option>
						<option value="4">Circle CSR</option>
						<option value="6">Category CSR </option>
				</html:select></span> </span> </p>
		</li>
		</logic:equal>
	
		<logic:equal name="loginActorId" value="3">
		<li class="clearfix alt" id="favCategoryTr1" style="visibility: hidden;">
						<span class="text2 fll width160"><strong><bean:message key="createUser.category" /><FONT color="red"> *</FONT>&nbsp;&nbsp;</strong></span>
						<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner">
							<html:select property="favCategoryId" name="kmUserMstrFormBean" styleClass="select1" styleId="favCategoryId">
								<html:option value="">Select Category</html:option>
								<logic:notEmpty name="kmUserMstrFormBean" property="categoryList">
									<bean:define id="categories" name="kmUserMstrFormBean"	property="categoryList" /> 
									<html:options labelProperty="elementName" property="elementId" collection="categories" />
								</logic:notEmpty>
							</html:select>``````
						</span> </span> </p>
		</li>
		</logic:equal>
			<li class="clearfix">
          	<span class="text2 fll width160"><strong><bean:message
				key="createUser.UserLoginId" /></strong> </span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77" property="userLoginId" maxlength="25"  /></span> </span> </p>
		</li>
		<li class="clearfix alt">
          	<span class="text2 fll width160"><strong><bean:message
				key="createUser.FirstName" /></strong> </span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77" property="userFname" maxlength="40" /></span> </span> </p>
		</li>
		<li class="clearfix">
          	<span class="text2 fll width160"><strong><bean:message
				key="createUser.MiddleName" /></strong></span>
		<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77" property="userMname" maxlength="40" /></span> </span> </p>
			</li>
		<li class="clearfix alt">
          	<span class="text2 fll width160"><strong><bean:message
				key="createUser.LastName" /></strong></span>
		<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77"
				property="userLname" maxlength="40" /></span> </span> </p>
			</li>
		<li class="clearfix">
          	<span class="text2 fll width160"><strong><bean:message key="createUser.E-Mail" /></strong></span>
		<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77"
				property="userEmailid" maxlength="40" /></span> </span> </p>
			</li>
		<li class="clearfix alt">
          	<span class="text2 fll width160"><strong><bean:message key="createUser.Mobile" /></strong></span>
		<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77"
				property="userMobileNumber" maxlength="11" /></span> </span> </p>
			</li>
			
		<li class="clearfix" id="partner" style="visibility: hidden;">
          	<span class="text2 fll width160"><strong><bean:message key="createUser.Partner" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77"
				property="partner" maxlength="11" /></span> </span> </p>
		</li>
		<li class="clearfix alt" id="pbxId" style="visibility: hidden;">
          	<span class="text2 fll width160"><strong><bean:message key="createUser.PbxId" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77"
				property="pbxId" maxlength="11" /></span> </span> </p>
		</li>
		<li class="clearfix " id="businessSegment" style="visibility: hidden;">
          	<span class="text2 fll width160"><strong><bean:message key="createUser.BusinessSegment" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77"
				property="businessSegment" maxlength="11" /></span> </span> </p>
		</li>
			<li class="clearfix alt" id="activity" style="visibility: hidden;">
          	<span class="text2 fll width160"><strong><bean:message key="createUser.Activity" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77"
				property="activity" maxlength="11" /></span> </span> </p>
		</li>
			<li class="clearfix" id="role" style="visibility: hidden;">
          	<span class="text2 fll width160"><strong><bean:message key="createUser.Role" /></strong></span>
			<p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner"><html:text styleClass="textbox77"
				property="role" maxlength="11" /></span> </span> </p>
		</li>
					

		<logic:notEqual name="loginActorId" value="3">	
		<li class="clearfix alt" id="favCategoryTr" style="visibility: hidden;">
		 <span class="text2 fll width160"><strong><bean:message key="createUser.category" /><FONT color="red"> *</FONT>&nbsp;&nbsp;</strong></span>		
			<html:select property="favCategoryId" name="kmUserMstrFormBean" styleId="favCategoryId" styleClass="select1">
				<html:option value="">Select Category</html:option>
				<logic:notEmpty name="kmUserMstrFormBean" property="categoryList">
					<bean:define id="categories" name="kmUserMstrFormBean"	property="categoryList" /> 
					<html:options labelProperty="elementName" property="elementId" collection="categories" />
				</logic:notEmpty>
			</html:select>				
		</li>
		</logic:notEqual>	
			<!--	<script>document.getElementById("favCategoryTr").style.visibility="visible";</script>  -->
	 </ul>
	 
	 <div class="button-area">
            <div class="button">
              <input class="submit-btn1 red-btn" type="submit" value="" onclick="return validateData();" />
            </div>
            <div class="button"> <input class="red-btn" onclick="return clearFields();" type="button" value="clear" />  </div>
          </div>
	  </div>
	  	<jsp:include page="Disclaminer.jsp"></jsp:include>
	  </div>
</html:form>
<script language="javascript"> 
initialFavCategory();
</script>