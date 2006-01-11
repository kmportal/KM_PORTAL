
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<bean:define id="kmUserBean" name="USER_INFO"  type="com.ibm.km.dto.KmUserMstr" scope="session" />

<html:errors />

<SCRIPT>
function showScrollerMessage()
	{
		var message='<div style="margin-top:2px;" ><font face=arial size=2 color="white"><B><bean:write name="csrHomeBean" property="message" /></B></font></font></DIV>'
		document.getElementById('slider').innerHTML=setMessage(message);
		timer();
			
	}

function timer(){
	getAlerts()
	window.setTimeout('timer()',30000)
	}

function getAlerts()
{
	var url="kmAlertMstr.do?methodName=view";
	if(window.XmlHttpRequest) {
		req = new XmlHttpRequest();
		
	}else if(window.ActiveXObject) {
		req=new ActiveXObject("Microsoft.XMLHTTP");
		
	}
	if(req==null) {
		alert("Browser Doesnt Support AJAX");
		return;
	}
	req.onreadystatechange = getOnChange;
	req.open("POST",url,true);
	req.send();
	
}
function getOnChange()
{
	if (req.readyState==4 || req.readyState=="complete") {
	if(req.responseText=="none")
	{
	
	}
	else{
	//alert("YOU HAVE A NEW MESSAGE");
	document.getElementById("alert").value = req.responseText;
	
	}
}
}

</SCRIPT>

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
</script>
<script language="javascript">  
var circle;
function formValidate(){
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	if(document.kmFeedbackMstrFormBean.comment.value==""||isEmpty(document.kmFeedbackMstrFormBean.comment)){
		alert("Please Enter Comment");
		document.kmFeedbackMstrFormBean.comment.focus();
		return false;
	}else{
		document.kmFeedbackMstrFormBean.methodName.value = "insert";
		//document.kmFeedbackMstrFormBean.submit();
	}
}
function validate(){
	var form=document.forms[0];
	if(form.comment.value==""){
		alert("Please Enter Comment");
		form.comment.focus();
		return false;
	}	
	if(isEmpty(form.comment)){
		alert("Please Enter Comment");
		form.comment.value="";
		form.comment.focus();
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
var initialParentId=<bean:write property="parentId" name="kmFeedbackMstrFormBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmFeedbackMstrFormBean"/>;
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

function loadDropdown(selectBox,e)
{
	e= e || event;
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

function timer(){
	getAlerts()
	window.setTimeout('timer()',30000)
	}

function getAlerts()
{
	var url="kmAlertMstr.do?methodName=view";
	if(window.XmlHttpRequest) {
		req = new XmlHttpRequest();
		
	}else if(window.ActiveXObject) {
		req=new ActiveXObject("Microsoft.XMLHTTP");
		
	}
	if(req==null) {
		alert("Browser Doesnt Support AJAX");
		return;
	}
	req.onreadystatechange = getOnChange;
	req.open("POST",url,true);
	req.send();
	
}
function getOnChange()
{
	if (req.readyState==4 || req.readyState=="complete") {
	if(req.responseText=="none")
	{
	
	}
	else{
	//alert("YOU HAVE A NEW MESSAGE");
	document.getElementById("alert").value = req.responseText;
	
	}
}
}

</script>

<script language="JavaScript" type="text/JavaScript">
function limitText(textArea, length) {
if (textArea.value.length > length) {
alert ("Please limit comments length to "+length+" characters.");
textArea.value = textArea.value.substr(0,length);
}
}
</script>

<html:form action="/kmFeedbackMstr" >
	<html:hidden property="methodName" value="insert"/> 
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
			<table width="100%" class="mTop30" align="center" cellspacing="0" cellpadding="0">
					<tr align="left">
							<td colspan="2">&nbsp;&nbsp;&nbsp;<img src="images/give-feeback.gif" ></td>
					</tr>
                    <tr><td colspan="2">
                    <table id="table_0"><tbody>
					<tr>
					  <td align="right" class="pRight10" width="179">
					  	Select <script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script>
						</td>
					  <td align="left" class="pBot2" width="258">
                      <span class="width150" >
                   <html:select property="initialSelectBox" styleId="initialSelectBox" onchange="javascript:loadDropdown(this);">
                        	<html:option value="">|Select Parent Element|</html:option>
                        	<logic:present name="firstList" scope="request">
                        	<html:options collection="firstList" property="elementId" labelProperty="elementName" />
                        	</logic:present>
                        </html:select>
                      </span>
                      </td>
                    </tr></tbody>
                    </table>
                    </td>
                    </tr>
					<tr>
						<td align="right" width="170" class="pLeft10 pTop2" valign="top">&nbsp;&nbsp;&nbsp;&nbsp;Your Comments<span class="redText">*</span> :</td>
						<td align="left" width="630" class="pTop2">&nbsp;&nbsp;&nbsp;&nbsp;<html:textarea property="comment" name="kmFeedbackMstrFormBean" cols="50" rows="8" onkeydown="limitText(this,350);"></html:textarea></td>
					</tr>
					<tr align="left">
						<td width="147" class="pLeft10 pTop2"></td>
						<td width="636" class="pTop2"><BR>&nbsp;&nbsp;&nbsp;&nbsp;<input type="image" src="images/submit_button.jpg" onclick="return formValidate();"/> 
                       	</td>
					</tr>
					<tr>
						<td colspan="2" class="pTop8 pLeft10">
						<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
							<tr align="left">
								<td class=""><font color="#0B8E7C"><strong>
								
							<html:messages id="msg" message="true">
               				<bean:write name="msg"/>  
                    		</html:messages></strong></font></td>
							</tr>
						</table>
						</td>
					</tr>
					<tr>
						<td colspan="2" class="pTop8 pLeft10">
							<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
							<tr align="left">
								<td class=""><font color="#0B8E7C"><strong>
								<html:errors/></strong></font>
							</tr>
							</table>
						</td>
					</tr>
		    </table>
	</html:form>
