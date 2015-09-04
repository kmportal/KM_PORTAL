<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">

<logic:notEmpty name="FILE_COUNT_DTO">
<bean:define id="fileCount" name="FILE_COUNT_DTO" type="com.ibm.km.dto.FileReportDto" scope="request"/>
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
var initialParentId=<bean:write property="parentId" name="kmFileReportForm"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmFileReportForm"/>;
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
		        var td1=document.createElement("td");
		        var td2=document.createElement("td");
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);
		        td2.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        td1.setAttribute("align","left");
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
</script>

<SCRIPT>
function importExcel(){
 $id("parentId").value=parentId;
$id("elementLevel").value=parseInt(parentLevel)+1;
document.kmFileReportForm.methodName.value="approvedRejectedExcelReport";
document.kmFileReportForm.submit();
}
function listFiles(){
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	document.forms[0].methodName.value="approvedRejectedFiles";
	document.forms[0].submit();
}
</SCRIPT>
<html:form action="/fileReport" >
	<html:hidden property="methodName" /> 
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<html:hidden property="initStatus" />
	<div class="box2">
        <div class="content-upload">
	<H1>File Count Report</H1>
		<table width="100%" class="form1" align="left" cellspacing="0" cellpadding="0">
            <tr align="left">
				<td colspan="2">
				<table id="table_0" cellspacing="10" cellpadding="0">
					<tbody>
						<tr>
							<td style="width:177px" align="left">
								Select <script type="text/javascript">
										document.write(elementLevelNames[initialLevel]);</script></td>
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
			<tr align="center">
				<td class="pTop10" colspan="2" align="center">
					<input class="red-btn" value="Submit" alt="Submit" onclick="javascript:return listFiles();" readonly="readonly"/>
				</td>
			</tr>
					<tr>
						<td colspan="2" class="pTop4 pLeft10">
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
						<td colspan="2" class="pTop4 pLeft10">
								<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
									<tr align="left">
										<td class=""><font color="#0B8E7C"><strong>
										<html:errors/></strong></font>
									</tr>
								</table>
						</td>
					</tr>

	<logic:notEmpty name="fileCount" >

	<tr align="right" >
			<td width="147" class="pLeft10 pTop2"></td>
			<td width="636" class="pTop2" colspan="5"><img  src="images/excel.gif" width="59" height="35" border="0" onclick="importExcel();"></td>
    </tr>	
	<tr class="lightBg">
		<td  align="left" width="90%"><FONT size="2"><B>File Count for : &nbsp;&nbsp;<bean:write name="fileCount" property="elementPath" /></B></FONT> 
		</td>
	</tr>
	<TR>
	<TD><BR>
	<TABLE align="center" border="1" cellpadding="5" cellspacing="0" bordercolor="maroon">
	<tr class="lightBg" height="25">
		  <th  class="pLeft10"> File Type</th> 
		  <th  class="pLeft10 pRight10 "> File Count</th>
	</tr>
	<tr class="lightBg" height="20">
		  <td width="220" class="pLeft10"> Approved Files 
		  <td class="text" align="center">
		  <bean:write name="fileCount" property="approvedFileCount" />
			</td>
	</tr>
	<tr class="lightBg" height="20">
		  <td  align="left" width="220"  class="pLeft10"> Rejected Files 
		  <td class="text" align="center">
		  <bean:write name="fileCount" property="rejectedFileCount" />
			</td>
	</tr>
		<tr class="lightBg"  height="20">
		  <td  align="left" width="220"  class="pLeft10"> Pending Files 
		  <td class="text" align="center">
		  <bean:write name="fileCount" property="pendingFileCount" />
			</td>
	</tr>
		<tr class="lightBg"  height="20">
		  <td  align="left" width="220"  class="pLeft10"> OldFiles Files 
		  <td class="text" align="center">
		  <bean:write name="fileCount" property="oldFileCount" />
			</td>
	</tr>
	<tr class="lightBg"  height="20">
		  <td  align="left" width="220"  class="pLeft10"> Deleted Files 
		  <td class="text" align="center">
		  <bean:write name="fileCount" property="deletedFileCount" />
			</td>
	</tr>
	<tr  height="20">
	<td  class="pLeft10"><B>Total Files</B></td>
	<td align="center"><B><bean:write name="fileCount" property="total" /></B></td>
	</tr>
	</TABLE> </TD> </TR>
		</logic:notEmpty>
	</table>

	</div>
	</div>
</html:form>