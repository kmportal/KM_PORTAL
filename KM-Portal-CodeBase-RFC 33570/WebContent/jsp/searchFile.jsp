<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:notEmpty name="FILE_LIST">
<bean:define id="documentList" name="FILE_LIST" type="java.util.ArrayList" scope="request"/>

</logic:notEmpty>

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
var initialParentId=<bean:write property="parentId" name="kmDocumentMstrForm"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmDocumentMstrForm"/>;
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
		        
		        //<td style="width:177px" align="right">
		        
		        var td1=document.createElement("td");
		        var td2=document.createElement("td");
		        var sp =document.createElement("span");
		        var sptd1 =document.createElement("span");
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);		
		        
		        var tbl0 = document.getElementById("table_0");
			    var rowsLength = tbl0.rows.length;
			    if( rowsLength%2 == 1)
			    {
			        tr.setAttribute("class","alt");		
			    } 
		        td2.setAttribute("class","pBot2");	
		        td1.setAttribute("height","35");
		        sptd1.setAttribute("style","margin-left:15px");	           
		        td2.appendChild(sp);
		        sp.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        sptd1.appendChild(newNode);
		        td1.appendChild(sptd1);
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

</script>

<SCRIPT>

function listFiles(){
	
	
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	
	document.forms[0].methodName.value="viewFiles";
	document.forms[0].submit();
	
}

function deleteSubmit()
{
	if(validate())
	{
		if(confirm("Do you want to delete the checked files ? ")){
		//document.forms[0].selectedDocumentId.value=documentId;
		
		
		document.forms[0].methodName.value= "deleteDocument";
		
	}	
	else
	{
		
		
		document.forms[0].methodName.value="init";
		
	}
	
	document.forms[0].submit();
	
	}
	
	
	
} 

function validate(){

	 var chks = document.forms[0].checkedDocs;	

	if (chks){ //checkbox(s) exists
		var checked = false;
		if (chks.length){ //multiple checkboxes
			var len = chks.length;
			for (var i=0; i<len; i++){
				if (chks[i].checked){
					checked = true;
					break;
				}
			}
		}
		else{ //single checkbox only
			checked = chks.checked;
		}
		if (!checked){
			alert("Please check at least one file to delete");
			return false;
		}
	}
		    
		    
		return true;
		
	}


function displaySubmit(documentPath)
{
	
	document.forms[0].selectedDocumentPath.value=documentPath;
	document.forms[0].methodName.value= "displayDocument";
	document.forms[0].submit();
	
}  
function editSubmit(documentId)
{
	document.forms[0].selectedDocumentId.value=documentId;
	document.forms[0].methodName.value= "initEditDocument";
	document.forms[0].submit();
}
function downloadDocument(documentPath)
{
	//window.location.href="documentAction.do?methodName=downloadDocument&documentId="+documentId";
	document.forms[0].selectedDocumentPath.value=documentPath;
	document.forms[0].methodName.value= "downloadDocument";
	document.forms[0].submit();  
}

</SCRIPT>
<html:form action="/documentAction" >
	<html:hidden property="methodName" /> 
	<html:hidden property="deleteStatus" />
	<html:hidden property="selectedDocumentPath" />
	<html:hidden property="selectedDocumentId" />
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<html:hidden property="initStatus"/>
	
