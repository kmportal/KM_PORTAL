<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">

<logic:notEmpty name="AGENT_LIST">
<bean:define  id="userList" name="AGENT_LIST" type="java.util.ArrayList" scope="request"/>

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

var initialParentId=<bean:write property="parentId" name="kmUserReportForm"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmUserReportForm"/>;
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
//Change end by Vishi   -->
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
			//elementPath=elementPath.substring(0,elementPath.lastIndexOf("/"));
			tempLevelCount--;
		}
		if(emptyChildFlag==1){
			//alert("cropping element Path for null children: "+elementPath);
			//elementPath=elementPath.substring(0,elementPath.lastIndexOf("/"));
			emptyChildFlag=0;
		}
		
		//alert("Final Element Path after cropping: "+elementPath);
	}
	else if ((e.srcElement || e.target).id=="initialSelectBox"){
		var table=$id("table_0");
		//elementPath=initialElementPath;
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
	   // elementPath=elementPath+"/"+parentId;
	    //alert("Submitting with element Path: "+elementPath);
	    var url=path+"/userReport.do?methodName=loadElementDropDown&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    //alert(url);
	    if(levelCount>3)
		{ 
		return;
		}
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
		        td1.setAttribute("height","35");
		       
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
	document.kmUserReportForm.methodName.value="agentWiseExcelReport";
	document.kmUserReportForm.submit();
}

function listFiles(){
var LOBId=document.kmUserReportForm.initialSelectBox.value;
var partner=document.kmUserReportForm.selectedPartnerName.value;

	$id("parentId").value=parentId;
	document.kmUserReportForm.methodName.value="agentWiseReport";
	document.kmUserReportForm.submit();
	//window.location.href="csrFileReport.do?methodName=agentWiseReport";

}
</SCRIPT>

<script language="JavaScript" type="text/javascript"> </script>
<html:form action="/userReport">
	<html:hidden property="methodName" /> 
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<html:hidden property="initStatus" />
	<html:hidden property="elementId"/>
	
<div class="box2">
 <div class="content-upload">
  <h1><bean:message key="agentIdWise.title" /></h1>
	<table width="100%" class="form1" cellpadding="0" cellspacing="10" border="0">
     <tr>
	  <td colspan="2">
		<table id="table_0" class="form1" cellpadding="0" cellspacing="0" border="0">
		 <tbody>
		  <tr>
		   <td style="width:177px" align="left" height="35">Select <script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script></td>
		   <td style="width:238px" align="left" class="pBot2"><span class="width150">
			  <!--Change start by Vishi   -->
			   <html:select property="initialSelectBox" styleClass="select1" styleId="initialSelectBox" onchange="javascript:loadDropdown(event);">
			   <!--Change start by Vishi   -->
				 <html:option value="">Select</html:option>
					<logic:present name="firstList" scope="request">
					<html:options collection="firstList" property="elementId" labelProperty="elementName" />
					</logic:present>
			   </html:select> </span>
		   </td>
		 </tr>
		</tbody>
	   </table>
	  </td>
	 </tr>
	 <tr>
	 	<td colspan="2">
			<table id="table_0" class="form1" cellpadding="0" cellspacing="0" border="0">
				<tbody>
					<tr>
						<td style="width:177px" align="left" height="35"><bean:message key="agentIdWise.partner" /></td>
						<td style="width:238px" align="left" class="pBot2"><span class="width150">
							<html:select property="selectedPartnerName" name="kmUserReportForm" styleClass="select1">
								<html:option value="">Select Partner</html:option>
									<logic:notEmpty name="kmUserReportForm" property="partnerList">
										<bean:define id="partners" name="kmUserReportForm"	property="partnerList" /> 
										<html:options  property="partnerName" collection="partners" />
									</logic:notEmpty>
							</html:select>
						</td>
					</tr>
				</tbody>
			</table>
		</td>
	 </tr>
	 <tr align="center" >
			<td class="pTop2" colspan="2"><img  src="images/submit.jpg"  onclick="listFiles();"/></td>
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
  </table>	
	
  <table width="100%" class="mTop30" cellpadding="0" cellspacing="0" border="0">
	<logic:present name="AGENT_LIST">	
	<tr align="right" >
		<td width="147" class="pLeft10 pTop2"></td>
		<td width="636" class="pTop2" colspan="5">
			<img  src="images/excel.gif" width="39" height="35" border="0" onclick="importExcel();" /></td>
     </tr>	
     <tr class="lightBg">
		<td  colspan="10" align="left" width="100%"><FONT size="2"><B>Search Location :</B></FONT>&nbsp;&nbsp;<bean:write name="kmUserReportForm" property="documentPath" />
		&nbsp;&nbsp;&nbsp;<FONT size="2"><B>Partner:</B></FONT>&nbsp;&nbsp;<bean:write name="kmUserReportForm" property="partnerName"/></td>
	</tr>	
		<tr class="text white">

					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">S.No</span>
					</td>
					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">User LoginId</span></td>
					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">First Name</span>
					</td>
					<td style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35"><span class="mLeft5 mTop5">Last Name</span></td>
					<!--<td class="darkRedBg height19"><span class="mLeft5 mTop5">Last LogIn Time</span></td>-->
					

				</tr>
              	 <logic:notEmpty name="AGENT_LIST">
				<logic:iterate name="userList" id="file" indexId="i"
					 type="com.ibm.km.dto.KmUserMstr">
			
				

					<TR class="lightBg">

						<TD class="text" align="left"><%=(i.intValue() + 1)%>.</TD>

						<TD class="text" align="left"><bean:write name="file"
							property="userId" /></TD>
						<TD class="text" align="left"><bean:write name="file"
							property="firstName" /></TD>
						<TD class="text" align="left"><bean:write name="file"
							property="lastName" /></TD>
					
					</TR>
				</logic:iterate>
						
		</logic:notEmpty>
			<logic:empty name="AGENT_LIST"  >
				<TR class="lightBg">
				<TD colspan="2" class="error" align="left"><FONT color="red"><bean:message
					key="viewAllFiles.NotFound" /></FONT></TD>
			</TR>
			</logic:empty>
			</logic:present>	
  </table>
 </div>
</div>
</html:form>