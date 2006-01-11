<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
</script>
<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">
 
<script language="javascript">  
//Code change after UAT observation	
function resetFields(){
	document.forms[0].initialSelectBox.value="-1";
	document.forms[0].elementName.value="";
	document.forms[0].elementDesc.value="";
	return false;
}

var circle;
function formValidate(){
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	$id("elementFolderPath").value=elementPath;
	//alert("Parent ID: "+parentId);
	//alert("elementLevel ID: "+$id("elementLevel").value);
	//alert("elementFolderPath ID: "+elementPath);
	if(validate()){
		document.forms[0].methodName.value = "insert";
		document.forms[0].submit();
		return true;
	}else{
		return false;
	}
	
}

function validate(){
	var form=document.forms[0];
	var searchChars="`~!$^&*()=+><{}[]+|=?':;\\\"-&-,@";
	var reg=/^[0-9a-zA-Z ]*$/;
	if(flag!=1){
		if(form.elementName.value==""){
			alert("Please Enter a Element name");
			form.elementName.focus();
			return false;
		}	
			
		if(isEmpty(form.elementName)){
			alert("Please Enter a Element name");
			form.elementName.value="";
			form.elementName.focus();
			return false;
		}
	
		//alert(reg.test(form.elementName.value)+" "+form.elementName.value);

		if(!reg.test(form.elementName.value)){
			alert("Underscore(_) or Alpha-Numeric Characters are not allowed");
			form.elementName.value="";
			form.elementName.focus();
			return false;	
		}
		
	}else
	{
		alert("Element Cannot be Created Under the Selected Parent. \nPlease Unselect the Parent. ");
		return false;
	}
	return true;
}


</script>
<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
</script>
<SCRIPT language="JavaScript">
var req=null; 
var ctr=1;

var levelCount=0;
var initialElementPath='<bean:write scope="request" name="allParentIdString"/>';
var elementPath=initialElementPath;
var initialParentId=<bean:write property="parentId" name="kmElementMstrFormBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmElementMstrFormBean"/>;
var parentLevel=initialLevel-1;
var elementLevelNames=new Array();
var childLevel;
var lk=0;
var emptyChildFlag=0;
var flag=0;

<logic:iterate name="elementLevelNames" scope="request" id="levelName" >
	elementLevelNames[lk]='<bean:write name="levelName"/>';
	lk++;
</logic:iterate>
var level=elementLevelNames[initialLevel];

function loadCircle()
  {
  	if(initialLevel == 3)
  	{
  	  // Populate Circles Initially for Lob User
  	  var circelDropDown = document.getElementById("createMultiple");
  	  var obj = document.getElementById('initialSelectBox');
  	 
  	  var addOption = function (value, text, selectBox){
	                var optn = document.createElement("OPTION");
	                optn.text = text;
	                optn.value = value;
	                selectBox.options.add(optn);
	            	}            	
	  cleanSelectBox("createMultiple");
	  addOption("-1",'All Circles', circelDropDown);
	  
  	 for (var i = 1; i < obj.options.length; i++) 
  	 	{ 
           addOption(obj.options[i].value, obj.options[i].text, circelDropDown); 
        }
       
  	}//for LOB User Only
  	
  }

