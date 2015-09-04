
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<script language="JavaScript" src="jScripts/KmValidations.js"
	type="text/javascript">
</script>
	
 
<script language="javascript">  
function resetFields(){
	document.forms[0].initialSelectBox.value="-1";
	document.forms[0].elementName.value="";
	document.forms[0].elementDesc.value="";
	return false;
}

function formValidate(){
	$id("parentId").value=parentId;
	$id("elementFolderPath").value=elementPath;
	if(validate()){
		document.forms[0].methodName.value = "insert";
		return true;
	}else{
		return false;
	}
	
}

function validate(){
	var form=document.forms[0];
	return true;
}


</script>
<script language="javascript">
	var path = '<%=request.getContextPath()%>';
	var port = '<%= request.getServerPort()%>';
	var serverName = '<%=request.getServerName() %>';
</script>
<SCRIPT language="JavaScript">

var initialElementPath='<bean:write scope="request" name="allParentIdString"/>';
var initialParentId=<bean:write property="parentId" name="kmElementMstrFormBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmElementMstrFormBean"/>;
alert(initialLevel);
var elementLevelNames=new Array();

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

var level= initialLevel;

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
	  addOption("-1",'-- All Circles --', circelDropDown);
	  
  	 for (var i = 1; i < obj.options.length; i++) 
  	 	{ 
           addOption(obj.options[i].value, obj.options[i].text, circelDropDown); 
        }
       
  	}//for LOB User Only
  	
  }

function newXMLHttpRequest() {

    var xmlreq = false;

    if (window.XMLHttpRequest) {
        xmlreq = new XMLHttpRequest();

    } else if (window.ActiveXObject) {

        try {
            xmlreq = new ActiveXObject("Msxml2.XMLHTTP");

        } catch (e1) {
            try {
                xmlreq = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e2) {
            }
        }
    }
    return xmlreq;
}

function loadDropdown()
{
	var currentElementLevel = level;
		
		if(currentElementLevel <= 3)
		{
			document.getElementById("circleLabel").innerHTML = "";
			document.getElementById("circleLabel").style.display = "none";
			document.getElementById("circleSelect").style.display = "none";
		}
		
	parentId=$id("initialSelectBox").value;
	
	if (parentId!="") {
	
		var selectedItem = document.getElementById('initialSelectBox').options[document.getElementById('initialSelectBox').selectedIndex].text;
		selectedItem = selectedItem + " >> ";
		var labelUpdate = "label_"+currentElementLevel;
		$id(labelUpdate).innerHTML = selectedItem; 
		$id(labelUpdate).style.display = 'inline';
		$id(labelUpdate).title = parentId;
			
	    req = newXMLHttpRequest();
	    if (!req) {
	        alert("Your browser does not support AJAX! Add Files module is accessible only by browsers that support AJAX. " +
	              "Please check the KM requirements and contact your System Administrator");
	        return;
	    }
	    
	    req.onreadystatechange = returnJson;
	    var url=path+"/elementAction.do?methodName=loadElementDropDown&ParentId="+parentId+"&Dummy="+new Date().getTime();
	    req.open("GET", url, true);
	    req.send(null);
	}
}

