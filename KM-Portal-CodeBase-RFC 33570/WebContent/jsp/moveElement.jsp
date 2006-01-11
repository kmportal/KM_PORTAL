<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
</script>
	
	 
<script language="javascript">  

var circle;
function listFiles(){
	
	$id("elementPath").value=elementPath;
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	if(document.forms[0].elementLevel.value<=3){
		alert("Element Move/Copy cannot be made at or above circle level");
		return false;
	}
	document.forms[0].methodName.value = "listElements";
	return true;
}
function moveFiles(chk){
	var counter = 0;
	for (var i=0;i < document.kmMoveElementFormBean.elements.length;i++)
	{
		var e = document.kmMoveElementFormBean.elements[i];
		if (e.type == "checkbox" && e.checked)
		{
			counter++;
		}
	}
	
	if(counter < 1) {
	  alert("Please select atleast one element.");
	  return false;
	}
	else {
	    //Bug MASDB00064326  resolved 
	    var destLevel=parseInt(parentLevel)+1;
	    $id("elementPath").value=elementPath;
		$id("parentId").value=parentId;
		$id("elementLevel").value=destLevel;
		if(destLevel<=3){
			alert("Element Move/Copy cannot be made at or above circle level")
			return false;
		}
		document.forms[0].methodName.value = "moveElements";
		return true;
	}
}

function copyFiles(chk)
{
	var counter = 0;
	for (var i=0;i < document.kmMoveElementFormBean.elements.length;i++)
	{
		var e = document.kmMoveElementFormBean.elements[i];
		if (e.type == "checkbox" && e.checked)
		{
			counter++;
		}
	}
	
	if(counter < 1) {
	  alert("Please select atleast one element.");
	  return false;
	}
	else {
	    //Bug MASDB00064326  resolved 
	    var destLevel=parseInt(parentLevel)+1;
	    $id("elementPath").value=elementPath;
		$id("parentId").value=parentId;
		$id("elementLevel").value=destLevel;
		
		if(destLevel<=3){
			alert("Element Move/Copy cannot be made at or above circle level");
			return false;
		}
		
		document.forms[0].methodName.value = "copyElements";
		
		return true;
		//document.forms[0].submit();
	}
}

function loadPageAgain()
{		
		document.forms[0].methodName.value = "initMove";
		
		document.forms[0].submit();	
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
var initialParentId=<bean:write property="parentId" name="kmMoveElementFormBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmMoveElementFormBean"/>;
var parentLevel=initialLevel-1;
var elementLevelNames=new Array();
var childLevel;
var lk=0;
var emptyChildFlag=0;

<logic:iterate name="elementLevelNames" scope="request" id="levelName" >
	elementLevelNames[lk]='<bean:write name="levelName"/>';
	lk++;
</logic:iterate>
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
//<!--Change start by Vishi   -->
function loadDropdown(e)
//<!--Change end by Vishi   -->
{
	//e= e || event;
	//alert("The source element's id is: "+ (e.srcElement || e.target).id);
	//alert("Element Path0: "+elementPath);
	var tempLevelCount=levelCount;
	
	if ((e.srcElement || e.target).id!="initialSelectBox") {
		var elementId=(e.srcElement || e.target).id;
		var currentElementLevel=elementId.substring(6);
		var table=$id("table_0");
		parentLevel=currentElementLevel;
		//alert("Parent level: "+parentLevel);
		//var levelCount=$id("levelCount").value;
		//alert("currentElementLevel:  "+currentElementLevel);
		//alert("levelCount: "+levelCount);
		for (i=parseInt(currentElementLevel)+1;i<=parseInt(levelCount);i++) {
			//alert("i="+i);
			table.tBodies[0].removeChild($id("tr_level_"+i));
			//alert("cropping element Path: "+elementPath);
			elementPath=elementPath.substring(0,elementPath.lastIndexOf("/"));
			tempLevelCount--;
		}
		if(emptyChildFlag==1){
			//alert("cropping element Path for null children: "+elementPath);
			elementPath=elementPath.substring(0,elementPath.lastIndexOf("/"));
			emptyChildFlag=0;
		}
		//alert("Final Element Path after cropping: "+elementPath);
	}
	else if ((e.srcElement || e.target).id=="initialSelectBox"){
		var table=$id("table_0");
		elementPath=initialElementPath;
		parentLevel=initialLevel;
		//alert("parent level: "+parentLevel);
		//alert("levelCount"+$id("levelCount").value);
		//alert("initialLevel: "+initialLevel);
		//alert("initialElementPath: "+initialElementPath);
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
			childLevel=level;
			//alert("Done setting values");
			if (elements.length!=0) {
	            var addOption = function (value, text, selectBox){
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
	            selectDropDown.setAttribute("id",id);
	            selectDropDown.setAttribute("name",name);
				if (selectDropDown.addEventListener){
					selectDropDown.addEventListener('change', loadDropdown, false); 
				} else if (selectDropDown.attachEvent){
					selectDropDown.attachEvent('onchange', loadDropdown);
				}
				addOption("",' |Select Parent Element| ', selectDropDown);
				for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		        }
		        var table=$id("table_0");
		        var tr=document.createElement("tr");
		        var td1=document.createElement("td");
		        var td2=document.createElement("td");
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);
		        td2.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level]+" -->  ";
		        var newNode = document.createTextNode(text);
		        td1.setAttribute("align","right");
		        td1.appendChild(newNode);
   		        table.tBodies[0].appendChild(tr);
		        tr.appendChild(td1);
		        tr.appendChild(td2);
		        emptyChildFlag=0;
		    }else{
				childLevel=parseInt(levelCount)+1;
				emptyChildFlag=1;
		    }
        }
    }
}
function $id(id) {
	return document.getElementById(id);
}
function changeButton()
{
	if(document.forms[0].buttonType.value=="list")
	{
		//alert("list");
		document.getElementById("listButton").style.visibility="visible";
		document.getElementById("moveButton").style.visibility="hidden";
	} 
	else if(document.forms[0].buttonType.value=="move")
	{
		//alert("move");
		//document.getElementById("listButton").style.visibility="hidden";
		document.getElementById("moveButton").style.visibility="visible";
	} 
} 

