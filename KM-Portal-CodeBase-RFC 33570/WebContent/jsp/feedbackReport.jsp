<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
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
//<!--Change start by Vishi   -->
function loadDropdown(e)
//<!--Change end by Vishi   -->
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
		        var td1=document.createElement("th");
		        var td2=document.createElement("td");
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);
		        td2.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        td1.setAttribute("align","left");
		        td1.setAttribute("height","30");
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
<script>

function listFeedbacks(){
	
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	var today = new Date();
	var dd = today.getDate();
	var mm = today.getMonth()+1;//January is 0!
	var yyyy = today.getFullYear();
	if(dd<10){dd='0'+dd}
	if(mm<10){mm='0'+mm}
	var curr_dt=yyyy+'-'+mm+'-'+dd;
	var startDate=document.forms[0].startDate.value;
	var endDate=document.forms[0].endDate.value;
	if(startDate=='' || endDate ==''){
		alert("Dates cannot be blank");
		return false;
	}
	if(startDate > endDate){
		alert("From Date Cannot be greater than To date");
		return false;
	}
	if(endDate > curr_dt ){
		alert("Dates cannot be a future date");
		return false;
	}
	document.forms[0].methodName.value="feedbackReport";
	document.forms[0].submit();
	
}
function importExcel(){
$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	document.forms[0].methodName.value="feedbackExcelReport";
	document.forms[0].submit();
 
 }
</SCRIPT>
<html:form action="/kmFeedbackMstr" >
	<html:hidden property="methodName" /> 
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<div class="box2">
     <div class="content-upload">
	  <h1>Feedback Report</h1>
	   <table width="95%"  align="center">
		<tr>
			<td colspan="2" align="center" class="error" width="902">
				<strong><font color=red> <html:errors /></font></strong>
			</td>
        </tr> 
		<tr align="center">
			<td colspan="2" class="pTop8 pLeft10" width="902">
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
		</table>
		
		<ul class="list2 form1">
				
			<li class="clearfix">
			<span class="text2 fll width160"><strong> Send Date(From)&nbsp;&nbsp;&nbsp;		</strong></span>	  
				<input type="text" class="tcal calender2 fll" readonly="readonly" name="startDate" value="<bean:write property='startDate' name='kmFeedbackMstrFormBean'/>"/>
			</li>
			<li class="clearfix alt">
			<span class="text2 fll width160"><strong> Send Date(To)&nbsp;&nbsp;&nbsp;</strong></span>
				<input type="text" class="tcal calender2 fll" readonly="readonly" name="endDate" value="<bean:write property='endDate' name='kmFeedbackMstrFormBean'/>"/>
			</li>
		    <li>
                    <table id="table_0" class="form1"><tbody>
					<tr>
					  <th style="width:158px" align="left" height="30">
					  	Select <script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script> &nbsp;&nbsp;
					  </th>
					  <td style="width:236px" align="left" class="pBot2">
                      <span>
                      <!--Change start by Vishi   -->
                        <html:select property="initialSelectBox" styleClass="select1" styleId="initialSelectBox" onchange="javascript:loadDropdown(event);">
                        <!--Change start by Vishi   -->
                        	<html:option value="">Select</html:option>
                        	<logic:present name="firstList" scope="request">
                        	<html:options collection="firstList" property="elementId" labelProperty="elementName" />
                        	</logic:present>
                        </html:select>
                      </span>
                      </td>
                    </tr></tbody>
                    </table>
             </li>
       		 <li class="clearfix" style="padding-left:170px;">
				<span class="text2 fll">&nbsp;</span>
				<input class="red-btn" value="Submit" alt="Submit" onclick="javascript:return listFeedbacks();" readonly="readonly"/>
            </li>
       </ul>
   
   <table width="100%" cellpadding="0" cellspacing="0" border="0" >
   <logic:notEqual name="kmFeedbackMstrFormBean" property="initStatus" value="true">	
     	<tr>
			<td class="flr" colspan="6"><img  src="images/excel.gif" width="39" height="35" border="0" onclick="importExcel();" />&nbsp;&nbsp;&nbsp;</td>			
        </tr>	
           <!-- Changes Made Against Defect Id MASDB00105338 -->
        <tr class="lightBg">
			<td  colspan="10" align="left"><FONT size="2"><B>Search Location :</B></FONT><bean:write name="kmFeedbackMstrFormBean" property="elementFolderPath"/></td>
		</tr>
		<tr class="textwhite">
			<td width="5%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"><span class="mTop5">&nbsp;<bean:message key="feedback.SNO" /></span> </td>
			<td width="17%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"><span class="mTop5">Date Feedback Given </span></td>
			<td width="15%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"><span class="mTop5">Date Action Taken</span></td>
			<td width="15%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"><span class="mTop5">User ID</span> </td>
			<td width="30%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"><span class="mTop5"><bean:message key="feedback.comment" /></span> </td>
			<td width="18%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"><span class="mTop5">Action Taken(Accepted/Rejected)</span> </td>
		</tr>
		<logic:empty name="kmFeedbackMstrFormBean" property="feedbackList">
		 <TR class="lightBg">
		  <TD colspan="2" class="error" align="left" height="15"><FONT color="red"><bean:message key="viewAllFiles.NotFound" /></FONT></TD>
		 </TR>
		</logic:empty>
			
		<logic:notEmpty name="kmFeedbackMstrFormBean" property="feedbackList">
			<logic:iterate name="kmFeedbackMstrFormBean" id="report" indexId="i" property="feedbackList">
			 <%	String cssName = ""; if( i%2==1){cssName = "alt";}%>
				<TR class="<%=cssName%>">	
					<TD height="25" class="text mLeft5">&nbsp;<%=(i.intValue() + 1)%>.</TD>
					<TD class="text mLeft5"><bean:write name="report" property="createdDate" /></TD>
					<TD class="text mLeft5"><bean:write name="report" 	property="updatedDate" /></TD>
					<TD class="text mLeft5"><bean:write name="report" 	property="createdBy" /></TD>
					<TD class="text mLeft5"><bean:write name="report" 	property="comment" /></TD> 
				   <TD class="text mLeft5"><bean:write name="report" 	property="readStatus" /></TD> 
				</TR>
			</logic:iterate>  
		</logic:notEmpty>
	</logic:notEqual>		
   </table>
 </div>
</div>
</html:form>