function returnJson() {
    if (req.readyState == 4) {
        if (req.status == 200) {
            var json = eval('(' + req.responseText + ')');
            
			var elements = json.elements;
			
		if (elements.length!=0)
		 {
			level = json.level;
			var maxlevel=elementLevelNames.length-1;
			var currentLevel=parseInt(level)+1;
			if(currentLevel>maxlevel)
			{
			 	alert("Element cannot be Selected Beyond the Maximum Level.");
			 	return;
			}
						
			if(level >= 4  && initialLevel <= 3 )
		      {
		        document.getElementById("circleLabel").innerHTML = "Circles --> ";
		        document.getElementById("circleLabel").style.display="inline";
		        document.getElementById("circleSelect").style.display="inline";
		       }
		       else
		       {
				document.getElementById("circleLabel").innerHTML = "";
				document.getElementById("circleLabel").style.display = "none";
				document.getElementById("circleSelect").style.display = "none";
			   }
	         
	         		var addOption = function (value, text, selectBox){
	                var optn = document.createElement("OPTION");
	                optn.text = text;
	                optn.value = value;
	                selectBox.options.add(optn);
	            	}
	                
	            var selectDropDown = document.getElementById("initialSelectBox");
	            var circelDropDown = document.getElementById("createMultiple");
	            
	            cleanSelectBox("initialSelectBox");
	            var opt = "-- Select" + elementLevelNames[level] + " --";
				addOption("",opt, selectDropDown);
				for (var i = 0; i < elements.length; i++) {
		            addOption(elements[i].elementId, elements[i].elementName, selectDropDown);
		        }
		        if(level == 3)
		        {
		        	cleanSelectBox("createMultiple");
		        	addOption("-1",'-- All Circles --', circelDropDown);
		        	for (var j = 0; j < elements.length; j++) {
		            addOption(elements[j].elementId, elements[j].elementName, circelDropDown);
		        	}
		        }
		        var text="Select "+elementLevelNames[level]+" --> ";
		        $id("elementLabel").innerHTML = text ;
		    }
		    
        }
       }
        
    }
   
 function switchElement(selectedLabel)
  {
  	var labelParent = "";
  	var labelId;
  	selectedLabel = selectedLabel.id;
  	var len = selectedLabel.length;
  	var labelNo = selectedLabel.substring((len-1),len);
  	var parentLabelNo = parseInt(labelNo)-1 ;
  	labelParent = "label_"+parentLabelNo;
  	
  	if(labelParent == "label_1")
  		labelParent = 1;
  	else if(initialLevel == 3 && labelParent == "label_2" )
  		labelParent = initialParentId;
  	else if(initialLevel == 4 && labelParent == "label_3" )
  		labelParent = initialParentId;
  	else
  		labelParent = $id(labelParent).title;
  	
	if (labelParent!="") {
	
		for(var i=labelNo ; i <= 9 ; i++)
		{
			labelId = "label_" + i ;
			$id(labelId).innerHTML = ""; 
			$id(labelId).style.display = 'none';
			$id(labelId).title = "";	
		}
			
	    req = newXMLHttpRequest();
	    if (!req) {
	        alert("Your browser does not support AJAX! Add Files module is accessible only by browsers that support AJAX. " +
	              "Please check the KM requirements and contact your System Administrator");
	        return;
	    }
	    
	    req.onreadystatechange = returnJson;
	    var url=path+"/elementAction.do?methodName=loadElementDropDown&ParentId="+labelParent+"&Dummy="+new Date().getTime();
	    req.open("GET", url, true);
	    req.send(null);
	}
  	
  }
  
   function cleanSelectBox(selectBox)
  {
  	var obj = document.getElementById(selectBox);
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

loadCircle();
</script>



<FONT color="red"><html:errors/></FONT>
<html:form action="/elementAction" >
	<html:hidden property="methodName" value="insert"/>
	<html:hidden property="parentId" styleId="parentId"/>
	<html:hidden property="elementLevel" styleId="elementLevel"/>
	<html:hidden property="elementFolderPath" styleId="elementFolderPath"/>
	<html:hidden property="token" styleId="token"/>
	<BR><BR><BR><BR>
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
		   
		   <tr><td colspan="8">
		   <label id="label_2" title="" onclick="switchElement(this)" style="display:none"> </label> 
		   <label id="label_3" title="" onclick="switchElement(this)" style="display:none"> </label> 
		   <label id="label_4" title="" onclick="switchElement(this)" style="display:none"> </label> 
		   <label id="label_5" title="" onclick="switchElement(this)" style="display:none"> </label> 
		   <label id="label_6" title="" onclick="switchElement(this)" style="display:none"> </label> 
		   <label id="label_7" title="" onclick="switchElement(this)" style="display:none"> </label> 
		   <label id="label_8" title="" onclick="switchElement(this)" style="display:none"> </label> 
		   <label id="label_9" title="" onclick="switchElement(this)" style="display:none"> </label>
		   </td>
		   </tr>
                    <tr><td colspan="4">
				<table id="table_0" width="99%">
					<tr>
						<td width="184"> <div id="elementLabel"> Select <script
							type="text/javascript">document.write(elementLevelNames[initialLevel]);</script>
						--&gt; </div></td>
						<td style="width: 238px" align="left" class="pBot2" width="194">
						<html:select property="initialSelectBox" styleId="initialSelectBox" 
						onchange="javascript:loadDropdown();">
							<html:option value=""> -- Select <script type="text/javascript">
							document.write(elementLevelNames[initialLevel]);</script> -- </html:option>
							<logic:present name="firstList" scope="request">
								<html:options collection="firstList" property="elementId"
									labelProperty="elementName" />
							</logic:present>
						</html:select></td>
						<td width="69"> 
						<label id="circleLabel" title="Circle" style="display:none;"> </label>
						</td>
						<td width="248" rowspan = "3">
						<div id="circleSelect" style="display:none;">
						<html:select property="createMultiple" styleId="createMultiple"
							multiple="multiple" onchange="javascript:selectAll();">
						</html:select>
						</div> </td>
					</tr>
				</table>
				</td>
                    </tr>

			
		</TBODY>
	</TABLE>
	
</html:form>

