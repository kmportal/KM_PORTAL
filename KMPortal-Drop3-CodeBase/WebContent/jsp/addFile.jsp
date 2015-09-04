<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<script language="javascript">
var path = '<%=request.getContextPath()%>';
var port = '<%= request.getServerPort()%>';
var serverName = '<%=request.getServerName() %>';

	function validate()
	{
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;
		
		var reg=/^[0-9a-zA-Z_ ]*$/;
		
		if(document.forms[0].newFile.value == "")
		{
			alert("Please Select File to be uploaded");
			return false;
		}
		if(isEmpty(document.forms[0].docDisplayName)){
			alert("Please enter document title");
			return false;	
		} 
		else if(!reg.test(document.forms[0].docDisplayName.value))
		{
		alert("Special characters except underscore not allowed in Document Title");
		return false;	
	   }	
	//added publishing date and publishing end date
	
	else if(document.forms[0].publishDate.value == "")
		{
			alert("Please enter the date of publishing");
			return false;			
		}
		else if(document.forms[0].publishDate.value < curr_dt)
		{
			alert("Publish Date cannot be a past date (day before today) ");
			return false;			
		}else if(document.forms[0].publishEndDate.value == "")
		{
			alert("Please enter the end date of publishing");
			return false;			
		}
		else if(document.forms[0].publishEndDate.value < document.forms[0].publishDate.value)
		{
			alert("Publishing Date should not be greater than Publishing End Date ");
			return false;			
		}
		else
		{
			var fileName = document.forms[0].newFile.value;
			var extn = fileName.substring(fileName.lastIndexOf(".")+1);
		}
		//Phase 2 changes
		if(extn == "HTML" || extn == "html" || extn == "HTM" || extn == "htm" ||  extn == "xls" || extn == "doc" || extn == "docx" || extn == "DOCX" || extn == "xlsx" || extn == "XLSX" || extn == "pdf" || extn == "XLS" || extn == "DOC" || extn == "PDF" || extn == "TXT" || extn == "txt"|| extn == "MHT" || extn == "mht")
		{
			return true;
		}
		else
		// Change Made Against DefectId MASDB00105316
		{  
			alert("File Type Not Supported. The File types supported are doc,xls,pdf,mht,txt,docx,xlsx and html.");
			return false;
		}
		
	}
	function formSubmit()
	{
		$id("documentPath").value=elementPath;
		if(validate())
		{
			document.forms[0].methodName.value = "insertFile";
			return true;
		}
		else
		{
			return false;
		}
	}
	
	function showStatus()
	{
	//	document.forms[0].newFile.value = "C:\MiniSef Security\Revised\Annexure15-mAOL.xls";
		var sta = document.forms[0].status.value;
		
		if(sta=='L'){
			alert("File can only be uploaded below circle level")
			document.forms[0].status.value='';
			document.forms[0].keyword.value="";
			document.forms[0].docDesc.value="";
			document.forms[0].publishDate.value="";
			document.forms[0].publishEndDate.value="";
			document.forms[0].docDisplayName.value="";
			return false;
		}
		if(sta == 'P')
			{
				if (confirm("File with same name already exists in Pending state!!! Do you want to update?"))
				{
						document.forms[0].methodName.value = "insertFile";
						document.forms[0].submit();
				}else{
					document.forms[0].status.value='';
					document.forms[0].keyword.value="";
					document.forms[0].docDesc.value="";
					document.forms[0].publishDate.value="";
					document.forms[0].publishEndDate.value="";
					document.forms[0].docDisplayName.value="";
					return false;
				}
			}
			else if(sta == 'A')
			{
				if (confirm("File with same name already exists in Approved state!!! Do you want to create another Version?"))
				{
						document.forms[0].methodName.value = "insertFile";
						document.forms[0].submit();
				}else{
					document.forms[0].status.value='';
					document.forms[0].keyword.value="";
					document.forms[0].docDesc.value="";
					document.forms[0].publishDate.value="";
					document.forms[0].publishEndDate.value="";
					document.forms[0].docDisplayName.value="";
					return false;	
				}
			}						
	}

var req=null; 
var ctr=1;

var levelCount=0;
var initialElementPath='<bean:write scope="request" name="allParentIdString"/>';
var elementPath=initialElementPath;
var initialParentId=<bean:write property="parentId" name="kmAddFileFormBean"/>;
var parentId=initialParentId;
var initialLevel=<bean:write property="initialLevel" name="kmAddFileFormBean"/>;
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
//Change start by Vishi
function loadDropdown(e)
//Change end by Vishi
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
		        var text=" Select "+elementLevelNames[level];
		        var newNode = document.createTextNode(text);
		        td1.setAttribute("align","left");
		        td1.setAttribute("style", "font-weight: BOLD");
		        
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
<html:form action="/addFile" enctype="multipart/form-data" >
<html:hidden property="methodName"/>
<html:hidden property="status"/>
<html:hidden property="completeFileName"/>
<html:hidden property="documentPath" styleId="documentPath"/>
<html:hidden property="elementId"/>
<html:hidden property="circleId"/>