</script>

<FONT color="red"><html:errors/></FONT>
<html:form action="/moveElementAction" >
	<html:hidden property="methodName" value="initMove"/>
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<html:hidden property="buttonType"/>
	<html:hidden property="elementPath" styleId="elementPath"/>
		<div class="box2">
        <div class="content-upload">
			<h1><bean:message key="moveElement.title" /></h1>
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

			<tr>
				<td colspan="3">
				<table id="table_0">
					<tbody>
						<tr>
							<td style="width:177px" align="right">
								Select <script type="text/javascript">
										document.write(elementLevelNames[initialLevel]);</script> --&gt;</td>
							<td style="width:238px" align="left" class="pBot2"><span class="width150">
							<!--Change start by Vishi   -->
							<html:select property="initialSelectBox" styleId="initialSelectBox" onchange="javascript:loadDropdown(event);">
							<!--Change end by Vishi   -->
								<html:option value="">|Select Parent Element|</html:option>
								<logic:present name="firstList" scope="request">
									<html:options collection="firstList" property="elementId"
										labelProperty="elementName" />
								</logic:present>
							</html:select> </span></td>
						</tr>
					</tbody>
				</table>
				</td>
			</tr>
			<TR align="center">
				<TD align="center"  class="pRight10" id="listButton" style="visibility: visible;"><input type="image" src="images/submit.jpg" value="submit" onclick="return listFiles();"/></td>
		 		<logic:notEqual name="kmMoveElementFormBean" property="initStatus" value="true" >
		 		<TD align="center"  class="pRight10" id="moveButton" style="visibility: visible;"><input type="image" src="images/move.jpg" value="submit" onclick="return moveFiles(this);"/></td>
		 		</logic:notEqual>
		 		<logic:notEqual name="kmMoveElementFormBean" property="initStatus" value="true" >
		 		<TD align="center"  class="pRight10" id="copyButton" style="visibility: visible;"><input type="image"  src="images/copy.jpg" value="submit" onclick="return copyFiles(this);"/></td>
		 		</logic:notEqual>
		 		<TD align="center"  class="pRight10" id="cancelButton" style="visibility: visible;"><input type="image"  src="images/cancel.jpg" value="submit" onclick="return loadPageAgain();"/></td>
			</TR>
			</TBODY>
	</TABLE>

	<logic:notEqual name="kmMoveElementFormBean" property="initStatus" value="true" >
	<table  align="center">
		<TR align="center">
			<td colspan="5" class="lightBg"><span class="heading"><bean:message key="moveElement.elementList" /></span></td>
		</TR>
			
		<TR class="textwhite">
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="viewAllFiles.SNO" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35" width="281"><span class="mLeft5 mTop5"><bean:message
				key="moveElement.elementName" /></span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35" width="293"><span class="mLeft5 mTop5"><bean:message
				key="moveElement.elementPath" /></span></td>
		</tr>
		
		<logic:notEmpty name="ELEMENT_LIST">
			<bean:define id="elementList" name="ELEMENT_LIST" type="java.util.ArrayList" scope="request" />
		</logic:notEmpty>
		
		<logic:empty name="elementList" >
			<TR class="lightBg">
				<TD colspan="2" class="error"  align="left"><bean:message key="viewAllFiles.NotFound" /></TD>
			</TR>
		</logic:empty>
        <% int length=0; %>
		<logic:notEmpty name="elementList" >
			<logic:iterate name="elementList" id="report" indexId="i" type="com.ibm.km.dto.KmElementMstr">
				<TR class="lightBg">
					<TD class="text" align="center"  width="30"><%=(i.intValue() + 1)%>.</TD>
					<TD class="text" align="left"><bean:write name="report"
						property="elementName" /></TD>
					<TD class="text" align="left"><html:textarea rows="3" cols="30" disabled="true" name="report"
						property="path" /></TD>
					<TD class="text" align="center">
						<html:multibox property="moveElementList" 
						name="kmMoveElementFormBean"><bean:write name="report" property="elementId"/></html:multibox>
					</TD> 	
				</TR>
				
			</logic:iterate>
		</logic:notEmpty>
		
	</table>
	</logic:notEqual>
	
<script>
changeButton();
</script>
</div>
</div>
</html:form>