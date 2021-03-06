<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<LINK href="./theme/css.css" rel="stylesheet" type="text/css">
<LINK href=./jsp/theme/css.css rel="stylesheet" type="text/css">

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
</script>
		 
<script language="javascript">  

var elementLvl;
function listElements(){
	
	$id("elementPath").value=elementPath;
	$id("parentId").value=parentId;
	$id("elementLevel").value=parseInt(parentLevel)+1;
	
	elementLvl=document.forms[0].elementLevel.value;
	document.forms[0].methodName.value = "viewElements";	
	document.forms[0].submit();
	
}
function editSubmit(elementId)
{
	document.forms[0].selectedElementId.value=elementId;
	document.forms[0].methodName.value= "initEditElement";
	document.forms[0].submit();
}

	function getSelectedCircles() {

	var str="";
	var obj = document.getElementById('createMultiple');

	for (i=0;i< obj.options.length;i++) {
    if (obj.options[i].selected) {
		if(i != (obj.options.length -1))    
         str = str + obj.options[i].text + ",";
        else
         str = str + obj.options[i].text;
    	}
	}
	
	return str;


  }


function deleteSubmit(elementId)
{	 
	 
	 if(document.getElementById("createMultiple").value== "")
	 {
	 	if(confirm("Do You Want To Delete the Element ? ")){
		document.forms[0].selectedElementId.value=elementId;
		document.forms[0].methodName.value= "deleteElement";
		document.forms[0].submit();
		return true;
	 }
	 else
	  {
		return false;
	  }
	 
	}
	else
	{
		if(confirm("Do You Want To Delete the Element from all the Selected Circles viz "+getSelectedCircles())){
		document.forms[0].selectedElementId.value=elementId;
		document.forms[0].methodName.value= "deleteElement";
		document.forms[0].submit();
		return true;
	 }
	 else
	  {
		return false;
	  }
	
	}	
	
	
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
var initialElementPath= '<%= request.getAttribute("allParentIdString") %>' ;
var elementPath=initialElementPath;
var initialParentId=<bean:write property="parentId" name="kmElementMstrFormBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmElementMstrFormBean"/>;
var parentLevel=initialLevel-1;
var elementLevelNames=new Array();
var childLevel;
var lk=0;
var emptyChildFlag=0;

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
  	
  	if(initialLevel <= 3)
  	{
  	
  	var  displayCircle = document.getElementById("displayCircle").value;
  	
  	if(displayCircle == "true")
  	{
  		var json = '<%= request.getAttribute("js") %>' ;
  		json = eval('(' + json + ')');
        var elements = json.elements;
        var circelDrop = document.getElementById("createMultiple");
        
        var addCircle = function (value, text, selectBox){
	                var optn = document.createElement("OPTION");
	                optn.text = text;
	                optn.value = value;
	                selectBox.options.add(optn);
	            }
   
        document.getElementById("circleLabel").innerHTML = "Circles ";
		document.getElementById("createMultiple").style.display = 'inline';
	               
  		cleanSelectBox();
		addCircle("-1",'All Circles', circelDrop);
		for (var j = 0; j < elements.length; j++) 
		{ 
			addCircle(elements[j].elementId, elements[j].elementName, circelDrop);
		}
  		
  		
  	}
  	
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
		if(currentElementLevel >= 3  && initialLevel <= 3 )
		      {
		        document.getElementById("circleLabel").innerHTML = "Circles ";
		        document.getElementById("createMultiple").style.display = 'inline';
		       }
		
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
		document.getElementById("circleLabel").innerHTML = "";
		document.getElementById("createMultiple").style.display = 'none';
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
			if(level >= 4  && initialLevel <= 3 )
		      {
		        document.getElementById("circleLabel").innerHTML = "Circles ";
		        document.getElementById("createMultiple").style.display = 'inline';
		       }
			
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
		        if(level == 3)
		        {
		        	cleanSelectBox();
		        	addCircle("-1",'All Circles', circelDropDown);
		        	for (var j = 0; j < elements.length; j++) {
		            addCircle(elements[j].elementId, elements[j].elementName, circelDropDown);
		        	}
		        }
		        
		        var table=$id("table_0");
		        var tr=document.createElement("tr");
		        var td1=document.createElement("td");
		        var td2=document.createElement("td");
		        var st=document.createElement("strong"); 
		        var trId="tr_"+id;
		        tr.setAttribute("id",trId);
		        td2.appendChild(selectDropDown);
		        var text="Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        td1.setAttribute("align","left");
		        td1.setAttribute("valign","middle");
		        td2.setAttribute("valign","middle");
		        td1.setAttribute("height","30");
		        td1.appendChild(newNode);
   		        table.tBodies[0].appendChild(tr);
		        st.appendChild(td1);   		        
		        tr.appendChild(st);
		        tr.appendChild(td2);
		        emptyChildFlag=0;
		    }else{
				childLevel=parseInt(levelCount)+1;
				emptyChildFlag=1;
		    }
        }
    }
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
</script>
<FONT color="red"><html:errors/></FONT>
<html:form action="/elementAction" >
	<html:hidden property="methodName" value="viewElements"/>
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="levelCount" styleId="levelCount"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<html:hidden property="elementPath" styleId="elementPath"/>
	<html:hidden property="selectedElementId" />
	<html:hidden property="oldElementName"/>
	<html:hidden property="displayCircle" styleId="displayCircle"/>
		<div class="box2">
        <div class="content-upload">
	<h1>View/Edit Elements</h1>
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
		   <TR><TD><BR></TD></TR>
			<tr>
				<td colspan="4">
				<table id="table_0" class="form1" cellspacing="10px">
					<tbody>
						<tr>
							
							<td style="width:238px" align="left" class="pBot2"><span class="width150">
							<!--Change start by Vishi   -->
							<html:select property="initialSelectBox" styleId="initialSelectBox" styleClass="select1" onchange="javascript:loadDropdown(event);">
							<!--Change end by Vishi   -->
								<html:option value="">Select</html:option>
								<logic:present name="firstList" scope="request">
									<html:options collection="firstList" property="elementId"
										labelProperty="elementName" />
								</logic:present>
							</html:select> </span></td>
							<td width="69"> <div id="circleLabel" style="font-weight: bold;"> </div> </td>
						<td width="248" rowspan = "3" valign="top">
						<html:select property="createMultiple" styleId="createMultiple" style="display:none; width:100px;"
							multiple="multiple" size="6" onchange="javascript:selectAll();">
						</html:select></td>
						
						</tr>
					</tbody>
				</table>
				</td>
			</tr>
			<TR >
  			  <TD colspan="2" align="center">
				  <div class="button-area">
			       <div class="button"><input class="submit-btn1 red-btn"  name="" value="" onclick="javascript:return listElements();" /></div>
			      </div>
              </td>
            </TR>
		</TBODY>
	</TABLE>

	<logic:notEqual name="kmElementMstrFormBean" property="initStatus" value="true" >
	<table width="100%" align="center" border="0"  cellpadding="0" cellspacing="0">
		<TR align="center">
			<td colspan="5" height="30" class="lightBg"><span class="heading"><bean:message key="moveElement.elementList" /></span></td>
		</TR>
			
		<TR class="textwhite">
			<th width="5%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" height="35" ><span class=" mTop5">&nbsp;<bean:message key="viewAllFiles.SNO"/></span></th>
			<th width="20%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class=" mTop5"><bean:message key="moveElement.elementName"/></span></th>
			<th width="55%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"  ><span class=" mTop5"><bean:message key="moveElement.elementPath"/></span></th>
			<th width="10%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;" ><span class=" mTop5"></span>Action</th>
			<th width="10%" style="background-image:url(./images/left-nav-heading-bg_grey.jpg); background-repeat:repeat-x;"  ><span class=" mTop5"></span>Action</th>
		</tr>
		
		<logic:notEmpty name="ELEMENT_LIST">
			<bean:define id="elementList" name="ELEMENT_LIST" type="java.util.ArrayList" scope="request" />
		</logic:notEmpty>
		<logic:notEmpty name="elementList" >
			<logic:iterate name="elementList" id="report" indexId="i" type="com.ibm.km.dto.KmElementMstr">
			<%	
	 		String cssName = "";				
			if( i%2==1)
			{			
			cssName = "alt";
			}	
			%>
			<tr  class="<%=cssName %>"> 
					<TD class="text" height="25" >&nbsp;<%=(i.intValue() + 1)%>.</TD>
					<TD class="text" align="left"><bean:write name="report"
						property="elementName" /></TD>
					<TD class="text" ><bean:write name="report" property="path"/> </TD>
					<TD class="text" ><A Href="#" onclick="javascript: editSubmit('<bean:write name="report" property="elementId"/>');"><FONT color="red"><U><bean:message key="editElement.edit"/></U></FONT></A></TD>
					<TD class="text" ><A Href="#" onclick="javascript: return deleteSubmit('<bean:write name="report" property="elementId"/>');"><FONT color="red"><U>Delete</U></FONT></A></TD>
				</TR>
			</logic:iterate>
		</logic:notEmpty>
		<logic:empty name="elementList" >
			<TR class="lightBg">
				<TD colspan="2" class="error"  align="left"><bean:message key="viewAllFiles.NotFound" /></TD>
			</TR>
		</logic:empty>
	</table>
	</logic:notEqual>
<script>
loadCircle();
</script>
</div>
</div>
</html:form>
