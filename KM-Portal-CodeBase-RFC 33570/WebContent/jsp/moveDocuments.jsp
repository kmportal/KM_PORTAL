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
	//alert(elementPath);
	$id("documentPath").value=elementPath;
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	document.forms[0].methodName.value = "listDocuments";
	document.forms[0].submit();
}


function moveFiles(chk){
    
  var counter = 0;
	for (var i=0;i < document.kmMoveDocumentFormBean.elements.length;i++)
	{
		var e = document.kmMoveDocumentFormBean.elements[i];
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
	$id("documentPath").value=elementPath;
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	
	var toMoveLevel = parseInt(parentLevel);
	if(toMoveLevel < 4 )
	{
	  alert("Document can be moved below Circle Level only, pls select any category");
	  return false;
	}
	
	document.forms[0].methodName.value = "moveDocuments";
	document.forms[0].submit();
  }	
}
//added by vishwas
function copyFiles(chk){
    
  var counter = 0;
	for (var i=0;i < document.kmMoveDocumentFormBean.elements.length;i++)
	{
		var e = document.kmMoveDocumentFormBean.elements[i];
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
	$id("documentPath").value=elementPath;
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	
	var toMoveLevel = parseInt(parentLevel);
	if(toMoveLevel < 4 )
	{
	  alert("Document can be moved below Circle Level only, pls select any category");
	  return false;
	}
	
	document.forms[0].methodName.value = "copyDocuments";
	document.forms[0].submit();
  }	
}

//end by vishwas


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
var initialParentId=<bean:write property="parentId" name="kmMoveDocumentFormBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmMoveDocumentFormBean"/>;
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
		        var table=$id("table_0");
		        var tr=document.createElement("tr");
		        var td1=document.createElement("td");
		        var st=document.createElement("strong");
		        var td2=document.createElement("td");
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);
		        td2.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        td1.setAttribute("align","left");
		        st.appendChild(newNode);
		        td1.appendChild(st);
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
		document.getElementById("listButton").style.visibility="hidden";
		document.getElementById("moveButton").style.visibility="visible";
	} 
} 

</script>

<FONT color="red"><html:errors/></FONT>
<html:form action="/moveDocumentAction" >
	<html:hidden property="methodName" />
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<html:hidden property="buttonType"/>
	<html:hidden property="documentPath" styleId="documentPath"/>
	
	<div class="box2">
        <div class="content-upload">
	<h1><bean:message key="moveDocument.title" /></h1>
			
	<TABLE width="100%"> 
			<TBODY>
			<tr>
			<td colspan="2" align="center" class="error">
			<strong> 
          	<html:messages id="msg" message="true" >
                 <bean:write name="msg" filter="false"/>  
                          
             </html:messages>
            </strong>
            </td>
		   </tr>

			<tr>
				<td colspan="2">
				<table id="table_0" class="form1" cellspacing="10">
					<tbody>
						<tr>
							<td style="width:177px" align="left"><strong>
								Select <script type="text/javascript">
										document.write(elementLevelNames[initialLevel]);</script></strong></td>
							<td style="width:238px" align="left" class="pBot2"><span class="width150">
							<!--Change start by Vishi   -->
							<html:select property="initialSelectBox" styleClass="select1" styleId="initialSelectBox" onchange="javascript:loadDropdown(event);">
							<!--Change end by Vishi   -->
								<html:option value="">Select</html:option>
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
			<!-- Code change after UAT observation -->
			<TR align="center">
				<TD align="right"  class="pRight10" id="listButton" style="visibility: visible;">
					<input class="red-btn" value="Submit" alt="Submit" onclick="javascript:return listFiles();" readonly="readonly"/>
				</td>
				
		 		<logic:notEqual name="kmMoveDocumentFormBean" property="initStatus" value="true" >
		 		<TD align="left"  class="pRight10" id="moveButton" style="visibility: visible;">
		 			<input class="red-btn" value="Move" alt="Move" onclick="return moveFiles(this);" readonly="readonly"/></td>
		 		</logic:notEqual>
		 		<logic:notEqual name="kmMoveDocumentFormBean" property="initStatus" value="true" >
		 		<TD align="left"  class="pRight10" id="moveButton" style="visibility: visible;">
		 			<input class="red-btn" value="Copy" alt="Copy" onclick="return copyFiles(this);" readonly="readonly"/></td>
		 		</logic:notEqual>
		 		
			</TR>
			</TBODY>
	</TABLE>

	<logic:notEqual name="kmMoveDocumentFormBean" property="initStatus" value="true" >
	<table width="100%" cellpadding="0" cellspacing="0" border="0">
		<TR >
			<td colspan="5" class="lightBg"><span class="heading"><bean:message key="moveDocument.documentList" /></span></td>
		</TR>
			
		<TR class="textwhite">
			<th width="6%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mTop5">&nbsp;<bean:message key="viewAllFiles.SNO" /></span></th>
			<th  width="37%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"  ><span class="mTop5"><bean:message key="moveDocument.documentName" /></span></th>
			<th  width="50%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class="mTop5"><bean:message key="moveDocument.documentPath" /></span></th>
			<th  width="7%" align="center" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class="mTop5">Select</span></th>
		</tr>
		
		<logic:notEmpty name="DOCUMENT_LIST">
			<bean:define id="documentList" name="DOCUMENT_LIST" type="java.util.ArrayList" scope="request" />
		</logic:notEmpty>
		
		<logic:empty name="documentList" >
			<TR class="lightBg">
				<TD colspan="2" class="error"  align="left"><bean:message key="viewAllFiles.NotFound" /></TD>
			</TR>
		</logic:empty>

		<logic:notEmpty name="documentList" >
			<logic:iterate name="documentList" id="report" indexId="i" type="com.ibm.km.dto.KmDocumentMstr">
				<%	String cssName = ""; if( i%2==1){cssName = "alt";}%>
				<TR class="<%=cssName%>">	
					<TD class="text" align="left" height="25">&nbsp;<%=(i.intValue() + 1)%>.</TD>
					<!-- updated by Shiva -->
					<!-- hyperlink is removed, document has to moved only, no need to open document here -->
					<TD class="text" align="left" ><FONT color="red"><bean:write name="report" property="documentDisplayName" /></FONT></TD>
					<TD class="text" align="left"><bean:write name="report" property="documentStringPath"/></TD>
					<TD class="text" align="center"><html:multibox property="moveDocumentList"	name="kmMoveDocumentFormBean"><bean:write name="report" property="elementId"/></html:multibox>
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