<div class="box2">
  <div class="content-upload">
  <input type="hidden" name="levelCount" id="levelCount">
  <h1><bean:message key="addFile.heading"/></h1>
  <table width="100%" align="center" border="0" cellspacing="0" cellpadding="0">
		<tr>
			<td colspan="2" align="center" class="error">
				<strong><html:messages id="msg" message="true"><bean:write name="msg"/></html:messages></strong>
		    </td>
		</tr>
		<tr>
		  <td colspan="2" align="center" class="error">
		  		<strong><font color=red> <html:errors /></font></strong>
		  </td>
		  <tr>
		<logic:notEmpty name="kmAddFileFormBean" property="circleList" >
		  <bean:define id="displayText1" value="Uploaded For Circles :" />
		 <bean:write name="displayText1" />
		</logic:notEmpty>
		<logic:iterate name="kmAddFileFormBean" property="circleList" id="i">
<bean:write name="i"  />
 </logic:iterate>
		 </tr>
		</tr>
		
  </table>
  <table class="form1" id="table_0" border="0"  cellpadding="0" cellspacing="15">
   <tbody>
	<tr>
	  <td width="145"><span class="text2"><strong>
	  	    Select <script type="text/javascript">document.write(elementLevelNames[initialLevel]);</script> </strong></span>
	  </td>
	  <td>
         <!--Change start by Vishi   -->
          <html:select property="initialSelectBox" styleClass="select1" styleId="initialSelectBox" onchange="javascript:loadDropdown(event);" >
          <!--Change end by Vishi   -->
         	<html:option value="">Select</html:option>
         	 <logic:present name="firstList" scope="request">
         	  <html:options collection="firstList" property="elementId" labelProperty="elementName" />
         	 </logic:present>
          </html:select>
          <td>
            <INPUT type="checkbox" name="lobCheck" >
          <bean:define id="displayText" value="LOB WISE" />
		 <bean:write name="displayText" />
            </td>
           
       </td>
     </tr>
    </tbody>
  </table>
	<ul class="list2 form1">
		<li class="clearfix alt">
		   <span class="text2 fll width160"><bean:message key="addfile.selectFile" /></span>
		     <p class="clearfix fll margin-r20"> <span class="textbox6">
                  <input type="file" name="newFile" /></span> <h5><bean:message key="file.add"/> </h5></p>                   
		</li> 
		<li class="clearfix" id="element">
			<span class="text2 fll width160"><strong><bean:message key="addfile.docTitle" /></strong></span>
			  <p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner">
                    	<html:text property="docDisplayName" styleClass="textbox77" size="25" maxlength="150" />
                    </span> </span> </p>
		</li>
        <li class="clearfix alt" id="element">
		 <span class="text2 fll width160"><strong><bean:message key="addfile.docDesc" /> </strong></span>
		  <p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner">
                   	<html:text property="docDesc" styleClass="textbox77" size="25" maxlength="150"/>
                   </span> </span> </p>
		</li>
		<li class="clearfix" id="element">
			<span class="text2 fll width160"><strong><bean:message key="addfile.keyword" /></strong></span>
			 <p class="clearfix fll margin-r20"> <span class="textbox6"> <span class="textbox6-inner">
                  
<!--	Increase keyword limit to 200 characters in add new document : defect no. MASDB00060758 -->                      
                  	<html:text property="keyword"  styleClass="textbox77" size="25" maxlength="200"/>
                  </span> </span> </p>
		</li>
<!--	added publishing date and publishing end date -->                    
	  
		<li class="clearfix alt" id="element">
			<span class="text2 fll width160"><strong><bean:message key="addfile.publishDate" /> </strong></span>		
			<input type="text" class="tcal calender2 fll"  readonly="readonly"  name="publishDate" value="<bean:write property='publishDate'name='kmAddFileFormBean'/>"/>
			<span class="text2 fll width160" style="margin-left: 150px;"><strong><bean:message key="addfile.publishDate_end" /> </strong></span>
			<input type="text" class="tcal calender2 fll"   readonly="readonly"  name="publishEndDate" value="<bean:write property='publishEndDate' name='kmAddFileFormBean'/>"/>
		</li>
		<li class="clearfix alt" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
                  <INPUT type="Image" src="images/submit.jpg" onclick="javascript: return formSubmit();">
        </li>
   	 </ul> 
   <br>

  <script> 
  	showStatus(); 
  </script>
 </div>
    <jsp:include page="Disclaminer.jsp"></jsp:include>
</div>
</html:form>