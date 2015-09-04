<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html>
<HEAD>
<LINK href="./theme/text.css" rel="stylesheet" type="text/css">
<TITLE>Csv Bulk Upload</TITLE>


<script language="javascript" src="jScripts/KmValidations.js">
var path = '<%=request.getContextPath()%>';
var port = '<%= request.getServerPort()%>';
var serverName = '<%=request.getServerName() %>';
</script>
<script language="JavaScript" type="text/JavaScript">
function limitText(textArea, length) {
if (textArea.value.length > length) {
alert ("Please limit comments length to "+length+" characters.");
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
function openFile(fileName){
window.location.href="kmUploadDetails.do?methodName=openFile&fileName="+fileName;
}
function formsubmit(){
  		$id("documentPath").value=elementPath;	
		var today = new Date();
		var dd = today.getDate();
		var mm = today.getMonth()+1;//January is 0!
		var yyyy = today.getFullYear();
		if(dd<10){dd='0'+dd}
		if(mm<10){mm='0'+mm}
		var curr_dt=yyyy+'-'+mm+'-'+dd;
		
		var reg=/^[0-9a-zA-Z_ ]*$/;
		
		
	//added publishing date and publishing end date
		if(document.forms[0].publishDate.value < curr_dt)
		{
			alert("Publish Date cannot be a past date (day before today) ");
			return false;			
		}
		else if(document.forms[0].publishEndDate.value < document.forms[0].publishDate.value)
		{
			alert("Publishing Date should not be greater than Publishing End Date ");
			return false;			
		}
	if(document.bulkUploadDetailsFormBean.docType.value=="0" ){
			alert("Please Select Document Type to be uploaded"); 
			return false;
	} 
	
	if(document.bulkUploadDetailsFormBean.newFile.value==""){
		
			alert("Please Select File"); 
				
			return false;
		
		
	} 
	
	
	 if(document.bulkUploadDetailsFormBean.uploadType.value=="0"){
		alert("Please Select the purpose of uploading");
		return false;
	}
	else if(document.bulkUploadDetailsFormBean.newFile.value==""){
		alert("Please Select the file");
		return false;
	}else if(!(/^.*\.csv$/.test(document.bulkUploadDetailsFormBean.newFile.value))) {
			//Extension not proper.
			alert("Please select a .csv file only.");
			return false;
				
	}else if(navigator.userAgent.indexOf("MSIE") != -1) {
		if(document.bulkUploadDetailsFormBean.newFile.value.indexOf(":\\") == -1 ) {
			alert("Please Select a proper file.");
			return false;
		}
	}else{  
		document.forms[0].methodName.value='uploadFile';
		return true;
	}
	
}
var req=null; 
var ctr=1;

var levelCount=0;
var initialElementPath='<bean:write scope="request" name="allParentIdString"/>';
var elementPath=initialElementPath;
var initialParentId='<bean:write property="parentId" name="bulkUploadDetailsFormBean"/>';
var parentId=initialParentId;
var initialLevel='<bean:write property="initialLevel" name="bulkUploadDetailsFormBean"/>';
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
		for (i=parseInt(initialLevel)+1;i<=parseInt(levelCount);i++) {
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
        	
            var json = eval('(' + req.responseText + ')');
            
			var elements = json.elements;
			level = json.level;
			childLevel=level;
			
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
</HEAD>
  					


<BODY>

<html:form action="/kmUploadDetails" enctype="multipart/form-data" onsubmit="return formsubmit();" >
	
	<html:hidden name="bulkUploadDetailsFormBean" property="methodName" value="uploadFile"/>
	<html:hidden name="bulkUploadDetailsFormBean" property="loginActorId" />
	<input type="hidden" name="levelCount" id="levelCount">
	<html:hidden property="documentPath" styleId="documentPath"/>
	<!--  
	<html:hidden name="bulkUploadDetailsFormBean" property="circleElementId"/>
	-->		
		<div class="box2">
        <div class="content-upload">
        
        <H1>Bulk Details Uploading</H1>
        
			
			<TABLE border="0" align="center" width="100%">
			<tr><td width="258"><br><br></td></tr>
			<tr align="center">
			
			<td colspan="2"  class="error">
			<strong> 
			<tr style="color: red">
           	<html:messages id="msg" message="true">
                <bean:write name="msg"/>  
             </html:messages>
             
             <logic:equal name="bulkUploadDetailsFormBean" property="errorFlag" value="true">
			  <font size="2"> <a href="./kmUploadDetails.do?methodName=openErrLog&filePath=<%=request.getAttribute("filePath")%>" target="_new">click here</a></font>
		    </logic:equal>
             </tr>
            </strong>
            </td>
		   </tr>
			<TR align="center"><TD  colspan="2" >
			
			<font color="RED" ><strong>
						
						 
					<html:errors/>
	 
				</strong></font>
			<TD ></TR>
			
		</TABLE>	
			
			
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
         
       </td>
     </tr>
    </tbody>
  </table>
          	 <ul class="list2 form1 ">	
			<li class="clearfix">
			
			<span class="text2 fll width160"><strong>Select Document Type:</strong> </span>
			
			<p class="clearfix fll margin-r20"> 
				
				<html:select  property="docType" styleClass="select1 fll width180">
					<option selected="selected" value="0">-- Select Document Type --</option>
					<option value="1">Distributor Details </option>
					<option value="2">ARC Details </option>
					<option value="3">Coordinator Details</option>	
					<option value="4">Other SMS</option>	
				</html:select>
				
			 </p>
			
			</li>
			
			
			 <li class="clearfix alt" >
				<span class="text2 fll width160"><strong>Select File: </strong> </span>	

				<p class="clearfix fll margin-r20"> 
				   <html:file property="newFile"/> 
				</p>		
			  </li>
			  <li class="clearfix alt" id="element">
			<span class="text2 fll width160"><strong><bean:message key="addfile.publishDate" /> </strong></span>		
			<input type="text" class="tcal calender2 fll"  readonly="readonly"  name="publishDate" value="<bean:write property='publishDate'name='bulkUploadDetailsFormBean'/>"/>
			<span class="text2 fll width160" style="margin-left: 150px;"><strong><bean:message key="addfile.publishDate_end" /> </strong></span>
			<input type="text" class="tcal calender2 fll"   readonly="readonly"  name="publishEndDate" value="<bean:write property='publishEndDate' name='bulkUploadDetailsFormBean'/>"/>
		</li>
				   
	   <li class="clearfix" style="padding-left:170px;">
			<span class="text2 fll">&nbsp;</span>
			<input type="image" src="images/submit.jpg" style="margin-right:10px;" onclick="return formsubmit();" styleClass="red-btn fll"/>&nbsp;&nbsp;&nbsp;&nbsp;	
		</li>
					
	
			</ul>
			
			
	</div>
	</div>		

			
</html:form>
</BODY>
</html:html>