function newXMLHttpRequest() {
    var xmlreq = false;
    if (window.XMLHttpRequest) {
        // Create XMLHttpRequest object in non-Microsoft browsers
        xmlreq = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        // Create XMLHttpRequest via MS ActiveX
        try {
            xmlreq = new ActiveXObject("Msxml2.XMLHTTP");
        } catch (e1) 
        {
            // Failed to create required ActiveXObject
            try {
                xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e2) {
                // Unable to create an XMLHttpRequest with ActiveX
            }
        }
    }

    return xmlreq;
}
function doit(){
	window.location.href="elementAction.do?methodName=init";
	document.forms[0].submit();
}
//Change start by Vishi  
function loadDropdown(e)
//Change end by Vishi 
{
	flag=0;
	var countLevel=levelCount;
	e= e || window.event;
	var tempLevelCount=levelCount;
	 var table = "";

	 //alert("Hurraaay !!!!!!  "+e);
	if ((e.srcElement || e.target).id!="initialSelectBox") {
		var elementId=(e.srcElement || e.target).id;
		var currentElementLevel=elementId.substring(6);
		table=$id("table_0");
		parentLevel=currentElementLevel;
		
		if(currentElementLevel >= 3  && initialLevel <= 3 )
		      {
		        document.getElementById("circleLabel").innerHTML = "Circles"+"&nbsp;&nbsp;&nbsp;&nbsp;";
		        document.getElementById("createMultiple").style.display = 'inline';
		       }
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
		document.getElementById("circleLabel").innerHTML = "";
		document.getElementById("createMultiple").style.display = 'none';
		table=$id("table_0");
		elementPath=initialElementPath;
		parentLevel=initialLevel;
		for (i=initialLevel+1;i<=levelCount;i++) {
			table.tBodies[0].removeChild($id("tr_level_"+i));
			tempLevelCount--;
		}
	}
	levelCount=tempLevelCount;
	//alert("Final Level count after cropping element Path: "+levelCount);
	
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
	    var url=path+"/elementAction.do?methodName=loadElementDropDown&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    //alert(url);
	    req.open("GET", url, true);
	    req.send(null);
	}else if(parentId==""){
		showTextBoxes();
		changeTrLabel(parentLevel);
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
			//alert("Level "+currentLevel+"   :"+ maxlevel);
			if(currentLevel>maxlevel)
			{
			 	alert("Element cannot be Created Beyond the Maximum Level.\n Please Unselect the Parent to create a "+elementLevelNames[parentLevel]+".");
				flag=1;
				hideTextBoxes();
			 	return;
			}
			else
			{
				 showTextBoxes();
			}
			if(level >= 4  && initialLevel <= 3 )
		      {
		        document.getElementById("circleLabel").innerHTML = "Circles";
		        document.getElementById("createMultiple").style.display = 'inline';
		       }
			//alert("Done setting values");
			if (elements.length!=0) {
	            var addOption = function (value, text, selectBox){
	                var optn = document.createElement("OPTION");
	                optn.text = text;
	                optn.value = value;
	                selectBox.options.add(optn);
	            }
	            
	              var addCircle = function (value, text, selectBox){
	                var optn = document.createElement("OPTION");
	                optn.text = text;
	                optn.value = value;
	                selectBox.options.add(optn);
	            }
	          
	            var form=document.forms[0];
	            
	            var id="level_"+level;
	            levelCount=level;
	            //alert(levelCount);
	            var name="element_"+(++ctr);
	            var selectDropDown = document.createElement("SELECT");
	            var circelDropDown = document.getElementById("createMultiple");
	            selectDropDown.setAttribute("id",id);
	            selectDropDown.setAttribute("name",name);
	            selectDropDown.setAttribute("class","select1");
				if (selectDropDown.addEventListener){
					selectDropDown.addEventListener('change', loadDropdown, false); 
				} else if (selectDropDown.attachEvent){
					selectDropDown.attachEvent('onchange', loadDropdown);
				}
				addOption("",'Select', selectDropDown);
				for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		        }
		        if(currentLevel == 3)
		        {
		        	cleanSelectBox();
		        	addCircle("-1",'All Circles', circelDropDown);
		        	for (var j = 0; j < elements.length; j++) {
		            addCircle(elements[j].elementId, elements[j].elementName, circelDropDown);
		        	}
		        }
		       
		        var table=$id("table_0");
		        var tr=document.createElement("tr");
		        var st=document.createElement("strong");
		        var td1=document.createElement("td");
		        var td2=document.createElement("td");
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);
		        td2.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        td1.setAttribute("align","left");	
		        td1.setAttribute("height","30");		        
		        td1.appendChild(newNode);
		        st.appendChild(td1);
   		        table.tBodies[0].appendChild(tr);
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
    changeTrLabel(childLevel);
}
	
 function cleanSelectBox()
  {
  	var obj = document.getElementById('createMultiple');
    if (obj == null) return;
	if (obj.options == null) return;
	while (obj.options.length > 0) {
		obj.remove(0);
	}
  }
  
   function selectAll()
  {
    var obj = document.getElementById('createMultiple');
  	if(obj.value == "-1")
  	{	
  		obj.options[0].selected = false;	
  	 	for (var i = 1; i < obj.options.length; i++) 
  	 	{ 
           obj.options[i].selected = true; 
        }
     }  
  }

