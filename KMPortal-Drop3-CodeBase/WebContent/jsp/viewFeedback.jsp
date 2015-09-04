<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">



<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript"	>
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

function limitText(textArea, length) 
  {
	if (textArea.value.length > length) {
	alert ("Please limit response length to "+length+" characters.");
	textArea.value = textArea.value.substr(0,length);
	}
  }

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
//<!--Change start by Vishi   -->
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
	   // alert(url);
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
		        
		        var st =document.createElement("strong");
		        var sp =document.createElement("span");
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);
		        td2.setAttribute("align","left");
		        td2.setAttribute("style","width:238px");
		        td2.setAttribute("class","pBot2");
		        sp.setAttribute("class","width172");
		        td2.appendChild(sp);
		        sp.appendChild(selectDropDown);
		        
		        var text="Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        td1.setAttribute("align","left");
		        td1.setAttribute("style","width:177px");
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

</script>
<script>

function readFeedbacks()
{
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	//alert("read "+document.forms[0].parentId.value);
/*	if(document.forms[0].feedbackList.length==0)
	{	
		
		alert("Please Mark Atleast One Feedback as Read");
		return false;
	} */
	document.forms[0].methodName.value="readFeedback";
	document.forms[0].submit();
	

}
function listFeedbacks(){
	
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	
	//alert(document.forms[0].parentId.value);
	document.forms[0].methodName.value="viewFeedback";
	document.forms[0].submit();
	
}
function importExcel(){
document.kmFeedbackMstrFormBean.methodName.value="ViewFeedbackExcelReport";
document.kmFeedbackMstrFormBean.submit();
 
 }
</SCRIPT>

  

	<div class="box2">
        <div class="content-upload">
<html:form action="/kmFeedbackMstr" >
	<html:hidden property="methodName" /> 
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	

	<H1><bean:message key="feedback.viewFeedback" /></H1>

	<table width="100%" align="left">
	 <tr>
		<td colspan="2" align="center" class="error">
		   <strong><font color=red> <html:errors /></font></strong>
		</td>
	 </tr> 
	 <tr align="left">
		<td colspan="2" class="pTop8 pLeft10">
			
			<table width="100%" border="0" cellpadding="5" cellspacing="0" class="text">
				<tr align="left">
					<td class=""><font color="#0B8E7C"><strong>
					 <html:messages id="msg" message="true">	<bean:write name="msg"/></html:messages></strong></font></td>
				</tr>
			</table>				
		</td>
	  </tr>
      <tr align="left">
     	 <td colspan="2">
            <table id="table_0" class="form1" cellspacing="10px">
               <tbody>
				<tr>
				  <td style="width:177px" align="left">
				 <strong>  	Select <script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script> 
					</strong> </td>
				  <td style="width:238px" align="left" class="pBot2"><span class="width172">
				  <!--Change start by Vishi   -->			  
                     <html:select property="initialSelectBox"  styleClass="select1"  styleId="initialSelectBox" onchange="javascript:loadDropdown(event);">
                     <!--Change end by Vishi   -->
                     	<html:option value="">Select</html:option>
                     	<logic:present name="firstList" scope="request">
                     	<html:options collection="firstList" property="elementId" labelProperty="elementName" />
                     	</logic:present>
                     </html:select>
                   </span>
                   </td>
                </tr>
               </tbody>
              </table>
           </td>
        </tr>
         <tr align="center">
         
        <div class="button-area">
        
	            <div class="button">
	            <td style="width:550px" class="pTop2" align="center" colspan="2">
	            <a  class="red-btn" onclick="javascript:return listFeedbacks();">SUBMIT</a></td>
	        </div>
      </div>  
       </tr>
			<!--<td style="width:550px" class="pTop2" align="center" colspan="2">
				<input type="image" src="images/submit.jpg" class="btnActive"   onclick="return listFeedbacks();"/>
			</td> 
		
   
   --><logic:notEqual name="kmFeedbackMstrFormBean" property="initStatus" value="true">	
	   <tr align="right" >
			<td width="147" class="pLeft10 pTop2"></td>
			<td width="636" class="pTop2" colspan="5"><img  src="images/excel.gif" width="39" height="35" border="0" onclick="importExcel();" /></td>
	   </tr>
	   <tr class="lightBg">
		<td  colspan="10" align="left" width="90%"><FONT size="2">&nbsp;<B>Search Location : </B></FONT><bean:write name="kmFeedbackMstrFormBean" property="elementFolderPath"/></td></tr>
		
		<tr class="textwhite">
			
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5"><bean:message key="feedback.SNO" /></span> </td>
			
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class=" mTop5"><bean:message
				key="feedback.comment" /></span> </td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class=" mTop5">Element Path</span></td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class=" mTop5"><bean:message
				key="feedback.createdBy" /></span> </td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class=" mTop5"><bean:message
				key="feedback.createdDt" /></span> </td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class=" mTop5">Feedback Response</span> </td>
			<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class=" mTop5">Action</span> </td>
		<!--	<td bgcolor="a90000" align="center"></td>
			<td bgcolor="a90000" align="center">&nbsp;</td>
			<td bgcolor="a90000" align="center">&nbsp;</td> -->
		</tr>
		<logic:empty name="kmFeedbackMstrFormBean" property="feedbackList">
			
			<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
			
		</logic:empty>
			
		<logic:notEmpty name="kmFeedbackMstrFormBean" property="feedbackList">
			<logic:iterate name="kmFeedbackMstrFormBean" id="report" indexId="i" property="feedbackList">
			    <%
				String cssName = "";				
				if( i%2==1)
				{			
				cssName = "alt";
				}	
				%>
				<tr class="<%=cssName%>">	
					<TD height="25" class="text" width="5%">&nbsp;<%=(i.intValue() + 1)%>.</TD>
					<TD class="text"  width="25%" ><bean:write name="report" property="comment" /></TD>
					<TD class="text"  width="30%" ><bean:write name="report" property="feedbackStringPath" />&nbsp;</TD>
					<TD class="text"  width="10%" ><bean:write name="report" property="createdBy" />&nbsp;</TD>
					<TD class="text"  width="15%" ><bean:write name="report" property="createdDate" />&nbsp;</TD>
					<%--
					<TD class="text" align="center">
					<html:multibox property="readFeedbackList"
					name="kmFeedbackMstrFormBean"><bean:write name="report" property="feedbackId"/></html:multibox>
					</TD>
					--%> 
					<%-- Added by Atul for Feedback Response  --%>	
				   <TD class="text" align="center"  width="25%" >
				   <html:textarea name="kmFeedbackMstrFormBean" styleClass="textarea1" 
						property="feedbackResp" onkeydown="limitText(this,250);" ></html:textarea></TD> 
				    <%--	 Atul added ended --%>					
					
					
					<%-- Added by Atul for Feedback combo box --%> 
				    <TD class="text" align="center" width="15%">
					<html:select property="feedbackArray" name="kmFeedbackMstrFormBean">
      				<html:option value="N">Select</html:option>
					<html:option value="I">Incorporated</html:option>
					<html:option value="R">Rejected</html:option>
					</html:select>
					</TD>
              </TR>
			</logic:iterate> 
		<tr align="center">
			<td colspan="7"><BR>
			  <input class="red-btn flr"  style="margin-right:20px;" value="Update" alt="Update" onclick="return readFeedbacks();"/>
			</td>
		</tr>
	</logic:notEmpty>
			
			
	</logic:notEqual>		
	</table>
<BR>
</html:form>
	</div>
	</div>