<div class="box2">
  <div class="content-upload">
   <h1><bean:message key="viewAllFiles.ViewFiles" /></h1>
   <table width="100%" class="form1 mTop5"  id="table_0" border="0" cellspacing="0" cellpadding="0">
			<tbody>
				<tr>
					<td style="width:160px" height="35" ><span style="margin-left:15px">
						Select <script type="text/javascript">
									document.write(elementLevelNames[initialLevel]); </script></span>
					</td>
					<td style="width:360px" align="left" class="pBot2"><span class="width172">
					<!--Change start by Vishi   -->
					<html:select property="initialSelectBox" styleClass="select1" styleId="initialSelectBox" onchange="javascript:loadDropdown(event);" >
					<!--Change end by Vishi   -->
						<html:option value="">Select</html:option>
						<logic:present name="firstList" scope="request">
							<html:options collection="firstList" property="elementId"
								labelProperty="elementName" />
						</logic:present>
					</html:select> </span>
				  </td>
				</tr>
			</tbody>
		</table>
		
		
    <table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
	  <tr align="left">
		 <td width="600px" height="60" colspan="2" align="center" valign="bottom"><input type="image" src="images/submit.jpg"  onclick="return listFiles();"/></td>
	  </tr>
	  <tr>
		  <td colspan="2" class="pTop4 pLeft10">
				<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
					<tr align="left">
						<td class=""><font color="#0B8E7C"><strong>
							<html:messages id="msg" message="true"><bean:write name="msg"/> </html:messages></strong></font>
						</td>
					</tr>
				</table>
		  </td>
	    </tr>
		<tr>
			<td colspan="2" class="pTop4 pLeft10">				
					<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
						<tr align="left">
							<td class=""><font color="#0B8E7C"><strong>
							<html:errors/></strong></font>
						</tr>
					</table>					
			</td>
		</tr>
	</table>
		
	<logic:notEqual name="kmDocumentMstrForm" property="initStatus" value="true">
		<table width="100%" class="mLeft5" align="center">
			<tr class="lightBg">
			  <td  align="left" ><B><FONT size="2">Search Result for : &nbsp;&nbsp;<bean:write name="kmDocumentMstrForm" property="elementPath" /></FONT></B></td>
			</tr>
			<tr><td>
				<BR>
				</td>
			</tr>			
	    </table>
	    
		<div class="box2">
			<H1>Search Result </H1>
		
		
		<table align=center width="100%" cellpadding="0" cellspacing="0" border="0" style="table-layout: fixed;">
		<tr  style="background-color: #C5C2C2">
			<td width="5%" style="background-color: #C5C2C2">
				<span class="mTop5"><bean:message key="viewAllFiles.SNO" /></span></td>			
			<td width="20%" >
			    <span class="mTop5"><bean:message key="viewAllFiles.DocumentName" /></span></td>			
			<td width="25%">
				<span class="mTop5"><bean:message key="viewAllFiles.path" /></span></td>
			<td width="10%">
				<span class="mTop5"><bean:message key="viewAllFiles.approvalStatus" /></span></td>	
			<td width="16%">
				<span class="mTop5"><bean:message key="viewAllFiles.UploadedDate" /></span></td>
			<!--<td width="15%">
				<span class="mLeft5 "><bean:message key="viewAllFiles.ApprovalDate" /></span></td>-->
			<td width="7%">
				<span class="mTop5">Action</span>&nbsp;</td>
			<td width="6%">
				<span class="mTop5"> <A Href="#" onclick="javascript: deleteSubmit();" id="deleteLink" ><FONT color="red"><U>
				<bean:message key="viewAllFiles.delete" /></U></FONT></A>&nbsp;&nbsp;</span></td>
			<!-- added by beeru on 12 May	-->
				<logic:equal name ="kmDocumentMstrForm" property="downloadAccess" value="Y">
					<td><span class="mTop5">&nbsp;&nbsp;<bean:message key="viewAllFiles.download" /></span></td>
				</logic:equal>
			<!-- end by beeru on 12 May	-->
			</tr>
		<logic:empty name="documentList" >
		  <script language="javascript"> document.getElementById("deleteLink").disabled = true;  </script>			
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
		</logic:empty>
			
		<logic:notEmpty name="documentList" >
			<logic:iterate name="documentList" type="com.ibm.km.dto.KmDocumentMstr" id="report" indexId="i">
				<% String cssName = "";	if( i%2==1){cssName = "alt";}%>
				<tr class="<%=cssName%>">	
					<TD height="25"><span>&nbsp;<%=(i.intValue() + 1)%>.</span></TD>
					<TD style="word-wrap: break-word"><span ><A HREF='<bean:write name="report" property="documentViewer"/>&docID=<bean:write name="report" property="documentId"/>' class="Red11" ><bean:write name="report"
						property="documentDisplayName" /></A></span></TD>
					<TD class="text" style="word-wrap: break-word"><bean:write name="report" property="documentStringPath" /></TD>					
					<TD><span><bean:write name="report" property="approvalStatus" /></span>&nbsp;</TD>	
					<TD style="word-wrap: break-word"><span><bean:write name="report" property="uploadedDate" /></span></TD>
				<!--	<TD><span ><bean:write name="report" property="approvalRejectionDate" /></span></TD>-->
					<logic:notEqual name="kmDocumentMstrForm" property="kmActorId" value="3" >
					<TD class="text" align="center"><A Href="#" onclick="javascript: editSubmit('<bean:write name="report" property="documentId"/>');"><FONT color="red"><U><bean:message key="editDocument.edit"/></U></FONT></A>&nbsp;</TD>
					<TD class="text" align="center"><html:multibox  property="checkedDocs" styleClass="checbox2" value= '<%=report.getDocumentId()+""%>' /></TD>
					<!--<A Href="#" onclick="javascript: deleteSubmit('<bean:write name="report" property="documentId"/>');"><FONT color="red"><U><bean:message key="viewAllFiles.delete" /></U></FONT></A>&nbsp;&nbsp;--> 
					<!-- added by beeru on 12 May	-->
					<logic:equal name ="kmDocumentMstrForm" property="downloadAccess" value="Y">
						<TD class="text" align="center"><A Href="documentAction.do?methodName=downloadDocument&docID=<bean:write name="report" property="documentId"/>" ><FONT color="red"><U><bean:message key="viewAllFiles.download" /></U></FONT></A></TD>
					</logic:equal>
					<!-- end by beeru on 12 May	-->
					</logic:notEqual>
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
		</table>
	  </div>
	</logic:notEqual>
	
	</div>
	
	</div>
</html:form>