function $id(id) {
	return document.getElementById(id);
}
function changeTrLabel(childElementLevelId)
{
	
    document.getElementById('tr_0').innerHTML="<strong>"+elementLevelNames[childElementLevelId]+" Name"+"<strong>";
    document.getElementById('tr_1').innerHTML="<strong>"+elementLevelNames[childElementLevelId]+" Desc"+"<strong>";
    document.getElementById('title').innerHTML=elementLevelNames[childElementLevelId];
}
function hideTextBoxes()
{
 	document.getElementById("tr_1").style.visibility="hidden";		
	document.getElementById("tr_0").style.visibility="hidden";
	document.getElementById("elementName").style.visibility="hidden";
	document.getElementById("elementDesc").style.visibility="hidden";

}
function showTextBoxes(){
	document.getElementById("tr_1").style.visibility="visible";		
	document.getElementById("tr_0").style.visibility="visible";
    document.getElementById("elementName").style.visibility="visible";
	document.getElementById("elementDesc").style.visibility="visible";
}
</script>

<FONT color="red"><html:errors/></FONT>
<html:form action="/elementAction">
	<html:hidden property="methodName" value="insert"/>
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<html:hidden property="elementFolderPath" styleId="elementFolderPath"/>
	<html:hidden property="token" styleId="token"/> 
	<div class="box2"> 
     <div class="content-upload">        	
	  <h1>Create <span id="title"><script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script></span></h1>
	  <TABLE cellspacing="10px" border="0"  class="form1"> 
		<TBODY>
		<tr>
			<td colspan="4" align="center" class="error">
			  <strong> 
	         	<html:messages id="msg" message="true">
	                <bean:write name="msg"/>  
	                         
	            </html:messages>
	          </strong>
	       </td>
	   </tr>
       <tr><td colspan="4">
				<table id="table_0" width="100%" class="form1" border="0">
					<tr class="lightBg" >
						<td  align="left" class="text" width="180" height="30"><strong> Select <script
							type="text/javascript">document.write(elementLevelNames[initialLevel]);</script>
							</strong>
						</td>
						<!--Change start by Vishi   -->
						<td><html:select property="initialSelectBox" styleId="initialSelectBox" styleClass="select1"
							onchange="javascript:loadDropdown(event);">
						<!--Change end by Vishi   -->
							<html:option value=""> Select  </html:option>
							<logic:present name="firstList" scope="request">
								<html:options collection="firstList" property="elementId" labelProperty="elementName" />
							</logic:present>
							</html:select>
						</td>
						<td width="140" align="right"> <strong> <div id="circleLabel"> </div> </strong> </td>
						<td rowspan="3"  valign="top" class="mTop20">
							<html:select property="createMultiple"  styleId="createMultiple" style="display:none; "
								multiple="multiple" size="6" onchange="javascript:selectAll();">
							</html:select></td>
					</tr>
				</table>
			</td>
        </tr>
		<TR>
			<TD id="tr_0"  width="150"  align="left"  class="text"><strong><script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script> Name </strong></TD>
			<TD colspan="3" class="pBot2" align="left">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:text styleId="elementName"  property="elementName" name="kmElementMstrFormBean" maxlength="50"></html:text>
			 &nbsp;Suggested: best view upto 30 charaters, max 50 characters limit.</TD>
		</TR>
		<TR>
			<TD id="tr_1"  align="left"  class="text"><strong><script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script> Desc </strong></TD>
			<TD colspan="3" align="left" class="pBot2">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<html:text styleId="elementDesc" property="elementDesc" name="kmElementMstrFormBean" maxlength="150"/></TD>
		</TR>
		<TR>
			<TD align="center" class="pRight10" colspan="4"><input class="red-btn" value="Submit" alt="Submit" onclick="javascript:return formValidate();" readonly="readonly"/></td>
		</TR>			
	 </TBODY>
	</TABLE>	
   </div>
 </div>
</html:form>